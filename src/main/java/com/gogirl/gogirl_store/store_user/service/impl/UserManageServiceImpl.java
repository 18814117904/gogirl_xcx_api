package com.gogirl.gogirl_store.store_user.service.impl;

import com.gogirl.gogirl_store.store_classes.dao.ClassesTechnicianMapper;
import com.gogirl.gogirl_store.store_commons.dto.UserManage;
import com.gogirl.gogirl_store.store_user.dao.UserManageMapper;
import com.gogirl.gogirl_store.store_user.service.UserManageService;
import com.gogirl.gogirl_technician.technician_commons.dto.TechnicianManage;
import com.gogirl.gogirl_technician.technician_user.dao.TechnicianManageMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by yinyong on 2018/9/17.
 */
@Service
public class UserManageServiceImpl implements UserManageService {

    @Autowired
    private UserManageMapper userManageMapper;

    @Autowired
    private TechnicianManageMapper technicianManageMapper;

    @Autowired
    private ClassesTechnicianMapper classesTechnicianMapper;

    @Override
    public List<UserManage> listUserManageForPage(UserManage userManage) {
        return userManageMapper.listUserManageForPage(userManage);
    }

    @Override
    public List<UserManage> listUserManageForAll(UserManage userManage) {
        return userManageMapper.listUserManageForAll(userManage);
    }

    @Override
    public List<UserManage> listUserForAllNotQuit(UserManage userManage) {
        return userManageMapper.listUserForAllNotQuit(userManage);
    }

    @Override
    @Transactional
    public int updateUserManage(UserManage userManage) {
        int result = userManageMapper.updateUserManage(userManage);
        if(userManage.getJobs() != null && !(userManage.getJobs().contains("美甲师")) && !(userManage.getJobs().contains("美睫师"))){
            technicianManageMapper.deleteTechnician(userManage.getId());
            //判断用户职位是否包含美甲师、美睫师
        }else if (userManage.getJobs() != null && ((userManage.getJobs().contains("美甲师")) || (userManage.getJobs().contains("美睫师")))) {
            TechnicianManage technicianManage = technicianManageMapper.getTechnicianManage(userManage.getId());
            if(technicianManage != null){   //修改美甲师
                technicianManageMapper.updateTechnician(userManage.getName(), userManage.getDepartmentId(), userManage.getId(), userManage.getPicturePath(), userManage.getMobile(), userManage.getStatus());
            }else{   //新增美甲师
                technicianManageMapper.insertTechnician(userManage.getName(), userManage.getDepartmentId(), userManage.getId(), userManage.getPicturePath(), userManage.getMobile(), 1);
            }
        }
        classesTechnicianMapper.updateClassesTechnicianByIdAndStatus(userManage.getId());

        return result;
    }

    @Override
    public UserManage getUserManageForDetail(Integer id) {
        return userManageMapper.getUserManageForDetail(id);
    }

    @Override
    public UserManage checkLogin(UserManage userManage) {
        return userManageMapper.checkLogin(userManage);
    }

    @Override
    @Transactional
    public int deleteUserManageById(Integer id) {
        //删除用户表中信息同时删除美甲师中美甲师信息
        technicianManageMapper.deleteTechnician(id);
        return userManageMapper.deleteUserManageById(id);
    }

    @Override
    @Transactional
    public int insertUserManage(UserManage userManage) {
        userManage.setCreateTime(new Date());
        userManage.setStatus(1);
        int result = userManageMapper.insertUserManage(userManage);
        if(userManage.getJobs() != null && (userManage.getJobs().contains("美甲师") || userManage.getJobs().contains("美睫师"))){
            technicianManageMapper.insertTechnician(userManage.getName(), userManage.getDepartmentId(), userManage.getId(), userManage.getPicturePath(), userManage.getMobile(), userManage.getStatus());
        }
        return result;
    }

	@Override
	public List<UserManage> selectByopenid(String openid) {
        return technicianManageMapper.selectByopenid(openid);
	}
}
