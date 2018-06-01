package com.nhpm.Models;

import java.io.Serializable;

/**
 * Created by Saurabh on 11-04-2017.
 */

public class NotificationModel implements Serializable {

    String id;
    String description;
    String aadhaarNo;
    String createDate;
    String ipAddress;
    String source;
    String targetState;
    String expireDate;
    String activeStatus;
    String startDate;

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAadhaarNo() {
        return aadhaarNo;
    }

    public void setAadhaarNo(String aadhaarNo) {
        this.aadhaarNo = aadhaarNo;
    }

    public String getCreatedDate() {
        return createDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createDate = createdDate;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTargetState() {
        return targetState;
    }

    public void setTargetState(String targetState) {
        this.targetState = targetState;
    }

    public String getDateExpire() {
        return expireDate;
    }

    public void setDateExpire(String dateExpire) {
        this.expireDate = dateExpire;
    }

    public String getActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(String activeStatus) {
        this.activeStatus = activeStatus;
    }
}
