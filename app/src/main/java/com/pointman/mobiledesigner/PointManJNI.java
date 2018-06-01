package com.pointman.mobiledesigner;

/**
 * Created by dev1 on 01-05-2017.
 */

import android.R.bool;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class PointManJNI {


    static {
        try {
            try {
                System.loadLibrary("CardDesigner");
            } catch (UnsatisfiedLinkError e) {
                try {
                    System.load("CardDesigner");
                } catch (UnsatisfiedLinkError ex) {

                }
            }
        } catch (Exception ex) {

        }

    }


    public native String SetNetworkInfo(String ip, int port);

    public native String SetConnect(Boolean cont);

    public native byte[] PrintCommand(int command, byte[] buffer, String frontc, String frontk, String backc, String backk);

    public native byte[] GetCommandBYTE(int i, byte[] senddata, int b);


    // USB

    public native String getdevice(String dname);

    public native int OpenDevice(String dname, int fd, int iface);

    public native int CloseDevice();


}
