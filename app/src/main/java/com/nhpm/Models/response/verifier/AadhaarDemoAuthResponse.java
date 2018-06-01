package com.nhpm.Models.response.verifier;

import com.nhpm.Models.DemoAuthResp;
import com.nhpm.Models.response.GenericResponse;
import com.google.gson.Gson;

/**
 * Created by Saurabh on 28-08-2017.
 */

public class AadhaarDemoAuthResponse extends GenericResponse {

    private DemoAuthResp AuthRes;



    static public AadhaarDemoAuthResponse create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, AadhaarDemoAuthResponse.class);
    }

    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
    public DemoAuthResp getAuthRes() {
        return AuthRes;
    }

    public void setAuthRes(DemoAuthResp authRes) {
        AuthRes = authRes;
    }
}
