package com.nhpm.Models.response;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by SUNAINA on 31-05-2018.
 */

public class FamilyListResponseItem extends GenericResponse implements Serializable {

    private FamilyListResult result;

    public FamilyListResult getResult() {
        return result;
    }

    public void setResult(FamilyListResult result) {
        this.result = result;
    }

    static public FamilyListResponseItem create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, FamilyListResponseItem.class);
    }

    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
