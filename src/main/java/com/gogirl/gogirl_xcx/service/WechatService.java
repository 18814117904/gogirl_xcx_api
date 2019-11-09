package com.gogirl.gogirl_xcx.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.gogirl.gogirl_order.order_commons.utils.JsonResult;
import com.gogirl.gogirl_user.entity.Customer;
import com.gogirl.gogirl_user.service.CustomerService;
import com.gogirl.gogirl_user.service.myhttp.MyHttpPost;
import com.gogirl.gogirl_xcx.config.WxConfig;
import com.gogirl.gogirl_xcx.config.WxUrlType;
import com.gogirl.gogirl_xcx.entity.GogirlToken;
import com.gogirl.gogirl_xcx.util.WXCore;


@Service
public class WechatService {
    private final Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	RestTemplate restTemplate;
	@Autowired
	MyHttpPost myHttpPost ;
	@Autowired
	WxConfig wxConfig;
	@Autowired
	AccessTokenService accessTokenService;
	@Resource
	CustomerService customerService;
	//根据code获得openid和accesstoken
	public JsonResult getOpenidByCode(String code,int type) {
		String url = null;
		url = WxUrlType.OAuth2_ACCESS_TOKEN_URL.replaceAll("APPID", wxConfig.getAppid(type)).replaceAll("SECRET", wxConfig.getSecret(type)).replaceAll("JSCODE", code);
		ResponseEntity<String> response = restTemplate.postForEntity(url, null,String.class );
		if(response==null){
			return new JsonResult(false,"微信服务器返回空",null);
		}
		if(!response.getStatusCode().equals(200)){
			return new JsonResult(true,"正常",JSONObject.fromObject(response.getBody()));
		}else{
			return new JsonResult(false,"微信服务器访问异常",JSONObject.fromObject(response.getBody()));
		}
	}

	public JsonResult getUserInfoByAccessToken2(String openid,int type) {
		String token = accessTokenService.getAccess_Token(type).getAccess_token();
		String url = WxUrlType.OAuth2_USER_INFO_URL2.replaceAll("OPENID", openid).replaceAll("ACCESS_TOKEN", token);
		ResponseEntity<String> response = restTemplate.postForEntity(url, null,String.class );
//		ResponseEntity<JsonResult> response = myHttpPost.httpRequestEntity(url,null);
		if(response==null){
			return new JsonResult(false,"微信服务器返回空",null);
		}
		if(!response.getStatusCode().equals(200)){
			return new JsonResult(true,"正常",JSONObject.fromObject(response.getBody()));
		}else{
			return new JsonResult(false,"微信服务器访问异常",JSONObject.fromObject(response.getBody()));
		}
	}
	
	
	//从微信服务器获得用户信息
	public Customer getCustomerBycode(String code,int type,Customer customer) throws Exception {
		//获得openid和accesstoken
		JsonResult responseResult = getOpenidByCode(code,type);
		if(!responseResult.getSuccess()){
			return null;
		}
		Map<String, Object> accesstoken_map = (Map<String, Object>)responseResult.getData();
		if(accesstoken_map.containsKey("errcode")&&(accesstoken_map.get("errcode").equals(40029)||accesstoken_map.get("errcode").equals(40163))){
			logger.info("40029");
			throw new Exception(JsonResult.INVALID_CODE);
		}
		String openid = (String) accesstoken_map.get("openid");
		String unionid = (String) accesstoken_map.get("unionid");
//		GogirlToken 
		
		// 根据unionid查询用户信息
//		if(unionid==null||unionid.isEmpty()){
//			throw new Exception("unionid为空");
//		}
//		customer.setUnionid(unionid);
//		if(type==0){
//			customer.setOpenid(openid);
//		}else if(type==1){
//			customer.setOpenid1(openid);
//		}else if(type==2){
////			customer.setOpenid2(openid);
//		}
//		Customer qc=customerService.selectByUnionid(unionid);
//		if(qc!=null){
//			customer.setId(qc.getId());
//			customerService.updateByPrimaryKeySelective(customer);
//		}else{
//			int id = customerService.insertSelective(customer);
//			customer.setId(id);
//		}
//
//
//		if(customer!=null){//***数据库存在用户
//			if(customer.getUpdateTime()!=null){
//				Date updateTime =  customer.getUpdateTime();
//				if(greaterThan30Day(updateTime)){//当前时间距离该用户上次登陆是否大于30天
//					customer = updateInfoFromWechat(customer.getId(),openid,type);//更新数据
//				}else{//使用数据库查询得到的数据
//					return customer;
//				}
//			}else{
//				customer = updateInfoFromWechat(customer.getId(),openid,type);//更新数据
//			}
//		}else{
//			customer = insertInfoFromWechat(openid,type);//添加用户
//		}
		return customer;
	}
	
	private Customer updateInfoFromWechat(Integer integer, String openid,int type){
		//通过accesstoken，从微信服务器获得用户信息
		JsonResult response2=getUserInfoByAccessToken2(openid,type);
		//用户信息存入数据库
		Customer customer;
		if(response2.getSuccess()==true){
			Map<String, Object> map = ((Map<String, Object>)response2.getData());
			map.put("id",integer);//从数据库中获取
			//Todo 设置id
			map.put("sex",map.get("sex").equals(1)?"男":"女");
//			map.put("state","1");//状态固定设为1正常
			customer = new Customer();
			customer.setId((Integer) map.get("id"));
			customer.setOpenid((String) map.get("openid"));
			customer.setUnionid((String) map.get("unionid"));
			customer.setPhone((String) map.get("phone"));
			String nickname =  filterEmoji((String)map.get("nickname"));
			customer.setNickname(nickname);
			customer.setPassword((String) map.get("password"));
			customer.setSex((String) map.get("sex"));
			customer.setCountry((String) map.get("country"));
			customer.setProvince((String) map.get("province"));
			customer.setCity((String) map.get("city"));
			customer.setHeadimgurl((String) map.get("headimgurl"));
			customer.setPrivilege( map.get("privilege").toString());
			customer.setState((String) map.get("state"));
//			customer.setRegisterTime(new Date());//更新不需要这个字段
			customer.setUpdateTime(new Date());
			customer.setSource(3);
			/*更新数据库客户客户信息*/
			customerService.updateByPrimaryKeySelective(customer);
			/*更新数据库客户客户信息*/
		}else{
			logger.info("JsonResult response2=getUserInfoByAccessToken(access_token);==false");
			return null;
		}
		return customer;
	}
	private Customer insertInfoFromWechat(String openid,int type){
		//通过accesstoken，从微信服务器获得用户信息
		JsonResult response2=getUserInfoByAccessToken2(openid,type);
		//用户信息存入数据库
		Customer customer;
		if(response2.getSuccess()==true){
			Map<String, Object> map = ((Map<String, Object>)response2.getData());
			map.put("sex",map.get("sex").equals(1)?"男":"女");
			map.put("state","1");//状态固定设为1正常
			
			customer = new Customer();
//			customer.setId((Integer) map.get("id"));//自增
			customer.setOpenid((String) map.get("openid"));
			customer.setUnionid((String) map.get("unionid"));
			customer.setPhone((String) map.get("phone"));
			String nickname =  filterEmoji((String)map.get("nickname"));
			customer.setNickname(nickname);
			customer.setPassword((String) map.get("password"));
			customer.setSex((String) map.get("sex"));
			customer.setCountry((String) map.get("country"));
			customer.setProvince((String) map.get("province"));
			customer.setCity((String) map.get("city"));
			customer.setHeadimgurl((String) map.get("headimgurl"));
			customer.setPrivilege( map.get("privilege").toString());
			customer.setState((String) map.get("state"));
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			customer.setRegisterTime(new Date());//更新不需要这个字段
			customer.setUpdateTime(new Date());
			customer.setSource(3);
			/*更新数据库客户客户信息*/
			int id = customerService.insertSelective(customer);
			customer.setId(id);
			/*更新数据库客户客户信息*/
		}else{
			return null;
		}
		return customer;
	}

//	public JsonResult mergeRemoveCustomer(Integer id, Customer customer) {
////		ResponseEntity<String> result1 = restTemplate.postForEntity("http://192.168.1.2:8089/gogirl_user/updateCustomerSelective",customer , String.class);
//		JsonResult result1 = myHttpPost.httpRequest("http://192.168.1.2:8089/gogirl_user/updateCustomerSelective",ParseUtil.paramsToMap(customer));
//		System.out.println(result1);
////		JsonResult jsonResult = restTemplate.postForObject("http://192.168.1.2:8089/gogirl_user/mergeRemoveCustomer/"+id,null , JsonResult.class);
//		JsonResult jsonResult = myHttpPost.httpRequest("http://192.168.1.2:8089/gogirl_user/mergeRemoveCustomer/"+id,null);
//		return jsonResult;
//	}
	public boolean  greaterThan30Day(Date updateTime) {
		Long test = new Date().getTime()-updateTime.getTime();
		Long day30 = new Long("2592000000");//30天的毫秒数
		if(test.compareTo(day30)>0){
			return true;
		}else{
			return false;
		}
	}
	
	public String getAUrl(String redirect_uri,String state,int type) {
		try {
			redirect_uri = URLEncoder.encode(redirect_uri, "utf-8");
			state = URLEncoder.encode(state, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String url = WxUrlType.OAUTH2_AUTHORIZE_URL.replaceAll("APPID", wxConfig.getAppid(type))
			.replaceAll("REDIRECT_URI", redirect_uri).replaceAll("SCOPE", "snsapi_userinfo").replaceAll("STATE", state);
		return url;
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


}
