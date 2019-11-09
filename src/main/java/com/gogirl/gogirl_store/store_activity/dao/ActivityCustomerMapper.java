package com.gogirl.gogirl_store.store_activity.dao;

import com.gogirl.gogirl_store.store_commons.dto.ActivityCustomer;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by yinyong on 2019/1/9.
 */
@Mapper
@Repository
public interface ActivityCustomerMapper {

    List<ActivityCustomer> listActivityCustomerForPage(ActivityCustomer activityCustomer);

    ActivityCustomer checkActivityCustomer(ActivityCustomer activityCustomer);

    int updateCustomerStatus(ActivityCustomer activityCustomer);

    int insertActivityCustomer(ActivityCustomer activityCustomer);
}
