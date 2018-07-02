package com.nhpm.Models;

import com.google.gson.Gson;
import com.nhpm.Models.response.AadhaarCaptureDetailItem;

import java.io.Serializable;

public class NameMatchScoreModelRequest implements Serializable {

        private String firstName;
        private String secondName;
        private String password="ZCbEJyPUlaQXo8fJT2P+5PAKJOs6emRZgdI/w5qkIrN2NqRUQQ3Sdqp+9WbS8P4j";
        private String userName="nhps_fvs^1&%mobile";

    static public NameMatchScoreModelRequest create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, NameMatchScoreModelRequest.class);
    }

    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
