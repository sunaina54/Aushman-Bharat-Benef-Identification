package com.nhpm.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.customComponent.CustomAlert;
import com.nhpm.CameraUtils.CommonUtilsImageCompression;
import com.nhpm.CameraUtils.squarecamera.CameraActivity;
import com.nhpm.LocalDataBase.DatabaseHelpers;
import com.nhpm.Models.FamilyMemberModel;
import com.nhpm.Models.request.FamilyDetailsItemModel;
import com.nhpm.Models.request.PersonalDetailItem;
import com.nhpm.Models.request.PrintCardItem;
import com.nhpm.Models.response.DocsListItem;
import com.nhpm.Models.response.GovernmentIdItem;
import com.nhpm.Models.response.SearchResult;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;
import com.nhpm.Utility.AppUtility;
import com.nhpm.activity.BlockDetailActivity;
import com.nhpm.activity.CollectDataActivity;
import com.nhpm.activity.FamilyMemberEntryActivity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FamilyDetailsFragment extends Fragment {
    private View view;
    private FragmentTransaction fragmentTransection;
    private FragmentManager fragmentManager;
    private FamilyAdapter adapter;
    private Context context;
    private ArrayList<GovernmentIdItem> govtIdStatusList;
    private Spinner govtIdSP;
    private TextView beneficiaryNameTV;
    private GovernmentIdItem item;
    private Button captureImageBT;
    private Bitmap captureImageBM;
    private CollectDataActivity activity;
    private DocsListItem beneficiaryListItem;
    private String voterIdImg;
    private ImageView beneficiaryPhotoIV;
    private int CAMERA_PIC_REQUEST = 0;
    private Button previousBT;
    private ImageView addIV;
    private PersonalDetailItem personalDetailItem;
    private LinearLayout addFamilyMemberLL;
    private RecyclerView memberRecycle;
    private ArrayList<FamilyMemberModel> familyMembersList;
    private FamilyDetailsItemModel familyDetailsItemModel;
    private EditText govtIdET;
    private Button getFamilyScoreBT, nextBT;
    public static String FAMILY_DETAIL = "familyDetail";
    public static String INDEX = "Index";
    private LinearLayout scoreLL;
    private PrintCardItem printCardItem;


    public FamilyDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_family_details, container, false);
        context = getActivity();
        setupScreen(view);
        return view;

    }

    private void setupScreen(View view) {
        Bundle bundle = getArguments();
        //bundle.getString("personalDetail");
       /* personalDetailItem = PersonalDetailItem.create(bundle.getString("personalDetail"));
        familyDetailsItemModel = FamilyDetailsItemModel.create(bundle.getString("personalDetail"));*/
        fragmentManager = getActivity().getSupportFragmentManager();

        govtIdSP = (Spinner) view.findViewById(R.id.govtIdSP);
        govtIdET = (EditText) view.findViewById(R.id.govtIdET);
        getFamilyScoreBT = (Button) view.findViewById(R.id.getFamilyScoreBT);
        prepareGovernmentIdSpinner();
        memberRecycle = (RecyclerView) view.findViewById(R.id.memberRecycle);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        memberRecycle.setLayoutManager(layoutManager);
        memberRecycle.setItemAnimator(new DefaultItemAnimator());
        addFamilyMemberLL = (LinearLayout) view.findViewById(R.id.addFamilyMemberLL);
        beneficiaryNameTV = (TextView) view.findViewById(R.id.beneficiaryNameTV);
        captureImageBT = (Button) view.findViewById(R.id.captureImageBT);
        beneficiaryPhotoIV = (ImageView) view.findViewById(R.id.beneficiaryPhotoIV);
        scoreLL = (LinearLayout) view.findViewById(R.id.scoreLL);
        scoreLL.setVisibility(View.GONE);
        //getFamilyScoreBT.setVisibility(View.VISIBLE);
        getFamilyScoreBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomAlert.alertWithOk(context, "Under Development");
            }
        });
        captureImageBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
            }
        });

        if (beneficiaryListItem != null) {
            beneficiaryNameTV.setText(beneficiaryListItem.getName());
            familyDetailsItemModel = beneficiaryListItem.getFamilyDetailsItemModel();
            printCardItem = beneficiaryListItem.getPrintCardDetail();


            if (familyDetailsItemModel != null) {
                familyMembersList = familyDetailsItemModel.getFamilyMemberModels();
                if (familyDetailsItemModel.getIdImage() != null &&
                        !familyDetailsItemModel.getIdImage().equalsIgnoreCase("")) {
                    //updateScreen(personalDetailItem.getFamilyDetailsItem().getIdImage());
                    voterIdImg = familyDetailsItemModel.getIdImage();
                    beneficiaryPhotoIV.setImageBitmap(AppUtility.
                            convertStringToBitmap(familyDetailsItemModel.getIdImage()));
                    captureImageBT.setEnabled(false);
                    govtIdSP.setEnabled(false);


                }

               /* if(personalDetailItem.getFamilyDetailsItem().getIdType()!=null &&
                        personalDetailItem.getFamilyDetailsItem().getIdType().equalsIgnoreCase("")){
                    //govtIdSP.setSelection();
                }*/

                if (familyDetailsItemModel.getIdNumber() != null &&
                        !familyDetailsItemModel.getIdNumber().equalsIgnoreCase("")) {
                    govtIdET.setText(familyDetailsItemModel.getIdNumber());
                    govtIdET.setEnabled(false);
                    captureImageBT.setBackground(getResources().getDrawable(R.drawable.rounded_grey_button));
                    getFamilyScoreBT.setEnabled(false);
                    getFamilyScoreBT.setBackground(getResources().getDrawable(R.drawable.rounded_grey_button));
                }


                if (printCardItem != null) {
                    addFamilyMemberLL.setEnabled(false);
                }

                if (familyDetailsItemModel.getFamilyMemberModels() != null) {
                    familyMembersList = familyDetailsItemModel.getFamilyMemberModels();
                    scoreLL.setVisibility(View.VISIBLE);
                    getFamilyScoreBT.setEnabled(false);

                    getFamilyScoreBT.setBackground(getResources().getDrawable(R.drawable.rounded_grey_button));

                    refreshList(familyDetailsItemModel.getFamilyMemberModels());

                }

            }else {
                familyMembersList = new ArrayList<>();
                FamilyMemberModel item = new FamilyMemberModel();
                item.setName(beneficiaryListItem.getName());

                familyMembersList.add(item);
                refreshList(familyMembersList);
            }
        }
        addIV = (ImageView) view.findViewById(R.id.addIV);
        addIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomAlert.alertWithOk(context, "Under Development");
            }
        });
        previousBT = (Button) view.findViewById(R.id.previousBT);
        previousBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new PersonalDetailsFragment();

                //Bundle args = new Bundle();

           /*     if(personalDetailItem!=null && personalDetailItem.getFamilyDetailsItem()!=null){
                    familyDetailsItemModel= personalDetailItem.getFamilyDetailsItem();
                    personalDetailItem.setFamilyDetailsItem(familyDetailsItemModel);

                } else {*/
                familyDetailsItemModel = new FamilyDetailsItemModel();
                if(!govtIdET.getText().toString().equalsIgnoreCase("")) {
                    familyDetailsItemModel.setIdNumber(govtIdET.getText().toString());
                }
                if(item.statusCode!=0) {
                    familyDetailsItemModel.setIdType(item.status);
                }
                if(voterIdImg!=null && !voterIdImg.equalsIgnoreCase("")) {
                    familyDetailsItemModel.setIdImage(voterIdImg);
                }

                familyDetailsItemModel.setFamilyMemberModels(familyMembersList);

                beneficiaryListItem.setFamilyDetailsItemModel(familyDetailsItemModel);
                fragmentTransection = fragmentManager.beginTransaction();
                fragmentTransection.add(R.id.fragContainer, fragment);
                fragmentTransection.commitAllowingStateLoss();
            }
        });

        nextBT = (Button) view.findViewById(R.id.nextBT);
        nextBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //familyDetailsItemModel = new FamilyDetailsItemModel();
                String govtId = govtIdET.getText().toString();
                // String idType=item.status;
                if (item.statusCode == 0) {
                    CustomAlert.alertWithOk(context, "Please select family id");
                    return;
                }
                if (govtId.equalsIgnoreCase("")) {
                    CustomAlert.alertWithOk(context, "Please enter family id number");
                    return;
                }

                if (voterIdImg == null || voterIdImg.equalsIgnoreCase("")) {
                    CustomAlert.alertWithOk(context, "Please capture family id photo");
                    return;
                }
                if (familyMembersList.size() < 2) {
                    CustomAlert.alertWithOk(context, "Please add atleast two member as per family id");
                    return;
                }
                familyDetailsItemModel = new FamilyDetailsItemModel();
                familyDetailsItemModel.setIdNumber(govtIdET.getText().toString());
                familyDetailsItemModel.setIdType(item.status);
                familyDetailsItemModel.setIdImage(voterIdImg);
                familyDetailsItemModel.setFamilyMemberModels(familyMembersList);


                // personalDetailItem.setFamilyDetailsItem(familyDetailsItemModel);

                // }

              /*  if (personalDetailItem != null) {
                    args.putString("personalDetail", personalDetailItem.serialize());
                }*/
                // fragment.setArguments(args);
                PrintCardItem printCard = new PrintCardItem();
                printCard.setBenefPhoto(activity.benefItem.getPersonalDetail().getBenefPhoto());
                printCard.setNameOnCard(activity.benefItem.getName());
                printCard.setFatherNameOnCard(activity.benefItem.getFathername());
                printCard.setGenderOnCard(activity.benefItem.getGenderid());
                String ahltin=activity.benefItem.getAhl_tin();
                if(ahltin!=null && !ahltin.equalsIgnoreCase("")){
                    Log.d("TAG","AhlTine : "+ahltin);
                    String firstTwoChar=ahltin.substring(0,2);
                    // 2 7 8 5 4 3
                    String nextSevenChar=ahltin.substring(2,9);
                    String nextEightChar=ahltin.substring(9,17);
                    String nextFiveChar=ahltin.substring(17,22);
                    String nextFourChar=ahltin.substring(22,26);
                    String lastThreeChar=ahltin.substring(26,29);
                    Log.d("TAG","AhlTine : "+ahltin);
                    Log.d("TAG","First TTwo : "+firstTwoChar);
                    Log.d("TAG","next seven : "+nextSevenChar);
                    Log.d("TAG","next eight : "+nextEightChar);
                    Log.d("TAG","next five : "+nextFiveChar);
                    Log.d("TAG","next four : "+nextFourChar);
                    Log.d("TAG","next three : "+lastThreeChar);
                    ahltin=firstTwoChar+" "+nextSevenChar+" "+nextEightChar+" "+nextFiveChar+" "+nextFourChar+" "+lastThreeChar;
                    Log.d("TAG","Ayushman Id  : "+ahltin);
                    Log.d("TAG","Ayushman Id  : "+ahltin);
                    printCard.setCardNo(ahltin);
                    printCard.setStateName(beneficiaryListItem.getState_name_english());


                }
                if (activity.benefItem.getDob() != null && activity.benefItem.getDob().length() >= 4) {
                    printCard.setYobObCard(activity.benefItem.getDob().substring(0, 4));
                }
                beneficiaryListItem.setPrintCardDetail(printCard);
                beneficiaryListItem.setFamilyDetailsItemModel(familyDetailsItemModel);
                activity.benefItem = beneficiaryListItem;

                activity.personalDetailsLL.setBackground(context.getResources().getDrawable(R.drawable.arrow));
                activity.familyDetailsLL.setBackground(context.getResources().getDrawable(R.drawable.arrow));
                activity.printEcardLL.setBackground(context.getResources().getDrawable(R.drawable.arrow_yellow));


                Fragment fragment = new PrintCardFragment();
                //Bundle args = new Bundle();

                //args.putString("familyDetail", fa.serialize());

                //fragment.setArguments(args);
                CallFragment(fragment);
                //CustomAlert.alertWithOk(context,"Under Development");

            }
        });
        govtIdSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // AadhaarStatusItem item=aadhaarStatusList.get(position);
                item = govtIdStatusList.get(position);
                // AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Setup screen Selected id" + item.statusCode);
                switch (item.statusCode) {

                    /*case NO_GOVID:
                        voterIdLayout.setVisibility(View.GONE);
                        govtIdPhotoLayout.setVisibility(View.GONE);

                        break;

                    case 0:
                        voterIdLayout.setVisibility(View.GONE);
                        govtIdPhotoLayout.setVisibility(View.GONE);
                        *//*voterIdCardNumberET.setText("");
                        voterIdCardNameET.setText("");*//*
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
                        *//*voterIdCardNumberET.setText("");
                        voterIdCardNameET.setText("");*//*
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
                       *//* rationCardLayout.setVisibility(View.GONE);
                        enrollmentLayout.setVisibility(View.VISIBLE);*//*
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
                       *//* rationCardLayout.setVisibility(View.GONE);
                        enrollmentLayout.setVisibility(View.GONE);*//*
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
                       *//* rationCardLayout.setVisibility(View.VISIBLE);
                        enrollmentLayout.setVisibility(View.GONE);*//*
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
                        *//*voterIdCardNumberET.setText("");

                        voterIdCardNameET.setText("");*//*
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
                        updateScreen(voterIdImg);*/

                    // break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        addFamilyMemberLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FamilyMemberModel item = null;
                if (familyMembersList != null && familyMembersList.size() > 0) {
                    for (FamilyMemberModel familyMemberModel : familyMembersList) {
                        item = familyMemberModel;
                    }
                }
                Intent theIntent = new Intent(context, FamilyMemberEntryActivity.class);
                theIntent.putExtra(AppConstant.FAMILY_MEMBER_RESULT_CODE_NAME, item);
                startActivityForResult(theIntent, AppConstant.FAMILY_MEMBER_REQUEST_CODE_VALUE);
            }
        });


    }

    public void CallFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragContainer, fragment);
            fragmentTransaction.commit();
        }
    }

    private void prepareGovernmentIdSpinner() {
        govtIdStatusList = AppUtility.prepareGovernmentIdSpinnerList();
        ArrayList<String> spinnerList = new ArrayList<>();
        for (GovernmentIdItem item : govtIdStatusList) {
            spinnerList.add(item.status);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.custom_drop_down, R.id.textView, spinnerList);
        govtIdSP.setAdapter(adapter);

        govtIdSP.setSelection(1);

        if (personalDetailItem != null && personalDetailItem.getFamilyDetailsItem() != null) {

            govtIdSP.setSelection(1);
        }
    }

    private void openCamera() {
        AppUtility.capturingType = AppConstant.capturingModeGovId;

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

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        //activity = (CaptureAadharDetailActivity) context;
        // if (activity instanceof CollectDataActivity) {
        activity = (CollectDataActivity) context;

        beneficiaryListItem = activity.benefItem;
        // }
       /* if (ekycActivity instanceof EkycActivity) {
            ekycActivity = (EkycActivity) context;
        }*/
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

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
                captureImageBM = MediaStore.Images.Media.getBitmap(context.getContentResolver(), compressedUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Log.d(TAG," Bitmap Size : "+image.getAllocationByteCount());
            voterIdImg = AppUtility.convertBitmapToString(captureImageBM);
            updateScreen(voterIdImg);
        }

        if (requestCode == AppConstant.FAMILY_MEMBER_REQUEST_CODE_VALUE) {
            if (resultCode == Activity.RESULT_OK) {

                // Utility.scrollToEnd(scrollView);
                //  Toast.makeText(context, "Result Recieved", Toast.LENGTH_LONG).show();
                FamilyMemberModel item = (FamilyMemberModel) data.getSerializableExtra(AppConstant.FAMILY_MEMBER_RESULT_CODE_NAME);
                String index = data.getStringExtra(INDEX);
                if (item != null) {
                    if (index != null) {
                        familyMembersList.set(Integer.parseInt(index), item);
                        refreshList(familyMembersList);
                    } else {
                        familyMembersList.add(item);
                        refreshList(familyMembersList);
                    }
                }
                scoreLL.setVisibility(View.GONE);
                if (familyMembersList.size() > 0) {
                    scoreLL.setVisibility(View.VISIBLE);
                }
                refreshList(familyMembersList);
            }
        }
    }

    private void refreshList(ArrayList<FamilyMemberModel> familyMembersList) {
        if (familyMembersList != null) {
            adapter = new FamilyAdapter(context, familyMembersList);
            memberRecycle.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    private class FamilyAdapter extends RecyclerView.Adapter<FamilyAdapter.MyViewHolder> {

        View view;
        private ArrayList<FamilyMemberModel> dataSet;


        public class MyViewHolder extends RecyclerView.ViewHolder {
            RelativeLayout menuLayout;
            ImageView settings;
            TextView nameTV;


            public MyViewHolder(final View itemView) {
                super(itemView);
                nameTV = (TextView) itemView.findViewById(R.id.nameTV);
                settings = (ImageView) itemView.findViewById(R.id.settings);
                menuLayout = (RelativeLayout) itemView.findViewById(R.id.menuLayout);
            }
        }


        public FamilyAdapter(Context context, ArrayList<FamilyMemberModel> data) {
            this.dataSet = data;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.member_data_row, parent, false);
            MyViewHolder myViewHolder = new MyViewHolder(view);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {


            final FamilyMemberModel item = dataSet.get(listPosition);
           /* String aadhaarNo = "";
            holder.houseHoldIdTV.setText(item.getHouseholdId());
            if(item.getAadhaarNo()!=null && !item.getAadhaarNo().equalsIgnoreCase("")) {
                aadhaarNo = "XXXXXXXX" + item.getAadhaarNo().substring(8);
            }
            holder.houseHoldAadhaarNoTV.setText(aadhaarNo);*/
            holder.nameTV.setText(item.getName());
            if (beneficiaryListItem.getPrintCardDetail() != null) {
                holder.menuLayout.setVisibility(View.GONE);
                holder.settings.setVisibility(View.GONE);
            }

            editDelete(holder.menuLayout, holder.settings, item, listPosition);

            /*if(item.getStatus().equalsIgnoreCase(AppConstant.SYNC_STATUS)){
                holder.editActionTV.setBackgroundColor(context.getResources().getColor(R.color.sync_status_color));
                holder.editActionTV.setText("Synced");
                holder.editActionTV.setTextColor(context.getResources().getColor(R.color.white));
                holder.editActionTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ProjectPrefrence.saveSharedPrefrenceData(
                                AppConstant.PROJECT_PREF, AppConstant.ACTION_TO_PERFORM,AppConstant.VIEW_ACTION, context);
                        ProjectPrefrence.saveSharedPrefrenceData(
                                AppConstant.PROJECT_PREF,AppConstant.SELECTED_HOUSEHOLD_ITEM,item.serialize(),context);
                        Intent theIntent = new Intent(context, PreviewActivity.class);
                        context.startActivity(theIntent);
                    }
                });

            }else if(item.getStatus().equalsIgnoreCase(AppConstant.LOCK_STATUS)){
                holder.editActionTV.setBackgroundColor(context.getResources().getColor(R.color.lock_status_color));
                holder.editActionTV.setText("Locked");
                holder.editActionTV.setTextColor(context.getResources().getColor(R.color.white));
                holder.editActionRL.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openResetHousehold(item,holder.editActionRL,context,getActivity());

                    }
                });

            }*/
         /*   holder.editActionRL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openResetHousehold(item,holder.editActionRL);
                }
            });*/

        }

        @Override
        public int getItemCount() {
            return dataSet.size();
        }


    }


    private void updateScreen(String idImage) {
        try {
            if (idImage != null) {
                beneficiaryPhotoIV.setImageBitmap(AppUtility.convertStringToBitmap(idImage));
            } else {
//                if (seccItem.getGovtIdPhoto() != null && !seccItem.getGovtIdPhoto().equalsIgnoreCase("")) {
//
//                } else {
                beneficiaryPhotoIV.setImageBitmap(null);
//                }
            }
        } catch (Exception e) {

        }
    }

    void editDelete(RelativeLayout menuLayout, final ImageView settings, final FamilyMemberModel item1, final int index) {
        settings.setVisibility(View.VISIBLE);

        menuLayout.setVisibility(View.VISIBLE);
        menuLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(context, settings);
                popup.getMenuInflater()
                        .inflate(R.menu.menu_edit, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.edit:
                                Intent theIntent = new Intent(context, FamilyMemberEntryActivity.class);
                                theIntent.putExtra(AppConstant.FAMILY_MEMBER_RESULT_CODE_NAME, item1);
                                theIntent.putExtra(INDEX, index + "");
                                startActivityForResult(theIntent, AppConstant.FAMILY_MEMBER_REQUEST_CODE_VALUE);
                                break;
                            case R.id.delete:

                                familyMembersList.remove(index);
                                refreshList(familyMembersList);
                                break;

                        }
                        return true;
                    }
                });
                popup.show();

            }
        });
    }


}
