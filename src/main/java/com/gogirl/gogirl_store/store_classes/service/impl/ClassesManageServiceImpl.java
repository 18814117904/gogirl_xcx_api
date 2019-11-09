package com.gogirl.gogirl_store.store_classes.service.impl;

import com.gogirl.gogirl_store.store_classes.dao.ClassesManageMapper;
import com.gogirl.gogirl_store.store_classes.service.ClassesManageService;
import com.gogirl.gogirl_store.store_commons.dto.ClassesManage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by yinyong on 2018/9/20.
 */
@Service
public class ClassesManageServiceImpl implements ClassesManageService {

    @Autowired
    private ClassesManageMapper classesManageMapper;

    @Override
    public List<ClassesManage> listClassesManageForPage(ClassesManage classesManage) {
        return classesManageMapper.listClassesManageForPage(classesManage);
    }

    @Override
    public List<ClassesManage> listClassesManage(Integer departmentId) {
        return classesManageMapper.listClassesManage(departmentId);
    }

    @Override
    @Transactional
    public int updateClassesManage(ClassesManage classesManage) {
        return classesManageMapper.updateClassesManage(classesManage);
    }

    @Override
    @Transactional
    public int deleteClassesManageById(Integer id) {
        return classesManageMapper.deleteClassesManageById(id);
    }

    @Override
    @Transactional
    public void insertClassesManage(ClassesManage classesManage) {
        classesManage.setName("早班");
        classesManageMapper.insertClassesManage(classesManage);
        classesManage.setName("晚班");
        classesManageMapper.insertClassesManage(classesManage);
    }
}
