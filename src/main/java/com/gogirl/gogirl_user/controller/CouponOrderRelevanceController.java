package com.gogirl.gogirl_user.controller;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.gogirl.gogirl_order.order_commons.config.Constans;
import com.gogirl.gogirl_order.order_commons.dto.OrderManage;
import com.gogirl.gogirl_order.order_commons.dto.OrderServe;
import com.gogirl.gogirl_order.order_commons.utils.JsonResult;
import com.gogirl.gogirl_order.order_manage.service.OrderManageService;
import com.gogirl.gogirl_order.order_manage.service.OrderServeService;
import com.gogirl.gogirl_user.dao.CouponCustomerRelevanceMapper;
import com.gogirl.gogirl_user.dao.CouponOrderRelevanceMapper;
import com.gogirl.gogirl_user.entity.Coupon;
import com.gogirl.gogirl_user.entity.CouponCustomerRelevance;
import com.gogirl.gogirl_user.entity.CouponOrderRelevance;
import com.gogirl.gogirl_user.entity.TimesCardCustomerRelevance;
import com.gogirl.gogirl_user.entity.TimesCardUsedRecord;
import com.gogirl.gogirl_user.service.CouponService;
import com.gogirl.gogirl_user.service.TimesCardService;
import com.gogirl.gogirl_xcx.entity.GogirlToken;
import com.gogirl.gogirl_xcx.service.GogirlTokenService;

@RestController
@Api(tags = { "8.优惠券" },value = "")
public class CouponOrderRelevanceController {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	@Resource
	CouponService couponService;
	@Resource
	OrderManageService orderManageService;
	@Resource
	GogirlTokenService gogirlTokenService;
	@Resource
	CouponOrderRelevanceMapper couponOrderRelevanceMapper;
	@Resource
	CouponCustomerRelevanceMapper couponCustomerRelevanceMapper;
	@Resource
	TimesCardService timesCardService;
	@Resource
	OrderServeService orderServeService;
	
	//美甲师提交外部券
	@ApiOperation(value = "美甲师提交外部券", notes = "")
	@RequestMapping(method={RequestMethod.POST},value="/techSubmitCoupons")
    public JsonResult<List<CouponOrderRelevance>> techSubmitCoupons(String token,Integer orderId,String couponIds){
		logger.info("美甲师提交外部券");
		List<CouponOrderRelevance> listResult = new ArrayList<>();
		OrderManage orderManage = orderManageService.getOrderForDetail(orderId);
		if(orderManage==null){
			return new JsonResult<>(false,"找不到订单");
		}
		BigDecimal canDiscountAmount= orderManage.getTotalPrice();
		
		List<OrderServe> listOrderServe = orderManage.getListOrderServer();

		
		
		// 删除订单已经关联的外部券
		List<CouponOrderRelevance> list = couponOrderRelevanceMapper.selectByOrderId(orderId);//根据订单id查询已选优惠券
		Iterator<CouponOrderRelevance> itCouponOrder = list.iterator();
		while(itCouponOrder.hasNext()){
			CouponOrderRelevance cor = itCouponOrder.next();
			if(cor.getCoupon()!=null&&cor.getCoupon().getSourceType().equals(1)){
				cor.setStatus(5);//设置为美甲师删除
				couponCustomerRelevanceMapper.deleteByPrimaryKey(cor.getCouponCustomerRelevanceId());
				couponOrderRelevanceMapper.updateByPrimaryKeySelective(cor);
			}
		}
		String [] couponIdsArr =  new String[0];
		if(couponIds!=null){
			couponIdsArr = couponIds.split(",");
		}

		//TODO 判断券是否可以同时使用(用户端也要判断
		
		for(int i=0;i<couponIdsArr.length;i++){//遍历处理传过来的id
			if(couponIdsArr[i]==null||couponIdsArr[i].trim().isEmpty()){//为空的直接过
				continue;
			}
			Integer itemId = null;
			try {
				itemId = Integer.parseInt(couponIdsArr[i]);
			} catch (Exception e) {
				new JsonResult<>(false,"Id转int报错:"+couponIdsArr[i],null);
			}
			Coupon coupon = couponService.selectByPrimaryKey(itemId);
			if(coupon==null){
				return new JsonResult<>(false,"抱歉找不到券id:"+itemId,null);
			}
			//找到该外部券可以用到哪些服务
			List<OrderServe> listOs = orderServeService.listCouponCanUseOrderServeId(coupon.getId(), orderId);
			Map<Integer, OrderServe> map = new HashMap<>();
			Iterator<OrderServe> it = listOs.iterator();
			if(it.hasNext()){
				OrderServe item = it.next();
				map.put(item.getId(), item);
			}
			BigDecimal couponDiscountAmount = new BigDecimal(coupon.getDiscountAmount().toString());
			//遍历外部券,找到各个外部券具体用到的服务
			Iterator<OrderServe> it2 = listOrderServe.iterator();
			OrderServe timesCardOs = null;//外部券用于该服务
			while(it2.hasNext()){//遍历找到该外部券对应的服务
				OrderServe os = it2.next();
				if(map.containsKey(os.getId())){//判断外部券是否能使用于该服务
					if(timesCardOs==null){//记录第一张可以使用该外部券的服务
						timesCardOs = os;
					}else if(timesCardOs.getServePrice().add(timesCardOs.getServeChangePrice()).compareTo(os.getServePrice().add(os.getServeChangePrice()))<0){
						//上一次记录的服务金额小于这次遍历的金额
						if(timesCardOs.getServePrice().add(timesCardOs.getServeChangePrice()).compareTo(couponDiscountAmount)<0){
							//上次记录的金额小于优惠券可优惠金额,用这次遍历的服务
							timesCardOs = os;
						}
					}else{
						//上一次记录的服务金额大于这次遍历的金额
						if(os.getServePrice().add(os.getServeChangePrice()).compareTo(couponDiscountAmount)>0){
							//这次遍历的金额大于优惠券可优惠金额,用这次遍历的服务
							timesCardOs = os;
						}
					}
				}
			}
			Integer orderServeId = null;
			if(timesCardOs!=null){//记录外部券使用
				orderServeId = timesCardOs.getId();
			}else{//找不到外部券可用的服务
				return new JsonResult<>(false,"该外部券没有配置到该服务,不能使用,请联系管理员在总后台配置");
			}
			
			/*发一张未使用的券*/
			CouponCustomerRelevance ccr = new CouponCustomerRelevance();
			ccr.setCustomerId(orderManage.getOrderUser());
			ccr.setCouponId(itemId);
			ccr.setState(1);
			ccr.setReceiveTime(new Date());
			if(coupon.getValidType().equals(2)){
				ccr.setValidStartTime(new Date());
				ccr.setValidEndTime(new Date(new Date().getTime()+new Long("86400000")*coupon.getValidDate()));
			}else{
				ccr.setValidStartTime(coupon.getValidStartTime());
				ccr.setValidEndTime(coupon.getValidEndTime());
			}
			couponCustomerRelevanceMapper.insertSelective(ccr);
			
			/*添加订单关联外部券*/
			CouponOrderRelevance itemCor = new CouponOrderRelevance();
			if(canDiscountAmount.compareTo(couponDiscountAmount)>=0){//可以优惠的金额,大于优惠金额,直接优惠
				canDiscountAmount.subtract(couponDiscountAmount);
				itemCor.setDiscountAmount(couponDiscountAmount);
			}else{//可以优惠的金额小于优惠金额,只优惠可以优惠的金额
				canDiscountAmount = new BigDecimal("0");
				itemCor.setDiscountAmount(canDiscountAmount);
			}
			itemCor.setCouponId(itemId);
			itemCor.setCustomerId(orderManage.getOrderUser());
			itemCor.setCouponCustomerRelevanceId(ccr.getId());
			itemCor.setCanBeUse(true);//默认可以使用
			itemCor.setCreateTime(new Date());
			itemCor.setOrderId(orderId);
			itemCor.setOrderServeId(orderServeId);
			itemCor.setStatus(Constans.COUPON_ORDER_STATUS_CANUSE);//默认待确认
			
			couponOrderRelevanceMapper.insertSelective(itemCor);
		}

		
		//计算且更新订单优惠券金额
		OrderManage om = orderManageService.getOrderForDetail(orderId);
		BigDecimal discountPrice = countOrderDiscount(om);
		OrderManage ordermanage = new OrderManage();
		ordermanage.setId(om.getId());
		ordermanage.setDiscountPrice(discountPrice);
		orderManageService.updateOrderStatus(ordermanage);
		return new JsonResult<>(true,JsonResult.APP_DEFINE_SUC,null);
	}	
	
	@ApiOperation(value = "用户提交优惠券", notes = "")
	@RequestMapping(method={RequestMethod.POST},value="/submitCoupons")
    public JsonResult<List<CouponOrderRelevance>> submitCoupons(String token,Integer orderId,String couponRelevanceIds,String timesCardIds){
		logger.info("用户提交优惠券");
		List<CouponOrderRelevance> listResult = new ArrayList<>();
		if(couponRelevanceIds==null){
			return new JsonResult<>(true,"无选择优惠券",null);
		}
		String [] couponRelevanceIdsArr =  couponRelevanceIds.split(",");
		if(couponRelevanceIdsArr.length==0){
			return new JsonResult<>(true,"无选择优惠券",null);
		}
		OrderManage orderManage = orderManageService.getOrderForDetail(orderId);
		if(orderManage==null){
			return new JsonResult<>(false,"找不到订单");
		}
		BigDecimal canDiscountAmount= orderManage.getTotalPrice();
		
		List<OrderServe> listOrderServe = orderManage.getListOrderServer();

		//取消之前已经选好的次卡
		int row = timesCardService.cancelTimesCardByOrderId(orderId);
		
		
		//记录次卡使用
		String [] timesCardIdsArr =  new String[0];
		if(timesCardIds!=null){
			timesCardIdsArr = timesCardIds.split(",");
		}
		for(int i=0;i<timesCardIdsArr.length;i++){//遍历处理传过来的次卡id
			if(timesCardIdsArr[i]==null||timesCardIdsArr[i].trim().isEmpty()){//为空的直接过
				continue;
			}
			Integer itemId = null;
			try {
				itemId = Integer.parseInt(timesCardIdsArr[i]);
			} catch (Exception e) {
				new JsonResult<>(false,"Id转int报错:"+timesCardIdsArr[i],null);
			}
			TimesCardCustomerRelevance timesCardCustomerRelevance = timesCardService.getTimesCardDetail(itemId);
			if(timesCardCustomerRelevance==null){
				return new JsonResult<>(false,"抱歉找不到次卡id:"+itemId,null);
			}
			//判断该次卡是否可以使用
			if(timesCardCustomerRelevance.getStatus().equals(2)){
				return new JsonResult<>(false,"该次卡次数已用完");
			}
			if(timesCardCustomerRelevance.getStatus().equals(3)){
				return new JsonResult<>(false,"该次卡已过期1");
			}
			if(timesCardCustomerRelevance.getValidEndTime().getTime()<new Date().getTime()){
				return new JsonResult<>(false,"该次卡已过期2");
			}
			
			//查次卡id可以用到订单id里面的哪些服务
			List<OrderServe> list = orderServeService.listTimesCardCanUseOrderServeId(itemId,orderId);
			Map<Integer, OrderServe> map1 = new HashMap<>();
			for(int j=0;j<list.size();j++){
				map1.put(list.get(i).getId(), list.get(i));
			}
			Iterator<OrderServe> it = listOrderServe.iterator();
			OrderServe timesCardOs = null;//次卡用于该服务
			while(it.hasNext()){//遍历找到该次卡对应的服务
				OrderServe os = it.next();
				if(map1.containsKey(os.getId())){//判断次卡是否能使用于该服务
					if(timesCardOs==null){//记录第一张可以使用该次卡的服务
						timesCardOs = os;
					}else if(timesCardOs.getServePrice().add(timesCardOs.getServeChangePrice()).compareTo(os.getServePrice().add(os.getServeChangePrice()))<0){
						//上一次记录的服务金额小于这次遍历的金额
						if(timesCardOs.getServePrice().add(timesCardOs.getServeChangePrice()).compareTo(timesCardCustomerRelevance.getTimesCardType().getDiscountAmount())<0){
							//上次记录的金额小于优惠券可优惠金额,用这次遍历的服务
							timesCardOs = os;
						}
					}else{
						//上一次记录的服务金额大于这次遍历的金额
						if(os.getServePrice().add(os.getServeChangePrice()).compareTo(timesCardCustomerRelevance.getTimesCardType().getDiscountAmount())>0){
							//这次遍历的金额大于优惠券可优惠金额,用这次遍历的服务
							timesCardOs = os;
						}
					}
				}
			}
			Integer serveId = null;
			Integer orderServeId = null;
			Integer technicianId = null;
			if(timesCardOs!=null){//记录次卡使用
				serveId=timesCardOs.getServeId();
				technicianId = Integer.parseInt(timesCardOs.getTechnicianId().trim().split(",")[0]);//取第一位技师
				orderServeId = timesCardOs.getId();
			}else{//找不到次卡可用的服务
				return new JsonResult<>(false,"该次卡没有配置到该服务,不能使用,请联系管理员在总后台配置");
			}
			//记录次卡使用,状态为1.已选择待付款
			TimesCardUsedRecord record = new TimesCardUsedRecord();
			record.setCardRelevanceCustomerId(itemId);
			record.setCardTypeId(timesCardCustomerRelevance.getCardTypeId());
			record.setCustomerId(timesCardCustomerRelevance.getCustomerId());
			record.setDepartmentId(orderManage.getDepartmentId());
			record.setOrderId(orderId);
			record.setServeId(serveId);
			record.setOrderServeId(orderServeId);
			record.setTechnicianId(technicianId);
			record.setTime(new Date());
			record.setType(2);
			record.setStatus(1);//1.已选择次卡,待付款;2.已使用;3.已取消选择
			timesCardService.insertTimesCardUsedRecord(record);
		}

		
		
		
		List<CouponOrderRelevance> list = couponOrderRelevanceMapper.selectByOrderId(orderId);//根据订单id查询已选优惠券
		//遍历把已选优惠券放到map
		Map<Integer,CouponOrderRelevance> map = new HashMap<Integer, CouponOrderRelevance>();
		for(int i=0;i<list.size();i++){
			CouponOrderRelevance item = list.get(i);
			if(item.getCoupon()!=null&&!item.getCoupon().getSourceType().equals(1)){
				map.put(item.getCouponCustomerRelevanceId(), item);
			}
		}
		int gogirlCouponNum = 0;
		int needConfirmCouponNum = 0;
		for(int i=0;i<couponRelevanceIdsArr.length;i++){//遍历处理传过来的id
			if(couponRelevanceIdsArr[i]==null){//为空的直接过
				continue;
			}
			Integer itemId = null;
			try {
				itemId = Integer.parseInt(couponRelevanceIdsArr[i]);
			} catch (Exception e) {
				new JsonResult<>(false,"Id转int报错:"+couponRelevanceIdsArr[i],null);
			}
			CouponCustomerRelevance couponCustomerRelevance = couponService.selectRelevanceByPrimaryKey(itemId);
			if(couponCustomerRelevance==null){
				return new JsonResult<>(false,"抱歉找不到券id:"+itemId,null);
			}
			CouponOrderRelevance itemCor = new CouponOrderRelevance();
			itemCor.setCouponId(couponCustomerRelevance.getCouponId());
			itemCor.setCustomerId(couponCustomerRelevance.getCustomerId());
			itemCor.setCouponCustomerRelevanceId(couponCustomerRelevance.getId());
			itemCor.setCanBeUse(true);//默认可以使用
			itemCor.setCreateTime(new Date());
			itemCor.setOrderId(orderId);
			itemCor.setStatus(Constans.COUPON_ORDER_STATUS_CREATE);//默认待确认
			//设置券不能用的情况1.过期;2.卸甲券数量只能低于指甲服务数量;3.次卡券,只能用于次卡服务;4.单色芭比券只能用于单色芭比;5.券只能用于服务和券关联表
			if(couponCustomerRelevance.getValidEndTime().getTime()<new Date().getTime()){//1.过期判断
				logger.info("优惠券已过期,不能使用");
				itemCor.setCanBeUse(false);
				itemCor.setMessage("优惠券已过期,不能使用");
			}
			if(couponCustomerRelevance.getCoupon().getSourceType().equals(1)){//2.内部券只能使用1张
				logger.info("外部券"+couponCustomerRelevance.getCoupon().getName());
				needConfirmCouponNum++;//需要确认的数量
			}else if(couponCustomerRelevance.getCoupon().getSourceType().equals(0)){
				logger.info("内部券"+couponCustomerRelevance.getCoupon().getName());
				if(gogirlCouponNum>0){//最多只能选择一张gogirl券
					logger.info("最多只能选择一张gogirl优惠券");
					return new JsonResult<>(false,"最多只能选择1张gogirl券",null);
				}
				itemCor.setStatus(Constans.COUPON_ORDER_STATUS_CANUSE);
				if(couponCustomerRelevance.getCoupon().getType().equals(4)){//内部券中的卸甲券
					logger.info("卸甲券");
				}else{//内部券中的其他券
					gogirlCouponNum++;//已使用的内部券数量
				}
			}else{
				logger.info("不应该存在,既不是内部券0也不是外部券1");
			}
//			if(couponCustomerRelevance.getCouponId()==66){//TODO 卸甲券判断
//				
//			}
			if(true){//TODO 该券是否支持该服务类型
				
			}
			//判断map中如果有该券,不动,如果没该券,插入,遍历结束后,没有动到的券,删除
			if(map.containsKey(itemId)){
				couponOrderRelevanceMapper.updateByCouponCustomerRelevanceId(itemCor);
				map.remove(itemId);
			}else{
				couponOrderRelevanceMapper.insertSelective(itemCor);
//				listResult.add(itemCor);
			}
		}
		Iterator<Entry<Integer, CouponOrderRelevance>> it = map.entrySet().iterator();
		while(it.hasNext()){//删除不存在的
			Entry<Integer, CouponOrderRelevance> item = it.next();
			CouponOrderRelevance itemdel = item.getValue();
			itemdel.setStatus(Constans.COUPON_ORDER_STATUS_CUSDEL);
			couponOrderRelevanceMapper.updateByPrimaryKeySelective(itemdel);
			
		}
		//计算且更新订单优惠券金额
		OrderManage om = orderManageService.getOrderForDetail(orderId);
		BigDecimal discountPrice = countOrderDiscount(om);
		OrderManage ordermanage = new OrderManage();
		ordermanage.setId(om.getId());
		ordermanage.setDiscountPrice(discountPrice);
		if(needConfirmCouponNum>0){
			ordermanage.setStatus(Constans.ORDER_STATUS_COUPON_WAIT_SURE);
		}else{
			ordermanage.setStatus(Constans.ORDER_STATUS_PAMENT);
		}
		orderManageService.updateOrderStatus(ordermanage);

		return new JsonResult<>(true,JsonResult.APP_DEFINE_SUC,null);
	}
	//计算订单优惠金额
	public BigDecimal countOrderDiscount(OrderManage om) {
		logger.info("//计算订单优惠金额");
		BigDecimal totalPrice = om.getTotalPrice().add(om.getChangePrice());
		BigDecimal discountPrice = new BigDecimal("0");
		//查出所以已选的次卡
		List<TimesCardUsedRecord> listTimesCardRecord = om.getListTimesCardRecord();
		if(listTimesCardRecord!=null){
			for(int i=0;i<listTimesCardRecord.size();i++){
				TimesCardUsedRecord item = listTimesCardRecord.get(i);
				BigDecimal cardDiscount = item.getTimesCardType().getDiscountAmount();
				discountPrice = discountPrice.add(cardDiscount);
			}
		}
		
		
		//查出所有已选优惠券,计算折扣金额
		List<CouponOrderRelevance> listCouponOrderRelevance  = om.getListCouponOrderRelevance();
		Iterator<CouponOrderRelevance> it = listCouponOrderRelevance.iterator();
		while (it.hasNext()) {
			CouponOrderRelevance item = it.next();
			if(item.getStatus().equals(Constans.COUPON_ORDER_STATUS_CANUSE)){
				BigDecimal itemDiscountPrice = item.getCoupon().getDiscountAmount()==null?new BigDecimal("0"):new BigDecimal(item.getCoupon().getDiscountAmount().toString());
				discountPrice = discountPrice.add(itemDiscountPrice);
			}else{
				logger.info("不参与计算item.getStatus():"+item.getStatus());
			}
		}
		logger.info("计算后优惠金额:"+discountPrice);
		if(discountPrice.compareTo(totalPrice)==1){
			discountPrice = totalPrice;
		}
		return discountPrice;
	}
	@ApiOperation(value = "(暂时保留,下版本撤掉该方法)美甲师确认优惠券可用", notes = "")
	@RequestMapping(method={RequestMethod.POST},value="/confirmCouponsCanUse")
    public JsonResult<List<CouponOrderRelevance>> confirmCouponsCanUse(String token,Integer orderId){
		logger.info("美甲师确认优惠券可用");
		if(orderId==null){
			return new JsonResult<>(false,"订单id为空",null);
		}
		GogirlToken gt = gogirlTokenService.getTokenByToken_t(token);
		if(gt==null||gt.getUserTechnician()==null){
			return new JsonResult<>(false,JsonResult.TOKEN_NULL_CODE,null);
		}
		//美甲师确认券可用,拿到美甲师的technician_id设置到确认人
		CouponOrderRelevance updateTech = new CouponOrderRelevance();
		updateTech.setOrderId(orderId);
		updateTech.setStatus(Constans.COUPON_ORDER_STATUS_CANUSE);
		updateTech.setTechnicainId(gt.getUserTechnician().getTechnicianId());
		couponOrderRelevanceMapper.updateByOrderId(updateTech);
		
		//选中的券设置为已使用
		couponOrderRelevanceMapper.updateCouponCustomerRelevanceUse(orderId);
		
//		//订单设置优惠券价格和待付款状态		
//		List<CouponOrderRelevance> list = couponOrderRelevanceMapper.selectByOrderId(orderId);
//		double sum = 0;
//		for(int i=0;i<list.size();i++){
//			CouponOrderRelevance item = list.get(i);
//			Coupon coupon = item.getCoupon();
//			sum+=coupon.getDiscountAmount();
//		}
		OrderManage om = orderManageService.getOrderForDetail(orderId);
		BigDecimal discountPrice = countOrderDiscount(om);
		OrderManage ordermanage = new OrderManage();
		ordermanage.setId(om.getId());
		ordermanage.setDiscountPrice(discountPrice);
		ordermanage.setStatus(Constans.ORDER_STATUS_PAMENT);
		orderManageService.updateOrderStatus(ordermanage);
		
		
		return new JsonResult<>(true,JsonResult.APP_DEFINE_SUC);
	}
}
