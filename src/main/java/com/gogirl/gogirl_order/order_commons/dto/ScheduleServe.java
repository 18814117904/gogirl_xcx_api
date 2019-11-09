package com.gogirl.gogirl_order.order_commons.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gogirl.gogirl_service.entity.Produce;
import com.gogirl.gogirl_service.entity.Serve;
import com.gogirl.gogirl_technician.technician_commons.dto.TechnicianManage;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yinyong on 2018/10/7.
 */
public class ScheduleServe implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
    private Integer schId;
    private Integer technicianId;
    private Integer serveId;
    private Integer mainServeId;
    private Boolean isCustomerPick;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date startTime;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date endTime;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;
    private String produceName;
    private Integer produceId;
    private Produce produce;
    private Integer serveNumber;
    private Integer lengthTimeForEndTime; //用于根据前端传过来开始时间计算结束时间

    private Serve serve;
    private TechnicianManage technicianManage;
    private Map<Integer ,TechnicianManage> mapTechnicianManage;
    private Integer recordType = 0; //记录类型 0#修改 1#增加 2#删除 3#原始数据  修改时传递数据是增加还是修改

    private String serveType;  // 服务分类
    private String serveName;  // 服务名称
    private Boolean needRemoveOldServe;  // 是否需要卸甲卸睫毛

    List<LinkedHashMap> listTechnician;

    private BigDecimal serveChangePrice;

    public Integer getId() {
        return id;
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

	public Boolean getIsCustomerPick() {
		return isCustomerPick;
	}



	public Map<Integer, TechnicianManage> getMapTechnicianManage() {
		return mapTechnicianManage;
	}

	public void setMapTechnicianManage(
			Map<Integer, TechnicianManage> mapTechnicianManage) {
		this.mapTechnicianManage = mapTechnicianManage;
	}

	public void setIsCustomerPick(Boolean isCustomerPick) {
		this.isCustomerPick = isCustomerPick;
	}

	public Serve getServe() {
		return serve;
	}

	public void setServe(Serve serve) {
		this.serve = serve;
	}

	public Integer getMainServeId() {
		return mainServeId;
	}

	public void setMainServeId(Integer mainServeId) {
		this.mainServeId = mainServeId;
	}

	public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getNeedRemoveOldServe() {
		return needRemoveOldServe;
	}

	public void setNeedRemoveOldServe(Boolean needRemoveOldServe) {
		this.needRemoveOldServe = needRemoveOldServe;
	}

	public Integer getSchId() {
        return schId;
    }

    public void setSchId(Integer schId) {
        this.schId = schId;
    }

    public Integer getTechnicianId() {
        return technicianId;
    }

    public void setTechnicianId(Integer technicianId) {
        this.technicianId = technicianId;
    }

    public Integer getServeId() {
        return serveId;
    }

    public void setServeId(Integer serveId) {
        this.serveId = serveId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getProduceName() {
        return produceName;
    }

    public void setProduceName(String produceName) {
        this.produceName = produceName;
    }

    public Integer getServeNumber() {
        return serveNumber;
    }

    public void setServeNumber(Integer serveNumber) {
        this.serveNumber = serveNumber;
    }



	public TechnicianManage getTechnicianManage() {
        return technicianManage;
    }

    public void setTechnicianManage(TechnicianManage technicianManage) {
        this.technicianManage = technicianManage;
    }

    public Integer getRecordType() {
        return recordType;
    }

    public void setRecordType(Integer recordType) {
        this.recordType = recordType;
    }

    public Integer getLengthTimeForEndTime() {
        return lengthTimeForEndTime;
    }

    public void setLengthTimeForEndTime(Integer lengthTimeForEndTime) {
        this.lengthTimeForEndTime = lengthTimeForEndTime;
    }

    public List<LinkedHashMap> getListTechnician() {
        return listTechnician;
    }

    public void setListTechnician(List<LinkedHashMap> listTechnician) {
        this.listTechnician = listTechnician;
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

    public BigDecimal getServeChangePrice() {
        return serveChangePrice;
    }

    public void setServeChangePrice(BigDecimal serveChangePrice) {
        this.serveChangePrice = serveChangePrice;
    }

    @Override
    public String toString() {
        return "ScheduleServe{" +
                "id=" + id +
                ", schId=" + schId +
                ", technicianId=" + technicianId +
                ", serveId=" + serveId +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", createTime=" + createTime +
                ", produceName='" + produceName + '\'' +
                ", serveNumber=" + serveNumber +
                ", lengthTimeForEndTime=" + lengthTimeForEndTime +
//                ", serve=" + serve +
                ", technicianManage=" + technicianManage +
                ", recordType=" + recordType +
                ", serveType='" + serveType + '\'' +
                ", serveName='" + serveName + '\'' +
                ", listTechnician=" + listTechnician +
                ", serveChangePrice=" + serveChangePrice +
                '}';
    }
    @Override
    public ScheduleServe clone(){
    	ScheduleServe newItem = new ScheduleServe();
    	newItem.setId(this.id);
    	newItem.setServe(this.serve);
    	newItem.setServeId(this.serveId);
    	newItem.setIsCustomerPick(this.isCustomerPick);
    	newItem.setTechnicianManage(this.technicianManage);
    	newItem.setTechnicianId(this.technicianId);
    	newItem.setStartTime(this.startTime);
    	newItem.setEndTime(this.endTime);
    	newItem.setMainServeId(this.mainServeId);
    	newItem.setLengthTimeForEndTime(this.lengthTimeForEndTime);
    	return newItem;
    }
}
