package com.nhpm.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.customComponent.CustomAlert;
import com.customComponent.utility.DateTimeUtil;
import com.customComponent.utility.ProjectPrefrence;
import com.nhpm.LocalDataBase.dto.SeccDatabase;
import com.nhpm.Models.response.GovernmentIdItem;
import com.nhpm.Models.response.RelationItem;
import com.nhpm.Models.response.WhoseMobileItem;
import com.nhpm.Models.response.master.ConfigurationItem;
import com.nhpm.Models.response.master.HealthSchemeItem;
import com.nhpm.Models.response.master.MemberStatusItem;
import com.nhpm.Models.response.master.StateItem;
import com.nhpm.Models.response.rsbyMembers.RSBYItem;
import com.nhpm.Models.response.seccMembers.SeccMemberItem;
import com.nhpm.Models.response.seccMembers.SelectedMemberItem;
import com.nhpm.Models.response.verifier.FamilyStatusItem;
import com.nhpm.Models.response.verifier.VerifierLocationItem;
import com.nhpm.Models.response.verifier.VerifierLoginResponse;
import com.nhpm.PrintCard.PrintCardMainActivity;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;
import com.nhpm.Utility.AppUtility;

import java.util.ArrayList;

import pl.polidea.view.ZoomView;

public class MemberPreviewActivity extends BaseActivity implements ComponentCallbacks2 {
    private String TAG = "PreviewActivity";
    private SelectedMemberItem selectedMember;
    private ImageView memberPhotoIV, govtIdIV;
    private TextView nameTV, fatherNameTV, motherNameTV, relationTV,
            houseHoldStatTV, memberStatusTV, prevHouseholdIDTV;
    private TextView aadhaarNumberTV, nameAsAadhaarTV, prevCaptureMode;//aadhaarStatusTV
    private ImageView aadharStatusIV;
    private TextView govtIDTV, govtIdTypeTV, nameAsIdTV, prevGovtEidTV, EidTV;
    private TextView mobileNoTV, whoseMobileTV, mobileStatusTV;
    private TextView healthSchemeTV1, schemIdTV1, healthSchemeTV2, schemIdTV2, healthSchemeTV3, schemIdTV3;
    private TextView urnNoTV, hhdTV, dashBoardNavBT;
    private TextView reqNameTV, reqFatherNameTV, reqMotherNameTV,
            reqRelationTV, reqDobTV, reqGenderTV, reqMaritalTV,
            reqOccupTV, prevNomineeNameTV, prevNomineeRelTV, syncVersionTV;
    private TextView syncLockDtTV, hofNameTV, memberIdTV, nhpsIdTV, prevValidatedByTV, prevServeyedByTV;
    private Button confrimBT, printCardBT;
    private SeccMemberItem seccItem;
    private RSBYItem rsbyItem;
    private Context context;
    private TextView headerTV;
    private ArrayList<FamilyStatusItem> householdStatusList;
    private ArrayList<MemberStatusItem> memberStatusList;
    private RelativeLayout backLayout;
    private ImageView backIV;
    private LinearLayout memberPhotoLayout, aadhaarStatusLayout,
            govtIdStatusLayout, mobileNoStatusLayout, addSchemeDetLayout;
    private LinearLayout healthSchemeLayout, urnNoLayout;
    private RelativeLayout healthSchemeheaderLayout;
    private LinearLayout nomineeDetailLayout;
    private TextView addressTV, prevGenderTV, prevDOB, prevNameInRegionalTV, prevGuardianNameTV;
    private ArrayList<RelationItem> relationList;
    private LinearLayout mZoomLinearLayout;
    private ZoomView zoomView;
    private VerifierLocationItem selectedLocation;
    private VerifierLoginResponse storedLoginResponse;
    private boolean pinLockIsShown = false;
    private String zoomMode = "N";

    private int wrongPinCount = 0;
    private long wrongPinSavedTime;
    private long currentTime;
    private TextView wrongAttempetCountValue, wrongAttempetCountText;
    private long millisecond24 = 86400000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        checkAppConfig();

        if (zoomMode.equalsIgnoreCase("N")) {
            setContentView(R.layout.activity_preview);
            setupScreenWithoutZoom();
        } else {
            setContentView(R.layout.dummy_layout_for_zooming);
            setupScreenWithZoom();
        }

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
        storedLoginResponse = VerifierLoginResponse.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.VERIFIER_CONTENT, context));
        householdStatusList = SeccDatabase.getFamilyStatusList(context);
        memberStatusList = SeccDatabase.getMemberStatusList(context);
        relationList = SeccDatabase.getRelationList(context);
        hhdTV = (TextView) v.findViewById(R.id.prevHhdTV);
        syncLockDtTV = (TextView) v.findViewById(R.id.prevSyncLockDtTV);
        memberIdTV = (TextView) v.findViewById(R.id.prevMemberIdTV);
        printCardBT = (Button) v.findViewById(R.id.printCardBT);
        nhpsIdTV = (TextView) v.findViewById(R.id.prevNhpsIdTV);
        hofNameTV = (TextView) v.findViewById(R.id.prevHofTV);
        prevGovtEidTV = (TextView) v.findViewById(R.id.prevGovtEidTV);
        EidTV = (TextView) v.findViewById(R.id.EidTV);
        prevValidatedByTV = (TextView) v.findViewById(R.id.prevValidatedByTV);
        prevServeyedByTV = (TextView) v.findViewById(R.id.prevServeyedByTV);
        prevServeyedByTV.setVisibility(View.GONE);
        syncVersionTV = (TextView) v.findViewById(R.id.prevSyncVersionTV);
        prevHouseholdIDTV = (TextView) v.findViewById(R.id.prevHouseholdIDTV);
        memberPhotoLayout = (LinearLayout) v.findViewById(R.id.memberPhotoLayout);
        aadhaarStatusLayout = (LinearLayout) v.findViewById(R.id.adhaarStatusLayout);
        govtIdStatusLayout = (LinearLayout) v.findViewById(R.id.govtIdStatusLayout);
        mobileNoStatusLayout = (LinearLayout) v.findViewById(R.id.mobileNoStatusLayout);
        addSchemeDetLayout = (LinearLayout) v.findViewById(R.id.add_scheme_det_layout);
        healthSchemeLayout = (LinearLayout) v.findViewById(R.id.stateHealthSchemeLayout);
        healthSchemeheaderLayout = (RelativeLayout) v.findViewById(R.id.healthSchemeheaderLayout);
        nomineeDetailLayout = (LinearLayout) v.findViewById(R.id.nomineeDetailLayout);
        urnNoLayout = (LinearLayout) v.findViewById(R.id.urnNumberLayout);
        prevCaptureMode = (TextView) v.findViewById(R.id.prevCaptureMode);
        headerTV = (TextView) v.findViewById(R.id.centertext);
        memberPhotoIV = (ImageView) v.findViewById(R.id.prevMemberPhotoIV);
        govtIdIV = (ImageView) v.findViewById(R.id.idPhotoIV);
        nameTV = (TextView) v.findViewById(R.id.prevNameTV);
        addressTV = (TextView) v.findViewById(R.id.prevAddressTV);
        fatherNameTV = (TextView) v.findViewById(R.id.prevFatherNameTV);
        motherNameTV = (TextView) v.findViewById(R.id.prevMotherNameTV);
        prevDOB = (TextView) v.findViewById(R.id.prevDobTV);
        prevGenderTV = (TextView) v.findViewById(R.id.prevGenderTV);
        prevNameInRegionalTV = (TextView) v.findViewById(R.id.prevNameInReginalTV);
        relationTV = (TextView) v.findViewById(R.id.prevRelationTV);
        houseHoldStatTV = (TextView) v.findViewById(R.id.prevHouseHoldStatusTV);
        memberStatusTV = (TextView) v.findViewById(R.id.prevMemberStatusTV);
        aadhaarNumberTV = (TextView) v.findViewById(R.id.prevAadhaarNoTV);
        nameAsAadhaarTV = (TextView) v.findViewById(R.id.prevNameAsAadhaarTV);
        aadharStatusIV = (ImageView) v.findViewById(R.id.aadharStatusIV);
        govtIDTV = (TextView) v.findViewById(R.id.prevGovtIdNoTV);
        nameAsIdTV = (TextView) v.findViewById(R.id.prevGovtNameAsIdTV);
        prevNomineeNameTV = (TextView) v.findViewById(R.id.prevNomineeNameTV);
        prevNomineeRelTV = (TextView) v.findViewById(R.id.prevNomineeRelation);
        prevGuardianNameTV = (TextView) v.findViewById(R.id.prevGuardianNameTV);
        govtIdTypeTV = (TextView) v.findViewById(R.id.prevGovtIdTypeTV);
        mobileNoTV = (TextView) v.findViewById(R.id.prevMobileNoTV);
        mobileStatusTV = (TextView) v.findViewById(R.id.prevMobileStatTV);
        whoseMobileTV = (TextView) v.findViewById(R.id.prevWhoseMobileTV);
        urnNoTV = (TextView) v.findViewById(R.id.prevURNTV);
        healthSchemeTV1 = (TextView) v.findViewById(R.id.prevStateHealthTV1);
        schemIdTV1 = (TextView) v.findViewById(R.id.prevStateHealthIDTV1);
        healthSchemeTV2 = (TextView) v.findViewById(R.id.prevStateHealthTV2);
        schemIdTV2 = (TextView) v.findViewById(R.id.prevStateHealthIDTV2);
        healthSchemeTV3 = (TextView) v.findViewById(R.id.prevStateHealthTV3);
        schemIdTV3 = (TextView) v.findViewById(R.id.prevStateHealthIDTV3);


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
        relationList = SeccDatabase.getRelationList(context);
        dashboardDropdown(v);
        zoomView = new ZoomView(this);
        zoomView.addView(v);
        mZoomLinearLayout.addView(zoomView);

        if (selectedMember.getSeccMemberItem() != null) {
            seccItem = selectedMember.getSeccMemberItem();
            System.out.print(seccItem);
            if (seccItem.getName() != null)
                headerTV.setText(seccItem.getName());

            previewVerfication();


            for (int i = 0; i < AppUtility.getMaritalStatusCode().size(); i++) {
                if (seccItem.getReqMarritalStatCode() != null && seccItem.getReqMarritalStatCode().equalsIgnoreCase(AppUtility.getMaritalStatusCode().get(i))) {
                    reqMaritalTV.setText(AppUtility.getMaritalStatusLabel().get(i));
                    break;
                }
            }


           /* for(RelationItem item : relationList){
                if(seccItem.getReqGenderCode()!=null && seccItem.getReqRelationCode().equalsIgnoreCase(item.getRelationCode())){
                    reqRelationTV.setText(item.getRelationName());
                    break;
                }
            }*/

        } else if (selectedMember.getRsbyMemberItem() != null) {
            rsbyItem = selectedMember.getRsbyMemberItem();
        }
      /*  confrimBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent theIntent=null;
                if(seccItem!=null) {
                    if(selectedMember.getOldHeadMember()!=null){
                        SeccMemberItem oldHead=selectedMember.getOldHeadMember();
                        oldHead.setLockedSave(AppConstant.LOCKED+"");
                        AppUtility.showLog(AppConstant.LOG_STATUS,TAG," OLD Household name " +
                                ": "+oldHead.getName()+"" +
                                " Member Status "+oldHead.getMemStatus()+" House hold Status :" +
                                " "+oldHead.getHhStatus()+" Locked Save :"+oldHead.getLockedSave());
                        SeccDatabase.updateSeccMember(selectedMember.getOldHeadMember(),context);
                    }

                    seccItem.setLockedSave(AppConstant.LOCKED + "");
                    SeccDatabase.updateSeccMember(seccItem,context);
                    HouseHoldItem houseHoldItem=selectedMember.getHouseHoldItem();

                    if(SeccDatabase.checkUnderSurveyMember(context,houseHoldItem)){
                        houseHoldItem.setLockSave(AppConstant.SAVE+"");
                    }else{
                        houseHoldItem.setLockSave(AppConstant.LOCKED+"");
                    }

                    Log.d("Preview Activity"," household json : "+houseHoldItem.serialize());
                    selectedMember.setHouseHoldItem(houseHoldItem);
                    SeccDatabase.updateHouseHold(selectedMember.getHouseHoldItem(),context);
                    seccItem= SeccDatabase.getSeccMemberDetail(seccItem.getNhpsMemId(),context);
                    selectedMember.setSeccMemberItem(seccItem);
                    ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                            AppConstant.SELECTED_ITEM_FOR_VERIFICATION,selectedMember.serialize(),context);
                   // SeccDatabase.getSeccMemberDetail()
                    theIntent = new Intent(context, SECCFamilyListActivity.class);
                }
                theIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(theIntent);
                finish();
                leftTransition();
            }
        });*/
        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent theIntent;
                // if (seccItem != null && seccItem.getAadhaarStatus().equalsIgnoreCase("1")) {
                //  theIntent = new Intent(context, WithAadhaarActivity.class);
               /* } else {
                    theIntent = new Intent(context, WithoutAadhaarVerificationActivity.class);
                }*/
                // startActivity(theIntent);
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

    private void setupScreenWithoutZoom() {
        showNotification();
        selectedLocation = VerifierLocationItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.SELECTED_BLOCK, context));
        selectedMember = SelectedMemberItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.SELECTED_ITEM_FOR_VERIFICATION, context));
        storedLoginResponse = VerifierLoginResponse.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.VERIFIER_CONTENT, context));
        householdStatusList = SeccDatabase.getFamilyStatusList(context);
        memberStatusList = SeccDatabase.getMemberStatusList(context);
        relationList = SeccDatabase.getRelationList(context);
        hhdTV = (TextView) findViewById(R.id.prevHhdTV);
        syncLockDtTV = (TextView) findViewById(R.id.prevSyncLockDtTV);
        memberIdTV = (TextView) findViewById(R.id.prevMemberIdTV);
        printCardBT = (Button) findViewById(R.id.printCardBT);
        nhpsIdTV = (TextView) findViewById(R.id.prevNhpsIdTV);
        hofNameTV = (TextView) findViewById(R.id.prevHofTV);
        prevGovtEidTV = (TextView) findViewById(R.id.prevGovtEidTV);
        EidTV = (TextView) findViewById(R.id.EidTV);
        prevValidatedByTV = (TextView) findViewById(R.id.prevValidatedByTV);
        prevServeyedByTV = (TextView) findViewById(R.id.prevServeyedByTV);
        prevServeyedByTV.setVisibility(View.GONE);
        syncVersionTV = (TextView) findViewById(R.id.prevSyncVersionTV);
        prevHouseholdIDTV = (TextView) findViewById(R.id.prevHouseholdIDTV);
        memberPhotoLayout = (LinearLayout) findViewById(R.id.memberPhotoLayout);
        aadhaarStatusLayout = (LinearLayout) findViewById(R.id.adhaarStatusLayout);
        govtIdStatusLayout = (LinearLayout) findViewById(R.id.govtIdStatusLayout);
        mobileNoStatusLayout = (LinearLayout) findViewById(R.id.mobileNoStatusLayout);
        addSchemeDetLayout = (LinearLayout) findViewById(R.id.add_scheme_det_layout);
        healthSchemeLayout = (LinearLayout) findViewById(R.id.stateHealthSchemeLayout);
        healthSchemeheaderLayout = (RelativeLayout) findViewById(R.id.healthSchemeheaderLayout);
        nomineeDetailLayout = (LinearLayout) findViewById(R.id.nomineeDetailLayout);
        urnNoLayout = (LinearLayout) findViewById(R.id.urnNumberLayout);
        prevCaptureMode = (TextView) findViewById(R.id.prevCaptureMode);
        headerTV = (TextView) findViewById(R.id.centertext);
        memberPhotoIV = (ImageView) findViewById(R.id.prevMemberPhotoIV);
        govtIdIV = (ImageView) findViewById(R.id.idPhotoIV);
        nameTV = (TextView) findViewById(R.id.prevNameTV);
        addressTV = (TextView) findViewById(R.id.prevAddressTV);
        fatherNameTV = (TextView) findViewById(R.id.prevFatherNameTV);
        motherNameTV = (TextView) findViewById(R.id.prevMotherNameTV);
        prevDOB = (TextView) findViewById(R.id.prevDobTV);
        prevGenderTV = (TextView) findViewById(R.id.prevGenderTV);
        prevNameInRegionalTV = (TextView) findViewById(R.id.prevNameInReginalTV);
        relationTV = (TextView) findViewById(R.id.prevRelationTV);
        houseHoldStatTV = (TextView) findViewById(R.id.prevHouseHoldStatusTV);
        memberStatusTV = (TextView) findViewById(R.id.prevMemberStatusTV);
        aadhaarNumberTV = (TextView) findViewById(R.id.prevAadhaarNoTV);
        nameAsAadhaarTV = (TextView) findViewById(R.id.prevNameAsAadhaarTV);
        aadharStatusIV = (ImageView) findViewById(R.id.aadharStatusIV);
        govtIDTV = (TextView) findViewById(R.id.prevGovtIdNoTV);
        nameAsIdTV = (TextView) findViewById(R.id.prevGovtNameAsIdTV);
        prevNomineeNameTV = (TextView) findViewById(R.id.prevNomineeNameTV);
        prevNomineeRelTV = (TextView) findViewById(R.id.prevNomineeRelation);
        prevGuardianNameTV = (TextView) findViewById(R.id.prevGuardianNameTV);
        govtIdTypeTV = (TextView) findViewById(R.id.prevGovtIdTypeTV);
        mobileNoTV = (TextView) findViewById(R.id.prevMobileNoTV);
        mobileStatusTV = (TextView) findViewById(R.id.prevMobileStatTV);
        whoseMobileTV = (TextView) findViewById(R.id.prevWhoseMobileTV);
        urnNoTV = (TextView) findViewById(R.id.prevURNTV);
        healthSchemeTV1 = (TextView) findViewById(R.id.prevStateHealthTV1);
        schemIdTV1 = (TextView) findViewById(R.id.prevStateHealthIDTV1);
        healthSchemeTV2 = (TextView) findViewById(R.id.prevStateHealthTV2);
        schemIdTV2 = (TextView) findViewById(R.id.prevStateHealthIDTV2);
        healthSchemeTV3 = (TextView) findViewById(R.id.prevStateHealthTV3);
        schemIdTV3 = (TextView) findViewById(R.id.prevStateHealthIDTV3);
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
        relationList = SeccDatabase.getRelationList(context);
        dashboardDropdown();
        if (selectedMember.getSeccMemberItem() != null) {
            seccItem = selectedMember.getSeccMemberItem();
            System.out.print(seccItem);
            if (seccItem.getName() != null)
                headerTV.setText(seccItem.getName());

            previewVerfication();


            for (int i = 0; i < AppUtility.getMaritalStatusCode().size(); i++) {
                if (seccItem.getReqMarritalStatCode() != null && seccItem.getReqMarritalStatCode().equalsIgnoreCase(AppUtility.getMaritalStatusCode().get(i))) {
                    reqMaritalTV.setText(AppUtility.getMaritalStatusLabel().get(i));
                    break;
                }
            }

        } else if (selectedMember.getRsbyMemberItem() != null) {
            rsbyItem = selectedMember.getRsbyMemberItem();
        }

        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

    private void dashboardDropdown(View v) {
/*
        RelativeLayout menuLayout = (RelativeLayout) findViewById(R.id.menuLayout);
        menuLayout.setVisibility(View.VISIBLE);
*/

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

    private void dashboardDropdown() {
/*
        RelativeLayout menuLayout = (RelativeLayout) findViewById(R.id.menuLayout);
        menuLayout.setVisibility(View.VISIBLE);
*/

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

    private void previewVerfication() {
        boolean aadhaarflag = false, govtIdFlag = false;
        if (seccItem.getDataSource() != null && seccItem.getDataSource().trim().equalsIgnoreCase(AppConstant.RSBY_SOURCE)) {
            showRsbyDetail();
        } else {
            showSeccDetail();
        }

        if (seccItem != null && seccItem.getNhpsRelationCode() != null) {
            for (RelationItem item : relationList) {
                if (seccItem.getNhpsRelationCode().trim().equalsIgnoreCase(item.getRelationCode())) {
                    relationTV.setText(item.getRelationName());
                    break;
                }
            }
        }
        memberPhotoLayout.setVisibility(View.GONE);
        if (seccItem.getMemberPhoto1() != null && !seccItem.getMemberPhoto1().equalsIgnoreCase("-")) {
            memberPhotoLayout.setVisibility(View.VISIBLE);
            try {
                memberPhotoIV.setImageBitmap(AppUtility.convertStringToBitmap(seccItem.getMemberPhoto1()));
                memberPhotoIV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ShowImageInPopUp(AppUtility.convertStringToBitmap(seccItem.getMemberPhoto1()));
                    }
                });
            } catch (Exception e) {
            }
        } else {
        }
        if (seccItem.getSyncedStatus() != null && seccItem.getSyncedStatus().trim().equalsIgnoreCase(AppConstant.SYNCED_STATUS_MEMBER)) {
            if (seccItem.getNhpsMemId() != null) {
                memberIdTV.setText(seccItem.getNhpsMemId());
                nhpsIdTV.setText(seccItem.getNhpsId());
            }

        }
        ;
        if (seccItem.getSyncDt() != null) {
            String dateTime = DateTimeUtil.convertTimeMillisIntoStringDateNew(seccItem.getSyncDt(), AppConstant.SYNC_DATE_TIME);
            syncLockDtTV.setText(dateTime);
        }

        if (seccItem.getAppVersion() != null) {
            syncVersionTV.setText(seccItem.getAppVersion());
        }

        if (seccItem.getAadhaarVerifiedBy() != null && !seccItem.getAadhaarVerifiedBy().equalsIgnoreCase("")) {
            String aadhaarNo = "XXXXXXXX" + seccItem.getAadhaarVerifiedBy().trim().substring(8);
            prevValidatedByTV.setText(aadhaarNo);
        }

        if (seccItem.getSyncedStatus() != null && !seccItem.getSyncedStatus().equalsIgnoreCase("")) {
            prevServeyedByTV.setVisibility(View.VISIBLE);
            if (storedLoginResponse.getHnoAadhaarNo() != null && !storedLoginResponse.getHnoAadhaarNo().equalsIgnoreCase("")) {
                String aadhaarNo = "XXXXXXXX" + storedLoginResponse.getHnoAadhaarNo().trim().substring(8);
                prevServeyedByTV.setText(aadhaarNo);
            }
        }

        aadhaarStatusLayout.setVisibility(View.GONE);
        govtIdStatusLayout.setVisibility(View.GONE);
        if (seccItem.getAadhaarStatus() != null && seccItem.getAadhaarStatus().equalsIgnoreCase("1")) {
            aadhaarflag = true;
            govtIdFlag = false;

            if (seccItem.getAadhaarNo() != null && !seccItem.getAadhaarNo().equalsIgnoreCase("")) {
                //    Log.d("Preview ","Aadhaar Number : "+seccItem.getAadhaarNo());
                aadhaarStatusLayout.setVisibility(View.VISIBLE);
                String aadhaarNo = "XXXXXXXX" + seccItem.getAadhaarNo().substring(8);
                aadhaarNumberTV.setText(aadhaarNo);
                if (seccItem.getAadhaarAuth() != null)
                    //aadhaarStatusTV.setText(seccItem.getAadhaarAuth());
                    if (seccItem.getAadhaarAuth().equalsIgnoreCase(AppConstant.AADHAAR_AUTH_YES)) {
                        aadharStatusIV.setVisibility(View.VISIBLE);
                        aadharStatusIV.setImageDrawable(context.getResources().getDrawable(R.drawable.right_tick));
                    } else {
                        aadharStatusIV.setVisibility(View.VISIBLE);
                        aadharStatusIV.setImageDrawable(context.getResources().getDrawable(R.drawable.exclamation));
                    }/*else if(seccItem.getAadhaarAuth().equalsIgnoreCase()){
                    aadharStatusIV.setImageDrawable(context.getResources().getDrawable(R.drawable.exclamation));
                }*/
                if (seccItem.getNameAadhaar() != null)
                    nameAsAadhaarTV.setText(seccItem.getNameAadhaar());

                if (seccItem.getAadhaarCapturingMode() != null && seccItem.getAadhaarCapturingMode().trim().equalsIgnoreCase(AppConstant.QR_CODE_MODE)) {
                    //prevCaptureMode.setText(seccItem.getAadhaarCapturingMode());
                    prevCaptureMode.setText("QR");
                } else if (seccItem.getAadhaarCapturingMode() != null && seccItem.getAadhaarCapturingMode().trim().equalsIgnoreCase(AppConstant.MANUAL_MODE)) {
                    prevCaptureMode.setText(seccItem.getAadhaarCapturingMode());
                }
            }
        } else {
            //Log.d("Preview","Govt Id : "+seccItem.getIdNo());

            govtIdIV.setVisibility(View.VISIBLE);
            //Log.d("Preview","Govt Id photo : "+seccItem.getGovtIdPhoto());
            if (seccItem.getIdType() != null && !seccItem.getIdType().equalsIgnoreCase("")) {
                aadhaarflag = false;
                govtIdFlag = true;
                govtIdStatusLayout.setVisibility(View.VISIBLE);
                for (GovernmentIdItem item : AppUtility.prepareGovernmentIdSpinner()) {
                    if (seccItem.getIdType().trim().equalsIgnoreCase(item.statusCode + "")) {
                        govtIdTypeTV.setText(item.status);
                        break;
                    }
                }
                if (seccItem.getEid() != null && !seccItem.getEid().equalsIgnoreCase("")) {
                    if (seccItem.getEid().length() > 13) {
                        prevGovtEidTV.setText(seccItem.getEid());
                        EidTV.setText("Enrolment ID ");
                    } else {
                        prevGovtEidTV.setText(seccItem.getEid());
                        EidTV.setText("Mobile No.");
                    }
                }
                if (seccItem.getIdNo() != null)
                    govtIDTV.setText(seccItem.getIdNo());
                if (seccItem.getNameAsId() != null) {
                    nameAsIdTV.setText(seccItem.getNameAsId());
                }
            }
            if (seccItem.getGovtIdPhoto() != null && !seccItem.getGovtIdPhoto().equalsIgnoreCase("enrollement")) {
                govtIdIV.setVisibility(View.VISIBLE);
                try {
                    govtIdIV.setImageBitmap(AppUtility.convertStringToBitmap(seccItem.getGovtIdPhoto()));
                    govtIdIV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ShowImageInPopUp(AppUtility.convertStringToBitmap(seccItem.getGovtIdPhoto()));
                        }
                    });
                } catch (Exception e) {

                }
            } else {

            }

        }

        ////
        // showing print card button
        //
        if (seccItem.getSyncedStatus() != null && seccItem.getSyncDt() != null) {
            if (seccItem.getNhpsRelationCode() != null && seccItem.getNhpsRelationCode().equalsIgnoreCase("01")) {
                printCardBT.setVisibility(View.GONE);
                printCardBT.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //  if (seccItem.getName() != null && !seccItem.getName().equalsIgnoreCase("")) {
                        //    if (seccItem.getMemberPhoto1() != null && !seccItem.getMemberPhoto1().equalsIgnoreCase("")) {
                        //      if (seccItem.getFathername() != null && !seccItem.getFathername().equalsIgnoreCase("")) {
                        //        if (seccItem.getDob() != null && !seccItem.getDob().equalsIgnoreCase("")) {
                        //          if (seccItem.getGenderid() != null && !seccItem.getGenderid().equalsIgnoreCase("")) {
                        Intent theIntent = new Intent(context, PrintCardMainActivity.class);
                        theIntent.putExtra(AppConstant.sendingPrintData, seccItem);
                        startActivity(theIntent);
                    /*        } else {
                                AppUtility.alertWithOk(context, "Member gender not available");
                            }
                        } else {
                            AppUtility.alertWithOk(context, "Member date of birth not available");
                        }
                    } else {
                        AppUtility.alertWithOk(context, "Member father name not available");
                    }
                } else {
                    AppUtility.alertWithOk(context, "Member photo not available");
                }
            } else {
                AppUtility.alertWithOk(context, "Member name not available");

            }

*/
                    }
                });
            }
        }

        mobileNoStatusLayout.setVisibility(View.GONE);
        if (seccItem.getMobileNo() != null && !seccItem.getMobileNo().equalsIgnoreCase("")) {
            mobileNoStatusLayout.setVisibility(View.VISIBLE);
            String mobileNo = "XXXXXX" + seccItem.getMobileNo().substring(6);
            mobileNoTV.setText(mobileNo);
            if (seccItem.getMobileAuth() != null && seccItem.getMobileAuth().trim().equalsIgnoreCase(AppConstant.VALID_STATUS)) {
                mobileStatusTV.setText("Verifed");
            } else {
                mobileStatusTV.setText("Not Verified");

            }
            for (WhoseMobileItem item : AppUtility.prepareWhoseMobile()) {
                if (seccItem.getWhoseMobile().equalsIgnoreCase(item.getStatusCode())) {
                    whoseMobileTV.setText(item.getStatusType());
                    break;
                }
            }
        } else {
        }
       /* urnNoLayout.setVisibility(View.GONE);
        healthSchemeLayout.setVisibility(View.GONE);*/
        healthSchemeheaderLayout.setVisibility(View.GONE);
        if (seccItem.getUrnNo() != null && !seccItem.getUrnNo().equalsIgnoreCase("")) {
            healthSchemeheaderLayout.setVisibility(View.VISIBLE);
            // urnNoLayout.setVisibility(View.VISIBLE);
            urnNoTV.setText(seccItem.getUrnNo());
        } else {

        }

      /*  if (seccItem.getSchemeId() != null && !seccItem.getSchemeId().equalsIgnoreCase("")) {*/
        if ((seccItem.getSchemeId1() != null && !seccItem.getSchemeId1().equalsIgnoreCase("")) ||
                (seccItem.getSchemeId2() != null && !seccItem.getSchemeId2().equalsIgnoreCase("")) ||
                (seccItem.getSchemeId3() != null && !seccItem.getSchemeId3().equalsIgnoreCase(""))) {
            // healthSchemeLayout.setVisibility(View.VISIBLE);
            healthSchemeheaderLayout.setVisibility(View.VISIBLE);
            for (HealthSchemeItem item : SeccDatabase.getHealthSchemeList(context, selectedLocation.getStateCode().trim())) {
                if (seccItem.getSchemeId1() != null && seccItem.getSchemeId1().equalsIgnoreCase(item.getSchemeId())) {
                    healthSchemeTV1.setText(item.getSchemeName());
                    if (seccItem.getSchemeNo1() != null) {
                        schemIdTV1.setText(seccItem.getSchemeNo1());
                    }
                }
                if (seccItem.getSchemeId2() != null && seccItem.getSchemeId2().equalsIgnoreCase(item.getSchemeId())) {
                    healthSchemeTV2.setText(item.getSchemeName());
                    if (seccItem.getSchemeNo2() != null) {
                        schemIdTV2.setText(seccItem.getSchemeNo2());

                    }
                }
                if (seccItem.getSchemeId3() != null && seccItem.getSchemeId3().equalsIgnoreCase(item.getSchemeId())) {
                    healthSchemeTV3.setText(item.getSchemeName());
                    if (seccItem.getSchemeNo3() != null) {
                        schemIdTV3.setText(seccItem.getSchemeNo3());
                    }
                }
            }
        } else {
        }
        nomineeDetailLayout.setVisibility(View.GONE);
        if (seccItem.getRelationNomineeCode() != null && !seccItem.getRelationNomineeCode().equalsIgnoreCase("")) {
            nomineeDetailLayout.setVisibility(View.VISIBLE);
            /*if(seccItem.getNameNominee()!=null)
              *//* prevNomineeNameTV.setText(seccItem.getNameNominee());*//*
                prevNomineeNameTV.setText(getNomineeName(seccItem.getHhdNo(),seccItem.getNameNominee()));
                if(seccItem.getNomineeRelationName()!=null){
                prevNomineeRelTV.setText(seccItem.getNomineeRelationName());
            }*/
            if (seccItem.getRelationNomineeCode() != null && !seccItem.getRelationNomineeCode().equalsIgnoreCase("")) {
                nomineeDetailLayout.setVisibility(View.VISIBLE);
                if (seccItem.getNameNominee() != null) {
                    if (seccItem.getDataSource() != null &&
                            seccItem.getDataSource().trim().equalsIgnoreCase(AppConstant.RSBY_SOURCE)) {
                        prevNomineeNameTV.setText(getRsbyNomineeName(seccItem.getRsbyUrnId(), seccItem.getNameNominee()));

                    } else {
                        prevNomineeNameTV.setText(getNomineeName(seccItem.getHhdNo(), seccItem.getNameNominee()));
                    }
                }
                prevNomineeRelTV.setText("-");
                if (seccItem.getRelationNomineeCode() != null) {
                    for (RelationItem item : relationList) {
                        if (item.getRelationCode().equalsIgnoreCase(seccItem.getRelationNomineeCode().trim())) {
                            //prevNomineeRelTV.setText(item.getRelationName()+"("+seccItem.getNomineeRelationName()+")");
                            if (item.getRelationName().equalsIgnoreCase(seccItem.getNomineeRelationName())) {
                                prevNomineeRelTV.setText(item.getRelationName());
                            } else {
                                prevNomineeRelTV.setText(item.getRelationName() + "(" + seccItem.getNomineeRelationName() + ")");
                            }
                            break;
                        }
                    }
                }
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
                if (govtIdFlag) {
                    memberStatusTV.setText(memStatItem.getStatusDesc() + " " + getString(R.string.without_aadhaar));
                } else if (aadhaarflag) {
                    memberStatusTV.setText(memStatItem.getStatusDesc() + " " + getString(R.string.with_aadhaar1));
                } else {
                    memberStatusTV.setText(memStatItem.getStatusDesc());
                }

            }
        }
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

    private String getNomineeName(String HhdNo, String nomineeName) {

        String modifiedNomineeDropdownCaption = nomineeName;
        ArrayList<SeccMemberItem> nomineeList = SeccDatabase.getSeccMemberList(HhdNo, context);

        for (SeccMemberItem item : nomineeList) {
            if (item.getName() != null && !item.getName().equalsIgnoreCase("")) {
                if (item.getName().equalsIgnoreCase(nomineeName)) {
                    if (item.getMemStatus() != null && (item.getMemStatus().trim().equalsIgnoreCase("") || item.getMemStatus().trim().equalsIgnoreCase(AppConstant.MEMBERFOUND_BUT_NOT_PRESENT) || item.getMemStatus().trim().equalsIgnoreCase(AppConstant.MEMBERFOUND_AND_PRESENT) || item.getMemStatus() == null)) {
                        if (!item.getName().equalsIgnoreCase("")) {
                            String yearOfBirth = null;
                            String gender;

                            if (item.getDob() != null && !item.getDob().equalsIgnoreCase("") && !item.getDob().equalsIgnoreCase("-")) {
                                yearOfBirth = item.getDob().substring(0, 4);
                            } else if (item.getDobFrmNpr() != null && !item.getDobFrmNpr().equalsIgnoreCase("") && !item.getDobFrmNpr().equalsIgnoreCase("-")) {
                                yearOfBirth = item.getDob().substring(0, 4);
                            }
                            if (item.getGenderid() != null && item.getGenderid().equalsIgnoreCase(AppConstant.MALE_GENDER)) {
                                gender = "M";
                            } else if (item.getGenderid() != null && item.getGenderid().equalsIgnoreCase(AppConstant.FEMALE_GENDER)) {
                                gender = "F";
                            } else {
                                gender = "O";
                            }
                            int age = 0;
                            if (yearOfBirth != null) {
                                age = AppConstant.COMPARED_YEAR - Integer.parseInt(yearOfBirth);
                                if (age < 18) {
                                    prevGuardianNameTV.setText(seccItem.getNomineeGaurdianName());
                                }
                            }
                            modifiedNomineeDropdownCaption = item.getName() + " - (" + gender + ") " + age + " yrs";
                            //  AppUtility.showLog(AppConstant.LOG_STATUS, TAG, " Nominee Label :" + ite1.getNomineeLabel());

                        }
                    }
                }
            }

        }
        return modifiedNomineeDropdownCaption;
    }

    private String getRsbyNomineeName(String urnNo, String nomineeName) {
        String modifiedNomineeDropdownCaption = nomineeName;
        ArrayList<SeccMemberItem> nomineeList = SeccDatabase.getRsbyMemberListWithUrn(urnNo, context);
        for (SeccMemberItem item : nomineeList) {
            if (item.getName() != null && !item.getName().equalsIgnoreCase("")) {
                if (item.getName().equalsIgnoreCase(nomineeName)) {
                    if (!item.getName().equalsIgnoreCase("")) {
                        String yearOfBirth = null;
                        String gender;

                        if (item.getRsbyDob() != null && !item.getRsbyDob().equalsIgnoreCase("") && !item.getRsbyDob().equalsIgnoreCase("-")) {
                            yearOfBirth = item.getRsbyDob().substring(0, 4);
                        }
                        if (item.getRsbyGender() != null && item.getRsbyGender().equalsIgnoreCase(AppConstant.MALE_GENDER)) {
                            gender = "M";
                        } else if (item.getRsbyGender() != null && item.getRsbyGender().equalsIgnoreCase(AppConstant.FEMALE_GENDER)) {
                            gender = "F";
                        } else {
                            gender = "O";
                        }
                        int age = 0;
                        if (yearOfBirth != null) {
                            age = AppConstant.COMPARED_YEAR - Integer.parseInt(yearOfBirth);
                            if (age < 18) {
                                prevGuardianNameTV.setText(seccItem.getNomineeGaurdianName());
                            }
                        }
                        modifiedNomineeDropdownCaption = item.getName() + " - (" + gender + ") " + age + " yrs";
                        //  AppUtility.showLog(AppConstant.LOG_STATUS, TAG, " Nominee Label :" + ite1.getNomineeLabel());

                    }
                }
            }

        }
        return modifiedNomineeDropdownCaption;
    }

    private void showRsbyDetail() {
        if (seccItem.getHhdNo() != null) {
            prevHouseholdIDTV.setText(seccItem.getHhdNo());
        }
        if (seccItem.getName() != null)
            nameTV.setText(seccItem.getName());
        if (seccItem.getFathername() != null)
            fatherNameTV.setText(seccItem.getFathername());
        if (seccItem.getMothername() != null)
            motherNameTV.setText(seccItem.getMothername());
       /* if (seccItem.getRelation() != null)
            relationTV.setText(seccItem.getRelation());*/

        if (seccItem.getNameSl() != null)
            prevNameInRegionalTV.setText(Html.fromHtml(seccItem.getNameSl()));
        if (seccItem.getRsbyDob() != null)
            prevDOB.setText(seccItem.getRsbyDob());

        if (seccItem.getRsbyGender() != null) {
            if (seccItem.getRsbyGender().equalsIgnoreCase("1")) {
                prevGenderTV.setText("Male");
            } else if (seccItem.getRsbyGender().equalsIgnoreCase("2")) {
                prevGenderTV.setText("Female");
            } else if (seccItem.getRsbyGender().equalsIgnoreCase("3")) {
                prevGenderTV.setText("Other");
            }
        }

        String address = "";
        if (seccItem.getAddressline1() != null) {
            address = address + "" + seccItem.getAddressline1();
        }
        if (seccItem.getAddressline2() != null) {
            address = address + "," + seccItem.getAddressline2();
        }
        if (seccItem.getAddressline3() != null) {
            address = address + "," + seccItem.getAddressline3();
        }
        if (seccItem.getAddressline4() != null) {
            address = address + "," + seccItem.getAddressline4();
        }
        addressTV.setText(address);
        SeccMemberItem hofItem = AppUtility.findHof(seccItem, context);
        if (hofItem != null) {
            hofNameTV.setText(hofItem.getName());
        }
    }

    private void showSeccDetail() {
        if (seccItem.getHhdNo() != null) {
            prevHouseholdIDTV.setText(seccItem.getHhdNo());
        }
        if (seccItem.getName() != null)
            nameTV.setText(seccItem.getName());
        if (seccItem.getFathername() != null)
            fatherNameTV.setText(seccItem.getFathername());
        if (seccItem.getMothername() != null)
            motherNameTV.setText(seccItem.getMothername());
        if (seccItem.getRelation() != null)
            relationTV.setText(seccItem.getRelation());

        if (seccItem.getNameSl() != null)
            prevNameInRegionalTV.setText(Html.fromHtml(seccItem.getNameSl()));
        if (seccItem.getDob() != null)
            prevDOB.setText(seccItem.getDob());

        if (seccItem.getGenderid() != null) {
            if (seccItem.getGenderid().equalsIgnoreCase("1")) {
                prevGenderTV.setText("Male");
            } else if (seccItem.getGenderid().equalsIgnoreCase("2")) {
                prevGenderTV.setText("Female");
            } else if (seccItem.getGenderid().equalsIgnoreCase("3")) {
                prevGenderTV.setText("Other");
            }
        }

        String address = "";
        if (seccItem.getAddressline1() != null) {
            address = address + "" + seccItem.getAddressline1();
        }
        if (seccItem.getAddressline2() != null) {
            address = address + "," + seccItem.getAddressline2();
        }
        if (seccItem.getAddressline3() != null) {
            address = address + "," + seccItem.getAddressline3();
        }
        if (seccItem.getAddressline4() != null) {
            address = address + "," + seccItem.getAddressline4();
        }
        addressTV.setText(address);
        SeccMemberItem hofItem = AppUtility.findHof(seccItem, context);
        if (hofItem != null) {
            hofNameTV.setText(hofItem.getName());
        }
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

    public void showNotification() {

        LinearLayout notificationLayout = (LinearLayout) findViewById(R.id.notificationLayout);
        WebView notificationWebview = (WebView) findViewById(R.id.notificationWebview);
        String prePairedMessage = AppUtility.getNotificationData(context);
        if (prePairedMessage != null) {
            notificationLayout.setVisibility(View.VISIBLE);
            notificationWebview.loadData(prePairedMessage, "text/html", "utf-8"); // Set focus to textview
        }
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
