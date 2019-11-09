package com.gogirl.gogirl_store.store_classes.dao;

import com.gogirl.gogirl_store.store_commons.dto.ClassesTechnician;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by yinyong on 2018/9/20.
 */
@Mapper
@Repository
public interface ClassesTechnicianMapper {

    List<ClassesTechnician> listClassesTechnicianForPage(ClassesTechnician classesTechnician);

    /**
     * 查询已经排班美甲师情况
     * @param departmentId
     * @param days
     * @return
     */
    List<ClassesTechnician> listClassesTechnician(@Param("departmentId") Integer departmentId, @Param("days") String days);

    /*List<ClassesTechnician> listClassesTechnicianByStartTime(@Param("startTime")String startTime, @Param("startDate") String startDate);*/

    List<ClassesTechnician> listClassTimeByStartDate(@Param("startDate")String startDate, @Param("startTime") String startTime, @Param("departmentId")Integer departmentId);

    List<ClassesTechnician> listClassesTechnicianDetail(@Param("userId") Integer userId, @Param("days") String days);
    List<ClassesTechnician> listClassesTechnicianDetailByDays(@Param("userId") Integer userId, @Param("days") String days);
    
    int updateClassesTechnician(ClassesTechnician classesTechnician);

    int updateClassesTechnicianByIdAndStatus(Integer id);

    int deleteClassesTechnicianById(Integer id);

    int updateClassesTechnicianByList(List<Map<String, Object>> list);

    int insertClassesTechnicianByStore(Map<String, Object> map);

    int insertClassesTechnician(ClassesTechnician classesTechnician);
}
