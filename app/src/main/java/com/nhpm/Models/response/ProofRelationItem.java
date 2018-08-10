package com.nhpm.Models.response;

import java.io.Serializable;

/**
 * Created by SUNAINA on 08-08-2018.
 */

public class ProofRelationItem implements Serializable {
    public int proofRelCode;
    public String proofRelIDName;

    public ProofRelationItem(int proofRelCode, String proofRelID) {
        this.proofRelCode = proofRelCode;
        this.proofRelIDName = proofRelID;
    }

    public int getProofRelCode() {
        return proofRelCode;
    }

    public void setProofRelCode(int proofRelCode) {
        this.proofRelCode = proofRelCode;
    }

    public String getProofRelIDName() {
        return proofRelIDName;
    }

    public void setProofRelIDName(String proofRelIDName) {
        this.proofRelIDName = proofRelIDName;
    }
}
