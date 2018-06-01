package com.nhpm.Models.request;

import java.io.Serializable;

/**
 * Created by SUNAINA on 22-05-2018.
 */

public class BeneficiarySearchModel implements Serializable {
    private String cardType;

    public BeneficiarySearchModel(String cardType) {
        this.cardType = cardType;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }
}
