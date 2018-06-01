package com.nhpm.Models.response.verifier;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Anand on 22-11-2016.
 */
public class DemoAuthResponseItem implements Serializable {
   // {"ret":"Y","code":null,"txn":"39973415817520161122045646070NHPS","err":"Code:Code:null:(Not sure. Pls. check spec.):(Not sure. Pls. check spec.)","info":"02{c950466b2f125e78775a29f2e10df3aa6c1bd026384955d45897afcf13134ea9,7398e31fe34015ca07e52fd7252c42f98519f358b208028f261e5dc5d5f7ee70,0180000018000000,1.0,20161122165645,0,0,0,1.6,73bd5abf744d9c0ec0c3c44e9a7c2eaf9cc2fc9e9d3e49f2c11a3b09ee314cc3,57266d21922bc5f567bb4426c4924210afe690a51a98dec2b612612ef76440ca,1037NHPS,P,560103,23,E,100,NA,NA,NA,NA,NA,NA,NA,efa1f375d76194fa51a3556a97e641e61685f914d446979da50a551a4333ffd7}"}

    private String ret;

    static public DemoAuthResponseItem create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, DemoAuthResponseItem.class);
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
}
