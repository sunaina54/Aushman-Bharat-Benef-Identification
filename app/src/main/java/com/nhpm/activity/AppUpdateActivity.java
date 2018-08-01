package com.nhpm.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.customComponent.CustomAsyncTask;
import com.customComponent.TaskListener;
import com.customComponent.utility.DateTimeUtil;
import com.customComponent.utility.ProjectPrefrence;
import com.nhpm.BaseActivity;
import com.nhpm.LocalDataBase.DatabaseHelpers;
import com.nhpm.Models.response.master.response.AppUpdatVersionResponse;
import com.nhpm.Models.response.verifier.VerifierLoginResponse;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;
import com.nhpm.Utility.AppUtility;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class AppUpdateActivity extends BaseActivity {
    private Button updateApkBT, cancelUpdate;
    //private RelativeLayout backLayout;
    // private ImageView backIV;
    private TextView updateVersion, currentVersion, releaseDate, whatsNew;
    private Activity activity;
    private Context context;
    private AlertDialog askForPinDailog;
    private String updateVersionCode;
    private Cursor cursor;
    private Uri uri;
    private boolean downloading;
    private ProgressDialog progressDialog;
    private AppUpdatVersionResponse respItem;
    private boolean isCancelled = false;
    private String TAG = "AppUpdateActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_update);
        context = this;
        activity = this;
        respItem = (AppUpdatVersionResponse) getIntent().getSerializableExtra("VERSION");
        cancelUpdate = (Button) findViewById(R.id.cancelUpdate);
        updateApkBT = (Button) findViewById(R.id.updateApk);
        whatsNew = (TextView) findViewById(R.id.whatsNew);
        //    headerTV = (TextView) findViewById(R.id.centertext);
        currentVersion = (TextView) findViewById(R.id.currentVersion);
        updateVersion = (TextView) findViewById(R.id.updateVersion);
        releaseDate = (TextView) findViewById(R.id.releaseDate);
        if (respItem != null && respItem.getVersionName() != null) {
            updateVersion.setText(context.getResources().getString(R.string.updateVersion) + respItem.getVersionName());
            if (respItem.getReleaseDate() != null) {
                releaseDate.setText(context.getResources().getString(R.string.releaseDate) + DateTimeUtil.convertTimeMillisIntoStringDate(Long.parseLong(respItem.getReleaseDate())));
            }
        }

        if (respItem.getDescription() != null && !respItem.getDescription().equalsIgnoreCase("")) {
            whatsNew.setText("Whats New : \n" + respItem.getDescription().replace(",", "\n"));
        }

        currentVersion.setText(context.getResources().getString(R.string.currentVersion) + String.valueOf(findApplicationVersionName()));
        // headerTV.setText(context.getResources().getString(R.string.UpdateApp));
       /* backIV = (ImageView) findViewById(R.id.back);
        backLayout = (RelativeLayout) findViewById(R.id.backLayout);*/
        updateApkBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkAvailable()) {
                    alertForsoftwareLock();
                } else {
                    AppUtility.alertWithOk(context, context.getResources().getString(R.string.internet_connection_msg));
                }
            }
        });
        cancelUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Intent theIntent = new Intent(context, LoginActivity.class);
                startActivity(theIntent);*/
                //finish();
            /*    Intent theIntent = new Intent(context, BlockDetailActivity.class);
                startActivity(theIntent);*/
                finish();
                leftTransition();
            }
        });
    }

    private void askPinToLock() {
        AppUtility.softKeyBoard(activity, 1);
        askForPinDailog = new AlertDialog.Builder(context).create();
        LayoutInflater factory = LayoutInflater.from(context);
        View alertView = factory.inflate(R.layout.ask_pin_layout, null);
        askForPinDailog.setView(alertView);
        askForPinDailog.show();
        final VerifierLoginResponse verifierDetail = VerifierLoginResponse.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.VERIFIER_CONTENT, context));
        final EditText pinET = (EditText) alertView.findViewById(R.id.deletPinET);
        final TextView errorTV = (TextView) alertView.findViewById(R.id.invalidOtpTV);
        pinET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
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
                AppUtility.softKeyBoard(activity, 0);
                String pin = pinET.getText().toString();
                if (pin.toString().equalsIgnoreCase(verifierDetail.getPin())) {
                    askForPinDailog.dismiss();
                    updateApk();

                } else if (pin.equalsIgnoreCase("")) {
                    errorTV.setVisibility(View.VISIBLE);
                    errorTV.setText(context.getResources().getString(R.string.enterThePin));
                    pinET.setText("");
                } else if (!pin.toString().equalsIgnoreCase(verifierDetail.getPin())) {
                    errorTV.setVisibility(View.VISIBLE);
                    errorTV.setText(context.getResources().getString(R.string.enteTheCorrectPin));
                    pinET.setText("");
                }
            }
        });
        cancelBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askForPinDailog.dismiss();
            }
        });
    }

    private void updateApk() {
        //showHideProgressDialog(true);

        String destination = DatabaseHelpers.DELETE_FOLDER_PATH + "/";//Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/";
        String fileName = "NHPS_FVS.apk";
        destination += fileName;
        uri = Uri.parse("file://" + destination);
        File file = new File(destination);
        if (file.exists())
            file.delete();
        String url = AppConstant.PLAY_STORE_LINK;//AppConstant.APP_UPDATE_URL;//"https://docs.google.com/uc?export=download&id=0B2c4QvlR_2eGYXNYRDF2NTJXelU";//
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setDestinationUri(uri);
        final DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        final long downloadId = manager.enqueue(request);
        progressDialog = new ProgressDialog(context);
        progressDialog.setIndeterminateDrawable(context.getResources().getDrawable(R.mipmap.nhps_logo));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMessage(context.getResources().getString(R.string.downloadingSoftware));
        progressDialog.setCancelable(false);
        progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, context.getResources().getString(R.string.cancelDownloading), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                manager.remove(downloadId);
                cursor.close();
                isCancelled = true;
                downloading = false;
                progressDialog.dismiss();
            }
        });
        progressDialog.show();
        /*BroadcastReceiver onComplete = new BroadcastReceiver() {
            public void onReceive(Context ctxt, Intent intent) {
                try {
                    unregisterReceiver(this);
                }catch (Exception ex){

                }
                progressDialog.dismiss();
                if (!isCancelled) {
                    try {
                        unregisterReceiver(this);
                    }catch (Exception ex){

                    }
                    Intent install = new Intent(Intent.ACTION_VIEW);
                    install.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    install.setDataAndType(uri, "application/vnd.android.package-archive");
                    startActivity(install);
*//*
                    Intent intentco = new Intent(context, ExitActivity.class);
                    intentco.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    finish();*//*
                }

            }
        };*/
        registerReceiver(taskCompleteListener, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        new Thread(new Runnable() {
            @Override
            public void run() {
                downloading = true;
                while (downloading) {
                    try {
                        DownloadManager.Query q = new DownloadManager.Query();
                        q.setFilterById(downloadId);
                        cursor = manager.query(q);
                        cursor.moveToFirst();
                        int bytes_downloaded = cursor.getInt(cursor
                                .getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                        int bytes_total = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                        if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                            downloading = false;
                        }
                        //  final double dl_progress = (bytes_downloaded / bytes_total) * 100;
                        final int dl_progress = (int) ((bytes_downloaded * 100l) / bytes_total);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.setProgress((int) dl_progress);
                            }
                        });
                        cursor.close();
                    } catch (Exception ex) {

                    }
                }

            }
        }).start();

        AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Download Id : " + downloadId);

    }

    private final BroadcastReceiver taskCompleteListener = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                unregisterReceiver(taskCompleteListener);
            } catch (Exception ex) {
            }
            progressDialog.dismiss();
            if (!isCancelled) {
                try {
                    unregisterReceiver(taskCompleteListener);
                } catch (Exception ex) {
                }
                Intent install = new Intent(Intent.ACTION_VIEW);
                install.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                install.setDataAndType(uri, "application/vnd.android.package-archive");
                startActivity(install);
               /* Intent intentco = new Intent(context, ExitActivity.class);
                intentco.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);*/
                finish();
            }
        }
    };

    private void updateApp() {
        TaskListener taskListener = new TaskListener() {
            @Override
            public void execute() {
                downloadInstall(AppConstant.APP_UPDATE_URL);
            }

            @Override
            public void updateUI() {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/download/" + "app.apk")), "application/vnd.android.package-archive");
                startActivity(intent);
            }
        };
        CustomAsyncTask asyncTask = new CustomAsyncTask(taskListener, context.getResources().getString(R.string.please_wait), context);
        asyncTask.execute();
    }

    public void downloadInstall(String apkurl) {
        try {
            URL url = new URL(apkurl);
            HttpURLConnection c = (HttpURLConnection) url.openConnection();
            c.setRequestMethod("GET");
            c.setDoOutput(true);
            c.connect();
            String PATH = Environment.getExternalStorageDirectory() + "/download/";
            File file = new File(PATH);
            file.mkdirs();
            File outputFile = new File(file, "app.apk");
            FileOutputStream fos = new FileOutputStream(outputFile);
            InputStream is = c.getInputStream();
            byte[] buffer = new byte[is.available()];
            int len1 = 0;
            while ((len1 = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len1);
            }
            fos.close();
            is.close();

        } catch (IOException e) {
            AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Download Error");
        }
    }

    private void alertForsoftwareLock() {
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
  /*      nameAsInAdhar.setText("Name as in Govt Id :");
        nameAsInAadharTV.setText(voterIdName);
        nameAsInSeccTV.setText(seccItem.getName());*/
        msgTV.setText(context.getResources().getString(R.string.appUpdatePopUp));
        Button tryAgainBT = (Button) alertView.findViewById(R.id.tryAgainBT);
        tryAgainBT.setText(context.getResources().getString(R.string.Confirm_Submit));
        final Button cancelBT = (Button) alertView.findViewById(R.id.cancelBT);
        tryAgainBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                internetDiaolg.dismiss();
                askPinToLock();

            }
        });
        cancelBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                internetDiaolg.dismiss();
            }
        });
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


    private String findApplicationVersionName() {
        PackageManager packageManager = context.getPackageManager();
        String packageName = context.getPackageName();
        String versionCode = null; // initialize String

        try {
            versionCode = packageManager.getPackageInfo(packageName, 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

}



