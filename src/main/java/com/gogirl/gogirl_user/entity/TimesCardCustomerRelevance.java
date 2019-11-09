package com.gogirl.gogirl_user.entity;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public class TimesCardCustomerRelevance {
    private Integer id;

    private Integer cardTypeId;

    private Integer customerId;

    private Date createTime;

    private Integer usedTimes;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date validStartTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date validEndTime;

    private String refereeId;
    private Integer status;
    
    private TimesCardType timesCardType;
    private List<TimesCardUsedRecord> timesCardUsedRecordList;
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getCardTypeId() {
        return cardTypeId;
    }

    public TimesCardType getTimesCardType() {
		return timesCardType;
	}

	public void setTimesCardType(TimesCardType timesCardType) {
		this.timesCardType = timesCardType;
	}

	public List<TimesCardUsedRecord> getTimesCardUsedRecordList() {
		return timesCardUsedRecordList;
	}

	public void setTimesCardUsedRecordList(
			List<TimesCardUsedRecord> timesCardUsedRecordList) {
		this.timesCardUsedRecordList = timesCardUsedRecordList;
	}

	public void setCardTypeId(Integer cardTypeId) {
        this.cardTypeId = cardTypeId;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getUsedTimes() {
        return usedTimes;
    }

    public void setUsedTimes(Integer usedTimes) {
        this.usedTimes = usedTimes;
    }

    public Date getValidStartTime() {
        return validStartTime;
    }

    public void setValidStartTime(Date validStartTime) {
        this.validStartTime = validStartTime;
    }

    public Date getValidEndTime() {
        return validEndTime;
    }

    public void setValidEndTime(Date validEndTime) {
        this.validEndTime = validEndTime;
    }

    public String getRefereeId() {
        return refereeId;
    }

    public void setRefereeId(String refereeId) {
        this.refereeId = refereeId == null ? null : refereeId.trim();
    }
}