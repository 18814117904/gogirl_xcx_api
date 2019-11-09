package com.gogirl.gogirl_order.order_commons.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gogirl.gogirl_order.entity.OrderServeDescribe;
import com.gogirl.gogirl_order.entity.OrderServeSku;
import com.gogirl.gogirl_service.entity.Produce;
import com.gogirl.gogirl_service.entity.Serve;
import com.gogirl.gogirl_technician.technician_commons.dto.TechnicianManage;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by yinyong on 2018/9/21.
 */
public class OrderServe implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
    private Integer orderId;
    private String technicianId;
    private Integer serveId;
    private Integer produceId;
    private String produceName;
    private BigDecimal servePrice;
    private BigDecimal serveChangePrice;
    private Integer serveNumber;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date scheduledTime;
    @JsonFormat(pattern = "HH:mm", timezone = "GMT+8")
    private Date startTime;
    @JsonFormat (pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date startDate;
    @JsonFormat(pattern = "HH:mm", timezone = "GMT+8")
    private Date endTime;
    private Integer executionStatus;
    private Integer updateType = 0;
    private Boolean canReplace = false;
    private Integer commentScore;
    
    private String serveType;  // 服务分类
    private String serveName;  // 服务名称
    private Integer removeCouponId;
    private BigDecimal achievement;
    private OrderRecord orderRecord; //订单记录
    private Integer	isCountAchievement=0;
    
    private Serve serve;
    private Produce produce;

    private TechnicianManage technicianManage;

    private List<TechnicianManage> listTechnicianManage;
    private List<OrderServeSku> listOrderServeSku;
    private List<OrderServeDescribe> listOrderServeDescribe;
    
    public Integer getId() {
        return id;
    }

    public Boolean getCanReplace() {
		return canReplace;
	}

	public Integer getIsCountAchievement() {
		return isCountAchievement;
	}

	public List<OrderServeDescribe> getListOrderServeDescribe() {
		return listOrderServeDescribe;
	}

	public void setListOrderServeDescribe(
			List<OrderServeDescribe> listOrderServeDescribe) {
		this.listOrderServeDescribe = listOrderServeDescribe;
	}

	public void setIsCountAchievement(Integer isCountAchievement) {
		this.isCountAchievement = isCountAchievement;
	}

	public List<OrderServeSku> getListOrderServeSku() {
		return listOrderServeSku;
	}

	public void setListOrderServeSku(List<OrderServeSku> listOrderServeSku) {
		this.listOrderServeSku = listOrderServeSku;
	}

	public Integer getProduceId() {
		return produceId;
	}

	public void setProduceId(Integer produceId) {
		this.produceId = produceId;
	}

	public Produce getProduce() {
		return produce;
	}

	public void setProduce(Produce produce) {
		this.produce = produce;
	}

	public BigDecimal getAchievement() {
		return achievement;
	}

	public Integer getCommentScore() {
		return commentScore;
	}

	public void setCommentScore(Integer commentScore) {
		this.commentScore = commentScore;
	}

	public void setAchievement(BigDecimal achievement) {
		this.achievement = achievement;
	}

	public Integer getRemoveCouponId() {
		return removeCouponId;
	}

	public void setRemoveCouponId(Integer removeCouponId) {
		this.removeCouponId = removeCouponId;
	}

	public Date getScheduledTime() {
		return scheduledTime;
	}

	public void setScheduledTime(Date scheduledTime) {
		this.scheduledTime = scheduledTime;
	}

//	public Integer getTechnicianId() {
//		return technicianId;
//	}
//
//	public void setTechnicianId(Integer technicianId) {
//		this.technicianId = technicianId;
//	}

	public void setCanReplace(Boolean canReplace) {
		this.canReplace = canReplace;
	}




	public String getTechnicianId() {
		return technicianId;
	}

	public void setTechnicianId(String technicianId) {
		this.technicianId = technicianId;
	}

	public void setId(Integer id) {
        this.id = id;
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

    public String getProduceName() {
        return produceName;
    }

    public void setProduceName(String produceName) {
        this.produceName = produceName;
    }

    public BigDecimal getServePrice() {
        return servePrice;
    }

    public void setServePrice(BigDecimal servePrice) {
        this.servePrice = servePrice;
    }

    public BigDecimal getServeChangePrice() {
        return serveChangePrice;
    }

    public void setServeChangePrice(BigDecimal serveChangePrice) {
        this.serveChangePrice = serveChangePrice;
    }

    public Integer getServeNumber() {
        return serveNumber;
    }

    public void setServeNumber(Integer serveNumber) {
        this.serveNumber = serveNumber;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getServeType() {
        return serveType;
    }

    public void setServeType(String serveType) {
        this.serveType = serveType;
    }

    public String getServeName() {
        return serveName;
    }

    public void setServeName(String serveName) {
        this.serveName = serveName;
    }

    public Serve getServe() {
        return serve;
    }

    public void setServe(Serve serve) {
        this.serve = serve;
    }

    public TechnicianManage getTechnicianManage() {
        return technicianManage;
    }

    public void setTechnicianManage(TechnicianManage technicianManage) {
        this.technicianManage = technicianManage;
    }

    public List<TechnicianManage> getListTechnicianManage() {
        return listTechnicianManage;
    }

    public void setListTechnicianManage(List<TechnicianManage> listTechnicianManage) {
        this.listTechnicianManage = listTechnicianManage;
    }

    public Integer getUpdateType() {
        return updateType;
    }

    public void setUpdateType(Integer updateType) {
        this.updateType = updateType;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getExecutionStatus() {
        return executionStatus;
    }

    public void setExecutionStatus(Integer executionStatus) {
        this.executionStatus = executionStatus;
    }

 

    public OrderRecord getOrderRecord() {
		return orderRecord;
	}

	public void setOrderRecord(OrderRecord orderRecord) {
		this.orderRecord = orderRecord;
	}

	@Override
    public String toString() {
        return "OrderServe{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", technicianId='" + technicianId + '\'' +
                ", serveId=" + serveId +
                ", produceName='" + produceName + '\'' +
                ", servePrice=" + servePrice +
                ", serveChangePrice=" + serveChangePrice +
                ", serveNumber=" + serveNumber +
                ", createTime=" + createTime +
                ", startTime=" + startTime +
                ", startDate=" + startDate +
                ", endTime=" + endTime +
                ", executionStatus=" + executionStatus +
                ", updateType=" + updateType +
                ", serveType='" + serveType + '\'' +
                ", serveName='" + serveName + '\'' +
                ", serve=" + serve +
                ", technicianManage=" + technicianManage +
                ", listTechnicianManage=" + listTechnicianManage +
                '}';
    }
}
