package com.nhpm.rsbyFieldValidation;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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

import com.android.volley.VolleyError;
import com.customComponent.CustomAlert;
import com.customComponent.Networking.CustomVolleyGet;
import com.customComponent.Networking.VolleyTaskListener;
import com.customComponent.utility.DateTimeUtil;
import com.customComponent.utility.ProjectPrefrence;
import com.nhpm.activity.BaseActivity;
import com.nhpm.LocalDataBase.dto.SeccDatabase;
import com.nhpm.Models.response.MobileNumberItem;
import com.nhpm.Models.response.MobileOTPResponse;
import com.nhpm.Models.response.WhoseMobileItem;
import com.nhpm.Models.response.rsbyMembers.RSBYItem;
import com.nhpm.Models.response.rsbyMembers.RsbyHouseholdItem;
import com.nhpm.Models.response.seccMembers.SelectedMemberItem;
import com.nhpm.Models.response.verifier.MobileNumberOTPValidaationResponse;
import com.nhpm.Models.response.verifier.VerifierLoginResponse;
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

public class RsbyMobileNumberCaptureActitivty extends BaseActivity {
    private TextView headerTV;
    private AlertDialog dialog;
    private Context context;
    private Button otpBT,validateLaterBT;
    private LinearLayout mobileValidateLayout;
    private Button submitBt;
    private ImageView backIV;
    private Spinner whoseMobileSP;
    private int navigateType;
    private SelectedMemberItem selectedMemItem;
    private RSBYItem rsbyItem;
    private AutoCompleteTextView mobileNumberET;
    private ArrayList<WhoseMobileItem> spinnerList1;
    private ArrayAdapter<String> maritalAdapter;
    private int memberType;
    private int whoseMobileSelect=0;
    private MobileOTPResponse otpResponse;
    private String TAG="Phone Number Activity";
    private ImageView verified,rejected,pending;
    private VerifierLoginResponse loginResponse;
    private String SELF="0";
    private RsbyHouseholdItem houseHoldItem;
    private boolean isValidMobile;
    private LinearLayout mZoomLinearLayout;
    private ZoomView zoomView;
    private ArrayList<MobileNumberItem> mobileNumberList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dummy_layout_for_zooming);
        setupScreen();
    }

    private void prepareFamilyStatusSpinner(){
        ArrayList<RSBYItem> memberList= SeccDatabase.getRsbyMemberList(houseHoldItem.getUrnId(),context);
        boolean flag=false;
        /*for(SeccMemberItem item : memberList){
            if(item.getWhoseMobile().equalsIgnoreCase(SELF) && !item.getAhlTin().equalsIgnoreCase(rsbyItem.getAhlTin())){
                flag=true;
                break;
            }
        }*/
        spinnerList1=new ArrayList<>();
        spinnerList1.add(new WhoseMobileItem("-1", "Select mobile number belongs to"));
        /*if(flag){
            spinnerList1.add(new WhoseMobileItem("1","Family"));
            spinnerList1.add(new WhoseMobileItem("2","Other"));
        }else {*/
        spinnerList1.add(new WhoseMobileItem("0", "Self"));
        spinnerList1.add(new WhoseMobileItem("1","Family"));
        spinnerList1.add(new WhoseMobileItem("2","Other"));
        // }
        ArrayList<String> spinnerList=new ArrayList<>();
        for(WhoseMobileItem item : spinnerList1){
            spinnerList.add(item.getStatusType());
        }
        ArrayAdapter<String> maritalAdapter = new ArrayAdapter<>(context, R.layout.custom_drop_down, R.id.textView,spinnerList);
        whoseMobileSP.setAdapter(maritalAdapter);

    }
    private void setupScreen(){
        context=this;
        mZoomLinearLayout = (LinearLayout) findViewById(R.id.mZoomLinearLayout);
        View v = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.activity_phone_number, null, false);
        v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));

        loginResponse=(VerifierLoginResponse.create(ProjectPrefrence.
                getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.VERIFIER_CONTENT,context)));
        headerTV=(TextView)v.findViewById(R.id.centertext);
        mobileValidateLayout=(LinearLayout)v.findViewById(R.id.mobileValidateLayout) ;
        mobileNumberET=(AutoCompleteTextView)v.findViewById(R.id.mobileNumberET);
        AppUtility.requestFocus(mobileNumberET);
        otpBT=(Button)v.findViewById(R.id.validateMobileOTPBT);
        validateLaterBT=(Button)v.findViewById(R.id.validateLaterBT) ;
        whoseMobileSP=(Spinner)v.findViewById(R.id.whoseMobileNoSP);
        submitBt=(Button)v.findViewById(R.id.submitBT);
        backIV=(ImageView)v.findViewById(R.id.back);
        verified=(ImageView)v.findViewById(R.id.verifiedIV);
        rejected=(ImageView)v.findViewById(R.id.rejectedIV);
        pending=(ImageView)v.findViewById(R.id.pendingIV);

        zoomView = new ZoomView(this);
        zoomView.addView(v);
        mZoomLinearLayout.addView(zoomView);
        selectedMemItem= SelectedMemberItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.SELECTED_ITEM_FOR_VERIFICATION, context));


        mobileNumberET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().length()>0) {
                    if (Integer.parseInt(charSequence.toString().substring(0, 1)) > 5) {
                        isValidMobile=true;
                        mobileNumberET.setTextColor(AppUtility.getColor(context, R.color.black_shine));
                    } else {
                        isValidMobile=false;
                        mobileNumberET.setTextColor(AppUtility.getColor(context, R.color.red));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        if(selectedMemItem.getRsbyMemberItem()!=null) {
            rsbyItem = selectedMemItem.getRsbyMemberItem();
            houseHoldItem= selectedMemItem.getRsbyHouseholdItem();
            System.out.print(rsbyItem.getName());
            headerTV.setText(rsbyItem.getName());
            ArrayList<RSBYItem> seccMemList= SeccDatabase.getRsbyMemberList(rsbyItem.getUrnNo(),context);
            mobileNumberList=new ArrayList<>();
            for(RSBYItem item : seccMemList){
                if(item.getMobileNo()!=null && !item.getMobileNo().equalsIgnoreCase("")){
                    AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Mobile Number : "+item.getMobileNo());
                    MobileNumberItem item1=new MobileNumberItem();
                    item1.setMobileNumber(item.getMobileNo());
                    item1.setMobileType(item.getWhoseMobile());
                    mobileNumberList.add(item1);
                }
            }
            TreeSet<MobileNumberItem> mobileNoSet = new TreeSet<MobileNumberItem>(new Comparator<MobileNumberItem>(){

                public int compare(MobileNumberItem o1, MobileNumberItem o2) {
                    // return 0 if objects are equal in terms of your properties
                    if (o1.getMobileNumber().equalsIgnoreCase(o2.getMobileNumber())){
                        return 0;
                    }
                    return 1;
                }
            });
            mobileNoSet.addAll(mobileNumberList);
            mobileNumberList=new ArrayList<>();
            mobileNumberList.addAll(mobileNoSet);
            ArrayList<String> list=new ArrayList<>();
            if (rsbyItem.getMobileNo() != null && !rsbyItem.getMobileNo().equalsIgnoreCase("")) {
                mobileNumberET.setText(rsbyItem.getMobileNo());
            }
            for(MobileNumberItem item : mobileNumberList){
                list.add(item.getMobileNumber());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,list);
            mobileNumberET.setThreshold(1);
            mobileNumberET.setAdapter(adapter);
            mobileNumberET.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
                    //... your stuff
                    AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Position : "+position);
                    // AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Position : "+i);
                    MobileNumberItem item= mobileNumberList.get(position);
                    AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Mobile Type : "+item.getMobileType());

                    if(item!=null && item.getMobileType()!=null)
                        if(item.getMobileType().trim().equalsIgnoreCase(AppConstant.SELF)){
                            whoseMobileSP.setSelection(Integer.parseInt(AppConstant.FAMILY)+1);
                        }else if(item.getMobileType().trim().equalsIgnoreCase(AppConstant.FAMILY)){
                            whoseMobileSP.setSelection(Integer.parseInt(AppConstant.FAMILY)+1);
                        }else if(item.getMobileType().trim().equalsIgnoreCase(AppConstant.OTHER)){
                            whoseMobileSP.setSelection(Integer.parseInt(AppConstant.OTHER)+1);
                        }

                }
            });
           /* mobileNumberET.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Position : "+i);
                    MobileNumberItem item= mobileNumberList.get(i);
                    AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Mobile Type : "+item.getMobileType());
                    whoseMobileSP.setSelection(2);

                   *//* if(item!=null && item.getMobileType()!=null)
                    if(item.getMobileType().trim().equalsIgnoreCase(AppConstant.SELF)){
                    }else if(item.getMobileType().trim().equalsIgnoreCase(AppConstant.FAMILY)){
                        whoseMobileSP.setSelection(Integer.parseInt(AppConstant.FAMILY));
                    }else if(item.getMobileType().trim().equalsIgnoreCase(AppConstant.OTHER)){
                        whoseMobileSP.setSelection(Integer.parseInt(AppConstant.OTHER));
                    }*//*
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });*/
            prepareFamilyStatusSpinner();

            if (rsbyItem.getWhoseMobile() != null && !rsbyItem.getWhoseMobile().equalsIgnoreCase("")) {
                for (int i = 0; i < spinnerList1.size(); i++) {
                    if (rsbyItem.getWhoseMobile().equalsIgnoreCase(spinnerList1.get(i).getStatusCode())) {
                        whoseMobileSelect = i;
                        break;
                    }
                }
                whoseMobileSP.setSelection(whoseMobileSelect);
            }
            verified.setVisibility(View.GONE);
            rejected.setVisibility(View.GONE);
            pending.setVisibility(View.GONE);
            if (rsbyItem.getMobileAuth() != null && rsbyItem.getMobileAuth().equalsIgnoreCase(AppConstant.VALID_STATUS)) {
                verified.setVisibility(View.VISIBLE);
            } else if (rsbyItem.getMobileAuth() != null && rsbyItem.getMobileAuth().equalsIgnoreCase(AppConstant.INVALID_STATUS)) {
                rejected.setVisibility(View.VISIBLE);
            } else if (rsbyItem.getMobileAuth() != null && rsbyItem.getMobileAuth().equalsIgnoreCase(AppConstant.PENDING_STATUS)) {
                pending.setVisibility(View.VISIBLE);
            }

        }

        whoseMobileSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                WhoseMobileItem item=  spinnerList1.get(i);
                mobileValidateLayout.setVisibility(View.GONE);
                submitBt.setVisibility(View.GONE);
                if(item.getStatusCode().equalsIgnoreCase("0")){
                    mobileValidateLayout.setVisibility(View.VISIBLE);
                }else if(item.getStatusCode().equalsIgnoreCase("-1")){
                    mobileValidateLayout.setVisibility(View.GONE);
                    submitBt.setVisibility(View.GONE);
                }else{
                    mobileValidateLayout.setVisibility(View.GONE);
                    submitBt.setVisibility(View.VISIBLE);
                    AppUtility.clearFocus(mobileNumberET);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // if(rsbyItem.getAadhaarStatus().equalsIgnoreCase("1")) {
                Intent theIntent = new Intent(context, RsbyValidationWithAadharActivity.class);
                startActivity(theIntent);
                finish();
                rightTransition();
               /* }else{
                    Intent theIntent = new Intent(context, WithoutAadhaarVerificationActivity.class);
                    startActivity(theIntent);
                    finish();
                    rightTransition();
                }*/
            }
        });
        submitBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // String mobile=mobileNumberET.getText().toString();
                /*int selectWhoseMobile=whoseMobileSP.getSelectedItemPosition();
                if(mobile.equalsIgnoreCase(loginResponse.getMobileNumber())){
                    CustomAlert.alertWithOk(context,"You have entered mobile number of validator. Please enter mobile number of household to proceed further.");
                    return;
                }
                if(mobile.equalsIgnoreCase("")){
                    CustomAlert.alertWithOk(context,"Please enter mobile number.");
                    return;
                }else if(mobile.length()<10){
                    CustomAlert.alertWithOk(context,"Please enter valid mobile number");
                    return;
                }else if(selectWhoseMobile==0){
                    CustomAlert.alertWithOk(context,"Please select whose mobile number");
                    return;
                }else{
                    if(rsbyItem.getMobileAuth()!=null && rsbyItem.getMobileAuth().equalsIgnoreCase("")){
                        rsbyItem.setMobileAuth("P");
                    }
                    rsbyItem.setMobileNo(mobile);
                    rsbyItem.setWhoseMobile(spinnerList1.get(selectWhoseMobile).getStatusCode());
                    rsbyItem.setMobileNoSurveyedStat(AppConstant.SURVEYED+"");
                    rsbyItem.setLockedSave(AppConstant.SAVE + "");
                    if(selectedMemItem.getOldHeadMember()!=null){
                        SeccMemberItem oldHead=selectedMemItem.getOldHeadMember();
                        oldHead.setLockedSave(AppConstant.LOCKED+"");
                        AppUtility.showLog(AppConstant.LOG_STATUS,TAG," OLD Household name " +
                                ": "+oldHead.getName()+"" +
                                " Member Status "+oldHead.getMemStatus()+" House hold Status :" +
                                " "+oldHead.getHhStatus()+" Locked Save :"+oldHead.getLockedSave());
                        SeccDatabase.updateSeccMember(selectedMemItem.getOldHeadMember(),context);
                    }
                    SeccDatabase.updateSeccMember(rsbyItem,context);
                    SeccDatabase.updateHouseHold(houseHoldItem,context);
                    rsbyItem=SeccDatabase.getSeccMemberDetail(rsbyItem.getNhpsMemId(),context);
                    selectedMemItem.setSeccMemberItem(rsbyItem);
                    ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                            AppConstant.SELECTED_ITEM_FOR_VERIFICATION,selectedMemItem.serialize(),context);
                   // if(rsbyItem.getAadhaarStatus().equalsIgnoreCase("1")) {
                        Intent theIntent = new Intent(context, WithAadhaarActivity.class);
                        startActivity(theIntent);
                        finish();
                        rightTransition();
                    *//*}else{*//**//*
                        Intent theIntent = new Intent(context, WithoutAadhaarVerificationActivity.class);
                        startActivity(theIntent);
                        finish();
                        rightTransition();
                   // }
*//*
                }*/

                String mobile=mobileNumberET.getText().toString();
                //AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Mobile Number Index : "+Integer.parseInt(mobileNumber.substring(0,1)));
                rsbyItem.setMobileAuth(AppConstant.PENDING_STATUS);

                if(mobile.equalsIgnoreCase("") || mobile.length()<10) {
                    CustomAlert.alertWithOk(context,"Please enter 10-digit mobile number");
                    return;
                }
                if(!isValidMobile){
                    CustomAlert.alertWithOk(context,"Please enter valid mobile number");
                    return;
                }
               /* if (mobile.equalsIgnoreCase(loginResponse.getMobileNumber())) {
                    CustomAlert.alertWithOk(context, "You have entered mobile number of validator. Please enter mobile number of household to proceed further.");
                    return;
                }*/
                submitMobile();

            }
        });
        /*backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rsbyItem.getAadhaarStatus().equalsIgnoreCase("1")) {
                    Intent theIntent = new Intent(context, WithAadhaarActivity.class);
                    startActivity(theIntent);
                    rightTransition();
                    finish();
                }else{
                    Intent theIntent = new Intent(context, WithoutAadhaarVerificationActivity.class);
                    startActivity(theIntent);
                    finish();
                    rightTransition();
                   //without aadhaar
                }
            }
        });*/
        validateLaterBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rsbyItem.setMobileAuth(AppConstant.PENDING_STATUS);
                String mobile=mobileNumberET.getText().toString();
                //AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Mobile Number Index : "+Integer.parseInt(mobileNumber.substring(0,1)));

                if(mobile.equalsIgnoreCase("") || mobile.length()<10) {
                    CustomAlert.alertWithOk(context,"Please enter 10-digit mobile number");
                    return;
                }
                AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Mobile Number Index : "+isValidMobile);
                if(!isValidMobile){
                    CustomAlert.alertWithOk(context,"Please enter valid mobile number");
                    return;
                }

                // commented by saurabh for bypassing mobile no.validation
              /*  if (mobile.equalsIgnoreCase(loginResponse.getMobileNumber())) {
                    CustomAlert.alertWithOk(context, "You have entered mobile number of validator. Please enter mobile number of household to proceed further.");
                    return;
                }*/
                submitMobile();

               /* if(isValidMobile){

                }else{
                    CustomAlert.alertWithOk(context,"Please enter valid mobile number");
                }*/

            }
        });
        otpBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobile=mobileNumberET.getText().toString().trim();
                //  rsbyItem.setMobileAuth(AppConstant.PENDING_STATUS);
                //String mobileNumber=mobileNumberET.getText().toString();
                //AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Mobile Number Index : "+mobile.length());

                if(mobile.equalsIgnoreCase("") || mobile.length()<10) {
                    CustomAlert.alertWithOk(context,"Please enter 10-digit mobile number");
                    return;
                }
                AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Mobile Number Index : "+isValidMobile);
                if(!isValidMobile){
                    CustomAlert.alertWithOk(context,"Please enter valid mobile number");
                    return;
                }
                if (mobile.equalsIgnoreCase(loginResponse.getMobileNumber())) {
                    CustomAlert.alertWithOk(context, "You have entered mobile number of validator. Please enter mobile number of household to proceed further.");
                    return;
                }
                if(isNetworkAvailable()) {
                    requestToOTP(mobile);
                }else{
                    CustomAlert.alertWithOk(context,getResources().getString(R.string.internet_connection_msg));
                }

            }
        });

    }
    private void submitMobile() {
        String mobile = mobileNumberET.getText().toString();
        int selectWhoseMobile = whoseMobileSP.getSelectedItemPosition();
       /* if (mobile.equalsIgnoreCase(loginResponse.getMobileNumber())) {
            CustomAlert.alertWithOk(context, "You have entered mobile number of validator. Please enter mobile number of household to proceed further.");
            return;
        }*/
        if (mobile.equalsIgnoreCase("")) {
            CustomAlert.alertWithOk(context, "Please enter mobile number.");
            return;
        } else if (mobile.length() < 10) {
            CustomAlert.alertWithOk(context, "Please enter valid mobile number");
            return;
        } else if (selectWhoseMobile == 0) {
            CustomAlert.alertWithOk(context, "Please select whose mobile number");
            return;
        } else {
            if (rsbyItem.getMobileAuth() != null && rsbyItem.getMobileAuth().equalsIgnoreCase("")) {
                rsbyItem.setMobileAuth("P");
            }
            rsbyItem.setMobileNo(mobile);
            rsbyItem.setWhoseMobile(spinnerList1.get(selectWhoseMobile).getStatusCode());
            rsbyItem.setMobileNoSurveyedStat(AppConstant.SURVEYED + "");
            rsbyItem.setLockedSave(AppConstant.SAVE + "");
            if (selectedMemItem.getOldHeadrsbyMemberItem() != null && selectedMemItem.getNewHeadrsbyMemberItem()!=null) {
                RSBYItem oldHead = selectedMemItem.getOldHeadrsbyMemberItem();
                oldHead.setLockedSave(AppConstant.LOCKED + "");
                AppUtility.showLog(AppConstant.LOG_STATUS, TAG, " OLD Household name " +
                        ": " + oldHead.getName() + "" +
                        " Member Status " + oldHead.getMemStatus() + " House hold Status :" +
                        " " + oldHead.getHhStatus() + " Locked Save :" + oldHead.getLockedSave());
                SeccDatabase.updateRsbyMember(selectedMemItem.getOldHeadrsbyMemberItem(), context);
            }
            SeccDatabase.updateRsbyMember(rsbyItem, context);
            SeccDatabase.updateRSBYHouseHold(houseHoldItem, context);
            rsbyItem = SeccDatabase.getRsbyMemberDetail(rsbyItem.getRsbyMemId(), context);
            selectedMemItem.setRsbyMemberItem(rsbyItem);
            ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                    AppConstant.SELECTED_ITEM_FOR_VERIFICATION, selectedMemItem.serialize(), context);
            // if(rsbyItem.getAadhaarStatus().equalsIgnoreCase("1")) {
            Intent theIntent = new Intent(context, RsbyValidationWithAadharActivity.class);
            startActivity(theIntent);
            finish();
            rightTransition();
        }
    }

    private void popupForOTP(final String mobileNumber,String OTP){
        dialog = new AlertDialog.Builder(context).create();
        LayoutInflater factory = LayoutInflater.from(context);
        View alertView = factory.inflate(R.layout.opt_auth_layout, null);
        dialog.setView(alertView);
        dialog.setCancelable(false);
        // dialog.setContentView(R.layout.opt_auth_layout);
        final TextView otpAuthMsg=(TextView)alertView.findViewById(R.id.otpAuthMsg);
        otpAuthMsg.setVisibility(View.INVISIBLE);
        Button okButton= (Button) alertView.findViewById(R.id.ok);
        Button resendBT=(Button)alertView.findViewById(R.id.resendBT);
        Button cancelBT=(Button)alertView.findViewById(R.id.cancelBT);
        final EditText optET= (EditText) alertView.findViewById(R.id.otpET);
        // optET.setText(OTP);
        //  optET.setText("4040");
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otp = optET.getText().toString();
                //  otpAuthMsg.setVisibility(View.GONE);
                if (!otp.equalsIgnoreCase("")) {
                    //  updatedVersionApp();
                    if(otpResponse.getOtp().equalsIgnoreCase(otp)) {
                        validateOTP(otp, otpAuthMsg);
                    }else{
                        otpAuthMsg.setText("Enter valid otp");
                        otpAuthMsg.setTextColor(AppUtility.getColor(context, R.color.red));
                        otpAuthMsg.setVisibility(View.VISIBLE);
                    }
                    // dialog.dismiss();
                } else {
                    otpAuthMsg.setText("Enter OTP");
                    otpAuthMsg.setTextColor(AppUtility.getColor(context, R.color.red));
                    otpAuthMsg.setVisibility(View.VISIBLE);
                }

            }
        });
        cancelBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // rsbyItem.setMobileAuth("P");
                dialog.dismiss();
            }
        });
        resendBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                requestToOTP(mobileNumber);
            }
        });
        dialog.show();
    }
    private void validateOTP(String otp, final TextView authOtpTV){
        VolleyTaskListener taskListener=new VolleyTaskListener() {
            @Override
            public void postExecute(String response) {
                MobileNumberOTPValidaationResponse mobile= MobileNumberOTPValidaationResponse.create(response);
                verified.setVisibility(View.GONE);
                rejected.setVisibility(View.GONE);
                pending.setVisibility(View.GONE);
                if(mobile!=null && mobile.getMessage().trim().equalsIgnoreCase("Y")){
                    rsbyItem.setMobileAuth(AppConstant.VALID_STATUS);
                    rsbyItem.setMobileAuthDt(String.valueOf(DateTimeUtil.currentTimeMillis()));
                    verified.setVisibility(View.VISIBLE);
                    submitMobile();
                    dialog.dismiss();
                }else if(mobile!=null && mobile.getMessage().trim().equalsIgnoreCase("N")){
                    authOtpTV.setText("Invalid OTP");
                    authOtpTV.setTextColor(AppUtility.getColor(context, R.color.red));
                    //rsbyItem.setMobileAuth("N");
                    // rsbyItem.setMobileAuth("Y");
                }

            }

            @Override
            public void onError(VolleyError error) {
                CustomAlert.alertWithOk(context,getResources().getString(R.string.slow_internet_connection_msg));
                dialog.dismiss();
            }
        };
        String validateOTP= AppConstant.AUTH_MOBILE_OTP+otpResponse.getSender()
                +"/"+otpResponse.getSequenceNo()+"/"+otp+ AppConstant.MOBILE_OTP_USERID+ AppConstant.MOBILE_OTP_PASS;
        Log.d(TAG,"OTP Validate API : "+validateOTP);
        CustomVolleyGet volley=new CustomVolleyGet(taskListener,"Please wait..",validateOTP.trim(),context);
        volley.execute();
    }
    private void requestToOTP(final String mobileNumber){
        VolleyTaskListener taskListener=new VolleyTaskListener() {
            @Override
            public void postExecute(String response) {
                Log.d(TAG,"Mobile Number OTP");
                otpResponse= MobileOTPResponse.create(response);
                if(otpResponse!=null && otpResponse.getOtp()!=null && !otpResponse.getOtp().equalsIgnoreCase("")){
                    popupForOTP(mobileNumber,otpResponse.getOtp());
                }else{
                    CustomAlert.alertWithOk(context,getResources().getString(R.string.server_error));
                }
            }
            @Override
            public void onError(VolleyError error) {
                CustomAlert.alertWithOk(context,getResources().getString(R.string.server_error));
            }
        };
        String otpRequestUrl= AppConstant.MOBILE_OTP_REQUEST+mobileNumber+
                AppConstant.MOBILE_OTP_USERID+ AppConstant.MOBILE_OTP_PASS;
        Log.d(TAG,"OTP Request API : "+otpRequestUrl);
        CustomVolleyGet volley=new CustomVolleyGet(taskListener,"Please wait..",otpRequestUrl,context);
        volley.execute();
    }

}
