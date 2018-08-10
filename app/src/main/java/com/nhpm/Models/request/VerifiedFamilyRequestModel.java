package com.nhpm.Models.request;

import com.google.gson.Gson;
import com.nhpm.Models.response.VerifiedFamilyResponseModel;

import java.io.Serializable;

/**
 * Created by SUNAINA on 10-08-2018.
 */

public class VerifiedFamilyRequestModel implements Serializable {
    private String hhd_no="";
    private String mobile_no="";
    private String rationcard_no="";
    private int statecode;
    private String nha_id="";
    private int param;

    public String getHhd_no() {
        return hhd_no;
    }

    public void setHhd_no(String hhd_no) {
        this.hhd_no = hhd_no;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getRationcard_no() {
        return rationcard_no;
    }

    public void setRationcard_no(String rationcard_no) {
        this.rationcard_no = rationcard_no;
    }

    public int getStatecode() {
        return statecode;
    }

    public void setStatecode(int statecode) {
        this.statecode = statecode;
    }

    public String getNha_id() {
        return nha_id;
    }

    public void setNha_id(String nha_id) {
        this.nha_id = nha_id;
    }

    public int getParam() {
        return param;
    }

    public void setParam(int param) {
        this.param = param;
    }

    static public VerifiedFamilyRequestModel create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, VerifiedFamilyRequestModel.class);
    }

    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
