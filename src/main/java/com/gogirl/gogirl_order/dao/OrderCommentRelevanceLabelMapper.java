package com.gogirl.gogirl_order.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.gogirl.gogirl_order.entity.OrderCommentRelevanceLabel;
@Mapper
public interface OrderCommentRelevanceLabelMapper {
    int insertList(List<OrderCommentRelevanceLabel> list);
}