package com.nhpm.Models.request;

import com.nhpm.Models.response.GenericResponse;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by Saurabh on 06-07-2017.
 */

public class NotificationRequestItem extends GenericResponse {

    private ArrayList<TargetStateCodeItem> targetStateCodeList;
    private String nationalCode;


    static public NotificationRequestItem create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, NotificationRequestItem.class);
    }

    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }


    public ArrayList<TargetStateCodeItem> getTargetStateCodeList() {
        return targetStateCodeList;
    }

    public void setTargetStateCodeList(ArrayList<TargetStateCodeItem> targetStateCodeList) {
        this.targetStateCodeList = targetStateCodeList;
    }

    public String getNationalCode() {
        return nationalCode;
    }

    public void setNationalCode(String nationalCode) {
        this.nationalCode = nationalCode;
    }
}
