package com.nhpm.Models.response.master;

import com.nhpm.Models.response.GenericResponse;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by Saurabh on 11-08-2017.
 */

public class RelationMasterItem extends GenericResponse {

    private ArrayList<MemberRelationItem> relationMasterData;


    static public RelationMasterItem create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, RelationMasterItem.class);
    }

    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }


    public ArrayList<MemberRelationItem> getRelationMasterData() {
        return relationMasterData;
    }

    public void setRelationMasterData(ArrayList<MemberRelationItem> relationMasterData) {
        this.relationMasterData = relationMasterData;
    }
}
