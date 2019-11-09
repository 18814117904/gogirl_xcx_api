package com.gogirl.gogirl_store.store_commons.jms;

import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import org.json.JSONException;
import org.springframework.stereotype.Service;

import javax.xml.ws.http.HTTPException;
import java.io.IOException;

@Service
public class SmsService {
	int appid = SmsConstant.appid;
	String appkey = SmsConstant.appkey;

	public SmsSingleSenderResult sendLoginSmsCode(String phoneNumber,String code) {
		String smsSign = SmsConstant.smsSign;
			int templateId = SmsConstant.loginTemplateId;
			String activeTime = SmsConstant.activeTime;

		    String[] params = {code,activeTime};
		    return sendSmsCode(phoneNumber, params, templateId, smsSign);
	}	
	public SmsSingleSenderResult sendBindSmsCode(String phoneNumber,String code) {
		String smsSign = SmsConstant.smsSign;
			int templateId = SmsConstant.bindTemplateId;
			String activeTime = SmsConstant.activeTime;

		    String[] params = {code,activeTime};
		    return sendSmsCode(phoneNumber, params, templateId, smsSign);
	}	
	private SmsSingleSenderResult sendSmsCode(String phoneNumber,String[] params,int templateId,String smsSign) {
		SmsSingleSenderResult result = new SmsSingleSenderResult();
		try {
		    SmsSingleSender ssender = new SmsSingleSender(appid, appkey);
		    result = ssender.sendWithParam("86", phoneNumber,
		    		templateId, params, smsSign, "", "");
		} catch (HTTPException e) {
		    // HTTP响应码错误
		    e.printStackTrace();
		    return result;
		} catch (JSONException e) {
		    // json解析错误
		    e.printStackTrace();
		    return result;
		} catch (IOException e) {
		    // 网络IO错误
		    e.printStackTrace();
		    return result;
		} catch (com.github.qcloudsms.httpclient.HTTPException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static String decodeUnicode(String unicode) {
        StringBuffer sb = new StringBuffer();
		String[] hex = unicode.split("\\\\u");
		for (int i = 1; i < hex.length; i++) {
			int data = Integer.parseInt(hex[i], 16);
			sb.append((char) data);
		}
		return sb.toString();
	}
}
