package com.nhpm.Models;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Saurabh on 09-10-2017.
 */

public class ApplicationDataModelList implements Serializable {

    private ArrayList<ApplicationDataModel> applicationDataModel;

    static public ApplicationDataModelList create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, ApplicationDataModelList.class);
    }

    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public ArrayList<ApplicationDataModel> getApplicationDataModel() {
        return applicationDataModel;
    }

    public void setApplicationDataModel(ArrayList<ApplicationDataModel> applicationDataModel) {
        this.applicationDataModel = applicationDataModel;
    }
}
