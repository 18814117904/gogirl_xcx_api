package com.gogirl.gogirl_user.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gogirl.gogirl_store.store_commons.dto.UserManage;


public class CustomerBalanceRecord {
    private Integer id;

    private Integer customerId;

    private Integer source;

    private Integer type;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date time;

    private Integer currentBalance;

    private Integer orderAmount;
    private Integer newOrderId;

    private Double discount;

    private Integer bestowAmount;

    private String orderId;

    private Integer orderState;

    private String ip;
    private Integer departmentId;
    private String departmentName;
    
    private String remark;
    private String refereeId;
    private UserManage referee;
    public CustomerBalanceRecord() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CustomerBalanceRecord(Integer customerId, Integer source,
			Integer type, Date time, Integer currentBalance,
			Integer orderAmount, String orderId, Integer orderState,Double discount,Integer bestowAmount,String remark) {
		super();
		this.customerId = customerId;
		this.source = source;
		this.type = type;
		this.time = time;
		this.currentBalance = currentBalance;
		this.orderAmount = orderAmount;
		this.orderId = orderId;
		this.orderState = orderState;
		this.discount = discount;
		this.bestowAmount = bestowAmount;
		this.remark = remark;
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

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public Integer getType() {
        return type;
    }

    public Integer getNewOrderId() {
		return newOrderId;
	}

	public void setNewOrderId(Integer newOrderId) {
		this.newOrderId = newOrderId;
	}

	public void setType(Integer type) {
        this.type = type;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Integer getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(Integer currentBalance) {
        this.currentBalance = currentBalance;
    }

    public Integer getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(Integer orderAmount) {
        this.orderAmount = orderAmount;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Integer getBestowAmount() {
        return bestowAmount;
    }

    public void setBestowAmount(Integer bestowAmount) {
        this.bestowAmount = bestowAmount;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId == null ? null : orderId.trim();
    }

    public Integer getOrderState() {
        return orderState;
    }

    public void setOrderState(Integer orderState) {
        this.orderState = orderState;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip == null ? null : ip.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }



	public UserManage getReferee() {
		return referee;
	}

	public void setReferee(UserManage referee) {
		this.referee = referee;
	}

	public String getRefereeId() {
		return refereeId;
	}

	public void setRefereeId(String refereeId) {
		this.refereeId = refereeId;
	}

	public Integer getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}





    
}