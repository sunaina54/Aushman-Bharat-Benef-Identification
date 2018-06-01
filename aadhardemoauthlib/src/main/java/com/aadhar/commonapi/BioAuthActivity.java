package com.aadhar.commonapi;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
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
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.support.multidex.MultiDex;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aadhar.CheckConnection;
import com.aadhar.CommonMethods;
import com.aadhar.Global;
import com.aadhar.Logs;
import com.aadhar.MainActivity;
import com.aadhar.R;
import com.aadhar.ShowDialogWaitForAuth;
import com.aadhar.StatusLogs;
import com.aadhar.UidaiAuthHelper;
import com.aadhar.VerhoeffAadhar;
import com.sec.biometric.iris.SecIrisCallback;
import com.sec.biometric.iris.SecIrisManager;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;




/**
 * Created by user on 17-04-2017.
 */

public class BioAuthActivity extends Activity implements View.OnClickListener, HelperInterface, SecIrisCallback {
    public static final String ACTION_USB_PERMISSION = "com.aadhar.commonapi.USB_PERMISSION";
    public static UsbManager mManager;
    public static PendingIntent mPermissionIntent = null;
    public static String deviceType = "";
    public static String deviceMake = "", deviceModel = "", serialNumber = "", deviceVendor = "";
    public DeviceRecognizer deviceRecognizer = new DeviceRecognizer(BioAuthActivity.this);
    File f = new File(Environment.getExternalStorageDirectory(), "Authentication");
    Button btn_authBio;
    CheckConnection checkConnection;
    Timer timer;
    ShowDialogWaitForAuth dialogWaitForAuth;
    boolean checkNetwork;
    Logs log;
    long startTime, endTime, totalTime;
    boolean deviceInit = false;
    StatusLogs statusLog;
    String fpImgString;
    ProgressDialog barProgressDialog;
    Handler handler1 = new Handler();
    Activity activity;
    TimerTask doAsynchronousPermissionCheck;
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

           /* IrisCaptureBean bean = intent.getExtras().getParcelable("CAPTUREBEAN");
            System.out.println(bean.getIrisBioData());
            Log.e("eye captured","-->>>>>>>"+bean.getIrisBioData());
            String fpString = bean.getIrisBioData();
            String aadhaar_no = Global.AUTH_AADHAAR;

            Global.KYC_API_VERSION = "1.0";
            String KYCAuthXml="";
            String txtMessage = "";
            Global.AUTH_USING="Iris";
            if (btnype.equalsIgnoreCase("bio_auth")) {

                txtMessage = " Auth BIO";
                KYCAuthXml = getKYCAuthXmlForINFOCUS("I", aadhaar_no,fpString, btnype);
                //			KYCAuthXml = getKYCAuthXml("I",aadhaar_no , fpImgString, "bio_auth" );
                Global.Tempxmldel = KYCAuthXml;

            }else if (btnype.equalsIgnoreCase("bio_ekyc")) {
                txtMessage = "  KYC BIO";
                KYCAuthXml = getKYCAuthXmlForINFOCUS("I", aadhaar_no,fpString, " ");
                Log.e("bio kyc xml", "="+KYCAuthXml);
                String encodedKYCXml = Base64.encodeToString(KYCAuthXml.getBytes() , Base64.NO_WRAP);
                String KYCXml = "";
                KYCXml = getKYCXml("I" , aadhaar_no, encodedKYCXml ,fpString, "bio_ekyc");
                Global.Tempxmldel = KYCXml;
            }*/
            // ShowDialogForSavingData(txtMessage);
        }
    };
    private EditText et_aadhaar;
    private TextView textviewTimer, textViewStatus;
    private boolean validAadhaar = false ;
    private LinearLayout aadhaar_layout;
    private LinearLayout ll_Scan_main , ll_scan_bottom;
    private CountDown countDown;
   // private CountDownSamsung countDownSamsung;
    private ImageView imageViewFingerPrint ,imageViewConnectionStatus , imageViewDeviceStatus;
    private ProgressBar progressBarFPQuality;
    private Button buttonEnd,buttonScan;
    private String xml,aadhaar,btnype="";
    private BiometricDeviceHandler handler;
    BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action)) {
                SetTextonuiThread("Biometric Scanner is Ready");
                imageViewDeviceStatus.setImageResource(R.drawable.dgreen);
                FindVendorDevice();
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
                        BioAuthActivity.this);

                wrngAlert.setTitle("Device Can't be detached/changed in between the process");
                wrngAlert.setMessage("Do the process without disturbing the device.");
                wrngAlert.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Intent intent = new Intent(getApplicationContext(),
                                        MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                wrngAlert.setCancelable(false);
                wrngAlert.create().show();
                //Code to stop changing the devices in between<-----
            } else if (ACTION_USB_PERMISSION.equals(action)) {

            }

        }
    };
    private boolean captureStatus;
    private SensorManager mSensorManager;
    private Context context;
    private SensorEventListener proximitySensorEventListener;
    private TextWatcher inputTextWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (et_aadhaar.length() == 12) {
                try {
                    if (VerhoeffAadhar.validateVerhoeff(et_aadhaar.getEditableText()
                            .toString())) {
                        et_aadhaar.setTextColor(Color.parseColor("#0B610B"));
                        validAadhaar = true;

                    } else {
                        et_aadhaar.setTextColor(Color.parseColor("#ff0000"));
                        validAadhaar = false;
                    }

                } catch (Exception e) {
                    System.out.print(e.toString());
                }
            } else {
                et_aadhaar.setTextColor(Color.parseColor("#000000"));
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

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bio);
        context =this;
        activity = this;
        checkConnection = new CheckConnection(BioAuthActivity.this);

        if (!f.exists()) {
            f.mkdirs();
        }

        countDown = new CountDown(Global.TIME_FOR_SCANNING, Global.INTERVAL);
       // countDownSamsung = new CountDownSamsung(Global.TIME_FOR_SCANNING_SAMSUNG, Global.INTERVAL);

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
                Method get = c.getMethod("get",String.class);
                serial = (String) get.invoke(c, "ril.serialnumber");
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

			/*	if (serial == null) {
				Global.serialNumber =  android.os.Build.SERIAL;
			}else{
			 */	Global.serialNumber = serial ;
            //			}
            init();
            imageViewDeviceStatus.setImageResource(R.drawable.dgreen);
            Global.connectedDevice = Global.deviceMake.trim().toString()+ Global.deviceModel.trim().toString()+ Global.deviceVendor.trim().toString()+ Global.serialNumber.trim().toString();
            Log.e("", "Samsung values : " + Global.connectedDevice);

        } catch (RuntimeException re) {

//            Utility.getDeviceId(getApplicationContext());
//            Log.e("1","-->"+ Utility.getDeviceId(getApplicationContext()));
            String device ="";/* Utility.getDeviceNameID(getApplicationContext());
            Log.e("2","-->"+Utility.getDeviceNameID(getApplicationContext()));*/

            if (device.startsWith("InFocus")){
                Global.INFOCUS_Iris = true;

                Global.scannerAttached = true;
                init();
                hideScanControl();
                imageViewDeviceStatus.setImageResource(R.drawable.dgreen);
                Global.connectedDevice = Global.deviceMake.trim().toString()+ Global.deviceModel.trim().toString()+ Global.deviceVendor.trim().toString()+ Global.serialNumber.trim().toString();

                registerReceiver(broadcastReceiver, new IntentFilter("broadCastName"));
            }else{
                mManager = ((UsbManager) getSystemService(Context.USB_SERVICE));
                IntentFilter filter = new IntentFilter();
                filter.addAction(ACTION_USB_PERMISSION);
                filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
                filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
                registerReceiver(mUsbReceiver, filter, "permission.ALLOW_BROADCAST", handler1);//you are just registering a receiver to listen to events.
                MultiDex.install(this);
                init();

                //		registerReceiver(mUsbReceiver, filter);//you are just registering a receiver to listen to events.
                FindVendorDevice();// This function is called here to check if any supported device is attachd during the application startup.
                AsynchronousPermissionCheck();
            }




            // Initializing all fields
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        try{
            unregisterReceiver(broadcastReceiver);

        }catch (Exception ex){

        }
        try {
            unregisterReceiver(mUsbReceiver);
        }catch (Exception e)
        {

        }
    }

    private void init()
    {
        log = new Logs(this);
        statusLog = new StatusLogs(BioAuthActivity.this);

        dialogWaitForAuth = new ShowDialogWaitForAuth(this);
        dialogWaitForAuth.setCancelable(false);

        textviewTimer = (TextView) findViewById(R.id.scanTimer);
        checkNetwork = checkConnection.isConnectingToInternet();

        et_aadhaar = (EditText) findViewById(R.id.et_aadhaar);
        if (!Global.AUTH_AADHAAR.equalsIgnoreCase("")) {
            et_aadhaar.setText(Global.AUTH_AADHAAR);
            et_aadhaar.setTextColor(Color.parseColor("#0B610B"));
            validAadhaar = true;
        }
      et_aadhaar.addTextChangedListener(inputTextWatcher);
        //btn_submit = (Button) findViewById(R.id.auth_demo_go);
        //btn_submit.setOnClickListener(this);

        btn_authBio = (Button) findViewById(R.id.btn_authBio);
        btn_authBio.setOnClickListener(this);


        /*btn_home = (Button) findViewById(R.id.home);
        btn_home.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i=new Intent(getBaseContext(),MainScreen.class);
                startActivity(i);
                finish();
            }
        });*/

        aadhaar_layout = (LinearLayout) findViewById(R.id.aadhaar_layout);
        //		RadioButton rBtn_bio = (RadioButton) findViewById(R.id.bio);
        //		RadioButton rBtn_Iris = (RadioButton) findViewById(R.id.iris);
        // rgDevice = (RadioGroup) findViewById(R.id.rg_device);

        ll_scan_bottom = (LinearLayout)findViewById(R.id.scan_bottom_layout);
        ll_Scan_main = (LinearLayout) findViewById(R.id.scan_layout);

        imageViewFingerPrint = (ImageView) findViewById(R.id.imageViewFingerPrint);
        imageViewConnectionStatus = (ImageView) findViewById(R.id.imageViewConnectionStatus);
        imageViewDeviceStatus = (ImageView) findViewById(R.id.imageViewDeviceStatus);
        progressBarFPQuality = (ProgressBar) findViewById(R.id.progressBarFPQuality);

        buttonEnd = (Button) findViewById(R.id.buttonEnd);
        buttonScan = (Button) findViewById(R.id.buttonScan);

        buttonEnd.setOnClickListener(this);
        buttonScan.setOnClickListener(this);

        textViewStatus = (TextView) findViewById(R.id.textViewStatus);


        hideScanControl();
        showAadhaarControl();
        ll_scan_bottom.setVisibility(View.VISIBLE);

        if (checkNetwork) {				// checking either network is connected or not.
            barProgressDialog = new ProgressDialog(BioAuthActivity.this);

            barProgressDialog.setTitle("");
            barProgressDialog.setMessage("Please wait ...");
            barProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            barProgressDialog.setProgress(0);
            barProgressDialog.setCancelable(false);
            //			barProgressDialog.show();

            imageViewConnectionStatus.setImageResource(R.drawable.cgreen);

            CommonMethods.SetApplicationContext(BioAuthActivity.this);

            //			fetching public key
            GetPubKeycertificateData publicKey1 = new GetPubKeycertificateData();
            publicKey1.execute();
        }else {
            imageViewConnectionStatus.setImageResource(R.drawable.cred);
            ShowPromptNetwork("Network Issue", "Please Enable the network");
        }

    }

    //	hiding scan layout only
    public void hideScanControl()
    {
        //		ll_scan_bottom.setVisibility(View.GONE);
        ll_Scan_main.setVisibility(View.GONE);
        textviewTimer.setVisibility(View.GONE);
    }

    //	hiding aadhaar layout only
    public void hideAadhaarControl()
    {
        aadhaar_layout.setVisibility(View.GONE);
    }

    //	visible the scan layout
    public void showScanControl()
    {
        ll_scan_bottom.setVisibility(View.VISIBLE);
        ll_Scan_main.setVisibility(View.VISIBLE);
        textviewTimer.setVisibility(View.VISIBLE);

        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(textviewTimer.getWindowToken(), 0);
    }

    //	visibile the aadhaar layout
    public void showAadhaarControl()
    {
        aadhaar_layout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        Log.e("loc address", "------"+ Global.LOCATION_ADDRESS);
        int i = v.getId();
        if (i == R.id.btn_authBio) {
            btnype = "bio_auth";


            if (validAadhaar) {                            // checking either aadhaar is valid not not
                Global.AUTH_AADHAAR = et_aadhaar.getText().toString();
                aadhaar = Global.AUTH_AADHAAR;
                checkNetwork = checkConnection.isConnectingToInternet();
                if (checkNetwork) {
                    startCapturingAUTH();
                } else {
                    log.myMessage(getResources().getString(R.string.network_not_available) + " AUTH BIO Button click", Global.AUTH_AADHAAR);
                    ShowPromptNetworkforAUTH("Network Issue", "Please Enable the network");
                }
				/*if(Global.scannerAttached) {
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
				}*/
            } else {
                CommonMethods.showErrorDialog("Invalid Value",
                        "Please check Aadhaar Number");
            }


            /*case R.id.auth_demo_go:
                btnype = "bio_ekyc";


                if (validAadhaar) {							// checing either aadhaar is valid not not
                    Global.AUTH_AADHAAR = edtxt_Aadhaar.getText().toString();
                    aadhaar = Global.AUTH_AADHAAR;
                    checkNetwork = checkConnection.isConnectingToInternet();
                    if (checkNetwork) {
                        startCapturingKYC();
                    }else{
                        log.myMessage(getResources().getString(R.string.network_not_available)+" KYC BIO buton click", Global.AUTH_AADHAAR);
                        ShowPromptNetworkforKYC("Network Issue", "Please Enable the network");
                    }
                }else {
                    CommonMethods.showErrorDialog("Invalid Value",
                            "Please check Aadhaar Number");
                }
                break;
*/
        } else if (i == R.id.buttonEnd) {//			Intent intent = new Intent(getApplicationContext(),
            //					MainScreen.class);
            //			intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            //			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            //			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            //			startActivity(intent);
            finish();

        } else if (i == R.id.buttonScan) {
            if (Global.scannerAttached == true) {

                repeatScanning();

            } else
                CommonMethods.showErrorDialog("Something is Wrong",
                        "Biometric Scanner not found.");


        } else {
        }

    }

    private void startCapturingAUTH(){
        checkNetwork = checkConnection.isConnectingToInternet();
        if (checkNetwork) {
            if(Global.scannerAttached) {
                //	isOnline()
                //AsynchronousPermissionCheck();  .....................Bapuji checking  ...not required methods
                hideAadhaarControl();
                showScanControl();


                captureStatus = false;

                // Boolean activate =activateIrisLicense();
                mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
                mSensorManager.registerListener(proximitySensorEventListener,
                        mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY),
                        SensorManager.SENSOR_DELAY_NORMAL);

                /*if (activate) {
                    countDownSamsung.start();
                    Log.e("activate", "------->"+activate);
                    imageViewFingerPrint.setVisibility(View.GONE);
                    startCapture();
                }else{

                    if (Global.INFOCUS_Iris){
                        hideScanControl();
                        Intent intent = null;
                        try {
                            intent = new Intent(this, Class.forName("in.kdms.irislib.IRISCaptureActivity"));
                            startActivity(intent);
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }

                    }else{
                        countDown.start();
                        imageViewFingerPrint.setImageBitmap(null);
                        imageViewFingerPrint.invalidate();
                        Toast.makeText(BioAuthActivity.this, "Scanning Start", Toast.LENGTH_SHORT).show();
                        handler.BeginCapture();
                    }

                }*/

                countDown.start();
                imageViewFingerPrint.setImageBitmap(null);
                imageViewFingerPrint.invalidate();
                Toast.makeText(BioAuthActivity.this, "Scanning Start", Toast.LENGTH_SHORT).show();
                handler.BeginCapture();



            }else{
                CommonMethods.showErrorDialog("Something is Wrong",
                        "Biometric Scanner not found.");
            }
        }else{
            ShowPromptNetworkforAUTH("Network Issue", "Please Enable the network");
        }
    }

    void SetTextonuiThread(final String str) {
        textViewStatus.post(new Runnable() {
            public void run() {
                textViewStatus.setText(str);

            }
        });
    }

    public void AsynchronousPermissionCheck() {
        Log.e("Asynchron", "executing");
        final Handler handlerTask = new Handler();
        this.timer = new Timer();
        this.doAsynchronousPermissionCheck = new TimerTask() {
            public void run() {
                handlerTask.post(new Runnable() {
                    private int bioDeviceType;

                    public void run() {
                        if(Global.scannerAttached && !deviceInit) {
                            mManager = (UsbManager)BioAuthActivity.this.getSystemService(Context.USB_SERVICE);
                            HashMap deviceList = mManager.getDeviceList();
                            Iterator deviceIterator = deviceList.values().iterator();

                            while(true) {
                                while(true) {
                                    boolean startInit;
                                    do {
                                        if(!deviceIterator.hasNext()) {
                                            return;
                                        }

                                        UsbDevice device = (UsbDevice)deviceIterator.next();
                                        long biometricProductID = (long)device.getProductId();
                                        startInit = false;
                                        if(Global.mantraAttached && biometricProductID == 4101L && mManager.hasPermission(device)) {
                                            startInit = true;
                                        } else if(Global.startekAttached && biometricProductID == 33312L && mManager.hasPermission(device)) {
                                            startInit = true;
                                        } else if(Global.iritechAttached && biometricProductID == 61441L && mManager.hasPermission(device)) {
                                            startInit = true;
                                        } else if((Global.morphoMSO1300Attached && biometricProductID == 71L || Global.morphoMSO1350Attached && biometricProductID == 82L || Global.morphoMSO30xAttached && biometricProductID == 36L || Global.morphoMSO35xAttached && biometricProductID == 38L) && mManager.hasPermission(device)) {
                                            startInit = true;
                                        } else if(Global.bioenableAttached && biometricProductID == 1616L && mManager.hasPermission(device)) {
                                            startInit = true;
                                        } else if(Global.secugenAttached && biometricProductID == 8704L && mManager.hasPermission(device)) {
                                            startInit = true;
                                        } else if(Global.biometriquesAttached && biometricProductID == 1003L && mManager.hasPermission(device)) {
                                            startInit = true;
                                        } else if(Global.precisionElkonTouchAttached && biometricProductID == 8214L && mManager.hasPermission(device)) {
                                            startInit = true;
                                        } else if(Global.precisionuru4500Attached && biometricProductID == 11L && mManager.hasPermission(device)) {
                                            startInit = true;
                                        } else {
                                            startInit = false;
                                        }
                                    } while(!startInit);

                                    BioAuthActivity.this.deviceInit = true;
                                    int init = BioAuthActivity.this.handler.InitDevice(Global.connectedDeviceID);
                                    if(init == 0) {

                                        //										btnScan.setVisibility(View.VISIBLE);
                                        //										btnUnInit.setVisibility(View.VISIBLE);
                                        //										btnInit.setVisibility(View.GONE);
                                        //										DialogInitParam initParam = new DialogInitParam(
                                        //												MainActivity.this);

                                        handler.SetThresholdQuality(60);
                                        handler.SetCaptureTimeout(Global.SCAN_TIME);
                                        Global.bioDeviceType = handler
                                                .GetDeviceType();
                                        if (Global.iritechAttached){
                                            Global.bioDeviceType = 1;
                                            handler.SetFingerprintBiometricDataType("IIR");
                                        }else
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
                                }
                            }
                        }
                    }
                });
            }
        };
        this.timer.schedule(this.doAsynchronousPermissionCheck, 0L, 1000L);
    }
    protected boolean FindVendorDevice() {
        Log.e("FindVendorDevice", "executing");
        this.deviceRecognizer = new DeviceRecognizer(this);
        this.deviceRecognizer.FindSupportedDevice();
        UsbDevice device = null;
        if(Global.scannerAttached) {
            if(deviceRecognizer.GetAttachedDeviceCount() == 1) {

                Global.scannerAttached = true;
                Global.connectedDeviceID = deviceRecognizer.GetAttachedDeviceID();
                mManager = (UsbManager)this.getSystemService(Context.USB_SERVICE);
                HashMap deviceList = mManager.getDeviceList();
                Iterator deviceIterator = deviceList.values().iterator();

                while(deviceIterator.hasNext()) {
                    device = (UsbDevice)deviceIterator.next();
                    if(Global.scannerAttached) {
                        Global.attachedDeviceType = deviceRecognizer.GetAttachedDeviceName();
                    }
                }
                Log.e("device typ","-->"+ Global.attachedDeviceType);
                this.handler = new BiometricDeviceHandler(this, Global.connectedDeviceID,activity);
                this.handler.SetApplicationContext(this,activity);
                Global.biometricInitialized = true;
                if(device != null) {
                    mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent("com.aadhar.commonapi.USB_PERMISSION"), 0);
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
    }
    // after scanning of fp/iris
    @Override
    public void handlerFunction(final byte[] rawImage, final int imageHeight,
                                final int imageWidth, final int status, final String errorMessage,
                                final boolean complete, final byte[] isoData, final int quality,
                                final int finalNFIQ) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.e("status", "--->"+status);
                if (status < 0) {
                    SetTextonuiThread("ERROR:" + errorMessage);
                } else if (status == 0) {
                    if (rawImage != null){
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
                    Log.e("complete", "=="+complete);
                    if (complete) {

                        dialogWaitForAuth.show();

                        startTime = System.currentTimeMillis();

                        Log.e("complete in if", "=="+complete);
                        String devicetype="F";
                        fpImgString = Base64.encodeToString(isoData,Base64.NO_WRAP);
                        //						Log.e("fp", "=="+fpImgString);
                        if(Global.iritechAttached)
                            devicetype="I";
                        //						if we are using fingure pirnt device then if part execute
                        //if ( Global.authType.equalsIgnoreCase("O") ){

                        byte[] publicKey = Base64.decode(Global.productionPublicKey, Base64.DEFAULT);
                        String aadhaar_no = Global.AUTH_AADHAAR;
                        Log.e("public key", "==="+publicKey);

                        Global.KYC_API_VERSION = "1.0";
                        String KYCAuthXml="";
                        String txtMessage = "";
                        if(btnype.equalsIgnoreCase("bio_auth")){    // Only Auth  ----bapuji
                            txtMessage = " Auth BIO";
                            if(devicetype.equalsIgnoreCase("F")){
                                Global.AUTH_USING="Fingerprint";
                                KYCAuthXml = getKYCAuthXml("F",aadhaar_no , fpImgString, "bio_auth" );
                            }else if(devicetype.equalsIgnoreCase("I")){
                                Global.AUTH_USING="Iris";
                                KYCAuthXml = getKYCAuthXml("I",aadhaar_no , fpImgString, "bio_auth" );
                            }Log.e("bio auth xml", "="+KYCAuthXml);
                            Global.Tempxmldel = KYCAuthXml;
                        }else{                      // For KYC
                            txtMessage = " KYC BIO";
                            if(devicetype.equalsIgnoreCase("F")){   // For Finger
                                KYCAuthXml = getKYCAuthXml("F",aadhaar_no , fpImgString, "" );
                            }else if(devicetype.equalsIgnoreCase("I")){   //For Iris
                                KYCAuthXml = getKYCAuthXml("I",aadhaar_no , fpImgString, "" );
                            }
                            Log.e("bio kyc xml", "="+KYCAuthXml);
                            String encodedKYCXml = Base64.encodeToString(KYCAuthXml.getBytes() , Base64.NO_WRAP);
                            String KYCXml = "";
                            if(devicetype.equalsIgnoreCase("F")){
                                Global.AUTH_USING="Fingerprint";
                                KYCXml = getKYCXml("F" , aadhaar_no, encodedKYCXml , fpImgString, "bio_ekyc");
                            }else if(devicetype.equalsIgnoreCase("I")){
                                Global.AUTH_USING="Iris";
                                KYCXml = getKYCXml("I" , aadhaar_no, encodedKYCXml , fpImgString, "bio_ekyc");
                            }
                            Global.Tempxmldel = KYCXml;
                        }

                         hitToServerforFINALRequest()

                        //						appendLog(Global.Tempxmldel);
//						ShowDialogForSavingData(txtMessage);
                        ;
						/*checkNetwork = checkConnection.isConnectingToInternet();
						if (checkNetwork) {
							//							dialogWaitForAuth.dismiss();
							hitToServerforFINALRequest();
						}else {
							log.myMessage(getResources().getString(R.string.network_not_available)+txtMessage + "after scanning", Global.AUTH_AADHAAR);
							ShowPromptNetworkforFinalHit("Network Issue", "Please Enable the network");
						}*/
                    }else{

                    }
                }
            }
        });
    }

    //	to create KYC XML
    public String getKYCXml(String deviceType,String aadhaar_no,String encodedXml , String fpImgString, String atype)
    {
        String kycXml = "";
        byte[] publicKey = null;
        //		GetPubKeycertificateData();
        publicKey = Base64.decode(Global.productionPublicKey, Base64.DEFAULT);
        if (deviceType.equalsIgnoreCase("F") || deviceType.equalsIgnoreCase("I")) {
            UidaiAuthHelper helper = new UidaiAuthHelper(publicKey);

            TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
            String IMEINumber=tm.getDeviceId();
            kycXml = helper.createXmlForKYC(aadhaar_no, deviceType, encodedXml, fpImgString, IMEINumber, atype);

        }
        return kycXml;

    }
    //	to create KYC auth/encoded Rad xml //used in bio-f
    public String getKYCAuthXml(String deviceType,String aadhaar_no ,String fpImgString, String atype)
    {
        String kycXml = "";
        byte[] publicKey = null;
        //		GetPubKeycertificateData();
        publicKey = Base64.decode(Global.productionPublicKey, Base64.DEFAULT);
        String imeiNo = CommonMethods.GetIMEI(context);
        Log.e("demoIMEI",imeiNo);
        if (deviceType.equalsIgnoreCase("F") || deviceType.equalsIgnoreCase("I") ) {
            UidaiAuthHelper helper = new UidaiAuthHelper(publicKey);
            kycXml = helper.createXmlForKycAuth(deviceType, aadhaar_no, fpImgString, false, "");
            if(atype.equalsIgnoreCase("bio_auth")){
                Log.d("cert xml : ","kycXML : "+kycXml);
                kycXml = helper.createCustomXmlForAuth(kycXml, atype);
            }
        }

        return kycXml;

    }
    public String getKYCAuthXmlForINFOCUS(String deviceType,String aadhaar_no ,String fpImgString, String atype)
    {
        String kycXml = "";
        byte[] publicKey = null;
        //		GetPubKeycertificateData();
        publicKey = Base64.decode(Global.productionPublicKey, Base64.DEFAULT);
        if (deviceType.equalsIgnoreCase("F") || deviceType.equalsIgnoreCase("I") ) {
            UidaiAuthHelper helper = new UidaiAuthHelper(publicKey);
            kycXml = helper.createXmlForKycAuthForINFOCUS(deviceType, aadhaar_no ,fpImgString , false , "");
            if(atype.equalsIgnoreCase("bio_auth")){
                kycXml = helper.createCustomXmlForAuth(kycXml, atype);
            }
        }

        return kycXml;

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

    //	to show message
    private void ShowPrompt(String title, String message) {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(
                BioAuthActivity.this);
        dlgAlert.setMessage(message);
        dlgAlert.setTitle(title);
        dlgAlert.setIcon(R.drawable.erroricon);
        dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                finish();
            }
        });
        /*dlgAlert.setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        btn_submit.setEnabled(false);

                    }
                });*/
        dlgAlert.setCancelable(false);
        dlgAlert.create().show();
    }

    //	to show network message
    private void ShowPromptNetwork(String title, String message) {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(
                BioAuthActivity.this);
        dlgAlert.setMessage(message);
        dlgAlert.setTitle(title);
        dlgAlert.setIcon(R.drawable.erroricon);
        dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                init();
            }
        });
        dlgAlert.setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        finish();
                    }
                });
        dlgAlert.setCancelable(false);
        dlgAlert.create().show();
    }

    private void ShowPromptNetworkforAUTH(String title, String message) {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(
                BioAuthActivity.this);
        dlgAlert.setMessage(message);
        dlgAlert.setTitle(title);
        dlgAlert.setIcon(R.drawable.erroricon);
        dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                startCapturingAUTH();
            }
        });
       /* dlgAlert.setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        btn_auth.setEnabled(false);
                        finish();
                    }
                });*/
        dlgAlert.setCancelable(false);
        dlgAlert.create().show();
    }

    public void repeatScanning()
    {
        if (!Global.samsungIris) {
            deviceInit = false;
            if(captureStatus){
                captureStatus=false;
            }
            if((!deviceModel.equalsIgnoreCase("mfs100")))
                handler.UnInitDevice();
        }
       // GetConformationAgain();
    }

    private void ShowPromptNetworkforFinalHit(String title, String message) {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(
                BioAuthActivity.this);
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
                        //				btn_submit.setEnabled(false);
                        Intent intent = new Intent (BioAuthActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
        dlgAlert.setCancelable(false);
        dlgAlert.create().show();
    }

    private void hitToServerforFINALRequest(){
        checkNetwork = checkConnection.isConnectingToInternet();
        countDown.cancel();
        if (checkNetwork) {
            startTime = System.currentTimeMillis();
            HitToServer task = new HitToServer();
            task.execute();
        }else{
            ShowPromptNetworkforFinalHit("Network Issue", "Please Enable the network");
        }
    }

    private void ShowNoResponse(String error) {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(
                BioAuthActivity.this);
        dlgAlert.setCancelable(false);
        String message = null;

        message = error;
        dlgAlert.setMessage(message);
        dlgAlert.setTitle("Warning");
        dlgAlert.setIcon(R.drawable.erroricon);
        dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Intent intent = new Intent(BioAuthActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();

            }
        });

        dlgAlert.setCancelable(false);
        dlgAlert.create().show();
    }

    private void ShowErrorMessage(String error) {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(
                BioAuthActivity.this);
        dlgAlert.setCancelable(false);
        String message = null;

        message = error;
        dlgAlert.setMessage(message);
        dlgAlert.setTitle("Warning");
        dlgAlert.setIcon(R.drawable.erroricon);
        dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Intent intent = new Intent(BioAuthActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
        dlgAlert.setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });
        dlgAlert.setCancelable(false);
        dlgAlert.create().show();
    }

    public void readAuthXml(String result)
    {
        /*if (Global.samsungIris) {
            countDownSamsung.cancel();
        }*/
        endTime   = System.currentTimeMillis();
        totalTime = endTime - startTime;
        String rt = null;
        try {


            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc2 = dBuilder.parse(new InputSource(new StringReader(result)));

            //optional, but recommended

            // XPath to retrieve the content of the <FamilyAnnualDeductibleAmount> tag
            XPath xpath2 = XPathFactory.newInstance().newXPath();


            XPathExpression expImei = xpath2.compile("/xml/imeivalid");
            String imeiValid = (String)expImei.evaluate(doc2, XPathConstants.STRING);
            String encodedResponse;
            Document doc = null;
            XPath xpath=null;
            if (imeiValid.equalsIgnoreCase("true")) {
                XPathExpression expRes = xpath2.compile("/xml/res");
                String response = (String)expRes.evaluate(doc2, XPathConstants.STRING);

                byte[] data = Base64.decode(response, Base64.DEFAULT);
                encodedResponse = new String(data, "UTF-8");

                xpath = XPathFactory.newInstance().newXPath();

                doc = dBuilder.parse(new InputSource(new StringReader(encodedResponse)));
                XPathExpression exprt = xpath.compile("/AuthRes/@ret");

                XPathExpression expcode = xpath.compile("/AuthRes/@code");
                String authcode = (String)expcode.evaluate(doc, XPathConstants.STRING);

                XPathExpression expts = xpath.compile("/AuthRes/@ts");
                String authrests = (String)expts.evaluate(doc, XPathConstants.STRING);

                rt = (String)exprt.evaluate(doc, XPathConstants.STRING);
                Log.e("rt", "==="+rt);
                if (rt == null) {
                    log.myMessage(getResources().getString(R.string.unable_to_get_server_response) + " AUTH BIO", Global.AUTH_AADHAAR);
                }
                String status = "";
                if (rt.equalsIgnoreCase("Y")) {
                    countDown.cancel();
                    ShowPromptMessage(rt, "", totalTime,authcode,authrests);
                    status = "Auth Bio Success";
                }else{
                    countDown.cancel();
                    status = "Auth Bio failed";
                    XPathExpression expErr = xpath.compile("/AuthRes/@err");
                    String error = (String)expErr.evaluate(doc, XPathConstants.STRING);

                    XPathExpression expErrmsg = xpath.compile("/AuthRes/@msg");
                    String errormsg = (String)expErrmsg.evaluate(doc, XPathConstants.STRING);


                    int err = Integer.parseInt(error);
                    switch (err) {
                        case 100:
                            error = "Pi (basic) attributes of demographic data did not match-"+err;
                            break;

                        case 200:
                            error = "Pa (address) attributes of demographic data did not match-"+err;
                            break;

                        case 300:
                            error = "Biometric data did not match-"+err;
                            break;

                        case 310:
                            error = "Duplicate fingers used-"+err;
                            break;

                        case 311:
                            error = "Duplicate Irises used-"+err;
                            break;

                        case 312:
                            error = "FMR and FIR cannot be used in same transaction-"+err;
                            break;

                        case 313:
                            error = "Single FIR record contains more than one finger-"+err;
                            break;

                        case 314:
                            error = "Number of FMR/FIR should not exceed 10-"+err;
                            break;

                        case 315:
                            error = "Number of IIR should not exceed 2-"+err;
                            break;

                        case 400:
                            error = "Invalid OTP value-"+err;
                            break;

                        case 401:
                            error = "Invalid TKN value-"+err;
                            break;

                        case 500:
                            error = "Invalid encryption of Skey-"+err;
                            break;

                        case 501:
                            error = "Invalid certificate identifier in ci attribute of Skey-"+err;
                            break;

                        case 502:
                            error = "Invalid encryption of Pid-"+err;
                            break;

                        case 503:
                            error = "Invalid encryption of Hmac-"+err;
                            break;

                        case 504:
                            error = "Session key re-initiation required due to expiry or key out of sync-"+err;
                            break;

                        case 505:
                            error = "Synchronized Key usage not allowed for the AUA-"+err;
                            break;

                        case 510:
                            error = "Invalid Auth XML format-"+err;
                            break;

                        case 511:
                            error = "Invalid PID XML format-"+err;
                            break;

                        case 520:
                            error = "Invalid device-"+err;
                            break;

                        case 521:
                            error = "Invalid FDC code under Meta tag-"+err;

                        case 522:
                            error = "Invalid IDC code under Meta tag-"+err;
                            break;

                        case 530:
                            error = "Invalid authenticator code-"+err;
                            break;

                        case 540:
                            error = "Invalid Auth XML version-"+err;
                            break;

                        case 541:
                            error = "Invalid PID XML version-"+err;
                            break;

                        case 542:
                            error = "AUA not authorized for ASA. This erroror will be returned if AUA and ASA do not have linking in the portal-"+err;

                        case 543:
                            error = "Sub-AUA not associated with AUA. This error will be returned if Sub-AUA specified in sa attribute is not added as Sub-AUA in portal-"+err;
                            break;

                        case 550:
                            error = "Invalid Uses element attributes-"+err;
                            break;

                        case 551:
                            error = "Invalid tid value for registered device-"+err;
                            break;

                        case 552:
                            error = "Invalid registered device key, please reset-"+err;
                            break;

                        case 553:
                            error = "Invalid registered device HOTP, please reset-"+err;

                        case 554:
                            error = "Invalid registered device encryption-"+err;
                            break;

                        case 555:
                            error = "Mandatory reset required for registered device-"+err;
                            break;

                        case 561:
                            error = "Request expired-"+err;
                            break;

                        case 562:
                            error = "Timestamp value is future time-"+err;
                            break;

                        case 563:
                            error = "Duplicate request-"+err;

                        case 564:
                            error = "HMAC Validation failed-"+err;
                            break;

                        case 565:
                            error = "AUA license has expired-"+err;
                            break;

                        case 566:
                            error = "Invalid non-decryptable license key-"+err;
                            break;

                        case 567:
                            error = "Invalid input-"+err;
                            break;

                        case 568:
                            error = "Unsupported Language-"+err;
                            break;

                        case 569:
                            error = "Digital signature verification failed-"+err;
                            break;

                        case 570:
                            error = "Invalid key info in digital signature-"+err;
                            break;

                        case 571:
                            error = "PIN Requires reset-"+err;
                            break;

                        case 572:
                            error = "Invalid biometric position-"+err;
                            break;

                        case 573:
                            error = "Pi usage not allowed as per license-"+err;
                            break;

                        case 574:
                            error = "Pa usage not allowed as per license-"+err;
                            break;

                        case 575:
                            error = "Pfa usage not allowed as per license-"+err;
                            break;

                        case 576:
                            error = "FMR usage not allowed as per license-"+err;
                            break;

                        case 577:
                            error = "FIR usage not allowed as per license-"+err;
                            break;

                        case 578:
                            error = "IIR usage not allowed as per license-"+err;
                            break;

                        case 579:
                            error = "OTP usage not allowed as per license-"+err;
                            break;

                        case 580:
                            error = "PIN usage not allowed as per license-"+err;
                            break;

                        case 581:
                            error = "Fuzzy matching usage not allowed as per license-"+err;
                            break;

                        case 582:
                            error = "Local language usage not allowed as per license-"+err;
                            break;

                        case 584:
                            error = "Invalid pincode in LOV attribute under Meta tag-"+err;
                            break;

                        case 585:
                            error = "Invalid geo-code in LOV attribute under Meta tag-"+err;
                            break;

                        case 710:
                            error = "Missing Pi data as specified in Uses-"+err;
                            break;

                        case 720:
                            error = "Missing Pa data as specified in Uses-"+err;
                            break;

                        case 721:
                            error = "Missing Pfa data as specified in Uses-"+err;
                            break;

                        case 730:
                            error = "Missing PIN data as specified in Uses-"+err;
                            break;

                        case 740:
                            error = "Missing OTP data as specified in Uses-"+err;
                            break;

                        case 800:
                            error = "Invalid biometric data-"+err;
                            break;

                        case 810:
                            error = "Missing biometric data as specified in Uses-"+err;
                            break;

                        case 811:
                            error = "Missing biometric data in CIDR for the given Aadhaar number-"+err;
                            break;

                        case 812:
                            error = "Resident has not done Best Finger Detection.-"+err;
                            break;

                        case 820:
                            error = "Missing or empty value for bt attribute in Uses element-"+err;
                            break;

                        case 821:
                            error = "Invalid value in the bt attribute of Uses element-"+err;
                            break;

                        case 901:
                            error = "No authentication data found in the request-"+err;
                            break;

                        case 902:
                            error = "Invalid dob value in the Pi element-"+err;
                            break;

                        case 910:
                            error = "Invalid mv value in the Pi element-"+err;
                            break;

                        case 911:
                            error = "Invalid mv value in the Pfa element-"+err;
                            break;

                        case 912:
                            error = "Invalid ms value-"+err;
                            break;

                        case 913:
                            error = "Both Pa and Pfa are present in the authentication request-"+err;
                            break;

                        case 930:
                            error = "Technical error that are internal to authentication server-"+err;
                            break;

                        case 931:
                            error = "Technical error that are internal to authentication server-"+err;
                            break;

                        case 932:
                            error = "Technical error that are internal to authentication server-"+err;
                            break;

                        case 933:
                            error = "Technical error that are internal to authentication server-"+err;
                            break;

                        case 934:
                            error = "Technical error that are internal to authentication server-"+err;
                            break;

                        case 935:
                            error = "Technical error that are internal to authentication server-"+err;
                            break;

                        case 936:
                            error = "Technical error that are internal to authentication server-"+err;
                            break;

                        case 937:
                            error = "Technical error that are internal to authentication server-"+err;
                            break;

                        case 938:
                            error = "Technical error that are internal to authentication server-"+err;
                            break;

                        case 939:
                            error = "Technical error that are internal to authentication server-"+err;
                            break;

                        case 940:
                            error = "Unauthorized ASA channel-"+err;
                            break;

                        case 941:
                            error = "Unspecified ASA channel-"+err;
                            break;

                        case 980:
                            error = "Unsupported option-"+err;
                            break;

                        case 997:
                            error = "Invalid Aadhaar status-"+err;
                            break;

                        case 998:
                            error = "Invalid Aadhaar Number-"+err;
                            break;

                        case 999:
                            error = "Unknown error-"+err;
                            break;

                        case 9903:
                            error = errormsg;
                            break;

                        default:
                            break;
                    }

                    ShowPromptMessage(rt,error,totalTime,authcode,authrests);
                }
                statusLog.myMessage(authcode, totalTime+" ms",status);
            }else{
                log.myMessage(getResources().getString(R.string.invalid_device)+ " AUTH BIO", Global.AUTH_AADHAAR);
                ShowPromptMessage("n","Unauthorised Device",totalTime,"","");
            }

        } catch (Exception e) {
            Log.e("Exception", "=="+e);
            appendLogInvalidXml("<<<<<<<<<<-------"+ result + "-------->>>>>>>>  AUTH BIO  >>>");
            showMyMessage("Invalid response XML");
            e.printStackTrace();
        }

    }

    public void appendLogInvalidXml(String text)
    {
        //File logFile = new File("sdcard/BIO_KYC"+ctime+".txt");
        File logFile = new File("sdcard/invalid_xml.txt");
        if (!logFile.exists())
        {
            try
            {
                logFile.createNewFile();
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        try
        {
            //BufferedWriter for performance, true to set append to file flag
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, false));
            buf.append(text);
            buf.newLine();
            buf.close();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            Log.e("IOException", "appendlog***** "+e.getMessage());
            e.printStackTrace();
        }
    }

    public void showMyMessage(String msg){
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(
                BioAuthActivity.this);
        dlgAlert.setMessage(msg);
        dlgAlert.setTitle("Warning");
        dlgAlert.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Intent intnt = new Intent(BioAuthActivity.this , MainActivity.class);
                        startActivity(intnt);
                        finish();
                    }
                });
        dlgAlert.setCancelable(false);
        dlgAlert.create().show();
    }

    private void ShowPromptMessage(String ret,String err, long totaltime, String authcode, String rests) {
        final Dialog authdialog = new Dialog(BioAuthActivity.this);
        authdialog.show();
        authdialog.setCancelable(false);
        //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //		dialog.getWindow().setLayout(width, height);
        authdialog.setContentView(R.layout.auth_information_box);
        ImageView imgviewName = (ImageView) authdialog.findViewById(R.id.imageViewAtdType);

        TextView authStatus =  (TextView)authdialog.findViewById(R.id.textView1);

        if(ret.equalsIgnoreCase("y")){
            imgviewName.setImageResource(R.drawable.atdsuccess);
            authStatus.setText("Authentication Success");
        }else{
            imgviewName.setImageResource(R.drawable.atdfail);
            authStatus.setText(err);
        }

        TextView respstatus =  (TextView)authdialog.findViewById(R.id.responsestatus);
        respstatus.setText("Auth using"+" "+Global.AUTH_USING+". Code= " + authcode);

        TextView respts =  (TextView)authdialog.findViewById(R.id.responsets);
        respts.setText("Authentication Time= " + rests);

        TextView resptime =  (TextView)authdialog.findViewById(R.id.responseTime);
        resptime.setText("Response Time - "+totaltime+" ms");


        Button btn = (Button) authdialog.findViewById(R.id.okbtn);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                authdialog.dismiss();
                //unregisterReceiver(mUsbReceiver);----------------NOT Required
                Intent intent2 = new Intent(BioAuthActivity.this , MainActivity.class);
                intent2.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent2);
                finish();
            }
        });

    }

    @Override
    public void onCaptureSuccess() {

    }

    @Override
    public void onCaptureFailed(int i) {

    }

    @Override
    public void showPreviewFrame(byte[] bytes, int i, int i1, int i2) {

    }



   /* public void readXml(String result)
    {
        String rt = null;
        if (Global.samsungIris) {
            countDownSamsung.cancel();
        }
        endTime   = System.currentTimeMillis();
        totalTime = endTime - startTime;
        Log.e("totalTime", "=== " + totalTime);

        try {

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc2 = dBuilder.parse(new InputSource(new StringReader(result)));

            //optional, but recommended

            // XPath to retrieve the content of the <FamilyAnnualDeductibleAmount> tag
            XPath xpath2 = XPathFactory.newInstance().newXPath();


            XPathExpression expImei = xpath2.compile("/xml/imeivalid");
            String imeiValid = (String)expImei.evaluate(doc2, XPathConstants.STRING);
            String encodedResponse;
            Document doc = null;
            XPath xpath=null;
            if (imeiValid.equalsIgnoreCase("true")) {
                XPathExpression expRes = xpath2.compile("/xml/res");
                String response = (String)expRes.evaluate(doc2, XPathConstants.STRING);

                byte[] data = Base64.decode(response, Base64.DEFAULT);
                encodedResponse = new String(data, "UTF-8");
                //appendLog(encodedResponse);
                xpath = XPathFactory.newInstance().newXPath();

                doc = dBuilder.parse(new InputSource(new StringReader(encodedResponse)));
                XPathExpression exprt = xpath.compile("/KycRes/@ret");
                rt = (String)exprt.evaluate(doc, XPathConstants.STRING);
                Log.e("rt", "==="+rt);

                if (rt == null) {
                    log.myMessage(getResources().getString(R.string.unable_to_get_server_response) + " KYC BIO", Global.AUTH_AADHAAR);
                }
                String authcode = "";
                String status = "";
                if (rt.equalsIgnoreCase("Y")) {
                    status = "KYC Bio success";
                    XPathExpression expcode = xpath.compile("/KycRes/@code");
                    authcode = (String)expcode.evaluate(doc, XPathConstants.STRING);

                    XPathExpression expts = xpath.compile("/KycRes/@ts");
                    String authrests = (String)expts.evaluate(doc, XPathConstants.STRING);

                    //APPEND TO TEXT FILES
                    //appendLog(result);

                    countDown.cancel();
                    XPathExpression expr = xpath.compile("//UidData/Poi/@name");
                    String name = (String)expr.evaluate(doc, XPathConstants.STRING);
                    Log.e("name", "==="+name);
                    XPathExpression exprDob = xpath.compile("//UidData/Poi/@dob");
                    String dob = (String)exprDob.evaluate(doc, XPathConstants.STRING);
                    Log.e("dob", "==="+dob);
                    XPathExpression exprGender = xpath.compile("//UidData/Poi/@gender");
                    String gender = (String)exprGender.evaluate(doc, XPathConstants.STRING);
                    Log.e("gender", "==="+gender);
                    XPathExpression exprCo = xpath.compile("//UidData/Poa/@co");
                    String co = (String)exprCo.evaluate(doc, XPathConstants.STRING);
                    Log.e("co", "==="+co);
                    XPathExpression expremail = xpath.compile("//UidData/Poi/@email");
                    String email = (String)expremail.evaluate(doc, XPathConstants.STRING);
                    Log.e("email", "==="+email);
                    XPathExpression exprphone = xpath.compile("//UidData/Poi/@phone");
                    String phone = (String)exprphone.evaluate(doc, XPathConstants.STRING);
                    Log.e("phone", "==="+phone);
                    XPathExpression exprsubdist = xpath.compile("//UidData/Poa/@subdist");
                    String subdist = (String)exprsubdist.evaluate(doc, XPathConstants.STRING);
                    Log.e("subdist", "==="+subdist);
                    XPathExpression exprvtc = xpath.compile("//UidData/Poa/@vtc");
                    String vtc = (String)exprvtc.evaluate(doc, XPathConstants.STRING);
                    Log.e("vtc", "==="+vtc);
                    XPathExpression exprvtcCode = xpath.compile("//UidData/Poa/@vtcCode");
                    String vtcCode = (String)exprvtcCode.evaluate(doc, XPathConstants.STRING);
                    Log.e("vtcCode", "==="+vtcCode);
                    XPathExpression exprHN = xpath.compile("//UidData/Poa/@house");
                    String house = (String)exprHN.evaluate(doc, XPathConstants.STRING);
                    Log.e("house", "==="+house);
                    XPathExpression exprLoc = xpath.compile("//UidData/Poa/@loc");
                    String loc = (String)exprLoc.evaluate(doc, XPathConstants.STRING);
                    Log.e("loc", "==="+loc);
                    XPathExpression exprPo = xpath.compile("//UidData/Poa/@po");
                    String po = (String)exprPo.evaluate(doc, XPathConstants.STRING);
                    Log.e("po", "==="+po);
                    XPathExpression exprDist = xpath.compile("//UidData/Poa/@dist");
                    String dist = (String)exprDist.evaluate(doc, XPathConstants.STRING);
                    Log.e("dist", "==="+dist);
                    XPathExpression exprPc = xpath.compile("//UidData/Poa/@pc");
                    String pc = (String)exprPc.evaluate(doc, XPathConstants.STRING);
                    Log.e("pc", "==="+pc);
                    XPathExpression exprState = xpath.compile("//UidData/Poa/@state");
                    String state = (String)exprState.evaluate(doc, XPathConstants.STRING);
                    Log.e("state", "==="+state);
                    XPathExpression exprPhoto = xpath.compile("//UidData/Pht");
                    String photo = (String)exprPhoto.evaluate(doc, XPathConstants.STRING);
                    Log.e("exprPhoto", "==="+photo);
                    String address = house + " "+loc+" " + po ;

                    XPathExpression exprlocname = xpath.compile("//UidData/LData/@name");
                    String locname = (String)exprlocname.evaluate(doc, XPathConstants.STRING);
                    Log.e("locname", "==="+locname);

                    XPathExpression exprlocco = xpath.compile("//UidData/LData/@co");
                    String locco = (String)exprlocco.evaluate(doc, XPathConstants.STRING);
                    Log.e("locco", "==="+locco);

                    XPathExpression exprlocHN = xpath.compile("//UidData/LData/@house");
                    String lochouse = (String)exprlocHN.evaluate(doc, XPathConstants.STRING);
                    XPathExpression exprlocLoc = xpath.compile("//UidData/LData/@loc");
                    String locloc = (String)exprlocLoc.evaluate(doc, XPathConstants.STRING);
                    XPathExpression exprlocPo = xpath.compile("//UidData/LData/@po");
                    String locpo = (String)exprlocPo.evaluate(doc, XPathConstants.STRING);
                    Log.e("po", "==="+po);
                    String locaddress = lochouse + " "+locloc+" " + locpo ;
                    XPathExpression exprlocstate = xpath.compile("//UidData/LData/@state");
                    String locstate = (String)exprlocstate.evaluate(doc, XPathConstants.STRING);
                    XPathExpression exprlocdist = xpath.compile("//UidData/LData/@dist");
                    String locdist = (String)exprlocdist.evaluate(doc, XPathConstants.STRING);

                    //showData(name,dob,gender,address,dist,pc,state,photo);
                    showData(name,locname,dob,gender,co,locco,email,phone,subdist,vtc,vtcCode,address,locaddress,dist,locdist,pc,state,locstate,photo, totalTime,authcode, authrests);
                }else{
                    countDown.cancel();
                    status = "KYC Bio failed";
                    XPathExpression expRet = xpath.compile("/Resp/@ret");
                    String ret = (String)expRet.evaluate(doc, XPathConstants.STRING);

                    XPathExpression expcode = xpath.compile("/Resp/@code");
                    authcode = (String)expcode.evaluate(doc, XPathConstants.STRING);

                    XPathExpression expts = xpath.compile("/Resp/@ts");
                    String authrests = (String)expts.evaluate(doc, XPathConstants.STRING);

                    XPathExpression expErr = xpath.compile("/Resp/@err");
                    String err = (String)expErr.evaluate(doc, XPathConstants.STRING);

                    XPathExpression expErrMsg = xpath.compile("/Resp/@msg");
                    String errmsg = (String)expErrMsg.evaluate(doc, XPathConstants.STRING);

                    Log.e("err", "==="+err);
                    Log.e("ret", "==="+ret);
                    if (err.equalsIgnoreCase("K-100")) {
                        err =  "Resident authentication failed-"+err;
                    }else if (err.equalsIgnoreCase("K-200")) {
                        err =  "Resident data currently not available-"+err;
                    }else if (err.equalsIgnoreCase("K-540")) {
                        err =  "Invalid KYC XML-"+err;
                    }else if (err.equalsIgnoreCase("K-541")) {
                        err =  "Invalid e-KYC API version-"+err;
                    }else if (err.equalsIgnoreCase("K-542")) {
                        err =  "Invalid resident consent-"+err;
                    }else if (err.equalsIgnoreCase("K-543")) {
                        err =  "Invalid timestamp-"+err;
                    }else if (err.equalsIgnoreCase("K-544")) {
                        err =  "Invalid resident auth type-"+err;
                    }else if (err.equalsIgnoreCase("K-545")) {
                        err = err + "\n" + "Resident has opted-out of this service-"+err;
                    }else if (err.equalsIgnoreCase("K-550")) {
                        err = err + "\n" + "Invalid Uses Attribute-"+err;
                    }else if (err.equalsIgnoreCase("K-551")) {
                        err =  "Invalid Txn namespace-"+err;
                    }else if (err.equalsIgnoreCase("K-552")) {
                        err =  "Invalid License key-"+err;
                    }else if (err.equalsIgnoreCase("K-569")) {
                        err =  "Digital signature verification failed for e-KYC XML-"+err;
                    }else if (err.equalsIgnoreCase("K-570")) {
                        err =  "Invalid key info in digital signature for e-KYC XML-"+err;
                    }else if (err.equalsIgnoreCase("K-600")) {
                        err =  "AUA is invalid or not an authorized KUA-"+err;
                    }else if (err.equalsIgnoreCase("K-601")) {
                        err =  "ASA is invalid or not an authorized KSA-"+err;
                    }else if (err.equalsIgnoreCase("K-602")) {
                        err =  "KUA encryption key not available-"+err;
                    }else if (err.equalsIgnoreCase("K-603")) {
                        err =  "KSA encryption key not available-"+err;
                    }else if (err.equalsIgnoreCase("K-604")) {
                        err =  "KSA Signature not allowed-"+err;
                    }else if (err.equalsIgnoreCase("K-605")) {
                        err =  "Neither KUA key nor KSA encryption key are available-"+err;
                    }else if (err.equalsIgnoreCase("K-955")) {
                        err =  "Technical Failure-"+err;
                    }else if (err.equalsIgnoreCase("K-999")) {
                        err =  "Unknown error-"+err;
                    }else if (err.equalsIgnoreCase("9903")) {
                        err = errmsg;
                    }else {
                        err = err + "\n" + " e-KYC failed";
                    }
                    ShowPromptMessage(ret, err, totalTime,authcode,authrests);
                }
                statusLog.myMessage(authcode, totalTime+" ms",status);
            }else{
                log.myMessage(getResources().getString(R.string.invalid_device)+ " KYC BIO", Global.AUTH_AADHAAR);
                ShowPromptMessage("n","Unauthorised Device",totalTime,"","");
            }

        } catch (Exception e) {
            Log.e("Exception", "=="+e);
            showMyMessage("Invalid response XML");
            appendLogInvalidXml("<<<<<<<<<<-------"+ result + "-------->>>> KYC BIO  >>>>>>>");
            e.printStackTrace();
        }
    }*/







/*    public class CountDownSamsung extends CountDownTimer
    {

        public CountDownSamsung(long startTime, long interval)
        {
            super(startTime, interval);
        }

        @Override
        public void onFinish()
        {

            textviewTimer.setTextColor(Color.parseColor("#d62d20"));
            textviewTimer.setText(R.string.eye_scan_time_up);
            if (!captureStatus ) {                                                              // CAPTURE STATUS FALSE HERE ALL THE TIMES
                if(btnype.equalsIgnoreCase("bio_auth"))
                    log.myMessage("No fingure captured" + " KYC Auth", Global.AUTH_AADHAAR);
                else
                    log.myMessage("No fingure captured" + " KYC BIO", Global.AUTH_AADHAAR);
                repeatScanning();
            }

        }

        @Override
        public void onTick(long millisUntilFinished)
        {
            Log.e("cowntown", "== "+ millisUntilFinished/1000);
            textviewTimer.setTextColor(Color.parseColor("#000000"));
            textviewTimer.setText(getResources().getString(R.string.scan_your_eye) + millisUntilFinished/1000 );

        }
    }*/

    @Override
    public void onEyeInfoUiHints(String s, String s1) {

    }

    @Override
    public void onTimeOut() {

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

    //	To hit server
    private class HitToServer extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            captureStatus = true;
            String res = "";
            Log.e("request xml", "=====" + Global.Tempxmldel);
            res = CommonMethods.HttpPostLifeCerticiate(Global.KYC_URL, Global.Tempxmldel,"","");
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
                et_aadhaar.setText("");
                // rgDevice.clearCheck();
                if (btnype.equalsIgnoreCase("bio_auth"))
                    readAuthXml(result);
                /*else
                    readXml(result);*/
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


}
