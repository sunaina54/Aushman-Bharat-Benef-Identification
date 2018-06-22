package com.nhpm.Models.response;

import java.io.Serializable;

/**
 * Created by SUNAINA on 20-06-2018.
 */

public class FamilyListResult implements Serializable {
    private SearchResult response;
    public SearchResult getResponse() {
        return response;
    }

    public void setResponse(SearchResult response) {
        this.response = response;
    }
}
