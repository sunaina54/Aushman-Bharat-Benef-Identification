package com.nhpm.Models.response.seccMembers;

import com.nhpm.Models.response.GenericResponse;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Anand on 01-11-2016.
 */
public class SeccMemberResponse extends GenericResponse implements Serializable {

    private ArrayList<SeccMemberItem> rsbyMemberReadList;
    private ArrayList<PopSeccWriteItem> popSeccWriteList;
    private ArrayList<SeccMemberItem> popSeccReadList;


    public ArrayList<SeccMemberItem> getSeccMemberList() {
        return popSeccReadList;
    }

    public void setSeccMemberList(ArrayList<SeccMemberItem> seccMemberList) {
        this.popSeccReadList = seccMemberList;
    }

    public ArrayList<PopSeccWriteItem> getPopSeccWriteList() {
        return popSeccWriteList;
    }

    public void setPopSeccWriteList(ArrayList<PopSeccWriteItem> popSeccWriteList) {
        this.popSeccWriteList = popSeccWriteList;
    }


    public ArrayList<SeccMemberItem> getRsbyMemberReadList() {
        return rsbyMemberReadList;
    }

    public void setRsbyMemberReadList(ArrayList<SeccMemberItem> rsbyMemberReadList) {
        this.rsbyMemberReadList = rsbyMemberReadList;
    }

    static public SeccMemberResponse create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, SeccMemberResponse.class);
    }


    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
