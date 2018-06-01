package com.nhpm.Models.response;

import java.io.Serializable;

/**
 * Created by Quikhop on 11/23/2016.
 */
public class RelationItem implements Serializable {
    private String relationCode;
    private String relationName;
    private String displayOrder;
    private String activeStatus;
    private String relationGender;

    public RelationItem(String relationCode, String relationName) {
        this.relationCode = relationCode;
        this.relationName = relationName;
    }

    public RelationItem() {
    }

    public String getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(String displayOrder) {
        this.displayOrder = displayOrder;
    }

    public String getActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(String activeStatus) {
        this.activeStatus = activeStatus;
    }

    public String getRelationGender() {
        return relationGender;
    }

    public void setRelationGender(String relationGender) {
        this.relationGender = relationGender;
    }

    public String getRelationCode() {
        return relationCode;
    }

    public void setRelationCode(String relationCode) {
        this.relationCode = relationCode;
    }

    public String getRelationName() {
        return relationName;
    }

    public void setRelationName(String relationName) {
        this.relationName = relationName;
    }
}
