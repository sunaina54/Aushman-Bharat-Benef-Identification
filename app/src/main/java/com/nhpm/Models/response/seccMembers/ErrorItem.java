package com.nhpm.Models.response.seccMembers;

import java.io.Serializable;

/**
 * Created by PSQ on 12/12/2016.
 */

public class ErrorItem implements Serializable {
    private String errorCode;
    private String errorMsg;
    private String ahlTin;
    private String errorType;
    private String hhdNo;
    private String nhpsMemId;

    public String getNhpsMemId() {
        return nhpsMemId;
    }

    public void setNhpsMemId(String nhpsMemId) {
        this.nhpsMemId = nhpsMemId;
    }

    public String getHhdNo() {
        return hhdNo;
    }

    public void setHhdNo(String hhdNo) {
        this.hhdNo = hhdNo;
    }

    public String getErrorType() {
        return errorType;
    }

    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }

    public String getAhlTin() {
        return ahlTin;
    }

    public void setAhlTin(String ahlTin) {
        this.ahlTin = ahlTin;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
