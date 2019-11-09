package com.gogirl.gogirl_order.order_manage.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gogirl.gogirl_order.order_commons.dto.OrderManage;
import com.gogirl.gogirl_order.order_commons.dto.OrderRecord;
import com.gogirl.gogirl_order.order_commons.dto.OrderServe;
import com.gogirl.gogirl_order.order_commons.utils.JsonResult;
import com.gogirl.gogirl_order.order_manage.dao.OrderServeMapper;
import com.gogirl.gogirl_order.order_manage.service.OrderManageService;
import com.gogirl.gogirl_order.order_manage.service.OrderRecordService;
import com.gogirl.gogirl_user.dao.CustomerDetailMapper;
import com.gogirl.gogirl_user.entity.CustomerDetail;
import com.mysql.fabric.xmlrpc.base.Array;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yinyong on 2018/12/3.
 */

@RestController
@RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "orderrecord")
public class OrderRecordController {

    private final static Logger logger = LoggerFactory.getLogger(OrderRecordController.class);

    @Autowired
    private OrderRecordService orderRecordService;

    @Autowired
    private OrderManageService orderManageService;

    @Autowired
    private OrderServeMapper orderServeMapper;
    @Autowired
    private CustomerDetailMapper customerDetailMapper;

    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "tempMethod")
    public JsonResult tempMethod(String picUrl){//处理表格客照,当客照数据正常后,可以删除该方法
    	if(picUrl!=null){
    		temp(picUrl);
    	}else{
    		List<String> list = new ArrayList<String>();
    		list.add("Fnc4h5SlUgD8AJ85N6rY1aMS9pUd");

    		for(int i =0;i<list.size();i++){
    			temp(list.get(i));
    		}
    	}
        return new JsonResult<>(true,"ok",null);
    }
    
    public void temp(String picUrl) {//处理表格客照,当客照数据正常后,可以删除该方法
    	OrderRecord orderRecord = new OrderRecord();
    	orderRecord.setPicturePath(picUrl);
    	
        List<OrderRecord> lists = orderRecordService.listOrderRecord(orderRecord);
        for(int i=0;i<lists.size();i++){
        	OrderRecord item = lists.get(i);
        	logger.info(item.toString());
        	OrderServe orderServe = orderServeMapper.getOrderServeDetail(item.getOrderServeId());
        	if(orderServe!=null){
        		OrderManage orderManage = orderManageService.getOrderForDetail(orderServe.getOrderId());
        		if(orderManage!=null){
        			CustomerDetail customerDetail = customerDetailMapper.selectByPrimaryKey(orderManage.getOrderUser());
        			if(customerDetail==null){
        				logger.info("插入用户详情id:"+orderManage.getOrderUser());
        				customerDetail = new CustomerDetail();
        				customerDetail.setCustomerId(orderManage.getOrderUser());
        				customerDetail.setQuestionnaireImgUrl(picUrl);
        				customerDetailMapper.insertSelective(customerDetail);
        			}else{
        				logger.info("修改用户详情id"+orderManage.getOrderUser());
        				CustomerDetail cd2 = new CustomerDetail();
        				cd2.setCustomerId(customerDetail.getCustomerId());
        				String questionnaireImgUrl = customerDetail.getQuestionnaireImgUrl();
        				if(questionnaireImgUrl==null){
        					questionnaireImgUrl=picUrl;
        					cd2.setQuestionnaireImgUrl(questionnaireImgUrl);
        					customerDetailMapper.updateByPrimaryKeySelective(cd2);
        				}else if(questionnaireImgUrl.indexOf(picUrl)>=0){//该客照已经存在,无需操作
        					logger.info("该客照已经存在,无需操作");
        				}else{
        					cd2.setQuestionnaireImgUrl(questionnaireImgUrl+","+picUrl);
        					customerDetailMapper.updateByPrimaryKeySelective(cd2);
        				}
        			}
        		}else{
        			logger.info("orderId"+orderServe.getOrderId()+"找不到订单");
        		}
        	}else{
        		logger.info("serveId"+item.getOrderServeId()+"找不到服务");
        	}
        	String picturePath = item.getPicturePath();
        	picturePath = picturePath.replaceAll(picUrl, "");
        	picturePath.replaceAll(",,", ",");
        	if(picturePath.startsWith(",")){
        		picturePath = picturePath.substring(1);
        	}
        	if(picturePath.endsWith(",")){
        		picturePath = picturePath.substring(0,picturePath.length()-1);
        	}
        	if(picturePath.isEmpty()){
        		picturePath = "无";
        	}
        	orderRecord.setOrderServeId(item.getOrderServeId());
        	orderRecord.setPicturePath(picturePath);
        	orderRecordService.updateOrderRecord(orderRecord);
        }
	}
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "queryOrderRecordForPage")
    public JsonResult queryOrderRecordForPage(OrderRecord orderRecord, HttpServletRequest request, HttpServletResponse response){
        JsonResult jsonResul = new JsonResult();
        String pageNum = request.getParameter("pageNum");
        String pageSize = request.getParameter("pageSize");
        PageHelper.startPage(Integer.parseInt(pageNum),Integer.parseInt(pageSize));
        List<OrderRecord> lists = orderRecordService.listOrderRecord(orderRecord);
        PageInfo<OrderRecord> pageInfo = new PageInfo<OrderRecord>(lists);
        jsonResul.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC).setData(pageInfo);
        return jsonResul;
    }

    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "queryOrderDetail")
    public JsonResult queryOrderDetail(Integer orderServeId, HttpServletRequest request, HttpServletResponse response){
        JsonResult jsonResult = new JsonResult();
        OrderRecord orderRecord = orderRecordService.getOrderRecord(orderServeId);
        if(orderRecord == null){
            orderRecord = new OrderRecord();
        }
        jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC).setData(orderRecord);
        return jsonResult;
    }

    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "modifyOrderRecord")
    public JsonResult modifyOrderRecord(OrderRecord orderRecord, HttpServletRequest request, HttpServletResponse response){
        JsonResult jsonResult = new JsonResult();
        int result = orderRecordService.updateOrderRecord(orderRecord);
        if(result >0){
            jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC);
        }
        return jsonResult;
    }

    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "addOrUpdateOrderRecord")
    public JsonResult addOrderRecord(OrderRecord orderRecord, HttpServletRequest request, HttpServletResponse response){
        JsonResult jsonResult = new JsonResult();
        OrderRecord checkOrderRecord = orderRecordService.getOrderRecord(orderRecord.getOrderServeId());
        int result = 0;
        if(checkOrderRecord != null){
            result = orderRecordService.updateOrderRecord(orderRecord);
        }else{
            result = orderRecordService.insertOrderRecord(orderRecord);
        }
        List<OrderRecord> listOrderRecord = orderRecordService.listOrderRecordByOrderId(orderRecord);

        double dataIntegrity = 0.0;

        for(OrderRecord orderRecord1 : listOrderRecord) {
            if(StringUtils.isNotBlank(orderRecord1.getFixHour())) {
                dataIntegrity = dataIntegrity + 10;
            }
            if(orderRecord1.getIsTimeOut() != null) {
                dataIntegrity = dataIntegrity + 10;
            }
            if(StringUtils.isNotBlank(orderRecord1.getLinkCause())) {
                dataIntegrity = dataIntegrity + 10;
            }
            if(StringUtils.isNotBlank(orderRecord1.getUserFeedback())) {
                dataIntegrity = dataIntegrity + 10;
            }
            if(StringUtils.isNotBlank(orderRecord1.getTechnicianFeedback())) {
                dataIntegrity = dataIntegrity + 10;
            }
            if(StringUtils.isNotBlank(orderRecord1.getExistingProblems())) {
                dataIntegrity = dataIntegrity + 10;
            }
            if(StringUtils.isNotBlank(orderRecord1.getSolution())) {
                dataIntegrity = dataIntegrity + 10;
            }
            if(StringUtils.isNotBlank(orderRecord1.getAfterSaleFeedback())) {
                dataIntegrity = dataIntegrity + 10;
            }
            if(orderRecord1.getIsGuestPhoto() == 1) {
                dataIntegrity = dataIntegrity + 20;
            }
        }

        if(listOrderRecord != null) {
            List<OrderServe> serveList = orderServeMapper.listOrderServe(orderRecord.getOrderId());
            dataIntegrity = dataIntegrity/serveList.size();
            OrderManage orderManage = new OrderManage();
            orderManage.setId(orderRecord.getOrderId());
            orderManage.setDataIntegrity(dataIntegrity);
            orderManageService.updateOrderDataIntegrity(orderManage);
        }

        if(result > 0){
            jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC);
        }
        return jsonResult;
    }

}