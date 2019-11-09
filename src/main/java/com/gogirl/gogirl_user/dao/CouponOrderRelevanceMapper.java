package com.gogirl.gogirl_user.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.gogirl.gogirl_user.entity.CouponOrderRelevance;
@Mapper
public interface CouponOrderRelevanceMapper {

    int insertSelective(CouponOrderRelevance record);

    CouponOrderRelevance selectByPrimaryKey(Integer id);
    List<CouponOrderRelevance> selectByOrderId(Integer orderId);
    
    int updateByPrimaryKeySelective(CouponOrderRelevance record);

	int updateByCouponCustomerRelevanceId(CouponOrderRelevance record);

	int updateByOrderId(CouponOrderRelevance record);

	int updateCouponCustomerRelevanceUse(Integer orderId);
}