package com.nhpm.Utility;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.Builder;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi.AttestationResult;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.SecureRandom;
import java.util.Random;

public final class CheckDeviceRooted implements ConnectionCallbacks {
    private static String TAG = CheckDeviceRooted.class.getSimpleName();
    static boolean isRooted;
    private static final Random mRandom = new SecureRandom();

    private static boolean checkRootMethod1() {
        String str = Build.TAGS;
        return str != null && str.contains("test-keys");
    }

    private static boolean checkRootMethod2() {
        for (String file : new String[]{"/system/app/Superuser.apk", "/sbin/su", "/system/bin/su", "/system/xbin/su", "/data/local/xbin/su", "/data/local/bin/su", "/system/sd/xbin/su", "/system/bin/failsafe/su", "/data/local/su"}) {
            if (new File(file).exists()) {
                return true;
            }
        }
        return false;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static boolean checkRootMethod3() {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(new String[]{"/system/xbin/which", "su"});
            if (new BufferedReader(new InputStreamReader(process.getInputStream())).readLine() == null) {
                if (process != null) {
                    process.destroy();
                }
                return false;
            } else if (process == null) {
                return true;
            } else {
                process.destroy();
                return true;
            }
        } catch (Throwable th) {
            if (process != null) {
                process.destroy();
                return false;
            }
        }
        return false;
    }

    private static boolean checkRootMethod4() {
        return new File("/system/app/Superuser.apk").exists();
    }

    private static boolean checkRootMethod5(Context context) {
        byte[] requestNonce = getRequestNonce("Safety Net Sample: " + System.currentTimeMillis());
        isRooted = false;
        GoogleApiClient build = new Builder(context).addApi(SafetyNet.API).build();
        build.connect();
        SafetyNet.SafetyNetApi.attest(build, requestNonce).setResultCallback(new ResultCallback<AttestationResult>() {
            public void onResult(AttestationResult attestationResult) {
                if (attestationResult.getStatus().isSuccess()) {
                    Log.i("RootUtil", "jwsResult::" + attestationResult.getJwsResult());
                    CheckDeviceRooted.isRooted = true;
                    Log.i("RootUtil", "INSIDE IF");
                    return;
                }
                CheckDeviceRooted.isRooted = false;
                Log.i("RootUtil", "INSIDE ELSE");
            }
        });
        return isRooted;
    }

    private static byte[] getRequestNonce(String str) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bArr = new byte[24];
        mRandom.nextBytes(bArr);
        try {
            byteArrayOutputStream.write(bArr);
            byteArrayOutputStream.write(str.getBytes());
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            return null;
        }
    }

    public static boolean isDeviceRooted() {
        return checkRootMethod2() || checkRootMethod3() || checkRootMethod4();
    }

    public static boolean isDeviceRootedSafetyNet(Context context) {
        return checkRootMethod5(context);
    }

    public void onConnected(Bundle bundle) {
        Log.d(TAG, "onConnected: ");
        isRooted = true;
    }

    public void onConnectionSuspended(int i) {
        isRooted = false;
    }
}
