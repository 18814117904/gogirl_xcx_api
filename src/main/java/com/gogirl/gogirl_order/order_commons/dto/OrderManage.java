package com.gogirl.gogirl_order.order_commons.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gogirl.gogirl_user.entity.CouponOrderRelevance;
import com.gogirl.gogirl_user.entity.Customer;
import com.gogirl.gogirl_user.entity.CustomerBalanceRecord;
import com.gogirl.gogirl_user.entity.TimesCardUsedRecord;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by yinyong on 2018/9/18.
 */
public class OrderManage implements Serializable {

    private Integer id;  //订单编号
    private String orderNo;  //订单no
    private Integer orderUser;  //用户id
    private String telephone;  //用户电话号码
    private String storeScheduleUsername;  //预约用户名
    private Integer departmentId;  //店铺编号
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;  //创建时间
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date updateTime;  //修改时间
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date launchTime;  //开始付款时间
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date finishTime;  //完成时间
    private BigDecimal totalPrice;  //总价
    private BigDecimal changePrice;  //改价
    private BigDecimal discountPrice;  //折后价
    private Integer paymentType;  //支付方式
    private Integer status;  // 订单状态
    private String remark;  //备注
    private String message;  //支付备注
    private String createUser;  //创建人
    private Integer orderType;  //订单类型
    private String delRemark;
    private Integer couponRelevanceId;  //优惠券id
    private BigDecimal couponRelevancePrice; //优惠券实际价格

    private List<OrderServe> listOrderServer;
    private List<CouponOrderRelevance> listCouponOrderRelevance;
    private List<CustomerBalanceRecord> listCustomerBalanceRecord;
    private List<TimesCardUsedRecord> listTimesCardRecord;
    
    private String departmentName;  //店铺名字
    private String changeStatus = "false";  //是否改价
    private String deleteOrder; //删除订单

    private Customer customer; //预约用户信息

    private String exportTime;  //导出订单按月份

    private String startTime; // 统计开始时间
    private String endTime;   // 统计结束时间

    private String receipt;   //是否是收款

    private Integer scheduledId; //关联预约id
    private UserManage userManage; //开单人
    private String multiplePaymentType; //多选支付方式
    private Integer openOrderUser; //开单人id

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date scheduledTime;  //预约建立时间
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date arriveTime; //预约时间
    private Double dataIntegrity; //数据完整度

    private String reminder; //待售后

    private String dataInfo; //信息填写
    private String token;
    private List<String> images;
    
    
    
    public Integer getId() {
        return id;
    }

    public List<TimesCardUsedRecord> getListTimesCardRecord() {
		return listTimesCardRecord;
	}

	public void setListTimesCardRecord(List<TimesCardUsedRecord> listTimesCardRecord) {
		this.listTimesCardRecord = listTimesCardRecord;
	}

	public String getDelRemark() {
		return delRemark;
	}

	public List<CouponOrderRelevance> getListCouponOrderRelevance() {
		return listCouponOrderRelevance;
	}

	public List<String> getImages() {
		return images;
	}

	public void setImages(List<String> images) {
		this.images = images;
	}

	public void setListCouponOrderRelevance(
			List<CouponOrderRelevance> listCouponOrderRelevance) {
		this.listCouponOrderRelevance = listCouponOrderRelevance;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public void setDelRemark(String delRemark) {
		this.delRemark = delRemark;
	}

	public void setId(Integer id) {
        this.id = id;
    }

    public List<CustomerBalanceRecord> getListCustomerBalanceRecord() {
		return listCustomerBalanceRecord;
	}

	public void setListCustomerBalanceRecord(
			List<CustomerBalanceRecord> listCustomerBalanceRecord) {
		this.listCustomerBalanceRecord = listCustomerBalanceRecord;
	}

	public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getOrderUser() {
        return orderUser;
    }

    public void setOrderUser(Integer orderUser) {
        this.orderUser = orderUser;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getStoreScheduleUsername() {
        return storeScheduleUsername;
    }

    public void setStoreScheduleUsername(String storeScheduleUsername) {
        this.storeScheduleUsername = storeScheduleUsername;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getLaunchTime() {
        return launchTime;
    }

    public void setLaunchTime(Date launchTime) {
        this.launchTime = launchTime;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public BigDecimal getChangePrice() {
        return changePrice;
    }

    public void setChangePrice(BigDecimal changePrice) {
        this.changePrice = changePrice;
    }

    public BigDecimal getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(BigDecimal discountPrice) {
        this.discountPrice = discountPrice;
    }

    public Integer getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(Integer paymentType) {
        this.paymentType = paymentType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public List<OrderServe> getListOrderServer() {
        return listOrderServer;
    }

    public void setListOrderServer(List<OrderServe> listOrderServer) {
        this.listOrderServer = listOrderServer;
    }

    public String getChangeStatus() {
        return changeStatus;
    }

    public void setChangeStatus(String changeStatus) {
        this.changeStatus = changeStatus;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDeleteOrder() {
        return deleteOrder;
    }

    public void setDeleteOrder(String deleteOrder) {
        this.deleteOrder = deleteOrder;
    }

    public String getExportTime() {
        return exportTime;
    }

    public void setExportTime(String exportTime) {
        this.exportTime = exportTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getReceipt() {
        return receipt;
    }

    public void setReceipt(String receipt) {
        this.receipt = receipt;
    }

    public Integer getCouponRelevanceId() {
        return couponRelevanceId;
    }

    public void setCouponRelevanceId(Integer couponRelevanceId) {
        this.couponRelevanceId = couponRelevanceId;
    }

    public Integer getScheduledId() {
        return scheduledId;
    }

    public void setScheduledId(Integer scheduledId) {
        this.scheduledId = scheduledId;
    }

    public UserManage getUserManage() {
        return userManage;
    }

    public void setUserManage(UserManage userManage) {
        this.userManage = userManage;
    }

    public String getMultiplePaymentType() {
        return multiplePaymentType;
    }

    public void setMultiplePaymentType(String multiplePaymentType) {
        this.multiplePaymentType = multiplePaymentType;
    }

    public Integer getOpenOrderUser() {
        return openOrderUser;
    }

    public void setOpenOrderUser(Integer openOrderUser) {
        this.openOrderUser = openOrderUser;
    }


    public Double getDataIntegrity() {
        return dataIntegrity;
    }

    public void setDataIntegrity(Double dataIntegrity) {
        this.dataIntegrity = dataIntegrity;
    }

    public BigDecimal getCouponRelevancePrice() {
        return couponRelevancePrice;
    }

    public void setCouponRelevancePrice(BigDecimal couponRelevancePrice) {
        this.couponRelevancePrice = couponRelevancePrice;
    }

    public String getReminder() {
        return reminder;
    }

    public void setReminder(String reminder) {
        this.reminder = reminder;
    }

    public String getDataInfo() {
        return dataInfo;
    }

    public void setDataInfo(String dataInfo) {
        this.dataInfo = dataInfo;
    }



	public Date getScheduledTime() {
		return scheduledTime;
	}

	public void setScheduledTime(Date scheduledTime) {
		this.scheduledTime = scheduledTime;
	}

	public Date getArriveTime() {
		return arriveTime;
	}

	public void setArriveTime(Date arriveTime) {
		this.arriveTime = arriveTime;
	}

	@Override
    public String toString() {
        return "OrderManage{" +
                "id=" + id +
                ", orderNo='" + orderNo + '\'' +
                ", orderUser=" + orderUser +
                ", telephone='" + telephone + '\'' +
                ", storeScheduleUsername='" + storeScheduleUsername + '\'' +
                ", departmentId=" + departmentId +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", launchTime=" + launchTime +
                ", finishTime=" + finishTime +
                ", totalPrice=" + totalPrice +
                ", changePrice=" + changePrice +
                ", discountPrice=" + discountPrice +
                ", paymentType=" + paymentType +
                ", status=" + status +
                ", remark='" + remark + '\'' +
                ", message='" + message + '\'' +
                ", createUser='" + createUser + '\'' +
                ", orderType=" + orderType +
                ", couponRelevanceId=" + couponRelevanceId +
                ", couponRelevancePrice=" + couponRelevancePrice +
                ", listOrderServer=" + listOrderServer +
                ", departmentName='" + departmentName + '\'' +
                ", changeStatus='" + changeStatus + '\'' +
                ", deleteOrder='" + deleteOrder + '\'' +
                ", customer=" + customer +
                ", exportTime='" + exportTime + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", receipt='" + receipt + '\'' +
                ", scheduledId=" + scheduledId +
                ", userManage=" + userManage +
                ", multiplePaymentType='" + multiplePaymentType + '\'' +
                ", openOrderUser=" + openOrderUser +
                ", scheduledTime=" + scheduledTime +
                ", arriveTime=" + arriveTime +
                ", dataIntegrity=" + dataIntegrity +
                ", reminder='" + reminder + '\'' +
                ", dataInfo='" + dataInfo + '\'' +
                '}';
    }
}
