package com.nhpm.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
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
import com.customComponent.utility.ProjectPrefrence;
import com.nhpm.LocalDataBase.dto.SeccDatabase;
import com.nhpm.Models.response.seccMembers.HouseHoldItem;
import com.nhpm.Models.response.seccMembers.SeccMemberItem;
import com.nhpm.Models.response.verifier.VerifierLocationItem;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;
import com.nhpm.Utility.AppUtility;
import com.nhpm.activity.SyncHouseholdActivity;

import java.util.ArrayList;

/**
 * Created by Saurabh on 1/25/2017.
 */

public class SyncHouseHoldFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    // private RecyclerView syncList;
    private Context context;
    private ArrayList<SeccMemberItem> memberList;
    private ArrayList<HouseHoldItem> householdList;
    private final String TAG = "SyncHouseholdActivity";
    //private ImageView backIV;
    // private RelativeLayout  backLayout;
    private ArrayList<HouseHoldItem> pendingHouseholdList, syncedHouseholdList, underSurveyedHouseholdList;
    private Button syncAllBT;
    //  private TextView headerTV;
    //synchouseholdStatTV,underSurveyedHiueholdTV,surveyedHouseholdTV
    private TextView totalTV, syncedTV, readyToSyncTV, syncErrorTV, yetToValidateTV, invalidTV;
    private CustomAsyncTask asyncTask;
    private ImageView settings;
    private LinearLayout totalHouseholdLayout, syncedHouseholdLayout, readyToSyncHouseholdLayout,
            syncErrorLayout,
            yetToValidate, validationErrorLayout;
    private ArrayList<HouseHoldItem> totalHouseHold;
    private VerifierLocationItem downloadedLocation;
    public static String TOTAL_HOUSEHOLD = "1", SYNCED_HOUSEHOLD = "2",
            READY_TO_SYNC = "3", SYNC_ERROR = "4", YET_TO_VALIDATE = "5", VALIDATION_ERROR = "6";
    private int SELECTED, UNSELECTED;
    private SyncStatusFragment fragment;
    private FragmentTransaction fragTransect;
    private FragmentManager fragMgr;
    private String SYNC_SELECTED_TAB;
    private ArrayList<SeccMemberItem> seccMemberList;
    private SyncHouseholdActivity activity;
    private String downloadedDataType;


    public SyncHouseHoldFragment() {
        // Required empty public constructor
    }


    public static SyncHouseHoldFragment newInstance(String param1, String param2) {
        SyncHouseHoldFragment fragment = new SyncHouseHoldFragment();
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
        View view = inflater.inflate(R.layout.fragment_sync_household, container, false);
        setupScreen(view);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (SyncHouseholdActivity) context;
    }

    private void setupScreen(View v) {
        context = getActivity();
        fragMgr = getActivity().getSupportFragmentManager();
        downloadedLocation = VerifierLocationItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF
                , AppConstant.SELECTED_BLOCK, context));
        downloadedDataType = ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.DOWNLOADINGSOURCE, context);
        SELECTED = AppUtility.getColor(context, R.color.yellow);
        UNSELECTED = AppUtility.getColor(context, R.color.dark_grey);
        totalHouseholdLayout = (LinearLayout) v.findViewById(R.id.totalHouseholdLayout);
        syncedHouseholdLayout = (LinearLayout) v.findViewById(R.id.syncedHouseLayout);
        readyToSyncHouseholdLayout = (LinearLayout) v.findViewById(R.id.pendingHouseholdLayout);
        syncErrorLayout = (LinearLayout) v.findViewById(R.id.syncErrorLayout);
        totalTV = (TextView) v.findViewById(R.id.totalHouseholdTV);
        syncedTV = (TextView) v.findViewById(R.id.syncedHouseholdTV);
        readyToSyncTV = (TextView) v.findViewById(R.id.readyToSyncedTV);
        syncErrorTV = (TextView) v.findViewById(R.id.syncErrorTV);
        yetToValidateTV = (TextView) v.findViewById(R.id.yetToValidateTV);
        invalidTV = (TextView) v.findViewById(R.id.invalidHouseholdTV);
        yetToValidate = (LinearLayout) v.findViewById(R.id.yetToValidate);
        validationErrorLayout = (LinearLayout) v.findViewById(R.id.validationErrorLayout);


        findAllHousehold();
        // findSyncList();
        updateStatus();
        // String selectedTab=ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,SYNC_SELECTED_TAB,context);
        if (AppUtility.redirection == 10) {
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
            openSyncStatusFragment(findErrorHousehold(), SYNC_ERROR);
            AppUtility.redirection = 0;
            //  }
        } else {
            openSyncStatusFragment(findReadyToSyncHousehold(), READY_TO_SYNC);
        }


        totalHouseholdLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSyncStatusFragment(totalHouseHold, TOTAL_HOUSEHOLD);
            }
        });
        syncedHouseholdLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSyncStatusFragment(findSyncedHousehold(), SYNCED_HOUSEHOLD);
            }
        });
        readyToSyncHouseholdLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSyncStatusFragment(findReadyToSyncHousehold(), READY_TO_SYNC);

            }
        });
        syncErrorLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSyncStatusFragment(findErrorHousehold(), SYNC_ERROR);
            }
        });
        yetToValidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSyncStatusFragment(yetToValidate(), YET_TO_VALIDATE);

            }
        });
        validationErrorLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSyncStatusFragment(findValidationError(), VALIDATION_ERROR);

            }
        });

    }

    private ArrayList<HouseHoldItem> findValidationError() {
        ArrayList<HouseHoldItem> list = new ArrayList<>();
        ArrayList<SeccMemberItem> memberList = new ArrayList<>();
        // ArrayList<HouseHoldItem> readyToSyncList=findReadyToSyncHousehold();

        for (HouseHoldItem item : totalHouseHold) {
            if (item.getError_code() != null) {
                if (item.getError_type().equalsIgnoreCase(AppConstant.AADHAAR_VALIDATION_ERROR)) {
                    list.add(item);
                } else if (item.getError_code().equalsIgnoreCase(AppConstant.URI_ALREADY_EXIST)) {
                    list.add(item);
                }
            } else {
               /* if (item.getDataSource() != null && item.getDataSource().equalsIgnoreCase(AppConstant.RSBY_SOURCE)) {
                    memberList = SeccDatabase.getRsbyMemberListWithUrn(item.getRsbyUrnId().trim(), context);
                } else if (item.getDataSource() != null && item.getDataSource().equalsIgnoreCase(AppConstant.SECC_SOURCE)) {
                    memberList = SeccDatabase.getSeccMemberList(item.getHhdNo().trim(), context);
                }
                if (memberList != null && memberList.size() > 0) {
                    for (SeccMemberItem item1 : memberList) {
                        if (item1.getError_code() != null) {
                            list.add(item);
                        }
                    }
                }*/
            }
        }

        return list;
    }

    private ArrayList<HouseHoldItem> yetToValidate() {
        ArrayList<HouseHoldItem> list = new ArrayList<>();
        // list=SeccDatabase.getSyncHouseholdList(context,AppConstant.SYNCED_STATUS+"");

        for (HouseHoldItem item : totalHouseHold) {
            if (item.getSyncedStatus() != null && item.getSyncedStatus().trim().equalsIgnoreCase(AppConstant.SYNCED_STATUS)) {

            } else {
                if (item.getLockSave() != null && item.getLockSave().trim().equalsIgnoreCase(AppConstant.LOCKED + "")) {
                    ArrayList<SeccMemberItem> list1 = new ArrayList<>();
                    if (item != null && item.getDataSource() != null && item.getDataSource().trim().equalsIgnoreCase(AppConstant.RSBY_SOURCE)) {
                        list1 = SeccDatabase.getRsbyMemberListWithUrn(item.getRsbyUrnId().trim(), context);
                    } else {
                        list1 = SeccDatabase.getSeccMemberList(item.getHhdNo().trim(), context);
                    }
                    boolean isYetTovalidate = false;
                    for (SeccMemberItem item1 : list1) {
                        if (item1.getAadhaarAuth() != null && item1.getAadhaarAuth().trim().equalsIgnoreCase(AppConstant.PENDING_STATUS)) {
                            isYetTovalidate = true;
                            break;
                        }
                    }
                    if (isYetTovalidate) {
                        list.add(item);
                    }
                }
            }
        }
        return list;
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connec = (ConnectivityManager) getActivity().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        // Check for network connections
        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||

                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {
            //Toast.makeText(this, " Connected ", Toast.LENGTH_LONG).show();
            return true;
        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {
            // Toast.makeText(this, " Not Connected ", Toast.LENGTH_LONG).show();
            return false;
        }
        return false;
    }
/*
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
                            Log.d(TAG, "Synced Member 111111" + item.getLockSave()+ " Ahl tin : "+seccItem.getNhpsMemId()+" hhd No : "+seccItem.getHhdNo());
                            savedStatus = true;
                            break;
                        }
                        if(seccItem.getLockedSave()==null){
                            savedStatus=true;
                            //Log.d(TAG, "Synced Member 111111" + item.getLockSave()+ " Ahl tin : "+seccItem.getAhlTin()+" hhd No : "+seccItem.getHhdNo());
                        }
                        if(seccItem.getLockedSave()!=null && seccItem.getLockedSave().equalsIgnoreCase("")){
                            savedStatus=true;
                            // Log.d(TAG, "Synced Member 111111" + item.getLockSave()+ " Ahl tin : "+seccItem.getAhlTin()+" hhd No : "+seccItem.getHhdNo());
                        }
                      //  errorItem=SeccDatabase.getMemberErrorByHhdNo(context,seccItem.getHhdNo());
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
*/

    private void updateStatus() {
        totalTV.setText(totalHouseHold.size() + "");
        syncedTV.setText(findSyncedHousehold().size() + "");
        readyToSyncTV.setText(findReadyToSyncHousehold().size() + "");
        syncErrorTV.setText(findErrorHousehold().size() + "");
        yetToValidateTV.setText(yetToValidate().size() + "");
        invalidTV.setText(findValidationError().size() + "");
    }

    public void openSyncStatusFragment(ArrayList<HouseHoldItem> list, String status) {
        totalHouseholdLayout.setBackgroundColor(UNSELECTED);
        syncedHouseholdLayout.setBackgroundColor(UNSELECTED);
        readyToSyncHouseholdLayout.setBackgroundColor(UNSELECTED);
        syncErrorLayout.setBackgroundColor(UNSELECTED);
        yetToValidate.setBackgroundColor(UNSELECTED);
        validationErrorLayout.setBackgroundColor(UNSELECTED);
        // ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,SYNC_SELECTED_TAB,"3",context);
        if (status.equalsIgnoreCase(TOTAL_HOUSEHOLD)) {
            totalHouseholdLayout.setBackgroundColor(SELECTED);
        } else if (status.equalsIgnoreCase(SYNCED_HOUSEHOLD)) {
            syncedHouseholdLayout.setBackgroundColor(SELECTED);
        } else if (status.equalsIgnoreCase(READY_TO_SYNC)) {
            readyToSyncHouseholdLayout.setBackgroundColor(SELECTED);
        } else if (status.equalsIgnoreCase(SYNC_ERROR)) {
            syncErrorLayout.setBackgroundColor(SELECTED);
        } else if (status.equalsIgnoreCase(YET_TO_VALIDATE)) {
            yetToValidate.setBackgroundColor(SELECTED);
        } else if (status.equalsIgnoreCase(VALIDATION_ERROR)) {
            validationErrorLayout.setBackgroundColor(SELECTED);
        }
        fragTransect = fragMgr.beginTransaction();
        if (fragment != null) {
            fragTransect.detach(fragment);
            fragment = null;
        }
        fragment = new SyncStatusFragment();
        fragment.setPendingHouseholdList(list);
        fragment.setStatus(status);
        fragTransect.replace(R.id.syncFragmentContainerNew, fragment);
        fragTransect.commitAllowingStateLoss();
    }

    public void findAllHousehold() {
        if (downloadedDataType != null && downloadedDataType.equalsIgnoreCase(AppConstant.EbWiseDownloading)) {
            totalHouseHold = SeccDatabase.getHouseHoldList(downloadedLocation.getStateCode()
                    , downloadedLocation.getDistrictCode()
                    , downloadedLocation.getTehsilCode(), downloadedLocation.getVtCode(),
                    downloadedLocation.getWardCode(), downloadedLocation.getBlockCode(), context);
        } else if (downloadedDataType != null && downloadedDataType.equalsIgnoreCase(AppConstant.VillageWiseDownloading)) {
            totalHouseHold = SeccDatabase.getHouseHoldVillageWiseList(downloadedLocation.getStateCode()
                    , downloadedLocation.getDistrictCode()
                    , downloadedLocation.getTehsilCode(), downloadedLocation.getVtCode()/*,
               downloadedLocation.getWardCode(),downloadedLocation.getBlockCode()*/, context);
        } else if (downloadedDataType != null && downloadedDataType.equalsIgnoreCase(AppConstant.WardWiseDownloading)) {
            totalHouseHold = SeccDatabase.getAllHouseHoldWardWiseList(downloadedLocation.getStateCode()
                    , downloadedLocation.getDistrictCode()
                    , downloadedLocation.getTehsilCode(), downloadedLocation.getVtCode(),
                    downloadedLocation.getWardCode()/*,downloadedLocation.getBlockCode()*/, context);

        }
    }

    private ArrayList<HouseHoldItem> findReadyToSyncHousehold() {
        ArrayList<HouseHoldItem> list = new ArrayList<>();
        for (HouseHoldItem item : totalHouseHold) {
            AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Ready To Sync household : " + item.getLockSave() + "  Name :" + item.getName());
            if (item.getError_code() != null) {

            } else {
                if (item.getSyncedStatus() != null && item.getSyncedStatus().equalsIgnoreCase(AppConstant.SYNCED_STATUS)) {

                } else {
                    if (item.getLockSave() != null && item.getLockSave().trim().equalsIgnoreCase(AppConstant.LOCKED + "")) {
                        ArrayList<SeccMemberItem> list1 = new ArrayList<>();
                        if (item != null && item.getDataSource() != null && item.getDataSource().trim().equalsIgnoreCase(AppConstant.RSBY_SOURCE)) {
                            list1 = SeccDatabase.getRsbyMemberListWithUrn(item.getRsbyUrnId().trim(), context);
                        } else {
                            list1 = SeccDatabase.getSeccMemberList(item.getHhdNo().trim(), context);
                        }
                        boolean isYetTovalidate = false;
                        for (SeccMemberItem item1 : list1) {
                            if (item1.getAadhaarAuth() != null &&
                                    (item1.getAadhaarAuth().trim()
                                            .equalsIgnoreCase(AppConstant.PENDING_STATUS) || item1.getAadhaarAuth().trim()
                                            .equalsIgnoreCase(AppConstant.INVALID_STATUS))) {
                                isYetTovalidate = true;
                                break;
                            }
                        }
                        if (isYetTovalidate) {

                        } else {
                            list.add(item);
                        }
                    }
                }


            }
        }
        return list;
    }

    private ArrayList<HouseHoldItem> findSyncedHousehold() {
        ArrayList<HouseHoldItem> list = new ArrayList<>();
        // list=SeccDatabase.getSyncHouseholdList(context,AppConstant.SYNCED_STATUS+"");
        for (HouseHoldItem item : totalHouseHold) {
            if (item.getSyncedStatus() != null && item.getSyncedStatus().trim().equalsIgnoreCase(AppConstant.SYNCED_STATUS)) {
                list.add(item);
            }
        }
        return list;
    }

    private ArrayList<HouseHoldItem> findErrorHousehold() {
        ArrayList<HouseHoldItem> list = new ArrayList<>();
        // ArrayList<HouseHoldItem> readyToSyncList=findReadyToSyncHousehold();

        for (HouseHoldItem item : totalHouseHold) {

            if (item.getError_code() != null) {
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
}
