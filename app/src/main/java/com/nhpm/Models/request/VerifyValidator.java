package com.nhpm.Models.request;

import com.nhpm.Models.response.GenericResponse;
import com.google.gson.Gson;

/**
 * Created by Saurabh on 21-08-2017.
 */

public class VerifyValidator extends GenericResponse {

    private String aadhaarNumber;
    private String imei;
    private String pin;


    static public VerifyValidator create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, VerifyValidator.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
    public String getAadhaarNumber() {
        return aadhaarNumber;
    }

    public void setAadhaarNumber(String aadhaarNumber) {
        this.aadhaarNumber = aadhaarNumber;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }
}
