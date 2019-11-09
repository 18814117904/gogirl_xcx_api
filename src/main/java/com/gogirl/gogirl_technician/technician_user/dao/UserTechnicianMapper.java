package com.gogirl.gogirl_technician.technician_user.dao;

import com.gogirl.gogirl_store.store_commons.dto.ClassesTechnician;
import com.gogirl.gogirl_store.store_commons.dto.UserManage;
import com.gogirl.gogirl_technician.technician_commons.dto.UserTechnician;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by yinyong on 2018/9/18.
 */
@Mapper
@Repository
public interface UserTechnicianMapper {
	UserTechnician getTechnicianManageForDetail(Integer id);
    List<UserTechnician> listTechnicianForPage(UserTechnician technicianManage);
    List<UserTechnician> getTechnicianManageByOpenid(String openid);
//
//    List<TechnicianManage> listTechnicianManageForAll(Integer departmentId);
//
//    int updateTechnicianManageById(TechnicianManage technicianManage);
//
//    int deleteTechnicianById(Integer id);
//
//    int insertTechnicianManage(TechnicianManage technicianManage);
//
//
//    int insertTechnician(@Param("name") String name, @Param("departmentId") Integer departmentId, @Param("userId") Integer userId, @Param("picturePath")String picturePath, @Param("mobile") String mobile, @Param("status") Integer status);
//
//    int updateTechnician(@Param("name") String name, @Param("departmentId") Integer departmentId, @Param("userId") Integer userId, @Param("picturePath")String picturePath, @Param("mobile") String mobile, @Param("status") Integer status);
//
//    int deleteTechnician(Integer id);
//
//    TechnicianManage getTechnicianManage(Integer id);
//
//    List<TechnicianManage> listClassesTechnician(@Param("departmentId") Integer departmentId, @Param("days") String days);
//
//	List<UserManage> selectByopenid(String openid);
//
//	List<TechnicianManage> queryDayTechnicianClasses(Integer departmentId,
//			Date day);
	int updateTechnicianByAuthorityId(UserTechnician item);
	List<UserTechnician> queryDayTechnicianClasses(@Param("departmentId") Integer departmentId,@Param("day")Date day);
//	List<UserTechnician> listClassesTechnicianDetail(Integer userId, String days);
	String getShortCode(Integer techId);
}
