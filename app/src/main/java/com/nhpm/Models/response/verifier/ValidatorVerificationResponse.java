package com.nhpm.Models.response.verifier;

import com.nhpm.Models.request.VerifyValidator;
import com.nhpm.Models.response.GenericResponse;
import com.google.gson.Gson;

/**
 * Created by Saurabh on 21-08-2017.
 */

public class ValidatorVerificationResponse extends GenericResponse {

    private String userStatus;
    private String name;
    private String role;



    static public ValidatorVerificationResponse create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, ValidatorVerificationResponse.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
