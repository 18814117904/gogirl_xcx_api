package com.gogirl.gogirl_order.order_manage.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.gogirl.gogirl_order.order_commons.dto.OrderServe;
import com.gogirl.gogirl_order.order_manage.dao.OrderServeMapper;
import com.gogirl.gogirl_order.order_manage.service.OrderServeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by yinyong on 2018/11/19.
 */
@Service
public class OrderServeServiceImpl implements OrderServeService {

    @Autowired
    private OrderServeMapper orderServeMapper;

    @Override
    public int updateOrderService(OrderServe orderServe) {
        return orderServeMapper.updateOrderService(orderServe);
    }
    @Override
    public int updateOrderServiceByOrderId(OrderServe orderServe) {
        return orderServeMapper.updateOrderServiceByOrderId(orderServe);
    }
	public List<OrderServe> listTimesCardCanUseOrderServeId(Integer timesCardId,Integer orderId) {
		List<OrderServe> list = orderServeMapper.listTimesCardCanUseOrderServeId1(timesCardId,orderId);
		List<OrderServe> list2 = orderServeMapper.listTimesCardCanUseOrderServeId2(timesCardId,orderId);
		if(list==null){list = new ArrayList<OrderServe>();}
		if(list2!=null){
			list.addAll(list2);
		}
		return list;
	}
	@Override
	public List<OrderServe> listCouponCanUseOrderServeId(Integer couponId,Integer orderId) {
		List<OrderServe> list = orderServeMapper.listCouponCanUseOrderServeId1(couponId,orderId);
		List<OrderServe> list2 = orderServeMapper.listCouponCanUseOrderServeId2(couponId,orderId);
		if(list==null){list = new ArrayList<OrderServe>();}
		if(list2!=null){
			list.addAll(list2);
		}
		return list;
	}
	@Override
	public OrderServe getOrderServeDetail(Integer id) {
		return orderServeMapper.getOrderServeDetail(id);
	}

}
