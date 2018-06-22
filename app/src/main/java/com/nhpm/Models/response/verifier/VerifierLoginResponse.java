package com.nhpm.Models.response.verifier;

import com.nhpm.Models.response.GenericResponse;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;



/**
 * Created by Anand on 01-11-2016.
 */
public class VerifierLoginResponse extends GenericResponse implements Serializable {
    private ArrayList<VerifierLocationItem> locationList;
    private AadhaarResponseItem aadhaarItem;
    private String ssoid;
    private String name;
    private String designation;
    private String mobileNumber;
    private String imeiNumber1;
    private String gender;
    private String imeiNumber2;
    private String email;
    private String emailLogin;
    private String dob;
    private String userName;
    private String userId;
    private String pin;
    private String userStatus;
    private String role;
    private String appVersion;
    private String hnoName;
    private String hnoAadhaarNo;
    private boolean loginSession;
    private String lastLogin;
    private String authToken;
    private String statecode;
    private String stateName;
    private String districtCode;
    private String districtName;
    private String blockCode;
    private String blockName;
    private String ruralUrban;

    public String getStatecode() {
        return statecode;
    }

    public void setStatecode(String statecode) {
        this.statecode = statecode;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getBlockCode() {
        return blockCode;
    }

    public void setBlockCode(String blockCode) {
        this.blockCode = blockCode;
    }

    public String getBlockName() {
        return blockName;
    }

    public void setBlockName(String blockName) {
        this.blockName = blockName;
    }

    public String getRuralUrban() {
        return ruralUrban;
    }

    public void setRuralUrban(String ruralUrban) {
        this.ruralUrban = ruralUrban;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isLoginSession() {
        return loginSession;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    public void setLoginSession(boolean loginSession) {
        this.loginSession = loginSession;
    }

    public String getAadhaarNumber() {
        return ssoid;
    }

    public void setAadhaarNumber(String aadhaarNumber) {
        this.ssoid = aadhaarNumber;
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

    public String getHnoName() {
        return hnoName;
    }

    public void setHnoName(String hnoName) {
        this.hnoName = hnoName;
    }

    public String getHnoAadhaarNo() {
        return hnoAadhaarNo;
    }

    public void setHnoAadhaarNo(String hnoAadhaarNo) {
        this.hnoAadhaarNo = hnoAadhaarNo;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmailLogin() {
        return emailLogin;
    }

    public void setEmailLogin(String emailLogin) {
        this.emailLogin = emailLogin;
    }

    static public VerifierLoginResponse create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, VerifierLoginResponse.class);
    }

    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public ArrayList<VerifierLocationItem> getLocationList() {
        return locationList;
    }

    public void setLocationList(ArrayList<VerifierLocationItem> locationList) {
        this.locationList = locationList;
    }

    public AadhaarResponseItem getAadhaarItem() {
        return aadhaarItem;
    }

    public void setAadhaarItem(AadhaarResponseItem aadhaarItem) {
        this.aadhaarItem = aadhaarItem;
    }
}
