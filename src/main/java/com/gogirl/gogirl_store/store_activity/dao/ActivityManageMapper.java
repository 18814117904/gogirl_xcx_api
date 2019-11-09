package com.gogirl.gogirl_store.store_activity.dao;

import com.gogirl.gogirl_store.store_commons.dto.ActivityManage;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by yinyong on 2019/1/8.
 */
@Mapper
@Repository
public interface ActivityManageMapper {

    List<ActivityManage> listActivityForPage(ActivityManage activityManage);

    ActivityManage getActivityForDetail(Integer id);

    int updateActivity(ActivityManage activityManage);

    int deleteActivityById(Integer id);

    int insertActivity(ActivityManage activityManage);
}
