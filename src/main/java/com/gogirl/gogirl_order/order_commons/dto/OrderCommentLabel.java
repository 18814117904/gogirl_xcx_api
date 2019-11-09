package com.gogirl.gogirl_order.order_commons.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by yinyong on 2018/10/22.
 */
public class OrderCommentLabel implements Serializable {

    private Integer id;
    private String name;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date updateTime;
    private String ipdateUser;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getIpdateUser() {
        return ipdateUser;
    }

    public void setIpdateUser(String ipdateUser) {
        this.ipdateUser = ipdateUser;
    }

    @Override
    public String toString() {
        return "OrderCommentLabel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", ipdateUser='" + ipdateUser + '\'' +
                '}';
    }
}
