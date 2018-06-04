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
import com.nhpm.Models.response.master.ConfigurationItem;
import com.nhpm.Models.response.master.StateItem;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;
import com.nhpm.fragments.AadharAuthManualFragment;
import com.nhpm.fragments.AadharAuthQrCode;
import com.nhpm.fragments.FindBeneficiaryByManualFragment;
import com.nhpm.fragments.FindBeneficiaryByQrCode;

import java.util.ArrayList;

/**
 * Created by SUNAINA on 01-06-2018.
 */

public class FindBeneficiaryByNameActivity extends BaseActivity {

    private Context context;
    private String zoomMode = "N";
    private String benefidentificationMode = "", ekycMode = "", demoMode = "", aadharAuthModeModel = "";
    private FragmentManager fragmentManager;
    private Fragment fragment;
    private FragmentTransaction fragmentTransection;
    private RadioGroup aadharAuthRG;
    private RadioButton manualRadioButton, QrCodeRadioButton;
    private RelativeLayout backLayout, menuLayout;
    private ImageView back;
    private TextView headerTV;
    private String aadhaarNo;
    private int resultCode = 1;
    private String activityName = "";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_demo_auth_layout);
        setupScreen();
    }

    private void setupScreen() {

        aadhaarNo = getIntent().getStringExtra("aadhaarNo");
        headerTV = (TextView) findViewById(R.id.centertext);
        headerTV.setText("Beneficiary Data");
        manualRadioButton = (RadioButton) findViewById(R.id.manualRadioButton);
        QrCodeRadioButton = (RadioButton) findViewById(R.id.qrCodeRadioButton);
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
        activityName = getIntent().getStringExtra("PhoneNumberActivity");

        if (isNetworkAvailable()) {
            /*if (activityName!=null && activityName.equalsIgnoreCase("")) {

                Intent intent =new Intent();
                onActivityResult(resultCode,Activity.RESULT_OK,intent);
            }else {
                openQrCodeFragment();
            }*/

            openQrCodeFragment();
        } else {
            CustomAlert.alertWithOk(context, getResources().getString(R.string.internet_connection_msg));

        }
        aadharAuthRG = (RadioGroup) findViewById(R.id.aadharAuthRG);
        aadharAuthRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (checkedId == QrCodeRadioButton.getId()) {
                    if (isNetworkAvailable()) {
                        openQrCodeFragment();
                    } else {
                        CustomAlert.alertWithOk(context, getResources().getString(R.string.internet_connection_msg));
                    }
                } else if (checkedId == manualRadioButton.getId()) {
                    if (isNetworkAvailable()) {

                        openManualFragment();
                        // startActivity();
                    } else {
                        CustomAlert.alertWithOk(context, getResources().getString(R.string.internet_connection_msg));
                    }
                }
            }
        });
    }


  /*  public void startActivity() {
        Intent intent = new Intent(context, PhoneNumberActivity.class);
        intent.putExtra("PhoneActivity", "Demo");
        startActivityForResult(intent, resultCode);
        startActivity(intent);
    }*/

  /*  @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                openManualFragment();
            }
        }
    }*/

    private void openQrCodeFragment() {
        QrCodeRadioButton.setChecked(true);
        fragmentManager = getSupportFragmentManager();
        fragment = new FindBeneficiaryByQrCode();
        fragmentTransection = fragmentManager.beginTransaction();
        fragmentTransection = fragmentTransection.replace(R.id.frameContainer, fragment);
        fragmentTransection.commitAllowingStateLoss();
        ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.AUTHTYPESELECTED, AppConstant.QRCODE, context);
        ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.AadharNumber, aadhaarNo, context);
    }

    private void openManualFragment() {
        manualRadioButton.setChecked(true);
        fragmentManager = getSupportFragmentManager();
        fragment = new FindBeneficiaryByManualFragment();
        fragmentTransection = fragmentManager.beginTransaction();
        fragmentTransection = fragmentTransection.replace(R.id.frameContainer, fragment);
        fragmentTransection.commitAllowingStateLoss();
        ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.AUTHTYPESELECTED, AppConstant.MANUALAUTH, context);
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

        QrCodeRadioButton.setVisibility(View.GONE);
        manualRadioButton.setVisibility(View.GONE);
        if (demoMode != null && !demoMode.equalsIgnoreCase("")) {
            String mode = "";
            if (demoMode.contains("Q")) {
                //  mode="QR Code";
                QrCodeRadioButton.setVisibility(View.VISIBLE);
                manualRadioButton.setVisibility(View.GONE);

            }
            if (demoMode.contains("M")) {
                mode = "Manual";
                manualRadioButton.setVisibility(View.VISIBLE);
            }
            if (demoMode.contains("QM")) {
                QrCodeRadioButton.setVisibility(View.VISIBLE);
                manualRadioButton.setVisibility(View.VISIBLE);
            }

        }
      /*  aadhaarLayout.setVisibility(View.GONE);
        nonAadhaarLayout.setVisibility(View.GONE);

        if (benefidentificationMode.equalsIgnoreCase("b")) {
            //appConfigWithValidationViaBoth();
            aadhaarLayout.setVisibility(View.VISIBLE);
            nonAadhaarLayout.setVisibility(View.VISIBLE);
        } else if (benefidentificationMode.equalsIgnoreCase("a")) {
            //appConfigWithValidationViaAadharOnly();
            aadhaarLayout.setVisibility(View.VISIBLE);

        } else if (benefidentificationMode.equalsIgnoreCase("g")) {
            // appConfigWithValidationViaGov();
            nonAadhaarLayout.setVisibility(View.VISIBLE);

        }*/
        return null;
    }
}
