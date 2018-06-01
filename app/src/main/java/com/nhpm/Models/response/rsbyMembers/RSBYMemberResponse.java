package com.nhpm.Models.response.rsbyMembers;

import com.nhpm.Models.response.GenericResponse;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Anand on 05-11-2016.
 */
public class RSBYMemberResponse extends GenericResponse implements Serializable {

   private ArrayList<RSBYItem> rsbyList;



    static public RSBYMemberResponse create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, RSBYMemberResponse.class);
    }

    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
    public ArrayList<RSBYItem> getRsbyList() {
        return rsbyList;
    }

    public void setRsbyList(ArrayList<RSBYItem> rsbyList) {
        this.rsbyList = rsbyList;
    }
}
