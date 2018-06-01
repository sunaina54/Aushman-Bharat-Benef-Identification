/***********************************************
 * CONFIDENTIAL AND PROPRIETARY
 * The source code and other information contained herein is the confidential and the exclusive property of
 * ZIH Corp. and is subject to the terms and conditions in your end user license agreement.
 * Copyright ZIH Corp. 2016
 * ALL RIGHTS RESERVED
 ***********************************************/

package com.nhpm.PrintCard;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class UsbHelper {

    private static final String LOG_TAG = UsbHelper.class.getName();
    public static final String ACTION_USB_PERMISSION = "com.zebra.printersetup.ACTION_USB_PERMISSION";


    @Nullable
    public static UsbManager getUsbManager(@NonNull Context context) {
        Log.i(LOG_TAG, "getUsbManager");

        UsbManager usbManager = null;
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_USB_HOST)) {
            usbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        } else {
            Log.i(LOG_TAG, "USB host not supported");
        }
        return usbManager;
    }

    public static List<UsbDevice> discoverUsbDevices(@NonNull Context context) {
        Log.i(LOG_TAG, "discoverUsbDevices");

        
        List<UsbDevice> usbDeviceList = new ArrayList<UsbDevice>();

        UsbManager usbManager = getUsbManager(context);
        if (usbManager != null) {
            Iterator<UsbDevice> devices = usbManager.getDeviceList().values().iterator();
            while (devices.hasNext()) {
                UsbDevice usbDevice = devices.next();
                if (usbDevice.getInterface(0).getInterfaceClass() == UsbConstants.USB_CLASS_PRINTER) {
                    usbDeviceList.add(usbDevice);
                }
            }
        }

        return usbDeviceList;
    }

    public static void selectUsbPrinter(Activity activity, UsbDevice selectedPrinter) {
        UsbManager usbManager = getUsbManager(activity);
        UsbDevice usbDevice = selectedPrinter;

        if (!usbManager.hasPermission(usbDevice)) {
            PendingIntent permissionIntent = PendingIntent.getBroadcast(activity, 0, new Intent(UsbHelper.ACTION_USB_PERMISSION), 0);
            usbManager.requestPermission(usbDevice, permissionIntent);
        }
    }

    public static final class UsbDisconnectionReceiver extends BroadcastReceiver {

        private static final String LOG_TAG = UsbDisconnectionReceiver.class.getName();

        @Override
        public void onReceive(@NonNull Context context, @NonNull Intent intent) {
            String action = intent.getAction();
            Log.i(LOG_TAG, "onReceive - action: " + action);

            if (action.equalsIgnoreCase(UsbManager.ACTION_USB_DEVICE_DETACHED)) {
                UsbDevice usbDevice = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                if (usbDevice != null) {
                    Intent goToMainActivity = new Intent(context, PrintCardMainActivity.class);
                    goToMainActivity.setAction(action);
                    goToMainActivity.putExtra(UsbManager.EXTRA_DEVICE, usbDevice);

                    goToMainActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(goToMainActivity);
                }
            }
        }
    }
}
