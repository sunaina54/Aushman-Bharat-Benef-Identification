package com.nhpm.Models.response.master.response;

import com.nhpm.Models.response.GenericResponse;
import com.nhpm.Models.response.master.MemberStatusItem;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Anand on 20-11-2016.
 */
public class MemberStatusItemResponse extends GenericResponse implements Serializable {
    private ArrayList<MemberStatusItem> memberStatusList;
    static public MemberStatusItemResponse create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, MemberStatusItemResponse.class);
    }

    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
    public ArrayList<MemberStatusItem> getMemberStatusList() {

        return memberStatusList;
    }

    public void setMemberStatusList(ArrayList<MemberStatusItem> memberStatusList) {
        this.memberStatusList = memberStatusList;
    }
}
