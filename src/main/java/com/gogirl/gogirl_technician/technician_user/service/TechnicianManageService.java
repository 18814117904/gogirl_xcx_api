package com.gogirl.gogirl_technician.technician_user.service;


import com.gogirl.gogirl_technician.technician_commons.dto.TechnicianManage;

import java.util.Date;
import java.util.List;

/**
 * Created by yinyong on 2018/9/18.
 */
public interface TechnicianManageService {

    List<TechnicianManage> listTechnicianForPage(TechnicianManage technicianManage);
    
    List<TechnicianManage> listTechnicianManageForAll(Integer departmentId);
    List<TechnicianManage> queryDayTechnicianClasses(Integer departmentId,Date day);

    int updateTechnicianManageById(TechnicianManage technicianManage);

    int deleteTechnicianManageById(Integer id);

    int insertTechnicianManage(TechnicianManage technicianManage);
    List<TechnicianManage> getTechnicianManageByOpenid(String openid);
//    TechnicianManage getTechnicianManageByPhone(String openid);
    TechnicianManage getTechnicianManageForDetail(Integer id);

}
