package com.nhpm.Models.response;

import java.io.Serializable;

/**
 * Created by Anand on 04-11-2016.
 */
public class WhoseMobileItem implements Serializable {
    private String statusCode;
    private String statusType;

    public WhoseMobileItem(String statusCode, String statusType) {
        this.statusCode = statusCode;
        this.statusType = statusType;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusType() {
        return statusType;
    }

    public void setStatusType(String statusType) {
        this.statusType = statusType;
    }
}
