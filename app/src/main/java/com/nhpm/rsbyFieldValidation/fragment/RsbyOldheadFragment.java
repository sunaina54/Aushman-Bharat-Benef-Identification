package com.nhpm.rsbyFieldValidation.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.customComponent.utility.ProjectPrefrence;
import com.nhpm.Models.response.rsbyMembers.RSBYItem;
import com.nhpm.Models.response.rsbyMembers.RsbyHouseholdItem;
import com.nhpm.Models.response.seccMembers.SelectedMemberItem;
import com.nhpm.Models.response.verifier.FamilyStatusItem;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;
import com.nhpm.Utility.AppUtility;
import com.nhpm.rsbyFieldValidation.RsbyMainActivity;
import com.nhpm.rsbyFieldValidation.RsbyMemberDetailActivity;
import com.nhpm.rsbyFieldValidation.RsbyMemberPreviewActivity;

import java.util.ArrayList;

import static android.support.v7.widget.StaggeredGridLayoutManager.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RsbyOldheadFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RsbyOldheadFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Context context;
    private String urnid ;
    private ArrayList<RSBYItem> houseHoldMemberList;
    private RsbyHouseholdItem rsbyHouseHoldItem;
    private RecyclerView recycleView;
    private RsbyMainActivity activity;
    private FamilyStatusItem familyStatusItem;
    private RSBYItem oldHeadItem;
    private SelectedMemberItem selectedMemberItem;


    public RsbyOldheadFragment() {
        // Required empty public constructor
    }

    public ArrayList<RSBYItem> getHouseHoldMemberList() {
        return houseHoldMemberList;
    }

    public void setHouseHoldMemberList(ArrayList<RSBYItem> houseHoldMemberList) {
        this.houseHoldMemberList = houseHoldMemberList;
    }

    public FamilyStatusItem getFamilyStatusItem() {
        return familyStatusItem;
    }

    public void setFamilyStatusItem(FamilyStatusItem familyStatusItem) {
        this.familyStatusItem = familyStatusItem;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RsbyOldheadFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RsbyOldheadFragment newInstance(String param1, String param2) {
        RsbyOldheadFragment fragment = new RsbyOldheadFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity=(RsbyMainActivity)context;
    }

    private void findHead(){
        if(houseHoldMemberList!=null) {
            for (RSBYItem item : houseHoldMemberList) {
                if (item.getRsbyMemId() != null && item.getRsbyMemId().equalsIgnoreCase(rsbyHouseHoldItem.getRsbyMemId())) {
                    oldHeadItem = item;
                    break;
                }
            }
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context=getActivity();
        View v=inflater.inflate(R.layout.fragment_rsby_oldhead, container, false);
        setupScreen(v);
        return v;
    }

    private void setupScreen(View v){
        /*urnid = getArguments().getString("URN");
        if(SeccDatabase.getRsbyMemberList(urnid,context)!=null && SeccDatabase.getRsbyMemberList(urnid,context).size()>0) {
            rsbyItemList = SeccDatabase.getRsbyMemberList(urnid,context);
            rsbyHouseHoldItem = SeccDatabase.getRsbyHouseHoldQ(urnid,context);
        }*/
        selectedMemberItem= SelectedMemberItem.create(ProjectPrefrence.
                getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SELECTED_ITEM_FOR_VERIFICATION,context));
        recycleView = (RecyclerView)v.findViewById(R.id.recycleViewRsbyData);
        if(selectedMemberItem!=null) {
            if (selectedMemberItem.getRsbyHouseholdItem() != null) {
                rsbyHouseHoldItem = selectedMemberItem.getRsbyHouseholdItem();
                findHead();
            }
        }
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        recycleView.setLayoutManager(mLayoutManager);
        recycleView.setItemAnimator(new DefaultItemAnimator());
        if(houseHoldMemberList!=null && houseHoldMemberList.size()>0){
            RsbyHouseholdAdapter adapter = new RsbyHouseholdAdapter(houseHoldMemberList);
            recycleView.setAdapter(adapter);
        }else{
            //AppUtility.alertWithOk(context, "No data available.  Please read RSBY card first");
        }
    }

    public class RsbyHouseholdAdapter extends RecyclerView.Adapter<RsbyHouseholdAdapter.MyViewHolder> {

        private ArrayList<RSBYItem> householdList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView nameTV, urnNoTV , dateOfBirthTV,genderTV , memberIdTV;
            public LinearLayout verifyLayout;
            private Button memberStatusBT;
            private RelativeLayout lay;

            public MyViewHolder(View view) {
                super(view);
                urnNoTV = (TextView) view.findViewById(R.id.urnNoTV);
                nameTV = (TextView) view.findViewById(R.id.nameTV);
                dateOfBirthTV = (TextView) view.findViewById(R.id.dateOfBirthTV);
                genderTV = (TextView) view.findViewById(R.id.genderTV);
                memberIdTV = (TextView) view.findViewById(R.id.memberIdTV);
                verifyLayout = (LinearLayout) view.findViewById(R.id.verifyLayout);
                verifyLayout.setVisibility(View.GONE);
                memberStatusBT=(Button)view.findViewById(R.id.proceedSurveyBT);
                lay=(RelativeLayout)view.findViewById(R.id.lay);
            }
        }


        public RsbyHouseholdAdapter(ArrayList<RSBYItem> arrayList) {
            this.householdList = arrayList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.rsby_member_item, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            final RSBYItem item = householdList.get(position);
            holder.urnNoTV.setText(item.getUrnId());
            if(item.getGender()!=null && !item.getGender().equalsIgnoreCase("")){
                if(item.getGender().equalsIgnoreCase("1")){
                    holder.genderTV.setText("Male");
                }else if(item.getGender().equalsIgnoreCase("2")){
                    holder.genderTV.setText("Female");
                }else{
                    holder.genderTV.setText("Other");
                }
            }

         AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Sub block : "+item.getVl_subBlockcode());
            holder.nameTV.setText(item.getName());

            holder.dateOfBirthTV.setText(AppUtility.convertRsbyDate(item.getDob()));
            holder.memberIdTV.setText(item.getMemid());
            if(rsbyHouseHoldItem!=null){
                if(rsbyHouseHoldItem.getRsbyMemId()!=null){

            if(item.getRsbyMemId()!=null && item.getRsbyMemId().trim().equalsIgnoreCase(rsbyHouseHoldItem.getRsbyMemId())){
                holder.lay.setBackgroundColor(AppUtility.getColor(context, R.color.Bg_bal_color));
            }

                }
            }
            holder.memberStatusBT.setVisibility(View.GONE);
            /*if(item.getSyncedStatus()!=null && item.getSyncedStatus().trim().
                    equalsIgnoreCase(AppConstant.SYNCED_STATUS)){
                holder.proceedSurveyBT.setVisibility(View.VISIBLE);
                holder.proceedSurveyBT.setText(getResources().getString(R.string.synced));
                holder.proceedSurveyBT.setBackgroundColor(AppUtility.getColor(context,R.color.sync_color));

            }else{
                if(item.getLockedSave()!=null && item.getLockedSave().trim().equalsIgnoreCase(AppConstant.LOCKED+"")){
                    holder.proceedSurveyBT.setVisibility(View.VISIBLE);
                    holder.proceedSurveyBT.setText(getResources().getString(R.string.locked));
                    holder.proceedSurveyBT.setBackgroundColor(AppUtility.getColor(context,R.color.locked_color));
                    holder.proceedSurveyBT.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //Lock preview
                        }
                    });
                }else if(item.getLockedSave()!=null && item.getLockedSave().trim().equalsIgnoreCase(AppConstant.SAVE+"")){
                    holder.proceedSurveyBT.setVisibility(View.VISIBLE);
                    holder.proceedSurveyBT.setText(getResources().getString(R.string.under_survey));
                    holder.proceedSurveyBT.setBackgroundColor(AppUtility.getColor(context,R.color.under_survey_color));
                    holder.proceedSurveyBT.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            openYetToSrvey(item,rsbyHouseHoldItem);
                        }
                    });
                }else{
                    holder.proceedSurveyBT.setVisibility(View.VISIBLE);
                    holder.proceedSurveyBT.setText(getResources().getString(R.string.yet_to_survey));
                    holder.proceedSurveyBT.setBackgroundColor(AppUtility.getColor(context,R.color.yet_to_survey_color));
                    holder.proceedSurveyBT.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            openYetToSrvey(item,rsbyHouseHoldItem);
                        }
                    });
                }
            }*/

            if(item.getSyncedStatus()!=null && item.getSyncedStatus().trim()
                    .equalsIgnoreCase(AppConstant.SYNCED_STATUS)) {
                activity.hofStatusSP.setEnabled(false);
                activity.familyStatusSP.setEnabled(false);
                holder.memberStatusBT.setVisibility(View.VISIBLE);
            /*    holder.syncDetailLayout.setVisibility(View.VISIBLE);
                holder.memberIdLayout.setVisibility(View.VISIBLE);
                holder.memberId.setText(item.getNhpsMemId());
                String dateTime= DateTimeUtil.convertTimeMillisIntoStringDate(Long.parseLong(item.getSyncDt()),AppConstant.SYNC_DATE_TIME);
                holder.syncDate.setText(dateTime);*/
                holder.memberStatusBT.setText(getResources().getString(R.string.synced));
                holder.memberStatusBT.setBackgroundColor(AppUtility.getColor(context, R.color.sync_color));
                holder.memberStatusBT.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openPreview(item);
                    }
                });
            }else{
                if (oldHeadItem != null) {
                    if (oldHeadItem.getLockedSave() != null && !oldHeadItem.getLockedSave().equalsIgnoreCase("")) {
// HEAD IS SURVEYE
                        activity.hofStatusSP.setEnabled(false);
                        activity.familyStatusSP.setEnabled(false);
                        if (item.getLockedSave() != null && !item.getLockedSave().equalsIgnoreCase("")) {
                            if (item.getLockedSave().trim().equalsIgnoreCase(AppConstant.LOCKED + "")) {
                                if (item.getMemStatus() != null && item.getMemStatus().trim().equalsIgnoreCase(AppConstant.MEMBERFOUND_AND_PRESENT)) {
                                    if (item.getAadhaarStatus() != null && item.getAadhaarStatus().trim().
                                            equalsIgnoreCase(AppConstant.AADHAAR_STAT)) {
                                        if (item.getAadhaarAuth() != null && item.getAadhaarAuth().trim().equalsIgnoreCase(AppConstant.VALID_STATUS)) {
                                            holder.memberStatusBT.setVisibility(View.VISIBLE);
                                            holder.memberStatusBT.setText(getResources().getString(R.string.locked));
                                            holder.memberStatusBT.setBackgroundColor(AppUtility.getColor(context, R.color.locked_color));
                                        }else{
                                            holder.memberStatusBT.setVisibility(View.VISIBLE);
                                            holder.memberStatusBT.setText(getResources().getString(R.string.locked));
                                            holder.memberStatusBT.setBackgroundColor(AppUtility.getColor(context, R.color.locked_color));
                                        }
                                    }
                                    if (item.getAadhaarStatus() != null && item.getAadhaarStatus().trim().
                                            equalsIgnoreCase(AppConstant.GOVT_ID_STAT)) {
                                        holder.memberStatusBT.setVisibility(View.VISIBLE);
                                        holder.memberStatusBT.setText(getResources().getString(R.string.locked));
                                        holder.memberStatusBT.setBackgroundColor(AppUtility.getColor(context, R.color.locked_color));
                                    }
                                    //if(item.getNhpsMemId().equalsIgnoreCase(oldHeadItem.getNhpsMemId())){
                                    holder.memberStatusBT.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
//REDIRECT FOR PREVIEW
                                            //  openPreview(item);
                                            // openReset(item,holder.memberStatusBT);
                                         //   SeccMemberListActivity.openReset(item,holder.memberStatusBT,context,activity);

                                        }
                                    });

                                } else if (item.getMemStatus() != null && item.getMemStatus().trim().equalsIgnoreCase(AppConstant.MEMBERFOUND_BUT_NOT_PRESENT)) {
                                    if (item.getAadhaarStatus() != null && item.getAadhaarStatus().trim().
                                            equalsIgnoreCase(AppConstant.AADHAAR_STAT)) {
                                        if (item.getAadhaarAuth() != null && item.getAadhaarAuth().trim().equalsIgnoreCase(AppConstant.VALID_STATUS)) {
                                            holder.memberStatusBT.setVisibility(View.VISIBLE);
                                            holder.memberStatusBT.setText(getResources().getString(R.string.locked));
                                            holder.memberStatusBT.setBackgroundColor(AppUtility.getColor(context, R.color.locked_color));

                                        }
                                    } else {
                                        holder.memberStatusBT.setVisibility(View.VISIBLE);
                                        holder.memberStatusBT.setText(getResources().getString(R.string.locked));
                                        holder.memberStatusBT.setBackgroundColor(AppUtility.getColor(context, R.color.locked_color));
                                    }
                                    holder.memberStatusBT.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            //SeccMemberListActivity.openReset(item,holder.memberStatusBT,context,activity);
                                        }
                                    });
                                    /*}else{
                                        holder.memberStatusBT.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
//REDIRECT FOR PREVIEW
                                                //  openPreview(item);
                                                openUnlock(item,holder.memberStatusBT);
                                            }
                                        });
                                    }*/
                                } else {
                                    holder.memberStatusBT.setVisibility(View.VISIBLE);
                                    holder.memberStatusBT.setText(getResources().getString(R.string.locked));
                                    holder.memberStatusBT.setBackgroundColor(AppUtility.getColor(context, R.color.locked_color));
                                   /* holder.memberStatusBT.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
//REDIRECT FOR PREVIEW
                                            //AppUtility.showLog(AppConstant.LOG_STATUS,TAG," locked status5"+item.getLockedSave());

                                            openPreview(item);
                                        }
                                    });*/

                                    //if(item.getNhpsMemId().equalsIgnoreCase(oldHeadItem.getNhpsMemId())){
                                    holder.memberStatusBT.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
//REDIRECT FOR PREVIEW
                                            //  openPreview(item);
                                        //    SeccMemberListActivity.openReset(item,holder.memberStatusBT,context,activity);
                                        }
                                    });
                                    /*}else{
                                        holder.memberStatusBT.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
//REDIRECT FOR PREVIEW
                                                //  openPreview(item);
                                                openUnlock(item,holder.memberStatusBT);
                                            }
                                        });

                                    }*/
                                }

                              /*  holder.memberStatusBT.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
//REDIRECT FOR PREVIEW
                                        openPreview(item);
                                    }
                                });*/
//MEMBER IS UNDER SURVEY
                            } else if (item.getLockedSave() != null && item.getLockedSave().trim().equalsIgnoreCase(AppConstant.SAVE + "")) {
                                // Save block
                                AppUtility.showLog(AppConstant.LOG_STATUS,TAG," membe status 9"+" Name : "+item.getName());
                                if (item.getMemStatus() != null && item.getMemStatus().trim().
                                        equalsIgnoreCase(AppConstant.MEMBERFOUND_AND_PRESENT)) {
                                    AppUtility.showLog(AppConstant.LOG_STATUS,TAG," membe status 1");
                                    if (item.getAadhaarStatus() != null && item.getAadhaarStatus().trim().
                                            equalsIgnoreCase(AppConstant.AADHAAR_STAT)) {
                                        AppUtility.showLog(AppConstant.LOG_STATUS,TAG," membe status 2");

                                        if (item.getAadhaarAuth() != null && item.getAadhaarAuth().trim().equalsIgnoreCase(AppConstant.PENDING_STATUS)) {
                                            holder.memberStatusBT.setVisibility(View.VISIBLE);
                                            holder.memberStatusBT.setText(getResources().getString(R.string.under_survey));
                                            holder.memberStatusBT.setBackgroundColor(AppUtility.getColor(context, R.color.under_survey_color));
                                            holder.memberStatusBT.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
// REDIRECT FOR UNDER SURVEY
                                                    openYetToSurvey(item);
                                                }
                                            });
                                        }else if (item.getAadhaarAuth() != null && item.getAadhaarAuth().trim().equalsIgnoreCase(AppConstant.VALID_STATUS)) {
                                            holder.memberStatusBT.setVisibility(View.VISIBLE);
                                            holder.memberStatusBT.setText(getResources().getString(R.string.validated));
                                            holder.memberStatusBT.setBackgroundColor(AppUtility.getColor(context, R.color.validated_color));
                                            holder.memberStatusBT.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
// REDIRECT FOR UNDER SURVEY
                                                    openYetToSurvey(item);
                                                }
                                            });
                                            AppUtility.showLog(AppConstant.LOG_STATUS, TAG, " membe status 3");
                                        }else if (item.getAadhaarAuth() != null && item.getAadhaarAuth().trim().equalsIgnoreCase(AppConstant.INVALID_STATUS)) {
                                            holder.memberStatusBT.setVisibility(View.VISIBLE);
                                            holder.memberStatusBT.setText(getResources().getString(R.string.under_survey));
                                            holder.memberStatusBT.setBackgroundColor(AppUtility.getColor(context, R.color.under_survey_color));
                                            holder.memberStatusBT.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
// REDIRECT FOR UNDER SURVEY
                                                    openYetToSurvey(item);
                                                }
                                            });
                                            AppUtility.showLog(AppConstant.LOG_STATUS, TAG, " membe status 3");
                                        }else{
                                            holder.memberStatusBT.setVisibility(View.VISIBLE);
                                            holder.memberStatusBT.setText(getResources().getString(R.string.under_survey));
                                            holder.memberStatusBT.setBackgroundColor(AppUtility.getColor(context, R.color.under_survey_color));
                                            holder.memberStatusBT.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
// REDIRECT FOR UNDER SURVEY
                                                    openYetToSurvey(item);
                                                }
                                            });
                                        }
                                        AppUtility.showLog(AppConstant.LOG_STATUS,TAG," membe status 4");

                                    }else if (item.getAadhaarStatus() != null && item.getAadhaarStatus().trim().
                                            equalsIgnoreCase(AppConstant.GOVT_ID_STAT)) {
                                        holder.memberStatusBT.setVisibility(View.VISIBLE);
                                        holder.memberStatusBT.setText(getResources().getString(R.string.under_survey));
                                        holder.memberStatusBT.setBackgroundColor(AppUtility.getColor(context, R.color.under_survey_color));
                                        holder.memberStatusBT.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
// REDIRECT FOR UNDER SURVEY
                                                openYetToSurvey(item);
                                            }
                                        });
                                    }else{
                                        holder.memberStatusBT.setVisibility(View.VISIBLE);
                                        holder.memberStatusBT.setText(getResources().getString(R.string.under_survey));
                                        holder.memberStatusBT.setBackgroundColor(AppUtility.getColor(context, R.color.under_survey_color));
                                        holder.memberStatusBT.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
// REDIRECT FOR UNDER SURVEY
                                                openYetToSurvey(item);
                                            }
                                        });
                                    }
                                } else if (item.getMemStatus() != null && item.getMemStatus().trim().
                                        equalsIgnoreCase(AppConstant.MEMBERFOUND_BUT_NOT_PRESENT)) {
                                    if (item.getAadhaarStatus() != null && item.getAadhaarStatus().trim().
                                            equalsIgnoreCase(AppConstant.AADHAAR_STAT)) {
                                        if (item.getAadhaarAuth() != null && item.getAadhaarAuth().trim().equalsIgnoreCase(AppConstant.PENDING_STATUS)) {
                                            holder.memberStatusBT.setVisibility(View.VISIBLE);
                                            holder.memberStatusBT.setText(getResources().getString(R.string.under_survey));
                                            holder.memberStatusBT.setBackgroundColor(AppUtility.getColor(context, R.color.under_survey_color));
                                            holder.memberStatusBT.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
// REDIRECT FOR UNDER SURVEY
                                                    openYetToSurvey(item);
                                                }
                                            });
                                        }else{
                                            holder.memberStatusBT.setVisibility(View.VISIBLE);
                                            holder.memberStatusBT.setText(getResources().getString(R.string.under_survey));
                                            holder.memberStatusBT.setBackgroundColor(AppUtility.getColor(context, R.color.under_survey_color));
                                            holder.memberStatusBT.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
// REDIRECT FOR UNDER SURVEY
                                                    openYetToSurvey(item);
                                                }
                                            });
                                        }
                                    } else {
                                        holder.memberStatusBT.setVisibility(View.VISIBLE);
                                        holder.memberStatusBT.setText(getResources().getString(R.string.under_survey));
                                        holder.memberStatusBT.setBackgroundColor(AppUtility.getColor(context, R.color.under_survey_color));
                                        holder.memberStatusBT.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
// REDIRECT FOR UNDER SURVEY
                                                openYetToSurvey(item);
                                            }
                                        });
                                    }
                                } else {
                                    //  AppUtility.showLog(AppConstant.LOG_STATUS,TAG," membe status 4 "+item.getName()+" locked status : "+item.getLockedSave());
                                    holder.memberStatusBT.setVisibility(View.VISIBLE);
                                    holder.memberStatusBT.setText(getResources().getString(R.string.under_survey));
                                    holder.memberStatusBT.setBackgroundColor(AppUtility.getColor(context, R.color.under_survey_color));
                                    holder.memberStatusBT.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
// REDIRECT FOR UNDER SURVEY
                                            openYetToSurvey(item);
                                        }
                                    });
                                }
                            }
                            AppUtility.showLog(AppConstant.LOG_STATUS,TAG," membe status 4 "+item.getName()+" locked status : "+item.getLockedSave());

                        } else {
                            holder.memberStatusBT.setVisibility(View.VISIBLE);
                            holder.memberStatusBT.setText(getResources().getString(R.string.yet_to_survey));
                            holder.memberStatusBT.setBackgroundColor(AppUtility.getColor(context, R.color.yet_to_survey_color));
                            holder.memberStatusBT.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
// REDIRECT FOR YET TO SURVEY
                                    openYetToSurvey(item);
                                }
                            });
                        }
                    } else {
// head remain to survey
                        if(oldHeadItem!=null && oldHeadItem.getRsbyMemId()!=null &&
                                oldHeadItem.getRsbyMemId().trim().equalsIgnoreCase(item.getRsbyMemId().trim())) {
                            holder.memberStatusBT.setVisibility(View.VISIBLE);
                            holder.memberStatusBT.setText(getResources().getString(R.string.yet_to_survey));
                            holder.memberStatusBT.setBackgroundColor(AppUtility.getColor(context, R.color.yet_to_survey_color));
                            holder.memberStatusBT.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
// REDIRECT FOR YET TO SURVEY
                                    item.setNhpsRelationCode(AppConstant.NEW_HEAD_RELATION_CODE);
                                    openYetToSurvey(item);
                                }
                            });
                        }
                    }
                }
            }
        }

        @Override
        public int getItemCount() {
            return householdList.size();
        }
    }


   /* private void openYetToSrvey(RSBYItem rsbyItem,RsbyHouseholdItem rsbyHouseHoldItem){

        SelectedMemberItem selectedMemItem=new SelectedMemberItem();

        rsbyHouseHoldItem.setHhStatus(familyStatusItem.getStatusCode());
        selectedMemItem.setRsbyHouseholdItem(rsbyHouseHoldItem);
        if(rsbyHouseHoldItem.getRsbyMemId().equalsIgnoreCase(rsbyItem.getRsbyMemId())){
            oldHeadItem=rsbyItem;
            rsbyItem.setNhpsRelationCode(AppConstant.NEW_HEAD_RELATION_CODE);
        }
        selectedMemItem.setOldHeadrsbyMemberItem(oldHeadItem);
        selectedMemItem.setRsbyMemberItem(rsbyItem);
        ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.SELECTED_ITEM_FOR_VERIFICATION, selectedMemItem.serialize(), context);
        String payLoad = selectedMemItem.serialize().toString();
        Intent theIntent=new Intent(context, RsbyMemberDetailActivity.class);
        startActivity(theIntent);
        rsbyMainActivity.leftTransition();

    }*/

    private void openYetToSurvey(RSBYItem item){
        AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Relation Code : "+item.getNhpsRelationCode());
        rsbyHouseHoldItem.setHhStatus(activity.householdStatus.getStatusCode());
        item.setHhStatus(rsbyHouseHoldItem.getHhStatus());
        item.setMemStatus(activity.hofStatusItem.getStatusCode());
       // item.setNhpsRelationCode(AppConstant.NEW_HEAD_RELATION_CODE);
        selectedMemberItem.setRsbyMemberItem(item);
        selectedMemberItem.setRsbyHouseholdItem(rsbyHouseHoldItem);
        selectedMemberItem.setOldHeadrsbyMemberItem(oldHeadItem);
        ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.SELECTED_ITEM_FOR_VERIFICATION, selectedMemberItem.serialize(), context);
        Intent theIntent=new Intent(context,RsbyMemberDetailActivity.class);
        startActivity(theIntent);

    }

    private void openPreview(RSBYItem item){


        selectedMemberItem.setRsbyMemberItem(item);
        // selectedMemberItem.setOldHeadMember(oldHeadItem);
        selectedMemberItem.setRsbyHouseholdItem(rsbyHouseHoldItem);
        ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.SELECTED_ITEM_FOR_VERIFICATION, selectedMemberItem.serialize(), context);
        Intent theIntent = new Intent(context, RsbyMemberPreviewActivity.class);
        startActivity(theIntent);

    }


}