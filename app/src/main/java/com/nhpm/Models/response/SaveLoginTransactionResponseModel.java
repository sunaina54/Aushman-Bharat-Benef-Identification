package com.nhpm.Models.response;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by SUNAINA on 25-06-2018.
 */

public class SaveLoginTransactionResponseModel extends GenericResponse implements Serializable {

    private Integer transactionId;

    public Integer getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Integer transactionId) {
        this.transactionId = transactionId;
    }


    static public SaveLoginTransactionResponseModel create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, SaveLoginTransactionResponseModel.class);
    }

    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
