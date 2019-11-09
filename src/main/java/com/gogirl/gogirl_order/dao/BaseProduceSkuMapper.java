package com.gogirl.gogirl_order.dao;

import org.apache.ibatis.annotations.Mapper;

import com.gogirl.gogirl_order.entity.BaseProduceSku;

@Mapper
public interface BaseProduceSkuMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BaseProduceSku record);

    int insertSelective(BaseProduceSku record);

    BaseProduceSku selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BaseProduceSku record);

    int updateByPrimaryKey(BaseProduceSku record);
}