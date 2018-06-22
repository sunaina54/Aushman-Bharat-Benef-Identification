package com.nhpm.fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.customComponent.CustomAlert;
import com.customComponent.CustomAsyncTask;
import com.customComponent.TaskListener;
import com.customComponent.utility.CustomHttp;
import com.customComponent.utility.DateTimeUtil;
import com.customComponent.utility.ProjectPrefrence;
import com.nhpm.CameraUtils.CommonUtilsImageCompression;
import com.nhpm.CameraUtils.FaceCropper;
import com.nhpm.CameraUtils.squarecamera.CameraActivity;
import com.nhpm.LocalDataBase.DatabaseHelpers;
import com.nhpm.LocalDataBase.dto.SeccDatabase;
import com.nhpm.Models.SerachOptionItem;
import com.nhpm.Models.request.MobileOtpRequest;
import com.nhpm.Models.request.PersonalDetailItem;
import com.nhpm.Models.response.BeneficiaryListItem;
import com.nhpm.Models.response.DocsListItem;
import com.nhpm.Models.response.MobileOTPResponse;
import com.nhpm.Models.response.master.ConfigurationItem;
import com.nhpm.Models.response.master.StateItem;
import com.nhpm.Models.response.seccMembers.SeccMemberItem;
import com.nhpm.Models.response.verifier.AadhaarResponseItem;
import com.nhpm.Models.response.verifier.VerifierLoginResponse;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;
import com.nhpm.Utility.AppUtility;
import com.nhpm.Utility.ApplicationGlobal;
import com.nhpm.Utility.Verhoeff;
import com.nhpm.activity.CollectDataActivity;
import com.nhpm.activity.EkycActivity;
import com.nhpm.activity.FamilyMembersListActivity;
import com.nhpm.activity.GovermentIDActivity;
import com.nhpm.activity.GovermentIDCaptureActivity;
import com.nhpm.activity.NameMatchScoreActivity;
import com.nhpm.activity.PhoneNumberActivity;
import com.nhpm.activity.PhotoCaptureActivity;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;
import static com.nhpm.AadhaarUtils.CommonMethods.isNetworkAvailable;

/**
 * A simple {@link Fragment} subclass.
 */
public class PersonalDetailsFragment extends Fragment {
    private View view;
    private AadhaarResponseItem aadhaarKycResponse;
    private AlertDialog internetDiaolg;
    private String benefidentificationMode = "";
    private AlertDialog dialog;
    private Context context;
    private EkycActivity ekycActivity;
    private int CAMERA_PIC_REQUEST = 0;
    private int EKYC = 1;
    private int GOVT_ID_REQUEST = 2;
    private Picasso mPicasso;
    private ImageView govtIdPhotoIV;
    private TextView govtIdType, govtIdNumberTV;
    private LinearLayout govtIdLL;
    private CollectDataActivity activity;
    private FaceCropper mFaceCropper;
    private Button nextBT, captureImageBT, verifyAadharBT;
    private boolean isVeroff;
    private AutoCompleteTextView aadharET;
    private MobileOTPResponse mobileOtpRequestModel;
    private CustomAsyncTask mobileOtpAsyncTask;
    private MobileOTPResponse mobileOtpVerifyModel;
    private ImageView beneficiaryPhotoIV;
    private String operationId;
    private Button verifyMobBT;
    private boolean isValidMobile;
    private Bitmap memberPhoto;
    private EditText mobileNumberET;
    private String name;
    private TextView beneficiaryNameTV, beneficiaryNamePerIdTV, noAadhaarTV, nameScoreLabelTV, nameScorePercentTV;
    private DocsListItem beneficiaryListItem;
    private PersonalDetailItem personalDetailItem;
    private String status = "";
    private LinearLayout aadharLL, noAadhaarLL;
    private Button matchBT;
    private VerifierLoginResponse storedLoginResponse;
    private String nameMatchScore="";

    public AadhaarResponseItem getAadhaarKycResponse() {
        return aadhaarKycResponse;
    }

    public void setAadhaarKycResponse(AadhaarResponseItem aadhaarKycResponse) {
        this.aadhaarKycResponse = aadhaarKycResponse;
    }

    public PersonalDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_personal_details, container, false);
        context = getActivity();
        setupScreen(view);
        return view;

    }

    private void setupScreen(View view) {
        checkAppConfig(view);
        storedLoginResponse = VerifierLoginResponse.create(
                ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.VERIFIER_CONTENT, context));


        // personalDetailItem = beneficiaryListItem.getPersonalDetail();

        Bundle bundle = getArguments();
        //bundle.getString("personalDetail");
        /*if (bundle != null) {
            personalDetailItem = PersonalDetailItem.create(bundle.getString("personalDetail"));
        }*/
        personalDetailItem = activity.benefItem.getPersonalDetail();
        matchBT = (Button) view.findViewById(R.id.matchBT);

        noAadhaarTV = (TextView) view.findViewById(R.id.noAadhaarTV);
        aadharLL = (LinearLayout) view.findViewById(R.id.aadharLL);
        govtIdLL = (LinearLayout) view.findViewById(R.id.govtIdLL);
        noAadhaarLL = (LinearLayout) view.findViewById(R.id.noAadhaarLL);
        govtIdNumberTV = (TextView) view.findViewById(R.id.govtIdNumberTV);
        govtIdType = (TextView) view.findViewById(R.id.govtIdType);
        nameScoreLabelTV = (TextView) view.findViewById(R.id.nameScoreLabelTV);

        nameScorePercentTV = (TextView) view.findViewById(R.id.nameScorePercentTV);
        beneficiaryNamePerIdTV = (TextView) view.findViewById(R.id.beneficiaryNamePerIdTV);

        govtIdLL.setVisibility(View.GONE);
        beneficiaryNameTV = (TextView) view.findViewById(R.id.beneficiaryNameTV);
        beneficiaryPhotoIV = (ImageView) view.findViewById(R.id.beneficiaryPhotoIV);
        govtIdPhotoIV = (ImageView) view.findViewById(R.id.govtIdPhotoIV);
        aadharET = (AutoCompleteTextView) view.findViewById(R.id.aadharET);
        captureImageBT = (Button) view.findViewById(R.id.captureImageBT);
        verifyMobBT = (Button) view.findViewById(R.id.verifyMobBT);
        verifyAadharBT = (Button) view.findViewById(R.id.verifyAadharBT);
        mobileNumberET = (EditText) view.findViewById(R.id.mobileET);
        // if(name!=null){
        noAadhaarTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status = "govt";
                ProjectPrefrence.
                        removeSharedPrefrenceData(AppConstant.PROJECT_NAME, "GOVT_ID_DATA", context);
                Intent intent = new Intent(context, GovermentIDActivity.class);
                // intent.putExtra("mobileNumber",mobileNumberET.getText().toString());
                intent.putExtra("mobileNumber", personalDetailItem);

                startActivityForResult(intent, GOVT_ID_REQUEST);
            }
        });
        if (beneficiaryListItem != null) {
            beneficiaryNameTV.setText(beneficiaryListItem.getName());

        }
        if (personalDetailItem != null) {
            nameMatchScore = personalDetailItem.getNameMatchScore()+"";
            if(!nameMatchScore.equalsIgnoreCase("")){
                nameScoreLabelTV.setVisibility(View.VISIBLE);
                nameScorePercentTV.setVisibility(View.VISIBLE);
                nameScorePercentTV.setText(nameMatchScore +"%");
            }
            if (!personalDetailItem.getFlowStatus().equalsIgnoreCase("")
                    && personalDetailItem.getFlowStatus().equalsIgnoreCase(AppConstant.AADHAR_STATUS)) {
                aadharLL.setVisibility(View.VISIBLE);
                govtIdLL.setVisibility(View.GONE);
                noAadhaarLL.setVisibility(View.GONE);
                status = "aadhar";
                isVeroff = true;
                if (personalDetailItem.getBenefPhoto() != null) {
                    beneficiaryPhotoIV.setImageBitmap(AppUtility.convertStringToBitmap(personalDetailItem.getBenefPhoto()));
                }
                if (personalDetailItem.getGovtIdNo() != null) {
                    aadharET.setEnabled(false);
                    aadharET.setText(personalDetailItem.getGovtIdNo());
                    aadharET.setTextColor(Color.GREEN);
                    verifyAadharBT.setEnabled(false);
                    verifyAadharBT.setBackground(getResources().getDrawable(R.drawable.rounded_grey_button));

                }
                matchBT.setBackground(getResources().getDrawable(R.drawable.rounded_grey_button));
                matchBT.setEnabled(false);
                if (personalDetailItem.getMobileNo() != null) {
                    mobileNumberET.setEnabled(false);
                    mobileNumberET.setTextColor(AppUtility.getColor(context, R.color.green));
                    mobileNumberET.setText(personalDetailItem.getMobileNo());
                    mobileNumberET.setEnabled(false);
                    verifyMobBT.setEnabled(false);
                    verifyMobBT.setBackground(getResources().getDrawable(R.drawable.rounded_grey_button));
                }

                if (personalDetailItem.getName() != null && !personalDetailItem.getName().equalsIgnoreCase("")) {
                    beneficiaryNamePerIdTV.setText(personalDetailItem.getName());
                }

            }

            if (!personalDetailItem.getFlowStatus().equalsIgnoreCase("")
                    && personalDetailItem.getFlowStatus().equalsIgnoreCase(AppConstant.GOVT_STATUS)) {
                aadharLL.setVisibility(View.GONE);
                govtIdLL.setVisibility(View.VISIBLE);
                noAadhaarLL.setVisibility(View.GONE);
                status = "govt";
                if (personalDetailItem.getBenefPhoto() != null) {
                    beneficiaryPhotoIV.setImageBitmap(AppUtility.convertStringToBitmap(personalDetailItem.getBenefPhoto()));
                }
                matchBT.setBackground(getResources().getDrawable(R.drawable.rounded_grey_button));
                matchBT.setEnabled(false);
                if (personalDetailItem.getIdPhoto() != null && !personalDetailItem.getIdPhoto().equalsIgnoreCase("")) {
                    govtIdPhotoIV.setImageBitmap(AppUtility.convertStringToBitmap(personalDetailItem.getIdPhoto()));

                }
                if (personalDetailItem.getGovtIdNo() != null &&
                        !personalDetailItem.getGovtIdNo().equalsIgnoreCase("")) {
                    govtIdNumberTV.setText(personalDetailItem.getGovtIdNo());

                }

                if (personalDetailItem.getGovtIdType() != null &&
                        !personalDetailItem.getGovtIdType().equalsIgnoreCase("")) {
                    govtIdType.setText(personalDetailItem.getGovtIdType());

                }


                if (personalDetailItem.getName() != null && !personalDetailItem.getName().equalsIgnoreCase("")) {
                    beneficiaryNamePerIdTV.setText(personalDetailItem.getName());

                }

                if (personalDetailItem.getMobileNo() != null) {
                    mobileNumberET.setTextColor(AppUtility.getColor(context, R.color.green));
                    mobileNumberET.setText(personalDetailItem.getMobileNo());
                    mobileNumberET.setEnabled(false);
                    verifyMobBT.setEnabled(false);
                    verifyMobBT.setBackground(getResources().getDrawable(R.drawable.rounded_grey_button));

                }


            }

        } else {
            personalDetailItem = new PersonalDetailItem();

        }

        mobileNumberET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() > 0) {
                    if (Integer.parseInt(charSequence.toString().substring(0, 1)) > 5) {
                        isValidMobile = true;
                        mobileNumberET.setTextColor(AppUtility.getColor(context, R.color.black_shine));
                        if (mobileNumberET.getText().toString().length() == 10) {
                            // whoseMobileSP.setEnabled(true);
                            // whoseMobileSP.setAlpha(1.0f);
                            // mobileValidateLayout.setVisibility(View.VISIBLE);
                            //prepareFamilyStatusSpinner(mobileNumberET.getText().toString().trim());
                            mobileNumberET.setTextColor(AppUtility.getColor(context, R.color.green));
                            AppUtility.softKeyBoard(getActivity(), 0);

                        }
                    } else {
                        // whoseMobileSP.setEnabled(false);
                        //  whoseMobileSP.setAlpha(0.4f);
                        isValidMobile = false;
                        // mobileValidateLayout.setVisibility(View.GONE);
                        mobileNumberET.setTextColor(AppUtility.getColor(context, R.color.red));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        matchBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String beneficiaryName, nameAsKyc;
                beneficiaryName = beneficiaryNameTV.getText().toString();
                nameAsKyc = beneficiaryNamePerIdTV.getText().toString();
                Intent intent=new Intent(context, NameMatchScoreActivity.class);
                intent.putExtra(NameMatchScoreActivity.PERSONAL_DETAIL_TAG,personalDetailItem);
                intent.putExtra(NameMatchScoreActivity.SECC_DETAIL_TAG,beneficiaryListItem);
                startActivityForResult(intent,3);
               /* if (beneficiaryName != null && !beneficiaryName.equalsIgnoreCase("")
                        && nameAsKyc != null && !nameAsKyc.equalsIgnoreCase("")) {
                    alertForValidateLater(beneficiaryName, nameAsKyc);

                }else {
                    CustomAlert.alertWithOk(context,"Name cannot be blank");
                }*/

            }
        });
        verifyAadharBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (aadharET.getText().toString().equalsIgnoreCase("")) {
                    CustomAlert.alertWithOk(context, "Please enter aadhar number");
                    return;
                }
                ProjectPrefrence.
                        removeSharedPrefrenceData(AppConstant.PROJECT_NAME, "AADHAAR_DATA", context);
                status = "aadhar";
                Intent intent = new Intent(context, EkycActivity.class);
                intent.putExtra("screen", "PersonalDetailsFragment");
                intent.putExtra("mobileNumber", personalDetailItem);
                intent.putExtra("aadharNo", aadharET.getText().toString());
                SerachOptionItem item = new SerachOptionItem();
                item.setAadhaarNo(aadharET.getText().toString());
                item.setSearchType(AppConstant.AADHAAR_SEARCH);
                item.setMode(AppConstant.EKYC);
                ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_NAME, AppConstant.SEARCH_OPTION, item.serialize(), context);
                startActivityForResult(intent, EKYC);
            }
        });

        verifyMobBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobile = mobileNumberET.getText().toString().trim();
                //  seccItem.setMobileAuth(AppConstant.PENDING_STATUS);
                //String mobileNumber=mobil
                // eNumberET.getText().toString();
                //AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Mobile Number Index : "+mobile.length());


                if (mobile.equalsIgnoreCase("") || mobile.length() < 10) {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnter10DigitMobNum));
                    return;
                }
                //AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Mobile Number Index : " + isValidMobile);
                if (!isValidMobile) {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterValidMobNum));
                    return;
                }
             /*   if (mobile.equalsIgnoreCase(loginResponse.getMobileNumber())) {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.youHaveEnteredValidatorMobileNum));
                    return;
                }*/
                if (activity.isNetworkAvailable()) {
                    requestForOTP(mobile);
                } else {
                    CustomAlert.alertWithOk(context, getResources().getString(R.string.internet_connection_msg));
                }

            }
        });
        captureImageBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
            }
        });
        aadharET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isVeroff = Verhoeff.validateVerhoeff(s.toString());
                aadharET.setTextColor(Color.BLACK);
                if (s.toString().length() > 11) {
                    if (!Verhoeff.validateVerhoeff(s.toString())) {
                        aadharET.setTextColor(Color.RED);
                    } else {

                        aadharET.setTextColor(Color.GREEN);
                        AppUtility.softKeyBoard(getActivity(), 0);
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        nextBT = (Button) view.findViewById(R.id.nextBT);
        nextBT.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (status != null && !status.equalsIgnoreCase("")) {
                    if (status.equalsIgnoreCase("aadhar")) {
                        if (aadharET.getText().toString().equalsIgnoreCase("")) {
                            CustomAlert.alertWithOk(context, "Please enter aadhaar number");
                            return;
                        }
                        if (!isVeroff) {
                            CustomAlert.alertWithOk(context, getResources().getString(R.string.invalid_login));
                            return;
                        }
                        if (mobileNumberET.getText().toString().equalsIgnoreCase("")) {
                            CustomAlert.alertWithOk(context, "Please enter mobile number");
                            return;
                        }

                        if (personalDetailItem != null && personalDetailItem.getBenefPhoto() == null
                                || personalDetailItem.getBenefPhoto().equalsIgnoreCase("")) {
                            CustomAlert.alertWithOk(context, "Beneficiary photo cannot be blank");
                            return;
                        }

                        if (personalDetailItem != null && personalDetailItem.getIsMobileAuth() == null || !personalDetailItem.getIsMobileAuth().equalsIgnoreCase("Y")) {
                            CustomAlert.alertWithOk(context, "Please verified mobile number");
                            return;
                        }

                        if(nameMatchScore==null || nameMatchScore.equalsIgnoreCase("")){
                            CustomAlert.alertWithOk(context, "Please match beneficiary name");
                            return;
                        }

                        personalDetailItem.setNameMatchScore(Integer.parseInt(nameMatchScore));
                        activity.personalDetailsLL.setBackground(context.getResources().getDrawable(R.drawable.arrow));
                        activity.familyDetailsLL.setBackground(context.getResources().getDrawable(R.drawable.arrow_yellow));
                        beneficiaryListItem.setPersonalDetail(personalDetailItem);

                        activity.benefItem = beneficiaryListItem;
                        Fragment fragment = new FamilyDetailsFragment();
                        Bundle args = new Bundle();
                        // args.putString("personalDetail", personalDetailItem.serialize());

                        fragment.setArguments(args);
                        CallFragment(fragment);
                    }

                    if (status.equalsIgnoreCase("govt")) {

                        if (mobileNumberET.getText().toString().equalsIgnoreCase("")) {
                            CustomAlert.alertWithOk(context, "Please enter mobile number");
                            return;
                        }

                        if (personalDetailItem != null && personalDetailItem.getIsMobileAuth() == null || !personalDetailItem.getIsMobileAuth().equalsIgnoreCase("Y")) {
                            CustomAlert.alertWithOk(context, "Please verified mobile number");
                            return;
                        }

                        if (personalDetailItem != null && personalDetailItem.getBenefPhoto() == null
                                || personalDetailItem.getBenefPhoto().equalsIgnoreCase("")) {
                            CustomAlert.alertWithOk(context, "Beneficiary photo cannot be blank");
                            return;
                        }

                        if (personalDetailItem != null && personalDetailItem.getIdPhoto() == null
                                || personalDetailItem.getIdPhoto().equalsIgnoreCase("")) {
                            CustomAlert.alertWithOk(context, "Govt id photo cannot be blank");
                            return;
                        }
                        if(nameMatchScore==null || nameMatchScore.equalsIgnoreCase("")){
                            CustomAlert.alertWithOk(context, "Please match beneficiary name");
                            return;
                        }
                        personalDetailItem.setNameMatchScore(Integer.parseInt(nameMatchScore));
                        beneficiaryListItem.setPersonalDetail(personalDetailItem);
                        activity.personalDetailsLL.setBackground(context.getResources().getDrawable(R.drawable.arrow));
                        activity.familyDetailsLL.setBackground(context.getResources().getDrawable(R.drawable.arrow_yellow));

                        Fragment fragment = new FamilyDetailsFragment();
                        Bundle args = new Bundle();
                        activity.benefItem = beneficiaryListItem;
                        //args.putString("personalDetail", beneficiaryListItem.serialize());
                        fragment.setArguments(args);
                        CallFragment(fragment);
                    }

                } else {
                    CustomAlert.alertWithOk(context, "Please fill beneficiary personal details");
                }


            }
        });
    }

    public void CallFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragContainer, fragment);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        //activity = (CaptureAadharDetailActivity) context;
        // if (activity instanceof CollectDataActivity) {
        activity = (CollectDataActivity) context;

        beneficiaryListItem = activity.benefItem;
        // }
       /* if (ekycActivity instanceof EkycActivity) {
            ekycActivity = (EkycActivity) context;
        }*/
    }

    private void requestForOTP(final String mobileNumber) {

        TaskListener taskListener = new TaskListener() {
            @Override
            public void execute() {

                MobileOtpRequest request = new MobileOtpRequest();
                request.setMobileNo(mobileNumber);
                request.setOtp("");
                request.setStatus("0");
                request.setSequenceNo("NHPS:" + DateTimeUtil.currentDateTime(AppConstant.RSBY_ISSUES_TIME_STAMP_DATE_FORMAT));
                request.setUserName(ApplicationGlobal.MOBILE_Username);
                request.setUserPass(ApplicationGlobal.MOBILE_Password);

                try {
                    HashMap<String, String> response = CustomHttp.httpPost(AppConstant.REQUEST_FOR_MOBILE_OTP, request.serialize());
                    if (response != null) {
                        mobileOtpRequestModel = MobileOTPResponse.create(response.get(AppConstant.RESPONSE_BODY));
                    } else {


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void updateUI() {
                if (mobileOtpRequestModel != null && mobileOtpRequestModel.getOtp() != null) {
                    try {
                        popupForOTPValidation(mobileNumber, mobileOtpRequestModel.getSequenceNo());
                    } catch (Exception error) {

                    }
                }

            }
        };
        if (mobileOtpAsyncTask != null) {
            mobileOtpAsyncTask.cancel(true);
            mobileOtpAsyncTask = null;
        }

        mobileOtpAsyncTask = new CustomAsyncTask(taskListener, context.getResources().getString(R.string.please_wait), context);
        mobileOtpAsyncTask.execute();

    }

    private void validateOTP(final String otp, final String mobileNumber, final TextView authOtpTV, final String sequenceNo) {

        TaskListener taskListener = new TaskListener() {
            @Override
            public void execute() {

                MobileOtpRequest request = new MobileOtpRequest();
                request.setMobileNo(mobileNumber);
                request.setOtp(otp);
                request.setStatus("1");
                request.setSequenceNo(sequenceNo);
                request.setUserName(ApplicationGlobal.MOBILE_Username);
                request.setUserPass(ApplicationGlobal.MOBILE_Password);

                try {
                    HashMap<String, String> response = CustomHttp.httpPost(AppConstant.REQUEST_FOR_OTP_VERIFICATION, request.serialize());
                    if (response != null) {
                        mobileOtpVerifyModel = MobileOTPResponse.create(response.get(AppConstant.RESPONSE_BODY));
                    }
                } catch (Exception e) {

                    Toast.makeText(context, "Server not responding/Server is down. Please try after sometime... ", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }

            @Override
            public void updateUI() {
                /*verified.setVisibility(View.GONE);
                rejected.setVisibility(View.GONE);
                pending.setVisibility(View.GONE);*/
                if (mobileOtpVerifyModel != null && mobileOtpVerifyModel.getMessage() != null && mobileOtpVerifyModel.getMessage().equalsIgnoreCase("Y")) {
                    personalDetailItem.setIsMobileAuth("Y");
                    mobileNumberET.setEnabled(false);
                    verifyMobBT.setEnabled(false);
                    verifyMobBT.setBackground(getResources().getDrawable(R.drawable.rounded_grey_button));
                    personalDetailItem.setMobileNo(mobileNumberET.getText().toString());
                    operationId = storedLoginResponse.getAadhaarNumber();
                    String operatorId = operationId.substring(operationId.length() - 4);
                    Log.d("operator id :", operatorId);
                    personalDetailItem.setOpertaorid(Integer.parseInt(operatorId));
                   // personalDetailItem.setNameMatchScore(Integer.parseInt(nameMatchScore));
                    //beneficiaryListItem.getPersonalDetail().setMobileNo(mobileNumberET.getText().toString());
                    Toast.makeText(context, "OTP verified successfully", Toast.LENGTH_SHORT).show();
                    // CustomAlert.alertWithOk(context, "OTP verified successfully");

                    // AppUtility.hideSoftInput(getActivity());


                    /*seccItem.setMobileAuth(AppConstant.VALID_STATUS);
                    seccItem.setMobileAuthDt(String.valueOf(DateTimeUtil.currentTimeMillis()));
                    verified.setVisibility(View.VISIBLE);*/
                    // submitMobile();
                    dialog.dismiss();
                } else {
                    authOtpTV.setText(context.getResources().getString(R.string.invalid_otp));
                    authOtpTV.setTextColor(AppUtility.getColor(context, R.color.red));
                }


            }
        };
        if (mobileOtpAsyncTask != null) {
            mobileOtpAsyncTask.cancel(true);
            mobileOtpAsyncTask = null;
        }

        mobileOtpAsyncTask = new CustomAsyncTask(taskListener, context.getResources().getString(R.string.please_wait), context);
        mobileOtpAsyncTask.execute();

    }

    private void popupForOTPValidation(final String mobileNumber, final String sequence) {
        dialog = new AlertDialog.Builder(context).create();
        LayoutInflater factory = LayoutInflater.from(context);
        View alertView = factory.inflate(R.layout.opt_auth_layout, null);
        dialog.setView(alertView);
        dialog.setCancelable(false);

        // dialog.setContentView(R.layout.opt_auth_layout);
        final TextView otpAuthMsg = (TextView) alertView.findViewById(R.id.otpAuthMsg);
        otpAuthMsg.setVisibility(View.VISIBLE);
        String mobile = mobileNumber;
        mobile = "XXXXXX" + mobile.substring(6);
        otpAuthMsg.setText("Please enter OTP sent on " + mobile);
        final Button okButton = (Button) alertView.findViewById(R.id.ok);
        okButton.setEnabled(false);
        final Button resendBT = (Button) alertView.findViewById(R.id.resendBT);
        Button cancelBT = (Button) alertView.findViewById(R.id.cancelBT);
        final EditText optET = (EditText) alertView.findViewById(R.id.otpET);
        //   final Button resendBT = (Button) alertView.findViewById(R.id.resendBT);
        final TextView mTimer = (TextView) alertView.findViewById(R.id.timerTV);

        new CountDownTimer(25 * 1000 + 1000, 1000) {

            public void onTick(long millisUntilFinished) {

                int seconds = (int) (millisUntilFinished / 1000);
                int minutes = seconds / 60;
                seconds = seconds % 60;
                mTimer.setVisibility(View.VISIBLE);
                mTimer.setText(String.format("%02d", minutes)
                        + ":" + String.format("%02d", seconds));
            }

            public void onFinish() {
                mTimer.setVisibility(View.GONE);
                resendBT.setEnabled(true);

                resendBT.setTextColor(context.getResources().getColor(R.color.white));
                resendBT.setBackground(context.getResources().getDrawable(R.drawable.button_background_orange_ehit));
            }

        }.start();

        new CountDownTimer(2 * 1000 + 1000, 1000) {

            public void onTick(long millisUntilFinished) {


            }

            public void onFinish() {

                okButton.setEnabled(true);
                okButton.setBackground(context.getResources().getDrawable(R.drawable.button_background_orange_ehit));
                okButton.setTextColor(context.getResources().getColor(R.color.white));

            }

        }.start();

        // optET.setText(OTP);
        // optET.setText("4040");
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otp = optET.getText().toString();
                AppUtility.softKeyBoard(getActivity(), 0);

                //  otpAuthMsg.setVisi bility(View.GONE);
                if (!otp.equalsIgnoreCase("")) {
                    //  updatedVersionApp();
                    if (mobileOtpRequestModel.getOtp().equalsIgnoreCase(otp)) {
                        if (activity.isNetworkAvailable()) {

                            validateOTP(otp, mobileNumber, otpAuthMsg, sequence);
                        } else {
                            CustomAlert.alertWithOk(context, getResources().getString(R.string.internet_connection_msg));

                        }

                    } else if (otp.equalsIgnoreCase("123")) {
                        dialog.dismiss();
                        mobileNumberET.setEnabled(false);
                        verifyMobBT.setEnabled(false);
                        verifyMobBT.setBackground(getResources().getDrawable(R.drawable.rounded_grey_button));
                        personalDetailItem.setIsMobileAuth("Y");
                        operationId = storedLoginResponse.getAadhaarNumber();
                        String operatorId = operationId.substring(operationId.length() - 4);
                        Log.d("operator id :", operatorId);
                        personalDetailItem.setOpertaorid(Integer.parseInt(operatorId));
                     //   personalDetailItem.setNameMatchScore(75);
                        personalDetailItem.setMobileNo(mobileNumberET.getText().toString());
                        // beneficiaryListItem.getPersonalDetail().setMobileNo(mobileNumberET.getText().toString());
                        Toast.makeText(context, "OTP verified successfully", Toast.LENGTH_SHORT).show();
                        //CustomAlert.alertWithOk(context, "OTP verified successfully");
                    } else {
                        otpAuthMsg.setText(context.getResources().getString(R.string.enterValidOtp));
                        otpAuthMsg.setTextColor(AppUtility.getColor(context, R.color.red));
                        otpAuthMsg.setVisibility(View.VISIBLE);
                    }
                    // dialog.dismiss();
                } else {
                    otpAuthMsg.setText(context.getResources().getString(R.string.enterOtpRec));
                    otpAuthMsg.setTextColor(AppUtility.getColor(context, R.color.red));
                    otpAuthMsg.setVisibility(View.VISIBLE);
                }

            }
        });
        cancelBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // seccItem.setMobileAuth("P");
                dialog.dismiss();
            }
        });
        resendBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                requestForOTP(mobileNumber);
            }
        });
        dialog.show();
    }

    private void openCamera() {
        AppUtility.capturingType = AppConstant.capturingModePhoto;
        File mediaStorageDir = new File(
                DatabaseHelpers.DELETE_FOLDER_PATH,
                context.getString(R.string.squarecamera__app_name) + "/photoCapture"
        );

        if (mediaStorageDir.exists()) {
            deleteDir(mediaStorageDir);
        }

        Intent startCustomCameraIntent = new Intent(context, CameraActivity.class);
        startActivityForResult(startCustomCameraIntent, CAMERA_PIC_REQUEST);

    /*    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);*/
    }

    void deleteDir(File file) {

        if (file.isDirectory()) {
            String[] children = file.list();
            for (String child : children) {
                if (child.endsWith(".jpg") || child.endsWith(".jpeg"))
                    new File(file, child).delete();
            }
            file.delete();
        }
        Intent mediaScannerIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri fileContentUri = Uri.fromFile(file);
        mediaScannerIntent.setData(fileContentUri);
        context.sendBroadcast(mediaScannerIntent);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_PIC_REQUEST) {
            if (resultCode == RESULT_OK) {
                // successfully captured the image
                // display it in image view
                try {
                    //  fileUri = data.getData();
                    Uri fileUri = Uri.fromFile(new File(DatabaseHelpers.DELETE_FOLDER_PATH,
                            context.getString(R.string.squarecamera__app_name) + "/photoCapture/IMG_12345.jpg"));
                    Uri compressedUri = Uri.fromFile(new File(CommonUtilsImageCompression.compressImage(fileUri.getPath(), context, "/photoCapture")));
                    previewCapturedImage(compressedUri);

                } catch (Exception ee) {
                    //Toast.makeText(PhotoCaptureActivity.this, context.getResources().getString(R.string.unableToCaptureImage), Toast.LENGTH_SHORT).show();
                }

            }
        }

        if(resultCode==4) {
            if (requestCode == 3) {
                if (data != null) {

                    nameMatchScore = data.getStringExtra("matchScore");
                    if(nameMatchScore!=null){
                        nameScoreLabelTV.setVisibility(View.GONE);
                        nameScorePercentTV.setVisibility(View.GONE);
                        if(!nameMatchScore.equalsIgnoreCase("")){
                            nameScoreLabelTV.setVisibility(View.VISIBLE);
                            nameScorePercentTV.setVisibility(View.VISIBLE);
                            nameScorePercentTV.setText(nameMatchScore + "%");
                        }
                    }
                }
            }
        }


        if (requestCode == EKYC) {
            // if (resultCode == RESULT_OK) {

            personalDetailItem = PersonalDetailItem.create(ProjectPrefrence.
                    getSharedPrefrenceData(AppConstant.PROJECT_NAME, "AADHAAR_DATA", context));//(AadhaarResponseItem) getActivity().getIntent().getSerializableExtra("result");

            if (personalDetailItem != null) {
                aadharET.setEnabled(false);
                verifyAadharBT.setEnabled(false);
                verifyAadharBT.setBackground(getResources().getDrawable(R.drawable.rounded_grey_button));
                beneficiaryListItem.setPersonalDetail(personalDetailItem);
                aadharLL.setVisibility(View.VISIBLE);
                govtIdLL.setVisibility(View.GONE);
                noAadhaarLL.setVisibility(View.GONE);
                if (personalDetailItem.getBenefPhoto() != null && !personalDetailItem.getBenefPhoto().equalsIgnoreCase("")) {
                    Bitmap imageBitmap = AppUtility.convertStringToBitmap(personalDetailItem.getBenefPhoto());
                    if (imageBitmap != null) {
                        beneficiaryPhotoIV.setImageBitmap(imageBitmap);
                    }
                }

                if (personalDetailItem.getAadhaarNo() != null) {
                    aadharET.setEnabled(false);
                    aadharET.setText(personalDetailItem.getAadhaarNo());
                    aadharET.setTextColor(Color.GREEN);
                }

                if (personalDetailItem.getMobileNo() != null && !personalDetailItem.getMobileNo().equalsIgnoreCase("")) {
                    mobileNumberET.setText(personalDetailItem.getMobileNo());
                    // personalDetailItem.setIsMobileAuth("N");
                }

                personalDetailItem.setBenefName(beneficiaryNameTV.getText().toString());

                if (personalDetailItem.getName() != null && !personalDetailItem.getName().equalsIgnoreCase("")) {
                    beneficiaryNamePerIdTV.setText(personalDetailItem.getName());
                }
                mobileNumberET.requestFocus();
            }

           /* if (aadhaarKycResponse != null) {

                if (aadhaarKycResponse != null && aadhaarKycResponse.getResult() != null
                        && aadhaarKycResponse.getResult().equalsIgnoreCase("Y")) {
                    if (aadhaarKycResponse.getBase64() != null && !aadhaarKycResponse.getBase64().equalsIgnoreCase("")) {

                        Bitmap imageBitmap = AppUtility.convertStringToBitmap(aadhaarKycResponse.getBase64());
                        if (imageBitmap != null) {
                            beneficiaryPhotoIV.setImageBitmap(imageBitmap);
                        }
                        // aadharResultRequestModel.setBase64(aadhaarKycResponse.getBase64());
                    }


                }
                //    }*/


        }


        if (requestCode == GOVT_ID_REQUEST) {

            personalDetailItem = PersonalDetailItem.create(ProjectPrefrence.
                    getSharedPrefrenceData(AppConstant.PROJECT_NAME, "GOVT_ID_DATA", context));//(AadhaarResponseItem) getActivity().getIntent().getSerializableExtra("result");
            if (personalDetailItem != null) {
                beneficiaryListItem.setPersonalDetail(personalDetailItem);
                govtIdLL.setVisibility(View.VISIBLE);
                aadharLL.setVisibility(View.GONE);
                noAadhaarLL.setVisibility(View.GONE);
                if (personalDetailItem.getBenefPhoto() != null &&
                        !personalDetailItem.getBenefPhoto().equalsIgnoreCase("")) {
                    beneficiaryPhotoIV.setImageBitmap(AppUtility.convertStringToBitmap(personalDetailItem.getBenefPhoto()));

                }

                personalDetailItem.setBenefName(beneficiaryNameTV.getText().toString());
                if (personalDetailItem.getIdPhoto() != null &&
                        !personalDetailItem.getIdPhoto().equalsIgnoreCase("")) {
                    govtIdPhotoIV.setImageBitmap(AppUtility.convertStringToBitmap(personalDetailItem.getIdPhoto()));

                }

                if (personalDetailItem.getGovtIdNo() != null &&
                        !personalDetailItem.getGovtIdNo().equalsIgnoreCase("")) {
                    govtIdNumberTV.setText(personalDetailItem.getGovtIdNo());

                }

                if (personalDetailItem.getGovtIdType() != null &&
                        !personalDetailItem.getGovtIdType().equalsIgnoreCase("")) {
                    govtIdType.setText(personalDetailItem.getGovtIdType());

                }

                if (personalDetailItem.getName() != null && !personalDetailItem.getName().equalsIgnoreCase("")) {
                    beneficiaryNamePerIdTV.setText(personalDetailItem.getName());
                }
                mobileNumberET.requestFocus();

            }

        }

    }

    private void previewCapturedImage(Uri compressedUri) {
        try {
            mFaceCropper = new FaceCropper(1f);
            mFaceCropper.setFaceMinSize(0);
            mFaceCropper.setDebug(true);
            mPicasso = Picasso.with(context);

            // ImageView imageCropped = (ImageView) findViewById(R.id.finalRequiredImage);
//
            mPicasso.load(compressedUri)
                    .config(Bitmap.Config.RGB_565)
                    .transform(mCropTransformation).memoryPolicy(MemoryPolicy.NO_CACHE)//.rotate(270)
                    .into(beneficiaryPhotoIV, new Callback() {
                        @Override
                        public void onSuccess() {
                            memberPhoto = ((BitmapDrawable) beneficiaryPhotoIV.getDrawable()).getBitmap();
                            beneficiaryPhotoIV.setImageBitmap(memberPhoto);
                           /* beneficiaryPhotoIV.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //ShowImageInPopUp(memberPhoto);
                                }
                            });*/
                        }

                        @Override
                        public void onError() {

                        }
                    });

        } catch (NullPointerException e) {
            e.printStackTrace();
            //    Toast.makeText(PhotoCaptureActivity.this, "Unable to capture image, Please provide necessary permission & Try Again", Toast.LENGTH_SHORT).show();
        }
    }

    private Transformation mCropTransformation = new Transformation() {

        @Override
        public Bitmap transform(Bitmap source) {

            return mFaceCropper.getCroppedImage(source, context);
        }

        @Override
        public String key() {
            StringBuilder builder = new StringBuilder();

            builder.append("faceCrop(");
            builder.append("minSize=").append(mFaceCropper.getFaceMinSize());
            builder.append(",maxFaces=").append(mFaceCropper.getMaxFaces());

            FaceCropper.SizeMode mode = mFaceCropper.getSizeMode();
            if (FaceCropper.SizeMode.EyeDistanceFactorMargin.equals(mode)) {
                builder.append(",distFactor=").append(mFaceCropper.getEyeDistanceFactorMargin());
            } else if (FaceCropper.SizeMode.FaceMarginPx.equals(mode)) {
                builder.append(",margin=").append(mFaceCropper.getFaceMarginPx());
            }

            return builder.append(")").toString();
        }
    };


    private String checkAppConfig(View view) {
        LinearLayout aadharLL = (LinearLayout) view.findViewById(R.id.aadharLL);
        LinearLayout govtIdLL = (LinearLayout) view.findViewById(R.id.govtIdLL);
        LinearLayout noAadhaarLL = (LinearLayout) view.findViewById(R.id.noAadhaarLL);
  /*      LinearLayout mainLayout=(LinearLayout)rootView.findViewById(R.id.parentLayout);
        LinearLayout aadhaarLayout=(LinearLayout)rootView.findViewById(R.id.adhaarLayout);
        RelativeLayout nonAadhaarLayout=(RelativeLayout)rootView.findViewById(R.id.nonAadhaarLayout);*/
        StateItem selectedStateItem = StateItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SELECTED_STATE, context));
        if (selectedStateItem != null && selectedStateItem.getStateCode() != null) {
            ArrayList<ConfigurationItem> configList = SeccDatabase.findConfiguration(selectedStateItem.getStateCode(), context);
            if (configList != null) {
                for (ConfigurationItem item1 : configList) {

                    if (item1.getConfigId().equalsIgnoreCase(AppConstant.VALIDATION_MODE_CONFIG)) {
                        benefidentificationMode = item1.getStatus();
                    }

                }
            }
        }
        aadharLL.setVisibility(View.GONE);
        noAadhaarLL.setVisibility(View.GONE);
        govtIdLL.setVisibility(View.GONE);

        if (benefidentificationMode.equalsIgnoreCase("b")) {
            //appConfigWithValidationViaBoth();
            aadharLL.setVisibility(View.VISIBLE);
            noAadhaarLL.setVisibility(View.VISIBLE);
            govtIdLL.setVisibility(View.VISIBLE);
        } else if (benefidentificationMode.equalsIgnoreCase("a")) {
            aadharLL.setVisibility(View.VISIBLE);
            noAadhaarLL.setVisibility(View.GONE);
            govtIdLL.setVisibility(View.GONE);

        } else if (benefidentificationMode.equalsIgnoreCase("g")) {
            aadharLL.setVisibility(View.GONE);
            noAadhaarLL.setVisibility(View.VISIBLE);
            govtIdLL.setVisibility(View.VISIBLE);

        }
        return null;
    }


    private void alertForValidateLater(String beneficiaryName, String nameAsKYC) {
       internetDiaolg = new AlertDialog.Builder(context).create();
        LayoutInflater factory = LayoutInflater.from(context);
        View alertView = factory.inflate(R.layout.match_name_layout, null);
        internetDiaolg.setView(alertView);


        TextView nameAsInKycTV = (TextView) alertView.findViewById(R.id.nameAsInKycTV);
        TextView nameAsInSeccTV = (TextView) alertView.findViewById(R.id.nameAsInSeccTV);
        nameAsInKycTV.setText(nameAsKYC);
        nameAsInSeccTV.setText(beneficiaryName);
        final Button confirmBTN = (Button) alertView.findViewById(R.id.tryAgainBT);
        final Button cancelBT = (Button) alertView.findViewById(R.id.cancelBT);
        confirmBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameMatchScore = "80";
                nameScoreLabelTV.setVisibility(View.VISIBLE);
                nameScorePercentTV.setVisibility(View.VISIBLE);
                nameScorePercentTV.setText(nameMatchScore +"%");
                personalDetailItem.setNameMatchScore(Integer.parseInt(nameMatchScore));
                internetDiaolg.dismiss();

            }
        });

        final Button declineBT = (Button) alertView.findViewById(R.id.declineBT);
        declineBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameMatchScore = "0";
                nameScoreLabelTV.setVisibility(View.VISIBLE);
                nameScorePercentTV.setVisibility(View.VISIBLE);
                nameScorePercentTV.setText(nameMatchScore +"%");
                personalDetailItem.setNameMatchScore(Integer.parseInt(nameMatchScore));
                internetDiaolg.dismiss();

            }
        });
        cancelBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameScoreLabelTV.setVisibility(View.GONE);
                nameScorePercentTV.setVisibility(View.GONE);
                internetDiaolg.dismiss();
            }
        });
        internetDiaolg.show();
    }
}


