package com.nhpm.Models.response.master;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by dell1 on 18-04-2017.
 */
public class StateItem implements Serializable {
    private String stateCode;
    private String stateName;
    private String status;
    private String loginType;

    static public StateItem create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, StateItem.class);
    }

    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public StateItem() {
    }

    public StateItem(String stateCode, String stateName) {
        this.stateCode = stateCode;
        this.stateName = stateName;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLogin_type() {
        return loginType;
    }

    public void setLogin_type(String login_type) {
        this.loginType = login_type;
    }
}
