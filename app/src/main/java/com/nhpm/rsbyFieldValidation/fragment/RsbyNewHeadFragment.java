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

import com.customComponent.utility.DateTimeUtil;
import com.customComponent.utility.ProjectPrefrence;
import com.nhpm.LocalDataBase.dto.SeccDatabase;
import com.nhpm.Models.response.RelationItem;
import com.nhpm.Models.response.master.MemberStatusItem;
import com.nhpm.Models.response.rsbyMembers.RSBYItem;
import com.nhpm.Models.response.rsbyMembers.RsbyHouseholdItem;
import com.nhpm.Models.response.seccMembers.SeccMemberItem;
import com.nhpm.Models.response.seccMembers.SelectedMemberItem;
import com.nhpm.Models.response.verifier.FamilyStatusItem;
import com.nhpm.Models.response.verifier.VerifierLoginResponse;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;
import com.nhpm.Utility.AppUtility;
import com.nhpm.activity.MemberPreviewActivity;
import com.nhpm.rsbyFieldValidation.RsbyMainActivity;
import com.nhpm.rsbyFieldValidation.RsbyMemberDetailActivity;
import com.nhpm.rsbyFieldValidation.RsbyMemberPreviewActivity;

import java.util.ArrayList;

import static android.support.v7.widget.StaggeredGridLayoutManager.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RsbyNewHeadFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RsbyNewHeadFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ArrayList<RSBYItem> houseHoldMemberList;
    private RsbyHouseholdItem rsbyHouseHoldItem;
    private FamilyStatusItem familyStatusItem;
    private RecyclerView recycleView;
    private RsbyMainActivity activity;
    private RSBYItem newHeadItem,oldHeadItem;
    private MemberStatusItem hofStatusItem;
    private Context context;
    private VerifierLoginResponse loginDetail;
    private ArrayList<RelationItem> relationList;
    private SelectedMemberItem selectedMemberItem;
    private RsbyHouseholdItem houseHoldItem;
    private ArrayList<MemberStatusItem>  memberStatusList;


    public RsbyNewHeadFragment() {
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
    }public void setFamilyStatusItem(FamilyStatusItem familyStatusItem) {
        this.familyStatusItem = familyStatusItem;
    }

    public MemberStatusItem getHofStatusItem() {
        return hofStatusItem;
    }

    public void setHofStatusItem(MemberStatusItem hofStatusItem) {
        this.hofStatusItem = hofStatusItem;
    }

    public RsbyHouseholdItem getRsbyHouseHoldItem() {
        return rsbyHouseHoldItem;
    }

    public void setRsbyHouseHoldItem(RsbyHouseholdItem rsbyHouseHoldItem) {
        this.rsbyHouseHoldItem = rsbyHouseHoldItem;
    }

    public RSBYItem getOldHeadItem() {
        return oldHeadItem;
    }

    public void setOldHeadItem(RSBYItem oldHeadItem) {
        this.oldHeadItem = oldHeadItem;
    }

    public static RsbyNewHeadFragment newInstance(String param1, String param2) {
        RsbyNewHeadFragment fragment = new RsbyNewHeadFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        context=getActivity();
        activity = (RsbyMainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_rsby_new_head, container, false);
        setupScreen(v);
        return v;
    }

    private void setupScreen(View v){
       /* urnid = getArguments().getString("URN");
        if(SeccDatabase.getRsbyMemberList(urnid,context)!=null && SeccDatabase.getRsbyMemberList(urnid,context).size()>0) {
            rsbyItemList = SeccDatabase.getRsbyMemberList(urnid,context);
            rsbyHouseHoldItem = SeccDatabase.getRsbyHouseHoldQ(urnid,context);
        }*/
        relationList= SeccDatabase.getRelationList(context);
        selectedMemberItem= SelectedMemberItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.SELECTED_ITEM_FOR_VERIFICATION, context));
        loginDetail= VerifierLoginResponse.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.VERIFIER_CONTENT,context));
        if(selectedMemberItem.getRsbyHouseholdItem()!=null){
            houseHoldItem=selectedMemberItem.getRsbyHouseholdItem();
            // Log.d(TAG,"Household Status : "+houseHoldItem.getHhStatus());
        }
        if(activity.newHeadItem!=null) {
            newHeadItem = activity.newHeadItem;
        }
        if(activity.oldHeadItem!=null) {
            oldHeadItem = activity.oldHeadItem;
        }
        memberStatusList= SeccDatabase.getMemberStatusList(context);
        recycleView = (RecyclerView)v.findViewById(R.id.recycleViewRsbyData);
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

            holder.nameTV.setText(item.getName());

            holder.dateOfBirthTV.setText(AppUtility.convertRsbyDate(item.getDob()));
            holder.memberIdTV.setText(item.getMemid());
            if(item.getRsbyMemId()!=null && item.getRsbyMemId().trim().equalsIgnoreCase(houseHoldItem.getRsbyMemId())){
                holder.lay.setBackgroundColor(AppUtility.getColor(context, R.color.Bg_bal_color));
            }

            if(newHeadItem!=null) {
                if(newHeadItem.getNhpsRelationCode()!=null && newHeadItem.getRsbyMemId().equalsIgnoreCase(item.getRsbyMemId())) {
                   // holder.relationTV.setText(item.getRelation());
                    if(item.getNhpsRelationCode()!=null){
                      /*  holder.newRelationTV.setText("Head");
                        holder.newRelationTV.setTextColor(AppUtility.getColor(context,R.color.yellow_dark));*/
                    }
                    holder.lay.setBackgroundColor(AppUtility.getColor(context, R.color.Bg_bal_color));
                }else{
                    //AppUtility.showLog(AppConstant.LOG_STATUS,TAG," Old Head : "+oldHeadItem);
                    if(item.getRsbyMemId().equalsIgnoreCase(oldHeadItem.getRsbyMemId())) {
                    /*    holder.relationTV.setText(item.getRelation());
                        holder.newRelationTV.setText("Old Head");*/
                        holder.lay.setBackgroundColor(AppUtility.getColor(context, R.color.gray_transparent));
                    }
                   // holder.relationTV.setTextColor(AppUtility.getColor(context, R.color.black));
                }
            }
            holder.memberStatusBT.setVisibility(View.GONE);
            if(item.getSyncedStatus()!=null && item.getSyncedStatus().trim().equalsIgnoreCase(AppConstant.SYNCED_STATUS)) {
                holder.memberStatusBT.setVisibility(View.VISIBLE);
                /*holder.syncDetailLayout.setVisibility(View.VISIBLE);
                holder.memberIdLayout.setVisibility(View.VISIBLE);
                holder.memberId.setText(item.getNhpsMemId());*/
                String dateTime= DateTimeUtil.convertTimeMillisIntoStringDate(Long.parseLong(item.getSyncDt()), AppConstant.SYNC_DATE_TIME);
               // holder.syncDate.setText(dateTime);
                holder.memberStatusBT.setText(getResources().getString(R.string.synced));
                activity.hofStatusSP.setEnabled(false);
                activity.familyStatusSP.setEnabled(false);
                activity.chooseHeadLayout.setVisibility(View.GONE);
                holder.memberStatusBT.setBackgroundColor(AppUtility.getColor(context, R.color.sync_color));
                holder.memberStatusBT.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openPreview(item);
                    }
                });
            }else{
                if (newHeadItem != null) {
                    if (newHeadItem.getLockedSave() != null && !newHeadItem.getLockedSave().equalsIgnoreCase("")) {
// HEAD IS SURVEYE
                        activity.hofStatusSP.setEnabled(false);
                        activity.familyStatusSP.setEnabled(false);
                        activity.chooseHeadLayout.setVisibility(View.GONE);
                        AppUtility.showLog(AppConstant.LOG_STATUS,TAG," membe status 3"+oldHeadItem.getName());
                        if (item.getLockedSave() != null && !item.getLockedSave().equalsIgnoreCase("")) {
                            AppUtility.showLog(AppConstant.LOG_STATUS,TAG," membe status 4"+oldHeadItem.getName());
                            if (item.getLockedSave().trim().equalsIgnoreCase(AppConstant.LOCKED + "")) {

                                AppUtility.showLog(AppConstant.LOG_STATUS,TAG," membe status 90"+oldHeadItem.getName());
                                if (item.getMemStatus() != null && item.getMemStatus().trim().
                                        equalsIgnoreCase(AppConstant.MEMBERFOUND_AND_PRESENT)) {
                                    if (item.getAadhaarStatus() != null && item.getAadhaarStatus().trim().
                                            equalsIgnoreCase(AppConstant.AADHAAR_STAT)) {
                                        if (item.getAadhaarAuth() != null && item.getAadhaarAuth().trim().equalsIgnoreCase(AppConstant.VALID_STATUS)) {
                                            holder.memberStatusBT.setVisibility(View.VISIBLE);
                                            holder.memberStatusBT.setText(getResources().getString(R.string.locked));
                                            holder.memberStatusBT.setBackgroundColor(AppUtility.getColor(context, R.color.locked_color));
                                           /* holder.memberStatusBT.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
//REDIRECT FOR PREVIEW
                                                    AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Redriect 4");

                                                    openPreview(item);
                                                }
                                            });*/
                                        }else{
                                            holder.memberStatusBT.setVisibility(View.VISIBLE);
                                            holder.memberStatusBT.setText(getResources().getString(R.string.locked));
                                            holder.memberStatusBT.setBackgroundColor(AppUtility.getColor(context, R.color.locked_color));
                                           /* holder.memberStatusBT.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
//REDIRECT FOR PREVIEW
                                                    AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Redriect 4");

                                                    openPreview(item);
                                                }
                                            });*/
                                        }
                                    }
                                    if (item.getAadhaarStatus() != null && item.getAadhaarStatus().trim().
                                            equalsIgnoreCase(AppConstant.GOVT_ID_STAT)) {
                                        holder.memberStatusBT.setVisibility(View.VISIBLE);
                                        holder.memberStatusBT.setText(getResources().getString(R.string.locked));
                                        holder.memberStatusBT.setBackgroundColor(AppUtility.getColor(context, R.color.locked_color));
                                        /*holder.memberStatusBT.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
//REDIRECT FOR PREVIEW
                                                AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Redriect 4");

                                                openPreview(item);
                                            }
                                        });*/
                                    }
                                    if(item.getNhpsRelationCode()!=null && item.getNhpsRelationCode().trim().equalsIgnoreCase(AppConstant.NEW_HEAD_RELATION_CODE)){
                                        holder.memberStatusBT.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
//REDIRECT FOR PREVIEW
                                                //  openPreview(item);
                                             //   openReset(item,holder.memberStatusBT);
                                            }
                                        });
                                    }else{
                                        holder.memberStatusBT.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
//REDIRECT FOR PREVIEW
                                                //  openPreview(item);
                                              //  openUnlock(item,holder.memberStatusBT);
                                            }
                                        });

                                    }
                                } else if (item.getMemStatus() != null && item.getMemStatus().trim().
                                        equalsIgnoreCase(AppConstant.MEMBERFOUND_BUT_NOT_PRESENT)) {
                                    if (item.getAadhaarStatus() != null && item.getAadhaarStatus().trim().
                                            equalsIgnoreCase(AppConstant.AADHAAR_STAT)) {
                                        if (item.getAadhaarAuth() != null && item.getAadhaarAuth().trim().equalsIgnoreCase(AppConstant.VALID_STATUS)) {
                                            holder.memberStatusBT.setVisibility(View.VISIBLE);
                                            holder.memberStatusBT.setText(getResources().getString(R.string.locked));
                                            holder.memberStatusBT.setBackgroundColor(AppUtility.getColor(context, R.color.locked_color));
                                           /* holder.memberStatusBT.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
//REDIRECT FOR PREVIEW
                                                    AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Redriect 4");

                                                    openPreview(item);
                                                }
                                            });*/
                                        }
                                    } else {
                                        holder.memberStatusBT.setVisibility(View.VISIBLE);
                                        holder.memberStatusBT.setText(getResources().getString(R.string.locked));
                                        holder.memberStatusBT.setBackgroundColor(AppUtility.getColor(context, R.color.locked_color));
                                        /*holder.memberStatusBT.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
//REDIRECT FOR PREVIEW
                                                AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Redriect 4");

                                                openPreview(item);
                                            }
                                        });*/
                                    }
                                    if(item.getNhpsRelationCode()!=null && item.getNhpsRelationCode().trim().equalsIgnoreCase(AppConstant.NEW_HEAD_RELATION_CODE)){
                                        holder.memberStatusBT.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
//REDIRECT FOR PREVIEW
                                                //  openPreview(item);
                                                // openReset(item,holder.memberStatusBT);
                                            //    SeccMemberListActivity.openReset(item,holder.memberStatusBT,context,activity);
                                            }
                                        });
                                    }else{
                                        holder.memberStatusBT.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
//REDIRECT FOR PREVIEW
                                                //  openPreview(item);
                                                //openUnlock(item,holder.memberStatusBT);
                                             //   SeccMemberListActivity.openReset(item,holder.memberStatusBT,context,activity);
                                            }
                                        });
                                    }
                                } else {
                                    holder.memberStatusBT.setVisibility(View.VISIBLE);
                                    holder.memberStatusBT.setText(getResources().getString(R.string.locked));
                                    holder.memberStatusBT.setBackgroundColor(AppUtility.getColor(context, R.color.locked_color));
                                   /* holder.memberStatusBT.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
//REDIRECT FOR PREVIEW
                                            AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Redriect 4");

                                            openPreview(item);
                                        }
                                    });*/
                                    if(item.getNhpsRelationCode()!=null && item.getNhpsRelationCode().trim().equalsIgnoreCase(AppConstant.NEW_HEAD_RELATION_CODE)){
                                        holder.memberStatusBT.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
//REDIRECT FOR PREVIEW
                                                //  openPreview(item);
                                                // openReset(item,holder.memberStatusBT);
                                              //  SeccMemberListActivity.openReset(item,holder.memberStatusBT,context,activity);

                                            }
                                        });
                                    }else if(item.getNhpsRelationCode()==null || item.getNhpsRelationCode().equalsIgnoreCase("") ) {

                                        holder.memberStatusBT.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
//REDIRECT FOR PREVIEW
                                              //  openPreview(item);
                                            }
                                        });
                                    }else{
                                        holder.memberStatusBT.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
//REDIRECT FOR PREVIEW
                                                //  openPreview(item);
                                                // openUnlock(item,holder.memberStatusBT);
                                             //   SeccMemberListActivity.openReset(item,holder.memberStatusBT,context,activity);
                                            }
                                        });

                                    }
                                }
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
                                                    AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Redriect 3");

                                                    openYetToSurvey(item);
                                                }
                                            });
                                        }else if (item.getAadhaarAuth() != null && item.getAadhaarAuth().trim().equalsIgnoreCase(AppConstant.VALID_STATUS)) {
                                            holder.memberStatusBT.setVisibility(View.VISIBLE);
                                            holder.memberStatusBT.setText(getResources().getString(R.string.validated));
                                            holder.memberStatusBT.setBackgroundColor(AppUtility.getColor(context, R.color.validated_color));
                                            AppUtility.showLog(AppConstant.LOG_STATUS, TAG, " membe status 3");
                                            holder.memberStatusBT.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
// REDIRECT FOR UNDER SURVEY
                                                    AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Redriect 3");

                                                    openYetToSurvey(item);
                                                }
                                            });
                                        }else if (item.getAadhaarAuth() != null && item.getAadhaarAuth().trim().equalsIgnoreCase(AppConstant.INVALID_STATUS)) {
                                            holder.memberStatusBT.setVisibility(View.VISIBLE);
                                            holder.memberStatusBT.setText(getResources().getString(R.string.under_survey));
                                            holder.memberStatusBT.setBackgroundColor(AppUtility.getColor(context, R.color.under_survey_color));
                                            AppUtility.showLog(AppConstant.LOG_STATUS, TAG, " membe status 3");
                                            holder.memberStatusBT.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
// REDIRECT FOR UNDER SURVEY
                                                    AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Redriect 3");

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
                                                    AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Redriect 3");

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
                                                AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Redriect 3");

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
                                                AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Redriect 3");

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
                                                    AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Redriect 3");

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
                                                    AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Redriect 3");

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
                                                AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Redriect 3");

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
                                            AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Redriect 3");

                                            openYetToSurvey(item);
                                        }
                                    });
                                }
                            }
                            AppUtility.showLog(AppConstant.LOG_STATUS,TAG," membe status 4 "+item.getName()+" locked status : "+item.getLockedSave());

                           /* holder.memberStatusBT.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
// REDIRECT FOR UNDER SURVEY
                                    AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Redriect 3");

                                    openYetToSurvey(item);
                                }
                            });*/
                        } else {
                            holder.memberStatusBT.setVisibility(View.VISIBLE);
                            holder.memberStatusBT.setText(getResources().getString(R.string.yet_to_survey));
                            holder.memberStatusBT.setBackgroundColor(AppUtility.getColor(context, R.color.yet_to_survey_color));
                            holder.memberStatusBT.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
// REDIRECT FOR YET TO SURVEY
                                    AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Redriect 1");
                                    openYetToSurvey(item);
                                }
                            });
                        }
                    } else {
// head remain to survey
                        if(newHeadItem!=null && newHeadItem.getRsbyMemId()!=null &&
                                newHeadItem.getRsbyMemId().trim().equalsIgnoreCase(item.getRsbyMemId().trim())) {
                            holder.memberStatusBT.setVisibility(View.VISIBLE);
                            holder.memberStatusBT.setText(getResources().getString(R.string.yet_to_survey));
                            holder.memberStatusBT.setBackgroundColor(AppUtility.getColor(context, R.color.yet_to_survey_color));
                            holder.memberStatusBT.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
// REDIRECT FOR YET TO SURVEY
                                    AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Redriect 2");
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

    private void openYetToSurvey(RSBYItem item){
        houseHoldItem.setHhStatus(activity.householdStatus.getStatusCode());
        item.setHhStatus(houseHoldItem.getHhStatus());
        selectedMemberItem.setRsbyMemberItem(item);
        selectedMemberItem.setRsbyHouseholdItem(houseHoldItem);
        oldHeadItem.setAadhaarVerifiedBy(loginDetail.getAadhaarNumber());
        selectedMemberItem.setOldHeadrsbyMemberItem(oldHeadItem);
        selectedMemberItem.setNewHeadrsbyMemberItem(newHeadItem);
        ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.SELECTED_ITEM_FOR_VERIFICATION, selectedMemberItem.serialize(), context);
        Intent theIntent=new Intent(context,RsbyMemberDetailActivity.class);
        startActivity(theIntent);
        activity.leftTransition();

    }
    /*private void openYetToSrvey(RSBYItem rsbyItem,RsbyHouseholdItem rsbyHouseHoldItem){

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

    private void openPreview(RSBYItem item){


        selectedMemberItem.setRsbyMemberItem(item);
        // selectedMemberItem.setOldHeadMember(oldHeadItem);
        selectedMemberItem.setRsbyHouseholdItem(houseHoldItem);
        ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.SELECTED_ITEM_FOR_VERIFICATION, selectedMemberItem.serialize(), context);
        Intent theIntent = new Intent(context, RsbyMemberPreviewActivity.class);
        startActivity(theIntent);

    }
}
