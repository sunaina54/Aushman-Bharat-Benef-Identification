package com.nhpm.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.customComponent.CustomAlert;
import com.customComponent.utility.DateTimeUtil;
import com.customComponent.utility.ProjectPrefrence;
import com.nhpm.LocalDataBase.dto.SeccDatabase;
import com.nhpm.Models.response.RelationItem;
import com.nhpm.Models.response.master.MemberStatusItem;
import com.nhpm.Models.response.seccMembers.HouseHoldItem;
import com.nhpm.Models.response.seccMembers.SeccMemberItem;
import com.nhpm.Models.response.seccMembers.SelectedMemberItem;
import com.nhpm.Models.response.verifier.FamilyStatusItem;
import com.nhpm.Models.response.verifier.VerifierLoginResponse;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;
import com.nhpm.Utility.AppUtility;
import com.nhpm.activity.MemberPreviewActivity;
import com.nhpm.activity.SeccMemberDetailActivity;
import com.nhpm.activity.SeccMemberListActivity;
import com.nhpm.activity.SyncHouseholdActivity;

import java.util.ArrayList;
import java.util.List;

import static android.support.v7.widget.StaggeredGridLayoutManager.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link  interface
 * to handle interaction events.
 * Use the {@link OldHeadFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OldHeadFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private AlertDialog dialog,askForPinDailog;
    private String mParam1;
    private String mParam2;
    private Context context;
    private ArrayList<RelationItem> relationList;
    private SelectedMemberItem selectedMemberItem;
    private HouseHoldItem houseHoldItem;
    private ArrayList<SeccMemberItem>  houseHoldMemberList;
    private SeccMemberItem newHeadItem,oldHeadItem;
    private RecyclerView memberList;
    private ArrayList<MemberStatusItem>  memberStatusList;
    private MemberStatusItem headStatus;
    private SeccMemberListActivity activity;
    private ArrayList<FamilyStatusItem> familyStatusList;
    private OtherMemberAdapter adapter;
    private String EDIT_STATUS="edit",RESET_STATUS="reset";
    private LinearLayout footerSyncLayout;
    private Button syncBT;
    private int wrongPinCount = 0;
    private long wrongPinSavedTime;
    private long currentTime;
    private TextView wrongAttempetCountValue,wrongAttempetCountText;
    private long millisecond24 = 86400000;



    public OldHeadFragment() {

        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChooseNewHeadFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OldHeadFragment newInstance(String param1, String param2) {
        OldHeadFragment fragment = new OldHeadFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    public ArrayList<SeccMemberItem> getHouseHoldMemberList() {
        return houseHoldMemberList;
    }

    public void setHouseHoldMemberList(ArrayList<SeccMemberItem> houseHoldMemberList) {
        this.houseHoldMemberList = houseHoldMemberList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_choose_new_head, container, false);
        setupScreen(view);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event

    private void setupScreen(View view){
        context=getActivity();
        relationList= SeccDatabase.getRelationList(context);
        headStatus= activity.hofStatusItem;
        footerSyncLayout = (LinearLayout) view.findViewById(R.id.footerSyncLayout);
        syncBT = (Button) view.findViewById(R.id.syncBT);
        selectedMemberItem= SelectedMemberItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.SELECTED_ITEM_FOR_VERIFICATION, context));
        if(selectedMemberItem.getHouseHoldItem()!=null){
            houseHoldItem=selectedMemberItem.getHouseHoldItem();
            // Log.d(TAG,"Household Status : "+houseHoldItem.getHhStatus());
        }
        if(houseHoldItem!=null && houseHoldItem.getDataSource()!=null && houseHoldItem.getDataSource().equalsIgnoreCase(AppConstant.RSBY_SOURCE)){
            findRsbyHead();
        }else {
            findHead();
        }
        memberStatusList= SeccDatabase.getMemberStatusList(context);
        //findHouseholdMember();
        memberList=(RecyclerView)view.findViewById(R.id.memberList);

        memberList.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        memberList.setLayoutManager(layoutManager);
        memberList.setItemAnimator(new DefaultItemAnimator());
        displayList();
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity=(SeccMemberListActivity)context;

    }
    @Override
    public void onDetach() {
        super.onDetach();
    }
    private void findHead(){
        if(houseHoldMemberList!=null) {
            for (SeccMemberItem item : houseHoldMemberList) {
                if (item.getNhpsMemId() != null && item.getNhpsMemId().equalsIgnoreCase(houseHoldItem.getNhpsMemId())) {
                    oldHeadItem = item;
                    break;
                }
            }
        }
    }

    private void findRsbyHead(){
        if(houseHoldMemberList!=null) {
            for (SeccMemberItem item : houseHoldMemberList) {
                if (item.getRsbyMemId() != null && item.getRsbyMemId().equalsIgnoreCase(houseHoldItem.getRsbyMemId())) {
                    oldHeadItem = item;
                    break;
                }
            }
        }
    }
/*
    private void findHouseholdMember() {
        ArrayList<SeccMemberItem> seccFamilyList = SeccDatabase.getSeccMemberList(houseHoldItem.getHhdNo(), context);
        houseHoldMemberList = new ArrayList<>();
        if (houseHoldItem.getLockSave() != null && !houseHoldItem.getHhStatus().equalsIgnoreCase("")) {
        } else {
            newHeadItem = null;
        }
        for (SeccMemberItem item : seccFamilyList) {
            if (item.getNhpsRelationCode() != null && item.getNhpsRelationCode().equalsIgnoreCase(AppConstant.NEW_HEAD_RELATION_CODE)) {
                newHeadItem = item;
                break;
            }
        }
        if (newHeadItem == null) {
            for (SeccMemberItem item : seccFamilyList) {
                if (item.getNhpsMemId().equalsIgnoreCase(houseHoldItem.getNhpsMemId())) {
                    houseHoldMemberList.add(0, item);
                    oldHeadItem = item;
                } else {
                    houseHoldMemberList.add(item);
                }
            }
        } else {
            for (SeccMemberItem item : seccFamilyList) {
                if (item.getNhpsRelationCode() != null && item.getNhpsRelationCode().equalsIgnoreCase(AppConstant.NEW_HEAD_RELATION_CODE)) {
                    houseHoldMemberList.add(0, item);
                } else if (item.getNhpsMemId().equalsIgnoreCase(houseHoldItem.getNhpsMemId())) {
                    oldHeadItem = item;
                } else {
                    houseHoldMemberList.add(item);
                }
            }
            houseHoldMemberList.add(houseHoldMemberList.size(), oldHeadItem);
        }
        TreeSet<SeccMemberItem> seccTreeSet = new TreeSet<SeccMemberItem>(new Comparator<SeccMemberItem>() {
            public int compare(SeccMemberItem o1, SeccMemberItem o2) {
                // return 0 if objects are equal in terms of your properties
                if (o1.getNhpsMemId().equalsIgnoreCase(o2.getNhpsMemId())) {
                    return 0;
                }
                return 1;
            }
        });
        seccTreeSet.addAll(houseHoldMemberList);
        houseHoldMemberList = new ArrayList<>();
        houseHoldMemberList.addAll(seccTreeSet);
    }
*/

    private class OtherMemberAdapter extends
            RecyclerView.Adapter<OtherMemberAdapter.MyViewHolder> {
        private ArrayList<SeccMemberItem> dataSet;
        public class MyViewHolder extends RecyclerView.ViewHolder   {
            TextView nameTV,nameLocalTV,houseNoTV,fatherNameTV,fatherNameLocalTV,
                    genderTV,addressTV,
                    relationTV,newRelationTV,memberStatusTV,errorTV,syncDate,memberId;
            RelativeLayout parentLayout;
            LinearLayout memberIdLayout,syncDetailLayout;
            Button memberStatusBT;
            LinearLayout errorLayout;

            public MyViewHolder(View itemView) {
                super(itemView);

                syncDate = (TextView) itemView.findViewById(R.id.syncDate);
                memberId = (TextView) itemView.findViewById(R.id.memberId);
                memberIdLayout = (LinearLayout) itemView.findViewById(R.id.memberIdLayout);
                syncDetailLayout = (LinearLayout) itemView.findViewById(R.id.syncDetailLayout);

                parentLayout=(RelativeLayout)itemView.findViewById(R.id.lay);
                nameTV = (TextView) itemView.findViewById(R.id.nameTV);
                nameLocalTV=(TextView) itemView.findViewById(R.id.localNameTV);
                houseNoTV=(TextView)itemView.findViewById(R.id.houseNoTV);
                fatherNameTV=(TextView)itemView.findViewById(R.id.fatherNameTV);
                memberStatusTV=(TextView)itemView.findViewById(R.id.memberStatusTV) ;
                newRelationTV=(TextView)itemView.findViewById(R.id.newRelationTV) ;
                fatherNameLocalTV=(TextView) itemView.findViewById(R.id.localfatherNameTV);
                relationTV=(TextView) itemView.findViewById(R.id.relationTV);
                addressTV=(TextView) itemView.findViewById(R.id.addressTV);
                genderTV=(TextView)itemView.findViewById(R.id.genderTV);
                // printLayout=(LinearLayout)itemView.findViewById(R.id.verifiedSynced);
                memberStatusBT=(Button)itemView.findViewById(R.id.memberStatusBT) ;
                errorLayout=(LinearLayout)itemView.findViewById(R.id.errorMsgLayout);
                errorTV=(TextView)itemView.findViewById(R.id.errorTV) ;
                errorLayout.setVisibility(View.GONE);

            }

        }
        public void addAll(List<SeccMemberItem> list) {

            dataSet.addAll(list);
            notifyDataSetChanged();
        }
        public OtherMemberAdapter(Context context, ArrayList<SeccMemberItem> data) {
            this.dataSet = data;;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.member_item, parent, false);
            //view.setOnClickListener(MainActivity.myOnClickListener);
            MyViewHolder myViewHolder = new MyViewHolder(view);
            return myViewHolder;
        }
        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

            holder.memberStatusBT.setVisibility(View.GONE);
            final SeccMemberItem item = dataSet.get(listPosition);
            // AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"111111111"+item.getSyncedStatus());
            AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Verified By : "+item.getAadhaarVerifiedBy());
         /*   if (item.getName() != null)
                holder.nameTV.setText(item.getName());

            if (item.getNameSl() != null)
                holder.nameLocalTV.setText(item.getNameSl());

            if(item.getAhlslnohhd()!=null)
                holder.houseNoTV.setText(item.getAhlslnohhd());

            if (item.getFathername() != null)
                holder.fatherNameTV.setText(item.getFathername());

            if (item.getFathernameSl() != null)
                holder.fatherNameLocalTV.setText(item.getFathernameSl());
            if (item.getGenderid() != null) {
                if (item.getGenderid().equalsIgnoreCase("1")) {
                    holder.genderTV.setText("Male");
                } else {
                    holder.genderTV.setText("Female");
                }
            }*/
            holder.newRelationTV.setText("-");
            holder.parentLayout.setBackgroundColor(AppUtility.getColor(context, R.color.white_shine));

            holder.memberStatusTV.setText("-");

            if(houseHoldItem!=null && houseHoldItem.getDataSource()!=null &&
                    houseHoldItem.getDataSource().trim().equalsIgnoreCase(AppConstant.RSBY_SOURCE)){
                showRsbyMemberList(item,holder);
            }else{
                showSeccMemberList(item,holder);
            }

            if(item.getMemStatus()!=null){
                //  AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Member Status : "+item.getMemStatus());
                for(MemberStatusItem memberStatusItem : memberStatusList){
                    //    AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Member Status1 : "+memberStatusItem.getStatusCode());
                    if(memberStatusItem.getStatusCode().equalsIgnoreCase(item.getMemStatus())){
                        //      AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Member Status2 : "+memberStatusItem.getStatusCode());
                        holder.memberStatusTV.setText(memberStatusItem.getStatusDesc());
                        break;
                    }
                }
            }
            holder.errorLayout.setVisibility(View.GONE);

            if(item.getError_code()!=null){
                holder.errorLayout.setVisibility(View.VISIBLE);
                holder.errorTV.setText(item.getError_msg());
            }


            holder.memberStatusBT.setVisibility(View.GONE);
          //  if(item.getSyncedStatus()!=null && (item.getSyncedStatus().trim().equalsIgnoreCase(AppConstant.SYNCED_STATUS_MEMBER) ||item.getSyncedStatus().trim().equalsIgnoreCase("1"))) {
            if (item.getSyncedStatus() != null ){
                if( item.getSyncedStatus().equalsIgnoreCase(AppConstant.SYNCED_STATUS_MEMBER)|| item.getSyncedStatus().equalsIgnoreCase("Y")|| item.getSyncedStatus().equalsIgnoreCase("1") ) {
                activity.hofStatusSP.setEnabled(false);
                activity.familyStatusSP.setEnabled(false);
                holder.memberStatusBT.setVisibility(View.VISIBLE);
                holder.syncDetailLayout.setVisibility(View.VISIBLE);
                holder.memberIdLayout.setVisibility(View.VISIBLE);
                holder.memberId.setText(item.getNhpsMemId());
                if(item.getSyncDt()!=null){
                    String dateTime;//= DateTimeUtil.convertTimeMillisIntoStringDate(Long.parseLong(item.getSyncDt()), AppConstant.SYNC_DATE_TIME);
                    try{
                        dateTime= DateTimeUtil.convertTimeMillisIntoStringDate(Long.parseLong(item.getSyncDt()), AppConstant.SYNC_DATE_TIME);
                    }catch (Exception ex){
                        dateTime = item.getSyncDt();
                    }
                    holder.syncDate.setText(dateTime);}
                holder.memberStatusBT.setText(getResources().getString(R.string.synced));
                holder.memberStatusBT.setBackgroundColor(AppUtility.getColor(context, R.color.sync_color));
                holder.memberStatusBT.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openPreview(item);
                    }
                });
            }}else{
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
                                            SeccMemberListActivity.openReset(item,holder.memberStatusBT,context,activity);

                                        }
                                    });

                                } else if (item.getMemStatus() != null && item.getMemStatus().trim().equalsIgnoreCase(AppConstant.MEMBERFOUND_BUT_NOT_PRESENT)) {
                                    if (item.getAadhaarStatus() != null && item.getAadhaarStatus().trim().
                                            equalsIgnoreCase(AppConstant.AADHAAR_STAT)) {
                                        if (item.getAadhaarAuth() != null && item.getAadhaarAuth().trim().equalsIgnoreCase(AppConstant.VALID_STATUS)) {
                                            holder.memberStatusBT.setVisibility(View.VISIBLE);
                                            holder.memberStatusBT.setText(getResources().getString(R.string.locked));
                                            holder.memberStatusBT.setBackgroundColor(AppUtility.getColor(context, R.color.locked_color));

                                        }else   if (item.getAadhaarAuth() != null && item.getAadhaarAuth().trim().equalsIgnoreCase(AppConstant.PENDING_STATUS)) {
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
                                            SeccMemberListActivity.openReset(item,holder.memberStatusBT,context,activity);
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
                                            SeccMemberListActivity.openReset(item,holder.memberStatusBT,context,activity);
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
                        if(oldHeadItem!=null && oldHeadItem.getNhpsMemId()!=null &&
                                oldHeadItem.getNhpsMemId().trim().equalsIgnoreCase(item.getNhpsMemId().trim())) {
                            holder.memberStatusBT.setVisibility(View.VISIBLE);
                            holder.memberStatusBT.setText(getResources().getString(R.string.yet_to_survey));
                            holder.memberStatusBT.setBackgroundColor(AppUtility.getColor(context, R.color.yet_to_survey_color));
                            holder.memberStatusBT.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
// REDIRECT FOR YET TO SURVEY
                                    item.setNhpsRelationCode(AppConstant.NEW_HEAD_RELATION_CODE);
                                    item.setMemStatus(headStatus.getStatusCode());
                                    openYetToSurvey(item);
                                }
                            });
                        }else if(oldHeadItem!=null && oldHeadItem.getRsbyMemId()!=null &&
                                oldHeadItem.getRsbyMemId().trim().equalsIgnoreCase(item.getRsbyMemId().trim())){
                            holder.memberStatusBT.setVisibility(View.VISIBLE);
                            holder.memberStatusBT.setText(getResources().getString(R.string.yet_to_survey));
                            holder.memberStatusBT.setBackgroundColor(AppUtility.getColor(context, R.color.yet_to_survey_color));
                            holder.memberStatusBT.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
// REDIRECT FOR YET TO SURVEY
                                    item.setNhpsRelationCode(AppConstant.NEW_HEAD_RELATION_CODE);
                                    item.setMemStatus(headStatus.getStatusCode());
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
            return dataSet.size();
        }
        public void clearDataSource() {
            dataSet.clear();
            notifyDataSetChanged();
        }


    }

    private void showRsbyMemberList(SeccMemberItem item, OldHeadFragment.OtherMemberAdapter.MyViewHolder holder){
        if (item.getRsbyName() != null)
            holder.nameTV.setText(item.getRsbyName());
        /*if (item.getNameSl() != null)
            holder.nameLocalTV.setText(item.getNameSl());
            holder.houseNoTV.setText(item.getAhlslnohhd());*/
        /*if (item.getRsby() != null)
            holder.fatherNameTV.setText(item.getFathername());*/
        if(item!=null && item.getNhpsRelationCode()!=null && item.getNhpsRelationCode().trim().equalsIgnoreCase(AppConstant.HEAD_RELATION)){
            holder.parentLayout.setBackgroundColor(AppUtility.getColor(context,R.color.Bg_bal_color));
        }else{
            if(item!=null && item.getRsbyMemId()!=null && item.getRsbyMemId().trim().equalsIgnoreCase(houseHoldItem.getRsbyMemId())){
                holder.parentLayout.setBackgroundColor(AppUtility.getColor(context,R.color.Bg_bal_color));
            }
        }


        String gender="";
        if(item.getRsbyGender()!=null && item.getRsbyGender().trim().equalsIgnoreCase(AppConstant.MALE_GENDER)){
            gender=AppConstant.MALE;
        }else if(item.getRsbyGender()!=null && item.getRsbyGender().trim().equalsIgnoreCase(AppConstant.FEMALE_GENDER)){
            gender=AppConstant.FEMALE;
        }else{
            gender=AppConstant.OTHER_GENDER_NAME;
        }
        holder.genderTV.setText(gender);

        AppUtility.showLog(AppConstant.LOG_STATUS,"RSBY House hold","RSBY House hold Member"+item.serialize());
        if(item.getNhpsRelationCode()!=null){
            for(RelationItem item1 : relationList){
                if(item1.getRelationCode().equalsIgnoreCase(item.getNhpsRelationCode())){
                    holder.newRelationTV.setText(item1.getRelationName());
                    break;
                }
            }
        }
    }

    private void showSeccMemberList(SeccMemberItem item, OldHeadFragment.OtherMemberAdapter.MyViewHolder holder){

       if(item!=null && item.getNhpsRelationCode()!=null && item.getNhpsRelationCode().trim().equalsIgnoreCase(AppConstant.HEAD_RELATION)){
           holder.parentLayout.setBackgroundColor(AppUtility.getColor(context,R.color.Bg_bal_color));
       }else{
           if(item!=null && item.getNhpsMemId()!=null && item.getNhpsMemId().trim().equalsIgnoreCase(houseHoldItem.getNhpsMemId())){
               holder.parentLayout.setBackgroundColor(AppUtility.getColor(context,R.color.Bg_bal_color));
           }
       }

        if (item.getName() != null)
            holder.nameTV.setText(item.getName());
        if (item.getNameSl() != null)
            holder.nameLocalTV.setText(Html.fromHtml(item.getNameSl()));
        holder.houseNoTV.setText(item.getAhlslnohhd());
        if (item.getFathername() != null)
            holder.fatherNameTV.setText(item.getFathername());
        if (item.getFathernameSl() != null)
            holder.fatherNameLocalTV.setText(item.getFathernameSl());
        if (item.getGenderid() != null) {
            if (item.getGenderid().equalsIgnoreCase("1")) {
                holder.genderTV.setText(context.getResources().getString(R.string.genderMale));
            } else {
                holder.genderTV.setText(context.getResources().getString(R.string.genderFemale));
            }
        }

        AppUtility.showLog(AppConstant.LOG_STATUS,"RSBY House hold","RSBY House hold Member"+item.serialize());

        if(item.getNhpsRelationCode()!=null){
            for(RelationItem item1 : relationList){
                if(item1.getRelationCode().equalsIgnoreCase(item.getNhpsRelationCode())){
                    holder.newRelationTV.setText(item1.getRelationName());
                    break;
                }
            }
        }
    }

    private void openPreview(SeccMemberItem item){


        selectedMemberItem.setSeccMemberItem(item);
        // selectedMemberItem.setOldHeadMember(oldHeadItem);
        selectedMemberItem.setHouseHoldItem(houseHoldItem);
        ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.SELECTED_ITEM_FOR_VERIFICATION, selectedMemberItem.serialize(), context);
        Intent theIntent = new Intent(context, MemberPreviewActivity.class);
        startActivity(theIntent);

    }
    private void openYetToSurvey(SeccMemberItem item){
        AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Relation Code : "+item.getNhpsRelationCode());
        houseHoldItem.setHhStatus(activity.householdStatus.getStatusCode());
        item.setHhStatus(houseHoldItem.getHhStatus());
        selectedMemberItem.setSeccMemberItem(item);
        selectedMemberItem.setHouseHoldItem(houseHoldItem);
        selectedMemberItem.setOldHeadMember(oldHeadItem);
        String str=item.serialize();
        System.out.print(str);
        ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.SELECTED_ITEM_FOR_VERIFICATION, selectedMemberItem.serialize(), context);
        Intent theIntent=new Intent(context,SeccMemberDetailActivity.class);
        startActivity(theIntent);

    }
    private void displayList(){
        if(houseHoldMemberList!=null) {
            adapter = new OtherMemberAdapter(context, houseHoldMemberList);
            memberList.setAdapter(adapter);

            boolean allLocked = true;
            for(SeccMemberItem item : houseHoldMemberList){
                System.out.print(item);
                if(item.getLockedSave()!=null && item.getLockedSave().equalsIgnoreCase(AppConstant.LOCKED+"")){
                    if(item.getSyncedStatus()!=null ){
                        if(item.getSyncedStatus().equalsIgnoreCase(AppConstant.SYNCED_STATUS)){
                            allLocked= false;
                            break;
                        }
                        // allLocked = true;
                    }else{
                        allLocked = true;

                    }

                }else{
                    allLocked = false;
                    break;
                }
            }
            if(allLocked){
                footerSyncLayout.setVisibility(View.VISIBLE);
                syncBT.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        syncHousehold();
                    }
                });
            }

        }
    }

    private void askPinToLock(final String status,final SeccMemberItem item) {
        AppUtility.softKeyBoard(activity,1);
        askForPinDailog = new AlertDialog.Builder(context).create();
        LayoutInflater factory = LayoutInflater.from(context);
        View alertView = factory.inflate(R.layout.ask_pin_layout, null);
        askForPinDailog.setView(alertView);
        askForPinDailog.show();
        // Log.d(TAG,"delete status :"+deleteStatus);
        // dialog.setContentView(R.layout.opt_auth_layout);
        //    final TextView otpAuthMsg=(TextView)alertView.findViewById(R.id.otpAuthMsg);
        final VerifierLoginResponse verifierDetail = VerifierLoginResponse.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.VERIFIER_CONTENT, context));
        final EditText pinET = (EditText) alertView.findViewById(R.id.deletPinET);
        final TextView errorTV = (TextView) alertView.findViewById(R.id.invalidOtpTV);

        wrongAttempetCountText = (TextView) alertView.findViewById(R.id.wrongAttempetCountText);
        wrongAttempetCountValue = (TextView) alertView.findViewById(R.id.wrongAttempetCountValue);



        Button proceedBT = (Button) alertView.findViewById(R.id.proceedBT);
        Button cancelBT = (Button) alertView.findViewById(R.id.cancelBT);
        proceedBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                currentTime = System.currentTimeMillis();
                try {

                    wrongPinSavedTime = Long.parseLong(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.WORNG_PIN_ENTERED_TIMESTAMP, context));
                } catch (Exception ex) {
                    wrongPinSavedTime = 0;
                }
                if (currentTime > (wrongPinSavedTime + millisecond24)) {


                AppUtility.softKeyBoard(activity,0);
                String pin = pinET.getText().toString();
                if (pin.toString().equalsIgnoreCase(verifierDetail.getPin())) {
                    if(status.equalsIgnoreCase(SeccMemberListActivity.RESET)){
                        SeccDatabase.resetData(item,context);
                        askForPinDailog.dismiss();
                        getActivity().recreate();
                    }else if(status.equalsIgnoreCase(SeccMemberListActivity.EDIT)){
                        SeccDatabase.editRecord(item,context);
                        askForPinDailog.dismiss();
                        activity.recreate();
                    }
                } else if (pin.equalsIgnoreCase("")) {
                    // CustomAlert.alertWithOk(context,"Please enter valid pin");
                    errorTV.setVisibility(View.VISIBLE);
                    errorTV.setText(context.getResources().getString(R.string.plzEnterPin));
                } else if (!pin.toString().equalsIgnoreCase(verifierDetail.getPin())) {

                    if (wrongPinCount >= 2) {
                        errorTV.setTextColor(context.getResources().getColor(R.color.red));
                        wrongAttempetCountValue.setTextColor(context.getResources().getColor(R.color.red));
                        wrongAttempetCountText.setTextColor(context.getResources().getColor(R.color.red));
                    }
                    wrongPinCount++;
                    wrongAttempetCountValue.setText((3 - wrongPinCount)+"");
                    if (wrongPinCount > 2) {
                        long time = System.currentTimeMillis();
                        ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.WORNG_PIN_ENTERED_TIMESTAMP, time + "", context);
                        CustomAlert.alertWithOk(context, context.getResources().getString(R.string.youHaveExceedPinLimit));
                    } else {
                        errorTV.setVisibility(View.VISIBLE);
                        errorTV.setText("Enter correct pin");
                        pinET.setText("");
//                        CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterValidPin));
//                        pinET.setText("");
                    }
//                    errorTV.setVisibility(View.VISIBLE);
//                    errorTV.setText(context.getResources().getString(R.string.plzEnterValidPin));
                }

            } else {

                //alert  when pin login is diabled for 24 hrs
                CustomAlert.alertWithOk(context, context.getResources().getString(R.string.pinLoginDisabled));
                errorTV.setText("Pin login disabled for 24 hrs.");
                pinET.setText("");
                return;
            }
            }
        });
        cancelBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askForPinDailog.dismiss();
            }
        });
    }

    private void syncHousehold(){
        Intent theIntent = new Intent(getActivity(), SyncHouseholdActivity.class);
        //editedbysaurabh
        theIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(theIntent);
        getActivity().finish();
    }
}
