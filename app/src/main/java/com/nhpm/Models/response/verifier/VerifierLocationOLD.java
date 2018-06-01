package com.nhpm.Models.response.verifier;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Anand on 01-11-2016.
 */
public class VerifierLocationOLD implements Serializable {
    /*state_code character varying(2),
    district_code character varying(2),
    tehsil_code character varying(3),
    vt_code character varying(4),
    ward_code character varying(4),
    block_code character varying(4),
    aadhar_no character varying(12),*/

    private String id;
    private String stateCode;
    private String districtCode;
    private String tehsilCode;
    private String villTownCode;
    private String wardCode;
    private String blockCode;
    private String aadhaarNo;
    private String stateName;
    private String distName;
    private String tehsilName;
    private String villTownName;
    private String region;

    public VerifierLocationOLD() {
    }

    public VerifierLocationOLD(String stateCode, String districtCode,
                               String tehsilCode, String villTownCode, String wardCode, String blockCode, String aadhaarNo) {
        this.stateCode = stateCode;
        this.districtCode = districtCode;
        this.tehsilCode = tehsilCode;
        this.villTownCode = villTownCode;
        this.wardCode = wardCode;
        this.blockCode = blockCode;
        this.aadhaarNo = aadhaarNo;
    }
    static public VerifierLocationOLD create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, VerifierLocationOLD.class);
    }

    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getDistName() {
        return distName;
    }

    public void setDistName(String distName) {
        this.distName = distName;
    }

    public String getTehsilName() {
        return tehsilName;
    }

    public void setTehsilName(String tehsilName) {
        this.tehsilName = tehsilName;
    }

    public String getVillTownName() {
        return villTownName;
    }

    public void setVillTownName(String villTownName) {
        this.villTownName = villTownName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    public String getTehsilCode() {
        return tehsilCode;
    }

    public void setTehsilCode(String tehsilCode) {
        this.tehsilCode = tehsilCode;
    }

    public String getVillTownCode() {
        return villTownCode;
    }

    public void setVillTownCode(String villTownCode) {
        this.villTownCode = villTownCode;
    }

    public String getWardCode() {
        return wardCode;
    }

    public void setWardCode(String wardCode) {
        this.wardCode = wardCode;
    }

    public String getBlockCode() {
        return blockCode;
    }

    public void setBlockCode(String blockCode) {
        this.blockCode = blockCode;
    }

    public String getAadhaarNo() {
        return aadhaarNo;
    }

    public void setAadhaarNo(String aadhaarNo) {
        this.aadhaarNo = aadhaarNo;
    }
}
