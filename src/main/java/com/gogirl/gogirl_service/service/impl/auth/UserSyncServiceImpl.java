package com.gogirl.gogirl_service.service.impl.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogirl.gogirl_service.entity.GetByBLUserAcct;
import com.gogirl.gogirl_service.service.service.auth.UserSyncService;
import com.gogirl.gogirl_service.utils.ApiDisposition;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import java.io.IOException;

@Service
public class UserSyncServiceImpl implements UserSyncService {

    private static Logger logger = LoggerFactory.getLogger(UserSyncServiceImpl.class);

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public GetByBLUserAcct getUserByCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String BLUserAcct = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("BLUserAcct".equals(cookie.getName())) {
                    BLUserAcct = cookie.getValue();
                }
            }
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> params= new LinkedMultiValueMap<String, String>();
        params.add("BLUserAcct",BLUserAcct);
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<MultiValueMap<String, String>>(params, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(ApiDisposition.LOGING_USER_API, httpEntity, String.class);


        ObjectMapper mapper = new ObjectMapper();
        String strBody = null;
        if (response != null && response.getStatusCodeValue() == 200) {
            strBody = response.getBody();
        }
        strBody=strBody.toLowerCase();
        logger.info("从 oa 系统获取登陆用户信息："+strBody);
        GetByBLUserAcct getByBLUserAcct = null;
        try {
            getByBLUserAcct = mapper.readValue(strBody, GetByBLUserAcct.class);
        } catch (IOException e) {
            logger.error(strBody+"转换失败");
            e.printStackTrace();
        }
        return getByBLUserAcct;
    }
}
