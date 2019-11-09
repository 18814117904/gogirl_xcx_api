package com.gogirl.gogirl_order.order_manage.service;

import com.gogirl.gogirl_order.order_commons.dto.OrderRecord;

import java.util.List;

/**
 * Created by yinyong on 2018/12/3.
 */
public interface OrderRecordService {

    OrderRecord getOrderRecord(Integer orderServeId);

    List<OrderRecord> listOrderRecord(OrderRecord orderRecord);

    List<OrderRecord> listOrderRecordByOrderId(OrderRecord orderRecord);

    int updateOrderRecord(OrderRecord orderRecord);

    int insertOrderRecord(OrderRecord orderRecord);
}
