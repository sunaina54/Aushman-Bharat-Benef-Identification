package com.nhpm.Models;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Saurabh on 02-05-2017.
 */

public class AadharAuthItem implements Serializable {

    private String aadharNo;
    private String name;
    private String dob;
    private String gender;


    public String getAadharNo() {
        return aadharNo;
    }

    public void setAadharNo(String aadharNo) {
        this.aadharNo = aadharNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
