//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.aadhar.commonapi;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.acpl.Startek_DeviceHandler;
import com.bioenable.andriodwrapper.BioEnableWrapper;
import com.cogentsdk.mainpackage.CogentWrapper;
import com.secugen.SecuGenWrapper;

import java.io.IOException;

public class BiometricDeviceHandler {
    Startek_DeviceHandler acpl_handler = null;
    private boolean acpl_dev = false;
    final int fm220u = '舠';
    MantraWrapper mantra = null;
    final int mfs100 = 41;
    IrishieldMK2120UWrapper iritech = null;
    final int iris_mk2120u = 1002;
    MorphoWrapper morpho = null;
    final int morphoMSO1300 = 71;
    final int morphoMSO1350 = 82;
    final int morphoMSO30x = 36;
    final int morphoMSO35x = 38;
    BioEnableWrapper bioenable = null;
    final int bioenable_hfdu08 = 1616;
    SecuGenWrapper secugen = null;
    final int pro20 = 8704;
    BiomatiquesEPI1000Wraper biometiques = null;
    final int EPI_100 = 1003;
    PreEIKONTouch elkonTouch = null;
    final int E_TOUCH = 8214;
    PreUareU4500 precision_UareUHandler = null;
    final int precision_UareU4500 = 11;
    CogentWrapper mCogentWrapper = null;
    final int cogent_csd200 = 8512;
    HelperInterface handlerInterface = null;
    int invokerDeviceId;

    public BiometricDeviceHandler(HelperInterface callback, int deviceId, Activity act) {
        try {
            Log.d("Init Device ID:", "" + deviceId);
            this.invokerDeviceId = deviceId;
            if (this.invokerDeviceId == '舠') {
                try {
                    this.acpl_handler = new Startek_DeviceHandler(callback);
                    this.acpl_dev = true;
                } catch (Startek_DeviceHandler.BioException var4) {
                    this.acpl_dev = false;
                }
            } else if (this.invokerDeviceId == 41) {
                this.mantra = new MantraWrapper(callback);
            } else if (this.invokerDeviceId == 1002) {
                this.iritech = new IrishieldMK2120UWrapper(callback);
            } else if (this.invokerDeviceId != 71 && this.invokerDeviceId != 82 && this.invokerDeviceId != 36 && this.invokerDeviceId != 38) {
                if (this.invokerDeviceId == 1616) {
                    this.bioenable = new BioEnableWrapper(callback);
                } else if (this.invokerDeviceId == 8704) {
                    this.secugen = new SecuGenWrapper(callback);
                } else if (this.invokerDeviceId == 1003) {
                    this.biometiques = new BiomatiquesEPI1000Wraper(callback);
                } else if (this.invokerDeviceId == 8214) {
                    this.elkonTouch = new PreEIKONTouch(callback);
                } else if (this.invokerDeviceId == 11) {
                    this.precision_UareUHandler = new PreUareU4500(callback);
                } else if (this.invokerDeviceId == 8512) {
                    this.mCogentWrapper = new CogentWrapper(callback);
                }
            } else {
                this.morpho = new MorphoWrapper(callback);
            }
        } catch (Exception ex) {
            System.out.print(ex.toString());
            try {
                act.recreate();
            } catch (Exception fasd) {

            }
        }

    }

    public void SetApplicationContext(Context parentContext, Activity act) {
        try {
            if (this.invokerDeviceId == '舠') {
                try {
                    this.acpl_handler.SetApplicationContext(parentContext);
                } catch (Exception var3) {
                    var3.printStackTrace();
                }
            } else if (invokerDeviceId == 41) {
             try{
                mantra.SetApplicationContext(parentContext);
            } catch (Exception var3) {

            }
            } else if (this.invokerDeviceId == 1002) {
                this.iritech.SetApplicationContext(parentContext);
            } else if (this.invokerDeviceId == 1616) {
                this.bioenable.SetApplicationContext(parentContext);
            } else if (this.invokerDeviceId == 8704) {
                this.secugen.SetApplicationContext(parentContext);
            } else if (this.invokerDeviceId == 1003) {
                this.biometiques.SetApplicationContext(parentContext);
            } else if (this.invokerDeviceId == 8214) {
                this.elkonTouch.SetApplicationContext(parentContext);
            } else if (this.invokerDeviceId == 11) {
                this.precision_UareUHandler.SetApplicationContext(parentContext);
            } else if (this.invokerDeviceId == 8512) {
                this.mCogentWrapper.SetApplicationContext(parentContext);
            }
        } catch (Exception ex) {
            try {
                act.recreate();
            } catch (Exception adfv) {

            }
        }

    }

    public int GetAttachedDeviceID() {
        if (this.invokerDeviceId == '舠') {
            if (this.acpl_dev) {
                return this.acpl_handler.GetAttachedDeviceID();
            }
        } else {
            if (this.invokerDeviceId == 41) {
                return this.mantra.GetAttachedDeviceID();
            }

            if (this.invokerDeviceId == 1002) {
                return this.iritech.GetAttachedDeviceID();
            }

            if (this.invokerDeviceId == 71 || this.invokerDeviceId == 82 || this.invokerDeviceId == 36 || this.invokerDeviceId == 38) {
                return this.morpho.GetAttachedDeviceID();
            }

            if (this.invokerDeviceId == 1616) {
                return this.bioenable.GetAttachedDeviceID();
            }

            if (this.invokerDeviceId == 8704) {
                return this.secugen.GetAttachedDeviceID();
            }

            if (this.invokerDeviceId == 1003) {
                return this.biometiques.GetAttachedDeviceID();
            }

            if (this.invokerDeviceId == 8214) {
                return this.elkonTouch.GetAttachedDeviceID();
            }

            if (this.invokerDeviceId == 11) {
                return this.precision_UareUHandler.GetAttachedDeviceID();
            }

            if (this.invokerDeviceId == 8512) {
                return this.mCogentWrapper.GetAttachedDeviceID();
            }
        }

        return -1;
    }

    public int GetAttachedDeviceVendorID() {
        if (this.invokerDeviceId == '舠') {
            if (this.acpl_dev) {
                return this.acpl_handler.GetAttachedDeviceVendorID();
            }
        } else {
            if (this.invokerDeviceId == 41) {
                return this.mantra.GetAttachedDeviceVendorID();
            }

            if (this.invokerDeviceId == 1002) {
                return this.iritech.GetAttachedDeviceVendorID();
            }

            if (this.invokerDeviceId == 71 || this.invokerDeviceId == 82 || this.invokerDeviceId == 36 || this.invokerDeviceId == 38) {
                return this.morpho.GetAttachedDeviceVendorID();
            }

            if (this.invokerDeviceId == 1616) {
                return this.bioenable.GetAttachedDeviceVendorID();
            }

            if (this.invokerDeviceId == 8704) {
                return this.secugen.GetAttachedDeviceVendorID();
            }

            if (this.invokerDeviceId == 1003) {
                return this.biometiques.GetAttachedDeviceVendorID();
            }

            if (this.invokerDeviceId == 8214) {
                return this.elkonTouch.GetAttachedDeviceVendorID();
            }

            if (this.invokerDeviceId == 11) {
                return this.precision_UareUHandler.GetAttachedDeviceVendorID();
            }

            if (this.invokerDeviceId == 8512) {
                return this.mCogentWrapper.GetAttachedDeviceVendorID();
            }
        }

        return -1;
    }

    public String GetDeviceMake() {
        if (this.invokerDeviceId == '舠') {
            if (this.acpl_dev) {
                return this.acpl_handler.GetDeviceMake();
            }
        } else {
            if (this.invokerDeviceId == 41) {
                return this.mantra.GetDeviceMake();
            }

            if (this.invokerDeviceId == 1002) {
                return this.iritech.GetDeviceMake();
            }

            if (this.invokerDeviceId == 71 || this.invokerDeviceId == 82 || this.invokerDeviceId == 36 || this.invokerDeviceId == 38) {
                return this.morpho.GetDeviceMake();
            }

            if (this.invokerDeviceId == 1616) {
                return this.bioenable.GetDeviceMake();
            }

            if (this.invokerDeviceId == 1003) {
                return this.biometiques.GetDeviceMake();
            }

            if (this.invokerDeviceId == 8214) {
                return this.elkonTouch.GetDeviceMake();
            }

            if (this.invokerDeviceId == 11) {
                return this.precision_UareUHandler.GetDeviceMake();
            }

            if (this.invokerDeviceId == 8512) {
                return this.mCogentWrapper.GetDeviceMake();
            }
        }

        return "";
    }

    public String GetDeviceModel() {
        if (this.invokerDeviceId == '舠') {
            if (this.acpl_dev) {
                return this.acpl_handler.GetDeviceModel();
            }
        } else {
            if (this.invokerDeviceId == 41) {
                return this.mantra.GetDeviceModel();
            }

            if (this.invokerDeviceId == 1002) {
                return this.iritech.GetDeviceModel();
            }

            if (this.invokerDeviceId == 71 || this.invokerDeviceId == 82 || this.invokerDeviceId == 36 || this.invokerDeviceId == 38) {
                return this.morpho.GetDeviceModel();
            }

            if (this.invokerDeviceId == 1616) {
                return this.bioenable.GetDeviceModel();
            }

            if (this.invokerDeviceId == 8704) {
                return this.secugen.GetDeviceMake();
            }

            if (this.invokerDeviceId == 1003) {
                return this.biometiques.GetDeviceMake();
            }

            if (this.invokerDeviceId == 8214) {
                return this.elkonTouch.GetDeviceModel();
            }

            if (this.invokerDeviceId == 11) {
                return this.precision_UareUHandler.GetDeviceModel();
            }

            if (this.invokerDeviceId == 8512) {
                return this.mCogentWrapper.GetDeviceModel();
            }
        }

        return "";
    }

    public String GetDeviceSerialNumber() {
        if (this.invokerDeviceId == '舠') {
            if (this.acpl_dev) {
                return this.acpl_handler.GetDeviceSerialNumber();
            }
        } else {
            if (this.invokerDeviceId == 41) {
                return this.mantra.GetDeviceSerialNumber();
            }

            if (this.invokerDeviceId == 1002) {
                return this.iritech.GetDeviceSerialNumber();
            }

            if (this.invokerDeviceId == 71 || this.invokerDeviceId == 82 || this.invokerDeviceId == 36 || this.invokerDeviceId == 38) {
                return this.morpho.GetDeviceSerialNumber();
            }

            if (this.invokerDeviceId == 1616) {
                return this.bioenable.GetDeviceSerialNumber();
            }

            if (this.invokerDeviceId == 1616) {
                return this.bioenable.GetDeviceSerialNumber();
            }

            if (this.invokerDeviceId == 8704) {
                return this.secugen.GetDeviceSerialNumber();
            }

            if (this.invokerDeviceId == 1003) {
                return this.biometiques.GetDeviceSerialNumber();
            }

            if (this.invokerDeviceId == 8214) {
                return this.elkonTouch.GetDeviceSerialNumber();
            }

            if (this.invokerDeviceId == 11) {
                return this.precision_UareUHandler.GetDeviceSerialNumber();
            }

            if (this.invokerDeviceId == 8512) {
                return this.mCogentWrapper.GetDeviceSerialNumber();
            }
        }

        return "";
    }

    public int GetDeviceType() {
        if (this.invokerDeviceId == '舠') {
            if (this.acpl_dev) {
                return this.acpl_handler.GetDeviceType();
            }
        } else {
            if (this.invokerDeviceId == 41) {
                return this.mantra.GetDeviceType();
            }

            if (this.invokerDeviceId == 1002) {
                return this.iritech.GetDeviceType();
            }

            if (this.invokerDeviceId == 71 || this.invokerDeviceId == 82 || this.invokerDeviceId == 36 || this.invokerDeviceId == 38) {
                return this.morpho.GetDeviceType();
            }

            if (this.invokerDeviceId == 1616) {
                return this.bioenable.GetDeviceType();
            }

            if (this.invokerDeviceId == 8704) {
                return this.secugen.GetDeviceType();
            }

            if (this.invokerDeviceId == 1003) {
                return this.biometiques.GetDeviceType();
            }

            if (this.invokerDeviceId == 8214) {
                return this.elkonTouch.GetDeviceType();
            }

            if (this.invokerDeviceId == 11) {
                return this.precision_UareUHandler.GetDeviceType();
            }

            if (this.invokerDeviceId == 8512) {
                return this.mCogentWrapper.GetDeviceType();
            }
        }

        return -1;
    }

    public void SetFingerprintBiometricDataType(String bioDataType) {
        if (this.invokerDeviceId == '舠') {
            if (this.acpl_dev) {
                this.acpl_handler.SetFingerprintBiometricDataType(bioDataType);
            }
        } else if (this.invokerDeviceId == 41) {
            this.mantra.SetFingerprintBiometricDataType(bioDataType);
        } else if (this.invokerDeviceId != 71 && this.invokerDeviceId != 82 && this.invokerDeviceId != 36 && this.invokerDeviceId != 38) {
            if (this.invokerDeviceId == 1616) {
                this.bioenable.SetFingerprintBiometricDataType(bioDataType);
            } else if (this.invokerDeviceId == 8704) {
                this.secugen.SetFingerprintBiometricDataType(bioDataType);
            } else if (this.invokerDeviceId == 8214) {
                this.elkonTouch.SetFingerprintBiometricDataType(bioDataType);
            } else if (this.invokerDeviceId == 11) {
                this.precision_UareUHandler.SetFingerprintBiometricDataType(bioDataType);
            } else if (this.invokerDeviceId == 8512) {
                this.mCogentWrapper.SetFingerprintBiometricDataType(bioDataType);
            }
        } else {
            this.morpho.SetFingerprintBiometricDataType(bioDataType);
        }

    }

    public void SetCaptureTimeout(int TimeOutval) {
        if (this.invokerDeviceId == '舠' && this.acpl_dev) {
            this.acpl_handler.SetCaptureTimeout(TimeOutval);
        }

        if (this.invokerDeviceId == 41) {
            this.mantra.SetCaptureTimeout(TimeOutval);
        } else if (this.invokerDeviceId == 1002) {
            this.iritech.SetCaptureTimeout(TimeOutval);
        } else if (this.invokerDeviceId != 71 && this.invokerDeviceId != 82 && this.invokerDeviceId != 36 && this.invokerDeviceId != 38) {
            if (this.invokerDeviceId == 1616) {
                this.bioenable.SetCaptureTimeout(TimeOutval);
            } else if (this.invokerDeviceId == 8704) {
                this.secugen.SetCaptureTimeout(TimeOutval);
            } else if (this.invokerDeviceId == 1003) {
                this.biometiques.SetCaptureTimeout(TimeOutval);
            } else if (this.invokerDeviceId == 8214) {
                this.elkonTouch.SetCaptureTimeout(TimeOutval);
            } else if (this.invokerDeviceId == 11) {
                this.precision_UareUHandler.SetCaptureTimeout(TimeOutval);
            } else if (this.invokerDeviceId == 8512) {
                this.mCogentWrapper.SetCaptureTimeout(TimeOutval);
            }
        } else {
            this.morpho.SetCaptureTimeout(TimeOutval);
        }

    }

    public void SetThresholdQuality(int thresholdQuality) {
        if (this.invokerDeviceId == '舠') {
            if (this.acpl_dev) {
                this.acpl_handler.SetThresholdQuality(thresholdQuality);
            }
        } else if (this.invokerDeviceId == 41) {
            this.mantra.SetThresholdQuality(thresholdQuality);
        } else if (this.invokerDeviceId == 1002) {
            this.iritech.SetThresholdQuality(thresholdQuality);
        } else if (this.invokerDeviceId != 71 && this.invokerDeviceId != 82 && this.invokerDeviceId != 36 && this.invokerDeviceId != 38) {
            if (this.invokerDeviceId == 1616) {
                this.bioenable.SetThresholdQuality(thresholdQuality);
            } else if (this.invokerDeviceId == 8704) {
                this.secugen.SetThresholdQuality(thresholdQuality);
            } else if (this.invokerDeviceId == 1003) {
                this.biometiques.SetThresholdQuality(thresholdQuality);
            } else if (this.invokerDeviceId == 8214) {
                this.elkonTouch.SetThresholdQuality(thresholdQuality);
            } else if (this.invokerDeviceId == 11) {
                this.precision_UareUHandler.SetThresholdQuality(thresholdQuality);
            } else if (this.invokerDeviceId == 8512) {
                this.mCogentWrapper.SetThresholdQuality(thresholdQuality);
            }
        } else {
            this.morpho.SetThresholdQuality(thresholdQuality);
        }

    }

    public int InitDevice(int deviceID) {
        if (this.invokerDeviceId == '舠') {
            if (this.acpl_dev) {
                return this.acpl_handler.InitDevice(deviceID);
            }
        } else {
            if (this.invokerDeviceId == 41) {
                return this.mantra.InitDevice(deviceID);
            }

            if (this.invokerDeviceId == 1002) {
                return this.iritech.InitDevice(deviceID);
            }

            if (this.invokerDeviceId == 71 || this.invokerDeviceId == 82 || this.invokerDeviceId == 36 || this.invokerDeviceId == 38) {
                return this.morpho.InitDevice(deviceID);
            }

            if (this.invokerDeviceId == 1616) {
                return this.bioenable.InitDevice(deviceID);
            }

            if (this.invokerDeviceId == 8704) {
                return this.secugen.InitDevice(deviceID);
            }

            if (this.invokerDeviceId == 1003) {
                return this.biometiques.InitDevice(deviceID);
            }

            if (this.invokerDeviceId == 8214) {
                return this.elkonTouch.InitDevice(deviceID);
            }

            if (this.invokerDeviceId == 11) {
                return this.precision_UareUHandler.InitDevice(deviceID);
            }

            if (this.invokerDeviceId == 8512) {
                return this.mCogentWrapper.InitDevice(deviceID);
            }
        }

        return -1;
    }

    public void UnInitDevice() {
        if (this.invokerDeviceId == '舠') {
            this.acpl_handler.unInitDevice();
        } else if (this.invokerDeviceId == 41) {
            this.mantra.UnInitDevice();
        } else if (this.invokerDeviceId == 1002) {
            this.iritech.UnInitDevice();
        } else if (this.invokerDeviceId != 71 && this.invokerDeviceId != 82 && this.invokerDeviceId != 36 && this.invokerDeviceId != 38) {
            if (this.invokerDeviceId == 1616) {
                this.bioenable.DisconnectDevice();
            } else if (this.invokerDeviceId == 8704) {
                this.secugen.UnInitDevice();
            } else if (this.invokerDeviceId == 1003) {
                this.biometiques.UnInitDevice();
            } else if (this.invokerDeviceId == 8214) {
                this.elkonTouch.UnInitDevice();
            } else if (this.invokerDeviceId == 11) {
                this.precision_UareUHandler.UnInitDevice();
            } else if (this.invokerDeviceId == 8512) {
                this.mCogentWrapper.UnInitDevice();
            }
        } else {
            this.morpho.UnInit();
        }

    }

    public void BeginCapture() {
        if (this.invokerDeviceId == '舠') {
            if (this.acpl_dev) {
                this.acpl_handler.BeginCapture();
            }
        } else if (this.invokerDeviceId == 41) {
            this.mantra.BeginCapture();
        } else if (this.invokerDeviceId == 1002) {
            this.iritech.BeginCapture();
        } else if (this.invokerDeviceId != 71 && this.invokerDeviceId != 82 && this.invokerDeviceId != 36 && this.invokerDeviceId != 38) {
            if (this.invokerDeviceId == 1616) {
                this.bioenable.BeginCapture();
            } else if (this.invokerDeviceId == 8704) {
                this.secugen.BeginCapture();
            } else if (this.invokerDeviceId == 1003) {
                this.biometiques.BeginCapture();
            } else if (this.invokerDeviceId == 8214) {
                this.elkonTouch.BeginCapture();
            } else if (this.invokerDeviceId == 11) {
                this.precision_UareUHandler.BeginCapture();
            } else if (this.invokerDeviceId == 8512) {
                this.mCogentWrapper.BeginCapture();
            }
        } else {
            this.morpho.BeginCapture();
        }

    }

    public static class BioException extends IOException {
        private static final long serialVersionUID = 1L;

        public BioException() {
        }

        public BioException(String ftStatusMsg) {
        }
    }
}
