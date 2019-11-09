package com.gogirl.gogirl_service.entity;

import java.io.Serializable;

/**
 * Created by yinyong on 2018/8/20.
 */
public class GetByBLUserAcct implements Serializable {

    private static final Long serialVersionUID = 1l;
    private UserGetByBLUserAcct data;
    private Integer code;
    private String message;

    public UserGetByBLUserAcct getData() {
        return data;
    }

    public void setData(UserGetByBLUserAcct data) {
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
