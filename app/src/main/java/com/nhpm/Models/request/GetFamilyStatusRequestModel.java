package com.nhpm.Models.request;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by SUNAINA on 06-08-2018.
 */

public class GetFamilyStatusRequestModel implements Serializable {
    private String hhd_no;
    private int statecode;

    public String getHhd_no() {
        return hhd_no;
    }

    public void setHhd_no(String hhd_no) {
        this.hhd_no = hhd_no;
    }

    public int getStatecode() {
        return statecode;
    }

    public void setStatecode(int statecode) {
        this.statecode = statecode;
    }

    static public GetFamilyStatusRequestModel create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, GetFamilyStatusRequestModel.class);
    }

    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
