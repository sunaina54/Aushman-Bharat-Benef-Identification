package com.nhpm.Models.response;

import com.nhpm.Models.response.rsbyMembers.RSBYItem;
import com.nhpm.Models.response.seccMembers.SeccMemberItem;

import java.io.Serializable;

/**
 * Created by Anand on 19-10-2016.
 */
public class NomineeMemberItem implements Serializable {
    public int statusCode;
    public String memberName;
    private SeccMemberItem memberItem;
    private RSBYItem rsbyItem;
    private int age;
    private String gender;
    private String nomineeLabel;

    public NomineeMemberItem() {
    }

    /*public NomineeMemberItem(int statusCode, String memberName) {
        this.statusCode = statusCode;
        this.memberName = memberName;
    }*/

    public NomineeMemberItem(int statusCode, String memberName, String nomineeLabel) {
        this.statusCode = statusCode;
        this.memberName = memberName;
        this.nomineeLabel = nomineeLabel;
    }

    public String getNomineeLabel() {
        return nomineeLabel;
    }

    public void setNomineeLabel(String nomineeLabel) {
        this.nomineeLabel = nomineeLabel;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public SeccMemberItem getMemberItem() {
        return memberItem;
    }

    public void setMemberItem(SeccMemberItem memberItem) {
        this.memberItem = memberItem;
    }

    public RSBYItem getRsbyItem() {
        return rsbyItem;
    }

    public void setRsbyItem(RSBYItem rsbyItem) {
        this.rsbyItem = rsbyItem;
    }
}
