package com.gogirl.gogirl_order.order_manage.service;

import java.util.List;

import com.gogirl.gogirl_order.order_commons.dto.OrderServe;

/**
 * Created by yinyong on 2018/11/19.
 */
public interface OrderServeService {

    int updateOrderService (OrderServe orderServe);
    OrderServe getOrderServeDetail (Integer id);

	int updateOrderServiceByOrderId(OrderServe orderServe);
	List<OrderServe> listTimesCardCanUseOrderServeId(Integer timesCardId,Integer orderId);
	List<OrderServe> listCouponCanUseOrderServeId(Integer couponId,Integer orderId);
}
