package com.nhpm.Models.request;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by SUNAINA on 29-06-2018.
 */

public class FamilyMatchScoreRequestModel implements Serializable {
    private String strFamilyNames1;
    private String strFamilyNames2;

    public String getStrFamilyNames1() {
        return strFamilyNames1;
    }

    public void setStrFamilyNames1(String strFamilyNames1) {
        this.strFamilyNames1 = strFamilyNames1;
    }

    public String getStrFamilyNames2() {
        return strFamilyNames2;
    }

    public void setStrFamilyNames2(String strFamilyNames2) {
        this.strFamilyNames2 = strFamilyNames2;
    }


    static public FamilyMatchScoreRequestModel create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, FamilyMatchScoreRequestModel.class);
    }

    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
