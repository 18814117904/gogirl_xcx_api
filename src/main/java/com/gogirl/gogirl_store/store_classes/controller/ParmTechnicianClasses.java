package com.gogirl.gogirl_store.store_classes.controller;

import java.util.List;

import com.gogirl.gogirl_technician.technician_commons.dto.UserTechnician;

public class ParmTechnicianClasses{
	String token;
	String dayString;
	List<UserTechnician> list;
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getDayString() {
		return dayString;
	}
	public void setDayString(String dayString) {
		this.dayString = dayString;
	}
	public List<UserTechnician> getList() {
		return list;
	}
	public void setList(List<UserTechnician> list) {
		this.list = list;
	}

}