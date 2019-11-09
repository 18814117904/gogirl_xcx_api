package com.gogirl.gogirl_store.store_user.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gogirl.gogirl_order.order_commons.utils.JsonResult;
import com.gogirl.gogirl_store.store_commons.dto.UserManage;
import com.gogirl.gogirl_store.store_commons.jms.SmsService;
import com.gogirl.gogirl_store.store_user.service.UserManageService;
import com.gogirl.gogirl_xcx.entity.GogirlToken;
import com.gogirl.gogirl_xcx.service.GogirlTokenService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import springfox.documentation.annotations.ApiIgnore;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yinyong on 2018/9/17.
 */

@Api(tags = { "9.店铺" },value = "店铺")
@RestController
@RequestMapping("user")
public class UserManageController {

    private final Logger logger = LoggerFactory.getLogger(UserManageController.class);

    @Autowired
    private UserManageService userManageService;

    @Autowired
    private SmsService smsService;
    @Resource
    GogirlTokenService gogirlTokenService;

    @ApiOperation(value = "查询当前店铺的美甲师,选推荐人")
    @RequestMapping(method={RequestMethod.GET},value = "queryUserForAll")
    public JsonResult queryUserForAll(String token) {
    	GogirlToken gt = gogirlTokenService.getTokenByToken_t(token);
    	if(gt==null){
    		return new JsonResult<>(false,JsonResult.TOKEN_NULL_CODE);
    	}
        JsonResult jsonResult = new JsonResult();
        UserManage userManage = new UserManage();
        userManage.setDepartmentId(gt.getUserTechnician().getDepartmentId());
        List<UserManage> lists = userManageService.listUserForAllNotQuit(userManage);
        jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC).setData(lists);
        return jsonResult;
    }
    @ApiIgnore
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "queryUserManageForPage")
    public JsonResult queryUserManageForPage(UserManage userManage, HttpServletRequest request, HttpServletResponse response){
        JsonResult jsonResult = new JsonResult();
        String pageNum = request.getParameter("pageNum");
        String pageSize = request.getParameter("pageSize");
        pageNum = pageNum.replace("=", "");
        pageSize = pageSize.replace("=", "");
        PageHelper.startPage(Integer.parseInt(pageNum),Integer.parseInt(pageSize));
        Map<String, Object> map = new HashMap<String, Object>();
        List<UserManage> lists = userManageService.listUserManageForPage(userManage);
        PageInfo<UserManage> pageInfo = new PageInfo<UserManage>(lists);
        jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC).setData(pageInfo);
        return jsonResult;
    }

    @ApiIgnore
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "queryUserManageForAll")
    public JsonResult queryUserManageForAll(UserManage userManage, HttpServletRequest request, HttpServletResponse response) {
        JsonResult jsonResult = new JsonResult();
        List<UserManage> lists = userManageService.listUserManageForAll(userManage);
        jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC).setData(lists);
        return jsonResult;
    }

    @ApiIgnore
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "queryUserForAllNotQuit")
    public JsonResult queryUserForAllNotQuit(UserManage userManage, HttpServletRequest request, HttpServletResponse response) {
        JsonResult jsonResult = new JsonResult();
        List<UserManage> lists = userManageService.listUserForAllNotQuit(userManage);
        jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC).setData(lists);
        return jsonResult;
    }

    @ApiIgnore
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "modifyUserManage")
    public JsonResult modifyUserManageById(UserManage userManage, HttpServletRequest request, HttpServletResponse response){
        JsonResult jsonResult = new JsonResult();
        int result = userManageService.updateUserManage(userManage);
        if(result > 0){
            jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC);
        }
        return jsonResult;
    }

    @ApiIgnore
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "queryUserDetail")
    public JsonResult queryUserDetail(Integer id, HttpServletRequest request, HttpServletResponse response){
        JsonResult jsonResult = new JsonResult();
        UserManage userManage = userManageService.getUserManageForDetail(id);
        jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC).setData(userManage);
        return jsonResult;
    }

    @ApiIgnore
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value="checkLogin")
    public JsonResult checkLogin(UserManage userManage, HttpServletRequest request, HttpServletResponse response){
        JsonResult jsonResult = new JsonResult();
        UserManage userManage1 = userManageService.checkLogin(userManage);
        if(userManage1 != null){
            jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC).setData(userManage1);
        }
        return jsonResult;
    }

    @ApiIgnore
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "deleteUserManageById")
    public JsonResult deleteUserManageById(Integer id, HttpServletRequest request, HttpServletResponse response){
        JsonResult jsonResult = new JsonResult();
        int result = userManageService.deleteUserManageById(id);
        if(result > 0){
            jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC);
        }
        return jsonResult;
    }

    @ApiIgnore
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "addUserManage")
    public JsonResult addUserManage(UserManage userManage, HttpServletRequest request, HttpServletResponse response){
        JsonResult jsonResult = new JsonResult();
        int result = userManageService.insertUserManage(userManage);
        if(result > 0){
            jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC);
        }
        return jsonResult;
    }
}
