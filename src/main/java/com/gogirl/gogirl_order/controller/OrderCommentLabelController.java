package com.gogirl.gogirl_order.controller;

import com.gogirl.gogirl_order.order_commons.dto.OrderCommentLabel;
import com.gogirl.gogirl_order.order_commons.utils.JsonResult;
import com.gogirl.gogirl_order.service.OrderCommentLabelService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.List;

/**
 * Created by yinyong on 2018/10/22.
 */

@Api(tags={"12.评论"},value="评论")
@RestController
@RequestMapping("orderlabel")
public class OrderCommentLabelController {

    @Autowired
    private OrderCommentLabelService orderCommentLabelService;

    /*查询所有标签*/
    @ApiOperation(value="查询所有标签")
    @RequestMapping(method={RequestMethod.GET},value = "queryOrderLabel")
    public JsonResult queryOrderLabel(HttpServletRequest request, HttpServletResponse response){
        JsonResult jsonResult = new JsonResult();
        List<OrderCommentLabel> listOrderCommentLabel = orderCommentLabelService.listOrderCommentLabel();
        jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC).setData(listOrderCommentLabel);
        return jsonResult;
    }
}
