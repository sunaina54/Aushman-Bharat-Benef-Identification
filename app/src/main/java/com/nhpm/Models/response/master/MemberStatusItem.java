package com.nhpm.Models.response.master;

import java.io.Serializable;

/**
 * Created by Anand on 20-11-2016.
 */

import java.io.Serializable;

/**
 * Created by Anand on 20-11-2016.
 */

public class MemberStatusItem implements Serializable {
    private String statusType;
    private String statusCode;
    private String statusDesc;
    private String order;
    private String isActive;
    public MemberStatusItem() {
    }

    public MemberStatusItem(String statusType, String statusCode, String statusDesc,String order,String isActive) {
        this.statusType = statusType;
        this.statusCode = statusCode;
        this.statusDesc = statusDesc;
        this.order=order;
        this.isActive=isActive;
    }
    /*public MemberStatusItem(String statusType, String statusCode, String statusDesc,String order) {
        this.statusType = statusType;
        this.statusCode = statusCode;
        this.statusDesc = statusDesc;
        this.sno=order;
        // this.isActive=isActive;
    }*/

    public String getSno() {
        return order;
    }

    public void setSno(String sno) {
        this.order = sno;
    }

    public String getStatusType() {
        return statusType;
    }

    public void setStatusType(String statusType) {
        this.statusType = statusType;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }
}
