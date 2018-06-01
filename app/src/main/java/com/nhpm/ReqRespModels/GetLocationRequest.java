package com.nhpm.ReqRespModels;

/**
 * Created by psqit on 9/2/2016.
 */
public class GetLocationRequest {

    /*"stateCode":"34",
            "districtCode":"04",
            "tehsilCode":"001",*/

    private String stateCode,districtCode,tehsilCode;

    public GetLocationRequest(String stateCode, String districtCode, String tehsilCode) {
        this.stateCode = stateCode;
        this.districtCode = districtCode;
        this.tehsilCode = tehsilCode;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    public String getTehsilCode() {
        return tehsilCode;
    }

    public void setTehsilCode(String tehsilCode) {
        this.tehsilCode = tehsilCode;
    }
}
