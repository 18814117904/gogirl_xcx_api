package com.gogirl.gogirl_xcx.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.qcloudsms.SmsSingleSenderResult;
import com.gogirl.gogirl_order.order_commons.utils.JsonResult;
import com.gogirl.gogirl_store.store_commons.jms.SmsService;
import com.gogirl.gogirl_technician.technician_commons.dto.UserTechnician;
import com.gogirl.gogirl_technician.technician_user.service.TechnicianManageService;
import com.gogirl.gogirl_technician.technician_user.service.UserTechnicianService;
//import com.gogirl.gogirl_store_old.store_commons.jms.SmsService;
import com.gogirl.gogirl_user.entity.Customer;
import com.gogirl.gogirl_user.service.CustomerService;
import com.gogirl.gogirl_xcx.entity.GogirlToken;
import com.gogirl.gogirl_xcx.service.GogirlTokenService;
//import com.gogirl.gogirl_xcx.service.myhttp.MyHttpPost;
import com.gogirl.gogirl_xcx.util.CheckUtil;

@Controller
@RequestMapping("/phone")
@Api(tags={"2.手机号码相关接口"},value="手机号码相关接口")
public class PhoneLoginController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Resource
	SmsService smsService;
	
	@Value("${spring.jackson.date-format}")
	String dateformat;
	@Resource
	CustomerService customerService;
	@Resource
	GogirlTokenService gogirlTokenService;
	@Resource
	UserTechnicianService userTechnicianService;
	//用户端获取验证码
	@ResponseBody
	@ApiOperation(value = "获取验证码，用于绑定手机号码")
	@RequestMapping(method={RequestMethod.POST},value="/getBindCode")
	public JsonResult getBindCode(@RequestParam String phone,String token) {
		if(token==null||token.isEmpty()){
			return new JsonResult(false,JsonResult.TOKEN_NULL_CODE,"入参token为空");
		}
		if(!CheckUtil.isPhone(phone)){
			logger.info("用户号码格式不正确");
			return new JsonResult(false,"用户号码格式不正确",null);
		}
		GogirlToken gt = gogirlTokenService.getTokenByToken(token);
		if(gt==null){
			return new JsonResult(false,JsonResult.TOKEN_NULL_CODE,"token过期");
		}
		String code=getNewCode();
		SmsSingleSenderResult result = smsService.sendBindSmsCode(phone, code);
		if(result!=null){
			//验证码保存到TOKEN
			gt.setCode(code);
			gt.setPhone(phone);
			gogirlTokenService.updateByPrimaryKeySelective(gt);
			return new JsonResult(true, JsonResult.APP_DEFINE_SUC, null);
		}else{
			return new JsonResult(false, "发送验证码失败，请重试", null);
		}
	}
	public String getNewCode(){
		return String.valueOf((int)((Math.random()*9+1)*100000));
	}

	//用户小程序绑定号码
	@ResponseBody
	@ApiOperation(value = "小程序用户根据验证码绑定手机号码和称呼",notes="生日的格式是:yyyy-MM-dd")
	@RequestMapping(method={RequestMethod.POST},value="/bindPhoneAndName")
	public JsonResult bindPhoneAndCode(String token,String phone,String code,String realName,Integer customerSource,@DateTimeFormat(pattern="yyyy-MM-dd")Date birthday,String sex,HttpServletResponse response) {
		if(code==null||code.isEmpty()){
			return new JsonResult(false,"",null);
		}
		GogirlToken gogirlToken = gogirlTokenService.getTokenByToken(token);
		if(gogirlToken==null){
			return new JsonResult(false,"token:"+token+".找不到token信息",null);
		}
//		if(gogirlToken.getCode()==null){
//			return new JsonResult(false,"请先获取验证码",code);
//		}
//		if(!gogirlToken.getCode().trim().equals(code.trim())){
//			return new JsonResult(false,"验证码不正确",null);
//		}
		Customer customer = gogirlToken.getCustomer();
		Customer phoneCustomer = customerService.selectByPhone(phone);
		if(phoneCustomer!=null){//合并手机用户
			Customer updatecustomer = new Customer();
			updatecustomer.setId(phoneCustomer.getId());
			updatecustomer.setRealName(realName);
			updatecustomer.setBirthday(birthday);
			updatecustomer.setCustomerSource(customerSource);
			updatecustomer.setSex(sex);;
			if(customer.getUnionid()!=null){
				updatecustomer.setUnionid(customer.getUnionid());
			}
			updatecustomer.setOpenid1(customer.getOpenid1());
			int i = customerService.updateByPrimaryKeySelective(updatecustomer);
			if(i>0){
				gogirlToken.setCustomerId(updatecustomer.getId());
				gogirlTokenService.updateByPrimaryKeySelective(gogirlToken);
				customerService.deleteByPrimaryKey(customer.getId());
			}
			return new JsonResult(true, JsonResult.APP_DEFINE_SUC, phoneCustomer);
		}else{//直接绑定号码
			Customer updatecustomer = new Customer();
			updatecustomer.setId(customer.getId());
			updatecustomer.setPhone(phone);			
			updatecustomer.setRealName(realName);
			updatecustomer.setBirthday(birthday);
			updatecustomer.setSex(sex);
			customerService.updateByPrimaryKeySelective(updatecustomer);
			customer.setPhone(phone);
			return new JsonResult(true, JsonResult.APP_DEFINE_SUC, customer);
		}
	}
	//美甲师小程序登录
	@ResponseBody
	@ApiOperation(value = "美甲师根据验证码绑定手机号码")
	@RequestMapping(method={RequestMethod.POST},value="/bindPhone_t")
	public JsonResult bindPhone_t(HttpServletRequest request,String token,String phone,String code,HttpServletResponse response) {
		if(code==null||code.isEmpty()){
			return new JsonResult(false,"",null);
		}
		if(code==null||code.isEmpty()){
			return new JsonResult(false,"",null);
		}
		GogirlToken gogirlToken = gogirlTokenService.getTokenByToken(token);
		if(gogirlToken==null){
			return new JsonResult(false,"token:"+token+".找不到token信息",null);
		}
		if(!phone.equals("18888888888")){//微信审核人员使用
			if(gogirlToken.getPhone()==null&&(gogirlToken.getCode()==null)){
				return new JsonResult(false,"请先获取验证码",code);
			}
			if(!gogirlToken.getPhone().trim().equals(phone.trim())){
				return new JsonResult(false,"手机号码改变后，需要重新发送验证码",null);
			}
		}
//		if(!gogirlToken.getCode().trim().equals(code.trim())){
//			return new JsonResult(false,"验证码不正确",null);
//		}
		UserTechnician technicianManage = new UserTechnician();
		technicianManage.setMobile(phone);
		List<UserTechnician> list = userTechnicianService.listTechnicianForPage(technicianManage);
		if(list!=null&&list.size()==1){//如果只有一个用户，设置登录
			gogirlToken.setCustomerId(list.get(0).getId());
			gogirlTokenService.updateByPrimaryKeySelective(gogirlToken);
		}
		for(int i=0;i<list.size();i++){//记录openid到该电话号码下的用户
			UserTechnician item = list.get(i);
			item.setOpenid(gogirlToken.getOpenid());
			userTechnicianService.updateTechnicianByAuthorityId(item);
		}
		Map<String, Object> map = new HashMap<>();
		map.put("list", list);
		return new JsonResult(true, JsonResult.APP_DEFINE_SUC, list);
	}
}
