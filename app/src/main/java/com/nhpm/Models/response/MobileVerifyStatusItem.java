package com.nhpm.Models.response;

import java.io.Serializable;

/**
 * Created by Anand on 19-10-2016.
 */
public class MobileVerifyStatusItem implements Serializable {
    public int statusCode;
    public String status;

    public MobileVerifyStatusItem(int statusCode, String status) {
        this.statusCode = statusCode;
        this.status = status;
    }
}
