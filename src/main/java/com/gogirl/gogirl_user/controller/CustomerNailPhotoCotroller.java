package com.gogirl.gogirl_user.controller;

import java.net.InetAddress;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gogirl.gogirl_order.order_commons.utils.JsonResult;
import com.gogirl.gogirl_user.entity.Coupon;
import com.gogirl.gogirl_user.entity.CustomerNailPhoto;
import com.gogirl.gogirl_user.service.CountService;
import com.gogirl.gogirl_user.service.CustomerNailPhotoService;

@ResponseBody
@Controller
@RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value="/photo")
public class CustomerNailPhotoCotroller {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Resource
	private CustomerNailPhotoService customerNailPhotoService;

	@RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value="/selectByCustomerNailPhoto")
	public JsonResult selectByCustomerNailPhoto(CustomerNailPhoto record,Integer pageNum,Integer pageSize) {
		if(pageNum==null){
			return new JsonResult(false,String.format(JsonResult.PARAM_NULL, "pageNum"),null);
		}
		if(pageSize==null){
			return new JsonResult(false,String.format(JsonResult.PARAM_NULL, "pageSize"),null);
		}
		logger.info("查询客照列表:"+record.toString());
        PageHelper.startPage(pageNum,pageSize);
		List<CustomerNailPhoto> list = customerNailPhotoService.selectByCustomerNailPhoto(record);
		PageInfo<CustomerNailPhoto> pageInfo = new PageInfo<CustomerNailPhoto>(list);
		return new JsonResult(true, JsonResult.APP_DEFINE_SUC, pageInfo);
	}
	@RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value="/insertOrUpdateSelective")
	public JsonResult insertOrUpdateSelective(CustomerNailPhoto record){
		CustomerNailPhoto isExist = new CustomerNailPhoto();
		isExist.setServiceId(record.getServiceId());
		List<CustomerNailPhoto> list = customerNailPhotoService.selectByCustomerNailPhoto(isExist);
		int row =  0;
		if(list.size()>0){
			for(int j=0;j<list.size();j++){
				record.setId(list.get(j).getId());
				row =  customerNailPhotoService.updateByPrimaryKeySelective(record);
			}
		}else{
			row =  customerNailPhotoService.insertSelective(record);
		}
		return new JsonResult(true, JsonResult.APP_DEFINE_SUC, row);
	}
//	@RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value="/selectByPrimaryKey")
//	public JsonResult selectByPrimaryKey(Integer id) {
//		CustomerNailPhoto record = customerNailPhotoService.selectByPrimaryKey(id);
//		return new JsonResult(true, JsonResult.APP_DEFINE_SUC, record);
//	}
//	
//	@RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value="/insertSelective")
//	public JsonResult insertSelective(CustomerNailPhoto record){
//		int row =  customerNailPhotoService.insertSelective(record);
//		return new JsonResult(true, JsonResult.APP_DEFINE_SUC, row);
//	}
//	@RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value="/updateByPrimaryKeySelective")
//	public JsonResult updateByPrimaryKeySelective(CustomerNailPhoto record){
//		if(record.getId()==null){
//			return new JsonResult(false,String.format(JsonResult.PARAM_NULL,"id"),null);
//		}
//		int row =   customerNailPhotoService.updateByPrimaryKeySelective(record);
//		return new JsonResult(true, JsonResult.APP_DEFINE_SUC, row);
//	}
	@RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value="/delete")
	public JsonResult delete(CustomerNailPhoto record){
		if(record==null){
			return new JsonResult(false,String.format(JsonResult.PARAM_NULL,"所有参数"),null);
		}
		CustomerNailPhoto q = new CustomerNailPhoto();
		q.setServiceId(record.getServiceId());
		List<CustomerNailPhoto> list = customerNailPhotoService.selectByCustomerNailPhoto(q);
		int row =  0;
		for(int i=0;i<list.size();i++){
			customerNailPhotoService.deleteByPrimaryKey(list.get(i).getId());
			row++;
		}
		return new JsonResult(true, JsonResult.APP_DEFINE_SUC, row);
	}
}
