package com.customComponent;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.customComponent.location.CurrentLocation;

public class SettingPrefrence {
    private final static String CONTACT_PREFERENCE = "OVAMBA_PLACE_PREF";
    private final static String FAVORITE_PREF = "PLACES";
    private final static String OVAMBAPLATFORM_PREFERENCE = "OVAMBA_PLTFORM_PREF";
    private final static String GPS_PREFERENCE = "GPSS_PREF";
    private final static String LOCATION_PREF="COM.ORG.PSQUICKIT.LOCATION_PREF";
    private final static String CUSTOM_COMPONENT_PREF="COM.ORG.PSQUICKIT.CUSTOM_COMPONENT";

    public static void saveRadiusPrefrence(int redius, Context context) {
        try {
            SharedPreferences prefs = context.getSharedPreferences(CONTACT_PREFERENCE,
                    Context.MODE_PRIVATE);
//		String serializedData = item.serialize();
            SharedPreferences.Editor prefEditor = prefs.edit();
            prefEditor.putInt(FAVORITE_PREF, redius);
            Log.i("", "User Prefrence : Data saved successfully");
            prefEditor.commit();
        } catch (Exception e) {
            Log.i("", "Exception : " + e.toString());
        }
    }

    public static int getRadiusPrefrence(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(CONTACT_PREFERENCE,
                Context.MODE_PRIVATE);
        int radius = prefs.getInt(FAVORITE_PREF, -1);

        // Create a new object from the serialized data with the same state

        return radius;
    }

    public static void saveGpsPrefrence(GpsData gpsData, Context context) {
        try {
            SharedPreferences prefs = context.getSharedPreferences(OVAMBAPLATFORM_PREFERENCE,
                    Context.MODE_PRIVATE);
//		String serializedData = item.serialize();
            SharedPreferences.Editor prefEditor = prefs.edit();
            prefEditor.putString(GPS_PREFERENCE, gpsData.serialize());
            Log.i("", "User Prefrence : Data saved successfully");
            prefEditor.commit();
        } catch (Exception e) {
            Log.i("", "Exception : " + e.toString());
        }
    }

    public static GpsData getPrefrenceGps(GpsData gpsData, Context context) {
        try {
            SharedPreferences prefs = context.getSharedPreferences(OVAMBAPLATFORM_PREFERENCE,
                    Context.MODE_PRIVATE);
            String str = prefs.getString(GPS_PREFERENCE, null);
            return GpsData.create(str);
        } catch (Exception e) {

        }
        return null;
    }

    public static void savePrefrence(Context context,String data,String applicationDomain,String prefrenceName) {
        try {
            SharedPreferences prefs = context.getSharedPreferences(applicationDomain,
                    Context.MODE_PRIVATE);
//		String serializedData = item.serialize();
            SharedPreferences.Editor prefEditor = prefs.edit();
            prefEditor.putString(prefrenceName,data);
            Log.i("", "User Prefrence : Data saved successfully");
            prefEditor.commit();
            prefEditor.clear();
        } catch (Exception e) {
            Log.i("", "Exception : " + e.toString());
        }
    }

    public static String getPrefrence(Context context,String applicationDomain,String prefrenceName) {
        try {
            SharedPreferences prefs = context.getSharedPreferences(applicationDomain,
                    Context.MODE_PRIVATE);
            String str = prefs.getString(prefrenceName, null);
            return str;
           // return GpsData.create(str);
        } catch (Exception e) {

        }
        return null;
    }

    public static CurrentLocation getPrefrenceLocation( Context context) {
        try {
            SharedPreferences prefs = context.getSharedPreferences(CUSTOM_COMPONENT_PREF,
                    Context.MODE_PRIVATE);
            String str = prefs.getString(LOCATION_PREF, null);
            return CurrentLocation.create(str);
        } catch (Exception e) {

        }
        return null;
    }

    public static void savePrefrenceLocation(CurrentLocation currentLocation, Context context) {
        try {
            SharedPreferences prefs = context.getSharedPreferences(OVAMBAPLATFORM_PREFERENCE,
                    Context.MODE_PRIVATE);
//		String serializedData = item.serialize();
            SharedPreferences.Editor prefEditor = prefs.edit();
            prefEditor.putString(LOCATION_PREF, currentLocation.serialize());
         //   Log.i("", "User Prefrence : Data saved successfully");
            prefEditor.commit();
            prefEditor.clear();
        } catch (Exception e) {
            Log.i("", "Exception : " + e.toString());
        }
    }



}
