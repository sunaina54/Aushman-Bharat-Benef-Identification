package com.nhpm.Models.response.master;

import com.nhpm.Models.response.GenericResponse;

import java.io.Serializable;

/**
 * Created by Saurabh on 11-08-2017.
 */

public class MemberRelationItem implements Serializable {


    private String mddsRCode;
    private String relationName;
    private String activeStatus;
    private String relationCode;
    private String displayOrder;
    private String relationGender;


    public String getMddsRCode() {
        return mddsRCode;
    }

    public void setMddsRCode(String mddsRCode) {
        this.mddsRCode = mddsRCode;
    }

    public String getRelationName() {
        return relationName;
    }

    public void setRelationName(String relationName) {
        this.relationName = relationName;
    }

    public String getActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(String activeStatus) {
        this.activeStatus = activeStatus;
    }

    public String getRelationCode() {
        return relationCode;
    }

    public void setRelationCode(String relationCode) {
        this.relationCode = relationCode;
    }

    public String getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(String displayOrder) {
        this.displayOrder = displayOrder;
    }

    public String getRelationGender() {
        return relationGender;
    }

    public void setRelationGender(String relationGender) {
        this.relationGender = relationGender;
    }
}
