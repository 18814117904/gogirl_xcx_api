package com.gogirl.gogirl_service.dao;

import org.apache.ibatis.annotations.Mapper;

import com.gogirl.gogirl_service.entity.Label;
@Mapper
public interface LabelMapper {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(Label record);

    Label selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Label record);

}