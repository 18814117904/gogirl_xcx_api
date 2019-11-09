package com.gogirl.gogirl_technician.technician_commons.dto;

import com.gogirl.gogirl_technician.technician_user.controller.GroupA;

import javax.validation.constraints.NotNull;

import java.io.Serializable;

/**
 * Created by yinyong on 2019/4/11.
 */
public class UserManage implements Serializable {

    private Integer id;
    @NotNull(message = "姓名不能为空", groups = {GroupA.class})
    private String name;
    @NotNull(message = "性别不能为空")
    private String sex;

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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    @Override
    public String toString() {
        return "UserManage{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                '}';
    }
}
