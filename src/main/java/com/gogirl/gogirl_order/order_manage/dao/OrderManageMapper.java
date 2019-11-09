package com.gogirl.gogirl_order.order_manage.dao;

import com.gogirl.gogirl_order.order_commons.dto.OrderManage;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by yinyong on 2018/9/21.
 */
@Mapper
@Repository
public interface OrderManageMapper {

    List<OrderManage> listOrderManageForPage(OrderManage orderManage);
    List<OrderManage> getListOrderManageForCancle(@Param("startDate")String startDate,@Param("endDate")String endDate);
    
    List<OrderManage> listOrderManageForExport(OrderManage orderManage);

    Map<String, String> getOrderSummary(OrderManage orderManage);

    int getReceivable(OrderManage orderManage);

    OrderManage getOrderForDetail(Integer id);
    OrderManage getOrderForOrderNo(String orderNo);
    
    int getOrderData(Integer departmentId);

    int getOrderReminder(Integer departmentId);
    double countSumPay(Integer customerId,Integer departmentId);
    
    OrderManage checkOrderNo(String orderNo);
    OrderManage getOrderWithRecord(Integer id);
    
    int updateOrderManage(OrderManage orderManage);

    int updateOrderDataIntegrity(OrderManage orderManage);

    int deleteOrderManageById(OrderManage orderManage);

    int insertOrderOrderManageByUser(OrderManage orderManage);

    int insertOrderManage(@Param("orderManage") OrderManage orderManage, @Param("schId") Integer schId, @Param("openOrderUser") Integer openOrderUser);

    int insertRechargeOrder(OrderManage orderManage);

	List<OrderManage> getListOrderManageForPage(OrderManage orderManage);
	List<OrderManage> getListOrderPhotoForPage(OrderManage orderManage);
	List<Map<String, Object>> countTechnicianServing();

	int updateOrderStatusByScheduleId(OrderManage orderManage);

	OrderManage getMaxOrderNo();
	Integer countOrderTimes(Integer customerId);
	
}
