package com.nhpm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.Log;


/**
 * Created by psqit on 6/13/2016.
 */
public class WifiReceiver extends BroadcastReceiver {

    static Context mContext;


    @Override
    public void onReceive(Context context, Intent intent) {
        mContext=context;
        final String action = intent.getAction();
        Log.d("ACTION",action);


        if(intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {NetworkInfo networkInfo =
                    intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            if(networkInfo.isConnected()) {
                // Wifi is connected
                Log.d("Identify", "Wifi is connected: " + String.valueOf(networkInfo));



                Intent serviceintent = new Intent(context,EditHitfromLocal.class);
                context.startService(serviceintent);
            }
        } else if(intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            NetworkInfo networkInfo =
                    intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
            if(networkInfo.getType() == ConnectivityManager.TYPE_WIFI &&
                    ! networkInfo.isConnected()) {
                // Wifi is disconnected
                //globals.setStatus(false);

                Log.d("Identify", "Wifi is disconnected: " + String.valueOf(networkInfo));
            }
        }
    }
    }

