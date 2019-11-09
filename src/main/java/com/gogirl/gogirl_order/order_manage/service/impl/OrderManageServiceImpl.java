package com.gogirl.gogirl_order.order_manage.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.gogirl.gogirl_order.dao.OrderCommentMapper;
import com.gogirl.gogirl_order.order_commons.config.Constans;
import com.gogirl.gogirl_order.order_commons.dto.*;
import com.gogirl.gogirl_order.order_commons.utils.JsonResult;
import com.gogirl.gogirl_order.order_commons.utils.RandomUtils;
import com.gogirl.gogirl_order.order_manage.dao.OrderManageMapper;
import com.gogirl.gogirl_order.order_manage.dao.OrderRecordMapper;
import com.gogirl.gogirl_order.order_manage.dao.OrderServeMapper;
import com.gogirl.gogirl_order.order_manage.service.OrderManageService;
import com.gogirl.gogirl_order.order_schedule.dao.ScheduleManageMapper;
import com.gogirl.gogirl_order.order_schedule.dao.ScheduleServeMapper;
import com.gogirl.gogirl_service.dao.serve.ServeMapper;
import com.gogirl.gogirl_service.entity.Serve;
import com.gogirl.gogirl_store.store_commons.dto.ShopManage;
import com.gogirl.gogirl_store.store_shop.service.ShopManageService;
import com.gogirl.gogirl_technician.technician_commons.dto.TechnicianManage;
import com.gogirl.gogirl_technician.technician_user.service.TechnicianManageService;
import com.gogirl.gogirl_technician.technician_user.service.UserTechnicianService;
import com.gogirl.gogirl_user.dao.StoreManageMapper2;
import com.gogirl.gogirl_user.entity.Coupon;
import com.gogirl.gogirl_user.entity.CouponCustomerRelevance;
import com.gogirl.gogirl_user.entity.CouponOrderRelevance;
import com.gogirl.gogirl_user.entity.CustomerBalanceRecord;
import com.gogirl.gogirl_user.entity.TimesCardCustomerRelevance;
import com.gogirl.gogirl_user.entity.TimesCardUsedRecord;
import com.gogirl.gogirl_user.service.CouponService;
import com.gogirl.gogirl_user.service.CustomerBalanceService;
import com.gogirl.gogirl_user.service.TimesCardService;
import com.gogirl.gogirl_user.util.OrderNoLock;
import com.gogirl.gogirl_xcx.entity.GogirlToken;
import com.gogirl.gogirl_xcx.service.SendMpTemplateMsgService;
import com.gogirl.gogirl_xcx.service.SendXcxTemplateMsgService;

import org.apache.commons.collections.iterators.EntrySetMapIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

import io.swagger.annotations.ApiOperation;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

import javax.annotation.Resource;

/**
 * Created by yinyong on 2018/9/21.
 */
@Service
public class OrderManageServiceImpl implements OrderManageService {

    private final static Logger logger = LoggerFactory.getLogger(OrderManageServiceImpl.class);

    @Resource
    private OrderManageMapper orderManageMapper;

    @Resource
    private OrderServeMapper orderServeMapper;

    @Resource
    private OrderCommentMapper orderCommentMapper;

    @Resource
    private ScheduleServeMapper scheduleServeMapper;

    @Resource
    private ScheduleManageMapper scheduleManageMapper;

    @Resource
    private OrderRecordMapper orderRecordMapper;
    
	@Resource
	CustomerBalanceService customerBalanceService;
	@Resource
	SendXcxTemplateMsgService sendXcxTemplateMsgService;
	
	@Resource
	TechnicianManageService technicianManageService;
	@Resource
	UserTechnicianService userTechnicianService;
	
	@Resource
	ShopManageService shopManageService;
	@Resource
	SendMpTemplateMsgService sendMpTemplateMsgService;
	
    @Value("${customer-url}")
    private String customerUrl;

    @Value("${to_pay}")
    private String toPay;
    @Value("${ma_to_pay}")
    private String maToPay;
    
    @Value("${evaluationMsg}")
    private String evaluationMsg;

    @Autowired
    private RestTemplate restTemplate;
    
    @Resource
    CouponService couponService;
    
    @Resource
    ServeMapper serveMapper;
    @Resource
    TimesCardService timesCardService;
    
    OrderNoLock orderNoLock=OrderNoLock.getInsatance();
    
    @Override
	public List<Map<String, Object>> countTechnicianServing(){
    	return orderManageMapper.countTechnicianServing();
    }
    @Override
    public int updateOrderStatus(OrderManage orderManage) {
        return orderManageMapper.updateOrderManage(orderManage);
    }
    @Override
    public int updateOrderStatusCancel(OrderManage orderManage) {
        return orderManageMapper.updateOrderManage(orderManage);
    }
    @Override
    public List<OrderManage> getListOrderManageForPage(OrderManage orderManage) {
        return orderManageMapper.getListOrderManageForPage(orderManage);
    }
	@Override
	public List<OrderManage> getListOrderPhotoForPage(OrderManage orderManage) {
		return orderManageMapper.getListOrderPhotoForPage(orderManage);
	}
	@Override
    public List<OrderManage> listOrderManageForPage(OrderManage orderManage) {
        return orderManageMapper.listOrderManageForPage(orderManage);
    }

    @Override
    public List<OrderManage> listOrderManageForExport(OrderManage orderManage) {
        return orderManageMapper.listOrderManageForExport(orderManage);
    }

    @Override
    public Map<String, String> getOrderSummary(OrderManage orderManage) {
        return orderManageMapper.getOrderSummary(orderManage);
    }

    @Override
    public int getReceivable(OrderManage orderManage) {
        return orderManageMapper.getReceivable(orderManage);
    }
    
    @Override
    public OrderManage getOrderForDetail(Integer id) {
        return orderManageMapper.getOrderForDetail(id);
    }
    @Override
    public OrderManage getOrderWithRecord(Integer id) {
        return orderManageMapper.getOrderWithRecord(id);
    }
    @Override
    public OrderManage getOrderForOrderNo(String orderNo) {
        return orderManageMapper.getOrderForOrderNo(orderNo);
    }

    @Override
    public int getOrderData(Integer departmentId) {
        return orderManageMapper.getOrderData(departmentId);
    }

    @Override
    public int getOrderReminder(Integer deparmentId) {
        return orderManageMapper.getOrderReminder(deparmentId);
    }
    @Override
    @Transactional
    public int modifyOrder(OrderManage orderManage) {
        OrderManage orderManage1 = orderManageMapper.getOrderForDetail(orderManage.getId());
        if(orderManage1.getScheduledId()!=null){//删除原服务,重新添加预约服务
        	int row = scheduleServeMapper.deleteScheduleServeByServeId(orderManage1.getScheduledId());
        }
        
        Map<Integer, OrderServe> map = new HashMap<Integer, OrderServe>();
        if(orderManage1!=null&&orderManage1.getListOrderServer()!=null){
        	List<OrderServe> list = orderManage1.getListOrderServer();
			for(OrderServe item:list){
				map.put(item.getId(),item);
			}
        }
        int result = 0;
        List<OrderServe> updateList = new ArrayList<OrderServe>();
        List<OrderServe> insertList = new ArrayList<OrderServe>();
        for(OrderServe orderServe : orderManage.getListOrderServer()){
        	if(orderServe.getServeId()!=null) {
        		Serve serve = serveMapper.getServeForDetail(orderServe.getServeId());
        		orderServe.setServePrice(serve.getPrice());
        		orderServe.setServe(serve);
        	}
        	
        	if(orderManage1.getScheduledId()!=null){//新增预约服务,上面已删光
            	ScheduleServe item = new ScheduleServe();
            	item.setSchId(orderManage1.getScheduledId());
            	if(orderServe.getTechnicianId()!=null){
            		String techIds[] = orderServe.getTechnicianId().split(",");
            		if(techIds.length>0){
            			try {
            				int techId = Integer.parseInt(techIds[0]);
            				item.setTechnicianId(techId);
						} catch (Exception e) {
							logger.info("技师id转换出错:"+orderServe.getTechnicianId());
						}
            		}
            	}
            	item.setServeId(orderServe.getServeId());
            	item.setStartTime(orderServe.getScheduledTime());
            	item.setEndTime(new Date(orderServe.getScheduledTime().getTime()+orderServe.getServe().getServiceDuration()*60000));
            	item.setCreateTime(orderServe.getScheduledTime());
            	item.setServeNumber(orderManage.getListOrderServer().size());
            	scheduleServeMapper.insertScheduleServe(item);
        	}
        	
        	if(map.get(orderServe.getId()) != null){
                orderServe.setServeNumber(1);
                orderServe.setCreateTime(new Date());
                map.remove(orderServe.getId());
                updateList.add(orderServe);
        	}else{
              orderServe.setOrderId(orderManage.getId());
              orderServe.setServeNumber(1);
              orderServe.setCreateTime(new Date());
              insertList.add(orderServe);
        	}
        }
        Iterator<Integer> it = map.keySet().iterator();//删除不要的服务
        while(it.hasNext()){
        	Integer serveId= it.next();
        	orderServeMapper.deleteOrderServeById(serveId);
        }
        if(insertList != null && insertList.size() > 0){//插入新增的服务
            result += orderServeMapper.insertOrderServeByUpdate(insertList);
        }
        if(updateList != null && updateList.size() > 0){//更新修改的服务
            result += orderServeMapper.modifyOrderServeByUpdate(updateList);
        }
        updateOrderManage(orderManage);
        return result;
    }

    @Override
    @Transactional
    public JsonResult updateOrderStatusFinash(OrderManage orderManage) {
        JsonResult jsonResult = new JsonResult();
        OrderManage orderManage1 = orderManageMapper.getOrderForDetail(orderManage.getId());
        List<OrderServe> listOrderServe = orderServeMapper.listOrderServe(orderManage.getId());

        OrderComment orderComment = new OrderComment();
        if(orderManage1.getOrderUser() != null){
            orderComment.setUserId(orderManage1.getOrderUser());
        }
        orderComment.setOrderId(orderManage.getId());
        //orderComment.setOrderServeId("");

        orderComment.setCreateTime(new Date());
        orderComment.setStatus(Constans.ORDER_COMMENT_EVALUATED);
        orderCommentMapper.insertOrderComment(orderComment);
        /*for(OrderServe orderServe : listOrderServe){
            OrderComment orderComment = new OrderComment();
            if(orderManage1.getOrderUser() != null){
                orderComment.setUserId(orderManage1.getOrderUser());
            }
            orderComment.setOrderId(orderManage.getId());
            orderComment.setOrderServeId(orderServe.getId());

            orderComment.setCreateTime(new Date());
            orderComment.setStatus(Constans.ORDER_COMMENT_EVALUATED);
            orderCommentMapper.insertOrderComment(orderComment);
            logger.info("新增未评论订单：" + orderComment);
        }*/
        // 评价跟随单个订单走  放弃评价跟随订单服务走
        orderManage.setFinishTime(new Date());
        logger.info("修改订单状态：" + orderManage);

//        try{
//            MultiValueMap<String, Object> map= new LinkedMultiValueMap<String, Object>();
//            map.add("couponRelevanceId", orderManage1.getCouponRelevanceId() == null ? "" : String.valueOf(orderManage1.getCouponRelevanceId()));
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//            HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<MultiValueMap<String, Object>>(map, headers);
//            ResponseEntity<JsonResult> responseEntity = restTemplate.postForEntity(customerUrl + "couponUse", entity, JsonResult.class);
//        }catch (Exception e){
//            e.printStackTrace();
//        }

        if(orderManage.getMultiplePaymentType() != null){
            List<Map> list =  JSONObject.parseArray(orderManage.getMultiplePaymentType(), Map.class);
            for(Map paymentMap : list){
            	Integer customerId=orderManage1.getCustomer() == null ? null : orderManage1.getCustomer().getId();
            	BigDecimal bigDecimal = new BigDecimal(paymentMap.get("price").toString());
            	bigDecimal = bigDecimal.multiply(new BigDecimal("100"));
               if("2".equals(paymentMap.get("type").toString())){
                   
                   
                   MultiValueMap<String, Object> map= new LinkedMultiValueMap<String, Object>();
                   map.add("customerId",customerId==null?"":String.valueOf(customerId ));
                   map.add("orderId", String.valueOf(orderManage.getId()));
                   map.add("amount", bigDecimal == null ? "0" : bigDecimal.setScale( 0, BigDecimal.ROUND_DOWN ).toString());
                   map.add("departmentId", String.valueOf(orderManage1.getDepartmentId()));
                   map.add("couponRelevanceId", orderManage1.getCouponRelevanceId() == null ? "" : String.valueOf(orderManage1.getCouponRelevanceId()));
                   HttpHeaders headers = new HttpHeaders();
                   headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                   HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<MultiValueMap<String, Object>>(map, headers);
                   ResponseEntity<JsonResult> responseEntity = restTemplate.postForEntity(customerUrl+"consumeBalance", entity, JsonResult.class);
                   JsonResult jsonResult1 = responseEntity.getBody();
                   LinkedHashMap linkedHashMap = (LinkedHashMap) jsonResult1.getData();
                   if(!(jsonResult1.getSuccess())) {
                       jsonResult.setSuccess(JsonResult.CODE_ERROR).setMessage(jsonResult1.getMessage());
                       TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                       return jsonResult;
                   }
               }else{
                   //HUI 会员卡消费以外的消费也要记录到customer_balance_record表
                   customerBalanceService.insertSelective(customerId, orderManage1.getDepartmentId(), "",
                		   bigDecimal.toBigInteger().intValue(), orderManage.getId(), orderManage1.getRemark(), Integer.parseInt(paymentMap.get("type").toString()));
               }
            }
        }
        
        
        
        try{
            if(orderManage1.getCustomer() != null){
                MultiValueMap<String, Object> map= new LinkedMultiValueMap<String, Object>();
                map.add("openid", orderManage1.getCustomer().getOpenid());
                map.add("customerId", String.valueOf(orderManage1.getCustomer().getId()));
                map.add("orderAmount", orderManage1.getTotalPrice().subtract(orderManage.getDiscountPrice() == null ? new BigDecimal(0) : orderManage.getDiscountPrice()).setScale(2, RoundingMode.HALF_UP).toString());
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<MultiValueMap<String, Object>>(map, headers);
                ResponseEntity<JsonResult> responseEntity = restTemplate.postForEntity(customerUrl + "sendTicketAfterOrder", entity, JsonResult.class);
            }
        }catch(Exception e) {
            e.printStackTrace();
        }

        try{
            if(orderManage1.getCustomer() != null&&orderManage1.getCustomer().getOpenid()!=null){
                MultiValueMap<String, Object> map= new LinkedMultiValueMap<String, Object>();
                map.add("openid", orderManage1.getCustomer().getOpenid());
                map.add("customerId", String.valueOf(orderManage1.getCustomer().getId()));
                map.add("orderId", String.valueOf(orderManage.getId()));
                map.add("url", "");
                map.add("technicianName", orderManage1.getListOrderServer().get(0).getListTechnicianManage().get(0).getName());
                map.add("serveName", orderManage1.getListOrderServer().get(0).getProduceName());
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<MultiValueMap<String, Object>>(map, headers);
                ResponseEntity<JsonResult> responseEntity = restTemplate.postForEntity(evaluationMsg, entity, JsonResult.class);
            }else{
            	logger.info("openid为空,无法提示模板消息");
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
        int result = orderManageMapper.updateOrderManage(orderManage);
        if(result > 0){
            jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC);
        }
        return jsonResult;
    }

    @Override
    public int updateOrderDataIntegrity(OrderManage orderManage) {
        return orderManageMapper.updateOrderDataIntegrity(orderManage);
    }

    @Override
    public int updateOrderPayMentType(OrderManage orderManage) {
        return orderManageMapper.updateOrderManage(orderManage);
    }


    @Override
    @Transactional
    public int updateOrderManage(OrderManage orderManage) {
        OrderManage orderForDetail = orderManageMapper.getOrderForDetail(orderManage.getId());
        BigDecimal oldOrderPrice = orderManage.getChangePrice();
        List<OrderServe> listOrderServe = orderForDetail.getListOrderServer();
        BigDecimal totalPrice = new BigDecimal(0);
        for(OrderServe orderServe : listOrderServe){
        	//发送被预约的模板消息
//        	if(orderServe.getTechnicianId()!=null){
//        		Integer techId = Integer.parseInt(orderServe.getTechnicianId());
//        		TechnicianManage technicianManage = technicianManageService.getTechnicianManageForDetail(techId);
//        		if(technicianManage!=null){
//        			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        			sendTemplateMsgService.sendBeBookedMsg(technicianManage.getOpenid(), orderForDetail.getId(),
//        					sdf.format(orderServe.getScheduledTime()), orderForDetail.getDepartmentName(), technicianManage.getName(), orderServe.getServeName(), 
//        					orderForDetail.getCustomer().getRealName(), orderForDetail.getCustomer().getPhone());
//        		}
//        	}
            if(orderServe.getServePrice() != null){
                BigDecimal bigDecimal = orderServe.getServePrice();
                BigDecimal bigDecimal1 = orderServe.getServeChangePrice();
                if(bigDecimal != null){
                    totalPrice = totalPrice.add(bigDecimal);
                }
                if(bigDecimal1 != null) {
                    totalPrice = totalPrice.add(bigDecimal1);
                }
            }else if(orderServe.getServe() != null){
                BigDecimal bigDecimal = orderServe.getServe().getPrice();
                BigDecimal bigDecimal1 = orderServe.getServeChangePrice();
                if(bigDecimal != null){
                    totalPrice = totalPrice.add(bigDecimal);
                }
                if(bigDecimal1 != null) {
                    totalPrice = totalPrice.add(bigDecimal1);
                }
            }
        }
        orderManage.setTotalPrice(totalPrice);
//        if(oldOrderPrice != null && "true".equals(orderManage.getChangeStatus())){
//            orderManage.setChangePrice(oldOrderPrice.subtract(totalPrice));
//        }else if(oldOrderPrice != null && "false".equals(orderManage.getChangeStatus())){
//            orderManage.setChangePrice(new BigDecimal(0));
//        }
//        if(orderForDetail.getOrderUser() != null){
//        BigDecimal totelBrancePrice =  totalPrice.multiply(new BigDecimal(100));
//        Integer intTotalPrice = totelBrancePrice.intValue();
//        MultiValueMap<String, Object> multiValueMap= new LinkedMultiValueMap<String, Object>();
//        multiValueMap.add("customerId", orderForDetail.getOrderUser().toString());
//        multiValueMap.add("price", intTotalPrice.toString());
//        multiValueMap.add("couponRelevanceId", "");
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<MultiValueMap<String, Object>>(multiValueMap, headers);
//        ResponseEntity<JsonResult> responseEntity = restTemplate.postForEntity(customerUrl + "count", entity, JsonResult.class);
//        JsonResult jsonResult1 = responseEntity.getBody();
//        LinkedHashMap linkedHashMap = (LinkedHashMap) jsonResult1.getData();
//        orderManage.setDiscountPrice(new BigDecimal((Double) linkedHashMap.get("discountPrice")).divide(new BigDecimal(100),2, BigDecimal.ROUND_HALF_UP));
//        }
//        orderManage.setLaunchTime(new Date());
        orderManage.setUpdateTime(new Date());
        
        try{
            if(orderForDetail.getCustomer() != null &&orderForDetail.getCustomer().getOpenid() != null && "true".equals(orderManage.getReceipt())){
                MultiValueMap<String, Object> map= new LinkedMultiValueMap<String, Object>();
                map.add("openid", orderForDetail.getCustomer().getOpenid());
                map.add("customerId", String.valueOf(orderForDetail.getCustomer().getId()));
                map.add("orderId", String.valueOf(orderManage.getId()));
                map.add("url", "");
                if(orderForDetail.getListOrderServer().get(0).getProduceName() != null) {
                    map.add("serveName", orderForDetail.getListOrderServer().get(0).getProduceName());
                }else {
                    map.add("serveName", orderForDetail.getListOrderServer().get(0).getServe().getName());
                }
                map.add("amount", orderForDetail.getTotalPrice().setScale(2, RoundingMode.HALF_UP).toString());
                map.add("orderNo", orderForDetail.getOrderNo());
                map.add("time", new SimpleDateFormat("yyyy年MM月dd日 HH时mm分").format(new Date()));
                map.add("validityTime", "1分钟");
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<MultiValueMap<String, Object>>(map, headers);
                ResponseEntity<JsonResult> responseEntity = restTemplate.postForEntity(toPay, entity, JsonResult.class);
            }else{
            	logger.info("支付提醒,没有用户信息,不发模板消息orderForDetail:"+orderForDetail);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return orderManageMapper.updateOrderManage(orderManage);
    }

    @Override
    @Transactional
    public int startPay(OrderManage orderManage) {
        OrderManage orderForDetail = orderManageMapper.getOrderForDetail(orderManage.getId());
        //设置结束服务时间为当前时间
        OrderServe orderServeEndTime = new OrderServe();
        orderServeEndTime.setOrderId(orderManage.getId());
        orderServeEndTime.setEndTime(new Date());
        orderServeMapper.updateOrderServiceByOrderId(orderServeEndTime);
        
        BigDecimal oldOrderPrice = orderManage.getChangePrice();
        List<OrderServe> listOrderServe = orderForDetail.getListOrderServer();
        BigDecimal totalPrice = new BigDecimal(0);
        for(OrderServe orderServe : listOrderServe){
            if(orderServe.getServePrice() != null){
                BigDecimal bigDecimal = orderServe.getServePrice();
                BigDecimal bigDecimal1 = orderServe.getServeChangePrice();
                if(bigDecimal != null){
                    totalPrice = totalPrice.add(bigDecimal);
                }
                if(bigDecimal1 != null) {
                    totalPrice = totalPrice.add(bigDecimal1);
                }
            }else if(orderServe.getServe() != null){
                BigDecimal bigDecimal = orderServe.getServe().getPrice();
                BigDecimal bigDecimal1 = orderServe.getServeChangePrice();
                if(bigDecimal != null){
                    totalPrice = totalPrice.add(bigDecimal);
                }
                if(bigDecimal1 != null) {
                    totalPrice = totalPrice.add(bigDecimal1);
                }
            }
        }
        orderManage.setTotalPrice(totalPrice);
        if(oldOrderPrice != null && "true".equals(orderManage.getChangeStatus())){
            orderManage.setChangePrice(oldOrderPrice.subtract(totalPrice));
        }else if(oldOrderPrice != null && "false".equals(orderManage.getChangeStatus())){
            orderManage.setChangePrice(new BigDecimal(0));
        }
//        if(orderForDetail.getOrderUser() != null){
//        BigDecimal totelBrancePrice =  totalPrice.multiply(new BigDecimal(100));
//        Integer intTotalPrice = totelBrancePrice.intValue();
//        MultiValueMap<String, Object> multiValueMap= new LinkedMultiValueMap<String, Object>();
//        multiValueMap.add("customerId", orderForDetail.getOrderUser().toString());
//        multiValueMap.add("price", intTotalPrice.toString());
//        multiValueMap.add("couponRelevanceId", "");
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<MultiValueMap<String, Object>>(multiValueMap, headers);
//        ResponseEntity<JsonResult> responseEntity = restTemplate.postForEntity(customerUrl + "count", entity, JsonResult.class);
//        JsonResult jsonResult1 = responseEntity.getBody();
//        LinkedHashMap linkedHashMap = (LinkedHashMap) jsonResult1.getData();
//        orderManage.setDiscountPrice(new BigDecimal((Double) linkedHashMap.get("discountPrice")).divide(new BigDecimal(100),2, BigDecimal.ROUND_HALF_UP));
//        }
        orderManage.setLaunchTime(new Date());
        orderManage.setUpdateTime(new Date());

        try{
            if(orderForDetail.getCustomer() != null && "true".equals(orderManage.getReceipt())){
//                MultiValueMap<String, Object> map= new LinkedMultiValueMap<String, Object>();
//                map.add("openid", orderForDetail.getCustomer().getOpenid());
//                map.add("customerId", String.valueOf(orderForDetail.getCustomer().getId()));
//                map.add("orderId", String.valueOf(orderManage.getId()));
//                map.add("url", "");
//                if(orderForDetail.getListOrderServer().get(0).getProduceName() != null) {
//                    map.add("serveName", orderForDetail.getListOrderServer().get(0).getProduceName());
//                }else {
//                    map.add("serveName", orderForDetail.getListOrderServer().get(0).getServe().getName());
//                }
//                map.add("amount", orderForDetail.getTotalPrice().setScale(2, RoundingMode.HALF_UP).toString());
//                map.add("orderNo", orderForDetail.getOrderNo());
//                map.add("time", new SimpleDateFormat("yyyy年MM月dd日 HH时mm分").format(new Date()));
//                map.add("validityTime", "1分钟");
//                HttpHeaders headers = new HttpHeaders();
//                headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//                HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<MultiValueMap<String, Object>>(map, headers);
//                ResponseEntity<JsonResult> responseEntity = restTemplate.postForEntity(maToPay, entity, JsonResult.class);
            	sendXcxTemplateMsgService.sendWaitForPayMsg(orderForDetail.getCustomer().getOpenid1(), orderManage.getId(), orderForDetail.getOrderNo(),
            			orderForDetail.getListOrderServer().get(0).getServe().getName(), orderForDetail.getTotalPrice().setScale(2, RoundingMode.HALF_UP).toString());
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return orderManageMapper.updateOrderManage(orderManage);
    }

    @Override
    @Transactional
    public int modifyOrderServe(OrderManage orderManage) {
        OrderManage orderManage1 = orderManageMapper.getOrderForDetail(orderManage.getId());

        if(orderManage.getOrderType() != null && !("3".equals(orderManage.getOrderType()))){
            orderManage.setTelephone("");
        }

        try{
            MultiValueMap<String, Object> map= new LinkedMultiValueMap<String, Object>();
            map.add("phone", orderManage.getTelephone() == null ? "" : orderManage.getTelephone());
            map.add("storeRecordRealName", orderManage.getStoreScheduleUsername() == null ? "" : orderManage.getStoreScheduleUsername());
            map.add("source", String.valueOf(Constans.MEMBER_ORDER));
            map.add("registerDepartment", String.valueOf(orderManage.getDepartmentId()));
            map.add("oldId", String.valueOf(orderManage1.getOrderUser()));
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<MultiValueMap<String, Object>>(map, headers);
            ResponseEntity<JsonResult> responseEntity = restTemplate.postForEntity(customerUrl + "insertOrUpdateCustomer", entity, JsonResult.class);
            JsonResult jsonResult1 = responseEntity.getBody();
            LinkedHashMap memberMap = (LinkedHashMap) jsonResult1.getData();
            orderManage.setOrderUser((Integer) memberMap.get("id"));
        }catch (Exception e){
            logger.info("订单新增会员失败");
            e.printStackTrace();
        }
        String deleteOrder = orderManage.getDeleteOrder();
        if(deleteOrder != null && !("".equals(deleteOrder))){
            String orderServeId[] = deleteOrder.split(",");
            for(String id : orderServeId){
                orderServeMapper.deleteOrderServeById(Integer.parseInt(id));
            }
        }
        int result = 1;
        List<OrderServe> updateList = new ArrayList<OrderServe>();
        List<OrderServe> insertList = new ArrayList<OrderServe>();
        for(OrderServe orderServe : orderManage.getListOrderServer()){
            if(orderServe.getUpdateType() == 0){
                orderServe.setServeNumber(1);
                orderServe.setCreateTime(new Date());
                updateList.add(orderServe);
            }else if(orderServe.getUpdateType() == 1){
                orderServe.setOrderId(orderManage.getId());
                orderServe.setServeNumber(1);
                orderServe.setCreateTime(new Date());
                /*orderServe.setServeName("");
                orderServe.setServeType("");*/
                insertList.add(orderServe);
            }
        }
        if(insertList != null && insertList.size() > 0){
            result = orderServeMapper.insertOrderServeByUpdate(insertList);
        }
        if(updateList != null && updateList.size() > 0){
            result = orderServeMapper.modifyOrderServeByUpdate(updateList);
        }
        updateOrderManage(orderManage);
        return result;
    }



    @Override
    public int deleteOrderServeById(Integer id) {
        OrderServe orderServe = orderServeMapper.getOrderServeDetail(id);
        OrderManage orderManage = orderManageMapper.getOrderForDetail(orderServe.getId());
        int result = orderServeMapper.deleteOrderServeById(id);
        updateOrderManage(orderManage);
        return result;
    }

    @Override
    @Transactional
    public int deleteOrderManageById(OrderManage om) {
        OrderManage orderManage = orderManageMapper.getOrderForDetail(om.getId());
        int result = orderManageMapper.deleteOrderManageById(om);
        List<Integer> listOrderServe = orderServeMapper.listOrderServeById(om.getId());
//        if(listOrderServe != null){
//            orderRecordMapper.deleteOrderRecord(listOrderServe);
//        }
//        result = orderServeMapper.deleteOrderServeById(om.getId());

        try{
            MultiValueMap<String, Object> map= new LinkedMultiValueMap<String, Object>();
            map.add("phone", orderManage.getTelephone() == null ? "" : orderManage.getTelephone());
            map.add("customerId", String.valueOf(orderManage.getOrderUser()));
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<MultiValueMap<String, Object>>(map, headers);
            ResponseEntity<JsonResult> responseEntity = restTemplate.postForEntity(customerUrl + "minusOrderTimes", entity, JsonResult.class);
            JsonResult jsonResult1 = responseEntity.getBody();
            logger.info("删除订单调用用户次数减1成功");
        }catch (Exception e){
            logger.info("订单会员减少次数失败");
            e.printStackTrace();
        }

        return result;
    }

    @Override
    @Transactional
    public JsonResult insertOrderManageByUser(OrderManage orderManage) {
        logger.info("店铺端新增订单开始...");
        JsonResult jsonResult = new JsonResult();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        orderManage.setCreateTime(new Date());
        orderManage.setStatus(Constans.ORDER_STATUS_SERVE);
        if(orderManage.getOrderType() != null && !("3".equals(orderManage.getOrderType()))){
            orderManage.setTelephone("");
        }else{
            orderManage.setOrderType(Constans.ORDER_TYPE_STORE);
        }

        try{
            MultiValueMap<String, Object> map1= new LinkedMultiValueMap<String, Object>();
            map1.add("phone", orderManage.getTelephone() == null ? "" : orderManage.getTelephone());
            map1.add("storeRecordRealName", orderManage.getStoreScheduleUsername() == null ? "" : orderManage.getStoreScheduleUsername());
            map1.add("source", String.valueOf(Constans.MEMBER_ORDER));
            map1.add("registerDepartment", String.valueOf(orderManage.getDepartmentId()));
            map1.add("openOrderUser", String.valueOf(orderManage.getOpenOrderUser()));
            map1.add("oldId", "");
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<MultiValueMap<String, Object>>(map1, headers);
            ResponseEntity<JsonResult> responseEntity = restTemplate.postForEntity(customerUrl + "insertOrUpdateCustomer", entity, JsonResult.class);
            JsonResult jsonResult1 = responseEntity.getBody();
            LinkedHashMap memberMap = (LinkedHashMap) jsonResult1.getData();
            orderManage.setOrderUser((Integer) memberMap.get("id"));
            logger.info("店铺端订单-会员操作");
        }catch (Exception e){
            logger.info("订单新增会员失败");
            e.printStackTrace();
        }
        Integer serveId = null;
        Integer techId = null;
        try {
        	serveId = orderManage.getListOrderServer().get(0).getServeId();
		} catch (Exception e) {}
        try {
        	techId = Integer.parseInt(orderManage.getListOrderServer().get(0).getTechnicianId().split(",")[0]);
		} catch (Exception e) {}
        orderNoLock.lock(0);
        orderManage.setOrderNo(createOrderNO(orderManage.getDepartmentId(),serveId,techId));
        int resultOrderManage = orderManageMapper.insertOrderOrderManageByUser(orderManage);
        orderNoLock.unlock(0);
        
        logger.info("新增订单："+ orderManage);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("orderId", orderManage.getId());
        map.put("createTime", simpleDateFormat.format(new Date()));
        map.put("listScheduleServe", orderManage.getListOrderServer());
        int resultOrderServe = orderServeMapper.insertOrderServe(map);
        logger.info("新增订单服务："+ map);
        if(resultOrderManage > 0 && resultOrderServe > 0){
            jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC);
        }
            updateOrderManage(orderManage);  //更改订单总价和折后价
        return jsonResult;
    }

    @Override
    @Transactional
    public JsonResult insertOrderManage(Integer schId, Integer openOrderUser) {
        logger.info("开单操作开始...");
        JsonResult jsonResult = new JsonResult();
        ScheduleManage schManage = scheduleManageMapper.getScheduleForDetail(schId);
        OrderManage orderManage = new OrderManage();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        orderManage.setCreateTime(new Date());
        orderManage.setStatus(Constans.ORDER_STATUS_SERVE);
        if(Constans.SCHEDULE_TYPE_USER == schManage.getScheduledType()){  //用户端
            orderManage.setOrderType(Constans.ORDER_TYPE_USER);
        }else if(Constans.SCHEDULE_TYPE_PC == schManage.getScheduledType()) {  //店铺端
            orderManage.setOrderType(Constans.ORDER_TYPE_SCHEDULE);
        }
        ScheduleManage scheduleManage = new ScheduleManage();
        scheduleManage.setId(schId);
        scheduleManage.setOpenbillTime(new Date());
        scheduleManage.setStatus(Constans.SCHEDULE_STATUS_WAITSERVE);
        scheduleManageMapper.updateSchedule(scheduleManage);
        logger.info("修改预约状态为守约："+ scheduleManage);
        List<ScheduleServe> listScheduleServe = scheduleServeMapper.listScheduleServeBySchId(schId);
        /*BigDecimal totelPrice = new BigDecimal(0);
        for(ScheduleServe scheduleServe : listScheduleServe){
            totelPrice = totelPrice.add(scheduleServe.getServe().getPrice());
        }
        orderManage.setTotelPrice(totelPrice);*/
        
        //创建订单时加锁
        Integer serveId = null;
        Integer techId = null;
        try {
        	serveId = listScheduleServe.get(0).getServeId();
		} catch (Exception e) {}
        try {
        	techId = listScheduleServe.get(0).getTechnicianId();
		} catch (Exception e) {}
        orderNoLock.lock(0);
        orderManage.setOrderNo(createOrderNO(schManage.getDepartmentId(),serveId,techId));
        int result = orderManageMapper.insertOrderManage(orderManage, schId, openOrderUser);
        orderNoLock.unlock(0);


        logger.info("预约生成订单："+ orderManage);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("orderId", orderManage.getId());
        map.put("createTime", simpleDateFormat.format(new Date()));
        map.put("listScheduleServe", listScheduleServe);
        if(listScheduleServe != null &&listScheduleServe.size() > 0){
            orderServeMapper.insertOrderServe(map);
        }
        updateOrderManage(orderManage);
        logger.info("预约服务生成订单服务(开单成功)："+ map);
        jsonResult.setSuccess(true).setMessage(JsonResult.APP_DEFINE_SUC).setData(map);

        try{
            ScheduleManage scheduleManage1 = scheduleManageMapper.getScheduleForDetail(schId);
            MultiValueMap<String, Object> map1= new LinkedMultiValueMap<String, Object>();
            map1.add("customerId", String.valueOf(scheduleManage1.getScheduledUser()));
            map1.add("phone", scheduleManage1.getTelephone());
            map1.add("openOrderUser", String.valueOf(openOrderUser));
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<MultiValueMap<String, Object>>(map1, headers);
            ResponseEntity<JsonResult> responseEntity = restTemplate.postForEntity(customerUrl + "addOrderTimes", entity, JsonResult.class);
            JsonResult jsonResult1 = responseEntity.getBody();
            Integer memberUserId = (Integer) jsonResult1.getData();
            logger.info("开单操作订单次数加一：用户id"+scheduleManage1.getScheduledUser());
        }catch (Exception e){
            logger.error("开单操作订单次数加一失败");
            e.printStackTrace();
        }
        return jsonResult;
    }
    @Override
    @Transactional
    public JsonResult insertOrderManageXcx(Integer schId, Integer openOrderUser) {
        logger.info("开单操作开始...");
        JsonResult jsonResult = new JsonResult();
        ScheduleManage schManage = scheduleManageMapper.getScheduleForDetail(schId);
        OrderManage orderManage = new OrderManage();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        orderManage.setCreateTime(new Date());
        orderManage.setDiscountPrice(new BigDecimal("0"));
        orderManage.setStatus(Constans.ORDER_STATUS_WAIT);
//        if(Constans.SCHEDULE_TYPE_USER == schManage.getScheduledType()){  //用户端
//            orderManage.setOrderType(Constans.ORDER_TYPE_USER);
//        }else if(Constans.SCHEDULE_TYPE_PC == schManage.getScheduledType()) {  //店铺端
//            orderManage.setOrderType(Constans.ORDER_TYPE_SCHEDULE);
//        }
        orderManage.setOrderType(Constans.ORDER_TYPE_XCX);//这边的预约都是小程序预约
        ScheduleManage scheduleManage = new ScheduleManage();
        scheduleManage.setId(schId);
        scheduleManage.setOpenbillTime(new Date());
        scheduleManage.setStatus(Constans.SCHEDULE_STATUS_WAITSERVE);
        scheduleManageMapper.updateSchedule(scheduleManage);
        logger.info("修改预约状态为守约："+ scheduleManage);
        List<ScheduleServe> listScheduleServe = scheduleServeMapper.listScheduleServeBySchId(schId);
        /*BigDecimal totelPrice = new BigDecimal(0);
        for(ScheduleServe scheduleServe : listScheduleServe){
            totelPrice = totelPrice.add(scheduleServe.getServe().getPrice());
        }
        orderManage.setTotelPrice(totelPrice);*/
        Integer serveId = null;
        Integer techId = null;
        try {
        	serveId = listScheduleServe.get(0).getServeId();
		} catch (Exception e) {}
        try {
        	techId = listScheduleServe.get(0).getTechnicianId();
		} catch (Exception e) {}
        orderNoLock.lock(0);
        orderManage.setOrderNo(createOrderNO(schManage.getDepartmentId(),serveId,techId));
        int result = orderManageMapper.insertOrderManage(orderManage, schId, openOrderUser);
        orderNoLock.unlock(0);

        logger.info("预约生成订单："+ orderManage);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("orderId", orderManage.getId());
        map.put("createTime", simpleDateFormat.format(new Date()));
        map.put("listScheduleServe", listScheduleServe);
        if(listScheduleServe != null &&listScheduleServe.size() > 0){
            orderServeMapper.insertOrderServe(map);
        }
        updateOrderManage(orderManage);
        logger.info("预约服务生成订单服务(开单成功)："+ map);
        jsonResult.setSuccess(true).setMessage(JsonResult.APP_DEFINE_SUC).setData(orderManage.getId());

        try{
            ScheduleManage scheduleManage1 = scheduleManageMapper.getScheduleForDetail(schId);
            MultiValueMap<String, Object> map1= new LinkedMultiValueMap<String, Object>();
            map1.add("customerId", String.valueOf(scheduleManage1.getScheduledUser()));
            map1.add("phone", scheduleManage1.getTelephone());
            map1.add("openOrderUser", String.valueOf(openOrderUser));
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<MultiValueMap<String, Object>>(map1, headers);
            ResponseEntity<JsonResult> responseEntity = restTemplate.postForEntity(customerUrl + "addOrderTimes", entity, JsonResult.class);
            JsonResult jsonResult1 = responseEntity.getBody();
            Integer memberUserId = (Integer) jsonResult1.getData();
            logger.info("开单操作订单次数加一：用户id"+scheduleManage1.getScheduledUser());
        }catch (Exception e){
            logger.error("开单操作订单次数加一失败");
            e.printStackTrace();
        }
        return jsonResult;
    }

    @Override
    @Transactional
    public JsonResult insertRechargeOrder(OrderManage orderManage) {
        JsonResult jsonResult = new JsonResult();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        orderManage.setCreateTime(new Date());
        orderManage.setStatus(Constans.ORDER_STATUS_FINISH);
        orderManage.setOrderType(Constans.ORDER_TYPE_RECHARGE);
        Integer serveId = null;
        Integer techId = null;
        try {
        	serveId = orderManage.getListOrderServer().get(0).getServeId();
		} catch (Exception e) {}
        try {
        	techId = Integer.parseInt(orderManage.getListOrderServer().get(0).getTechnicianId().split(",")[0]);
		} catch (Exception e) {}
        orderNoLock.lock(0);
        orderManage.setOrderNo(createOrderNO(orderManage.getDepartmentId(),serveId,techId));
        int result = orderManageMapper.insertRechargeOrder(orderManage);
        orderNoLock.unlock(0);

        if(result > 0){
            jsonResult.setSuccess(true).setMessage(JsonResult.APP_DEFINE_SUC).setData(orderManage);
        }
        return jsonResult;
    }

    //创建订单id
    public String createOrderNO(Integer storeId,Integer serveId,Integer techId){
    	StringBuffer orderNo = new StringBuffer("");
    	//店铺短码
    	if(storeId!=null){
    		ShopManage shop = shopManageService.getShopManageForDetail(storeId);
        	if(shop!=null&&shop.getShortCode()!=null){
        		orderNo.append(shop.getShortCode());
        	}else{
        		orderNo.append("NNN");
        	}
    	}else{
    		orderNo.append("NNN");
    	}
    	orderNo.append('-');
    	//服务类型短码
    	if(serveId!=null){
    		String shortCode = serveMapper.getServeShortCode(serveId);
        	if(shortCode!=null&&!shortCode.isEmpty()){
        		orderNo.append(shortCode);
        	}else{
        		orderNo.append("NNN");
        	}
    	}else{
    		orderNo.append("NNN");
    	}
    	orderNo.append('-');
    	//技师短码
    	if(techId!=null){
    		String shortCode = userTechnicianService.getShortCode(techId);
        	if(shortCode!=null&&!shortCode.isEmpty()){
        		orderNo.append(shortCode);
        	}else{
        		orderNo.append("NNN");
        	}
    	}else{
    		orderNo.append("NNN");
    	}
    	orderNo.append('-');
    	//年月日
    	SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
    	orderNo.append(sdf.format(new Date()));
    	orderNo.append('-');
    	//当天4位流水号
    	OrderManage maxOrder = orderManageMapper.getMaxOrderNo();
    	String serialNumber = "0001";
    	if(maxOrder!=null){
    		try {
    			String day = maxOrder.getOrderNo().substring(maxOrder.getOrderNo().length()-11,maxOrder.getOrderNo().length()-5);
    			if(day.equals(sdf.format(new Date()))){//今日流水
        			String hao = maxOrder.getOrderNo().substring(maxOrder.getOrderNo().length()-4);
        		    Integer intHao = Integer.parseInt(hao);
        		    intHao++;
        		    String strHao = intHao.toString();
        		    while (strHao.length() < 4)
        		        strHao = "0" + strHao;
        		    serialNumber = strHao;
    			}else{
    				logger.error("新的一天,订单号流水从0001开始");
    			}
			} catch (Exception e) {
				logger.error("创建订单号报错,e:"+e.getMessage());
			}
    	}else{
    		logger.error("找不到任何订单,创建订单号报错");
    	}
    	orderNo.append(serialNumber);
        return orderNo.toString();
    }
	@Override
	public double countSumPay(Integer customerId,Integer departmentId) {
		return orderManageMapper.countSumPay(customerId,departmentId);
	}
	@Override
	public int xcxUpdateOrderFinash(OrderManage orderDeatail,OrderManage orderManage,String formId) {
		//查如果有多种支付方式,就设置
		CustomerBalanceRecord customerBalanceRecord = new CustomerBalanceRecord();
		customerBalanceRecord.setOrderId(orderDeatail.getId().toString());
		List<CustomerBalanceRecord> listPay = customerBalanceService.getBalanceRecord(customerBalanceRecord);
		if(listPay!=null&&listPay.size()>0){
			int sizePayType = 0;
			List<String> multiplePayType = new ArrayList<String>();
			
			Iterator<CustomerBalanceRecord> iteratorPay = listPay.iterator();
			while(iteratorPay.hasNext()){
				sizePayType++;
				CustomerBalanceRecord item = iteratorPay.next();
				JSONObject itemDetail = new JSONObject();
				itemDetail.put("type", item.getSource());
				itemDetail.put("price", new BigDecimal(item.getOrderAmount()).divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP).doubleValue());
				multiplePayType.add(itemDetail.toJSONString());
			}
			if(sizePayType>1){
				orderManage.setMultiplePaymentType(multiplePayType.toString());
			}
		}
		int row = orderManageMapper.updateOrderManage(orderManage);
		//设置预约为已完成,该时间可再被预约
		if(orderDeatail.getScheduledId()!=null){
			ScheduleManage sm = new ScheduleManage();
			sm.setId(orderDeatail.getScheduledId());
			sm.setStatus(Constans.SCHEDULE_STATUS_ABIDING);
			scheduleManageMapper.updateSchedule(sm);
		}
		
//		//清空改订单的业绩
		List<OrderServe> list = orderDeatail.getListOrderServer();
		Iterator<OrderServe> iteratorClear = list.iterator();
		while(iteratorClear.hasNext()){
			OrderServe item = iteratorClear.next();
			item.setAchievement(null);
		}
		
		//查该订单使用次卡能作用到的服务,然后设置业绩
		//销次卡
		List<TimesCardUsedRecord> listTimesCardRecord = orderDeatail.getListTimesCardRecord();
		if(listTimesCardRecord!=null){
			Iterator<TimesCardUsedRecord> iterator = listTimesCardRecord.iterator();
			while(iterator.hasNext()){
				TimesCardUsedRecord item = iterator.next();
				//次卡记录设置已完成
				TimesCardUsedRecord ccr = new TimesCardUsedRecord();
				ccr.setId(item.getId());
				ccr.setStatus(2);
				int rowTimesCard = timesCardService.updateTimesCardUsedRecord(ccr);
				if(rowTimesCard>0){//次卡已使用次数要加1
					TimesCardCustomerRelevance timesCardCustomerRelevance = new TimesCardCustomerRelevance();
					timesCardCustomerRelevance.setId(item.getTimesCardCustomerRelevance().getId());
					int times = item.getTimesCardCustomerRelevance().getUsedTimes()+1;
					timesCardCustomerRelevance.setUsedTimes(times);
					if(item.getTimesCardType().getSumTimes()<=times){
						timesCardCustomerRelevance.setStatus(2);//设置次数已经用完
					}
					timesCardService.updateTimesCardCustomerRelevance(timesCardCustomerRelevance);
					//设置次卡服务业绩
					int orderServeId= item.getOrderServeId();
					Iterator<OrderServe> iteratorClear1 = list.iterator();
					while(iteratorClear1.hasNext()){
						OrderServe itemOs = iteratorClear1.next();
						if(itemOs.getId().equals(orderServeId)){
							itemOs.setAchievement(item.getTimesCardType().getPayAmount());//次卡业绩
							itemOs.setIsCountAchievement(1);//标记为已算次卡业绩
							break;
						}
					}
				}else{
					//应该不会出现次卡找不到的情况
				}
			}
		}
		
		
		
		//销优惠券+计算优惠券计算业绩的金额
		BigDecimal couponDiscountPrice = new BigDecimal("0");//内部券总优惠金额
		List<CouponOrderRelevance> listCouponOrderRelevance = orderDeatail.getListCouponOrderRelevance();
		if(listCouponOrderRelevance!=null){
			Iterator<CouponOrderRelevance> iterator = listCouponOrderRelevance.iterator();
			while(iterator.hasNext()){
				CouponOrderRelevance item = iterator.next();
				CouponCustomerRelevance ccr = new CouponCustomerRelevance();
				ccr.setId(item.getCouponCustomerRelevanceId());
				ccr.setCouponId(item.getCouponId());
				ccr.setState(Constans.COUPON_STATUS_USED);
				couponService.updateRelevanceByPrimaryKeySelective(ccr);
				if(item.getCoupon().getSourceType().equals(1)){//设置外部券服务业绩
					int orderServeId= item.getOrderServeId();
					Iterator<OrderServe> iteratorClear1 = list.iterator();
					while(iteratorClear1.hasNext()){
						OrderServe itemOs = iteratorClear1.next();
						if(itemOs.getId().equals(orderServeId)){
							itemOs.setAchievement(item.getCoupon().getPayAmount());//外部券业绩
							itemOs.setIsCountAchievement(2);//标记为已算外部券业绩
							break;
						}
					}
				}else{//计算内部券总优惠金额
					couponDiscountPrice=couponDiscountPrice.add(new BigDecimal(item.getCoupon().getDiscountAmount().toString()));
				}
			}
		}
		
		int countNailServe = 0;//记录有几个美甲服务
		BigDecimal rateTotalPrice = new BigDecimal("0");
		Iterator<OrderServe> iteratorClear1 = list.iterator();
		while(iteratorClear1.hasNext()){
			OrderServe itemOs = iteratorClear1.next();
			if(itemOs.getAchievement()==null||itemOs.equals(0)){//不是外部券和次卡的服务,按比例算业绩
				rateTotalPrice = rateTotalPrice.add(itemOs.getServePrice()).add(itemOs.getServeChangePrice());//算总金额,待会儿算比例
			}else{//次卡或外部券服务,按照实收金额算业绩
				//不用处理
			}
		}
		Iterator<OrderServe> iteratorClear2 = list.iterator();
		while(iteratorClear2.hasNext()){
			OrderServe itemOs = iteratorClear2.next();
			if(itemOs.getAchievement()==null||itemOs.equals(0)){//不是外部券和次卡的服务,按比例算业绩
				if(rateTotalPrice.compareTo(new BigDecimal("0"))==0){
					itemOs.setAchievement(new BigDecimal("0"));
					itemOs.setIsCountAchievement(3);
				}else {
					itemOs.setAchievement(rateTotalPrice.subtract(couponDiscountPrice).multiply(itemOs.getServePrice().add(itemOs.getServeChangePrice())).divide(rateTotalPrice,2, BigDecimal.ROUND_HALF_UP));
					itemOs.setIsCountAchievement(3);
				}
			}
			if(itemOs.getServe().getType().equals("美甲")){//计算有几个卸甲服务，然后送卸甲券
				countNailServe++;
			}
			itemOs.setExecutionStatus(Constans.ORDER_EXECUTION_COMMEND);
			orderServeMapper.updateOrderService(itemOs);
		}
		
		
		//业绩:服务单价/服务总价*(合计金额+外部券金额)
//		BigDecimal couponPayPrice = new BigDecimal("0");
//		if(list!=null&&list.size()!=0){
//			BigDecimal orderTotalPrice = orderDeatail.getTotalPrice();
//			if(orderTotalPrice!=null){//优惠券实付金额+订单金额
//				couponPayPrice=couponPayPrice.add(orderTotalPrice);
//			}
//			BigDecimal orderChangePrice = orderDeatail.getChangePrice();
//			if(orderChangePrice!=null){//+总改价
//				couponPayPrice=couponPayPrice.add(orderChangePrice);
//			}
//			BigDecimal orderDiscountPrice = orderDeatail.getDiscountPrice();
//			if(orderDiscountPrice!=null){//-总优惠
//				couponPayPrice=couponPayPrice.subtract(orderDiscountPrice);
//			}
//			
//			for(int i= 0;i<list.size();i++){
//				OrderServe orderServe = list.get(i);
//				BigDecimal servePrice = orderServe.getServe().getPrice();
//    			BigDecimal serveChangePrice = orderServe.getServeChangePrice();
//    			if(serveChangePrice!=null){
//    				servePrice.add(serveChangePrice);
//    			}
//				BigDecimal achievement = null;
//				if(orderTotalPrice!=null&&orderTotalPrice.compareTo(new BigDecimal("0"))==0){
//					achievement = new BigDecimal("0");
//				}else{
//					achievement = couponPayPrice.multiply(servePrice).divide(orderTotalPrice,2, BigDecimal.ROUND_HALF_UP);//按照比例分业绩
//				}
//				OrderServe updateOrderServe = new OrderServe();
//				updateOrderServe.setId(orderServe.getId());
//				updateOrderServe.setAchievement(achievement.compareTo(new BigDecimal("0"))<0?new BigDecimal("0"):achievement);
//				orderServeMapper.updateOrderService(updateOrderServe);
//				if(orderServe.getServe().getType().equals("美甲")){//计算有几个卸甲服务，然后送卸甲券
//					countNailServe++;
//				}
//			}
//		}
		//送卸甲券
		if(countNailServe>0){
	        //查卸甲券
	        Coupon coupon = couponService.selectByPrimaryKey(66);
	        if(coupon!=null){
	        	CouponCustomerRelevance ccr = new CouponCustomerRelevance();
	        	ccr.setCouponId(66);
	        	ccr.setCustomerId(orderDeatail.getOrderUser());
		        couponService.insertSelective(coupon,ccr);
	        }
		}

        return countNailServe;
    }
	@Override
	public int updateOrderStatusByScheduleId(OrderManage orderManage) {
		return orderManageMapper.updateOrderStatusByScheduleId(orderManage);
	}
	@Scheduled(cron = "0 1/2 * * * *")
    public void notifyCustomerToShopServic(){
//		logger.info("提前一个小时通知客户到店服务");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date startDate = new Date(new Date().getTime()+new Long("3480000"));
		Date endDate = new Date(new Date().getTime()+new Long("3600000"));
		List<OrderManage> list = orderManageMapper.getListOrderManageForCancle(sdf.format(startDate),sdf.format(endDate));
		Iterator<OrderManage> it = list.iterator();
		while (it.hasNext()) {
			OrderManage item = it.next();
	    	if(item==null){
	    		continue;
	    	}
	    	logger.info("提前一个小时提醒用户到店OrderManage:"+item);
	    	if(item.getCustomer()==null||item.getCustomer().getOpenid()==null){
	    		logger.info("找不到用户openid,跳过该预约订单提醒:"+item);
	    		continue;
	    	}
	    	String serveName = "";
	    	String time = "";
	    	String storeAddress = "gogirl美甲美睫沙龙";
	    	if(item.getListOrderServer()!=null&&item.getListOrderServer().size()>0
	    			&&item.getListOrderServer().get(0).getServe().getName()!=null){
	    		serveName = item.getListOrderServer().get(0).getServe().getName();
	    		time = sdf.format(item.getListOrderServer().get(0).getScheduledTime());
	    	}
	    	if(item.getDepartmentId()!=null){
	    		ShopManage sm = shopManageService.getShopManageForDetail(item.getDepartmentId());
	    		if(sm!=null){
	    			storeAddress = sm.getAddress();
	    		}
	    	}
	    	
	    	sendMpTemplateMsgService.scheduledRemindMsg(item.getCustomer().getOpenid(), 
	    			serveName, time, storeAddress, "欢迎准时到店服务.");
		}
    }
	@Scheduled(cron = "0 0/2 * * * *")
    public void canclePassSchedule(){
//		logger.info("定时检查有无过期的预约");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date pass15Date = new Date(new Date().getTime()-new Long("900000"));
		List<OrderManage> list = orderManageMapper.getListOrderManageForCancle(null,sdf.format(pass15Date));
		Iterator<OrderManage> it = list.iterator();
		while (it.hasNext()) {
			OrderManage item = it.next();
	    	if(item==null){
	    		continue;
	    	}
	    	OrderManage orderManage = new OrderManage();
	    	orderManage.setId(item.getId());
	    	orderManage.setStatus(9);
	    	orderManageMapper.updateOrderManage(orderManage);
	    	logger.info(item.getOrderNo()+"超时15分钟自动设置为失约orderManage:"+orderManage);
	    	//找到订单中的预约id
	    	if(item.getScheduledId()!=null){
	        	//取消预约
	        	ScheduleManage scheduleManage = new ScheduleManage();
	        	scheduleManage.setId(item.getScheduledId());
	        	scheduleManage.setStatus(4);
	            logger.info("超时15分钟同时取消预约：" + scheduleManage);
	            scheduleManageMapper.updateSchedule(scheduleManage);
	    	}			
		}
    }
	@Override
	public Integer countOrderTimes(Integer customerId) {
		return orderManageMapper.countOrderTimes(customerId);
	}

}
