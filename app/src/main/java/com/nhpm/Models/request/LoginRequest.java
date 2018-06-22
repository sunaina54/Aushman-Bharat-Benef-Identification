package com.nhpm.Models.request;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Anand on 18-11-2016.
 */

     /*   {
        "aadhaarNumber":"399734158175",
        "imeiNo1":"352356078907011",
        "imeiNo2":""
        }*/

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Anand on 18-11-2016.
 */

     /*   {
        "aadhaarNumber":"399734158175",
        "imeiNo1":"352356078907011",
        "imeiNo2":""
        }*/
public class LoginRequest implements Serializable {

    private String ssoid;
    private String imeiNo1;
    private String imeiNo2;
    private String appVersion;
    private String userName;
    private String password;
    private String email;
    private String mobile;
    private String loginType;
    private String applicationid;
    // private int lastLogin;


    public String getApplicationid() {
        return applicationid;
    }

    public void setApplicationid(String applicationid) {
        this.applicationid = applicationid;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getAadhaarNumber() {
        return ssoid;
    }

    public void setAadhaarNumber(String ssoid) {
        this.ssoid = ssoid;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

   /* public int getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(int lastLogin) {
        this.lastLogin = lastLogin;
    }*/

    static public LoginRequest create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, LoginRequest.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
