package com.gogirl.gogirl_service.controller.broadcast;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gogirl.gogirl_order.order_commons.utils.JsonResult;
import com.gogirl.gogirl_service.entity.Broadcast;
import com.gogirl.gogirl_service.service.service.brodacast.BroadcastService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import springfox.documentation.annotations.ApiIgnore;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yinyong on 2018/9/11.
 */

@RestController
@RequestMapping("broadcast")
@Api(tags = { "3.轮播图" },value = "轮播图")
public class BroadcastController {

    @Autowired
    private BroadcastService broadcastService;
	@ApiOperation(value = "1.查询轮播图列表", notes = "查询轮播图列表")
    @RequestMapping(method={RequestMethod.GET},value = "queryBroadcast")
    public JsonResult queryBroadcast(HttpServletRequest request, HttpServletResponse response){
        JsonResult jsonResult = new JsonResult();
        List<Broadcast> list = broadcastService.listBroadcast();
        jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC).setData(list);
        return jsonResult;
    }
    @ApiIgnore()
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "queryBroadcastForPage")
    public JsonResult queryBroadcastForPage(Broadcast broadcast, HttpServletRequest request, HttpServletResponse response){
        JsonResult jsonResult = new JsonResult();
        String pageNum = request.getParameter("pageNum");
        String pageSize = request.getParameter("pageSize");
        PageHelper.startPage(Integer.parseInt(pageNum),Integer.parseInt(pageSize));
        List<Broadcast> list = broadcastService.listBroadcastForPage(broadcast);
        PageInfo<Broadcast> pageInfo = new PageInfo<Broadcast>(list);
        jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC).setData(pageInfo);
        return jsonResult;
    }


    @ApiIgnore()
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "queryBroadcastForDetail")
    public JsonResult queryBroadcastForDetail(Integer id, HttpServletRequest request, HttpServletResponse response){
        JsonResult jsonResult = new JsonResult();
        Broadcast broadcast = broadcastService.getBroadcastForDetail(id);
        jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC).setData(broadcast);
        return jsonResult;
    }

    @ApiIgnore()
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "modifyBroadcast")
    public JsonResult modifyBroadcast(Broadcast broadcast, HttpServletRequest request, HttpServletResponse response){
        JsonResult jsonResult = new JsonResult();
        int result = broadcastService.updateBroadcast(broadcast);
        if(result > 0){
            jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC);
        }
        return jsonResult;
    }

    @ApiIgnore()
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "deleteBroadcast")
    public JsonResult deleteBroadcast(Integer id, HttpServletRequest request, HttpServletResponse response){
        JsonResult jsonResult = new JsonResult();
        int result = broadcastService.deleteBreadcast(id);
        if(result > 0){
            jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC);
        }
        return jsonResult;
    }

    @ApiIgnore()
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "addBroadcast")
    public JsonResult addBroadcast(Broadcast broadcast, HttpServletRequest request, HttpServletResponse response){
        JsonResult jsonResult = new JsonResult();
        int result = broadcastService.insertBroadcast(broadcast);
        if(result > 0){
            jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC);
        }
        return jsonResult;
    }

    @ApiIgnore()
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "topTimeBroadcast")
    public JsonResult topTimeBroadcast(Integer id, HttpServletRequest request, HttpServletResponse response){
        JsonResult jsonResult = new JsonResult();
        int result = broadcastService.topTimeBroadcast(id);
        if(result > 0 ){
            jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC);
        }
        return jsonResult;
    }

}
