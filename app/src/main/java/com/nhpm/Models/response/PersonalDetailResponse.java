package com.nhpm.Models.response;

import com.google.gson.Gson;
import com.nhpm.Models.ApplicationDataModel;

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
    private String rsby_no="";
    private String rsby_mem_id="";
    private String rsby_source="";
    private String guid="";
    private String stateCode="";
    private String districtCode="";
    private String blockCode="";
    private String villageCode="";
    private String ruralUrbanCode="";
    private String channel="1";
    private String amRelationHAId="";
    private String amRelation="";
    private String amProofDocType="";
    private String amProofDocNo="";
    private String amProofDocPhoto="";



    private String aadhaarConsent;
    private String careOfTypeDec;
    private String careOfDec;
    private String addressDec;
    private String sourceOfData;
    private String uidToken;
    private String uidAuthType;

    private int stateCodeLgdBen=0;
    private int districtCodeLgdBen=0;
    private int subDistrictCodeLgdBen=0;
    private int villageTownCodeLgdBen=0;
    private String ruralUrbanBen;
    private String aadhaarConsentVer;


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

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    public String getBlockCode() {
        return blockCode;
    }

    public void setBlockCode(String blockCode) {
        this.blockCode = blockCode;
    }

    public String getVillageCode() {
        return villageCode;
    }

    public void setVillageCode(String villageCode) {
        this.villageCode = villageCode;
    }

    public String getRuralUrbanCode() {
        return ruralUrbanCode;
    }

    public void setRuralUrbanCode(String ruralUrbanCode) {
        this.ruralUrbanCode = ruralUrbanCode;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getAmRelationHAId() {
        return amRelationHAId;
    }

    public void setAmRelationHAId(String amRelationHAId) {
        this.amRelationHAId = amRelationHAId;
    }

    public String getAmRelation() {
        return amRelation;
    }

    public void setAmRelation(String amRelation) {
        this.amRelation = amRelation;
    }

    public String getAmProofDocType() {
        return amProofDocType;
    }

    public void setAmProofDocType(String amProofDocType) {
        this.amProofDocType = amProofDocType;
    }

    public String getAmProofDocNo() {
        return amProofDocNo;
    }

    public void setAmProofDocNo(String amProofDocNo) {
        this.amProofDocNo = amProofDocNo;
    }

    public String getAmProofDocPhoto() {
        return amProofDocPhoto;
    }

    public void setAmProofDocPhoto(String amProofDocPhoto) {
        this.amProofDocPhoto = amProofDocPhoto;
    }

    public String getRsby_no() {
        return rsby_no;
    }

    public void setRsby_no(String rsby_no) {
        this.rsby_no = rsby_no;
    }

    public String getRsby_mem_id() {
        return rsby_mem_id;
    }

    public void setRsby_mem_id(String rsby_mem_id) {
        this.rsby_mem_id = rsby_mem_id;
    }

    public String getRsby_source() {
        return rsby_source;
    }

    public void setRsby_source(String rsby_source) {
        this.rsby_source = rsby_source;
    }

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


    static public PersonalDetailResponse create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, PersonalDetailResponse.class);
    }

    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
