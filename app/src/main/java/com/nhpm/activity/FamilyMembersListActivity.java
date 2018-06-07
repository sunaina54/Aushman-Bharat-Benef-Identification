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
import com.nhpm.Models.request.FamilyListRequestModel;
import com.nhpm.Models.response.BeneficiaryListItem;
import com.nhpm.Models.response.BeneficiaryModel;
import com.nhpm.Models.response.DocsListItem;
import com.nhpm.Models.response.FamilyListResponseItem;
import com.nhpm.Models.response.verifier.VerifierLocationItem;
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
    private String hhdNo = "";
    public static String SELECTED_MEMBER = "SELECTED-MEMBER";
    private FamilyListRequestModel familyListRequestModel;
    private LinearLayout noMemberLL;
    private CustomAsyncTask customAsyncTask;
    private FamilyMembersListActivity activity;
    private TextView errorTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_famiy_members_list);
        setupScreen();
    }

    private void setupScreen() {
        context = this;
        activity=this;
        centerText = (TextView) findViewById(R.id.centertext);
        centerText.setText("Family Members");
        backIV = (ImageView) findViewById(R.id.back);
        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        AppUtility.navigateToHome(context,activity);
        collectDataBT = (Button) findViewById(R.id.collectDataBT);
        errorTV= (TextView) findViewById(R.id.errorTV);
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
        if (!hhdNo.equalsIgnoreCase("")) {
            familyIdNoTV.setText(hhdNo);
            familyListData();
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
                    HashMap<String, String> response = CustomHttp.httpPost(AppConstant.SEARCH_FAMILY_LIST, request);
                    String familyResponse = response.get("response");

                    if (familyResponse != null) {
                        familyListResponseModel = new FamilyListResponseItem().create(familyResponse);
                        errorTV.setVisibility(View.GONE);
                        memberListRV.setVisibility(View.VISIBLE);
                    }else {
                        errorTV.setVisibility(View.VISIBLE);
                        memberListRV.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void updateUI() {
                if (familyListResponseModel != null && familyListResponseModel.getResponse() != null
                        ) {
                    int matchCount = Integer.parseInt(familyListResponseModel.getResponse().getNumFound());
                    //noMemberTV.setText(matchCount + " matches found. Kindly refine your search.");
                    if (familyListResponseModel.getResponse().getDocs() != null && familyListResponseModel.getResponse().getDocs().size() > 0) {
                        //  if (matchCount<=familyListResponseModel.getResponse().getDocs().size()) {
                        try {
                            familyMembersNoTV.setText(familyListResponseModel.getResponse().getDocs().size()+"");

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
                    }

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
            String gender = "",address="";

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

            String currentYear= DateTimeUtil.currentDate(AppConstant.DATE_FORMAT);
            currentYear=currentYear.substring(0,4);
            int age=Integer.parseInt(currentYear)-Integer.parseInt(yob);
            holder.ageTV.setText(age+"");

           // holder.ageTV.setText(item.getAge());
            if(item!=null) {
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
                holder.addressTV.setText(address);

            }
            holder.collectDataBT.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, CollectDataActivity.class);
                     intent.putExtra("member",mDataset.get(position).getName());
                    ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_NAME, SELECTED_MEMBER, item.serialize(), context);
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
