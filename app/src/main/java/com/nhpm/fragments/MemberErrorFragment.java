package com.nhpm.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
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
import com.nhpm.activity.SyncPreviewActivity;

import java.util.ArrayList;
import java.util.List;

import static android.support.v7.widget.StaggeredGridLayoutManager.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MemberErrorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MemberErrorFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ArrayList<SeccMemberItem> memberList;
    private RecyclerView errorMemberList;
    private Context context;
    private MemberAdapter memberAdapter;
    private ArrayList <FamilyStatusItem> familyList;
    private ArrayList<MemberStatusItem> memberStatusList;
    private LinearLayout sortLayout;
    private SelectedMemberItem selectedMemberItem;
    private HouseHoldItem houseHoldItem;
    public ArrayList<SeccMemberItem> getMemberList() {
        return memberList;
    }

    public void setMemberList(ArrayList<SeccMemberItem> memberList) {
        this.memberList = memberList;
    }
    public MemberErrorFragment() {
        // Required empty public constructor
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MemberErrorFragment.
     */
    // TODO: Rename and change types and number of parameters

    public static MemberErrorFragment newInstance(String param1, String param2) {
        MemberErrorFragment fragment = new MemberErrorFragment();
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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_member_eroor, container, false);
        setupScreen(view);
        return view;
    }
    private void setupScreen(View view){
        context=getActivity();
        selectedMemberItem=SelectedMemberItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.SELECTED_ITEM_FOR_VERIFICATION,context));
        houseHoldItem=selectedMemberItem.getHouseHoldItem();
        sortListByHead();
        sortLayout=(LinearLayout)view.findViewById(R.id.sortLayout) ;
        sortLayout.setVisibility(View.GONE);
        familyList= SeccDatabase.getFamilyStatusList(context);
        memberStatusList=SeccDatabase.getMemberStatusList(context);
        errorMemberList=(RecyclerView) view.findViewById(R.id.errorMemberList);
        errorMemberList.setHasFixedSize(true);
        LinearLayoutManager manager=new LinearLayoutManager(context);
        errorMemberList.setLayoutManager(manager);
        errorMemberList.setItemAnimator(new DefaultItemAnimator());
        if(memberList!=null && memberList.size()>0){
            memberAdapter=new MemberAdapter(context,memberList);
            errorMemberList.setAdapter(memberAdapter);
        }
    }
    private void sortListByHead(){
        ArrayList<SeccMemberItem> list=new ArrayList<>();
        SeccMemberItem head=null;
        if(houseHoldItem!=null && houseHoldItem.getHhStatus()!=null && houseHoldItem.getHhStatus().trim().equalsIgnoreCase(AppConstant.HOUSEHOLD_FOUND)){
             for(SeccMemberItem item : memberList){
                 AppUtility.showLog(AppConstant.LOG_STATUS,"MemberErrorFragment",
                         item.getNhpsRelationCode()+" Name :"+item.getName());
                 if(item.getNhpsRelationCode()!=null && item.getNhpsRelationCode().trim().equalsIgnoreCase(AppConstant.NEW_HEAD_RELATION_CODE)){
                     head=item;
                 }else {
                     list.add(item);
                 }
             }
            list.add(0,head);
        }else {
            for (SeccMemberItem item : memberList) {
                if (houseHoldItem != null && item.getNhpsMemId().trim().equalsIgnoreCase(houseHoldItem.getNhpsMemId().trim())) {
                    head = item;
                } else {
                    list.add(item);
                }
            }
            list.add(0, head);
        }
        memberList=list;
    }


    private class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.MyViewHolder> {

        View view;
        AlertDialog dialog;
        private ArrayList<SeccMemberItem> dataSet;
        private Context context;
        private TextView text;
        public class MyViewHolder extends RecyclerView.ViewHolder   {
            TextView nameTV,localNameTV,fatherNameTV,houseHoldIDTV,errorTV,housholdStatusTV,
                    memberStatusTV,addressTV,syncDateTV,nhpsMemId;
            Button syncStatusBT;
            LinearLayout errorMsgLayout,syncTimeLayout;
            RelativeLayout memberLayout;
            Button printBT;
            public MyViewHolder(View itemView) {
                super(itemView);
                this.localNameTV = (TextView) itemView.findViewById(R.id.localNameTV);
                this.nameTV = (TextView) itemView.findViewById(R.id.memberNametTV);
                this.fatherNameTV = (TextView) itemView.findViewById(R.id.fatherNameTV);
                this.errorTV=(TextView) itemView.findViewById(R.id.errorTV);
                this.houseHoldIDTV=(TextView)itemView.findViewById(R.id.householdIdTV);
                this.housholdStatusTV=(TextView)itemView.findViewById(R.id.householdStatusTV);
                this.memberStatusTV=(TextView)itemView.findViewById(R.id.memberStatusTV);
                this.addressTV=(TextView)itemView.findViewById(R.id.addressTV) ;
                this.syncStatusBT=(Button) itemView.findViewById(R.id.memberStatusBT);
                this.errorMsgLayout=(LinearLayout)itemView.findViewById(R.id.errorMsgLayout);
                this.memberLayout=(RelativeLayout)itemView.findViewById(R.id.memberRowLayout) ;
                this.nhpsMemId=(TextView)itemView.findViewById(R.id.nhpsMemId);
                errorMsgLayout.setVisibility(View.GONE);
                syncDateTV=(TextView)itemView.findViewById(R.id.syncDateTV) ;
                syncTimeLayout=(LinearLayout)itemView.findViewById(R.id.syncTimeLayout);
                syncTimeLayout.setVisibility(View.VISIBLE);
                //syncTimeLayout.setVisibility(View.GONE);

            }

        }
        public void addAll(List<SeccMemberItem> list) {

            dataSet.addAll(list);
            notifyDataSetChanged();
        }
        public MemberAdapter(Context context, ArrayList<SeccMemberItem> data) {
            this.dataSet =data;
            this.context=context;
            this.text=text;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_member_error_layout, parent, false);

            //view.setOnClickListener(MainActivity.myOnClickListener);

            MyViewHolder myViewHolder = new MyViewHolder(view);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

            final SeccMemberItem item=dataSet.get(listPosition);

            if(item.getDataSource()!=null && item.getDataSource().trim().equalsIgnoreCase(AppConstant.RSBY_SOURCE)){
                showRsbyMemberList(item,holder);
            }else{
                showSeccMemberList(item,holder);
            }

            holder.housholdStatusTV.setText("-");
            if(item.getHhStatus()!=null){
                for(FamilyStatusItem famStatItem : familyList) {
                    if(item.getHhStatus().equalsIgnoreCase(famStatItem.getStatusCode())) {
                        holder.housholdStatusTV.setText(famStatItem.getStatusDesc());
                        break;
                    }
                }
            }
            holder.memberStatusTV.setText("-");
            if(item.getMemStatus()!=null) {
                for(MemberStatusItem memStatItem : memberStatusList){
                    if(item.getMemStatus().equalsIgnoreCase(memStatItem.getStatusCode())){
                        holder.memberStatusTV.setText(memStatItem.getStatusDesc());
                        break;
                    }
                }
            }

            holder.syncStatusBT.setVisibility(View.GONE);
            holder.errorMsgLayout.setVisibility(View.GONE);

            holder.memberLayout.setBackgroundColor(AppUtility.getColor(context,R.color.white_shine));
            /*if(item.getNhpsRelationCode()!=null && item.getNhpsRelationCode().trim().equalsIgnoreCase(AppConstant.NEW_HEAD_RELATION_CODE)){
                holder.memberLayout.setBackgroundColor(AppUtility.getColor(context,R.color.green));
            }*/

            if(listPosition==0){
                holder.memberLayout.setBackgroundColor(AppUtility.getColor(context,R.color.Bg_bal_color));
            }
            holder.nhpsMemId.setText("-");
            holder.syncStatusBT.setVisibility(View.GONE);
            if (item.getSyncedStatus() != null ){
                if( item.getSyncedStatus().equalsIgnoreCase(AppConstant.SYNCED_STATUS_MEMBER)|| item.getSyncedStatus().equalsIgnoreCase("1") ) {
                holder.syncStatusBT.setVisibility(View.VISIBLE);
                holder.syncStatusBT.setText(context.getResources().getString(R.string.synced));
                if(item.getDataSource()!=null && item.getDataSource().trim().equalsIgnoreCase(AppConstant.RSBY_SOURCE)){

                }else {
                    holder.nhpsMemId.setText(item.getNhpsMemId());
                }
                holder.syncStatusBT.setBackgroundColor(AppUtility.getColor(context, R.color.sync_color));
                holder.syncStatusBT.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        syncPreview(item);
                    }
                });
            } }else
                if (item.getLockedSave() != null &&
                        item.getLockedSave().trim().equalsIgnoreCase(AppConstant.LOCKED + "")) {
                    if(item.getHhStatus()!=null && item.getHhStatus().trim().
                            equalsIgnoreCase(AppConstant.HOUSEHOLD_FOUND)) {
                    holder.syncStatusBT.setVisibility(View.VISIBLE);
                    if (item.getError_code() != null && !item.getError_code().equalsIgnoreCase("")) {
                        holder.errorMsgLayout.setVisibility(View.VISIBLE);
                        holder.syncTimeLayout.setVisibility(View.GONE);
                        holder.errorTV.setText(item.getError_msg());
                        if (item.getError_code() != null && !item.getError_code().trim().equalsIgnoreCase(AppConstant.INTERNET_ERROR_CODE)) {

                        }
                    }
                   if(item.getNhpsRelationCode() == null || item.getNhpsRelationCode().equalsIgnoreCase("")) {
                        holder.syncStatusBT.setVisibility(View.VISIBLE);
                        holder.syncStatusBT.setText(getString(R.string.locked));
                        holder.syncStatusBT.setBackgroundColor(AppUtility.getColor(context, R.color.locked_color));
                        holder.syncStatusBT.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //openReset(item,holder.syncStatusBT,context,getActivity());
                                AppUtility.openSyncPreview(item, context, getActivity());
                            }
                        });
                    } else {
                        holder.syncStatusBT.setVisibility(View.VISIBLE);
                        holder.syncStatusBT.setText(getString(R.string.locked));
                        holder.syncStatusBT.setBackgroundColor(AppUtility.getColor(context, R.color.locked_color));
                        holder.syncStatusBT.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // openReset(item,holder.syncStatusBT,context,getActivity());
                                //AppUtility.openSyncPreview(item,context,getActivity());
                                openReset(item, holder.syncStatusBT, context, getActivity());
                            }
                        });
                    }
                }else{
                        holder.syncStatusBT.setVisibility(View.VISIBLE);
                        holder.syncStatusBT.setText(getString(R.string.locked));
                        holder.syncStatusBT.setBackgroundColor(AppUtility.getColor(context, R.color.locked_color));

                        holder.syncStatusBT.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                openResetHousehold(item,holder.syncStatusBT,context,getActivity());

                            }
                        });
                    }
            }
            if(item.getSyncDt()!=null){

                String dateTime= DateTimeUtil.convertTimeMillisIntoStringDate(Long.parseLong(item.getSyncDt()),AppConstant.SYNC_DATE_TIME);
                if(dateTime!=null) {
                    holder.syncDateTV.setText(dateTime.trim());
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
    private  void syncPreview(SeccMemberItem item){
        SelectedMemberItem selectedMemItem=new SelectedMemberItem();
        selectedMemItem.setSeccMemberItem(item);
        ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,AppConstant.SELECTED_ITEM_FOR_VERIFICATION,
                selectedMemItem.serialize(),context);
        Intent theIntent=new Intent(context, MemberPreviewActivity.class);
        startActivity(theIntent);
    }
    public void openReset(final SeccMemberItem item1, Button button, final Context context, final Activity activity){
        PopupMenu popup = new PopupMenu(context, button);
        popup.getMenuInflater()
                .inflate(R.menu.menu_reset, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.reset:
                        alertForValidateLater(context.getResources().getString(R.string.alert_for_reset),item1,context,activity,AppConstant.RESET);
                        break;
                    case R.id.unlockRecord:
                        // askPinToLock(SeccMemberListActivity.EDIT,item1);
                        alertForValidateLater(context.getResources().getString(R.string.alert_for_unlock),item1,
                                context,activity,AppConstant.UNLOCK);
                        break;
                    case R.id.preview:
                        syncPreview(item1);
                        break;
                }
                return true;
            }
        });
        popup.show();
    }
    private  void alertForValidateLater(String msg, final SeccMemberItem item, final Context context, final Activity activity, final String action){

        final android.app.AlertDialog internetDiaolg = new android.app.AlertDialog.Builder(context).create();
        LayoutInflater factory = LayoutInflater.from(context);
        View alertView = factory.inflate(R.layout.internet_try_again_popup, null);
        internetDiaolg.setView(alertView);
        internetDiaolg.show();
        TextView tryGainMsgTV=(TextView) alertView.findViewById(R.id.deleteMsg);
        tryGainMsgTV.setText(msg);
        Button tryAgainBT=(Button) alertView.findViewById(R.id.tryAgainBT);
        tryAgainBT.setText(context.getResources().getString(R.string.Confirm_Submit));
        Button cancelBT=(Button)alertView.findViewById(R.id.cancelBT);
        tryAgainBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(action.equalsIgnoreCase(AppConstant.RESET)) {
                    askPinToLock(AppConstant.RESET, item, context, activity);
                    internetDiaolg.dismiss();
                }else if(action.equalsIgnoreCase(AppConstant.UNLOCK)){
                    SeccDatabase.editRecord(item,context);
                 /*   ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,AppConstant.DASHBOARD_TAB_STATUS,1+"",context);
                    ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                            AppConstant.HOUSEHOLD_TAB_STATUS,6+"",context);*/
                    if(item!=null && item.getDataSource() !=null && item.getDataSource().equalsIgnoreCase(AppConstant.RSBY_SOURCE)){
                        ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.DASHBOARD_TAB_STATUS,3+"",context);
                        ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                                AppConstant.HOUSEHOLD_TAB_STATUS,6+"",context);
                    }else{
                        ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.DASHBOARD_TAB_STATUS,1+"",context);
                        ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                                AppConstant.HOUSEHOLD_TAB_STATUS,6+"",context);
                    }
                    Intent theIntent = new Intent(context,SearchActivityWithHouseHold.class);
                    theIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(theIntent);
                    activity.finish();
                }
            }
        });
        cancelBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                internetDiaolg.dismiss();
            }
        });
    }
    public static  void askPinToLock(final String status, final SeccMemberItem item,final Context context, final Activity activity) {
        AppUtility.softKeyBoard(activity,1);
        final android.app.AlertDialog askForPinDailog = new android.app.AlertDialog.Builder(context).create();
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
                AppUtility.softKeyBoard(activity,0);
                String pin = pinET.getText().toString();
                if (pin.toString().equalsIgnoreCase(verifierDetail.getPin())) {
                    if(status.equalsIgnoreCase(AppConstant.RESET)){
                        SeccDatabase.resetData(item,context);
                        HouseHoldItem houseHoldItem=SeccDatabase.getHouseHoldList(item.getHhdNo(),context);
                        SelectedMemberItem selectedMemberItem=SelectedMemberItem.create(ProjectPrefrence.
                                getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                                        AppConstant.SELECTED_ITEM_FOR_VERIFICATION,context));
                        selectedMemberItem.setHouseHoldItem(houseHoldItem);
                        ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                                AppConstant.SELECTED_ITEM_FOR_VERIFICATION,selectedMemberItem.serialize(),context);
                        askForPinDailog.dismiss();

                        if(item!=null && item.getDataSource() !=null && item.getDataSource().equalsIgnoreCase(AppConstant.RSBY_SOURCE)){
                            ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.DASHBOARD_TAB_STATUS,3+"",context);
                            ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                                    AppConstant.HOUSEHOLD_TAB_STATUS,2+"",context);
                        }else{
                            ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.DASHBOARD_TAB_STATUS,1+"",context);
                            ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                                    AppConstant.HOUSEHOLD_TAB_STATUS,2+"",context);
                        }
                        Intent theIntent = new Intent(context,SearchActivityWithHouseHold.class);
                        theIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        activity.startActivity(theIntent);
                        activity.finish();
                    }
                } else if (pin.equalsIgnoreCase("")) {
                    // CustomAlert.alertWithOk(context,"Please enter valid pin");
                    errorTV.setVisibility(View.VISIBLE);
                    errorTV.setText(context.getResources().getString(R.string.plzEnterPin));
                    pinET.setText("");
                } else if (!pin.toString().equalsIgnoreCase(verifierDetail.getPin())) {
                    errorTV.setVisibility(View.VISIBLE);
                    errorTV.setText(context.getResources().getString(R.string.plzEnterValidPin));
                    pinET.setText("");
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
    public  void openResetHousehold(final SeccMemberItem item1, Button button, final Context context, final Activity activity){
        PopupMenu popup = new PopupMenu(context, button);
        popup.getMenuInflater()
                .inflate(R.menu.menu_reset_household,popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.reset:
                        alertForValidateLater(context.getResources().getString(R.string.alert_for_reset),item1,context,activity,AppConstant.RESET);
                        break;
                    case R.id.preview:
                        // openPreview(item1);
                        AppUtility.openSyncPreview(item1,context,activity);
                        break;
                }
                return true;
            }
        });
        popup.show();
    }

    private void showSeccMemberList(SeccMemberItem item, MemberErrorFragment.MemberAdapter.MyViewHolder holder){
        if(item.getName()!=null){
            holder.nameTV.setText(item.getName());
        }
        if(item.getNameSl()!=null){
            holder.localNameTV.setText(Html.fromHtml(item.getNameSl()));
        }
        if(item.getFathername()!=null){
            holder.fatherNameTV.setText(item.getFathername());
        }

        if(item.getAhlslnohhd()!=null){
            holder.houseHoldIDTV.setText(item.getAhlslnohhd());
        }

        String address="";
        if(item.getAddressline1()!=null){
            address=address+""+item.getAddressline1();
        }
        if(item.getAddressline2()!=null){
            address=address+","+item.getAddressline2();
        }
        if(item.getAddressline3()!=null){
            address=address+","+item.getAddressline3();
        }
        if(item.getAddressline4()!=null){
            address=address+","+item.getAddressline4();
        }
        holder.addressTV.setText(address);
        /*AppUtility.showLog(AppConstant.LOG_STATUS,"RSBY House hold","RSBY House hold Member"+item.serialize());

        if(newHeadItem!=null) {
            if(newHeadItem.getNhpsRelationCode()!=null && newHeadItem.getNhpsMemId().equalsIgnoreCase(item.getNhpsMemId())) {
                holder.relationTV.setText(item.getRelation());
                if(item.getNhpsRelationCode()!=null){
                    holder.newRelationTV.setText("Head");
                    holder.newRelationTV.setTextColor(AppUtility.getColor(context, R.color.yellow_dark));
                }
                holder.parentLayout.setBackgroundColor(AppUtility.getColor(context, R.color.Bg_bal_color));
            }else{
                AppUtility.showLog(AppConstant.LOG_STATUS,TAG," Old Head : "+oldHeadItem);
                if(item.getNhpsMemId().equalsIgnoreCase(oldHeadItem.getNhpsMemId())) {
                    holder.relationTV.setText(item.getRelation());
                    holder.newRelationTV.setText("Old Head");
                    holder.parentLayout.setBackgroundColor(AppUtility.getColor(context, R.color.gray_transparent));
                }
                holder.relationTV.setTextColor(AppUtility.getColor(context, R.color.black));
            }
        }
        if(item.getNhpsRelationCode()!=null){
            for(RelationItem item1 : relationList){
                if(item1.getRelationCode().equalsIgnoreCase(item.getNhpsRelationCode())){
                    holder.newRelationTV.setText(item1.getRelationName());
                    break;
                }
            }
        }*/
    }
    private void showRsbyMemberList(SeccMemberItem item, MemberErrorFragment.MemberAdapter.MyViewHolder holder){
        if(item.getRsbyName()!=null){
            holder.nameTV.setText(item.getRsbyName());
        }
        /*if(item.getNameSl()!=null){
            holder.localNameTV.setText(item.getNameSl());
        }*/
       /* if(item.getFathername()!=null){
            holder.fatherNameTV.setText(item.getFathername());
        }*/

        /*if(item.getAhlslnohhd()!=null){
            holder.houseHoldIDTV.setText(item.getAhlslnohhd());
        }

        String address="";
        if(item.getAddressline1()!=null){
            address=address+""+item.getAddressline1();
        }
        if(item.getAddressline2()!=null){
            address=address+","+item.getAddressline2();
        }
        if(item.getAddressline3()!=null){
            address=address+","+item.getAddressline3();
        }
        if(item.getAddressline4()!=null){
            address=address+","+item.getAddressline4();
        }
        holder.addressTV.setText(address);*/
        /*AppUtility.showLog(AppConstant.LOG_STATUS,"RSBY House hold","RSBY House hold Member"+item.serialize());

        if(newHeadItem!=null) {
            if(newHeadItem.getNhpsRelationCode()!=null && newHeadItem.getNhpsMemId().equalsIgnoreCase(item.getNhpsMemId())) {
                holder.relationTV.setText(item.getRelation());
                if(item.getNhpsRelationCode()!=null){
                    holder.newRelationTV.setText("Head");
                    holder.newRelationTV.setTextColor(AppUtility.getColor(context, R.color.yellow_dark));
                }
                holder.parentLayout.setBackgroundColor(AppUtility.getColor(context, R.color.Bg_bal_color));
            }else{
                AppUtility.showLog(AppConstant.LOG_STATUS,TAG," Old Head : "+oldHeadItem);
                if(item.getNhpsMemId().equalsIgnoreCase(oldHeadItem.getNhpsMemId())) {
                    holder.relationTV.setText(item.getRelation());
                    holder.newRelationTV.setText("Old Head");
                    holder.parentLayout.setBackgroundColor(AppUtility.getColor(context, R.color.gray_transparent));
                }
                holder.relationTV.setTextColor(AppUtility.getColor(context, R.color.black));
            }
        }
        if(item.getNhpsRelationCode()!=null){
            for(RelationItem item1 : relationList){
                if(item1.getRelationCode().equalsIgnoreCase(item.getNhpsRelationCode())){
                    holder.newRelationTV.setText(item1.getRelationName());
                    break;
                }
            }
        }*/
    }


}
