package com.nhpm.rsbyFieldValidation.fragment;


import android.app.AlertDialog;
import android.content.Context;
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
import com.nhpm.Models.response.master.MemberStatusItem;
import com.nhpm.Models.response.rsbyMembers.RSBYItem;
import com.nhpm.Models.response.rsbyMembers.RsbyHouseholdItem;
import com.nhpm.Models.response.seccMembers.SelectedMemberItem;
import com.nhpm.Models.response.verifier.FamilyStatusItem;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;
import com.nhpm.Utility.AppUtility;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RsbyMemberErrorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RsbyMemberErrorFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ArrayList<RSBYItem> memberList;
    private RecyclerView errorMemberList;
    private Context context;
    private MemberAdapter memberAdapter;
    private ArrayList <FamilyStatusItem> familyList;
    private ArrayList<MemberStatusItem> memberStatusList;
    private LinearLayout sortLayout;
    private SelectedMemberItem selectedMemberItem;
    private RsbyHouseholdItem houseHoldItem;
    public ArrayList<RSBYItem> getMemberList() {
        return memberList;
    }

    public void setMemberList(ArrayList<RSBYItem> memberList) {
        this.memberList = memberList;
    }


    public RsbyMemberErrorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RsbyMemberErrorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RsbyMemberErrorFragment newInstance(String param1, String param2) {
        RsbyMemberErrorFragment fragment = new RsbyMemberErrorFragment();
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
        View view=inflater.inflate(R.layout.fragment_rsby_member_error, container, false);
        setupScreen(view);
        return view;
    }
    private void setupScreen(View view){
        context=getActivity();
        selectedMemberItem= SelectedMemberItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.SELECTED_ITEM_FOR_VERIFICATION,context));
        houseHoldItem=selectedMemberItem.getRsbyHouseholdItem();
        sortListByHead();
        sortLayout=(LinearLayout)view.findViewById(R.id.sortLayout) ;
        sortLayout.setVisibility(View.GONE);
        familyList= SeccDatabase.getFamilyStatusList(context);
        memberStatusList= SeccDatabase.getMemberStatusList(context);
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

    private class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.MyViewHolder> {

        View view;
        AlertDialog dialog;
        private ArrayList<RSBYItem> dataSet;
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
        public void addAll(List<RSBYItem> list) {

            dataSet.addAll(list);
            notifyDataSetChanged();
        }
        public MemberAdapter(Context context, ArrayList<RSBYItem> data) {
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

            final RSBYItem item=dataSet.get(listPosition);
            if(item.getName()!=null){
                holder.nameTV.setText(item.getName());
            }
            if(item.getUrnId()!=null){
                holder.houseHoldIDTV.setText(item.getUrnId());
            }
           /* if(item.getNameSl()!=null){
                holder.localNameTV.setText(item.getNameSl());
            }
            if(item.getFathername()!=null){
                holder.fatherNameTV.setText(item.getFathername());
            }

            if(item.getAhlslnohhd()!=null){
                holder.houseHoldIDTV.setText(item.getAhlslnohhd());
            }*/

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
            String address="";
           /* if(item.getAddressline1()!=null){
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
            }*/
          //  holder.addressTV.setText(address);
            holder.syncStatusBT.setVisibility(View.GONE);
            holder.errorMsgLayout.setVisibility(View.GONE);

            holder.memberLayout.setBackgroundColor(AppUtility.getColor(context, R.color.white_shine));
            /*if(item.getNhpsRelationCode()!=null && item.getNhpsRelationCode().trim().equalsIgnoreCase(AppConstant.NEW_HEAD_RELATION_CODE)){
                holder.memberLayout.setBackgroundColor(AppUtility.getColor(context,R.color.green));
            }*/

            if(listPosition==0){
                holder.memberLayout.setBackgroundColor(AppUtility.getColor(context, R.color.Bg_bal_color));
            }
            holder.nhpsMemId.setText("-");
            holder.syncStatusBT.setVisibility(View.GONE);
            if (item.getSyncedStatus() != null && item.getSyncedStatus().equalsIgnoreCase(AppConstant.SYNCED_STATUS)) {
                holder.syncStatusBT.setVisibility(View.VISIBLE);
                holder.syncStatusBT.setText("Synced");
                holder.nhpsMemId.setText(item.getNhpsMemId());
                holder.syncStatusBT.setBackgroundColor(AppUtility.getColor(context, R.color.sync_color));
                holder.syncStatusBT.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                      //  syncPreview(item);
                    }
                });
            } else
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
                                // openReset(item,holder.syncStatusBT,context,getActivity());
                              //  AppUtility.openSyncPreview(item, context, getActivity());
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
                               // openReset(item, holder.syncStatusBT, context, getActivity());
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
                          //  openResetHousehold(item,holder.syncStatusBT,context,getActivity());

                        }
                    });
                }
            }
            if(item.getSyncDt()!=null){
                try {
                    String dateTime = DateTimeUtil.convertTimeMillisIntoStringDate(Long.parseLong(item.getSyncDt()), AppConstant.SYNC_DATE_TIME);
                    if (dateTime != null) {
                        holder.syncDateTV.setText(dateTime.trim());
                    }
                }catch (Exception ex){
                    holder.syncDateTV.setText(item.getSyncDt());
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

    private void sortListByHead(){
        ArrayList<RSBYItem> list=new ArrayList<>();
        RSBYItem head=null;
        if(houseHoldItem!=null && houseHoldItem.getHhStatus()!=null && houseHoldItem.getHhStatus().trim().equalsIgnoreCase(AppConstant.HOUSEHOLD_FOUND)){

           try {
               for (RSBYItem item : memberList) {
                   AppUtility.showLog(AppConstant.LOG_STATUS, "MemberErrorFragment",
                           item.getNhpsRelationCode() + " Name :" + item.getName());
                   if (item.getNhpsRelationCode() != null && item.getNhpsRelationCode().trim().equalsIgnoreCase(AppConstant.NEW_HEAD_RELATION_CODE)) {
                       head = item;
                   } else {
                       list.add(item);
                   }
               }
           }catch (Exception ex){

           }
            list.add(0,head);
        }else {
            for (RSBYItem item : memberList) {
                if (houseHoldItem != null && item.getRsbyMemId().trim().equalsIgnoreCase(houseHoldItem.getRsbyMemId().trim())) {
                    head = item;
                } else {
                    list.add(item);
                }
            }
            list.add(0, head);
        }
        memberList=list;
    }


}
