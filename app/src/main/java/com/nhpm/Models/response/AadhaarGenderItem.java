package com.nhpm.Models.response;

import java.io.Serializable;

/**
 * Created by Anand on 02-11-2016.
 */
public class AadhaarGenderItem implements Serializable {
    private String genderLable;
    private int genderId;
    private String genderIdString;

    public AadhaarGenderItem(String genderLable, int genderId, String genderIdString) {
        this.genderLable = genderLable;
        this.genderId = genderId;
        this.genderIdString = genderIdString;
    }

    public String getGenderLable() {
        return genderLable;
    }

    public void setGenderLable(String genderLable) {
        this.genderLable = genderLable;
    }

    public int getGenderId() {
        return genderId;
    }

    public void setGenderId(int genderId) {
        this.genderId = genderId;
    }

    public String getGenderIdString() {
        return genderIdString;
    }

    public void setGenderIdString(String genderIdString) {
        this.genderIdString = genderIdString;
    }
}
