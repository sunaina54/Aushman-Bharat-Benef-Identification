package com.nhpm.rsbyFieldValidation.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.customComponent.CustomAlert;
import com.customComponent.utility.DateTimeUtil;
import com.customComponent.utility.ProjectPrefrence;
import com.nhpm.LocalDataBase.dto.SeccDatabase;
import com.nhpm.Models.response.rsbyMembers.RsbyHouseholdItem;
import com.nhpm.Models.response.seccMembers.SelectedMemberItem;
import com.nhpm.Models.response.verifier.FamilyStatusItem;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;
import com.nhpm.backgroundService.RsbySyncService;
import com.nhpm.fragments.SyncHouseHoldFragment;
import com.nhpm.rsbyFieldValidation.RsbySyncErrorActivity;
import com.nhpm.rsbyFieldValidation.RsbySyncHouseholdActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RsbySyncStatusFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RsbySyncStatusFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ArrayList<RsbyHouseholdItem> pendingHouseholdList;
    private String status;
    private RecyclerView syncList;
    private String TAG = "Sync Status Fragment";
    private Context context;
    private OtherMemberAdapter adapter;
    private ArrayList<FamilyStatusItem> familyStatusList;
    private Button syncAllBT;
    private RsbySyncHouseholdActivity activity;
    private int readyToSyncCount;
    private int unsyncCount;
    private int syncCount;
    private String HOUSEHOLD_SORT_STATUS;
    private Spinner sortedSP;
    private ArrayList<String> spinnerList;
    public static int ErrorMemberRequest = 2;

    public RsbySyncStatusFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RsbySyncStatusFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RsbySyncStatusFragment newInstance(String param1, String param2) {
        RsbySyncStatusFragment fragment = new RsbySyncStatusFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ArrayList<RsbyHouseholdItem> getPendingHouseholdList() {
        return pendingHouseholdList;
    }

    public void setPendingHouseholdList(ArrayList<RsbyHouseholdItem> pendingHouseholdList) {
        this.pendingHouseholdList = pendingHouseholdList;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
        View v = inflater.inflate(R.layout.fragment_rsby_sync_status, container, false);
        setupScreen(v);
        return v;
    }


        private void setupScreen(View view){
            context=getActivity();
            familyStatusList= SeccDatabase.getFamilyStatusList(context);
            syncList=(RecyclerView)view.findViewById(R.id.syncMemberList);
            syncAllBT=(Button)view.findViewById(R.id.syncAllBT);
            syncAllBT.setVisibility(View.GONE);
            sortedSP=(Spinner)view.findViewById(R.id.sortSP) ;
            syncList.setHasFixedSize(true);
            LinearLayoutManager layoutManager = new LinearLayoutManager(context);
            syncList.setLayoutManager(layoutManager);
            syncList.setItemAnimator(new DefaultItemAnimator());
      /*  HOUSEHOLD_SORT_STATUS=ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,AppConstant.HOUSEHOLD_SORT_STATUS,context);
        if(HOUSEHOLD_SORT_STATUS!=null){
            sortedSP.setSelection(Integer.parseInt(HOUSEHOLD_SORT_STATUS));
        }else{

        }*/
            sortedSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    // ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,AppConstant.HOUSEHOLD_SORT_STATUS,i+"",context);
                    if(pendingHouseholdList!=null) {
                        sortList(i);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            prepareFamilyStatusSpinner();
            if( status!=null && status.equalsIgnoreCase(SyncRsbyHouseholdDashoardfragment.READY_TO_SYNC)){
                // activity.updateSyncStatus(123);
                readyToSyncCount=pendingHouseholdList.size();
                if(pendingHouseholdList.size()>0) {
                    syncAllBT.setVisibility(View.VISIBLE);
                    if(pendingHouseholdList!=null && pendingHouseholdList.size()>0) {
                        // startSyncServce(context, pendingHouseholdList);
                    }
                }else {
                    syncAllBT.setVisibility(View.GONE);
                }
            }else  if(status!=null && status.equalsIgnoreCase(SyncRsbyHouseholdDashoardfragment.SYNC_ERROR)){
                if(pendingHouseholdList.size()>0) {
                    syncAllBT.setText("Resync");
                    syncAllBT.setVisibility(View.VISIBLE);
                }else {
                    syncAllBT.setVisibility(View.GONE);
                }
            }else{
                // syncAllBT.setVisibility(View.VISIBLE);

            }

            syncAllBT.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(activity.isNetworkAvailable()) {
                        //  syncHouseholdToServer();
                        if(pendingHouseholdList!=null && pendingHouseholdList.size()>0) {
                            startSyncServce(context, pendingHouseholdList);
                        }
                    }else{
                        CustomAlert.alertWithOk(context,getResources().getString(R.string.internet_connection_msg));
                    }
                }
            });

      /*  adapter=new OtherMemberAdapter(context,pendingHouseholdList);
        syncList.setAdapter(adapter);
        adapter.notifyDataSetChanged();*/
        }

    private void startSyncServce(Context context,ArrayList<RsbyHouseholdItem> list){
        activity.showProgressDialog();
        Intent intent = new Intent(context, RsbySyncService.class);
        intent.putExtra(RsbySyncService.SYNC_ARG,list);
        getActivity().startService(intent);
        getActivity().registerReceiver(activity.broadcastReceiver, new IntentFilter(RsbySyncService.BROADCAST_ACTION));

    }

    private void prepareFamilyStatusSpinner(){

        spinnerList=new ArrayList<>();
        spinnerList.add("Sort by HouseholdID#");
        spinnerList.add("Sort by Name");
        ArrayAdapter<String> maritalAdapter = new ArrayAdapter<>(context, R.layout.custom_drop_down, R.id.textView,spinnerList);
        sortedSP.setAdapter(maritalAdapter);
    }
    private void sortList(int index) {
        switch (index) {
            case 0:
                //  Log.d("Pending household", " Sort by House No ");
               /* Collections.sort(pendingHouseholdList, new Comparator<RsbyHouseholdItem>() {
                    @Override
                    public int compare(RsbyHouseholdItem o1, RsbyHouseholdItem o2) {
                        return Integer.parseInt(o1.ge) - Integer.parseInt(o2.getAhlslnohhd());
                      *//*  if(status)
                        return 1;
                        else
                            return 0;*//*

                        //return Integer.compare(o1.getAge(), o2.getId());
                    }
                });*/
                break;
            case 1:
                Collections.sort(pendingHouseholdList, new Comparator<RsbyHouseholdItem>() {
                    @Override
                    public int compare(RsbyHouseholdItem o1, RsbyHouseholdItem o2) {
                        return o1.getName().compareTo(o2.getName());
                    }
                });

                break;
        }
        adapter=new OtherMemberAdapter(context,pendingHouseholdList);
        syncList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
    class OtherMemberAdapter extends RecyclerView.Adapter<OtherMemberAdapter.MyViewHolder> {
        private ArrayList<RsbyHouseholdItem> dataSet;
        // Context context;
        private TextView text;
        //private Activity context;
        public class MyViewHolder extends RecyclerView.ViewHolder   {
            TextView hofNameTV,noOfFamilyTV,fatherNameTV,houseHoldNo,addressTV,
                    genderTV,verifiedMemTV,localHofNameTV,householdStatusTV,syncDateTV;;
            TextView errorTV,nhpsIdTV;
            LinearLayout syncTimeLayout,errorLayout;
            public MyViewHolder(View itemView) {
                super(itemView);
                verifiedMemTV=(TextView) itemView.findViewById(R.id.verifiedMemTV);
                noOfFamilyTV = (TextView) itemView.findViewById(R.id.totalMemberTV);
                hofNameTV = (TextView) itemView.findViewById(R.id.hofNameTV);
                localHofNameTV=(TextView)itemView.findViewById(R.id.localHofNameTV);
                houseHoldNo=(TextView)itemView.findViewById(R.id.houseNoTV);
                fatherNameTV=(TextView)itemView.findViewById(R.id.hhdFatherNameTV);
                addressTV=(TextView)itemView.findViewById(R.id.hhdAddressTV);
                genderTV=(TextView)itemView.findViewById(R.id.hhdGenderTV);
                errorTV=(TextView)itemView.findViewById(R.id.errorTV);
                householdStatusTV=(TextView)itemView.findViewById(R.id.householdStatusTV);
                syncDateTV=(TextView)itemView.findViewById(R.id.syncDateTV) ;
                nhpsIdTV=(TextView)itemView.findViewById(R.id.nhpsIdTV);
                syncTimeLayout=(LinearLayout)itemView.findViewById(R.id.syncTimeLayout);
                errorLayout=(LinearLayout)itemView.findViewById(R.id.errorLayout) ;
                syncTimeLayout.setVisibility(View.GONE);
                errorLayout.setVisibility(View.GONE);
                if(status!=null && status.equalsIgnoreCase(SyncHouseHoldFragment.SYNCED_HOUSEHOLD)) {
                    syncTimeLayout.setVisibility(View.VISIBLE);
                }

                if(status!=null && status.equalsIgnoreCase(SyncHouseHoldFragment.SYNC_ERROR)){
                    errorLayout.setVisibility(View.VISIBLE);
                }

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent theIntent=new Intent(context,RsbySyncErrorActivity.class);
                        SelectedMemberItem selectedMemberItem=new SelectedMemberItem();
                        selectedMemberItem.setRsbyHouseholdItem(dataSet.get(getPosition()));
                        //  Log.d(TAG," Selected Household : "+selectedMemberItem.getHouseHoldItem().getHhdNo());
                        ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                                AppConstant.SELECTED_ITEM_FOR_VERIFICATION,selectedMemberItem.serialize(), context);
                        startActivityForResult(theIntent,ErrorMemberRequest);
                    }
                });
            }
        }
        public void addAll(List<RsbyHouseholdItem> list) {

            dataSet.addAll(list);
            notifyDataSetChanged();
        }
        public OtherMemberAdapter(Context context, ArrayList<RsbyHouseholdItem> data) {

            this.dataSet = data;;
            this.text=text;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_pending_family_list_item, parent, false);

            //view.setOnClickListener(MainActivity.myOnClickListener);

            MyViewHolder myViewHolder = new MyViewHolder(view);
            return myViewHolder;
        }


        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {


            RsbyHouseholdItem item=dataSet.get(listPosition);

            holder.noOfFamilyTV.setText("2");
            holder.verifiedMemTV.setText("0");

        if(item.getUrnId()!=null)
            holder.houseHoldNo.setText(item.getUrnId());
        if(item.getName()!=null)
            holder.hofNameTV.setText(item.getName());
       /* if(item.getFathername()!=null){
            holder.fatherNameTV.setText(item.getFathername());
        }*/
       /* if(item.getNameSl()!=null)
            holder.localHofNameTV.setText(item.getNameSl());*/
        if(item.getGender().equalsIgnoreCase("1")) {
            holder.genderTV.setText("Male");
        }else{
            holder.genderTV.setText("Female");
        }

            holder.householdStatusTV.setText("-");
            if(item.getHhStatus()!=null && !item.getHhStatus().equalsIgnoreCase("")) {
                for (FamilyStatusItem item1 : familyStatusList) {
                    if(item1.getStatusCode().equalsIgnoreCase(item.getHhStatus())){
                        holder.householdStatusTV.setText(item1.getStatusDesc());
                        break;
                    }
                }
            }
            String address="";

       /* if(item.getAddressline1()!=null && !item.getAddressline1().trim().
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
        }*/
           /* if(item.getAddressline1()!=null && !item.getAddressline1().equalsIgnoreCase("")){
                address=item.getAddressline1();
            }
            if(item.getAddressline2()!=null && !item.getAddressline2().equalsIgnoreCase("")){
                address=address+","+item.getAddressline2();
            }
            if(item.getAddressline3()!=null && !item.getAddressline3().equalsIgnoreCase("")){
                address=address+","+item.getAddressline3();
            }
            if(item.getAddressline4()!=null && !item.getAddressline4().equalsIgnoreCase("")){
                address=address+","+item.getAddressline4();
            }*/
            // AppUtility.showLog(AppConstant.LOG_STATUS,"Houshold Status ","Address : "+address);
            holder.addressTV.setText(address);
            if(item.getSyncDt()!=null){
                try {
                   /* String[] array = item.getSyncDt().split(".");
*/                    String dateTime = DateTimeUtil.convertTimeMillisIntoStringDate(Long.parseLong(item.getSyncDt()), AppConstant.SYNC_DATE_TIME);
                    if (dateTime != null) {
                        holder.syncDateTV.setText(dateTime);
                    }
                }catch (Exception ex){
                    holder.syncDateTV.setText(item.getSyncDt());
                }
            }
            if(item.getSyncDt()!=null){
                try {
                    String dateTime = DateTimeUtil.convertTimeMillisIntoStringDate(Long.parseLong(item.getSyncDt()), AppConstant.SYNC_DATE_TIME);
                    if (dateTime != null) {
                        holder.syncDateTV.setText(dateTime);
                    }
                }catch(Exception ex){
                   holder.syncDateTV.setText(item.getSyncDt());
                }
            }
            holder.nhpsIdTV.setText("-");
            if(item.getSyncedStatus()!=null && item.getSyncedStatus().trim().equalsIgnoreCase(AppConstant.SYNCED_STATUS)){
                holder.nhpsIdTV.setText(item.getNhpsId());
            }
            if(item.getError_code()!=null && !item.getError_code().equalsIgnoreCase("")){
                holder.errorTV.setText(item.getError_msg());
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

    @Override
    public void onAttach(Activity mactivity) {
        activity = (RsbySyncHouseholdActivity) mactivity;
        super.onAttach(mactivity);
    }
}


