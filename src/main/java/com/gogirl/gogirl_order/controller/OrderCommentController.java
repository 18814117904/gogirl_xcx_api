package com.gogirl.gogirl_order.controller;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gogirl.gogirl_order.order_commons.config.Constans;
import com.gogirl.gogirl_order.order_commons.dto.ImageManage;
import com.gogirl.gogirl_order.order_commons.dto.OrderComment;
import com.gogirl.gogirl_order.order_commons.dto.OrderManage;
import com.gogirl.gogirl_order.order_commons.dto.OrderServe;
import com.gogirl.gogirl_order.order_commons.utils.JsonResult;
import com.gogirl.gogirl_order.order_manage.service.OrderManageService;
import com.gogirl.gogirl_order.order_manage.service.OrderServeService;
import com.gogirl.gogirl_order.service.OrderCommentService;
import com.gogirl.gogirl_xcx.entity.GogirlToken;
import com.gogirl.gogirl_xcx.service.GogirlTokenService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import springfox.documentation.annotations.ApiIgnore;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.util.*;
/**
 * 订单评价
 */
@Api(tags={"12.评论"},value="评论")
@RestController
@RequestMapping("ordercomment")
public class OrderCommentController {
    Logger logger = LoggerFactory.getLogger(OrderCommentController.class);
    @Resource
    private OrderCommentService orderCommentService;
    @Resource
    private OrderManageService orderManageService;
    @Resource
    private OrderServeService orderServeService;
    @Resource
    GogirlTokenService gogirlTokenService;
    @Value("${picture-path}")
    private String picturePath;

    @Value("${picture-prefix}")
    private String picturePrefix;

    /*查询订单评论，一个美甲师1个评价*/
    @ApiOperation(value="查询订单评论，一个美甲师1个评价")
    @RequestMapping(method={RequestMethod.GET},value = "queryOrderCommentForListDetail")
    public JsonResult<List<OrderComment>> queryOrderCommentForListDetail(Integer orderId){
    	List<OrderComment> list = orderCommentService.listOrderCommentForListDetail(orderId);
        return new JsonResult<>(JsonResult.CODE_SUCCESS, JsonResult.APP_DEFINE_SUC,list);
    }

    /*查询订单服务评论，一个美甲师1个评价*/
    @ApiOperation(value="查询订单服务评论，一个美甲师1个评价")
    @RequestMapping(method={RequestMethod.GET},value = "queryOrderServeCommentForListDetail")
    public JsonResult<List<OrderComment>> queryOrderServeCommentForListDetail(Integer orderServeId){
    	List<OrderComment> list = orderCommentService.listOrderServeCommentForListDetail(orderServeId);
        return new JsonResult<>(JsonResult.CODE_SUCCESS, JsonResult.APP_DEFINE_SUC,list);
    }
    
    /*订单服务评论*/
    @RequestMapping(method={RequestMethod.POST},value = "addOrModifyOrderComment")
    @ApiOperation(value="订单服务评论", notes="[{\"labelSticker\": \"1,2\",\"level\": 4,\"orderId\": 5811,\"orderServeId\": 9053,\"picturePath\": \"string\",\"remark\": \"梁正菊__9ABD1D8743CFBD7E7DD5AB99A9A42.jpg\",\"technicianId\": 108,\"orderServe\":{\"commentScore\":5}}]")
    public JsonResult<Integer> addOrModifyOrderComment(@RequestBody CommentParam param){
        logger.info("订单评论");
        List<OrderComment> list = param.getCommentList();
    	if(list==null){
    		return new JsonResult<>(false,"入参list为null");
    	}
    	int row = 0;
    	Integer orderId = null;
    	Integer orderServeId = null;
    	Integer commentScore = null;//总体评分
    	for(int i=0;i<list.size();i++){
    		OrderComment item = list.get(i);
    		item.setFinishTime(new Date());
    		item.setStatus(Constans.ORDER_COMMENT_FINISH);
            int result = orderCommentService.updateOrderCommentByOrderAndTech(item);
            row+=result;
            if(item.getOrderId()!=null){//获得orderId
                orderId = item.getOrderId();
            }
            if(item.getOrderServeId()!=null){//获得orderServeId
            	orderServeId = item.getOrderServeId();
            }
            if(item.getOrderServe()!=null&&item.getOrderServe().getCommentScore()!=null){//获得commentScore
            	commentScore = item.getOrderServe().getCommentScore();
            }
    	}
    	//修改订单服务状态
	    if(orderId!=null){
	        OrderServe orderServe = new OrderServe();
	        orderServe.setId(orderServeId);
	        orderServe.setExecutionStatus(Constans.ORDER_EXECUTION_FINISH);
	        orderServe.setCommentScore(commentScore);
	        orderServeService.updateOrderService(orderServe);
	    }
    	
    	//修改订单状态
	    if(orderId!=null){
	        OrderManage orderManage = new OrderManage();
	        orderManage.setStatus(Constans.ORDER_STATUS_FINISH);
	        orderManage.setId(orderId);
	        orderManageService.updateOrderStatus(orderManage);
	    }

        return new JsonResult<>(true,JsonResult.APP_DEFINE_SUC,row);
    }

    /*美甲师查自己的评论列表*/
    @ApiOperation(value="美甲师查自己的评论列表")
    @RequestMapping(method={RequestMethod.GET},value = "queryMyCommentForPage")
    public JsonResult<PageInfo<OrderComment>> queryMyCommentForPage(String token,Integer pageSize,Integer pageNum){
    	if(pageSize==null||pageNum==null){
    		return new JsonResult<>(false,"pageSize或pageNum为空");
    	}
    	if(token==null){
    		return new JsonResult<>(false,"token为空");
    	}
    	GogirlToken gogirlToken=gogirlTokenService.getTokenByToken_t(token);
    	if(gogirlToken==null){
    		return new JsonResult<>(false,JsonResult.TOKEN_NULL_CODE);
    	}
    	if(gogirlToken.getUserTechnician()==null||gogirlToken.getUserTechnician().getTechnicianId()==null){
    		return new JsonResult<>(false,JsonResult.NO_DEPARTMENT_CODE);
    	}
    	OrderComment orderComment = new OrderComment();
    	orderComment.setTechnicianId(gogirlToken.getUserTechnician().getTechnicianId());
        PageHelper.startPage(pageNum,pageSize);
    	List<OrderComment> list = orderCommentService.queryMyCommentForPage(orderComment);
        PageInfo<OrderComment> pageInfo = new PageInfo<OrderComment>(list);
        return new JsonResult<>(JsonResult.CODE_SUCCESS, JsonResult.APP_DEFINE_SUC,pageInfo);
    }
    
    
    
    @ApiIgnore
    @Deprecated
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "queryOrderCommentForPage")
    public JsonResult queryOrderCommentForPage(OrderComment orderComment, HttpServletRequest request, HttpServletResponse response){
        JsonResult jsonResult = new JsonResult();
        String pageNum = request.getParameter("pageNum");
        String pageSize = request.getParameter("pageSize");
        PageHelper.startPage(Integer.parseInt(pageNum),Integer.parseInt(pageSize));
        List<OrderComment> lists = orderCommentService.listOrderCommentForPage(orderComment);
        PageInfo<OrderComment> pageInfo = new PageInfo<OrderComment>(lists);
        jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC).setData(pageInfo);
        return jsonResult;
    }

    @ApiIgnore
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "queryOrderCommentForCMS")
    public JsonResult queryOrderCommentForCMS(Integer departmentId, HttpServletRequest request, HttpServletResponse response) {
        JsonResult jsonResult = new JsonResult();
        String pageNum = request.getParameter("pageNum");
        String pageSize = request.getParameter("pageSize");
        PageHelper.startPage(Integer.parseInt(pageNum),Integer.parseInt(pageSize));
        List<OrderComment> lists = orderCommentService.listOrderCommentForCMS(departmentId);
        PageInfo<OrderComment> pageInfo = new PageInfo<OrderComment>(lists);
        jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC).setData(pageInfo);
        return jsonResult;
    }

    @ApiIgnore
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "queryOrderCommentForDetail")
    public JsonResult queryOrderCommentForDetail(Integer orderId, HttpServletRequest request, HttpServletResponse response){
        JsonResult jsonResult = new JsonResult();
        OrderComment orderComment = orderCommentService.getOrderCommentForDetail(orderId);
        jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC).setData(orderComment);
        return jsonResult;
    }


    @ApiIgnore
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value = "modifyOrderComment")
    public JsonResult modifyOrderComment(OrderComment orderComment, HttpServletRequest request, HttpServletResponse response){
        JsonResult jsonResult = new JsonResult();
        orderComment.setFinishTime(new Date());
        logger.info("订单评论开始... 提交订单评论数据：" + orderComment);
        // 新增评论图片在跳转层操作
        /*if(formData.length != 0){
        List<String> list = new ArrayList<String>();
        for(MultipartFile file: formData){
            File filePicture = new File(picturePath + file.getOriginalFilename());
            if (!filePicture.getParentFile().exists()) {
                filePicture.getParentFile().mkdirs();
            }
            if(!filePicture.exists()){
                try {
                    filePicture.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                    return jsonResult;
                }
            }
            try {
                file.transferTo(filePicture);
                list.add(picturePrefix + file.getOriginalFilename());
            } catch (IOException e) {
                e.printStackTrace();
                return jsonResult;
            }
        }
        orderComment.setPicturePath(String.join(",", list));
    }*/
        orderComment.setStatus(Constans.ORDER_COMMENT_FINISH);
        int result = orderCommentService.updateOrderComment(orderComment);
        if(result > 0){
            jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC);
            logger.info("订单评论成功：" + orderComment);
        }
        return jsonResult;
    }

    @ApiIgnore
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value="addPicture")
    public JsonResult addPicture (String typeName, @RequestParam(value = "files", required = false) MultipartFile[] files, HttpServletRequest request, HttpServletResponse response) {
        JsonResult jsonResult = new JsonResult();
        if(files.length == 0){
            jsonResult.setSuccess(JsonResult.CODE_ERROR).setMessage("上传图片为空");
            return jsonResult;
        }
        List<ImageManage> list = new ArrayList<ImageManage>();
        for(MultipartFile file: files){
            File filePicture = new File(picturePath+file.getOriginalFilename());
            if (!filePicture.getParentFile().exists()) {
                filePicture.getParentFile().mkdirs();
            }
            if(!filePicture.exists()){
                try {
                    filePicture.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                    return jsonResult;
                }
            }
            try {
                file.transferTo(filePicture);
            } catch (IOException e) {
                e.printStackTrace();
                return jsonResult;
            }
        }
        jsonResult.setSuccess(JsonResult.CODE_SUCCESS).setMessage(JsonResult.APP_DEFINE_SUC);
        return jsonResult;
    }
}
