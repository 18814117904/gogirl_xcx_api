package com.gogirl.gogirl_technician.technician_user.service;

import com.gogirl.gogirl_store.store_commons.dto.ClassesTechnician;
import com.gogirl.gogirl_technician.technician_commons.dto.UserTechnician;
import com.gogirl.gogirl_technician.technician_user.dao.TechnicianManageMapper;
import com.gogirl.gogirl_technician.technician_user.dao.UserTechnicianMapper;
import com.gogirl.gogirl_technician.technician_user.service.TechnicianManageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by yinyong on 2018/9/18.
 */
@Service
public class UserTechnicianService{

    @Autowired
    private UserTechnicianMapper userTechnicianMapper;
	public UserTechnician getTechnicianManageForDetail(Integer id) {
		return userTechnicianMapper.getTechnicianManageForDetail(id);
	}
    public List<UserTechnician> listTechnicianForPage(UserTechnician technicianManage) {
        return userTechnicianMapper.listTechnicianForPage(technicianManage);
    }
	public List<UserTechnician> getTechnicianManageByOpenid(String openid) {
		return userTechnicianMapper.getTechnicianManageByOpenid(openid);
	}
	public int updateTechnicianByAuthorityId(UserTechnician item) {
		return userTechnicianMapper.updateTechnicianByAuthorityId(item);
	}
	public List<UserTechnician> queryDayTechnicianClasses(Integer departmentId, Date day) {
		return userTechnicianMapper.queryDayTechnicianClasses(departmentId,day);
	}
//    public List<UserTechnician> listClassesTechnicianDetail(Integer userId, String days) {
//        List<UserTechnician> classesTechnicianList = userTechnicianMapper.listClassesTechnicianDetail(userId, days);
//        return classesTechnicianList;
//    }
	public String getShortCode(Integer techId) {
		return userTechnicianMapper.getShortCode(techId);
	}

//    
//    public List<TechnicianManage> listTechnicianManageForAll(Integer departmentId) {
//        return userTechnicianMapper.listTechnicianManageForAll(departmentId);
//    }
//
//    
//    public int updateTechnicianManageById(TechnicianManage technicianManage) {
//        return technicianManageMapper.updateTechnicianManageById(technicianManage);
//    }
//
//    
//    public int deleteTechnicianManageById(Integer id) {
//        return technicianManageMapper.deleteTechnicianById(id);
//    }
//
//    
//    public int insertTechnicianManage(TechnicianManage technicianManage) {
//        return technicianManageMapper.insertTechnicianManage(technicianManage);
//    }
//
//	
//
//	
//
//
//	
//	public List<TechnicianManage> queryDayTechnicianClasses(
//			Integer departmentId, Date day) {
//		return technicianManageMapper.queryDayTechnicianClasses(departmentId,day);
//	}

}
