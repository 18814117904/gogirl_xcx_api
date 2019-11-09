package com.gogirl.gogirl_order.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.gogirl.gogirl_order.entity.OrderServeDescribe;
@Mapper
public interface OrderServeDescribeMapper {
    int deleteByOrderServeId(Integer id);

    int insertList(List<OrderServeDescribe> list);

    int insertSelective(OrderServeDescribe record);

    OrderServeDescribe selectByPrimaryKey(Integer id);
    List<OrderServeDescribe> selectByType(Integer type);

    int updateByPrimaryKeySelective(OrderServeDescribe record);

    int updateByPrimaryKey(OrderServeDescribe record);
}