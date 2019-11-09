package com.gogirl.gogirl_user.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gogirl.gogirl_service.entity.Serve;
import com.gogirl.gogirl_store.store_commons.dto.ShopManage;
import com.gogirl.gogirl_technician.technician_commons.dto.TechnicianManage;

public class TimesCardUsedRecord {
    private Integer id;

    private Integer cardTypeId;

    private Integer customerId;

    private Integer cardRelevanceCustomerId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date time;

    private Integer type;

    private Integer orderId;

    private Integer serveId;
    private Integer orderServeId;

    private Integer technicianId;

    private Integer departmentId;
    
    private String serveName;

    private String technicianName;

    private String departmentName;
    private Integer status;
    private TimesCardType timesCardType;
    private TimesCardCustomerRelevance timesCardCustomerRelevance;
    									
    public Integer getId() {
        return id;
    }
	public Integer getOrderServeId() {
		return orderServeId;
	}



	public void setOrderServeId(Integer orderServeId) {
		this.orderServeId = orderServeId;
	}



	public TimesCardCustomerRelevance getTimesCardCustomerRelevance() {
		return timesCardCustomerRelevance;
	}



	public void setTimesCardCustomerRelevance(
			TimesCardCustomerRelevance timesCardCustomerRelevance) {
		this.timesCardCustomerRelevance = timesCardCustomerRelevance;
	}



	public String getServeName() {
		return serveName;
	}



	public Integer getStatus() {
		return status;
	}



	public TimesCardType getTimesCardType() {
		return timesCardType;
	}



	public void setTimesCardType(TimesCardType timesCardType) {
		this.timesCardType = timesCardType;
	}



	public void setStatus(Integer status) {
		this.status = status;
	}



	public void setServeName(String serveName) {
		this.serveName = serveName;
	}



	public String getTechnicianName() {
		return technicianName;
	}



	public void setTechnicianName(String technicianName) {
		this.technicianName = technicianName;
	}



	public String getDepartmentName() {
		return departmentName;
	}



	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}



	public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCardTypeId() {
        return cardTypeId;
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

    public Integer getCardRelevanceCustomerId() {
        return cardRelevanceCustomerId;
    }

    public void setCardRelevanceCustomerId(Integer cardRelevanceCustomerId) {
        this.cardRelevanceCustomerId = cardRelevanceCustomerId;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getServeId() {
        return serveId;
    }

    public void setServeId(Integer serveId) {
        this.serveId = serveId;
    }

    public Integer getTechnicianId() {
        return technicianId;
    }

    public void setTechnicianId(Integer technicianId) {
        this.technicianId = technicianId;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }
}