package com.gogirl.gogirl_order.service;

import com.gogirl.gogirl_order.dao.OrderCommentLabelMapper;
import com.gogirl.gogirl_order.order_commons.dto.OrderCommentLabel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by yinyong on 2018/10/22.
 */
@Service
public class OrderCommentLabelService{

    @Autowired
    private OrderCommentLabelMapper orderCommentLabelMapper;

    public List<OrderCommentLabel> listOrderCommentLabel() {
        return orderCommentLabelMapper.listOrderCommentLabel();
    }
}
