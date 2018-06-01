package com.nhpm.Models.response.verifier;

import com.google.gson.Gson;
import com.nhpm.Models.request.GovtDetailsModel;

import java.io.Serializable;

/**
 * Created by Anand on 20-11-2016.
 */
public class AadhaarResponseItem implements Serializable {
   /* "uid": "399734158175",
            "name": null,
            "dob": null,
            "gender": null,
            "vtc": null,
            "vtccode": null,
            "co": null,
            "dist": null,
            "subdist": null,
            "state": null,
            "pc": null,
            "loc": null,
            "po": null,
            "pht": null,
            "id": 0,
            "result": "N",
            "role": null,
            "userid": 0,
            "base64": null,
            "txn": "UKC:39973415817520161115051804627NHPS",
            "err": null*/
    private String uid;
    private String name;
    private String dob;
    private String gender;
    private String vtc;
    private String vtccode;
    private String co;
    private String dist;
    private String subdist;
    private String state;
    private String pc;
    private String loc;
    private String po;
    private String pht;
    private String id;
    private String result;
    private String role;
    private String userid;
    private String base64;
    private String txn;
    private String err;
    private String house;
    private String street;
    private String phone;
    private String email;
    private String nameLg;
    private String houseLg;
    private String lang;
    private String coLg;
    private String distLg;
    private String locLg;
    private String pcLg;
    private String stateLg;
    private String subdistLg;
    private String vtcLg;
    private String vtcCodeLg;
    private String poLg;
    private String streetLg;
    private String lmLg;
    private String lm;
    private String ts;
    private GovtDetailsModel govtDetailsModel;

    public GovtDetailsModel getGovtDetailsModel() {
        return govtDetailsModel;
    }

    public void setGovtDetailsModel(GovtDetailsModel govtDetailsModel) {
        this.govtDetailsModel = govtDetailsModel;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNameLg() {
        return nameLg;
    }

    public void setNameLg(String nameLg) {
        this.nameLg = nameLg;
    }

    public String getHouseLg() {
        return houseLg;
    }

    public void setHouseLg(String houseLg) {
        this.houseLg = houseLg;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getCoLg() {
        return coLg;
    }

    public void setCoLg(String coLg) {
        this.coLg = coLg;
    }

    public String getDistLg() {
        return distLg;
    }

    public void setDistLg(String distLg) {
        this.distLg = distLg;
    }

    public String getLocLg() {
        return locLg;
    }

    public void setLocLg(String locLg) {
        this.locLg = locLg;
    }

    public String getPcLg() {
        return pcLg;
    }

    public void setPcLg(String pcLg) {
        this.pcLg = pcLg;
    }

    public String getStateLg() {
        return stateLg;
    }

    public void setStateLg(String stateLg) {
        this.stateLg = stateLg;
    }

    public String getSubdistLg() {
        return subdistLg;
    }

    public void setSubdistLg(String subdistLg) {
        this.subdistLg = subdistLg;
    }

    public String getVtcLg() {
        return vtcLg;
    }

    public void setVtcLg(String vtcLg) {
        this.vtcLg = vtcLg;
    }

    public String getVtcCodeLg() {
        return vtcCodeLg;
    }

    public void setVtcCodeLg(String vtcCodeLg) {
        this.vtcCodeLg = vtcCodeLg;
    }

    public String getPoLg() {
        return poLg;
    }

    public void setPoLg(String poLg) {
        this.poLg = poLg;
    }

    public String getStreetLg() {
        return streetLg;
    }

    public void setStreetLg(String streetLg) {
        this.streetLg = streetLg;
    }

    public String getLmLg() {
        return lmLg;
    }

    public void setLmLg(String lmLg) {
        this.lmLg = lmLg;
    }

    public String getLm() {
        return lm;
    }

    public void setLm(String lm) {
        this.lm = lm;
    }

    static public AadhaarResponseItem create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, AadhaarResponseItem.class);
    }

    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public String getHouse() {
        return house;
    }

    public void setHouse(String house) {
        this.house = house;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getVtc() {
        return vtc;
    }

    public void setVtc(String vtc) {
        this.vtc = vtc;
    }

    public String getVtccode() {
        return vtccode;
    }

    public void setVtccode(String vtccode) {
        this.vtccode = vtccode;
    }

    public String getCo() {
        return co;
    }

    public void setCo(String co) {
        this.co = co;
    }

    public String getDist() {
        return dist;
    }

    public void setDist(String dist) {
        this.dist = dist;
    }

    public String getSubdist() {
        return subdist;
    }

    public void setSubdist(String subdist) {
        this.subdist = subdist;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPc() {
        return pc;
    }

    public void setPc(String pc) {
        this.pc = pc;
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    public String getPo() {
        return po;
    }

    public void setPo(String po) {
        this.po = po;
    }

    public String getPht() {
        return pht;
    }

    public void setPht(String pht) {
        this.pht = pht;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }

    public String getTxn() {
        return txn;
    }

    public void setTxn(String txn) {
        this.txn = txn;
    }

    public String getErr() {
        return err;
    }

    public void setErr(String err) {
        this.err = err;
    }

    public String getTs() {
        return ts;
    }

    public void setTs(String ts) {
        this.ts = ts;
    }
}
