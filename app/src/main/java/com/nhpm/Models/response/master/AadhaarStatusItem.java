package com.nhpm.Models.response.master;

import java.io.Serializable;

/**
 * Created by Anand on 20-11-2016.
 */
/*"aStatusCode": "1",
        "aStatusDesc": "Aadhaar Available"*/
public class AadhaarStatusItem implements Serializable {

    private String aStatusCode;
    private String aStatusDesc;

    public AadhaarStatusItem() {
    }

    public AadhaarStatusItem(String aStatusCode, String aStatusDesc) {

        this.aStatusCode = aStatusCode;
        this.aStatusDesc = aStatusDesc;
    }

    public String getaStatusCode() {
        return aStatusCode;
    }

    public void setaStatusCode(String aStatusCode) {
        this.aStatusCode = aStatusCode;
    }

    public String getaStatusDesc() {
        return aStatusDesc;
    }

    public void setaStatusDesc(String aStatusDesc) {
        this.aStatusDesc = aStatusDesc;
    }
}
