package com.nhpm.Models.response.verifier;

import java.io.Serializable;

/**
 * Created by Anand on 19-10-2016.
 */

/*"aadhaarNumber": "399734158175",
        "name": "Santosh Kumar",
        "designation": "verifier",
        "mobileNumber": "9910412952",
        "imeiNumber1": "352356078907011",
        "imeiNumber2": "352356078907029",
        "gender": "1",
        "email": "santosh.abhijit19@gmail.com",
        "dob": "",
        "userId": "1",
        "pin": "1234",*/
public class VerifierItem implements Serializable {
    private String aadhaarNumber;
    private String name;
    private String designation;
    private String mobileNumber;
    private String imeiNumber1;
    private String gender;
    private String imeiNumber2;
    private String email;
    private String dob;
    private String userId;
    private String pin;

    public String getAadhaarNumber() {
        return aadhaarNumber;
    }

    public void setAadhaarNumber(String aadhaarNumber) {
        this.aadhaarNumber = aadhaarNumber;
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

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getImeiNumber1() {
        return imeiNumber1;
    }

    public void setImeiNumber1(String imeiNumber1) {
        this.imeiNumber1 = imeiNumber1;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getImeiNumber2() {
        return imeiNumber2;
    }

    public void setImeiNumber2(String imeiNumber2) {
        this.imeiNumber2 = imeiNumber2;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }
}
