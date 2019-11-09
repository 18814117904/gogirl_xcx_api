package com.gogirl.gogirl_user.dao;

import org.apache.ibatis.annotations.Mapper;

import com.gogirl.gogirl_user.entity.TimesCardServeRelevance;
@Mapper
public interface TimesCardServeRelevanceMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TimesCardServeRelevance record);

    int insertSelective(TimesCardServeRelevance record);

    TimesCardServeRelevance selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TimesCardServeRelevance record);

    int updateByPrimaryKey(TimesCardServeRelevance record);
}