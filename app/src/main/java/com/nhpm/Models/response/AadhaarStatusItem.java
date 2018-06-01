package com.nhpm.Models.response;

import java.io.Serializable;

/**
 * Created by Anand on 18-10-2016.
 */
public class AadhaarStatusItem implements Serializable{
    public int statusCode;
    public String status;


    public AadhaarStatusItem(int statusCode, String status) {
        this.statusCode = statusCode;
        this.status = status;
    }
}
