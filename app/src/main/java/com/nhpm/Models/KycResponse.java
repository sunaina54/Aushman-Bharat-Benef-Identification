package com.nhpm.Models;

import com.nhpm.Models.response.AadharDataModel;
import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Saurabh on 23-05-2017.
 */

public class KycResponse implements Serializable {

    private AadharDataModel KycRes;

    static public KycResponse create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, KycResponse.class);
    }

    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }


    public AadharDataModel getKycRes() {
        return KycRes;
    }

    public void setKycRes(AadharDataModel kycRes) {
        KycRes = kycRes;
    }
}
