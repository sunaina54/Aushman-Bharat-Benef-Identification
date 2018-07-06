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
import android.widget.TextView;

import com.customComponent.CustomAlert;
import com.customComponent.CustomAsyncTask;
import com.customComponent.TaskListener;
import com.customComponent.utility.CustomHttp;
import com.customComponent.utility.DateTimeUtil;
import com.customComponent.utility.ProjectPrefrence;
import com.nhpm.Models.FamilyMemberModel;
import com.nhpm.Models.request.FamilyListRequestModel;
import com.nhpm.Models.request.GetMemberDetail;
import com.nhpm.Models.request.GetMemberDetailRequestModel;
import com.nhpm.Models.request.ValidateUrnRequestModel;
import com.nhpm.Models.response.BeneficiaryListItem;
import com.nhpm.Models.response.BeneficiaryModel;
import com.nhpm.Models.response.DocsListItem;
import com.nhpm.Models.response.FamilyListResponseItem;
import com.nhpm.Models.response.URNResponseItem;
import com.nhpm.Models.response.URNResponseModel;
import com.nhpm.Models.response.master.StateItem;
import com.nhpm.Models.response.verifier.VerifierLocationItem;
import com.nhpm.Models.response.verifier.VerifierLoginResponse;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;
import com.nhpm.Utility.AppUtility;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by SUNAINA on 23-05-2018.
 */

public class FamilyMembersListActivity extends BaseActivity {

    private Context context;
    private RecyclerView memberListRV;
    private CustomAdapter adapter;
    private ArrayList<BeneficiaryListItem> beneficiaryList;
    private BeneficiaryModel beneficiaryModel;
    private TextView centerText, familyIdNoTV, familyMembersNoTV;
    private ImageView backIV;
    private Button collectDataBT;
    private FamilyListResponseItem familyListResponseModel;
    private URNResponseModel urnResponseModel;
    private String hhdNo = "", urnNo = "";

    public static String SELECTED_MEMBER = "SELECTED-MEMBER";
    private FamilyListRequestModel familyListRequestModel;
    private LinearLayout noMemberLL;
    private CustomAsyncTask customAsyncTask;
    private FamilyMembersListActivity activity;
    private TextView errorTV;
    private StateItem selectedStateItem;
    private GetMemberDetail getMemberDetailResponse;
    private ValidateUrnRequestModel urRequest;
    private FamilyListRequestModel seccRequest;
    private CustomAdapterRSBY adapterRSBY;
    private VerifierLoginResponse verifierLoginResp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_famiy_members_list);
        setupScreen();
    }

    private void setupScreen() {
        context = this;
        activity = this;
        centerText = (TextView) findViewById(R.id.centertext);
        selectedStateItem = StateItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SELECTED_STATE_SEARCH, context));
        verifierLoginResp = VerifierLoginResponse.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.VERIFIER_CONTENT, context));
        centerText.setText("Family Members" + " by " + AppUtility.searchTitleHeader + " (" + selectedStateItem.getStateName() + ")");
        backIV = (ImageView) findViewById(R.id.back);
        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        AppUtility.navigateToHome(context, activity);
        collectDataBT = (Button) findViewById(R.id.collectDataBT);
        errorTV = (TextView) findViewById(R.id.errorTV);
        collectDataBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomAlert.alertWithOk(context, "Under development");
            }
        });
        familyMembersNoTV = (TextView) findViewById(R.id.familyMembersNoTV);
        familyIdNoTV = (TextView) findViewById(R.id.familyIdNoTV);
        memberListRV = (RecyclerView) findViewById(R.id.memberListRV);
        memberListRV.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        memberListRV.setLayoutManager(mLayoutManager);
        hhdNo = getIntent().getStringExtra("hhdNo");

        if (hhdNo != null && !hhdNo.equalsIgnoreCase("")) {
            familyIdNoTV.setText(hhdNo);
            familyListData();
        }

        urnNo = getIntent().getStringExtra("urnNo");
        if (urnNo != null && !urnNo.equalsIgnoreCase("")) {
            familyIdNoTV.setText(urnNo);
            familyListDataForRSBY();
        }
        //beneficiaryModel = (BeneficiaryModel) getIntent().getSerializableExtra("result");
     /*   if(beneficiaryModel!=null && beneficiaryModel.getBeneficiaryList()!=null && beneficiaryModel.getBeneficiaryList().size()>0) {
            refreshMembersList(beneficiaryModel.getBeneficiaryList());
            familyMembersNoTV.setText(beneficiaryModel.getBeneficiaryList().size()+"");
            String id = getIntent().getStringExtra("cardNo");
            familyIdNoTV.setText(id);
        }*/


    }

    private void familyListData() {

        familyListRequestModel = new FamilyListRequestModel();
        familyListRequestModel.setUserName("nhps_fvs^1&%mobile");
        familyListRequestModel.setUserPass("ZCbEJyPUlaQXo8fJT2P+5PAKJOs6emRZgdI/w5qkIrN2NqRUQQ3Sdqp+9WbS8P4j");
        familyListRequestModel.setHho_id(hhdNo);
        familyListRequestModel.setAhlblockno("");
        familyListRequestModel.setRural_urban("");
        familyListRequestModel.setSpousenms("");
        familyListRequestModel.setState_name("");
        familyListRequestModel.setAge("");
        familyListRequestModel.setAhlTinno("");
        familyListRequestModel.setDistrict_code("");
        familyListRequestModel.setPincode("");
        familyListRequestModel.setName("");
        familyListRequestModel.setGenderid("");
        familyListRequestModel.setMothername("");
        familyListRequestModel.setVt_name("");
        familyListRequestModel.setDistrict_name("");
        familyListRequestModel.setSpousenm("");
        familyListRequestModel.setResultCount("100");
        familyListRequestModel.setFathername("");
        familyListRequestModel.setState_name_english("");
        familyListRequestModel.setBlock_name_english("");


        TaskListener taskListener = new TaskListener() {
            @Override
            public void execute() {
                try {
                    String request = familyListRequestModel.serialize();
                    // HashMap<String, String> response = CustomHttp.httpPost(AppConstant.SEARCH_FAMILY_LIST, request);
                    HashMap<String, String> response = CustomHttp.httpPostWithTokken(AppConstant.SEARCH_FAMILY_LIST, request, AppConstant.AUTHORIZATION, verifierLoginResp.getAuthToken());

                    String familyResponse = response.get("response");

                    if (familyResponse != null) {
                        familyListResponseModel = new FamilyListResponseItem().create(familyResponse);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void updateUI() {
                if (familyListResponseModel != null) {
                    if (familyListResponseModel.isStatus()) {
                        if (familyListResponseModel.getResult() != null &&
                                familyListResponseModel.getResult().getResponse() != null) {
                            if(familyListResponseModel.getResult().getResponse().getNumFound()!=null) {
                                int matchCount = Integer.parseInt(familyListResponseModel.getResult().getResponse().getNumFound());
                            }
                            //noMemberTV.setText(matchCount + " matches found. Kindly refine your search.");
                            if (familyListResponseModel.getResult().getResponse().getDocs() != null && familyListResponseModel.getResult().getResponse().getDocs().size() > 0) {
                                //  if (matchCount<=familyListResponseModel.getResponse().getDocs().size()) {
                                try {
                                    familyMembersNoTV.setText(familyListResponseModel.getResult().getResponse().getDocs().size() + "");

                                    refreshMembersList(familyListResponseModel.getResult().getResponse().getDocs());
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
                                //noMemberLL.setVisibility(View.VISIBLE);

                                errorTV.setVisibility(View.VISIBLE);
                                errorTV.setText("No family member found");
                            }
                        }
                    } else if (familyListResponseModel != null &&
                            familyListResponseModel.getErrorCode().equalsIgnoreCase(AppConstant.SESSION_EXPIRED)
                            || familyListResponseModel.getErrorCode().equalsIgnoreCase(AppConstant.INVALID_TOKEN)) {
                        Intent intent = new Intent(context, LoginActivity.class);
                        CustomAlert.alertWithOkLogout(context, familyListResponseModel.getErrorMessage(), intent);

                    }
                } else {
                    errorTV.setVisibility(View.GONE);
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

    private void familyListDataForRSBY() {

        final ValidateUrnRequestModel validateUrnRequestModel = new ValidateUrnRequestModel();
        validateUrnRequestModel.setUrn(urnNo);

        TaskListener taskListener = new TaskListener() {
            @Override
            public void execute() {
                try {

                    String request = validateUrnRequestModel.serialize();
                    String url = AppConstant.SEARCH_BY_MOBILE_RATION;
                    HashMap<String, String> response = CustomHttp.httpPostWithTokken(AppConstant.SEARCH_BY_MOBILE_RATION, request,AppConstant.AUTHORIZATION,verifierLoginResp.getAuthToken());
                    // HashMap<String, String> response = CustomHttp.httpPostWithTokken(AppConstant.VALIDATE_URN, request,AppConstant.AUTHORIZATION,verifierLoginResp.getAuthToken());
                    String familyResponse = response.get("response");
                    if (familyResponse != null) {

                        urnResponseModel = new URNResponseModel().create(familyResponse);

                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }

            }

            @Override
            public void updateUI() {
                if (urnResponseModel != null) {
                    if (urnResponseModel.isStatus()) {
                        if (urnResponseModel.getUrnResponse() != null) {
                            if (urnResponseModel.getUrnResponse().size() > 0) {
                                familyMembersNoTV.setText(urnResponseModel.getUrnResponse().size() + "");

                                refreshMembersListByURN(urnResponseModel.getUrnResponse());
                            } else {
                                errorTV.setVisibility(View.VISIBLE);
                                errorTV.setText("No family member found");
                            }
                        }
                    }else if (urnResponseModel != null &&
                            urnResponseModel.getErrorCode().equalsIgnoreCase(AppConstant.SESSION_EXPIRED)
                            || urnResponseModel.getErrorCode().equalsIgnoreCase(AppConstant.INVALID_TOKEN)) {
                        Intent intent = new Intent(context, LoginActivity.class);
                        CustomAlert.alertWithOkLogout(context, urnResponseModel.getErrorMessage(), intent);

                    } else {
                        errorTV.setVisibility(View.VISIBLE);
                        errorTV.setText("No family member found");
                    }
                } else {
                    errorTV.setVisibility(View.GONE);
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

    private void refreshMembersList(ArrayList<DocsListItem> beneficiaryList) {

        adapter = new CustomAdapter(beneficiaryList);
        memberListRV.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void refreshMembersListByURN(ArrayList<URNResponseItem> beneficiaryList) {

        adapterRSBY = new CustomAdapterRSBY(beneficiaryList);
        memberListRV.setAdapter(adapterRSBY);
        adapterRSBY.notifyDataSetChanged();
    }

    private class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
        private ArrayList<DocsListItem> mDataset;

        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView nameTV, fatherNameTV, genderTV, ageTV, addressTV;
            private Button collectDataBT, printCardBT;

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
        public CustomAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.family_members_item, parent, false);
            CustomAdapter.ViewHolder vh = new CustomAdapter.ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(CustomAdapter.ViewHolder holder, final int position) {
            final DocsListItem item = mDataset.get(position);

            holder.nameTV.setText(item.getName());
            holder.fatherNameTV.setText(item.getFathername());
            String gender = "", address = "";

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

            String currentYear = DateTimeUtil.currentDate(AppConstant.DATE_FORMAT);
            currentYear = currentYear.substring(0, 4);
            int age = Integer.parseInt(currentYear) - Integer.parseInt(yob);
            holder.ageTV.setText(age + "");

            // holder.ageTV.setText(item.getAge());
            if (item != null) {
                if (item.getAddressline1() != null && !item.getAddressline1().trim().
                        equalsIgnoreCase("-") && !item.getAddressline1().equalsIgnoreCase("")) {
                    address = address + item.getAddressline1() + ", ";

                }
                if (item.getAddressline2() != null && !item.getAddressline2().trim()
                        .equalsIgnoreCase("-") && !item.getAddressline2().equalsIgnoreCase("")) {

                    address = address + item.getAddressline2() + ", ";
                }
                if (item.getAddressline3() != null && !item.getAddressline3().trim().
                        equalsIgnoreCase("-") && !item.getAddressline3().equalsIgnoreCase("")) {


                    address = address + item.getAddressline3() + ", ";
                }
                if (item.getAddressline4() != null && !item.getAddressline4().trim().
                        equalsIgnoreCase("-") && !item.getAddressline4().equalsIgnoreCase("")) {


                    address = address + item.getAddressline4();
                }
                holder.addressTV.setText(item.getDistrict_name_english());

            }
            holder.collectDataBT.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                /*    ArrayList<FamilyMemberModel> oldMemList = new ArrayList<>();
                    for (DocsListItem item2 : mDataset) {
                        FamilyMemberModel model = new FamilyMemberModel();
                        model.setName(item2.getName());
                        oldMemList.add(model);
                    }

                    item.setOldMembers(oldMemList);

                    Intent intent = new Intent(context, CollectDataActivity.class);
                    intent.putExtra("member", mDataset.get(position).getName());
                    item.setPersonalDetail(null);
                     *//*if(item.getState_code()!=null && !item.getState_code().equalsIgnoreCase("")) {
                         item.setStatecode(item.getState_code());
                     }*//*
                    Log.d("TAG", "Doc List : " + item.serialize());
                    item.setFamilyDetailsItemModel(null);
                    ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_NAME, SELECTED_MEMBER, item.serialize(), context);
                    Log.d("TAG", "Doc List from shared pref: " + DocsListItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_NAME,
                            SELECTED_MEMBER, context)));
                    startActivity(intent);*/


                    ///// ********** FLOW BY API ************/////

                    final GetMemberDetailRequestModel requestModel = new GetMemberDetailRequestModel();
                    if (item.getAhl_tin() != null && !item.getAhl_tin().equalsIgnoreCase("")) {
                        requestModel.setAhl_tin(item.getAhl_tin());
                    }
                    if (item.getHhd_no() != null && !item.getHhd_no().equalsIgnoreCase("")) {
                        requestModel.setHhd_no(item.getHhd_no());
                    }

                    if (item.getState_code() != null && !item.getState_code().equalsIgnoreCase("")) {
                        requestModel.setStatecode(Integer.parseInt(verifierLoginResp.getStatecode()));
                    }

                    TaskListener taskListener = new TaskListener() {
                        @Override
                        public void execute() {
                            try {
                                String request = requestModel.serialize();
                                HashMap<String, String> response = CustomHttp.httpPostWithTokken(AppConstant.GET_MEMBER_DETAIL, request, AppConstant.AUTHORIZATION, verifierLoginResp.getAuthToken());
                                String familyResponse = response.get("response");

                                if (familyResponse != null) {
                                    getMemberDetailResponse = new GetMemberDetail().create(familyResponse);

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void updateUI() {
                            if (getMemberDetailResponse != null) {
                                if (getMemberDetailResponse.isStatus()) {
                                    if (getMemberDetailResponse.getPersonalDetail().getBenefName() != null &&
                                            !getMemberDetailResponse.getPersonalDetail().getBenefName().equalsIgnoreCase("")) {

                                        Intent intent = new Intent(context, ViewMemberDataActivity.class);
                                        ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_NAME, AppConstant.VIEW_DATA, getMemberDetailResponse.serialize(), context);
                                        startActivity(intent);

                                    } else {
                                        ArrayList<FamilyMemberModel> oldMemList = new ArrayList<>();
                                        for (DocsListItem item2 : mDataset) {
                                            FamilyMemberModel model = new FamilyMemberModel();
                                            model.setName(item2.getName());
                                            model.setDob(item2.getDob());
                                            model.setGenderid(item2.getGenderid());
                                            model.setPincode(item2.getPincode());
                                            oldMemList.add(model);
                                        }

                                        item.setOldMembers(oldMemList);
                                        item.setSource(AppConstant.SECC_SOURCE_NEW);
                                        Intent intent = new Intent(context, CollectDataActivity.class);
                                        intent.putExtra("member", mDataset.get(position).getName());
                                        item.setPersonalDetail(null);
                                        if (item.getState_code() != null && !item.getState_code().equalsIgnoreCase("")) {
                                            item.setStatecode(item.getState_code());
                                        }
                                        Log.d("TAG", "Doc List : " + item.serialize());
                                        item.setFamilyDetailsItemModel(null);
                                        ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_NAME, SELECTED_MEMBER, item.serialize(), context);
                                        Log.d("TAG", "Doc List from shared pref: " + DocsListItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_NAME,
                                                SELECTED_MEMBER, context)));
                                        startActivity(intent);
                                    }


                                } else if (getMemberDetailResponse != null &&
                                        getMemberDetailResponse.getErrorcode().equalsIgnoreCase(AppConstant.SESSION_EXPIRED)
                                        || getMemberDetailResponse.getErrorcode().equalsIgnoreCase(AppConstant.INVALID_TOKEN)) {
                                    Intent intent = new Intent(context, LoginActivity.class);
                                    CustomAlert.alertWithOkLogout(context, getMemberDetailResponse.getErrorMessage(), intent);
                                } else {
                                    CustomAlert.alertWithOk(context, getMemberDetailResponse.getErrorMessage());
                                }
                            } else {
                                CustomAlert.alertWithOk(context, "Server Error");
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
            });

        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return mDataset.size();
        }
    }

    private class CustomAdapterRSBY extends RecyclerView.Adapter<CustomAdapterRSBY.ViewHolder> {
        private ArrayList<URNResponseItem> mDataset;

        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView nameTV, fatherNameTV, genderTV, ageTV, addressTV;
            private Button collectDataBT, printCardBT;

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

        public CustomAdapterRSBY(ArrayList<URNResponseItem> myDataset) {
            mDataset = myDataset;
        }

        @Override
        public CustomAdapterRSBY.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                               int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.family_members_item, parent, false);
            CustomAdapterRSBY.ViewHolder vh = new CustomAdapterRSBY.ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(CustomAdapterRSBY.ViewHolder holder, final int position) {
            final URNResponseItem item = mDataset.get(position);

            holder.nameTV.setText(item.getMemberName());
            holder.fatherNameTV.setText(item.getFatherhusbandname());
            String gender = "", address = "";

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

            String currentYear = DateTimeUtil.currentDate(AppConstant.DATE_FORMAT);
            currentYear = currentYear.substring(0, 4);
            int age = Integer.parseInt(currentYear) - Integer.parseInt(yob);
            holder.ageTV.setText(age + "");

            // holder.ageTV.setText(item.getAge());
            if (item != null) {
                if (item.getVillageName() != null && !item.getVillageName().equalsIgnoreCase("")) {
                    address = address + item.getVillageName() + ", ";

                }
                if (item.getDistrictName() != null && !item.getDistrictName().equalsIgnoreCase("")) {

                    address = address + item.getDistrictName() + ", ";
                }
                if (item.getStateName() != null && !item.getStateName().equalsIgnoreCase("")) {


                    address = address + item.getStateName();
                }

                //  holder.addressTV.setText(address);

            }
            if (item.getDistrictName() != null) {
                holder.addressTV.setText(item.getDistrictName());
            }
            holder.collectDataBT.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final DocsListItem item1 = new DocsListItem();
                 /*   DocsListItem item1 = new DocsListItem();
                    item1.setName(item.getMemberName());
                    item1.setHhd_no(item.getUrnNo());
                    item1.setSource(AppConstant.RSBY_SOURCE_NEW);
                    ArrayList<FamilyMemberModel> oldMemList = new ArrayList<>();
                    for (URNResponseItem item2 : mDataset) {
                        FamilyMemberModel model = new FamilyMemberModel();
                        model.setName(item2.getMemberName());
                        oldMemList.add(model);
                    }

                    item1.setOldMembers(oldMemList);

                    Intent intent = new Intent(context, CollectDataActivity.class);
                    intent.putExtra("member", mDataset.get(position).getMemberName());
                    item1.setPersonalDetail(null);
                     *//*if(item.getState_code()!=null && !item.getState_code().equalsIgnoreCase("")) {
                         item.setStatecode(item.getState_code());
                     }*//*
                    Log.d("TAG", "Doc List : " + item.serialize());
                    item1.setFamilyDetailsItemModel(null);
                    ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_NAME, SELECTED_MEMBER, item1.serialize(), context);
                    Log.d("TAG", "Doc List from shared pref: " + DocsListItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_NAME,
                            SELECTED_MEMBER, context)));
                    startActivity(intent);*/

                    final GetMemberDetailRequestModel requestModel = new GetMemberDetailRequestModel();
                    if (item.getUrnNo() != null && !item.getUrnNo().equalsIgnoreCase("")
                            && item.getMemberId()!=null && !item.getMemberId().equalsIgnoreCase("")) {
                        requestModel.setAhl_tin(item.getUrnNo()+""+item.getMemberId());
                        requestModel.setHhd_no(item.getUrnNo());
                    }
                   /* if (item1.getHhd_no() != null && !item1.getHhd_no().equalsIgnoreCase("")) {
                        requestModel.setHhd_no(item.getUrnNo());
                    }*/

                   // if (item1.getState_code() != null && !item1.getState_code().equalsIgnoreCase("")) {
                        requestModel.setStatecode(6);
                   // }

                    TaskListener taskListener = new TaskListener() {
                        @Override
                        public void execute() {
                            try {
                                String request = requestModel.serialize();
                                HashMap<String, String> response = CustomHttp.httpPostWithTokken(AppConstant.GET_MEMBER_DETAIL, request, AppConstant.AUTHORIZATION, verifierLoginResp.getAuthToken());
                                String familyResponse = response.get("response");

                                if (familyResponse != null) {
                                    getMemberDetailResponse = new GetMemberDetail().create(familyResponse);

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void updateUI() {
                            if (getMemberDetailResponse != null) {
                                if (getMemberDetailResponse.isStatus()) {
                                    if (getMemberDetailResponse.getPersonalDetail().getBenefName() != null &&
                                            !getMemberDetailResponse.getPersonalDetail().getBenefName().equalsIgnoreCase("")) {

                                        Intent intent = new Intent(context, ViewMemberDataActivity.class);
                                        ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_NAME, AppConstant.VIEW_DATA, getMemberDetailResponse.serialize(), context);
                                        startActivity(intent);

                                    } else {
                                        item1.setName(item.getMemberName());
                                        item1.setState_code("6");
                                        item1.setHhd_no(item.getUrnNo());
                                        item1.setAhl_tin(item.getUrnNo()+""+item.getMemberId());
                                        item1.setSource(AppConstant.RSBY_SOURCE_NEW);
                                        item1.setDob(item.getDob());
                                        item1.setGenderid(item.getGender());
                                        item1.setState_name(item.getStateName());
                                        item1.setDistrict_name(item.getDistrictName());
                                        item1.setFathername(item.getFatherhusbandname());

                                      //  item1.setPincode(item.ge());
                                        ArrayList<FamilyMemberModel> oldMemList = new ArrayList<>();
                                        for (URNResponseItem item2 : mDataset) {
                                            FamilyMemberModel model = new FamilyMemberModel();
                                            model.setName(item2.getMemberName());
                                            if(item2.getGender()!=null && item2.getGender().equalsIgnoreCase("M")){
                                                model.setGenderid("1");
                                            }else if(item2.getGender()!=null && item2.getGender().equalsIgnoreCase("F")){
                                                model.setGenderid("2");
                                            }else{
                                                model.setGenderid("3");
                                            }
                                            oldMemList.add(model);
                                        }

                                        item1.setOldMembers(oldMemList);

                                        Intent intent = new Intent(context, CollectDataActivity.class);
                                        intent.putExtra("member", mDataset.get(position).getMemberName());
                                        item1.setPersonalDetail(null);
                                        if (item1.getState_code() != null && !item1.getState_code().equalsIgnoreCase("")) {
                                            item1.setStatecode(item1.getState_code());
                                        }
                                        Log.d("TAG", "Doc List : " + item.serialize());
                                        item1.setFamilyDetailsItemModel(null);
                                        ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_NAME, SELECTED_MEMBER, item1.serialize(), context);
                                        Log.d("TAG", "Doc List from shared pref: " + DocsListItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_NAME,
                                                SELECTED_MEMBER, context)));
                                        startActivity(intent);
                                    }


                                } else if (getMemberDetailResponse != null &&
                                        getMemberDetailResponse.getErrorcode().equalsIgnoreCase(AppConstant.SESSION_EXPIRED)
                                        || getMemberDetailResponse.getErrorcode().equalsIgnoreCase(AppConstant.INVALID_TOKEN)) {
                                    Intent intent = new Intent(context, LoginActivity.class);
                                    CustomAlert.alertWithOkLogout(context, familyListResponseModel.getErrorMessage(), intent);
                                } else {
                                    CustomAlert.alertWithOk(context, getMemberDetailResponse.getErrorMessage());
                                }
                            } else {
                                CustomAlert.alertWithOk(context, "Server Error");
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
            });

        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return mDataset.size();
        }
    }

    private void getMemberDetail(String ahl_tin, String hhd_no, int statecode) {
        GetMemberDetailRequestModel requestModel = new GetMemberDetailRequestModel();
        requestModel.setAhl_tin(ahl_tin);
        requestModel.setHhd_no(hhd_no);
        requestModel.setStatecode(statecode);

        TaskListener taskListener = new TaskListener() {
            @Override
            public void execute() {
                try {
                    String request = familyListRequestModel.serialize();
                    HashMap<String, String> response = CustomHttp.httpPost(AppConstant.SEARCH_FAMILY_LIST, request);
                    String familyResponse = response.get("response");

                    if (familyResponse != null) {
                        getMemberDetailResponse = new GetMemberDetail().create(familyResponse);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void updateUI() {
                if (getMemberDetailResponse != null) {
                    if (getMemberDetailResponse.isStatus()) {
                        if (getMemberDetailResponse.getPersonalDetail().getBenefName() != null &&
                                !getMemberDetailResponse.getPersonalDetail().getBenefName().equalsIgnoreCase("")) {

                        } else {

                        }


                    } else {
                        CustomAlert.alertWithOk(context, getMemberDetailResponse.getErrorMessage());
                    }
                } else {
                    CustomAlert.alertWithOk(context, "Server Error");
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
}
