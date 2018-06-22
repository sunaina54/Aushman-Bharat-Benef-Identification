package com.nhpm.Models;

import com.google.gson.Gson;
import com.nhpm.Models.request.FamilyDetailsItemModel;
import com.nhpm.Models.response.FamilyListResponseItem;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by SUNAINA on 21-06-2018.
 */

public class FamilyCardList implements Serializable {
    private ArrayList<FamilyDetailsItemModel> familyList;

    public ArrayList<FamilyDetailsItemModel> getFamilyList() {
        return familyList;
    }

    public void setFamilyList(ArrayList<FamilyDetailsItemModel> familyList) {
        this.familyList = familyList;
    }

    static public FamilyCardList create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, FamilyCardList.class);
    }

    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
