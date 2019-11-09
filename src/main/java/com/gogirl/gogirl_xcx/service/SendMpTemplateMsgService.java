package com.gogirl.gogirl_xcx.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.gogirl.gogirl_order.order_commons.utils.JsonResult;
import com.gogirl.gogirl_user.constant.GogirlProperties;
import com.gogirl.gogirl_user.service.myhttp.MyHttpGet;
import com.gogirl.gogirl_user.service.myhttp.MyHttpPost;
import com.gogirl.gogirl_xcx.dao.XcxFormIdMapper;
import com.gogirl.gogirl_xcx.entity.XcxFormId;

@Service
public class SendMpTemplateMsgService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Resource
    private MyHttpPost myHttpPost;
	@Resource
    private MyHttpGet myHttpGet;
	@Resource
	GogirlProperties gogirlProperties;
	@Resource
	XcxFormIdMapper xcxFormIdMapper;
	
	/**
	 * 充值成功
	 */
	public Boolean sendChargeMsg(String openid,String time,String customerName,String type,String chargeDetail,String accountDetail) {
		Map<String, Object> map = new HashMap<>();
		map.put("openid", openid);
		map.put("time", time);
		map.put("customerName", customerName);
		map.put("type", type);
		map.put("chargeDetail", chargeDetail);
		map.put("accountDetail", accountDetail);
		logger.info(map.toString());
		try{
			myHttpPost.httpRequest(gogirlProperties.getGOGIRLMP()+"/gogirl_mp/wx/template/chargeRemindMsg", map);
		}catch(Exception e){
			logger.info(e.getMessage());
		}
		return true;
	}	
	/**
	 * 预约到期提醒
	 */
	public Boolean scheduledRemindMsg(String openid,String serveName,String time,String storeAddress,String remark) {
		
		Map<String, Object> map = new HashMap<>();
		map.put("openid", openid);
		map.put("serveName", serveName);
		map.put("time", time);
		map.put("storeAddress", storeAddress);
		map.put("remark", remark);
		logger.info(map.toString());
		try{
			myHttpPost.httpRequest(gogirlProperties.getGOGIRLMP()+"/gogirl_mp/wx/template/scheduledRemindMsg", map);
		}catch(Exception e){
			logger.info(e.getMessage());
		}
		return true;
	}	
//	/**
//	 * 预约
//	 */
//	public Boolean sendScheduledMsg(String openid,Integer orderId,String time,String technicianName,String serveName,String storeName,String storePhone,String storeAddress) {
//		//获取且删除formId,
//		XcxFormId xcxFormId =xcxFormIdMapper.selectFormIdByOpenid(openid,new Date(new Date().getTime()-new Long("604000000")));
//		if(xcxFormId!=null){
//			xcxFormIdMapper.deleteByPrimaryKey(xcxFormId.getId());
//		}else{
//			logger.info("没有formId,发不了预约提醒");
//			return true;
//		}
//		//pages/mine/order-details
//		Map<String, Object> map = new HashMap<>();
//		map.put("openid", openid);
//		map.put("url", "pages/mine/order-details?orderId="+orderId);
//		map.put("time", time);
//		map.put("technicianName", technicianName);
//		map.put("serveName", serveName);
//		map.put("storeName", storeName);
//		map.put("storePhone", storePhone);
//		map.put("storeAddress", storeAddress);
//		map.put("formId", xcxFormId.getFormId());
//		logger.info(map.toString());
//		try{
//			myHttpGet.httpRequest(gogirlProperties.getGOGIRLMP()+"/gogirl_mp/wx/ma/sendScheduledMsg", map);
//		}catch(Exception e){
//			logger.info(e.getMessage());
//		}
//		return true;
//	}	
//	/**
//	 * 预约
//	 */
//	public Boolean sendBeBookedMsg(String openid2,Integer orderId,String time,String storeName,String technicianName,String serveName,String customerName,String customerPhone) {
//		//获取且删除formId,
//		XcxFormId xcxFormId =xcxFormIdMapper.selectFormIdByOpenid(openid2,new Date(new Date().getTime()-new Long("604000000")));
//		if(xcxFormId!=null){
//			xcxFormIdMapper.deleteByPrimaryKey(xcxFormId.getId());
//			String url = "pages/order/order-details?orderId="+orderId;
//			String formId = xcxFormId.getFormId();
//			try {
//				wxMaController.sendBeBookedMsg(openid2, url, time, storeName, technicianName, serveName, customerName, customerPhone, formId);
//			} catch (Exception e) {
//				logger.error(e.getMessage());
//			}
//		}else{
//			logger.info("没有formId,发不了被预约提醒");
//			return true;
//		}
//		return true;
//	}	
//	/**
//	 * 待支付
//	 */
//	public Boolean sendWaitForPayMsg(String openid,Integer orderId,String orderNo,String serveName,String amount) {
//		//获取且删除formId,
//		XcxFormId xcxFormId =xcxFormIdMapper.selectFormIdByOpenid(openid,new Date(new Date().getTime()-new Long("604000000")));
//		if(xcxFormId!=null){
//			xcxFormIdMapper.deleteByPrimaryKey(xcxFormId.getId());
//		}else{
//			logger.info("没有formId,发不了待支付提醒");
//			return true;
//		}
//		
//		Map<String, Object> map = new HashMap<>();
//		map.put("openid", openid);
//		map.put("orderNo", orderNo);
//		map.put("serveName", serveName);
//		map.put("amount", amount);
//		map.put("formId", xcxFormId.getFormId());
//		logger.info(map.toString());
//		try{
//			myHttpPost.httpRequest(gogirlProperties.getGOGIRLMP()+"/gogirl_mp/wx/ma/sendWaitForPayMsg", map);
//		}catch(Exception e){
//			logger.info(e.getMessage());
//		}
//		return true;
//	}	
//	/**
//	 * 支付成功
//	 */
//	public Boolean sendPaySuccessMsg(String openid,Integer orderId,String orderNo,String serveName,String amount,String payType,String balance,String couponName,String storeName) {
//		//获取且删除formId,
//		XcxFormId xcxFormId =xcxFormIdMapper.selectFormIdByOpenid(openid,new Date(new Date().getTime()-new Long("604000000")));
//		Map<String, Object> map = new HashMap<>();
//		if(xcxFormId!=null){
//			map.put("formId", xcxFormId.getFormId());
//			xcxFormIdMapper.deleteByPrimaryKey(xcxFormId.getId());
//			map.put("openid", openid);
//			map.put("orderNo", orderNo);
//			map.put("serveName", serveName);
//			map.put("amount", amount);
//			map.put("payType", payType);
//			map.put("balance", balance);
//			map.put("couponName", couponName);
//			map.put("storeName", storeName);
//			logger.info(map.toString());
//			try{
//				myHttpPost.httpRequest(gogirlProperties.getGOGIRLMP()+"/gogirl_mp/wx/ma/sendPaySuccessMsg", map);
//			}catch(Exception e){
//				logger.info(e.getMessage());
//			}
//		}else{
//			logger.info("没有formId,发不了支付成功提醒");
//			return true;
//		}
//		
//		return true;
//	}	


}
