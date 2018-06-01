package com.nhpm.Models.response.master.response;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Anand on 21-11-2016.
 */

/*{
        "statecode":"24"
        }*/
public class HealthSchemeRequest implements Serializable {
    private String stateCode;
    static public HealthSchemeRequest create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, HealthSchemeRequest.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
    public String getStatecode() {
        return stateCode;
    }
    public void setStatecode(String statecode) {
        this.stateCode = statecode;
    }
}
