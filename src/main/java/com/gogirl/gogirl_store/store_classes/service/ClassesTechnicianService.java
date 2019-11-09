package com.gogirl.gogirl_store.store_classes.service;

import com.gogirl.gogirl_store.store_commons.dto.ClassesManage;
import com.gogirl.gogirl_store.store_commons.dto.ClassesTechnician;

import java.util.List;
import java.util.Map;

/**
 * Created by yinyong on 2018/9/20.
 */
public interface ClassesTechnicianService {

    List<ClassesTechnician> listClassesTechnicianForPage(ClassesTechnician classesTechnician);

    List<ClassesTechnician> listClassesTechnician(Integer departmentId, String days);

    List<ClassesTechnician> listClassesTechnicianDetail(Integer userId, String days);

    /*Map<String, Object> checkTimeForSchedule(String startDateTime, String startDate, String startTime, String endDateTime, Integer serveId, Integer produceId, Integer departmentId);*/

    List<ClassesTechnician> getReservableTime(String startDate, String startTime, Integer departmentId);

    int updateClassesTechnician(ClassesTechnician classesTechnician);

    int deleteClassesTechnicianById(Integer id);

    int insertClassesTechnician(ClassesTechnician classesTechnician);

    int updateClassesTechnicianByList(List<Map<String, Object>> list);

    int insertClassesTechnicianByStore(List<Map<String, Object>> list);

	List<ClassesTechnician> listClassesTechnicianDetailByDays(Integer userId,String days);
}
