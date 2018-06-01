package com.nhpm.rsbyFieldValidation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
import com.nhpm.Models.response.master.HealthSchemeItem;
import com.nhpm.Models.response.rsbyMembers.RSBYItem;
import com.nhpm.Models.response.rsbyMembers.RsbyHouseholdItem;
import com.nhpm.Models.response.seccMembers.SeccMemberItem;
import com.nhpm.Models.response.seccMembers.SelectedMemberItem;
import com.nhpm.Models.response.verifier.VerifierLocationItem;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;
import com.nhpm.Utility.AppUtility;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.TreeSet;

import pl.polidea.view.ZoomView;

/**
 * Created by DELL on 26-02-2017.
 */

public class RsbhyHealthSchemeActivity extends BaseActivity {
    private TextView headerTV;
    private ArrayList<HealthSchemeItem> schemeList;
    private Context context;
    private Spinner schemeSP;
    private Button submitBt;
    private ImageView backIV;
    private int navigateType;
    private SelectedMemberItem selectedMemItem;
    private RSBYItem rsbyItem;
    private LinearLayout stateHealthSchemeLayout;
    private EditText stateHealthIdET;
    private AutoCompleteTextView urnET;
    private String TAG="HealthSchemeActivity";
    private LinearLayout mZoomLinearLayout;
    private ZoomView zoomView;
    private RSBYItem headOfThefamily;
    private RsbyHouseholdItem houseHoldItem;
    private LinearLayout sstateHealthSchemeLayout;
    private ArrayList<String> urnList=new ArrayList<>();
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
        View v = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.activity_health_scheme, null, false);
        v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));

        selectedMemItem= SelectedMemberItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.SELECTED_ITEM_FOR_VERIFICATION, context));
        selectedLocation= VerifierLocationItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.SELECTED_BLOCK,context));
        headerTV=(TextView)v.findViewById(R.id.centertext);
        schemeSP=(Spinner)v.findViewById(R.id.healthSchemeNameSP);
        sstateHealthSchemeLayout = (LinearLayout) v.findViewById(R.id.sstateHealthSchemeLayout);
        submitBt=(Button)v.findViewById(R.id.submitBT);
        stateHealthIdET=(EditText)v.findViewById(R.id.stateHealthIdET);
        backIV=(ImageView)v.findViewById(R.id.back);
        urnET=(AutoCompleteTextView)v.findViewById(R.id.rsbyURNNoET);
        AppUtility.requestFocus(urnET);
        prepareSchemeSpinner();
        stateHealthSchemeLayout=(LinearLayout)v.findViewById(R.id.stateHealthSchemeLayout);

        zoomView = new ZoomView(this);
        zoomView.addView(v);
        mZoomLinearLayout.addView(zoomView);
        if(selectedMemItem.getRsbyMemberItem()!=null) {
            rsbyItem = selectedMemItem.getRsbyMemberItem();
            houseHoldItem=selectedMemItem.getRsbyHouseholdItem();
            ArrayList<RSBYItem> seccMemList= SeccDatabase.getRsbyMemberList(rsbyItem.getUrnNo(),context);
            for(RSBYItem item : seccMemList){
                if (item.getNhpsRelationCode()!=null && item.getNhpsRelationCode().trim().equalsIgnoreCase(AppConstant.NEW_HEAD_RELATION_CODE)) {
                    // if(item.getNhpsMemId().trim().equalsIgnoreCase(houseHoldItem.getNhpsMemId().trim())){
                    headOfThefamily=item;
                    AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Head of the family name : "+item.getName()+" Scheme code : "+item.getSchemeId());
                    // }
                }
                if(item.getUrnNo()!=null && !item.getUrnNo().equalsIgnoreCase("")){
                    //if(urnList.size()>0){
                    AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Captured URN : "+item.getUrnNo());
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
            TreeSet<String> businessTypeSet = new TreeSet<String>(new Comparator<String>(){

                public int compare(String o1, String o2) {
                    // return 0 if objects are equal in terms of your properties
                    if (o1.equalsIgnoreCase(o2)){
                        return 0;
                    }
                    return 1;
                }
            });
            businessTypeSet.addAll(urnList);
            urnList=new ArrayList<>();
            urnList.addAll(businessTypeSet);
            // wardTypeSpinnerList.addAll(businessTypeSet);
            headerTV.setText(rsbyItem.getName());

            if(rsbyItem.getUrnNo()!=null && !rsbyItem.getUrnNo().equalsIgnoreCase("")){
                urnET.setText(rsbyItem.getUrnNo());
            }

            if(schemeList!=null && schemeList.size()>0){
                int selectedPos=0;
                for(int i=0;i<schemeList.size();i++){

                    if(headOfThefamily!=null && headOfThefamily.getSchemeId()!=null&& headOfThefamily.getSchemeId().equalsIgnoreCase(schemeList.get(i).getSchemeId())){
                        AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Head of the family name : "+headOfThefamily.getName()+" Scheme code : "+headOfThefamily.getSchemeId());
                        selectedPos=i;
                        break;
                    }
                }
                schemeSP.setSelection(selectedPos);
                if (headOfThefamily!=null && headOfThefamily.getSchemeNo() != null) {
                    stateHealthIdET.setText(headOfThefamily.getSchemeNo());
                }
            }
            if(rsbyItem.getSchemeId()!=null && !rsbyItem.getSchemeId().equalsIgnoreCase("")) {
                if (schemeList != null && schemeList.size() > 0) {
                    int selectedPos = 0;
                    for (int i = 0; i < schemeList.size(); i++) {
                        if (rsbyItem.getSchemeId() != null && rsbyItem.getSchemeId().equalsIgnoreCase(schemeList.get(i).getSchemeId())) {
                            selectedPos = i;
                            break;
                        }
                    }
                    schemeSP.setSelection(selectedPos);
                    if (rsbyItem.getSchemeNo() != null) {
                        stateHealthIdET.setText(rsbyItem.getSchemeNo());
                    }
                }
            }
        }
        AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Captured urn list :"+urnList.size());
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,urnList);
        urnET.setThreshold(1);
        urnET.setAdapter(adapter);

        schemeSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                stateHealthSchemeLayout.setVisibility(View.GONE);
                if(position==0){

                }else{
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
                // if (rsbyItem != null && rsbyItem.getAadhaarStatus().equalsIgnoreCase("1")) {
                theIntent = new Intent(context, RsbyValidationWithAadharActivity.class);
                /*} else {
                    theIntent = new Intent(context, WithoutAadhaarVerificationActivity.class);
                }*/
                startActivity(theIntent);
                rightTransition();
                finish();
                //backNSubmit();
            }
        });

    }



    private void submitStateHealth(){
        int selectedIndex=schemeSP.getSelectedItemPosition();
        String urnNo=urnET.getText().toString();
        if(selectedIndex==0){
            stateHealthIdET.setText("");
        }else{

            HealthSchemeItem item=schemeList.get(selectedIndex);
            rsbyItem.setSchemeId(item.getSchemeId());
            String schemeId=stateHealthIdET.getText().toString();
            if(!schemeId.equalsIgnoreCase("")) {
                rsbyItem.setSchemeNo(schemeId);
            }else{
                CustomAlert.alertWithOk(context,"Please enter scheme number");
                return;
            }
        }

           /* if(schemeId.equalsIgnoreCase("")){
                CustomAlert.alertWithOk(context,"Please enter Scheme ID");
            }else{*/
        if(urnNo!=null && !urnNo.equalsIgnoreCase("")&& urnNo.length()<17){
            CustomAlert.alertWithOk(context,"Please enter 17-digit URN Number");
            return;
        }
        rsbyItem.setUrnNo(urnNo);
        //  rsbyItem.setStateSchemeCodeAuth("P");
        rsbyItem.setLockedSave(AppConstant.SAVE + "");
        if(selectedMemItem.getOldHeadMember()!=null && selectedMemItem.getNewHeadMember()!=null){
            SeccMemberItem oldHead=selectedMemItem.getOldHeadMember();
            oldHead.setLockedSave(AppConstant.LOCKED+"");
            AppUtility.showLog(AppConstant.LOG_STATUS,TAG," OLD Household name " +
                    ": "+oldHead.getName()+"" +
                    " Member Status "+oldHead.getMemStatus()+" House hold Status :" +
                    " "+oldHead.getHhStatus()+" Locked Save :"+oldHead.getLockedSave());
            SeccDatabase.updateRsbyMember(selectedMemItem.getOldHeadrsbyMemberItem(),context);
        }
        SeccDatabase.updateRsbyMember(rsbyItem,context);
        SeccDatabase.updateRSBYHouseHold(selectedMemItem.getRsbyHouseholdItem(),context);
        // SeccDatabase.getSeccMemberDetail(rsbyItem.getNhpsMemId(),context);
        rsbyItem= SeccDatabase.getRsbyMemberDetail(rsbyItem.getNhpsMemId(),context);

        ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.SELECTED_ITEM_FOR_VERIFICATION,selectedMemItem.serialize(),context);
        //  Log.d("State Health Scheme","Scheme Code auth : "+SeccDatabase.getSeccMemberDetail(rsbyItem.getAhlTin(),context).getStateSchemeCodeAuth());
        Intent theIntent = null;
        //if (rsbyItem != null && rsbyItem.getAadhaarStatus().equalsIgnoreCase("1")) {
        theIntent = new Intent(context, RsbyValidationWithAadharActivity.class);
                /*} else {
                    theIntent = new Intent(context, WithoutAadhaarVerificationActivity.class);

                }*/
        startActivity(theIntent);
        rightTransition();
        finish();
        // }
    }

    private void prepareSchemeSpinner(){
        //  schemeList=new ArrayList<>();
        schemeList= SeccDatabase.getHealthSchemeList(context,selectedLocation.getStateCode().trim());
       /* schemeList.add(new HealthSchemeItem("24","001","Mukhyamantri Amrutam"));
        schemeList.add(new HealthSchemeItem("24","002","MA Vatsalya"));
        Log.d("Health Scheme List", "scheme list : "+schemeList.size());*/
        if(schemeList!=null && schemeList.size()>0) {
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
        }else{
            sstateHealthSchemeLayout.setVisibility(View.GONE);
        }

    }

    private void backNSubmit(){
        Intent theIntent;
      /*  if(navigateType== AppConstant.WITH_AADHAAR){
            theIntent=new Intent(context,WithAadhaarActivity.class);
        }else{
            theIntent=new Intent(context,WithoutAadhaarVerificationActivity.class);
        }
        startActivity(theIntent);*/
        rightTransition();
        finish();
    }



}

