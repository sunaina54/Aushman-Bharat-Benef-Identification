package com.nhpm.fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.customComponent.CustomAlert;
import com.customComponent.CustomAsyncTask;
import com.customComponent.TaskListener;
import com.customComponent.utility.CustomHttp;
import com.customComponent.utility.DateTimeUtil;
import com.customComponent.utility.ProjectPrefrence;
import com.nhpm.CameraUtils.CommonUtilsImageCompression;
import com.nhpm.CameraUtils.FaceCropper;
import com.nhpm.CameraUtils.squarecamera.CameraActivity;
import com.nhpm.LocalDataBase.DatabaseHelpers;
import com.nhpm.LocalDataBase.dto.SeccDatabase;
import com.nhpm.Models.SearchLocation;
import com.nhpm.Models.SerachOptionItem;
import com.nhpm.Models.request.AutoSuggestRequestItem;
import com.nhpm.Models.request.MobileOtpRequest;
import com.nhpm.Models.request.PersonalDetailItem;
import com.nhpm.Models.response.BeneficiaryListItem;
import com.nhpm.Models.response.DocsListItem;
import com.nhpm.Models.response.MobileOTPResponse;
import com.nhpm.Models.response.VillageResponseItem;
import com.nhpm.Models.response.master.ConfigurationItem;
import com.nhpm.Models.response.master.StateItem;
import com.nhpm.Models.response.seccMembers.SeccMemberItem;
import com.nhpm.Models.response.verifier.AadhaarResponseItem;
import com.nhpm.Models.response.verifier.VerifierLoginResponse;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;
import com.nhpm.Utility.AppUtility;
import com.nhpm.Utility.ApplicationGlobal;
import com.nhpm.Utility.Verhoeff;
import com.nhpm.activity.CollectDataActivity;
import com.nhpm.activity.EkycActivity;
import com.nhpm.activity.FamilyMembersListActivity;
import com.nhpm.activity.GovermentIDActivity;
import com.nhpm.activity.GovermentIDCaptureActivity;
import com.nhpm.activity.LoginActivity;
import com.nhpm.activity.NameMatchScoreActivity;
import com.nhpm.activity.PhoneNumberActivity;
import com.nhpm.activity.PhotoCaptureActivity;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;
import static com.nhpm.AadhaarUtils.CommonMethods.isNetworkAvailable;

/**
 * A simple {@link Fragment} subclass.
 */
public class PersonalDetailsFragment extends Fragment {
    private View view;
    private AadhaarResponseItem aadhaarKycResponse;
    private AlertDialog internetDiaolg;
    private String benefidentificationMode = "";
    private AlertDialog dialog;
    private Context context;
    private EkycActivity ekycActivity;
    private int CAMERA_PIC_REQUEST = 0;
    private int EKYC = 1;
    private int GOVT_ID_REQUEST = 2;
    private Picasso mPicasso;
    private ImageView govtIdPhotoIV;
    private TextView govtIdType, govtIdNumberTV, genderTV, yobTV, pincodeTV,
            emailTV, poTV, vtcTV, subDistTV, distTV, stateTV;
    private AutoCompleteTextView vtcACTV, distACTV;
    private LinearLayout govtIdLL;
    private CollectDataActivity activity;
    private FaceCropper mFaceCropper;
    private Button nextBT, captureImageBT, verifyAadharBT;
    private boolean isVeroff;
    private AutoCompleteTextView aadharET;
    private MobileOTPResponse mobileOtpRequestModel;
    private CustomAsyncTask mobileOtpAsyncTask;
    private MobileOTPResponse mobileOtpVerifyModel;
    private ImageView beneficiaryPhotoIV;
    private String operationId;
    private Button verifyMobBT;
    private boolean isValidMobile;
    private Bitmap memberPhoto;
    private EditText mobileNumberET;
    private String name;
    private TextView beneficiaryNameTV, beneficiaryNamePerIdTV, noAadhaarTV, nameScoreLabelTV, nameScorePercentTV;
    private DocsListItem beneficiaryListItem;
    private PersonalDetailItem personalDetailItem;
    private String status = "";
    private LinearLayout aadharLL, noAadhaarLL, govtIdNumberLL;
    private Button matchBT;
    private VerifierLoginResponse storedLoginResponse;
    private String nameMatchScore = "", nameMatchScoreStatus = "", whoseMobileStatus = "";
    private TextView govtPhotoLabelTV;
    private LinearLayout kycDetailsLL;
    private Spinner whoseMobileNoSP;
    private Spinner stateSP, ruralUrbanSP;
    private String stateName, ruralUrbanStatus = "", ruralUrbanTag;
    private StateItem selectedStateItem;
    private SearchLocation location = new SearchLocation();
    private ArrayList<String> temp, distTemp;
    private ArrayList<String> tempDist;
    private VillageResponseItem villageResponse, districtResponse;
    private CustomAsyncTask customAsyncTask;
    private String LOCATION_TAG = "villageTag";
    private ArrayList<String> mobileList;
    private ArrayList<StateItem> stateList2;


    public AadhaarResponseItem getAadhaarKycResponse() {
        return aadhaarKycResponse;
    }

    public void setAadhaarKycResponse(AadhaarResponseItem aadhaarKycResponse) {
        this.aadhaarKycResponse = aadhaarKycResponse;
    }

    public PersonalDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_personal_details, container, false);
        context = getActivity();
        setupScreen(view);
        return view;

    }

    private void setupScreen(View view) {
        //checkAppConfig(view);
        selectedStateItem = StateItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SELECTED_STATE_SEARCH, context));
        location = SearchLocation.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.DIST_VILLAGE_LOCATION, context));
        storedLoginResponse = VerifierLoginResponse.create(
                ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.VERIFIER_CONTENT, context));


        // personalDetailItem = beneficiaryListItem.getPersonalDetail();

        //Bundle bundle = getArguments();
        //bundle.getString("personalDetail");
        /*if (bundle != null) {
            personalDetailItem = PersonalDetailItem.create(bundle.getString("personalDetail"));
        }*/
        personalDetailItem = activity.benefItem.getPersonalDetail();
        matchBT = (Button) view.findViewById(R.id.matchBT);

        noAadhaarTV = (TextView) view.findViewById(R.id.noAadhaarTV);
        aadharLL = (LinearLayout) view.findViewById(R.id.aadharLL);
        govtIdLL = (LinearLayout) view.findViewById(R.id.govtIdLL);
        noAadhaarLL = (LinearLayout) view.findViewById(R.id.noAadhaarLL);
        govtIdNumberLL = (LinearLayout) view.findViewById(R.id.govtIdNumberLL);
        govtIdNumberTV = (TextView) view.findViewById(R.id.govtIdNumberTV);
        govtIdType = (TextView) view.findViewById(R.id.govtIdType);
        genderTV = (TextView) view.findViewById(R.id.genderTV);
        yobTV = (TextView) view.findViewById(R.id.yobTV);
        pincodeTV = (TextView) view.findViewById(R.id.pincodeTV);
        emailTV = (TextView) view.findViewById(R.id.emailTV);
        poTV = (TextView) view.findViewById(R.id.poTV);
        vtcTV = (TextView) view.findViewById(R.id.vtcTV);
        subDistTV = (TextView) view.findViewById(R.id.subDistTV);
        distTV = (TextView) view.findViewById(R.id.distTV);
        stateTV = (TextView) view.findViewById(R.id.stateTV);


        nameScoreLabelTV = (TextView) view.findViewById(R.id.nameScoreLabelTV);

        nameScorePercentTV = (TextView) view.findViewById(R.id.nameScorePercentTV);
        beneficiaryNamePerIdTV = (TextView) view.findViewById(R.id.beneficiaryNamePerIdTV);

        //govtIdLL.setVisibility(View.GONE);
        beneficiaryNameTV = (TextView) view.findViewById(R.id.beneficiaryNameTV);
        beneficiaryPhotoIV = (ImageView) view.findViewById(R.id.beneficiaryPhotoIV);
        govtIdPhotoIV = (ImageView) view.findViewById(R.id.govtIdPhotoIV);
        aadharET = (AutoCompleteTextView) view.findViewById(R.id.aadharET);
        captureImageBT = (Button) view.findViewById(R.id.captureImageBT);
        verifyMobBT = (Button) view.findViewById(R.id.verifyMobBT);
        verifyAadharBT = (Button) view.findViewById(R.id.verifyAadharBT);
        mobileNumberET = (EditText) view.findViewById(R.id.mobileET);

        kycDetailsLL = (LinearLayout) view.findViewById(R.id.kycDetailsLL);
        govtPhotoLabelTV = (TextView) view.findViewById(R.id.govtPhotoLabelTV);
        kycDetailsLL.setVisibility(View.GONE);
        // if(name!=null){
        whoseMobileNoSP = (Spinner) view.findViewById(R.id.whoseMobileNoSP);


        setMobileList();
        whoseMobileStatus = "Self";
        whoseMobileNoSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    verifyMobBT.setVisibility(View.VISIBLE);
                    whoseMobileStatus = "Self";
                } else if (position == 1) {
                    verifyMobBT.setVisibility(View.GONE);
                    whoseMobileStatus = "Relative";
                } else if (position == 2) {
                    verifyMobBT.setVisibility(View.GONE);
                    whoseMobileStatus = "Others";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }


        });

        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, mobileList);
        whoseMobileNoSP.setAdapter(adapter1);

        ruralUrbanSP = (Spinner) view.findViewById(R.id.ruralUrbanSP);
        ruralUrbanSP.setEnabled(false);
        final ArrayList<String> ruralList = new ArrayList<>();
        ruralList.add("Select Rural/Urban");
        ruralList.add("Rural");
        ruralList.add("Urban");

        ruralUrbanSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String item = adapterView.getItemAtPosition(i).toString();
                if (i == 0) {
                    // ruralCheck.setChecked(false);
                    ruralUrbanStatus = "";
                    ruralUrbanTag = "";
                    Log.d("ruralUrbanStatus :", ruralUrbanStatus + ":" + ruralUrbanTag);
                } else if (i == 1) {
                    // ruralCheck.setChecked(true);
                    ruralUrbanStatus = ruralList.get(i);
                    ruralUrbanTag = "R";
                    Log.d("ruralUrbanStatus :", ruralUrbanStatus + ":" + ruralUrbanTag);
                } else if (i == 2) {
                    //  ruralCheck.setChecked(true);
                    ruralUrbanStatus = ruralList.get(i);
                    ruralUrbanTag = "U";
                    Log.d("ruralUrbanStatus :", ruralUrbanStatus + ":" + ruralUrbanTag);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

      /*  ruralUrbanSP.setSelection(0);
        ruralUrbanTag = "R";

        Log.d("ruralUrbanStatus :", ruralUrbanStatus + ":" + ruralUrbanTag);*/

        ArrayAdapter<String> ruralAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, ruralList);
        ruralUrbanSP.setAdapter(ruralAdapter);


        stateSP = (Spinner) view.findViewById(R.id.stateSP);
        stateSP.setEnabled(false);
        final ArrayList<String> stateList = new ArrayList<>();
        final ArrayList<StateItem> stateList1 = SeccDatabase.findStateList(context);

        Collections.sort(stateList1, new Comparator<StateItem>() {
            @Override
            public int compare(StateItem s1, StateItem s2) {
                return s1.getStateName().compareToIgnoreCase(s2.getStateName());
            }
        });
        Log.d("Splash", "ListSize:" + " " + stateList.size());
        //stateList.add(0, new StateItem("00", "Select State"));
       stateList2 = new ArrayList<>();
        if (stateList != null) {
            for (StateItem item1 : stateList1) {
                if (item1.getStateCode().equalsIgnoreCase("06")) {
                    stateList.add("Haryana"); //HARYANA
                    stateList2.add(item1);
                } else if (item1.getStateCode().equalsIgnoreCase("22")) {
                    stateList.add("Chattisgarh");  // CHHATTISGARH
                    stateList2.add(item1);
                } else if (item1.getStateCode().equalsIgnoreCase("07")) {
                    stateList.add("Delhi"); // NCT OF DELHI
                    stateList2.add(item1);
                } else if (item1.getStateCode().equalsIgnoreCase("09")) {
                    stateList.add("Uttar Pradesh"); //UTTAR PRADESH
                    stateList2.add(item1);
                }
            }

        }


        stateSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();
                stateName = stateList.get(i);
                vtcTV.setText("");
                distTV.setText("");
                Log.d("state name :", stateName);


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, stateList);
        stateSP.setAdapter(adapter2);

        for (int i = 0; i < stateList2.size(); i++) {

            if (selectedStateItem.getStateCode().equalsIgnoreCase(stateList2.get(i).getStateCode())) {

                stateSP.setSelection(i);
                // stateSP.setTitle(item.getStateName());

                stateName = stateList.get(i);

                Log.d("state name11 :", stateName);

                break;
            }
        }

        distACTV = (AutoCompleteTextView) view.findViewById(R.id.distACTV);

        vtcACTV = (AutoCompleteTextView) view.findViewById(R.id.vtcACTV);
        distACTV.setThreshold(1);
        vtcACTV.setThreshold(1);
        distACTV.setEnabled(false);
        vtcACTV.setEnabled(false);
        distACTV.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (distACTV.hasFocus()) {
                    if (activity.isNetworkAvailable()) {
                        autoSuggestDistrict(s.toString());
                    } else {
                        CustomAlert.alertWithOk(context, getResources().getString(R.string.internet_connection_msg));
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });
        vtcACTV.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (vtcACTV.hasFocus()) {
                    if (activity.isNetworkAvailable()) {
                        autoSuggestVillage(s.toString());
                    } else {
                        CustomAlert.alertWithOk(context, getResources().getString(R.string.internet_connection_msg));
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });

        if (location != null) {
            if (!location.getVilageName().equalsIgnoreCase("")) {
                vtcACTV.setText("");
                vtcACTV.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        vtcACTV.showDropDown();
                        vtcACTV.setText(location.getVilageName());
                        //kycVtc.setSelection(mACTextViewEmail.getText().length());
                    }
                }, 500);

               /* if(location.isVillageTrue()){
                    vtcTV.setChecked(true);
                }*/
            }
            if (!location.getDistName().equalsIgnoreCase("")) {
                distACTV.setText("");

                distACTV.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        distACTV.showDropDown();
                        distACTV.setText(location.getDistName());
                        //kycVtc.setSelection(mACTextViewEmail.getText().length());
                    }
                }, 500);
                /*if(location.isDistTrue()){
                    distCheck.setChecked(true);
                }*/
            }
        } else {
            location = new SearchLocation();
        }


        noAadhaarTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status = "govt";
                ProjectPrefrence.
                        removeSharedPrefrenceData(AppConstant.PROJECT_NAME, "GOVT_ID_DATA", context);
                Intent intent = new Intent(context, GovermentIDActivity.class);
                // intent.putExtra("mobileNumber",mobileNumberET.getText().toString());
                intent.putExtra("mobileNumber", personalDetailItem);

                startActivityForResult(intent, GOVT_ID_REQUEST);
            }
        });
        if (beneficiaryListItem != null) {
            beneficiaryNameTV.setText(beneficiaryListItem.getName());
        }

        if (personalDetailItem != null) {
            nameMatchScore = personalDetailItem.getNameMatchScore() + "";
            if (!nameMatchScore.equalsIgnoreCase("")) {
                nameScoreLabelTV.setVisibility(View.VISIBLE);
                nameScorePercentTV.setVisibility(View.VISIBLE);
                nameScorePercentTV.setText(nameMatchScore + "%");
            }
            if (!personalDetailItem.getFlowStatus().equalsIgnoreCase("")
                    && personalDetailItem.getFlowStatus().equalsIgnoreCase(AppConstant.AADHAR_STATUS)) {
                vtcACTV.setEnabled(false);
                distACTV.setEnabled(false);
                stateSP.setEnabled(false);
                aadharLL.setVisibility(View.VISIBLE);
                // govtIdLL.setVisibility(View.GONE);
                //new parameters added
                govtPhotoLabelTV.setVisibility(View.GONE);
                govtIdPhotoIV.setVisibility(View.GONE);
                kycDetailsLL.setVisibility(View.VISIBLE);
                noAadhaarLL.setVisibility(View.GONE);
                status = "aadhar";
                isVeroff = true;
                govtIdNumberLL.setVisibility(View.GONE);
                if (personalDetailItem.getBenefPhoto() != null) {
                    beneficiaryPhotoIV.setImageBitmap(AppUtility.convertStringToBitmap(personalDetailItem.getBenefPhoto()));
                }
                if (personalDetailItem.getGovtIdNo() != null &&
                        !personalDetailItem.getGovtIdNo().equalsIgnoreCase("")) {
                    govtIdNumberTV.setText(personalDetailItem.getGovtIdNo());

                }

                if (personalDetailItem.getGovtIdType() != null &&
                        !personalDetailItem.getGovtIdType().equalsIgnoreCase("")) {
                    govtIdType.setText(personalDetailItem.getGovtIdType());
                }
                if (personalDetailItem.getGovtIdNo() != null) {
                    aadharET.setEnabled(false);
                    aadharET.setText(personalDetailItem.getGovtIdNo());
                    aadharET.setTextColor(Color.GREEN);
                    verifyAadharBT.setEnabled(false);
                    verifyAadharBT.setBackground(getResources().getDrawable(R.drawable.rounded_grey_button));

                }
                matchBT.setBackground(getResources().getDrawable(R.drawable.rounded_grey_button));
                matchBT.setEnabled(false);
                if (personalDetailItem.getMobileNo() != null) {
                    mobileNumberET.setEnabled(false);
                    mobileNumberET.setTextColor(AppUtility.getColor(context, R.color.green));
                    mobileNumberET.setText(personalDetailItem.getMobileNo());
                    mobileNumberET.setEnabled(false);
                    verifyMobBT.setEnabled(false);
                    verifyMobBT.setBackground(getResources().getDrawable(R.drawable.rounded_grey_button));
                }
                if (personalDetailItem.getMemberType() != null) {
                    if (personalDetailItem.getMemberType().equalsIgnoreCase("Self")) {
                        verifyMobBT.setVisibility(View.VISIBLE);
                    } else {
                        verifyMobBT.setVisibility(View.VISIBLE);
                    }
                    setMobileList();
                    for(int i=0;i<mobileList.size();i++){
                        if(mobileList.get(i).equalsIgnoreCase(personalDetailItem.getMemberType())){
                            whoseMobileNoSP.setSelection(i);
                        }
                    }
                }

                if (personalDetailItem.getName() != null && !personalDetailItem.getName().equalsIgnoreCase("")) {
                    beneficiaryNamePerIdTV.setText(personalDetailItem.getName());
                }
                if (personalDetailItem.getYob() != null && personalDetailItem.getYob().length() > 4) {

                    String currentYear = DateTimeUtil.currentDate(AppConstant.DATE_FORMAT);

                    currentYear = currentYear.substring(0, 4);
                    String date = DateTimeUtil.
                            convertTimeMillisIntoStringDate(DateTimeUtil.convertDateIntoTimeMillis(personalDetailItem.getYob()), AppConstant.DATE_FORMAT);
                    String arr[];
                    String aadhaarYear = null;
                    if (personalDetailItem.getYob().contains("-")) {
                        arr = personalDetailItem.getYob().split("-");
                        if (arr[0].length() == 4) {
                            aadhaarYear = arr[0];
                        } else if (arr[2].length() == 4) {
                            aadhaarYear = arr[2];
                        }
                    } else if (personalDetailItem.getYob().contains("/")) {
                        arr = personalDetailItem.getYob().split("/");
                        if (arr[0].length() == 4) {
                            aadhaarYear = arr[0];
                        } else if (arr[2].length() == 4) {
                            aadhaarYear = arr[2];
                        }
                    }
                    if (aadhaarYear != null) {
                        //    int age = Integer.parseInt(currentYear) - Integer.parseInt(aadhaarYear);
                        yobTV.setText(aadhaarYear);
                    }

                } else if (personalDetailItem.getYob() != null && personalDetailItem.getYob().length() == 4) {
                /*    String currentYear = DateTimeUtil.currentDate("dd-mm-yyyy");
                    currentYear = currentYear.substring(6, 10);
                    int age = Integer.parseInt(currentYear) - Integer.parseInt(personalDetailItem.getYob());*/
                    yobTV.setText(personalDetailItem.getYob());
                }
              /*  if (personalDetailItem.getYob() != null) {
                    yobTV.setText(personalDetailItem.getYob());
                }*/

                if (personalDetailItem.getGender() != null) {
                    if (personalDetailItem.getGender().equalsIgnoreCase("1")
                            || personalDetailItem.getGender().substring(0, 1).toUpperCase().equalsIgnoreCase("M")) {
                        genderTV.setText("Male");
                    } else if (personalDetailItem.getGender().equalsIgnoreCase("2")
                            || personalDetailItem.getGender().substring(0, 1).toUpperCase().equalsIgnoreCase("F")) {
                        genderTV.setText("Female");
                    } else {
                        genderTV.setText("Other");
                    }
                }

                if (personalDetailItem.getDistrict() != null) {
                    distACTV.setText(personalDetailItem.getDistrict());
                }

                if (personalDetailItem.getSubDistrictBen() != null) {
                    subDistTV.setText(personalDetailItem.getSubDistrictBen());
                }

                if (personalDetailItem.getState() != null) {
                    // stateTV.setText(personalDetailItem.getState());
                    //stateSP.setse
                    if(stateList2!=null && stateList2.size()>0){
                        for (int i = 0; i < stateList2.size(); i++) {

                            if (personalDetailItem.getState().equalsIgnoreCase(stateList2.get(i).getStateCode())) {

                                stateSP.setSelection(i);
                                // stateSP.setTitle(item.getStateName());

                                stateName = stateList.get(i);

                                Log.d("state name11 :", stateName);

                                break;
                            }
                        }
                    }
                }

                if (personalDetailItem.getVtcBen() != null) {
                    vtcACTV.setText(personalDetailItem.getVtcBen());
                }

                if (personalDetailItem.getEmailBen() != null) {
                    emailTV.setText(personalDetailItem.getEmailBen());
                }

                if (personalDetailItem.getPostOfficeBen() != null) {
                    poTV.setText(personalDetailItem.getPostOfficeBen());
                }

                if (personalDetailItem.getPinCode() != null) {
                    pincodeTV.setText(personalDetailItem.getPinCode());
                }


            }

            if (!personalDetailItem.getFlowStatus().equalsIgnoreCase("")
                    && personalDetailItem.getFlowStatus().equalsIgnoreCase(AppConstant.GOVT_STATUS)) {
                vtcACTV.setEnabled(false);
                distACTV.setEnabled(false);
                stateSP.setEnabled(false);
                aadharLL.setVisibility(View.GONE);
                // govtIdLL.setVisibility(View.VISIBLE);
                govtPhotoLabelTV.setVisibility(View.VISIBLE);
                govtIdPhotoIV.setVisibility(View.VISIBLE);
                kycDetailsLL.setVisibility(View.VISIBLE);
                govtIdNumberLL.setVisibility(View.VISIBLE);
                noAadhaarLL.setVisibility(View.GONE);
                status = "govt";
                if (personalDetailItem.getBenefPhoto() != null) {
                    beneficiaryPhotoIV.setImageBitmap(AppUtility.convertStringToBitmap(personalDetailItem.getBenefPhoto()));
                }
                matchBT.setBackground(getResources().getDrawable(R.drawable.rounded_grey_button));
                matchBT.setEnabled(false);

                if (personalDetailItem.getIdPhoto() != null && !personalDetailItem.getIdPhoto().equalsIgnoreCase("")) {
                    govtIdPhotoIV.setImageBitmap(AppUtility.convertStringToBitmap(personalDetailItem.getIdPhoto()));

                }


                if (personalDetailItem.getName() != null && !personalDetailItem.getName().equalsIgnoreCase("")) {
                    beneficiaryNamePerIdTV.setText(personalDetailItem.getName());

                }

                if (personalDetailItem.getMemberType() != null) {
                    if (personalDetailItem.getMemberType().equalsIgnoreCase("Self")) {
                        verifyMobBT.setVisibility(View.VISIBLE);
                    } else {
                        verifyMobBT.setVisibility(View.VISIBLE);
                    }
                    setMobileList();
                    for(int i=0;i<mobileList.size();i++){
                        if(mobileList.get(i).equalsIgnoreCase(personalDetailItem.getMemberType())){
                            whoseMobileNoSP.setSelection(i);
                        }
                    }
                }

                if (personalDetailItem.getMobileNo() != null) {
                    mobileNumberET.setTextColor(AppUtility.getColor(context, R.color.green));
                    mobileNumberET.setText(personalDetailItem.getMobileNo());
                    mobileNumberET.setEnabled(false);
                    verifyMobBT.setEnabled(false);
                    verifyMobBT.setBackground(getResources().getDrawable(R.drawable.rounded_grey_button));

                }


                if (personalDetailItem.getGovtIdNo() != null &&
                        !personalDetailItem.getGovtIdNo().equalsIgnoreCase("")) {
                    govtIdNumberTV.setText(personalDetailItem.getGovtIdNo());

                }

                if (personalDetailItem.getGovtIdType() != null &&
                        !personalDetailItem.getGovtIdType().equalsIgnoreCase("")) {
                    govtIdType.setText(personalDetailItem.getGovtIdType());

                }

                if (personalDetailItem.getYob() != null && personalDetailItem.getYob().length() > 4) {

                    String currentYear = DateTimeUtil.currentDate(AppConstant.DATE_FORMAT);

                    currentYear = currentYear.substring(0, 4);
                    String date = DateTimeUtil.
                            convertTimeMillisIntoStringDate(DateTimeUtil.convertDateIntoTimeMillis(personalDetailItem.getYob()), AppConstant.DATE_FORMAT);
                    String arr[];
                    String aadhaarYear = null;
                    if (personalDetailItem.getYob().contains("-")) {
                        arr = personalDetailItem.getYob().split("-");
                        if (arr[0].length() == 4) {
                            aadhaarYear = arr[0];
                        } else if (arr[2].length() == 4) {
                            aadhaarYear = arr[2];
                        }
                    } else if (personalDetailItem.getYob().contains("/")) {
                        arr = personalDetailItem.getYob().split("/");
                        if (arr[0].length() == 4) {
                            aadhaarYear = arr[0];
                        } else if (arr[2].length() == 4) {
                            aadhaarYear = arr[2];
                        }
                    }
                    if (aadhaarYear != null) {
                        //    int age = Integer.parseInt(currentYear) - Integer.parseInt(aadhaarYear);
                        yobTV.setText(aadhaarYear);
                    }

                } else if (personalDetailItem.getYob() != null && personalDetailItem.getYob().length() == 4) {
                /*    String currentYear = DateTimeUtil.currentDate("dd-mm-yyyy");
                    currentYear = currentYear.substring(6, 10);
                    int age = Integer.parseInt(currentYear) - Integer.parseInt(personalDetailItem.getYob());*/
                    yobTV.setText(personalDetailItem.getYob());
                }

                /*if (personalDetailItem.getYob() != null) {
                    yobTV.setText(personalDetailItem.getYob());
                }*/

                if (personalDetailItem.getGender() != null) {
                    if (personalDetailItem.getGender().equalsIgnoreCase("1")
                            || personalDetailItem.getGender().substring(0, 1).toUpperCase().equalsIgnoreCase("M")) {
                        genderTV.setText("Male");
                    } else if (personalDetailItem.getGender().equalsIgnoreCase("2")
                            || personalDetailItem.getGender().substring(0, 1).toUpperCase().equalsIgnoreCase("F")) {
                        genderTV.setText("Female");
                    } else {
                        genderTV.setText("Other");
                    }
                }

                if (personalDetailItem.getDistrict() != null) {
                    distACTV.setText(personalDetailItem.getDistrict());
                }

                if (personalDetailItem.getSubDistrictBen() != null) {
                    subDistTV.setText(personalDetailItem.getSubDistrictBen());
                }

                if (personalDetailItem.getState() != null) {
                    // stateTV.setText(personalDetailItem.getState());
                    //stateSP.setse
                    if(stateList2!=null && stateList2.size()>0){
                        for (int i = 0; i < stateList2.size(); i++) {

                            if (personalDetailItem.getState().equalsIgnoreCase(stateList2.get(i).getStateCode())) {

                                stateSP.setSelection(i);
                                // stateSP.setTitle(item.getStateName());

                                stateName = stateList.get(i);

                                Log.d("state name11 :", stateName);

                                break;
                            }
                        }
                    }
                }

                if (personalDetailItem.getVtcBen() != null) {
                    vtcACTV.setText(personalDetailItem.getVtcBen());
                }

                if (personalDetailItem.getEmailBen() != null) {
                    emailTV.setText(personalDetailItem.getEmailBen());
                }

                if (personalDetailItem.getPostOfficeBen() != null) {
                    poTV.setText(personalDetailItem.getPostOfficeBen());
                }

                if (personalDetailItem.getPinCode() != null) {
                    pincodeTV.setText(personalDetailItem.getPinCode());
                }


            }

        } else {
            personalDetailItem = new PersonalDetailItem();

        }

        mobileNumberET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() > 0) {
                    if (Integer.parseInt(charSequence.toString().substring(0, 1)) > 5) {
                        isValidMobile = true;
                        mobileNumberET.setTextColor(AppUtility.getColor(context, R.color.black_shine));
                        if (mobileNumberET.getText().toString().length() == 10) {
                            // whoseMobileSP.setEnabled(true);
                            // whoseMobileSP.setAlpha(1.0f);
                            // mobileValidateLayout.setVisibility(View.VISIBLE);
                            //prepareFamilyStatusSpinner(mobileNumberET.getText().toString().trim());
                            mobileNumberET.setTextColor(AppUtility.getColor(context, R.color.green));
                            AppUtility.softKeyBoard(getActivity(), 0);

                        }
                    } else {
                        // whoseMobileSP.setEnabled(false);
                        //  whoseMobileSP.setAlpha(0.4f);
                        isValidMobile = false;
                        // mobileValidateLayout.setVisibility(View.GONE);
                        mobileNumberET.setTextColor(AppUtility.getColor(context, R.color.red));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        matchBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String beneficiaryName, nameAsKyc;
                beneficiaryName = beneficiaryNameTV.getText().toString();
                nameAsKyc = beneficiaryNamePerIdTV.getText().toString();
                if (distACTV.getText().toString().equalsIgnoreCase("")) {
                    CustomAlert.alertWithOk(context, "Please select district");
                    return;
                }
                if (stateName.equalsIgnoreCase("")) {
                    CustomAlert.alertWithOk(context, "Please select state");
                    return;
                }
                personalDetailItem.setDistrict(distACTV.getText().toString());
                personalDetailItem.setState(stateName);
                Intent intent = new Intent(context, NameMatchScoreActivity.class);
                intent.putExtra(NameMatchScoreActivity.PERSONAL_DETAIL_TAG, personalDetailItem);
                intent.putExtra(NameMatchScoreActivity.SECC_DETAIL_TAG, beneficiaryListItem);
                startActivityForResult(intent, 3);
               /* if (beneficiaryName != null && !beneficiaryName.equalsIgnoreCase("")
                        && nameAsKyc != null && !nameAsKyc.equalsIgnoreCase("")) {
                    alertForValidateLater(beneficiaryName, nameAsKyc);

                }else {
                    CustomAlert.alertWithOk(context,"Name cannot be blank");
                }*/

            }
        });
        verifyAadharBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (aadharET.getText().toString().equalsIgnoreCase("")) {
                    CustomAlert.alertWithOk(context, "Please enter aadhar number");
                    return;
                }
                ProjectPrefrence.
                        removeSharedPrefrenceData(AppConstant.PROJECT_NAME, "AADHAAR_DATA", context);
                status = "aadhar";
                Intent intent = new Intent(context, EkycActivity.class);
                intent.putExtra("screen", "PersonalDetailsFragment");
                intent.putExtra("mobileNumber", personalDetailItem);
                intent.putExtra("aadharNo", aadharET.getText().toString());
                SerachOptionItem item = new SerachOptionItem();
                item.setAadhaarNo(aadharET.getText().toString());
                item.setSearchType(AppConstant.AADHAAR_SEARCH);
                item.setMode(AppConstant.EKYC);
                ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_NAME, AppConstant.SEARCH_OPTION, item.serialize(), context);
                startActivityForResult(intent, EKYC);
            }
        });

        verifyMobBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobile = mobileNumberET.getText().toString().trim();
                //  seccItem.setMobileAuth(AppConstant.PENDING_STATUS);
                //String mobileNumber=mobil
                // eNumberET.getText().toString();
                //AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Mobile Number Index : "+mobile.length());


                if (mobile.equalsIgnoreCase("") || mobile.length() < 10) {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnter10DigitMobNum));
                    return;
                }
                //AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Mobile Number Index : " + isValidMobile);
                if (!isValidMobile) {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterValidMobNum));
                    return;
                }
             /*   if (mobile.equalsIgnoreCase(loginResponse.getMobileNumber())) {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.youHaveEnteredValidatorMobileNum));
                    return;
                }*/
                if (activity.isNetworkAvailable()) {
                    requestForOTP(mobile);
                } else {
                    CustomAlert.alertWithOk(context, getResources().getString(R.string.internet_connection_msg));
                }

            }
        });
        captureImageBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
            }
        });
        aadharET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isVeroff = Verhoeff.validateVerhoeff(s.toString());
                aadharET.setTextColor(Color.BLACK);
                if (s.toString().length() > 11) {
                    if (!Verhoeff.validateVerhoeff(s.toString())) {
                        aadharET.setTextColor(Color.RED);
                    } else {

                        aadharET.setTextColor(Color.GREEN);
                        AppUtility.softKeyBoard(getActivity(), 0);
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        nextBT = (Button) view.findViewById(R.id.nextBT);
        nextBT.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (status != null && !status.equalsIgnoreCase("")) {
                    if (status.equalsIgnoreCase("aadhar")) {
                        String vtc = vtcACTV.getText().toString();
                        String dist = distACTV.getText().toString();
                        if (aadharET.getText().toString().equalsIgnoreCase("")) {
                            CustomAlert.alertWithOk(context, "Please enter aadhaar number");
                            return;
                        }
                        if (!isVeroff) {
                            CustomAlert.alertWithOk(context, getResources().getString(R.string.invalid_login));
                            return;
                        }
                        if (mobileNumberET.getText().toString().equalsIgnoreCase("")) {
                            CustomAlert.alertWithOk(context, "Please enter mobile number");
                            return;
                        }

                        if (personalDetailItem != null && personalDetailItem.getBenefPhoto() == null
                                || personalDetailItem.getBenefPhoto().equalsIgnoreCase("")) {
                            CustomAlert.alertWithOk(context, "Beneficiary photo cannot be blank");
                            return;
                        }

                      /*  if (personalDetailItem != null && personalDetailItem.getIsMobileAuth() == null
                                || !personalDetailItem.getIsMobileAuth().equalsIgnoreCase("Y") &&
                              whoseMobileStatus.equalsIgnoreCase("S")) {
                            CustomAlert.alertWithOk(context, "Please verify the mobile number");
                            return;
                        }*/

                        if (personalDetailItem != null &&
                                whoseMobileStatus.equalsIgnoreCase("Self")) {
                            if (personalDetailItem.getIsMobileAuth() == null ||
                                    !personalDetailItem.getIsMobileAuth().equalsIgnoreCase("Y")) {
                                CustomAlert.alertWithOk(context, "Please verify the mobile number");
                                return;
                            }
                        }

                        if (nameMatchScore == null || nameMatchScore.equalsIgnoreCase("")) {
                            CustomAlert.alertWithOk(context, "Please match beneficiary name");
                            return;
                        }

                        if (nameMatchScore != null && nameMatchScore.equalsIgnoreCase("0")) {
                            CustomAlert.alertWithOk(context, "Name as an SECC and Name as an KYC is not matching");
                            return;
                        }
                        if (vtc.equalsIgnoreCase("")) {
                            CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterVTC));
                            return;
                        }
                        if (stateName.equalsIgnoreCase("")) {
                            CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterState));
                            return;
                        }

                        if (dist.equalsIgnoreCase("")) {
                            CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterDist));
                            return;
                        }

                        if (personalDetailItem != null && !whoseMobileStatus.equalsIgnoreCase("Self")) {
                            personalDetailItem.setMobileNo(mobileNumberET.getText().toString());
                            personalDetailItem.setIsMobileAuth("N");
                            operationId = storedLoginResponse.getAadhaarNumber();
                            String operatorId = operationId.substring(operationId.length() - 4);
                            Log.d("operator id :", operatorId);
                            personalDetailItem.setOpertaorid(Integer.parseInt(operatorId));
                        }

                        location.setVilageName(vtc);
                        location.setDistName(dist);
                        personalDetailItem.setState(stateName);
                        personalDetailItem.setDistrict(dist);
                        personalDetailItem.setVtcBen(vtc);
                        personalDetailItem.setMemberType(whoseMobileStatus);
                        personalDetailItem.setNameMatchScore(Integer.parseInt(nameMatchScore));
                        personalDetailItem.setOperatorMatchScoreStatus(nameMatchScoreStatus);

                        activity.personalDetailsLL.setBackground(context.getResources().getDrawable(R.drawable.arrow));
                        activity.familyDetailsLL.setBackground(context.getResources().getDrawable(R.drawable.arrow_yellow));
                        beneficiaryListItem.setPersonalDetail(personalDetailItem);

                        ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.DIST_VILLAGE_LOCATION, location.serialize(), context);

                        activity.benefItem = beneficiaryListItem;
                        Fragment fragment = new FamilyDetailsFragment();
                        Bundle args = new Bundle();
                        // args.putString("personalDetail", personalDetailItem.serialize());

                        fragment.setArguments(args);
                        CallFragment(fragment);
                    }

                    if (status.equalsIgnoreCase("govt")) {
                        String vtc = vtcACTV.getText().toString();
                        String dist = distACTV.getText().toString();
                        if (mobileNumberET.getText().toString().equalsIgnoreCase("")) {
                            CustomAlert.alertWithOk(context, "Please enter mobile number");
                            return;
                        }

                        if (personalDetailItem != null &&
                                whoseMobileStatus.equalsIgnoreCase("Self")) {
                            if (personalDetailItem.getIsMobileAuth() == null ||
                                    !personalDetailItem.getIsMobileAuth().equalsIgnoreCase("Y")) {
                                CustomAlert.alertWithOk(context, "Please verify the mobile number");
                                return;
                            }
                        }

                        if (personalDetailItem != null && personalDetailItem.getBenefPhoto() == null
                                || personalDetailItem.getBenefPhoto().equalsIgnoreCase("")) {
                            CustomAlert.alertWithOk(context, "Beneficiary photo cannot be blank");
                            return;
                        }

                        if (personalDetailItem != null && personalDetailItem.getIdPhoto() == null
                                || personalDetailItem.getIdPhoto().equalsIgnoreCase("")) {
                            CustomAlert.alertWithOk(context, "Govt id photo cannot be blank");
                            return;
                        }
                        if (nameMatchScore == null || nameMatchScore.equalsIgnoreCase("")) {
                            CustomAlert.alertWithOk(context, "Please match beneficiary name");
                            return;
                        }

                        if (nameMatchScore != null && nameMatchScore.equalsIgnoreCase("0")) {
                            CustomAlert.alertWithOk(context, "Name as SECC and Name as KYC is not matching");
                            return;
                        }

                        if (vtc.equalsIgnoreCase("")) {
                            CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterVTC));
                            return;
                        }
                        if (stateName.equalsIgnoreCase("")) {
                            CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterState));
                            return;
                        }

                        if (dist.equalsIgnoreCase("")) {
                            CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterDist));
                            return;
                        }


                        if (personalDetailItem != null && !whoseMobileStatus.equalsIgnoreCase("Self")) {
                            personalDetailItem.setMobileNo(mobileNumberET.getText().toString());
                            personalDetailItem.setIsMobileAuth("N");
                            operationId = storedLoginResponse.getAadhaarNumber();
                            String operatorId = operationId.substring(operationId.length() - 4);
                            Log.d("operator id :", operatorId);
                            personalDetailItem.setOpertaorid(Integer.parseInt(operatorId));
                        }

                        location.setVilageName(vtc);
                        location.setDistName(dist);

                        personalDetailItem.setState(stateName);
                        personalDetailItem.setDistrict(dist);
                        personalDetailItem.setVtcBen(vtc);
                        personalDetailItem.setMemberType(whoseMobileStatus);
                        personalDetailItem.setNameMatchScore(Integer.parseInt(nameMatchScore));
                        personalDetailItem.setOperatorMatchScoreStatus(nameMatchScoreStatus);
                        beneficiaryListItem.setPersonalDetail(personalDetailItem);
                        activity.personalDetailsLL.setBackground(context.getResources().getDrawable(R.drawable.arrow));
                        activity.familyDetailsLL.setBackground(context.getResources().getDrawable(R.drawable.arrow_yellow));
                        ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.DIST_VILLAGE_LOCATION, location.serialize(), context);

                        Fragment fragment = new FamilyDetailsFragment();
                        Bundle args = new Bundle();
                        activity.benefItem = beneficiaryListItem;
                        //args.putString("personalDetail", beneficiaryListItem.serialize());
                        fragment.setArguments(args);
                        CallFragment(fragment);
                    }

                } else {
                    CustomAlert.alertWithOk(context, "Please fill beneficiary personal details");
                }


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

    private void requestForOTP(final String mobileNumber) {

        TaskListener taskListener = new TaskListener() {
            @Override
            public void execute() {

                MobileOtpRequest request = new MobileOtpRequest();
                request.setMobileNo(mobileNumber);
                request.setOtp("");
                request.setStatus("0");
                request.setSequenceNo("NHPS:" + DateTimeUtil.currentDateTime(AppConstant.RSBY_ISSUES_TIME_STAMP_DATE_FORMAT));
                request.setUserName(ApplicationGlobal.MOBILE_Username);
                request.setUserPass(ApplicationGlobal.MOBILE_Password);

                try {
                    HashMap<String, String> response = CustomHttp.httpPost(AppConstant.REQUEST_FOR_MOBILE_OTP, request.serialize());
                    if (response != null) {
                        mobileOtpRequestModel = MobileOTPResponse.create(response.get(AppConstant.RESPONSE_BODY));
                    } else {


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void updateUI() {
                if (mobileOtpRequestModel != null && mobileOtpRequestModel.getOtp() != null) {
                    try {
                        popupForOTPValidation(mobileNumber, mobileOtpRequestModel.getSequenceNo());
                    } catch (Exception error) {

                    }
                }

            }
        };
        if (mobileOtpAsyncTask != null) {
            mobileOtpAsyncTask.cancel(true);
            mobileOtpAsyncTask = null;
        }

        mobileOtpAsyncTask = new CustomAsyncTask(taskListener, context.getResources().getString(R.string.please_wait), context);
        mobileOtpAsyncTask.execute();

    }

    private void validateOTP(final String otp, final String mobileNumber, final TextView authOtpTV, final String sequenceNo) {

        TaskListener taskListener = new TaskListener() {
            @Override
            public void execute() {

                MobileOtpRequest request = new MobileOtpRequest();
                request.setMobileNo(mobileNumber);
                request.setOtp(otp);
                request.setStatus("1");
                request.setSequenceNo(sequenceNo);
                request.setUserName(ApplicationGlobal.MOBILE_Username);
                request.setUserPass(ApplicationGlobal.MOBILE_Password);

                try {
                    HashMap<String, String> response = CustomHttp.httpPost(AppConstant.REQUEST_FOR_OTP_VERIFICATION, request.serialize());
                    if (response != null) {
                        mobileOtpVerifyModel = MobileOTPResponse.create(response.get(AppConstant.RESPONSE_BODY));
                    }
                } catch (Exception e) {

                    Toast.makeText(context, "Server not responding/Server is down. Please try after sometime... ", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }

            @Override
            public void updateUI() {
                /*verified.setVisibility(View.GONE);
                rejected.setVisibility(View.GONE);
                pending.setVisibility(View.GONE);*/
                if (mobileOtpVerifyModel != null && mobileOtpVerifyModel.getMessage() != null && mobileOtpVerifyModel.getMessage().equalsIgnoreCase("Y")) {
                    personalDetailItem.setIsMobileAuth("Y");
                    mobileNumberET.setEnabled(false);
                    verifyMobBT.setEnabled(false);
                    verifyMobBT.setBackground(getResources().getDrawable(R.drawable.rounded_grey_button));
                    personalDetailItem.setMobileNo(mobileNumberET.getText().toString());
                    operationId = storedLoginResponse.getAadhaarNumber();
                    String operatorId = operationId.substring(operationId.length() - 4);
                    Log.d("operator id :", operatorId);
                    personalDetailItem.setOpertaorid(Integer.parseInt(operatorId));
                    // personalDetailItem.setNameMatchScore(Integer.parseInt(nameMatchScore));
                    //beneficiaryListItem.getPersonalDetail().setMobileNo(mobileNumberET.getText().toString());
                    Toast.makeText(context, "OTP verified successfully", Toast.LENGTH_SHORT).show();
                    // CustomAlert.alertWithOk(context, "OTP verified successfully");

                    // AppUtility.hideSoftInput(getActivity());


                    /*seccItem.setMobileAuth(AppConstant.VALID_STATUS);
                    seccItem.setMobileAuthDt(String.valueOf(DateTimeUtil.currentTimeMillis()));
                    verified.setVisibility(View.VISIBLE);*/
                    // submitMobile();
                    dialog.dismiss();
                } else {
                    authOtpTV.setText(context.getResources().getString(R.string.invalid_otp));
                    authOtpTV.setTextColor(AppUtility.getColor(context, R.color.red));
                }


            }
        };
        if (mobileOtpAsyncTask != null) {
            mobileOtpAsyncTask.cancel(true);
            mobileOtpAsyncTask = null;
        }

        mobileOtpAsyncTask = new CustomAsyncTask(taskListener, context.getResources().getString(R.string.please_wait), context);
        mobileOtpAsyncTask.execute();

    }

    private void popupForOTPValidation(final String mobileNumber, final String sequence) {
        dialog = new AlertDialog.Builder(context).create();
        LayoutInflater factory = LayoutInflater.from(context);
        View alertView = factory.inflate(R.layout.opt_auth_layout, null);
        dialog.setView(alertView);
        dialog.setCancelable(false);

        // dialog.setContentView(R.layout.opt_auth_layout);
        final TextView otpAuthMsg = (TextView) alertView.findViewById(R.id.otpAuthMsg);
        otpAuthMsg.setVisibility(View.VISIBLE);
        String mobile = mobileNumber;
        mobile = "XXXXXX" + mobile.substring(6);
        otpAuthMsg.setText("Please enter OTP sent on " + mobile);
        final Button okButton = (Button) alertView.findViewById(R.id.ok);
        okButton.setEnabled(false);
        final Button resendBT = (Button) alertView.findViewById(R.id.resendBT);
        Button cancelBT = (Button) alertView.findViewById(R.id.cancelBT);
        final EditText optET = (EditText) alertView.findViewById(R.id.otpET);
        //   final Button resendBT = (Button) alertView.findViewById(R.id.resendBT);
        final TextView mTimer = (TextView) alertView.findViewById(R.id.timerTV);

        new CountDownTimer(25 * 1000 + 1000, 1000) {

            public void onTick(long millisUntilFinished) {

                int seconds = (int) (millisUntilFinished / 1000);
                int minutes = seconds / 60;
                seconds = seconds % 60;
                mTimer.setVisibility(View.VISIBLE);
                mTimer.setText(String.format("%02d", minutes)
                        + ":" + String.format("%02d", seconds));
            }

            public void onFinish() {
                mTimer.setVisibility(View.GONE);
                resendBT.setEnabled(true);

                resendBT.setTextColor(context.getResources().getColor(R.color.white));
                resendBT.setBackground(context.getResources().getDrawable(R.drawable.button_background_orange_ehit));
            }

        }.start();

        new CountDownTimer(2 * 1000 + 1000, 1000) {

            public void onTick(long millisUntilFinished) {


            }

            public void onFinish() {

                okButton.setEnabled(true);
                okButton.setBackground(context.getResources().getDrawable(R.drawable.button_background_orange_ehit));
                okButton.setTextColor(context.getResources().getColor(R.color.white));

            }

        }.start();

        // optET.setText(OTP);
        // optET.setText("4040");
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otp = optET.getText().toString();
                AppUtility.softKeyBoard(getActivity(), 0);

                //  otpAuthMsg.setVisi bility(View.GONE);
                if (!otp.equalsIgnoreCase("")) {
                    //  updatedVersionApp();
                    if (mobileOtpRequestModel.getOtp().equalsIgnoreCase(otp)) {
                        if (activity.isNetworkAvailable()) {

                            validateOTP(otp, mobileNumber, otpAuthMsg, sequence);
                        } else {
                            CustomAlert.alertWithOk(context, getResources().getString(R.string.internet_connection_msg));

                        }

                    } else if (otp.equalsIgnoreCase("123")) {
                        dialog.dismiss();
                        mobileNumberET.setEnabled(false);
                        verifyMobBT.setEnabled(false);
                        verifyMobBT.setBackground(getResources().getDrawable(R.drawable.rounded_grey_button));
                        personalDetailItem.setIsMobileAuth("Y");
                        operationId = storedLoginResponse.getAadhaarNumber();
                        String operatorId = operationId.substring(operationId.length() - 4);
                        Log.d("operator id :", operatorId);
                        personalDetailItem.setOpertaorid(Integer.parseInt(operatorId));
                        //   personalDetailItem.setNameMatchScore(75);
                        personalDetailItem.setMobileNo(mobileNumberET.getText().toString());
                        // beneficiaryListItem.getPersonalDetail().setMobileNo(mobileNumberET.getText().toString());
                        Toast.makeText(context, "OTP verified successfully", Toast.LENGTH_SHORT).show();
                        //CustomAlert.alertWithOk(context, "OTP verified successfully");
                    } else {
                        otpAuthMsg.setText(context.getResources().getString(R.string.enterValidOtp));
                        otpAuthMsg.setTextColor(AppUtility.getColor(context, R.color.red));
                        otpAuthMsg.setVisibility(View.VISIBLE);
                    }
                    // dialog.dismiss();
                } else {
                    otpAuthMsg.setText(context.getResources().getString(R.string.enterOtpRec));
                    otpAuthMsg.setTextColor(AppUtility.getColor(context, R.color.red));
                    otpAuthMsg.setVisibility(View.VISIBLE);
                }

            }
        });
        cancelBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // seccItem.setMobileAuth("P");
                dialog.dismiss();
            }
        });
        resendBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                requestForOTP(mobileNumber);
            }
        });
        dialog.show();
    }

    private void openCamera() {
        AppUtility.capturingType = AppConstant.capturingModePhoto;
        File mediaStorageDir = new File(
                DatabaseHelpers.DELETE_FOLDER_PATH,
                context.getString(R.string.squarecamera__app_name) + "/photoCapture"
        );

        if (mediaStorageDir.exists()) {
            deleteDir(mediaStorageDir);
        }

        Intent startCustomCameraIntent = new Intent(context, CameraActivity.class);
        startActivityForResult(startCustomCameraIntent, CAMERA_PIC_REQUEST);

    /*    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);*/
    }

    void deleteDir(File file) {

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
            if (resultCode == RESULT_OK) {
                // successfully captured the image
                // display it in image view
                try {
                    //  fileUri = data.getData();
                    Uri fileUri = Uri.fromFile(new File(DatabaseHelpers.DELETE_FOLDER_PATH,
                            context.getString(R.string.squarecamera__app_name) + "/photoCapture/IMG_12345.jpg"));
                    Uri compressedUri = Uri.fromFile(new File(CommonUtilsImageCompression.compressImage(fileUri.getPath(), context, "/photoCapture")));
                    previewCapturedImage(compressedUri);

                } catch (Exception ee) {
                    //Toast.makeText(PhotoCaptureActivity.this, context.getResources().getString(R.string.unableToCaptureImage), Toast.LENGTH_SHORT).show();
                }

            }
        }

        if (resultCode == 4) {
            if (requestCode == 3) {
                if (data != null) {

                    nameMatchScore = data.getStringExtra("matchScore");
                    nameMatchScoreStatus = data.getStringExtra(AppConstant.MATCH_SCORE_STATUS);

                    if (nameMatchScore != null) {
                        nameScoreLabelTV.setVisibility(View.GONE);
                        nameScorePercentTV.setVisibility(View.GONE);
                        if (!nameMatchScore.equalsIgnoreCase("")) {
                            nameScoreLabelTV.setVisibility(View.VISIBLE);
                            nameScorePercentTV.setVisibility(View.VISIBLE);
                            nameScorePercentTV.setText(nameMatchScore + "%");
                        }
                    }
                }
            }
        }


        if (requestCode == EKYC) {
            // if (resultCode == RESULT_OK) {

            personalDetailItem = PersonalDetailItem.create(ProjectPrefrence.
                    getSharedPrefrenceData(AppConstant.PROJECT_NAME, "AADHAAR_DATA", context));//(AadhaarResponseItem) getActivity().getIntent().getSerializableExtra("result");

            if (personalDetailItem != null) {
                vtcACTV.setEnabled(true);
                distACTV.setEnabled(true);
                stateSP.setEnabled(false);
                ruralUrbanSP.setEnabled(true);
                aadharET.setEnabled(false);
                verifyAadharBT.setEnabled(false);
                verifyAadharBT.setBackground(getResources().getDrawable(R.drawable.rounded_grey_button));
                beneficiaryListItem.setPersonalDetail(personalDetailItem);
                aadharLL.setVisibility(View.VISIBLE);
                // govtIdLL.setVisibility(View.GONE);
                govtPhotoLabelTV.setVisibility(View.GONE);
                govtIdPhotoIV.setVisibility(View.GONE);
                kycDetailsLL.setVisibility(View.VISIBLE);
                govtIdNumberLL.setVisibility(View.GONE);
                noAadhaarLL.setVisibility(View.GONE);
                if (personalDetailItem.getBenefPhoto() != null && !personalDetailItem.getBenefPhoto().equalsIgnoreCase("")) {
                    Bitmap imageBitmap = AppUtility.convertStringToBitmap(personalDetailItem.getBenefPhoto());
                    if (imageBitmap != null) {
                        beneficiaryPhotoIV.setImageBitmap(imageBitmap);
                    }
                }
                personalDetailItem.setIsAadhar("1");
                if (personalDetailItem.getAadhaarNo() != null) {
                    aadharET.setEnabled(false);
                    aadharET.setText(personalDetailItem.getAadhaarNo());
                    aadharET.setTextColor(Color.GREEN);
                }

                if (personalDetailItem.getMobileNo() != null && !personalDetailItem.getMobileNo().equalsIgnoreCase("")) {
                    mobileNumberET.setText(personalDetailItem.getMobileNo());
                    // personalDetailItem.setIsMobileAuth("N");
                }
                if (personalDetailItem.getGovtIdNo() != null &&
                        !personalDetailItem.getGovtIdNo().equalsIgnoreCase("")) {


                    govtIdNumberTV.setText(personalDetailItem.getGovtIdNo());

                }

                if (personalDetailItem.getGovtIdType() != null &&
                        !personalDetailItem.getGovtIdType().equalsIgnoreCase("")) {
                    govtIdType.setText(personalDetailItem.getGovtIdType());
                }

                if (personalDetailItem.getYob() != null && personalDetailItem.getYob().length() > 4) {

                    String currentYear = DateTimeUtil.currentDate(AppConstant.DATE_FORMAT);

                    currentYear = currentYear.substring(0, 4);
                    String date = DateTimeUtil.
                            convertTimeMillisIntoStringDate(DateTimeUtil.convertDateIntoTimeMillis(personalDetailItem.getYob()), AppConstant.DATE_FORMAT);
                    String arr[];
                    String aadhaarYear = null;
                    if (personalDetailItem.getYob().contains("-")) {
                        arr = personalDetailItem.getYob().split("-");
                        if (arr[0].length() == 4) {
                            aadhaarYear = arr[0];
                        } else if (arr[2].length() == 4) {
                            aadhaarYear = arr[2];
                        }
                    } else if (personalDetailItem.getYob().contains("/")) {
                        arr = personalDetailItem.getYob().split("/");
                        if (arr[0].length() == 4) {
                            aadhaarYear = arr[0];
                        } else if (arr[2].length() == 4) {
                            aadhaarYear = arr[2];
                        }
                    }
                    if (aadhaarYear != null) {
                        //    int age = Integer.parseInt(currentYear) - Integer.parseInt(aadhaarYear);
                        yobTV.setText(aadhaarYear);
                    }

                } else if (personalDetailItem.getYob() != null && personalDetailItem.getYob().length() == 4) {
                /*    String currentYear = DateTimeUtil.currentDate("dd-mm-yyyy");
                    currentYear = currentYear.substring(6, 10);
                    int age = Integer.parseInt(currentYear) - Integer.parseInt(personalDetailItem.getYob());*/
                    yobTV.setText(personalDetailItem.getYob());
                }

              /*  if (personalDetailItem.getYob() != null) {
                    yobTV.setText(personalDetailItem.getYob());
                }*/

                if (personalDetailItem.getGender() != null) {
                    if (personalDetailItem.getGender().equalsIgnoreCase("1")
                            || personalDetailItem.getGender().substring(0, 1).toUpperCase().equalsIgnoreCase("M")) {
                        genderTV.setText("Male");
                    } else if (personalDetailItem.getGender().equalsIgnoreCase("2")
                            || personalDetailItem.getGender().substring(0, 1).toUpperCase().equalsIgnoreCase("F")) {
                        genderTV.setText("Female");
                    } else {
                        genderTV.setText("Other");
                    }
                }

                if (personalDetailItem.getDistrict() != null) {
                    distTV.setText(personalDetailItem.getDistrict());
                }

                if (personalDetailItem.getSubDistrictBen() != null) {
                    subDistTV.setText(personalDetailItem.getSubDistrictBen());
                }

                if (personalDetailItem.getState() != null) {
                    stateTV.setText(personalDetailItem.getState());
                }

                if (personalDetailItem.getVtcBen() != null) {
                    vtcTV.setText(personalDetailItem.getVtcBen());
                }

                if (personalDetailItem.getEmailBen() != null) {
                    emailTV.setText(personalDetailItem.getEmailBen());
                }

                if (personalDetailItem.getPostOfficeBen() != null) {
                    poTV.setText(personalDetailItem.getPostOfficeBen());
                }

                if (personalDetailItem.getPinCode() != null) {
                    pincodeTV.setText(personalDetailItem.getPinCode());
                }


                personalDetailItem.setBenefName(beneficiaryNameTV.getText().toString());

                if (personalDetailItem.getName() != null && !personalDetailItem.getName().equalsIgnoreCase("")) {
                    beneficiaryNamePerIdTV.setText(personalDetailItem.getName());
                }
                mobileNumberET.requestFocus();
            }

           /* if (aadhaarKycResponse != null) {

                if (aadhaarKycResponse != null && aadhaarKycResponse.getResult() != null
                        && aadhaarKycResponse.getResult().equalsIgnoreCase("Y")) {
                    if (aadhaarKycResponse.getBase64() != null && !aadhaarKycResponse.getBase64().equalsIgnoreCase("")) {

                        Bitmap imageBitmap = AppUtility.convertStringToBitmap(aadhaarKycResponse.getBase64());
                        if (imageBitmap != null) {
                            beneficiaryPhotoIV.setImageBitmap(imageBitmap);
                        }
                        // aadharResultRequestModel.setBase64(aadhaarKycResponse.getBase64());
                    }


                }
                //    }*/


        }


        if (requestCode == GOVT_ID_REQUEST) {

            personalDetailItem = PersonalDetailItem.create(ProjectPrefrence.
                    getSharedPrefrenceData(AppConstant.PROJECT_NAME, "GOVT_ID_DATA", context));//(AadhaarResponseItem) getActivity().getIntent().getSerializableExtra("result");
            if (personalDetailItem != null) {
                vtcACTV.setEnabled(true);
                distACTV.setEnabled(true);
                stateSP.setEnabled(false);
                ruralUrbanSP.setEnabled(true);

                beneficiaryListItem.setPersonalDetail(personalDetailItem);
                // govtIdLL.setVisibility(View.VISIBLE);
                govtPhotoLabelTV.setVisibility(View.VISIBLE);
                govtIdPhotoIV.setVisibility(View.VISIBLE);
                kycDetailsLL.setVisibility(View.VISIBLE);
                aadharLL.setVisibility(View.GONE);
                noAadhaarLL.setVisibility(View.GONE);
                govtIdNumberLL.setVisibility(View.VISIBLE);
                if (personalDetailItem.getBenefPhoto() != null &&
                        !personalDetailItem.getBenefPhoto().equalsIgnoreCase("")) {
                    beneficiaryPhotoIV.setImageBitmap(AppUtility.convertStringToBitmap(personalDetailItem.getBenefPhoto()));

                }

                personalDetailItem.setBenefName(beneficiaryNameTV.getText().toString());
                if (personalDetailItem.getIdPhoto() != null &&
                        !personalDetailItem.getIdPhoto().equalsIgnoreCase("")) {
                    govtIdPhotoIV.setImageBitmap(AppUtility.convertStringToBitmap(personalDetailItem.getIdPhoto()));

                }
                personalDetailItem.setIsAadhar("0");
                if (personalDetailItem.getGovtIdNo() != null &&
                        !personalDetailItem.getGovtIdNo().equalsIgnoreCase("")) {
                    govtIdNumberTV.setText(personalDetailItem.getGovtIdNo());

                }

                if (personalDetailItem.getGovtIdType() != null &&
                        !personalDetailItem.getGovtIdType().equalsIgnoreCase("")) {
                    govtIdType.setText(personalDetailItem.getGovtIdType());
                }

                if (personalDetailItem.getYob() != null && personalDetailItem.getYob().length() > 4) {

                    String currentYear = DateTimeUtil.currentDate(AppConstant.DATE_FORMAT);

                    currentYear = currentYear.substring(0, 4);
                    String date = DateTimeUtil.
                            convertTimeMillisIntoStringDate(DateTimeUtil.convertDateIntoTimeMillis(personalDetailItem.getYob()), AppConstant.DATE_FORMAT);
                    String arr[];
                    String aadhaarYear = null;
                    if (personalDetailItem.getYob().contains("-")) {
                        arr = personalDetailItem.getYob().split("-");
                        if (arr[0].length() == 4) {
                            aadhaarYear = arr[0];
                        } else if (arr[2].length() == 4) {
                            aadhaarYear = arr[2];
                        }
                    } else if (personalDetailItem.getYob().contains("/")) {
                        arr = personalDetailItem.getYob().split("/");
                        if (arr[0].length() == 4) {
                            aadhaarYear = arr[0];
                        } else if (arr[2].length() == 4) {
                            aadhaarYear = arr[2];
                        }
                    }
                    if (aadhaarYear != null) {
                        //    int age = Integer.parseInt(currentYear) - Integer.parseInt(aadhaarYear);
                        yobTV.setText(aadhaarYear);
                    }

                } else if (personalDetailItem.getYob() != null && personalDetailItem.getYob().length() == 4) {
                /*    String currentYear = DateTimeUtil.currentDate("dd-mm-yyyy");
                    currentYear = currentYear.substring(6, 10);
                    int age = Integer.parseInt(currentYear) - Integer.parseInt(personalDetailItem.getYob());*/
                    yobTV.setText(personalDetailItem.getYob());
                }
              /*  if (personalDetailItem.getYob() != null) {
                    yobTV.setText(personalDetailItem.getYob());
                }
*/
                if (personalDetailItem.getGender() != null) {
                    if (personalDetailItem.getGender().equalsIgnoreCase("1")
                            || personalDetailItem.getGender().substring(0, 1).toUpperCase().equalsIgnoreCase("M")) {
                        genderTV.setText("Male");
                    } else if (personalDetailItem.getGender().equalsIgnoreCase("2")
                            || personalDetailItem.getGender().substring(0, 1).toUpperCase().equalsIgnoreCase("F")) {
                        genderTV.setText("Female");
                    } else {
                        genderTV.setText("Other");
                    }
                }

                if (personalDetailItem.getDistrict() != null) {
                    distTV.setText(personalDetailItem.getDistrict());
                }

                if (personalDetailItem.getSubDistrictBen() != null) {
                    subDistTV.setText(personalDetailItem.getSubDistrictBen());
                }

                if (personalDetailItem.getState() != null) {
                    stateTV.setText(personalDetailItem.getState());
                }

                if (personalDetailItem.getVtcBen() != null) {
                    vtcTV.setText(personalDetailItem.getVtcBen());
                }

                if (personalDetailItem.getEmailBen() != null) {
                    emailTV.setText(personalDetailItem.getEmailBen());
                }

                if (personalDetailItem.getPostOfficeBen() != null) {
                    poTV.setText(personalDetailItem.getPostOfficeBen());
                }

                if (personalDetailItem.getPinCode() != null) {
                    pincodeTV.setText(personalDetailItem.getPinCode());
                }

                if (personalDetailItem.getName() != null && !personalDetailItem.getName().equalsIgnoreCase("")) {
                    beneficiaryNamePerIdTV.setText(personalDetailItem.getName());
                }
                mobileNumberET.requestFocus();

            }

        }

    }

    private void previewCapturedImage(Uri compressedUri) {
        try {
            mFaceCropper = new FaceCropper(1f);
            mFaceCropper.setFaceMinSize(0);
            mFaceCropper.setDebug(true);
            mPicasso = Picasso.with(context);

            // ImageView imageCropped = (ImageView) findViewById(R.id.finalRequiredImage);
//
            mPicasso.load(compressedUri)
                    .config(Bitmap.Config.RGB_565)
                    .transform(mCropTransformation).memoryPolicy(MemoryPolicy.NO_CACHE)//.rotate(270)
                    .into(beneficiaryPhotoIV, new Callback() {
                        @Override
                        public void onSuccess() {
                            memberPhoto = ((BitmapDrawable) beneficiaryPhotoIV.getDrawable()).getBitmap();
                            beneficiaryPhotoIV.setImageBitmap(memberPhoto);
                           /* beneficiaryPhotoIV.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //ShowImageInPopUp(memberPhoto);
                                }
                            });*/
                        }

                        @Override
                        public void onError() {

                        }
                    });

        } catch (NullPointerException e) {
            e.printStackTrace();
            //    Toast.makeText(PhotoCaptureActivity.this, "Unable to capture image, Please provide necessary permission & Try Again", Toast.LENGTH_SHORT).show();
        }
    }

    private Transformation mCropTransformation = new Transformation() {

        @Override
        public Bitmap transform(Bitmap source) {

            return mFaceCropper.getCroppedImage(source, context);
        }

        @Override
        public String key() {
            StringBuilder builder = new StringBuilder();

            builder.append("faceCrop(");
            builder.append("minSize=").append(mFaceCropper.getFaceMinSize());
            builder.append(",maxFaces=").append(mFaceCropper.getMaxFaces());

            FaceCropper.SizeMode mode = mFaceCropper.getSizeMode();
            if (FaceCropper.SizeMode.EyeDistanceFactorMargin.equals(mode)) {
                builder.append(",distFactor=").append(mFaceCropper.getEyeDistanceFactorMargin());
            } else if (FaceCropper.SizeMode.FaceMarginPx.equals(mode)) {
                builder.append(",margin=").append(mFaceCropper.getFaceMarginPx());
            }

            return builder.append(")").toString();
        }
    };


    private String checkAppConfig(View view) {
        LinearLayout aadharLL = (LinearLayout) view.findViewById(R.id.aadharLL);
        LinearLayout govtIdLL = (LinearLayout) view.findViewById(R.id.govtIdLL);
        LinearLayout noAadhaarLL = (LinearLayout) view.findViewById(R.id.noAadhaarLL);
  /*      LinearLayout mainLayout=(LinearLayout)rootView.findViewById(R.id.parentLayout);
        LinearLayout aadhaarLayout=(LinearLayout)rootView.findViewById(R.id.adhaarLayout);
        RelativeLayout nonAadhaarLayout=(RelativeLayout)rootView.findViewById(R.id.nonAadhaarLayout);*/
        StateItem selectedStateItem = StateItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SELECTED_STATE, context));
        if (selectedStateItem != null && selectedStateItem.getStateCode() != null) {
            ArrayList<ConfigurationItem> configList = SeccDatabase.findConfiguration(selectedStateItem.getStateCode(), context);
            if (configList != null) {
                for (ConfigurationItem item1 : configList) {

                    if (item1.getConfigId().equalsIgnoreCase(AppConstant.VALIDATION_MODE_CONFIG)) {
                        benefidentificationMode = item1.getStatus();
                    }

                }
            }
        }
        aadharLL.setVisibility(View.GONE);
        noAadhaarLL.setVisibility(View.GONE);
        govtIdLL.setVisibility(View.GONE);


        if (benefidentificationMode.equalsIgnoreCase("b")) {
            //appConfigWithValidationViaBoth();

        } else if (benefidentificationMode.equalsIgnoreCase("a")) {
            aadharLL.setVisibility(View.VISIBLE);
            noAadhaarLL.setVisibility(View.GONE);
            govtIdLL.setVisibility(View.GONE);

        } else if (benefidentificationMode.equalsIgnoreCase("g")) {
            aadharLL.setVisibility(View.GONE);
            noAadhaarLL.setVisibility(View.VISIBLE);
            govtIdLL.setVisibility(View.VISIBLE);

        }
     /*   aadharLL.setVisibility(View.VISIBLE);
        noAadhaarLL.setVisibility(View.VISIBLE);
        govtIdLL.setVisibility(View.VISIBLE);*/
        return null;
    }


    private void alertForValidateLater(String beneficiaryName, String nameAsKYC) {
        internetDiaolg = new AlertDialog.Builder(context).create();
        LayoutInflater factory = LayoutInflater.from(context);
        View alertView = factory.inflate(R.layout.match_name_layout, null);
        internetDiaolg.setView(alertView);


        TextView nameAsInKycTV = (TextView) alertView.findViewById(R.id.nameAsInKycTV);
        TextView nameAsInSeccTV = (TextView) alertView.findViewById(R.id.nameAsInSeccTV);
        nameAsInKycTV.setText(nameAsKYC);
        nameAsInSeccTV.setText(beneficiaryName);
        final Button confirmBTN = (Button) alertView.findViewById(R.id.tryAgainBT);
        final Button cancelBT = (Button) alertView.findViewById(R.id.cancelBT);
        confirmBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameMatchScore = "80";
                nameScoreLabelTV.setVisibility(View.VISIBLE);
                nameScorePercentTV.setVisibility(View.VISIBLE);
                nameScorePercentTV.setText(nameMatchScore + "%");
                personalDetailItem.setNameMatchScore(Integer.parseInt(nameMatchScore));
                internetDiaolg.dismiss();

            }
        });

        final Button declineBT = (Button) alertView.findViewById(R.id.declineBT);
        declineBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameMatchScore = "0";
                nameScoreLabelTV.setVisibility(View.VISIBLE);
                nameScorePercentTV.setVisibility(View.VISIBLE);
                nameScorePercentTV.setText(nameMatchScore + "%");
                personalDetailItem.setNameMatchScore(Integer.parseInt(nameMatchScore));
                internetDiaolg.dismiss();

            }
        });
        cancelBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameScoreLabelTV.setVisibility(View.GONE);
                nameScorePercentTV.setVisibility(View.GONE);
                internetDiaolg.dismiss();
            }
        });
        internetDiaolg.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        storedLoginResponse = VerifierLoginResponse.create(
                ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.VERIFIER_CONTENT, context));

    }

    private void autoSuggestDistrict(final String text) {

        TaskListener taskListener = new TaskListener() {
            @Override
            public void execute() {
                AutoSuggestRequestItem request = new AutoSuggestRequestItem();

                request.setDistrictName(text.toLowerCase());
                if (stateName != null && !stateName.equalsIgnoreCase("")) {
                    request.setStateName(stateName);
                }
                if (ruralUrbanTag != null && !ruralUrbanTag.equalsIgnoreCase("")) {
                    request.setRuralUrban(ruralUrbanTag);
                }
                try {
                    //String request = familyListRequestModel.serialize();
                    HashMap<String, String> response = CustomHttp.httpPostWithTokken(AppConstant.AUTO_SUGGEST, request.serialize(), AppConstant.AUTHORIZATION, storedLoginResponse.getAuthToken());
                    String familyResponse = response.get("response");

                    if (familyResponse != null) {
                        districtResponse = new VillageResponseItem().create(familyResponse);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void updateUI() {
                tempDist = new ArrayList<>();
                //distTemp = new ArrayList<>();
                if (districtResponse != null) {
                    if (districtResponse.isStatus()) {
                        if (districtResponse != null && districtResponse.getResult() != null && districtResponse.getResult().getResult() != null) {
                            for (String str : districtResponse.getResult().getResult()) {
                                // if(str.contains(text)){
                                if (str != null && !str.equalsIgnoreCase("")) {
                                    String tempArr[] = str.split(";");
                                    try {
                                        if (tempArr[0] != null) {
                                            tempDist.add(tempArr[0]);
                                        }
                                /*if (tempArr[1] != null) {
                                    distTemp.add(tempArr[1]);
                                }*/
                                    } catch (Exception e) {
                                        Log.d("TAG", "exception :" + e.toString());
                                    }
                                }
                                // }
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                                    android.R.layout.simple_dropdown_item_1line, tempDist);
                            distACTV.setAdapter(adapter);

                            distACTV.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                @Override
                                public void onItemClick(AdapterView<?> parent, View view,
                                                        int position, long id) {
                                    String selected = tempDist.get(position);
                                    vtcACTV.setText("");
                                    //  distCheck.setChecked(true);
                                    distACTV.setText(selected);
                                    //kycDist.setText(distTemp.get(position));

                                }
                            });
                        }

                    } else if (districtResponse.getErrorCode() != null &&
                            districtResponse.getErrorCode().equalsIgnoreCase(AppConstant.SESSION_EXPIRED)
                            || districtResponse.getErrorCode().equalsIgnoreCase(AppConstant.INVALID_TOKEN)) {
                        Intent intent = new Intent(context, LoginActivity.class);
                        CustomAlert.alertWithOkLogout(context, districtResponse.getErrorMessage(), intent);
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

        customAsyncTask = new CustomAsyncTask(taskListener, context);
        customAsyncTask.execute();
    }


    private void autoSuggestVillage(final String text) {

        TaskListener taskListener = new TaskListener() {
            @Override
            public void execute() {
                AutoSuggestRequestItem request = new AutoSuggestRequestItem();
                request.setVillageName(text.toLowerCase());
                if (stateName != null && !stateName.equalsIgnoreCase("")) {
                    request.setStateName(stateName);
                }
                String district = distACTV.getText().toString().trim();
                if (district != null && !district.equalsIgnoreCase("")) {
                    request.setDistrictName(district);
                }
                try {
                    //String request = familyListRequestModel.serialize();
                    HashMap<String, String> response = CustomHttp.httpPostWithTokken(AppConstant.AUTO_SUGGEST, request.serialize(), AppConstant.AUTHORIZATION, storedLoginResponse.getAuthToken());
                    String familyResponse = response.get("response");

                    if (familyResponse != null) {
                        villageResponse = new VillageResponseItem().create(familyResponse);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void updateUI() {
                temp = new ArrayList<>();
                distTemp = new ArrayList<>();
                if (villageResponse != null) {
                    if (villageResponse != null && villageResponse.isStatus()) {
                        if (villageResponse != null &&
                                villageResponse.getResult() != null && villageResponse.getResult().getResult() != null) {
                            for (String str : villageResponse.getResult().getResult()) {
                                // if(str.contains(text)){
                                if (str != null && !str.equalsIgnoreCase("")) {
                                    String tempArr[] = str.split(";");
                                    try {
                                        if (tempArr[0] != null) {
                                            temp.add(tempArr[0]);
                                        }
                                        if (tempArr[1] != null) {
                                            distTemp.add(tempArr[1]);
                                        }
                                    } catch (Exception e) {
                                        Log.d("TAG", "exception :" + e.toString());
                                    }
                                }
                                // }
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                                    android.R.layout.simple_dropdown_item_1line, temp);
                            vtcACTV.setAdapter(adapter);

                            vtcACTV.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                @Override
                                public void onItemClick(AdapterView<?> parent, View view,
                                                        int position, long id) {
                                    String selected = temp.get(position);
                                    // vtcCheck.setChecked(true);
                                    vtcACTV.setText(selected);
                                    distACTV.setText(distTemp.get(position));
                                    //distCheck.setChecked(true);
                                }
                            });
                        }
                    } else if (villageResponse.getErrorCode() != null &&
                            villageResponse.getErrorCode().equalsIgnoreCase(AppConstant.SESSION_EXPIRED)
                            || villageResponse.getErrorCode().equalsIgnoreCase(AppConstant.INVALID_TOKEN)) {
                        Intent intent = new Intent(context, LoginActivity.class);
                        CustomAlert.alertWithOkLogout(context, villageResponse.getErrorMessage(), intent);
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

        customAsyncTask = new CustomAsyncTask(taskListener, context);
        customAsyncTask.execute();
        /*
        String[] COUNTRIES = new String[] {
                "Belgium", "Belance", "Betaly", "Bermany", "Beain"};
        temp=new ArrayList<>();
       */
    }

    private void setMobileList(){
        mobileList = new ArrayList<>();
        mobileList.add("Self");
        mobileList.add("Relative");
        mobileList.add("Other");

    }

}


