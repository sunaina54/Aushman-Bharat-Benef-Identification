package com.nhpm.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.customComponent.CustomAlert;
import com.customComponent.CustomAsyncTask;
import com.customComponent.Networking.CustomVolley;
import com.customComponent.Networking.VolleyTaskListener;
import com.customComponent.TaskListener;
import com.customComponent.utility.CustomHttp;
import com.customComponent.utility.ProjectPrefrence;
import com.nhpm.BaseActivity;
import com.nhpm.Models.request.PinRequestItem;
import com.nhpm.Models.response.verifier.UpdatePinResponse;
import com.nhpm.Models.response.verifier.VerifierLoginResponse;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;
import com.nhpm.Utility.AppUtility;

import java.util.HashMap;

import pl.polidea.view.ZoomView;

public class ChangePinActivity extends BaseActivity {
    private ImageView settings;
    private TextView headerTV;
    private Button resetBT;
    private VerifierLoginResponse verifierLoginResp;
    private Context context;
    private UpdatePinResponse pinResp;
    private EditText pinET, confirmPinET;
    private PinRequestItem request;
    private String TAG = "Change Pin Activity";
    private ImageView backIV;
    private LinearLayout mZoomLinearLayout;
    private ZoomView zoomView;
    private CustomAsyncTask mobileOtpAsyncTask;
    private ChangePinActivity changePinActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dummy_layout_for_zooming);
        setupScreen();
    }

    private void setupScreen() {
        context = this;
        changePinActivity = this;
        mZoomLinearLayout = (LinearLayout) findViewById(R.id.mZoomLinearLayout);
        View v = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.activity_change_pin, null, false);
        v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
        showNotification(v);
        AppUtility.softKeyBoard(changePinActivity, 0);
        verifierLoginResp = VerifierLoginResponse.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.VERIFIER_CONTENT, context));
        headerTV = (TextView) v.findViewById(R.id.centertext);
        settings = (ImageView) v.findViewById(R.id.settings);
        pinET = (EditText) v.findViewById(R.id.newPinNumberET);
        pinET.requestFocus();
        pinET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() > 0) {

                    if (pinET.getText().toString().length() == 4) {
                        AppUtility.softKeyBoard(changePinActivity, 0);

                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        confirmPinET = (EditText) v.findViewById(R.id.confirmNumberET);
        confirmPinET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() > 0) {

                    if (confirmPinET.getText().toString().length() == 4) {
                        AppUtility.softKeyBoard(changePinActivity, 0);

                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        backIV = (ImageView) v.findViewById(R.id.back);
        settings.setVisibility(View.GONE);
        headerTV.setText(context.getResources().getString(R.string.resetPin));
        resetBT = (Button) v.findViewById(R.id.resetPinBT);
        zoomView = new ZoomView(this);
        zoomView.addView(v);
        mZoomLinearLayout.addView(zoomView);
        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                rightTransition();
            }
        });
        resetBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pinEtr = pinET.getText().toString();
                String confirmPinStr = confirmPinET.getText().toString();
                if (pinEtr.equalsIgnoreCase("")) {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterPin));
                } else if (confirmPinStr.equalsIgnoreCase("")) {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterConfirmPin));
                } else if (pinEtr.length() < 4) {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.enterPin));

                } else if (!pinEtr.equalsIgnoreCase(confirmPinStr)) {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.pinDoesNotMAtchWithConfirm));
                } else {
                    request = new PinRequestItem();
                    request.setPin(pinEtr);
                    request.setAadharNo(verifierLoginResp.getAadhaarNumber());
                    //request.setUserId(verifierLoginResp.getUserId());
                    if (isNetworkAvailable()) {
                        resetPinRequest();
                    } else {
                        CustomAlert.alertWithOk(context, getResources().getString(R.string.internet_connection_msg));
                    }

                }


            }
        });
    }

    private void resetPinRequest() {
        TaskListener taskListener = new TaskListener() {
            @Override
            public void execute() {

                try {
                    HashMap<String, String> response = CustomHttp.httpPostWithTokken(AppConstant.UPDATE_PIN, request.serialize(),AppConstant.AUTHORIZATION,verifierLoginResp.getAuthToken());
                    if (response != null) {
                        pinResp = UpdatePinResponse.create(response.get(AppConstant.RESPONSE_BODY));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void updateUI() {
                if (pinResp != null) {
                    if (pinResp.isStatus()) {
                        verifierLoginResp.setPin(pinResp.getPin());
                        ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.VERIFIER_CONTENT, verifierLoginResp.serialize(), context);
                        Log.d(TAG, "Updated Pin : " + verifierLoginResp.getPin());
                        finish();
                        rightTransition();
                    }else if(pinResp!=null &&
                            pinResp.getErrorCode().equalsIgnoreCase(AppConstant.SESSION_EXPIRED)
                            ||   pinResp.getErrorCode().equalsIgnoreCase(AppConstant.INVALID_TOKEN) ){
                        Intent intent = new Intent(context,LoginActivity.class);
                        CustomAlert.alertWithOkLogout(context,pinResp.getErrorMessage(),intent);

                    }else {
                        CustomAlert.alertWithOk(context,pinResp.getErrorMessage());
                    }
                } else {
                    CustomAlert.alertWithOk(context, "Internal server error");
                }

            }

       /* VolleyTaskListener taskListener=new VolleyTaskListener() {
            @Override
            public void postExecute(String response) {
                UpdatePinResponse pinResp=UpdatePinResponse.create(response);
                if(pinResp.isStatus()){
                    verifierLoginResp.setPin(pinResp.getPin());
                    ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.VERIFIER_CONTENT, verifierLoginResp.serialize(), context);
                    Log.d(TAG, "Updated Pin : " + verifierLoginResp.getPin());
                    finish();
                    rightTransition();
                }else{
                    CustomAlert.alertWithOk(context,pinResp.getErrorMessage());
                }

            }

            @Override
            public void onError(VolleyError error) {
                CustomAlert.alertWithOk(context,error.getMessage());
            }
        };
        CustomVolley volley=new CustomVolley(taskListener,context.getResources().getString(R.string.please_wait),AppConstant.UPDATE_PIN,request.serialize(),AppConstant.AUTHORIZATION,verifierLoginResp.getAuthToken(),context);
        volley.execute();*/
        };

        if (mobileOtpAsyncTask != null) {
            mobileOtpAsyncTask.cancel(true);
            mobileOtpAsyncTask = null;
        }

        mobileOtpAsyncTask = new CustomAsyncTask(taskListener, context.getResources().getString(R.string.please_wait), context);
        mobileOtpAsyncTask.execute();
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



}
