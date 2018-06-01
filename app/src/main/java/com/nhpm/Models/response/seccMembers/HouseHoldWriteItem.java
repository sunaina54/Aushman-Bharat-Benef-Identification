package com.nhpm.Models.response.seccMembers;

import java.io.Serializable;

/**
 * Created by Dell3 on 31-05-2017.
 */
public class HouseHoldWriteItem implements Serializable{

    private String syncDt;
    private String appVersion;
    private String nhpsMemId;
    private String hhStatus;
    private String lockedStatus;
    private String nhpsId;
    private String syncedStatus;

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

    public String getNhpsMemId() {
        return nhpsMemId;
    }

    public void setNhpsMemId(String nhpsMemId) {
        this.nhpsMemId = nhpsMemId;
    }

    public String getHhStatus() {
        return hhStatus;
    }

    public void setHhStatus(String hhStatus) {
        this.hhStatus = hhStatus;
    }

    public String getLockedStatus() {
        return lockedStatus;
    }

    public void setLockedStatus(String lockedStatus) {
        this.lockedStatus = lockedStatus;
    }

    public String getNhpsId() {
        return nhpsId;
    }

    public void setNhpsId(String nhpsId) {
        this.nhpsId = nhpsId;
    }

    public String getSyncedStatus() {
        return syncedStatus;
    }

    public void setSyncedStatus(String syncedStatus) {
        this.syncedStatus = syncedStatus;
    }
}
