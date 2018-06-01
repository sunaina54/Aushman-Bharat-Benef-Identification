package com.nhpm.Models.response.rsbyMembers;

import java.io.Serializable;

/**
 * Created by Saurabh on 30-03-2017.
 */

public class RsbyRelationItem implements Serializable {

   private String relationCode;
    private String relationName;


    public String getRelCode() {
        return relationCode;
    }

    public void setRelCode(String relCode) {
        this.relationCode = relCode;
    }

    public String getRelName() {
        return relationName;
    }

    public void setRelName(String relName) {
        this.relationName = relName;
    }
}
