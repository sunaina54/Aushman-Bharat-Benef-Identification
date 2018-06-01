package com.nhpm.Models.response.seccMembers;

import com.nhpm.Models.response.GenericResponse;
import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by PSQ on 12/8/2016.
 */

/*"ahlTin": "43242200101500000020500031001",
        "hhStatus": "1",
        "nhpsId": "123456789009",
        "syncedStatus": "1",
        "status": true,
        "operation": "Update Household"*/

public class SyncHouseholdResponse extends GenericResponse implements Serializable{

    private String ahlTin;
    private String hhStatus;
    private String syncedStatus;
    private String nhpsId;
    private String syncDt;
    private String appVersion;
    private String nhpsMemId;


    static public SyncHouseholdResponse create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, SyncHouseholdResponse.class);
    }

    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getSyncDt() {
        return syncDt;
    }

    public void setSyncDt(String syncDt) {
        this.syncDt = syncDt;
    }

    public String getAhlTin() {
        return ahlTin;
    }

    public void setAhlTin(String ahlTin) {
        this.ahlTin = ahlTin;
    }

    public String getHhStatus() {
        return hhStatus;
    }

    public void setHhStatus(String hhStatus) {
        this.hhStatus = hhStatus;
    }

    public String getSyncedStatus() {
        return syncedStatus;
    }

    public void setSyncedStatus(String syncedStatus) {
        this.syncedStatus = syncedStatus;
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
}
