package com.nhpm.Models.response.rsbyMembers;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by PSQ on 3/7/2017.
 */

public class RsbyMemberSyncRequest implements Serializable {

    private String rsbymemid;
    private String memActiveStatus;
    private String printStatus;
    private String urnid;
    private String insccode;
    private String policyno;
    private String policyamt;
    private String startdate;
    private String enddate;
    private String issuestimespam;
    private String hofnamereg;
    private String doorhouse;
    private String villagecode;
    private String panchyattowncode;
    private String blockcode;
    private String districtcode;
    private String statecode;
    private String memid;
    private String name;
    private String dob;
    private String gender;
    private String relcode;
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
    private String nameNominee;
    private String relationNomineeCode;
    private String idType;
    private String idNo;
    private String nameAsId;
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
    private String nhpsRelationCode;
    private String nhpsRelationName;
    private String nomineeRelationName;
    private String govtIdPhoto;
    private String lockedSave;
    private String syncedStatus;
    private String nhpsMemId;
    private String appVersion;
    private String idPhoto1;
    private String idPhoto2;
    private String memberPhoto;
    private String memberPhoto1;
    private String consentPhoto;
    private String vlAadhaarNo;
    private String vlStateCode;
    private String vlDistrictCode;
    private String vlTehsilCode;
    private String vlVtCode;
    private String vlWardCode;
    private String vlBlockCode;
    private String vlSubBlockcode;
    private String vlRuralUrban;
    private String csmNo;
    private String cardType;
    private String cardCategory;

    static public RsbyMemberSyncRequest create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, RsbyMemberSyncRequest.class);
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

    public String getRsbymemid() {
        return rsbymemid;
    }

    public void setRsbymemid(String rsbymemid) {
        this.rsbymemid = rsbymemid;
    }

    public String getUrnid() {
        return urnid;
    }

    public void setUrnid(String urnid) {
        this.urnid = urnid;
    }

    public String getInsccode() {
        return insccode;
    }

    public void setInsccode(String insccode) {
        this.insccode = insccode;
    }

    public String getPolicyno() {
        return policyno;
    }

    public void setPolicyno(String policyno) {
        this.policyno = policyno;
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

    public String getIssuestimespam() {
        return issuestimespam;
    }

    public void setIssuestimespam(String issuestimespam) {
        this.issuestimespam = issuestimespam;
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

    public String getVillagecode() {
        return villagecode;
    }

    public void setVillagecode(String villagecode) {
        this.villagecode = villagecode;
    }

    public String getPanchyattowncode() {
        return panchyattowncode;
    }

    public void setPanchyattowncode(String panchyattowncode) {
        this.panchyattowncode = panchyattowncode;
    }

    public String getBlockcode() {
        return blockcode;
    }

    public void setBlockcode(String blockcode) {
        this.blockcode = blockcode;
    }

    public String getDistrictcode() {
        return districtcode;
    }

    public void setDistrictcode(String districtcode) {
        this.districtcode = districtcode;
    }

    public String getStatecode() {
        return statecode;
    }

    public void setStatecode(String statecode) {
        this.statecode = statecode;
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

    public String getSyncedStatus() {
        return syncedStatus;
    }

    public void setSyncedStatus(String syncedStatus) {
        this.syncedStatus = syncedStatus;
    }

    public String getNhpsMemId() {
        return nhpsMemId;
    }

    public void setNhpsMemId(String nhpsMemId) {
        this.nhpsMemId = nhpsMemId;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getVlAadharNo() {
        return vlAadhaarNo;
    }

    public void setVlAadharNo(String vlAadharNo) {
        this.vlAadhaarNo = vlAadharNo;
    }

    public String getVlStateCode() {
        return vlStateCode;
    }

    public void setVlStateCode(String vlStateCode) {
        this.vlStateCode = vlStateCode;
    }

    public String getVlDistrictCode() {
        return vlDistrictCode;
    }

    public void setVlDistrictCode(String vlDistrictCode) {
        this.vlDistrictCode = vlDistrictCode;
    }

    public String getVlTehsilCode() {
        return vlTehsilCode;
    }

    public void setVlTehsilCode(String vlTehsilCode) {
        this.vlTehsilCode = vlTehsilCode;
    }

    public String getVlVtCode() {
        return vlVtCode;
    }

    public void setVlVtCode(String vlVtCode) {
        this.vlVtCode = vlVtCode;
    }

    public String getVlWardCode() {
        return vlWardCode;
    }

    public void setVlWardCode(String vlWardCode) {
        this.vlWardCode = vlWardCode;
    }

    public String getVlBlockCode() {
        return vlBlockCode;
    }

    public void setVlBlockCode(String vlBlockCode) {
        this.vlBlockCode = vlBlockCode;
    }

    public String getVlSubBlockcode() {
        return vlSubBlockcode;
    }

    public void setVlSubBlockcode(String vlSubBlockcode) {
        this.vlSubBlockcode = vlSubBlockcode;
    }

    public String getVlRuralUrban() {
        return vlRuralUrban;
    }

    public void setVlRuralUrban(String vlRuralUrban) {
        this.vlRuralUrban = vlRuralUrban;
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

    public String getConsentPhoto() {
        return consentPhoto;
    }

    public void setConsentPhoto(String consentPhoto) {
        this.consentPhoto = consentPhoto;
    }

    public String getMemActiveStatus() {
        return memActiveStatus;
    }

    public void setMemActiveStatus(String memActiveStatus) {
        this.memActiveStatus = memActiveStatus;
    }

    public String getPrintStatus() {
        return printStatus;
    }

    public void setPrintStatus(String printStatus) {
        this.printStatus = printStatus;
    }

    public String getVlAadhaarNo() {
        return vlAadhaarNo;
    }

    public void setVlAadhaarNo(String vlAadhaarNo) {
        this.vlAadhaarNo = vlAadhaarNo;
    }
}
