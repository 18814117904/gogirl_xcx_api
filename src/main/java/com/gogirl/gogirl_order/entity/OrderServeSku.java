package com.gogirl.gogirl_order.entity;

import java.math.BigDecimal;

public class OrderServeSku {
    private Integer id;

    private Integer orderServeId;

    private Integer skuId;

    private Integer type;

    private BigDecimal count;

    private PurchaseSku purchaseSku;
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrderServeId() {
        return orderServeId;
    }

    public PurchaseSku getPurchaseSku() {
		return purchaseSku;
	}

	public void setPurchaseSku(PurchaseSku purchaseSku) {
		this.purchaseSku = purchaseSku;
	}

	public void setOrderServeId(Integer orderServeId) {
        this.orderServeId = orderServeId;
    }

    public Integer getSkuId() {
        return skuId;
    }

    public void setSkuId(Integer skuId) {
        this.skuId = skuId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public BigDecimal getCount() {
        return count;
    }

    public void setCount(BigDecimal count) {
        this.count = count;
    }
}