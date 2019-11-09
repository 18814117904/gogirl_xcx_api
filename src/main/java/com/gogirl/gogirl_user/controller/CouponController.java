package com.gogirl.gogirl_user.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import springfox.documentation.annotations.ApiIgnore;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gogirl.gogirl_order.order_commons.dto.OrderManage;
import com.gogirl.gogirl_order.order_commons.dto.OrderServe;
import com.gogirl.gogirl_order.order_commons.utils.JsonResult;
import com.gogirl.gogirl_order.order_manage.service.OrderManageService;
import com.gogirl.gogirl_user.constant.GogirlProperties;
import com.gogirl.gogirl_user.entity.Coupon;
import com.gogirl.gogirl_user.entity.CouponCustomerRelevance;
import com.gogirl.gogirl_user.entity.Customer;
import com.gogirl.gogirl_user.service.CouponService;
import com.gogirl.gogirl_user.service.CustomerService;
import com.gogirl.gogirl_user.service.RedirectService;
import com.gogirl.gogirl_xcx.dao.GogirlConfigMapper;
import com.gogirl.gogirl_xcx.entity.GogirlConfig;
import com.gogirl.gogirl_xcx.entity.GogirlToken;
import com.gogirl.gogirl_xcx.service.GogirlTokenService;

@RestController
@Api(tags = { "8.优惠券" },value = "")
public class CouponController {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	@Resource
	CouponService couponService;
	@Resource
	CustomerService customerService;
	@Resource
	RedirectService redirectService;
	@Resource
	GogirlProperties gogirlProperties;
	@Resource
	public RestTemplate restTemplate;
	@Resource
	GogirlTokenService gogirlTokenService;
	@Resource
	OrderManageService orderManageService;
	@Resource
	GogirlConfigMapper gogirlConfigMapper;
	
	//查询优惠券详情
	@ApiOperation(value = "查询优惠券详情", notes = "")
	@RequestMapping(method={RequestMethod.GET},value="/selectCouponByPrimaryKey")
    public JsonResult selectCouponByPrimaryKey(Integer id){
		Coupon coupon = couponService.selectByPrimaryKey(id);
		if(coupon==null){
			return new JsonResult(false, "无该优惠券", null);
		}else{
			return new JsonResult(true, JsonResult.APP_DEFINE_SUC, coupon);
		}
    }
	@ApiOperation(value = "美甲师根据订单号查询可用外部券", notes = "")
	@RequestMapping(method={RequestMethod.GET},value="/getOrderExternalCoupon")
    public JsonResult<List<Coupon>> getOrderExternalCoupon(String token,Integer orderId,Integer serveType){
		logger.info("用户根据订单号查询可用优惠券");
		if(token==null){
			return new JsonResult<List<Coupon>>(false,String.format(JsonResult.PARAM_NULL, "token"),null);
		}
		GogirlToken gogirlToken=gogirlTokenService.getTokenByToken_t(token);
		if(gogirlToken==null){
			return new JsonResult<List<Coupon>>(false,JsonResult.TOKEN_NULL_CODE,null);
		}
		if(gogirlToken.getCustomerId()==null){
			return new JsonResult<List<Coupon>>(false,JsonResult.TOKEN_NULL_AUTHORIZED_CODE,null);
		}
 		
 		if(orderId==null){
 			return new JsonResult<>(false,"入参orderId为null",null);
 		}
 		OrderManage orderManage = orderManageService.getOrderForDetail(orderId);
 		if(orderManage==null){
 			return new JsonResult<>(false,"找不到订单",null);
 		}
		Integer customerId = orderManage.getOrderUser();
 		logger.info("查询领取优惠券的用户列表CouponCustomerRelevance:"+customerId.toString());
		BigDecimal totalPrice = orderManage.getTotalPrice()==null?new BigDecimal("0"):orderManage.getTotalPrice();
		BigDecimal changePrice = orderManage.getChangePrice()==null?new BigDecimal("0"):orderManage.getChangePrice();//.multiply(big100).toBigInteger();
		BigDecimal discountPrice = orderManage.getDiscountPrice()==null?new BigDecimal("0"):orderManage.getDiscountPrice();
		BigDecimal payPrice = totalPrice.add(changePrice);

 		//获得订单和服务可以使用的所有优惠券
 		List<Coupon> orderCanUseCouponList = couponService.getOrderCanUseCouponPart1(orderId);
 		List<Coupon> orderCanUseCouponListPart2 = couponService.getOrderCanUseCouponPart2(orderId);
 		Iterator<Coupon> itPart2 = orderCanUseCouponListPart2.iterator();
 		while(itPart2.hasNext()){
 			orderCanUseCouponList.add(itPart2.next());
 		}
 		
 		for(int i=orderCanUseCouponList.size()-1;i>=0;i--){
 			Coupon item = orderCanUseCouponList.get(i);
 			if(!item.getSourceType().equals(1)){//只保留外部券
 				orderCanUseCouponList.remove(item);
 				continue;
 			}
			//移除未满足满减条件的券
			if(item.getType().equals(3)){
				BigDecimal reachingAmount = new BigDecimal(item.getReachingAmount());
				if(payPrice.compareTo(reachingAmount)==-1){orderCanUseCouponList.remove(item);continue;}
			}
 		}
		return new JsonResult<List<Coupon>>(true, JsonResult.APP_DEFINE_SUC, orderCanUseCouponList);
	}
	@ApiOperation(value = "用户根据订单号查询可用优惠券", notes = "")
	@RequestMapping(method={RequestMethod.GET},value="/getOrderCoupon")
    public JsonResult<List<CouponCustomerRelevance>> getOrderCoupon(String token,Integer orderId){
		logger.info("用户根据订单号查询可用优惠券");
		if(token==null){
			return new JsonResult<List<CouponCustomerRelevance>>(false,String.format(JsonResult.PARAM_NULL, "token"),null);
		}
		GogirlToken gogirlToken=gogirlTokenService.getTokenByToken(token);
		if(gogirlToken==null){
			return new JsonResult<List<CouponCustomerRelevance>>(false,JsonResult.TOKEN_NULL_CODE,null);
		}
		if(gogirlToken.getCustomerId()==null){
			return new JsonResult<List<CouponCustomerRelevance>>(false,JsonResult.TOKEN_NULL_AUTHORIZED_CODE,null);
		}
		Integer customerId = gogirlToken.getCustomerId();
 		logger.info("查询领取优惠券的用户列表CouponCustomerRelevance:"+customerId.toString());
 		
 		if(orderId==null){
 			return new JsonResult<>(false,"入参orderId为null",null);
 		}
 		OrderManage orderManage = orderManageService.getOrderForDetail(orderId);
 		if(orderManage==null){
 			return new JsonResult<>(false,"找不到订单",null);
 		}
		BigDecimal totalPrice = orderManage.getTotalPrice()==null?new BigDecimal("0"):orderManage.getTotalPrice();
		BigDecimal changePrice = orderManage.getChangePrice()==null?new BigDecimal("0"):orderManage.getChangePrice();//.multiply(big100).toBigInteger();
		BigDecimal discountPrice = orderManage.getDiscountPrice()==null?new BigDecimal("0"):orderManage.getDiscountPrice();
		BigDecimal payPrice = totalPrice.add(changePrice);

		List<OrderServe> listOrderServe = orderManage.getListOrderServer();
 		int removeCouponNum = 0;
 		for(int i=0;i<listOrderServe.size();i++){
 			OrderServe item = listOrderServe.get(i);
 			if(item.getServe().getId().equals(87)){
 				removeCouponNum++;
 			}
 		}
 		//获得订单和服务可以使用的所有优惠券
 		List<Coupon> orderCanUseCouponList = couponService.getOrderCanUseCoupon(orderId);
 		Map<Integer, Integer> map = new HashMap<>();
 		for(int i=orderCanUseCouponList.size()-1;i>=0;i--){
 			Coupon item = orderCanUseCouponList.get(i);
 			if(item.getSourceType().equals(1)){//移除外部券
 				orderCanUseCouponList.remove(item);
 				continue;
 			}
 			map.put(item.getId(), item.getId());
 		}
 		
		List<CouponCustomerRelevance> list = couponService.selectMyCoupon(customerId);
		long nowTime = new Date().getTime();
		for(int i=list.size()-1;i>=0;i--){
			CouponCustomerRelevance item = list.get(i);
			//移除已使用和过期券
			if(item.getState().equals(2)||item.getState().equals(3)){list.remove(i);continue;}
			//移除过期券
			if(item.getValidEndTime().getTime()<new Date().getTime()){list.remove(i);continue;}
			//移除没有在服务列表里面的券,查询列表,遍历判断
			if(!map.containsKey(item.getCouponId())){list.remove(i);continue;}
			//控制只能返回多少张卸甲券
			if(list.get(i).getCouponId().equals(66)){//移除卸甲券
				if(removeCouponNum>0){
					removeCouponNum--;
				}else{
					list.remove(i);
					continue;
				}
			}
			//移除未满足满减条件的券
			if(item.getCoupon().getType().equals(3)){
				BigDecimal reachingAmount = new BigDecimal(item.getCoupon().getReachingAmount());
				if(payPrice.compareTo(reachingAmount)==-1){list.remove(i);continue;}
			}
			
		}
		return new JsonResult<List<CouponCustomerRelevance>>(true, JsonResult.APP_DEFINE_SUC, list);
	}
	
	
	
	
	//关联,查询领取优惠券的用户列表,领取时间排序
	@ApiOperation(value = "我的优惠券列表,查所有时不用传pageNum,pageSize", notes = "")
	@RequestMapping(method={RequestMethod.GET},value="/getMyCoupon")
    public JsonResult getMyCoupon(String token,Integer pageNum,Integer pageSize,Integer state,Boolean containRemoveCoupon){
		if(token==null){
			return new JsonResult(false,String.format(JsonResult.PARAM_NULL, "token"),null);
		}
		GogirlToken gogirlToken=gogirlTokenService.getTokenByToken(token);
		if(gogirlToken==null){
			return new JsonResult(false,JsonResult.TOKEN_NULL_CODE,JsonResult.TOKEN_NULL_ERR);
		}
		if(gogirlToken.getCustomerId()==null){
			return new JsonResult(false,JsonResult.TOKEN_NULL_AUTHORIZED_CODE,JsonResult.TOKEN_NULL_AUTHORIZED_MSG);
		}
		Integer customerId = gogirlToken.getCustomerId();
 		logger.info("查询领取优惠券的用户列表CouponCustomerRelevance:"+customerId.toString());
 		if(pageNum!=null&&pageSize!=null){
 	        PageHelper.startPage(pageNum,pageSize);
 		}
		List<CouponCustomerRelevance> list = couponService.selectMyCoupon(customerId);
		long nowTime = new Date().getTime();
		for(int i=0;i<list.size();i++){
			if(containRemoveCoupon!=null&&containRemoveCoupon==false&&list.get(i).getCouponId().equals(66)){//移除卸甲券
				list.remove(i);
				i--;
				continue;
			}
			CouponCustomerRelevance couponCustomerRelevance = list.get(i);
			if(nowTime>couponCustomerRelevance.getValidEndTime().getTime()&&couponCustomerRelevance.getState()==1){
				couponCustomerRelevance.setState(3);
			}
			if(state!=null&&state!=0&&couponCustomerRelevance!=null&&couponCustomerRelevance.getState()!=null&&!couponCustomerRelevance.getState().equals(state)){
				list.remove(i);
				i--;
				continue;
			}
		}
		PageInfo<CouponCustomerRelevance> pageInfo = new PageInfo<CouponCustomerRelevance>(list);
		return new JsonResult(true, JsonResult.APP_DEFINE_SUC, pageInfo);
    }
	//查询卸甲券
	@ApiOperation(value = "(暂时保留,下版本撤掉该方法)查询卸甲券", notes = "")
	@RequestMapping(method={RequestMethod.GET},value="/getMyRemoveCoupon")
    public JsonResult getRemoveCoupon(String token){
		if(token==null){
			return new JsonResult(false,String.format(JsonResult.PARAM_NULL, "token"),null);
		}
		GogirlToken gogirlToken=gogirlTokenService.getTokenByToken(token);
		if(gogirlToken==null){
			return new JsonResult(false,JsonResult.TOKEN_NULL_CODE,JsonResult.TOKEN_NULL_ERR);
		}
		if(gogirlToken.getCustomerId()==null){
			return new JsonResult(false,JsonResult.TOKEN_NULL_AUTHORIZED_CODE,JsonResult.TOKEN_NULL_AUTHORIZED_MSG);
		}
		Integer customerId = gogirlToken.getCustomerId();
 		logger.info("查询领取优惠券的用户列表CouponCustomerRelevance:"+customerId.toString());
		List<CouponCustomerRelevance> list = couponService.selectMyCoupon(customerId);
		long nowTime = new Date().getTime();
		for(int i=0;i<list.size();i++){
			if(list.get(i)==null||list.get(i).getCouponId()==null||!list.get(i).getCouponId().equals(66)){
				list.remove(i);
				i--;
				continue;
			}
			CouponCustomerRelevance couponCustomerRelevance = list.get(i);
			if(nowTime>couponCustomerRelevance.getValidEndTime().getTime()&&couponCustomerRelevance.getState()==1){
				couponCustomerRelevance.setState(3);
			}
		}
		PageInfo<CouponCustomerRelevance> pageInfo = new PageInfo<CouponCustomerRelevance>(list);
		return new JsonResult(true, JsonResult.APP_DEFINE_SUC, pageInfo);
    }
	@ApiOperation(value = "我的某张券的详情", notes = "返回customer信息，如果没有该用户返回null")
	//查询用户领取详情
	@RequestMapping(method={RequestMethod.GET},value="/selectRelevanceByPrimaryKey")
    public JsonResult selectRelevanceByPrimaryKey(Integer id){
		CouponCustomerRelevance couponCustomerRelevance = couponService.selectRelevanceByPrimaryKey(id);
		return new JsonResult(true, JsonResult.APP_DEFINE_SUC, couponCustomerRelevance);
    }
	@ApiOperation(value = "查询是否可领新人优惠券,是新人返回券信息,不是新人返回null;")
    @RequestMapping(method={RequestMethod.GET},value = "checkNewCustomer")
    public JsonResult checkNewCustomer(String token){
		if(token==null){
			return new JsonResult(false,String.format(JsonResult.PARAM_NULL, "token"),null);
		}
		GogirlToken gogirlToken=gogirlTokenService.getTokenByToken(token);
		if(gogirlToken==null){
			return new JsonResult(false,JsonResult.TOKEN_NULL_CODE,JsonResult.TOKEN_NULL_ERR);
		}
		if(gogirlToken.getCustomer()==null){
			return new JsonResult(false,JsonResult.TOKEN_NULL_AUTHORIZED_CODE,JsonResult.TOKEN_NULL_AUTHORIZED_MSG);
		}
		Integer customerId = gogirlToken.getCustomer().getId();

		GogirlConfig gogirlConfig = gogirlConfigMapper.selectByPrimaryKey(2);
		if(gogirlConfig==null||gogirlConfig.getValue()==null||gogirlConfig.getValue().isEmpty()){
			return new JsonResult<>(false,"请联系管理员配置发放哪张新人优惠券");
		}
		String[] couponIds = gogirlConfig.getValue().split(",");
		Integer couponId = null;
		List<Integer> coupontIdList = new ArrayList<>();
		for(int i=0;i<couponIds.length;i++){
			if(couponIds[i]!=null&&!couponIds[i].isEmpty()){
				Integer itemId = Integer.parseInt(couponIds[0]);
				if(i==0){
					couponId = itemId;
				}
				coupontIdList.add(itemId);
			}
		}
		if(coupontIdList.size()>0){//判断是否有领过相关的优惠券
			List<CouponCustomerRelevance> list = couponService.selectMyNewCustomerCoupon(customerId,coupontIdList);
			if(list.size()>0){
				return new JsonResult<>(true,"您曾经领取过新人优惠券",null);
			}
		}
		Integer orderTimes = orderManageService.countOrderTimes(gogirlToken.getCustomerId());
		if(orderTimes==null||orderTimes==0){//没有订单,确认是新人,发券
			Coupon  c = couponService.selectByPrimaryKey(couponId);
			if(c==null){
				return new JsonResult<>(false,"请联系管理员配置新人优惠券",null);
			}
			return new JsonResult<>(true,JsonResult.APP_DEFINE_SUC,c);
		}else{
			return new JsonResult<>(true,"只有新客才能领取优惠券",null);
		}
    }

	//新人优惠券,后台配置具体发那张券
	@ApiOperation(value = "新人优惠券,后台配置具体发那张券", notes = "")
	@RequestMapping(method={RequestMethod.POST},value="/sendNewCustomerCouponXcx")
    public JsonResult sendNewCustomerCouponXcx(String token){
		if(token==null){
			return new JsonResult(false,String.format(JsonResult.PARAM_NULL, "token"),null);
		}
		GogirlToken gogirlToken=gogirlTokenService.getTokenByToken(token);
		if(gogirlToken==null){
			return new JsonResult(false,JsonResult.TOKEN_NULL_CODE,JsonResult.TOKEN_NULL_ERR);
		}
		if(gogirlToken.getCustomer()==null){
			return new JsonResult(false,JsonResult.TOKEN_NULL_AUTHORIZED_CODE,JsonResult.TOKEN_NULL_AUTHORIZED_MSG);
		}
		Integer customerId = gogirlToken.getCustomer().getId();

		GogirlConfig gogirlConfig = gogirlConfigMapper.selectByPrimaryKey(2);
		if(gogirlConfig==null||gogirlConfig.getValue()==null||gogirlConfig.getValue().isEmpty()){
			return new JsonResult<>(false,"请联系管理员配置发放哪张新人优惠券");
		}
		String[] couponIds = gogirlConfig.getValue().split(",");
		Integer couponId = null;
		List<Integer> coupontIdList = new ArrayList<>();
		for(int i=0;i<couponIds.length;i++){
			if(couponIds[i]!=null&&!couponIds[i].isEmpty()){
				Integer itemId = Integer.parseInt(couponIds[0]);
				if(i==0){
					couponId = itemId;
				}
				coupontIdList.add(itemId);
			}
		}
		if(coupontIdList.size()>0){//判断是否有领过相关的优惠券
			List<CouponCustomerRelevance> list = couponService.selectMyNewCustomerCoupon(customerId,coupontIdList);
			if(list.size()>0){
				return new JsonResult<>(false,"您曾经领取过新人优惠券",list);
			}
		}
		logger.info("发放优惠券customerId:"+customerId.toString());
		logger.info("发放优惠券couponId:"+couponId.toString());
		
		Customer customer = gogirlToken.getCustomer();
		//返回消息
		return  sendCoupon(couponId,customerId,customer);
    }	
	
	//小程序发券
	@ApiOperation(value = "小程序发券", notes = "")
	@RequestMapping(method={RequestMethod.POST},value="/sendCouponXcx")
    public JsonResult sendCouponXcx(String token ,Integer couponId){
		if(token==null){
			return new JsonResult(false,String.format(JsonResult.PARAM_NULL, "token"),null);
		}
		GogirlToken gogirlToken=gogirlTokenService.getTokenByToken(token);
		if(gogirlToken==null){
			return new JsonResult(false,JsonResult.TOKEN_NULL_CODE,JsonResult.TOKEN_NULL_ERR);
		}
		if(gogirlToken.getCustomer()==null){
			return new JsonResult(false,JsonResult.TOKEN_NULL_AUTHORIZED_CODE,JsonResult.TOKEN_NULL_AUTHORIZED_MSG);
		}
		Integer customerId = gogirlToken.getCustomer().getId();
		if(couponId==null){
			return new JsonResult(false,String.format(JsonResult.PARAM_NULL, "couponId"),null);
		}
		logger.info("发放优惠券customerId:"+customerId.toString());
		logger.info("发放优惠券couponId:"+couponId.toString());
		
		Customer customer = gogirlToken.getCustomer();
		//返回消息
		return  sendCoupon(couponId,customerId,customer);
    }	
	
	
	
	
	
	//新增优惠券
	@ApiIgnore
	@RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value="/insertCoupon")
    public JsonResult insertCoupon(Coupon coupon){
		if(coupon==null){
			return new JsonResult(false,String.format(JsonResult.PARAM_NULL, "coupon"),null);
		}
		logger.info("新增优惠券:"+coupon.toString());
		int row = couponService.insertSelective(coupon);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("row", row);
		return new JsonResult(true, JsonResult.APP_DEFINE_SUC, map);
    }
	
	//查询优惠券列表,最后一次修改时间排序
	@ApiIgnore
	@RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value="/selectByCoupon")
    public JsonResult selectByCoupon(Coupon coupon,Integer pageNum,Integer pageSize){
		if(pageNum==null){
			return new JsonResult(false,String.format(JsonResult.PARAM_NULL, "pageNum"),null);
		}
		if(pageSize==null){
			return new JsonResult(false,String.format(JsonResult.PARAM_NULL, "pageSize"),null);
		}
 		logger.info("查询优惠券列表:"+coupon.toString());
        PageHelper.startPage(pageNum,pageSize);
		List<Coupon> list = couponService.selectByCoupon(coupon);
		PageInfo<Coupon> pageInfo = new PageInfo<Coupon>(list);
		return new JsonResult(true, JsonResult.APP_DEFINE_SUC, pageInfo);
    }
	//修改优惠券
	@ApiIgnore
	@RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value="/updateCoupon")
    public JsonResult updateByPrimaryKeySelective(Coupon coupon){
		if(coupon==null||coupon.getId()==null){
			return new JsonResult(false,String.format(JsonResult.PARAM_NULL, "id"),null);
		}
		logger.info("修改优惠券:"+coupon.toString());
		int row = couponService.updateByPrimaryKeySelective(coupon);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("row", row);
		return new JsonResult(true, JsonResult.APP_DEFINE_SUC, map);
    }
	//启用和停止优惠券
	@ApiIgnore
	@RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value="/enablingStoppingCoupon")
    public JsonResult enablingStoppingCoupon(Integer couponId){
		if(couponId==null){
			return new JsonResult(false,String.format(JsonResult.PARAM_NULL, "couponId"),null);
		}
		logger.info("启用和停止优惠券:"+couponId.toString());
		Coupon coupon = couponService.selectByPrimaryKey(couponId);
		if(coupon==null){
			return new JsonResult(false,"找不到该优惠券",null);
		}else{
			if(coupon.getState()!=null&&coupon.getState()==1) coupon.setState(2);
			else coupon.setState(1);
		}
		int row = couponService.updateByPrimaryKeySelective(coupon);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("row", row);
		return new JsonResult(true, JsonResult.APP_DEFINE_SUC, map);
    }
	//关联,查询领取优惠券的用户列表,领取时间排序
	@ApiIgnore
	@RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value="/selectByCouponCustomerRelevance")
    public JsonResult selectByCouponCustomerRelevance(CouponCustomerRelevance record,String phone,String username,Integer pageNum,Integer pageSize){
		if(pageNum==null){
			return new JsonResult(false,String.format(JsonResult.PARAM_NULL, "pageNum"),null);
		}
		if(pageSize==null){
			return new JsonResult(false,String.format(JsonResult.PARAM_NULL, "pageSize"),null);
		}
		if(record.getCode()!=null&&record.getCode().isEmpty()){
			record.setCode(null);
		}
		if(phone!=null&&phone.isEmpty()){
			phone=null;
		}
		if(username!=null&&username.isEmpty()){
			username=null;
		}
 		logger.info("查询领取优惠券的用户列表CouponCustomerRelevance:"+record.toString());
        PageHelper.startPage(pageNum,pageSize);
		List<CouponCustomerRelevance> list = couponService.selectByCouponCustomerRelevance(record,phone,username);
		long nowTime = new Date().getTime();
		int listSize = list.size();
		for(int i=0;i<listSize;i++){
			CouponCustomerRelevance couponCustomerRelevance = list.get(i);
			if(nowTime>couponCustomerRelevance.getValidEndTime().getTime()&&couponCustomerRelevance.getState()==1){
				couponCustomerRelevance.setState(3);
			}
		}
		PageInfo<CouponCustomerRelevance> pageInfo = new PageInfo<CouponCustomerRelevance>(list);
		return new JsonResult(true, JsonResult.APP_DEFINE_SUC, pageInfo);
    }
	//关联,发放优惠券
	@ApiIgnore
	@RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value="/insertCouponCustomerRelevance")
    public JsonResult insertCouponCustomerRelevance(Integer customerId,Integer couponId){
		/*临时加上,查询优惠券id*/
		String couponIdString = couponService.getCouponIdFromConfig();
		String coupon2String = couponIdString.substring(3);
		couponIdString = couponIdString.substring(0,2);
		try {
			couponId = Integer.parseInt(couponIdString);
		} catch (Exception e) {
			return new JsonResult(false,"优惠券已经领完,谢谢参与",null);
		}
		
		logger.info("发放优惠券");
		if(customerId==null){
			return new JsonResult(false,String.format(JsonResult.PARAM_NULL, "customerId"),null);
		}
		if(couponId==null){
			return new JsonResult(false,String.format(JsonResult.PARAM_NULL, "couponId"),null);
		}
		logger.info("customerId:"+customerId.toString());
		logger.info("couponId:"+couponId.toString());
		
		Customer customer = customerService.selectByPrimaryKey(customerId);
		if(customer==null){
			return new JsonResult(false,"找不到该用户",null);
		}
		
		//返回消息
		Map<String, Object> map = new HashMap<String, Object>();
		JsonResult jr = sendCoupon(couponId,customerId,customer);
		if(jr.getSuccess()){
			map.put("id", jr.getData());
		}else{
			return jr;
		}
		if(coupon2String!=null&&!coupon2String.isEmpty()){
			JsonResult jr2 = sendCoupon(Integer.parseInt(coupon2String),customerId,customer);
			if(jr2.getSuccess()){
				map.put("id2", jr2.getData());
			}else{
				return jr2;
			}
		}
		return new JsonResult(true, JsonResult.APP_DEFINE_SUC, map);
    }
	public JsonResult sendCoupon(Integer couponId,Integer customerId,Customer customer) {
		Coupon coupon = couponService.selectByPrimaryKey(couponId);
		if(coupon==null){
			return new JsonResult(false,"找不到该优惠券",null);
		}
		//判断优惠券是否还有
		if(coupon.getAllQuantity()<=coupon.getReceiveQuantity()){
			return new JsonResult(false,"优惠券已经领完",null);
		}
		//判断该用户是否限领,null过,0过,有但限制不过,不限制过
		CouponCustomerRelevance record = new CouponCustomerRelevance();
		record.setCustomerId(customerId);
		record.setCouponId(couponId);
		int row = couponService.countRelevanceNum(record);
		if(coupon.getLimitQuantity()!=null&&coupon.getLimitQuantity()!=0&&coupon.getLimitQuantity()<=row){
			return new JsonResult(false,"抱歉,该优惠券最多可领取"+coupon.getLimitQuantity()+"张,你已有"+row+"张该优惠券.",null);
		}
		
		CouponCustomerRelevance couponCustomerRelevance= new CouponCustomerRelevance();
		couponCustomerRelevance.setCustomerId(customerId);
		couponCustomerRelevance.setCouponId(couponId);
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
		redirectService.myHttpPost.httpRequest(gogirlProperties.getGOGIRLMP()+"gogirl_mp/wx/template/getTicketMsg", mapParm);
		
		coupon.setValidStartTime(couponCustomerRelevance.getValidStartTime());
		coupon.setValidEndTime(couponCustomerRelevance.getValidEndTime());
		return new JsonResult(true, JsonResult.APP_DEFINE_SUC, coupon);
	}
	//关联,查询领取优惠券的用户列表,领取时间排序
	@ApiIgnore
	@RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value="/selectMyCoupon")
    public JsonResult selectMyCoupon(Integer customerId,Integer pageNum,Integer pageSize){
		if(pageNum==null){
			return new JsonResult(false,String.format(JsonResult.PARAM_NULL, "pageNum"),null);
		}
		if(pageSize==null){
			return new JsonResult(false,String.format(JsonResult.PARAM_NULL, "pageSize"),null);
		}
 		logger.info("查询领取优惠券的用户列表CouponCustomerRelevance:"+customerId.toString());
        PageHelper.startPage(pageNum,pageSize);
		List<CouponCustomerRelevance> list = couponService.selectMyCoupon(customerId);
		long nowTime = new Date().getTime();
		int listSize = list.size();
		for(int i=0;i<listSize;i++){
			CouponCustomerRelevance couponCustomerRelevance = list.get(i);
			if(nowTime>couponCustomerRelevance.getValidEndTime().getTime()&&couponCustomerRelevance.getState()==1){
				couponCustomerRelevance.setState(3);
			}
		}
		PageInfo<CouponCustomerRelevance> pageInfo = new PageInfo<CouponCustomerRelevance>(list);
		return new JsonResult(true, JsonResult.APP_DEFINE_SUC, pageInfo);
    }
    //订单结束后调用,根据订单金额插入优惠券且发消息模板
	@SuppressWarnings("unchecked")
	@ApiIgnore
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value="/sendTicketAfterOrder")
	public JsonResult sendTicketAfterOrder(HttpServletRequest request,String openid,Integer customerId,
			Double orderAmount
			){
    	Boolean sentMsg = true;
    	if(openid==null||openid.isEmpty()){
    		if(customerId!=null){
    			Customer c = customerService.selectByPrimaryKey(customerId);
    			if(c!=null){
    				if(c.getOpenid()!=null&&!c.getOpenid().isEmpty()){
    					openid = c.getOpenid();
    				}else{
    					sentMsg = false;
    				}
    			}else{
    				return new JsonResult(false,"找不到该用户customerid:"+customerId,null);
    			}
    		}else{
    			return new JsonResult(false,String.format(JsonResult.PARAM_NULL,"openid和customerId"),null);
    		}
    	}else{
    		Customer customer = customerService.selectByOpenid(openid);
    		if(customer!=null){
    			customerId = customer.getId();
    		}else{
    			return new JsonResult(false,"找不到该用户openid:"+openid,null);
    		}
    	}
    	String storeName="gogirl美甲美睫沙龙所有门店";
    	String type="";
    	String amount="";
    	String code="无需验证";
		Map<Integer, String> map = new HashMap<>();
		map.put(1, "现金抵扣券");
		map.put(2, "免单券");
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
    	}
    	
    	int row =0;
    	if(c!=null){
    		type = map.get(c.getType());
    		if(type==null||type.isEmpty()){
    			type = c.getDiscountAmount()+"元";
    		}
    		amount = c.getName();
    		//插入关联
    		CouponCustomerRelevance ccr = new CouponCustomerRelevance();
    		ccr.setCouponId(c.getId());
    		ccr.setCustomerId(customerId);
    		row = couponService.insertSelective(c,ccr);
    	}else{
    		return new JsonResult(false,"找不到优惠券",null);
    	}
    	if(sentMsg){//是否发送消息模板
        	//发送模板消息
    		Map<Integer, String> mapType = new HashMap<>();
    		mapType.put(1, "现金抵扣券");
    		mapType.put(2, "免单券");
    		Map<String, Object> mapParm	= new HashMap<>();
    		mapParm.put("storeName", storeName);
    		mapParm.put("code", code);
    		mapParm.put("openid", openid);
    		mapParm.put("type", type);
    		mapParm.put("amount", amount);
    		redirectService.myHttpPost.httpRequest(gogirlProperties.getGOGIRLMP()+"gogirl_mp/wx/template/getTicketMsg", mapParm);
    	}
		return  new JsonResult(true,JsonResult.APP_DEFINE_SUC,row);
    }

    //查询所有优惠券接口
	@ApiIgnore
	@RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value="/getAllCoupon")
    public JsonResult getAllCoupon(){
 		logger.info("查询所有券");
		List<Coupon> list = couponService.getAllUseCoupon();
		return new JsonResult(true, JsonResult.APP_DEFINE_SUC, list);
    }
    
    //总后台发券接口:issueType发放类型，groupType分组类型，phone电话号码，couponId优惠券id，amount数量
    //issueType发放类型：1.单个用户；2.群发用户
    //groupType分组类型：1.注册用户;2.订单用户;3.次订单用户;4.会员卡用户
	@RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value="/sendCoupons")
	@ApiIgnore
    public JsonResult sendCoupons (HttpServletRequest request,Integer issueType,String groupType,String phone,Integer couponId,Integer amount){
//入参判断
		if(issueType==null){
			return new JsonResult(false,String.format(JsonResult.PARAM_NULL,"issueType"),null);
		}
		if(couponId==null){
			return new JsonResult(false,String.format(JsonResult.PARAM_NULL,"couponId"),null);
		}
		if(amount==null){
			return new JsonResult(false,String.format(JsonResult.PARAM_NULL,"issueType"),null);
		}
		logger.info("发券："+issueType,groupType,phone,couponId,amount);
		//查券
		Coupon coupon = couponService.selectByPrimaryKey(couponId);
		if(coupon==null){
			return new JsonResult(false,"找不到该优惠券",null);
		}
		
		//查用户
		List<Customer> listCustomer = new ArrayList<>();
 		if(issueType==1){//单个用户
 			if(phone==null){
 				return new JsonResult(false,String.format(JsonResult.PARAM_NULL,"phone"),null);
 			}
 			//查询电话号码用户
 			Customer customer = customerService.selectByPhone(phone);
 			if(customer!=null){
 	 			listCustomer.add(customer);
 			}else{
 				return new JsonResult(false, "找不到该用户phone="+phone, null);
 			}
 		}else if(issueType==2){
 			if(groupType==null){
 				return new JsonResult(false,String.format(JsonResult.PARAM_NULL,"groupType"),null);
 			}
 			//groupType拆分成四个参数，调用四段条件查询,通过or关联。查询用户id,openid
 			Integer havePhone= null;
 			Integer orderTimes= null;
 			Integer haveBalance= null;
 			if(groupType.contains("1")){
 				havePhone = 1;//随便赋值
 			}
 			if(groupType.contains("3")){
 				orderTimes = 2;//两次订单以上
 			}
 			if(groupType.contains("2")){
 				orderTimes = 1;//一次订单以上
 			}
 			if(groupType.contains("4")){
 				haveBalance = 1;//随便赋值
 			}
 			listCustomer = customerService.getGroupCustomer(havePhone, orderTimes, haveBalance);
 		}else{
 			logger.info("未知发放类型");
 			return new JsonResult(false,"未知发放类型issueType="+issueType,null);
 		}
 		//判断是否足够券，增加优惠券总数
 		int addCouponNum = 0;//新增了几张优惠券
 		int listCustomerSize = listCustomer.size();
		if(coupon.getAllQuantity()-coupon.getReceiveQuantity()<listCustomerSize){
			addCouponNum = coupon.getReceiveQuantity()+listCustomerSize-coupon.getAllQuantity();
			coupon.setAllQuantity(coupon.getReceiveQuantity()+listCustomerSize);//增加优惠券总数
//			coupon.setReceiveQuantity(coupon.getAllQuantity());//设置所有优惠券都被领了//批量发券时再改
			couponService.updateByPrimaryKeySelective(coupon);
		}
		String[] openidList = new String[listCustomerSize];
		//批量插入关联数据
		List<CouponCustomerRelevance> list = new ArrayList<>();
		
		for(int i=0;i<listCustomerSize;i++){
			CouponCustomerRelevance ccr = new CouponCustomerRelevance();
			ccr.setCustomerId(listCustomer.get(i).getId());
			ccr.setCouponId(coupon.getId());
			list.add(ccr);
			if(listCustomer.get(i)!=null&&listCustomer.get(i).getOpenid()!=null){//所有有openid的账号
				openidList[i]=listCustomer.get(i).getOpenid();
			}else{
				
			}
		}
		int insertRow = couponService.insertSelectiveList(coupon, list,amount);
		try {
			// 批量发起模板消息
			Map<Integer, String> mapType = new HashMap<>();
			mapType.put(1, "现金抵扣券");
			mapType.put(2, "免单券");
//			Map<String, Object> mapParm	= new HashMap<>();
//			mapParm.put("openidList", String.valueOf(openidList));
//			logger.info(openidList.toString());
//			mapParm.put("storeName", "gogirl美甲美睫沙龙所有门店");
//			mapParm.put("type", mapType.get(coupon.getType()));
//			mapParm.put("amount", coupon.getName()==null?coupon.getDiscountAmount()+"元":coupon.getName());
//			mapParm.put("code", "无需验证");
			restTemplate.postForObject(gogirlProperties.getGOGIRLMP()+"gogirl_mp/wx/template/getTicketMsgOpenidList?storeName=gogirl美甲美睫沙龙所有门店&code=无需验证&type="+
					 mapType.get(coupon.getType())+"&amount="+ (coupon.getName()==null?coupon.getDiscountAmount()+"元":coupon.getName()), 
					openidList, JsonResult.class );
//						redirectService.myHttpPost.httpRequest(gogirlProperties.getGOGIRLMP()+"gogirl_mp/wx/template/getTicketMsgOpenidList", mapParm);
//			redirectService.myHttpPost.restTemplate.postForObject(gogirlProperties.getGOGIRLMP()+"gogirl_mp/wx/template/getTicketMsgOpenidList", 
//					request, JsonResult.class,listOpenid );
		} catch (Exception e) {
			logger.info(e.getMessage());
		}
		
		
 		//TODO 返回哪些用户没有发送成功
		Map<String, Object> map = new HashMap<>();
		map.put("addCouponNum",addCouponNum );
		map.put("insertRow",insertRow );
 		return new JsonResult(true, "群发成功", map);
    }
	@RequestMapping(method={RequestMethod.GET},value="/couponExpireRemind")
	@ApiIgnore
    public JsonResult couponExpireRemind (){
		couponService.couponExpireRemind();
		return new JsonResult(true);
	}
}
