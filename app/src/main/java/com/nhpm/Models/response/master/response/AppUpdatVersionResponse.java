package com.nhpm.Models.response.master.response;

import com.nhpm.Models.response.GenericResponse;
import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by PSQ on 12/15/2016.
 */

//{"id":"1","versionCode":"1","versionName":"1.0.0","appName":"NHPS Vield verification","status":true,"operation":"Get Update Version"}

public class AppUpdatVersionResponse extends GenericResponse implements Serializable {
    private String id;
    private String versionCode;
    private String versionName;
    private String appName;
    private String description;
    private String releaseDate;

    static public AppUpdatVersionResponse create(String serializedData) {

        Gson gson = new Gson();
        return gson.fromJson(serializedData, AppUpdatVersionResponse.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
    public String getVersionCode() {
        return versionCode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }
}
