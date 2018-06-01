package com.nhpm.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.customComponent.utility.DateTimeUtil;
import com.customComponent.utility.ProjectPrefrence;
import com.nhpm.LocalDataBase.SharedPrefrenceData;
import com.nhpm.LocalDataBase.dto.CommonDatabase;
import com.nhpm.LocalDataBase.dto.SeccDatabase;
import com.nhpm.Models.NotificationModel;
import com.nhpm.Models.response.master.ConfigurationItem;
import com.nhpm.Models.response.master.StateItem;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;
import com.nhpm.Utility.AppUtility;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import biz.kasual.materialnumberpicker.MaterialNumberPicker;


public class AppConfigActivity extends BaseActivity {

    private RadioGroup validationModeRG, dataSourceRG, printCardRG, additionalSchemeRG, addNewMemberRG, aadharAuthRG, dataDownloadRG,
            photoCollectRG, nomineeCollectRG, seccDownloadRG, zoomRG, biographicRG;
    private RadioButton rsbyYesRB, resbNoRB,/*dsSeccOnlyRB, dsBothRB, dsRsbyOnlyRB,*/
            vmAadharOnlyRB, vmGovernmentIdOnlyRB, vmBothRB,
            printCardYesBT, printCardNoBT, additionalSchemeStateBT, additionalSchemeRsbyBT, additionalSchemeBothBT,
            additionalSchemeNoneBT, aadharAuthBothBT, aadharEkycBT,
            aadharDemoAuthBT, villageViseBT, ebViseBT, photoCollectYesBT, photoCollectNoBT, nomineeCollectYesBT,
            nominneCollectNoBT, seccDataDownloadYesBT, seccDataDownloadNoBT, zoomYesBT, zoomNoBT, fingerBioBT, irisBioBT, wardViseBT, subEbViseBT;

    private Button updateConfig;
    private RelativeLayout backLayout;
    private ImageView back;
    private TextView centertext, selectNumber, downLoadWarning, dataCatogery;
    private Context context;
    private StateItem selectedStateItem;
    private ConfigurationItem rsbyDataModel;
    private ConfigurationItem printCardModel;
    private ConfigurationItem additionalSchemeModel;
    private ConfigurationItem validationModeModel;
    private ConfigurationItem aadharAuthModeModel;
    private ConfigurationItem ekycModel;
    private ConfigurationItem demoGraphicModeModel;
    private ConfigurationItem dataDownloadingModeModel;
    private ConfigurationItem photoCollectModeModel;
    private ConfigurationItem nomineeCollectModeModel;
    private ConfigurationItem applicationZoomModeModel;
    private ConfigurationItem seccDataDownloadModeModel;
    private ConfigurationItem biographicModeModel;
    private ConfigurationItem dataCategoryModeModel;
    private int sessionTimeOut;
    private ScrollView appConfigScrollView;
    private LinearLayout biographicLinearLayout;
    private EditText notificationMsgET, stateCodeET;
    private static TextView endDateTV;
    private static TextView startDateTV;
    private NotificationModel notificatioModelItem;
    private CheckBox checkForAddNotification;
    private CheckBox irisBioCB, fingerPrintBioCB, otpBioCB, qrCodeCB, manualCB;
    private String ekycStatus, demoStatus;
    private LinearLayout demographicLinearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_config);
        setupScreen();
    }


    private void setupScreen() {
        context = this;
        checkAppConfig();
        biographicLinearLayout = (LinearLayout) findViewById(R.id.biographicLinearLayout);
        notificationMsgET = (EditText) findViewById(R.id.notificationMsgET);
        stateCodeET = (EditText) findViewById(R.id.stateCodeET);
        selectedStateItem = StateItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SELECTED_STATE, context));
        if (selectedStateItem != null && selectedStateItem.getStateCode() != null && !selectedStateItem.getStateCode().equalsIgnoreCase("")) {
            stateCodeET.setEnabled(false);
            stateCodeET.setText(selectedStateItem.getStateCode());
        }


        endDateTV = (TextView) findViewById(R.id.endDateTV);
        dataCatogery = (TextView) findViewById(R.id.dataCatogery);
        startDateTV = (TextView) findViewById(R.id.startDateTV);
        checkForAddNotification = (CheckBox) findViewById(R.id.checkForAddNotification);
        biographicRG = (RadioGroup) findViewById(R.id.biographicRG);
        fingerBioBT = (RadioButton) findViewById(R.id.fingerBioBT);
        irisBioBT = (RadioButton) findViewById(R.id.irisBioBT);
        zoomRG = (RadioGroup) findViewById(R.id.zoomRG);
        zoomYesBT = (RadioButton) findViewById(R.id.zoomYesBT);
        zoomNoBT = (RadioButton) findViewById(R.id.zoomNoBT);
        back = (ImageView) findViewById(R.id.back);
        selectNumber = (TextView) findViewById(R.id.selectNumber);
        centertext = (TextView) findViewById(R.id.centertext);
        downLoadWarning = (TextView) findViewById(R.id.downLoadWarning);
        appConfigScrollView = (ScrollView) findViewById(R.id.appConfigScrollView);
        centertext.setText(context.getResources().getString(R.string.appConfig));
        backLayout = (RelativeLayout) findViewById(R.id.backLayout);
        validationModeRG = (RadioGroup) findViewById(R.id.validationModeRG);
        dataSourceRG = (RadioGroup) findViewById(R.id.dataSourceRG);
        aadharAuthRG = (RadioGroup) findViewById(R.id.aadharAuthRG);
        rsbyYesRB = (RadioButton) findViewById(R.id.rsbyYesRB);
        photoCollectRG = (RadioGroup) findViewById(R.id.photoCollectRG);
        photoCollectYesBT = (RadioButton) findViewById(R.id.photoCollectYesBT);
        photoCollectNoBT = (RadioButton) findViewById(R.id.photoCollectNoBT);
        nomineeCollectRG = (RadioGroup) findViewById(R.id.nomineeCollectRG);
        nomineeCollectYesBT = (RadioButton) findViewById(R.id.nomineeCollectYesBT);
        nominneCollectNoBT = (RadioButton) findViewById(R.id.nominneCollectNoBT);
        seccDownloadRG = (RadioGroup) findViewById(R.id.seccDownloadRG);
        seccDataDownloadYesBT = (RadioButton) findViewById(R.id.seccDataDownloadYesBT);
        seccDataDownloadNoBT = (RadioButton) findViewById(R.id.seccDataDownloadNoBT);
        resbNoRB = (RadioButton) findViewById(R.id.resbNoRB);

        vmAadharOnlyRB = (RadioButton) findViewById(R.id.vmAadharOnlyRB);
        vmGovernmentIdOnlyRB = (RadioButton) findViewById(R.id.vmGovernmentIdOnlyRB);
        vmBothRB = (RadioButton) findViewById(R.id.vmBothRB);
        printCardYesBT = (RadioButton) findViewById(R.id.printCardYesBT);
        printCardNoBT = (RadioButton) findViewById(R.id.printCardNoBT);
        additionalSchemeStateBT = (RadioButton) findViewById(R.id.additionalSchemeStateBT);
        additionalSchemeRsbyBT = (RadioButton) findViewById(R.id.additionalSchemeRsbyBT);
        additionalSchemeNoneBT = (RadioButton) findViewById(R.id.additionalSchemeNoneBT);
        additionalSchemeBothBT = (RadioButton) findViewById(R.id.additionalSchemeBothBT);
        wardViseBT = (RadioButton) findViewById(R.id.wardViseBT);
        subEbViseBT = (RadioButton) findViewById(R.id.subEbViseBT);
        villageViseBT = (RadioButton) findViewById(R.id.villageViseBT);
        ebViseBT = (RadioButton) findViewById(R.id.ebViseBT);
        dataDownloadRG = (RadioGroup) findViewById(R.id.dataDownloadRG);
        aadharAuthBothBT = (RadioButton) findViewById(R.id.aadharAuthBothBT);
        aadharEkycBT = (RadioButton) findViewById(R.id.aadharEkycBT);
        aadharDemoAuthBT = (RadioButton) findViewById(R.id.aadharDemoAuthBT);
        printCardRG = (RadioGroup) findViewById(R.id.printCardRG);
        additionalSchemeRG = (RadioGroup) findViewById(R.id.additionalSchemeRG);
        irisBioCB = (CheckBox) findViewById(R.id.irisBioCB);
        fingerPrintBioCB = (CheckBox) findViewById(R.id.fingerPrintBioCB);
        otpBioCB = (CheckBox) findViewById(R.id.otpBioCB);
        updateConfig = (Button) findViewById(R.id.updateConfig);
        qrCodeCB = (CheckBox) findViewById(R.id.qrCodeCB);
        manualCB = (CheckBox) findViewById(R.id.manualCB);
        demographicLinearLayout = (LinearLayout) findViewById(R.id.demographicLinearLayout);
        demographicLinearLayout.setVisibility(View.GONE);
        startDateTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStartDatePickerDialog(v);
            }
        });

        endDateTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEndDatePickerDialog(v);
            }
        });

        startDateTV.setText(setStartDate());
        endDateTV.setText(setEndDate());
        if (ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SESSIONTIMEOUT, context) != null) {
            sessionTimeOut = Integer.parseInt(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SESSIONTIMEOUT, context));
        } else {
            sessionTimeOut = Integer.parseInt(AppConstant.SESSIONTIME);
        }
        final MaterialNumberPicker numberPicker = new MaterialNumberPicker.Builder(context)
                .minValue(2)
                .maxValue(180)
                .defaultValue(sessionTimeOut)
                .backgroundColor(Color.WHITE)
                .separatorColor(Color.TRANSPARENT)
                .textColor(Color.BLACK)
                .textSize(20)
                .enableFocusability(false)
                .wrapSelectorWheel(true)
                .build();
        backLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (biographicModeModel != null) {
            if (biographicModeModel.getStatus() != null && !biographicModeModel.getStatus().equalsIgnoreCase("")) {
                if (biographicModeModel.getStatus().equalsIgnoreCase("I")) {
                    irisBioBT.setChecked(true);
                } else if (biographicModeModel.getStatus().equalsIgnoreCase("F")) {
                    fingerBioBT.setChecked(true);
                } else {
                    biographicLinearLayout.setVisibility(View.GONE);
                }
            }

        }


        if (validationModeModel != null) {
            if (validationModeModel.getStatus() != null && !validationModeModel.getStatus().equalsIgnoreCase("")) {
                if (validationModeModel.getStatus().equalsIgnoreCase("B")) {
                    vmBothRB.setChecked(true);
                } else if (validationModeModel.getStatus().equalsIgnoreCase("G")) {
                    vmGovernmentIdOnlyRB.setChecked(true);
                } else if (validationModeModel.getStatus().equalsIgnoreCase("A")) {
                    vmAadharOnlyRB.setChecked(true);
                }

            }
        }


        if (dataCategoryModeModel != null) {
            if (dataCategoryModeModel.getAcceptedvalueName() != null) {
                dataCatogery.setText(dataCategoryModeModel.getAcceptedvalueName());
            }
        }


        if (dataDownloadingModeModel != null) {
            if (dataDownloadingModeModel.getStatus() != null && !dataDownloadingModeModel.getStatus().equalsIgnoreCase("")) {
                if (dataDownloadingModeModel.getStatus().equalsIgnoreCase(AppConstant.VillageWiseDownloading)) {
                    villageViseBT.setChecked(true);
                } else if (dataDownloadingModeModel.getStatus().equalsIgnoreCase(AppConstant.EbWiseDownloading)) {
                    ebViseBT.setChecked(true);
                } else if (dataDownloadingModeModel.getStatus().equalsIgnoreCase(AppConstant.WardWiseDownloading)) {
                    wardViseBT.setChecked(true);
                } else if (dataDownloadingModeModel.getStatus().equalsIgnoreCase(AppConstant.SubBlockWiseDownloading)) {
                    subEbViseBT.setChecked(true);
                }


            } else {
                ebViseBT.setChecked(true);
            }
        } else {
            ebViseBT.setChecked(true);
        }

        if (additionalSchemeModel != null) {
            if (additionalSchemeModel.getStatus() != null && !additionalSchemeModel.getStatus().equalsIgnoreCase("")) {
                if (additionalSchemeModel.getStatus().equalsIgnoreCase("S")) {
                    additionalSchemeStateBT.setChecked(true);
                } else if (additionalSchemeModel.getStatus().equalsIgnoreCase("R")) {
                    additionalSchemeRsbyBT.setChecked(true);
                } else if (additionalSchemeModel.getStatus().equalsIgnoreCase("B")) {
                    additionalSchemeBothBT.setChecked(true);
                } else if (additionalSchemeModel.getStatus().equalsIgnoreCase("N")) {
                    additionalSchemeNoneBT.setChecked(true);
                }
            }

        }

        if (aadharAuthModeModel != null) {
            if (aadharAuthModeModel.getStatus() != null && !aadharAuthModeModel.getStatus().equalsIgnoreCase("")) {
                if (aadharAuthModeModel.getStatus().equalsIgnoreCase("D")) {
                    biographicModeModel.setStatus("N");
                    biographicLinearLayout.setVisibility(View.GONE);
                    demographicLinearLayout.setVisibility(View.VISIBLE);
                    aadharDemoAuthBT.setChecked(true);
                } else if (aadharAuthModeModel.getStatus().equalsIgnoreCase("E")) {
                    biographicLinearLayout.setVisibility(View.VISIBLE);
                    demographicLinearLayout.setVisibility(View.GONE);
                    aadharEkycBT.setChecked(true);
                } else if (aadharAuthModeModel.getStatus().equalsIgnoreCase("B")) {
                    biographicLinearLayout.setVisibility(View.VISIBLE);
                    demographicLinearLayout.setVisibility(View.VISIBLE);
                    aadharAuthBothBT.setChecked(true);
                }

            } else {
                aadharDemoAuthBT.setChecked(true);
                biographicLinearLayout.setVisibility(View.GONE);
            }
        } else {
            aadharDemoAuthBT.setChecked(true);
            biographicLinearLayout.setVisibility(View.GONE);
        }


        if (printCardModel != null) {
            if (printCardModel.getStatus() != null && !printCardModel.getStatus().equalsIgnoreCase("")) {
                if (printCardModel.getStatus().equalsIgnoreCase("Y")) {
                    printCardYesBT.setChecked(true);
                } else
                    printCardNoBT.setChecked(true);
            }

        }

        if (applicationZoomModeModel != null) {
            if (applicationZoomModeModel.getStatus() != null && !applicationZoomModeModel.getStatus().equalsIgnoreCase("")) {
                if (applicationZoomModeModel.getStatus().equalsIgnoreCase("Y")) {
                    zoomYesBT.setChecked(true);
                } else
                    zoomNoBT.setChecked(true);
            }

        }
        if (photoCollectModeModel != null) {
            if (photoCollectModeModel.getStatus() != null && !photoCollectModeModel.getStatus().equalsIgnoreCase("")) {
                if (photoCollectModeModel.getStatus().equalsIgnoreCase("Y")) {
                    photoCollectYesBT.setChecked(true);
                } else
                    photoCollectNoBT.setChecked(true);
            }

        }
        if (ekycModel != null) {
            if (ekycModel.getStatus() != null && !ekycModel.getStatus().equalsIgnoreCase("")) {
                if (ekycModel.getStatus().contains("I")) {
                    irisBioCB.setChecked(true);
                }
                if (ekycModel.getStatus().contains("F")) {
                    fingerPrintBioCB.setChecked(true);
                }
                if (ekycModel.getStatus().contains("O")) {
                    otpBioCB.setChecked(true);
                }
            }
        }

        if (demoGraphicModeModel != null) {
            if (demoGraphicModeModel.getStatus() != null &&
                    !demoGraphicModeModel.getStatus().equalsIgnoreCase("")) {
                if (demoGraphicModeModel.getStatus().contains("Q")) {
                    qrCodeCB.setChecked(true);
                }

                if (demoGraphicModeModel.getStatus().contains("M")) {
                    manualCB.setChecked(true);
                }
            }
        }
        biographicRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == irisBioBT.getId()) {
                    biographicModeModel.setStatus("I");

                } else if (checkedId == fingerBioBT.getId()) {

                    biographicModeModel.setStatus("F");

                }
            }

        });
        seccDownloadRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == seccDataDownloadYesBT.getId()) {
                    seccDataDownloadModeModel.setStatus("Y");

                } else if (checkedId == seccDataDownloadNoBT.getId()) {

                    seccDataDownloadModeModel.setStatus("N");

                }
            }

        });

        if (nomineeCollectModeModel != null) {
            if (nomineeCollectModeModel.getStatus() != null && !nomineeCollectModeModel.getStatus().equalsIgnoreCase("")) {
                if (nomineeCollectModeModel.getStatus().equalsIgnoreCase("Y")) {
                    nomineeCollectYesBT.setChecked(true);
                } else
                    nominneCollectNoBT.setChecked(true);
            }

        }

        nomineeCollectRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == nomineeCollectYesBT.getId()) {
                    nomineeCollectModeModel.setStatus("Y");

                } else if (checkedId == nominneCollectNoBT.getId()) {

                    nomineeCollectModeModel.setStatus("N");

                }
            }

        });


        if (seccDataDownloadModeModel != null) {
            if (seccDataDownloadModeModel.getStatus() != null && !seccDataDownloadModeModel.getStatus().equalsIgnoreCase("")) {
                if (seccDataDownloadModeModel.getStatus().equalsIgnoreCase("Y")) {
                    seccDataDownloadYesBT.setChecked(true);
                } else
                    seccDataDownloadNoBT.setChecked(true);
            }

        }

        photoCollectRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == photoCollectYesBT.getId()) {
                    photoCollectModeModel.setStatus("Y");

                } else if (checkedId == photoCollectNoBT.getId()) {

                    photoCollectModeModel.setStatus("N");

                }
            }

        });

        aadharAuthRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (aadharAuthModeModel != null) {
                    if (aadharAuthModeModel.getStatus() != null && !aadharAuthModeModel.getStatus().equalsIgnoreCase("")) {

                        if (checkedId == aadharDemoAuthBT.getId()) {
                            biographicLinearLayout.setVisibility(View.GONE);
                            demographicLinearLayout.setVisibility(View.VISIBLE);
                            aadharAuthModeModel.setStatus("D");
                            biographicModeModel.setStatus("N");
                        } else if (checkedId == aadharEkycBT.getId()) {
                            biographicLinearLayout.setVisibility(View.VISIBLE);
                            demographicLinearLayout.setVisibility(View.GONE);
                            aadharAuthModeModel.setStatus("E");
                        } else if (checkedId == aadharAuthBothBT.getId()) {
                            biographicLinearLayout.setVisibility(View.VISIBLE);
                            demographicLinearLayout.setVisibility(View.VISIBLE);
                            aadharAuthModeModel.setStatus("B");
                        }
                    } else {
                        Log.d("AadharAuthModelStatus", "AadharAuthModelStatus is null");
                    }
                } else {
                    Log.d("AadharAuthModel", "AadharAuthModel is null");
                }
            }

        });
        validationModeRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (validationModeModel != null) {
                    if (validationModeModel.getStatus() != null && !validationModeModel.getStatus().equalsIgnoreCase("")) {
                        if (checkedId == vmAadharOnlyRB.getId()) {
                            validationModeModel.setStatus("A");
                        } else if (checkedId == vmGovernmentIdOnlyRB.getId()) {
                            validationModeModel.setStatus("G");
                        } else if (checkedId == vmBothRB.getId()) {
                            validationModeModel.setStatus("B");
                        }
                    }
                }
            }

        });
        if (rsbyDataModel != null) {
            if (rsbyDataModel.getStatus() != null && !rsbyDataModel.getStatus().equalsIgnoreCase("")) {

                if (rsbyDataModel.getStatus().equalsIgnoreCase("Y")) {
                    rsbyYesRB.setChecked(true);
                } else if (rsbyDataModel.getStatus().equalsIgnoreCase("N")) {
                    resbNoRB.setChecked(true);
                }

            }
        }


        additionalSchemeRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == additionalSchemeStateBT.getId()) {

                    additionalSchemeModel.setStatus("S");

                } else if (checkedId == additionalSchemeRsbyBT.getId()) {

                    additionalSchemeModel.setStatus("R");

                } else if (checkedId == additionalSchemeBothBT.getId()) {

                    additionalSchemeModel.setStatus("B");

                } else if (checkedId == additionalSchemeNoneBT.getId()) {

                    additionalSchemeModel.setStatus("N");

                }
            }

        });
        dataDownloadRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == ebViseBT.getId()) {
                    dataDownloadingModeModel.setStatus(AppConstant.EbWiseDownloading);
                    ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.DOWNLOADINGSOURCE, AppConstant.EbWiseDownloading, context);
                } else if (checkedId == villageViseBT.getId()) {
                    ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.DOWNLOADINGSOURCE, AppConstant.VillageWiseDownloading, context);
                    dataDownloadingModeModel.setStatus(AppConstant.VillageWiseDownloading);

                } else if (checkedId == subEbViseBT.getId()) {
                    ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.DOWNLOADINGSOURCE, AppConstant.SubBlockWiseDownloading, context);
                    dataDownloadingModeModel.setStatus(AppConstant.SubBlockWiseDownloading);

                } else if (checkedId == wardViseBT.getId()) {
                    ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.DOWNLOADINGSOURCE, AppConstant.WardWiseDownloading, context);
                    dataDownloadingModeModel.setStatus(AppConstant.WardWiseDownloading);

                }
            }

        });

        if (SeccDatabase.getAllHouseHold(context).size() > 0) {
            ebViseBT.setEnabled(false);
            villageViseBT.setEnabled(false);
            //  downLoadWarning.setVisibility(View.VISIBLE);
            rsbyYesRB.setEnabled(false);
            resbNoRB.setEnabled(false);
            seccDataDownloadNoBT.setEnabled(false);
            seccDataDownloadYesBT.setEnabled(false);
            subEbViseBT.setEnabled(false);
            wardViseBT.setEnabled(false);
        } else {
            ebViseBT.setEnabled(true);
            villageViseBT.setEnabled(true);
            rsbyYesRB.setEnabled(true);
            resbNoRB.setEnabled(true);
            seccDataDownloadNoBT.setEnabled(true);
            seccDataDownloadYesBT.setEnabled(true);
            downLoadWarning.setVisibility(View.GONE);
            subEbViseBT.setEnabled(true);
            wardViseBT.setEnabled(true);
        }

        dataDownloadRG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SeccDatabase.getAllHouseHold(context).size() > 0) {
                    AppUtility.alertWithOk(context, "Clear device to change downloading mode");
                }

            }
        });


        printCardRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == printCardYesBT.getId()) {
                    printCardModel.setStatus("Y");

                } else if (checkedId == printCardYesBT.getId()) {

                    printCardModel.setStatus("N");

                }
            }

        });
        zoomRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == zoomYesBT.getId()) {
                    applicationZoomModeModel.setStatus("Y");

                } else if (checkedId == zoomNoBT.getId()) {

                    applicationZoomModeModel.setStatus("N");

                }
            }

        });
        dataSourceRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == rsbyYesRB.getId()) {
                    rsbyDataModel.setStatus("Y");

                } else if (checkedId == resbNoRB.getId()) {

                    rsbyDataModel.setStatus("N");

                }
            }

        });


        updateConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (rsbyDataModel != null && seccDataDownloadModeModel != null && rsbyDataModel.getStatus().equalsIgnoreCase("N") && seccDataDownloadModeModel.getStatus().equalsIgnoreCase("N")) {
                    AppUtility.alertWithOk(context, "Please check RSBY data import as Yes or SECC data download as YES");
                } else {

                    if (aadharAuthModeModel.getStatus().equalsIgnoreCase("D")) {

                    } else if (aadharAuthModeModel.getStatus().equalsIgnoreCase("E")) {
                      /*  if (biographicModeModel.getStatus() != null) {
                            if (biographicModeModel.getStatus().equalsIgnoreCase("") || biographicModeModel.getStatus().equalsIgnoreCase("N")) {
                                AppUtility.alertWithOk(context, "Please provide Biographic KYC mode");
                                return;
                            }
                        } else {
                            AppUtility.alertWithOk(context, "Please provide Biographic KYC mode");
                            return;
                        }*/

                    } else if (aadharAuthModeModel.getStatus().equalsIgnoreCase("B")) {

                      /*  if (biographicModeModel != null && biographicModeModel.getStatus() != null) {
                            if (biographicModeModel.getStatus().equalsIgnoreCase("") || biographicModeModel.getStatus().equalsIgnoreCase("N")) {
                                AppUtility.alertWithOk(context, "Please provide Biographic KYC mode");
                                return;
                            }
                        } else {
                            AppUtility.alertWithOk(context, "Please provide Biographic KYC mode");
                            return;
                        }*/
                    }
                    if (checkForAddNotification.isChecked()) {
                        if (!notificationMsgET.getText().toString().equalsIgnoreCase("")) {
                            if (!stateCodeET.getText().toString().equalsIgnoreCase("")) {
                                if (!startDateTV.getText().toString().equalsIgnoreCase("")) {
                                    if (!endDateTV.getText().toString().equalsIgnoreCase("")) {
                                        notificatioModelItem = new NotificationModel();
                                        String startDate = String.valueOf(DateTimeUtil.
                                                convertDateIntoTimeMillis(startDateTV.getText().toString(), AppConstant.dateTimeFormate));
                                        String endDate = String.valueOf(DateTimeUtil.
                                                convertDateIntoTimeMillis(endDateTV.getText().toString(), AppConstant.dateTimeFormate));
                                        long time = System.currentTimeMillis();
                                        notificatioModelItem.setId(time + "");
                                        notificatioModelItem.setAadhaarNo("352624429973");
                                        notificatioModelItem.setActiveStatus("Y");
                                        notificatioModelItem.setIpAddress("192.168.191.2");
                                        notificatioModelItem.setSource("1");
                                        notificatioModelItem.setCreatedDate(startDateTV.getText().toString());
                                        notificatioModelItem.setStartDate(startDateTV.getText().toString());
                                        notificatioModelItem.setDateExpire(endDateTV.getText().toString());
                                        /* notificatioModelItem.setCreatedDate(startDate);
                                        notificatioModelItem.setDateExpire(endDate);*/
                                        notificatioModelItem.setDescription(notificationMsgET.getText().toString());
                                        notificatioModelItem.setTargetState(stateCodeET.getText().toString());


                                    } else {
                                        AppUtility.alertWithOk(context, "Please provide end date of notification");
                                        return;
                                    }
                                } else {
                                    AppUtility.alertWithOk(context, "Please provide start date of notification");
                                    return;
                                }

                            } else {
                                AppUtility.alertWithOk(context, "Please provide state code for notification");
                                return;
                            }
                        } else {
                            AppUtility.alertWithOk(context, "Please provide noification message");
                            return;
                        }
                        if (notificatioModelItem != null) {
                            CommonDatabase.saveNotification(notificatioModelItem, context);
                        }
                    }

                    String iris = null, bio = null, otp = null, qrcode = null, manual = null;
                    if (qrCodeCB.isChecked()) {
                        qrcode = "Q";
                    }
                    if (manualCB.isChecked()) {
                        manual = "M";
                    }
                    if (qrcode != null) {
                        demoStatus = qrcode;
                    }
                    if (manual != null) {
                        demoStatus = demoStatus + manual;
                    }
                    if (demoGraphicModeModel == null) {
                        demoGraphicModeModel = new ConfigurationItem();
                        demoGraphicModeModel.setConfigId(AppConstant.DEMOGRAPHIC_SOURCE_CONFIG);
                        demoGraphicModeModel.setStatus(demoStatus);
                        demoGraphicModeModel.setStateCode(selectedStateItem.getStateCode());
                        SeccDatabase.saveApplicationConfigData(demoGraphicModeModel, context);
                    } else {
                        demoGraphicModeModel.setConfigId(AppConstant.DEMOGRAPHIC_SOURCE_CONFIG);
                        demoGraphicModeModel.setStatus(demoStatus);
                        demoGraphicModeModel.setStateCode(selectedStateItem.getStateCode());
                        SeccDatabase.updateAppConf(demoGraphicModeModel, context);
                    }
                    if (irisBioCB.isChecked()) {
                        // ekycStatus=ekycStatus+"I";
                        iris = "I";
                    }
                    if (fingerPrintBioCB.isChecked()) {
                        bio = "F";
                    }
                    if (otpBioCB.isChecked()) {
                        otp = "O";
                    }
                    if (iris != null) {
                        ekycStatus = iris;
                    }
                    if (bio != null) {
                        ekycStatus = ekycStatus + bio;
                    }
                    if (otp != null) {
                        ekycStatus = ekycStatus + otp;
                    }
                    if (ekycModel == null) {
                        ekycModel = new ConfigurationItem();
                        ekycModel.setConfigId(AppConstant.EKYC_SOURCE_CONFIG);
                        ekycModel.setStatus(ekycStatus);
                        ekycModel.setStateCode(selectedStateItem.getStateCode());
                        SeccDatabase.saveApplicationConfigData(ekycModel, context);
                    } else {
                        ekycModel.setConfigId(AppConstant.EKYC_SOURCE_CONFIG);
                        ekycModel.setStatus(ekycStatus);
                        ekycModel.setStateCode(selectedStateItem.getStateCode());
                        SeccDatabase.updateAppConf(ekycModel, context);
                    }


                    SeccDatabase.updateAppConf(rsbyDataModel, context);
                    SeccDatabase.updateAppConf(validationModeModel, context);
                    SeccDatabase.updateAppConf(additionalSchemeModel, context);
                    SeccDatabase.updateAppConf(printCardModel, context);
                    SeccDatabase.updateAppConf(aadharAuthModeModel, context);
                    SeccDatabase.updateAppConf(dataDownloadingModeModel, context);
                    SeccDatabase.updateAppConf(photoCollectModeModel, context);
                    SeccDatabase.updateAppConf(nomineeCollectModeModel, context);
                    SeccDatabase.updateAppConf(seccDataDownloadModeModel, context);
                    SeccDatabase.updateAppConf(applicationZoomModeModel, context);
                    SeccDatabase.updateAppConf(biographicModeModel, context);


                    Intent theIntent = new Intent(context, BlockDetailActivity.class);
                    theIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    theIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(theIntent);
                    finish();
                }
            }
        });


        selectNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (numberPicker.getParent() != null) {
                    ((ViewGroup) numberPicker.getParent()).removeView(numberPicker);
                }
                AlertDialog.Builder mdialog = new AlertDialog.Builder(context);
                mdialog.setTitle("Set session time out (minutes)")
                        .setView(numberPicker)
                        .setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(context, "Session timeout time : " + numberPicker.getValue() + " minutes", Toast.LENGTH_LONG).show();
                                ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SESSIONTIMEOUT, String.valueOf(numberPicker.getValue()), context);
                                AppConfigActivity.this.recreate();
                                appConfigScrollView.fullScroll(View.FOCUS_DOWN);
                                // Snackbar.make(findViewById(R.id.your_container), "You picked : " + numberPicker.getValue(), Snackbar.LENGTH_LONG).show();
                            }
                        })
                        .show();
            }
        });

        // Disable All views.
        disableView();

    }

    private void checkAppConfig() {
        selectedStateItem = StateItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SELECTED_STATE, context));
        ArrayList<ConfigurationItem> configList;
        if (selectedStateItem != null && selectedStateItem.getStateCode() != null) {
            configList = SeccDatabase.findConfiguration(selectedStateItem.getStateCode(), context);
        } else {
            configList = SeccDatabase.findConfiguration("24", context);
        }
        if (configList != null) {
            for (ConfigurationItem item1 : configList) {
                if (item1.getConfigId().equalsIgnoreCase(AppConstant.RSBY_DATA_SOURCE_CONFIG)) {
                    rsbyDataModel = item1;
                }
                if (item1.getConfigId().equalsIgnoreCase(AppConstant.VALIDATION_MODE_CONFIG)) {
                    validationModeModel = item1;
                }
                if (item1.getConfigId().equalsIgnoreCase(AppConstant.PRINT_CARD)) {
                    printCardModel = item1;
                }
                if (item1.getConfigId().equalsIgnoreCase(AppConstant.ADDITIONAL_SCHEME)) {
                    additionalSchemeModel = item1;
                }
                if (item1.getConfigId().equalsIgnoreCase(AppConstant.AADHAR_AUTH)) {
                    aadharAuthModeModel = item1;
                }
                if (item1.getConfigId().equalsIgnoreCase(AppConstant.DATA_DOWNLOAD)) {
                    dataDownloadingModeModel = item1;
                }
                if (item1.getConfigId().equalsIgnoreCase(AppConstant.PHOTO_COLLECT)) {
                    photoCollectModeModel = item1;
                }
                if (item1.getConfigId().equalsIgnoreCase(AppConstant.NOMINEE_COLLECT)) {
                    nomineeCollectModeModel = item1;
                }
                if (item1.getConfigId().equalsIgnoreCase(AppConstant.SECC_DOWNLOAD)) {
                    seccDataDownloadModeModel = item1;
                }
                if (item1.getConfigId().equalsIgnoreCase(AppConstant.APPLICATION_ZOOM)) {
                    applicationZoomModeModel = item1;
                }
                if (item1.getConfigId().equalsIgnoreCase(AppConstant.BIOGRAPHIC_AUTH)) {
                    biographicModeModel = item1;
                }
                if (item1.getConfigId().equalsIgnoreCase(AppConstant.DATA_CATEGORY)) {
                    dataCategoryModeModel = item1;
                }
                if (item1.getConfigId().equalsIgnoreCase((AppConstant.EKYC_SOURCE_CONFIG))) {
                    ekycModel = item1;
                }

                if (item1.getConfigId().equalsIgnoreCase((AppConstant.DEMOGRAPHIC_SOURCE_CONFIG))) {
                    demoGraphicModeModel = item1;
                }


            }
        }


    }


    private void showStartDatePickerDialog(View view) {
        DialogFragment newFragment = new StartDatePickerFragment();

        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public static class StartDatePickerFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            DatePickerDialog datePickerDialog;
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);
            datePickerDialog.show();
            return datePickerDialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            Calendar cal = Calendar.getInstance();
            Date currentLocalTime = cal.getTime();
            DateFormat date = new SimpleDateFormat("HH:mm:ss");
            String localTime = date.format(currentLocalTime);
            System.out.print(localTime);
            // yyyy-MM-dd HH:mm:ss
            startDateTV.setText(year + "-" + (month + 1) + "-" + day);
        }


    }


    private void showEndDatePickerDialog(View view) {
        DialogFragment newFragment = new EndDatePickerFragment();

        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public static class EndDatePickerFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            DatePickerDialog datePickerDialog;
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);
            datePickerDialog.show();
            return datePickerDialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            Calendar cal = Calendar.getInstance();
            Date currentLocalTime = cal.getTime();
            DateFormat date = new SimpleDateFormat("HH:mm:ss");
            String localTime = date.format(currentLocalTime);
            System.out.print(localTime);
            // yyyy-MM-dd HH:mm:ss5yyyyyyyyy
            endDateTV.setText(year + "-" + (month + 1) + "-" + day);
        }


    }


    private String setStartDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return dateFormat.format(date);
    }


    private String setEndDate() {
        String dateToreturn;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String currentDate = dateFormat.format(date);
        SimpleDateFormat dateFormatN = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(dateFormatN.parse(currentDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        cal.add(Calendar.DATE, 2);
        String convertedDate = dateFormat.format(cal.getTime());

        return convertedDate;

    }

    private void disableView() {
        rsbyYesRB.setEnabled(true);
        resbNoRB.setEnabled(true);
        vmAadharOnlyRB.setEnabled(true);
        vmGovernmentIdOnlyRB.setEnabled(true);
        vmBothRB.setEnabled(true);
        printCardYesBT.setEnabled(true);
        printCardNoBT.setEnabled(true);
        additionalSchemeStateBT.setEnabled(true);
        additionalSchemeRsbyBT.setEnabled(true);
        additionalSchemeBothBT.setEnabled(true);
        additionalSchemeNoneBT.setEnabled(true);
        aadharAuthBothBT.setEnabled(true);
        aadharEkycBT.setEnabled(true);
        aadharDemoAuthBT.setEnabled(true);
        villageViseBT.setEnabled(true);
        ebViseBT.setEnabled(true);
        photoCollectYesBT.setEnabled(true);
        photoCollectNoBT.setEnabled(true);
        nomineeCollectYesBT.setEnabled(true);
        nominneCollectNoBT.setEnabled(true);
        seccDataDownloadYesBT.setEnabled(true);
        seccDataDownloadNoBT.setEnabled(true);
        zoomYesBT.setEnabled(true);
        zoomNoBT.setEnabled(true);
        fingerBioBT.setEnabled(true);
        irisBioBT.setEnabled(true);
        wardViseBT.setEnabled(true);
        subEbViseBT.setEnabled(true);
    }
}
