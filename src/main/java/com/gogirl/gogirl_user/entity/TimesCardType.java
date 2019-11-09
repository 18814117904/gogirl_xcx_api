package com.gogirl.gogirl_user.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.gogirl.gogirl_store.store_commons.dto.ShopManage;

public class TimesCardType {
    private Integer id;

    private String name;

    private Integer sumTimes;

    private BigDecimal discountAmount;

    private BigDecimal payAmount;

    private Integer sumCount;

    private Integer validDays;

    private String notes;

    private Date createTime;

    private Integer status;

    private List<TimesCardTypeContent> cardTypeCententList;
    private List<ShopManage> storeList;
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public List<ShopManage> getStoreList() {
		return storeList;
	}

	public void setStoreList(List<ShopManage> storeList) {
		this.storeList = storeList;
	}

	public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public List<TimesCardTypeContent> getCardTypeCententList() {
		return cardTypeCententList;
	}

	public void setCardTypeCententList(
			List<TimesCardTypeContent> cardTypeCententList) {
		this.cardTypeCententList = cardTypeCententList;
	}

	public Integer getSumTimes() {
        return sumTimes;
    }

    public void setSumTimes(Integer sumTimes) {
        this.sumTimes = sumTimes;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }



    public BigDecimal getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(BigDecimal payAmount) {
		this.payAmount = payAmount;
	}

	public Integer getSumCount() {
        return sumCount;
    }

    public void setSumCount(Integer sumCount) {
        this.sumCount = sumCount;
    }

    public Integer getValidDays() {
        return validDays;
    }

    public void setValidDays(Integer validDays) {
        this.validDays = validDays;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes == null ? null : notes.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}