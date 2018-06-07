package com.nhpm.Models.request;

import com.google.gson.Gson;
import com.nhpm.Models.FamilyMemberModel;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by SUNAINA on 07-06-2018.
 */

public class FamilyDetailsItemModel implements Serializable {
    private String idImage;
    private String idNumber;
    private String idType;

    private ArrayList<FamilyMemberModel> familyMemberModels;

    public ArrayList<FamilyMemberModel> getFamilyMemberModels() {
        return familyMemberModels;
    }

    public void setFamilyMemberModels(ArrayList<FamilyMemberModel> familyMemberModels) {
        this.familyMemberModels = familyMemberModels;
    }

    public String getIdImage() {
        return idImage;
    }

    public void setIdImage(String idImage) {
        this.idImage = idImage;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    static public FamilyDetailsItemModel create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, FamilyDetailsItemModel.class);
    }

    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
