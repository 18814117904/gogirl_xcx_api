package com.gogirl.gogirl_store.store_activity.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gogirl.gogirl_order.order_commons.utils.JsonResult;
import com.gogirl.gogirl_store.store_activity.service.ActivityCustomerService;
import com.gogirl.gogirl_store.store_commons.dto.ActivityCustomer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.List;

@RestController
@Api(tags={"活动相关接口"},value="活动相关接口")
@RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "activityCustomer")
public class ActivityCustomerController {

    @Autowired
    private ActivityCustomerService activityCustomerService;

    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "queryActivityCustomerForPage")
    public JsonResult queryActivityCustomerForPage(ActivityCustomer activityCustomer, HttpServletRequest request, HttpServletResponse response) {
        JsonResult jsonResult = new JsonResult();
        String pageNum = request.getParameter("pageNum");
        String pageSize = request.getParameter("pageSize");
        PageHelper.startPage(Integer.parseInt(pageNum),Integer.parseInt(pageSize));
        List<ActivityCustomer> lists = activityCustomerService.listActivityCustomerForPage(activityCustomer);
        PageInfo<ActivityCustomer> pageInfo = new PageInfo<ActivityCustomer>(lists);
        jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC).setData(pageInfo);
        return jsonResult;
    }

    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "checkShowActivity")
    public JsonResult checkShowActivity(ActivityCustomer activityCustomer, HttpServletRequest request, HttpServletResponse response) {
        JsonResult jsonResult = new JsonResult();
        ActivityCustomer activityCustomer1;
        activityCustomer1 = activityCustomerService.checkActivityCustomer(activityCustomer);
        if(activityCustomer1 == null) {
            activityCustomer1 = new ActivityCustomer();
        }
        jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC).setData(activityCustomer1);
        return jsonResult;
    }

    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "checkActivityCustomer")
    public JsonResult checkActivityCustomer(ActivityCustomer activityCustomer, HttpServletRequest request, HttpServletResponse response) {
        JsonResult jsonResult = new JsonResult();
        ActivityCustomer activityCustomer1;
        activityCustomer1 = activityCustomerService.checkActivityCustomer(activityCustomer);
        if(activityCustomer1 == null) {
            int result = activityCustomerService.insertActivityCustomer(activityCustomer);
            activityCustomer1 = activityCustomerService.checkActivityCustomer(activityCustomer);
        }
        jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC).setData(activityCustomer1);
        return jsonResult;
    }
}
