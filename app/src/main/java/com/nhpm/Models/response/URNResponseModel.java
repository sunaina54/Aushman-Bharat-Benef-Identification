package com.nhpm.Models.response;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by SUNAINA on 14-06-2018.
 */

public class URNResponseModel extends GenericResponse implements Serializable {
    private String operation;
    private ArrayList<RSBYDataItem> rsbyData;
    private ArrayList<ADCDDataItem> adcdData;

    public ArrayList<RSBYDataItem> getRsbyData() {
        return rsbyData;
    }

    public void setRsbyData(ArrayList<RSBYDataItem> rsbyData) {
        this.rsbyData = rsbyData;
    }

    public ArrayList<ADCDDataItem> getAdcdData() {
        return adcdData;
    }

    public void setAdcdData(ArrayList<ADCDDataItem> adcdData) {
        this.adcdData = adcdData;
    }

     private ArrayList<URNResponseItem> urnResponse;


    public ArrayList<URNResponseItem> getUrnResponse() {
        return urnResponse;
    }

    public void setUrnResponse(ArrayList<URNResponseItem> urnResponse) {
        this.urnResponse = urnResponse;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }


    static public URNResponseModel create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, URNResponseModel.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }

}
