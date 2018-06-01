package com.nhpm.PrintCard;

import android.hardware.usb.UsbDevice;

public interface UsbPermissionRequestor {

    void requestUsbPermission(UsbDevice usbDevice);
}