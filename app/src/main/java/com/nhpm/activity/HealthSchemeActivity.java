package com.nhpm.activity;

import android.app.AlertDialog;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;

import com.customComponent.CustomAlert;
import com.customComponent.utility.ProjectPrefrence;
import com.nhpm.BaseActivity;
import com.nhpm.LocalDataBase.dto.SeccDatabase;
import com.nhpm.Models.response.master.ConfigurationItem;
import com.nhpm.Models.response.master.HealthSchemeItem;
import com.nhpm.Models.response.master.StateItem;
import com.nhpm.Models.response.seccMembers.HouseHoldItem;
import com.nhpm.Models.response.seccMembers.SeccMemberItem;
import com.nhpm.Models.response.seccMembers.SelectedMemberItem;
import com.nhpm.Models.response.verifier.VerifierLocationItem;
import com.nhpm.Models.response.verifier.VerifierLoginResponse;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;
import com.nhpm.Utility.AppUtility;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.TreeSet;

import pl.polidea.view.ZoomView;

import static com.nhpm.Utility.AppUtility.DuplicateCharRemoverbool;
import static com.nhpm.Utility.AppUtility.isCheckFirstTwoChar;

public class HealthSchemeActivity extends BaseActivity implements ComponentCallbacks2 {
    private EditText schemeEditText1, schemeEditText2, schemeEditText3;
    private CheckBox schemeCheckBox1, schemeCheckBox2, schemeCheckBox3;
    private LinearLayout schemeLayout1, schemeLayout2, schemeLayout3;
    private boolean isUrnId1, isUrnId2, isUrnId3;
    private String schemeId1, schemeId2, schemeId3;
    private TextView headerTV;
    private ArrayList<HealthSchemeItem> schemeList;
    private Context context;
    private Spinner schemeSP;
    private Button submitBt;
    private ImageView backIV;
    private int navigateType;
    private SelectedMemberItem selectedMemItem;
    private SeccMemberItem seccItem;
    private LinearLayout stateHealthSchemeLayout;
    private EditText stateHealthIdET;
    private AutoCompleteTextView urnET;
    private String TAG = "HealthSchemeActivity";
    private LinearLayout mZoomLinearLayout;
    private ZoomView zoomView;
    private SeccMemberItem headOfThefamily;
    private HouseHoldItem houseHoldItem;
    private LinearLayout sstateHealthSchemeLayout, urnNumberLayout;
    private ArrayList<String> urnList = new ArrayList<>();
    private VerifierLocationItem selectedLocation;
    private String validationMode = "g";
    private String additionalScheme = "Y";
    private String validationDataSource = "Y";
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
            setContentView(R.layout.activity_health_scheme);
            setupScreenWithoutZoom();
        } else {
            setContentView(R.layout.dummy_layout_for_zooming);
            setupScreenWithZoom();
        }


        schemeEditText1 = (EditText) findViewById(R.id.schemeEditText1);
        schemeEditText1.setFilters(new InputFilter[]{filter});


    }

    private String blockCharacterSet = " :-/\\\\\\.";

    private InputFilter filter = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            schemeEditText1 = (EditText) findViewById(R.id.schemeEditText1);

            if (source != null && blockCharacterSet.contains(("" + source))) {

                if (schemeEditText1.getText().length() <=2) {
                    return "";
                } else {
                    return null;
                }
            }
            return null;
        }
    };

    private void setupScreenWithZoom() {
        mZoomLinearLayout = (LinearLayout) findViewById(R.id.mZoomLinearLayout);
        View v = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.activity_health_scheme, null, false);
        v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
        showNotification(v);

        selectedMemItem = SelectedMemberItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.SELECTED_ITEM_FOR_VERIFICATION, context));
        selectedLocation = VerifierLocationItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.SELECTED_BLOCK, context));
        headerTV = (TextView) v.findViewById(R.id.centertext);
        schemeSP = (Spinner) v.findViewById(R.id.healthSchemeNameSP);
        sstateHealthSchemeLayout = (LinearLayout) v.findViewById(R.id.sstateHealthSchemeLayout);
        submitBt = (Button) v.findViewById(R.id.submitBT);
        stateHealthIdET = (EditText) v.findViewById(R.id.stateHealthIdET);
        backIV = (ImageView) v.findViewById(R.id.back);
        urnET = (AutoCompleteTextView) v.findViewById(R.id.rsbyURNNoET);
        //  additional scheme checkbox

        schemeEditText1 = (EditText) v.findViewById(R.id.schemeEditText1);
        schemeEditText2 = (EditText) v.findViewById(R.id.schemeEditText2);
        schemeEditText3 = (EditText) v.findViewById(R.id.schemeEditText3);
        schemeCheckBox1 = (CheckBox) v.findViewById(R.id.schemeCheckBox1);
        schemeCheckBox2 = (CheckBox) v.findViewById(R.id.schemeCheckBox2);
        schemeCheckBox3 = (CheckBox) v.findViewById(R.id.schemeCheckBox3);
        schemeLayout1 = (LinearLayout) v.findViewById(R.id.schemeLayout1);
        schemeLayout2 = (LinearLayout) v.findViewById(R.id.schemeLayout2);
        schemeLayout3 = (LinearLayout) v.findViewById(R.id.schemeLayout3);

        AppUtility.requestFocus(urnET);
        //   prepareSchemeSpinner();
        stateHealthSchemeLayout = (LinearLayout) v.findViewById(R.id.stateHealthSchemeLayout);
        urnNumberLayout = (LinearLayout) v.findViewById(R.id.urnNumberLayout);
        dashboardDropdown(v);
        zoomView = new ZoomView(this);
        zoomView.addView(v);
        mZoomLinearLayout.addView(zoomView);


        if (selectedMemItem.getSeccMemberItem() != null) {
            seccItem = selectedMemItem.getSeccMemberItem();
            houseHoldItem = selectedMemItem.getHouseHoldItem();
            ArrayList<SeccMemberItem> seccMemList = new ArrayList<>();
            if (seccItem != null && seccItem.getDataSource() != null && seccItem.getDataSource().trim().equalsIgnoreCase(AppConstant.RSBY_SOURCE)) {
                seccMemList = SeccDatabase.getRsbyMemberListWithUrn(seccItem.getRsbyUrnId(), context);
                showRsbyDetail(seccItem);


            } else {
                seccMemList = SeccDatabase.getSeccMemberList(seccItem.getHhdNo(), context);
                showSeccDetail(seccItem);
            }

            for (SeccMemberItem item : seccMemList) {
                if (item.getNhpsRelationCode() != null && item.getNhpsRelationCode().trim().equalsIgnoreCase(AppConstant.NEW_HEAD_RELATION_CODE)) {
                    // if(item.getNhpsMemId().trim().equalsIgnoreCase(houseHoldItem.getNhpsMemId().trim())){
                    headOfThefamily = item;
                    //      AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Head of the family name : " + item.getName() + " Scheme code : " + item.getSchemeId());
                    // }
                }
                if (item.getUrnNo() != null && !item.getUrnNo().equalsIgnoreCase("")) {
                    //if(urnList.size()>0){
                    AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Captured URN : " + item.getUrnNo());
                    //for(int i=0;i<urnList.size();i++){
                    //if(item.getUrnNo().equalsIgnoreCase(urnList.get(i))){
                    //  urnList.set(i,item.getUrnNo());
                    //}else{
                    urnList.add(item.getUrnNo());
                    //}
                    // }
                    //}
                }
            }
            TreeSet<String> businessTypeSet = new TreeSet<String>(new Comparator<String>() {

                public int compare(String o1, String o2) {
                    // return 0 if objects are equal in terms of your properties
                    if (o1.equalsIgnoreCase(o2)) {
                        return 0;
                    }
                    return 1;
                }
            });
            businessTypeSet.addAll(urnList);
            urnList = new ArrayList<>();
            urnList.addAll(businessTypeSet);
            // wardTypeSpinnerList.addAll(businessTypeSet);

            if (seccItem.getUrnNo() != null && !seccItem.getUrnNo().equalsIgnoreCase("")) {

                urnET.setText(seccItem.getUrnNo());
            }

        }
        prepareSchemeCheckBox();
        AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Captured urn list :" + urnList.size());
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, urnList);
        urnET.setThreshold(1);
        urnET.setAdapter(adapter);

        schemeSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                stateHealthSchemeLayout.setVisibility(View.GONE);
                if (position == 0) {

                } else {
                    AppUtility.clearFocus(urnET);
                    AppUtility.requestFocus(stateHealthIdET);
                    stateHealthSchemeLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        submitBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // backNSubmit();
                submitStateHealth();
            }
        });
        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent theIntent = null;
                // if (seccItem != null && seccItem.getAadhaarStatus().equalsIgnoreCase("1")) {
                theIntent = new Intent(context, WithAadhaarActivity.class);
                /*} else {
                    theIntent = new Intent(context, WithoutAadhaarVerificationActivity.class);
                }*/
                startActivity(theIntent);
                rightTransition();
                finish();
                //backNSubmit();
            }
        });

        if (additionalScheme.equalsIgnoreCase("N")) {
            sstateHealthSchemeLayout.setVisibility(View.GONE);
            urnNumberLayout.setVisibility(View.GONE);
        } else if (additionalScheme.equalsIgnoreCase("S")) {
            sstateHealthSchemeLayout.setVisibility(View.VISIBLE);
            urnNumberLayout.setVisibility(View.GONE);
        } else if (additionalScheme.equalsIgnoreCase("R")) {
            sstateHealthSchemeLayout.setVisibility(View.GONE);
            urnNumberLayout.setVisibility(View.VISIBLE);
        } else if (additionalScheme.equalsIgnoreCase("B")) {
            urnNumberLayout.setVisibility(View.VISIBLE);
            sstateHealthSchemeLayout.setVisibility(View.VISIBLE);
        }


    }

    private void setupScreenWithoutZoom() {
        showNotification();

        selectedMemItem = SelectedMemberItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.SELECTED_ITEM_FOR_VERIFICATION, context));
        selectedLocation = VerifierLocationItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.SELECTED_BLOCK, context));
        headerTV = (TextView) findViewById(R.id.centertext);
        schemeSP = (Spinner) findViewById(R.id.healthSchemeNameSP);
        sstateHealthSchemeLayout = (LinearLayout) findViewById(R.id.sstateHealthSchemeLayout);
        submitBt = (Button) findViewById(R.id.submitBT);
        stateHealthIdET = (EditText) findViewById(R.id.stateHealthIdET);
        backIV = (ImageView) findViewById(R.id.back);
        urnET = (AutoCompleteTextView) findViewById(R.id.rsbyURNNoET);
        //  additional scheme checkbox

        schemeEditText1 = (EditText) findViewById(R.id.schemeEditText1);
        schemeEditText2 = (EditText) findViewById(R.id.schemeEditText2);
        schemeEditText3 = (EditText) findViewById(R.id.schemeEditText3);
        schemeCheckBox1 = (CheckBox) findViewById(R.id.schemeCheckBox1);
        schemeCheckBox2 = (CheckBox) findViewById(R.id.schemeCheckBox2);
        schemeCheckBox3 = (CheckBox) findViewById(R.id.schemeCheckBox3);
        schemeLayout1 = (LinearLayout) findViewById(R.id.schemeLayout1);
        schemeLayout2 = (LinearLayout) findViewById(R.id.schemeLayout2);
        schemeLayout3 = (LinearLayout) findViewById(R.id.schemeLayout3);

        AppUtility.requestFocus(urnET);
        //   prepareSchemeSpinner();
        stateHealthSchemeLayout = (LinearLayout) findViewById(R.id.stateHealthSchemeLayout);
        urnNumberLayout = (LinearLayout) findViewById(R.id.urnNumberLayout);
        dashboardDropdown();


        if (selectedMemItem.getSeccMemberItem() != null) {
            seccItem = selectedMemItem.getSeccMemberItem();
            houseHoldItem = selectedMemItem.getHouseHoldItem();
            ArrayList<SeccMemberItem> seccMemList = new ArrayList<>();
            if (seccItem != null && seccItem.getDataSource() != null && seccItem.getDataSource().trim().equalsIgnoreCase(AppConstant.RSBY_SOURCE)) {
                seccMemList = SeccDatabase.getRsbyMemberListWithUrn(seccItem.getRsbyUrnId(), context);
                showRsbyDetail(seccItem);


            } else {
                seccMemList = SeccDatabase.getSeccMemberList(seccItem.getHhdNo(), context);
                showSeccDetail(seccItem);
            }

            for (SeccMemberItem item : seccMemList) {
                if (item.getNhpsRelationCode() != null && item.getNhpsRelationCode().trim().equalsIgnoreCase(AppConstant.NEW_HEAD_RELATION_CODE)) {
                    // if(item.getNhpsMemId().trim().equalsIgnoreCase(houseHoldItem.getNhpsMemId().trim())){
                    headOfThefamily = item;
                    //      AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Head of the family name : " + item.getName() + " Scheme code : " + item.getSchemeId());
                    // }
                }
                if (item.getUrnNo() != null && !item.getUrnNo().equalsIgnoreCase("")) {
                    //if(urnList.size()>0){
                    AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Captured URN : " + item.getUrnNo());
                    //for(int i=0;i<urnList.size();i++){
                    //if(item.getUrnNo().equalsIgnoreCase(urnList.get(i))){
                    //  urnList.set(i,item.getUrnNo());
                    //}else{
                    urnList.add(item.getUrnNo());
                    //}
                    // }
                    //}
                }
            }
            TreeSet<String> businessTypeSet = new TreeSet<String>(new Comparator<String>() {

                public int compare(String o1, String o2) {
                    // return 0 if objects are equal in terms of your properties
                    if (o1.equalsIgnoreCase(o2)) {
                        return 0;
                    }
                    return 1;
                }
            });
            businessTypeSet.addAll(urnList);
            urnList = new ArrayList<>();
            urnList.addAll(businessTypeSet);
            // wardTypeSpinnerList.addAll(businessTypeSet);

            if (seccItem.getUrnNo() != null && !seccItem.getUrnNo().equalsIgnoreCase("")) {

                urnET.setText(seccItem.getUrnNo());
            }


        }
        prepareSchemeCheckBox();
        AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Captured urn list :" + urnList.size());
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, urnList);
        urnET.setThreshold(1);
        urnET.setAdapter(adapter);

        schemeSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                stateHealthSchemeLayout.setVisibility(View.GONE);
                if (position == 0) {

                } else {
                    AppUtility.clearFocus(urnET);
                    AppUtility.requestFocus(stateHealthIdET);
                    stateHealthSchemeLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        submitBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // backNSubmit();
                submitStateHealth();
            }
        });
        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent theIntent = null;
                // if (seccItem != null && seccItem.getAadhaarStatus().equalsIgnoreCase("1")) {
                theIntent = new Intent(context, WithAadhaarActivity.class);
                /*} else {
                    theIntent = new Intent(context, WithoutAadhaarVerificationActivity.class);
                }*/
                startActivity(theIntent);
                rightTransition();
                finish();
                //backNSubmit();
            }
        });

        if (additionalScheme.equalsIgnoreCase("N")) {
            sstateHealthSchemeLayout.setVisibility(View.GONE);
            urnNumberLayout.setVisibility(View.GONE);
        } else if (additionalScheme.equalsIgnoreCase("S")) {
            sstateHealthSchemeLayout.setVisibility(View.VISIBLE);
            urnNumberLayout.setVisibility(View.GONE);
        } else if (additionalScheme.equalsIgnoreCase("R")) {
            sstateHealthSchemeLayout.setVisibility(View.GONE);
            urnNumberLayout.setVisibility(View.VISIBLE);
        } else if (additionalScheme.equalsIgnoreCase("B")) {
            urnNumberLayout.setVisibility(View.VISIBLE);
            sstateHealthSchemeLayout.setVisibility(View.VISIBLE);
        }


    }


    private void submitStateHealth() {
        int selectedIndex = schemeSP.getSelectedItemPosition();
        String urnNo = urnET.getText().toString();
        if (additionalScheme.equalsIgnoreCase("N")) {

        } else {
            if (selectedIndex == 0) {
                stateHealthIdET.setText("");
            } else {
                //        HealthSchemeItem item = schemeList.get(selectedIndex);
                if (isUrnId1) {
                    seccItem.setSchemeId1(schemeId1);
                    String schemeId = schemeEditText1.getText().toString();

                    if (DuplicateCharRemoverbool(schemeId)) {
                        schemeEditText1.setText("");
                        CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterValidSchemeNum));
                        return;
                    }

                    if(!isCheckFirstTwoChar(schemeId)){
                        schemeEditText1.setText("");
                        CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterValidSchemeNum));
                        return;
                    }

                    if (!CheckSchemeNumbers(schemeId)) {
                        CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterValidSchemeNum));
                        return;
                    } else if (!schemeId.equalsIgnoreCase("")) {
                        seccItem.setSchemeNo1(schemeId);
                    } else {
                        CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterSchemeNum));
                        return;
                    }
                } else {
                    seccItem.setSchemeNo1(null);
                    seccItem.setSchemeId1(null);
                }
                if (isUrnId2) {
                    seccItem.setSchemeId2(schemeId2);
                    String schemeId = schemeEditText2.getText().toString();
                    if (!CheckSchemeNumbers(schemeId)) {
                        CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterValidSchemeNum));
                        return;
                    } else if (!schemeId.equalsIgnoreCase("")) {
                        seccItem.setSchemeNo2(schemeId);
                    } else {
                        CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterSchemeNum));
                        return;
                    }
                } else {
                    seccItem.setSchemeNo2(null);
                    seccItem.setSchemeId2(null);
                }
                if (isUrnId3) {
                    seccItem.setSchemeId3(schemeId3);
                    String schemeId = schemeEditText3.getText().toString();
                    if (!CheckSchemeNumbers(schemeId)) {
                        CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterValidSchemeNum));
                        return;
                    } else if (!schemeId.equalsIgnoreCase("")) {
                        seccItem.setSchemeNo3(schemeId);
                    } else {
                        CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterSchemeNum));
                        return;
                    }
                } else {
                    seccItem.setSchemeNo3(null);
                    seccItem.setSchemeId3(null);
                }

            }

        }


           /* if(schemeId.equalsIgnoreCase("")){
                CustomAlert.alertWithOk(context,"Please enter Scheme ID");
            }else{*/
        if (validationDataSource.equalsIgnoreCase("Y")) {
            if (urnNo != null && !urnNo.equalsIgnoreCase("") && urnNo.length() < 17) {
                CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnter17digitUrn));
                return;
            }
        } else {
            urnNumberLayout.setVisibility(View.GONE);
        }


        seccItem.setUrnNo(urnNo);
        //  seccItem.setStateSchemeCodeAuth("P");
        seccItem.setLockedSave(AppConstant.SAVE + "");
        /*if(selectedMemItem.getOldHeadMember()!=null && selectedMemItem.getNewHeadMember()!=null){
            SeccMemberItem oldHead=selectedMemItem.getOldHeadMember();
            oldHead.setLockedSave(AppConstant.LOCKED+"");
            AppUtility.showLog(AppConstant.LOG_STATUS,TAG," OLD Household name " +
                    ": "+oldHead.getName()+"" +
                    " Member Status "+oldHead.getMemStatus()+" House hold Status :" +
                    " "+oldHead.getHhStatus()+" Locked Save :"+oldHead.getLockedSave());
            SeccDatabase.updateSeccMember(selectedMemItem.getOldHeadMember(),context);
        }
        SeccDatabase.updateSeccMember(seccItem,context);
        SeccDatabase.updateHouseHold(selectedMemItem.getHouseHoldItem(),context);
       // SeccDatabase.getSeccMemberDetail(seccItem.getNhpsMemId(),context);
        seccItem=SeccDatabase.getSeccMemberDetail(seccItem,context);
                ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                        AppConstant.SELECTED_ITEM_FOR_VERIFICATION,selectedMemItem.serialize(),context);
                Intent theIntent = new Intent(context, WithAadhaarActivity.class);
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

    private void prepareSchemeSpinner() {
        //  schemeList=new ArrayList<>();
        schemeList = SeccDatabase.getHealthSchemeList(context, selectedLocation.getStateCode().trim());
       /* schemeList.add(new HealthSchemeItem("24","001","Mukhyamantri Amrutam"));
        schemeList.add(new HealthSchemeItem("24","002","MA Vatsalya"));
        Log.d("Health Scheme List", "scheme list : "+schemeList.size());*/
        if (schemeList != null && schemeList.size() > 0) {
            ArrayList<String> spinnerList = new ArrayList<>();
            HealthSchemeItem defaultItem = new HealthSchemeItem();
            defaultItem.setSchemeId("-1");
            defaultItem.setSchemeName("Select State Scheme");
            schemeList.add(0, defaultItem);
            for (HealthSchemeItem item : schemeList) {
                spinnerList.add(item.getSchemeName());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.custom_drop_down, R.id.textView, spinnerList);
            schemeSP.setAdapter(adapter);
        } else {
            sstateHealthSchemeLayout.setVisibility(View.GONE);
        }

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

    public void showNotification() {

        LinearLayout notificationLayout = (LinearLayout) findViewById(R.id.notificationLayout);
        WebView notificationWebview = (WebView) findViewById(R.id.notificationWebview);
        String prePairedMessage = AppUtility.getNotificationData(context);
        if (prePairedMessage != null) {
            notificationLayout.setVisibility(View.VISIBLE);
            notificationWebview.loadData(prePairedMessage, "text/html", "utf-8"); // Set focus to textview
        }
    }

    private void checkAppConfig() {
        StateItem selectedStateItem = selectedStateItem = StateItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SELECTED_STATE, context));
        ArrayList<ConfigurationItem> configList = SeccDatabase.findConfiguration(selectedStateItem.getStateCode(), context);

        if (configList != null) {
            for (ConfigurationItem item1 : configList) {
                if (item1.getConfigId().equalsIgnoreCase(AppConstant.VALIDATION_MODE_CONFIG)) {
                    validationMode = item1.getStatus();
                }
                if (item1.getConfigId().equalsIgnoreCase(AppConstant.ADDITIONAL_SCHEME)) {
                    additionalScheme = item1.getStatus();
                }
                if (item1.getConfigId().equalsIgnoreCase(AppConstant.RSBY_DATA_SOURCE_CONFIG)) {
                    validationDataSource = item1.getStatus();
                }
                if (item1.getConfigId().equalsIgnoreCase(AppConstant.APPLICATION_ZOOM)) {
                    zoomMode = item1.getStatus();
                }
            }
        }

    }

    private void prepareSchemeCheckBox() {
        schemeList = SeccDatabase.getHealthSchemeList(context, selectedLocation.getStateCode().trim());
        if (schemeList != null && schemeList.size() > 0) {
            ArrayList<HealthSchemeItem> newSchemeList = new ArrayList<>();
            int listCount = 0;
            if (schemeList.size() > 3) {
                listCount = 3;
            } else if (schemeList.size() == 2) {
                listCount = 2;
            } else if (schemeList.size() == 1) {
                listCount = 1;
            }
            for (int i = 0; i < listCount; i++) {
                newSchemeList.add(schemeList.get(i));
            }

            if (newSchemeList.size() == 1) {
                schemeCheckBox1.setText(schemeList.get(0).getSchemeName());
                schemeLayout1.setVisibility(View.VISIBLE);
                schemeId1 = schemeList.get(0).getSchemeId();
              /*  schemeEditText1.setEnabled(true);
                schemeEditText2.setEnabled(false);
                schemeEditText3.setEnabled(false);*/
            } else if (newSchemeList.size() == 2) {
                schemeCheckBox1.setText(schemeList.get(0).getSchemeName());
                schemeId1 = schemeList.get(0).getSchemeId();
                schemeCheckBox2.setText(schemeList.get(1).getSchemeName());
                schemeId2 = schemeList.get(1).getSchemeId();
                schemeLayout1.setVisibility(View.VISIBLE);
                schemeLayout2.setVisibility(View.VISIBLE);
           /*     schemeEditText1.setEnabled(true);
                schemeEditText2.setEnabled(true);
                schemeEditText3.setEnabled(false);*/
            } else if (newSchemeList.size() == 3) {
                schemeCheckBox1.setText(schemeList.get(0).getSchemeName());
                schemeId1 = schemeList.get(0).getSchemeId();
                schemeCheckBox2.setText(schemeList.get(1).getSchemeName());
                schemeId2 = schemeList.get(1).getSchemeId();
                schemeCheckBox3.setText(schemeList.get(2).getSchemeName());
                schemeId3 = schemeList.get(2).getSchemeId();
            /*    schemeEditText1.setEnabled(true);
                schemeEditText2.setEnabled(true);
*/
                schemeLayout1.setVisibility(View.VISIBLE);
                schemeLayout2.setVisibility(View.VISIBLE);
                schemeLayout3.setVisibility(View.VISIBLE);
            }

        } else {
            schemeLayout1.setVisibility(View.GONE);
            schemeLayout3.setVisibility(View.GONE);
            schemeLayout2.setVisibility(View.GONE);
        }

        schemeCheckBox3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    schemeEditText3.setEnabled(true);
                    schemeEditText3.requestFocus();
                    isUrnId3 = true;
                } else {
                    schemeEditText3.setEnabled(false);
                    isUrnId3 = false;
                }
            }
        });

        schemeCheckBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    schemeEditText2.setEnabled(true);
                    schemeEditText2.requestFocus();
                    isUrnId2 = true;
                } else {
                    schemeEditText2.setEnabled(false);
                    isUrnId2 = false;
                }
            }
        });
        schemeCheckBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    schemeEditText1.setEnabled(true);
                    schemeEditText1.requestFocus();
                    isUrnId1 = true;
                } else {
                    schemeEditText1.setEnabled(false);
                    isUrnId1 = false;
                }
            }
        });


        if (seccItem != null) {
            if (seccItem.getSchemeId1() != null && seccItem.getSchemeNo1() != null) {
                if (!seccItem.getSchemeId1().equalsIgnoreCase("") && !seccItem.getSchemeNo1().equalsIgnoreCase("")) {
                    schemeEditText1.setEnabled(true);
                    schemeEditText1.setText(seccItem.getSchemeNo1());
                    schemeCheckBox1.setChecked(true);
                    isUrnId1 = true;
                }
            }
            if (seccItem.getSchemeId2() != null && seccItem.getSchemeNo2() != null) {
                if (!seccItem.getSchemeId2().equalsIgnoreCase("") && !seccItem.getSchemeNo2().equalsIgnoreCase("")) {
                    schemeEditText2.setEnabled(true);
                    schemeEditText2.setText(seccItem.getSchemeNo2());
                    schemeCheckBox2.setChecked(true);
                    isUrnId2 = true;
                }
            }
            if (seccItem.getSchemeId3() != null && seccItem.getSchemeNo3() != null) {
                if (!seccItem.getSchemeId3().equalsIgnoreCase("") && !seccItem.getSchemeNo3().equalsIgnoreCase("")) {
                    schemeEditText3.setEnabled(true);
                    schemeEditText3.setText(seccItem.getSchemeNo3());
                    schemeCheckBox3.setChecked(true);
                    isUrnId3 = true;
                }
            }
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

    public static boolean CheckSchemeNumbers(String input) {
        if (input != null && input.length() > 2) {

        } else {
            return false;
        }

        for (int ctr = 0; ctr < input.length(); ctr++) {
            if ("1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ:/\\. ".contains(Character.valueOf(input.charAt(ctr)).toString())) {
                continue;
            } else {
                return false;
            }
        }
        return true;
    }
}
