package com.nhpm.Models.request;

import com.nhpm.Models.response.GenericResponse;
import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Saurabh on 23-08-2017.
 */

public class MobileOtpRequestLoginModel implements Serializable {

    private String status;
    private String mobileotp;
    private String ssoid;
    private String transactionId;
    private String userName;
    private String userPass;
    private String applicationId;
    private String appVersion;

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    static public MobileOtpRequestLoginModel create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, MobileOtpRequestLoginModel.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOtp() {
        return mobileotp;
    }

    public void setOtp(String otp) {
        this.mobileotp = otp;
    }

    public String getMobileNo() {
        return ssoid;
    }

    public void setMobileNo(String mobileNo) {
        this.ssoid = mobileNo;
    }

    public String getSequenceNo() {
        return transactionId;
    }

    public void setSequenceNo(String sequenceNo) {
        this.transactionId = sequenceNo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPass() {
        return userPass;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }
}
