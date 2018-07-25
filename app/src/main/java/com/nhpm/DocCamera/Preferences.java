package com.nhpm.DocCamera;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Admin on 7/17/2016.
 */
public class Preferences {
    public static String IMAGE_PURPOSE = "imagePurpose";
    public static String LATITUDE = "Latitude";
    private SharedPreferences preferences;
    private SharedPreferences.Editor edit;
    public static String INLOCATION = "InLocation";
    public static String WORKLOCATION_YN = "workLocationYN";
    public static String SELFIEYN = "selfieYN";
    public static String HEADER_BG_COLOR = "headerBgColor";
    public static String HEADER_TEXT_COLOR = "headerTextColor";
    public static String USERLATITUDE = "UserLatitude";
    public static String USERLONGITUDE = "UserLongitude";
    public static String USERLOCATION_ACCURACY = "locationAccuracy";
    public static String LOCATIONSTATUS = "LocationStatus";
    public static String SITENAME = "SiteName";
    public static String LONGITUDE = "Longitude";
    public static String SITE_RADIUS = "SiteRadius";
    public static String USERNAME = "UserName";
    public static String ISONPREMISE = "isOnPremise";
    public static String ISTESTSERVER = "isTestServer";
    public static String ISPRODUCTION = "isProduction";

    public Preferences(Context context) {
        if (context != null) {
            preferences = context.getSharedPreferences("AttendanceTracker", context.MODE_PRIVATE);
            edit = preferences.edit();
        }
    }

    public void saveString(String strKey, String strValue) {
        edit.putString(strKey, strValue);
    }

    public void saveInt(String strKey, int value) {
        edit.putInt(strKey, value);
    }

    public void saveBoolean(String strKey, boolean value) {
        edit.putBoolean(strKey, value);
    }

    public void saveLong(String strKey, Long value) {
        edit.putLong(strKey, value);
    }

    public void saveDouble(String strKey, String value) {
        edit.putString(strKey, value);
    }

    public void remove(String strKey) {
        edit.remove(strKey);
    }

    public void commit() {
        edit.commit();
    }

    public String getString(String strKey, String defaultValue) {
        return preferences.getString(strKey, defaultValue);
    }

    public boolean getBoolean(String strKey, boolean defaultValue) {
        return preferences.getBoolean(strKey, defaultValue);
    }

    public int getInt(String strKey, int defaultValue) {
        return preferences.getInt(strKey, defaultValue);
    }

    public double getDouble(String strKey, double defaultValue) {
        return Double.parseDouble(preferences.getString(strKey, "" + defaultValue));
    }

    public long getLong(String strKey) {
        return preferences.getLong(strKey, 0);
    }

    public void clearPreferences() {
        edit.clear();
        edit.commit();
    }
}
