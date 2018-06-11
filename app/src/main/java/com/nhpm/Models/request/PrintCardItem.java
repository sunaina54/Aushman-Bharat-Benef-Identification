package com.nhpm.Models.request;

import java.io.Serializable;

public class PrintCardItem implements Serializable {
    private String nameOnCard;
    private String fatherNameOnCard;
    private String genderOnCard;
    private String yobObCard;
    private String cardNo;
    private String qrCode;
    private String benefPhoto;
    private String stateName;

    public String getBenefPhoto() {
        return benefPhoto;
    }

    public void setBenefPhoto(String benefPhoto) {
        this.benefPhoto = benefPhoto;
    }

    public String getNameOnCard() {
        return nameOnCard;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public void setNameOnCard(String nameOnCard) {
        this.nameOnCard = nameOnCard;
    }

    public String getFatherNameOnCard() {
        return fatherNameOnCard;
    }

    public void setFatherNameOnCard(String fatherNameOnCard) {
        this.fatherNameOnCard = fatherNameOnCard;
    }

    public String getGenderOnCard() {
        return genderOnCard;
    }

    public void setGenderOnCard(String genderOnCard) {
        this.genderOnCard = genderOnCard;
    }

    public String getYobObCard() {
        return yobObCard;
    }

    public void setYobObCard(String yobObCard) {
        this.yobObCard = yobObCard;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }
}
