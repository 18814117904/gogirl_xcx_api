package com.gogirl.gogirl_order.order_schedule.dao;

import com.gogirl.gogirl_order.order_commons.dto.ScheduleManage;
import com.gogirl.gogirl_order.order_commons.dto.ScheduleServe;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by yinyong on 2018/10/17.
 */
@Mapper
@Repository
public interface ScheduleServeMapper {

    List<ScheduleServe> listScheduleServe(@Param("departmentId") Integer departmentId, @Param("days") String days);

    List<ScheduleServe> listScheduleManageByDate(@Param("date")String date,@Param("departmentId")Integer departmentId);
    int updateScheduleServeByOne(ScheduleServe scheduleServe);

    int updateScheduleServe(List<ScheduleServe> list);

    List<ScheduleServe> checkScheduleServeByTechnicianId(Integer technicianId);

    int updateScheduleServeForTechnician(@Param("id") Integer id, @Param("technicianId") Integer technicianId);

    int insertScheduleServe(ScheduleServe scheduleServe);

    int deleteScheduleServeById(Integer id);

    int deleteScheduleServeByServeId(Integer serveId);

    int insertScheduleServeByOne(ScheduleServe scheduleServe);

    int insertScheduleServeByUpdate(List<ScheduleServe> list);

    List<ScheduleServe> listScheduleServeBySchId(Integer schId);

    ScheduleServe getCheckSchedule(@Param("startDateTime") String startDateTime, @Param("endDateTime") String endDateTime, @Param("technicianId") Integer technicianId);

    ScheduleServe getCheckScheduleNotContainItself(@Param("startDateTime") String startDateTime, @Param("endDateTime") String endDateTime, @Param("technicianId") Integer technicianId, @Param("scheduleServeId") Integer scheduleServeId);

	List<ScheduleServe> listExistScheduleServe(@Param("startDate")String startDate,@Param("customerId")Integer customerId);

}
