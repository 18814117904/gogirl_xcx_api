package com.gogirl.gogirl_order.order_schedule.service.impl;

import com.gogirl.gogirl_order.order_commons.config.Constans;
import com.gogirl.gogirl_order.order_commons.dto.ScheduleManage;
import com.gogirl.gogirl_order.order_commons.dto.ScheduleServe;
import com.gogirl.gogirl_order.order_commons.utils.BalanceLock;
import com.gogirl.gogirl_order.order_commons.utils.JsonResult;
import com.gogirl.gogirl_order.order_schedule.dao.ScheduleManageMapper;
import com.gogirl.gogirl_order.order_schedule.dao.ScheduleServeMapper;
import com.gogirl.gogirl_order.order_schedule.dao.ScheduleUpdateRecordMapper;
import com.gogirl.gogirl_order.order_schedule.service.ScheduleServeService;
import com.gogirl.gogirl_store.store_commons.dto.ClassesTechnician;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by yinyong on 2018/10/17.
 */
@Service
public class ScheduleServeServiceImpl implements ScheduleServeService {

    private final Logger logger = LoggerFactory.getLogger(ScheduleServeServiceImpl.class);

    @Autowired
    private ScheduleServeMapper scheduleServeMapper;

    @Autowired
    private ScheduleUpdateRecordMapper scheduleUpdateRecordMapper;

    @Autowired
    private ScheduleManageMapper scheduleManageMapper;

    @Value("${store-url}")
    private String storeUrl;

    @Value("${technician-url}")
    private String technicianUrl;

    @Value("${customer-url}")
    private String customerUrl;

    @Value("${end-time}")
    private String dayEndTime;

    @Autowired
    private RestTemplate restTemplate;
    @Override
    public List<ScheduleServe> listScheduleManageByDate(String date,Integer departmentId) {
        return scheduleServeMapper.listScheduleManageByDate(date,departmentId);
    }

    @Override
    public List<ScheduleServe> listScheduleServe(Integer departmentId, String days) {


        List<ScheduleServe> list = scheduleServeMapper.listScheduleServe(departmentId, days);

        for(ScheduleServe scheduleServe : list) {
            try {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String startDateTime = simpleDateFormat.format(scheduleServe.getStartTime());
                String endDateTime = simpleDateFormat.format(scheduleServe.getEndTime());
                String startDate = new SimpleDateFormat("yyyy-MM-dd").format(scheduleServe.getStartTime());
                String startTime = new SimpleDateFormat("HH:mm:ss").format(scheduleServe.getStartTime());
                MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
                map.add("startDate", startDate);
                map.add("startTime", startTime);
                map.add("departmentId", departmentId.toString());
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<MultiValueMap<String, Object>>(map, headers);
                ResponseEntity<JsonResult> responseEntity = restTemplate.postForEntity(storeUrl + "classestechnician/getReservableTime", entity, JsonResult.class);
                JsonResult jsonResult1 = responseEntity.getBody();
                //获取当前日期排班美甲师班次时间列表
                List<LinkedHashMap> listClassesTechnician = (List<LinkedHashMap>) jsonResult1.getData();
                //获取包含当前预约时间的已预约服务记录
                List<ScheduleServe> listScheduleServe = scheduleManageMapper.listScheduleServe(startDateTime, endDateTime, departmentId);
                List<LinkedHashMap> listTechnician = retureOptionalTechnician(listClassesTechnician, listScheduleServe);
                scheduleServe.setListTechnician(listTechnician);
            }catch(Exception e){
                e.printStackTrace();
            }
        }



        return list;
    }

    @Override
    public List<ScheduleServe> checkScheduleServeByTechnicianId(Integer technicianId) {
        return scheduleServeMapper.checkScheduleServeByTechnicianId(technicianId);
    }

    @Override
    @Transactional
    public int updateScheduleServeForTechnician(Integer id, Integer technicianId) {
        logger.info("向未指定美甲师预约指定美甲师：预约id：" + id + "美甲师id：" + technicianId);
        return scheduleServeMapper.updateScheduleServeForTechnician(id, technicianId);
    }

    @Override
    @Transactional
    public JsonResult updateScheduleServe(ScheduleManage scheduleManage) {

        ScheduleManage scheduleManage1 = scheduleManageMapper.getScheduleForDetail(scheduleManage.getId());

        try{
            MultiValueMap<String, Object> map= new LinkedMultiValueMap<String, Object>();
            map.add("phone", scheduleManage.getTelephone());
            map.add("storeRecordRealName", scheduleManage.getStoreScheduleUsername());
            map.add("source", String.valueOf(Constans.MEMBER_SCHEDULE));
            map.add("registerDepartment", String.valueOf(scheduleManage.getDepartmentId()));
            map.add("oldId", String.valueOf(scheduleManage1.getScheduledUser()));
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


        logger.info("修改-预约服务开始...");
        JsonResult jsonResult = new JsonResult();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<ScheduleServe> listUpdate = new ArrayList<ScheduleServe>();
        List<ScheduleServe> listInsert = new ArrayList<ScheduleServe>();
        String deleteServe = scheduleManage.getDeleteServe();
        if(deleteServe != null && !("".equals(deleteServe))){
              String scheduleServeId[] = deleteServe.split(",");
              for(String id : scheduleServeId){
                  scheduleUpdateRecordMapper.insertScheduleUpdateRecordByDelete(Integer.parseInt(id), Constans.SCHEDULE_RECORD_DELETE);
                  logger.info("记录删除预约服务数据: ID：" + id + "类型：删除");
                  int result = scheduleServeMapper.deleteScheduleServeById(Integer.parseInt(id));
                  logger.info("修改-删除预约下服务id：" + id);
              }
        }
        int index = 0;  //记录第几个服务
        String arriveTime = "";
        for(ScheduleServe scheduleServe : scheduleManage.getListScheduleServer()){
            index ++;
            List<String> lockList = new ArrayList<String>();
            try {
                Date startDateTimeFormat = scheduleServe.getStartTime();  //预约服务开始时间
                String startDateTime = simpleDateFormat.format(startDateTimeFormat);
                if(index == 1){
                    arriveTime = startDateTime;
                }
                String startDate = new SimpleDateFormat("yyyy-MM-dd").format(startDateTimeFormat);
                String startTime = new SimpleDateFormat("HH:mm:ss").format(startDateTimeFormat);
                Calendar cal = Calendar.getInstance();
                cal.setTime(startDateTimeFormat);
                cal.add(Calendar.MINUTE, scheduleServe.getLengthTimeForEndTime()); //时长
                Date endDateTime = cal.getTime();
                String endDateTimeFormat = simpleDateFormat.format(endDateTime);

                SimpleDateFormat lockSimpleDateFormat = new SimpleDateFormat("HH:mm:ss");
                Date lastEndLock = lockSimpleDateFormat.parse(dayEndTime);
                // 专用锁变量  防止改变开始时间
                String lockTime = lockSimpleDateFormat.format(startDateTimeFormat);
                Date lockStartTime = simpleDateFormat.parse(startDateTime);
                Date lockEndTime = simpleDateFormat.parse(endDateTimeFormat);
                //并发事务锁开始
                while(lockStartTime.before(lockEndTime) && lockSimpleDateFormat.parse(lockTime).before(lastEndLock)){
                    BalanceLock.getInsatance().lock(lockTime);
                    lockList.add(lockTime);
                    cal.setTime(lockStartTime);
                    cal.add(Calendar.MINUTE, 30);
                    lockTime = lockSimpleDateFormat.format(cal.getTime());
                    String addTime = simpleDateFormat.format(cal.getTime());
                    lockStartTime = simpleDateFormat.parse(addTime);
                }
             //   scheduleManage.setScheduledNo(createScheduleID());
                scheduleManage.setLastUpdateTime(new Date());
                scheduleManage.setScheduledTime(new Date());
                scheduleManage.setArriveTime(simpleDateFormat.parse(startDateTime));
                //预约状态为正在服务
                scheduleManage.setStatus(Constans.SCHEDULE_STATUS_SERVICE);
                MultiValueMap<String, Object> map= new LinkedMultiValueMap<String, Object>();
                map.add("startDate", startDate);
                map.add("startTime", startTime);
                map.add("departmentId", String.valueOf(scheduleManage.getDepartmentId()));
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<MultiValueMap<String, Object>>(map, headers);

                /*if(simpleDateFormat.parse(startDateTime).before(new Date())){
                    logger.info("修改预约错误：第"+index+"个服务选取时间"+startDateTime+"早于当前时间："+scheduleManage);
                    jsonResult.setSuccess(false).setMessage("修改预约错误：第"+index+"个服务选取时间"+startDateTime+"早于当前时间");
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();  //事务回滚
                    return jsonResult;
                }*/

                ResponseEntity<JsonResult> responseEntity = restTemplate.postForEntity(storeUrl + "classestechnician/getReservableTime", entity, JsonResult.class);
                JsonResult jsonResult1 = responseEntity.getBody();
                //获取当前日期排班美甲师排期时间列表
                List<ClassesTechnician> listClassesTechnician = (List<ClassesTechnician>) jsonResult1.getData();
                //  修改数据
                if(scheduleServe.getRecordType() == Constans.SCHEDULE_RECORD_UPDATE){
                    scheduleServe.setSchId(scheduleManage.getId());
                    if (simpleDateFormat.parse(startDateTime).after(new Date())) {
                    //检测当前美甲师是否已经被预约 -- 修改操作 不包含自身
                    ScheduleServe checkScheduleServe = scheduleServeMapper.getCheckScheduleNotContainItself(startDateTime, endDateTimeFormat, scheduleServe.getTechnicianId(), scheduleServe.getId());  //美甲师
                    if(checkScheduleServe != null){
                        logger.info("修改预约错误：第"+index+"个服务美甲师已被预约"+scheduleManage);
                        jsonResult.setSuccess(false).setMessage("修改预约错误：第"+index+"个服务美甲师已被预约");
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //事务回滚
                        return jsonResult;
                    }
                    //获取包含当前预约时间的已预约服务记录
                    List<ScheduleServe> listScheduleServe = scheduleManageMapper.listScheduleServeNotContainItself(startDateTime, endDateTimeFormat, scheduleManage.getDepartmentId(), scheduleServe.getId()); //所属店铺
                    if (listClassesTechnician.size() <= listScheduleServe.size()) {
                        logger.info("修改预约错误：第"+index+"个服务预约时间已被预约："+scheduleManage);
                        jsonResult.setSuccess(false).setMessage("修改预约错误：第"+index+"个服务预约时间已被预约");
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //事务回滚
                        return jsonResult;
                    }
                    }
                    scheduleServeMapper.updateScheduleServeByOne(scheduleServe);
                    listUpdate.add(scheduleServe);
                    logger.info("修改-修改预约服务：" + scheduleServe);
                    if(index < scheduleManage.getListScheduleServer().size()) {
                        continue;
                    }
                }else if (scheduleServe.getRecordType() == Constans.SCHEDULE_RECORD_ADD){  //  新增数据
                    scheduleServe.setSchId(scheduleManage.getId());
                    if (simpleDateFormat.parse(startDateTime).after(new Date())) {
                    //检测当前美甲师是否已经被预约 -- 新增操作
                    ScheduleServe checkScheduleServe = scheduleServeMapper.getCheckSchedule(startDateTime, endDateTimeFormat, scheduleServe.getTechnicianId());  //美甲师
                    if(checkScheduleServe != null){
                        logger.info("修改预约错误：第"+index+"个服务美甲师已被预约"+scheduleManage);
                        jsonResult.setSuccess(false).setMessage("修改预约错误：第"+index+"个服务美甲师已被预约");
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //事务回滚
                        return jsonResult;
                    }
                    //获取包含当前预约时间的已预约服务记录
                    List<ScheduleServe> listScheduleServe = scheduleManageMapper.listScheduleServe(startDateTime, endDateTimeFormat, scheduleManage.getDepartmentId()); //所属店铺
                    if (listClassesTechnician.size() <= listScheduleServe.size()) {
                        logger.info("修改预约错误：第"+index+"个服务预约时间已被预约："+scheduleManage);
                        jsonResult.setSuccess(false).setMessage("修改预约错误：第"+index+"个服务预约时间已被预约");
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //事务回滚
                        return jsonResult;
                    }
                    }
                    scheduleServeMapper.insertScheduleServeByOne(scheduleServe);
                    listInsert.add(scheduleServe);
                    logger.info("修改-新增预约服务：" + scheduleServe);
                    if(index < scheduleManage.getListScheduleServer().size()){
                    continue;
                    }
                }

                    //修改预约服务
                    /*if(listUpdate != null && listUpdate.size() > 0){
                        int resultUpdate = scheduleServeMapper.updateScheduleServe(listUpdate);
                        logger.info("修改-修改预约服务：" + listUpdate);
                    }*/

                    //新增预约服务  插入数据后返回主键 用于插入修改记录表中
                    /*if(listInsert != null && listInsert.size() > 0){
                        int resultInsert = scheduleServeMapper.insertScheduleServeByUpdate(listInsert);
                        logger.info("修改-新增预约服务：" + listInsert);
                    }*/

                    //预约主表数据修改
                    scheduleManage.setArriveTime(simpleDateFormat.parse(arriveTime));
                    int resultScheduleManage =  scheduleManageMapper.updateScheduleManage(scheduleManage);
                    logger.info("修改-预约用户名："+ scheduleManage.getStoreScheduleUsername() + "预约手机号："+scheduleManage.getTelephone());
                    listInsert.addAll(listUpdate);
                    //预约修改、新增数据存入记录表中   运用replace into插入数据  有数据先删除原数据再添加新数据
                    scheduleUpdateRecordMapper.insertScheduleServeRecord(listInsert);
                    logger.info("修改-预约服务记录数据：" + listInsert);
                    jsonResult.setSuccess(true).setMessage(JsonResult.APP_DEFINE_SUC);
            } catch (ParseException e) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //事务回滚
                e.printStackTrace();
                return jsonResult.setSuccess(JsonResult.CODE_ERROR).setMessage(e.getMessage());
            }finally {
                //释放并发事务锁
                for(String lockTime : lockList){
                    BalanceLock.getInsatance().unlock(lockTime);
                }
            }
        }


        try{
            MultiValueMap<String, Object> map= new LinkedMultiValueMap<String, Object>();
            map.add("phone", scheduleManage.getTelephone() == null ? "" : scheduleManage.getTelephone());
            map.add("storeRecordRealName", scheduleManage.getStoreScheduleUsername() == null ? "" : scheduleManage.getStoreScheduleUsername());
            map.add("source", String.valueOf(Constans.MEMBER_SCHEDULE));
            map.add("registerDepartment", String.valueOf(scheduleManage.getDepartmentId()));
            map.add("oldId", String.valueOf(scheduleManage1.getScheduledUser()));
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<MultiValueMap<String, Object>>(map, headers);
            ResponseEntity<JsonResult> responseEntity = restTemplate.postForEntity(customerUrl + "insertOrUpdateCustomer", entity, JsonResult.class);
            JsonResult jsonResult1 = responseEntity.getBody();
            LinkedHashMap memberUserMap = (LinkedHashMap)jsonResult1.getData();
        }catch (Exception e){
            logger.info("订单新增会员失败");
            /*TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //事务回滚*/
            e.printStackTrace();
        }
        jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC);
        return jsonResult;
    }

    @Override
    @Transactional
    public int deleteScheduleServeById(Integer id) {
        scheduleUpdateRecordMapper.insertScheduleUpdateRecordByDelete(id, Constans.SCHEDULE_RECORD_DELETE);
        int result = scheduleServeMapper.deleteScheduleServeById(id);
        return result;
    }

    @Override
    @Transactional
    public ScheduleServe insertScheduleServeByUpdate(ScheduleServe scheduleServe) {
     //   int resule = scheduleServeMapper.insertScheduleServeByUpdate(scheduleServe);
        return scheduleServe;
    }

    public List<LinkedHashMap> retureOptionalTechnician(List<LinkedHashMap> listClassesTechnician, List<ScheduleServe> listTechnicianServe){
        List<LinkedHashMap> list = new ArrayList<LinkedHashMap>();
        for(LinkedHashMap classesTechnician : listClassesTechnician){
            boolean classBoolean = true;
            for(ScheduleServe technicianServe : listTechnicianServe){
                if(classesTechnician.get("userId").equals(technicianServe.getTechnicianId())){
                    classBoolean = false;
                }
            }
            if(classBoolean){
                list.add(classesTechnician);
            }
        }
        return list;
    }
    @Override
    @Transactional
    public int insertListScheduleServe(List<ScheduleServe> list) {
        int resule = scheduleServeMapper.insertScheduleServeByUpdate(list);
        return resule;
    }

	@Override
	public int updateScheduleServe(ScheduleServe scheduleServe) {
		return scheduleServeMapper.updateScheduleServeByOne(scheduleServe);
	}
    
    
}
