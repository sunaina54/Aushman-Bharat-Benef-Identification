package com.nhpm.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.nhpm.activity.SearchActivityWithHouseHold;
import com.nhpm.activity.SeccMemberListActivity;
import com.nhpm.activity.SyncHouseholdActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link  interface
 * to handle interaction events.
 * Use the {@link DefaultFamilyListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DefaultFamilyListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Context context;
    private ArrayList<RelationItem> relationList;
    private SelectedMemberItem selectedMemberItem;
    private HouseHoldItem houseHoldItem;
    private ArrayList<SeccMemberItem> houseHoldMemberList;
    private SeccMemberItem newHeadItem, oldHeadItem;
    private RecyclerView memberList;
    private ArrayList<MemberStatusItem> memberStatusList;
    private MemberStatusItem headStatus;
    private SeccMemberListActivity activity;
    private ArrayList<FamilyStatusItem> familyStatusList;
    private OtherMemberAdapter adapter;
    private LinearLayout footerLayout;
    private Button saveBT, lockBT;
    private FamilyStatusItem householdStatus;
    private String urnNumber;
    private AlertDialog askForPinDailog, lockDialog;
    private VerifierLoginResponse loginDetail;
    private LinearLayout footerSyncAllLayout;
    private Button syncButton;
    private Fragment mFragment;
    private FragmentManager mFragmentManager;

    private int wrongPinCount = 0;
    private long wrongPinSavedTime;
    private long currentTime;
    private TextView wrongAttempetCountValue, wrongAttempetCountText;
    private long millisecond24 = 86400000;


    public DefaultFamilyListFragment() {

        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static DefaultFamilyListFragment newInstance(String param1, String param2) {
        DefaultFamilyListFragment fragment = new DefaultFamilyListFragment();
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
        View view = inflater.inflate(R.layout.fragment_default_family_list, container, false);

        setupScreen(view);
        return view;
    }

    private void setupScreen(View view) {
        context = getActivity();
        mFragmentManager = getFragmentManager();
        relationList = SeccDatabase.getRelationList(context);
        selectedMemberItem = SelectedMemberItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.SELECTED_ITEM_FOR_VERIFICATION, context));
        loginDetail = VerifierLoginResponse.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.VERIFIER_CONTENT, context));
        if (selectedMemberItem.getHouseHoldItem() != null) {
            houseHoldItem = selectedMemberItem.getHouseHoldItem();
            // Log.d(TAG,"Household Status : "+houseHoldItem.getHhStatus());
        }
        householdStatus = activity.householdStatus;
        memberStatusList = SeccDatabase.getMemberStatusList(context);
        //findHouseholdMember();
        footerLayout = (LinearLayout) view.findViewById(R.id.footerLayout);
        saveBT = (Button) view.findViewById(R.id.saveBT);
        lockBT = (Button) view.findViewById(R.id.lockBT);
        lockBT.setVisibility(View.GONE);
        syncButton = (Button) view.findViewById(R.id.syncBotton);
        footerSyncAllLayout = (LinearLayout) view.findViewById(R.id.footerSyncAllLayout);

        memberList = (RecyclerView) view.findViewById(R.id.memberList);
        //   memberList.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        memberList.setLayoutManager(layoutManager);
        memberList.setItemAnimator(new DefaultItemAnimator());
        footerLayout.setVisibility(View.GONE);
        // AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Verified By : "+householdStatus.getStatusCode());

        if (householdStatus != null && householdStatus.getStatusCode().equalsIgnoreCase(AppConstant.DEFAULT_HOUSEHOLD)) {

           // by Rajesh kumar if new head list is null
            if (SeccMemberListActivity.isFlagNoNewHead) {
                footerLayout.setVisibility(View.VISIBLE);
                footerLayout.bringToFront();
                lockBT.setVisibility(View.VISIBLE);
                SeccMemberListActivity.isFlagNoNewHead =false;
            }else {
                footerLayout.setVisibility(View.GONE);
            }
        } else if (householdStatus != null && householdStatus.getStatusCode().equalsIgnoreCase(AppConstant.HOUSEHOLD_FOUND)) {

          //   by Rajesh kumar if new head list is null
            if (SeccMemberListActivity.isFlagNoNewHead) {
                footerLayout.setVisibility(View.VISIBLE);
                footerLayout.bringToFront();
                lockBT.setVisibility(View.VISIBLE);
                SeccMemberListActivity.isFlagNoNewHead =false;
            }else {
                footerLayout.setVisibility(View.GONE);
            }
        } else{
            footerLayout.setVisibility(View.VISIBLE);
            footerLayout.bringToFront();
            lockBT.setVisibility(View.VISIBLE);
        }

//        if (SeccMemberListActivity.isFlagNoNewHead) {
////
//        } else{}

        if (houseHoldItem != null && houseHoldItem.getLockSave() != null &&
                houseHoldItem.getLockSave().equalsIgnoreCase(AppConstant.LOCKED + "")) {
            activity.familyStatusSP.setEnabled(false);
            footerLayout.setVisibility(View.GONE);
        }
        displayList();
        saveBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveFamily();
            }
        });
        lockBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  lockPrompt();
                if (activity.householdStatus.getStatusCode().equalsIgnoreCase("9")) {
                    askUrnId();
                } else {
                    askPinToLock();
                }

            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (SeccMemberListActivity) context;

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private class OtherMemberAdapter extends
            RecyclerView.Adapter<OtherMemberAdapter.MyViewHolder> {
        private ArrayList<SeccMemberItem> dataSet;

        public class MyViewHolder extends RecyclerView.ViewHolder {

            Button memberStatusBT;
            LinearLayout errorLayout;
            TextView nameTV, nameLocalTV, houseNoTV, fatherNameTV, fatherNameLocalTV,
                    genderTV, addressTV,
                    relationTV, newRelationTV, memberStatusTV, errorTV, syncDate, memberId;
            RelativeLayout parentLayout;
            LinearLayout memberIdLayout, syncDetailLayout;

            public MyViewHolder(View itemView) {
                super(itemView);

                syncDate = (TextView) itemView.findViewById(R.id.syncDate);
                memberId = (TextView) itemView.findViewById(R.id.memberId);
                memberIdLayout = (LinearLayout) itemView.findViewById(R.id.memberIdLayout);
                syncDetailLayout = (LinearLayout) itemView.findViewById(R.id.syncDetailLayout);

                parentLayout = (RelativeLayout) itemView.findViewById(R.id.lay);
                nameTV = (TextView) itemView.findViewById(R.id.nameTV);
                nameLocalTV = (TextView) itemView.findViewById(R.id.localNameTV);
                houseNoTV = (TextView) itemView.findViewById(R.id.houseNoTV);
                fatherNameTV = (TextView) itemView.findViewById(R.id.fatherNameTV);
                memberStatusTV = (TextView) itemView.findViewById(R.id.memberStatusTV);
                newRelationTV = (TextView) itemView.findViewById(R.id.newRelationTV);
                fatherNameLocalTV = (TextView) itemView.findViewById(R.id.localfatherNameTV);
                relationTV = (TextView) itemView.findViewById(R.id.relationTV);
                addressTV = (TextView) itemView.findViewById(R.id.addressTV);
                genderTV = (TextView) itemView.findViewById(R.id.genderTV);
                // printLayout=(LinearLayout)itemView.findViewById(R.id.verifiedSynced);
                memberStatusBT = (Button) itemView.findViewById(R.id.memberStatusBT);
                memberStatusBT.setVisibility(View.GONE);
                errorLayout = (LinearLayout) itemView.findViewById(R.id.errorMsgLayout);
                errorTV = (TextView) itemView.findViewById(R.id.errorTV);
                errorLayout.setVisibility(View.GONE);


            }

        }

        public void addAll(List<SeccMemberItem> list) {

            dataSet.addAll(list);
            notifyDataSetChanged();
        }

        public OtherMemberAdapter(Context context, ArrayList<SeccMemberItem> data) {
            this.dataSet = data;
            ;
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
            /*if (item.getName() != null)
                holder.nameTV.setText(item.getName());
            if (item.getNameSl() != null)
                holder.nameLocalTV.setText(item.getNameSl());
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
            }

            AppUtility.showLog(AppConstant.LOG_STATUS,"RSBY House hold","RSBY House hold Member"+item.serialize());

            if(item.getNhpsRelationCode()!=null){
                for(RelationItem item1 : relationList){
                    if(item1.getRelationCode().equalsIgnoreCase(item.getNhpsRelationCode())){
                        holder.newRelationTV.setText(item1.getRelationName());
                        break;
                    }
                }
            }*/


            if (item != null && item.getDataSource() != null && item.getDataSource().trim().equalsIgnoreCase(AppConstant.RSBY_SOURCE)) {
                holder.parentLayout.setBackgroundColor(AppUtility.getColor(context, R.color.white));

                showRsbyMemberList(item, holder);
            } else {
                holder.parentLayout.setBackgroundColor(AppUtility.getColor(context, R.color.white));

                showSeccMemberList(item, holder);
            }
            if (item.getMemStatus() != null) {
                for (MemberStatusItem memberStatusItem : memberStatusList) {
                    if (memberStatusItem.getStatusCode().equalsIgnoreCase(item.getMemStatus())) {
                        holder.memberStatusTV.setText(memberStatusItem.getStatusDesc());
                        break;
                    }
                }
            }

            holder.errorLayout.setVisibility(View.GONE);
            if (item.getError_code() != null) {
                holder.errorLayout.setVisibility(View.VISIBLE);
                holder.errorTV.setText(item.getError_msg());
            }


            // holder.parentLayout.setBackgroundColor(AppUtility.getColor(context, R.color.white_shine));
            /*if (item.getRelation() != null) {
                if (item.getNhpsMemId().equalsIgnoreCase(houseHoldItem.getNhpsMemId())) {
                    holder.parentLayout.setBackgroundColor(AppUtility.getColor(context, R.color.Bg_bal_color));
                    holder.relationTV.setTextColor(AppUtility.getColor(context, R.color.green));
                } else {
                    holder.relationTV.setTextColor(AppUtility.getColor(context, R.color.black));
                }
                holder.relationTV.setText(item.getRelation());
            }
*/
            // if (item.getSyncedStatus() != null && item.getSyncedStatus().trim().equalsIgnoreCase(AppConstant.SYNCED_STATUS_MEMBER + "")) {
            if (item.getSyncedStatus() != null) {
                if (item.getSyncedStatus().equalsIgnoreCase(AppConstant.SYNCED_STATUS_MEMBER) || item.getSyncedStatus().equalsIgnoreCase("Y") || item.getSyncedStatus().equalsIgnoreCase("1")) {
                    holder.memberStatusBT.setVisibility(View.VISIBLE);

                    holder.syncDetailLayout.setVisibility(View.VISIBLE);
                    holder.memberIdLayout.setVisibility(View.VISIBLE);
                    holder.memberId.setText(item.getNhpsMemId());
                    if (item.getSyncDt() != null) {
                        String dateTime;
                        try {
                            dateTime = DateTimeUtil.convertTimeMillisIntoStringDate(Long.parseLong(item.getSyncDt()), AppConstant.SYNC_DATE_TIME);
                        } catch (Exception ex) {
                            dateTime = item.getSyncDt();

                        }
                        holder.syncDate.setText(dateTime);
                    }

                    holder.memberStatusBT.setText(getResources().getString(R.string.synced));
                    holder.memberStatusBT.setBackgroundColor(AppUtility.getColor(context, R.color.sync_color));
                    holder.memberStatusBT.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            openPreview(item);
                        }
                    });
                }
            } else if (item.getLockedSave() != null && item.getLockedSave().trim().equalsIgnoreCase(AppConstant.LOCKED + "")) {
                holder.memberStatusBT.setVisibility(View.VISIBLE);
                holder.memberStatusBT.setText(getResources().getString(R.string.locked));
                holder.memberStatusBT.setBackgroundColor(AppUtility.getColor(context, R.color.locked_color));
                holder.memberStatusBT.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // openPreview(item);
                        SeccMemberListActivity.openResetHousehold(item, holder.memberStatusBT, context, getActivity());

                    }
                });
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

    private void displayList() {
        if (houseHoldMemberList != null) {
            adapter = new OtherMemberAdapter(context, houseHoldMemberList);
            memberList.setAdapter(adapter);
            boolean allLocked = false;
            for (SeccMemberItem item : houseHoldMemberList) {
                System.out.print(item);
                /*if(item.get){

                }*/
                if (item.getLockedSave() != null && item.getLockedSave().equalsIgnoreCase(AppConstant.LOCKED + "")) {
                    if (item.getSyncedStatus() != null) {
                        if (item.getSyncedStatus().equalsIgnoreCase(AppConstant.SYNCED_STATUS)) {
                            allLocked = false;
                            break;
                        }
                        // allLocked = true;
                    } else {
                        allLocked = true;

                    }

                } else {
                    allLocked = false;
                    break;
                }
            }
            if (allLocked) {
                footerSyncAllLayout.setVisibility(View.VISIBLE);
                syncButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        syncHousehold();
                    }
                });
            }

        }
    }

    private void openPreview(SeccMemberItem item) {
        selectedMemberItem.setSeccMemberItem(item);
        //selectedMemberItem.setOldHeadMember(oldHeadItem);
        selectedMemberItem.setHouseHoldItem(houseHoldItem);
        ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.SELECTED_ITEM_FOR_VERIFICATION, selectedMemberItem.serialize(), context);
        Intent theIntent = new Intent(context, MemberPreviewActivity.class);
        startActivity(theIntent);
        // finish();
        //leftTransition();

    }

    private void saveFamily() {
        houseHoldItem.setHhStatus(householdStatus.getStatusCode());
        for (SeccMemberItem item : houseHoldMemberList) {
            item.setHhStatus(householdStatus.getStatusCode());
            if (householdStatus.getStatusCode() != null && !householdStatus.getStatusCode().equalsIgnoreCase(AppConstant.HOUSEHOLD_FOUND)) {
                item.setMemStatus(AppConstant.NO_INFO_AVAIL);
            }
            item.setAadhaarVerifiedBy(loginDetail.getAadhaarNumber());
            item.setLockedSave(AppConstant.SAVE + "");
            SeccDatabase.updateSeccMember(item, context);
        }
        houseHoldItem.setLockSave(AppConstant.SAVE + "");
        SeccDatabase.updateHouseHold(houseHoldItem, context);
        Intent theIntent = new Intent(context, SearchActivityWithHouseHold.class);
        startActivity(theIntent);
        activity.finish();
        activity.leftTransition();
    }

    private void lockPrompt() {
        lockDialog = new AlertDialog.Builder(context).create();
        LayoutInflater factory = LayoutInflater.from(context);
        View alertView = factory.inflate(R.layout.delete_house_hold_prompt, null);
        lockDialog.setView(alertView);
        // dialog.setContentView(R.layout.opt_auth_layout);
        //    final TextView otpAuthMsg=(TextView)alertView.findViewById(R.id.otpAuthMsg);

        Button syncBT = (Button) alertView.findViewById(R.id.syncBT);
        syncBT.setText(context.getResources().getString(R.string.OK));
        Button cancelBT = (Button) alertView.findViewById(R.id.cancelBT);
        final TextView deletePromptTV = (TextView) alertView.findViewById(R.id.deleteMsg);
        // long underSurvey=SeccDatabase.countUnderSurveyedHousehold(context,"","");
        String msg = getResources().getString(R.string.locked_msg);
        //   otpAuthMsg.setText("Please enter OTP sent by the UIDAI on your Aadhaar registerd mobile number(XXXXXX0906");
        deletePromptTV.setText(msg);
        syncBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lockDialog.dismiss();
                /*Intent theIntent=new Intent(context,SyncHouseholdActivity.class);
                startActivity(theIntent);
                finish();
                leftTransition();*/

                askPinToLock();
            }
        });


        cancelBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lockDialog.dismiss();
            }
        });
        lockDialog.show();
    }

    private void askPinToLock() {
        AppUtility.softKeyBoard(activity, 1);
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


        pinET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // errorTV.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
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


                    AppUtility.softKeyBoard(activity, 0);
                    String pin = pinET.getText().toString();
                    if (pin.toString().equalsIgnoreCase(verifierDetail.getPin())) {
                        lockSubmit();
                    } else if (pin.equalsIgnoreCase("")) {
                        // CustomAlert.alertWithOk(context,"Please enter valid pin");
                        errorTV.setVisibility(View.VISIBLE);
                        errorTV.setText("Enter pin");
                        pinET.setText("");
                    } else if (!pin.toString().equalsIgnoreCase(verifierDetail.getPin())) {

                        if (wrongPinCount >= 2) {
                            errorTV.setTextColor(context.getResources().getColor(R.color.red));
                            wrongAttempetCountValue.setTextColor(context.getResources().getColor(R.color.red));
                            wrongAttempetCountText.setTextColor(context.getResources().getColor(R.color.red));
                        }
                        wrongPinCount++;
                        wrongAttempetCountValue.setText((3 - wrongPinCount) + "");
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
//                    errorTV.setText("Enter correct pin");
//                    pinET.setText("");
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

    private void lockSubmit() {
        houseHoldItem.setHhStatus(householdStatus.getStatusCode());

        for (SeccMemberItem item : houseHoldMemberList) {
            item.setHhStatus(houseHoldItem.getHhStatus());
                   /* if(houseHoldItem.getHhStatus().equalsIgnoreCase(AppConstant.HOUSEHOLD_NOT_FOUND)){
                        item.setMemStatus(AppConstant.MEMBER_NOT_FOUND);
                    }
                    if(houseHoldItem.getHhStatus().equalsIgnoreCase(AppConstant.HOUSEHOLD_LOCKED)){
                        item.setMemStatus(AppConstant.NO_INFO_AVAIL);
                    }

                    if(houseHoldItem.getHhStatus().equalsIgnoreCase(AppConstant.FAMILY_MIGRATED)){
                        item.setMemStatus(AppConstant.MEMBER_MIGRATED);
                    }
                    if(houseHoldItem.getHhStatus().equalsIgnoreCase(AppConstant.HOUSEHOLD_TO_ENROLL_RSBY)){
                        item.setMemStatus(AppConstant.MEMBER_ENROL_THROUGH_RSBY);
                    }*/


            if (houseHoldItem.getHhStatus().equalsIgnoreCase(AppConstant.HOUSEHOLD_FOUND)) {
            } else {
                // item=AppUtility.addAdditionalParamInSecc(item,context);
                item.setMemStatus(AppConstant.NO_INFO_AVAIL);

            }
           /* if(houseHoldItem.getHhStatus().equalsIgnoreCase(AppConstant.FAMILY_MIGRATED)){
                item.setMemStatus(AppConstant.NO_INFO_AVAIL);
            }
            if(houseHoldItem.getHhStatus().equalsIgnoreCase(AppConstant.HOUSEHOLD_TO_ENROLL_RSBY)){
                item.setMemStatus(AppConstant.MEMBER_ENROL_THROUGH_RSBY);
            }
            if(houseHoldItem.getHhStatus().equalsIgnoreCase(AppConstant.NO_FAMILY_LIVING)){
                item.setMemStatus(AppConstant.NO_INFO_AVAIL);
            }*/

                    /*if(houseHoldItem.getHhStatus().equalsIgnoreCase(AppConstant.NO_MEMBER_LIVING)){
                        item.setMemberStatus(AppConstant.NO_INFO_AVAIL);
                    }*/
            //item.setMemberStatus(AppConstant.NO_INFO_AVAIL);
            item.setAadhaarVerifiedBy(loginDetail.getAadhaarNumber());
            item.setLockedSave(AppConstant.LOCKED + "");
            SeccDatabase.updateSeccMember(item, context);
        }
        // houseHoldItem=AppUtility.addAdditionalParamInHousehold(houseHoldItem,context);
        houseHoldItem.setLockSave(AppConstant.LOCKED + "");
        houseHoldItem.setUrn_no(urnNumber);
        SeccDatabase.updateHouseHold(houseHoldItem, context);
        Intent theIntent = new Intent(context, SearchActivityWithHouseHold.class);
        startActivity(theIntent);
        activity.finish();
        activity.leftTransition();
    }

    private void syncHousehold() {
        Intent theIntent = new Intent(getActivity(), SyncHouseholdActivity.class);
        startActivity(theIntent);
    }

    private void showRsbyMemberList(SeccMemberItem item, OtherMemberAdapter.MyViewHolder holder) {
        if (item.getRsbyName() != null) {
            holder.nameTV.setText(item.getRsbyName());
        } else {
            holder.nameTV.setText(item.getName());
        }
        /*if (item.getNameSl() != null)
            holder.nameLocalTV.setText(item.getNameSl());
            holder.houseNoTV.setText(item.getAhlslnohhd());*/
        /*if (item.getRsby() != null)
            holder.fatherNameTV.setText(item.getFathername());*/
      /*  if(item!=null && item.getRsbyRelcode()!=null && item.getRsbyRelcode().trim().equalsIgnoreCase("1")){
            holder.parentLayout.setBackgroundColor(AppUtility.getColor(context,R.color.Bg_bal_color));
        }else{*/
        if (item != null && item.getRsbyMemId() != null && item.getRsbyMemId().trim().equalsIgnoreCase(houseHoldItem.getRsbyMemId())) {
            holder.parentLayout.setBackgroundColor(AppUtility.getColor(context, R.color.Bg_bal_color));
        }
      /*  }*/
        String gender = "";
        if (item.getRsbyGender() != null && item.getRsbyGender().trim().equalsIgnoreCase(AppConstant.MALE_GENDER)) {
            gender = AppConstant.MALE;
        } else if (item.getRsbyGender() != null && item.getRsbyGender().trim().equalsIgnoreCase(AppConstant.FEMALE_GENDER)) {
            gender = AppConstant.FEMALE;
        } else {
            gender = AppConstant.OTHER_GENDER_NAME;
        }
        holder.genderTV.setText(gender);

        AppUtility.showLog(AppConstant.LOG_STATUS, "RSBY House hold", "RSBY House hold Member" + item.serialize());
        if (item.getNhpsRelationCode() != null) {
            for (RelationItem item1 : relationList) {
                if (item1.getRelationCode().equalsIgnoreCase(item.getNhpsRelationCode())) {
                    holder.newRelationTV.setText(item1.getRelationName());
                    break;
                }
            }
        }
    }

    private void showSeccMemberList(SeccMemberItem item, OtherMemberAdapter.MyViewHolder holder) {
        if (item.getName() != null)
            holder.nameTV.setText(item.getName());
        if (item.getNameSl() != null)
            holder.nameLocalTV.setText(Html.fromHtml(item.getNameSl()));
        holder.houseNoTV.setText(item.getAhlslnohhd());
        if (item.getFathername() != null)
            holder.fatherNameTV.setText(item.getFathername());
        if (item.getFathernameSl() != null)
            holder.fatherNameLocalTV.setText(Html.fromHtml(item.getFathernameSl()));
        if (item.getGenderid() != null) {
            if (item.getGenderid().equalsIgnoreCase("1")) {
                holder.genderTV.setText("Male");
            } else {
                holder.genderTV.setText("Female");
            }
        }
        if (item != null && houseHoldItem != null && houseHoldItem.getNhpsMemId() != null
                && item.getNhpsMemId() != null && item.getNhpsMemId().trim().equalsIgnoreCase(houseHoldItem.getNhpsMemId().trim())) {
            holder.parentLayout.setBackgroundColor(AppUtility.getColor(context, R.color.Bg_bal_color));
            ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SELECTED_HEAD_GENDER_ID, item.getGenderid(), context);

        }

        if (item != null && item.getNhpsRelationCode() != null && item.getNhpsRelationCode().trim().equalsIgnoreCase(AppConstant.HEAD_RELATION)) {
            holder.parentLayout.setBackgroundColor(AppUtility.getColor(context, R.color.Bg_bal_color));
            ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SELECTED_HEAD_GENDER_ID, item.getGenderid(), context);

        }/*else{
            if(item!=null && item.getRsbyMemId()!=null && item.getRsbyMemId().trim().equalsIgnoreCase(houseHoldItem.getRsbyMemId())){
                holder.parentLayout.setBackgroundColor(AppUtility.getColor(context,R.color.Bg_bal_color));
            }
        }*/
        AppUtility.showLog(AppConstant.LOG_STATUS, "RSBY House hold", "RSBY House hold Member" + item.serialize());

        if (item.getNhpsRelationCode() != null) {
            for (RelationItem item1 : relationList) {
                if (item1.getRelationCode().equalsIgnoreCase(item.getNhpsRelationCode())) {
                    holder.newRelationTV.setText(item1.getRelationName());
                    break;
                }
            }
        }
    }

    private void askUrnId() {
        AppUtility.softKeyBoard(activity, 1);
        askForPinDailog = new AlertDialog.Builder(context).create();
        LayoutInflater factory = LayoutInflater.from(context);
        View alertView = factory.inflate(R.layout.ask_urn_layout, null);
        askForPinDailog.setView(alertView);
        askForPinDailog.show();
        // Log.d(TAG,"delete status :"+deleteStatus);
        // dialog.setContentView(R.layout.opt_auth_layout);
        final TextView invalidUrnTV = (TextView) alertView.findViewById(R.id.invalidUrnTV);
        final VerifierLoginResponse verifierDetail = VerifierLoginResponse.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.VERIFIER_CONTENT, context));
        final EditText pinET = (EditText) alertView.findViewById(R.id.deletPinET);

        pinET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // errorTV.setVisibility(View.GONE);
                if (pinET.getText().toString().length() == 17) {
                    pinET.setTextColor(context.getResources().getColor(R.color.green));
                    invalidUrnTV.setVisibility(View.GONE);
                } else {
                    pinET.setTextColor(context.getResources().getColor(R.color.red));
                    invalidUrnTV.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        Button proceedBT = (Button) alertView.findViewById(R.id.proceedBT);
        Button cancelBT = (Button) alertView.findViewById(R.id.cancelBT);
        proceedBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pinET.getText().toString().length() == 17) {
                    urnNumber = pinET.getText().toString();
                    askForPinDailog.dismiss();
                    askPinToLock();
                } else {
                    invalidUrnTV.setVisibility(View.VISIBLE);
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
}
