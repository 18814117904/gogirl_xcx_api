package com.gogirl.gogirl_store.store_commons.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gogirl.gogirl_technician.technician_commons.dto.TechnicianManage;

/**
 * Created by yinyong on 2018/9/20.
 */
public class ClassesTechnician {

    private Integer id;
    private Integer userId;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone="GMT+8")
    private Date days;
    private Integer classes;
    private Integer status;

    private ClassesManage classesManage;

    private TechnicianManage technicianManage;
//
//    private List<ScheduleServe> listScheduleServe;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }



    public Date getDays() {
		return days;
	}

	public void setDays(Date days) {
		this.days = days;
	}

	public Integer getClasses() {
        return classes;
    }

    public void setClasses(Integer classes) {
        this.classes = classes;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public ClassesManage getClassesManage() {
        return classesManage;
    }

    public void setClassesManage(ClassesManage classesManage) {
        this.classesManage = classesManage;
    }

    public TechnicianManage getTechnicianManage() {
        return technicianManage;
    }

    public void setTechnicianManage(TechnicianManage technicianManage) {
        this.technicianManage = technicianManage;
    }

//    public List<ScheduleServe> getListScheduleServe() {
//        return listScheduleServe;
//    }
//
//    public void setListScheduleServe(List<ScheduleServe> listScheduleServe) {
//        this.listScheduleServe = listScheduleServe;
//    }

    @Override
    public String toString() {
        return "ClassesTechnician{" +
                "id=" + id +
                ", userId=" + userId +
                ", days='" + days + '\'' +
                ", classes=" + classes +
                ", status=" + status +
                ", classesManage=" + classesManage +
                ", technicianManage=" + technicianManage +
//                ", listScheduleServe=" + listScheduleServe +
                '}';
    }
}
