package com.gogirl.gogirl_order.order_schedule.dao;

import com.gogirl.gogirl_order.order_commons.dto.ScheduleRecordManage;
import com.gogirl.gogirl_order.order_commons.dto.ScheduleServe;
import com.gogirl.gogirl_order.order_commons.dto.ScheduleUpdateRecord;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by yinyong on 2018/10/17.
 */
@Mapper
@Repository
public interface ScheduleUpdateRecordMapper {

  //  List<ScheduleUpdateRecord> listScheduleServeRecord(Integer id);
    ScheduleRecordManage getScheduleRecordForDetail(Integer id);

    int insertScheduleServeRecordByUser(ScheduleUpdateRecord scheduleUpdateRecord);

    int insertScheduleServeRecord(List<ScheduleServe> list);

    int insertScheduleUpdateRecordByDelete(@Param("id") Integer id, @Param("type") Integer type);

}
