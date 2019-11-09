//package com.gogirl.gogirl_service.controller.label;
//
//import com.github.pagehelper.PageHelper;
//import com.github.pagehelper.PageInfo;
//import com.gogirl.gogirl_service.entity.Label;
//import com.gogirl.gogirl_service.entity.Type;
//import com.gogirl.gogirl_service.service.service.label.LabelService;
//import com.gogirl.gogirl_service.utils.JsonResult;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * Created by yinyong on 2018/9/3.
// */
//
//@RestController
//@RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "label")
//public class LabelController {
//
//    @Autowired
//    private LabelService labelService;
//
//    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "queryLabelForPage")
//    public JsonResult queryTypeForPage(HttpServletRequest request, HttpServletResponse response){
//        JsonResult jsonResult = new JsonResult();
//        String pageNum = request.getParameter("pageNum");
//        String pageSize = request.getParameter("pageSize");
//        PageHelper.startPage(Integer.parseInt(pageNum),Integer.parseInt(pageSize));
//        List<Label> lists = labelService.listLabel();
//        PageInfo<Label> pageInfo = new PageInfo<Label>(lists);
//        jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC).setData(pageInfo);
//        return jsonResult;
//    }
//
//    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "queryLabel")
//    public JsonResult queryLabel(Label label, HttpServletRequest request, HttpServletResponse response){
//        JsonResult jsonResult = new JsonResult();
//        List<Label> lists = labelService.listLabel();
//        jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC).setData(lists);
//        return jsonResult;
//    }
//
//    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "addLabel")
//    public JsonResult addLabel(Label label, String createUserName, HttpServletRequest request, HttpServletResponse response){
//        JsonResult jsonResult = new JsonResult();
//        int result = labelService.insertLabel(label, createUserName);
//        if(result > 0){
//            jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC);
//        }
//        return jsonResult;
//    }
//
//    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "deleteLabelById")
//    public JsonResult deleteLabelById(Integer id, HttpServletRequest request, HttpServletResponse response){
//        JsonResult jsonResult = new JsonResult();
//        int result = labelService.deleteLabelById(id);
//        if(result > 0){
//            jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC);
//        }
//        return jsonResult;
//    }
//
//    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "modifyLabel")
//    public JsonResult modifyLabel(Label label, HttpServletRequest request, HttpServletResponse response){
//        JsonResult jsonResult = new JsonResult();
//        int result = labelService.updateLabel(label);
//        if(result > 0){
//            jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC);
//        }
//        return jsonResult;
//    }
//}
