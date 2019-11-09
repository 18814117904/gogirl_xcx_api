package com.gogirl.gogirl_xcx.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gogirl.gogirl_order.order_commons.utils.JsonResult;
import com.gogirl.gogirl_service.dao.MessageMapper;
import com.gogirl.gogirl_service.entity.Message;
import com.gogirl.gogirl_technician.technician_user.service.TechnicianManageService;
import com.gogirl.gogirl_xcx.dao.VisitsLogMapper;
import com.gogirl.gogirl_xcx.entity.GogirlToken;
import com.gogirl.gogirl_xcx.entity.VisitsLog;
import com.gogirl.gogirl_xcx.service.GogirlTokenService;

@RestController
@RequestMapping("/message")
@Api(tags={"10.美甲师消息和用户登入登出记录"},value="美甲师消息和用户登入登出记录")
public class MessageController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Resource
	MessageMapper messageService;
	@Resource
	GogirlTokenService gogirlTokenService;
	@Resource
	TechnicianManageService technicianManageService;
	@Resource
	VisitsLogMapper visitsLogMapper;
	//消息接口使用店铺的
	//用户端获取验证码,
	@ResponseBody
	@ApiOperation(value = "获取我的消息")
	@RequestMapping(method={RequestMethod.POST},value="/selectMyMessage")
	public JsonResult<?> selectMyMessage(String token,Integer pageSize,Integer pageNum) {
		if(token==null||token.isEmpty()){
			return new JsonResult<Object>(false,JsonResult.TOKEN_NULL_CODE,"入参token为空");
		}
		GogirlToken gt = gogirlTokenService.getTokenByToken_t(token);
		if(gt==null&&gt.getUserTechnician()!=null){
			return new JsonResult<Object>(false,JsonResult.NO_DEPARTMENT_CODE,"token过期");
		}
		if(pageSize!=null&&pageNum!=null){
			PageHelper.startPage(pageNum,pageSize);
		}
		List<Message> list = messageService.selectMyMessage(gt.getUserTechnician().getTechnicianId());
        PageInfo<Message> pageInfo = new PageInfo<Message>(list);
		return new JsonResult<>(true,JsonResult.APP_DEFINE_SUC,pageInfo);
	}
	@ResponseBody
	@ApiOperation(value = "小程序登入记录")
	@RequestMapping(method={RequestMethod.POST},value="/loginLog")
	public JsonResult<?> loginLog(String token,String pageUrl) {
		//pageUrl参数对照：{app：1}待加
		if(token==null||token.isEmpty()){
			return new JsonResult<Object>(false,JsonResult.TOKEN_NULL_CODE,"入参token为空");
		}
		GogirlToken gt = gogirlTokenService.getTokenByToken(token);
		if(gt==null){
			return new JsonResult<Object>(false,JsonResult.NO_DEPARTMENT_CODE,"token过期");
		}
		VisitsLog visitsLog = new VisitsLog();
		if(gt.getCustomerId()!=null){
			visitsLog.setCustomerId(gt.getCustomerId());
		}
		visitsLog.setTime(new Date());
		visitsLog.setDuration(0);
		visitsLog.setType(1);
		
		visitsLogMapper.insertSelective(visitsLog);
		return new JsonResult<>(true,JsonResult.APP_DEFINE_SUC,visitsLog.getId());
	}
	@ResponseBody
	@ApiOperation(value = "小程序登出记录")
	@RequestMapping(method={RequestMethod.POST},value="/logoutLog")
	public JsonResult<?> logoutLog(String token,Integer visitsId) {
		if(token==null||token.isEmpty()){
			return new JsonResult<Object>(false,JsonResult.TOKEN_NULL_CODE,"入参token为空");
		}
		GogirlToken gt = gogirlTokenService.getTokenByToken(token);
		if(gt==null){
			return new JsonResult<Object>(false,JsonResult.NO_DEPARTMENT_CODE,"token过期");
		}
		VisitsLog visitsLog = visitsLogMapper.selectByPrimaryKey(visitsId);
		if(visitsLog==null){
			return new JsonResult<>(true,"登出记录失败",null);
		}
		long duration= new Date().getTime()-visitsLog.getTime().getTime();
		VisitsLog updateLog = new VisitsLog();
		updateLog.setId(visitsId);
		updateLog.setDuration(Integer.valueOf(String.valueOf(duration/1000)));
		visitsLogMapper.updateByPrimaryKeySelective(updateLog);
		return new JsonResult<>(true,JsonResult.APP_DEFINE_SUC,null);
	}
	@ResponseBody
	@ApiOperation(value = "记录该次访问的活动")
	@RequestMapping(method={RequestMethod.POST},value="/recordActivityId")
	public JsonResult<?> recordActivityId(String token,Integer visitsId,Integer activityId) {
		if(token==null||token.isEmpty()){
			return new JsonResult<Object>(false,JsonResult.TOKEN_NULL_CODE,"入参token为空");
		}
		
		GogirlToken gt = gogirlTokenService.getTokenByToken(token);
		if(gt==null){
			return new JsonResult<Object>(false,JsonResult.NO_DEPARTMENT_CODE,"token过期");
		}
		VisitsLog visitsLog = visitsLogMapper.selectByPrimaryKey(visitsId);
		if(visitsLog==null){
			return new JsonResult<>(true,"记录访问活动失败,找不到visitsLog:"+visitsId,null);
		}
		VisitsLog updateLog = new VisitsLog();
		updateLog.setId(visitsId);
		updateLog.setType(2);
		updateLog.setActivityId(activityId);
		
		visitsLogMapper.updateByPrimaryKeySelective(updateLog);
		return new JsonResult<>(true,JsonResult.APP_DEFINE_SUC,null);
	}
	
	@ResponseBody
	@ApiOperation(value = "页面登入记录",notes="参数直接传过来")
	@RequestMapping(method={RequestMethod.POST},value="/pageIn")
	public JsonResult<?> pageIn(String token,Integer type,Integer activityId,Integer shareType,Integer shareUserId,Integer serviceId,Integer departmentId,String pageName) {
		//pageUrl参数对照：{app：1}待加
		if(token==null||token.isEmpty()){
			return new JsonResult<Object>(false,JsonResult.TOKEN_NULL_CODE,"入参token为空");
		}
		GogirlToken gt = gogirlTokenService.getTokenByToken(token);
		if(gt==null){
			return new JsonResult<Object>(false,JsonResult.NO_DEPARTMENT_CODE,"token过期");
		}
		VisitsLog visitsLog = new VisitsLog();
		if(gt.getCustomerId()!=null){
			visitsLog.setCustomerId(gt.getCustomerId());
		}
		visitsLog.setTime(new Date());
		visitsLog.setDuration(0);
		
		visitsLog.setActivityId(activityId);
		visitsLog.setShareType(shareType);
		visitsLog.setShareUserId(shareUserId);
		visitsLog.setServiceId(serviceId);
		visitsLog.setPageName(pageName);
		
		if(type!=null){//type有传的话,就直接记录:1.打开首页;4客服推送;
			visitsLog.setType(type);
		}else if(activityId!=null){//有活动id记录为活动扫码进入
			visitsLog.setType(2);
		}else if(departmentId!=null){//有店铺id,记录为门店扫码进入
			visitsLog.setShareType(3);
		}else if(shareUserId!=null){//有分享者的就记录为分享进入
			visitsLog.setShareType(5);
		}else{//啥也没有的,记录为访问内页
			visitsLog.setType(6);
		}
		visitsLogMapper.insertSelective(visitsLog);
		return new JsonResult<>(true,JsonResult.APP_DEFINE_SUC,visitsLog.getId());
	}
	@ResponseBody
	@ApiOperation(value = "页面登出记录")
	@RequestMapping(method={RequestMethod.POST},value="/pageOut")
	public JsonResult<?> pageOut(String token,Integer visitsId) {
		if(token==null||token.isEmpty()){
			return new JsonResult<Object>(false,JsonResult.TOKEN_NULL_CODE,"入参token为空");
		}
		GogirlToken gt = gogirlTokenService.getTokenByToken(token);
		if(gt==null){
			return new JsonResult<Object>(false,JsonResult.NO_DEPARTMENT_CODE,"token过期");
		}
		VisitsLog visitsLog = visitsLogMapper.selectByPrimaryKey(visitsId);
		if(visitsLog==null){
			return new JsonResult<>(true,"登出记录失败",null);
		}
		long duration= new Date().getTime()-visitsLog.getTime().getTime();
		VisitsLog updateLog = new VisitsLog();
		updateLog.setId(visitsId);
		updateLog.setDuration(Integer.valueOf(String.valueOf(duration/1000)));
		visitsLogMapper.updateByPrimaryKeySelective(updateLog);
		return new JsonResult<>(true,JsonResult.APP_DEFINE_SUC,null);
	}
}
