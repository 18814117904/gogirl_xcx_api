package com.gogirl.gogirl_service.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by yinyong on 2018/8/20.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserGetByBLUserAcct implements Serializable {

    private static final Long serialVersionUID = 1l;

    private String departmentname1;
    private String username;
    private String jobs;
    private String departmentname2;
    private String departmentpath;
    private String usercode;
    private String useraccount;
    private String sex;
    private String rztype;
    private Integer userid;
    private String state;

    public String getDepartmentname1() {
        return departmentname1;
    }

    public void setDepartmentname1(String departmentname1) {
        this.departmentname1 = departmentname1;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getJobs() {
        return jobs;
    }

    public void setJobs(String jobs) {
        this.jobs = jobs;
    }

    public String getDepartmentname2() {
        return departmentname2;
    }

    public void setDepartmentname2(String departmentname2) {
        this.departmentname2 = departmentname2;
    }

    public String getDepartmentpath() {
        return departmentpath;
    }

    public void setDepartmentpath(String departmentpath) {
        this.departmentpath = departmentpath;
    }

    public String getUsercode() {
        return usercode;
    }

    public void setUsercode(String usercode) {
        this.usercode = usercode;
    }

    public String getUseraccount() {
        return useraccount;
    }

    public void setUseraccount(String useraccount) {
        this.useraccount = useraccount;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getRztype() {
        return rztype;
    }

    public void setRztype(String rztype) {
        this.rztype = rztype;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "UserGetByBLUserAcct{" +
                "departmentname1='" + departmentname1 + '\'' +
                ", username='" + username + '\'' +
                ", jobs='" + jobs + '\'' +
                ", departmentname2='" + departmentname2 + '\'' +
                ", departmentpath='" + departmentpath + '\'' +
                ", usercode='" + usercode + '\'' +
                ", useraccount='" + useraccount + '\'' +
                ", sex='" + sex + '\'' +
                ", rztype='" + rztype + '\'' +
                ", userid=" + userid +
                ", state='" + state + '\'' +
                '}';
    }
}
