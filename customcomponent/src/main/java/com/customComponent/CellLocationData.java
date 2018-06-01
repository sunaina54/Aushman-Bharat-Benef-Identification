package com.customComponent;

public class CellLocationData {

    private String latitude;
    private String longitude;

    public CellLocationData(String latitude, String longitude) {
        super();
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public CellLocationData() {
        super();
        // TODO Auto-generated constructor stub
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }


}
