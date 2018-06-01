package com.nhpm.fragments;

import android.annotation.SuppressLint;
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
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.customComponent.CustomAlert;
import com.customComponent.CustomAsyncTask;
import com.customComponent.Networking.CustomVolley;
import com.customComponent.Networking.CustomVolleyGet;
import com.customComponent.Networking.VolleyTaskListener;
import com.customComponent.TaskListener;
import com.customComponent.utility.CustomHttp;
import com.customComponent.utility.ProjectPrefrence;
import com.nhpm.AadhaarUtils.Global;
import com.nhpm.AadhaarUtils.UdaiAuthOTPHelper;
import com.nhpm.LocalDataBase.dto.SeccDatabase;
import com.nhpm.Models.ApplicationLanguageItem;
import com.nhpm.Models.request.AadhaarAuthRequestItem;
import com.nhpm.Models.request.LoginRequest;
import com.nhpm.Models.request.RegisterItem;
import com.nhpm.Models.response.master.ConfigurationItem;
import com.nhpm.Models.response.master.StateItem;
import com.nhpm.Models.response.master.response.AppUpdatVersionResponse;
import com.nhpm.Models.response.rsbyMembers.RSBYMemberItemResponse;
import com.nhpm.Models.response.seccMembers.HouseHoldItem;
import com.nhpm.Models.response.verifier.AadhaarDemoAuthResponse;
import com.nhpm.Models.response.verifier.AadhaarOtpResponse;
import com.nhpm.Models.response.verifier.AadhaarResponseItem;
import com.nhpm.Models.response.verifier.VerifierLocationItem;
import com.nhpm.Models.response.verifier.VerifierLoginResponse;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;
import com.nhpm.Utility.AppUtility;
import com.nhpm.Utility.ApplicationGlobal;
import com.nhpm.Utility.ErrorCodeDescriptions;
import com.nhpm.Utility.Verhoeff;
import com.nhpm.activity.AppUpdateActivity;
import com.nhpm.activity.BlockDetailActivity;
import com.nhpm.activity.LoginActivity;
import com.nhpm.activity.OptionActivity;
import com.nhpm.activity.PinLoginActivity;
import com.nhpm.activity.SetPinActivity;
import com.nhpm.activity.SignUpActivity;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import fr.arnaudguyon.xmltojsonlib.XmlToJson;
import pl.polidea.view.ZoomView;

public class AadharLoginFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static String JSON = "";

    // TODO: Rename and change types of parameters
    AadhaarOtpResponse resp;
    private String mParam1;
    private String mParam2;
    private Button submit;
    private Button verifyWithOfflineBT;
    private ArrayList<ApplicationLanguageItem> languageList;
    private View alertView;
    private EditText otp;
    private AutoCompleteTextView adhar;
    private ImageView internet;
    private StateItem selectedStateItem;
    private Button verify;
    private boolean isVeroff;
    private ApplicationLanguageItem appLangItem;
    private AlertDialog dialog;
    private Context context;
    private TextView appVersionTV, login_title;
    private VerifierLoginResponse loginResponse, storedLoginResponse;
    private LoginRequest request;
    private static final String TAG = "LOGIN ACTIVITY";
    private CheckBox termsCB;
    private TextView releaseDateTV;
    private String[] aadhaarNumber = new String[1];
    private LinearLayout toolTipLayout;
    private CustomAsyncTask asyncTask;
    private LinearLayout mZoomLinearLayout;
    private ZoomView zoomView;
    private AadhaarDemoAuthResponse aadhaarAuthRespItem;
    private AadhaarResponseItem aadhaarKycResponse;
    //  private  EditText optET;
    private static EditText optET;
    private LoginActivity activity;
    String seccDataDownloaded;
    private String flagUserConsent = "N";
    private Boolean isKycEnabled = AppConstant.isKyCEnabled;
    private Button notRegisterYet;


    public AadharLoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AadharLoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AadharLoginFragment newInstance(String param1, String param2) {
        AadharLoginFragment fragment = new AadharLoginFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_login, container, false);
        setupScreen(view);
        searchMenu(view);

        return view;
    }

    private void setupScreen(View v) {
        context = getActivity();
        selectedStateItem = StateItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SELECTED_STATE, context));
        showNotification(v);
        updateTable();
        storedLoginResponse = VerifierLoginResponse.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.VERIFIER_CONTENT, context));
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
                        internet.setImageDrawable(context.getResources().getDrawable(R.drawable.green_like));
                    } else {
                        internet.setImageDrawable(context.getResources().getDrawable(R.drawable.red_like));
                    }
                    handler.postDelayed(this, 5);
                }

            };
            handler.postDelayed(runnable, 5);
        } catch (Exception ex) {
        }
        submit = (Button) v.findViewById(R.id.submit);
        notRegisterYet= (Button) v.findViewById(R.id.notRegisterYet);
        toolTipLayout = (LinearLayout) v.findViewById(R.id.toolTipLayout);
        termsCB = (CheckBox) v.findViewById(R.id.termsCB);
        LayoutInflater factory = LayoutInflater.from(activity);
        alertView = factory.inflate(R.layout.custom_alert, null);
        verifyWithOfflineBT = (Button) v.findViewById(R.id.offlineLoginBT);
        appVersionTV = (TextView) v.findViewById(R.id.versionTV);
        otp = (EditText) alertView.findViewById(R.id.otp);
        adhar = (AutoCompleteTextView) v.findViewById(R.id.adhar);
        adhar.requestFocus();
        login_title = (TextView) v.findViewById(R.id.login_title);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, aadhaarNumber);
        adhar.setThreshold(1);
        adhar.setAdapter(adapter);
        releaseDateTV = (TextView) v.findViewById(R.id.releaseDateTV);
        VerifierLoginResponse verifierDetail = VerifierLoginResponse.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.VERIFIER_CONTENT, context));
        if (verifierDetail != null && verifierDetail.getLocationList() != null && verifierDetail.getLocationList().size() > 0) {
            VerifierLocationItem locationItem = verifierDetail.getLocationList().get(0);

         /*   long saveHouseholdCount = SeccDatabase.houseHoldVillageCountSeccOnLy(context, locationItem.getStateCode(), locationItem.getDistrictCode(), locationItem.getTehsilCode(), locationItem.getVtCode());
            long saveMembersCount = SeccDatabase.seccMemberVillageCountSeccOnly(context, locationItem.getStateCode(), locationItem.getDistrictCode(), locationItem.getTehsilCode(), locationItem.getVtCode());
            long downloadedHouseholdCount = Long.parseLong(SeccDatabase.getDataCount(context).getSeccHouseholdCount());
            long downloadedMemberCount = Long.parseLong(SeccDatabase.getDataCount(context).getSeccMemberCount());
            if (saveHouseholdCount != downloadedHouseholdCount || saveMembersCount != downloadedMemberCount) {
                removePhoneKeypad(v);
                //  AppUtility.hideSoftInput(activity);
                // alertWithOk(context,"Data downloaded partially.\nClear the device  ");
            } else {
                AppUtility.showSoftInput(activity);
            }*/

        }

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
        adhar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isVeroff = Verhoeff.validateVerhoeff(s.toString());
                adhar.setTextColor(Color.BLACK);
                if (s.toString().length() > 11) {
                    if (!Verhoeff.validateVerhoeff(s.toString())) {
                        adhar.setTextColor(Color.RED);
                    } else {
                        adhar.setTextColor(Color.GREEN);
                        AppUtility.softKeyBoard(activity, 0);
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        if (storedLoginResponse == null) {
            verifyWithOfflineBT.setVisibility(View.GONE);
        }
        if (storedLoginResponse != null && storedLoginResponse.getPin().equalsIgnoreCase("")) {
            verifyWithOfflineBT.setVisibility(View.GONE);
        }
        if (storedLoginResponse != null && !storedLoginResponse.getPin().equalsIgnoreCase("")) {
            verifyWithOfflineBT.setVisibility(View.VISIBLE);
        }

        seccDataDownloaded = ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.dataDownloaded, context);

        if (seccDataDownloaded != null) {
            if (seccDataDownloaded.equalsIgnoreCase("N")) {

            } else {
                if (SeccDatabase.houseHoldCount(context) == 0) {
                    verifyWithOfflineBT.setVisibility(View.GONE);
                }
            }
        } else {
            if (SeccDatabase.houseHoldCount(context) == 0) {
               // verifyWithOfflineBT.setVisibility(View.GONE);
            }
        }
        notRegisterYet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupForRegister();
            }
        });
        verifyWithOfflineBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedStateItem != null && selectedStateItem.getStateCode() != null) {
                    AppUtility.hideSoftInput(activity, verifyWithOfflineBT);
                    if (!isVeroff) {

                        CustomAlert.alertWithOk(context, getResources().getString(R.string.invalid_login));

                    } else if (!termsCB.isChecked()) {
                        CustomAlert.alertWithOk(context, context.getResources().getString(R.string.login_ekyc_check));
                    } else if (storedLoginResponse != null) {
                        if (storedLoginResponse.getAadhaarNumber() != null && !storedLoginResponse.getAadhaarNumber().equalsIgnoreCase("")) {
                            if (!storedLoginResponse.getAadhaarNumber().equalsIgnoreCase(adhar.getText().toString().trim())) {
                               /* if (SeccDatabase.houseHoldCount(context) > 0) {
                                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.ebNotMaching));
                                } else {
                                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.dataNotDownload));//getResources().getString(R.string.invalid_login));
                                }*/
                                if (seccDataDownloaded != null) {
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
                                }

                            } else {
                                pinOfflineLogin();
                            }
                        } else {
                            // CustomAlert.alertWithOk(context, getResources().getString(R.string.invalid_login));
                        }
                  /*  if (adhar.getText().toString().equalsIgnoreCase(storedLoginResponse.getAadhaarNumber())) {

                    }else{
                        CustomAlert.alertWithOk(context, getResources().getString(R.string.invalid_login));

                    }*/

                    }
                } else {
                    AppUtility.alertWithOk(context, "Please select state");
                }
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //setPin();
              /*  Intent intent = new Intent(context , BlockDetailActivity.class);
                startActivity(intent);*/
                if (selectedStateItem != null && selectedStateItem.getStateCode() != null) {
                    AppUtility.hideSoftInput(activity, submit);
                    String adharNo = adhar.getText().toString();
                    if (!isVeroff) {
                        CustomAlert.alertWithOk(context, context.getResources().getString(R.string.enterValidAadhaar));
                    } else if (!termsCB.isChecked()) {
                        CustomAlert.alertWithOk(context, context.getResources().getString(R.string.login_ekyc_check));
                    } else {
                        request = new LoginRequest();
                        request.setAadhaarNumber(adharNo);
                        //request.setLoginType(AppConstant.LOGIN_TYPE_AADHAR);
                        request.setImeiNo1(AppUtility.getIMEINumber(context));
                        request.setAppVersion(AppUtility.getCurrentApplicationVersion(context));
                        if (activity.isNetworkAvailable()) {
                            if (storedLoginResponse != null) {
                                if (storedLoginResponse.getAadhaarNumber() != null && !storedLoginResponse.getAadhaarNumber().equalsIgnoreCase("")) {
                                    if (!storedLoginResponse.getAadhaarNumber().equalsIgnoreCase(adharNo.trim())) {
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
                            if (location != null) {
                                houseHoldList = SeccDatabase.getSeccHouseHoldList(location.getStateCode(), location.getDistrictCode(),
                                        location.getTehsilCode(), location.getVtCode(), location.getWardCode(), location.getBlockCode(), context);
                            }else{
                                    houseHoldList = SeccDatabase.getAllHouseHold(context);
                            }
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
                        // rsbyResponse();

                    }

                } else {
                    AppUtility.alertWithOk(context, "Please select state");
                }
            }
        });

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

            // open another activity
            Intent theIntent = new Intent(context, BlockDetailActivity.class);
            //Intent theIntent = new Intent(context, BlockDetailActivity.class);
            startActivity(theIntent);
            activity.finish();
            activity.leftTransition();

        }
        //checkUpdatedVersion();
    }

    private void pinOfflineLogin() {
        AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Login Response : " + storedLoginResponse.serialize());
        String aadhaarNumber = adhar.getText().toString();
        if (aadhaarNumber.equalsIgnoreCase("")) {
            CustomAlert.alertWithOk(context, context.getResources().getString(R.string.pleaseEnterAadhaar));
        } else if (storedLoginResponse != null && storedLoginResponse.getAadhaarNumber() != null && !aadhaarNumber.equalsIgnoreCase(storedLoginResponse.getAadhaarNumber())) {
            if (SeccDatabase.houseHoldCount(context) > 0 && SeccDatabase.seccMemberCount(context) > 0) {

                CustomAlert.alertWithOk(context, context.getResources().getString(R.string.ebNotMaching));

            } else if (SeccDatabase.houseHoldCount(context) == 0 && SeccDatabase.seccMemberCount(context) == 0) {
                CustomAlert.alertWithOk(context, context.getResources().getString(R.string.pleaseDownloadEb));
            } else {
                Intent theIntent = new Intent(context, PinLoginActivity.class);
                startActivity(theIntent);
                activity.leftTransition();
                activity.finish();
            }
        } else if (!termsCB.isChecked()) {
            CustomAlert.alertWithOk(context, context.getResources().getString(R.string.login_ekyc_check));
        } else {
            if (storedLoginResponse == null) {
                CustomAlert.alertWithOk(context, "Your session has been expired, Please login online.");
                return;
            } /*else if (storedLoginResponse != null && !storedLoginResponse.isLoginSession()) {
                CustomAlert.alertWithOk(context, "Your session has been expired, Please login online.");
            }*/ /*else if (SeccDatabase.houseHoldCount(context) > 0 && SeccDatabase.seccMemberCount(context) > 0) {
                Intent theIntent = new Intent(context, PinLoginActivity.class);
                startActivity(theIntent);
                activity.leftTransition();
                activity.finish();*/
                Intent theIntent = new Intent(context, PinLoginActivity.class);
                startActivity(theIntent);
                activity.leftTransition();
                activity.finish();
               /* if (seccDataDownloaded != null && seccDataDownloaded.equalsIgnoreCase("N")) {
                    Intent theIntent = new Intent(context, PinLoginActivity.class);
                    startActivity(theIntent);
                    activity.leftTransition();
                    activity.finish();
                } else {
                    CustomAlert.alertWithOk(context,
                            "Data is not downloaded to validate," +
                                    "Please login with Aadhaar OTP and download data");
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
        Log.e(TAG, "Login request : " + request.serialize() + " : URL : " + AppConstant.LOGIN_API);
        VolleyTaskListener taskListener = new VolleyTaskListener() {
            @Override
            public void postExecute(String response) {
                Log.e(TAG, "Login Response : " + response.toString());
                loginResponse = VerifierLoginResponse.create(response);
                if (loginResponse != null) {
                    if (loginResponse.isStatus()) {
                        // if(!loginResponse.getAadhaarNumber().equalsIgnoreCase(adhar.getText().toString())) {
                        if (loginResponse.getUserStatus() != null && loginResponse.getUserStatus().trim().equalsIgnoreCase(AppConstant.VALID_STATUS)) {
                            if (loginResponse.getRole() != null && loginResponse.getRole().equalsIgnoreCase(AppConstant.user_status)) {
                                validateStateAndData();
                            } else {
                                CustomAlert.alertWithOk(context, context.getResources().getString(R.string.Youare) + loginResponse.getRole() + context.getResources().getString(R.string.levelUser));
                            }
                        } else {
                            CustomAlert.alertWithOk(context, context.getResources().getString(R.string.inactiveUser));
                        }
                    } else {
                        if (loginResponse != null && loginResponse.getErrorCode() != null && loginResponse.getErrorCode().equalsIgnoreCase(AppConstant.invalid_user)) {
                            CustomAlert.alertWithOk(context, context.getResources().getString(R.string.aadhar_not_register));
                        } else if (loginResponse != null && loginResponse.getErrorCode() != null && loginResponse.getErrorCode().trim().equalsIgnoreCase(AppConstant.invalid_imei)) {
                            CustomAlert.alertWithOk(context, loginResponse.getErrorMessage());
                        }
                    }
                }
            }

            @Override
            public void onError(VolleyError error) {
                CustomAlert.alertWithOk(context, getResources().getString(R.string.slow_internet_connection_msg));
            }
        };
        AppUtility.writeFileToStorage(request.serialize(), "loginPayload");
        AppUtility.writeFileToStorage(AppConstant.LOGIN_API, "loginApi");
        CustomVolley volley = new CustomVolley(taskListener, context.getResources().getString(R.string.pleaseWait), AppConstant.LOGIN_API, request.serialize(), AppConstant.AUTHORIZATION, AppConstant.AUTHORIZATIONVALUE, context);
        volley.execute();
    }

    private void requestAadhaarAuth(String aadhaarNo, final String txn) {
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
                            popupForOTP(requiredString, txn);
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
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                } else if (error instanceof AuthFailureError) {
                    //TODO
                } else if (error instanceof ServerError) {
                    //TODO
                } else if (error instanceof NetworkError) {
                    //TODO
                } else if (error instanceof ParseError) {
                    //TODO
                }

                NetworkResponse response = error.networkResponse;
                if (response != null && response.data != null) {
                    System.out.print(response.statusCode);
                    System.out.print(response.data);
                }
                //  popupForOTP();
            }
        };
        String url = AppConstant.AADHAR_OTP_AUTH_API_NEW;
        AadhaarAuthRequestItem aadhaarReq = new AadhaarAuthRequestItem();
        aadhaarReq.setUid(aadhaarNo);
        String imei = AppUtility.getIMEINumber(context);
        if (imei != null) {
            aadhaarReq.setImeiNo(imei);
        }
        aadhaarReq.setIpAddress("NA");
        aadhaarReq.setProject(AppConstant.PROJECT_NAME);
        aadhaarReq.setUserName(ApplicationGlobal.AADHAAR_AUTH_USERNAME);
        aadhaarReq.setUserPass(ApplicationGlobal.AADHAAR_AUTH_ENCRIPTED_PASSWORD);
        AppUtility.showLog(AppConstant.LOG_STATUS, TAG, " Login Response otp request : " + aadhaarReq.serialize() + " : API :" + url);
        String payLoad = aadhaarReq.serialize();
        CustomVolley volley = new CustomVolley(taskListener, context.getResources().getString(R.string.pleaseWait), AppConstant.AADHAR_OTP_AUTH_API_NEW, payLoad, null, null, context);
        volley.execute();
        Log.d(TAG, "OTP Request API : " + url);

    }

    private void requestAadhaarOtpAuth(final String aadhaarNo) {

        TaskListener taskListener = new TaskListener() {
            @Override
            public void execute() {
                Global.VALIDATORAADHAR = aadhaarNo;
                Global.USER_NAME = ApplicationGlobal.USER_NAME;
                Global.imei = AppUtility.getIMEINumber(context);
                Global.USER_PASSWORD = ApplicationGlobal.USER_PASSWORD;
                String payLoad = AppUtility.getAadhaarOtpRequestXml(aadhaarNo);
                System.out.print(payLoad);
                Log.e("OTP REQ BODY", payLoad);
                try {
                    HashMap<String, String> response = CustomHttp.httpPostAadhaar(AppConstant.AADHAAR_REQUEST_FOR_OTP, payLoad, AppConstant.AUTHORIZATION, AppConstant.AUTHORIZATIONVALUE);
                    Log.e("OTP RESP BODY", response.toString());

                    if (response != null) {
                        resp = AadhaarOtpResponse.create(response.get(AppConstant.RESPONSE_BODY));
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void updateUI() {
                if (resp != null) {
                    if (resp.getRet() != null) {
                        if (resp.getRet().trim().equalsIgnoreCase(AppConstant.VALID_STATUS)) {
                            String stringContainingNumber = resp.getInfo();
                            System.out.print(stringContainingNumber);

                            String requiredString = getMobileNumber(resp.getInfo());//resp.getInfo().substring(resp.getInfo().indexOf("*"), resp.getInfo().indexOf(",NA"));
                            String txn = resp.getTxn();
                            popupForOTP(requiredString, txn);
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
        };
        if (asyncTask != null) {
            asyncTask.cancel(true);
            asyncTask = null;
        }

        asyncTask = new CustomAsyncTask(taskListener, context.getResources().getString(R.string.please_wait), context);
        asyncTask.execute();
    }

    private String getMobileNumber(String data) {
        String moblieNumber = "";
        String[] stringArray;
        stringArray = data.split(",");
        if (stringArray != null && stringArray.length > 0) {
            for (int k = 0; k < stringArray.length; k++) {

                if (stringArray[k].length() == 11 && countOccurrences(stringArray[k], "*") == 7) {
                    moblieNumber = stringArray[k];
                }
            }
        } else {
            moblieNumber = "**********";
        }
        return moblieNumber;
    }


    public static int countOccurrences(String str, String needle) {
        String[] haystack = str.split("");
        int count = 0;
        for (int i = 0; i < haystack.length; i++) {
            if (haystack[i].equalsIgnoreCase(needle)) {
                count++;
            }
        }
        return count;
    }

    private void validateOTP(final String aadhaarNo, final String otp, final TextView errorTV, final String txn, final String userConsent) {

        TaskListener taskListener = new TaskListener() {
            @Override
            public void execute() {
//                String url = AppConstant.AADHAAR_KYC_DATA_API_NEW;
                AadhaarAuthRequestItem aadhaarReq = new AadhaarAuthRequestItem();
                aadhaarReq.setUid(aadhaarNo.trim());
                String imei = AppUtility.getIMEINumber(context);
                if (imei != null) {
                    aadhaarReq.setImeiNo(imei);
                }
                aadhaarReq.setOtp(otp);
                aadhaarReq.setIpAddress("NA");
                aadhaarReq.setProject(AppConstant.PROJECT_NAME);
                aadhaarReq.setUserName(ApplicationGlobal.AADHAAR_AUTH_USERNAME);
                aadhaarReq.setUserPass(ApplicationGlobal.AADHAAR_AUTH_ENCRIPTED_PASSWORD);

                // AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Login Response otp request "+aadhaarReq.serialize()+" : API :"+url);
                String payLoad = getKYCAuthXml(aadhaarNo, otp, txn, userConsent);
                System.out.print("VERIFY OTP XML" + payLoad);
//                Log.e("VERIFY REQ OTP XML",payLoad);
                try {
                    HashMap<String, String> response;


                    if (isKycEnabled) {

//                        Log.e("httpPostAadhaarURL", AppConstant.REQUEST_FOR_OTP_EKYC);
//                        Log.e("httpPostAadhaarREQBODY", payLoad);
//                        Log.e("httpPostAadhaarTOKEN", AppConstant.AUTHORIZATION);
//                        Log.e("httpPostAadhaarTVALUE", AppConstant.AUTHORIZATIONVALUE);

                        response = CustomHttp.httpPostAadhaar(AppConstant.REQUEST_FOR_OTP_EKYC, payLoad, AppConstant.AUTHORIZATION, AppConstant.AUTHORIZATIONVALUE);

//                        Log.e("VERIFY RESP Ver XML KYC", response.toString());

                        if (response != null) {
                            try {
                                aadhaarKycResponse = new AadhaarResponseItem().create(response.get(AppConstant.RESPONSE_BODY));
                            } catch (Exception ex) {

                            }
                        }
                    } else {


//                        Log.e("HIT URL OPTVERIFY AUTH",AppConstant.REQUEST_FOR_OTP_AUTH);

//                        Log.e("httpPostAadhaarURL", AppConstant.REQUEST_FOR_OTP_AUTH);
//                        Log.e("httpPostAadhaarREQBODY", payLoad);
//                        Log.e("httpPostAadhaarTOKEN", AppConstant.AUTHORIZATION);
//                        Log.e("httpPostAadhaarTVALUE", AppConstant.AUTHORIZATIONVALUE);
//
                        response = CustomHttp.httpPostAadhaar(AppConstant.REQUEST_FOR_OTP_AUTH, payLoad, AppConstant.AUTHORIZATION, AppConstant.AUTHORIZATIONVALUE);
                        Log.e("VERIFY RESP Ver XML!KYC", response.toString());

                        if (response != null) {
                            try {
                                XmlToJson xmlToJson = new XmlToJson.Builder(response.get(AppConstant.RESPONSE_BODY)).build();
                                JSON = xmlToJson.toString();
                            } catch (Exception ex) {
                                System.out.print(ex.toString());
                            }
                            System.out.println(JSON);
                            aadhaarAuthRespItem = AadhaarDemoAuthResponse.create(JSON);
                        }
                    }
                } catch (Exception e) {

                }

            }

            @Override
            public void updateUI() {

                if (isKycEnabled) {
                    if (aadhaarKycResponse != null) {
                        if (aadhaarKycResponse.getResult() != null && aadhaarKycResponse.getResult().equalsIgnoreCase("Y")) {
                            loginResponse.setAadhaarItem(aadhaarKycResponse);
                            ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.VERIFIER_CONTENT, loginResponse.serialize(), context);
                            setPin();
                        } else if (aadhaarKycResponse.getResult() != null && aadhaarKycResponse.getResult().equalsIgnoreCase("N")) {
                            if (aadhaarKycResponse.getErr() != null && !aadhaarKycResponse.getErr().equalsIgnoreCase("")) {
                                errorTV.setVisibility(View.VISIBLE);
                                errorTV.setText(" Error code: "
                                        + aadhaarKycResponse.getErr() + " ("
                                        + ErrorCodeDescriptions.getDescription(aadhaarKycResponse.getErr())
                                        + ")");
//                                errorTV.setText(aadhaarKycResponse.getErr());
                                optET.setText("");
                            }
                        } else {
                            errorTV.setVisibility(View.VISIBLE);
                            optET.setText("");
                        }
                    } else {
                        CustomAlert.alertWithOk(context, getResources().getString(R.string.aadhaar_connect_error));
                    }
                } else {

                    if (aadhaarAuthRespItem != null && aadhaarAuthRespItem.getAuthRes() != null) {
                        if (aadhaarAuthRespItem.getAuthRes().getRet() != null && aadhaarAuthRespItem.getAuthRes().getRet().equalsIgnoreCase("Y")) {
                            ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.VERIFIER_CONTENT, loginResponse.serialize(), context);
                            setPin();
                        } else if (aadhaarAuthRespItem.getAuthRes().getRet() != null && aadhaarAuthRespItem.getAuthRes().getRet().equalsIgnoreCase("N")) {
                            if (aadhaarAuthRespItem.getAuthRes().getErr() != null && !aadhaarAuthRespItem.getAuthRes().getErr().equalsIgnoreCase("")) {
                                errorTV.setVisibility(View.VISIBLE);
//                                errorTV.setText(aadhaarAuthRespItem.getAuthRes().getErr());
                                errorTV.setText(" Error code: "
                                        + aadhaarAuthRespItem.getAuthRes().getErr() + " ("
                                        + ErrorCodeDescriptions.getDescription(aadhaarAuthRespItem.getAuthRes().getErr())
                                        + ")");
                                optET.setText("");
                            }
                        } else {
                            errorTV.setVisibility(View.VISIBLE);
                            optET.setText("");
                        }
                    } else {
                        //  setPin();
                        CustomAlert.alertWithOk(context, getResources().getString(R.string.aadhaar_connect_error));
                    }
                }
            }
        };
        if (asyncTask != null) {
            asyncTask.cancel(true);
            asyncTask = null;
        }

        asyncTask = new CustomAsyncTask(taskListener, context.getResources().getString(R.string.please_wait), context);
        asyncTask.execute();
        /*CustomVolley volley=new CustomVolley(taskListener,"Please Wait..",AppConstant.AADHAR_OTP_AUTH_API_NEW,aadhaarReq.serialize(),null,null,context);
        volley.execute();*/
    }

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
                    "vl_aadharNo character varying(99)," +
                    "vl_stateCode character varying(99)," +
                    "vl_districtCode character varying(99)," +
                    "vl_tehsilCode character varying(99)," +
                    "vl_vtCode character varying(99)," +
                    "vl_wardCode character varying(99)," +
                    "vl_blockCode character varying(99)," +
                    "vl_subBlockcode character varying(99)," +
                    "vl_ruralUrban character varying(99)," +
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


    private void searchMenu(View view) {
        RelativeLayout menuLayout = (RelativeLayout) view.findViewById(R.id.menuLayout);
        menuLayout.setVisibility(View.VISIBLE);
        ArrayList<String> spinnerListState = new ArrayList<>();
        final Spinner appLanguageSpinner = (Spinner) view.findViewById(R.id.appLanguageText);
        final Spinner stateSelection = (Spinner) view.findViewById(R.id.stateSelection);
        stateSelection.setVisibility(View.GONE);
        final ArrayList<StateItem> stateList = SeccDatabase.findStateList(context);
        selectedStateItem = StateItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SELECTED_STATE, context));
        if (selectedStateItem != null && selectedStateItem.getStateName() != null) {
            login_title.setText(context.getResources().getString(R.string.login) + " (" + selectedStateItem.getStateName() + ")");
        } else {
          /*  selectedStateItem.setStateName("Gujrat");
            selectedStateItem.setStateCode("24");*/
            login_title.setText(context.getResources().getString(R.string.login));
        }
        int position = 0;
        stateList.add(0, new StateItem("00", "Select State"));
        if (stateList != null) {
            for (StateItem item1 : stateList) {
                spinnerListState.add(item1.getStateName());
            }
            if (selectedStateItem != null && selectedStateItem.getStateCode() != null) {
                for (int i = 0; i < stateList.size(); i++) {
                    if (stateList.get(i).getStateCode().equalsIgnoreCase(selectedStateItem.getStateCode())) {
                        position = i + 1;
                    }
                }
            }
        }
        stateSelection.setSelection(position);
        ArrayAdapter<String> adapterState = new ArrayAdapter<>(activity, R.layout.custom_lang_dropdown, R.id.textView, spinnerListState);
        stateSelection.setAdapter(adapterState);
        if ((SeccDatabase.houseHoldCount(context) > 0)) {
            stateSelection.setVisibility(View.GONE);
        }

        languageList = AppUtility.prepareLanguageSpinner(context);
        ArrayList<String> spinnerList = new ArrayList<String>();

        for (ApplicationLanguageItem item : languageList) {
            spinnerList.add(item.languageName);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(activity, R.layout.custom_lang_dropdown, R.id.textView, spinnerList);
        appLanguageSpinner.setAdapter(adapter);
        /*final Drawable upArrow = getResources().getDrawable(R.drawable.menu_icon);
        upArrow.setColorFilter(getResources().getColor(R.color.sky_shign), PorterDuff.Mode.SRC_ATOP);
        settings.setImageDrawable(upArrow);*/
        //  settings.setVisibility(View.GONE);
      /*  menuLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settings.performClick();
            }
        });*/
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


    private void loginNavigation(StateItem selectedStateItem) {
        if (selectedStateItem != null) {
            ArrayList<ConfigurationItem> configList = SeccDatabase.findConfiguration(selectedStateItem.getStateCode(), context);
            Log.d(TAG, "Config Detail : " + configList.size() + " State Name : " + selectedStateItem.getStateName());
            ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SELECTED_STATE, selectedStateItem.serialize(), context);
            if (configList != null) {
                for (ConfigurationItem item1 : configList) {
                    if (item1.getConfigId().equalsIgnoreCase(AppConstant.LOGIN_CONFIG)) {
                        //configurationItem=item1;

                       /* if (item1 != null && item1.getStatus().equalsIgnoreCase(AppConstant.LOGIN_STATUS)) {
                            Intent iLoging = new Intent(mContext, NonAdharLoginActivity.class);
                            startActivity(iLoging);
                            //  overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            activity.overridePendingTransition(0, 0);
                            activity.finish();
                        } else {*/
                        Intent iLoging = new Intent(context, LoginActivity.class);
                        startActivity(iLoging);
                        // overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        activity.overridePendingTransition(0, 0);
                        activity.finish();
                       /* }*/
                        break;
                    }
                }

            }

        }
    }

    private void popupForOTP(String mobileNo, final String txn) {
        dialog = new AlertDialog.Builder(context).create();
        LayoutInflater factory = LayoutInflater.from(context);
        View alertView = factory.inflate(R.layout.opt_auth_layout, null);
        dialog.setView(alertView);
        dialog.setCancelable(false);

        // dialog.setContentView(R.layout.opt_auth_layout);
        final TextView otpAuthMsg = (TextView) alertView.findViewById(R.id.otpAuthMsg);
        final Button okButton = (Button) alertView.findViewById(R.id.ok);
        okButton.setEnabled(false);
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

            @SuppressLint("NewApi")
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

            @SuppressLint("NewApi")
            public void onFinish() {

                okButton.setEnabled(true);
                okButton.setBackground(context.getResources().getDrawable(R.drawable.button_background_orange_ehit));
                okButton.setTextColor(context.getResources().getColor(R.color.white));

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
      /*  String mobileNo="XXXX";
        if(loginResponse!=null && loginResponse.getMobileNumber()!=null && loginResponse.getMobileNumber().length()==10) {
          mobileNo = loginResponse.getMobileNumber().substring(6);
        }*/
        otpAuthMsg.setText(context.getResources().getString(R.string.pleaseEnterAadharOtp) + " " + mobileNo.replace("*", "X"));
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otp = optET.getText().toString();
                if (!otp.equalsIgnoreCase("")) {

                    if (termsCB.isChecked()) {
                        flagUserConsent = "Y";
                    } else {
                        flagUserConsent = "N";
                    }
                    validateOTP(loginResponse.getAadhaarNumber(), otp, inalidOTP, txn, flagUserConsent);
                } else {
                    inalidOTP.setVisibility(View.VISIBLE);
                }
               /* otpAuthMsg.setVisibility(View.GONE);
                if (otp.equalsIgnoreCase("4040")) {
                    updatedVersionApp();
                    // pinOfflineLogin();
                    dialog.dismiss();
                } else {
                    otpAuthMsg.setVisibility(View.VISIBLE);
                }*/
            }
        });

        resendBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   requestAadhaarAuth(loginResponse.getAadhaarNumber());
                requestAadhaarOtpAuth(loginResponse.getAadhaarNumber());
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
    }

    private void popupForRegister() {
        dialog = new AlertDialog.Builder(context).create();
        LayoutInflater factory = LayoutInflater.from(context);
        View alertView = factory.inflate(R.layout.register_option_layout, null);
        dialog.setView(alertView);
        dialog.setCancelable(true);

        // dialog.setContentView(R.layout.opt_auth_layout);
        Button withAadhaarBT = (Button) alertView.findViewById(R.id.withAadhaarBT);
        Button withoutAadhaarBT = (Button) alertView.findViewById(R.id.withoutAadhaarBT);
        withoutAadhaarBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent theIntent = new Intent(context, SignUpActivity.class);
                startActivity(theIntent);
                activity.leftTransition();
                RegisterItem item=new RegisterItem();
                item.setRegisterMode(AppConstant.NON_AADHAAR_SEARCH);
                ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_NAME,
                        AppConstant.REGISTER_MODE,item.serialize(),context);
            }
        });
        dialog.show();
    }
    @Override
    public void onAttach(Activity mActivity) {
        super.onAttach(mActivity);
        activity = (LoginActivity) mActivity;
    }


    public void removePhoneKeypad(View view) {
        try {
            InputMethodManager inputManager = (InputMethodManager) view
                    .getContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);

            IBinder binder = view.getWindowToken();
            inputManager.hideSoftInputFromWindow(binder,
                    InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception ex) {

        }
    }

    private void validateStateAndData() {
        if (selectedStateItem != null && selectedStateItem.getStateCode() != null && loginResponse.getLocationList() != null && loginResponse.getLocationList().size() > 0) {
            if (!selectedStateItem.getStateCode().equalsIgnoreCase(loginResponse.getLocationList().get(0).getStateCode())) {
                alertWithOk(context, "You are not authorised user for " + selectedStateItem.getStateName());
            } else {
            /*    // open another activity
                Intent theIntent = new Intent(context, OptionActivity.class);
                startActivity(theIntent);*/
            setPin();
               // requestAadhaarOtpAuth(loginResponse.getAadhaarNumber());
                loginResponse.setLoginSession(true);
                // setPin();
            }
        } else {
            AppUtility.alertWithOk(context, context.getResources().getString(R.string.locationNotAllocate));
        }

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


    //	to create KYC auth/encoded Rad xml
    public String getKYCAuthXml(String aadhaar_no, String otp, String txnID, String userConsent) {
        String kycXml = "";

        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String IMEINumber = tm.getDeviceId();
        Global.imei = IMEINumber;

                byte[] publicKey = null;
        //		GetPubKeycertificateData();
        publicKey = Base64.decode(Global.productionPublicKey,Base64.DEFAULT);

        Log.e("FINAL PUB KEY:","=======================>>>>>>>>>>"+Global.productionPublicKey );

        UdaiAuthOTPHelper helper = new UdaiAuthOTPHelper(publicKey);
        if (isKycEnabled) {
            Log.e("IS KYC", "TRUE") ;
            kycXml = helper.createXmlForKycAuthByRaj("", aadhaar_no, "", true, otp, txnID, userConsent);
            kycXml = kycXml.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>");

        } else {

            Log.e("IS KYC", "FALSE") ;
            //by rajesh modification
            kycXml = helper.createXmlForKycAuthByRaj("", aadhaar_no, "", true, otp,txnID,userConsent);
            kycXml = kycXml.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>");
        }

        return kycXml;

    }


}
