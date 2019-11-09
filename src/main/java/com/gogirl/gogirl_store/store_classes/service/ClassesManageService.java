package com.gogirl.gogirl_store.store_classes.service;

import com.gogirl.gogirl_store.store_commons.dto.ClassesManage;

import java.util.List;

/**
 * Created by yinyong on 2018/9/20.
 */
public interface ClassesManageService {

    List<ClassesManage> listClassesManageForPage(ClassesManage classesManage);

    List<ClassesManage> listClassesManage(Integer departmentId);

    int updateClassesManage(ClassesManage classesManage);

    int deleteClassesManageById(Integer id);

    void insertClassesManage(ClassesManage listClassesManage);
}
