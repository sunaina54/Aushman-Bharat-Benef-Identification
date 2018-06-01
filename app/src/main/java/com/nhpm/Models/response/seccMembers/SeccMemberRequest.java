package com.nhpm.Models.response.seccMembers;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Anand on 21-11-2016.
 */
public class SeccMemberRequest implements Serializable {

    /*{
        "stateCode":"24",
            "distCode":"22",
            "tehsilCode":"001",
            "villTownCode":"0150",
            "wardCode":"0000",
            "blockCode":"0205"
    }*/

    private String stateCode;
    private String districtCode;
    private String tehsilCode;
    private String townCode;
    private String wardId;
    private String ahlBlockNo;
    private String ahlSubBlockNo;

    static public SeccMemberRequest create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, SeccMemberRequest.class);
    }

    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getDistCode() {
        return districtCode;
    }

    public void setDistCode(String distCode) {
        this.districtCode = distCode;
    }

    public String getTehsilCode() {
        return tehsilCode;
    }

    public void setTehsilCode(String tehsilCode) {
        this.tehsilCode = tehsilCode;
    }

    public String getVillTownCode() {
        return townCode;
    }

    public void setVillTownCode(String villTownCode) {
        this.townCode = villTownCode;
    }

    public String getWardCode() {
        return wardId;
    }

    public void setWardCode(String wardCode) {
        this.wardId = wardCode;
    }

    public String getBlockCode() {
        return ahlBlockNo;
    }

    public void setBlockCode(String blockCode) {
        this.ahlBlockNo = blockCode;
    }

    public String getAhlSubBlockNo() {
        return ahlSubBlockNo;
    }

    public void setAhlSubBlockNo(String ahlSubBlockNo) {
        this.ahlSubBlockNo = ahlSubBlockNo;
    }
}
