package com.nhpm.Models.response.rsbyMembers;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by saurabh on 26-02-2017.
 urn id
 issuestimespam
 hofnamereg
 doorhouse
 villagecode
 panchyattowncode
 blockcode
 districtcode
 statecode
 memid
 name
 dob
 gender
 relcode
 */
public class RSBYItem implements Serializable {
    private String rsbyMemId;
    private String urnId;
    private String issuesTimespam;
    private String hofnamereg;
    private String doorhouse;
    private String villageCode;
    private String panchyatTownCode;
    private String blockCode;
    private String districtCode;
    private String stateCode;
    private String memid;
    private String name;
    private String dob;
    private String gender;
    private String relcode;
    private String insccode;
    private String policyno;
    private String policyamt;
    private String startdate;
    private String enddate;
    // added by saurabh
    private String nhpsMemId;
    private String hhdNo;
    private String aadhaarNo;
    private String nameAadhaar;
    private String urnNo;
    private String mobileNo;
    private String ruralUrban;
    private String bankIfsc;
    private String markForDeletion;
    private String aadhaarStatus;

    private String memStatus;
    private String eid;
    private String bankAccNo;
    private String schemeId;
    private String schemeNo;
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
    private String govtIdSurveyedStat;
    private String aadhaarSurveyedStat;
    private String photoSurveyedStatus;
    private String mobileNoSurveyedStat;
    private String nomineeDetailSurveyedStat;
    private String syncedStatus;
    private String syncDt;
    private String error_code;
    private String error_msg;
    private String error_type;
    private String appVersion;
    private String lockedSave;
    private String hhStatus;
    private String vl_aadharNo;
    private String vl_stateCode;
    private String vl_districtCode ;
    private String vl_tehsilCode;
    private String vl_vtCode ;
    private String vl_wardCode;
    private String vl_blockCode;
    private String vl_subBlockcode;
    private String vl_ruralUrban;
    private String csmNo;
    private String cardType;
    private String cardCategory;
//    private String reqName,reqFatherName,reqMotherName,reqRelationCode,reqRelationName,reqMarritalStatCode,reqGenderCode,reqDOB,reqOccupation;


    static public RSBYItem create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, RSBYItem.class);
    }

    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public RSBYItem() {
    }


    public String getRsbyMemId() {
        return rsbyMemId;
    }

    public void setRsbyMemId(String rsbyMemId) {
        this.rsbyMemId = rsbyMemId;
    }

    public String getUrnId() {
        return urnId;
    }

    public void setUrnId(String urnId) {
        this.urnId = urnId;
    }

    public String getIssuesTimespam() {
        return issuesTimespam;
    }

    public void setIssuesTimespam(String issuesTimespam) {
        this.issuesTimespam = issuesTimespam;
    }

    public String getHofnamereg() {
        return hofnamereg;
    }

    public void setHofnamereg(String hofnamereg) {
        this.hofnamereg = hofnamereg;
    }

    public String getDoorhouse() {
        return doorhouse;
    }

    public void setDoorhouse(String doorhouse) {
        this.doorhouse = doorhouse;
    }

    public String getVillageCode() {
        return villageCode;
    }

    public void setVillageCode(String villageCode) {
        this.villageCode = villageCode;
    }

    public String getPanchyatTownCode() {
        return panchyatTownCode;
    }

    public void setPanchyatTownCode(String panchyatTownCode) {
        this.panchyatTownCode = panchyatTownCode;
    }

    public String getBlockCode() {
        return blockCode;
    }

    public void setBlockCode(String blockCode) {
        this.blockCode = blockCode;
    }

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getMemid() {
        return memid;
    }

    public void setMemid(String memid) {
        this.memid = memid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getRelcode() {
        return relcode;
    }

    public void setRelcode(String relcode) {
        this.relcode = relcode;
    }

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

    public String getMemberPhoto1() {
        return memberPhoto1;
    }

    public void setMemberPhoto1(String memberPhoto1) {
        this.memberPhoto1 = memberPhoto1;
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

    public String getNomineeRelationName() {
        return nomineeRelationName;
    }

    public void setNomineeRelationName(String nomineeRelationName) {
        this.nomineeRelationName = nomineeRelationName;
    }

    public String getGovtIdPhoto() {
        return govtIdPhoto;
    }

    public void setGovtIdPhoto(String govtIdPhoto) {
        this.govtIdPhoto = govtIdPhoto;
    }

    public String getLockedSave() {
        return lockedSave;
    }

    public void setLockedSave(String lockedSave) {
        this.lockedSave = lockedSave;
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

    public String getSyncedStatus() {
        return syncedStatus;
    }

    public void setSyncedStatus(String syncedStatus) {
        this.syncedStatus = syncedStatus;
    }

    public String getSyncDt() {
        return syncDt;
    }

    public void setSyncDt(String syncDt) {
        this.syncDt = syncDt;
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

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getInsccode() {
        return insccode;
    }

    public void setInsccode(String insccode) {
        this.insccode = insccode;
    }

    public String getPolicyamt() {
        return policyamt;
    }

    public void setPolicyamt(String policyamt) {
        this.policyamt = policyamt;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public String getPolicyno() {
        return policyno;
    }

    public void setPolicyno(String policyno) {
        this.policyno = policyno;
    }

    public String getVl_aadharNo() {
        return vl_aadharNo;
    }

    public void setVl_aadharNo(String vl_aadharNo) {
        this.vl_aadharNo = vl_aadharNo;
    }

    public String getVl_stateCode() {
        return vl_stateCode;
    }

    public void setVl_stateCode(String vl_stateCode) {
        this.vl_stateCode = vl_stateCode;
    }

    public String getVl_districtCode() {
        return vl_districtCode;
    }

    public void setVl_districtCode(String vl_districtCode) {
        this.vl_districtCode = vl_districtCode;
    }

    public String getVl_tehsilCode() {
        return vl_tehsilCode;
    }

    public void setVl_tehsilCode(String vl_tehsilCode) {
        this.vl_tehsilCode = vl_tehsilCode;
    }

    public String getVl_vtCode() {
        return vl_vtCode;
    }

    public void setVl_vtCode(String vl_vtCode) {
        this.vl_vtCode = vl_vtCode;
    }

    public String getVl_wardCode() {
        return vl_wardCode;
    }

    public void setVl_wardCode(String vl_wardCode) {
        this.vl_wardCode = vl_wardCode;
    }

    public String getVl_blockCode() {
        return vl_blockCode;
    }

    public void setVl_blockCode(String vl_blockCode) {
        this.vl_blockCode = vl_blockCode;
    }

    public String getVl_subBlockcode() {
        return vl_subBlockcode;
    }

    public void setVl_subBlockcode(String vl_subBlockcode) {
        this.vl_subBlockcode = vl_subBlockcode;
    }

    public String getVl_ruralUrban() {
        return vl_ruralUrban;
    }

    public void setVl_ruralUrban(String vl_ruralUrban) {
        this.vl_ruralUrban = vl_ruralUrban;
    }

    public String getCsmNo() {
        return csmNo;
    }

    public void setCsmNo(String csmNo) {
        this.csmNo = csmNo;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCardCategory() {
        return cardCategory;
    }

    public void setCardCategory(String cardCategory) {
        this.cardCategory = cardCategory;
    }
}
