package com.aadhar.commonapi;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.acpl.Startek_DeviceHandler;

import java.io.IOException;

//import android.content.BroadcastReceiver;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.hardware.usb.UsbDevice;
//import android.hardware.usb.UsbManager;
//import android.util.Log;


public class BiometricDeviceHandlerOld {
	private Startek_DeviceHandler acpl_handler;
	private boolean acpl_dev = false;
//	private static final String ACTION_USB_PERMISSION = "com.acpl";
	private static Context mContext = null;
//	private static IntentFilter mPermissionFilter = null;
//	private UsbDevice d;
	HelperInterface handlerInterface = null;


	public void SetApplicationContext(Context parentContext)
	{
/*		IntentFilter filter = new IntentFilter();
		filter.addAction("android.hardware.usb.action.USB_DEVICE_ATTACHED");
		filter.addAction("android.hardware.usb.action.USB_DEVICE_DETACHED");
		parentContext.getApplicationContext().registerReceiver(
				this.mUsbPlugEvents, filter);

		filter.addAction(ACTION_USB_PERMISSION);
		filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
		filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
		parentContext.getApplicationContext().registerReceiver(mUsbReceiver,
				filter);


		if (mContext != parentContext) {
			mContext = parentContext;
			mPermissionFilter = new IntentFilter(ACTION_USB_PERMISSION);
			mContext.getApplicationContext().registerReceiver(
					mUsbDevicePermissions, mPermissionFilter);
		}
*/		try {
			acpl_handler.SetApplicationContext(parentContext);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public BiometricDeviceHandlerOld(HelperInterface callback)
			throws BiometricDeviceHandler.BioException {
		
		try {
			acpl_handler = new Startek_DeviceHandler(callback);
			acpl_dev = true;
		} catch (Startek_DeviceHandler.BioException ex) {
			acpl_dev = false;
		//	throw new BioException(ex.getMessage());
		}

	}

	public void BeginCapture() {
		if (acpl_dev)
		{
//			int did = acpl_handler.GetAttachedDeviceID();
			acpl_handler.BeginCapture();
		}
		else
		{
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
			alertDialogBuilder.setTitle("Error");
			alertDialogBuilder
					.setMessage("No device connected")
					.setCancelable(false)
					.setPositiveButton("Ok",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							});
			try {
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();
			}
			catch (Exception e1)
			{
//				String msg1 = e1.getMessage();
			}

		}
	}

	public String GetDeviceMake() {
		if (acpl_dev)
			return acpl_handler.GetDeviceMake();
		return "";
	}

	public String GetDeviceModel() {
		if (acpl_dev)
			return acpl_handler.GetDeviceModel();
		return "";
	}

	public String GetDeviceSerialNumber() {
		if (acpl_dev)
			return acpl_handler.GetDeviceSerialNumber();
		return "";
	}

	public int GetDeviceType() {
		if (acpl_dev)
			return acpl_handler.GetDeviceType();
		return 0;
	}

	public void SetFingerprintBiometricDataType(String val) {
		if (acpl_dev)
			acpl_handler.SetFingerprintBiometricDataType(val);
	}

	public int GetAttachedDeviceID() {
		if (acpl_dev)
			return acpl_handler.GetAttachedDeviceID();
		return 0;
	}

	public int GetAttachedDeviceVendorID() {
		if (acpl_dev)
			return acpl_handler.GetAttachedDeviceVendorID();
		return 0;
	}

	public int InitDevice(int deviceID) {
		if (acpl_dev)
		{
			int retval = 0;
			retval = acpl_handler.InitDevice(deviceID);
			return retval;
		}
		return 0;
	}
	
	public void unInitDevice()
	{
		acpl_handler.unInitDevice();
	}
	public void SetCaptureTimeout(int TimeOutval) {
		if (acpl_dev)
			acpl_handler.SetCaptureTimeout(TimeOutval);
	}

	public void SetThresholdQuality(int val) {
		if (acpl_dev)
			acpl_handler.SetThresholdQuality(val);
	}

	public static class BioException extends IOException {
		private static final long serialVersionUID = 1L;

		public BioException() {
		}

		public BioException(String ftStatusMsg) {
			super();
		}
	}
}
