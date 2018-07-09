package com.nhpm.Models.request;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by SUNAINA on 09-07-2018.
 */

public class GetSearchParaRequestModel implements Serializable {
    private String user_id="";
    private String type_of_search="";
    private String uid_search_type="";
    private String state_code="";
    private String district_code="";
    private String ahl_tin="";
    private String type_of_doc="";
    private String tid="";
    private Long startTime;
    private Long endTime;
    private String source="";


    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getType_of_search() {
        return type_of_search;
    }

    public void setType_of_search(String type_of_search) {
        this.type_of_search = type_of_search;
    }

    public String getUid_search_type() {
        return uid_search_type;
    }

    public void setUid_search_type(String uid_search_type) {
        this.uid_search_type = uid_search_type;
    }

    public String getState_code() {
        return state_code;
    }

    public void setState_code(String state_code) {
        this.state_code = state_code;
    }

    public String getDistrict_code() {
        return district_code;
    }

    public void setDistrict_code(String district_code) {
        this.district_code = district_code;
    }

    public String getAhl_tin() {
        return ahl_tin;
    }

    public void setAhl_tin(String ahl_tin) {
        this.ahl_tin = ahl_tin;
    }

    public String getType_of_doc() {
        return type_of_doc;
    }

    public void setType_of_doc(String type_of_doc) {
        this.type_of_doc = type_of_doc;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    static public GetSearchParaRequestModel create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, GetSearchParaRequestModel.class);
    }

    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
