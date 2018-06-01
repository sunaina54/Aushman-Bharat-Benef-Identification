package com.nhpm.Models.response;

import com.google.gson.Gson;
import com.nhpm.Models.ApplicationDataModel;
import com.nhpm.Models.request.FamilyDetailItem;
import com.nhpm.Models.request.PersonalDetailItem;

import java.io.Serializable;

/**
 * Created by SUNAINA on 23-05-2018.
 */

public class BeneficiaryListItem implements Serializable {
    private String rashancardNo;
    private String mobileNo;
    private String ahlTin;
    private String urnNo;
    private String Name;
    private String FatherName;
    private String Gender;
    private String Age;
    private String Address;
    private PersonalDetailItem personalDetail;
    private FamilyDetailItem familyDetail;

    static public BeneficiaryListItem create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, BeneficiaryListItem.class);
    }

    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public PersonalDetailItem getPersonalDetail() {
        return personalDetail;
    }

    public void setPersonalDetail(PersonalDetailItem personalDetail) {
        this.personalDetail = personalDetail;
    }

    public FamilyDetailItem getFamilyDetail() {
        return familyDetail;
    }

    public void setFamilyDetail(FamilyDetailItem familyDetail) {
        this.familyDetail = familyDetail;
    }

    public String getRashancardNo() {
        return rashancardNo;
    }

    public void setRashancardNo(String rashancardNo) {
        this.rashancardNo = rashancardNo;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getAhlTin() {
        return ahlTin;
    }

    public void setAhlTin(String ahlTin) {
        this.ahlTin = ahlTin;
    }

    public String getUrnNo() {
        return urnNo;
    }

    public void setUrnNo(String urnNo) {
        this.urnNo = urnNo;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getFatherName() {
        return FatherName;
    }

    public void setFatherName(String fatherName) {
        FatherName = fatherName;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getAge() {
        return Age;
    }

    public void setAge(String age) {
        Age = age;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }
}
