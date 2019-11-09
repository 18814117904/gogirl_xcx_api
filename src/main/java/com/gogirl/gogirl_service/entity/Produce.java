package com.gogirl.gogirl_service.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.gogirl.gogirl_order.entity.BaseProduceSku;

/**
 * Created by yinyong on 2018/8/20.
 */
public class Produce implements Serializable {

    private Integer id;
    private String name;
    private String picturePath;
    private String type;
    private String label;
    private Integer shopSort;
    private String reqGrade;
    private String remark;
    private String status;
    private String details;

    private Integer serveId;
    private Serve serve;
    private String serveName;
    private BigDecimal price;
    private Integer serviceDuration;
    private Integer sortOrder;
    private Integer praiseSum;
    private BigDecimal currentPrice;
    private BigDecimal bargainPrice;
    private String briefIntroduction;
    private String subTitle;
    private PraiseRecord praiseRecord;
    private List<BaseProduceSku> listProduceSku;

    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public PraiseRecord getPraiseRecord() {
		return praiseRecord;
	}

	public List<BaseProduceSku> getListProduceSku() {
		return listProduceSku;
	}

	public void setListProduceSku(List<BaseProduceSku> listProduceSku) {
		this.listProduceSku = listProduceSku;
	}

	public void setPraiseRecord(PraiseRecord praiseRecord) {
		this.praiseRecord = praiseRecord;
	}

	public Serve getServe() {
		return serve;
	}

	public BigDecimal getCurrentPrice() {
		return currentPrice;
	}

	public void setCurrentPrice(BigDecimal currentPrice) {
		this.currentPrice = currentPrice;
	}

	public BigDecimal getBargainPrice() {
		return bargainPrice;
	}

	public void setBargainPrice(BigDecimal bargainPrice) {
		this.bargainPrice = bargainPrice;
	}

	public String getBriefIntroduction() {
		return briefIntroduction;
	}

	public void setBriefIntroduction(String briefIntroduction) {
		this.briefIntroduction = briefIntroduction;
	}

	public String getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

	public Integer getPraiseSum() {
		return praiseSum;
	}

	public void setPraiseSum(Integer praiseSum) {
		this.praiseSum = praiseSum;
	}

	public void setServe(Serve serve) {
		this.serve = serve;
	}

	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Integer getShopSort() {
        return shopSort;
    }

    public void setShopSort(Integer shopSort) {
        this.shopSort = shopSort;
    }

    public Integer getServeId() {
        return serveId;
    }

    public void setServeId(Integer serveId) {
        this.serveId = serveId;
    }

    public String getReqGrade() {
        return reqGrade;
    }

    public String getServeName() {
        return serveName;
    }

    public void setServeName(String serveName) {
        this.serveName = serveName;
    }

    public void setReqGrade(String reqGrade) {
        this.reqGrade = reqGrade;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getServiceDuration() {
        return serviceDuration;
    }

    public void setServiceDuration(Integer serviceDuration) {
        this.serviceDuration = serviceDuration;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    @Override
    public String toString() {
        return "Produce{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", picturePath='" + picturePath + '\'' +
                ", type='" + type + '\'' +
                ", label='" + label + '\'' +
                ", shopSort=" + shopSort +
                ", reqGrade='" + reqGrade + '\'' +
                ", remark='" + remark + '\'' +
                ", status='" + status + '\'' +
                ", details='" + details + '\'' +
                ", serveId=" + serveId +
                ", serveName='" + serveName + '\'' +
                ", price=" + price +
                ", serviceDuration=" + serviceDuration +
                ", sortOrder=" + sortOrder +
                '}';
    }
}
