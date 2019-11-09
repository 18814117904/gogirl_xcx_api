package com.gogirl.gogirl_service.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by yinyong on 2018/9/11.
 */
public class Broadcast implements Serializable {

    private Integer id;
    private String title;
    private String url;
    private String picturePath;
    private String remark;
    private Date topTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getTopTime() {
        return topTime;
    }

    public void setTopTime(Date topTime) {
        this.topTime = topTime;
    }

    @Override
    public String toString() {
        return "Broadcast{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", picturePath='" + picturePath + '\'' +
                ", remark='" + remark + '\'' +
                ", topTime=" + topTime +
                '}';
    }
}
