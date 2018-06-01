package com.customComponent.location;

import android.content.Context;
import android.location.Location;

import com.customComponent.CellLocationData;
import com.customComponent.TaskListener;
import com.google.gson.Gson;

/**
 * Created by Anand on 14-03-2016.
 */
public class CurrentLocation {

    private static CurrentLocation currentLocation;

    private String defaultLatitude;
    private String defaultLongitude;
    private DeviceLocation deviceLocation;
  //  private CellLocation cellLocation;
    private OpenCellLocation openCellLocation;
    private Context context;

    static public CurrentLocation create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, CurrentLocation.class);
    }

    public String serialize() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }


    public CurrentLocation getInstance(Context context){
        this.context=context;
        if(currentLocation==null){
            currentLocation=new CurrentLocation();
        }
        return  currentLocation;
    }


    public DeviceLocation getDeviceLocation() {
        return deviceLocation;
    }

    public void setDeviceLocation(DeviceLocation deviceLocation) {
        this.deviceLocation = deviceLocation;
    }

    public OpenCellLocation getOpenCellLocation() {
        return openCellLocation;
    }

    public void setOpenCellLocation(OpenCellLocation openCellLocation) {
        this.openCellLocation = openCellLocation;
    }

    public String getDefaultLatitude() {
        return defaultLatitude;
    }

    public void setDefaultLatitude(String defaultLatitude) {
        this.defaultLatitude = defaultLatitude;
    }

    public String getDefaultLongitude() {
        return defaultLongitude;
    }

    public void setDefaultLongitude(String defaultLongitude) {
        this.defaultLongitude = defaultLongitude;
    }

    public void findDeviceLocation(){
       Location location= UserLocationManager.getInstance().getLocation(context);
        deviceLocation=new DeviceLocation();
        deviceLocation.setLatitude(location.getLatitude());
        deviceLocation.setLongitude(location.getLongitude());
    }

    public void findCellLocation(){
        TaskListener taskListener=new TaskListener() {
            @Override
            public void execute() {
            CellLocationData cellLocationData= UserLocationManager.getInstance().getCellLocation(context);
             openCellLocation=new OpenCellLocation();
                openCellLocation.setLongitude(Double.parseDouble(cellLocationData.getLongitude()));
                openCellLocation.setLatitude(Double.parseDouble(cellLocationData.getLatitude()));

            }

            @Override
            public void updateUI() {

            }
        };


    }
}
