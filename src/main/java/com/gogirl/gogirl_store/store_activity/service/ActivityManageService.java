package com.gogirl.gogirl_store.store_activity.service;

import com.gogirl.gogirl_store.store_commons.dto.ActivityManage;

import java.util.List;

/**
 * Created by yinyong on 2019/1/8.
 */
public interface ActivityManageService {

    List<ActivityManage> listActivityForPage(ActivityManage activityManage);

    ActivityManage getActivityForDetail(Integer id);

    int updateActivity(ActivityManage activityManage);

    int deleteActivityById(Integer id);

    int insertActivity(ActivityManage activityManage);
}
