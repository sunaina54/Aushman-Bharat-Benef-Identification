package com.nhpm.Models.response.seccMembers;

import com.nhpm.Models.response.rsbyMembers.RSBYItem;
import com.nhpm.Models.response.rsbyMembers.RsbyHouseholdItem;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Anand on 22-11-2016.
 */
public class SelectedMemberItem implements Serializable{
    private SeccMemberItem seccMemberItem;
    private RSBYItem rsbyMemberItem;
    private RSBYItem oldHeadrsbyMemberItem;
    private RSBYItem newHeadrsbyMemberItem;
    private RsbyHouseholdItem rsbyHouseholdItem;
    private HouseHoldItem houseHoldItem;
    private SeccMemberItem oldHeadMember;
    private SeccMemberItem newHeadMember;
    private ArrayList<SeccMemberItem> relationUpdatedList;
    private ArrayList<RSBYItem> rsbyRelationUpdatedList;

    public ArrayList<RSBYItem> getRsbyRelationUpdatedList() {
        return rsbyRelationUpdatedList;
    }

    public void setRsbyRelationUpdatedList(ArrayList<RSBYItem> rsbyRelationUpdatedList) {
        this.rsbyRelationUpdatedList = rsbyRelationUpdatedList;
    }

    public ArrayList<SeccMemberItem> getRelationUpdatedList() {
        return relationUpdatedList;
    }

    public void setRelationUpdatedList(ArrayList<SeccMemberItem> relationUpdatedList) {
        this.relationUpdatedList = relationUpdatedList;
    }

    static public SelectedMemberItem create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, SelectedMemberItem.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public SeccMemberItem getOldHeadMember() {
        return oldHeadMember;
    }

    public void setOldHeadMember(SeccMemberItem oldHeadMember) {
        this.oldHeadMember = oldHeadMember;
    }

    public SeccMemberItem getNewHeadMember() {
        return newHeadMember;
    }

    public void setNewHeadMember(SeccMemberItem newHeadMember) {
        this.newHeadMember = newHeadMember;
    }

    public HouseHoldItem getHouseHoldItem() {
        return houseHoldItem;
    }

    public void setHouseHoldItem(HouseHoldItem houseHoldItem) {
        this.houseHoldItem = houseHoldItem;
    }

    public SeccMemberItem getSeccMemberItem() {
        return seccMemberItem;
    }

    public void setSeccMemberItem(SeccMemberItem seccMemberItem) {
        this.seccMemberItem = seccMemberItem;
    }

    public RsbyHouseholdItem getRsbyHouseholdItem() {
        return rsbyHouseholdItem;
    }

    public void setRsbyHouseholdItem(RsbyHouseholdItem rsbyHouseholdItem) {
        this.rsbyHouseholdItem = rsbyHouseholdItem;
    }

    public RSBYItem getRsbyMemberItem() {
        return rsbyMemberItem;
    }

    public void setRsbyMemberItem(RSBYItem rsbyMemberItem) {
        this.rsbyMemberItem = rsbyMemberItem;
    }

    public RSBYItem getOldHeadrsbyMemberItem() {
        return oldHeadrsbyMemberItem;
    }

    public void setOldHeadrsbyMemberItem(RSBYItem oldHeadrsbyMemberItem) {
        this.oldHeadrsbyMemberItem = oldHeadrsbyMemberItem;
    }

    public RSBYItem getNewHeadrsbyMemberItem() {
        return newHeadrsbyMemberItem;
    }

    public void setNewHeadrsbyMemberItem(RSBYItem newHeadrsbyMemberItem) {
        this.newHeadrsbyMemberItem = newHeadrsbyMemberItem;
    }
}
