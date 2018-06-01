package com.nhpm.Models.request;

import com.google.gson.Gson;
import com.nhpm.Models.DemoAuthResp;

import java.io.Serializable;

/**
 * Created by SUNAINA on 19-05-2018.
 */

public class RegisterItem implements Serializable{
    private String registerMode;

    public String getRegisterMode() {
        return registerMode;
    }

    public void setRegisterMode(String registerMode) {
        this.registerMode = registerMode;
    }
    static public RegisterItem create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, RegisterItem.class);
    }

    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }

}
