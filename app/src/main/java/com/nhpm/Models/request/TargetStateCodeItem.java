package com.nhpm.Models.request;

import com.nhpm.Models.response.GenericResponse;

/**
 * Created by Saurabh on 06-07-2017.
 */

public class TargetStateCodeItem extends GenericResponse {

    private String targetStateCode;

    public String getTargetStateCode() {
        return targetStateCode;
    }

    public void setTargetStateCode(String targetStateCode) {
        this.targetStateCode = targetStateCode;
    }
}
