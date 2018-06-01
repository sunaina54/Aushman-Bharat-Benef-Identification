package com.nhpm.ReqRespModels;

/**
 * Created by psqit on 9/2/2016.
 */
public class UpdateRequest {
private String id,nameNpr,relnameNpr,fathernmNpr,mothernmNpr,aadhaarNo,phoneRespondent,occunameNpr,inss,ration,dobNpr,genderidNpr,mstatusidNpr;

    public UpdateRequest(String id, String nameNpr, String fathernmNpr, String relnameNpr, String mothernmNpr, String aadhaarNo, String phoneRespondent, String occunameNpr, String inss, String ration, String dobNpr, String genderidNpr, String mstatusidNpr) {
        this.id = id;
        this.nameNpr = nameNpr;
        this.fathernmNpr = fathernmNpr;
        this.relnameNpr = relnameNpr;
        this.mothernmNpr = mothernmNpr;
        this.aadhaarNo = aadhaarNo;
        this.phoneRespondent = phoneRespondent;
        this.occunameNpr = occunameNpr;
        this.inss = inss;
        this.ration = ration;
        this.dobNpr = dobNpr;
        this.genderidNpr = genderidNpr;
        this.mstatusidNpr = mstatusidNpr;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNameNpr() {
        return nameNpr;
    }

    public void setNameNpr(String nameNpr) {
        this.nameNpr = nameNpr;
    }

    public String getRelnameNpr() {
        return relnameNpr;
    }

    public void setRelnameNpr(String relnameNpr) {
        this.relnameNpr = relnameNpr;
    }

    public String getFathernmNpr() {
        return fathernmNpr;
    }

    public void setFathernmNpr(String fathernmNpr) {
        this.fathernmNpr = fathernmNpr;
    }

    public String getMothernmNpr() {
        return mothernmNpr;
    }

    public void setMothernmNpr(String mothernmNpr) {
        this.mothernmNpr = mothernmNpr;
    }

    public String getAadhaarNo() {
        return aadhaarNo;
    }

    public void setAadhaarNo(String aadhaarNo) {
        this.aadhaarNo = aadhaarNo;
    }

    public String getPhoneRespondent() {
        return phoneRespondent;
    }

    public void setPhoneRespondent(String phoneRespondent) {
        this.phoneRespondent = phoneRespondent;
    }

    public String getOccunameNpr() {
        return occunameNpr;
    }

    public void setOccunameNpr(String occunameNpr) {
        this.occunameNpr = occunameNpr;
    }

    public String getInss() {
        return inss;
    }

    public void setInss(String inss) {
        this.inss = inss;
    }

    public String getRation() {
        return ration;
    }

    public void setRation(String ration) {
        this.ration = ration;
    }

    public String getDobNpr() {
        return dobNpr;
    }

    public void setDobNpr(String dobNpr) {
        this.dobNpr = dobNpr;
    }

    public String getGenderidNpr() {
        return genderidNpr;
    }

    public void setGenderidNpr(String genderidNpr) {
        this.genderidNpr = genderidNpr;
    }

    public String getMstatusidNpr() {
        return mstatusidNpr;
    }

    public void setMstatusidNpr(String mstatusidNpr) {
        this.mstatusidNpr = mstatusidNpr;
    }
}
