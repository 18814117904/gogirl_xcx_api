package com.gogirl.gogirl_order.service;

import com.gogirl.gogirl_order.dao.OrderCommentMapper;
import com.gogirl.gogirl_order.dao.OrderCommentRelevanceLabelMapper;
import com.gogirl.gogirl_order.entity.OrderCommentRelevanceLabel;
import com.gogirl.gogirl_order.order_commons.config.Constans;
import com.gogirl.gogirl_order.order_commons.dto.OrderComment;
import com.gogirl.gogirl_order.order_commons.dto.OrderManage;
import com.gogirl.gogirl_order.order_manage.dao.OrderManageMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

/**
 * Created by yinyong on 2018/10/19.
 */
@Service
public class OrderCommentService {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	@Resource
	OrderCommentRelevanceLabelMapper orderCommentRelevanceLabelMapper;
	
    
    public int updateOrderCommentByOrderAndTech(OrderComment comment) {
    	//插入评论
	    int row = orderCommentMapper.insertOrderComment(comment);
	    //插入评论标签
	    if(comment.getLabelSticker()!=null){
	    	String [] labels = comment.getLabelSticker().split(",");
	    	List<OrderCommentRelevanceLabel> list = new ArrayList<OrderCommentRelevanceLabel>();
	    	for(int i=0;i<labels.length;i++){
	    		try {
			    	OrderCommentRelevanceLabel item = new OrderCommentRelevanceLabel();
			    	item.setCommentLabelId(Integer.parseInt(labels[i]));
			    	item.setOrderCommentId(comment.getId());
			    	list.add(item);
				} catch (Exception e) {
					logger.info("添加标签id转int失败："+e.getMessage());
				}
	    	}
	    	if(list.size()>0){
		    	orderCommentRelevanceLabelMapper.insertList(list);
	    	}
	    }
      return row;
  }
    @Autowired
    private OrderCommentMapper orderCommentMapper;

    @Autowired
    private OrderManageMapper orderManageMapper;


    
    @Transactional
    public int insertOrderComment(OrderComment comment) {
        return orderCommentMapper.insertOrderComment(comment);
    }

    
    @Transactional
    public int updateOrderComment(OrderComment comment) {
        OrderComment orderComment = orderCommentMapper.getOrderCommentForDetail(comment.getOrderId());
        orderComment.setStatus(Constans.ORDER_COMMENT_EVALUATED);
        if(orderComment != null){
            OrderManage orderManage = new OrderManage();
            orderManage.setStatus(Constans.ORDER_STATUS_FINISH);
            orderManage.setId(comment.getOrderId());
            orderManageMapper.updateOrderManage(orderManage);
        }
        return orderCommentMapper.updateOrderComment(comment);
    }

    
    public List<OrderComment> listOrderCommentForPage(OrderComment orderComment) {
        return orderCommentMapper.listOrderCommentForPage(orderComment);
    }

    
    public List<OrderComment> listOrderCommentForCMS(Integer departmentId) {
        return orderCommentMapper.listOrderCommentForCMS(departmentId);
    }

    
    public OrderComment getOrderCommentForDetail(Integer orderId) {
        return orderCommentMapper.getOrderCommentForDetail(orderId);
    }

    
	public List<OrderComment> listOrderCommentForListDetail(Integer orderId) {
		return orderCommentMapper.listOrderCommentForListDetail(orderId);
	}
	public List<OrderComment> listOrderServeCommentForListDetail(Integer orderServeId) {
		return orderCommentMapper.listOrderServeCommentForListDetail(orderServeId);
	}
    public List<OrderComment> queryMyCommentForPage(OrderComment orderComment) {
        return orderCommentMapper.queryMyCommentForPage(orderComment);
    }
}
