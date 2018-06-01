package com.nhpm.rsbyFieldValidation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.customComponent.CustomAlert;
import com.customComponent.utility.ProjectPrefrence;
import com.nhpm.activity.BaseActivity;
import com.nhpm.LocalDataBase.dto.SeccDatabase;
import com.nhpm.Models.response.NomineeMemberItem;
import com.nhpm.Models.response.RelationItem;
import com.nhpm.Models.response.rsbyMembers.RSBYItem;
import com.nhpm.Models.response.seccMembers.SelectedMemberItem;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;
import com.nhpm.Utility.AppUtility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import pl.polidea.view.ZoomView;

/**
 * Created by DELL on 26-02-2017.
 */

public class RsbyNomineeCaptureActivity extends BaseActivity {
    private Spinner nomineeRelSP,otherRelSP,memberSP;
    private EditText relationNameET,otherNomineeNameET;
    private ArrayList<NomineeMemberItem> nomineeMemberList;
    private TextView headerTV;
    private Button submitBt;
    private ImageView backIV;
    private SelectedMemberItem selectedMemItem;
    /*private SeccMemberItem rsbyItem;*/
    private RSBYItem rsbyItem;
    private ArrayList<RelationItem> relationList,otherRelationList;
    private final String TAG="Nominee details";
    private Context context;
    private LinearLayout nomineeRelLayout,otherNomineeLayout;
    private RelationItem memberRelationItem,otherRelationItem;
    private boolean isMemberRel,isOtherRel,isMemberOutOftheFamily;
    private NomineeMemberItem nomineeItem;
    private LinearLayout mZoomLinearLayout;
    private ZoomView zoomView;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dummy_layout_for_zooming);
        activity = this;
        setupScreen();
    }
    private void setupScreen(){
        context=this;
        AppUtility.hideSoftInput(activity);
        mZoomLinearLayout = (LinearLayout) findViewById(R.id.mZoomLinearLayout);
        View v = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.activity_nominee_capture, null, false);
        v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));

        headerTV=(TextView)v.findViewById(R.id.centertext);
        selectedMemItem= SelectedMemberItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.SELECTED_ITEM_FOR_VERIFICATION, context));
        if(selectedMemItem.getRsbyMemberItem()!=null) {
            rsbyItem = selectedMemItem.getRsbyMemberItem();
            headerTV.setText(rsbyItem.getName());
            Log.d(TAG,"Secc Member : "+rsbyItem.serialize());
        }
        nomineeRelLayout=(LinearLayout)v.findViewById(R.id.nomineeRelLayout);
        otherNomineeLayout=(LinearLayout)v.findViewById(R.id.otherNomineeLayout);
        submitBt=(Button)v.findViewById(R.id.submitBT);
        relationNameET=(EditText)v.findViewById(R.id.nomineeRelationshipET);

        otherNomineeNameET=(EditText)v.findViewById(R.id.otherNomineeNameET) ;
        backIV=(ImageView)v.findViewById(R.id.back);
        memberSP=(Spinner)v.findViewById(R.id.nomineeMemberSP);

        zoomView = new ZoomView(this);
        zoomView.addView(v);
        mZoomLinearLayout.addView(zoomView);
        memberSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                nomineeItem = nomineeMemberList.get(position);
                isMemberRel=false;
                isOtherRel=false;
                isMemberOutOftheFamily=false;
                switch (nomineeItem.statusCode) {
                    case 0:
                        nomineeRelLayout.setVisibility(View.GONE);
                        otherNomineeLayout.setVisibility(View.GONE);
                        submitBt.setVisibility(View.GONE);
                        // nomineeRelSP.setSelection(0);
                        break;
                    case 1:
                        prepareNomineeRelation();
                        nomineeRelLayout.setVisibility(View.VISIBLE);
                        otherNomineeLayout.setVisibility(View.GONE);
                        break;
                    case 2:
                        isMemberOutOftheFamily=true;
                        submitBt.setVisibility(View.GONE);
                        nomineeRelLayout.setVisibility(View.GONE);
                        // otherRelSP.setSelection(0);
                        otherNomineeLayout.setVisibility(View.VISIBLE);
                        AppUtility.requestFocus(otherNomineeNameET);
                        AppUtility.showSoftInput(activity);
                        boolean flag=false;
                        if(rsbyItem.getNameNominee()!=null && !rsbyItem.getNameNominee().equalsIgnoreCase("") ){
                            for(NomineeMemberItem item : nomineeMemberList){
                                if(item.getMemberName().equalsIgnoreCase(rsbyItem.getNameNominee())){
                                    flag=true;
                                    break;
                                }
                            }
                        }
                        if(flag){

                        }else {
                            otherNomineeNameET.setText(rsbyItem.getNameNominee());

                        }
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        nomineeRelSP=(Spinner) findViewById(R.id.nomineeRelSP);
        nomineeRelSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                submitBt.setVisibility(View.GONE);
                memberRelationItem=relationList.get(position);

                if(memberRelationItem.getRelationCode().equalsIgnoreCase(AppConstant.DEFAULT_RELATION)){

                }else{
                    isMemberRel=true;
                    isOtherRel=false;
                    isMemberOutOftheFamily=false;
                    AppUtility.showLog(AppConstant.LOG_STATUS,TAG," here 1 : "+memberRelationItem.getRelationName());
                    submitBt.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        otherRelSP=(Spinner) findViewById(R.id.otherNomineeRelSP);
        otherRelSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                relationNameET.setVisibility(View.GONE);
                // submitBt.setVisibility(View.GONE);
                otherRelationItem=otherRelationList.get(position);
                isMemberRel=false;
                isOtherRel=false;
                AppUtility.showLog(AppConstant.LOG_STATUS,TAG," here 2 : "+otherRelationItem.getRelationName());

                if(otherRelationItem.getRelationCode().equalsIgnoreCase(AppConstant.DEFAULT_RELATION)){
                    relationNameET.setVisibility(View.GONE);
                    //  submitBt.setVisibility(View.GONE);
                }else{
                    //RelationItem item=otherRelationList.get(position);
                    submitBt.setVisibility(View.VISIBLE);

                    AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Selected relation code : " +
                            ""+otherRelationItem.getRelationCode()+"Relation Name : "+otherRelationItem.getRelationName());
                    if(otherRelationItem.getRelationCode().equalsIgnoreCase(AppConstant.OTHER_RELATION)){
                        isOtherRel=true;
                        relationNameET.setVisibility(View.VISIBLE);
                        AppUtility.clearFocus(otherNomineeNameET);
                        AppUtility.requestFocus(relationNameET);
                        //     AppUtility.showSoftInput(activity);
                        if(rsbyItem.getRelationNomineeCode()!=null && rsbyItem.getRelationNomineeCode().
                                equalsIgnoreCase(AppConstant.OTHER_RELATION)){
                            relationNameET.setText(rsbyItem.getNomineeRelationName());
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
                theIntent = new Intent(context, RsbyValidationWithAadharActivity.class);
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
    private void submit(){
        String relationName,memberName;
        if(isMemberRel){
            if(nomineeItem.getAge()<18){
                CustomAlert.alertWithOk(context, getResources().getString(R.string.minor_nominee_msg));
                return;
            }
            rsbyItem.setNameNominee(nomineeItem.getMemberName());
            rsbyItem.setRelationNomineeCode(memberRelationItem.getRelationCode());
            rsbyItem.setNomineeRelationName(memberRelationItem.getRelationName());
        }
        if(isMemberOutOftheFamily){
            memberName=otherNomineeNameET.getText().toString();
            if(memberName.equalsIgnoreCase("")){
                CustomAlert.alertWithOk(context, "Please enter nominee name");
                return;
            }
            rsbyItem.setNameNominee(memberName);

            if(isOtherRel) {
                rsbyItem.setRelationNomineeCode(otherRelationItem.getRelationCode());
                relationName = relationNameET.getText().toString();
                if (relationName.equalsIgnoreCase("")) {
                    CustomAlert.alertWithOk(context, "Please enter relation name");
                    return;
                }
                // edited by saurfabh

                rsbyItem.setNomineeRelationName(relationName);
            }else{
                rsbyItem.setNomineeRelationName(otherRelationItem.getRelationName());
                rsbyItem.setRelationNomineeCode(otherRelationItem.getRelationCode());
            }
        }

        AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Relation Code :" +
                " " + rsbyItem.getRelationNomineeCode() + " :" +
                " " + rsbyItem.getNomineeRelationName() + " Nominee Name : " + rsbyItem.getNameNominee()
                +" ismember ; "+isMemberRel+" out of family : "+isMemberOutOftheFamily+" : is other "+isOtherRel);

        AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Relation Code :" +
                " " + rsbyItem.getRelationNomineeCode() + " :" +
                " " + rsbyItem.getNomineeRelationName() + " Nominee Name : " + rsbyItem.getNameNominee());


        AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Relation Code :" +
                " " + rsbyItem.getRelationNomineeCode() + " :" +
                " " + rsbyItem.getNomineeRelationName() + " Nominee Name : " + rsbyItem.getNameNominee());

        if(selectedMemItem.getOldHeadrsbyMemberItem()!=null && selectedMemItem.getNewHeadrsbyMemberItem()!=null){
            RSBYItem oldHead=selectedMemItem.getOldHeadrsbyMemberItem();
            oldHead.setLockedSave(AppConstant.LOCKED+"");
            AppUtility.showLog(AppConstant.LOG_STATUS,TAG," OLD Household name " +
                    ": "+oldHead.getName()+"" +
                    " Member Status "+oldHead.getMemStatus()+" House hold Status :" +
                    " "+oldHead.getHhStatus()+" Locked Save :"+oldHead.getLockedSave());
            SeccDatabase.updateRsbyMember(selectedMemItem.getOldHeadrsbyMemberItem(),context);
        }
        rsbyItem.setLockedSave(AppConstant.SAVE + "");
        SeccDatabase.updateRsbyMember(rsbyItem,context);
        SeccDatabase.updateRSBYHouseHold(selectedMemItem.getRsbyHouseholdItem(),context);
        // SeccDatabase.getSeccMemberDetail(rsbyItem.getAhlTin(),context);
        rsbyItem= SeccDatabase.getRsbyMemberDetail(rsbyItem.getRsbyMemId(),context);
        selectedMemItem.setRsbyMemberItem(rsbyItem);
        ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.SELECTED_ITEM_FOR_VERIFICATION,selectedMemItem.serialize(),context);
        Intent theIntent = null;
        //if (rsbyItem != null && rsbyItem.getAadhaarStatus().equalsIgnoreCase("1")) {
        theIntent = new Intent(context, RsbyValidationWithAadharActivity.class);
        startActivity(theIntent);
        rightTransition();
        finish();
    }
    private void prepareNomineeSpinner(){
        boolean flag=false;
        nomineeMemberList=new ArrayList<>();
        ArrayList<RSBYItem> nomineeList= SeccDatabase.getRsbyMemberList(rsbyItem.getUrnNo(),context);
        ArrayList<String> spinnerList=new ArrayList<>();
        nomineeMemberList.add(new NomineeMemberItem(0,"Select member for nominee","Select member for nominee"));
        for(RSBYItem item : nomineeList){
            if(item.getNhpsRelationCode()!=null && item.getNhpsRelationCode().equalsIgnoreCase(AppConstant.NEW_HEAD_RELATION_CODE)){
                flag=true;
                break;
            }
        }
        for(RSBYItem item : nomineeList){
         /*   if(item.getRsbyMemId().equalsIgnoreCase(rsbyItem.getRsbyMemId())){

            }else if(item.getMemStatus()!=null && item.getMemStatus().trim().equalsIgnoreCase("") || item.getMemStatus().trim().equalsIgnoreCase(AppConstant.MEMBERFOUND_BUT_NOT_PRESENT) || item.getMemStatus().trim().equalsIgnoreCase(AppConstant.MEMBERFOUND_AND_PRESENT) ||item.getMemStatus()==null ){
           */     if(!item.getName().equalsIgnoreCase("")) {
                    String yearOfBirth=null;
                    String gender;

                    if(item.getDob()!=null && !item.getDob().equalsIgnoreCase("") && !item.getDob().equalsIgnoreCase("-")){
                        yearOfBirth=item.getDob().substring(4, item.getDob().length());
                    }/*else if(item.getDobFrmNpr()!=null && !item.getDobFrmNpr().equalsIgnoreCase("") && !item.getDobFrmNpr().equalsIgnoreCase("-")){
                        yearOfBirth=item.getDob().substring(0, 4);
                    }*/
                    NomineeMemberItem ite1 = new NomineeMemberItem();
                    ite1.setRsbyItem(item);
                    ite1.setStatusCode(1);
                    if(item.getGender()!=null && item.getGender().equalsIgnoreCase(AppConstant.MALE_GENDER)){
                        gender="M";
                    }else if(item.getGender()!=null && item.getGender().equalsIgnoreCase(AppConstant.FEMALE_GENDER)){
                        gender="F";
                    }else{
                        gender="O";
                    }
                    ite1.setGender(gender);
                    if(yearOfBirth!=null){
                        int age= AppConstant.COMPARED_YEAR-Integer.parseInt(yearOfBirth);
                        ite1.setAge(age);
                    }
                    String modifiedNomineeDropdownCaption=item.getName()+" - ("+ite1.getGender()+") "+ite1.getAge()+" yrs";
                    ite1.setMemberName(item.getName());
                    ite1.setNomineeLabel(modifiedNomineeDropdownCaption);
                    AppUtility.showLog(AppConstant.LOG_STATUS,TAG," Nominee Label :"+ite1.getNomineeLabel());
                if(!ite1.getMemberName().equalsIgnoreCase(rsbyItem.getName()))
                    nomineeMemberList.add(ite1);
                }
      /*      }*/
        }

        nomineeMemberList.add(new NomineeMemberItem(2, "Other","Other"));
        for(NomineeMemberItem item1 : nomineeMemberList){
            AppUtility.showLog(AppConstant.LOG_STATUS,TAG," Nominee Label :"+item1.getNomineeLabel());
            spinnerList.add(item1.getNomineeLabel());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.custom_drop_down, R.id.textView,spinnerList);
        memberSP.setAdapter(adapter);
        int selectNominee=0;
        for(int i=0;i< nomineeMemberList.size();i++){
            if(rsbyItem.getNameNominee()!=null && rsbyItem.getNameNominee().equalsIgnoreCase(nomineeMemberList.get(i).memberName)){
                selectNominee=i;
                break;
            }
        }
        if(selectNominee==0){
            if(rsbyItem.getNameNominee()!=null && !rsbyItem.getNameNominee().equalsIgnoreCase("")){
                selectNominee=nomineeMemberList.size()-1;
            }
        }
        memberSP.setSelection(selectNominee);
      /*  if(selectNominee==0){
            if(rsbyItem.getNameNominee()!=null && !rsbyItem.getNameNominee().equalsIgnoreCase("")){
                nomineeMemberSP.setSelection(nomineeMemberList.size()-1);
                otherNomineeNameET.setText(rsbyItem.getNameNominee());
                otherNomineeRelET.setText(rsbyItem.getNomineeRelationName());
            }
        }*/



    }

    private void prepareNomineeRelation(){
        relationList=new ArrayList<>();
        ArrayList<RelationItem> list=new ArrayList<>();
        if(nomineeItem.getMemberItem()!=null && nomineeItem.getMemberItem().getGenderid().trim().equalsIgnoreCase(AppConstant.MALE_GENDER)){
            list= SeccDatabase.getRelationListByGender(context,"M");
        }else if(nomineeItem.getMemberItem()!=null && nomineeItem.getMemberItem().getGenderid().trim().equalsIgnoreCase(AppConstant.FEMALE_GENDER)){
            list= SeccDatabase.getRelationListByGender(context,"F");
        }else{
            list= SeccDatabase.getRelationList(context);
        }
        RelationItem item1=new RelationItem();
        item1.setRelationCode(AppConstant.DEFAULT_RELATION);
        item1.setRelationName("Select Relation");
        relationList.add(0,item1);
        for (RelationItem relItem :list ){
            if(relItem.getRelationCode().equalsIgnoreCase(AppConstant.HEAD_RELATION)) {

            } else {
                relationList.add(relItem);
            }
        }
        Collections.sort(relationList, new Comparator<RelationItem>() {
            @Override
            public int compare(RelationItem relationItem, RelationItem t1) {

                return Integer.parseInt(relationItem.getRelationCode())-Integer.parseInt(t1.getRelationCode());
            }
        });
        ArrayList<String> spinnerList=new ArrayList<>();
        //spinnerList.add("Select Relation");
        for(RelationItem item : relationList){
            spinnerList.add(item.getRelationName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.custom_drop_down, R.id.textView,spinnerList);
        nomineeRelSP.setAdapter(adapter);
        int selectedPos=0;
        for(int i=0;i<relationList.size();i++){
            if(rsbyItem.getRelationNomineeCode()!=null && rsbyItem.getRelationNomineeCode().equalsIgnoreCase(relationList.get(i).getRelationCode())){
                selectedPos=i;
                //  submitBt.setVisibility(View.VISIBLE);
                break;
            }
        }
        if(selectedPos!=0)
            nomineeRelSP.setSelection(selectedPos);


    }
    private void prepareNomineeOtherRelation(){
        //nomineeMemberList=new ArrayList<>();
        otherRelationList=new ArrayList<>();
        ArrayList<RelationItem> list= SeccDatabase.getRelationList(context);
        RelationItem item1=new RelationItem();
        item1.setRelationCode(AppConstant.DEFAULT_RELATION);
        item1.setRelationName("Select Relation");
        otherRelationList.add(0,item1);
        for(RelationItem relationItem : list){
            if(relationItem.getRelationCode().equalsIgnoreCase(AppConstant.HEAD_RELATION)) {

            }else{
                otherRelationList.add(relationItem);
            }
        }
        Collections.sort(otherRelationList, new Comparator<RelationItem>() {
            @Override
            public int compare(RelationItem relationItem, RelationItem t1) {

                return Integer.parseInt(relationItem.getRelationCode())-Integer.parseInt(t1.getRelationCode());
            }
        });
        ArrayList<String> spinnerList=new ArrayList<>();
        for(RelationItem item : otherRelationList){
            spinnerList.add(item.getRelationName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.custom_drop_down, R.id.textView,spinnerList);
        otherRelSP.setAdapter(adapter);
        int selectedPos=0;
        AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Other Relation code : "+rsbyItem.getRelationNomineeCode());
        for(int i=0;i<otherRelationList.size();i++){
            if(rsbyItem.getRelationNomineeCode()!=null &&
                    rsbyItem.getRelationNomineeCode().equalsIgnoreCase(otherRelationList.get(i).getRelationCode())){
                selectedPos=i;
                // submitBt.setVisibility(View.VISIBLE);
                break;
            }
        }
        //if(!isMemberRel) {
        // if(selectedPos!=0)
        otherRelSP.setSelection(selectedPos);
        //}
    }

}
