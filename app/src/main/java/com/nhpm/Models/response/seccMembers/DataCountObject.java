package com.nhpm.Models.response.seccMembers;

import java.io.Serializable;

/**
 * Created by Saurabh on 07-06-2017.
 */

public class DataCountObject implements Serializable {

    private String seccHouseHoldCount;
    private String seccMembersCount;
    private String rsbyHouseHoldCount;
    private String rsbyMembersCount;


    public String getHouseHoldCount() {
        return seccHouseHoldCount;
    }

    public void setHouseHoldCount(String houseHoldCount) {
        this.seccHouseHoldCount = houseHoldCount;
    }

    public String getMembersCount() {
        return seccMembersCount;
    }

    public void setMembersCount(String membersCount) {
        this.seccMembersCount = membersCount;
    }

    public String getRsbyHouseHoldCount() {
        return rsbyHouseHoldCount;
    }

    public void setRsbyHouseHoldCount(String rsbyHouseHoldCount) {
        this.rsbyHouseHoldCount = rsbyHouseHoldCount;
    }

    public String getRsbyMembersCount() {
        return rsbyMembersCount;
    }

    public void setRsbyMembersCount(String rsbyMembersCount) {
        this.rsbyMembersCount = rsbyMembersCount;
    }
}
