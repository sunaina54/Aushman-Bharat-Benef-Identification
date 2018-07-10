package com.nhpm.fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.customComponent.CustomAsyncTask;
import com.customComponent.TaskListener;
import com.customComponent.utility.CustomHttp;
import com.customComponent.utility.DateTimeUtil;
import com.customComponent.utility.ProjectPrefrence;
import com.nhpm.CameraUtils.CommonUtilsImageCompression;
import com.nhpm.CameraUtils.squarecamera.CameraActivity;
import com.nhpm.LocalDataBase.DatabaseHelpers;
import com.nhpm.Models.FamilyCardList;
import com.nhpm.Models.FamilyMemberModel;
import com.nhpm.Models.request.FamilyDetailsItemModel;
import com.nhpm.Models.request.GetMemberDetail;
import com.nhpm.Models.request.GetSearchParaRequestModel;
import com.nhpm.Models.request.PersonalDetailItem;
import com.nhpm.Models.request.PrintCardItem;
import com.nhpm.Models.request.SearchByRationRequestModel;
import com.nhpm.Models.response.DocsListItem;
import com.nhpm.Models.response.FamilyDetailResponse;
import com.nhpm.Models.response.FamilyListResponseItem;
import com.nhpm.Models.response.GenericResponse;
import com.nhpm.Models.response.GetSearchParaResponseModel;
import com.nhpm.Models.response.GovernmentIdItem;
import com.nhpm.Models.response.PersonalDetailResponse;
import com.nhpm.Models.response.SearchResult;
import com.nhpm.Models.response.verifier.VerifierLoginResponse;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;
import com.nhpm.Utility.AppUtility;
import com.nhpm.activity.BlockDetailActivity;
import com.nhpm.activity.CollectDataActivity;
import com.nhpm.activity.FamilyMemberEntryActivity;
import com.nhpm.activity.FamilyMemberMatchActivity;
import com.nhpm.activity.FamilyMembersListActivity;
import com.nhpm.activity.LoginActivity;
import com.nhpm.activity.ViewMemberDataActivity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class FamilyDetailsFragment extends Fragment {
    private Integer familyScore, nameScore;
    private AlertDialog alert;
    private View view;
    private GetMemberDetail getMemberDetailResponse;
    private FragmentTransaction fragmentTransection;

    private CustomAsyncTask customAsyncTask;
    private FragmentManager fragmentManager;
    private String familyResponse, responseRation;
    private GenericResponse genericResponse;
    private FamilyAdapter adapter;
    private ExistingFamilyAdapter existingFamilyAdapter;
    private OldFamilyAdapter oldMemberAdapter;
    private Context context;
    private ArrayList<GovernmentIdItem> govtIdStatusList;
    private Spinner govtIdSP;
    private TextView beneficiaryNameTV, familyMatchScoreTV;
    private LinearLayout familyScoreLL;
    private GovernmentIdItem item;
    private Button captureImageBT;
    private Bitmap captureImageBM;
    private CollectDataActivity activity;
    private DocsListItem beneficiaryListItem;
    private String voterIdImg;
    private ImageView beneficiaryPhotoIV;
    private int CAMERA_PIC_REQUEST = 0;
    private Button previousBT, searchBT,editBT;
    private ImageView addIV;
    private PersonalDetailItem personalDetailItem;
    private LinearLayout addFamilyMemberLL, familyDetailsLL;
    private RecyclerView memberRecycle, oldMemberRecycle;
    private ArrayList<FamilyMemberModel> familyMembersList;
    private FamilyDetailsItemModel familyDetailsItemModel;
    private EditText govtIdET;
    private Button getFamilyScoreBT, nextBT, submitBT;
    private String familyMatchScore = "",familyMatchScoreStatus="";
    public static String FAMILY_DETAIL = "familyDetail";
    public static String INDEX = "Index";
    private LinearLayout scoreLL;
    private PrintCardItem printCardItem;
    private VerifierLoginResponse verifierDetail;
    private FamilyCardList familyCardList;
    private String FAMILY_CARD_LIST = "FAMILY_CARD_LIST";
    private String searchTag="";

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
        /*Bundle bundle = getArguments();

        familyMatchScore=bundle.getString("familyMatchScore");*/
       /* personalDetailItem = PersonalDetailItem.create(bundle.getString("personalDetail"));
        familyDetailsItemModel = FamilyDetailsItemModel.create(bundle.getString("personalDetail"));*/
        fragmentManager = getActivity().getSupportFragmentManager();
        verifierDetail = VerifierLoginResponse.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.VERIFIER_CONTENT, context));
        // familyCardList=FamilyCardList.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,))
        govtIdSP = (Spinner) view.findViewById(R.id.govtIdSP);
        govtIdET = (EditText) view.findViewById(R.id.govtIdET);
        getFamilyScoreBT = (Button) view.findViewById(R.id.getFamilyScoreBT);
        submitBT = (Button) view.findViewById(R.id.submitBT);
        prepareGovernmentIdSpinner();
        searchBT = (Button) view.findViewById(R.id.searchBT);
        editBT = (Button) view.findViewById(R.id.editBT);
        familyDetailsLL = (LinearLayout) view.findViewById(R.id.familyDetailsLL);
        familyDetailsLL.setVisibility(View.GONE);

        memberRecycle = (RecyclerView) view.findViewById(R.id.memberRecycle);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        memberRecycle.setLayoutManager(layoutManager);
        memberRecycle.setItemAnimator(new DefaultItemAnimator());

        oldMemberRecycle = (RecyclerView) view.findViewById(R.id.oldMemberRecycle);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(context);
        oldMemberRecycle.setLayoutManager(layoutManager1);
        oldMemberRecycle.setItemAnimator(new DefaultItemAnimator());


        addFamilyMemberLL = (LinearLayout) view.findViewById(R.id.addFamilyMemberLL);
        beneficiaryNameTV = (TextView) view.findViewById(R.id.beneficiaryNameTV);
        captureImageBT = (Button) view.findViewById(R.id.captureImageBT);
        beneficiaryPhotoIV = (ImageView) view.findViewById(R.id.beneficiaryPhotoIV);
        scoreLL = (LinearLayout) view.findViewById(R.id.scoreLL);
        //scoreLL.setVisibility(View.GONE);
        familyScoreLL = (LinearLayout) view.findViewById(R.id.familyScoreLL);
        familyMatchScoreTV = (TextView) view.findViewById(R.id.familyMatchScoreTV);
        //getFamilyScoreBT.setVisibility(View.VISIBLE);
        searchBT.setVisibility(View.VISIBLE);
        editBT.setVisibility(View.GONE);
        searchBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String rationCard = govtIdET.getText().toString();
                if (rationCard != null && !rationCard.equalsIgnoreCase("")) {
                    getFamilyDetailsByRationCard(rationCard);
                }
            }
        });
        editBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                govtIdET.setText("");
                govtIdET.setEnabled(true);
                resetData();
                searchBT.setVisibility(View.VISIBLE);
                editBT.setVisibility(View.GONE);
            }
        });
        getFamilyScoreBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, FamilyMemberMatchActivity.class);
                if (beneficiaryListItem != null && beneficiaryListItem.getOldMembers() != null && beneficiaryListItem.getOldMembers().size() > 0) {
                    intent.putExtra("Old_Members", beneficiaryListItem.getOldMembers());
                }
                if (familyMembersList != null && familyMembersList.size() > 0) {
                    intent.putExtra("Family_Card_Members", familyMembersList);
                }
                startActivityForResult(intent, 3);

                // getFamilyMatchScore();
                //familyDetailsItemModel.setFamilyMatchScore(Integer.parseInt(familyMatchScore));
                // CustomAlert.alertWithOk(context, "Under Development");
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
            if (beneficiaryListItem.getOldMembers() != null && beneficiaryListItem.getOldMembers().size() > 0) {
                // oldMemberRefreshList(beneficiaryListItem.getOldMembers());
            }
            familyDetailsItemModel = beneficiaryListItem.getFamilyDetailsItemModel();
            printCardItem = beneficiaryListItem.getPrintCardDetail();

            if (familyDetailsItemModel != null) {
                searchTag=familyDetailsItemModel.getExistingData();
                familyDetailsLL.setVisibility(View.VISIBLE);
                if(familyDetailsItemModel.getExistingData()!=null &&
                        searchTag.equalsIgnoreCase("SearchByRation")){
                    searchBT.setEnabled(false);
                    searchBT.setBackground(getResources().getDrawable(R.drawable.rounded_grey_button));
                    addFamilyMemberLL.setEnabled(false);
                    addFamilyMemberLL.setBackground(getResources().getDrawable(R.drawable.rounded_grey_button));
                    govtIdET.setEnabled(false);
                    captureImageBT.setEnabled(false);
                    captureImageBT.setBackground(getResources().getDrawable(R.drawable.rounded_grey_button));
                }else {
                    addFamilyMemberLL.setEnabled(true);
                    addFamilyMemberLL.setBackground(getResources().getDrawable(R.drawable.rounded_shape_yello_button));
                    govtIdET.setEnabled(true);
                    captureImageBT.setEnabled(true);
                    captureImageBT.setBackground(getResources().getDrawable(R.drawable.rounded_shape_yello_button));
                }

                if (familyDetailsItemModel.getFamilyMatchScore() != null) {
                    familyMatchScore = familyDetailsItemModel.getFamilyMatchScore() + "";
                }
                if (!familyMatchScore.equalsIgnoreCase("")) {
                    familyScoreLL.setVisibility(View.VISIBLE);
                    familyMatchScoreTV.setText(familyMatchScore + "%");
                }
                familyMembersList = familyDetailsItemModel.getFamilyMemberModels();
                if (familyDetailsItemModel.getIdImage() != null &&
                        !familyDetailsItemModel.getIdImage().equalsIgnoreCase("")) {
                    //updateScreen(personalDetailItem.getFamilyDetailsItem().getIdImage());
                    voterIdImg = familyDetailsItemModel.getIdImage();
                    beneficiaryPhotoIV.setImageBitmap(AppUtility.
                            convertStringToBitmap(familyDetailsItemModel.getIdImage()));


                    govtIdSP.setEnabled(false);


                }

                //  if(familyDetailsItemModel!=null && familyDetailsItemModel.getFamilyMatchScore())
               /* if(personalDetailItem.getFamilyDetailsItem().getIdType()!=null &&
                        personalDetailItem.getFamilyDetailsItem().getIdType().equalsIgnoreCase("")){
                    //govtIdSP.setSelection();
                }*/

                if (familyDetailsItemModel.getIdNumber() != null &&
                        !familyDetailsItemModel.getIdNumber().equalsIgnoreCase("")) {
                    govtIdET.setText(familyDetailsItemModel.getIdNumber());
                    govtIdET.setEnabled(false);
                }


                if (printCardItem != null) {
                    familyDetailsLL.setVisibility(View.VISIBLE);
                    govtIdET.setEnabled(false);
                    searchBT.setEnabled(false);
                    searchBT.setBackground(getResources().getDrawable(R.drawable.rounded_grey_button));
                    getFamilyScoreBT.setEnabled(false);

                    getFamilyScoreBT.setBackground(getResources().getDrawable(R.drawable.rounded_grey_button));
                    addFamilyMemberLL.setEnabled(false);
                    addFamilyMemberLL.setBackground(getResources().getDrawable(R.drawable.rounded_grey_button));

                    captureImageBT.setEnabled(false);
                    captureImageBT.setBackground(getResources().getDrawable(R.drawable.rounded_grey_button));
                }

                if (familyDetailsItemModel.getFamilyMemberModels() != null) {
                    familyMembersList = familyDetailsItemModel.getFamilyMemberModels();
                    //scoreLL.setVisibility(View.VISIBLE);
                    if(familyDetailsItemModel.getExistingData()!=null &&
                            searchTag.equalsIgnoreCase("SearchByRation")){
                        refreshListWithExistingMember(familyMembersList);
                    }else {
                        refreshList(familyDetailsItemModel.getFamilyMemberModels());
                    }
                }

            } /*else {
                familyMembersList = new ArrayList<>();
                FamilyMemberModel item = new FamilyMemberModel();
                item.setName(beneficiaryListItem.getName());
                familyMembersList.add(item);
                refreshList(familyMembersList);
            }*/
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
                if (!govtIdET.getText().toString().equalsIgnoreCase("")) {
                    familyDetailsItemModel.setIdNumber(govtIdET.getText().toString());
                }
                if (item.statusCode != 0) {
                    familyDetailsItemModel.setIdType(item.status);
                }
                if (voterIdImg != null && !voterIdImg.equalsIgnoreCase("")) {
                    familyDetailsItemModel.setIdImage(voterIdImg);
                }
                familyDetailsItemModel.setExistingData(searchTag);
                if (getMemberDetailResponse != null && getMemberDetailResponse.getFamilyDetailsItem() != null &&
                        getMemberDetailResponse.getFamilyDetailsItem().getIdNumber() != null &&
                        !getMemberDetailResponse.getFamilyDetailsItem().getIdNumber().equalsIgnoreCase("")) {
                    searchTag="SearchByRation";
                    familyDetailsItemModel.setExistingData(searchTag);
                }


                familyDetailsItemModel.setFamilyMemberModels(familyMembersList);
                if (!familyMatchScore.equalsIgnoreCase(""))
                    familyDetailsItemModel.setFamilyMatchScore(Integer.parseInt(familyMatchScore));


                beneficiaryListItem.setFamilyDetailsItemModel(familyDetailsItemModel);
                activity.benefItem = beneficiaryListItem;
                fragmentTransection = fragmentManager.beginTransaction();
                fragmentTransection.add(R.id.fragContainer, fragment);
                fragmentTransection.commitAllowingStateLoss();
            }
        });

        nextBT = (Button) view.findViewById(R.id.nextBT);
        nextBT.setVisibility(View.GONE);
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
                if (familyMatchScore == null || familyMatchScore.equalsIgnoreCase("")) {
                    CustomAlert.alertWithOk(context, "Please match family score");
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
                // familyDetailsItemModel.setFamilyMatchScore(78);
                familyDetailsItemModel.setFamilyMatchScore(Integer.parseInt(familyMatchScore));
                familyDetailsItemModel.setFamilyMemberModels(familyMembersList);
                beneficiaryListItem.setFamilyDetailsItemModel(familyDetailsItemModel);

                if (familyMatchScore != null && !familyMatchScore.equalsIgnoreCase("")) {
                    familyScore = Integer.parseInt(familyMatchScore);
                    nameScore = beneficiaryListItem.getPersonalDetail().getNameMatchScore();
                    if (familyScore >= 80 && nameScore >= 80) {
                        printCard();
                    } else {
                        CustomAlert.alertWithOk(context, getResources().getString(R.string.score_error_message));
                        return;
                    }
                }

               /* if (activity.isNetworkAvailable()) {

                    submitMemberData();
                } else {
                    CustomAlert.alertWithOk(context, getResources().getString(R.string.internet_connection_msg));
                }*/
                // personalDetailItem.setFamilyDetailsItem(familyDetailsItemModel);

                // }

              /*  if (personalDetailItem != null) {
                    args.putString("personalDetail", personalDetailItem.serialize());
                }*/
                // fragment.setArguments(args);
               /* PrintCardItem printCard = new PrintCardItem();
                printCard.setBenefPhoto(activity.benefItem.getPersonalDetail().getBenefPhoto());
                printCard.setNameOnCard(activity.benefItem.getName());
                printCard.setFatherNameOnCard(activity.benefItem.getFathername());
                printCard.setGenderOnCard(activity.benefItem.getGenderid());
                String ahltin = activity.benefItem.getAhl_tin();
                if (ahltin != null && !ahltin.equalsIgnoreCase("")) {
                    Log.d("TAG", "AhlTine : " + ahltin);
                    String firstTwoChar = ahltin.substring(0, 2);
                    // 2 7 8 5 4 3
                    String nextSevenChar = ahltin.substring(2, 9);
                    String nextEightChar = ahltin.substring(9, 17);
                    String nextFiveChar = ahltin.substring(17, 22);
                    String nextFourChar = ahltin.substring(22, 26);
                    String lastThreeChar = ahltin.substring(26, 29);
                    Log.d("TAG", "AhlTine : " + ahltin);
                    Log.d("TAG", "First TTwo : " + firstTwoChar);
                    Log.d("TAG", "next seven : " + nextSevenChar);
                    Log.d("TAG", "next eight : " + nextEightChar);
                    Log.d("TAG", "next five : " + nextFiveChar);
                    Log.d("TAG", "next four : " + nextFourChar);
                    Log.d("TAG", "next three : " + lastThreeChar);
                    ahltin = firstTwoChar + " " + nextSevenChar + "  " + nextEightChar + "  " + nextFiveChar + " " + nextFourChar + " " + lastThreeChar;
                    Log.d("TAG", "Ayushman Id  : " + ahltin);
                    Log.d("TAG", "Ayushman Id  : " + ahltin);
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
                CallFragment(fragment);*/

            }
        });


        submitBT.setOnClickListener(new View.OnClickListener() {
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
                if (familyMatchScore == null || familyMatchScore.equalsIgnoreCase("")) {
                    CustomAlert.alertWithOk(context, "Please match family score");
                    return;
                }
                if (familyMatchScore.equalsIgnoreCase("0")) {
                    // printCard();
                    CustomAlert.alertWithOk(context, "SECC Family members and Family card members does not matching.");
                    return;
                }
                if (govtId.length() != 12) {
                    CustomAlert.alertWithOk(context, "Please enter 12 -digit Rashan Card number");
                    return;
                }
               /* if (familyMembersList.size() < 2) {
                    CustomAlert.alertWithOk(context, "Please add atleast two member as per family id");
                    return;
                }*/
                familyDetailsItemModel = new FamilyDetailsItemModel();
                familyDetailsItemModel.setIdNumber(govtIdET.getText().toString());
                familyDetailsItemModel.setIdType(item.status);
                familyDetailsItemModel.setIdName(item.statusCode + "");
                familyDetailsItemModel.setIdImage(voterIdImg);
                familyDetailsItemModel.setFamilyMatchScore(Integer.parseInt(familyMatchScore));
                familyDetailsItemModel.setOperatorMatchScoreStatus(familyMatchScoreStatus);

                // familyDetailsItemModel.setFamilyMatchScore(78);
                familyDetailsItemModel.setFamilyMemberModels(familyMembersList);
                beneficiaryListItem.setFamilyDetailsItemModel(familyDetailsItemModel);

                if (familyMatchScore != null && !familyMatchScore.equalsIgnoreCase("")) {
                    familyScore = Integer.parseInt(familyMatchScore);
                    nameScore = beneficiaryListItem.getPersonalDetail().getNameMatchScore();

                    String msg = getResources().getString(R.string.score_message_verification);
                    final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle(context.getResources().getString(com.customComponent.R.string.Alert));
                    builder.setMessage(msg)
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //do things
                                    // printCard();
                                    submitMemberData();
                                    alert.dismiss();
                                }
                            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //do things
                            // printCard();
                            // submitMemberData();
                            alert.dismiss();
                        }
                    });
                    alert = builder.create();
                    alert.show();
                    //} else {
                       /* if (activity.isNetworkAvailable()) {
                            submitMemberData();
                        } else {
                            CustomAlert.alertWithOk(context, getResources().getString(R.string.internet_connection_msg));
                        }*/
                       /* String msg = getResources().getString(R.string.score_error_message);
                        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle(context.getResources().getString(com.customComponent.R.string.Alert));
                        builder.setMessage(msg)
                                .setCancelable(false)
                                .setPositiveButton(context.getResources().getString(com.customComponent.R.string.OK), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //do things
                                       // printCard();
                                        alert.dismiss();
                                    }
                                });
                        alert = builder.create();
                        alert.show();
                    }*/
                }
                /*if (activity.isNetworkAvailable()) {
                    submitMemberData();
                } else {
                    CustomAlert.alertWithOk(context, getResources().getString(R.string.internet_connection_msg));
                }*/


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
                theIntent.putExtra("FamilyMemberList",familyMembersList);
                theIntent.putExtra(AppConstant.FAMILY_MEMBER_RESULT_CODE_NAME, item);
                startActivityForResult(theIntent, AppConstant.FAMILY_MEMBER_REQUEST_CODE_VALUE);
            }
        });


    }

    private void printCard() {
        PrintCardItem printCard = new PrintCardItem();
        printCard.setBenefPhoto(activity.benefItem.getPersonalDetail().getBenefPhoto());
        printCard.setNameOnCard(activity.benefItem.getName());
        printCard.setFatherNameOnCard(activity.benefItem.getFathername());
        printCard.setGenderOnCard(activity.benefItem.getGenderid());
        String ahltin = activity.benefItem.getAhl_tin();
        if (beneficiaryListItem.getSource() != null && beneficiaryListItem.getSource().equalsIgnoreCase(AppConstant.RSBY_SOURCE_NEW)) {
            printCard.setCardNo(ahltin);
        } else {
            if (ahltin != null && !ahltin.equalsIgnoreCase("")) {
                Log.d("TAG", "AhlTine : " + ahltin);
                String firstTwoChar = ahltin.substring(0, 2);
                // 2 7 8 5 4 3
                String nextSevenChar = ahltin.substring(2, 9);
                String nextEightChar = ahltin.substring(9, 17);
                String nextFiveChar = ahltin.substring(17, 22);
                String nextFourChar = ahltin.substring(22, 26);
                String lastThreeChar = ahltin.substring(26, 29);
                Log.d("TAG", "AhlTine : " + ahltin);
                Log.d("TAG", "First TTwo : " + firstTwoChar);
                Log.d("TAG", "next seven : " + nextSevenChar);
                Log.d("TAG", "next eight : " + nextEightChar);
                Log.d("TAG", "next five : " + nextFiveChar);
                Log.d("TAG", "next four : " + nextFourChar);
                Log.d("TAG", "next three : " + lastThreeChar);
                ahltin = firstTwoChar + " " + nextSevenChar + "  " + nextEightChar + "  " + nextFiveChar + " " + nextFourChar + " " + lastThreeChar;
                Log.d("TAG", "Ayushman Id  : " + ahltin);
                Log.d("TAG", "Ayushman Id  : " + ahltin);
                printCard.setCardNo(ahltin);



            }
        }


        if(beneficiaryListItem.getState_name_english()!=null){
        printCard.setStateName(beneficiaryListItem.getState_name_english());}
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
        CallFragment(fragment);
    }

    private void submitMemberData() {
        TaskListener taskListener = new TaskListener() {
            @Override
            public void execute() {
                try {

                    // beneficiaryListItem.setFamilyDetailsItemModel(familyDetailsItemModel);
                    PersonalDetailItem personalDetailItem = beneficiaryListItem.getPersonalDetail();
                    FamilyDetailsItemModel familyMemberModel = beneficiaryListItem.getFamilyDetailsItemModel();

                    GetMemberDetail request = new GetMemberDetail();
                    request.setAhl_tin(beneficiaryListItem.getAhl_tin());
                    request.setHhd_no(beneficiaryListItem.getHhd_no());
                    if (beneficiaryListItem.getSource() != null && beneficiaryListItem.getSource().equalsIgnoreCase(AppConstant.RSBY_SOURCE_NEW)) {
                        request.setDataSource(AppConstant.RSBY_SOURCE_NEW);
                    } else {
                        request.setDataSource(AppConstant.SECC_SOURCE_NEW);

                    }
                    request.setStatecode(Integer.parseInt(verifierDetail.getStatecode()));

                    PersonalDetailResponse personalDetail = new PersonalDetailResponse();

                    personalDetail.setBenefName(personalDetailItem.getName());
                    personalDetail.setBenefPhoto(personalDetailItem.getBenefPhoto());
                    personalDetail.setGovtIdNo(personalDetailItem.getGovtIdNo());
                    personalDetail.setGovtIdType(personalDetailItem.getIdName());
                    personalDetail.setIdPhoto(personalDetailItem.getIdPhoto());
                    personalDetail.setIdPhoto1(personalDetailItem.getIdPhoto1());
                    personalDetail.setIsAadhar(personalDetailItem.getIsAadhar());
                    personalDetail.setMobileNo(personalDetailItem.getMobileNo());
                    personalDetail.setName(personalDetailItem.getBenefName());
                    personalDetail.setNameMatchScore(personalDetailItem.getNameMatchScore());
                    personalDetail.setIsMobileAuth(personalDetailItem.getIsMobileAuth());
                    personalDetail.setOpertaorid(personalDetailItem.getOpertaorid());
                    personalDetail.setFlowStatus(personalDetailItem.getFlowStatus());
                    personalDetail.setMemberType(personalDetailItem.getMemberType());

                    personalDetail.setDistrictNameBen(personalDetailItem.getDistrict());
                    personalDetail.setSubDistrictBen(personalDetailItem.getSubDistrictBen());
                    personalDetail.setVtcBen(personalDetailItem.getVtcBen());

                    personalDetail.setDobBen(personalDetailItem.getYob());
                    personalDetail.setEmailBen(personalDetailItem.getEmailBen());
                    String gender = "";
                    if (personalDetailItem.getGender() != null) {

                        gender = personalDetailItem.getGender().toUpperCase().substring(0, 1);
                        if (gender.equalsIgnoreCase("M") || gender.equalsIgnoreCase("1")) {
                            personalDetail.setGenderBen("1");
                        } else if (gender.equalsIgnoreCase("F") || gender.equalsIgnoreCase("2")) {
                            personalDetail.setGenderBen("2");
                        } else {
                            personalDetail.setGenderBen("3");
                        }

                    }
                    personalDetail.setPinCodeBen(personalDetailItem.getPinCode());
                    personalDetail.setStateNameBen(personalDetailItem.getState());
                    personalDetail.setPostOfficeBen(personalDetailItem.getPostOfficeBen());
                    if (beneficiaryListItem.getYob() != null && !beneficiaryListItem.getYob().equalsIgnoreCase("")) {
                        personalDetail.setYobSecc(beneficiaryListItem.getYob());
                    }

                    if (beneficiaryListItem.getDob() != null && beneficiaryListItem.getDob().length() > 4) {
                        personalDetail.setYobSecc(beneficiaryListItem.getDob().substring(0, 4));
                    }
                    personalDetail.setFatherNameSecc(beneficiaryListItem.getFathername());
                    personalDetail.setMotherNameSecc(beneficiaryListItem.getMothername());
                    personalDetail.setGenderIdSecc(beneficiaryListItem.getGenderid());

                    FamilyDetailResponse familyDetail = new FamilyDetailResponse();

                    familyDetail.setFamilyMemberModels(familyMemberModel.getFamilyMemberModels());
                    familyDetail.setFamilyMatchScore(familyMemberModel.getFamilyMatchScore());
                    familyDetail.setIdImage(familyMemberModel.getIdImage());
                    familyDetail.setIdNumber(familyMemberModel.getIdNumber());
                    familyDetail.setIdType(familyMemberModel.getIdName());

                    request.setPersonalDetail(personalDetail);
                    request.setFamilyDetailsItem(familyDetail);


                    String syncRequest = request.serialize();
                    HashMap<String, String> apiResponse = CustomHttp.httpPostWithTokken(AppConstant.SUBMIT_MEMBER_ADDITIONAL_DATA, syncRequest, AppConstant.AUTHORIZATION, verifierDetail.getAuthToken());
                    familyResponse = apiResponse.get("response");

                    if (familyResponse != null) {
                        genericResponse = new GenericResponse().create(familyResponse);
                        String ahltin = activity.benefItem.getAhl_tin();
                        GetSearchParaRequestModel getSearchParaRequestModel= GetSearchParaRequestModel.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, "SEARCH_DATA", context));
                        //getSearchParaRequestModel.setUser_id(loginResponse.getAadhaarNumber());
                       // getSearchParaRequestModel.setType_of_search(searchType);
                        getSearchParaRequestModel.setUid_search_type(personalDetailItem.getIsAadhar());
                        getSearchParaRequestModel.setState_code(activity.benefItem.getState_code());
                        getSearchParaRequestModel.setDistrict_code(activity.benefItem.getDistrict_code());
                        getSearchParaRequestModel.setAhl_tin(ahltin);
                        getSearchParaRequestModel.setType_of_doc(personalDetailItem.getIdName());
                        //getSearchParaRequestModel.setTid(responseModel.getTransactionId()+"");
                       // getSearchParaRequestModel.setStartTime(time+"");
                        long endTime= System.currentTimeMillis();

                        getSearchParaRequestModel.setEndTime(endTime);
                       // getSearchParaRequestModel.setSource(AppConstant.MOBILE_SOURCE);
                        String request1= getSearchParaRequestModel.serialize();
                        Log.d("Find by name",request1);
                        ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,"updated log",getSearchParaRequestModel.serialize(),context);
                        if (genericResponse!=null && genericResponse.isStatus()) {
                            //Hit log api to track the app
                            HashMap<String, String> searchResRsby = CustomHttp.httpPostWithTokken(AppConstant.GET_SEARCH_PARA, getSearchParaRequestModel.serialize(), AppConstant.AUTHORIZATION, verifierDetail.getAuthToken());
                            String searchResponse = searchResRsby.get("response");
                            GetSearchParaResponseModel getSearchParaResponseModel = GetSearchParaResponseModel.create(searchResponse);

                        }

                    }

                  /*  String ahltin = activity.benefItem.getAhl_tin();
                    GetSearchParaRequestModel getSearchParaRequestModel= GetSearchParaRequestModel.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, "SEARCH_DATA", context));
                    //getSearchParaRequestModel.setUser_id(loginResponse.getAadhaarNumber());
                    // getSearchParaRequestModel.setType_of_search(searchType);
                    getSearchParaRequestModel.setUid_search_type(personalDetailItem.getIsAadhar());
                    //getSearchParaRequestModel.setState_code(selectedStateItem1.getStateCode());
                    getSearchParaRequestModel.setDistrict_code(activity.benefItem.getDistrict_code());
                    getSearchParaRequestModel.setAhl_tin(ahltin);
                    getSearchParaRequestModel.setType_of_doc(personalDetailItem.getIdName());
                    //getSearchParaRequestModel.setTid(responseModel.getTransactionId()+"");
                    // getSearchParaRequestModel.setStartTime(time+"");
                    long endTime= System.currentTimeMillis();

                    getSearchParaRequestModel.setEndTime(endTime);
                    // getSearchParaRequestModel.setSource(AppConstant.MOBILE_SOURCE);
                    String request1= getSearchParaRequestModel.serialize();
                    Log.d("Find by name",request1);
                  //  if (genericResponse!=null && genericResponse.isStatus()) {
                        //Hit log api to track the app
                        HashMap<String, String> searchResRsby = CustomHttp.httpPostWithTokken(AppConstant.GET_SEARCH_PARA, getSearchParaRequestModel.serialize(), AppConstant.AUTHORIZATION, verifierDetail.getAuthToken());
                        String searchResponse = searchResRsby.get("response");
                        GetSearchParaResponseModel getSearchParaResponseModel = GetSearchParaResponseModel.create(searchResponse);
*/
                   // }
                } catch (Exception e) {
                    e.printStackTrace();

                }

            }

            @Override
            public void updateUI() {
                if (genericResponse != null) {
                    if (genericResponse.isStatus()) {
                        if (familyScore >= 80 && nameScore >= 80) {
                            printCard();
                        } else {
                            String msg = getResources().getString(R.string.submit_data_message);
                            final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle(context.getResources().getString(com.customComponent.R.string.Alert));
                            builder.setMessage(msg)
                                    .setCancelable(false)
                                    .setPositiveButton(context.getResources().getString(com.customComponent.R.string.OK), new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            activity.finish();
                                            alert.dismiss();
                                        }
                                    });
                            alert = builder.create();
                            alert.show();
                        }

                        // Intent intent = new Intent(context, FamilyMembersListActivity.class);
                        // CustomAlert.alertWithOk(context, getResources().getString(R.string.submit_data_message), intent);
                      /*  PrintCardItem printCard = new PrintCardItem();
                        printCard.setBenefPhoto(activity.benefItem.getPersonalDetail().getBenefPhoto());
                        printCard.setNameOnCard(activity.benefItem.getName());
                        printCard.setFatherNameOnCard(activity.benefItem.getFathername());
                        printCard.setGenderOnCard(activity.benefItem.getGenderid());
                        String ahltin = activity.benefItem.getAhl_tin();
                        if (ahltin != null && !ahltin.equalsIgnoreCase("")) {
                            Log.d("TAG", "AhlTine : " + ahltin);
                            String firstTwoChar = ahltin.substring(0, 2);
                            String lastThreeChar = ahltin.substring(ahltin.length() - 3);
                            String middleChar = ahltin.substring(3, ahltin.length() - 3);
                            Log.d("TAG", "AhlTine : " + ahltin);
                            Log.d("TAG", "First TTwo : " + firstTwoChar);
                            Log.d("TAG", "Last Three : " + lastThreeChar);
                            Log.d("TAG", "middle : " + middleChar);
                            ahltin = firstTwoChar + " " + middleChar + " " + lastThreeChar;
                            Log.d("TAG", "Ayushman
                             Id  : " + ahltin);
                            printCard.setCardNo(ahltin);


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
                        CallFragment(fragment);*/
                    } else if (genericResponse != null && genericResponse.getErrorCode() != null &&
                            genericResponse.getErrorCode().equalsIgnoreCase(AppConstant.SESSION_EXPIRED)|| genericResponse.getErrorCode().equalsIgnoreCase(AppConstant.INVALID_TOKEN)) {
                        Intent intent = new Intent(context, LoginActivity.class);
                        CustomAlert.alertWithOkLogout(context, genericResponse.getErrorMessage(), intent);

                    } else {
                        //server error
                        CustomAlert.alertWithOk(context, genericResponse.getErrorMessage());
                    }
                } else {
                    CustomAlert.alertWithOk(context, "Internal server error");

                }
            }
        };
        if (customAsyncTask != null) {
            customAsyncTask.cancel(true);
            customAsyncTask = null;
        }

        customAsyncTask = new CustomAsyncTask(taskListener, "Please wait", context);
        customAsyncTask.execute();
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
                // scoreLL.setVisibility(View.GONE);
                if (familyMembersList.size() > 0) {
                    // scoreLL.setVisibility(View.VISIBLE);
                }
                refreshList(familyMembersList);
            }
        }

        if (resultCode == 4) {
            if (requestCode == 3) {
                if (data != null) {
                    familyMatchScore = data.getStringExtra("matchScore");
                    familyMatchScoreStatus=data.getStringExtra(AppConstant.MATCH_SCORE_STATUS);
                    if (familyMatchScore != null) {
                        familyScoreLL.setVisibility(View.GONE);
                        if (!familyMatchScore.equalsIgnoreCase("")) {
                            familyScoreLL.setVisibility(View.VISIBLE);
                            familyMatchScoreTV.setText(familyMatchScore + "%");
                        }
                    }
                }
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

    private void refreshListWithExistingMember(ArrayList<FamilyMemberModel> familyMembersList) {
        if (familyMembersList != null) {
            existingFamilyAdapter = new ExistingFamilyAdapter(context, familyMembersList);
            memberRecycle.setAdapter(existingFamilyAdapter);
            existingFamilyAdapter.notifyDataSetChanged();
        }
    }

    private void oldMemberRefreshList(ArrayList<FamilyMemberModel> familyMembersList) {
        if (familyMembersList != null) {
            oldMemberAdapter = new OldFamilyAdapter(context, familyMembersList);
            oldMemberRecycle.setAdapter(oldMemberAdapter);
            oldMemberAdapter.notifyDataSetChanged();
        }
    }

    private class ExistingFamilyAdapter extends RecyclerView.Adapter<ExistingFamilyAdapter.MyViewHolder> {

        View view;
        private ArrayList<FamilyMemberModel> dataSet;


        public class MyViewHolder extends RecyclerView.ViewHolder {
            RelativeLayout menuLayout;
            ImageView settings;
            TextView nameTV;


            public MyViewHolder(final View itemView) {
                super(itemView);
                nameTV = (TextView) itemView.findViewById(R.id.nameTV);
                settings = (ImageView) itemView.findViewById(R.id.settingsIV);
                settings.setVisibility(View.GONE);
                menuLayout = (RelativeLayout) itemView.findViewById(R.id.menuLayoutRL);
                menuLayout.setVisibility(View.GONE);
            }
        }


        public ExistingFamilyAdapter(Context context, ArrayList<FamilyMemberModel> data) {
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
            holder.menuLayout.setVisibility(View.GONE);
            holder.settings.setVisibility(View.GONE);
            holder.nameTV.setText(item.getName());
            /*if (beneficiaryListItem.getPrintCardDetail() != null) {
                holder.menuLayout.setVisibility(View.GONE);
                holder.settings.setVisibility(View.GONE);
            }*/

        }

        @Override
        public int getItemCount() {
            return dataSet.size();
        }


    }


    private class FamilyAdapter extends RecyclerView.Adapter<FamilyAdapter.MyViewHolder> {

        View view;
        private ArrayList<FamilyMemberModel> dataSet;


        public class MyViewHolder extends RecyclerView.ViewHolder {
            RelativeLayout menuLayout;
            ImageView settings;
            TextView nameTV,genderTV,ageTV,pincodeTV;


            public MyViewHolder(final View itemView) {
                super(itemView);
                nameTV = (TextView) itemView.findViewById(R.id.nameTV);
                genderTV = (TextView) itemView.findViewById(R.id.genderTV);
                ageTV = (TextView) itemView.findViewById(R.id.ageTV);
                pincodeTV = (TextView) itemView.findViewById(R.id.pincodeTV);

                settings = (ImageView) itemView.findViewById(R.id.settingsIV);
                settings.setVisibility(View.GONE);
                menuLayout = (RelativeLayout) itemView.findViewById(R.id.menuLayoutRL);
                menuLayout.setVisibility(View.GONE);
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
            //  holder.menuLayout.setVisibility(View.GONE);
            //  holder.settings.setVisibility(View.GONE);

          /*  if (listPosition == 0) {
                holder.menuLayout.setVisibility(View.GONE);
                holder.settings.setVisibility(View.GONE);
            } else {
                editDelete(holder.menuLayout, holder.settings, item, listPosition);

            }*/
           /* String aadhaarNo = "";
            holder.houseHoldIdTV.setText(item.getHouseholdId());
            if(item.getAadhaarNo()!=null && !item.getAadhaarNo().equalsIgnoreCase("")) {
                aadhaarNo = "XXXXXXXX" + item.getAadhaarNo().substring(8);
            }
            holder.houseHoldAadhaarNoTV.setText(aadhaarNo);*/
            holder.nameTV.setText(item.getName());
            if(item.getGenderid()!=null){
                if(item.getGenderid().equalsIgnoreCase("1")){
                    holder.genderTV.setText("Male");
                }else if(item.getGenderid().equalsIgnoreCase("2")){
                    holder.genderTV.setText("Female");
                }else {
                    holder.genderTV.setText("Other");
                }
            }
            if(item.getDob()!=null){
                String currentDate = DateTimeUtil.currentDate("dd MM yyyy");
                Log.d("current date", currentDate);
                String currentYear = currentDate.substring(6, 10);
                Log.d("current year", currentYear);
                int age = Integer.parseInt(currentYear) - Integer.parseInt(item.getDob());
                holder.ageTV.setText(age+"");
            }
            if(item.getPincode()!=null){
                holder.pincodeTV.setText(item.getPincode());
            }
            if (beneficiaryListItem.getPrintCardDetail() != null) {
                holder.menuLayout.setVisibility(View.GONE);
                holder.settings.setVisibility(View.GONE);
            } else {
                //if (listPosition != 0) {
                editDelete(holder.menuLayout, holder.settings, item, listPosition);
                //  }

            }


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

    private class OldFamilyAdapter extends RecyclerView.Adapter<OldFamilyAdapter.MyViewHolder> {

        View view;
        private ArrayList<FamilyMemberModel> dataSet;


        public class MyViewHolder extends RecyclerView.ViewHolder {
            RelativeLayout menuLayout;
            ImageView settings;
            TextView nameTV;


            public MyViewHolder(final View itemView) {
                super(itemView);
                nameTV = (TextView) itemView.findViewById(R.id.nameTV);
                settings = (ImageView) itemView.findViewById(R.id.settingsIV);
                settings.setVisibility(View.GONE);
                menuLayout = (RelativeLayout) itemView.findViewById(R.id.menuLayoutRL);
                menuLayout.setVisibility(View.GONE);
            }
        }


        public OldFamilyAdapter(Context context, ArrayList<FamilyMemberModel> data) {
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
            holder.menuLayout.setVisibility(View.GONE);
            holder.settings.setVisibility(View.GONE);
          /*  if (listPosition == 0) {
                holder.menuLayout.setVisibility(View.GONE);
                holder.settings.setVisibility(View.GONE);
            } else {
                editDelete(holder.menuLayout, holder.settings, item, listPosition);

            }*/
           /* String aadhaarNo = "";
            holder.houseHoldIdTV.setText(item.getHouseholdId());
            if(item.getAadhaarNo()!=null && !item.getAadhaarNo().equalsIgnoreCase("")) {
                aadhaarNo = "XXXXXXXX" + item.getAadhaarNo().substring(8);
            }
            holder.houseHoldAadhaarNoTV.setText(aadhaarNo);*/
            holder.nameTV.setText(item.getName());
         /*   if (beneficiaryListItem.getPrintCardDetail() != null) {
                holder.menuLayout.setVisibility(View.GONE);
                holder.settings.setVisibility(View.GONE);
            } else {
                if (listPosition!=0) {
                    editDelete(holder.menuLayout, holder.settings, item, listPosition);
                }

            }*/


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
                                alertWithOk(context, "Do yow want to delete member", familyMembersList, index);
                              /*  familyMembersList.remove(index);
                                refreshList(familyMembersList);*/
                                break;

                        }
                        return true;
                    }
                });
                popup.show();

            }
        });
    }

    private void alertWithOk(Context mContext, String msg, final ArrayList<FamilyMemberModel> familyMembersList, final int index) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(mContext.getResources().getString(com.customComponent.R.string.Alert));
        builder.setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(mContext.getResources().getString(com.customComponent.R.string.OK), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                        familyMembersList.remove(index);
                        refreshList(familyMembersList);

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void getFamilyMatchScore() {
        final AlertDialog internetDiaolg = new AlertDialog.Builder(context).create();
        final LayoutInflater factory = LayoutInflater.from(context);
        View alertView = factory.inflate(R.layout.match_name_layout, null);
        internetDiaolg.setView(alertView);
        internetDiaolg.show();

        TextView nameAsInKycTV = (TextView) alertView.findViewById(R.id.nameAsInKycTV);
        LinearLayout nameAsSeccLayout = (LinearLayout) alertView.findViewById(R.id.nameAsSeccLayout);
        nameAsSeccLayout.setVisibility(View.GONE);
        LinearLayout nameAsIDLayout = (LinearLayout) alertView.findViewById(R.id.nameAsIDLayout);
        nameAsIDLayout.setVisibility(View.GONE);
        TextView msgTV = (TextView) alertView.findViewById(R.id.msgTV);
        msgTV.setText(getResources().getString(R.string.family_confirmation_msg));
        TextView nameAsInSeccTV = (TextView) alertView.findViewById(R.id.nameAsInSeccTV);


        Button confirmBTN = (Button) alertView.findViewById(R.id.tryAgainBT);
        final Button cancelBT = (Button) alertView.findViewById(R.id.cancelBT);
        confirmBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                familyMatchScore = "80";
                familyScoreLL.setVisibility(View.VISIBLE);
                familyMatchScoreTV.setText(familyMatchScore + "%");

                internetDiaolg.dismiss();

            }
        });

        final Button declineBT = (Button) alertView.findViewById(R.id.declineBT);
        declineBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                familyMatchScore = "0";
                familyScoreLL.setVisibility(View.VISIBLE);
                familyMatchScoreTV.setText(familyMatchScore + "%");

                internetDiaolg.dismiss();

            }
        });
        cancelBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                familyScoreLL.setVisibility(View.GONE);
                internetDiaolg.dismiss();
            }
        });
    }

    private void getFamilyDetailsByRationCard(final String rationCard) {
        TaskListener taskListener = new TaskListener() {
            @Override
            public void execute() {
                SearchByRationRequestModel requestModel = new SearchByRationRequestModel();
                requestModel.setDocumentType(item.statusCode + "");
                requestModel.setRationno(rationCard);
                requestModel.setStatecode(Integer.parseInt(verifierDetail.getStatecode()));
                String request = requestModel.serialize();
                HashMap<String, String> response = null;
                try {
                    response = CustomHttp.httpPost(AppConstant.SEARCH_BY_RATION, request);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                responseRation = response.get("response");
                if (responseRation != null) {
                    getMemberDetailResponse = new GetMemberDetail().create(responseRation);

                }
            }

            @Override
            public void updateUI() {
                if (getMemberDetailResponse != null) {
                    if (getMemberDetailResponse.isStatus()) {
                        if (getMemberDetailResponse.getFamilyDetailsItem()!=null&& getMemberDetailResponse.getFamilyDetailsItem().getIdNumber() != null &&
                                !getMemberDetailResponse.getFamilyDetailsItem().getIdNumber().equalsIgnoreCase("")) {

                            setExistingData(getMemberDetailResponse);
                        } else  {
                            CustomAlert.alertWithOk(context,getMemberDetailResponse.getErrorMessage());
                            setNewData();
                        }
                    } else if (getMemberDetailResponse != null &&
                            getMemberDetailResponse.getErrorcode().equalsIgnoreCase(AppConstant.SESSION_EXPIRED)
                            || getMemberDetailResponse.getErrorcode().equalsIgnoreCase(AppConstant.INVALID_TOKEN)) {
                        Intent intent = new Intent(context, LoginActivity.class);
                        CustomAlert.alertWithOkLogout(context, getMemberDetailResponse.getErrorMessage(), intent);
                    } else {
                        CustomAlert.alertWithOk(context, getMemberDetailResponse.getErrorMessage());
                    }
                } else {
                    CustomAlert.alertWithOk(context, "Server Error");
                }
            }
        };

        if (customAsyncTask != null) {
            customAsyncTask.cancel(true);
            customAsyncTask = null;
        }

        customAsyncTask = new CustomAsyncTask(taskListener, "Please wait", context);
        customAsyncTask.execute();
    }

    private void setExistingData(GetMemberDetail getMemberDetailResponse) {
        familyDetailsLL.setVisibility(View.VISIBLE);
        searchBT.setVisibility(View.GONE);
        editBT.setVisibility(View.VISIBLE);
        if (getMemberDetailResponse.getFamilyDetailsItem() != null && getMemberDetailResponse.getFamilyDetailsItem().getIdImage() != null
                && !getMemberDetailResponse.getFamilyDetailsItem().getIdImage().equalsIgnoreCase("")) {
            voterIdImg = getMemberDetailResponse.getFamilyDetailsItem().getIdImage();
            beneficiaryPhotoIV.setImageBitmap(AppUtility.convertStringToBitmap(voterIdImg));
        }
        //  familyScoreLL.setVisibility(View.VISIBLE);
        //  familyMatchScore = getMemberDetailResponse.getFamilyDetailsItem().getFamilyMatchScore() + "";
        // familyMatchScoreTV.setText(familyMatchScore + "%");
        if (getMemberDetailResponse.getFamilyDetailsItem() != null &&
                getMemberDetailResponse.getFamilyDetailsItem().getFamilyMemberModels() != null
                && getMemberDetailResponse.getFamilyDetailsItem().getFamilyMemberModels().size() > 0) {
            familyMembersList = getMemberDetailResponse.getFamilyDetailsItem().getFamilyMemberModels();
            refreshListWithExistingMember(familyMembersList);
        }
        govtIdET.setEnabled(false);
        addFamilyMemberLL.setEnabled(false);
        addFamilyMemberLL.setBackground(getResources().getDrawable(R.drawable.rounded_grey_button));

        captureImageBT.setEnabled(false);
        captureImageBT.setBackground(getResources().getDrawable(R.drawable.rounded_grey_button));
    }

    private void setNewData() {
        familyDetailsLL.setVisibility(View.VISIBLE);
        familyMembersList = new ArrayList<>();
        FamilyMemberModel item = new FamilyMemberModel();
        item.setName(beneficiaryListItem.getName());
        familyMembersList.add(item);
        refreshList(familyMembersList);
        voterIdImg = null;
        beneficiaryPhotoIV.setImageBitmap(null);
        addFamilyMemberLL.setEnabled(true);
        addFamilyMemberLL.setBackground(getResources().getDrawable(R.drawable.rounded_shape_yello_button));
        govtIdET.setEnabled(true);
        captureImageBT.setEnabled(true);
        captureImageBT.setBackground(getResources().getDrawable(R.drawable.rounded_shape_yello_button));
    }
    private void resetData() {
        familyDetailsLL.setVisibility(View.GONE);
        familyMembersList = new ArrayList<>();
        FamilyMemberModel item = new FamilyMemberModel();
        searchTag="";
        item.setName(beneficiaryListItem.getName());
        familyMembersList.add(item);
        refreshList(familyMembersList);
        voterIdImg = null;
        getMemberDetailResponse=null;
        beneficiaryPhotoIV.setImageBitmap(null);
        addFamilyMemberLL.setEnabled(true);
        addFamilyMemberLL.setBackground(getResources().getDrawable(R.drawable.rounded_shape_yello_button));
        govtIdET.setEnabled(true);
        captureImageBT.setEnabled(true);
        captureImageBT.setBackground(getResources().getDrawable(R.drawable.rounded_shape_yello_button));
    }

    @Override
    public void onResume() {
        super.onResume();
        verifierDetail = VerifierLoginResponse.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.VERIFIER_CONTENT, context));
    }
}
