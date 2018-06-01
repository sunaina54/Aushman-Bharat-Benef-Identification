package com.nhpm.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.customComponent.CustomAlert;
import com.customComponent.CustomAsyncTask;
import com.customComponent.CustomHttpClient;
import com.customComponent.TaskListener;
import com.customComponent.utility.DateTimeUtil;
import com.customComponent.utility.ProjectPrefrence;
import com.nhpm.BaseActivity;
import com.nhpm.CameraUtils.barcode.BarcodeCaptureActivity;
import com.nhpm.LocalDataBase.dto.SeccDatabase;
import com.nhpm.Models.AadharAuthItem;
import com.nhpm.Models.request.AadhaarAuthRequestItem;
import com.nhpm.Models.response.AadhaarCaptureDetailItem;
import com.nhpm.Models.response.AadhaarGenderItem;
import com.nhpm.Models.response.AadhaarStatusItem;
import com.nhpm.Models.response.GovernmentIdItem;
import com.nhpm.Models.response.seccMembers.SeccMemberItem;
import com.nhpm.Models.response.seccMembers.SelectedMemberItem;
import com.nhpm.Models.response.verifier.DemoAuthResponseItem;
import com.nhpm.Models.response.verifier.VerifierLoginResponse;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;
import com.nhpm.Utility.AppUtility;
import com.nhpm.Utility.ApplicationGlobal;
import com.nhpm.Utility.Verhoeff;
import com.nhpm.fragments.AadharAuthFingerPrintKycFragment;
import com.nhpm.fragments.AadharAuthIrisKycFragment;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import pl.polidea.view.ZoomView;

public class CaptureAadhaarActivity extends BaseActivity {
    private static final int BARCODE_READER_REQUEST_CODE = 1;
    private int QR_REQUEST_CODE = 1;
    private AadhaarCaptureDetailItem aadhaarItem;
    private SeccMemberItem seccItem;
    private String consent = "Y";
    private Button validateAadhaarBT, validateLaterBT;
    private SelectedMemberItem selectedMemItem;
    private VerifierLoginResponse loginResponse;
    private ImageView verified, rejected, pending, qrCodeAadhaarPendingIV, qrCodeAadhaarRejectedIV, qrCodeAadhaarVerifiedIV;
    private AlertDialog internetDiaolg;
    private CheckBox consentCB;
    private static final String TAG = "Capture Activity";
    private Button aadhaarDetailBT, bankDetailBT;
    private ArrayList<AadhaarStatusItem> aadhaarStatusList;
    private ArrayList<GovernmentIdItem> govtIdStatusList;
    private Context context;
    private Activity activity;
    private TextView headerTV;
    private ImageView backIV;
    private TextView dobTV, ageTV;
    private ArrayList<AadhaarGenderItem> genderList;
    private Button captureAadharDetBT;
    private int memberType;
    private RadioGroup consentGroup;
    private int CONSENT_PHOTO_REQUEST = 2;
    private Bitmap consentBitmap;
    private boolean isConsent;
    private boolean isYobSelected;
    private boolean isDobSelected;

    private boolean isVeroff;
    private LinearLayout mZoomLinearLayout;
    private ZoomView zoomView;
    private RadioGroup radioGroup, dateOfBirthRG, genderRG;
    private RadioButton radioButtonQrcode, radioButtonManual, maleRB, femaleRB, otherRB, dobRB, yobRB;
    private LinearLayout qrCodeAadhaarLayout, manualAadhaarLayout;
    private boolean qrCodeLayoutEnable = true;
    private TaskListener mTaskListener;
    private CustomAsyncTask mAsycncTask;
    private String response;
    private EditText qrCodeAadharNumberET, qrCodeNameAsInAadhaarET, qrCodeDobAsInAadhaarET,
            qrCodeGenderAsInAadhaarET, manualAadharNumberET, manualNameAsInAadhaarET,
            manualGenderAsInAadhaarET, manualDobAsInAadhaarET, manualYobAsInAadhaarET;
    private String manualGenderSelection;
    private AadharAuthItem aadharAuthItem;
    private FragmentManager fragmentManager;
    private Fragment fragment;
    private FragmentTransaction fragmentTransection;
    private RadioGroup aadharAuthRG;
    private RadioButton irisRadioButton, biometricRadioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dummy_layout_for_zooming);
        setupScreen();
        logoutScreen();
    }

    private void logoutScreen() {
        final ImageView settings = (ImageView) findViewById(R.id.settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(context, settings);
                popup.getMenuInflater()
                        .inflate(R.menu.logout_home, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.logout:
                                // logoutVerifier();
                                break;
                        }
                        return true;
                    }
                });
                popup.show();
            }
        });

    }

    private void setupScreen() {
        context = this;
        activity = this;
        getSupportActionBar().hide();


        aadharAuthItem = new AadharAuthItem();

        View v = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.activity_capture_aadhaar, null, false);
        v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
        //memberType=getIntent().getIntExtra(AppConstant.MEMBER_TYPE, -1);

        showNotification(v);
        aadharAuthRG = (RadioGroup) v.findViewById(R.id.aadharAuthRG);
        irisRadioButton = (RadioButton) v.findViewById(R.id.irisRadioButton);
        biometricRadioButton = (RadioButton) v.findViewById(R.id.biometricRadioButton);

        String authType = ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.AUTHTYPESELECTED, context);
        if (authType != null && !authType.equalsIgnoreCase("")) {

            if (authType.equalsIgnoreCase(AppConstant.FINGER)) {
                biometricRadioButton.setChecked(true);
                fragmentManager = getSupportFragmentManager();
                fragment = new AadharAuthFingerPrintKycFragment();
                fragmentTransection = fragmentManager.beginTransaction();
                fragmentTransection = fragmentTransection.replace(R.id.frameContainer, fragment);
                fragmentTransection.commitAllowingStateLoss();
            } else if (authType.equalsIgnoreCase(AppConstant.IRIS)) {
                irisRadioButton.setChecked(true);
                fragmentManager = getSupportFragmentManager();
                fragment = new AadharAuthIrisKycFragment();
                fragmentTransection = fragmentManager.beginTransaction();
                fragmentTransection = fragmentTransection.replace(R.id.frameContainer, fragment);
                fragmentTransection.commitAllowingStateLoss();
            }

        } else {
            biometricRadioButton.setChecked(true);
            fragmentManager = getSupportFragmentManager();
            fragment = new AadharAuthFingerPrintKycFragment();
            fragmentTransection = fragmentManager.beginTransaction();
            fragmentTransection = fragmentTransection.replace(R.id.frameContainer, fragment);
            fragmentTransection.commitAllowingStateLoss();
        }

        aadharAuthRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (checkedId == irisRadioButton.getId()) {
                    fragmentManager = getSupportFragmentManager();
      /*  fragment = new AadharAuthFingerPrintKycFragment();*/
                    fragment = new AadharAuthIrisKycFragment();
                    fragmentTransection = fragmentManager.beginTransaction();
                    fragmentTransection = fragmentTransection.replace(R.id.frameContainer, fragment);
                    fragmentTransection.commitAllowingStateLoss();
                    ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                            AppConstant.AUTHTYPESELECTED, AppConstant.IRIS, context);

                } else if (checkedId == biometricRadioButton.getId()) {
                    fragmentManager = getSupportFragmentManager();
                    fragment = new AadharAuthFingerPrintKycFragment();
                    fragmentTransection = fragmentManager.beginTransaction();
                    fragmentTransection = fragmentTransection.replace(R.id.frameContainer, fragment);
                    fragmentTransection.commitAllowingStateLoss();


                }
            }
        });


        consentCB = (CheckBox) v.findViewById(R.id.consentCheck);
        validateAadhaarBT = (Button) v.findViewById(R.id.validateAdhaarBT);
        validateLaterBT = (Button) v.findViewById(R.id.validateAdhaarLaterBT);
        captureAadharDetBT = (Button) v.findViewById(R.id.captureAadharDetBT);
        dobTV = (TextView) v.findViewById(R.id.dob);
        consentGroup = (RadioGroup) v.findViewById(R.id.radioGroup);
        qrCodeAadhaarPendingIV = (ImageView) v.findViewById(R.id.qrCodeAadhaarPendingIV);
        qrCodeAadhaarRejectedIV = (ImageView) v.findViewById(R.id.qrCodeAadhaarRejectedIV);
        qrCodeAadhaarVerifiedIV = (ImageView) v.findViewById(R.id.qrCodeAadhaarVerifiedIV);
        headerTV = (TextView) v.findViewById(R.id.centertext);
        selectedMemItem = SelectedMemberItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.SELECTED_ITEM_FOR_VERIFICATION, context));
        loginResponse = (VerifierLoginResponse.create(ProjectPrefrence.
                getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.VERIFIER_CONTENT, context)));
        verified = (ImageView) v.findViewById(R.id.aadhaarVerifiedIV);
        rejected = (ImageView) v.findViewById(R.id.aadhaarRejectedIV);
        pending = (ImageView) v.findViewById(R.id.aadhaarPendingIV);
        backIV = (ImageView) v.findViewById(R.id.back);
        if (selectedMemItem.getSeccMemberItem() != null) {
            seccItem = selectedMemItem.getSeccMemberItem();

            if (seccItem != null && seccItem.getDataSource() != null &&
                    seccItem.getDataSource().trim().equalsIgnoreCase(AppConstant.RSBY_SOURCE)) {
                showRsbyDetail(seccItem);
            } else {
                showSeccDetail(seccItem);
            }
        }

        radioGroup = (RadioGroup) v.findViewById(R.id.radioGroup);
        radioButtonManual = (RadioButton) v.findViewById(R.id.radioButtonManual);
        radioButtonQrcode = (RadioButton) v.findViewById(R.id.radioButtonQrcode);
        manualAadhaarLayout = (LinearLayout) v.findViewById(R.id.manualAadhaarLayout);
        qrCodeAadhaarLayout = (LinearLayout) v.findViewById(R.id.qrCodeAadhaarLayout);
        qrCodeAadharNumberET = (EditText) v.findViewById(R.id.qrCodeAadharNumberET);
        //  qrCodeAadharNumberET.setEnabled(false);
        qrCodeNameAsInAadhaarET = (EditText) v.findViewById(R.id.qrCodeNameAsInAadhaarET);
        //    qrCodeNameAsInAadhaarET.setEnabled(false);
        qrCodeDobAsInAadhaarET = (EditText) v.findViewById(R.id.qrCodeDobAsInAadhaarET);
        //   qrCodeDobAsInAadhaarET.setEnabled(false);
        qrCodeGenderAsInAadhaarET = (EditText) v.findViewById(R.id.qrCodeGenderAsInAadhaarET);
        //  qrCodeGenderAsInAadhaarET.setEnabled(false);
        manualAadharNumberET = (EditText) v.findViewById(R.id.manualAadharNumberET);
        manualNameAsInAadhaarET = (EditText) v.findViewById(R.id.manualNameAsInAadhaarET);
        manualGenderAsInAadhaarET = (EditText) v.findViewById(R.id.manualGenderAsInAadhaarET);
        manualDobAsInAadhaarET = (EditText) v.findViewById(R.id.manualDobAsInAadhaarET);
        manualYobAsInAadhaarET = (EditText) v.findViewById(R.id.manualYobAsInAadhaarET);
        maleRB = (RadioButton) v.findViewById(R.id.maleRB);
        femaleRB = (RadioButton) v.findViewById(R.id.femaleRB);
        otherRB = (RadioButton) v.findViewById(R.id.otherRB);
        dobRB = (RadioButton) v.findViewById(R.id.dobRB);
        yobRB = (RadioButton) v.findViewById(R.id.yobRB);
        dateOfBirthRG = (RadioGroup) v.findViewById(R.id.dateOfBirthRG);
        genderRG = (RadioGroup) v.findViewById(R.id.genderRG);

        if (seccItem.getAadhaarCapturingMode() != null && !seccItem.getAadhaarCapturingMode().equalsIgnoreCase("")) {
            if (seccItem.getAadhaarCapturingMode().equalsIgnoreCase(AppConstant.MANUAL_MODE)) {
                radioButtonManual.setChecked(true);
                qrCodeLayoutEnable = false;
                qrCodeAadhaarLayout.setVisibility(View.GONE);
                manualAadhaarLayout.setVisibility(View.VISIBLE);
                manualLayoutVisible();
            } else {
                radioButtonQrcode.setChecked(true);
                qrCodeAadhaarLayout.setVisibility(View.VISIBLE);
                manualAadhaarLayout.setVisibility(View.GONE);
                qrCodeLayoutEnable = true;
                qrCodeLayoutVisible();
            }
        } else {
            qrCodeAadhaarLayout.setVisibility(View.VISIBLE);
            manualAadhaarLayout.setVisibility(View.GONE);
            qrCodeLayoutEnable = true;
            qrCodeLayoutVisible();
        }


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == radioButtonQrcode.getId()) {
                    qrCodeAadhaarLayout.setVisibility(View.VISIBLE);
                    manualAadhaarLayout.setVisibility(View.GONE);
                    qrCodeLayoutEnable = true;
                    qrCodeLayoutVisible();

                } else if (checkedId == radioButtonManual.getId()) {
                    qrCodeLayoutEnable = false;
                    qrCodeAadhaarLayout.setVisibility(View.GONE);
                    manualAadhaarLayout.setVisibility(View.VISIBLE);
                    manualLayoutVisible();
                }
            }

        });


        zoomView = new ZoomView(this);
        zoomView.addView(v);
        mZoomLinearLayout.addView(zoomView);


    }

    private void ManualAlertForValidateLater(final AadharAuthItem authItem, SeccMemberItem item, final String aadhaarNumber, final String aadhaarName) {
        internetDiaolg = new AlertDialog.Builder(context).create();

        final LayoutInflater factory = LayoutInflater.from(context);
        View alertView = factory.inflate(R.layout.aadhar_validation_msg_popup, null);
        internetDiaolg.setView(alertView);
        internetDiaolg.show();
        TextView nameAsInAadharTV = (TextView) alertView.findViewById(R.id.nameAsInAadharTV);
        TextView nameAsInSeccTV = (TextView) alertView.findViewById(R.id.nameAsInSeccTV);
        nameAsInAadharTV.setText(aadhaarName);
        nameAsInSeccTV.setText(item.getName());
        Button tryAgainBT = (Button) alertView.findViewById(R.id.tryAgainBT);
        tryAgainBT.setText(context.getResources().getString(R.string.Confirm_Submit));
        Button cancelBT = (Button) alertView.findViewById(R.id.cancelBT);
        tryAgainBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seccItem.setAadhaarAuth(AppConstant.PENDING_STATUS);
                /*requestAadhaarAuth(aadhaarNumber, aadhaarName);*/
               /* Intent theIntent = new Intent(context, AadharAuthActivity.class);
                theIntent.putExtra(AppConstant.AUTHITEM, authItem);
                startActivity(theIntent);*/
                internetDiaolg.dismiss();

                //submitAaadhaarDetail();
            }
        });
        cancelBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                internetDiaolg.dismiss();
            }
        });
    }

    private void requestAadhaarAuth(final String aadhaarNo, final String nameAsInAadhaar) {

       /* VolleyTaskListener taskListener = new VolleyTaskListener() {
            @Override
            public void postExecute(String response) {
                AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Login Response : " + response.toString());
                // AadhaarOtpResponse resp=AadhaarOtpResponse.create(response);
                // Log.d(TAG,"Aadhaar Response : "+demoAuth);
                DemoAuthResponseItem demoAuthItem = DemoAuthResponseItem.create(response);
                // demoAuthItem.setAadhaarAuth(demoAuthItem.getRet().trim());
                rejected.setVisibility(View.GONE);
                verified.setVisibility(View.GONE);
                pending.setVisibility(View.GONE);
                seccItem.setAadhaarAuthMode(AppConstant.DEMO_AUTH);
                if (demoAuthItem != null && demoAuthItem.getRet() != null) {
                    seccItem.setAadhaarAuth(demoAuthItem.getRet().trim());
                    if (demoAuthItem != null && demoAuthItem.getRet() != null && demoAuthItem.getRet().equalsIgnoreCase("Y")) {
                        verified.setVisibility(View.VISIBLE);
                        AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Auth response " + response.toString());
                        seccItem.setAadhaarAuth(AppConstant.VALID_STATUS);
                        seccItem.setAadhaarAuthDt(String.valueOf(DateTimeUtil.currentTimeMillis()));
                        if (qrCodeLayoutEnable) {
                            qrCodeSubmitAaadhaarDetail();
                        } else {
                            manualSubmitAaadhaarDetail();
                        }
                    } else if (demoAuthItem.getRet().equalsIgnoreCase("N")) {
                        AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Auth response " + response.toString());
                        rejected.setVisibility(View.VISIBLE);
                        CustomAlert.alertWithOk(context, "Please enter correct Aadhaar Number and Name as in Aadhaar");
                        // seccItem.setAadhaarAuth(AppConstant.INVALID_STATUS);
                    } else {
                        seccItem.setAadhaarAuth(AppConstant.PENDING_STATUS);
                        pending.setVisibility(View.VISIBLE);
                    }
                } else {
                    CustomAlert.alertWithOk(context, "Unable To Connect from UIDAI Server, Please try again.");
                }
                // SeccDatabase.updateSeccMember(seccItem,context);

            }

            @Override
            public void onError(VolleyError error) {
                //  CustomAlert.alertWithOk(context,error.getMessage());
               // seccItem.setAadhaarAuth("P");
                CustomAlert.alertWithOk(context, "Error Code:1201\n\nUnable To Connect from UIDAI Server, Please try again." );

            }



        };*/

        mTaskListener = new TaskListener() {
            @Override
            public void execute() {
                AadhaarAuthRequestItem requestItem = new AadhaarAuthRequestItem();
                requestItem.setUid(aadhaarNo);
                requestItem.setName(nameAsInAadhaar);
                String imei = AppUtility.getIMEINumber(context);
                if (imei != null) {
                    requestItem.setImeiNo(imei);
                }
                requestItem.setProject(AppConstant.PROJECT_NAME);
                requestItem.setUserName(ApplicationGlobal.AADHAAR_AUTH_USERNAME);
                requestItem.setUserPass(ApplicationGlobal.AADHAAR_AUTH_ENCRIPTED_PASSWORD);
                response = CustomHttpClient.postStringRequestWithTimeOut(AppConstant.AADHAAR_DEMO_AUTH_API_NEW, requestItem.serialize());
            }

            @Override
            public void updateUI() {
                if (response != null && !response.equalsIgnoreCase("")) {
                    //       AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Login Response : " + response.toString());
                    // AadhaarOtpResponse resp=AadhaarOtpResponse.create(response);
                    // Log.d(TAG,"Aadhaar Response : "+demoAuth);
                    DemoAuthResponseItem demoAuthItem = DemoAuthResponseItem.create(response);
                    // demoAuthItem.setAadhaarAuth(demoAuthItem.getRet().trim());
                    rejected.setVisibility(View.GONE);
                    verified.setVisibility(View.GONE);
                    pending.setVisibility(View.GONE);
                    seccItem.setAadhaarAuthMode(AppConstant.DEMO_AUTH);
                    if (demoAuthItem != null && demoAuthItem.getRet() != null) {
                        seccItem.setAadhaarAuth(demoAuthItem.getRet().trim());
                        if (demoAuthItem != null && demoAuthItem.getRet() != null && demoAuthItem.getRet().equalsIgnoreCase("Y")) {
                            verified.setVisibility(View.VISIBLE);
                            // AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Auth response " + response.toString());
                            seccItem.setAadhaarAuth(AppConstant.VALID_STATUS);
                            seccItem.setAadhaarAuthDt(String.valueOf(DateTimeUtil.currentTimeMillis()));
                            if (qrCodeLayoutEnable) {
                                qrCodeSubmitAaadhaarDetail();
                            } else {
                                manualSubmitAaadhaarDetail();
                            }
                        } else if (demoAuthItem.getRet().equalsIgnoreCase("N")) {
                            //AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Auth response " + response.toString());
                            rejected.setVisibility(View.VISIBLE);
                            CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterCorrectNoAndName));
                            // seccItem.setAadhaarAuth(AppConstant.INVALID_STATUS);
                        } else {
                            seccItem.setAadhaarAuth(AppConstant.PENDING_STATUS);
                            pending.setVisibility(View.VISIBLE);
                        }
                    } else {
                        CustomAlert.alertWithOk(context, context.getResources().getString(R.string.unableToConnectUiadiaServer));
                    }
                } else {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.connectionTerminated));
                }
            }
        };
        ProgressDialog mProgress = new ProgressDialog(context);
        mProgress.setIndeterminateDrawable(context.getResources().getDrawable(R.mipmap.nhps_logo));
        mProgress.setMessage(context.getResources().getString(R.string.please_wait));
        mProgress.setCancelable(false);
        mAsycncTask = new CustomAsyncTask(mTaskListener, mProgress, context);
        mAsycncTask.execute();

    /*    AadhaarAuthRequestItem requestItem=new AadhaarAuthRequestItem();
        requestItem.setUid(aadhaarNo);
        requestItem.setName(nameAsInAadhaar);
        String imei=AppUtility.getIMEINumber(context);
        if(imei!=null) {
            requestItem.setImeiNo(imei);
        }
        requestItem.setProject(AppConstant.PROJECT_NAME);
        requestItem.setUserName(AppConstant.AADHAAR_AUTH_USERNAME);
        requestItem.setUserPass(AppConstant.AADHAAR_AUTH_ENCRIPTED_PASSWORD);
         CustomVolley volley = new CustomVolley(taskListener, "Please wait..",AppConstant.AADHAAR_DEMO_AUTH_API_NEW,requestItem.serialize(),null,null,context);
         volley.execute();*/
    }

    private void openQRCapture() {
 /*       Intent theIntent=new Intent(context,ScalingScannerActivity.class);
        theIntent.putExtra(AppConstant.AADHAAR_CAPTURE_ITEM,aadhaarItem);
        startActivityForResult(theIntent, QR_REQUEST_CODE);
*/
        Intent theIntent = new Intent(context, BarcodeCaptureActivity.class);
        // theIntent.putExtra(AppConstant.AADHAAR_CAPTURE_ITEM,aadhaarItem);
        startActivityForResult(theIntent, BARCODE_READER_REQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "Aadhaar Detail : " + requestCode);
        // Log.d(TAG, "Aadhaar Detail : " + aadhaarItem.serialize());
        if (requestCode == BARCODE_READER_REQUEST_CODE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
                    //  Point[] p = barcode.cornerPoints;
                    parserAdhaarXML(barcode.displayValue);

                } else {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.aadhar_not_readable));
                }
            } else if (requestCode == CONSENT_PHOTO_REQUEST) {
                Bitmap image = (Bitmap) data.getExtras().get("data");
                // Log.d(TAG," Bitmap Size : "+image.getAllocationByteCount());
                //  consentPhotoIV.setImageBitmap(image);
                consentBitmap = image;
            }
        }
    }

    public void parserAdhaarXML(String rawData) {
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(context.getApplicationContext(), notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
        XmlPullParser myparser;
        XmlPullParserFactory xmlFactoryObject;

        // CustomAlert.alertWithOk(context, rawResult.getText());
        //rawData = rawData.replace("</?", "<?");
        String validatedString = AppUtility.aadharXmlCorrection(rawData);
        if (validatedString.contains("xml")) {
            try {
                xmlFactoryObject = XmlPullParserFactory.newInstance();
                myparser = xmlFactoryObject.newPullParser();

                myparser.setInput(new StringReader(validatedString));
                int event = myparser.getEventType();
                while (event != XmlPullParser.END_DOCUMENT) {
                    String name = myparser.getName();
                    switch (event) {
                        case XmlPullParser.START_TAG:
                            break;

                        case XmlPullParser.END_TAG:
                            if (name.equals("PrintLetterBarcodeData")) {
                                aadhaarItem = new AadhaarCaptureDetailItem();
                                aadhaarItem.setName(myparser.getAttributeValue(null, "name"));
                                aadhaarItem.setUid(myparser.getAttributeValue(null, "uid"));
                                aadhaarItem.setGender(myparser.getAttributeValue(null, "gender"));
                                aadhaarItem.setYob(myparser.getAttributeValue(null, "yob"));
                                aadhaarItem.setCo(myparser.getAttributeValue(null, "co"));
                                aadhaarItem.setHouse(myparser.getAttributeValue(null, "house"));
                                aadhaarItem.setStreet(myparser.getAttributeValue(null, "street"));
                                aadhaarItem.setIm(myparser.getAttributeValue(null, "Im"));
                                aadhaarItem.setLoc(myparser.getAttributeValue(null, "loc"));
                                aadhaarItem.setVtc(myparser.getAttributeValue(null, "vtc"));
                                aadhaarItem.setPo(myparser.getAttributeValue(null, "po"));
                                aadhaarItem.setDist(myparser.getAttributeValue(null, "dist"));
                                aadhaarItem.setSubDist(myparser.getAttributeValue(null, "subdist"));
                                aadhaarItem.setState(myparser.getAttributeValue(null, "state"));
                                aadhaarItem.setPc(myparser.getAttributeValue(null, "pc"));
                                aadhaarItem.setDob(myparser.getAttributeValue(null, "dob"));
                                setAadhaarDetail(aadhaarItem);
                            }
                            break;
                    }
                    event = myparser.next();
                }
            } catch (XmlPullParserException e) {
                CustomAlert.alertWithOk(context, context.getResources().getString(R.string.aadhar_invalid_data) + "\n\n" + rawData);
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void setAadhaarDetail(AadhaarCaptureDetailItem item) {
        qrCodeAadharNumberET.setText(item.getUid());
       /* qrCodeNameAsInAadhaarET.setText(item.getName());*/
        if (seccItem != null && seccItem.getDataSource() != null && seccItem.getDataSource().equalsIgnoreCase(AppConstant.RSBY_SOURCE)) {
            qrCodeNameAsInAadhaarET.setText(item.getName());
        } else {
            qrCodeNameAsInAadhaarET.setText(item.getName());
        }
        if (item.getDob() != null && !item.getDob().equalsIgnoreCase("")) {
            qrCodeDobAsInAadhaarET.setText(item.getDob());
        } else {
            if (item.getYob() != null && !item.getYob().equalsIgnoreCase("")) {
                qrCodeDobAsInAadhaarET.setText(item.getYob());
            }
        }
        if (item.getGender() != null && !item.getGender().equalsIgnoreCase("")) {
            qrCodeGenderAsInAadhaarET.setText(item.getGender());
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

                seccItem.setAadhaarAuth(AppConstant.PENDING_STATUS);
                if (qrCodeLayoutEnable) {
                    qrCodeSubmitAaadhaarDetail();
                } else {
                    manualSubmitAaadhaarDetail();
                }
                //submitAaadhaarDetail();

            }
        });
        cancelBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                internetDiaolg.dismiss();
            }
        });
    }

    private void qrCodeAlertForValidateLater(SeccMemberItem item, final String aadhaarNumber, final String aadhaarName) {
        internetDiaolg = new AlertDialog.Builder(context).create();

        final LayoutInflater factory = LayoutInflater.from(context);
        View alertView = factory.inflate(R.layout.aadhar_validation_msg_popup, null);
        internetDiaolg.setView(alertView);
        internetDiaolg.show();
        TextView nameAsInAadharTV = (TextView) alertView.findViewById(R.id.nameAsInAadharTV);
        TextView nameAsInSeccTV = (TextView) alertView.findViewById(R.id.nameAsInSeccTV);
        nameAsInAadharTV.setText(aadhaarName);
        nameAsInSeccTV.setText(item.getName());
        Button tryAgainBT = (Button) alertView.findViewById(R.id.tryAgainBT);
        tryAgainBT.setText(context.getResources().getString(R.string.Confirm_Submit));
        Button cancelBT = (Button) alertView.findViewById(R.id.cancelBT);
        tryAgainBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seccItem.setAadhaarAuth(AppConstant.PENDING_STATUS);
                requestAadhaarAuth(aadhaarNumber, aadhaarName);
                internetDiaolg.dismiss();

                //submitAaadhaarDetail();
            }
        });
        cancelBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                internetDiaolg.dismiss();
            }
        });
    }

    private void qrCodeSubmitAaadhaarDetail() {

        seccItem.setAadhaarVerifiedBy(loginResponse.getAadhaarNumber());
        if (aadhaarItem != null) {
            seccItem.setAadhaarCapturingMode(AppConstant.QR_CODE_MODE);
            seccItem.setAadhaarNo(aadhaarItem.getUid());
            seccItem.setAadhaarLm(aadhaarItem.getIm());
            seccItem.setAadhaarPc(aadhaarItem.getPc());
            seccItem.setNameAadhaar(aadhaarItem.getName());
            seccItem.setAadhaarCo(aadhaarItem.getCo());
            seccItem.setAadhaarDist(aadhaarItem.getDist());
            seccItem.setAadhaarDob(aadhaarItem.getDob());
            seccItem.setAadhaarSubdist(aadhaarItem.getSubDist());
            seccItem.setAadhaarGender(aadhaarItem.getGender());
            seccItem.setAadhaarHouse(aadhaarItem.getHouse());
            seccItem.setAadhaarLoc(aadhaarItem.getLoc());
            seccItem.setAadhaarPo(aadhaarItem.getPo());
            seccItem.setAadhaarState(aadhaarItem.getState());
            seccItem.setAadhaarStreet(aadhaarItem.getStreet());
            seccItem.setAadhaarVtc(aadhaarItem.getVtc());
            seccItem.setAadhaarYob(aadhaarItem.getYob());
            seccItem.setConsent(consent);
            seccItem.setAadhaarSurveyedStat(AppConstant.SURVEYED + "");
            if (seccItem.getAadhaarStatus().equalsIgnoreCase("1")) {

                updateAadhaarDetail();
            }
        } else if (!qrCodeAadharNumberET.getText().toString().equalsIgnoreCase("") &&
                !qrCodeNameAsInAadhaarET.getText().toString().equalsIgnoreCase("")) {

            seccItem.setAadhaarNo(qrCodeAadharNumberET.getText().toString().trim());
            seccItem.setNameAadhaar(qrCodeNameAsInAadhaarET.getText().toString().trim());
            /*if (seccItem.getAadhaarCapturingMode() == null || seccItem.getAadhaarCapturingMode().equalsIgnoreCase("")) {
                seccItem.setAadhaarCapturingMode(AppConstant.MANUAL);
            }*/
            seccItem.setAadhaarSurveyedStat(AppConstant.SURVEYED + "");
            seccItem.setConsent(consent);
            updateAadhaarDetail();
            /*// if (seccItem.getAadhaarStatus().equalsIgnoreCase("1")) {
            AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Captured Aadhaar : "+seccItem.serialize());
            Intent theIntent = new Intent(context, WithAadhaarActivity.class);
            seccItem.setLockedSave(AppConstant.SAVE + "");
            SeccDatabase.updateSeccMember(seccItem, context);
            SeccDatabase.updateHouseHold(selectedMemItem.getHouseHoldItem(), context);
            seccItem = SeccDatabase.getSeccMemberDetail(seccItem.getNhpsMemId(), context);
            selectedMemItem.setSeccMemberItem(seccItem);
            ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                    AppConstant.SELECTED_ITEM_FOR_VERIFICATION, selectedMemItem.serialize(), context);
            startActivity(theIntent);
            finish();
            rightTransition();*/
            // }
        } else {
            CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzCaptureAadhaardetails));
        }
    }

    private void manualSubmitAaadhaarDetail() {
        seccItem.setAadhaarVerifiedBy(loginResponse.getAadhaarNumber());
       /* if (selectedMemItem.getOldHeadMember() != null && selectedMemItem.getNewHeadMember() != null) {
            SeccMemberItem oldHead = selectedMemItem.getOldHeadMember();
            oldHead.setLockedSave(AppConstant.LOCKED + "");
            AppUtility.showLog(AppConstant.LOG_STATUS, TAG, " OLD Household name " +
                    ": " + oldHead.getName() + "" +
                    " Member Status " + oldHead.getMemStatus() + " House hold Status :" +
                    " " + oldHead.getHhStatus() + " Locked Save :" + oldHead.getLockedSave());
            SeccDatabase.updateSeccMember(selectedMemItem.getOldHeadMember(), context);
        }*/
        if (!manualAadharNumberET.getText().toString().equalsIgnoreCase("") &&
                !manualNameAsInAadhaarET.getText().toString().equalsIgnoreCase("")) {

            seccItem.setAadhaarNo(manualAadharNumberET.getText().toString().trim());
            seccItem.setNameAadhaar(manualNameAsInAadhaarET.getText().toString().trim());
            seccItem.setAadhaarCapturingMode(AppConstant.MANUAL_MODE);
            seccItem.setAadhaarLm("");
            seccItem.setAadhaarPc("");
            seccItem.setAadhaarCo("");
            seccItem.setAadhaarDist("");
            seccItem.setAadhaarDob("");
            seccItem.setAadhaarSubdist("");
            seccItem.setAadhaarGender("");
            seccItem.setAadhaarHouse("");
            seccItem.setAadhaarLoc("");
            seccItem.setAadhaarPo("");
            seccItem.setAadhaarState("");
            seccItem.setAadhaarStreet("");
            seccItem.setAadhaarVtc("");
            seccItem.setAadhaarYob("");
            seccItem.setConsent(consent);
            if (seccItem.getAadhaarCapturingMode() == null || seccItem.getAadhaarCapturingMode().equalsIgnoreCase("")) {
                seccItem.setAadhaarCapturingMode(AppConstant.MANUAL_MODE);
            }
            seccItem.setAadhaarSurveyedStat(AppConstant.SURVEYED + "");
            seccItem.setConsent(consent);
            if (seccItem.getAadhaarStatus().equalsIgnoreCase("1")) {
                AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Captured Aadhaar : " + seccItem.serialize());
                updateAadhaarDetail();
            }
        } else {
            CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzCaptureAadhaardetails));
        }
    }

    private void qrCodeLayoutVisible() {
        qrCodeAadharNumberET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                qrCodeAadharNumberET.setTextColor(Color.BLACK);
                isVeroff = false;
                if (charSequence.toString().length() > 11) {
                    if (!Verhoeff.validateVerhoeff(charSequence.toString())) {
                        qrCodeAadharNumberET.setTextColor(Color.RED);
                    } else {
                        isVeroff = true;
                        qrCodeAadharNumberET.setTextColor(Color.GREEN);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        if (aadhaarItem != null) {

            qrCodeAadharNumberET.setText(aadhaarItem.getUid());


            qrCodeNameAsInAadhaarET.setText(aadhaarItem.getName());


            if (aadhaarItem.getDob() != null && !aadhaarItem.getDob().equalsIgnoreCase("")) {
                qrCodeDobAsInAadhaarET.setText(aadhaarItem.getDob());
            } else if (aadhaarItem.getYob() != null && !aadhaarItem.getYob().equalsIgnoreCase("")) {
                qrCodeDobAsInAadhaarET.setText(aadhaarItem.getYob());
            }

            qrCodeGenderAsInAadhaarET.setText(aadhaarItem.getGender());

        } else {


            if (seccItem.getAadhaarCapturingMode() != null) {
                if (seccItem.getAadhaarCapturingMode().equalsIgnoreCase(AppConstant.QR_CODE_MODE) || seccItem.getAadhaarCapturingMode().equalsIgnoreCase("")) {
                    if (seccItem.getAadhaarAuth() != null && seccItem.getAadhaarAuth().equalsIgnoreCase("")) {

                    } else if (seccItem.getAadhaarAuth() != null && seccItem.getAadhaarAuth().equalsIgnoreCase(AppConstant.PENDING_STATUS)) {
                        qrCodeAadhaarPendingIV.setVisibility(View.VISIBLE);
                        if (qrCodeAadharNumberET.getText().toString().equalsIgnoreCase("")) {
                            qrCodeAadharNumberET.setText(seccItem.getAadhaarNo());
                        }
                        if (qrCodeNameAsInAadhaarET.getText().toString().equalsIgnoreCase("")) {
                            qrCodeNameAsInAadhaarET.setText(seccItem.getNameAadhaar());
                        }
                        if (qrCodeDobAsInAadhaarET.getText().toString().equalsIgnoreCase("")) {
                            if (seccItem.getAadhaarDob() != null && !seccItem.getAadhaarDob().equalsIgnoreCase("")) {
                                qrCodeDobAsInAadhaarET.setText(seccItem.getAadhaarDob());
                            } else if (seccItem.getAadhaarYob() != null && !seccItem.getAadhaarYob().equalsIgnoreCase("")) {
                                qrCodeDobAsInAadhaarET.setText(seccItem.getAadhaarYob());
                            }
                        }
                        if (qrCodeGenderAsInAadhaarET.getText().toString().equalsIgnoreCase("")) {
                            qrCodeGenderAsInAadhaarET.setText(seccItem.getAadhaarGender());
                        }
                    } else if (seccItem.getAadhaarAuth() != null && seccItem.getAadhaarAuth().equalsIgnoreCase(AppConstant.VALID_STATUS)) {
                        qrCodeAadhaarVerifiedIV.setVisibility(View.VISIBLE);

                        if (qrCodeAadharNumberET.getText().toString().equalsIgnoreCase("")) {
                            qrCodeAadharNumberET.setText(seccItem.getAadhaarNo());
                        }
                        if (qrCodeNameAsInAadhaarET.getText().toString().equalsIgnoreCase("")) {
                            qrCodeNameAsInAadhaarET.setText(seccItem.getNameAadhaar());
                        }
                        if (qrCodeDobAsInAadhaarET.getText().toString().equalsIgnoreCase("")) {
                            if (seccItem.getAadhaarDob() != null && !seccItem.getAadhaarDob().equalsIgnoreCase("")) {
                                qrCodeDobAsInAadhaarET.setText(seccItem.getAadhaarDob());
                            } else if (seccItem.getAadhaarYob() != null && !seccItem.getAadhaarYob().equalsIgnoreCase("")) {
                                qrCodeDobAsInAadhaarET.setText(seccItem.getAadhaarYob());
                            }
                        }
                        if (qrCodeGenderAsInAadhaarET.getText().toString().equalsIgnoreCase("")) {
                            qrCodeGenderAsInAadhaarET.setText(seccItem.getAadhaarGender());
                        }
                    } else if (seccItem.getAadhaarAuth() != null && seccItem.getAadhaarAuth().equalsIgnoreCase(AppConstant.INVALID_STATUS)) {
                        qrCodeAadhaarRejectedIV.setVisibility(View.VISIBLE);
                        if (qrCodeAadharNumberET.getText().toString().equalsIgnoreCase("")) {
                            qrCodeAadharNumberET.setText(seccItem.getAadhaarNo());
                        }
                        if (qrCodeNameAsInAadhaarET.getText().toString().equalsIgnoreCase("")) {
                            qrCodeNameAsInAadhaarET.setText(seccItem.getNameAadhaar());
                        }
                        if (qrCodeDobAsInAadhaarET.getText().toString().equalsIgnoreCase("")) {
                            if (seccItem.getAadhaarDob() != null && !seccItem.getAadhaarDob().equalsIgnoreCase("")) {
                                qrCodeDobAsInAadhaarET.setText(seccItem.getAadhaarDob());
                            } else if (seccItem.getAadhaarYob() != null && !seccItem.getAadhaarYob().equalsIgnoreCase("")) {
                                qrCodeDobAsInAadhaarET.setText(seccItem.getAadhaarYob());
                            }
                        }
                        if (qrCodeGenderAsInAadhaarET.getText().toString().equalsIgnoreCase("")) {
                            qrCodeGenderAsInAadhaarET.setText(seccItem.getAadhaarGender());
                        }
                    }
                    if (seccItem.getNameAadhaar() == null || seccItem.getNameAadhaar().equalsIgnoreCase("")) {
                        if (qrCodeNameAsInAadhaarET.getText().toString().equalsIgnoreCase("")) {
                            if (seccItem != null && seccItem.getDataSource() != null && seccItem.getDataSource().equalsIgnoreCase(AppConstant.RSBY_SOURCE)) {
                                qrCodeNameAsInAadhaarET.setText(seccItem.getRsbyName());
                            } else {
                                qrCodeNameAsInAadhaarET.setText(seccItem.getName());
                            }
                        }

                    }
                    //       submitBt=(Button)findViewById(R.id.submitBT);

/*
                if (seccItem.getAadhaarNo() != null) {

                    if (qrCodeAadharNumberET.getText().toString().equalsIgnoreCase("")) {
                        qrCodeAadharNumberET.setText(seccItem.getAadhaarNo());
                    }
                    if (qrCodeDobAsInAadhaarET.getText().toString().equalsIgnoreCase("")) {
                        if(seccItem.getAadhaarDob()!=null && !seccItem.getAadhaarDob().equalsIgnoreCase("")) {
                            qrCodeDobAsInAadhaarET.setText(seccItem.getAadhaarDob());
                        }else if(seccItem.getAadhaarYob()!=null && !seccItem.getAadhaarYob().equalsIgnoreCase("")){
                            qrCodeDobAsInAadhaarET.setText(seccItem.getAadhaarYob());
                        }
                    }
                    if (qrCodeGenderAsInAadhaarET.getText().toString().equalsIgnoreCase("")) {
                        qrCodeGenderAsInAadhaarET.setText(seccItem.getAadhaarGender());
                    }
                }
                if (seccItem.getNameAadhaar() != null && !seccItem.getNameAadhaar().equalsIgnoreCase("")) {

                    if (qrCodeNameAsInAadhaarET.getText().toString().equalsIgnoreCase("")) {
                        qrCodeNameAsInAadhaarET.setText(seccItem.getNameAadhaar());
                    }
                    if (qrCodeDobAsInAadhaarET.getText().toString().equalsIgnoreCase("")) {
                        if(seccItem.getAadhaarDob()!=null && !seccItem.getAadhaarDob().equalsIgnoreCase("")) {
                            qrCodeDobAsInAadhaarET.setText(seccItem.getAadhaarDob());
                        }else if(seccItem.getAadhaarYob()!=null && !seccItem.getAadhaarYob().equalsIgnoreCase("")){
                            qrCodeDobAsInAadhaarET.setText(seccItem.getAadhaarYob());
                        }
                    }
                    if (qrCodeGenderAsInAadhaarET.getText().toString().equalsIgnoreCase("")) {
                        qrCodeGenderAsInAadhaarET.setText(seccItem.getAadhaarGender());
                    }
                }*/
                }
            }
        }
        // aadharNumberET.setText("424175531180");
        // nameAsInAadhaarET.setText("Sunil Kumar");
        validateAadhaarBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtility.hideSoftInput(activity, validateAadhaarBT);
                String nameAsInAadhaar2 = qrCodeNameAsInAadhaarET.getText().toString().trim();
                String aadhaarNumber2 = qrCodeAadharNumberET.getText().toString().trim();
                if (aadhaarNumber2.trim().equalsIgnoreCase("")) {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterAadhaar));

                    return;
                }
                if (nameAsInAadhaar2.trim().equalsIgnoreCase("")) {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterName));

                    return;
                }
                if (!isVeroff) {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterValidAadhaar));

                    return;
                }
                if (!consentCB.isChecked()) {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.adhar_consent_msg));
                    return;
                }
                /*SeccMemberItem checkAadhaar=SeccDatabase.seccMemberDetailByAadhaar(aadhaarNumber.trim(),context);
              //  AppUtility.showLog(AppConstant.LOG_STATUS,TAG," Aaadhaar Number : "+checkAadhaar.getAadhaarNo());
                if(checkAadhaar!=null){
                    if(checkAadhaar.getAadhaarNo()!=null && checkAadhaar.getAadhaarNo().trim().equalsIgnoreCase(seccItem.getAadhaarNo().trim())) {
                    }else{
                        CustomAlert.alertWithOk(context, "Aadhaar Number has been  already captured");
                        return;
                    }
                }*/
                /*if(aadhaarNumber2.equalsIgnoreCase(loginResponse.getAadhaarNumber())) {
                    CustomAlert.alertWithOk(context,"You have entered verifier Aadhaar number, Please enter member Aadhaar Number to proceed");
                    return;
                }*/
               /* if(!aadhaarNumber.equalsIgnoreCase(loginResponse.getAadhaarNumber())) {
                }else{
                    CustomAlert.alertWithOk(context,"You have entered verifier Aadhaar number, Please member enter member Aadhaar number to proceed.");
                }*/

                qrCodeAlertForValidateLater(seccItem, aadhaarNumber2, nameAsInAadhaar2);


            }
        });
        validateLaterBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppUtility.hideSoftInput(activity, validateLaterBT);
                String nameAsInAadhaar = qrCodeNameAsInAadhaarET.getText().toString().trim();
                String aadhaarNumber = qrCodeAadharNumberET.getText().toString().trim();
                if (aadhaarNumber.trim().equalsIgnoreCase("")) {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterAadhaar));
                    return;
                }
                if (nameAsInAadhaar.trim().equalsIgnoreCase("")) {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterName));
                    return;
                }
                if (!isVeroff) {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterValidAadhaar));

                    return;
                }
                if (!consentCB.isChecked()) {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.adhar_consent_msg));
                    return;
                }
                //  SeccMemberItem checkAadhaar=SeccDatabase.seccMemberDetailByAadhaar(aadhaarNumber.trim(),context);

                /*if(aadhaarNumber.equalsIgnoreCase(loginResponse.getAadhaarNumber())&& seccItem.getAadhaarAuth().equalsIgnoreCase(AppConstant.VALID_STATUS)) {
                    CustomAlert.alertWithOk(context,"You have entered verifier Aadhaar number, Please member enter member Aadhaar number to proceed.");
                    return;
                }*/
                seccItem.setAadhaarAuth(AppConstant.PENDING_STATUS);
                StringBuilder alertMessage = new StringBuilder();
                alertMessage.append(context.getResources().getString(R.string.name_as_sec) + seccItem.getName() +
                        "\n" + context.getResources().getString(R.string.name_as_aadhar) + nameAsInAadhaar + "\n\n" + context.getResources().getString(R.string.aadhar_confirmation_msg));
                alertForValidateLater(nameAsInAadhaar, seccItem);
            }
        });


        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // if(seccItem.getAadhaarStatus().equalsIgnoreCase("1")) {
                Intent theIntent = new Intent(context, WithAadhaarActivity.class);
                startActivity(theIntent);
                rightTransition();
                finish();
                //}
            }
        });
        captureAadharDetBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openQRCapture();
            }
        });


    }

    private void manualLayoutVisible() {

        genderRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == maleRB.getId()) {
                    manualGenderSelection = "M";
                    aadharAuthItem.setGender(manualGenderSelection);
                } else if (checkedId == maleRB.getId()) {

                    manualGenderSelection = "F";
                    aadharAuthItem.setGender(manualGenderSelection);
                } else if (checkedId == otherRB.getId()) {
                    manualGenderSelection = "O";
                    aadharAuthItem.setGender(manualGenderSelection);
                }

            }

        });
        dateOfBirthRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == yobRB.getId()) {
                    isYobSelected = true;
                    isDobSelected = false;
                    manualYobAsInAadhaarET.setVisibility(View.VISIBLE);
                    manualDobAsInAadhaarET.setVisibility(View.GONE);
                } else if (checkedId == dobRB.getId()) {
                    isYobSelected = false;
                    isDobSelected = true;
                    manualDobAsInAadhaarET.setVisibility(View.VISIBLE);
                    manualYobAsInAadhaarET.setVisibility(View.GONE);
                }

            }

        });
        manualAadharNumberET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                manualAadharNumberET.setTextColor(Color.BLACK);
                isVeroff = false;
                if (charSequence.toString().length() > 11) {
                    if (!Verhoeff.validateVerhoeff(charSequence.toString())) {
                        manualAadharNumberET.setTextColor(Color.RED);
                    } else {
                        isVeroff = true;
                        manualAadharNumberET.setTextColor(Color.GREEN);

                    }
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        if (seccItem.getAadhaarCapturingMode() != null) {
            if (seccItem.getAadhaarCapturingMode().equalsIgnoreCase(AppConstant.MANUAL_MODE) || seccItem.getAadhaarCapturingMode().equalsIgnoreCase("")) {
                if (seccItem.getAadhaarAuth() != null && seccItem.getAadhaarAuth().equalsIgnoreCase("")) {

                } else if (seccItem.getAadhaarAuth() != null && seccItem.getAadhaarAuth().equalsIgnoreCase(AppConstant.PENDING_STATUS)) {
                    pending.setVisibility(View.VISIBLE);
                    if (manualAadharNumberET.getText().toString().equalsIgnoreCase("")) {
                        manualAadharNumberET.setText(seccItem.getAadhaarNo());
                    }
                    if (manualNameAsInAadhaarET.getText().toString().equalsIgnoreCase("")) {
                        manualNameAsInAadhaarET.setText(seccItem.getNameAadhaar());
                    }
                } else if (seccItem.getAadhaarAuth() != null && seccItem.getAadhaarAuth().equalsIgnoreCase(AppConstant.VALID_STATUS)) {
                    verified.setVisibility(View.VISIBLE);
                    if (manualAadharNumberET.getText().toString().equalsIgnoreCase("")) {
                        manualAadharNumberET.setText(seccItem.getAadhaarNo());
                    }
                    if (manualNameAsInAadhaarET.getText().toString().equalsIgnoreCase("")) {
                        manualNameAsInAadhaarET.setText(seccItem.getNameAadhaar());
                    }
                } else if (seccItem.getAadhaarAuth() != null && seccItem.getAadhaarAuth().equalsIgnoreCase(AppConstant.INVALID_STATUS)) {
                    rejected.setVisibility(View.VISIBLE);
                    if (manualAadharNumberET.getText().toString().equalsIgnoreCase("")) {
                        manualAadharNumberET.setText(seccItem.getAadhaarNo());
                    }
                    if (manualNameAsInAadhaarET.getText().toString().equalsIgnoreCase("")) {
                        manualNameAsInAadhaarET.setText(seccItem.getNameAadhaar());
                    }
                }
                if (seccItem.getNameAadhaar() == null || seccItem.getNameAadhaar().equalsIgnoreCase("")) {
                    if (manualNameAsInAadhaarET.getText().toString().equalsIgnoreCase("")) {
                        if (seccItem != null && seccItem.getDataSource() != null && seccItem.getDataSource().equalsIgnoreCase(AppConstant.RSBY_SOURCE)) {
                            manualNameAsInAadhaarET.setText(seccItem.getRsbyName());
                        } else {
                            manualNameAsInAadhaarET.setText(seccItem.getName());
                        }

                    }
                }

                if (seccItem.getAadhaarNo() != null) {
                    if (manualAadharNumberET.getText().toString().equalsIgnoreCase("")) {
                        manualAadharNumberET.setText(seccItem.getAadhaarNo());
                    }
                }
                if (seccItem.getNameAadhaar() != null && !seccItem.getNameAadhaar().equalsIgnoreCase("")) {
                    if (manualNameAsInAadhaarET.getText().toString().equalsIgnoreCase("")) {
                        manualNameAsInAadhaarET.setText(seccItem.getNameAadhaar());
                    }
                }
                if (seccItem.getAadhaarDob() != null && !seccItem.getAadhaarDob().equalsIgnoreCase("")) {
                    if (manualGenderAsInAadhaarET.getText().toString().equalsIgnoreCase("")) {
                        manualGenderAsInAadhaarET.setText(seccItem.getAadhaarDob());
                    }
                }
                if (seccItem.getAadhaarGender() != null && !seccItem.getAadhaarGender().equalsIgnoreCase("")) {
                    if (manualDobAsInAadhaarET.getText().toString().equalsIgnoreCase("")) {
                        manualDobAsInAadhaarET.setText(seccItem.getAadhaarGender());
                    }
                }
            }
        }
        // aadharNumberET.setText("424175531180");
        // nameAsInAadhaarET.setText("Sunil Kumar");
        validateAadhaarBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtility.hideSoftInput(activity, validateAadhaarBT);
                String nameAsInAadhaar = manualNameAsInAadhaarET.getText().toString().trim();
                String aadhaarNumber = manualAadharNumberET.getText().toString().trim();
                String aadharDob = "";
                String aadhaarGender = manualGenderAsInAadhaarET.getText().toString();
                aadharAuthItem.setAadharNo(aadhaarNumber);
                aadharAuthItem.setName(nameAsInAadhaar);
                if (isDobSelected) {
                    aadharAuthItem.setDob(manualDobAsInAadhaarET.getText().toString());
                    aadharDob = manualDobAsInAadhaarET.getText().toString();

                }
                if (isYobSelected) {
                    aadharDob = manualYobAsInAadhaarET.getText().toString();
                    aadharAuthItem.setDob(AppUtility.formatAadharAuth(manualYobAsInAadhaarET.getText().toString()));
                }
                // aadharAuthItem.setDob();
                if (aadhaarNumber.trim().equalsIgnoreCase("")) {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterAadhaar));
                    return;
                }
                if (nameAsInAadhaar.trim().equalsIgnoreCase("")) {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterName));
                    return;
                }

                if (!isVeroff) {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterValidAadhaar));
                    return;
                }
                if (aadharDob.trim().equalsIgnoreCase("")) {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterDob));
                    return;
                }
                if (manualGenderSelection.trim().equalsIgnoreCase("")) {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterGender));
                    return;
                }

                if (!consentCB.isChecked()) {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.adhar_consent_msg));
                    return;
                }
                if (isNetworkAvailable()) {

                    //  requestAadhaarAuth(aadhaarNumber, nameAsInAadhaar);
                    ManualAlertForValidateLater(aadharAuthItem, seccItem, aadhaarNumber, nameAsInAadhaar);
                } else {
                    CustomAlert.alertWithOk(context, getResources().getString(R.string.internet_connection_msg));
                }
            }
        });
        validateLaterBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppUtility.hideSoftInput(activity, validateLaterBT);
                String nameAsInAadhaar1 = manualNameAsInAadhaarET.getText().toString().trim();
                String aadhaarNumber1 = manualAadharNumberET.getText().toString().trim();
                if (aadhaarNumber1.trim().equalsIgnoreCase("")) {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterAadhaar));
                    return;
                }
                if (nameAsInAadhaar1.trim().equalsIgnoreCase("")) {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterName));

                    return;
                }
                if (!isVeroff) {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterValidAadhaar));
                    return;
                }
                if (!consentCB.isChecked()) {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.adhar_consent_msg));
                    return;
                }
                //         SeccMemberItem checkAadhaar = SeccDatabase.seccMemberDetailByAadhaar(aadhaarNumber1.trim(), context);

                /*if(checkAadhaar!=null){
                    if(checkAadhaar.getAadhaarNo()!=null && checkAadhaar.getAadhaarNo().trim().equalsIgnoreCase(seccItem.getAadhaarNo().trim())) {
                    }else{
                        CustomAlert.alertWithOk(context, "Aadhaar Number has been already captured");
                        return;
                    }
                }*/
                /*if(SeccDatabase.seccMemberDetailByAadhaar(aadhaarNumber.trim(),context)!=null){
                    CustomAlert.alertWithOk(context,"Aadhaar Number is already captured");
                    return;
                }*/

               /* if(SeccDatabase.seccMemberDetailByAadhaar(aadhaarNumber.trim(),context)!=null){
                    CustomAlert.alertWithOk(context,"Aadhaar Number is already captured");
                    return;
                }*/
                       /* if (aadhaarNumber1.equalsIgnoreCase(loginResponse.getAadhaarNumber()) && seccItem.getAadhaarAuth().equalsIgnoreCase(AppConstant.VALID_STATUS)) {
                            CustomAlert.alertWithOk(context, "You have entered verifier Aadhaar number, Please member enter member Aadhaar number to proceed.");
                            return;
                        }*/
                seccItem.setAadhaarAuth(AppConstant.PENDING_STATUS);
                alertForValidateLater(nameAsInAadhaar1, seccItem);
            }
        });

        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // if(seccItem.getAadhaarStatus().equalsIgnoreCase("1")) {
                Intent theIntent = new Intent(context, WithAadhaarActivity.class);
                startActivity(theIntent);
                rightTransition();
                finish();
                //}
            }
        });
      /*  captureAadharDetBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openQRCapture();
            }
        });*/


    }

    private void showRsbyDetail(SeccMemberItem item) {
        headerTV.setText(item.getRsbyName());
    }

    private void showSeccDetail(SeccMemberItem item) {
        headerTV.setText(item.getName());
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
        finish();
        rightTransition();

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
}
