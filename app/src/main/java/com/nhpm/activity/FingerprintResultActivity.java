package com.nhpm.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.customComponent.CustomAlert;
import com.customComponent.CustomAsyncTask;
import com.customComponent.TaskListener;
import com.customComponent.utility.CustomHttp;
import com.customComponent.utility.DateTimeUtil;
import com.customComponent.utility.ProjectPrefrence;
import com.nhpm.LocalDataBase.dto.SeccDatabase;
import com.nhpm.Models.SearchLocation;
import com.nhpm.Models.request.AadharResultRequestModel;
import com.nhpm.Models.request.AutoSuggestRequestItem;
import com.nhpm.Models.request.FamilyListRequestModel;
import com.nhpm.Models.request.LogRequestItem;
import com.nhpm.Models.response.FamilyListResponseItem;
import com.nhpm.Models.response.VillageResponseItem;
import com.nhpm.Models.response.master.StateItem;
import com.nhpm.Models.response.verifier.AadhaarResponseItem;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;
import com.nhpm.Utility.AppUtility;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Created by Dell3 on 04-05-2018.
 */

public class FingerprintResultActivity extends BaseActivity {
    private String LOCATION_TAG="villageTag";
    private String DIST_TAG="distTag";
    private CustomAsyncTask customAsyncTask;
    private Context context;
    private AadhaarResponseItem aadhaarKycResponse;
    private EditText kycSubDist, kycName, kycDob, kycPincode, kycGender,
            kycEmail, kycPhone, kycCareOf, kycAddr, kycTs, kycTxn, kycRespTs,
            kycErrorEditText, kycState, kycAge, kycSpouse, kycMother, kycFather;
    private AutoCompleteTextView kycVtc, kycDist;
    private ImageView kycImageView;
    private long endTime;
    private long totalTime;
    private long startTime;
    private TextView headerTV;
    private ImageView backIV;
    private TextView kycGovtId;
    private Button updateKycButton, cancelButton;
    private LinearLayout nameLL, dobLL, genderLL, emailLL, phoneLL, coLL,
            addressLL, pincodeLL, timestampLL, txnLL, respTimeLL, stateLL, distLL, subDistLL, vtcLL;
    private AadharResultRequestModel aadharResultRequestModel;
    private Spinner cardTypeSpinner;
    private CheckBox ageCheck, nameCheck, dobCheck, genderCheck, pincodeCheck, fatherCheck, motherCheck,
            spouseCheck, stateCheck, distCheck, vtcCheck,ruralCheck;
    private ArrayList<String> temp, distTemp;
    private ArrayList<String> tempDist;
    private VillageResponseItem villageResponse,districtResponse;
    private String screen;
    private StateItem selectedStateItem;
  //  private SearchableSpinner stateSP;
    private Spinner stateSP,ruralUrbanSP;
    private String stateName,ruralUrbanStatus="",ruralUrbanTag;
    private LogRequestItem logRequestItem;
    private SearchLocation location=new SearchLocation();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.bio_ekyc_layout);
        setupScreen();
    }

    private void setupScreen() {
        location=SearchLocation.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                LOCATION_TAG,context  ));
        aadharResultRequestModel = new AadharResultRequestModel();
        logRequestItem=LogRequestItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,AppConstant.LOG_REQUEST,context));
        headerTV = (TextView) findViewById(R.id.centertext);
        ageCheck = (CheckBox) findViewById(R.id.ageCheck);
        //stateSP = (SearchableSpinner) findViewById(R.id.stateSP);
        stateSP = (Spinner) findViewById(R.id.stateSP);
        ruralUrbanSP = (Spinner) findViewById(R.id.ruralUrbanSP);
        nameCheck = (CheckBox) findViewById(R.id.nameCheck);
        dobCheck = (CheckBox) findViewById(R.id.dobCheck);
        genderCheck = (CheckBox) findViewById(R.id.genderCheck);
        pincodeCheck = (CheckBox) findViewById(R.id.pincodeCheck);
        fatherCheck = (CheckBox) findViewById(R.id.fatherCheck);
        motherCheck = (CheckBox) findViewById(R.id.motherCheck);
        spouseCheck = (CheckBox) findViewById(R.id.spouseCheck);
        stateCheck = (CheckBox) findViewById(R.id.stateCheck);
        distCheck = (CheckBox) findViewById(R.id.distCheck);
        vtcCheck = (CheckBox) findViewById(R.id.vtcCheck);
        ruralCheck = (CheckBox) findViewById(R.id.ruralCheck);
        selectedStateItem = StateItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SELECTED_STATE, context));

        headerTV.setText("Beneficiary Data" + " (" + selectedStateItem.getStateName() + ")");
        backIV = (ImageView) findViewById(R.id.back);
        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                rightTransition();
            }
        });
        aadhaarKycResponse = (AadhaarResponseItem) getIntent().getSerializableExtra("result");
        if (aadhaarKycResponse != null) {
            Log.d("TAG", "RESPONSE FINGERPRINT :" + aadhaarKycResponse.getDob() + aadhaarKycResponse.getUid());
        }

        nameLL = (LinearLayout) findViewById(R.id.nameLL);
        dobLL = (LinearLayout) findViewById(R.id.dobLL);
        genderLL = (LinearLayout) findViewById(R.id.genderLL);
        emailLL = (LinearLayout) findViewById(R.id.emailLL);
        phoneLL = (LinearLayout) findViewById(R.id.phoneLL);
        coLL = (LinearLayout) findViewById(R.id.coLL);
        coLL.setVisibility(View.VISIBLE);

        addressLL = (LinearLayout) findViewById(R.id.addressLL);
        pincodeLL = (LinearLayout) findViewById(R.id.pincodeLL);
        stateLL = (LinearLayout) findViewById(R.id.stateLL);
        distLL = (LinearLayout) findViewById(R.id.distLL);
        timestampLL = (LinearLayout) findViewById(R.id.timestampLL);
        txnLL = (LinearLayout) findViewById(R.id.txnLL);
        subDistLL = (LinearLayout) findViewById(R.id.subDistLL);
        respTimeLL = (LinearLayout) findViewById(R.id.respTimeLL);
        updateKycButton = (Button) findViewById(R.id.updateKycButton);
        screen = getIntent().getStringExtra("FindBeneficiaryByManualFragment");
        if (screen != null && !screen.equalsIgnoreCase("")) {
            coLL.setVisibility(View.GONE);
        }

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
        final ArrayList<StateItem> stateList2=new ArrayList<>();
        if (stateList != null) {
            for (StateItem item1 : stateList1) {
                if(item1.getStateCode().equalsIgnoreCase("16")){
                    stateList.add("Haryana");
                    stateList2.add(item1);
                }else if(item1.getStateCode().equalsIgnoreCase("22")){
                    stateList.add("Chattisgarh");
                    stateList2.add(item1);
                }else if(item1.getStateCode().equalsIgnoreCase("07")){
                    stateList.add("Delhi");
                    stateList2.add(item1);
                }
            }

        }



  /*      stateList.add("Haryana");
        stateList.add("Chattisgarh");
        stateList.add("Delhi");*/

        stateSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();
                stateCheck.setChecked(true);
                stateName = stateList.get(i);
                kycVtc.setText("");
                kycDist.setText("");
                Log.d("state name :", stateName);


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, stateList);
        stateSP.setAdapter(adapter1);

        for (int i =0 ; i<stateList2.size();i++){

            if (selectedStateItem.getStateCode().equalsIgnoreCase(stateList2.get(i).getStateCode())){

                stateSP.setSelection(i);
                // stateSP.setTitle(item.getStateName());

                stateName=stateList.get(i);
                Log.d("state name11 :", stateName);
                stateCheck.setChecked(true);
                break;
            }
        }


        final ArrayList<String> ruralList = new ArrayList<>();
        ruralList.add("Select Rural/Urban");
        ruralList.add("Rural");
        ruralList.add("Urban");

        ruralUrbanSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String item = adapterView.getItemAtPosition(i).toString();
                if(i==0){
                    ruralCheck.setChecked(false);
                    ruralUrbanStatus ="";
                    ruralUrbanTag="";
                    Log.d("ruralUrbanStatus :", ruralUrbanStatus + ":"+ ruralUrbanTag);
                }else if(i==1){
                    ruralCheck.setChecked(true);
                    ruralUrbanStatus = ruralList.get(i);
                    ruralUrbanTag="R";
                    Log.d("ruralUrbanStatus :", ruralUrbanStatus + ":"+ ruralUrbanTag);
                }else if(i==2){
                    ruralCheck.setChecked(true);
                    ruralUrbanStatus = ruralList.get(i);
                    ruralUrbanTag="U";
                    Log.d("ruralUrbanStatus :", ruralUrbanStatus + ":"+ ruralUrbanTag);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        ArrayAdapter<String> ruralAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, ruralList);
        ruralUrbanSP.setAdapter(ruralAdapter);



        updateKycButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = kycName.getText().toString();
                String gender = kycGender.getText().toString();
                String yob = kycDob.getText().toString();
                if (!gender.equalsIgnoreCase("")) {
                    gender = gender.substring(0, 1);
                    if (gender.equalsIgnoreCase("M")) {
                        gender = "1";
                    } else if (gender.equalsIgnoreCase("F")) {
                        gender = "2";
                    } else {
                        gender = "3";
                    }
                }
                String age = kycAge.getText().toString();
                String pincode = kycPincode.getText().toString();
                String fatherName = kycFather.getText().toString();
                String motherName = kycMother.getText().toString();
                String spouseName = kycSpouse.getText().toString();
                //String state = kycState.getText().toString();
                String district = kycDist.getText().toString();
                String village = kycVtc.getText().toString().trim();


                //String[] str=villageCode.split("-");

                FamilyListRequestModel request = new FamilyListRequestModel();

                if (nameCheck.isChecked()) {
                    request.setName(name.trim());
                }
                if (genderCheck.isChecked())
                    request.setGenderid(gender.trim());
                if (ageCheck.isChecked())
                    request.setAge(age.trim());
                if (pincodeCheck.isChecked())
                    request.setPincode(pincode.trim());
                if (fatherCheck.isChecked()) {
                    request.setFathername(fatherName.trim());
                }
                if (motherCheck.isChecked()) {
                    request.setMothername(motherName.trim());
                }
                if (spouseCheck.isChecked()) {
                    request.setSpousenm(spouseName.trim());
                }
                if (stateCheck.isChecked()) {

                    request.setState_name(stateName.trim());
                }
                if (ruralCheck.isChecked()) {
                    request.setRural_urban(ruralUrbanTag.trim());
                }
                location.setVillageTrue(false);
                if (vtcCheck.isChecked()) {
                    request.setVt_name(village);
                    location.setVillageTrue(true);
                }
                location.setDistTrue(false);
                if (distCheck.isChecked()) {
                    request.setDistrict_name(district);
                    location.setDistTrue(true);
                }
                location.setVilageName(village);
                location.setDistName(district);
                ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,LOCATION_TAG,location.serialize(),context);
                logRequestItem.setOperatorinput(request.serialize());
                ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,AppConstant.LOG_REQUEST,logRequestItem.serialize(),context);
                //  if(dobCheck.isChecked())
                // request.setD(pincode);
                // CustomAlert.alertWithOk(context, "Under Development");
                Intent theIntent = new Intent(context, FamilyListActivity.class);
                theIntent.putExtra("SearchParam", request);
                startActivity(theIntent);


            }
        });

        kycImageView = (ImageView) findViewById(R.id.kycImageView);
        kycGovtId = (TextView) findViewById(R.id.kycGovtId);
        kycName = (EditText) findViewById(R.id.kycName);
        kycDob = (EditText) findViewById(R.id.kycDob);
        kycGender = (EditText) findViewById(R.id.kycGender);
        kycEmail = (EditText) findViewById(R.id.kycEmail);
        kycPhone = (EditText) findViewById(R.id.kycPhone);
        kycCareOf = (EditText) findViewById(R.id.kycCareOf);
        kycAddr = (EditText) findViewById(R.id.kycAddr);
        kycTs = (EditText) findViewById(R.id.kycTs);
        kycTxn = (EditText) findViewById(R.id.kycTxn);
        kycRespTs = (EditText) findViewById(R.id.kycRespTs);
        kycPincode = (EditText) findViewById(R.id.kycPincode);
        kycState = (EditText) findViewById(R.id.kycState);
        kycDist = (AutoCompleteTextView) findViewById(R.id.kycDist);
        kycDist.setThreshold(1);
        kycSubDist = (EditText) findViewById(R.id.kycSubDist);
        kycVtc = (AutoCompleteTextView) findViewById(R.id.kycVtc);
        kycVtc.setThreshold(1);
        kycAge = (EditText) findViewById(R.id.kycAge);

        kycSpouse = (EditText) findViewById(R.id.kycSpouse);
        kycMother = (EditText) findViewById(R.id.kycMother);
        kycFather = (EditText) findViewById(R.id.kycFather);

        ArrayList<String> spinnerList = new ArrayList<>();
        spinnerList.add("C/O");
        spinnerList.add("S/O");
        spinnerList.add("D/O");
        spinnerList.add("W/O");

        kycVtc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (kycVtc.hasFocus())
                    autoSuggestVillage(s.toString());
                // AppUtility.softKeyBoard(FingerprintResultActivity.this, 0);
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });

        kycDist.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (kycDist.hasFocus())
                    autoSuggestDistrict(s.toString());
                // AppUtility.softKeyBoard(FingerprintResultActivity.this, 0);
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });
        if(location!=null){
            if(!location.getVilageName().equalsIgnoreCase("")){
                kycVtc.setText("");
                kycVtc.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        kycVtc.showDropDown();
                        kycVtc.setText(location.getVilageName());
                        //kycVtc.setSelection(mACTextViewEmail.getText().length());
                    }
                },500);

                if(location.isVillageTrue()){
                    vtcCheck.setChecked(true);
                }
            }
            if(!location.getDistName().equalsIgnoreCase("")){
                kycDist.setText("");

                kycDist.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        kycDist.showDropDown();
                        kycDist.setText(location.getDistName());
                        //kycVtc.setSelection(mACTextViewEmail.getText().length());
                    }
                },500);
                if(location.isDistTrue()){
                    distCheck.setChecked(true);
                }
            }
        }else{
            location=new SearchLocation();
        }

        cardTypeSpinner = (Spinner) findViewById(R.id.cardTypeSpinner);
        cardTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.spinner_layout, R.id.textView, spinnerList);
        cardTypeSpinner.setAdapter(adapter);

        kycDob.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() > 0) {
                    if (charSequence.toString().startsWith("1") || charSequence.toString().startsWith("2")) {
                        // if (Integer.parseInt(charSequence.toString().substring(1)) < 2) {

                        kycDob.setTextColor(context.getResources().getColor(R.color.black_shine));

                        if (kycDob.getText().toString().length() == 4) {
                            kycDob.setTextColor(context.getResources().getColor(R.color.green));

                            setAge(kycDob.getText().toString());
                            // isValidMobile = true;
                        } else {
                            //isValidMobile = false;
                        }
                    } else {
                        //isValidMobile = false;
                        kycDob.setTextColor(context.getResources().getColor(R.color.red));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        kycPincode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() > 0) {
                    if (!charSequence.toString().startsWith("0")) {
                        // if (Integer.parseInt(charSequence.toString().substring(1)) < 2) {

                        kycPincode.setTextColor(context.getResources().getColor(R.color.black_shine));

                        if (kycPincode.getText().toString().length() == 6) {
                            kycPincode.setTextColor(context.getResources().getColor(R.color.green));
                            // isValidMobile = true;
                        } else {
                            //isValidMobile = false;
                        }
                    } else {
                        //isValidMobile = false;
                        kycPincode.setTextColor(context.getResources().getColor(R.color.red));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        if (aadhaarKycResponse != null) {


            if (aadhaarKycResponse != null && aadhaarKycResponse.getResult() != null
                    && aadhaarKycResponse.getResult().equalsIgnoreCase("Y")) {

                //  kycDetailLayoutNew.setVisibility(View.VISIBLE);
                //   kycErrorLayout.setVisibility(View.GONE);

                if (aadhaarKycResponse.getBase64() != null && !aadhaarKycResponse.getBase64().equalsIgnoreCase("")) {

                    Bitmap imageBitmap = AppUtility.convertStringToBitmap(aadhaarKycResponse.getBase64());
                    if (imageBitmap != null) {
                        kycImageView.setImageBitmap(imageBitmap);
                    }
                    aadharResultRequestModel.setBase64(aadhaarKycResponse.getBase64());
                } /*else {
                    kycImageView.setVisibility(View.GONE);
                }*/

                if (aadhaarKycResponse.getUid() != null && !aadhaarKycResponse.getUid().equalsIgnoreCase("")) {
                    String aadharNo = "XXXXXXXX" + aadhaarKycResponse.getUid().substring(8);
                    kycGovtId.setText(aadharNo);

                }

                if (aadhaarKycResponse.getGovtDetailsModel() != null) {
                    if (aadhaarKycResponse.getGovtDetailsModel().getImage() != null
                            && !aadhaarKycResponse.getGovtDetailsModel().getImage().equalsIgnoreCase("")) {
                        Bitmap imageBitmap = AppUtility.convertStringToBitmap(aadhaarKycResponse.getGovtDetailsModel().getImage());
                        if (imageBitmap != null) {
                            kycImageView.setVisibility(View.VISIBLE);
                            kycImageView.setImageBitmap(imageBitmap);
                        }
                        aadharResultRequestModel.setBase64(aadhaarKycResponse.getBase64());
                    }
                    if (aadhaarKycResponse.getGovtDetailsModel().getIdNumber() != null) {
                        kycGovtId.setText(aadhaarKycResponse.getGovtDetailsModel().getIdNumber());
                    }
                }
                if (aadhaarKycResponse.getName() != null && !aadhaarKycResponse.getName().equalsIgnoreCase("")) {
                    kycName.setText(aadhaarKycResponse.getName());
                    aadharResultRequestModel.setName(kycName.getText().toString());
                } else {
                    nameLL.setVisibility(View.GONE);
                }
                if (aadhaarKycResponse.getDob() != null && !aadhaarKycResponse.getDob().equalsIgnoreCase("")) {
                    String yob;
                    yob = aadhaarKycResponse.getDob();
                    if (aadhaarKycResponse.getDob().length() > 4) {
                        yob = aadhaarKycResponse.getDob().substring(aadhaarKycResponse.getDob().length() - 4);
                    }

                    kycDob.setText(yob);

                    setAge(yob);
                    aadharResultRequestModel.setDob(yob);
                } else {
                    dobLL.setVisibility(View.GONE);
                }
                  /*  if (aadhaarKycResponse.getUidData().getPoa().getCo() != null)
                        kycCareOf.setText(aadhaarKycResponse.getUidData().getPoa().getCo());*/
                if (aadhaarKycResponse.getEmail() != null && !aadhaarKycResponse.getEmail().equalsIgnoreCase("")) {
                    kycEmail.setText(aadhaarKycResponse.getEmail());
                    aadharResultRequestModel.setEmail(kycEmail.getText().toString());
                } else {
                    emailLL.setVisibility(View.GONE);
                }
                if (aadhaarKycResponse.getGender() != null && !aadhaarKycResponse.getGender().equalsIgnoreCase("")) {
                    kycGender.setText(aadhaarKycResponse.getGender());
                    aadharResultRequestModel.setGender(kycGender.getText().toString());
                } else {
                    genderLL.setVisibility(View.GONE);
                }
                if (aadhaarKycResponse.getPhone() != null && !aadhaarKycResponse.getPhone().equalsIgnoreCase("")) {
                    kycPhone.setText(aadhaarKycResponse.getPhone() + "");
                    aadharResultRequestModel.setPhone(kycPhone.getText().toString());
                } else {
                    phoneLL.setVisibility(View.GONE);
                }

                StringBuilder addr = new StringBuilder();
                if (aadhaarKycResponse.getCo() != null && !aadhaarKycResponse.getCo().equalsIgnoreCase("")) {
                    //addr.append(aadhaarKycResponse.getCo());
                    String genderId = aadhaarKycResponse.getGender().substring(0, 1);
                    String[] co = aadhaarKycResponse.getCo().split(" ");
                    String careOf = "";
                    if (co.length > 1) {
                        String coTag = co[0];
                        coTag = coTag.replace(":", " ").trim();
                        kycCareOf.setText(co[1]);
                        for (int i = 1; i < co.length - 1; i++) {
                            careOf = careOf + co[i] + " ";
                        }

                      /* spinnerList.add("C/O");
                       spinnerList.add("S/O");
                       spinnerList.add("D/O");
                       spinnerList.add("W/O");*/
                        if (coTag.equalsIgnoreCase("C/O")) {
                            kycCareOf.setText(careOf.trim());
                            cardTypeSpinner.setSelection(0);
                            cardTypeSpinner.setEnabled(false);
                        }
                        if (coTag.equalsIgnoreCase("D/O") || coTag.equalsIgnoreCase("S/O")) {
                            kycFather.setText(careOf.trim());
                            kycMother.setText(careOf.trim());
                            if (genderId.equalsIgnoreCase("M")) {
                                cardTypeSpinner.setSelection(1);
                                cardTypeSpinner.setEnabled(false);
                            } else if (genderId.equalsIgnoreCase("F")) {
                                cardTypeSpinner.setSelection(2);
                                cardTypeSpinner.setEnabled(false);
                            } else {
                                cardTypeSpinner.setSelection(0);
                                cardTypeSpinner.setEnabled(false);
                            }
                        }
                        if (coTag.equalsIgnoreCase("W/O")) {
                            kycSpouse.setText(careOf.trim());
                            //if(genderId.equalsIgnoreCase("M")){
                            cardTypeSpinner.setSelection(3);
                            cardTypeSpinner.setEnabled(false);
                            //}
                        }
                    }

                } else {
                    //coLL.setVisibility(View.GONE);
                }
              /*  if (aadhaarKycResponse.getCo() != null && !aadhaarKycResponse.getCo().equalsIgnoreCase("")) {
                    //addr.append(aadhaarKycResponse.getCo());

                    String[] co = aadhaarKycResponse.getCo().split(" ");
                    String careOf = "";
                    if (co.length > 1) {
                        String coTag = co[0];
                        coTag.replace(":", "");
                        kycCareOf.setText(co[1]);
                        for (int i = 1; i < co.length - 1; i++) {
                            careOf = careOf + co[i] + " ";
                        }
                        if (coTag.equalsIgnoreCase("C/O")) {
                            kycCareOf.setText(careOf.trim());
                        }
                        if (coTag.equalsIgnoreCase("D/O") || coTag.equalsIgnoreCase("S/O")) {
                            kycFather.setText(careOf.trim());
                            kycMother.setText(careOf.trim());
                        }
                        if (coTag.equalsIgnoreCase("W/O")) {
                            kycSpouse.setText(careOf.trim());
                        }
                    }
                }*/
                /*if (aadhaarKycResponse.getCo() != null && !aadhaarKycResponse.getCo().equalsIgnoreCase("")) {
                    //addr.append(aadhaarKycResponse.getCo());

                    String[] co = aadhaarKycResponse.getCo().split(" ");
                    String careOf="";
                    if(co.length>1) {
                        kycCareOf.setText(co[1]);
                        for(int i=1;i<co.length-1;i++) {
                            careOf =careOf+co[i]+" ";
                        }
                        kycCareOf.setText(careOf.trim());
                        kycFather.setText(careOf.trim());
                        kycMother.setText(careOf.trim());
                        kycSpouse.setText(careOf.trim());
                    }

                } else {
                    //coLL.setVisibility(View.GONE);
                }*/


                if (aadhaarKycResponse.getHouse() != null && !aadhaarKycResponse.getHouse().equalsIgnoreCase("")) {
                    addr.append(aadhaarKycResponse.getHouse());
                }
                if (aadhaarKycResponse.getStreet() != null)
                    addr.append(", " + aadhaarKycResponse.getStreet());
                if (aadhaarKycResponse.getLm() != null)
                    addr.append(", " + aadhaarKycResponse.getLm());
                if (aadhaarKycResponse.getVtc() != null) {
                    addr.append(", " + aadhaarKycResponse.getVtc());
                    // kycVtc.setText(aadhaarKycResponse.getVtc());
                }
                   /* if (aadhaarKycResponse.getUidData().getPoa().getSubdist() != null)
                        addr.append("," + aadhaarKycResponse.getUidData().getPoa().getSubdist());*/
                if (aadhaarKycResponse.getDist() != null && !aadhaarKycResponse.getDist().equalsIgnoreCase("")) {
                    addr.append(", " + aadhaarKycResponse.getDist());
                } else {
                    // stateLL.setVisibility(View.GONE);
                }
                if (aadhaarKycResponse.getState() != null && !aadhaarKycResponse.getState().equalsIgnoreCase("")) {
                    addr.append(", " + aadhaarKycResponse.getState());
                } else {

                    addressLL.setVisibility(View.GONE);

                    //distLL.setVisibility(View.GONE);
                }
           /*     if (aadhaarKycResponse.getC != null)
                    addr.append(", " + aadhaarKycResponse.getUidData().getPoa().getCountry());*/

                kycAddr.setText(addr.toString());
                if (aadhaarKycResponse.getTs() != null && !aadhaarKycResponse.getTs().equalsIgnoreCase("")) {
                    kycTs.setText(AppUtility.convetEkycDate(aadhaarKycResponse.getTs()));
                    aadharResultRequestModel.setTs(kycTs.getText().toString());
                } else {
                    timestampLL.setVisibility(View.GONE);
                }
                if (aadhaarKycResponse.getTxn() != null && !aadhaarKycResponse.getTxn().equalsIgnoreCase("")) {
                    kycTxn.setText(aadhaarKycResponse.getTxn());

                } else {
                    txnLL.setVisibility(View.GONE);
                }
                if (aadhaarKycResponse.getPc() != null && !aadhaarKycResponse.getPc().equalsIgnoreCase("")) {
                    kycPincode.setText(aadhaarKycResponse.getPc());
                    aadharResultRequestModel.setPc(kycPincode.getText().toString());
                } else {
                    pincodeLL.setVisibility(View.GONE);
                }
                endTime = System.currentTimeMillis();
                totalTime = endTime - startTime;
                kycRespTs.setText(totalTime + " miliseconds");
                if (aadhaarKycResponse.getDist() != null && !aadhaarKycResponse.getDist().equalsIgnoreCase("")) {
                    kycDist.setText(aadhaarKycResponse.getDist());
                }
                if (selectedStateItem.getStateName() != null) {
                    kycState.setText(selectedStateItem.getStateName());
                }
                /*if (aadhaarKycResponse.getState() != null && !aadhaarKycResponse.getState().equalsIgnoreCase("")) {
                    kycState.setText(aadhaarKycResponse.getState());
                }*/
               /* if (aadhaarKycResponse.getSubdist() != null && !aadhaarKycResponse.getSubdist().equalsIgnoreCase("")) {
                    kycSubDist.setText(aadhaarKycResponse.getSubdist());
                }*/ /*else {
                    subDistLL.setVisibility(View.GONE);
                }*/
            }
        }

    }

    private void setAge(String yob) {
        kycAge.setText("");
        String currentYear = DateTimeUtil.currentDate(AppConstant.DATE_FORMAT);
        currentYear = currentYear.substring(0, 4);
        int age = Integer.parseInt(currentYear) - Integer.parseInt(yob);

        kycAge.setText(age + "");
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
                try {
                    //String request = familyListRequestModel.serialize();
                    HashMap<String, String> response = CustomHttp.httpPost(AppConstant.AUTO_SUGGEST, request.serialize());
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
                    for (String str : districtResponse) {
                        // if(str.contains(text)){
                        if(str!=null && !str.equalsIgnoreCase("")) {
                            String tempArr[] = str.split(";");
                            try {
                                if (tempArr[0] != null) {
                                    tempDist.add(tempArr[0]);
                                }
                                /*if (tempArr[1] != null) {
                                    distTemp.add(tempArr[1]);
                                }*/
                            }catch (Exception e){
                                Log.d("TAG","exception :"+e.toString());
                            }
                        }
                        // }
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                            android.R.layout.simple_dropdown_item_1line, tempDist);
                    kycDist.setAdapter(adapter);

                    kycDist.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {
                            String selected = tempDist.get(position);
                            kycVtc.setText("");
                            distCheck.setChecked(true);
                            kycDist.setText(selected);
                            //kycDist.setText(distTemp.get(position));

                        }
                    });
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
                String district = kycDist.getText().toString().trim();
                if (district != null && !district.equalsIgnoreCase("")) {
                    request.setDistrictName(district);
                }
                try {
                    //String request = familyListRequestModel.serialize();
                    HashMap<String, String> response = CustomHttp.httpPost(AppConstant.AUTO_SUGGEST, request.serialize());
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
                    for (String str : villageResponse) {
                        // if(str.contains(text)){
                        if(str!=null && !str.equalsIgnoreCase("")) {
                            String tempArr[] = str.split(";");
                            try {
                                if (tempArr[0] != null) {
                                    temp.add(tempArr[0]);
                                }
                                if (tempArr[1] != null) {
                                    distTemp.add(tempArr[1]);
                                }
                            }catch (Exception e){
                                Log.d("TAG","exception :"+e.toString());
                            }
                        }
                        // }
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                            android.R.layout.simple_dropdown_item_1line, temp);
                    kycVtc.setAdapter(adapter);

                    kycVtc.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {
                            String selected = temp.get(position);
                            vtcCheck.setChecked(true);
                            kycVtc.setText(selected);
                            kycDist.setText(distTemp.get(position));
                            distCheck.setChecked(true);
                        }
                    });
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

   /* private void autoSuggestVillage(final String text){

        TaskListener taskListener=new TaskListener() {
            @Override
            public void execute() {
                AutoSuggestRequestItem request=new AutoSuggestRequestItem();
                request.setVillageName(text);
                try {
                    //String request = familyListRequestModel.serialize();
                    HashMap<String, String> response = CustomHttp.httpPost(AppConstant.AUTO_SUGGEST, request.serialize());
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
                    for (String str : villageResponse) {
                        // if(str.contains(text)){
                        String tempArr[] = str.split(";");
                        temp.add(tempArr[0]);
                        distTemp.add(tempArr[1]);
                        // }
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                            android.R.layout.simple_dropdown_item_1line, temp);
                    kycVtc.setAdapter(adapter);

                    kycVtc.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {
                            String selected = temp.get(position);
                            kycVtc.setText(selected);
                            kycDist.setText(distTemp.get(position));
                        }
                    });
                }
            }
        };
        if (customAsyncTask != null) {
            customAsyncTask.cancel(true);
            customAsyncTask = null;
        }

        customAsyncTask = new CustomAsyncTask(taskListener, context);
        customAsyncTask.execute();
        *//*
        String[] COUNTRIES = new String[] {
                "Belgium", "Belance", "Betaly", "Bermany", "Beain"};
        temp=new ArrayList<>();
       *//*
    }
*/

}
