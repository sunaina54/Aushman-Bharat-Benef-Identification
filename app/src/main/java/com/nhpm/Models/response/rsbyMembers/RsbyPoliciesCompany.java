package com.nhpm.Models.response.rsbyMembers;

import java.io.Serializable;

/**
 * Created by Saurabh on 03-04-2017.
 */

public class RsbyPoliciesCompany implements Serializable {

    private String insuranceCompanyName;
    private String insuranceCompanyCode;


    public String getCompanyName() {
        return insuranceCompanyName;
    }

    public void setCompanyName(String companyName) {
        this.insuranceCompanyName = companyName;
    }

    public String getCompanyCode() {
        return insuranceCompanyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.insuranceCompanyCode = companyCode;
    }
}
