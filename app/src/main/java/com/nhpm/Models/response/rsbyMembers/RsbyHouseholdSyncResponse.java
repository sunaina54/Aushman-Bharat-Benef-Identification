package com.nhpm.Models.response.rsbyMembers;

import com.nhpm.Models.response.GenericResponse;
import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by PSQ on 3/7/2017.
 */

public class RsbyHouseholdSyncResponse extends GenericResponse implements Serializable {
            private String rsbymemid;
            private String syncedStatus;
            private String nhpsId;
            private String syncDt;
            private String appVersion;

    static public RsbyHouseholdSyncResponse create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, RsbyHouseholdSyncResponse.class);
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

    public String getSyncDt() {
        return syncDt;
    }

    public void setSyncDt(String syncDt) {
        this.syncDt = syncDt;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }
}
