package com.nhpm.Models.response.master.response;

import com.nhpm.Models.response.GenericResponse;
import com.nhpm.Models.response.master.AadhaarStatusItem;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Anand on 20-11-2016.
 */
public class AadhaarStatusItemResponse extends GenericResponse implements Serializable {

    private ArrayList<AadhaarStatusItem> aadhaarStatusList;

    static public AadhaarStatusItemResponse create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, AadhaarStatusItemResponse.class);
    }

    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public ArrayList<AadhaarStatusItem> getAadhaarStatusList() {
        return aadhaarStatusList;
    }

    public void setAadhaarStatusList(ArrayList<AadhaarStatusItem> aadhaarStatusList) {
        this.aadhaarStatusList = aadhaarStatusList;
    }
}
