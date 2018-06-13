package com.nhpm.Models.request;

import com.google.gson.Gson;

import java.io.Serializable;

public class PersonalDetailItem implements Serializable {
    private String aadhaarNo;
    private String benefPhoto;
    private String idPhoto;
    private String mobileNo;
    private String isAadhaarAuth;
    private String isMobileAuth;
    private String name;
    private String gender;
    private String yob;
    private String govtIdNo;
    private String govtIdType;
    private String flowStatus;
    private String benefName;
    private int opertaorid;
    private String isAadhar;
    private int nameMatchScore;
    private FamilyDetailsItemModel familyDetailsItem;

    public int getNameMatchScore() {
        return nameMatchScore;
    }

    public void setNameMatchScore(int nameMatchScore) {
        this.nameMatchScore = nameMatchScore;
    }

    public int getOpertaorid() {
        return opertaorid;
    }

    public void setOpertaorid(int opertaorid) {
        this.opertaorid = opertaorid;
    }

    public String getIsAadhar() {
        return isAadhar;
    }

    public void setIsAadhar(String isAadhar) {
        this.isAadhar = isAadhar;
    }

    public FamilyDetailsItemModel getFamilyDetailsItem() {
        return familyDetailsItem;
    }

    public void setFamilyDetailsItem(FamilyDetailsItemModel familyDetailsItem) {
        this.familyDetailsItem = familyDetailsItem;
    }

    public String getBenefName() {
        return benefName;
    }

    public void setBenefName(String benefName) {
        this.benefName = benefName;
    }

    public String getFlowStatus() {
        return flowStatus;
    }

    public void setFlowStatus(String flowStatus) {
        this.flowStatus = flowStatus;
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

    public String getIdPhoto() {
        return idPhoto;
    }

    public void setIdPhoto(String idPhoto) {
        this.idPhoto = idPhoto;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getYob() {
        return yob;
    }

    public void setYob(String yob) {
        this.yob = yob;
    }

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

    static public PersonalDetailItem create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, PersonalDetailItem.class);
    }

    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
