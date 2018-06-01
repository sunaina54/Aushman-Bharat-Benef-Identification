package com.nhpm.rsbyFieldValidation;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.nhpm.Models.response.master.MemberStatusItem;
import com.nhpm.Models.response.rsbyMembers.RSBYItem;
import com.nhpm.Models.response.rsbyMembers.RsbyHouseholdItem;
import com.nhpm.Models.response.seccMembers.SelectedMemberItem;
import com.nhpm.Models.response.verifier.VerifierLoginResponse;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;
import com.nhpm.Utility.AppUtility;
import com.nhpm.activity.BaseActivity;
import com.nhpm.activity.WithAadhaarActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import pl.polidea.view.ZoomView;

/**
 * Created by DELL on 26-02-2017.
 */

public class RsbyMemberDetailActivity  extends BaseActivity {
    public static int SECC_MEMBER_DETAIL=2;
    private TextView headerTV;
    private Button verifyBT;
    private Context context;
    private ArrayList<MemberStatusItem> memberStatusList;
    private Spinner memberStatusSP;
    private LinearLayout memberStatusLayout;
    private TextView verifyWithAadhaarBT,verifyWithoutAadhaarBT;
    private RSBYItem rsbyItem;
    private VerifierLoginResponse loginDetail;
    private RsbyHouseholdItem houseHoldItem;
    private TextView houseHoldNoTV,nameTV,relationTV,fatherNameTV,motherNameTV,occupatTV,
            dobTV,genderTV,maritalStatTV,nameAsRegionalTV,hofNameTV;
    private ImageView backIV;
    private RelativeLayout withAadhaarLayout,withoutAadharLayout;
    private LinearLayout updateLayout;
    private SelectedMemberItem selectedMemItem;
    private int selectedMemberStatus;
    private Button updateBT,lockBT;
    private RelativeLayout withAadharIV,withoutAadhaarIV;
    private TextView newRelationTV,centertext;
    private ArrayList<RelationItem> relationList;
    private Spinner newRelSP;
    private RelationItem newRelItem;
    private RelativeLayout newRelationLayout;
    private AlertDialog dialog,askForPinDailog;
    private LinearLayout mZoomLinearLayout;
    private ZoomView zoomView;
    private RelativeLayout menuLayout;


    private int wrongPinCount = 0;
    private long wrongPinSavedTime;
    private long currentTime;
    private TextView wrongAttempetCountValue,wrongAttempetCountText;
    private long millisecond24 = 86400000;

    private String TAG="Secc Member activity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dummy_layout_for_zooming);
        setupScreen();
    }
    private void setupScreen(){
        context=this;
        mZoomLinearLayout = (LinearLayout) findViewById(R.id.mZoomLinearLayout);
        View v = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.activity_secc_member_detail, null, false);
        v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
        centertext = (TextView) v.findViewById(R.id.centertext);
        //  rsbyItem=(SeccMemberItem)getIntent().getSerializableExtra(AppConstant.SELECTED_MEMBER);
        /*rsbyItem=SeccMemberItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.SELECTED_ITEM_FOR_VERIFICATION, context));*/
        selectedMemItem= SelectedMemberItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.SELECTED_ITEM_FOR_VERIFICATION, context));
        loginDetail= VerifierLoginResponse.create(ProjectPrefrence.getSharedPrefrenceData(
                AppConstant.PROJECT_PREF, AppConstant.VERIFIER_CONTENT,context));
        relationList= SeccDatabase.getRelationList(context);
        if(selectedMemItem.getRsbyMemberItem()!=null){
            rsbyItem=selectedMemItem.getRsbyMemberItem();
            rsbyItem.setAadhaarVerifiedBy(loginDetail.getAadhaarNumber());
            houseHoldItem=selectedMemItem.getRsbyHouseholdItem();
            //  AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Relation Code1 : "+selectedMemItem.getOldHeadMember().getNhpsRelationCode());
            String statusCode = rsbyItem.getMemStatus();
            String statusCode1 = rsbyItem.getHhStatus();
            System.out.print(statusCode+"\n"+statusCode1);
            if(houseHoldItem.getUrnId()!=null) {
                centertext.setText(houseHoldItem.getUrnId());

            }

        }
        headerTV=(TextView)v.findViewById(R.id.centertext);
        newRelationLayout=(RelativeLayout) v.findViewById(R.id.newRelationLayout) ;
        newRelationLayout.setVisibility(View.GONE);
        if(selectedMemItem.getNewHeadrsbyMemberItem()!=null){
            //newRelationLayout.setVisibility(View.VISIBLE);
//NEW HEAD FLOW
            if(rsbyItem.getNhpsRelationCode()==null || !rsbyItem.getNhpsRelationCode().equalsIgnoreCase(AppConstant.NEW_HEAD_RELATION_CODE)){
                newRelationLayout.setVisibility(View.VISIBLE);
            }
        }else{
//OLD HEAD FLOW
            AppUtility.showLog(AppConstant.LOG_STATUS,TAG," OLD HEAD FLOW..");
            if(rsbyItem.getNhpsRelationCode()==null || !rsbyItem.getNhpsRelationCode().equalsIgnoreCase(AppConstant.NEW_HEAD_RELATION_CODE)){
                newRelationLayout.setVisibility(View.VISIBLE);
                AppUtility.showLog(AppConstant.LOG_STATUS,TAG," OLD HEAD FLOW..");
            }
        }

        newRelSP=(Spinner) v.findViewById(R.id.newRelSP) ;
        newRelationTV=(TextView)v.findViewById(R.id.newRelationTV) ;
        memberStatusSP=(Spinner)v.findViewById(R.id.memberStatusSP);
        memberStatusLayout=(LinearLayout)v.findViewById(R.id.verifyLayout);
        houseHoldNoTV=(TextView)v.findViewById(R.id.hhldTV);
        nameAsRegionalTV=(TextView)v.findViewById(R.id.regionalNameTV) ;
        nameTV=(TextView)v.findViewById(R.id.nameTV);
        relationTV=(TextView)v.findViewById(R.id.relTV);
        fatherNameTV=(TextView)v.findViewById(R.id.fatherNameTV);
        motherNameTV=(TextView)v.findViewById(R.id.motherNameTV);
        hofNameTV=(TextView)v.findViewById(R.id.hofNameTV);
        occupatTV=(TextView)v.findViewById(R.id.occupTV);
        dobTV=(TextView)v.findViewById(R.id.dob);
        genderTV=(TextView)v.findViewById(R.id.gender);
        maritalStatTV=(TextView)v.findViewById(R.id.marital);
        newRelationTV=(TextView)v.findViewById(R.id.newRelationTV);
        backIV=(ImageView)v.findViewById(R.id.back);
        menuLayout=(RelativeLayout)v.findViewById(R.id.menuLayout) ;
        withAadhaarLayout=(RelativeLayout)v.findViewById(R.id.withAadharLayout);
        withoutAadharLayout=(RelativeLayout)v.findViewById(R.id.withoutAadharLayout);
        withAadharIV=(RelativeLayout)v.findViewById(R.id.withAadhaarIV);
        withoutAadhaarIV=(RelativeLayout)v.findViewById(R.id.withoutAadhaarIV);
        updateLayout=(LinearLayout)v.findViewById(R.id.updateMemberLayout);

        updateBT=(Button)v.findViewById(R.id.updateBT);
        lockBT=(Button)v.findViewById(R.id.lockBT);
        verifyWithAadhaarBT=(TextView)v.findViewById(R.id.verifyWithAadhaarTV);
        verifyWithoutAadhaarBT=(TextView)v.findViewById(R.id.verifyWithOutAadhaarTV);

        zoomView = new ZoomView(this);
        zoomView.addView(v);
        mZoomLinearLayout.addView(zoomView);
        houseHoldNoTV.setText(rsbyItem.getUrnId());
        nameTV.setText(rsbyItem.getName());
        if(rsbyItem!=null) {
            if (rsbyItem.getDob() != null) {
                dobTV.setText(AppUtility.convertRsbyDate(rsbyItem.getDob().trim()));
            }
        }
       /* if(rsbyItem.get()!=null)
            nameAsRegionalTV.setText(rsbyItem.getNameSl());
        relationTV.setText(rsbyItem.getRelation());
        fatherNameTV.setText(rsbyItem.getFathername());
        motherNameTV.setText(rsbyItem.getMothername());
        if(rsbyItem.getDob()!=null && !rsbyItem.getDob().equalsIgnoreCase("")) {
            dobTV.setText(rsbyItem.getDob().trim());
        }
        else {
            if(rsbyItem.getDobFrmNpr()!=null) {
                dobTV.setText(rsbyItem.getDobFrmNpr().trim());
            }
        }*/
        if(rsbyItem.getNhpsRelationCode()!=null){
            ArrayList<RelationItem> relationItems= SeccDatabase.getRelationList(context);
            for(RelationItem item : relationItems){
                if(item.getRelationCode().equalsIgnoreCase(rsbyItem.getNhpsRelationCode())){
                    if(item.getRelationCode().equalsIgnoreCase(AppConstant.NEW_HEAD_RELATION_CODE)){
                        newRelationTV.setText("Head");
                    }else{
                        newRelationTV.setText(item.getRelationName());
                    }
                    break;
                }
            }
        }

        if(rsbyItem.getGender().equalsIgnoreCase(AppConstant.MALE_GENDER)){
            genderTV.setText("Male");
        }else if(rsbyItem.getGender().equalsIgnoreCase(AppConstant.FEMALE_GENDER)){
            genderTV.setText("Female");
        }else{
            genderTV.setText("Other");
        }
AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Hof Code : "+rsbyItem.getNhpsRelationCode());
        if(rsbyItem.getNhpsRelationCode()!=null && !rsbyItem.getNhpsRelationCode().trim().equalsIgnoreCase("")){
            if(rsbyItem.getNhpsRelationCode().trim().equalsIgnoreCase(AppConstant.NEW_HEAD_RELATION_CODE)){
                hofNameTV.setText(rsbyItem.getName());
            }else{
                RSBYItem item= AppUtility.findRsbyHof(rsbyItem,context);
                if(item!=null){
                    hofNameTV.setText(item.getName());
                }
            }
        }else{
            RSBYItem item= AppUtility.findRsbyHof(rsbyItem,context);
            if(item!=null){
                hofNameTV.setText(item.getName());
            }
        }
        withAadharIV.setVisibility(View.GONE);
        withoutAadhaarIV.setVisibility(View.GONE);
        if(rsbyItem.getAadhaarStatus()!=null && rsbyItem.getAadhaarStatus().equalsIgnoreCase("1")){
            withAadharIV.setVisibility(View.GONE);
        }else if(rsbyItem.getAadhaarStatus()!=null && rsbyItem.getAadhaarStatus().equalsIgnoreCase("2")){
            withoutAadhaarIV.setVisibility(View.GONE);
        }
        headerTV.setText("Member Detail");
        prepareNomineeRelation();
        newRelSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0){
                    newRelationTV.setText("-");
                    newRelItem=null;
                    rsbyItem.setNhpsRelationCode(null);
                }else{
                    newRelItem=relationList.get(i);
                    newRelationTV.setText(newRelItem.getRelationName());
                    rsbyItem.setNhpsRelationCode(newRelItem.getRelationCode());
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        updateBT.setTextColor(context.getResources().getColor(R.color.white));
        updateBT.setText("Save");
        updateBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent theIntent=null;
                if(rsbyItem!=null) {
                    if(rsbyItem.getNhpsRelationCode()!=null && rsbyItem.getNhpsRelationCode().equalsIgnoreCase("01")){
                        if(!rsbyItem.getMemStatus().equalsIgnoreCase(AppConstant.MEMBERFOUND_BUT_NOT_PRESENT) ||
                                !rsbyItem.getMemStatus().equalsIgnoreCase(AppConstant.MEMBERFOUND_BUT_NOT_PRESENT)) {
                            CustomAlert.alertWithOk(context, "Head of the family member should not be died or migrated");
                            return;
                        }
                    }
                    if(rsbyItem.getRelcode()!=null && rsbyItem.getRelcode().equalsIgnoreCase("HEAD")){

                        if(!rsbyItem.getMemStatus().equalsIgnoreCase(AppConstant.MEMBERFOUND_BUT_NOT_PRESENT) ||
                                !rsbyItem.getMemStatus().equalsIgnoreCase(AppConstant.MEMBERFOUND_BUT_NOT_PRESENT)) {
                            CustomAlert.alertWithOk(context, "Head of the family member should not be died or migrated");
                            return;
                        }
                    }
                    rsbyItem.setLockedSave(AppConstant.SAVE + "");
                    SeccDatabase.updateRsbyMember(rsbyItem,context);
                   /* selectedMemItem.setSeccMemberItem(seccMemberItem);
                    ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                            AppConstant.SELECTED_ITEM_FOR_VERIFICATION, selectedMemItem.serialize(), context);*/

                    RsbyHouseholdItem houseHoldItem=selectedMemItem.getRsbyHouseholdItem();
                    houseHoldItem.setLockedSave(AppConstant.SAVE+"");

                    /*if(SeccDatabase.checkUnderSurveyMember(context,houseHoldItem.getHhdNo())){
                        houseHoldItem.setLockSave(AppConstant.SAVE+"");
                    }else{
                        houseHoldItem.setLockSave(AppConstant.LOCKED+"");
                    }*/
                    selectedMemItem.setRsbyHouseholdItem(houseHoldItem);
                    SeccDatabase.updateRSBYHouseHold(selectedMemItem.getRsbyHouseholdItem(),context);
                    ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                            AppConstant.SELECTED_ITEM_FOR_VERIFICATION, selectedMemItem.serialize(), context);
                    theIntent = new Intent(context, RsbyMainActivity.class);
                }
                startActivity(theIntent);
                finish();
                rightTransition();
            }
        });
        lockBT.setTextColor(context.getResources().getColor(R.color.white));
        lockBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent theIntent=null;
                if(rsbyItem.getNhpsRelationCode()!=null && rsbyItem.getNhpsRelationCode().equalsIgnoreCase("01")){
                    if(!rsbyItem.getMemStatus().equalsIgnoreCase(AppConstant.MEMBERFOUND_BUT_NOT_PRESENT) ||
                            !rsbyItem.getMemStatus().equalsIgnoreCase(AppConstant.MEMBERFOUND_BUT_NOT_PRESENT)) {
                        CustomAlert.alertWithOk(context, "Head of the family member should not be died or migrated");
                        return;
                    }
                }
                /*if(rsbyItem.getRelation()!=null && rsbyItem.getRelation().equalsIgnoreCase("HEAD")){

                    if(!rsbyItem.getMemStatus().equalsIgnoreCase(AppConstant.MEMBERFOUND_BUT_NOT_PRESENT) ||
                            !rsbyItem.getMemStatus().equalsIgnoreCase(AppConstant.MEMBERFOUND_BUT_NOT_PRESENT)) {
                        CustomAlert.alertWithOk(context, "Head of the family member should not be died or migrated");
                        return;
                    }
                }*/

                //lockPrompt();
                alertForConsent(context);
//                askPinToLock();


            }
        });

        prepareMemberStatusSpinner();
        memberStatusSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                MemberStatusItem statItem = memberStatusList.get(position);
                AppUtility.showLog(AppConstant.LOG_STATUS,"Secc Member Activity","Status Code:"+statItem.getStatusCode());
                if (statItem.getStatusCode().equalsIgnoreCase(AppConstant.MEMBERFOUND_BUT_NOT_PRESENT)) {
                    memberStatusLayout.setVisibility(View.VISIBLE);
                    updateLayout.setVisibility(View.GONE);
                    rsbyItem.setMemStatus(statItem.getStatusCode()+"");
                    //SeccDatabase.updateSeccMember(rsbyItem,context);
                } else if(statItem.getStatusCode().equalsIgnoreCase("0")) {
                    memberStatusLayout.setVisibility(View.GONE);
                    updateLayout.setVisibility(View.GONE);
                }else if (statItem.getStatusCode().equalsIgnoreCase(AppConstant.MEMBERFOUND_AND_PRESENT)) {
                    memberStatusLayout.setVisibility(View.VISIBLE);
                    updateLayout.setVisibility(View.GONE);
                    rsbyItem.setMemStatus(statItem.getStatusCode() + "");
                }else{
                    Log.d("SeccMemberDetail","Member Status :"+statItem.getStatusCode());
                    memberStatusLayout.setVisibility(View.GONE);
                    updateLayout.setVisibility(View.VISIBLE);
                    rsbyItem.setMemStatus(statItem.getStatusCode() + "");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
                backIV.performClick();
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


    private void openWithoutAadhaar(){
        Intent theIntent=new Intent(context,WithAadhaarActivity.class);
        // theIntent.putExtra(AppConstant.MEMBER_TYPE,AppConstant.SECC_MEMBER);
        // rsbyItem.setAadhaarStatus("2");
      //  AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Relation Code : "+selectedMemItem.getOldHeadMember().getNhpsRelationCode());
       /* AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Update is calling : " +
                ""+rsbyItem.getNhpsRelationCode()+" : "+" Relation Item : "+newRelItem);*/
        if(selectedMemItem.getNewHeadMember()!=null){
            if(rsbyItem.getNhpsRelationCode()==null){
                if(newRelItem==null){
                    String headName=selectedMemItem.getNewHeadMember().getName();
                    AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Update is calling : " +
                            ""+headName+" : "+" Relation Item : "+newRelItem);
                    CustomAlert.alertWithOk(context,"Please select relation with respect to "+headName);
                    return;
                }
            }
        }else {
            if (selectedMemItem.getOldHeadMember() != null) {
                if (rsbyItem.getNhpsRelationCode() == null) {
                    if (newRelItem == null) {
                        String headName = selectedMemItem.getOldHeadMember().getName();
                        AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Update is calling : " +
                                "" + headName + " : " + " Relation Item : " + newRelItem);
                        CustomAlert.alertWithOk(context, "Please select relation with respect to " + headName);
                        return;
                    }

                }
            }
        }

        selectedMemItem.setRsbyMemberItem(rsbyItem);
        // selectedMemberStatus=memberStatusSP.getSelectedItemPosition();
        rsbyItem.setMemStatus(memberStatusList.get(memberStatusSP.getSelectedItemPosition()).getStatusCode());
        rsbyItem.setLockedSave(AppConstant.SAVE+"");
        houseHoldItem.setLockedSave(AppConstant.SAVE+"");

        SeccDatabase.updateRsbyMember(rsbyItem, context);
        SeccDatabase.updateRSBYHouseHold(houseHoldItem,context);
        rsbyItem= SeccDatabase.getRsbyMemberDetail(rsbyItem.getRsbyMemId(),context);

        // Log.d("Secc Member setR ","Secc member det : "+rsbyItem.getAadhaarStatus());
        selectedMemItem.setRsbyMemberItem(rsbyItem);
        selectedMemItem.setRsbyHouseholdItem(houseHoldItem);
      /*  AppUtility.showLog(AppConstant.LOG_STATUS,
                TAG,"Relation" + " Code : "+ selectedMemItem.getOldHeadMember().getNhpsRelationCode());
*/
        ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.SELECTED_ITEM_FOR_VERIFICATION, selectedMemItem.serialize(), context);
        theIntent=new Intent(context,RsbyValidationWithAadharActivity.class);
        startActivityForResult(theIntent,SECC_MEMBER_DETAIL);
        leftTransition();
        // finish();
    }
    private void prepareMemberStatusSpinner(){
        memberStatusList=new ArrayList<>();
        ArrayList<String> spinnerList=new ArrayList<>();
        ArrayList<MemberStatusItem>  memberStatusList1= SeccDatabase.getMemberStatusList(context);
        memberStatusList.add(0,new MemberStatusItem("M","0","Select Member Status",null,"Y"));
       /* ;
        memberStatusList.add(new MemberStatusItem(1,"Member Found"));
        memberStatusList.add(new MemberStatusItem(2,"Member Migrated"));
        memberStatusList.add(new MemberStatusItem(3,"Member Died"));
        memberStatusList.add(new MemberStatusItem(4,"No Information Available"));*/
        boolean headStatus=false;
        if(rsbyItem.getRsbyMemId()!=null &&
                rsbyItem.getRsbyMemId().equalsIgnoreCase(houseHoldItem.getRsbyMemId()) ){
            headStatus=true;
        }
        if(rsbyItem.getNhpsRelationCode()!=null &&
                rsbyItem.getNhpsRelationCode().equalsIgnoreCase(AppConstant.NEW_HEAD_RELATION_CODE)){
            headStatus=true;
        }

        if(rsbyItem.getName()!=null && !rsbyItem.getName().equalsIgnoreCase("")) {

            //  if (selectedMemItem.getSeccMemberItem().getNhpsRelationCode() != null && selectedMemItem.getSeccMemberItem().getNhpsRelationCode().equalsIgnoreCase(AppConstant.NEW_HEAD_RELATION_CODE)) {

            if(headStatus){
                for (MemberStatusItem item : memberStatusList1) {
                    if (item.getStatusCode().equalsIgnoreCase(AppConstant.MEMBERFOUND_BUT_NOT_PRESENT)) {
                        //spinnerList.add(item.getStatusDesc());
                        memberStatusList.add(item);
                    }
                    if (item.getStatusCode().equalsIgnoreCase(AppConstant.MEMBERFOUND_AND_PRESENT)) {
                        memberStatusList.add(item);
                        // spinnerList.add(item.getStatusDesc());
                    }
                }
            } else {
                for (MemberStatusItem item : memberStatusList1) {
                    if(item.getStatusCode().equalsIgnoreCase(AppConstant.MEMBER_ENROL_THROUGH_RSBY)){

                    }else {
                        memberStatusList.add(item);
                    }
                }
            }
        }else{
            AppUtility.showLog(AppConstant.LOG_STATUS,"Secc Member detai : ","Name is blank..");
            for (MemberStatusItem item : memberStatusList1) {
                if(item.getStatusCode().equalsIgnoreCase(AppConstant.NO_INFO_AVAIL)){
                    memberStatusList.add(item);
                }
               /* if (item.getStatusCode().equalsIgnoreCase(AppConstant.MEMBERFOUND_BUT_NOT_PRESENT) || item.getStatusCode().equalsIgnoreCase(AppConstant.MEMBERFOUND_AND_PRESENT)) {
                    //spinnerList.add(item.getStatusDesc());
                   // memberStatusList.add(item);
                }else{
                    AppUtility.showLog(AppConstant.LOG_STATUS,"Secc Member detai : ","Name is blank..");
                    memberStatusList.add(item);
                }*/
                // spinnerList.add(item.getStatusDesc());
            }
        }
        for(MemberStatusItem item : memberStatusList){
            spinnerList.add(item.getStatusDesc());
        }
        ArrayAdapter<String> maritalAdapter = new ArrayAdapter<>(context, R.layout.custom_drop_down, R.id.textView,spinnerList);
        memberStatusSP.setAdapter(maritalAdapter);
        if(rsbyItem!=null){
            for(int i=0;i<memberStatusList.size();i++){
                if(rsbyItem.getMemStatus()!=null && rsbyItem.getMemStatus().trim().equalsIgnoreCase(memberStatusList.get(i).getStatusCode())){
                    selectedMemberStatus=i;
                    break;
                }
            }
        }

        memberStatusSP.setSelection(selectedMemberStatus);


    }
    private void logoutScreen(){
        final ImageView settings=(ImageView)findViewById(R.id.settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(context, settings);
                popup.getMenuInflater()
                        .inflate(R.menu.logout_home, popup.getMenu());
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

    private void prepareNomineeRelation(){
        relationList=new ArrayList<>();
        ArrayList<RelationItem>  relationList1=new ArrayList<>();
        RelationItem item1=new RelationItem();
        item1.setRelationCode("0");
        item1.setRelationName("Select Relation");
        relationList.add(0,item1);
        if(rsbyItem.getGender()!=null && rsbyItem.getGender().trim().equalsIgnoreCase(AppConstant.MALE_GENDER)){
            relationList1= SeccDatabase.getRelationListByGender(context,"M");
        }else if(rsbyItem.getGender()!=null && rsbyItem.getGender().trim().equalsIgnoreCase(AppConstant.FEMALE_GENDER)){
            relationList1= SeccDatabase.getRelationListByGender(context,"F");
        }else{
            relationList1= SeccDatabase.getRelationList(context);
        }
        Collections.sort(relationList1, new Comparator<RelationItem>() {
            @Override
            public int compare(RelationItem relationItem, RelationItem t1) {
                return Integer.parseInt(relationItem.getDisplayOrder())-Integer.parseInt(t1.getDisplayOrder());
            }
        });

        ArrayList<String> spinnerList=new ArrayList<>();
        for(RelationItem item : relationList1){
            if(item.getRelationCode().equalsIgnoreCase(AppConstant.NEW_HEAD_RELATION_CODE)) {

            } else {
                relationList.add(item);
            }
        }
        for(RelationItem item : relationList){
            spinnerList.add(item.getRelationName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.custom_drop_down, R.id.textView,spinnerList);
        newRelSP.setAdapter(adapter);
        int selectedPos=0;
        for(int i=0;i<relationList.size();i++){
            if(rsbyItem.getNhpsRelationCode()!=null && rsbyItem.getNhpsRelationCode().trim().equalsIgnoreCase(relationList.get(i).getRelationCode().trim())){
                // AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Relation Code "+relationList.get(i).getRelationName());
                selectedPos=i;
                break;
            }
        }
        newRelSP.setSelection(selectedPos);

        /*if(rsbyItem.getNhpsRelationCode()!=null) {

        }else{
            newRelSP.setSelection(selectedPos);
        }*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==SECC_MEMBER_DETAIL){
            //  setupScreen();
        }

    }

    private void lockPrompt(){
        dialog = new AlertDialog.Builder(context).create();
        LayoutInflater factory = LayoutInflater.from(context);
        View alertView = factory.inflate(R.layout.delete_house_hold_prompt, null);
        dialog.setView(alertView);
        // dialog.setContentView(R.layout.opt_auth_layout);
        //    final TextView otpAuthMsg=(TextView)alertView.findViewById(R.id.otpAuthMsg);

        Button syncBT= (Button) alertView.findViewById(R.id.syncBT);
        syncBT.setText("Ok");
        Button cancelBT=(Button)alertView.findViewById(R.id.cancelBT);
        final TextView deletePromptTV= (TextView) alertView.findViewById(R.id.deleteMsg);
        // long unsyncData=SeccDatabase.countSurveyedHousehold(context,"","");
        // long underSurvey=SeccDatabase.countUnderSurveyedHousehold(context,"","");
        String msg=getResources().getString(R.string.locked_msg);
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

                currentTime = System.currentTimeMillis();
                try {

                    wrongPinSavedTime = Long.parseLong(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.WORNG_PIN_ENTERED_TIMESTAMP, context));
                } catch (Exception ex) {
                    wrongPinSavedTime = 0;
                }
                if (currentTime > (wrongPinSavedTime + millisecond24)) {

                String pin = pinET.getText().toString();
                if (pin.toString().equalsIgnoreCase(verifierDetail.getPin())) {
                    lockSubmit();
                } else if (pin.equalsIgnoreCase("")) {
                    // CustomAlert.alertWithOk(context,"Please enter valid pin");
                    errorTV.setVisibility(View.VISIBLE);
                    errorTV.setText("Enter pin");
                    pinET.setText("");
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

    private void lockSubmit(){
        Intent theIntent;
        if(rsbyItem!=null) {
            rsbyItem.setLockedSave(AppConstant.LOCKED + "");
           /* rsbyItem.setSyncDt(String.valueOf(DateTimeUtil.currentTimeMillis()));
            rsbyItem.setAppVersion(AppUtility.getCurrentApplicationVersion(context));*/
            SeccDatabase.updateRsbyMember(rsbyItem,context);
            RsbyHouseholdItem houseHoldItem=selectedMemItem.getRsbyHouseholdItem();
            if(SeccDatabase.checkRsbyUnderSurveyMember(houseHoldItem.getUrnId(),context)){
                houseHoldItem.setLockedSave(AppConstant.SAVE+"");
            }else{
                //  houseHoldItem.setAppVersion(AppUtility.getCurrentApplicationVersion(context));
                houseHoldItem.setLockedSave(AppConstant.LOCKED+"");
                //  houseHoldItem.setSyncDt(String.valueOf(DateTimeUtil.currentTimeMillis()));
            }
            selectedMemItem.setRsbyHouseholdItem(houseHoldItem);
            //    Log.d("Secc Member Detail "," Household Item json : "+houseHoldItem.serialize());
            SeccDatabase.updateRSBYHouseHold(selectedMemItem.getRsbyHouseholdItem(),context);
            rsbyItem= SeccDatabase.getRsbyMemberDetail(rsbyItem.getRsbyMemId(),context);
            selectedMemItem.setRsbyMemberItem(rsbyItem);
            ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                    AppConstant.SELECTED_ITEM_FOR_VERIFICATION, selectedMemItem.serialize(), context);
            theIntent = new Intent(context, RsbyMainActivity.class);
            startActivity(theIntent);
            finish();
            rightTransition();
        }




    }
    private  void alertForConsent(Context context) {

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
                if(consent.isChecked()){
                    errorTV.setVisibility(View.GONE);
                    askPinToLock();
                }else{
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

   /* private void findHead(){
        if(houseHoldMemberList!=null) {
            for (SeccMemberItem item : houseHoldMemberList) {
                if (item.getNhpsMemId() != null && item.getNhpsMemId().equalsIgnoreCase(houseHoldItem.getNhpsMemId())) {
                    oldHeadItem = item;
                    break;
                }
            }
        }
    }*/
}