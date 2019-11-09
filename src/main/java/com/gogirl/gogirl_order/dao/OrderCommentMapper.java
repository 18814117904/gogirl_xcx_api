package com.gogirl.gogirl_order.dao;

import com.gogirl.gogirl_order.order_commons.dto.OrderComment;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by yinyong on 2018/10/19.
 */
@Mapper
@Repository
public interface OrderCommentMapper {

    int insertOrderComment(OrderComment orderComment);

    int updateOrderComment(OrderComment orderComment);

    List<OrderComment> listOrderCommentForPage(OrderComment orderComment);

    List<OrderComment> listOrderCommentForCMS(@Param("departmentId") Integer departmentId);

    OrderComment getOrderCommentForDetail(Integer orderId);
    
	List<OrderComment> listOrderCommentForListDetail(Integer orderServeId);
	List<OrderComment> listOrderServeCommentForListDetail(Integer orderServeId);
    List<OrderComment> queryMyCommentForPage(OrderComment orderComment);
}
