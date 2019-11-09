package com.gogirl.gogirl_order.controller;

import java.util.List;

import com.gogirl.gogirl_order.order_commons.dto.OrderComment;

public class CommentParam {
	List<OrderComment> commentList = null;
	String token;

	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public List<OrderComment> getCommentList() {
		return commentList;
	}
	public void setCommentList(List<OrderComment> commentList) {
		this.commentList = commentList;
	}
	
}
