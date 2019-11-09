package com.gogirl.gogirl_order.entity;

public class BaseProduceSku {
    private Integer id;

    private Integer purchaseSkuId;

    private Integer produceId;

    private Integer type;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPurchaseSkuId() {
        return purchaseSkuId;
    }

    public void setPurchaseSkuId(Integer purchaseSkuId) {
        this.purchaseSkuId = purchaseSkuId;
    }

    public Integer getProduceId() {
        return produceId;
    }

    public void setProduceId(Integer produceId) {
        this.produceId = produceId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}