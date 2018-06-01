package com.nhpm.Models.response.verifier;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Anand on 22-11-2016.
 */
public class MobileNumberOTPValidaationResponse implements Serializable{
    //{"sender":"9999341830","message":"Y","time":"2016-11-22 09:57:52:807","status":"1","otp":"714476","result":null,"txn":null,"iemiNo":null,"sequenceNo":"NHPS:2016-11-2209:57:16:984","houseHoldNo":null,"err":null}
    private String sender;
    private String message;
    private String status;
    static public MobileNumberOTPValidaationResponse create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, MobileNumberOTPValidaationResponse.class);
    }

    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }


    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
