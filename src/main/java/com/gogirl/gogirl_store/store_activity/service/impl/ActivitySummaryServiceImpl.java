package com.gogirl.gogirl_store.store_activity.service.impl;

import com.gogirl.gogirl_store.store_activity.dao.ActivityDynamicDetailMapper;
import com.gogirl.gogirl_store.store_activity.dao.ActivityDynamicHeadMapper;
import com.gogirl.gogirl_store.store_activity.dao.ActivitySummaryMapper;
import com.gogirl.gogirl_store.store_activity.service.ActivitySummaryService;
import com.gogirl.gogirl_store.store_commons.dto.ActivityDynamicDetail;
import com.gogirl.gogirl_store.store_commons.dto.ActivityDynamicHead;
import com.gogirl.gogirl_store.store_commons.dto.ActivitySummary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by yinyong on 2019/1/9.
 */
@Service
public class ActivitySummaryServiceImpl implements ActivitySummaryService {

    @Autowired
    private ActivitySummaryMapper activitySummaryMapper;

    @Autowired
    private ActivityDynamicHeadMapper activityDynamicHeadMapper;

    @Autowired
    private ActivityDynamicDetailMapper activityDynamicDetailMapper;

    @Override
    public List<ActivitySummary> listActivitySummaryForPage(ActivitySummary activitySummary) {
        return activitySummaryMapper.listActivitySummaryForPage(activitySummary);
    }

    @Override
    public ActivityDynamicHead getActivityDynamicHead(Integer activityId) {
        return activityDynamicHeadMapper.getActivityDynamicHead(activityId);
    }

    @Override
    public List<ActivityDynamicDetail> listDynamicDetailForPage(ActivityDynamicDetail activityDynamicDetail) {
        return activityDynamicDetailMapper.listDynamicDetailForPage(activityDynamicDetail);
    }

    @Override
    public ActivitySummary getActivitySummaryDetail(ActivitySummary activitySummary) {
        return activitySummaryMapper.getActivitySummaryDetail(activitySummary);
    }

    @Override
    public ActivitySummary checkActivitySummary(ActivitySummary activitySummary) {
        return activitySummaryMapper.checkActivitySummary(activitySummary);
    }

    @Override
    public int updateActivitySummary(ActivitySummary activitySummary) {
        return activitySummaryMapper.updateActivitySummary(activitySummary);
    }

    @Override
    public int insertActivitySummary(ActivitySummary activitySummary) {
        return activitySummaryMapper.insertActivitySummary(activitySummary);
    }
}
