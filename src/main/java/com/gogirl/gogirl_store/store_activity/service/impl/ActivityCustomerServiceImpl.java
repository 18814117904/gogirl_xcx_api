package com.gogirl.gogirl_store.store_activity.service.impl;

import com.gogirl.gogirl_store.store_activity.dao.ActivityCustomerMapper;
import com.gogirl.gogirl_store.store_activity.service.ActivityCustomerService;
import com.gogirl.gogirl_store.store_commons.dto.ActivityCustomer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by yinyong on 2019/1/9.
 */
@Service
public class ActivityCustomerServiceImpl implements ActivityCustomerService {

    @Autowired
    private ActivityCustomerMapper activityCustomerMapper;

    @Override
    public List<ActivityCustomer> listActivityCustomerForPage(ActivityCustomer activityCustomer) {
        return activityCustomerMapper.listActivityCustomerForPage(activityCustomer);
    }

    @Override
    public ActivityCustomer checkActivityCustomer(ActivityCustomer activityCustomer) {
        return activityCustomerMapper.checkActivityCustomer(activityCustomer);
    }

    @Override
    public int updateCustomerStatus(ActivityCustomer activityCustomer) {
        return activityCustomerMapper.updateCustomerStatus(activityCustomer);
    }

    @Override
    public int insertActivityCustomer(ActivityCustomer activityCustomer) {
        return activityCustomerMapper.insertActivityCustomer(activityCustomer);
    }
}
