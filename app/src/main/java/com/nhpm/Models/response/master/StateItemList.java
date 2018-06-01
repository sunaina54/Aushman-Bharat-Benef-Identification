package com.nhpm.Models.response.master;

import com.nhpm.Models.response.GenericResponse;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by Saurabh on 18-08-2017.
 */

public class StateItemList extends GenericResponse {

    private ArrayList<StateItem> stateList;

    public ArrayList<StateItem> getStateItemList() {
        return stateList;
    }

    public void setStateItemList(ArrayList<StateItem> stateItemList) {
        this.stateList = stateItemList;
    }

    static public StateItemList create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, StateItemList.class);
    }

    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
