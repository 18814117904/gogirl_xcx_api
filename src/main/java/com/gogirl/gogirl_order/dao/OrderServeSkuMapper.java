package com.gogirl.gogirl_order.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.gogirl.gogirl_order.entity.OrderServeSku;
@Mapper

public interface OrderServeSkuMapper {
    int deleteByOrderServeId(Integer id);

    int insertList(List<OrderServeSku> list);

    int insertSelective(OrderServeSku record);

    OrderServeSku selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderServeSku record);

    int updateByPrimaryKey(OrderServeSku record);
}