package com.nhpm.ReqRespModels;

import com.nhpm.Networking.NetworkResponse;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by psqit on 8/29/2016.
 */
public class MasterLocation extends NetworkResponse {

    /*"stateCode": "34",
            "stateName": "PUDUCHERRY",
            "districtCode": "04",
            "districtName": "Karaikal",
            "tehsilCode": "002",
            "tehsilName": "ThirunallarTaluk",
            "vtCode": "0001",
            "vtName": "Kurumbagaram",
            "wardCode": "",
            "ruralUrban": "R",
            "gpCode": "0001",
            "gpName": "THIRUNALLAR TALUK",
            "mddsStc": "34",
            "mddsDtc": "637",
            "mddsSdtc": "05915",
            "mddsPlcn": "644996",
            "mddsName": "Kurumbagaram",
            "localBodyCode": "253387",
            "localBodyName": "KURUMBAGARAM"*/

    public List<MasterLocList> masterLocList = new ArrayList<>();
    static public MasterLocation create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, MasterLocation.class);
    }

    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
