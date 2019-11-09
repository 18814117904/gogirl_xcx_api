package com.gogirl.gogirl_order.order_schedule.dao;

import com.gogirl.gogirl_order.order_commons.dto.ScheduleManage;
import com.gogirl.gogirl_order.order_commons.dto.ScheduleServe;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by yinyong on 2018/9/28.
 */
@Mapper
@Repository
public interface ScheduleManageMapper {

    List<ScheduleManage> listScheduleManageForPage(ScheduleManage scheduleManage);

    Integer getScheduleNumber(Integer userId);

    int getTodaySummary(@Param("departmentId") Integer departmentId);

    int getScheduleSummary(ScheduleManage scheduleManage);

    List<ScheduleManage> listScheduleNoReminder();

    List<ScheduleManage> listScheduleManageDetail();

    List<ScheduleServe> listScheduleServe(@Param("startDateTime") String startDateTime, @Param("endDateTime") String endDateTime, @Param("departmentId") Integer departmentId);
    
    List<ScheduleServe> listScheduleServeNotContainItself(@Param("startDateTime") String startDateTime, @Param("endDateTime") String endDateTime, @Param("departmentId") Integer departmentId, @Param("scheduleServeId") Integer scheduleServeId);
    List<ScheduleServe> listScheduleServeNotContainOrderId(@Param("startDateTime") String startDateTime, @Param("endDateTime") String endDateTime, @Param("departmentId") Integer departmentId, @Param("orderId") Integer orderId);

    ScheduleManage getScheduleForDetail(Integer id);

    ScheduleManage getScheduleRecordForDetail(Integer id);

    List<Map> getScheduleNoReadNumByGroup();

    List<Map> getScheduleNoRaingReadNumByGroup();

    ScheduleManage checkScheduledNo(String no);

    int deleteScheduleById(@Param("serveId") Integer serveId, @Param("delRemark") String delRemark);
    int deleteScheduleByIdStatus5(@Param("scheduleId") Integer scheduleId);

    int updateSchedule(ScheduleManage scheduleManage);

    int updateScheduledStatusByTime(String dayTime);

    int updateScheduleManage(ScheduleManage scheduleManage);

    int insertScheduleAndServe(ScheduleManage scheduleManage);

    int updatecheduleIsReadStatus(ScheduleManage scheduleManage);

    int updatescheduleRaingStatus(ScheduleManage scheduleManage);

    int modifyScheduleReadStatus();

    int updateScheduleReminderStatus();

    int updateScheduleStoreReminderStatus();

    int updateScheduleStoreIsReminderStatus();

    int updateScheduleIsReminderStatus();

    int getScheduleNoReadNum(@Param("departmentId") Integer departmentId);

	List<ScheduleManage> getListScheduleManageForPage(
			ScheduleManage scheduleManage);


	ScheduleManage getMaxScheduleNo();
	
	int insertScheduleManage(ScheduleManage scheduleManage);
}
