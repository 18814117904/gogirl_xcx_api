package com.gogirl.gogirl_user.constant;



import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * wechat mp properties
 *
 * @author Binary Wang(https://github.com/binarywang)
 */
@Component
@ConfigurationProperties(prefix = "gogirl")
public class GogirlProperties {
	@Bean
	public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        return restTemplate;
	}
	private String GOGIRLMP;
	private String notifyOrder;
	private String notifyCharge;
	private String notifyTimesCard;
	private String picturePath;
	public String getGOGIRLMP() {
		return GOGIRLMP;
	}
	public void setGOGIRLMP(String gOGIRLMP) {
		GOGIRLMP = gOGIRLMP;
	}
	public String getNotifyOrder() {
		return notifyOrder;
	}
	public void setNotifyOrder(String notifyOrder) {
		this.notifyOrder = notifyOrder;
	}
	public String getPicturePath() {
		return picturePath;
	}
	public void setPicturePath(String picturePath) {
		this.picturePath = picturePath;
	}
	public String getNotifyCharge() {
		return notifyCharge;
	}
	public void setNotifyCharge(String notifyCharge) {
		this.notifyCharge = notifyCharge;
	}
	public String getNotifyTimesCard() {
		return notifyTimesCard;
	}
	public void setNotifyTimesCard(String notifyTimesCard) {
		this.notifyTimesCard = notifyTimesCard;
	}


}
