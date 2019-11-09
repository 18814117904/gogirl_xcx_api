package com.gogirl.gogirl_order.order_manage.service.impl;

import com.gogirl.gogirl_order.order_commons.dto.OrderRecord;
import com.gogirl.gogirl_order.order_manage.dao.OrderRecordMapper;
import com.gogirl.gogirl_order.order_manage.service.OrderRecordService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by yinyong on 2018/12/3.
 */
@Service
public class OrderRecordServiceImpl implements OrderRecordService {

    @Autowired
    private OrderRecordMapper orderRecordMapper;

    @Override
    public OrderRecord getOrderRecord(Integer orderServeId) {
        return orderRecordMapper.getOrderRecord(orderServeId);
    }

    @Override
    public List<OrderRecord> listOrderRecord(OrderRecord orderRecord) {
        return orderRecordMapper.listOrderRecord(orderRecord);
    }

    @Override
    public List<OrderRecord> listOrderRecordByOrderId(OrderRecord orderRecord) {
        return orderRecordMapper.listOrderRecordByOrderId(orderRecord);
    }

    @Override
    public int updateOrderRecord(OrderRecord orderRecord) {
        return orderRecordMapper.updateOrderRecord(orderRecord);
    }

    @Override
    public int insertOrderRecord(OrderRecord orderRecord) {
        return orderRecordMapper.insertOrderRecord(orderRecord);
    }
}
