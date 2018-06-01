package com.nhpm.Models.response.verifier;

import java.io.Serializable;

/**
 * Created by Anand on 19-10-2016.
 */
public class VerifierDetail implements Serializable {
    public String userId,name,designation,mobileNo,imeiNo1,imeiNo2,gender,dob,macAddress,aadhaarNo,email,pin;

    public VerifierDetail(String userId, String name, String designation, String mobileNo, String imeiNo1, String imeiNo2, String gender,
                          String dob, String macAddress, String aadhaarNo) {
        this.userId = userId;
        this.name = name;
        this.designation = designation;
        this.mobileNo = mobileNo;
        this.imeiNo1 = imeiNo1;
        this.imeiNo2 = imeiNo2;
        this.gender = gender;
        this.dob = dob;
        this.macAddress = macAddress;
        this.aadhaarNo = aadhaarNo;
    }

    public VerifierDetail() {
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getImeiNo1() {
        return imeiNo1;
    }

    public void setImeiNo1(String imeiNo1) {
        this.imeiNo1 = imeiNo1;
    }

    public String getImeiNo2() {
        return imeiNo2;
    }

    public void setImeiNo2(String imeiNo2) {
        this.imeiNo2 = imeiNo2;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getAadhaarNo() {
        return aadhaarNo;
    }

    public void setAadhaarNo(String aadhaarNo) {
        this.aadhaarNo = aadhaarNo;
    }
}
