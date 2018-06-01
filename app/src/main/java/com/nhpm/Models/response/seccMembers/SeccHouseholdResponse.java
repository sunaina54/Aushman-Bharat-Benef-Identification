package com.nhpm.Models.response.seccMembers;

import com.nhpm.Models.response.GenericResponse;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Anand on 03-11-2016.
 */
public class SeccHouseholdResponse extends GenericResponse implements Serializable {
    private ArrayList<HouseHoldItem> houseSeccReadList;
    private ArrayList<HouseHoldWriteItem> houseSeccWriteList;
    private ArrayList<HouseHoldItem> rsbyHouseholdReadList;


    public ArrayList<HouseHoldItem> getRsbyHouseholdReadList() {
        return rsbyHouseholdReadList;
    }

    public void setRsbyHouseholdReadList(ArrayList<HouseHoldItem> rsbyHouseholdReadList) {
        this.rsbyHouseholdReadList = rsbyHouseholdReadList;
    }

    public ArrayList<HouseHoldItem> getSeccHouseholdList() {
        return houseSeccReadList;
    }

    public void setSeccHouseholdList(ArrayList<HouseHoldItem> seccHouseholdList) {
        this.houseSeccReadList = seccHouseholdList;
    }

    public ArrayList<HouseHoldWriteItem> getHouseSeccWriteList() {
        return houseSeccWriteList;
    }

    public void setHouseSeccWriteList(ArrayList<HouseHoldWriteItem> houseSeccWriteList) {
        this.houseSeccWriteList = houseSeccWriteList;
    }

    static public SeccHouseholdResponse create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
       /* JSONObject jsonObj = new JSONObject(serializedData,"");*/
        return gson.fromJson(serializedData, SeccHouseholdResponse.class);
    }

    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
