package com.nhpm.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.customComponent.Networking.CustomVolley;
import com.customComponent.Networking.VolleyTaskListener;
import com.customComponent.utility.ProjectPrefrence;
import com.nhpm.LocalDataBase.dto.SeccDatabase;
import com.nhpm.Models.response.master.ConfigurationItem;
import com.nhpm.Models.response.master.StateItem;
import com.nhpm.Models.response.verifier.PinRequestItem;
import com.nhpm.Models.response.verifier.UpdatePinResponse;
import com.nhpm.Models.response.verifier.VerifierLoginResponse;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;
import com.nhpm.Utility.AppUtility;

import java.util.ArrayList;

import pl.polidea.view.ZoomView;

public class SetPinActivity extends BaseActivity {
    private ImageView settings;
    private TextView headerTV;
    private Button submitBT;
    private EditText pintET, confirmPinET;
    private Context context;
    private VerifierLoginResponse loginResponse;
    private ImageView backIV;
    private PinRequestItem pinRequestItem;
    private UpdatePinResponse updatePinResponse;
    private LinearLayout mZoomLinearLayout;
    private ZoomView zoomView;
    private String zoomMode = "N";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        checkAppConfig();
        if (zoomMode.equalsIgnoreCase("N")) {
            setContentView(R.layout.activity_set_pin);
            setupScreenWithOutZoom();
        } else {
            setContentView(R.layout.dummy_layout_for_zooming);
            setupScreenWithZoom();
        }

    }

    private void setupScreenWithZoom() {
        mZoomLinearLayout = (LinearLayout) findViewById(R.id.mZoomLinearLayout);
        View v = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.activity_set_pin, null, false);
        v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
        showNotification(v);
        loginResponse = VerifierLoginResponse.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.VERIFIER_CONTENT, context));
        settings = (ImageView) v.findViewById(R.id.settings);
        headerTV = (TextView) v.findViewById(R.id.centertext);
        pintET = (EditText) v.findViewById(R.id.pinET);
        pintET.requestFocus();
        confirmPinET = (EditText) v.findViewById(R.id.confirmPinET);
        submitBT = (Button) v.findViewById(R.id.resetBT);
        headerTV.setText(context.getResources().getString(R.string.setPin));
        settings.setVisibility(View.GONE);
        backIV = (ImageView) v.findViewById(R.id.back);
        openSoftinputKeyBoard();
        zoomView = new ZoomView(this);
        zoomView.addView(v);
        mZoomLinearLayout.addView(zoomView);
        submitBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pin = pintET.getText().toString().trim();
                String confrimPin = confirmPinET.getText().toString().trim();
                if (pin.equalsIgnoreCase("")) {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterPin));
                } else if (pin.length() < 4) {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnter4digitPin));
                } else if (confrimPin.equalsIgnoreCase("")) {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterConfirmPin));
                } else if (!pin.equalsIgnoreCase(confrimPin)) {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.pinDoesNotMAtchWithConfirm));
                } else {
                    pinRequestItem = new PinRequestItem();
                    pinRequestItem.setPin(pin);
                    if (loginResponse.getAadhaarNumber() != null) {
                        pinRequestItem.setAadharNo(loginResponse.getAadhaarNumber());
                    } /*else {
                        pinRequestItem.setUserId(loginResponse.getUserId());
                    }*/
                    pinRequestItem.setUserId(loginResponse.getUserId());
                    requestToUpdatePin();
                    /*loginResponse.getVerifierDetail().setPin(pin);
                    ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.VERIFIER_CONTENT, loginResponse.serialize(), context);
                    Intent theIntent=new Intent(context,BlockDetailActivity.class);
                    startActivity(theIntent);*/
                }
            }
        });
        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  finish();
                rightTransition();*/
            }
        });
    }

    private void setupScreenWithOutZoom() {
        showNotification();
        loginResponse = VerifierLoginResponse.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.VERIFIER_CONTENT, context));
        settings = (ImageView) findViewById(R.id.settings);
        headerTV = (TextView) findViewById(R.id.centertext);
        pintET = (EditText) findViewById(R.id.pinET);
        pintET.requestFocus();
        confirmPinET = (EditText) findViewById(R.id.confirmPinET);
        submitBT = (Button) findViewById(R.id.resetBT);
        headerTV.setText(context.getResources().getString(R.string.setPin));
        settings.setVisibility(View.GONE);
        backIV = (ImageView) findViewById(R.id.back);
        openSoftinputKeyBoard();

        submitBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pin = pintET.getText().toString().trim();
                String confrimPin = confirmPinET.getText().toString().trim();
                if (pin.equalsIgnoreCase("")) {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterPin));
                } else if (pin.length() < 4) {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnter4digitPin));
                } else if (confrimPin.equalsIgnoreCase("")) {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterConfirmPin));
                } else if (!pin.equalsIgnoreCase(confrimPin)) {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.pinDoesNotMAtchWithConfirm));
                } else {
                    pinRequestItem = new PinRequestItem();
                    pinRequestItem.setPin(pin);
                    if (loginResponse.getAadhaarNumber() != null) {
                        pinRequestItem.setAadharNo(loginResponse.getAadhaarNumber());
                    } /*else {
                        pinRequestItem.setUserId(loginResponse.getUserId());
                    }*/
                    pinRequestItem.setUserId(loginResponse.getUserId());
                    requestToUpdatePin();
                    /*loginResponse.getVerifierDetail().setPin(pin);
                    ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.VERIFIER_CONTENT, loginResponse.serialize(), context);
                    Intent theIntent=new Intent(context,BlockDetailActivity.class);
                    startActivity(theIntent);*/
                }
            }
        });
        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  finish();
                rightTransition();*/
            }
        });
    }

    private void openSoftinputKeyBoard() {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        } catch (Exception ex) {

        }
    }

    private void updatedVersionApp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Alert");
        builder.setMessage(getString(R.string.updated_version_msg))
                .setCancelable(false)
                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                        Intent theIntent = new Intent(context, BlockDetailActivity.class);
                        startActivity(theIntent);
                        finish();
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }

                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        logoutVerifier();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void requestToUpdatePin() {
        VolleyTaskListener taskListener = new VolleyTaskListener() {
            @Override
            public void postExecute(String response) {
                updatePinResponse = UpdatePinResponse.create(response);
                if (updatePinResponse.isStatus()) {
                    loginResponse.setPin(updatePinResponse.getPin());
                    ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.VERIFIER_CONTENT, loginResponse.serialize(), context);
                    Intent theIntent = new Intent(context, BlockDetailActivity.class);
                    startActivity(theIntent);
                    finish();
                } else {
                    CustomAlert.alertWithOk(context, updatePinResponse.getErrorMessage());
                }


            }

            @Override
            public void onError(VolleyError error) {
                CustomAlert.alertWithOk(context, context.getResources().getString(R.string.serverNotResponding));

            }
        };

        CustomVolley volley = new CustomVolley(taskListener, context.getResources().getString(R.string.please_wait), AppConstant.UPDATE_PIN, pinRequestItem.serialize(), AppConstant.AUTHORIZATION,AppConstant.AUTHORIZATIONVALUE, context);
        volley.execute();

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
