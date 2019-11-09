package com.gogirl.gogirl_store.store_activity.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gogirl.gogirl_order.order_commons.utils.JsonResult;
import com.gogirl.gogirl_store.store_activity.service.ActivityManageService;
import com.gogirl.gogirl_store.store_commons.dto.ActivityManage;

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
@RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "activity")
@Api(tags={"活动相关接口"},value="活动相关接口")
public class ActivityManageController {

    @Autowired
    private ActivityManageService activityManageService;

    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "queryActivityForPage")
    public JsonResult queryActivityForPage (ActivityManage activityManage, HttpServletRequest request, HttpServletResponse response) {
        JsonResult jsonResult = new JsonResult();
        String pageNum = request.getParameter("pageNum");
        String pageSize = request.getParameter("pageSize");
        PageHelper.startPage(Integer.parseInt(pageNum),Integer.parseInt(pageSize));
        List<ActivityManage> lists = activityManageService.listActivityForPage(activityManage);
        PageInfo<ActivityManage> pageInfo = new PageInfo<ActivityManage>(lists);
        jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC).setData(pageInfo);
        return jsonResult;
    }

    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "queryActivityForDetail")
    public JsonResult queryActivityForDetail(Integer id, HttpServletRequest request, HttpServletResponse response) {
        JsonResult jsonResult = new JsonResult();
        ActivityManage activityManage = activityManageService.getActivityForDetail(id);
        jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC).setData(activityManage);
        return jsonResult;
    }

    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "modifyActivity")
    public JsonResult modifyActivity(ActivityManage activityManage, HttpServletRequest request, HttpServletResponse response) {
        JsonResult jsonResult = new JsonResult();
        int result = activityManageService.updateActivity(activityManage);
        if(result > 0){
            jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC);
        }
        return jsonResult;
    }

    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "deleteActivity")
    public JsonResult deleteActivity(Integer id, HttpServletRequest request, HttpServletResponse response) {
        JsonResult jsonResult = new JsonResult();
        int result = activityManageService.deleteActivityById(id);
        if(result > 0) {
            jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setData(JsonResult.APP_DEFINE_SUC);
        }
        return jsonResult;
    }

    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "addActivity")
    public JsonResult addActivity(ActivityManage activityManage, HttpServletRequest request, HttpServletResponse response) {
        JsonResult jsonResult = new JsonResult();
        activityManage.setCreateTime(new Date());
        int result = activityManageService.insertActivity(activityManage);
        if(result > 0){
            jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC);
        }
        return jsonResult;
    }
}
