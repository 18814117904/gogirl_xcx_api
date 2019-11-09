package com.gogirl.gogirl_technician.technician_commons.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gogirl.gogirl_store.store_commons.dto.ClassesTechnician;
import com.gogirl.gogirl_store.store_commons.dto.ScheduleServe;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by yinyong on 2018/9/18.
 */
public class TechnicianManage {
    private Integer id;
    private Integer technicianId;
    private String openid;
    private String picturePath;
    private String technicianNo;
    private String name;
    private Integer departmentId;
    private String departmentName;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;
    private String mobile;
    private String userAuthority;
    private String grade;
    private Double seniority;
    private Double informationIntegrity;
    private Integer complaintNumber;
    private String complaintReason;
    private Integer serviceNumberTotal;
    private BigDecimal serviceMoneyTotal;
    private BigDecimal applyCardMoneyTotal;
    private Double score;
    private Integer userId;
    private String jobs;
    private Date time;//发消息模板时,被预约时间
    private String serveName;//发消息模板时,被预约项目

    private Integer status = 0;
    private ClassesTechnician classesTechnician;
    private List<ScheduleServe> listScheduleServer;
    
    public Integer getId() {
        return id;
    }

    public String getJobs() {
		return jobs;
	}

	public void setJobs(String jobs) {
		this.jobs = jobs;
	}

	public Integer getTechnicianId() {
		return technicianId;
	}

	public void setTechnicianId(Integer technicianId) {
		this.technicianId = technicianId;
	}

	public List<ScheduleServe> getListScheduleServer() {
		return listScheduleServer;
	}

	public void setListScheduleServer(List<ScheduleServe> listScheduleServer) {
		this.listScheduleServer = listScheduleServer;
	}

	public ClassesTechnician getClassesTechnician() {
		return classesTechnician;
	}

	public void setClassesTechnician(ClassesTechnician classesTechnician) {
		this.classesTechnician = classesTechnician;
	}

	public void setId(Integer id) {
        this.id = id;
    }

    public String getOpenid() {
		return openid;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    public String getTechnicianNo() {
        return technicianNo;
    }

    public void setTechnicianNo(String technicianNo) {
        this.technicianNo = technicianNo;
    }

    public String getName() {
        return name;
    }

    public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public void setName(String name) {
        this.name = name;
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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUserAuthority() {
        return userAuthority;
    }

    public void setUserAuthority(String userAuthority) {
        this.userAuthority = userAuthority;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public Double getSeniority() {
        return seniority;
    }

    public void setSeniority(Double seniority) {
        this.seniority = seniority;
    }

    public Double getInformationIntegrity() {
        return informationIntegrity;
    }

    public void setInformationIntegrity(Double informationIntegrity) {
        this.informationIntegrity = informationIntegrity;
    }

    public Integer getComplaintNumber() {
        return complaintNumber;
    }

    public void setComplaintNumber(Integer complaintNumber) {
        this.complaintNumber = complaintNumber;
    }

    public String getComplaintReason() {
        return complaintReason;
    }

    public void setComplaintReason(String complaintReason) {
        this.complaintReason = complaintReason;
    }

    public Integer getServiceNumberTotal() {
        return serviceNumberTotal;
    }

    public void setServiceNumberTotal(Integer serviceNumberTotal) {
        this.serviceNumberTotal = serviceNumberTotal;
    }

    public BigDecimal getServiceMoneyTotal() {
        return serviceMoneyTotal;
    }

    public void setServiceMoneyTotal(BigDecimal serviceMoneyTotal) {
        this.serviceMoneyTotal = serviceMoneyTotal;
    }

    public BigDecimal getApplyCardMoneyTotal() {
        return applyCardMoneyTotal;
    }

    public void setApplyCardMoneyTotal(BigDecimal applyCardMoneyTotal) {
        this.applyCardMoneyTotal = applyCardMoneyTotal;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getServeName() {
		return serveName;
	}

	public void setServeName(String serveName) {
		this.serveName = serveName;
	}

	@Override
    public String toString() {
        return "TechnicianManage{" +
                "id=" + id +
                ", picturePath='" + picturePath + '\'' +
                ", technicianNo='" + technicianNo + '\'' +
                ", name='" + name + '\'' +
                ", departmentId=" + departmentId +
                ", createTime=" + createTime +
                ", mobile='" + mobile + '\'' +
                ", userAuthority='" + userAuthority + '\'' +
                ", grade='" + grade + '\'' +
                ", seniority=" + seniority +
                ", informationIntegrity=" + informationIntegrity +
                ", complaintNumber=" + complaintNumber +
                ", complaintReason='" + complaintReason + '\'' +
                ", serviceNumberTotal=" + serviceNumberTotal +
                ", serviceMoneyTotal=" + serviceMoneyTotal +
                ", applyCardMoneyTotal=" + applyCardMoneyTotal +
                ", score=" + score +
                ", status=" + status +
                '}';
    }
}
