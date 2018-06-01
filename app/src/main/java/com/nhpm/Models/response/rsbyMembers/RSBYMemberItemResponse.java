package com.nhpm.Models.response.rsbyMembers;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Anand on 18-11-2016.
 */

/*
"statusCode": "1",
        "msg": "Success",
        "urnList": [
*/
public class RSBYMemberItemResponse implements Serializable {

    private String statusCode;
    private String msg;
    private ArrayList<RSBYMemberItem> urnList;

    static public RSBYMemberItemResponse create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, RSBYMemberItemResponse.class);
    }

    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }


    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ArrayList<RSBYMemberItem> getUrnList() {
        return urnList;
    }

    public void setUrnList(ArrayList<RSBYMemberItem> urnList) {
        this.urnList = urnList;
    }
}
