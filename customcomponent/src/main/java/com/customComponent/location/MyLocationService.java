package com.customComponent.location;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;

import com.customComponent.CellLocationData;
import com.customComponent.CustomHttpClient;

import org.json.JSONObject;

/**
 * Created by Anand on 15-03-2016.
 */
public class MyLocationService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */

    private final String TAG="My Location Service";
    private String SERVICE_NAME=getClass().getName();
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 1000;
    private static final float LOCATION_DISTANCE = 10f;
    private OpenCellLocation cellLocation;
    private DeviceLocation deviceLocation;
    /*public MyLocationService() {
        //super(name);
    }*/

    public MyLocationService() {
        super("com.org.psq.customCompoent.MyLocationService");
    }

    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        initializeLocationManager();
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[1]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[0]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }
        getCellLocation(getApplicationContext());
    }

    LocationListener[] mLocationListeners = new LocationListener[] {
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };

    private class LocationListener implements android.location.LocationListener
    {
        Location mLastLocation;

        public LocationListener(String provider)
        {
            Log.e(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);
            Log.e(TAG, "LocationListener " + mLastLocation.getLatitude()+", Longitude : "+mLastLocation.getLongitude());

        }

        @Override
        public void onLocationChanged(Location location)
        {
            Log.e(TAG, "onLocationChanged: " + location);
            mLastLocation.set(location);
        }

        @Override
        public void onProviderDisabled(String provider)
        {
            Log.e(TAG, "onProviderDisabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider)
        {
            Log.e(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras)
        {
            Log.e(TAG, "onStatusChanged: " + provider);
        }
    }

    public void  getCellLocation(Context context) {
        String mcc = null,mnc=null,cid=null,lac = null;
        String cellularLocationResponse=null;

        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        GsmCellLocation cellLocation = (GsmCellLocation) telephonyManager.getCellLocation();

        String networkOperator = telephonyManager.getNetworkOperator();
        Log.i(TAG," Network operator : "+networkOperator);


        if(networkOperator!=null && !networkOperator.equalsIgnoreCase("")) {
            mcc = networkOperator.substring(0, 3);
            mnc = networkOperator.substring(3);
        }
        Log.i(TAG," Gcm Cell Location : "+cellLocation);
        if(cellLocation !=null) {
            cid = String.valueOf(cellLocation.getCid());
            lac = String.valueOf(cellLocation.getLac());
        }



         /*  textMCC.setText("mcc: " + mcc);
           textMNC.setText("mnc: " + mnc);*/
           /*{
               "lon": 21.011393650000002,
	    	   "lat": 52.2308017,
	    	   "mcc": 260,
	    	   "mnc": 2,
	    	   "lac": 10250,
	    	   "cellid": 26511,
	    	   "averageSignalStrength": -65,
	    	   "range": 34,
	    	   "samples": 2,
	    	   "changeable": true,
	    	   "radio": "GSM"
	    	 }*/
 if(mcc!=null&& mcc!=null && cid!=null && lac !=null  ){
     String strURLSent =
             "http://www.opencellid.org/cell/get?mcc=" + mcc
                     + "&mnc=" + mnc
                     + "&cellid=" + cid
                     + "&lac=" + lac
                     + "&format=json&key=5e3b745c-d63b-4904-a7af-1fa7fdbca65f";
     Log.i(TAG, "OpenCellid Resonse : " + strURLSent);
     String locResponse = CustomHttpClient.getStringRequest(strURLSent);
     Log.i(TAG, "OpenCellid Resonse : " + locResponse);
     parsecellLocationResp(locResponse);

 }




        // "http://opencellid.org/cell/get?key=5e3b745c-d63b-4904-a7af-1fa7fdbca65f"+"&mcc="260&mnc=2&lac=10250&cellid=26511&format=json


    }

    private void parsecellLocationResp(String locResponse){
        try {
            if (locResponse != null) {
                JSONObject mainObj = new JSONObject(locResponse);
                if (mainObj != null) {

                    if (mainObj.has("lon")) {
                        cellLocation=new OpenCellLocation();
                        cellLocation.setLongitude(Double.parseDouble(mainObj.getString("lon")));

                    }

                    if (mainObj.has("lat")) {
                        cellLocation.setLatitude(Double.parseDouble(mainObj.getString("lat")));
                    }
                }
            }
        } catch (Exception e) {

        }
    }
}
