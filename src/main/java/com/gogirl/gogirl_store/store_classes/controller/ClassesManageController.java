package com.gogirl.gogirl_store.store_classes.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gogirl.gogirl_order.order_commons.utils.JsonResult;
import com.gogirl.gogirl_store.store_classes.service.ClassesManageService;
import com.gogirl.gogirl_store.store_commons.dto.ClassesManage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.List;



@RestController
@RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "classes")
public class ClassesManageController {

    private Logger logger = LoggerFactory.getLogger(ClassesManageController.class);

    @Autowired
    private ClassesManageService classesManageService;

    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "queryClassesManageForPage")
    public JsonResult queryClassesManageForPage(ClassesManage classesManage, HttpServletRequest request, HttpServletResponse response){
        JsonResult jsonResult = new JsonResult();
        String pageNum = request.getParameter("pageNum");
        String pageSize = request.getParameter("pageSize");
        PageHelper.startPage(Integer.parseInt(pageNum),Integer.parseInt(pageSize));
        List<ClassesManage> lists = classesManageService.listClassesManageForPage(classesManage);
        PageInfo<ClassesManage> pageInfo = new PageInfo<ClassesManage>(lists);
        jsonResult.setSuccess(true).setMessage(JsonResult.APP_DEFINE_SUC).setData(pageInfo);
        return jsonResult;
    }

    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "queryClassesManage")
    public JsonResult queryClassesManage(Integer departmentId, HttpServletRequest request, HttpServletResponse response){
        JsonResult jsonResult = new JsonResult();
        List<ClassesManage> list = classesManageService.listClassesManage(departmentId);
        jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC).setData(list);
        return jsonResult;
    }

    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "modifyClassesManage")
    public JsonResult modifyClassesManage(ClassesManage classesManage, HttpServletRequest request, HttpServletResponse response){
        JsonResult jsonResult = new JsonResult();
        int result = classesManageService.updateClassesManage(classesManage);
        logger.info("修改班次：" + classesManage);
        if(result > 0){
            jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC);
        }
        return jsonResult;
    }

    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "deleteClassesManageById")
    public JsonResult deleteClassesManageById(Integer id, HttpServletRequest request, HttpServletResponse response){
        JsonResult jsonResult = new JsonResult();
        int result = classesManageService.deleteClassesManageById(id);
        logger.info("删除班次。。。" );
        if(result > 0){
            jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC);
        }
        return jsonResult;
    }

    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "addClassesManage")
    public JsonResult addClassesManage(ClassesManage classesManage, HttpServletRequest request, HttpServletResponse response){
        JsonResult jsonResult = new JsonResult();
        classesManageService.insertClassesManage(classesManage);
        logger.info("新增班次：" + classesManage);
        jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC);
        return jsonResult;
    }
}
