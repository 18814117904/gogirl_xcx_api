package com.gogirl.gogirl_technician.technician_user.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gogirl.gogirl_order.order_commons.utils.JsonResult;
import com.gogirl.gogirl_technician.technician_commons.dto.TechnicianManage;
import com.gogirl.gogirl_technician.technician_user.service.TechnicianManageService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.*;

/**
 * Created by yinyong on 2018/9/18.
 */
@RestController
@RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "technician")
public class TechnicianManageController {

    private final static Logger logger = LoggerFactory.getLogger(TechnicianManageController.class);

    @Autowired
    private TechnicianManageService technicianManageService;

    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "queryTechnicianForPage")
    public JsonResult queryTechnicianForPage(TechnicianManage technicianManage, HttpServletRequest request, HttpServletResponse response){
        JsonResult jsonResult = new JsonResult();
        String pageNum = request.getParameter("pageNum");
        String pageSize = request.getParameter("pageSize");
        PageHelper.startPage(Integer.parseInt(pageNum),Integer.parseInt(pageSize));
        List<TechnicianManage> lists = technicianManageService.listTechnicianForPage(technicianManage);
        PageInfo<TechnicianManage> pageInfo = new PageInfo<TechnicianManage>(lists);
        jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC).setData(pageInfo);
        return jsonResult;
    }

    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "queryTechnicianManageForAll")
    public JsonResult queryTechnicianManageForAll(Integer departmentId, HttpServletRequest request, HttpServletResponse response){
        JsonResult jsonResult = new JsonResult();
        List<TechnicianManage> listTechnicianManage = technicianManageService.listTechnicianManageForAll(departmentId);
        jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC).setData(listTechnicianManage);
        return jsonResult;
    }

    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "modifyTechnician")
    public JsonResult modifyTechnician(TechnicianManage technicianManage, HttpServletRequest request, HttpServletResponse response){
        JsonResult jsonResult = new JsonResult();
        logger.info("修改美甲师：" + technicianManage);
        int result = technicianManageService.updateTechnicianManageById(technicianManage);
        if(result > 0){
            jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC);
        }
        return jsonResult;
    }

    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "deleteTechnicianById")
    public JsonResult deleteTechnicianById(Integer id, HttpServletRequest request, HttpServletResponse response){
        JsonResult jsonResult = new JsonResult();
        logger.info("删除美甲师：id = " + id);
        int result = technicianManageService.deleteTechnicianManageById(id);
        if(result > 0){
            jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC);
        }
        return jsonResult;
    }

    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "addTechnician")
    public JsonResult addTechnician(TechnicianManage technicianManage, HttpServletRequest request, HttpServletResponse response){
        JsonResult jsonResult = new JsonResult();
        logger.info("店铺新增美甲师：" + technicianManage);
        int result = technicianManageService.insertTechnicianManage(technicianManage);
        if(result > 0){
            jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC);
        }
        return jsonResult;
    }
}
