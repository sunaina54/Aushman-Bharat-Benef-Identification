package com.nhpm.activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.customComponent.utility.ProjectPrefrence;
import com.nhpm.Models.request.PersonalDetailItem;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;
import com.nhpm.fragments.PersonalDetailsFragment;

import java.util.ArrayList;

/**
 * Created by SUNAINA on 02-08-2018.
 */

public class KycPreviewActivity extends BaseActivity {
    private Context context;

    private PersonalDetailItem personalDetailItem;
    private Spinner cardTypeSpinner;
    private EditText kycCareOf,kycSpouse,kycMother,kycFather;
    private Button submitBTN;
    private KycPreviewActivity kycPreviewActivity;
    private String careofDec="";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        kycPreviewActivity=this;
        setContentView(R.layout.activity_kyc_preview_layout);
        setupScreen();
    }

    private void setupScreen() {
        personalDetailItem = PersonalDetailItem.create(ProjectPrefrence.
                getSharedPrefrenceData(AppConstant.PROJECT_NAME, "AADHAAR_DATA", context));
        submitBTN = (Button) findViewById(R.id.submitBTN);
        kycCareOf = (EditText) findViewById(R.id.kycCareOf);
        kycSpouse = (EditText) findViewById(R.id.kycSpouse);
        kycMother = (EditText) findViewById(R.id.kycMother);
        kycFather = (EditText) findViewById(R.id.kycFather);

        cardTypeSpinner = (Spinner) findViewById(R.id.cardTypeSpinner);
        ArrayList<String> spinnerList = new ArrayList<>();
        spinnerList.add("C/O");
        spinnerList.add("S/O");
        spinnerList.add("D/O");
        spinnerList.add("W/O");
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

        if (personalDetailItem != null) {
            if (personalDetailItem.getCoTypeValue() != null && !personalDetailItem.getCoTypeValue().equalsIgnoreCase("")) {
                //addr.append(aadhaarKycResponse.getCo());
                String genderId = personalDetailItem.getGender().substring(0, 1);
                String[] co = personalDetailItem.getCoTypeValue() .split(" ");
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
                        cardTypeSpinner.setEnabled(true);
                    }
                    if (coTag.equalsIgnoreCase("D/O") || coTag.equalsIgnoreCase("S/O")) {
                        kycFather.setText(careOf.trim());

                        kycMother.setText(careOf.trim());

                        if (genderId.equalsIgnoreCase("M")) {
                            cardTypeSpinner.setSelection(1);
                            cardTypeSpinner.setEnabled(true);
                        } else if (genderId.equalsIgnoreCase("F")) {
                            cardTypeSpinner.setSelection(2);
                            cardTypeSpinner.setEnabled(true);
                        } else {
                            cardTypeSpinner.setSelection(0);
                            cardTypeSpinner.setEnabled(true);
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

            }
        }

        submitBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!kycFather.getText().toString().equalsIgnoreCase("")){
                    careofDec=kycFather.getText().toString();
                }else if(!kycMother.getText().toString().equalsIgnoreCase("")){
                    careofDec=kycMother.getText().toString();
                }else  if(!kycSpouse.getText().toString().equalsIgnoreCase("")) {
                    careofDec = kycSpouse.getText().toString();
                }


                personalDetailItem.setCareOfTypeDec(kycCareOf.getText().toString());
                personalDetailItem.setCareOfDec(careofDec);
                ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_NAME, "AADHAAR_DATA", personalDetailItem.serialize(), context);
                /*  PersonalDetailsFragment fragment = new PersonalDetailsFragment();

                        callFragment(fragment);*/

                kycPreviewActivity.finish();
            }
        });

    }

    public void callFragment(PersonalDetailsFragment fragment) {
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragContainer, fragment);
            fragmentTransaction.commit();
        }
    }
}
