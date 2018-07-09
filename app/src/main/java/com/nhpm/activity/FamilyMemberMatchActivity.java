package com.nhpm.activity;

import android.app.Activity;
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
import com.nhpm.Models.request.FamilyMatchScoreRequestModel;
import com.nhpm.Models.response.MatchScoreResponse;
import com.nhpm.Models.response.verifier.VerifierLoginResponse;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;
import com.nhpm.fragments.FamilyDetailsFragment;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by SUNAINA on 21-06-2018.
 */

public class FamilyMemberMatchActivity extends BaseActivity {
    private Context context;
    private FamilyMemberMatchActivity activity;
    private OldFamilyAdapter oldMemberAdapter;
    private FamilyAdapter adapter;
    private RecyclerView memberRecycle, oldMemberRecycle;
    private String familyMatchScore = "";
    private Button confirmBTN, cancelBT, declineBT,fetchScoreBT;
    private ArrayList<FamilyMemberModel> familyMemberFromSecc, familyMemberFromFamilyCard;
    private FamilyMatchScoreRequestModel requestModel;
    private CustomAsyncTask asyncTask;
    private VerifierLoginResponse verifierLoginResponse;
    private MatchScoreResponse matchResponse;

    private AlertDialog dialog;
    private ImageView backIV;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        activity = this;
        setContentView(R.layout.activity_family_member_match);
        setupScreen();
    }

    private void setupScreen() {
        verifierLoginResponse = VerifierLoginResponse.create(
                ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.VERIFIER_CONTENT, context));
        familyMemberFromSecc = (ArrayList<FamilyMemberModel>) getIntent().getSerializableExtra("Old_Members");
        familyMemberFromFamilyCard = (ArrayList<FamilyMemberModel>) getIntent().getSerializableExtra("Family_Card_Members");
        backIV = (ImageView) findViewById(R.id.back);
        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                rightTransition();
            }
        });
        memberRecycle = (RecyclerView) findViewById(R.id.memberRecycle);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        memberRecycle.setLayoutManager(layoutManager);
        memberRecycle.setItemAnimator(new DefaultItemAnimator());

        oldMemberRecycle = (RecyclerView) findViewById(R.id.oldMemberRecycle);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(context);
        oldMemberRecycle.setLayoutManager(layoutManager1);
        oldMemberRecycle.setItemAnimator(new DefaultItemAnimator());



        fetchScoreBT = (Button) findViewById(R.id.fetchScoreBT);
        confirmBTN = (Button) findViewById(R.id.tryAgainBT);
        cancelBT = (Button) findViewById(R.id.cancelBT);
        declineBT = (Button) findViewById(R.id.declineBT);

        if (familyMemberFromSecc != null) {
            oldMemberRefreshList(familyMemberFromSecc);
        }

        if (familyMemberFromFamilyCard != null) {
            refreshList(familyMemberFromFamilyCard);
        }

        confirmBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                familyMatchScore = "80";
                Intent data = new Intent();
                data.putExtra("matchScore", familyMatchScore);
                setResult(4, data);
                activity.finish();

            }
        });


        declineBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                familyMatchScore = "0";
                Intent data = new Intent();
                data.putExtra("matchScore", familyMatchScore);
                setResult(4, data);
                activity.finish();


            }
        });
        cancelBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent data = new Intent();
                data.putExtra("matchScore", familyMatchScore);
                setResult(4, data);
                activity.finish();
            }
        });

        fetchScoreBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFamilyMatchScore();
            }
        });
    }

    public void callFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragContainer, fragment);
            fragmentTransaction.commit();
        }
    }

    private void refreshList(ArrayList<FamilyMemberModel> familyMembersList) {
        if (familyMembersList != null) {
            adapter = new FamilyAdapter(context, familyMembersList);
            memberRecycle.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    private void oldMemberRefreshList(ArrayList<FamilyMemberModel> familyMembersList) {
        if (familyMembersList != null) {
            oldMemberAdapter = new OldFamilyAdapter(context, familyMembersList);
            oldMemberRecycle.setAdapter(oldMemberAdapter);
            oldMemberAdapter.notifyDataSetChanged();
        }
    }

    private class FamilyAdapter extends RecyclerView.Adapter<FamilyAdapter.MyViewHolder> {

        View view;
        private ArrayList<FamilyMemberModel> dataSet;


        public class MyViewHolder extends RecyclerView.ViewHolder {
            RelativeLayout menuLayout;
            ImageView settings;
            TextView nameTV, genderTV, ageTV, pincodeTV;


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
        public FamilyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.member_data_row, parent, false);
            FamilyAdapter.MyViewHolder myViewHolder = new FamilyAdapter.MyViewHolder(view);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(final FamilyAdapter.MyViewHolder holder, final int listPosition) {


            final FamilyMemberModel item = dataSet.get(listPosition);
            holder.menuLayout.setVisibility(View.GONE);
            holder.settings.setVisibility(View.GONE);
            holder.nameTV.setText(item.getName());
            if (item.getGenderid() != null) {
                if (item.getGenderid().equalsIgnoreCase("1")) {
                    holder.genderTV.setText("Male");
                } else if (item.getGenderid().equalsIgnoreCase("2")) {
                    holder.genderTV.setText("Female");
                } else {
                    holder.genderTV.setText("Other");
                }
            }
            if (item.getDob() != null) {
                String currentDate = DateTimeUtil.currentDate("dd MM yyyy");
                Log.d("current date", currentDate);
                String currentYear = currentDate.substring(6, 10);
                Log.d("current year", currentYear);
                int age = Integer.parseInt(currentYear) - Integer.parseInt(item.getDob());
                holder.ageTV.setText(age + "");
            }
            if (item.getPincode() != null) {
                holder.pincodeTV.setText(item.getPincode());
            }


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
            TextView nameTV, pincodeTV, genderTV, ageTV;


            public MyViewHolder(final View itemView) {
                super(itemView);
                nameTV = (TextView) itemView.findViewById(R.id.nameTV);
                pincodeTV = (TextView) itemView.findViewById(R.id.pincodeTV);
                genderTV = (TextView) itemView.findViewById(R.id.genderTV);
                ageTV = (TextView) itemView.findViewById(R.id.ageTV);
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
        public OldFamilyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.member_secc_data, parent, false);
            OldFamilyAdapter.MyViewHolder myViewHolder = new OldFamilyAdapter.MyViewHolder(view);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(final OldFamilyAdapter.MyViewHolder holder, final int listPosition) {


            final FamilyMemberModel item = dataSet.get(listPosition);
            holder.menuLayout.setVisibility(View.GONE);
            holder.settings.setVisibility(View.GONE);
            if (item.getName() != null) {
                holder.nameTV.setText(item.getName());
            }
            if (item.getPincode() != null) {
                holder.pincodeTV.setText(item.getPincode());
            }

            String gender = "", address = "";

            if (item.getGenderid() != null) {
                if (item.getGenderid().equalsIgnoreCase("1")) {
                    gender = "Male";
                } else if (item.getGenderid().equalsIgnoreCase("2")) {
                    gender = "Female";
                } else {
                    gender = "Other";
                }
                holder.genderTV.setText(gender);
            }

            String yob = "";
            if (item.getDob() != null) {
                if (item.getDob() != null && item.getDob().length() > 4) {
                    yob = item.getDob().substring(0, 4);
                } else {
                    yob = item.getDob();
                }

                String currentYear = DateTimeUtil.currentDate(AppConstant.DATE_FORMAT);
                currentYear = currentYear.substring(0, 4);
                int age = Integer.parseInt(currentYear) - Integer.parseInt(yob);
                holder.ageTV.setText(age + "");

            }


        }

        @Override
        public int getItemCount() {
            return dataSet.size();
        }


    }

    private void getFamilyMatchScore() {
        String seccMemberList = "", kycMemberList = "";
        for (FamilyMemberModel item : familyMemberFromSecc) {
            String name = item.getName();
            seccMemberList = seccMemberList + name + "; ";
            Log.d("name secc:", seccMemberList);
        }

        for (FamilyMemberModel item : familyMemberFromFamilyCard) {
            String name = item.getName();
            kycMemberList = kycMemberList + name + "; ";
            Log.d("name kyc:", kycMemberList);
        }

        requestModel = new FamilyMatchScoreRequestModel();
        requestModel.setStrFamilyNames1(seccMemberList);
        requestModel.setStrFamilyNames2(kycMemberList);


        TaskListener taskListener = new TaskListener() {
            @Override
            public void execute() {
                String request = requestModel.serialize();
                HashMap<String, String> response = null;
                try {
                    response = CustomHttp.httpPostWithTokken(AppConstant.GET_FAMILY_MATCH_SCORE, request, AppConstant.AUTHORIZATION, verifierLoginResponse.getAuthToken());
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
                if(matchResponse!=null){
                    if(matchResponse.isStatus()){
                        if(matchResponse.getErrorCode()==null) {
                            if (matchResponse.getResult().getResult() != null) {
                                Log.d("TAG", "Match Score : " + matchResponse.getResult().getResult());
                                showConfirmationDialog(matchResponse.getResult().getResult());
                            }
                        }else if(matchResponse.getErrorCode().equalsIgnoreCase(AppConstant.SESSION_EXPIRED) ||
                                matchResponse.getErrorCode().equalsIgnoreCase(AppConstant.INVALID_TOKEN)){
                            Intent intent = new Intent(context, LoginActivity.class);
                            CustomAlert.alertWithOkLogout(context, matchResponse.getErrorMessage(), intent);
                        }else {
                            CustomAlert.alertWithOk(context,matchResponse.getErrorMessage());
                        }
                    }else if(matchResponse.getErrorCode().equalsIgnoreCase(AppConstant.SESSION_EXPIRED) ||
                            matchResponse.getErrorCode().equalsIgnoreCase(AppConstant.INVALID_TOKEN)){
                        Intent intent = new Intent(context, LoginActivity.class);
                        CustomAlert.alertWithOkLogout(context, matchResponse.getErrorMessage(), intent);
                    }
                }else{
                    CustomAlert.alertWithOk(context,"Internal Server error");

                }
            }
        };

        if (asyncTask != null) {
            asyncTask.cancel(true);
            asyncTask = null;
        }

        asyncTask = new CustomAsyncTask(taskListener, "Please wait..", context);
        asyncTask.execute();
    }

    private void showConfirmationDialog(final String matchPercentage){
        dialog = new AlertDialog.Builder(context).create();
        LayoutInflater factory = LayoutInflater.from(context);
        View alertView = factory.inflate(R.layout.confirmation_dialog, null);
        //View alertView = factory.inflate(R.layout.opt_auth_layout, null);
        dialog.setView(alertView);
        dialog.setCancelable(false);
        Button confirmBT=(Button) alertView.findViewById(R.id.confirmBT);
        Button declineBT=(Button) alertView.findViewById(R.id.declineBT);
        Button cancelBT=(Button) alertView.findViewById(R.id.cancelBT);
        TextView confirmTV=(TextView)alertView.findViewById(R.id.confirmTV);
        confirmTV.setText("Beneficiary name match score is "+matchPercentage+"%");
        confirmBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                familyMatchScore = matchPercentage;
                Intent data = new Intent();
                data.putExtra("matchScore", familyMatchScore);
                data.putExtra(AppConstant.MATCH_SCORE_STATUS,AppConstant.MATCH_SCORE_STATUS_CONFIRM);

                setResult(4, data);
                activity.finish();
            }
        });
        declineBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                familyMatchScore = matchPercentage;
                Intent data = new Intent();
                data.putExtra("matchScore", familyMatchScore);
                data.putExtra(AppConstant.MATCH_SCORE_STATUS,AppConstant.MATCH_SCORE_STATUS_CONFIRM);

                setResult(4, data);
                activity.finish();
            }
        });
        cancelBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();;
            }
        });
        dialog.show();

    }
}
