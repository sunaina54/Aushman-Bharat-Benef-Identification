package com.nhpm.Models.response.verifier;

import com.nhpm.Models.response.GenericResponse;
import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Anand on 20-11-2016.
 */
/*{
        "aadhaarNumber": "399734158175",
        "userId": "1",
        "pin": "1234",
        "status": true,
        "operation": "update pin"
        }*/
public class UpdatePinResponse extends GenericResponse implements Serializable {

    private String aadhaarNo;
    private String userId;
    private String pin;

    static public UpdatePinResponse create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, UpdatePinResponse.class);
    }

    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public String getAadhaarNumber() {
        return aadhaarNo;
    }

    public void setAadhaarNumber(String aadhaarNo) {
        this.aadhaarNo = aadhaarNo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }
}
