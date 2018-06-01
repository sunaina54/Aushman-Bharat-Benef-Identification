package com.nhpm.Models.response.verifier;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Anand on 20-11-2016.
 */
public class AadhaarOtpResponse implements Serializable {
   /* "txn": "39973415817520161115051447431NHPS",
            "err": null,
            "code": "985e38d776e842e4be5dc71b13f78977",
            "ts": 1479210287983,
            "ret": "Y"*/
    private String txn;
    private String err;
    private String code;
    private String ts;
    private String ret;
    private String info;
    static public AadhaarOtpResponse create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, AadhaarOtpResponse.class);
    }

    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public String getTxn() {
        return txn;
    }

    public void setTxn(String txn) {
        this.txn = txn;
    }

    public String getErr() {
        return err;
    }

    public void setErr(String err) {
        this.err = err;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTs() {
        return ts;
    }

    public void setTs(String ts) {
        this.ts = ts;
    }

    public String getRet() {
        return ret;
    }

    public void setRet(String ret) {
        this.ret = ret;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
