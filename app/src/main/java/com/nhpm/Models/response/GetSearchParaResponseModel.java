package com.nhpm.Models.response;

import com.google.gson.Gson;
import com.nhpm.Models.request.GetSearchParaRequestModel;

import java.io.Serializable;

/**
 * Created by SUNAINA on 09-07-2018.
 */

public class GetSearchParaResponseModel extends GenericResponse implements Serializable {
    private String result;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    static public GetSearchParaResponseModel create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, GetSearchParaResponseModel.class);
    }

    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
