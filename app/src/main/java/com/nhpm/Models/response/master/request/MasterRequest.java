package com.nhpm.Models.response.master.request;

import com.nhpm.Models.response.master.response.HealthSchemeRequest;
import com.nhpm.Models.response.master.response.MasterResponseItem;
import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by PSQ on 12/23/2016.
 */

public class MasterRequest implements Serializable {
    private HealthSchemeRequest stateSchemeRequest;
    static public MasterRequest create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, MasterRequest.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public HealthSchemeRequest getStateSchemeRequest() {
        return stateSchemeRequest;
    }

    public void setStateSchemeRequest(HealthSchemeRequest stateSchemeRequest) {
        this.stateSchemeRequest = stateSchemeRequest;
    }
}
