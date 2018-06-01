package com.nhpm.Models.response.master;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by dell1 on 17-04-2017.
 */
public class ConfigurationItem implements Serializable {
    private String stateCode;
    private String configId;
    private String description;
    private String status;
    private String acceptedvalueName;
    private String stateName;

    static public ConfigurationItem create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, ConfigurationItem.class);
    }

    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public ConfigurationItem() {
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getConfigId() {
        return configId;
    }

    public void setConfigId(String configId) {
        this.configId = configId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAcceptedvalueName() {
        return acceptedvalueName;
    }

    public void setAcceptedvalueName(String acceptedvalueName) {
        this.acceptedvalueName = acceptedvalueName;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }
}
