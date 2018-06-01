package com.nhpm.rsbyFieldValidation;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.customComponent.CustomAlert;
import com.customComponent.utility.ProjectPrefrence;
import com.nhpm.activity.BaseActivity;
import com.nhpm.CameraUtils.CommonUtilsImageCompression;
import com.nhpm.CameraUtils.squarecamera.CameraActivity;
import com.nhpm.LocalDataBase.DatabaseHelpers;
import com.nhpm.LocalDataBase.dto.SeccDatabase;
import com.nhpm.Models.response.GovernmentIdItem;
import com.nhpm.Models.response.rsbyMembers.RSBYItem;
import com.nhpm.Models.response.seccMembers.SelectedMemberItem;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;
import com.nhpm.Utility.AppUtility;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import pl.polidea.view.ZoomView;

/**
 * Created by DELL on 26-02-2017.
 */

public class RsbyGovermentIdCaptureActivity extends BaseActivity {
    private Spinner govtIdSP;
    private Context context;
    private ArrayList<GovernmentIdItem> govtIdStatusList;
    private LinearLayout voterIdLayout,rationCardLayout,govtIdPhotoLayout;
    private TextView headerTV;
    private ImageView rashanCardIV,voterIdIV;
    private ImageView voterClickIV,rashanCardClickIV;
    private int CAMERA_PIC_REQUEST=0;
    private LinearLayout rashanCardCaptureLayout,voterIdCaptureLayout,enrollmentLayout;
    private Bitmap captureImageBM;
    private ImageView backIV;
    private Button rationCardSubmitBT,voterIdSubmitBT,enrollmentSubmitBT;
    private int navigateType;
    private SelectedMemberItem selectedMemItem;
    private RSBYItem rsbyItem;
    private String aadhaarStatus="";
    private GovernmentIdItem item;
    private EditText rationCardNameET,rationCardNumberET,voterIdCardNameET,
            voterIdCardNumberET,enrollmentNameET,enrollmentIdET;
    private AlertDialog internetDiaolg;
    private String voterIdImg,rashanCardImg;
    private int selectedId=0;
    private int RASHAN_CARD_REQUEST=1;
    private int VOTER_ID_REQUEST=2;
    private String consent="y";
    private final String TAG="Government Activity";
    private final int ENROLLMENT_ID=1,VOTER_ID=2,RASHAN_CARD=3,NREGA=4,DRIVIG_LICENCE=5,BIRTH_CERT=6,OTHER_CARD=7;
    private String nameAsIsinID,numberAsID;
    private CheckBox termCB;
    private LinearLayout mZoomLinearLayout;
    private ZoomView zoomView;
    private Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dummy_layout_for_zooming);
        setupScreen();
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //   if(data!=null) {

        if(resultCode != Activity.RESULT_CANCELED) {
            if (requestCode == CAMERA_PIC_REQUEST) {
               /* Uri imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),"govtIdPhoto" +".jpg"));
                try {
                    captureImageBM = MediaStore.Images.Media.getBitmap(this.getContentResolver(),imageUri); //(Bitmap)imageUri;//data.getExtras().get("data");
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
                Uri fileUri = Uri.fromFile(new File(DatabaseHelpers.DELETE_FOLDER_PATH,
                        context.getString(R.string.squarecamera__app_name) + "/photoCapture/IMG_12345.jpg"));
                Uri compressedUri = Uri.fromFile(new File(CommonUtilsImageCompression.compressImage(fileUri.getPath(), context, "/photoCapture")));
                //  captureImageBM=(Bitmap)data.getExtras().get("data");
                try {
                    captureImageBM = MediaStore.Images.Media.getBitmap(this.getContentResolver(), compressedUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // Log.d(TAG," Bitmap Size : "+image.getAllocationByteCount());
                voterIdImg = AppUtility.convertBitmapToString(captureImageBM);
                updateScreen(voterIdImg);
            }
        }else{
            if (requestCode == VOTER_ID_REQUEST) {
                Uri imageUri = Uri.fromFile(new File(DatabaseHelpers.DELETE_FOLDER_PATH, context.getString(R.string.squarecamera__app_name) + "/govtIdPhoto/IMG_12345.jpg"));
                Uri compressedUri = Uri.fromFile(new File(CommonUtilsImageCompression.compressImage(imageUri.getPath(), context, "/govtIdPhoto")));
                try {
                    captureImageBM = MediaStore.Images.Media.getBitmap(this.getContentResolver(), compressedUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // Log.d(TAG," Bitmap Size : "+image.getAllocationByteCount());
                voterIdImg = AppUtility.convertBitmapToString(captureImageBM);
                updateScreen(voterIdImg);
            }
        }
    }
    private void deleteDir(File file) {

        if (file.isDirectory()) {
            String[] children = file.list();
            for (String child : children) {
                if (child.endsWith(".jpg") || child.endsWith(".jpeg"))
                    new File(file, child).delete();
            }
            file.delete();
        }
        Intent mediaScannerIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri fileContentUri = Uri.fromFile(file);
        mediaScannerIntent.setData(fileContentUri);
        context.sendBroadcast(mediaScannerIntent);
    }
    private void setupScreen(){
        context=this;
        activity = this;
        mZoomLinearLayout = (LinearLayout) findViewById(R.id.mZoomLinearLayout);
        View v = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.activity_goverment_idcapture, null, false);
        v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));

        headerTV=(TextView)v.findViewById(R.id.centertext);
        selectedMemItem= SelectedMemberItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.SELECTED_ITEM_FOR_VERIFICATION, context));
        if(selectedMemItem.getRsbyMemberItem()!=null){
            rsbyItem = selectedMemItem.getRsbyMemberItem();
            System.out.print(rsbyItem.getName());
        }
        govtIdSP=(Spinner)v.findViewById(R.id.govtIdSP);
        prepareGovernmentIdSpinner();
        voterIdIV=(ImageView)v.findViewById(R.id.voterIdIV);
        rashanCardIV=(ImageView)v.findViewById(R.id.rationCardIV);
        backIV=(ImageView)v.findViewById(R.id.back);
        rationCardLayout=(LinearLayout)v.findViewById(R.id.rationCardLayout);
        voterIdLayout=(LinearLayout)v.findViewById(R.id.voterIdLayout);
        voterIdCaptureLayout=(LinearLayout)v.findViewById(R.id.voterIdCaptureLayout);
        rashanCardCaptureLayout=(LinearLayout)v.findViewById(R.id.rashanCardCaptureLayout);
        govtIdPhotoLayout=(LinearLayout)v.findViewById(R.id.govtIdPhotoLayout);
        enrollmentLayout=(LinearLayout)v.findViewById(R.id.enrollmentLayout);
        headerTV.setText("Government Id Detail");
        rationCardSubmitBT=(Button)v.findViewById(R.id.rationCardSubmitBT);
        voterIdSubmitBT=(Button)v.findViewById(R.id.voterIdSubmitBT);
        enrollmentSubmitBT=(Button)v.findViewById(R.id.enrollmentIDSubmitBT);
        rationCardNameET=(EditText)v.findViewById(R.id.rationCardNameET);
        rationCardNumberET=(EditText)v.findViewById(R.id.rationCardNumberET);
        termCB=(CheckBox)v.findViewById(R.id.termsCB) ;

        voterIdCardNumberET=(EditText)v.findViewById(R.id.voterIdCardNumberET);
        voterIdCardNameET=(EditText)v.findViewById(R.id.voterIdCardNameET);

        enrollmentIdET=(EditText)v.findViewById(R.id.enrollmentIdET);
        enrollmentNameET=(EditText)v.findViewById(R.id.enrollmentNameET);
        zoomView = new ZoomView(this);
        zoomView.addView(v);
        mZoomLinearLayout.addView(zoomView);
        termCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!b){
                    consent="N";
                }else {
                    consent="Y";
                }
            }
        });

        voterIdSubmitBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String voterIdNumber=voterIdCardNumberET.getText().toString();
                String voterIdName=voterIdCardNameET.getText().toString();
                if(voterIdNumber.equalsIgnoreCase("")){
                    CustomAlert.alertWithOk(context,"Please enter Govt id number");
                    return;
                }
                if(voterIdName.equalsIgnoreCase("")){
                    CustomAlert.alertWithOk(context,"Please enter name as in Govt id");
                    return;
                }
            /*    if(consent.equalsIgnoreCase("N")){
                    CustomAlert.alertWithOk(context,"Please tick Name in SECC and name in govt. id are of same person");
                    return;
                }*/
                if(item.statusCode!=0) {
                    if(voterIdImg==null) {
                        CustomAlert.alertWithOk(context, "Please capture Govt id photo");
                        return;
                    }
                }
                alertForValidateLater(voterIdNumber,voterIdName);

            }



        });
      /*  enrollmentSubmitBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String enrollmentId=enrollmentIdET.getText().toString();
                String nameAsInId=enrollmentNameET.getText().toString();
                if(enrollmentId.equalsIgnoreCase("")){
                    CustomAlert.alertWithOk(context,"Please enter enrollment id");
                }else if(nameAsInId.equalsIgnoreCase("")){
                    CustomAlert.alertWithOk(context,"Please enter name as in id");
                }else {
                    if (rsbyItem != null) {
                    //    rsbyItem.setAadhaarStatus(aadhaarStatus);
                        rsbyItem.setIdType(item.statusCode + "");
                       // Log.d(TAG,"Enrollment Id :"+enrollmentId);
                        rsbyItem.setIdNo(enrollmentId);
                        rsbyItem.setNameAsId(nameAsInId);
                        rsbyItem.setGovtIdPhoto("enrollement");
                        rsbyItem.setGovtIdSurveyedStat(AppConstant.SURVEYED+"");
                        rsbyItem.setLockedSave(AppConstant.SAVE + "");
                        SeccDatabase.updateSeccMember(rsbyItem,context);
                        SeccDatabase.updateHouseHold(selectedMemItem.getHouseHoldItem(),context);
                        rsbyItem=SeccDatabase.getSeccMemberDetail(rsbyItem.getNhpsMemId(),context);
                        //  Log.d(TAG,"")
                        selectedMemItem.setSeccMemberItem(rsbyItem);
                        ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                                AppConstant.SELECTED_ITEM_FOR_VERIFICATION, selectedMemItem.serialize(), context);

                      //  if (rsbyItem.getAadhaarStatus().equalsIgnoreCase("1")) {
                            Intent theIntent = new Intent(context, WithAadhaarActivity.class);
                            startActivity(theIntent);
                            finish();
                            rightTransition();
                        *//*} else {
                            Intent theIntent = new Intent(context, WithoutAadhaarVerificationActivity.class);
                            startActivity(theIntent);
                            finish();
                            rightTransition();
                        }*//*
                    }
                }
            }
        });*/

        voterIdCaptureLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtility.capturingType = AppConstant.capturingModeGovId;
              /*  Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
              //  Uri imageUri = Uri.fromFile(new File(DatabaseHelpers.DELETE_FOLDER_PATH,context.getString(R.string.squarecamera__app_name)+"/govtIdPhoto" +".jpg"));
                Uri imageUri = Uri.fromFile(new File(DatabaseHelpers.DELETE_FOLDER_PATH,context.getString(R.string.squarecamera__app_name)+"/govtIdPhoto/IMG_12345.jpg"));
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(cameraIntent, 2);*/
                File mediaStorageDir = new File(
                        DatabaseHelpers.DELETE_FOLDER_PATH,
                        context.getString(R.string.squarecamera__app_name)+"/photoCapture"
                );

                if(mediaStorageDir.exists()){
                    deleteDir(mediaStorageDir);
                }
                Intent startCustomCameraIntent = new Intent(context, CameraActivity.class);
                startActivityForResult(startCustomCameraIntent, CAMERA_PIC_REQUEST);

            }
        });
        rashanCardCaptureLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent,RASHAN_CARD_REQUEST);
            }
        });

        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // backNSubmit();
                // if(rsbyItem.getAadhaarStatus().equalsIgnoreCase("1")) {
                Intent theIntent = new Intent(context, RsbyValidationWithAadharActivity.class);
                startActivity(theIntent);
                finish();
                rightTransition();
                /*}else{
                    Intent theIntent = new Intent(context, WithoutAadhaarVerificationActivity.class);
                    startActivity(theIntent);
                    finish();
                    rightTransition();
                }*/

            }
        });


        govtIdSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // AadhaarStatusItem item=aadhaarStatusList.get(position);
                item = govtIdStatusList.get(position);
                AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Setup screen Selected id"+item.statusCode);
                switch (item.statusCode) {


                    case 0:
                        voterIdLayout.setVisibility(View.GONE);
                        govtIdPhotoLayout.setVisibility(View.GONE);
                        /*voterIdCardNumberET.setText("");
                        voterIdCardNameET.setText("");*/
                        //aadhaarStatus="";
                        // voterIdImg=null;
                        //  updateScreen(voterIdImg);
                        break;
                    case ENROLLMENT_ID:
                        // voterIdImg=null;
                        // updateScreen(voterIdImg);
                        voterIdCardNumberET.requestFocus();
                        AppUtility.showSoftInput(activity);
                        voterIdLayout.setVisibility(View.VISIBLE);
                        govtIdPhotoLayout.setVisibility(View.VISIBLE);
                        /*voterIdCardNumberET.setText("");
                        voterIdCardNameET.setText("");*/
                        //updateScreen(voterIdImg);
                        voterIdCardNumberET.setHint("Enter 24-digit Enrollment ID");
                        voterIdCardNameET.setHint("Enter Name as in Enrollment ID");
                        voterIdCardNameET.setText(rsbyItem.getName());
                        voterIdCardNameET.setFilters(new InputFilter[]{new InputFilter.LengthFilter(24)});
                        if(rsbyItem.getIdType()!=null) {
                            if (rsbyItem.getIdType().equalsIgnoreCase(ENROLLMENT_ID + "")) {
                                if (rsbyItem.getIdNo() != null && !rsbyItem.getIdNo().equalsIgnoreCase("")) {
                                    voterIdCardNameET.setText(rsbyItem.getNameAsId());
                                    voterIdCardNumberET.setText(rsbyItem.getIdNo());
                                } else {
                                    voterIdCardNameET.setText(rsbyItem.getName());
                                }
                            }
                        }
                       /* rationCardLayout.setVisibility(View.GONE);
                        enrollmentLayout.setVisibility(View.VISIBLE);*/
                        //aadhaarStatus="1";
                        break;
                    case VOTER_ID:
                        AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Setup screen Selected id");
                        voterIdImg=null;
                        voterIdLayout.setVisibility(View.VISIBLE);
                        govtIdPhotoLayout.setVisibility(View.VISIBLE);
                        voterIdCardNumberET.requestFocus();
                        voterIdCardNumberET.setText("");
                        voterIdCardNameET.setText("");
                        // updateScreen(voterIdImg);
                        voterIdCardNumberET.setHint("Enter voter id number");
                        voterIdCardNameET.setHint("Enter Name as in Voter ID");
                        voterIdCardNameET.setText(rsbyItem.getName());
                       /* rationCardLayout.setVisibility(View.GONE);
                        enrollmentLayout.setVisibility(View.GONE);*/
                        // aadhaarStatus="2";
                        if(rsbyItem.getIdType()!=null) {
                            if (rsbyItem.getIdType().equalsIgnoreCase(VOTER_ID + "")) {
                                voterIdImg = rsbyItem.getGovtIdPhoto();
                                if (rsbyItem.getIdNo() != null && !rsbyItem.getIdNo().equalsIgnoreCase("")) {
                                    voterIdCardNameET.setText(rsbyItem.getNameAsId());
                                    voterIdCardNumberET.setText(rsbyItem.getIdNo());
                                } else {
                                    voterIdCardNameET.setText(rsbyItem.getName());
                                }
                                voterIdCardNumberET.requestFocus();
                                AppUtility.showSoftInput(activity);
                            }
                        }
                        break;
                    case RASHAN_CARD:
                        voterIdImg=null;
                        voterIdLayout.setVisibility(View.VISIBLE);
                        govtIdPhotoLayout.setVisibility(View.VISIBLE);
                        //  updateScreen(voterIdImg);
                        voterIdCardNumberET.requestFocus();
                        AppUtility.showSoftInput(activity);
                        voterIdCardNumberET.setText("");
                        voterIdCardNameET.setText("");
                        voterIdCardNumberET.setHint("Enter Ration card number");
                        voterIdCardNameET.setHint("Enter Name as in Ration Card");
                        voterIdCardNameET.setText(rsbyItem.getName());
                       /* rationCardLayout.setVisibility(View.VISIBLE);
                        enrollmentLayout.setVisibility(View.GONE);*/
                        //aadhaarStatus="3";
                        if(rsbyItem.getIdType()!=null) {
                            if (rsbyItem.getIdType().equalsIgnoreCase(RASHAN_CARD + "")) {
                                voterIdImg = rsbyItem.getGovtIdPhoto();
                                if (rsbyItem.getIdNo() != null && !rsbyItem.getIdNo().equalsIgnoreCase("")) {
                                    voterIdCardNameET.setText(rsbyItem.getNameAsId());
                                    voterIdCardNumberET.setText(rsbyItem.getIdNo());
                                } else {
                                    voterIdCardNameET.setText(rsbyItem.getName());
                                }
                            }
                        }
                        break;
                    case NREGA:
                        voterIdImg=null;
                        // updateScreen(voterIdImg);
                        voterIdLayout.setVisibility(View.VISIBLE);
                        govtIdPhotoLayout.setVisibility(View.VISIBLE);
                        voterIdCardNumberET.setText("");
                        voterIdCardNameET.setText("");
                        voterIdCardNumberET.setHint("Enter NREGA job card number");
                        voterIdCardNameET.setHint("Ente Name as in NREGA job card");
                        voterIdCardNameET.setText(rsbyItem.getName());
                        voterIdCardNumberET.requestFocus();
                        AppUtility.showSoftInput(activity);
                        if(rsbyItem.getIdType()!=null) {
                            if (rsbyItem.getIdType().equalsIgnoreCase(NREGA + "")) {
                                voterIdImg = rsbyItem.getGovtIdPhoto();
                                if (rsbyItem.getIdNo() != null && !rsbyItem.getIdNo().equalsIgnoreCase("")) {
                                    voterIdCardNameET.setText(rsbyItem.getNameAsId());
                                    voterIdCardNumberET.setText(rsbyItem.getIdNo());
                                } else {
                                    voterIdCardNameET.setText(rsbyItem.getName());
                                }
                            }
                        }
                        break;
                    case DRIVIG_LICENCE:
                        voterIdImg=null;
                        // updateScreen(voterIdImg);
                        voterIdLayout.setVisibility(View.VISIBLE);
                        govtIdPhotoLayout.setVisibility(View.VISIBLE);
                        voterIdCardNumberET.requestFocus();
                        AppUtility.showSoftInput(activity);
                        /*voterIdCardNumberET.setText("");

                        voterIdCardNameET.setText("");*/
                        voterIdCardNumberET.setText("");
                        voterIdCardNameET.setText("");
                        voterIdCardNumberET.setHint("Enter  Driving License number");
                        voterIdCardNameET.setHint("Enter  Name as in Driving License");
                        voterIdCardNameET.setText(rsbyItem.getName());
                        if(rsbyItem.getIdType()!=null) {
                            if (rsbyItem.getIdType().equalsIgnoreCase(DRIVIG_LICENCE + "")) {
                                voterIdImg = rsbyItem.getGovtIdPhoto();
                                if (rsbyItem.getIdNo() != null && !rsbyItem.getIdNo().equalsIgnoreCase("")) {
                                    voterIdCardNameET.setText(rsbyItem.getNameAsId());
                                    voterIdCardNumberET.setText(rsbyItem.getIdNo());
                                } else {
                                    voterIdCardNameET.setText(rsbyItem.getName());
                                }
                            }
                        }
                        break;
                    case BIRTH_CERT:
                        voterIdImg=null;
                        // updateScreen(voterIdImg);
                        voterIdLayout.setVisibility(View.VISIBLE);
                        govtIdPhotoLayout.setVisibility(View.VISIBLE);
                        voterIdCardNumberET.setText("");
                        voterIdCardNameET.setText("");
                        voterIdCardNumberET.setText("");
                        voterIdCardNameET.setText("");
                        voterIdCardNumberET.requestFocus();
                        AppUtility.showSoftInput(activity);
                        voterIdCardNumberET.setHint("Enter Birth Certificate Number");
                        voterIdCardNameET.setHint("Enter  Name as in Birth Certificate");
                        voterIdCardNameET.setText(rsbyItem.getName());
                        if(rsbyItem.getIdType()!=null){
                        if(rsbyItem.getIdType().equalsIgnoreCase(BIRTH_CERT+"")) {
                            voterIdImg = rsbyItem.getGovtIdPhoto();
                            if (rsbyItem.getIdNo() != null && !rsbyItem.getIdNo().equalsIgnoreCase("")) {
                                voterIdCardNameET.setText(rsbyItem.getNameAsId());
                                voterIdCardNumberET.setText(rsbyItem.getIdNo());
                            } else {
                                voterIdCardNameET.setText(rsbyItem.getName());
                            }
                        }
                        }
                        break;
                    case OTHER_CARD:
                        voterIdImg=null;
                        // updateScreen(voterIdImg);
                        voterIdLayout.setVisibility(View.VISIBLE);
                        govtIdPhotoLayout.setVisibility(View.VISIBLE);
                        voterIdCardNumberET.setText("");
                        voterIdCardNameET.setText("");
                        voterIdCardNumberET.setText("");
                        voterIdCardNameET.setText("");
                        voterIdCardNumberET.requestFocus();
                        AppUtility.showSoftInput(activity);
                        voterIdCardNumberET.setHint("Enter ID Number");
                        voterIdCardNameET.setHint("Enter  Name as in ID");
                        voterIdCardNameET.setText(rsbyItem.getName());
                        if(rsbyItem.getIdType()!=null) {
                            if (rsbyItem.getIdType().equalsIgnoreCase(OTHER_CARD + "")) {
                                voterIdImg = rsbyItem.getGovtIdPhoto();
                                if (rsbyItem.getIdNo() != null && !rsbyItem.getIdNo().equalsIgnoreCase("")) {
                                    voterIdCardNameET.setText(rsbyItem.getNameAsId());
                                    voterIdCardNumberET.setText(rsbyItem.getIdNo());
                                } else {
                                    voterIdCardNameET.setText(rsbyItem.getName());
                                }
                            }
                        }
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        if(selectedMemItem.getRsbyMemberItem()!=null) {
            rsbyItem = selectedMemItem.getRsbyMemberItem();
            headerTV.setText(rsbyItem.getName());
            setupSeccData(rsbyItem);
        }else{

        }

    }

    private void updateScreen(String idImage){
        try {
            if(idImage!=null) {
                voterIdIV.setImageBitmap(AppUtility.convertStringToBitmap(idImage));
            }
        }catch (Exception e){

        }
    }
    private void prepareGovernmentIdSpinner(){
        govtIdStatusList= AppUtility.prepareGovernmentIdSpinner();
        ArrayList<String> spinnerList=new ArrayList<>();
        /*govtIdStatusList.add(new GovernmentIdItem(0,"Select Govt.ID"));
        govtIdStatusList.add(new GovernmentIdItem(1,"Aadhaar Enrollment ID"));
        govtIdStatusList.add(new GovernmentIdItem(2,"Voter ID Card"));
        govtIdStatusList.add(new GovernmentIdItem(3,"Ration Card"));
        govtIdStatusList.add(new GovernmentIdItem(4,"NREGA job card"));
        govtIdStatusList.add(new GovernmentIdItem(5,"Driving License"));*/
        for(GovernmentIdItem item : govtIdStatusList){
            spinnerList.add(item.status);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.custom_drop_down, R.id.textView,spinnerList);
        govtIdSP.setAdapter(adapter);
    }
    private void backNSubmit(){
        /*Intent theIntent;
        if(navigateType== AppConstant.WITH_AADHAAR){
            theIntent=new Intent(context,WithAadhaarActivity.class);
        }else{
            theIntent=new Intent(context,WithoutAadhaarVerificationActivity.class);
        }
        startActivity(theIntent);
        rightTransition();
        finish();*/
    }
    private void setupSeccData(RSBYItem rsbyItem){

        if(rsbyItem!=null & rsbyItem.getIdType()!=null && !rsbyItem.getIdType().equalsIgnoreCase("")) {
            for (int i = 0; i < govtIdStatusList.size(); i++) {
                if (govtIdStatusList.get(i).statusCode == Integer.parseInt(rsbyItem.getIdType())) {
                    selectedId = i;
                    break;
                }
            }
            govtIdSP.setSelection(selectedId);
            if(rsbyItem.getIdNo()!=null){
                AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Setup screen");
                voterIdCardNumberET.setText(rsbyItem.getIdNo());
            }
            if(rsbyItem.getNameAsId()!=null){
                voterIdCardNameET.setText(rsbyItem.getNameAsId());
            }
            if(rsbyItem.getGovtIdPhoto()!=null){

                if(rsbyItem.getGovtIdPhoto()!=null){
                    try {
                        voterIdIV.setImageBitmap(AppUtility.convertStringToBitmap(rsbyItem.getGovtIdPhoto()));
                    }catch(Exception e){

                    }
                }
            }



           /* if (rsbyItem.getIdType().equalsIgnoreCase("3")) {

                rashanCardImg = rsbyItem.getGovtIdPhoto();
                Log.d(TAG, "Rashan Card Img" + rashanCardImg);
                if (rashanCardImg != +-----------------------**********
                1225+54+
                4154null) {
                    rashanCardIV.setImageBitmap(AppUtility.convertStringToBitmap(rashanCardImg));
                }
                rationCardNumberET.setText(rsbyItem.getIdNo());
                rationCardNameET.setText(rsbyItem.getNameAsId());
            }else if(rsbyItem.getIdType().equalsIgnoreCase("1")){
                enrollmentIdET.setText(rsbyItem.getIdNo());
                enrollmentNameET.setText(rsbyItem.getNameAsId());
            }else if(rsbyItem.getIdType().equalsIgnoreCase("2")){
               voterIdImg=rsbyItem.getGovtIdPhoto();
                if(voterIdImg!=null){
                    voterIdIV.setImageBitmap(AppUtility.convertStringToBitmap(voterIdImg));
                }
                voterIdCardNumberET.setText(rsbyItem.getIdNo());
                voterIdCardNameET.setText(rsbyItem.getNameAsId());
            }*/
        }

    }

    private void alertForValidateLater(final String voterIdNumber, final String voterIdName){
        internetDiaolg = new AlertDialog.Builder(context).create();
        LayoutInflater factory = LayoutInflater.from(context);
        View alertView = factory.inflate(R.layout.aadhar_validation_msg_popup, null);
        internetDiaolg.setView(alertView);
        internetDiaolg.show();

        TextView nameAsInAadharTV=(TextView) alertView.findViewById(R.id.nameAsInAadharTV);
        TextView nameAsInSeccTV=(TextView) alertView.findViewById(R.id.nameAsInSeccTV);
        TextView nameAsInAdhar= (TextView) alertView.findViewById(R.id.nameAsInAdhar);
        TextView nameasrsby = (TextView) alertView.findViewById(R.id.nameasrsby);
        nameasrsby.setText(context.getResources().getString(R.string.name_as_rsby));
        nameAsInAdhar.setText("Name as in Govt Id :");
        nameAsInAadharTV.setText(voterIdName);
        nameAsInSeccTV.setText(rsbyItem.getName());
        Button tryAgainBT=(Button) alertView.findViewById(R.id.tryAgainBT);
        tryAgainBT.setText("Confirm");
        final Button cancelBT=(Button)alertView.findViewById(R.id.cancelBT);
        tryAgainBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                submitDetail( voterIdNumber,  voterIdName);

            }
        });
        cancelBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                internetDiaolg.dismiss();
            }
        });
    }

    private void submitDetail(String voterIdNumber, String voterIdName){
        if(rsbyItem!=null) {
            //  rsbyItem.setAadhaarStatus(aadhaarStatus);
            rsbyItem.setConsent(consent);
            rsbyItem.setIdType(item.statusCode+"");
            rsbyItem.setIdNo(voterIdNumber);
            rsbyItem.setNameAsId(voterIdName);
            rsbyItem.setGovtIdPhoto(voterIdImg);
            rsbyItem.setGovtIdSurveyedStat(AppConstant.SURVEYED+"");
            rsbyItem.setLockedSave(AppConstant.SAVE + "");
            if(selectedMemItem.getOldHeadrsbyMemberItem()!=null && selectedMemItem.getNewHeadrsbyMemberItem()!=null){
                RSBYItem oldHead=selectedMemItem.getRsbyMemberItem();
                oldHead.setLockedSave(AppConstant.LOCKED+"");
                AppUtility.showLog(AppConstant.LOG_STATUS,TAG," OLD Household name " +
                        ": "+oldHead.getName()+"" +
                        " Member Status "+oldHead.getMemStatus()+" House hold Status :" +
                        " "+oldHead.getHhStatus()+" Locked Save :"+oldHead.getLockedSave());
                SeccDatabase.updateRsbyMember(selectedMemItem.getOldHeadrsbyMemberItem(),context);
            }
            SeccDatabase.updateRsbyMember(rsbyItem,context);
            SeccDatabase.updateRSBYHouseHold(selectedMemItem.getRsbyHouseholdItem(),context);
            rsbyItem = SeccDatabase.getRsbyMemberDetail(rsbyItem.getRsbyMemId(),context);
            selectedMemItem.setRsbyMemberItem(rsbyItem);
            ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                    AppConstant.SELECTED_ITEM_FOR_VERIFICATION,selectedMemItem.serialize(),context);
            //  if (rsbyItem.getAadhaarStatus().equalsIgnoreCase("1")) {
            Intent theIntent = new Intent(context, RsbyValidationWithAadharActivity.class);
            startActivity(theIntent);
            finish();
            rightTransition();
                            /*} else {
                                Intent theIntent = new Intent(context, WithoutAadhaarVerificationActivity.class);
                                startActivity(theIntent);
                                finish();
                                rightTransition();
                            }*/
        }
    }
}
