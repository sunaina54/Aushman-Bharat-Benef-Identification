package com.nhpm.Models.response;

import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by Saurabh on 27-07-2017.
 */

public class ApplicationConfigListModel extends GenericResponse {

private ArrayList<ApplicationConfigurationModel> appStateConfigList;


    static public ApplicationConfigListModel create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, ApplicationConfigListModel.class);
    }

    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }


    public ArrayList<ApplicationConfigurationModel> getAppStateConfigList() {
        return appStateConfigList;
    }

    public void setAppStateConfigList(ArrayList<ApplicationConfigurationModel> appStateConfigList) {
        this.appStateConfigList = appStateConfigList;
    }
}
