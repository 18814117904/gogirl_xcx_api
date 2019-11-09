package com.gogirl.gogirl_order.order_commons.dto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.gogirl.gogirl_technician.technician_commons.dto.TechnicianManage;
import com.gogirl.gogirl_user.entity.Customer;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class OrderComment implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
    private Integer orderId;
    private Integer orderServeId;
    private Integer technicianId;
    
    private Integer userId;
    private Integer level;
    private String remark;
    private String picturePath;
    private String labelSticker;
    private Date createTime;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date finishTime;
    private Integer status;
    private String departmentName;
    private String orderNo;
    private Customer customer;
    TechnicianManage technicianManage;
    OrderServe orderServe;
    private List<OrderCommentLabel> listOrderCommentLabel;

    public Integer getId() {
        return id;
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

    public OrderServe getOrderServe() {
		return orderServe;
	}

	public void setOrderServe(OrderServe orderServe) {
		this.orderServe = orderServe;
	}

	public TechnicianManage getTechnicianManage() {
		return technicianManage;
	}

	public void setTechnicianManage(TechnicianManage technicianManage) {
		this.technicianManage = technicianManage;
	}

	public Integer getOrderServeId() {
        return orderServeId;
    }

    public void setOrderServeId(Integer orderServeId) {
        this.orderServeId = orderServeId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getTechnicianId() {
		return technicianId;
	}

	public void setTechnicianId(Integer technicianId) {
		this.technicianId = technicianId;
	}

	public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    public String getLabelSticker() {
        return labelSticker;
    }

    public void setLabelSticker(String labelSticker) {
        this.labelSticker = labelSticker;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
//
//    public List<OrderServe> getListOrderServe() {
//        return listOrderServe;
//    }
//
//    public void setListOrderServe(List<OrderServe> listOrderServe) {
//        this.listOrderServe = listOrderServe;
//    }

    public List<OrderCommentLabel> getListOrderCommentLabel() {
        return listOrderCommentLabel;
    }

    public void setListOrderCommentLabel(List<OrderCommentLabel> listOrderCommentLabel) {
        this.listOrderCommentLabel = listOrderCommentLabel;
    }

    /*public MultipartFile[] getFormData() {
        return formData;
    }

    public void setFormData(MultipartFile[] formData) {
        this.formData = formData;
    }*/

    @Override
    public String toString() {
        return "OrderComment{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", orderServeId=" + orderServeId +
                ", userId=" + userId +
                ", level=" + level +
                ", remark='" + remark + '\'' +
                ", picturePath='" + picturePath + '\'' +
                ", labelSticker='" + labelSticker + '\'' +
                ", createTime=" + createTime +
                ", finishTime=" + finishTime +
                ", status=" + status +
                ", departmentName='" + departmentName + '\'' +
                ", orderNo='" + orderNo + '\'' +
                ", customer=" + customer +
//                ", listOrderServe=" + listOrderServe +
                ", listOrderCommentLabel=" + listOrderCommentLabel +
                '}';
    }
}
