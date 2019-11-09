package com.gogirl.gogirl_store.store_commons.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * Created by yinyong on 2019/1/8.
 */
public class ActivityManage {

    private Integer id;
    private String name;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date createTime;
    private String startTime;
    private String endTime;
    private String prize;  // 活动奖品
    private String conditionUse;  // 使用条件
    private Integer participantsNumber; //参与人数
    private Integer recipientNumber;  // 领取人数
    private String status;  // 活动状态   开启关闭

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

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getPrize() {
        return prize;
    }

    public void setPrize(String prize) {
        this.prize = prize;
    }

    public String getConditionUse() {
        return conditionUse;
    }

    public void setConditionUse(String conditionUse) {
        this.conditionUse = conditionUse;
    }

    public Integer getParticipantsNumber() {
        return participantsNumber;
    }

    public void setParticipantsNumber(Integer participantsNumber) {
        this.participantsNumber = participantsNumber;
    }

    public Integer getRecipientNumber() {
        return recipientNumber;
    }

    public void setRecipientNumber(Integer recipientNumber) {
        this.recipientNumber = recipientNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ActivityManage{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", createTime=" + createTime +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", prize='" + prize + '\'' +
                ", conditionUse='" + conditionUse + '\'' +
                ", participantsNumber=" + participantsNumber +
                ", recipientNumber=" + recipientNumber +
                ", status='" + status + '\'' +
                '}';
    }
}
