package com.nhpm.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.Formatter;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.customComponent.CustomAlert;
import com.customComponent.CustomAsyncTask;
import com.customComponent.TaskListener;
import com.customComponent.utility.CustomHttp;
import com.customComponent.utility.ProjectPrefrence;
import com.nhpm.AadhaarUtils.CheckConnection;
import com.nhpm.AadhaarUtils.Global;
import com.nhpm.AadhaarUtils.UdaiAuthOTPHelper;
import com.nhpm.AadhaarUtils.VerhoeffAadhar;
import com.nhpm.Models.request.AadhaarAuthRequestItem;
import com.nhpm.Models.request.PersonalDetailItem;
import com.nhpm.Models.response.seccMembers.SeccMemberItem;
import com.nhpm.Models.response.seccMembers.SelectedMemberItem;
import com.nhpm.Models.response.verifier.AadhaarDemoAuthResponse;
import com.nhpm.Models.response.verifier.AadhaarOtpResponse;
import com.nhpm.Models.response.verifier.AadhaarResponseItem;
import com.nhpm.Models.response.verifier.VerifierLoginResponse;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;
import com.nhpm.Utility.AppUtility;
import com.nhpm.Utility.ApplicationGlobal;
import com.nhpm.Utility.ErrorCodeDescriptions;
import com.nhpm.activity.CaptureAadharDetailActivity;
import com.nhpm.activity.EkycActivity;
import com.nhpm.activity.FingerprintResultActivity;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.HashMap;

import fr.arnaudguyon.xmltojsonlib.XmlToJson;

import static android.content.Context.WIFI_SERVICE;


public class OTPFragment extends Fragment implements View.OnClickListener {

    private Boolean isKycEnabled = true;
    private EditText edtxt_Aadhaar;
    private AlertDialog dialog;
    private CheckBox aadharConsetCB;
    private PersonalDetailItem personalDetailItem;
    private CustomAsyncTask asyncTask;
    private static String JSON = "";
    private AadhaarDemoAuthResponse aadhaarAuthRespItem;
    private boolean validAadhaar = false;
    private View view;
    private static EditText optET;
    private String consent = "Y";
    private CaptureAadharDetailActivity activity;
    private Button cancelEkyc, auth_demo_go;
    private TextView aadharNumResetEditText;
    private Context context;
    // RD Service values
    private String deviceType = "F";
    boolean e_KYC = true;
    private String mywadh = "";
    String cert_type = "PP";
    private String pidFormate = "0";
    private String final_XML = "";
    private Boolean demographic = false;
    private String ra = "";
    private String rc = "Y";
    private String lr = "N";
    private String de = "N";
    private String pfr = "N";
    private String dpid, Skey, Hmac, Data, Datatype;
    private String ci;
    private String deviceMake = "";
    private String serialNumber = "";
    private String deviceModel = "";
    private String rdsId;
    private String rdsVer;
    private String dc;
    private String mc;
    public String DeviceInfoXml = "";
    public String pidDataXML = "";
    private String IPaddress = "";
    private TextView txtView_info;
    private String kyc_ver = "2.1";
    private Intent intentCapture;
    private Intent intentInfo;
    private static int capture_finger = 1;
    private static final int EXTERNAL_STORAGE_PERMISSION_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    private CheckConnection checkConnection;
    private long startTime;
    private AadhaarResponseItem aadhaarKycResponse;
    private LinearLayout kycDetailLayoutNew, kycErrorLayout;
    private long endTime;
    private Button updateKycButtonNew, cancelButtonNew, errorCancelButton;
    private long totalTime;
    private ImageView kycImageView;
    private CardView card_view1;
    private SeccMemberItem seccItem;
    private SelectedMemberItem selectedMemItem;
    private AlertDialog internetDiaolg;
    private VerifierLoginResponse loginResponse;
    private boolean checkNetwork;
    private TextView kycName, kycDob, kycGender, kycEmail, kycPhone, kycCareOf, kycAddr, kycTs, kycTxn, kycRespTs, kycErrorTextView;
    private TextView nameAsAadhar, dobAsAadhar, genderAsAadhar, aadharNum;
    private EkycActivity ekycActivity;
    private ProgressBar progressBar;
    private String flagUserConsent = "N";
    AadhaarOtpResponse resp;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for context fragment
        view = inflater.inflate(R.layout.fragment_otp, container, false);
        context = getActivity();
        checkConnection = new CheckConnection(context);
        if (checkConnection.isConnectingToInternet()) {
            // toast("Connected to Internet Connection.");
            NetwordDetect();
        } else {
            showMessageDialogue("Please check your Internet Connection .");
            // toast("Please check your Internet Connection .");
        }
        String aadharNo = ekycActivity.serachItem.getAadhaarNo();//ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.AadharNumber, context);

        String validatorAadhar = null;
        VerifierLoginResponse storedLoginResponse = VerifierLoginResponse.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.VERIFIER_CONTENT, context));
        if (storedLoginResponse != null) {
            validatorAadhar = storedLoginResponse.getAadhaarNumber();
        }
        if (validatorAadhar != null && !validatorAadhar.equalsIgnoreCase("")) {

            Global.VALIDATORAADHAR = validatorAadhar;
        } else {
            Global.VALIDATORAADHAR = "352624429973";
        }
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        progressBar.bringToFront();
        txtView_info = (TextView) view.findViewById(R.id.textview_info);
        edtxt_Aadhaar = (EditText) view.findViewById(R.id.aadhaarNoEdittext);
        edtxt_Aadhaar.addTextChangedListener(inputTextWatcher);
        if (aadharNo != null) {
            edtxt_Aadhaar.setText(aadharNo);
            edtxt_Aadhaar.setEnabled(false);

        }

        String aadharNo1 = ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.AadharNumber, context);

        if(aadharNo1!=null){
            edtxt_Aadhaar.setText(aadharNo1);
            edtxt_Aadhaar.setEnabled(false);
        }
        kycErrorTextView = (TextView) view.findViewById(R.id.errorTextView);
        aadharConsetCB = (CheckBox) view.findViewById(R.id.aadharConsetCB);
        auth_demo_go = (Button) view.findViewById(R.id.auth_demo_go);
        auth_demo_go.setOnClickListener(this);
        cancelEkyc = (Button) view.findViewById(R.id.cancelEkyc);
        cancelEkyc.setOnClickListener(this);
        return view;
    }

    private void NetwordDetect() {

        boolean WIFI = false;

        boolean MOBILE = false;

        ConnectivityManager CM = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo[] networkInfo = CM.getAllNetworkInfo();

        for (NetworkInfo netInfo : networkInfo) {

            if (netInfo.getTypeName().equalsIgnoreCase("WIFI"))

                if (netInfo.isConnected())

                    WIFI = true;

            if (netInfo.getTypeName().equalsIgnoreCase("MOBILE"))

                if (netInfo.isConnected())

                    MOBILE = true;
        }

        if (WIFI == true)

        {
            IPaddress = GetDeviceipWiFiData();
            Log.e("IPaddress", "-->" + IPaddress);
            toast(IPaddress);


        }

        if (MOBILE == true) {

            IPaddress = GetDeviceipMobileData();
            Log.e("IPaddress", "-->" + IPaddress);
            toast(IPaddress);

        }

    }

    public String GetDeviceipMobileData() {

        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    System.out.println("ip1--:" + inetAddress);
                    System.out.println("ip2--:" + inetAddress.getHostAddress());

                    // for getting IPV4 format
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {

                        String ip = inetAddress.getHostAddress().toString();
                        System.out.println("ip---::" + ip);

                        // return inetAddress.getHostAddress().toString();
                        return ip;
                    }
                }
            }
        } catch (Exception ex) {
            Log.e("IP Address", ex.toString());
        }
        return null;

    }

    public String GetDeviceipWiFiData() {

        WifiManager wm = (WifiManager) context.getSystemService(WIFI_SERVICE);

        @SuppressWarnings("deprecation")

        String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());

        return ip;

    }

    public void showMessageDialogue(String messageTxt) {
        new AlertDialog.Builder(activity)
                .setCancelable(false)
                .setTitle("Message")
                .setMessage(messageTxt)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();
    }

    protected void toast(final String msg) {
     /*   activity.runOnUiThread(new Thread(new Runnable() {*/
      /*      @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
            public void run() {*/
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
   /*         }
        }));*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.aadharNumResetEditText:
                edtxt_Aadhaar.setText("");
                break;
            case R.id.auth_demo_go:
                if (ekycActivity.isNetworkAvailable()) {
                    requestAadhaarOtpAuth(edtxt_Aadhaar.getText().toString());
                } else {
                    CustomAlert.alertWithOk(context, getResources().getString(R.string.internet_connection_msg));
                }
                break;
            case R.id.cancelEkyc:
                ekycActivity.finish();
                break;
        }
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        //activity = (CaptureAadharDetailActivity) context;
        ekycActivity = (EkycActivity) context;
    }

    private TextWatcher inputTextWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (edtxt_Aadhaar.length() == 12) {
                try {
                    if (VerhoeffAadhar.validateVerhoeff(edtxt_Aadhaar.getEditableText().toString())) {
                        edtxt_Aadhaar.setTextColor(Color.parseColor("#0B610B"));
                        validAadhaar = true;
                    } else {
                        edtxt_Aadhaar.setTextColor(Color.parseColor("#ff0000"));
                        validAadhaar = false;
                    }

                } catch (Exception e) {

                }
            } else {
                edtxt_Aadhaar.setTextColor(Color.parseColor("#000000"));
                validAadhaar = false;
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

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
                            String txn1 = "UKC:" + resp.getTxn();
                            popupForOTP(requiredString, txn1);
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

    private void popupForOTP(String mobileNo, final String txn1) {
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

                    if (aadharConsetCB.isChecked()) {
                        flagUserConsent = "Y";
                    } else {
                        flagUserConsent = "N";
                    }
                    if (ekycActivity.isNetworkAvailable()) {
                        validateOTP(edtxt_Aadhaar.getText().toString(), otp, inalidOTP, txn1, flagUserConsent);
                        dialog.dismiss();
                    } else {
                        CustomAlert.alertWithOk(context, getResources().getString(R.string.internet_connection_msg));
                    }
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
                //requestAadhaarOtpAuth(loginResponse.getAadhaarNumber());
                if (ekycActivity.isNetworkAvailable()) {
                    requestAadhaarOtpAuth(edtxt_Aadhaar.getText().toString());
                    dialog.dismiss();
                } else {
                    CustomAlert.alertWithOk(context, getResources().getString(R.string.internet_connection_msg));
                }

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

    private void validateOTP(final String aadhaarNo, final String otp, final TextView errorTV, final String txn1, final String userConsent) {

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
                String payLoad = getKYCAuthXml(aadhaarNo, otp, txn1, userConsent);
                System.out.print("VERIFY OTP XML" + payLoad);
//                Log.e("VERIFY REQ OTP XML",payLoad);
                try {
                    HashMap<String, String> response;


                    if (isKycEnabled) {

//                        Log.e("httpPostAadhaarURL", AppConstant.REQUEST_FOR_OTP_EKYC);
//                        Log.e("httpPostAadhaarREQBODY", payLoad);
//                        Log.e("httpPostAadhaarTOKEN", AppConstant.AUTHORIZATION);
//                        Log.e("httpPostAadhaarTVALUE", AppConstant.AUTHORIZATIONVALUE);

                        response = CustomHttp.httpPostAadhaar(AppConstant.REQUEST_FOR_OTP_EKYC1, payLoad, AppConstant.AUTHORIZATION, AppConstant.AUTHORIZATIONVALUE);

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
                          /*  loginResponse.setAadhaarItem(aadhaarKycResponse);
                            ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.VERIFIER_CONTENT, loginResponse.serialize(), context);
                          //  setPin();*/



                            if (personalDetailItem != null) {
                                personalDetailItem.setGovtIdType("Aadhaar");
                                personalDetailItem.setBenefPhoto(aadhaarKycResponse.getBase64());
                                personalDetailItem.setMobileNo(aadhaarKycResponse.getPhone());
                                personalDetailItem.setName(aadhaarKycResponse.getName());
                                personalDetailItem.setGovtIdNo(edtxt_Aadhaar.getText().toString());
                                personalDetailItem.setIdName("13");
                                personalDetailItem.setIsAadhar("Y");

                                personalDetailItem.setSubDistrictBen(aadhaarKycResponse.getSubdist());
                              //  personalDetailItem.setVtcBen(aadhaarKycResponse.getVtc());
                                personalDetailItem.setPostOfficeBen(aadhaarKycResponse.getPo());
                                personalDetailItem.setEmailBen(aadhaarKycResponse.getEmail());

                                personalDetailItem.setGender(aadhaarKycResponse.getGender());
                                personalDetailItem.setYob(aadhaarKycResponse.getDob());
                              //  personalDetailItem.setState(aadhaarKycResponse.getState());
                              //  personalDetailItem.setDistrict(aadhaarKycResponse.getDist());
                                personalDetailItem.setPinCode(aadhaarKycResponse.getPc());
                                personalDetailItem.setFlowStatus(AppConstant.AADHAR_STATUS);
                                ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_NAME,"AADHAAR_DATA",personalDetailItem.serialize(),context);

                            }else {

                                personalDetailItem = new PersonalDetailItem();
                                personalDetailItem.setGovtIdType("Aadhaar");
                                personalDetailItem.setBenefPhoto(aadhaarKycResponse.getBase64());
                                personalDetailItem.setGovtIdNo(edtxt_Aadhaar.getText().toString());
                                personalDetailItem.setIdName("13");
                                personalDetailItem.setIsAadhar("Y");

                                personalDetailItem.setSubDistrictBen(aadhaarKycResponse.getSubdist());
                               // personalDetailItem.setVtcBen(aadhaarKycResponse.getVtc());
                                personalDetailItem.setPostOfficeBen(aadhaarKycResponse.getPo());
                                personalDetailItem.setEmailBen(aadhaarKycResponse.getEmail());

                                personalDetailItem.setGender(aadhaarKycResponse.getGender());
                                personalDetailItem.setYob(aadhaarKycResponse.getDob());
                            //    personalDetailItem.setState(aadhaarKycResponse.getState());
                             //   personalDetailItem.setDistrict(aadhaarKycResponse.getDist());
                                personalDetailItem.setMobileNo(aadhaarKycResponse.getPhone());
                                personalDetailItem.setName(aadhaarKycResponse.getName());
                                personalDetailItem.setPinCode(aadhaarKycResponse.getPc());
                                personalDetailItem.setFlowStatus(AppConstant.AADHAR_STATUS);
                                ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_NAME,"AADHAAR_DATA",personalDetailItem.serialize(),context);
                            }
                         /*   PersonalDetailItem personalDetailItem = new PersonalDetailItem();
                            personalDetailItem.setBenefPhoto(aadhaarKycResponse.getBase64());
                            personalDetailItem.setMobileNo(aadhaarKycResponse.getPhone());
                            personalDetailItem.setName(aadhaarKycResponse.getName());
                            ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_NAME,"AADHAAR_DATA",personalDetailItem.serialize(),context);*/

                            ekycActivity.finish();
                           // ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_NAME,"AADHAAR_DATA",aadhaarKycResponse.serialize(),context);

                           // ekycActivity.finish();
                            /*Intent intent = new Intent(context, FingerprintResultActivity.class);
                            intent.putExtra("result", aadhaarKycResponse);
                            startActivity(intent);*/
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
                         /*   ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.VERIFIER_CONTENT, loginResponse.serialize(), context);
                            //setPin();*/

                            Intent intent = new Intent(context, PersonalDetailsFragment.class);
                            intent.putExtra("result", aadhaarKycResponse);
                            startActivity(intent);
                          /*  Intent intent = new Intent(context, FingerprintResultActivity.class);
                            intent.putExtra("result", aadhaarKycResponse);
                            startActivity(intent);*/
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
        if (asyncTask != null && !asyncTask.isCancelled()) {
            asyncTask.cancel(true);
            asyncTask = null;
        }

        asyncTask = new CustomAsyncTask(taskListener, context.getResources().getString(R.string.please_wait), context);
        asyncTask.execute();
        /*CustomVolley volley=new CustomVolley(taskListener,"Please Wait..",AppConstant.AADHAR_OTP_AUTH_API_NEW,aadhaarReq.serialize(),null,null,context);
        volley.execute();*/
    }

    //	to create KYC auth/encoded Rad xml
    public String getKYCAuthXml(String aadhaar_no, String otp, String txnID, String userConsent) {
        String kycXml = "";

        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String IMEINumber = tm.getDeviceId();
        Global.imei = IMEINumber;

        byte[] publicKey = null;
        //		GetPubKeycertificateData();
        publicKey = Base64.decode(Global.productionPublicKey, Base64.DEFAULT);

        Log.e("FINAL PUB KEY:", "=======================>>>>>>>>>>" + Global.productionPublicKey);

        UdaiAuthOTPHelper helper = new UdaiAuthOTPHelper(publicKey);
        if (isKycEnabled) {
            Log.e("IS KYC", "TRUE");
            kycXml = helper.createXmlForKycAuthByRaj("", aadhaar_no, "", true, otp, txnID, userConsent);
            kycXml = kycXml.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>");

        } else {

            Log.e("IS KYC", "FALSE");
            //by rajesh modification
            kycXml = helper.createXmlForKycAuthByRaj("", aadhaar_no, "", true, otp, txnID, userConsent);
            kycXml = kycXml.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>");
        }

        return kycXml;

    }
}
