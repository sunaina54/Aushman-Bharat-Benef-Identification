package com.nhpm.Models;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Saurabh on 25-05-2017.
 */

public class DemoAuthResp implements Serializable {


    private String ret;
    private String txn;
    private String err;



    static public DemoAuthResp create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, DemoAuthResp.class);
    }

    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
    public String getRet() {
        return ret;
    }

    public void setRet(String ret) {
        this.ret = ret;
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
}
