package com.nhpm.ReqRespModels;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by psqit on 8/31/2016.
 */
public class NhsDataList implements Serializable {


    public String getRemoteid() {
        return remoteid;
    }

    public void setRemoteid(String remoteid) {
        this.remoteid = remoteid;
    }

    private String remoteid;
    private String id;
    private String status;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String tin;
    private String statecode;
    private String districtcode;
    private String tehsilcode;
    private String towncode;
    private String wardid;
    private String blockno;
    private String thid;
    private String slnohhdNpr;
    private String slnomemberNpr;
    private String nameNpr;
    private String nameslNpr;
    private String fathernmNpr;
    private String fathernmslNpr;
    private String mothernmNpr;
    private String mothernmslNpr;
    private String occunameNpr;
    private String occunameslNpr;
    private String relnameNpr;
    private String relnameslNpr;
    private String genderidNpr;
    private String dobNpr;
    private String mstatusidNpr;
    private String edunameNpr;
    private String spousenm;
    private String aadhaarNo;
    private String eid;
    private String statename;
    private String districtname;
    private String villagename;
    private String countryname;
    private String addressline1;
    private String addressline2;
    private String addressline3;
    private String addressline4;
    private String addressline5;
    private String pincode;
    private String paddressline1;
    private String paddressline2;
    private String paddressline3;
    private String paddressline4;
    private String paddressline5;
    private String ppincode;
    private String phoneRespondent;
    private String slnohhdSecc;
    private String slnomemberSecc;
    private String nameSecc;
    private String nameSlSecc;
    private String fathernameSecc;
    private String fathernameSlSecc;
    private String mothernameSecc;
    private String mothernameSlSecc;
    private String occupationSecc;
    private String occupationSlSecc;
    private String relationSecc;
    private String relationSlSecc;
    private String genderidSecc;
    private String dobSecc;
    private String mstatusidSecc;
    private String educodeSecc;
    private String educationOther;
    private String ahlblockno;
    private String ahlslnohhd;
    private String disabilitycode;
    private String typeofhhd;
    private String ahlTin;
    private String hhdLandownedcodes;
    private String totalirrigated;
    private String totalunirrigated;
    private String otherirrigated;
    private String casteGroup;
    private String ahltypeofeb;
    private String livinginshelter;
    private String incomesourceUrban;
    private String wagesUrban;
    private String religion;
    private String chronicillness;
    private String housingWalltype;
    private String housingRooftype;
    private String housingOwnership;
    private String housingNoOfRooms;
    private String hhdAssetRef;
    private String hhdAssetTelmob;
    private String hhdAssetComLap;
    private String hhdAssetMveh;
    private String hhdAssetAc;
    private String hhdAssetWmachine;
    private String hhdPtg;
    private String hhdLrbl;
    private String hhdMs;
    private String hhdMagriEquip;
    private String hhdIrriEquip;
    private String hhdKcc;
    private String hhdEmpSal;
    private String hhdEmpSector;
    private String hhdEmpPit;
    private String hhdEmpErg;
    private String hhdEmpHem;
    private String hhdEmpOther;
    private String hhdAmenitiescodesDw;
    private String hhdAmenitiescodesL;
    private String hhdAmenitiescodesWsl;
    private String hhdAmenitiescodesWwo;
    private String hhdAmenitiescodesSrk;
    private String memberStatus;
    private String source;
    private String imagefilename;
    private String pds;
    private String pension;
    private String edunamesl;
    private String spousenmsl;
    private String villagenamesl;
    private String districtnamesl;
    private String statenamesl;
    private String countrynamesl;
    private String natnamesl;
    private String addressline1Sl;
    private String addressline2Sl;
    private String addressline3Sl;
    private String addressline4Sl;
    private String addressline5Sl;
    private String paddressline1Sl;
    private String paddressline2Sl;
    private String paddressline3Sl;
    private String paddressline4Sl;
    private String paddressline5Sl;
    private String nameRespondentsl;
    private String census2011Stcode;
    private String census2011Dtcode;
    private String census2011Tehsilcode;
    private String cencus2011Towncode;



  /*  public nhsDataList(String id, String tin, String statecode, String districtcode, String tehsilcode, String towncode, String wardid, String blockno, String thid, String slnohhdNpr, String slnomemberNpr, String nameNpr, String nameslNpr, String fathernmNpr, String fathernmslNpr, String mothernmNpr, String mothernmslNpr, String occunameNpr, String occunameslNpr, String relnameNpr, String relnameslNpr, String genderidNpr, String dobNpr, String mstatusidNpr, String edunameNpr, String spousenm, String aadhaarNo, String eid, String statename, String districtname, String villagename, String countryname, String addressline1, String addressline2, String addressline3, String addressline4, String addressline5, String pincode, String paddressline1, String paddressline2, String paddressline3, String paddressline4, String paddressline5, String ppincode, String phoneRespondent, String slnohhdSecc, String slnomemberSecc, String nameSecc, String nameSlSecc, String fathernameSecc, String fathernameSlSecc, String mothernameSecc, String mothernameSlSecc, String occupationSecc, String occupationSlSecc, String relationSecc, String relationSlSecc, String genderidSecc, String dobSecc, String mstatusidSecc, String educodeSecc, String educationOther, String ahlblockno, String ahlslnohhd, String typeofhhd, String disabilitycode, String ahlTin, String hhdLandownedcodes, String totalirrigated, String totalunirrigated, String otherirrigated, String casteGroup, String ahltypeofeb, String livinginshelter, String incomesourceUrban, String wagesUrban, String religion, String chronicillness, String housingWalltype, String housingRooftype, String housingOwnership, String housingNoOfRooms, String hhdAssetRef, String hhdAssetTelmob, String hhdAssetComLap, String hhdAssetMveh, String hhdAssetAc, String hhdAssetWmachine, String hhdPtg, String hhdLrbl, String hhdMs, String hhdMagriEquip, String hhdIrriEquip, String hhdKcc, String hhdEmpSal, String hhdEmpSector, String hhdEmpPit, String hhdEmpErg, String hhdEmpHem, String hhdEmpOther, String hhdAmenitiescodesDw, String hhdAmenitiescodesL, String hhdAmenitiescodesWsl, String hhdAmenitiescodesWwo, String hhdAmenitiescodesSrk, String memberStatus, String source, String imagefilename, String pds, String pension, String edunamesl, String spousenmsl, String villagenamesl, String districtnamesl, String statenamesl, String countrynamesl, String natnamesl, String addressline1Sl, String addressline2Sl, String addressline3Sl, String addressline4Sl, String addressline5Sl, String paddressline1Sl, String paddressline2Sl, String paddressline3Sl, String paddressline4Sl, String paddressline5Sl, String nameRespondentsl, String census2011Stcode, String census2011Dtcode, String census2011Tehsilcode, String cencus2011Towncode) {
        this.id = id;
        this.tin = tin;
        this.statecode = statecode;
        this.districtcode = districtcode;
        this.tehsilcode = tehsilcode;
        this.towncode = towncode;
        this.wardid = wardid;
        this.blockno = blockno;
        this.thid = thid;
        this.slnohhdNpr = slnohhdNpr;
        this.slnomemberNpr = slnomemberNpr;
        this.nameNpr = nameNpr;
        this.nameslNpr = nameslNpr;
        this.fathernmNpr = fathernmNpr;
        this.fathernmslNpr = fathernmslNpr;
        this.mothernmNpr = mothernmNpr;
        this.mothernmslNpr = mothernmslNpr;
        this.occunameNpr = occunameNpr;
        this.occunameslNpr = occunameslNpr;
        this.relnameNpr = relnameNpr;
        this.relnameslNpr = relnameslNpr;
        this.genderidNpr = genderidNpr;
        this.dobNpr = dobNpr;
        this.mstatusidNpr = mstatusidNpr;
        this.edunameNpr = edunameNpr;
        this.spousenm = spousenm;
        this.aadhaarNo = aadhaarNo;
        this.eid = eid;
        this.statename = statename;
        this.districtname = districtname;
        this.villagename = villagename;
        this.countryname = countryname;
        this.addressline1 = addressline1;
        this.addressline2 = addressline2;
        this.addressline3 = addressline3;
        this.addressline4 = addressline4;
        this.addressline5 = addressline5;
        this.pincode = pincode;
        this.paddressline1 = paddressline1;
        this.paddressline2 = paddressline2;
        this.paddressline3 = paddressline3;
        this.paddressline4 = paddressline4;
        this.paddressline5 = paddressline5;
        this.ppincode = ppincode;
        this.phoneRespondent = phoneRespondent;
        this.slnohhdSecc = slnohhdSecc;
        this.slnomemberSecc = slnomemberSecc;
        this.nameSecc = nameSecc;
        this.nameSlSecc = nameSlSecc;
        this.fathernameSecc = fathernameSecc;
        this.fathernameSlSecc = fathernameSlSecc;
        this.mothernameSecc = mothernameSecc;
        this.mothernameSlSecc = mothernameSlSecc;
        this.occupationSecc = occupationSecc;
        this.occupationSlSecc = occupationSlSecc;
        this.relationSecc = relationSecc;
        this.relationSlSecc = relationSlSecc;
        this.genderidSecc = genderidSecc;
        this.dobSecc = dobSecc;
        this.mstatusidSecc = mstatusidSecc;
        this.educodeSecc = educodeSecc;
        this.educationOther = educationOther;
        this.ahlblockno = ahlblockno;
        this.ahlslnohhd = ahlslnohhd;
        this.typeofhhd = typeofhhd;
        this.disabilitycode = disabilitycode;
        this.ahlTin = ahlTin;
        this.hhdLandownedcodes = hhdLandownedcodes;
        this.totalirrigated = totalirrigated;
        this.totalunirrigated = totalunirrigated;
        this.otherirrigated = otherirrigated;
        this.casteGroup = casteGroup;
        this.ahltypeofeb = ahltypeofeb;
        this.livinginshelter = livinginshelter;
        this.incomesourceUrban = incomesourceUrban;
        this.wagesUrban = wagesUrban;
        this.religion = religion;
        this.chronicillness = chronicillness;
        this.housingWalltype = housingWalltype;
        this.housingRooftype = housingRooftype;
        this.housingOwnership = housingOwnership;
        this.housingNoOfRooms = housingNoOfRooms;
        this.hhdAssetRef = hhdAssetRef;
        this.hhdAssetTelmob = hhdAssetTelmob;
        this.hhdAssetComLap = hhdAssetComLap;
        this.hhdAssetMveh = hhdAssetMveh;
        this.hhdAssetAc = hhdAssetAc;
        this.hhdAssetWmachine = hhdAssetWmachine;
        this.hhdPtg = hhdPtg;
        this.hhdLrbl = hhdLrbl;
        this.hhdMs = hhdMs;
        this.hhdMagriEquip = hhdMagriEquip;
        this.hhdIrriEquip = hhdIrriEquip;
        this.hhdKcc = hhdKcc;
        this.hhdEmpSal = hhdEmpSal;
        this.hhdEmpSector = hhdEmpSector;
        this.hhdEmpPit = hhdEmpPit;
        this.hhdEmpErg = hhdEmpErg;
        this.hhdEmpHem = hhdEmpHem;
        this.hhdEmpOther = hhdEmpOther;
        this.hhdAmenitiescodesDw = hhdAmenitiescodesDw;
        this.hhdAmenitiescodesL = hhdAmenitiescodesL;
        this.hhdAmenitiescodesWsl = hhdAmenitiescodesWsl;
        this.hhdAmenitiescodesWwo = hhdAmenitiescodesWwo;
        this.hhdAmenitiescodesSrk = hhdAmenitiescodesSrk;
        this.memberStatus = memberStatus;
        this.source = source;
        this.imagefilename = imagefilename;
        this.pds = pds;
        this.pension = pension;
        this.edunamesl = edunamesl;
        this.spousenmsl = spousenmsl;
        this.villagenamesl = villagenamesl;
        this.districtnamesl = districtnamesl;
        this.statenamesl = statenamesl;
        this.countrynamesl = countrynamesl;
        this.natnamesl = natnamesl;
        this.addressline1Sl = addressline1Sl;
        this.addressline2Sl = addressline2Sl;
        this.addressline3Sl = addressline3Sl;
        this.addressline4Sl = addressline4Sl;
        this.addressline5Sl = addressline5Sl;
        this.paddressline1Sl = paddressline1Sl;
        this.paddressline2Sl = paddressline2Sl;
        this.paddressline3Sl = paddressline3Sl;
        this.paddressline4Sl = paddressline4Sl;
        this.paddressline5Sl = paddressline5Sl;
        this.nameRespondentsl = nameRespondentsl;
        this.census2011Stcode = census2011Stcode;
        this.census2011Dtcode = census2011Dtcode;
        this.census2011Tehsilcode = census2011Tehsilcode;
        this.cencus2011Towncode = cencus2011Towncode;
    }
*/
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTin() {
        return tin;
    }

    public void setTin(String tin) {
        this.tin = tin;
    }

    public String getStatecode() {
        return statecode;
    }

    public void setStatecode(String statecode) {
        this.statecode = statecode;
    }

    public String getTehsilcode() {
        return tehsilcode;
    }

    public void setTehsilcode(String tehsilcode) {
        this.tehsilcode = tehsilcode;
    }

    public String getDistrictcode() {
        return districtcode;
    }

    public void setDistrictcode(String districtcode) {
        this.districtcode = districtcode;
    }

    public String getTowncode() {
        return towncode;
    }

    public void setTowncode(String towncode) {
        this.towncode = towncode;
    }

    public String getWardid() {
        return wardid;
    }

    public void setWardid(String wardid) {
        this.wardid = wardid;
    }

    public String getBlockno() {
        return blockno;
    }

    public void setBlockno(String blockno) {
        this.blockno = blockno;
    }

    public String getThid() {
        return thid;
    }

    public void setThid(String thid) {
        this.thid = thid;
    }

    public String getSlnohhdNpr() {
        return slnohhdNpr;
    }

    public void setSlnohhdNpr(String slnohhdNpr) {
        this.slnohhdNpr = slnohhdNpr;
    }

    public String getSlnomemberNpr() {
        return slnomemberNpr;
    }

    public void setSlnomemberNpr(String slnomemberNpr) {
        this.slnomemberNpr = slnomemberNpr;
    }

    public String getNameNpr() {
        return nameNpr;
    }

    public void setNameNpr(String nameNpr) {
        this.nameNpr = nameNpr;
    }

    public String getNameslNpr() {
        return nameslNpr;
    }

    public void setNameslNpr(String nameslNpr) {
        this.nameslNpr = nameslNpr;
    }

    public String getFathernmNpr() {
        return fathernmNpr;
    }

    public void setFathernmNpr(String fathernmNpr) {
        this.fathernmNpr = fathernmNpr;
    }

    public String getFathernmslNpr() {
        return fathernmslNpr;
    }

    public void setFathernmslNpr(String fathernmslNpr) {
        this.fathernmslNpr = fathernmslNpr;
    }

    public String getMothernmNpr() {
        return mothernmNpr;
    }

    public void setMothernmNpr(String mothernmNpr) {
        this.mothernmNpr = mothernmNpr;
    }

    public String getMothernmslNpr() {
        return mothernmslNpr;
    }

    public void setMothernmslNpr(String mothernmslNpr) {
        this.mothernmslNpr = mothernmslNpr;
    }

    public String getOccunameslNpr() {
        return occunameslNpr;
    }

    public void setOccunameslNpr(String occunameslNpr) {
        this.occunameslNpr = occunameslNpr;
    }

    public String getOccunameNpr() {
        return occunameNpr;
    }

    public void setOccunameNpr(String occunameNpr) {
        this.occunameNpr = occunameNpr;
    }

    public String getRelnameNpr() {
        return relnameNpr;
    }

    public void setRelnameNpr(String relnameNpr) {
        this.relnameNpr = relnameNpr;
    }

    public String getRelnameslNpr() {
        return relnameslNpr;
    }

    public void setRelnameslNpr(String relnameslNpr) {
        this.relnameslNpr = relnameslNpr;
    }

    public String getGenderidNpr() {
        return genderidNpr;
    }

    public void setGenderidNpr(String genderidNpr) {
        this.genderidNpr = genderidNpr;
    }

    public String getDobNpr() {
        return dobNpr;
    }

    public void setDobNpr(String dobNpr) {
        this.dobNpr = dobNpr;
    }

    public String getMstatusidNpr() {
        return mstatusidNpr;
    }

    public void setMstatusidNpr(String mstatusidNpr) {
        this.mstatusidNpr = mstatusidNpr;
    }

    public String getEdunameNpr() {
        return edunameNpr;
    }

    public void setEdunameNpr(String edunameNpr) {
        this.edunameNpr = edunameNpr;
    }

    public String getSpousenm() {
        return spousenm;
    }

    public void setSpousenm(String spousenm) {
        this.spousenm = spousenm;
    }

    public String getAadhaarNo() {
        return aadhaarNo;
    }

    public void setAadhaarNo(String aadhaarNo) {
        this.aadhaarNo = aadhaarNo;
    }

    public String getEid() {
        return eid;
    }

    public void setEid(String eid) {
        this.eid = eid;
    }

    public String getStatename() {
        return statename;
    }

    public void setStatename(String statename) {
        this.statename = statename;
    }

    public String getDistrictname() {
        return districtname;
    }

    public void setDistrictname(String districtname) {
        this.districtname = districtname;
    }

    public String getVillagename() {
        return villagename;
    }

    public void setVillagename(String villagename) {
        this.villagename = villagename;
    }

    public String getCountryname() {
        return countryname;
    }

    public void setCountryname(String countryname) {
        this.countryname = countryname;
    }

    public String getAddressline1() {
        return addressline1;
    }

    public void setAddressline1(String addressline1) {
        this.addressline1 = addressline1;
    }

    public String getAddressline2() {
        return addressline2;
    }

    public void setAddressline2(String addressline2) {
        this.addressline2 = addressline2;
    }

    public String getAddressline3() {
        return addressline3;
    }

    public void setAddressline3(String addressline3) {
        this.addressline3 = addressline3;
    }

    public String getAddressline4() {
        return addressline4;
    }

    public void setAddressline4(String addressline4) {
        this.addressline4 = addressline4;
    }

    public String getAddressline5() {
        return addressline5;
    }

    public void setAddressline5(String addressline5) {
        this.addressline5 = addressline5;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getPaddressline1() {
        return paddressline1;
    }

    public void setPaddressline1(String paddressline1) {
        this.paddressline1 = paddressline1;
    }

    public String getPaddressline2() {
        return paddressline2;
    }

    public void setPaddressline2(String paddressline2) {
        this.paddressline2 = paddressline2;
    }

    public String getPaddressline3() {
        return paddressline3;
    }

    public void setPaddressline3(String paddressline3) {
        this.paddressline3 = paddressline3;
    }

    public String getPaddressline4() {
        return paddressline4;
    }

    public void setPaddressline4(String paddressline4) {
        this.paddressline4 = paddressline4;
    }

    public String getPaddressline5() {
        return paddressline5;
    }

    public void setPaddressline5(String paddressline5) {
        this.paddressline5 = paddressline5;
    }

    public String getPpincode() {
        return ppincode;
    }

    public void setPpincode(String ppincode) {
        this.ppincode = ppincode;
    }

    public String getPhoneRespondent() {
        return phoneRespondent;
    }

    public void setPhoneRespondent(String phoneRespondent) {
        this.phoneRespondent = phoneRespondent;
    }

    public String getSlnohhdSecc() {
        return slnohhdSecc;
    }

    public void setSlnohhdSecc(String slnohhdSecc) {
        this.slnohhdSecc = slnohhdSecc;
    }

    public String getSlnomemberSecc() {
        return slnomemberSecc;
    }

    public void setSlnomemberSecc(String slnomemberSecc) {
        this.slnomemberSecc = slnomemberSecc;
    }

    public String getNameSecc() {
        return nameSecc;
    }

    public void setNameSecc(String nameSecc) {
        this.nameSecc = nameSecc;
    }

    public String getNameSlSecc() {
        return nameSlSecc;
    }

    public void setNameSlSecc(String nameSlSecc) {
        this.nameSlSecc = nameSlSecc;
    }

    public String getFathernameSecc() {
        return fathernameSecc;
    }

    public void setFathernameSecc(String fathernameSecc) {
        this.fathernameSecc = fathernameSecc;
    }

    public String getFathernameSlSecc() {
        return fathernameSlSecc;
    }

    public void setFathernameSlSecc(String fathernameSlSecc) {
        this.fathernameSlSecc = fathernameSlSecc;
    }

    public String getMothernameSlSecc() {
        return mothernameSlSecc;
    }

    public void setMothernameSlSecc(String mothernameSlSecc) {
        this.mothernameSlSecc = mothernameSlSecc;
    }

    public String getMothernameSecc() {
        return mothernameSecc;
    }

    public void setMothernameSecc(String mothernameSecc) {
        this.mothernameSecc = mothernameSecc;
    }

    public String getOccupationSecc() {
        return occupationSecc;
    }

    public void setOccupationSecc(String occupationSecc) {
        this.occupationSecc = occupationSecc;
    }

    public String getOccupationSlSecc() {
        return occupationSlSecc;
    }

    public void setOccupationSlSecc(String occupationSlSecc) {
        this.occupationSlSecc = occupationSlSecc;
    }

    public String getRelationSlSecc() {
        return relationSlSecc;
    }

    public void setRelationSlSecc(String relationSlSecc) {
        this.relationSlSecc = relationSlSecc;
    }

    public String getRelationSecc() {
        return relationSecc;
    }

    public void setRelationSecc(String relationSecc) {
        this.relationSecc = relationSecc;
    }

    public String getGenderidSecc() {
        return genderidSecc;
    }

    public void setGenderidSecc(String genderidSecc) {
        this.genderidSecc = genderidSecc;
    }

    public String getDobSecc() {
        return dobSecc;
    }

    public void setDobSecc(String dobSecc) {
        this.dobSecc = dobSecc;
    }

    public String getMstatusidSecc() {
        return mstatusidSecc;
    }

    public void setMstatusidSecc(String mstatusidSecc) {
        this.mstatusidSecc = mstatusidSecc;
    }

    public String getEducodeSecc() {
        return educodeSecc;
    }

    public void setEducodeSecc(String educodeSecc) {
        this.educodeSecc = educodeSecc;
    }

    public String getEducationOther() {
        return educationOther;
    }

    public void setEducationOther(String educationOther) {
        this.educationOther = educationOther;
    }

    public String getAhlblockno() {
        return ahlblockno;
    }

    public void setAhlblockno(String ahlblockno) {
        this.ahlblockno = ahlblockno;
    }

    public String getDisabilitycode() {
        return disabilitycode;
    }

    public void setDisabilitycode(String disabilitycode) {
        this.disabilitycode = disabilitycode;
    }

    public String getAhlslnohhd() {
        return ahlslnohhd;
    }

    public void setAhlslnohhd(String ahlslnohhd) {
        this.ahlslnohhd = ahlslnohhd;
    }

    public String getTypeofhhd() {
        return typeofhhd;
    }

    public void setTypeofhhd(String typeofhhd) {
        this.typeofhhd = typeofhhd;
    }

    public String getAhlTin() {
        return ahlTin;
    }

    public void setAhlTin(String ahlTin) {
        this.ahlTin = ahlTin;
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

    public String getCasteGroup() {
        return casteGroup;
    }

    public void setCasteGroup(String casteGroup) {
        this.casteGroup = casteGroup;
    }

    public String getAhltypeofeb() {
        return ahltypeofeb;
    }

    public void setAhltypeofeb(String ahltypeofeb) {
        this.ahltypeofeb = ahltypeofeb;
    }

    public String getLivinginshelter() {
        return livinginshelter;
    }

    public void setLivinginshelter(String livinginshelter) {
        this.livinginshelter = livinginshelter;
    }

    public String getIncomesourceUrban() {
        return incomesourceUrban;
    }

    public void setIncomesourceUrban(String incomesourceUrban) {
        this.incomesourceUrban = incomesourceUrban;
    }

    public String getWagesUrban() {
        return wagesUrban;
    }

    public void setWagesUrban(String wagesUrban) {
        this.wagesUrban = wagesUrban;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public String getChronicillness() {
        return chronicillness;
    }

    public void setChronicillness(String chronicillness) {
        this.chronicillness = chronicillness;
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

    public String getHousingOwnership() {
        return housingOwnership;
    }

    public void setHousingOwnership(String housingOwnership) {
        this.housingOwnership = housingOwnership;
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

    public String getHhdAssetComLap() {
        return hhdAssetComLap;
    }

    public void setHhdAssetComLap(String hhdAssetComLap) {
        this.hhdAssetComLap = hhdAssetComLap;
    }

    public String getHhdAssetMveh() {
        return hhdAssetMveh;
    }

    public void setHhdAssetMveh(String hhdAssetMveh) {
        this.hhdAssetMveh = hhdAssetMveh;
    }

    public String getHhdAssetAc() {
        return hhdAssetAc;
    }

    public void setHhdAssetAc(String hhdAssetAc) {
        this.hhdAssetAc = hhdAssetAc;
    }

    public String getHhdAssetWmachine() {
        return hhdAssetWmachine;
    }

    public void setHhdAssetWmachine(String hhdAssetWmachine) {
        this.hhdAssetWmachine = hhdAssetWmachine;
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

    public String getHhdMs() {
        return hhdMs;
    }

    public void setHhdMs(String hhdMs) {
        this.hhdMs = hhdMs;
    }

    public String getHhdMagriEquip() {
        return hhdMagriEquip;
    }

    public void setHhdMagriEquip(String hhdMagriEquip) {
        this.hhdMagriEquip = hhdMagriEquip;
    }

    public String getHhdIrriEquip() {
        return hhdIrriEquip;
    }

    public void setHhdIrriEquip(String hhdIrriEquip) {
        this.hhdIrriEquip = hhdIrriEquip;
    }

    public String getHhdKcc() {
        return hhdKcc;
    }

    public void setHhdKcc(String hhdKcc) {
        this.hhdKcc = hhdKcc;
    }

    public String getHhdEmpSal() {
        return hhdEmpSal;
    }

    public void setHhdEmpSal(String hhdEmpSal) {
        this.hhdEmpSal = hhdEmpSal;
    }

    public String getHhdEmpSector() {
        return hhdEmpSector;
    }

    public void setHhdEmpSector(String hhdEmpSector) {
        this.hhdEmpSector = hhdEmpSector;
    }

    public String getHhdEmpPit() {
        return hhdEmpPit;
    }

    public void setHhdEmpPit(String hhdEmpPit) {
        this.hhdEmpPit = hhdEmpPit;
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

    public String getHhdEmpOther() {
        return hhdEmpOther;
    }

    public void setHhdEmpOther(String hhdEmpOther) {
        this.hhdEmpOther = hhdEmpOther;
    }

    public String getHhdAmenitiescodesDw() {
        return hhdAmenitiescodesDw;
    }

    public void setHhdAmenitiescodesDw(String hhdAmenitiescodesDw) {
        this.hhdAmenitiescodesDw = hhdAmenitiescodesDw;
    }

    public String getHhdAmenitiescodesL() {
        return hhdAmenitiescodesL;
    }

    public void setHhdAmenitiescodesL(String hhdAmenitiescodesL) {
        this.hhdAmenitiescodesL = hhdAmenitiescodesL;
    }

    public String getHhdAmenitiescodesWsl() {
        return hhdAmenitiescodesWsl;
    }

    public void setHhdAmenitiescodesWsl(String hhdAmenitiescodesWsl) {
        this.hhdAmenitiescodesWsl = hhdAmenitiescodesWsl;
    }

    public String getHhdAmenitiescodesWwo() {
        return hhdAmenitiescodesWwo;
    }

    public void setHhdAmenitiescodesWwo(String hhdAmenitiescodesWwo) {
        this.hhdAmenitiescodesWwo = hhdAmenitiescodesWwo;
    }

    public String getHhdAmenitiescodesSrk() {
        return hhdAmenitiescodesSrk;
    }

    public void setHhdAmenitiescodesSrk(String hhdAmenitiescodesSrk) {
        this.hhdAmenitiescodesSrk = hhdAmenitiescodesSrk;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getMemberStatus() {
        return memberStatus;
    }

    public void setMemberStatus(String memberStatus) {
        this.memberStatus = memberStatus;
    }

    public String getImagefilename() {
        return imagefilename;
    }

    public void setImagefilename(String imagefilename) {
        this.imagefilename = imagefilename;
    }

    public String getPds() {
        return pds;
    }

    public void setPds(String pds) {
        this.pds = pds;
    }

    public String getPension() {
        return pension;
    }

    public void setPension(String pension) {
        this.pension = pension;
    }

    public String getEdunamesl() {
        return edunamesl;
    }

    public void setEdunamesl(String edunamesl) {
        this.edunamesl = edunamesl;
    }

    public String getSpousenmsl() {
        return spousenmsl;
    }

    public void setSpousenmsl(String spousenmsl) {
        this.spousenmsl = spousenmsl;
    }

    public String getVillagenamesl() {
        return villagenamesl;
    }

    public void setVillagenamesl(String villagenamesl) {
        this.villagenamesl = villagenamesl;
    }

    public String getDistrictnamesl() {
        return districtnamesl;
    }

    public void setDistrictnamesl(String districtnamesl) {
        this.districtnamesl = districtnamesl;
    }

    public String getStatenamesl() {
        return statenamesl;
    }

    public void setStatenamesl(String statenamesl) {
        this.statenamesl = statenamesl;
    }

    public String getCountrynamesl() {
        return countrynamesl;
    }

    public void setCountrynamesl(String countrynamesl) {
        this.countrynamesl = countrynamesl;
    }

    public String getNatnamesl() {
        return natnamesl;
    }

    public void setNatnamesl(String natnamesl) {
        this.natnamesl = natnamesl;
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

    public String getPaddressline1Sl() {
        return paddressline1Sl;
    }

    public void setPaddressline1Sl(String paddressline1Sl) {
        this.paddressline1Sl = paddressline1Sl;
    }

    public String getPaddressline2Sl() {
        return paddressline2Sl;
    }

    public void setPaddressline2Sl(String paddressline2Sl) {
        this.paddressline2Sl = paddressline2Sl;
    }

    public String getPaddressline3Sl() {
        return paddressline3Sl;
    }

    public void setPaddressline3Sl(String paddressline3Sl) {
        this.paddressline3Sl = paddressline3Sl;
    }

    public String getPaddressline4Sl() {
        return paddressline4Sl;
    }

    public void setPaddressline4Sl(String paddressline4Sl) {
        this.paddressline4Sl = paddressline4Sl;
    }

    public String getPaddressline5Sl() {
        return paddressline5Sl;
    }

    public void setPaddressline5Sl(String paddressline5Sl) {
        this.paddressline5Sl = paddressline5Sl;
    }

    public String getNameRespondentsl() {
        return nameRespondentsl;
    }

    public void setNameRespondentsl(String nameRespondentsl) {
        this.nameRespondentsl = nameRespondentsl;
    }

    public String getCensus2011Stcode() {
        return census2011Stcode;
    }

    public void setCensus2011Stcode(String census2011Stcode) {
        this.census2011Stcode = census2011Stcode;
    }

    public String getCensus2011Dtcode() {
        return census2011Dtcode;
    }

    public void setCensus2011Dtcode(String census2011Dtcode) {
        this.census2011Dtcode = census2011Dtcode;
    }

    public String getCensus2011Tehsilcode() {
        return census2011Tehsilcode;
    }

    public void setCensus2011Tehsilcode(String census2011Tehsilcode) {
        this.census2011Tehsilcode = census2011Tehsilcode;
    }

    public String getCencus2011Towncode() {
        return cencus2011Towncode;
    }

    public void setCencus2011Towncode(String cencus2011Towncode) {
        this.cencus2011Towncode = cencus2011Towncode;
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
