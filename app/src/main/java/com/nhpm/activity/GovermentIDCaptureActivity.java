package com.nhpm.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import com.nhpm.CameraUtils.CommonUtilsImageCompression;
import com.nhpm.CameraUtils.squarecamera.CameraActivity;
import com.nhpm.LocalDataBase.DatabaseHelpers;
import com.nhpm.LocalDataBase.dto.SeccDatabase;
import com.nhpm.Models.request.GovtDetailsModel;
import com.nhpm.Models.response.GovernmentIdItem;
import com.nhpm.Models.response.master.ConfigurationItem;
import com.nhpm.Models.response.master.StateItem;
import com.nhpm.Models.response.seccMembers.SeccMemberItem;
import com.nhpm.Models.response.seccMembers.SelectedMemberItem;
import com.nhpm.Models.response.verifier.VerifierLoginResponse;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;
import com.nhpm.Utility.AppUtility;
import com.nhpm.fragments.GovtIdDataFragment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pl.polidea.view.ZoomView;

import static com.nhpm.Utility.AppUtility.isCheckFirstTwoChar;

public class GovermentIDCaptureActivity extends BaseActivity {

    private Spinner govtIdSP;
    private Context context;
    private ArrayList<GovernmentIdItem> govtIdStatusList;
    private LinearLayout voterIdLayout, rationCardLayout, govtIdPhotoLayout;
    private TextView headerTV;
    private ImageView rashanCardIV, voterIdIV;
    private ImageView voterClickIV, rashanCardClickIV;
    private int CAMERA_PIC_REQUEST = 0;
    private LinearLayout rashanCardCaptureLayout, voterIdCaptureLayout, enrollmentLayout;
    private Bitmap captureImageBM;
    private ImageView backIV;
    private Button rationCardSubmitBT, voterIdSubmitBT, enrollmentSubmitBT;
    private int navigateType;
    private SelectedMemberItem selectedMemItem;
    private SeccMemberItem seccItem;
    private String aadhaarStatus = "";
    private GovernmentIdItem item;
    private EditText rationCardNameET, rationCardNumberET, voterIdCardNameET, voterIdCardNumberET, enrollmentNameET, enrollmentIdET;
    private AlertDialog internetDiaolg;
    private String voterIdImg, rashanCardImg;
    private int selectedId = 0;
    private int RASHAN_CARD_REQUEST = 1;
    private int VOTER_ID_REQUEST = 2;
    private String consent = "y";
    private final String TAG = "Government Activity";
    private final int ENROLLMENT_ID = 1, VOTER_ID = 2, RASHAN_CARD = 3, NREGA = 4, DRIVIG_LICENCE = 5, BIRTH_CERT = 6, OTHER_CARD = 7, NO_GOVID = 8, ID_NO_PHOTO = 9;
    private String nameAsIsinID, numberAsID;
    private CheckBox termCB;
    private LinearLayout mZoomLinearLayout;
    private ZoomView zoomView;
    private Activity activity;
    private boolean pinLockIsShown = false;
    private String zoomMode = "N";
    private int selectedIdType = 0;

    private int wrongPinCount = 0;
    private long wrongPinSavedTime;
    private long currentTime;
    private TextView wrongAttempetCountValue, wrongAttempetCountText;
    private long millisecond24 = 86400000;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransection;
    private Fragment fragment;

    private String blockCharacterSet = ":-/\\\\\\.";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        activity = this;
        checkAppConfig();
        if (zoomMode.equalsIgnoreCase("N")) {
            setContentView(R.layout.activity_goverment_idcapture);
            setupScreenWithoutZoom();
        } else {
            setContentView(R.layout.dummy_layout_for_zooming);
            setupScreenWithZoom();
        }

//        voterIdCardNumberET = (EditText) findViewById(R.id.voterIdCardNumberET);
//        voterIdCardNumberET.setFilters(new InputFilter[]{filter});

    }


    private InputFilter filter = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
//            voterIdCardNumberET = (EditText) findViewById(R.id.schemeEditText1);

            if (source != null && blockCharacterSet.contains(("" + source))) {

//                Toast.makeText(context, "INFO" + source, Toast.LENGTH_SHORT).show();
                if (voterIdCardNumberET.getText().length() <= 2) {
                    return "";
                } else {
                    return null;
                }

//                if
            }
            return null;
        }
    };


    private static String DuplicateCharRemover(String input) {


        Pattern pattern = Pattern.compile("(\\W)\\1{1,}");  // \W FOR NON DIGIT / WORDS, \w FOR DIGIT AND WORDS.
        return pattern.matcher(input).replaceAll("$1"); // REPLACE WITH MULTIPLE OCCURANCE WITH THE SINGLE OCCURANCE
    }

    private static boolean DuplicateCharRemoverbool(String input) {
        Pattern pattern = Pattern.compile("(\\W)\\1{1,}");
        Matcher match = pattern.matcher(input);

        return match.find();
        //        return pattern.matcher(input).replaceAll("$1");

    }


    private void setupScreenWithZoom() {
        fragmentManager = getSupportFragmentManager();
        mZoomLinearLayout = (LinearLayout) findViewById(R.id.mZoomLinearLayout);
        View v = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.activity_goverment_idcapture, null, false);
        v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
        showNotification(v);
        headerTV = (TextView) v.findViewById(R.id.centertext);
        selectedMemItem = SelectedMemberItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.SELECTED_ITEM_FOR_VERIFICATION, context));
        govtIdSP = (Spinner) v.findViewById(R.id.govtIdSP);
        prepareGovernmentIdSpinner();
        voterIdIV = (ImageView) v.findViewById(R.id.voterIdIV);
        rashanCardIV = (ImageView) v.findViewById(R.id.rationCardIV);
        backIV = (ImageView) v.findViewById(R.id.back);
        rationCardLayout = (LinearLayout) v.findViewById(R.id.rationCardLayout);
        voterIdLayout = (LinearLayout) v.findViewById(R.id.voterIdLayout);
        voterIdCaptureLayout = (LinearLayout) v.findViewById(R.id.voterIdCaptureLayout);
        rashanCardCaptureLayout = (LinearLayout) v.findViewById(R.id.rashanCardCaptureLayout);
        govtIdPhotoLayout = (LinearLayout) v.findViewById(R.id.govtIdPhotoLayout);
        enrollmentLayout = (LinearLayout) v.findViewById(R.id.enrollmentLayout);
        headerTV.setText("Beneficiary Data(Without Aadhaar)");
        rationCardSubmitBT = (Button) v.findViewById(R.id.rationCardSubmitBT);
        voterIdSubmitBT = (Button) v.findViewById(R.id.voterIdSubmitBT);
        enrollmentSubmitBT = (Button) v.findViewById(R.id.enrollmentIDSubmitBT);
        rationCardNameET = (EditText) v.findViewById(R.id.rationCardNameET);
        rationCardNumberET = (EditText) v.findViewById(R.id.rationCardNumberET);
        termCB = (CheckBox) v.findViewById(R.id.termsCB);

        voterIdCardNumberET = (EditText) v.findViewById(R.id.voterIdCardNumberET);
        voterIdCardNumberET.setFilters(new InputFilter[]{filter});
        voterIdCardNameET = (EditText) v.findViewById(R.id.voterIdCardNameET);
        dashboardDropdown(v);
        enrollmentIdET = (EditText) v.findViewById(R.id.enrollmentIdET);
        enrollmentNameET = (EditText) v.findViewById(R.id.enrollmentNameET);
        zoomView = new ZoomView(this);
        zoomView.addView(v);
        mZoomLinearLayout.addView(zoomView);
        termCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!b) {
                    consent = "N";
                } else {
                    consent = "Y";
                }
            }
        });

        voterIdSubmitBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String voterIdNumber = voterIdCardNumberET.getText().toString();
                String voterIdName = voterIdCardNameET.getText().toString();

                if (item.statusCode == 0) {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzSelectGovId));
                    return;
                }
                if (item.statusCode == 8) {

                    alertForNoGovIdValidateLater();
                } else {
                    if (voterIdNumber.equalsIgnoreCase("")) {
                        CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterGovId));
                        return;
                    }
                  /*  if (voterIdName.equalsIgnoreCase("")) {
                        CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterNameAsGovId));
                        return;
                    }*/
//                    if (selectedIdType != ID_NO_PHOTO) {
                    if (voterIdImg == null || voterIdIV == null || voterIdImg.equalsIgnoreCase("")) {
                        CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzCaptureGovId));
                        return;
                    }

                    if (DuplicateCharRemoverbool(voterIdNumber)) {
                        voterIdCardNumberET.setText("");
                        CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterValidGovId));
                        return;
                    }

                    if (!isCheckFirstTwoChar(voterIdNumber)) {
                        voterIdCardNumberET.setText("");
                        CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterValidGovId));
                        return;
                    }
//                    }
                  //  alertForValidateLater(voterIdNumber, voterIdName);

           openGovtIdDataFragment();
                }


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
                    if (seccItem != null) {
                    //    seccItem.setAadhaarStatus(aadhaarStatus);
                        seccItem.setIdType(item.statusCode + "");
                       // Log.d(TAG,"Enrollment Id :"+enrollmentId);
                        seccItem.setIdNo(enrollmentId);
                        seccItem.setNameAsId(nameAsInId);
                        seccItem.setGovtIdPhoto("enrollement");
                        seccItem.setGovtIdSurveyedStat(AppConstant.SURVEYED+"");
                        seccItem.setLockedSave(AppConstant.SAVE + "");
                        SeccDatabase.updateSeccMember(seccItem,context);
                        SeccDatabase.updateHouseHold(selectedMemItem.getHouseHoldItem(),context);
                        seccItem=SeccDatabase.getSeccMemberDetail(seccItem.getNhpsMemId(),context);
                        //  Log.d(TAG,"")
                        selectedMemItem.setSeccMemberItem(seccItem);
                        ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                                AppConstant.SELECTED_ITEM_FOR_VERIFICATION, selectedMemItem.serialize(), context);

                      //  if (seccItem.getAadhaarStatus().equalsIgnoreCase("1")) {
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
                        context.getString(R.string.squarecamera__app_name) + "/photoCapture"
                );

                if (mediaStorageDir.exists()) {
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
                startActivityForResult(cameraIntent, RASHAN_CARD_REQUEST);
            }
        });

        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // backNSubmit();
                // if(seccItem.getAadhaarStatus().equalsIgnoreCase("1")) {
                activity.finish();
             /*   Intent theIntent = new Intent(context, WithAadhaarActivity.class);
                startActivity(theIntent);
                finish();
                rightTransition();*/
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
                AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Setup screen Selected id" + item.statusCode);
                switch (item.statusCode) {

                    case NO_GOVID:
                        voterIdLayout.setVisibility(View.GONE);
                        govtIdPhotoLayout.setVisibility(View.GONE);

                        break;

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
                        voterIdCardNameET.setEnabled(true);

                        voterIdImg = null;
//                        updateScreen(voterIdImg);
                        voterIdCardNumberET.requestFocus();
                        AppUtility.showSoftInput(activity);
                        voterIdLayout.setVisibility(View.VISIBLE);
                        voterIdCaptureLayout.setVisibility(View.VISIBLE);
                        govtIdPhotoLayout.setVisibility(View.VISIBLE);
                        /*voterIdCardNumberET.setText("");
                        voterIdCardNameET.setText("");*/
                        //updateScreen(voterIdImg);
                        voterIdCardNumberET.setHint(context.getResources().getString(R.string.enter24digitEid));
                        voterIdCardNameET.setHint(context.getResources().getString(R.string.enterNameInEid));
                       // voterIdCardNameET.setText(seccItem.getName());
                        voterIdCardNameET.setFilters(new InputFilter[]{new InputFilter.LengthFilter(24)});
                        if (seccItem!=null && seccItem.getIdType() != null && seccItem.getIdType().equalsIgnoreCase(ENROLLMENT_ID + "")) {
                            if (seccItem.getIdNo() != null && !seccItem.getIdNo().equalsIgnoreCase("")) {
                                voterIdCardNameET.setText(seccItem.getNameAsId());
                                voterIdCardNumberET.setText(seccItem.getIdNo());
                            } else {
                                voterIdCardNameET.setText(seccItem.getName());
                            }
                        }
                        updateScreen(voterIdImg);
                       /* rationCardLayout.setVisibility(View.GONE);
                        enrollmentLayout.setVisibility(View.VISIBLE);*/
                        //aadhaarStatus="1";
                        break;
                    case VOTER_ID:
                        AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Setup screen Selected id");
                        voterIdImg = null;
                        voterIdCardNameET.setEnabled(true);

                        voterIdLayout.setVisibility(View.VISIBLE);
                        voterIdCaptureLayout.setVisibility(View.VISIBLE);
                        govtIdPhotoLayout.setVisibility(View.VISIBLE);
                        voterIdCardNumberET.requestFocus();
                        voterIdCardNumberET.setText("");
                        voterIdCardNameET.setText("");
//                        updateScreen(voterIdImg);
                        voterIdCardNumberET.setHint(context.getResources().getString(R.string.plzEnterVoterIdNum));
                        voterIdCardNameET.setHint(context.getResources().getString(R.string.plzEnterNameAsVoterId));


                      //  voterIdCardNameET.setText(seccItem.getName());
                       /* rationCardLayout.setVisibility(View.GONE);
                        enrollmentLayout.setVisibility(View.GONE);*/
                        // aadhaarStatus="2";
                        if (seccItem!=null && seccItem.getIdType() != null && seccItem.getIdType().equalsIgnoreCase(VOTER_ID + "")) {
                            voterIdImg = seccItem.getGovtIdPhoto();
                            if (seccItem.getIdNo() != null && !seccItem.getIdNo().equalsIgnoreCase("")) {
                                voterIdCardNameET.setText(seccItem.getNameAsId());
                                voterIdCardNumberET.setText(seccItem.getIdNo());
                            } else {
                                voterIdCardNameET.setText(seccItem.getName());
                            }
                            voterIdCardNumberET.requestFocus();
                            AppUtility.showSoftInput(activity);
                        }
                        updateScreen(voterIdImg);


                        break;
                    case RASHAN_CARD:
                        voterIdImg = null;
                        voterIdCardNameET.setEnabled(true);

                        voterIdLayout.setVisibility(View.VISIBLE);
                        voterIdCaptureLayout.setVisibility(View.VISIBLE);
                        govtIdPhotoLayout.setVisibility(View.VISIBLE);
//                        updateScreen(voterIdImg);
                        voterIdCardNumberET.requestFocus();
                        AppUtility.showSoftInput(activity);
                        voterIdCardNumberET.setText("");
                        voterIdCardNameET.setText("");
                        voterIdCardNumberET.setHint(context.getResources().getString(R.string.plzEnterRationCardNum));
                        voterIdCardNameET.setHint(context.getResources().getString(R.string.plzEnterNameAsRationCard));
//                        voterIdCardNameET.setText(seccItem.getName());
                       /* rationCardLayout.setVisibility(View.VISIBLE);
                        enrollmentLayout.setVisibility(View.GONE);*/
                        //aadhaarStatus="3";
                        if (seccItem!=null && seccItem.getIdType() != null && seccItem.getIdType().equalsIgnoreCase(RASHAN_CARD + "")) {
                            voterIdImg = seccItem.getGovtIdPhoto();
                            if (seccItem.getIdNo() != null && !seccItem.getIdNo().equalsIgnoreCase("")) {
                                voterIdCardNameET.setText(seccItem.getNameAsId());
                                voterIdCardNumberET.setText(seccItem.getIdNo());
                            } else {
                                voterIdCardNameET.setText(seccItem.getName());
                            }
                        }
                        updateScreen(voterIdImg);

                        break;
                    case NREGA:
                        voterIdImg = null;
                        voterIdCardNameET.setEnabled(true);

//                        updateScreen(voterIdImg);
                        voterIdLayout.setVisibility(View.VISIBLE);
                        voterIdCaptureLayout.setVisibility(View.VISIBLE);
                        govtIdPhotoLayout.setVisibility(View.VISIBLE);
                        voterIdCardNumberET.setText("");
                        voterIdCardNameET.setText("");
                        voterIdCardNumberET.setHint(context.getResources().getString(R.string.plzEnterNaregaNum));
                        voterIdCardNameET.setHint(context.getResources().getString(R.string.plzEnterNameAsNarega));
                       // voterIdCardNameET.setText(seccItem.getName());
                        voterIdCardNumberET.requestFocus();
                        AppUtility.showSoftInput(activity);
                        if (seccItem!=null && seccItem.getIdType() != null && seccItem.getIdType().equalsIgnoreCase(NREGA + "")) {
                            voterIdImg = seccItem.getGovtIdPhoto();
                            if (seccItem.getIdNo() != null && !seccItem.getIdNo().equalsIgnoreCase("")) {
                                voterIdCardNameET.setText(seccItem.getNameAsId());
                                voterIdCardNumberET.setText(seccItem.getIdNo());
                            } else {
                                voterIdCardNameET.setText(seccItem.getName());
                            }
                        }
                        updateScreen(voterIdImg);

                        break;
                    case DRIVIG_LICENCE:
                        voterIdImg = null;
//                        updateScreen(voterIdImg);
                        voterIdCardNameET.setEnabled(true);

                        voterIdLayout.setVisibility(View.VISIBLE);
                        voterIdCaptureLayout.setVisibility(View.VISIBLE);
                        govtIdPhotoLayout.setVisibility(View.VISIBLE);
                        voterIdCardNumberET.requestFocus();
                        AppUtility.showSoftInput(activity);
                        /*voterIdCardNumberET.setText("");

                        voterIdCardNameET.setText("");*/
                        voterIdCardNumberET.setText("");
                        voterIdCardNameET.setText("");
                        voterIdCardNumberET.setHint(context.getResources().getString(R.string.plzEnterDrivingNum));
                        voterIdCardNameET.setHint(context.getResources().getString(R.string.plzEnterNameAsDriving));
                      //  voterIdCardNameET.setText(seccItem.getName());

                        if (seccItem!=null && seccItem.getIdType() != null && seccItem.getIdType().equalsIgnoreCase(DRIVIG_LICENCE + "")) {
                            voterIdImg = seccItem.getGovtIdPhoto();
                            if (seccItem.getIdType() != null && seccItem.getIdNo() != null && !seccItem.getIdNo().equalsIgnoreCase("")) {
                                voterIdCardNameET.setText(seccItem.getNameAsId());
                                voterIdCardNumberET.setText(seccItem.getIdNo());
                            } else {
                                voterIdCardNameET.setText(seccItem.getName());
                            }
                        }
                        updateScreen(voterIdImg);

                        break;
                    case BIRTH_CERT:
                        voterIdImg = null;
//                        updateScreen(voterIdImg);
                        voterIdCardNameET.setEnabled(true);

                        voterIdLayout.setVisibility(View.VISIBLE);
                        govtIdPhotoLayout.setVisibility(View.VISIBLE);
                        voterIdCaptureLayout.setVisibility(View.VISIBLE);
                        voterIdCardNumberET.setText("");
                        voterIdCardNameET.setText("");
                        voterIdCardNumberET.setText("");
                        voterIdCardNameET.setText("");
                        voterIdCardNumberET.requestFocus();
                        AppUtility.showSoftInput(activity);
                        voterIdCardNumberET.setHint(context.getResources().getString(R.string.plzEnterBirthCerfNum));
                        voterIdCardNameET.setHint(context.getResources().getString(R.string.plzEnterNameAsBirthCerf));
                        //voterIdCardNameET.setText(seccItem.getName());
                        if (seccItem!=null && seccItem.getIdType() != null && seccItem.getIdType().equalsIgnoreCase(BIRTH_CERT + "")) {
                            voterIdImg = seccItem.getGovtIdPhoto();
                            if (seccItem.getIdNo() != null && !seccItem.getIdNo().equalsIgnoreCase("")) {
                                voterIdCardNameET.setText(seccItem.getNameAsId());
                                voterIdCardNumberET.setText(seccItem.getIdNo());
                            } else {
                                voterIdCardNameET.setText(seccItem.getName());
                            }
                        }
                        updateScreen(voterIdImg);

                        break;
                    case OTHER_CARD:

                        voterIdImg = null;
//                        updateScreen(voterIdImg);
                        voterIdLayout.setVisibility(View.VISIBLE);
                        voterIdCaptureLayout.setVisibility(View.VISIBLE);
                        govtIdPhotoLayout.setVisibility(View.VISIBLE);
                        voterIdCardNumberET.setText("");
                        voterIdCardNameET.setText("");
//                        voterIdCardNumberET.setText("");
//                        voterIdCardNameET.setText("");
                        voterIdCardNameET.setEnabled(true);
                        voterIdCardNumberET.requestFocus();
                        AppUtility.showSoftInput(activity);
                        voterIdCardNumberET.setHint(context.getResources().getString(R.string.plzEnterId));
                        voterIdCardNameET.setHint(context.getResources().getString(R.string.plzEnterNameAsId));
                      //  voterIdCardNameET.setText(seccItem.getName());
                        if (seccItem!=null && seccItem.getIdType() != null && seccItem.getIdType().equalsIgnoreCase(OTHER_CARD + "")) {
                            voterIdImg = seccItem.getGovtIdPhoto();
                            if (seccItem.getIdNo() != null && !seccItem.getIdNo().equalsIgnoreCase("")) {
                                voterIdCardNameET.setText(seccItem.getNameAsId());
                                voterIdCardNumberET.setText(seccItem.getIdNo());
                            } else {
                                voterIdCardNameET.setText(seccItem.getName());
                            }
                        }
                        updateScreen(voterIdImg);

                        break;

                    case ID_NO_PHOTO:
                        voterIdImg = null;
//                        updateScreen(voterIdImg);
                        voterIdCardNameET.setEnabled(true);
                        voterIdLayout.setVisibility(View.VISIBLE);
                        govtIdPhotoLayout.setVisibility(View.VISIBLE);
                        //  preparedItem.setAadhaarSurveyedStat(item.getAadhaarSurveyedStat());
                        //  preparedItem.setAadhaarSurveyedStat(item.getAadhaarSurveyedStat());
//                        voterIdCaptureLayout.setVisibility(View.GONE);
                        voterIdCaptureLayout.setVisibility(View.VISIBLE);
                        voterIdCardNumberET.setText("");

                        voterIdCardNameET.setText("");
//                        voterIdCardNumberET.setText("");
//                        voterIdCardNameET.setText("");
                        voterIdCardNameET.setEnabled(true);
                        selectedIdType = ID_NO_PHOTO;
                        voterIdCardNumberET.requestFocus();
                        AppUtility.showSoftInput(activity);
                        voterIdCardNumberET.setHint(context.getResources().getString(R.string.plzEnterId));
                        voterIdCardNameET.setHint(context.getResources().getString(R.string.plzEnterNameAsId));
                       // voterIdCardNameET.setText(seccItem.getName());
                        if (seccItem !=null && seccItem.getIdType() != null && seccItem.getIdType().equalsIgnoreCase(ID_NO_PHOTO + "")) {
                            voterIdImg = seccItem.getGovtIdPhoto();
                            if (seccItem.getIdNo() != null && !seccItem.getIdNo().equalsIgnoreCase("")) {
                                voterIdCardNameET.setText(seccItem.getNameAsId());
                                voterIdCardNumberET.setText(seccItem.getIdNo());
                            } else {
                                voterIdCardNameET.setText(seccItem.getName());
                            }
                        }
                        updateScreen(voterIdImg);

                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        if (selectedMemItem!=null && selectedMemItem.getSeccMemberItem() != null) {
            seccItem = selectedMemItem.getSeccMemberItem();
            if (seccItem != null && seccItem.getDataSource() != null &&
                    seccItem.getDataSource().trim().equalsIgnoreCase(AppConstant.RSBY_SOURCE)) {
                showRsbyDetail(seccItem);
            } else {
                showSeccDetail(seccItem);
            }
            setupSeccData(seccItem);
        } else {

        }

    }

    private void setupScreenWithoutZoom() {
        showNotification();
        fragmentManager = getSupportFragmentManager();
        headerTV = (TextView) findViewById(R.id.centertext);
        selectedMemItem = SelectedMemberItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.SELECTED_ITEM_FOR_VERIFICATION, context));
        govtIdSP = (Spinner) findViewById(R.id.govtIdSP);
        prepareGovernmentIdSpinner();
        voterIdIV = (ImageView) findViewById(R.id.voterIdIV);
        rashanCardIV = (ImageView) findViewById(R.id.rationCardIV);
        backIV = (ImageView) findViewById(R.id.back);
        rationCardLayout = (LinearLayout) findViewById(R.id.rationCardLayout);
        voterIdLayout = (LinearLayout) findViewById(R.id.voterIdLayout);
        voterIdCaptureLayout = (LinearLayout) findViewById(R.id.voterIdCaptureLayout);
        rashanCardCaptureLayout = (LinearLayout) findViewById(R.id.rashanCardCaptureLayout);
        govtIdPhotoLayout = (LinearLayout) findViewById(R.id.govtIdPhotoLayout);
        enrollmentLayout = (LinearLayout) findViewById(R.id.enrollmentLayout);
        headerTV.setText(context.getResources().getString(R.string.governmentId));
        rationCardSubmitBT = (Button) findViewById(R.id.rationCardSubmitBT);
        voterIdSubmitBT = (Button) findViewById(R.id.voterIdSubmitBT);
        enrollmentSubmitBT = (Button) findViewById(R.id.enrollmentIDSubmitBT);
        rationCardNameET = (EditText) findViewById(R.id.rationCardNameET);
        rationCardNumberET = (EditText) findViewById(R.id.rationCardNumberET);
        termCB = (CheckBox) findViewById(R.id.termsCB);

        voterIdCardNumberET = (EditText) findViewById(R.id.voterIdCardNumberET);
        voterIdCardNumberET.setFilters(new InputFilter[]{filter});
        voterIdCardNameET = (EditText) findViewById(R.id.voterIdCardNameET);
        dashboardDropdown();
        enrollmentIdET = (EditText) findViewById(R.id.enrollmentIdET);
        enrollmentNameET = (EditText) findViewById(R.id.enrollmentNameET);

        termCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!b) {
                    consent = "N";
                } else {
                    consent = "Y";
                }
            }
        });

        voterIdSubmitBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String voterIdNumber = voterIdCardNumberET.getText().toString();
                String voterIdName = voterIdCardNameET.getText().toString();
                if (item.statusCode == 0) {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzSelectGovId));
                    return;
                }
                if (item.statusCode == 8) {
                    alertForNoGovIdValidateLater();
                } else {
                    if (voterIdNumber.equalsIgnoreCase("")) {
                        CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterGovId));
                        return;
                    }
                    if (voterIdName.equalsIgnoreCase("")) {
                        CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterNameAsGovId));
                        return;
                    }
            /*if(consent.equalsIgnoreCase("N")){
                    CustomAlert.alertWithOk(context,"Please tick Name in SECC and name in govt. id are of same person");
                    return;
                }*/
                    if (selectedIdType == ID_NO_PHOTO) {
                        if (voterIdImg == null) {
                            CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzCaptureGovId));
                            return;
                        }
                    }
                    alertForValidateLater(voterIdNumber, voterIdName);


                    if (DuplicateCharRemoverbool(voterIdNumber)) {
                        voterIdCardNumberET.setText("");
                        CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterValidGovId));
                        return;
                    }

                    if (!isCheckFirstTwoChar(voterIdNumber)) {
                        voterIdCardNumberET.setText("");
                        CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterValidGovId));
                        return;
                    }
                }


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
                    if (seccItem != null) {
                    //    seccItem.setAadhaarStatus(aadhaarStatus);
                        seccItem.setIdType(item.statusCode + "");
                       // Log.d(TAG,"Enrollment Id :"+enrollmentId);
                        seccItem.setIdNo(enrollmentId);
                        seccItem.setNameAsId(nameAsInId);
                        seccItem.setGovtIdPhoto("enrollement");
                        seccItem.setGovtIdSurveyedStat(AppConstant.SURVEYED+"");
                        seccItem.setLockedSave(AppConstant.SAVE + "");
                        SeccDatabase.updateSeccMember(seccItem,context);
                        SeccDatabase.updateHouseHold(selectedMemItem.getHouseHoldItem(),context);
                        seccItem=SeccDatabase.getSeccMemberDetail(seccItem.getNhpsMemId(),context);
                        //  Log.d(TAG,"")
                        selectedMemItem.setSeccMemberItem(seccItem);
                        ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                                AppConstant.SELECTED_ITEM_FOR_VERIFICATION, selectedMemItem.serialize(), context);

                      //  if (seccItem.getAadhaarStatus().equalsIgnoreCase("1")) {
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
                        context.getString(R.string.squarecamera__app_name) + "/photoCapture"
                );

                if (mediaStorageDir.exists()) {
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
                startActivityForResult(cameraIntent, RASHAN_CARD_REQUEST);
            }
        });

        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
                // backNSubmit();
                // if(seccItem.getAadhaarStatus().equalsIgnoreCase("1")) {
               /* Intent theIntent = new Intent(context, WithAadhaarActivity.class);
                startActivity(theIntent);
                finish();
                rightTransition();*/
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
                AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Setup screen Selected id" + item.statusCode);
                switch (item.statusCode) {
                    case NO_GOVID:
                        voterIdLayout.setVisibility(View.GONE);
                        govtIdPhotoLayout.setVisibility(View.GONE);

                        break;

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
                        voterIdImg = null;
//                        updateScreen(voterIdImg);
                        voterIdCardNumberET.requestFocus();
                        AppUtility.showSoftInput(activity);
                        voterIdLayout.setVisibility(View.VISIBLE);
                        voterIdCaptureLayout.setVisibility(View.VISIBLE);
                        govtIdPhotoLayout.setVisibility(View.VISIBLE);
                        /*voterIdCardNumberET.setText("");
                        voterIdCardNameET.setText("");*/
                        //updateScreen(voterIdImg);
                        voterIdCardNumberET.setHint(context.getResources().getString(R.string.enter24digitEid));
                        voterIdCardNameET.setHint(context.getResources().getString(R.string.enterNameInEid));
                        voterIdCardNameET.setEnabled(true);
                        voterIdCardNameET.setText(seccItem.getName());
                        voterIdCardNameET.setFilters(new InputFilter[]{new InputFilter.LengthFilter(24)});
                        if (seccItem.getIdType() != null && seccItem.getIdType().equalsIgnoreCase(ENROLLMENT_ID + "")) {
                            if (seccItem.getIdNo() != null && !seccItem.getIdNo().equalsIgnoreCase("")) {
                                voterIdCardNameET.setText(seccItem.getNameAsId());
                                voterIdCardNumberET.setText(seccItem.getIdNo());
                            } else {
                                voterIdCardNameET.setText(seccItem.getName());
                            }
                        }
                        updateScreen(voterIdImg);

                       /* rationCardLayout.setVisibility(View.GONE);
                        enrollmentLayout.setVisibility(View.VISIBLE);*/
                        //aadhaarStatus="1";
                        break;
                    case VOTER_ID:
                        AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Setup screen Selected id");
                        voterIdImg = null;
                        voterIdLayout.setVisibility(View.VISIBLE);
                        govtIdPhotoLayout.setVisibility(View.VISIBLE);
                        voterIdCaptureLayout.setVisibility(View.VISIBLE);
                        voterIdCardNumberET.requestFocus();
                        voterIdCardNumberET.setText("");
                        voterIdCardNameET.setText("");
//                        updateScreen(voterIdImg);
                        voterIdCardNumberET.setHint(context.getResources().getString(R.string.plzEnterVoterIdNum));
                        voterIdCardNameET.setHint(context.getResources().getString(R.string.plzEnterNameAsVoterId));

                        voterIdCardNameET.setText(seccItem.getName());
                        voterIdCardNameET.setEnabled(true);

                       /* rationCardLayout.setVisibility(View.GONE);
                        enrollmentLayout.setVisibility(View.GONE);*/
                        // aadhaarStatus="2";
                        if (seccItem.getIdType() != null && seccItem.getIdType().equalsIgnoreCase(VOTER_ID + "")) {
                            voterIdImg = seccItem.getGovtIdPhoto();
                            if (seccItem.getIdNo() != null && !seccItem.getIdNo().equalsIgnoreCase("")) {
                                voterIdCardNameET.setText(seccItem.getNameAsId());
                                voterIdCardNumberET.setText(seccItem.getIdNo());
                            } else {
                                voterIdCardNameET.setText(seccItem.getName());
                            }
                            voterIdCardNumberET.requestFocus();
                            AppUtility.showSoftInput(activity);
                        }
                        updateScreen(voterIdImg);

                        break;
                    case RASHAN_CARD:
                        voterIdImg = null;
                        voterIdLayout.setVisibility(View.VISIBLE);
                        govtIdPhotoLayout.setVisibility(View.VISIBLE);
                        voterIdCaptureLayout.setVisibility(View.VISIBLE);
//                        updateScreen(voterIdImg);
                        voterIdCardNumberET.requestFocus();
                        AppUtility.showSoftInput(activity);
                        voterIdCardNumberET.setText("");
                        voterIdCardNameET.setText("");
                        voterIdCardNumberET.setHint(context.getResources().getString(R.string.plzEnterRationCardNum));
                        voterIdCardNameET.setHint(context.getResources().getString(R.string.plzEnterNameAsRationCard));
                        voterIdCardNameET.setText(seccItem.getName());
                        voterIdCardNameET.setEnabled(true);

                       /* rationCardLayout.setVisibility(View.VISIBLE);
                        enrollmentLayout.setVisibility(View.GONE);*/
                        //aadhaarStatus="3";
                        if (seccItem.getIdType() != null && seccItem.getIdType().equalsIgnoreCase(RASHAN_CARD + "")) {
                            voterIdImg = seccItem.getGovtIdPhoto();
                            if (seccItem.getIdNo() != null && !seccItem.getIdNo().equalsIgnoreCase("")) {
                                voterIdCardNameET.setText(seccItem.getNameAsId());
                                voterIdCardNumberET.setText(seccItem.getIdNo());
                            } else {
                                voterIdCardNameET.setText(seccItem.getName());
                            }
                        }
                        updateScreen(voterIdImg);

                        break;
                    case NREGA:
                        voterIdImg = null;
//                        updateScreen(voterIdImg);
                        voterIdLayout.setVisibility(View.VISIBLE);
                        govtIdPhotoLayout.setVisibility(View.VISIBLE);
                        voterIdCaptureLayout.setVisibility(View.VISIBLE);
                        voterIdCardNumberET.setText("");
                        voterIdCardNameET.setText("");
                        voterIdCardNumberET.setHint(context.getResources().getString(R.string.plzEnterNaregaNum));
                        voterIdCardNameET.setHint(context.getResources().getString(R.string.plzEnterNameAsNarega));
                        voterIdCardNameET.setText(seccItem.getName());
                        voterIdCardNameET.setEnabled(true);

                        voterIdCardNumberET.requestFocus();
                        AppUtility.showSoftInput(activity);
                        if (seccItem.getIdType() != null && seccItem.getIdType().equalsIgnoreCase(NREGA + "")) {
                            voterIdImg = seccItem.getGovtIdPhoto();
                            if (seccItem.getIdNo() != null && !seccItem.getIdNo().equalsIgnoreCase("")) {
                                voterIdCardNameET.setText(seccItem.getNameAsId());
                                voterIdCardNumberET.setText(seccItem.getIdNo());
                            } else {
                                voterIdCardNameET.setText(seccItem.getName());
                            }
                        }
                        updateScreen(voterIdImg);

                        break;
                    case DRIVIG_LICENCE:
                        voterIdImg = null;
//                        updateScreen(voterIdImg);
                        voterIdLayout.setVisibility(View.VISIBLE);
                        govtIdPhotoLayout.setVisibility(View.VISIBLE);
                        voterIdCaptureLayout.setVisibility(View.VISIBLE);
                        voterIdCardNumberET.requestFocus();
                        AppUtility.showSoftInput(activity);
                        /*voterIdCardNumberET.setText("");

                        voterIdCardNameET.setText("");*/
                        voterIdCardNumberET.setText("");
                        voterIdCardNameET.setText("");
                        voterIdCardNumberET.setHint(context.getResources().getString(R.string.plzEnterDrivingNum));
                        voterIdCardNameET.setHint(context.getResources().getString(R.string.plzEnterNameAsDriving));
                        voterIdCardNameET.setText(seccItem.getName());
                        voterIdCardNameET.setEnabled(true);

                        if (seccItem.getIdType().equalsIgnoreCase(DRIVIG_LICENCE + "")) {
                            voterIdImg = seccItem.getGovtIdPhoto();
                            if (seccItem.getIdType() != null && seccItem.getIdNo() != null && !seccItem.getIdNo().equalsIgnoreCase("")) {
                                voterIdCardNameET.setText(seccItem.getNameAsId());
                                voterIdCardNumberET.setText(seccItem.getIdNo());
                            } else {
                                voterIdCardNameET.setText(seccItem.getName());
                            }
                        }
                        updateScreen(voterIdImg);

                        break;
                    case BIRTH_CERT:
                        voterIdImg = null;
//                        updateScreen(voterIdImg);
                        voterIdLayout.setVisibility(View.VISIBLE);
                        govtIdPhotoLayout.setVisibility(View.VISIBLE);
                        voterIdCaptureLayout.setVisibility(View.VISIBLE);
                        voterIdCardNumberET.setText("");
                        voterIdCardNameET.setText("");
                        voterIdCardNumberET.setText("");
                        voterIdCardNameET.setText("");
                        voterIdCardNumberET.requestFocus();
                        AppUtility.showSoftInput(activity);
                        voterIdCardNumberET.setHint(context.getResources().getString(R.string.plzEnterBirthCerfNum));
                        voterIdCardNameET.setHint(context.getResources().getString(R.string.plzEnterNameAsBirthCerf));
                        voterIdCardNameET.setText(seccItem.getName());
                        voterIdCardNameET.setEnabled(true);

                        if (seccItem.getIdType() != null && seccItem.getIdType().equalsIgnoreCase(BIRTH_CERT + "")) {
                            voterIdImg = seccItem.getGovtIdPhoto();
                            if (seccItem.getIdNo() != null && !seccItem.getIdNo().equalsIgnoreCase("")) {
                                voterIdCardNameET.setText(seccItem.getNameAsId());
                                voterIdCardNumberET.setText(seccItem.getIdNo());
                            } else {
                                voterIdCardNameET.setText(seccItem.getName());
                            }
                        }
                        updateScreen(voterIdImg);

                        break;
                    case OTHER_CARD:
                        voterIdImg = null;
//                        updateScreen(voterIdImg);
                        voterIdLayout.setVisibility(View.VISIBLE);
                        voterIdCaptureLayout.setVisibility(View.VISIBLE);
                        govtIdPhotoLayout.setVisibility(View.VISIBLE);
                        voterIdCardNumberET.setText("");
                        voterIdCardNameET.setText("");
                        voterIdCardNumberET.setText("");
                        voterIdCardNameET.setText("");
                        voterIdCardNumberET.requestFocus();
                        AppUtility.showSoftInput(activity);
                        voterIdCardNumberET.setHint(context.getResources().getString(R.string.plzEnterId));
                        voterIdCardNameET.setHint(context.getResources().getString(R.string.plzEnterNameAsId));
                        voterIdCardNameET.setText(seccItem.getName());
                        voterIdCardNameET.setEnabled(true);

                        if (seccItem.getIdType() != null && seccItem.getIdType().equalsIgnoreCase(OTHER_CARD + "")) {
                            voterIdImg = seccItem.getGovtIdPhoto();
                            if (seccItem.getIdNo() != null && !seccItem.getIdNo().equalsIgnoreCase("")) {
                                voterIdCardNameET.setText(seccItem.getNameAsId());
                                voterIdCardNumberET.setText(seccItem.getIdNo());
                            } else {
                                voterIdCardNameET.setText(seccItem.getName());
                            }
                        }
                        updateScreen(voterIdImg);

                        break;

                    case ID_NO_PHOTO:
                        voterIdImg = null;
//                        updateScreen(voterIdImg);
                        voterIdLayout.setVisibility(View.VISIBLE);
                        govtIdPhotoLayout.setVisibility(View.VISIBLE);
//                        voterIdCaptureLayout.setVisibility(View.GONE);
                        voterIdCaptureLayout.setVisibility(View.VISIBLE);
                        selectedIdType = ID_NO_PHOTO;
                        voterIdCardNumberET.setText("");
                        voterIdCardNameET.setText("");
                        voterIdCardNumberET.setText("");
                        voterIdCardNameET.setText("");
                        voterIdCardNumberET.requestFocus();
                        AppUtility.showSoftInput(activity);
                        voterIdCardNumberET.setHint(context.getResources().getString(R.string.plzEnterId));
                        voterIdCardNameET.setHint(context.getResources().getString(R.string.plzEnterNameAsId));
                        voterIdCardNameET.setText(seccItem.getName());
                        voterIdCardNameET.setEnabled(true);

                        if (seccItem.getIdType() != null && seccItem.getIdType().equalsIgnoreCase(ID_NO_PHOTO + "")) {
                            voterIdImg = seccItem.getGovtIdPhoto();
                            if (seccItem.getIdNo() != null && !seccItem.getIdNo().equalsIgnoreCase("")) {
                                voterIdCardNameET.setText(seccItem.getNameAsId());
                                voterIdCardNumberET.setText(seccItem.getIdNo());
                            } else {
                                voterIdCardNameET.setText(seccItem.getName());
                            }
                        }
                        updateScreen(voterIdImg);

                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        if (selectedMemItem.getSeccMemberItem() != null) {
            seccItem = selectedMemItem.getSeccMemberItem();
            if (seccItem != null && seccItem.getDataSource() != null &&
                    seccItem.getDataSource().trim().equalsIgnoreCase(AppConstant.RSBY_SOURCE)) {
                showRsbyDetail(seccItem);
            } else {
                showSeccDetail(seccItem);
            }
            setupSeccData(seccItem);
        } else {

        }

    }


    private void dashboardDropdown(View v) {


        final ImageView settings = (ImageView) v.findViewById(R.id.settings);
        settings.setVisibility(View.GONE);

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
        settings.setVisibility(View.GONE);

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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //   if(data!=null) {

        if (resultCode != Activity.RESULT_CANCELED) {
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
            }/* else if (requestCode == RASHAN_CARD_REQUEST) {
                Log.d("Govt id capture","rashan card calling");
                captureImageBM = (Bitmap) data.getExtras().get("data");
                rashanCardImg=AppUtility.convertBitmapToString(captureImageBM);
                rashanCardIV.setImageBitmap(AppUtility.convertStringToBitmap(rashanCardImg));
            }*/
       /* }else{

        }*/
        } else {
            if (requestCode == VOTER_ID_REQUEST) {
               /* Uri imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),"govtIdPhoto" +".jpg"));
                try {
                    captureImageBM = MediaStore.Images.Media.getBitmap(this.getContentResolver(),imageUri); //(Bitmap)imageUri;//data.getExtras().get("data");
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
                Uri imageUri = Uri.fromFile(new File(DatabaseHelpers.DELETE_FOLDER_PATH, context.getString(R.string.squarecamera__app_name) + "/govtIdPhoto/IMG_12345.jpg"));
                Uri compressedUri = Uri.fromFile(new File(CommonUtilsImageCompression.compressImage(imageUri.getPath(), context, "/govtIdPhoto")));
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

    private void updateScreen(String idImage) {
        try {
            if (idImage != null) {
                voterIdIV.setImageBitmap(AppUtility.convertStringToBitmap(idImage));
            } else {
//                if (seccItem.getGovtIdPhoto() != null && !seccItem.getGovtIdPhoto().equalsIgnoreCase("")) {
//
//                } else {
                voterIdIV.setImageBitmap(null);
//                }
            }
        } catch (Exception e) {

        }
    }

    private void prepareGovernmentIdSpinner() {
        govtIdStatusList = AppUtility.prepareGovernmentIdSpinner();
        ArrayList<String> spinnerList = new ArrayList<>();
        /*govtIdStatusList.add(new GovernmentIdItem(0,"Select Govt.ID"));
        govtIdStatusList.add(new GovernmentIdItem(1,"Aadhaar Enrollment ID"));
        govtIdStatusList.add(new GovernmentIdItem(2,"Voter ID Card"));
        govtIdStatusList.add(new GovernmentIdItem(3,"Ration Card"));
        govtIdStatusList.add(new GovernmentIdItem(4,"NREGA job card"));
        govtIdStatusList.add(new GovernmentIdItem(5,"Driving License"));*/
        for (GovernmentIdItem item : govtIdStatusList) {
            spinnerList.add(item.status);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.custom_drop_down, R.id.textView, spinnerList);
        govtIdSP.setAdapter(adapter);
    }

    private void backNSubmit() {
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

    private void setupSeccData(SeccMemberItem seccItem) {

        if (seccItem != null & seccItem.getIdType() != null && !seccItem.getIdType().equalsIgnoreCase("")) {
            for (int i = 0; i < govtIdStatusList.size(); i++) {
                if (govtIdStatusList.get(i).statusCode == Integer.parseInt(seccItem.getIdType())) {
                    selectedId = i;
                    break;
                }
            }
            govtIdSP.setSelection(selectedId);
            if (seccItem.getIdNo() != null) {
                AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Setup screen");
                voterIdCardNumberET.setText(seccItem.getIdNo());
            }
            if (seccItem.getNameAsId() != null) {
                voterIdCardNameET.setText(seccItem.getNameAsId());
            }
            if (seccItem.getGovtIdPhoto() != null) {

                if (seccItem.getGovtIdPhoto() != null) {
                    try {
                        voterIdIV.setImageBitmap(AppUtility.convertStringToBitmap(seccItem.getGovtIdPhoto()));
                    } catch (Exception e) {

                    }
                }
            }



           /* if (seccItem.getIdType().equalsIgnoreCase("3")) {

                rashanCardImg = seccItem.getGovtIdPhoto();
                Log.d(TAG, "Rashan Card Img" + rashanCardImg);
                if (rashanCardImg != null) {
                    rashanCardIV.setImageBitmap(AppUtility.convertStringToBitmap(rashanCardImg));
                }
                rationCardNumberET.setText(seccItem.getIdNo());
                rationCardNameET.setText(seccItem.getNameAsId());
            }else if(seccItem.getIdType().equalsIgnoreCase("1")){
                enrollmentIdET.setText(seccItem.getIdNo());
                enrollmentNameET.setText(seccItem.getNameAsId());
            }else if(seccItem.getIdType().equalsIgnoreCase("2")){
               voterIdImg=seccItem.getGovtIdPhoto();
                if(voterIdImg!=null){
                    voterIdIV.setImageBitmap(AppUtility.convertStringToBitmap(voterIdImg));
                }
                voterIdCardNumberET.setText(seccItem.getIdNo());
                voterIdCardNameET.setText(seccItem.getNameAsId());
            }*/
        }

    }

    private void alertForNoGovIdValidateLater() {
        internetDiaolg = new AlertDialog.Builder(context).create();
        LayoutInflater factory = LayoutInflater.from(context);
        View alertView = factory.inflate(R.layout.aadhar_validation_msg_popup, null);
        internetDiaolg.setView(alertView);
        internetDiaolg.show();

        LinearLayout layout1 = (LinearLayout) alertView.findViewById(R.id.nameAsIDLayout);
        layout1.setVisibility(View.GONE);
        LinearLayout layout2 = (LinearLayout) alertView.findViewById(R.id.nameAsSeccLayout);
        layout2.setVisibility(View.GONE);
        TextView msgTV = (TextView) alertView.findViewById(R.id.msgTV);
  /*      nameAsInAdhar.setText("Name as in Govt Id :");
        nameAsInAadharTV.setText(voterIdName);
        nameAsInSeccTV.setText(seccItem.getName());*/
        msgTV.setText(context.getResources().getString(R.string.plzConfrmNoGovId));
        Button tryAgainBT = (Button) alertView.findViewById(R.id.tryAgainBT);
        tryAgainBT.setText(context.getResources().getString(R.string.Confirm_Submit));
        final Button cancelBT = (Button) alertView.findViewById(R.id.cancelBT);
        tryAgainBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                submitDetail(null, null);

            }
        });
        cancelBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                internetDiaolg.dismiss();
            }
        });
    }

    private void alertForValidateLater(final String voterIdNumber, final String voterIdName) {
        internetDiaolg = new AlertDialog.Builder(context).create();
        LayoutInflater factory = LayoutInflater.from(context);
        View alertView = factory.inflate(R.layout.aadhar_validation_msg_popup, null);
        internetDiaolg.setView(alertView);
        internetDiaolg.show();

        TextView nameAsInAadharTV = (TextView) alertView.findViewById(R.id.nameAsInAadharTV);
        TextView nameAsInSeccTV = (TextView) alertView.findViewById(R.id.nameAsInSeccTV);
        TextView nameAsInAdhar = (TextView) alertView.findViewById(R.id.nameAsInAdhar);
        nameAsInAdhar.setText(context.getResources().getString(R.string.plzEnterNameAsId));
        nameAsInAadharTV.setText(voterIdName);
        nameAsInSeccTV.setText(seccItem.getName());
        Button tryAgainBT = (Button) alertView.findViewById(R.id.tryAgainBT);
        tryAgainBT.setText(context.getResources().getString(R.string.Confirm_Submit));
        final Button cancelBT = (Button) alertView.findViewById(R.id.cancelBT);
        tryAgainBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                submitDetail(voterIdNumber, voterIdName);

            }
        });
        cancelBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                internetDiaolg.dismiss();
            }
        });
    }

    private void submitDetail(String voterIdNumber, String voterIdName) {
        if (seccItem != null) {
            //  seccItem.setAadhaarStatus(aadhaarStatus);
            seccItem.setConsent(consent);
            seccItem.setIdType(item.statusCode + "");
            seccItem.setIdNo(voterIdNumber);
            seccItem.setNameAsId(voterIdName);

            seccItem.setGovtIdPhoto(voterIdImg);

            seccItem.setGovtIdSurveyedStat(AppConstant.SURVEYED + "");
            seccItem.setLockedSave(AppConstant.SAVE + "");
            if (seccItem != null && seccItem.getDataSource() != null && seccItem.getDataSource().trim().equalsIgnoreCase(AppConstant.RSBY_SOURCE)) {
                if (selectedMemItem.getOldHeadMember() != null && selectedMemItem.getNewHeadMember() != null) {
                    SeccMemberItem oldHead = selectedMemItem.getOldHeadMember();
                    oldHead.setLockedSave(AppConstant.LOCKED + "");
                    AppUtility.showLog(AppConstant.LOG_STATUS, TAG, " OLD Household name " +
                            ": " + oldHead.getName() + "" +
                            " Member Status " + oldHead.getMemStatus() + " House hold Status :" +
                            " " + oldHead.getHhStatus() + " Locked Save :" + oldHead.getLockedSave());
                    SeccDatabase.updateRsbyMember(selectedMemItem.getOldHeadMember(), context);
                }
                SeccDatabase.updateRsbyMember(seccItem, context);
                SeccDatabase.updateHouseHold(selectedMemItem.getHouseHoldItem(), context);
                seccItem = SeccDatabase.getSeccMemberDetail(seccItem, context);
                selectedMemItem.setSeccMemberItem(seccItem);
                ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                        AppConstant.SELECTED_ITEM_FOR_VERIFICATION, selectedMemItem.serialize(), context);

            } else {
                if (selectedMemItem.getOldHeadMember() != null && selectedMemItem.getNewHeadMember() != null) {
                    SeccMemberItem oldHead = selectedMemItem.getOldHeadMember();
                    oldHead.setLockedSave(AppConstant.LOCKED + "");
                    AppUtility.showLog(AppConstant.LOG_STATUS, TAG, " OLD Household name " +
                            ": " + oldHead.getName() + "" +
                            " Member Status " + oldHead.getMemStatus() + " House hold Status :" +
                            " " + oldHead.getHhStatus() + " Locked Save :" + oldHead.getLockedSave());
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
            finish();
            rightTransition();

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


    private void checkAppConfig() {
        StateItem selectedStateItem = StateItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SELECTED_STATE, context));
        ArrayList<ConfigurationItem> configList = SeccDatabase.findConfiguration(selectedStateItem.getStateCode(), context);

        if (configList != null) {
            for (ConfigurationItem item1 : configList) {

                if (item1.getConfigId().equalsIgnoreCase(AppConstant.APPLICATION_ZOOM)) {
                    zoomMode = item1.getStatus();
                }


            }
        }

    }

    private void openGovtIdDataFragment() {
        GovtDetailsModel govtDetailsModel =new GovtDetailsModel();
        govtDetailsModel.setImage(voterIdImg);
        govtDetailsModel.setIdNumber(voterIdCardNumberET.getText().toString());
        govtDetailsModel.setGovtIdType(item.status);
        GovtIdDataFragment fragment = new GovtIdDataFragment();
        fragment.setGovtDetailsModel(govtDetailsModel);
        fragmentTransection = fragmentManager.beginTransaction();
        fragmentTransection.add(R.id.fragContainer, fragment);
        fragmentTransection.commitAllowingStateLoss();
    }

}
