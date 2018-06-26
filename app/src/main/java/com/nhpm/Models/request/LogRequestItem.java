package com.nhpm.Models.request;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by SUNAINA on 25-06-2018.
 */

public class LogRequestItem implements Serializable{
    private String tid;
    private String operatorheader;
    private String pagescreenname;
    private String sequence;
    private String attempt;
    private String action;
    private String operatorinput;
    private String operatoroutput;
    private String subpageinput;
    private String subpageoutput;
    private String source="Mobile";
    private String error;
    private String created_by;
    private String creation_date;

    static public LogRequestItem create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, LogRequestItem.class);
    }

    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getOperatorheader() {
        return operatorheader;
    }

    public void setOperatorheader(String operatorheader) {
        this.operatorheader = operatorheader;
    }

    public String getPagescreenname() {
        return pagescreenname;
    }

    public void setPagescreenname(String pagescreenname) {
        this.pagescreenname = pagescreenname;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public String getAttempt() {
        return attempt;
    }

    public void setAttempt(String attempt) {
        this.attempt = attempt;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getOperatorinput() {
        return operatorinput;
    }

    public void setOperatorinput(String operatorinput) {
        this.operatorinput = operatorinput;
    }

    public String getOperatoroutput() {
        return operatoroutput;
    }

    public void setOperatoroutput(String operatoroutput) {
        this.operatoroutput = operatoroutput;
    }

    public String getSubpageinput() {
        return subpageinput;
    }

    public void setSubpageinput(String subpageinput) {
        this.subpageinput = subpageinput;
    }

    public String getSubpageoutput() {
        return subpageoutput;
    }

    public void setSubpageoutput(String subpageoutput) {
        this.subpageoutput = subpageoutput;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(String creation_date) {
        this.creation_date = creation_date;
    }
}
