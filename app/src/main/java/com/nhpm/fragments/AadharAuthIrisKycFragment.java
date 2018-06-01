package com.nhpm.fragments;


import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nhpm.AadhaarUtils.VerhoeffAadhar;
import com.nhpm.Models.KycResponse;
import com.nhpm.Models.response.seccMembers.SeccMemberItem;
import com.nhpm.Models.response.seccMembers.SelectedMemberItem;
import com.nhpm.Models.response.verifier.AadhaarDemoAuthResponse;
import com.nhpm.Models.response.verifier.AadhaarResponseItem;
import com.nhpm.Models.response.verifier.VerifierLoginResponse;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;
import com.nhpm.activity.CaptureAadharDetailActivity;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import javax.security.cert.X509Certificate;

/*
import com.aadhar.CheckConnection;
import com.aadhar.CommonMethods;
import com.aadhar.Global;
import com.aadhar.Logs;
import com.aadhar.ShowDialogWaitForAuth;
import com.aadhar.StatusLogs;
import com.aadhar.UidaiAuthHelper;
import com.aadhar.VerhoeffAadhar;
import com.aadhar.commonapi.DeviceRecognizer;
import com.aadhar.commonapi.HelperInterface;*/
/*import com.sec.biometric.iris.SecIrisCallback;
import com.sec.biometric.iris.SecIrisManager;
import com.sec.biometric.license.SecBiometricLicenseManager;*/

//import com.sec.biometric.license.SecBiometricLicenseManager;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AadharAuthIrisKycFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AadharAuthIrisKycFragment extends Fragment {//implements View.OnClickListener, HelperInterface, SecIrisCallback {
    String ACTION_USB_PERMISSION = "com.aadhar.commonapi.USB_PERMISSION";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "Capture Activity Iris";
    public static UsbManager mManager;
    public static PendingIntent mPermissionIntent = null;
    public View view;
    String[] arry = new String[2];
    File f = new File(Environment.getExternalStorageDirectory(), "Authentication");
    Handler handler1 = new Handler();
    String deviceMake, deviceModel, serialNumber, deviceVendor;
    boolean deviceInit = false;
    Timer timer;
    TimerTask doAsynchronousPermissionCheck;
    String fpImgString;
   /* ShowDialogWaitForAuth dialogWaitForAuth;
    ProgressDialog barProgressDialog;
    CheckConnection checkConnection;
    boolean checkNetwork;
    long startTime, endTime, totalTime;
    Logs log;
    StatusLogs statusLog;*/
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Context context;
    /*public DeviceRecognizer deviceRecognizer = new DeviceRecognizer(context);*/
    private CaptureAadharDetailActivity activity;
    private LinearLayout ll_Scan_main, ll_scan_bottom;
    private ImageView imageViewFingerPrint, imageViewConnectionStatus, imageViewDeviceStatus;
    private ProgressBar progressBarFPQuality;
    private Button buttonEnd, buttonScan, btn_auth;
    private TextView textViewStatus;
    private EditText edtxt_Aadhaar;
    private Button btn_submit, btn_home;
    private boolean validAadhaar = false;
    private LinearLayout aadhaar_layout;
    private RadioGroup rgDevice;
    private RelativeLayout backLayout;
    private ImageView back;
   /* private com.aadhar.commonapi.BiometricDeviceHandler handler;
    private String xml, aadhaar, btnype = "bio_ekyc";
    private CountDown countDown;
    private CountDownSamsung countDownSamsung;*/
    private TextView textviewTimer, aadharNumResetEditText;
    private boolean captureStatus;
    private boolean isSMASUNGScannerStarted = false;
    private CheckBox aadharConsetCB;
    private TextView kycName, kycDob, kycGender, kycEmail, kycPhone, kycCareOf, kycAddr, kycTs, kycTxn, kycRespTs, kycErrorTextView;
    private LinearLayout kycDetailLayout, kycErrorLayout;
    private Button updateKycButton, cancelButton;
    /*********SAMUSUNG********/
    private Handler mHandler = new Handler();
    private int mRotation;
    private byte[] mBytesData;
    private SurfaceHolder holder;
    private Canvas canvas;
    private String mEyeDist;
    private String mEyeOpen;
    static final int SESSION_TIMEOUT = 1400;
    int goodCount, tooFarCount, openWidelyCount, tooCloseCount, eyesNotDetected;
    private static int openingCount;
    private X509Certificate uidaiCert;
    private boolean singleChecked;
    private SensorManager mSensorManager;
    private SensorEventListener proximitySensorEventListener;
  /*  private SecIrisManager secIrisManager;*/
    public boolean isCapturinginProgress = false;
    public static SurfaceView preview;
    private ImageView aadharImageView;
    private LinearLayout aadharImageLinearLayout;
    private SeccMemberItem seccItem;
    private SelectedMemberItem selectedMemItem;
    private VerifierLoginResponse loginResponse;
    private AlertDialog internetDiaolg;
    private KycResponse kycModel;
    private String consent = "Y";
    private Button cancelEkyc;
    private CardView card_view1;
    private Boolean isKycEnabled = AppConstant.isKyCEnabled;
    private TextView nameAsAadhar, dobAsAadhar, genderAsAadhar, aadharNum;

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


    public AadharAuthIrisKycFragment() {
    }

    // TODO: Rename and change types and number of parameters
    public static AadharAuthIrisKycFragment newInstance(String param1, String param2) {
        AadharAuthIrisKycFragment fragment = new AadharAuthIrisKycFragment();
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
        view = inflater.inflate(R.layout.fragment_iris_kyc, container, false);
        context = getActivity();
        backLayout = (RelativeLayout) view.findViewById(R.id.backLayout);
        back = (ImageView) view.findViewById(R.id.back);
        edtxt_Aadhaar = (EditText) view.findViewById(R.id.auth_demo_aadhaar);
        edtxt_Aadhaar.addTextChangedListener(inputTextWatcher);
       // checkConnection = new CheckConnection(context);
       /* back.setOnClickListener(new View.OnClickListener() {
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
        });
        init(view);
        attachedDevices(view);*/
        return view;
    }
/*

    private void init(View view) {

        card_view1 = (CardView) view.findViewById(R.id.card_view1);
        aadharNum = (TextView) view.findViewById(R.id.aadharNum);
        nameAsAadhar = (TextView) view.findViewById(R.id.nameAsAadhar);
        dobAsAadhar = (TextView) view.findViewById(R.id.dobAsAadhar);
        genderAsAadhar = (TextView) view.findViewById(R.id.genderAsAadhar);
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
        aadharNumResetEditText = (TextView) view.findViewById(R.id.aadharNumResetEditText);
        aadharConsetCB = (CheckBox) view.findViewById(R.id.aadharConsetCB);
        cancelEkyc = (Button) view.findViewById(R.id.cancelEkyc);
        cancelEkyc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
        log = new Logs(context);
        statusLog = new StatusLogs(activity);

        dialogWaitForAuth = new ShowDialogWaitForAuth(activity);
        dialogWaitForAuth.setCancelable(false);

        textviewTimer = (TextView) view.findViewById(R.id.scanTimer);
        checkNetwork = checkConnection.isConnectingToInternet();

        */
/*edtxt_Aadhaar = (EditText) view.findViewById(R.id.auth_demo_aadhaar);
        *//*
*/
/*if (!Global.AUTH_AADHAAR.equalsIgnoreCase("")) {
            edtxt_Aadhaar.setText(Global.AUTH_AADHAAR);
            edtxt_Aadhaar.setTextColor(Color.parseColor("#0B610B"));
            validAadhaar = true;
        }*//*
*/
/*
        edtxt_Aadhaar.addTextChangedListener(inputTextWatcher);*//*

        btn_submit = (Button) view.findViewById(R.id.auth_demo_go);
        btn_submit.setOnClickListener(this);

        btn_auth = (Button) view.findViewById(R.id.auth_demo_simple);
        btn_auth.setOnClickListener(this);


        btn_home = (Button) view.findViewById(R.id.home);
       */
/* btn_home.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(getBaseContext(), MainScreen.class);
                startActivity(i);
                finish();
            }
        });*//*


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
        aadharNumResetEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtxt_Aadhaar.setText("");
            }
        });

        selectedMemItem = SelectedMemberItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.SELECTED_ITEM_FOR_VERIFICATION, context));
        if (selectedMemItem.getSeccMemberItem() != null) {
            seccItem = selectedMemItem.getSeccMemberItem();
        }

        if (seccItem != null) {
            if (seccItem.getAadhaarCapturingMode() != null) {
                if (seccItem.getAadhaarCapturingMode().equalsIgnoreCase(AppConstant.IRIS_MODE) || seccItem.getAadhaarCapturingMode().equalsIgnoreCase("")) {
                    if (seccItem.getAadhaarAuth() != null && !seccItem.getAadhaarAuth().equalsIgnoreCase("")) {
                        card_view1.setVisibility(View.VISIBLE);


                        if (seccItem.getAadhaarNo() != null) {
                            aadharNum.setText(seccItem.getAadhaarNo());
                            edtxt_Aadhaar.setText(seccItem.getAadhaarNo());
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
        loginResponse = (VerifierLoginResponse.create(ProjectPrefrence.
                getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.VERIFIER_CONTENT, context)));
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
            //     imageViewConnectionStatus.setImageResource(R.drawable.cred);
            ShowPromptNetwork("Network Issue", "Please Enable the network");
        }

    }


    public void attachedDevices(View view) {


        //checkConnection = new CheckConnection(context);

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

        Global.USER_NAME = AppConstant.USER_NAME;
        Global.USER_PASSWORD = AppConstant.USER_PASSWORD;
        countDown = new CountDown(Global.TIME_FOR_SCANNING, Global.INTERVAL);
        countDownSamsung = new CountDownSamsung(Global.TIME_FOR_SCANNING_SAMSUNG, Global.INTERVAL);
        Global.imei = CommonMethods.GetIMEI(context);
        try {
            SecIrisManager secIris = SecIrisManager.getInstance();
            Log.d("product", "Is a samsung device");
            Global.samsungIris = true;
            Global.scannerAttached = true;

            //Log.i("", "Model Name : " + android.os.Build.MODEL);
            Global.deviceMake = android.os.Build.MANUFACTURER;
            Global.deviceModel = android.os.Build.MODEL;
            Global.deviceVendor = android.os.Build.MANUFACTURER;
            String serial = "";
            try {
                Class<?> c = Class.forName("android.os.SystemProperties");
                Method get = c.getMethod("get", String.class);
                serial = (String) get.invoke(c, "ril.serialnumber");
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Global.serialNumber = serial;
            init(view);
            imageViewDeviceStatus.setImageResource(R.drawable.dgreen);
            Global.connectedDevice = Global.deviceMake.trim().toString() + Global.deviceModel.trim().toString() + Global.deviceVendor.trim().toString() + Global.serialNumber.trim().toString();
            Log.e("", "Samsung values : " + Global.connectedDevice);
        } catch (RuntimeException re) {
            Log.e("1", "-->" + Utility.getDeviceId(context));
            String device = Utility.getDeviceNameID(context);
            Log.e("2", "-->" + Utility.getDeviceNameID(context));
            if (device.startsWith("InFocus")) {
                Global.INFOCUS_Iris = true;

                Global.scannerAttached = true;
                init(view);
                hideScanControl();
                imageViewDeviceStatus.setImageResource(R.drawable.dgreen);
                Global.connectedDevice = Global.deviceMake.trim().toString() + Global.deviceModel.trim().toString() + Global.deviceVendor.trim().toString() + Global.serialNumber.trim().toString();

                activity.registerReceiver(broadcastReceiver, new IntentFilter("broadCastName"));
            } else {
            */
/*    mManager = ((UsbManager) context.getSystemService(Context.USB_SERVICE));
                IntentFilter filter = new IntentFilter();
                filter.addAction(ACTION_USB_PERMISSION);
                filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
                filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
                registerReceiver(mUsbReceiver, filter, "permission.ALLOW_BROADCAST", handler1);//you are just registering a receiver to listen to events.
                MultiDex.install(context);
                init();

                //		registerReceiver(mUsbReceiver, filter);//you are just registering a receiver to listen to events.
                FindVendorDevice();// This function is called here to check if any supported device is attachd during the application startup.
                AsynchronousPermissionCheck();*//*

            }

            // Initializing all fields
        }
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            IrisCaptureBean bean = intent.getExtras().getParcelable("CAPTUREBEAN");
            System.out.println(bean.getIrisBioData());
            Log.e("eye captured", "-->>>>>>>" + bean.getIrisBioData());
            String fpString = bean.getIrisBioData();
            String aadhaar_no = Global.AUTH_AADHAAR;

            Global.KYC_API_VERSION = "1.0";
            String KYCAuthXml = "";
            String txtMessage = "";
            Global.AUTH_USING = "Iris";
        */
/*    if (btnype.equalsIgnoreCase("bio_auth")) {

                txtMessage = " Auth BIO";
                KYCAuthXml = getKYCAuthXmlForINFOCUS("I", aadhaar_no, fpString, btnype);
                appendLogInvalidXmlAWithoutRad("infocus Ayth xml",KYCAuthXml);
                //			KYCAuthXml = getKYCAuthXml("I",aadhaar_no , fpImgString, "bio_auth" );
                Global.Tempxmldel = KYCAuthXml;

            } else *//*

            if (btnype.equalsIgnoreCase("bio_ekyc")) {
                txtMessage = "  KYC BIO";
                ////////////////////////////////
                if (isKycEnabled) {
                    KYCAuthXml = getKYCAuthXmlForINFOCUS("I", aadhaar_no, fpString, " ");
                    Global.KYCIRISXML = KYCAuthXml;
                } else {

                }
                appendLogInvalidXmlAWithoutRad("infocus Ayth xml", KYCAuthXml);
                Log.e("bio kyc xml", "=" + KYCAuthXml);
                String encodedKYCXml = Base64.encodeToString(KYCAuthXml.getBytes(), Base64.NO_WRAP);
                String KYCXml = "";
                KYCXml = getKYCXml("I", aadhaar_no, encodedKYCXml, fpString, "bio_ekyc");
                Global.Tempxmldel = KYCXml;
                hitToServerforFINALRequest();
            }
            //  ShowDialogForSavingData(txtMessage);
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

    @Override
    public void onCaptureSuccess() {

        // TODO Auto-generated method stub
        captureStatus = true;
        mHandler.post(new Runnable() {
            public void run() {
                mSensorManager.unregisterListener(proximitySensorEventListener);
                stopCapture();
                authenticate();
            }
        });
    }

    @Override
    public void onCaptureFailed(int arg0) {

        mHandler.post(new Runnable() {
            public void run() {
                mSensorManager.unregisterListener(proximitySensorEventListener);
                stopCapture();
            }
        });
    }

    @Override
    public void onEyeInfoUiHints(String EyeDist, String EyeOpen) {

        mEyeOpen = EyeOpen;
        mEyeDist = EyeDist;
        mHandler.post(new Runnable() {
            public void run() {
                updateEyeInfo(mEyeDist, mEyeOpen);
            }
        });
    }

    @Override
    public void onTimeOut() {

        // TODO Auto-generated method stub
        mHandler.post(new Runnable() {
            public void run() {

                mSensorManager.unregisterListener(proximitySensorEventListener);
                stopCapture();
            }
        });
    }

    @Override
    public void showPreviewFrame(byte[] bytesPreview, int width, int height, int rotation) {

        mBytesData = bytesPreview;
        mRotation = rotation;
        final int w = width, h = height;
        mHandler.post(new Runnable() {
            @SuppressWarnings("deprecation")
            public void run() {
                preview = (SurfaceView) view.findViewById(R.id.preview);
                preview.setVisibility(View.VISIBLE);
                Paint circlePaint = null, reflectPaint = null, bitmapPaint = null;
                int[] pixelsPreview = null;
                if (pixelsPreview == null) {
                    pixelsPreview = new int[w * h];
                }
                for (int i = 0; i < pixelsPreview.length; i++) {
                    final int index = mRotation > 90 ? pixelsPreview.length - i - 1 : i;
                    int p = (mBytesData[index] & 0xff);
                    pixelsPreview[i] = p | (p << 8) | (p << 16) | 0xff000000;
                }
                holder = preview.getHolder();
                canvas = null;
                try {
                    canvas = holder.lockCanvas();
                    if (canvas != null) {
                        if (mRotation == 90) {
                            canvas.scale(canvas.getWidth() * 1.0f / h, -canvas.getHeight() * 1.0f / w);
                            canvas.rotate(90);
                            canvas.translate(-w, -h);
                        } else {
                            canvas.scale(3, 3);
                        }
                        if (bitmapPaint == null) {
                            bitmapPaint = new Paint(Paint.DITHER_FLAG | Paint.FILTER_BITMAP_FLAG);
                            bitmapPaint.setColor(0xff40d0ff);
                            bitmapPaint.setStyle(Paint.Style.STROKE);
                            bitmapPaint.setStrokeWidth(1f);
                            circlePaint = new Paint(bitmapPaint);
                            circlePaint.setColor(0x8040d0ff);
                            reflectPaint = new Paint(bitmapPaint);
                            reflectPaint.setColor(0xffff0000);
                        }
                        System.gc();
                        canvas.drawColor(Color.TRANSPARENT, android.graphics.PorterDuff.Mode.CLEAR);
                        canvas.drawBitmap(pixelsPreview, 0, w, 0, 0, w, h, true, bitmapPaint);
                    }
                } finally {
                    if (canvas != null) {
                        holder.unlockCanvasAndPost(canvas);
                    }
                }
            }
        });
    }


    private void updateEyeInfo(String eyeDist, String eyeOpen) {
        int eye_distance = Integer.valueOf(eyeDist);
        String eye_open = eyeOpen;
        {
            if (eye_distance < 16 && eye_distance > 0) {
                tooCloseCount++;
                if (tooCloseCount >= 3) {
                    if (singleChecked)
                        preview.setBackgroundResource(R.drawable.red_single);
                    else
                        preview.setBackgroundResource(R.drawable.red);
                    return;
                }
                if (eye_open == "Open Eyes") {
                    if (openingCount >= 0) {
                        openWidelyCount++;
                        if (openWidelyCount >= 0) {
                            if (singleChecked)
                                preview.setBackgroundResource(R.drawable.red_single);
                            else
                                preview.setBackgroundResource(R.drawable.red);
                            return;
                        }
                    }
                } else {
                    openingCount = 0;
                }
            } else if (eye_distance >= 25) {
                tooFarCount++;
                if (tooFarCount >= 3) {
                    if (singleChecked)
                        preview.setBackgroundResource(R.drawable.red_single);
                    else
                        preview.setBackgroundResource(R.drawable.red);
                    // t.run();
                    return;
                }
                if (eye_open == "Open Eyes") {
                    if (openingCount >= 0) {
                        openWidelyCount++;
                        if (openWidelyCount >= 0) {
                            if (singleChecked)
                                preview.setBackgroundResource(R.drawable.red_single);
                            else
                                preview.setBackgroundResource(R.drawable.red);
                            return;
                        }
                    }
                } else {
                    openingCount = 0;
                }
            } else if (eye_distance <= 0) {
                eyesNotDetected++;
                if (eyesNotDetected >= 10) {
                    if (singleChecked)
                        preview.setBackgroundResource(R.drawable.red_single);
                    else
                        preview.setBackgroundResource(R.drawable.red);
                }
            } else if (eye_distance >= 16 && eye_distance < 25) {
                if (eye_open == "Open Eyes Wider") {
                    openWidelyCount++;
                    if (openWidelyCount >= 0) {
                        if (singleChecked)
                            preview.setBackgroundResource(R.drawable.green_single);
                        else
                            preview.setBackgroundResource(R.drawable.green);
                        return;
                    }
                } else {
                    goodCount++;
                    if (goodCount >= 0) {
                        if (singleChecked)
                            preview.setBackgroundResource(R.drawable.green_single);
                        else
                            preview.setBackgroundResource(R.drawable.green);
                        goodCount = 0;
                        tooFarCount = 0;
                        openWidelyCount = 0;
                        tooCloseCount = 0;
                        eyesNotDetected = 0;
                    }
                    return;
                }
            } else {
                if (singleChecked)
                    preview.setBackgroundResource(R.drawable.grey_single);
                else
                    preview.setBackgroundResource(R.drawable.grey);
            }
        }
        return;
    }

    private void stopCapture() {
        isSMASUNGScannerStarted = false;
        Log.e("stopCapture", "call");
        if (preview != null)
            preview.setVisibility(View.GONE);
        secIrisManager.stopCapture();
        isCapturinginProgress = false;
    }

    private void closeActivity() {
        try {

            handler1.removeCallbacksAndMessages(null);
            //activity.unregisterReceiver();
        } catch (Exception ex) {

        }
        try {

            countDownSamsung.cancel();
            countDown.cancel();
        } catch (Exception exx) {

        }
        try {
            handler.UnInitDevice();
        } catch (Exception wdef) {

        }
        activity.finish();
    }

    protected Boolean activateIrisLicense() {
        try {
            //String key = "3DDB5D3BB977DE500E0B8D1164CE9433E923CC2614011F52AB506208E5D2A35B9B9B149C7C42936A8E688FEC6D7B94D7A84AFC2BD13EB56E0A676133429E7C43";
            String key = "7FD14956718AECD5049ABCFB54D8B72E07E05D3297F0295D6699413F2D0D0D09F3BF7CF097683529659DADC28DDCACC9BF9BA0896F4ABE91D653B55721EE1022";

            String packageName = context.getApplicationContext().getPackageName();

            SecBiometricLicenseManager mLicenseMgr = SecBiometricLicenseManager.getInstance(context);
            mLicenseMgr.activateLicense(key, packageName);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void startCapture() {

        isSMASUNGScannerStarted = true;
        Log.e("startCapture", "call");
        int numeye = 1;
        singleChecked = true;

		*/
/*//*
/		for double eye
        int numeye=2;
		singleChecked = false;*//*

        try {
            secIrisManager = SecIrisManager.getInstance();
            secIrisManager.registerCallback(this);
        } catch (RuntimeException e) {
            e.printStackTrace();
            activity.finish();
            return;
        }
        try {
            mSensorManager.registerListener(proximitySensorEventListener,
                    mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY),
                    SensorManager.SENSOR_DELAY_NORMAL);

            secIrisManager.startCapture(numeye);
            secIrisManager.startCapture(numeye);
            isCapturinginProgress = true;
            preview = (SurfaceView) view.findViewById(R.id.preview);
            Log.e("preview", "=======>>>>>> " + preview);
            preview.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    protected void authenticate() {

        String aadhaar_no = Global.AUTH_AADHAAR;

        Global.KYC_API_VERSION = "1.0";
        String KYCAuthXml = "";
        String txtMessage = "";
        Global.AUTH_USING = "Iris";
        if (btnype.equalsIgnoreCase("bio_auth")) {

            txtMessage = " Auth BIO";
            KYCAuthXml = getKycAuthXMLForSamsung("I", aadhaar_no, btnype);
            //			KYCAuthXml = getKYCAuthXml("I",aadhaar_no , fpImgString, "bio_auth" );
            Global.Tempxmldel = KYCAuthXml;
        } else if (btnype.equalsIgnoreCase("bio_ekyc")) {
            txtMessage = "  KYC BIO";
            KYCAuthXml = getKycAuthXMLForSamsung("I", aadhaar_no, " ");
            Log.e("bio kyc xml", "=" + KYCAuthXml);
            appendLogInvalidXmlAWithoutRad(KYCAuthXml, "withoutRadAuthenticate()");
            String encodedKYCXml = Base64.encodeToString(KYCAuthXml.getBytes(), Base64.NO_WRAP);
            String KYCXml = "";
            KYCXml = getKYCXmlSamsung("I", aadhaar_no, encodedKYCXml, "bio_ekyc");
            appendLogInvalidXmlAWithoutRad(KYCXml, "withRadAuthenticate()");
            ////////////////////////////////////
            Global.KYCIRISXML = KYCAuthXml;
        }
        hitToServerforFINALRequest();
        // ShowDialogForSavingData(txtMessage);
        */
/*checkNetwork = checkConnection.isConnectingToInternet();
        if (checkNetwork) {
			//							dialogWaitForAuth.dismiss();
			hitToServerforFINALRequest();
		}else {
			log.myMessage(getResources().getString(R.string.network_not_available)+txtMessage + "after scanning", Global.AUTH_AADHAAR);
			ShowPromptNetworkforFinalHit("Network Issue", "Please Enable the network");
		}*//*



    }

    */
/*************SAMSUNG*****************//*



    private void startCapturingKYC() {
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
                    Toast.makeText(context, "Scanning Start", Toast.LENGTH_SHORT).show();
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
                        imageViewFingerPrint.invalidate();
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
            ShowPromptNetworkforKYC("Network Issue", "Please Enable the network");
        }
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

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

                closeActivity();
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

    public void repeatScanning() {
        if (!Global.samsungIris) {
            deviceInit = false;
            if (captureStatus) {
                captureStatus = false;
            }
            if ((!deviceModel.equalsIgnoreCase("mfs100")))
                handler.UnInitDevice();
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

                            Boolean activate = activateIrisLicense();
                            mSensorManager = (SensorManager) context.getSystemService(SENSOR_SERVICE);
                            mSensorManager.registerListener(proximitySensorEventListener,
                                    mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY),
                                    SensorManager.SENSOR_DELAY_NORMAL);
                            if (activate) {
                                countDownSamsung.start();
                                Toast.makeText(context, "Scanning Start", Toast.LENGTH_SHORT).show();
                                imageViewFingerPrint.setVisibility(View.GONE);
                                startCapture();
                            } else {
                                countDown.start();
                                imageViewFingerPrint.invalidate();
                                imageViewFingerPrint.setImageBitmap(null);
                                imageViewFingerPrint.invalidate();
                                Toast.makeText(context, "Scanning Start", Toast.LENGTH_SHORT).show();
                                handler.BeginCapture();
                            }


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
        dlgAlert.create().show();

    }

    @Override
    public void handlerFunction(byte[] rawImage, int imageHeight, int imageWidth, int status, String errorMessage,
                                boolean complete, byte[] isoData, int quality, int finalNFIQ) {

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

    //	to show message
    private void ShowPrompt(String title, String message) {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(
                context);
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
        dlgAlert.create().show();
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

    //	to show network message
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

    private void ShowPromptNetworkforAUTH(String title, String message) {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(
                context);
        dlgAlert.setMessage(message);
        dlgAlert.setTitle(title);
        dlgAlert.setIcon(R.drawable.erroricon);
        dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //startCapturingAUTH();
            }
        });
        dlgAlert.setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        btn_auth.setEnabled(false);
                        activity.finish();
                    }
                });
        dlgAlert.setCancelable(false);
        dlgAlert.create().show();
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
                        dialogWaitForAuth.dismiss();
                        dialogWaitForAuth.cancel();
                        closeActivity();
                    }
                });
        dlgAlert.setCancelable(false);
        dlgAlert.create().show();
    }

    private void hitToServerforFINALRequest() {
        checkNetwork = checkConnection.isConnectingToInternet();
        countDown.cancel();
        if (checkNetwork) {
            startTime = System.currentTimeMillis();
            HitToServer task = new HitToServer();
            task.execute();
        } else {
            ShowPromptNetworkforFinalHit("Network Issue", "Please Enable the network");
        }
    }


    //	To hit server
    private class HitToServer extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            captureStatus = true;
            String res = "";
            Log.e("request xml", "=====" + Global.KYCIRISXML);
            //   String
            //   res = CommonMethods.HttpPostLifeCerticiate(Global.KYC_URL, Global.Tempxmldel);
            if (isKycEnabled) {

                res = CommonMethods.HttpPostLifeCerticiate(Global.KYC_URL, Global.KYCIRISXML, AppConstant.AUTHORIZATION, AppConstant.AUTHORIZATIONVALUE);
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
            try {
                dialogWaitForAuth.dismiss();
            } catch (Exception ex) {

            }
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
                try {
                    dialogWaitForAuth.show();
                } catch (Exception ex) {

                }
            }
        }
    }

    private void ShowNoResponse(String error) {
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
    public void onAttach(Activity context) {
        super.onAttach(context);
        activity = (CaptureAadharDetailActivity) context;
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
                repeatScanning();
            }

        }

        @Override
        public void onTick(long millisUntilFinished) {
            Log.e("cowntown", "== " + millisUntilFinished / 1000);
            textviewTimer.setTextColor(Color.parseColor("#000000"));
            textviewTimer.setText(getResources().getString(R.string.scan_your_fingure) + millisUntilFinished / 1000);

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
                repeatScanning();
            }

        }

        @Override
        public void onTick(long millisUntilFinished) {
            Log.e("cowntown", "== " + millisUntilFinished / 1000);
            textviewTimer.setTextColor(Color.parseColor("#000000"));
            textviewTimer.setText(getResources().getString(R.string.scan_your_eye) + millisUntilFinished / 1000);

        }
    }

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
            kycXml = helper.createXmlForKYC(aadhaar_no, deviceType, encodedXml, " ", IMEINumber, atype);
            Global.KYCIRISXML = kycXml;
            // hitToServerforFINALRequest();
            appendLogInvalidXmlAWithoutRad(kycXml, "getKYCXmlSamsung");
        }
        return kycXml;

    }

    //	to create KYC auth/encoded Rad xml
    public String getKYCAuthXml(String deviceType, String aadhaar_no, String fpImgString, String atype) {
        String kycXml = "";
        byte[] publicKey = null;
        //		GetPubKeycertificateData();
        publicKey = Base64.decode(Global.productionPublicKey, Base64.DEFAULT);
        if (deviceType.equalsIgnoreCase("F") || deviceType.equalsIgnoreCase("I")) {
            UidaiAuthHelper helper = new UidaiAuthHelper(publicKey);
            kycXml = helper.createXmlForKycAuth(deviceType, aadhaar_no, fpImgString, false, "");
            if (atype.equalsIgnoreCase("bio_auth")) {
                kycXml = helper.createCustomXmlForAuth(kycXml, atype);
            }
        }

        return kycXml;

    }

    public String getKycAuthXMLForSamsung(String deviceType, String aadhaar, String atype) {
        String samsungAuthXml = "";
        byte[] publicKey = null;
        //		GetPubKeycertificateData();
        publicKey = Base64.decode(Global.productionPublicKey, Base64.DEFAULT);
        UidaiAuthHelper helper = new UidaiAuthHelper(publicKey);
        ////////////////////////////
        samsungAuthXml = helper.createXmlForSamsungKycAuth(deviceType, false, aadhaar);
        if (atype.equalsIgnoreCase("bio_auth")) {
            samsungAuthXml = helper.createCustomXmlForAuth(samsungAuthXml, atype);
        }
        appendLogInvalidXmlAWithoutRad(samsungAuthXml, "getKycAuthXMLSamsung");
        return samsungAuthXml;

    }

    public String getKYCAuthXmlForINFOCUS(String deviceType, String aadhaar_no, String fpImgString, String atype) {
        String kycXml = "";
        byte[] publicKey = null;
        //		GetPubKeycertificateData();
        publicKey = Base64.decode(Global.productionPublicKey, Base64.DEFAULT);
        if (deviceType.equalsIgnoreCase("F") || deviceType.equalsIgnoreCase("I")) {
            UidaiAuthHelper helper = new UidaiAuthHelper(publicKey);
            //////////////////////////////////////
            kycXml = helper.createXmlForKycAuthForINFOCUS(deviceType, aadhaar_no, fpImgString, false, "");
            if (atype.equalsIgnoreCase("bio_auth")) {
                kycXml = helper.createCustomXmlForAuth(kycXml, atype);
            }
        }

        return kycXml;

    }


    public void appendLogInvalidXmlAWithoutRad(String text, String loc) {
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
    }

    public void readXml(String resp) {
        String JSON = null;

        String nameAsInAadhar = "";
        if (resp != null) {
            if (isKycEnabled) {
               // ShowKycData(resp);
                ShowKycDataNew(resp);

            } else {
                try {
                    XmlToJson xmlToJson = new XmlToJson.Builder(resp).build();
                    JSON = xmlToJson.toString();
                } catch (Exception ex) {
                    // ShowErrorMessage("Invalid Response");
                    CustomAlert.alertWithOk(context, resp);
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


        } else {
            CustomAlert.alertWithOk(context, resp);
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
        String TAG_PC = "pc";
        String TAG_VTC = "vtc";
        String TAG_STREET = "street";
        String TAG_DIST = "dist";
        String TAG_STATE = "state";
        String TAG_CO = "co";
        String TAG_HOUSE = "house";
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
                        String str_ho = Poa_obj.getString(TAG_HOUSE);
                        poaModel.setHouse(str_ho);

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try {

            handler1.removeCallbacksAndMessages(null);
            //activity.unregisterReceiver();
        } catch (Exception ex) {

        }
        try {

            countDownSamsung.cancel();
            countDown.cancel();
        } catch (Exception exx) {

        }
        try {
            handler.UnInitDevice();
        } catch (Exception wdef) {

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {

            handler1.removeCallbacksAndMessages(null);
            //activity.unregisterReceiver();
        } catch (Exception ex) {

        }
        try {

            countDownSamsung.cancel();
            countDown.cancel();
        } catch (Exception exx) {

        }
        try {
            handler.UnInitDevice();
        } catch (Exception wdef) {

        }
    }

    @Override
    public void onStop() {
        super.onStop();
        try {

            handler1.removeCallbacksAndMessages(null);
            //activity.unregisterReceiver();
        } catch (Exception ex) {

        }
        try {

            countDownSamsung.cancel();
            countDown.cancel();
        } catch (Exception exx) {

        }
        try {
            handler.UnInitDevice();
        } catch (Exception wdef) {

        }
    }

    private void alertForValidateLater(String aadharName, SeccMemberItem item) {
        internetDiaolg = new AlertDialog.Builder(context).create();
        LayoutInflater factory = LayoutInflater.from(context);
        View alertView = factory.inflate(R.layout.aadhar_validation_msg_popup, null);
        internetDiaolg.setView(alertView);
        internetDiaolg.show();

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

  */
/*  private void qrCodeSubmitAaadhaarDetail() {

        seccItem.setAadhaarVerifiedBy(loginResponse.getAadhaarNumber());
        if (kycModel != null) {
            seccItem.setAadhaarAuth(AppConstant.VALID_STATUS);
            seccItem.setAadhaarCapturingMode(AppConstant.IRIS_MODE);
            seccItem.setAadhaarNo(kycModel.getKycRes().getUidData().getUid());
            seccItem.setNameAadhaar(kycModel.getKycRes().getUidData().getPoi().getName());
            seccItem.setAadhaarGender(kycModel.getKycRes().getUidData().getPoi().getGender());
            seccItem.setAadhaarDob(kycModel.getKycRes().getUidData().getPoi().getDob());
            seccItem.setConsent(consent);
            seccItem.setAadhaarSurveyedStat(AppConstant.SURVEYED + "");
            seccItem.setAadhaarAuthDt(String.valueOf(DateTimeUtil.currentTimeMillis()));
            if (seccItem.getAadhaarStatus().equalsIgnoreCase(AppConstant.AADHAAR_STAT)) {

                updateAadhaarDetail();
            }
        }
    }*//*


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
                seccItem.setAadhaarCapturingMode(AppConstant.IRIS_MODE);
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

           */
/* if (kycModel != null) {
                seccItem.setAadhaarCapturingMode(AppConstant.IRIS_MODE);
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

            seccItem.setAadhaarCapturingMode(AppConstant.IRIS_MODE);
        */
/*    seccItem.setAadhaarNo(kycModel.getKycRes().getUidData().getUid());
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

    private void ShowKycData(String JSON){
        String nameAsInAadhar = "";
        kycModel = setKycData(JSON);
        if (kycModel != null) {

            if (kycModel.getKycRes() != null && kycModel.getKycRes().getRet() != null &&
                    kycModel.getKycRes().getRet().equalsIgnoreCase("Y")) {
                updateKycButton.setText("Save");
                cancelButton.setVisibility(View.VISIBLE);
                updateKycButton.setVisibility(View.VISIBLE);
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
                nameAsInAadhar = kycModel.getKycRes().getUidData().getPoi().getName();
                if (kycModel.getKycRes().getUidData().getPoi().getName() != null)
                    kycName.setText(kycModel.getKycRes().getUidData().getPoi().getName());
                if (kycModel.getKycRes().getUidData().getPoi().getDob() != null)
                    kycDob.setText(kycModel.getKycRes().getUidData().getPoi().getDob());
                 */
/*   if (kycModel.getKycRes().getUidData().getPoa().getCo() != null)
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
                    addr.append("," + kycModel.getKycRes().getUidData().getPoa().getHouse());
                if (kycModel.getKycRes().getUidData().getPoa().getStreet() != null)
                    addr.append(kycModel.getKycRes().getUidData().getPoa().getStreet());
                if (kycModel.getKycRes().getUidData().getPoa().getLm() != null)
                    addr.append("," + kycModel.getKycRes().getUidData().getPoa().getLm());
                if (kycModel.getKycRes().getUidData().getPoa().getVtc() != null)
                    addr.append("," + kycModel.getKycRes().getUidData().getPoa().getVtc());
                   */
/* if (kycModel.getKycRes().getUidData().getPoa().getSubdist() != null)
                        addr.append("," + kycModel.getKycRes().getUidData().getPoa().getSubdist());*//*

                if (kycModel.getKycRes().getUidData().getPoa().getDist() != null)
                    addr.append("," + kycModel.getKycRes().getUidData().getPoa().getDist());
                if (kycModel.getKycRes().getUidData().getPoa().getState() != null)
                    addr.append("," + kycModel.getKycRes().getUidData().getPoa().getState());
                if (kycModel.getKycRes().getUidData().getPoa().getCountry() != null)
                    addr.append("," + kycModel.getKycRes().getUidData().getPoa().getCountry());

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
                final String finalNameAsInAadhar = nameAsInAadhar;
                updateKycButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertForValidateLater(finalNameAsInAadhar, seccItem);
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
                    kycModel.getKycRes().getRet().equalsIgnoreCase("N") && kycModel.getKycRes().getErr() != null
                    && !kycModel.getKycRes().getErr().equalsIgnoreCase("")) {
                cancelButton.setVisibility(View.GONE);
                updateKycButton.setVisibility(View.VISIBLE);
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
                kycErrorTextView.setText(err + "\n\n" + "Response time : " + totalTime + " miliseconds");

                final String finalNameAsInAadhar = nameAsInAadhar;
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
                CustomAlert.alertWithOk(context, JSON);
                updateKycButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        closeActivity();
                    }
                });

            }
        } else {
            CustomAlert.alertWithOk(context, JSON);
        }
    }
    private void ShowKycDataNew(String JSON){

        aadhaarKycResponse = new AadhaarResponseItem().create(JSON);
        AppUtility.writeFileToStorage(JSON, "E-kyc_with_BIO/JSON");
        if (aadhaarKycResponse != null) {


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
                  */
/*  if (aadhaarKycResponse.getUidData().getPoa().getCo() != null)
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
                   */
/* if (aadhaarKycResponse.getUidData().getPoa().getSubdist() != null)
                        addr.append("," + aadhaarKycResponse.getUidData().getPoa().getSubdist());*//*

                if (aadhaarKycResponse.getDist() != null)
                    addr.append(", " + aadhaarKycResponse.getDist());
                if (aadhaarKycResponse.getState() != null)
                    addr.append(", " + aadhaarKycResponse.getState());
           */
/*     if (aadhaarKycResponse.getC != null)
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
    }
*/

}
