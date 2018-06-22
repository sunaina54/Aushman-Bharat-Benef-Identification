package com.nhpm.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.customComponent.utility.DateTimeUtil;
import com.nhpm.Models.FamilyMemberModel;
import com.nhpm.Models.request.PersonalDetailItem;
import com.nhpm.Models.response.DocsListItem;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;

import java.util.ArrayList;

/**
 * Created by SUNAINA on 21-06-2018.
 */

public class NameMatchScoreActivity extends BaseActivity {
    private Context context;
    private NameMatchScoreActivity activity;
    private RecyclerView memberRecycle, oldMemberRecycle;
    private String nameMatchScore = "";
    private Button confirmBTN, cancelBT, declineBT;
    private ArrayList<FamilyMemberModel> familyMemberFromSecc, familyMemberFromFamilyCard;
    private PersonalDetailItem personalDetailItem;
    private DocsListItem docsListItem;
    public static String PERSONAL_DETAIL_TAG = "PERSONAL_DETAIL";
    public static String SECC_DETAIL_TAG = "SECC_DETAIL";
    private TextView nameTV, genderTV, ageTV, distTV, stateTV, kycNameTV, kycgenderTV, kycageTV, kycdistTV, kycstateTV;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        activity = this;
        setContentView(R.layout.activity_name_match_score);
        setupScreen();
    }

    private void setupScreen() {

        personalDetailItem = (PersonalDetailItem) getIntent().getSerializableExtra(PERSONAL_DETAIL_TAG);
        docsListItem = (DocsListItem) getIntent().getSerializableExtra(SECC_DETAIL_TAG);

        nameTV = (TextView) findViewById(R.id.nameTV);
        genderTV = (TextView) findViewById(R.id.genderTV);
        ageTV = (TextView) findViewById(R.id.ageTV);
        distTV = (TextView) findViewById(R.id.distTV);
        stateTV = (TextView) findViewById(R.id.stateTV);

        confirmBTN = (Button) findViewById(R.id.tryAgainBT);
        cancelBT = (Button) findViewById(R.id.cancelBT);
        declineBT = (Button) findViewById(R.id.declineBT);

        kycNameTV = (TextView) findViewById(R.id.kycNameTV);
        kycageTV = (TextView) findViewById(R.id.kycageTV);
        kycgenderTV = (TextView) findViewById(R.id.kycgenderTV);
        kycdistTV = (TextView) findViewById(R.id.kycdistTV);
        kycstateTV = (TextView) findViewById(R.id.kycstateTV);


        if (personalDetailItem != null) {
            if (personalDetailItem.getName() != null) {
                kycNameTV.setText(personalDetailItem.getName());
            }
            if (personalDetailItem.getDistrict() != null) {
                kycdistTV.setText(personalDetailItem.getDistrict());
            }
            if (personalDetailItem.getState() != null) {
                kycdistTV.setText(personalDetailItem.getState());
            }
            kycageTV.setText("");
            /*if (personalDetailItem.getYob() != null && personalDetailItem.getYob().length() >= 4) {
                try {
                    String currentYear = DateTimeUtil.currentDate(AppConstant.DATE_FORMAT);
                    currentYear = currentYear.substring(0, 4);
                    int age = Integer.parseInt(currentYear) - Integer.parseInt(personalDetailItem.getYob());
                    kycageTV.setText(age + "");
                }catch (Exception e){
                    String currentYear = DateTimeUtil.currentDate("dd-mm-yyyy");
                    currentYear = currentYear.substring(0, 4);
                    int age = Integer.parseInt(currentYear) - Integer.parseInt(personalDetailItem.getYob());
                    kycageTV.setText(age + "");
                }
            }*/
            if (personalDetailItem.getYob() != null && personalDetailItem.getYob().length() >= 4) {

                    String currentYear = DateTimeUtil.currentDate(AppConstant.DATE_FORMAT);

                    currentYear = currentYear.substring(0, 4);
                    String date=DateTimeUtil.
                            convertTimeMillisIntoStringDate(DateTimeUtil.convertDateIntoTimeMillis(personalDetailItem.getYob()),AppConstant.DATE_FORMAT);
                   String arr[];
                String aadhaarYear=null;
                    if(personalDetailItem.getYob().contains("-")){
                      arr=personalDetailItem.getYob().split("-") ;
                      if(arr[0].length()==4){
                          aadhaarYear=arr[0];
                      }else if(arr[2].length()==4){
                          aadhaarYear=arr[2];
                      }
                    }else if(personalDetailItem.getYob().contains("/")){
                        arr=personalDetailItem.getYob().split("/") ;
                        if(arr[0].length()==4){
                            aadhaarYear=arr[0];
                        }else if(arr[2].length()==4){
                            aadhaarYear=arr[2];
                        }
                    }
                    if(aadhaarYear!=null) {
                        int age = Integer.parseInt(currentYear) - Integer.parseInt(aadhaarYear);
                        kycageTV.setText(age + "");
                    }

            }else  if(personalDetailItem.getYob() != null && personalDetailItem.getYob().length() ==4){
                String currentYear = DateTimeUtil.currentDate("dd-mm-yyyy");
                currentYear = currentYear.substring(0, 4);
                int age = Integer.parseInt(currentYear) - Integer.parseInt(personalDetailItem.getYob());
            }

            if (personalDetailItem.getGender() != null && !personalDetailItem.getGender().equalsIgnoreCase("")) {
                String gender = personalDetailItem.getGender();
                if (gender.substring(0, 1).toUpperCase().equalsIgnoreCase("M")) {
                    genderTV.setText("Male");
                } else if (gender.substring(0, 1).toUpperCase().equalsIgnoreCase("F")) {
                    genderTV.setText("Female");
                } else {
                    genderTV.setText("Other");
                }
            }

        }
        if (docsListItem != null) {
            if (docsListItem.getName() != null) {
                nameTV.setText(docsListItem.getName());
            }
            if (docsListItem.getDistrict_name() != null) {
                distTV.setText(docsListItem.getDistrict_name());
            }
            if (docsListItem.getState_name() != null) {
                stateTV.setText(docsListItem.getState_name());
            }
            if (docsListItem.getGenderid().equalsIgnoreCase("1")) {
                genderTV.setText("Male");
            } else if (docsListItem.getGenderid().equalsIgnoreCase("2")) {
                genderTV.setText("Female");
            } else {
                genderTV.setText("Other");
            }
            String yob = "";
            if (docsListItem.getDob() != null && docsListItem.getDob().length() > 4) {
                yob = docsListItem.getDob().substring(0, 4);
            } else {
                yob = docsListItem.getDob();
            }

            String currentYear = DateTimeUtil.currentDate(AppConstant.DATE_FORMAT);
            currentYear = currentYear.substring(0, 4);
            int age = Integer.parseInt(currentYear) - Integer.parseInt(yob);
            ageTV.setText(age + "");
            //  ageTV.setText();
        }


        confirmBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                nameMatchScore = "80";
                Intent data = new Intent();
                data.putExtra("matchScore", nameMatchScore);
                setResult(4, data);
                activity.finish();

            }
        });


        declineBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameMatchScore = "0";
                /*Fragment fragment= new FamilyDetailsFragment();
                Bundle args = new Bundle();
                args.putString("familyMatchScore", familyMatchScore);
                fragment.setArguments(args);
                callFragment(fragment);
                activity.finish();*/
                Intent data = new Intent();
                data.putExtra("matchScore", nameMatchScore);
                setResult(4, data);
                activity.finish();


            }
        });
        cancelBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent data = new Intent();
                data.putExtra("matchScore", nameMatchScore);
                setResult(4, data);
                activity.finish();
            }
        });
    }


}
