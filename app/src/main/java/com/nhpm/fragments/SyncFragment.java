package com.nhpm.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nhpm.LocalDataBase.dto.SeccDatabase;
import com.nhpm.Models.response.seccMembers.ErrorItem;
import com.nhpm.Models.response.seccMembers.HouseHoldItem;
import com.nhpm.Models.response.seccMembers.SeccMemberItem;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;
import com.nhpm.Utility.AppUtility;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SyncFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SyncFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private LinearLayout totalHouseholdLayout,syncedHouseholdLayout,readyToSyncHouseholdLayout,syncErrorLayout;
    public static  String TOTAL,SYNCED,READY_TO_SYNCED,PENDING;
    private int YELLOW,WHITE_SHINE;
    private Context context;
    private String TAG="Sync Fragment";
    private ArrayList<HouseHoldItem> pendingHouseholdList,syncedHouseholdList,underSurveyedHouseholdList;
    private ArrayList<HouseHoldItem> householdList;
    private TextView totalTV,syncedTV,readyToSyncTV,syncErrorTV;
    private SyncStatusFragment syncStatusFragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragTransection;


    public SyncFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SyncFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SyncFragment newInstance(String param1, String param2) {
        SyncFragment fragment = new SyncFragment();
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
        View view=inflater.inflate(R.layout.fragment_sync, container, false);
        setupScreen(view);
        return view;
    }
    private void setupScreen(View view){
        context=getActivity();
        fragmentManager=getActivity().getSupportFragmentManager();
        findSyncList();
        YELLOW = AppUtility.getColor(context, R.color.yellow);
        WHITE_SHINE = AppUtility.getColor(context, R.color.dark_grey);
        totalHouseholdLayout=(LinearLayout)view.findViewById(R.id.totalHouseholdLayout);
        syncedHouseholdLayout=(LinearLayout)view.findViewById(R.id.syncHouseholdLayout);
        readyToSyncHouseholdLayout=(LinearLayout)view.findViewById(R.id.readyToSynLayout);
        syncErrorLayout=(LinearLayout)view.findViewById(R.id.syncErrorLayout);
        totalTV=(TextView)view.findViewById(R.id.totalHouseholdTV) ;
        syncedTV=(TextView)view.findViewById(R.id.syncedHouseholdTV) ;
        readyToSyncTV=(TextView)view.findViewById(R.id.readyToSyncedTV) ;
        syncErrorTV=(TextView)view.findViewById(R.id.syncErrorTV) ;
        totalTV.setText(SeccDatabase.houseHoldCount(context)+"");
        readyToSyncHouseholdLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openSyncStatusFragment(pendingHouseholdList,READY_TO_SYNCED);
            }
        });
    }
    private void openSyncStatusFragment(ArrayList<HouseHoldItem> readyToSyncList,String status){

        totalHouseholdLayout.setBackgroundColor(WHITE_SHINE);
        syncedHouseholdLayout.setBackgroundColor(WHITE_SHINE);
        readyToSyncHouseholdLayout.setBackgroundColor(WHITE_SHINE);
        syncedHouseholdLayout.setBackgroundColor(WHITE_SHINE);
        syncErrorLayout.setBackgroundColor(WHITE_SHINE);
        if (status.equalsIgnoreCase(READY_TO_SYNCED)) {
            readyToSyncHouseholdLayout.setBackgroundColor(YELLOW);
        }
        fragTransection=fragmentManager.beginTransaction();
        if(syncStatusFragment!=null){
            fragTransection.detach(syncStatusFragment);
        }
        syncStatusFragment =new SyncStatusFragment();
        syncStatusFragment.setStatus(status);
        syncStatusFragment.setPendingHouseholdList(readyToSyncList);
        fragTransection.replace(R.id.syncStatusFragContainer,syncStatusFragment);
        fragTransection.commitAllowingStateLoss();
    }
    private void findSyncList(){

        syncedHouseholdList=new ArrayList<>();
        underSurveyedHouseholdList=new ArrayList<>();
        householdList= SeccDatabase.getAllHouseHold(context);
        ///  memberList=new ArrayList<>();
        pendingHouseholdList=new ArrayList<>();
        ArrayList<SeccMemberItem> tempList;
        for(HouseHoldItem item : householdList){
            boolean savedStatus=false;
            if(item.getSyncedStatus()!=null  && item.getSyncedStatus().
                    equalsIgnoreCase(AppConstant.SYNCED_STATUS)) {
                Log.d(TAG," Synce Status :"+item.getSyncedStatus());
                syncedHouseholdList.add(item);
            }else{
                if (item.getHhStatus() != null && !item.getHhStatus().equalsIgnoreCase("")) {
                    ArrayList<SeccMemberItem> seccList = SeccDatabase.getSeccMemberList(item.getHhdNo(), context);
                    //  tempList=new ArrayList<>();
                    //SeccMemberItem seccItem=null;
                    ErrorItem errorItem=null;
                    for (SeccMemberItem seccItem : seccList) {
                        //  if(seccItem.getHhStatus().equalsIgnoreCase())
                        Log.d(TAG, "Synced Member" + item.getLockSave());
                        if (seccItem.getLockedSave()!=null && seccItem.getLockedSave().equalsIgnoreCase(AppConstant.SAVE + "")) {
                            //  memberList.add(seccItem);
                           // Log.d(TAG, "Synced Member 111111" + item.getLockSave()+ " Ahl tin : "+seccItem.getAhlTin()+" hhd No : "+seccItem.getHhdNo());
                            savedStatus = true;
                            break;
                        }
                        if(seccItem.getLockedSave()==null){
                            savedStatus=true;
                         //   Log.d(TAG, "Synced Member 111111" + item.getLockSave()+ " Ahl tin : "+seccItem.getAhlTin()+" hhd No : "+seccItem.getHhdNo());
                        }
                        if(seccItem.getLockedSave()!=null && seccItem.getLockedSave().equalsIgnoreCase("")){
                            savedStatus=true;
                     //       Log.d(TAG, "Synced Member 111111" + item.getLockSave()+ " Ahl tin : "+seccItem.getAhlTin()+" hhd No : "+seccItem.getHhdNo());
                        }
                        errorItem=SeccDatabase.getMemberErrorByHhdNo(context,seccItem.getHhdNo());
                        Log.d(TAG," Error Item : "+errorItem);
                    }
                    if (savedStatus) {
                        Log.d(TAG, "Synced Member" + item.getLockSave());
                        underSurveyedHouseholdList.add(item);
                    } else {
                        // memberList.addAll(seccList);
                        Log.d(TAG," Surveyed household jscon : \n"+item.serialize());
                        item.setErrorItem(errorItem);
                        pendingHouseholdList.add(item);
                    }
                }else{
                    underSurveyedHouseholdList.add(item);
                }
            }
        }
    }

}
