package com.nhpm.Models.response;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by SUNAINA on 16-07-2018.
 */

public class ADCDDataItem implements Serializable {
    private String state_code;
    private String district_code;
    private String block_code;
    private String village_mdds;
    private String shh_code;
    private String ahl_hh_id;
    private String family_status;
    private String family_head;
    private String rationcard_number;
    private String mobile_number;
    private String no_of_family_members;
    private String rsby_urn;
    private String msby_urn;
    private String labour_reg_no;


    static public ADCDDataItem create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, ADCDDataItem.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public String getState_code() {
        return state_code;
    }

    public void setState_code(String state_code) {
        this.state_code = state_code;
    }

    public String getDistrict_code() {
        return district_code;
    }

    public void setDistrict_code(String district_code) {
        this.district_code = district_code;
    }

    public String getBlock_code() {
        return block_code;
    }

    public void setBlock_code(String block_code) {
        this.block_code = block_code;
    }

    public String getVillage_mdds() {
        return village_mdds;
    }

    public void setVillage_mdds(String village_mdds) {
        this.village_mdds = village_mdds;
    }

    public String getShh_code() {
        return shh_code;
    }

    public void setShh_code(String shh_code) {
        this.shh_code = shh_code;
    }

    public String getAhl_hh_id() {
        return ahl_hh_id;
    }

    public void setAhl_hh_id(String ahl_hh_id) {
        this.ahl_hh_id = ahl_hh_id;
    }

    public String getFamily_status() {
        return family_status;
    }

    public void setFamily_status(String family_status) {
        this.family_status = family_status;
    }

    public String getFamily_head() {
        return family_head;
    }

    public void setFamily_head(String family_head) {
        this.family_head = family_head;
    }

    public String getRationcard_number() {
        return rationcard_number;
    }

    public void setRationcard_number(String rationcard_number) {
        this.rationcard_number = rationcard_number;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public String getNo_of_family_members() {
        return no_of_family_members;
    }

    public void setNo_of_family_members(String no_of_family_members) {
        this.no_of_family_members = no_of_family_members;
    }

    public String getRsby_urn() {
        return rsby_urn;
    }

    public void setRsby_urn(String rsby_urn) {
        this.rsby_urn = rsby_urn;
    }

    public String getMsby_urn() {
        return msby_urn;
    }

    public void setMsby_urn(String msby_urn) {
        this.msby_urn = msby_urn;
    }

    public String getLabour_reg_no() {
        return labour_reg_no;
    }

    public void setLabour_reg_no(String labour_reg_no) {
        this.labour_reg_no = labour_reg_no;
    }
}
