package com.gogirl.gogirl_order.order_commons.service;

import com.alibaba.fastjson.JSONObject;
import com.gogirl.gogirl_order.order_commons.dto.ScheduleManage;
import com.gogirl.gogirl_order.order_commons.utils.JsonResult;
import com.gogirl.gogirl_order.order_commons.utils.WebSocketServer;
import com.gogirl.gogirl_order.order_schedule.dao.ScheduleManageMapper;
import com.gogirl.gogirl_order.order_schedule.service.ScheduleManageService;

import lombok.extern.slf4j.Slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by yinyong on 2018/9/25.
 */
@Slf4j
@Async
@Component
public class ScheduledService {

    private final static Logger logger = LoggerFactory.getLogger(ScheduledService.class);

    @Autowired
    private ScheduleManageService scheduleManageService;

    @Autowired
    private ScheduleManageMapper scheduleManageMapper;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${store-url}")
    private String storeUrl;

    @Value("${scheduled_remind}")
    private String scheduledRemind;

    @Value("${scheduled_template}")
    private String scheduledTemplate;

    /**
     * 定时修改预约状态
     */
    @Scheduled(cron="0 30 23 * * ?")
    public void schedule(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dayTime = simpleDateFormat.format(new Date());
        scheduleManageService.updateScheduledStatusByTime(dayTime);
    }

    /**
     * 预约消息推送
     */
    @Scheduled(cron="0 0/1 * * * ?")
    public void messageReminder() throws ParseException {

        //店铺端推送数据：新增+时效提醒数据总和 开始
        int readStatus = scheduleManageMapper.modifyScheduleReadStatus();
        Map<String, String> msgMap = new HashMap<String, String>();
        Map<String, String> raingMap = new HashMap<String, String>();
        List<Map> detailMap = scheduleManageMapper.getScheduleNoReadNumByGroup();
        String depId = "";
        for(Map map : detailMap){

            if(depId.equals(String.valueOf(map.get("departmentId")))){
                continue;
            }
            List<Map<String, String>> list = new ArrayList<Map<String, String>>();
            if("1".equals(String.valueOf(map.get("isRead")))) {
                msgMap.put("type", "1");
                msgMap.put("number", "0");
            }else {
                msgMap.put("type", "1");
                msgMap.put("number", String.valueOf(map.get("noReadNum")));
            }
                raingMap.put("type", "2");
                raingMap.put("number", String.valueOf(map.get("raingNum") == null ? "0" : map.get("raingNum")));

            list.add(msgMap);
            list.add(raingMap);
            try {
                String sm = JSONObject.toJSONString(list);
                WebSocketServer.sendMessageByTimmer(sm, String.valueOf(map.get("departmentId")));
            } catch (IOException e) {
                e.printStackTrace();
            }
            depId = String.valueOf(map.get("departmentId"));
        }
        //店铺端推送数据：时效提醒结束

        //公众号预约到期时效提醒 开始
        scheduleManageMapper.updateScheduleReminderStatus();
        List<ScheduleManage> listSchedule = scheduleManageMapper.listScheduleNoReminder();

        for(ScheduleManage scheduleManage : listSchedule) {
            if(scheduleManage.getCustomer() != null&&scheduleManage.getCustomer().getOpenid()!=null) {
                MultiValueMap<String, Object> messageMap= new LinkedMultiValueMap<String, Object>();
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                messageMap.add("openid", scheduleManage.getCustomer().getOpenid());
                messageMap.add("customerId", String.valueOf(scheduleManage.getCustomer().getId()));
                messageMap.add("scheduledId", String.valueOf(scheduleManage.getId()));
                messageMap.add("url", "");
                if(scheduleManage.getListScheduleServer().get(0).getProduceName() != null){
                    messageMap.add("serveName", scheduleManage.getListScheduleServer().get(0).getProduceName());
                }else{
                    messageMap.add("serveName", scheduleManage.getListScheduleServer().get(0).getServe().getName());
                }
                messageMap.add("time", new SimpleDateFormat("yyyy年MM月dd日 HH时mm分").format(scheduleManage.getArriveTime()));
                messageMap.add("storeAddress", scheduleManage.getDepartmentName());
                messageMap.add("remark", "无");
                try{
                    HttpEntity<MultiValueMap<String, Object>> messageEntity = new HttpEntity<MultiValueMap<String, Object>>(messageMap, headers);
                    ResponseEntity<JsonResult> responseEntity1 = restTemplate.postForEntity(scheduledRemind, messageEntity, JsonResult.class);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }else{
            	logger.info("预约到期该提醒,但是没有公众号openid");
            }
        }
        scheduleManageMapper.updateScheduleIsReminderStatus();
        //公众号预约到期时效提醒 结束
        //店铺端新增预约10分钟后 公众号推送消息提醒 开始
        scheduleManageMapper.updateScheduleStoreReminderStatus();

        List<ScheduleManage> detailList = scheduleManageMapper.listScheduleManageDetail();
        for(ScheduleManage scheduleManage : detailList) {
        	if(scheduleManage.getCustomer()==null||scheduleManage.getCustomer().getOpenid()==null){
        		continue;
        	}
            MultiValueMap<String, Object> storeMap = new LinkedMultiValueMap<String, Object>();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            storeMap.add("id", String.valueOf(scheduleManage.getDepartmentId()));
            HttpEntity<MultiValueMap<String, Object>> messageStoreEntity = new HttpEntity<MultiValueMap<String, Object>>(storeMap, headers);
            ResponseEntity<JsonResult> responseStoreEntity = restTemplate.postForEntity(storeUrl + "shop/queryShopForDetail", messageStoreEntity, JsonResult.class);
            JsonResult storeJsonResult = responseStoreEntity.getBody();
            LinkedHashMap shopManage = (LinkedHashMap) storeJsonResult.getData();

            MultiValueMap<String, Object> messageMap= new LinkedMultiValueMap<String, Object>();
            messageMap.add("openid", String.valueOf(scheduleManage.getCustomer().getOpenid()));
            messageMap.add("customerId", String.valueOf(scheduleManage.getCustomer().getId()));
            messageMap.add("scheduledId", String.valueOf(scheduleManage.getId()));
            messageMap.add("url", "");
            if(scheduleManage.getListScheduleServer().get(0).getProduceName() != null){
                messageMap.add("serveName", scheduleManage.getListScheduleServer().get(0).getProduceName());
            }else{
                messageMap.add("serveName", scheduleManage.getListScheduleServer().get(0).getServe().getName());
            }
            messageMap.add("time", new SimpleDateFormat("yyyy年MM月dd日HH时mm分").format(scheduleManage.getScheduledTime()));
            messageMap.add("storeName", shopManage.get("name"));
            messageMap.add("phone", shopManage.get("customerServiceTelphone"));
            messageMap.add("storeAddress", shopManage.get("address"));
            try{
                HttpEntity<MultiValueMap<String, Object>> messageEntity = new HttpEntity<MultiValueMap<String, Object>>(messageMap, headers);
                ResponseEntity<JsonResult> responseEntity1 = restTemplate.postForEntity(scheduledTemplate, messageEntity, JsonResult.class);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        scheduleManageMapper.updateScheduleStoreIsReminderStatus();
        //店铺端新增预约10分钟后 公众号推送提醒消息  结束
    }
}
