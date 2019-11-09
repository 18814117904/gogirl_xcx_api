package com.gogirl.gogirl_user.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.gogirl.gogirl_user.entity.Coupon;
import com.gogirl.gogirl_user.entity.CouponCustomerRelevance;

@Mapper
public interface CouponMapper {
    int deleteByPrimaryKey(Integer id);


    int insertSelective(Coupon record);

    Coupon selectByPrimaryKey(Integer id);
    List<Coupon> selectByCoupon(Coupon coupon);
    List<Coupon> getAllUseCoupon();
    
    int updateByPrimaryKeySelective(Coupon record);

    String getCouponIdFromConfig();

	List<Coupon> getOrderCanUseCoupon(Integer orderId);
	List<Coupon> getOrderCanUseCouponPart1(Integer orderId);

	List<Coupon> getOrderCanUseCouponPart2(Integer orderId);

}