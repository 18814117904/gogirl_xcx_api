package com.gogirl.gogirl_xcx.config;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * wxpay pay properties
 *
 * @author Binary Wang
 */
@Component
@ConfigurationProperties(prefix = "wx.ma")
public class WxMaProperties {

    /**
     * 设置微信小程序的appid
     */
    private String appId;

    /**
     * 设置微信小程序的app secret
     */
    private String secret;

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}



}
