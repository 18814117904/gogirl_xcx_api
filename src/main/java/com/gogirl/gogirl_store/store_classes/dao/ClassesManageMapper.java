package com.gogirl.gogirl_store.store_classes.dao;

import com.gogirl.gogirl_store.store_commons.dto.ClassesManage;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by yinyong on 2018/9/20.
 */
@Mapper
@Repository
public interface ClassesManageMapper {

    List<ClassesManage> listClassesManageForPage(ClassesManage classesManage);

    List<ClassesManage> listClassesManage(@Param("departmentId") Integer departmentId);

    int updateUseNumber(Integer id);

    int updateClassesManage(ClassesManage classesManage);

    int deleteClassesManageById(Integer id);

    int insertClassesManage(ClassesManage classesManage);
}
