package com.gogirl.gogirl_service.controller.produce;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gogirl.gogirl_order.order_commons.utils.JsonResult;
import com.gogirl.gogirl_service.entity.Produce;
import com.gogirl.gogirl_service.service.service.produce.ProduceService;

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
 * Created by yinyong on 2018/8/23.
 */

@Api(tags = { "5.服务管理" },value = "服务管理")
@RestController
@RequestMapping(value = "produce")
public class ProduceController {

    @Autowired
    private ProduceService produceService;
    
	@ApiOperation(value = "查询款式详情", notes = "查询款式详情")
    @RequestMapping(method={RequestMethod.GET},value = "queryProduceDetail")
    public JsonResult queryProduceDetail(Integer id, HttpServletRequest request, HttpServletResponse response){
        JsonResult jsonResult = new JsonResult();
        Produce produce = produceService.getProduceForDetail(id);
        jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC).setData(produce);
        return jsonResult;
    }
	@ApiOperation(value = "查询款式详情,produce和serve分开", notes = "查询款式详情")
    @RequestMapping(method={RequestMethod.GET},value = "queryProduceDetail2")
    public JsonResult queryProduceDetail2(Integer id, HttpServletRequest request, HttpServletResponse response){
        JsonResult jsonResult = new JsonResult();
        Map<String, Object> map = new HashMap<String, Object>();
        Produce produce = produceService.getProduceForDetail(id);
        map.put("produce", produce);
        map.put("serve", produce.getServe());
        jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC).setData(map);
        return jsonResult;
    }

    @ApiIgnore()
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "queryProduceForPage")
    public JsonResult queryProduceForPage(Produce produce, HttpServletRequest request, HttpServletResponse response){
        JsonResult jsonResult = new JsonResult();
        String pageNum = request.getParameter("pageNum");
        String pageSize = request.getParameter("pageSize");
        PageHelper.startPage(Integer.parseInt(pageNum),Integer.parseInt(pageSize));
        List<Produce> lists = produceService.listProduceForPage(produce);
        PageInfo<Produce> pageInfo = new PageInfo<Produce>(lists);
        jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC).setData(pageInfo);
        return jsonResult;
    }

    @ApiIgnore()
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "queryProduceWithPrice")
    public JsonResult queryProduceWithPrice (HttpServletRequest request, HttpServletResponse response){
        JsonResult jsonResult = new JsonResult();
        List<Produce> list = produceService.queryProduceWithPrice();
        jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC).setData(list);
        return jsonResult;
    }

    @ApiIgnore()
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "deleteProdecu")
    public JsonResult deleteProdecu(Integer id, HttpServletRequest request, HttpServletResponse response){
        JsonResult jsonResult = new JsonResult();
        int result = produceService.deleteProdecu(id);
        if(result > 0){
            jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC);
        }
        return jsonResult;
    }

    @ApiIgnore()
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "addProduce")
    public JsonResult addProduce(Produce produce, HttpServletRequest request, HttpServletResponse response){
        JsonResult jsonResult = new JsonResult();
        int result = produceService.insertProduce(produce);
        if(result > 0){
            jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC);
        }
        return jsonResult;
    }


    @ApiIgnore()
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "queryProduceAll")
    public List<Produce> queryProduceAll(HttpServletRequest request, HttpServletResponse response){
        return produceService.listProduceAll();
    }

    @ApiIgnore()
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "queryProduceByServe")
    public JsonResult queryProduceByServe(Integer serveId, HttpServletRequest request, HttpServletResponse response){
        JsonResult jsonResult = new JsonResult();
        List<Produce> lists = produceService.listProduceByServe(serveId);
        jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC).setData(lists);
        return jsonResult;
    }

    @ApiIgnore()
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "queryProduceNoDesignationServe")
    public JsonResult queryProduceNoDesignationServe(Integer serveId, HttpServletRequest request, HttpServletResponse response){
        JsonResult jsonResult = new JsonResult();
        List<Produce> list = produceService.listProduceNoDesignationServe(serveId);
        jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC).setData(list);
        return jsonResult;
    }

    @ApiIgnore()
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "queryProduceNoDesignationAllServe")
    public JsonResult queryProduceNoDesignationAllServe(HttpServletRequest request, HttpServletResponse response){
        JsonResult jsonResult = new JsonResult();
        List<Produce> list = produceService.listProduceNoDesignationAllServe();
        jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC).setData(list);
        return jsonResult;
    }

    @ApiIgnore()
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "modifyProduce")
    public JsonResult modifyProduce(Produce produce, HttpServletRequest request, HttpServletResponse response){
        JsonResult jsonResult = new JsonResult();
        int result = produceService.updateProduceById(produce);
        if(result > 0){
            jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC);
        }
        return jsonResult;
    }

    @ApiIgnore()
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "topProduce")
    public JsonResult topProduce(Integer id, HttpServletRequest request, HttpServletResponse response){
        JsonResult jsonResult = new JsonResult();
        int result = produceService.topProduce(id);
        if(result > 0){
            jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC);
        }
        return jsonResult;
    }
}
