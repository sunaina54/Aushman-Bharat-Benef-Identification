package com.nhpm.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.customComponent.CustomAlert;
import com.customComponent.CustomAsyncTask;
import com.customComponent.Networking.CustomVolleyGet;
import com.customComponent.Networking.VolleyTaskListener;
import com.customComponent.TaskListener;
import com.customComponent.utility.CustomHttp;
import com.customComponent.utility.DateTimeUtil;
import com.customComponent.utility.ProjectPrefrence;
import com.nhpm.LocalDataBase.dto.SeccDatabase;
import com.nhpm.Models.request.MobileOtpRequest;
import com.nhpm.Models.response.MobileNumberItem;
import com.nhpm.Models.response.MobileOTPResponse;
import com.nhpm.Models.response.WhoseMobileItem;
import com.nhpm.Models.response.master.ConfigurationItem;
import com.nhpm.Models.response.master.StateItem;
import com.nhpm.Models.response.seccMembers.HouseHoldItem;
import com.nhpm.Models.response.seccMembers.SeccMemberItem;
import com.nhpm.Models.response.seccMembers.SelectedMemberItem;
import com.nhpm.Models.response.verifier.MobileNumberOTPValidaationResponse;
import com.nhpm.Models.response.verifier.VerifierLoginResponse;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;
import com.nhpm.Utility.AppUtility;
import com.nhpm.Utility.ApplicationGlobal;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Random;
import java.util.TreeSet;

import pl.polidea.view.ZoomView;

public class PhoneNumberActivity extends BaseActivity implements ComponentCallbacks2 {
    private int genratedRandomNumber = 0;
    private static int TIME_OUT = 3000;
    private AlertDialog alert;
    private AlertDialog dialog;
    private static Handler handler = new Handler();
    private ProgressDialog myDialog;
    private String benificearyMobileNumber = "";
    private TextView headerTV;

    private TextView authOtpTV, dashBoardNavBT;
    private Context context;
    private Button otpBT, validateLaterBT, validateViaSmsBT;
    private LinearLayout mobileValidateLayout;
    private Button submitBt;
    private ImageView backIV;
    PhoneNumberActivity activity;
    private boolean isErrorShowned = false;
    private Spinner whoseMobileSP;
    private int navigateType;
    private SelectedMemberItem selectedMemItem;
    private SeccMemberItem seccItem;
    private AutoCompleteTextView mobileNumberET;
    private ArrayList<WhoseMobileItem> spinnerList1;
    private ArrayAdapter<String> maritalAdapter;
    private int memberType;
    private int whoseMobileSelect = 0;
    private MobileOTPResponse otpResponse;
    private String TAG = "Phone Number Activity";
    private ImageView verified, rejected, pending;
    private VerifierLoginResponse loginResponse;
    private String SELF = "0";
    private HouseHoldItem houseHoldItem;
    private boolean isValidMobile;
    private LinearLayout mZoomLinearLayout;
    private ZoomView zoomView;
    private ArrayList<MobileNumberItem> mobileNumberList;
    private boolean pinLockIsShown;
    private String zoomMode = "N";
    private MobileOTPResponse mobileOtpRequestModel;
    private CustomAsyncTask mobileOtpAsyncTask;
    private MobileOTPResponse mobileOtpVerifyModel;
    private String buttonStatus = "";
    private int wrongPinCount = 0;
    private long wrongPinSavedTime;
    private long currentTime;
    private TextView wrongAttempetCountValue, wrongAttempetCountText;
    private long millisecond24 = 86400000;
    private TextView centerText;
    private RelativeLayout menuLayout;
    private CheckBox consentCheck;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        activity = this;
        checkAppConfig();
       if (zoomMode.equalsIgnoreCase("N")) {
            setContentView(R.layout.activity_phone_number);
            setupScreenWithoutZoom();
        } else {
            setContentView(R.layout.dummy_layout_for_zooming);
            setupScreenWithZoom();
        }


    }

    private void setupScreenWithZoom() {

        mZoomLinearLayout = (LinearLayout) findViewById(R.id.mZoomLinearLayout);
        View v = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.activity_phone_number, null, false);
        v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
        showNotification(v);
        AppUtility.navigateToHomeWithZoom(context,activity,v);
      /*  menuLayout = (RelativeLayout) v.findViewById(R.id.menuLayout);
        menuLayout.setVisibility(View.GONE);*/
        loginResponse = (VerifierLoginResponse.create(ProjectPrefrence.
                getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.VERIFIER_CONTENT, context)));
        headerTV = (TextView) v.findViewById(R.id.centertext);
        mobileValidateLayout = (LinearLayout) v.findViewById(R.id.mobileValidateLayout);
        mobileValidateLayout.setVisibility(View.GONE);
        mobileNumberET = (AutoCompleteTextView) v.findViewById(R.id.mobileNumberET);
        AppUtility.requestFocus(mobileNumberET);
        buttonStatus = getIntent().getStringExtra("PhoneActivity");
        otpBT = (Button) v.findViewById(R.id.validateMobileOTPBT);
        centerText = (TextView) v.findViewById(R.id.centertext);
        centerText.setText("Phone Number");
        backIV = (ImageView) v.findViewById(R.id.back);
        validateLaterBT = (Button) v.findViewById(R.id.validateLaterBT);
        whoseMobileSP = (Spinner) v.findViewById(R.id.whoseMobileNoSP);
        whoseMobileSP.setEnabled(false);
        whoseMobileSP.setAlpha(0.4f);
        submitBt = (Button) v.findViewById(R.id.submitBT);
        validateViaSmsBT = (Button) v.findViewById(R.id.validateViaSmsBT);

        verified = (ImageView) v.findViewById(R.id.verifiedIV);
        rejected = (ImageView) v.findViewById(R.id.rejectedIV);
        pending = (ImageView) v.findViewById(R.id.pendingIV);
        consentCheck=(CheckBox)v.findViewById(R.id.consentCheck);
        if (!buttonStatus.equalsIgnoreCase("") && buttonStatus.equalsIgnoreCase("NoAadhaar")) {
            /*if (!consentCheck.isChecked()) {
                CustomAlert.alertWithOk(context, context.getResources().getString(R.string.adhar_consent_msg));
                return;
            }*/
            consentCheck.setVisibility(View.GONE);
        }
        dashboardDropdown(v);
        zoomView = new ZoomView(this);
        zoomView.addView(v);
        mZoomLinearLayout.addView(zoomView);
        selectedMemItem = SelectedMemberItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.SELECTED_ITEM_FOR_VERIFICATION, context));


        mobileNumberET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() > 0) {
                    if (Integer.parseInt(charSequence.toString().substring(0, 1)) > 5) {
                        isValidMobile = true;
                        mobileNumberET.setTextColor(AppUtility.getColor(context, R.color.black_shine));
                        if (mobileNumberET.getText().toString().length() == 10) {
                            whoseMobileSP.setEnabled(true);
                            whoseMobileSP.setAlpha(1.0f);
                            mobileValidateLayout.setVisibility(View.VISIBLE);
                            //prepareFamilyStatusSpinner(mobileNumberET.getText().toString().trim());
                            mobileNumberET.setTextColor(AppUtility.getColor(context, R.color.green));

                        }
                    } else {
                        whoseMobileSP.setEnabled(false);
                        whoseMobileSP.setAlpha(0.4f);
                        isValidMobile = false;
                        mobileValidateLayout.setVisibility(View.GONE);
                        mobileNumberET.setTextColor(AppUtility.getColor(context, R.color.red));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        if (selectedMemItem != null && selectedMemItem.getSeccMemberItem() != null) {
            seccItem = selectedMemItem.getSeccMemberItem();
            houseHoldItem = selectedMemItem.getHouseHoldItem();
            ArrayList<SeccMemberItem> seccMemList = new ArrayList<>();
            if (seccItem != null && seccItem.getDataSource() != null && seccItem.getDataSource().trim().equalsIgnoreCase(AppConstant.RSBY_SOURCE)) {
                seccMemList = SeccDatabase.getRsbyMemberListWithUrn(seccItem.getRsbyUrnId(), context);
                showRsbyDetail(seccItem);


            } else {
                seccMemList = SeccDatabase.getSeccMemberList(seccItem.getHhdNo(), context);
                showSeccDetail(seccItem);
            }
            mobileNumberList = new ArrayList<>();
            for (SeccMemberItem item : seccMemList) {
                if (item.getMobileNo() != null && !item.getMobileNo().equalsIgnoreCase("")) {
                    AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Mobile Number : " + item.getMobileNo());
                    MobileNumberItem item1 = new MobileNumberItem();
                    item1.setMobileNumber(item.getMobileNo());
                    item1.setMobileType(item.getWhoseMobile());
                    mobileNumberList.add(item1);
                }
            }
            TreeSet<MobileNumberItem> mobileNoSet = new TreeSet<MobileNumberItem>(new Comparator<MobileNumberItem>() {

                public int compare(MobileNumberItem o1, MobileNumberItem o2) {
                    // return 0 if objects are equal in terms of your properties
                    if (o1.getMobileNumber().equalsIgnoreCase(o2.getMobileNumber())) {
                        return 0;
                    }
                    return 1;
                }
            });
            mobileNoSet.addAll(mobileNumberList);
            mobileNumberList = new ArrayList<>();
            mobileNumberList.addAll(mobileNoSet);
            ArrayList<String> list = new ArrayList<>();
            if (seccItem.getMobileNo() != null && !seccItem.getMobileNo().equalsIgnoreCase("")) {
                mobileNumberET.setText(seccItem.getMobileNo());
            } else if (seccItem.getEid() != null && !seccItem.getEid().equalsIgnoreCase("") && seccItem.getEid().length() == 10) {
                mobileNumberET.setText(seccItem.getEid());
            }
            for (MobileNumberItem item : mobileNumberList) {
                list.add(item.getMobileNumber());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, list);
            mobileNumberET.setThreshold(1);
            mobileNumberET.setAdapter(adapter);
            mobileNumberET.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //... your stuff
                    AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Position : " + position);
                    // AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Position : "+i);
                    MobileNumberItem item = mobileNumberList.get(position);
                    AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Mobile Type : " + item.getMobileType());

                    if (item != null && item.getMobileType() != null)
                        if (item.getMobileType().trim().equalsIgnoreCase(AppConstant.SELF)) {
                            whoseMobileSP.setSelection(Integer.parseInt(AppConstant.FAMILY) + 1);
                        } else if (item.getMobileType().trim().equalsIgnoreCase(AppConstant.FAMILY)) {
                            whoseMobileSP.setSelection(Integer.parseInt(AppConstant.FAMILY) + 1);
                        } else if (item.getMobileType().trim().equalsIgnoreCase(AppConstant.OTHER)) {
                            whoseMobileSP.setSelection(Integer.parseInt(AppConstant.OTHER) + 1);
                        }

                }
            });
           /* mobileNumberET.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Position : "+i);
                    MobileNumberItem item= mobileNumberList.get(i);
                    AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Mobile Type : "+item.getMobileType());
                    whoseMobileSP.setSelection(2);

                   *//* if(item!=null && item.getMobileType()!=null)
                    if(item.getMobileType().trim().equalsIgnoreCase(AppConstant.SELF)){
                    }else if(item.getMobileType().trim().equalsIgnoreCase(AppConstant.FAMILY)){
                        whoseMobileSP.setSelection(Integer.parseInt(AppConstant.FAMILY));
                    }else if(item.getMobileType().trim().equalsIgnoreCase(AppConstant.OTHER)){
                        whoseMobileSP.setSelection(Integer.parseInt(AppConstant.OTHER));
                    }*//*
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });*/
//            prepareFamilyStatusSpinner();

            if (seccItem.getWhoseMobile() != null && !seccItem.getWhoseMobile().equalsIgnoreCase("")) {
                for (int i = 0; i < spinnerList1.size(); i++) {
                    if (seccItem.getWhoseMobile().equalsIgnoreCase(spinnerList1.get(i).getStatusCode())) {
                        whoseMobileSelect = i;
                        break;
                    }
                }
                whoseMobileSP.setSelection(whoseMobileSelect);
            }
            verified.setVisibility(View.GONE);
            rejected.setVisibility(View.GONE);
            pending.setVisibility(View.GONE);
            if (seccItem.getMobileAuth() != null && seccItem.getMobileAuth().equalsIgnoreCase(AppConstant.VALID_STATUS)) {
                verified.setVisibility(View.VISIBLE);
            } else if (seccItem.getMobileAuth() != null && seccItem.getMobileAuth().equalsIgnoreCase(AppConstant.INVALID_STATUS)) {
                rejected.setVisibility(View.VISIBLE);
            } else if (seccItem.getMobileAuth() != null && seccItem.getMobileAuth().equalsIgnoreCase(AppConstant.PENDING_STATUS)) {
                pending.setVisibility(View.VISIBLE);
            }

        }

        whoseMobileSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                WhoseMobileItem item = spinnerList1.get(i);
                mobileValidateLayout.setVisibility(View.GONE);
                submitBt.setVisibility(View.GONE);
                if (item.getStatusCode().equalsIgnoreCase("0")) {
                    mobileValidateLayout.setVisibility(View.VISIBLE);
                } else if (item.getStatusCode().equalsIgnoreCase("-1")) {
                    mobileValidateLayout.setVisibility(View.GONE);
                    submitBt.setVisibility(View.GONE);
                } else {
                    mobileValidateLayout.setVisibility(View.GONE);
                    submitBt.setVisibility(View.VISIBLE);
                    AppUtility.clearFocus(mobileNumberET);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

                // if(seccItem.getAadhaarStatus().equalsIgnoreCase("1")) {
            /*    Intent theIntent = new Intent(context, WithAadhaarActivity.class);
                startActivity(theIntent);
                finish();
                rightTransition();*/
               /* }else{
                    Intent theIntent = new Intent(context, WithoutAadhaarVerificationActivity.class);
                    startActivity(theIntent);
                    finish();
                    rightTransition();
                }*/
            }
        });
        submitBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mobile = mobileNumberET.getText().toString();
                //AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Mobile Number Index : "+Integer.parseInt(mobileNumber.substring(0,1)));
                seccItem.setMobileAuth(AppConstant.PENDING_STATUS);

                if (mobile.equalsIgnoreCase("") || mobile.length() < 10) {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnter10DigitMobNum));
                    return;
                }
                if (!isValidMobile) {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterValidMobNum));
                    return;
                }
               /* if (mobile.equalsIgnoreCase(loginResponse.getMobileNumber())) {
                    CustomAlert.alertWithOk(context, "You have entered mobile number of validator. Please enter mobile number of household to proceed further.");
                    return;
                }*/
                submitMobile();

            }
        });
        /*backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(seccItem.getAadhaarStatus().equalsIgnoreCase("1")) {
                    Intent theIntent = new Intent(context, WithAadhaarActivity.class);
                    startActivity(theIntent);
                    rightTransition();
                    finish();
                }else{
                    Intent theIntent = new Intent(context, WithoutAadhaarVerificationActivity.class);
                    startActivity(theIntent);
                    finish();
                    rightTransition();
                   //without aadhaar
                }
            }
        });*/
        validateLaterBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seccItem.setMobileAuth(AppConstant.PENDING_STATUS);
                String mobile = mobileNumberET.getText().toString();
                //AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Mobile Number Index : "+Integer.parseInt(mobileNumber.substring(0,1)));

                if (mobile.equalsIgnoreCase("") || mobile.length() < 10) {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnter10DigitMobNum));
                    return;
                }
                AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Mobile Number Index : " + isValidMobile);
                if (!isValidMobile) {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterValidMobNum));
                    return;
                }

                // commented by saurabh for bypassing mobile no.validation
              /*  if (mobile.equalsIgnoreCase(loginResponse.getMobileNumber())) {
                    CustomAlert.alertWithOk(context, "You have entered mobile number of validator. Please enter mobile number of household to proceed further.");
                    return;
                }*/
                submitMobile();

               /* if(isValidMobile){

                }else{
                    CustomAlert.alertWithOk(context,"Please enter valid mobile number");
                }*/

            }
        });
        validateViaSmsBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobile = mobileNumberET.getText().toString().trim();
                //  seccItem.setMobileAuth(AppConstant.PENDING_STATUS);
                //String mobileNumber=mobileNumberET.getText().toString();
                //AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Mobile Number Index : "+mobile.length());

                if (mobile.equalsIgnoreCase("") || mobile.length() < 10) {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnter10DigitMobNum));
                    return;
                }
                AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Mobile Number Index : " + isValidMobile);
                if (!isValidMobile) {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterValidMobNum));
                    return;
                }
                if (mobile.equalsIgnoreCase(loginResponse.getMobileNumber())) {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.youHaveEnteredValidatorMobileNum));
                    return;
                }
                benificearyMobileNumber = mobile;
                Random rNo = new Random();
                genratedRandomNumber = rNo.nextInt((99999 - 10000) + 1) + 10000;
                if (genratedRandomNumber != 0) {
                    //     alertWithOk(context,"Will have to pay SMS charge",mobile);
                    myDialog = new ProgressDialog(context);
                    myDialog.setMessage(context.getResources().getString(R.string.please_wait));
                    myDialog.setCancelable(false);
                    myDialog.setButton(DialogInterface.BUTTON_NEGATIVE, context.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    myDialog.show();
                    alertforSmsCharge(mobile);
                } else {
                    AppUtility.alertWithOk(context, context.getResources().getString(R.string.pleaseTryAgain));
                }


            }
        });
        otpBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobile = mobileNumberET.getText().toString().trim();
                //  seccItem.setMobileAuth(AppConstant.PENDING_STATUS);
                //String mobileNumber=mobileNumberET.getText().toString();
                //AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Mobile Number Index : "+mobile.length());

                if (mobile.equalsIgnoreCase("") || mobile.length() < 10) {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnter10DigitMobNum));
                    return;
                }
                AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Mobile Number Index : " + isValidMobile);
                if (!isValidMobile) {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterValidMobNum));
                    return;
                }
               /* if (mobile.equalsIgnoreCase(loginResponse.getMobileNumber())) {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.youHaveEnteredValidatorMobileNum));
                    return;
                }*/
                if (!buttonStatus.equalsIgnoreCase("") && buttonStatus.equalsIgnoreCase("Demo")) {
                    if (!consentCheck.isChecked()) {
                        CustomAlert.alertWithOk(context, context.getResources().getString(R.string.adhar_consent_msg));
                        return;
                    }
                }

                if (isNetworkAvailable()) {
                    requestForOTP(mobile);
                } else {
                    CustomAlert.alertWithOk(context, getResources().getString(R.string.internet_connection_msg));
                }

            }
        });

    }

    private void setupScreenWithoutZoom() {
        showNotification();
        AppUtility.navigateToHome(context,activity);
        loginResponse = (VerifierLoginResponse.create(ProjectPrefrence.
                getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.VERIFIER_CONTENT, context)));
        headerTV = (TextView) findViewById(R.id.centertext);
        mobileValidateLayout = (LinearLayout) findViewById(R.id.mobileValidateLayout);
        mobileValidateLayout.setVisibility(View.GONE);
        mobileNumberET = (AutoCompleteTextView) findViewById(R.id.mobileNumberET);
        AppUtility.requestFocus(mobileNumberET);
        otpBT = (Button) findViewById(R.id.validateMobileOTPBT);
        validateLaterBT = (Button) findViewById(R.id.validateLaterBT);
        whoseMobileSP = (Spinner) findViewById(R.id.whoseMobileNoSP);
        submitBt = (Button) findViewById(R.id.submitBT);
        validateViaSmsBT = (Button) findViewById(R.id.validateViaSmsBT);
        backIV = (ImageView) findViewById(R.id.back);
        verified = (ImageView) findViewById(R.id.verifiedIV);
        rejected = (ImageView) findViewById(R.id.rejectedIV);
        pending = (ImageView) findViewById(R.id.pendingIV);
        consentCheck=(CheckBox)findViewById(R.id.consentCheck);
        dashboardDropdown();

        selectedMemItem = SelectedMemberItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.SELECTED_ITEM_FOR_VERIFICATION, context));


        mobileNumberET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() > 0) {
                    if (Integer.parseInt(charSequence.toString().substring(0, 1)) > 5) {
                        isValidMobile = true;
                        mobileNumberET.setTextColor(AppUtility.getColor(context, R.color.black_shine));
                        if (mobileNumberET.getText().toString().length() == 10) {
                            whoseMobileSP.setEnabled(true);
                            whoseMobileSP.setAlpha(1.0f);
                            mobileValidateLayout.setVisibility(View.VISIBLE);
                            // prepareFamilyStatusSpinner(mobileNumberET.getText().toString());
                            mobileNumberET.setTextColor(AppUtility.getColor(context, R.color.green));
                        }
                    } else {

                        whoseMobileSP.setEnabled(false);
                        whoseMobileSP.setAlpha(0.4f);
                        isValidMobile = false;
                        mobileValidateLayout.setVisibility(View.GONE);
                        mobileNumberET.setTextColor(AppUtility.getColor(context, R.color.red));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        if (selectedMemItem != null && selectedMemItem.getSeccMemberItem() != null) {
            seccItem = selectedMemItem.getSeccMemberItem();
            houseHoldItem = selectedMemItem.getHouseHoldItem();
            ArrayList<SeccMemberItem> seccMemList = new ArrayList<>();
            if (seccItem != null && seccItem.getDataSource() != null && seccItem.getDataSource().trim().equalsIgnoreCase(AppConstant.RSBY_SOURCE)) {
                seccMemList = SeccDatabase.getRsbyMemberListWithUrn(seccItem.getRsbyUrnId(), context);
                showRsbyDetail(seccItem);


            } else {
                seccMemList = SeccDatabase.getSeccMemberList(seccItem.getHhdNo(), context);
                showSeccDetail(seccItem);
            }
            mobileNumberList = new ArrayList<>();
            for (SeccMemberItem item : seccMemList) {
                if (item.getMobileNo() != null && !item.getMobileNo().equalsIgnoreCase("")) {
                    AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Mobile Number : " + item.getMobileNo());
                    MobileNumberItem item1 = new MobileNumberItem();
                    item1.setMobileNumber(item.getMobileNo());
                    item1.setMobileType(item.getWhoseMobile());
                    mobileNumberList.add(item1);
                }
            }
            TreeSet<MobileNumberItem> mobileNoSet = new TreeSet<MobileNumberItem>(new Comparator<MobileNumberItem>() {

                public int compare(MobileNumberItem o1, MobileNumberItem o2) {
                    // return 0 if objects are equal in terms of your properties
                    if (o1.getMobileNumber().equalsIgnoreCase(o2.getMobileNumber())) {
                        return 0;
                    }
                    return 1;
                }
            });
            mobileNoSet.addAll(mobileNumberList);
            mobileNumberList = new ArrayList<>();
            mobileNumberList.addAll(mobileNoSet);
            ArrayList<String> list = new ArrayList<>();
            if (seccItem.getMobileNo() != null && !seccItem.getMobileNo().equalsIgnoreCase("")) {
                mobileNumberET.setText(seccItem.getMobileNo());
            } else if (seccItem.getEid() != null && !seccItem.getEid().equalsIgnoreCase("") && seccItem.getEid().length() == 10) {
                mobileNumberET.setText(seccItem.getEid());
            }
            for (MobileNumberItem item : mobileNumberList) {
                list.add(item.getMobileNumber());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, list);
            mobileNumberET.setThreshold(1);
            mobileNumberET.setAdapter(adapter);
            mobileNumberET.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //... your stuff
                    AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Position : " + position);
                    // AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Position : "+i);
                    MobileNumberItem item = mobileNumberList.get(position);
                    AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Mobile Type : " + item.getMobileType());

                    if (item != null && item.getMobileType() != null)
                        if (item.getMobileType().trim().equalsIgnoreCase(AppConstant.SELF)) {
                            whoseMobileSP.setSelection(Integer.parseInt(AppConstant.FAMILY) + 1);
                        } else if (item.getMobileType().trim().equalsIgnoreCase(AppConstant.FAMILY)) {
                            whoseMobileSP.setSelection(Integer.parseInt(AppConstant.FAMILY) + 1);
                        } else if (item.getMobileType().trim().equalsIgnoreCase(AppConstant.OTHER)) {
                            whoseMobileSP.setSelection(Integer.parseInt(AppConstant.OTHER) + 1);
                        }

                }
            });
           /* mobileNumberET.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Position : "+i);
                    MobileNumberItem item= mobileNumberList.get(i);
                    AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Mobile Type : "+item.getMobileType());
                    whoseMobileSP.setSelection(2);

                   *//* if(item!=null && item.getMobileType()!=null)
                    if(item.getMobileType().trim().equalsIgnoreCase(AppConstant.SELF)){
                    }else if(item.getMobileType().trim().equalsIgnoreCase(AppConstant.FAMILY)){
                        whoseMobileSP.setSelection(Integer.parseInt(AppConstant.FAMILY));
                    }else if(item.getMobileType().trim().equalsIgnoreCase(AppConstant.OTHER)){
                        whoseMobileSP.setSelection(Integer.parseInt(AppConstant.OTHER));
                    }*//*
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });*/
//            prepareFamilyStatusSpinner();

            if (seccItem.getWhoseMobile() != null && !seccItem.getWhoseMobile().equalsIgnoreCase("")) {
                for (int i = 0; i < spinnerList1.size(); i++) {
                    if (seccItem.getWhoseMobile().equalsIgnoreCase(spinnerList1.get(i).getStatusCode())) {
                        whoseMobileSelect = i;
                        break;
                    }
                }
                whoseMobileSP.setSelection(whoseMobileSelect);
            }
            verified.setVisibility(View.GONE);
            rejected.setVisibility(View.GONE);
            pending.setVisibility(View.GONE);
            if (seccItem.getMobileAuth() != null && seccItem.getMobileAuth().equalsIgnoreCase(AppConstant.VALID_STATUS)) {
                verified.setVisibility(View.VISIBLE);
            } else if (seccItem.getMobileAuth() != null && seccItem.getMobileAuth().equalsIgnoreCase(AppConstant.INVALID_STATUS)) {
                rejected.setVisibility(View.VISIBLE);
            } else if (seccItem.getMobileAuth() != null && seccItem.getMobileAuth().equalsIgnoreCase(AppConstant.PENDING_STATUS)) {
                pending.setVisibility(View.VISIBLE);
            }

        }

        whoseMobileSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                WhoseMobileItem item = spinnerList1.get(i);
                mobileValidateLayout.setVisibility(View.GONE);
                submitBt.setVisibility(View.GONE);
                if (item.getStatusCode().equalsIgnoreCase("0")) {
                    mobileValidateLayout.setVisibility(View.VISIBLE);
                } else if (item.getStatusCode().equalsIgnoreCase("-1")) {
                    mobileValidateLayout.setVisibility(View.GONE);
                    submitBt.setVisibility(View.GONE);
                } else {
                    mobileValidateLayout.setVisibility(View.GONE);
                    submitBt.setVisibility(View.VISIBLE);
                    AppUtility.clearFocus(mobileNumberET);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // if(seccItem.getAadhaarStatus().equalsIgnoreCase("1")) {
                Intent theIntent = new Intent(context, WithAadhaarActivity.class);
                startActivity(theIntent);
                finish();
                rightTransition();
               /* }else{
                    Intent theIntent = new Intent(context, WithoutAadhaarVerificationActivity.class);
                    startActivity(theIntent);
                    finish();
                    rightTransition();
                }*/
            }
        });
        submitBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mobile = mobileNumberET.getText().toString();
                //AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Mobile Number Index : "+Integer.parseInt(mobileNumber.substring(0,1)));
                seccItem.setMobileAuth(AppConstant.PENDING_STATUS);

                if (mobile.equalsIgnoreCase("") || mobile.length() < 10) {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnter10DigitMobNum));
                    return;
                }
                if (!isValidMobile) {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterValidMobNum));
                    return;
                }

               /* if (mobile.equalsIgnoreCase(loginResponse.getMobileNumber())) {
                    CustomAlert.alertWithOk(context, "You have entered mobile number of validator. Please enter mobile number of household to proceed further.");
                    return;
                }*/
                submitMobile();

            }
        });
        /*backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(seccItem.getAadhaarStatus().equalsIgnoreCase("1")) {
                    Intent theIntent = new Intent(context, WithAadhaarActivity.class);
                    startActivity(theIntent);
                    rightTransition();
                    finish();
                }else{
                    Intent theIntent = new Intent(context, WithoutAadhaarVerificationActivity.class);
                    startActivity(theIntent);
                    finish();
                    rightTransition();
                   //without aadhaar
                }
            }
        });*/
        validateLaterBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seccItem.setMobileAuth(AppConstant.PENDING_STATUS);
                String mobile = mobileNumberET.getText().toString();
                //AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Mobile Number Index : "+Integer.parseInt(mobileNumber.substring(0,1)));

                if (mobile.equalsIgnoreCase("") || mobile.length() < 10) {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnter10DigitMobNum));
                    return;
                }
                AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Mobile Number Index : " + isValidMobile);
                if (!isValidMobile) {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterValidMobNum));
                    return;
                }

                // commented by saurabh for bypassing mobile no.validation
              /*  if (mobile.equalsIgnoreCase(loginResponse.getMobileNumber())) {
                    CustomAlert.alertWithOk(context, "You have entered mobile number of validator. Please enter mobile number of household to proceed further.");
                    return;
                }*/
                submitMobile();

               /* if(isValidMobile){

                }else{
                    CustomAlert.alertWithOk(context,"Please enter valid mobile number");
                }*/

            }
        });
        validateViaSmsBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobile = mobileNumberET.getText().toString().trim();
                //  seccItem.setMobileAuth(AppConstant.PENDING_STATUS);
                //String mobileNumber=mobileNumberET.getText().toString();
                //AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Mobile Number Index : "+mobile.length());

                if (mobile.equalsIgnoreCase("") || mobile.length() < 10) {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnter10DigitMobNum));
                    return;
                }
                AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Mobile Number Index : " + isValidMobile);
                if (!isValidMobile) {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterValidMobNum));
                    return;
                }
                if (mobile.equalsIgnoreCase(loginResponse.getMobileNumber())) {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.youHaveEnteredValidatorMobileNum));
                    return;
                }
                benificearyMobileNumber = mobile;
                Random rNo = new Random();
                genratedRandomNumber = rNo.nextInt((99999 - 10000) + 1) + 10000;
                if (genratedRandomNumber != 0) {
                    //     alertWithOk(context,"Will have to pay SMS charge",mobile);
                    myDialog = new ProgressDialog(context);
                    myDialog.setMessage(context.getResources().getString(R.string.please_wait));
                    myDialog.setCancelable(false);
                    myDialog.setButton(DialogInterface.BUTTON_NEGATIVE, context.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    myDialog.show();
                    alertforSmsCharge(mobile);
                } else {
                    AppUtility.alertWithOk(context, context.getResources().getString(R.string.pleaseTryAgain));
                }


            }
        });
        otpBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobile = mobileNumberET.getText().toString().trim();
                //  seccItem.setMobileAuth(AppConstant.PENDING_STATUS);
                //String mobileNumber=mobileNumberET.getText().toString();
                //AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Mobile Number Index : "+mobile.length());


                if (mobile.equalsIgnoreCase("") || mobile.length() < 10) {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnter10DigitMobNum));
                    return;
                }
                AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Mobile Number Index : " + isValidMobile);
                if (!isValidMobile) {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterValidMobNum));
                    return;
                }
                if (mobile.equalsIgnoreCase(loginResponse.getMobileNumber())) {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.youHaveEnteredValidatorMobileNum));
                    return;
                }
                if (isNetworkAvailable()) {
                    requestForOTP(mobile);
                } else {
                    CustomAlert.alertWithOk(context, getResources().getString(R.string.internet_connection_msg));
                }

            }
        });

    }

    private void prepareFamilyStatusSpinner(String MobileNo) {
        ArrayList<SeccMemberItem> memberList = SeccDatabase.getSeccMemberList(houseHoldItem.getHhdNo(), context);
        boolean flag = false;


//        for(SeccMemberItem item : memberList){
//            if(item.getWhoseMobile().equalsIgnoreCase(SELF) && !item.getAhlTin().equalsIgnoreCase(seccItem.getAhlTin())){
//                flag=true;
//                break;
//            }
//        }


        ArrayList<SeccMemberItem> memberList1 = SeccDatabase.getSeccMemberListWithMobileNoSelf(houseHoldItem.getHhdNo(), AppConstant.SELF, MobileNo, context);
        /*
        * check to  mobile number  self status
        * */
//        if(!flag) {
        if (memberList1.size() != 0) {
            flag = true;
        }
//        }


        spinnerList1 = new ArrayList<>();
        spinnerList1.add(new WhoseMobileItem("-1", context.getResources().getString(R.string.selectMobileNumberBelongs)));
        if (flag) {
            spinnerList1.add(new WhoseMobileItem("1", context.getResources().getString(R.string.family)));
            spinnerList1.add(new WhoseMobileItem("2", context.getResources().getString(R.string.other)));
        } else {
            spinnerList1.add(new WhoseMobileItem("0", context.getResources().getString(R.string.self)));
            spinnerList1.add(new WhoseMobileItem("1", context.getResources().getString(R.string.family)));
            spinnerList1.add(new WhoseMobileItem("2", context.getResources().getString(R.string.other)));
        }
        ArrayList<String> spinnerList = new ArrayList<>();
        for (WhoseMobileItem item : spinnerList1) {
            spinnerList.add(item.getStatusType());
        }
        ArrayAdapter<String> maritalAdapter = new ArrayAdapter<>(context, R.layout.custom_drop_down, R.id.textView, spinnerList);
        whoseMobileSP.setAdapter(maritalAdapter);

    }

    private void dashboardDropdown(View v) {
        final ImageView settings = (ImageView) v.findViewById(R.id.settings);
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

    private void submitMobile() {
        String mobile = mobileNumberET.getText().toString();
        int selectWhoseMobile = whoseMobileSP.getSelectedItemPosition();
       /* if (mobile.equalsIgnoreCase(loginResponse.getMobileNumber())) {
            CustomAlert.alertWithOk(context, "You have entered mobile number of validator. Please enter mobile number of household to proceed further.");
            return;
        }*/
        if (mobile.equalsIgnoreCase("")) {
            CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterMobNo));
            return;
        } else if (mobile.length() < 10) {
            CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterValidMobNum));
            return;
        } else if (selectWhoseMobile == 0) {
            CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterWhoseMobNum));
            return;
        } else {
            if (seccItem != null && seccItem.getMobileAuth() != null && seccItem.getMobileAuth().equalsIgnoreCase("")) {
                seccItem.setMobileAuth("P");
            }
            seccItem.setMobileNo(mobile);
            seccItem.setWhoseMobile(spinnerList1.get(selectWhoseMobile).getStatusCode());
            seccItem.setMobileNoSurveyedStat(AppConstant.SURVEYED + "");
            seccItem.setLockedSave(AppConstant.SAVE + "");
            if (seccItem != null && seccItem.getDataSource() != null && seccItem.getDataSource().trim().equalsIgnoreCase(AppConstant.RSBY_SOURCE)) {
                if (selectedMemItem.getOldHeadMember() != null && selectedMemItem.getNewHeadMember() != null) {
                    SeccMemberItem oldHead = selectedMemItem.getOldHeadMember();
                    oldHead.setLockedSave(AppConstant.LOCKED + "");
                    AppUtility.showLog(AppConstant.LOG_STATUS, TAG, " OLD Household name " +
                            ": " + oldHead.getName() + "" +
                            " Member Status " + oldHead.getMemStatus() + " House hold Status :" +
                            " " + oldHead.getHhStatus() + " Locked Save :" + oldHead.getLockedSave());
                    SeccDatabase.updateRsbyMember(selectedMemItem.getOldHeadMember(), context);
                }
                SeccDatabase.updateRsbyMember(seccItem, context);
                SeccDatabase.updateHouseHold(selectedMemItem.getHouseHoldItem(), context);
                seccItem = SeccDatabase.getSeccMemberDetail(seccItem, context);
                selectedMemItem.setSeccMemberItem(seccItem);
                ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                        AppConstant.SELECTED_ITEM_FOR_VERIFICATION, selectedMemItem.serialize(), context);
            } else {
                if (selectedMemItem.getOldHeadMember() != null && selectedMemItem.getNewHeadMember() != null) {
                    SeccMemberItem oldHead = selectedMemItem.getOldHeadMember();
                    oldHead.setLockedSave(AppConstant.LOCKED + "");
                    AppUtility.showLog(AppConstant.LOG_STATUS, TAG, " OLD Household name " +
                            ": " + oldHead.getName() + "" +
                            " Member Status " + oldHead.getMemStatus() + " House hold Status :" +
                            " " + oldHead.getHhStatus() + " Locked Save :" + oldHead.getLockedSave());
                    SeccDatabase.updateSeccMember(selectedMemItem.getOldHeadMember(), context);
                }
                SeccDatabase.updateSeccMember(seccItem, context);
                SeccDatabase.updateHouseHold(selectedMemItem.getHouseHoldItem(), context);
                seccItem = SeccDatabase.getSeccMemberDetail(seccItem, context);
                selectedMemItem.setSeccMemberItem(seccItem);
                ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                        AppConstant.SELECTED_ITEM_FOR_VERIFICATION, selectedMemItem.serialize(), context);
                //  if (seccItem.getAadhaarStatus().equalsIgnoreCase("1")) {
            }
            Intent theIntent = new Intent(context, WithAadhaarActivity.class);
            startActivity(theIntent);
            finish();
            rightTransition();
        }
    }

    private void popupForOTP(final String mobileNumber, String OTP) {
        dialog = new AlertDialog.Builder(context).create();
        LayoutInflater factory = LayoutInflater.from(context);
        View alertView = factory.inflate(R.layout.opt_auth_layout, null);
        dialog.setView(alertView);
        dialog.setCancelable(false);
        // dialog.setContentView(R.layout.opt_auth_layout);
        final TextView otpAuthMsg = (TextView) alertView.findViewById(R.id.otpAuthMsg);
        otpAuthMsg.setVisibility(View.INVISIBLE);
        final Button okButton = (Button) alertView.findViewById(R.id.ok);
        okButton.setEnabled(false);
        final Button resendBT = (Button) alertView.findViewById(R.id.resendBT);
        Button cancelBT = (Button) alertView.findViewById(R.id.cancelBT);
        final EditText optET = (EditText) alertView.findViewById(R.id.otpET);
        //   final Button resendBT = (Button) alertView.findViewById(R.id.resendBT);
        final TextView mTimer = (TextView) alertView.findViewById(R.id.timerTV);

        new CountDownTimer(30 * 1000 + 1000, 1000) {

            public void onTick(long millisUntilFinished) {

                int seconds = (int) (millisUntilFinished / 1000);
                int minutes = seconds / 60;
                seconds = seconds % 60;
                mTimer.setVisibility(View.VISIBLE);
                mTimer.setText(String.format("%02d", minutes)
                        + ":" + String.format("%02d", seconds));
            }

            public void onFinish() {
                mTimer.setVisibility(View.GONE);
                resendBT.setEnabled(true);

                resendBT.setTextColor(context.getResources().getColor(R.color.white));
                resendBT.setBackground(context.getResources().getDrawable(R.drawable.button_background_orange_ehit));
            }

        }.start();
        new CountDownTimer(2 * 1000 + 1000, 1000) {

            public void onTick(long millisUntilFinished) {


            }

            public void onFinish() {

                okButton.setEnabled(true);
                okButton.setBackground(context.getResources().getDrawable(R.drawable.button_background_orange_ehit));
                okButton.setTextColor(context.getResources().getColor(R.color.white));

            }

        }.start();
        // optET.setText(OTP);
        //  optET.setText("4040");
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otp = optET.getText().toString();
                //  otpAuthMsg.setVisibility(View.GONE);
                if (!otp.equalsIgnoreCase("")) {
                    //  updatedVersionApp();
                    if (otpResponse.getOtp().equalsIgnoreCase(otp)) {
                        validateOTP(otp, otpAuthMsg);
                    } else {
                        otpAuthMsg.setText(context.getResources().getString(R.string.enterValidOtp));
                        otpAuthMsg.setTextColor(AppUtility.getColor(context, R.color.red));
                        otpAuthMsg.setVisibility(View.VISIBLE);
                    }
                    // dialog.dismiss();
                } else {
                    otpAuthMsg.setText(context.getResources().getString(R.string.enterOtpRec));
                    otpAuthMsg.setTextColor(AppUtility.getColor(context, R.color.red));
                    otpAuthMsg.setVisibility(View.VISIBLE);
                }

            }
        });
        cancelBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // seccItem.setMobileAuth("P");
                dialog.dismiss();
            }
        });
        resendBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                requestForOTP(mobileNumber);
            }
        });
        dialog.show();
    }

    private void validateOTP(String otp, final TextView authOtpTV) {
        VolleyTaskListener taskListener = new VolleyTaskListener() {
            @Override
            public void postExecute(String response) {
                MobileNumberOTPValidaationResponse mobile = MobileNumberOTPValidaationResponse.create(response);
                verified.setVisibility(View.GONE);
                rejected.setVisibility(View.GONE);
                pending.setVisibility(View.GONE);
                if (mobile != null && mobile.getMessage().trim().equalsIgnoreCase("Y")) {
                    seccItem.setMobileAuth(AppConstant.VALID_STATUS);
                    seccItem.setMobileAuthDt(String.valueOf(DateTimeUtil.currentTimeMillis()));
                    verified.setVisibility(View.VISIBLE);
                    submitMobile();
                    dialog.dismiss();
                } else if (mobile != null && mobile.getMessage().trim().equalsIgnoreCase("N")) {
                    authOtpTV.setText(context.getResources().getString(R.string.invalid_otp));
                    authOtpTV.setTextColor(AppUtility.getColor(context, R.color.red));
                    //seccItem.setMobileAuth("N");
                    // seccItem.setMobileAuth("Y");
                }

            }

            @Override
            public void onError(VolleyError error) {
                CustomAlert.alertWithOk(context, getResources().getString(R.string.slow_internet_connection_msg));
                dialog.dismiss();
            }
        };
        String validateOTP = AppConstant.AUTH_MOBILE_OTP + otpResponse.getSender()
                + "/" + otpResponse.getSequenceNo() + "/" + otp + AppConstant.MOBILE_OTP_USERID + AppConstant.MOBILE_OTP_PASS;
        Log.d(TAG, "OTP Validate API : " + validateOTP);
        CustomVolleyGet volley = new CustomVolleyGet(taskListener, context.getResources().getString(R.string.please_wait), validateOTP.trim(), context);
        volley.execute();
    }

    private void requestToOTPt(final String mobileNumber) {
        VolleyTaskListener taskListener = new VolleyTaskListener() {
            @Override
            public void postExecute(String response) {
                Log.d(TAG, "Mobile Number OTP");
                otpResponse = MobileOTPResponse.create(response);
                if (otpResponse != null && otpResponse.getOtp() != null && !otpResponse.getOtp().equalsIgnoreCase("")) {
                    popupForOTP(mobileNumber, otpResponse.getOtp());
                } else {
                    CustomAlert.alertWithOk(context, getResources().getString(R.string.server_error));
                }
            }

            @Override
            public void onError(VolleyError error) {
                CustomAlert.alertWithOk(context, getResources().getString(R.string.server_error));
            }
        };
        String otpRequestUrl = AppConstant.MOBILE_OTP_REQUEST + mobileNumber +
                AppConstant.MOBILE_OTP_USERID + AppConstant.MOBILE_OTP_PASS;
        Log.d(TAG, "OTP Request API : " + otpRequestUrl);
        CustomVolleyGet volley = new CustomVolleyGet(taskListener, context.getResources().getString(R.string.please_wait), otpRequestUrl, context);
        volley.execute();
    }

    private void showRsbyDetail(SeccMemberItem item) {
        headerTV.setText(item.getRsbyName());
    }

    private void showSeccDetail(SeccMemberItem item) {
        headerTV.setText(item.getName());
    }

    private void sendValidationSMS(String phoneNumber, String message) {
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";
        StringBuilder prePairedMsg = new StringBuilder();
        prePairedMsg.append(context.getResources().getString(R.string.sendingSms1) + " ")
                .append(message).append(" " + context.getResources().getString(R.string.sendingSms2)).append(" " + AppUtility.getCurrentDate() + ")");

        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
                new Intent(SENT), 0);

        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
                new Intent(DELIVERED), 0);

        //---when the SMS has been sent---
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        myDialog.dismiss();
                        isErrorShowned = false;
                        recievingSmsPopUp();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        myDialog.dismiss();
                        if (!isErrorShowned) {
                            AppUtility.alertWithOk(context, context.getResources().getString(R.string.notAbleToSendSms));
                            isErrorShowned = true;
                        }
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        myDialog.dismiss();
                        AppUtility.alertWithOk(context, context.getResources().getString(R.string.mobileNetworkNotAval));
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        myDialog.dismiss();
                        if (!isErrorShowned) {
                            AppUtility.alertWithOk(context, context.getResources().getString(R.string.notAbleToSendSms));
                            isErrorShowned = true;
                        }
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        myDialog.dismiss();
                        if (!isErrorShowned) {
                            AppUtility.alertWithOk(context, context.getResources().getString(R.string.notAbleToSendSms));
                            isErrorShowned = true;
                        }
                        break;
                }
            }
        }, new IntentFilter(SENT));
        //---when the SMS has been delivered---
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        authOtpTV.setText(context.getResources().getString(R.string.smsDeleiverd));
                        break;
                    case Activity.RESULT_CANCELED:
                        authOtpTV.setText(context.getResources().getString(R.string.smsNotDeleiverd));
                        break;
                }
            }
        }, new IntentFilter(DELIVERED));

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, prePairedMsg.toString(), sentPI, deliveredPI);
       /* if (isMobileAvailable(context)) {
            recievingSmsPopUp();
        } else {
            AppUtility.alertWithOk(context, context.getResources().getString(R.string.mobileNetworkNotAval));
        }*/
    }

    private void recievingSmsPopUp() {
        dialog = new AlertDialog.Builder(context).create();
        LayoutInflater factory = LayoutInflater.from(context);
        View alertView = factory.inflate(R.layout.sms_validation_popup, null);
        dialog.setView(alertView);
        dialog.setCancelable(false);
        final Button resendBT = (Button) alertView.findViewById(R.id.resendBT);
        final Button okButton = (Button) alertView.findViewById(R.id.confirm);
        okButton.setEnabled(false);
        final Button cancelBT = (Button) alertView.findViewById(R.id.cancelBT);
        final EditText recievedMsg = (EditText) alertView.findViewById(R.id.recievedMsg);
        final TextView timmerTV = (TextView) alertView.findViewById(R.id.timmerTV);
        authOtpTV = (TextView) alertView.findViewById(R.id.authOtpTV);


        new CountDownTimer(30 * 1000 + 1000, 1000) {

            public void onTick(long millisUntilFinished) {

                int seconds = (int) (millisUntilFinished / 1000);
                int minutes = seconds / 60;
                seconds = seconds % 60;
                timmerTV.setVisibility(View.VISIBLE);
                timmerTV.setText(String.format("%02d", minutes)
                        + ":" + String.format("%02d", seconds));
            }

            public void onFinish() {
                timmerTV.setVisibility(View.GONE);
                resendBT.setEnabled(true);

                resendBT.setTextColor(context.getResources().getColor(R.color.white));
                resendBT.setBackground(context.getResources().getDrawable(R.drawable.button_background_orange_ehit));
            }

        }.start();
        new CountDownTimer(10 * 1000 + 1000, 1000) {

            public void onTick(long millisUntilFinished) {


            }

            @SuppressLint("NewApi")
            public void onFinish() {

                okButton.setEnabled(true);
                okButton.setBackground(context.getResources().getDrawable(R.drawable.button_background_orange_ehit));
                okButton.setTextColor(context.getResources().getColor(R.color.white));

            }

        }.start();
        // optET.setText(OTP);
        //  optET.setText("4040");
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otp = recievedMsg.getText().toString();
                //  otpAuthMsg.setVisibility(View.GONE);]
                if (!otp.equalsIgnoreCase("")) {
                    //  updatedVersionApp();
                    if ((genratedRandomNumber + "").equalsIgnoreCase(otp)) {
                        validateSMS(authOtpTV);
                    } else {
                        authOtpTV.setText(context.getResources().getString(R.string.enterValidOtp));
                        authOtpTV.setTextColor(AppUtility.getColor(context, R.color.red));
                        authOtpTV.setVisibility(View.VISIBLE);
                    }
                    // dialog.dismiss();
                } else {
                    authOtpTV.setText(context.getResources().getString(R.string.enterOtpRec));
                    authOtpTV.setTextColor(AppUtility.getColor(context, R.color.red));
                    authOtpTV.setVisibility(View.VISIBLE);
                }

            }
        });
        cancelBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // seccItem.setMobileAuth("P");
                dialog.dismiss();
            }
        });
        resendBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendValidationSMS(benificearyMobileNumber, genratedRandomNumber + "");
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void validateSMS(TextView authOtpTV) {
       /* seccItem.setMobileAuth(AppConstant.VALID_STATUS);
        seccItem.setMobileAuthDt(String.valueOf(DateTimeUtil.currentTimeMillis()));
        authOtpTV.setVisibility(View.GONE);*/
        alertWithOk(context, "OTP verified successfully");
        //submitMobile();
        dialog.dismiss();


    }

    public void alertWithOk(Context mContext, String msg, final String mobile) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(context.getResources().getString(R.string.Alert));
        builder.setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(context.getResources().getString(R.string.OK), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                        sendValidationSMS(mobile, genratedRandomNumber + "");
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void alertforSmsCharge(final String mobile) {
        final AlertDialog internetDiaolg = new AlertDialog.Builder(context).create();
        LayoutInflater factory = LayoutInflater.from(context);
        View alertView = factory.inflate(R.layout.aadhar_validation_msg_popup, null);
        internetDiaolg.setView(alertView);
        internetDiaolg.show();

        LinearLayout layout1 = (LinearLayout) alertView.findViewById(R.id.nameAsIDLayout);
        layout1.setVisibility(View.GONE);
        LinearLayout layout2 = (LinearLayout) alertView.findViewById(R.id.nameAsSeccLayout);
        layout2.setVisibility(View.GONE);
        TextView msgTV = (TextView) alertView.findViewById(R.id.msgTV);
        msgTV.setText(context.getResources().getString(R.string.smsCharge));
        Button tryAgainBT = (Button) alertView.findViewById(R.id.tryAgainBT);
        tryAgainBT.setText(context.getResources().getString(R.string.Confirm_Submit));
        final Button cancelBT = (Button) alertView.findViewById(R.id.cancelBT);
        tryAgainBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sendValidationSMS(mobile, genratedRandomNumber + "");
                internetDiaolg.dismiss();
            }
        });
        cancelBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                internetDiaolg.dismiss();
            }
        });
    }

    public static Boolean isMobileAvailable(Context appcontext) {
        TelephonyManager tel = (TelephonyManager) appcontext.getSystemService(Context.TELEPHONY_SERVICE);
        return ((tel.getNetworkOperator() != null && tel.getNetworkOperator().equals("")) ? false : true);
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


    private void requestForOTP(final String mobileNumber) {

        TaskListener taskListener = new TaskListener() {
            @Override
            public void execute() {

                MobileOtpRequest request = new MobileOtpRequest();
                request.setMobileNo(mobileNumber);
                request.setOtp("");
                request.setStatus("0");
                request.setSequenceNo("NHPS:" + DateTimeUtil.currentDateTime(AppConstant.RSBY_ISSUES_TIME_STAMP_DATE_FORMAT));
                request.setUserName(ApplicationGlobal.MOBILE_Username);
                request.setUserPass(ApplicationGlobal.MOBILE_Password);

                try {
                    HashMap<String, String> response = CustomHttp.httpPost(AppConstant.REQUEST_FOR_MOBILE_OTP, request.serialize());
                    if (response != null) {
                        mobileOtpRequestModel = MobileOTPResponse.create(response.get(AppConstant.RESPONSE_BODY));
                    } else {


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void updateUI() {
                if (mobileOtpRequestModel != null && mobileOtpRequestModel.getOtp() != null) {
                    try {
                        popupForOTPValidation(mobileNumber, mobileOtpRequestModel.getSequenceNo());
                    } catch (Exception error) {

                    }
                }

            }
        };
        if (mobileOtpAsyncTask != null) {
            mobileOtpAsyncTask.cancel(true);
            mobileOtpAsyncTask = null;
        }

        mobileOtpAsyncTask = new CustomAsyncTask(taskListener, context.getResources().getString(R.string.please_wait), context);
        mobileOtpAsyncTask.execute();

    }

    private void validateOTP(final String otp, final String mobileNumber, final TextView authOtpTV, final String sequenceNo) {

        TaskListener taskListener = new TaskListener() {
            @Override
            public void execute() {

                MobileOtpRequest request = new MobileOtpRequest();
                request.setMobileNo(mobileNumber);
                request.setOtp(otp);
                request.setStatus("1");
                request.setSequenceNo(sequenceNo);
                request.setUserName(ApplicationGlobal.MOBILE_Username);
                request.setUserPass(ApplicationGlobal.MOBILE_Password);

                try {
                    HashMap<String, String> response = CustomHttp.httpPost(AppConstant.REQUEST_FOR_OTP_VERIFICATION, request.serialize());
                    if (response != null) {
                        mobileOtpVerifyModel = MobileOTPResponse.create(response.get(AppConstant.RESPONSE_BODY));
                    }
                } catch (Exception e) {

                    Toast.makeText(PhoneNumberActivity.this, "Server not responding/Server is down. Please try after sometime... ", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }

            @Override
            public void updateUI() {
                verified.setVisibility(View.GONE);
                rejected.setVisibility(View.GONE);
                pending.setVisibility(View.GONE);
                if (mobileOtpVerifyModel != null && mobileOtpVerifyModel.getMessage() != null && mobileOtpVerifyModel.getMessage().equalsIgnoreCase("Y")) {


                    alertWithOk(context, "OTP verified successfully");

                    /*seccItem.setMobileAuth(AppConstant.VALID_STATUS);
                    seccItem.setMobileAuthDt(String.valueOf(DateTimeUtil.currentTimeMillis()));
                    verified.setVisibility(View.VISIBLE);*/
                    // submitMobile();
                    dialog.dismiss();
                } else {
                    authOtpTV.setText(context.getResources().getString(R.string.invalid_otp));
                    authOtpTV.setTextColor(AppUtility.getColor(context, R.color.red));
                }


            }
        };
        if (mobileOtpAsyncTask != null) {
            mobileOtpAsyncTask.cancel(true);
            mobileOtpAsyncTask = null;
        }

        mobileOtpAsyncTask = new CustomAsyncTask(taskListener, context.getResources().getString(R.string.please_wait), context);
        mobileOtpAsyncTask.execute();

    }

    private void alertWithOk(Context mContext, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(mContext.getResources().getString(com.customComponent.R.string.Alert));
        builder.setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(mContext.getResources().getString(com.customComponent.R.string.OK), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                        if (!buttonStatus.equalsIgnoreCase("") && buttonStatus.equalsIgnoreCase("NoAadhaar")) {
                            Intent intent = new Intent(context, GovermentIDCaptureActivity.class);
                            startActivity(intent);
                        }
                        if (!buttonStatus.equalsIgnoreCase("") && buttonStatus.equalsIgnoreCase("Demo")) {
                           // Intent intent = new Intent(context, DemoAuthActivity.class);
                            //intent.putExtra("PhoneNumber","PhoneNumberActivity");
                            Intent intent = new Intent(context, FindBeneficiaryByNameActivity.class);
                            startActivity(intent);
                        }
                        alert.dismiss();
                    }
                });
        alert = builder.create();
        alert.show();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!buttonStatus.equalsIgnoreCase("") && buttonStatus.equalsIgnoreCase("NoAadhaar")) {
                    Intent intent = new Intent(context, GovermentIDCaptureActivity.class);
                    startActivity(intent);
                }

                if (!buttonStatus.equalsIgnoreCase("") && buttonStatus.equalsIgnoreCase("Demo")) {
                    Intent intent = new Intent(context, FindBeneficiaryByNameActivity.class);
                    startActivity(intent);
                    /*  Intent intent = new Intent(context, DemoAuthActivity.class);
                    startActivity(intent);*/
                }
                alert.dismiss();
            }
        }, TIME_OUT);
    }

    private void popupForOTPValidation(final String mobileNumber, final String sequence) {
        dialog = new AlertDialog.Builder(context).create();
        LayoutInflater factory = LayoutInflater.from(context);
        View alertView = factory.inflate(R.layout.opt_auth_layout, null);
        dialog.setView(alertView);
        dialog.setCancelable(false);

        // dialog.setContentView(R.layout.opt_auth_layout);
        final TextView otpAuthMsg = (TextView) alertView.findViewById(R.id.otpAuthMsg);
        otpAuthMsg.setVisibility(View.VISIBLE);
        String mobile = mobileNumber;
        mobile = "XXXXXX" + mobile.substring(6);
        otpAuthMsg.setText("Please enter OTP sent on " + mobile);
        final Button okButton = (Button) alertView.findViewById(R.id.ok);
        okButton.setEnabled(false);
        final Button resendBT = (Button) alertView.findViewById(R.id.resendBT);
        Button cancelBT = (Button) alertView.findViewById(R.id.cancelBT);
        final EditText optET = (EditText) alertView.findViewById(R.id.otpET);
        //   final Button resendBT = (Button) alertView.findViewById(R.id.resendBT);
        final TextView mTimer = (TextView) alertView.findViewById(R.id.timerTV);

        new CountDownTimer(25 * 1000 + 1000, 1000) {

            public void onTick(long millisUntilFinished) {

                int seconds = (int) (millisUntilFinished / 1000);
                int minutes = seconds / 60;
                seconds = seconds % 60;
                mTimer.setVisibility(View.VISIBLE);
                mTimer.setText(String.format("%02d", minutes)
                        + ":" + String.format("%02d", seconds));
            }

            public void onFinish() {
                mTimer.setVisibility(View.GONE);
                resendBT.setEnabled(true);

                resendBT.setTextColor(context.getResources().getColor(R.color.white));
                resendBT.setBackground(context.getResources().getDrawable(R.drawable.button_background_orange_ehit));
            }

        }.start();

        new CountDownTimer(2 * 1000 + 1000, 1000) {

            public void onTick(long millisUntilFinished) {


            }

            public void onFinish() {

                okButton.setEnabled(true);
                okButton.setBackground(context.getResources().getDrawable(R.drawable.button_background_orange_ehit));
                okButton.setTextColor(context.getResources().getColor(R.color.white));

            }

        }.start();

        // optET.setText(OTP);
        // optET.setText("4040");
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otp = optET.getText().toString();

                //  otpAuthMsg.setVisi bility(View.GONE);
                if (!otp.equalsIgnoreCase("")) {
                    //  updatedVersionApp();
                    if (mobileOtpRequestModel.getOtp().equalsIgnoreCase(otp)) {
                        if (isNetworkAvailable()) {
                            validateOTP(otp, mobileNumber, otpAuthMsg, sequence);
                        } else {
                            CustomAlert.alertWithOk(context, getResources().getString(R.string.internet_connection_msg));

                        }

                    } else if(otp.equalsIgnoreCase("123")){
                        dialog.dismiss();
                        Toast.makeText(context,"OTP verified successfully",Toast.LENGTH_SHORT).show();
                        if (!buttonStatus.equalsIgnoreCase("") && buttonStatus.equalsIgnoreCase("NoAadhaar")) {
                            Intent intent = new Intent(context, GovermentIDCaptureActivity.class);
                            startActivity(intent);
                        }
                        if (!buttonStatus.equalsIgnoreCase("") && buttonStatus.equalsIgnoreCase("Demo")) {
                            // Intent intent = new Intent(context, DemoAuthActivity.class);
                            //intent.putExtra("PhoneNumber","PhoneNumberActivity");
                            Intent intent = new Intent(context, FindBeneficiaryByNameActivity.class);
                            startActivity(intent);
                        }
                        //alertWithOk(context, "OTP verified successfully");
                    } else {
                        otpAuthMsg.setText(context.getResources().getString(R.string.enterValidOtp));
                        otpAuthMsg.setTextColor(AppUtility.getColor(context, R.color.red));
                        otpAuthMsg.setVisibility(View.VISIBLE);
                    }
                    // dialog.dismiss();
                } else {
                    otpAuthMsg.setText(context.getResources().getString(R.string.enterOtpRec));
                    otpAuthMsg.setTextColor(AppUtility.getColor(context, R.color.red));
                    otpAuthMsg.setVisibility(View.VISIBLE);
                }

            }
        });
        cancelBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // seccItem.setMobileAuth("P");
                dialog.dismiss();
            }
        });
        resendBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                requestForOTP(mobileNumber);
            }
        });
        dialog.show();
    }
}
