package com.nhpm.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.customComponent.CustomAlert;
import com.customComponent.utility.ProjectPrefrence;
import com.nhpm.LocalDataBase.dto.SeccDatabase;
import com.nhpm.Models.response.master.ConfigurationItem;
import com.nhpm.Models.response.master.StateItem;
import com.nhpm.Models.response.seccMembers.HouseHoldItem;
import com.nhpm.Models.response.verifier.VerifierLocationItem;
import com.nhpm.Models.response.verifier.VerifierLoginResponse;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;
import com.nhpm.Utility.AppUtility;
import com.nhpm.backgroundService.SyncService;
import com.nhpm.fragments.SyncHouseHoldFragment;

import java.util.ArrayList;

import pl.polidea.view.ZoomView;

public class SyncHouseholdActivity extends BaseActivity implements ComponentCallbacks2 {
    private Context context;
    private final String TAG = "SyncHouseholdActivity";
    private ImageView backIV;
    private SyncHouseHoldFragment fragment;
    private FragmentTransaction fragTransect;
    private FragmentManager fragMgr;
    private LinearLayout mZoomLinearLayout;
    private ZoomView zoomView;
    private ProgressDialog mProgressDialog;
    private RelativeLayout backLayout;
    private ArrayList<HouseHoldItem> totalHouseHold;
    private VerifierLocationItem downloadedLocation;
    private MyCountDownTimer myCountDownTimer;
    private boolean pinLockIsShown = false;
    private String zoomMode = "N";

    private int wrongPinCount = 0;
    private long wrongPinSavedTime;
    private long currentTime;
    private TextView wrongAttempetCountValue, wrongAttempetCountText;
    private long millisecond24 = 86400000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        checkAppConfig();
        if (zoomMode.equalsIgnoreCase("N")) {
            setContentView(R.layout.activity_sync_household);
            setupScreenWithOutZoom();
        } else {
            setContentView(R.layout.dummy_layout_for_zooming);
            setupScreenWithZoom();
        }
    }

    private void setupScreenWithOutZoom() {
        showNotification();
        backIV = (ImageView) findViewById(R.id.back);
        backLayout = (RelativeLayout) findViewById(R.id.backLayout);
        downloadedLocation = VerifierLocationItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SELECTED_BLOCK, context));
        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AppUtility.navgateFromEb) {
                    finish();
                } else {
                    Intent theIntent = new Intent(context, SeccMemberListActivity.class);
                    theIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(theIntent);
                    finish();
                }
            }
        });
        searchMenuAppcongigBoth();
        backLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backIV.performClick();
            }
        });
        openSyncHouseholdFragment();

    }

    private void setupScreenWithZoom() {
        mZoomLinearLayout = (LinearLayout) findViewById(R.id.mZoomLinearLayout);
        View v = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.activity_sync_household, null, false);
        v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        showNotification(v);
        zoomView = new ZoomView(this);
        zoomView.addView(v);
        mZoomLinearLayout.addView(zoomView);
        backIV = (ImageView) v.findViewById(R.id.back);
        backLayout = (RelativeLayout) v.findViewById(R.id.backLayout);
        downloadedLocation = VerifierLocationItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF
                , AppConstant.SELECTED_BLOCK, context));
        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // finish();


                if (AppUtility.navgateFromEb) {
                    finish();
                } else {
                    Intent theIntent = new Intent(context, SeccMemberListActivity.class);
                    theIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(theIntent);
                    finish();
                }

            }
        });
        searchMenuAppcongigBoth(v);
        backLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backIV.performClick();
            }
        });
        openSyncHouseholdFragment();

    }

    private void searchMenuAppcongigBoth(View v) {

        final ImageView settings = (ImageView) v.findViewById(R.id.settings);
        //  settings.setVisibility(View.GONE);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(context, settings);
                popup.getMenuInflater()
                        .inflate(R.menu.menu_nav_dashboard, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {

                            case R.id.dashboard:
                                //   String searchText = searchFamilyMemberET.getText().toString();
                                Intent theIntent = new Intent(context, SearchActivityWithHouseHold.class);
                                theIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                theIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(theIntent);
                                leftTransition();
                                break;

                        }
                        return true;
                    }
                });
                popup.show();
            }
        });

    }

    private void searchMenuAppcongigBoth() {

        final ImageView settings = (ImageView) findViewById(R.id.settings);
        //  settings.setVisibility(View.GONE);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(context, settings);
                popup.getMenuInflater()
                        .inflate(R.menu.menu_nav_dashboard, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {

                            case R.id.dashboard:
                                //   String searchText = searchFamilyMemberET.getText().toString();
                                Intent theIntent = new Intent(context, SearchActivityWithHouseHold.class);
                                theIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                theIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(theIntent);
                                leftTransition();
                                break;

                        }
                        return true;
                    }
                });
                popup.show();
            }
        });

    }

    public void openSyncHouseholdFragment() {
        //  myCountDownTimer.cancel();
        fragMgr = getSupportFragmentManager();
        fragTransect = fragMgr.beginTransaction();
        if (fragment != null) {
            fragTransect.detach(fragment);
            fragment = null;
        }
        fragment = new SyncHouseHoldFragment();
        fragTransect.replace(R.id.syncFragmentContainer, fragment);
        fragTransect.commitAllowingStateLoss();
    }

    public BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        public static final String PROCESS_RESPONSE = "responseSucces";

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                String response = intent.getStringExtra(SyncService.RESPONSE_SUCCESS);
                if (response != null && response.equalsIgnoreCase("x")) {
                    AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Response : " + response);
                    openSyncHouseholdFragment();

                } else if (response != null && response.equalsIgnoreCase(SyncService.SYNC_COMPLETE)) {
                    AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Response : " + response);
                    findAllHousehold();
                    openSyncHouseholdFragment();
                    unRegisterBroadCastReciver();
                    dismissDialog();
                    myCountDownTimer.cancel();
                    if (findErrorHousehold() != null && findErrorHousehold().size() > 0) {
                        //   AppUtility.alertWithOk(context, "Syncing done" + "\n" + "Total syncing error found : " + findErrorHousehold().size());
                        alertWithError(context, context.getResources().getString(R.string.totalSyncErrorCount) + findErrorHousehold().size());
                    } else {
                        //  xc
                        alertWithNoError(context, context.getResources().getString(R.string.syncComplete));
                    }
                } else if (response != null && response.equalsIgnoreCase(AppConstant.cancelSyncMsg)) {
                    AppUtility.alertWithOk(context, context.getString(R.string.sync_cancel));
                    openSyncHouseholdFragment();
                    unRegisterBroadCastReciver();
                    //    dismissDialog();


                }
            }
        }
    };

    public void showProgressDialog() {
        myCountDownTimer = new MyCountDownTimer(30000 * 1000 + 1000, 1000);

        myCountDownTimer.start();
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setIndeterminateDrawable(context.getResources().getDrawable(R.mipmap.nhps_logo));
        mProgressDialog.setMessage(context.getResources().getString(R.string.please_wait));
        mProgressDialog.show();

    }

    public class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {

            int progress = (int) (((30000 * 1000 + 1000) - millisUntilFinished) / 100);
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                int seconds = (int) (((300 * 1000 + 1000) - millisUntilFinished) / 1000);
                int minutes = seconds / 60;
                seconds = seconds % 60;

           /*     mProgressDialog.setMessage(context.getResources().getString(R.string.please_wait) + "\n" + (String.format("%02d", minutes)
                        + ":" + String.format("%02d", seconds)));
*/
            }
        }

        @Override
        public void onFinish() {
            myCountDownTimer.cancel();
        }

    }

    public void dismissDialog() {
        if (mProgressDialog != null) {
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
                //   myCountDownTimer.cancel();
            }
        }
    }

    private void unRegisterBroadCastReciver() {
        try {
            unregisterReceiver(broadcastReceiver);
        } catch (Exception ex) {

        }
    }

    public void findAllHousehold() {
        totalHouseHold = SeccDatabase.getHouseHoldList(downloadedLocation.getStateCode()
                , downloadedLocation.getDistrictCode()
                , downloadedLocation.getTehsilCode(), downloadedLocation.getVtCode(),
                downloadedLocation.getWardCode(), downloadedLocation.getBlockCode(), context);

    }

    private ArrayList<HouseHoldItem> findReadyToSyncHousehold() {
        ArrayList<HouseHoldItem> list = new ArrayList<>();
        for (HouseHoldItem item : totalHouseHold) {
            AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Ready To Sync household : " + item.getLockSave() + "  Name :" + item.getName());
            if (item.getError_code() != null) {

            } else {
                if (item.getSyncedStatus() != null && item.getSyncedStatus().equalsIgnoreCase(AppConstant.SYNCED_STATUS)) {

                } else {
                    if (item.getLockSave() != null && item.getLockSave().equalsIgnoreCase(AppConstant.LOCKED + "")) {
                        AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Ready To Sync household : " + item.getName());
                        list.add(item);

                    }
                }
            }
        }
        return list;
    }

    private ArrayList<HouseHoldItem> findSyncedHousehold() {
        ArrayList<HouseHoldItem> list = new ArrayList<>();
        for (HouseHoldItem item : totalHouseHold) {
            if (item.getSyncedStatus() != null && item.getSyncedStatus().trim().equalsIgnoreCase(AppConstant.SYNCED_STATUS)) {
                list.add(item);
            }
        }
        return list;
    }

    private ArrayList<HouseHoldItem> findErrorHousehold() {
        ArrayList<HouseHoldItem> list = new ArrayList<>();
        for (HouseHoldItem item : totalHouseHold) {
            if (item.getError_code() != null) {
                list.add(item);
            }
        }
        return list;
    }

    public void alertWithNoError(Context mContext, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(context.getResources().getString(R.string.Alert));
        builder.setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(context.getResources().getString(R.string.OK), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                        ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.DASHBOARD_TAB_STATUS, 1 + "", context);
                        ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                                AppConstant.HOUSEHOLD_TAB_STATUS, 4 + "", context);
                        Intent theIntent = new Intent(context, SearchActivityWithHouseHold.class);
                        theIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(theIntent);
                        finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void alertWithError(Context mContext, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(context.getResources().getString(R.string.Alert));
        builder.setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(context.getResources().getString(R.string.OK), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,"","4",context);
                        AppUtility.redirection = 10;
                        Intent theIntent = new Intent(context, SyncHouseholdActivity.class);
                        theIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(theIntent);
                        finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void showNotification(View v) {

        LinearLayout notificationLayout = (LinearLayout) v.findViewById(R.id.notificationLayout);
        WebView notificationWebview = (WebView) v.findViewById(R.id.notificationWebview);
        String prePairedMessage = AppUtility.getNotificationData(context);
        if (prePairedMessage != null) {
            notificationLayout.setVisibility(View.VISIBLE);
            notificationWebview.loadData(prePairedMessage, "text/html", "utf-8"); // Set focus to textview
        }
    }

    public void showNotification() {

        LinearLayout notificationLayout = (LinearLayout) findViewById(R.id.notificationLayout);
        WebView notificationWebview = (WebView) findViewById(R.id.notificationWebview);
        String prePairedMessage = AppUtility.getNotificationData(context);
        if (prePairedMessage != null) {
            notificationLayout.setVisibility(View.VISIBLE);
            notificationWebview.loadData(prePairedMessage, "text/html", "utf-8"); // Set focus to textview
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (AppUtility.isAppIsInBackground(context)) {
            if (!pinLockIsShown) {
                askPinToLock();
            }
        }
    }

    private void askPinToLock() {
        pinLockIsShown = true;
        final AlertDialog askForPinDailog = new AlertDialog.Builder(context).create();
        LayoutInflater factory = LayoutInflater.from(context);
        View alertView = factory.inflate(R.layout.ask_pin_layout, null);
        askForPinDailog.setView(alertView);
        askForPinDailog.setCancelable(false);
        askForPinDailog.show();
        // Log.d(TAG,"delete status :"+deleteStatus);
        // dialog.setContentView(R.layout.opt_auth_layout);
        //    final TextView otpAuthMsg=(TextView)alertView.findViewById(R.id.otpAuthMsg);
        final VerifierLoginResponse verifierDetail = VerifierLoginResponse.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.VERIFIER_CONTENT, context));
        final EditText pinET = (EditText) alertView.findViewById(R.id.deletPinET);
        final TextView errorTV = (TextView) alertView.findViewById(R.id.invalidOtpTV);
        wrongAttempetCountText = (TextView) alertView.findViewById(R.id.wrongAttempetCountText);
        wrongAttempetCountValue = (TextView) alertView.findViewById(R.id.wrongAttempetCountValue);


        pinET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //   errorTV.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        Button proceedBT = (Button) alertView.findViewById(R.id.proceedBT);
        Button cancelBT = (Button) alertView.findViewById(R.id.cancelBT);
        proceedBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                currentTime = System.currentTimeMillis();
                try {

                    wrongPinSavedTime = Long.parseLong(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.WORNG_PIN_ENTERED_TIMESTAMP, context));
                } catch (Exception ex) {
                    wrongPinSavedTime = 0;
                }
                if (currentTime > (wrongPinSavedTime + millisecond24)) {


                    //  AppUtility.softKeyBoard(activity, 0);
                    String pin = pinET.getText().toString();
                    if (pin.toString().equalsIgnoreCase(verifierDetail.getPin())) {
                        askForPinDailog.dismiss();
                        pinLockIsShown = false;
                    } else if (pin.equalsIgnoreCase("")) {
                        // CustomAlert.alertWithOk(context,"Please enter valid pin");
                        errorTV.setVisibility(View.VISIBLE);
                        errorTV.setText("Enter pin");
                        pinET.setText("");
                        //  pinET.setHint("");
                    } else if (!pin.toString().equalsIgnoreCase(verifierDetail.getPin())) {

                        if (wrongPinCount >= 2) {
                            errorTV.setTextColor(context.getResources().getColor(R.color.red));
                            wrongAttempetCountValue.setTextColor(context.getResources().getColor(R.color.red));
                            wrongAttempetCountText.setTextColor(context.getResources().getColor(R.color.red));
                        }
                        wrongPinCount++;
                        wrongAttempetCountValue.setText((3 - wrongPinCount) + "");
                        if (wrongPinCount > 2) {
                            long time = System.currentTimeMillis();
                            ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.WORNG_PIN_ENTERED_TIMESTAMP, time + "", context);
                            CustomAlert.alertWithOk(context, context.getResources().getString(R.string.youHaveExceedPinLimit));
                        } else {
                            errorTV.setVisibility(View.VISIBLE);
                            errorTV.setText("Enter correct pin");
                            pinET.setText("");
//                        CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterValidPin));
//                        pinET.setText("");
                        }
//                    errorTV.setVisibility(View.VISIBLE);
//                    errorTV.setText("Enter correct pin");
//                    pinET.setText("");
//                    // pinET.setHint("Enter 4-di");
                    }
                } else {

                    //alert  when pin login is diabled for 24 hrs
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.pinLoginDisabled));
                    errorTV.setText("Pin login disabled for 24 hrs.");
                    pinET.setText("");
                    return;
                }
            }
        });
        cancelBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pinLockIsShown = false;
                Intent intent_login = new Intent(context, LoginActivity.class);
                intent_login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent_login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent_login);
                finish();
            }
        });
    }

    @Override
    public void onConfigurationChanged(final Configuration newConfig) {

    }

    @Override
    public void onLowMemory() {

    }

    @Override
    public void onTrimMemory(final int level) {
        if (level == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {
            if (!pinLockIsShown) {
                askPinToLock();
            }
        }
    }

    private String checkAppConfig() {
        StateItem selectedStateItem = StateItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SELECTED_STATE, context));
        if (selectedStateItem != null && selectedStateItem.getStateCode() != null) {
            ArrayList<ConfigurationItem> configList = SeccDatabase.findConfiguration(selectedStateItem.getStateCode(), context);
            if (configList != null) {
                for (ConfigurationItem item1 : configList) {
                    if (item1.getConfigId().equalsIgnoreCase(AppConstant.APPLICATION_ZOOM)) {
                        zoomMode = item1.getStatus();
                    }

                }
            }
        }
        return null;
    }
}

