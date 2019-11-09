package com.gogirl.gogirl_order.order_commons.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gogirl.gogirl_user.entity.Customer;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by yinyong on 2018/9/28.
 */
public class ScheduleManage implements Serializable {

    private Integer oldId;//修改预约的时候,修改前的id
    private Integer id;//选时间的时候就新建的id(删除状态),在确定预约按钮改为已预约状态
    private String scheduledNo;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date scheduledTime; //下单时间
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date lastUpdateTime; //最近修改时间
    private Integer scheduledUser; //下单人
    private String storeScheduleUsername; //店铺端预约用户名
    private String telephone; //店铺端预约手机号码
    private String arriveTimeString; //预约到达时间字符创
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date arriveTime; //预约到达时间
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date lastServiceTime; //上次服务时间
    private String arriveUser; //到店人
    private Integer departmentId; //下单门店
    private String remark; //备注
    private String delRemaerk; //删除备注
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date openbillTime; //开单时间
    private Integer status; //已预约#1  失约#2  守约#3  已取消#4 已删除#5
    private String createUser;  //创建人

    private List<ScheduleServe> listScheduleServer; //预约下服务列表
    private Customer customer; //预约用户信息
    private String departmentName; //店铺名称
    private String deleteServe;  //删除服务字段
    private Integer scheduledType;

    private Integer remindStatus; //提醒类型
    private Integer isRead; //已读状态

    private String startTime;  //统计开始时间
    private String endTime; //统计结束时间

    private String sameDayTime;  //分页查询当天预约
    private String token;
    private String formId;
    private String scheduleDate;
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOldId() {
		return oldId;
	}

	public void setOldId(Integer oldId) {
		this.oldId = oldId;
	}

	public String getFormId() {
		return formId;
	}

	public String getArriveTimeString() {
		return arriveTimeString;
	}

	public void setArriveTimeString(String arriveTimeString) {
		this.arriveTimeString = arriveTimeString;
	}

	public String getScheduleDate() {
		return scheduleDate;
	}

	public void setScheduleDate(String scheduleDate) {
		this.scheduleDate = scheduleDate;
	}

	public void setFormId(String formId) {
		this.formId = formId;
	}

	public String getScheduledNo() {
        return scheduledNo;
    }

    public void setScheduledNo(String scheduledNo) {
        this.scheduledNo = scheduledNo;
    }

    public Date getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(Date scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public Integer getScheduledUser() {
        return scheduledUser;
    }

    public void setScheduledUser(Integer scheduledUser) {
        this.scheduledUser = scheduledUser;
    }

    public String getStoreScheduleUsername() {
        return storeScheduleUsername;
    }

    public void setStoreScheduleUsername(String storeScheduleUsername) {
        this.storeScheduleUsername = storeScheduleUsername;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Date getArriveTime() {
		return arriveTime;
	}

	public void setArriveTime(Date arriveTime) {
		this.arriveTime = arriveTime;
	}

	public Date getLastServiceTime() {
        return lastServiceTime;
    }

    public void setLastServiceTime(Date lastServiceTime) {
        this.lastServiceTime = lastServiceTime;
    }

    public String getArriveUser() {
        return arriveUser;
    }

    public void setArriveUser(String arriveUser) {
        this.arriveUser = arriveUser;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDelRemaerk() {
        return delRemaerk;
    }

    public void setDelRemaerk(String delRemaerk) {
        this.delRemaerk = delRemaerk;
    }

    public Date getOpenbillTime() {
        return openbillTime;
    }

    public void setOpenbillTime(Date openbillTime) {
        this.openbillTime = openbillTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<ScheduleServe> getListScheduleServer() {
        return listScheduleServer;
    }

    public void setListScheduleServer(List<ScheduleServe> listScheduleServer) {
        this.listScheduleServer = listScheduleServer;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDeleteServe() {
        return deleteServe;
    }

    public void setDeleteServe(String deleteServe) {
        this.deleteServe = deleteServe;
    }

    public Integer getScheduledType() {
        return scheduledType;
    }

    public void setScheduledType(Integer scheduledType) {
        this.scheduledType = scheduledType;
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

    public Integer getRemindStatus() {
        return remindStatus;
    }

    public void setRemindStatus(Integer remindStatus) {
        this.remindStatus = remindStatus;
    }

    public Integer getIsRead() {
        return isRead;
    }

    public void setIsRead(Integer isRead) {
        this.isRead = isRead;
    }

    public String getSameDayTime() {
        return sameDayTime;
    }

    public void setSameDayTime(String sameDayTime) {
        this.sameDayTime = sameDayTime;
    }

    public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Override
    public String toString() {
        return "ScheduleManage{" +
                "id=" + id +
                ", scheduledNo='" + scheduledNo + '\'' +
                ", scheduledTime=" + scheduledTime +
                ", lastUpdateTime=" + lastUpdateTime +
                ", scheduledUser=" + scheduledUser +
                ", storeScheduleUsername='" + storeScheduleUsername + '\'' +
                ", telephone='" + telephone + '\'' +
                ", arriveTime='" + arriveTime + '\'' +
                ", lastServiceTime=" + lastServiceTime +
                ", arriveUser='" + arriveUser + '\'' +
                ", departmentId=" + departmentId +
                ", remark='" + remark + '\'' +
                ", delRemaerk='" + delRemaerk + '\'' +
                ", openbillTime=" + openbillTime +
                ", status=" + status +
                ", createUser='" + createUser + '\'' +
                ", listScheduleServer=" + listScheduleServer +
                ", customer=" + customer +
                ", departmentName='" + departmentName + '\'' +
                ", deleteServe='" + deleteServe + '\'' +
                ", scheduledType=" + scheduledType +
                ", remindStatus=" + remindStatus +
                ", isRead=" + isRead +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", sameDayTime='" + sameDayTime + '\'' +
                '}';
    }
}
