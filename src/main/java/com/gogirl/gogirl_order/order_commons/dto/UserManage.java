package com.gogirl.gogirl_order.order_commons.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * Created by yinyong on 2018/9/17.
 */
public class UserManage {

    private Integer id;
    private String no;
    private String name;
    private String picturePath;
    private Integer departmentId;
    private String jobs;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;
    private String mobile;
    private ShopManage shopManage = new ShopManage();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public String getJobs() {
        return jobs;
    }

    public void setJobs(String jobs) {
        this.jobs = jobs;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public ShopManage getShopManage() {
        return shopManage;
    }

    public void setShopManage(ShopManage shopManage) {
        this.shopManage = shopManage;
    }

    @Override
    public String toString() {
        return "UserManage{" +
                "id=" + id +
                ", no='" + no + '\'' +
                ", name='" + name + '\'' +
                ", picturePath='" + picturePath + '\'' +
                ", departmentId=" + departmentId +
                ", jobs='" + jobs + '\'' +
                ", createTime=" + createTime +
                ", mobile='" + mobile + '\'' +
                ", shopManage=" + shopManage +
                '}';
    }
}
