package com.nhpm.rsbyFieldValidation;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.customComponent.utility.DateTimeUtil;
import com.customComponent.utility.ProjectPrefrence;
import com.nhpm.PrintCard.PrintRSBYCard;
import com.nhpm.activity.BaseActivity;
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
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;
import com.nhpm.Utility.AppUtility;

import java.util.ArrayList;

import pl.polidea.view.ZoomView;

/**
 * Created by DELL on 06-03-2017.
 */

public class RsbyMemberPreviewActivity extends BaseActivity {
    private String TAG="PreviewActivity";
    private SelectedMemberItem selectedMember;
    private ImageView memberPhotoIV,govtIdIV;
    private TextView nameTV,fatherNameTV,motherNameTV,relationTV,
            houseHoldStatTV,memberStatusTV,prevHouseholdIDTV;
    private TextView aadhaarNumberTV,nameAsAadhaarTV,prevCaptureMode;//aadhaarStatusTV
    private ImageView aadharStatusIV;
    private TextView govtIDTV,govtIdTypeTV,nameAsIdTV;
    private TextView mobileNoTV,whoseMobileTV,mobileStatusTV;
    private TextView urnNoTV,healthSchemeTV,schemIdTV,hhdTV;
    private TextView reqNameTV,reqFatherNameTV,reqMotherNameTV,
            reqRelationTV,reqDobTV,reqGenderTV,reqMaritalTV,
            reqOccupTV,prevNomineeNameTV,prevNomineeRelTV,syncVersionTV;
    private TextView syncLockDtTV,hofNameTV,memberIdTV,nhpsIdTV,prevValidatedByTV;
    private Button confrimBT,printCardBT;
  //  private SeccMemberItem seccItem;
    private RSBYItem rsbyItem;
    private Context context;
    private TextView headerTV;
    private ArrayList<FamilyStatusItem> householdStatusList;
    private ArrayList<MemberStatusItem> memberStatusList;
    private RelativeLayout backLayout;
    private ImageView backIV;
    private LinearLayout memberPhotoLayout,aadhaarStatusLayout,
            govtIdStatusLayout,mobileNoStatusLayout,addSchemeDetLayout;
    private LinearLayout healthSchemeLayout,urnNoLayout;
    private RelativeLayout healthSchemeheaderLayout;
    private LinearLayout nomineeDetailLayout;
    private TextView addressTV,prevGenderTV,prevDOB,prevNameInRegionalTV;
    private ArrayList<RelationItem> relationList;
    private LinearLayout mZoomLinearLayout;
    private ZoomView zoomView;
    private VerifierLocationItem selectedLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dummy_layout_for_zooming);
        setupScreen();
    }
    private void setupScreen(){
        context=this;
        mZoomLinearLayout = (LinearLayout) findViewById(R.id.mZoomLinearLayout);
        View v = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.activity_preview, null, false);
        v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
        selectedLocation= VerifierLocationItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.SELECTED_BLOCK,context));
        selectedMember= SelectedMemberItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.SELECTED_ITEM_FOR_VERIFICATION, context));
        householdStatusList= SeccDatabase.getFamilyStatusList(context);
        memberStatusList= SeccDatabase.getMemberStatusList(context);
        relationList= SeccDatabase.getRelationList(context);
        hhdTV=(TextView)v.findViewById(R.id.prevHhdTV) ;
        syncLockDtTV=(TextView) v.findViewById(R.id.prevSyncLockDtTV);
        memberIdTV=(TextView)v.findViewById(R.id.prevMemberIdTV);
        printCardBT = (Button) v.findViewById(R.id.printCardBT);
        nhpsIdTV=(TextView)v.findViewById(R.id.prevNhpsIdTV);
        hofNameTV=(TextView)v.findViewById(R.id.prevHofTV);
        prevValidatedByTV=(TextView)v.findViewById(R.id.prevValidatedByTV);
        syncVersionTV=(TextView)v.findViewById(R.id.prevSyncVersionTV);
        prevHouseholdIDTV=(TextView)v.findViewById(R.id.prevHouseholdIDTV);
        memberPhotoLayout=(LinearLayout)v.findViewById(R.id.memberPhotoLayout);
        aadhaarStatusLayout=(LinearLayout)v.findViewById(R.id.adhaarStatusLayout);
        govtIdStatusLayout=(LinearLayout)v.findViewById(R.id.govtIdStatusLayout);
        mobileNoStatusLayout=(LinearLayout)v.findViewById(R.id.mobileNoStatusLayout);
        addSchemeDetLayout=(LinearLayout)v.findViewById(R.id.add_scheme_det_layout);
        healthSchemeLayout=(LinearLayout)v.findViewById(R.id.stateHealthSchemeLayout);
        healthSchemeheaderLayout=(RelativeLayout)v.findViewById(R.id.healthSchemeheaderLayout);
        nomineeDetailLayout=(LinearLayout)v.findViewById(R.id.nomineeDetailLayout);
        urnNoLayout=(LinearLayout)v.findViewById(R.id.urnNumberLayout);
        prevCaptureMode=(TextView)v.findViewById(R.id.prevCaptureMode);
        headerTV=(TextView)v.findViewById(R.id.centertext);
        memberPhotoIV=(ImageView) v.findViewById(R.id.prevMemberPhotoIV);
        govtIdIV=(ImageView)v.findViewById(R.id.idPhotoIV);
        nameTV=(TextView)v.findViewById(R.id.prevNameTV);
        addressTV=(TextView)v.findViewById(R.id.prevAddressTV);
        fatherNameTV=(TextView)v.findViewById(R.id.prevFatherNameTV);
        motherNameTV=(TextView)v.findViewById(R.id.prevMotherNameTV);
        prevDOB=(TextView)v.findViewById(R.id.prevDobTV) ;
        prevGenderTV=(TextView)v.findViewById(R.id.prevGenderTV);
        prevNameInRegionalTV=(TextView)v.findViewById(R.id.prevNameInReginalTV) ;
        relationTV=(TextView)v.findViewById(R.id.prevRelationTV);
        houseHoldStatTV=(TextView)v.findViewById(R.id.prevHouseHoldStatusTV);
        memberStatusTV=(TextView)v.findViewById(R.id.prevMemberStatusTV);
        aadhaarNumberTV=(TextView)v.findViewById(R.id.prevAadhaarNoTV);
        // aadhaarStatusTV=(TextView)v.findViewById(R.id.prevAdhaarStatusTV);
        nameAsAadhaarTV=(TextView)v.findViewById(R.id.prevNameAsAadhaarTV);
        aadharStatusIV = (ImageView) v.findViewById(R.id.aadharStatusIV);
        govtIDTV=(TextView)v.findViewById(R.id.prevGovtIdNoTV);
        nameAsIdTV=(TextView)v.findViewById(R.id.prevGovtNameAsIdTV);

        prevNomineeNameTV=(TextView)v.findViewById(R.id.prevNomineeNameTV) ;
        prevNomineeRelTV=(TextView)v.findViewById(R.id.prevNomineeRelation) ;

        govtIdTypeTV=(TextView)v.findViewById(R.id.prevGovtIdTypeTV);
        mobileNoTV=(TextView)v.findViewById(R.id.prevMobileNoTV);
        mobileStatusTV=(TextView)v.findViewById(R.id.prevMobileStatTV);
        whoseMobileTV=(TextView)v.findViewById(R.id.prevWhoseMobileTV);
        urnNoTV=(TextView)v.findViewById(R.id.prevURNTV);
        //healthSchemeTV=(TextView)v.findViewById(R.id.prevStateHealthTV);
       // schemIdTV=(TextView)v.findViewById(R.id.prevStateHealthIDTV);
        reqNameTV=(TextView)v.findViewById(R.id.reqNameTV);
        reqFatherNameTV=(TextView)v.findViewById(R.id.reqFatherNameTV);
        reqMotherNameTV=(TextView)v.findViewById(R.id.reqMotherNameTV);
        reqRelationTV=(TextView)v.findViewById(R.id.reqRelationTV);
        reqDobTV=(TextView)v.findViewById(R.id.reqDateOfBirthTV);
        reqGenderTV=(TextView)v.findViewById(R.id.reqGenderTV);
        reqMaritalTV=(TextView)v.findViewById(R.id.reqMaritalStatTV);
        reqOccupTV=(TextView)v.findViewById(R.id.reqOccupationTV) ;
        confrimBT=(Button)v.findViewById(R.id.confirmBT);
        confrimBT.setVisibility(View.GONE);
        backLayout=(RelativeLayout)v.findViewById(R.id.backLayout);
        backIV=(ImageView)v.findViewById(R.id.back);
        relationList= SeccDatabase.getRelationList(context);
        zoomView = new ZoomView(this);
        zoomView.addView(v);
        mZoomLinearLayout.addView(zoomView);

        if(selectedMember.getRsbyMemberItem()!=null){
            rsbyItem=selectedMember.getRsbyMemberItem();
            System.out.print(rsbyItem);
            if(rsbyItem.getName()!=null)
                headerTV.setText(rsbyItem.getName());

            previewVerfication();


         /*   for(int i = 0; i< AppUtility.getMaritalStatusCode().size(); i++){
                if(rsbyItem.getReqMarritalStatCode()!=null && rsbyItem.getReqMarritalStatCode().equalsIgnoreCase(AppUtility.getMaritalStatusCode().get(i))){
                    reqMaritalTV.setText(AppUtility.getMaritalStatusLabel().get(i));
                    break;
                }
            }*/


           /* for(RelationItem item : relationList){
                if(rsbyItem.getReqGenderCode()!=null && rsbyItem.getReqRelationCode().equalsIgnoreCase(item.getRelationCode())){
                    reqRelationTV.setText(item.getRelationName());
                    break;
                }
            }*/

        }else if(selectedMember.getRsbyMemberItem()!=null){
            rsbyItem=selectedMember.getRsbyMemberItem();
        }
        confrimBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent theIntent=null;
                if(rsbyItem!=null) {
                    if(selectedMember.getOldHeadrsbyMemberItem()!=null){
                        RSBYItem oldHead=selectedMember.getOldHeadrsbyMemberItem();
                        oldHead.setLockedSave(AppConstant.LOCKED+"");
                        AppUtility.showLog(AppConstant.LOG_STATUS,TAG," OLD Household name " +
                                ": "+oldHead.getName()+"" +
                                " Member Status "+oldHead.getMemStatus()+" House hold Status :" +
                                " "+oldHead.getHhStatus()+" Locked Save :"+oldHead.getLockedSave());
                        SeccDatabase.updateRsbyMember(selectedMember.getOldHeadrsbyMemberItem(),context);
                    }

                    rsbyItem.setLockedSave(AppConstant.LOCKED + "");
                    SeccDatabase.updateRsbyMember(rsbyItem,context);
                    RsbyHouseholdItem houseHoldItem=selectedMember.getRsbyHouseholdItem();

                    if(SeccDatabase.checkRsbyUnderSurveyMember(houseHoldItem.getUrnId(),context)){
                        houseHoldItem.setLockedSave(AppConstant.SAVE+"");
                    }else{
                        houseHoldItem.setLockedSave(AppConstant.LOCKED+"");
                    }

                    Log.d("Preview Activity"," household json : "+houseHoldItem.serialize());
                    selectedMember.setRsbyHouseholdItem(houseHoldItem);
                    SeccDatabase.updateRSBYHouseHold(selectedMember.getRsbyHouseholdItem(),context);
                    rsbyItem= SeccDatabase.getRsbyMemberDetail(rsbyItem.getRsbyMemId(),context);
                    selectedMember.setRsbyMemberItem(rsbyItem);
                    ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                            AppConstant.SELECTED_ITEM_FOR_VERIFICATION,selectedMember.serialize(),context);
                    // SeccDatabase.getSeccMemberDetail()
                    theIntent = new Intent(context, RsbyMainActivity.class);
                }
                theIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(theIntent);
                finish();
                leftTransition();
            }
        });
        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent theIntent;
                // if (rsbyItem != null && rsbyItem.getAadhaarStatus().equalsIgnoreCase("1")) {
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
    private void previewVerfication(){
        boolean aadhaarflag=false,govtIdFlag=false;
        if(rsbyItem.getHhdNo()!=null){
            hhdTV.setText(rsbyItem.getHhdNo());
        }
        if(rsbyItem.getUrnId()!=null){
            prevHouseholdIDTV.setText(rsbyItem.getUrnId());
        }
        if(rsbyItem.getName()!=null)
            nameTV.setText(rsbyItem.getName());
     /*   if(rsbyItem.getFathername()!=null)
            fatherNameTV.setText(rsbyItem.getFathername());
        if(rsbyItem.getMothername()!=null)
            motherNameTV.setText(rsbyItem.getMothername());
        if(rsbyItem.getNameSl()!=null)
            prevNameInRegionalTV.setText(rsbyItem.getNameSl());*/
        if(rsbyItem.getDob()!=null)
            prevDOB.setText(AppUtility.convertRsbyDate(rsbyItem.getDob()));

        if(rsbyItem.getGender()!=null){
            if(rsbyItem.getGender().equalsIgnoreCase("1")){
                prevGenderTV.setText("Male");
            }else if(rsbyItem.getGender().equalsIgnoreCase("2")){
                prevGenderTV.setText("Female");
            }else if(rsbyItem.getGender().equalsIgnoreCase("3")){
                prevGenderTV.setText("Other");
            }
        }

        RSBYItem hofItem= AppUtility.findRsbyHof(rsbyItem,context);
        if(hofItem!=null){
            hofNameTV.setText(hofItem.getName());
        }

        if(rsbyItem.getSyncDt()!=null && !rsbyItem.getSyncDt().trim().equalsIgnoreCase("")){
            String dateTime= DateTimeUtil.convertTimeMillisIntoStringDate(Long.parseLong(rsbyItem.getSyncDt()), AppConstant.SYNC_DATE_TIME);
            if(dateTime!=null) {
                syncLockDtTV.setText(dateTime.trim());
            }
        }
        if(rsbyItem.getNhpsRelationCode()!=null) {
            if (relationList != null) {
                AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Relation : "+rsbyItem.getNhpsRelationCode());
                for (RelationItem item : relationList) {
                    if (item.getRelationCode().trim().equalsIgnoreCase(rsbyItem.getNhpsRelationCode().trim())) {
                        AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Relation : "+rsbyItem.getNhpsRelationCode());
                        relationTV.setText(rsbyItem.getNomineeRelationName());
                        break;
                    }
                }
            }
        }

      /*  String address="";
        if(rsbyItem.getAddressline1()!=null && !rsbyItem.getAddressline1().trim().equalsIgnoreCase("-") && !rsbyItem.getAddressline1().equalsIgnoreCase("")){
            address=address+rsbyItem.getAddressline1()+", ";
            // AppUtility.showLog(AppConstant.LOG_STATUS,TAG," Address3 : "+rsbyItem.getAddressline1());

        }
        if(rsbyItem.getAddressline2()!=null && !rsbyItem.getAddressline2().trim().equalsIgnoreCase("-") && !rsbyItem.getAddressline2().equalsIgnoreCase("") ){
            //AppUtility.showLog(AppConstant.LOG_STATUS,TAG," Address : "+rsbyItem.getAddressline2());
            address=address+rsbyItem.getAddressline2()+", ";
        }
        if(rsbyItem.getAddressline3()!=null && !rsbyItem.getAddressline3().trim().equalsIgnoreCase("-") && !rsbyItem.getAddressline3().equalsIgnoreCase("") ){
            //AppUtility.showLog(AppConstant.LOG_STATUS,TAG," Address1 : "+rsbyItem.getAddressline3());

            address=address+rsbyItem.getAddressline3()+", ";
        }
        if(rsbyItem.getAddressline4()!=null && !rsbyItem.getAddressline4().trim().equalsIgnoreCase("-") && !rsbyItem.getAddressline1().equalsIgnoreCase("") ){
            // AppUtility.showLog(AppConstant.LOG_STATUS,TAG," Address2 : "+rsbyItem.getAddressline4());

            address=address+rsbyItem.getAddressline4();
        }
        addressTV.setText(address);*/
        memberPhotoLayout.setVisibility(View.GONE);

        if(rsbyItem.getMemberPhoto1()!=null&& !rsbyItem.getMemberPhoto1().equalsIgnoreCase("-")) {
            memberPhotoLayout.setVisibility(View.VISIBLE);
            try {
                memberPhotoIV.setImageBitmap(AppUtility.convertStringToBitmap(rsbyItem.getMemberPhoto1()));
                memberPhotoIV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ShowImageInPopUp(AppUtility.convertStringToBitmap(rsbyItem.getMemberPhoto1()));
                    }
                });
            } catch (Exception e) {
            }
        }else{
        }
        if(rsbyItem.getSyncedStatus()!=null && rsbyItem.getSyncedStatus().trim().equalsIgnoreCase(AppConstant.SYNCED_STATUS)){
            if(rsbyItem.getNhpsMemId()!=null){
                memberIdTV.setText(rsbyItem.getNhpsMemId());
                nhpsIdTV.setText(rsbyItem.getNhpsId());
            }

        }

        if(rsbyItem.getAppVersion()!=null){
            syncVersionTV.setText(rsbyItem.getAppVersion());
        }

        if(rsbyItem.getAadhaarVerifiedBy()!=null && !rsbyItem.getAadhaarVerifiedBy().equalsIgnoreCase("")){
            String aadhaarNo="XXXXXXXX"+rsbyItem.getAadhaarVerifiedBy().trim().substring(8);
            prevValidatedByTV.setText(aadhaarNo);
        }

        aadhaarStatusLayout.setVisibility(View.GONE);
        govtIdStatusLayout.setVisibility(View.GONE);
        if(rsbyItem.getAadhaarStatus()!=null && rsbyItem.getAadhaarStatus().equalsIgnoreCase("1")){
            aadhaarflag=true;
            govtIdFlag=false;

            if(rsbyItem.getAadhaarNo()!=null && !rsbyItem.getAadhaarNo().equalsIgnoreCase("")) {
                //    Log.d("Preview ","Aadhaar Number : "+rsbyItem.getAadhaarNo());
                aadhaarStatusLayout.setVisibility(View.VISIBLE);
                String aadhaarNo="XXXXXXXX"+rsbyItem.getAadhaarNo().substring(8);
                aadhaarNumberTV.setText(aadhaarNo);
                if (rsbyItem.getAadhaarAuth() != null)
                    //aadhaarStatusTV.setText(rsbyItem.getAadhaarAuth());
                    if(rsbyItem.getAadhaarAuth().equalsIgnoreCase(AppConstant.AADHAAR_AUTH_YES)){
                        aadharStatusIV.setVisibility(View.VISIBLE);
                        aadharStatusIV.setImageDrawable(context.getResources().getDrawable(R.drawable.right_tick));
                    }else {
                        aadharStatusIV.setVisibility(View.VISIBLE);
                        aadharStatusIV.setImageDrawable(context.getResources().getDrawable(R.drawable.exclamation));
                    }/*else if(rsbyItem.getAadhaarAuth().equalsIgnoreCase()){
                    aadharStatusIV.setImageDrawable(context.getResources().getDrawable(R.drawable.exclamation));
                }*/
                if (rsbyItem.getNameAadhaar() != null)
                    nameAsAadhaarTV.setText(rsbyItem.getNameAadhaar());

                if(rsbyItem.getAadhaarCapturingMode()!=null && rsbyItem.getAadhaarCapturingMode().trim().equalsIgnoreCase(AppConstant.QR_CODE_MODE)){
                    //prevCaptureMode.setText(rsbyItem.getAadhaarCapturingMode());
                    prevCaptureMode.setText("QR");
                }else if(rsbyItem.getAadhaarCapturingMode()!=null && rsbyItem.getAadhaarCapturingMode().trim().equalsIgnoreCase(AppConstant.MANUAL_MODE)) {
                    prevCaptureMode.setText(rsbyItem.getAadhaarCapturingMode());
                }
            }
        }else{
            //Log.d("Preview","Govt Id : "+rsbyItem.getIdNo());

            govtIdIV.setVisibility(View.VISIBLE);
            //Log.d("Preview","Govt Id photo : "+rsbyItem.getGovtIdPhoto());
            if(rsbyItem.getIdType()!=null && !rsbyItem.getIdNo().equalsIgnoreCase("")) {
                aadhaarflag=false;
                govtIdFlag=true;
                govtIdStatusLayout.setVisibility(View.VISIBLE);
                for(GovernmentIdItem item : AppUtility.prepareGovernmentIdSpinner()) {
                    if(rsbyItem.getIdType().trim().equalsIgnoreCase(item.statusCode+"")) {
                        govtIdTypeTV.setText(item.status);
                        break;
                    }
                }
                if(rsbyItem.getIdNo()!=null)
                    govtIDTV.setText(rsbyItem.getIdNo());
                if(rsbyItem.getNameAsId()!=null){
                    nameAsIdTV.setText(rsbyItem.getNameAsId());
                }
            }
            if(rsbyItem.getGovtIdPhoto()!=null && !rsbyItem.getGovtIdPhoto().equalsIgnoreCase("enrollement")) {
                govtIdIV.setVisibility(View.VISIBLE);
                try {
                    govtIdIV.setImageBitmap(AppUtility.convertStringToBitmap(rsbyItem.getGovtIdPhoto()));
                    govtIdIV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ShowImageInPopUp(AppUtility.convertStringToBitmap(rsbyItem.getGovtIdPhoto()));
                        }
                    });
                }catch(Exception e){

                }
            }else{

            }

        }

        ////
        // showing print card button
        //
        if(rsbyItem.getSyncedStatus()!=null){
            if(rsbyItem.getSyncedStatus().equalsIgnoreCase(AppConstant.SYNCED_STATUS)){
            if(rsbyItem.getRelcode().equalsIgnoreCase("01")) {
                printCardBT.setVisibility(View.VISIBLE);
                printCardBT.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //  if (rsbyItem.getName() != null && !rsbyItem.getName().equalsIgnoreCase("")) {
                        //    if (rsbyItem.getMemberPhoto1() != null && !rsbyItem.getMemberPhoto1().equalsIgnoreCase("")) {
                        //      if (rsbyItem.getFathername() != null && !rsbyItem.getFathername().equalsIgnoreCase("")) {
                        //        if (rsbyItem.getDob() != null && !rsbyItem.getDob().equalsIgnoreCase("")) {
                        //          if (rsbyItem.getGenderid() != null && !rsbyItem.getGenderid().equalsIgnoreCase("")) {
                        Intent theIntent = new Intent(context, PrintRSBYCard.class);
                        theIntent.putExtra(AppConstant.sendingPrintData, rsbyItem);
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
        }

        mobileNoStatusLayout.setVisibility(View.GONE);
        if(rsbyItem.getMobileNo()!=null && !rsbyItem.getMobileNo().equalsIgnoreCase("")) {
            mobileNoStatusLayout.setVisibility(View.VISIBLE);
            String mobileNo="XXXXXX"+rsbyItem.getMobileNo().substring(6);
            mobileNoTV.setText(mobileNo);
            if(rsbyItem.getMobileAuth()!=null && rsbyItem.getMobileAuth().trim().equalsIgnoreCase(AppConstant.VALID_STATUS)){
                mobileStatusTV.setText("Verifed");
            }else{
                mobileStatusTV.setText("Not Verified");

            }
            for (WhoseMobileItem item : AppUtility.prepareWhoseMobile()) {
                if (rsbyItem.getWhoseMobile().equalsIgnoreCase(item.getStatusCode())) {
                    whoseMobileTV.setText(item.getStatusType());
                    break;
                }
            }
        }else{
        }
       /* urnNoLayout.setVisibility(View.GONE);
        healthSchemeLayout.setVisibility(View.GONE);*/
        healthSchemeheaderLayout.setVisibility(View.GONE);
        if(rsbyItem.getUrnNo()!=null && !rsbyItem.getUrnNo().equalsIgnoreCase("")) {
            healthSchemeheaderLayout.setVisibility(View.VISIBLE);
            // urnNoLayout.setVisibility(View.VISIBLE);
            urnNoTV.setText(rsbyItem.getUrnNo());
        }else{

        }

        if(rsbyItem.getSchemeId()!=null && !rsbyItem.getSchemeId().equalsIgnoreCase("")) {
            // healthSchemeLayout.setVisibility(View.VISIBLE);
            healthSchemeheaderLayout.setVisibility(View.VISIBLE);
            for(HealthSchemeItem item : SeccDatabase.getHealthSchemeList(context,selectedLocation.getStateCode().trim())) {
                if(rsbyItem.getSchemeId()!=null && rsbyItem.getSchemeId().equalsIgnoreCase(item.getSchemeId())) {
                    healthSchemeTV.setText(item.getSchemeName());
                    schemIdTV.setText(rsbyItem.getSchemeNo());
                }
            }
        }else{
        }
        nomineeDetailLayout.setVisibility(View.GONE);
        if(rsbyItem.getRelationNomineeCode()!=null && !rsbyItem.getRelationNomineeCode().equalsIgnoreCase("")){
            nomineeDetailLayout.setVisibility(View.VISIBLE);
            /*if(rsbyItem.getNameNominee()!=null)
              *//* prevNomineeNameTV.setText(rsbyItem.getNameNominee());*//*
                prevNomineeNameTV.setText(getNomineeName(rsbyItem.getHhdNo(),rsbyItem.getNameNominee()));
                if(rsbyItem.getNomineeRelationName()!=null){
                prevNomineeRelTV.setText(rsbyItem.getNomineeRelationName());
            }*/
            if(rsbyItem.getRelationNomineeCode()!=null && !rsbyItem.getRelationNomineeCode().equalsIgnoreCase("")){
                nomineeDetailLayout.setVisibility(View.VISIBLE);
                if(rsbyItem.getNameNominee()!=null)
                    prevNomineeNameTV.setText(getNomineeName(rsbyItem.getUrnId(),rsbyItem.getNameNominee()));

                prevNomineeRelTV.setText("-");
                if(rsbyItem.getRelationNomineeCode()!=null){
                    for(RelationItem item : relationList) {
                        if(item.getRelationCode().equalsIgnoreCase(rsbyItem.getRelationNomineeCode().trim())) {
                            //prevNomineeRelTV.setText(item.getRelationName()+"("+rsbyItem.getNomineeRelationName()+")");
                            if(item.getRelationName().equalsIgnoreCase(rsbyItem.getNomineeRelationName())){
                                prevNomineeRelTV.setText(item.getRelationName());
                            }else {
                                prevNomineeRelTV.setText(item.getRelationName() + "(" + rsbyItem.getNomineeRelationName() + ")");
                            }
                            break;
                        }
                    }
                }
            }
        }

        for(FamilyStatusItem famStatItem: householdStatusList){
            if(famStatItem.getStatusCode().equalsIgnoreCase(rsbyItem.getHhStatus())){
                houseHoldStatTV.setText(famStatItem.getStatusDesc());
                break;
            }
        }

        for(MemberStatusItem memStatItem : memberStatusList){
            if(rsbyItem.getMemStatus().equalsIgnoreCase(memStatItem.getStatusCode())){
                if(govtIdFlag) {
                    memberStatusTV.setText(memStatItem.getStatusDesc() + " " + getString(R.string.without_aadhaar));
                }else if(aadhaarflag){
                    memberStatusTV.setText(memStatItem.getStatusDesc() + " " + getString(R.string.with_aadhaar1));
                }else{
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

    private String getNomineeName(String HhdNo,String nomineeName){
        String modifiedNomineeDropdownCaption = nomineeName;
        ArrayList<RSBYItem> nomineeList= SeccDatabase.getRsbyMemberList(HhdNo,context);
        for(RSBYItem item : nomineeList) {
            if(item.getName()!=null && !item.getName().equalsIgnoreCase("")){
                if(item.getName().equalsIgnoreCase(nomineeName)){
                    if (item.getMemStatus() != null && item.getMemStatus().trim().equalsIgnoreCase("") || item.getMemStatus().trim().equalsIgnoreCase(AppConstant.MEMBERFOUND_BUT_NOT_PRESENT) || item.getMemStatus().trim().equalsIgnoreCase(AppConstant.MEMBERFOUND_AND_PRESENT) || item.getMemStatus() == null) {
                        if (!item.getName().equalsIgnoreCase("")) {
                            String yearOfBirth = null;
                            String gender;

                            if (item.getDob() != null && !item.getDob().equalsIgnoreCase("") && !item.getDob().equalsIgnoreCase("-")) {
                                yearOfBirth = item.getDob().substring(0, 4);
                            } /*else if (item.getDobFrmNpr() != null && !item.getDobFrmNpr().equalsIgnoreCase("") && !item.getDobFrmNpr().equalsIgnoreCase("-")) {
                                yearOfBirth = item.getDob().substring(0, 4);
                            }*/
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

        }




        return  modifiedNomineeDropdownCaption;
    }
}
