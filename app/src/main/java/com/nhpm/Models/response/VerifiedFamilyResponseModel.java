package com.nhpm.Models.response;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by SUNAINA on 10-08-2018.
 */

public class VerifiedFamilyResponseModel implements Serializable {
    private boolean status;
    private String operation;
    private  String errorMessage;
    private  String errorcode;
    private ArrayList<MemberListModel> familyMemberList;

    public ArrayList<MemberListModel> getFamilyMemberList() {
        return familyMemberList;
    }

    public String getErrorcode() {
        return errorcode;
    }

    public void setErrorcode(String errorcode) {
        this.errorcode = errorcode;
    }

    public void setFamilyMemberList(ArrayList<MemberListModel> familyMemberList) {
        this.familyMemberList = familyMemberList;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
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
