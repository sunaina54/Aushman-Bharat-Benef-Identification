package com.nhpm.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.admin.DevicePolicyManager;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.multidex.MultiDex;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/*import com.SamsungKnox.SampleAdminReceiver;*/
import com.customComponent.CustomAlert;
import com.customComponent.CustomAsyncTask;
import com.customComponent.TaskListener;
import com.customComponent.utility.CustomHttp;
import com.customComponent.utility.ProjectPrefrence;
import com.nhpm.DeviceTesting.JWS;
import com.nhpm.DeviceTesting.JWSRequest;
import com.nhpm.DeviceTesting.Response;
import com.nhpm.DeviceTesting.RetrofitInterface;

import com.nhpm.LocalDataBase.dto.SeccDatabase;
import com.nhpm.Models.ApplicationDataModel;
import com.nhpm.Models.ApplicationDataModelList;
import com.nhpm.Models.response.master.ConfigurationItem;
import com.nhpm.Models.response.master.StateItem;
import com.nhpm.Models.response.master.StateItemList;
import com.nhpm.Models.response.seccMembers.DataCountModel;
import com.nhpm.Models.response.verifier.VerifierLoginResponse;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;
import com.nhpm.Utility.AppUtility;
import com.nhpm.Utility.ApplicationGlobal;
import com.nhpm.Utility.CheckDeviceRooted;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.gson.Gson;
import com.splunk.mint.Mint;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.nhpm.Utility.AppUtility.printKeyHash;

public class SplashNhps extends Activity implements GoogleApiClient.ConnectionCallbacks {

    /**
     * Created By Rajesh Kumar Dated 26/10/2016
     **/
    private ArrayList<ConfigurationItem> stateItemList;
    private Spinner dropdown;
    private StateItem selectedStateItem;
    private ConfigurationItem configurationItem;
    private Context mContext;
    private TextView splash_text;
    private TextView splash_text_mohfw;
    private TextView tv_goi;
    private AlertDialog stateDialog;
    // private String TAG = "Splash NHPS";
    private String language;
    private static int SPLASH_TIME_OUT = 6 * 1000;
    public static boolean isEmulator = false;
    public static final String GOOGLE_API_VERIFY_URL = "https://www.googleapis.com/androidcheck/v1/attestations/";

    private ArrayList<StateItem> stateListArray;
    public static String COMING_FROM_SPLASH="ComingFromSplash";

    private ApplicationDataModelList applicationDataModelList;
    private GoogleApiClient mGoogleApiClient;
    private ProgressBar mProgressBar;
    private boolean isConnected = false;
    private CustomAsyncTask countAsyncTask;

    static final int DEVICE_ADMIN_ADD_RESULT_ENABLE = 1;
    private MessageDigest md = null;
    private SplashNhps activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_nhps);


        mContext = SplashNhps.this;
        activity=this;
        printKeyHash(activity);
        createLogTable();

       /* try {
            PackageInfo info = mContext.getPackageManager().getPackageInfo(
                    mContext.getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
        Log.i("SecretKey = ",Base64.encodeToString(md.digest(), Base64.DEFAULT));*/


        mProgressBar = (ProgressBar) findViewById(R.id.mProgressBar);
        language = ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.APPLICATIONLANGUAGE, mContext);

        applicationDataModelList = ApplicationDataModelList.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.APPLICATION_DATA, mContext));

        if (applicationDataModelList != null && applicationDataModelList.getApplicationDataModel() != null && applicationDataModelList.getApplicationDataModel().size() > 0) {
            ArrayList<ApplicationDataModel> applicationDataModel = applicationDataModelList.getApplicationDataModel();
            for (ApplicationDataModel item : applicationDataModel) {

                if (item.getConfigId().equalsIgnoreCase(AppConstant.USER_DATA_TAG)) {

                    ApplicationGlobal.USER_NAME = item.getUserName();
                    ApplicationGlobal.USER_PASSWORD = item.getUserPwd();

                } else if (item.getConfigId().equalsIgnoreCase(AppConstant.AADHAAR_DATA_TAG)) {

                    ApplicationGlobal.AADHAAR_AUTH_USERNAME = item.getUserName();
                    ApplicationGlobal.AADHAAR_AUTH_ENCRIPTED_PASSWORD = item.getUserPwd();

                } else if (item.getConfigId().equalsIgnoreCase(AppConstant.MOBILE_DATA)) {

                    ApplicationGlobal.MOBILE_Username = item.getUserName();
                    ApplicationGlobal.MOBILE_Password = item.getUserPwd();

                }
            }
        } else {
            applicationDataModelList = new ApplicationDataModelList();
            ArrayList<ApplicationDataModel> applicationDataModel = SeccDatabase.getApplicationDataItem(mContext);
            for (ApplicationDataModel item : applicationDataModel) {

                if (item.getConfigId().equalsIgnoreCase(AppConstant.USER_DATA_TAG)) {

                    ApplicationGlobal.USER_NAME = item.getUserName();
                    ApplicationGlobal.USER_PASSWORD = item.getUserPwd();

                } else if (item.getConfigId().equalsIgnoreCase(AppConstant.AADHAAR_DATA_TAG)) {

                    ApplicationGlobal.AADHAAR_AUTH_USERNAME = item.getUserName();
                    ApplicationGlobal.AADHAAR_AUTH_ENCRIPTED_PASSWORD = item.getUserPwd();

                } else if (item.getConfigId().equalsIgnoreCase(AppConstant.MOBILE_DATA)) {

                    ApplicationGlobal.MOBILE_Username = item.getUserName();
                    ApplicationGlobal.MOBILE_Password = item.getUserPwd();

                }
            }
            applicationDataModelList.setApplicationDataModel(applicationDataModel);
            ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.APPLICATION_DATA, applicationDataModelList.serialize(), mContext);
        }


        if (language != null && !language.equalsIgnoreCase("")) {

            Locale mLocale = new Locale(language);
            Resources ress = getResources();
            DisplayMetrics dmm = ress.getDisplayMetrics();
            Configuration con = ress.getConfiguration();
            con.locale = mLocale;
            ress.updateConfiguration(con, dmm);

        }
        String savedSession = ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SESSIONTIMEOUT, mContext);
        if (savedSession != null && !savedSession.equalsIgnoreCase("")) {

        } else {
            ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SESSIONTIMEOUT, AppConstant.SESSIONTIME, mContext);
        }
        // Set the application environment
        Mint.setApplicationEnvironment(Mint.appEnvironmentTesting);
        // TODO: Update with your API key
       // Mint.initAndStartSession(this.getApplication(), "5e33a5e1");
        Mint.initAndStartSession(this.getApplication(), AppConstant.SPLUNK_MINT_ID);
      /* long dateTimeMillis= DateTimeUtil.currentTimeMillis();
        AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Current date and time :" +
                " "+DateTimeUtil.convertTimeMillisIntoStringDate(dateTimeMillis,"yyyy-MM-dd HH:mm:ss.SSS"));
*/
        splash_text = (TextView) findViewById(R.id.tv_splash);
        splash_text_mohfw = (TextView) findViewById(R.id.tv_splash1);
        tv_goi = (TextView) findViewById(R.id.textview_goi);

        splash_text.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in));
        splash_text_mohfw.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in));
        tv_goi.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in));
       /* if (!isNetworkAvailable()) {
            Toast.makeText(mContext, "Internet not available", Toast.LENGTH_SHORT).show();

        }
*/
        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {

                if (isNetworkAvailable()) {

                    if (SeccDatabase.findStateList(mContext) != null && SeccDatabase.findStateList(mContext).size() > 0) {
                        VerifierLoginResponse userDetail=VerifierLoginResponse.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                                AppConstant.VERIFIER_CONTENT, mContext));
                        if(userDetail==null) {
                            Intent iLoging = new Intent(mContext, LoginActivity.class);
                            startActivity(iLoging);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        }else{
                            Intent iLoging = new Intent(mContext, BlockDetailActivity.class);
                            iLoging.putExtra("Splash",COMING_FROM_SPLASH);
                            startActivity(iLoging);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        }
                    } else {
                        downloadStateMaster();
                    }


                } else {
                    CustomAlert.alertOkWithFinish(mContext,"No internet connection available");
                }
            }
        }, SPLASH_TIME_OUT);

    }



    public boolean isNetworkAvailable() {
        ConnectivityManager connec = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        // Check for network connections
        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||

                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {
            //Toast.makeText(this, " Connected ", Toast.LENGTH_LONG).show();
            return true;
        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {
            // Toast.makeText(this, " Not Connected ", Toast.LENGTH_LONG).show();
            return false;
        }
        return false;
    }



    private void initClient() {

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(SafetyNet.API)
                .addConnectionCallbacks(this)
                .build();

        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        Log.d("", "onConnected: ");

        isConnected = true;

    }

    @Override
    public void onConnectionSuspended(int i) {

        isConnected = false;
    }

    private void startVerification() {

        final byte[] nonce = getRequestNonce();

        SafetyNet.SafetyNetApi.attest(mGoogleApiClient, nonce)
                .setResultCallback(new ResultCallback<SafetyNetApi.AttestationResult>() {
                    @Override
                    public void onResult(@NonNull SafetyNetApi.AttestationResult attestationResult) {

                        Status status = attestationResult.getStatus();
                        Log.d("", "status.isSuccess()::" + status.isSuccess());
                        if (status.isSuccess()) {
                            String jwsResult = attestationResult.getJwsResult();
                            verifyOnline(jwsResult);
                        } else {

                            mProgressBar.setVisibility(View.GONE);
                            Toast.makeText(mContext, "Error !", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void verifyOnline(final String jws) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GOOGLE_API_VERIFY_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);

        JWSRequest jwsRequest = new JWSRequest();
        jwsRequest.setSignedAttestation(jws);
        Call<Response> responseCall = retrofitInterface.getResult(jwsRequest, getString(R.string.api_key));

        responseCall.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {

                boolean result = response.body().isValidSignature();
                Log.d("", "status.result::" + result);

                if (result) {

                    decodeJWS(jws);
//                    return;

                } else {

                    mProgressBar.setVisibility(View.GONE);
                    Toast.makeText(mContext, "Verification Error !", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {

                mProgressBar.setVisibility(View.GONE);
                Log.d("", "onFailure: " + t.getLocalizedMessage());
                Toast.makeText(mContext, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void decodeJWS(String jwsString) {

        byte[] json = Base64.decode(jwsString.split("[.]")[1], Base64.DEFAULT);
        String text = new String(json, StandardCharsets.UTF_8);

        Gson gson = new Gson();
        JWS jws = gson.fromJson(text, JWS.class);

//        new isDevicedRooted().execute();
        if (jws != null && jws.isBasicIntegrity()) {
            new SplashNhps.isDevicedRooted().execute();
        } else {
            // Toast.makeText(mContext, "Device is rooted.", Toast.LENGTH_SHORT).show();
            alertWithOk(mContext, "Device is Rooted");
        }
    }

    private byte[] getRequestNonce() {

        String data = String.valueOf(System.currentTimeMillis());

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        byte[] bytes = new byte[24];
        Random random = new Random();
        random.nextBytes(bytes);
        try {
            byteStream.write(bytes);
            byteStream.write(data.getBytes());
        } catch (IOException e) {
            return null;
        }

        return byteStream.toByteArray();
    }

    private class isDevicedRooted extends AsyncTask<Void, Void, Boolean> {
        private isDevicedRooted() {
        }

        protected Boolean doInBackground(Void... voidArr) {
            if (isEmulator) {
                return Boolean.valueOf(false);
            }
            boolean isDeviceRooted = CheckDeviceRooted.isDeviceRooted();
            Log.i("SplashScreenActivity", "isRooted:" + isDeviceRooted);
            return Boolean.valueOf(isDeviceRooted);
        }

        protected void onPostExecute(Boolean bool) {
            super.onPostExecute(bool);
            if (bool.booleanValue()) {

                //   Toast.makeText(mContext, "Device is Roooted)", Toast.LENGTH_SHORT).show();

                alertWithOk(mContext, "Device is Rooted");
            }
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    if (SeccDatabase.findStateList(mContext) != null && SeccDatabase.findStateList(mContext).size() > 0) {
                        Intent iLoging = new Intent(mContext, LoginActivity.class);
                        startActivity(iLoging);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        finish();
                    } else {
                        downloadStateMaster();
                    }
                }
            }, 1000);
//            mCvIsRooted.setVisibility(View.VISIBLE);

        }
    }


    public void alertWithOk(Context mContext, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Alert");
        builder.setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void downloadStateMaster() {

        TaskListener taskListener = new TaskListener() {
            @Override
            public void execute() {
                try {
                    HashMap<String, String> response = CustomHttp.httpGet(AppConstant.GET_STATE_MASTER_DATA, null);
                    if (response != null) {
                        stateListArray = new StateItemList().create(response.get(AppConstant.RESPONSE_BODY)).getStateItemList();
                        //   dataCountModel = new StateItem().create(response.get(AppConstant.RESPONSE_BODY));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void updateUI() {

                if (stateListArray != null && stateListArray.size() > 0) {
                    if (SeccDatabase.findStateList(mContext) != null && SeccDatabase.findStateList(mContext).size() > 0) {
                        String query = "delete from " + AppConstant.m_state;
                        SeccDatabase.deleteTable(query, mContext);
                    }
                    for (StateItem item : stateListArray) {
                        SeccDatabase.saveStateMaster(item,
                                mContext);
                    }
                    VerifierLoginResponse userDetail=VerifierLoginResponse.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                            AppConstant.VERIFIER_CONTENT, mContext));
                    if(userDetail==null) {
                        Intent iLoging = new Intent(mContext, LoginActivity.class);
                        startActivity(iLoging);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }else{
                        Intent iLoging = new Intent(mContext, BlockDetailActivity.class);
                        iLoging.putExtra("Splash",COMING_FROM_SPLASH);
                        startActivity(iLoging);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }
                    finish();
                } else {
                    alertWithOk(mContext, "Data not downloaded");
                }
            }
        };
        if (countAsyncTask != null) {
            countAsyncTask.cancel(true);
            countAsyncTask = null;
        }

        countAsyncTask = new CustomAsyncTask(taskListener, mProgressBar, mContext);
        countAsyncTask.execute();

    }

    private void createLogTable (){
      String query= "CREATE TABLE l_flowlog( flid bigint NOT NULL," +
              "tid bigint NOT NULL,operatorheader text NOT NULL," +
              "pagescreenname character varying(50) NOT NULL, " +
              "sequence integer NOT NULL, attempt integer, action text NOT NULL," +
              " operatorinput text NOT NULL, operatoroutput text NOT NULL," +
              " subpageinput text, subpageoutput text, error text, created_by bigint NOT NULL," +
              " creation_date date, source character varying(50)," +
              " statecode integer, districtcode integer, CONSTRAINT l_flowlog_pk PRIMARY KEY (flid))";
      try {
          SeccDatabase.createTable(query, mContext);
      }catch(Exception e) {

      }
    }


}
