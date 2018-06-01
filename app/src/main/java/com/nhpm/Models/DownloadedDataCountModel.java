package com.nhpm.Models;

import java.io.Serializable;

/**
 * Created by Saurabh on 08-06-2017.
 */

public class DownloadedDataCountModel implements Serializable {

    private String seccMemberCount;
    private String seccHouseholdCount;
    private String rsbyMemberCount;
    private String rsbyHouseholdCount;
    private String id;


    public String getSeccMemberCount() {
        return seccMemberCount;
    }

    public void setSeccMemberCount(String seccMemberCount) {
        this.seccMemberCount = seccMemberCount;
    }

    public String getSeccHouseholdCount() {
        return seccHouseholdCount;
    }

    public void setSeccHouseholdCount(String seccHouseholdCount) {
        this.seccHouseholdCount = seccHouseholdCount;
    }

    public String getRsbyMemberCount() {
        return rsbyMemberCount;
    }

    public void setRsbyMemberCount(String rsbyMemberCount) {
        this.rsbyMemberCount = rsbyMemberCount;
    }

    public String getRsbyHouseholdCount() {
        return rsbyHouseholdCount;
    }

    public void setRsbyHouseholdCount(String rsbyHouseholdCount) {
        this.rsbyHouseholdCount = rsbyHouseholdCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
