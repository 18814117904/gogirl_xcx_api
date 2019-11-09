package com.gogirl.gogirl_service.controller.type;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gogirl.gogirl_order.order_commons.utils.JsonResult;
import com.gogirl.gogirl_service.entity.Type;
import com.gogirl.gogirl_service.service.service.type.TypeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yinyong on 2018/9/4.
 */

@RestController
@RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "type")
public class TypeController {

    @Autowired
    private TypeService typeService;

    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "queryTypeForPage")
    public JsonResult queryTypeForPage(HttpServletRequest request, HttpServletResponse response){
        JsonResult jsonResult = new JsonResult();
        String pageNum = request.getParameter("pageNum");
        String pageSize = request.getParameter("pageSize");
        PageHelper.startPage(Integer.parseInt(pageNum),Integer.parseInt(pageSize));
        List<Type> lists = typeService.listType();
        PageInfo<Type> pageInfo = new PageInfo<Type>(lists);
        jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC).setData(pageInfo);
        return jsonResult;
    }

    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "queryType")
    public JsonResult queryLabelOrType(HttpServletRequest request, HttpServletResponse response){
        JsonResult jsonResult = new JsonResult();
        List<Type> lists = typeService.listType();
        jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC).setData(lists);
        return jsonResult;
    }

    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "queryTypeForDetail")
    public Type queryTypeForDetail(Integer id, HttpServletRequest request, HttpServletResponse responsep){
        return typeService.queryTypeForDetail(id);
    }

    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "modifyType")
    public JsonResult modifyType(Type type, HttpServletRequest request, HttpServletResponse response){
        JsonResult jsonResult = new JsonResult();
        int result = typeService.updateType(type);
        if(result > 0){
            jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC);
        }
        return jsonResult;
    }

    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "deleteTypeById")
    public JsonResult deleteTypeById(Integer id, HttpServletRequest request, HttpServletResponse response){
        JsonResult jsonResult = new JsonResult();
        int result = typeService.deleteTypeById(id);
        if(result > 0){
            jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC);
        }
        return jsonResult;
    }

    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "addType")
    public JsonResult addType(Type type, String createUserName, HttpServletRequest request, HttpServletResponse response){
        JsonResult jsonResult = new JsonResult();
        int result = typeService.insertType(type, createUserName);
        if(result > 0){
            jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC);
        }
        return jsonResult;
    }
}
