package com.nhpm.activity;

import android.app.AlertDialog;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.customComponent.CustomAlert;
import com.customComponent.utility.ProjectPrefrence;
import com.nhpm.LocalDataBase.dto.SeccDatabase;
import com.nhpm.Models.response.GovernmentIdItem;
import com.nhpm.Models.response.RelationItem;
import com.nhpm.Models.response.WhoseMobileItem;
import com.nhpm.Models.response.master.ConfigurationItem;
import com.nhpm.Models.response.master.HealthSchemeItem;
import com.nhpm.Models.response.master.MemberStatusItem;
import com.nhpm.Models.response.master.StateItem;
import com.nhpm.Models.response.rsbyMembers.RSBYMemberItem;
import com.nhpm.Models.response.seccMembers.HouseHoldItem;
import com.nhpm.Models.response.seccMembers.SeccMemberItem;
import com.nhpm.Models.response.seccMembers.SelectedMemberItem;
import com.nhpm.Models.response.verifier.FamilyStatusItem;
import com.nhpm.Models.response.verifier.VerifierLocationItem;
import com.nhpm.Models.response.verifier.VerifierLoginResponse;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;
import com.nhpm.Utility.AppUtility;

import java.util.ArrayList;

import pl.polidea.view.ZoomView;

public class SyncPreviewActivity extends BaseActivity implements ComponentCallbacks2 {
    private SelectedMemberItem selectedMember;
    private ImageView memberPhotoIV, govtIdIV;
    private TextView nameTV, fatherNameTV, motherNameTV, relationTV, houseHoldStatTV, memberStatusTV;
    private TextView aadhaarNumberTV, aadhaarStatusTV, nameAsAadhaarTV;
    private TextView govtIDTV, govtIdTypeTV, nameAsIdTV;
    private TextView mobileNoTV, whoseMobileTV, mobileStatusTV;
    private TextView urnNoTV, healthSchemeTV, schemIdTV;
    private TextView reqNameTV, reqFatherNameTV, reqMotherNameTV, reqRelationTV, reqDobTV, reqGenderTV, reqMaritalTV, reqOccupTV;
    private Button confrimBT;
    private SeccMemberItem seccItem;
    private RSBYMemberItem rsbyItem;
    private Context context;
    private TextView headerTV;
    private ArrayList<FamilyStatusItem> householdStatusList;
    private ArrayList<MemberStatusItem> memberStatusList;
    private RelativeLayout backLayout;
    private ImageView backIV;
    private ArrayList<RelationItem> relationList;
    private LinearLayout mZoomLinearLayout;
    private ZoomView zoomView;
    private VerifierLocationItem selectedLocation;
    private boolean pinLockIsShown = false;
    private String zoomMode = "N";

    private int wrongPinCount = 0;
    private long wrongPinSavedTime;
    private long currentTime;
    private TextView wrongAttempetCountValue,wrongAttempetCountText;
    private long millisecond24 = 86400000;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        checkAppConfig();
        setContentView(R.layout.activity_preview);
        setContentView(R.layout.dummy_layout_for_zooming);

    }

    private void setupScreenWithZoom() {
        mZoomLinearLayout = (LinearLayout) findViewById(R.id.mZoomLinearLayout);
        View v = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.activity_preview, null, false);
        v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
        showNotification(v);
        selectedLocation = VerifierLocationItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.SELECTED_BLOCK, context));
        selectedMember = SelectedMemberItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.SELECTED_ITEM_FOR_VERIFICATION, context));
        householdStatusList = SeccDatabase.getFamilyStatusList(context);
        memberStatusList = SeccDatabase.getMemberStatusList(context);
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
        aadhaarStatusTV = (TextView) v.findViewById(R.id.prevAdhaarStatusTV);
        nameAsAadhaarTV = (TextView) v.findViewById(R.id.prevNameAsAadhaarTV);
        govtIDTV = (TextView) v.findViewById(R.id.prevGovtIdNoTV);
        nameAsIdTV = (TextView) v.findViewById(R.id.prevGovtNameAsIdTV);

        govtIdTypeTV = (TextView) v.findViewById(R.id.prevGovtIdTypeTV);
        mobileNoTV = (TextView) v.findViewById(R.id.prevMobileNoTV);
        mobileStatusTV = (TextView) v.findViewById(R.id.prevMobileStatTV);
        whoseMobileTV = (TextView) v.findViewById(R.id.prevWhoseMobileTV);
        urnNoTV = (TextView) v.findViewById(R.id.prevURNTV);
        //healthSchemeTV=(TextView)v.findViewById(R.id.prevStateHealthTV);
        // schemIdTV=(TextView)v.findViewById(R.id.prevStateHealthIDTV);
        reqNameTV = (TextView) v.findViewById(R.id.reqNameTV);
        reqFatherNameTV = (TextView) v.findViewById(R.id.reqFatherNameTV);
        reqMotherNameTV = (TextView) v.findViewById(R.id.reqMotherNameTV);
        reqRelationTV = (TextView) v.findViewById(R.id.reqRelationTV);
        reqDobTV = (TextView) v.findViewById(R.id.reqDateOfBirthTV);
        reqGenderTV = (TextView) v.findViewById(R.id.reqGenderTV);
        reqMaritalTV = (TextView) v.findViewById(R.id.reqMaritalStatTV);
        reqOccupTV = (TextView) v.findViewById(R.id.reqOccupationTV);
        confrimBT = (Button) v.findViewById(R.id.confirmBT);
        confrimBT.setVisibility(View.GONE);
        backLayout = (RelativeLayout) v.findViewById(R.id.backLayout);
        backIV = (ImageView) v.findViewById(R.id.back);

        zoomView = new ZoomView(this);
        zoomView.addView(v);
        mZoomLinearLayout.addView(zoomView);

        dashboardDropdown(v);

        relationList = SeccDatabase.getRelationList(context);

        if (selectedMember.getSeccMemberItem() != null) {
            seccItem = selectedMember.getSeccMemberItem();

            if (seccItem.getName() != null)
                headerTV.setText(seccItem.getName());

            if (seccItem.getMemberPhoto1() != null)
                memberPhotoIV.setImageBitmap(AppUtility.convertStringToBitmap(seccItem.getMemberPhoto1()));

            if (seccItem.getName() != null)
                nameTV.setText(seccItem.getName());

            if (seccItem.getFathername() != null)
                fatherNameTV.setText(seccItem.getFathername());

            if (seccItem.getMothername() != null)
                motherNameTV.setText(seccItem.getMothername());

            if (seccItem.getRelation() != null)
                relationTV.setText(seccItem.getRelation());

            if (seccItem.getAadhaarStatus() != null && seccItem.getAadhaarStatus().equalsIgnoreCase("1")) {
                if (seccItem.getName() != null)
                    aadhaarNumberTV.setText(seccItem.getAadhaarNo());

                if (seccItem.getAadhaarAuth() != null)
                    aadhaarStatusTV.setText(seccItem.getAadhaarAuth());

                if (seccItem.getNameAadhaar() != null)
                    nameAsAadhaarTV.setText(seccItem.getNameAadhaar());
            } else {
                if (seccItem.getGovtIdPhoto() != null)
                    govtIdIV.setImageBitmap(AppUtility.convertStringToBitmap(seccItem.getGovtIdPhoto()));

                if (seccItem.getIdType() != null) {
                    for (GovernmentIdItem item : AppUtility.prepareGovernmentIdSpinner()) {
                        if (seccItem.getIdType().equalsIgnoreCase(item.statusCode + "")) {
                            govtIdTypeTV.setText(item.status);
                            break;
                        }
                    }
                }

                if (seccItem.getIdNo() != null)
                    govtIDTV.setText(seccItem.getIdNo());
                if (seccItem.getNameAsId() != null) {
                    nameAsIdTV.setText(seccItem.getNameAsId());
                }
            }

            if (seccItem.getMobileNo() != null)
                mobileNoTV.setText(seccItem.getMobileNo());

            for (WhoseMobileItem item : AppUtility.prepareWhoseMobile()) {
                if (seccItem.getWhoseMobile().equalsIgnoreCase(item.getStatusCode())) {
                    whoseMobileTV.setText(item.getStatusType());
                    break;
                }
            }
            mobileStatusTV.setText(seccItem.getAadhaarAuth());

            if (seccItem.getUrnNo() != null)
                urnNoTV.setText(seccItem.getUrnNo());

            if (seccItem.getSchemeId() != null) {
                for (HealthSchemeItem item : SeccDatabase.getHealthSchemeList(context, selectedLocation.getStateCode().trim())) {
                    if (seccItem.getSchemeId() != null && seccItem.getSchemeId().equalsIgnoreCase(item.getSchemeId())) {
                        healthSchemeTV.setText(item.getSchemeName());
                        schemIdTV.setText(seccItem.getSchemeNo());
                    }
                }
            }
            if (seccItem.getReqName() != null)
                reqNameTV.setText(seccItem.getReqName());

            if (seccItem.getReqFatherName() != null) {
                reqFatherNameTV.setText(seccItem.getReqFatherName());
            }

            if (seccItem.getReqMotherName() != null)
                reqMotherNameTV.setText(seccItem.getReqMotherName());

            if (seccItem.getReqRelationName() != null)
                reqRelationTV.setText(seccItem.getReqRelationName());

            if (seccItem.getReqDOB() != null)
                reqDobTV.setText(seccItem.getReqDOB());

            if (seccItem.getReqOccupation() != null)
                reqOccupTV.setText(seccItem.getReqOccupation());

            for (int i = 0; i < AppUtility.getGenderCode().size(); i++) {
                if (seccItem.getReqGenderCode() != null && seccItem.getReqGenderCode().equalsIgnoreCase(AppUtility.getGenderCode().get(i))) {
                    reqGenderTV.setText(AppUtility.getGenderLabel().get(i));
                    break;
                }
            }

            Log.d("Preview Activity", "Marital Status : " + seccItem.getReqMarritalStatCode());

            for (int i = 0; i < AppUtility.getMaritalStatusCode().size(); i++) {
                if (seccItem.getReqMarritalStatCode() != null && seccItem.getReqMarritalStatCode().equalsIgnoreCase(AppUtility.getMaritalStatusCode().get(i))) {
                    reqMaritalTV.setText(AppUtility.getMaritalStatusLabel().get(i));
                    break;
                }
            }

            for (FamilyStatusItem famStatItem : householdStatusList) {
                if (famStatItem.getStatusCode().equalsIgnoreCase(seccItem.getHhStatus())) {
                    houseHoldStatTV.setText(famStatItem.getStatusDesc());
                    break;
                }
            }

            for (MemberStatusItem memStatItem : memberStatusList) {
                if (seccItem.getMemStatus().equalsIgnoreCase(memStatItem.getStatusCode())) {
                    memberStatusTV.setText(memStatItem.getStatusDesc());
                }
            }

            for (RelationItem item : relationList) {
                if (seccItem.getReqGenderCode() != null && seccItem.getReqRelationCode().equalsIgnoreCase(item.getRelationCode())) {
                    reqRelationTV.setText(item.getRelationName());
                    break;
                }
            }

        }
        confrimBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent theIntent = null;
                if (seccItem != null) {
                    seccItem.setLockedSave(AppConstant.LOCKED + "");
                    SeccDatabase.updateSeccMember(seccItem, context);
                    HouseHoldItem houseHoldItem = selectedMember.getHouseHoldItem();

                    if (SeccDatabase.checkUnderSurveyMember(context, houseHoldItem)) {
                        houseHoldItem.setLockSave(AppConstant.SAVE + "");
                    } else {
                        houseHoldItem.setLockSave(AppConstant.LOCKED + "");
                    }

                    Log.d("Preview Activity", " household json : " + houseHoldItem.serialize());
                    selectedMember.setHouseHoldItem(houseHoldItem);
                    SeccDatabase.updateHouseHold(selectedMember.getHouseHoldItem(), context);
                    seccItem = SeccDatabase.getSeccMemberDetail(seccItem, context);
                    selectedMember.setSeccMemberItem(seccItem);
                    ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                            AppConstant.SELECTED_ITEM_FOR_VERIFICATION, selectedMember.serialize(), context);
                    // SeccDatabase.getSeccMemberDetail()
                    //   theIntent = new Intent(context, SECCFamilyListActivity.class);
                } else {
                    // theIntent = new Intent(context, RSBYFamilyMemberListActivity.class);
                }
                startActivity(theIntent);
                finish();
            }
        });
        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Intent theIntent;
               // if (seccItem != null && seccItem.getAadhaarStatus().equalsIgnoreCase("1")) {
                    theIntent = new Intent(context, WithAadhaarActivity.class);
               *//* } else {
                    theIntent = new Intent(context, WithoutAadhaarVerificationActivity.class);
                }*//*
                    startActivity(theIntent);*/
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

    private void setupScreenWithOutZoom() {
        showNotification();
        selectedLocation = VerifierLocationItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.SELECTED_BLOCK, context));
        selectedMember = SelectedMemberItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.SELECTED_ITEM_FOR_VERIFICATION, context));
        householdStatusList = SeccDatabase.getFamilyStatusList(context);
        memberStatusList = SeccDatabase.getMemberStatusList(context);
        headerTV = (TextView) findViewById(R.id.centertext);
        memberPhotoIV = (ImageView) findViewById(R.id.prevMemberPhotoIV);
        govtIdIV = (ImageView) findViewById(R.id.idPhotoIV);
        nameTV = (TextView) findViewById(R.id.prevNameTV);
        fatherNameTV = (TextView) findViewById(R.id.prevFatherNameTV);
        motherNameTV = (TextView) findViewById(R.id.prevMotherNameTV);
        relationTV = (TextView) findViewById(R.id.prevRelationTV);
        houseHoldStatTV = (TextView) findViewById(R.id.prevHouseHoldStatusTV);
        memberStatusTV = (TextView) findViewById(R.id.prevMemberStatusTV);
        aadhaarNumberTV = (TextView) findViewById(R.id.prevAadhaarNoTV);
        aadhaarStatusTV = (TextView) findViewById(R.id.prevAdhaarStatusTV);
        nameAsAadhaarTV = (TextView) findViewById(R.id.prevNameAsAadhaarTV);
        govtIDTV = (TextView) findViewById(R.id.prevGovtIdNoTV);
        nameAsIdTV = (TextView) findViewById(R.id.prevGovtNameAsIdTV);

        govtIdTypeTV = (TextView) findViewById(R.id.prevGovtIdTypeTV);
        mobileNoTV = (TextView) findViewById(R.id.prevMobileNoTV);
        mobileStatusTV = (TextView) findViewById(R.id.prevMobileStatTV);
        whoseMobileTV = (TextView) findViewById(R.id.prevWhoseMobileTV);
        urnNoTV = (TextView) findViewById(R.id.prevURNTV);
        //healthSchemeTV=(TextView)findViewById(R.id.prevStateHealthTV);
        // schemIdTV=(TextView)findViewById(R.id.prevStateHealthIDTV);
        reqNameTV = (TextView) findViewById(R.id.reqNameTV);
        reqFatherNameTV = (TextView) findViewById(R.id.reqFatherNameTV);
        reqMotherNameTV = (TextView) findViewById(R.id.reqMotherNameTV);
        reqRelationTV = (TextView) findViewById(R.id.reqRelationTV);
        reqDobTV = (TextView) findViewById(R.id.reqDateOfBirthTV);
        reqGenderTV = (TextView) findViewById(R.id.reqGenderTV);
        reqMaritalTV = (TextView) findViewById(R.id.reqMaritalStatTV);
        reqOccupTV = (TextView) findViewById(R.id.reqOccupationTV);
        confrimBT = (Button) findViewById(R.id.confirmBT);
        confrimBT.setVisibility(View.GONE);
        backLayout = (RelativeLayout) findViewById(R.id.backLayout);
        backIV = (ImageView) findViewById(R.id.back);

        dashboardDropdown();

        relationList = SeccDatabase.getRelationList(context);

        if (selectedMember.getSeccMemberItem() != null) {
            seccItem = selectedMember.getSeccMemberItem();

            if (seccItem.getName() != null)
                headerTV.setText(seccItem.getName());

            if (seccItem.getMemberPhoto1() != null)
                memberPhotoIV.setImageBitmap(AppUtility.convertStringToBitmap(seccItem.getMemberPhoto1()));

            if (seccItem.getName() != null)
                nameTV.setText(seccItem.getName());

            if (seccItem.getFathername() != null)
                fatherNameTV.setText(seccItem.getFathername());

            if (seccItem.getMothername() != null)
                motherNameTV.setText(seccItem.getMothername());

            if (seccItem.getRelation() != null)
                relationTV.setText(seccItem.getRelation());

            if (seccItem.getAadhaarStatus() != null && seccItem.getAadhaarStatus().equalsIgnoreCase("1")) {
                if (seccItem.getName() != null)
                    aadhaarNumberTV.setText(seccItem.getAadhaarNo());

                if (seccItem.getAadhaarAuth() != null)
                    aadhaarStatusTV.setText(seccItem.getAadhaarAuth());

                if (seccItem.getNameAadhaar() != null)
                    nameAsAadhaarTV.setText(seccItem.getNameAadhaar());
            } else {
                if (seccItem.getGovtIdPhoto() != null)
                    govtIdIV.setImageBitmap(AppUtility.convertStringToBitmap(seccItem.getGovtIdPhoto()));

                if (seccItem.getIdType() != null) {
                    for (GovernmentIdItem item : AppUtility.prepareGovernmentIdSpinner()) {
                        if (seccItem.getIdType().equalsIgnoreCase(item.statusCode + "")) {
                            govtIdTypeTV.setText(item.status);
                            break;
                        }
                    }
                }

                if (seccItem.getIdNo() != null)
                    govtIDTV.setText(seccItem.getIdNo());
                if (seccItem.getNameAsId() != null) {
                    nameAsIdTV.setText(seccItem.getNameAsId());
                }
            }

            if (seccItem.getMobileNo() != null)
                mobileNoTV.setText(seccItem.getMobileNo());

            for (WhoseMobileItem item : AppUtility.prepareWhoseMobile()) {
                if (seccItem.getWhoseMobile().equalsIgnoreCase(item.getStatusCode())) {
                    whoseMobileTV.setText(item.getStatusType());
                    break;
                }
            }
            mobileStatusTV.setText(seccItem.getAadhaarAuth());

            if (seccItem.getUrnNo() != null)
                urnNoTV.setText(seccItem.getUrnNo());

            if (seccItem.getSchemeId() != null) {
                for (HealthSchemeItem item : SeccDatabase.getHealthSchemeList(context, selectedLocation.getStateCode().trim())) {
                    if (seccItem.getSchemeId() != null && seccItem.getSchemeId().equalsIgnoreCase(item.getSchemeId())) {
                        healthSchemeTV.setText(item.getSchemeName());
                        schemIdTV.setText(seccItem.getSchemeNo());
                    }
                }
            }
            if (seccItem.getReqName() != null)
                reqNameTV.setText(seccItem.getReqName());

            if (seccItem.getReqFatherName() != null) {
                reqFatherNameTV.setText(seccItem.getReqFatherName());
            }

            if (seccItem.getReqMotherName() != null)
                reqMotherNameTV.setText(seccItem.getReqMotherName());

            if (seccItem.getReqRelationName() != null)
                reqRelationTV.setText(seccItem.getReqRelationName());

            if (seccItem.getReqDOB() != null)
                reqDobTV.setText(seccItem.getReqDOB());

            if (seccItem.getReqOccupation() != null)
                reqOccupTV.setText(seccItem.getReqOccupation());

            for (int i = 0; i < AppUtility.getGenderCode().size(); i++) {
                if (seccItem.getReqGenderCode() != null && seccItem.getReqGenderCode().equalsIgnoreCase(AppUtility.getGenderCode().get(i))) {
                    reqGenderTV.setText(AppUtility.getGenderLabel().get(i));
                    break;
                }
            }

            Log.d("Preview Activity", "Marital Status : " + seccItem.getReqMarritalStatCode());

            for (int i = 0; i < AppUtility.getMaritalStatusCode().size(); i++) {
                if (seccItem.getReqMarritalStatCode() != null && seccItem.getReqMarritalStatCode().equalsIgnoreCase(AppUtility.getMaritalStatusCode().get(i))) {
                    reqMaritalTV.setText(AppUtility.getMaritalStatusLabel().get(i));
                    break;
                }
            }

            for (FamilyStatusItem famStatItem : householdStatusList) {
                if (famStatItem.getStatusCode().equalsIgnoreCase(seccItem.getHhStatus())) {
                    houseHoldStatTV.setText(famStatItem.getStatusDesc());
                    break;
                }
            }

            for (MemberStatusItem memStatItem : memberStatusList) {
                if (seccItem.getMemStatus().equalsIgnoreCase(memStatItem.getStatusCode())) {
                    memberStatusTV.setText(memStatItem.getStatusDesc());
                }
            }

            for (RelationItem item : relationList) {
                if (seccItem.getReqGenderCode() != null && seccItem.getReqRelationCode().equalsIgnoreCase(item.getRelationCode())) {
                    reqRelationTV.setText(item.getRelationName());
                    break;
                }
            }

        }
        confrimBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent theIntent = null;
                if (seccItem != null) {
                    seccItem.setLockedSave(AppConstant.LOCKED + "");
                    SeccDatabase.updateSeccMember(seccItem, context);
                    HouseHoldItem houseHoldItem = selectedMember.getHouseHoldItem();

                    if (SeccDatabase.checkUnderSurveyMember(context, houseHoldItem)) {
                        houseHoldItem.setLockSave(AppConstant.SAVE + "");
                    } else {
                        houseHoldItem.setLockSave(AppConstant.LOCKED + "");
                    }

                    Log.d("Preview Activity", " household json : " + houseHoldItem.serialize());
                    selectedMember.setHouseHoldItem(houseHoldItem);
                    SeccDatabase.updateHouseHold(selectedMember.getHouseHoldItem(), context);
                    seccItem = SeccDatabase.getSeccMemberDetail(seccItem, context);
                    selectedMember.setSeccMemberItem(seccItem);
                    ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                            AppConstant.SELECTED_ITEM_FOR_VERIFICATION, selectedMember.serialize(), context);
                    // SeccDatabase.getSeccMemberDetail()
                    //   theIntent = new Intent(context, SECCFamilyListActivity.class);
                } else {
                    // theIntent = new Intent(context, RSBYFamilyMemberListActivity.class);
                }
                startActivity(theIntent);
                finish();
            }
        });
        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Intent theIntent;
               // if (seccItem != null && seccItem.getAadhaarStatus().equalsIgnoreCase("1")) {
                    theIntent = new Intent(context, WithAadhaarActivity.class);
               *//* } else {
                    theIntent = new Intent(context, WithoutAadhaarVerificationActivity.class);
                }*//*
                    startActivity(theIntent);*/
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

    public void showNotification(View v) {

        LinearLayout notificationLayout = (LinearLayout) v.findViewById(R.id.notificationLayout);
        WebView notificationWebview = (WebView) v.findViewById(R.id.notificationWebview);
        String prePairedMessage = AppUtility.getNotificationData(context);
        if (prePairedMessage != null) {
            notificationLayout.setVisibility(View.VISIBLE);
            notificationWebview.loadData(prePairedMessage, "text/html", "utf-8"); // Set focus to textview
        }
    }

    private void dashboardDropdown(View v) {


        final ImageView settings = (ImageView) v.findViewById(R.id.settings);
        settings.setVisibility(View.VISIBLE);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(context, settings);
                popup.getMenuInflater()
                        .inflate(R.menu.menu_nav_dashboard, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {

                            case R.id.dashboard:
                                //   String searchText = searchFamilyMemberET.getText().toString();
                                Intent theIntent = new Intent(context, SearchActivityWithHouseHold.class);
                                theIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                theIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(theIntent);
                                leftTransition();
                                break;

                        }
                        return true;
                    }
                });
                popup.show();
            }
        });


    }

    public void showNotification() {

        LinearLayout notificationLayout = (LinearLayout) findViewById(R.id.notificationLayout);
        WebView notificationWebview = (WebView) findViewById(R.id.notificationWebview);
        String prePairedMessage = AppUtility.getNotificationData(context);
        if (prePairedMessage != null) {
            notificationLayout.setVisibility(View.VISIBLE);
            notificationWebview.loadData(prePairedMessage, "text/html", "utf-8"); // Set focus to textview
        }
    }

    private void dashboardDropdown() {


        final ImageView settings = (ImageView) findViewById(R.id.settings);
        settings.setVisibility(View.VISIBLE);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(context, settings);
                popup.getMenuInflater()
                        .inflate(R.menu.menu_nav_dashboard, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {

                            case R.id.dashboard:
                                //   String searchText = searchFamilyMemberET.getText().toString();
                                Intent theIntent = new Intent(context, SearchActivityWithHouseHold.class);
                                theIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                theIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(theIntent);
                                leftTransition();
                                break;

                        }
                        return true;
                    }
                });
                popup.show();
            }
        });


    }


    @Override
    protected void onPause() {
        super.onPause();
        if (AppUtility.isAppIsInBackground(context)) {
            if (!pinLockIsShown) {
                askPinToLock();
            }
        }
    }

    private void askPinToLock() {
        pinLockIsShown = true;
        final AlertDialog askForPinDailog = new AlertDialog.Builder(context).create();
        LayoutInflater factory = LayoutInflater.from(context);
        View alertView = factory.inflate(R.layout.ask_pin_layout, null);
        askForPinDailog.setView(alertView);
        askForPinDailog.setCancelable(false);
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



                //  AppUtility.softKeyBoard(activity, 0);
                String pin = pinET.getText().toString();
                if (pin.toString().equalsIgnoreCase(verifierDetail.getPin())) {
                    askForPinDailog.dismiss();
                    pinLockIsShown = false;
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
                    wrongAttempetCountValue.setText((3 - wrongPinCount)+"");
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
                pinLockIsShown = false;
                Intent intent_login = new Intent(context, LoginActivity.class);
                intent_login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent_login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent_login);
                finish();
            }
        });
    }

    @Override
    public void onConfigurationChanged(final Configuration newConfig) {

    }

    @Override
    public void onLowMemory() {

    }

    @Override
    public void onTrimMemory(final int level) {
        if (level == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {
            if (!pinLockIsShown) {
                askPinToLock();
            }
        }
    }

    private String checkAppConfig() {
        StateItem selectedStateItem = StateItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SELECTED_STATE, context));
        if (selectedStateItem != null && selectedStateItem.getStateCode() != null) {
            ArrayList<ConfigurationItem> configList = SeccDatabase.findConfiguration(selectedStateItem.getStateCode(), context);
            if (configList != null) {
                for (ConfigurationItem item1 : configList) {

                    if (item1.getConfigId().equalsIgnoreCase(AppConstant.APPLICATION_ZOOM)) {
                        zoomMode = item1.getStatus();
                    }

                }
            }
        }
        return null;
    }
}
