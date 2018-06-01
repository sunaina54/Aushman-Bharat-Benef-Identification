package com.nhpm.Models.response.rsbyMembers;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Anand on 18-11-2016.
 */
public class RSBYMemberItem implements Serializable {

    /*"urn": "15081200116000024",
            "familyId": "180003205",
            "memberID": 1,
            "memberName": "CHHIETLAI",
            "gender": "1",
            "relationCode": "1",
            "dateofBirth": "6/1/1963",
            "age": 53,
            "enrolled": -1,
            "uid": "U",
            "uidNumber": "0",
            "status": "-1"*/
    private String urn;
    private String familyId;
    private String memberID;
    private String memberName;
    private String gender;
    private String relationCode;
    private String dateofBirth;
    private String age;
    private String enrolled;
    private String uid;
    private String uidNumber;
    private String status;

    static public RSBYMemberItem create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, RSBYMemberItem.class);
    }

    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }




}
