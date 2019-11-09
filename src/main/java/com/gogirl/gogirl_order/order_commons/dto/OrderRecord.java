package com.gogirl.gogirl_order.order_commons.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by yinyong on 2018/12/3.
 */
public class OrderRecord implements Serializable {

    private Integer id;  //主键id
    private Integer orderId; //关联订单id
    private Integer orderServeId;  //关联订单服务id
    private String fixHour;  //确定款式时间
    private Integer isTimeOut;  //是否超时
    private String linkCause;  //哪个环节问题
    private String userFeedback;  //用户反馈
    private String technicianFeedback;  //美甲师反馈
    private String picturePath;  //图片地址
    private String existingProblems;  //存在问题
    private String solution;  //解决方案
    private String afterSaleFeedback;  //售后反馈

    private Integer isGuestPhoto; //是否有客照

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getOrderServeId() {
        return orderServeId;
    }

    public void setOrderServeId(Integer orderServeId) {
        this.orderServeId = orderServeId;
    }

    public String getFixHour() {
        return fixHour;
    }

    public void setFixHour(String fixHour) {
        this.fixHour = fixHour;
    }

    public Integer getIsTimeOut() {
        return isTimeOut;
    }

    public void setIsTimeOut(Integer isTimeOut) {
        this.isTimeOut = isTimeOut;
    }

    public String getLinkCause() {
        return linkCause;
    }

    public void setLinkCause(String linkCause) {
        this.linkCause = linkCause;
    }

    public String getUserFeedback() {
        return userFeedback;
    }

    public void setUserFeedback(String userFeedback) {
        this.userFeedback = userFeedback;
    }

    public String getTechnicianFeedback() {
        return technicianFeedback;
    }

    public void setTechnicianFeedback(String technicianFeedback) {
        this.technicianFeedback = technicianFeedback;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    public String getExistingProblems() {
        return existingProblems;
    }

    public void setExistingProblems(String existingProblems) {
        this.existingProblems = existingProblems;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public String getAfterSaleFeedback() {
        return afterSaleFeedback;
    }

    public void setAfterSaleFeedback(String afterSaleFeedback) {
        this.afterSaleFeedback = afterSaleFeedback;
    }

    public Integer getIsGuestPhoto() {
        return isGuestPhoto;
    }

    public void setIsGuestPhoto(Integer isGuestPhoto) {
        this.isGuestPhoto = isGuestPhoto;
    }

    @Override
    public String toString() {
        return "OrderRecord{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", orderServeId=" + orderServeId +
                ", fixHour='" + fixHour + '\'' +
                ", isTimeOut=" + isTimeOut +
                ", linkCause='" + linkCause + '\'' +
                ", userFeedback='" + userFeedback + '\'' +
                ", technicianFeedback='" + technicianFeedback + '\'' +
                ", picturePath='" + picturePath + '\'' +
                ", existingProblems='" + existingProblems + '\'' +
                ", solution='" + solution + '\'' +
                ", afterSaleFeedback='" + afterSaleFeedback + '\'' +
                ", isGuestPhoto=" + isGuestPhoto +
                '}';
    }
}
