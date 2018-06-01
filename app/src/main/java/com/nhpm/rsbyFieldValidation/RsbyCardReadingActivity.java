package com.nhpm.rsbyFieldValidation;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.smartcardio.TerminalFactory;
import android.smartcardio.ipc.ICardService;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.acs.smartcard.Features;
import com.acs.smartcard.Reader;
import com.acs.smartcard.Reader.OnStateChangeListener;
import com.customComponent.CustomAsyncTask;
import com.customComponent.TaskListener;
import com.example.cardreaderlib.Readcard;
import com.nhpm.R;
import com.nhpm.Utility.AppUtility;
import com.nhpm.activity.SearchActivityWithHouseHold;
import com.identive.libs.SCard;
import com.pointman.mobiledesigner.PointManJNI;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import static com.nhpm.PrintCard.UsbHelper.ACTION_USB_PERMISSION;


public class RsbyCardReadingActivity extends Activity {

    private static final String ACTION_USB_PERMISSION_acs = "com.android.example.USB_PERMISSION";
    private static final String ACTION_USB_PERMISSION_scm = "com.android.scard.USB_PERMISSION";
    long startTime;
    long countUp;
    private Readcard rd;
    private AlertDialog internetDiaolg;
    private RelativeLayout backLayout;
    private ImageView back;
    private FileOutputStream fos = null;
    private File file;
    private boolean isCalled = false;
    private boolean isACS, isSCM, isOMNI;
    private ArrayAdapter<String> spinneradapter;
    private RadioGroup radioGroupreaders;
    private Spinner spinner;
    private CheckBox chk_fam;
    private CheckBox chk_photo;
    private CheckBox chk_memdet;
    private CheckBox chk_minudet;
    private CheckBox chk_insu;
    private CheckBox chk_bal;
    private CheckBox chk_do;
  //  private ProgressDialog dialogbox;
    private CustomAsyncTask asyncTask;
    // ACS reader
    // Javelin reader
    private PointManJNI jni;
    private UsbDevice udevice;
    private UsbManager mManager;
    //  Javelin  receiver
    private final BroadcastReceiver mJavelinUsbReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbDevice device = (UsbDevice) intent
                            .getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (intent.getBooleanExtra(
                            UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if (device != null) {
                            // pointman connect
                            udevice = device;
                            // FROM NATIVE
                            String dname = udevice.getDeviceName();
                            final UsbDeviceConnection conn = mManager.openDevice(udevice);
                            int fd = conn.getFileDescriptor();
                            if (fd == -1) {
                                Log.d("Javelin", "Fails to open DeviceConnection");
                            } else {
                                Log.d("Javelin", "Opened DeviceConnection" + Integer.toString(conn.getFileDescriptor()));
                            }
                            //javelin interface 1
                            int ret = jni.OpenDevice(dname, fd, 1);
                            if (ret == 0) {
                                //printtaskjavelin();
                            }
                        }
                    } else {
                    }
                }
            } else if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
            } else if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action)) {
            }
        }
    };
    private Reader mReader;
    private PendingIntent mPermissionIntentacs;
    private ArrayAdapter<String> mReaderAdapter;
    private ArrayAdapter<String> mSpinnerAdapter;
    private final BroadcastReceiver mUsbDetection = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            getCardReaderName();
            //  hideProgressDialog();
            dismisProgressDialog();
        }
    };
    private ArrayAdapter<String> mSlotAdapter;
    private Features mFeatures = new Features();
    // SCM reader
    private SCard s1;
    private ArrayList<String> scrreaderlist;
    private PendingIntent mPermissionIntentscm;
    //private final BroadcastReceiver mUsbReceiver;

    // HID Reader
    private Button button1;
    private String requiredXml;
    private ICardService mService;
    private TerminalFactory mFactory;
    private Context context;
    // ACS receiver
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            try {
                context.unregisterReceiver(mReceiver);
            } catch (Exception ex) {

            }
            String action = intent.getAction();
            if (ACTION_USB_PERMISSION_acs.equals(action)) {
                synchronized (this) {
                    UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (intent.getBooleanExtra(
                            UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        //Toast.makeText(getApplicationContext(), "Granted", Toast.LENGTH_LONG).show();
                        if (device != null) {
                            new OpenTask().execute(device);
                        } else {
                            //    System.out.print("ACS device null");
                        }

                    } else {
                        showPopUp(context.getResources().getString(R.string.permissionDenied));
                        /*if (dialogbox != null) {
                            if (dialogbox.isShowing()) {
                                dialogbox.dismiss();

                            }
                        }*/
                        dismisProgressDialog();
                    }
                }

            } else if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
                synchronized (this) {
                    // Update reader list
                    mReaderAdapter.clear();
                    for (UsbDevice device : mManager.getDeviceList().values()) {
                        if (mReader.isSupported(device)) {
                            mReaderAdapter.add(device.getDeviceName());
                        }
                    }
                    UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);

                    if (device != null && device.equals(mReader.getDevice())) {

                        // Disable buttons

                        //	button_read.setEnabled(false);

                        // Clear slot items
                        mSlotAdapter.clear();

                        // Close reader
                        //    logMsg("Closing reader...");
                        new CloseTask().execute();


                    }
                }
            } else if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action)) {


            }
        }
    };
    //  SCM receiver
    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            try {
                context.unregisterReceiver(mUsbReceiver);
            } catch (Exception ex) {
                Toast.makeText(context, ex.toString() + "21321", Toast.LENGTH_SHORT).show();
            }
            String action = intent.getAction();
            if (ACTION_USB_PERMISSION_scm.equals(action)) {
                synchronized (this) {
                    UsbDevice device = (UsbDevice) intent
                            .getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (intent.getBooleanExtra(
                            UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        //Toast.makeText(getApplicationContext(), "Granted", Toast.LENGTH_LONG).show();
                        if (device != null) {
                            // SCM card connect
                            //Toast.makeText(getApplicationContext(), "scm granted", Toast.LENGTH_LONG).show();
                            s1.SCardEstablishContext(getApplicationContext());
                            scrreaderlist = new ArrayList<String>();
                            s1.SCardListReaders(getApplicationContext(), scrreaderlist);
                            if (scrreaderlist.size() > 0) {
                                CharSequence[] items = null;
                                items = scrreaderlist.toArray(new CharSequence[scrreaderlist.size()]);
                                String selectedRdr = (String) items[0];
                                s1.SCardConnect(selectedRdr, 1, 1);
                                rd = new Readcard(s1);
                                rd.setreadername("SCM");
                                if (!isCalled) {
                                    readCardInBackGround();
                                }
                              /*  String xml = rd.readcard(true, true, true, true, true, true, true);
                                if (!isCalled) {
                                    startPreviewScreen(xml);
                                }*/
                            }
                        } else {
                            //    System.out.print("Scm Device not connected");
                        }
                    } else {
                        showPopUp(context.getResources().getString(R.string.permissionDenied));
                      /*  if (dialogbox != null) {
                            if (dialogbox.isShowing()) {
                                dialogbox.dismiss();

                            }
                        }*/
                        dismisProgressDialog();
                    }
                }

            } else if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
                synchronized (this) {
                    // Update reader list
                    mReaderAdapter.clear();
                    for (UsbDevice device : mManager.getDeviceList().values()) {
                        if (mReader.isSupported(device)) {
                            mReaderAdapter.add(device.getDeviceName());
                        }
                    }
                    UsbDevice device = (UsbDevice) intent
                            .getParcelableExtra(UsbManager.EXTRA_DEVICE);

                    if (device != null && device.equals(mReader.getDevice())) {
                        // Disable buttons
                        //	button_read.setEnabled(false);
                        // Clear slot items
                        mSlotAdapter.clear();
                        // Close reader
                        //    logMsg("Closing reader...");
                        new CloseTask().execute();
                    }
                }
            } else if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action)) {


            }

        }
    };

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.rsbydisplay);
        context = this;
        //     dialogbox = new ProgressDialog(RsbyCardReadingActivity.this);

        back = (ImageView) findViewById(R.id.back);
        backLayout = (RelativeLayout) findViewById(R.id.backLayout);
        spinner = (Spinner) findViewById(R.id.spinner);
        chk_fam = (CheckBox) findViewById(R.id.checkBox_famdet);
        chk_photo = (CheckBox) findViewById(R.id.checkBox_photo);
        chk_memdet = (CheckBox) findViewById(R.id.checkBox_memdet);
        chk_minudet = (CheckBox) findViewById(R.id.checkBox_minudet);
        chk_insu = (CheckBox) findViewById(R.id.checkBox_insu);
        chk_bal = (CheckBox) findViewById(R.id.checkBox_bal);
        chk_do = (CheckBox) findViewById(R.id.checkBox_do);
        button1 = (Button) findViewById(R.id.button1);
        radioGroupreaders = (RadioGroup) findViewById(R.id.radioGroupreaders);
        mManager = (UsbManager) getSystemService(Context.USB_SERVICE);

        // Initialize slot spinner
        mSlotAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item);
        // Initialize reader spinner
        mReaderAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item);
        mSpinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item);
        getCardReaderName();

        // Initialize reader
        mReader = new Reader(mManager);
        mReader.setOnStateChangeListener(new OnStateChangeListener() {

            @Override
            public void onStateChange(int slotNum, int prevState, int currState) {

                if (prevState < Reader.CARD_UNKNOWN
                        || prevState > Reader.CARD_SPECIFIC) {
                    prevState = Reader.CARD_UNKNOWN;
                }

                if (currState < Reader.CARD_UNKNOWN
                        || currState > Reader.CARD_SPECIFIC) {
                    currState = Reader.CARD_UNKNOWN;
                }


            }
        });

        // Register receiver for USB permission
        mPermissionIntentacs = PendingIntent.getBroadcast(this, 0, new Intent(
                ACTION_USB_PERMISSION_acs), 0);
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_USB_PERMISSION_acs);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        registerReceiver(mReceiver, filter);


        // HID

    /*    mService = CardService.getInstance(this);
        mService.bindToService();*/


        // Hide input window
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCardReaderName();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent theIntent = new Intent(context, SearchActivityWithHouseHold.class);
                startActivity(theIntent);
                finish();
            }
        });
        backLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent theIntent = new Intent(context, SearchActivityWithHouseHold.class);
                startActivity(theIntent);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        registerReceiver(mUsbDetection, filter);
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        try {
            unregisterReceiver(mUsbDetection);
        } catch (Exception ex) {

        }
        try {
            unregisterReceiver(mReceiver);
        } catch (Exception ex) {

        }
        try {
            unregisterReceiver(mUsbReceiver);
        } catch (Exception ex) {

        }
        try {
            mReader.close();
        } catch (Exception ex) {

        }
        // mService.releaseService();
    }

    public void cardReadStart(View v) {
        //showProgressDialog();
        progressDialog();
        if (isACS) {
            rd = new Readcard(mReader);
            rd.setreadername("ACS");
            // IF reader is acs
            // Update reader list
            mReaderAdapter.clear();
            for (UsbDevice device : mManager.getDeviceList().values()) {
                if (mReader.isSupported(device)) {
                    mReaderAdapter.add(device.getDeviceName());
                }
            }

            if (mReaderAdapter.getCount() == 0)
                return;


            String deviceName = mReaderAdapter.getItem(0);
            //String deviceName = (String) mReaderSpinner.getSelectedItem();

            if (deviceName != null) {


                if (deviceName.contains("/dev/bus")) {
                    //acs

                    // For each device
                    for (UsbDevice device : mManager.getDeviceList().values()) {

                        // If device name is found
                        if (deviceName.equals(device.getDeviceName())) {
                            mManager.requestPermission(device,
                                    mPermissionIntentacs);
                            break;
                        }
                    }
                }

            }


        } else if (isSCM) {

            s1 = new SCard();
            scm_USBRequestPermission(getApplicationContext());


        } else if (isOMNI) {

            dismisProgressDialog();
        } else {
            //
            System.out.print("Not device attacted");
            if (internetDiaolg != null) {
                if (internetDiaolg.isShowing()) {
                    internetDiaolg.dismiss();
                    // RsbyReader.this.recreate();
                    alertWithOk(context, context.getResources().getString(R.string.noDeviceAttached));
                }
            }
        }


    }

    public void scm_USBRequestPermission(Context paramContext) {
        long l = 0L;
        if (paramContext == null) {
            Log.e("SCard Library", "SCARD_E_INVALID_PARAMETER");
            return;
        }
        mManager = (UsbManager) paramContext.getSystemService(Context.USB_SERVICE);
        mPermissionIntentscm = PendingIntent.getBroadcast(paramContext, 0, new Intent("com.android.scard.USB_PERMISSION"), 0);
        IntentFilter intntfilter = new IntentFilter("com.android.scard.USB_PERMISSION");
        intntfilter.addAction("android.hardware.usb.action.USB_DEVICE_ATTACHED");
        intntfilter.addAction("android.hardware.usb.action.USB_DEVICE_DETACHED");
        paramContext.registerReceiver(this.mUsbReceiver, (IntentFilter) intntfilter);
        // For each device
        for (UsbDevice device : mManager.getDeviceList().values()) {
            if (device.getVendorId() == 1254 || device.getVendorId() == 8186) {
                // Request permission
                mManager.requestPermission(device,
                        mPermissionIntentscm);
                break;
            } else {
                System.out.print("SCM device no suported");
            }
        }
    }

    private void getCardReaderName() {
        int deviceVenderId = 0;
        String deviceManufacture = null;
        mSpinnerAdapter.clear();
        mSpinnerAdapter.add("Please insert card reader");
        spinner.setAdapter(mSpinnerAdapter);
        for (UsbDevice device : mManager.getDeviceList().values()) {

            mSpinnerAdapter.add(device.getManufacturerName());
            //  mSpinnerAdapter.remove("Please insert card reader");
            deviceVenderId = device.getVendorId();
            deviceManufacture = device.getManufacturerName();
        }

        if (deviceVenderId != 0 && deviceManufacture != null) {
            if (deviceVenderId == 8186 || deviceVenderId == 1254 && deviceManufacture.equalsIgnoreCase("SCM Microsystems Inc.")) {
                isSCM = true;
                isACS = false;
                isOMNI = false;
                if (mSpinnerAdapter != null) {
                    spinner.setAdapter(mSpinnerAdapter);
                    spinner.setSelection(1);
                }
            } else if (deviceManufacture.equalsIgnoreCase("ACS") && deviceVenderId == 1839) {
                isSCM = false;
                isACS = true;
                isOMNI = false;
                if (mSpinnerAdapter != null) {
                    spinner.setAdapter(mSpinnerAdapter);
                    spinner.setSelection(1);
                }
            }
            else if (deviceManufacture.equalsIgnoreCase("OMNIKEY") && deviceVenderId == 1899){
                isSCM = false;
                isACS = false;
                isOMNI = true;
                if (mSpinnerAdapter != null) {
                    spinner.setAdapter(mSpinnerAdapter);
                    spinner.setSelection(1);
                }
            }
        } else {
            isSCM = false;
            isACS = false;
            isOMNI = false;
        }
    }

    private void startPreviewScreen(String xml) {
        isCalled = true;
        try {
            unregisterReceiver(mUsbDetection);
        } catch (Exception ex) {

        }
        try {
            unregisterReceiver(mReceiver);

        } catch (Exception ex) {

        }
        try {
            unregisterReceiver(mUsbReceiver);
        } catch (Exception ex) {
            //  Toast.makeText(context, ex.toString(), Toast.LENGTH_SHORT).show();

        }
        try {

            mReader.close();
        } catch (Exception e) {

        }
        if (xml != null) {
            if (xml.equalsIgnoreCase(context.getResources().getString(R.string.cardRemovedMsg))) {
                alertWithOk(context, context.getResources().getString(R.string.cardRemovedError));
            } else {
                Intent rsbyIntent = new Intent(context, RsbyCardDataPreview.class);
                rsbyIntent.putExtra("XML", xml);
                startActivity(rsbyIntent);
                finish();
            }
        } else {
            alertWithOk(context, context.getResources().getString(R.string.cardRemovedError));
        }
        //  RsbyCardReadingActivity.this.recreate();

    }

    private void restartActivity() {
        try {
            unregisterReceiver(mUsbDetection);
        } catch (Exception ex) {

        }
        try {
            unregisterReceiver(mReceiver);

        } catch (Exception ex) {

        }
        try {
            unregisterReceiver(mUsbReceiver);
        } catch (Exception ex) {

        }
        try {

            mReader.close();
        } catch (Exception e) {

        }
        try {
            if (asyncTask != null && !asyncTask.isCancelled()) {
                asyncTask.cancel(true);
                asyncTask = null;
            }
        } catch (Exception ex) {

        }
        // RsbyCardReadingActivity.this.recreate();
        Intent restartIntent = new Intent(this, RsbyCardReadingActivity.class);
        startActivity(restartIntent);
        finish();
    }

    public void alertWithOk(final Context mContext, final String msg) {
        RsbyCardReadingActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(RsbyCardReadingActivity.this);
                builder.setTitle(context.getResources().getString(R.string.Alert));
                builder.setMessage(msg)
                        .setCancelable(false)
                        .setPositiveButton(context.getResources().getString(R.string.OK), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //do things
                                restartActivity();
                            }
                        });
                AlertDialog alert = builder.create();
                if (!((Activity) context).isFinishing()) {
                    alert.show();
                }
            }
        });

    }

    public void showPopUp(final String msg) {
        RsbyCardReadingActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                if (!((Activity) context).isFinishing()) {
                    AppUtility.alertWithOk(RsbyCardReadingActivity.this, msg);
                }
            }
        });
    }

    private void readCardInBackGround() {

        TaskListener taskListener = new TaskListener() {
            @Override
            public void execute() {
                requiredXml = rd.readcard(true, true, true, true, true, true);
            }

            @Override
            public void updateUI() {


                startPreviewScreen(requiredXml);

            }
        };
        if (asyncTask != null) {
            asyncTask.cancel(true);
            asyncTask = null;
        }

        asyncTask = new CustomAsyncTask(taskListener, context);
        asyncTask.execute();
    }

    private void progressDialog() {
         internetDiaolg = new AlertDialog.Builder(context).create();
        LayoutInflater factory = LayoutInflater.from(context);

        View alertView = factory.inflate(R.layout.card_reader_progressdialog, null);
        internetDiaolg.setView(alertView);
        internetDiaolg.show();

        Chronometer stopWatch = (Chronometer)alertView.findViewById(R.id.chrono);
        startTime = SystemClock.elapsedRealtime();

       final TextView timmerCount = (TextView)alertView.findViewById(R.id.timmerCount);

        new CountDownTimer(60 * 1000 + 1000, 1000) {

            public void onTick(long millisUntilFinished) {

                int seconds = (int) (millisUntilFinished / 1000);
                int minutes = seconds / 60;
                seconds = seconds % 60;
                timmerCount.setVisibility(View.VISIBLE);
                timmerCount.setText(String.format("%02d", minutes)
                        + ":" + String.format("%02d", seconds));
            }

            public void onFinish() {


            }

        }.start();



        final Button cancelBT = (Button) alertView.findViewById(R.id.cancelBT);

        cancelBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                internetDiaolg.dismiss();
                restartActivity();
            }
        });
    }

    private void dismisProgressDialog(){
        if(internetDiaolg!=null) {
            if (internetDiaolg.isShowing()) {
                internetDiaolg.dismiss();
            }
        }
        try {
            unregisterReceiver(mUsbDetection);
        } catch (Exception ex) {

        }
        try {
            unregisterReceiver(mReceiver);

        } catch (Exception ex) {

        }
        try {
            unregisterReceiver(mUsbReceiver);
        } catch (Exception ex) {

        }
        try {

            mReader.close();
        } catch (Exception e) {

        }
        restartActivity();
    }

    private class OpenTask extends AsyncTask<UsbDevice, Void, Exception> {

        @Override
        protected Exception doInBackground(UsbDevice... params) {

            Exception result = null;

            try {

                mReader.open(params[0]);

            } catch (Exception e) {

                result = e;
            }

            return result;
        }

        @Override
        protected void onPostExecute(Exception result) {

            if (result != null) {
            } else {
                int numSlots = mReader.getNumSlots();
                mSlotAdapter.clear();
                for (int i = 0; i < numSlots; i++) {
                    mSlotAdapter.add(Integer.toString(i));
                }
                mFeatures.clear();
                int slotNum = 0;
                int actionNum = 2;
                if (slotNum != Spinner.INVALID_POSITION
                        && actionNum != Spinner.INVALID_POSITION) {

                    if (actionNum < Reader.CARD_POWER_DOWN
                            || actionNum > Reader.CARD_WARM_RESET) {
                        actionNum = Reader.CARD_WARM_RESET;
                    }
                    PowerParams params = new PowerParams();
                    params.slotNum = slotNum;
                    params.action = actionNum;
                    new PowerTask().execute(params);
                }

            }
        }
    }

    private class CloseTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            mReader.close();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

        }

    }

    private class PowerParams {

        public int slotNum;
        public int action;
    }

    private class PowerResult {

        public byte[] atr;
        public Exception e;
    }

    private class PowerTask extends AsyncTask<PowerParams, Void, PowerResult> {

        @Override
        protected PowerResult doInBackground(PowerParams... params) {

            PowerResult result = new PowerResult();

            try {

                result.atr = mReader.power(params[0].slotNum, params[0].action);

            } catch (Exception e) {
                alertWithOk(context, context.getResources().getString(R.string.cardRemovedError));
                result.e = e;
            }

            return result;
        }

        @Override
        protected void onPostExecute(PowerResult result) {

            if (result.e != null) {

            } else {

                // Show ATR
                if (result.atr != null) {

                    String atrstr = toHexString(result.atr);
                    //Toast.makeText(getApplicationContext(), atrstr, Toast.LENGTH_LONG).show();
                    if (!isCalled) {
                        readCardInBackGround();
                    }
                    /*String xml = rd.readcard(true, true, true, true, true, true, true);
                    if (!isCalled) {
                        startPreviewScreen(xml);
                    }*/
                } else {

                }
            }
        }

        private String toHexString(byte[] buffer) {

            String bufferString = "";

            for (int i = 0; i < buffer.length; i++) {

                String hexChar = Integer.toHexString(buffer[i] & 0xFF);
                if (hexChar.length() == 1) {
                    hexChar = "0" + hexChar;
                }

                //bufferString += hexChar.toUpperCase() + " ";
                bufferString += hexChar.toUpperCase();
            }

            return bufferString;
        }

    }
  /*  public void printtaskjavelin()
    {

            Bitmap frntk = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.back);

        Bitmap frntkresized  =  getResizedBitmap(frntk, 625, 1000);



        Bitmap frkcnvas =  Bitmap.createBitmap(frntkresized.getWidth(), frntkresized.getHeight(), frntkresized.getConfig());

        Canvas frntk1 = new Canvas(frkcnvas);
        //  frnt.drawBitmap(frntcolresized, new Matrix(), null);

        frntk1.drawBitmap(frntkresized, 0, 0, null);

        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(35f);


        //  float scale = 0.480f;

        float scale = 1;

        float offsetx = (240 / scale) ;


        float offsety = 231/ scale;

        String nameui = ((EditText) findViewById(R.id.textname)).getText().toString();

        int maxlen = 35;


        if(nameui.length()<maxlen)
        {
            frntk1.drawText( "Name: "+ nameui , offsetx , offsety, paint);

            offsety = offsety + 70/  scale;
        }
        else
        {

            String firstline = nameui.substring(0,nameui.lastIndexOf(" "));

            frntk1.drawText( "Name: "+ firstline , offsetx, offsety, paint);


            offsety = offsety + 30/ scale;

            String secondline =  nameui.substring(nameui.lastIndexOf(" ")+1, nameui.length() );


            frntk1.drawText( "             "+ secondline, offsetx, offsety, paint);


            offsety = offsety + 70/ scale;
        }


        String fnameui = ((EditText) findViewById(R.id.textfathername)).getText().toString();


        if(fnameui.length()<maxlen)
        {
            frntk1.drawText( "Father Name: "+ fnameui , offsetx, offsety, paint);

            offsety = offsety + 70/ scale;
        }
        else
        {

            String firstline = fnameui.substring(0,fnameui.lastIndexOf(" "));

            frntk1.drawText( "Father Name: "+ firstline , offsetx, offsety, paint);

            offsety = offsety + 40/ scale;

            String secondline =  fnameui.substring(fnameui.lastIndexOf(" ")+1, fnameui.length() );


            frntk1.drawText( "                          "+ secondline, offsetx, offsety, paint);


            offsety = offsety + 50 / scale ;
        }


        frntk1.drawText( "Gender: "+  ((EditText) findViewById(R.id.textViewgender)).getText().toString(), offsetx, offsety, paint);

        offsety = offsety + 70 / scale;

        frntk1.drawText( "DOB: "+  ((EditText) findViewById(R.id.editTextdob)).getText().toString(), offsetx, offsety, paint);

        paint.setTextSize(40f);

        frntk1.drawText( "NHPS ID:"+  ((EditText) findViewById(R.id.editText_nhpsid)).getText().toString(), 260/ scale, 596 / scale, paint);




        // Save front K image


        try {
            AndroidBmpUtilorig.save(frkcnvas, Environment.getExternalStorageDirectory() + "/frontk.bmp");

        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        runtask();


    }

    public void runtask()
    {
        final String filename_fc= Environment.getExternalStorageDirectory() +"/front.bmp" ;
        final String filename_fk= Environment.getExternalStorageDirectory() + "/frontk.bmp" ;
        final String filename_bc= Environment.getExternalStorageDirectory() + "/back.bmp" ;
        final String filename_bk= Environment.getExternalStorageDirectory() + "/backk.bmp" ;
        runOnUiThread(clearLog);
        Runnable r2 = new Runnable()
        {

            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        logString= "Printing Javelin...";
                        runOnUiThread(updateLog);
//
                        mHandler.postDelayed( new Runnable()
                        {
                            @Override
                            public void run()
                            {





                                // Print Start




                                byte[] print = {};

//					      			    byte[] print_data  =  jni.PrintCommand(2,print , filename_fc,filename_fk,filename_bc,filename_bk);


                                byte[] print_data  =  jni.PrintCommand(2,print , filename_fc,filename_fk,filename_bc,filename_bk);


                                if(print_data != null)
                                {
  //                                  Log.d("native:" , CommonUtils.toHexString(print_data));
                                    //	  new CountDownTimer(60000, 500) {

                                    new CountDownTimer(60000, 1000) {

                                        //  @Override
                                        public void onTick(long millisUntilFinished) {
                                            byte[] senddata = {};
                                            final byte[] data  =  jni.GetCommandBYTE(401,senddata, 0);
//                                            Log.d("native:%s" , CommonUtils.toHexString(data));


                                            Log.d("recive PJS","recive PJS===============\r\n");
                                            for (int i = 0 ; i < data.length; i++) {
                                                Log.d("PJS:",  String.format("%02x ", data[i]));

                                            }


                                            if(data[0]==0x00 )
                                            {
//							  				            	mTimer.cancel();
                                                cancel ();
                                                jni.SetConnect(false);
                                                                          return;

                                            }
                                            if(data[0]==0xFF)
                                            {
                                                cancel ();
                                                AlertDialog.Builder alt_bld = new AlertDialog.Builder(
                                                        RsbyCardReadingActivity.this);
                                                alt_bld.setMessage("Check Printer .[3]")
                                                        .setCancelable(false)
                                                        .setPositiveButton("OK",
                                                                new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog,
                                                                                        int id) {
                                                                        byte[] senddata = {};
                                                                        byte[] data2  =  jni.GetCommandBYTE(402,senddata, data[1]);
                                                                       // Log.d("native:%s" , CommonUtils.toHexString(data2));
                                                                        jni.SetConnect(false);

                                                                    }
                                                                });

                                                return;
                                            }
                                        }
                                        @Override
                                        public void onFinish() {

                                        }
                                    }.start();
                                }else
                                {
                                    AlertDialog.Builder alt_bld = new AlertDialog.Builder(
                                            RsbyCardReadingActivity.this);
                                    alt_bld.setMessage("Check Printer .[4]")
                                            .setCancelable(false)
                                            .setPositiveButton("OK",
                                                    new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog,
                                                                            int id) {

                                                        }
                                                    });
                                    return ;
                                }
                                logString = "Printing Javelin Finished";
                                runOnUiThread(updateLog);
                            }
                        }, 3000);
                    }
                });
            }
        };
        r2.run();
    }

*/
}

