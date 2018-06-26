package com.nhpm.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.customComponent.CustomAlert;
import com.customComponent.utility.CustomHttp;
import com.customComponent.utility.DateTimeUtil;
import com.customComponent.utility.ProjectPrefrence;
import com.nhpm.AadhaarUtils.Global;
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
import com.nhpm.Utility.ErrorCodeDescriptions;
import com.nhpm.Utility.Verhoeff;
import com.nhpm.activity.CaptureAadharDetailActivity;
import com.nhpm.activity.DemoAuthActivity;
import com.nhpm.activity.FindBeneficiaryByNameActivity;
import com.nhpm.activity.FingerprintResultActivity;
import com.nhpm.activity.WithAadhaarActivity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;

import fr.arnaudguyon.xmltojsonlib.XmlToJson;

/*import com.aadhar.CommonMethods;
import com.aadhar.Global;*/


public class FindBeneficiaryByManualFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String nameAsInAadharForPopUp;
    private AadhaarDemoAuthResponse demoAuthResp;
    private String pidXml;
    private String consent = "Y";
    private long startTime;
    String res = "";
    String aadharDob1 = "";
    private String aadharDob = null;
    private AlertDialog internetDiaolg;
    private EditText qrCodeAadharNumberET, qrCodeNameAsInAadhaarET, qrCodeDobAsInAadhaarET,
            qrCodeGenderAsInAadhaarET, manualAadharNumberET, manualNameAsInAadhaarET,
            manualDobAsInAadhaarET, manualYobAsInAadhaarET, manualPincodeAsInAadhaarET;
    private String manualGenderSelection = "";
    private AadharAuthItem aadharAuthItem;
    private RadioGroup radioGroup, dateOfBirthRG, genderRG;
    private RadioButton radioButtonQrcode, radioButtonManual, maleRB, femaleRB, otherRB, dobRB, yobRB;
    private boolean isYobSelected;
    private boolean isDobSelected;
    private Context context;
    private boolean isVeroff;
    private SelectedMemberItem selectedMemItem;
    private VerifierLoginResponse loginResponse;
    private ImageView verified, rejected, pending;
    private AadhaarCaptureDetailItem aadhaarItem;
    private SeccMemberItem seccItem;
    private CaptureAadharDetailActivity activity;
    private Button validateAadhaarBT, validateLaterBT;
    private CheckBox consentCheck;
    private ProgressDialog dialogProcessRequest;
    //private DemoAuthActivity demoAuthActivity;
    private FindBeneficiaryByNameActivity demoAuthActivity;

    private Button proceedBT;

    private String aadharNo, currentYear;
    private LogRequestItem logRequestItem;

    public FindBeneficiaryByManualFragment() {
        // Required empty public constructor
    }

    public static FindBeneficiaryByManualFragment newInstance(String param1, String param2) {
        FindBeneficiaryByManualFragment fragment = new FindBeneficiaryByManualFragment();
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
        View view = inflater.inflate(R.layout.fragment_aadhar_auth_manual, container, false);
        context = getActivity();
        setUpScreen(view);
        return view;
    }


    private void setUpScreen(View v) {
        aadharAuthItem = new AadharAuthItem();
        dialogProcessRequest = new ProgressDialog(context);
        dialogProcessRequest.setIndeterminateDrawable(context.getResources().getDrawable(R.mipmap.nhps_logo));
       /* dialogProcessRequest.setProgressStyle(ProgressDialog.STYLE_SPINNER);*/
        dialogProcessRequest.setMessage(context.getResources().getString(R.string.please_wait));
        dialogProcessRequest.setCancelable(false);
        aadharNo = ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.AadharNumber, context);

        String currentDate = DateTimeUtil.currentDate("dd MM yyyy");
        Log.d("current date", currentDate);
        currentYear = currentDate.substring(6, 10);
        Log.d("current year", currentYear);
        consentCheck = (CheckBox) v.findViewById(R.id.consentCheck);
        validateAadhaarBT = (Button) v.findViewById(R.id.validateAdhaarBT);
        validateLaterBT = (Button) v.findViewById(R.id.validateAdhaarLaterBT);
        manualAadharNumberET = (EditText) v.findViewById(R.id.manualAadharNumberET);
        manualNameAsInAadhaarET = (EditText) v.findViewById(R.id.manualNameAsInAadhaarET);
        manualPincodeAsInAadhaarET = (EditText) v.findViewById(R.id.manualPincodeAsInAadhaarET);
        //  manualGenderAsInAadhaarET = (EditText) v.findViewById(R.id.manualGenderAsInAadhaarET);
        proceedBT = (Button) v.findViewById(R.id.proceedBT);
        proceedBT.setVisibility(View.GONE);
        manualDobAsInAadhaarET = (EditText) v.findViewById(R.id.manualDobAsInAadhaarET);
        manualDobAsInAadhaarET.addTextChangedListener(tw);
        manualYobAsInAadhaarET = (EditText) v.findViewById(R.id.manualYobAsInAadhaarET);
        maleRB = (RadioButton) v.findViewById(R.id.maleRB);
        femaleRB = (RadioButton) v.findViewById(R.id.femaleRB);
        otherRB = (RadioButton) v.findViewById(R.id.otherRB);
        dobRB = (RadioButton) v.findViewById(R.id.dobRB);
        yobRB = (RadioButton) v.findViewById(R.id.yobRB);
        dateOfBirthRG = (RadioGroup) v.findViewById(R.id.dateOfBirthRG);
        genderRG = (RadioGroup) v.findViewById(R.id.genderRG);
        selectedMemItem = SelectedMemberItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.SELECTED_ITEM_FOR_VERIFICATION, context));
        loginResponse = (VerifierLoginResponse.create(ProjectPrefrence.
                getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.VERIFIER_CONTENT, context)));
        if (loginResponse != null && loginResponse.getAadhaarNumber() != null) {
            Global.VALIDATORAADHAR = loginResponse.getAadhaarNumber();
        } else {
            Global.VALIDATORAADHAR = "352624429973";
        }
        verified = (ImageView) v.findViewById(R.id.aadhaarVerifiedIV);
        rejected = (ImageView) v.findViewById(R.id.aadhaarRejectedIV);
        pending = (ImageView) v.findViewById(R.id.aadhaarPendingIV);
        /*backIV = (ImageView) v.findViewById(R.id.back);*/
        if (selectedMemItem != null && selectedMemItem.getSeccMemberItem() != null) {
            seccItem = selectedMemItem.getSeccMemberItem();
        }

        manualYobAsInAadhaarET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() > 0) {
                    if(charSequence.toString().startsWith("1") || charSequence.toString().startsWith("2")){
                        // if (Integer.parseInt(charSequence.toString().substring(1)) < 2) {

                        manualYobAsInAadhaarET.setTextColor(context.getResources().getColor(R.color.black_shine));

                        if (manualYobAsInAadhaarET.getText().toString().length() == 4) {
                            manualYobAsInAadhaarET.setTextColor(context.getResources().getColor(R.color.green));
                            // isValidMobile = true;
                        }else{
                            //isValidMobile = false;
                        }
                    } else {
                        //isValidMobile = false;
                        manualYobAsInAadhaarET.setTextColor(context.getResources().getColor(R.color.red));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        manualLayoutVisible();
    }

    private void manualLayoutVisible() {

        genderRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == maleRB.getId()) {
                    manualGenderSelection = "M";
                    aadharAuthItem.setGender(manualGenderSelection);
                } else if (checkedId == femaleRB.getId()) {
                    manualGenderSelection = "F";
                    aadharAuthItem.setGender(manualGenderSelection);
                } else if (checkedId == otherRB.getId()) {
                    manualGenderSelection = "T";
                    aadharAuthItem.setGender(manualGenderSelection);
                }

            }

        });

        //////
        if (seccItem != null && seccItem.getDataSource() != null && seccItem.getDataSource().equalsIgnoreCase(AppConstant.RSBY_SOURCE)) {
            if (seccItem.getRsbyGender() != null) {
                if (seccItem.getRsbyGender().equalsIgnoreCase(AppConstant.FEMALE_GENDER)) {
                    femaleRB.setChecked(true);
                    manualGenderSelection = "F";
                    aadharAuthItem.setGender(manualGenderSelection);
                } else if (seccItem.getRsbyGender().equalsIgnoreCase(AppConstant.MALE_GENDER)) {
                    maleRB.setChecked(true);
                    manualGenderSelection = "M";
                    aadharAuthItem.setGender(manualGenderSelection);
                } else {
                    otherRB.setChecked(true);
                    manualGenderSelection = "T";
                    aadharAuthItem.setGender(manualGenderSelection);

                }
            }
        } else {
            if (seccItem != null && seccItem.getGenderid() != null) {
                if (seccItem.getGenderid().equalsIgnoreCase(AppConstant.FEMALE_GENDER)) {
                    femaleRB.setChecked(true);
                    manualGenderSelection = "F";
                    aadharAuthItem.setGender(manualGenderSelection);
                } else if (seccItem.getGenderid().equalsIgnoreCase(AppConstant.MALE_GENDER)) {
                    maleRB.setChecked(true);
                    manualGenderSelection = "M";
                    aadharAuthItem.setGender(manualGenderSelection);
                } else {
                    otherRB.setChecked(true);
                    manualGenderSelection = "T";
                    aadharAuthItem.setGender(manualGenderSelection);
                }
            }
        }

        ////////
        dateOfBirthRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                AppUtility.hideSoftInput(activity);
                if (checkedId == yobRB.getId()) {
                    isYobSelected = true;
                    isDobSelected = false;
                    manualYobAsInAadhaarET.setVisibility(View.VISIBLE);
                    manualYobAsInAadhaarET.requestFocus();
                    AppUtility.showSoftInput(activity);
                    manualDobAsInAadhaarET.setVisibility(View.GONE);
                } else if (checkedId == dobRB.getId()) {
                    isYobSelected = false;
                    isDobSelected = true;
                    manualDobAsInAadhaarET.setVisibility(View.VISIBLE);
                    manualDobAsInAadhaarET.requestFocus();
                    AppUtility.showSoftInput(activity);
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

        manualPincodeAsInAadhaarET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() > 0) {
                    if(!charSequence.toString().startsWith("0")){
                        // if (Integer.parseInt(charSequence.toString().substring(1)) < 2) {

                        manualPincodeAsInAadhaarET.setTextColor(context.getResources().getColor(R.color.black_shine));

                        if (manualPincodeAsInAadhaarET.getText().toString().length() == 6) {
                            manualPincodeAsInAadhaarET.setTextColor(context.getResources().getColor(R.color.green));
                            // isValidMobile = true;
                        }else{
                            //isValidMobile = false;
                        }
                    } else {
                        //isValidMobile = false;
                        manualPincodeAsInAadhaarET.setTextColor(context.getResources().getColor(R.color.red));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
  /*      manualPincodeAsInAadhaarET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                manualPincodeAsInAadhaarET.setTextColor(Color.BLACK);
                if (charSequence.toString().length() < 6) {
                    //if (!Verhoeff.validateVerhoeff(charSequence.toString())) {
                    //manualPincodeAsInAadhaarET.setTextColor(Color.RED);
                    manualPincodeAsInAadhaarET.setTextColor(Color.BLACK);

                } else {

                    manualPincodeAsInAadhaarET.setTextColor(Color.GREEN);

                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });*/


        if (aadharNo != null) {
            manualAadharNumberET.setText(aadharNo);
        }
        if (seccItem != null && seccItem.getAadhaarCapturingMode() != null) {
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
                    if (seccItem.getAadhaarDob() != null && !seccItem.getAadhaarDob().equalsIgnoreCase("")) {
                        String dob = seccItem.getAadhaarDob();
                        if (dob.length() == 4) {
                            yobRB.setChecked(true);
                            manualYobAsInAadhaarET.setVisibility(View.VISIBLE);
                            manualYobAsInAadhaarET.setText(dob);
                        } else {
                            dobRB.setChecked(true);
                            manualDobAsInAadhaarET.setVisibility(View.VISIBLE);
                            manualDobAsInAadhaarET.setText(dob);
                        }

                    }

                    if (seccItem.getAadhaarGender() != null && !seccItem.getAadhaarGender().equalsIgnoreCase("")) {
                        if (seccItem.getAadhaarGender().equalsIgnoreCase("M")) {
                            maleRB.setChecked(true);
                        } else if (seccItem.getAadhaarGender().equalsIgnoreCase("F")) {
                            femaleRB.setChecked(true);
                        } else {
                            otherRB.setChecked(true);
                        }
                    }

                } else if (seccItem.getAadhaarAuth() != null && seccItem.getAadhaarAuth().equalsIgnoreCase(AppConstant.VALID_STATUS)) {
                    verified.setVisibility(View.VISIBLE);
                    if (manualAadharNumberET.getText().toString().equalsIgnoreCase("")) {
                        manualAadharNumberET.setText(seccItem.getAadhaarNo());
                    }
                    if (manualNameAsInAadhaarET.getText().toString().equalsIgnoreCase("")) {
                        manualNameAsInAadhaarET.setText(seccItem.getNameAadhaar());
                    }

                    if (seccItem.getAadhaarDob() != null && !seccItem.getAadhaarDob().equalsIgnoreCase("")) {
                        String dob = seccItem.getAadhaarDob();
                        if (dob.length() == 4) {
                            yobRB.setChecked(true);
                            manualYobAsInAadhaarET.setVisibility(View.VISIBLE);
                            manualYobAsInAadhaarET.setText(dob);
                        } else {
                            dobRB.setChecked(true);
                            manualDobAsInAadhaarET.setVisibility(View.VISIBLE);
                            manualDobAsInAadhaarET.setText(dob);
                        }

                    }

                    if (seccItem.getAadhaarGender() != null && !seccItem.getAadhaarGender().equalsIgnoreCase("")) {
                        if (seccItem.getAadhaarGender().equalsIgnoreCase("M")) {
                            maleRB.setChecked(true);
                        } else if (seccItem.getAadhaarGender().equalsIgnoreCase("F")) {
                            femaleRB.setChecked(true);
                        } else {
                            otherRB.setChecked(true);
                        }
                    }
                } else if (seccItem.getAadhaarAuth() != null && seccItem.getAadhaarAuth().equalsIgnoreCase(AppConstant.INVALID_STATUS)) {
                    rejected.setVisibility(View.VISIBLE);
                    if (manualAadharNumberET.getText().toString().equalsIgnoreCase("")) {
                        manualAadharNumberET.setText(seccItem.getAadhaarNo());
                    }
                    if (manualNameAsInAadhaarET.getText().toString().equalsIgnoreCase("")) {
                        manualNameAsInAadhaarET.setText(seccItem.getNameAadhaar());
                    }
                    if (seccItem.getAadhaarDob() != null && !seccItem.getAadhaarDob().equalsIgnoreCase("")) {
                        String dob = seccItem.getAadhaarDob();
                        if (dob.length() == 4) {
                            yobRB.setChecked(true);
                            manualYobAsInAadhaarET.setVisibility(View.VISIBLE);
                            manualYobAsInAadhaarET.setText(dob);
                        } else {
                            dobRB.setChecked(true);
                            manualDobAsInAadhaarET.setVisibility(View.VISIBLE);
                            manualDobAsInAadhaarET.setText(dob);
                        }

                    }

                    if (seccItem.getAadhaarGender() != null && !seccItem.getAadhaarGender().equalsIgnoreCase("")) {
                        if (seccItem.getAadhaarGender().equalsIgnoreCase("M")) {
                            maleRB.setChecked(true);
                        } else if (seccItem.getAadhaarGender().equalsIgnoreCase("F")) {
                            femaleRB.setChecked(true);
                        } else {
                            otherRB.setChecked(true);
                        }
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
               /* if (seccItem.getAadhaarDob() != null && !seccItem.getAadhaarDob().equalsIgnoreCase("")) {
                    if (manualGenderAsInAadhaarET.getText().toString().equalsIgnoreCase("")) {
                        manualGenderAsInAadhaarET.setText(seccItem.getAadhaarDob());
                    }
                }*/
                if (seccItem.getAadhaarGender() != null && !seccItem.getAadhaarGender().equalsIgnoreCase("")) {
                    /*if (manualDobAsInAadhaarET.getText().toString().equalsIgnoreCase("")) {
                        manualDobAsInAadhaarET.setText(seccItem.getAadhaarGender());
                    }*/
                }
            }
        }
        // aadharNumberET.setText("424175531180");
        // nameAsInAadhaarET.setText("Sunil Kumar");
        validateAadhaarBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logRequestItem=new LogRequestItem();
                logRequestItem.setAction("SEARCH_BY_NAME/MANUAL");
                logRequestItem.setPagescreenname("Manual");
                ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,AppConstant.LOG_REQUEST,logRequestItem.serialize(),context);

                AppUtility.hideSoftInput(activity, validateAadhaarBT);
                String nameAsInAadhaar = manualNameAsInAadhaarET.getText().toString().trim();
               // String aadhaarNumber = manualAadharNumberET.getText().toString().trim();
                String aadharDob = "";
              //  aadharAuthItem.setAadharNo(aadhaarNumber);
                aadharAuthItem.setName(nameAsInAadhaar);
                aadharDob = manualYobAsInAadhaarET.getText().toString();
                aadharDob1 = manualYobAsInAadhaarET.getText().toString();
                aadharAuthItem.setDob(manualYobAsInAadhaarET.getText().toString());

                /*if (isDobSelected) {
                    if (!checkDob(manualDobAsInAadhaarET.getText().toString())) {
                        CustomAlert.alertWithOk(context, "Please enter valid date of birth");
                        return;
                    }
                    aadharAuthItem.setDob(*//*AppUtility.formatAadharAuth(*//*manualDobAsInAadhaarET.getText().toString())*//*)*//*;
                    aadharDob = manualDobAsInAadhaarET.getText().toString();

                }
                if (isYobSelected) {
                    aadharDob = manualYobAsInAadhaarET.getText().toString();
                    aadharAuthItem.setDob(manualYobAsInAadhaarET.getText().toString());
                }*/
                // aadharAuthItem.setDob();
                /*if (aadhaarNumber.trim().equalsIgnoreCase("")) {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterAadhaar));
                    return;
                }*/
                if (nameAsInAadhaar.trim().equalsIgnoreCase("")) {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterName));
                    return;
                }
/*
                if (!isVeroff) {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterValidAadhaar));
                    return;
                }*/
                if (aadharDob.trim().equalsIgnoreCase("")) {
                    CustomAlert.alertWithOk(context, "Please enter year of birth");
                    return;
                }

                if (aadharDob != null && !aadharDob.equalsIgnoreCase("")) {
                    int yearRange = Integer.parseInt(currentYear) - 100;

                    if (aadharDob.equalsIgnoreCase(currentYear) || Integer.parseInt(aadharDob) < yearRange) {
                        CustomAlert.alertWithOk(context, "Please enter valid year of birth");
                        return;
                    }

                }


                if (manualGenderSelection != null && manualGenderSelection.equalsIgnoreCase("")) {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterGender));
                    return;
                }

                if (manualPincodeAsInAadhaarET.getText().toString().equalsIgnoreCase("")) {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterPincode));
                    return;
                }

                /*if (!consentCheck.isChecked()) {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.adhar_consent_msg));
                    return;
                }*/
                if (demoAuthActivity.isNetworkAvailable()) {
                    //performAuth();
                    proceedForSearch();
                    //  requestAadhaarAuth(aadhaarNumber, nameAsInAadhaar);
                    //ManualAlertForValidateNow(aadharAuthItem, seccItem, aadhaarNumber, nameAsInAadhaar);
                } else {
                    CustomAlert.alertWithOk(context, getResources().getString(R.string.internet_connection_msg));
                }
            }
        });
        validateLaterBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             /*   AppUtility.hideSoftInput(activity, validateLaterBT);
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
                if (!consentCheck.isChecked()) {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.adhar_consent_msg));
                    return;
                }
*/


                AppUtility.hideSoftInput(activity, validateAadhaarBT);
                String nameAsInAadhaar = manualNameAsInAadhaarET.getText().toString().trim();
                //String aadhaarNumber = manualAadharNumberET.getText().toString().trim();
                String aadharDob = "";
                //   String aadhaarGender = manualGenderAsInAadhaarET.getText().toString();
               // aadharAuthItem.setAadharNo(aadhaarNumber);
                aadharAuthItem.setName(nameAsInAadhaar);

                if (isDobSelected) {
                    if (!checkDob(manualDobAsInAadhaarET.getText().toString())) {
                        CustomAlert.alertWithOk(context, "Please enter valid date of birth");
                        return;
                    }

                    aadharAuthItem.setDob(/*AppUtility.formatAadharAuth(*/manualDobAsInAadhaarET.getText().toString()/*)*/);
                    aadharDob = manualDobAsInAadhaarET.getText().toString();

                }
                if (isYobSelected) {
                    aadharDob = manualYobAsInAadhaarET.getText().toString();
                    aadharAuthItem.setDob(manualYobAsInAadhaarET.getText().toString());
                }
                // aadharAuthItem.setDob();
               /* if (aadhaarNumber.trim().equalsIgnoreCase("")) {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterAadhaar));
                    return;
                }*/
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

                if (!consentCheck.isChecked()) {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.adhar_consent_msg));
                    return;
                }
                seccItem.setAadhaarAuth(AppConstant.PENDING_STATUS);
                alertForValidateLater(nameAsInAadhaar, seccItem);
            }
        });


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
                manualSubmitAaadhaarDetail();

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

        if (isDobSelected) {
            if (!checkDob(manualDobAsInAadhaarET.getText().toString())) {
                CustomAlert.alertWithOk(context, "Please enter valid date of birth");
                return;
            }
            aadharAuthItem.setDob(/*AppUtility.formatAadharAuth(*/manualDobAsInAadhaarET.getText().toString()/*)*/);
            aadharDob = manualDobAsInAadhaarET.getText().toString();

        }
        if (isYobSelected) {
            aadharDob = manualYobAsInAadhaarET.getText().toString();
            aadharAuthItem.setDob(manualYobAsInAadhaarET.getText().toString());
        }
        if (!manualAadharNumberET.getText().toString().equalsIgnoreCase("") &&
                !manualNameAsInAadhaarET.getText().toString().equalsIgnoreCase("")) {

            //seccItem.setAadhaarNo(manualAadharNumberET.getText().toString().trim());
            seccItem.setNameAadhaar(manualNameAsInAadhaarET.getText().toString().trim());
            seccItem.setAadhaarCapturingMode(AppConstant.MANUAL_MODE);
            seccItem.setAadhaarLm("");
            seccItem.setAadhaarPc("");
            seccItem.setAadhaarCo("");
            seccItem.setAadhaarDist("");
            if (aadharDob != null && !aadharDob.equalsIgnoreCase("")) {
                seccItem.setAadhaarDob(aadharDob);
            } else {
                seccItem.setAadhaarDob("");
            }
            seccItem.setAadhaarSubdist("");
            if (manualGenderSelection != null && !manualGenderSelection.equalsIgnoreCase("")) {
                seccItem.setAadhaarGender(manualGenderSelection);
            } else {
                seccItem.setAadhaarGender("");
            }
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
                AppUtility.showLog(AppConstant.LOG_STATUS, "", "Captured Aadhaar : " + seccItem.serialize());
                updateAadhaarDetail();
            }
        } else {
            CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzCaptureAadhaardetails));
        }
    }

    private void updateAadhaarDetail() {
        Intent theIntent = new Intent(context, WithAadhaarActivity.class);
        seccItem.setLockedSave(AppConstant.SAVE + "");
        if (seccItem != null && seccItem.getDataSource() != null && seccItem.getDataSource()
                .trim().equalsIgnoreCase(AppConstant.RSBY_SOURCE)) {
            if (selectedMemItem.getOldHeadMember() != null && selectedMemItem.getNewHeadMember() != null) {
                SeccMemberItem oldHead = selectedMemItem.getOldHeadMember();
                oldHead.setLockedSave(AppConstant.LOCKED + "");
                AppUtility.showLog(AppConstant.LOG_STATUS, "", " OLD Household name " +
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
                AppUtility.showLog(AppConstant.LOG_STATUS, "", " OLD Household name " +
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

    private void ManualAlertForValidateNow(final AadharAuthItem authItem, SeccMemberItem item, final String aadhaarNumber, final String aadhaarName) {
        internetDiaolg = new AlertDialog.Builder(context).create();

        final LayoutInflater factory = LayoutInflater.from(context);
        View alertView = factory.inflate(R.layout.aadhar_validation_msg_popup, null);
        internetDiaolg.setView(alertView);
        internetDiaolg.show();
        TextView nameAsInAadharTV = (TextView) alertView.findViewById(R.id.nameAsInAadharTV);
        TextView nameAsInSeccTV = (TextView) alertView.findViewById(R.id.nameAsInSeccTV);
        nameAsInAadharTV.setText(aadhaarName);
        // nameAsInSeccTV.setText(item.getName());
        Button tryAgainBT = (Button) alertView.findViewById(R.id.tryAgainBT);
        tryAgainBT.setText(context.getResources().getString(R.string.Confirm_Submit));
        Button cancelBT = (Button) alertView.findViewById(R.id.cancelBT);
        tryAgainBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  seccItem.setAadhaarAuth(AppConstant.PENDING_STATUS);

                /*requestAadhaarAuth(aadhaarNumber, aadhaarName);*/
            /*    Intent theIntent = new Intent(context, AadharAuthActivity.class);
                theIntent.putExtra(AppConstant.AUTHITEM, authItem);
                startActivity(theIntent);*/
                nameAsInAadharForPopUp = aadhaarName;
                performAuth();
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

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        // activity = (CaptureAadharDetailActivity) context;
       // demoAuthActivity = (DemoAuthActivity) context;
        demoAuthActivity = (FindBeneficiaryByNameActivity) context;
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
    private void proceedForSearch(){
        AadhaarResponseItem aadhaarResponseItem = new AadhaarResponseItem();
        aadhaarResponseItem.setName(manualNameAsInAadhaarET.getText().toString());
        aadhaarResponseItem.setDob(aadharDob1);
        aadhaarResponseItem.setPc(manualPincodeAsInAadhaarET.getText().toString());
        aadhaarResponseItem.setGender(manualGenderSelection);
        aadhaarResponseItem.setResult("Y");
        //aadhaarResponseItem.setUid(manualAadharNumberET.getText().toString());
        Intent intent = new Intent(context, FingerprintResultActivity.class);
        intent.putExtra("FindBeneficiaryByManualFragment","FindBeneficiaryByManualFragment");
        intent.putExtra("result", aadhaarResponseItem);
        startActivity(intent);
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
            if (dialogProcessRequest != null) {
                dialogProcessRequest.show();
            }
            HitToServer task = new HitToServer();
            task.execute();
        } else {
            AppUtility.alertWithOk(context, context.getResources().getString(R.string.internet_connection_msg));
        }
    }

    private class HitToServer extends AsyncTask<String, Void, AadhaarDemoAuthResponse> {

        @Override
        protected AadhaarDemoAuthResponse doInBackground(String... params) {
            String JSON;
            Log.e("httpPostAadhaarURL_d", Global.DEMO_AUTH);
            Log.e("httpPostAadhaarREQBODY", pidXml);
            Log.e("httpPostAadhaarTOKEN", AppConstant.AUTHORIZATION);
            Log.e("httpPostAadhaarTVALUE", AppConstant.AUTHORIZATIONVALUE);

            res = CustomHttp.HttpPostLifeCerticiate(Global.DEMO_AUTH, pidXml, AppConstant.AUTHORIZATION, AppConstant.AUTHORIZATIONVALUE);
            Log.e("ResponseAuthDemo", "==" + res);
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
                        alertWithOk(context, "Aadhar auth successfull");
                        // finalSubmit("Aadhar auth successfull");
                    } else {
                        AppUtility.alertWithOk(context, "Aadhar auth failed \n" + "Error code: "
                                + result.getAuthRes().getErr() + " ("
                                + ErrorCodeDescriptions.getDescription(result.getAuthRes().getErr())
                                + ")");
                    }

                } else {
                    AppUtility.alertWithOk(context, "Aadhar auth failed " + res);
                }
            } else {
                AppUtility.alertWithOk(context, "Aadhar auth failed " + res);
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
                        //do things
                        AadhaarResponseItem aadhaarResponseItem = new AadhaarResponseItem();
                        aadhaarResponseItem.setName(manualNameAsInAadhaarET.getText().toString());
                        aadhaarResponseItem.setDob(aadharDob1);
                        aadhaarResponseItem.setPc(manualPincodeAsInAadhaarET.getText().toString());
                        aadhaarResponseItem.setGender(manualGenderSelection);
                        aadhaarResponseItem.setResult("Y");
                     //   aadhaarResponseItem.setUid(manualAadharNumberET.getText().toString());
                        Intent intent = new Intent(context, FingerprintResultActivity.class);
                        intent.putExtra("result", aadhaarResponseItem);
                        startActivity(intent);
                        /*proceedBT.setVisibility(View.VISIBLE);
                        validateAadhaarBT.setVisibility(View.GONE);*/

                        /*seccItem.setAadhaarAuth(AppConstant.VALID_STATUS);
                        seccItem.setAadhaarAuthDt(String.valueOf(DateTimeUtil.currentTimeMillis()));
                        seccItem.setAadhaarAuthMode(AppConstant.MANUAL_MODE);
                        manualSubmitAaadhaarDetail();*/
                    }
                });
        AlertDialog alert = builder.create();
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
                seccItem.setAadhaarAuthMode(AppConstant.MANUAL_MODE);
                manualSubmitAaadhaarDetail();
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

    TextWatcher tw = new TextWatcher() {
        private String current = "";
        private String yyyymmdd = "YYYYMMDD";
        private Calendar cal = Calendar.getInstance();

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!s.toString().equals(current)) {
                String clean = s.toString().replaceAll("[^\\d.]", "");
                String cleanC = current.replaceAll("[^\\d.]", "");
                int cl = clean.length();
                int sel = cl;
                for (int i = 4; i <= cl && i < 8; i += 2) {
                    sel++;
                }
                if (clean.equals(cleanC)) sel--;
                if (clean.length() < 8) {
                    clean = clean + yyyymmdd.substring(clean.length());
                } else {
                    int day = Integer.parseInt(clean.substring(6, 8));
                    int mon = Integer.parseInt(clean.substring(4, 6));
                    int year = Integer.parseInt(clean.substring(0, 4));
                    if (mon > 12) mon = 12;
                    cal.set(Calendar.MONTH, mon - 1);
                    year = (year < 1900) ? 1900 : (year > 2100) ? 2100 : year;
                    cal.set(Calendar.YEAR, year);
                    day = (day > cal.getActualMaximum(Calendar.DATE)) ? cal.getActualMaximum(Calendar.DATE) : day;
                    clean = String.format("%02d%02d%02d", year, mon, day);
                }
                clean = String.format("%s-%s-%s", clean.substring(0, 4),
                        clean.substring(4, 6),
                        clean.substring(6, 8));
                sel = sel < 0 ? 0 : sel;
                current = clean;
                manualDobAsInAadhaarET.setText(current);
                manualDobAsInAadhaarET.setSelection(sel < current.length() ? sel : current.length());
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private boolean checkDob(String dob) {
        // String dob=dobET.getText().toString();
        int dobLength = dob.length();

        if (dob.contains("Y") || dob.contains("M") || dob.contains("D")) {
            return false;
        } else if (dobLength != 10) {
            return false;
        } else {
            return true;
        }
    }
}

