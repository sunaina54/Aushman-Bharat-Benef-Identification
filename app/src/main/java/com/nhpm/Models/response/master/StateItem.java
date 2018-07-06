package com.nhpm.Models.response.master;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by dell1 on 18-04-2017.
 */
public class StateItem implements Serializable {
    private String state_code;
    private String state_name;
    private String status;
    private String login_type;

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
        this.state_code = stateCode;
        this.state_name = stateName;
    }

    public String getStateCode() {
        return state_code;
    }

    public void setStateCode(String stateCode) {
        this.state_code = stateCode;
    }

    public String getStateName() {
        return state_name;
    }

    public void setStateName(String stateName) {
        this.state_name = stateName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLogin_type() {
        return login_type;
    }

    public void setLogin_type(String login_type) {
        this.login_type = login_type;
    }
}
