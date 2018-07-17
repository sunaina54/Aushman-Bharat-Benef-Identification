package com.nhpm.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.nhpm.Models.request.LogRequestModel;
import com.nhpm.Models.request.ValidateUrnRequestModel;
import com.nhpm.Models.response.ADCDDataItem;
import com.nhpm.Models.response.DocsListItem;
import com.nhpm.Models.response.FamilyListResponseItem;
import com.nhpm.Models.response.RSBYDataItem;
import com.nhpm.Models.response.URNResponseItem;
import com.nhpm.Models.response.URNResponseModel;
import com.nhpm.Models.response.master.StateItem;
import com.nhpm.Models.response.verifier.VerifierLoginResponse;
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
    private VerifierLoginResponse verifierLoginResp;
   // private ArrayList<URNResponseItem> urnResponseItem;
    private ArrayList<Object> consolidatedRsbyList;

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
        selectedStateItem = StateItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SELECTED_STATE_SEARCH, context));

        verifierLoginResp = VerifierLoginResponse.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.VERIFIER_CONTENT, context));
        headerTV.setText("Family Data" + " by " + AppUtility.searchTitleHeader + " (" + selectedStateItem.getStateName() + ")");
        noMemberLL = (LinearLayout) findViewById(R.id.noMemberLL);
        noMemberLL.setVisibility(View.VISIBLE);
        noMemberTV = (TextView) findViewById(R.id.noMemberTV);

        AppUtility.navigateToHome(context, activity);
        // validateUrnRequestModel = (ValidateUrnRequestModel) getIntent().getSerializableExtra("SearchParam");
      //  urnResponseItem = (ArrayList<URNResponseItem>) getIntent().getSerializableExtra("SearchByURN");
       consolidatedRsbyList = (ArrayList<Object>) getIntent().getSerializableExtra("SearchByURN");


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
            // familyListData();
            if (consolidatedRsbyList != null && consolidatedRsbyList.size() > 0) {
                refreshMembersList(consolidatedRsbyList);
            }

        } else {
            CustomAlert.alertWithOk(context, context.getResources().getString(R.string.internet_connection_msg));
        }
    }


   /* private void familyListData() {

        TaskListener taskListener = new TaskListener() {
            @Override
            public void execute() {
                try {
                    noMemberLL.setVisibility(View.GONE);
                    searchListRV.setVisibility(View.VISIBLE);
                    String request = validateUrnRequestModel.serialize();
                    String url = AppConstant.SEARCH_BY_MOBILE_RATION;
                    // HashMap<String, String> response = CustomHttp.httpPost(AppConstant.SEARCH_BY_MOBILE_RATION, request);
                    HashMap<String, String> response = CustomHttp.httpPostWithTokken(AppConstant.SEARCH_BY_MOBILE_RATION, request, AppConstant.AUTHORIZATION, verifierLoginResp.getAuthToken());
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
                    } else if (familyListResponseModel != null &&
                            familyListResponseModel.getErrorCode().equalsIgnoreCase(AppConstant.SESSION_EXPIRED)
                            || familyListResponseModel.getErrorCode().equalsIgnoreCase(AppConstant.INVALID_TOKEN)) {
                        Intent intent = new Intent(context, LoginActivity.class);
                        CustomAlert.alertWithOkLogout(context, familyListResponseModel.getErrorMessage(), intent);

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

    }*/

    private void refreshMembersList(ArrayList<Object> docsListItems) {

        adapter = new CustomAdapter(docsListItems);
        searchListRV.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
        private ArrayList<Object> mDataset;

        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView nameTV, relationTV, genderTV, ageTV,
                    motherNameTV, spouseNameTV, memberIdTV, urnNoTV, stateTV, distTV, villageTV,
                    blockTV, pincodeTV, familyIdTV, fatherNameTV;
            private LinearLayout familyItemLL,familyItemAdcdLL;
            private TextView familyStatusTV, mobileTV, shhCodeTV, ahlHHIdTV, stateADCDTV,
                    distADCDTV, villageAdcdTV, blockNameTV,
                    stateCodeTV, distCodeTV, blockCodeTV, villageCodeTV;

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
                fatherNameTV = (TextView) v.findViewById(R.id.fatherNameTV);
                genderTV = (TextView) v.findViewById(R.id.genderTV);
                ageTV = (TextView) v.findViewById(R.id.ageTV);
                familyItemLL = (LinearLayout) v.findViewById(R.id.familyItemLL);




                familyStatusTV = (TextView) v.findViewById(R.id.familyStatusTV);
                mobileTV = (TextView) v.findViewById(R.id.mobileTV);
                shhCodeTV = (TextView) v.findViewById(R.id.shhCodeTV);
                ahlHHIdTV = (TextView) v.findViewById(R.id.ahlHHIdTV);
                stateADCDTV = (TextView) v.findViewById(R.id.stateADCDTV);
                distADCDTV = (TextView) v.findViewById(R.id.distADCDTV);
                villageAdcdTV = (TextView) v.findViewById(R.id.villageAdcdTV);
                blockNameTV = (TextView) v.findViewById(R.id.blockNameTV);
                stateCodeTV = (TextView) v.findViewById(R.id.stateCodeTV);
                distCodeTV = (TextView) v.findViewById(R.id.distCodeTV);
                blockCodeTV = (TextView) v.findViewById(R.id.blockCodeTV);
                villageCodeTV = (TextView) v.findViewById(R.id.villageCodeTV);
                familyItemAdcdLL = (LinearLayout) v.findViewById(R.id.familyItemAdcdLL);


            }
        }


        public void add(int position, Object item) {
            mDataset.add(position, item);
            notifyItemInserted(position);
        }

        public void remove(String item) {
            int position = mDataset.indexOf(item);
            mDataset.remove(position);
            notifyItemRemoved(position);
        }

        public void updateData(ArrayList<Object> itemList) {
            mDataset.clear();
            mDataset.addAll(itemList);
            notifyDataSetChanged();
        }

        public CustomAdapter(ArrayList<Object> myDataset) {
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

            final Object object = mDataset.get(position);
            final LogRequestModel logRequestModel = LogRequestModel.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SAVE_LOG_REQUEST, context));
            holder.familyItemLL.setVisibility(View.GONE);
            holder.familyItemAdcdLL.setVisibility(View.GONE);
            if (object instanceof RSBYDataItem) {
                holder.familyItemLL.setVisibility(View.VISIBLE);
                holder.familyItemAdcdLL.setVisibility(View.GONE);
                final RSBYDataItem item = (RSBYDataItem) object;

                if (item.getFamilyid() != null) {
                    holder.familyIdTV.setText(item.getFamilyid());
                }
                if (item.getEname() != null) {
                    holder.nameTV.setText(item.getEname().trim());
                }


                //  holder.relationTV.setText(item.getRelationName());


                //holder.stateTV.setText(item.getStateName());
                //  holder.distTV.setText(item.getDistrictName());
                if (item.getVillagename() != null) {
                    holder.villageTV.setText(item.getVillagename());
                }

                if (item.getUrn() != null) {
                    holder.urnNoTV.setText(item.getUrn());

                }
                if (item.getMemberid() != null) {
                    holder.memberIdTV.setText(item.getMemberid());
                }

                holder.fatherNameTV.setText(item.getFatherhusbandname());
                String gender = "";
                if (item.getGender() != null) {
                    if (item.getGender().equalsIgnoreCase("M")) {
                        gender = "Male";
                    } else if (item.getGender().equalsIgnoreCase("F")) {
                        gender = "Female";
                    } else {
                        gender = "Other";
                    }
                    holder.genderTV.setText(gender);
                }
                String yob = "";
              /*  if(item.getDob()!=null) {
                    if (item.getDob() != null && item.getDob().length() > 4) {
                        yob = item.getDob().substring(0, 4);
                    } else {
                        yob = item.getDob();
                    }
                    holder.ageTV.setText(yob);
                }
*/
                holder.familyItemLL.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (item.getUrn() == null || item.getUrn().equalsIgnoreCase("")) {
                            CustomAlert.alertWithOk(context, "Urn number is blank. You can't processed data");
                            return;
                        }

                        if (item.getUrn() != null && !item.getUrn().equalsIgnoreCase("")) {
                            logRequestModel.setHhId(item.getUrn());
                            logRequestModel.setAhl_tin(item.getUrn() + "" + item.getMemberid());

                            ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SAVE_LOG_REQUEST, logRequestModel.serialize(), context);

                            Intent intent = new Intent(context, FamilyMembersListActivity.class);
                            //intent.putExtra("result", beneficiaryModel);
                            intent.putExtra("urnNo", item.getUrn());
                            startActivity(intent);

                        }
                    }
                });

            }


            if (object instanceof ADCDDataItem) {
                holder.familyItemLL.setVisibility(View.GONE);
                holder.familyItemAdcdLL.setVisibility(View.VISIBLE);
                final ADCDDataItem item = (ADCDDataItem) object;

                if (item.getFamily_status() != null) {
                    holder.familyStatusTV.setText(item.getFamily_status());
                }

                if (item.getMobile_number() != null) {
                    holder.mobileTV.setText(item.getMobile_number());
                }
                if (item.getShh_code() != null) {
                    holder.shhCodeTV.setText(item.getShh_code());
                }
                if (item.getAhl_hh_id() != null) {
                    holder.ahlHHIdTV.setText(item.getAhl_hh_id());

                }
               /* if (item.getStateName() != null) {
                    holder.stateTV.setText(item.getStateName());
                }*/
                if (item.getState_code() != null) {
                    holder.stateCodeTV.setText(item.getState_code());
                }
               /* if (item.getDistrictName() != null) {
                    holder.distTV.setText(item.getDistrictName());
                }*/
                if (item.getDistrict_code() != null) {
                    holder.distCodeTV.setText(item.getDistrict_code());
                }
               /* if (item.getBlockName() != null) {
                    holder.blockNameTV.setText(item.getBlockName());
                }*/
                if (item.getBlock_code() != null) {
                    holder.blockCodeTV.setText(item.getBlock_code());
                }
                /*if (item.getVilageName() != null) {
                    holder.villageTV.setText(item.getVilageName());
                }*/
                if (item.getVillage_mdds() != null) {
                    holder.villageCodeTV.setText(item.getVillage_mdds());
                }
               /* if (item.getRation_card() != null) {
                    holder.rationTV.setText(item.getRation_card());
                }*/


                holder.familyItemAdcdLL.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (item.getAhl_hh_id() == null || item.getAhl_hh_id().equalsIgnoreCase("")) {
                            CustomAlert.alertWithOk(context, "HHID is blank. You can't processed data");
                            return;
                        }

                        if (item.getAhl_hh_id() != null && !item.getAhl_hh_id().equalsIgnoreCase("")) {
                            logRequestModel.setHhId(item.getAhl_hh_id());
                            ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SAVE_LOG_REQUEST, logRequestModel.serialize(), context);
                            Intent intent = new Intent(context, FamilyMembersListActivity.class);
                            //intent.putExtra("result", beneficiaryModel);
                            intent.putExtra("hhdNo",  item.getAhl_hh_id());
                            startActivity(intent);

                        }
                    }
                });


            }
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return mDataset.size();
        }
    }

   /* private void refreshMembersList(ArrayList<URNResponseItem> docsListItems) {

        adapter = new CustomAdapter(docsListItems);
        searchListRV.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
        private ArrayList<URNResponseItem> mDataset;

        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView nameTV, relationTV, genderTV, ageTV,
                    motherNameTV, spouseNameTV, memberIdTV, urnNoTV, stateTV, distTV, villageTV,
                    blockTV, pincodeTV, familyIdTV, fatherNameTV;
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
                fatherNameTV = (TextView) v.findViewById(R.id.fatherNameTV);
                genderTV = (TextView) v.findViewById(R.id.genderTV);
                ageTV = (TextView) v.findViewById(R.id.ageTV);
                familyItemLL = (LinearLayout) v.findViewById(R.id.familyItemLL);


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
            final LogRequestModel logRequestModel = LogRequestModel.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SAVE_LOG_REQUEST, context));

            holder.familyIdTV.setText(item.getFamilyId());
            holder.nameTV.setText(item.getMemberName().trim());
            holder.relationTV.setText(item.getRelationName());
            holder.stateTV.setText(item.getStateName());
            holder.distTV.setText(item.getDistrictName());
            holder.villageTV.setText(item.getVillageName());

            if (item.getUrnNo() != null) {
                holder.urnNoTV.setText(item.getUrnNo());

            }
            if (item.getMemberId() != null) {
                holder.memberIdTV.setText(item.getMemberId());
            }
            if (item.getMemberId() != null && item.getUrnNo() != null) {
              //  logRequestModel.setAhl_tin(item.getUrnNo() + "" + item.getMemberId());
            }
            holder.fatherNameTV.setText(item.getFatherhusbandname());
            String gender = "";
            if (item.getGender().equalsIgnoreCase("M")) {
                gender = "Male";
            } else if (item.getGender().equalsIgnoreCase("F")) {
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

            holder.familyItemLL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (mDataset.get(position).getUrnNo() == null || mDataset.get(position).getUrnNo().equalsIgnoreCase("")) {
                        CustomAlert.alertWithOk(context, "Urn number is blank. You can't processed data");
                        return;
                    }

                    if (mDataset.get(position).getUrnNo() != null && !mDataset.get(position).getUrnNo().equalsIgnoreCase("")) {
                        logRequestModel.setHhId(mDataset.get(position).getUrnNo());
                        logRequestModel.setAhl_tin(mDataset.get(position).getUrnNo() + "" + mDataset.get(position).getMemberId());

                        ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SAVE_LOG_REQUEST, logRequestModel.serialize(), context);

                        Intent intent = new Intent(context, FamilyMembersListActivity.class);
                        //intent.putExtra("result", beneficiaryModel);
                        intent.putExtra("urnNo", mDataset.get(position).getUrnNo());
                        startActivity(intent);

                    }
                }
            });

            *//*holder.collectDataBT.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent= new Intent(context,CollectDataActivity.class);
                    intent.putExtra("Name",item.getName());
                    startActivity(intent);
                }
            });*//*

        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return mDataset.size();
        }
    }*/
}
