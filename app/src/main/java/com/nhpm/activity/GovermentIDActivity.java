package com.nhpm.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.customComponent.CustomAlert;
import com.customComponent.CustomAsyncTask;
import com.customComponent.TaskListener;
import com.customComponent.utility.CustomHttp;
import com.customComponent.utility.DateTimeUtil;
import com.customComponent.utility.ProjectPrefrence;
import com.nhpm.BaseActivity;
import com.nhpm.CameraUtils.CommonUtilsImageCompression;
import com.nhpm.CameraUtils.FaceCropper;
import com.nhpm.CameraUtils.squarecamera.CameraActivity;
import com.nhpm.DocCamera.ImageUtil;
import com.nhpm.LocalDataBase.DatabaseHelpers;
import com.nhpm.LocalDataBase.dto.SeccDatabase;
import com.nhpm.Models.SearchLocation;
import com.nhpm.Models.request.AutoSuggestRequestItem;
import com.nhpm.Models.request.GovtDetailsModel;
import com.nhpm.Models.request.PersonalDetailItem;
import com.nhpm.Models.response.GovernmentIdItem;
import com.nhpm.Models.response.VillageResponseItem;
import com.nhpm.Models.response.master.ConfigurationItem;
import com.nhpm.Models.response.master.StateItem;
import com.nhpm.Models.response.seccMembers.SeccMemberItem;
import com.nhpm.Models.response.seccMembers.SelectedMemberItem;
import com.nhpm.Models.response.verifier.VerifierLoginResponse;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;
import com.nhpm.Utility.AppUtility;
import com.nhpm.fragments.GovtIdDataFragment;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pl.polidea.view.ZoomView;

import static com.nhpm.DocCamera.ImageUtil.rotateImage;
import static com.nhpm.Utility.AppUtility.isCheckFirstTwoChar;
import static com.nhpm.fragments.NonAadharLoginFragment.isEmailValid;

public class GovermentIDActivity extends BaseActivity {

    private Spinner govtIdSP;
    private Context context;
    private ArrayList<GovernmentIdItem> govtIdStatusList;
    private LinearLayout voterIdLayout, rationCardLayout, govtIdPhotoLayout;
    private TextView headerTV;
    private ImageView rashanCardIV, voterIdIV;
    private ImageView voterClickIV, rashanCardClickIV;
    private int CAMERA_PIC_REQUEST = 0;
    private int CAMERA_PIC_BACK_REQUEST = 101;
    private int PIC_REQUEST = 1;
    private int PIC_REQUEST2 = 2;
    private String benefImage;

    private LinearLayout rashanCardCaptureLayout, enrollmentLayout;
    private RelativeLayout voterIdCaptureLayout;
    private Bitmap captureImageBM,captureImageBackBM;
    private LinearLayout nameLL;
    private ImageView backIV;
    private Button rationCardSubmitBT, voterIdSubmitBT, enrollmentSubmitBT;
    private int navigateType;
    private SelectedMemberItem selectedMemItem;
    private SeccMemberItem seccItem;
    private String aadhaarStatus = "";
    private GovernmentIdItem item;
    private EditText rationCardNameET, rationCardNumberET, voterIdCardNameET, voterIdCardNumberET, enrollmentNameET, enrollmentIdET;
    private AlertDialog internetDiaolg;
    private String voterIdImg, rashanCardImg,voterIdBackImg="";
    private int selectedId = 0;
    private int RASHAN_CARD_REQUEST = 1;
    private int VOTER_ID_REQUEST = 2;
    private String consent = "y";
    private final String TAG = "Government Activity";
   /* govtIdStatusList.add(new GovernmentIdItem(0, "Choose Your ID Card"));
        govtIdStatusList.add(new GovernmentIdItem(13, "Aadhaar"));
        govtIdStatusList.add(new GovernmentIdItem(1, "Voter ID Card"));
        govtIdStatusList.add(new GovernmentIdItem(2, "Ration Card"));
        govtIdStatusList.add(new GovernmentIdItem(10, "MNREGA Job Card"));
        govtIdStatusList.add(new GovernmentIdItem(4, "Driving License"));
        govtIdStatusList.add(new GovernmentIdItem(14, "Government ID with no Photograph"));*/

    private final int ENROLLMENT_ID = 15,
            VOTER_ID = 1, RASHAN_CARD = 3, NREGA = 10,
            DRIVIG_LICENCE = 4, BIRTH_CERT = 11, OTHER_CARD = 14,
            NO_GOVID = 8, ID_NO_PHOTO = 9, AADHAR_ID = 13;
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
    private Bitmap memberPhoto;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransection;
    private Fragment fragment;

    private EditText nameTV, yobET, pincodeET, emailET, subDistET, distET, vtcET, poET, stateET;
    private RadioGroup genderRG;
    private RadioButton maleRB, femaleRB, otherRB;
    private String manualGenderSelection = "", currentYear;
    private boolean emailValid = false;


    private LinearLayout photoLayout;
    private Button capturePhotoBT;
    private ImageView photoIV;
    private FaceCropper mFaceCropper;
    private Picasso mPicasso;
    private Button captureVoterIdBT,captureVoterIdBackBT;
    private ImageView voterIdBackIV;
    private String mobileNumber = "";
    private PersonalDetailItem personalDetailItem;
    private AutoCompleteTextView distTV, vtcTV;
    private Spinner stateSP;
    private String stateName;
    private StateItem selectedStateItem;
    private VillageResponseItem villageResponse, districtResponse;
    private CustomAsyncTask customAsyncTask;
    private ArrayList<String> temp, distTemp;
    private ArrayList<String> tempDist;
    private SearchLocation location = new SearchLocation();
    private VerifierLoginResponse verifierLoginResponse;

    private String blockCharacterSet = ":-/\\\\\\.";
    private Bitmap bitmap;
    private String purpose = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        activity = this;
        checkAppConfig();
        verifierLoginResponse = VerifierLoginResponse.create(
                ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.VERIFIER_CONTENT, context));

        if (zoomMode.equalsIgnoreCase("N")) {
            setContentView(R.layout.activity_goverment_id);
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
        View v = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.activity_goverment_id, null, false);
        v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
        showNotification(v);
        headerTV = (TextView) v.findViewById(R.id.centertext);
        selectedMemItem = SelectedMemberItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.SELECTED_ITEM_FOR_VERIFICATION, context));
        location = SearchLocation.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.DIST_VILLAGE_LOCATION, context));
        govtIdSP = (Spinner) v.findViewById(R.id.govtIdSP);
        prepareGovernmentIdSpinner();
        nameTV = (EditText) v.findViewById(R.id.nameET);

        yobET = (EditText) v.findViewById(R.id.yobET);
        pincodeET = (EditText) v.findViewById(R.id.pincodeET);
        emailET = (EditText) v.findViewById(R.id.emailET);
        subDistET = (EditText) v.findViewById(R.id.subDistET);
        //distET = (EditText) v.findViewById(R.id.distET);
        //vtcET = (EditText) v.findViewById(R.id.vtcET);
        poET = (EditText) v.findViewById(R.id.poET);
        //  stateET = (EditText) v.findViewById(R.id.stateET);
        distTV = (AutoCompleteTextView) v.findViewById(R.id.distTV);
        vtcTV = (AutoCompleteTextView) v.findViewById(R.id.vtcTV);
        distTV.setThreshold(1);
        vtcTV.setThreshold(1);
        distTV.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (distTV.hasFocus())
                    autoSuggestDistrict(s.toString());
                // AppUtility.softKeyBoard(FingerprintResultActivity.this, 0);
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });
        vtcTV.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (vtcTV.hasFocus())
                    autoSuggestVillage(s.toString());
                // AppUtility.softKeyBoard(FingerprintResultActivity.this, 0);
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });
        if (location != null) {
            if (!location.getVilageName().equalsIgnoreCase("")) {
                vtcTV.setText("");
                vtcTV.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        vtcTV.showDropDown();
                        vtcTV.setText(location.getVilageName());
                        //kycVtc.setSelection(mACTextViewEmail.getText().length());
                    }
                }, 500);

               /* if(location.isVillageTrue()){
                    vtcTV.setChecked(true);
                }*/
            }
            if (!location.getDistName().equalsIgnoreCase("")) {
                distTV.setText("");

                distTV.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        distTV.showDropDown();
                        distTV.setText(location.getDistName());
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

        stateSP = (Spinner) v.findViewById(R.id.stateSP);

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
        final ArrayList<StateItem> stateList2 = new ArrayList<>();
        if (stateList != null) {
            for (StateItem item1 : stateList1) {
                if (item1.getStateCode().equalsIgnoreCase("16")) {
                    stateList.add("Haryana");
                    stateList2.add(item1);
                } else if (item1.getStateCode().equalsIgnoreCase("22")) {
                    stateList.add("Chattisgarh");
                    stateList2.add(item1);
                } else if (item1.getStateCode().equalsIgnoreCase("07")) {
                    stateList.add("Delhi");
                    stateList2.add(item1);
                } else if (item1.getStateCode().equalsIgnoreCase("09")) {
                    stateList.add("Uttar Pradesh");
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


        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, stateList);
        stateSP.setAdapter(adapter1);

        for (int i = 0; i < stateList2.size(); i++) {

            if (selectedStateItem.getStateCode().equalsIgnoreCase(stateList2.get(i).getStateCode())) {

                stateSP.setSelection(i);
                // stateSP.setTitle(item.getStateName());

                stateName = stateList.get(i);
                Log.d("state name11 :", stateName);

                break;
            }
        }
        genderRG = (RadioGroup) v.findViewById(R.id.genderRG);
        maleRB = (RadioButton) v.findViewById(R.id.maleRB);
        femaleRB = (RadioButton) v.findViewById(R.id.femaleRB);
        otherRB = (RadioButton) v.findViewById(R.id.otherRB);
        genderRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == maleRB.getId()) {
                    manualGenderSelection = "1";
                } else if (checkedId == femaleRB.getId()) {
                    manualGenderSelection = "2";
                } else if (checkedId == otherRB.getId()) {
                    manualGenderSelection = "3";
                }

            }

        });

        pincodeET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() > 0) {
                    if (!charSequence.toString().startsWith("0")) {
                        // if (Integer.parseInt(charSequence.toString().substring(1)) < 2) {

                        pincodeET.setTextColor(context.getResources().getColor(R.color.black_shine));

                        if (pincodeET.getText().toString().length() == 6) {
                            pincodeET.setTextColor(context.getResources().getColor(R.color.green));
                            // isValidMobile = true;
                        } else {
                            //isValidMobile = false;
                        }
                    } else {
                        //isValidMobile = false;
                        pincodeET.setTextColor(context.getResources().getColor(R.color.red));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        yobET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() > 0) {
                    if (charSequence.toString().startsWith("1") || charSequence.toString().startsWith("2")) {
                        // if (Integer.parseInt(charSequence.toString().substring(1)) < 2) {

                        yobET.setTextColor(context.getResources().getColor(R.color.black_shine));

                        if (yobET.getText().toString().length() == 4) {
                            yobET.setTextColor(context.getResources().getColor(R.color.green));
                            // isValidMobile = true;
                        } else {
                            //isValidMobile = false;
                        }
                    } else {
                        //isValidMobile = false;
                        yobET.setTextColor(context.getResources().getColor(R.color.red));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        emailET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                emailET.setTextColor(Color.BLACK);
                emailValid = false;
                if (!isEmailValid(s.toString())) {

                    emailET.setTextColor(Color.RED);
                    emailValid = false;
                } else {
                    emailET.setTextColor(Color.GREEN);
                    emailValid = true;
                    //AppUtility.softKeyBoard(activity, 0);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        String currentDate = DateTimeUtil.currentDate("dd MM yyyy");
        Log.d("current date", currentDate);
        currentYear = currentDate.substring(6, 10);
        Log.d("current year", currentYear);

        personalDetailItem = PersonalDetailItem.create(getIntent().getStringExtra("mobileNumber"));
        // nameLL= (LinearLayout) v.findViewById(R.id.nameBenefLL);
        //  nameLL.setVisibility(View.GONE);
        photoLayout = (LinearLayout) v.findViewById(R.id.photoLayout);
        // photoLayout.setVisibility(View.GONE);
        capturePhotoBT = (Button) v.findViewById(R.id.capturePhotoBT);
        photoIV = (ImageView) v.findViewById(R.id.photoIV);
        voterIdIV = (ImageView) v.findViewById(R.id.voterIdIV);
        voterIdBackIV = (ImageView) v.findViewById(R.id.voterIdBackIV);
        rashanCardIV = (ImageView) v.findViewById(R.id.rationCardIV);
        backIV = (ImageView) v.findViewById(R.id.back);
        rationCardLayout = (LinearLayout) v.findViewById(R.id.rationCardLayout);
        voterIdLayout = (LinearLayout) v.findViewById(R.id.voterIdLayout);
        voterIdCaptureLayout = (RelativeLayout) v.findViewById(R.id.voterIdCaptureLayout);
        captureVoterIdBT = (Button) v.findViewById(R.id.captureVoterIdBT);
        captureVoterIdBackBT = (Button) v.findViewById(R.id.captureVoterIdBackBT);
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

        capturePhotoBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
            }
        });
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

        voterIdIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(voterIdImg!=null && !voterIdImg.equalsIgnoreCase("")){
                    PersonalDetailItem personalDetailItem= new PersonalDetailItem();
                    personalDetailItem.setIdPhoto(voterIdImg);
                    Intent intent = new Intent(context, ViewDocImageActivity.class);
                    ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,"GovtDocImage",personalDetailItem.getIdPhoto(),context);
                    //  intent.putExtra("DocImage",personalDetailItem.getIdPhoto());
                    intent.putExtra("ScreenName","GovernmentIdActivity");
                    startActivity(intent);
                }else {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzCaptureGovId));
                    return;
                }
            }
        });

        voterIdBackIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(voterIdBackImg!=null && !voterIdBackImg.equalsIgnoreCase("")){
                    PersonalDetailItem personalDetailItem= new PersonalDetailItem();
                    personalDetailItem.setIdPhoto1(voterIdBackImg);
                    Intent intent = new Intent(context, ViewDocImageActivity.class);
                    ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,"GovtDocImage",personalDetailItem.getIdPhoto1(),context);
                    //  intent.putExtra("DocImage",personalDetailItem.getIdPhoto());
                    intent.putExtra("ScreenName","GovernmentIdActivity");

                    startActivity(intent);
                }else {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzCaptureGovId));
                    return;
                }
            }
        });

        voterIdSubmitBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String voterIdNumber = voterIdCardNumberET.getText().toString();
                String voterIdName = voterIdCardNameET.getText().toString();
                String name = nameTV.getText().toString();
                String yob = yobET.getText().toString();
                String pincode = pincodeET.getText().toString();
                String subDist = subDistET.getText().toString();

                String po = poET.getText().toString();
                String email = emailET.getText().toString();
                //     String vtc = vtcTV.getText().toString();
         //       String dist = distTV.getText().toString();
                //String state = stateET.getText().toString();

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


                    if (name.equalsIgnoreCase("")) {
                        CustomAlert.alertWithOk(context, "Please enter name");
                        return;
                    }

                    if (!isCheckFirstTwoChar(voterIdNumber)) {
                        voterIdCardNumberET.setText("");
                        CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterValidGovId));
                        return;
                    }

                    if (item.statusCode == 13) {
                        Log.d("TAG", "Aaadhaar Length : " + voterIdCardNameET.getText().toString().length());
                        if (voterIdCardNumberET.getText().toString().length() != 12) {
                            CustomAlert.alertWithOk(context, "Please enter valid Aadhaar Number");
                            return;
                        }

                    }
                    /*if (benefImage != null && benefImage.equalsIgnoreCase("")) {
                        CustomAlert.alertWithOk(context, "Please capture beneficiary image");
                        return;
                    }*/

                    if (benefImage != null && benefImage.equalsIgnoreCase("")) {
                        CustomAlert.alertWithOk(context, "Please capture beneficiary image");
                        return;
                    }

                    if (benefImage == null || benefImage.equalsIgnoreCase("")) {
                        CustomAlert.alertWithOk(context, "Please capture beneficiary image");
                        return;
                    }

                    if (yob.trim().equalsIgnoreCase("")) {
                        CustomAlert.alertWithOk(context, "Please enter year of birth");
                        return;
                    }

                    if (yob != null && !yob.equalsIgnoreCase("")) {
                        int yearRange = Integer.parseInt(currentYear) - 100;

                        if (yob.equalsIgnoreCase(currentYear) || Integer.parseInt(yob) < yearRange) {
                            CustomAlert.alertWithOk(context, "Please enter valid year of birth");
                            return;
                        }

                    }


                    if (manualGenderSelection != null && manualGenderSelection.equalsIgnoreCase("")) {
                        CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterGenderGovt));
                        return;
                    }

                    if (pincode.equalsIgnoreCase("")) {
                        CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterPincodeGovt));
                        return;
                    }

                    /*if (subDist.equalsIgnoreCase("")) {
                        CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterSubDist));
                        return;
                    }*/



                  /*  if (po.equalsIgnoreCase("")) {
                        CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterPo));
                        return;
                    }*/

                   /* if (email.equalsIgnoreCase("")) {
                        CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterEmail));
                        return;
                    }

                    if(!emailValid){
                        CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterEmailValid));
                        return;
                    }*/
    /* if (vtc.equalsIgnoreCase("")) {
                        CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterVTC));
                        return;
                    }*/
                 /*   if (state.equalsIgnoreCase("")) {
                        CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterState));
                        return;
                    }*/

                   /* if (dist.equalsIgnoreCase("")) {
                        CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterDist));
                        return;
                    }*/


                    if (personalDetailItem != null) {
                        /*if (mobileNumber != null) {
                            personalDetailItem.setMobileNo(mobileNumber);
                        }*/
                        personalDetailItem.setBenefPhoto(benefImage);
                        personalDetailItem.setName(name);
                        personalDetailItem.setIdPhoto(voterIdImg);
                        personalDetailItem.setIdPhoto1(voterIdBackImg);

                        personalDetailItem.setIsAadhar("N");
                        personalDetailItem.setGovtIdType(item.status);
                        personalDetailItem.setIdName(item.statusCode + "");
                        personalDetailItem.setFlowStatus(AppConstant.GOVT_STATUS);
                        personalDetailItem.setGovtIdNo(voterIdCardNumberET.getText().toString());

                        personalDetailItem.setYob(yob);
                       // personalDetailItem.setState(stateName);
                        personalDetailItem.setGender(manualGenderSelection);
                        personalDetailItem.setPinCode(pincode);
                        personalDetailItem.setSubDistrictBen(subDist);
                      //  personalDetailItem.setVtcBen(vtc);
                        personalDetailItem.setPostOfficeBen(po);
                        personalDetailItem.setEmailBen(email);
                      //  personalDetailItem.setDistrict(dist);
                        //location.setVilageName(vtc);
                       // location.setDistName(dist);
                    } else {

                        personalDetailItem = new PersonalDetailItem();
                     /*   if (mobileNumber != null) {
                            personalDetailItem.setMobileNo(mobileNumber);
                        }*/
                        personalDetailItem.setBenefPhoto(benefImage);
                        personalDetailItem.setName(name);
                        personalDetailItem.setIdPhoto(voterIdImg);
                        personalDetailItem.setIdPhoto1(voterIdBackImg);
                        personalDetailItem.setIsAadhar("N");
                        personalDetailItem.setGovtIdType(item.status);
                        personalDetailItem.setIdName(item.statusCode + "");
                        personalDetailItem.setFlowStatus(AppConstant.GOVT_STATUS);
                        personalDetailItem.setGovtIdNo(voterIdCardNumberET.getText().toString());

                        personalDetailItem.setYob(yob);
                     //   personalDetailItem.setState(stateName);
                        personalDetailItem.setGender(manualGenderSelection);
                        personalDetailItem.setPinCode(pincode);
                        personalDetailItem.setSubDistrictBen(subDist);
                     //   personalDetailItem.setVtcBen(vtc);
                        personalDetailItem.setPostOfficeBen(po);
                        personalDetailItem.setEmailBen(email);
                      //  personalDetailItem.setDistrict(dist);
                     //   location.setVilageName(vtc);
                     //   location.setDistName(dist);
                    }
                    ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_NAME, "GOVT_ID_DATA", personalDetailItem.serialize(), context);
                 //   ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.DIST_VILLAGE_LOCATION, location.serialize(), context);

                    activity.finish();
//                    }
                    //  alertForValidateLater(voterIdNumber, voterIdName);

                    //openGovtIdDataFragment();
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

        captureVoterIdBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtility.openCamera(activity, AppConstant.BACK_CAMREA_OPEN, "ForStore", "DummyImagePreviewClass");

              /*  AppUtility.capturingType = AppConstant.capturingModeGovId;

                File mediaStorageDir = new File(
                        DatabaseHelpers.DELETE_FOLDER_PATH,
                        context.getString(R.string.squarecamera__app_name) + "/photoCapture"
                );

                if (mediaStorageDir.exists()) {
                    deleteDir(mediaStorageDir);
                }
                Intent startCustomCameraIntent = new Intent(context, CameraActivity.class);
                startActivityForResult(startCustomCameraIntent, CAMERA_PIC_REQUEST);*/

            }
        });

        captureVoterIdBackBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtility.openCameraForGovtId(activity, AppConstant.BACK_CAMREA_OPEN, "ForStore", "DummyImagePreviewClass");

               /* AppUtility.capturingType = AppConstant.capturingModeGovId;

                File mediaStorageDir = new File(
                        DatabaseHelpers.DELETE_FOLDER_PATH,
                        context.getString(R.string.squarecamera__app_name) + "/photoCapture"
                );

                if (mediaStorageDir.exists()) {
                    deleteDir(mediaStorageDir);
                }
                Intent startCustomCameraIntent = new Intent(context, CameraActivity.class);
                startActivityForResult(startCustomCameraIntent, CAMERA_PIC_BACK_REQUEST);*/

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
                voterIdCardNumberET.setInputType(InputType.TYPE_CLASS_TEXT);
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
                        //   nameLL.setVisibility(View.VISIBLE);
                        govtIdPhotoLayout.setVisibility(View.VISIBLE);
                        /*voterIdCardNumberET.setText("");
                        voterIdCardNameET.setText("");*/
                        //updateScreen(voterIdImg);
                        voterIdCardNumberET.setHint(context.getResources().getString(R.string.enter24digitEid));
                        voterIdCardNameET.setHint(context.getResources().getString(R.string.enterNameInEid));
                        // voterIdCardNameET.setText(seccItem.getName());
                        voterIdCardNameET.setFilters(new InputFilter[]{new InputFilter.LengthFilter(24)});
                        if (seccItem != null && seccItem.getIdType() != null && seccItem.getIdType().equalsIgnoreCase(ENROLLMENT_ID + "")) {
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
                        //  nameLL.setVisibility(View.VISIBLE);
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
                        if (seccItem != null && seccItem.getIdType() != null && seccItem.getIdType().equalsIgnoreCase(VOTER_ID + "")) {
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
                        //  nameLL.setVisibility(View.VISIBLE);
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
                        if (seccItem != null && seccItem.getIdType() != null && seccItem.getIdType().equalsIgnoreCase(RASHAN_CARD + "")) {
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
                        // nameLL.setVisibility(View.VISIBLE);
                        govtIdPhotoLayout.setVisibility(View.VISIBLE);
                        voterIdCardNumberET.setText("");
                        voterIdCardNameET.setText("");
                        voterIdCardNumberET.setHint(context.getResources().getString(R.string.plzEnterNaregaNum));
                        voterIdCardNameET.setHint(context.getResources().getString(R.string.plzEnterNameAsNarega));
                        // voterIdCardNameET.setText(seccItem.getName());
                        voterIdCardNumberET.requestFocus();
                        AppUtility.showSoftInput(activity);
                        if (seccItem != null && seccItem.getIdType() != null && seccItem.getIdType().equalsIgnoreCase(NREGA + "")) {
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
                        // nameLL.setVisibility(View.VISIBLE);
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

                        if (seccItem != null && seccItem.getIdType() != null && seccItem.getIdType().equalsIgnoreCase(DRIVIG_LICENCE + "")) {
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
                        // nameLL.setVisibility(View.VISIBLE);
                        voterIdCardNumberET.setText("");
                        voterIdCardNameET.setText("");
                        voterIdCardNumberET.setText("");
                        voterIdCardNameET.setText("");
                        voterIdCardNumberET.requestFocus();
                        AppUtility.showSoftInput(activity);
                        voterIdCardNumberET.setHint(context.getResources().getString(R.string.plzEnterBirthCerfNum));
                        voterIdCardNameET.setHint(context.getResources().getString(R.string.plzEnterNameAsBirthCerf));
                        //voterIdCardNameET.setText(seccItem.getName());
                        if (seccItem != null && seccItem.getIdType() != null && seccItem.getIdType().equalsIgnoreCase(BIRTH_CERT + "")) {
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
                        // nameLL.setVisibility(View.VISIBLE);
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
                        if (seccItem != null && seccItem.getIdType() != null && seccItem.getIdType().equalsIgnoreCase(OTHER_CARD + "")) {
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
                        //  nameLL.setVisibility(View.VISIBLE);
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
                        if (seccItem != null && seccItem.getIdType() != null && seccItem.getIdType().equalsIgnoreCase(ID_NO_PHOTO + "")) {
                            voterIdImg = seccItem.getGovtIdPhoto();
                            if (seccItem.getIdNo() != null && !seccItem.getIdNo().equalsIgnoreCase("")) {
                                voterIdCardNameET.setText(seccItem.getNameAsId());
                                voterIdCardNumberET.setText(seccItem.getIdNo());
                            } else {
                                voterIdCardNameET.setText(seccItem.getName());
                            }
                        }
                        updateScreen(voterIdImg);
                    case AADHAR_ID:
                        voterIdImg = null;
//                        updateScreen(voterIdImg);
                        voterIdCardNameET.setEnabled(true);
                        voterIdLayout.setVisibility(View.VISIBLE);
                        govtIdPhotoLayout.setVisibility(View.VISIBLE);
                        //  preparedItem.setAadhaarSurveyedStat(item.getAadhaarSurveyedStat());
                        //  preparedItem.setAadhaarSurveyedStat(item.getAadhaarSurveyedStat());
//                        voterIdCaptureLayout.setVisibility(View.GONE);
                        voterIdCaptureLayout.setVisibility(View.VISIBLE);
                        //  nameLL.setVisibility(View.VISIBLE);
                        voterIdCardNumberET.setText("");
                        voterIdCardNameET.setText("");
//                        voterIdCardNumberET.setText("");
//                        voterIdCardNameET.setText("");
                        voterIdCardNameET.setEnabled(true);
                        selectedIdType = AADHAR_ID;
                        voterIdCardNumberET.requestFocus();
                        AppUtility.showSoftInput(activity);
                        voterIdCardNumberET.setHint("Enter Aadhar Number");
                        voterIdCardNameET.setHint("Please Enter Name As in Aadhar");
                        voterIdCardNumberET.setInputType(InputType.TYPE_CLASS_NUMBER);
                        // voterIdCardNumberET.setMaxEms(12);

                        //  voterIdCardNumberET.setInputType();
                        // voterIdCardNameET.setText(seccItem.getName());
                        if (seccItem != null && seccItem.getIdType() != null && seccItem.getIdType().equalsIgnoreCase(ID_NO_PHOTO + "")) {
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
        if (selectedMemItem != null && selectedMemItem.getSeccMemberItem() != null) {
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
        location = SearchLocation.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.DIST_VILLAGE_LOCATION, context));
        govtIdSP = (Spinner) findViewById(R.id.govtIdSP);
        prepareGovernmentIdSpinner();
        personalDetailItem = PersonalDetailItem.create(getIntent().getStringExtra("mobileNumber"));
        voterIdIV = (ImageView) findViewById(R.id.voterIdIV);
        voterIdBackIV = (ImageView) findViewById(R.id.voterIdBackIV);
        nameTV = (EditText) findViewById(R.id.nameET);

        yobET = (EditText) findViewById(R.id.yobET);
        pincodeET = (EditText) findViewById(R.id.pincodeET);

        emailET = (EditText) findViewById(R.id.emailET);
        subDistET = (EditText) findViewById(R.id.subDistET);
        //distET = (EditText)findViewById(R.id.distET);
        // vtcET = (EditText)findViewById(R.id.vtcET);
        poET = (EditText) findViewById(R.id.poET);
        //stateET = (EditText)findViewById(R.id.stateET);

        distTV = (AutoCompleteTextView) findViewById(R.id.distTV);

        vtcTV = (AutoCompleteTextView) findViewById(R.id.vtcTV);
        distTV.setThreshold(1);
        vtcTV.setThreshold(1);
        distTV.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (distTV.hasFocus())
                    autoSuggestDistrict(s.toString());
                // AppUtility.softKeyBoard(FingerprintResultActivity.this, 0);
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });
        vtcTV.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (vtcTV.hasFocus())
                    autoSuggestVillage(s.toString());
                // AppUtility.softKeyBoard(FingerprintResultActivity.this, 0);
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });

        if (location != null) {
            if (!location.getVilageName().equalsIgnoreCase("")) {
                vtcTV.setText("");
                vtcTV.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        vtcTV.showDropDown();
                        vtcTV.setText(location.getVilageName());
                        //kycVtc.setSelection(mACTextViewEmail.getText().length());
                    }
                }, 500);

               /* if(location.isVillageTrue()){
                    vtcTV.setChecked(true);
                }*/
            }
            if (!location.getDistName().equalsIgnoreCase("")) {
                distTV.setText("");

                distTV.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        distTV.showDropDown();
                        distTV.setText(location.getDistName());
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
        stateSP = (Spinner) findViewById(R.id.stateSP);

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
        final ArrayList<StateItem> stateList2 = new ArrayList<>();
        if (stateList != null) {
            for (StateItem item1 : stateList1) {
                if (item1.getStateCode().equalsIgnoreCase("16")) {
                    stateList.add("Haryana");
                    stateList2.add(item1);
                } else if (item1.getStateCode().equalsIgnoreCase("22")) {
                    stateList.add("Chattisgarh");
                    stateList2.add(item1);
                } else if (item1.getStateCode().equalsIgnoreCase("07")) {
                    stateList.add("Delhi");
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


        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, stateList);
        stateSP.setAdapter(adapter1);

        for (int i = 0; i < stateList2.size(); i++) {

            if (selectedStateItem.getStateCode().equalsIgnoreCase(stateList2.get(i).getStateCode())) {

                stateSP.setSelection(i);
                // stateSP.setTitle(item.getStateName());

                stateName = stateList.get(i);
                Log.d("state name11 :", stateName);

                break;
            }
        }

        genderRG = (RadioGroup) findViewById(R.id.genderRG);
        maleRB = (RadioButton) findViewById(R.id.maleRB);
        femaleRB = (RadioButton) findViewById(R.id.femaleRB);
        otherRB = (RadioButton) findViewById(R.id.otherRB);
        genderRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == maleRB.getId()) {
                    manualGenderSelection = "1";
                } else if (checkedId == femaleRB.getId()) {
                    manualGenderSelection = "2";
                } else if (checkedId == otherRB.getId()) {
                    manualGenderSelection = "3";
                }

            }

        });

        yobET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() > 0) {
                    if (charSequence.toString().startsWith("1") || charSequence.toString().startsWith("2")) {
                        // if (Integer.parseInt(charSequence.toString().substring(1)) < 2) {

                        yobET.setTextColor(context.getResources().getColor(R.color.black_shine));

                        if (yobET.getText().toString().length() == 4) {
                            yobET.setTextColor(context.getResources().getColor(R.color.green));
                            // isValidMobile = true;
                        } else {
                            //isValidMobile = false;
                        }
                    } else {
                        //isValidMobile = false;
                        yobET.setTextColor(context.getResources().getColor(R.color.red));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        String currentDate = DateTimeUtil.currentDate("dd MM yyyy");
        Log.d("current date", currentDate);
        currentYear = currentDate.substring(6, 10);
        Log.d("current year", currentYear);

        emailET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                emailET.setTextColor(Color.BLACK);
                emailValid = false;
                if (!isEmailValid(s.toString())) {
                    emailET.setTextColor(Color.RED);
                    emailValid = false;
                } else {
                    emailET.setTextColor(Color.GREEN);
                    emailValid = true;
                    //AppUtility.softKeyBoard(activity, 0);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        pincodeET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() > 0) {
                    if (!charSequence.toString().startsWith("0")) {
                        // if (Integer.parseInt(charSequence.toString().substring(1)) < 2) {

                        pincodeET.setTextColor(context.getResources().getColor(R.color.black_shine));

                        if (pincodeET.getText().toString().length() == 6) {
                            pincodeET.setTextColor(context.getResources().getColor(R.color.green));
                            // isValidMobile = true;
                        } else {
                            //isValidMobile = false;
                        }
                    } else {
                        //isValidMobile = false;
                        pincodeET.setTextColor(context.getResources().getColor(R.color.red));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //nameLL= (LinearLayout) findViewById(R.id.nameBenefLL);
        //nameLL.setVisibility(View.GONE);
        capturePhotoBT = (Button) findViewById(R.id.capturePhotoBT);
        photoIV = (ImageView) findViewById(R.id.photoIV);
        photoLayout = (LinearLayout) findViewById(R.id.photoLayout);
        photoLayout.setVisibility(View.GONE);
        rashanCardIV = (ImageView) findViewById(R.id.rationCardIV);
        backIV = (ImageView) findViewById(R.id.back);
        rationCardLayout = (LinearLayout) findViewById(R.id.rationCardLayout);
        voterIdLayout = (LinearLayout) findViewById(R.id.voterIdLayout);
        voterIdCaptureLayout = (RelativeLayout) findViewById(R.id.voterIdCaptureLayout);
        captureVoterIdBT = (Button) findViewById(R.id.captureVoterIdBT);
        captureVoterIdBackBT = (Button) findViewById(R.id.captureVoterIdBackBT);
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
        capturePhotoBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
            }
        });
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

        voterIdIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(voterIdImg!=null && !voterIdImg.equalsIgnoreCase("")){
                    PersonalDetailItem personalDetailItem= new PersonalDetailItem();
                    personalDetailItem.setIdPhoto(voterIdImg);
                    Intent intent = new Intent(context, ViewDocImageActivity.class);
                    ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,"GovtDocImage",personalDetailItem.getIdPhoto(),context);
                    //  intent.putExtra("DocImage",personalDetailItem.getIdPhoto());
                    intent.putExtra("ScreenName","GovernmentIdActivity");
                    startActivity(intent);
                }else {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzCaptureGovId));
                    return;
                }
            }
        });

        voterIdBackIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(voterIdBackImg!=null && !voterIdBackImg.equalsIgnoreCase("")){
                    PersonalDetailItem personalDetailItem= new PersonalDetailItem();
                    personalDetailItem.setIdPhoto1(voterIdBackImg);
                    Intent intent = new Intent(context, ViewDocImageActivity.class);
                    ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,"GovtDocImage",personalDetailItem.getIdPhoto1(),context);
                    //  intent.putExtra("DocImage",personalDetailItem.getIdPhoto());
                    intent.putExtra("ScreenName","GovernmentIdActivity");

                    startActivity(intent);
                }else {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzCaptureGovId));
                    return;
                }
            }
        });

        voterIdSubmitBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String voterIdNumber = voterIdCardNumberET.getText().toString();
                String voterIdName = voterIdCardNameET.getText().toString();
                String name = nameTV.getText().toString();
                String yob = yobET.getText().toString();
                String pincode = pincodeET.getText().toString();
                String subDist = subDistET.getText().toString();
               // String vtc = vtcTV.getText().toString();
                String po = poET.getText().toString();
                String email = emailET.getText().toString();
              //  String dist = distTV.getText().toString();
                // String state = stateET.getText().toString();
                if (item.statusCode == 0) {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzSelectGovId));
                    return;
                }
                if (item.statusCode == 8) {
                    alertForNoGovIdValidateLater();
                } else {
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


                    if (name.equalsIgnoreCase("")) {
                        CustomAlert.alertWithOk(context, "Please enter name");
                        return;
                    }

                    if (!isCheckFirstTwoChar(voterIdNumber)) {
                        voterIdCardNumberET.setText("");
                        CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterValidGovId));
                        return;
                    }

                    if (item.statusCode == 13) {
                        Log.d("TAG", "Aaadhaar Length : " + voterIdCardNameET.getText().toString().length());
                        if (voterIdCardNumberET.getText().toString().length() != 12) {
                            CustomAlert.alertWithOk(context, "Please enter valid Aadhaar Number");
                            return;
                        }

                    }
                    /*if (benefImage != null && benefImage.equalsIgnoreCase("")) {
                        CustomAlert.alertWithOk(context, "Please capture beneficiary image");
                        return;
                    }*/

                    if (benefImage != null && benefImage.equalsIgnoreCase("")) {
                        CustomAlert.alertWithOk(context, "Please capture beneficiary image");
                        return;
                    }

                    if (benefImage == null || benefImage.equalsIgnoreCase("")) {
                        CustomAlert.alertWithOk(context, "Please capture beneficiary image");
                        return;
                    }

                    if (yob.trim().equalsIgnoreCase("")) {
                        CustomAlert.alertWithOk(context, "Please enter year of birth");
                        return;
                    }

                    if (yob != null && !yob.equalsIgnoreCase("")) {
                        int yearRange = Integer.parseInt(currentYear) - 100;

                        if (yob.equalsIgnoreCase(currentYear) || Integer.parseInt(yob) < yearRange) {
                            CustomAlert.alertWithOk(context, "Please enter valid year of birth");
                            return;
                        }

                    }


                    if (manualGenderSelection != null && manualGenderSelection.equalsIgnoreCase("")) {
                        CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterGenderGovt));
                        return;
                    }

                    if (pincode.equalsIgnoreCase("")) {
                        CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterPincodeGovt));
                        return;
                    }

                    /*if (subDist.equalsIgnoreCase("")) {
                        CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterSubDist));
                        return;
                    }*/

                  /*  if (vtc.equalsIgnoreCase("")) {
                        CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterVTC));
                        return;
                    }*/

                  /*  if (po.equalsIgnoreCase("")) {
                        CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterPo));
                        return;
                    }*/

                   /* if (email.equalsIgnoreCase("")) {
                        CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterEmail));
                        return;
                    }

                    if(!emailValid){
                        CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterEmailValid));
                        return;
                    }*/

                 /*   if (state.equalsIgnoreCase("")) {
                        CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterState));
                        return;
                    }*/

                   /* if (dist.equalsIgnoreCase("")) {
                        CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterDist));
                        return;
                    }*/


                    if (personalDetailItem != null) {
                        /*if (mobileNumber != null) {
                            personalDetailItem.setMobileNo(mobileNumber);
                        }*/
                        personalDetailItem.setBenefPhoto(benefImage);
                        personalDetailItem.setName(name);
                        personalDetailItem.setIdPhoto(voterIdImg);
                        personalDetailItem.setIdPhoto1(voterIdBackImg);
                        personalDetailItem.setIsAadhar("N");
                        personalDetailItem.setGovtIdType(item.status);
                        personalDetailItem.setIdName(item.statusCode + "");
                        personalDetailItem.setFlowStatus(AppConstant.GOVT_STATUS);
                        personalDetailItem.setGovtIdNo(voterIdCardNumberET.getText().toString());

                        personalDetailItem.setYob(yob);
                       // personalDetailItem.setState(stateName);
                        personalDetailItem.setGender(manualGenderSelection);
                        personalDetailItem.setPinCode(pincode);
                        personalDetailItem.setSubDistrictBen(subDist);
                      //  personalDetailItem.setVtcBen(vtc);
                        personalDetailItem.setPostOfficeBen(po);
                        personalDetailItem.setEmailBen(email);
                      //  personalDetailItem.setDistrict(dist);
                     //   location.setVilageName(vtc);
                     //   location.setDistName(dist);
                    } else {

                        personalDetailItem = new PersonalDetailItem();
                        /*if (mobileNumber != null) {
                            personalDetailItem.setMobileNo(mobileNumber);
                        }*/
                        personalDetailItem.setBenefPhoto(benefImage);
                        personalDetailItem.setName(name);

                        personalDetailItem.setIdPhoto(voterIdImg);
                        personalDetailItem.setIdPhoto1(voterIdBackImg);

                        personalDetailItem.setIsAadhar("N");
                        personalDetailItem.setGovtIdType(item.status);
                        personalDetailItem.setIdName(item.statusCode + "");
                        personalDetailItem.setFlowStatus(AppConstant.GOVT_STATUS);
                        personalDetailItem.setGovtIdNo(voterIdCardNumberET.getText().toString());

                        personalDetailItem.setYob(yob);
                     //   personalDetailItem.setState(stateName);
                        personalDetailItem.setGender(manualGenderSelection);
                        personalDetailItem.setPinCode(pincode);
                        personalDetailItem.setSubDistrictBen(subDist);
                     //   personalDetailItem.setVtcBen(vtc);
                        personalDetailItem.setPostOfficeBen(po);
                        personalDetailItem.setEmailBen(email);
                     //   personalDetailItem.setDistrict(dist);
                    //    location.setVilageName(vtc);
                    //    location.setDistName(dist);
                    }
                    ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_NAME, "GOVT_ID_DATA", personalDetailItem.serialize(), context);
                    ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.DIST_VILLAGE_LOCATION, location.serialize(), context);

                    activity.finish();
                  /*  if (voterIdNumber.equalsIgnoreCase("")) {
                        CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterGovId));
                        return;
                    }
                   *//* if (voterIdName.equalsIgnoreCase("")) {
                        CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterNameAsGovId));
                        return;
                    }*//*
            *//*if(consent.equalsIgnoreCase("N")){
                    CustomAlert.alertWithOk(context,"Please tick Name in SECC and name in govt. id are of same person");
                    return;
                }*//*
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

                    if (name.equalsIgnoreCase("")) {
                        CustomAlert.alertWithOk(context, "Please enter name");
                        return;
                    }

                    if (!isCheckFirstTwoChar(voterIdNumber)) {
                        voterIdCardNumberET.setText("");
                        CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterValidGovId));
                        return;
                    }


                    if (benefImage == null || benefImage.equalsIgnoreCase("")) {
                        CustomAlert.alertWithOk(context, "Please capture beneficiary image");
                        return;
                    }

                    if (yob.trim().equalsIgnoreCase("")) {
                        CustomAlert.alertWithOk(context, "Please enter year of birth");
                        return;
                    }

                    if (yob != null && !yob.equalsIgnoreCase("")) {
                        int yearRange = Integer.parseInt(currentYear) - 100;

                        if (yob.equalsIgnoreCase(currentYear) || Integer.parseInt(yob) < yearRange) {
                            CustomAlert.alertWithOk(context, "Please enter valid year of birth");
                            return;
                        }

                    }


                    if (manualGenderSelection != null && manualGenderSelection.equalsIgnoreCase("")) {
                        CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterGenderGovt));
                        return;
                    }

                    if (pincode.equalsIgnoreCase("")) {
                        CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterPincodeGovt));
                        return;
                    }

                   *//* if (subDist.equalsIgnoreCase("")) {
                        CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterSubDist));
                        return;
                    }*//*

                    if (vtc.equalsIgnoreCase("")) {
                        CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterVTC));
                        return;
                    }

                   *//* if (po.equalsIgnoreCase("")) {
                        CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterPo));
                        return;
                    }
*//*
                   *//* if (email.equalsIgnoreCase("")) {
                        CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterEmail));
                        return;
                    }

                    if(!emailValid){
                        CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterEmailValid));
                        return;
                    }*//*

                  *//*  if (state.equalsIgnoreCase("")) {
                        CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterState));
                        return;
                    }*//*

                    if (dist.equalsIgnoreCase("")) {
                        CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterDist));
                        return;
                    }

                    if (personalDetailItem != null) {
                        if (mobileNumber != null) {
                            personalDetailItem.setMobileNo(mobileNumber);
                        }
                        personalDetailItem.setBenefPhoto(benefImage);
                        personalDetailItem.setName(name);
                        personalDetailItem.setIdPhoto(voterIdImg);
                        personalDetailItem.setIsAadhar("N");
                        personalDetailItem.setGovtIdType(item.status);
                        personalDetailItem.setIdName(item.statusCode + "");
                        personalDetailItem.setFlowStatus(AppConstant.GOVT_STATUS);
                        personalDetailItem.setGovtIdNo(voterIdCardNumberET.getText().toString());

                        personalDetailItem.setYob(yob);
                        personalDetailItem.setState(stateName);
                        personalDetailItem.setGender(manualGenderSelection);
                        personalDetailItem.setPinCode(pincode);
                        personalDetailItem.setSubDistrictBen(subDist);
                        personalDetailItem.setVtcBen(vtc);
                        personalDetailItem.setPostOfficeBen(po);
                        personalDetailItem.setEmailBen(email);
                        personalDetailItem.setDistrict(dist);
                        location.setVilageName(vtc);
                        location.setDistName(dist);

                    } else {


                        personalDetailItem = new PersonalDetailItem();

                        if (mobileNumber != null) {
                            personalDetailItem.setMobileNo(mobileNumber);
                        }
                        personalDetailItem.setBenefPhoto(benefImage);
                        personalDetailItem.setName(name);
                        personalDetailItem.setIdPhoto(voterIdImg);
                        personalDetailItem.setIsAadhar("N");
                        personalDetailItem.setGovtIdType(item.status);
                        personalDetailItem.setIdName(item.statusCode + "");
                        personalDetailItem.setFlowStatus(AppConstant.GOVT_STATUS);
                        personalDetailItem.setGovtIdNo(voterIdCardNumberET.getText().toString());

                        personalDetailItem.setYob(yob);
                        personalDetailItem.setState(stateName);
                        personalDetailItem.setGender(manualGenderSelection);
                        personalDetailItem.setPinCode(pincode);
                        personalDetailItem.setSubDistrictBen(subDist);
                        personalDetailItem.setVtcBen(vtc);
                        personalDetailItem.setPostOfficeBen(po);
                        personalDetailItem.setEmailBen(email);
                        personalDetailItem.setDistrict(dist);
                        location.setVilageName(vtc);
                        location.setDistName(dist);
                    }
                    ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_NAME, "GOVT_ID_DATA", personalDetailItem.serialize(), context);
                    ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.DIST_VILLAGE_LOCATION, location.serialize(), context);


                *//*    PersonalDetailItem personalDetailItem = new PersonalDetailItem();
                    personalDetailItem.setBenefPhoto(benefImage);
                    personalDetailItem.setName(name);
                    if (mobileNumber != null) {
                        personalDetailItem.setMobileNo(mobileNumber);
                    }
                    personalDetailItem.setIdPhoto(voterIdImg);
                    personalDetailItem.setGovtIdType(item.status);
                    personalDetailItem.setGovtIdNo(voterIdCardNumberET.getText().toString());


                    personalDetailItem.setYob(yob);
                    personalDetailItem.setState(state);
                    personalDetailItem.setGender(manualGenderSelection);
                    personalDetailItem.setPinCode(pincode);
                    personalDetailItem.setSubDistrictBen(subDist);
                    personalDetailItem.setVtcBen(vtc);
                    personalDetailItem.setPostOfficeBen(po);
                    personalDetailItem.setEmailBen(email);
                    personalDetailItem.setDistrict(dist);
                    ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_NAME, "GOVT_ID_DATA", personalDetailItem.serialize(), context);
*//*
                    activity.finish();*/
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

        captureVoterIdBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtility.openCamera(activity, AppConstant.BACK_CAMREA_OPEN, "ForStore", "DummyImagePreviewClass");

           /*     AppUtility.capturingType = AppConstant.capturingModeGovId;
              *//*  Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
              //  Uri imageUri = Uri.fromFile(new File(DatabaseHelpers.DELETE_FOLDER_PATH,context.getString(R.string.squarecamera__app_name)+"/govtIdPhoto" +".jpg"));
                Uri imageUri = Uri.fromFile(new File(DatabaseHelpers.DELETE_FOLDER_PATH,context.getString(R.string.squarecamera__app_name)+"/govtIdPhoto/IMG_12345.jpg"));
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(cameraIntent, 2);*//*
                File mediaStorageDir = new File(
                        DatabaseHelpers.DELETE_FOLDER_PATH,
                        context.getString(R.string.squarecamera__app_name) + "/photoCapture"
                );

                if (mediaStorageDir.exists()) {
                    deleteDir(mediaStorageDir);
                }
                Intent startCustomCameraIntent = new Intent(context, CameraActivity.class);
                startActivityForResult(startCustomCameraIntent, CAMERA_PIC_REQUEST);*/

            }
        });


        captureVoterIdBackBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtility.openCameraForGovtId(activity, AppConstant.BACK_CAMREA_OPEN, "ForStore", "DummyImagePreviewClass");

            /*    AppUtility.capturingType = AppConstant.capturingModeGovId;
              *//*  Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
              //  Uri imageUri = Uri.fromFile(new File(DatabaseHelpers.DELETE_FOLDER_PATH,context.getString(R.string.squarecamera__app_name)+"/govtIdPhoto" +".jpg"));
                Uri imageUri = Uri.fromFile(new File(DatabaseHelpers.DELETE_FOLDER_PATH,context.getString(R.string.squarecamera__app_name)+"/govtIdPhoto/IMG_12345.jpg"));
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(cameraIntent, 2);*//*
                File mediaStorageDir = new File(
                        DatabaseHelpers.DELETE_FOLDER_PATH,
                        context.getString(R.string.squarecamera__app_name) + "/photoCapture"
                );

                if (mediaStorageDir.exists()) {
                    deleteDir(mediaStorageDir);
                }
                Intent startCustomCameraIntent = new Intent(context, CameraActivity.class);
                startActivityForResult(startCustomCameraIntent, CAMERA_PIC_BACK_REQUEST);*/

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
                        photoLayout.setVisibility(View.GONE);

                        break;

                    case 0:
                        voterIdLayout.setVisibility(View.GONE);
                        govtIdPhotoLayout.setVisibility(View.GONE);
                        photoLayout.setVisibility(View.GONE);

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
                        photoLayout.setVisibility(View.VISIBLE);

                        voterIdLayout.setVisibility(View.VISIBLE);
                        voterIdCaptureLayout.setVisibility(View.VISIBLE);
                        govtIdPhotoLayout.setVisibility(View.VISIBLE);
                        photoLayout.setVisibility(View.VISIBLE);
                        /*voterIdCardNumberET.setText("");
                        voterIdCardNameET.setText("");*/
                        //updateScreen(voterIdImg);
                        voterIdCardNumberET.setHint(context.getResources().getString(R.string.enter24digitEid));
                        voterIdCardNameET.setHint(context.getResources().getString(R.string.enterNameInEid));
                        voterIdCardNameET.setEnabled(true);
                     //   voterIdCardNameET.setText(seccItem.getName());
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
                        voterIdLayout.setVisibility(View.VISIBLE);
                        govtIdPhotoLayout.setVisibility(View.VISIBLE);
                        photoLayout.setVisibility(View.VISIBLE);
                        voterIdCaptureLayout.setVisibility(View.VISIBLE);
                        voterIdCardNumberET.requestFocus();
                        voterIdCardNumberET.setText("");
                        voterIdCardNameET.setText("");
//                        updateScreen(voterIdImg);
                        voterIdCardNumberET.setHint(context.getResources().getString(R.string.plzEnterVoterIdNum));
                        voterIdCardNameET.setHint(context.getResources().getString(R.string.plzEnterNameAsVoterId));

//                        voterIdCardNameET.setText(seccItem.getName());
                        voterIdCardNameET.setEnabled(true);

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
                        voterIdLayout.setVisibility(View.VISIBLE);
                        govtIdPhotoLayout.setVisibility(View.VISIBLE);
                        photoLayout.setVisibility(View.VISIBLE);
                        voterIdCaptureLayout.setVisibility(View.VISIBLE);
//                        updateScreen(voterIdImg);
                        voterIdCardNumberET.requestFocus();
                        AppUtility.showSoftInput(activity);
                        voterIdCardNumberET.setText("");
                        voterIdCardNameET.setText("");
                        voterIdCardNumberET.setHint(context.getResources().getString(R.string.plzEnterRationCardNum));
                        voterIdCardNameET.setHint(context.getResources().getString(R.string.plzEnterNameAsRationCard));
                     //   voterIdCardNameET.setText(seccItem.getName());
                        voterIdCardNameET.setEnabled(true);

                       /* rationCardLayout.setVisibility(View.VISIBLE);
                        enrollmentLayout.setVisibility(View.GONE);*/
                        //aadhaarStatus="3";
                        if (seccItem!=null &&seccItem.getIdType() != null && seccItem.getIdType().equalsIgnoreCase(RASHAN_CARD + "")) {
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
                        photoLayout.setVisibility(View.VISIBLE);
                        voterIdCaptureLayout.setVisibility(View.VISIBLE);
                        voterIdCardNumberET.setText("");
                        voterIdCardNameET.setText("");
                        voterIdCardNumberET.setHint(context.getResources().getString(R.string.plzEnterNaregaNum));
                        voterIdCardNameET.setHint(context.getResources().getString(R.string.plzEnterNameAsNarega));
//                        voterIdCardNameET.setText(seccItem.getName());
                        voterIdCardNameET.setEnabled(true);

                        voterIdCardNumberET.requestFocus();
                        AppUtility.showSoftInput(activity);
                        if (seccItem!=null &&seccItem.getIdType() != null && seccItem.getIdType().equalsIgnoreCase(NREGA + "")) {
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
                        photoLayout.setVisibility(View.VISIBLE);
                        voterIdCaptureLayout.setVisibility(View.VISIBLE);
                        voterIdCardNumberET.requestFocus();
                        AppUtility.showSoftInput(activity);
                        /*voterIdCardNumberET.setText("");

                        voterIdCardNameET.setText("");*/
                        voterIdCardNumberET.setText("");
                        voterIdCardNameET.setText("");
                        voterIdCardNumberET.setHint(context.getResources().getString(R.string.plzEnterDrivingNum));
                        voterIdCardNameET.setHint(context.getResources().getString(R.string.plzEnterNameAsDriving));
                      //  voterIdCardNameET.setText(seccItem.getName());
                        voterIdCardNameET.setEnabled(true);

                        if (seccItem!=null &&seccItem.getIdType().equalsIgnoreCase(DRIVIG_LICENCE + "")) {
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
                        photoLayout.setVisibility(View.VISIBLE);
                        voterIdCaptureLayout.setVisibility(View.VISIBLE);
                        voterIdCardNumberET.setText("");
                        voterIdCardNameET.setText("");
                        voterIdCardNumberET.setText("");
                        voterIdCardNameET.setText("");
                        voterIdCardNumberET.requestFocus();
                        AppUtility.showSoftInput(activity);
                        voterIdCardNumberET.setHint(context.getResources().getString(R.string.plzEnterBirthCerfNum));
                        voterIdCardNameET.setHint(context.getResources().getString(R.string.plzEnterNameAsBirthCerf));
                     //   voterIdCardNameET.setText(seccItem.getName());
                        voterIdCardNameET.setEnabled(true);

                        if (seccItem!=null &&seccItem.getIdType() != null && seccItem.getIdType().equalsIgnoreCase(BIRTH_CERT + "")) {
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
                        photoLayout.setVisibility(View.VISIBLE);
                        voterIdCardNumberET.setText("");
                        voterIdCardNameET.setText("");
                        voterIdCardNumberET.setText("");
                        voterIdCardNameET.setText("");
                        voterIdCardNumberET.requestFocus();
                        AppUtility.showSoftInput(activity);
                        voterIdCardNumberET.setHint(context.getResources().getString(R.string.plzEnterId));
                        voterIdCardNameET.setHint(context.getResources().getString(R.string.plzEnterNameAsId));
                      //  voterIdCardNameET.setText(seccItem.getName());
                        voterIdCardNameET.setEnabled(true);

                        if (seccItem!=null &&seccItem.getIdType() != null && seccItem.getIdType().equalsIgnoreCase(OTHER_CARD + "")) {
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
                        photoLayout.setVisibility(View.VISIBLE);
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
                       // voterIdCardNameET.setText(seccItem.getName());
                        voterIdCardNameET.setEnabled(true);

                        if (seccItem!=null &&seccItem.getIdType() != null && seccItem.getIdType().equalsIgnoreCase(ID_NO_PHOTO + "")) {
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
                    case AADHAR_ID:
                        voterIdImg = null;
//                        updateScreen(voterIdImg);
                        voterIdCardNameET.setEnabled(true);
                        voterIdLayout.setVisibility(View.VISIBLE);
                        govtIdPhotoLayout.setVisibility(View.VISIBLE);
                        //  preparedItem.setAadhaarSurveyedStat(item.getAadhaarSurveyedStat());
                        //  preparedItem.setAadhaarSurveyedStat(item.getAadhaarSurveyedStat());
//                        voterIdCaptureLayout.setVisibility(View.GONE);
                        voterIdCaptureLayout.setVisibility(View.VISIBLE);
                        //  nameLL.setVisibility(View.VISIBLE);
                        voterIdCardNumberET.setText("");

                        voterIdCardNameET.setText("");
//                        voterIdCardNumberET.setText("");
//                        voterIdCardNameET.setText("");
                        voterIdCardNameET.setEnabled(true);
                        selectedIdType = AADHAR_ID;
                        voterIdCardNumberET.requestFocus();
                        AppUtility.showSoftInput(activity);
                        voterIdCardNumberET.setHint("Enter Aadhar Number");
                        voterIdCardNameET.setHint("Please Enter Name As in Aadhar");
                        // voterIdCardNameET.setText(seccItem.getName());
                        if (seccItem!=null &&seccItem != null && seccItem.getIdType() != null && seccItem.getIdType().equalsIgnoreCase(ID_NO_PHOTO + "")) {
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
            if (requestCode == AppConstant.REQ_CAMERA && resultCode == RESULT_OK) {
                final Intent intent = data;//new Intent();
                String path = intent.getStringExtra("response");
                Uri uri = Uri.fromFile(new File(path));
                if (uri == null) {
                    Log.d("uri", "null");
                } else {
                    bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    File mediaFile = null;
                    if (bitmap != null) {
                        byte[] imageBytes = ImageUtil.bitmapToByteArray(rotateImage(bitmap, 270));

                        File mediaStorageDir = new File(context.getExternalFilesDir(Environment.DIRECTORY_DCIM), "");
                        mediaFile = new File(mediaStorageDir.getPath() + File.separator + purpose + ".jpg");
                        if (mediaFile != null) {
                            try {
                                FileOutputStream fos = new FileOutputStream(mediaFile);
                                fos.write(imageBytes);
                                fos.close();
                            } catch (FileNotFoundException e) {
                                // Crashlytics.log(1, getClass().getName(), e.getMessage());
                                // Crashlytics.logException(e);
                            } catch (IOException e) {
                                //  Crashlytics.log(1, getClass().getName(), e.getMessage());
                                //  Crashlytics.logException(e);
                            }
                        }
                    }
                }

                voterIdImg = AppUtility.converBitmapToBase64(bitmap);
                if(voterIdImg!=null) {
                    voterIdIV.setImageBitmap(bitmap);
                }else {
                    voterIdIV.setImageBitmap(null);
                }
                    //image.setImageBitmap(bitmap);
            }

            if (requestCode == AppConstant.BACK_REQ_CAMERA && resultCode == RESULT_OK) {
                final Intent intent = data;//new Intent();
                String path = intent.getStringExtra("response");
                Uri uri = Uri.fromFile(new File(path));
                if (uri == null) {
                    Log.d("uri", "null");
                } else {
                    bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    File mediaFile = null;
                    if (bitmap != null) {
                        byte[] imageBytes = ImageUtil.bitmapToByteArray(rotateImage(bitmap, 270));

                        File mediaStorageDir = new File(context.getExternalFilesDir(Environment.DIRECTORY_DCIM), "");
                        mediaFile = new File(mediaStorageDir.getPath() + File.separator + purpose + ".jpg");
                        if (mediaFile != null) {
                            try {
                                FileOutputStream fos = new FileOutputStream(mediaFile);
                                fos.write(imageBytes);
                                fos.close();
                            } catch (FileNotFoundException e) {
                                // Crashlytics.log(1, getClass().getName(), e.getMessage());
                                // Crashlytics.logException(e);
                            } catch (IOException e) {
                                //  Crashlytics.log(1, getClass().getName(), e.getMessage());
                                //  Crashlytics.logException(e);
                            }
                        }
                    }
                }

                voterIdBackImg = AppUtility.converBitmapToBase64(bitmap);
                if(voterIdBackImg!=null) {
                    voterIdBackIV.setImageBitmap(bitmap);
                }else {
                    voterIdBackIV.setImageBitmap(null);
                }
                //image.setImageBitmap(bitmap);
            }
         /*   if (requestCode == CAMERA_PIC_REQUEST) {
                Uri imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),"govtIdPhoto" +".jpg"));
                try {
                    captureImageBM = MediaStore.Images.Media.getBitmap(this.getContentResolver(),imageUri); //(Bitmap)imageUri;//data.getExtras().get("data");
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
            } else if (requestCode == RASHAN_CARD_REQUEST) {
                Log.d("Govt id capture","rashan card calling");
                captureImageBM = (Bitmap) data.getExtras().get("data");
                rashanCardImg=AppUtility.convertBitmapToString(captureImageBM);
                rashanCardIV.setImageBitmap(AppUtility.convertStringToBitmap(rashanCardImg));
            }
        }else{

        }
            if (requestCode == CAMERA_PIC_BACK_REQUEST) {
                Uri imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),"govtIdPhoto" +".jpg"));
                try {
                    captureImageBM = MediaStore.Images.Media.getBitmap(this.getContentResolver(),imageUri); //(Bitmap)imageUri;//data.getExtras().get("data");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Uri fileUri = Uri.fromFile(new File(DatabaseHelpers.DELETE_FOLDER_PATH,
                        context.getString(R.string.squarecamera__app_name) + "/photoCapture/IMG_12345.jpg"));
                Uri compressedUri = Uri.fromFile(new File(CommonUtilsImageCompression.compressImage(fileUri.getPath(), context, "/photoCapture")));
                //  captureImageBM=(Bitmap)data.getExtras().get("data");
                try {
                    captureImageBackBM = MediaStore.Images.Media.getBitmap(this.getContentResolver(), compressedUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // Log.d(TAG," Bitmap Size : "+image.getAllocationByteCount());
                voterIdBackImg = AppUtility.convertBitmapToString(captureImageBackBM);
                updateBackImageScreen(voterIdBackImg);
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


        if (requestCode == PIC_REQUEST) {
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

    void deleteDirPic(File file) {

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
    private void updateBackImageScreen(String idImage) {
        try {
            if (idImage != null) {
                voterIdBackIV.setImageBitmap(AppUtility.convertStringToBitmap(idImage));
            } else {
//                if (seccItem.getGovtIdPhoto() != null && !seccItem.getGovtIdPhoto().equalsIgnoreCase("")) {
//
//                } else {
                voterIdBackIV.setImageBitmap(null);
//                }
            }
        } catch (Exception e) {

        }
    }
    private void prepareGovernmentIdSpinner() {
        govtIdStatusList = AppUtility.prepareGovernmentIdSpinnerForNoAadhar();
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
        selectedStateItem = StateItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SELECTED_STATE_SEARCH, context));
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
        GovtDetailsModel govtDetailsModel = new GovtDetailsModel();
        govtDetailsModel.setImage(voterIdImg);
        govtDetailsModel.setIdNumber(voterIdCardNumberET.getText().toString());
        govtDetailsModel.setGovtIdType(item.status);
        GovtIdDataFragment fragment = new GovtIdDataFragment();
        fragment.setGovtDetailsModel(govtDetailsModel);
        fragmentTransection = fragmentManager.beginTransaction();
        fragmentTransection.add(R.id.fragContainer, fragment);
        fragmentTransection.commitAllowingStateLoss();
    }

    private void openCamera() {
        AppUtility.capturingType = AppConstant.capturingModePhoto;
        File mediaStorageDir = new File(
                DatabaseHelpers.DELETE_FOLDER_PATH,
                context.getString(R.string.squarecamera__app_name) + "/photoCapture"
        );

        if (mediaStorageDir.exists()) {
            deleteDirPic(mediaStorageDir);
        }

        Intent startCustomCameraIntent = new Intent(context, CameraActivity.class);
        startActivityForResult(startCustomCameraIntent, PIC_REQUEST);

    /*    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);*/
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
                    .into(photoIV, new Callback() {
                        @Override
                        public void onSuccess() {
                            memberPhoto = ((BitmapDrawable) photoIV.getDrawable()).getBitmap();
                            benefImage = AppUtility.convertBitmapToString(memberPhoto);
                            photoIV.setImageBitmap(memberPhoto);
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


    private void autoSuggestDistrict(final String text) {

        TaskListener taskListener = new TaskListener() {
            @Override
            public void execute() {
                AutoSuggestRequestItem request = new AutoSuggestRequestItem();

                request.setDistrictName(text.toLowerCase());
                if (stateName != null && !stateName.equalsIgnoreCase("")) {
                    request.setStateName(stateName);
                }
                try {
                    //String request = familyListRequestModel.serialize();
                    HashMap<String, String> response = CustomHttp.httpPostWithTokken(AppConstant.AUTO_SUGGEST, request.serialize(),AppConstant.AUTHORIZATION,verifierLoginResponse.getAuthToken());
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
                        if (districtResponse.getResult() != null && districtResponse.getResult().getResult() != null) {
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
                            distTV.setAdapter(adapter);

                            distTV.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                @Override
                                public void onItemClick(AdapterView<?> parent, View view,
                                                        int position, long id) {
                                    String selected = tempDist.get(position);
                                    vtcTV.setText("");
                                    distTV.setText(selected);
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
        /*
        String[] COUNTRIES = new String[] {
                "Belgium", "Belance", "Betaly", "Bermany", "Beain"};
        temp=new ArrayList<>();
       */
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
                String district = distTV.getText().toString().trim();
                if (district != null && !district.equalsIgnoreCase("")) {
                    request.setDistrictName(district);
                }
                try {
                    //String request = familyListRequestModel.serialize();
                    HashMap<String, String> response = CustomHttp.httpPostWithTokken(AppConstant.AUTO_SUGGEST, request.serialize(),AppConstant.AUTHORIZATION,verifierLoginResponse.getAuthToken());
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
                    if (villageResponse.isStatus()) {
                        if (villageResponse.getResult() != null && villageResponse.getResult().getResult() != null) {
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
                            vtcTV.setAdapter(adapter);

                            vtcTV.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                @Override
                                public void onItemClick(AdapterView<?> parent, View view,
                                                        int position, long id) {
                                    String selected = temp.get(position);
                                    vtcTV.setText(selected);
                                    distTV.setText(distTemp.get(position));
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

}
