package com.gogirl.gogirl_order.order_commons.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.sql.Time;

/**
 * Created by yinyong on 2018/9/17.
 */
public class ShopManage implements Serializable {

    private Integer id;
    private String storeNo;
    private String logo;
    private String name;
    private String longitude;
    private String latitude;
    private String address;
    private String master;
    private Integer employeeNumber;
    private String customerServiceTelphone;
    private String contactName;
    private String contactTelphone;
    private String remark;
    private Integer businessDay;
    @JsonFormat(pattern="HH:mm:ss",timezone="GMT+8")
    private Time businessStartTime;
    @JsonFormat(pattern="HH:mm:ss",timezone="GMT+8")
    private Time businessEndTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStoreNo() {
        return storeNo;
    }

    public void setStoreNo(String storeNo) {
        this.storeNo = storeNo;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMaster() {
        return master;
    }

    public void setMaster(String master) {
        this.master = master;
    }

    public Integer getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(Integer employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public String getCustomerServiceTelphone() {
        return customerServiceTelphone;
    }

    public void setCustomerServiceTelphone(String customerServiceTelphone) {
        this.customerServiceTelphone = customerServiceTelphone;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactTelphone() {
        return contactTelphone;
    }

    public void setContactTelphone(String contactTelphone) {
        this.contactTelphone = contactTelphone;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getBusinessDay() {
        return businessDay;
    }

    public void setBusinessDay(Integer businessDay) {
        this.businessDay = businessDay;
    }

    public Time getBusinessStartTime() {
        return businessStartTime;
    }

    public void setBusinessStartTime(Time businessStartTime) {
        this.businessStartTime = businessStartTime;
    }

    public Time getBusinessEndTime() {
        return businessEndTime;
    }

    public void setBusinessEndTime(Time businessEndTime) {
        this.businessEndTime = businessEndTime;
    }

    @Override
    public String toString() {
        return "ShopManage{" +
                "id=" + id +
                ", storeNo='" + storeNo + '\'' +
                ", logo='" + logo + '\'' +
                ", name='" + name + '\'' +
                ", longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                ", address='" + address + '\'' +
                ", master='" + master + '\'' +
                ", employeeNumber=" + employeeNumber +
                ", customerServiceTelphone='" + customerServiceTelphone + '\'' +
                ", contactName='" + contactName + '\'' +
                ", contactTelphone='" + contactTelphone + '\'' +
                ", remark='" + remark + '\'' +
                ", businessDay=" + businessDay +
                ", businessStartTime=" + businessStartTime +
                ", businessEndTime=" + businessEndTime +
                '}';
    }
}
