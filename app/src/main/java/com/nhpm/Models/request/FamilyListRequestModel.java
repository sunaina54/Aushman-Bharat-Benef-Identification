package com.nhpm.Models.request;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by SUNAINA on 31-05-2018.
 */

public class FamilyListRequestModel implements Serializable {

    private String ahlblockno;
    private String ahl_tin;
    private String rural_urban;
    private String spousenmsl;
    private String state_name;
    private String age;
    private String hhd_no;
    private String district_code;
    private String pincode;
    private String genderid;
    private String mothername;
    private String name;
    private String spousenm;
    private String state_name_english;
    private String fathername;
    private String block_name_english;
    private String userName;
    private String userPass;
    private String resultCount;

    public String getAhlTinno() {
        return ahl_tin;
    }

    public void setAhlTinno(String ahlTinno) {
        this.ahl_tin = ahlTinno;
    }

    public String getResultCount() {
        return resultCount;
    }

    public void setResultCount(String resultCount) {
        this.resultCount = resultCount;
    }

    public String getAhlblockno() {
        return ahlblockno;
    }

    public void setAhlblockno(String ahlblockno) {
        this.ahlblockno = ahlblockno;
    }

    public String getRural_urban() {
        return rural_urban;
    }

    public void setRural_urban(String rural_urban) {
        this.rural_urban = rural_urban;
    }

    public String getSpousenms() {
        return spousenmsl;
    }

    public void setSpousenms(String spousenms) {
        this.spousenmsl = spousenms;
    }

    public String getState_name() {
        return state_name;
    }

    public void setState_name(String state_name) {
        this.state_name = state_name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getHho_id() {
        return hhd_no;
    }

    public void setHho_id(String hho_id) {
        this.hhd_no = hho_id;
    }

    public String getDistrict_code() {
        return district_code;
    }

    public void setDistrict_code(String district_code) {
        this.district_code = district_code;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getGenderid() {
        return genderid;
    }

    public void setGenderid(String genderid) {
        this.genderid = genderid;
    }

    public String getMothername() {
        return mothername;
    }

    public void setMothername(String mothername) {
        this.mothername = mothername;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpousenm() {
        return spousenm;
    }

    public void setSpousenm(String spousenm) {
        this.spousenm = spousenm;
    }

    public String getState_name_english() {
        return state_name_english;
    }

    public void setState_name_english(String state_name_english) {
        this.state_name_english = state_name_english;
    }

    public String getFathername() {
        return fathername;
    }

    public void setFathername(String fathername) {
        this.fathername = fathername;
    }

    public String getBlock_name_english() {
        return block_name_english;
    }

    public void setBlock_name_english(String block_name_english) {
        this.block_name_english = block_name_english;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPass() {
        return userPass;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    static public FamilyListRequestModel create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, FamilyListRequestModel.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }

}



