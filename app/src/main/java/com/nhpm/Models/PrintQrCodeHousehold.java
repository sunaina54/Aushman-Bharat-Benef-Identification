package com.nhpm.Models;

import java.io.Serializable;

/**
 * Created by Saurabh on 06-04-2017.
 */

public class PrintQrCodeHousehold implements Serializable {
    String nhpsId;
    String state;
    String dist;
    String teh;
    String vill;
    String src;
    String urnNo;
    String hhld;

    String lastUpdated;


    public String getNhpsId() {
        return nhpsId;
    }

    public void setNhpsId(String nhpsId) {
        this.nhpsId = nhpsId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDist() {
        return dist;
    }

    public void setDist(String dist) {
        this.dist = dist;
    }

    public String getTeh() {
        return teh;
    }

    public void setTeh(String teh) {
        this.teh = teh;
    }

    public String getVill() {
        return vill;
    }

    public void setVill(String vill) {
        this.vill = vill;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getUrnNo() {
        return urnNo;
    }

    public void setUrnNo(String urnNo) {
        this.urnNo = urnNo;
    }

    public String getHhld() {
        return hhld;
    }

    public void setHhld(String hhld) {
        this.hhld = hhld;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
