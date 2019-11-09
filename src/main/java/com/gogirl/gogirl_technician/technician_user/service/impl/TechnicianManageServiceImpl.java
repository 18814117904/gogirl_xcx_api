package com.gogirl.gogirl_technician.technician_user.service.impl;

import com.gogirl.gogirl_technician.technician_commons.dto.TechnicianManage;
import com.gogirl.gogirl_technician.technician_user.dao.TechnicianManageMapper;
import com.gogirl.gogirl_technician.technician_user.service.TechnicianManageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by yinyong on 2018/9/18.
 */
@Service
public class TechnicianManageServiceImpl implements TechnicianManageService {

    @Autowired
    private TechnicianManageMapper technicianManageMapper;

    @Override
    public List<TechnicianManage> listTechnicianForPage(TechnicianManage technicianManage) {
        return technicianManageMapper.listTechnicianForPage(technicianManage);
    }

    @Override
    public List<TechnicianManage> listTechnicianManageForAll(Integer departmentId) {
        return technicianManageMapper.listTechnicianManageForAll(departmentId);
    }

    @Override
    public int updateTechnicianManageById(TechnicianManage technicianManage) {
        return technicianManageMapper.updateTechnicianManageById(technicianManage);
    }

    @Override
    public int deleteTechnicianManageById(Integer id) {
        return technicianManageMapper.deleteTechnicianById(id);
    }

    @Override
    public int insertTechnicianManage(TechnicianManage technicianManage) {
        return technicianManageMapper.insertTechnicianManage(technicianManage);
    }

	@Override
	public List<TechnicianManage> getTechnicianManageByOpenid(String openid) {
		return technicianManageMapper.getTechnicianManageByOpenid(openid);
	}

	@Override
	public TechnicianManage getTechnicianManageForDetail(Integer id) {
		return technicianManageMapper.getTechnicianManageForDetail(id);
	}

	@Override
	public List<TechnicianManage> queryDayTechnicianClasses(
			Integer departmentId, Date day) {
		return technicianManageMapper.queryDayTechnicianClasses(departmentId,day);
	}

}
