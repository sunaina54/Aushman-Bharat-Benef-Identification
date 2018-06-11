package com.nhpm.Models.response;

import java.io.Serializable;

public class FamilyDetailResponse implements Serializable{
    private String idImage;
    private String idNumber;
    private String idType;
    private String familyMatchScore;

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

    public String getFamilyMatchScore() {
        return familyMatchScore;
    }

    public void setFamilyMatchScore(String familyMatchScore) {
        this.familyMatchScore = familyMatchScore;
    }
}
