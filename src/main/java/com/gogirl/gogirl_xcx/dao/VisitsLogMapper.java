package com.gogirl.gogirl_xcx.dao;

import org.apache.ibatis.annotations.Mapper;

import com.gogirl.gogirl_xcx.entity.VisitsLog;
@Mapper
public interface VisitsLogMapper {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(VisitsLog record);

    VisitsLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(VisitsLog record);
}