package com.nhpm.activity;

import android.app.AlertDialog;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
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
import android.widget.TextView;

import com.customComponent.CustomAlert;
import com.customComponent.utility.ProjectPrefrence;
import com.nhpm.Models.response.seccMembers.SeccMemberItem;
import com.nhpm.Models.response.seccMembers.SelectedMemberItem;
import com.nhpm.Models.response.verifier.VerifierLoginResponse;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;
import com.nhpm.Utility.AppUtility;
import com.github.pinball83.maskededittext.MaskedEditText;

/**
 * Created by Saurabh on 16-03-2017.
 */

public class GovermentIdCapturingNavigation extends BaseActivity implements ComponentCallbacks2 {

    private Context context;
    private boolean isValidMobile;
    private LinearLayout firstLayout, mobileNoLayout, EidCaptureLayout;
    private EditText captureMobileET;
    private MaskedEditText captureEidET, captureEidTimeStampET;
    private Button submitEid, submitMobileNumber, aadharEnrollNo, aadharEnrollYes;
    private Intent theIntent;
    private Button firstLayoutBackButton, mobileNoLayoutBackButton, EidCaptureLayoutBackButton;
    private SelectedMemberItem selectedMemItem;
    private SeccMemberItem seccMemberItem;
    private boolean pinLockIsShown = false;

    private int wrongPinCount = 0;
    private long wrongPinSavedTime;
    private long currentTime;
    private TextView wrongAttempetCountValue, wrongAttempetCountText;
    private long millisecond24 = 86400000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_govid_navigation);
        context = this;
        theIntent = new Intent(context, GovermentIDCaptureActivity.class);
        setUpScreen();
    }

    private void dashboardDropdown() {


        final ImageView settings = (ImageView) findViewById(R.id.settings);
        settings.setVisibility(View.VISIBLE);

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

    private void setUpScreen() {
        selectedMemItem = SelectedMemberItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.SELECTED_ITEM_FOR_VERIFICATION, context));
        if (selectedMemItem != null && selectedMemItem.getSeccMemberItem() != null) {
            seccMemberItem = selectedMemItem.getSeccMemberItem();
        }
        showNotification();
        firstLayoutBackButton = (Button) findViewById(R.id.firstLayoutBackButton);
        mobileNoLayoutBackButton = (Button) findViewById(R.id.mobileNoLayoutBackButton);
        EidCaptureLayoutBackButton = (Button) findViewById(R.id.EidCaptureLayoutBackButton);
        firstLayout = (LinearLayout) findViewById(R.id.firstLayout);
        mobileNoLayout = (LinearLayout) findViewById(R.id.mobileNoLayout);
        EidCaptureLayout = (LinearLayout) findViewById(R.id.EidCaptureLayout);
        //     MaskedEditText editText = new MaskedEditText(context, "8 (***) *** **-**", "*");
        //     captureEidET = (EditText) findViewById(R.id.captureEidET);
        captureEidTimeStampET = (MaskedEditText) findViewById(R.id.captureEidTimeStampET);
        captureEidET = (MaskedEditText) findViewById(R.id.captureEidET);
        captureMobileET = (EditText) findViewById(R.id.captureMobileET);
        //  MaskedEditText editText = (MaskedEditText) findViewById(R.id.my_edit_text);
        captureMobileET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() > 0) {
                    if (Integer.parseInt(charSequence.toString().substring(0, 1)) > 5) {
                        isValidMobile = true;
                        captureMobileET.setTextColor(AppUtility.getColor(context, R.color.black_shine));
                        if (charSequence.toString().length() == 10) {
                            captureMobileET.setTextColor(AppUtility.getColor(context, R.color.green));
                        }
                    } else {
                        isValidMobile = false;
                        captureMobileET.setTextColor(AppUtility.getColor(context, R.color.red));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        if (seccMemberItem != null) {
            if (seccMemberItem.getEid() != null && !seccMemberItem.getEid().equalsIgnoreCase("")) {
                if (seccMemberItem.getEid().length() > 10) {
                    String eid = seccMemberItem.getEid().substring(0, Math.min(seccMemberItem.getEid().length(), 16));
                    String timeStap = seccMemberItem.getEid().substring(16, Math.min(seccMemberItem.getEid().length(), 36));
                    timeStap = timeStap.replaceAll("/", "");
                    timeStap = timeStap.replaceAll(":", "");
                    timeStap = timeStap.replaceAll(" ", "");
                    System.out.print(timeStap);
                    captureEidET.setText(eid);
                    captureEidTimeStampET.setMaskedText(timeStap);
                } else {
                    captureMobileET.setText(seccMemberItem.getEid());
                }
            }
        }
        submitEid = (Button) findViewById(R.id.submitEid);
        submitMobileNumber = (Button) findViewById(R.id.submitMobileNumber);
        aadharEnrollNo = (Button) findViewById(R.id.aadharEnrollNo);
        aadharEnrollYes = (Button) findViewById(R.id.aadharEnrollYes);
        //  dashboardDropdown();
        landingLayout();
    }


    private void landingLayout() {
        firstLayout.setVisibility(View.VISIBLE);
        EidCaptureLayout.setVisibility(View.GONE);
        mobileNoLayout.setVisibility(View.GONE);

        firstLayoutBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent theintent = new Intent(context, WithAadhaarActivity.class);
                startActivity(theintent);
                finish();
            }
        });

        aadharEnrollYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eidCapture();
            }
        });
        aadharEnrollNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mobileCapture();
            }
        });
    }

    private void eidCapture() {

        EidCaptureLayout.setVisibility(View.VISIBLE);
        firstLayout.setVisibility(View.GONE);
        mobileNoLayout.setVisibility(View.GONE);
        EidCaptureLayoutBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                landingLayout();
            }
        });

        submitEid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder eid = new StringBuilder();
                eid.append(captureEidET.getText().toString()).append(captureEidTimeStampET.getText().toString());
                int count = eid.toString().length();
                System.out.print(count);
                int eidLength = (captureEidET.getUnmaskedText() + captureEidTimeStampET.getUnmaskedText()).length();
                if (!captureEidET.getUnmaskedText().equalsIgnoreCase("") && !captureEidTimeStampET.getUnmaskedText().equalsIgnoreCase("")) {
                    if (eidLength == 28) {
                        if (AppUtility.validateEidTimeStamp(eid.toString().trim())) {
                            seccMemberItem.setEid(eid.toString());
                            ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                                    AppConstant.SELECTED_ITEM_FOR_VERIFICATION, selectedMemItem.serialize(), context);
                            startActivity(theIntent);
                            finish();
                            leftTransition();
                        } else {
                            AppUtility.alertWithOk(context, context.getResources().getString(R.string.pleaseEnterValidTime));
                        }
                    } else {
                        AppUtility.alertWithOk(context, context.getResources().getString(R.string.pleaseEnterValidEid));
                    }
                } else {
                    AppUtility.alertWithOk(context, context.getResources().getString(R.string.pleaseEnterEid));
                }
            }
        });
    }

    private void mobileCapture() {

        EidCaptureLayout.setVisibility(View.GONE);
        firstLayout.setVisibility(View.GONE);
        mobileNoLayout.setVisibility(View.VISIBLE);

        mobileNoLayoutBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                landingLayout();
            }
        });


        submitMobileNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!captureMobileET.getText().toString().equalsIgnoreCase("")) {
                    if (isValidMobile && captureMobileET.getText().length() > 9) {
                        //  showPopUp();
                        seccMemberItem.setEid(captureMobileET.getText().toString());
                        ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                                AppConstant.SELECTED_ITEM_FOR_VERIFICATION, selectedMemItem.serialize(), context);
                        startActivity(theIntent);
                        finish();
                        leftTransition();
                    } else {
                        AppUtility.alertWithOk(context, context.getResources().getString(R.string.plzEnterValidMobNo));
                    }
                } else {
                    AppUtility.alertWithOk(context, context.getResources().getString(R.string.plzEnterMobNo));
                }

            }
        });
    }


    private void showPopUp() {
        AlertDialog internetDiaolg = new AlertDialog.Builder(context).create();
        LayoutInflater factory = LayoutInflater.from(context);
        View alertView = factory.inflate(R.layout.popup_givid_navigation, null);
        internetDiaolg.setView(alertView);
        internetDiaolg.show();
        final Button submit = (Button) alertView.findViewById(R.id.govIdYes);
        submit.setText(context.getResources().getString(R.string.Confirm_Submit));
        final Button cancelBT = (Button) alertView.findViewById(R.id.govIdNo);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(theIntent);
                finish();
                leftTransition();

            }
        });

        cancelBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(theIntent);
                finish();
                leftTransition();

            }
        });

    }


    @Override
    public void onBackPressed() {
        finish();
        //super.onBackPressed();
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


}

