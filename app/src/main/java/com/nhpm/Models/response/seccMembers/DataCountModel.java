package com.nhpm.Models.response.seccMembers;

import com.nhpm.Models.response.GenericResponse;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Saurabh on 07-06-2017.
 */

public class DataCountModel extends GenericResponse implements Serializable  {

private ArrayList<DataCountObject> downloadCountList;


    static public DataCountModel create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, DataCountModel.class);
    }

    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public ArrayList<DataCountObject> getDownloadCountList() {
        return downloadCountList;
    }

    public void setDownloadCountList(ArrayList<DataCountObject> downloadCountList) {
        this.downloadCountList = downloadCountList;
    }
}
