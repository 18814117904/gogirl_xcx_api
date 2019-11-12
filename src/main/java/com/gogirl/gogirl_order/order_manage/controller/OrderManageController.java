package com.gogirl.gogirl_order.order_manage.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gogirl.gogirl_order.dao.OrderServeDescribeMapper;
import com.gogirl.gogirl_order.dao.OrderServeDescribeRelevanceMapper;
import com.gogirl.gogirl_order.dao.OrderServeSkuMapper;
import com.gogirl.gogirl_order.dao.PurchaseSkuMapper;
import com.gogirl.gogirl_order.entity.OrderServeDescribe;
import com.gogirl.gogirl_order.entity.OrderServeDescribeRelevance;
import com.gogirl.gogirl_order.entity.OrderServeSku;
import com.gogirl.gogirl_order.entity.PurchaseSku;
import com.gogirl.gogirl_order.order_commons.config.Constans;
import com.gogirl.gogirl_order.order_commons.dto.OrderManage;
import com.gogirl.gogirl_order.order_commons.dto.OrderRecord;
import com.gogirl.gogirl_order.order_commons.dto.OrderServe;
import com.gogirl.gogirl_order.order_commons.dto.ScheduleManage;
import com.gogirl.gogirl_order.order_commons.utils.JsonResult;
import com.gogirl.gogirl_order.order_manage.dao.OrderManageMapper;
import com.gogirl.gogirl_order.order_manage.dao.OrderServeMapper;
import com.gogirl.gogirl_order.order_manage.service.OrderManageService;
import com.gogirl.gogirl_order.order_manage.service.OrderRecordService;
import com.gogirl.gogirl_order.order_manage.service.OrderServeService;
import com.gogirl.gogirl_order.order_schedule.service.ScheduleManageService;
import com.gogirl.gogirl_user.entity.CouponCustomerRelevance;
import com.gogirl.gogirl_user.entity.CouponOrderRelevance;
import com.gogirl.gogirl_user.entity.Customer;
import com.gogirl.gogirl_user.entity.CustomerBalanceRecord;
import com.gogirl.gogirl_user.service.CouponService;
import com.gogirl.gogirl_user.service.CustomerBalanceService;
import com.gogirl.gogirl_user.service.CustomerService;
import com.gogirl.gogirl_xcx.entity.GogirlToken;
import com.gogirl.gogirl_xcx.service.GogirlTokenService;
import com.gogirl.gogirl_xcx.util.CheckUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import springfox.documentation.annotations.ApiIgnore;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by yinyong on 2018/9/21.
 */
@Api(tags="7.订单")
@RestController
@RequestMapping("ordermanage")
public class OrderManageController {

    private final static Logger logger = LoggerFactory.getLogger(OrderManageController.class);

    @Autowired
    private OrderManageService orderManageService;
    @Autowired
    OrderServeService orderServeService;
    @Resource
    ScheduleManageService scheduleManageService;
	@Resource
	OrderRecordService orderRecordService;
    @Resource
    GogirlTokenService gogirlTokenService;
	@Autowired
	CouponService couponService;
	@Autowired
	CustomerBalanceService customerBalanceService;
	@Resource
	CustomerService customerService;
	@Resource
	OrderServeMapper orderServeMapper;
	@Resource
	OrderServeSkuMapper orderServeSkuMapper;
	@Resource
	PurchaseSkuMapper purchaseSkuMapper;
	@Resource
	OrderServeDescribeMapper orderServeDescribeMapper;
	@Resource
	OrderServeDescribeRelevanceMapper orderServeDescribeRelevanceMapper;
	/*查询所有可选的描述*/
    @ApiOperation(value="查询所有可选的描述",notes="")
    @RequestMapping(method={RequestMethod.GET},value = "listOrderServeDescribe")
    public JsonResult<List<OrderServeDescribe>> listOrderServeDescribe(Integer type){
		if(type==null){
			return new JsonResult(false,String.format(JsonResult.PARAM_NULL,"type"),null);
		}
		List<OrderServeDescribe> list = orderServeDescribeMapper.selectByType(type);
		return new JsonResult<>(true,JsonResult.APP_DEFINE_SUC,list);
	}
	
	/*根据skuName查询sku*/
    @ApiOperation(value="根据skuName查询sku",notes="")
    @RequestMapping(method={RequestMethod.POST},value = "selectBySkuName")
    public JsonResult<PurchaseSku> selectBySkuName(String skuName){
    	return new JsonResult<>(true,JsonResult.APP_DEFINE_SUC,purchaseSkuMapper.selectBySkuName(skuName));
    }

	/*查询款式/色号和款式描述by orderServeId*/
    @ApiOperation(value="查询款式/色号和款式描述by orderServeId",notes="")
    @RequestMapping(method={RequestMethod.GET},value = "getOrderServeDetail")
    public JsonResult<OrderServe> getOrderServeDetail(Integer orderServeId){
		if(orderServeId==null){
			return new JsonResult(false,String.format(JsonResult.PARAM_NULL,"orderServeId"),null);
		}
		OrderServe orderServe = orderServeService.getOrderServeDetail(orderServeId);
		return new JsonResult<>(true,JsonResult.APP_DEFINE_SUC,orderServe);
	}
    
	/*美甲师录入款式/色号和款式描述*/
    @ApiOperation(value="美甲师录入款式/色号和款式描述",notes="{\"id\":9005,\"produceId\":119,\"listOrderServeSku\":[{\"skuId\":357,\"type\":1},{\"skuId\":358,\"type\":2}],\"listOrderServeDescribe\":[{\"id\":1},{\"id\":2}]}")
    @RequestMapping(method={RequestMethod.POST},value = "recordOrderServeData")
    public JsonResult<Map<String, Object>> recordOrderServeData(@RequestBody OrderServe orderServe){
		if(orderServe==null||orderServe.getId()==null){
			return new JsonResult(false,String.format(JsonResult.PARAM_NULL,"orderServeId"),null);
		}
		//修改已选款式
		if(orderServe.getProduceId()!=null){
			orderServeService.updateOrderService(orderServe);
		}
		
		//插入录入的色号
		List<OrderServeSku> list3 = orderServe.getListOrderServeSku();
		if(list3!=null&&list3.size()>0){
			//删除已录入的色号
			orderServeSkuMapper.deleteByOrderServeId(orderServe.getId());
			for(OrderServeSku item3 : list3){
				if(item3.getPurchaseSku()==null){
					return new JsonResult<>(false,"别着急,正在确认款式是否存在,稍后再提交.");
				}
				item3.setType(item3.getPurchaseSku().getSkuType());
				item3.setSkuId(item3.getPurchaseSku().getId());
				item3.setCount(new BigDecimal("1"));
				item3.setOrderServeId(orderServe.getId());
			}
			orderServeSkuMapper.insertList(list3);
		}
		List<OrderServeDescribe> list4 = orderServe.getListOrderServeDescribe();
		if(list4!=null&&list4.size()>0){
			//删除已经录入的描述
			orderServeDescribeRelevanceMapper.deleteByOrderServeId(orderServe.getId());
			//插入已经录入的描述
			List<OrderServeDescribeRelevance> list5 = new ArrayList<>();
			for(OrderServeDescribe item:list4){
				OrderServeDescribeRelevance rele = new OrderServeDescribeRelevance();
				rele.setDescribeId(item.getId());
				rele.setOrderServeId(orderServe.getId());
				list5.add(rele);
			}
			orderServeDescribeRelevanceMapper.insertList(list5);
		}
		return new JsonResult<>(true,JsonResult.APP_DEFINE_SUC);
	}
	
	
	/*美甲师录入订单资料*/
    @ApiOperation(value="美甲师录入订单资料",notes="{\"id\":9005,\"userFeedback\":\"用户反馈\",\"picturePath\":\"客照图片路径\"}")
    @RequestMapping(method={RequestMethod.POST},value = "recordOrderData")
    public JsonResult<Map<String, Object>> recordOrderData(Integer orderId,String remark,String userFeedback,String picturePath){
    	//,Integer customerId,Integer orderId,String remark,String userFeedback,String picturePath
		if(orderId==null){
			return new JsonResult(false,String.format(JsonResult.PARAM_NULL,"orderId"),null);
		}
		OrderManage orderManage = orderManageService.getOrderForDetail(orderId);
		//订单信息
		if(remark!=null&&!remark.isEmpty()){
			OrderManage  orderManage1 = new OrderManage();
			orderManage1.setId(orderId);
			orderManage1.setRemark(remark);
			orderManageService.updateOrderStatus(orderManage1);
		}
		//订单录入信息
		List<OrderServe> list = orderManage.getListOrderServer();
		for(OrderServe itemOrderServe:list){
			OrderRecord queryOrderRecord = orderRecordService.getOrderRecord(itemOrderServe.getId());
			OrderRecord orderRecord = new OrderRecord();
			orderRecord.setOrderServeId(itemOrderServe.getId());
			orderRecord.setTechnicianFeedback(remark);
			orderRecord.setUserFeedback(userFeedback);
			orderRecord.setPicturePath(picturePath);
			//修改订单记录信息
			if(queryOrderRecord==null){
				orderRecordService.insertOrderRecord(orderRecord);
			}else{
				orderRecordService.updateOrderRecord(orderRecord);
			}
		}
		//计算资料录入百分比
		OrderManage om = orderManageService.getOrderForDetail(orderId);
		Customer c = customerService.selectByPrimaryKeyWithDetail(orderManage.getOrderUser());
		double dataRate = CheckUtil.countOrderDataRate(c,om);
		OrderManage updateDataint = new OrderManage();
		updateDataint.setId(orderId);
		updateDataint.setDataIntegrity(dataRate);
		orderManageService.updateOrderDataIntegrity(updateDataint);
		return new JsonResult<>(true,JsonResult.APP_DEFINE_SUC);
    }
	/*处理10月业绩数据*/
    @ApiOperation(value="处理10月业绩数据")
    @RequestMapping(method={RequestMethod.GET},value = "setAchievement")
    public JsonResult<Map<String, Object>> setAchievement(String orderIds){
    	logger.info("算业绩");
    	if(orderIds!=null){
    		String []arr = orderIds.split(",");
        		for(int i=0;i<arr.length;i++){
            		try {
	        			Integer orderId = Integer.parseInt(arr[i]);
	        			name(orderId);
        			} catch (Exception e) {
        				logger.info(arr[i]); 
        			}
        		}
    	}
		return new JsonResult<>(true,JsonResult.APP_DEFINE_SUC);
    }
	/*处理10月业绩数据*/
    public void name(Integer orderId) {
    	OrderManage orderDeatail = orderManageService.getOrderForDetail(orderId);
    	//销优惠券+计算优惠券计算业绩的金额
    	BigDecimal couponPayPrice = new BigDecimal("0");
    	List<CouponOrderRelevance> listCouponOrderRelevance = orderDeatail.getListCouponOrderRelevance();
    	if(listCouponOrderRelevance!=null){
    		Iterator<CouponOrderRelevance> iterator = listCouponOrderRelevance.iterator();
    		while(iterator.hasNext()){
    			CouponOrderRelevance item = iterator.next();
    			if(item.getCoupon()!=null&&item.getCoupon().getPayAmount()!=null){
    				couponPayPrice = couponPayPrice.add(item.getCoupon().getPayAmount());
    			}
    		}
    	}
    	
    	int countNailServe = 0;//记录有几个美甲服务
    	//业绩:服务单价/服务总价*(合计金额+外部券金额)
    	List<OrderServe> list = orderDeatail.getListOrderServer();
    	if(list!=null&&list.size()!=0){
    		BigDecimal orderTotalPrice = orderDeatail.getTotalPrice();
    		if(orderTotalPrice!=null){//优惠券实付金额+订单金额
    			couponPayPrice=couponPayPrice.add(orderTotalPrice);
    		}
    		BigDecimal orderChangePrice = orderDeatail.getChangePrice();
    		if(orderChangePrice!=null){//+总改价
    			couponPayPrice=couponPayPrice.add(orderChangePrice);
    		}
    		BigDecimal orderDiscountPrice = orderDeatail.getDiscountPrice();
    		if(orderDiscountPrice!=null){//-总优惠
    			couponPayPrice=couponPayPrice.subtract(orderDiscountPrice);
    		}
    		
    		for(int i= 0;i<list.size();i++){
    			OrderServe orderServe = list.get(i);
    			BigDecimal servePrice = orderServe.getServe().getPrice();
    			BigDecimal serveChangePrice = orderServe.getServeChangePrice();
    			if(serveChangePrice!=null){
    				servePrice = servePrice.add(serveChangePrice);
    			}
    			BigDecimal achievement = null;
    			if(orderTotalPrice!=null&&orderTotalPrice.compareTo(new BigDecimal("0"))==0){
    				achievement = new BigDecimal("0");
    			}else{
    				achievement = couponPayPrice.multiply(servePrice).divide(orderTotalPrice,2, BigDecimal.ROUND_HALF_UP);//按照比例分业绩
    			}
    			OrderServe updateOrderServe = new OrderServe();
    			updateOrderServe.setId(orderServe.getId());
    			updateOrderServe.setAchievement(achievement.compareTo(new BigDecimal("0"))<0?new BigDecimal("0"):achievement);
    			orderServeMapper.updateOrderService(updateOrderServe);
    			if(orderServe.getServe().getType().equals("美甲")){//计算有几个卸甲服务，然后送卸甲券
    				countNailServe++;
    			}
    		}
    	}
	}
    /*查询会员消费记录*/
    @ApiOperation(value="查询会员消费记录")
    @RequestMapping(method={RequestMethod.GET},value = "queryCustomerOrder")
    public JsonResult<Map<String, Object>> queryCustomerOrder(String token,Integer customerId,Integer pageNum,Integer pageSize){
        JsonResult<Map<String, Object>> jsonResult = new JsonResult<Map<String, Object>>();
        OrderManage orderManage = new OrderManage();
        GogirlToken gogirlToken = gogirlTokenService.getTokenByToken_t(token);
        if(gogirlToken==null){
        	return new JsonResult<Map<String, Object>>(false,JsonResult.TOKEN_NULL_CODE,null);
        }
        if(gogirlToken.getUserTechnician()==null){
        	return new JsonResult<Map<String, Object>>(false,JsonResult.NO_DEPARTMENT_CODE,null);
        }
        orderManage.setDepartmentId(gogirlToken.getUserTechnician().getDepartmentId());
        orderManage.setOrderUser(customerId);
        if(pageNum!=null||pageSize!=null){
        	PageHelper.startPage(pageNum,pageSize);
        }
        List<OrderManage> list = orderManageService.getListOrderManageForPage(orderManage);
        Iterator<OrderManage> it = list.iterator();
        while(it.hasNext()){
        	OrderManage item = it.next();
        	List<OrderServe> listOrderServer = item.getListOrderServer();
        	if(listOrderServer==null)continue;
        	Iterator<OrderServe> itOrderServer=listOrderServer.iterator();
        	List<String> images = new ArrayList<String>();
        	while(itOrderServer.hasNext()){
        		OrderServe itemordersServe = itOrderServer.next();
        		OrderRecord orderRecord = itemordersServe.getOrderRecord();
        		if(orderRecord==null||orderRecord.getPicturePath()==null||orderRecord.getPicturePath().isEmpty())continue;
        		String [] pictureString = orderRecord.getPicturePath().split(",");
        		for(int i=0;i<pictureString.length;i++){
        			if(pictureString[i]!=null&&!pictureString[i].isEmpty()){
        				images.add(pictureString[i]);
        			}
        		}
        	}
        	item.setImages(images);
        }
        PageInfo<OrderManage> pageInfo = new PageInfo<OrderManage>(list);
        Map<String, Object> map = new HashMap<>();
        map.put("pageInfo", pageInfo);
        map.put("orderTimes", pageInfo.getSize());
        map.put("sumPay", orderManageService.countSumPay(customerId,gogirlToken.getUserTechnician().getDepartmentId()));
        jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC).setData(map);
        return jsonResult;
    }
    /*修改订单*/
    @ApiOperation(value="修改订单", notes = "{\"token\":\"3cf60e2abc\",\"id\": 3849,\"remark\":\"备注\",\"listOrderServer\": [{ \"id\": 5558, \"produceName\": \"芭比单色2\", \"serveChangePrice\": 0, \"serveChangePriceText\": \"\",\"serveId\": 101, \"servePrice\": 168, \"servePriceText\": 168, \"serviceName\": \"芭比单色\", \"technicianId\": 92, \"typeName\": \"美甲\"}]}")
    @RequestMapping(method={RequestMethod.POST},value = "modifyOrder")
    public JsonResult modifyOrder(@RequestBody OrderManage orderManage){
        JsonResult jsonResult = new JsonResult();
        if(orderManage==null||orderManage.getToken()==null){
        	return new JsonResult<>(false,"token为空",null);
        }
        GogirlToken gogirlToken = gogirlTokenService.getTokenByToken_t(orderManage.getToken());
        if(gogirlToken==null){
        	return new JsonResult<>(false,JsonResult.TOKEN_NULL_CODE,null);
        }
        orderManage.setOpenOrderUser(gogirlToken.getUserTechnician().getUserId());
        int result = orderManageService.modifyOrder(orderManage);
        if(result > 0){
            jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC);
        }
        return jsonResult;
    }
    /*美甲师收款*/
    @ApiOperation(value="美甲师收款")
    @RequestMapping(method={RequestMethod.POST},value = "startPay")
    public JsonResult startPay(String token,Integer orderId){
    	if(orderId==null){
    		return new JsonResult<>(false,"orderId为空");
    	}
        OrderManage orderDeatail = orderManageService.getOrderForDetail(orderId);
        if(orderDeatail==null){
        	return new JsonResult<>(false,"找不到订单orderId:"+orderId);
        }
        if(orderDeatail.getStatus().equals(1)){//不做操作,正常往下走
        }else if(orderDeatail.getStatus().equals(2)){
        	return new JsonResult<>(false,"该订单已经是待支付状态,无需重复支付");
        }else if(orderDeatail.getStatus().equals(3)){
        	return new JsonResult<>(false,"该订单已完成支付，请刷新列表查看状态");
        }else{
        	return new JsonResult<>(false,"请刷新列表后再操作,订单状态异常为"+orderDeatail.getStatus());
        }
        JsonResult jsonResult = new JsonResult();
        OrderManage orderManage = new OrderManage();
        orderManage.setId(orderId);
        orderManage.setStatus(Constans.ORDER_STATUS_PAMENT);
        orderManage.setReceipt("true");
        int result = orderManageService.startPay(orderManage);
		//设置预约为已完成,该时间可再被预约
		if(orderDeatail.getScheduledId()!=null){
			ScheduleManage sm = new ScheduleManage();
			sm.setId(orderDeatail.getScheduledId());
			sm.setStatus(Constans.SCHEDULE_STATUS_ABIDING);
			scheduleManageService.updateSchedule(sm);
		}
        
        logger.info("店铺端收款操作..."+ orderManage);
        if(result > 0){
            jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC);
        }
        return jsonResult;
    }
    /*用户取消订单，自动取消订单关联的预约*/
    @ApiOperation(value="用户取消订单，自动取消订单关联的预约")
    @RequestMapping(method={RequestMethod.POST},value = "cancelOrder")
    public JsonResult cancelOrder(String token, Integer orderId){
    	if(token==null){
    		return new JsonResult(false).setMessage("token为空");
    	}
    	if(orderId==null){
    		return new JsonResult(false).setMessage("orderId为空");
    	}
    	OrderManage order = orderManageService.getOrderForDetail(orderId);
    	if(order==null){
    		return new JsonResult(false).setMessage("找不到订单orderId:"+orderId);
    	}
    	GogirlToken tk = gogirlTokenService.getTokenByToken(token);
    	if(order.getOrderUser()==null||tk.getCustomerId()==null||!order.getOrderUser().equals(tk.getCustomerId())){
    		return new JsonResult(false).setMessage("抱歉，不能删除他人的订单");
    	}
    	OrderManage orderManage = new OrderManage();
    	orderManage.setId(orderId);
    	orderManage.setStatus(7);
    	orderManageService.updateOrderStatus(orderManage);
    	//找到订单中的预约id
    	if(order.getScheduledId()!=null){
        	//取消预约
        	ScheduleManage scheduleManage = new ScheduleManage();
        	scheduleManage.setId(order.getScheduledId());
        	scheduleManage.setStatus(4);
            logger.info("取消预约：" + scheduleManage);
            int result = scheduleManageService.updateSchedule(scheduleManage);
    	}
        return new JsonResult(true).setMessage(JsonResult.APP_DEFINE_SUC);
    }
    /*查询我的客照,没有客照的记录不返回*/
    @ApiOperation(value="查询我的客照,没有客照的记录不返回")
    @RequestMapping(method={RequestMethod.GET},value = "queryMyOrderPhoto")
    public JsonResult queryMyOrderPhoto(String token,Integer pageNum,Integer pageSize){
        JsonResult jsonResult = new JsonResult();
    	if(token==null){
    		return new JsonResult(false).setMessage("token为空");
    	}
    	GogirlToken tk = gogirlTokenService.getTokenByToken(token);
    	if(tk==null){
    		return new JsonResult(false).setMessage("token无效");
    	}
    	OrderManage orderManage = new OrderManage();
    	orderManage.setOrderUser(tk.getCustomerId());
        if(pageNum!=null&&pageSize!=null){
        	PageHelper.startPage(pageNum,pageSize);
        }
        List<OrderManage> lists = orderManageService.getListOrderPhotoForPage(orderManage);
        Iterator<OrderManage> it = lists.iterator();
        while(it.hasNext()){
        	OrderManage item = it.next();
        	List<OrderServe> listOrderServer = item.getListOrderServer();
        	Iterator<OrderServe> itOrderServer=listOrderServer.iterator();
        	List<String> images = new ArrayList<String>();
        	while(itOrderServer.hasNext()){
        		OrderServe itemordersServe = itOrderServer.next();
        		OrderRecord orderRecord = itemordersServe.getOrderRecord();
        		String [] pictureString = orderRecord.getPicturePath().split(",");
        		for(int i=0;i<pictureString.length;i++){
        			if(pictureString[i]!=null&&!pictureString[i].isEmpty()){
        				images.add(pictureString[i]);
        			}
        		}
        	}
        	item.setImages(images);
        }
        PageInfo<OrderManage> pageInfo = new PageInfo<OrderManage>(lists);
        jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC).setData(pageInfo);
        return jsonResult;
    }
    /*查询我的订单*/
    @ApiOperation(value="查询我的订单")
    @RequestMapping(method={RequestMethod.GET},value = "queryMyOrderForPage")
    public JsonResult queryMyOrderForPage(String token,Integer pageNum,Integer pageSize){
        JsonResult jsonResult = new JsonResult();
    	if(token==null){
    		return new JsonResult(false).setMessage("token为空");
    	}
    	GogirlToken tk = gogirlTokenService.getTokenByToken(token);
    	if(tk==null){
    		return new JsonResult(false).setMessage("token无效");
    	}
    	OrderManage orderManage = new OrderManage();
    	orderManage.setOrderUser(tk.getCustomerId());
        if(pageNum!=null&&pageSize!=null){
        	PageHelper.startPage(pageNum,pageSize);
        }
        List<OrderManage> lists = orderManageService.getListOrderManageForPage(orderManage);
        PageInfo<OrderManage> pageInfo = new PageInfo<OrderManage>(lists);
        jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC).setData(pageInfo);
        return jsonResult;
    }
    /*根据订单id查询订单详情，原接口*/
    @ApiOperation(value="根据订单id查询订单详情，原接口")
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "queryOrderForDetail")
    public JsonResult queryOrderForDetail(Integer id, HttpServletRequest request, HttpServletResponse response){
        JsonResult jsonResult = new JsonResult();
        OrderManage orderManage = orderManageService.getOrderForDetail(id);
        if(orderManage!=null){
        	List<CustomerBalanceRecord> list = customerBalanceService.listPartPay(id);
        	orderManage.setListCustomerBalanceRecord(list);
        }
        jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC).setData(orderManage);
        return jsonResult;
    }
    /*美甲师接单*/
    @ApiOperation(value="美甲师接单")
    @RequestMapping(method={RequestMethod.GET},value = "takeOrders")
    public JsonResult takeOrders(String token,Integer orderId){
        JsonResult jsonResult = new JsonResult();
    	if(token==null){
    		return new JsonResult(false).setMessage("token为空");
    	}
    	if(orderId==null){
    		return new JsonResult(false).setMessage("orderId为空");
    	}
    	GogirlToken tk = gogirlTokenService.getTokenByToken_t(token);
    	if(tk==null){
    		return new JsonResult(false).setMessage("token无效");
    	}
        OrderManage orderManage = orderManageService.getOrderForDetail(orderId);
    	if(orderManage==null){
    		return new JsonResult(false).setMessage("找不到订单orderId："+orderId);
    	}
    	if(orderManage.getStatus().equals(6)||orderManage.getStatus().equals(7)||orderManage.getStatus().equals(9)||orderManage.getStatus().equals(10)){
    		//正常逻辑
    	}else if(orderManage.getStatus().equals(1)||orderManage.getStatus().equals(2)||orderManage.getStatus().equals(3)||orderManage.getStatus().equals(4)||orderManage.getStatus().equals(8)){
    		return new JsonResult<>(false,"该订单已被其他美甲师接单,请勿重复接单,刷新列表可查看.");
    	}else if(orderManage.getStatus().equals(5)){
    		return new JsonResult<>(false,"订单已删除,刷新列表可查看");
    	}else{
    		return new JsonResult<>(false,"订单状态异常,无法接单,刷新列表可查看");
    	}
    	List<OrderServe> list = orderManage.getListOrderServer();
    	for(int i=0;i<list.size();i++){
    	}
    	//改订单状态为已接单，设置开单人
    	OrderManage updateorderManage = new OrderManage();
    	updateorderManage.setId(orderId);
    	if(tk!=null&&tk.getUserTechnician()!=null&&tk.getUserTechnician().getUserId()!=null){
    		updateorderManage.setOpenOrderUser(tk.getUserTechnician().getUserId());
    	}
    	updateorderManage.setStatus(Constans.ORDER_STATUS_SERVE);
    	orderManageService.updateOrderManage(updateorderManage);
    	
    	//改服务技师为开单的技师，开始计时
		OrderServe orderServe = new OrderServe();
		orderServe.setOrderId(orderId);
		orderServe.setExecutionStatus(Constans.ORDER_EXECUTION_TIMESLOT);//直接给它结束计时
		orderServe.setTechnicianId(tk.getUserTechnician().getTechnicianId().toString());
		orderServe.setStartTime(new Date());
		orderServe.setEndTime(new Date());
		orderServeService.updateOrderServiceByOrderId(orderServe);

		//改预约状态为已接单
    	if(orderManage.getScheduledId()!=null){
        	//取消预约
        	ScheduleManage scheduleManage = new ScheduleManage();
        	scheduleManage.setId(orderManage.getScheduledId());
        	scheduleManage.setStatus(Constans.SCHEDULE_STATUS_SERVING);
            logger.info("接单scheduleManage：" + scheduleManage);
            int result = scheduleManageService.updateSchedule(scheduleManage);
    	}
		
        return jsonResult.setSuccess(true).setMessage(JsonResult.APP_DEFINE_SUC);
    }
    /*查询店铺订单，如果是预约过来的订单，带上预约信息，顺带查询是否可以替单*/
    @ApiOperation(value="查询店铺订单，如果是预约过来的订单，带上预约信息，顺带查询是否可以替单")
    @RequestMapping(method={RequestMethod.GET},value = "queryListOrder")
    public JsonResult<PageInfo<OrderManage>> queryListOrder(Integer orderId,String token,Integer customerId,String customerName,String customerPhone,Integer status,Integer pageNum,Integer pageSize){
        JsonResult<PageInfo<OrderManage>> jsonResult = new JsonResult<PageInfo<OrderManage>>();
        OrderManage orderManage = new OrderManage();
        orderManage.setStatus(status);
        GogirlToken gogirlToken = gogirlTokenService.getTokenByToken_t(token);
        if(gogirlToken==null){
        	return new JsonResult<PageInfo<OrderManage>>(false,JsonResult.TOKEN_NULL_CODE,null);
        }
        if(gogirlToken.getUserTechnician()==null){
        	return new JsonResult<PageInfo<OrderManage>>(false,JsonResult.NO_DEPARTMENT_CODE,null);
        }
        orderManage.setId(orderId);
        orderManage.setDepartmentId(gogirlToken.getUserTechnician().getDepartmentId());
        orderManage.setOrderUser(customerId);
        orderManage.setStoreScheduleUsername(customerName);
        orderManage.setTelephone(customerPhone);
        if(pageNum==null||pageSize==null){
        	return new JsonResult<PageInfo<OrderManage>>(false,"页码为空",null);
        }
        PageHelper.startPage(pageNum,pageSize);
        Date date  = new Date();
        List<OrderManage> list = orderManageService.getListOrderManageForPage(orderManage);
        logger.info("查询订单列表耗时："+String.valueOf((new Date().getTime()-date.getTime())/1000.0)+"s");
        //查询服务中的美甲师
        List<Map<String, Object>> techIdCountlist = orderManageService.countTechnicianServing();
        Map<Integer, Integer> techIdCountmap = new HashMap<Integer, Integer>();
        for(int i=0;i<techIdCountlist.size();i++){
        	if(techIdCountlist.get(i).get("technician_id")!=null){
//        		logger.info("techIdCountlist.get(i):"+techIdCountlist.get(i));
        		String[] techId = techIdCountlist.get(i).get("technician_id").toString().split(",");
        		for(int j=0;j<techId.length;j++){
        			if(techId[j]!=null&&!techId[j].isEmpty()){
        				techIdCountmap.put(Integer.valueOf(techId[j]),Integer.valueOf(String.valueOf(techIdCountlist.get(i).get("serving_count"))));
        			}
        		}
        	}
        }
        //便利list，设置每一单的是否可被替单
        for(int i=0;i<list.size();i++){
        	List<OrderServe> item = list.get(i).getListOrderServer();
        	for(int j=0;j<item.size();j++){
        		if(item==null||item.get(j)==null||item.get(j).getTechnicianId()==null){
        			continue; 
        		}
        		Integer count = techIdCountmap.get(item.get(j).getTechnicianId());
        		if(count!=null&&count>0){
            		item.get(j).setCanReplace(true);
        		}
        	}
        }
        
        PageInfo<OrderManage> pageInfo = new PageInfo<OrderManage>(list);
        jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC).setData(pageInfo);
        return jsonResult;
    }
    /*替单*/
    @ApiOperation(value="替单")
    @RequestMapping(method={RequestMethod.GET},value = "takeOtherTechOrders")
    public JsonResult takeOtherTechOrders(String token,Integer orderId){
        JsonResult jsonResult = new JsonResult();
    	if(token==null){
    		return new JsonResult(false).setMessage("token为空");
    	}
    	if(orderId==null){
    		return new JsonResult(false).setMessage("orderId为空");
    	}
    	GogirlToken tk = gogirlTokenService.getTokenByToken_t(token);
    	if(tk==null){
    		return new JsonResult(false).setMessage("token无效");
    	}
        OrderManage orderManage = orderManageService.getOrderForDetail(orderId);
    	if(orderManage==null){
    		return new JsonResult(false).setMessage("找不到订单orderId："+orderId);
    	}
    	if(orderManage.getStatus().equals(6)||orderManage.getStatus().equals(7)||orderManage.getStatus().equals(9)||orderManage.getStatus().equals(10)){
    		//正常逻辑
    	}else if(orderManage.getStatus().equals(1)||orderManage.getStatus().equals(2)||orderManage.getStatus().equals(3)||orderManage.getStatus().equals(4)||orderManage.getStatus().equals(8)){
    		return new JsonResult<>(false,"该订单已被其他美甲师接单,请勿重复接单,刷新列表可查看.");
    	}else if(orderManage.getStatus().equals(5)){
    		return new JsonResult<>(false,"订单已删除,刷新列表可查看");
    	}else{
    		return new JsonResult<>(false,"订单状态异常,无法接单,刷新列表可查看");
    	}
    	List<OrderServe> list = orderManage.getListOrderServer();
    	for(int i=0;i<list.size();i++){
    	}
    	//改订单状态为已接单，设置开单人
    	OrderManage updateorderManage = new OrderManage();
    	updateorderManage.setId(orderId);
    	if(tk!=null&&tk.getUserTechnician()!=null&&tk.getUserTechnician().getUserId()!=null){
    		updateorderManage.setOpenOrderUser(tk.getUserTechnician().getUserId());
    	}
    	updateorderManage.setStatus(Constans.ORDER_STATUS_SERVE);
    	orderManageService.updateOrderManage(updateorderManage);
    	
    	//改服务技师为开单的技师，开始计时
		OrderServe orderServe = new OrderServe();
		orderServe.setOrderId(orderId);
		orderServe.setExecutionStatus(Constans.ORDER_EXECUTION_TIMESLOT);//直接给它结束计时
		orderServe.setTechnicianId(tk.getUserTechnician().getTechnicianId().toString());
		orderServe.setStartTime(new Date());
		orderServe.setEndTime(new Date());
		orderServeService.updateOrderServiceByOrderId(orderServe);

		//改预约状态为已接单
    	if(orderManage.getScheduledId()!=null){
        	//取消预约
        	ScheduleManage scheduleManage = new ScheduleManage();
        	scheduleManage.setId(orderManage.getScheduledId());
        	scheduleManage.setStatus(Constans.SCHEDULE_STATUS_SERVING);
            logger.info("接单scheduleManage：" + scheduleManage);
            int result = scheduleManageService.updateSchedule(scheduleManage);
    	}
        return jsonResult.setSuccess(true).setMessage(JsonResult.APP_DEFINE_SUC);
    }
    
    @ApiIgnore
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "addOrderManage")
    public JsonResult addOrderManage(Integer schId, Integer openOrderUser, HttpServletRequest request, HttpServletResponse response){
        JsonResult result = orderManageService.insertOrderManage(schId, openOrderUser);
        if(result.getSuccess()){
            result.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC);
        }
        return result;
    }

    @ApiIgnore
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "queryOrderManageForPage")
    public JsonResult queryOrderManageForPage(OrderManage orderManage, HttpServletRequest request, HttpServletResponse response){
        JsonResult jsonResult = new JsonResult();
        String pageNum = request.getParameter("pageNum");
        String pageSize = request.getParameter("pageSize");
        PageHelper.startPage(Integer.parseInt(pageNum),Integer.parseInt(pageSize));
        List<OrderManage> lists = orderManageService.listOrderManageForPage(orderManage);
        PageInfo<OrderManage> pageInfo = new PageInfo<OrderManage>(lists);
        jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC).setData(pageInfo);
        return jsonResult;
    }

    @ApiIgnore
    @RequestMapping(method={RequestMethod.GET},value = "queryOrderSummary")
    public JsonResult queryOrderSummary(OrderManage orderManage, HttpServletRequest request, HttpServletResponse response){
        JsonResult jsonResult = new JsonResult();
        Map<String, String> map = orderManageService.getOrderSummary(orderManage);
        if(map.get("totelPrice") == null){
            map.put("totelPrice", "0");
        }
        jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC).setData(map);
        return jsonResult;
    }

    @ApiIgnore
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "queryReceivable")
    public JsonResult queryReceivable(OrderManage orderManage, HttpServletRequest request, HttpServletResponse response) {
        JsonResult jsonResult = new JsonResult();
        int result = orderManageService.getReceivable(orderManage);
        jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC).setData(result);
        return jsonResult;
    }

    @ApiIgnore
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "queryOrderData")
    public JsonResult queryOrderData(Integer departmentId, HttpServletRequest request, HttpServletResponse response){
        JsonResult jsonResult = new JsonResult();
        int result = orderManageService.getOrderData(departmentId);
        jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC).setData(result);
        return jsonResult;
    }

    @ApiIgnore
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "queryOrderReminder")
    public JsonResult queryOrderReminder(Integer departmentId, HttpServletRequest request, HttpServletResponse response) {
        JsonResult jsonResult = new JsonResult();
        int result = orderManageService.getOrderReminder(departmentId);
        jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC).setData(result);
        return jsonResult;
    }

    @ApiIgnore
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "modifyOrderStatusFinash")
    public JsonResult modifyOrderStatusFinash(OrderManage orderManage,  HttpServletRequest request, HttpServletResponse response){
        JsonResult jsonResult = orderManageService.updateOrderStatusFinash(orderManage);
        logger.info("支付完成..."+orderManage);
        /*if(result > 0){
            jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC);
        }*/
        return jsonResult;
    }

    @ApiIgnore
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "modifyOrderPayMentType")
    public JsonResult modifyOrderPayMentType(OrderManage orderManage, HttpServletRequest request, HttpServletResponse response) {
        JsonResult jsonResult = new JsonResult();
        int result = orderManageService.updateOrderPayMentType(orderManage);
        logger.info("修改支付方式..."+orderManage);
        if(result > 0){
            jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC);
        }
        return jsonResult;
    }

    @ApiIgnore
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "modifyOrderManage")
    public JsonResult modifyOrderManage(OrderManage orderManage, HttpServletRequest request, HttpServletResponse response){
        JsonResult jsonResult = new JsonResult();
        orderManage.setReceipt("true");
        int result = orderManageService.updateOrderManage(orderManage);
        logger.info("店铺端收款操作..."+ orderManage);
        if(result > 0){
            jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC);
        }
        return jsonResult;
    }

    @ApiIgnore
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "modifyOrderServe")
    public JsonResult modifyOrderServe(@RequestBody OrderManage orderManage, HttpServletRequest request, HttpServletResponse response){
        JsonResult jsonResult = new JsonResult();
        int result = orderManageService.modifyOrderServe(orderManage);
        if(result > 0){
            jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC);
        }
        return jsonResult;
    }

    @ApiIgnore
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "modifyOrderStatusCancel")
    public JsonResult modifyOrderStatusCancel(OrderManage orderManage, HttpServletRequest request, HttpServletResponse response) {
        JsonResult jsonResult = new JsonResult();
        orderManage.setStatus(Constans.ORDER_STATUS_SERVE);
        orderManageService.updateOrderStatusCancel(orderManage);
        jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC);
        return jsonResult;
    }

    @ApiIgnore
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "deleteOrderServeById")
    public JsonResult deleteOrderServeById (Integer id, HttpServletRequest request, HttpServletResponse response) {
        JsonResult jsonResult = new JsonResult();
        int result = orderManageService.deleteOrderServeById(id);
        if(result > 0){
            jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC);
        }
        return jsonResult;
    }

    @ApiIgnore
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "deleteOrderManageById")
    public JsonResult deleteOrderManageById(Integer id,String delRemark, HttpServletRequest request, HttpServletResponse response){
        JsonResult jsonResult = new JsonResult();
        OrderManage om = new OrderManage();
        om.setId(id);
        om.setDelRemark(delRemark);
        int result = orderManageService.deleteOrderManageById(om);
        if(result > 0){
            jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC);
        }
        return jsonResult;
    }

    @ApiIgnore
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "addOrderManageByUser")
    public JsonResult addOrderManageByUser(@RequestBody OrderManage orderManage, HttpServletRequest request, HttpServletResponse response){
        return orderManageService.insertOrderManageByUser(orderManage);
    }

    @ApiIgnore
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "addRechargeOrder")
    public JsonResult addRechargeOrder(OrderManage orderManage, HttpServletRequest request, HttpServletResponse response){
        JsonResult jsonResult = orderManageService.insertRechargeOrder(orderManage);
        return jsonResult;
    }
    //订单日期修改到昨天
    @ApiIgnore
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "setOrderDateBackOneDay")
    public JsonResult setOrderDateBackOneDay(Integer id){
    	OrderManage orderManage = orderManageService.getOrderForDetail(id);
    	if(orderManage==null){
    		JsonResult jsonResult =  new JsonResult(false).setMessage("找不到订单");
    	}
    	Long onedate = new Long("86400000");
    	if(orderManage.getCreateTime().getTime()<new Date().getTime()-onedate){
    		JsonResult jsonResult =  new JsonResult(false).setMessage("抱歉，只能修改24小时内新建的订单。");
    	}
    	if(orderManage.getCreateTime()!=null){
    		orderManage.setCreateTime(new Date(orderManage.getCreateTime().getTime()-onedate));
    	}
    	if(orderManage.getUpdateTime()!=null){
    		orderManage.setUpdateTime(new Date(orderManage.getUpdateTime().getTime()));
    	}
    	if(orderManage.getLaunchTime()!=null){
    		orderManage.setLaunchTime(new Date(orderManage.getLaunchTime().getTime()-onedate));
    	}
    	if(orderManage.getFinishTime()!=null){
    		orderManage.setFinishTime(new Date(orderManage.getFinishTime().getTime()-onedate));
    	}
    	orderManageService.updateOrderManage(orderManage);
    	JsonResult jsonResult = new JsonResult(true,JsonResult.APP_DEFINE_SUC,orderManage);
//    	orderServeService.updateOrderService(orderServe);
        return jsonResult;
    }
    
}
