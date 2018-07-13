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
    private String correctIncorrectFamilyStatus="";
    private String transactionId="";
    private String mobile="";
    private String source= "";
    private String finalSave="";

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
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
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
