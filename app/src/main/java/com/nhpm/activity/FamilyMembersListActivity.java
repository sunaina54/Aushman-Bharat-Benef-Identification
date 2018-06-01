package com.nhpm.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.customComponent.CustomAlert;
import com.customComponent.utility.ProjectPrefrence;
import com.nhpm.Models.response.BeneficiaryListItem;
import com.nhpm.Models.response.BeneficiaryModel;
import com.nhpm.Models.response.verifier.VerifierLocationItem;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by SUNAINA on 23-05-2018.
 */

public class FamilyMembersListActivity extends BaseActivity {

    private Context context;
    private RecyclerView memberListRV;
    private CustomAdapter adapter;
    private ArrayList<BeneficiaryListItem> beneficiaryList;
    private BeneficiaryModel beneficiaryModel;
    private TextView centerText ,familyIdNoTV,familyMembersNoTV;
    private ImageView backIV;
    private Button collectDataBT;
    public static String SELECTED_MEMBER="SELECTED-MEMBER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_famiy_members_list);
        setupScreen();
    }

    private void setupScreen(){
        context = this;
        centerText = (TextView)findViewById(R.id.centertext);
        centerText.setText("Family Members");
        backIV = (ImageView)findViewById(R.id.back);
        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        collectDataBT = (Button) findViewById(R.id.collectDataBT);
        collectDataBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomAlert.alertWithOk(context,"Under development");
            }
        });
        familyMembersNoTV = (TextView) findViewById(R.id.familyMembersNoTV);
        familyIdNoTV = (TextView) findViewById(R.id.familyIdNoTV);
        memberListRV = (RecyclerView) findViewById(R.id.memberListRV);
        memberListRV.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        memberListRV.setLayoutManager(mLayoutManager);
        beneficiaryModel = (BeneficiaryModel) getIntent().getSerializableExtra("result");
        if(beneficiaryModel!=null && beneficiaryModel.getBeneficiaryList()!=null && beneficiaryModel.getBeneficiaryList().size()>0) {
            refreshMembersList(beneficiaryModel.getBeneficiaryList());
            familyMembersNoTV.setText(beneficiaryModel.getBeneficiaryList().size()+"");
            String id = getIntent().getStringExtra("cardNo");
            familyIdNoTV.setText(id);
        }



    }



    private void refreshMembersList(ArrayList<BeneficiaryListItem> beneficiaryList){

        adapter = new CustomAdapter(beneficiaryList);
        memberListRV.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
        private ArrayList<BeneficiaryListItem> mDataset;

        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView nameTV,fatherNameTV,genderTV,ageTV,addressTV;
            private Button collectDataBT,printCardBT;

            public ViewHolder(View v) {
                super(v);
                nameTV = (TextView) v.findViewById(R.id.nameTV);
                fatherNameTV = (TextView) v.findViewById(R.id.fatherNameTV);
                genderTV = (TextView) v.findViewById(R.id.genderTV);
                ageTV = (TextView) v.findViewById(R.id.ageTV);
                addressTV = (TextView) v.findViewById(R.id.addressTV);
                collectDataBT = (Button) v.findViewById(R.id.collectDataBT);
                printCardBT = (Button) v.findViewById(R.id.printCardBT);

            }
        }


        public void add(int position, BeneficiaryListItem item) {
            mDataset.add(position, item);
            notifyItemInserted(position);
        }

        public void remove(String item) {
            int position = mDataset.indexOf(item);
            mDataset.remove(position);
            notifyItemRemoved(position);
        }

        public void updateData(ArrayList<BeneficiaryListItem> itemList) {
            mDataset.clear();
            mDataset.addAll(itemList);
            notifyDataSetChanged();
        }

        public CustomAdapter(ArrayList<BeneficiaryListItem> myDataset) {
            mDataset = myDataset;
        }

        @Override
        public CustomAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                               int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.family_members_item, parent, false);
            CustomAdapter.ViewHolder vh = new CustomAdapter.ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(CustomAdapter.ViewHolder holder, final int position) {
            final BeneficiaryListItem item = mDataset.get(position);
            holder.nameTV.setText(item.getName());
            holder.fatherNameTV.setText(item.getFatherName());
            holder.genderTV.setText(item.getGender());
            holder.ageTV.setText(item.getAge());
            holder.addressTV.setText(item.getAddress());
            holder.collectDataBT.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent= new Intent(context,CollectDataActivity.class);
                   // intent.putExtra("member",item);
                    ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_NAME,SELECTED_MEMBER,item.serialize(),context);
                    startActivity(intent);
                }
            });

        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return mDataset.size();
        }
    }


}
