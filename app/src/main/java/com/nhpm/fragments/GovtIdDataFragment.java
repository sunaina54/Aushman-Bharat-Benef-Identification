package com.nhpm.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.customComponent.CustomAlert;
import com.customComponent.utility.DateTimeUtil;
import com.customComponent.utility.ProjectPrefrence;
import com.nhpm.AadhaarUtils.Global;
import com.nhpm.Models.AadharAuthItem;
import com.nhpm.Models.request.GovtDetailsModel;
import com.nhpm.Models.response.seccMembers.SelectedMemberItem;
import com.nhpm.Models.response.verifier.AadhaarResponseItem;
import com.nhpm.Models.response.verifier.VerifierLoginResponse;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;
import com.nhpm.activity.FingerprintResultActivity;


public class GovtIdDataFragment extends Fragment {
    private Context context;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private EditText manualAadharNumberET, manualNameAsInAadhaarET,
            manualDobAsInAadhaarET, manualYobAsInAadhaarET,manualPincodeAsInAadhaarET;
    private Button validateAdhaarBT;
    private RadioGroup radioGroup, dateOfBirthRG, genderRG;
    private RadioButton radioButtonQrcode, radioButtonManual, maleRB, femaleRB, otherRB, dobRB, yobRB;
    public GovtIdDataFragment() {
        // Required empty public constructor
    }
    private String manualGenderSelection = "";
    AadhaarResponseItem aadhaarResponseItem;
    private GovtDetailsModel govtDetailsModel;
    private TextView headerTV;
    private String currentYear;

    public GovtDetailsModel getGovtDetailsModel() {
        return govtDetailsModel;
    }

    public void setGovtDetailsModel(GovtDetailsModel govtDetailsModel) {
        this.govtDetailsModel = govtDetailsModel;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_govt_id_data, container, false);
        context = getActivity();
        setUpScreen(view);
        return view;
    }

    private void setUpScreen(View v) {
        aadhaarResponseItem = new AadhaarResponseItem();
        if(govtDetailsModel!=null){
            Log.d("TAG","DATA GOVT"+govtDetailsModel.getIdNumber());
        }else {
            Log.d("TAG","DATA GOVT no data");
        }
        String currentDate = DateTimeUtil.currentDate("dd MM yyyy");
        Log.d("current date", currentDate);
        currentYear = currentDate.substring(6, 10);
        Log.d("current year", currentYear);

        manualYobAsInAadhaarET= (EditText) v.findViewById(R.id.manualYobAsInAadhaarET);
        manualNameAsInAadhaarET= (EditText) v.findViewById(R.id.manualNameAsInAadhaarET);
        manualPincodeAsInAadhaarET= (EditText) v.findViewById(R.id.manualPincodeAsInAadhaarET);
        validateAdhaarBT = (Button) v.findViewById(R.id.validateAdhaarBT);
        genderRG = (RadioGroup) v.findViewById(R.id.genderRG);
        maleRB = (RadioButton) v.findViewById(R.id.maleRB);
        femaleRB = (RadioButton) v.findViewById(R.id.femaleRB);
        otherRB = (RadioButton) v.findViewById(R.id.otherRB);
        manualPincodeAsInAadhaarET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() > 0) {
                    if(!charSequence.toString().startsWith("0")){
                        // if (Integer.parseInt(charSequence.toString().substring(1)) < 2) {

                        manualPincodeAsInAadhaarET.setTextColor(context.getResources().getColor(R.color.black_shine));

                        if (manualPincodeAsInAadhaarET.getText().toString().length() == 6) {
                            manualPincodeAsInAadhaarET.setTextColor(context.getResources().getColor(R.color.green));
                            // isValidMobile = true;
                        }else{
                            //isValidMobile = false;
                        }
                    } else {
                        //isValidMobile = false;
                        manualPincodeAsInAadhaarET.setTextColor(context.getResources().getColor(R.color.red));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        manualYobAsInAadhaarET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() > 0) {
                    if(charSequence.toString().startsWith("1") || charSequence.toString().startsWith("2")){
                   // if (Integer.parseInt(charSequence.toString().substring(1)) < 2) {

                        manualYobAsInAadhaarET.setTextColor(context.getResources().getColor(R.color.black_shine));

                        if (manualYobAsInAadhaarET.getText().toString().length() == 4) {
                            manualYobAsInAadhaarET.setTextColor(context.getResources().getColor(R.color.green));
                           // isValidMobile = true;
                        }else{
                            //isValidMobile = false;
                        }
                    } else {
                        //isValidMobile = false;
                        manualYobAsInAadhaarET.setTextColor(context.getResources().getColor(R.color.red));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        manualLayoutVisible();

        validateAdhaarBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String aadharDob="";
                aadharDob = manualYobAsInAadhaarET.getText().toString();
                if(manualNameAsInAadhaarET.getText().toString().equalsIgnoreCase("")){
                    CustomAlert.alertWithOk(context,"Please enter name");
                    return;
                }

                if(manualYobAsInAadhaarET.getText().toString().equalsIgnoreCase("")){
                    CustomAlert.alertWithOk(context,"Please enter year of birth");
                    return;
                }

                if (aadharDob != null && !aadharDob.equalsIgnoreCase("")) {
                    int yearRange = Integer.parseInt(currentYear) - 100;

                    if (aadharDob.equalsIgnoreCase(currentYear) || Integer.parseInt(aadharDob) < yearRange) {
                        CustomAlert.alertWithOk(context, "Please enter valid year of birth");
                        return;
                    }

                }
                if (manualGenderSelection != null && manualGenderSelection.equalsIgnoreCase("")) {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterGender));
                    return;
                }

                if(manualPincodeAsInAadhaarET.getText().toString().equalsIgnoreCase("")){
                    CustomAlert.alertWithOk(context,"Please enter pincode");
                    return;
                }

                aadhaarResponseItem.setName(manualNameAsInAadhaarET.getText().toString());
                aadhaarResponseItem.setDob(manualYobAsInAadhaarET.getText().toString());
                aadhaarResponseItem.setPc(manualPincodeAsInAadhaarET.getText().toString());
                aadhaarResponseItem.setGender(manualGenderSelection);
                aadhaarResponseItem.setResult("Y");
                aadhaarResponseItem.setGovtDetailsModel(govtDetailsModel);
                Intent intent = new Intent(context , FingerprintResultActivity.class);
                intent.putExtra("result",aadhaarResponseItem);
                startActivity(intent);
            }
        });

    }
    private void manualLayoutVisible() {

        genderRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == maleRB.getId()) {
                    manualGenderSelection = "M";
                //    aadharAuthItem.setGender(manualGenderSelection);
                } else if (checkedId == femaleRB.getId()) {
                    manualGenderSelection = "F";
                 //   aadharAuthItem.setGender(manualGenderSelection);
                } else if (checkedId == otherRB.getId()) {
                    manualGenderSelection = "T";
                  //  aadharAuthItem.setGender(manualGenderSelection);
                }

            }

        });
    }
}
