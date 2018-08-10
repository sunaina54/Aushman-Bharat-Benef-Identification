package com.nhpm.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.customComponent.CustomAlert;
import com.customComponent.CustomAsyncTask;
import com.customComponent.TaskListener;
import com.customComponent.utility.CustomHttp;
import com.customComponent.utility.DateTimeUtil;
import com.customComponent.utility.ProjectPrefrence;
import com.nhpm.Models.FamilyMemberModel;
import com.nhpm.Models.request.GetMemberDetail;
import com.nhpm.Models.request.GetMemberDetailRequestModel;
import com.nhpm.Models.request.SearchByRationRequestModel;
import com.nhpm.Models.request.VerifiedFamilyRequestModel;
import com.nhpm.Models.response.DocsListItem;
import com.nhpm.Models.response.MemberListModel;
import com.nhpm.Models.response.VerifiedFamilyResponseModel;
import com.nhpm.Models.response.master.StateItem;
import com.nhpm.Models.response.verifier.VerifierLoginResponse;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;
import com.nhpm.Utility.AppUtility;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by SUNAINA on 07-08-2018.
 */

public class AddMemberActivity extends BaseActivity {
    private Context context;
    private AddMemberActivity addMemberActivity;
    private EditText mobileET, hhIdNoET, nhaIdET,rationCardET;
    private Spinner searchSpinner;
    private Button searchBTN;
    private String searchType = "", searchValue = "";
    private LinearLayout searchLL;
    private CustomAdapter adapter;
    private RecyclerView memberListRV;
    private TextView errorTV,headerTV;
    private ArrayList<MemberListModel> memberListModels;
    private StateItem selectedStateItem;
    private ImageView back;
    private RelativeLayout backLayout;
    private VerifierLoginResponse verifierLoginResponse;
    private VerifiedFamilyResponseModel verifiedFamilyResponseModel;
    private String familyResponse;
    private CustomAsyncTask customAsyncTask;
    private int param;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        addMemberActivity = this;
        setContentView(R.layout.activity_add_member_layout);
        setupScreen();
    }

    private void setupScreen() {
        ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, "add member per", context);
        verifierLoginResponse = VerifierLoginResponse.create(
                ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.VERIFIER_CONTENT, context));

        selectedStateItem = StateItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SELECTED_STATE_SEARCH, context));
        headerTV = (TextView) findViewById(R.id.centertext);

        headerTV.setText("Search Data " + "(" + selectedStateItem.getStateName() + ")");
        back = (ImageView) findViewById(R.id.back);
        backLayout = (RelativeLayout) findViewById(R.id.backLayout);
        AppUtility.navigateToHome(context,addMemberActivity);
        backLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backLayout.performClick();
            }
        });
        searchLL = (LinearLayout) findViewById(R.id.searchLL);
        searchLL.setVisibility(View.GONE);
        memberListRV = (RecyclerView) findViewById(R.id.memberListRV);
        memberListRV.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        memberListRV.setLayoutManager(mLayoutManager);

        errorTV = (TextView) findViewById(R.id.errorTV);
        mobileET = (EditText) findViewById(R.id.mobileET);
        mobileET.setSelection(mobileET.getText().toString().length());
        if (mobileET.getText().toString().length() == 10) {
            mobileET.setTextColor(AppUtility.getColor(context, R.color.green));
        }
        mobileET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() > 0) {
                    if (Integer.parseInt(charSequence.toString().substring(0, 1)) > 5) {
                        //isValidMobile = true;
                        mobileET.setTextColor(AppUtility.getColor(context, R.color.black_shine));
                        if (mobileET.getText().toString().length() == 10) {

                            mobileET.setTextColor(AppUtility.getColor(context, R.color.green));

                        }
                    } else {

                        mobileET.setTextColor(AppUtility.getColor(context, R.color.red));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        hhIdNoET = (EditText) findViewById(R.id.hhIdNoET);
        hhIdNoET.setSelection(hhIdNoET.getText().toString().length());
        if (hhIdNoET.getText().toString().length() == 24) {
            hhIdNoET.setTextColor(AppUtility.getColor(context, R.color.green));

        }
        hhIdNoET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() > 0) {

                    hhIdNoET.setTextColor(AppUtility.getColor(context, R.color.black_shine));
                    if (hhIdNoET.getText().toString().length() == 24) {
                        hhIdNoET.setTextColor(AppUtility.getColor(context, R.color.green));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        rationCardET = (EditText) findViewById(R.id.rationCardET);
        rationCardET.setSelection(rationCardET.getText().toString().length());

        nhaIdET = (EditText) findViewById(R.id.nhaIdET);
        nhaIdET.setSelection(nhaIdET.getText().toString().length());
        searchBTN = (Button) findViewById(R.id.searchBTN);
        searchSpinner = (Spinner) findViewById(R.id.searchSpinner);

        ArrayList<String> spinnerList = new ArrayList<>();
        spinnerList.add("Select");
        spinnerList.add(AppConstant.BY_MOBILE);
        spinnerList.add(AppConstant.By_HHID);
        spinnerList.add(AppConstant.BY_NHA_ID);
        spinnerList.add(AppConstant.BY_RATION_CARD);
        //"param":1/2/3/4/5;HHD_NO=1,RATION_CARD_NO=2,MOBILE_NO=3,NHA_ID=4;


        searchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                nhaIdET.setVisibility(View.GONE);
                rationCardET.setVisibility(View.GONE);
                mobileET.setVisibility(View.GONE);
                hhIdNoET.setVisibility(View.GONE);
                searchLL.setVisibility(View.GONE);
                if (position == 0) {

                } else if (position == 1) {

                    searchLL.setVisibility(View.VISIBLE);
                    nhaIdET.setVisibility(View.GONE);
                    mobileET.setVisibility(View.VISIBLE);
                    hhIdNoET.setVisibility(View.GONE);
                    rationCardET.setVisibility(View.GONE);
                    searchType = AppConstant.BY_MOBILE;
                    param=3;
                    AppUtility.searchTitleHeader=searchType;

                } else if (position == 2) {
                    searchLL.setVisibility(View.VISIBLE);
                    nhaIdET.setVisibility(View.GONE);
                    mobileET.setVisibility(View.GONE);
                    hhIdNoET.setVisibility(View.VISIBLE);
                    rationCardET.setVisibility(View.GONE);
                    searchType = AppConstant.By_HHID;
                    param=1;
                    AppUtility.searchTitleHeader=searchType;

                } else if (position == 3) {
                    searchLL.setVisibility(View.VISIBLE);
                    nhaIdET.setVisibility(View.VISIBLE);
                    mobileET.setVisibility(View.GONE);
                    hhIdNoET.setVisibility(View.GONE);
                    rationCardET.setVisibility(View.GONE);
                    searchType = AppConstant.BY_NHA_ID;
                    param=4;
                    AppUtility.searchTitleHeader=searchType;
                }else if (position == 4) {
                    searchLL.setVisibility(View.VISIBLE);
                    nhaIdET.setVisibility(View.GONE);
                    mobileET.setVisibility(View.GONE);
                    hhIdNoET.setVisibility(View.GONE);
                    rationCardET.setVisibility(View.VISIBLE);
                    searchType = AppConstant.BY_RATION_CARD;
                    param=2;
                    AppUtility.searchTitleHeader=searchType;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.custom_drop_down, R.id.textView, spinnerList);
        searchSpinner.setAdapter(adapter);

        searchBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!searchType.equalsIgnoreCase("")) {
                    if (searchType.equalsIgnoreCase(AppConstant.BY_MOBILE)) {
                        searchValue = mobileET.getText().toString();
                        if (searchValue.equalsIgnoreCase("")) {
                            CustomAlert.alertWithOk(context, "Please enter mobile number");
                            return;
                        } else if (searchValue.length() < 10) {
                            CustomAlert.alertWithOk(context, "Please enter valid mobile number");
                            return;
                        }
                        getMemberRecord();
                       // getVerifiedFamilyList();
                    }

                    if (searchType.equalsIgnoreCase(AppConstant.By_HHID)) {
                        searchValue = hhIdNoET.getText().toString();
                        if (searchValue.equalsIgnoreCase("")) {
                            CustomAlert.alertWithOk(context, "Please enter HHId number");
                            return;
                        } else if (searchValue.length() < 24) {
                            CustomAlert.alertWithOk(context, "Please enter valid HHId number");
                            return;
                        }
                        getMemberRecord();
                      //  getVerifiedFamilyList();
                    }

                    if (searchType.equalsIgnoreCase(AppConstant.BY_NHA_ID)) {
                        searchValue = nhaIdET.getText().toString();
                        if (searchValue.equalsIgnoreCase("")) {
                            CustomAlert.alertWithOk(context, "Please enter NHA ID");
                            return;
                        }
                        getMemberRecord();
                       // getVerifiedFamilyList();
                    }

                    if (searchType.equalsIgnoreCase(AppConstant.BY_RATION_CARD)) {
                        searchValue = rationCardET.getText().toString();
                        if (searchValue.equalsIgnoreCase("")) {
                            CustomAlert.alertWithOk(context, "Please enter ration card number");
                            return;
                        }
                        getMemberRecord();
                        //getVerifiedFamilyList();
                    }
                }
            }
        });

    }

    private void getMemberRecord() {
        memberListModels = new ArrayList<>();
        MemberListModel memberListModel = new MemberListModel();
        memberListModel.setId("POC4FGXZU");
        memberListModel.setName("Nitin");
        memberListModels.add(memberListModel);
        Log.d("List is :", memberListModel.serialize());

        // GET_VERIFIED_FAMILY_LIST
        refreshMembersList(memberListModels);

    }

    private void getVerifiedFamilyList() {
        TaskListener taskListener = new TaskListener() {
            @Override
            public void execute() {
                VerifiedFamilyRequestModel requestModel = new VerifiedFamilyRequestModel();
                if(searchType.equalsIgnoreCase(AppConstant.BY_NHA_ID)){
                    requestModel.setNha_id(searchValue);
                }
                if(searchType.equalsIgnoreCase(AppConstant.By_HHID)){
                    requestModel.setHhd_no(searchValue);
                }
                if(searchType.equalsIgnoreCase(AppConstant.BY_MOBILE)){
                    requestModel.setHhd_no(searchValue);
                }
                if(searchType.equalsIgnoreCase(AppConstant.BY_RATION_CARD)){
                    requestModel.setHhd_no(searchValue);
                }
                requestModel.setParam(param);
                requestModel.setStatecode(Integer.parseInt(selectedStateItem.getStateCode()));
                String request = requestModel.serialize();
                HashMap<String, String> response = null;
                try {
                    response = CustomHttp.httpPostWithTokken(AppConstant.GET_VERIFIED_FAMILY_LIST, request,AppConstant.AUTHORIZATION,verifierLoginResponse.getAuthToken());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                familyResponse = response.get("response");
                if (familyResponse != null) {
                    verifiedFamilyResponseModel = new VerifiedFamilyResponseModel().create(familyResponse);

                }
            }

            @Override
            public void updateUI() {
                if (verifiedFamilyResponseModel != null) {
                    if (verifiedFamilyResponseModel.isStatus()) {
                        if(verifiedFamilyResponseModel.getFamilyMemberList()!=null && verifiedFamilyResponseModel.getFamilyMemberList().size()>0) {

                            errorTV.setVisibility(View.GONE);
                            memberListRV.setVisibility(View.VISIBLE);
                            refreshMembersList(verifiedFamilyResponseModel.getFamilyMemberList());
                        }else {
                            memberListRV.setVisibility(View.GONE);
                            errorTV.setVisibility(View.VISIBLE);

                        }
                    } else if (verifiedFamilyResponseModel != null &&
                            verifiedFamilyResponseModel.getErrorCode().equalsIgnoreCase(AppConstant.SESSION_EXPIRED)
                            || verifiedFamilyResponseModel.getErrorCode().equalsIgnoreCase(AppConstant.INVALID_TOKEN)) {
                        Intent intent = new Intent(context, LoginActivity.class);
                        CustomAlert.alertWithOkLogout(context, verifiedFamilyResponseModel.getErrorMessage(), intent);
                    } else {
                        CustomAlert.alertWithOk(context, verifiedFamilyResponseModel.getErrorMessage());
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


    private void refreshMembersList(ArrayList<MemberListModel> memberListModels) {

        adapter = new CustomAdapter(memberListModels);
        memberListRV.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
        private ArrayList<MemberListModel> mDataset;

        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView nameTV, idTV;
            private Button collectDataBT;
            private LinearLayout familyMemberDetailsLL;

            public ViewHolder(View v) {
                super(v);
                nameTV = (TextView) v.findViewById(R.id.nameTV);
                idTV = (TextView) v.findViewById(R.id.idTV);
                familyMemberDetailsLL = (LinearLayout) v.findViewById(R.id.familyMemberDetailsLL);
                collectDataBT = (Button) v.findViewById(R.id.collectDataBT);


            }
        }


        public void add(int position, MemberListModel item) {
            mDataset.add(position, item);
            notifyItemInserted(position);
        }

        public void remove(String item) {
            int position = mDataset.indexOf(item);
            mDataset.remove(position);
            notifyItemRemoved(position);
        }

        public void updateData(ArrayList<MemberListModel> itemList) {
            mDataset.clear();
            mDataset.addAll(itemList);
            notifyDataSetChanged();
        }

        public CustomAdapter(ArrayList<MemberListModel> myDataset) {
            mDataset = myDataset;
        }

        @Override
        public CustomAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.added_member_detail, parent, false);
            CustomAdapter.ViewHolder vh = new CustomAdapter.ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(CustomAdapter.ViewHolder holder, final int position) {
            final MemberListModel item = mDataset.get(position);
            holder.nameTV.setText(item.getName());
            holder.idTV.setText(item.getId());

            holder.collectDataBT.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProjectPrefrence.removeSharedPrefrenceData(AppConstant.PROJECT_PREF, "add member per", context);
                    ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, "Golden_Data", item.serialize(), context);
                    Intent intent = new Intent(context, CollectMemberDataActivity.class);
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
