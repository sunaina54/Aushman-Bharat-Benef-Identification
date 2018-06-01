package com.nhpm.rsbyFieldValidation;

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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
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
import com.nhpm.Utility.ApplicationGlobal;
import com.nhpm.activity.BaseActivity;
import com.nhpm.CameraUtils.barcode.BarcodeCaptureActivity;
import com.nhpm.LocalDataBase.dto.SeccDatabase;
import com.nhpm.Models.request.AadhaarAuthRequestItem;
import com.nhpm.Models.response.AadhaarCaptureDetailItem;
import com.nhpm.Models.response.AadhaarGenderItem;
import com.nhpm.Models.response.AadhaarStatusItem;
import com.nhpm.Models.response.GovernmentIdItem;
import com.nhpm.Models.response.rsbyMembers.RSBYItem;
import com.nhpm.Models.response.seccMembers.SelectedMemberItem;
import com.nhpm.Models.response.verifier.DemoAuthResponseItem;
import com.nhpm.Models.response.verifier.VerifierLoginResponse;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;
import com.nhpm.Utility.AppUtility;
import com.nhpm.Utility.Verhoeff;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import pl.polidea.view.ZoomView;

/**
 * Created by DELL on 26-02-2017.
 */

public class RsbyAadharCaptureActivity extends BaseActivity {
    private static final int BARCODE_READER_REQUEST_CODE = 1;
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
    private int QR_REQUEST_CODE = 1;
    private AadhaarCaptureDetailItem aadhaarItem;
    private static final String TAG = "Capture Activity";
    private RSBYItem rsbyItem;
    private int memberType;
    private RadioGroup consentGroup;
    private int CONSENT_PHOTO_REQUEST = 2;
    private Bitmap consentBitmap;
    private boolean isConsent;
    private String consent = "Y";
    private Button validateAadhaarBT, validateLaterBT;
    private SelectedMemberItem selectedMemItem;
    private VerifierLoginResponse loginResponse;
    private ImageView verified, rejected, pending, qrCodeAadhaarPendingIV, qrCodeAadhaarRejectedIV, qrCodeAadhaarVerifiedIV;
    private AlertDialog internetDiaolg;
    private CheckBox consentCB;
    private boolean isVeroff;
    private LinearLayout mZoomLinearLayout;
    private ZoomView zoomView;
    private RadioGroup radioGroup;
    private RadioButton radioButtonQrcode, radioButtonManual;
    private LinearLayout qrCodeAadhaarLayout, manualAadhaarLayout;
    private EditText qrCodeAadharNumberET, qrCodeNameAsInAadhaarET, qrCodeDobAsInAadhaarET,
            qrCodeGenderAsInAadhaarET, manualAadharNumberET, manualNameAsInAadhaarET;
    private boolean qrCodeLayoutEnable = true;
    private TaskListener mTaskListener;
    private CustomAsyncTask mAsycncTask;
    private String response;

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
        mZoomLinearLayout = (LinearLayout) findViewById(R.id.mZoomLinearLayout);
        View v = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.activity_capture_aadhaar, null, false);
        v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
        //memberType=getIntent().getIntExtra(AppConstant.MEMBER_TYPE, -1);
        selectedMemItem = SelectedMemberItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.SELECTED_ITEM_FOR_VERIFICATION, context));
        loginResponse = (VerifierLoginResponse.create(ProjectPrefrence.
                getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.VERIFIER_CONTENT, context)));
        consentCB = (CheckBox) v.findViewById(R.id.consentCheck);
        validateAadhaarBT = (Button) v.findViewById(R.id.validateAdhaarBT);
        validateLaterBT = (Button) v.findViewById(R.id.validateAdhaarLaterBT);
        captureAadharDetBT = (Button) v.findViewById(R.id.captureAadharDetBT);
        dobTV = (TextView) v.findViewById(R.id.dob);
        consentGroup = (RadioGroup) v.findViewById(R.id.radioGroup);
        verified = (ImageView) v.findViewById(R.id.aadhaarVerifiedIV);
        rejected = (ImageView) v.findViewById(R.id.aadhaarRejectedIV);
        pending = (ImageView) v.findViewById(R.id.aadhaarPendingIV);
        qrCodeAadhaarPendingIV = (ImageView) v.findViewById(R.id.qrCodeAadhaarPendingIV);
        qrCodeAadhaarRejectedIV = (ImageView) v.findViewById(R.id.qrCodeAadhaarRejectedIV);
        qrCodeAadhaarVerifiedIV = (ImageView) v.findViewById(R.id.qrCodeAadhaarVerifiedIV);
        headerTV = (TextView) v.findViewById(R.id.centertext);
        backIV = (ImageView) v.findViewById(R.id.back);
        if (selectedMemItem.getRsbyMemberItem() != null) {
            rsbyItem = selectedMemItem.getRsbyMemberItem();
            headerTV.setText(rsbyItem.getName());
            //   AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"AAdhaar Name : "+rsbyItem.getNameAadhaar());
            Log.d(TAG, "Secc Member Detail : " + rsbyItem.serialize());
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

        if (rsbyItem.getAadhaarCapturingMode() != null && !rsbyItem.getAadhaarCapturingMode().equalsIgnoreCase("")) {
            if (rsbyItem.getAadhaarCapturingMode().equalsIgnoreCase(AppConstant.MANUAL_MODE)) {
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

    private void ManualAlertForValidateLater(RSBYItem item, final String aadhaarNumber, final String aadhaarName) {
        internetDiaolg = new AlertDialog.Builder(context).create();

        final LayoutInflater factory = LayoutInflater.from(context);
        View alertView = factory.inflate(R.layout.aadhar_validation_msg_popup, null);
        internetDiaolg.setView(alertView);
        internetDiaolg.show();
        TextView nameAsInAadharTV = (TextView) alertView.findViewById(R.id.nameAsInAadharTV);
        TextView nameAsInSeccTV = (TextView) alertView.findViewById(R.id.nameAsInSeccTV);
        TextView nameasrsby = (TextView) alertView.findViewById(R.id.nameasrsby);
        nameasrsby.setText(context.getResources().getString(R.string.name_as_rsby));
        nameAsInAadharTV.setText(aadhaarName);
        nameAsInSeccTV.setText(item.getName());
        Button tryAgainBT = (Button) alertView.findViewById(R.id.tryAgainBT);
        tryAgainBT.setText("Confirm");
        Button cancelBT = (Button) alertView.findViewById(R.id.cancelBT);
        tryAgainBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rsbyItem.setAadhaarAuth(AppConstant.PENDING_STATUS);
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

    private void requestAadhaarAuth(final String aadhaarNo, final String nameAsInAadhaar) {


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
                    rsbyItem.setAadhaarAuthMode(AppConstant.DEMO_AUTH);
                    if (demoAuthItem != null && demoAuthItem.getRet() != null) {
                        rsbyItem.setAadhaarAuth(demoAuthItem.getRet().trim());
                        if (demoAuthItem != null && demoAuthItem.getRet() != null && demoAuthItem.getRet().equalsIgnoreCase("Y")) {
                            verified.setVisibility(View.VISIBLE);
                            // AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Auth response " + response.toString());
                            rsbyItem.setAadhaarAuth(AppConstant.VALID_STATUS);
                            rsbyItem.setAadhaarAuthDt(String.valueOf(DateTimeUtil.currentTimeMillis()));
                            if (qrCodeLayoutEnable) {
                                qrCodeSubmitAaadhaarDetail();
                            } else {
                                manualSubmitAaadhaarDetail();
                            }
                        } else if (demoAuthItem.getRet().equalsIgnoreCase("N")) {
                            //AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Auth response " + response.toString());
                            rejected.setVisibility(View.VISIBLE);
                            CustomAlert.alertWithOk(context, "Please enter correct Aadhaar Number and Name as in Aadhaar");
                            // rsbyItem.setAadhaarAuth(AppConstant.INVALID_STATUS);
                        } else {
                            rsbyItem.setAadhaarAuth(AppConstant.PENDING_STATUS);
                            pending.setVisibility(View.VISIBLE);
                        }
                    } else {
                        CustomAlert.alertWithOk(context, "Unable To Connect from UIDAI Server, Please try again.");
                    }
                } else {
                    CustomAlert.alertWithOk(context, "Connection terminated, please Retry");
                }
            }
        };
        ProgressDialog mProgress = new ProgressDialog(context);
        mProgress.setIndeterminateDrawable(context.getResources().getDrawable(R.mipmap.nhps_logo));
        mProgress.setMessage("Please wait..");
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
        qrCodeNameAsInAadhaarET.setText(item.getName());
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

    private void alertForValidateLater(String aadharName, RSBYItem item) {
        internetDiaolg = new AlertDialog.Builder(context).create();
        LayoutInflater factory = LayoutInflater.from(context);
        View alertView = factory.inflate(R.layout.aadhar_validation_msg_popup, null);
        internetDiaolg.setView(alertView);
        internetDiaolg.show();

        TextView nameAsInAadharTV = (TextView) alertView.findViewById(R.id.nameAsInAadharTV);
        TextView nameAsInSeccTV = (TextView) alertView.findViewById(R.id.nameAsInSeccTV);
        TextView nameasrsby = (TextView) alertView.findViewById(R.id.nameasrsby);
        nameasrsby.setText(context.getResources().getString(R.string.name_as_rsby));
        nameAsInAadharTV.setText(aadharName);
        nameAsInSeccTV.setText(item.getName());
        Button tryAgainBT = (Button) alertView.findViewById(R.id.tryAgainBT);
        tryAgainBT.setText("Confirm");
        final Button cancelBT = (Button) alertView.findViewById(R.id.cancelBT);
        tryAgainBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                rsbyItem.setAadhaarAuth(AppConstant.PENDING_STATUS);
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

    private void qrCodeAlertForValidateLater(RSBYItem item, final String aadhaarNumber, final String aadhaarName) {
        internetDiaolg = new AlertDialog.Builder(context).create();

        final LayoutInflater factory = LayoutInflater.from(context);
        View alertView = factory.inflate(R.layout.aadhar_validation_msg_popup, null);
        internetDiaolg.setView(alertView);
        internetDiaolg.show();
        TextView nameAsInAadharTV = (TextView) alertView.findViewById(R.id.nameAsInAadharTV);
        TextView nameAsInSeccTV = (TextView) alertView.findViewById(R.id.nameAsInSeccTV);
        TextView nameasrsby = (TextView) alertView.findViewById(R.id.nameasrsby);
        nameasrsby.setText(context.getResources().getString(R.string.name_as_rsby));
        nameAsInAadharTV.setText(aadhaarName);
        nameAsInSeccTV.setText(item.getName());
        Button tryAgainBT = (Button) alertView.findViewById(R.id.tryAgainBT);
        tryAgainBT.setText("Confirm");
        Button cancelBT = (Button) alertView.findViewById(R.id.cancelBT);
        tryAgainBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rsbyItem.setAadhaarAuth(AppConstant.PENDING_STATUS);
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

        rsbyItem.setAadhaarVerifiedBy(loginResponse.getAadhaarNumber());
        if (selectedMemItem.getOldHeadrsbyMemberItem() != null && selectedMemItem.getNewHeadrsbyMemberItem() != null) {
            RSBYItem oldHead = selectedMemItem.getOldHeadrsbyMemberItem();
            oldHead.setLockedSave(AppConstant.LOCKED + "");
            AppUtility.showLog(AppConstant.LOG_STATUS, TAG, " OLD Household name " +
                    ": " + oldHead.getName() + "" +
                    " Member Status " + oldHead.getMemStatus() + " House hold Status :" +
                    " " + oldHead.getHhStatus() + " Locked Save :" + oldHead.getLockedSave());
            SeccDatabase.updateRsbyMember(selectedMemItem.getOldHeadrsbyMemberItem(), context);
        }


        if (aadhaarItem != null) {
            rsbyItem.setAadhaarCapturingMode(AppConstant.QR_CODE_MODE);
            rsbyItem.setAadhaarNo(aadhaarItem.getUid());
            rsbyItem.setAadhaarLm(aadhaarItem.getIm());
            rsbyItem.setAadhaarPc(aadhaarItem.getPc());
            rsbyItem.setNameAadhaar(aadhaarItem.getName());
            rsbyItem.setAadhaarCo(aadhaarItem.getCo());
            rsbyItem.setAadhaarDist(aadhaarItem.getDist());
            rsbyItem.setAadhaarDob(aadhaarItem.getDob());
            rsbyItem.setAadhaarSubdist(aadhaarItem.getSubDist());
            rsbyItem.setAadhaarGender(aadhaarItem.getGender());
            rsbyItem.setAadhaarHouse(aadhaarItem.getHouse());
            rsbyItem.setAadhaarLoc(aadhaarItem.getLoc());
            rsbyItem.setAadhaarPo(aadhaarItem.getPo());
            rsbyItem.setAadhaarState(aadhaarItem.getState());
            rsbyItem.setAadhaarStreet(aadhaarItem.getStreet());
            rsbyItem.setAadhaarVtc(aadhaarItem.getVtc());
            rsbyItem.setAadhaarYob(aadhaarItem.getYob());
            rsbyItem.setConsent(consent);
            rsbyItem.setAadhaarSurveyedStat(AppConstant.SURVEYED + "");
            if (rsbyItem.getAadhaarStatus().equalsIgnoreCase("1")) {
                Intent theIntent = new Intent(context, RsbyValidationWithAadharActivity.class);
                rsbyItem.setLockedSave(AppConstant.SAVE + "");
                SeccDatabase.updateRsbyMember(rsbyItem, context);
                SeccDatabase.updateRSBYHouseHold(selectedMemItem.getRsbyHouseholdItem(), context);
                rsbyItem = SeccDatabase.getRsbyMemberDetail(rsbyItem.getRsbyMemId(), context);
                if (rsbyItem.getAadhaarAuth() == null || rsbyItem.getAadhaarAuth().equalsIgnoreCase("")) {
                    rsbyItem.setAadhaarAuth(AppConstant.PENDING_STATUS);
                }
                AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Captured Aadhaar QRCode : " + rsbyItem.serialize());
                selectedMemItem.setRsbyMemberItem(rsbyItem);
                ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                        AppConstant.SELECTED_ITEM_FOR_VERIFICATION, selectedMemItem.serialize(), context);
                startActivity(theIntent);
                finish();
                rightTransition();
                if (rsbyItem.getAadhaarAuth().equalsIgnoreCase(AppConstant.PENDING_STATUS)) {
                    //statrtValidateService(rsbyItem);
                }
            }
        } else if (!qrCodeAadharNumberET.getText().toString().equalsIgnoreCase("") &&
                !qrCodeNameAsInAadhaarET.getText().toString().equalsIgnoreCase("")) {

            rsbyItem.setAadhaarNo(qrCodeAadharNumberET.getText().toString().trim());
            rsbyItem.setNameAadhaar(qrCodeNameAsInAadhaarET.getText().toString().trim());
            /*if (rsbyItem.getAadhaarCapturingMode() == null || rsbyItem.getAadhaarCapturingMode().equalsIgnoreCase("")) {
                rsbyItem.setAadhaarCapturingMode(AppConstant.MANUAL);
            }*/
            rsbyItem.setAadhaarSurveyedStat(AppConstant.SURVEYED + "");
            rsbyItem.setConsent(consent);
            // if (rsbyItem.getAadhaarStatus().equalsIgnoreCase("1")) {
            AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Captured Aadhaar : " + rsbyItem.serialize());
            Intent theIntent = new Intent(context, RsbyValidationWithAadharActivity.class);
            rsbyItem.setLockedSave(AppConstant.SAVE + "");
            SeccDatabase.updateRsbyMember(rsbyItem, context);
            SeccDatabase.updateRSBYHouseHold(selectedMemItem.getRsbyHouseholdItem(), context);
            rsbyItem = SeccDatabase.getRsbyMemberDetail(rsbyItem.getRsbyMemId(), context);
            selectedMemItem.setRsbyMemberItem(rsbyItem);
            ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                    AppConstant.SELECTED_ITEM_FOR_VERIFICATION, selectedMemItem.serialize(), context);
            startActivity(theIntent);
            finish();
            rightTransition();
            // }
        } else {
            CustomAlert.alertWithOk(context, "Please Capture Aadhaar details");
        }
    }

    private void manualSubmitAaadhaarDetail() {
        rsbyItem.setAadhaarVerifiedBy(loginResponse.getAadhaarNumber());
        if (selectedMemItem.getOldHeadrsbyMemberItem() != null && selectedMemItem.getNewHeadrsbyMemberItem() != null) {
            RSBYItem oldHead = selectedMemItem.getRsbyMemberItem();
            oldHead.setLockedSave(AppConstant.LOCKED + "");
            AppUtility.showLog(AppConstant.LOG_STATUS, TAG, " OLD Household name " +
                    ": " + oldHead.getName() + "" +
                    " Member Status " + oldHead.getMemStatus() + " House hold Status :" +
                    " " + oldHead.getHhStatus() + " Locked Save :" + oldHead.getLockedSave());
            SeccDatabase.updateRsbyMember(selectedMemItem.getOldHeadrsbyMemberItem(), context);
        }
        if (!manualAadharNumberET.getText().toString().equalsIgnoreCase("") &&
                !manualNameAsInAadhaarET.getText().toString().equalsIgnoreCase("")) {

            rsbyItem.setAadhaarNo(manualAadharNumberET.getText().toString().trim());
            rsbyItem.setNameAadhaar(manualNameAsInAadhaarET.getText().toString().trim());
            rsbyItem.setAadhaarCapturingMode(AppConstant.MANUAL_MODE);
            rsbyItem.setAadhaarLm("");
            rsbyItem.setAadhaarPc("");
            rsbyItem.setAadhaarCo("");
            rsbyItem.setAadhaarDist("");
            rsbyItem.setAadhaarDob("");
            rsbyItem.setAadhaarSubdist("");
            rsbyItem.setAadhaarGender("");
            rsbyItem.setAadhaarHouse("");
            rsbyItem.setAadhaarLoc("");
            rsbyItem.setAadhaarPo("");
            rsbyItem.setAadhaarState("");
            rsbyItem.setAadhaarStreet("");
            rsbyItem.setAadhaarVtc("");
            rsbyItem.setAadhaarYob("");
            rsbyItem.setConsent(consent);
            if (rsbyItem.getAadhaarCapturingMode() == null || rsbyItem.getAadhaarCapturingMode().equalsIgnoreCase("")) {
                rsbyItem.setAadhaarCapturingMode(AppConstant.MANUAL_MODE);
            }
            rsbyItem.setAadhaarSurveyedStat(AppConstant.SURVEYED + "");
            rsbyItem.setConsent(consent);
            if (rsbyItem.getAadhaarStatus().equalsIgnoreCase("1")) {
                AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Captured Aadhaar : " + rsbyItem.serialize());
                Intent theIntent = new Intent(context, RsbyValidationWithAadharActivity.class);
                rsbyItem.setLockedSave(AppConstant.SAVE + "");
                SeccDatabase.updateRsbyMember(rsbyItem, context);
                SeccDatabase.updateRSBYHouseHold(selectedMemItem.getRsbyHouseholdItem(), context);
                rsbyItem = SeccDatabase.getRsbyMemberDetail(rsbyItem.getRsbyMemId(), context);
                selectedMemItem.setRsbyMemberItem(rsbyItem);
                ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                        AppConstant.SELECTED_ITEM_FOR_VERIFICATION, selectedMemItem.serialize(), context);
                startActivity(theIntent);
                finish();
                rightTransition();
                if (rsbyItem.getAadhaarAuth().equalsIgnoreCase(AppConstant.PENDING_STATUS)) {
                    //statrtValidateService(rsbyItem);
                }
            }
        } else {
            CustomAlert.alertWithOk(context, "Please Capture Aadhaar details");
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


            if (rsbyItem.getAadhaarCapturingMode() != null) {
                if (rsbyItem.getAadhaarCapturingMode().equalsIgnoreCase(AppConstant.QR_CODE_MODE) || rsbyItem.getAadhaarCapturingMode().equalsIgnoreCase("")) {
                    if (rsbyItem.getAadhaarAuth() != null && rsbyItem.getAadhaarAuth().equalsIgnoreCase("")) {

                    } else if (rsbyItem.getAadhaarAuth() != null && rsbyItem.getAadhaarAuth().equalsIgnoreCase(AppConstant.PENDING_STATUS)) {
                        qrCodeAadhaarPendingIV.setVisibility(View.VISIBLE);
                        if (qrCodeAadharNumberET.getText().toString().equalsIgnoreCase("")) {
                            qrCodeAadharNumberET.setText(rsbyItem.getAadhaarNo());
                        }
                        if (qrCodeNameAsInAadhaarET.getText().toString().equalsIgnoreCase("")) {
                            qrCodeNameAsInAadhaarET.setText(rsbyItem.getNameAadhaar());
                        }
                        if (qrCodeDobAsInAadhaarET.getText().toString().equalsIgnoreCase("")) {
                            if (rsbyItem.getAadhaarDob() != null && !rsbyItem.getAadhaarDob().equalsIgnoreCase("")) {
                                qrCodeDobAsInAadhaarET.setText(rsbyItem.getAadhaarDob());
                            } else if (rsbyItem.getAadhaarYob() != null && !rsbyItem.getAadhaarYob().equalsIgnoreCase("")) {
                                qrCodeDobAsInAadhaarET.setText(rsbyItem.getAadhaarYob());
                            }
                        }
                        if (qrCodeGenderAsInAadhaarET.getText().toString().equalsIgnoreCase("")) {
                            qrCodeGenderAsInAadhaarET.setText(rsbyItem.getAadhaarGender());
                        }
                    } else if (rsbyItem.getAadhaarAuth() != null && rsbyItem.getAadhaarAuth().equalsIgnoreCase(AppConstant.VALID_STATUS)) {
                        qrCodeAadhaarVerifiedIV.setVisibility(View.VISIBLE);

                        if (qrCodeAadharNumberET.getText().toString().equalsIgnoreCase("")) {
                            qrCodeAadharNumberET.setText(rsbyItem.getAadhaarNo());
                        }
                        if (qrCodeNameAsInAadhaarET.getText().toString().equalsIgnoreCase("")) {
                            qrCodeNameAsInAadhaarET.setText(rsbyItem.getNameAadhaar());
                        }
                        if (qrCodeDobAsInAadhaarET.getText().toString().equalsIgnoreCase("")) {
                            if (rsbyItem.getAadhaarDob() != null && !rsbyItem.getAadhaarDob().equalsIgnoreCase("")) {
                                qrCodeDobAsInAadhaarET.setText(rsbyItem.getAadhaarDob());
                            } else if (rsbyItem.getAadhaarYob() != null && !rsbyItem.getAadhaarYob().equalsIgnoreCase("")) {
                                qrCodeDobAsInAadhaarET.setText(rsbyItem.getAadhaarYob());
                            }
                        }
                        if (qrCodeGenderAsInAadhaarET.getText().toString().equalsIgnoreCase("")) {
                            qrCodeGenderAsInAadhaarET.setText(rsbyItem.getAadhaarGender());
                        }
                    } else if (rsbyItem.getAadhaarAuth() != null && rsbyItem.getAadhaarAuth().equalsIgnoreCase(AppConstant.INVALID_STATUS)) {
                        qrCodeAadhaarRejectedIV.setVisibility(View.VISIBLE);
                        if (qrCodeAadharNumberET.getText().toString().equalsIgnoreCase("")) {
                            qrCodeAadharNumberET.setText(rsbyItem.getAadhaarNo());
                        }
                        if (qrCodeNameAsInAadhaarET.getText().toString().equalsIgnoreCase("")) {
                            qrCodeNameAsInAadhaarET.setText(rsbyItem.getNameAadhaar());
                        }
                        if (qrCodeDobAsInAadhaarET.getText().toString().equalsIgnoreCase("")) {
                            if (rsbyItem.getAadhaarDob() != null && !rsbyItem.getAadhaarDob().equalsIgnoreCase("")) {
                                qrCodeDobAsInAadhaarET.setText(rsbyItem.getAadhaarDob());
                            } else if (rsbyItem.getAadhaarYob() != null && !rsbyItem.getAadhaarYob().equalsIgnoreCase("")) {
                                qrCodeDobAsInAadhaarET.setText(rsbyItem.getAadhaarYob());
                            }
                        }
                        if (qrCodeGenderAsInAadhaarET.getText().toString().equalsIgnoreCase("")) {
                            qrCodeGenderAsInAadhaarET.setText(rsbyItem.getAadhaarGender());
                        }
                    }
                    if (rsbyItem.getNameAadhaar() == null || rsbyItem.getNameAadhaar().equalsIgnoreCase("")) {
                        if (qrCodeNameAsInAadhaarET.getText().toString().equalsIgnoreCase("")) {
                            qrCodeNameAsInAadhaarET.setText(rsbyItem.getName());
                        }

                    }
                    //       submitBt=(Button)findViewById(R.id.submitBT);

/*
                if (rsbyItem.getAadhaarNo() != null) {

                    if (qrCodeAadharNumberET.getText().toString().equalsIgnoreCase("")) {
                        qrCodeAadharNumberET.setText(rsbyItem.getAadhaarNo());
                    }
                    if (qrCodeDobAsInAadhaarET.getText().toString().equalsIgnoreCase("")) {
                        if(rsbyItem.getAadhaarDob()!=null && !rsbyItem.getAadhaarDob().equalsIgnoreCase("")) {
                            qrCodeDobAsInAadhaarET.setText(rsbyItem.getAadhaarDob());
                        }else if(rsbyItem.getAadhaarYob()!=null && !rsbyItem.getAadhaarYob().equalsIgnoreCase("")){
                            qrCodeDobAsInAadhaarET.setText(rsbyItem.getAadhaarYob());
                        }
                    }
                    if (qrCodeGenderAsInAadhaarET.getText().toString().equalsIgnoreCase("")) {
                        qrCodeGenderAsInAadhaarET.setText(rsbyItem.getAadhaarGender());
                    }
                }
                if (rsbyItem.getNameAadhaar() != null && !rsbyItem.getNameAadhaar().equalsIgnoreCase("")) {

                    if (qrCodeNameAsInAadhaarET.getText().toString().equalsIgnoreCase("")) {
                        qrCodeNameAsInAadhaarET.setText(rsbyItem.getNameAadhaar());
                    }
                    if (qrCodeDobAsInAadhaarET.getText().toString().equalsIgnoreCase("")) {
                        if(rsbyItem.getAadhaarDob()!=null && !rsbyItem.getAadhaarDob().equalsIgnoreCase("")) {
                            qrCodeDobAsInAadhaarET.setText(rsbyItem.getAadhaarDob());
                        }else if(rsbyItem.getAadhaarYob()!=null && !rsbyItem.getAadhaarYob().equalsIgnoreCase("")){
                            qrCodeDobAsInAadhaarET.setText(rsbyItem.getAadhaarYob());
                        }
                    }
                    if (qrCodeGenderAsInAadhaarET.getText().toString().equalsIgnoreCase("")) {
                        qrCodeGenderAsInAadhaarET.setText(rsbyItem.getAadhaarGender());
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
                    CustomAlert.alertWithOk(context, "Please enter Aadhaar Number");
                    return;
                }
                if (nameAsInAadhaar2.trim().equalsIgnoreCase("")) {
                    CustomAlert.alertWithOk(context, "Please enter Name as in Aadhaar");
                    return;
                }
                if (!isVeroff) {
                    CustomAlert.alertWithOk(context, "Please enter valid Aadhaar Number");
                    return;
                }
                if (!consentCB.isChecked()) {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.adhar_consent_msg));
                    return;
                }
                /*SeccMemberItem checkAadhaar=SeccDatabase.seccMemberDetailByAadhaar(aadhaarNumber.trim(),context);
              //  AppUtility.showLog(AppConstant.LOG_STATUS,TAG," Aaadhaar Number : "+checkAadhaar.getAadhaarNo());
                if(checkAadhaar!=null){
                    if(checkAadhaar.getAadhaarNo()!=null && checkAadhaar.getAadhaarNo().trim().equalsIgnoreCase(rsbyItem.getAadhaarNo().trim())) {
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

                qrCodeAlertForValidateLater(rsbyItem, aadhaarNumber2, nameAsInAadhaar2);


            }
        });
        validateLaterBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppUtility.hideSoftInput(activity, validateLaterBT);
                String nameAsInAadhaar = qrCodeNameAsInAadhaarET.getText().toString().trim();
                String aadhaarNumber = qrCodeAadharNumberET.getText().toString().trim();
                if (aadhaarNumber.trim().equalsIgnoreCase("")) {
                    CustomAlert.alertWithOk(context, "Please enter Aadhaar Number");
                    return;
                }
                if (nameAsInAadhaar.trim().equalsIgnoreCase("")) {
                    CustomAlert.alertWithOk(context, "Please enter Name as in Aadhaar");
                    return;
                }
                if (!isVeroff) {
                    CustomAlert.alertWithOk(context, "Please enter valid Aadhaar Number");
                    return;
                }
                if (!consentCB.isChecked()) {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.adhar_consent_msg));
                    return;
                }
                //  SeccMemberItem checkAadhaar=SeccDatabase.seccMemberDetailByAadhaar(aadhaarNumber.trim(),context);

                /*if(aadhaarNumber.equalsIgnoreCase(loginResponse.getAadhaarNumber())&& rsbyItem.getAadhaarAuth().equalsIgnoreCase(AppConstant.VALID_STATUS)) {
                    CustomAlert.alertWithOk(context,"You have entered verifier Aadhaar number, Please member enter member Aadhaar number to proceed.");
                    return;
                }*/
                rsbyItem.setAadhaarAuth(AppConstant.PENDING_STATUS);
                StringBuilder alertMessage = new StringBuilder();
                alertMessage.append(context.getResources().getString(R.string.name_as_sec) + rsbyItem.getName() +
                        "\n" + context.getResources().getString(R.string.name_as_aadhar) + nameAsInAadhaar + "\n\n" + context.getResources().getString(R.string.aadhar_confirmation_msg));
                alertForValidateLater(nameAsInAadhaar, rsbyItem);
            }
        });


        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // if(rsbyItem.getAadhaarStatus().equalsIgnoreCase("1")) {
                Intent theIntent = new Intent(context, RsbyValidationWithAadharActivity.class);
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
        if (rsbyItem.getAadhaarCapturingMode() != null) {
            if (rsbyItem.getAadhaarCapturingMode().equalsIgnoreCase(AppConstant.MANUAL_MODE) || rsbyItem.getAadhaarCapturingMode().equalsIgnoreCase("")) {
                if (rsbyItem.getAadhaarAuth() != null && rsbyItem.getAadhaarAuth().equalsIgnoreCase("")) {

                } else if (rsbyItem.getAadhaarAuth() != null && rsbyItem.getAadhaarAuth().equalsIgnoreCase(AppConstant.PENDING_STATUS)) {
                    pending.setVisibility(View.VISIBLE);
                    if (manualAadharNumberET.getText().toString().equalsIgnoreCase("")) {
                        manualAadharNumberET.setText(rsbyItem.getAadhaarNo());
                    }
                    if (manualNameAsInAadhaarET.getText().toString().equalsIgnoreCase("")) {
                        manualNameAsInAadhaarET.setText(rsbyItem.getNameAadhaar());
                    }
                } else if (rsbyItem.getAadhaarAuth() != null && rsbyItem.getAadhaarAuth().equalsIgnoreCase(AppConstant.VALID_STATUS)) {
                    verified.setVisibility(View.VISIBLE);
                    if (manualAadharNumberET.getText().toString().equalsIgnoreCase("")) {
                        manualAadharNumberET.setText(rsbyItem.getAadhaarNo());
                    }
                    if (manualNameAsInAadhaarET.getText().toString().equalsIgnoreCase("")) {
                        manualNameAsInAadhaarET.setText(rsbyItem.getNameAadhaar());
                    }
                } else if (rsbyItem.getAadhaarAuth() != null && rsbyItem.getAadhaarAuth().equalsIgnoreCase(AppConstant.INVALID_STATUS)) {
                    rejected.setVisibility(View.VISIBLE);
                    if (manualAadharNumberET.getText().toString().equalsIgnoreCase("")) {
                        manualAadharNumberET.setText(rsbyItem.getAadhaarNo());
                    }
                    if (manualNameAsInAadhaarET.getText().toString().equalsIgnoreCase("")) {
                        manualNameAsInAadhaarET.setText(rsbyItem.getNameAadhaar());
                    }
                }
                if (rsbyItem.getNameAadhaar() == null || rsbyItem.getNameAadhaar().equalsIgnoreCase("")) {
                    if (manualNameAsInAadhaarET.getText().toString().equalsIgnoreCase("")) {
                        manualNameAsInAadhaarET.setText(rsbyItem.getName());
                    }
                }

                if (rsbyItem.getAadhaarNo() != null) {
                    if (manualAadharNumberET.getText().toString().equalsIgnoreCase("")) {
                        manualAadharNumberET.setText(rsbyItem.getAadhaarNo());
                    }
                }
                if (rsbyItem.getNameAadhaar() != null && !rsbyItem.getNameAadhaar().equalsIgnoreCase("")) {
                    if (manualNameAsInAadhaarET.getText().toString().equalsIgnoreCase("")) {
                        manualNameAsInAadhaarET.setText(rsbyItem.getNameAadhaar());
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
                if (aadhaarNumber.trim().equalsIgnoreCase("")) {
                    CustomAlert.alertWithOk(context, "Please enter Aadhaar Number");
                    return;
                }
                if (nameAsInAadhaar.trim().equalsIgnoreCase("")) {
                    CustomAlert.alertWithOk(context, "Please enter Name as in Aadhaar");
                    return;
                }
                if (!isVeroff) {
                    CustomAlert.alertWithOk(context, "Please enter valid Aadhaar Number");
                    return;
                }
                if (!consentCB.isChecked()) {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.adhar_consent_msg));
                    return;
                }
                if (isNetworkAvailable()) {

                    //  requestAadhaarAuth(aadhaarNumber, nameAsInAadhaar);
                    ManualAlertForValidateLater(rsbyItem, aadhaarNumber, nameAsInAadhaar);
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
                    CustomAlert.alertWithOk(context, "Please enter Aadhaar Number");
                    return;
                }
                if (nameAsInAadhaar1.trim().equalsIgnoreCase("")) {
                    CustomAlert.alertWithOk(context, "Please enter Name as in Aadhaar");
                    return;
                }
                if (!isVeroff) {
                    CustomAlert.alertWithOk(context, "Please enter valid Aadhaar Number");
                    return;
                }
                if (!consentCB.isChecked()) {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.adhar_consent_msg));
                    return;
                }
                //         SeccMemberItem checkAadhaar = SeccDatabase.seccMemberDetailByAadhaar(aadhaarNumber1.trim(), context);

                /*if(checkAadhaar!=null){
                    if(checkAadhaar.getAadhaarNo()!=null && checkAadhaar.getAadhaarNo().trim().equalsIgnoreCase(rsbyItem.getAadhaarNo().trim())) {
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
                       /* if (aadhaarNumber1.equalsIgnoreCase(loginResponse.getAadhaarNumber()) && rsbyItem.getAadhaarAuth().equalsIgnoreCase(AppConstant.VALID_STATUS)) {
                            CustomAlert.alertWithOk(context, "You have entered verifier Aadhaar number, Please member enter member Aadhaar number to proceed.");
                            return;
                        }*/
                rsbyItem.setAadhaarAuth(AppConstant.PENDING_STATUS);
                alertForValidateLater(nameAsInAadhaar1, rsbyItem);
            }
        });

        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // if(rsbyItem.getAadhaarStatus().equalsIgnoreCase("1")) {
                Intent theIntent = new Intent(context, RsbyValidationWithAadharActivity.class);
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

}
