package com.gogirl.gogirl_order.order_commons.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gogirl.gogirl_service.entity.Serve;
import com.gogirl.gogirl_technician.technician_commons.dto.TechnicianManage;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by yinyong on 2018/10/7.
 */
public class ScheduleUpdateRecord implements Serializable {

    private Integer id;
    private Integer schId;
    private Integer technicianId;
    private Integer serveId;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date startTime;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date endTime;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;
    private String produceName;
    private Integer serveNumber;

    private Serve serve;
    private TechnicianManage technicianManage;

    private Integer type;

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

    public Serve getServe() {
        return serve;
    }

    public void setServe(Serve serve) {
        this.serve = serve;
    }

    public TechnicianManage getTechnicianManage() {
        return technicianManage;
    }

    public void setTechnicianManage(TechnicianManage technicianManage) {
        this.technicianManage = technicianManage;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ScheduleUpdateRecord{" +
                "id=" + id +
                ", schId=" + schId +
                ", technicianId=" + technicianId +
                ", serveId=" + serveId +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", createTime=" + createTime +
                ", produceName='" + produceName + '\'' +
                ", serveNumber=" + serveNumber +
                ", serve=" + serve +
                ", technicianManage=" + technicianManage +
                ", type=" + type +
                '}';
    }
}
