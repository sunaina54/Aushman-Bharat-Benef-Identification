package com.nhpm.Models.response.rsbyMembers;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by PSQ on 2/27/2017.
 */

public class RsbyHouseholdItem implements Serializable {
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
    private String insccode;
    private String policyamt;
    private String policyno;
    private String startdate;
    private String enddate;
    private String memid;
    private String name;
    private String dob;
    private String gender;
    private String relcode;
    private String syncedStatus;
    private String syncDt;
    private String error_code;
    private String error_msg;
    private String error_type;
    private String appVersion;
    private String lockedSave;
    private String hhStatus;
    private String nhpsId;
    private String vlAadharNo;
    private String vlStateCode;
    private String  vlDistrictCode;
    private String vlTehsilCode;
    private String vlVtCode;
    private String vlWardCode;
    private String vlBlockCode;
    private String vlSubBlockcode;
    private String vlRuralUrban;
    private String csmNo;
    private String cardType;
    private String cardCategory;
    private String hofImage;

    static public RsbyHouseholdItem create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, RsbyHouseholdItem.class);
    }

    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public String getNhpsId() {
        return nhpsId;
    }

    public void setNhpsId(String nhpsId) {
        this.nhpsId = nhpsId;
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

    public String getLockedSave() {
        return lockedSave;
    }

    public void setLockedSave(String lockedSave) {
        this.lockedSave = lockedSave;
    }

    public String getHhStatus() {
        return hhStatus;
    }

    public void setHhStatus(String hhStatus) {
        this.hhStatus = hhStatus;
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

    public String getVlAadharNo() {
        return vlAadharNo;
    }

    public void setVlAadharNo(String vlAadharNo) {
        this.vlAadharNo = vlAadharNo;
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

    public String getHofImage() {
        return hofImage;
    }

    public void setHofImage(String hofImage) {
        this.hofImage = hofImage;
    }
}
