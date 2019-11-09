package com.gogirl.gogirl_store.store_commons.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * Created by yinyong on 2019/1/9.
 */
public class ActivitySummary {

    private Integer id;
    private Integer activityId;  // 关联活动id
    @JsonFormat(pattern = "yyyy-MM-dd", timezone="GMT+8")
    private Date summaryTime;  // 汇总时间  年月日
    private Integer ejectTimes;  // 弹出次数
    private Integer participationTimes;  // 参与次数
    private Integer participantsNumber;  // 参与人数
    private Integer recipientNumber; // 领取人数

    private Integer customerId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public Date getSummaryTime() {
        return summaryTime;
    }

    public void setSummaryTime(Date summaryTime) {
        this.summaryTime = summaryTime;
    }

    public Integer getEjectTimes() {
        return ejectTimes;
    }

    public void setEjectTimes(Integer ejectTimes) {
        this.ejectTimes = ejectTimes;
    }

    public Integer getParticipationTimes() {
        return participationTimes;
    }

    public void setParticipationTimes(Integer participationTimes) {
        this.participationTimes = participationTimes;
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

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    @Override
    public String toString() {
        return "ActivitySummary{" +
                "id=" + id +
                ", activityId=" + activityId +
                ", summaryTime=" + summaryTime +
                ", ejectTimes=" + ejectTimes +
                ", participationTimes=" + participationTimes +
                ", participantsNumber=" + participantsNumber +
                ", recipientNumber=" + recipientNumber +
                ", customerId=" + customerId +
                '}';
    }
}
