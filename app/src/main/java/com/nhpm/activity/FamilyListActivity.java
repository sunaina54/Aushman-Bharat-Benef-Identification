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
import com.nhpm.Models.FamilyMemberModel;
import com.nhpm.Models.request.FamilyListRequestModel;
import com.nhpm.Models.request.GetMemberDetail;
import com.nhpm.Models.request.GetMemberDetailRequestModel;
import com.nhpm.Models.request.LogRequestItem;
import com.nhpm.Models.request.LogRequestModel;
import com.nhpm.Models.response.DocsListItem;
import com.nhpm.Models.response.FamilyListResponseItem;
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
    private StateItem selectedStateItem;
    private VerifierLoginResponse verifierLoginResp;
    private LogRequestItem logRequestItem;
    private LogRequestModel logRequestModel;
    private int matchCount = 0;
    private Button saveLogBT;
    private AlertDialog alert;
    private boolean logStatus;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        activity = this;
        setContentView(R.layout.activity_search_result);
        setupScreen();
    }


    private void setupScreen() {
        //mProgressBar = (ProgressBar) findViewById(R.id.mProgressBar);
        logRequestModel = LogRequestModel.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SAVE_LOG_REQUEST, context));

        logRequestItem = LogRequestItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.LOG_REQUEST, context));

        headerTV = (TextView) findViewById(R.id.centertext);
        selectedStateItem = StateItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SELECTED_STATE_SEARCH, context));
        verifierLoginResp = VerifierLoginResponse.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.VERIFIER_CONTENT, context));
        headerTV.setText("Family Data" + " (" + selectedStateItem.getStateName() + ")");
        noMemberLL = (LinearLayout) findViewById(R.id.noMemberLL);
        noMemberLL.setVisibility(View.VISIBLE);
        noMemberTV = (TextView) findViewById(R.id.noMemberTV);
        saveLogBT=(Button)findViewById(R.id.saveLogBT);

        AppUtility.navigateToHome(context, activity);
        familyListRequestModel = (FamilyListRequestModel) getIntent().getSerializableExtra("SearchParam");
        backIV = (ImageView) findViewById(R.id.back);
        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                rightTransition();
            }
        });
        saveLogBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //saveLogData();
                showAlert();
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

    private void showAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getResources().getString(com.customComponent.R.string.Alert));

        builder.setMessage("Do you want send the search log ?")
                .setCancelable(false)
                .setNegativeButton(context.getResources().getString(R.string.No), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        alert.dismiss();

                    }
                })
                .setPositiveButton(context.getResources().getString(R.string.Yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        saveLogData();
                    }
                });

        alert = builder.create();
        alert.show();

    }
    private void saveLogData() {

        TaskListener taskListener = new TaskListener() {
            @Override
            public void execute() {
                try {
                   // logRequestModel.setCorrectIncorrectFamilyStatus(familyStatus);

                    HashMap<String, String> searchLogAPI = CustomHttp.httpPost(AppConstant.SEARCH_LOG_API, logRequestModel.serialize());
                    String resp=searchLogAPI.get("response");
                    Log.d("TAG"," Search Log Resp : "+resp);
                    logStatus=true;
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void updateUI() {
                        finish();
            }
        };
        if (customAsyncTask != null) {
            customAsyncTask.cancel(true);
            customAsyncTask = null;
        }

        customAsyncTask = new CustomAsyncTask(taskListener, "Please wait", context);
        customAsyncTask.execute();

    }

    private void familyListData() {

        // familyListRequestModel = new FamilyListRequestModel();
        // familyListRequestModel.setName("sumit");
        familyListRequestModel.setUserName("nhps_fvs^1&%mobile");
        familyListRequestModel.setUserPass("ZCbEJyPUlaQXo8fJT2P+5PAKJOs6emRZgdI/w5qkIrN2NqRUQQ3Sdqp+9WbS8P4j");
//familyListRequestModel.setAge("");
        //familyListRequestModel.setAhlblockno("");
        //familyListRequestModel.setBlock_name_english("");
        // familyListRequestModel.setDistrict_code("");
        familyListRequestModel.setResultCount("5");
/*        if (familyListRequestModel.getFathername() == null) {
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
        familyListRequestModel.setSpousenms("");*/
        TaskListener taskListener = new TaskListener() {
            @Override
            public void execute() {
                try {
                    String request = familyListRequestModel.serialize();
                    HashMap<String, String> response = CustomHttp.httpPostWithTokken(AppConstant.SEARCH_FAMILY_LIST, request, AppConstant.AUTHORIZATION, verifierLoginResp.getAuthToken());
                    String familyResponse = response.get("response");

                    if (familyResponse != null && !familyResponse.equalsIgnoreCase("")) {
                        familyListResponseModel = new FamilyListResponseItem().create(familyResponse);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void updateUI() {
                saveLogBT.setVisibility(View.GONE);

                if (familyListResponseModel != null) {

                    if (familyListResponseModel.isStatus()) {
                        if (familyListResponseModel.getResult() != null && familyListResponseModel.getResult().getResponse() != null
                                ) {
                            logRequestItem.setOperatoroutput(familyListResponseModel.serialize());
                            ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.LOG_REQUEST, logRequestItem.serialize(), context);
                            if (familyListResponseModel.getResult().getResponse().getNumFound() != null
                                    && !familyListResponseModel.getResult().getResponse().getNumFound().equalsIgnoreCase("")) {
                                matchCount = Integer.parseInt(familyListResponseModel.getResult().getResponse().getNumFound());
                            }
                            if (matchCount <= 5) {
                                noMemberTV.setText(matchCount + " matches found");
                            } else {
                                saveLogBT.setVisibility(View.VISIBLE);
                                logRequestModel.setResult(matchCount+"");
                                noMemberTV.setText(matchCount + " matches found. Kindly refine your search.");
                            }
                            if (familyListResponseModel.getResult().getResponse().getDocs() != null && familyListResponseModel.getResult().getResponse().getDocs().size() > 0) {
                                if (matchCount <= familyListResponseModel.getResult().getResponse().getDocs().size()) {
                                    try {
                                        refreshMembersList(familyListResponseModel.getResult().getResponse().getDocs());
                                    } catch (Exception e) {
                                        Log.d("TAG", "Exception : " + e.toString());
                                    }
                                } else {
                                    //mProgressBar.setVisibility(View.GONE);
                                    noMemberLL.setVisibility(View.VISIBLE);
                                    noMemberTV.setText(matchCount + " matches found. Kindly refine your search.");
                                }
                            } else {
                                // mProgressBar.setVisibility(View.GONE);
                                noMemberLL.setVisibility(View.VISIBLE);
                                noMemberTV.setText("No Family member found");
                            }

                        }
                    } else if (familyListResponseModel != null &&
                            familyListResponseModel.getErrorCode().equalsIgnoreCase(AppConstant.SESSION_EXPIRED)
                            || familyListResponseModel.getErrorCode().equalsIgnoreCase(AppConstant.INVALID_TOKEN)) {
                        Intent intent = new Intent(context, LoginActivity.class);
                        CustomAlert.alertWithOkLogout(context, familyListResponseModel.getErrorMessage(), intent);

                    }
                } else {
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
            private LinearLayout familyItemLL, spouseLL;


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
                spouseLL = (LinearLayout) v.findViewById(R.id.spouseLL);
                //spouseLL.setVisibility(View.GONE);

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
            final LogRequestModel logRequestModel = LogRequestModel.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SAVE_LOG_REQUEST, context));

            if (item.getName() != null) {
                holder.nameTV.setText(item.getName().trim());
            }
            if (item.getFathername() != null) {
                holder.fatherNameTV.setText(item.getFathername().trim());
            }
            String gender = "";
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
            if (item.getDob() != null && item.getDob().length() > 4) {
                yob = item.getDob().substring(0, 4);
            } else {
                yob = item.getDob();
            }
            holder.ageTV.setText(yob);
            if (item.getMothername() != null) {
                holder.motherNameTV.setText(item.getMothername());
            }
            if (item.getSpousenm() != null) {
                holder.spouseNameTV.setText(item.getSpousenm());
            }
            // holder.spouseNameTV.setText(item.gets);
            if (item.getAhl_tin() != null) {
                holder.ahltinTV.setText(item.getAhl_tin());
            }

            if (item.getHhd_no() != null) {
                holder.hhidTV.setText(item.getHhd_no());
            }

            if (item.getState_name_english() != null) {
                holder.stateTV.setText(item.getState_name_english());
            }
            if (item.getDistrict_name_english() != null) {
                holder.distTV.setText(item.getDistrict_name_english());
            }
            if (item.getVillage_name_english() != null) {
                holder.villageTV.setText(item.getVillage_name_english());
            }
            if (item.getBlock_name_english() != null) {
                holder.blockTV.setText(item.getBlock_name_english());
            }
            if (item.getPincode() != null) {
                holder.pincodeTV.setText(item.getPincode());
            }
            holder.familyItemLL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mDataset.get(position).getHhd_no() == null || mDataset.get(position).getHhd_no().equalsIgnoreCase("")) {
                        CustomAlert.alertWithOk(context, "HHID is blank. You can't process data");
                        return;
                    }
                    if (mDataset.get(position).getHhd_no() != null && !mDataset.get(position).getHhd_no().equalsIgnoreCase("")) {
                        logRequestModel.setResult(matchCount+"");
                        logRequestModel.setAhl_tin(mDataset.get(position).getAhl_tin());
                        logRequestModel.setHhId(mDataset.get(position).getHhd_no());

                        ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SAVE_LOG_REQUEST, logRequestModel.serialize(), context);

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
