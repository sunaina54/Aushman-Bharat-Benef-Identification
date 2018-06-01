package com.nhpm.Models.response;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Dell3 on 22-05-2017.
 */
public class UidData implements Serializable {
    String uid;
    String Pht;
    Poi Poi;
    Poa Poa;

/*    static public UidData create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, UidData.class);
    }

    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }*/

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public com.nhpm.Models.response.Poi getPoi() {
        return Poi;
    }

    public void setPoi(com.nhpm.Models.response.Poi poi) {
        Poi = poi;
    }

    public com.nhpm.Models.response.Poa getPoa() {
        return Poa;
    }

    public void setPoa(com.nhpm.Models.response.Poa poa) {
        Poa = poa;
    }

    public String getPht() {
        return Pht;
    }

    public void setPht(String pht) {
        Pht = pht;
    }
}
