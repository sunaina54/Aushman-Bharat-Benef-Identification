package com.nhpm.Models;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Saurabh on 28-07-2017.
 */

public class DataCountRequest implements Serializable {

    private String stateCode;
    private String districtCode;
    private String tehsilCode;
    private String villageTownCode;
    private String wardCode;
    private String ahlBlockCode;
    private String ahlSubBlockCode;

    static public DataCountRequest create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, DataCountRequest.class);
    }

    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
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

    public String getVillageTownCode() {
        return villageTownCode;
    }

    public void setVillageTownCode(String villageTownCode) {
        this.villageTownCode = villageTownCode;
    }

    public String getWardCode() {
        return wardCode;
    }

    public void setWardCode(String wardCode) {
        this.wardCode = wardCode;
    }

    public String getAhlBlockCode() {
        return ahlBlockCode;
    }

    public void setAhlBlockCode(String ahlBlockCode) {
        this.ahlBlockCode = ahlBlockCode;
    }

    public String getAhlSubBlockCode() {
        return ahlSubBlockCode;
    }

    public void setAhlSubBlockCode(String ahlSubBlockCode) {
        this.ahlSubBlockCode = ahlSubBlockCode;
    }
}
