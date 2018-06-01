package com.nhpm.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.util.SparseBooleanArray;
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
import com.customComponent.utility.DateTimeUtil;
import com.customComponent.utility.ProjectPrefrence;
import com.nhpm.LocalDataBase.dto.SeccDatabase;
import com.nhpm.Models.response.rsbyMembers.RsbyHouseholdItem;
import com.nhpm.Models.response.seccMembers.HouseHoldItem;
import com.nhpm.Models.response.seccMembers.SelectedMemberItem;
import com.nhpm.Models.response.verifier.FamilyStatusItem;
import com.nhpm.Models.response.verifier.VerifierLocationItem;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;
import com.nhpm.Utility.AppUtility;
import com.nhpm.activity.SearchActivityWithHouseHold;
import com.nhpm.activity.SeccMemberListActivity;
import com.nhpm.activity.SyncHouseholdActivity;
import com.nhpm.rsbyFieldValidation.RsbyMainActivity;
import com.nhpm.rsbyFieldValidation.fragment.RsbyHouseholdStatusBoardFragment;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Saurabh on 04-03-2017.
 */

public class RsbyHouseholdStatusFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private RecyclerView houseHoldRecycleList;
    private Context context;
    private ArrayList<HouseHoldItem> totalHouseHold;
    //  private SeccHouseholdResponse householdResponse;
    private OtherMemberAdapter adapter;
    private RelativeLayout notFindMemberLayout, searchClearLayout;
    private EditText searchHouseholdET;
    private SearchActivityWithHouseHold activity;
    private VerifierLocationItem locationItem;
    private ArrayList<HouseHoldItem> houseHoldList;
    private SelectedMemberItem selectedMemberItem;
    private Spinner sortedSP;
    private ArrayList<String> spinnerList;
    private ArrayList<HouseHoldItem> pendingHouseHoldList;
    private String status;
    private ArrayList<FamilyStatusItem> householdStatusList;
    private String HOUSEHOLD_SORT_STATUS;
    private int selectedPosition;
    private String SELECTED_POSTION = "selectedPosition";
    private TextView noDataErrorTV;
    private Button syncButton;


    public ArrayList<HouseHoldItem> getPendingHouseHoldList() {
        return pendingHouseHoldList;
    }

    public void setPendingHouseHoldList(ArrayList<HouseHoldItem> pendingHouseHoldList) {
        this.pendingHouseHoldList = pendingHouseHoldList;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public RsbyHouseholdStatusFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HouseholdStatusFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RsbyHouseholdStatusFragment newInstance(String param1, String param2) {
        RsbyHouseholdStatusFragment fragment = new RsbyHouseholdStatusFragment();
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
        View view = inflater.inflate(R.layout.fragment_pending_house_hold, container, false);
        setupScreen(view);
        return view;
    }

    /* private void getSeccHouseholdList(){
         houseHoldList= SeccDatabase.getHouseHoldList(locationItem.getStateCode(), locationItem.getDistrictCode(),
                 locationItem.getTehsilCode(), locationItem.getVtCode(), locationItem.getWardCode(), locationItem.getBlockCode(), context);
         pendingHouseHoldList=new ArrayList<>();
         for(HouseHoldItem item : houseHoldList){
             if(item.getHhStatus()==null){
                 pendingHouseHoldList.add(item);
             }
             if(item.getHhStatus()!=null && item.getHhStatus().equalsIgnoreCase("")){
                 pendingHouseHoldList.add(item);
             }
         }


     }*/
    private void sortList(int index) {
        if (pendingHouseHoldList != null) {
            switch (index) {
                case 0:

                    Log.d("Pending household", " Sort by House No ");
                    Collections.sort(pendingHouseHoldList, new Comparator<HouseHoldItem>() {
                        @Override
                        public int compare(HouseHoldItem o1, HouseHoldItem o2) {
                            //     AppUtility.showLog(AppConstant.LOG_STATUS,"HouseholdStatusFragment",o1.getAhlslnohhd());
                            if (o1.getRsbyUrnId() != null && !o1.getRsbyUrnId().equalsIgnoreCase("") && o2.getRsbyUrnId() != null && !o2.getRsbyUrnId().equalsIgnoreCase("")) {
                                return o1.getRsbyUrnId().compareTo(o2.getRsbyUrnId());
                            }
      /*                  if(status)
                        return 1;
                        else
                            return 0;

                        //return Integer.compare(o1.getAge(), o2.getId());*/
                            return 0;
                        }
                    });
                    break;
                case 1:
                    Collections.sort(pendingHouseHoldList, new Comparator<HouseHoldItem>() {
                        @Override
                        public int compare(HouseHoldItem o1, HouseHoldItem o2) {
                            return o1.getName().compareTo(o2.getName());
                        }
                    });
                    break;
            }


            OtherMemberAdapter adapter = new OtherMemberAdapter(context, pendingHouseHoldList);
            houseHoldRecycleList.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    private void setupScreen(View view) {
        context = getActivity();
       /* householdResponse=SeccHouseholdResponse.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.SECC_HOUSE_HOLD_CONTENT, context));*/
        locationItem = VerifierLocationItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SELECTED_BLOCK, context));
        householdStatusList = SeccDatabase.getFamilyStatusList(context);
        HOUSEHOLD_SORT_STATUS = ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.HOUSEHOLD_SORT_STATUS, context);
        notFindMemberLayout = (RelativeLayout) view.findViewById(R.id.notFindRelLayout);
        houseHoldRecycleList = (RecyclerView) view.findViewById(R.id.pendingHouseList);
        searchHouseholdET = (EditText) view.findViewById(R.id.searchHouseholdET);
        searchClearLayout = (RelativeLayout) view.findViewById(R.id.notFindRelLayout);
        noDataErrorTV = (TextView) view.findViewById(R.id.noDataErrorTV);
        noDataErrorTV.setVisibility(View.GONE);
        syncButton = (Button)view.findViewById(R.id.syncButton);
        sortedSP = (Spinner) view.findViewById(R.id.sortSP);
        if (pendingHouseHoldList != null && pendingHouseHoldList.size() > 0) {

        } else {
            noDataErrorTV.setVisibility(View.VISIBLE);
        }

        if (ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, SELECTED_POSTION, context) != null) {
            selectedPosition = Integer.parseInt(ProjectPrefrence.getSharedPrefrenceData(AppConstant.
                    PROJECT_PREF, SELECTED_POSTION, context));
        }
        findAllHousehold();
        houseHoldRecycleList.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        houseHoldRecycleList.setLayoutManager(layoutManager);
        layoutManager.scrollToPosition(selectedPosition);
        houseHoldRecycleList.setItemAnimator(new DefaultItemAnimator());

        //houseHoldRecycleList.smoothScrollToPosition();
        //getSeccHouseholdList();
        prepareFamilyStatusSpinner();
        if (HOUSEHOLD_SORT_STATUS != null) {
            sortedSP.setSelection(Integer.parseInt(HOUSEHOLD_SORT_STATUS));
        } else {

        }


        sortedSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.HOUSEHOLD_SORT_STATUS, i + "", context);
                if (pendingHouseHoldList != null) {
                    sortList(i);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        /*adapter=new OtherMemberAdapter(context,householdResponse.getSeccHouseholdList());
        houseHoldRecycleList.setAdapter(adapter);
        adapter.notifyDataSetChanged();*/
        searchHouseholdET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //  Log.d(TAG, " Search Query : " + s.toString());

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchClearLayout.setVisibility(View.VISIBLE);
                if (s.toString().length() > 0) {
                    publishSearchList(s.toString());
                } else {
                    searchClearLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        if (status != null) {
            if (status.equalsIgnoreCase("5")) {
                if (pendingHouseHoldList != null && pendingHouseHoldList.size() > 0) {
                    syncButton.setVisibility(View.VISIBLE);
                    syncButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (activity.isNetworkAvailable()) {
                                if (totalHouseHold != null && totalHouseHold.size() > 0) {
                                    if (findReadyToSyncHousehold() != null && findReadyToSyncHousehold().size() > 0) {
                                        Intent theIntent = new Intent(context, SyncHouseholdActivity.class);
                                        activity.startActivity(theIntent);
                                        activity.leftTransition();
                                    } else {
                                        CustomAlert.alertWithOk(context, context.getResources().getString(R.string.noHouseholdToSync));
                                    }
                                } else {
                                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.noHouseholdToSync));
                                }
                            } else {
                                CustomAlert.alertWithOk(context, getResources()
                                        .getString(R.string.internet_connection_msg));
                            }
                        }
                    });
                }
            }
        }


    }

    private class OtherMemberAdapter extends RecyclerView.Adapter<OtherMemberAdapter.MyViewHolder> {

        View view;
        AlertDialog dialog;
        private ArrayList<HouseHoldItem> dataSet;
        // Context context;
        private TextView text;
        //private Activity context;
        private SparseBooleanArray selectedItems = new SparseBooleanArray();

        public class MyViewHolder extends RecyclerView.ViewHolder {
            RelativeLayout itemlay;
            TextView hofNameTV, noOfFamilyTV, fatherNameTV, houseHoldNo,
                    addressTV, genderTV, verifiedMemTV, localHofNameTV,
                    houseHoldStatusTV, syncDateTV, errorTV, nhpsIdTV, dataSourceTV, urnIdTV;
            ImageView rightArrowmark;
            ImageView next;
            Button eSignBT;
            LinearLayout syncTimeLayout, errorLayout, nhpsIdLayout, urnIdLayout, householdIdLayout;

            public MyViewHolder(final View itemView) {
                super(itemView);
                verifiedMemTV = (TextView) itemView.findViewById(R.id.verifiedMemTV);
                noOfFamilyTV = (TextView) itemView.findViewById(R.id.totalMemberTV);
                hofNameTV = (TextView) itemView.findViewById(R.id.hofNameTV);
                localHofNameTV = (TextView) itemView.findViewById(R.id.localHofNameTV);
                houseHoldNo = (TextView) itemView.findViewById(R.id.houseNoTV);
                fatherNameTV = (TextView) itemView.findViewById(R.id.hhdFatherNameTV);
                addressTV = (TextView) itemView.findViewById(R.id.hhdAddressTV);
                genderTV = (TextView) itemView.findViewById(R.id.hhdGenderTV);
                errorTV = (TextView) itemView.findViewById(R.id.errorTV);
                dataSourceTV = (TextView) itemView.findViewById(R.id.dataSourceTV);
                houseHoldStatusTV = (TextView) itemView.findViewById(R.id.householdStatusTV);
                rightArrowmark = (ImageView) itemView.findViewById(R.id.rightArrowMarkIV);
                syncDateTV = (TextView) itemView.findViewById(R.id.syncDateTV);
                nhpsIdTV = (TextView) itemView.findViewById(R.id.nhpsIdTV);
                syncTimeLayout = (LinearLayout) itemView.findViewById(R.id.syncTimeLayout);
                errorLayout = (LinearLayout) itemView.findViewById(R.id.errorLayout);
                nhpsIdLayout = (LinearLayout) itemView.findViewById(R.id.nhpsIdLayout);
                householdIdLayout = (LinearLayout) itemView.findViewById(R.id.householdIdLayout);
                urnIdLayout = (LinearLayout) itemView.findViewById(R.id.urnIdLayout);
                urnIdTV = (TextView) itemView.findViewById(R.id.urnIdTV);
                householdIdLayout.setVisibility(View.GONE);
                urnIdLayout.setVisibility(View.GONE);
                nhpsIdLayout.setVisibility(View.GONE);
                syncTimeLayout.setVisibility(View.GONE);
                rightArrowmark.setVisibility(View.GONE);
                errorLayout.setVisibility(View.GONE);

                if (status.equalsIgnoreCase(HouseholdStatusBoardFragment.SYNC_HOUSEHOLD)) {
                    rightArrowmark.setVisibility(View.VISIBLE);
                    syncTimeLayout.setVisibility(View.VISIBLE);
                } else if (status.equalsIgnoreCase(HouseholdStatusBoardFragment.TOTAL_HOUSEHOLD)) {
                    rightArrowmark.setVisibility(View.VISIBLE);
                } else if (status.equalsIgnoreCase(HouseholdStatusBoardFragment.VISITED_HOUSEHOLD)) {

                } else if (status.equalsIgnoreCase(HouseholdStatusBoardFragment.UNDER_SURVEYED_HOUSEHOLD)) {
                    rightArrowmark.setVisibility(View.VISIBLE);
                } else if (status.equalsIgnoreCase(HouseholdStatusBoardFragment.SURVEYED_HOUSEHOLD)) {
                    rightArrowmark.setVisibility(View.VISIBLE);
                } else if (status.equalsIgnoreCase(HouseholdStatusBoardFragment.PENDING_HOUSEHOLD)) {
                    rightArrowmark.setVisibility(View.VISIBLE);
                }

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (selectedItems.get(getAdapterPosition(), false)) {
                            selectedItems.delete(getAdapterPosition());
                            v.setSelected(false);
                            itemView.setBackgroundColor(AppUtility.getColor(context, R.color.white_shine));
                        } else {
                            selectedItems.put(getAdapterPosition(), true);
                            v.setSelected(true);
                            itemView.setBackgroundColor(AppUtility.getColor(context, R.color.gray));

                        }
                        selectedPosition = getPosition();
                        ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, SELECTED_POSTION, selectedPosition + "", context);

                        if (status.equalsIgnoreCase(HouseholdStatusBoardFragment.SYNC_HOUSEHOLD)) {
                            //   CustomAlert.alertWithOk(context,"You can't edit syced household");
                           /* Intent theIntent=new Intent(context,SECCFamilyListActivity.class);
                            selectedMemberItem=new SelectedMemberItem();
                            selectedMemberItem.setHouseHoldItem(dataSet.get(getPosition()));
                            ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                                    AppConstant.SELECTED_ITEM_FOR_VERIFICATION,selectedMemberItem.serialize(), context);
                            startActivity(theIntent);
                            activity.finish();
                            activity.leftTransition();*/
                            Intent theIntent = new Intent(context, SeccMemberListActivity.class);
                            selectedMemberItem = new SelectedMemberItem();
                            selectedMemberItem.setHouseHoldItem(dataSet.get(getPosition()));
                            ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                                    AppConstant.SELECTED_ITEM_FOR_VERIFICATION, selectedMemberItem.serialize(), context);
                        /*theIntent.putExtra(AppConstant.SELECTED_MEMBER, dataSet.get(getPosition()));
                        ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                                AppConstant.SELECTED_MEMBER, dataSet.get(getPosition()).serialize(), context);*/
                            startActivity(theIntent);
                            activity.finish();
                            activity.leftTransition();
                        } else if (status.equalsIgnoreCase(HouseholdStatusBoardFragment.TOTAL_HOUSEHOLD)) {
                            Intent theIntent = new Intent(context, SeccMemberListActivity.class);
                            selectedMemberItem = new SelectedMemberItem();
                            selectedMemberItem.setHouseHoldItem(dataSet.get(getPosition()));
                            ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                                    AppConstant.SELECTED_ITEM_FOR_VERIFICATION, selectedMemberItem.serialize(), context);
                        /*theIntent.putExtra(AppConstant.SELECTED_MEMBER, dataSet.get(getPosition()));
                        ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                                AppConstant.SELECTED_MEMBER, dataSet.get(getPosition()).serialize(), context);*/
                            startActivity(theIntent);
                            activity.finish();
                            activity.leftTransition();
                        } else if (status.equalsIgnoreCase(HouseholdStatusBoardFragment.VISITED_HOUSEHOLD)) {

                        } else if (status.equalsIgnoreCase(HouseholdStatusBoardFragment.UNDER_SURVEYED_HOUSEHOLD)) {
                            // Intent theIntent=new Intent(context,SECCFamilyListActivity.class);
                            Intent theIntent = new Intent(context, SeccMemberListActivity.class);

                            selectedMemberItem = new SelectedMemberItem();
                            selectedMemberItem.setHouseHoldItem(dataSet.get(getPosition()));
                            ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                                    AppConstant.SELECTED_ITEM_FOR_VERIFICATION, selectedMemberItem.serialize(), context);
                        /*theIntent.putExtra(AppConstant.SELECTED_MEMBER, dataSet.get(getPosition()));
                        ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                                AppConstant.SELECTED_MEMBER, dataSet.get(getPosition()).serialize(), context);*/
                            startActivity(theIntent);
                            activity.finish();
                            activity.leftTransition();
                        } else if (status.equalsIgnoreCase(HouseholdStatusBoardFragment.SURVEYED_HOUSEHOLD)) {
                            // Intent theIntent=new Intent(context,SECCFamilyListActivity.class);
                            AppUtility.showLog(AppConstant.LOG_STATUS, "Household status ", "Household item : " + dataSet.get(getPosition()).serialize());
                            Intent theIntent = new Intent(context, SeccMemberListActivity.class);
                            selectedMemberItem = new SelectedMemberItem();
                            selectedMemberItem.setHouseHoldItem(dataSet.get(getPosition()));
                            ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                                    AppConstant.SELECTED_ITEM_FOR_VERIFICATION, selectedMemberItem.serialize(), context);
                        /*theIntent.putExtra(AppConstant.SELECTED_MEMBER, dataSet.get(getPosition()));
                        ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                                AppConstant.SELECTED_MEMBER, dataSet.get(getPosition()).serialize(), context);*/
                            startActivity(theIntent);
                            activity.finish();
                            activity.leftTransition();
                        } else if (status.equalsIgnoreCase(HouseholdStatusBoardFragment.PENDING_HOUSEHOLD)) {
                            //Intent theIntent=new Intent(context,SECCFamilyListActivity.class);
                            Intent theIntent = new Intent(context, SeccMemberListActivity.class);
                            selectedMemberItem = new SelectedMemberItem();
                            String str = dataSet.get(getPosition()).serialize();
                            System.out.print(str);
                            selectedMemberItem.setHouseHoldItem(dataSet.get(getPosition()));
                            ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                                    AppConstant.SELECTED_ITEM_FOR_VERIFICATION, selectedMemberItem.serialize(), context);
                        /*theIntent.putExtra(AppConstant.SELECTED_MEMBER, dataSet.get(getPosition()));
                        ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                                AppConstant.SELECTED_MEMBER, dataSet.get(getPosition()).serialize(), context);*/
                            startActivity(theIntent);
                            activity.finish();
                            activity.leftTransition();

                        }

                    }
                });

            }
        }

        public void addAll(List<HouseHoldItem> list) {

            dataSet.addAll(list);
            notifyDataSetChanged();
        }

        public OtherMemberAdapter(Context context, ArrayList<HouseHoldItem> data) {

            this.dataSet = data;
            ;
            this.text = text;
        }

        @Override
        public OtherMemberAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                  int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_pending_family_list_item, parent, false);

            //view.setOnClickListener(MainActivity.myOnClickListener);

            OtherMemberAdapter.MyViewHolder myViewHolder = new OtherMemberAdapter.MyViewHolder(view);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(final OtherMemberAdapter.MyViewHolder holder, final int listPosition) {


            HouseHoldItem item = dataSet.get(listPosition);

            holder.noOfFamilyTV.setText("2");
            holder.verifiedMemTV.setText("0");
            holder.itemView.setBackgroundColor(AppUtility.getColor(context, R.color.white_shine));

            if (listPosition == selectedPosition) {
                holder.itemView.setBackgroundColor(AppUtility.getColor(context, R.color.dark_grey));
            }
            AppUtility.showLog(AppConstant.LOG_STATUS, "gahsgjga", "Hof Name : " + item.getName());

            holder.householdIdLayout.setVisibility(View.GONE);
            holder.urnIdLayout.setVisibility(View.GONE);
            holder.houseHoldNo.setText("-");
            holder.hofNameTV.setText("-");
            holder.localHofNameTV.setText("-");
            holder.genderTV.setText("-");
            holder.addressTV.setText("-");
            if (item.getDataSource() != null && item.getDataSource().equalsIgnoreCase(AppConstant.RSBY_SOURCE)) {
                // holder.dataSourceTV.setText("RSBY");
               /* holder.hofNameTV.setText("");
                AppUtility.showLog(AppConstant.LOG_STATUS,"gahsgjga","Hof Name : "+item.getName());
                holder.hofNameTV.setText(item.getName());
                holder.dataSourceTV.setText(AppConstant.RSBY_SOURCE_NAME);*/
                showRsbyHouseHold(item, holder);
            } else {
                /*holder.hofNameTV.setText("");
                AppUtility.showLog(AppConstant.LOG_STATUS,"gahsgjga","Hof Name : "+item.getRsbyName());
                holder.hofNameTV.setText(item.getRsbyName());
                holder.dataSourceTV.setText(AppConstant.SECC_SOURCE_NAME);*/
                showSeccHouseHold(item, holder);
            }

            holder.houseHoldStatusTV.setText("-");
            if (item.getHhStatus() != null) {
                for (FamilyStatusItem familyStatusItem : householdStatusList) {
                    if (familyStatusItem.getStatusCode().equalsIgnoreCase(item.getHhStatus())) {
                        holder.houseHoldStatusTV.setText(familyStatusItem.getStatusDesc());
                        //   AppUtility.showLog(AppConstant.LOG_STATUS,"Household Fragment" ,"Household Status : "+familyStatusItem.getStatusDesc());
                        break;
                    }
                }
            }

            if (item.getSyncDt() != null) {
                String dateTime;
                try {
                     dateTime = DateTimeUtil.convertTimeMillisIntoStringDate(Long.parseLong(item.getSyncDt()), AppConstant.SYNC_DATE_TIME);
                }catch (Exception ex){
                    dateTime = item.getSyncDt();
                }
                    if (dateTime != null) {
                    holder.syncDateTV.setText(dateTime);
                }
            }
            if (item.getError_code() != null && !item.getError_code().equalsIgnoreCase("")) {
                holder.errorLayout.setVisibility(View.VISIBLE);
                holder.errorTV.setText(item.getError_msg());
            }
            holder.nhpsIdLayout.setVisibility(View.GONE);
            if (item.getSyncedStatus() != null && item.getSyncedStatus().trim()
                    .equalsIgnoreCase(AppConstant.SYNCED_STATUS)) {
                holder.nhpsIdLayout.setVisibility(View.VISIBLE);
                holder.nhpsIdTV.setText(item.getNhpsId());
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

    private void publishSearchList(String searchTag) {
        ArrayList<HouseHoldItem> dataList = new ArrayList<>();
        for (HouseHoldItem item : houseHoldList) {
            if (item.getName() != null && item.getName().toLowerCase().contains(searchTag.toLowerCase())) {
                dataList.add(item);
            }

            if (item.getAhlslnohhd() != null && item.getAhlslnohhd().toLowerCase().contains(searchTag.toLowerCase())) {
                dataList.add(item);
            }
        }

        if (dataList.size() > 0) {
            notFindMemberLayout.setVisibility(View.GONE);
            adapter = new OtherMemberAdapter(context, dataList);
            houseHoldRecycleList.setAdapter(adapter);


            final Handler handler = new Handler();
//100ms wait to scroll to item after applying changes
            /*handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    houseHoldRecycleList.smoothScrollToPosition(10);
                }}, 100);*/
            // houseHoldRecycleList.getLayoutManager().scrollToPosition(5);
        } else {
            houseHoldRecycleList.setAdapter(null);
            notFindMemberLayout.setVisibility(View.VISIBLE);
        }
    }

    private void showRsbyHouseHold(HouseHoldItem item, OtherMemberAdapter.MyViewHolder holder) {
        // holder.itemlay.setBackgroundColor(AppUtility.getColor(context,getResources().getColor(R.color.Bg_bal_color)));
        holder.urnIdLayout.setVisibility(View.VISIBLE);
        holder.urnIdTV.setText(AppUtility.formatUrn(item.getRsbyUrnId()));
        holder.hofNameTV.setText("");
      //  AppUtility.showLog(AppConstant.LOG_STATUS, "gahsgjga", "Hof Name : " + item.getName());
        if(item.getRsbyName()!=null) {
            holder.hofNameTV.setText(item.getRsbyName());
        }else{
            holder.hofNameTV.setText(item.getName());
        }
        holder.dataSourceTV.setText(AppConstant.RSBY_SOURCE_NAME);
        //  holder.dataSourceTV.setTextColor(AppUtility.getColor(context,getResources().getColor(R.color.Bg_bal_color)));
        String gender = "";
        if (item.getRsbyGender() != null && item.getRsbyGender().trim().equalsIgnoreCase(AppConstant.MALE_GENDER)) {
            gender = AppConstant.MALE;
        } else if (item.getRsbyGender() != null && item.getRsbyGender().trim().equalsIgnoreCase(AppConstant.FEMALE_GENDER)) {
            gender = AppConstant.FEMALE;
        } else {
            gender = AppConstant.OTHER_GENDER_NAME;
        }
        holder.genderTV.setText(gender);

    }

    private void showSeccHouseHold(HouseHoldItem item, OtherMemberAdapter.MyViewHolder holder) {
        holder.householdIdLayout.setVisibility(View.VISIBLE);
        holder.hofNameTV.setText("");
        AppUtility.showLog(AppConstant.LOG_STATUS, "gahsgjga", "Hof Name : " + item.getRsbyName());
        holder.hofNameTV.setText(item.getName());
        holder.dataSourceTV.setText(AppConstant.SECC_SOURCE_NAME);

        if (item.getAhlslnohhd() != null)
            holder.houseHoldNo.setText(item.getAhlslnohhd());
        if (item.getName() != null)
            holder.hofNameTV.setText(item.getName());
        if (item.getFathername() != null) {
            holder.fatherNameTV.setText(item.getFathername());
        }
        if (item.getNameSl() != null)
            holder.localHofNameTV.setText(Html.fromHtml(item.getNameSl()));
        String gender = "";
        if (item.getGenderid() != null && item.getGenderid().trim().equalsIgnoreCase(AppConstant.MALE_GENDER)) {
            gender = AppConstant.MALE;
        } else if (item.getGenderid() != null && item.getGenderid().trim().equalsIgnoreCase(AppConstant.FEMALE_GENDER)) {
            gender = AppConstant.FEMALE;
        } else {
            gender = AppConstant.OTHER_GENDER_NAME;
        }
        holder.genderTV.setText(gender);
        String address = "";
        if (item != null) {
            if (item.getAddressline1() != null && !item.getAddressline1().trim().
                    equalsIgnoreCase("-") && !item.getAddressline1().equalsIgnoreCase("")) {
                address = address + item.getAddressline1() + ", ";
                // AppUtility.showLog(AppConstant.LOG_STATUS,TAG," Address3 : "+seccItem.getAddressline1());

            }
            if (item.getAddressline2() != null && !item.getAddressline2().trim()
                    .equalsIgnoreCase("-") && !item.getAddressline2().equalsIgnoreCase("")) {
                //AppUtility.showLog(AppConstant.LOG_STATUS,TAG," Address : "+seccItem.getAddressline2());
                address = address + item.getAddressline2() + ", ";
            }
            if (item.getAddressline3() != null && !item.getAddressline3().trim().
                    equalsIgnoreCase("-") && !item.getAddressline3().equalsIgnoreCase("")) {
                //AppUtility.showLog(AppConstant.LOG_STATUS,TAG," Address1 : "+seccItem.getAddressline3());

                address = address + item.getAddressline3() + ", ";
            }
            if (item.getAddressline4() != null && !item.getAddressline4().trim().
                    equalsIgnoreCase("-") && !item.getAddressline4().equalsIgnoreCase("")) {
                // AppUtility.showLog(AppConstant.LOG_STATUS,TAG," Address2 : "+seccItem.getAddressline4());

                address = address + item.getAddressline4();
            }
            holder.addressTV.setText(address);
        }
    }

    private void prepareFamilyStatusSpinner() {
        spinnerList = new ArrayList<>();

        spinnerList.add(context.getResources().getString(R.string.sortByUrn));
        spinnerList.add(context.getResources().getString(R.string.sortByName));


        ArrayAdapter<String> maritalAdapter = new ArrayAdapter<>(context, R.layout.custom_drop_down, R.id.textView, spinnerList);
        sortedSP.setAdapter(maritalAdapter);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (SearchActivityWithHouseHold) context;
    }


    public void findAllHousehold() {
        if(locationItem!=null) {
            totalHouseHold = SeccDatabase.getHouseHoldList(locationItem.getStateCode()
                    , locationItem.getDistrictCode()
                    , locationItem.getTehsilCode(), locationItem.getVtCode(),
                    locationItem.getWardCode(), locationItem.getBlockCode(), context);
        }
    }


    private ArrayList<HouseHoldItem> findReadyToSyncHousehold() {
        ArrayList<HouseHoldItem> list = new ArrayList<>();
        if(totalHouseHold!=null)
            for (HouseHoldItem item : totalHouseHold) {

                if (item.getSyncedStatus() != null && item.getSyncedStatus().equalsIgnoreCase(AppConstant.SYNCED_STATUS)) {

                } else {
                    if (item.getLockSave() != null && item.getLockSave().equalsIgnoreCase(AppConstant.LOCKED + "")) {
                        list.add(item);
                    }
                    //   }
                }
            }
        return list;
    }
}
