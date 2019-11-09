package com.gogirl.gogirl_xcx.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.gogirl.gogirl_xcx.config.WxConfig;
import com.gogirl.gogirl_xcx.entity.AccessToken;



@Configuration          //证明这个类是一个配置文件
@EnableScheduling       //打开quartz定时器总开关

@Service
public class AccessTokenService {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	@Resource
	WxConfig config;
	AccessToken accessToken;
	AccessToken accessToken1;
	AccessToken accessToken2;

	public AccessToken getAccess_Token(int type) {
		System.out.println("获取accessToken,type="+type);
		if(type==0){
			if (accessToken == null || accessToken.getAccess_token() == null) {
				System.out.println("公众号accessToken，从微信服务器获取");
				AccessToken accessToken = getAccessToken(config.getAppid(type), config.getSecret(type));
				this.accessToken = accessToken;
				return accessToken;
			} else {
				System.out.println("公众号accessToken，从上下文获取");
				return accessToken;
			}
		}else if(type==1){
			if (accessToken1 == null || accessToken1.getAccess_token() == null) {
				System.out.println("客户端小程序accessToken1，从微信服务器获取");
				AccessToken accessToken = getAccessToken(config.getAppid(type), config.getSecret(type));
				this.accessToken1 = accessToken;
				return accessToken1;
			} else {
				System.out.println("客户端小程序accessToken1，从上下文获取");
				return accessToken1;
			}
		}else if(type==2){
			if (accessToken2 == null || accessToken2.getAccess_token() == null) {
				System.out.println("客户端小程序accessToken2，从微信服务器获取");
				AccessToken accessToken = getAccessToken(config.getAppid(type), config.getSecret(type));
				this.accessToken2 = accessToken;
				return accessToken2;
			} else {
				System.out.println("客户端小程序accessToken2，从上下文获取");
				return accessToken2;
			}
		}else{
			return null;
		}
	}

	/**
	 * 获取accessToken
	 * 
	 * @param appID微信公众号凭证
	 * @param appScret微信公众号凭证秘钥
	 * @return
	 */
	public static AccessToken getAccessToken(String appID, String appScret) {
		AccessToken token = new AccessToken();
		// 访问微信服务器
		String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="
				+ appID + "&secret=" + appScret;
		try {
			URL getUrl = new URL(url);
			HttpURLConnection http = (HttpURLConnection) getUrl
					.openConnection();
			http.setRequestMethod("GET");
			http.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			http.setDoOutput(true);
			http.setDoInput(true);

			http.connect();
			InputStream is = http.getInputStream();
			int size = is.available();
			byte[] b = new byte[size];
			is.read(b);

			String message = new String(b, "UTF-8");

			JSONObject json = JSONObject.fromObject(message);
			System.out.println(json.toString());
			token.setAccess_token(json.getString("access_token"));
			token.setExpires_in(new Integer(json.getString("expires_in")));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return token;
	}
    @Scheduled(cron = "0 0 0/2 * * *")// 关闭自动获取accesstoken
    public void refreshAccessToken(){
        //获取当前时间
    	
        LocalDateTime localDateTime =LocalDateTime.now();
        System.out.println("当前时间为:" + localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
//		//刷新accessToken
//        AccessToken accessToken = getAccessToken(config.getAppid(0), config.getSecret(0));
//        this.accessToken = accessToken;
        AccessToken accessToken1 = getAccessToken(config.getAppid(1), config.getSecret(1));
		this.accessToken1 = accessToken1;
        AccessToken accessToken2 = getAccessToken(config.getAppid(2), config.getSecret(2));
		this.accessToken2 = accessToken2;
//        System.out.println("公众号，刷新accessToken为："+accessToken.getAccess_token());
        System.out.println("用户端小程序，刷新accessToken为："+accessToken1.getAccess_token());
        System.out.println("店员端小程序，刷新accessToken为："+accessToken2.getAccess_token());
    }
	// public AccessToken getAccessToken(){
	// System.out.println("从接口中获取");
	// Jedis jedis = RedisUtil.getJedis();
	// AccessToken token = new AccessToken();
	// String url = UrlType.ACCESS_TOKEN_URL.replace("APPID",
	// config.getAppId()).replace("APPSECRET", config.getSecret());
	// JSONObject json = WeiXinUtil.doGetstr(url);
	// if(json!=null){
	// token.setAccess_token(json.getString("access_token"));
	// token.setExpires_in(json.getInt("expires_in"));
	// jedis.set("access_token", json.getString("access_token"));
	// jedis.expire("access_token", 60*60*2);
	// }
	// RedisUtil.returnResource(jedis);
	// return token;
	// }
}
