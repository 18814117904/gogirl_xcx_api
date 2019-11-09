package com.gogirl.gogirl_order.order_commons.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gogirl.gogirl_user.entity.Customer;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by yinyong on 2018/9/28.
 */

/**
 * 预约记录封装实体类
 */
public class ScheduleRecordManage implements Serializable {

    private Integer id;
    private String scheduledNo;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date scheduledTime; //下单时间
    private Integer scheduledUser; //下单人
    private String telephone;  //用户电话号码
    private String storeScheduleUsername;  //预约用户名
    private String arriveTime; //预约到达时间
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date lastServiceTime; //上次服务时间
    private String arriveUser; //到店人
    private Integer departmentId; //下单门店
    private String remark; //备注
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date openbillTime; //开单时间
    private Integer status; //已预约#1  失约#2  守约#3  已取消#4

    private List<ScheduleUpdateRecord> listScheduleUpdateRecord; //预约下服务列表
    private Customer customer; //预约用户信息
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date lastUpdateTime;  //最新修改时间

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getScheduledNo() {
        return scheduledNo;
    }

    public void setScheduledNo(String scheduledNo) {
        this.scheduledNo = scheduledNo;
    }

    public Date getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(Date scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public Integer getScheduledUser() {
        return scheduledUser;
    }

    public void setScheduledUser(Integer scheduledUser) {
        this.scheduledUser = scheduledUser;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getStoreScheduleUsername() {
        return storeScheduleUsername;
    }

    public void setStoreScheduleUsername(String storeScheduleUsername) {
        this.storeScheduleUsername = storeScheduleUsername;
    }

    public String getArriveTime() {
        return arriveTime;
    }

    public void setArriveTime(String arriveTime) {
        this.arriveTime = arriveTime;
    }

    public Date getLastServiceTime() {
        return lastServiceTime;
    }

    public void setLastServiceTime(Date lastServiceTime) {
        this.lastServiceTime = lastServiceTime;
    }

    public String getArriveUser() {
        return arriveUser;
    }

    public void setArriveUser(String arriveUser) {
        this.arriveUser = arriveUser;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getOpenbillTime() {
        return openbillTime;
    }

    public void setOpenbillTime(Date openbillTime) {
        this.openbillTime = openbillTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<ScheduleUpdateRecord> getListScheduleUpdateRecord() {
        return listScheduleUpdateRecord;
    }

    public void setListScheduleUpdateRecord(List<ScheduleUpdateRecord> listScheduleUpdateRecord) {
        this.listScheduleUpdateRecord = listScheduleUpdateRecord;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    @Override
    public String toString() {
        return "ScheduleRecordManage{" +
                "id=" + id +
                ", scheduledNo='" + scheduledNo + '\'' +
                ", scheduledTime=" + scheduledTime +
                ", scheduledUser=" + scheduledUser +
                ", telephone='" + telephone + '\'' +
                ", storeScheduleUsername='" + storeScheduleUsername + '\'' +
                ", arriveTime='" + arriveTime + '\'' +
                ", lastServiceTime=" + lastServiceTime +
                ", arriveUser='" + arriveUser + '\'' +
                ", departmentId=" + departmentId +
                ", remark='" + remark + '\'' +
                ", openbillTime=" + openbillTime +
                ", status=" + status +
                ", listScheduleUpdateRecord=" + listScheduleUpdateRecord +
                ", customer=" + customer +
                ", lastUpdateTime=" + lastUpdateTime +
                '}';
    }
}
