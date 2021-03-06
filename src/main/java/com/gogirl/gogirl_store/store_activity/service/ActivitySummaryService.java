package com.gogirl.gogirl_store.store_activity.service;

import com.gogirl.gogirl_store.store_commons.dto.ActivityDynamicDetail;
import com.gogirl.gogirl_store.store_commons.dto.ActivityDynamicHead;
import com.gogirl.gogirl_store.store_commons.dto.ActivitySummary;

import java.util.List;

/**
 * Created by yinyong on 2019/1/9.
 */
public interface ActivitySummaryService {

    List<ActivitySummary> listActivitySummaryForPage(ActivitySummary activitySummary);

    ActivityDynamicHead getActivityDynamicHead(Integer activityId);

    List<ActivityDynamicDetail> listDynamicDetailForPage(ActivityDynamicDetail activityDynamicDetail);

    ActivitySummary getActivitySummaryDetail(ActivitySummary activitySummary);

    ActivitySummary checkActivitySummary(ActivitySummary activitySummary);

    int updateActivitySummary(ActivitySummary activitySummary);

    int insertActivitySummary(ActivitySummary activitySummary);
}
