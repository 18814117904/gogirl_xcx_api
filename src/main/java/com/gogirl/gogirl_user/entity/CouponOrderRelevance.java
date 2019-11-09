package com.gogirl.gogirl_user.entity;

import java.math.BigDecimal;
import java.util.Date;

public class CouponOrderRelevance {
    private Integer id;

    private Integer couponId;

    private Integer customerId;

    private Integer couponCustomerRelevanceId;

    private Integer status;

    private Date createTime;

    private Date confirmTime;

    private Integer technicainId;
    private Integer orderId;
    private Integer orderServeId;
    private Boolean canBeUse;
    private String message;
    private BigDecimal discountAmount;
    private Coupon coupon;
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


	public Coupon getCoupon() {
		return coupon;
	}

	public Integer getOrderServeId() {
		return orderServeId;
	}

	public void setOrderServeId(Integer orderServeId) {
		this.orderServeId = orderServeId;
	}

	public void setCoupon(Coupon coupon) {
		this.coupon = coupon;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public BigDecimal getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(BigDecimal discountAmount) {
		this.discountAmount = discountAmount;
	}

	public Boolean getCanBeUse() {
		return canBeUse;
	}

	public void setCanBeUse(Boolean canBeUse) {
		this.canBeUse = canBeUse;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Integer getCouponId() {
        return couponId;
    }

    public void setCouponId(Integer couponId) {
        this.couponId = couponId;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getCouponCustomerRelevanceId() {
        return couponCustomerRelevanceId;
    }

    public void setCouponCustomerRelevanceId(Integer couponCustomerRelevanceId) {
        this.couponCustomerRelevanceId = couponCustomerRelevanceId;
    }



    public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getConfirmTime() {
        return confirmTime;
    }

    public void setConfirmTime(Date confirmTime) {
        this.confirmTime = confirmTime;
    }

    public Integer getTechnicainId() {
        return technicainId;
    }

    public void setTechnicainId(Integer technicainId) {
        this.technicainId = technicainId;
    }
}