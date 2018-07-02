package com.nhpm.Models.response;

import com.google.gson.Gson;
import com.nhpm.Models.NameMatchScoreModelRequest;

import java.io.Serializable;

public class MatchScoreResponse extends GenericResponse implements Serializable{
    private Result result;

    static public MatchScoreResponse create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, MatchScoreResponse.class);
    }

    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

   public class Result implements Serializable {
        public String result;

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }
    }

}
