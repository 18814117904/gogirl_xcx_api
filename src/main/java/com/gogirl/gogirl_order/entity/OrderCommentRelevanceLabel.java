package com.gogirl.gogirl_order.entity;

public class OrderCommentRelevanceLabel {
    private Integer id;

    private Integer orderCommentId;

    private Integer commentLabelId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrderCommentId() {
        return orderCommentId;
    }

    public void setOrderCommentId(Integer orderCommentId) {
        this.orderCommentId = orderCommentId;
    }

    public Integer getCommentLabelId() {
        return commentLabelId;
    }

    public void setCommentLabelId(Integer commentLabelId) {
        this.commentLabelId = commentLabelId;
    }
}