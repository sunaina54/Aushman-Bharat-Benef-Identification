package com.nhpm.Models;

/**
 * Created by psqit on 8/27/2016.
 */
public class MemberModel {
    private String name,id;
    private String slnohhd_npr;
    private String tin;

  /*  public MemberModel(String name, String id) {
        this.name = name;
        this.id = id;
    }*/


    public String getTin() {
        return tin;
    }

    public void setTin(String tin) {
        this.tin = tin;
    }

    public String getSlnohhd_npr() {
        return slnohhd_npr;
    }

    public void setSlnohhd_npr(String slnohhd_npr) {
        this.slnohhd_npr = slnohhd_npr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
