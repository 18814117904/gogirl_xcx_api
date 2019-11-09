package com.gogirl.gogirl_store.store_commons.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gogirl.gogirl_technician.technician_commons.dto.TechnicianManage;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by yinyong on 2018/10/7.
 */
public class ScheduleServe implements Serializable {

    private Integer id;
    private Integer schId;
    private Integer technicianId;
    private Integer serveId;
    @JsonFormat(pattern="HH:mm:ss",timezone="GMT+8")
    private Date startTime;
    @JsonFormat(pattern="HH:mm:ss",timezone="GMT+8")
    private Date endTime;
    @JsonFormat(pattern="HH:mm:ss",timezone="GMT+8")
    private Date createTime;
    private String produceName;
    private Integer serveNumber;
    private Integer lengthTimeForEndTime; //用于根据前端传过来开始时间计算结束时间

    private TechnicianManage technicianManage;
    private String serveName;
    private String serveType;
    private Integer recordType = 0; //记录类型 0#修改 1#增加  修改时传递数据是增加还是修改

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSchId() {
        return schId;
    }

    public void setSchId(Integer schId) {
        this.schId = schId;
    }

    public Integer getTechnicianId() {
        return technicianId;
    }

    public void setTechnicianId(Integer technicianId) {
        this.technicianId = technicianId;
    }

    public Integer getServeId() {
        return serveId;
    }

    public void setServeId(Integer serveId) {
        this.serveId = serveId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getProduceName() {
        return produceName;
    }

    public void setProduceName(String produceName) {
        this.produceName = produceName;
    }

    public Integer getServeNumber() {
        return serveNumber;
    }

    public void setServeNumber(Integer serveNumber) {
        this.serveNumber = serveNumber;
    }

    public TechnicianManage getTechnicianManage() {
        return technicianManage;
    }

    public void setTechnicianManage(TechnicianManage technicianManage) {
        this.technicianManage = technicianManage;
    }

    public String getServeName() {
        return serveName;
    }

    public void setServeName(String serveName) {
        this.serveName = serveName;
    }

    public String getServeType() {
        return serveType;
    }

    public void setServeType(String serveType) {
        this.serveType = serveType;
    }

    public Integer getRecordType() {
        return recordType;
    }

    public void setRecordType(Integer recordType) {
        this.recordType = recordType;
    }

    public Integer getLengthTimeForEndTime() {
        return lengthTimeForEndTime;
    }

    public void setLengthTimeForEndTime(Integer lengthTimeForEndTime) {
        this.lengthTimeForEndTime = lengthTimeForEndTime;
    }

    @Override
    public String toString() {
        return "ScheduleServe{" +
                "id=" + id +
                ", schId=" + schId +
                ", technicianId=" + technicianId +
                ", serveId=" + serveId +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", createTime=" + createTime +
                ", produceName='" + produceName + '\'' +
                ", serveNumber=" + serveNumber +
                ", lengthTimeForEndTime=" + lengthTimeForEndTime +
                ", technicianManage=" + technicianManage +
                ", serveName='" + serveName + '\'' +
                ", serveType='" + serveType + '\'' +
                ", recordType=" + recordType +
                '}';
    }
}
