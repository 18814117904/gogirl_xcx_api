package com.gogirl.gogirl_order.order_manage.controller;

import com.gogirl.gogirl_order.order_commons.config.Constans;
import com.gogirl.gogirl_order.order_commons.dto.OrderServe;
import com.gogirl.gogirl_order.order_commons.utils.JsonResult;
import com.gogirl.gogirl_order.order_manage.service.OrderServeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Date;

/**
 * Created by yinyong on 2018/11/19.
 */
@RestController
@RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "orderserve")
public class OrderServeController {

    @Autowired
    private OrderServeService orderServeService;

    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "modifyOrderServe")
    public JsonResult modifyOrderServe(OrderServe orderServe, HttpServletRequest request, HttpServletResponse response){
        JsonResult jsonResult = new JsonResult();
        if(orderServe.getExecutionStatus() == Constans.ORDER_EXECUTION_START){
            orderServe.setExecutionStatus(Constans.ORDER_EXECUTION_END);
            orderServe.setStartTime(new Date());
        }else if (orderServe.getExecutionStatus() == Constans.ORDER_EXECUTION_END) {
            orderServe.setExecutionStatus(Constans.ORDER_EXECUTION_TIMESLOT);
            orderServe.setEndTime(new Date());
        }
        int result = orderServeService.updateOrderService(orderServe);
        if(result > 0){
            jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC);
        }
        return jsonResult;
    }
}
