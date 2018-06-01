package com.nhpm.ReqRespModels;

/**
 * Created by psqit on 9/2/2016.
 */
public class NHSDataRequest {
   /* "stateCode":"34",
            "districtCode":"04",
            "tehsilCode":"001",
            "towncode":"7006",
            "wardid":"0015",
            "blockno":"0142"*/

    private String stateCode,districtCode,tehsilCode,towncode,wardid,blockno;

    public NHSDataRequest(String stateCode, String districtCode, String tehsilCode, String towncode, String wardid, String blockno) {
        this.stateCode = stateCode;
        this.districtCode = districtCode;
        this.tehsilCode = tehsilCode;
        this.towncode = towncode;
        this.wardid = wardid;
        this.blockno = blockno;
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

    public String getTowncode() {
        return towncode;
    }

    public void setTowncode(String towncode) {
        this.towncode = towncode;
    }

    public String getWardid() {
        return wardid;
    }

    public void setWardid(String wardid) {
        this.wardid = wardid;
    }

    public String getBlockno() {
        return blockno;
    }

    public void setBlockno(String blockno) {
        this.blockno = blockno;
    }
}
