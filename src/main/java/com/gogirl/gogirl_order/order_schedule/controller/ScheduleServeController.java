package com.gogirl.gogirl_order.order_schedule.controller;

import com.gogirl.gogirl_order.order_commons.dto.ScheduleManage;
import com.gogirl.gogirl_order.order_commons.dto.ScheduleServe;
import com.gogirl.gogirl_order.order_commons.utils.JsonResult;
import com.gogirl.gogirl_order.order_schedule.service.ScheduleServeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by yinyong on 2018/9/28.
 */

@RestController
@RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "scheduleserve")
public class ScheduleServeController {

    @Autowired
    private ScheduleServeService scheduleServeService;

    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "queryScheduleServe")
    public JsonResult queryScheduleServe(Integer departmentId, String days, HttpServletRequest request, HttpServletResponse response){
        JsonResult jsonResult = new JsonResult();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            days = simpleDateFormat.format(simpleDateFormat.parse(days));
        } catch (ParseException e) {
            e.printStackTrace();
            jsonResult.setSuccess(JsonResult.CODE_ERROR).setMessage(JsonResult.APP_DEFINE_ERR);
            return jsonResult;
        }
        List<ScheduleServe> list = scheduleServeService.listScheduleServe(departmentId, days);
        jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC).setData(list);
        return jsonResult;
    }

    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "checkScheduleServeByTechnicianId")
    public JsonResult checkScheduleServeByTechnicianId(Integer technicianId, HttpServletRequest request, HttpServletResponse response){
        JsonResult jsonResult = new JsonResult();
        List<ScheduleServe> listScheduleServe = scheduleServeService.checkScheduleServeByTechnicianId(technicianId);
        if(listScheduleServe != null && listScheduleServe.size() > 0){
            jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC);
        }
        return jsonResult;
    }

    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "modifyScheduleServe")
    public JsonResult modifyScheduleServe(@RequestBody  ScheduleManage scheduleManage, HttpServletRequest request, HttpServletResponse response){
        JsonResult jsonResult = new JsonResult();
        jsonResult = scheduleServeService.updateScheduleServe(scheduleManage);
        /*int result =
        if(result > 0){
            jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC);
        }*/
        return jsonResult;
    }

    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "modifyScheduleServeTechnician")
    public JsonResult modifyScheduleServeTechnician(Integer id, Integer technicianId, HttpServletRequest request, HttpServletResponse response){
        JsonResult jsonResult = new JsonResult();
        int result = scheduleServeService.updateScheduleServeForTechnician(id, technicianId);
        if(result > 0){
            jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC);
        }
        return jsonResult;
    }

    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "deleteScheduleServeById")
    public JsonResult deleteScheduleServeById(Integer id, HttpServletRequest request, HttpServletResponse response){
        JsonResult jsonResult = new JsonResult();
        int result = scheduleServeService.deleteScheduleServeById(id);
        if(result > 0){
            jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC);
        }
        return jsonResult;
    }

    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "addScheduleServe")
    public JsonResult addScheduleServe(ScheduleServe scheduleServe, org.apache.catalina.servlet4preview.http.HttpServletRequest request, HttpServletResponse response){
        JsonResult jsonResult = new JsonResult();
        scheduleServe = scheduleServeService.insertScheduleServeByUpdate(scheduleServe);
        jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC).setData(scheduleServe);
        return jsonResult;
    }
}
