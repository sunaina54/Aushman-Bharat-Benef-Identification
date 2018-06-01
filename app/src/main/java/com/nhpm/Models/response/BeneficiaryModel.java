package com.nhpm.Models.response;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by SUNAINA on 23-05-2018.
 */

public class BeneficiaryModel implements Serializable {

    private ArrayList<BeneficiaryListItem> beneficiaryList;

    public ArrayList<BeneficiaryListItem> getBeneficiaryList() {
        return beneficiaryList;
    }

    public void setBeneficiaryList(ArrayList<BeneficiaryListItem> beneficiaryList) {
        this.beneficiaryList = beneficiaryList;
    }

    static public BeneficiaryModel create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, BeneficiaryModel.class);
    }

    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }

}
