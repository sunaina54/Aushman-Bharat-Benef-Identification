package com.nhpm.Models.request;

import java.io.Serializable;

public class PersonalDetailItem implements Serializable {
    private String aadhaarNo;
    private String benefPhoto;
    private String mobileNo;
    private String isAadhaarAuth;
    private String isMobileAuth;

    public String getAadhaarNo() {
        return aadhaarNo;
    }

    public void setAadhaarNo(String aadhaarNo) {
        this.aadhaarNo = aadhaarNo;
    }

    public String getBenefPhoto() {
        return benefPhoto;
    }

    public void setBenefPhoto(String benefPhoto) {
        this.benefPhoto = benefPhoto;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getIsAadhaarAuth() {
        return isAadhaarAuth;
    }

    public void setIsAadhaarAuth(String isAadhaarAuth) {
        this.isAadhaarAuth = isAadhaarAuth;
    }

    public String getIsMobileAuth() {
        return isMobileAuth;
    }

    public void setIsMobileAuth(String isMobileAuth) {
        this.isMobileAuth = isMobileAuth;
    }
}
