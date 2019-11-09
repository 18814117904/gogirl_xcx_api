package com.gogirl.gogirl_xcx.entity;

import org.springframework.stereotype.Component;

@Component
public class AccessToken {
	
	private String access_token;//获取到的凭证
	
	private int expires_in;//凭证有效时间
//	private static String access_token1;//获取到的凭证
//	
//	private static int expires_in1;//凭证有效时间
//	private static String access_token2;//获取到的凭证
//	
//	private static int expires_in2;//凭证有效时间

	public String getAccess_token() {
//		if(type==0){
			return access_token;
//		}else if(type==1){
//			return access_token1;
//		}else if(type==2){
//			return access_token2;
//		}else{
//			return null;
//		}
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public int getExpires_in() {
//		if(type==0){
			return expires_in;
//		}else if(type==1){
//			return expires_in;
//		}else if(type==2){
//			return expires_in;
//		}else{
//			return 0;
//		}
	}

	public void setExpires_in(int expires_in) {
		this.expires_in = expires_in;
	}
	
	
}	
