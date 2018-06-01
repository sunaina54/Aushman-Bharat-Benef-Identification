package com.customComponent.utility;


import android.content.Context;
import android.util.Log;

import com.customComponent.Address;
import com.customComponent.CustomHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Anand on 11-04-2016.
 */
public class LocationUtils {
    private final static String TAG="Location Utils ";

  /*  public static com.customComponent.Address convertLatLongintoAddress(Context context,double latitude,double longitude){
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        com.customComponent.Address address = null;
        List<Address> addresses  = null;
        try {
            addresses = geocoder.getFromLocation(latitude,longitude, 1);
            Log.i(TAG, "City : " +addresses.size());
            if(addresses !=null && addresses.size()>0) {
                String city = addresses.get(0).getLocality();
                Log.i(TAG,"City : "+city);
                String state = addresses.get(0).getAdminArea();
                String zip = addresses.get(0).getPostalCode();
                String country = addresses.get(0).getCountryName();
                address=new com.customComponent.Address();
                address.setCity(city);
                address.setState(state);
                address.setCountry(country);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.i(TAG, "City : " + e.toString());
        }
        return  address;
    }*/


    public static Address parseGeoCodeAddress(String url){
       // String geoCodeUrl = "http://maps.googleapis.com/maps/api/geocode/json?sensor=true_or_false&latlng=";
        Address address = null;
      //  String wetherUrl=OvambaConstants.weatherUrl+gpsData.getLatitude()+","+ gpsData.getLongitude();
        String resp = CustomHttpClient.getStringRequest(url);
        Log.i("Location Utils : ","Response : "+resp+" Url: "+url);

        try {
            JSONObject result = new JSONObject(resp);
            if (result != null) {
                JSONArray resultArr = result.getJSONArray("results");
                if (resultArr != null && resultArr.length() > 0) {
                    JSONObject resultObj = resultArr.getJSONObject(0);
                    if (resultObj != null) {
                        JSONArray arr = resultObj.getJSONArray("address_components");
                        if (arr != null && arr.length() > 0) {
                            address = new Address();
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject obj = arr.getJSONObject(i);
                                if (obj != null) {
                                    if (obj.getJSONArray("types").getString(0).equalsIgnoreCase("locality")) {
                                        address.setCity(obj.getString("long_name"));

                                    }
                                    if (obj.getJSONArray("types").getString(0).equalsIgnoreCase("administrative_area_level_1")) {
                                        address.setState(obj.getString("long_name"));

                                    }
                                    if (obj.getJSONArray("types").getString(0).equalsIgnoreCase("country")) {
                                        address.setCountry(obj.getString("long_name"));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            Log.i("", " GetCurrentClass Exception : " + e.toString());
        }
        return address;


    }
}
