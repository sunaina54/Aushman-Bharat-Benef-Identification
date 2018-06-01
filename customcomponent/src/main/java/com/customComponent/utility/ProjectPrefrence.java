package com.customComponent.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.customComponent.GpsData;

/**
 * Created by Anand on 27-04-2016.
 */
public class ProjectPrefrence {
    private static ProjectPrefrence prefrence;

    public static String getSharedPrefrenceData(String projectPrefName,String contentPrefName,Context context){
        try {
            SharedPreferences prefs = context.getSharedPreferences(projectPrefName,
                    Context.MODE_PRIVATE);
            String str = prefs.getString(contentPrefName, null);
            return str;
        } catch (Exception e) {

        }
        return null;
    }

    public static void saveSharedPrefrenceData(String projectPrefName,String contentPrefName,String content,Context context){
        try {
            SharedPreferences prefs = context.getSharedPreferences(projectPrefName,
                    Context.MODE_PRIVATE);
//		String serializedData = item.serialize();
            SharedPreferences.Editor prefEditor = prefs.edit();
            prefEditor.putString(contentPrefName, content);
            Log.i("", "User Prefrence : Data saved successfully");
            prefEditor.commit();
            prefEditor.clear();
        } catch (Exception e) {
            Log.i("", "Exception : " + e.toString());
        }
    }
    public static void removeSharedPrefrenceData(String projectPrefName,String contentPrefName,Context context){
        try {
            SharedPreferences prefs = context.getSharedPreferences(projectPrefName,
                    Context.MODE_PRIVATE);
//		String serializedData = item.serialize();
            SharedPreferences.Editor prefEditor = prefs.edit();
            prefEditor.remove(contentPrefName);
            prefEditor.commit();
            prefEditor.clear();
        } catch (Exception e) {
            Log.i("", "Exception : " + e.toString());
        }
    }

   /* public static void saveGpsPrefrence(GpsData gpsData, Context context) {
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
    */

}
