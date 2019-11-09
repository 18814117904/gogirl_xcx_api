package com.gogirl.gogirl_user.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;




import com.gogirl.gogirl_order.order_commons.utils.JsonResult;
import com.gogirl.gogirl_user.entity.CustomerReceiveInfo;
import com.gogirl.gogirl_user.entity.CustomerWeibo;
import com.gogirl.gogirl_user.service.CustomerReceiveInfoService;
import com.gogirl.gogirl_user.service.CustomerWeiboService;

@Controller
public class ReceiveInfoController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Resource
	CustomerReceiveInfoService customerReceiveInfoService;
	
	@ResponseBody
	@RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value="/insertOrUpdateReceiveInfo")
    public JsonResult insertOrUpdateSelective(CustomerReceiveInfo record){
    	if(record==null||record.getCustomerId()==null){
    		return new JsonResult(false,String.format(JsonResult.PARAM_NULL,"customerId"),null);
    	}
    	CustomerReceiveInfo customerReceiveInfo = customerReceiveInfoService.selectByPrimaryKey(record.getCustomerId());
    	if(customerReceiveInfo!=null){//更新
        	int row = customerReceiveInfoService.updateByPrimaryKeySelective(record);
        	return new JsonResult(true,JsonResult.APP_DEFINE_SUC,row);
    	}else{//插入
    		int row = customerReceiveInfoService.insertSelective(record);
        	return new JsonResult(true,JsonResult.APP_DEFINE_SUC,row);
    	}
    }

	@ResponseBody
	@RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value="/selectReceiveInfo")
    public JsonResult selectByPrimaryKey(Integer customerId){
    	if(customerId==null){
    		return new JsonResult(false,String.format(JsonResult.PARAM_NULL,"customerId"),null);
    	}
    	return new JsonResult(true,JsonResult.APP_DEFINE_SUC,customerReceiveInfoService.selectByPrimaryKey(customerId));
    }

}
