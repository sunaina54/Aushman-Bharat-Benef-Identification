package com.nhpm;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.customComponent.CustomAlert;
import com.customComponent.utility.ProjectPrefrence;
import com.nhpm.Models.response.master.StateItem;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;
import com.nhpm.Utility.TransDialog;
import com.nhpm.activity.LoginActivity;

public class BaseActivity extends AppCompatActivity {
    private Context context;
    protected TransDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        getSupportActionBar().hide();
        context = this;
        pd = new TransDialog(this, R.drawable.loading);
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
    }

    public void leftTransition() {
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void rightTransition() {
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public void logoutVerifier() {
       /* if (isNetworkAvailable()) {*/
        ProjectPrefrence.removeSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SELECTED_STATE, context);
        ProjectPrefrence.removeSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.DOWNLOADINGSOURCE, context);
        ProjectPrefrence.removeSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.VERIFIER_CONTENT, context);
        ProjectPrefrence.removeSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.MEMBER_DOWNLOADED_COUNT, context);
        ProjectPrefrence.removeSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.HOUSEHOLD_DOWNLOADED_COUNT, context);
        ProjectPrefrence.removeSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.dataDownloaded, context);
        Intent theIntent = new Intent(context, LoginActivity.class);
        theIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        theIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(theIntent);


       /* } else {
            CustomAlert.alertWithOk(context, "Please connect your device to internet to logout.");
        }*/
    }

    public void showHideProgressDialog(boolean isShow) {
        if (isShow) {
            //  _progressDialog.setMessage(message);
            //  _progressDialog.show();
            pd.setTitle("Please wait\nDownloading software");
            pd.show();
        } else {
            // _progressDialog.dismiss();
            pd.dismiss();
        }
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connec = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        // Check for network connections
        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {
            //Toast.makeText(this, " Connected ", Toast.LENGTH_LONG).show();
            return true;
        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {
            // Toast.makeText(this, " Not Connected ", Toast.LENGTH_LONG).show();
            return false;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
    }

}
