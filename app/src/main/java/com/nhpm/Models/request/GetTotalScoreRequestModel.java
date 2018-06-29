package com.nhpm.Models.request;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by SUNAINA on 29-06-2018.
 */

public class GetTotalScoreRequestModel implements Serializable {
    private String strName1;
    private String strState1;
    private String strDistrict1;
    private String strSubDistrict1;
    private String strVillage1;
    private String nAge1;
    private String chGender1;
    private String strName2;
    private String strState2;
    private String strDistrict2;
    private String strSubDistrict2;
    private String strVillage2;
    private String nAge2;
    private String chGender2;


    static public GetTotalScoreRequestModel create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, GetTotalScoreRequestModel.class);
    }

    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public String getStrName1() {
        return strName1;
    }

    public void setStrName1(String strName1) {
        this.strName1 = strName1;
    }

    public String getStrState1() {
        return strState1;
    }

    public void setStrState1(String strState1) {
        this.strState1 = strState1;
    }

    public String getStrDistrict1() {
        return strDistrict1;
    }

    public void setStrDistrict1(String strDistrict1) {
        this.strDistrict1 = strDistrict1;
    }

    public String getStrSubDistrict1() {
        return strSubDistrict1;
    }

    public void setStrSubDistrict1(String strSubDistrict1) {
        this.strSubDistrict1 = strSubDistrict1;
    }

    public String getStrVillage1() {
        return strVillage1;
    }

    public void setStrVillage1(String strVillage1) {
        this.strVillage1 = strVillage1;
    }

    public String getnAge1() {
        return nAge1;
    }

    public void setnAge1(String nAge1) {
        this.nAge1 = nAge1;
    }

    public String getChGender1() {
        return chGender1;
    }

    public void setChGender1(String chGender1) {
        this.chGender1 = chGender1;
    }

    public String getStrName2() {
        return strName2;
    }

    public void setStrName2(String strName2) {
        this.strName2 = strName2;
    }

    public String getStrState2() {
        return strState2;
    }

    public void setStrState2(String strState2) {
        this.strState2 = strState2;
    }

    public String getStrDistrict2() {
        return strDistrict2;
    }

    public void setStrDistrict2(String strDistrict2) {
        this.strDistrict2 = strDistrict2;
    }

    public String getStrSubDistrict2() {
        return strSubDistrict2;
    }

    public void setStrSubDistrict2(String strSubDistrict2) {
        this.strSubDistrict2 = strSubDistrict2;
    }

    public String getStrVillage2() {
        return strVillage2;
    }

    public void setStrVillage2(String strVillage2) {
        this.strVillage2 = strVillage2;
    }

    public String getnAge2() {
        return nAge2;
    }

    public void setnAge2(String nAge2) {
        this.nAge2 = nAge2;
    }

    public String getChGender2() {
        return chGender2;
    }

    public void setChGender2(String chGender2) {
        this.chGender2 = chGender2;
    }
}
