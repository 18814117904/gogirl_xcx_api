package com.gogirl.gogirl_order.order_manage.dao;

import com.gogirl.gogirl_order.order_commons.dto.OrderRecord;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by yinyong on 2018/9/21.
 */
@Mapper
@Repository
public interface OrderRecordMapper {

    OrderRecord getOrderRecord(Integer orderServeId);

    List<OrderRecord> listOrderRecord(OrderRecord orderRecord);

    List<OrderRecord> listOrderRecordByOrderId(OrderRecord orderRecord);

    int deleteOrderRecord(List<Integer> list);

    int updateOrderRecord(OrderRecord orderRecord);

    int insertOrderRecord(OrderRecord orderRecord);
}
