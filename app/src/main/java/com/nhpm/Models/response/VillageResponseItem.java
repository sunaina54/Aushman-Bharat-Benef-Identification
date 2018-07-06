package com.nhpm.Models.response;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;

public class VillageResponseItem extends GenericResponse implements Serializable {
    /*{
        "status": true,
            "operation": "HISP API",
            "errorCode": null,
            "errorMessage": null,
            "result": {
        "result": [
        "SONIPAT",
                "Sonipat"
        ]
    }
    }*/
    private Result result;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    static public VillageResponseItem create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, VillageResponseItem.class);
    }

    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public  class Result implements Serializable{
        private ArrayList<String> result;

        public ArrayList<String> getResult() {
            return result;
        }

        public void setResult(ArrayList<String> result) {
            this.result = result;
        }
    }
}
