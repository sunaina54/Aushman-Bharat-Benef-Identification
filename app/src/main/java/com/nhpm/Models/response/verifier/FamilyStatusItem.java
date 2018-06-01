package com.nhpm.Models.response.verifier;

import java.io.Serializable;

/**
 * Created by Anand on 04-11-2016.
 */
public class FamilyStatusItem  implements Serializable{
    public String statusType;
    public String statusCode;
    public String statusDesc;
    public String sno;


    public FamilyStatusItem() {
    }

    public FamilyStatusItem(String statusType, String statusCode, String statusDesc, String sno) {
        this.statusType = statusType;
        this.statusCode = statusCode;
        this.statusDesc = statusDesc;
        this.sno = sno;
    }

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
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
}
