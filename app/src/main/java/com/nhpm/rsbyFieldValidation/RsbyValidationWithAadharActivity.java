package com.nhpm.rsbyFieldValidation;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.customComponent.CustomAlert;
import com.customComponent.utility.ProjectPrefrence;
import com.nhpm.activity.BaseActivity;
import com.nhpm.LocalDataBase.dto.SeccDatabase;
import com.nhpm.Models.response.master.AadhaarStatusItem;
import com.nhpm.Models.response.rsbyMembers.RSBYItem;
import com.nhpm.Models.response.rsbyMembers.RsbyHouseholdItem;
import com.nhpm.Models.response.seccMembers.SelectedMemberItem;
import com.nhpm.Models.response.verifier.VerifierLoginResponse;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;
import com.nhpm.Utility.AppUtility;

import java.util.ArrayList;

import pl.polidea.view.ZoomView;

/**
 * Created by DELL on 26-02-2017.
 */

public class RsbyValidationWithAadharActivity extends BaseActivity {
    private TextView aadhaarDetailTV,bankDetailTV,rsbyDetTV,mobileNoTV,
            healthSchemeNoTV,nomineeTV,govtIdDetTV,memberPhotoTV;
    private Context context;
    private TextView headerTV;
    private Spinner memberStatSP;
    private RelativeLayout menuLayout;
    private int selectedAadhaarStatus;
    private RelativeLayout enrollmentLayout,bankLayout,rsbyLayout,mobileLayout,healthSchemelayout,
            nomineeLayout,editMemberDetlayout;
    private LinearLayout photoLayout,govtIdLayout,aadhaarLayout;
    private RelativeLayout aadhaarStatusLayout;
    private RelativeLayout aadhaarStatLayout;
    private Button updateNhpsBT;
    private ImageView backIV;
/*    private rsbyItem rsbyItem;*/
    private RsbyHouseholdItem houseHoldItem;
    private RSBYItem rsbyItem;
    private int memberType;
    private SelectedMemberItem selectedMemItem;
    private Button lockBT;
    private final String TAG="With Aadhaar Activity";
    private ArrayList<AadhaarStatusItem> aadhaarStatusList;
    private Spinner aadhaarStatusSP;
    private RadioGroup aadhaarStatusRG;
    private RadioButton aadhaarAvailableRB,aadhaarNotPrepRB;
    private String aadhaarStatus;
    private RelativeLayout photoCaptureStatus,govtIdCaptureStatus,aadhaarCaptureStatus,mobileCaptureStatus,
            nomineeCaptureStatus,additionalCaptureStatus;
    private AlertDialog internetDiaolg;
    private LinearLayout mZoomLinearLayout;
    private ZoomView zoomView;
    private ImageView photoVerifiedIV;
    private VerifierLoginResponse loginResponse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dummy_layout_for_zooming);
        setupScreen();
        logoutScreen();
    }
    private void logoutScreen(){
        final ImageView settings=(ImageView)findViewById(R.id.settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(context, settings);
                popup.getMenuInflater()
                        .inflate(R.menu.menu_home, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.logout:
                                logoutVerifier();
                                break;

                        }
                        return true;
                    }
                });
                popup.show();
            }
        });

    }
    private void setupScreen(){
        context=this;
        mZoomLinearLayout = (LinearLayout) findViewById(R.id.mZoomLinearLayout);
        View v = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.activity_nhpsmember, null, false);
        v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
        loginResponse=(VerifierLoginResponse.create(ProjectPrefrence.
                getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.VERIFIER_CONTENT,context)));

        headerTV=(TextView)v.findViewById(R.id.centertext);
        selectedMemItem= SelectedMemberItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.SELECTED_ITEM_FOR_VERIFICATION, context));
        if(selectedMemItem!=null && selectedMemItem.getRsbyHouseholdItem()!=null){
            houseHoldItem=selectedMemItem.getRsbyHouseholdItem();
        }
        if(selectedMemItem !=null && selectedMemItem.getRsbyMemberItem()!=null) {
            rsbyItem =selectedMemItem.getRsbyMemberItem();
            System.out.print(rsbyItem.getName());
            headerTV.setText(rsbyItem.getName());
        }
        AppUtility.showLog(AppConstant.LOG_STATUS,TAG," Secc Member Item : "+rsbyItem);
        photoCaptureStatus=(RelativeLayout)v.findViewById(R.id.photoCaptureStatus);
        govtIdCaptureStatus=(RelativeLayout)v.findViewById(R.id.govtidCaptureStatus);
        aadhaarCaptureStatus=(RelativeLayout)v.findViewById(R.id.aadharCaptureStatus);
        mobileCaptureStatus=(RelativeLayout)v.findViewById(R.id.mobileCaptureStatus);
        nomineeCaptureStatus=(RelativeLayout)v.findViewById(R.id.nomineeCaptureStatus);
        additionalCaptureStatus=(RelativeLayout)v.findViewById(R.id.additionalCaptureStatus);
        photoVerifiedIV = (ImageView) v.findViewById(R.id.photoVerifiedIV);
        photoVerifiedIV.setVisibility(View.GONE);
        menuLayout=(RelativeLayout)v.findViewById(R.id.menuLayout) ;

        aadhaarStatusRG=(RadioGroup)v.findViewById(R.id.aadhaarStatusRG);
        aadhaarAvailableRB=(RadioButton)v.findViewById(R.id.aadhaarAvailRB);
        aadhaarNotPrepRB=(RadioButton)v.findViewById(R.id.aadhaarNotPrepRB);
        aadhaarStatusSP=(Spinner)v.findViewById(R.id.aadhaarStatusSP);
        aadhaarLayout=(LinearLayout)v. findViewById(R.id.aadhaarRelLayout);
        enrollmentLayout=(RelativeLayout)v.findViewById(R.id.enrollRelLayout);
        bankLayout=(RelativeLayout)v.findViewById(R.id.bankDetRelLayout);
        rsbyLayout=(RelativeLayout)v.findViewById(R.id.rsbyDetRelLayout);
        mobileLayout=(RelativeLayout)v.findViewById(R.id.mobileRelLayout);
        healthSchemelayout=(RelativeLayout)v.findViewById(R.id.healthSchemeRelLayout);
        nomineeLayout=(RelativeLayout)v.findViewById(R.id.nomineeRelLayout);
        photoLayout=(LinearLayout)v.findViewById(R.id.memberPhotoLayout);
        govtIdLayout=(LinearLayout)v.findViewById(R.id.govtIdLayout) ;
        editMemberDetlayout=(RelativeLayout)v.findViewById(R.id.memberDetRelLayout);
        editMemberDetlayout.setVisibility(View.GONE);
        backIV=(ImageView)v.findViewById(R.id.back);
        updateNhpsBT=(Button)v.findViewById(R.id.updateNhpsBT);
        lockBT=(Button)v.findViewById(R.id.lockBT);
        aadhaarStatLayout=(RelativeLayout)v.findViewById(R.id.aadhaarStatLayout);
        aadhaarStatLayout.findViewById(R.id.aadhVerifiedIV).setVisibility(View.VISIBLE);
        ImageView verified=(ImageView)aadhaarStatLayout.findViewById(R.id.aadhVerifiedIV);
        verified.setVisibility(View.GONE);
        ImageView rejected=(ImageView)aadhaarStatLayout.findViewById(R.id.aadhRejectedIV);
        rejected.setVisibility(View.GONE);
        ImageView pending=(ImageView)aadhaarStatLayout.findViewById(R.id.aadhPendingIV);
        pending.setVisibility(View.GONE);

        zoomView = new ZoomView(this);
        zoomView.addView(v);
        mZoomLinearLayout.addView(zoomView);

        if(rsbyItem!=null) {
            if(rsbyItem.getMemStatus()!=null) {
                if (rsbyItem.getMemStatus().equalsIgnoreCase(AppConstant.MEMBERFOUND_BUT_NOT_PRESENT)) {
                    photoLayout.setVisibility(View.GONE);
                } else {
                    photoLayout.setVisibility(View.VISIBLE);
                }
            }
            if (rsbyItem.getAadhaarAuth() != null) {
                if (rsbyItem.getAadhaarAuth() != null && rsbyItem.getAadhaarAuth().equalsIgnoreCase("Y")) {
                    verified.setVisibility(View.VISIBLE);
                } else if (rsbyItem.getAadhaarAuth() != null && rsbyItem.getAadhaarAuth().equalsIgnoreCase("N")) {
                    rejected.setVisibility(View.VISIBLE);
                } else if (rsbyItem.getAadhaarAuth() != null && rsbyItem.getAadhaarAuth().equalsIgnoreCase("P")) {
                    pending.setVisibility(View.VISIBLE);
                }
            }
            if(rsbyItem.getMemberPhoto1()!=null){
                if(!rsbyItem.getMemberPhoto1().equalsIgnoreCase("")){
                    photoVerifiedIV.setVisibility(View.VISIBLE);
                }
            }
        }
        photoCaptureStatus.setVisibility(View.INVISIBLE);
        govtIdCaptureStatus.setVisibility(View.INVISIBLE);
        aadhaarCaptureStatus.setVisibility(View.INVISIBLE);
        mobileCaptureStatus.setVisibility(View.INVISIBLE);
        nomineeCaptureStatus.setVisibility(View.INVISIBLE);
        additionalCaptureStatus.setVisibility(View.INVISIBLE);
        setMobileStatus();
        setURNStatus();
        setPhotoCaptureStatus();
        prepareAadhaarStatusSpinner();
        setAadhaarStatus();
        setNomineeStatus();


        aadhaarStatusRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                View radioButton = aadhaarStatusRG.findViewById(checkedId);
                int index = aadhaarStatusRG.indexOfChild(radioButton);

                // Add logic here
                aadhaarLayout.setVisibility(View.GONE);
                govtIdLayout.setVisibility(View.GONE);
             /*   rsbyItem.setAadhaarStatus(item.getaStatusCode());
                Log.d(TAG,"Secc Member Status "+rsbyItem.getAadhaarStatus());
                if(item.getaStatusCode().equalsIgnoreCase("1")){
                    aadhaarLayout.setVisibility(View.VISIBLE);
                }else{
                    govtIdLayout.setVisibility(View.VISIBLE);

                }*/
                switch (index) {
                    case 0:
                        Log.d(TAG,"Aadhaar Status Selected");
                        aadhaarStatus="1";
                        rsbyItem.setAadhaarStatus(aadhaarStatus);
                        aadhaarLayout.setVisibility(View.VISIBLE);
                        showMendatoryField();
                        break;
                    case 1:
                        Log.d(TAG,"Aadhaar Status not selected");
                        aadhaarStatus="2";
                        rsbyItem.setAadhaarStatus(aadhaarStatus);
                        govtIdLayout.setVisibility(View.VISIBLE);
                        showMendatoryField();
                        break;
                }

            }
        });

        showMendatoryField();
       /* aadhaarAvailableRB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){

                }

            }
        });*/

        aadhaarLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent theIntent = new Intent(context, RsbyAadharCaptureActivity.class);
                // theIntent.putExtra(AppConstant.MEMBER_TYPE,memberType);
                selectedMemItem.setRsbyMemberItem(rsbyItem);
                ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                        AppConstant.SELECTED_ITEM_FOR_VERIFICATION, selectedMemItem.serialize(), context);
                startActivity(theIntent);
                finish();
                leftTransition();
            }
        });
        govtIdLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent theIntent = new Intent(context, RsbyGovermentIdCaptureActivity.class);
                //theIntent.putExtra(AppConstant.NEVIGATE, AppConstant.WITH_OUT_AADHAAR);
                selectedMemItem.setRsbyMemberItem(rsbyItem);
                System.out.print(rsbyItem.getName());
         //       selectedMemItem.setRsbyMemberItem(rsbyItem);
                ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                        AppConstant.SELECTED_ITEM_FOR_VERIFICATION,selectedMemItem.serialize(), context);
                startActivity(theIntent);
                finish();
                leftTransition();
            }
        });
       /* enrollmentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent theIntent = new Intent(context, EnrollmentActivity.class);
                startActivity(theIntent);
                finish();
                leftTransition();

            }
        });*/
        mobileLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent theIntent = new Intent(context, RsbyMobileNumberCaptureActitivty.class);
                selectedMemItem.setRsbyMemberItem(rsbyItem);
                System.out.print(rsbyItem.getName());
                ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                        AppConstant.SELECTED_ITEM_FOR_VERIFICATION,selectedMemItem.serialize(), context);
                startActivity(theIntent);
                finish();
                leftTransition();
            }
        });

        healthSchemelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent theIntent=new Intent(context,RsbhyHealthSchemeActivity.class);
                selectedMemItem.setRsbyMemberItem(rsbyItem);
                System.out.print(rsbyItem.getName());
                ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                        AppConstant.SELECTED_ITEM_FOR_VERIFICATION,selectedMemItem.serialize(), context);
                startActivity(theIntent);
                finish();
                leftTransition();
            }
        });
        nomineeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent theIntent = new Intent(context, RsbyNomineeCaptureActivity.class);
                ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                        AppConstant.SELECTED_ITEM_FOR_VERIFICATION,selectedMemItem.serialize(), context);
                selectedMemItem.setRsbyMemberItem(rsbyItem);
                System.out.print(rsbyItem.getName());
                startActivity(theIntent);
                finish();
                leftTransition();
            }
        });
        photoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent theIntent = new Intent(context, RsbyPhotoCaptureActivity.class);
                selectedMemItem.setRsbyMemberItem(rsbyItem);
                System.out.print(rsbyItem.getName());
                ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                        AppConstant.SELECTED_ITEM_FOR_VERIFICATION,selectedMemItem.serialize(), context);
                startActivity(theIntent);
                finish();
                leftTransition();

            }
        });

    /*    editMemberDetlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent theIntent = new Intent(context, OtherDetailActivity.class);
                selectedMemItem.setRsbyMemberItem(rsbyItem);
                ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                        AppConstant.SELECTED_ITEM_FOR_VERIFICATION,selectedMemItem.serialize(), context);
                startActivity(theIntent);
                finish();
                leftTransition();
                //  finish();

            }
        });*/
        aadhaarStatusSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                AadhaarStatusItem item=aadhaarStatusList.get(i);
                aadhaarLayout.setVisibility(View.GONE);
                govtIdLayout.setVisibility(View.GONE);
                rsbyItem.setAadhaarStatus(item.getaStatusCode());
                //Log.d(TAG,"Secc Member Status "+rsbyItem.getAadhaarStatus());
                if(item.getaStatusCode().equalsIgnoreCase("1")){
                    aadhaarLayout.setVisibility(View.VISIBLE);
                }else{
                    govtIdLayout.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent theIntent=null;
                rightTransition();
                finish();
            }
        });
        menuLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backIV.performClick();
            }
        });
        updateNhpsBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent theIntent=null;
                if(rsbyItem!=null) {

                    if(rsbyItem.getAadhaarStatus()!=null && rsbyItem.getAadhaarStatus().equalsIgnoreCase("1")){
                      /*  if (rsbyItem.getAadhaarNo()==null || rsbyItem.getAadhaarNo().equalsIgnoreCase("")) {
                            CustomAlert.alertWithOk(context, "Please capture aadhaar detail");
                            return;
                        }*/
                    }
                    if(rsbyItem.getAadhaarStatus()!=null && rsbyItem.getAadhaarStatus().equalsIgnoreCase("2")){
                    /*    if(rsbyItem.getIdNo()==null || (rsbyItem.getIdNo()!=null && rsbyItem.getIdNo().equalsIgnoreCase(""))){
                            CustomAlert.alertWithOk(context,"Please capture government id detail");
                            return;
                        }*/
                    }
                    //commented by saurabh for mobile no. validation bypass
                /*    if (rsbyItem.getMobileNo()==null ){
                        CustomAlert.alertWithOk(context, "Please capture mobile number");
                        return;
                    }else{
                      if(rsbyItem.getMobileNo().equalsIgnoreCase(loginResponse.getMobileNumber())){
                          CustomAlert.alertWithOk(context, "You have entered verifier mobile number, Please enter member mobile number to proceed.");
                          return;
                      }
                    }*/
                    rsbyItem= AppUtility.clearAadhaarOrGovtDetailRSBY(rsbyItem);
                    rsbyItem.setLockedSave(AppConstant.SAVE + "");
                    AppUtility.showLog(AppConstant.LOG_STATUS,TAG," OLD Household name " +
                            ": "+selectedMemItem.getOldHeadrsbyMemberItem().getName()+"" +
                            " Member Status "+selectedMemItem.getOldHeadrsbyMemberItem().getMemStatus()+" House hold Status :" +
                            " "+selectedMemItem.getOldHeadrsbyMemberItem().getHhStatus()+" Locked Save :"+selectedMemItem.getOldHeadrsbyMemberItem().getLockedSave());
                    Log.d(TAG," NHPS Relation : "+rsbyItem.getNhpsRelationCode());
                    if(selectedMemItem.getOldHeadrsbyMemberItem()!=null && selectedMemItem.getNewHeadrsbyMemberItem()!=null){
                        RSBYItem oldHead=selectedMemItem.getOldHeadrsbyMemberItem();
                        oldHead.setLockedSave(AppConstant.LOCKED+"");
                        AppUtility.showLog(AppConstant.LOG_STATUS,TAG," OLD Household name " +
                                ": "+oldHead.getName()+"" +
                                " Member Status "+oldHead.getMemStatus()+" House hold Status :" +
                                " "+oldHead.getHhStatus()+" Locked Save :"+oldHead.getLockedSave());
                        SeccDatabase.updateRsbyMember(selectedMemItem.getOldHeadrsbyMemberItem(),context);
                    }

                    SeccDatabase.updateRsbyMember(rsbyItem,context);
                    RsbyHouseholdItem houseHoldItem=selectedMemItem.getRsbyHouseholdItem();
                    AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"  Household name " +
                            ": "+houseHoldItem.getName()+ "House hold Status :" +
                            " "+houseHoldItem.getHhStatus()+" Locked Save :"+houseHoldItem.getLockedSave());
                    if(SeccDatabase.checkRsbyUnderSurveyMember(houseHoldItem.getUrnId(),context)){
                        houseHoldItem.setLockedSave(AppConstant.SAVE+"");
                    }else{
                        houseHoldItem.setLockedSave(AppConstant.LOCKED+"");
                    }
                    selectedMemItem.setRsbyHouseholdItem(houseHoldItem);
                    Log.d("Preview Activity"," household json : "+houseHoldItem.serialize());
                    SeccDatabase.updateRSBYHouseHold(selectedMemItem.getRsbyHouseholdItem(),context);
                    selectedMemItem.setRsbyMemberItem(rsbyItem);
                    ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                            AppConstant.SELECTED_ITEM_FOR_VERIFICATION, selectedMemItem.serialize(), context);
                    //  theIntent = new Intent(context, SECCFamilyListActivity.class);
                    theIntent = new Intent(context, RsbyMainActivity.class);

                }
                theIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(theIntent);
                finish();
                leftTransition();
            }
        });
        lockBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent theIntent=null;

                if(rsbyItem!=null) {
                    if (rsbyItem.getMemStatus()!=null &&
                            !rsbyItem.getMemStatus().trim().equalsIgnoreCase(AppConstant.MEMBERFOUND_BUT_NOT_PRESENT)) {
                        //  AppUtility.showLog(AppConstant.LOG_STATUS,TAG," Aadhaar Status "+rsbyItem.getAadhaarStatus());
                        if (rsbyItem.getMemberPhoto1() == null || rsbyItem.getMemberPhoto1().equalsIgnoreCase("")) {
                            CustomAlert.alertWithOk(context, "Please capture member photo");
                            return;
                        }
                        // AppUtility.showLog(AppConstant.LOG_STATUS,TAG," Aadhaar Status 6"+rsbyItem.getAadhaarStatus());

                        if(rsbyItem.getAadhaarStatus()!=null && rsbyItem.getAadhaarStatus().trim().equalsIgnoreCase(AppConstant.AADHAAR_STAT)){
                            if (rsbyItem.getAadhaarNo()==null || rsbyItem.getAadhaarNo().equalsIgnoreCase("")) {
                                CustomAlert.alertWithOk(context, "Please capture aadhaar detail");
                                return;
                            }

                        }
                        if(rsbyItem.getAadhaarStatus()!=null && rsbyItem.getAadhaarStatus().equalsIgnoreCase(AppConstant.GOVT_ID_STAT)){
                            if(rsbyItem.getIdNo()==null || (rsbyItem.getIdNo()!=null && rsbyItem.getIdNo().equalsIgnoreCase(""))){
                                CustomAlert.alertWithOk(context,"Please capture government id detail");
                                return;
                            }
                        }
                        if (rsbyItem.getMobileNo()==null || rsbyItem.getMobileNo().equalsIgnoreCase("")) {
                            CustomAlert.alertWithOk(context, "Please capture mobile number");
                            return;
                        }
                        if (rsbyItem.getMobileNo()==null || rsbyItem.getMobileNo().equalsIgnoreCase(loginResponse.getMobileNumber())) {
                            CustomAlert.alertWithOk(context, "You have entered verifier mobile number, Please enter member mobile number to proceed.");
                            return;
                        }
                        //   AppUtility.showLog(AppConstant.LOG_STATUS,TAG," Aadhaar Status 3"+rsbyItem.getAadhaarStatus());

                        if (rsbyItem.getNameNominee()==null || rsbyItem.getNameNominee().equalsIgnoreCase("")) {
                            CustomAlert.alertWithOk(context, "Please capture nominee detail");
                            return;
                        }

                        //  AppUtility.showLog(AppConstant.LOG_STATUS,TAG," Aadhaar Status 2"+rsbyItem.getAadhaarStatus());

                        if(rsbyItem!=null && rsbyItem.getAadhaarStatus()!=null
                                && rsbyItem.getAadhaarStatus().equalsIgnoreCase(AppConstant.AADHAAR_STAT)) {
                            //ArrayList<rsbyItem> checkAadhaarList = SeccDatabase.seccMemberListByAadhaar(rsbyItem.getAadhaarNo().trim(), context);

                            if (rsbyItem.getMobileNo().equalsIgnoreCase(loginResponse.getMobileNumber())) {
                                CustomAlert.alertWithOk(context, "You have entered verifier mobile number, Please enter member mobile number to proceed.");
                                return;
                            }

                            if (AppUtility.isAadhaarDuplicateRSBY(rsbyItem,context)) {
                                alertForValidateLater(getResources().getString(R.string.aadhaar_already_captured),null);
                            }else{
                                rsbyItem.setError_code(null);
                                rsbyItem.setError_type(null);
                                rsbyItem.setError_msg(null);
                                houseHoldItem.setError_code(null);
                                houseHoldItem.setError_msg(null);
                                houseHoldItem.setError_type(null);
                                lockedDetail();
                            }
                        }else if(rsbyItem!=null && rsbyItem.getAadhaarStatus()!=null
                                && rsbyItem.getAadhaarStatus().equalsIgnoreCase(AppConstant.GOVT_ID_STAT)) {
                            rsbyItem.setError_code(null);
                            rsbyItem.setError_type(null);
                            rsbyItem.setError_msg(null);
                            houseHoldItem.setError_code(null);
                            houseHoldItem.setError_msg(null);
                            houseHoldItem.setError_type(null);
                            lockedDetail();
                        }

                        //  finish();
                    }else{
                        rsbyItem.setMemberPhoto1(null);


                        if (rsbyItem.getMobileNo()==null || rsbyItem.getMobileNo().equalsIgnoreCase(loginResponse.getMobileNumber())) {
                            CustomAlert.alertWithOk(context, "You have entered mobile no of validator, Please enter beneficiary mobile no to Proceed.");
                            return;
                        }


                        if(rsbyItem.getAadhaarStatus()!=null && rsbyItem.getAadhaarStatus().trim().equalsIgnoreCase(AppConstant.AADHAAR_STAT)){
                            if (rsbyItem.getAadhaarNo()==null || rsbyItem.getAadhaarNo().equalsIgnoreCase("")) {
                                CustomAlert.alertWithOk(context, "Please capture aadhaar detail");
                                return;
                            }
                            AppUtility.showLog(AppConstant.LOG_STATUS,TAG," Aadhaar Status 7 :"+rsbyItem.getAadhaarNo());

                        }

                        if(rsbyItem!=null && rsbyItem.getAadhaarStatus()!=null
                                && rsbyItem.getAadhaarStatus().equalsIgnoreCase(AppConstant.AADHAAR_STAT)) {
                            //ArrayList<rsbyItem> checkAadhaarList = SeccDatabase.seccMemberListByAadhaar(rsbyItem.getAadhaarNo().trim(), context);

                            if(loginResponse.getAadhaarNumber().equalsIgnoreCase(rsbyItem.getAadhaarNo().toString())){
                                alertForValidateLater(getResources().getString(R.string.validator_adhaar_captured),null);
                            }else if (AppUtility.isAadhaarDuplicateRSBY(rsbyItem,context)) {
                                alertForValidateLater(getResources().getString(R.string.aadhaar_already_captured),null);
                            }else if(rsbyItem.getAadhaarAuth()!=null && !rsbyItem.getAadhaarAuth().equalsIgnoreCase(AppConstant.AADHAAR_AUTH_YES)){
                                alertForValidateLater(getResources().getString(R.string.aadhaar_not_validate),null);
                            }else{
                                rsbyItem.setError_code(null);
                                rsbyItem.setError_type(null);
                                rsbyItem.setError_msg(null);
                                houseHoldItem.setError_code(null);
                                houseHoldItem.setError_msg(null);
                                houseHoldItem.setError_type(null);
                                lockedDetail();
                            }
                        }else if(rsbyItem!=null && rsbyItem.getAadhaarStatus()!=null
                                && rsbyItem.getAadhaarStatus().equalsIgnoreCase(AppConstant.GOVT_ID_STAT)) {
                            lockedDetail();
                        }
                    }
                } else {
                    // theIntent = new Intent(context, RSBYFamilyMemberListActivity.class);
                }
            }
        });
    }
    private void alertForValidateLater(String msg, RSBYItem item){

        internetDiaolg = new AlertDialog.Builder(context).create();
        LayoutInflater factory = LayoutInflater.from(context);
        View alertView = factory.inflate(R.layout.internet_try_again_popup, null);
        internetDiaolg.setView(alertView);
        internetDiaolg.show();
        TextView tryGainMsgTV=(TextView) alertView.findViewById(R.id.deleteMsg);
        tryGainMsgTV.setText(msg);
        Button tryAgainBT=(Button) alertView.findViewById(R.id.tryAgainBT);
        tryAgainBT.setText("Confirm");
        Button cancelBT=(Button)alertView.findViewById(R.id.cancelBT);
        tryAgainBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                internetDiaolg.dismiss();
                lockedDetail();
            }
        });
        cancelBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                internetDiaolg.dismiss();
            }
        });
    }
    private void lockedDetail(){
        Intent theIntent;
        rsbyItem= AppUtility.clearAadhaarOrGovtDetailRSBY(rsbyItem);
        selectedMemItem.setRsbyMemberItem(rsbyItem);
        AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Total Byte size : "+rsbyItem.serialize().getBytes().length);
        ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.SELECTED_ITEM_FOR_VERIFICATION, selectedMemItem.serialize(), context);
        theIntent = new Intent(context, RsbyPreviewScreenActivity.class);
        startActivity(theIntent);
        leftTransition();
    }
    private void prepareAadhaarStatusSpinner(){
        aadhaarStatusList=new ArrayList<>();
        ArrayList<String> spinnerList=new ArrayList<>();
        aadhaarStatusList.add(new AadhaarStatusItem("1","Aadhaar Available"));
        aadhaarStatusList.add(new AadhaarStatusItem("2","Aadhaar not prepared"));
        for(AadhaarStatusItem item : aadhaarStatusList){
            spinnerList.add(item.getaStatusDesc());
        }
        ArrayAdapter<String> maritalAdapter = new ArrayAdapter<>(context, R.layout.custom_drop_down, R.id.textView,spinnerList);
        aadhaarStatusSP.setAdapter(maritalAdapter);
        if(rsbyItem!=null){
            for(int i=0;i<aadhaarStatusList.size();i++){
                if(rsbyItem.getAadhaarStatus()!=null && rsbyItem.getAadhaarStatus()
                        .equalsIgnoreCase(aadhaarStatusList.get(i).getaStatusCode())){
                    selectedAadhaarStatus=i;
                    break;
                }
            }
        }
        aadhaarStatusSP.setSelection(selectedAadhaarStatus);

    }
    private void setScreenData(){
        // prepareMemberStatusSpinner();
    }
    private void setMobileStatus(){
        RelativeLayout mobileStatusLayout=(RelativeLayout)findViewById(R.id.mobileStatusLayout);
        ImageView mobVerified=(ImageView)mobileStatusLayout.findViewById(R.id.mobileVerifiedIV);
        ImageView mobRejected=(ImageView)mobileStatusLayout.findViewById(R.id.mobileRejectedIV);
        ImageView mobPending=(ImageView)mobileStatusLayout.findViewById(R.id.mobilePendingIV);
        mobRejected.setVisibility(View.GONE);
        mobPending.setVisibility(View.GONE);
        mobVerified.setVisibility(View.GONE);
        if(rsbyItem!=null) {
            if (rsbyItem.getMobileNo() != null) {
                if (rsbyItem.getMobileNo() != null && !rsbyItem.getMobileNo().equalsIgnoreCase("")) {
                    if (rsbyItem.getMobileAuth().equalsIgnoreCase("Y")) {
                        mobVerified.setVisibility(View.VISIBLE);
                    } else if (rsbyItem.getMobileAuth().equalsIgnoreCase("N")) {
                        mobRejected.setVisibility(View.VISIBLE);
                    } else if (rsbyItem.getMobileAuth().equalsIgnoreCase("P")) {
                        mobPending.setVisibility(View.VISIBLE);
                    }
                    mobileCaptureStatus.setVisibility(View.VISIBLE);
                }
            }
        }
    }
    private void setURNStatus(){
        ImageView rsbyVerified=(ImageView)findViewById(R.id.rsbyVerifiedIV);
        ImageView rsbyPending=(ImageView)findViewById(R.id.rsbyPendingIV);
        ImageView rsbyRejected=(ImageView)findViewById(R.id.rsbyRejectedIV);
        rsbyVerified.setVisibility(View.GONE);
        rsbyPending.setVisibility(View.GONE);
        rsbyRejected.setVisibility(View.GONE);
        if(rsbyItem.getUrnNo()!=null && !rsbyItem.getUrnNo().equalsIgnoreCase("")){
            additionalCaptureStatus.setVisibility(View.VISIBLE);
        }
        if(rsbyItem.getSchemeId()!=null && !rsbyItem.getSchemeId().equalsIgnoreCase("")){
            additionalCaptureStatus.setVisibility(View.VISIBLE);
        }
       /* if(rsbyItem.getUrnAuth().equalsIgnoreCase("Y")){
            rsbyVerified.setVisibility(View.VISIBLE);
        }else if(rsbyItem.getUrnAuth().equalsIgnoreCase("N")){
            rsbyRejected.setVisibility(View.VISIBLE);
        }else if(rsbyItem.getUrnAuth().equalsIgnoreCase("P")){
            rsbyPending.setVisibility(View.VISIBLE);
        }*/
    }
    private void setPhotoCaptureStatus(){

        if(rsbyItem.getMemberPhoto1()!=null && !rsbyItem.getMemberPhoto1().equalsIgnoreCase("")){
            photoCaptureStatus.setVisibility(View.VISIBLE);
        }
        //  Log.d("With Aadhaar","State code auth : "+rsbyItem.getStateSchemeCodeAuth());
        /*if(rsbyItem.getStateSchemeCodeAuth().equalsIgnoreCase("Y")){
            verified.setVisibility(View.VISIBLE);
        }else if(rsbyItem.getStateSchemeCodeAuth().equalsIgnoreCase("N")){
            rejected.setVisibility(View.VISIBLE);
        }else if(rsbyItem.getStateSchemeCodeAuth().equalsIgnoreCase("P")){
            Log.d("With Aadhaar","State code auth1111 : "+rsbyItem.getStateSchemeCodeAuth());
            pending.setVisibility(View.VISIBLE);
        }*/
    }

    private void setAadhaarStatus(){
        aadhaarStatus=rsbyItem.getAadhaarStatus();
        aadhaarLayout.setVisibility(View.GONE);
        govtIdLayout.setVisibility(View.GONE);
        if(aadhaarStatus!=null && !aadhaarStatus.equalsIgnoreCase("")){
            if(aadhaarStatus.equalsIgnoreCase("1")){
                aadhaarLayout.setVisibility(View.VISIBLE);
                aadhaarAvailableRB.setChecked(true);
            }else if(aadhaarStatus.equalsIgnoreCase("2")){
                govtIdLayout.setVisibility(View.VISIBLE);
                aadhaarNotPrepRB.setChecked(true);
            }else{
                aadhaarLayout.setVisibility(View.VISIBLE);
                aadhaarStatus="1";
                rsbyItem.setAadhaarStatus(aadhaarStatus);
                aadhaarAvailableRB.setChecked(true);
            }
        }else{
            aadhaarLayout.setVisibility(View.VISIBLE);
            aadhaarStatus="1";
            rsbyItem.setAadhaarStatus(aadhaarStatus);
            aadhaarAvailableRB.setChecked(true);
        }
        if(rsbyItem.getAadhaarStatus().equalsIgnoreCase(AppConstant.AADHAAR_STAT)){
            if(rsbyItem.getNameAadhaar()!=null && !rsbyItem.getNameAadhaar().equalsIgnoreCase(""))
                aadhaarCaptureStatus.setVisibility(View.VISIBLE);
        }else if(rsbyItem.getAadhaarStatus().equalsIgnoreCase(AppConstant.GOVT_ID_STAT) ){
            if(rsbyItem.getIdNo()!=null && !rsbyItem.getIdNo().equalsIgnoreCase("")) {
                govtIdCaptureStatus.setVisibility(View.VISIBLE);
            }
        }
    }
    private void setNomineeStatus(){
        if(rsbyItem.getNameNominee()!=null && !rsbyItem.getNameNominee().equalsIgnoreCase("")){
            nomineeCaptureStatus.setVisibility(View.VISIBLE);
        }
    }
    private void showMendatoryField(){
        TextView photoStar=(TextView) findViewById(R.id.capturePhotoStarTV);
        photoStar.setVisibility(View.INVISIBLE);
        TextView govtIdStar=(TextView) findViewById(R.id.govtIdStarTV);
        govtIdStar.setVisibility(View.INVISIBLE);
        TextView aadhaarStar=(TextView)findViewById(R.id.aadhaarStarTV);
        aadhaarStar.setVisibility(View.INVISIBLE);
        TextView mobileNoStar=(TextView)findViewById(R.id.mobileNoStarTV);
        mobileNoStar.setVisibility(View.INVISIBLE);
        TextView nomineeDetStart=(TextView)findViewById(R.id.nomineeDetailStarTV);
        nomineeDetStart.setVisibility(View.INVISIBLE);
        if(rsbyItem!=null && rsbyItem.getMemStatus()!=null &&
                rsbyItem.getMemStatus().equalsIgnoreCase(AppConstant.MEMBERFOUND_AND_PRESENT)){
            if(rsbyItem.getAadhaarStatus().equalsIgnoreCase(AppConstant.AADHAAR_STAT)){
                aadhaarStar.setVisibility(View.VISIBLE);
            }else if(rsbyItem.getAadhaarStatus().equalsIgnoreCase(AppConstant.GOVT_ID_STAT) ){
                govtIdStar.setVisibility(View.VISIBLE);
            }
            photoStar.setVisibility(View.VISIBLE);
            mobileNoStar.setVisibility(View.VISIBLE);
            nomineeDetStart.setVisibility(View.VISIBLE);
        }else if(rsbyItem!=null && rsbyItem.getMemStatus()!=null &&
                rsbyItem.getMemStatus().equalsIgnoreCase(AppConstant.MEMBERFOUND_BUT_NOT_PRESENT)){
            photoStar.setVisibility(View.INVISIBLE);
            mobileNoStar.setVisibility(View.INVISIBLE);
            nomineeDetStart.setVisibility(View.INVISIBLE);
            if(rsbyItem.getAadhaarStatus().equalsIgnoreCase(AppConstant.AADHAAR_STAT)){
                aadhaarStar.setVisibility(View.VISIBLE);

            }else if(rsbyItem.getAadhaarStatus().equalsIgnoreCase(AppConstant.GOVT_ID_STAT) ){
                if(rsbyItem.getIdNo()!=null && !rsbyItem.getIdNo().equalsIgnoreCase("")) {
                }
            }
           /* photoStar.setVisibility(View.VISIBLE);
            mobileNoStar.setVisibility(View.VISIBLE);
            nomineeDetStart.setVisibility(View.VISIBLE);*/
        }
    }
}

