package com.nhpm.Models.response;

import com.nhpm.Models.FamilyMemberModel;

import java.io.Serializable;
import java.util.ArrayList;

public class FamilyDetailResponse implements Serializable{
    private String idImage;
    private String idNumber;
    private String idType;
    private Integer familyMatchScore;
    private ArrayList<FamilyMemberModel> familyMemberModels;

    public ArrayList<FamilyMemberModel> getFamilyMemberModels() {
        return familyMemberModels;
    }

    public void setFamilyMemberModels(ArrayList<FamilyMemberModel> familyMemberModels) {
        this.familyMemberModels = familyMemberModels;
    }

    public String getIdImage() {
        return idImage;
    }

    public void setIdImage(String idImage) {
        this.idImage = idImage;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public Integer getFamilyMatchScore() {
        return familyMatchScore;
    }

    public void setFamilyMatchScore(Integer familyMatchScore) {
        this.familyMatchScore = familyMatchScore;
    }
}
