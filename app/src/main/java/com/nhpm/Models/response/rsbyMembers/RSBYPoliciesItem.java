package com.nhpm.Models.response.rsbyMembers;

import java.io.Serializable;

/**
 * Created by Saurabh on 17-03-2017.
 */

public class RSBYPoliciesItem implements Serializable {

    private String policyNo;
    private String stateCode;
    private String districtCode;
    private String insuranceCompanyCode;
    private String entensionEndDate;
    private String entensionType;

    public String getPolicyNo() {
        return policyNo;
    }

    public void setPolicyNo(String policyNo) {
        this.policyNo = policyNo;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        stateCode = stateCode;
    }

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        districtCode = districtCode;
    }

    public String getInsuranceCompanyCode() {
        return insuranceCompanyCode;
    }

    public void setInsuranceCompanyCode(String insuranceCompanyCode) {
        insuranceCompanyCode = insuranceCompanyCode;
    }

    public String getExtensionEndDate() {
        return entensionEndDate;
    }

    public void setExtensionEndDate(String extensionEndDate) {
        entensionEndDate = extensionEndDate;
    }

    public String getExtensionType() {
        return entensionType;
    }

    public void setExtensionType(String extensionType) {
        entensionType = extensionType;
    }
}
