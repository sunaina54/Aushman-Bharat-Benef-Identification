package com.nhpm.Models.response.rsbyMembers;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by PSQ on 3/7/2017.
 */

public class RsbySyncHouseholdRequest implements Serializable {
    private String rsbymemid;
    private String urnid;
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
    private String insccode;
    private String policyno;
    private String policyamt;
    private String startdate;
    private String enddate;
    private String hhStatus;
    private String syncedStatus;
    private String lockedSave;
    private String nhpsId;
    private String appVersion;
    private String vlAadhaarNo;
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
    private String rsbyFamilyPhoto;
    private String dataSource;

    public String getRsbyFamilyPhoto() {
        return rsbyFamilyPhoto;
    }

    public void setRsbyFamilyPhoto(String rsbyFamilyPhoto) {
        this.rsbyFamilyPhoto = rsbyFamilyPhoto;
    }

    static public RsbySyncHouseholdRequest create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, RsbySyncHouseholdRequest.class);
    }

    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
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

    public String getHofImage() {
        return hofImage;
    }

    public void setHofImage(String hofImage) {
        this.hofImage = hofImage;
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

    public String getHhStatus() {
        return hhStatus;
    }

    public void setHhStatus(String hhStatus) {
        this.hhStatus = hhStatus;
    }

    public String getSyncedStatus() {
        return syncedStatus;
    }

    public void setSyncedStatus(String syncedStatus) {
        this.syncedStatus = syncedStatus;
    }

    public String getLockedSave() {
        return lockedSave;
    }

    public void setLockedSave(String lockedSave) {
        this.lockedSave = lockedSave;
    }

    public String getNhpsId() {
        return nhpsId;
    }

    public void setNhpsId(String nhpsId) {
        this.nhpsId = nhpsId;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }
}
