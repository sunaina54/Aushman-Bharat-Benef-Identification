package com.nhpm.Models.response;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by SUNAINA on 06-08-2018.
 */

public class GetFamilyStatusResponseModel extends GenericResponse implements Serializable {
private ArrayList<FamilyMemberListItem> familyMemberList;

    public ArrayList<FamilyMemberListItem> getFamilyMemberList() {
        return familyMemberList;
    }

    public void setFamilyMemberList(ArrayList<FamilyMemberListItem> familyMemberList) {
        this.familyMemberList = familyMemberList;
    }

    static public GetFamilyStatusResponseModel create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, GetFamilyStatusResponseModel.class);
    }

    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
