package com.nhpm.Models.response.seccMembers;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Anand on 01-11-2016.
 */

public class SeccMemberItem implements Serializable {

    private String nhpsMemId;
    private String memActiveStatus;
    private String printStatus;
    private String tinNpr;
    private String stateCode;
    private String districtCode;
    private String tehsilCode;
    private String townCode;
    private String wardId;
    //private String blockno;
    private String gramPanchayatCode;
    private String gramPanchayatName;
    private String ahlBlockNo;
    private String ahlSubBlockNo;
    private String slnohhd;
    private String slnomember;
    private String ahlSlNoHhd;
    private String ahlTypeOfeb;
    private String typeOfHhd;
    private String livingInShelter;
    private String name;
    private String nameSl;
    private String relation;
    private String relationSl;
    private String genderId;
    private String dob;
    private String mStatusId;
    private String fatherName;
    private String fatherNameSl;
    private String motherName;
    private String motherNameSl;
    private String occupation;
    private String occupationSl;
    private String educode;
    private String educationOther;
    private String disabilitycode;
    private String casteGroup;
    private String ahlTin;
    //  private String memberStatus;
    private String addressLine1;
    private String addressLine2;
    private String addressLine3;
    private String addressLine4;
    private String addressLine5;
    private String pinCode;
    private String addressLine1Sl;
    private String addressLine2sl;
    private String addressLine3Sl;
    private String addressLine4sl;
    private String addressLine5sl;
    private String dobFrmNpr;
    private String spousenm;
    private String hhdNo;
    private String aadhaarNo;
    private String nameAadhaar;
    private String urnNo;
    private String mobileNo;
    private String ruralUrban;
    private String bankIfsc;
    private String markForDeletion;
    private String aadhaarStatus;
    private String hhStatus;
    private String memStatus;
    private String eid;
    private String bankAccNo;
    private String schemeId;
    private String schemeNo;
    private String schemeId1;
    private String schemeNo1;
    private String schemeId2;
    private String schemeNo2;
    private String schemeId3;
    private String schemeNo3;
    private String nameNominee;
    private String relationNomineeCode;
    private String idType;
    private String idNo;
    private String nameAsId;
    private String idPhoto1;
    private String idPhoto2;
    private String memberPhoto;
    private String aadhaarCapturingMode;
    private String consent;
    private String aadhaarAuthMode;
    private String aadhaarAuthDt;
    private String aadhaarVerifiedBy;
    private String validatedBy;
    private String userid;
    private String dateUpdated;
    private String aadhaarAuth;
    private String mobileAuth;
    private String urnAuth;
    private String ifscAuth;
    private String bankAccAuth;
    private String stateSchemeCodeAuth;
    private String schemeCode;
    private String dataSource;
    private String nhpsId;
    private String mobileAuthDt;
    private String urnAuthDt;
    private String ifscAuthDt;
    private String bankAccAuthDt;
    private String stateSchemeCodeAuthDt;
    private String consentDt;
    private String aadhaarGender;
    private String aadhaarYob;
    private String aadhaarDob;
    private String aadhaarCo;
    private String aadhaarGname;
    private String aadhaarHouse;
    private String aadhaarStreet;
    private String aadhaarLoc;
    private String aadhaarVtc;
    private String aadhaarPo;
    private String aadhaarDist;
    private String aadhaarSubdist;
    private String aadhaarState;
    private String aadhaarPc;
    private String aadhaarLm;
    private String latitude;
    private String longitude;
    private String whoseMobile;
    private String consentPhoto;
    private String memberPhoto1;
    private String nhpsRelationCode;
    private String nhpsRelationName;
    private String nomineeRelationName;
    private String govtIdPhoto;
    private String lockedSave;
    private String govtIdSurveyedStat;
    private String aadhaarSurveyedStat;
    private String photoSurveyedStatus;
    private String mobileNoSurveyedStat;
    private String nomineeDetailSurveyedStat;
    private String syncedStatus;
    private String nomineeGaurdianName;
    private String syncDt;
    private ErrorItem errorItem;
    private String error_code;
    private String urnId;
    private String error_msg;
    private String error_type;
    private String appVersion;
    private String rsbyMemId;
    private String rsbyUrnId;
    private String rsbyIssuesTimespam;
    private String rsbyHofnamereg;
    private String rsbyDoorhouse;
    private String rsbyVillageCode;
    private String rsbyPanchyatTownCode;
    private String rsbyBlockCode;
    private String rsbyDistrictCode;
    private String rsbyStateCode;
    private String rsbyName;
    private String rsbyDob;
    private String rsbyGender;
    private String rsbyRelcode;
    private String rsbyInsccode;
    private String rsbyPolicyno;
    private String rsbyPolicyamt;
    private String rsbyStartdate;
    private String rsbyEnddate;
    private String rsbyCsmNo;
    private String rsbyCardType;
    private String rsbyCardCategory;
    private String rsbyMemid;//rsbyMemId
    private String rsbyCardMenutiaDetail;

    private String panchayatTownCode;
    private String cardType;
    private String policyExpiryDate;
    private String cardCategory;
    private String hofNameReg;
    private String policyAmount;
    private String policyNo;
    private String inscCode;
    private String gender;
    private String csmNo;
    private String startDate;
    private String issueTimeStamp;
    private String endDate;
    private String villageCode;



    public String getRsbyCardMenutiaDetail() {
        return rsbyCardMenutiaDetail;
    }

    public void setRsbyCardMenutiaDetail(String rsbyCardMenutiaDetail) {
        this.rsbyCardMenutiaDetail = rsbyCardMenutiaDetail;
    }

    public String getNomineeGaurdianName() {
        return nomineeGaurdianName;
    }

    public void setNomineeGaurdianName(String nomineeGaurdianName) {
        this.nomineeGaurdianName = nomineeGaurdianName;
    }

    public String getRsbyMemid() {
        return rsbyMemid;
    }

    public void setRsbyMemid(String rsbyMemid) {
        this.rsbyMemid = rsbyMemid;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }

    public String getError_type() {
        return error_type;
    }

    public void setError_type(String error_type) {
        this.error_type = error_type;
    }

    public String getSyncDt() {
        return syncDt;
    }

    public void setSyncDt(String syncDt) {
        this.syncDt = syncDt;
    }

    public String getNhpsMemId() {
        return nhpsMemId;
    }

    public void setNhpsMemId(String nhpsMemId) {
        this.nhpsMemId = nhpsMemId;
    }

    //REQUEST FOR DATA CORRECTION.
    private String reqName, reqFatherName, reqMotherName, reqRelationCode, reqRelationName, reqMarritalStatCode, reqGenderCode, reqDOB, reqOccupation;

    public ErrorItem getErrorItem() {
        return errorItem;
    }

    public void setErrorItem(ErrorItem errorItem) {
        this.errorItem = errorItem;
    }

    public String getSyncedStatus() {
        return syncedStatus;
    }

    public void setSyncedStatus(String syncedStatus) {
        this.syncedStatus = syncedStatus;
    }

    public String getGovtIdSurveyedStat() {
        return govtIdSurveyedStat;
    }

    public void setGovtIdSurveyedStat(String govtIdSurveyedStat) {
        this.govtIdSurveyedStat = govtIdSurveyedStat;
    }

    public String getAadhaarSurveyedStat() {
        return aadhaarSurveyedStat;
    }

    public void setAadhaarSurveyedStat(String aadhaarSurveyedStat) {
        this.aadhaarSurveyedStat = aadhaarSurveyedStat;
    }

    public String getPhotoSurveyedStatus() {
        return photoSurveyedStatus;
    }

    public void setPhotoSurveyedStatus(String photoSurveyedStatus) {
        this.photoSurveyedStatus = photoSurveyedStatus;
    }

    public String getMobileNoSurveyedStat() {
        return mobileNoSurveyedStat;
    }

    public void setMobileNoSurveyedStat(String mobileNoSurveyedStat) {
        this.mobileNoSurveyedStat = mobileNoSurveyedStat;
    }

    public String getNomineeDetailSurveyedStat() {
        return nomineeDetailSurveyedStat;
    }

    public void setNomineeDetailSurveyedStat(String nomineeDetailSurveyedStat) {
        this.nomineeDetailSurveyedStat = nomineeDetailSurveyedStat;
    }

    public String getLockedSave() {
        return lockedSave;
    }

    public void setLockedSave(String lockedSave) {
        this.lockedSave = lockedSave;
    }

    public String getReqRelationName() {
        return reqRelationName;
    }

    public String getMember_active_status() {
        return memActiveStatus;
    }

    public void setMember_active_status(String member_active_status) {
        this.memActiveStatus = member_active_status;
    }

    public void setReqRelationName(String reqRelationName) {
        this.reqRelationName = reqRelationName;
    }

    public String getReqName() {
        return reqName;
    }

    public void setReqName(String reqName) {
        this.reqName = reqName;
    }

    public String getReqFatherName() {
        return reqFatherName;
    }

    public void setReqFatherName(String reqFatherName) {
        this.reqFatherName = reqFatherName;
    }

    public String getReqMotherName() {
        return reqMotherName;
    }

    public void setReqMotherName(String reqMotherName) {
        this.reqMotherName = reqMotherName;
    }

    public String getReqRelationCode() {
        return reqRelationCode;
    }

    public void setReqRelationCode(String reqRelationCode) {
        this.reqRelationCode = reqRelationCode;
    }

    public String getReqMarritalStatCode() {
        return reqMarritalStatCode;
    }

    public void setReqMarritalStatCode(String reqMarritalStatCode) {
        this.reqMarritalStatCode = reqMarritalStatCode;
    }

    public String getReqGenderCode() {
        return reqGenderCode;
    }

    public void setReqGenderCode(String reqGenderCode) {
        this.reqGenderCode = reqGenderCode;
    }

    public String getReqDOB() {
        return reqDOB;
    }

    public void setReqDOB(String reqDOB) {
        this.reqDOB = reqDOB;
    }

    public String getReqOccupation() {
        return reqOccupation;
    }

    public void setReqOccupation(String reqOccupation) {
        this.reqOccupation = reqOccupation;
    }

    public String getGovtIdPhoto() {
        return govtIdPhoto;
    }

    public void setGovtIdPhoto(String govtIdPhoto) {
        this.govtIdPhoto = govtIdPhoto;
    }

    public String getNomineeRelationName() {
        return nomineeRelationName;
    }

    public void setNomineeRelationName(String nomineeRelationName) {
        this.nomineeRelationName = nomineeRelationName;
    }

    public String getNhpsRelationCode() {
        return nhpsRelationCode;
    }

    public void setNhpsRelationCode(String nhpsRelationCode) {
        this.nhpsRelationCode = nhpsRelationCode;
    }

    public String getNhpsRelationName() {
        return nhpsRelationName;
    }

    public void setNhpsRelationName(String nhpsRelationName) {
        this.nhpsRelationName = nhpsRelationName;
    }

    static public SeccMemberItem create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, SeccMemberItem.class);
    }

    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public String getMemberPhoto1() {
        return memberPhoto1;
    }

    public void setMemberPhoto1(String memberPhoto1) {
        this.memberPhoto1 = memberPhoto1;
    }

    public String getTinNpr() {
        return tinNpr;
    }

    public void setTinNpr(String tinNpr) {
        this.tinNpr = tinNpr;
    }

    public String getStatecode() {
        return stateCode;
    }

    public void setStatecode(String statecode) {
        this.stateCode = statecode;
    }

    public String getDistrictcode() {
        return districtCode;
    }

    public void setDistrictcode(String districtcode) {
        this.districtCode = districtcode;
    }

    public String getTehsilcode() {
        return tehsilCode;
    }

    public void setTehsilcode(String tehsilcode) {
        this.tehsilCode = tehsilcode;
    }

    public String getTowncode() {
        return townCode;
    }

    public void setTowncode(String towncode) {
        this.townCode = towncode;
    }

    public String getWardid() {
        return wardId;
    }

    public void setWardid(String wardid) {
        this.wardId = wardid;
    }

   /* public String getBlockno() {
        return blockno;
    }

    public void setBlockno(String blockno) {
        this.blockno = blockno;
    }*/

    public String getGrampanchayatcode() {
        return gramPanchayatCode;
    }

    public void setGrampanchayatcode(String grampanchayatcode) {
        this.gramPanchayatCode = grampanchayatcode;
    }

    public String getGrampanchayatname() {
        return gramPanchayatName;
    }

    public void setGrampanchayatname(String grampanchayatname) {
        this.gramPanchayatName = grampanchayatname;
    }

    public String getAhlblockno() {
        return ahlBlockNo;
    }

    public void setAhlblockno(String ahlblockno) {
        this.ahlBlockNo = ahlblockno;
    }

    public String getAhlsubblockno() {
        return ahlSubBlockNo;
    }

    public void setAhlsubblockno(String ahlsubblockno) {
        this.ahlSubBlockNo = ahlsubblockno;
    }

    public String getSlnohhd() {
        return slnohhd;
    }

    public void setSlnohhd(String slnohhd) {
        this.slnohhd = slnohhd;
    }

    public String getSlnomember() {
        return slnomember;
    }

    public void setSlnomember(String slnomember) {
        this.slnomember = slnomember;
    }

    public String getAhlslnohhd() {
        return ahlSlNoHhd;
    }

    public void setAhlslnohhd(String ahlslnohhd) {
        this.ahlSlNoHhd = ahlslnohhd;
    }

    public String getAhltypeofeb() {
        return ahlTypeOfeb;
    }

    public void setAhltypeofeb(String ahltypeofeb) {
        this.ahlTypeOfeb = ahltypeofeb;
    }

    public String getTypeofhhd() {
        return typeOfHhd;
    }

    public void setTypeofhhd(String typeofhhd) {
        this.typeOfHhd = typeofhhd;
    }

    public String getLivinginshelter() {
        return livingInShelter;
    }

    public void setLivinginshelter(String livinginshelter) {
        this.livingInShelter = livinginshelter;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameSl() {
        return nameSl;
    }

    public void setNameSl(String nameSl) {
        this.nameSl = nameSl;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getRelationSl() {
        return relationSl;
    }

    public void setRelationSl(String relationSl) {
        this.relationSl = relationSl;
    }

    public String getGenderid() {
        return genderId;
    }

    public void setGenderid(String genderid) {
        this.genderId = genderid;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getMstatusid() {
        return mStatusId;
    }

    public void setMstatusid(String mstatusid) {
        this.mStatusId = mstatusid;
    }

    public String getFathername() {
        return fatherName;
    }

    public void setFathername(String fathername) {
        this.fatherName = fathername;
    }

    public String getFathernameSl() {
        return fatherNameSl;
    }

    public void setFathernameSl(String fathernameSl) {
        this.fatherNameSl = fathernameSl;
    }

    public String getMothername() {
        return motherName;
    }

    public void setMothername(String mothername) {
        this.motherName = mothername;
    }

    public String getMothernameSl() {
        return motherNameSl;
    }

    public void setMothernameSl(String mothernameSl) {
        this.motherNameSl = mothernameSl;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getOccupationSl() {
        return occupationSl;
    }

    public void setOccupationSl(String occupationSl) {
        this.occupationSl = occupationSl;
    }

    public String getEducode() {
        return educode;
    }

    public void setEducode(String educode) {
        this.educode = educode;
    }

    public String getEducationOther() {
        return educationOther;
    }

    public void setEducationOther(String educationOther) {
        this.educationOther = educationOther;
    }

    public String getDisabilitycode() {
        return disabilitycode;
    }

    public void setDisabilitycode(String disabilitycode) {
        this.disabilitycode = disabilitycode;
    }

    public String getCasteGroup() {
        return casteGroup;
    }

    public void setCasteGroup(String casteGroup) {
        this.casteGroup = casteGroup;
    }

    public String getAhlTin() {
        return ahlTin;
    }

    public void setAhlTin(String ahlTin) {
        this.ahlTin = ahlTin;
    }

    public String getAddressline1() {
        return addressLine1;
    }

    public void setAddressline1(String addressline1) {
        this.addressLine1 = addressline1;
    }

    public String getAddressline2() {
        return addressLine2;
    }

    public void setAddressline2(String addressline2) {
        this.addressLine2 = addressline2;
    }

    public String getAddressline3() {
        return addressLine3;
    }

    public void setAddressline3(String addressline3) {
        this.addressLine3 = addressline3;
    }

    public String getAddressline4() {
        return addressLine4;
    }

    public void setAddressline4(String addressline4) {
        this.addressLine4 = addressline4;
    }

    public String getAddressline5() {
        return addressLine5;
    }

    public void setAddressline5(String addressline5) {
        this.addressLine5 = addressline5;
    }

    public String getPincode() {
        return pinCode;
    }

    public void setPincode(String pincode) {
        this.pinCode = pincode;
    }

    public String getAddressline1Sl() {
        return addressLine1Sl;
    }

    public void setAddressline1Sl(String addressline1Sl) {
        this.addressLine1Sl = addressline1Sl;
    }

    public String getAddressline2Sl() {
        return addressLine2sl;
    }

    public void setAddressline2Sl(String addressline2Sl) {
        this.addressLine2sl = addressline2Sl;
    }

    public String getAddressline3Sl() {
        return addressLine3Sl;
    }

    public void setAddressline3Sl(String addressline3Sl) {
        this.addressLine3Sl = addressline3Sl;
    }

    public String getAddressline4Sl() {
        return addressLine4sl;
    }

    public void setAddressline4Sl(String addressline4Sl) {
        this.addressLine4sl = addressline4Sl;
    }

    public String getAddressline5Sl() {
        return addressLine5sl;
    }

    public void setAddressline5Sl(String addressline5Sl) {
        this.addressLine5sl = addressline5Sl;
    }

    public String getDobFrmNpr() {
        return dobFrmNpr;
    }

    public void setDobFrmNpr(String dobFrmNpr) {
        this.dobFrmNpr = dobFrmNpr;
    }

    public String getSpousenm() {
        return spousenm;
    }

    public void setSpousenm(String spousenm) {
        this.spousenm = spousenm;
    }

    public String getHhdNo() {
        return hhdNo;
    }

    public void setHhdNo(String hhdNo) {
        this.hhdNo = hhdNo;
    }

    public String getAadhaarNo() {
        return aadhaarNo;
    }

    public void setAadhaarNo(String aadhaarNo) {
        this.aadhaarNo = aadhaarNo;
    }

    public String getNameAadhaar() {
        return nameAadhaar;
    }

    public void setNameAadhaar(String nameAadhaar) {
        this.nameAadhaar = nameAadhaar;
    }

    public String getUrnNo() {
        return urnNo;
    }

    public void setUrnNo(String urnNo) {
        this.urnNo = urnNo;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getRuralUrban() {
        return ruralUrban;
    }

    public void setRuralUrban(String ruralUrban) {
        this.ruralUrban = ruralUrban;
    }

    public String getBankIfsc() {
        return bankIfsc;
    }

    public void setBankIfsc(String bankIfsc) {
        this.bankIfsc = bankIfsc;
    }

    public String getMarkForDeletion() {
        return markForDeletion;
    }

    public void setMarkForDeletion(String markForDeletion) {
        this.markForDeletion = markForDeletion;
    }

    public String getAadhaarStatus() {
        return aadhaarStatus;
    }

    public void setAadhaarStatus(String aadhaarStatus) {
        this.aadhaarStatus = aadhaarStatus;
    }

    public String getHhStatus() {
        return hhStatus;
    }

    public void setHhStatus(String hhStatus) {
        this.hhStatus = hhStatus;
    }

    public String getMemStatus() {
        return memStatus;
    }

    public void setMemStatus(String memStatus) {
        this.memStatus = memStatus;
    }

    public String getEid() {
        return eid;
    }

    public void setEid(String eid) {
        this.eid = eid;
    }

    public String getBankAccNo() {
        return bankAccNo;
    }

    public void setBankAccNo(String bankAccNo) {
        this.bankAccNo = bankAccNo;
    }
/*
    public String getSchemeId() {
        return schemeId;
    }

    public void setSchemeId(String schemeId) {
        this.schemeId = schemeId;
    }

    public String getSchemeNo() {
        return schemeNo;
    }

    public void setSchemeNo(String schemeNo) {
        this.schemeNo = schemeNo;
    }*/

    public String getNameNominee() {
        return nameNominee;
    }

    public void setNameNominee(String nameNominee) {
        this.nameNominee = nameNominee;
    }

    public String getRelationNomineeCode() {
        return relationNomineeCode;
    }

    public void setRelationNomineeCode(String relationNomineeCode) {
        this.relationNomineeCode = relationNomineeCode;
    }

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getNameAsId() {
        return nameAsId;
    }

    public void setNameAsId(String nameAsId) {
        this.nameAsId = nameAsId;
    }

    public String getIdPhoto1() {
        return idPhoto1;
    }

    public void setIdPhoto1(String idPhoto1) {
        this.idPhoto1 = idPhoto1;
    }

    public String getIdPhoto2() {
        return idPhoto2;
    }

    public void setIdPhoto2(String idPhoto2) {
        this.idPhoto2 = idPhoto2;
    }

    public String getMemberPhoto() {
        return memberPhoto;
    }

    public void setMemberPhoto(String memberPhoto) {
        this.memberPhoto = memberPhoto;
    }

    public String getAadhaarCapturingMode() {
        return aadhaarCapturingMode;
    }

    public void setAadhaarCapturingMode(String aadhaarCapturingMode) {
        this.aadhaarCapturingMode = aadhaarCapturingMode;
    }

    public String getConsent() {
        return consent;
    }

    public void setConsent(String consent) {
        this.consent = consent;
    }

    public String getAadhaarAuthMode() {
        return aadhaarAuthMode;
    }

    public void setAadhaarAuthMode(String aadhaarAuthMode) {
        this.aadhaarAuthMode = aadhaarAuthMode;
    }

    public String getAadhaarAuthDt() {
        return aadhaarAuthDt;
    }

    public void setAadhaarAuthDt(String aadhaarAuthDt) {
        this.aadhaarAuthDt = aadhaarAuthDt;
    }

    public String getAadhaarVerifiedBy() {
        return aadhaarVerifiedBy;
    }

    public void setAadhaarVerifiedBy(String aadhaarVerifiedBy) {
        this.aadhaarVerifiedBy = aadhaarVerifiedBy;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(String dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public String getAadhaarAuth() {
        return aadhaarAuth;
    }

    public void setAadhaarAuth(String aadhaarAuth) {
        this.aadhaarAuth = aadhaarAuth;
    }

    public String getMobileAuth() {
        return mobileAuth;
    }

    public void setMobileAuth(String mobileAuth) {
        this.mobileAuth = mobileAuth;
    }

    public String getUrnAuth() {
        return urnAuth;
    }

    public void setUrnAuth(String urnAuth) {
        this.urnAuth = urnAuth;
    }

    public String getIfscAuth() {
        return ifscAuth;
    }

    public void setIfscAuth(String ifscAuth) {
        this.ifscAuth = ifscAuth;
    }

    public String getBankAccAuth() {
        return bankAccAuth;
    }

    public void setBankAccAuth(String bankAccAuth) {
        this.bankAccAuth = bankAccAuth;
    }

    public String getStateSchemeCodeAuth() {
        return stateSchemeCodeAuth;
    }

    public void setStateSchemeCodeAuth(String stateSchemeCodeAuth) {
        this.stateSchemeCodeAuth = stateSchemeCodeAuth;
    }

    public String getSchemeCode() {
        return schemeCode;
    }

    public void setSchemeCode(String schemeCode) {
        this.schemeCode = schemeCode;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public String getNhpsId() {
        return nhpsId;
    }

    public void setNhpsId(String nhpsId) {
        this.nhpsId = nhpsId;
    }

    public String getMobileAuthDt() {
        return mobileAuthDt;
    }

    public void setMobileAuthDt(String mobileAuthDt) {
        this.mobileAuthDt = mobileAuthDt;
    }

    public String getUrnAuthDt() {
        return urnAuthDt;
    }

    public void setUrnAuthDt(String urnAuthDt) {
        this.urnAuthDt = urnAuthDt;
    }

    public String getIfscAuthDt() {
        return ifscAuthDt;
    }

    public void setIfscAuthDt(String ifscAuthDt) {
        this.ifscAuthDt = ifscAuthDt;
    }

    public String getBankAccAuthDt() {
        return bankAccAuthDt;
    }

    public void setBankAccAuthDt(String bankAccAuthDt) {
        this.bankAccAuthDt = bankAccAuthDt;
    }

    public String getStateSchemeCodeAuthDt() {
        return stateSchemeCodeAuthDt;
    }

    public void setStateSchemeCodeAuthDt(String stateSchemeCodeAuthDt) {
        this.stateSchemeCodeAuthDt = stateSchemeCodeAuthDt;
    }

    public String getConsentDt() {
        return consentDt;
    }

    public void setConsentDt(String consentDt) {
        this.consentDt = consentDt;
    }

    public String getAadhaarGender() {
        return aadhaarGender;
    }

    public void setAadhaarGender(String aadhaarGender) {
        this.aadhaarGender = aadhaarGender;
    }

    public String getAadhaarYob() {
        return aadhaarYob;
    }

    public void setAadhaarYob(String aadhaarYob) {
        this.aadhaarYob = aadhaarYob;
    }

    public String getAadhaarDob() {
        return aadhaarDob;
    }

    public void setAadhaarDob(String aadhaarDob) {
        this.aadhaarDob = aadhaarDob;
    }

    public String getAadhaarCo() {
        return aadhaarCo;
    }

    public void setAadhaarCo(String aadhaarCo) {
        this.aadhaarCo = aadhaarCo;
    }

    public String getAadhaarGname() {
        return aadhaarGname;
    }

    public void setAadhaarGname(String aadhaarGname) {
        this.aadhaarGname = aadhaarGname;
    }

    public String getAadhaarHouse() {
        return aadhaarHouse;
    }

    public void setAadhaarHouse(String aadhaarHouse) {
        this.aadhaarHouse = aadhaarHouse;
    }

    public String getAadhaarStreet() {
        return aadhaarStreet;
    }

    public void setAadhaarStreet(String aadhaarStreet) {
        this.aadhaarStreet = aadhaarStreet;
    }

    public String getAadhaarLoc() {
        return aadhaarLoc;
    }

    public void setAadhaarLoc(String aadhaarLoc) {
        this.aadhaarLoc = aadhaarLoc;
    }

    public String getAadhaarVtc() {
        return aadhaarVtc;
    }

    public void setAadhaarVtc(String aadhaarVtc) {
        this.aadhaarVtc = aadhaarVtc;
    }

    public String getAadhaarPo() {
        return aadhaarPo;
    }

    public void setAadhaarPo(String aadhaarPo) {
        this.aadhaarPo = aadhaarPo;
    }

    public String getAadhaarDist() {
        return aadhaarDist;
    }

    public void setAadhaarDist(String aadhaarDist) {
        this.aadhaarDist = aadhaarDist;
    }

    public String getAadhaarSubdist() {
        return aadhaarSubdist;
    }

    public void setAadhaarSubdist(String aadhaarSubdist) {
        this.aadhaarSubdist = aadhaarSubdist;
    }

    public String getAadhaarState() {
        return aadhaarState;
    }

    public void setAadhaarState(String aadhaarState) {
        this.aadhaarState = aadhaarState;
    }

    public String getAadhaarPc() {
        return aadhaarPc;
    }

    public void setAadhaarPc(String aadhaarPc) {
        this.aadhaarPc = aadhaarPc;
    }

    public String getAadhaarLm() {
        return aadhaarLm;
    }

    public void setAadhaarLm(String aadhaarLm) {
        this.aadhaarLm = aadhaarLm;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getWhoseMobile() {
        return whoseMobile;
    }

    public void setWhoseMobile(String whoseMobile) {
        this.whoseMobile = whoseMobile;
    }

    public String getConsentPhoto() {
        return consentPhoto;
    }

    public void setConsentPhoto(String consentPhoto) {
        this.consentPhoto = consentPhoto;
    }

    public String getRsbyMemId() {
        return rsbyMemId;
    }

    public void setRsbyMemId(String rsbyMemId) {
        this.rsbyMemId = rsbyMemId;
    }

    public String getRsbyUrnId() {
        return rsbyUrnId;
    }

    public void setRsbyUrnId(String rsbyUrnId) {
        this.rsbyUrnId = rsbyUrnId;
    }

    public String getRsbyIssuesTimespam() {
        return rsbyIssuesTimespam;
    }

    public void setRsbyIssuesTimespam(String rsbyIssuesTimespam) {
        this.rsbyIssuesTimespam = rsbyIssuesTimespam;
    }

    public String getRsbyHofnamereg() {
        return rsbyHofnamereg;
    }

    public void setRsbyHofnamereg(String rsbyHofnamereg) {
        this.rsbyHofnamereg = rsbyHofnamereg;
    }

    public String getRsbyDoorhouse() {
        return rsbyDoorhouse;
    }

    public void setRsbyDoorhouse(String rsbyDoorhouse) {
        this.rsbyDoorhouse = rsbyDoorhouse;
    }

    public String getRsbyVillageCode() {
        return rsbyVillageCode;
    }

    public void setRsbyVillageCode(String rsbyVillageCode) {
        this.rsbyVillageCode = rsbyVillageCode;
    }

    public String getRsbyPanchyatTownCode() {
        return rsbyPanchyatTownCode;
    }

    public void setRsbyPanchyatTownCode(String rsbyPanchyatTownCode) {
        this.rsbyPanchyatTownCode = rsbyPanchyatTownCode;
    }

    public String getRsbyBlockCode() {
        return rsbyBlockCode;
    }

    public void setRsbyBlockCode(String rsbyBlockCode) {
        this.rsbyBlockCode = rsbyBlockCode;
    }

    public String getRsbyDistrictCode() {
        return rsbyDistrictCode;
    }

    public void setRsbyDistrictCode(String rsbyDistrictCode) {
        this.rsbyDistrictCode = rsbyDistrictCode;
    }

    public String getRsbyStateCode() {
        return rsbyStateCode;
    }

    public void setRsbyStateCode(String rsbyStateCode) {
        this.rsbyStateCode = rsbyStateCode;
    }


    public String getRsbyName() {
        return rsbyName;
    }

    public void setRsbyName(String rsbyName) {
        this.rsbyName = rsbyName;
    }

    public String getRsbyDob() {
        return rsbyDob;
    }

    public void setRsbyDob(String rsbyDob) {
        this.rsbyDob = rsbyDob;
    }

    public String getRsbyGender() {
        return rsbyGender;
    }

    public void setRsbyGender(String rsbyGender) {
        this.rsbyGender = rsbyGender;
    }

    public String getRsbyRelcode() {
        return rsbyRelcode;
    }

    public void setRsbyRelcode(String rsbyRelcode) {
        this.rsbyRelcode = rsbyRelcode;
    }

    public String getRsbyInsccode() {
        return rsbyInsccode;
    }

    public void setRsbyInsccode(String rsbyInsccode) {
        this.rsbyInsccode = rsbyInsccode;
    }

    public String getRsbyPolicyno() {
        return rsbyPolicyno;
    }

    public void setRsbyPolicyno(String rsbyPolicyno) {
        this.rsbyPolicyno = rsbyPolicyno;
    }

    public String getRsbyPolicyamt() {
        return rsbyPolicyamt;
    }

    public void setRsbyPolicyamt(String rsbyPolicyamt) {
        this.rsbyPolicyamt = rsbyPolicyamt;
    }

    public String getRsbyStartdate() {
        return rsbyStartdate;
    }

    public void setRsbyStartdate(String rsbyStartdate) {
        this.rsbyStartdate = rsbyStartdate;
    }

    public String getRsbyEnddate() {
        return rsbyEnddate;
    }

    public void setRsbyEnddate(String rsbyEnddate) {
        this.rsbyEnddate = rsbyEnddate;
    }

    public String getRsbyCsmNo() {
        return rsbyCsmNo;
    }

    public void setRsbyCsmNo(String rsbyCsmNo) {
        this.rsbyCsmNo = rsbyCsmNo;
    }

    public String getRsbyCardType() {
        return rsbyCardType;
    }

    public void setRsbyCardType(String rsbyCardType) {
        this.rsbyCardType = rsbyCardType;
    }

    public String getRsbyCardCategory() {
        return rsbyCardCategory;
    }

    public void setRsbyCardCategory(String rsbyCardCategory) {
        this.rsbyCardCategory = rsbyCardCategory;
    }

    public String getPrintStatus() {
        return printStatus;
    }

    public void setPrintStatus(String printStatus) {
        this.printStatus = printStatus;
    }

    public String getValidatedBy() {
        return validatedBy;
    }

    public void setValidatedBy(String validatedBy) {
        this.validatedBy = validatedBy;
    }

    public String getSchemeId1() {
        return schemeId1;
    }

    public void setSchemeId1(String schemeId1) {
        this.schemeId1 = schemeId1;
    }

    public String getSchemeNo1() {
        return schemeNo1;
    }

    public void setSchemeNo1(String schemeNo1) {
        this.schemeNo1 = schemeNo1;
    }

    public String getSchemeId2() {
        return schemeId2;
    }

    public void setSchemeId2(String schemeId2) {
        this.schemeId2 = schemeId2;
    }

    public String getSchemeNo2() {
        return schemeNo2;
    }

    public void setSchemeNo2(String schemeNo2) {
        this.schemeNo2 = schemeNo2;
    }

    public String getSchemeId3() {
        return schemeId3;
    }

    public void setSchemeId3(String schemeId3) {
        this.schemeId3 = schemeId3;
    }

    public String getSchemeNo3() {
        return schemeNo3;
    }

    public void setSchemeNo3(String schemeNo3) {
        this.schemeNo3 = schemeNo3;
    }

    public String getSchemeId() {
        return schemeId;
    }

    public void setSchemeId(String schemeId) {
        this.schemeId = schemeId;
    }

    public String getSchemeNo() {
        return schemeNo;
    }

    public void setSchemeNo(String schemeNo) {
        this.schemeNo = schemeNo;
    }

    public String getUrnId() {
        return urnId;
    }

    public void setUrnId(String urnId) {
        this.urnId = urnId;
    }

    public String getPanchayatTownCode() {
        return panchayatTownCode;
    }

    public void setPanchayatTownCode(String panchayatTownCode) {
        this.panchayatTownCode = panchayatTownCode;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getPolicyExpiryDate() {
        return policyExpiryDate;
    }

    public void setPolicyExpiryDate(String policyExpiryDate) {
        this.policyExpiryDate = policyExpiryDate;
    }

    public String getCardCategory() {
        return cardCategory;
    }

    public void setCardCategory(String cardCategory) {
        this.cardCategory = cardCategory;
    }

    public String getHofNameReg() {
        return hofNameReg;
    }

    public void setHofNameReg(String hofNameReg) {
        this.hofNameReg = hofNameReg;
    }

    public String getPolicyAmount() {
        return policyAmount;
    }

    public void setPolicyAmount(String policyAmount) {
        this.policyAmount = policyAmount;
    }

    public String getPolicyNo() {
        return policyNo;
    }

    public void setPolicyNo(String policyNo) {
        this.policyNo = policyNo;
    }

    public String getInscCode() {
        return inscCode;
    }

    public void setInscCode(String inscCode) {
        this.inscCode = inscCode;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getIssueTimeStamp() {
        return issueTimeStamp;
    }

    public void setIssueTimeStamp(String issueTimeStamp) {
        this.issueTimeStamp = issueTimeStamp;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getVillageCode() {
        return villageCode;
    }

    public void setVillageCode(String villageCode) {
        this.villageCode = villageCode;
    }

    public String getCsmNo() {
        return csmNo;
    }

    public void setCsmNo(String csmNo) {
        this.csmNo = csmNo;
    }
}
