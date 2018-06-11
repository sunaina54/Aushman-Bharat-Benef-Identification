package com.nhpm.Models.response;

import java.io.Serializable;

public class PersonalDetailResponse implements Serializable{
    private String benefName;
    private Integer nameMatchScore;
    private String benefPhoto;
    private String govtIdNo;
    private String govtIdType;
    private String govtIdTypeId;
    private String idPhoto;
    private String idPhoto1;
    private String mobileNo;
    private String name;
    private String isAadhar;
    private Integer opertaorid;

    public String getBenefName() {
        return benefName;
    }

    public void setBenefName(String benefName) {
        this.benefName = benefName;
    }

    public Integer getNameMatchScore() {
        return nameMatchScore;
    }

    public void setNameMatchScore(Integer nameMatchScore) {
        this.nameMatchScore = nameMatchScore;
    }

    public String getBenefPhoto() {
        return benefPhoto;
    }

    public void setBenefPhoto(String benefPhoto) {
        this.benefPhoto = benefPhoto;
    }

    public String getGovtIdNo() {
        return govtIdNo;
    }

    public void setGovtIdNo(String govtIdNo) {
        this.govtIdNo = govtIdNo;
    }

    public String getGovtIdType() {
        return govtIdType;
    }

    public void setGovtIdType(String govtIdType) {
        this.govtIdType = govtIdType;
    }

    public String getGovtIdTypeId() {
        return govtIdTypeId;
    }

    public void setGovtIdTypeId(String govtIdTypeId) {
        this.govtIdTypeId = govtIdTypeId;
    }

    public String getIdPhoto() {
        return idPhoto;
    }

    public void setIdPhoto(String idPhoto) {
        this.idPhoto = idPhoto;
    }

    public String getIdPhoto1() {
        return idPhoto1;
    }

    public void setIdPhoto1(String idPhoto1) {
        this.idPhoto1 = idPhoto1;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsAadhar() {
        return isAadhar;
    }

    public void setIsAadhar(String isAadhar) {
        this.isAadhar = isAadhar;
    }

    public Integer getOpertaorid() {
        return opertaorid;
    }

    public void setOpertaorid(Integer opertaorid) {
        this.opertaorid = opertaorid;
    }
}
