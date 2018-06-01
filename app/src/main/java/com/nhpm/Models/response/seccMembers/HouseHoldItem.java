package com.nhpm.Models.response.seccMembers;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Anand on 03-11-2016.
 */
public class HouseHoldItem implements Serializable {
    private String nhpsMemId;
    private String ahlTin;
    private String hhdNo;
    private String slNoMember;
    private String slNoHd;
    private String ahlSlNoHhd;
    private String name;
    private String nameSl;
    private String fatherName;
    private String fatherNameSl;
    private String relation;
    private String relationSl;
    private String genderId;
    private String validatedBy;
    private String dob;
    private String mStatusId;
    private String motherName;
    private String motherNameSl;
    private String occupation;
    private String occupationSl;
    private String casteGroup;
    private String stateCode;
    private String districtCode;
    private String tehsilCode;
    private String townCode;
    private String wardId;
    private String ahlBlockNo;
    private String typeofhhd;
    private String hhdLandownedcodes;
    private String totalirrigated;
    private String hhdIrriEquip;
    private String totalunirrigated;
    private String otherirrigated;
    private String hhdAssetMveh;
    private String hhdMagriEquip;
    private String hhdKcc;
    private String hhdEmpSector;
    private String hhdEmpErg;
    private String addressLine1;
    private String addressLine2;
    private String addressLine3;
    private String addressLine4;
    private String addressLine5;
    private String pinCode;
    private String addressline1Sl;
    private String addressline2Sl;
    private String addressline3Sl;
    private String addressline4Sl;
    private String addressline5Sl;
    private String hhdEmpHem;
    private String hhdEmpPit;
    private String housingWalltype;
    private String housingRooftype;
    private String housingNoOfRooms;
    private String hhdAssetRef;
    private String hhdAssetTelmob;
    private String livinginshelter;
    private String hhdEmpOther;
    private String hhdMs;
    private String hhdPtg;
    private String hhdLrbl;
    private String ruralUrban;
    private String ahlSubBlockNo;
    private String nhpsId;
    private String mddsStc;
    private String mddsDtc;
    private String mddsSdtc;
    private String mddsPlcn;
    private String hhStatus;
    private String lockSave;
    private String syncedStatus;
    private String syncDt;
    private ErrorItem errorItem;
    private String error_code;
    private String error_msg;
    private String error_type;
    private String appVersion;
    private String urn_no;
    private String urnId;
    private String rsbyMemId;
    private String rsbyMemid;
    private String rsbyUrnId;
    private String issueDate;
    private String rsbyHofnamereg;
    private String rsbyDoorhouse;
    private String rsbyVillageCode;
    private String panchayatTownCode;
    private String rsbyBlockCode;
    private String rsbyDistrictCode;
    private String rsbyStateCode;
    private String rsbyName;
    private String rsbyDob;
    private String rsbyGender;
    private String rsbyRelcode;
    private String inssCode;
    private String policyNo;
    private String policyAmount;
    private String startDate;
    private String endDate;
    private String csmNo;
    private String cardType;
    private String cardCategory;
    private String dataSource;
    private String rsbyHouseholdPhoto;


    public String getRsbyFamilyPhoto() {
        return rsbyHouseholdPhoto;
    }

    public void setRsbyFamilyPhoto(String rsbyFamilyPhoto) {
        this.rsbyHouseholdPhoto = rsbyFamilyPhoto;
    }

    public String getRsbyMemId() {
        return rsbyMemId;
    }

    public void setRsbyMemId(String rsbyMemId) {
        this.rsbyMemId = rsbyMemId;
    }

    public String getRsbyMemid() {
        return rsbyMemid;
    }

    public void setRsbyMemid(String rsbyMemid) {
        this.rsbyMemid = rsbyMemid;
    }

    public String getRsbyUrnId() {
        return rsbyUrnId;
    }

    public void setRsbyUrnId(String rsbyUrnId) {
        this.rsbyUrnId = rsbyUrnId;
    }//rsby_urnIdrsby_urnId

    public String getRsbyIssuesTimespam() {
        return issueDate;
    }

    public void setRsbyIssuesTimespam(String rsbyIssuesTimespam) {
        this.issueDate = rsbyIssuesTimespam;
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
        return panchayatTownCode;
    }

    public void setRsbyPanchyatTownCode(String rsbyPanchyatTownCode) {
        this.panchayatTownCode = rsbyPanchyatTownCode;
    }

    public String getAhlSubBlockNo() {
        return ahlSubBlockNo;
    }

    public void setAhlSubBlockNo(String ahlSubBlockNo) {
        this.ahlSubBlockNo = ahlSubBlockNo;
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
        return inssCode;
    }

    public void setRsbyInsccode(String rsbyInsccode) {
        this.inssCode = rsbyInsccode;
    }

    public String getRsbyPolicyno() {
        return policyNo;
    }

    public void setRsbyPolicyno(String rsbyPolicyno) {
        this.policyNo = rsbyPolicyno;
    }

    public String getRsbyPolicyamt() {
        return policyAmount;
    }

    public void setRsbyPolicyamt(String rsbyPolicyamt) {
        this.policyAmount = rsbyPolicyamt;
    }

    public String getRsbyStartdate() {
        return startDate;
    }

    public void setRsbyStartdate(String rsbyStartdate) {
        this.startDate = rsbyStartdate;
    }

    public String getRsbyEnddate() {
        return endDate;
    }

    public void setRsbyEnddate(String rsbyEnddate) {
        this.endDate = rsbyEnddate;
    }

    public String getRsbyCsmNo() {
        return csmNo;
    }

    public void setRsbyCsmNo(String rsbyCsmNo) {
        this.csmNo = rsbyCsmNo;
    }

    public String getRsbyCardType() {
        return cardType;
    }

    public void setRsbyCardType(String rsbyCardType) {
        this.cardType = rsbyCardType;
    }

    public String getRsbyCardCategory() {
        return cardCategory;
    }

    public void setRsbyCardCategory(String rsbyCardCategory) {
        this.cardCategory = rsbyCardCategory;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
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

    public String getNhpsMemId() {
        return nhpsMemId;
    }

    public void setNhpsMemId(String nhpsMemId) {
        this.nhpsMemId = nhpsMemId;
    }

    public String getSyncDt() {
        return syncDt;
    }

    public void setSyncDt(String syncDt) {
        this.syncDt = syncDt;
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
        return addressline1Sl;
    }

    public void setAddressline1Sl(String addressline1Sl) {
        this.addressline1Sl = addressline1Sl;
    }

    public String getAddressline2Sl() {
        return addressline2Sl;
    }

    public void setAddressline2Sl(String addressline2Sl) {
        this.addressline2Sl = addressline2Sl;
    }

    public String getAddressline3Sl() {
        return addressline3Sl;
    }

    public void setAddressline3Sl(String addressline3Sl) {
        this.addressline3Sl = addressline3Sl;
    }

    public String getAddressline4Sl() {
        return addressline4Sl;
    }

    public void setAddressline4Sl(String addressline4Sl) {
        this.addressline4Sl = addressline4Sl;
    }

    public String getAddressline5Sl() {
        return addressline5Sl;
    }

    public void setAddressline5Sl(String addressline5Sl) {
        this.addressline5Sl = addressline5Sl;
    }

    static public HouseHoldItem create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, HouseHoldItem.class);
    }

    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public ErrorItem getErrorItem() {
        return errorItem;
    }

    public void setErrorItem(ErrorItem errorItem) {
        this.errorItem = errorItem;
    }

    public HouseHoldItem() {
    }

    public String getNhpsId() {
        return nhpsId;
    }

    public void setNhpsId(String nhpsId) {
        this.nhpsId = nhpsId;
    }

    public String getSyncedStatus() {
        return syncedStatus;
    }

    public void setSyncedStatus(String syncedStatus) {
        this.syncedStatus = syncedStatus;
    }

    public String getLockSave() {
        return lockSave;
    }

    public void setLockSave(String lockSave) {
        this.lockSave = lockSave;
    }

    public String getHhStatus() {
        return hhStatus;
    }

    public void setHhStatus(String hhStatus) {
        this.hhStatus = hhStatus;
    }


    public String getAhlTin() {
        return ahlTin;
    }

    public void setAhlTin(String ahlTin) {
        this.ahlTin = ahlTin;
    }

    public String getHhdNo() {
        return hhdNo;
    }

    public void setHhdNo(String hhdNo) {
        this.hhdNo = hhdNo;
    }

    public String getSlnomember() {
        return slNoMember;
    }

    public void setSlnomember(String slnomember) {
        this.slNoMember = slnomember;
    }

    public String getSlnohhd() {
        return slNoHd;
    }

    public void setSlnohhd(String slnohhd) {
        this.slNoHd = slnohhd;
    }

    public String getAhlslnohhd() {
        return ahlSlNoHhd;
    }

    public void setAhlslnohhd(String ahlslnohhd) {
        this.ahlSlNoHhd = ahlslnohhd;
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

    public String getCasteGroup() {
        return casteGroup;
    }

    public void setCasteGroup(String casteGroup) {
        this.casteGroup = casteGroup;
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

    public String getAhlblockno() {
        return ahlBlockNo;
    }

    public void setAhlblockno(String ahlblockno) {
        this.ahlBlockNo = ahlblockno;
    }

    public String getTypeofhhd() {
        return typeofhhd;
    }

    public void setTypeofhhd(String typeofhhd) {
        this.typeofhhd = typeofhhd;
    }

    public String getHhdLandownedcodes() {
        return hhdLandownedcodes;
    }

    public void setHhdLandownedcodes(String hhdLandownedcodes) {
        this.hhdLandownedcodes = hhdLandownedcodes;
    }

    public String getTotalirrigated() {
        return totalirrigated;
    }

    public void setTotalirrigated(String totalirrigated) {
        this.totalirrigated = totalirrigated;
    }

    public String getHhdIrriEquip() {
        return hhdIrriEquip;
    }

    public void setHhdIrriEquip(String hhdIrriEquip) {
        this.hhdIrriEquip = hhdIrriEquip;
    }

    public String getTotalunirrigated() {
        return totalunirrigated;
    }

    public void setTotalunirrigated(String totalunirrigated) {
        this.totalunirrigated = totalunirrigated;
    }

    public String getOtherirrigated() {
        return otherirrigated;
    }

    public void setOtherirrigated(String otherirrigated) {
        this.otherirrigated = otherirrigated;
    }

    public String getHhdAssetMveh() {
        return hhdAssetMveh;
    }

    public void setHhdAssetMveh(String hhdAssetMveh) {
        this.hhdAssetMveh = hhdAssetMveh;
    }

    public String getHhdMagriEquip() {
        return hhdMagriEquip;
    }

    public void setHhdMagriEquip(String hhdMagriEquip) {
        this.hhdMagriEquip = hhdMagriEquip;
    }

    public String getHhdKcc() {
        return hhdKcc;
    }

    public void setHhdKcc(String hhdKcc) {
        this.hhdKcc = hhdKcc;
    }

    public String getHhdEmpSector() {
        return hhdEmpSector;
    }

    public void setHhdEmpSector(String hhdEmpSector) {
        this.hhdEmpSector = hhdEmpSector;
    }

    public String getHhdEmpErg() {
        return hhdEmpErg;
    }

    public void setHhdEmpErg(String hhdEmpErg) {
        this.hhdEmpErg = hhdEmpErg;
    }

    public String getHhdEmpHem() {
        return hhdEmpHem;
    }

    public void setHhdEmpHem(String hhdEmpHem) {
        this.hhdEmpHem = hhdEmpHem;
    }

    public String getHhdEmpPit() {
        return hhdEmpPit;
    }

    public void setHhdEmpPit(String hhdEmpPit) {
        this.hhdEmpPit = hhdEmpPit;
    }

    public String getHousingWalltype() {
        return housingWalltype;
    }

    public void setHousingWalltype(String housingWalltype) {
        this.housingWalltype = housingWalltype;
    }

    public String getHousingRooftype() {
        return housingRooftype;
    }

    public void setHousingRooftype(String housingRooftype) {
        this.housingRooftype = housingRooftype;
    }

    public String getHousingNoOfRooms() {
        return housingNoOfRooms;
    }

    public void setHousingNoOfRooms(String housingNoOfRooms) {
        this.housingNoOfRooms = housingNoOfRooms;
    }

    public String getHhdAssetRef() {
        return hhdAssetRef;
    }

    public void setHhdAssetRef(String hhdAssetRef) {
        this.hhdAssetRef = hhdAssetRef;
    }

    public String getHhdAssetTelmob() {
        return hhdAssetTelmob;
    }

    public void setHhdAssetTelmob(String hhdAssetTelmob) {
        this.hhdAssetTelmob = hhdAssetTelmob;
    }

    public String getLivinginshelter() {
        return livinginshelter;
    }

    public void setLivinginshelter(String livinginshelter) {
        this.livinginshelter = livinginshelter;
    }

    public String getHhdEmpOther() {
        return hhdEmpOther;
    }

    public void setHhdEmpOther(String hhdEmpOther) {
        this.hhdEmpOther = hhdEmpOther;
    }

    public String getHhdMs() {
        return hhdMs;
    }

    public void setHhdMs(String hhdMs) {
        this.hhdMs = hhdMs;
    }

    public String getHhdPtg() {
        return hhdPtg;
    }

    public void setHhdPtg(String hhdPtg) {
        this.hhdPtg = hhdPtg;
    }

    public String getHhdLrbl() {
        return hhdLrbl;
    }

    public void setHhdLrbl(String hhdLrbl) {
        this.hhdLrbl = hhdLrbl;
    }

    public String getRuralUrban() {
        return ruralUrban;
    }

    public void setRuralUrban(String ruralUrban) {
        this.ruralUrban = ruralUrban;
    }

    public String getMddsStc() {
        return mddsStc;
    }

    public void setMddsStc(String mddsStc) {
        this.mddsStc = mddsStc;
    }

    public String getMddsDtc() {
        return mddsDtc;
    }

    public void setMddsDtc(String mddsDtc) {
        this.mddsDtc = mddsDtc;
    }

    public String getMddsSdtc() {
        return mddsSdtc;
    }

    public void setMddsSdtc(String mddsSdtc) {
        this.mddsSdtc = mddsSdtc;
    }

    public String getMddsPlcn() {
        return mddsPlcn;
    }

    public void setMddsPlcn(String mddsPlcn) {
        this.mddsPlcn = mddsPlcn;
    }
/*
    public String getSubblockno() {
        return subblockno;
    }

    public void setSubblockno(String subblockno) {
        this.subblockno = subblockno;
    }*/

    public String getUrn_no() {
        return urn_no;
    }

    public void setUrn_no(String urn_no) {
        this.urn_no = urn_no;
    }

    public String getValidatedBy() {
        return validatedBy;
    }

    public void setValidatedBy(String validatedBy) {
        this.validatedBy = validatedBy;
    }

    public String getUrnId() {
        return urnId;
    }

    public void setUrnId(String urnId) {
        this.urnId = urnId;
    }
}
