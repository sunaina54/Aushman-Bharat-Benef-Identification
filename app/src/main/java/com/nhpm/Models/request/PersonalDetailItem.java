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
    private String state;
    private String district;
    private String pinCode;
    private int opertaorid;
    private String isAadhar;
    private int nameMatchScore;
    private String idName;


    private String fatherNameSecc;
    private String motherNameSecc;
    private String genderIdSecc;
    private String yobSecc;
    private String dobBen;
    private String genderBen;
    private String addressBen;
    private String stateNameBen;
    private String districtNameBen;
    private String subDistrictBen;
    private String VtcBen;
    private String postOfficeBen;
    private String pinCodeBen;
    private String emailBen;
    private String idPhoto1;
    private String memberType;

    private String aadhaarConsent;
    private String careOfTypeDec;
    private String careOfDec;
    private String addressDec;
    private String sourceOfData;
    private String uidToken;
    private String uidAuthType;
    private int stateCodeLgdBen;
    private int districtCodeLgdBen;
    private int subDistrictCodeLgdBen;
    private int villageTownCodeLgdBen;
    private String ruralUrbanBen;
    private String aadhaarConsentVer;

    // define by sunaina -> co type and value
    private String coTypeValue;


                /*"aadhaarConsent": "y",
                        "careOfTypeDec": "careOfTypeDec",
                        "careOfDec": "careOfDec",
                        "addressDec": "addressDec",
                        "sourceOfData": "R",
                        "uidToken": "uidToken",
                        "uidAuthType": "OTP",
                        "stateCodeLgdBen": 5,
                        "districtCodeLgdBen": 68,
                        "subDistrictCodeLgdBen": 97969,
                        "villageTownCodeLgdBen": 123456,
                        "ruralUrbanBen": "r",
                        "aadhaarConsentVer": "consent",*/


    	/*"guid": "guid",
                "stateCode": "s",
                "districtCode": "d",
                "blockCode": "b",
                "villageCode": "v",
                "ruralUrbanCode": "r",
                  "channel": 1,
                   "amRelationHAId": "HAId",
                "amRelation": "amRelation",
                "amProofDocType": "amProofDocType",
                "amProofDocNo": "amProofDocNo",
                "amProofDocPhoto": "/9jg45g4gwg534ww/4AA"*/

    public String getCoTypeValue() {
        return coTypeValue;
    }

    public void setCoTypeValue(String coTypeValue) {
        this.coTypeValue = coTypeValue;
    }

    public String getAadhaarConsent() {
        return aadhaarConsent;
    }

    public void setAadhaarConsent(String aadhaarConsent) {
        this.aadhaarConsent = aadhaarConsent;
    }

    public String getCareOfTypeDec() {
        return careOfTypeDec;
    }

    public void setCareOfTypeDec(String careOfTypeDec) {
        this.careOfTypeDec = careOfTypeDec;
    }

    public String getCareOfDec() {
        return careOfDec;
    }

    public void setCareOfDec(String careOfDec) {
        this.careOfDec = careOfDec;
    }

    public String getAddressDec() {
        return addressDec;
    }

    public void setAddressDec(String addressDec) {
        this.addressDec = addressDec;
    }

    public String getSourceOfData() {
        return sourceOfData;
    }

    public void setSourceOfData(String sourceOfData) {
        this.sourceOfData = sourceOfData;
    }

    public String getUidToken() {
        return uidToken;
    }

    public void setUidToken(String uidToken) {
        this.uidToken = uidToken;
    }

    public String getUidAuthType() {
        return uidAuthType;
    }

    public void setUidAuthType(String uidAuthType) {
        this.uidAuthType = uidAuthType;
    }

    public int getStateCodeLgdBen() {
        return stateCodeLgdBen;
    }

    public void setStateCodeLgdBen(int stateCodeLgdBen) {
        this.stateCodeLgdBen = stateCodeLgdBen;
    }

    public int getDistrictCodeLgdBen() {
        return districtCodeLgdBen;
    }

    public void setDistrictCodeLgdBen(int districtCodeLgdBen) {
        this.districtCodeLgdBen = districtCodeLgdBen;
    }

    public int getSubDistrictCodeLgdBen() {
        return subDistrictCodeLgdBen;
    }

    public void setSubDistrictCodeLgdBen(int subDistrictCodeLgdBen) {
        this.subDistrictCodeLgdBen = subDistrictCodeLgdBen;
    }

    public int getVillageTownCodeLgdBen() {
        return villageTownCodeLgdBen;
    }

    public void setVillageTownCodeLgdBen(int villageTownCodeLgdBen) {
        this.villageTownCodeLgdBen = villageTownCodeLgdBen;
    }

    public String getRuralUrbanBen() {
        return ruralUrbanBen;
    }

    public void setRuralUrbanBen(String ruralUrbanBen) {
        this.ruralUrbanBen = ruralUrbanBen;
    }

    public String getAadhaarConsentVer() {
        return aadhaarConsentVer;
    }

    public void setAadhaarConsentVer(String aadhaarConsentVer) {
        this.aadhaarConsentVer = aadhaarConsentVer;
    }

    public String getMemberType() {
        return memberType;
    }

    public void setMemberType(String memberType) {
        this.memberType = memberType;
    }

    public String getIdPhoto1() {
        return idPhoto1;
    }

    public void setIdPhoto1(String idPhoto1) {
        this.idPhoto1 = idPhoto1;
    }

    private String operatorMatchScoreStatus;

    public String getOperatorMatchScoreStatus() {
        return operatorMatchScoreStatus;
    }

    public void setOperatorMatchScoreStatus(String operatorMatchScoreStatus) {
        this.operatorMatchScoreStatus = operatorMatchScoreStatus;
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
        return VtcBen;
    }

    public void setVtcBen(String vtcBen) {
        VtcBen = vtcBen;
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

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getIdName() {
        return idName;
    }

    public void setIdName(String idName) {
        this.idName = idName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

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
