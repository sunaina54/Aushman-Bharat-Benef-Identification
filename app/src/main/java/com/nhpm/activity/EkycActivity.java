package com.nhpm.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.customComponent.CustomAlert;
import com.customComponent.utility.ProjectPrefrence;
import com.nhpm.LocalDataBase.dto.SeccDatabase;
import com.nhpm.Models.SerachOptionItem;
import com.nhpm.Models.response.master.ConfigurationItem;
import com.nhpm.Models.response.master.StateItem;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;
import com.nhpm.fragments.AadhaarFingerPrintViaRDSevices;
import com.nhpm.fragments.AadhaarIrisViaRDServices;
import com.nhpm.fragments.OTPFragment;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Dell3 on 03-05-2018.
 */

public class EkycActivity extends BaseActivity {
    private Context context;
    private String zoomMode = "N";
    private String benefidentificationMode = "", ekycMode = "", demoMode = "", aadharAuthModeModel = "";
    private FragmentManager fragmentManager;
    private Fragment fragment;
    private FragmentTransaction fragmentTransection;
    private RadioGroup aadharAuthRG;
    private RadioButton irisRadioButton, biometricRadioButton, otpRadioButton, QrCodeRadioButton;
    private RelativeLayout backLayout, menuLayout;
    private ImageView back;
    private TextView headerTV;
    private String aadhaarNo,screenName;
    public SerachOptionItem serachItem;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_ekyc_layout);
        setupScreen();
    }

    private void setupScreen() {
        serachItem = SerachOptionItem.create(ProjectPrefrence.getSharedPrefrenceData
                (AppConstant.PROJECT_NAME, AppConstant.SEARCH_OPTION, context));
        aadhaarNo = serachItem.getAadhaarNo();//getIntent().getStringExtra("aadhaarNo");

        screenName = getIntent().getStringExtra("screen");
        if (screenName != null && screenName.equalsIgnoreCase("PersonalDetailsFragment")) {
            aadhaarNo = getIntent().getStringExtra("aadharNo");
        }

        headerTV = (TextView) findViewById(R.id.centertext);
        headerTV.setText("Beneficiary Data(With Aadhaar)");
        irisRadioButton = (RadioButton) findViewById(R.id.irisRadioButton);
        biometricRadioButton = (RadioButton) findViewById(R.id.biometricRadioButton);
        otpRadioButton = (RadioButton) findViewById(R.id.otpRadioButton);
        backLayout = (RelativeLayout) findViewById(R.id.backLayout);
        backLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backLayout.performClick();
            }
        });
        checkAppConfig();
        if (isNetworkAvailable()) {
            openOTPFragment();
        } else {
            CustomAlert.alertWithOk(context, getResources().getString(R.string.internet_connection_msg));
        }
        aadharAuthRG = (RadioGroup) findViewById(R.id.aadharAuthRG);
        aadharAuthRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (checkedId == irisRadioButton.getId()) {
                    if (isNetworkAvailable()) {
                        openIrisFragment();
                    } else {
                        CustomAlert.alertWithOk(context, getResources().getString(R.string.internet_connection_msg));
                    }
                } else if (checkedId == biometricRadioButton.getId()) {
                    if (isNetworkAvailable()) {
                        openFingerPrintFragment();
                    } else {
                        CustomAlert.alertWithOk(context, getResources().getString(R.string.internet_connection_msg));

                    }
                } else if (checkedId == otpRadioButton.getId()) {
                    if (isNetworkAvailable()) {
                        openOTPFragment();
                    } else {
                        CustomAlert.alertWithOk(context, getResources().getString(R.string.internet_connection_msg));
                    }
                }
            }
        });

    }

    private void openIrisFragment() {
        irisRadioButton.setChecked(true);
        fragmentManager = getSupportFragmentManager();
        fragment = new AadhaarIrisViaRDServices();
        fragmentTransection = fragmentManager.beginTransaction();
        fragmentTransection = fragmentTransection.replace(R.id.frameContainer, fragment);
        fragmentTransection.commitAllowingStateLoss();
        ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.AUTHTYPESELECTED, AppConstant.IRISH_AUTH, context);
        ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.AadharNumber, aadhaarNo, context);

    }

    private void openFingerPrintFragment() {
        biometricRadioButton.setChecked(true);
        fragmentManager = getSupportFragmentManager();
        fragment = new AadhaarFingerPrintViaRDSevices();
        fragmentTransection = fragmentManager.beginTransaction();
        fragmentTransection = fragmentTransection.replace(R.id.frameContainer, fragment);
        fragmentTransection.commitAllowingStateLoss();
        ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.AUTHTYPESELECTED, AppConstant.FINGER, context);
        ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.AadharNumber, aadhaarNo, context);
    }

    private void openOTPFragment() {
        otpRadioButton.setChecked(true);
        fragmentManager = getSupportFragmentManager();
        fragment = new OTPFragment();
        fragmentTransection = fragmentManager.beginTransaction();
        fragmentTransection = fragmentTransection.replace(R.id.frameContainer, fragment);
        fragmentTransection.commitAllowingStateLoss();
        ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.AUTHTYPESELECTED, AppConstant.OTP, context);
        ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.AadharNumber, aadhaarNo, context);
    }

    private String checkAppConfig() {
        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.parentLayout);
        LinearLayout aadhaarLayout = (LinearLayout) findViewById(R.id.adhaarLayout);
        RelativeLayout nonAadhaarLayout = (RelativeLayout) findViewById(R.id.nonAadhaarLayout);
        StateItem selectedStateItem = StateItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SELECTED_STATE, context));
        if (selectedStateItem != null && selectedStateItem.getStateCode() != null) {
            ArrayList<ConfigurationItem> configList = SeccDatabase.findConfiguration(selectedStateItem.getStateCode(), context);
            if (configList != null) {
                for (ConfigurationItem item1 : configList) {

                    if (item1.getConfigId().equalsIgnoreCase(AppConstant.APPLICATION_ZOOM)) {
                        zoomMode = item1.getStatus();
                    }
                    if (item1.getConfigId().equalsIgnoreCase(AppConstant.VALIDATION_MODE_CONFIG)) {
                        benefidentificationMode = item1.getStatus();
                    }

                    if (item1.getConfigId().equalsIgnoreCase(AppConstant.AADHAR_AUTH)) {
                        aadharAuthModeModel = item1.getStatus();
                    }

                    if (item1.getConfigId().equalsIgnoreCase(AppConstant.EKYC_SOURCE_CONFIG)) {
                        ekycMode = item1.getStatus();
                    }

                    if (item1.getConfigId().equalsIgnoreCase(AppConstant.DEMOGRAPHIC_SOURCE_CONFIG)) {
                        demoMode = item1.getStatus();
                    }

                }
            }
        }
        otpRadioButton.setVisibility(View.GONE);
        irisRadioButton.setVisibility(View.GONE);
        biometricRadioButton.setVisibility(View.GONE);

        if (ekycMode != null && !ekycMode.equalsIgnoreCase("")) {
            String mode = "";
            if (ekycMode.contains("I")) {
                mode = "Iris";
                otpRadioButton.setVisibility(View.GONE);
                irisRadioButton.setVisibility(View.VISIBLE);
                biometricRadioButton.setVisibility(View.GONE);
            }
            if (ekycMode.contains("F")) {
                mode = "FingerPrint";
                otpRadioButton.setVisibility(View.GONE);
                irisRadioButton.setVisibility(View.GONE);
                biometricRadioButton.setVisibility(View.VISIBLE);
            }
            if (ekycMode.contains("O")) {
                mode = "OTP";
                otpRadioButton.setVisibility(View.VISIBLE);
                irisRadioButton.setVisibility(View.GONE);
                biometricRadioButton.setVisibility(View.GONE);
            }
            if (ekycMode.contains("IF")) {
                mode = "Iris/FingerPrint";
                otpRadioButton.setVisibility(View.GONE);
                irisRadioButton.setVisibility(View.VISIBLE);
                biometricRadioButton.setVisibility(View.VISIBLE);
            }
            if (ekycMode.contains("IO")) {
                mode = "Iris/OTP";
                otpRadioButton.setVisibility(View.VISIBLE);
                irisRadioButton.setVisibility(View.VISIBLE);
                biometricRadioButton.setVisibility(View.GONE);
            }
            if (ekycMode.contains("FO")) {
                mode = "FingerPrint/OTP";
                otpRadioButton.setVisibility(View.VISIBLE);
                irisRadioButton.setVisibility(View.GONE);
                biometricRadioButton.setVisibility(View.VISIBLE);
            }
            if (ekycMode.contains("IFO")) {
                mode = "Iris/FingerPrint/OTP";
                otpRadioButton.setVisibility(View.VISIBLE);
                irisRadioButton.setVisibility(View.VISIBLE);
                biometricRadioButton.setVisibility(View.VISIBLE);
            }

        }
        return null;
    }


}

