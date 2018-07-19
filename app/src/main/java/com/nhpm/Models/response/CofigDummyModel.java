package com.nhpm.Models.response;

import com.google.gson.Gson;
import com.nhpm.Models.response.master.ConfigurationItem;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by SUNAINA on 18-07-2018.
 */

public class CofigDummyModel implements Serializable {
    private ArrayList<ConfigurationItem> configurationItems;

    public ArrayList<ConfigurationItem> getConfigurationItems() {
        return configurationItems;
    }

    public void setConfigurationItems(ArrayList<ConfigurationItem> configurationItems) {
        this.configurationItems = configurationItems;
    }

    static public CofigDummyModel create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, CofigDummyModel.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
