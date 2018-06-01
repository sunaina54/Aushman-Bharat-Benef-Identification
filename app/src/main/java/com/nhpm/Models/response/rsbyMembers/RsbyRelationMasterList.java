package com.nhpm.Models.response.rsbyMembers;

import com.nhpm.Models.response.GenericResponse;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by Saurabh on 22-08-2017.
 */

public class RsbyRelationMasterList extends GenericResponse {

    private ArrayList<RsbyRelationItem> rsbyRelationList;



    static public RsbyRelationMasterList create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, RsbyRelationMasterList.class);
    }

    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public ArrayList<RsbyRelationItem> getRsbyRelationList() {
        return rsbyRelationList;
    }

    public void setRsbyRelationList(ArrayList<RsbyRelationItem> rsbyRelationList) {
        this.rsbyRelationList = rsbyRelationList;
    }
}
