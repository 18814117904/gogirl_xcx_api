package com.gogirl.gogirl_store.store_activity.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gogirl.gogirl_order.order_commons.utils.JsonResult;
import com.gogirl.gogirl_store.store_activity.service.ActivityCustomerService;
import com.gogirl.gogirl_store.store_activity.service.ActivitySummaryService;
import com.gogirl.gogirl_store.store_commons.dto.ActivityCustomer;
import com.gogirl.gogirl_store.store_commons.dto.ActivityDynamicDetail;
import com.gogirl.gogirl_store.store_commons.dto.ActivityDynamicHead;
import com.gogirl.gogirl_store.store_commons.dto.ActivitySummary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "activitySummary")
@Api(tags={"活动相关接口"},value="活动相关接口")
public class ActivitySummaryController {

    @Autowired
    private ActivitySummaryService activitySummaryService;

    @Autowired
    private ActivityCustomerService activityCustomerService;

    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "queryActivitySummaryForPage")
    public JsonResult queryActivitySummaryForPage(ActivitySummary activitySummary, HttpServletRequest request, HttpServletResponse response){
        JsonResult jsonResult = new JsonResult();
        String pageNum = request.getParameter("pageNum");
        String pageSize = request.getParameter("pageSize");
        PageHelper.startPage(Integer.parseInt(pageNum),Integer.parseInt(pageSize));
        List<ActivitySummary> lists = activitySummaryService.listActivitySummaryForPage(activitySummary);
        PageInfo<ActivitySummary> pageInfo = new PageInfo<ActivitySummary>(lists);
        jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC).setData(pageInfo);
        return jsonResult;
    }

    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "queryDynamicHead")
    public JsonResult queryDynamicHead(Integer activityId, HttpServletRequest request, HttpServletResponse response) {
        JsonResult jsonResult = new JsonResult();
        ActivityDynamicHead activityDynamicHead = activitySummaryService.getActivityDynamicHead(activityId);
        jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC).setData(activityDynamicHead);
        return jsonResult;
    }

    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "queryDynamicDetailForPage")
    public JsonResult queryDynamicDetail(ActivityDynamicDetail activityDynamicDetail, HttpServletRequest request, HttpServletResponse response) {
        JsonResult jsonResult = new JsonResult();
        String pageNum = request.getParameter("pageNum");
        String pageSize = request.getParameter("pageSize");
        PageHelper.startPage(Integer.parseInt(pageNum),Integer.parseInt(pageSize));
        List<ActivityDynamicDetail> listActivityDynamicDetail = activitySummaryService.listDynamicDetailForPage(activityDynamicDetail);
        PageInfo<ActivityDynamicDetail> pageInfo = new PageInfo<ActivityDynamicDetail>(listActivityDynamicDetail);
        jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC).setData(pageInfo);
        return jsonResult;
    }

    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "queryActivitySummaryDetail")
    public JsonResult queryActivitySummaryDetail(ActivitySummary activitySummary, HttpServletRequest request, HttpServletResponse response) {
        JsonResult jsonResult = new JsonResult();
        ActivitySummary activitySummary1 = activitySummaryService.getActivitySummaryDetail(activitySummary);
        jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC).setData(activitySummary1);
        return jsonResult;
    }

    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "addOrmodifyActivitySummary")
    public JsonResult addOrmodifyActivitySummary(ActivitySummary activitySummary, HttpServletRequest request, HttpServletResponse response) {
        JsonResult jsonResult = new JsonResult();
        activitySummary.setSummaryTime(new Date());
        ActivityCustomer activityCustomer = new ActivityCustomer();
        activityCustomer.setActivityId(activitySummary.getActivityId());
        activityCustomer.setCustomerId(activitySummary.getCustomerId());
        ActivityCustomer activityCustomer1 = activityCustomerService.checkActivityCustomer(activityCustomer);
        if(activityCustomer1 != null && activitySummary.getParticipationTimes() != null && activityCustomer1.getParticipationTime() == null && "0".equals(activityCustomer1.getStatus())) {
            activitySummary.setParticipantsNumber(1);  // 参与人数
            activityCustomer1.setStatus("1");
            activityCustomer1.setParticipationTime(new Date());
            activityCustomerService.updateCustomerStatus(activityCustomer1);
        }
        if(activityCustomer1 != null && activitySummary.getRecipientNumber() != null && "1".equals(activityCustomer1.getStatus())) {
            activityCustomer1.setStatus("2");
            activityCustomer1.setCollectTime(new Date());
            activityCustomerService.updateCustomerStatus(activityCustomer1);
        }
        ActivitySummary activitySummary1 = activitySummaryService.checkActivitySummary(activitySummary);
        int result;
        if(activitySummary1 != null) {
            result  = activitySummaryService.updateActivitySummary(activitySummary);
        }else{
            activitySummary.setEjectTimes(1);
            /*activitySummary.setParticipationTimes(1);
            activitySummary.setParticipantsNumber(1);
            activitySummary.setRecipientNumber(1);*/
            result = activitySummaryService.insertActivitySummary(activitySummary);
        }
        if(result > 0) {
            jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC);
        }
        return jsonResult;
    }

    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "addActivitySummarty")
    public JsonResult addActivitySummarty(ActivitySummary activitySummary, HttpServletRequest request, HttpServletResponse response) {
        JsonResult jsonResult = new JsonResult();
        int result = activitySummaryService.insertActivitySummary(activitySummary);
        if(result > 0) {
            jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC);
        }
        return jsonResult;
    }
}
