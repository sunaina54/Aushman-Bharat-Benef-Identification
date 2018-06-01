package com.nhpm.Models.response.master;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Anand on 20-11-2016.
 */

/*{
        "statecode":"24"
        }*/
public class HealthSchemeItem implements Serializable {
    /*    "statecode": "24",
                "schemeId": "001",
                "schemeName": "Mukhyamantri Amrutam"*/
    private String stateCode;
    private String schemeId;
    private String schemeName;

    public HealthSchemeItem() {
    }

    public HealthSchemeItem(String statecode, String schemeId, String schemeName) {
        this.stateCode = statecode;
        this.schemeId = schemeId;
        this.schemeName = schemeName;
    }

    static public HealthSchemeItem create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, HealthSchemeItem.class);
    }

    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public String getStatecode() {
        return stateCode;
    }

    public void setStatecode(String statecode) {
        this.stateCode = statecode;
    }

    public String getSchemeId() {
        return schemeId;
    }

    public void setSchemeId(String schemeId) {
        this.schemeId = schemeId;
    }

    public String getSchemeName() {
        return schemeName;
    }

    public void setSchemeName(String schemeName) {
        this.schemeName = schemeName;
    }
}
