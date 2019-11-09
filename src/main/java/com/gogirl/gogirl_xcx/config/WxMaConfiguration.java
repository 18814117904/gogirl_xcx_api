package com.gogirl.gogirl_xcx.config;

import javax.annotation.Resource;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.config.WxMaInMemoryConfig;

@Configuration
public class WxMaConfiguration {
	@Resource
	WxMaProperties properties;
	// 此处获取配置的方式可以改成你自己的方式，也可以注解等方式获取配置等。
//	private static final String appId = "wx058b46db3cd0e979";
//	private static final String secret = "7f4d1707caf9abaaf0b37aa03e6674f3";
	
	private static WxMaService wxMaService = null;
	
	@Bean
	public Object services(){
		WxMaInMemoryConfig config = new WxMaInMemoryConfig();
		config.setAppid(properties.getAppId());
		config.setSecret(properties.getSecret());
		
		wxMaService = new WxMaServiceImpl();
		wxMaService.setWxMaConfig(config);
		
		return Boolean.TRUE;
	}
	
	public static WxMaService getWxMaService(){
	 return wxMaService;
	}
}
