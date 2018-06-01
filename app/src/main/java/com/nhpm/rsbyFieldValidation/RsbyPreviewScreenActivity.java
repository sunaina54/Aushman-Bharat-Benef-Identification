package com.nhpm.rsbyFieldValidation;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.customComponent.CustomAlert;
import com.customComponent.utility.ProjectPrefrence;
import com.nhpm.LocalDataBase.dto.SeccDatabase;
import com.nhpm.Models.response.GovernmentIdItem;
import com.nhpm.Models.response.RelationItem;
import com.nhpm.Models.response.WhoseMobileItem;
import com.nhpm.Models.response.master.HealthSchemeItem;
import com.nhpm.Models.response.master.MemberStatusItem;
import com.nhpm.Models.response.rsbyMembers.RSBYItem;
import com.nhpm.Models.response.rsbyMembers.RsbyHouseholdItem;
import com.nhpm.Models.response.seccMembers.SelectedMemberItem;
import com.nhpm.Models.response.verifier.FamilyStatusItem;
import com.nhpm.Models.response.verifier.VerifierLocationItem;
import com.nhpm.Models.response.verifier.VerifierLoginResponse;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;
import com.nhpm.Utility.AppUtility;
import com.nhpm.activity.BaseActivity;

import java.util.ArrayList;

import pl.polidea.view.ZoomView;

/**
 * Created by DELL on 26-02-2017.
 */

public class RsbyPreviewScreenActivity extends BaseActivity {
    private String TAG = "PreviewActivity";
    private SelectedMemberItem selectedMember;
    private ImageView memberPhotoIV, govtIdIV;
    private TextView nameTV, fatherNameTV, motherNameTV, relationTV, houseHoldStatTV, memberStatusTV, prevHouseholdIDTV;
    private TextView aadhaarNumberTV, nameAsAadhaarTV, prevCaptureMode, hofNameTV;//aadhaarStatusTV
    private ImageView aadharStatusIV;
    private TextView govtIDTV, govtIdTypeTV, nameAsIdTV;
    private TextView mobileNoTV, whoseMobileTV, mobileStatusTV;
    private TextView urnNoTV, healthSchemeTV, schemIdTV;
    private TextView reqNameTV, reqFatherNameTV, reqMotherNameTV,
            reqRelationTV, reqDobTV, reqGenderTV, reqMaritalTV,
            reqOccupTV, prevNomineeNameTV, prevNomineeRelTV, addressTV, prevGenderTV, prevDOB, prevNameInRegionalTV;
    ;

    private Button confrimBT;
    // private RSBYItem rsbyItem;
    private RSBYItem rsbyItem;
    private Context context;
    private TextView headerTV;
    private ArrayList<FamilyStatusItem> householdStatusList;
    private ArrayList<MemberStatusItem> memberStatusList;
    private RelativeLayout backLayout;
    private ImageView backIV;
    private ArrayList<RelationItem> relationList;
    private LinearLayout memberPhotoLayout, aadhaarStatusLayout,
            govtIdStatusLayout, mobileNoStatusLayout, addSchemeDetLayout;
    private LinearLayout healthSchemeLayout, urnNoLayout;
    private RelativeLayout healthSchemeheaderLayout;
    private LinearLayout nomineeDetailLayout;
    private AlertDialog askForPinDailog, lockDialog;
    private LinearLayout mZoomLinearLayout;
    private ZoomView zoomView;
    private VerifierLocationItem selectedLocation;


    private int wrongPinCount = 0;
    private long wrongPinSavedTime;
    private long currentTime;
    private TextView wrongAttempetCountValue, wrongAttempetCountText;
    private long millisecond24 = 86400000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //   setContentView(R.layout.activity_preview);
        setContentView(R.layout.dummy_layout_for_zooming);
        setupScreen();
    }

    private void setupScreen() {
        context = this;
        mZoomLinearLayout = (LinearLayout) findViewById(R.id.mZoomLinearLayout);
        View v = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.activity_preview, null, false);
        v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
        selectedLocation = VerifierLocationItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.SELECTED_BLOCK, context));
        selectedMember = SelectedMemberItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.SELECTED_ITEM_FOR_VERIFICATION, context));
        householdStatusList = SeccDatabase.getFamilyStatusList(context);
        memberStatusList = SeccDatabase.getMemberStatusList(context);
        relationList = SeccDatabase.getRelationList(context);
        prevHouseholdIDTV = (TextView) v.findViewById(R.id.prevHouseholdIDTV);
        addressTV = (TextView) v.findViewById(R.id.prevAddressTV);
        prevNomineeNameTV = (TextView) v.findViewById(R.id.prevNomineeNameTV);
        prevNomineeRelTV = (TextView) v.findViewById(R.id.prevNomineeRelation);
        memberPhotoLayout = (LinearLayout) v.findViewById(R.id.memberPhotoLayout);
        aadhaarStatusLayout = (LinearLayout) v.findViewById(R.id.adhaarStatusLayout);
        govtIdStatusLayout = (LinearLayout) v.findViewById(R.id.govtIdStatusLayout);
        mobileNoStatusLayout = (LinearLayout) v.findViewById(R.id.mobileNoStatusLayout);
        addSchemeDetLayout = (LinearLayout) v.findViewById(R.id.add_scheme_det_layout);
        healthSchemeLayout = (LinearLayout) v.findViewById(R.id.stateHealthSchemeLayout);
        healthSchemeheaderLayout = (RelativeLayout) v.findViewById(R.id.healthSchemeheaderLayout);
        urnNoLayout = (LinearLayout) v.findViewById(R.id.urnNumberLayout);
        nomineeDetailLayout = (LinearLayout) v.findViewById(R.id.nomineeDetailLayout);
        prevDOB = (TextView) v.findViewById(R.id.prevDobTV);
        hofNameTV = (TextView) v.findViewById(R.id.prevHofTV);
        prevGenderTV = (TextView) v.findViewById(R.id.prevGenderTV);
        prevNameInRegionalTV = (TextView) v.findViewById(R.id.prevNameInReginalTV);

        headerTV = (TextView) v.findViewById(R.id.centertext);
        memberPhotoIV = (ImageView) v.findViewById(R.id.prevMemberPhotoIV);
        govtIdIV = (ImageView) v.findViewById(R.id.idPhotoIV);
        nameTV = (TextView) v.findViewById(R.id.prevNameTV);
        fatherNameTV = (TextView) v.findViewById(R.id.prevFatherNameTV);
        motherNameTV = (TextView) v.findViewById(R.id.prevMotherNameTV);
        relationTV = (TextView) v.findViewById(R.id.prevRelationTV);
        houseHoldStatTV = (TextView) v.findViewById(R.id.prevHouseHoldStatusTV);
        memberStatusTV = (TextView) v.findViewById(R.id.prevMemberStatusTV);
        aadhaarNumberTV = (TextView) v.findViewById(R.id.prevAadhaarNoTV);
        // aadhaarStatusTV=(TextView)v.findViewById(R.id.prevAdhaarStatusTV);
        nameAsAadhaarTV = (TextView) v.findViewById(R.id.prevNameAsAadhaarTV);
        prevCaptureMode = (TextView) v.findViewById(R.id.prevCaptureMode);
        aadharStatusIV = (ImageView) v.findViewById(R.id.aadharStatusIV);
        govtIDTV = (TextView) v.findViewById(R.id.prevGovtIdNoTV);
        nameAsIdTV = (TextView) v.findViewById(R.id.prevGovtNameAsIdTV);
        govtIdTypeTV = (TextView) v.findViewById(R.id.prevGovtIdTypeTV);
        mobileNoTV = (TextView) v.findViewById(R.id.prevMobileNoTV);
        mobileStatusTV = (TextView) v.findViewById(R.id.prevMobileStatTV);
        whoseMobileTV = (TextView) v.findViewById(R.id.prevWhoseMobileTV);
        urnNoTV = (TextView) v.findViewById(R.id.prevURNTV);
        //healthSchemeTV=(TextView)v.findViewById(R.id.prevStateHealthTV);
        //  schemIdTV=(TextView)v.findViewById(R.id.prevStateHealthIDTV);
        reqNameTV = (TextView) v.findViewById(R.id.reqNameTV);
        reqFatherNameTV = (TextView) v.findViewById(R.id.reqFatherNameTV);
        reqMotherNameTV = (TextView) v.findViewById(R.id.reqMotherNameTV);
        reqRelationTV = (TextView) v.findViewById(R.id.reqRelationTV);
        reqDobTV = (TextView) v.findViewById(R.id.reqDateOfBirthTV);
        reqGenderTV = (TextView) v.findViewById(R.id.reqGenderTV);
        reqMaritalTV = (TextView) v.findViewById(R.id.reqMaritalStatTV);
        reqOccupTV = (TextView) v.findViewById(R.id.reqOccupationTV);
        confrimBT = (Button) v.findViewById(R.id.confirmBT);
        confrimBT.setVisibility(View.VISIBLE);
        backLayout = (RelativeLayout) v.findViewById(R.id.backLayout);
        backIV = (ImageView) v.findViewById(R.id.back);
        //relationList=SeccDatabase.getRelationList(context);

        zoomView = new ZoomView(this);
        zoomView.addView(v);
        mZoomLinearLayout.addView(zoomView);

        if (selectedMember.getRsbyMemberItem() != null) {
            rsbyItem = selectedMember.getRsbyMemberItem();

            if (rsbyItem.getName() != null)
                headerTV.setText(rsbyItem.getName());

            previewVerfication();


            for (int i = 0; i < AppUtility.getMaritalStatusCode().size(); i++) {
            /*    if(rsbyItem.getReqMarritalStatCode()!=null && rsbyItem.getReqMarritalStatCode().equalsIgnoreCase(AppUtility.getMaritalStatusCode().get(i))){
                    reqMaritalTV.setText(AppUtility.getMaritalStatusLabel().get(i));
                    break;
                }*/
            }


            for (RelationItem item : relationList) {
              /*  if(rsbyItem.getReqGenderCode()!=null && rsbyItem.getReqRelationCode().equalsIgnoreCase(item.getRelationCode())){
                    reqRelationTV.setText(item.getRelationName());
                    break;
                }*/
            }

        } else if (selectedMember.getRsbyMemberItem() != null) {
            rsbyItem = selectedMember.getRsbyMemberItem();
        }
        confrimBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  lockPrompt();
                //askPinToLock();
                alertForConsent(context);
            }
        });
        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent theIntent;
                finish();
                rightTransition();
            }
        });
        backLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backIV.performClick();
            }
        });

    }

    private void previewVerfication() {

        boolean aadhaarflag = false, govtIdFlag = false;
       /* if(rsbyItem.getAhlslnohhd()!=null){
            prevHouseholdIDTV.setText(rsbyItem.getAhlslnohhd());
        }*/
        if (rsbyItem.getName() != null)
            nameTV.setText(rsbyItem.getName());
      /*  if(rsbyItem.get()!=null)
            fatherNameTV.setText(rsbyItem.getFathername());*/
        /*if(rsbyItem.getMothername()!=null)
            motherNameTV.setText(rsbyItem.get());*/
        if (rsbyItem.getNhpsRelationName() != null)
            relationTV.setText(rsbyItem.getNhpsRelationName());

        /*if(rsbyItem.getNameSl()!=null)
            prevNameInRegionalTV.setText(rsbyItem.getNameSl());*/
        if (rsbyItem.getDob() != null)
            prevDOB.setText(AppUtility.convertRsbyDate(rsbyItem.getDob()));

        if (rsbyItem.getGender() != null) {
            if (rsbyItem.getGender().equalsIgnoreCase("1")) {
                prevGenderTV.setText("Male");
            } else if (rsbyItem.getGender().equalsIgnoreCase("2")) {
                prevGenderTV.setText("Female");
            } else if (rsbyItem.getGender().equalsIgnoreCase("3")) {
                prevGenderTV.setText("Other");
            }
        }

        String address = "";
        /*if(rsbyItem.getA()!=null){
            address=address+""+rsbyItem.getAddressline1();
        }
        if(rsbyItem.getAddressline2()!=null){
            address=address+","+rsbyItem.getAddressline2();
        }
        if(rsbyItem.getAddressline3()!=null){
            address=address+","+rsbyItem.getAddressline3();
        }
        if(rsbyItem.getAddressline4()!=null){
            address=address+","+rsbyItem.getAddressline4();
        }*/
        addressTV.setText(address);
        RSBYItem hofItem = AppUtility.findRsbyHof(rsbyItem, context);
        if (hofItem != null) {
            hofNameTV.setText(hofItem.getName());
        }
        if (rsbyItem.getNhpsRelationCode() != null) {
            if (relationList != null) {
                for (RelationItem item : relationList) {
                    if (item.getRelationCode().equalsIgnoreCase(rsbyItem.getNhpsRelationCode())) {
                        relationTV.setText(item.getRelationName());
                        break;
                    }
                }
            }
        }
        memberPhotoLayout.setVisibility(View.GONE);

        if (rsbyItem.getMemberPhoto1() != null && !rsbyItem.getMemberPhoto1().equalsIgnoreCase("")) {
            memberPhotoLayout.setVisibility(View.VISIBLE);
            try {
                memberPhotoIV.setImageBitmap(AppUtility.convertStringToBitmap(rsbyItem.getMemberPhoto1()));
                memberPhotoIV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ShowImageInPopUp(AppUtility.convertStringToBitmap(rsbyItem.getMemberPhoto1()));
                    }
                });
            } catch (Exception e) {
            }
        } else {
        }

        //  aadhaarStatusLayout.setVisibility(View.VISIBLE);
        // govtIdStatusLayout.setVisibility(View.VISIBLE);
        if (rsbyItem.getAadhaarStatus() != null && rsbyItem.getAadhaarStatus().equalsIgnoreCase("1")) {
            aadhaarflag = true;
            govtIdFlag = false;
            if (rsbyItem.getAadhaarNo() != null && !rsbyItem.getAadhaarNo().equalsIgnoreCase("")) {
                Log.d("Preview ", "Aadhaar Number : " + rsbyItem.getAadhaarNo());
                aadhaarStatusLayout.setVisibility(View.VISIBLE);
                String aadhaarNo = "XXXXXXXX" + rsbyItem.getAadhaarNo().substring(8);
                aadhaarNumberTV.setText(aadhaarNo);
             /*   if (rsbyItem.getAadhaarAuth() != null)
                    aadhaarStatusTV.setText(rsbyItem.getAadhaarAuth());*/
                if (rsbyItem.getAadhaarAuth() != null)
                    //aadhaarStatusTV.setText(rsbyItem.getAadhaarAuth());
                    if (rsbyItem.getAadhaarAuth().equalsIgnoreCase(AppConstant.AADHAAR_AUTH_YES)) {
                        aadharStatusIV.setVisibility(View.VISIBLE);
                        aadharStatusIV.setImageDrawable(context.getResources().getDrawable(R.drawable.right_tick));
                    } else {
                        aadharStatusIV.setVisibility(View.VISIBLE);
                        aadharStatusIV.setImageDrawable(context.getResources().getDrawable(R.drawable.exclamation));
                    }
                if (rsbyItem.getNameAadhaar() != null)
                    nameAsAadhaarTV.setText(rsbyItem.getNameAadhaar());
                if (rsbyItem.getAadhaarCapturingMode() != null && rsbyItem.getAadhaarCapturingMode().trim().equalsIgnoreCase(AppConstant.QR_CODE_MODE)) {
                    //  prevCaptureMode.setText(rsbyItem.getAadhaarCapturingMode());
                    prevCaptureMode.setText("QR");
                } else if (rsbyItem.getAadhaarCapturingMode() != null && rsbyItem.getAadhaarCapturingMode().trim().equalsIgnoreCase(AppConstant.MANUAL_MODE)) {
                    prevCaptureMode.setText(rsbyItem.getAadhaarCapturingMode());
                }
            }
        } else {
            govtIdIV.setVisibility(View.VISIBLE);

            if (rsbyItem.getIdType() != null && !rsbyItem.getIdNo().equalsIgnoreCase("")) {
                govtIdStatusLayout.setVisibility(View.VISIBLE);
                govtIdFlag = true;
                aadhaarflag = false;
                for (GovernmentIdItem item : AppUtility.prepareGovernmentIdSpinner()) {
                    if (rsbyItem.getIdType().equalsIgnoreCase(item.statusCode + "")) {
                        govtIdTypeTV.setText(item.status);
                        break;
                    }
                }
                if (rsbyItem.getIdNo() != null)
                    govtIDTV.setText(rsbyItem.getIdNo());
                if (rsbyItem.getNameAsId() != null) {
                    nameAsIdTV.setText(rsbyItem.getNameAsId());
                }
            }
            if (rsbyItem.getGovtIdPhoto() != null && !rsbyItem.getGovtIdPhoto().equalsIgnoreCase("enrollement")) {
                govtIdIV.setVisibility(View.VISIBLE);
                try {
                    govtIdIV.setImageBitmap(AppUtility.convertStringToBitmap(rsbyItem.getGovtIdPhoto()));
                    govtIdIV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ShowImageInPopUp(AppUtility.convertStringToBitmap(rsbyItem.getGovtIdPhoto()));
                        }
                    });
                } catch (Exception e) {

                }
            } else {

            }

        }
        mobileNoStatusLayout.setVisibility(View.GONE);
       /* if(rsbyItem.getMobileNo()!=null && !rsbyItem.getMobileNo().equalsIgnoreCase("")) {
            mobileNoStatusLayout.setVisibility(View.VISIBLE);
            String mobileNo="XXXXXX"+rsbyItem.getMobileNo().substring(6);
            mobileNoTV.setText(mobileNo);
            mobileStatusTV.setText(rsbyItem.getMobileAuth());
            for (WhoseMobileItem item : AppUtility.prepareWhoseMobile()) {
                if (rsbyItem.getWhoseMobile().equalsIgnoreCase(item.getStatusCode())) {
                    whoseMobileTV.setText(item.getStatusType());
                    break;
                }
            }
        }else{

        }


*/

        if (rsbyItem.getMobileNo() != null && !rsbyItem.getMobileNo().equalsIgnoreCase("")) {
            mobileNoStatusLayout.setVisibility(View.VISIBLE);
            String mobileNo = "XXXXXX" + rsbyItem.getMobileNo().substring(6);
            mobileNoTV.setText(mobileNo);
            if (rsbyItem.getMobileAuth() != null && rsbyItem.getMobileAuth().trim().equalsIgnoreCase(AppConstant.VALID_STATUS)) {
                mobileStatusTV.setText("Verifed");
            } else {
                mobileStatusTV.setText("Not Verified");

            }
            for (WhoseMobileItem item : AppUtility.prepareWhoseMobile()) {
                if (rsbyItem.getWhoseMobile().equalsIgnoreCase(item.getStatusCode())) {
                    whoseMobileTV.setText(item.getStatusType());
                    break;
                }
            }
        } else {
        }
        urnNoLayout.setVisibility(View.GONE);
        healthSchemeLayout.setVisibility(View.GONE);
        healthSchemeheaderLayout.setVisibility(View.GONE);
        if (rsbyItem.getUrnNo() != null && !rsbyItem.getUrnNo().equalsIgnoreCase("")) {
            healthSchemeheaderLayout.setVisibility(View.VISIBLE);
            urnNoLayout.setVisibility(View.VISIBLE);
            urnNoTV.setText(rsbyItem.getUrnNo());
        } else {

        }

        if (rsbyItem.getSchemeId() != null && !rsbyItem.getSchemeId().equalsIgnoreCase("")) {
            healthSchemeLayout.setVisibility(View.VISIBLE);
            healthSchemeheaderLayout.setVisibility(View.VISIBLE);
            for (HealthSchemeItem item : SeccDatabase.getHealthSchemeList(context, selectedLocation.getStateCode().trim())) {
                if (rsbyItem.getSchemeId() != null && rsbyItem.getSchemeId().equalsIgnoreCase(item.getSchemeId())) {
                    healthSchemeTV.setText(item.getSchemeName());
                    schemIdTV.setText(rsbyItem.getSchemeNo());
                }
            }
        } else {
        }
        nomineeDetailLayout.setVisibility(View.GONE);
        if (rsbyItem.getRelationNomineeCode() != null && !rsbyItem.getRelationNomineeCode().equalsIgnoreCase("")) {
            nomineeDetailLayout.setVisibility(View.VISIBLE);
            if (rsbyItem.getNameNominee() != null)
                prevNomineeNameTV.setText(getNomineeName(rsbyItem.getUrnId(), rsbyItem.getNameNominee()));

            prevNomineeRelTV.setText("-");
            if (rsbyItem.getRelationNomineeCode() != null) {
                for (RelationItem item : relationList) {
                    if (item.getRelationCode().equalsIgnoreCase(rsbyItem.getRelationNomineeCode().trim())) {
                        if (item.getRelationName().equalsIgnoreCase(rsbyItem.getNomineeRelationName())) {
                            prevNomineeRelTV.setText(item.getRelationName());
                        } else {
                            prevNomineeRelTV.setText(item.getRelationName() + "(" + rsbyItem.getNomineeRelationName() + ")");
                        }
                        break;
                    }
                }
            }
        }
/*
        for(int i=0;i<AppUtility.getMaritalStatusCode().size();i++){
            if(rsbyItem.getReqMarritalStatCode()!=null && rsbyItem.getReqMarritalStatCode().equalsIgnoreCase(AppUtility.getMaritalStatusCode().get(i))){
                reqMaritalTV.setText(AppUtility.getMaritalStatusLabel().get(i));
                break;
            }
        }*/

        for (FamilyStatusItem famStatItem : householdStatusList) {
            if (famStatItem.getStatusCode().equalsIgnoreCase(rsbyItem.getHhStatus())) {
                houseHoldStatTV.setText(famStatItem.getStatusDesc());
                break;
            }
        }

        for (MemberStatusItem memStatItem : memberStatusList) {
            if (rsbyItem.getMemStatus() != null) {
                if (rsbyItem.getMemStatus().equalsIgnoreCase(memStatItem.getStatusCode())) {
                    if (govtIdFlag) {
                        memberStatusTV.setText(memStatItem.getStatusDesc() + " " + getString(R.string.without_aadhaar));
                    } else if (aadhaarflag) {
                        AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Member Status1111111111 : " + memStatItem.getStatusCode());
                        memberStatusTV.setText(memStatItem.getStatusDesc() + " " + getString(R.string.with_aadhaar1));
                    } else {
                        memberStatusTV.setText(memStatItem.getStatusDesc());
                    }
                }
            }
        }

       /* for(RelationItem item : relationList){
            if(rsbyItem.getRelcode()!=null && rsbyItem.getRelcode().equalsIgnoreCase(item.getRelationCode())){
                reqRelationTV.setText(item.getRelationName());
                break;
            }
        }*/
    }

    public void ShowImageInPopUp(Bitmap mIgameBitmap) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.layout_to_performzoom);
        LinearLayout mZoomLinearLayout;
        ZoomView zoomView;
        mZoomLinearLayout = (LinearLayout) dialog.findViewById(R.id.mZoomLinearLayoutPopUp);
        View v = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.image_view_popup, null, false);
        v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
        dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setDimAmount(0.0f);
        dialog.setCancelable(false);
        ImageView mImageView = (ImageView) v.findViewById(R.id.imageView);
        ImageView mCancelPopUp = (ImageView) v.findViewById(R.id.mCancelPopUp);
        mImageView.setImageBitmap(mIgameBitmap);
        zoomView = new ZoomView(this);
        zoomView.addView(v);
        mZoomLinearLayout.addView(zoomView);
        mCancelPopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }


    private void lockPrompt() {
        lockDialog = new AlertDialog.Builder(context).create();
        LayoutInflater factory = LayoutInflater.from(context);
        View alertView = factory.inflate(R.layout.delete_house_hold_prompt, null);
        lockDialog.setView(alertView);
        // dialog.setContentView(R.layout.opt_auth_layout);
        //    final TextView otpAuthMsg=(TextView)alertView.findViewById(R.id.otpAuthMsg);

        Button syncBT = (Button) alertView.findViewById(R.id.syncBT);
        syncBT.setText("Ok");
        Button cancelBT = (Button) alertView.findViewById(R.id.cancelBT);
        final TextView deletePromptTV = (TextView) alertView.findViewById(R.id.deleteMsg);
        //long unsyncData=SeccDatabase.countSurveyedHousehold(context,"","");
        // long underSurvey=SeccDatabase.countUnderSurveyedHousehold(context,"","");
        String msg = getResources().getString(R.string.locked_msg);
        //   otpAuthMsg.setText("Please enter OTP sent by the UIDAI on your Aadhaar registerd mobile number(XXXXXX0906");
        deletePromptTV.setText(msg);
        syncBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lockDialog.dismiss();
                /*Intent theIntent=new Intent(context,SyncHouseholdActivity.class);
                startActivity(theIntent);
                finish();
                leftTransition();*/


            }
        });


        cancelBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lockDialog.dismiss();
            }
        });
        lockDialog.show();
    }

    private void askPinToLock() {
        askForPinDailog = new AlertDialog.Builder(context).create();
        LayoutInflater factory = LayoutInflater.from(context);
        View alertView = factory.inflate(R.layout.ask_pin_layout, null);
        askForPinDailog.setView(alertView);
        askForPinDailog.show();
        // Log.d(TAG,"delete status :"+deleteStatus);
        // dialog.setContentView(R.layout.opt_auth_layout);
        //    final TextView otpAuthMsg=(TextView)alertView.findViewById(R.id.otpAuthMsg);
        final VerifierLoginResponse verifierDetail = VerifierLoginResponse.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.VERIFIER_CONTENT, context));
        final EditText pinET = (EditText) alertView.findViewById(R.id.deletPinET);
        final TextView errorTV = (TextView) alertView.findViewById(R.id.invalidOtpTV);

        wrongAttempetCountText = (TextView) alertView.findViewById(R.id.wrongAttempetCountText);
        wrongAttempetCountValue = (TextView) alertView.findViewById(R.id.wrongAttempetCountValue);


        pinET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //   errorTV.setVisibility(View.GONE);
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


                currentTime = System.currentTimeMillis();
                try {

                    wrongPinSavedTime = Long.parseLong(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.WORNG_PIN_ENTERED_TIMESTAMP, context));
                } catch (Exception ex) {
                    wrongPinSavedTime = 0;
                }
                if (currentTime > (wrongPinSavedTime + millisecond24)) {


                    String pin = pinET.getText().toString();
                    if (pin.toString().equalsIgnoreCase(verifierDetail.getPin())) {
                        submitLock();
                    } else if (pin.equalsIgnoreCase("")) {
                        // CustomAlert.alertWithOk(context,"Please enter valid pin");
                        errorTV.setVisibility(View.VISIBLE);
                        errorTV.setText("Enter pin");
                        pinET.setText("");
                        //  pinET.setHint("");
                    } else if (!pin.toString().equalsIgnoreCase(verifierDetail.getPin())) {

                        if (wrongPinCount >= 2) {
                            errorTV.setTextColor(context.getResources().getColor(R.color.red));
                            wrongAttempetCountValue.setTextColor(context.getResources().getColor(R.color.red));
                            wrongAttempetCountText.setTextColor(context.getResources().getColor(R.color.red));
                        }
                        wrongPinCount++;
                        wrongAttempetCountValue.setText((3 - wrongPinCount) + "");
                        if (wrongPinCount > 2) {
                            long time = System.currentTimeMillis();
                            ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.WORNG_PIN_ENTERED_TIMESTAMP, time + "", context);
                            CustomAlert.alertWithOk(context, context.getResources().getString(R.string.youHaveExceedPinLimit));
                        } else {
                            errorTV.setVisibility(View.VISIBLE);
                            errorTV.setText("Enter correct pin");
                            pinET.setText("");
//                        CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterValidPin));
//                        pinET.setText("");
                        }
//                    errorTV.setVisibility(View.VISIBLE);
//                    errorTV.setText("Enter correct pin");
//                    pinET.setText("");
                        // pinET.setHint("Enter 4-di");
                    }
                } else {

                    //alert  when pin login is diabled for 24 hrs
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.pinLoginDisabled));
                    errorTV.setText("Pin login disabled for 24 hrs.");
                    pinET.setText("");
                    return;
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

    private void submitLock() {
       /* Intent theIntent;
        if(rsbyItem!=null) {
            if(selectedMember.getOldHeadMember()!=null){
                SeccMemberItem oldHead=selectedMember.getOldHeadMember();
                oldHead.setLockedSave(AppConstant.LOCKED+"");
                AppUtility.showLog(AppConstant.LOG_STATUS,TAG," OLD Household name " +
                        ": "+oldHead.getName()+"" +
                        " Member Status "+oldHead.getMemStatus()+" House hold Status :" +
                        " "+oldHead.getHhStatus()+" Locked Save :"+oldHead.getLockedSave());
                SeccDatabase.updateSeccMember(selectedMember.getOldHeadMember(),context);
            }

            rsbyItem.setLockedSave(AppConstant.LOCKED + "");
            SeccDatabase.updateSeccMember(rsbyItem,context);
            HouseHoldItem houseHoldItem=selectedMember.getHouseHoldItem();

            if(SeccDatabase.checkUnderSurveyMember(context,houseHoldItem.getHhdNo())){
                houseHoldItem.setLockSave(AppConstant.SAVE+"");
            }else{
                houseHoldItem.setLockSave(AppConstant.LOCKED+"");
            }

            Log.d("Preview Activity"," household json : "+houseHoldItem.serialize());
            selectedMember.setHouseHoldItem(houseHoldItem);
            SeccDatabase.updateHouseHold(selectedMember.getHouseHoldItem(),context);
            rsbyItem=SeccDatabase.getSeccMemberDetail(rsbyItem.getNhpsMemId(),context);
            selectedMember.setSeccMemberItem(rsbyItem);
            ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                    AppConstant.SELECTED_ITEM_FOR_VERIFICATION,selectedMember.serialize(),context);
            // SeccDatabase.getSeccMemberDetail()
            theIntent = new Intent(context, SECCFamilyListActivity.class);
        }else{
            theIntent = new Intent(context, RSBYFamilyMemberListActivity.class);
        }
        theIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(theIntent);
        finish();
        leftTransition();*/

        Intent theIntent = null;
        if (rsbyItem != null) {
            if (selectedMember.getOldHeadrsbyMemberItem() != null && selectedMember.getNewHeadrsbyMemberItem() != null) {
                RSBYItem oldHead = selectedMember.getOldHeadrsbyMemberItem();
                oldHead.setLockedSave(AppConstant.LOCKED + "");
               /* oldHead.setAppVersion(AppUtility.getCurrentApplicationVersion(context));
                oldHead.setSyncDt(String.valueOf(DateTimeUtil.currentTimeMillis()));*/
                AppUtility.showLog(AppConstant.LOG_STATUS, TAG, " OLD Household name " +
                        ": " + oldHead.getName() + "" +
                        " Member Status " + oldHead.getMemStatus() + " House hold Status :" +
                        " " + oldHead.getHhStatus() + " Locked Save :" + oldHead.getLockedSave());
                SeccDatabase.updateRsbyMember(selectedMember.getOldHeadrsbyMemberItem(), context);
            }
           /* rsbyItem.setSyncDt(String.valueOf(DateTimeUtil.currentTimeMillis()));
            rsbyItem.setAppVersion(AppUtility.getCurrentApplicationVersion(context));*/
            rsbyItem.setLockedSave(AppConstant.LOCKED + "");
            SeccDatabase.updateRsbyMember(rsbyItem, context);
            RsbyHouseholdItem houseHoldItem = selectedMember.getRsbyHouseholdItem();

            if (SeccDatabase.checkRsbyUnderSurveyMember(houseHoldItem.getUrnId(), context)) {
                houseHoldItem.setLockedSave(AppConstant.SAVE + "");
            } else {
                houseHoldItem.setLockedSave(AppConstant.LOCKED + "");
            }
            selectedMember.setRsbyHouseholdItem(houseHoldItem);
            SeccDatabase.updateRSBYHouseHold(selectedMember.getRsbyHouseholdItem(), context);
            rsbyItem = SeccDatabase.getRsbyMemberDetail(rsbyItem.getRsbyMemId(), context);
            selectedMember.setRsbyMemberItem(rsbyItem);
            ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                    AppConstant.SELECTED_ITEM_FOR_VERIFICATION, selectedMember.serialize(), context);
            // SeccDatabase.getSeccMemberDetail()
            theIntent = new Intent(context, RsbyMainActivity.class);
        }
        theIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(theIntent);
        finish();
        leftTransition();
    }

    private String getNomineeName(String urnId, String nomineeName) {
        String modifiedNomineeDropdownCaption = nomineeName;
       /* ArrayList<RSBYItem> nomineeList= SeccDatabase.getRsbyMemberList(urnId,context);
        for(RSBYItem item : nomineeList) {
            if(item.getName()!=null && !item.getName().equalsIgnoreCase("")){
                if(item.getName().equalsIgnoreCase(nomineeName)){
                    if (item.getMemStatus() != null && item.getMemStatus().trim().equalsIgnoreCase("") || item.getMemStatus().trim().equalsIgnoreCase(AppConstant.MEMBERFOUND_BUT_NOT_PRESENT) || item.getMemStatus().trim().equalsIgnoreCase(AppConstant.MEMBERFOUND_AND_PRESENT) || item.getMemStatus() == null) {
                        if (!item.getName().equalsIgnoreCase("")) {
                            String yearOfBirth = null;
                            String gender;

                            if (item.getDob() != null && !item.getDob().equalsIgnoreCase("") && !item.getDob().equalsIgnoreCase("-")) {
                                yearOfBirth = item.getDob().substring(0, 4);
                            } else if (item.getDob() != null && !item.getDob().equalsIgnoreCase("") && !item.getDob().equalsIgnoreCase("-")) {
                                yearOfBirth = item.getDob().substring(0, 4);
                            }
                            if (item.getGender() != null && item.getGender().equalsIgnoreCase(AppConstant.MALE_GENDER)) {
                                gender = "M";
                            } else if (item.getGender() != null && item.getGender().equalsIgnoreCase(AppConstant.FEMALE_GENDER)) {
                                gender = "F";
                            } else {
                                gender = "O";
                            }
                            int age = 0;
                            if (yearOfBirth != null) {
                                age = AppConstant.COMPARED_YEAR - Integer.parseInt(yearOfBirth);
                            }
                            modifiedNomineeDropdownCaption = item.getName() + " - (" + gender + ") " + age + " yrs";
                            //  AppUtility.showLog(AppConstant.LOG_STATUS, TAG, " Nominee Label :" + ite1.getNomineeLabel());

                        }
                    }
                }
            }

        }*/


        return modifiedNomineeDropdownCaption;
    }


    private void alertForConsent(Context context) {

        final AlertDialog internetDiaolg = new AlertDialog.Builder(context).create();
        LayoutInflater factory = LayoutInflater.from(context);
        View alertView = factory.inflate(R.layout.rsbyconsent_layout, null);
        internetDiaolg.setView(alertView);
        internetDiaolg.show();
        final CheckBox consent = (CheckBox) alertView.findViewById(R.id.consent);
        Button tryAgainBT = (Button) alertView.findViewById(R.id.tryAgainBT);
        Button cancelBT = (Button) alertView.findViewById(R.id.cancelBT);
        final TextView errorTV = (TextView) alertView.findViewById(R.id.errorTV);

        tryAgainBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (consent.isChecked()) {
                    errorTV.setVisibility(View.GONE);
                    askPinToLock();
                } else {
                    errorTV.setVisibility(View.VISIBLE);
                    errorTV.setText("Please check the consent.");
                }
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