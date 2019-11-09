package com.gogirl.gogirl_store.store_activity.service.impl;

import com.gogirl.gogirl_store.store_activity.dao.ActivityCustomerMapper;
import com.gogirl.gogirl_store.store_activity.dao.ActivityManageMapper;
import com.gogirl.gogirl_store.store_activity.service.ActivityManageService;
import com.gogirl.gogirl_store.store_commons.dto.ActivityManage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by yinyong on 2019/1/8.
 */
@Service
public class ActivityManageServiceImpl implements ActivityManageService {

    @Autowired
    private ActivityManageMapper activityManageMapper;

    @Override
    public List<ActivityManage> listActivityForPage(ActivityManage activityManage) {
        return activityManageMapper.listActivityForPage(activityManage);
    }

    @Override
    public ActivityManage getActivityForDetail(Integer id) {
        return activityManageMapper.getActivityForDetail(id);
    }

    @Override
    public int updateActivity(ActivityManage activityManage) {
        return activityManageMapper.updateActivity(activityManage);
    }

    @Override
    public int deleteActivityById(Integer id) {
        return activityManageMapper.deleteActivityById(id);
    }

    @Override
    public int insertActivity(ActivityManage activityManage) {
        return activityManageMapper.insertActivity(activityManage);
    }
}
