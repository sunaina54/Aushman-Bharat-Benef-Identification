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
    private String isMobileAuth;
    private String flowStatus;
    private String fatherNameSecc;
    private String motherNameSecc;
    private String genderIdSecc;
    private String yobSecc;
    private String dobBen="";
    private String genderBen="";
    private String addressBen="";
    private String stateNameBen="";
    private String districtNameBen="";
    private String subDistrictBen="";
    private String villageTownBen="";
    private String postOfficeBen="";
    private String pinCodeBen="";
    private String emailBen="";
    private String memberType="";






     /*"fatherNameSecc" : "",
             "motherNameSecc" : "",
             "genderIdSecc" : "",
             "yobSecc" : "",

             "dobBen" : "",
             "genderBen" : "",
             "addressBen" : "",
             "stateNameBen" : "",
             "districtNameBen" : "",
             "subDistrictBen" : "",
             "VtcBen" : "",
             "postOfficeBen" : "",
             "pinCodeBen" : "",
             "emailBen" : ""*/

    public String getMemberType() {
        return memberType;
    }

    public void setMemberType(String memberType) {
        this.memberType = memberType;
    }

    public String getFatherNameSecc() {
        return fatherNameSecc;
    }

    public void setFatherNameSecc(String fatherNameSecc) {
        this.fatherNameSecc = fatherNameSecc;
    }

    public String getMotherNameSecc() {
        return motherNameSecc;
    }

    public void setMotherNameSecc(String motherNameSecc) {
        this.motherNameSecc = motherNameSecc;
    }

    public String getGenderIdSecc() {
        return genderIdSecc;
    }

    public void setGenderIdSecc(String genderIdSecc) {
        this.genderIdSecc = genderIdSecc;
    }

    public String getYobSecc() {
        return yobSecc;
    }

    public void setYobSecc(String yobSecc) {
        this.yobSecc = yobSecc;
    }

    public String getDobBen() {
        return dobBen;
    }

    public void setDobBen(String dobBen) {
        this.dobBen = dobBen;
    }

    public String getGenderBen() {
        return genderBen;
    }

    public void setGenderBen(String genderBen) {
        this.genderBen = genderBen;
    }

    public String getAddressBen() {
        return addressBen;
    }

    public void setAddressBen(String addressBen) {
        this.addressBen = addressBen;
    }

    public String getStateNameBen() {
        return stateNameBen;
    }

    public void setStateNameBen(String stateNameBen) {
        this.stateNameBen = stateNameBen;
    }

    public String getDistrictNameBen() {
        return districtNameBen;
    }

    public void setDistrictNameBen(String districtNameBen) {
        this.districtNameBen = districtNameBen;
    }

    public String getSubDistrictBen() {
        return subDistrictBen;
    }

    public void setSubDistrictBen(String subDistrictBen) {
        this.subDistrictBen = subDistrictBen;
    }

    public String getVtcBen() {
        return villageTownBen;
    }

    public void setVtcBen(String villageTownBen) {
        this.villageTownBen = villageTownBen;
    }

    public String getPostOfficeBen() {
        return postOfficeBen;
    }

    public void setPostOfficeBen(String postOfficeBen) {
        this.postOfficeBen = postOfficeBen;
    }

    public String getPinCodeBen() {
        return pinCodeBen;
    }

    public void setPinCodeBen(String pinCodeBen) {
        this.pinCodeBen = pinCodeBen;
    }

    public String getEmailBen() {
        return emailBen;
    }

    public void setEmailBen(String emailBen) {
        this.emailBen = emailBen;
    }

    public String getFlowStatus() {
        return flowStatus;
    }

    public void setFlowStatus(String flowStatus) {
        this.flowStatus = flowStatus;
    }

    public String getIsMobileAuth() {
        return isMobileAuth;
    }

    public void setIsMobileAuth(String isMobileAuth) {
        this.isMobileAuth = isMobileAuth;
    }

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
