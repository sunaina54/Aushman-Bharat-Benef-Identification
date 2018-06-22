package com.nhpm.activity;

import android.app.Activity;
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
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;
import com.nhpm.fragments.FamilyDetailsFragment;

import java.util.ArrayList;

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
    private Button confirmBTN, cancelBT, declineBT;
    private ArrayList<FamilyMemberModel> familyMemberFromSecc, familyMemberFromFamilyCard;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        activity = this;
        setContentView(R.layout.activity_family_member_match);
        setupScreen();
    }

    private void setupScreen() {

        familyMemberFromSecc = (ArrayList<FamilyMemberModel>) getIntent().getSerializableExtra("Old_Members");
        familyMemberFromFamilyCard = (ArrayList<FamilyMemberModel>) getIntent().getSerializableExtra("Family_Card_Members");

        memberRecycle = (RecyclerView) findViewById(R.id.memberRecycle);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        memberRecycle.setLayoutManager(layoutManager);
        memberRecycle.setItemAnimator(new DefaultItemAnimator());

        oldMemberRecycle = (RecyclerView) findViewById(R.id.oldMemberRecycle);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(context);
        oldMemberRecycle.setLayoutManager(layoutManager1);
        oldMemberRecycle.setItemAnimator(new DefaultItemAnimator());

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
                Intent data=new Intent();
                data.putExtra("matchScore",familyMatchScore);
                setResult(4,data);
                activity.finish();

            }
        });


        declineBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                familyMatchScore = "0";
                /*Fragment fragment= new FamilyDetailsFragment();
                Bundle args = new Bundle();
                args.putString("familyMatchScore", familyMatchScore);
                fragment.setArguments(args);
                callFragment(fragment);
                activity.finish();*/
                Intent data=new Intent();
                data.putExtra("matchScore",familyMatchScore);
                setResult(4,data);
                activity.finish();


            }
        });
        cancelBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent data=new Intent();
                data.putExtra("matchScore",familyMatchScore);
                setResult(4,data);
                activity.finish();
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
            TextView nameTV;


            public MyViewHolder(final View itemView) {
                super(itemView);
                nameTV = (TextView) itemView.findViewById(R.id.nameTV);
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
            TextView nameTV,pincodeTV,genderTV,ageTV;


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
            if(item.getName()!=null) {
                holder.nameTV.setText(item.getName());
            }
            if(item.getPincode()!=null){
                holder.pincodeTV.setText(item.getPincode());
            }

            String gender = "", address = "";

            if(item.getGenderid()!=null) {
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
            if(item.getDob()!=null){
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
}
