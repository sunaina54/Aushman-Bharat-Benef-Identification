package com.nhpm.Models.response;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Anand on 22-11-2016.
 */
public class MobileOTPResponse implements Serializable {
   // {"sender":"99993418","message":"","time":"2016-11-22 09:18:36:261","status":"0","otp":"104126","result":null,"txn":null,"iemiNo":null,"sequenceNo":"NHPS:2016-11-2209:18:36:261","houseHoldNo":null,"err":null}
    private String sender;
    private String id;
    private String message;
    private String time;
    private String status;
    private String otp;
    private String result;
    private String txn;
    private String iemiNo;
    private String sequenceNo;
    private String houseHoldNo;
    private String err;

    static public MobileOTPResponse create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, MobileOTPResponse.class);
    }

    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getSequenceNo() {
        return sequenceNo;
    }

    public void setSequenceNo(String sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getTxn() {
        return txn;
    }

    public void setTxn(String txn) {
        this.txn = txn;
    }

    public String getIemiNo() {
        return iemiNo;
    }

    public void setIemiNo(String iemiNo) {
        this.iemiNo = iemiNo;
    }

    public String getHouseHoldNo() {
        return houseHoldNo;
    }

    public void setHouseHoldNo(String houseHoldNo) {
        this.houseHoldNo = houseHoldNo;
    }

    public String getErr() {
        return err;
    }

    public void setErr(String err) {
        this.err = err;
    }
}
