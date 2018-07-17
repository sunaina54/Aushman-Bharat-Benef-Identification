package com.nhpm.Models.response;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by SUNAINA on 16-07-2018.
 */

public class RSBYDataItem implements Serializable {
    private String statecode;
    private String districtcode;
    private String round;
    private String blockcode;
    private String blockname;
    private String panchayattowncode;
    private String panchayattownname;
    private String villagecode;
    private String villagename;
    private String locationtype;
    private String familyid;
    private String urn;
    private String ename;
    private String vernacularname;
    private String fatherhusbandname;
    private String headmemberid;
    private String doorhouseno;
    private String bplcitizen;
    private String categorycode;
    private String enrolleddate;
    private String personalizeddate;
    private String cardissueddate;
    private String rsbytype;
    private String minority;
    private String maincardcsn;
    private String miccsn;
    private String micauthorityid;
    private String dateissuance;
    private String dateactivation;
    private String dateexpiry;
    private String membername;
    private String memberid;
    private String gender;
    private String relationcode;
    private String age;
    private String ispresentoncard;
    private String insurancecompanycode;
    private String insurancepolicyno;


    static public RSBYDataItem create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, RSBYDataItem.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
    public String getStatecode() {
        return statecode;
    }

    public void setStatecode(String statecode) {
        this.statecode = statecode;
    }

    public String getDistrictcode() {
        return districtcode;
    }

    public void setDistrictcode(String districtcode) {
        this.districtcode = districtcode;
    }

    public String getRound() {
        return round;
    }

    public void setRound(String round) {
        this.round = round;
    }

    public String getBlockcode() {
        return blockcode;
    }

    public void setBlockcode(String blockcode) {
        this.blockcode = blockcode;
    }

    public String getBlockname() {
        return blockname;
    }

    public void setBlockname(String blockname) {
        this.blockname = blockname;
    }

    public String getPanchayattowncode() {
        return panchayattowncode;
    }

    public void setPanchayattowncode(String panchayattowncode) {
        this.panchayattowncode = panchayattowncode;
    }

    public String getPanchayattownname() {
        return panchayattownname;
    }

    public void setPanchayattownname(String panchayattownname) {
        this.panchayattownname = panchayattownname;
    }

    public String getVillagecode() {
        return villagecode;
    }

    public void setVillagecode(String villagecode) {
        this.villagecode = villagecode;
    }

    public String getVillagename() {
        return villagename;
    }

    public void setVillagename(String villagename) {
        this.villagename = villagename;
    }

    public String getLocationtype() {
        return locationtype;
    }

    public void setLocationtype(String locationtype) {
        this.locationtype = locationtype;
    }

    public String getFamilyid() {
        return familyid;
    }

    public void setFamilyid(String familyid) {
        this.familyid = familyid;
    }

    public String getUrn() {
        return urn;
    }

    public void setUrn(String urn) {
        this.urn = urn;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public String getVernacularname() {
        return vernacularname;
    }

    public void setVernacularname(String vernacularname) {
        this.vernacularname = vernacularname;
    }

    public String getFatherhusbandname() {
        return fatherhusbandname;
    }

    public void setFatherhusbandname(String fatherhusbandname) {
        this.fatherhusbandname = fatherhusbandname;
    }

    public String getHeadmemberid() {
        return headmemberid;
    }

    public void setHeadmemberid(String headmemberid) {
        this.headmemberid = headmemberid;
    }

    public String getDoorhouseno() {
        return doorhouseno;
    }

    public void setDoorhouseno(String doorhouseno) {
        this.doorhouseno = doorhouseno;
    }

    public String getBplcitizen() {
        return bplcitizen;
    }

    public void setBplcitizen(String bplcitizen) {
        this.bplcitizen = bplcitizen;
    }

    public String getCategorycode() {
        return categorycode;
    }

    public void setCategorycode(String categorycode) {
        this.categorycode = categorycode;
    }

    public String getEnrolleddate() {
        return enrolleddate;
    }

    public void setEnrolleddate(String enrolleddate) {
        this.enrolleddate = enrolleddate;
    }

    public String getPersonalizeddate() {
        return personalizeddate;
    }

    public void setPersonalizeddate(String personalizeddate) {
        this.personalizeddate = personalizeddate;
    }

    public String getCardissueddate() {
        return cardissueddate;
    }

    public void setCardissueddate(String cardissueddate) {
        this.cardissueddate = cardissueddate;
    }

    public String getRsbytype() {
        return rsbytype;
    }

    public void setRsbytype(String rsbytype) {
        this.rsbytype = rsbytype;
    }

    public String getMinority() {
        return minority;
    }

    public void setMinority(String minority) {
        this.minority = minority;
    }

    public String getMaincardcsn() {
        return maincardcsn;
    }

    public void setMaincardcsn(String maincardcsn) {
        this.maincardcsn = maincardcsn;
    }

    public String getMiccsn() {
        return miccsn;
    }

    public void setMiccsn(String miccsn) {
        this.miccsn = miccsn;
    }

    public String getMicauthorityid() {
        return micauthorityid;
    }

    public void setMicauthorityid(String micauthorityid) {
        this.micauthorityid = micauthorityid;
    }

    public String getDateissuance() {
        return dateissuance;
    }

    public void setDateissuance(String dateissuance) {
        this.dateissuance = dateissuance;
    }

    public String getDateactivation() {
        return dateactivation;
    }

    public void setDateactivation(String dateactivation) {
        this.dateactivation = dateactivation;
    }

    public String getDateexpiry() {
        return dateexpiry;
    }

    public void setDateexpiry(String dateexpiry) {
        this.dateexpiry = dateexpiry;
    }

    public String getMembername() {
        return membername;
    }

    public void setMembername(String membername) {
        this.membername = membername;
    }

    public String getMemberid() {
        return memberid;
    }

    public void setMemberid(String memberid) {
        this.memberid = memberid;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getRelationcode() {
        return relationcode;
    }

    public void setRelationcode(String relationcode) {
        this.relationcode = relationcode;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getIspresentoncard() {
        return ispresentoncard;
    }

    public void setIspresentoncard(String ispresentoncard) {
        this.ispresentoncard = ispresentoncard;
    }

    public String getInsurancecompanycode() {
        return insurancecompanycode;
    }

    public void setInsurancecompanycode(String insurancecompanycode) {
        this.insurancecompanycode = insurancecompanycode;
    }

    public String getInsurancepolicyno() {
        return insurancepolicyno;
    }

    public void setInsurancepolicyno(String insurancepolicyno) {
        this.insurancepolicyno = insurancepolicyno;
    }
}
