package com.gogirl.gogirl_order.order_manage.dao;

import com.gogirl.gogirl_order.order_commons.dto.OrderManage;
import com.gogirl.gogirl_order.order_commons.dto.OrderServe;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by yinyong on 2018/9/21.
 */
@Mapper
@Repository
public interface OrderServeMapper {

    int insertOrderServe(Map<String, Object> map);

    int updateOrderService(OrderServe orderServe);
    int updateOrderServiceByOrderId(OrderServe orderServe);
    
    List<Integer> listOrderServeById(Integer id);

    int deleteOrderServeById(Integer id);

    int modifyOrderServeByUpdate(List<OrderServe> list);

    int updateOrderStatusCancel(OrderManage orderManage);

    int insertOrderServeByUpdate(List<OrderServe> list);

    List<OrderServe> listOrderServe(Integer orderId);

    OrderServe getOrderServeDetail(Integer id);
    
	List<OrderServe> listTimesCardCanUseOrderServeId1(@Param("timesCardId")Integer timesCardId,@Param("orderId")Integer orderId);
	List<OrderServe> listTimesCardCanUseOrderServeId2(@Param("timesCardId")Integer timesCardId,@Param("orderId")Integer orderId);

	List<OrderServe> listCouponCanUseOrderServeId1(@Param("couponId")Integer couponId,@Param("orderId")Integer orderId);
	List<OrderServe> listCouponCanUseOrderServeId2(@Param("couponId")Integer couponId,@Param("orderId")Integer orderId);
}
