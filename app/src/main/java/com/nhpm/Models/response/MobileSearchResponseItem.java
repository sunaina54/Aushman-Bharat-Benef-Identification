package com.nhpm.Models.response;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by SUNAINA on 14-06-2018.
 */

public class MobileSearchResponseItem implements Serializable {
    private String village_code;
    private String family_status;
    private String districtName;
    private String blockName;
    private String mobile_no;
    private String shh_code;
    private String ahl_hh_id;
    private String vilageName;
    private String stateName;
    private String district_code;
    private String ration_card;
    private String block_code;
    private String stateCode;


    static public MobileSearchResponseItem create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, MobileSearchResponseItem.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public String getVillage_code() {
        return village_code;
    }

    public void setVillage_code(String village_code) {
        this.village_code = village_code;
    }

    public String getFamily_status() {
        return family_status;
    }

    public void setFamily_status(String family_status) {
        this.family_status = family_status;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getBlockName() {
        return blockName;
    }

    public void setBlockName(String blockName) {
        this.blockName = blockName;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
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

    public String getVilageName() {
        return vilageName;
    }

    public void setVilageName(String vilageName) {
        this.vilageName = vilageName;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getDistrict_code() {
        return district_code;
    }

    public void setDistrict_code(String district_code) {
        this.district_code = district_code;
    }

    public String getRation_card() {
        return ration_card;
    }

    public void setRation_card(String ration_card) {
        this.ration_card = ration_card;
    }

    public String getBlock_code() {
        return block_code;
    }

    public void setBlock_code(String block_code) {
        this.block_code = block_code;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }
}
