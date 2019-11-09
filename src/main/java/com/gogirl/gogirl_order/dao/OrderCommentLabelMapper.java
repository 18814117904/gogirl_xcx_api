package com.gogirl.gogirl_order.dao;

import com.gogirl.gogirl_order.order_commons.dto.OrderCommentLabel;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by yinyong on 2018/10/22.
 */
@Mapper
@Repository
public interface OrderCommentLabelMapper {

    List<OrderCommentLabel> listOrderCommentLabel();
}
