package com.gogirl.gogirl_store.store_commons.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * Created by yinyong on 2019/1/9.
 */
public class ActivityCustomer {

    private Integer id;
    private Integer customerId;  //关联用户id
    private Integer activityId;  //活动id
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date participationTime;  // 参与时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date collectTime; // 领取时间
    private String status;  //活动状态  判断用户参与活动到第几步了

    private Customer customer;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public Date getParticipationTime() {
        return participationTime;
    }

    public void setParticipationTime(Date participationTime) {
        this.participationTime = participationTime;
    }

    public Date getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(Date collectTime) {
        this.collectTime = collectTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public String toString() {
        return "ActivityCustomer{" +
                "id=" + id +
                ", customerId=" + customerId +
                ", activityId=" + activityId +
                ", participationTime=" + participationTime +
                ", collectTime=" + collectTime +
                ", status='" + status + '\'' +
                ", customer=" + customer +
                '}';
    }
}
