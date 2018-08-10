package com.nhpm.Models.response;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by SUNAINA on 10-08-2018.
 */

public class VerifiedFamilyResponseModel extends GenericResponse implements Serializable {
    private ArrayList<MemberListModel> familyMemberList;

    public ArrayList<MemberListModel> getFamilyMemberList() {
        return familyMemberList;
    }

    public void setFamilyMemberList(ArrayList<MemberListModel> familyMemberList) {
        this.familyMemberList = familyMemberList;
    }

    static public VerifiedFamilyResponseModel create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, VerifiedFamilyResponseModel.class);
    }

    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
