package com.nhpm.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.customComponent.CustomAlert;
import com.customComponent.utility.CustomHttp;
import com.customComponent.utility.DateTimeUtil;
import com.customComponent.utility.ProjectPrefrence;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.nhpm.AadhaarUtils.Global;
import com.nhpm.CameraUtils.barcode.BarcodeCaptureActivity;
import com.nhpm.LocalDataBase.dto.SeccDatabase;
import com.nhpm.Models.AadharAuthItem;
import com.nhpm.Models.request.LogRequestItem;
import com.nhpm.Models.response.AadhaarCaptureDetailItem;
import com.nhpm.Models.response.seccMembers.SeccMemberItem;
import com.nhpm.Models.response.seccMembers.SelectedMemberItem;
import com.nhpm.Models.response.verifier.AadhaarDemoAuthResponse;
import com.nhpm.Models.response.verifier.AadhaarResponseItem;
import com.nhpm.Models.response.verifier.VerifierLoginResponse;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;
import com.nhpm.Utility.AppUtility;
import com.nhpm.Utility.ApplicationGlobal;
import com.nhpm.Utility.Verhoeff;
import com.nhpm.activity.CaptureAadharDetailActivity;
import com.nhpm.activity.DemoAuthActivity;
import com.nhpm.activity.FindBeneficiaryByNameActivity;
import com.nhpm.activity.FingerprintResultActivity;
import com.nhpm.activity.WithAadhaarActivity;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;

import fr.arnaudguyon.xmltojsonlib.XmlToJson;


public class FindBeneficiaryByQrCode extends Fragment {
    private static final String TAG = "Capture Activity";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private AadhaarDemoAuthResponse demoAuthResp;
    private Context context;
    private CaptureAadharDetailActivity activity;
    //private DemoAuthActivity demoAuthActivity;
    private FindBeneficiaryByNameActivity demoAuthActivity;
    private long startTime;
    private AlertDialog alert=null ;
    private String pidXml;
    private EditText qrCodeAadharNumberET, qrCodeNameAsInAadhaarET, qrCodeDobAsInAadhaarET,
            qrCodeGenderAsInAadhaarET,qrCodePincodeET;
    private static final int BARCODE_READER_REQUEST_CODE = 1;
    private int QR_REQUEST_CODE = 1;
    private AadhaarCaptureDetailItem aadhaarItem;
    private String consent = "Y";
    private Button validateAadhaarBT, validateLaterBT, captureAadharDetBT;
    private SeccMemberItem seccItem;
    private SelectedMemberItem selectedMemItem;
    private VerifierLoginResponse loginResponse;
    private AlertDialog internetDiaolg;
    private ImageView verified, rejected, pending, qrCodeAadhaarPendingIV, qrCodeAadhaarRejectedIV, qrCodeAadhaarVerifiedIV;
    private CheckBox consentCB;
    private boolean isVeroff;
    private AadharAuthItem aadharAuthItem;
    private ProgressDialog dialogProcessRequest;
    private String nameAsInAadharForPopUp;
    String res = "";
    private Button proceedBT;
   private AadhaarResponseItem aadhaarResponseItem ;
   private LinearLayout qrCodeLayout;
   private LogRequestItem logRequestItem;

    public FindBeneficiaryByQrCode() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment\
        View view = inflater.inflate(R.layout.fragment_aadhar_auth_qr_code, container, false);
        context = getActivity();
        setUpScreen(view);
        return view;
    }

    private void setUpScreen(View view) {
        aadhaarResponseItem = new AadhaarResponseItem();
        dialogProcessRequest = new ProgressDialog(demoAuthActivity);
        dialogProcessRequest.setIndeterminateDrawable(context.getResources().getDrawable(R.mipmap.nhps_logo));
       /* dialogProcessRequest.setProgressStyle(ProgressDialog.STYLE_SPINNER);*/
        dialogProcessRequest.setMessage(context.getResources().getString(R.string.please_wait));
        dialogProcessRequest.setCancelable(false);
        aadharAuthItem = new AadharAuthItem();
        selectedMemItem = SelectedMemberItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.SELECTED_ITEM_FOR_VERIFICATION, context));
        if (selectedMemItem!=null && selectedMemItem.getSeccMemberItem() != null) {
            seccItem = selectedMemItem.getSeccMemberItem();
        }

        loginResponse = (VerifierLoginResponse.create(ProjectPrefrence.
                getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.VERIFIER_CONTENT, context)));
        if (loginResponse!=null && loginResponse.getAadhaarNumber() != null) {
            Global.VALIDATORAADHAR = loginResponse.getAadhaarNumber();
        } else {
            Global.VALIDATORAADHAR = "352624429973";
        }
        proceedBT= (Button) view.findViewById(R.id.proceedBT);
        proceedBT.setVisibility(View.GONE);
        qrCodeLayout=(LinearLayout)view.findViewById(R.id.qrCodeLayout);
        qrCodeLayout.setVisibility(View.GONE);
        consentCB = (CheckBox) view.findViewById(R.id.consentCheck);
        validateAadhaarBT = (Button) view.findViewById(R.id.validateAdhaarBT);
        validateLaterBT = (Button) view.findViewById(R.id.validateAdhaarLaterBT);
        captureAadharDetBT = (Button) view.findViewById(R.id.captureAadharDetBT);
        captureAadharDetBT.setText("Capture beneficiary detail by QR Code");
        verified = (ImageView) view.findViewById(R.id.aadhaarVerifiedIV);
        rejected = (ImageView) view.findViewById(R.id.aadhaarRejectedIV);
        pending = (ImageView) view.findViewById(R.id.aadhaarPendingIV);
        qrCodeAadhaarPendingIV = (ImageView) view.findViewById(R.id.qrCodeAadhaarPendingIV);
        qrCodeAadhaarRejectedIV = (ImageView) view.findViewById(R.id.qrCodeAadhaarRejectedIV);
        qrCodeAadhaarVerifiedIV = (ImageView) view.findViewById(R.id.qrCodeAadhaarVerifiedIV);
        qrCodeAadharNumberET = (EditText) view.findViewById(R.id.qrCodeAadharNumberET);
        qrCodeNameAsInAadhaarET = (EditText) view.findViewById(R.id.qrCodeNameAsInAadhaarET);
        qrCodeDobAsInAadhaarET = (EditText) view.findViewById(R.id.qrCodeDobAsInAadhaarET);
        qrCodeGenderAsInAadhaarET = (EditText) view.findViewById(R.id.qrCodeGenderAsInAadhaarET);
        qrCodePincodeET= (EditText) view.findViewById(R.id.qrCodePincodeET);

        qrCodeLayoutVisible();
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

        qrCodePincodeET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                qrCodePincodeET.setTextColor(Color.BLACK);
                if (charSequence.toString().length() < 6) {
                    //if (!Verhoeff.validateVerhoeff(charSequence.toString())) {
                        //qrCodePincodeET.setTextColor(Color.RED);
                    qrCodePincodeET.setTextColor(Color.BLACK);

                    //}
                }else {
                    qrCodePincodeET.setTextColor(Color.GREEN);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        if (aadhaarItem != null) {
            qrCodeLayout.setVisibility(View.VISIBLE);
            qrCodeAadharNumberET.setText(aadhaarItem.getUid());


            qrCodeNameAsInAadhaarET.setText(aadhaarItem.getName());


            if (aadhaarItem.getDob() != null && !aadhaarItem.getDob().equalsIgnoreCase("")) {
                qrCodeDobAsInAadhaarET.setText(aadhaarItem.getDob());
            } else if (aadhaarItem.getYob() != null && !aadhaarItem.getYob().equalsIgnoreCase("")) {
                qrCodeDobAsInAadhaarET.setText(aadhaarItem.getYob());
            }

            qrCodeGenderAsInAadhaarET.setText(aadhaarItem.getGender());

        } else {


            if (seccItem!=null && seccItem.getAadhaarCapturingMode() != null) {
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
              //  String aadhaarNumber2 = qrCodeAadharNumberET.getText().toString().trim();
                String dob = qrCodeDobAsInAadhaarET.getText().toString().trim();
                String gender = qrCodeGenderAsInAadhaarET.getText().toString().trim();
                aadharAuthItem = new AadharAuthItem();
              //  aadharAuthItem.setAadharNo(aadhaarNumber2);
                aadharAuthItem.setName(nameAsInAadhaar2);
                //aadharAuthItem.setDob(qrCodeDobAsInAadhaarET.getText().toString());
                aadharAuthItem.setDob(AppUtility.formatQrCodeAadharDob(dob.replace("/", "")));

                if(!gender.equalsIgnoreCase("")) {
                    aadharAuthItem.setGender(gender.substring(0,1).toUpperCase());
                }
               /* if (aadhaarNumber2.trim().equalsIgnoreCase("")) {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterAadhaar));

                    return;
                }*/
                if (nameAsInAadhaar2.trim().equalsIgnoreCase("")) {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterName));

                    return;
                }
                if (dob.trim().equalsIgnoreCase("")) {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterDob));

                    return;
                }
                if (gender.trim().equalsIgnoreCase("")) {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterGender));

                    return;
                }
                if (!isVeroff) {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterValidAadhaar));

                    return;
                }
                /*if (!consentCB.isChecked()) {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.adhar_consent_msg));
                    return;
                }*/

                if (demoAuthActivity.isNetworkAvailable()) {
                    //qqqqqqqq
                    //performAuth();
                    proceedForSearch();
                    //qrCodeAlertForValidateNow(seccItem, aadharAuthItem);
                } else {
                    AppUtility.alertWithOk(context, context.getResources().getString(R.string.internet_connection_msg));
                }

            }
        });
        validateLaterBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              /*  AppUtility.hideSoftInput(activity, validateLaterBT);
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
                }*/


                AppUtility.hideSoftInput(activity, validateAadhaarBT);
                String nameAsInAadhaar2 = qrCodeNameAsInAadhaarET.getText().toString().trim();
               // String aadhaarNumber2 = qrCodeAadharNumberET.getText().toString().trim();
                String dob = qrCodeDobAsInAadhaarET.getText().toString().trim();
                String gender = qrCodeGenderAsInAadhaarET.getText().toString().trim();
              /*  aadharAuthItem = new AadharAuthItem();
                aadharAuthItem.setAadharNo(aadhaarNumber2);
                aadharAuthItem.setName(nameAsInAadhaar2);
                aadharAuthItem.setDob(AppUtility.formatAadharAuth(dob.replace("/","")));
                aadharAuthItem.setGender(gender);*/
               /* if (aadhaarNumber2.trim().equalsIgnoreCase("")) {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterAadhaar));

                    return;
                }*/
                if (nameAsInAadhaar2.trim().equalsIgnoreCase("")) {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterName));

                    return;
                }
                if (dob.trim().equalsIgnoreCase("")) {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterDob));

                    return;
                }
                if (gender.trim().equalsIgnoreCase("")) {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterGender));

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
                seccItem.setAadhaarAuth(AppConstant.PENDING_STATUS);
                StringBuilder alertMessage = new StringBuilder();
                alertMessage.append(context.getResources().getString(R.string.name_as_sec) + seccItem.getName() +
                        "\n" + context.getResources().getString(R.string.name_as_aadhar) + nameAsInAadhaar2 + "\n\n" + context.getResources().getString(R.string.aadhar_confirmation_msg));
                alertForValidateLater(nameAsInAadhaar2, seccItem);
            }
        });

        captureAadharDetBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openQRCapture();
            }
        });


    }

    private void openQRCapture() {
        Intent theIntent = new Intent(context, BarcodeCaptureActivity.class);
        startActivityForResult(theIntent, BARCODE_READER_REQUEST_CODE);
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
        } else {
            CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzCaptureAadhaardetails));
        }
    }

    private void qrCodeAlertForValidateNow(SeccMemberItem item, final AadharAuthItem aadharAuthItem) {
        internetDiaolg = new AlertDialog.Builder(context).create();

        final LayoutInflater factory = LayoutInflater.from(context);
        View alertView = factory.inflate(R.layout.aadhar_validation_msg_popup, null);
        internetDiaolg.setView(alertView);
        internetDiaolg.show();
        TextView nameAsInAadharTV = (TextView) alertView.findViewById(R.id.nameAsInAadharTV);
        TextView nameAsInSeccTV = (TextView) alertView.findViewById(R.id.nameAsInSeccTV);
        nameAsInAadharTV.setText(aadharAuthItem.getName());
       // nameAsInSeccTV.setText(item.getName());
        Button tryAgainBT = (Button) alertView.findViewById(R.id.tryAgainBT);
        tryAgainBT.setText(context.getResources().getString(R.string.Confirm_Submit));
        Button cancelBT = (Button) alertView.findViewById(R.id.cancelBT);
        tryAgainBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* seccItem.setAadhaarAuth(AppConstant.PENDING_STATUS);
                nameAsInAadharForPopUp = aadharAuthItem.getName();
                performAuth();
                internetDiaolg.dismiss();*/
                nameAsInAadharForPopUp = aadharAuthItem.getName();
                performAuth();
                internetDiaolg.dismiss();

            }
        });
        cancelBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                internetDiaolg.dismiss();
            }
        });
    }


    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
       //activity = (CaptureAadharDetailActivity) context;
        //demoAuthActivity = (DemoAuthActivity) context;
        demoAuthActivity = (FindBeneficiaryByNameActivity) context;

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BARCODE_READER_REQUEST_CODE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
                    //  Point[] p = barcode.cornerPoints;
                    qrCodeLayout.setVisibility(View.VISIBLE);
                    parserAdhaarXML(barcode.displayValue);

                } else {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.aadhar_not_readable));
                }
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

        if (item.getPc() != null && !item.getPc().equalsIgnoreCase("")) {
            qrCodePincodeET.setText(item.getPc());
        }
        if(item.getState()!=null && !item.getState().equalsIgnoreCase("")){
            aadhaarResponseItem.setState(item.getState());
        }
        if(item.getDist()!=null && !item.getDist().equalsIgnoreCase("")){
            aadhaarResponseItem.setDist(item.getDist());
        }
        if(item.getSubDist()!=null && !item.getSubDist().equalsIgnoreCase("")){
            aadhaarResponseItem.setSubdist(item.getSubDist());
        }
        if(item.getCo()!=null && !item.getCo().equalsIgnoreCase("")){
            aadhaarResponseItem.setCo(item.getCo());
        }
        if(item.getHouse()!=null && !item.getHouse().equalsIgnoreCase("")){
            aadhaarResponseItem.setHouse(item.getHouse());
        }
        if(item.getStreet()!=null && !item.getStreet().equalsIgnoreCase("")){
            aadhaarResponseItem.setStreet(item.getStreet());
        }
        if(item.getVtc()!=null && !item.getVtc().equalsIgnoreCase("")){
            aadhaarResponseItem.setVtc(item.getVtc());
        }
        validateAadhaarBT.setVisibility(View.VISIBLE);
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


    private void performAuth() {
        if (demoAuthActivity.isNetworkAvailable()) {
            Global.USER_NAME = ApplicationGlobal.USER_NAME;
            Global.USER_PASSWORD = ApplicationGlobal.USER_PASSWORD;
            GetPubKeycertificateData publicKey1 = new GetPubKeycertificateData();
            publicKey1.execute();
            //		init();
        } else {
            AppUtility.alertWithOk(context, context.getResources().getString(R.string.internet_connection_msg));
        }
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
                //  Global.productionPublicKey=result;
                pidXml = AppUtility.generatePIDblockXml(context, result, aadharAuthItem.getName(), aadharAuthItem.getDob(), aadharAuthItem.getGender(), aadharAuthItem.getAadharNo());
                if (pidXml != null && !pidXml.equalsIgnoreCase("")) {
                    hitToServerforDemoAUTHRequest();
                }
            }
            //			if (result.endsWith("=") ) {
            //				result=result.replace("\r\n", "");
            //				Global.productionPublicKey=result;
            //			}
            else {
                //				ShowPrompt("Critical Error", "Please go back...");
                Global.USER_NAME = ApplicationGlobal.USER_NAME;
                Global.USER_PASSWORD = ApplicationGlobal.USER_PASSWORD;
                GetPubKeycertificateData publicKey1 = new GetPubKeycertificateData();
                publicKey1.execute();
            }

        }
    }

    public void hitToServerforDemoAUTHRequest() {

        if (demoAuthActivity.isNetworkAvailable()) {
            startTime = System.currentTimeMillis();
            if (dialogProcessRequest != null && activity != null) {
                dialogProcessRequest.show();
            }
            HitToServer task = new HitToServer();
            task.execute();
        } else {
            AppUtility.alertWithOk(context, context.getResources().getString(R.string.internet_connection_msg));
        }
    }

  /*  private class HitToServer extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String res = "";
            res = CommonMethods.HttpPostLifeCerticiate(Global.DEMO_AUTH, pidXml);
            Log.e("Response", "==" + res);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            if(dialogProcessRequest!=null && dialogProcessRequest.isShowing()){
                dialogProcessRequest.dismiss();
            }

            if (result.equalsIgnoreCase("ERROR") || result.equalsIgnoreCase("False from server") || result.equalsIgnoreCase("Connection time out Error")) {

                AppUtility.alertWithOk(context, result);
            } else {
                //				edtText_Aadhaar.setText("");
               // readAuthXml(result);

            }
        }
    }*/

   /* private class HitToServer extends AsyncTask<String, Void, DemoAuthResp> {

        @Override
        protected DemoAuthResp doInBackground(String... params) {

            res = CommonMethods.HttpPostLifeCerticiate(Global.DEMO_AUTH, pidXml);
            Log.e("Response", "==" + res);
            try {
                demoAuthResp = new DemoAuthResp().create(res);
            }catch (Exception e){

            }
            return demoAuthResp;
        }

        @Override
        protected void onPostExecute(DemoAuthResp result) {
            if (dialogProcessRequest != null && dialogProcessRequest.isShowing()) {
                dialogProcessRequest.dismiss();
            }
            if (result != null) {
                if (result != null && result.getRet() != null && !result.getRet().equalsIgnoreCase("")) {
                    if (result.getRet().equalsIgnoreCase("Y")) {

                        //alertWithOk(context, "Aadhar auth Successfull");
                        finalSubmit("Aadhar auth Successfull");
                    } else {
                        AppUtility.alertWithOk(context, "Aadhar auth failed \n" + result.getErr());
                    }

                } else {
                    AppUtility.alertWithOk(context, "Aadhar auth failed " + res);
                }
            }else{
                AppUtility.alertWithOk(context, "Aadhar auth failed " + res);
            }
        }
    }*/

    private class HitToServer extends AsyncTask<String, Void, AadhaarDemoAuthResponse> {


        @Override
        protected AadhaarDemoAuthResponse doInBackground(String... params) {
            String JSON;
            res = CustomHttp.HttpPostLifeCerticiate(Global.DEMO_AUTH, pidXml, AppConstant.AUTHORIZATION, AppConstant.AUTHORIZATIONVALUE);
            Log.e("Response", "==" + res);
            try {

                try {
                    XmlToJson xmlToJson = new XmlToJson.Builder(res).build();
                    JSON = xmlToJson.toString();
                } catch (Exception ex) {
                    return null;
                }
                demoAuthResp = new AadhaarDemoAuthResponse().create(JSON);
            } catch (Exception ex) {

            }
            return demoAuthResp;
        }

        @Override
        protected void onPostExecute(AadhaarDemoAuthResponse result) {
            if (dialogProcessRequest != null && dialogProcessRequest.isShowing()) {
                dialogProcessRequest.dismiss();
            }
            if (result != null) {
                if (result.getAuthRes() != null && result.getAuthRes().getRet() != null && !result.getAuthRes().getRet().equalsIgnoreCase("")) {
                    if (result.getAuthRes().getRet().equalsIgnoreCase("Y")) {
                        alertWithOk(context,"Aadhar auth successfull");
                       // finalSubmit("Aadhar auth successfull");
                    } else {
                        AppUtility.alertWithOk(context, "Aadhar auth failed \n" + result.getAuthRes().getErr());
                    }

                } else {
                    AppUtility.alertWithOk(context, "Aadhar auth failed " + res);
                }
            } else {
                AppUtility.alertWithOk(context, "Aadhar auth failed " + res);
            }
        }
    }

    private void proceedForSearch(){
        aadhaarResponseItem.setName(qrCodeNameAsInAadhaarET.getText().toString());
        aadhaarResponseItem.setDob(qrCodeDobAsInAadhaarET.getText().toString());
        aadhaarResponseItem.setGender(qrCodeGenderAsInAadhaarET.getText().toString());
        aadhaarResponseItem.setPc(qrCodePincodeET.getText().toString());
        aadhaarResponseItem.setResult("Y");
        logRequestItem=new LogRequestItem();
        logRequestItem.setPagescreenname("QRCODE");
        logRequestItem.setAction("SEARCH_BY_NAME/QRCODE");
        ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,AppConstant.LOG_REQUEST,logRequestItem.serialize(),context);
    //    aadhaarResponseItem.setUid(qrCodeAadharNumberET.getText().toString());
      //  alert.dismiss();
        Intent intent = new Intent(context , FingerprintResultActivity.class);
        intent.putExtra("result",aadhaarResponseItem);
        startActivity(intent);
    }
    public void alertWithOk(Context mContext, String msg) {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Alert");

        builder.setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things


                        aadhaarResponseItem.setName(qrCodeNameAsInAadhaarET.getText().toString());
                        aadhaarResponseItem.setDob(qrCodeDobAsInAadhaarET.getText().toString());
                        aadhaarResponseItem.setGender(qrCodeGenderAsInAadhaarET.getText().toString());
                        aadhaarResponseItem.setPc(qrCodePincodeET.getText().toString());
                        aadhaarResponseItem.setResult("Y");

                      //  aadhaarResponseItem.setUid(qrCodeAadharNumberET.getText().toString());
                        alert.dismiss();
                        Intent intent = new Intent(context , FingerprintResultActivity.class);
                        intent.putExtra("result",aadhaarResponseItem);
                        startActivity(intent);

                      /*  seccItem.setAadhaarAuth(AppConstant.VALID_STATUS);
                        seccItem.setAadhaarAuthDt(String.valueOf(DateTimeUtil.currentTimeMillis()));
                        seccItem.setAadhaarAuthMode(AppConstant.QR_CODE_MODE);
                        qrCodeSubmitAaadhaarDetail();*/
/*
                        proceedBT.setVisibility(View.VISIBLE);
                        validateAadhaarBT.setVisibility(View.GONE);*/
                    }
                });
        alert = builder.create();
        alert.show();
    }

    private void finalSubmit(String msg) {
        internetDiaolg = new AlertDialog.Builder(context).create();

        final LayoutInflater factory = LayoutInflater.from(context);
        View alertView = factory.inflate(R.layout.aadhar_validation_msg_popup, null);
        internetDiaolg.setView(alertView);
        internetDiaolg.show();
        TextView popUpHeader = (TextView) alertView.findViewById(R.id.popUpHeader);
        popUpHeader.setVisibility(View.VISIBLE);
        popUpHeader.setText(msg);
        TextView nameAsInAadharTV = (TextView) alertView.findViewById(R.id.nameAsInAadharTV);
        TextView nameAsInSeccTV = (TextView) alertView.findViewById(R.id.nameAsInSeccTV);
        nameAsInAadharTV.setText(nameAsInAadharForPopUp);
        nameAsInSeccTV.setText(seccItem.getName());
        Button tryAgainBT = (Button) alertView.findViewById(R.id.tryAgainBT);
        tryAgainBT.setText(context.getResources().getString(R.string.Confirm_Submit));
        Button cancelBT = (Button) alertView.findViewById(R.id.cancelBT);
        tryAgainBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seccItem.setAadhaarAuth(AppConstant.VALID_STATUS);
                seccItem.setAadhaarAuthDt(String.valueOf(DateTimeUtil.currentTimeMillis()));
                seccItem.setAadhaarAuthMode(AppConstant.QR_CODE_MODE);
                qrCodeSubmitAaadhaarDetail();
                internetDiaolg.dismiss();

            }
        });
        cancelBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                internetDiaolg.dismiss();
            }
        });
    }

}
