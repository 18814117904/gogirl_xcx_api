package com.gogirl.gogirl_technician.technician_user.dao;

import com.gogirl.gogirl_store.store_commons.dto.UserManage;
import com.gogirl.gogirl_technician.technician_commons.dto.TechnicianManage;

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
public interface TechnicianManageMapper {

    List<TechnicianManage> listTechnicianForPage(TechnicianManage technicianManage);

    List<TechnicianManage> listTechnicianManageForAll(Integer departmentId);

    int updateTechnicianManageById(TechnicianManage technicianManage);

    int deleteTechnicianById(Integer id);

    int insertTechnicianManage(TechnicianManage technicianManage);


    int insertTechnician(@Param("name") String name, @Param("departmentId") Integer departmentId, @Param("userId") Integer userId, @Param("picturePath")String picturePath, @Param("mobile") String mobile, @Param("status") Integer status);

    int updateTechnician(@Param("name") String name, @Param("departmentId") Integer departmentId, @Param("userId") Integer userId, @Param("picturePath")String picturePath, @Param("mobile") String mobile, @Param("status") Integer status);

    int deleteTechnician(Integer id);

    TechnicianManage getTechnicianManage(Integer id);
    List<TechnicianManage> getTechnicianManageByOpenid(String openid);
    TechnicianManage getTechnicianManageForDetail(Integer id);

    List<TechnicianManage> listClassesTechnician(@Param("departmentId") Integer departmentId, @Param("days") String days);

	List<UserManage> selectByopenid(String openid);

	List<TechnicianManage> queryDayTechnicianClasses(Integer departmentId,
			Date day);
}
