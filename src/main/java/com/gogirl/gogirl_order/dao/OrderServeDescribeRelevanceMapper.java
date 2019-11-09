package com.gogirl.gogirl_order.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.gogirl.gogirl_order.entity.OrderServeDescribe;
import com.gogirl.gogirl_order.entity.OrderServeDescribeRelevance;
@Mapper
public interface OrderServeDescribeRelevanceMapper {
    int deleteByOrderServeId(Integer id);

    int insertList(List<OrderServeDescribeRelevance> list);

    int insertSelective(OrderServeDescribeRelevance record);

    OrderServeDescribeRelevance selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderServeDescribeRelevance record);

    int updateByPrimaryKey(OrderServeDescribeRelevance record);
}