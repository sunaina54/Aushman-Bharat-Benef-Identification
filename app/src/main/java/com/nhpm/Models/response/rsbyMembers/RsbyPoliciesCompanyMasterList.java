package com.nhpm.Models.response.rsbyMembers;

import com.nhpm.Models.response.GenericResponse;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by Saurabh on 22-08-2017.
 */

public class RsbyPoliciesCompanyMasterList extends GenericResponse {


private ArrayList<RsbyPoliciesCompany> rsbyInsuranceCompanyList;


    static public RsbyPoliciesCompanyMasterList create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, RsbyPoliciesCompanyMasterList.class);
    }

    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public ArrayList<RsbyPoliciesCompany> getRsbyInsuranceCompanyList() {
        return rsbyInsuranceCompanyList;
    }

    public void setRsbyInsuranceCompanyList(ArrayList<RsbyPoliciesCompany> rsbyInsuranceCompanyList) {
        this.rsbyInsuranceCompanyList = rsbyInsuranceCompanyList;
    }
}
