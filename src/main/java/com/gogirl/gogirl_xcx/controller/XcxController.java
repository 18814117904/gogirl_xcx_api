package com.gogirl.gogirl_xcx.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.gogirl.gogirl_order.order_commons.dto.OrderManage;
import com.gogirl.gogirl_order.order_commons.utils.JsonResult;
import com.gogirl.gogirl_order.order_manage.service.OrderManageService;
import com.gogirl.gogirl_store.store_user.service.UserManageService;
import com.gogirl.gogirl_technician.technician_commons.dto.UserTechnician;
import com.gogirl.gogirl_technician.technician_user.service.UserTechnicianService;
import com.gogirl.gogirl_user.constant.GogirlProperties;
import com.gogirl.gogirl_user.entity.Customer;
import com.gogirl.gogirl_user.service.CustomerService;
import com.gogirl.gogirl_user.util.Openid1Lock;
import com.gogirl.gogirl_xcx.config.WxConfig;
import com.gogirl.gogirl_xcx.dao.XcxFormIdMapper;
import com.gogirl.gogirl_xcx.entity.GogirlToken;
import com.gogirl.gogirl_xcx.entity.XcxFormId;
import com.gogirl.gogirl_xcx.service.GogirlTokenService;
import com.gogirl.gogirl_xcx.service.QRCodeService;
import com.gogirl.gogirl_xcx.service.WechatService;
import com.gogirl.gogirl_xcx.util.ImageUtil;
import com.gogirl.gogirl_xcx.util.WXCore;

@RestController
@RequestMapping("/xcx")
@Api(tags = { "1.小程和用户相关接口" },value = "小程序相关接口")
public class XcxController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Resource
	WechatService wechatService;
	// @Resource
	// LoginInfoService loginInfoService;
	@Resource
	CustomerService customerService;
	@Resource
	GogirlTokenService gogirlTokenService;
	@Autowired
	UserManageService storeUserService;
	@Resource
	UserTechnicianService userTechnicianService;
	@Autowired
	WxConfig wxConfig;
	@Resource
	GogirlProperties gogirlProperties;
	@Resource
	XcxFormIdMapper xcxFormIdMapper;
	@Resource
	OrderManageService orderManageService;
	@Resource
	QRCodeService qrCodeService;
	
	Openid1Lock openid1Lock = Openid1Lock.getInsatance();

	// 用户端小程序登录login
	@ApiOperation(value = "客户根据code获取token")
	@RequestMapping(method = { RequestMethod.GET}, value = "/login")
	public JsonResult<String> login(
			@RequestParam(name = "code", required = false) String code,
			HttpServletRequest request, HttpServletResponse response){
		logger.info("调用login，授权用户信息到程序");
		response.setCharacterEncoding("UTF-8");
		if (code == null || code.isEmpty()) {
			logger.info("微信登录，授权用户信息时,code为空");
			return new JsonResult<String>(false, String.format(JsonResult.PARAM_NULL, "code"), null);
		}
		logger.info("code:"+code);
		JsonResult responseResult = wechatService.getOpenidByCode(code, 1);
		logger.info("responseResult:"+responseResult);
		if (!responseResult.getSuccess()) {
			logger.info("responseResult is false");
			return new JsonResult<>(false,"responseResult is false");
		}
		Map<String, Object> accesstoken_map = (Map<String, Object>) responseResult.getData();
		if (accesstoken_map.containsKey("errcode")) {
			if(accesstoken_map.get("errcode").equals(0)){
				logger.info("登录正常accesstoken_map:"+accesstoken_map);
			}else if(accesstoken_map.get("errcode").equals(40029)){
				logger.info("40029:code 无效");
				return new JsonResult<>(false,"code 无效");
			}else if(accesstoken_map.get("errcode").equals(40163)){
				logger.info("40163:code重复使用");
				return new JsonResult<>(false,"40163:code重复使用");
			}else if(accesstoken_map.get("errcode").equals(45011)){
				logger.info("45011:频率限制，每个用户每分钟100次");
				return new JsonResult<>(false,"频率限制，每个用户每分钟100次");
			}else{
				return new JsonResult<>(false,"状态异常,请重新打开小程序");
			}
		}
		if(accesstoken_map.get("openid")==null||((String)accesstoken_map.get("openid")).isEmpty()){
			return new JsonResult<>(false,"微信请求正常,但是拿不到openid1");
		}
//		if(accesstoken_map.get("unionid")==null||((String)accesstoken_map.get("unionid")).isEmpty()){
//			return new JsonResult<>(false,"微信请求正常,但是拿不到unionid");
//		}
		String openid = (String) accesstoken_map.get("openid");
		String sessionKey = (String) accesstoken_map.get("session_key");
		GogirlToken gt = new GogirlToken();
		String unionid = null;
		if(accesstoken_map.containsKey("unionid")){
			unionid = (String) accesstoken_map.get("unionid");
			gt.setUnionid(unionid);
		}
		gt.setSysId(1);
		gt.setOpenid(openid);
		gt.setSessionKey(sessionKey);
		//加锁
		logger.info("加锁"+openid);
		openid1Lock.lock(openid1GetInt(openid));
		try {
			Customer unionidCustomer = null;
			if(unionid!=null){
				unionidCustomer = customerService.selectByUnionid(unionid);
			}
			Customer openidCustomer = customerService.selectByOpenid1(openid);
			if(unionidCustomer!=null&&openidCustomer!=null){
				if(unionidCustomer.getId().equals(openidCustomer.getId())){
					//同个用户,啥也不干
				}else{
					if(openidCustomer.getPhone()!=null&&!openidCustomer.getPhone().isEmpty()){//openid1有号码以openid为主
						//取用户数据
						unionidCustomer = customerService.setCustomerData(unionidCustomer, openidCustomer);
						//迁移用户关联数据
						customerService.setCustomerLike(unionidCustomer.getId(), openidCustomer.getId());
						//删除用户
						customerService.deleteByPrimaryKey(unionidCustomer.getId());
						unionidCustomer = openidCustomer;
					}else{//openid无号码,以unionid为主
						//取用户数据
						unionidCustomer = customerService.setCustomerData(openidCustomer, unionidCustomer);
						//迁移用户关联数据
						customerService.setCustomerLike(openidCustomer.getId(), unionidCustomer.getId());
						//删除用户
						customerService.deleteByPrimaryKey(openidCustomer.getId());
					}
				}
			}else if(unionidCustomer!=null&&openidCustomer==null){
				//不做操作
			}else if(unionidCustomer==null&&openidCustomer!=null){
				unionidCustomer=openidCustomer;//直接用openid的用户
			}else{//建一个新用户
				unionidCustomer = new Customer();
			}
			unionidCustomer.setOpenid1(openid);
			if(unionid!=null){
				unionidCustomer.setUnionid(unionid);
			}
			if (unionidCustomer.getId() != null) {
				logger.info("更新用户信息："+unionidCustomer.toString());
				customerService.updateByPrimaryKeySelective(unionidCustomer);
				unionidCustomer = customerService.selectByPrimaryKey(unionidCustomer.getId());
			} else {
				logger.info("插入用户信息："+unionidCustomer.toString());
				int id = customerService.insertSelective(unionidCustomer);
				logger.info("插入后id："+id);
				unionidCustomer.setId(id);
			}
			gt.setCustomerId(unionidCustomer.getId());
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("login异常:"+e.getMessage());
		}
		logger.info("解锁"+openid);
		openid1Lock.unlock(openid1GetInt(openid));
		String token = gogirlTokenService.createToken(gt);

		return new JsonResult<String>(true, JsonResult.APP_DEFINE_SUC, token);
	}
	//客户授权后绑定个人信息authorized1
	@ApiOperation(value = "客户授权后绑定个人信息")
	@RequestMapping(method = {RequestMethod.GET,RequestMethod.POST }, value = "/authorized1")
	public JsonResult<Customer> Authorization1(
			@RequestParam(name = "token", required = false) String token,
			@RequestParam(name = "encryptedData", required = false) String encryptedData,
			@RequestParam(name = "iv", required = false) String iv,
			HttpServletRequest request, HttpServletResponse response){
		logger.info("调用authorized，授权用户信息到程序");
		response.setCharacterEncoding("UTF-8");
		if (token == null || token.isEmpty()) {
			logger.info("微信登录，授权用户信息时,token为空");
			return new JsonResult<Customer>(false, String.format(
					JsonResult.TOKEN_NULL_CODE, JsonResult.TOKEN_NULL_CODE), null);
		}
		GogirlToken gogirtoken = gogirlTokenService.getTokenByToken(token);
		JSONObject data = WXCore.decrypt(wxConfig.getAppid(1), encryptedData,
				gogirtoken.getSessionKey(), iv);
		String unionid = (String) data.get("unionId");
		String openId = (String) data.get("openId");
		Integer gender = (Integer) data.get("gender");
		String avatarUrl = (String) data.get("avatarUrl");
		String city = (String) data.get("city");
		String country = (String) data.get("country");
		String province = (String) data.get("province");
		String nickName = filterEmoji((String) data.get("nickName"));
		
		logger.info("加锁"+openId);
		openid1Lock.lock(openid1GetInt(openId));
		Customer customer = null;
		try {
			Customer unionidCustomer = null;
			if(unionid!=null){
				unionidCustomer = customerService.selectByUnionid(unionid);
			}
			Customer openidCustomer = customerService.selectByOpenid1(openId);
			if(unionidCustomer!=null&&openidCustomer!=null){
				if(unionidCustomer.getId().equals(openidCustomer.getId())){
					//同个用户,啥也不干
				}else{
					if(openidCustomer.getPhone()!=null&&!openidCustomer.getPhone().isEmpty()){//openid1有号码以openid为主
						//取用户数据
						unionidCustomer = customerService.setCustomerData(unionidCustomer, openidCustomer);
						//迁移用户关联数据
						customerService.setCustomerLike(unionidCustomer.getId(), openidCustomer.getId());
						//删除用户
						customerService.deleteByPrimaryKey(unionidCustomer.getId());
						unionidCustomer = openidCustomer;
					}else{//openid无号码,以unionid为主
						//取用户数据
						unionidCustomer = customerService.setCustomerData(openidCustomer, unionidCustomer);
						//迁移用户关联数据
						customerService.setCustomerLike(openidCustomer.getId(), unionidCustomer.getId());
						//删除用户
						customerService.deleteByPrimaryKey(openidCustomer.getId());
					}
				}
			
			}else if(unionidCustomer!=null&&openidCustomer==null){
				//不做操作
			}else if(unionidCustomer==null&&openidCustomer!=null){
				unionidCustomer=openidCustomer;//直接用openid的用户
			}else{//建一个新用户
				unionidCustomer = new Customer();
			}
			unionidCustomer.setOpenid1(openId);
			if(unionid!=null){
				unionidCustomer.setUnionid(unionid);
			}
			if (unionidCustomer.getId() != null) {
				logger.info("更新用户信息："+unionidCustomer.toString());
				customerService.updateByPrimaryKeySelective(unionidCustomer);
				unionidCustomer = customerService.selectByPrimaryKey(unionidCustomer.getId());
			} else {
				logger.info("插入用户信息："+unionidCustomer.toString());
				int id = customerService.insertSelective(unionidCustomer);
				logger.info("插入后id："+id);
				unionidCustomer.setId(id);
			}
			gogirtoken.setCustomerId(unionidCustomer.getId());
			gogirlTokenService.updateByPrimaryKeySelective(gogirtoken);
		
		//再去哪一个全新的用户操作
			if(gogirtoken.getCustomerId()==null){
				logger.info("解锁"+openId);
				openid1Lock.unlock(openid1GetInt(openId));
				return new JsonResult<>(false,JsonResult.TOKEN_NULL_CODE);
			}
			customer = customerService.selectByPrimaryKey(gogirtoken.getCustomerId());
			if(customer==null){
				logger.info("解锁"+openId);
				openid1Lock.unlock(openid1GetInt(openId));
				return new JsonResult<>(false,"找不到用户customerId:"+gogirtoken.getCustomerId());
			}
			if(gogirtoken.getPhone()!=null){
				customer.setPhone(gogirtoken.getPhone());
			}
			if (gender == null)
				gender = 0;
			customer.setHeadimgurl(avatarUrl);
			customer.setCity(city);
			customer.setCountry(country);
			customer.setSex(gender == 1 ? "男" : gender == 2 ? "女" : "无");
			customer.setProvince(province);
			customer.setNickname(nickName);
			if(openId!=null){
				customer.setOpenid1(openId);
			}
			if(unionid!=null){
				customer.setUnionid(unionid);
			}
			if (customer.getId() != null) {
				logger.info("authorized1更新用户信息："+customer.toString());
				customerService.updateByPrimaryKeySelective(customer);
				customer = customerService.selectByPrimaryKey(customer.getId());
			} else {
				logger.info("authorized1插入用户信息："+customer.toString());
				int id = customerService.insertSelective(customer);
				logger.info("插入后id："+id);
				customer.setId(id);
			}
			gogirtoken.setCustomerId(customer.getId());
			gogirlTokenService.updateByPrimaryKeySelective(gogirtoken);			
		} catch (Exception e) {
			logger.info("authorized1错误:"+e.getMessage());
		}
		logger.info("解锁"+openId);
		openid1Lock.unlock(openid1GetInt(openId));

		/* 设置用户session */
		HttpSession session = request.getSession();
		logger.info("session:" + session.hashCode());
		session.setAttribute("customer", customer);
		return new JsonResult<Customer>(true, JsonResult.APP_DEFINE_SUC, customer);
	}
	//客户授权手机号码authorizedPhone
	@ApiOperation(value = "客户授权手机号码")
	@RequestMapping(method = { RequestMethod.POST }, value = "/authorizedPhone")
	public JsonResult<Customer> authorizedPhone(
			@RequestParam(name = "token", required = false) String token,
			@RequestParam(name = "encryptedData", required = false) String encryptedData,
			@RequestParam(name = "iv", required = false) String iv,
			HttpServletRequest request, HttpServletResponse response){
		logger.info("解密encryptedData");
		response.setCharacterEncoding("UTF-8");
		if (token == null || token.isEmpty()) {
			logger.info("token为空");
			return new JsonResult<Customer>(false, String.format(JsonResult.PARAM_NULL, "token"), null);
		}
		GogirlToken gogirtoken = gogirlTokenService.getTokenByToken(token);
		if(gogirtoken==null){
			return new JsonResult<Customer>(false ,JsonResult.TOKEN_NULL_CODE,null);
		}
		logger.info("手机号码授权,用户信息:"+gogirtoken.getCustomer());
		if(encryptedData==null){
			logger.info("encryptedData为空");
			return new JsonResult<>(false,"encryptedData为空");
		}
		if(gogirtoken.getSessionKey()==null){
			logger.info("gogirtoken.getSessionKey()为空");
			return new JsonResult<>(false,"gogirtoken.getSessionKey()为空");
		}
		if(iv==null){
			logger.info("iv为空");
			return new JsonResult<>(false,"iv为空");
		}
		JSONObject data = null;
		try {
			data = WXCore.decrypt(wxConfig.getAppid(1), encryptedData,gogirtoken.getSessionKey(), iv);
		} catch (Exception e) {
			logger.info("用户信息:"+gogirtoken.getCustomer());
			logger.info("解码出错:"+e.getMessage());
			logger.info("encryptedData:"+encryptedData);
			logger.info("getSessionKey:"+gogirtoken.getSessionKey());
			logger.info("iv:"+iv);
			return new JsonResult<>(false,"状态异常1,请重新打开小程序后再授权");
		}
		if(data==null){
			return new JsonResult<>(false,"encryptedData解密后数据为null");
		}
		if(!data.containsKey("purePhoneNumber")||((String)data.get("purePhoneNumber")).isEmpty()){
			logger.info("用户信息:"+gogirtoken.getCustomer());
			return new JsonResult<>(false,"状态异常2,请重新打开小程序");
		}
		String phone = (String) data.get("purePhoneNumber");
		GogirlToken updategt = new GogirlToken();
		updategt.setId(gogirtoken.getId());
		updategt.setPhone(phone);
		gogirlTokenService.updateByPrimaryKeySelective(updategt);
		
		Customer phoneCustomer = customerService.selectByPrimaryKey(gogirtoken.getCustomerId());
		phoneCustomer.setPhone(phone);
		Integer id = customerService.updateByPrimaryKeySelective(phoneCustomer);
		if(id!=null&&id>0){
			gogirtoken.setCustomerId(id);
			gogirlTokenService.updateByPrimaryKeySelective(gogirtoken);
		}
		return new JsonResult<Customer>(true, JsonResult.APP_DEFINE_SUC, phoneCustomer);
	}	

	
	@ApiOperation(value = "收集美甲师formId,,type:1用户端；2美甲师端")
	@RequestMapping(method = { RequestMethod.POST }, value = "/saveFormId")
	public JsonResult<Integer> saveFormId(String token,String formId,Integer type)
			throws Exception {
		logger.info("收集美甲师formId,,type:"+type);
		if(token==null){
			return new JsonResult<>(false,String.format(JsonResult.PARAM_NULL,"token"),null);
		}
		if(formId==null){
			return new JsonResult<>(false,String.format(JsonResult.PARAM_NULL,"formId"),null);
		}
		//特俗任务，存formId
	    if(formId!=null&&!formId.isEmpty()&&!formId.equals("the formId is a mock one")&&token!=null&&!token.isEmpty()){
	    	GogirlToken gogirltoken = null;
	    	if(type==2){
	    		gogirltoken = gogirlTokenService.getTokenByToken_t(token);
	    	}else{
	    		gogirltoken = gogirlTokenService.getTokenByToken(token);
	    	}
	    	
	    	if(gogirltoken.getCustomerId()!=null){
	        	XcxFormId xcxFormId = new XcxFormId();
	        	xcxFormId.setCustomerId(gogirltoken.getCustomerId());
	        	if(type==2){
		        	xcxFormId.setOpenid(gogirltoken.getUserTechnician().getOpenid());
	        	}else{
		        	xcxFormId.setOpenid(gogirltoken.getCustomer().getOpenid1());
	        	}
	        	xcxFormId.setFormId(formId);
	        	xcxFormId.setType(type);
	        	xcxFormId.setTime(new Date());
	        	if(gogirltoken.getCustomer()!=null&&gogirltoken.getCustomer().getOpenid1()!=null){
	        		xcxFormId.setOpenid(gogirltoken.getCustomer().getOpenid1());
	        	}
	        	xcxFormIdMapper.insertSelective(xcxFormId);
	        	XcxFormId xcxFormId1 = new XcxFormId();
	        	xcxFormId1.setCustomerId(gogirltoken.getCustomerId());
	        	xcxFormId1.setType(type);
	        	xcxFormId1.setTime(new Date(new Date().getTime()-new Long("518400000")));//时间6天内存的formId
	        	Integer num = xcxFormIdMapper.countNumByXcxFormId(xcxFormId1);
	        	return new JsonResult<>(true,JsonResult.APP_DEFINE_SUC,num==null?0:num);
	    	}
	    }
	    
	    return new JsonResult<>(true,JsonResult.APP_DEFINE_SUC,null);
	}
	@ApiOperation(value = "查询还有几次接收模板消息的机会,type:1用户端；2美甲师端")
	@RequestMapping(method = { RequestMethod.POST }, value = "/getFormIdNum")
	public JsonResult<Integer> getFormIdNum(String token,Integer type)
			throws Exception {
		logger.info("查询还有几次接收模板消息的机会:"+type);
		//特俗任务，存formId
    	GogirlToken gogirltoken = gogirlTokenService.getTokenByToken(token);
    	if(gogirltoken.getCustomerId()!=null){
        	XcxFormId xcxFormId = new XcxFormId();
        	xcxFormId.setCustomerId(gogirltoken.getCustomerId());
        	xcxFormId.setType(type);
        	xcxFormId.setTime(new Date(new Date().getTime()-new Long("518400000")));//时间6天内存的formId
        	Integer num = xcxFormIdMapper.countNumByXcxFormId(xcxFormId);
        	return new JsonResult<>(true,JsonResult.APP_DEFINE_SUC,num==null?0:num);
    	}else{
    		return new JsonResult<>(false,"token查不到用户信息",null);
    	}
	}

	// 用户端小程序获取用户信息
	@RequestMapping(method = {RequestMethod.GET}, value = "/getUserInfo")
	@ApiOperation(value = "客户根据token获取用户信息", notes = "返回customer信息，如果没有该用户返回null")
	@ApiImplicitParam(paramType = "query", name = "token", value = "token编号", required = true, dataType = "String")
	public JsonResult<Object> getUserInfo(@RequestParam String token) throws Exception {
		logger.info("根据token:" + token + ",查询用户信息.");
		if (token == null) {
			return new JsonResult<Object>(false, JsonResult.TOKEN_NULL_CODE,
					JsonResult.TOKEN_NULL_MSG);
		}
		GogirlToken gogirlToken = gogirlTokenService.getTokenByToken(token);
		if (gogirlToken == null) {
			return new JsonResult<Object>(false, JsonResult.TOKEN_NULL_CODE,
					JsonResult.TOKEN_NULL_MSG);
		}
		Customer c = null;
		if (gogirlToken.getId() != null) {
			c = customerService.selectByPrimaryKeyWithCard(gogirlToken.getCustomerId());
		} else if (gogirlToken.getUnionid() != null) {
			c = customerService.selectByUnionid(gogirlToken.getUnionid());
		} else if (gogirlToken.getOpenid() != null) {
			c = customerService.selectByOpenid1(gogirlToken.getOpenid());
		}
		if (c != null) {
			//判断是否有用户二维码,若没有则生成
			if(c.getMyQrcode()==null&&c.getPhone()!=null){
				String myQrcode = qrCodeService.myQrCode(c.getId().toString(), c.getPhone());
				c.setMyQrcode(myQrcode);
				Customer updateC = new Customer();
				updateC.setId(c.getId());
				updateC.setMyQrcode(myQrcode);
				customerService.updateByPrimaryKeySelective(updateC);
			}
			return new JsonResult<Object>(true, JsonResult.APP_DEFINE_SUC, c);
		} else {
			return new JsonResult<Object>(false, JsonResult.TOKEN_NULL_AUTHORIZED_CODE, JsonResult.TOKEN_NULL_AUTHORIZED_MSG);
		}
	}


	// 美甲师端登录
	@ApiOperation(value = "美甲师根据token获取用户信息")
	@RequestMapping(method = { RequestMethod.GET }, value = "/getUserInfo_t")
	public JsonResult getUserInfo_t(String token) throws Exception {
		logger.info("根据token:" + token + ",查询用户信息.");
		if (token == null) {
			return new JsonResult(false, JsonResult.TOKEN_NULL_CODE,
					JsonResult.TOKEN_NULL_MSG);
		}
		// TODO id没选的话，需要先选店铺才有customerid
		GogirlToken gogirlToken = gogirlTokenService.getTokenByToken(token);
		if (gogirlToken == null) {
			return new JsonResult(false, JsonResult.TOKEN_NULL_CODE,
					JsonResult.TOKEN_NULL_MSG);
		}
		if (gogirlToken.getCustomerId() != null) {
			UserTechnician tm = userTechnicianService
					.getTechnicianManageForDetail(gogirlToken.getCustomerId());
			List<UserTechnician> list = new ArrayList<UserTechnician>();
			list.add(tm);
			return new JsonResult(true, JsonResult.APP_DEFINE_SUC, list);
		} else {
			UserTechnician technicianManage = new UserTechnician();
			technicianManage.setOpenid(gogirlToken.getOpenid());
			List<UserTechnician> list = userTechnicianService
					.listTechnicianForPage(technicianManage);
			return new JsonResult(true, JsonResult.APP_DEFINE_SUC, list);
		}
	}

	@ApiOperation(value = "美甲师根据code获取token")
	@RequestMapping(method = { RequestMethod.GET }, value = "/login_t")
	public JsonResult login_t(
			@RequestParam(name = "code", required = false) String code,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		logger.info("调用login，授权用户信息到程序");
		response.setCharacterEncoding("UTF-8");
		if (code == null || code.isEmpty()) {
			logger.info("微信登录，授权用户信息时,code为空");
			return new JsonResult(false, String.format(
					JsonResult.PARAM_NULL, "code"), null);
		}
		// Customer customer = new Customer();
		String openid;
		try {
			logger.info(code + "获取用户信息");
			// customer = wechatService.getCustomerBycode(code,1,customer);
			JsonResult responseResult = wechatService.getOpenidByCode(code, 2);
			Map<String, Object> accesstoken_map = (Map<String, Object>) responseResult.getData();
			if (accesstoken_map.containsKey("errcode")
					&& (accesstoken_map.get("errcode").equals(40029) || accesstoken_map
							.get("errcode").equals(40163))) {
				logger.info("40029");
				throw new Exception(JsonResult.INVALID_CODE);
			}
			if (accesstoken_map.get("openid") == null
					|| accesstoken_map.get("openid").equals("")) {
				return new JsonResult(false, "获取到的openid为空", null);
			}
			openid = (String) accesstoken_map.get("openid");
		} catch (Exception e) {
			logger.info("登录异常:" + e.getMessage());
			if (e.getMessage().equals(JsonResult.INVALID_CODE)) {
				return new JsonResult(false, JsonResult.INVALID_CODE, null);
			} else {
				throw e;
			}
		}
		// 根据openid查找美甲师表
		List<UserTechnician> list = userTechnicianService
				.getTechnicianManageByOpenid(openid);
		// 根据美甲师的id生成token
		GogirlToken gt = new GogirlToken();
		gt.setSysId(2);
		if (list != null && list.size() == 1) {
			gt.setCustomerId(list.get(0).getId());
		}
		gt.setOpenid(openid);
		// 返回美甲师信息及token
		String token = gogirlTokenService.createToken(gt);
		return new JsonResult(true, JsonResult.APP_DEFINE_SUC, token);
	}


	@ApiOperation(value = "解码数据EncryptedData")
	@RequestMapping(method = { RequestMethod.POST }, value = "/decodeEncryptedData")
	public JsonResult decodeEncryptedData(
			@RequestParam(name = "token", required = false) String token,
			@RequestParam(name = "encryptedData", required = false) String encryptedData,
			@RequestParam(name = "iv", required = false) String iv,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		logger.info("解密encryptedData");
		response.setCharacterEncoding("UTF-8");
		if (token == null || token.isEmpty()) {
			logger.info("token为空");
			return new JsonResult(false, String.format(
					JsonResult.PARAM_NULL, "token"), null);
		}
		GogirlToken gogirtoken = gogirlTokenService.getTokenByToken(token);
		JSONObject data = WXCore.decrypt(wxConfig.getAppid(1), encryptedData,
				gogirtoken.getSessionKey(), iv);

		return new JsonResult(true, JsonResult.APP_DEFINE_SUC, data);
	}
	@ApiOperation(value = "美甲师选择多门店的账号")
	@RequestMapping(method = { RequestMethod.POST }, value = "/choseStore")
	public JsonResult choseStore(
			@RequestParam(name = "token", required = true) String token,
			@RequestParam(name = "technicianId", required = true) Integer technicianId)
			throws Exception {
		logger.info("美甲师选择多门店的账号");
		if(token==null||token.isEmpty()){
			return new JsonResult(false,"入参token为空",null);
		}
		if(technicianId==null){
			return new JsonResult(false,"入参technicianId为空",null);
		}
		GogirlToken gt = gogirlTokenService.getTokenByToken(token);
		if(gt==null){
			return new JsonResult(false,"token过期",null);
		}
		UserTechnician technicianManage = userTechnicianService.getTechnicianManageForDetail(technicianId);
		if(technicianManage==null){
			return new JsonResult(false,"找不到该美甲师账号",null);
		}
		GogirlToken gogirlToken = new GogirlToken();
		gogirlToken.setId(gt.getId());
		gogirlToken.setCustomerId(technicianId);
		gogirlTokenService.updateByPrimaryKeySelective(gogirlToken);
		return new JsonResult(true, JsonResult.APP_DEFINE_SUC, technicianManage);
	}
	@ApiOperation(value = "美甲师选择多门店的账号-根据订单id")
	@RequestMapping(method = { RequestMethod.POST }, value = "/choseStoreByOrderId")
	public JsonResult choseStoreByOrderId(
			@RequestParam(name = "token", required = true) String token,
			@RequestParam(name = "orderId", required = true) Integer orderId)
			throws Exception {
		logger.info("美甲师选择多门店的账号");
		if(token==null||token.isEmpty()){
			return new JsonResult(false,"入参token为空",null);
		}
		if(orderId==null){
			return new JsonResult(false,"入参orderId为空",null);
		}
		GogirlToken gt = gogirlTokenService.getTokenByToken(token);
		if(gt==null){
			return new JsonResult(false,"token过期",null);
		}
		OrderManage orderManage = orderManageService.getOrderForDetail(orderId);
		if(orderManage==null){
			return new JsonResult(false,"找不到订单",null);
		}
		if(orderManage.getDepartmentId()==null){
			return new JsonResult(false,"订单无店铺",null);
		}
		
		List<UserTechnician> listUserTechnician = userTechnicianService.getTechnicianManageByOpenid(gt.getOpenid());
		if(listUserTechnician==null){
			return new JsonResult(false,"找不到该美甲师账号",null);
		}
		for(int i=0;i<listUserTechnician.size();i++){
			if(listUserTechnician.get(i).getDepartmentId().equals(orderManage.getDepartmentId())){
				GogirlToken gogirlToken = new GogirlToken();
				gogirlToken.setId(gt.getId());
				gogirlToken.setCustomerId(listUserTechnician.get(i).getId());
				gogirlTokenService.updateByPrimaryKeySelective(gogirlToken);
				return new JsonResult(true, JsonResult.APP_DEFINE_SUC, listUserTechnician.get(i));
			}
		}
		return new JsonResult(true, "无该店铺");
	}
	@ApiOperation(value = "图片上传")
	@RequestMapping(method = { RequestMethod.POST }, value = "/upload")
	public JsonResult choseStore(MultipartFile file)
			throws Exception {
		logger.info("美甲师选择多门店的账号");
		if(file==null){
			return new JsonResult(false,"file为空",null);
		}
		String imgUrl = ImageUtil.saveImage(gogirlProperties.getPicturePath(), file);

		return new JsonResult(true, JsonResult.APP_DEFINE_SUC, imgUrl);
	}

//	@ApiOperation(value = "美甲师静默授权，获取小程序openid")
//	@RequestMapping(method = { RequestMethod.POST }, value = "/authorized2")
//	public JsonResult Authorization2(
//			@RequestParam(name = "code", required = false) String code,
//			@RequestParam(name = "state", required = false) String state,
//			@RequestParam(name = "avatarUrl", required = false) String avatarUrl,
//			@RequestParam(name = "city", required = false) String city,
//			@RequestParam(name = "country", required = false) String country,
//			@RequestParam(name = "gender", required = false) Integer gender,
//			@RequestParam(name = "language", required = false) String language,
//			@RequestParam(name = "nickName", required = false) String nickName,
//			@RequestParam(name = "province", required = false) String province,
//			HttpServletRequest request, HttpServletResponse response)
//			throws Exception {
//		logger.info("调用authorized，授权用户信息到程序");
//		response.setCharacterEncoding("UTF-8");
//		if (code == null || code.isEmpty()) {
//			logger.info("微信登录，授权用户信息时,code为空");
//			return new JsonResult(false, String.format(
//					JsonResult.PARAM_NULL, "code"), null);
//		}
//		if (state == null || state.isEmpty()) {
//			logger.info("微信登录，授权用户信息时,state为空");
//			return new JsonResult(false, String.format(
//					JsonResult.PARAM_NULL, "state"), null);
//		}
//
//		UserManage storeUser = new UserManage();
//		// if(gender==null)gender=0;
//		storeUser.setPicturePath(avatarUrl);
//		// storeUser.set(gender==1?"男":gender==2?"女":"无");
//		storeUser.setName(nickName);
//		JsonResult responseResult = wechatService.getOpenidByCode(code, 2);
//		if (!responseResult.getSuccess()) {
//			return null;
//		}
//		Map<String, Object> accesstoken_map = (Map<String, Object>) responseResult
//				.getData();
//		if (accesstoken_map.containsKey("errcode")
//				&& (accesstoken_map.get("errcode").equals(40029) || accesstoken_map
//						.get("errcode").equals(40163))) {
//			logger.info("40029");
//			throw new Exception(JsonResult.INVALID_CODE);
//		}
//		String openid = (String) accesstoken_map.get("openid");
//		List<UserManage> qUserManage = storeUserService.selectByopenid(openid);
//		if (qUserManage != null && qUserManage.size() > 0) {
//			// 如果有该美甲师，则不动任何数据
//			storeUser = qUserManage.get(0);
//		} else {
//			// 如果没有该美甲师，插入该美甲师的数据
//			int id = storeUserService.insertUserManage(storeUser);
//			storeUser.setId(id);
//		}
//		HttpSession session = request.getSession();
//		logger.info("session:" + session.hashCode());
//		session.setAttribute("storeUser", storeUser);
//		/* 记录登陆信息 */
//		// LoginInfo loginInfo = new LoginInfo();
//		// loginInfo.setId(customer.getId()==null?0:customer.getId());
//		// loginInfo.setType(new Byte(state));
//		// loginInfo.setTime(new Date());
//		// loginInfo.setRedirectUri(request.getRequestURL().toString());
//		// loginInfoService.insertSelective(loginInfo);
//		/* 记录登陆信息 */
//		return new JsonResult(true, JsonResult.APP_DEFINE_SUC, storeUser);
//	}

	public JsonResult loginBindTechnician(String token, Integer technicianId) {
		GogirlToken gogirlToken = gogirlTokenService.getTokenByToken(token);
		gogirlToken.setCustomerId(technicianId);
		gogirlTokenService.updateByPrimaryKeySelective(gogirlToken);
		return new JsonResult(true, JsonResult.APP_DEFINE_SUC, gogirlToken);
	}
	/**
	  * 将emoji表情替换成空串
	  *  
	  * @param source
	  * @return 过滤后的字符串
	  */
	 public static String filterEmoji(String source) {
	  if (source != null && source.length() > 0) {
	   return source.replaceAll("[\ud800\udc00-\udbff\udfff\ud800-\udfff]", "");
	  } else {
	   return source;
	  }
	 }
	//字符串中所有字符相加得到一个int
	public int openid1GetInt(String openid1) {
		StringBuffer sb = new StringBuffer(openid1);
		int sum = 0;
		int length = sb.length();
		for(int i=20;i<length;i++){
			sum+=sb.charAt(i);
		}
		return sum;
	}

}
