package com.nhpm.Models.request;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by SUNAINA on 14-06-2018.
 */

public class ValidateUrnRequestModel implements Serializable {
    private String urn;

    public String getUrn() {
        return urn;
    }

    public void setUrn(String urn) {
        this.urn = urn;
    }

    static public ValidateUrnRequestModel create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, ValidateUrnRequestModel.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
