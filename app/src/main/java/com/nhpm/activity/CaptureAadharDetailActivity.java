package com.nhpm.activity;

import android.app.AlertDialog;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.customComponent.CustomAlert;
import com.customComponent.utility.ProjectPrefrence;
import com.nhpm.LocalDataBase.dto.SeccDatabase;
import com.nhpm.Models.response.master.ConfigurationItem;
import com.nhpm.Models.response.master.StateItem;
import com.nhpm.Models.response.seccMembers.SeccMemberItem;
import com.nhpm.Models.response.seccMembers.SelectedMemberItem;
import com.nhpm.Models.response.verifier.VerifierLoginResponse;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;
import com.nhpm.Utility.AppUtility;
import com.nhpm.fragments.AadhaarFingerPrintViaRDSevices;
import com.nhpm.fragments.AadhaarIrisViaRDServices;
import com.nhpm.fragments.AadharAuthManualFragment;
import com.nhpm.fragments.AadharAuthQrCode;

import java.util.ArrayList;

import pl.polidea.view.ZoomView;

public class CaptureAadharDetailActivity extends BaseActivity implements ComponentCallbacks2 {

    private Context context;
    private FragmentManager fragmentManager;
    private Fragment fragment;
    private ImageView settings;
    private FragmentTransaction fragmentTransection;
    private RadioGroup aadharAuthRG;
    private RadioButton irisRadioButton, biometricRadioButton, manualRadioButton, QrCodeRadioButton;
    private String aadharAuthMode;
    private RelativeLayout backLayout, menuLayout;
    private ImageView back;
    private SeccMemberItem seccItem;
    private LinearLayout mZoomLinearLayout;
    private SelectedMemberItem selectedMemItem;
    private boolean pinLockIsShown = false;
    private String zoomMode = "N";
    private String boigraphicMode;

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
            setContentView(R.layout.activity_capture_aadhar_detail);
            setScreenWithoutZoom();

        } else {
            setContentView(R.layout.dummy_layout_for_zooming);
            setupScreenWithZoom();
        }
        String authType = ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.AUTHTYPESELECTED, context);

    /*    if (seccItem != null && seccItem.getAadhaarCapturingMode() != null && !seccItem.getAadhaarCapturingMode().equalsIgnoreCase("")) {
            if (seccItem.getAadhaarCapturingMode().equalsIgnoreCase(AppConstant.IRIS_MODE)) {
                openIrisFragment();
            } else if (seccItem.getAadhaarCapturingMode().equalsIgnoreCase(AppConstant.FINGER_MODE)) {
                openFingerPrintFragment();
            } else if (seccItem.getAadhaarCapturingMode().equalsIgnoreCase(AppConstant.QR_CODE_MODE)) {
                openQrCodeFragment();
            } else if (seccItem.getAadhaarCapturingMode().equalsIgnoreCase(AppConstant.MANUAL_MODE)) {
                openManualFragment();
            }


       } else {*/
        if (aadharAuthMode != null && !aadharAuthMode.equalsIgnoreCase("")) {
            if (aadharAuthMode.equalsIgnoreCase("D")) {
                manualRadioButton.setVisibility(View.VISIBLE);
                irisRadioButton.setVisibility(View.GONE);
                biometricRadioButton.setVisibility(View.GONE);
                QrCodeRadioButton.setVisibility(View.VISIBLE);
                if (authType != null && !authType.equalsIgnoreCase("")) {
                    if (authType.equalsIgnoreCase(AppConstant.QRCODE)) {
                        openQrCodeFragment();
                    } else if (authType.equalsIgnoreCase(AppConstant.MANUALAUTH)) {
                        openManualFragment();
                    }
                } else {
                    openQrCodeFragment();
                }

            } else if (aadharAuthMode.equalsIgnoreCase("E")) {

                if (boigraphicMode != null && boigraphicMode.equalsIgnoreCase("I")) {
                    irisRadioButton.setVisibility(View.VISIBLE);
                    biometricRadioButton.setVisibility(View.GONE);
                    openIrisFragment();
                } else if (boigraphicMode != null && boigraphicMode.equalsIgnoreCase("F")) {
                    biometricRadioButton.setVisibility(View.VISIBLE);
                    irisRadioButton.setVisibility(View.GONE);
                    openFingerPrintFragment();
                }

                manualRadioButton.setVisibility(View.GONE);

                QrCodeRadioButton.setVisibility(View.GONE);
                 /*   if (authType != null && !authType.equalsIgnoreCase("")) {

                        if (authType.equalsIgnoreCase(AppConstant.FINGER)) {

                            openFingerPrintFragment();
                        } else if (authType.equalsIgnoreCase(AppConstant.IRIS)) {
                            openIrisFragment();

                        }
                    } else {
                        openFingerPrintFragment();
                    }*/
            } else if (aadharAuthMode.equalsIgnoreCase("B")) {
                manualRadioButton.setVisibility(View.VISIBLE);
                QrCodeRadioButton.setVisibility(View.VISIBLE);

                if (boigraphicMode != null && boigraphicMode.equalsIgnoreCase("I")) {
                    irisRadioButton.setVisibility(View.VISIBLE);
                    biometricRadioButton.setVisibility(View.GONE);
                    openIrisFragment();
                } else if (boigraphicMode != null && boigraphicMode.equalsIgnoreCase("F")) {
                    biometricRadioButton.setVisibility(View.VISIBLE);
                    irisRadioButton.setVisibility(View.GONE);
                    openFingerPrintFragment();
                }

/*

                    if (authType != null && !authType.equalsIgnoreCase("")) {

                        if (authType.equalsIgnoreCase(AppConstant.FINGER)) {
                            openFingerPrintFragment();
                        } else if (authType.equalsIgnoreCase(AppConstant.IRISH_AUTH)) {
                            openIrisFragment();
                        } else if (authType.equalsIgnoreCase(AppConstant.QRCODE)) {
                            openQrCodeFragment();
                        } else if (authType.equalsIgnoreCase(AppConstant.MANUALAUTH)) {
                            openManualFragment();
                        } else {
                            openQrCodeFragment();
                        }


                    } else {
                        openQrCodeFragment();
                    }
*/

            }
        } else {
            manualRadioButton.setVisibility(View.VISIBLE);
            irisRadioButton.setVisibility(View.VISIBLE);
            biometricRadioButton.setVisibility(View.VISIBLE);
            QrCodeRadioButton.setVisibility(View.VISIBLE);
            openQrCodeFragment();
        }
        //  }

    }


    private void setupScreenWithZoom() {
        mZoomLinearLayout = (LinearLayout) findViewById(R.id.mZoomLinearLayout);
        View v = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.activity_capture_aadhar_detail, null, false);
        v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
        settings = (ImageView) v.findViewById(R.id.settings);
        backLayout = (RelativeLayout) v.findViewById(R.id.backLayout);
        menuLayout = (RelativeLayout) v.findViewById(R.id.menuLayout);
        back = (ImageView) v.findViewById(R.id.back);
        backLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backLayout.performClick();
            }
        });
        aadharAuthRG = (RadioGroup) v.findViewById(R.id.aadharAuthRG);
        irisRadioButton = (RadioButton) v.findViewById(R.id.irisRadioButton);
        biometricRadioButton = (RadioButton) v.findViewById(R.id.biometricRadioButton);
        QrCodeRadioButton = (RadioButton) v.findViewById(R.id.QrCodeRadioButton);
        manualRadioButton = (RadioButton) v.findViewById(R.id.manualRadioButton);


        selectedMemItem = SelectedMemberItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.SELECTED_ITEM_FOR_VERIFICATION, context));
        if (selectedMemItem.getSeccMemberItem() != null) {
            seccItem = selectedMemItem.getSeccMemberItem();
        }

        dashboardDropdown();

        aadharAuthRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (checkedId == irisRadioButton.getId()) {
                    openIrisFragment();
                } else if (checkedId == biometricRadioButton.getId()) {


                    openFingerPrintFragment();
                } else if (checkedId == QrCodeRadioButton.getId()) {

                    openQrCodeFragment();
                } else if (checkedId == manualRadioButton.getId()) {
                    openManualFragment();

                }
            }
        });

        ZoomView zoomView = new ZoomView(this);
        zoomView.addView(v);
        mZoomLinearLayout.addView(zoomView);
    }

    private void setScreenWithoutZoom() {
        backLayout = (RelativeLayout) findViewById(R.id.backLayout);
        menuLayout = (RelativeLayout) findViewById(R.id.menuLayout);
        settings = (ImageView) findViewById(R.id.settings);

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
        menuLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        aadharAuthRG = (RadioGroup) findViewById(R.id.aadharAuthRG);
        irisRadioButton = (RadioButton) findViewById(R.id.irisRadioButton);
        biometricRadioButton = (RadioButton) findViewById(R.id.biometricRadioButton);
        QrCodeRadioButton = (RadioButton) findViewById(R.id.QrCodeRadioButton);
        manualRadioButton = (RadioButton) findViewById(R.id.manualRadioButton);


        selectedMemItem = SelectedMemberItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.SELECTED_ITEM_FOR_VERIFICATION, context));
        if (selectedMemItem.getSeccMemberItem() != null) {
            seccItem = selectedMemItem.getSeccMemberItem();
        }
      /*  String authType = ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.AUTHTYPESELECTED, context);

        if (seccItem != null && seccItem.getAadhaarCapturingMode() != null && !seccItem.getAadhaarCapturingMode().equalsIgnoreCase("")) {
            if (seccItem.getAadhaarCapturingMode().equalsIgnoreCase(AppConstant.IRIS_MODE)) {
                openIrisFragment();
            } else if (seccItem.getAadhaarCapturingMode().equalsIgnoreCase(AppConstant.FINGER_MODE)) {
                openFingerPrintFragment();
            } else if (seccItem.getAadhaarCapturingMode().equalsIgnoreCase(AppConstant.QR_CODE_MODE)) {
                openQrCodeFragment();
            } else if (seccItem.getAadhaarCapturingMode().equalsIgnoreCase(AppConstant.MANUAL_MODE)) {
                openManualFragment();
            }


        } else {
            if (aadharAuthMode != null && !aadharAuthMode.equalsIgnoreCase("")) {
                if (aadharAuthMode.equalsIgnoreCase("D")) {
                    manualRadioButton.setVisibility(View.VISIBLE);
                    irisRadioButton.setVisibility(View.GONE);
                    biometricRadioButton.setVisibility(View.GONE);
                    QrCodeRadioButton.setVisibility(View.VISIBLE);
                    if (authType != null && !authType.equalsIgnoreCase("")) {
                        if (authType.equalsIgnoreCase(AppConstant.QRCODE)) {
                            openQrCodeFragment();
                        } else if (authType.equalsIgnoreCase(AppConstant.MANUALAUTH)) {
                            openManualFragment();
                        }
                    } else {
                        openQrCodeFragment();
                    }

                } else if (aadharAuthMode.equalsIgnoreCase("E")) {
                    manualRadioButton.setVisibility(View.GONE);
                    irisRadioButton.setVisibility(View.VISIBLE);
                    biometricRadioButton.setVisibility(View.VISIBLE);
                    QrCodeRadioButton.setVisibility(View.GONE);
                    if (authType != null && !authType.equalsIgnoreCase("")) {

                        if (authType.equalsIgnoreCase(AppConstant.FINGER)) {

                            openFingerPrintFragment();
                        } else if (authType.equalsIgnoreCase(AppConstant.IRIS)) {
                            openIrisFragment();

                        }
                    } else {
                        openFingerPrintFragment();
                    }
                } else if (aadharAuthMode.equalsIgnoreCase("B")) {
                    manualRadioButton.setVisibility(View.VISIBLE);
                    irisRadioButton.setVisibility(View.VISIBLE);
                    biometricRadioButton.setVisibility(View.VISIBLE);
                    QrCodeRadioButton.setVisibility(View.VISIBLE);
                    if (authType != null && !authType.equalsIgnoreCase("")) {

                        if (authType.equalsIgnoreCase(AppConstant.FINGER)) {

                            openFingerPrintFragment();

                        } else if (authType.equalsIgnoreCase(AppConstant.IRISH_AUTH)) {
                            openIrisFragment();

                        } else if (authType.equalsIgnoreCase(AppConstant.QRCODE)) {
                            openQrCodeFragment();
                        } else if (authType.equalsIgnoreCase(AppConstant.MANUALAUTH)) {
                            openManualFragment();
                        } else {
                            openQrCodeFragment();
                        }

                    } else {
                        openQrCodeFragment();
                    }

                }
            } else {
                manualRadioButton.setVisibility(View.VISIBLE);
                irisRadioButton.setVisibility(View.VISIBLE);
                biometricRadioButton.setVisibility(View.VISIBLE);
                QrCodeRadioButton.setVisibility(View.VISIBLE);
                openQrCodeFragment();
            }
        }*/
        dashboardDropdown();

        aadharAuthRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {

                if (checkedId == irisRadioButton.getId()) {

                    openIrisFragment();

                } else if (checkedId == biometricRadioButton.getId()) {

                    openFingerPrintFragment();

                } else if (checkedId == QrCodeRadioButton.getId()) {

                    openQrCodeFragment();

                } else if (checkedId == manualRadioButton.getId()) {

                    openManualFragment();

                }
            }
        });
    }

    private void checkAppConfig() {
        StateItem selectedStateItem = StateItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SELECTED_STATE, context));
        ArrayList<ConfigurationItem> configList = SeccDatabase.findConfiguration(selectedStateItem.getStateCode(), context);

        if (configList != null) {
            for (ConfigurationItem item1 : configList) {
                if (item1.getConfigId().equalsIgnoreCase(AppConstant.AADHAR_AUTH)) {
                    aadharAuthMode = item1.getStatus();
                }

                if (item1.getConfigId().equalsIgnoreCase(AppConstant.APPLICATION_ZOOM)) {
                    zoomMode = item1.getStatus();
                }
                if (item1.getConfigId().equalsIgnoreCase(AppConstant.BIOGRAPHIC_AUTH)) {
                    boigraphicMode = item1.getStatus();
                }

            }
        }

    }

    private void openQrCodeFragment() {
        QrCodeRadioButton.setChecked(true);
        fragmentManager = getSupportFragmentManager();
        fragment = new AadharAuthQrCode();
        fragmentTransection = fragmentManager.beginTransaction();
        fragmentTransection = fragmentTransection.replace(R.id.frameContainer, fragment);
        fragmentTransection.commitAllowingStateLoss();
        ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.AUTHTYPESELECTED, AppConstant.QRCODE, context);
    }

    private void openManualFragment() {
        manualRadioButton.setChecked(true);
        fragmentManager = getSupportFragmentManager();
        fragment = new AadharAuthManualFragment();
        fragmentTransection = fragmentManager.beginTransaction();
        fragmentTransection = fragmentTransection.replace(R.id.frameContainer, fragment);
        fragmentTransection.commitAllowingStateLoss();
        ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.AUTHTYPESELECTED, AppConstant.MANUALAUTH, context);
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
     /*   biometricRadioButton.setChecked(true);
        fragmentManager = getSupportFragmentManager();
        fragment = new AadhaarFingerPrintViaRDSevices();
        fragmentTransection = fragmentManager.beginTransaction();
        fragmentTransection = fragmentTransection.replace(R.id.frameContainer, fragment);
        fragmentTransection.commitAllowingStateLoss();
        ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.AUTHTYPESELECTED, AppConstant.FINGER, context);*/
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
    }

    private void dashboardDropdown() {


        settings.setVisibility(View.VISIBLE);
       /* menuLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settings.performClick();
            }
        });*/
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

       /* menuLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settings.performClick();
            }
        });*/
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
                    wrongAttempetCountValue.setText((3 - wrongPinCount)+"");
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
                    // pinET.setHint("Enter 4-di");
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
    protected void onPause() {
        super.onPause();
        if (AppUtility.isAppIsInBackground(context)) {
            if (!pinLockIsShown) {
                askPinToLock();
            }
        }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 || requestCode == 2) {
            for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                if (fragment instanceof AadhaarFingerPrintViaRDSevices || fragment instanceof AadhaarIrisViaRDServices) {
                    fragment.onActivityResult(requestCode, resultCode, data);
                }
            }
        }
    }
}
