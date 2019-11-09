package com.gogirl.gogirl_technician.technician_user.controller;

import com.gogirl.gogirl_order.order_commons.utils.JsonResult;
import com.gogirl.gogirl_technician.technician_commons.dto.UserManage;

import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by yinyong on 2019/4/11.
 */
@RestController
public class TestModelController {


    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "getValidator")
    public JsonResult getValidator(@Validated(GroupA.class) UserManage userManage, BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response) {
        JsonResult jsonResult = new JsonResult();
        if(bindingResult.hasErrors()) {
            jsonResult.setSuccess(JsonResult.CODE_ERROR).setMessage(bindingResult.getFieldError().getDefaultMessage());
        }else {
            jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC);
        }
        return jsonResult;
    }
}
