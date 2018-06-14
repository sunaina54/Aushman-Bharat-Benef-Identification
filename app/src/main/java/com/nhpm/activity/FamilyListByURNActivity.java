package com.nhpm.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.customComponent.CustomAlert;
import com.customComponent.CustomAsyncTask;
import com.customComponent.TaskListener;
import com.customComponent.utility.CustomHttp;
import com.customComponent.utility.ProjectPrefrence;
import com.nhpm.Models.request.FamilyListRequestModel;
import com.nhpm.Models.request.ValidateUrnRequestModel;
import com.nhpm.Models.response.DocsListItem;
import com.nhpm.Models.response.FamilyListResponseItem;
import com.nhpm.Models.response.URNResponseItem;
import com.nhpm.Models.response.URNResponseModel;
import com.nhpm.Models.response.master.StateItem;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;
import com.nhpm.Utility.AppUtility;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by SUNAINA on 31-05-2018.
 */

public class FamilyListByURNActivity extends BaseActivity {

    private Context context;
    private String familyResponse;
    private CustomAsyncTask customAsyncTask;
    private URNResponseModel familyListResponseModel;
    private RecyclerView searchListRV;
    private TextView headerTV, noMemberTV;
    private ImageView backIV;
    private CustomAdapter adapter;
    private ValidateUrnRequestModel validateUrnRequestModel;
    private ProgressBar mProgressBar;
    private LinearLayout noMemberLL;
    private FamilyListByURNActivity activity;
    private StateItem selectedStateItem;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        activity = this;
        setContentView(R.layout.activity_search_result);
        setupScreen();
    }


    private void setupScreen() {
        //mProgressBar = (ProgressBar) findViewById(R.id.mProgressBar);
        headerTV = (TextView) findViewById(R.id.centertext);
        selectedStateItem = StateItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SELECTED_STATE, context));

        headerTV.setText("Family Data" + " (" + selectedStateItem.getStateName() + ")");
        noMemberLL = (LinearLayout) findViewById(R.id.noMemberLL);
        noMemberLL.setVisibility(View.VISIBLE);
        noMemberTV = (TextView) findViewById(R.id.noMemberTV);

        AppUtility.navigateToHome(context, activity);
        validateUrnRequestModel = (ValidateUrnRequestModel) getIntent().getSerializableExtra("SearchParam");
        backIV = (ImageView) findViewById(R.id.back);
        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                rightTransition();
            }
        });
        searchListRV = (RecyclerView) findViewById(R.id.searchListRV);
        searchListRV.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        searchListRV.setLayoutManager(mLayoutManager);
        if (isNetworkAvailable()) {
            familyListData();
        } else {
            CustomAlert.alertWithOk(context, context.getResources().getString(R.string.internet_connection_msg));
        }
    }


    private void familyListData() {

        TaskListener taskListener = new TaskListener() {
            @Override
            public void execute() {
                try {
                    noMemberLL.setVisibility(View.GONE);
                    searchListRV.setVisibility(View.VISIBLE);
                    String request = validateUrnRequestModel.serialize();
                    String url = AppConstant.VALIDATE_URN;
                    HashMap<String, String> response = CustomHttp.httpPost(AppConstant.VALIDATE_URN, request);
                    familyResponse = response.get("response");


                    if (familyResponse != null) {

                        familyListResponseModel = new URNResponseModel().create(familyResponse);

                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }

            }

            @Override
            public void updateUI() {
                if (familyListResponseModel != null) {
                    if (familyListResponseModel.isStatus()) {
                        if (familyListResponseModel.getUrnResponse() != null) {
                            if (familyListResponseModel.getUrnResponse().size() > 0) {
                                refreshMembersList(familyListResponseModel.getUrnResponse());
                            } else {
                                noMemberLL.setVisibility(View.VISIBLE);
                                noMemberTV.setText("No member found");
                                searchListRV.setVisibility(View.GONE);
                            }
                        }
                    } else {
                        noMemberLL.setVisibility(View.VISIBLE);
                        noMemberTV.setText(familyListResponseModel.getErrorMessage());
                        searchListRV.setVisibility(View.GONE);
                    }
                } else {
                    noMemberLL.setVisibility(View.VISIBLE);
                    noMemberTV.setText("Internal Server Error");
                    searchListRV.setVisibility(View.GONE);
                }
            }
        };
        if (customAsyncTask != null) {
            customAsyncTask.cancel(true);
            customAsyncTask = null;
        }

        customAsyncTask = new CustomAsyncTask(taskListener, "Please wait", context);
        customAsyncTask.execute();

    }

    private void refreshMembersList(ArrayList<URNResponseItem> docsListItems) {

        adapter = new CustomAdapter(docsListItems);
        searchListRV.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
        private ArrayList<URNResponseItem> mDataset;

        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView nameTV, relationTV, genderTV, ageTV,
                    motherNameTV, spouseNameTV, memberIdTV, urnNoTV, stateTV, distTV, villageTV,
                    blockTV, pincodeTV, familyIdTV;
            private LinearLayout familyItemLL;


            public ViewHolder(View v) {
                super(v);
                nameTV = (TextView) v.findViewById(R.id.nameTV);
                stateTV = (TextView) v.findViewById(R.id.stateTV);
                distTV = (TextView) v.findViewById(R.id.distTV);
                villageTV = (TextView) v.findViewById(R.id.villageTV);
                familyIdTV = (TextView) v.findViewById(R.id.familyIdTV);
                relationTV = (TextView) v.findViewById(R.id.relationTV);
                memberIdTV = (TextView) v.findViewById(R.id.memberIdTV);
                urnNoTV = (TextView) v.findViewById(R.id.urnNoTV);


            }
        }


        public void add(int position, URNResponseItem item) {
            mDataset.add(position, item);
            notifyItemInserted(position);
        }

        public void remove(String item) {
            int position = mDataset.indexOf(item);
            mDataset.remove(position);
            notifyItemRemoved(position);
        }

        public void updateData(ArrayList<URNResponseItem> itemList) {
            mDataset.clear();
            mDataset.addAll(itemList);
            notifyDataSetChanged();
        }

        public CustomAdapter(ArrayList<URNResponseItem> myDataset) {
            mDataset = myDataset;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent,
                                             int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.family_list_item_by_urn, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            final URNResponseItem item = mDataset.get(position);
            holder.familyIdTV.setText(item.getFamilyId());
            holder.nameTV.setText(item.getMemberName().trim());
            holder.relationTV.setText(item.getRelationName());
            holder.stateTV.setText(item.getStateName());
            holder.distTV.setText(item.getDistrictName());
            holder.villageTV.setText(item.getVillageName());
            holder.memberIdTV.setText(item.getMemberId());
            holder.urnNoTV.setText(item.getUrnNo());
          /*  holder.familyItemLL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (mDataset.get(position).getHhd_no() == null || mDataset.get(position).getHhd_no().equalsIgnoreCase("")) {
                        CustomAlert.alertWithOk(context, "HHID is blank. You can't processed data");
                        return;
                    }

                    if (mDataset.get(position).getHhd_no() != null && !mDataset.get(position).getHhd_no().equalsIgnoreCase("")) {
                        Intent intent = new Intent(context, FamilyMembersListActivity.class);
                        //intent.putExtra("result", beneficiaryModel);
                        intent.putExtra("hhdNo", mDataset.get(position).getHhd_no());
                        startActivity(intent);
                    }
                }
            });*/

            /*holder.collectDataBT.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent= new Intent(context,CollectDataActivity.class);
                    intent.putExtra("Name",item.getName());
                    startActivity(intent);
                }
            });*/

        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return mDataset.size();
        }
    }
}
