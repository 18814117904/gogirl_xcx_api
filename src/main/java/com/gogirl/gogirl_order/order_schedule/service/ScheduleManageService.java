package com.gogirl.gogirl_order.order_schedule.service;

import com.gogirl.gogirl_order.order_commons.dto.ScheduleManage;
import com.gogirl.gogirl_order.order_commons.dto.ScheduleServe;
import com.gogirl.gogirl_order.order_commons.utils.JsonResult;

import java.util.List;
import java.util.Map;

/**
 * Created by yinyong on 2018/9/28.
 */
public interface ScheduleManageService {

    List<ScheduleManage> listScheduleManageForPage(ScheduleManage scheduleManage);

    Integer getScheduleNumber(Integer userId);

    int getTodaySummary(Integer departmentId);

    int getScheduleSummary(ScheduleManage scheduleManage);

    List<ScheduleServe> listScheduleServe(String startDateTime, String endDateTime, Integer departmentId);

    List<ScheduleServe> listScheduleServeNotContainItself(String startDateTime, String endDateTime, Integer departmentId, Integer scheduleServeId);
    List<ScheduleServe> listScheduleServeNotContainOrderId(String startDateTime, String endDateTime, Integer departmentId, Integer orderId);//xcx

    ScheduleManage getScheduleForDetail(Integer id);

    JsonResult getDetailAndRecord(Integer id);

    int updateSchedule(ScheduleManage scheduleManage);

    int updateScheduledStatusByTime(String dayTime);

    JsonResult insertScheduleAndServe(String startDateTime, Integer lengthTime, Integer userId, Integer departmentId, Integer technicianId, Integer serveId, String produceName, String serveName);

    JsonResult insertScheduleWithServeByStore (ScheduleManage scheduleManage);
    int insertScheduleManage (ScheduleManage scheduleManage);

    JsonResult deleteScheduleAndServe(Integer serveId, String delRemark);

	List<ScheduleManage> getListScheduleManageForPage(
			ScheduleManage scheduleManage);

	List<ScheduleServe> listExistScheduleServe(String startDate,
			Integer customerId);

	int deleteScheduleAndServeStatusDelete(Integer scheduleId);

	String createScheduleID(Integer scheduleID);

}
