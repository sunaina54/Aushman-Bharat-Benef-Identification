package com.nhpm.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.customComponent.utility.DateTimeUtil;
import com.customComponent.utility.ProjectPrefrence;
import com.nhpm.LocalDataBase.dto.SeccDatabase;
import com.nhpm.Models.response.master.MemberStatusItem;
import com.nhpm.Models.response.seccMembers.HouseHoldItem;
import com.nhpm.Models.response.seccMembers.SeccMemberItem;
import com.nhpm.Models.response.seccMembers.SelectedMemberItem;
import com.nhpm.Models.response.verifier.FamilyStatusItem;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;
import com.nhpm.Utility.AppUtility;
import com.nhpm.activity.MemberPreviewActivity;
import com.nhpm.activity.SeccMemberListActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MemberStatusFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MemberStatusFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private List<SeccMemberItem> memberList;
    private RecyclerView errorMemberList;
    private Context context;
    private MemberAdapter memberAdapter;
    private String memberStatus;
    private  ArrayList<String> spinnerList;
    private Spinner sortedSP;
    private ArrayList<MemberStatusItem> memberStatusList;
    private ArrayList<FamilyStatusItem> householdStatusList;
    private String MEMBER_SORT_STATUS;
    private String TAG="Member Status Fragment";
    private int selectorIndex;
    private String SELECTED_MEMBER="selectedMember";

    public String getMemberStatus() {
        return memberStatus;
    }

    public void setMemberStatus(String memberStatus) {
        this.memberStatus = memberStatus;
    }

    public List<SeccMemberItem> getMemberList() {
        return memberList;
    }

    public void setMemberList(ArrayList<SeccMemberItem> memberList) {
        this.memberList = memberList;
    }
    public MemberStatusFragment() {
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
    public static MemberStatusFragment newInstance(String param1, String param2) {
        MemberStatusFragment fragment = new MemberStatusFragment();
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
        MEMBER_SORT_STATUS=ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,AppConstant.MEMBER_SORT_STATUS,context);
        memberStatusList=SeccDatabase.getMemberStatusList(context);
        householdStatusList=SeccDatabase.getFamilyStatusList(context);
      //  AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Member List Size : "+memberList.size());
        if(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,SELECTED_MEMBER,context)!=null){
            selectorIndex=Integer.parseInt(ProjectPrefrence.
                    getSharedPrefrenceData(AppConstant.PROJECT_PREF,SELECTED_MEMBER,context));
        }
        errorMemberList=(RecyclerView) view.findViewById(R.id.errorMemberList);
        sortedSP=(Spinner)view.findViewById(R.id.sortSP) ;
        errorMemberList.setHasFixedSize(true);
        LinearLayoutManager manager=new LinearLayoutManager(context);
        errorMemberList.setLayoutManager(manager);
        errorMemberList.setItemAnimator(new DefaultItemAnimator());
       /* if(memberList!=null && memberList.size()>0){
           defaultList();
        }*/
        manager.scrollToPosition(selectorIndex);
        prepareFamilyStatusSpinner();
        if(MEMBER_SORT_STATUS!=null){
            sortedSP.setSelection(Integer.parseInt(MEMBER_SORT_STATUS));
        }else{

        }
        sortedSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,AppConstant.MEMBER_SORT_STATUS,i+"",context);
                sortList(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    private class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.MyViewHolder> {
        View view;
        AlertDialog dialog;
        private List<SeccMemberItem> dataSet;
        private Context context;
        private TextView text;
        public class MyViewHolder extends RecyclerView.ViewHolder   {
            TextView nameTV,localNameTV,fatherNameTV,
                    houseHoldIDTV,errorTV,memberStatusTV,householdStatusTV,addressTV,syncDateTV,nhpsMemIdTV;
            LinearLayout errorMsgLayout,memberStatusLayout,syncTimeLayout,nhpsMemIdLayout,urnIdLayout,householdIdLayout;
            RelativeLayout memberLayout;
            Button printBT,memberStatusBT;

            public MyViewHolder(View itemView) {
                super(itemView);
                this.localNameTV = (TextView) itemView.findViewById(R.id.localNameTV);
                this.nameTV = (TextView) itemView.findViewById(R.id.memberNametTV);
                this.fatherNameTV = (TextView) itemView.findViewById(R.id.fatherNameTV);
                this.errorTV=(TextView) itemView.findViewById(R.id.errorTV);
                this.addressTV=(TextView)itemView.findViewById(R.id.addressTV) ;
                this.houseHoldIDTV=(TextView)itemView.findViewById(R.id.householdIdTV);
                this.errorMsgLayout=(LinearLayout)itemView.findViewById(R.id.errorMsgLayout);
                this.errorMsgLayout.setVisibility(View.GONE);
                this.memberStatusLayout=(LinearLayout)itemView.findViewById(R.id.memberStatusLayout);
                this.memberStatusTV=(TextView)itemView.findViewById(R.id.memberStatusTV);
                this.householdStatusTV=(TextView)itemView.findViewById(R.id.householdStatusTV);
                this.memberStatusBT=(Button)itemView.findViewById(R.id.memberStatusBT) ;
                this.memberLayout=(RelativeLayout)itemView.findViewById(R.id.memberRowLayout);
                syncDateTV=(TextView)itemView.findViewById(R.id.syncDateTV) ;
                nhpsMemIdTV=(TextView)itemView.findViewById(R.id.nhpsMemId);
                syncTimeLayout=(LinearLayout)itemView.findViewById(R.id.syncTimeLayout);
                nhpsMemIdLayout=(LinearLayout)itemView.findViewById(R.id.nhpsMemIdLayout);
               /* householdIdLayout=(LinearLayout)itemView.findViewById(R.id.householdIdLayout);
                urnIdLayout=(LinearLayout)itemView.findViewById(R.id.urnIdLayout);
                householdIdLayout.setVisibility(View.GONE);
                urnIdLayout.setVisibility(View.GONE);*/

                syncTimeLayout.setVisibility(View.GONE);
                nhpsMemIdLayout.setVisibility(View.GONE);
                if(memberStatus.equalsIgnoreCase(MemberStatusDadhboardFragment.SYNCED)){
                    syncTimeLayout.setVisibility(View.VISIBLE);
                }

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
            }
        }
        public void addAll(List<SeccMemberItem> list) {

            dataSet.addAll(list);
            notifyDataSetChanged();
        }
        public MemberAdapter(Context context, List<SeccMemberItem> data) {
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
            holder.memberLayout.setBackgroundColor(AppUtility.getColor(context,R.color.white_shine));
            if(item.getRelation()!=null && item.getRelation().equalsIgnoreCase("HEAD")){
                holder.memberLayout.setBackgroundColor(AppUtility.getColor(context,R.color.Bg_bal_color));

            }
            /*if(item.getNhpsMemId()!=null && item.getNhpsMemId().equalsIgnoreCase()){
                holder.memberLayout.setBackgroundColor(AppUtility.getColor(context,R.color.Bg_bal_color));

            }*/

            holder.memberStatusTV.setText("-");
            holder.householdStatusTV.setText("-");
            if(item.getMemStatus()!=null && !item.getMemStatus().equalsIgnoreCase("")){
                boolean flag=false;
                for(MemberStatusItem item1 : memberStatusList){
                    if(item1.getStatusCode().equalsIgnoreCase(item.getMemStatus())){
                        flag=true;
                        holder.memberStatusTV.setText(item1.getStatusDesc());
                        break;
                    }
                }
                /*if(flag){
                    holder.memberStatusLayout.setVisibility(View.VISIBLE);

                }*/
            }

            if(memberStatus.equalsIgnoreCase(MemberStatusDadhboardFragment.TOTAL_MEMBER)){
                holder.memberStatusBT.setVisibility(View.GONE);
            }else if(memberStatus.equalsIgnoreCase(MemberStatusDadhboardFragment.YET_TO_MEMBER)){
                holder.memberStatusBT.setBackgroundColor(AppUtility.getColor(context, R.color.yet_to_survey_color));
                holder.memberStatusBT.setText(context.getResources().getString(R.string.yet_to_survey));
                holder.memberStatusBT.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openMemberDetail(item);
                        ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,SELECTED_MEMBER,listPosition+"",context);
                    }
                });
            }else if(memberStatus.equalsIgnoreCase(MemberStatusDadhboardFragment.UNDER_SURVEYED)){
                holder.memberStatusBT.setText(context.getResources().getString(R.string.under_survey));
                holder.memberStatusBT.setBackgroundColor(AppUtility.getColor(context, R.color.under_survey_color));
                holder.memberStatusBT.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // openSyncPreview(item);
                        ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,SELECTED_MEMBER,listPosition+"",context);
                        openUnderSurved(item);
                    }
                });
            }else if(memberStatus.equalsIgnoreCase(MemberStatusDadhboardFragment.VALIDATED)){
                holder.memberStatusBT.setText(context.getResources().getString(R.string.validated));
                ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,SELECTED_MEMBER,listPosition+"",context);
                holder.memberStatusBT.setBackgroundColor(AppUtility.getColor(context, R.color.validated_color));
                holder.memberStatusBT.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // openSyncPreview(item);
                        openMemberDetail(item);
                    }
                });

            }else if(memberStatus.equalsIgnoreCase(MemberStatusDadhboardFragment.LOCKED)){
                if(item.getAadhaarAuth()!=null && item.getAadhaarAuth().equalsIgnoreCase(AppConstant.PENDING_STATUS)){
                    holder.memberStatusBT.setText(context.getResources().getString(R.string.underValidation));
                    ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,SELECTED_MEMBER,listPosition+"",context);
                    holder.memberStatusBT.setBackgroundColor(AppUtility.getColor(context, R.color.blue));
                    holder.memberStatusBT.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // openSyncPreview(item);
                            openMemberDetail(item);

                        }
                    });
                }else{
                    if(item.getHhStatus()!=null && !item.getHhStatus().trim().equalsIgnoreCase(AppConstant.HOUSEHOLD_FOUND)){
                        holder.memberStatusBT.setText(context.getResources().getString(R.string.locked));
                        ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, SELECTED_MEMBER, listPosition + "", context);
                        holder.memberStatusBT.setBackgroundColor(AppUtility.getColor(context, R.color.locked_color));
                        holder.memberStatusBT.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // openSyncPreview(item);
                                // openSyncPreview(item);
                                AppUtility.openResetHousehold(item, holder.memberStatusBT, context, getActivity());
                                //AppUtility.askPinToLock(AppConstant.RESET,item,context,getActivity());
                            }
                        });
                    }else {
                        holder.memberStatusBT.setText(context.getResources().getString(R.string.locked));
                        ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, SELECTED_MEMBER, listPosition + "", context);
                        holder.memberStatusBT.setBackgroundColor(AppUtility.getColor(context, R.color.locked_color));
                        holder.memberStatusBT.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // openSyncPreview(item);
                                // openSyncPreview(item);
                                AppUtility.openReset(item, holder.memberStatusBT, context, getActivity());
                                //AppUtility.askPinToLock(AppConstant.RESET,item,context,getActivity());
                            }
                        });
                    }
                }


            }else if(memberStatus.equalsIgnoreCase(MemberStatusDadhboardFragment.SYNCED)){
                holder.memberStatusBT.setText(context.getResources().getString(R.string.synced));
                ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,SELECTED_MEMBER,listPosition+"",context);
                holder.memberStatusBT.setBackgroundColor(AppUtility.getColor(context, R.color.sync_color));

                holder.memberStatusBT.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // openSyncPreview(item);
                        openSyncPreview(item);
                    }
                });
            }

            String address="";
            if(item.getAddressline1()!=null && !item.getAddressline1().trim().
                    equalsIgnoreCase("-") && !item.getAddressline1().equalsIgnoreCase("")){
                address=address+item.getAddressline1()+", ";
                // AppUtility.showLog(AppConstant.LOG_STATUS,TAG," Address3 : "+seccItem.getAddressline1());

            }
            if(item.getAddressline2()!=null && !item.getAddressline2().trim()
                    .equalsIgnoreCase("-") && !item.getAddressline2().equalsIgnoreCase("") ){
                //AppUtility.showLog(AppConstant.LOG_STATUS,TAG," Address : "+seccItem.getAddressline2());
                address=address+item.getAddressline2()+", ";
            }
            if(item.getAddressline3()!=null && !item.getAddressline3().trim().
                    equalsIgnoreCase("-") && !item.getAddressline3().equalsIgnoreCase("") ){
                //AppUtility.showLog(AppConstant.LOG_STATUS,TAG," Address1 : "+seccItem.getAddressline3());

                address=address+item.getAddressline3()+", ";
            }
            if(item.getAddressline4()!=null && !item.getAddressline4().trim().
                    equalsIgnoreCase("-") && !item.getAddressline4().equalsIgnoreCase("") ){
                // AppUtility.showLog(AppConstant.LOG_STATUS,TAG," Address2 : "+seccItem.getAddressline4());

                address=address+item.getAddressline4();
            }


            holder.addressTV.setText(address);


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
            if(item.getErrorItem()!=null){
                holder.errorMsgLayout.setVisibility(View.VISIBLE);
                holder.errorTV.setText(item.getErrorItem().getErrorMsg());
            }
            if(item.getHhStatus()!=null){
                for(FamilyStatusItem familyStatusItem : householdStatusList){
                    if(item.getHhStatus().equalsIgnoreCase(familyStatusItem.getStatusCode())){
                        holder.householdStatusTV.setText(familyStatusItem.getStatusDesc());
                        break;
                    }
                }
            }
            if(item.getSyncDt()!=null){
                String dateTime= DateTimeUtil.convertTimeMillisIntoStringDate(Long.parseLong(item.getSyncDt()),AppConstant.SYNC_DATE_TIME);
                if(dateTime!=null) {
                    holder.syncDateTV.setText(dateTime.trim());
                }
            }
            holder.errorMsgLayout.setVisibility(View.GONE);
            if(item.getError_code()!=null && !item.getError_code().equalsIgnoreCase("")){
                holder.errorMsgLayout.setVisibility(View.VISIBLE);
                holder.errorTV.setText(item.getError_msg());
            }
            holder.nhpsMemIdLayout.setVisibility(View.GONE);
            if(item.getSyncedStatus()!=null && !item.getSyncedStatus().equalsIgnoreCase("")){
                holder.nhpsMemIdLayout.setVisibility(View.VISIBLE);
                holder.nhpsMemIdTV.setText(item.getNhpsMemId());
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
    private HouseHoldItem findHousehold(SeccMemberItem item){
        HouseHoldItem item1= SeccDatabase.getHouseHoldDetailsByHhdNo(item.getHhdNo(),context);
        return item1;
    }
    private void openMemberDetail(SeccMemberItem item){
        SelectedMemberItem selectedMemberItem=new SelectedMemberItem();
        HouseHoldItem houseHoldItem=findHousehold(item);
        selectedMemberItem.setHouseHoldItem(houseHoldItem);
        ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,AppConstant.SELECTED_ITEM_FOR_VERIFICATION,selectedMemberItem.serialize(),context);
       // Intent theIntent=new Intent(context, SECCFamilyListActivity.class);
        Intent theIntent=new Intent(context, SeccMemberListActivity.class);

        startActivity(theIntent);
        getActivity().finish();
    }
    private void prepareFamilyStatusSpinner(){

        spinnerList=new ArrayList<>();
        spinnerList.add(context.getResources().getString(R.string.sortByName));
        spinnerList.add(context.getResources().getString(R.string.sortByHousehold));
        ArrayAdapter<String> maritalAdapter = new ArrayAdapter<>(context, R.layout.custom_drop_down, R.id.textView,spinnerList);
        sortedSP.setAdapter(maritalAdapter);

    }
    private void sortList(int index){
        switch(index){
            case 0:
          /*      Collections.sort(memberList, new Comparator<SeccMemberItem>() {
                    public int compare(SeccMemberItem v1, SeccMemberItem v2) {

                        return v1.getName().compareTo(v2.getName());
                    }
                });

              //  Log.d(TAG, "Secc Member list : " + seccMemberList.size());
                TreeSet<SeccMemberItem> seccTreeSet= new TreeSet<SeccMemberItem>(new Comparator<SeccMemberItem>(){

                    public int compare(SeccMemberItem o1, SeccMemberItem o2) {
                        // return 0 if objects are equal in terms of your properties
                        if (o1.getNhpsMemId().equalsIgnoreCase(o2.getNhpsMemId())) {
                            return 0;
                        }
                        return 1;
                    }
                });
                seccTreeSet.addAll(memberList);
                memberList=new ArrayList<>();
                memberList.addAll(seccTreeSet);*/
               // Log.d(TAG, "Secc Member list11111111 : " + seccMemberList.size());
                break;
            case 1:
               /* Log.d("Pending household"," Sort by House No ");
                Collections.sort(memberList, new Comparator<SeccMemberItem>() {
                    @Override
                    public int compare(SeccMemberItem o1, SeccMemberItem o2) {
                        return Integer.parseInt(o1.getAhlslnohhd())-Integer.parseInt(o2.getAhlslnohhd());
                      *//*  if(status)
                        return 1;
                        else
                            return 0;*//*

                        //return Integer.compare(o1.getAge(), o2.getId());
                    }
                });
                AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Member List Size 111111111 : "+memberList.size());
*/
                // memberList=arrangeListWithHeadTop();

             /* //  Log.d(TAG, "Secc Member list : " + seccMemberList.size());
                TreeSet<SeccMemberItem> seccTreeSet1= new TreeSet<SeccMemberItem>(new Comparator<SeccMemberItem>(){

                    public int compare(SeccMemberItem o1, SeccMemberItem o2) {
                        // return 0 if objects are equal in terms of your properties
                        if (o1.getAhlTin().equalsIgnoreCase(o2.getAhlTin())) {
                            return 0;
                        }
                        return 1;
                    }
                });

                seccTreeSet1.addAll(memberList);
                memberList=new ArrayList<>();

                memberList.addAll(seccTreeSet1);*/
               // Log.d(TAG, "Secc Member list11111111 : " + seccMemberList.size());

               // memberList =makeGroup(memberList);

                break;
        }
        defaultList();
    }
    private ArrayList<SeccMemberItem> arrangeListWithHeadTop(){
        ArrayList<SeccMemberItem> arrangedList=new ArrayList<>();
        ArrayList<SeccMemberItem> tempList;
      //  ArrayList<HouseHoldItem> houseHoldList=SeccDatabase.getAllHouseHold(context);
        for(SeccMemberItem item : memberList){

            tempList=new ArrayList<>();
            for(SeccMemberItem item1 : memberList){
                if(item.getNhpsMemId().equalsIgnoreCase(item1.getNhpsMemId())){
                    AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Head : "+item1.getRelation()+" HHd Number : "+item1.getHhdNo()+" Member Name :"+ item1.getName());
                    tempList.add(0,item1);
                   /* if(item1.getRelation().equalsIgnoreCase("HEAD")){

                    }else{

                    }*/
                }else{
                    AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Head : "+item1.getRelation()+" HHd Number : "+item1.getHhdNo()+" Member Name :"+ item1.getName());
                    tempList.add(item1);
                }

            }
            arrangedList.addAll(tempList);
            tempList.clear();
        }
        return arrangedList;
    }
    private List<SeccMemberItem> makeGroup(List<SeccMemberItem> list){
        Map<String, List<SeccMemberItem>> map = new HashMap<String, List<SeccMemberItem>>();
        List<SeccMemberItem> list1=new ArrayList<>();
        for (SeccMemberItem item : list) {
            String key  = item.getHhdNo();
            if(map.containsKey(key)) {
                list1 = map.get(key);
                if (item.getRelation().equalsIgnoreCase("HEAD")){
                    list1.add(0,item);
                }else{
                    list1.add(item);
                }

            }else{
                list1 = new ArrayList<SeccMemberItem>();
                if (item.getRelation().equalsIgnoreCase("HEAD")){
                    list1.add(0,item);
                }else{
                    list1.add(item);
                }
                map.put(key, list1);
            }
        }
      /*  if(map != null){
            return new ArrayList<SeccMemberItem>((Collection<? extends SeccMemberItem>) map.values());
        }*/
        List list2=new ArrayList<>();
        //list2.addAll(map.entrySet());
        return list2;
    }
    private void defaultList(){

        //AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Member List Size : "+memberList.size());
        if(memberList!=null && memberList.size()>0) {
            memberAdapter = new MemberAdapter(context, memberList);
            errorMemberList.setAdapter(memberAdapter);
           /* boolean allLocked = true;
            for(SeccMemberItem item : memberList){
                if(item.getLockedSave()!=null && item.getLockedSave().equalsIgnoreCase(String.valueOf(AppConstant.LOCKED))){
                    if(item.getSyncedStatus()!=null && !item.getSyncedStatus().equalsIgnoreCase(AppConstant.SYNCED_STATUS)){
                        allLocked = true;
                    }else{
                        allLocked = false;
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
*/
        }
    }
    private void openSyncPreview(SeccMemberItem item){
        SelectedMemberItem selectedMemberItem=new SelectedMemberItem();
        HouseHoldItem houseHoldItem=findHousehold(item);
        selectedMemberItem.setHouseHoldItem(houseHoldItem);
        selectedMemberItem.setSeccMemberItem(item);
        ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,AppConstant.SELECTED_ITEM_FOR_VERIFICATION,selectedMemberItem.serialize(),context);
        Intent theIntent=new Intent(context, MemberPreviewActivity.class);
        startActivity(theIntent);
    }
    private void openUnderSurved(SeccMemberItem item){
        SelectedMemberItem selectedMemberItem=new SelectedMemberItem();
        HouseHoldItem houseHoldItem=findHousehold(item);
        selectedMemberItem.setHouseHoldItem(houseHoldItem);
        selectedMemberItem.setSeccMemberItem(item);
        ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,AppConstant.SELECTED_ITEM_FOR_VERIFICATION,selectedMemberItem.serialize(),context);
        Intent theIntent=new Intent(context, SeccMemberListActivity.class);
        startActivity(theIntent);
        getActivity().finish();

    }
}
