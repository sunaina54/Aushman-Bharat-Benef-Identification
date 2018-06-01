package com.nhpm.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.customComponent.CustomAlert;
import com.customComponent.Networking.CustomVolleyGet;
import com.customComponent.Networking.VolleyTaskListener;
import com.customComponent.utility.ProjectPrefrence;
import com.nhpm.LocalDataBase.dto.SeccDatabase;
import com.nhpm.Models.response.master.ConfigurationItem;
import com.nhpm.Models.response.master.StateItem;
import com.nhpm.Models.response.master.response.AppUpdatVersionResponse;
import com.nhpm.Models.response.verifier.VerifierLocationItem;
import com.nhpm.Models.response.verifier.VerifierLoginResponse;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;
import com.nhpm.Utility.AppUtility;
import com.nhpm.Utility.DeviceInfo;

import java.util.ArrayList;

import pl.polidea.view.ZoomView;

public class PinLoginActivity extends BaseActivity {
    private long millisecond24 = 86400000;
    private Button goForVerificationBT;
    private EditText pinET;
    private VerifierLoginResponse response;
    private Context context;
    private TextView headerTV, wrongAttempetCountText;
    private ImageView settigs, backIV;
    private LinearLayout forgotPinLayout, wornPinLinearLayout;
    private boolean passwordEyeFlag;
    private Button showPassBT;
    private Activity activity;
    private LinearLayout mZoomLinearLayout;
    private ZoomView zoomView;
    private String zoomMode = "N";
    private int wrongPinCount = 0;
    private long wrongPinSavedTime;
    private long currentTime;
    private TextView wrongAttempetCountValue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        activity = this;
        checkAppConfig();

        if (zoomMode.equalsIgnoreCase("N")) {
            setContentView(R.layout.activity_pin_login);
            setupScreenWithoutZoom();
        } else {
            setContentView(R.layout.dummy_layout_for_zooming);
            setupScreenWithZoom();
        }
        currentTime = System.currentTimeMillis();
        try {

            wrongPinSavedTime = Long.parseLong(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.WORNG_PIN_ENTERED_TIMESTAMP, context));
        } catch (Exception ex) {
            wrongPinSavedTime = 0;
        }
        if (!(currentTime > (wrongPinSavedTime + millisecond24))) {
            wrongAttempetCountValue.setText("");
            wrongAttempetCountText.setText(context.getResources().getString(R.string.pinLoginDisabled));
            wrongAttempetCountText.setTextColor(context.getResources().getColor(R.color.red));
            wrongAttempetCountValue.setTextColor(context.getResources().getColor(R.color.red));
        }
    }

    private void setupScreenWithZoom() {

        mZoomLinearLayout = (LinearLayout) findViewById(R.id.mZoomLinearLayout);
        View v = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.activity_pin_login, null, false);
        v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
        showNotification(v);
        AppUtility.hideSoftInput(activity);
        AppUtility.showSoftInput(activity);
        // openSoftinputKeyBoard();
        response = VerifierLoginResponse.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.VERIFIER_CONTENT, context));
        goForVerificationBT = (Button) v.findViewById(R.id.goForVerificationBT);
        pinET = (EditText) v.findViewById(R.id.pinET);
        pinET.requestFocus();
        wornPinLinearLayout = (LinearLayout) v.findViewById(R.id.wornPinLinearLayout);
        wrongAttempetCountText = (TextView) v.findViewById(R.id.wrongAttempetCountText);
        wrongAttempetCountValue = (TextView) v.findViewById(R.id.wrongAttempetCountValue);
        showPassBT = (Button) v.findViewById(R.id.showPassBT);
        headerTV = (TextView) v.findViewById(R.id.centertext);
        headerTV.setText("");
        settigs = (ImageView) v.findViewById(R.id.settings);
        settigs.setVisibility(View.GONE);
        backIV = (ImageView) v.findViewById(R.id.back);
        forgotPinLayout = (LinearLayout) v.findViewById(R.id.forgetPinBT);

        zoomView = new ZoomView(this);
        zoomView.addView(v);
        mZoomLinearLayout.addView(zoomView);
        forgotPinLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomAlert.alertWithOk(context, context.getResources().getString(R.string.proceedToForgotPassword));
            }
        });
        showPassBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* if(!passwordEyeFlag){
                    passwordEyeFlag=true;
                }

                AppUtility.toggleShowPassword(passwordEyeFlag,pinET);
                passwordEyeFlag=true;*/

            }
        });
        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent theIntent;
                /*if (response != null && response.getAadhaarNumber() != null) {*/
                theIntent = new Intent(context, LoginActivity.class);
              /*  } else {
                    theIntent = new Intent(context, NonAdharLoginActivity.class);
                }*/

                theIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                theIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(theIntent);
                rightTransition();
            }
        });
        goForVerificationBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                currentTime = System.currentTimeMillis();
                try {

                    wrongPinSavedTime = Long.parseLong(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.WORNG_PIN_ENTERED_TIMESTAMP, context));
                } catch (Exception ex) {
                    wrongPinSavedTime = 0;
                }
                if (currentTime > (wrongPinSavedTime + millisecond24)) {

                    String pinStr = pinET.getText().toString();
                    if (pinStr.equalsIgnoreCase("")) {
                        CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterPin));
                        pinET.setText("");
                    } else if (!pinStr.equalsIgnoreCase(response.getPin())) {
                        if (wrongPinCount >= 2) {
                            wrongAttempetCountText.setTextColor(context.getResources().getColor(R.color.red));
                            wrongAttempetCountValue.setTextColor(context.getResources().getColor(R.color.red));
                        }
                        wrongPinCount++;
                        wrongAttempetCountValue.setText((3 - wrongPinCount)+"");
                        if (wrongPinCount > 2) {
                            long time = System.currentTimeMillis();
                            ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.WORNG_PIN_ENTERED_TIMESTAMP, time + "", context);
                            CustomAlert.alertWithOk(context, context.getResources().getString(R.string.youHaveExceedPinLimit));
                        } else {
                            CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterValidPin));
                            pinET.setText("");
                        }
                    } else if (SeccDatabase.houseHoldCount(context) < 0 && SeccDatabase.seccMemberCount(context) < 0) {
                        CustomAlert.alertWithOk(context, context.getResources().getString(R.string.yourValidationDataNotDownloaded));
                    } else {
                        //checkUpdatedVersion();
                        //  VerifierLocationItem item=findDownloadedItem();
                        // if(item!=null) {
                       /* ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                                AppConstant.SELECTED_BLOCK, item.serialize(), context);*/

                     // open another screen
                        Intent theIntent = new Intent(context, BlockDetailActivity.class);

                       // Intent theIntent = new Intent(context, DownloadedListActvity.class);
                        startActivity(theIntent);
                        leftTransition();
                        finish();
                   /* }else{
                        CustomAlert.alertWithOk(context,"No downloaded enumeration block found");
                    }*/
                    }
                } else {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.pinLoginDisabled));
                }
              /*  Intent theIntent=new Intent(context,SearchActivityWithHouseHold.class);
                startActivity(theIntent);
                finish();*/
            }
        });
    }

    private void setupScreenWithoutZoom() {

        showNotification();
        AppUtility.hideSoftInput(activity);
        AppUtility.showSoftInput(activity);
        // openSoftinputKeyBoard();
        response = VerifierLoginResponse.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.VERIFIER_CONTENT, context));
        goForVerificationBT = (Button) findViewById(R.id.goForVerificationBT);
        pinET = (EditText) findViewById(R.id.pinET);
        pinET.requestFocus();
        wrongAttempetCountText = (TextView) findViewById(R.id.wrongAttempetCountText);
        wrongAttempetCountValue = (TextView) findViewById(R.id.wrongAttempetCountValue);
        showPassBT = (Button) findViewById(R.id.showPassBT);
        headerTV = (TextView) findViewById(R.id.centertext);
        headerTV.setText("");
        wornPinLinearLayout = (LinearLayout) findViewById(R.id.wornPinLinearLayout);
        settigs = (ImageView) findViewById(R.id.settings);
        settigs.setVisibility(View.GONE);
        backIV = (ImageView) findViewById(R.id.back);
        forgotPinLayout = (LinearLayout) findViewById(R.id.forgetPinBT);
        forgotPinLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomAlert.alertWithOk(context, context.getResources().getString(R.string.proceedToForgotPassword));
            }
        });
        showPassBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent theIntent;
                theIntent = new Intent(context, LoginActivity.class);
                theIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                theIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(theIntent);
                rightTransition();
            }
        });
        goForVerificationBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentTime = System.currentTimeMillis();
                try {

                    wrongPinSavedTime = Long.parseLong(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.WORNG_PIN_ENTERED_TIMESTAMP, context));
                } catch (Exception ex) {
                    wrongPinSavedTime = 0;
                }
                if (currentTime > (wrongPinSavedTime + millisecond24)) {


                    String pinStr = pinET.getText().toString();
                    if (pinStr.equalsIgnoreCase("")) {
                        CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterPin));
                        pinET.setText("");
                    } else if (!pinStr.equalsIgnoreCase(response.getPin())) {
                        if (wrongPinCount >= 2) {
                            wrongAttempetCountText.setTextColor(context.getResources().getColor(R.color.red));
                            wrongAttempetCountValue.setTextColor(context.getResources().getColor(R.color.red));
                        }
                        wrongPinCount++;
                        wrongAttempetCountValue.setText((3 - wrongPinCount)+"");
                        if (wrongPinCount > 2) {
                            long time = System.currentTimeMillis();
                            ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.WORNG_PIN_ENTERED_TIMESTAMP, time + "", context);
                        } else {
                            CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterValidPin));
                            pinET.setText("");
                        }
                    } else if (SeccDatabase.houseHoldCount(context) < 0 && SeccDatabase.seccMemberCount(context) < 0) {
                        CustomAlert.alertWithOk(context, context.getResources().getString(R.string.yourValidationDataNotDownloaded));
                    } else {
                        Intent theIntent = new Intent(context, BlockDetailActivity.class);

                        // Intent theIntent = new Intent(context, DownloadedListActvity.class);
                        startActivity(theIntent);
                        leftTransition();
                        finish();
                    }
                } else {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.pinLoginDisabled));

                }
            }
        });
    }

    private VerifierLocationItem findDownloadedItem() {
        VerifierLocationItem selectedItem = null;
        if (response != null) {
            if (response.getLocationList() != null && response.getLocationList().size() > 0) {
                for (VerifierLocationItem item : response.getLocationList()) {
                    if (SeccDatabase.houseHoldCount(context, item.getStateCode(),
                            item.getDistrictCode(), item.getTehsilCode(),
                            item.getVtCode(), item.getWardCode(), item.getBlockCode()) > 0) {
                        selectedItem = item;
                        break;
                    }
                }
            }
        }
        return selectedItem;
    }

    private void openSoftinputKeyBoard() {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        } catch (Exception ex) {

        }
    }

    private void checkUpdatedVersion() {
        VolleyTaskListener taskListener = new VolleyTaskListener() {
            @Override
            public void postExecute(String response) {
                AppUpdatVersionResponse respItem = AppUpdatVersionResponse.create(response);

                if (respItem != null) {
                    AppUtility.showLog(AppConstant.LOG_STATUS, "Pin Login Activity", " Updated App Response : " + respItem.serialize());
                    if (respItem.isStatus()) {
                        int versionCode = Integer.parseInt(respItem.getVersionCode());
                        if (versionCode != DeviceInfo.findApplicationVersionCode(context)) {
                            Intent theInten = new Intent(context, AppUpdateActivity.class);
                            startActivity(theInten);
                            finish();
                        } else {
                            VerifierLocationItem item = findDownloadedItem();
                            if (item != null) {
                                ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                                        AppConstant.SELECTED_BLOCK, item.serialize(), context);
                                Intent theIntent = new Intent(context, SearchActivityWithHouseHold.class);
                                startActivity(theIntent);
                                leftTransition();
                                finish();
                            } else {
                                CustomAlert.alertWithOk(context, context.getResources().getString(R.string.noDownloadedEbFound));
                            }
                        }
                    }
                }
            }

            @Override
            public void onError(VolleyError error) {
            }
        };
        CustomVolleyGet volleyGet = new CustomVolleyGet(taskListener, context.getResources().getString(R.string.plzWaitCheckUpdat), AppConstant.GET_UPDATE_VERSION, context);
        volleyGet.execute();
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
