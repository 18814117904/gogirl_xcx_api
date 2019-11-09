package com.gogirl.gogirl_user.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.gogirl.gogirl_user.entity.Coupon;
import com.gogirl.gogirl_user.entity.CouponCustomerRelevance;
@Mapper
public interface CouponCustomerRelevanceMapper {
    List<CouponCustomerRelevance> select7DayExpireCoupon(String expireTimeStr);

	List<CouponCustomerRelevance> selectByCouponCustomerRelevance(@Param(value = "record") CouponCustomerRelevance record,@Param(value = "phone") String phone,@Param(value = "username") String username);
    List<CouponCustomerRelevance> selectMyCoupon(Integer customerId);
    int deleteByPrimaryKey(Integer id);

    int insert(CouponCustomerRelevance record);

    int insertSelective(CouponCustomerRelevance record);
    int insertSelectiveList(List<CouponCustomerRelevance> list);
    
    CouponCustomerRelevance selectByPrimaryKey(Integer id);
    int countRelevanceNum(CouponCustomerRelevance record);
    
    int updateByPrimaryKeySelective(CouponCustomerRelevance record);

    int updateByPrimaryKey(CouponCustomerRelevance record);
    
    String getRandomCode(Double random);
    int deleteCode(String code);
    int setCouponExpire(Date nowDate);

	List<CouponCustomerRelevance> selectMyNewCustomerCoupon(@Param(value = "customerId")Integer customerId,@Param(value = "coupontIdList")List<Integer> coupontIdList);


}