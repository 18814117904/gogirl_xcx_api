package com.gogirl.gogirl_xcx.dao;

import org.apache.ibatis.annotations.Mapper;

import com.gogirl.gogirl_xcx.entity.GogirlToken;
@Mapper
public interface GogirlTokenMapper {
    int deleteByPrimaryKey(Integer customerId);

    int insertSelective(GogirlToken record);

    GogirlToken selectByCustomerId(Integer customerId);

    int updateByPrimaryKeySelective(GogirlToken record);

	GogirlToken selectByToken(String token);
	GogirlToken selectByToken_t(String token);
	
//	int selectByToken(String token);
}