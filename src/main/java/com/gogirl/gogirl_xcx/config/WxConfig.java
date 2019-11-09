package com.gogirl.gogirl_xcx.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
@Component
@ConfigurationProperties(prefix = "wechat")
public class WxConfig {
	public String appid;
	public String secret;
	public String appid1;
	public String secret1;
	public String appid2;
	public String secret2;
	public String getAppid(int type) {
		if(type == 0){
			return appid;
		}else if(type == 1){
			return appid1;
		}else if(type == 2){
			return appid2;
		}else{
			return null;
		}
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public String getSecret(int type) {
		if(type == 0){
			return secret;
		}else if(type == 1){
			return secret1;
		}else if(type == 2){
			return secret2;
		}else{
			return null;
		}
	}
	public void setSecret(String secret) {
		this.secret = secret;
	}
	public void setAppid1(String appid1) {
		this.appid1 = appid1;
	}
	public void setSecret1(String secret1) {
		this.secret1 = secret1;
	}
	public void setAppid2(String appid2) {
		this.appid2 = appid2;
	}
	public void setSecret2(String secret2) {
		this.secret2 = secret2;
	}
	
}
