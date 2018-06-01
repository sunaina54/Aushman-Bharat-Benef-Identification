package com.nhpm.Utility;

import android.content.Context;
import android.content.pm.PackageManager;

/**
 * Created by PSQ on 1/13/2017.
 */

public class DeviceInfo {

    public static int findApplicationVersionCode(Context context){
        PackageManager packageManager = context.getPackageManager();
        String packageName = context.getPackageName();
        int versionCode = 0; // initialize String

        try {
            versionCode = packageManager.getPackageInfo(packageName, 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return  versionCode;
    }
}
