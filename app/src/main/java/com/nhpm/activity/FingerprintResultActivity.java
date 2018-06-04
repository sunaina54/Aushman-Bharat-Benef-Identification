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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.customComponent.CustomAlert;
import com.customComponent.utility.DateTimeUtil;
import com.nhpm.Models.request.AadharResultRequestModel;
import com.nhpm.Models.request.FamilyListRequestModel;
import com.nhpm.Models.response.verifier.AadhaarResponseItem;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;
import com.nhpm.Utility.AppUtility;

import java.util.ArrayList;

/**
 * Created by Dell3 on 04-05-2018.
 */

public class FingerprintResultActivity extends BaseActivity {
    private Context context;
    private AadhaarResponseItem aadhaarKycResponse;
    private EditText kycSubDist, kycName, kycDob, kycPincode, kycGender,
            kycEmail, kycPhone, kycCareOf, kycAddr, kycTs, kycTxn, kycRespTs,
            kycErrorEditText, kycDist, kycState, kycVtc,kycAge,kycSpouse,kycMother,kycFather;
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
    private CheckBox ageCheck,nameCheck,dobCheck,genderCheck,pincodeCheck,fatherCheck,motherCheck,
            spouseCheck,stateCheck,distCheck;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.bio_ekyc_layout);
        setupScreen();
    }

    private void setupScreen() {

        aadharResultRequestModel = new AadharResultRequestModel();
        headerTV = (TextView) findViewById(R.id.centertext);
        ageCheck = (CheckBox) findViewById(R.id.ageCheck);
        nameCheck = (CheckBox) findViewById(R.id.nameCheck);
        dobCheck = (CheckBox) findViewById(R.id.dobCheck);
        genderCheck = (CheckBox) findViewById(R.id.genderCheck);
        pincodeCheck = (CheckBox) findViewById(R.id.pincodeCheck);
        fatherCheck = (CheckBox) findViewById(R.id.fatherCheck);
        motherCheck = (CheckBox) findViewById(R.id.motherCheck);
        spouseCheck = (CheckBox) findViewById(R.id.spouseCheck);
        stateCheck = (CheckBox) findViewById(R.id.stateCheck);
        distCheck = (CheckBox) findViewById(R.id.distCheck);

        headerTV.setText("Beneficiary Data");
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
        addressLL = (LinearLayout) findViewById(R.id.addressLL);
        pincodeLL = (LinearLayout) findViewById(R.id.pincodeLL);
        stateLL = (LinearLayout) findViewById(R.id.stateLL);
        distLL = (LinearLayout) findViewById(R.id.distLL);
        timestampLL = (LinearLayout) findViewById(R.id.timestampLL);
        txnLL = (LinearLayout) findViewById(R.id.txnLL);
        subDistLL = (LinearLayout) findViewById(R.id.subDistLL);
        respTimeLL = (LinearLayout) findViewById(R.id.respTimeLL);
        updateKycButton = (Button) findViewById(R.id.updateKycButton);
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
                String state = kycState.getText().toString();
                String district = kycDist.getText().toString();
                FamilyListRequestModel request = new FamilyListRequestModel();
                request.setName("");
                request.setGenderid("");
                request.setAge("");
                request.setPincode("");
                request.setFathername("");


                if (nameCheck.isChecked()) {
                    request.setName(name);
                }
                if (genderCheck.isChecked())
                    request.setGenderid(gender);
                if (ageCheck.isChecked())
                    request.setAge(age);
                if (pincodeCheck.isChecked())
                    request.setPincode(pincode);
                if (fatherCheck.isChecked()) {
                    request.setFathername(fatherName);
                }
                if (motherCheck.isChecked()) {
                    request.setMothername(motherName);
                }
                if (spouseCheck.isChecked()) {
                    request.setSpousenm(spouseName);
                }
                if (stateCheck.isChecked()) {
                    request.setState_name(state);
                }
               /* if(distCheck.isChecked()){
                    request.setdi(state);
                }*/
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
        kycDist = (EditText) findViewById(R.id.kycDist);
        kycSubDist = (EditText) findViewById(R.id.kycSubDist);
        kycVtc = (EditText) findViewById(R.id.kycVtc);
        kycAge = (EditText) findViewById(R.id.kycAge);

        kycSpouse = (EditText) findViewById(R.id.kycSpouse);
        kycMother = (EditText) findViewById(R.id.kycMother);
        kycFather = (EditText) findViewById(R.id.kycFather);

        ArrayList<String> spinnerList = new ArrayList<>();
        spinnerList.add("C/O");
        spinnerList.add("S/O");
        spinnerList.add("W/O");
        spinnerList.add("D/O");


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
                }
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
                        kycVtc.setText(aadhaarKycResponse.getVtc());
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
                    }/* else {
                    kycDist.setVisibility(View.GONE);
                }*/
                    if (aadhaarKycResponse.getState() != null && !aadhaarKycResponse.getState().equalsIgnoreCase("")) {
                        kycState.setText(aadhaarKycResponse.getState());
                    } /*else {
                    kycState.setVisibility(View.GONE);
                }*/

               /* if (aadhaarKycResponse.getSubdist() != null && !aadhaarKycResponse.getSubdist().equalsIgnoreCase("")) {
                    kycSubDist.setText(aadhaarKycResponse.getSubdist());
                }*/ /*else {
                    subDistLL.setVisibility(View.GONE);
                }*/
                }
            }

        }

    private void setAge(String yob){
        kycAge.setText("");
        String currentYear= DateTimeUtil.currentDate(AppConstant.DATE_FORMAT);
        currentYear=currentYear.substring(0,4);
        int age=Integer.parseInt(currentYear)-Integer.parseInt(yob);

        kycAge.setText(age+"");
    }
}
