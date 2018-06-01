package com.nhpm.Models.response.master.response;

import com.nhpm.Models.response.GenericResponse;
import com.nhpm.Models.response.master.AadhaarStatusItem;
import com.nhpm.Models.response.master.HealthSchemeItem;
import com.nhpm.Models.response.master.MemberStatusItem;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by PSQ on 12/23/2016.
 */

public class MasterResponseItem extends GenericResponse implements Serializable {
    private ArrayList<HealthSchemeItem> stateHelathSchemeList;
    private ArrayList<AadhaarStatusItem> aadhaarStatusList;
    private ArrayList<MemberStatusItem> memberStatusList;
    static public MasterResponseItem create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, MasterResponseItem.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public ArrayList<HealthSchemeItem> getStateHelathSchemeList() {
        return stateHelathSchemeList;
    }

    public void setStateHelathSchemeList(ArrayList<HealthSchemeItem> stateHelathSchemeList) {
        this.stateHelathSchemeList = stateHelathSchemeList;
    }

    public ArrayList<AadhaarStatusItem> getAadhaarStatusList() {
        return aadhaarStatusList;
    }

    public void setAadhaarStatusList(ArrayList<AadhaarStatusItem> aadhaarStatusList) {
        this.aadhaarStatusList = aadhaarStatusList;
    }

    public ArrayList<MemberStatusItem> getMemberStatusList() {
        return memberStatusList;
    }

    public void setMemberStatusList(ArrayList<MemberStatusItem> memberStatusList) {
        this.memberStatusList = memberStatusList;
    }
}
