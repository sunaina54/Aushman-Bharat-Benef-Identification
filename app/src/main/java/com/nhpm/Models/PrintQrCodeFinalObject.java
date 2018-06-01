package com.nhpm.Models;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Saurabh on 04-04-2017.
 */

public class PrintQrCodeFinalObject implements Serializable {

    PrintQrCodeHousehold household;
    ArrayList<PrintQrCodeMemberDetail> members;

    static public PrintQrCodeFinalObject create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, PrintQrCodeFinalObject.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public PrintQrCodeHousehold getHousehold() {
        return household;
    }

    public void setHousehold(PrintQrCodeHousehold household) {
        this.household = household;
    }

    public ArrayList<PrintQrCodeMemberDetail> getFamilyMembers() {
        return members;
    }

    public void setFamilyMembers(ArrayList<PrintQrCodeMemberDetail> familyMembers) {
        this.members = familyMembers;
    }
}
