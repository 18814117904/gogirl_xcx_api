package com.gogirl.gogirl_xcx.config;

import java.nio.charset.StandardCharsets;

import javax.annotation.Resource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


@Component
@ConfigurationProperties(prefix = "gogirl.url")
public class RouteConfig {
	

	
	//错误页面
	public static String ERROR_PAGE;

	@Bean
	public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        return restTemplate;
	}
}
