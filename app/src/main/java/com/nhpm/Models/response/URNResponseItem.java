package com.nhpm.Models.response;

import com.google.gson.Gson;
import com.nhpm.Models.request.ValidateUrnRequestModel;

import java.io.Serializable;

/**
 * Created by SUNAINA on 14-06-2018.
 */

public class URNResponseItem implements Serializable {
    private String familyId;
    private String urnNo;
    private String relationName;
    private String districtName;
    private String stateName;
    private String memberName;
    private String villageName;
    private String memberId;



    public String getFamilyId() {
        return familyId;
    }

    public void setFamilyId(String familyId) {
        this.familyId = familyId;
    }

    public String getUrnNo() {
        return urnNo;
    }

    public void setUrnNo(String urnNo) {
        this.urnNo = urnNo;
    }

    public String getRelationName() {
        return relationName;
    }

    public void setRelationName(String relationName) {
        this.relationName = relationName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getVillageName() {
        return villageName;
    }

    public void setVillageName(String villageName) {
        this.villageName = villageName;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    static public URNResponseItem create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, URNResponseItem.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
