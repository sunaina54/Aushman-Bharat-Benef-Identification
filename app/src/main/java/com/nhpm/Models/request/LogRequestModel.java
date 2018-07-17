package com.nhpm.Models.request;

import com.google.gson.Gson;
import com.nhpm.Utility.AppConstant;

import java.io.Serializable;

/**
 * Created by SUNAINA on 11-07-2018.
 */

public class LogRequestModel implements Serializable {
    private String method="";
    private String subMethod="";
    private String searchParameter="";
    private String result="";
    private String hhId="";
    private String ahl_tin="";
    private String correctIncorrectFamilyStatus="Family not found";
    private String tid="";
    private String user_id="";
    private String source= "";
    private String finalSave="";
    private String memberId="";

    private String district_code="";
    private Long endTime;
    private Long startTime;
    private String state_code="";
    private String type_of_doc="0";
    private String type_of_search="";
    private String isaadhar="";


    public String getDistrict_code() {
        return district_code;
    }

    public void setDistrict_code(String district_code) {
        this.district_code = district_code;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public String getState_code() {
        return state_code;
    }

    public void setState_code(String state_code) {
        this.state_code = state_code;
    }

    public String getType_of_doc() {
        return type_of_doc;
    }

    public void setType_of_doc(String type_of_doc) {
        this.type_of_doc = type_of_doc;
    }

    public String getType_of_search() {
        return type_of_search;
    }

    public void setType_of_search(String type_of_search) {
        this.type_of_search = type_of_search;
    }

    public String getIsaadhar() {
        return isaadhar;
    }

    public void setIsaadhar(String isaadhar) {
        this.isaadhar = isaadhar;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getFinalSave() {
        return finalSave;
    }

    public void setFinalSave(String finalSave) {
        this.finalSave = finalSave;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTransactionId() {
        return tid;
    }

    public void setTransactionId(String transactionId) {
        this.tid = transactionId;
    }

    public String getMobile() {
        return user_id;
    }

    public void setMobile(String mobile) {
        this.user_id = mobile;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getSubMethod() {
        return subMethod;
    }

    public void setSubMethod(String subMethod) {
        this.subMethod = subMethod;
    }

    public String getSearchParameter() {
        return searchParameter;
    }

    public void setSearchParameter(String searchParameter) {
        this.searchParameter = searchParameter;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getHhId() {
        return hhId;
    }

    public void setHhId(String hhId) {
        this.hhId = hhId;
    }

    public String getAhl_tin() {
        return ahl_tin;
    }

    public void setAhl_tin(String ahl_tin) {
        this.ahl_tin = ahl_tin;
    }

    public String getCorrectIncorrectFamilyStatus() {
        return correctIncorrectFamilyStatus;
    }

    public void setCorrectIncorrectFamilyStatus(String correctIncorrectFamilyStatus) {
        this.correctIncorrectFamilyStatus = correctIncorrectFamilyStatus;
    }

    static public LogRequestModel create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, LogRequestModel.class);
    }

    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
