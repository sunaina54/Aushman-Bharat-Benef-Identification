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
import com.nhpm.Models.request.FamilyListRequestModel;
import com.nhpm.Models.response.DocsListItem;
import com.nhpm.Models.response.FamilyListResponseItem;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;
import com.nhpm.Utility.AppUtility;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by SUNAINA on 31-05-2018.
 */

public class FamilyListActivity extends BaseActivity {

    private Context context;
    private CustomAsyncTask customAsyncTask;
    private FamilyListResponseItem familyListResponseModel;
    private RecyclerView searchListRV;
    private TextView headerTV, noMemberTV;
    private ImageView backIV;
    private CustomAdapter adapter;
    private FamilyListRequestModel familyListRequestModel;
    private ProgressBar mProgressBar;
    private LinearLayout noMemberLL;
    private FamilyListActivity activity;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        activity=this;
        setContentView(R.layout.activity_search_result);
        setupScreen();
    }


    private void setupScreen() {
        //mProgressBar = (ProgressBar) findViewById(R.id.mProgressBar);
        headerTV = (TextView) findViewById(R.id.centertext);
        headerTV.setText("Family Data");
        noMemberLL = (LinearLayout) findViewById(R.id.noMemberLL);
        noMemberLL.setVisibility(View.VISIBLE);
        noMemberTV = (TextView) findViewById(R.id.noMemberTV);

        AppUtility.navigateToHome(context,activity);
        familyListRequestModel = (FamilyListRequestModel) getIntent().getSerializableExtra("SearchParam");
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

        // familyListRequestModel = new FamilyListRequestModel();
        // familyListRequestModel.setName("sumit");
        familyListRequestModel.setUserName("nhps_fvs^1&%mobile");
        familyListRequestModel.setUserPass("ZCbEJyPUlaQXo8fJT2P+5PAKJOs6emRZgdI/w5qkIrN2NqRUQQ3Sdqp+9WbS8P4j");
//familyListRequestModel.setAge("");
        familyListRequestModel.setAhlblockno("");
        familyListRequestModel.setBlock_name_english("");
        familyListRequestModel.setDistrict_code("");
        familyListRequestModel.setResultCount("5");
        if (familyListRequestModel.getFathername() == null) {
            familyListRequestModel.setFathername("");
        }
//familyListRequestModel.setGenderid("");
        if (familyListRequestModel.getMothername() == null) {
            familyListRequestModel.setMothername("");
        }
        if (familyListRequestModel.getHho_id() == null) {
            familyListRequestModel.setHho_id("");
        }

        if (familyListRequestModel.getState_name() == null) {
            familyListRequestModel.setState_name("");
        }

        if (familyListRequestModel.getAhlTinno() == null) {
            familyListRequestModel.setAhlTinno("");
        }
        if (familyListRequestModel.getPincode() == null) {
            familyListRequestModel.setPincode("");
        }
        familyListRequestModel.setRural_urban("");
        if (familyListRequestModel.getSpousenm() == null) {
            familyListRequestModel.setSpousenm("");
        }
        if (familyListRequestModel.getState_name() == null) {
            familyListRequestModel.setState_name("");
        }
        if (familyListRequestModel.getDistrict_name() == null) {
            familyListRequestModel.setDistrict_name("");
        }
        if (familyListRequestModel.getVt_name() == null) {
            familyListRequestModel.setVt_name("");
        }
        familyListRequestModel.setState_name_english("");
        familyListRequestModel.setSpousenms("");
        TaskListener taskListener = new TaskListener() {
            @Override
            public void execute() {
                try {
                    String request = familyListRequestModel.serialize();
                    HashMap<String, String> response = CustomHttp.httpPost(AppConstant.SEARCH_FAMILY_LIST, request);
                    String familyResponse = response.get("response");

                    if (familyResponse != null && !familyResponse.equalsIgnoreCase("")) {
                        familyListResponseModel = new FamilyListResponseItem().create(familyResponse);
                    }else {
                        noMemberLL.setVisibility(View.VISIBLE);
                        noMemberTV.setText("Internal Server Error");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void updateUI() {
                if(familyListResponseModel != null) {
                    if (familyListResponseModel.getResponse() != null
                            ) {
                        int matchCount = Integer.parseInt(familyListResponseModel.getResponse().getNumFound());
                        if (matchCount <= 5) {
                            noMemberTV.setText(matchCount + " matches found");
                        } else {
                            noMemberTV.setText(matchCount + " matches found. Kindly refine your search.");
                        }
                        if (familyListResponseModel.getResponse().getDocs() != null && familyListResponseModel.getResponse().getDocs().size() > 0) {
                            //  if (matchCount<=familyListResponseModel.getResponse().getDocs().size()) {
                            try {
                                refreshMembersList(familyListResponseModel.getResponse().getDocs());
                            } catch (Exception e) {
                                Log.d("TAG", "Exception : " + e.toString());
                            }
                        /*}else {
                            //mProgressBar.setVisibility(View.GONE);
                            noMemberLL.setVisibility(View.VISIBLE);
                            noMemberTV.setText(matchCount + " matches found. Kindly refine your search.");
                        }*/
                        } else {
                            // mProgressBar.setVisibility(View.GONE);
                            noMemberLL.setVisibility(View.VISIBLE);
                            noMemberTV.setText("No Family member found");
                        }

                    }
                }else {
                    noMemberLL.setVisibility(View.VISIBLE);
                    noMemberTV.setText("Internal Server Error");
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

    private void refreshMembersList(ArrayList<DocsListItem> docsListItems) {

        adapter = new CustomAdapter(docsListItems);
        searchListRV.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
        private ArrayList<DocsListItem> mDataset;

        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView nameTV, fatherNameTV, genderTV, ageTV,
                    motherNameTV, spouseNameTV, ahltinTV, hhidTV, stateTV, distTV, villageTV,
                    blockTV, pincodeTV;
            private Button collectDataBT, printCardBT;
            private LinearLayout familyItemLL;


            public ViewHolder(View v) {
                super(v);
                nameTV = (TextView) v.findViewById(R.id.nameTV);
                fatherNameTV = (TextView) v.findViewById(R.id.fatherNameTV);
                genderTV = (TextView) v.findViewById(R.id.genderTV);
                ageTV = (TextView) v.findViewById(R.id.ageTV);
                motherNameTV = (TextView) v.findViewById(R.id.motherNameTV);
                spouseNameTV = (TextView) v.findViewById(R.id.spouseNameTV);
                ahltinTV = (TextView) v.findViewById(R.id.ahltinTV);
                hhidTV = (TextView) v.findViewById(R.id.hhidTV);
                stateTV = (TextView) v.findViewById(R.id.stateTV);
                distTV = (TextView) v.findViewById(R.id.distTV);
                villageTV = (TextView) v.findViewById(R.id.villageTV);
                blockTV = (TextView) v.findViewById(R.id.blockTV);
                pincodeTV = (TextView) v.findViewById(R.id.pincodeTV);
                villageTV = (TextView) v.findViewById(R.id.villageTV);
                villageTV = (TextView) v.findViewById(R.id.villageTV);
                collectDataBT = (Button) v.findViewById(R.id.collectDataBT);
                printCardBT = (Button) v.findViewById(R.id.printCardBT);
                familyItemLL = (LinearLayout) v.findViewById(R.id.familyItemLL);

            }
        }


        public void add(int position, DocsListItem item) {
            mDataset.add(position, item);
            notifyItemInserted(position);
        }

        public void remove(String item) {
            int position = mDataset.indexOf(item);
            mDataset.remove(position);
            notifyItemRemoved(position);
        }

        public void updateData(ArrayList<DocsListItem> itemList) {
            mDataset.clear();
            mDataset.addAll(itemList);
            notifyDataSetChanged();
        }

        public CustomAdapter(ArrayList<DocsListItem> myDataset) {
            mDataset = myDataset;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent,
                                             int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.family_list_item, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            final DocsListItem item = mDataset.get(position);
            holder.nameTV.setText(item.getName().trim());
            holder.fatherNameTV.setText(item.getFathername().trim());
            String gender = "";
            if (item.getGenderid().equalsIgnoreCase("1")) {
                gender = "Male";
            } else if (item.getGenderid().equalsIgnoreCase("2")) {
                gender = "Female";
            } else {
                gender = "Other";
            }
            holder.genderTV.setText(gender);
            String yob = "";
            if (item.getDob() != null && item.getDob().length() > 4) {
                yob = item.getDob().substring(0, 4);
            } else {
                yob = item.getDob();
            }
            holder.ageTV.setText(yob);

            holder.motherNameTV.setText(item.getMothername());
            // holder.spouseNameTV.setText(item.gets);
            holder.ahltinTV.setText(item.getAhl_tin());
            holder.hhidTV.setText(item.getHhd_no());
            holder.stateTV.setText(item.getState_name());
            holder.distTV.setText(item.getDistrict_name());
            holder.villageTV.setText(item.getVillage_name_english());
            holder.blockTV.setText(item.getBlock_name_english());
            holder.pincodeTV.setText(item.getPincode());
            holder.familyItemLL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mDataset.get(position).getHhd_no() == null || mDataset.get(position).getHhd_no().equalsIgnoreCase("")) {
                        CustomAlert.alertWithOk(context,"HHID is blank. You can't process data");
                        return;
                    }
                    if (mDataset.get(position).getHhd_no() != null && !mDataset.get(position).getHhd_no().equalsIgnoreCase(""))
                        {
                            Intent intent = new Intent(context, FamilyMembersListActivity.class);
                            //intent.putExtra("result", beneficiaryModel);
                            intent.putExtra("hhdNo", mDataset.get(position).getHhd_no());
                            startActivity(intent);
                        }
                }
            });

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
