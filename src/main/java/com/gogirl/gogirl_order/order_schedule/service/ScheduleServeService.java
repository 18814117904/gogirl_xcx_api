package com.gogirl.gogirl_order.order_schedule.service;

import com.gogirl.gogirl_order.order_commons.dto.ScheduleManage;
import com.gogirl.gogirl_order.order_commons.dto.ScheduleServe;
import com.gogirl.gogirl_order.order_commons.utils.JsonResult;

import java.util.List;

/**
 * Created by yinyong on 2018/10/17.
 */
public interface ScheduleServeService {

    List<ScheduleServe> listScheduleServe(Integer departmentId, String days);

    List<ScheduleServe> checkScheduleServeByTechnicianId(Integer technicianId);

    int updateScheduleServeForTechnician(Integer id, Integer technicianId);

    JsonResult updateScheduleServe(ScheduleManage scheduleManage);

    int deleteScheduleServeById(Integer id);

    ScheduleServe insertScheduleServeByUpdate(ScheduleServe scheduleServe);
    public List<ScheduleServe> listScheduleManageByDate(String date,Integer departmentId);

	int insertListScheduleServe(List<ScheduleServe> list);

    int updateScheduleServe(ScheduleServe scheduleServe);
}
