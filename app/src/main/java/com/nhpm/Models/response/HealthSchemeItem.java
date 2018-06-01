package com.nhpm.Models.response;

import java.io.Serializable;

/**
 * Created by Anand on 28-10-2016.
 */
public class HealthSchemeItem implements Serializable {
    public int code;
    public String schemeName;

    public HealthSchemeItem(int code, String schemeName) {
        this.code = code;
        this.schemeName = schemeName;
    }
}
