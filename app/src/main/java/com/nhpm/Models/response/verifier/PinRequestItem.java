package com.nhpm.Models.response.verifier;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Anand on 20-11-2016.
 */

/*
{
        "aadharNo":"399734158175",
        "pin":"1234",
        "userId":"1"
        }
*/
public class PinRequestItem implements Serializable {
    private String aadharNo;
    private String pin;
    private String userId;

    static public PinRequestItem create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, PinRequestItem.class);
    }

    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public String getAadharNo() {
        return aadharNo;
    }

    public void setAadharNo(String aadharNo) {
        this.aadharNo = aadharNo;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
