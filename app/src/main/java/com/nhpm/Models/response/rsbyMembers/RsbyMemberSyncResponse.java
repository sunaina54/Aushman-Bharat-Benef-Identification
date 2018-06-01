package com.nhpm.Models.response.rsbyMembers;

import com.nhpm.Models.response.GenericResponse;
import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by PSQ on 3/7/2017.
 */

public class RsbyMemberSyncResponse extends GenericResponse implements Serializable {

    private String rsbymemid;
    private String nhpsId;
    private String nhpsMemId;
    private String syncDt;
    private String appVersion;
    private String syncedStatus;
    private String urnId;
    private String memId;


    static public RsbyMemberSyncResponse create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, RsbyMemberSyncResponse.class);
    }

    public String getSyncedStatus() {
        return syncedStatus;
    }

    public void setSyncedStatus(String syncedStatus) {
        this.syncedStatus = syncedStatus;
    }

    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public String getRsbymemid() {
        return rsbymemid;
    }

    public void setRsbymemid(String rsbymemid) {
        this.rsbymemid = rsbymemid;
    }

    public String getNhpsId() {
        return nhpsId;
    }

    public void setNhpsId(String nhpsId) {
        this.nhpsId = nhpsId;
    }

    public String getNhpsMemId() {
        return nhpsMemId;
    }

    public void setNhpsMemId(String nhpsMemId) {
        this.nhpsMemId = nhpsMemId;
    }

    public String getSyncdt() {
        return syncDt;
    }

    public void setSyncdt(String syncdt) {
        this.syncDt = syncdt;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }



    public String getUrnId() {
        return urnId;
    }

    public void setUrnId(String urnId) {
        this.urnId = urnId;
    }

    public String getMemId() {
        return memId;
    }

    public void setMemId(String memId) {
        this.memId = memId;
    }
}
