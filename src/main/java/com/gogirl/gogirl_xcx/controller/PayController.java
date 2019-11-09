package com.gogirl.gogirl_xcx.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gogirl.gogirl_order.order_commons.config.Constans;
import com.gogirl.gogirl_order.order_commons.dto.OrderManage;
import com.gogirl.gogirl_order.order_commons.dto.OrderServe;
import com.gogirl.gogirl_order.order_commons.utils.JsonResult;
import com.gogirl.gogirl_order.order_manage.service.OrderManageService;
import com.gogirl.gogirl_order.order_manage.service.OrderServeService;
import com.gogirl.gogirl_technician.technician_commons.dto.TechnicianManage;
import com.gogirl.gogirl_technician.technician_user.service.TechnicianManageService;
import com.gogirl.gogirl_user.constant.GogirlProperties;
import com.gogirl.gogirl_user.dao.CustomerBalanceRecordMapper;
import com.gogirl.gogirl_user.entity.Coupon;
import com.gogirl.gogirl_user.entity.CouponCustomerRelevance;
import com.gogirl.gogirl_user.entity.Customer;
import com.gogirl.gogirl_user.entity.CustomerBalance;
import com.gogirl.gogirl_user.entity.CustomerBalanceRecord;
import com.gogirl.gogirl_user.entity.CustomerDepartmentRelevance;
import com.gogirl.gogirl_user.entity.CustomerDepartmentRelevanceKey;
import com.gogirl.gogirl_user.entity.DiscountConfig;
import com.gogirl.gogirl_user.service.CouponService;
import com.gogirl.gogirl_user.service.CustomerBalanceService;
import com.gogirl.gogirl_user.service.CustomerDepartmentRelevanceService;
import com.gogirl.gogirl_user.service.CustomerService;
import com.gogirl.gogirl_user.service.DiscountConfigService;
import com.gogirl.gogirl_user.service.balance.ChargeBalance;
import com.gogirl.gogirl_user.service.balance.ChargeBalanceNoBestow;
import com.gogirl.gogirl_user.service.balance.ConsumeBalance;
import com.gogirl.gogirl_user.service.balance.ConsumeBalanceXcx;
import com.gogirl.gogirl_user.service.myhttp.MyHttpPost;
import com.gogirl.gogirl_xcx.entity.GogirlToken;
import com.gogirl.gogirl_xcx.service.GogirlTokenService;
import com.gogirl.gogirl_xcx.service.SendMpTemplateMsgService;
import com.gogirl.gogirl_xcx.service.SendTechXcxTemplateMsgService;
import com.gogirl.gogirl_xcx.service.SendXcxTemplateMsgService;
import com.gogirl.gogirl_xcx.util.XmlUtil;

/**
 * @author hui
 */
@Controller
@RequestMapping("/pay")
@Api(tags={"4.支付"},value="支付")
public class PayController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Resource
	CustomerService customerService;
	@Resource
	GogirlTokenService gogirlTokenService;
	@Resource
	OrderManageService orderManageService;
	@Resource
    private MyHttpPost myHttpPost;
	@Resource
	GogirlProperties gogirlProperties;
	@Resource
	DiscountConfigService discountConfigService;
	@Autowired
	ChargeBalance chargeBalance;
	@Autowired
	CustomerBalanceService balanceService;
	@Autowired
	ConsumeBalance consumeBalance;
	@Autowired
	ConsumeBalanceXcx consumeBalanceXcx;
	@Autowired
	CustomerBalanceRecordMapper balanceRecordDao;
	@Autowired
	CouponService couponService;
	@Autowired
	CustomerBalanceService customerBalanceService;
	@Resource
	SendXcxTemplateMsgService sendXcxTemplateMsgService;
	@Resource
	SendMpTemplateMsgService sendMpTemplateMsgService;
	@Resource
	OrderServeService orderServeService;
	@Autowired
	ChargeBalanceNoBestow chargeBalanceNoBestow;
	@Resource
	private CustomerDepartmentRelevanceService customerDepartmentRelevanceService;
	@Resource
	SendTechXcxTemplateMsgService sendTechXcxTemplateMsgService;
	@Resource
	TechnicianManageService technicianManageService;
	/**
	 * 1用户端，订单，微信支付
	 */
	@ResponseBody
	@ApiOperation(value = "用户端，订单，微信支付")
	@RequestMapping(method={RequestMethod.POST},value="/unifiedOrder")
	public JsonResult<?> unifiedOrder(String token,Integer orderId,HttpServletRequest request){
		logger.info("********微信支付订单");
		if(token==null||token.isEmpty()){
			return new JsonResult<Object>(false,"入参token为空",null);
		}
		if(orderId==null){
			return new JsonResult<Object>(false,"入参orderId为空",null);
		}
		logger.info("获取微信支付签名token:"+token+"    .orderId:"+orderId);
		GogirlToken gogirlToken = gogirlTokenService.getTokenByToken(token);
		if(gogirlToken==null){
			return new JsonResult<Object>(false,"token过期",null);
		}
		if(gogirlToken.getCustomer()==null){
			return new JsonResult<Object>(false,"找不到用户customerId:"+gogirlToken.getCustomerId(),null);
		}
		OrderManage order = orderManageService.getOrderForDetail(orderId);
		if(order==null){
			return new JsonResult<Object>(false,"找不到订单orderId:"+orderId,null);
		}
		//判断订单是否可以支付
		JsonResult jr = checkCanPay(order.getStatus());
		if(jr!=null){return jr;}
    	if(order.getOrderUser()==null||gogirlToken.getCustomerId()==null||!order.getOrderUser().equals(gogirlToken.getCustomerId())){
    		return new JsonResult< Object>(false,"抱歉，暂时不支持为她人付款。",null);
    	}
		//订单号最长26位,不足就补齐
		StringBuffer orderNox = new StringBuffer(order.getOrderNo());
		while(orderNox.length()<26){//订单号最长支持26位字符
			orderNox.append('x');
		}
		//算钱-优惠券-已经付过的款
		BigDecimal big100 = new BigDecimal("100");
		BigDecimal totalPrice = order.getTotalPrice()==null?new BigDecimal("0"):order.getTotalPrice();
		BigDecimal changePrice = order.getChangePrice()==null?new BigDecimal("0"):order.getChangePrice();
		BigDecimal discountPrice = order.getDiscountPrice()==null?new BigDecimal("0"):order.getDiscountPrice();
		BigDecimal payPrice = totalPrice.add(changePrice).subtract(discountPrice);
		BigDecimal countPartPayPrice = countPartPay(payPrice,orderId);//减去已经支付过的金额
		//发起统一支付
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("deviceInfo","设备号" );
		map.put("body", "gogirl美甲美睫"+order.getDepartmentId());
		map.put("detail", "是否显示具体做了哪些服务");
		map.put("attach", "o");
		map.put("outTradeNo", orderNox+String.valueOf(new Date().getTime()).substring(8));
		map.put("totalFee", String.valueOf(countPartPayPrice.multiply(big100).intValue()));
		map.put("spbillCreateIp", getIpAddress(request));
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		map.put("timeStart", df.format(new Date()));
		map.put("notifyUrl", gogirlProperties.getNotifyOrder());
		map.put("tradeType", "JSAPI");
		map.put("openid", gogirlToken.getCustomer().getOpenid1());
		map.put("sceneInfo","东方宝泰店" );
		return myHttpPost.httpRequestJsonData(gogirlProperties.getGOGIRLMP()+"/gogirl_mp/pay/createOrder", map);
	}

	/**
	 * 2用户端，微信支付回调，包括充值和支付订单.发券
	 */
	@ResponseBody
	@ApiOperation(value = "用户端，微信支付回调，包括充值和支付订单")
	@RequestMapping(method={RequestMethod.POST},value="/notifyOrder")
	public String notifyOrder(@RequestBody String xmlData,HttpServletRequest request) {
		logger.info("支付回调参数xmlData:"+xmlData);
        Map<String, String> map;
		try {
			map = XmlUtil.readStringXml(xmlData);
	        if(map.get("return_code").equals("SUCCESS")&&map.get("result_code").equals("SUCCESS")){
	        	String outTradeNo = map.get("out_trade_no");
        		outTradeNo =outTradeNo.substring(0,26);
	    		while(outTradeNo.endsWith("x")){
	    			outTradeNo = outTradeNo.substring(0, outTradeNo.length()-1);
	    		}
        		OrderManage order=orderManageService.getOrderForOrderNo(outTradeNo);
        		if(order!=null){
        			//算钱-优惠券-已经付过的款
        			BigDecimal big100 = new BigDecimal("100");
        			BigDecimal totalPrice = order.getTotalPrice()==null?new BigDecimal("0"):order.getTotalPrice();
        			BigDecimal changePrice = order.getChangePrice()==null?new BigDecimal("0"):order.getChangePrice();
        			BigDecimal discountPrice = order.getDiscountPrice()==null?new BigDecimal("0"):order.getDiscountPrice();
        			BigDecimal payPrice = totalPrice.add(changePrice).subtract(discountPrice);
        			BigDecimal countPartPayPrice = countPartPay(payPrice,order.getId());//减去已经支付过的金额
        			//记录消费
                    customerBalanceService.insertSelective(order.getOrderUser(), order.getDepartmentId(), "",Integer.valueOf(map.get("total_fee").toString()), order.getId(), order.getRemark(),1 );
        			//记录订单已支付
        			OrderManage updateOrder = new OrderManage();
        			updateOrder.setId(order.getId());
        			updateOrder.setStatus(Constans.ORDER_STATUS_EVALUATE);
        			Double total_fee_double = Double.valueOf(map.get("total_fee"))/100.0;
        			updateOrder.setMultiplePaymentType("[{\"type\":1,\"price\":"+total_fee_double+"}]");
        			updateOrder.setPaymentType(Constans.ORDER_PAYMENT_WEI);
        			updateOrder.setFinishTime(new Date());
        			//修改订单记录+计算业绩+赠送卸甲券
        			int sendRemoveCouponNum = orderManageService.xcxUpdateOrderFinash(order,updateOrder,null);
        			//赠送优惠券
                    Coupon coupon=couponService.sendTicketAfterOrder(order.getOrderUser(), total_fee_double);//赠送优惠券
                    //发送消息模板,提醒支付成功
                    List<TechnicianManage> listTech = new ArrayList<TechnicianManage>();
                    String serves = "";
                    List<OrderServe> listOrderServe = order.getListOrderServer();
                    for(int i=0;i<listOrderServe.size();i++){
                    	OrderServe item = listOrderServe.get(i);
                    	serves=serves+","+item.getServe().getName();
            			if(item.getTechnicianId()!=null){
            				String techs[] = item.getTechnicianId().split(",");
            				for(int j=0;j<techs.length;j++){
            					if(!techs[j].trim().isEmpty()){
                    				TechnicianManage technicianManage = technicianManageService.getTechnicianManageForDetail(Integer.parseInt(techs[j]));
                    				if(technicianManage!=null){
                    					listTech.add(technicianManage);
                    				}
            					}
            				}
            			}
                    }
                    if(serves.length()>0){
                    	serves = serves.substring(1);
                    }
                    CustomerBalance customerBalance = balanceService.getCustomerBalance(order.getOrderUser());//查询余额
                    String sendCouponString = coupon==null?"":coupon.getName()+"、";
                    sendCouponString += sendRemoveCouponNum>0?"卸甲券":"";
                    if(sendCouponString.endsWith("、"))sendCouponString = sendCouponString.substring(0, sendCouponString.length()-1);
                    sendXcxTemplateMsgService.sendPaySuccessMsg(map.get("openid"),order.getId(), outTradeNo, order.getListOrderServer().get(0).getServe().getName(),
                    		String.valueOf(total_fee_double), "微信支付", String.valueOf(Double.valueOf(customerBalance.getBalance())/100.0),
                    		sendCouponString.isEmpty()?"无":sendCouponString, order.getDepartmentName());//发送消息提醒
                    //发送支付成功通知到美甲师
        			String url = "pages/order/orders?orderId="+order.getId().toString();
        			SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日HH时mm分");
            		for(int i=0;i<listTech.size();i++){//客户预约成功,提醒美甲师
//            			String scheduledTimetem = sdf.format(listTech.get(i).getTime());
            			sendTechXcxTemplateMsgService.sendPaySuccessMsg
            			(listTech.get(i).getOpenid(), url, order.getOrderNo(), serves, payPrice.toString(), sdf.format(new Date()), "微信支付", order.getStoreScheduleUsername(), order.getTelephone());
//            			(listTech.get(i).getOpenid(), url, scheduledTimetem, storeName, listTech.get(i).getName(), serveName, scheduleManage.getStoreScheduleUsername(), scheduleManage.getTelephone());
            		}

        		}else{
        			logger.error("不可能,给钱之后订单号没有1");
        			//不可能，给钱之后订单号没有
        		}
	        }
		} catch (DocumentException e) {
			logger.error("不可能,给钱之后订单号没有5,e:"+e.getMessage());
		}
		return "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
	}
	
	/**
	 * 3会员卡支付。发券
	 */
	@ResponseBody
	@ApiOperation(value = "会员卡支付")
	@RequestMapping(method={RequestMethod.POST},value="/consumeBalance")
	public JsonResult<Object> consumeBalance(String token,String formId,Integer orderId,HttpServletRequest request) {
		logger.info("********会员卡支付订单");
    	if(token==null){
    		return new JsonResult<Object>(false,"token为空",null);
    	}
    	if(orderId==null){
    		return new JsonResult<Object>(false,"orderId为空",null);
    	}
    	GogirlToken gogirlToken = gogirlTokenService.getTokenByToken(token);
    	if(gogirlToken==null){
    		return new JsonResult<Object>(false,JsonResult.TOKEN_NULL_CODE,"token无效");
    	}
    	if(gogirlToken.getCustomer()==null){
    		return new JsonResult<Object>(false,"找不到用户id："+gogirlToken.getCustomerId(),null);
    	}
    	Integer customerId = gogirlToken.getCustomerId();
    	OrderManage order = orderManageService.getOrderForDetail(orderId);
    	if(order==null){
    		return new JsonResult<Object>(false,"找不到订单orderId:"+orderId,null);
    	}
    	if(order.getOrderUser()==null||gogirlToken.getCustomerId()==null||!order.getOrderUser().equals(gogirlToken.getCustomerId())){
    		return new JsonResult< Object>(false,"抱歉，暂时不支持为她人付款。",null);
    	}
		//算钱-优惠券-已经付过的款
		BigDecimal big100 = new BigDecimal("100");
		BigDecimal totalPrice = order.getTotalPrice()==null?new BigDecimal("0"):order.getTotalPrice();
		BigDecimal changePrice = order.getChangePrice()==null?new BigDecimal("0"):order.getChangePrice();
		BigDecimal discountPrice = order.getDiscountPrice()==null?new BigDecimal("0"):order.getDiscountPrice();
		BigDecimal payPrice = totalPrice.add(changePrice).subtract(discountPrice);
		BigDecimal countPartPayPrice = countPartPay(payPrice,orderId);//减去已经支付过的金额
		//扣款+记录消费流水
		consumeBalanceXcx.setDepartmentId(order.getDepartmentId());
		JsonResult result = null;
		try {
			result = balanceService.balanceChangeXcx(order,customerId, countPartPayPrice.multiply(big100).intValue(),consumeBalanceXcx,2 , -1);
			if(result.getSuccess()==false){
				return result;
			}else if(result.getMessage().equals("0003")){
				return result;
			}else{
				
			}
		} catch (Exception e) {
			logger.info(":"+e.getMessage());
			return new JsonResult<>(false,e.getMessage(),null);
		}
		//修改订单记录+计算业绩+赠送卸甲券
		OrderManage updateOrder = new OrderManage();
		updateOrder.setId(order.getId());
		updateOrder.setStatus(Constans.ORDER_STATUS_EVALUATE);
		updateOrder.setPaymentType(Constans.ORDER_PAYMENT_MEMBER);
		updateOrder.setFinishTime(new Date());
		int sendRemoveCouponNum = orderManageService.xcxUpdateOrderFinash(order,updateOrder,formId);//修改订单状态
		//赠送优惠券
        Coupon coupon=couponService.sendTicketAfterOrder(order.getOrderUser(), payPrice.doubleValue());//赠送优惠券
        //发送消息模板,提醒支付成功
        String sendCouponString = coupon==null?"":coupon.getName()+"、";
        sendCouponString += sendRemoveCouponNum>0?"卸甲券":"";
        if(sendCouponString.endsWith("、"))sendCouponString = sendCouponString.substring(0, sendCouponString.length()-1);
        sendXcxTemplateMsgService.sendPaySuccessMsg(gogirlToken.getCustomer().getOpenid1(),order.getId(), order.getOrderNo(), order.getListOrderServer().get(0).getServe().getName(),
        		String.valueOf(countPartPayPrice.doubleValue()), "会员卡支付",String.valueOf(consumeBalanceXcx.getBalance().getBalance()/100.0)+"元",
        		sendCouponString.isEmpty()?"无":sendCouponString, order.getDepartmentName());//发送消息提醒
		return new JsonResult<Object>(true,JsonResult.APP_DEFINE_SUC,null);
	}	
	
	/**
	 * 4用户端，仅选择支付方式，待店员确认是否完成订单。5：刷pos机7：大众点评团购
	 */
	@ResponseBody
    @ApiOperation(value="用户端，仅选择支付方式，待店员确认是否完成订单。5：刷pos机7：大众点评团购")
    @RequestMapping(method={RequestMethod.POST},value = "/chosePayType")
    public JsonResult<String> chosePayType(String token, Integer orderId,Integer payType){
		logger.info("********用户选择支付 方式");
    	if(token==null){
    		return new JsonResult<String>(false,"token为空",null);
    	}
    	if(orderId==null){
    		return new JsonResult<String>(false,"orderId为空",null);
    	}
    	GogirlToken gogirlToken = gogirlTokenService.getTokenByToken(token);
    	if(gogirlToken==null){
    		return new JsonResult<String>(false,JsonResult.TOKEN_NULL_CODE,"token无效");
    	}
    	if(gogirlToken.getCustomer()==null){
    		return new JsonResult<String>(false,"找不到用户id："+gogirlToken.getCustomerId(),null);
    	}
    	OrderManage order = orderManageService.getOrderForDetail(orderId);
    	if(order==null){
    		return new JsonResult<String>(false,"找不到订单orderId:"+orderId,null);
    	}
		//判断订单是否可以支付
		JsonResult jr = checkCanPay(order.getStatus());
		if(jr!=null){return jr;}
		//设置待美甲师确认
    	OrderManage orderManage = new OrderManage();
    	orderManage.setId(orderId);
    	orderManage.setPaymentType(payType);
    	orderManage.setStatus(Constans.ORDER_STATUS_WAIT_SURE);
    	orderManageService.updateOrderStatus(orderManage);
        return new JsonResult<String>(true,JsonResult.APP_DEFINE_SUC,null);
    }
	
	/**
	 * 5美甲师端收款。发券
	 */
	@ResponseBody
    @ApiOperation(value="美甲师端，收款。2：会员卡收款；5：刷pos机；7：大众点评团购，8.微信扫二维码支付")
    @RequestMapping(method={RequestMethod.POST},value = "/receiveMoney")
    public JsonResult<?> receiveMoney(String token, Integer orderId,Integer payType,String remark,HttpServletRequest request){
		logger.info("********美甲师端收款");
    	if(token==null){
    		return new JsonResult<Object>(false,"token为空",null);
    	}
    	if(orderId==null){
    		return new JsonResult<Object>(false,"orderId为空",null);
    	}
    	GogirlToken gogirlToken = gogirlTokenService.getTokenByToken_t(token);
    	if(gogirlToken==null){
    		return new JsonResult<Object>(false,JsonResult.TOKEN_NULL_CODE,"token无效");
    	}
    	OrderManage order = orderManageService.getOrderForDetail(orderId);
    	if(order==null){
    		return new JsonResult<Object>(false,"找不到订单orderId:"+orderId,null);
    	}
		//判断订单是否可以支付
		JsonResult jr = checkCanPay(order.getStatus());
		if(jr!=null){return jr;}
		
    	if(order.getOrderUser()==null){
    		return new JsonResult<Object>(false,"找不到用户id",null);
    	}
		//算钱-优惠券-已经付过的款
		BigDecimal big100 = new BigDecimal("100");
		BigDecimal totalPrice = order.getTotalPrice()==null?new BigDecimal("0"):order.getTotalPrice();
		BigDecimal changePrice = order.getChangePrice()==null?new BigDecimal("0"):order.getChangePrice();
		BigDecimal discountPrice = order.getDiscountPrice()==null?new BigDecimal("0"):order.getDiscountPrice();
		BigDecimal payPrice = totalPrice.add(changePrice).subtract(discountPrice);
		BigDecimal countPartPayPrice = countPartPay(payPrice,orderId);//减去已经支付过的金额
		//会员卡支付和其他支付方式
    	if(payType!=null&&payType.equals(Constans.ORDER_PAYMENT_MEMBER)){
    		int row = 0;
    		consumeBalance.setDepartmentId(order.getDepartmentId());
    		try {
    			row = balanceService.balanceChange(request,order.getOrderUser(), countPartPayPrice.multiply(big100).intValue(),consumeBalance , -1,String.valueOf(orderId),null,orderId);
    		} catch (Exception e) {
    			logger.info(":"+e.getMessage());
    			return new JsonResult<>(false,e.getMessage(),null);
    		}
    		if(row<1){
    			return new JsonResult<Object>(false,"扣费失败，请重试",null);
    		}
    	}else{
    		customerBalanceService.insertSelective(order.getOrderUser(), order.getDepartmentId(), "",
    				countPartPayPrice.multiply(big100).intValue(), order.getId(), order.getRemark(),1 );//记录消费
    	}
		//修改订单记录+计算业绩+赠送卸甲券
		OrderManage updateOrder = new OrderManage();
		updateOrder.setId(orderId);
		updateOrder.setPaymentType(payType);
		updateOrder.setStatus(Constans.ORDER_STATUS_EVALUATE);
		updateOrder.setFinishTime(new Date());
		updateOrder.setMessage(remark);
    	int sendRemoveCouponNum = orderManageService.xcxUpdateOrderFinash(order,updateOrder,null);
		//赠送优惠券
		Coupon coupon=couponService.sendTicketAfterOrder(order.getOrderUser(), payPrice.doubleValue());
        //发送消息模板,提醒支付成功
        String sendCouponString = coupon==null?"":coupon.getName()+"、";
        sendCouponString += sendRemoveCouponNum>0?"卸甲券":"";
        if(sendCouponString.endsWith("、"))sendCouponString = sendCouponString.substring(0, sendCouponString.length()-1);
		Customer c = customerService.selectByPrimaryKey(order.getOrderUser());
		if(c!=null&&c.getOpenid1()!=null){
			sendXcxTemplateMsgService.sendPaySuccessMsg(c.getOpenid1(),order.getId(), order.getOrderNo(), order.getListOrderServer().get(0).getServeName(),
					String.valueOf(countPartPayPrice.doubleValue()), "会员卡支付",c.getCustomerBalance()==null?"0":String.valueOf(c.getCustomerBalance().getBalance()),
							sendCouponString.isEmpty()?"无":sendCouponString, order.getDepartmentName());//发送消息提醒
			        return new JsonResult<Object>(true,JsonResult.APP_DEFINE_SUC,null);
		}
		return new JsonResult<Object>(true,JsonResult.APP_DEFINE_SUC,null);
	}
	
	


	/**
	 * 美甲师端，充值会员卡
	 */
	@ResponseBody
	@ApiOperation(value = "美甲师端，充值会员卡")
	@RequestMapping(method={RequestMethod.POST},value="/chargeCustomerCard")
	public JsonResult chargeCustomerCard(HttpServletRequest request,String token,String name,String phone,Double amount,Integer source,String refereeId,String remark) {
		Customer customer = customerService.selectByPhone(phone);
		GogirlToken gt = gogirlTokenService.getTokenByToken_t(token);
		if(gt==null){
			return new JsonResult<>(false,JsonResult.TOKEN_NULL_CODE);
		}
		Integer departmentId = gt.getUserTechnician().getDepartmentId();
		
		if(customer==null){//新建会员
			customer = new Customer();
			customer.setStoreRecordRealName(name);
			customer.setPhone(phone);
			int id = customerService.insertSelective(customer);
			if(id==0){
				return new JsonResult(false,"新增会员失败.",null);
			}else{
				customer.setId(id);
			}
		}else if(name!=null&&!name.isEmpty()){
			Customer record = new Customer();
			record.setId(customer.getId());
			record.setStoreRecordRealName(name);
			customerService.updateByPrimaryKey(record);
		}
		customerDepartmentRelevanceService.insertDepartmentRelevanceIfNotExist(customer.getId() ,departmentId,4,new Date());

		DiscountConfig dc = discountConfigService.selectByCharge((int)(amount*100));

		
		CustomerBalance card = balanceService.getCustomerBalance2(customer.getId());
		Integer type = 1;
		//设置type和余额和订单id
		if(card==null){
			type=2;
		}else{
			
		}
		int a = (int)(amount*100)+dc.getBestowAmount();
		String orderId = getOrderIdByTime();
		int row = 0;
		chargeBalanceNoBestow.setOrderAmount((int)(amount*100));
		chargeBalanceNoBestow.setBestowAmount(dc.getBestowAmount());
		chargeBalanceNoBestow.setSource(source);
		chargeBalanceNoBestow.setReferee(refereeId);
		chargeBalanceNoBestow.setRemark(remark);
		chargeBalanceNoBestow.setType(type); 
		chargeBalanceNoBestow.setDepartmentId(departmentId);
		try {
			row = balanceService.balanceChange(request,customer.getId(), a, chargeBalanceNoBestow, type,orderId,null,null);
		} catch (Exception e) {
			logger.info(e.getMessage());
			return new JsonResult(false,e.getMessage(),null);
		}
		if(row<1){
			return new JsonResult(false,"建卡失败，请重试",null);
		}
		
		//查询用户信息
		if(customer!=null&&customer.getOpenid()!=null){
			String openid = customer.getOpenid();
			//时间
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日HH时mm分");
			String time = simpleDateFormat.format(new Date());
			//余额和消费金额
			CustomerBalanceRecord customerBalanceRecord = balanceRecordDao.selectByPrimaryKey(chargeBalanceNoBestow.getRecordId());
			if(customerBalanceRecord!=null){
				String realAmount = customerBalanceRecord.getOrderAmount()==null?"0":String.valueOf(customerBalanceRecord.getOrderAmount()/100.00);
				String realBestow = customerBalanceRecord.getBestowAmount()==null?"0":String.valueOf(customerBalanceRecord.getBestowAmount()/100.00);
				String currentBalance = customerBalanceRecord.getCurrentBalance()==null?"0":String.valueOf(customerBalanceRecord.getCurrentBalance()/100.00);
				//姓名
				String customerName = "无名字";
				if(customer.getStoreRecordRealName()!=null&&!customer.getStoreRecordRealName().isEmpty()){
					customerName = customer.getStoreRecordRealName();
				}else if(customer.getRealName()!=null&&!customer.getRealName().isEmpty()){
					customerName = customer.getRealName();
				}else if(customer.getNickname()!=null&&!customer.getNickname().isEmpty()){
					customerName = customer.getNickname();
				}else if(customer.getPhone()!=null&&!customer.getPhone().isEmpty()){
					customerName = customer.getPhone();
				}
				//发送模板消息
				sendMpTemplateMsgService.sendChargeMsg(openid, time, customerName,"砖石会员卡","充值￥"+realAmount+"，送￥"+realBestow+"","余额￥"+currentBalance+"");
			}
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("row", row);
		return new JsonResult(true,"充值成功",map);
	}

	/**
	 * 用户端，微信充值会员卡
	 */
	@ResponseBody
	@ApiOperation(value = "用户端，充值会员卡")
	@RequestMapping(method={RequestMethod.POST},value="/chargeBalance")
	public JsonResult<?> chargeBalance(String token,Integer chargeId,HttpServletRequest request) {
		if(token==null||token.isEmpty()){
			return new JsonResult<Object>(false,"入参token为空",null);
		}
		if(chargeId==null){
			return new JsonResult<Object>(false,"入参orderId为空",null);
		}
		logger.info("获取微信支付签名token:"+token+"    .chargeId:"+chargeId);
		GogirlToken gt = gogirlTokenService.getTokenByToken(token);
		if(gt==null){
			return new JsonResult<Object>(false,"token过期",null);
		}
		if(gt.getCustomer()==null){
			return new JsonResult<Object>(false,"找不到用户customerId:"+gt.getCustomerId(),null);
		}
		DiscountConfig discountConfig = discountConfigService.selectByPrimaryKey(chargeId);
		if(discountConfig==null){
			return new JsonResult<Object>(false,"找不到配置chargeId:"+chargeId,null);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("deviceInfo","设备号");
		map.put("body", "充值"+discountConfig.getChargeAmount()/100+"赠送"+discountConfig.getBestowAmount()/100);
		map.put("detail", "详情");
		map.put("attach", "c");
		String outTradeNo = "c"+chargeId+String.valueOf(gt.getCustomerId())+String.valueOf((int)(Math.random()*10000));
		map.put("outTradeNo", outTradeNo);
		map.put("totalFee", String.valueOf(discountConfig.getChargeAmount()));
		map.put("spbillCreateIp", getIpAddress(request));
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		map.put("timeStart", df.format(new Date()));
		map.put("notifyUrl", gogirlProperties.getNotifyCharge());
		map.put("tradeType", "JSAPI");
		map.put("openid", gt.getCustomer().getOpenid1());
		map.put("sceneInfo","gogirl美甲美睫沙龙");
		return myHttpPost.httpRequestJsonData(gogirlProperties.getGOGIRLMP()+"/gogirl_mp/pay/createOrder", map);
	}

	/**
	 * 用户端，微信支付回调，包括充值和支付订单.发券
	 */
	@ResponseBody
	@ApiOperation(value = "用户端，微信支付回调，包括充值和支付订单")
	@RequestMapping(method={RequestMethod.POST},value="/notifyCharge")
	public String notifyCharge(@RequestBody String xmlData,HttpServletRequest request) {
		logger.info("充值会员卡,微信支付回调参数xmlData:"+xmlData);
        Map<String, String> map;
		try {
			map = XmlUtil.readStringXml(xmlData);
	        if(map.get("return_code").equals("SUCCESS")&&map.get("result_code").equals("SUCCESS")){
	        	String outTradeNo = map.get("out_trade_no");
        		String openid = map.get("openid");
        		String total_fee = map.get("total_fee");
        		Customer customer = customerService.selectByOpenid1(openid);
        		int row = 0;
        		try {
        			logger.info("customer.getId():"+customer.getId());
        			logger.info("total_fee:"+total_fee);
        			logger.info("chargeBalance:"+chargeBalance);
        			logger.info("outTradeNo:"+outTradeNo);
        			if(customer!=null&&customer.getCustomerBalance()!=null&&customer.getCustomerBalance().getTotalCharge()!=null&&customer.getCustomerBalance().getTotalCharge()>0){
        				chargeBalance.setType(1);
        			}else{
        				chargeBalance.setType(2);
        			}
        			row = balanceService.balanceChange(request,customer.getId(), Integer.valueOf(total_fee), chargeBalance, 1,outTradeNo,null,null);
        		} catch (Exception e) {
        			logger.error("不可能,给钱之后订单号没有2"+e.getMessage());
        		}
        		if(row<1){
        			logger.error("不可能,给钱之后订单号没有3");
        		}
        		//时间
        		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日HH时mm分");
        		String time = simpleDateFormat.format(new Date());
        		//余额和消费金额
        		CustomerBalanceRecord customerBalanceRecord = balanceRecordDao.selectByPrimaryKey(chargeBalance.getRecordId());
        		if(customerBalanceRecord!=null){
        			String realAmount = customerBalanceRecord.getOrderAmount()==null?"0":String.valueOf(customerBalanceRecord.getOrderAmount()/100.00);
        			String realBestow = customerBalanceRecord.getBestowAmount()==null?"0":String.valueOf(customerBalanceRecord.getBestowAmount()/100.00);
        			String currentBalance = customerBalanceRecord.getCurrentBalance()==null?"0":String.valueOf(customerBalanceRecord.getCurrentBalance()/100.00);
        			//姓名
        			String customerName = "尊敬的客户";
        			if(customer.getStoreRecordRealName()!=null&&!customer.getStoreRecordRealName().isEmpty()){
        				customerName = customer.getStoreRecordRealName();
        			}else if(customer.getRealName()!=null&&!customer.getRealName().isEmpty()){
        				customerName = customer.getRealName();
        			}else if(customer.getNickname()!=null&&!customer.getNickname().isEmpty()){
        				customerName = customer.getNickname();
        			}else if(customer.getPhone()!=null&&!customer.getPhone().isEmpty()){
        				customerName = customer.getPhone();
        			}
                    //设置今天推荐人
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    customerBalanceService.setChargeReferees(sdf.format(new Date()));

        			//发送模板消息
                    sendXcxTemplateMsgService.sendChargeMsg(openid, time, realAmount, realBestow, "充值成功", currentBalance, "gogirl美甲美睫沙龙");
        		}
	        }
		} catch (DocumentException e) {
			
		}
		return "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
	}

	
	/**********以下是辅助功能***/
	
	/**
	 * 计算订单金额,减去已经支付过的金额
	 */
	public BigDecimal countPartPay(BigDecimal totalAndDiscount,Integer orderId) {
		List<CustomerBalanceRecord> list = customerBalanceService.listPartPay(orderId);
		Iterator<CustomerBalanceRecord> it = list.iterator();
		Integer totalInt = totalAndDiscount.multiply(new BigDecimal("100")).intValue();
		while(it.hasNext()){
			CustomerBalanceRecord item = it.next();
			if(item!=null&&item.getOrderAmount()!=null&&!item.getOrderAmount().equals(0)){
				totalInt = totalInt-item.getOrderAmount();
			}
		}
		return new BigDecimal(totalInt).divide(new BigDecimal("100"),2, BigDecimal.ROUND_HALF_UP);
	}

	/**
	 * 获取ip
	 */
	public String getIpAddress(HttpServletRequest request) {  
	    String ip = request.getHeader("x-forwarded-for");  
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
	        ip = request.getHeader("Proxy-Client-IP");  
	    }  
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
	        ip = request.getHeader("WL-Proxy-Client-IP");  
	    }  
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
	        ip = request.getHeader("HTTP_CLIENT_IP");  
	    }  
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
	        ip = request.getHeader("HTTP_X_FORWARDED_FOR");  
	    }  
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
	        ip = request.getRemoteAddr();  
	    }  
	    return ip;  
	} 
	
	/**
	 * 美甲师端充值会员卡订单id
	 */
	public static String getOrderIdByTime() {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
		String newDate=sdf.format(new Date());
		String result="";
		Random random=new Random();
		for(int i=0;i<3;i++){
		result+=random.nextInt(10);
		}
		return newDate+result;
	}

	/**
	 * 支付前检查订单状态status
	 */
	public JsonResult<?> checkCanPay(Integer status) {
		//1:正在服务 2:待付款#  3:未评价# 4:已完成#5: 已删除#6:已预约待接单 7用户取消8待美甲师确认9系统取消订单
		logger.info("支付前检查订单状态status:"+status);
		if(status.equals(1)){
			return new JsonResult<>(false,"服务中的订单暂时不能支付,请联系美甲师结束服务.");
		}else if(status.equals(2)||status.equals(8)){
			return null;
		}else if(status.equals(3)||status.equals(4)){
			return new JsonResult<>(false,"订单已支付,无需重复支付.");
		}else if(status.equals(5)){
			return new JsonResult<>(false,"订单已删除,不能再进行支付");
		}else if(status.equals(6)){
			return new JsonResult<>(false,"订单处于待接单,不能支付");
		}else if(status.equals(7)){
			return new JsonResult<>(false,"用户已取消订单,不能支付");
		}else if(status.equals(9)){
			return new JsonResult<>(false,"订单超时未接单,系统取消订单,不能支付");
		}else{
			return new JsonResult<>(false,"订单状态异常,请联系管理员status:"+status);
		}
	}
}
