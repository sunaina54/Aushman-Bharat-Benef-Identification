package com.nhpm.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.customComponent.CustomAlert;
import com.customComponent.CustomAsyncTask;
import com.customComponent.TaskListener;
import com.customComponent.utility.CustomHttp;
import com.customComponent.utility.DateTimeUtil;
import com.customComponent.utility.ProjectPrefrence;
import com.nhpm.Models.FamilyMemberModel;
import com.nhpm.Models.NameMatchScoreModelRequest;
import com.nhpm.Models.request.GetTotalScoreRequestModel;
import com.nhpm.Models.request.PersonalDetailItem;
import com.nhpm.Models.response.DocsListItem;
import com.nhpm.Models.response.MatchScoreResponse;
import com.nhpm.Models.response.verifier.VerifierLoginResponse;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;
import com.nhpm.Utility.AppUtility;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

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
    private TextView villageTV,kycVillageTV,nameTV, genderTV, ageTV, distTV, stateTV, kycNameTV, kycgenderTV, kycageTV, kycdistTV, kycstateTV, pincodeTV, kycPincodeTV;
    private CustomAsyncTask asyncTask;
    private GetTotalScoreRequestModel requestModel;
    private NameMatchScoreModelRequest request;
    private MatchScoreResponse matchResponse;
    private AlertDialog dialog;
    private Button fetchScoreBT;
    private ImageView backIV;
    private VerifierLoginResponse verifierLoginResponse;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        activity = this;
        setContentView(R.layout.activity_name_match_score);
        setupScreen();
    }

    private void setupScreen() {
        Log.d("name match11","go for name match");
        verifierLoginResponse = VerifierLoginResponse.create(
                ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.VERIFIER_CONTENT, context));

      //  personalDetailItem = (PersonalDetailItem) getIntent().getSerializableExtra(PERSONAL_DETAIL_TAG);
        personalDetailItem =PersonalDetailItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,NameMatchScoreActivity.PERSONAL_DETAIL_TAG,context));
        docsListItem =DocsListItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,NameMatchScoreActivity.SECC_DETAIL_TAG,context));
//(DocsListItem) getIntent().getSerializableExtra(SECC_DETAIL_TAG);
        backIV = (ImageView) findViewById(R.id.back);
        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                rightTransition();
            }
        });
        nameTV = (TextView) findViewById(R.id.nameTV);
        villageTV = (TextView) findViewById(R.id.villageTV);
        genderTV = (TextView) findViewById(R.id.genderTV);
        ageTV = (TextView) findViewById(R.id.ageTV);
        distTV = (TextView) findViewById(R.id.distTV);
        stateTV = (TextView) findViewById(R.id.stateTV);
        pincodeTV = (TextView) findViewById(R.id.pincodeTV);
        fetchScoreBT = (Button) findViewById(R.id.fetchScoreBT);
        confirmBTN = (Button) findViewById(R.id.tryAgainBT);
        cancelBT = (Button) findViewById(R.id.cancelBT);
        declineBT = (Button) findViewById(R.id.declineBT);

        kycNameTV = (TextView) findViewById(R.id.kycNameTV);
        kycVillageTV = (TextView) findViewById(R.id.kycVillageTV);
        kycageTV = (TextView) findViewById(R.id.kycageTV);
        kycgenderTV = (TextView) findViewById(R.id.kycgenderTV);
        kycdistTV = (TextView) findViewById(R.id.kycdistTV);
        kycstateTV = (TextView) findViewById(R.id.kycstateTV);
        kycPincodeTV = (TextView) findViewById(R.id.kycPinTV);


        if (personalDetailItem != null) {
            if (personalDetailItem.getName() != null) {
                kycNameTV.setText(personalDetailItem.getName());
            }
            if (personalDetailItem.getDistrict() != null) {
                kycdistTV.setText(personalDetailItem.getDistrict());
            } else {
                kycdistTV.setText("");
            }
            if (personalDetailItem.getState() != null) {
                kycstateTV.setText(personalDetailItem.getState());
            } else {
                kycstateTV.setText("");

            }
            if (personalDetailItem.getPinCode() != null) {
                kycPincodeTV.setText(personalDetailItem.getPinCode());
            }

            if(personalDetailItem.getVtcBen()!=null){
                kycVillageTV.setText(personalDetailItem.getVtcBen());
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
                    int age = Integer.parseInt(currentYear) - Integer.parseInt(aadhaarYear);
                    kycageTV.setText(age + "");
                }

            } else if (personalDetailItem.getYob() != null && personalDetailItem.getYob().length() == 4) {
                String currentYear = DateTimeUtil.currentDate("dd-mm-yyyy");
                currentYear = currentYear.substring(6, 10);
                int age = Integer.parseInt(currentYear) - Integer.parseInt(personalDetailItem.getYob());
                kycageTV.setText(age + "");
            }

            if (personalDetailItem.getGender() != null && !personalDetailItem.getGender().equalsIgnoreCase("")) {
                String gender = personalDetailItem.getGender();
                if (gender.equalsIgnoreCase("1") || gender.substring(0, 1).toUpperCase().equalsIgnoreCase("M")) {
                    kycgenderTV.setText("Male");
                } else if (gender.equalsIgnoreCase("2") || gender.substring(0, 1).toUpperCase().equalsIgnoreCase("F")) {
                    kycgenderTV.setText("Female");
                } else {
                    kycgenderTV.setText("Other");
                }
            }

        }
        if (docsListItem != null) {
            if (docsListItem.getName() != null) {
                nameTV.setText(docsListItem.getName());
            }
            if (docsListItem.getDistrict_name_english() != null) {
                distTV.setText(docsListItem.getDistrict_name_english());
            }
            if (docsListItem.getState_name_english() != null) {
                stateTV.setText(docsListItem.getState_name_english());
            }

            if (docsListItem.getPincode() != null) {
                pincodeTV.setText(docsListItem.getPincode());
            }

            if(docsListItem.getVillage_name_english()!=null){
                villageTV.setText(docsListItem.getVillage_name_english());
            }
            if (docsListItem.getGenderid() != null) {
                if (docsListItem.getGenderid().equalsIgnoreCase("1")
                        || docsListItem.getGenderid().substring(0, 1).toUpperCase().equalsIgnoreCase("M")) {
                    genderTV.setText("Male");
                } else if (docsListItem.getGenderid().equalsIgnoreCase("2")
                        || docsListItem.getGenderid().substring(0, 1).toUpperCase().equalsIgnoreCase("F")) {
                    genderTV.setText("Female");
                } else {
                    genderTV.setText("Other");
                }
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

        fetchScoreBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getNameMatchScore();
            }
        });
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

    private void getNameMatchScore() {
        requestModel = new GetTotalScoreRequestModel();
        //secc data
        requestModel.setStrName1(nameTV.getText().toString().trim());
        requestModel.setStrState1(stateTV.getText().toString().trim());
        requestModel.setStrDistrict1(distTV.getText().toString().trim());
        requestModel.setChGender1(genderTV.getText().toString().trim());
        requestModel.setnAge1(ageTV.getText().toString().trim());
        if (docsListItem != null && docsListItem.getVillage_name_english() != null) {
            requestModel.setStrVillage1(docsListItem.getVillage_name_english().trim());
        } else {
            requestModel.setStrVillage1("");
        }
        if (docsListItem != null && docsListItem.getBlock_name_english() != null) {
            requestModel.setStrSubDistrict1(docsListItem.getBlock_name_english().trim());
        } else {
            requestModel.setStrSubDistrict1("");

        }
        //kyc data

        requestModel.setStrName2(kycNameTV.getText().toString());
        requestModel.setStrState2(kycstateTV.getText().toString());
        requestModel.setStrDistrict2(kycdistTV.getText().toString());
        requestModel.setChGender2(kycgenderTV.getText().toString());
        requestModel.setnAge2(kycageTV.getText().toString());
        if (personalDetailItem != null && personalDetailItem.getVtcBen()!=null && !personalDetailItem.getVtcBen().equalsIgnoreCase("")) {
            requestModel.setStrVillage2(personalDetailItem.getVtcBen().trim());
        }else {
            requestModel.setStrVillage2("");
        }

        if(personalDetailItem!=null && personalDetailItem.getSubDistrictBen()!=null && !personalDetailItem.getSubDistrictBen().equalsIgnoreCase("")){
            requestModel.setStrSubDistrict2(personalDetailItem.getSubDistrictBen().trim());
        }else {
            requestModel.setStrSubDistrict2("");

        }


        //request=new NameMatchScoreModelRequest();
        //request.setFirstName(nameTV.getText().toString());
        //request.setSecondName(kycNameTV.getText().toString());


        TaskListener taskListener = new TaskListener() {

            @Override
            public void execute() {
                verifierLoginResponse = VerifierLoginResponse.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                        AppConstant.VERIFIER_CONTENT, context));
                String request1 = requestModel.serialize();
                HashMap<String, String> response = null;
                try {
                    response = CustomHttp.httpPostWithTokken(AppConstant.GET_NAME_MATCH_SCORE, request1, AppConstant.AUTHORIZATION, verifierLoginResponse.getAuthToken());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String familyResponse = response.get("response");
                if (familyResponse != null) {
                    matchResponse = MatchScoreResponse.create(familyResponse);
                }
            }

            @Override
            public void updateUI() {
                if (matchResponse != null) {
                    if (matchResponse.isStatus()) {
                        if (matchResponse.getErrorCode() == null) {
                            if (matchResponse.getResult().getResult() != null) {
                                Log.d("TAG", "Match Score : " + matchResponse.getResult().getResult());
                                showConfirmationDialog(matchResponse.getResult().getResult());
                            } else {
                                CustomAlert.alertWithOk(context, matchResponse.getErrorMessage());
                            }
                        }
                    }else if (matchResponse.getErrorCode().equalsIgnoreCase(AppConstant.SESSION_EXPIRED) ||
                            matchResponse.getErrorCode().equalsIgnoreCase(AppConstant.INVALID_TOKEN)) {
                        Intent intent = new Intent(context, LoginActivity.class);
                        CustomAlert.alertWithOkLogout(context, matchResponse.getErrorMessage(), intent);
                    }else {
                        CustomAlert.alertWithOk(context, "Internal Server error");

                    }
                } else {
                    CustomAlert.alertWithOk(context, "Internal Server error");
                }
            }
        };

        if (asyncTask != null) {
            asyncTask.cancel(true);
            asyncTask = null;
        }

        asyncTask = new CustomAsyncTask(taskListener, "Please wait...", context);
        asyncTask.execute();

    }

    private void showConfirmationDialog(final String matchPercentage) {
        dialog = new AlertDialog.Builder(context).create();
        LayoutInflater factory = LayoutInflater.from(context);
        View alertView = factory.inflate(R.layout.confirmation_dialog, null);
        //View alertView = factory.inflate(R.layout.opt_auth_layout, null);
        dialog.setView(alertView);
        dialog.setCancelable(false);
        Button confirmBT = (Button) alertView.findViewById(R.id.confirmBT);
        Button declineBT = (Button) alertView.findViewById(R.id.declineBT);
        Button cancelBT = (Button) alertView.findViewById(R.id.cancelBT);
        TextView confirmTV = (TextView) alertView.findViewById(R.id.confirmTV);
        confirmTV.setText("Beneficiary name match score is " + matchPercentage + "%");
        confirmBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameMatchScore = matchPercentage;
                Intent data = new Intent();
                data.putExtra("matchScore", nameMatchScore);
                data.putExtra(AppConstant.MATCH_SCORE_STATUS, AppConstant.MATCH_SCORE_STATUS_CONFIRM);
                setResult(4, data);
                activity.finish();
            }
        });
        declineBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameMatchScore = matchPercentage;
                Intent data = new Intent();
                data.putExtra("matchScore", nameMatchScore);
                data.putExtra(AppConstant.MATCH_SCORE_STATUS, AppConstant.MATCH_SCORE_STATUS_REJECT);
                setResult(4, data);
                activity.finish();
            }
        });
        cancelBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                ;
            }
        });
        dialog.show();

    }
}
