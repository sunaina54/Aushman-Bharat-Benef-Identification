package com.nhpm.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.customComponent.CustomAlert;
import com.customComponent.utility.ProjectPrefrence;
import com.nhpm.LocalDataBase.dto.SeccDatabase;
import com.nhpm.Models.response.RelationItem;
import com.nhpm.Models.response.master.ConfigurationItem;
import com.nhpm.Models.response.master.MemberStatusItem;
import com.nhpm.Models.response.master.StateItem;
import com.nhpm.Models.response.seccMembers.HouseHoldItem;
import com.nhpm.Models.response.seccMembers.SeccMemberItem;
import com.nhpm.Models.response.seccMembers.SelectedMemberItem;
import com.nhpm.Models.response.verifier.VerifierLoginResponse;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;
import com.nhpm.Utility.AppUtility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import pl.polidea.view.ZoomView;

public class SeccMemberDetailActivity extends BaseActivity implements ComponentCallbacks2 {
    public static int SECC_MEMBER_DETAIL = 2;
    private TextView headerTV;
    private ImageView settings;
    private Button verifyBT;
    private Context context;
    private RelativeLayout backLayout;
    private ArrayList<MemberStatusItem> memberStatusList;
    private Spinner memberStatusSP;
    private LinearLayout memberStatusLayout;
    private TextView verifyWithAadhaarBT, verifyWithoutAadhaarBT;
    private SeccMemberItem selectedMemberItem;
    private VerifierLoginResponse loginDetail;
    private HouseHoldItem houseHoldItem;
    private TextView houseHoldNoTV, nameTV, relationTV, fatherNameTV, motherNameTV, occupatTV,
            dobTV, genderTV, maritalStatTV, nameAsRegionalTV, hofNameTV;
    private ImageView backIV;
    private RelativeLayout withAadhaarLayout, withoutAadharLayout;
    private LinearLayout updateLayout;
    private SelectedMemberItem selectedMemItem;
    private int selectedMemberStatus;
    private Button updateBT, lockBT;
    private RelativeLayout withAadharIV, withoutAadhaarIV;
    private TextView newRelationTV;
    private ArrayList<RelationItem> relationList;
    private Spinner newRelSP;
    private RelationItem newRelItem;
    private RelativeLayout newRelationLayout;
    private AlertDialog dialog, askForPinDailog;
    private LinearLayout mZoomLinearLayout;
    private ZoomView zoomView;
    private RelativeLayout menuLayout;
    private LinearLayout hhdIdLayout, urnIdLayout, fathersNameLayout;
    private Activity activity;
    // private TextView dashBoardNavBT;
    private boolean pinLockIsShown = false;

    private String TAG = "Secc Member activity";
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
        activity = this;
        checkAppConfig();

        if (zoomMode.equalsIgnoreCase("N")) {
            setContentView(R.layout.activity_secc_member_detail);
            setupScreenWithOutZoom();

        } else {
            setContentView(R.layout.dummy_layout_for_zooming);
            setupScreenWithZoom();

        }

    }

    private void setupScreenWithZoom() {

        mZoomLinearLayout = (LinearLayout) findViewById(R.id.mZoomLinearLayout);
        View v = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.activity_secc_member_detail, null, false);
        v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
        showNotification(v);
        //  selectedMemberItem=(SeccMemberItem)getIntent().getSerializableExtra(AppConstant.SELECTED_MEMBER);
        /*selectedMemberItem=SeccMemberItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.SELECTED_ITEM_FOR_VERIFICATION, context));*/
        selectedMemItem = SelectedMemberItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.SELECTED_ITEM_FOR_VERIFICATION, context));


        loginDetail = VerifierLoginResponse.create(ProjectPrefrence.getSharedPrefrenceData(
                AppConstant.PROJECT_PREF, AppConstant.VERIFIER_CONTENT, context));
        relationList = SeccDatabase.getRelationList(context);
        if (selectedMemItem.getSeccMemberItem() != null) {
            selectedMemberItem = selectedMemItem.getSeccMemberItem();
            selectedMemberItem.setAadhaarVerifiedBy(loginDetail.getAadhaarNumber());
            houseHoldItem = selectedMemItem.getHouseHoldItem();
        }


        settings = (ImageView) v.findViewById(R.id.settings);
        backLayout = (RelativeLayout) v.findViewById(R.id.backLayout);
        headerTV = (TextView) v.findViewById(R.id.centertext);
        newRelationLayout = (RelativeLayout) v.findViewById(R.id.newRelationLayout);
        hhdIdLayout = (LinearLayout) v.findViewById(R.id.hhdIdLayout);
        urnIdLayout = (LinearLayout) v.findViewById(R.id.urnIdLayout);
        newRelationLayout.setVisibility(View.GONE);
        dashboardDropdown(v);
        if (selectedMemItem.getNewHeadMember() != null) {
            //newRelationLayout.setVisibility(View.VISIBLE);
//NEW HEAD FLOW
            if (selectedMemberItem.getNhpsRelationCode() == null || !selectedMemberItem.getNhpsRelationCode().equalsIgnoreCase(AppConstant.NEW_HEAD_RELATION_CODE)) {
                newRelationLayout.setVisibility(View.VISIBLE);
            }
        } else {
//OLD HEAD FLOW
            AppUtility.showLog(AppConstant.LOG_STATUS, TAG, " OLD HEAD FLOW..");
            if (selectedMemberItem.getNhpsRelationCode() == null || !selectedMemberItem.getNhpsRelationCode().equalsIgnoreCase(AppConstant.NEW_HEAD_RELATION_CODE)) {
                newRelationLayout.setVisibility(View.VISIBLE);
            }

        }

        newRelSP = (Spinner) v.findViewById(R.id.newRelSP);
        newRelationTV = (TextView) v.findViewById(R.id.newRelationTV);
        memberStatusSP = (Spinner) v.findViewById(R.id.memberStatusSP);
        memberStatusLayout = (LinearLayout) v.findViewById(R.id.verifyLayout);
        houseHoldNoTV = (TextView) v.findViewById(R.id.hhldTV);
        nameAsRegionalTV = (TextView) v.findViewById(R.id.regionalNameTV);
        nameTV = (TextView) v.findViewById(R.id.nameTV);
        relationTV = (TextView) v.findViewById(R.id.relTV);
        fathersNameLayout = (LinearLayout) v.findViewById(R.id.fathersNameLayout);
        fatherNameTV = (TextView) v.findViewById(R.id.fatherNameTV);
        motherNameTV = (TextView) v.findViewById(R.id.motherNameTV);
        hofNameTV = (TextView) v.findViewById(R.id.hofNameTV);
        occupatTV = (TextView) v.findViewById(R.id.occupTV);
        dobTV = (TextView) v.findViewById(R.id.dob);
        genderTV = (TextView) v.findViewById(R.id.gender);
        maritalStatTV = (TextView) v.findViewById(R.id.marital);
        newRelationTV = (TextView) v.findViewById(R.id.newRelationTV);
        backIV = (ImageView) v.findViewById(R.id.back);
        menuLayout = (RelativeLayout) v.findViewById(R.id.menuLayout);
        withAadhaarLayout = (RelativeLayout) v.findViewById(R.id.withAadharLayout);
        withoutAadharLayout = (RelativeLayout) v.findViewById(R.id.withoutAadharLayout);
        withAadharIV = (RelativeLayout) v.findViewById(R.id.withAadhaarIV);
        withoutAadhaarIV = (RelativeLayout) v.findViewById(R.id.withoutAadhaarIV);
        updateLayout = (LinearLayout) v.findViewById(R.id.updateMemberLayout);

        updateBT = (Button) v.findViewById(R.id.updateBT);
        lockBT = (Button) v.findViewById(R.id.lockBT);
        verifyWithAadhaarBT = (TextView) v.findViewById(R.id.verifyWithAadhaarTV);
        verifyWithoutAadhaarBT = (TextView) v.findViewById(R.id.verifyWithOutAadhaarTV);

        zoomView = new ZoomView(this);
        zoomView.addView(v);
        mZoomLinearLayout.addView(zoomView);
        if (houseHoldItem != null && houseHoldItem.getDataSource() != null
                && houseHoldItem.getDataSource().trim().equalsIgnoreCase(AppConstant.RSBY_SOURCE)) {
            showRsbyDetail(selectedMemberItem);
            if (houseHoldItem.getRsbyMemId() != null && houseHoldItem.getRsbyMemId().trim().equalsIgnoreCase(selectedMemberItem.getRsbyMemId())) {
                // edited by saurabh on 25.10.2017
                //memberStatusSP.setEnabled(false);
                if (selectedMemberItem.getLockedSave() != null && selectedMemberItem.getLockedSave().equalsIgnoreCase(AppConstant.LOCKED + "")) {
                    memberStatusSP.setEnabled(false);
                } else {
                    memberStatusSP.setEnabled(true);
                }
            } else {
                memberStatusSP.setEnabled(true);
            }
        } else {
            showSeccDetail(selectedMemberItem);
            if (houseHoldItem.getNhpsMemId() != null && houseHoldItem.getNhpsMemId().trim().equalsIgnoreCase(selectedMemberItem.getNhpsMemId())) {
                // edited by saurabh on 25.10.2017
                // memberStatusSP.setEnabled(false);

                if (selectedMemberItem.getLockedSave() != null && selectedMemberItem.getLockedSave().equalsIgnoreCase(AppConstant.LOCKED + "")) {
                    memberStatusSP.setEnabled(false);
                } else {
                    memberStatusSP.setEnabled(true);
                }
            } else {
                memberStatusSP.setEnabled(true);
            }
        }


        withAadharIV.setVisibility(View.GONE);
        withoutAadhaarIV.setVisibility(View.GONE);
        if (selectedMemberItem.getAadhaarStatus() != null && selectedMemberItem.getAadhaarStatus().equalsIgnoreCase("1")) {
            withAadharIV.setVisibility(View.GONE);
        } else if (selectedMemberItem.getAadhaarStatus() != null && selectedMemberItem.getAadhaarStatus().equalsIgnoreCase("2")) {
            withoutAadhaarIV.setVisibility(View.GONE);
        }
        headerTV.setText(context.getResources().getString(R.string.MemberDetail));
        prepareNomineeRelation();
        newRelSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    newRelationTV.setText("-");
                    newRelItem = null;
                    selectedMemberItem.setNhpsRelationCode(null);
                } else {
                    newRelItem = relationList.get(i);
                    newRelationTV.setText(newRelItem.getRelationName());
                    selectedMemberItem.setNhpsRelationCode(newRelItem.getRelationCode());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        updateBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent theIntent = null;
                if (selectedMemberItem != null) {
                 /*   if(selectedMemberItem.getNhpsRelationCode()!=null && selectedMemberItem.getNhpsRelationCode().equalsIgnoreCase("01")){
                        if(!selectedMemberItem.getMemStatus().equalsIgnoreCase(AppConstant.MEMBERFOUND_BUT_NOT_PRESENT) ||
                                !selectedMemberItem.getMemStatus().equalsIgnoreCase(AppConstant.MEMBERFOUND_BUT_NOT_PRESENT)) {
                            CustomAlert.alertWithOk(context, "Head of the family member should not be died or migrated");
                            return;
                        }
                    }
                    if(selectedMemberItem.getRelation()!=null && selectedMemberItem.getRelation().equalsIgnoreCase("HEAD")){

                        if(!selectedMemberItem.getMemStatus().equalsIgnoreCase(AppConstant.MEMBERFOUND_BUT_NOT_PRESENT) ||
                                !selectedMemberItem.getMemStatus().equalsIgnoreCase(AppConstant.MEMBERFOUND_BUT_NOT_PRESENT)) {
                            CustomAlert.alertWithOk(context, "Head of the family member should not be died or migrated");
                            return;
                        }
                    }*/
                    selectedMemberItem.setLockedSave(AppConstant.SAVE + "");

                    if (selectedMemberItem != null && selectedMemberItem.getDataSource() != null &&
                            selectedMemberItem.getDataSource().trim().equalsIgnoreCase(AppConstant.RSBY_SOURCE)) {
                        SeccDatabase.updateRsbyMember(selectedMemberItem, context);
                        selectedMemItem.setSeccMemberItem(selectedMemberItem);
                        HouseHoldItem houseHoldItem = selectedMemItem.getHouseHoldItem();
                        houseHoldItem.setLockSave(AppConstant.SAVE + "");
                   /* if(SeccDatabase.checkUnderSurveyMember(context,houseHoldItem.getHhdNo())){
                        houseHoldItem.setLockSave(AppConstant.SAVE+"");
                    }else{
                        houseHoldItem.setLockSave(AppConstant.LOCKED+"");
                    }
                    Log.d("Secc Member Detail "," Household Item json : "+houseHoldItem.serialize());*/
                        selectedMemItem.setHouseHoldItem(houseHoldItem);
                        SeccDatabase.updateRsbyHousehold(selectedMemItem.getHouseHoldItem(), context);
                    } else {
                        SeccDatabase.updateSeccMember(selectedMemberItem, context);
                        selectedMemItem.setSeccMemberItem(selectedMemberItem);
                        HouseHoldItem houseHoldItem = selectedMemItem.getHouseHoldItem();
                        houseHoldItem.setLockSave(AppConstant.SAVE + "");
                   /* if(SeccDatabase.checkUnderSurveyMember(context,houseHoldItem.getHhdNo())){
                        houseHoldItem.setLockSave(AppConstant.SAVE+"");
                    }else{
                        houseHoldItem.setLockSave(AppConstant.LOCKED+"");
                    }
                    Log.d("Secc Member Detail "," Household Item json : "+houseHoldItem.serialize());*/
                        selectedMemItem.setHouseHoldItem(houseHoldItem);
                        SeccDatabase.updateHouseHold(selectedMemItem.getHouseHoldItem(), context);
                    }
                    ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                            AppConstant.SELECTED_ITEM_FOR_VERIFICATION, selectedMemItem.serialize(), context);
                    theIntent = new Intent(context, SeccMemberListActivity.class);
                    startActivity(theIntent);
                    finish();
                    rightTransition();
                }

            }
        });
        lockBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent theIntent = null;
                if (selectedMemberItem.getNhpsRelationCode() != null && selectedMemberItem.getNhpsRelationCode().equalsIgnoreCase("01")) {
                    if (!selectedMemberItem.getMemStatus().equalsIgnoreCase(AppConstant.MEMBERFOUND_BUT_NOT_PRESENT) ||
                            !selectedMemberItem.getMemStatus().equalsIgnoreCase(AppConstant.MEMBERFOUND_BUT_NOT_PRESENT)) {
                        CustomAlert.alertWithOk(context, context.getResources().getString(R.string.hofShouldNotBeDied));
                        return;
                    }
                }


                /*if(selectedMemberItem.getRelation()!=null && selectedMemberItem.getRelation().equalsIgnoreCase("HEAD")){

                    if(!selectedMemberItem.getMemStatus().equalsIgnoreCase(AppConstant.MEMBERFOUND_BUT_NOT_PRESENT) ||
                            !selectedMemberItem.getMemStatus().equalsIgnoreCase(AppConstant.MEMBERFOUND_BUT_NOT_PRESENT)) {
                        CustomAlert.alertWithOk(context, "Head of the family member should not be died or migrated");
                        return;
                    }
                }*/

                //lockPrompt();
                askPinToLock();


            }
        });

        prepareMemberStatusSpinner();
        memberStatusSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                MemberStatusItem statItem = memberStatusList.get(position);
                AppUtility.showLog(AppConstant.LOG_STATUS, "Secc Member Activity", "Status Code:" + statItem.getStatusCode());
                if (statItem.getStatusCode().equalsIgnoreCase(AppConstant.MEMBERFOUND_BUT_NOT_PRESENT)) {
                    memberStatusLayout.setVisibility(View.VISIBLE);
                    updateLayout.setVisibility(View.GONE);
                    selectedMemberItem.setMemStatus(statItem.getStatusCode() + "");
                    //SeccDatabase.updateSeccMember(selectedMemberItem,context);
                } else if (statItem.getStatusCode().equalsIgnoreCase("0")) {
                    memberStatusLayout.setVisibility(View.GONE);
                    updateLayout.setVisibility(View.GONE);
                } else if (statItem.getStatusCode().equalsIgnoreCase(AppConstant.MEMBERFOUND_AND_PRESENT)) {
                    memberStatusLayout.setVisibility(View.VISIBLE);
                    updateLayout.setVisibility(View.GONE);
                    selectedMemberItem.setMemStatus(statItem.getStatusCode() + "");
                } else {
                    Log.d("SeccMemberDetail", "Member Status :" + statItem.getStatusCode());
                    memberStatusLayout.setVisibility(View.GONE);
                    updateLayout.setVisibility(View.VISIBLE);
                    selectedMemberItem.setMemStatus(statItem.getStatusCode() + "");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        /*verifyWithAadhaarBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWithAadhaar();
            }
        });*/

      /*  verifyWithoutAadhaarBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  openWithoutAadhaar();

            }
        });*/
        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent theIntent=new Intent(context,SECCFamilyListActivity.class);
                startActivity(theIntent);*/
                finish();
                rightTransition();
            }
        });
        menuLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //    backIV.performClick();
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
        verifyWithoutAadhaarBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWithoutAadhaar();

            }
        });
        withoutAadharLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyWithoutAadhaarBT.performClick();
            }
        });

    }

    private void setupScreenWithOutZoom() {
        showNotification();

        selectedMemItem = SelectedMemberItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.SELECTED_ITEM_FOR_VERIFICATION, context));


        loginDetail = VerifierLoginResponse.create(ProjectPrefrence.getSharedPrefrenceData(
                AppConstant.PROJECT_PREF, AppConstant.VERIFIER_CONTENT, context));
        relationList = SeccDatabase.getRelationList(context);
        if (selectedMemItem.getSeccMemberItem() != null) {
            selectedMemberItem = selectedMemItem.getSeccMemberItem();
            selectedMemberItem.setAadhaarVerifiedBy(loginDetail.getAadhaarNumber());
            houseHoldItem = selectedMemItem.getHouseHoldItem();
        }


        headerTV = (TextView) findViewById(R.id.centertext);
        newRelationLayout = (RelativeLayout) findViewById(R.id.newRelationLayout);
        hhdIdLayout = (LinearLayout) findViewById(R.id.hhdIdLayout);
        backLayout = (RelativeLayout) findViewById(R.id.backLayout);
        urnIdLayout = (LinearLayout) findViewById(R.id.urnIdLayout);
        settings = (ImageView) findViewById(R.id.settings);
        newRelationLayout.setVisibility(View.GONE);
        dashboardDropdown();
        if (selectedMemItem.getNewHeadMember() != null) {
            //newRelationLayout.setVisibility(View.VISIBLE);
//NEW HEAD FLOW
            if (selectedMemberItem.getNhpsRelationCode() == null || !selectedMemberItem.getNhpsRelationCode().equalsIgnoreCase(AppConstant.NEW_HEAD_RELATION_CODE)) {
                newRelationLayout.setVisibility(View.VISIBLE);
            }
        } else {
//OLD HEAD FLOW
            AppUtility.showLog(AppConstant.LOG_STATUS, TAG, " OLD HEAD FLOW..");
            if (selectedMemberItem.getNhpsRelationCode() == null || !selectedMemberItem.getNhpsRelationCode().equalsIgnoreCase(AppConstant.NEW_HEAD_RELATION_CODE)) {
                newRelationLayout.setVisibility(View.VISIBLE);
            }

        }

        newRelSP = (Spinner) findViewById(R.id.newRelSP);
        newRelationTV = (TextView) findViewById(R.id.newRelationTV);
        memberStatusSP = (Spinner) findViewById(R.id.memberStatusSP);
        memberStatusLayout = (LinearLayout) findViewById(R.id.verifyLayout);
        houseHoldNoTV = (TextView) findViewById(R.id.hhldTV);
        nameAsRegionalTV = (TextView) findViewById(R.id.regionalNameTV);
        nameTV = (TextView) findViewById(R.id.nameTV);
        relationTV = (TextView) findViewById(R.id.relTV);
        fathersNameLayout = (LinearLayout) findViewById(R.id.fathersNameLayout);
        fatherNameTV = (TextView) findViewById(R.id.fatherNameTV);
        motherNameTV = (TextView) findViewById(R.id.motherNameTV);
        hofNameTV = (TextView) findViewById(R.id.hofNameTV);
        occupatTV = (TextView) findViewById(R.id.occupTV);
        dobTV = (TextView) findViewById(R.id.dob);
        genderTV = (TextView) findViewById(R.id.gender);
        maritalStatTV = (TextView) findViewById(R.id.marital);
        newRelationTV = (TextView) findViewById(R.id.newRelationTV);
        backIV = (ImageView) findViewById(R.id.back);
        menuLayout = (RelativeLayout) findViewById(R.id.menuLayout);
        withAadhaarLayout = (RelativeLayout) findViewById(R.id.withAadharLayout);
        withoutAadharLayout = (RelativeLayout) findViewById(R.id.withoutAadharLayout);
        withAadharIV = (RelativeLayout) findViewById(R.id.withAadhaarIV);
        withoutAadhaarIV = (RelativeLayout) findViewById(R.id.withoutAadhaarIV);
        updateLayout = (LinearLayout) findViewById(R.id.updateMemberLayout);

        updateBT = (Button) findViewById(R.id.updateBT);
        lockBT = (Button) findViewById(R.id.lockBT);
        verifyWithAadhaarBT = (TextView) findViewById(R.id.verifyWithAadhaarTV);
        verifyWithoutAadhaarBT = (TextView) findViewById(R.id.verifyWithOutAadhaarTV);

       /* if (houseHoldItem != null && houseHoldItem.getDataSource() != null
                && houseHoldItem.getDataSource().trim().equalsIgnoreCase(AppConstant.RSBY_SOURCE)) {
            showRsbyDetail(selectedMemberItem);
            if (houseHoldItem.getRsbyMemId() != null && houseHoldItem.getRsbyMemId().trim().equalsIgnoreCase(selectedMemberItem.getRsbyMemId())) {
                memberStatusSP.setEnabled(false);
            } else {
                memberStatusSP.setEnabled(true);
            }
        } else {
            showSeccDetail(selectedMemberItem);
            if (houseHoldItem.getNhpsMemId() != null && houseHoldItem.getNhpsMemId().trim().equalsIgnoreCase(selectedMemberItem.getNhpsMemId())) {
                memberStatusSP.setEnabled(false);
            } else {
                memberStatusSP.setEnabled(true);
            }
        }
*/

        if (houseHoldItem != null && houseHoldItem.getDataSource() != null
                && houseHoldItem.getDataSource().trim().equalsIgnoreCase(AppConstant.RSBY_SOURCE)) {
            showRsbyDetail(selectedMemberItem);
            if (houseHoldItem.getRsbyMemId() != null && houseHoldItem.getRsbyMemId().trim().equalsIgnoreCase(selectedMemberItem.getRsbyMemId())) {
                // edited by saurabh on 25.10.2017
                //memberStatusSP.setEnabled(false);
                if (selectedMemberItem.getLockedSave() != null && selectedMemberItem.getLockedSave().equalsIgnoreCase(AppConstant.LOCKED + "")) {
                    memberStatusSP.setEnabled(false);
                } else {
                    memberStatusSP.setEnabled(true);
                }
            } else {
                memberStatusSP.setEnabled(true);
            }
        } else {
            showSeccDetail(selectedMemberItem);
            if (houseHoldItem.getNhpsMemId() != null && houseHoldItem.getNhpsMemId().trim().equalsIgnoreCase(selectedMemberItem.getNhpsMemId())) {
                // edited by saurabh on 25.10.2017
                // memberStatusSP.setEnabled(false);

                if (selectedMemberItem.getLockedSave() != null && selectedMemberItem.getLockedSave().equalsIgnoreCase(AppConstant.LOCKED + "")) {
                    memberStatusSP.setEnabled(false);
                } else {
                    memberStatusSP.setEnabled(true);
                }
            } else {
                memberStatusSP.setEnabled(true);
            }
        }

        withAadharIV.setVisibility(View.GONE);
        withoutAadhaarIV.setVisibility(View.GONE);
        if (selectedMemberItem.getAadhaarStatus() != null && selectedMemberItem.getAadhaarStatus().equalsIgnoreCase("1")) {
            withAadharIV.setVisibility(View.GONE);
        } else if (selectedMemberItem.getAadhaarStatus() != null && selectedMemberItem.getAadhaarStatus().equalsIgnoreCase("2")) {
            withoutAadhaarIV.setVisibility(View.GONE);
        }
        headerTV.setText(context.getResources().getString(R.string.MemberDetail));
        prepareNomineeRelation();
        newRelSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    newRelationTV.setText("-");
                    newRelItem = null;
                    selectedMemberItem.setNhpsRelationCode(null);
                } else {
                    newRelItem = relationList.get(i);
                    newRelationTV.setText(newRelItem.getRelationName());
                    selectedMemberItem.setNhpsRelationCode(newRelItem.getRelationCode());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        updateBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent theIntent = null;
                if (selectedMemberItem != null) {
                 /*   if(selectedMemberItem.getNhpsRelationCode()!=null && selectedMemberItem.getNhpsRelationCode().equalsIgnoreCase("01")){
                        if(!selectedMemberItem.getMemStatus().equalsIgnoreCase(AppConstant.MEMBERFOUND_BUT_NOT_PRESENT) ||
                                !selectedMemberItem.getMemStatus().equalsIgnoreCase(AppConstant.MEMBERFOUND_BUT_NOT_PRESENT)) {
                            CustomAlert.alertWithOk(context, "Head of the family member should not be died or migrated");
                            return;
                        }
                    }
                    if(selectedMemberItem.getRelation()!=null && selectedMemberItem.getRelation().equalsIgnoreCase("HEAD")){

                        if(!selectedMemberItem.getMemStatus().equalsIgnoreCase(AppConstant.MEMBERFOUND_BUT_NOT_PRESENT) ||
                                !selectedMemberItem.getMemStatus().equalsIgnoreCase(AppConstant.MEMBERFOUND_BUT_NOT_PRESENT)) {
                            CustomAlert.alertWithOk(context, "Head of the family member should not be died or migrated");
                            return;
                        }
                    }*/
                    selectedMemberItem.setLockedSave(AppConstant.SAVE + "");

                    if (selectedMemberItem != null && selectedMemberItem.getDataSource() != null &&
                            selectedMemberItem.getDataSource().trim().equalsIgnoreCase(AppConstant.RSBY_SOURCE)) {
                        SeccDatabase.updateRsbyMember(selectedMemberItem, context);
                        selectedMemItem.setSeccMemberItem(selectedMemberItem);
                        HouseHoldItem houseHoldItem = selectedMemItem.getHouseHoldItem();
                        houseHoldItem.setLockSave(AppConstant.SAVE + "");
                   /* if(SeccDatabase.checkUnderSurveyMember(context,houseHoldItem.getHhdNo())){
                        houseHoldItem.setLockSave(AppConstant.SAVE+"");
                    }else{
                        houseHoldItem.setLockSave(AppConstant.LOCKED+"");
                    }
                    Log.d("Secc Member Detail "," Household Item json : "+houseHoldItem.serialize());*/
                        selectedMemItem.setHouseHoldItem(houseHoldItem);
                        SeccDatabase.updateRsbyHousehold(selectedMemItem.getHouseHoldItem(), context);
                    } else {
                        SeccDatabase.updateSeccMember(selectedMemberItem, context);
                        selectedMemItem.setSeccMemberItem(selectedMemberItem);
                        HouseHoldItem houseHoldItem = selectedMemItem.getHouseHoldItem();
                        houseHoldItem.setLockSave(AppConstant.SAVE + "");
                   /* if(SeccDatabase.checkUnderSurveyMember(context,houseHoldItem.getHhdNo())){
                        houseHoldItem.setLockSave(AppConstant.SAVE+"");
                    }else{
                        houseHoldItem.setLockSave(AppConstant.LOCKED+"");
                    }
                    Log.d("Secc Member Detail "," Household Item json : "+houseHoldItem.serialize());*/
                        selectedMemItem.setHouseHoldItem(houseHoldItem);
                        SeccDatabase.updateHouseHold(selectedMemItem.getHouseHoldItem(), context);
                    }
                    ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                            AppConstant.SELECTED_ITEM_FOR_VERIFICATION, selectedMemItem.serialize(), context);
                    theIntent = new Intent(context, SeccMemberListActivity.class);
                    startActivity(theIntent);
                    finish();
                    rightTransition();
                }

            }
        });
        lockBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent theIntent = null;
                if (selectedMemberItem.getNhpsRelationCode() != null && selectedMemberItem.getNhpsRelationCode().equalsIgnoreCase("01")) {
                    if (!selectedMemberItem.getMemStatus().equalsIgnoreCase(AppConstant.MEMBERFOUND_BUT_NOT_PRESENT) ||
                            !selectedMemberItem.getMemStatus().equalsIgnoreCase(AppConstant.MEMBERFOUND_BUT_NOT_PRESENT)) {
                        CustomAlert.alertWithOk(context, context.getResources().getString(R.string.hofShouldNotBeDied));
                        return;
                    }
                }
                /*if(selectedMemberItem.getRelation()!=null && selectedMemberItem.getRelation().equalsIgnoreCase("HEAD")){

                    if(!selectedMemberItem.getMemStatus().equalsIgnoreCase(AppConstant.MEMBERFOUND_BUT_NOT_PRESENT) ||
                            !selectedMemberItem.getMemStatus().equalsIgnoreCase(AppConstant.MEMBERFOUND_BUT_NOT_PRESENT)) {
                        CustomAlert.alertWithOk(context, "Head of the family member should not be died or migrated");
                        return;
                    }
                }*/

                //lockPrompt();
                askPinToLock();


            }
        });

        prepareMemberStatusSpinner();
        memberStatusSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                MemberStatusItem statItem = memberStatusList.get(position);
                AppUtility.showLog(AppConstant.LOG_STATUS, "Secc Member Activity", "Status Code:" + statItem.getStatusCode());
                if (statItem.getStatusCode().equalsIgnoreCase(AppConstant.MEMBERFOUND_BUT_NOT_PRESENT)) {
                    memberStatusLayout.setVisibility(View.VISIBLE);
                    updateLayout.setVisibility(View.GONE);
                    selectedMemberItem.setMemStatus(statItem.getStatusCode() + "");
                    //SeccDatabase.updateSeccMember(selectedMemberItem,context);
                } else if (statItem.getStatusCode().equalsIgnoreCase("0")) {
                    memberStatusLayout.setVisibility(View.GONE);
                    updateLayout.setVisibility(View.GONE);
                } else if (statItem.getStatusCode().equalsIgnoreCase(AppConstant.MEMBERFOUND_AND_PRESENT)) {
                    memberStatusLayout.setVisibility(View.VISIBLE);
                    updateLayout.setVisibility(View.GONE);
                    selectedMemberItem.setMemStatus(statItem.getStatusCode() + "");
                } else {
                    Log.d("SeccMemberDetail", "Member Status :" + statItem.getStatusCode());
                    memberStatusLayout.setVisibility(View.GONE);
                    updateLayout.setVisibility(View.VISIBLE);
                    selectedMemberItem.setMemStatus(statItem.getStatusCode() + "");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        /*verifyWithAadhaarBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWithAadhaar();
            }
        });*/

      /*  verifyWithoutAadhaarBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  openWithoutAadhaar();

            }
        });*/
        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent theIntent=new Intent(context,SECCFamilyListActivity.class);
                startActivity(theIntent);*/
                finish();
                rightTransition();
            }
        });
        backLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backIV.performClick();
            }
        });
        menuLayout.setOnClickListener(new View.OnClickListener() {
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
        verifyWithoutAadhaarBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWithoutAadhaar();

            }
        });
        withoutAadharLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyWithoutAadhaarBT.performClick();
            }
        });

    }

    private void openWithoutAadhaar() {
        Intent theIntent = new Intent(context, WithAadhaarActivity.class);
        // theIntent.putExtra(AppConstant.MEMBER_TYPE,AppConstant.SECC_MEMBER);
        // selectedMemberItem.setAadhaarStatus("2");
        AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Relation Code : " + selectedMemItem.getOldHeadMember().getNhpsRelationCode());
        AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Update is calling : " +
                "" + selectedMemberItem.getNhpsRelationCode() + " : " + " Relation Item : " + newRelItem);
        if (selectedMemItem.getNewHeadMember() != null) {
            if (selectedMemberItem.getNhpsRelationCode() == null) {
                if (newRelItem == null) {
                    String headName = selectedMemItem.getNewHeadMember().getName();
                    AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Update is calling : " + "" + headName + " : " + " Relation Item : " + newRelItem);
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzSelectRelatonWith) + " " + headName);
                    return;
                }
            }
        } else {
            if (selectedMemItem.getOldHeadMember() != null) {
                if (selectedMemberItem.getNhpsRelationCode() == null) {
                    if (newRelItem == null) {
                        String headName = selectedMemItem.getOldHeadMember().getName();
                        AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Update is calling : " +
                                "" + headName + " : " + " Relation Item : " + newRelItem);
                        CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzSelectRelatonWith) + " " + headName);
                        return;
                    }

                }
            }
        }

        selectedMemItem.setSeccMemberItem(selectedMemberItem);
        // selectedMemberStatus=memberStatusSP.getSelectedItemPosition();
        // edited by saurabh on 25.10.2017
        selectedMemberItem.setMemStatus(memberStatusList.get(memberStatusSP.getSelectedItemPosition()).getStatusCode());
        selectedMemberItem.setLockedSave(AppConstant.SAVE + "");
        houseHoldItem.setLockSave(AppConstant.SAVE + "");
        if (houseHoldItem != null && houseHoldItem.getDataSource() != null &&
                houseHoldItem.getDataSource().trim().equalsIgnoreCase(AppConstant.RSBY_SOURCE)) {
            SeccDatabase.updateRsbyMember(selectedMemberItem, context);
            SeccDatabase.updateRsbyHousehold(houseHoldItem, context);
            selectedMemberItem = SeccDatabase.getSeccMemberDetail(selectedMemberItem, context);
            selectedMemItem.setSeccMemberItem(selectedMemberItem);
            selectedMemItem.setHouseHoldItem(houseHoldItem);
            AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Relation Code : " + selectedMemItem.getOldHeadMember().getNhpsRelationCode());

        } else {
            SeccDatabase.updateSeccMember(selectedMemberItem, context);
            SeccDatabase.updateHouseHold(houseHoldItem, context);
            selectedMemberItem = SeccDatabase.getSeccMemberDetail(selectedMemberItem, context);
            selectedMemItem.setSeccMemberItem(selectedMemberItem);
            selectedMemItem.setHouseHoldItem(houseHoldItem);
            AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Relation Code : " + selectedMemItem.getOldHeadMember().getNhpsRelationCode());

        }
        ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.SELECTED_ITEM_FOR_VERIFICATION, selectedMemItem.serialize(), context);
        startActivityForResult(theIntent, SECC_MEMBER_DETAIL);
        leftTransition();
        // finish();
    }

    private void prepareMemberStatusSpinner() {
        memberStatusList = new ArrayList<>();
        ArrayList<String> spinnerList = new ArrayList<>();
        ArrayList<MemberStatusItem> memberStatusList1 = SeccDatabase.getMemberStatusList(context);
        memberStatusList.add(0, new MemberStatusItem("M", "0", "Select Member Status", null, "Y"));
       /* ;
        memberStatusList.add(new MemberStatusItem(1,"Member Found"));
        memberStatusList.add(new MemberStatusItem(2,"Member Migrated"));
        memberStatusList.add(new MemberStatusItem(3,"Member Died"));
        memberStatusList.add(new MemberStatusItem(4,"No Information Available"));*/
        boolean headStatus = false;
        if (houseHoldItem != null && houseHoldItem.getDataSource() != null
                && houseHoldItem.getDataSource().trim().equalsIgnoreCase(AppConstant.RSBY_SOURCE)) {
            if (selectedMemberItem.getRsbyMemId() != null &&
                    selectedMemberItem.getRsbyMemId().equalsIgnoreCase(houseHoldItem.getRsbyMemId())) {
                headStatus = true;
            }
            if (selectedMemberItem.getNhpsRelationCode() != null &&
                    selectedMemberItem.getNhpsRelationCode().equalsIgnoreCase(AppConstant.NEW_HEAD_RELATION_CODE)) {
                headStatus = true;
            }

            if (selectedMemberItem.getRsbyName() != null && !selectedMemberItem.getRsbyName().equalsIgnoreCase("")) {

                //  if (selectedMemItem.getSeccMemberItem().getNhpsRelationCode() != null && selectedMemItem.getSeccMemberItem().getNhpsRelationCode().equalsIgnoreCase(AppConstant.NEW_HEAD_RELATION_CODE)) {

                if (headStatus) {
                    for (MemberStatusItem item : memberStatusList1) {

//                        Original code for hod status ps quik it
                        if (item.getStatusCode().equalsIgnoreCase(AppConstant.MEMBERFOUND_BUT_NOT_PRESENT)) {
                            //spinnerList.add(item.getStatusDesc());
                            memberStatusList.add(item);
                        }
                        if (item.getStatusCode().equalsIgnoreCase(AppConstant.MEMBERFOUND_AND_PRESENT)) {
                            memberStatusList.add(item);
                            // spinnerList.add(item.getStatusDesc());
                        }

////                        modify by me rajesh
//                        if (item.getStatusCode().equalsIgnoreCase(AppConstant.MEMBER_ENROL_THROUGH_RSBY) || item.getStatusCode().equalsIgnoreCase("14")) {
//                        } else {
//                            memberStatusList.add(item);
//                        }


                    }
                } else {
                    for (MemberStatusItem item : memberStatusList1) {
                        if (item.getStatusCode().equalsIgnoreCase(AppConstant.MEMBER_ENROL_THROUGH_RSBY) || item.getStatusCode().equalsIgnoreCase("14")) {
                        } else {
                            memberStatusList.add(item);
                        }
                    }
                }
            } else {
                AppUtility.showLog(AppConstant.LOG_STATUS, "Secc Member detai : ", "Name is blank..");
                for (MemberStatusItem item : memberStatusList1) {
                    if (item.getStatusCode().equalsIgnoreCase(AppConstant.NO_INFO_AVAIL)) {
                        memberStatusList.add(item);
                    }
                }
            }
        } else {
            if (selectedMemberItem.getNhpsMemId() != null &&
                    selectedMemberItem.getNhpsMemId().equalsIgnoreCase(houseHoldItem.getNhpsMemId())) {
                headStatus = true;
            }
            if (selectedMemberItem.getNhpsRelationCode() != null &&
                    selectedMemberItem.getNhpsRelationCode().equalsIgnoreCase(AppConstant.NEW_HEAD_RELATION_CODE)) {
                headStatus = true;
            }

            if (selectedMemberItem.getName() != null && !selectedMemberItem.getName().equalsIgnoreCase("")) {

                //  if (selectedMemItem.getSeccMemberItem().getNhpsRelationCode() != null && selectedMemItem.getSeccMemberItem().getNhpsRelationCode().equalsIgnoreCase(AppConstant.NEW_HEAD_RELATION_CODE)) {

                if (headStatus) {
                    for (MemberStatusItem item : memberStatusList1) {

//                        //original code psquik
                        if (item.getStatusCode().equalsIgnoreCase(AppConstant.MEMBERFOUND_BUT_NOT_PRESENT)) {
                            //spinnerList.add(item.getStatusDesc());
                            memberStatusList.add(item);
                        }
                        if (item.getStatusCode().equalsIgnoreCase(AppConstant.MEMBERFOUND_AND_PRESENT)) {
                            memberStatusList.add(item);
                            // spinnerList.add(item.getStatusDesc());
                        }
//                        //by rajesh kumar
//                        if (item.getStatusCode().equalsIgnoreCase(AppConstant.MEMBER_ENROL_THROUGH_RSBY)) {
//
//                        } else {
//                            memberStatusList.add(item);
//                        }

                    }
                } else {
                    for (MemberStatusItem item : memberStatusList1) {
                        if (item.getStatusCode().equalsIgnoreCase(AppConstant.MEMBER_ENROL_THROUGH_RSBY)) {

                        } else {
                            memberStatusList.add(item);
                        }
                    }
                }
            } else {
                AppUtility.showLog(AppConstant.LOG_STATUS, "Secc Member detai : ", "Name is blank..");
                for (MemberStatusItem item : memberStatusList1) {
                    if (item.getStatusCode().equalsIgnoreCase(AppConstant.NO_INFO_AVAIL)) {
                        memberStatusList.add(item);
                    }
                }
            }
        }


        for (MemberStatusItem item : memberStatusList) {
            spinnerList.add(item.getStatusDesc());
        }
        ArrayAdapter<String> maritalAdapter = new ArrayAdapter<>(context, R.layout.custom_drop_down, R.id.textView, spinnerList);
        memberStatusSP.setAdapter(maritalAdapter);
        if (selectedMemberItem != null) {
            for (int i = 0; i < memberStatusList.size(); i++) {
                if (selectedMemberItem.getMemStatus() != null && selectedMemberItem.getMemStatus().trim().equalsIgnoreCase(memberStatusList.get(i).getStatusCode())) {
                    selectedMemberStatus = i;
                    break;
                }
            }
        }
        memberStatusSP.setSelection(selectedMemberStatus);
    }

    private void prepareNomineeRelation() {
        relationList = new ArrayList<>();
        ArrayList<RelationItem> relationList1 = new ArrayList<>();
        RelationItem item1 = new RelationItem();
        item1.setRelationCode("0");
        item1.setRelationName("Select Relation");
        relationList.add(0, item1);
        if (houseHoldItem != null && houseHoldItem.getDataSource() != null &&
                houseHoldItem.getDataSource().equalsIgnoreCase(AppConstant.RSBY_SOURCE)) {
            if (selectedMemberItem.getRsbyGender() != null &&
                    selectedMemberItem.getRsbyGender().trim().equalsIgnoreCase(AppConstant.MALE_GENDER)) {
                relationList1 = SeccDatabase.getRelationListByGender(context, "M");
            } else if (selectedMemberItem.getRsbyGender() != null &&
                    selectedMemberItem.getRsbyGender().trim().equalsIgnoreCase(AppConstant.FEMALE_GENDER)) {
                relationList1 = SeccDatabase.getRelationListByGender(context, "F");
            } else {
                relationList1 = SeccDatabase.getRelationList(context);
            }
        } else {
            if (selectedMemberItem.getGenderid() != null && selectedMemberItem.getGenderid().trim().equalsIgnoreCase(AppConstant.MALE_GENDER)) {
                relationList1 = SeccDatabase.getRelationListByGender(context, "M");
            } else if (selectedMemberItem.getGenderid() != null && selectedMemberItem.getGenderid().trim().equalsIgnoreCase(AppConstant.FEMALE_GENDER)) {
                relationList1 = SeccDatabase.getRelationListByGender(context, "F");
            } else {
                relationList1 = SeccDatabase.getRelationList(context);
            }
        }

        Collections.sort(relationList1, new Comparator<RelationItem>() {
            @Override
            public int compare(RelationItem relationItem, RelationItem t1) {
                return Integer.parseInt(relationItem.getDisplayOrder()) - Integer.parseInt(t1.getDisplayOrder());
            }
        });

        ArrayList<String> spinnerList = new ArrayList<>();
        for (RelationItem item : relationList1) {
            if (item.getRelationCode().equalsIgnoreCase(AppConstant.NEW_HEAD_RELATION_CODE)) {

            } else {
                relationList.add(item);
            }
        }
        for (RelationItem item : relationList) {
            spinnerList.add(item.getRelationName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.custom_drop_down, R.id.textView, spinnerList);
        newRelSP.setAdapter(adapter);
        int selectedPos = 0;
        for (int i = 0; i < relationList.size(); i++) {
            if (selectedMemberItem.getNhpsRelationCode() != null && selectedMemberItem.getNhpsRelationCode().trim().equalsIgnoreCase(relationList.get(i).getRelationCode().trim())) {
                // AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Relation Code "+relationList.get(i).getRelationName());
                selectedPos = i;
                break;
            }
        }
        newRelSP.setSelection(selectedPos);

        /*if(selectedMemberItem.getNhpsRelationCode()!=null) {

        }else{
            newRelSP.setSelection(selectedPos);
        }*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SECC_MEMBER_DETAIL) {
            //  setupScreen();
        }

    }

    private void lockPrompt() {
        dialog = new AlertDialog.Builder(context).create();
        LayoutInflater factory = LayoutInflater.from(context);
        View alertView = factory.inflate(R.layout.delete_house_hold_prompt, null);
        dialog.setView(alertView);
        // dialog.setContentView(R.layout.opt_auth_layout);
        //    final TextView otpAuthMsg=(TextView)alertView.findViewById(R.id.otpAuthMsg);

        Button syncBT = (Button) alertView.findViewById(R.id.syncBT);
        syncBT.setText("Ok");
        Button cancelBT = (Button) alertView.findViewById(R.id.cancelBT);
        final TextView deletePromptTV = (TextView) alertView.findViewById(R.id.deleteMsg);
        // long unsyncData=SeccDatabase.countSurveyedHousehold(context,"","");
        // long underSurvey=SeccDatabase.countUnderSurveyedHousehold(context,"","");
        String msg = getResources().getString(R.string.locked_msg);
        //   otpAuthMsg.setText("Please enter OTP sent by the UIDAI on your Aadhaar registerd mobile number(XXXXXX0906");
        deletePromptTV.setText(msg);
        syncBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                /*Intent theIntent=new Intent(context,SyncHouseholdActivity.class);
                startActivity(theIntent);
                finish();
                leftTransition();*/

                askPinToLock();
            }
        });


        cancelBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void askPinToLock() {
        AppUtility.softKeyBoard(activity, 1);
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
        pinET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //  errorTV.setVisibility(View.GONE);
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
                    lockSubmit();
                } else if (pin.equalsIgnoreCase("")) {
                    // CustomAlert.alertWithOk(context,"Please enter valid pin");
                    errorTV.setVisibility(View.VISIBLE);
                    errorTV.setText(context.getResources().getString(R.string.plzEnterPin));
                    pinET.setText("");
                } else if (!pin.toString().equalsIgnoreCase(verifierDetail.getPin())) {
                    errorTV.setVisibility(View.VISIBLE);
                    errorTV.setText(context.getResources().getString(R.string.plzEnterValidPin));
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

    private void lockSubmit() {
       /* Intent theIntent;
        if(selectedMemberItem!=null) {
            selectedMemberItem.setLockedSave(AppConstant.LOCKED + "");
           *//* selectedMemberItem.setSyncDt(String.valueOf(DateTimeUtil.currentTimeMillis()));
            selectedMemberItem.setAppVersion(AppUtility.getCurrentApplicationVersion(context));*//*
            SeccDatabase.updateSeccMember(selectedMemberItem,context);
            HouseHoldItem houseHoldItem=selectedMemItem.getHouseHoldItem();
            if(SeccDatabase.checkUnderSurveyMember(context,houseHoldItem.getHhdNo())){
                houseHoldItem.setLockSave(AppConstant.SAVE+"");
            }else{
              //  houseHoldItem.setAppVersion(AppUtility.getCurrentApplicationVersion(context));
                houseHoldItem.setLockSave(AppConstant.LOCKED+"");
              //  houseHoldItem.setSyncDt(String.valueOf(DateTimeUtil.currentTimeMillis()));
            }
            selectedMemItem.setHouseHoldItem(houseHoldItem);
            //    Log.d("Secc Member Detail "," Household Item json : "+houseHoldItem.serialize());
            SeccDatabase.updateHouseHold(selectedMemItem.getHouseHoldItem(),context);
            selectedMemberItem=SeccDatabase.getSeccMemberDetail(selectedMemberItem.getNhpsMemId(),context);
            selectedMemItem.setSeccMemberItem(selectedMemberItem);
            ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                    AppConstant.SELECTED_ITEM_FOR_VERIFICATION, selectedMemItem.serialize(), context);
            theIntent = new Intent(context, SeccMemberListActivity.class);
            startActivity(theIntent);
            finish();
            rightTransition();
        }*/

        if (selectedMemberItem != null && selectedMemberItem.getDataSource() != null &&
                selectedMemberItem.getDataSource().trim().equalsIgnoreCase(AppConstant.RSBY_SOURCE)) {
            submitRsby();
        } else {
            submitSecc();
        }


    }

    private void showRsbyDetail(SeccMemberItem item) {
        hhdIdLayout.setVisibility(View.GONE);
        fathersNameLayout.setVisibility(View.GONE);
        TextView urnIdTV = (TextView) urnIdLayout.findViewById(R.id.urnIdTV);
        if (item.getRsbyUrnId() != null && item.getRsbyUrnId().length() == 17) {
            urnIdTV.setText(AppUtility.formatUrn(item.getRsbyUrnId()));
        }
       /* if(item.getRsbyDoorhouse()!=null) {
            houseHoldNoTV.setText(item.getRsbyDoorhouse());
        }*/
        if (item.getRsbyName() != null)
            nameTV.setText(item.getRsbyName());
        /*if(item.getNameSl()!=null)
            nameAsRegionalTV.setText(item.getNameSl());*/
        //relationTV.setText(item.getRelation());
        // fatherNameTV.setText(item.getFathername());
        // motherNameTV.setText(item.getMothername());
        if (item.getRsbyDob() != null && !item.getRsbyDob().equalsIgnoreCase("")) {
            dobTV.setText(item.getRsbyDob());
        }
       /* else {
            if(item.getDobFrmNpr()!=null) {
                dobTV.setText(item.getDobFrmNpr().trim());
            }
        }*/
        if (item.getNhpsRelationCode() != null) {
            ArrayList<RelationItem> relationItems = SeccDatabase.getRelationList(context);
            for (RelationItem item1 : relationItems) {
                if (item1.getRelationCode().equalsIgnoreCase(item.getNhpsRelationCode())) {
                    if (item1.getRelationCode().equalsIgnoreCase(AppConstant.NEW_HEAD_RELATION_CODE)) {
                        newRelationTV.setText("Head");
                    } else {
                        newRelationTV.setText(item1.getRelationName());
                    }
                    break;
                }
            }
        }

        if (item.getRsbyGender().equalsIgnoreCase(AppConstant.MALE_GENDER)) {
            genderTV.setText(AppConstant.MALE);
            ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SELECTED_MEMBER_GENDER_ID,  item.getGenderid(), context);

        } else if (item.getRsbyGender().equalsIgnoreCase(AppConstant.FEMALE_GENDER)) {
            genderTV.setText(AppConstant.FEMALE);
            ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SELECTED_MEMBER_GENDER_ID,  item.getGenderid(), context);

        } else {
            genderTV.setText(AppConstant.OTHER_GENDER_NAME);
            ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SELECTED_MEMBER_GENDER_ID,  item.getGenderid(), context);

        }

        if (item.getNhpsRelationCode() != null && !item.getNhpsRelationCode().trim().equalsIgnoreCase("")) {
            if (item.getNhpsRelationCode().trim().equalsIgnoreCase(AppConstant.NEW_HEAD_RELATION_CODE)) {
                hofNameTV.setText(item.getRsbyName());
            } else {
                SeccMemberItem item1 = AppUtility.findHof(selectedMemberItem, context);
                if (item1 != null) {
                    hofNameTV.setText(item1.getRsbyName());
                }
            }
        } else {
            SeccMemberItem item1 = AppUtility.findHof(selectedMemberItem, context);
            if (item1 != null) {
                hofNameTV.setText(item1.getRsbyName());
            }
        }
    }

    private void showSeccDetail(SeccMemberItem item) {
        urnIdLayout.setVisibility(View.GONE);
        houseHoldNoTV.setText(item.getHhdNo());
        nameTV.setText(item.getName());
        if (item.getNameSl() != null)
            nameAsRegionalTV.setText(Html.fromHtml(item.getNameSl()));
        relationTV.setText(item.getRelation());
        fatherNameTV.setText(item.getFathername());
        motherNameTV.setText(item.getMothername());
        if (item.getDob() != null && !item.getDob().equalsIgnoreCase("")) {
            dobTV.setText(item.getDob().trim());
        } else {
            if (item.getDobFrmNpr() != null) {
                dobTV.setText(item.getDobFrmNpr().trim());
            }
        }
        if (item.getNhpsRelationCode() != null) {
            ArrayList<RelationItem> relationItems = SeccDatabase.getRelationList(context);
            for (RelationItem item1 : relationItems) {
                if (item1.getRelationCode().equalsIgnoreCase(item.getNhpsRelationCode())) {
                    if (item1.getRelationCode().equalsIgnoreCase(AppConstant.NEW_HEAD_RELATION_CODE)) {
                        newRelationTV.setText("Head");
                    } else {
                        newRelationTV.setText(item1.getRelationName());
                    }
                    break;
                }
            }
        }

        if (item.getGenderid().equalsIgnoreCase(AppConstant.MALE_GENDER)) {
            genderTV.setText("Male");
            ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SELECTED_MEMBER_GENDER_ID,  item.getGenderid(), context);

        } else if (item.getGenderid().equalsIgnoreCase(AppConstant.FEMALE_GENDER)) {
            genderTV.setText("Female");
            ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SELECTED_MEMBER_GENDER_ID,  item.getGenderid(), context);

        } else {
            genderTV.setText("Other");
            ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SELECTED_MEMBER_GENDER_ID,  item.getGenderid(), context);

        }

        if (item.getNhpsRelationCode() != null && !item.getNhpsRelationCode().trim().equalsIgnoreCase("")) {
            if (item.getNhpsRelationCode().trim().equalsIgnoreCase(AppConstant.NEW_HEAD_RELATION_CODE)) {
                hofNameTV.setText(item.getName());
            } else {
                SeccMemberItem item1 = AppUtility.findHof(selectedMemberItem, context);
                if (item1 != null) {
                    hofNameTV.setText(item1.getName());
                }
            }
        } else {
            SeccMemberItem item1 = AppUtility.findHof(selectedMemberItem, context);
            if (item1 != null) {
                hofNameTV.setText(item1.getName());
            }
        }
    }

    private void submitRsby() {
        Intent theIntent;
        if (selectedMemberItem != null) {
            selectedMemberItem.setLockedSave(AppConstant.LOCKED + "");
           /* selectedMemberItem.setSyncDt(String.valueOf(DateTimeUtil.currentTimeMillis()));
            selectedMemberItem.setAppVersion(AppUtility.getCurrentApplicationVersion(context));*/
            if (!selectedMemberItem.getMemStatus().equalsIgnoreCase(AppConstant.MEMBERFOUND_AND_PRESENT) ||
                    !selectedMemberItem.getMemStatus().equalsIgnoreCase(AppConstant.MEMBERFOUND_BUT_NOT_PRESENT)) {

                selectedMemberItem = AppUtility.clearMemberBasicDetail(selectedMemberItem);

            }
            SeccDatabase.updateRsbyMember(selectedMemberItem, context);
            HouseHoldItem houseHoldItem = selectedMemItem.getHouseHoldItem();
            if (SeccDatabase.checkUnderSurveyMember(context, houseHoldItem)) {
                houseHoldItem.setLockSave(AppConstant.SAVE + "");
            } else {
                //  houseHoldItem.setAppVersion(AppUtility.getCurrentApplicationVersion(context));
                houseHoldItem.setLockSave(AppConstant.LOCKED + "");
                //  houseHoldItem.setSyncDt(String.valueOf(DateTimeUtil.currentTimeMillis()));
            }
            selectedMemItem.setHouseHoldItem(houseHoldItem);
            //    Log.d("Secc Member Detail "," Household Item json : "+houseHoldItem.serialize());
            SeccDatabase.updateRsbyHousehold(selectedMemItem.getHouseHoldItem(), context);
            selectedMemberItem = SeccDatabase.getSeccMemberDetail(selectedMemberItem, context);
            selectedMemItem.setSeccMemberItem(selectedMemberItem);
            ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                    AppConstant.SELECTED_ITEM_FOR_VERIFICATION, selectedMemItem.serialize(), context);
            theIntent = new Intent(context, SeccMemberListActivity.class);
            startActivity(theIntent);
            finish();
            rightTransition();
        }
    }

    private void submitSecc() {
        Intent theIntent;
        if (selectedMemberItem != null) {
            selectedMemberItem.setLockedSave(AppConstant.LOCKED + "");
           /* selectedMemberItem.setSyncDt(String.valueOf(DateTimeUtil.currentTimeMillis()));
            selectedMemberItem.setAppVersion(AppUtility.getCurrentApplicationVersion(context));*/
            if (!selectedMemberItem.getMemStatus().equalsIgnoreCase(AppConstant.MEMBERFOUND_AND_PRESENT) ||
                    !selectedMemberItem.getMemStatus().equalsIgnoreCase(AppConstant.MEMBERFOUND_BUT_NOT_PRESENT)) {

                selectedMemberItem = AppUtility.clearMemberBasicDetail(selectedMemberItem);

            }

            SeccDatabase.updateSeccMember(selectedMemberItem, context);
            HouseHoldItem houseHoldItem = selectedMemItem.getHouseHoldItem();
            if (SeccDatabase.checkUnderSurveyMember(context, houseHoldItem)) {
                houseHoldItem.setLockSave(AppConstant.SAVE + "");
            } else {
                //  houseHoldItem.setAppVersion(AppUtility.getCurrentApplicationVersion(context));
                houseHoldItem.setLockSave(AppConstant.LOCKED + "");
                //  houseHoldItem.setSyncDt(String.valueOf(DateTimeUtil.currentTimeMillis()));
            }
            selectedMemItem.setHouseHoldItem(houseHoldItem);
            //    Log.d("Secc Member Detail "," Household Item json : "+houseHoldItem.serialize());
            SeccDatabase.updateHouseHold(selectedMemItem.getHouseHoldItem(), context);
            selectedMemberItem = SeccDatabase.getSeccMemberDetail(selectedMemberItem, context);
            selectedMemItem.setSeccMemberItem(selectedMemberItem);
            ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                    AppConstant.SELECTED_ITEM_FOR_VERIFICATION, selectedMemItem.serialize(), context);
            theIntent = new Intent(context, SeccMemberListActivity.class);
            startActivity(theIntent);
            finish();
            rightTransition();
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

    private void dashboardDropdown(View v) {

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
                askPinToUnLock();
            }
        }
    }

    private void askPinToUnLock() {

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
                    //true condtion  user gain access to page again
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
                askPinToUnLock();
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
