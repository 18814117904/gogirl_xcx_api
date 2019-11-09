package com.gogirl.gogirl_xcx.entity;

import java.util.Date;

import com.gogirl.gogirl_technician.technician_commons.dto.UserTechnician;
import com.gogirl.gogirl_user.entity.Customer;

public class GogirlToken {
    private Integer id;
    private Integer sysId;
    private Integer customerId;

    private String token;
    private String code;
    private Date createTime;

    private Date updateTime;
    private String openid;
    private String sessionKey;
    private String unionid;
    private String phone;
    Customer customer;
    UserTechnician userTechnician;


	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public UserTechnician getUserTechnician() {
		return userTechnician;
	}

	public void setUserTechnician(UserTechnician userTechnician) {
		this.userTechnician = userTechnician;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getUnionid() {
		return unionid;
	}

	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}

	public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token == null ? null : token.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getSysId() {
		return sysId;
	}

	public void setSysId(Integer sysId) {
		this.sysId = sysId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getSessionKey() {
		return sessionKey;
	}

	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}

	@Override
	public String toString() {
		return "GogirlToken [id=" + id + ", sysId=" + sysId + ", customerId="
				+ customerId + ", token=" + token + ", code=" + code
				+ ", createTime=" + createTime + ", updateTime=" + updateTime
				+ ", openid=" + openid + "]";
	}
    
}