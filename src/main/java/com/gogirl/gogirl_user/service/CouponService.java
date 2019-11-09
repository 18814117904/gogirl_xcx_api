package com.gogirl.gogirl_user.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import antlr.collections.impl.LList;

import com.gogirl.gogirl_order.order_commons.utils.JsonResult;
import com.gogirl.gogirl_user.constant.GogirlProperties;
import com.gogirl.gogirl_user.dao.CouponCustomerRelevanceMapper;
import com.gogirl.gogirl_user.dao.CouponMapper;
import com.gogirl.gogirl_user.dao.DiscountConfigMapper;
import com.gogirl.gogirl_user.entity.Coupon;
import com.gogirl.gogirl_user.entity.CouponCustomerRelevance;
import com.gogirl.gogirl_user.entity.Customer;
import com.gogirl.gogirl_user.entity.CustomerBalance;
import com.gogirl.gogirl_user.entity.DiscountConfig;
import com.gogirl.gogirl_user.service.discount.Discount;
import com.gogirl.gogirl_user.service.myhttp.MyHttpPost;

@Service
public class CouponService {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	@Resource
	CouponMapper couponDao;
	@Resource
	CouponCustomerRelevanceMapper couponCustomerRelevanceDao;
	@Resource
	public RestTemplate restTemplate;
	@Resource
	GogirlProperties gogirlProperties;
	@Resource
	CouponService couponService;
	@Resource
	MyHttpPost myHttpPost;
	public Coupon sendTicketAfterOrder(Integer customerId,Double orderAmount){
		Coupon c = null;
    	//根据订单金额插入优惠券
    	if(orderAmount>=300){
    		logger.info("送50券");
    		c = couponService.selectByPrimaryKey(36);
    	}else if(orderAmount>=200){
    		logger.info("送30券");
    		c = couponService.selectByPrimaryKey(37);
    	}else if(orderAmount>=100){
    		logger.info("送10券");
    		c = couponService.selectByPrimaryKey(38);
    	}else{
    		logger.info("不送券");
    		return null;
    	}
		CouponCustomerRelevance record = new CouponCustomerRelevance();
		record.setCustomerId(customerId);
		record.setCouponId(c.getId());
		record.setState(1);
		record.setCode(getRandomCode());
		record.setReceiveTime(new Date());
		
		if(c.getValidType()==1){
			record.setValidStartTime(c.getValidStartTime());//优惠券原有开始和结束时间
			record.setValidEndTime(c.getValidEndTime());
		}else if(c.getValidType()==2){
			long today = new Date().getTime();
			long day = today+new Long(86400000)*c.getValidDate();
			record.setValidStartTime(new Date(today));//从现在开始
			record.setValidEndTime(new Date(day));//n天后过期
		}
		
		Coupon changeQuantity = new Coupon();
		changeQuantity.setId(record.getCouponId());
		changeQuantity.setReceiveQuantity(c.getReceiveQuantity()+1);
		couponDao.updateByPrimaryKeySelective(changeQuantity);
		
		couponCustomerRelevanceDao.insertSelective(record);
		return c;
	}
	
	//提前写好发送优惠券+发送消息模板+点击消息模板去到小程序我的优惠券
	public JsonResult sendTicketAndSendMaMsg(Coupon coupon,Customer customer){
		if(coupon==null){
			return new JsonResult(false,"找不到该优惠券",null);
		}
		//判断优惠券是否还有
		if(coupon.getAllQuantity()<=coupon.getReceiveQuantity()){
			return new JsonResult(false,"优惠券已经领完",null);
		}
		//判断该用户是否限领,null过,0过,有但限制不过,不限制过
		CouponCustomerRelevance record = new CouponCustomerRelevance();
		record.setCustomerId(customer.getId());
		record.setCouponId(coupon.getId());
		int row = couponService.countRelevanceNum(record);
		if(coupon.getLimitQuantity()!=null&&coupon.getLimitQuantity()!=0&&coupon.getLimitQuantity()<=row){
			return new JsonResult(false,"抱歉,该优惠券最多可领取"+coupon.getLimitQuantity()+"张,你已有"+row+"张该优惠券.",null);
		}
		
		CouponCustomerRelevance couponCustomerRelevance= new CouponCustomerRelevance();
		couponCustomerRelevance.setCustomerId(customer.getId());
		couponCustomerRelevance.setCouponId(coupon.getId());
		couponCustomerRelevance.setState(1);
		couponCustomerRelevance.setCode(couponService.getRandomCode());
		couponCustomerRelevance.setReceiveTime(new Date());
		if(coupon.getValidType()==1){
			couponCustomerRelevance.setValidStartTime(coupon.getValidStartTime());//优惠券原有开始和结束时间
			couponCustomerRelevance.setValidEndTime(coupon.getValidEndTime());
		}else if(coupon.getValidType()==2){
			long today = new Date().getTime();
			long day7 = today+new Long(86400000);
			couponCustomerRelevance.setValidStartTime(new Date(today));//从现在开始
			couponCustomerRelevance.setValidEndTime(new Date(day7));//七天后过期
		}
		int id = couponService.insertSelective(coupon,couponCustomerRelevance);
		if(id>0){
			Coupon changeQuantity = new Coupon();
			changeQuantity.setId(record.getCouponId());
			changeQuantity.setReceiveQuantity(coupon.getReceiveQuantity()+1);
			couponDao.updateByPrimaryKeySelective(changeQuantity);
		}
		
    	//发送模板消息
		Map<Integer, String> mapType = new HashMap<>();
		mapType.put(1, "现金抵扣券");
		mapType.put(2, "免单券");
		mapType.put(3, "满减券");
		Map<String, Object> mapParm	= new HashMap<>();
		mapParm.put("openid", customer.getOpenid());
		mapParm.put("storeName", "gogirl美甲美睫沙龙所有门店");
		mapParm.put("type", mapType.get(coupon.getType()));
		mapParm.put("amount", coupon.getName()==null?coupon.getDiscountAmount()+"元":coupon.getName());
		mapParm.put("code", "无需验证");
		myHttpPost.httpRequest(gogirlProperties.getGOGIRLMP()+"gogirl_mp/wx/template/getTicketMsg", mapParm);
		return new JsonResult(true, JsonResult.APP_DEFINE_SUC, id);
	}
	
	
	public List<Coupon> selectByCoupon(Coupon coupon){
		return couponDao.selectByCoupon(coupon);
	}
	public int updateByPrimaryKeySelective(Coupon record){
		return couponDao.updateByPrimaryKeySelective(record);
	}
	public int insertSelective(Coupon record){
		if(record.getState()==null){
			record.setState(1);
		}
		record.setUpdateTime(new Date());
		return couponDao.insertSelective(record);
	}
	public Coupon selectByPrimaryKey(Integer id){
		return  couponDao.selectByPrimaryKey(id);
	}
	public Coupon selectByCouponCustomerRelevance(Integer id){
		return  couponDao.selectByPrimaryKey(id);
	}
	public CouponCustomerRelevance selectRelevanceByPrimaryKey(Integer id){
		return  couponCustomerRelevanceDao.selectByPrimaryKey(id);
	}
    public List<CouponCustomerRelevance> selectByCouponCustomerRelevance(CouponCustomerRelevance record,String phone,String username){
    	return couponCustomerRelevanceDao.selectByCouponCustomerRelevance(record, phone, username);
    }
    public List<CouponCustomerRelevance> selectMyCoupon(Integer customerId){
    	return couponCustomerRelevanceDao.selectMyCoupon(customerId);
    }
    public List<Coupon> getAllUseCoupon(){
    	return couponDao.getAllUseCoupon();
    }
    
	public int updateRelevanceByPrimaryKeySelective(CouponCustomerRelevance record){
		//查询优惠券状态是否从未使用到已使用,若是,则加次数
		if(record!=null&&record.getState()!=null&&record.getState()==2){
			CouponCustomerRelevance ccr = couponCustomerRelevanceDao.selectByPrimaryKey(record.getId());
			if(ccr!=null&&ccr.getState()!=null&&ccr.getState()==1){
				Coupon c= couponDao.selectByPrimaryKey(ccr.getCouponId());
				if(c==null){
					return 0;
				}
				Coupon changeQuantity = new Coupon();
				changeQuantity.setId(record.getCouponId());
				changeQuantity.setUseQuantity(c.getUseQuantity()+1);
				couponDao.updateByPrimaryKeySelective(changeQuantity);
			}
			record.setUseDate(new Date());
		}
		
		return couponCustomerRelevanceDao.updateByPrimaryKeySelective(record);
	}
	public int countRelevanceNum(CouponCustomerRelevance record){
		return couponCustomerRelevanceDao.countRelevanceNum(record);
	}
	
	public int insertSelective(Coupon coupon,CouponCustomerRelevance record){
		if(record.getState()==null){
			record.setState(1);
		}
		if(record.getCode()==null){
			record.setCode(getRandomCode());
		}
		if(record.getReceiveTime()==null){
			record.setReceiveTime(new Date());
		}
		if((record.getValidStartTime()==null||record.getValidEndTime()==null)&&coupon!=null){
			if(coupon.getValidType()==1){
				record.setValidStartTime(coupon.getValidStartTime());//优惠券原有开始和结束时间
				record.setValidEndTime(coupon.getValidEndTime());
			}else if(coupon.getValidType()==2){
				long today = new Date().getTime();
				long day7 = today+new Long(86400000)*coupon.getValidDate();
				record.setValidStartTime(new Date(today));//从现在开始
				record.setValidEndTime(new Date(day7));//七天后过期
			}
		}
		
		
		//修改已领取人数
		Coupon c= couponDao.selectByPrimaryKey(record.getCouponId());
		Coupon changeQuantity = new Coupon();
		changeQuantity.setId(record.getCouponId());
		changeQuantity.setReceiveQuantity(c.getReceiveQuantity()+1);
		couponDao.updateByPrimaryKeySelective(changeQuantity);
		
		
		couponCustomerRelevanceDao.insertSelective(record);
		return record.getId();
	}
	public int insertSelectiveList(Coupon coupon,List<CouponCustomerRelevance> list,Integer amount){
		for(int i=0;i<list.size();i++){
			CouponCustomerRelevance record = list.get(i);
			if(record.getState()==null){
				record.setState(1);
			}
//			if(record.getCode()==null){//不需要code
//				record.setCode(getRandomCode());
//			}
			if(record.getReceiveTime()==null){
				record.setReceiveTime(new Date());
			}
			if((record.getValidStartTime()==null||record.getValidEndTime()==null)&&coupon!=null){
				if(coupon.getValidType()==1){
					record.setValidStartTime(coupon.getValidStartTime());//优惠券原有开始和结束时间
					record.setValidEndTime(coupon.getValidEndTime());
				}else if(coupon.getValidType()==2){
					long today = new Date().getTime();
					long day7 = today+new Long(86400000)*coupon.getValidDate();
					record.setValidStartTime(new Date(today));//从现在开始
					record.setValidEndTime(new Date(day7));//七天后过期
				}
			}
			list.set(i, record);
		}
		//修改已领取人数
		Coupon changeQuantity = new Coupon();
		changeQuantity.setId(coupon.getId());
		changeQuantity.setReceiveQuantity(coupon.getReceiveQuantity()+list.size()*amount);
		couponDao.updateByPrimaryKeySelective(changeQuantity);
		
		
		//批量插入到数据库
		int row = 0;
		for(int i=0;i<amount;i++){
			row+=couponCustomerRelevanceDao.insertSelectiveList(list);
		}
		return row;
	}
	public String getRandomCode(){
		String code = couponCustomerRelevanceDao.getRandomCode(Math.random());
		if(code!=null){
			couponCustomerRelevanceDao.deleteCode(code);
		}
		return code;
	}
    @Scheduled(cron="0 0 12 * * ?")//每天12点检查优惠券是否过期,消息模板提醒
//  @Scheduled(cron="0 * * * * ?")
  public void couponExpireRemind(){
  	logger.info("每天定时任务,查看优惠券是否即将过期,发起消息模板提醒");
  	DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
  	Date d7=new Date(new Date().getTime()+new Long("604800000"));//七天后的时间
  	List<CouponCustomerRelevance> list = couponCustomerRelevanceDao.select7DayExpireCoupon(df.format(d7));
  	List<Map<String, String>> dataList = new ArrayList<>();
  	//TODO 处理数据
  	for(int i=0;i<list.size();i++){
  		CouponCustomerRelevance item = list.get(i);
  		Map<String, String> map = new HashMap<String, String>();
  		if(item.getCustomer()==null||item.getCustomer().getOpenid()==null||item.getCustomer().getOpenid().isEmpty()){
  			continue;
  		}
  		map.put("openid", item.getCustomer().getOpenid());
  		map.put("type",item.getCoupon().getName() );
  		map.put("validityTime",df.format(item.getValidEndTime()));
  		map.put("surplusDate",df.format(item.getReceiveTime()));
  		dataList.add(map);
  	}
	restTemplate.postForObject(gogirlProperties.getGOGIRLMP()+"gogirl_mp/wx/template/ticketExpiresMsg?time=7天", 
			 dataList, JsonResult.class );
    }
    @Scheduled(cron="0 0 3 * * ?")
//  @Scheduled(cron="0 * * * * ?")
    public void setCouponExpireJob(){
      	logger.info("每天定时任务,判断优惠券是否过期");
      	couponCustomerRelevanceDao.setCouponExpire(new Date());
      }
	public String getCouponIdFromConfig(){
		String code = couponDao.getCouponIdFromConfig();
		return code;
	}

	public List<Coupon> getOrderCanUseCoupon(Integer orderId) {
		return couponDao.getOrderCanUseCoupon(orderId);
	}

	public List<CouponCustomerRelevance> selectMyNewCustomerCoupon(Integer customerId,List<Integer> coupontIdList) {
		return couponCustomerRelevanceDao.selectMyNewCustomerCoupon(customerId,coupontIdList);
	}

	public List<Coupon> getOrderCanUseCouponPart1(Integer orderId) {
		return couponDao.getOrderCanUseCouponPart1(orderId);
	}

	public List<Coupon> getOrderCanUseCouponPart2(Integer orderId) {
		return couponDao.getOrderCanUseCouponPart2(orderId);
	}
}
