package com.nhpm.Models.request;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by SUNAINA on 13-06-2018.
 */

public class GetMemberDetailRequestModel implements Serializable {
    private String ahl_tin;
    private String hhd_no;
    private int statecode;


    public String getAhl_tin() {
        return ahl_tin;
    }

    public void setAhl_tin(String ahl_tin) {
        this.ahl_tin = ahl_tin;
    }

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

    static public GetMemberDetailRequestModel create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, GetMemberDetailRequestModel.class);
    }

    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
