package com.gogirl.gogirl_service.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by yinyong on 2018/8/20.
 */
public class Serve implements Serializable {

    private Integer id;
    private String name;
    private String picturePath;
    private String type;
    private List<Label> listLabel;
    private String label;
    private BigDecimal price;
    private Integer shopSort;
    private Integer status;
    private String produces;
    private Produce produce;
    private String remark;
    private String details;
    private Integer serviceDuration;
    private Integer hasIndexShow;
    private Integer indexSort;
    private Integer cusId;//用户查询列表时，查用户是否点赞
    private Integer praiseSum;
    private String briefIntroduction;
    private List<Produce> listProduce;
    private PraiseRecord praiseRecord;
    private Integer typeId;
    private Integer	schTypeId;
    public Integer getHasIndexShow() {
		return hasIndexShow;
	}

	public void setHasIndexShow(Integer hasIndexShow) {
		this.hasIndexShow = hasIndexShow;
	}

	public Integer getTypeId() {
		return typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

	public Produce getProduce() {
		return produce;
	}

	public void setProduce(Produce produce) {
		this.produce = produce;
	}

	public List<Label> getListLabel() {
		return listLabel;
	}

	public void setListLabel(List<Label> listLabel) {
		this.listLabel = listLabel;
	}

	public PraiseRecord getPraiseRecord() {
		return praiseRecord;
	}

	public String getBriefIntroduction() {
		return briefIntroduction;
	}

	public void setBriefIntroduction(String briefIntroduction) {
		this.briefIntroduction = briefIntroduction;
	}

	public Integer getSchTypeId() {
		return schTypeId;
	}

	public void setSchTypeId(Integer schTypeId) {
		this.schTypeId = schTypeId;
	}

	public Integer getCusId() {
		return cusId;
	}

	public void setCusId(Integer cusId) {
		this.cusId = cusId;
	}

	public void setPraiseRecord(PraiseRecord praiseRecord) {
		this.praiseRecord = praiseRecord;
	}

	public Integer getIndexSort() {
		return indexSort;
	}

	public void setIndexSort(Integer indexSort) {
		this.indexSort = indexSort;
	}

	public Integer getPraiseSum() {
		return praiseSum;
	}

	public void setPraiseSum(Integer praiseSum) {
		this.praiseSum = praiseSum;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getShopSort() {
        return shopSort;
    }

    public void setShopSort(Integer shopSort) {
        this.shopSort = shopSort;
    }

//    public String getStatus() {
//        return status;
//    }
//
//    public void setStatus(String status) {
//        this.status = status;
//    }

    public String getRemark() {
        return remark;
    }

    public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getProduces() {
        return produces;
    }

    public void setProduces(String produces) {
        this.produces = produces;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Integer getServiceDuration() {
        return serviceDuration;
    }

    public void setServiceDuration(Integer serviceDuration) {
        this.serviceDuration = serviceDuration;
    }

    public List<Produce> getListProduce() {
        return listProduce;
    }

    public void setListProduce(List<Produce> listProduce) {
        this.listProduce = listProduce;
    }

    @Override
    public String toString() {
        return "Serve{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", picturePath='" + picturePath + '\'' +
                ", type='" + type + '\'' +
                ", label='" + label + '\'' +
                ", price=" + price +
                ", shopSort=" + shopSort +
                ", status='" + status + '\'' +
                ", produces='" + produces + '\'' +
                ", remark='" + remark + '\'' +
                ", details='" + details + '\'' +
                ", serviceDuration=" + serviceDuration +
                ", listProduce=" + listProduce +
                '}';
    }
}
