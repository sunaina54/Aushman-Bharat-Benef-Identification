package com.nhpm.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.hardware.SensorManager;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.support.multidex.MultiDex;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/*import com.aadhar.CheckConnection;
import com.aadhar.CommonMethods;
import com.aadhar.Global;
import com.aadhar.Logs;
import com.aadhar.ShowDialogWaitForAuth;
import com.aadhar.StatusLogs;
import com.aadhar.UidaiAuthHelper;
import com.aadhar.VerhoeffAadhar;
import com.aadhar.commonapi.BiometricDeviceHandler;
import com.aadhar.commonapi.DeviceRecognizer;
import com.aadhar.commonapi.HelperInterface;*/
import com.customComponent.CustomAlert;
import com.customComponent.utility.DateTimeUtil;
import com.customComponent.utility.ProjectPrefrence;
import com.nhpm.AadhaarUtils.Global;
import com.nhpm.AadhaarUtils.VerhoeffAadhar;
import com.nhpm.LocalDataBase.dto.SeccDatabase;
import com.nhpm.Models.KycResponse;
import com.nhpm.Models.response.AadharDataModel;
import com.nhpm.Models.response.Poa;
import com.nhpm.Models.response.Poi;
import com.nhpm.Models.response.UidData;
import com.nhpm.Models.response.seccMembers.SeccMemberItem;
import com.nhpm.Models.response.seccMembers.SelectedMemberItem;
import com.nhpm.Models.response.verifier.AadhaarDemoAuthResponse;
import com.nhpm.Models.response.verifier.AadhaarResponseItem;
import com.nhpm.Models.response.verifier.VerifierLoginResponse;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;
import com.nhpm.Utility.AppUtility;
import com.nhpm.Utility.ApplicationGlobal;
import com.nhpm.activity.CaptureAadharDetailActivity;
import com.nhpm.activity.WithAadhaarActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import fr.arnaudguyon.xmltojsonlib.XmlToJson;

import static android.content.Context.INPUT_METHOD_SERVICE;


public class AadharAuthFingerPrintKycFragment extends Fragment { //implements View.OnClickListener, HelperInterface {
    String ACTION_USB_PERMISSION = "com.aadhar.commonapi.USB_PERMISSION";
    private static final String TAG = "Capture Activity Finger";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static UsbManager mManager;
    public static PendingIntent mPermissionIntent = null;
    public View view;
    File f = new File(Environment.getExternalStorageDirectory(), "Authentication");
    Handler handler1 = new Handler();
    String deviceMake, deviceModel, serialNumber, deviceVendor;
    boolean deviceInit = false;
    Timer timer;
    boolean kycCompleted = false;
    TimerTask doAsynchronousPermissionCheck;
    String fpImgString;
/*    ShowDialogWaitForAuth dialogWaitForAuth;
    ProgressDialog barProgressDialog;
    String[] arry = new String[2];
    CheckConnection checkConnection;
    boolean checkNetwork;
    long startTime, endTime, totalTime;
    Logs log;
    StatusLogs statusLog;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Context context;
    public DeviceRecognizer deviceRecognizer = new DeviceRecognizer(context);*/
    private CaptureAadharDetailActivity activity;
    private LinearLayout ll_Scan_main, ll_scan_bottom;
    private ImageView imageViewFingerPrint, imageViewConnectionStatus, imageViewDeviceStatus;
    private ProgressBar progressBarFPQuality;
    private Button buttonEnd, buttonScan, btn_auth, cancelButton;
    private TextView textViewStatus;
    private EditText edtxt_Aadhaar;
    private Button btn_submit, btn_home;
    private boolean validAadhaar = false;
    private LinearLayout aadhaar_layout;
    private RadioGroup rgDevice;
    private RelativeLayout backLayout;
    private ImageView back;
/*    private BiometricDeviceHandler handler;
    private String xml, aadhaar, btnype = "";
    private CountDown countDown;
    private TextView textviewTimer, aadharNumResetEditText;
    private boolean captureStatus;
    private boolean isSMASUNGScannerStarted = false;
    private CountDownSamsung countDownSamsung;*/
    private SensorManager mSensorManager;
    private TextView kycName, kycDob, kycGender, kycEmail, kycPhone, kycCareOf, kycAddr, kycTs, kycTxn, kycRespTs, kycErrorTextView;
    private LinearLayout kycDetailLayout, kycErrorLayout;
    private Button updateKycButton;
    private ImageView aadharImageView;
    private LinearLayout aadharImageLinearLayout;
    private CheckBox aadharConsetCB;
    private SeccMemberItem seccItem;
    private SelectedMemberItem selectedMemItem;
    private VerifierLoginResponse loginResponse;
    private AlertDialog internetDiaolg;
    private String consent = "Y";
    private KycResponse kycModel;
    private Button cancelEkyc;
    private CardView card_view1;
    private TextView nameAsAadhar, dobAsAadhar, genderAsAadhar, aadharNum;
    private ProgressBar progressbar;
    private Boolean isKycEnabled = AppConstant.isKyCEnabled;
    private TextWatcher inputTextWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (edtxt_Aadhaar.length() == 12) {
                try {
                    if (VerhoeffAadhar.validateVerhoeff(edtxt_Aadhaar.getEditableText()
                            .toString())) {
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
    private AadhaarDemoAuthResponse demoAuthResp;
    private AadhaarResponseItem aadhaarKycResponse;

    public AadharAuthFingerPrintKycFragment() {
        // Required empty public constructor
    }

    /**
     * Use context factory method to create a new instance of
     * context fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AadharAuthFingerPrintKycFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AadharAuthFingerPrintKycFragment newInstance(String param1, String param2) {
        AadharAuthFingerPrintKycFragment fragment = new AadharAuthFingerPrintKycFragment();
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
         /*   mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);*/
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for context fragment
        view = inflater.inflate(R.layout.fragment_finger_print_kyc, container, false);
     /*   context = getActivity();
        backLayout = (RelativeLayout) view.findViewById(R.id.backLayout);
        back = (ImageView) view.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeActivity();
            }
        });
        backLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeActivity();
            }
        });*/
        edtxt_Aadhaar = (EditText) view.findViewById(R.id.auth_demo_aadhaar);
        edtxt_Aadhaar.addTextChangedListener(inputTextWatcher);
        progressbar = (ProgressBar) view.findViewById(R.id.progressBar);
        progressbar.bringToFront();
        progressbar.setVisibility(View.GONE);


     /*   selectedMemItem = SelectedMemberItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.SELECTED_ITEM_FOR_VERIFICATION, context));
        if (selectedMemItem.getSeccMemberItem() != null) {
            seccItem = selectedMemItem.getSeccMemberItem();
        }*/
        card_view1 = (CardView) view.findViewById(R.id.card_view1);
        aadharNum = (TextView) view.findViewById(R.id.aadharNum);
        nameAsAadhar = (TextView) view.findViewById(R.id.nameAsAadhar);
        dobAsAadhar = (TextView) view.findViewById(R.id.dobAsAadhar);
        genderAsAadhar = (TextView) view.findViewById(R.id.genderAsAadhar);
     /*   selectedMemItem = SelectedMemberItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.SELECTED_ITEM_FOR_VERIFICATION, context));*/
        if (selectedMemItem.getSeccMemberItem() != null) {
            seccItem = selectedMemItem.getSeccMemberItem();
        }
        Global.USER_NAME = ApplicationGlobal.USER_NAME;
        Global.USER_PASSWORD = ApplicationGlobal.USER_PASSWORD;
        if (seccItem != null) {
            if (seccItem.getAadhaarCapturingMode() != null) {
                if (seccItem.getAadhaarCapturingMode().equalsIgnoreCase(AppConstant.FINGER_MODE) || seccItem.getAadhaarCapturingMode().equalsIgnoreCase("")) {
                    if (seccItem.getAadhaarAuth() != null && !seccItem.getAadhaarAuth().equalsIgnoreCase("")) {
                        card_view1.setVisibility(View.VISIBLE);
                        if (seccItem.getAadhaarNo() != null) {
                            edtxt_Aadhaar.setText(seccItem.getAadhaarNo());
                            aadharNum.setText(seccItem.getAadhaarNo());
                        }
                        if (seccItem.getAadhaarGender() != null) {
                            genderAsAadhar.setText(seccItem.getAadhaarGender());
                        }
                        if (seccItem.getNameAadhaar() != null) {
                            nameAsAadhar.setText(seccItem.getNameAadhaar());
                        }
                        if (seccItem.getAadhaarDob() != null) {
                            dobAsAadhar.setText(seccItem.getAadhaarDob());
                        }


                    }
                }
            }
        }

     //   findDeviceAttached(view);
        return view;
    }

  /*  private void closeActivity() {
        try {
            activity.unregisterReceiver(mUsbReceiver);
            handler1.removeCallbacksAndMessages(null);
            handler.UnInitDevice();
            //activity.unregisterReceiver();
        } catch (Exception ex) {

        }
        activity.finish();
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        activity = (CaptureAadharDetailActivity) context;
    }

    private void findDeviceAttached(View view) {

        checkConnection = new CheckConnection(context);

        if (!f.exists()) {
            f.mkdirs();
        }
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

        countDown = new CountDown(Global.TIME_FOR_SCANNING, Global.INTERVAL);
        countDownSamsung = new CountDownSamsung(Global.TIME_FOR_SCANNING_SAMSUNG, Global.INTERVAL);
        Global.imei = CommonMethods.GetIMEI(context);
        mManager = ((UsbManager) activity.getSystemService(Context.USB_SERVICE));
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_USB_PERMISSION);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        activity.registerReceiver(mUsbReceiver, filter, "permission.ALLOW_BROADCAST", handler1);//you are just registering a receiver to listen to events.

        //		registerReceiver(mUsbReceiver, filter);//you are just registering a receiver to listen to events.
        init(view);
        try {
            MultiDex.install(activity);

            FindVendorDevice();// context function is called here to check if any supported device is attachd during the application startup.
            AsynchronousPermissionCheck();
        } catch (Exception ex) {
            activity.recreate();
        }
    }

    BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action)) {
                SetTextonuiThread("Biometric Scanner is Ready");
                imageViewDeviceStatus.setImageResource(R.drawable.dgreen);
                if (!AppUtility.isFingerPrintDeviceAttached) {
                    progressbar.setVisibility(View.VISIBLE);
                }
                try {
                    //Global.scannerAttached = true;
                    FindVendorDevice();

                } catch (Exception sf) {
                    activity.recreate();//btnInit.setVisibility(View.GONE);
                }
            } else if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
                SetTextonuiThread("Biometric Scanner Not Found");
                imageViewDeviceStatus.setImageResource(R.drawable.dred);
                deviceInit = false;
                imageViewFingerPrint.invalidate();
                imageViewFingerPrint.setImageBitmap(null);
                if (Global.scannerAttached) {
                    try {
                        handler.UnInitDevice();
                    } catch (Exception ed) {

                    }
                }
                AppUtility.isFingerPrintDeviceAttached = false;
                //Code to stop changing the devices in between----->
                AlertDialog.Builder wrngAlert = new AlertDialog.Builder(
                        activity);

                wrngAlert.setTitle("Device Can't be detached/changed in between the process");
                wrngAlert.setMessage("Do the process without disturbing the device.");
                wrngAlert.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                //Editted by saurabh
                                if (!kycCompleted) {
                                    AppUtility.isFingerPrintDeviceAttached = true;
                                    progressbar.setVisibility(View.GONE);
                                    activity.recreate();
                                } else {
                                    AppUtility.isFingerPrintDeviceAttached = false;
                                }
                                *//* Intent intent = new Intent(getApplicationContext(),
                                        SplashActivity.class);
                                startActivity(intent);
                                finish();*//*
                            }
                        });
                wrngAlert.setCancelable(false);
                wrngAlert.create().show();
                //Code to stop changing the devices in between<-----
            } else if (ACTION_USB_PERMISSION.equals(action)) {

            }

        }
    };


    //	hiding scan layout only
    public void hideScanControl() {
        //		ll_scan_bottom.setVisibility(View.GONE);
        ll_Scan_main.setVisibility(View.GONE);
        textviewTimer.setVisibility(View.GONE);
    }

    //	hiding aadhaar layout only
    public void hideAadhaarControl() {
        aadhaar_layout.setVisibility(View.GONE);
    }

    //	visible the scan layout
    public void showScanControl() {
        ll_scan_bottom.setVisibility(View.VISIBLE);
        ll_Scan_main.setVisibility(View.VISIBLE);
        textviewTimer.setVisibility(View.VISIBLE);

        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(textviewTimer.getWindowToken(), 0);
    }

    //	visibile the aadhaar layout
    public void showAadhaarControl() {
        aadhaar_layout.setVisibility(View.VISIBLE);
    }

    private void startCapturingKYC() {
        checkNetwork = checkConnection.isConnectingToInternet();
        if (checkNetwork) {
            if (Global.scannerAttached) {
                //	isOnline()
                //AsynchronousPermissionCheck();  .....................Bapuji checking  ...not required methods
                hideAadhaarControl();
                showScanControl();


                captureStatus = false;

           *//*     Boolean activate = activateIrisLicense();
                mSensorManager = (SensorManager) activity.getSystemService(SENSOR_SERVICE);
                mSensorManager.registerListener(proximitySensorEventListener,
                        mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY),
                        SensorManager.SENSOR_DELAY_NORMAL);
                if (activate) {
                    countDownSamsung.start();
                    Toast.makeText(context, "Scanning Start", Toast.LENGTH_SHORT).show();
                    imageViewFingerPrint.setVisibility(View.GONE);
                    startCapture();
                } else {
*//*
                if (Global.INFOCUS_Iris) {
                    hideScanControl();
                    Intent intent = null;
                    try {
                        intent = new Intent(activity, Class.forName("in.kdms.irislib.IRISCaptureActivity"));
                        startActivity(intent);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }

                } else {
                    countDown.start();
                    imageViewFingerPrint.invalidate();
                    imageViewFingerPrint.setImageBitmap(null);
                    imageViewFingerPrint.invalidate();
                    Toast.makeText(context, "Scanning Start", Toast.LENGTH_SHORT).show();
                    handler.BeginCapture();
                }
                //}

            } else {
                CommonMethods.showErrorDialog("Something is Wrong",
                        "Biometric Scanner not found.");
            }
        } else {
            ShowPromptNetworkforKYC("Network Issue", "Please Enable the network");
        }
    }

  *//*  private void startCapturingAUTH() {
        checkNetwork = checkConnection.isConnectingToInternet();
        if (checkNetwork) {
            if (Global.scannerAttached) {
                //	isOnline()
                //AsynchronousPermissionCheck();  .....................Bapuji checking  ...not required methods
                hideAadhaarControl();
                showScanControl();


                captureStatus = false;

                Boolean activate = activateIrisLicense();
                mSensorManager = (SensorManager) activity.getSystemService(SENSOR_SERVICE);
                mSensorManager.registerListener(proximitySensorEventListener,
                        mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY),
                        SensorManager.SENSOR_DELAY_NORMAL);

                if (activate) {
                    countDownSamsung.start();
                    Log.e("activate", "------->" + activate);
                    imageViewFingerPrint.setVisibility(View.GONE);
                    startCapture();
                } else {

                    if (Global.INFOCUS_Iris) {
                        hideScanControl();
                        Intent intent = null;
                        try {
                            intent = new Intent(activity, Class.forName("in.kdms.irislib.IRISCaptureActivity"));
                            startActivity(intent);
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }

                    } else {
                        countDown.start();
                        imageViewFingerPrint.setImageBitmap(null);
                        imageViewFingerPrint.invalidate();
                        Toast.makeText(context, "Scanning Start", Toast.LENGTH_SHORT).show();
                        handler.BeginCapture();
                    }

                }


            } else {
                CommonMethods.showErrorDialog("Something is Wrong",
                        "Biometric Scanner not found.");
            }
        } else {
            ShowPromptNetworkforAUTH("Network Issue", "Please Enable the network");
        }
    }*//*

    private void GetConformation() {
        //showInfoDialog("GetConformation");
        String message = "";
        message = " Proceed?";

        //showInfoDialog("GetConformation b4 dialog");
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(
                context);

        dlgAlert.setMessage(message);
        dlgAlert.setTitle("Confirmation");
        dlgAlert.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (Global.scannerAttached) {
                            //AsynchronousPermissionCheck();  .....................Bapuji checking  ...not required methods
                            hideAadhaarControl();
                            showScanControl();
                            imageViewFingerPrint.invalidate();
                            if (Global.samsungIris) {
                                countDownSamsung.start();
                            } else {

                                countDown.start();
                            }
                            captureStatus = false;
                            imageViewFingerPrint.setImageBitmap(null);
                            //showInfoDialog("GetConformation inside dialog bigincapture");
                            Toast.makeText(context, "BeginCapture start", Toast.LENGTH_SHORT).show();
                            handler.BeginCapture();
                        } else {
                            CommonMethods.showErrorDialog("Something is Wrong",
                                    "Biometric Scanner not found.");
                        }
                        return;
                    }
                });
        dlgAlert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // return;
            }
        });
        dlgAlert.setCancelable(false);
        dlgAlert.create().show();
    }

    private void initilize() {


        mManager = ((UsbManager) activity.getSystemService(Context.USB_SERVICE));
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_USB_PERMISSION);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        activity.registerReceiver(mUsbReceiver, filter, "permission.ALLOW_BROADCAST", handler1);//you are just registering a receiver to listen to events.

        FindVendorDevice();
        AsynchronousPermissionCheck();
        //	unregisterReceiver(mUsbReceiver);
        System.out.println("unregisterReceiver new called...");
    }

    void SetTextonuiThread(final String str) {
        textViewStatus.post(new Runnable() {
            public void run() {
                textViewStatus.setText(str);

            }
        });
    }

  *//*  BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action)) {
                SetTextonuiThread("Biometric Scanner is Ready");
                imageViewDeviceStatus.setImageResource(R.drawable.dgreen);
                //     FindVendorDevice();
                //btnInit.setVisibility(View.GONE);
            } else if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
                SetTextonuiThread("Biometric Scanner Not Found");
                imageViewDeviceStatus.setImageResource(R.drawable.dred);
                deviceInit = false;
                imageViewFingerPrint.invalidate();
                imageViewFingerPrint.setImageBitmap(null);
                if (Global.scannerAttached) {
                    handler.UnInitDevice();
                    //					theMessage
                    //							.setText("Scanner Removed, Waiting for supported Scanner.");
                }
                //Code to stop changing the devices in between----->
                AlertDialog.Builder wrngAlert = new AlertDialog.Builder(
                        context);

                wrngAlert.setTitle("Device Can't be detached/changed in between the process");
                wrngAlert.setMessage("Do the process without disturbing the device.");
                wrngAlert.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                //Editted by saurabh
                                activity.recreate();
                                *//**//* Intent intent = new Intent(getApplicationContext(),
                                        SplashActivity.class);
                                startActivity(intent);
                                finish();*//**//*
                            }
                        });
                wrngAlert.setCancelable(false);
                wrngAlert.create().show();
                //Code to stop changing the devices in between<-----
            } else if (ACTION_USB_PERMISSION.equals(action)) {

            }

        }
    };*//*

    public void AsynchronousPermissionCheck() {
        Log.e("Asynchron", "executing");
        final Handler handlerTask = new Handler();
        this.timer = new Timer();
        this.doAsynchronousPermissionCheck = new TimerTask() {
            public void run() {
                handlerTask.post(new Runnable() {
                    // private int bioDeviceType;

                    public void run() {
                        try {
                            if (Global.scannerAttached && !deviceInit) {
                                mManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
                                HashMap deviceList = mManager.getDeviceList();
                                Iterator deviceIterator = deviceList.values().iterator();

                                while (true) {
                                    while (true) {
                                        boolean startInit;
                                        do {
                                            if (!deviceIterator.hasNext()) {
                                                return;
                                            }

                                            UsbDevice device = (UsbDevice) deviceIterator.next();
                                            long biometricProductID = (long) device.getProductId();
                                            startInit = false;
                                            if (Global.mantraAttached && biometricProductID == 4101L && mManager.hasPermission(device)) {
                                                startInit = true;
                                            } else if (Global.startekAttached && biometricProductID == 33312L && mManager.hasPermission(device)) {
                                                startInit = true;
                                            } else if (Global.iritechAttached && biometricProductID == 61441L && mManager.hasPermission(device)) {
                                                startInit = true;
                                            } else if ((Global.morphoMSO1300Attached && biometricProductID == 71L || Global.morphoMSO1350Attached && biometricProductID == 82L || Global.morphoMSO30xAttached && biometricProductID == 36L || Global.morphoMSO35xAttached && biometricProductID == 38L) && mManager.hasPermission(device)) {
                                                startInit = true;
                                            } else if (Global.bioenableAttached && biometricProductID == 1616L && mManager.hasPermission(device)) {
                                                startInit = true;
                                            } else if (Global.secugenAttached && biometricProductID == 8704L && mManager.hasPermission(device)) {
                                                startInit = true;
                                            } else if (Global.biometriquesAttached && biometricProductID == 1003L && mManager.hasPermission(device)) {
                                                startInit = true;
                                            } else if (Global.precisionElkonTouchAttached && biometricProductID == 8214L && mManager.hasPermission(device)) {
                                                startInit = true;
                                            } else if (Global.precisionuru4500Attached && biometricProductID == 11L && mManager.hasPermission(device)) {
                                                startInit = true;
                                            } else {
                                                startInit = false;
                                            }
                                        } while (!startInit);

                                        //deviceInit = true;
                                        AadharAuthFingerPrintKycFragment.this.deviceInit = true;

                                        try {
                                            int init = AadharAuthFingerPrintKycFragment.this.handler.InitDevice(Global.connectedDeviceID);
                                            System.out.print(init);
                                            // int init = handler.InitDevice(Global.connectedDeviceID);
                                            if (init == 0) {

                                                //										btnScan.setVisibility(View.VISIBLE);
                                                //										btnUnInit.setVisibility(View.VISIBLE);
                                                //										btnInit.setVisibility(View.GONE);
                                                //										DialogInitParam initParam = new DialogInitParam(
                                                //												MainActivity.this);

                                                handler.SetThresholdQuality(60);
                                                handler.SetCaptureTimeout(Global.SCAN_TIME);
                                                Global.bioDeviceType = handler
                                                        .GetDeviceType();
                                                if (Global.iritechAttached) {
                                                    Global.bioDeviceType = 1;
                                                    handler.SetFingerprintBiometricDataType("IIR");
                                                } else
                                                    handler.SetFingerprintBiometricDataType("FMR");
                                                //										initParam.SetHandlerObj(handler);
                                                //										initParam.setCancelable(false);
                                                //										initParam.show();
                                                deviceMake = handler.GetDeviceMake();
                                                deviceModel = handler.GetDeviceModel();
                                                deviceVendor = String.valueOf(handler
                                                        .GetAttachedDeviceVendorID());
                                                serialNumber = handler
                                                        .GetDeviceSerialNumber();

                                            } else {

                                            }
                                        } catch (Exception ex) {
                                            activity.recreate();
                                        }

                                    }
                                }
                            }
                        } catch (Exception ex) {
                            activity.recreate();
                        }
                    }
                });
            }
        };
        this.timer.schedule(this.doAsynchronousPermissionCheck, 0L, 1000L);
    }

    protected boolean FindVendorDevice() {
        Log.e("FindVendorDevice", "executing");
        try {
            deviceRecognizer = new DeviceRecognizer(context);
            deviceRecognizer.FindSupportedDevice();

            UsbDevice device = null;
            if (Global.scannerAttached) {
                if (deviceRecognizer.GetAttachedDeviceCount() == 1) {
                    Global.scannerAttached = true;
                    Global.connectedDeviceID = deviceRecognizer.GetAttachedDeviceID();
                    Global.connectedDeviceNameId = deviceRecognizer.GetAttachedDeviceID() + deviceRecognizer.GetAttachedDeviceName();
                    mManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
                    HashMap deviceList = mManager.getDeviceList();
                    Iterator deviceIterator = deviceList.values().iterator();
                    while (deviceIterator.hasNext()) {
                        device = (UsbDevice) deviceIterator.next();
                        if (Global.scannerAttached) {
                            Global.attachedDeviceType = deviceRecognizer.GetAttachedDeviceName();
                        }
                    }
                    Log.e("device typ", "-->" + Global.attachedDeviceType);
                    try {
                        this.handler = new BiometricDeviceHandler(this, Global.connectedDeviceID, activity);
                        System.out.print(handler);
                        this.handler.SetApplicationContext(context, activity);
                    } catch (Exception ex) {
                        try {
                            activity.unregisterReceiver(mUsbReceiver);
                        } catch (Exception exd) {
                            System.out.print(exd.toString());
                        }
                        activity.recreate();
                    }
                    Global.biometricInitialized = true;
                    if (device != null) {
                        mPermissionIntent = PendingIntent.getBroadcast(context, 0, new Intent("com.aadhar.commonapi.USB_PERMISSION"), 0);
                        mManager.requestPermission(device, mPermissionIntent);
                    }
                    SetTextonuiThread("Biometric Scanner is Ready");
                    imageViewDeviceStatus.setImageResource(R.drawable.dgreen);
                    return true;
                } else {

                    return false;
                }
            } else {

                return false;
            }
        } catch (Exception ex) {
            activity.recreate();
            return false;
        }

    }


    private void init(View view) {

        kycName = (TextView) view.findViewById(R.id.kycName);
        kycDob = (TextView) view.findViewById(R.id.kycDob);
        kycGender = (TextView) view.findViewById(R.id.kycGender);
        kycEmail = (TextView) view.findViewById(R.id.kycEmail);
        kycPhone = (TextView) view.findViewById(R.id.kycPhone);
        kycCareOf = (TextView) view.findViewById(R.id.kycCareOf);
        kycAddr = (TextView) view.findViewById(R.id.kycAddr);
        kycTs = (TextView) view.findViewById(R.id.kycTs);
        kycTxn = (TextView) view.findViewById(R.id.kycTxn);
        kycRespTs = (TextView) view.findViewById(R.id.kycRespTs);
        kycErrorTextView = (TextView) view.findViewById(R.id.errorTextView);
        kycDetailLayout = (LinearLayout) view.findViewById(R.id.kycDetailLayout);
        kycErrorLayout = (LinearLayout) view.findViewById(R.id.kycErrorLayout);
        updateKycButton = (Button) view.findViewById(R.id.updateKycButton);
        cancelButton = (Button) view.findViewById(R.id.cancelButton);
        aadharImageView = (ImageView) view.findViewById(R.id.aadharImageView);
        aadharImageLinearLayout = (LinearLayout) view.findViewById(R.id.aadharImageLinearLayout);
        aadharConsetCB = (CheckBox) view.findViewById(R.id.aadharConsetCB);
        log = new Logs(context);
        statusLog = new StatusLogs(activity);

        dialogWaitForAuth = new ShowDialogWaitForAuth(activity);
        dialogWaitForAuth.setCancelable(false);

        textviewTimer = (TextView) view.findViewById(R.id.scanTimer);
        checkNetwork = checkConnection.isConnectingToInternet();


      *//*  if (!Global.AUTH_AADHAAR.equalsIgnoreCase("")) {
            edtxt_Aadhaar.setText(Global.AUTH_AADHAAR);
            edtxt_Aadhaar.setTextColor(Color.parseColor("#0B610B"));
            validAadhaar = true;
        }*//*

        btn_submit = (Button) view.findViewById(R.id.auth_demo_go);
        btn_submit.setOnClickListener(this);

        btn_auth = (Button) view.findViewById(R.id.auth_demo_simple);
        btn_auth.setOnClickListener(this);

        cancelEkyc = (Button) view.findViewById(R.id.cancelEkyc);
        cancelEkyc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });

        btn_home = (Button) view.findViewById(R.id.home);
       *//* btn_home.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(getBaseContext(), MainScreen.class);
                startActivity(i);
                finish();
            }
        });*//*


        loginResponse = (VerifierLoginResponse.create(ProjectPrefrence.
                getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.VERIFIER_CONTENT, context)));
        aadhaar_layout = (LinearLayout) view.findViewById(R.id.aadhaar_layout);
        //		RadioButton rBtn_bio = (RadioButton) view.findViewById(R.id.bio);
        //		RadioButton rBtn_Iris = (RadioButton) view.findViewById(R.id.iris);
        rgDevice = (RadioGroup) view.findViewById(R.id.rg_device);

        ll_scan_bottom = (LinearLayout) view.findViewById(R.id.scan_bottom_layout);
        ll_Scan_main = (LinearLayout) view.findViewById(R.id.scan_layout);

        imageViewFingerPrint = (ImageView) view.findViewById(R.id.imageViewFingerPrint);
        imageViewConnectionStatus = (ImageView) view.findViewById(R.id.imageViewConnectionStatus);
        imageViewDeviceStatus = (ImageView) view.findViewById(R.id.imageViewDeviceStatus);
        progressBarFPQuality = (ProgressBar) view.findViewById(R.id.progressBarFPQuality);

        buttonEnd = (Button) view.findViewById(R.id.buttonEnd);
        buttonScan = (Button) view.findViewById(R.id.buttonScan);

        buttonEnd.setOnClickListener(this);
        buttonScan.setOnClickListener(this);

        textViewStatus = (TextView) view.findViewById(R.id.textViewStatus);
        aadharNumResetEditText = (TextView) view.findViewById(R.id.aadharNumResetEditText);
        aadharNumResetEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtxt_Aadhaar.setText("");
            }
        });
        hideScanControl();
        showAadhaarControl();
        ll_scan_bottom.setVisibility(View.VISIBLE);

        if (checkNetwork) {                // checking either network is connected or not.
            barProgressDialog = new ProgressDialog(activity);

            barProgressDialog.setTitle("");
            barProgressDialog.setMessage("Please wait ...");
            barProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            barProgressDialog.setProgress(0);
            barProgressDialog.setCancelable(false);
            //			barProgressDialog.show();

            // imageViewConnectionStatus.setImageResource(R.drawable.cgreen);

            CommonMethods.SetApplicationContext(activity);

            //			fetching public key
            GetPubKeycertificateData publicKey1 = new GetPubKeycertificateData();
            publicKey1.execute();
        } else {
            // imageViewConnectionStatus.setImageResource(R.drawable.cred);
            ShowPromptNetwork("Network Issue", "Please Enable the network");
        }

    }

    @Override
    public void onClick(View v) {
        Log.e("loc address", "------" + Global.LOCATION_ADDRESS);
        switch (v.getId()) {
           *//* case R.id.auth_demo_simple:
                btnype = "bio_auth";


                if (validAadhaar) {                            // checking either aadhaar is valid not not
                    Global.AUTH_AADHAAR = edtxt_Aadhaar.getText().toString();
                    aadhaar = Global.AUTH_AADHAAR;
                    checkNetwork = checkConnection.isConnectingToInternet();
                    if (checkNetwork) {
                        startCapturingAUTH();
                    } else {
                        log.myMessage(getResources().getString(R.string.network_not_available) + " AUTH BIO Button click", Global.AUTH_AADHAAR);
                        ShowPromptNetworkforAUTH("Network Issue", "Please Enable the network");
                    }
                *//**//*if(Global.scannerAttached) {
                    //AsynchronousPermissionCheck();  .....................Bapuji checking  ...not required methods
					hideAadhaarControl();
					showScanControl();
					imageViewFingerPrint.invalidate();
					countDown.start();
					captureStatus = false;
					imageViewFingerPrint.setImageBitmap(null);
					Toast.makeText(KYC_BIO.this, "BeginCapture start", Toast.LENGTH_SHORT).show();
					handler.BeginCapture();
				}else{
					CommonMethods.showErrorDialog("Something is Wrong",
							"Biometric Scanner not found.");
				}*//**//*
                } else {
                    CommonMethods.showErrorDialog("Invalid Value",
                            "Please check Aadhaar Number");
                }
                break;*//*

            case R.id.auth_demo_go:
                btnype = "bio_ekyc";


                if (!aadharConsetCB.isChecked()) {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.adhar_consent_msg));
                    return;
                }
                if (edtxt_Aadhaar.getText().toString().equalsIgnoreCase("")) {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.enterValidAadhaar));
                    return;
                }
                if (validAadhaar) {                            // checing either aadhaar is valid not not
                    Global.AUTH_AADHAAR = edtxt_Aadhaar.getText().toString();
                    aadhaar = Global.AUTH_AADHAAR;
                    checkNetwork = checkConnection.isConnectingToInternet();
                    if (checkNetwork) {
                        startCapturingKYC();
                    } else {
                        log.myMessage(getResources().getString(R.string.network_not_available) + " KYC BIO buton click", Global.AUTH_AADHAAR);
                        ShowPromptNetworkforKYC("Network Issue", "Please Enable the network");
                    }
                } else {
                    CommonMethods.showErrorDialog("Invalid Value",
                            "Please check Aadhaar Number");
                }
                break;

            case R.id.buttonEnd:

                activity.finish();
                break;

            case R.id.buttonScan:

                if (Global.scannerAttached == true) {

                    repeatScanning();

                } else
                    CommonMethods.showErrorDialog("Something is Wrong",
                            "Biometric Scanner not found.");

                break;

            default:
                break;
        }

    }

    @Override
    public void handlerFunction(final byte[] rawImage, final int imageHeight,
                                final int imageWidth, final int status, final String errorMessage,
                                final boolean complete, final byte[] isoData, final int quality, int finalNFIQ) {

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.e("status", "--->" + status);
                if (status < 0) {
                    SetTextonuiThread("ERROR:" + errorMessage);
                } else if (status == 0) {
                    if (rawImage != null) {
                        imageViewFingerPrint.setImageBitmap(RawToBitmap(rawImage,
                                imageWidth, imageHeight));
                        Resources res = getResources();
                        if (quality > 50) {

                            progressBarFPQuality.setProgressDrawable(res
                                    .getDrawable(R.drawable.greenprogress));
                        } else {
                            progressBarFPQuality.setProgressDrawable(res
                                    .getDrawable(R.drawable.redprogress));
                        }
                        progressBarFPQuality.setProgress(quality);
                    }
                    Log.e("complete", "==" + complete);
                    if (complete) {

                        dialogWaitForAuth.show();

                        startTime = System.currentTimeMillis();

                        Log.e("complete in if", "==" + complete);
                        String devicetype = "F";
                        fpImgString = Base64.encodeToString(isoData, Base64.NO_WRAP);
                        //						Log.e("fp", "=="+fpImgString);
                        if (Global.iritechAttached)
                            devicetype = "I";
                        //						if we are using fingure pirnt device then if part execute
                        //if ( Global.authType.equalsIgnoreCase("O") ){

                        byte[] publicKey = Base64.decode(Global.productionPublicKey, Base64.DEFAULT);
                        String aadhaar_no = Global.AUTH_AADHAAR;
                        Log.e("public key", "===" + publicKey);

                        Global.KYC_API_VERSION = "1.0";
                        String KYCAuthXml = "";
                        String txtMessage = "";
                   *//*     if (btnype.equalsIgnoreCase("bio_auth")) {    // Only Auth  ----bapuji
                            txtMessage = " Auth BIO";
                            if (devicetype.equalsIgnoreCase("F")) {
                                Global.AUTH_USING = "Fingerprint";
                                KYCAuthXml = getKYCAuthXml("F", aadhaar_no, fpImgString, "bio_auth");
                                AppUtility.writeFileToStorage(KYCAuthXml, "bio_auth");
                            } else if (devicetype.equalsIgnoreCase("I")) {
                                Global.AUTH_USING = "Iris";
                                KYCAuthXml = getKYCAuthXml("I", aadhaar_no, fpImgString, "bio_auth");
                            }
                            Log.e("bio auth xml", "=" + KYCAuthXml);
                            Global.Tempxmldel = KYCAuthXml;
                        } else { *//*                     // For KYC
                        txtMessage = " KYC BIO";
                        if (devicetype.equalsIgnoreCase("F")) {   // For Finger
                            KYCAuthXml = getKYCAuthXml("F", aadhaar_no, fpImgString, "");
                        } else if (devicetype.equalsIgnoreCase("I")) {   //For Iris
                            KYCAuthXml = getKYCAuthXml("I", aadhaar_no, fpImgString, "");
                        }
                        Global.KycXmlForNhps = KYCAuthXml;
                        AppUtility.writeFileToStorage(KYCAuthXml, "XmlForNhpsServer");
                        Log.e("bio kyc xml", "=" + KYCAuthXml);
                          *//*  String encodedKYCXml = Base64.encodeToString(KYCAuthXml.getBytes(), Base64.NO_WRAP);
                            String KYCXml = "";
                            if (devicetype.equalsIgnoreCase("F")) {
                                Global.AUTH_USING = "Fingerprint";
                                KYCXml = getKYCXml("F", aadhaar_no, encodedKYCXml, fpImgString, "bio_ekyc");
                                AppUtility.writeFileToStorage(KYCXml, "bio_ekyc");
                            } else if (devicetype.equalsIgnoreCase("I")) {
                                Global.AUTH_USING = "Iris";
                                KYCXml = getKYCXml("I", aadhaar_no, encodedKYCXml, fpImgString, "bio_ekyc");
                            }
                            Global.Tempxmldel = KYCXml;*//*
                        //   }
                        //						appendLog(Global.Tempxmldel);
//						ShowDialogForSavingData(txtMessage);
                        hitToServerforFINALRequest();
                        *//*checkNetwork = checkConnection.isConnectingToInternet();
                        if (checkNetwork) {
							//							dialogWaitForAuth.dismiss();
							hitToServerforFINALRequest();
						}else {
							log.myMessage(getResources().getString(R.string.network_not_available)+txtMessage + "after scanning", Global.AUTH_AADHAAR);
							ShowPromptNetworkforFinalHit("Network Issue", "Please Enable the network");
						}*//*
                    } else {

                    }
                }
            }
        });
    }

    //	to show network message

    private void ShowPromptNetwork(String title, String message) {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(
                context);
        dlgAlert.setMessage(message);
        dlgAlert.setTitle(title);
        dlgAlert.setIcon(R.drawable.erroricon);
        dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                init(view);
            }
        });
        dlgAlert.setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        activity.finish();
                    }
                });
        dlgAlert.setCancelable(false);
        dlgAlert.create().show();
    }

    private void ShowPromptNetworkforKYC(String title, String message) {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(
                context);
        dlgAlert.setMessage(message);
        dlgAlert.setTitle(title);
        dlgAlert.setIcon(R.drawable.erroricon);
        dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                startCapturingKYC();
            }
        });
        dlgAlert.setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        closeActivity();
                    }
                });
        dlgAlert.setCancelable(false);
        dlgAlert.create().show();
    }

    public void repeatScanning() {
        if (!Global.samsungIris) {
            deviceInit = false;
            if (captureStatus) {
                captureStatus = false;
            }
            try {
                if ((!deviceModel.equalsIgnoreCase("mfs100")))
                    handler.UnInitDevice();
            } catch (Exception dcs) {
                activity.recreate();
            }
        }
        GetConformationAgain();
    }

    private void GetConformationAgain() {

        String message = "";
        message = " Do you want to Scan again?";

        //showInfoDialog("GetConformation b4 dialog");
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(
                context);

        dlgAlert.setMessage(message);
        dlgAlert.setTitle("Confirmation");
        dlgAlert.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        Log.e("click", "yes");
                        if (Global.scannerAttached) {
                            //AsynchronousPermissionCheck();  .....................Bapuji checking  ...not required methods
                            hideAadhaarControl();
                            showScanControl();
                            captureStatus = false;

                           *//* Boolean activate = activateIrisLicense();
                            mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
                            mSensorManager.registerListener(proximitySensorEventListener,
                                    mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY),
                                    SensorManager.SENSOR_DELAY_NORMAL);
                            if (activate) {
                                countDownSamsung.start();
                                Toast.makeText(context, "Scanning Start", Toast.LENGTH_SHORT).show();
                                imageViewFingerPrint.setVisibility(View.GONE);
                                startCapture();
                            } else {*//*
                            countDown.start();
                            imageViewFingerPrint.invalidate();
                            imageViewFingerPrint.setImageBitmap(null);
                            imageViewFingerPrint.invalidate();
                            Toast.makeText(context, "Scanning Start", Toast.LENGTH_SHORT).show();
                            handler.BeginCapture();
                            // }


                        } else {
                            CommonMethods.showErrorDialog("Something is Wrong",
                                    "Biometric Scanner not found.");
                        }

                    }
                });
        dlgAlert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // return;
                closeActivity();
            }
        });
        dlgAlert.setCancelable(false);
        dlgAlert.show();

    }

    //	to show message
    private void ShowPrompt(String title, String message) {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(
                activity);
        dlgAlert.setMessage(message);
        dlgAlert.setTitle(title);
        dlgAlert.setIcon(R.drawable.erroricon);
        dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                closeActivity();
            }
        });
        dlgAlert.setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        btn_submit.setEnabled(false);

                    }
                });
        dlgAlert.setCancelable(false);
        dlgAlert.show();
    }

    private Bitmap RawToBitmap(byte[] rawImage, int imageWidth, int imageHeight) {
        // if (rawImage == null)
        // return null;
        byte[] Bits = new byte[rawImage.length * 4];
        int j;
        for (j = 0; j < rawImage.length; j++) {
            Bits[j * 4] = (byte) (rawImage[j]);
            Bits[j * 4 + 1] = (byte) (rawImage[j]);
            Bits[j * 4 + 2] = (byte) (rawImage[j]);
            Bits[j * 4 + 3] = -1;
        }
        Bitmap mCurrentBitmap = Bitmap.createBitmap(imageWidth, imageHeight,
                Bitmap.Config.ARGB_8888);
        mCurrentBitmap.copyPixelsFromBuffer(ByteBuffer.wrap(Bits));
        return mCurrentBitmap;
    }

    *//*public void appendLogInvalidXmlAWithoutRad(String text, String loc) {
        //File logFile = new File("sdcard/BIO_KYC"+ctime+".txt");
        File logFile = new File("sdcard/" + loc + ".txt");
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        try {
            //BufferedWriter for performance, true to set append to file flag
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, false));
            buf.append(text);
            buf.newLine();
            buf.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            Log.e("IOException", "appendlog***** " + e.getMessage());
            e.printStackTrace();
        }
    }*//*

    //	to create KYC XML
    public String getKYCXml(String deviceType, String aadhaar_no, String encodedXml, String fpImgString, String atype) {
        String kycXml = "";
        byte[] publicKey = null;
        //		GetPubKeycertificateData();
        publicKey = Base64.decode(Global.productionPublicKey, Base64.DEFAULT);
        if (deviceType.equalsIgnoreCase("F") || deviceType.equalsIgnoreCase("I")) {
            UidaiAuthHelper helper = new UidaiAuthHelper(publicKey);

            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String IMEINumber = tm.getDeviceId();
            Global.imei = IMEINumber;
            kycXml = helper.createXmlForKYC(aadhaar_no, deviceType, encodedXml, fpImgString, IMEINumber, atype);

        }
        return kycXml;

    }

    public String getKYCXmlSamsung(String deviceType, String aadhaar_no, String encodedXml, String atype) {
        String kycXml = "";
        byte[] publicKey = null;
        //		GetPubKeycertificateData();
        publicKey = Base64.decode(Global.productionPublicKey, Base64.DEFAULT);
        if (deviceType.equalsIgnoreCase("F") || deviceType.equalsIgnoreCase("I")) {
            UidaiAuthHelper helper = new UidaiAuthHelper(publicKey);

            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String IMEINumber = tm.getDeviceId();
            Global.imei = IMEINumber;
            kycXml = helper.createXmlForKYC(aadhaar_no, deviceType, encodedXml, " ", IMEINumber, atype);
            Global.KYCXML = kycXml;
            //  hitToServerforFINALRequest();
            AppUtility.writeFileToStorage(kycXml, "getKYCXmlSamsung");
        }
        return kycXml;

    }

    //	to create KYC auth/encoded Rad xml
    public String getKYCAuthXml(String deviceType, String aadhaar_no, String fpImgString, String atype) {
        String kycXml = "";

        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String IMEINumber = tm.getDeviceId();
        Global.imei = IMEINumber;
        byte[] publicKey = null;
        //		GetPubKeycertificateData();
        publicKey = Base64.decode(Global.productionPublicKey, Base64.DEFAULT);
        if (deviceType.equalsIgnoreCase("F") || deviceType.equalsIgnoreCase("I")) {
            UidaiAuthHelper helper = new UidaiAuthHelper(publicKey);

            // saurabh 08.09.2107
            if (isKycEnabled) {
                kycXml = helper.createXmlForKycNew(deviceType, aadhaar_no, fpImgString, false, "");

            } else {
                kycXml = helper.createXmlForOtpAuthNew(deviceType, aadhaar_no, fpImgString, false, "");

            }

            if (atype.equalsIgnoreCase("bio_auth")) {
                kycXml = helper.createCustomXmlForAuth(kycXml, atype);
            }
        }

        return kycXml;

    }

    public String getKycAuthXMLForSamsung(String deviceType, String aadhaar, String atype) {
        String samsungAuthXml = "";
        byte[] publicKey = null;

        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String IMEINumber = tm.getDeviceId();
        Global.imei = IMEINumber;
        //		GetPubKeycertificateData();
        publicKey = Base64.decode(Global.productionPublicKey, Base64.DEFAULT);
        UidaiAuthHelper helper = new UidaiAuthHelper(publicKey);
        samsungAuthXml = helper.createXmlForSamsungKycAuth(deviceType, false, aadhaar);
        if (atype.equalsIgnoreCase("bio_auth")) {
            samsungAuthXml = helper.createCustomXmlForAuth(samsungAuthXml, atype);
        }
        AppUtility.writeFileToStorage(samsungAuthXml, "getKycAuthXMLSamsung");
        return samsungAuthXml;

    }

    public String getKYCAuthXmlForINFOCUS(String deviceType, String aadhaar_no, String fpImgString, String atype) {
        String kycXml = "";
        byte[] publicKey = null;
        //		GetPubKeycertificateData();

        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String IMEINumber = tm.getDeviceId();
        Global.imei = IMEINumber;
        publicKey = Base64.decode(Global.productionPublicKey, Base64.DEFAULT);
        if (deviceType.equalsIgnoreCase("F") || deviceType.equalsIgnoreCase("I")) {
            UidaiAuthHelper helper = new UidaiAuthHelper(publicKey);
            kycXml = helper.createXmlForKycAuthForINFOCUS(deviceType, aadhaar_no, fpImgString, false, "");
            if (atype.equalsIgnoreCase("bio_auth")) {
                kycXml = helper.createCustomXmlForAuth(kycXml, atype);
            }
        }

        return kycXml;

    }

    private void hitToServerforFINALRequest() {
        checkNetwork = checkConnection.isConnectingToInternet();
        countDown.cancel();
        countDownSamsung.cancel();
        if (checkNetwork) {
            startTime = System.currentTimeMillis();
            HitToServer task = new HitToServer();
            task.execute();
        } else {
            ShowPromptNetworkforFinalHit("Network Issue", "Please Enable the network");
        }
    }

    private void ShowPromptNetworkforFinalHit(String title, String message) {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(
                context);
        dlgAlert.setMessage(message);
        dlgAlert.setTitle(title);
        dlgAlert.setIcon(R.drawable.erroricon);
        dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                hitToServerforFINALRequest();

            }
        });
        dlgAlert.setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        closeActivity();
                    }
                });
        dlgAlert.setCancelable(false);
        dlgAlert.create().show();
    }

    private void ShowNoResponse(String error) {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(
                activity);
        dlgAlert.setCancelable(false);
        String message = null;

        message = error;
        dlgAlert.setMessage(message);
        dlgAlert.setTitle("Warning");
        dlgAlert.setIcon(R.drawable.erroricon);
        dlgAlert.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                hitToServerforFINALRequest();
            }
        });
        dlgAlert.setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        closeActivity();
                    }
                });

        dlgAlert.setCancelable(false);
        dlgAlert.create().show();
    }

    private void ShowErrorMessage(String error) {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(
                context);
        dlgAlert.setCancelable(false);
        String message = null;

        message = error;
        dlgAlert.setMessage(message);
        dlgAlert.setTitle("Warning");
        dlgAlert.setIcon(R.drawable.erroricon);
        dlgAlert.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                hitToServerforFINALRequest();
            }
        });
        dlgAlert.setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        closeActivity();
                    }
                });
        dlgAlert.setCancelable(false);
        dlgAlert.create().show();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        try {
            activity.unregisterReceiver(mUsbReceiver);

            //activity.unregisterReceiver();
        } catch (Exception ex) {

        }
        try {

            handler1.removeCallbacksAndMessages(null);

            //activity.unregisterReceiver();
        } catch (Exception ex) {

        }
        try {
            handler.UnInitDevice();
            //activity.unregisterReceiver();
        } catch (Exception ex) {

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            activity.unregisterReceiver(mUsbReceiver);

            //activity.unregisterReceiver();
        } catch (Exception ex) {

        }
        try {

            handler1.removeCallbacksAndMessages(null);

            //activity.unregisterReceiver();
        } catch (Exception ex) {

        }
        try {
            handler.UnInitDevice();
            //activity.unregisterReceiver();
        } catch (Exception ex) {

        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try {
            activity.unregisterReceiver(mUsbReceiver);

            //activity.unregisterReceiver();
        } catch (Exception ex) {

        }
        try {

            handler1.removeCallbacksAndMessages(null);

            //activity.unregisterReceiver();
        } catch (Exception ex) {

        }
        try {
            handler.UnInitDevice();
            //activity.unregisterReceiver();
        } catch (Exception ex) {

        }
    }

    *//* public void readXml(String resp) {
         String JSON = null;
         try {
             XmlToJson xmlToJson = new XmlToJson.Builder(resp).build();
             JSON = xmlToJson.toString();
         } catch (Exception ex) {
             ShowErrorMessage("Invalid Response");
         }
         updateKycButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 activity.finish();
             }
         });
         if (JSON != null) {
             KycResponse kycModel = setKycData(JSON);
             if (kycModel != null) {
                 updateKycButton.setVisibility(View.VISIBLE);
                 if (kycModel.getKycRes() != null && kycModel.getKycRes().getRet() != null && kycModel.getKycRes().getRet().equalsIgnoreCase("Y")) {
                     updateKycButton.setText("Save");

                     kycDetailLayout.setVisibility(View.VISIBLE);
                     kycErrorLayout.setVisibility(View.GONE);
                     if (kycModel.getKycRes().getUidData().getPoi().getName() != null)
                         kycName.setText(kycModel.getKycRes().getUidData().getPoi().getName());
                     if (kycModel.getKycRes().getUidData().getPoi().getDob() != null)
                         kycDob.setText(kycModel.getKycRes().getUidData().getPoi().getDob());
                     if (kycModel.getKycRes().getUidData().getPoa().getCo() != null)
                         kycCareOf.setText(kycModel.getKycRes().getUidData().getPoa().getCo());
                     if (kycModel.getKycRes().getUidData().getPoi().getEmail() != null)
                         kycEmail.setText(kycModel.getKycRes().getUidData().getPoi().getEmail());
                     if (kycModel.getKycRes().getUidData().getPoi().getGender() != null)
                         kycGender.setText(kycModel.getKycRes().getUidData().getPoi().getGender());
                     if (kycModel.getKycRes().getUidData().getPoi().getPhone() != null)
                         kycPhone.setText(kycModel.getKycRes().getUidData().getPoi().getPhone() + "");
                     StringBuilder addr = new StringBuilder();
                     if (kycModel.getKycRes().getUidData().getPoa().getStreet() != null)
                         addr.append(kycModel.getKycRes().getUidData().getPoa().getStreet());
                     if (kycModel.getKycRes().getUidData().getPoa().getLm() != null)
                         addr.append("," + kycModel.getKycRes().getUidData().getPoa().getLm());
                     if (kycModel.getKycRes().getUidData().getPoa().getHouse() != null)
                         addr.append("," + kycModel.getKycRes().getUidData().getPoa().getHouse());
                     if (kycModel.getKycRes().getUidData().getPoa().getState() != null)
                         addr.append("," + kycModel.getKycRes().getUidData().getPoa().getState());

                     kycAddr.setText(addr.toString());
                     if (kycModel.getKycRes().getTs() != null)
                         kycTs.setText(kycModel.getKycRes().getTs());
                     if (kycModel.getKycRes().getTxn() != null)
                         kycTxn.setText(kycModel.getKycRes().getTxn());
                     endTime = System.currentTimeMillis();
                     totalTime = endTime - startTime;
                     kycRespTs.setText(totalTime + " miliseconds");

                 } else if (kycModel.getKycRes() != null && kycModel.getKycRes().getRet() != null && kycModel.getKycRes().getRet().equalsIgnoreCase("N") && kycModel.getKycRes().getErr() != null && !kycModel.getKycRes().getErr().equalsIgnoreCase("")) {

                     updateKycButton.setText("Cancel");

                     kycDetailLayout.setVisibility(View.GONE);
                     kycErrorLayout.setVisibility(View.VISIBLE);
                     String err = kycModel.getKycRes().getErr();
                     if (err.equalsIgnoreCase("K-100")) {
                         err = "Resident authentication failed-" + err;
                     } else if (err.equalsIgnoreCase("K-200")) {
                         err = "Resident data currently not available-" + err;
                     } else if (err.equalsIgnoreCase("K-540")) {
                         err = "Invalid KYC XML-" + err;
                     } else if (err.equalsIgnoreCase("K-541")) {
                         err = "Invalid e-KYC API version-" + err;
                     } else if (err.equalsIgnoreCase("K-542")) {
                         err = "Invalid resident consent-" + err;
                     } else if (err.equalsIgnoreCase("K-543")) {
                         err = "Invalid timestamp-" + err;
                     } else if (err.equalsIgnoreCase("K-544")) {
                         err = "Invalid resident auth type-" + err;
                     } else if (err.equalsIgnoreCase("K-545")) {
                         err = err + "\n" + "Resident has opted-out of this service-" + err;
                     } else if (err.equalsIgnoreCase("K-550")) {
                         err = err + "\n" + "Invalid Uses Attribute-" + err;
                     } else if (err.equalsIgnoreCase("K-551")) {
                         err = "Invalid Txn namespace-" + err;
                     } else if (err.equalsIgnoreCase("K-552")) {
                         err = "Invalid License key-" + err;
                     } else if (err.equalsIgnoreCase("K-569")) {
                         err = "Digital signature verification failed for e-KYC XML-" + err;
                     } else if (err.equalsIgnoreCase("K-570")) {
                         err = "Invalid key info in digital signature for e-KYC XML-" + err;
                     } else if (err.equalsIgnoreCase("K-600")) {
                         err = "AUA is invalid or not an authorized KUA-" + err;
                     } else if (err.equalsIgnoreCase("K-601")) {
                         err = "ASA is invalid or not an authorized KSA-" + err;
                     } else if (err.equalsIgnoreCase("K-602")) {
                         err = "KUA encryption key not available-" + err;
                     } else if (err.equalsIgnoreCase("K-603")) {
                         err = "KSA encryption key not available-" + err;
                     } else if (err.equalsIgnoreCase("K-604")) {
                         err = "KSA Signature not allowed-" + err;
                     } else if (err.equalsIgnoreCase("K-605")) {
                         err = "Neither KUA key nor KSA encryption key are available-" + err;
                     } else if (err.equalsIgnoreCase("K-955")) {
                         err = "Technical Failure-" + err;
                     } else if (err.equalsIgnoreCase("K-999")) {
                         err = "Unknown error-" + err;
                     } else if (err.equalsIgnoreCase("9903")) {
                         err = "e-KYC failed";
                     } else {
                         err = err + "\n" + " e-KYC failed";
                     }
                     endTime = System.currentTimeMillis();
                     totalTime = endTime - startTime;
                     kycErrorTextView.setText(err + "\n\n" + "Response time : " + totalTime);
                 }
             }
         }

     }*//*
    public void readXml(String resp) {
        AppUtility.writeFileToStorage(resp, "E-kyc_with_BIO/XML");
        AppUtility.writeFileToStorage(resp, "E-kyc casdkfjadlvhndsalvk;");
        String JSON = null;


        if (resp != null) {
            if (isKycEnabled) {
                // ShowKycData(JSON);
                ShowKycDataNew(resp);
            } else {
                try {
                    XmlToJson xmlToJson = new XmlToJson.Builder(resp).build();
                    JSON = xmlToJson.toString();
                } catch (Exception ex) {
                    ShowErrorMessage("Invalid Response");
                }
                demoAuthResp = new AadhaarDemoAuthResponse().create(JSON);
                if (demoAuthResp != null) {
                    if (demoAuthResp.getAuthRes() != null && demoAuthResp.getAuthRes().getRet() != null && !demoAuthResp.getAuthRes().getRet().equalsIgnoreCase("")) {
                        if (demoAuthResp.getAuthRes().getRet().equalsIgnoreCase("Y")) {
                            // alertWithOk(context,"Aadhar auth successfull");
                            alertWithOk(context, "Aadhar auth successfull");
                        } else {
                            AppUtility.alertWithOk(context, "Aadhar auth failed \n" + demoAuthResp.getAuthRes().getErr());
                        }

                    } else {
                        AppUtility.alertWithOk(context, "Aadhar auth failed " + JSON);
                    }
                } else {
                    AppUtility.alertWithOk(context, "Aadhar auth failed " + JSON);
                }
            }
        }


    }

    public void alertWithOk(Context mContext, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Alert");
        builder.setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                        seccItem.setAadhaarAuth(AppConstant.VALID_STATUS);
                        qrCodeSubmitAaadhaarDetail();
                        getActivity().finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void qrCodeSubmitAaadhaarDetail() {
        if (isKycEnabled) {
            seccItem.setAadhaarVerifiedBy(loginResponse.getAadhaarNumber());
            if (aadhaarKycResponse != null) {
                seccItem.setAadhaarCapturingMode(AppConstant.FINGER_MODE);
                seccItem.setAadhaarNo(aadhaarKycResponse.getUid());
                seccItem.setNameAadhaar(aadhaarKycResponse.getName());
                seccItem.setAadhaarGender(aadhaarKycResponse.getGender());
                seccItem.setAadhaarDob(aadhaarKycResponse.getDob());
                seccItem.setConsent(consent);
                seccItem.setAadhaarSurveyedStat(AppConstant.SURVEYED + "");
                seccItem.setAadhaarAuth(AppConstant.VALID_STATUS);
                seccItem.setAadhaarAuthDt(String.valueOf(DateTimeUtil.currentTimeMillis()));
                if (seccItem.getAadhaarStatus().equalsIgnoreCase(AppConstant.AADHAAR_STAT)) {

                    updateAadhaarDetail();
                }
            }
         *//*   if (kycModel != null) {
                seccItem.setAadhaarCapturingMode(AppConstant.FINGER_MODE);
                seccItem.setAadhaarNo(kycModel.getKycRes().getUidData().getUid());
                seccItem.setNameAadhaar(kycModel.getKycRes().getUidData().getPoi().getName());
                seccItem.setAadhaarGender(kycModel.getKycRes().getUidData().getPoi().getGender());
                seccItem.setAadhaarDob(kycModel.getKycRes().getUidData().getPoi().getDob());
                seccItem.setConsent(consent);
                seccItem.setAadhaarSurveyedStat(AppConstant.SURVEYED + "");
                seccItem.setAadhaarAuth(AppConstant.VALID_STATUS);
                seccItem.setAadhaarAuthDt(String.valueOf(DateTimeUtil.currentTimeMillis()));
                if (seccItem.getAadhaarStatus().equalsIgnoreCase(AppConstant.AADHAAR_STAT)) {

                    updateAadhaarDetail();
                }
            }*//*
        } else {


            seccItem.setAadhaarVerifiedBy(loginResponse.getAadhaarNumber());

            seccItem.setAadhaarCapturingMode(AppConstant.FINGER_MODE);
          *//*  seccItem.setAadhaarNo(kycModel.getKycRes().getUidData().getUid());
            seccItem.setNameAadhaar(kycModel.getKycRes().getUidData().getPoi().getName());
            seccItem.setAadhaarGender(kycModel.getKycRes().getUidData().getPoi().getGender());
            seccItem.setAadhaarDob(kycModel.getKycRes().getUidData().getPoi().getDob());*//*
            seccItem.setConsent(consent);
            seccItem.setAadhaarSurveyedStat(AppConstant.SURVEYED + "");
            seccItem.setAadhaarAuth(AppConstant.VALID_STATUS);
            seccItem.setAadhaarAuthDt(String.valueOf(DateTimeUtil.currentTimeMillis()));
            if (seccItem.getAadhaarStatus().equalsIgnoreCase(AppConstant.AADHAAR_STAT)) {

                updateAadhaarDetail();

            }
        }
    }

    public class CountDown extends CountDownTimer {

        public CountDown(long startTime, long interval) {
            super(startTime, interval);
        }

        @Override
        public void onFinish() {

            textviewTimer.setTextColor(Color.parseColor("#d62d20"));
            textviewTimer.setText(R.string.fingure_scan_time_up);
            if (!captureStatus) {                                                              // CAPTURE STATUS FALSE HERE ALL THE TIMES
                if (btnype.equalsIgnoreCase("bio_auth"))
                    log.myMessage("No fingure captured" + " KYC Auth", Global.AUTH_AADHAAR);
                else
                    log.myMessage("No fingure captured" + " KYC BIO", Global.AUTH_AADHAAR);
                //  repeatScanning();
            }

        }

        @Override
        public void onTick(long millisUntilFinished) {
            Log.e("cowntown", "== " + millisUntilFinished / 1000);
            try {
                textviewTimer.setTextColor(Color.parseColor("#000000"));
                textviewTimer.setText(getResources().getString(R.string.scan_your_fingure) + millisUntilFinished / 1000);
            } catch (Exception ex) {

            }
        }
    }

    public class CountDownSamsung extends CountDownTimer {

        public CountDownSamsung(long startTime, long interval) {
            super(startTime, interval);
        }

        @Override
        public void onFinish() {

            textviewTimer.setTextColor(Color.parseColor("#d62d20"));
            textviewTimer.setText(R.string.eye_scan_time_up);
            if (!captureStatus) {                                                              // CAPTURE STATUS FALSE HERE ALL THE TIMES
                if (btnype.equalsIgnoreCase("bio_auth"))
                    log.myMessage("No fingure captured" + " KYC Auth", Global.AUTH_AADHAAR);
                else
                    log.myMessage("No fingure captured" + " KYC BIO", Global.AUTH_AADHAAR);
                // repeatScanning();
            }

        }

        @Override
        public void onTick(long millisUntilFinished) {
            Log.e("cowntown", "== " + millisUntilFinished / 1000);
            textviewTimer.setTextColor(Color.parseColor("#000000"));
            textviewTimer.setText(getResources().getString(R.string.scan_your_eye) + millisUntilFinished / 1000);

        }
    }

    private class GetPubKeycertificateData extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {

            String result = null;
            StringBuilder total = new StringBuilder();


            try {

                InputStream is = getResources().openRawResource(
                        R.raw.uidai_auth_prod);

                BufferedReader r = new BufferedReader(new InputStreamReader(is));
                System.out.println("4444444444444444444");

                String line = "";
                System.out.println("55555555555555");
                while ((line = r.readLine()) != null) {
                    // 	total.append();
                    //  	total.append();
                    total.append(line);

                }
                System.out.println("6666666666666");
                result = total.toString();
                Log.e("result", "==" + result);

            } catch (Exception e) {
                Log.e("GetPubKeycertifica", "=" + e);
                ShowPrompt("Connection Issue", "Please go back...");
                System.out.println("errrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr");
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            barProgressDialog.dismiss();
            if ((result.startsWith("-----BEGIN CERTIFICATE-----") && result.endsWith("-----END CERTIFICATE-----"))) {
                result = result.replace("-----BEGIN CERTIFICATE-----", "");
                result = result.replace("-----END CERTIFICATE-----", "");
                result = result.replace("\r\n", "");
                Global.productionPublicKey = result;
            }
            //			if (result.endsWith("=") ) {
            //				result=result.replace("\r\n", "");
            //				Global.productionPublicKey=result;
            //			}
            else {
                //				ShowPrompt("Critical Error", "Please go back...");
                GetPubKeycertificateData publicKey1 = new GetPubKeycertificateData();
                publicKey1.execute();
            }

        }
    }

    //	To hit server
    private class HitToServer extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            captureStatus = true;
            String res = "";
            Log.e("request xml", "=====" + Global.Tempxmldel);
            //   String
            //   res = CommonMethods.HttpPostLifeCerticiate(Global.KYC_URL, Global.Tempxmldel);
            if (isKycEnabled) {
                res = CommonMethods.HttpPostLifeCerticiate(Global.KYC_URL_NEW, Global.KycXmlForNhps, AppConstant.AUTHORIZATION, AppConstant.AUTHORIZATIONVALUE);

            } else {

                res = CommonMethods.HttpPostLifeCerticiate(AppConstant.REQUEST_FOR_OTP_AUTH, Global.KycXmlForNhps, AppConstant.AUTHORIZATION, AppConstant.AUTHORIZATIONVALUE);
            }
            //APPEND TO TEXT FILES
//						appendLog(res);
            Log.e("Response", "==" + res);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            dialogWaitForAuth.dismiss();

            if (result.equalsIgnoreCase("ERROR") || result.equalsIgnoreCase("False from server") || result.equalsIgnoreCase("Connection time out Error")) {
                log.myMessage(result + " KYC BIO", Global.AUTH_AADHAAR);
                ShowErrorMessage(result);
            } else if (result.equalsIgnoreCase("")) {
                log.myMessage("No response from Server" + " KYC BIO", Global.AUTH_AADHAAR);
                ShowNoResponse("No response from Server");
            } else {
                edtxt_Aadhaar.setText("");
                rgDevice.clearCheck();
                readXml(result);
            }
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            if (Global.samsungIris) {
                dialogWaitForAuth.show();
            }
        }
    }

    private KycResponse setKycData(String resp) {
        hideAadhaarControl();
        hideScanControl();
        KycResponse model = new KycResponse();
//       model.create(resp);


        String TAG_KYCRES = "KycRes";
        String TAG_RET = "ret";
        String TAG_UIDDATA = "UidData";
        String TAG_UID = "uid";
        String TAG_POA = "Poa";
        String TAG_COUNTRY = "country";
        String TAG_SUBDIST = "subdist";
        String TAG_LM = "lm";
        String TAG_HOUSE = "house";
        String TAG_PC = "pc";
        String TAG_VTC = "vtc";
        String TAG_STREET = "street";
        String TAG_DIST = "dist";
        String TAG_STATE = "state";
        String TAG_CO = "co";
        String TAG_POI = "Poi";
        String TAG_GENDER = "gender";
        String TAG_PHONE = "phone";
        String TAG_DOB = "dob";
        String TAG_POI_OBJ_NAME = "name";
        String TAG_EMAIL = "email";
        String TAG_TXN = "txn";
        String TAG_TS = "ts";
        String TAG_PHOTO = "Pht";
        JSONObject json = null;
        AadharDataModel aadharModel = new AadharDataModel();
        UidData uidiaModel = new UidData();
        Poi poiModel = new Poi();
        Poa poaModel = new Poa();
        try {
            json = new JSONObject(resp);

            try {

                JSONObject KycRes_obj = json.getJSONObject(TAG_KYCRES);
                String str_ret = KycRes_obj.getString(TAG_RET);
                aadharModel.setRet(str_ret);
                if (str_ret != null && !str_ret.equalsIgnoreCase("Y")) {
                    aadharModel.setErr(KycRes_obj.getString("err"));
                } else {


                    JSONObject UidData_obj = KycRes_obj.getJSONObject(TAG_UIDDATA);
                    try {
                        uidiaModel.setPht(UidData_obj.getString(TAG_PHOTO));
                    } catch (Exception ex) {

                    }
                    try {
                        String str_uid = UidData_obj.getString(TAG_UID);
                        uidiaModel.setUid(str_uid);
                    } catch (Exception ex) {

                    }
                    JSONObject Poa_obj = UidData_obj.getJSONObject(TAG_POA);
                    try {
                        String str_country = Poa_obj.getString(TAG_COUNTRY);
                        poaModel.setCountry(str_country);
                    } catch (Exception ex) {

                    }
                    try {
                        String str_subdist = Poa_obj.getString(TAG_SUBDIST);
                        poaModel.setSubdist(str_subdist);
                    } catch (Exception ex) {

                    }
                    try {
                        String str_lm = Poa_obj.getString(TAG_LM);
                        poaModel.setLm(str_lm);
                    } catch (Exception ex) {

                    }
                    try {
                        String str_pc = Poa_obj.getString(TAG_PC);
                        poaModel.setPc(str_pc);
                    } catch (Exception ex) {

                    }
                    try {
                        String str_vtc = Poa_obj.getString(TAG_VTC);
                        poaModel.setVtc(str_vtc);
                    } catch (Exception ex) {

                    }
                    try {
                        String str_street = Poa_obj.getString(TAG_STREET);
                        poaModel.setStreet(str_street);
                    } catch (Exception ex) {

                    }
                    try {
                        String str_dist = Poa_obj.getString(TAG_DIST);
                        poaModel.setDist(str_dist);
                    } catch (Exception ex) {

                    }
                    try {
                        String str_state = Poa_obj.getString(TAG_STATE);
                        poaModel.setState(str_state);
                    } catch (Exception ex) {

                    }
                    try {
                        String str_co = Poa_obj.getString(TAG_CO);
                        poaModel.setCo(str_co);
                    } catch (Exception ex) {

                    }
                    try {
                        String str_ho = Poa_obj.getString(TAG_HOUSE);
                        poaModel.setHouse(str_ho);

                    } catch (Exception ex) {

                    }
                    JSONObject Poi_obj = UidData_obj.getJSONObject(TAG_POI);
                    try {
                        String str_gender = Poi_obj.getString(TAG_GENDER);
                        poiModel.setGender(str_gender);
                    } catch (Exception ex) {

                    }
                    try {
                        String str_phone = Poi_obj.getString(TAG_PHONE);
                        poiModel.setPhone(str_phone);
                    } catch (Exception ex) {

                    }
                    try {
                        String str_dob = Poi_obj.getString(TAG_DOB);
                        poiModel.setDob(str_dob);
                    } catch (Exception ex) {

                    }
                    try {
                        String str_Poi_obj_name = Poi_obj.getString(TAG_POI_OBJ_NAME);
                        poiModel.setName(str_Poi_obj_name);
                    } catch (Exception ex) {

                    }
                    try {
                        String str_email = Poi_obj.getString(TAG_EMAIL);
                        poiModel.setEmail(str_email);
                    } catch (Exception ex) {
                    }
                    try {
                        String str_txn = KycRes_obj.getString(TAG_TXN);
                        aadharModel.setTxn(str_txn);
                    } catch (Exception ex) {
                    }
                    try {
                        String str_ts = KycRes_obj.getString(TAG_TS);
                        aadharModel.setTs(str_ts);
                    } catch (Exception ex) {
                    }
                }
                uidiaModel.setPoa(poaModel);
                uidiaModel.setPoi(poiModel);
                aadharModel.setUidData(uidiaModel);
                model.setKycRes(aadharModel);
            } catch (JSONException e) {
                JSONObject KycRes_obj = json.getJSONObject("Resp");
                aadharModel.setErr(KycRes_obj.getString("err"));
                aadharModel.setRet(KycRes_obj.getString("ret"));
                model.setKycRes(aadharModel);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return model;
    }


    private void alertForValidateLater(String aadharName, SeccMemberItem item) {
        internetDiaolg = new AlertDialog.Builder(context).create();
        LayoutInflater factory = LayoutInflater.from(context);
        View alertView = factory.inflate(R.layout.aadhar_validation_msg_popup, null);
        internetDiaolg.setView(alertView);

        try {
            if (internetDiaolg != null) {
                internetDiaolg.show();
            }
        } catch (Exception ead) {

        }
        TextView nameAsInAadharTV = (TextView) alertView.findViewById(R.id.nameAsInAadharTV);
        TextView nameAsInSeccTV = (TextView) alertView.findViewById(R.id.nameAsInSeccTV);
        nameAsInAadharTV.setText(aadharName);
        nameAsInSeccTV.setText(item.getName());
        Button tryAgainBT = (Button) alertView.findViewById(R.id.tryAgainBT);
        tryAgainBT.setText(context.getResources().getString(R.string.Confirm_Submit));
        final Button cancelBT = (Button) alertView.findViewById(R.id.cancelBT);
        tryAgainBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seccItem.setAadhaarAuth(AppConstant.VALID_STATUS);
                qrCodeSubmitAaadhaarDetail();
            }
        });
        cancelBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                internetDiaolg.dismiss();
            }
        });
    }


    private void updateAadhaarDetail() {
        Intent theIntent = new Intent(context, WithAadhaarActivity.class);
        seccItem.setLockedSave(AppConstant.SAVE + "");
        if (seccItem != null && seccItem.getDataSource() != null && seccItem.getDataSource()
                .trim().equalsIgnoreCase(AppConstant.RSBY_SOURCE)) {
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
            SeccDatabase.updateRsbyHousehold(selectedMemItem.getHouseHoldItem(), context);
            seccItem = SeccDatabase.getSeccMemberDetail(seccItem, context);

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
        }

        selectedMemItem.setSeccMemberItem(seccItem);
        ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.SELECTED_ITEM_FOR_VERIFICATION, selectedMemItem.serialize(), context);
        startActivity(theIntent);
        activity.finish();
        activity.rightTransition();

    }

    private void ShowKycData(String JSON) {
        kycModel = setKycData(JSON);
        AppUtility.writeFileToStorage(JSON, "E-kyc_with_BIO/JSON");
        if (kycModel != null) {

            kycCompleted = true;
            if (kycModel.getKycRes() != null && kycModel.getKycRes().getRet() != null
                    && kycModel.getKycRes().getRet().equalsIgnoreCase("Y")) {
                updateKycButton.setText("Save");
                updateKycButton.setVisibility(View.VISIBLE);
                cancelButton.setVisibility(View.VISIBLE);

                kycDetailLayout.setVisibility(View.VISIBLE);
                kycErrorLayout.setVisibility(View.GONE);

                if (kycModel.getKycRes().getUidData().getPht() != null && !kycModel.getKycRes().getUidData().getPht().equalsIgnoreCase("")) {
                    aadharImageLinearLayout.setVisibility(View.VISIBLE);
                    Bitmap imageBitmap = AppUtility.convertStringToBitmap(kycModel.getKycRes().getUidData().getPht());
                    if (imageBitmap != null) {
                        aadharImageView.setImageBitmap(imageBitmap);
                    }

                } else {
                    aadharImageLinearLayout.setVisibility(View.GONE);
                }
                if (kycModel.getKycRes().getUidData().getPoi().getName() != null)
                    kycName.setText(kycModel.getKycRes().getUidData().getPoi().getName());
                if (kycModel.getKycRes().getUidData().getPoi().getDob() != null)
                    kycDob.setText(kycModel.getKycRes().getUidData().getPoi().getDob());
                  *//*  if (kycModel.getKycRes().getUidData().getPoa().getCo() != null)
                        kycCareOf.setText(kycModel.getKycRes().getUidData().getPoa().getCo());*//*
                if (kycModel.getKycRes().getUidData().getPoi().getEmail() != null)
                    kycEmail.setText(kycModel.getKycRes().getUidData().getPoi().getEmail());
                if (kycModel.getKycRes().getUidData().getPoi().getGender() != null)
                    kycGender.setText(kycModel.getKycRes().getUidData().getPoi().getGender());
                if (kycModel.getKycRes().getUidData().getPoi().getPhone() != null)
                    kycPhone.setText(kycModel.getKycRes().getUidData().getPoi().getPhone() + "");
                StringBuilder addr = new StringBuilder();
                if (kycModel.getKycRes().getUidData().getPoa().getCo() != null)
                    addr.append(kycModel.getKycRes().getUidData().getPoa().getCo());
                if (kycModel.getKycRes().getUidData().getPoa().getHouse() != null)
                    addr.append(", " + kycModel.getKycRes().getUidData().getPoa().getHouse());
                if (kycModel.getKycRes().getUidData().getPoa().getStreet() != null)
                    addr.append(", " + kycModel.getKycRes().getUidData().getPoa().getStreet());
                if (kycModel.getKycRes().getUidData().getPoa().getLm() != null)
                    addr.append(", " + kycModel.getKycRes().getUidData().getPoa().getLm());
                if (kycModel.getKycRes().getUidData().getPoa().getVtc() != null)
                    addr.append(", " + kycModel.getKycRes().getUidData().getPoa().getVtc());
                   *//* if (kycModel.getKycRes().getUidData().getPoa().getSubdist() != null)
                        addr.append("," + kycModel.getKycRes().getUidData().getPoa().getSubdist());*//*
                if (kycModel.getKycRes().getUidData().getPoa().getDist() != null)
                    addr.append(", " + kycModel.getKycRes().getUidData().getPoa().getDist());
                if (kycModel.getKycRes().getUidData().getPoa().getState() != null)
                    addr.append(", " + kycModel.getKycRes().getUidData().getPoa().getState());
                if (kycModel.getKycRes().getUidData().getPoa().getCountry() != null)
                    addr.append(", " + kycModel.getKycRes().getUidData().getPoa().getCountry());

                kycAddr.setText(addr.toString());
                if (kycModel.getKycRes().getTs() != null) {

                    kycTs.setText(AppUtility.convetEkycDate(kycModel.getKycRes().getTs()));
                }
                if (kycModel.getKycRes().getTxn() != null) {
                    kycTxn.setText(kycModel.getKycRes().getTxn());

                }
                endTime = System.currentTimeMillis();
                totalTime = endTime - startTime;
                kycRespTs.setText(totalTime + " miliseconds");
                updateKycButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertForValidateLater(kycModel.getKycRes().getUidData().getPoi().getName(), seccItem);
                    }
                });

                cancelButton.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                closeActivity();
                            }
                        }
                );
            } else if (kycModel.getKycRes() != null && kycModel.getKycRes().getRet() != null &&
                    kycModel.getKycRes().getRet().equalsIgnoreCase("N") && kycModel.getKycRes().getErr() != null &&
                    !kycModel.getKycRes().getErr().equalsIgnoreCase("")) {

                updateKycButton.setText("Cancel");
                cancelButton.setVisibility(View.GONE);

                kycDetailLayout.setVisibility(View.GONE);
                kycErrorLayout.setVisibility(View.VISIBLE);
                String err = kycModel.getKycRes().getErr();
                if (err.equalsIgnoreCase("K-100")) {
                    err = "Resident authentication failed-" + err;
                } else if (err.equalsIgnoreCase("K-200")) {
                    err = "Resident data currently not available-" + err;
                } else if (err.equalsIgnoreCase("K-540")) {
                    err = "Invalid KYC XML-" + err;
                } else if (err.equalsIgnoreCase("K-541")) {
                    err = "Invalid e-KYC API version-" + err;
                } else if (err.equalsIgnoreCase("K-542")) {
                    err = "Invalid resident consent-" + err;
                } else if (err.equalsIgnoreCase("K-543")) {
                    err = "Invalid timestamp-" + err;
                } else if (err.equalsIgnoreCase("K-544")) {
                    err = "Invalid resident auth type-" + err;
                } else if (err.equalsIgnoreCase("K-545")) {
                    err = err + "\n" + "Resident has opted-out of this service-" + err;
                } else if (err.equalsIgnoreCase("K-550")) {
                    err = err + "\n" + "Invalid Uses Attribute-" + err;
                } else if (err.equalsIgnoreCase("K-551")) {
                    err = "Invalid Txn namespace-" + err;
                } else if (err.equalsIgnoreCase("K-552")) {
                    err = "Invalid License key-" + err;
                } else if (err.equalsIgnoreCase("K-569")) {
                    err = "Digital signature verification failed for e-KYC XML-" + err;
                } else if (err.equalsIgnoreCase("K-570")) {
                    err = "Invalid key info in digital signature for e-KYC XML-" + err;
                } else if (err.equalsIgnoreCase("K-600")) {
                    err = "AUA is invalid or not an authorized KUA-" + err;
                } else if (err.equalsIgnoreCase("K-601")) {
                    err = "ASA is invalid or not an authorized KSA-" + err;
                } else if (err.equalsIgnoreCase("K-602")) {
                    err = "KUA encryption key not available-" + err;
                } else if (err.equalsIgnoreCase("K-603")) {
                    err = "KSA encryption key not available-" + err;
                } else if (err.equalsIgnoreCase("K-604")) {
                    err = "KSA Signature not allowed-" + err;
                } else if (err.equalsIgnoreCase("K-605")) {
                    err = "Neither KUA key nor KSA encryption key are available-" + err;
                } else if (err.equalsIgnoreCase("K-955")) {
                    err = "Technical Failure-" + err;
                } else if (err.equalsIgnoreCase("K-999")) {
                    err = "Unknown error-" + err;
                } else if (err.equalsIgnoreCase("9903")) {
                    err = "e-KYC failed";
                } else {
                    err = err + "\n" + " e-KYC failed";
                }
                endTime = System.currentTimeMillis();
                totalTime = endTime - startTime;

                kycErrorTextView.setText(err + "\n\n" + "Response time : " + totalTime + " miliseconds");
                updateKycButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        closeActivity();
                    }
                });
            } else {
                updateKycButton.setText("Cancel");
                cancelButton.setVisibility(View.GONE);

                kycDetailLayout.setVisibility(View.GONE);
                kycErrorLayout.setVisibility(View.VISIBLE);
                kycErrorTextView.setText("Unknown Error" + "\n\n" + "Response time : " + totalTime + " miliseconds");
                updateKycButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        closeActivity();
                    }
                });
            }
        }
    }

    private void ShowKycDataNew(String JSON) {

        aadhaarKycResponse = new AadhaarResponseItem().create(JSON);
        AppUtility.writeFileToStorage(JSON, "E-kyc_with_BIO/JSON");
        if (aadhaarKycResponse != null) {

            kycCompleted = true;
            if (aadhaarKycResponse != null && aadhaarKycResponse.getResult() != null
                    && aadhaarKycResponse.getResult().equalsIgnoreCase("Y")) {
                updateKycButton.setText("Save");
                updateKycButton.setVisibility(View.VISIBLE);
                cancelButton.setVisibility(View.VISIBLE);

                kycDetailLayout.setVisibility(View.VISIBLE);
                kycErrorLayout.setVisibility(View.GONE);

                if (aadhaarKycResponse.getBase64() != null && !aadhaarKycResponse.getBase64().equalsIgnoreCase("")) {
                    aadharImageLinearLayout.setVisibility(View.VISIBLE);
                    Bitmap imageBitmap = AppUtility.convertStringToBitmap(aadhaarKycResponse.getBase64());
                    if (imageBitmap != null) {
                        aadharImageView.setImageBitmap(imageBitmap);
                    }

                } else {
                    aadharImageLinearLayout.setVisibility(View.GONE);
                }
                if (aadhaarKycResponse.getName() != null)
                    kycName.setText(aadhaarKycResponse.getName());
                if (aadhaarKycResponse.getDob() != null)
                    kycDob.setText(aadhaarKycResponse.getDob());
                  *//*  if (aadhaarKycResponse.getUidData().getPoa().getCo() != null)
                        kycCareOf.setText(aadhaarKycResponse.getUidData().getPoa().getCo());*//*
                if (aadhaarKycResponse.getEmail() != null)
                    kycEmail.setText(aadhaarKycResponse.getEmail());
                if (aadhaarKycResponse.getGender() != null)
                    kycGender.setText(aadhaarKycResponse.getGender());
                if (aadhaarKycResponse.getPhone() != null)
                    kycPhone.setText(aadhaarKycResponse.getPhone() + "");
                StringBuilder addr = new StringBuilder();
                if (aadhaarKycResponse.getCo() != null)
                    addr.append(aadhaarKycResponse.getCo());
                if (aadhaarKycResponse.getHouse() != null)
                    addr.append(", " + aadhaarKycResponse.getHouse());
                if (aadhaarKycResponse.getStreet() != null)
                    addr.append(", " + aadhaarKycResponse.getStreet());
                if (aadhaarKycResponse.getLm() != null)
                    addr.append(", " + aadhaarKycResponse.getLm());
                if (aadhaarKycResponse.getVtc() != null)
                    addr.append(", " + aadhaarKycResponse.getVtc());
                   *//* if (aadhaarKycResponse.getUidData().getPoa().getSubdist() != null)
                        addr.append("," + aadhaarKycResponse.getUidData().getPoa().getSubdist());*//*
                if (aadhaarKycResponse.getDist() != null)
                    addr.append(", " + aadhaarKycResponse.getDist());
                if (aadhaarKycResponse.getState() != null)
                    addr.append(", " + aadhaarKycResponse.getState());
           *//*     if (aadhaarKycResponse.getC != null)
                    addr.append(", " + aadhaarKycResponse.getUidData().getPoa().getCountry());*//*

                kycAddr.setText(addr.toString());
                if (aadhaarKycResponse.getTs() != null) {
                    kycTs.setText(AppUtility.convetEkycDate(aadhaarKycResponse.getTs()));
                }
                if (aadhaarKycResponse.getTxn() != null) {
                    kycTxn.setText(aadhaarKycResponse.getTxn());

                }
                endTime = System.currentTimeMillis();
                totalTime = endTime - startTime;
                kycRespTs.setText(totalTime + " miliseconds");
                updateKycButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertForValidateLater(aadhaarKycResponse.getName(), seccItem);
                    }
                });

                cancelButton.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                closeActivity();
                            }
                        }
                );
            } else if (aadhaarKycResponse != null && aadhaarKycResponse.getResult() != null &&
                    aadhaarKycResponse.getResult().equalsIgnoreCase("N") && aadhaarKycResponse.getErr() != null &&
                    !aadhaarKycResponse.getErr().equalsIgnoreCase("")) {

                updateKycButton.setText("Cancel");
                cancelButton.setVisibility(View.GONE);

                kycDetailLayout.setVisibility(View.GONE);
                kycErrorLayout.setVisibility(View.VISIBLE);
                String err = aadhaarKycResponse.getErr();
                if (err.equalsIgnoreCase("K-100")) {
                    err = "Resident authentication failed-" + err;
                } else if (err.equalsIgnoreCase("K-200")) {
                    err = "Resident data currently not available-" + err;
                } else if (err.equalsIgnoreCase("K-540")) {
                    err = "Invalid KYC XML-" + err;
                } else if (err.equalsIgnoreCase("K-541")) {
                    err = "Invalid e-KYC API version-" + err;
                } else if (err.equalsIgnoreCase("K-542")) {
                    err = "Invalid resident consent-" + err;
                } else if (err.equalsIgnoreCase("K-543")) {
                    err = "Invalid timestamp-" + err;
                } else if (err.equalsIgnoreCase("K-544")) {
                    err = "Invalid resident auth type-" + err;
                } else if (err.equalsIgnoreCase("K-545")) {
                    err = err + "\n" + "Resident has opted-out of this service-" + err;
                } else if (err.equalsIgnoreCase("K-550")) {
                    err = err + "\n" + "Invalid Uses Attribute-" + err;
                } else if (err.equalsIgnoreCase("K-551")) {
                    err = "Invalid Txn namespace-" + err;
                } else if (err.equalsIgnoreCase("K-552")) {
                    err = "Invalid License key-" + err;
                } else if (err.equalsIgnoreCase("K-569")) {
                    err = "Digital signature verification failed for e-KYC XML-" + err;
                } else if (err.equalsIgnoreCase("K-570")) {
                    err = "Invalid key info in digital signature for e-KYC XML-" + err;
                } else if (err.equalsIgnoreCase("K-600")) {
                    err = "AUA is invalid or not an authorized KUA-" + err;
                } else if (err.equalsIgnoreCase("K-601")) {
                    err = "ASA is invalid or not an authorized KSA-" + err;
                } else if (err.equalsIgnoreCase("K-602")) {
                    err = "KUA encryption key not available-" + err;
                } else if (err.equalsIgnoreCase("K-603")) {
                    err = "KSA encryption key not available-" + err;
                } else if (err.equalsIgnoreCase("K-604")) {
                    err = "KSA Signature not allowed-" + err;
                } else if (err.equalsIgnoreCase("K-605")) {
                    err = "Neither KUA key nor KSA encryption key are available-" + err;
                } else if (err.equalsIgnoreCase("K-955")) {
                    err = "Technical Failure-" + err;
                } else if (err.equalsIgnoreCase("K-999")) {
                    err = "Unknown error-" + err;
                } else if (err.equalsIgnoreCase("9903")) {
                    err = "e-KYC failed";
                } else {
                    err = err + "\n" + " e-KYC failed";
                }
                endTime = System.currentTimeMillis();
                totalTime = endTime - startTime;

                kycErrorTextView.setText(err + "\n\n" + "Response time : " + totalTime + " miliseconds");
                updateKycButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        closeActivity();
                    }
                });
            } else {
                updateKycButton.setText("Cancel");
                cancelButton.setVisibility(View.GONE);

                kycDetailLayout.setVisibility(View.GONE);
                kycErrorLayout.setVisibility(View.VISIBLE);
                kycErrorTextView.setText("Unknown Error" + "\n\n" + "Response time : " + totalTime + " miliseconds");
                updateKycButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        closeActivity();
                    }
                });
            }
        }
    }*/
}

