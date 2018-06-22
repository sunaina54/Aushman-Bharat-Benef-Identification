package com.nhpm.Models.response;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by SUNAINA on 18-06-2018.
 */

public class LoginOTPResponseModel extends GenericResponse implements Serializable {
    private String transactionid;

    public String getTransactionid() {
        return transactionid;
    }

    public void setTransactionid(String transactionid) {
        this.transactionid = transactionid;
    }

    static public LoginOTPResponseModel create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, LoginOTPResponseModel.class);
    }

    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
