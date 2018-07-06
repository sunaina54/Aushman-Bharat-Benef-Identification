package com.nhpm.Models.request;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by SUNAINA on 14-06-2018.
 */

public class MobileRationRequestModel implements Serializable {

    private String mobileRation="";
    private String selectedState="";
    private String param="";
    private String villageCode="";
    private String shh="";

    public String getVillageCode() {
        return villageCode;
    }

    public void setVillageCode(String villageCode) {
        this.villageCode = villageCode;
    }

    public String getShh() {
        return shh;
    }

    public void setShh(String shh) {
        this.shh = shh;
    }

    public String getMobileRation() {
        return mobileRation;
    }

    public void setMobileRation(String mobileRation) {
        this.mobileRation = mobileRation;
    }

    public String getSelectedState() {
        return selectedState;
    }

    public void setSelectedState(String selectedState) {
        this.selectedState = selectedState;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    static public MobileRationRequestModel create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, MobileRationRequestModel.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
