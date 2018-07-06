package com.nhpm.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.customComponent.CustomAlert;
import com.customComponent.CustomAsyncTask;
import com.customComponent.Networking.CustomVolley;
import com.customComponent.Networking.CustomVolleyGet;
import com.customComponent.Networking.VolleyTaskListener;
import com.customComponent.TaskListener;
import com.customComponent.utility.CustomHttp;
import com.customComponent.utility.DateTimeUtil;
import com.customComponent.utility.ProjectPrefrence;
import com.nhpm.LocalDataBase.dto.SeccDatabase;
import com.nhpm.Models.ApplicationLanguageItem;
import com.nhpm.Models.request.LoginRequest;
import com.nhpm.Models.request.MobileOtpRequest;
import com.nhpm.Models.request.MobileOtpRequestLoginModel;
import com.nhpm.Models.request.SaveLoginTransactionRequestModel;
import com.nhpm.Models.response.LoginOTPResponseModel;
import com.nhpm.Models.response.MobileOTPResponse;
import com.nhpm.Models.response.SaveLoginTransactionResponseModel;
import com.nhpm.Models.response.master.ConfigurationItem;
import com.nhpm.Models.response.master.StateItem;
import com.nhpm.Models.response.master.response.AppUpdatVersionResponse;
import com.nhpm.Models.response.rsbyMembers.RSBYMemberItemResponse;
import com.nhpm.Models.response.seccMembers.HouseHoldItem;
import com.nhpm.Models.response.verifier.AadhaarResponseItem;
import com.nhpm.Models.response.verifier.MobileNumberOTPValidaationResponse;
import com.nhpm.Models.response.verifier.VerifierLocationItem;
import com.nhpm.Models.response.verifier.VerifierLoginResponse;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;
import com.nhpm.Utility.AppUtility;
import com.nhpm.Utility.ApplicationGlobal;
import com.nhpm.activity.AppUpdateActivity;
import com.nhpm.activity.BlockDetailActivity;
import com.nhpm.activity.LoginActivity;
import com.nhpm.activity.PinLoginActivity;
import com.nhpm.activity.SetPinActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pl.polidea.view.ZoomView;


public class NonAadharLoginFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "LOGIN ACTIVITY";
    //  private  EditText optET;
    private static EditText optET;
    private static Context mContext;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button submit;
    private Button verifyWithOfflineBT;
    private StateItem selectedStateItem;
    private LoginOTPResponseModel loginOTPResponseModel;
    private ArrayList<ApplicationLanguageItem> languageList;
    private View alertView;
    private MobileOTPResponse otpResponse;
    private EditText otp;
    private AutoCompleteTextView emailAddrET, passwordET;
    private ImageView internet;
    private Button verify;
    //private boolean isVeroff;
    private ApplicationLanguageItem appLangItem;
    private AlertDialog dialog;
    private Context context;
    private TextView appVersionTV, login_title;
    private VerifierLoginResponse loginResponse, storedLoginResponse;
    private LoginRequest request;
    // private CheckBox termsCB;
    private TextView releaseDateTV;
    private String[] aadhaarNumber = new String[1];
    private LinearLayout toolTipLayout;
    private CustomAsyncTask asyncTask;
    private LinearLayout mZoomLinearLayout;
    private ZoomView zoomView;
    private AadhaarResponseItem aadhaarRespItem;
    private LoginActivity activity;
    private String loginMode;
    private String seccDataDownloaded;
    private CustomAsyncTask mobileOtpAsyncTask;
    private MobileOTPResponse mobileOtpRequestModel;
    private MobileOTPResponse mobileOtpVerifyModel;

    //private AlertDialog dialog;
    public NonAadharLoginFragment() {
    }

    public static NonAadharLoginFragment newInstance(String param1, String param2) {
        NonAadharLoginFragment fragment = new NonAadharLoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_nonadhar_login, container, false);
        setupScreen(view);
        searchMenu(view);
        return view;
    }

    private void setupScreen(View v) {
        context = getActivity();
        mContext = getActivity();
        mZoomLinearLayout = (LinearLayout) v.findViewById(R.id.mZoomLinearLayout);
        selectedStateItem = StateItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SELECTED_STATE, mContext));
        showNotification(v);
        updateTable();
        storedLoginResponse = VerifierLoginResponse.create(
                ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.VERIFIER_CONTENT, context));
        if (storedLoginResponse != null) {
            aadhaarNumber[0] = storedLoginResponse.getAadhaarNumber();
        }
        internet = (ImageView) v.findViewById(R.id.internet);
        try {
            final Handler handler = new Handler();
            final Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    if (activity.isNetworkAvailable()) {
                        internet.setImageDrawable(mContext.getResources().getDrawable(R.drawable.green_like));
                    } else {
                        internet.setImageDrawable(mContext.getResources().getDrawable(R.drawable.red_like));
                    }
                    handler.postDelayed(this, 5);
                }

            };
            handler.postDelayed(runnable, 5);
        } catch (Exception ex) {
        }
        emailAddrET = (AutoCompleteTextView) v.findViewById(R.id.emailAddrET);
        passwordET = (AutoCompleteTextView) v.findViewById(R.id.passwordET);
        submit = (Button) v.findViewById(R.id.submit);
        toolTipLayout = (LinearLayout) v.findViewById(R.id.toolTipLayout);
        LayoutInflater factory = LayoutInflater.from(context);
        alertView = factory.inflate(R.layout.custom_alert, null);
        verifyWithOfflineBT = (Button) v.findViewById(R.id.offlineLoginBT);
        appVersionTV = (TextView) v.findViewById(R.id.versionTV);
        otp = (EditText) alertView.findViewById(R.id.otp);
        emailAddrET.requestFocus();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, aadhaarNumber);
        emailAddrET.setThreshold(1);
        emailAddrET.setAdapter(adapter);
        releaseDateTV = (TextView) v.findViewById(R.id.releaseDateTV);
        login_title = (TextView) v.findViewById(R.id.login_title);
        VerifierLoginResponse verifierDetail = VerifierLoginResponse.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.VERIFIER_CONTENT, context));
        releaseDateTV.setText(context.getResources().getString(R.string.releaseDate) + AppConstant.RELEASE_DATE);
        dialog = new AlertDialog.Builder(context).create();
        dialog.setView(alertView);
        appVersionTV.setText(context.getResources().getString(R.string.version) + findApplicationVersion());
        toolTipLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertForToolTip(getResources().getString(R.string.login_tool_tip));
            }
        });
        if (storedLoginResponse == null) {
            verifyWithOfflineBT.setVisibility(View.GONE);
        }
        if (storedLoginResponse != null && storedLoginResponse.getPin().equalsIgnoreCase("")) {
            verifyWithOfflineBT.setVisibility(View.GONE);
        }
        if (storedLoginResponse != null && !storedLoginResponse.getPin().equalsIgnoreCase("")) {
            //verifyWithOfflineBT.setVisibility(View.VISIBLE);
        }
        /*if (SeccDatabase.houseHoldCount(context) == 0) {
            verifyWithOfflineBT.setVisibility(View.GONE);
        }*/
        seccDataDownloaded = ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.dataDownloaded, context);

       /* if (seccDataDownloaded != null) {
            if (seccDataDownloaded.equalsIgnoreCase("N")) {
                verifyWithOfflineBT.setVisibility(View.VISIBLE);
            } else {
                if (SeccDatabase.houseHoldCount(context) == 0) {
                    verifyWithOfflineBT.setVisibility(View.GONE);
                }
            }
        } else {
            if (SeccDatabase.houseHoldCount(context) == 0) {
                verifyWithOfflineBT.setVisibility(View.GONE);
            }
        }*/

        verifyWithOfflineBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedStateItem != null && selectedStateItem.getStateCode() != null) {
                    AppUtility.hideSoftInput(activity, verifyWithOfflineBT);
                    if (emailAddrET.getText().toString().equalsIgnoreCase("")) {

                        //CustomAlert.alertWithOk(context, getResources().getString(R.string.enterValiduserName));
                        CustomAlert.alertWithOk(context, "Please enter mobile number");

                    } else
             /*   if (!termsCB.isChecked()) {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.login_ekyc_check));
                } else */
                        if (storedLoginResponse != null) {
                            if (storedLoginResponse.getAadhaarNumber() != null
                                    && !storedLoginResponse.getAadhaarNumber().equalsIgnoreCase("")) {
                                if (!storedLoginResponse.getAadhaarNumber().equalsIgnoreCase
                                        (emailAddrET.getText().toString().trim())) {
                                    pinOfflineLogin();
                               /*     if (seccDataDownloaded != null) {
                                        if (seccDataDownloaded.equalsIgnoreCase("N")) {
                                            pinOfflineLogin();
                                        } else {
                                            if (SeccDatabase.houseHoldCount(context) > 0) {
                                                CustomAlert.alertWithOk(context, context.getResources().getString(R.string.ebNotMaching));
                                            } else {
                                                CustomAlert.alertWithOk(context, context.getResources().getString(R.string.dataNotDownload));//getResources().getString(R.string.invalid_login));
                                            }
                                        }
                                    } else {
                                        if (SeccDatabase.houseHoldCount(context) > 0) {
                                            CustomAlert.alertWithOk(context, context.getResources().getString(R.string.ebNotMaching));
                                        } else {
                                            CustomAlert.alertWithOk(context, context.getResources().getString(R.string.dataNotDownload));//getResources().getString(R.string.invalid_login));
                                        }
                                    }*/

                                } else {
                                    pinOfflineLogin();
                                }
                            } else {
                                // CustomAlert.alertWithOk(context, getResources().getString(R.string.invalid_login));
                            }

                        }
                } else {
                    AppUtility.alertWithOk(context, "Please select state");
                }
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedStateItem != null && selectedStateItem.getStateCode() != null) {
                    AppUtility.hideSoftInput(activity, submit);
                    String userName = emailAddrET.getText().toString();
                    //String password = passwordET.getText().toString();
                    String password = "123456";
                    if (userName.equalsIgnoreCase("")) {
                        CustomAlert.alertWithOk(context, context.getResources().getString(R.string.enterValiduserMobileNumber));
                        return;
                    }

                    request = new LoginRequest();
                    request.setAadhaarNumber(userName);
                    request.setApplicationid(AppConstant.APPLICATION_ID);
                    //  request.setPassword(password);
                    //     request.setLoginType(AppConstant.LOGIN_TYPE_EMAIL);
                    //request.setImeiNo1("358520070004861");

                    request.setImeiNo1(AppUtility.getIMEINumber(context));
                    request.setAppVersion(AppUtility.getCurrentApplicationVersion(context));
                    if (activity.isNetworkAvailable()) {
                        if (storedLoginResponse != null) {
                            if (storedLoginResponse.getAadhaarNumber() != null && !storedLoginResponse.getAadhaarNumber().equalsIgnoreCase("")) {
                                if (!storedLoginResponse.getAadhaarNumber().equalsIgnoreCase(userName.trim())) {
                                    if (SeccDatabase.houseHoldCount(context) > 0) {
                                        CustomAlert.alertWithOk(context, context.getResources().getString(R.string.ebNotMaching));
                                    } else {
                                        loginRequest();
                                    }
                                } else {
                                    loginRequest();
                                }
                            } else {
                                VerifierLocationItem location = VerifierLocationItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SELECTED_BLOCK, context));
                                ArrayList<HouseHoldItem> houseHoldList = null;
                         /*   if (location != null) {
                                houseHoldList = SeccDatabase.getSeccHouseHoldList(location.getStateCode(), location.getDistrictCode(),
                                        location.getTehsilCode(), location.getVtCode(), location.getWardCode(), location.getBlockCode(), context);
                            }else{*/
                                houseHoldList = SeccDatabase.getAllHouseHold(context);
                           /* }*/
                                if (houseHoldList != null && houseHoldList.size() > 0) {
                                    AppUtility.alertWithOk(context, context.getResources().getString(R.string.pleaseClearDevice));
                                } else {
                                    loginRequest();
                                }
                                // loginRequest();
                            }
                        } else {
                            loginRequest();
                        }


                    } else {
                        CustomAlert.alertWithOk(context, context.getResources().getString(R.string.pleaseConnectInternet));
                    }

                } else {
                    AppUtility.alertWithOk(context, "Please select state");
                }
            }
        });

        emailAddrET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() > 0) {
                    if (Integer.parseInt(charSequence.toString().substring(0, 1)) > 5) {
                        //isValidMobile = true;
                        emailAddrET.setTextColor(AppUtility.getColor(context, R.color.black_shine));
                        if (emailAddrET.getText().toString().length() == 10) {
                            // whoseMobileSP.setEnabled(true);
                            // whoseMobileSP.setAlpha(1.0f);
                            // mobileValidateLayout.setVisibility(View.VISIBLE);
                            //prepareFamilyStatusSpinner(mobileNumberET.getText().toString().trim());
                            emailAddrET.setTextColor(AppUtility.getColor(context, R.color.green));

                        }
                    } else {
                        //whoseMobileSP.setEnabled(false);
                        // whoseMobileSP.setAlpha(0.4f);
                        // isValidMobile = false;
                        //mobileValidateLayout.setVisibility(View.GONE);
                        emailAddrET.setTextColor(AppUtility.getColor(context, R.color.red));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    /*    emailAddrET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                emailAddrET.setTextColor(Color.BLACK);

                if (!isEmailValid(s.toString())) {
                    emailAddrET.setTextColor(Color.RED);
                } else {
                    emailAddrET.setTextColor(Color.GREEN);
                    //AppUtility.softKeyBoard(activity, 0);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });*/

    }


    private void setPin() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.VERIFIER_CONTENT, loginResponse.serialize(), context);
        ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.WORNG_PIN_ENTERED_TIMESTAMP, 0 + "", context);
        if (loginResponse.getPin() != null && loginResponse.getPin().equalsIgnoreCase("")) {
            Intent theIntent = new Intent(context, SetPinActivity.class);
            startActivity(theIntent);
            activity.finish();
            activity.leftTransition();
        } else {
            String sessionValue = ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_NAME, AppConstant.SESSION_EXPIRE_INVAILD_TOKEN, context);
            // Log.d("Session Value: ",sessionValue);
            if (sessionValue != null && sessionValue.equalsIgnoreCase("Y")) {
                activity.finish();
                ProjectPrefrence.removeSharedPrefrenceData(AppConstant.PROJECT_NAME, AppConstant.SESSION_EXPIRE_INVAILD_TOKEN, context);
            } else {
                ProjectPrefrence.removeSharedPrefrenceData(AppConstant.PROJECT_NAME, AppConstant.SESSION_EXPIRE_INVAILD_TOKEN, context);
                Intent theIntent = new Intent(context, BlockDetailActivity.class);
                startActivity(theIntent);
                activity.finish();
                activity.leftTransition();
            }
            /*if(SeccDatabase.getAadhaarStatusList(context).size()>0
                    && SeccDatabase.getMemberStatusList(context).size()>0) {
                Intent theIntent = new Intent(context, BlockDetailActivity.class);
                startActivity(theIntent);
                finish();
                leftTransition();
            }else{
               if(loginResponse.getLocationList()!=null && loginResponse.getLocationList().size()>0) {
                   Intent theIntent = new Intent(context, DownloadingMasterActivity.class);
                   startActivity(theIntent);
                   finish();
                   leftTransition();
               }else{
                   CustomAlert.alertWithOk(context,"No Enumeration block assign to validation,Please contact to your supervisor.");
               }
            }*/
        }
        //checkUpdatedVersion();
    }

    private void pinOfflineLogin() {
        AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Login Response : " + storedLoginResponse.serialize());
        String userName = emailAddrET.getText().toString();
        if (userName.equalsIgnoreCase("")) {
            CustomAlert.alertWithOk(context, context.getResources().getString(R.string.pleaseEnterUserName));
        } else if (storedLoginResponse != null && storedLoginResponse.getAadhaarNumber() != null
                && !userName.equalsIgnoreCase(storedLoginResponse.getAadhaarNumber())) {

            if (SeccDatabase.houseHoldCount(context) > 0 && SeccDatabase.seccMemberCount(context) > 0) {

                CustomAlert.alertWithOk(context, context.getResources().getString(R.string.ebNotMaching));

            }/* else if (SeccDatabase.houseHoldCount(context) == 0 && SeccDatabase.seccMemberCount(context) == 0) {
                CustomAlert.alertWithOk(context, context.getResources().getString(R.string.pleaseDownloadEb));
            }*/ else {
                Intent theIntent = new Intent(context, PinLoginActivity.class);
                startActivity(theIntent);
                activity.leftTransition();
                activity.finish();
            }

        }
        /*else if (!termsCB.isChecked()) {
            CustomAlert.alertWithOk(context, context.getResources().getString(R.string.login_ekyc_check));

        }*/
        else {
            if (storedLoginResponse == null) {
                CustomAlert.alertWithOk(context, "Your session has been expired, Please login online.");
            }
            Intent theIntent = new Intent(context, PinLoginActivity.class);
            startActivity(theIntent);
            activity.leftTransition();
            activity.finish();
            /*else if (SeccDatabase.houseHoldCount(context) > 0 && SeccDatabase.seccMemberCount(context) > 0) {
                Intent theIntent = new Intent(context, PinLoginActivity.class);
                startActivity(theIntent);
                activity.leftTransition();
                activity.finish();
            } else {
                if (seccDataDownloaded != null && seccDataDownloaded.equalsIgnoreCase("N")) {
                    Intent theIntent = new Intent(context, PinLoginActivity.class);
                    startActivity(theIntent);
                    activity.leftTransition();
                    activity.finish();
                } else {
                    CustomAlert.alertWithOk(context, "Data is not downloaded to validate, Please login with Aadhaar OTP and download data");
                }
            }*/
        }
    }

    private String findApplicationVersion() {
        PackageManager packageManager = context.getPackageManager();
        String packageName = context.getPackageName();
        String myVersionName = "not available"; // initialize String

        try {
            myVersionName = packageManager.getPackageInfo(packageName, 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return myVersionName;
    }

    private int findApplicationVersionCode() {
        PackageManager packageManager = context.getPackageManager();
        String packageName = context.getPackageName();
        int versionCode = 0; // initialize String

        try {
            versionCode = packageManager.getPackageInfo(packageName, 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    private void loginRequest() {
        // showHideProgressDialog(true);
        Log.d(TAG, "Login request : " + request.serialize() + " : URL : " + AppConstant.LOGIN_API);
        TaskListener taskListener = new TaskListener() {
            @Override
            public void execute() {

                try {
                    HashMap<String, String> response = CustomHttp.httpPost(AppConstant.LOGIN_API, request.serialize());
                    loginOTPResponseModel = LoginOTPResponseModel.create(response.get(AppConstant.RESPONSE_BODY));
                    if (loginOTPResponseModel != null) {
                        if (loginOTPResponseModel.isStatus())
                            validateStateAndData();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void updateUI() {
                if (loginOTPResponseModel != null) {
                    if (loginOTPResponseModel.isStatus()) {
                        validateStateAndData();
                    } else {
                        CustomAlert.alertWithOk(context, loginOTPResponseModel.getErrorMessage());

                    }
                } else {
                    CustomAlert.alertWithOk(context, "Internal server error");

                }
            }

        };

        if (mobileOtpAsyncTask != null) {
            mobileOtpAsyncTask.cancel(true);
            mobileOtpAsyncTask = null;
        }

        mobileOtpAsyncTask = new CustomAsyncTask(taskListener, context.getResources().getString(R.string.please_wait), context);
        mobileOtpAsyncTask.execute();
               /* //  Log.d(TAG,"Login Response : "+response.toString());
                loginResponse = VerifierLoginResponse.create(response);
                if (loginResponse != null) {
                    if (loginResponse.isStatus()) {
                        // if(!loginResponse.getUserId().equalsIgnoreCase(adhar.getText().toString())) {
                        if (loginResponse.getUserStatus() != null && loginResponse.getUserStatus().trim().equalsIgnoreCase(AppConstant.VALID_STATUS)) {
                            if (loginResponse.getRole() != null && loginResponse.getRole().equalsIgnoreCase(AppConstant.user_status)) {
                                //  setPin();
                                if (loginResponse.getMobileNumber() != null && !loginResponse.getMobileNumber().equalsIgnoreCase("")) {
                                    *//*requestForOTP(loginResponse.getMobileNumber());*//*
                                    validateStateAndData();
                                }
                            } else {
                                CustomAlert.alertWithOk(context, context.getResources().getString(R.string.Youare) + loginResponse.getRole() + context.getResources().getString(R.string.levelUser));
                            }
                        } else {
                            CustomAlert.alertWithOk(context, context.getResources().getString(R.string.inactiveUser));
                        }

                    } else {
                        if (loginResponse != null && loginResponse.getErrorCode() != null && loginResponse.getErrorCode().equalsIgnoreCase(AppConstant.invalid_user)) {
                            CustomAlert.alertWithOk(context, context.getResources().getString(R.string.user_not_register));
                        } else if (loginResponse != null && loginResponse.getErrorCode() != null && loginResponse.getErrorCode().trim().equalsIgnoreCase(AppConstant.invalid_imei)) {
                            CustomAlert.alertWithOk(context, loginResponse.getErrorMessage());
                        }
                    }
                }*/


      /*  CustomVolley volley = new CustomVolley(taskListener, context.getResources().getString(R.string.pleaseWait), AppConstant.LOGIN_API, request.serialize(), AppConstant.AUTHORIZATION, AppConstant.AUTHORIZATIONVALUE, context);
        String requestBody = request.serialize();
        System.out.print(requestBody);
        volley.execute();*/
    }

    private void validateStateAndData() {
        popupForOTPValidation(request.getAadhaarNumber(), loginOTPResponseModel.getTransactionid());
        //loginResponse.setLoginSession(true);
   /*     if (selectedStateItem != null && selectedStateItem.getStateCode() != null && loginResponse.getLocationList() != null && loginResponse.getLocationList().size() > 0) {
            if (!selectedStateItem.getStateCode().equalsIgnoreCase(loginResponse.getLocationList().get(0).getStateCode())) {
                alertWithOk(context, "You are not authorised user for " + selectedStateItem.getStateName());
            } else {
              // requestForOTP(loginResponse.getMobileNumber());
                popupForOTPValidation(request.getAadhaarNumber(), mobileOtpRequestModel.getSequenceNo());
                loginResponse.setLoginSession(true);
                //setPin();
            }
        } else {
            AppUtility.alertWithOk(context, context.getResources().getString(R.string.locationNotAllocate));
        }*/

    }

    public void alertWithOk(Context mContext, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Alert");
        builder.setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if (AppUtility.deleteFile(new File(DatabaseHelpers.DELETE_FOLDER_PATH))) {
                        ProjectPrefrence.removeSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SELECTED_STATE, context);
                        ProjectPrefrence.removeSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.dataDownloaded, context);
                        ProjectPrefrence.removeSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.DOWNLOADINGSOURCE, context);
                        ProjectPrefrence.removeSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.VERIFIER_CONTENT, context);
                        ProjectPrefrence.removeSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.MEMBER_DOWNLOADED_COUNT, context);
                        ProjectPrefrence.removeSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.HOUSEHOLD_DOWNLOADED_COUNT, context);
                        Intent theIntent = new Intent(context, LoginActivity.class);
                        theIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        theIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(theIntent);
                        // }
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

/*    private void requestAadhaarAuth(String aadhaarNo) {
        VolleyTaskListener taskListener = new VolleyTaskListener() {
            @Override
            public void postExecute(String response) {
                Log.d(TAG, "Login Response : " + response.toString());
                AadhaarOtpResponse resp = AadhaarOtpResponse.create(response);
                System.out.print(resp);
                if (resp != null) {
                    if (resp.getRet() != null) {
                        if (resp.getRet().trim().equalsIgnoreCase(AppConstant.VALID_STATUS)) {
                            String stringContainingNumber = resp.getInfo();
                            System.out.print(stringContainingNumber);
                            String requiredString = resp.getInfo().substring(resp.getInfo().indexOf("*"), resp.getInfo().indexOf(",NA"));
                            popupForOTP(requiredString);
                        } else if (resp.getRet().trim().equalsIgnoreCase(AppConstant.INVALID_STATUS)) {
                            // CustomAlert.alertWithOk(context, resp.getErr());
                            CustomAlert.alertWithOk(context, context.getResources().getString(R.string.mobileno_not_reg_with_adhar));
                            //By Pass OTP

                            //   popupForOTP();
                        }
                    } else {
                        CustomAlert.alertWithOk(context, context.getResources().getString(R.string.unableToConnectUIDAI));
                    }

                } else {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.unableToConnectUIDAI));
                }

            }

            @Override
            public void onError(VolleyError error) {
                CustomAlert.alertWithOk(context, getResources().getString(R.string.aadhaar_connect_error));
                //  popupForOTP();
            }
        };
   *//*     String url=AppConstant.AADHAR_OTP_AUTH_API+aadhaarNo+AppConstant.USER_ID+AppConstant.PASSWORD;
        AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"URL : "+url);
        CustomVolleyGet volley=new CustomVolleyGet(taskListener,"Please wait..",url.trim(),context);
        volley.execute();*//*
        String url = AppConstant.AADHAR_OTP_AUTH_API_NEW;
        AadhaarAuthRequestItem aadhaarReq = new AadhaarAuthRequestItem();
        aadhaarReq.setUid(aadhaarNo);
        String imei = AppUtility.getIMEINumber(context);
        if (imei != null) {
            aadhaarReq.setImeiNo(imei);
        }
        aadhaarReq.setProject(AppConstant.PROJECT_NAME);
        aadhaarReq.setUserName(AppConstant.AADHAAR_AUTH_USERNAME);
        aadhaarReq.setUserPass(AppConstant.AADHAAR_AUTH_ENCRIPTED_PASSWORD);
        AppUtility.showLog(AppConstant.LOG_STATUS, TAG, " Login Response otp request : " + aadhaarReq.serialize() + " : API :" + url);
        CustomVolley volley = new CustomVolley(taskListener, context.getResources().getString(R.string.pleaseWait), AppConstant.AADHAR_OTP_AUTH_API_NEW, aadhaarReq.serialize(), null, null, context);
        volley.execute();
        Log.d(TAG, "OTP Request API : " + url);

    }*/

   /* private void validateOTP(final String aadhaarNo, final String otp, final TextView errorTV) {

        TaskListener taskListener = new TaskListener() {
            @Override
            public void execute() {
                aadhaarRespItem = null;
                String url = AppConstant.AADHAAR_KYC_DATA_API_NEW;
                AadhaarAuthRequestItem aadhaarReq = new AadhaarAuthRequestItem();
                aadhaarReq.setUid(aadhaarNo.trim());
                String imei = AppUtility.getIMEINumber(context);
                if (imei != null) {
                    aadhaarReq.setImeiNo(imei);
                }
                aadhaarReq.setOtp(otp);
                aadhaarReq.setProject(AppConstant.PROJECT_NAME);
                aadhaarReq.setUserName(AppConstant.AADHAAR_AUTH_USERNAME);
                aadhaarReq.setUserPass(AppConstant.AADHAAR_AUTH_ENCRIPTED_PASSWORD);
                // AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Login Response otp request "+aadhaarReq.serialize()+" : API :"+url);
                try {
                    HashMap<String, String> response = CustomHttp.httpPost(url, aadhaarReq.serialize(), null);
                    if (response != null) {
                        aadhaarRespItem = AadhaarResponseItem.create(response.get(AppConstant.RESPONSE_BODY));
                    }

                } catch (Exception e) {

                }

            }

            @Override
            public void updateUI() {
                if (aadhaarRespItem != null) {
                    if (aadhaarRespItem.getResult() != null && aadhaarRespItem.getResult().equalsIgnoreCase("Y")) {
                        loginResponse.setAadhaarItem(aadhaarRespItem);
                        ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.VERIFIER_CONTENT, loginResponse.serialize(), context);
                        setPin();
                    } else {
                        // aadhaarRespItem.get
                        // CustomAlert.alertWithOk(context,);
                        //CustomAlert.alertWithOk(context,"Invalid Aadhaar Number");
                        errorTV.setVisibility(View.VISIBLE);
                        optET.setText("");
//BYPASSED PROCESS
                        setPin();
                    }
                } else {
                    //  setPin();
                    CustomAlert.alertWithOk(context, getResources().getString(R.string.aadhaar_connect_error));
                }
            }
        };
     *//*   VolleyTaskListener taskListener=new VolleyTaskListener() {
            @Override
            public void postExecute(String response) {
                Log.d(TAG,"Login Response aadhaar : "+response.toString());
                AadhaarResponseItem aadhaarRespItem=AadhaarResponseItem.create(response);
              *//**//*  ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.VERIFIER_CONTENT, loginResponse.serialize(), context);
                setPin();*//**//*
                if(aadhaarRespItem!=null) {
                    if (aadhaarRespItem.getResult() != null && aadhaarRespItem.getResult().equalsIgnoreCase("Y")) {
                        loginResponse.setAadhaarItem(aadhaarRespItem);
                        ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.VERIFIER_CONTENT, loginResponse.serialize(), context);
                        setPin();
                    } else {
                       // aadhaarRespItem.get
                       // CustomAlert.alertWithOk(context,);
                        //CustomAlert.alertWithOk(context,"Invalid Aadhaar Number");
                        errorTV.setVisibility(View.VISIBLE);
 //BYPASSED PROCESS
                    //   setPin();
                    }
                }else{
                  //  setPin();
                    CustomAlert.alertWithOk(context,getResources().getString(R.string.aadhaar_connect_error));
                }
            }

            @Override
            public void onError(VolleyError error) {
                //setPin();
                CustomAlert.alertWithOk(context,getResources().getString(R.string.aadhaar_connect_error));
            }
        };*//*

      *//*  String url=AppConstant.AADHAAR_KYC_DATA+aadhaarNo+"/"+otp+AppConstant.USER_ID+AppConstant.PASSWORD;
        Log.d(TAG,"Validate OTP URL : "+url);
        *//**//*CustomVolley volley=new CustomVolley(taskListener,"Please Wait..",AppConstant.LOGIN_API,request.serialize(),null,null,context);
        volley.execute();
*//**//*
        CustomVolleyGet volley=new CustomVolleyGet(taskListener,"Please wait..",url.trim(),context);
        volley.execute();
        *//**//**//*
       *//* String url=AppConstant.AADHAAR_KYC_DATA_API_NEW;
        AadhaarAuthRequestItem aadhaarReq=new AadhaarAuthRequestItem();
        aadhaarReq.setUid(aadhaarNo);
        *//**//*String imei=AppUtility.getIMEINumber(context);
        if(imei!=null) {
            aadhaarReq.setImeiNo(imei);
        }*//**//*
        aadhaarReq.setOtp(otp);
        aadhaarReq.setProject(AppConstant.PROJECT_NAME);
        aadhaarReq.setUserName(AppConstant.AADHAAR_AUTH_USERNAME);
        aadhaarReq.setUserPass(AppConstant.AADHAAR_AUTH_ENCRIPTED_PASSWORD);*//*
        // AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Login Response otp request "+aadhaarReq.serialize()+" : API :"+url);
        if (asyncTask != null) {
            asyncTask.cancel(true);
            asyncTask = null;
        }

        asyncTask = new CustomAsyncTask(taskListener, context.getResources().getString(R.string.please_wait), context);
        asyncTask.execute();
        *//*CustomVolley volley=new CustomVolley(taskListener,"Please Wait..",AppConstant.AADHAR_OTP_AUTH_API_NEW,aadhaarReq.serialize(),null,null,context);
        volley.execute();*//*
    }*/

    private void rsbyResponse() {
        String url = "http://nikshay.gov.in/RSBY/MyRESTService.svc/Dependent/15081200116000024";
        VolleyTaskListener taskListener = new VolleyTaskListener() {
            @Override
            public void postExecute(String response) {
                RSBYMemberItemResponse resp = RSBYMemberItemResponse.create(response);
                Log.d(TAG, "RSBY List Size : " + resp.getUrnList().size());
            }

            @Override
            public void onError(VolleyError error) {

            }
        };
        CustomVolleyGet volley = new CustomVolleyGet(taskListener, context.getResources().getString(R.string.please_wait), url, context);
        volley.execute();
    }

    private void updateTable() {


        try {
            String query = "ALTER TABLE " + AppConstant.HOUSE_HOLD_SECC +
                    " ADD COLUMN  error_code character varying(200)";
            SeccDatabase.alterTable(query, context);
        } catch (Exception e) {

        }

        try {
            String query = "ALTER TABLE " + AppConstant.HOUSE_HOLD_SECC +
                    " ADD COLUMN  error_msg character TEXT";
            SeccDatabase.alterTable(query, context);
        } catch (Exception e) {

        }
        try {
            String query = "ALTER TABLE " + AppConstant.HOUSE_HOLD_SECC +
                    " ADD COLUMN error_type TEXT";
            SeccDatabase.alterTable(query, context);
        } catch (Exception e) {

        }

        try {
            String query = "ALTER TABLE " + AppConstant.HOUSE_HOLD_SECC +
                    " ADD COLUMN locked_save character varying(1)";
            SeccDatabase.alterTable(query, context);
        } catch (Exception e) {

        }

        try {
            String query = "ALTER TABLE " + AppConstant.HOUSE_HOLD_SECC +
                    " ADD COLUMN synced_status character varying(1)";
            SeccDatabase.alterTable(query, context);
        } catch (Exception e) {

        }

        try {
            String query = "ALTER TABLE " + AppConstant.HOUSE_HOLD_SECC +
                    " ADD COLUMN nhps_id character varying(12)";
            SeccDatabase.alterTable(query, context);
        } catch (Exception e) {

        }

       /* try {
            String query = "ALTER TABLE " + AppConstant.HOUSE_HOLD_SECC +
                    " ADD COLUMN error_code character varying(1)";
            SeccDatabase.alterTable(query, context);
        }catch (Exception e){

        }*/

        try {
            String query = " CREATE TABLE " + AppConstant.MEMBER_ERROR_TABLE + " (ahl_tin character varying(29),error_code character varying(1)," +
                    " error_msg character varying(200),error_type character varying(50))";
            SeccDatabase.createTable(query, context);
        } catch (Exception e) {
            Log.d(TAG, "Create Exception" + e.toString());

        }
        try {
            String query = "ALTER TABLE " + AppConstant.MEMBER_ERROR_TABLE +
                    " ADD COLUMN hhd_no character varying(25)";
            SeccDatabase.alterTable(query, context);
        } catch (Exception e) {


        }

        try {
            String query = "ALTER TABLE " + AppConstant.MEMBER_ERROR_TABLE +
                    " ADD COLUMN nhps_mem_id character varying(11)";
            SeccDatabase.alterTable(query, context);
        } catch (Exception e) {


        }


/*        try {
            String query = "ALTER TABLE " + AppConstant.TRANSECT_POPULATE_SECC +
                    " ADD COLUMN locked_save character varying(1)";
            SeccDatabase.alterTable(query, context);
        }catch (Exception e){

        }

        try {
            String query = "ALTER TABLE " + AppConstant.TRANSECT_POPULATE_SECC +
                    " ADD COLUMN govt_id_survey_status character varying(1)";
            SeccDatabase.alterTable(query, context);
        }catch (Exception e){

        }

        try {
            String query = "ALTER TABLE " + AppConstant.TRANSECT_POPULATE_SECC +
                    " ADD COLUMN  aadhar_survey_status character varying(1)";
            SeccDatabase.alterTable(query, context);
        }catch (Exception e){

        }
        try {
            String query = "ALTER TABLE " + AppConstant.TRANSECT_POPULATE_SECC +
                    " ADD COLUMN  photo_capture_status character varying(1)";
            SeccDatabase.alterTable(query, context);
        }catch (Exception e){

        }

        try {
            String query = "ALTER TABLE " + AppConstant.TRANSECT_POPULATE_SECC +
                    " ADD COLUMN  mobile_surveyed_status character varying(1)";
            SeccDatabase.alterTable(query, context);
        }catch (Exception e){

        }

        try {
            String query = "ALTER TABLE " + AppConstant.TRANSECT_POPULATE_SECC +
                    " ADD COLUMN  nominee_surveyed_status character varying(1)";
            SeccDatabase.alterTable(query, context);
        }catch (Exception e){

        }

        try {
            String query = "ALTER TABLE " + AppConstant.TRANSECT_POPULATE_SECC +
                    " ADD COLUMN  synced_status character varying(1)";
            SeccDatabase.alterTable(query, context);
        }catch (Exception e){

        }
        try {
            String query = "ALTER TABLE " + AppConstant.TRANSECT_POPULATE_SECC +
                    " ADD COLUMN  error_code character varying(200)";
            SeccDatabase.alterTable(query, context);
        }catch (Exception e){

        }

        try {
            String query = "ALTER TABLE " + AppConstant.TRANSECT_POPULATE_SECC +
                    " ADD COLUMN  error_msg character TEXT";
            SeccDatabase.alterTable(query, context);
        }catch (Exception e){

        }
        try {
            String query = "ALTER TABLE " + AppConstant.TRANSECT_POPULATE_SECC +
                    " ADD COLUMN error_type TEXT";
            SeccDatabase.alterTable(query, context);
        }catch (Exception e){

        }
        try {
            String query = "ALTER TABLE " + AppConstant.TRANSECT_POPULATE_SECC +
                    " ADD COLUMN nhps_mem_id character varying(11)";
            SeccDatabase.alterTable(query, context);
        }catch (Exception e){


        }

        try {
            String query = "ALTER TABLE " + AppConstant.TRANSECT_POPULATE_SECC +
                    " ADD COLUMN  sync_dt character varying(60)";
            SeccDatabase.alterTable(query, context);
        }catch (Exception e){

        }
        try {
            String query = "ALTER TABLE " + AppConstant.TRANSECT_POPULATE_SECC +
                    " ADD COLUMN  app_version character varying(10)";
            SeccDatabase.alterTable(query, context);
        }catch (Exception e){


        }*/
        try {
            String query = "ALTER TABLE " + AppConstant.HOUSE_HOLD_SECC +
                    " ADD COLUMN nhps_mem_id character varying(11)";
            SeccDatabase.alterTable(query, context);
        } catch (Exception e) {

        }

        try {
            String query = "ALTER TABLE " + AppConstant.MEMBER_STATUS +
                    " ADD COLUMN sno integer";
            SeccDatabase.alterTable(query, context);
        } catch (Exception e) {


        }
/*
        try{
            String query = "ALTER TABLE " + AppConstant.HOUSE_HOLD_SECC +
                    " ADD COLUMN  sync_dt character varying(60)";
            SeccDatabase.alterTable(query, context);
        }catch (Exception e){

        }

        try{
            String  query = "ALTER TABLE " + AppConstant.HOUSE_HOLD_SECC +
                    " ADD COLUMN pincode character varying(6)";
            SeccDatabase.alterTable(query, context);
        }catch (Exception e){

        }
        try{
            String query = "ALTER TABLE " + AppConstant.HOUSE_HOLD_SECC +
                    " ADD COLUMN addressline5 character varying(60)";
            SeccDatabase.alterTable(query, context);
        }catch (Exception e){

        }
        try{
            String query = "ALTER TABLE " + AppConstant.HOUSE_HOLD_SECC +
                    " ADD COLUMN addressline4 character varying(60)";
            SeccDatabase.alterTable(query, context);
        }catch (Exception e){

        }
        try{
            String query = "ALTER TABLE " + AppConstant.HOUSE_HOLD_SECC +
                    " ADD COLUMN addressline3 character varying(60)";
            SeccDatabase.alterTable(query, context);
        }catch (Exception e){

        }

        try{
            String query = "ALTER TABLE " + AppConstant.HOUSE_HOLD_SECC +
                    " ADD COLUMN addressline2 character varying(60)";
            SeccDatabase.alterTable(query, context);
        }catch (Exception e){

        }
        try{
            String query = "ALTER TABLE " + AppConstant.HOUSE_HOLD_SECC +
                    " ADD COLUMN addressline1 character varying(60)";
            SeccDatabase.alterTable(query, context);
        }catch (Exception e){

        }*/
        /*try {
            String query = "ALTER TABLE " + AppConstant.HOUSE_HOLD_SECC +
                    " ADD COLUMN  app_version character varying(10)";
            SeccDatabase.alterTable(query, context);
        }catch (Exception e){

        }*/


        try {
            String query = "CREATE TABLE  " + AppConstant.RSBY_HOUSEHOD_TABLE +
                    "           (rsbyMemId character varying(99)," +
                    "           urnId character varying(99)," +
                    "           issuesTimespam timestamp with time zone," +
                    "           hofnamereg character varying(99)," +
                    "           doorhouse character varying(99)," +
                    "           villageCode character varying(99)," +
                    "           panchyatTownCode character varying(99)," +
                    "           blockCode character varying(99), " +
                    "           districtCode character varying(99)," +
                    "           stateCode character varying(99)," +
                    "           memid character varying(99)," +
                    "           name character varying(99)," +
                    "           dob character varying(99)," +
                    "           gender character varying(99)," +
                    "           relcode character varying(99)," +
                    "           insccode character varying(99)," +
                    "           policyno character varying(99)," +
                    "           vl_aadharNo character varying(99)," +
                    "           vl_stateCode character varying(99)," +
                    "           vl_districtCode character varying(99)," +
                    "           vl_tehsilCode character varying(99)," +
                    "           vl_vtCode character varying(99)," +
                    "           vl_wardCode character varying(99)," +
                    "           vl_blockCode character varying(99)," +
                    "           vl_subBlockcode character varying(99)," +
                    "           vl_ruralUrban character varying(99)," +
                    "           csmNo character varying(99)," +
                    "           cardType character varying(99)," +
                    "           cardCategory character varying(99)," +
                    "           policyamt character varying(99)," +
                    "           startdate timestamp with time zone," +
                    "           enddate timestamp with time zone," +
                    "           hh_status character(1)," +
                    "           rsby_familyphoto bytea," +
                    "           synced_status character varying(1)," +
                    "           locked_save character varying(1)," +
                    "           nhps_id character varying(12),sync_dt timestamp with time zone," +
                    "           app_version character varying(10), error_code text," +
                    "           error_msg text," +
                    "           error_type text)";

            SeccDatabase.createTable(query, context);
        } catch (Exception e) {
            Log.d(TAG, "Create Exception" + e.toString());
            System.out.print(e.toString());
        }
        try {
            String query = "CREATE TABLE " + AppConstant.RSBY_POPULATION_TABLE +
                    "(rsbyMemId character varying(99)," +
                    "urnId character varying(99)," +
                    "insccode character varying(99)," +
                    "policyno character varying(99)," +
                    "policyamt character varying(99)," +
                    "startdate timestamp with time zone," +
                    "enddate timestamp with time zone," +
                    "issuesTimespam timestamp with time zone," +
                    "hofnamereg character varying(99)," +
                    "doorhouse character varying(99)," +
                    "villageCode character varying(99)," +
                    "panchyatTownCode character varying(99)," +
                    "blockCode character varying(99)," +
                    "districtCode character varying(99)," +
                    "stateCode character varying(99)," +
                    "memid character varying(99)," +
                    "name character varying(99)," +
                    "dob character varying(99)," +
                    "gender character varying(99)," +
                    "relcode character varying(99)," +
                    "aadhaar_no character varying(12)," +
                    "name_aadhaar character varying(99)," +
                    "urn_no character varying(30), " +
                    "mobile_no character varying(10)," +
                    "rural_urban character varying(1)," +
                    "bank_ifsc character varying(50)," +
                    "mark_for_deletion character varying(10), " +
                    "aadhaar_status character(1)," +
                    "hh_status character(1)," +
                    "mem_status character varying(2)," +
                    "eid character varying(40), " +
                    "bank_acc_no character varying(50)," +
                    "scheme_id character varying(3)," +
                    "scheme_no character varying(50)," +
                    "name_nominee character varying(99)," +
                    "relation_nominee_code character varying(2)," +
                    "id_type character(2), " +
                    "id_no character varying(99)," +
                    "name_as_id character varying(99)," +
                    "id_photo1 bytea, " +
                    "id_photo2 bytea," +
                    "member_photo bytea," +
                    "aadhaar_capturing_mode character(1), " +
                    "consent character(1)," +
                    "aadhaar_auth_mode character(1), " +
                    "aadhaar_auth_dt time with time zone, " +
                    "aadhaar_verified_by character varying(12)," +
                    "userid character varying(12)," +
                    "date_updated time with time zone," +
                    "aadhaar_auth character(1)," +
                    "mobile_auth character(1), " +
                    "urn_auth character(1)," +
                    "ifsc_auth character(1)," +
                    "bank_acc_auth character(1)," +
                    "state_scheme_code_auth character(1)," +
                    "scheme_code character(3)," +
                    "data_source character(4)," +
                    "nhps_id character varying(12), " +
                    "mobile_auth_dt time with time zone," +
                    "urn_auth_dt time with time zone," +
                    "ifsc_auth_dt time with time zone," +
                    "bank_acc_auth_dt time with time zone, " +
                    "state_scheme_code_auth_dt time with time zone," +
                    " consent_dt time with time zone," +
                    " aadhaar_gender character(1)," +
                    " aadhaar_yob character varying(4)," +
                    " aadhaar_dob character varying(10)," +
                    " aadhaar_co character varying(99)," +
                    " aadhaar_gname character varying(99)," +
                    " aadhaar_house character varying(99), " +
                    " aadhaar_street character varying(99)," +
                    " aadhaar_loc character varying(99)," +
                    " aadhaar_vtc character varying(99)," +
                    " aadhaar_po character varying(99)," +
                    " aadhaar_dist character varying(99)," +
                    " aadhaar_subdist character varying(99)," +
                    " aadhaar_state character varying(99), " +
                    "aadhaar_pc character varying(6)," +
                    " aadhaar_lm character varying(99)," +
                    " latitude double precision, " +
                    "longitude double precision," +
                    "whose_mobile character(1)," +
                    " consent_photo bytea, " +
                    "member_photo1 text, " +
                    "nhps_relation_code character varying(3)," +
                    " nhps_relation_name character varying(256)," +
                    " nominee_relation_name character varying(100)," +
                    " govt_id_photo text, " +
                    "locked_save character varying(1), " +
                    "synced_status character varying(1)," +
                    " nhps_mem_id character varying(11)," +
                    " timestamp with time zone, " +
                    "           vl_aadharNo character varying(99)," +
                    "           vl_stateCode character varying(99)," +

                    "           vl_districtCode character varying(99)," +

                    "           vl_tehsilCode character varying(99)," +

                    "           vl_vtCode character varying(99)," +

                    "           vl_wardCode character varying(99)," +
                    "           vl_blockCode character varying(99)," +
                    "           vl_subBlockcode character varying(99)," +
                    "      vl_ruralUrban character varying(99)," +

                    "sync_dt with time zone," +
                    "app_version character varying(10)," +
                    "csmNo character varying(99)," +
                    "cardType character varying(99)," +
                    "cardCategory character varying(99)," +
                    "error_code text," +
                    "error_msg text," +
                    "error_type text)";
            SeccDatabase.createTable(query, context);
        } catch (Exception e) {
            Log.d(TAG, "Create Exception" + e.toString());
            System.out.print(e.toString());
        }
    }

    private void checkUpdatedVersion() {
        VolleyTaskListener taskListener = new VolleyTaskListener() {
            @Override
            public void postExecute(String response) {
                AppUpdatVersionResponse respItem = AppUpdatVersionResponse.create(response);

                if (respItem != null) {
                    if (respItem.isStatus()) {
                        int versionCode = Integer.parseInt(respItem.getVersionCode());
                        if (versionCode != findApplicationVersionCode()) {
                            Intent theInten = new Intent(context, AppUpdateActivity.class);
                            startActivity(theInten);
                            activity.finish();
                        } else {
                            if (loginResponse.getPin() != null && loginResponse.getPin().equalsIgnoreCase("")) {
                                Intent theIntent = new Intent(context, SetPinActivity.class);
                                startActivity(theIntent);
                                activity.finish();
                                activity.leftTransition();
                            } else {
                                Intent theIntent = new Intent(context, BlockDetailActivity.class);
                                startActivity(theIntent);
                                activity.finish();
                                activity.leftTransition();
                            }
                        }
                    } else {
                        if (loginResponse.getPin() != null && loginResponse.getPin().equalsIgnoreCase("")) {
                            Intent theIntent = new Intent(context, SetPinActivity.class);
                            startActivity(theIntent);
                            activity.finish();
                            activity.leftTransition();
                        } else {
                            Intent theIntent = new Intent(context, BlockDetailActivity.class);
                            startActivity(theIntent);
                            activity.finish();
                            activity.leftTransition();
                        }
                    }
                }
            }

            @Override
            public void onError(VolleyError error) {
                if (loginResponse.getPin() != null && loginResponse.getPin().equalsIgnoreCase("")) {
                    Intent theIntent = new Intent(context, SetPinActivity.class);
                    startActivity(theIntent);
                    activity.finish();
                    activity.leftTransition();
                } else {
                    Intent theIntent = new Intent(context, BlockDetailActivity.class);
                    startActivity(theIntent);
                    activity.finish();
                    activity.leftTransition();
                }
            }
        };
        CustomVolleyGet volleyGet = new CustomVolleyGet(taskListener, context.getResources().getString(R.string.checkinUpdateVersion), "http://103.241.181.83:8080/nhps_service/nhps/app/updatedVersion", context);
        volleyGet.execute();
    }

    private boolean checkDataWithAadhaar(ArrayList<VerifierLocationItem> locationItem) {
        boolean flag = false;
        if (locationItem != null) {
            for (VerifierLocationItem item : locationItem) {
                if (SeccDatabase.houseHoldCount(context, item.getStateCode(),
                        item.getDistrictCode(), item.getTehsilCode(), item.getVtCode(),
                        item.getWardCode(), item.getBlockCode()) > 0 && SeccDatabase.seccMemberCount(context, item.getStateCode(),
                        item.getDistrictCode(), item.getTehsilCode(), item.getVtCode(),
                        item.getWardCode(), item.getBlockCode()) > 0) {
                    flag = true;
                    break;
                }
            }
        }

        //   SeccDatabase.getHouseHoldList(loginResponse.getLocationList())
        return flag;
    }

    private void alertForToolTip(String msg) {
        dialog = new AlertDialog.Builder(context).create();
        LayoutInflater factory = LayoutInflater.from(context);
        View alertView = factory.inflate(R.layout.tool_tip_layout, null);
        dialog.setView(alertView);
        TextView toolTipTV = (TextView) alertView.findViewById(R.id.toopTipTV);
        toolTipTV.setText(msg);
        dialog.show();
        Button toolTipOK = (Button) alertView.findViewById(R.id.toolTipOkBT);
        toolTipOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }


    private void searchMenu(View v) {
        RelativeLayout menuLayout = (RelativeLayout) v.findViewById(R.id.menuLayout);
        menuLayout.setVisibility(View.VISIBLE);
        ArrayList<String> spinnerListState = new ArrayList<>();
        final Spinner appLanguageSpinner = (Spinner) v.findViewById(R.id.appLanguageText);
        final Spinner stateSelection = (Spinner) v.findViewById(R.id.stateSelection);
        if ((SeccDatabase.houseHoldCount(context) > 0)) {
            stateSelection.setVisibility(View.GONE);
        }
        stateSelection.setVisibility(View.GONE);

        final ArrayList<StateItem> stateList = SeccDatabase.findStateList(mContext);

        if (selectedStateItem != null && selectedStateItem.getStateName() != null) {
            login_title.setText(context.getResources().getString(R.string.verifier_login) + " (" + selectedStateItem.getStateName() + ")");
        }
        int position = 0;
        stateList.add(0, new StateItem("00", "Select State"));
        if (stateList != null) {
            for (StateItem item1 : stateList) {
                spinnerListState.add(item1.getStateName());
            }
            for (int i = 0; i < stateList.size(); i++) {
                if (stateList.get(i).getStateCode().equalsIgnoreCase(selectedStateItem.getStateCode())) {
                    position = i + 1;
                }
            }
        }
        stateSelection.setSelection(position);
        ArrayAdapter<String> adapterState = new ArrayAdapter<>(activity, R.layout.custom_lang_dropdown, R.id.textView, spinnerListState);
        stateSelection.setAdapter(adapterState);


        languageList = AppUtility.prepareLanguageSpinner(context);
        ArrayList<String> spinnerList = new ArrayList<String>();

        for (ApplicationLanguageItem item : languageList) {
            spinnerList.add(item.languageName);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(activity, R.layout.custom_lang_dropdown, R.id.textView, spinnerList);
        appLanguageSpinner.setAdapter(adapter);

        appLanguageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                appLangItem = languageList.get(position);

                switch (appLangItem.languageCode) {
                    case "en":
                        Locale myLocale = new Locale("en");
                        Resources res = getResources();
                        DisplayMetrics dm = res.getDisplayMetrics();
                        Configuration conf = res.getConfiguration();
                        conf.locale = myLocale;
                        res.updateConfiguration(conf, dm);
                        ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                                AppConstant.APPLICATIONLANGUAGE, "en", context);
                        // LoginActivity.this.recreate();
                        startActivity(activity.getIntent());
                        activity.finish();
                        break;
                    case "hi":
                        Locale Locale = new Locale("hi");
                        Resources resh = getResources();
                        DisplayMetrics dmh = resh.getDisplayMetrics();
                        Configuration confh = resh.getConfiguration();
                        confh.locale = Locale;
                        resh.updateConfiguration(confh, dmh);
                        ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                                AppConstant.APPLICATIONLANGUAGE, "hi", context);
                        //  LoginActivity.this.recreate();
                        startActivity(activity.getIntent());
                        activity.finish();
                        break;
                    case "gu":
                        Locale Localeg = new Locale("gu");
                        Resources reshg = getResources();
                        DisplayMetrics dmhg = reshg.getDisplayMetrics();
                        Configuration confhg = reshg.getConfiguration();
                        confhg.locale = Localeg;
                        reshg.updateConfiguration(confhg, dmhg);
                        ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                                AppConstant.APPLICATIONLANGUAGE, "gu", context);
                        //  LoginActivity.this.recreate();
                        startActivity(activity.getIntent());
                        activity.finish();
                        break;
                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });


        stateSelection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Log.d(TAG, "Selected Position : " + position);
                if (position == 0) {

                } else {
                    StateItem selectedStateItem = stateList.get(position);
                    loginNavigation(selectedStateItem);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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

    private String checkAppConfig() {
        StateItem selectedStateItem = selectedStateItem = StateItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SELECTED_STATE, context));
        ArrayList<ConfigurationItem> configList = SeccDatabase.findConfiguration(selectedStateItem.getStateCode(), context);

        if (configList != null) {
            for (ConfigurationItem item1 : configList) {
                if (item1.getConfigId().equalsIgnoreCase(AppConstant.LOGIN_CONFIG)) {
                    loginMode = item1.getStatus();
                }
            }
        }

        return loginMode;
    }


    /*private void requestToOTP(final String mobileNumber) {
        VolleyTaskListener taskListener = new VolleyTaskListener() {
            @Override
            public void postExecute(String response) {
                Log.d(TAG, "Mobile Number OTP");
                otpResponse = MobileOTPResponse.create(response);
                if (otpResponse != null && otpResponse.getOtp() != null && !otpResponse.getOtp().equalsIgnoreCase("")) {
                    popupForOTPValidation(mobileNumber);
                } else {
                    CustomAlert.alertWithOk(context, getResources().getString(R.string.server_error));
                }
            }

            @Override
            public void onError(VolleyError error) {

                //System.out.print(error.getMessage().toString());
                CustomAlert.alertWithOk(context, getResources().getString(R.string.server_error));
            }
        };
        String otpRequestUrl = AppConstant.MOBILE_OTP_REQUEST + mobileNumber +
                AppConstant.MOBILE_OTP_USERID + AppConstant.MOBILE_OTP_PASS;
        Log.d(TAG, "OTP Request API : " + otpRequestUrl);

        CustomVolleyGet volley = new CustomVolleyGet(taskListener, context.getResources().getString(R.string.please_wait), otpRequestUrl, context);
        volley.execute();
    }*/

    private void validateOTP(String otp, final TextView authOtpTV) {
        VolleyTaskListener taskListener = new VolleyTaskListener() {
            @Override
            public void postExecute(String response) {
                MobileNumberOTPValidaationResponse mobile = MobileNumberOTPValidaationResponse.create(response);
                if (mobile != null && mobile.getMessage().trim().equalsIgnoreCase("Y")) {

                    setPin();
                    dialog.dismiss();
                } else if (mobile != null && mobile.getMessage().trim().equalsIgnoreCase("N")) {
                    authOtpTV.setText(context.getResources().getString(R.string.invalid_otp));
                    authOtpTV.setTextColor(AppUtility.getColor(context, R.color.red));
                    //seccItem.setMobileAuth("N");
                    // seccItem.setMobileAuth("Y");
                    // setPin();
                }

            }

            @Override
            public void onError(VolleyError error) {
                //   setPin();
                //CustomAlert.alertWithOk(context, getResources().getString(R.string.slow_internet_connection_msg));
                dialog.dismiss();
            }
        };
        String validateOTP = AppConstant.AUTH_MOBILE_OTP + otpResponse.getSender()
                + "/" + otpResponse.getSequenceNo() + "/" + otp + AppConstant.MOBILE_OTP_USERID + AppConstant.MOBILE_OTP_PASS;
        Log.d(TAG, "OTP Validate API : " + validateOTP);
        CustomVolleyGet volley = new CustomVolleyGet(taskListener, context.getResources().getString(R.string.please_wait), validateOTP.trim(), context);
        volley.execute();
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

        new CountDownTimer(10 * 1000 + 1000, 1000) {

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
                    validateOTP(otp, mobileNumber, otpAuthMsg, loginOTPResponseModel.getTransactionid());

                    //  updatedVersionApp();
                   /* if (mobileOtpRequestModel.getOtp().equalsIgnoreCase(otp)) {
                        validateOTP(otp, mobileNumber, otpAuthMsg, loginOTPResponseModel.getTransactionid());

                    } else {
                        otpAuthMsg.setText(context.getResources().getString(R.string.enterValidOtp));
                        otpAuthMsg.setTextColor(AppUtility.getColor(context, R.color.red));
                        otpAuthMsg.setVisibility(View.VISIBLE);
                    }*/
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
                loginRequest();
                //requestForOTP(mobileNumber);
            }
        });
        dialog.show();
    }

    private void loginNavigation(StateItem selectedStateItem) {
        if (selectedStateItem != null) {
            ArrayList<ConfigurationItem> configList = SeccDatabase.findConfiguration(selectedStateItem.getStateCode(), mContext);
            Log.d(TAG, "Config Detail : " + configList.size() + " State Name : " + selectedStateItem.getStateName());
            ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SELECTED_STATE, selectedStateItem.serialize(), mContext);
            if (configList != null) {
                /*for (ConfigurationItem item1 : configList) {
                    if (item1.getConfigId().equalsIgnoreCase(AppConstant.LOGIN_CONFIG)) {*/
                //configurationItem=item1;

                      /*  if (item1 != null && item1.getStatus().equalsIgnoreCase(AppConstant.LOGIN_STATUS)) {
                            Intent iLoging = new Intent(mContext, NonAdharLoginActivity.class);
                            startActivity(iLoging);
                            //  overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            activity.overridePendingTransition(0, 0);
                            activity.finish();
                        } else {*/
                Intent iLoging = new Intent(mContext, LoginActivity.class);
                startActivity(iLoging);
                // overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                activity.overridePendingTransition(0, 0);
                activity.finish();
                       /* }*/
                // break;
              /*      }
                }*/

            }

        }
    }


    @Override
    public void onAttach(Activity mActivity) {
        super.onAttach(mActivity);
        activity = (LoginActivity) mActivity;
    }
/*
    private void popupForOTP(String mobileNo,String sequenceNo) {
        dialog = new AlertDialog.Builder(context).create();
        LayoutInflater factory = LayoutInflater.from(context);
        View alertView = factory.inflate(R.layout.opt_auth_layout, null);
        dialog.setView(alertView);
        dialog.setCancelable(false);

        // dialog.setContentView(R.layout.opt_auth_layout);
        final TextView otpAuthMsg = (TextView) alertView.findViewById(R.id.otpAuthMsg);
        Button okButton = (Button) alertView.findViewById(R.id.ok);
        Button cancelBT = (Button) alertView.findViewById(R.id.cancelBT);
        final Button resendBT = (Button) alertView.findViewById(R.id.resendBT);
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


        optET = (EditText) alertView.findViewById(R.id.otpET);
        //openSoftinputKeyBoard();
        AppUtility.showSoftInput(activity);
        final TextView inalidOTP = (TextView) alertView.findViewById(R.id.invalidOtpTV);
        optET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // inalidOTP.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        // optET.setText("4040");
      *//*  String mobileNo="XXXX";
        if(loginResponse!=null && loginResponse.getMobileNumber()!=null && loginResponse.getMobileNumber().length()==10) {
          mobileNo = loginResponse.getMobileNumber().substring(6);
        }*//*
        otpAuthMsg.setText(context.getResources().getString(R.string.pleaseEnterAadharOtp) + mobileNo.replace("*", "X"));
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otp = optET.getText().toString();
                if (!otp.equalsIgnoreCase("")) {
                    validateOTP(loginResponse.getUserId(), otp, inalidOTP,sequenceNo);
                } else {
                    inalidOTP.setVisibility(View.VISIBLE);
                }
            }
        });

        resendBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestAadhaarAuth(loginResponse.getUserId());
                dialog.dismiss();

            }
        });
        cancelBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }*/

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
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
                String payload = request.serialize();
                Log.d("Request OTP :", payload);

                try {
                    HashMap<String, String> response = CustomHttp.httpPostWithTokken(AppConstant.REQUEST_FOR_MOBILE_OTP, request.serialize(), AppConstant.AUTHORIZATION, AppConstant.AUTHORIZATIONVALUE);
                    if (response != null) {
                        try {
                            mobileOtpRequestModel = MobileOTPResponse.create(response.get(AppConstant.RESPONSE_BODY));

                        } catch (Exception error) {
                            error.printStackTrace();
                            Toast.makeText(context, "Server not responding/server is down. Please try after some time...", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void updateUI() {
                if (mobileOtpRequestModel != null && mobileOtpRequestModel.getOtp() != null) {
                    popupForOTPValidation(mobileNumber, mobileOtpRequestModel.getSequenceNo());
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

                MobileOtpRequestLoginModel request = new MobileOtpRequestLoginModel();
                request.setMobileNo(mobileNumber);
                request.setOtp(otp);
                // request.setStatus("1");
                request.setSequenceNo(sequenceNo);
                request.setApplicationId(AppConstant.APPLICATION_ID);
                request.setAppVersion(AppUtility.getCurrentApplicationVersion(context));
                //request.setUserName(ApplicationGlobal.MOBILE_Username);
                // request.setUserPass(ApplicationGlobal.MOBILE_Password);
                String payLoad = request.serialize();
                System.out.print(payLoad);
                try {
                    HashMap<String, String> response = CustomHttp.httpPost(AppConstant.REQUEST_FOR_OTP_VERIFICATION_GATEWAY, payLoad);

                    if (response != null) {
                        loginResponse = VerifierLoginResponse.create(response.get(AppConstant.RESPONSE_BODY));
                        //mobileOtpVerifyModel = MobileOTPResponse.create(response.get(AppConstant.RESPONSE_BODY));
                    }
                    SaveLoginTransactionRequestModel logTransReq = new SaveLoginTransactionRequestModel();
                    logTransReq.setCreated_by(loginResponse.getAadhaarNumber());
                    HashMap<String, String> responseTid = CustomHttp.httpPost("https://pmrssm.gov.in/VIEWSTAT/api/login/saveLoginTransaction", logTransReq.serialize());
                    SaveLoginTransactionResponseModel responseModel = SaveLoginTransactionResponseModel.create(responseTid.get("response"));
                    ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, "logTrans", responseModel.serialize(), context);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void updateUI() {
                if (loginResponse != null && loginResponse.isStatus() && loginResponse.getErrorCode() == null) {
                    setPin();
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


}
