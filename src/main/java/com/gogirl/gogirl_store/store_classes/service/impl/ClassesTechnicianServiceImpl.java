package com.gogirl.gogirl_store.store_classes.service.impl;

import com.gogirl.gogirl_store.store_classes.dao.ClassesTechnicianMapper;
import com.gogirl.gogirl_store.store_classes.service.ClassesTechnicianService;
import com.gogirl.gogirl_store.store_commons.dto.ClassesTechnician;
import com.gogirl.gogirl_store.store_commons.dto.TechnicianServe;
import com.gogirl.gogirl_technician.technician_user.dao.TechnicianManageMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by yinyong on 2018/9/20.
 */
@Service
public class ClassesTechnicianServiceImpl implements ClassesTechnicianService {

    @Autowired
    private ClassesTechnicianMapper classesTechnicianMapper;

    @Autowired
    private TechnicianManageMapper technicianManageMapper;

    @Override
    public List<ClassesTechnician> listClassesTechnicianForPage(ClassesTechnician classesTechnician) {
        return classesTechnicianMapper.listClassesTechnicianForPage(classesTechnician);
    }

    @Override
    public List<ClassesTechnician> listClassesTechnician(Integer departmentId, String days) {
        List<ClassesTechnician> classesTechnicianList = classesTechnicianMapper.listClassesTechnician(departmentId, days);

        return classesTechnicianList;
    }
    
    @Override
    public List<ClassesTechnician> listClassesTechnicianDetail(Integer userId, String days) {
        List<ClassesTechnician> classesTechnicianList = classesTechnicianMapper.listClassesTechnicianDetail(userId, days);
        return classesTechnicianList;
    }
    @Override
    public List<ClassesTechnician> listClassesTechnicianDetailByDays(Integer userId, String days) {
        List<ClassesTechnician> classesTechnicianList = classesTechnicianMapper.listClassesTechnicianDetailByDays(userId, days);
        return classesTechnicianList;
    }

    /*@Override
    public Map<String, Object> checkTimeForSchedule(String startDateTime, String startDate, String startTime, String endDateTime, Integer serveId, Integer produceId, Integer departmentId) {
        List<ClassesTechnician> listTechnician = new ArrayList<ClassesTechnician>();
        List<TechnicianServe> listTechnicianServe = new ArrayList<TechnicianServe>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("success", false);
        //获取预约时间内美甲师排班列表
        listTechnician = classesTechnicianMapper.listClassesTechnicianByStartTime(startTime, startDate);
        //获取包含预约时间已经预约的记录数据列表
        listTechnicianServe = technicianServeMapper.listTechnicianServe(startDateTime, endDateTime);
        //获取店铺所有美甲师
        List<TechnicianManage> listTechnicianManage = technicianManageMapper.listTechnicianManageForAll(departmentId);
        if(listTechnician.size() > listTechnicianServe.size()){
            List<Integer> listTechnicianId = retureOptionalTechnician(listTechnician, listTechnicianServe);
            for(TechnicianManage technicianManage : listTechnicianManage){
                if(listTechnicianId.contains(technicianManage.getId())){
                    technicianManage.setStatus(1);
                }
            }
            map.put("success", true);
        }
        map.put("listTechnicianManage", listTechnicianManage);
        return map;
    }*/

    @Override
    public List<ClassesTechnician> getReservableTime(String startDate, String startTime, Integer departmentId) {
        List<ClassesTechnician> listClassesManage = classesTechnicianMapper.listClassTimeByStartDate(startDate, startTime, departmentId);
        return listClassesManage;
    }

    @Override
    @Transactional
    public int updateClassesTechnician(ClassesTechnician classesTechnician) {
        return classesTechnicianMapper.updateClassesTechnician(classesTechnician);
    }

    @Override
    @Transactional
    public int deleteClassesTechnicianById(Integer id) {
        return classesTechnicianMapper.deleteClassesTechnicianById(id);
    }

    @Override
    @Transactional
    public int insertClassesTechnician(ClassesTechnician classesTechnician) {
        return classesTechnicianMapper.insertClassesTechnician(classesTechnician);
    }

    @Override
    public int updateClassesTechnicianByList(List<Map<String, Object>> list) {
        return classesTechnicianMapper.updateClassesTechnicianByList(list);
    }

    @Override
    @Transactional
    public int insertClassesTechnicianByStore(List<Map<String, Object>> list) {
        List<Map<String, Object>> modifyList = new ArrayList<Map<String, Object>>();
        for(Map<String, Object> map : list){
            boolean modifyboolean = map.get("modify") == null ? false: (boolean) map.get("modify");
            if(modifyboolean){
                modifyList.add(map);
                continue;
            }
            classesTechnicianMapper.insertClassesTechnicianByStore(map);
        }
        if(modifyList.size() > 0){
            classesTechnicianMapper.updateClassesTechnicianByList(modifyList);
        }
        return 1;
    }

    public List<Integer> retureOptionalTechnician( List<ClassesTechnician> listTechnician, List<TechnicianServe> listTechnicianServe){
        List<Integer> list = new ArrayList<Integer>();
        for(ClassesTechnician classesTechnician : listTechnician){
            for(TechnicianServe technicianServe : listTechnicianServe){
                if(classesTechnician.getUserId() != technicianServe.getTechnicianId()){
                    list.add(classesTechnician.getUserId());
                }
            }
        }
        return list;
    }
}
