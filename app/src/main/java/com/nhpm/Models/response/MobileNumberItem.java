package com.nhpm.Models.response;

import java.io.Serializable;

/**
 * Created by PSQ on 1/30/2017.
 */

public class MobileNumberItem implements Serializable {
    private String mobileNumber;
    private String mobileType;

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getMobileType() {
        return mobileType;
    }

    public void setMobileType(String mobileType) {
        this.mobileType = mobileType;
    }
}
