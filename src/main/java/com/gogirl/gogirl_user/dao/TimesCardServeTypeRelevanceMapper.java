package com.gogirl.gogirl_user.dao;

import org.apache.ibatis.annotations.Mapper;

import com.gogirl.gogirl_user.entity.TimesCardServeTypeRelevance;
@Mapper
public interface TimesCardServeTypeRelevanceMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TimesCardServeTypeRelevance record);

    int insertSelective(TimesCardServeTypeRelevance record);

    TimesCardServeTypeRelevance selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TimesCardServeTypeRelevance record);

    int updateByPrimaryKey(TimesCardServeTypeRelevance record);
}