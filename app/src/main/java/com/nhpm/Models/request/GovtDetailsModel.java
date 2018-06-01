package com.nhpm.Models.request;

import java.io.Serializable;

/**
 * Created by SUNAINA on 15-05-2018.
 */

public class GovtDetailsModel implements Serializable {
    private String image;
    private String govtIdType;
    private String idNumber;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getGovtIdType() {
        return govtIdType;
    }

    public void setGovtIdType(String govtIdType) {
        this.govtIdType = govtIdType;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }
}
