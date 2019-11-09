package com.gogirl.gogirl_order.order_schedule.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.gogirl.gogirl_order.order_commons.config.Constans;
import com.gogirl.gogirl_order.order_commons.dto.*;
import com.gogirl.gogirl_order.order_commons.utils.*;
import com.gogirl.gogirl_order.order_schedule.dao.ScheduleManageMapper;
import com.gogirl.gogirl_order.order_schedule.dao.ScheduleServeMapper;
import com.gogirl.gogirl_order.order_schedule.dao.ScheduleUpdateRecordMapper;
import com.gogirl.gogirl_order.order_schedule.service.ScheduleManageService;
import com.gogirl.gogirl_store.store_classes.service.ClassesTechnicianService;
import com.gogirl.gogirl_store.store_commons.dto.ClassesTechnician;
import com.gogirl.gogirl_store.store_commons.dto.ShopManage;
import com.gogirl.gogirl_store.store_shop.service.ShopManageService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.annotation.Resource;

/**
 * Created by yinyong on 2018/9/28.
 */
@Service
public class ScheduleManageServiceImpl implements ScheduleManageService {

    private final static Logger logger = LoggerFactory.getLogger(ScheduleManageServiceImpl.class);

    @Autowired
    private ScheduleManageMapper scheduleManageMapper;

    @Autowired
    private ScheduleServeMapper scheduleServeMapper;

    @Autowired
    private ScheduleUpdateRecordMapper scheduleUpdateRecordMapper;
	@Resource
	ShopManageService shopManageService;

    @Value("${store-url}")
    private String storeUrl;

    @Value("${technician-url}")
    private String technicianUrl;

    @Value("${customer-url}")
    private String customerUrl;

    @Value("${end-time}")
    private String dayEndTime;

    @Value("${scheduled_template}")
    private String scheduledTemplate;

    @Autowired
    private RestTemplate restTemplate;

    @Resource
    ClassesTechnicianService classesTechnicianService;
    @Override
    @Transactional
    public int deleteScheduleAndServeStatusDelete(Integer scheduleId) {
    	int row = scheduleManageMapper.deleteScheduleByIdStatus5(scheduleId);
    	scheduleServeMapper.deleteScheduleServeByServeId(scheduleId);
        return row;
    }

    
    @Override
    public List<ScheduleManage> getListScheduleManageForPage(ScheduleManage scheduleManage) {
        scheduleManageMapper.updatecheduleIsReadStatus(scheduleManage);
        scheduleManageMapper.updatescheduleRaingStatus(scheduleManage);
        return scheduleManageMapper.getListScheduleManageForPage(scheduleManage);
    }
    @Override
    public List<ScheduleManage> listScheduleManageForPage(ScheduleManage scheduleManage) {
        scheduleManageMapper.updatecheduleIsReadStatus(scheduleManage);
        scheduleManageMapper.updatescheduleRaingStatus(scheduleManage);
        return scheduleManageMapper.listScheduleManageForPage(scheduleManage);
    }
    
    @Override
    public Integer getScheduleNumber(Integer userId) {
        return scheduleManageMapper.getScheduleNumber(userId);
    }

    @Override
    public int getTodaySummary(Integer departmentId) {
        return scheduleManageMapper.getTodaySummary(departmentId);
    }

    @Override
    public int getScheduleSummary(ScheduleManage scheduleManage) {
        return scheduleManageMapper.getScheduleSummary(scheduleManage);
    }

    @Override
    public List<ScheduleServe> listScheduleServe(String startDateTime, String endDateTime, Integer departmentId) {
        return scheduleManageMapper.listScheduleServe(startDateTime, endDateTime, departmentId);
    }

    @Override
    public List<ScheduleServe> listScheduleServeNotContainItself(String startDateTime, String endDateTime, Integer departmentId, Integer scheduleServeId) {
        return scheduleManageMapper.listScheduleServeNotContainItself(startDateTime, endDateTime, departmentId, scheduleServeId);
    }

    @Override
    public ScheduleManage getScheduleForDetail(Integer id) {
        return scheduleManageMapper.getScheduleForDetail(id);
    }

    @Override
    public JsonResult getDetailAndRecord(Integer id) {
        JsonResult jsonResult = new JsonResult();
        Map<String, Object> map = new HashMap<String, Object>();
        ScheduleRecordManage scheduleRecordManage = scheduleUpdateRecordMapper.getScheduleRecordForDetail(id);
        jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC).setData(scheduleRecordManage);
        return jsonResult;
    }

    @Override
    @Transactional()
    public JsonResult insertScheduleAndServe(String startDateTime, Integer lengthTime, Integer userId, Integer departmentId, Integer technicianId, Integer serveId, String produceName, String serveName) {
        JsonResult jsonResult = new JsonResult();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<String> lockList = new ArrayList<String>();
        ScheduleManage scheduleManage = new ScheduleManage();
        try {
            Date startDateTimeFormat = simpleDateFormat.parse(startDateTime);
            String startDate = new SimpleDateFormat("yyyy-MM-dd").format(startDateTimeFormat);
            String startTime = new SimpleDateFormat("HH:mm:ss").format(startDateTimeFormat);
            Calendar cal = Calendar.getInstance();
            cal.setTime(startDateTimeFormat);
            cal.add(Calendar.MINUTE, lengthTime);
            Date endDateTime = cal.getTime();
            String endDateTimeFormat = simpleDateFormat.format(endDateTime);

            SimpleDateFormat lockSimpleDateFormat = new SimpleDateFormat("HH:mm:ss");
            Date lastEndLock = lockSimpleDateFormat.parse(dayEndTime);
            String lockTime = lockSimpleDateFormat.format(startDateTimeFormat);
            Date lockStartTime = simpleDateFormat.parse(startDateTime);
            Date lockEndTime = simpleDateFormat.parse(endDateTimeFormat);
            while(lockStartTime.before(lockEndTime) && lockSimpleDateFormat.parse(lockTime).before(lastEndLock)){
                BalanceLock.getInsatance().lock(lockTime);
                lockList.add(lockTime);
                cal.setTime(lockStartTime);
                cal.add(Calendar.MINUTE, 30);
                lockTime = lockSimpleDateFormat.format(cal.getTime());
                String addTime = simpleDateFormat.format(cal.getTime());
                lockStartTime = simpleDateFormat.parse(addTime);
            }

            scheduleManage.setScheduledNo(createScheduleID(departmentId));
            scheduleManage.setScheduledUser(userId);
            scheduleManage.setLastUpdateTime(new Date());
            scheduleManage.setScheduledTime(new Date());
            scheduleManage.setArriveTime(simpleDateFormat.parse(startDateTime));
            scheduleManage.setDepartmentId(departmentId);
            scheduleManage.setScheduledType(Constans.SCHEDULE_TYPE_USER);
            scheduleManage.setStatus(Constans.SCHEDULE_STATUS_SERVICE);
            scheduleManage.setIsRead(Constans.SCHEDULE_NO_READ);
//            MultiValueMap<String, Object> map= new LinkedMultiValueMap<String, Object>();
//            map.add("startDate", startDate);
//            map.add("startTime", startTime);
//            map.add("departmentId", departmentId.toString());
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//            HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<MultiValueMap<String, Object>>(map, headers);

            if(simpleDateFormat.parse(startDateTime).before(new Date())){
                logger.info("选取时间早于当前时间："+scheduleManage);
                jsonResult.setSuccess(JsonResult.CODE_ERROR).setMessage("选取时间早于当前时间");
                return jsonResult;
            }
//            ResponseEntity<JsonResult> responseEntity = restTemplate.postForEntity(storeUrl + "classestechnician/getReservableTime", entity, JsonResult.class);
//            JsonResult jsonResult1 = responseEntity.getBody();
            //获取当前日期排班美甲师班次时间列表
            List<ClassesTechnician> listClassesTechnician = classesTechnicianService.getReservableTime(startDate, startTime, scheduleManage.getDepartmentId());
            //检测当前美甲师是否已经被预约
            ScheduleServe checkScheduleServe = scheduleServeMapper.getCheckSchedule(startDateTime, endDateTimeFormat, technicianId);
            if(checkScheduleServe != null){
                logger.info("该美甲师已被预约："+scheduleManage);
                jsonResult.setSuccess(JsonResult.CODE_ERROR).setMessage("该美甲师已被预约");
                return jsonResult;
            }
            //获取包含当前预约时间的已预约服务记录
            List<ScheduleServe> listScheduleServe = scheduleManageMapper.listScheduleServe(startDateTime, endDateTimeFormat, departmentId);
            if (listClassesTechnician.size() <= listScheduleServe.size()) {
                logger.info("预约时间已被预约："+scheduleManage);
                jsonResult.setSuccess(JsonResult.CODE_ERROR).setMessage("预约时间已被预约");
                return jsonResult;
            }
            int resultScheduleManage = scheduleManageMapper.insertScheduleAndServe(scheduleManage);
            jsonResult.setData(resultScheduleManage);
            logger.info("新增预约数据："+scheduleManage);
            ScheduleServe scheduleServe = new ScheduleServe();
            scheduleServe.setSchId(scheduleManage.getId());
            scheduleServe.setTechnicianId(technicianId);
            scheduleServe.setServeId(serveId);
            scheduleServe.setStartTime(startDateTimeFormat);
            scheduleServe.setEndTime(endDateTime);
            scheduleServe.setCreateTime(new Date());
            scheduleServe.setProduceName(produceName);
            scheduleServe.setServeNumber(1);
            int resultScheduleServe = scheduleServeMapper.insertScheduleServe(scheduleServe);
            logger.info("新增预约服务："+scheduleServe);
            //记录用户创建预约服务 type为0 原始用户数据
            ScheduleUpdateRecord scheduleUpdateRecord = new ScheduleUpdateRecord();
            //预约服务记录类型为 3 则为原始数据
            scheduleUpdateRecord.setType(Constans.SCHEDULE_RECORD_OLD);
            BeanUtils.copyProperties(scheduleServe, scheduleUpdateRecord);
            //预约服务数据存入记录表
            scheduleUpdateRecordMapper.insertScheduleServeRecordByUser(scheduleUpdateRecord);
            logger.info("新增预约服务记录数据："+scheduleUpdateRecord);
            jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC);

            int readStatus = scheduleManageMapper.modifyScheduleReadStatus();
            Map<String, String> msgMap = new HashMap<String, String>();
            Map<String, String> raingMap = new HashMap<String, String>();
            List<Map> detailMap = scheduleManageMapper.getScheduleNoReadNumByGroup();
            String depId = "";
            for(Map map3 : detailMap){  //noAgingReadNum departmentId  isRead

                if(depId.equals(String.valueOf(map3.get("departmentId")))){
                    continue;
                }
                List<Map<String, String>> list = new ArrayList<Map<String, String>>();
                msgMap.put("type", "1");
                msgMap.put("number", String.valueOf(map3.get("noReadNum")));

                raingMap.put("type", "2");
                raingMap.put("number", String.valueOf(map3.get("raingNum")));

                list.add(msgMap);
                list.add(raingMap);
                try {
                    String sm = JSONObject.toJSONString(list);
                    WebSocketServer.sendMessageByTimmer(sm, String.valueOf(map3.get("departmentId")));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                depId = String.valueOf(map3.get("departmentId"));
            }
           /* Map<String, String> msgMap = new HashMap<String, String>();
            int sum = scheduleManageMapper.getScheduleNoReadNum(departmentId);
            msgMap.put("type", "1");
            msgMap.put("number", String.valueOf(sum));
            try {
                WebSocketServer.sendMessageByTimmer(JSONObject.toJSONString(msgMap), String.valueOf(departmentId));
                logger.info("预约消息推送：部门："+ departmentId + "未读数量：" + sum);
            } catch (IOException e) {
                e.printStackTrace();
            }*/
        } catch (ParseException e) {
            e.printStackTrace();
            return jsonResult.setMessage(e.getMessage());
        }finally {
            for(String lockTime : lockList){
                BalanceLock.getInsatance().unlock(lockTime);
            }
        }

        try{
            MultiValueMap<String, Object> map= new LinkedMultiValueMap<String, Object>();
            map.add("id", String.valueOf(userId));
            map.add("source", String.valueOf(Constans.MEMBER_WEI));
            map.add("registerDepartment", String.valueOf(departmentId));
            map.add("oldId", "");
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<MultiValueMap<String, Object>>(map, headers);
            ResponseEntity<JsonResult> responseEntity = restTemplate.postForEntity(customerUrl + "insertOrUpdateCustomer", entity, JsonResult.class);
            JsonResult jsonResult1 = responseEntity.getBody();
            LinkedHashMap memberUserMap = (LinkedHashMap) jsonResult1.getData();
            logger.info("店铺端预约-会员操作");

            if(null != memberUserMap.get("openid") && !("".equals(memberUserMap.get("openid")))) {
                MultiValueMap<String, Object> storeMap = new LinkedMultiValueMap<String, Object>();
                storeMap.add("id", String.valueOf(departmentId));
                HttpEntity<MultiValueMap<String, Object>> messageStoreEntity = new HttpEntity<MultiValueMap<String, Object>>(storeMap, headers);
                ResponseEntity<JsonResult> responseStoreEntity = restTemplate.postForEntity(storeUrl + "shop/queryShopForDetail", messageStoreEntity, JsonResult.class);
                JsonResult storeJsonResult = responseStoreEntity.getBody();
                LinkedHashMap shopManage = (LinkedHashMap) storeJsonResult.getData();

                MultiValueMap<String, Object> messageMap= new LinkedMultiValueMap<String, Object>();
                String memberUserOpenId = (String) memberUserMap.get("openid");
                String memberUserId = String.valueOf(memberUserMap.get("id"));
                messageMap.add("openid", memberUserOpenId);
                messageMap.add("customerId", memberUserId);
                messageMap.add("scheduledId", String.valueOf(scheduleManage.getId()));
                messageMap.add("url", "");
                messageMap.add("serveName", produceName == null ? serveName : produceName);
                messageMap.add("time", new SimpleDateFormat("yyyy年MM月dd日HH时mm分").format(simpleDateFormat.parse(startDateTime)));
                messageMap.add("storeName", shopManage.get("name"));
                messageMap.add("phone", shopManage.get("customerServiceTelphone"));
                messageMap.add("storeAddress", shopManage.get("address"));
                HttpEntity<MultiValueMap<String, Object>> messageEntity = new HttpEntity<MultiValueMap<String, Object>>(messageMap, headers);
                ResponseEntity<JsonResult> responseEntity1 = restTemplate.postForEntity(scheduledTemplate, messageEntity, JsonResult.class);
                logger.info("公众号信息提醒");
            }
        }catch (Exception e){
            logger.error("用户端新增预约会员失败");
            e.printStackTrace();
        }
        return jsonResult;
    }

    @Override
    @Transactional
    public JsonResult insertScheduleWithServeByStore(ScheduleManage scheduleManage) {
        JsonResult jsonResult = new JsonResult();
        logger.info("店铺端新增预约服务开始...");

        try{
            MultiValueMap<String, Object> map= new LinkedMultiValueMap<String, Object>();
            map.add("phone", scheduleManage.getTelephone());
            map.add("storeRecordRealName", null);
            map.add("source", String.valueOf(Constans.MEMBER_SCHEDULE));
            map.add("registerDepartment", String.valueOf(scheduleManage.getDepartmentId()));
            map.add("oldId", "");
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<MultiValueMap<String, Object>>(map, headers);
            ResponseEntity<JsonResult> responseEntity = restTemplate.postForEntity(customerUrl + "insertOrUpdateCustomer", entity, JsonResult.class);
            JsonResult jsonResult1 = responseEntity.getBody();
            LinkedHashMap memberUserMap = (LinkedHashMap) jsonResult1.getData();
            if(memberUserMap.get("id") != null && !("".equals(memberUserMap.get("id")))){
                scheduleManage.setScheduledUser((Integer) memberUserMap.get("id"));
            }
            logger.info("店铺端预约-会员操作");
        }catch (Exception e){
            logger.info("店铺端预约新增会员失败");
            e.printStackTrace();
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int indexs = 0;
        for(ScheduleServe scheduleServe : scheduleManage.getListScheduleServer()){
            indexs ++;
	        List<String> lockList = new ArrayList<String>();
	        try {
	            Date startDateTimeFormat = scheduleServe.getStartTime();  //预约服务开始时间
	            String startDateTime = null;
	            if(startDateTimeFormat!=null){
	            	startDateTime = simpleDateFormat.format(startDateTimeFormat);  //格式化年月日时分秒
	            }
	            
	            String startDate = new SimpleDateFormat("yyyy-MM-dd").format(startDateTimeFormat); //格式化年月日
	            String startTime = new SimpleDateFormat("HH:mm:ss").format(startDateTimeFormat);   //格式化时分秒
	            Calendar cal = Calendar.getInstance();
	            cal.setTime(startDateTimeFormat);
	            cal.add(Calendar.MINUTE, scheduleServe.getLengthTimeForEndTime()); //时长
	            Date endDateTime = cal.getTime();
	            String endDateTimeFormat = simpleDateFormat.format(endDateTime);  //预约服务结束时间
	
	            SimpleDateFormat lockSimpleDateFormat = new SimpleDateFormat("HH:mm:ss");
	            Date lastEndLock = lockSimpleDateFormat.parse("22:30:00");
	            String lockTime = lockSimpleDateFormat.format(startDateTimeFormat); //同步锁的开始时间字符串
	            Date lockStartTime = simpleDateFormat.parse(startDateTime);    // 同步锁的开始时间
	            Date lockEndTime = simpleDateFormat.parse(endDateTimeFormat);  // 同步锁的结束时间
	            while (lockStartTime.before(lockEndTime) && lockSimpleDateFormat.parse(lockTime).before(lastEndLock)) {  //后面的作为判断是否已经加到最后的时间点
	                BalanceLock.getInsatance().lock(lockTime);
	                lockList.add(lockTime);
	                cal.setTime(lockStartTime);
	                cal.add(Calendar.MINUTE, 30);
	                lockTime = lockSimpleDateFormat.format(cal.getTime());
	                String addTime = simpleDateFormat.format(cal.getTime());
	                lockStartTime = simpleDateFormat.parse(addTime);
	            }
	            scheduleManage.setScheduledNo(createScheduleID(scheduleManage.getDepartmentId()));
	            //   scheduleManage.setScheduledUser(userId);
	            //预约最新更改时间  用于预约详情中展示
	            scheduleManage.setLastUpdateTime(new Date());
	            scheduleManage.setScheduledTime(new Date());
	            if (indexs == 1) {
	                scheduleManage.setArriveTime(simpleDateFormat.parse(startDateTime));
	            }
	            //预约状态
	            scheduleManage.setStatus(Constans.SCHEDULE_STATUS_WAITSERVE);
	            /*if(simpleDateFormat.parse(startDateTime).before(new Date())){
	                logger.info("新增预约错误：第"+indexs+"个服务选取时间"+startDateTime+"早于当前时间："+scheduleManage);
	                jsonResult.setSuccess(false).setMessage("新增预约错误：第"+indexs+"个服务选取时间"+startDateTime+"早于当前时间");
	                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();  //事务回滚
	                return jsonResult;
	            }*/
	            if (simpleDateFormat.parse(startDateTime).after(new Date())) {
//	            ResponseEntity<JsonResult> responseEntity = restTemplate.postForEntity(storeUrl + "classestechnician/getReservableTime", entity, JsonResult.class);
//	            JsonResult jsonResult1 = responseEntity.getBody();
	            //获取当前日期排班美甲师班次时间列表
	            List<ClassesTechnician> listClassesTechnician = classesTechnicianService.getReservableTime(startDate, startTime, scheduleManage.getDepartmentId());
	            //检测当前美甲师是否已经被预约
	            ScheduleServe checkScheduleServe = scheduleServeMapper.getCheckSchedule(startDateTime, endDateTimeFormat, scheduleServe.getTechnicianId());  //美甲师
	            if (checkScheduleServe != null) {
	                logger.info("新增预约错误：第" + indexs + "该美甲师已被预约" + scheduleManage);
	                jsonResult.setSuccess(false).setMessage("抱歉：第" + indexs + "个服务的美甲师刚刚被预约");
	                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //事务回滚
	                return jsonResult;
	            }
	            //获取包含当前预约时间的已预约服务记录
	            List<ScheduleServe> listScheduleServe = scheduleManageMapper.listScheduleServe(startDateTime, endDateTimeFormat, scheduleManage.getDepartmentId()); //所属店铺
	            if (listClassesTechnician.size() <= listScheduleServe.size()) {
	                logger.info("新增预约错误：第" + indexs + "个服务预约时间已被预约：" + scheduleManage);
	                jsonResult.setSuccess(false).setMessage("抱歉：第" + indexs + "个服务预约时间刚刚被预约");
	                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //事务回滚
	                return jsonResult;
	            }
	        }
            // 第一次循环时插入预约主表数据
            if(indexs == 1){
                scheduleManage.setScheduledType(Constans.SCHEDULE_TYPE_PC);
                int resultScheduleManage = scheduleManageMapper.insertScheduleAndServe(scheduleManage);
                jsonResult.setData(scheduleManage.getId());
            }
            logger.info("新增预约数据："+scheduleManage);
            scheduleServe.setSchId(scheduleManage.getId());
            scheduleServe.setStartTime(startDateTimeFormat);
            scheduleServe.setEndTime(endDateTime);
            scheduleServe.setCreateTime(new Date());
            scheduleServe.setServeNumber(1);
            int resultScheduleServe = scheduleServeMapper.insertScheduleServe(scheduleServe);
            logger.info("新增预约服务："+scheduleServe);
            ScheduleUpdateRecord scheduleUpdateRecord = new ScheduleUpdateRecord();
            //预约服务记录类型为 3 则为原始数据
            scheduleUpdateRecord.setType(Constans.SCHEDULE_RECORD_OLD);
            BeanUtils.copyProperties(scheduleServe, scheduleUpdateRecord);
            //预约服务数据存入记录表
            scheduleUpdateRecordMapper.insertScheduleServeRecordByUser(scheduleUpdateRecord);
            logger.info("新增预约服务记录数据："+scheduleUpdateRecord);
            jsonResult.setSuccess(true).setMessage(JsonResult.APP_DEFINE_SUC);
        } catch (ParseException e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //事务回滚
            e.printStackTrace();
            return jsonResult.setSuccess(JsonResult.CODE_ERROR).setMessage(e.getMessage());
        }finally {
            for(String lockTime : lockList){
                BalanceLock.getInsatance().unlock(lockTime);
            }
        }
        }
        return jsonResult;
    }

    @Override
    @Transactional
    public JsonResult deleteScheduleAndServe(Integer serveId, String delRemark) {
        JsonResult jsonResult = new JsonResult();
        ScheduleManage scheduleManage = scheduleManageMapper.getScheduleForDetail(serveId);
        scheduleManageMapper.deleteScheduleById(serveId, delRemark);
     //   scheduleServeMapper.deleteScheduleServeByServeId(serveId);
        try{
            MultiValueMap<String, Object> map= new LinkedMultiValueMap<String, Object>();
            map.add("phone", scheduleManage.getTelephone() == null ? "" : scheduleManage.getTelephone());
            map.add("customerId", String.valueOf(scheduleManage.getScheduledUser()));
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<MultiValueMap<String, Object>>(map, headers);
            ResponseEntity<JsonResult> responseEntity = restTemplate.postForEntity(customerUrl + "minusScheduledTimes", entity, JsonResult.class);
            JsonResult jsonResult1 = responseEntity.getBody();
            logger.info("删除预约调用用户次数减1成功");
        }catch (Exception e){
            logger.info("预约会员减少次数失败");
            e.printStackTrace();
        }
        return jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC);
    }

    @Override
    @Transactional
    public int updateSchedule(ScheduleManage scheduleManage) {
        return scheduleManageMapper.updateSchedule(scheduleManage);
    }

    @Override
    @Transactional
    public int updateScheduledStatusByTime(String dayTime) {
        return scheduleManageMapper.updateScheduledStatusByTime(dayTime);
    }
    @Override
    public String createScheduleID(Integer scheduleID){
    	ShopManage shop = null;
    	if(scheduleID!=null){
    		shop = shopManageService.getShopManageForDetail(scheduleID);
    	}
    	String orderNo = "SCH";
    	if(shop!=null&&shop.getShortCode()!=null){
    		orderNo = shop.getShortCode();
    	}
    	SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
    	orderNo+= sdf.format(new Date());
    	ScheduleManage maxOrder = scheduleManageMapper.getMaxScheduleNo();
    	if(maxOrder==null){
    		orderNo+="0001";
    	}else{
    		String serialNumber = maxOrder.getScheduledNo().substring(maxOrder.getScheduledNo().length()-4);
    		try {

    		    Integer intHao = Integer.parseInt(serialNumber);
    		    intHao++;
    		    String strHao = intHao.toString();
    		    while (strHao.length() < 4)
    		        strHao = "0" + strHao;
    		    orderNo+=strHao;
			} catch (Exception e) {
				orderNo+="0001";
			}
    	}
        return orderNo;
    }
//    public String createScheduleID(){
//        String tole = RandomUtils.random("SCH");
//        ScheduleManage scheduleManage = scheduleManageMapper.checkScheduledNo(tole);
//        while(null != scheduleManage && null != scheduleManage.getId()){
//            tole = RandomUtils.random("SCH");
//            scheduleManage = scheduleManageMapper.checkScheduledNo(tole);
//        }
//        return tole;
//    }
	@Override
	public List<ScheduleServe> listScheduleServeNotContainOrderId(String startDateTime, String endDateTime, Integer departmentId,Integer orderId) {
		return scheduleManageMapper.listScheduleServeNotContainOrderId(startDateTime, endDateTime, departmentId, orderId);
	}
	@Override
	public List<ScheduleServe> listExistScheduleServe(String startDate,Integer customerId) {
		return scheduleServeMapper.listExistScheduleServe(startDate,customerId);
	}


	@Override
	public int insertScheduleManage(ScheduleManage scheduleManage) {
		scheduleManageMapper.insertScheduleManage(scheduleManage);
		return scheduleManage.getId();
	}
}
