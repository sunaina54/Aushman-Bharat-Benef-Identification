package com.nhpm.Models.response;

import com.google.gson.Gson;
import com.nhpm.Models.request.GetFamilyStatusRequestModel;

import java.io.Serializable;

/**
 * Created by SUNAINA on 06-08-2018.
 */

public class FamilyMemberListItem implements Serializable {
    private String ahltin;
    private String verificationStatus;

    public String getAhltin() {
        return ahltin;
    }

    public void setAhltin(String ahltin) {
        this.ahltin = ahltin;
    }

    public String getVerificationStatus() {
        return verificationStatus;
    }

    public void setVerificationStatus(String verificationStatus) {
        this.verificationStatus = verificationStatus;
    }

    static public FamilyMemberListItem create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, FamilyMemberListItem.class);
    }

    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
