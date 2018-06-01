package com.nhpm.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.Spinner;
import android.widget.TextView;

import com.customComponent.CustomAlert;
import com.customComponent.utility.ProjectPrefrence;
import com.nhpm.LocalDataBase.dto.SeccDatabase;
import com.nhpm.Models.response.NomineeMemberItem;
import com.nhpm.Models.response.RelationItem;
import com.nhpm.Models.response.master.ConfigurationItem;
import com.nhpm.Models.response.master.StateItem;
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

public class NomineeCaptureActivity extends BaseActivity implements ComponentCallbacks2 {
    private Spinner nomineeRelSP, otherRelSP, memberSP;
    private EditText relationNameET, otherNomineeNameET, nomineeGardianNameET;
    private ArrayList<NomineeMemberItem> nomineeMemberList;
    private TextView headerTV;
    private Button submitBt;
    private ImageView backIV;
    private boolean gaurdianAvailable = false;
    private SelectedMemberItem selectedMemItem;
    private SeccMemberItem seccItem;
    private ArrayList<RelationItem> relationList, otherRelationList;
    private final String TAG = "Nominee details";
    private Context context;
    private LinearLayout nomineeRelLayout, otherNomineeLayout, nomineeGardianNameLayout;
    private RelationItem memberRelationItem, otherRelationItem;
    private boolean isMemberRel, isOtherRel, isMemberOutOftheFamily;
    private NomineeMemberItem nomineeItem;
    private LinearLayout mZoomLinearLayout;
    private ZoomView zoomView;
    private Activity activity;
    private boolean pinLockIsShown;
    private String zoomMode = "N";

    private int wrongPinCount = 0;
    private long wrongPinSavedTime;
    private long currentTime;
    private TextView wrongAttempetCountValue, wrongAttempetCountText;
    private long millisecond24 = 86400000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        context = this;
        checkAppConfig();

        if (zoomMode.equalsIgnoreCase("N")) {
            setContentView(R.layout.activity_nominee_capture);
            setupScreenWithoutZoom();

        } else {
            setContentView(R.layout.dummy_layout_for_zooming);
            setupScreenWithZoom();

        }
    }


    private void setupScreenWithZoom() {

        AppUtility.hideSoftInput(activity);
        mZoomLinearLayout = (LinearLayout) findViewById(R.id.mZoomLinearLayout);
        View v = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.activity_nominee_capture, null, false);
        v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
        showNotification(v);
        nomineeGardianNameLayout = (LinearLayout) v.findViewById(R.id.nomineeGardianNameLayout);
        nomineeGardianNameET = (EditText) v.findViewById(R.id.nomineeGardianNameET);
        headerTV = (TextView) v.findViewById(R.id.centertext);
        selectedMemItem = SelectedMemberItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.SELECTED_ITEM_FOR_VERIFICATION, context));
        if (selectedMemItem.getSeccMemberItem() != null) {
            seccItem = selectedMemItem.getSeccMemberItem();
            if (seccItem != null && seccItem.getDataSource() != null && seccItem.getDataSource().trim().equalsIgnoreCase(AppConstant.RSBY_SOURCE)) {
                showRsbyDetail(seccItem);


            } else {
                showSeccDetail(seccItem);
            }

        }
        nomineeRelLayout = (LinearLayout) v.findViewById(R.id.nomineeRelLayout);
        otherNomineeLayout = (LinearLayout) v.findViewById(R.id.otherNomineeLayout);
        submitBt = (Button) v.findViewById(R.id.submitBT);
        relationNameET = (EditText) v.findViewById(R.id.nomineeRelationshipET);

        otherNomineeNameET = (EditText) v.findViewById(R.id.otherNomineeNameET);
        backIV = (ImageView) v.findViewById(R.id.back);
        memberSP = (Spinner) v.findViewById(R.id.nomineeMemberSP);
        dashboardDropdown(v);
        zoomView = new ZoomView(this);
        zoomView.addView(v);
        mZoomLinearLayout.addView(zoomView);
        memberSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                nomineeItem = nomineeMemberList.get(position);
                isMemberRel = false;
                isOtherRel = false;
                isMemberOutOftheFamily = false;
                switch (nomineeItem.statusCode) {
                    case 0:
                        nomineeRelLayout.setVisibility(View.GONE);
                        otherNomineeLayout.setVisibility(View.GONE);
                        submitBt.setVisibility(View.GONE);
                        nomineeGardianNameLayout.setVisibility(View.GONE);

                        // nomineeRelSP.setSelection(0);
                        break;
                    case 1:
                        prepareNomineeRelation();
                        nomineeRelLayout.setVisibility(View.VISIBLE);
                        otherNomineeLayout.setVisibility(View.GONE);
                        String str = nomineeItem.getMemberName();
                        nomineeGardianNameLayout.setVisibility(View.GONE);

                        System.out.print(str);
                        break;
                    case 2:
                        isMemberOutOftheFamily = true;
                        submitBt.setVisibility(View.GONE);
                        nomineeRelLayout.setVisibility(View.GONE);
                        nomineeGardianNameLayout.setVisibility(View.GONE);

                        // otherRelSP.setSelection(0);
                        otherNomineeLayout.setVisibility(View.VISIBLE);
                        AppUtility.requestFocus(otherNomineeNameET);
                        AppUtility.showSoftInput(activity);
                        boolean flag = false;
                        if (seccItem.getNameNominee() != null && !seccItem.getNameNominee().equalsIgnoreCase("")) {
                            for (NomineeMemberItem item : nomineeMemberList) {
                                if (item.getMemberName().equalsIgnoreCase(seccItem.getNameNominee())) {
                                    flag = true;
                                    break;
                                }
                            }
                        }
                        if (flag) {

                        } else {
                            otherNomineeNameET.setText(seccItem.getNameNominee());

                        }
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        nomineeRelSP = (Spinner) findViewById(R.id.nomineeRelSP);
        nomineeRelSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                submitBt.setVisibility(View.GONE);
                memberRelationItem = relationList.get(position);

                if (memberRelationItem.getRelationCode().equalsIgnoreCase(AppConstant.DEFAULT_RELATION)) {
                    nomineeGardianNameLayout.setVisibility(View.GONE);
                } else {
                    isMemberRel = true;
                    isOtherRel = false;
                    isMemberOutOftheFamily = false;
                    if (nomineeItem.getAge() < 18) {
                        gaurdianAvailable = true;
                        nomineeGardianNameLayout.setVisibility(View.VISIBLE);
                        if (seccItem.getNomineeGaurdianName() != null) {
                            if (!seccItem.getNomineeGaurdianName().equalsIgnoreCase("")) {
                                nomineeGardianNameET.setText(seccItem.getNomineeGaurdianName());
                            }
                        }
                    } else {
                        gaurdianAvailable = false;
                        nomineeGardianNameLayout.setVisibility(View.GONE);
                    }
                    AppUtility.showLog(AppConstant.LOG_STATUS, TAG, " here 1 : " + memberRelationItem.getRelationName());
                    submitBt.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        otherRelSP = (Spinner) findViewById(R.id.otherNomineeRelSP);
        otherRelSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                relationNameET.setVisibility(View.GONE);
                // submitBt.setVisibility(View.GONE);
                otherRelationItem = otherRelationList.get(position);
                isMemberRel = false;
                isOtherRel = false;
                AppUtility.showLog(AppConstant.LOG_STATUS, TAG, " here 2 : " + otherRelationItem.getRelationName());

                if (otherRelationItem.getRelationCode().equalsIgnoreCase(AppConstant.DEFAULT_RELATION)) {
                    relationNameET.setVisibility(View.GONE);
                    //  submitBt.setVisibility(View.GONE);
                } else {
                    //RelationItem item=otherRelationList.get(position);
                    submitBt.setVisibility(View.VISIBLE);

                    AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Selected relation code : " +
                            "" + otherRelationItem.getRelationCode() + "Relation Name : " + otherRelationItem.getRelationName());
                    if (otherRelationItem.getRelationCode().equalsIgnoreCase(AppConstant.OTHER_RELATION)) {
                        isOtherRel = true;
                        relationNameET.setVisibility(View.VISIBLE);
                        AppUtility.clearFocus(otherNomineeNameET);
                        AppUtility.requestFocus(relationNameET);
                        //     AppUtility.showSoftInput(activity);
                        if (seccItem.getRelationNomineeCode() != null && seccItem.getRelationNomineeCode().
                                equalsIgnoreCase(AppConstant.OTHER_RELATION)) {
                            relationNameET.setText(seccItem.getNomineeRelationName());
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        prepareNomineeSpinner();
        //  prepareNomineeRelation();
        prepareNomineeOtherRelation();
        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent theIntent = null;
                theIntent = new Intent(context, WithAadhaarActivity.class);
                startActivity(theIntent);
                rightTransition();
                finish();
            }
        });
        submitBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit();
            }
        });
    }

    private void setupScreenWithoutZoom() {

        AppUtility.hideSoftInput(activity);
        showNotification();
        nomineeGardianNameLayout = (LinearLayout) findViewById(R.id.nomineeGardianNameLayout);
        nomineeGardianNameET = (EditText) findViewById(R.id.nomineeGardianNameET);
        headerTV = (TextView) findViewById(R.id.centertext);
        selectedMemItem = SelectedMemberItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.SELECTED_ITEM_FOR_VERIFICATION, context));
        if (selectedMemItem.getSeccMemberItem() != null) {
            seccItem = selectedMemItem.getSeccMemberItem();
            if (seccItem != null && seccItem.getDataSource() != null && seccItem.getDataSource().trim().equalsIgnoreCase(AppConstant.RSBY_SOURCE)) {
                showRsbyDetail(seccItem);


            } else {
                showSeccDetail(seccItem);
            }

        }
        nomineeRelLayout = (LinearLayout) findViewById(R.id.nomineeRelLayout);
        otherNomineeLayout = (LinearLayout) findViewById(R.id.otherNomineeLayout);
        submitBt = (Button) findViewById(R.id.submitBT);
        relationNameET = (EditText) findViewById(R.id.nomineeRelationshipET);

        otherNomineeNameET = (EditText) findViewById(R.id.otherNomineeNameET);
        backIV = (ImageView) findViewById(R.id.back);
        memberSP = (Spinner) findViewById(R.id.nomineeMemberSP);
        dashboardDropdown();
        memberSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                nomineeItem = nomineeMemberList.get(position);
                isMemberRel = false;
                isOtherRel = false;
                isMemberOutOftheFamily = false;
                switch (nomineeItem.statusCode) {
                    case 0:
                        nomineeRelLayout.setVisibility(View.GONE);
                        otherNomineeLayout.setVisibility(View.GONE);
                        submitBt.setVisibility(View.GONE);
                        nomineeGardianNameLayout.setVisibility(View.GONE);

                        // nomineeRelSP.setSelection(0);
                        break;
                    case 1:
                        prepareNomineeRelation();
                        nomineeRelLayout.setVisibility(View.VISIBLE);
                        otherNomineeLayout.setVisibility(View.GONE);
                        String str = nomineeItem.getMemberName();
                        nomineeGardianNameLayout.setVisibility(View.GONE);

                        System.out.print(str);
                        break;
                    case 2:
                        isMemberOutOftheFamily = true;
                        submitBt.setVisibility(View.GONE);
                        nomineeRelLayout.setVisibility(View.GONE);
                        nomineeGardianNameLayout.setVisibility(View.GONE);

                        // otherRelSP.setSelection(0);
                        otherNomineeLayout.setVisibility(View.VISIBLE);
                        AppUtility.requestFocus(otherNomineeNameET);
                        AppUtility.showSoftInput(activity);
                        boolean flag = false;
                        if (seccItem.getNameNominee() != null && !seccItem.getNameNominee().equalsIgnoreCase("")) {
                            for (NomineeMemberItem item : nomineeMemberList) {
                                if (item.getMemberName().equalsIgnoreCase(seccItem.getNameNominee())) {
                                    flag = true;
                                    break;
                                }
                            }
                        }
                        if (flag) {

                        } else {
                            otherNomineeNameET.setText(seccItem.getNameNominee());

                        }
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        nomineeRelSP = (Spinner) findViewById(R.id.nomineeRelSP);
        nomineeRelSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                submitBt.setVisibility(View.GONE);
                memberRelationItem = relationList.get(position);

                if (memberRelationItem.getRelationCode().equalsIgnoreCase(AppConstant.DEFAULT_RELATION)) {
                    nomineeGardianNameLayout.setVisibility(View.GONE);
                } else {
                    isMemberRel = true;
                    isOtherRel = false;
                    isMemberOutOftheFamily = false;
                    if (nomineeItem.getAge() < 18) {
                        gaurdianAvailable = true;
                        nomineeGardianNameLayout.setVisibility(View.VISIBLE);
                        if (seccItem.getNomineeGaurdianName() != null) {
                            if (!seccItem.getNomineeGaurdianName().equalsIgnoreCase("")) {
                                nomineeGardianNameET.setText(seccItem.getNomineeGaurdianName());
                            }
                        }
                    } else {
                        gaurdianAvailable = false;
                        nomineeGardianNameLayout.setVisibility(View.GONE);
                    }
                    AppUtility.showLog(AppConstant.LOG_STATUS, TAG, " here 1 : " + memberRelationItem.getRelationName());
                    submitBt.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        otherRelSP = (Spinner) findViewById(R.id.otherNomineeRelSP);
        otherRelSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                relationNameET.setVisibility(View.GONE);
                // submitBt.setVisibility(View.GONE);
                otherRelationItem = otherRelationList.get(position);
                isMemberRel = false;
                isOtherRel = false;
                AppUtility.showLog(AppConstant.LOG_STATUS, TAG, " here 2 : " + otherRelationItem.getRelationName());

                if (otherRelationItem.getRelationCode().equalsIgnoreCase(AppConstant.DEFAULT_RELATION)) {
                    relationNameET.setVisibility(View.GONE);
                    //  submitBt.setVisibility(View.GONE);
                } else {
                    //RelationItem item=otherRelationList.get(position);
                    submitBt.setVisibility(View.VISIBLE);

                    AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Selected relation code : " +
                            "" + otherRelationItem.getRelationCode() + "Relation Name : " + otherRelationItem.getRelationName());
                    if (otherRelationItem.getRelationCode().equalsIgnoreCase(AppConstant.OTHER_RELATION)) {
                        isOtherRel = true;
                        relationNameET.setVisibility(View.VISIBLE);
                        AppUtility.clearFocus(otherNomineeNameET);
                        AppUtility.requestFocus(relationNameET);
                        //     AppUtility.showSoftInput(activity);
                        if (seccItem.getRelationNomineeCode() != null && seccItem.getRelationNomineeCode().
                                equalsIgnoreCase(AppConstant.OTHER_RELATION)) {
                            relationNameET.setText(seccItem.getNomineeRelationName());
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        prepareNomineeSpinner();
        //  prepareNomineeRelation();
        prepareNomineeOtherRelation();
        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent theIntent = null;
                theIntent = new Intent(context, WithAadhaarActivity.class);
                startActivity(theIntent);
                rightTransition();
                finish();
            }
        });
        submitBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit();
            }
        });
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


    private void submit() {
        String relationName, memberName;
        if (isMemberRel) {
           /* if(nomineeItem.getAge()<18){
                CustomAlert.alertWithOk(context, getResources().getString(R.string.minor_nominee_msg));
                return;
            }*/
            if (gaurdianAvailable && nomineeItem.getAge() < 18) {
                if (nomineeGardianNameET.getText().toString().trim().equalsIgnoreCase("")) {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterGaurdianName));
                    return;
                }
            }
            if (!nomineeGardianNameET.getText().toString().equalsIgnoreCase("")) {
                seccItem.setNomineeGaurdianName(nomineeGardianNameET.getText().toString());
            }
            String str = nomineeItem.getMemberName();
            System.out.print(str);
            seccItem.setNameNominee(nomineeItem.getMemberName());
            seccItem.setRelationNomineeCode(memberRelationItem.getRelationCode());
            seccItem.setNomineeRelationName(memberRelationItem.getRelationName());
        }
        if (isMemberOutOftheFamily) {
            memberName = otherNomineeNameET.getText().toString();
            if (memberName.equalsIgnoreCase("")) {
                CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterNomineeName));
                return;
            }
            seccItem.setNameNominee(memberName);

            if (isOtherRel) {
                seccItem.setRelationNomineeCode(otherRelationItem.getRelationCode());
                relationName = relationNameET.getText().toString();
                if (relationName.equalsIgnoreCase("")) {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterRelationName));
                    return;
                }
                // edited by saurfabh

                seccItem.setNomineeRelationName(relationName);
            } else {
                seccItem.setNomineeRelationName(otherRelationItem.getRelationName());
                seccItem.setRelationNomineeCode(otherRelationItem.getRelationCode());
            }
        }

       /* if(selectedMemItem.getOldHeadMember()!=null && selectedMemItem.getNewHeadMember()!=null){
            SeccMemberItem oldHead=selectedMemItem.getOldHeadMember();
            oldHead.setLockedSave(AppConstant.LOCKED+"");
            AppUtility.showLog(AppConstant.LOG_STATUS,TAG," OLD Household name " +
                    ": "+oldHead.getName()+"" +
                    " Member Status "+oldHead.getMemStatus()+" House hold Status :" +
                    " "+oldHead.getHhStatus()+" Locked Save :"+oldHead.getLockedSave());
            SeccDatabase.updateSeccMember(selectedMemItem.getOldHeadMember(),context);
        }
        seccItem.setLockedSave(AppConstant.SAVE + "");
        SeccDatabase.updateSeccMember(seccItem,context);
        SeccDatabase.updateHouseHold(selectedMemItem.getHouseHoldItem(),context);
        // SeccDatabase.getSeccMemberDetail(seccItem.getAhlTin(),context);
        seccItem=SeccDatabase.getSeccMemberDetail(seccItem.getNhpsMemId(),context);
        selectedMemItem.setSeccMemberItem(seccItem);
        ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.SELECTED_ITEM_FOR_VERIFICATION,selectedMemItem.serialize(),context);
        Intent theIntent = null;
        //if (seccItem != null && seccItem.getAadhaarStatus().equalsIgnoreCase("1")) {
        theIntent = new Intent(context, WithAadhaarActivity.class);
        startActivity(theIntent);
        rightTransition();
        finish();*/


        if (seccItem != null && seccItem.getDataSource() != null && seccItem.getDataSource().trim().equalsIgnoreCase(AppConstant.RSBY_SOURCE)) {
            if (selectedMemItem.getOldHeadMember() != null && selectedMemItem.getNewHeadMember() != null) {
                SeccMemberItem oldHead = selectedMemItem.getOldHeadMember();
                oldHead.setLockedSave(AppConstant.LOCKED + "");
                SeccDatabase.updateRsbyMember(selectedMemItem.getOldHeadMember(), context);
            }
            SeccDatabase.updateRsbyMember(seccItem, context);
            SeccDatabase.updateRsbyHousehold(selectedMemItem.getHouseHoldItem(), context);
            seccItem = SeccDatabase.getSeccMemberDetail(seccItem, context);
            String str = seccItem.serialize();
            System.out.print(str);
            selectedMemItem.setSeccMemberItem(seccItem);
            ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                    AppConstant.SELECTED_ITEM_FOR_VERIFICATION, selectedMemItem.serialize(), context);

        } else {
            if (selectedMemItem.getOldHeadMember() != null && selectedMemItem.getNewHeadMember() != null) {
                SeccMemberItem oldHead = selectedMemItem.getOldHeadMember();
                oldHead.setLockedSave(AppConstant.LOCKED + "");
                SeccDatabase.updateSeccMember(selectedMemItem.getOldHeadMember(), context);
            }
            SeccDatabase.updateSeccMember(seccItem, context);
            SeccDatabase.updateHouseHold(selectedMemItem.getHouseHoldItem(), context);
            seccItem = SeccDatabase.getSeccMemberDetail(seccItem, context);
            selectedMemItem.setSeccMemberItem(seccItem);
            ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                    AppConstant.SELECTED_ITEM_FOR_VERIFICATION, selectedMemItem.serialize(), context);
            //  if (seccItem.getAadhaarStatus().equalsIgnoreCase("1")) {
        }

        Intent theIntent = new Intent(context, WithAadhaarActivity.class);
        startActivity(theIntent);
        rightTransition();
        finish();
    }

    private void prepareNomineeSpinner() {
        boolean flag = false;
        nomineeMemberList = new ArrayList<>();
        if (seccItem != null && seccItem.getDataSource() != null
                && seccItem.getDataSource().trim().equalsIgnoreCase(AppConstant.RSBY_SOURCE)) {
            ArrayList<SeccMemberItem> nomineeList = SeccDatabase.getRsbyMemberListWithUrn(seccItem.getRsbyUrnId(), context);
            ArrayList<String> spinnerList = new ArrayList<>();
            nomineeMemberList.add(new NomineeMemberItem(0, "Select member for nominee", "Select member for nominee"));
            for (SeccMemberItem item : nomineeList) {
                if (item.getNhpsRelationCode() != null && item.getNhpsRelationCode().equalsIgnoreCase(AppConstant.NEW_HEAD_RELATION_CODE)) {
                    flag = true;
                    break;
                }
            }
            for (SeccMemberItem item : nomineeList) {
                String str = item.getMemStatus();
                System.out.print(str);
                if (item.getRsbyMemId().equalsIgnoreCase(seccItem.getRsbyMemId())) {

                } else {
                    if (item.getMemStatus() != null &&
                            (item.getMemStatus().trim().equalsIgnoreCase(AppConstant.MEMBERFOUND_BUT_NOT_PRESENT) ||
                                    item.getMemStatus().trim().equalsIgnoreCase(AppConstant.MEMBERFOUND_AND_PRESENT))) {
                        if (!item.getRsbyName().equalsIgnoreCase("")) {
                            String yearOfBirth = null;
                            String gender;

                            if (item.getRsbyDob() != null && !item.getRsbyDob().equalsIgnoreCase("") && !item.getRsbyDob().equalsIgnoreCase("-")) {
                                yearOfBirth = item.getRsbyDob().substring(0, 4);
                            }
                            NomineeMemberItem ite1 = new NomineeMemberItem();
                            ite1.setMemberItem(item);
                            ite1.setStatusCode(1);
                            if (item.getRsbyGender() != null && item.getRsbyGender().equalsIgnoreCase(AppConstant.MALE_GENDER)) {
                                gender = "M";
                            } else if (item.getGenderid() != null && item.getGenderid().equalsIgnoreCase(AppConstant.FEMALE_GENDER)) {
                                gender = "F";
                            } else {
                                gender = "O";
                            }
                            ite1.setGender(gender);
                            if (yearOfBirth != null) {
                                int age = AppConstant.COMPARED_YEAR - Integer.parseInt(yearOfBirth);
                                ite1.setAge(age);
                            }
                            String modifiedNomineeDropdownCaption = item.getRsbyName() + " - (" + ite1.getGender() + ") " + ite1.getAge() + " yrs";
                            ite1.setMemberName(item.getRsbyName());
                            ite1.setNomineeLabel(modifiedNomineeDropdownCaption);
                            AppUtility.showLog(AppConstant.LOG_STATUS, TAG, " Nominee Label :" + ite1.getNomineeLabel());
                            nomineeMemberList.add(ite1);
                        }
                    }

                    if (item.getMemStatus() == null || item.getMemStatus().equalsIgnoreCase("")) {
                        if (!item.getRsbyName().equalsIgnoreCase("")) {
                            String yearOfBirth = null;
                            String gender;

                            if (item.getRsbyDob() != null && !item.getRsbyDob().equalsIgnoreCase("") && !item.getRsbyDob().equalsIgnoreCase("-")) {
                                yearOfBirth = item.getRsbyDob().substring(0, 4);
                            }
                            NomineeMemberItem ite1 = new NomineeMemberItem();
                            ite1.setMemberItem(item);
                            ite1.setStatusCode(1);
                            if (item.getRsbyGender() != null && item.getRsbyGender().equalsIgnoreCase(AppConstant.MALE_GENDER)) {
                                gender = "M";
                            } else if (item.getRsbyGender() != null && item.getRsbyGender().equalsIgnoreCase(AppConstant.FEMALE_GENDER)) {
                                gender = "F";
                            } else {
                                gender = "O";
                            }
                            ite1.setGender(gender);
                            if (yearOfBirth != null) {
                                int age = AppConstant.COMPARED_YEAR - Integer.parseInt(yearOfBirth);
                                ite1.setAge(age);
                            }
                            String modifiedNomineeDropdownCaption = item.getRsbyName() + " - (" + ite1.getGender() + ") " + ite1.getAge() + " yrs";
                            ite1.setMemberName(item.getRsbyName());
                            ite1.setNomineeLabel(modifiedNomineeDropdownCaption);
                            AppUtility.showLog(AppConstant.LOG_STATUS, TAG, " Nominee Label :" + ite1.getNomineeLabel());
                            nomineeMemberList.add(ite1);
                        }
                    }
                }
            }

            nomineeMemberList.add(new NomineeMemberItem(2, "Other", "Other"));
            for (NomineeMemberItem item : nomineeMemberList) {
                AppUtility.showLog(AppConstant.LOG_STATUS, TAG, " Nominee Label :" + item.getNomineeLabel());
                spinnerList.add(item.getNomineeLabel());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.custom_drop_down, R.id.textView, spinnerList);
            memberSP.setAdapter(adapter);
            int selectNominee = 0;
            for (int i = 0; i < nomineeMemberList.size(); i++) {
                if (seccItem.getNameNominee() != null && seccItem.getNameNominee().equalsIgnoreCase(nomineeMemberList.get(i).memberName)) {
                    selectNominee = i;
                    break;
                }
            }
            if (selectNominee == 0) {
                if (seccItem.getNameNominee() != null && !seccItem.getNameNominee().equalsIgnoreCase("")) {
                    selectNominee = nomineeMemberList.size() - 1;
                }
            }
            memberSP.setSelection(selectNominee);
        } else {
            ArrayList<SeccMemberItem> nomineeList = SeccDatabase.getSeccMemberList(seccItem.getHhdNo(), context);
            ArrayList<String> spinnerList = new ArrayList<>();
            nomineeMemberList.add(new NomineeMemberItem(0, "Select member for nominee", "Select member for nominee"));
            for (SeccMemberItem item : nomineeList) {
                if (item.getNhpsRelationCode() != null && item.getNhpsRelationCode().equalsIgnoreCase(AppConstant.NEW_HEAD_RELATION_CODE)) {
                    flag = true;
                    break;
                }
            }
            for (SeccMemberItem item : nomineeList) {
                if (item.getNhpsMemId().equalsIgnoreCase(seccItem.getNhpsMemId())) {

                } else if (item.getMemStatus() != null && (item.getMemStatus().trim().equalsIgnoreCase("") || item.getMemStatus().trim().equalsIgnoreCase(AppConstant.MEMBERFOUND_BUT_NOT_PRESENT) || item.getMemStatus().trim().equalsIgnoreCase(AppConstant.MEMBERFOUND_AND_PRESENT))) {
                    if (!item.getName().equalsIgnoreCase("")) {
                        String yearOfBirth = null;
                        String gender;

                        if (item.getDob() != null && !item.getDob().equalsIgnoreCase("") && !item.getDob().equalsIgnoreCase("-")) {
                            yearOfBirth = item.getDob().substring(0, 4);
                        } else if (item.getDobFrmNpr() != null && !item.getDobFrmNpr().equalsIgnoreCase("") && !item.getDobFrmNpr().equalsIgnoreCase("-")) {
                            yearOfBirth = item.getDob().substring(0, 4);
                        }
                        NomineeMemberItem ite1 = new NomineeMemberItem();
                        ite1.setMemberItem(item);
                        ite1.setStatusCode(1);
                        if (item.getGenderid() != null && item.getGenderid().equalsIgnoreCase(AppConstant.MALE_GENDER)) {
                            gender = "M";
                        } else if (item.getGenderid() != null && item.getGenderid().equalsIgnoreCase(AppConstant.FEMALE_GENDER)) {
                            gender = "F";
                        } else {
                            gender = "O";
                        }
                        ite1.setGender(gender);
                        if (yearOfBirth != null) {
                            int age = AppConstant.COMPARED_YEAR - Integer.parseInt(yearOfBirth);
                            ite1.setAge(age);
                        }
                        String modifiedNomineeDropdownCaption = item.getName() + " - (" + ite1.getGender() + ") " + ite1.getAge() + " yrs";
                        ite1.setMemberName(item.getName());
                        ite1.setNomineeLabel(modifiedNomineeDropdownCaption);
                        AppUtility.showLog(AppConstant.LOG_STATUS, TAG, " Nominee Label :" + ite1.getNomineeLabel());
                        nomineeMemberList.add(ite1);
                    }
                } else if (item.getMemStatus() == null) {
                    if (!item.getName().equalsIgnoreCase("")) {
                        String yearOfBirth = null;
                        String gender;

                        if (item.getDob() != null && !item.getDob().equalsIgnoreCase("") && !item.getDob().equalsIgnoreCase("-")) {
                            yearOfBirth = item.getDob().substring(0, 4);
                        } else if (item.getDobFrmNpr() != null && !item.getDobFrmNpr().equalsIgnoreCase("") && !item.getDobFrmNpr().equalsIgnoreCase("-")) {
                            yearOfBirth = item.getDob().substring(0, 4);
                        }
                        NomineeMemberItem ite1 = new NomineeMemberItem();
                        ite1.setMemberItem(item);
                        ite1.setStatusCode(1);
                        if (item.getGenderid() != null && item.getGenderid().equalsIgnoreCase(AppConstant.MALE_GENDER)) {
                            gender = "M";
                        } else if (item.getGenderid() != null && item.getGenderid().equalsIgnoreCase(AppConstant.FEMALE_GENDER)) {
                            gender = "F";
                        } else {
                            gender = "O";
                        }
                        ite1.setGender(gender);
                        if (yearOfBirth != null) {
                            int age = AppConstant.COMPARED_YEAR - Integer.parseInt(yearOfBirth);
                            ite1.setAge(age);
                        }
                        String modifiedNomineeDropdownCaption = item.getName() + " - (" + ite1.getGender() + ") " + ite1.getAge() + " yrs";
                        ite1.setMemberName(item.getName());
                        ite1.setNomineeLabel(modifiedNomineeDropdownCaption);
                        AppUtility.showLog(AppConstant.LOG_STATUS, TAG, " Nominee Label :" + ite1.getNomineeLabel());
                        nomineeMemberList.add(ite1);
                    }
                }
            }

            nomineeMemberList.add(new NomineeMemberItem(2, "Other", "Other"));
            for (NomineeMemberItem item : nomineeMemberList) {
                AppUtility.showLog(AppConstant.LOG_STATUS, TAG, " Nominee Label :" + item.getNomineeLabel());
                spinnerList.add(item.getNomineeLabel());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.custom_drop_down, R.id.textView, spinnerList);
            memberSP.setAdapter(adapter);
            int selectNominee = 0;
            for (int i = 0; i < nomineeMemberList.size(); i++) {
                if (seccItem.getNameNominee() != null && seccItem.getNameNominee().equalsIgnoreCase(nomineeMemberList.get(i).memberName)) {
                    selectNominee = i;
                    break;
                }
            }
            if (selectNominee == 0) {
                if (seccItem.getNameNominee() != null && !seccItem.getNameNominee().equalsIgnoreCase("")) {
                    selectNominee = nomineeMemberList.size() - 1;
                }
            }
            memberSP.setSelection(selectNominee);
        }

    }

    private void prepareNomineeRelation() {
        relationList = new ArrayList<>();
        ArrayList<RelationItem> list = new ArrayList<>();
        if (nomineeItem != null && nomineeItem.getMemberItem().getDataSource() != null && nomineeItem.getMemberItem().getDataSource().trim().equalsIgnoreCase(AppConstant.RSBY_SOURCE)) {
            if (nomineeItem.getMemberItem() != null && nomineeItem.getMemberItem().getRsbyGender().trim().equalsIgnoreCase(AppConstant.MALE_GENDER)) {
                list = SeccDatabase.getRelationListByGenderForNominee(context, "M");
            } else if (nomineeItem.getMemberItem() != null && nomineeItem.getMemberItem().getRsbyGender().trim().equalsIgnoreCase(AppConstant.FEMALE_GENDER)) {
                list = SeccDatabase.getRelationListByGenderForNominee(context, "F");
            } else {
                list = SeccDatabase.getRelationList(context);
            }
        } else {
            if (nomineeItem.getMemberItem() != null && nomineeItem.getMemberItem().getGenderid().trim().equalsIgnoreCase(AppConstant.MALE_GENDER)) {
                list = SeccDatabase.getRelationListByGenderForNominee(context, "M");
            } else if (nomineeItem.getMemberItem() != null && nomineeItem.getMemberItem().getGenderid().trim().equalsIgnoreCase(AppConstant.FEMALE_GENDER)) {
                list = SeccDatabase.getRelationListByGenderForNominee(context, "F");
            } else {
                list = SeccDatabase.getRelationList(context);
            }
        }
        RelationItem item1 = new RelationItem();
        item1.setRelationCode(AppConstant.DEFAULT_RELATION);
        item1.setRelationName("Select Relation");
        relationList.add(0, item1);
        for (RelationItem relItem : list) {
            if (relItem.getRelationCode().equalsIgnoreCase(AppConstant.HEAD_RELATION)) {

            } else  if (relItem.getRelationCode().equalsIgnoreCase(AppConstant.UNKNOWN_RELATION)) {

            } else {
                relationList.add(relItem);
            }
        }
        Collections.sort(relationList, new Comparator<RelationItem>() {
            @Override
            public int compare(RelationItem relationItem, RelationItem t1) {

                return Integer.parseInt(relationItem.getRelationCode()) - Integer.parseInt(t1.getRelationCode());
            }
        });
        ArrayList<String> spinnerList = new ArrayList<>();
        //spinnerList.add("Select Relation");
        for (RelationItem item : relationList) {
            spinnerList.add(item.getRelationName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.custom_drop_down, R.id.textView, spinnerList);
        nomineeRelSP.setAdapter(adapter);
        int selectedPos = 0;
        for (int i = 0; i < relationList.size(); i++) {
            if (seccItem.getRelationNomineeCode() != null && seccItem.getRelationNomineeCode().equalsIgnoreCase(relationList.get(i).getRelationCode())) {
                selectedPos = i;
                //  submitBt.setVisibility(View.VISIBLE);
                break;
            }
        }
        if (selectedPos != 0)
            nomineeRelSP.setSelection(selectedPos);


    }

    private void prepareNomineeOtherRelation() {
        //nomineeMemberList=new ArrayList<>();
        otherRelationList = new ArrayList<>();
        ArrayList<RelationItem> list = SeccDatabase.getRelationList(context);
        RelationItem item1 = new RelationItem();
        item1.setRelationCode(AppConstant.DEFAULT_RELATION);
        item1.setRelationName("Select Relation");
        otherRelationList.add(0, item1);
        for (RelationItem relationItem : list) {
            if (relationItem.getRelationCode().equalsIgnoreCase(AppConstant.HEAD_RELATION)) {

            } else if (relationItem.getRelationCode().equalsIgnoreCase(AppConstant.UNKNOWN_RELATION)) {

            } else {
                otherRelationList.add(relationItem);
            }
        }
        Collections.sort(otherRelationList, new Comparator<RelationItem>() {
            @Override
            public int compare(RelationItem relationItem, RelationItem t1) {

                return Integer.parseInt(relationItem.getRelationCode()) - Integer.parseInt(t1.getRelationCode());
            }
        });
        ArrayList<String> spinnerList = new ArrayList<>();
        for (RelationItem item : otherRelationList) {
            spinnerList.add(item.getRelationName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.custom_drop_down, R.id.textView, spinnerList);
        otherRelSP.setAdapter(adapter);
        int selectedPos = 0;
        AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Other Relation code : " + seccItem.getRelationNomineeCode());
        for (int i = 0; i < otherRelationList.size(); i++) {
            if (seccItem.getRelationNomineeCode() != null &&
                    seccItem.getRelationNomineeCode().equalsIgnoreCase(otherRelationList.get(i).getRelationCode())) {
                selectedPos = i;
                // submitBt.setVisibility(View.VISIBLE);
                break;
            }
        }
        //if(!isMemberRel) {
        // if(selectedPos!=0)
        otherRelSP.setSelection(selectedPos);
        //}
    }

    private void showRsbyDetail(SeccMemberItem item) {
        headerTV.setText(item.getRsbyName());
    }

    private void showSeccDetail(SeccMemberItem item) {
        headerTV.setText(item.getName());
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

    String mm  = "NOTE: Selected nominee member status must not be following status.\n" +
            "\\s\\s\\s\\s\\s\\s1.Member Migrated\n" +
            "\\s\\s\\s\\s\\s\\s2.Member died\n" +
            "\\s\\s\\s\\s\\s\\s3. Unknown member\n" +
            "\\s\\s\\s\\s\\s\\s4. Enrollment through RSBY";

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
