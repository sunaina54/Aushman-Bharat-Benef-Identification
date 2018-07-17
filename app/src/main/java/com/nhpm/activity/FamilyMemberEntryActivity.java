package com.nhpm.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.customComponent.CustomAlert;
import com.customComponent.utility.DateTimeUtil;
import com.nhpm.Models.FamilyMemberModel;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;
import com.nhpm.Utility.AppUtility;
import com.nhpm.fragments.FamilyDetailsFragment;

import java.util.ArrayList;

/**
 * Created by SUNAINA on 07-06-2018.
 */

public class FamilyMemberEntryActivity extends BaseActivity {
    private Context context;
    private FamilyMemberEntryActivity activity;
    private TextView headerTV;
    private ImageView backIV;
    private Button saveBT,cancelBT;
    private EditText familyMemberNameET;
    private FamilyMemberModel familymemberItem;
    private Intent theIntent;
    private String index;
    private EditText yobET,pincodeET;
    private RadioGroup genderRG;
    private RadioButton maleRB, femaleRB, otherRB;
    private String manualGenderSelection="",currentYear;
    private boolean emailValid=false;
    private ArrayList<FamilyMemberModel> familyMemberModel;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        activity=this;
        setContentView(R.layout.activity_family_member_entry);
        setupScreen();
    }

    private void setupScreen() {
        //mProgressBar = (ProgressBar) findViewById(R.id.mProgressBar);
        headerTV = (TextView) findViewById(R.id.centertext);
        headerTV.setText("Add Member");
        backIV = (ImageView) findViewById(R.id.back);
        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                rightTransition();
            }
        });



        AppUtility.navigateToHome(context, activity);
        theIntent = getIntent();
        familymemberItem = (FamilyMemberModel) theIntent.getSerializableExtra(AppConstant.FAMILY_MEMBER_RESULT_CODE_NAME);
        index= theIntent.getStringExtra(FamilyDetailsFragment.INDEX);
        familyMemberModel = (ArrayList<FamilyMemberModel>) getIntent().getSerializableExtra("FamilyMemberList");
        familyMemberNameET = (EditText) findViewById(R.id.familyMemberNameET);
        familyMemberNameET.requestFocus();
        AppUtility.softKeyBoard(activity, 1);

        yobET = (EditText) findViewById(R.id.yobET);
        pincodeET = (EditText)findViewById(R.id.pincodeET);
        genderRG = (RadioGroup) findViewById(R.id.genderRG);
        maleRB= (RadioButton) findViewById(R.id.maleRB);
        femaleRB= (RadioButton) findViewById(R.id.femaleRB);
        otherRB= (RadioButton) findViewById(R.id.otherRB);
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

        String currentDate = DateTimeUtil.currentDate("dd MM yyyy");
        Log.d("current date", currentDate);
        currentYear = currentDate.substring(6, 10);
        Log.d("current year", currentYear);

   /*     yobET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() > 0) {
                    if(charSequence.toString().startsWith("1") || charSequence.toString().startsWith("2")){
                        // if (Integer.parseInt(charSequence.toString().substring(1)) < 2) {

                        yobET.setTextColor(context.getResources().getColor(R.color.black_shine));

                        if (yobET.getText().toString().length() == 4) {
                            yobET.setTextColor(context.getResources().getColor(R.color.green));
                            // isValidMobile = true;
                        }else{
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
        });*/

        pincodeET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() > 0) {
                    if(!charSequence.toString().startsWith("0")){
                        // if (Integer.parseInt(charSequence.toString().substring(1)) < 2) {

                        pincodeET.setTextColor(context.getResources().getColor(R.color.black_shine));

                        if (pincodeET.getText().toString().length() == 6) {
                            pincodeET.setTextColor(context.getResources().getColor(R.color.green));
                            // isValidMobile = true;
                        }else{
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

        saveBT = (Button) findViewById(R.id.saveBT);
        cancelBT = (Button) findViewById(R.id.cancelBT);
        if(index!=null){
            saveBT.setText("Update");
            familyMemberNameET.setText(familymemberItem.getName());
            familyMemberNameET.setSelection(familymemberItem.getName().length());

        }

        saveBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String yob = yobET.getText().toString();
              //  String pincode = pincodeET.getText().toString();
                if(familyMemberNameET.getText().toString().equalsIgnoreCase("")){
                    CustomAlert.alertWithOk(context,getResources().getString(R.string.please_enter_name));
                    return;
                }

                if (yob.trim().equalsIgnoreCase("")) {
                    CustomAlert.alertWithOk(context, "Please enter age");
                    return;
                }

/*
                if (yob.trim().equalsIgnoreCase("")) {
                    CustomAlert.alertWithOk(context, "Please enter year of birth");
                    return;
                }*/

               /* if (yob != null && !yob.equalsIgnoreCase("")) {
                    int yearRange = Integer.parseInt(currentYear) - 100;

                    if (yob.equalsIgnoreCase(currentYear) || Integer.parseInt(yob) < yearRange) {
                        CustomAlert.alertWithOk(context, "Please enter valid year of birth");
                        return;
                    }

                }*/


                if (manualGenderSelection != null && manualGenderSelection.equalsIgnoreCase("")) {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterGenderGovt));
                    return;
                }

              /*  if (pincode.equalsIgnoreCase("")) {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterPincodeGovt));
                    return;
                }*/

                if(familyMemberModel!=null && familyMemberModel.size()>0){
                    String name =familyMemberNameET.getText().toString();
                        for(FamilyMemberModel item:familyMemberModel){
                            if(item.getName().equalsIgnoreCase(name)){
                                CustomAlert.alertWithOk(context,"Member is already added.");
                                return;

                            }
                            break;
                        }
                }

                if(familymemberItem!=null){
                    familymemberItem.setName(familyMemberNameET.getText().toString());
                   // familymemberItem.setPincode(pincode);
                    familymemberItem.setGenderid(manualGenderSelection);
                    familymemberItem.setDob(yob);


                }else{
                    familymemberItem = new FamilyMemberModel();
                    familymemberItem.setName(familyMemberNameET.getText().toString());
                 //   familymemberItem.setPincode(pincode);
                    familymemberItem.setGenderid(manualGenderSelection);
                    familymemberItem.setDob(yob);


                }

                Intent theIntent = new Intent();
                theIntent.putExtra(AppConstant.FAMILY_MEMBER_RESULT_CODE_NAME,familymemberItem);
                theIntent.putExtra(FamilyDetailsFragment.INDEX,index);
                setResult(Activity.RESULT_OK,theIntent);

                AppUtility.softKeyBoard(activity, 0);
                finish();

            }
        });
        cancelBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
