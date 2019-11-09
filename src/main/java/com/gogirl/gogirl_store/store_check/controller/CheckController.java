package com.gogirl.gogirl_store.store_check.controller;

import com.github.qcloudsms.SmsSingleSenderResult;
import com.gogirl.gogirl_order.order_commons.utils.JsonResult;
import com.gogirl.gogirl_store.store_commons.jms.SmsConstant;
import com.gogirl.gogirl_store.store_commons.jms.SmsService;
import com.gogirl.gogirl_store.store_commons.utils.RandomUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yinyong on 2018/9/29.
 */
@RestController
@RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "check")
public class CheckController {

    @Autowired
    private SmsService smsService;

    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "loginCheck")
    public JsonResult loginCheck(String telphone, String code, HttpServletRequest request, HttpServletResponse response){
        JsonResult jsonResult = new JsonResult();
        Map<String, Object> map = (Map<String, Object>) request.getSession().getAttribute(telphone);
        if(!(StringUtils.isEmpty(map)) && !(StringUtils.isEmpty(map.get("code"))) && code.equals(map.get("code"))){
            Date date = (Date) map.get("date");
            long time = (new Date().getTime() - date.getTime())/1000/60;
            if(time > Integer.parseInt(SmsConstant.activeTime)){
                return jsonResult.setSuccess(JsonResult.CODE_ERROR).setMessage("验证码超时!");
            }
            if(code.equals(request.getSession().getAttribute("invalid"))){
                return jsonResult.setSuccess(JsonResult.CODE_ERROR).setMessage("此验证码已失效!");
            }
            request.getSession().setAttribute("invalid", map.get("code"));
            jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC);
        }
        return jsonResult;
    }

    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "getVerificationCode")
    public JsonResult getVerificationCode(String telphone, HttpServletRequest request, HttpServletResponse response){
        Map<String, Object> map = new HashMap<String, Object>();
        JsonResult jsonResult = new JsonResult();
        String code = RandomUtils.randomMessage();
        map.put("code", code);
        map.put("date", new Date());
        request.getSession().setAttribute(telphone,map);
        SmsSingleSenderResult senderResult = smsService.sendLoginSmsCode(telphone,code);
        jsonResult.setSuccess(true).setMessage(senderResult.errMsg);
        return jsonResult;
    }
}
