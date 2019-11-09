package com.gogirl.gogirl_xcx.entity;

import java.util.Date;

public class VisitsLog {
    private Integer id;

    private Integer customerId;

    private Date time;

    private Integer duration;

    private Integer type;

    private Integer activityId;
    private Integer shareType;
    private Integer shareUserId;
    private Integer serviceId;
    private String pageName;

    
    
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getShareType() {
		return shareType;
	}

	public void setShareType(Integer shareType) {
		this.shareType = shareType;
	}

	public Integer getShareUserId() {
		return shareUserId;
	}

	public void setShareUserId(Integer shareUserId) {
		this.shareUserId = shareUserId;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}



	public String getPageName() {
		return pageName;
	}

	public void setPageName(String pageName) {
		this.pageName = pageName;
	}

	public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Integer getDuration() {
        return duration;
    }

    public Integer getActivityId() {
		return activityId;
	}

	public void setActivityId(Integer activityId) {
		this.activityId = activityId;
	}

	public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}