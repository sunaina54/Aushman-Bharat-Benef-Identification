package com.nhpm.Models.response.rsbyMembers;

import com.nhpm.Models.response.GenericResponse;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by Saurabh on 22-08-2017.
 */

public class RsbyCardCategoryMasterList extends GenericResponse {

    private ArrayList<RsbyCardCategoryItem> rsbyCategoryList;

    static public RsbyCardCategoryMasterList create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, RsbyCardCategoryMasterList.class);
    }

    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public ArrayList<RsbyCardCategoryItem> getRsbyCategoryList() {
        return rsbyCategoryList;
    }

    public void setRsbyCategoryList(ArrayList<RsbyCardCategoryItem> rsbyCategoryList) {
        this.rsbyCategoryList = rsbyCategoryList;
    }
}
