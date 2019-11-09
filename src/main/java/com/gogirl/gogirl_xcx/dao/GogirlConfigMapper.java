package com.gogirl.gogirl_xcx.dao;

import org.apache.ibatis.annotations.Mapper;

import com.gogirl.gogirl_xcx.entity.GogirlConfig;
@Mapper
public interface GogirlConfigMapper {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(GogirlConfig record);

    GogirlConfig selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GogirlConfig record);

}