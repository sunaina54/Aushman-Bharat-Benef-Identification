package com.nhpm.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.admin.DevicePolicyManager;
/*import android.app.enterprise.EnterpriseDeviceManager;
import android.app.enterprise.SecurityPolicy;
import android.app.enterprise.kioskmode.KioskMode;
import android.app.enterprise.license.EnterpriseLicenseManager;*/
import android.content.ComponentCallbacks2;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/*import com.SamsungKnox.SampleAdminReceiver;
import com.SamsungKnox.SamsungKnoxFeatures;*/
import com.customComponent.CustomAsyncTask;
import com.customComponent.TaskListener;
import com.customComponent.utility.CustomHttp;
import com.customComponent.utility.ProjectPrefrence;
import com.nhpm.AadhaarUtils.Global;
import com.nhpm.LocalDataBase.DatabaseHelpers;
import com.nhpm.LocalDataBase.dto.CommonDatabase;
import com.nhpm.LocalDataBase.dto.SeccDatabase;
import com.nhpm.Models.ApplicationLanguageItem;
import com.nhpm.Models.NotificationModel;
import com.nhpm.Models.request.LoginRequest;
import com.nhpm.Models.response.NotificationResponse;
import com.nhpm.Models.response.master.ConfigurationItem;
import com.nhpm.Models.response.master.StateItem;
import com.nhpm.Models.response.verifier.AadhaarResponseItem;
import com.nhpm.Models.response.verifier.VerifierLocationItem;
import com.nhpm.Models.response.verifier.VerifierLoginResponse;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;
import com.nhpm.Utility.AppUtility;
import com.nhpm.fragments.AadharLoginFragment;
import com.nhpm.fragments.NonAadharLoginFragment;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import pl.polidea.view.ZoomView;

import static com.nhpm.activity.SplashNhps.DEVICE_ADMIN_ADD_RESULT_ENABLE;


public class LoginActivity extends BaseActivity implements ComponentCallbacks2 {
    private static final String TAG = "LOGIN ACTIVITY";
    //  private  EditText optET;
    long saveRsbyHouseholdCount = 0;
    long saveRsbyMembersCount = 0;
    long downloadedRsbyHouseholdCount = 0;
    long downloadedRsbyMemberCount = 0;
    long saveSeccHouseholdCount = 0;
    long saveSeccMembersCount = 0;
    long downloadedSeccHouseholdCount = 0;
    long downloadedSeccMemberCount = 0;
    private final String INTERNET_LOST = "1";
    private String DOWNLOAD_COMPLETED = "DOWNLOAD_COMPLETE";
    private static EditText optET;
    private static Context mContext;
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
    private VerifierLoginResponse verifierDetail, loginResponse, storedLoginResponse;
    private LoginRequest request;
    private CheckBox termsCB;
    private TextView releaseDateTV;
    private String[] aadhaarNumber = new String[1];
    private LinearLayout toolTipLayout;
    private CustomAsyncTask asyncTask;
    private LinearLayout mZoomLinearLayout;
    private ZoomView zoomView;
    private AadhaarResponseItem aadhaarRespItem;
    private Activity activity;
    private Fragment fragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransection;
    private AlertDialog stateDialog;
    private String downloadSource = "V";
    private VerifierLocationItem verifierLocation;
    private String zoomMode = "N";
    private notificationDownloadTask notificationAsyncTask;
    private CustomAsyncTask countAsyncTask;
 /*   private final static String demoELMKey = "KLM06-ASSOA-CODG5-HMJOM-ZVKAG-EC00B";

    private DevicePolicyManager dpm;
    private EnterpriseDeviceManager edm;
    private EnterpriseLicenseManager elm;*/

    private ComponentName mDeviceAdmin;
    private final int DEVICE_ELM_KEY_REQUEST = 5;


    //private AlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        activity = this;
        mContext = this;
        selectedStateItem = StateItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SELECTED_STATE, context));
        checkAppConfig();
        if (isNetworkAvailable()) {
            new GetPubKeycertificateData().execute();
            downlOadNotification();
        }
     /*   mDeviceAdmin = new ComponentName(mContext, SampleAdminReceiver.class);

        dpm = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);

        edm = new EnterpriseDeviceManager(mContext);
        elm = EnterpriseLicenseManager.getInstance(mContext);*/

        //processOne();
        if (zoomMode.equalsIgnoreCase("N")) {
            setContentView(R.layout.login_frame_layout);
        } else {
            setContentView(R.layout.dummy_layout_for_zooming);
            mZoomLinearLayout = (LinearLayout) findViewById(R.id.mZoomLinearLayout);
            View v = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.login_frame_layout, null, false);
            v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
            zoomView = new ZoomView(this);
            zoomView.addView(v);
            mZoomLinearLayout.addView(zoomView);
        }
        getSupportActionBar().hide();

        fragmentManager = getSupportFragmentManager();
        verifierDetail = VerifierLoginResponse.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.VERIFIER_CONTENT, context));
        verifierLocation = VerifierLocationItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF
                , AppConstant.SELECTED_BLOCK, context));
        selectedStateItem = StateItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF
                , AppConstant.SELECTED_STATE, context));
        if (selectedStateItem != null) {
            loginMode();
        } else {
            alertForStateItem();
        }

        if (verifierDetail != null && verifierDetail.getLocationList() != null && verifierDetail.getLocationList().size() > 0) {
            VerifierLocationItem locationItem = verifierDetail.getLocationList().get(0);


            saveSeccHouseholdCount = SeccDatabase.VillageHouseholdCountCustom(context, locationItem.getStateCode(), locationItem.getDistrictCode(), locationItem.getTehsilCode(), locationItem.getVtCode(), AppConstant.SECC_SOURCE);
            saveSeccMembersCount = SeccDatabase.VillageMemberCountCustom(context, locationItem.getStateCode(), locationItem.getDistrictCode(), locationItem.getTehsilCode(), locationItem.getVtCode(), AppConstant.SECC_SOURCE);
            saveRsbyHouseholdCount = SeccDatabase.VillageHouseholdCountCustom(context, locationItem.getStateCode(), locationItem.getDistrictCode(), locationItem.getTehsilCode(), locationItem.getVtCode(), AppConstant.RSBY_SOURCE);
            saveRsbyMembersCount = SeccDatabase.VillageMemberCountCustom(context, locationItem.getStateCode(), locationItem.getDistrictCode(), locationItem.getTehsilCode(), locationItem.getVtCode(), AppConstant.RSBY_SOURCE);

            if (SeccDatabase.getDataCount(context) != null && SeccDatabase.getDataCount(context).getSeccHouseholdCount() != null && SeccDatabase.getDataCount(context).getSeccMemberCount() != null) {
                downloadedSeccHouseholdCount = Long.parseLong(SeccDatabase.getDataCount(context).getSeccHouseholdCount());
                downloadedSeccMemberCount = Long.parseLong(SeccDatabase.getDataCount(context).getSeccMemberCount());
            }
            if (SeccDatabase.getDataCount(context) != null && SeccDatabase.getDataCount(context).getRsbyHouseholdCount() != null && SeccDatabase.getDataCount(context).getRsbyMemberCount() != null) {
                downloadedRsbyHouseholdCount = Long.parseLong(SeccDatabase.getDataCount(context).getRsbyHouseholdCount());
                downloadedRsbyMemberCount = Long.parseLong(SeccDatabase.getDataCount(context).getRsbyMemberCount());
            }

            String data = "\n-------------------------------"
                    + "\n" + "SaveRsbyHouseholdCount:(" + saveRsbyHouseholdCount + ") != " + "DownloadedRsbyHouseholdCount:(" + downloadedRsbyHouseholdCount + ")"
                    + "\n-------------------------------"
                    + "\n" + "SaveRsbyMembersCount: (" + saveRsbyMembersCount + ") != " + "DownloadedRsbyMemberCount: (" + downloadedRsbyMemberCount + ")"
                    + "\n-------------------------------"
                    + "\n" + "SaveSeccHouseholdCount: (" + saveSeccHouseholdCount + ") != " + "DownloadedSeccHouseholdCount:(" + downloadedSeccHouseholdCount + ")"
                    + "\n-------------------------------"
                    + "\n" + "SaveSeccMembersCount: (" + saveSeccMembersCount + ") != " + "DownloadedSeccMemberCount: (" + downloadedSeccMemberCount + ")"
                    + "\n" + "-------------------------------";
            System.out.print("Check Condtion data partially downloaded: " + data);
            if (saveRsbyHouseholdCount != downloadedRsbyHouseholdCount || saveRsbyMembersCount != downloadedRsbyMemberCount || saveSeccHouseholdCount != downloadedSeccHouseholdCount || saveSeccMembersCount != downloadedSeccMemberCount) {
                String data1 = "**************************"
                        + "\n" + "SaveRsbyHouseholdCount: " + saveRsbyHouseholdCount + " != " + "DownloadedRsbyHouseholdCount" + downloadedRsbyHouseholdCount
                        + "\n" + "SaveRsbyMembersCount: " + saveRsbyMembersCount + " != " + "DownloadedRsbyMemberCount: " + downloadedRsbyMemberCount
                        + "\n" + "SaveSeccHouseholdCount: " + saveSeccHouseholdCount + " != " + "DownloadedSeccHouseholdCount: " + downloadedSeccHouseholdCount
                        + "\n" + "SaveSeccMembersCount: " + saveSeccMembersCount + " != " + "DownloadedSeccMemberCount" + downloadedSeccMemberCount
                        + "\n" + "************************";
                System.out.print(data1);

                alertWithOk(context, "Error: " + "Data downloaded partially. Clear the device  " + "\nMessage: " + data);
                //Toast.makeText(context, "Data download partially.. ", Toast.LENGTH_LONG).show();
            }

        }
    }

    public void alertWithOk(Context mContext, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Alert");
        builder.setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (AppUtility.deleteFile(new File(DatabaseHelpers.DELETE_FOLDER_PATH))) {
                            ProjectPrefrence.removeSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SELECTED_STATE, context);
                            ProjectPrefrence.removeSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.dataDownloaded, context);
                            ProjectPrefrence.removeSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.DOWNLOADINGSOURCE, context);
                            ProjectPrefrence.removeSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.VERIFIER_CONTENT, context);
                            ProjectPrefrence.removeSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.MEMBER_DOWNLOADED_COUNT, context);
                            ProjectPrefrence.removeSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.HOUSEHOLD_DOWNLOADED_COUNT, context);
                            Intent theIntent = new Intent(context, SplashNhps.class);
                            theIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            theIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(theIntent);

                        }
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    /*private void processOne() {
        //log("Activate new device administrator.");

        // This activity asks the user to grant device administrator rights to the app.
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mDeviceAdmin);
        startActivityForResult(intent, DEVICE_ADMIN_ADD_RESULT_ENABLE);
    }
*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }


    private void alertForStateItem() {
        ArrayList<String> spinnerList = new ArrayList<>();
        stateDialog = new AlertDialog.Builder(mContext).create();
        LayoutInflater factory = LayoutInflater.from(mContext);
        View alertView = factory.inflate(R.layout.activity_aftersplash, null);
        alertView.bringToFront();
        stateDialog.setView(alertView);
        stateDialog.show();
        //stateDialog.getWindow().setLayout(400, 200);
        stateDialog.setCancelable(false);
     /*   Button disableKiosMode =(Button)alertView.findViewById(R.id.disableKiosMode);
        disableKiosMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KioskMode kioskModeService = KioskMode.getInstance(context);
                if(kioskModeService.isKioskModeEnabled()){
                    kioskModeService.disableKioskMode();
                }else{
                    Toast.makeText(context,"Kiosk mode already disabled",Toast.LENGTH_SHORT).show();
                }
            }
        });*/
        final ArrayList<StateItem> stateList = SeccDatabase.findStateList(mContext);

        Collections.sort(stateList, new Comparator<StateItem>() {
            @Override
            public int compare(StateItem s1, StateItem s2) {
                return s1.getStateName().compareToIgnoreCase(s2.getStateName());
            }
        });
        Log.d("Splash", "ListSize:" + " " + stateList.size());
        stateList.add(0, new StateItem("00", "Select State"));
        if (stateList != null) {
            for (StateItem item1 : stateList) {
                spinnerList.add(item1.getStateName());
            }

        }
        TextView nameSelectTv = (TextView) alertView.findViewById(R.id.tvSelectState);
        Spinner dropdown = (Spinner) alertView.findViewById(R.id.spinner1);
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                } else {
                    selectedStateItem = stateList.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        Button buttonSbmit = (Button) alertView.findViewById(R.id.submitStateItem);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext, R.layout.custom_drop_down, R.id.textView, spinnerList);
        dropdown.setAdapter(adapter);
        buttonSbmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedStateItem != null) {
                    ArrayList<ConfigurationItem> configList = SeccDatabase.findConfiguration(selectedStateItem.getStateCode(), mContext);
                    if (configList != null) {
                        for (ConfigurationItem item1 : configList) {
                            if (item1.getConfigId().equalsIgnoreCase(AppConstant.LOGIN_CONFIG)) {
                                if (item1 != null && item1.getStatus().equalsIgnoreCase(AppConstant.LOGIN_STATUS)) {
                                    ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SELECTED_STATE, selectedStateItem.serialize(), mContext);
                                    stateDialog.dismiss();
                                    LoginActivity.this.recreate();
                                } else {
                                    ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SELECTED_STATE, selectedStateItem.serialize(), mContext);
                                    stateDialog.dismiss();
                                    LoginActivity.this.recreate();
                                }
                                break;
                            }
                            if (item1.getConfigId().equalsIgnoreCase(AppConstant.DATA_DOWNLOAD)) {
                                downloadSource = item1.getStatus();
                                ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.DOWNLOADINGSOURCE, downloadSource, context);
                            }
                        }
                    }
                    ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SELECTED_STATE, selectedStateItem.serialize(), mContext);
                }
                stateDialog.dismiss();
                LoginActivity.this.recreate();
            }
        });
    }

    private String checkAppConfig() {

        if (selectedStateItem != null && selectedStateItem.getStateCode() != null) {
            ArrayList<ConfigurationItem> configList = SeccDatabase.findConfiguration(selectedStateItem.getStateCode(), context);
            if (configList != null) {
                for (ConfigurationItem item1 : configList) {
                    if (item1.getConfigId().equalsIgnoreCase(AppConstant.DATA_DOWNLOAD)) {
                        downloadSource = item1.getStatus();
                        ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.DOWNLOADINGSOURCE, downloadSource, context);
                    }
                    if (item1.getConfigId().equalsIgnoreCase(AppConstant.APPLICATION_ZOOM)) {
                        zoomMode = item1.getStatus();
                    }
                }
            }
        }
        return null;
    }


    private void loginMode() {
        if (selectedStateItem.getStateCode() != null && !selectedStateItem.getStateCode().equalsIgnoreCase("")) {
            ArrayList<ConfigurationItem> configList = SeccDatabase.findConfiguration(selectedStateItem.getStateCode(), context);
            if (selectedStateItem != null && selectedStateItem.getLogin_type() != null && selectedStateItem.getLogin_type().equalsIgnoreCase("A")) {
                fragment = new AadharLoginFragment();
                fragmentTransection = fragmentManager.beginTransaction();
                fragmentTransection.add(R.id.fragContainer, fragment);
                fragmentTransection.commitAllowingStateLoss();
            } else if (selectedStateItem != null && selectedStateItem.getLogin_type() != null && selectedStateItem.getLogin_type().equalsIgnoreCase("N")) {
                fragment = new NonAadharLoginFragment();
                fragmentTransection = fragmentManager.beginTransaction();
                fragmentTransection.add(R.id.fragContainer, fragment);
                fragmentTransection.commitAllowingStateLoss();
            } else {
                alertWithOk(context, "Selected State does not have any login type.\nPlease select different state. ");
            }
        }
    }

    private void downloadNotificationData() {
        if (notificationAsyncTask != null && !notificationAsyncTask.isCancelled()) {
            notificationAsyncTask.cancel(true);
            notificationAsyncTask = null;
        }
        notificationAsyncTask = new notificationDownloadTask();
        notificationAsyncTask.execute();
    }

    private class notificationDownloadTask extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... params) {
            try {
                String url = AppConstant.APPLICATION_NOTIFICATION_URL + selectedStateItem.getStateCode();
                HashMap<String, String> response = CustomHttp.getStringRequest(AppConstant.APPLICATION_NOTIFICATION_URL + selectedStateItem.getStateCode(), AppConstant.AUTHORIZATION, AppConstant.AUTHORIZATIONVALUE);
                Log.d("response tag", String.valueOf(response));
                String healthSchemeResponse = AppUtility.fixEncoding(response.get(AppConstant.RESPONSE_BODY));
                AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Member response : " + healthSchemeResponse);
                if (healthSchemeResponse != null) {
                    NotificationResponse respItem = NotificationResponse.create(healthSchemeResponse);

                    if (respItem != null) {
                        if (respItem.isStatus()) {
                            String query = "delete from " + AppConstant.NOTIFICATION_TABLE;
                            SeccDatabase.deleteTable(query, context);
                            if (respItem.getNotificationList() != null && respItem.getNotificationList().size() > 0) {
                                for (NotificationModel item : respItem.getNotificationList()) {

                                    CommonDatabase.saveNotification(item, context);
                                    //showNotification(v);
                                }
                                return DOWNLOAD_COMPLETED;

                            }

                        }
                    }
                }
            } catch (Exception e) {

                return INTERNET_LOST;
            }

            return "Task Completed.";
        }

        @Override
        protected void onPostExecute(String result) {

            if (result.equalsIgnoreCase(DOWNLOAD_COMPLETED)) {

            } else if (result.equalsIgnoreCase(INTERNET_LOST)) {

            }

        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onProgressUpdate(String... values) {

        }
    }


    private void downlOadNotification() {

        TaskListener taskListener = new TaskListener() {
            @Override
            public void execute() {
                try {
                    String url = AppConstant.APPLICATION_NOTIFICATION_URL + selectedStateItem.getStateCode();
                    HashMap<String, String> response = CustomHttp.getStringRequest(AppConstant.APPLICATION_NOTIFICATION_URL + selectedStateItem.getStateCode(), AppConstant.AUTHORIZATION, AppConstant.AUTHORIZATIONVALUE);
                    //     Log.d("response tag", String.valueOf(response));
                    String notificationResponse = AppUtility.fixEncoding(response.get(AppConstant.RESPONSE_BODY));
                    AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Member response : " + notificationResponse);
                    if (notificationResponse != null) {
                        NotificationResponse respItem = NotificationResponse.create(notificationResponse);

                        if (respItem != null) {
                            if (respItem.isStatus()) {
                                String query = "delete from " + AppConstant.NOTIFICATION_TABLE;
                                SeccDatabase.deleteTable(query, context);
                                if (respItem.getNotificationList() != null && respItem.getNotificationList().size() > 0) {
                                    for (NotificationModel item : respItem.getNotificationList()) {
                                        CommonDatabase.saveNotification(item, context);
                                    }

                                }

                            }
                        }
                    }
                } catch (Exception e) {


                }

            }

            @Override
            public void updateUI() {
            }
        };
        if (countAsyncTask != null) {
            countAsyncTask.cancel(true);
            countAsyncTask = null;
        }

        countAsyncTask = new CustomAsyncTask(taskListener, context);
        countAsyncTask.execute();

    }


    private class GetPubKeycertificateData extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {

            String result = null;
            StringBuilder total = new StringBuilder();


            try {

                InputStream is = getResources().openRawResource(
                        R.raw.uidai_auth_pre_prod);

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
                Log.e("GetPubKeycert", "=" + e);
                //    ShowPrompt("Connection Issue", "Please go back...");
                System.out.println("errrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr");
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            if ((result.startsWith("-----BEGIN CERTIFICATE-----") && result.endsWith("-----END CERTIFICATE-----"))) {
                result = result.replace("-----BEGIN CERTIFICATE-----", "");
                result = result.replace("-----END CERTIFICATE-----", "");
                result = result.replace("\r\n", "");
                Global.productionPublicKey = result;

            } else {
                Log.e("PUBLIC KEY: ", "=================>>" + AppConstant.aadhaarCertificate.trim());
                Global.productionPublicKey = AppConstant.aadhaarCertificate.trim();
            }


        }
    }


    @Override
    public void onBackPressed() {
        exitPopUp();
    }


    private void exitPopUp() {
        AlertDialog.Builder ab = new AlertDialog.Builder(LoginActivity.this);
        ab.setTitle(context.getResources().getString(R.string.Alert));
        ab.setMessage(context.getResources().getString(R.string.areYouSureToExit));
        ab.setPositiveButton(context.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                try {
                    //if you want to kill app . from other then your main avtivity.(Launcher)
                    android.os.Process.killProcess(android.os.Process.myPid());
                    System.exit(1);
                } catch (Exception ex) {

                }

                finish();
            }
        });
        ab.setNegativeButton(context.getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        ab.show();
    }

   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        System.out.println("MainActivity.onActivityResult()");

        if (requestCode == DEVICE_ADMIN_ADD_RESULT_ENABLE) {

            switch (resultCode) {
                case Activity.RESULT_CANCELED:
                    // log("Request failed.");
                    Toast.makeText(mContext,"Request Failed",Toast.LENGTH_SHORT).show();
                    break;
                case Activity.RESULT_OK:

                   *//* EnterpriseDeviceManager edm = (EnterpriseDeviceManager) getSystemService(
                            EnterpriseDeviceManager.ENTERPRISE_POLICY_SERVICE);
                    SecurityPolicy securityPolicy = edm.getSecurityPolicy();*//*

                    EnterpriseLicenseManager elm =
                            EnterpriseLicenseManager.getInstance(mContext);
                    elm.activateLicense("87A2001FB1B31CA0B1FC450D3D592B58BBD6B259938691B0088BE9A1F36894992F2A5162CF2813A10ABD13C48B203E17E649A4FFF9C66DBD80B28D6075EAE54F");

                    Toast.makeText(mContext,"Admin activated",Toast.LENGTH_SHORT).show();


           *//*         try {
                        ComponentName enterpriseDeviceAdmin = new ComponentName(context,
                                LoginActivity.class);
                        securityPolicy.setRequireStorageCardEncryption(enterpriseDeviceAdmin,true);
                        SamsungKnoxFeatures. enableInternalEncryption(enterpriseDeviceAdmin,mContext);
                    }catch(SecurityException e) {
                        Log.w(TAG,"SecurityException: "+e);
                    }*//*
   *//*                 log("Device administrator activated.");
                    btn2.setEnabled(true);
                    btn1.setEnabled(false);*//*
                    break;
            }
        }
    };*/
}