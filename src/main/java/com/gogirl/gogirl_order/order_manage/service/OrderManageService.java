package com.gogirl.gogirl_order.order_manage.service;

import com.gogirl.gogirl_order.order_commons.dto.OrderManage;
import com.gogirl.gogirl_order.order_commons.utils.JsonResult;

import java.util.List;
import java.util.Map;

/**
 * Created by yinyong on 2018/9/21.
 */
public interface OrderManageService {

    List<OrderManage> listOrderManageForPage(OrderManage orderManage);

    List<OrderManage> listOrderManageForExport(OrderManage orderManage);

    Map<String, String> getOrderSummary(OrderManage orderManage);

    int getReceivable(OrderManage orderManage);

    OrderManage getOrderForDetail(Integer id);

    int getOrderData(Integer departmentId);

    int getOrderReminder(Integer deparmentId);

    JsonResult updateOrderStatusFinash(OrderManage orderManage);

    int updateOrderDataIntegrity(OrderManage orderManage);

    int updateOrderPayMentType(OrderManage orderManage);

    int updateOrderManage(OrderManage orderManage);

    int modifyOrderServe(OrderManage orderManage);

    int updateOrderStatusCancel(OrderManage orderManage);

    int deleteOrderServeById(Integer id);

    int deleteOrderManageById(OrderManage orderManage);

    JsonResult insertOrderManageByUser(OrderManage orderManage);

    JsonResult insertOrderManage(Integer serveId, Integer openOrderUser);

    JsonResult insertRechargeOrder(OrderManage orderManage);

	int updateOrderStatus(OrderManage orderManage);
	int updateOrderStatusByScheduleId(OrderManage orderManage);

	JsonResult insertOrderManageXcx(Integer schId, Integer openOrderUser);

	OrderManage getOrderForOrderNo(String orderNo);

	List<OrderManage> getListOrderManageForPage(OrderManage orderManage);
	List<Map<String, Object>> countTechnicianServing();

	int modifyOrder(OrderManage orderManage);

	OrderManage getOrderWithRecord(Integer id);
	double countSumPay(Integer customerId,Integer departmentId);
    int xcxUpdateOrderFinash(OrderManage orderDeatail,OrderManage orderManage,String formId);

	int startPay(OrderManage orderManage);

	Integer countOrderTimes(Integer customerId);

	List<OrderManage> getListOrderPhotoForPage(OrderManage orderManage);
}
