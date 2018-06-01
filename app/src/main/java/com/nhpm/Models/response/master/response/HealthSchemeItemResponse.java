package com.nhpm.Models.response.master.response;

import com.nhpm.Models.response.GenericResponse;
import com.nhpm.Models.response.master.HealthSchemeItem;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Anand on 21-11-2016.
 */
public class HealthSchemeItemResponse extends GenericResponse implements Serializable {
    private ArrayList<HealthSchemeItem> stateHelathSchemeList;

    static public HealthSchemeItemResponse create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, HealthSchemeItemResponse.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public ArrayList<HealthSchemeItem> getStateHelathSchemeList() {
        return stateHelathSchemeList;
    }

    public void setStateHelathSchemeList(ArrayList<HealthSchemeItem> stateHelathSchemeList) {
        this.stateHelathSchemeList = stateHelathSchemeList;
    }
}
