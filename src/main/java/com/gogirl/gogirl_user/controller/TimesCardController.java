package com.gogirl.gogirl_user.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import springfox.documentation.annotations.ApiIgnore;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gogirl.gogirl_order.order_commons.config.Constans;
import com.gogirl.gogirl_order.order_commons.dto.OrderManage;
import com.gogirl.gogirl_order.order_commons.dto.OrderServe;
import com.gogirl.gogirl_order.order_commons.utils.JsonResult;
import com.gogirl.gogirl_order.order_manage.service.OrderManageService;
import com.gogirl.gogirl_technician.technician_commons.dto.TechnicianManage;
import com.gogirl.gogirl_user.constant.GogirlProperties;
import com.gogirl.gogirl_user.dao.TimesCardServeRelevanceMapper;
import com.gogirl.gogirl_user.dao.TimesCardServeTypeRelevanceMapper;
import com.gogirl.gogirl_user.dao.TimesCardTypeMapper;
import com.gogirl.gogirl_user.entity.Coupon;
import com.gogirl.gogirl_user.entity.CouponCustomerRelevance;
import com.gogirl.gogirl_user.entity.Customer;
import com.gogirl.gogirl_user.entity.CustomerBalance;
import com.gogirl.gogirl_user.entity.TimesCardCustomerRelevance;
import com.gogirl.gogirl_user.entity.TimesCardServeRelevance;
import com.gogirl.gogirl_user.entity.TimesCardType;
import com.gogirl.gogirl_user.service.CouponService;
import com.gogirl.gogirl_user.service.CustomerService;
import com.gogirl.gogirl_user.service.RedirectService;
import com.gogirl.gogirl_user.service.TimesCardService;
import com.gogirl.gogirl_user.service.myhttp.MyHttpPost;
import com.gogirl.gogirl_xcx.dao.GogirlConfigMapper;
import com.gogirl.gogirl_xcx.entity.GogirlConfig;
import com.gogirl.gogirl_xcx.entity.GogirlToken;
import com.gogirl.gogirl_xcx.service.GogirlTokenService;
import com.gogirl.gogirl_xcx.util.XmlUtil;

@RestController
@RequestMapping("/timescard")
@Api(tags = { "13.次卡" },value = "")
public class TimesCardController {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	@Resource
	TimesCardService timesCardService;
	@Resource
	CustomerService customerService;
	@Resource
	GogirlProperties gogirlProperties;
	@Resource
	GogirlTokenService gogirlTokenService;
	@Resource
	OrderManageService orderManageService;
	@Resource
    private MyHttpPost myHttpPost;

	
	//查询次卡种类列表
	@ApiOperation(value = "查询次卡种类列表", notes = "")
	@RequestMapping(method={RequestMethod.GET},value="/getTimesCardTypeList")
    public JsonResult<List<TimesCardType>> getTimesCardTypeList(){
		return new JsonResult<List<TimesCardType>>(true, JsonResult.APP_DEFINE_SUC, timesCardService.getTimesCardTypeList());
    }
	//查询次卡种类详情
	@ApiOperation(value = "查询次卡种类详情", notes = "")
	@RequestMapping(method={RequestMethod.GET},value="/getTimesCardTypeDetail")
    public JsonResult<TimesCardType> getTimesCardTypeDetail(Integer id){
		return new JsonResult<TimesCardType>(true, JsonResult.APP_DEFINE_SUC, timesCardService.getTimesCardTypeDetail(id));
    }
	//用户购买次卡
	@ApiOperation(value = "用户购买次卡", notes = "")
	@RequestMapping(method={RequestMethod.POST},value="/buyTimesCard")
    public JsonResult<?> buyTimesCard(String token ,Integer timesCardId,HttpServletRequest request){
		logger.info("********用户购买次卡");
		if(token==null||token.isEmpty()){
			return new JsonResult<Object>(false,"入参token为空",null);
		}
		if(timesCardId==null){
			return new JsonResult<Object>(false,"入参timesCardId为空",null);
		}
		logger.info("获取微信支付签名token:"+token+"    .orderId:"+timesCardId);
		GogirlToken gogirlToken = gogirlTokenService.getTokenByToken(token);
		if(gogirlToken==null){
			return new JsonResult<Object>(false,"token过期",null);
		}
		if(gogirlToken.getCustomer()==null){
			return new JsonResult<Object>(false,"找不到用户customerId:"+gogirlToken.getCustomerId(),null);
		}
		TimesCardType tct = timesCardService.getTimesCardTypeDetail(timesCardId);
		if(tct==null){
			return new JsonResult<Object>(false,"找不到次卡id:"+timesCardId,null);
		}
		//计算次卡的价钱
		BigDecimal sumAmount = tct.getPayAmount().multiply(new BigDecimal(tct.getSumTimes()));
		
		String outTradeNo = "t-"+timesCardId+"-"+gogirlToken.getCustomer().getId()+"-"+String.valueOf(gogirlToken.getCustomerId())+String.valueOf((int)(Math.random()*10000));		
		
		
		
		//发起统一支付
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("deviceInfo","设备号" );
		map.put("body", "gogirl美甲美睫");
		map.put("detail", "是否显示具体做了哪些服务");
		map.put("attach", "t");
		map.put("outTradeNo", outTradeNo);
		map.put("totalFee", String.valueOf(sumAmount.multiply(new BigDecimal("100")).intValue()));
		map.put("spbillCreateIp", getIpAddress(request));
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		map.put("timeStart", df.format(new Date()));
		map.put("notifyUrl", gogirlProperties.getNotifyTimesCard());
		map.put("tradeType", "JSAPI");
		map.put("openid", gogirlToken.getCustomer().getOpenid1());
		map.put("sceneInfo","东方宝泰店" );
		return myHttpPost.httpRequestJsonData(gogirlProperties.getGOGIRLMP()+"/gogirl_mp/pay/createOrder", map);
    }
	//购买次卡,微信支付回调
	@ApiOperation(value = "购买次卡,微信支付回调", notes = "")
	@RequestMapping(method={RequestMethod.POST},value="/notifyTimesCard")
    public String notifyTimesCard(@RequestBody String xmlData,HttpServletRequest request){
		logger.info("支付回调参数xmlData:"+xmlData);
        Map<String, String> map;
		try {
			map = XmlUtil.readStringXml(xmlData);
	        if(map.get("return_code").equals("SUCCESS")&&map.get("result_code").equals("SUCCESS")){
	        	String outTradeNo = map.get("out_trade_no");
	        	//找到次卡
        		String timesCardIdString =outTradeNo.split("-")[1];
        		Integer timesCardId = Integer.parseInt(timesCardIdString);
        		TimesCardType timesCard = timesCardService.getTimesCardTypeDetail(timesCardId);
        		//找到用户
        		String customerIdString =outTradeNo.split("-")[2];
        		Integer custoemrId = Integer.parseInt(customerIdString);
        		Customer customer = customerService.selectByPrimaryKey(custoemrId);
        		//插入次卡
        		int row = timesCardService.insertTimesCardRelevance(customer, timesCard);
        		if(row>0){
        			logger.error("插入次卡成功");
        		}else{
        			logger.error("插入次卡失败");
        		}
	        }
		} catch (Exception e) {
			logger.error("不可能,给钱之后订单号没有5,e:"+e.getMessage());
		}
		return "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
    }
	//用户查询我的次卡列表
	@ApiOperation(value = "查询我的次卡列表", notes = "")
	@RequestMapping(method={RequestMethod.GET},value="/getMyTimesCardList")
    public JsonResult<List<TimesCardCustomerRelevance>> getMyTimesCardList(String token){
		if(token==null||token.isEmpty()){
			return new JsonResult<List<TimesCardCustomerRelevance>>(false,"入参token为空",null);
		}
		GogirlToken gogirlToken = gogirlTokenService.getTokenByToken(token);
		if(gogirlToken==null){
			return new JsonResult<List<TimesCardCustomerRelevance>>(false,"token过期",null);
		}
		if(gogirlToken.getCustomer()==null){
			return new JsonResult<List<TimesCardCustomerRelevance>>(false,"找不到用户customerId:"+gogirlToken.getCustomerId(),null);
		}
		TimesCardCustomerRelevance timesCardCustomerRelevance = new TimesCardCustomerRelevance();
		timesCardCustomerRelevance.setCustomerId(gogirlToken.getCustomerId());
		return new JsonResult<List<TimesCardCustomerRelevance>>(true, JsonResult.APP_DEFINE_SUC, timesCardService.getTimesCardList(timesCardCustomerRelevance));
    }
	//查询用户次卡详情
	@ApiOperation(value = "查询用户次卡详情", notes = "")
	@RequestMapping(method={RequestMethod.GET},value="/getTimesCardCustomerRelevanceDetail")
    public JsonResult<TimesCardCustomerRelevance> getTimesCardCustomerRelevanceDetail(Integer id){
		return new JsonResult<TimesCardCustomerRelevance>(true, JsonResult.APP_DEFINE_SUC, timesCardService.getTimesCardDetail(id));
    }
	//根据订单号查询我的可用的次卡
	@ApiOperation(value = "根据订单号查询我的可用的次卡", notes = "")
	@RequestMapping(method={RequestMethod.GET},value="/getMyTimesCardByOrderId")
    public JsonResult<List<TimesCardCustomerRelevance>> getMyTimesCardByOrderId(Integer orderId){
		if(orderId ==null){
			return new JsonResult<>(false,"orderId为空");
		}
		OrderManage orderManage = orderManageService.getOrderForDetail(orderId);
		if(orderManage==null){
			return new JsonResult<>(false,"找不到订单orderId:"+orderId);
		}
		TimesCardCustomerRelevance timesCardCustomerRelevance = new TimesCardCustomerRelevance();
		timesCardCustomerRelevance.setCustomerId(orderManage.getOrderUser());
		//查回我的所有优惠券
		List<TimesCardCustomerRelevance> list = timesCardService.getTimesCardList(timesCardCustomerRelevance);
		if(list==null){
			return new JsonResult<List<TimesCardCustomerRelevance>>(true, JsonResult.APP_DEFINE_SUC, new ArrayList<>());
		}
		Iterator<TimesCardCustomerRelevance> it = list.iterator();
		//循环过滤:已过期的设置为已过期
		while(it.hasNext()){
			TimesCardCustomerRelevance item = it.next();
			if(item.getStatus().equals(1)&&item.getValidEndTime().getTime()<new Date().getTime()){
				item.setStatus(3);
			}
			if(item.getStatus().equals(1)){//全部可用次卡设置为不可用,只有下面关联好的次卡才加入可用
				item.setStatus(4);
			}
		}
		//查询次卡可用的服务类型关联
		Map<Integer, Integer> map = new HashMap<>();//可用次卡id map
		List<TimesCardType> listCardServeType = timesCardService.listOrderCanUseCardByServeType(orderId);
		Iterator<TimesCardType> it2 = listCardServeType.iterator();
		while(it2.hasNext()){
			TimesCardType item = it2.next();
			map.put(item.getId(), item.getId());
		}
		//查询可用的服务关联
		List<TimesCardType> listCardServe = timesCardService.listOrderCanUseCardByServe(orderId);
		Iterator<TimesCardType> it3 = listCardServe.iterator();
		while(it3.hasNext()){
			TimesCardType item = it3.next();
			map.put(item.getId(), item.getId());
		}
		//循环过滤:可用的服务status设置为3可用
		it = list.iterator();
		while(it.hasNext()){
			TimesCardCustomerRelevance item = it.next();
			if(map.containsKey(item.getId())){//全部可用次卡设置为不可用,只有下面关联好的次卡才加入可用
				item.setStatus(3);
			}
		}
		return new JsonResult<List<TimesCardCustomerRelevance>>(true, JsonResult.APP_DEFINE_SUC, list);
    }
	//获取ip
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
}
