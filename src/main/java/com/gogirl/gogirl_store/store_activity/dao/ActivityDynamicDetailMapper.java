package com.gogirl.gogirl_store.store_activity.dao;

import com.gogirl.gogirl_store.store_commons.dto.ActivityDynamicDetail;
import com.gogirl.gogirl_store.store_commons.dto.ActivityDynamicHead;
import com.gogirl.gogirl_store.store_commons.dto.ActivitySummary;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by yinyong on 2019/1/9.
 */
@Mapper
@Repository
public interface ActivityDynamicDetailMapper {

    List<ActivityDynamicDetail> listDynamicDetailForPage(ActivityDynamicDetail activityDynamicDetail);

}
