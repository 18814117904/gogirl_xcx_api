package com.gogirl.gogirl_user.dao;

import org.apache.ibatis.annotations.Mapper;

import com.gogirl.gogirl_user.entity.TimesCardTypeContent;
@Mapper
public interface TimesCardTypeContentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TimesCardTypeContent record);

    int insertSelective(TimesCardTypeContent record);

    TimesCardTypeContent selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TimesCardTypeContent record);

    int updateByPrimaryKey(TimesCardTypeContent record);
}