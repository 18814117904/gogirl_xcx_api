package com.gogirl.gogirl_store.store_activity.service;

import com.gogirl.gogirl_store.store_commons.dto.ActivityCustomer;

import java.util.List;

/**
 * Created by yinyong on 2019/1/9.
 */
public interface ActivityCustomerService {

    List<ActivityCustomer> listActivityCustomerForPage(ActivityCustomer activityCustomer);

    ActivityCustomer checkActivityCustomer(ActivityCustomer activityCustomer);

    int updateCustomerStatus(ActivityCustomer activityCustomer);

    int insertActivityCustomer(ActivityCustomer activityCustomer);
}
