package com.nhpm.ReqRespModels;

import com.nhpm.Networking.NetworkResponse;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by psqit on 8/31/2016.
 */
public class MasterNHSdata extends NetworkResponse {

    public List<NhsDataList> nhsDataList = new ArrayList<>();
    static public MasterNHSdata create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, MasterNHSdata.class);
    }

    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
