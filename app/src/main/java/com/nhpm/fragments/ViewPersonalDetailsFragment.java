package com.nhpm.fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.nhpm.Models.request.GetMemberDetail;
import com.nhpm.Models.request.MobileOtpRequest;
import com.nhpm.Models.request.PersonalDetailItem;
import com.nhpm.Models.response.DocsListItem;
import com.nhpm.Models.response.MobileOTPResponse;
import com.nhpm.Models.response.PersonalDetailResponse;
import com.nhpm.Models.response.master.ConfigurationItem;
import com.nhpm.Models.response.master.StateItem;
import com.nhpm.Models.response.verifier.AadhaarResponseItem;
import com.nhpm.Models.response.verifier.VerifierLoginResponse;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;
import com.nhpm.Utility.AppUtility;
import com.nhpm.Utility.ApplicationGlobal;
import com.nhpm.Utility.Verhoeff;
import com.nhpm.activity.CollectDataActivity;
import com.nhpm.activity.EkycActivity;
import com.nhpm.activity.GovermentIDActivity;
import com.nhpm.activity.ViewMemberDataActivity;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewPersonalDetailsFragment extends Fragment {
    private View view;
    private AadhaarResponseItem aadhaarKycResponse;
    private String benefidentificationMode = "";
    private AlertDialog dialog;
    private Context context;
    private EkycActivity ekycActivity;
    private int CAMERA_PIC_REQUEST = 0;
    private int EKYC = 1;
    private int GOVT_ID_REQUEST = 2;
    private Picasso mPicasso;
    private ImageView govtIdPhotoIV;
    private TextView govtIdType, govtIdNumberTV,nameMatchScoreTV;
    private LinearLayout govtIdLL;
    private ViewMemberDataActivity activity;
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
    private TextView beneficiaryNameTV, beneficiaryNamePerIdTV, noAadhaarTV;
    private DocsListItem beneficiaryListItem;
    private GetMemberDetail getMemberDetail;
    private PersonalDetailResponse personalDetailItem;
    private String status = "";
    private LinearLayout aadharLL, noAadhaarLL;
    private Button matchBT;
    private VerifierLoginResponse storedLoginResponse;

    public AadhaarResponseItem getAadhaarKycResponse() {
        return aadhaarKycResponse;
    }

    public void setAadhaarKycResponse(AadhaarResponseItem aadhaarKycResponse) {
        this.aadhaarKycResponse = aadhaarKycResponse;
    }

    public ViewPersonalDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_view_personal_details, container, false);
        context = getActivity();
        setupScreen(view);
        return view;

    }

    private void setupScreen(View view) {
        checkAppConfig(view);
        storedLoginResponse = VerifierLoginResponse.create(
                ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.VERIFIER_CONTENT, context));

        personalDetailItem = activity.getMemberDetailItem.getPersonalDetail();
        matchBT = (Button) view.findViewById(R.id.matchBT);
        nameMatchScoreTV = (TextView) view.findViewById(R.id.nameMatchScoreTV);
        noAadhaarTV = (TextView) view.findViewById(R.id.noAadhaarTV);
        aadharLL = (LinearLayout) view.findViewById(R.id.aadharLL);
        govtIdLL = (LinearLayout) view.findViewById(R.id.govtIdLL);
        noAadhaarLL = (LinearLayout) view.findViewById(R.id.noAadhaarLL);
        noAadhaarLL.setVisibility(View.GONE);
        govtIdNumberTV = (TextView) view.findViewById(R.id.govtIdNumberTV);
        govtIdType = (TextView) view.findViewById(R.id.govtIdType);
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


        if (personalDetailItem != null) {
            if (personalDetailItem.getBenefName() != null && !personalDetailItem.getBenefName().equalsIgnoreCase("")) {
                beneficiaryNameTV.setText(personalDetailItem.getName());
            }

            if (personalDetailItem.getName() != null && !personalDetailItem.getName().equalsIgnoreCase("")) {
                beneficiaryNamePerIdTV.setText(personalDetailItem.getBenefName());
            }
            if(personalDetailItem.getBenefPhoto()!=null && !personalDetailItem.getBenefPhoto().equalsIgnoreCase("")){
                beneficiaryPhotoIV.setImageBitmap(AppUtility.convertStringToBitmap(personalDetailItem.getBenefPhoto()));
            }

            if(personalDetailItem.getMobileNo()!=null &&
                    !personalDetailItem.getMobileNo().equalsIgnoreCase("")){
                mobileNumberET.setText(personalDetailItem.getMobileNo());
            }

            if(personalDetailItem.getNameMatchScore()!=null){
                nameMatchScoreTV.setText(personalDetailItem.getNameMatchScore()+"%");
            }

            if (personalDetailItem.getIsAadhar() != null) {
                if (personalDetailItem.getIsAadhar().equalsIgnoreCase("Y")) {
                    govtIdLL.setVisibility(View.GONE);
                    aadharLL.setVisibility(View.VISIBLE);

                    if(personalDetailItem.getGovtIdNo()!=null &&
                            !personalDetailItem.getGovtIdNo().equalsIgnoreCase("")){
                        aadharET.setText(personalDetailItem.getGovtIdNo());
                    }

                } else {
                    govtIdLL.setVisibility(View.VISIBLE);
                    aadharLL.setVisibility(View.GONE);
                    if(personalDetailItem.getGovtIdNo()!=null &&
                            !personalDetailItem.getGovtIdNo().equalsIgnoreCase("")){
                        govtIdNumberTV.setText(personalDetailItem.getGovtIdNo());
                    }

                    if(personalDetailItem.getGovtIdType()!=null &&
                            !personalDetailItem.getGovtIdType().equalsIgnoreCase("")){
                        govtIdType.setText(personalDetailItem.getGovtIdType());
                    }

                    if(personalDetailItem.getIdPhoto()!=null &&
                            !personalDetailItem.getIdPhoto().equalsIgnoreCase("")){
                        govtIdPhotoIV.setImageBitmap(AppUtility.convertStringToBitmap(personalDetailItem.getIdPhoto()));
                    }
                }
            }
        }




        nextBT = (Button) view.findViewById(R.id.nextBT);
        nextBT.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                activity.personalDetailsLL.setBackground(context.getResources().getDrawable(R.drawable.arrow));
                activity.familyDetailsLL.setBackground(context.getResources().getDrawable(R.drawable.arrow_yellow));
                Fragment fragment = new ViewFamilyDetailsFragment();
                callFragment(fragment);
            }
        });
    }

    public void callFragment(Fragment fragment) {
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
        activity = (ViewMemberDataActivity) context;
        getMemberDetail = activity.getMemberDetailItem;

    }

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

}


