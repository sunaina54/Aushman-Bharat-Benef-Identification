package com.nhpm.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.customComponent.CustomAlert;
import com.customComponent.CustomAsyncTask;
import com.customComponent.TaskListener;
import com.customComponent.utility.CustomHttp;
import com.customComponent.utility.ProjectPrefrence;
import com.nhpm.Models.request.LogRequestItem;
import com.nhpm.Models.request.LogRequestModel;
import com.nhpm.Models.request.MobileRationRequestModel;
import com.nhpm.Models.request.SaveLoginTransactionRequestModel;
import com.nhpm.Models.request.ValidateUrnRequestModel;
import com.nhpm.Models.response.MobileSearchResponseItem;
import com.nhpm.Models.response.MobileSearchResponseModel;
import com.nhpm.Models.response.SaveLoginTransactionResponseModel;
import com.nhpm.Models.response.URNResponseItem;
import com.nhpm.Models.response.URNResponseModel;
import com.nhpm.Models.response.master.StateItem;
import com.nhpm.Models.response.verifier.VerifierLoginResponse;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;
import com.nhpm.Utility.AppUtility;
import com.nhpm.fragments.BeneficiaryFamilySearchFragment;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by SUNAINA on 31-05-2018.
 */

public class FamilyListByMobileActivity extends BaseActivity {

    private Context context;
    private String familyResponse;
    private CustomAsyncTask customAsyncTask;
    private MobileSearchResponseModel familyListResponseModel;
    private RecyclerView searchListRV;
    private TextView headerTV, noMemberTV;
    private ImageView backIV;
    private CustomAdapter adapter;
    //private ValidateUrnRequestModel validateUrnRequestModel;
    private MobileRationRequestModel mobileRationRequestModel;
    private ProgressBar mProgressBar;
    private LinearLayout noMemberLL;
    private FamilyListByMobileActivity activity;
    private StateItem selectedStateItem;
    private VerifierLoginResponse verifierLoginResp;
    private LogRequestItem logRequestItem;
    private ArrayList<MobileSearchResponseItem> mobileSearchResponseItem;
    private LogRequestModel logRequestModel;

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
        logRequestItem = LogRequestItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.LOG_REQUEST, context));

        headerTV.setText("Family Data" + " by " + AppUtility.searchTitleHeader + " (" + selectedStateItem.getStateName() + ")");
        noMemberLL = (LinearLayout) findViewById(R.id.noMemberLL);
        noMemberLL.setVisibility(View.VISIBLE);
        noMemberTV = (TextView) findViewById(R.id.noMemberTV);
        logRequestModel = LogRequestModel.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SAVE_LOG_REQUEST, context));

        AppUtility.navigateToHome(context, activity);
        //mobileRationRequestModel = (MobileRationRequestModel) getIntent().getSerializableExtra("SearchParam");
        mobileSearchResponseItem = (ArrayList<MobileSearchResponseItem>) getIntent().getSerializableExtra("SearchByMobileRation");
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
            if (mobileSearchResponseItem != null && mobileSearchResponseItem.size() > 0) {
                refreshMembersList(mobileSearchResponseItem);
            }
        } else {
            CustomAlert.alertWithOk(context, context.getResources().getString(R.string.internet_connection_msg));
        }
    }


    private void familyListData() {

        TaskListener taskListener = new TaskListener() {
            @Override
            public void execute() {
                try {

                    String request = mobileRationRequestModel.serialize();
                    String url = AppConstant.SEARCH_BY_MOBILE_RATION;
                    // HashMap<String, String> response = CustomHttp.httpPost(AppConstant.SEARCH_BY_MOBILE_RATION, request);
                    HashMap<String, String> response = CustomHttp.httpPostWithTokken(AppConstant.SEARCH_BY_MOBILE_RATION, request, AppConstant.AUTHORIZATION, verifierLoginResp.getAuthToken());

                    familyResponse = response.get("response");


                    if (familyResponse != null) {
                        if (logRequestItem == null) {
                            logRequestItem = new LogRequestItem();
                        }
                        logRequestItem.setOperatorinput(request);
                        familyListResponseModel = new MobileSearchResponseModel().create(familyResponse);

                        logRequestItem.setOperatoroutput(familyListResponseModel.serialize());
                        ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.LOG_REQUEST, logRequestItem.serialize(), context);
                        try {
                            SaveLoginTransactionRequestModel logTransReq = new SaveLoginTransactionRequestModel();
                            logTransReq.setCreated_by(verifierLoginResp.getAadhaarNumber());
                            HashMap<String, String> responseTid = CustomHttp.httpPost(AppConstant.SAVE_LOGIN_TRANSACTION, logTransReq.serialize());
                            SaveLoginTransactionResponseModel responseModel = SaveLoginTransactionResponseModel.create(responseTid.get("response"));
                            ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, "logTrans", responseModel.serialize(), context);
                            BeneficiaryFamilySearchFragment.sequence = 0;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }

            }

            @Override
            public void updateUI() {
                noMemberLL.setVisibility(View.GONE);
                searchListRV.setVisibility(View.VISIBLE);
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

    }

    private void refreshMembersList(ArrayList<MobileSearchResponseItem> docsListItems) {

        adapter = new CustomAdapter(docsListItems);
        searchListRV.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
        private ArrayList<MobileSearchResponseItem> mDataset;

        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView familyStatusTV, mobileTV, shhCodeTV, ahlHHIdTV, stateTV, distTV, villageTV,
                    blockNameTV, stateCodeTV, distCodeTV, blockCodeTV, villageCodeTV, rationTV,rsbyADCDTV,msbyTV;
            private LinearLayout familyItemLL, rsbyLL, msbyLL;


            public ViewHolder(View v) {
                super(v);
                familyStatusTV = (TextView) v.findViewById(R.id.familyStatusTV);
                stateTV = (TextView) v.findViewById(R.id.stateTV);
                distTV = (TextView) v.findViewById(R.id.distTV);
                villageTV = (TextView) v.findViewById(R.id.villageTV);
                stateCodeTV = (TextView) v.findViewById(R.id.stateCodeTV);
                mobileTV = (TextView) v.findViewById(R.id.mobileTV);
                shhCodeTV = (TextView) v.findViewById(R.id.shhCodeTV);
                ahlHHIdTV = (TextView) v.findViewById(R.id.ahlHHIdTV);
                distCodeTV = (TextView) v.findViewById(R.id.distCodeTV);
                blockNameTV = (TextView) v.findViewById(R.id.blockNameTV);
                blockCodeTV = (TextView) v.findViewById(R.id.blockCodeTV);
                villageCodeTV = (TextView) v.findViewById(R.id.villageCodeTV);
                rsbyADCDTV = (TextView) v.findViewById(R.id.rsbyADCDTV);
                msbyTV = (TextView) v.findViewById(R.id.msbyTV);
                rationTV = (TextView) v.findViewById(R.id.rationTV);
                familyItemLL = (LinearLayout) v.findViewById(R.id.familyItemLL);
                rsbyLL = (LinearLayout) v.findViewById(R.id.rsbyLL);
                msbyLL = (LinearLayout) v.findViewById(R.id.msbyLL);


            }
        }


        public void add(int position, MobileSearchResponseItem item) {
            mDataset.add(position, item);
            notifyItemInserted(position);
        }

        public void remove(String item) {
            int position = mDataset.indexOf(item);
            mDataset.remove(position);
            notifyItemRemoved(position);
        }

        public void updateData(ArrayList<MobileSearchResponseItem> itemList) {
            mDataset.clear();
            mDataset.addAll(itemList);
            notifyDataSetChanged();
        }

        public CustomAdapter(ArrayList<MobileSearchResponseItem> myDataset) {
            mDataset = myDataset;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent,
                                             int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.family_list_item_by_mobile_ration, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            final MobileSearchResponseItem item = mDataset.get(position);
            holder.rsbyLL.setVisibility(View.GONE);
            holder.msbyLL.setVisibility(View.GONE);

            if (item.getFamily_status() != null) {
                holder.familyStatusTV.setText(item.getFamily_status());
            }

            if (item.getMobile_no() != null) {
                holder.mobileTV.setText(item.getMobile_no());
            }
            if (item.getShh_code() != null) {
                holder.shhCodeTV.setText(item.getShh_code());
            }
            if (item.getAhl_hh_id() != null) {
                holder.ahlHHIdTV.setText(item.getAhl_hh_id());

            }
            if (item.getStateName() != null) {
                holder.stateTV.setText(item.getStateName());
            }
            if (item.getStateCode() != null) {
                holder.stateCodeTV.setText(item.getStateCode());
            }
            if (item.getDistrictName() != null) {
                holder.distTV.setText(item.getDistrictName());
            }
            if (item.getDistrict_code() != null) {
                holder.distCodeTV.setText(item.getDistrict_code());
            }
            if (item.getBlockName() != null) {
                holder.blockNameTV.setText(item.getBlockName());
            }
            if (item.getBlock_code() != null) {
                holder.blockCodeTV.setText(item.getBlock_code());
            }
            if (item.getVilageName() != null) {
                holder.villageTV.setText(item.getVilageName());
            }
            if (item.getVillage_code() != null) {
                holder.villageCodeTV.setText(item.getVillage_code());
            }
            if (item.getRation_card() != null) {
                holder.rationTV.setText(item.getRation_card());
            }

            if(item.getRsbyAdcdNo()!=null && !item.getRsbyAdcdNo().equalsIgnoreCase("")){
                holder.rsbyLL.setVisibility(View.VISIBLE);
                holder.rsbyADCDTV.setText(item.getRsbyAdcdNo());
            }

            if(item.getMsbyAdcdNo()!=null && !item.getMsbyAdcdNo().equalsIgnoreCase("")){
                holder.msbyLL.setVisibility(View.VISIBLE);
                holder.msbyTV.setText(item.getMsbyAdcdNo());
            }


            holder.familyItemLL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (mDataset.get(position).getAhl_hh_id() == null || mDataset.get(position).getAhl_hh_id().equalsIgnoreCase("")) {
                        CustomAlert.alertWithOk(context, "HHID is blank. You can't processed data");
                        return;
                    }

                    if (mDataset.get(position).getAhl_hh_id() != null && !mDataset.get(position).getAhl_hh_id().equalsIgnoreCase("")) {
                        logRequestModel.setHhId(mDataset.get(position).getAhl_hh_id());

                        ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SAVE_LOG_REQUEST, logRequestModel.serialize(), context);
                        Intent intent = new Intent(context, FamilyMembersListActivity.class);
                        //intent.putExtra("result", beneficiaryModel);
                        intent.putExtra("hhdNo", mDataset.get(position).getAhl_hh_id());
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
