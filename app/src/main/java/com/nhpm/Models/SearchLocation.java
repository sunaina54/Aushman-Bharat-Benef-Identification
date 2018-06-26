package com.nhpm.Models;

import com.google.gson.Gson;
import com.nhpm.Models.response.seccMembers.SeccHouseholdResponse;

import java.io.Serializable;

/**
 * Created by SUNAINA on 26-06-2018.
 */

public class SearchLocation implements Serializable {
    private String vilageName="";
    private String distName="";
    private boolean isDistTrue;
    private boolean isVillageTrue;
    static public SearchLocation create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
       /* JSONObject jsonObj = new JSONObject(serializedData,"");*/
        return gson.fromJson(serializedData, SearchLocation.class);
    }

    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
    public String getVilageName() {
        return vilageName;
    }

    public void setVilageName(String vilageName) {
        this.vilageName = vilageName;
    }

    public String getDistName() {
        return distName;
    }

    public void setDistName(String distName) {
        this.distName = distName;
    }

    public boolean isDistTrue() {
        return isDistTrue;
    }

    public void setDistTrue(boolean distTrue) {
        isDistTrue = distTrue;
    }

    public boolean isVillageTrue() {
        return isVillageTrue;
    }

    public void setVillageTrue(boolean villageTrue) {
        isVillageTrue = villageTrue;
    }
}
