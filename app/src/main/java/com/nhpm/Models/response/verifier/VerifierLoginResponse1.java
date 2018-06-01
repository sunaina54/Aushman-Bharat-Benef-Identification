package com.nhpm.Models.response.verifier;

import com.nhpm.Models.response.GenericResponse;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Anand on 01-11-2016.
 */
public class VerifierLoginResponse1 extends GenericResponse implements Serializable {
    private VerifierDetail verifierDetail;
    private ArrayList<VerifierLocationOLD> verifierLocations;

    static public VerifierLoginResponse1 create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, VerifierLoginResponse1.class);
    }

    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public VerifierDetail getVerifierDetail() {
        return verifierDetail;
    }

    public void setVerifierDetail(VerifierDetail verifierDetail) {
        this.verifierDetail = verifierDetail;
    }

    public ArrayList<VerifierLocationOLD> getVerifierLocations() {
        return verifierLocations;
    }

    public void setVerifierLocations(ArrayList<VerifierLocationOLD> verifierLocations) {
        this.verifierLocations = verifierLocations;
    }
}
