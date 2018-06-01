package com.customComponent;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * The manager class to manage the locations and sending to server.
 */
public class UserLocationManager {

    public static volatile UserLocationManager userLocationManager;
    static {
        userLocationManager = new UserLocationManager();
    }

    private LocationListener mLocationListener = null;
    private LocationManager mLocationManager = null;
    private boolean isGPSEnabled;

    /**
     * Hidden constructor
     */
    private UserLocationManager() {
    }

    /**
     * Method to get instance of ContentManager
     *
     * @return contentManager
     */
    public static UserLocationManager getInstance() {
        if (userLocationManager == null) {
            userLocationManager = new UserLocationManager();
        }
        return userLocationManager;
    }

    /**
     * Method to check is gps enabled or not
     *
     * @return isGPSEnabled
     */
    public boolean isGPSEnabled(Context context) {
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) context
                    .getSystemService(Context.LOCATION_SERVICE);
        }
        isGPSEnabled = mLocationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isGPSEnabled;
    }

    /**
     * This method used to get the location and send it to server
     *
     * @param context
     */

    public GpsData getGpsData(Context context) {
        GpsData gpsData;
        try {
            mLocationManager = (LocationManager) context
                    .getSystemService(Context.LOCATION_SERVICE);
            if (mLocationManager != null) {
                getLocationFromNetwork(context);
              /*  if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                }*/
                Location location = mLocationManager
                        .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                Log.i("", "Get location 1 " + location);
                if (location == null) {
                    getLocationFromGPS(context);
                    location = mLocationManager
                            .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    Log.i("", "Get location 1 " + location);
                }
                mLocationManager.removeUpdates(mLocationListener);
                gpsData = new GpsData();
                gpsData.setLatitude(location.getLatitude());
                gpsData.setLongitude(location.getLongitude());
                gpsData.setDateTime(gpsData.formattedDateTime(location.getTime()));

                return gpsData;
            }

        } catch (Exception e) {
            Log.i("", "Get location : " + e.toString());
        }
        return null;
    }

    public Location getLocation(Context context) {
        try {
            mLocationManager = (LocationManager) context
                    .getSystemService(Context.LOCATION_SERVICE);
            if (mLocationManager != null) {
                getLocationFromNetwork(context);
               /* if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.

                }*/
                Location location = mLocationManager
                        .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                Log.i("", "Get location 1 " + location);
                if (location == null) {
                    getLocationFromGPS(context);
                    location = mLocationManager
                            .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    Log.i("", "Get location 1 " + location);
                }
                mLocationManager.removeUpdates(mLocationListener);
                return location;
            }

        } catch (Exception e) {
            Log.i("", "Get location : " + e.toString());
        }
        return null;
    }

    /**
     * Get the location from GPS
     *
     * @param context
     */
    private void getLocationFromGPS(Context context) {
        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.i("", "Location from the gps : " + location);
            }

            @Override
            public void onProviderDisabled(String provider) {
                Log.i("", "Location from the  provider : " + provider);
            }

            @Override
            public void onProviderEnabled(String provider) {
                Log.i("", "Location from the  provider  enabled: " + provider);
            }

            @Override
            public void onStatusChanged(String provider, int status,
                                        Bundle extras) {
                Log.i("", "Location from the on status : " + provider + status);
            }
        };
       /* if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            return;
        }*/
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                0L, 0.0F, mLocationListener);
    }

    /**
     * Get the location from network.
     *
     * @param context
     */
    private void getLocationFromNetwork(Context context) {
        mLocationListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onStatusChanged(String provider, int status,
                                        Bundle extras) {
            }
        };
        /*if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }*/
        mLocationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, 0L, 0.0F, mLocationListener);
    }

    public void stopGpsUse() {
//		if (mLocationManager != null) {
//			mLocationManager.removeUpdates(mLocationListener);
//			mLocationManager = null;
//		}
    }

    public Address getAddress(double latitude, double longitude, Context mContext) {
        Geocoder geocoder;
        List<android.location.Address> addresses;
        Address address = null;

        geocoder = new Geocoder(mContext, Locale.getDefault());

        try {
            Log.i("", "Address sssss: " + Geocoder.isPresent());
            addresses = geocoder.getFromLocation(latitude, longitude, 3);
            // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            if (addresses != null) {
                Log.i("", "Address sssss: " + addresses.size());
                address = new Address();
                String addr = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();
                address.setCity(city);
                String state = addresses.get(0).getAdminArea();
                address.setState(state);
                String country = addresses.get(0).getCountryName();
                address.setCountry(country);
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName();
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return address;
    }

    public CellLocationData getCellLocation(Context context) {
        CellLocationData cellLoc = null;
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        GsmCellLocation cellLocation = (GsmCellLocation) telephonyManager.getCellLocation();

        String networkOperator = telephonyManager.getNetworkOperator();
        String mcc = networkOperator.substring(0, 3);
        String mnc = networkOperator.substring(3);
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


        int cid = cellLocation.getCid();
        int lac = cellLocation.getLac();
        String strURLSent =
                "http://www.opencellid.org/cell/get?mcc=" + mcc
                        + "&mnc=" + mnc
                        + "&cellid=" + cid
                        + "&lac=" + lac
                        + "&format=json&key=5e3b745c-d63b-4904-a7af-1fa7fdbca65f";
        Log.i("", "OpenCellid Resonse : " + strURLSent);
        // "http://opencellid.org/cell/get?key=5e3b745c-d63b-4904-a7af-1fa7fdbca65f"+"&mcc="260&mnc=2&lac=10250&cellid=26511&format=json
        String locResponse = CustomHttpClient.getStringRequest(strURLSent);
        Log.i("", "OpenCellid Resonse : " + locResponse);
        try {
            if (locResponse != null) {
                JSONObject mainObj = new JSONObject(locResponse);
                if (mainObj != null) {

                    if (mainObj.has("lon")) {
                        cellLoc = new CellLocationData();
                        cellLoc.setLongitude(mainObj.getString("lon"));

                    }

                    if (mainObj.has("lat")) {
                        cellLoc.setLatitude(mainObj.getString("lat"));
                    }
                }
            }
        } catch (Exception e) {

        }
        return cellLoc;
    }
}
