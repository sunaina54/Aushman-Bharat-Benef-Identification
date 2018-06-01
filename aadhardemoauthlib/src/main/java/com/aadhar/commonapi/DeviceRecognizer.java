//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.aadhar.commonapi;

import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.util.Log;

import com.aadhar.Global;

import java.util.Iterator;

public class DeviceRecognizer {
    public static final int startekPID = 33312;
    public static final int startekVID = 3018;
    public static final int fm220U = 33312;
    public static final int mfs100PID = 34323;
    public static final int mfs100VID = 1204;
    public static final int mfs100 = 41;
    public static final int iritechVID = 8035;
    public static final int iritechPID = 61441;
    public static final int iritechDeviceID = 1002;
    public static final int morphoDeviceID = 71;
    public static final int bioenableVID = 2694;
    public static final int bioenablePID = 1616;
    public static final int hfdu08 = 1616;
    public static final int secugenPID = 8704;
    public static final int secugenVID = 4450;
    public static final int pro20 = 8704;
    public static final int EPI_100 = 1003;
    public static final int elkontouchPID = 8214;
    public static final int precisionuru4500 = 11;
    public static final int cogentPID = 8512;
    String deviceType;
    UsbManager mUsbManager;
    UsbDevice device;
    /*  public boolean morphoMSO1300Attached;
      public boolean morphoMSO1350Attached;
      public boolean morphoMSO30xAttached;
      public boolean morphoMSO35xAttached;
      public boolean mantraAttached;
      public boolean iritechAttached;
      public boolean startekAttached;
      public boolean bioenableAttached;
      public boolean secugenAttached;
      public boolean biometriquesAttached;
      public boolean precisionElkonTouchAttached;
      public boolean precisionuru4500Attached;
      public boolean scannerAttached;
      public boolean cogentAttached;*/
    Context applicationContext;

    public DeviceRecognizer(Context context) {
        this.applicationContext = context;
        Global.morphoMSO1300Attached = Global.morphoMSO1350Attached = Global.morphoMSO30xAttached = Global.morphoMSO35xAttached = Global.mantraAttached = Global.iritechAttached = Global.startekAttached = Global.bioenableAttached = Global.secugenAttached = Global.biometriquesAttached = Global.precisionElkonTouchAttached = Global.precisionuru4500Attached = Global.cogentAttached = Global.scannerAttached = false;
    }

    public void FindSupportedDevice() {
        Global.morphoMSO1300Attached = Global.morphoMSO1350Attached = Global.morphoMSO30xAttached = Global.morphoMSO35xAttached = Global.mantraAttached = Global.iritechAttached = Global.scannerAttached = Global.startekAttached = Global.secugenAttached = Global.biometriquesAttached = Global.precisionElkonTouchAttached = Global.precisionuru4500Attached = Global.cogentAttached = false;
        this.mUsbManager = (UsbManager)this.applicationContext.getSystemService(Context.USB_SERVICE);
        this.FindAttachedDevices();
    }

    public String GetAttachedDeviceName() {
        return Global.mantraAttached?"MANTRA":(Global.morphoMSO1300Attached?"MORPHO_MSO_1300":(Global.morphoMSO1350Attached?"MORPHO_MSO_1350":(Global.morphoMSO30xAttached?"MORPHO_MSO_30X":(Global.morphoMSO35xAttached?"MORPHO_MSO_35X":(Global.startekAttached?"STARTEK":(Global.iritechAttached?"IRITECH":(Global.bioenableAttached?"BIOENABLE":(Global.secugenAttached?"SECUGEN":(Global.biometriquesAttached?"BIOMETRIQUES":(Global.precisionElkonTouchAttached?"ELKONTOUCH":(Global.precisionuru4500Attached?"URU4500":(Global.cogentAttached?"3M COGENT":""))))))))))));
    }

    public int GetAttachedDeviceID() {
        return Global.mantraAttached?41:(Global.morphoMSO1300Attached?71:(Global.morphoMSO1350Attached?82:(Global.morphoMSO30xAttached?36:(Global.morphoMSO35xAttached?38:(Global.startekAttached?'舠':(Global.iritechAttached?1002:(Global.bioenableAttached?1616:(Global.secugenAttached?8704:(Global.biometriquesAttached?1003:(Global.precisionElkonTouchAttached?8214:(Global.precisionuru4500Attached?11:(Global.cogentAttached?8512:0))))))))))));
    }

    public int GetAttachedDeviceCount() {
        int deviceCount = 0;
        if(Global.mantraAttached) {
            ++deviceCount;
        }

        if(Global.morphoMSO1300Attached) {
            ++deviceCount;
        }

        if(Global.morphoMSO1350Attached) {
            ++deviceCount;
        }

        if(Global.morphoMSO30xAttached) {
            ++deviceCount;
        }

        if(Global.morphoMSO35xAttached) {
            ++deviceCount;
        }

        if(Global.startekAttached) {
            ++deviceCount;
        }

        if(Global.iritechAttached) {
            ++deviceCount;
        }

        if(Global.bioenableAttached) {
            ++deviceCount;
        }

        if(Global.secugenAttached) {
            ++deviceCount;
        }

        if(Global.biometriquesAttached) {
            ++deviceCount;
        }

        if(Global.precisionElkonTouchAttached) {
            ++deviceCount;
        }

        if(Global.precisionuru4500Attached) {
            ++deviceCount;
        }

        if(Global.cogentAttached) {
            ++deviceCount;
        }

        return deviceCount;
    }

    protected void FindAttachedDevices() {
        this.mUsbManager = (UsbManager)this.applicationContext.getSystemService(Context.USB_SERVICE);
        Iterator localIterator = this.mUsbManager.getDeviceList().values().iterator();
        Global.scannerAttached = false;

        do {
            try {
                this.device = (UsbDevice)localIterator.next();
                long l1 = (long)this.device.getProductId();
                long l2 = (long)this.device.getVendorId();
                System.out.println("Product Id =============> " + l1);
                System.out.println("Vendor Id ==============> " + l2);
                if(l1 == 71L && l2 == 1947L) {
                    Global.morphoMSO1300Attached = true;
                    Global.scannerAttached = true;
                }

                if(l1 == 82L && l2 == 1947L) {
                    Global.morphoMSO1350Attached = true;
                    Global.scannerAttached = true;
                }

                if(l1 == 36L && l2 == 1947L) {
                    Global.morphoMSO30xAttached = true;
                    Global.scannerAttached = true;
                }

                if(l1 == 38L && l2 == 1947L) {
                    Global.morphoMSO35xAttached = true;
                    Global.scannerAttached = true;
                }

                if(l1 == 61441L && l2 == 8035L) {
                    Global.iritechAttached = true;
                    Global.scannerAttached = true;
                }

                if(l1 == 34323L && l2 == 1204L) {
                    Global.mantraAttached = true;
                    Global.scannerAttached = true;
                }

                if(l1 == 4101L && l2 == 1204L) {
                    Global.mantraAttached = true;
                    Global.scannerAttached = true;
                }

                if(l1 == 33312L && l2 == 3018L) {
                    Global.startekAttached = true;
                    Global.scannerAttached = true;
                }

                if(l1 == 1616L && l2 == 2694L) {
                    Global.bioenableAttached = true;
                    Global.scannerAttached = true;
                }

                if(l1 == 8704L && l2 == 4450L) {
                    Global.secugenAttached = true;
                    Global.scannerAttached = true;
                }

                if(l1 == 13209L && l2 == 6380L) {
                    Global.biometriquesAttached = true;
                    Global.scannerAttached = true;
                }

                if(l1 == 8214L && l2 == 5246L) {
                    Global.precisionElkonTouchAttached = true;
                    Global.scannerAttached = true;
                }

                if(l1 == 11L && l2 == 1466L) {
                    Global.precisionuru4500Attached = true;
                    Global.scannerAttached = true;
                }

                if(l1 == 8512L && l2 == 7717L) {
                    Global.cogentAttached = true;
                    Global.scannerAttached = true;
                }
            } catch (Exception var7) {
                Log.e("Device Recognizer Error", var7.toString());
            }
        } while(localIterator.hasNext());

    }
}
