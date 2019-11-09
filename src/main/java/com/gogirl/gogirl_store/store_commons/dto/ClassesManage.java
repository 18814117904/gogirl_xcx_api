package com.gogirl.gogirl_store.store_commons.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.sql.Time;

/**
 * Created by yinyong on 2018/9/20.
 */
public class ClassesManage implements Serializable {

    private Integer id;
    private String name;
    @JsonFormat(pattern = "HH:mm:ss", timezone="GMT+8")
    private Time startTime;
    @JsonFormat(pattern = "HH:mm:ss", timezone = "GMT+8")
    private Time endTime;
    private Integer useNumber;
    private Integer departmentId;

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

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }

    public Integer getUseNumber() {
        return useNumber;
    }

    public void setUseNumber(Integer useNumber) {
        this.useNumber = useNumber;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    @Override
    public String toString() {
        return "ClassesManage{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", useNumber=" + useNumber +
                ", departmentId=" + departmentId +
                '}';
    }
}
