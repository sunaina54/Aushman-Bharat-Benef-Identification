package com.nhpm.Models.response;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by SUNAINA on 07-08-2018.
 */

public class MemberListModel implements Serializable {
    private String name="Nitin";
    private String nhaId="POC4FGXZU";
    private String ahltin;
    private String verificationStatus;
    private String benPhoto;
    private String gender;
    private String yob;
    private String fatherName;
    private String motherName;
    private String spouseName;
    private String hhidType; // RSBY/SECC
    private String familyIdNumber;
    private String familyIdType;
    private String hhdNo;



    /*{
        "ahltin": null,
            "verificationStatus": null,
            "nhaId": "4XWKOPX",
            "benPhoto": null,
            "name": "Naresh",
            "gender": "1",
            "yob": "1970",
            "fatherName": "S/O: ABC",
            "motherName": null,
            "spouseName": null,
            "hhidType": null,
            "familyIdNumber": "q32425356363464564644",
            "familyIdType": "1"
    }*/

    public String getHhdNo() {
        return hhdNo;
    }

    public void setHhdNo(String hhdNo) {
        this.hhdNo = hhdNo;
    }

    public String getAhltin() {
        return ahltin;
    }

    public void setAhltin(String ahltin) {
        this.ahltin = ahltin;
    }

    public String getVerificationStatus() {
        return verificationStatus;
    }

    public void setVerificationStatus(String verificationStatus) {
        this.verificationStatus = verificationStatus;
    }

    public String getBenPhoto() {
        return benPhoto;
    }

    public void setBenPhoto(String benPhoto) {
        this.benPhoto = benPhoto;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getYob() {
        return yob;
    }

    public void setYob(String yob) {
        this.yob = yob;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getMotherName() {
        return motherName;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    public String getSpouseName() {
        return spouseName;
    }

    public void setSpouseName(String spouseName) {
        this.spouseName = spouseName;
    }

    public String getHhidType() {
        return hhidType;
    }

    public void setHhidType(String hhidType) {
        this.hhidType = hhidType;
    }

    public String getFamilyIdNumber() {
        return familyIdNumber;
    }

    public void setFamilyIdNumber(String familyIdNumber) {
        this.familyIdNumber = familyIdNumber;
    }

    public String getFamilyIdType() {
        return familyIdType;
    }

    public void setFamilyIdType(String familyIdType) {
        this.familyIdType = familyIdType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return nhaId;
    }

    public void setId(String id) {
        this.nhaId = id;
    }

    static public MemberListModel create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, MemberListModel.class);
    }

    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
