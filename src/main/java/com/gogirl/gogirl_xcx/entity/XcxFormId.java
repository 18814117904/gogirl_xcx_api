package com.gogirl.gogirl_xcx.entity;

import java.util.Date;

public class XcxFormId {
    private Integer id;

    private Integer customerId;

    private String openid;

    private String formId;

    private Date time;
    private Integer type;
    
    public XcxFormId() {
		super();
	}
    public XcxFormId(Integer customerId,String openid,String formId,Date time,Integer type) {
		super();
		this.customerId = customerId;
		this.openid = openid;
		this.formId = formId;
		this.time = time;
		this.type = type;
	}

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



    public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId == null ? null : formId.trim();
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}