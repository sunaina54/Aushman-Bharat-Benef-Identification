package com.nhpm.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.customComponent.CustomAlert;
import com.customComponent.utility.ProjectPrefrence;
import com.nhpm.Models.response.master.StateItem;
import com.nhpm.Models.response.verifier.VerifierLoginResponse;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;
import com.nhpm.Utility.AppUtility;
import com.nhpm.Utility.TransDialog;

public class BaseActivity extends AppCompatActivity {
    private CountDownTimer displayTimmer = null;
    private CountDownTimer backgroundTimmer = null;
    private boolean displayTimmerisRunning = false;
    private boolean backgroundTimmerisRunning = false;
    private Context context;
    protected TransDialog pd;
    private String savedSession;
    private int sessionTime;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        getSupportActionBar().hide();
        context = this;
        activity = this;


        try {
            final Handler handler = new Handler();
            final Runnable runnable = new Runnable() {
                @Override
                public void run() {

                    handler.postDelayed(this, 5);
                }

            };
            handler.postDelayed(runnable, 5);
        } catch (Exception ex) {
        }
        savedSession = ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SESSIONTIMEOUT, context);
        if (savedSession != null && !savedSession.equalsIgnoreCase("")) {
            sessionTime = Integer.parseInt(savedSession) * 60 * 1000;
        } else {
            sessionTime = 30 * 60 * 1000;
        }
        displayTimmer = new CountDownTimer(sessionTime, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                displayTimmerisRunning = true;
                AppUtility.showLog(AppConstant.LOG_STATUS, "BASE ACTIVITYACTIVITY123 ONTICK ", "calling " + millisUntilFinished);
            }

            @Override
            public void onFinish() {
                displayTimmerisRunning = false;
                AppUtility.showLog(AppConstant.LOG_STATUS, "BASE ACTIVITYACTIVITY123 FINISHED", "calling");
                //  BaseActivity.this.finish();
                Intent theIntent;
                VerifierLoginResponse verifierDetail = VerifierLoginResponse.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                        AppConstant.VERIFIER_CONTENT, context));
                //if (verifierDetail != null && verifierDetail.getAadhaarNumber() != null) {
                theIntent = new Intent(context, LoginActivity.class);
                /*} else {
                    theIntent = new Intent(context, NonAdharLoginActivity.class);
                }*/
                theIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                theIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(theIntent);
                rightTransition();
            }
        };
        backgroundTimmer = new CountDownTimer(sessionTime, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                backgroundTimmerisRunning = true;
                AppUtility.showLog(AppConstant.LOG_STATUS, "BASE ACTIVITYACTIVITY123 ONTICK ", "calling " + millisUntilFinished);
            }

            @Override
            public void onFinish() {
                backgroundTimmerisRunning = false;
                AppUtility.showLog(AppConstant.LOG_STATUS, "BASE ACTIVITYACTIVITY123 FINISHED", "calling");
                //  BaseActivity.this.finish();
                Intent theIntent;
                VerifierLoginResponse verifierDetail = VerifierLoginResponse.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                        AppConstant.VERIFIER_CONTENT, context));
                // if (verifierDetail != null && verifierDetail.getAadhaarNumber() != null) {
                theIntent = new Intent(context, LoginActivity.class);
                /*} else {
                    theIntent = new Intent(context, NonAdharLoginActivity.class);
                }*/
                theIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                theIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(theIntent);
                rightTransition();
            }
        };

        pd = new TransDialog(this, R.drawable.loading);
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
    }

    @Override
    protected void onPause() {
        AppUtility.showLog(AppConstant.LOG_STATUS, "BASE ACTIVITYACTIVITY123 onPause ", "calling");
        super.onPause();
    }

    @Override
    protected void onStart() {
        AppUtility.showLog(AppConstant.LOG_STATUS, "BASE ACTIVITYACTIVITY123 onStart ", "calling");
        super.onStart();
    }

    @Override
    protected void onStop() {
        if (displayTimmer != null) {
            if (displayTimmerisRunning) {
                displayTimmer.cancel();
            }
        }

        if (backgroundTimmer != null) {
            if (backgroundTimmerisRunning) {
                backgroundTimmer.cancel();
            }
        }
        AppUtility.showLog(AppConstant.LOG_STATUS, "BASE ACTIVITYACTIVITY123 onDestroy ", "calling");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if (displayTimmer != null) {
            if (displayTimmerisRunning) {
                displayTimmer.cancel();
            }
        }

        if (backgroundTimmer != null) {
            if (backgroundTimmerisRunning) {
                backgroundTimmer.cancel();
            }
        }
        AppUtility.showLog(AppConstant.LOG_STATUS, "BASE ACTIVITYACTIVITY123 onDestroy ", "calling");
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        AppUtility.showLog(AppConstant.LOG_STATUS, "BASE ACTIVITYACTIVITY123 onStart ", "calling");
        super.onResume();
    }

    public void leftTransition() {
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void rightTransition() {
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }


    public void logoutVerifier() {
        if (isNetworkAvailable()) {
            Intent theIntent = new Intent(context, LoginActivity.class);
            theIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            theIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(theIntent);
            ProjectPrefrence.removeSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.VERIFIER_CONTENT, context);
        } else {
            CustomAlert.alertWithOk(context, context.getResources().getString(R.string.pleaseConnectToInternet));
        }
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connec = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {
            return true;
        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {
            return false;
        }
        return false;
    }

    public void showHideProgressDialog(boolean isShow) {
        if (isShow) {
            pd.setTitle(context.getResources().getString(R.string.please_wait));
            pd.show();
        } else {
            pd.dismiss();
        }
    }


    @Override
    public void onBackPressed() {
    }


}
