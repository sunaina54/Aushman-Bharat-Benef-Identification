package com.nhpm.rsbyFieldValidation.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.customComponent.CustomAsyncTask;
import com.nhpm.LocalDataBase.dto.SeccDatabase;
import com.nhpm.Models.response.rsbyMembers.RSBYItem;
import com.nhpm.Models.response.rsbyMembers.RsbyHouseholdItem;
import com.nhpm.Models.response.verifier.VerifierLocationItem;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;
import com.nhpm.Utility.AppUtility;
import com.nhpm.rsbyFieldValidation.RsbySyncHouseholdActivity;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SyncRsbyHouseholdDashoardfragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SyncRsbyHouseholdDashoardfragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Context context;
    private ArrayList<RSBYItem> memberList;
    private ArrayList<RsbyHouseholdItem> householdList;
    private final String TAG="SyncHouseholdActivity";
    //private ImageView backIV;
    // private RelativeLayout  backLayout;
    private ArrayList<RsbyHouseholdItem> pendingHouseholdList,syncedHouseholdList,underSurveyedHouseholdList;
    private Button syncAllBT;
    //  private TextView headerTV;
    //synchouseholdStatTV,underSurveyedHiueholdTV,surveyedHouseholdTV
    private TextView totalTV,syncedTV,readyToSyncTV,syncErrorTV;
    private CustomAsyncTask asyncTask;
    private ImageView settings;
    private LinearLayout totalHouseholdLayout,syncedHouseholdLayout,readyToSyncHouseholdLayout,syncErrorLayout;
    private ArrayList<RsbyHouseholdItem> totalHouseHold;
    private VerifierLocationItem downloadedLocation;
    public static  String TOTAL_HOUSEHOLD="1",SYNCED_HOUSEHOLD="2",READY_TO_SYNC="3",SYNC_ERROR="4";
    private int SELECTED,UNSELECTED;
    private RsbySyncStatusFragment fragment;
    private FragmentTransaction fragTransect;
    private FragmentManager fragMgr;
    private String SYNC_SELECTED_TAB;
    private ArrayList<RsbyHouseholdItem> seccMemberList;
    private RsbySyncHouseholdActivity activity;


    public SyncRsbyHouseholdDashoardfragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SyncRsbyHouseholdDashoardfragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SyncRsbyHouseholdDashoardfragment newInstance(String param1, String param2) {
        SyncRsbyHouseholdDashoardfragment fragment = new SyncRsbyHouseholdDashoardfragment();
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
        View v=inflater.inflate(R.layout.fragment_sync_household_dashoardfragment, container, false);
        setupScreen(v);
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity=(RsbySyncHouseholdActivity) context;
    }
    private void setupScreen(View v){
        context=getActivity();
        fragMgr=getActivity().getSupportFragmentManager();
        SELECTED= AppUtility.getColor(context, R.color.yellow);
        UNSELECTED= AppUtility.getColor(context, R.color.dark_grey);
        totalHouseholdLayout=(LinearLayout)v.findViewById(R.id.totalHouseholdLayout);
        syncedHouseholdLayout=(LinearLayout)v.findViewById(R.id.syncedHouseLayout);
        readyToSyncHouseholdLayout=(LinearLayout)v.findViewById(R.id.pendingHouseholdLayout);
        syncErrorLayout=(LinearLayout)v.findViewById(R.id.syncErrorLayout);
        totalTV=(TextView)v.findViewById(R.id.totalHouseholdTV) ;
        syncedTV=(TextView)v.findViewById(R.id.syncedHouseholdTV) ;
        readyToSyncTV=(TextView)v.findViewById(R.id.readyToSyncedTV) ;
        syncErrorTV=(TextView)v.findViewById(R.id.syncErrorTV) ;

        findAllHousehold();
        // findSyncList();
        updateStatus();
        // String selectedTab=ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,SYNC_SELECTED_TAB,context);
        if(AppUtility.redirection==10){
          /*  if(selectedTab.equalsIgnoreCase(TOTAL_HOUSEHOLD)){
                openSyncStatusFragment(totalHouseHold,selectedTab);
            }
            if(selectedTab.equalsIgnoreCase(SYNCED_HOUSEHOLD)){
                openSyncStatusFragment(findSyncedHousehold(),selectedTab);
            }
            if(selectedTab.equalsIgnoreCase(READY_TO_SYNC)){
                openSyncStatusFragment(findReadyToSyncHousehold(),selectedTab);
            }*/
            //  if(selectedTab.equalsIgnoreCase(SYNC_ERROR)){
            openSyncStatusFragment(findErrorHousehold(),SYNC_ERROR);
            AppUtility.redirection=0;
            //  }
        }else{
            openSyncStatusFragment(findReadyToSyncHousehold(),READY_TO_SYNC);
        }



        totalHouseholdLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSyncStatusFragment(totalHouseHold,TOTAL_HOUSEHOLD);
            }
        });
        syncedHouseholdLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSyncStatusFragment(findSyncedHousehold(),SYNCED_HOUSEHOLD);
            }
        });
        readyToSyncHouseholdLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSyncStatusFragment(findReadyToSyncHousehold(),READY_TO_SYNC);

            }
        });
        syncErrorLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSyncStatusFragment(findErrorHousehold(),SYNC_ERROR);
            }
        });

    }

    private void updateStatus(){
        totalTV.setText(totalHouseHold.size()+"");
        syncedTV.setText(findSyncedHousehold().size()+"");
        readyToSyncTV.setText(findReadyToSyncHousehold().size()+"");
        syncErrorTV.setText(findErrorHousehold().size()+"");
    }
    public void findAllHousehold(){
        totalHouseHold = SeccDatabase.getAllRsbyHouseHoldList(context);
    }

    private ArrayList<RsbyHouseholdItem> findReadyToSyncHousehold(){
        ArrayList<RsbyHouseholdItem> list=new ArrayList<>();
        for(RsbyHouseholdItem item : totalHouseHold){
            AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Ready To Sync household : "+item.getLockedSave()+"  Name :"+item.getName());
            if(item.getError_code()!=null){

            }else {
                if (item.getSyncedStatus() != null && item.getSyncedStatus().equalsIgnoreCase(AppConstant.SYNCED_STATUS)) {

                } else {
                    if (item.getLockedSave() != null && item.getLockedSave().equalsIgnoreCase(AppConstant.LOCKED + "")) {
                        AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Ready To Sync household : " + item.getName());
                        list.add(item);
                    }
                }
            }
        }
        return list;
    }

    private ArrayList<RsbyHouseholdItem> findSyncedHousehold(){
        ArrayList<RsbyHouseholdItem> list=new ArrayList<>();
        // list=SeccDatabase.getSyncHouseholdList(context,AppConstant.SYNCED_STATUS+"");
        for(RsbyHouseholdItem item : totalHouseHold ){
            if(item.getSyncedStatus()!=null && item.getSyncedStatus().trim().equalsIgnoreCase(AppConstant.SYNCED_STATUS)){
                list.add(item);
            }
        }
        return  list;
    }
    private ArrayList<RsbyHouseholdItem> findErrorHousehold(){
        ArrayList<RsbyHouseholdItem> list=new ArrayList<>();
        // ArrayList<HouseHoldItem> readyToSyncList=findReadyToSyncHousehold();

        for(RsbyHouseholdItem item : totalHouseHold) {
            if(item.getError_code()!=null){
                list.add(item);
            }
           /* ErrorItem errorItem = SeccDatabase.getMemberErrorByHhdNo(context, item.getHhdNo());
            if (errorItem != null){
                AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Error List" + errorItem.getNhpsMemId());
            }
            if(errorItem!=null && errorItem.getHhdNo().equalsIgnoreCase(item.getHhdNo())){

            }*/
        }
        return list;
    }

    private void openSyncStatusFragment(ArrayList<RsbyHouseholdItem> list, String status){
        totalHouseholdLayout.setBackgroundColor(UNSELECTED);
        syncedHouseholdLayout.setBackgroundColor(UNSELECTED);
        readyToSyncHouseholdLayout.setBackgroundColor(UNSELECTED);
        syncErrorLayout.setBackgroundColor(UNSELECTED);
        // ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,SYNC_SELECTED_TAB,"3",context);
        if(status.equalsIgnoreCase(TOTAL_HOUSEHOLD)){
            totalHouseholdLayout.setBackgroundColor(SELECTED);
        }else if(status.equalsIgnoreCase(SYNCED_HOUSEHOLD)){
            syncedHouseholdLayout.setBackgroundColor(SELECTED);
        }else if(status.equalsIgnoreCase(READY_TO_SYNC)){
            readyToSyncHouseholdLayout.setBackgroundColor(SELECTED);
        }else if(status.equalsIgnoreCase(SYNC_ERROR)){
            syncErrorLayout.setBackgroundColor(SELECTED);
        }
        fragTransect=fragMgr.beginTransaction();
        if(fragment!=null){
            fragTransect.detach(fragment);
            fragment=null;
        }
        fragment=new RsbySyncStatusFragment();
        fragment.setPendingHouseholdList(list);
        fragment.setStatus(status);
        fragTransect.replace(R.id.syncFragmentContainerNew,fragment);
        fragTransect.commitAllowingStateLoss();
    }

}
