package com.nhpm.fragments;


import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.customComponent.utility.ProjectPrefrence;
import com.nhpm.activity.SearchActivityWithHouseHold;
import com.nhpm.LocalDataBase.dto.SeccDatabase;
import com.nhpm.Models.response.seccMembers.HouseHoldItem;
import com.nhpm.Models.response.seccMembers.SeccHouseholdResponse;
import com.nhpm.Models.response.verifier.VerifierLocationItem;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;
import com.nhpm.Utility.AppUtility;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HouseholdStatusBoardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HouseholdStatusBoardFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private SeccHouseholdResponse houseHoldResponse;
    private Button pendingHouseholdBT, partialVerifiedBT, verifiedBT;
    private FragmentManager fragMgr;
    private Context context;
    private HouseholdStatusFragment householdStatusFragment;
    /*  private PartialVerifiedHouseholFragment partialVerifiedHouseholFragment;
      private VerifiedHouseHoldFragment verifiedHouseholFragment;*/
    private Fragment fragment;
    private VerifierLocationItem location;
    private RelativeLayout searchFamilyMemberLayout, verifiedMemLayout, partialmemLayout,
            partialRsbyVerifiedMemberLayout, verifiedRsbyMemberLayout;

    private EditText searchFamilyMemberET;
    private ImageView backIV;
    private int YELLOW, WHITE_SHINE;
    private LinearLayout totalhouseholdLayout, visitedHouseholdLayout, pendingHouseholdLayout,
            syncedHouseholdLayout, underSurveyedLayout, surveyedlayout;
    private TextView syncHouseholdTV, underSurveyedHouseholdTV, surveyedHouseholdTV;
    private SearchActivityWithHouseHold activity;
    public static String TOTAL_HOUSEHOLD = "1", PENDING_HOUSEHOLD = "2",
            VISITED_HOUSEHOLD = "3", SYNC_HOUSEHOLD = "4", SURVEYED_HOUSEHOLD = "5", UNDER_SURVEYED_HOUSEHOLD = "6";
    private ArrayList<HouseHoldItem> houseHoldList;
    private ArrayList<HouseHoldItem> selectedHouseholdList;
    private String TAG = "Household Status Frag";
    private String downloadedDataType;


    public HouseholdStatusBoardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HouseholdStatusBoardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HouseholdStatusBoardFragment newInstance(String param1, String param2) {
        HouseholdStatusBoardFragment fragment = new HouseholdStatusBoardFragment();
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
        View view = inflater.inflate(R.layout.fragment_seccdadh_board, container, false);
        setupScreen(view);
        return view;
    }

    private void setupScreen(View view) {
        fragMgr = getActivity().getSupportFragmentManager();
        context = getActivity();
        YELLOW = AppUtility.getColor(context, R.color.yellow);
        WHITE_SHINE = AppUtility.getColor(context, R.color.dark_grey);
        downloadedDataType = ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.DOWNLOADINGSOURCE, context);
        location = VerifierLocationItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SELECTED_BLOCK, context));


        if (downloadedDataType != null && downloadedDataType.equalsIgnoreCase(AppConstant.EbWiseDownloading)) {
            if (location != null) {
                houseHoldList = SeccDatabase.getSeccHouseHoldList(location.getStateCode(), location.getDistrictCode(),
                        location.getTehsilCode(), location.getVtCode(), location.getWardCode(), location.getBlockCode(), context);
            }
        } else if (downloadedDataType != null && downloadedDataType.equalsIgnoreCase(AppConstant.VillageWiseDownloading)) {
            if (location != null) {
                houseHoldList = SeccDatabase.getSeccHouseHoldVillageWiseList(location.getStateCode(), location.getDistrictCode(),
                        location.getTehsilCode(), location.getVtCode()/*,location.getWardCode(),location.getBlockCode()*/, context);
            }
        } else if (downloadedDataType != null && downloadedDataType.equalsIgnoreCase(AppConstant.WardWiseDownloading)) {
            if (location != null) {
                houseHoldList = SeccDatabase.getSeccHouseHoldWardWiseList(location.getStateCode(), location.getDistrictCode(),
                        location.getTehsilCode(), location.getVtCode(),location.getWardCode()/*,location.getBlockCode()*/, context);
            }
        } else if (downloadedDataType != null && downloadedDataType.equalsIgnoreCase(AppConstant.SubBlockWiseDownloading)) {
            if (location != null) {
                houseHoldList = SeccDatabase.getSeccHouseHoldSubEBWiseList(location.getStateCode(), location.getDistrictCode(),
                        location.getTehsilCode(), location.getVtCode(),location.getWardCode(),location.getBlockCode(), location.getSubBlockcode(),context);
            }
        }





        if(houseHoldList!=null) {
            for (HouseHoldItem item : houseHoldList) {
                AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "HouseholdStatus : " + item.getRsbyName() + " Source : " + item.getDataSource());
            }
        }
        // houseHoldList= SeccDatabase.getAllHouseHold(context);

        totalhouseholdLayout = (LinearLayout) view.findViewById(R.id.totalHouseholdLayout);
        visitedHouseholdLayout = (LinearLayout) view.findViewById(R.id.visitedHouseholdLayout);
        pendingHouseholdLayout = (LinearLayout) view.findViewById(R.id.pendingHouseholdLayout);
        syncedHouseholdLayout = (LinearLayout) view.findViewById(R.id.syncHouseholdLayout);
        underSurveyedLayout = (LinearLayout) view.findViewById(R.id.underSurveyHouseholdLayout);
        surveyedlayout = (LinearLayout) view.findViewById(R.id.surveyedLayout);


        syncHouseholdTV = (TextView) view.findViewById(R.id.syncedHouseholdTV);
        syncHouseholdTV.setText(syncedHousehold().size() + "");
        syncHouseholdTV.setPaintFlags(syncHouseholdTV.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        underSurveyedHouseholdTV = (TextView) view.findViewById(R.id.underSurveyHouseholdTV);
        underSurveyedHouseholdTV.setPaintFlags(underSurveyedHouseholdTV.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        underSurveyedHouseholdTV.setText(underSurveyedHousehold().size() + "");

        surveyedHouseholdTV = (TextView) view.findViewById(R.id.surveyedHouseholdTV);
        surveyedHouseholdTV.setPaintFlags(surveyedHouseholdTV.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        surveyedHouseholdTV.setText(surveyedHousehold().size() + "");

       /* TextView totalHouseholdTV = (TextView) view.findViewById(R.id.totalHouseholdTV);
        totalHouseholdTV.setText(SeccDatabase.houseHoldCount(context) + "");
        totalHouseholdTV.setPaintFlags(totalHouseholdTV.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);*/
        int visitedHouseHold = 0;
        int pendingHouseHold = 0;
        /*for (HouseHoldItem item : houseHoldList) {
            if (item.getHhStatus() == null) {
                pendingHouseHold++;
            } else {
                visitedHouseHold++;
            }
        }*/
        final TextView visitedHouseholdTV = (TextView) view.findViewById(R.id.visitedHouseholdTV);
        visitedHouseholdTV.setText(findVisitedHousehold().size() + "");
        visitedHouseholdTV.setPaintFlags(visitedHouseholdTV.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        TextView pendingHouseholdTV = (TextView) view.findViewById(R.id.pendingHouseholdTV);
        pendingHouseholdTV.setPaintFlags(pendingHouseholdTV.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        pendingHouseholdTV.setText(findPendingHousehold().size() + "");
        TextView totalMemberTV = (TextView) view.findViewById(R.id.totalHouseholdTV);
        totalMemberTV.setPaintFlags(totalMemberTV.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        if(houseHoldList!=null) {
            totalMemberTV.setText(houseHoldList.size() + "");
        }
        String tabStatus = ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.HOUSEHOLD_TAB_STATUS, context);

        if (tabStatus != null) {
            if (tabStatus.equalsIgnoreCase(PENDING_HOUSEHOLD))
                openPendingFragment(findPendingHousehold(), PENDING_HOUSEHOLD);
            if (tabStatus.equalsIgnoreCase(VISITED_HOUSEHOLD))
                openPendingFragment(findVisitedHousehold(), VISITED_HOUSEHOLD);
            if (tabStatus.equalsIgnoreCase(TOTAL_HOUSEHOLD))
                openPendingFragment(houseHoldList, TOTAL_HOUSEHOLD);
            if (tabStatus.equalsIgnoreCase(SYNC_HOUSEHOLD))
                openPendingFragment(syncedHousehold(), SYNC_HOUSEHOLD);
            if (tabStatus.equalsIgnoreCase(UNDER_SURVEYED_HOUSEHOLD))
                openPendingFragment(underSurveyedHousehold(), UNDER_SURVEYED_HOUSEHOLD);
            if (tabStatus.equalsIgnoreCase(SURVEYED_HOUSEHOLD))
                openPendingFragment(surveyedHousehold(), SURVEYED_HOUSEHOLD);
        } else {
            openPendingFragment(houseHoldList, TOTAL_HOUSEHOLD);

        }
        //openPendingFragment(findPendingHousehold(),PENDING_HOUSEHOLD);
        totalhouseholdLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPendingFragment(houseHoldList, TOTAL_HOUSEHOLD);

            }
        });
        visitedHouseholdLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedHouseholdList = findVisitedHousehold();
                openPendingFragment(selectedHouseholdList, VISITED_HOUSEHOLD);
            }
        });

        pendingHouseholdLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectedHouseholdList = findPendingHousehold();
                openPendingFragment(selectedHouseholdList, PENDING_HOUSEHOLD);
            }
        });
        syncedHouseholdLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectedHouseholdList = syncedHousehold();
                openPendingFragment(selectedHouseholdList, SYNC_HOUSEHOLD);
            }
        });
        underSurveyedLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectedHouseholdList = underSurveyedHousehold();
                openPendingFragment(selectedHouseholdList, UNDER_SURVEYED_HOUSEHOLD);
            }
        });
        surveyedlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectedHouseholdList = surveyedHousehold();
                openPendingFragment(selectedHouseholdList, SURVEYED_HOUSEHOLD);
            }
        });

    }

    private void openPendingFragment(ArrayList<HouseHoldItem> list, String status) {
//        verifiedBT.setTextColor(WHITE_SHINE);
        totalhouseholdLayout.setBackgroundColor(WHITE_SHINE);
        pendingHouseholdLayout.setBackgroundColor(WHITE_SHINE);
        visitedHouseholdLayout.setBackgroundColor(WHITE_SHINE);
        syncedHouseholdLayout.setBackgroundColor(WHITE_SHINE);
        underSurveyedLayout.setBackgroundColor(WHITE_SHINE);
        surveyedlayout.setBackgroundColor(WHITE_SHINE);
        ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.HOUSEHOLD_TAB_STATUS, status, context);
        if (status != null && status.equalsIgnoreCase(TOTAL_HOUSEHOLD)) {
            totalhouseholdLayout.setBackgroundColor(YELLOW);
           /* ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                    AppConstant.HOUSEHOLD_TAB_STATUS,TOTAL_HOUSEHOLD,context);*/

        }
        if (status != null && status.equalsIgnoreCase(PENDING_HOUSEHOLD)) {
            pendingHouseholdLayout.setBackgroundColor(YELLOW);
           /* ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                    AppConstant.HOUSEHOLD_TAB_STATUS,PENDING_HOUSEHOLD,context);*/
        }
        if (status != null && status.equalsIgnoreCase(VISITED_HOUSEHOLD)) {
            visitedHouseholdLayout.setBackgroundColor(YELLOW);
            /*ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                    AppConstant.HOUSEHOLD_TAB_STATUS,VISITED_HOUSEHOLD,context);*/
        }
        if (status != null && status.equalsIgnoreCase(SYNC_HOUSEHOLD)) {
            syncedHouseholdLayout.setBackgroundColor(YELLOW);
            /*ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                    AppConstant.HOUSEHOLD_TAB_STATUS,VISITED_HOUSEHOLD,context);*/
        }
        if (status != null && status.equalsIgnoreCase(UNDER_SURVEYED_HOUSEHOLD)) {
            underSurveyedLayout.setBackgroundColor(YELLOW);
            /*ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                    AppConstant.HOUSEHOLD_TAB_STATUS,VISITED_HOUSEHOLD,context);*/
        }
        if (status != null && status.equalsIgnoreCase(SURVEYED_HOUSEHOLD)) {
            surveyedlayout.setBackgroundColor(YELLOW);
            /*ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                    AppConstant.HOUSEHOLD_TAB_STATUS,VISITED_HOUSEHOLD,context);*/
        }
        FragmentTransaction transaction = fragMgr.beginTransaction();
        if (householdStatusFragment != null) {
            transaction.detach(householdStatusFragment);
            householdStatusFragment = null;
            fragment = null;
        }
        householdStatusFragment = new HouseholdStatusFragment();
        householdStatusFragment.setPendingHouseHoldList(list);
        householdStatusFragment.setStatus(status);
        fragment = householdStatusFragment;
        transaction.replace(R.id.seccDashboardContainer, fragment);
        transaction.commitAllowingStateLoss();
    }

   /* private void getSeccHouseholdList() {
        houseHoldList = new ArrayList<>();
        houseHoldList = SeccDatabase.getHouseHoldList(location.getStateCode(), location.getDistrictCode(),
                location.getTehsilCode(), location.getVtCode(), location.getWardCode(), location.getBlockCode(), context);

        *//*for(HouseHoldItem item : houseHoldList){
            if(item.getHhStatus()==null){
                pendingHouseHoldList.add(item);
            }
            if(item.getHhStatus()!=null && item.getHhStatus().equalsIgnoreCase("")){
                pendingHouseHoldList.add(item);
            }
        }*//*


    }*/

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (SearchActivityWithHouseHold) context;
        fragMgr = activity.fragMgr;
    }

    private ArrayList<HouseHoldItem> findVisitedHousehold() {
        ArrayList<HouseHoldItem> list = new ArrayList<>();
        if(houseHoldList!=null) {
            for (HouseHoldItem item : houseHoldList) {

                if (item.getHhStatus() != null && !item.getHhStatus().equalsIgnoreCase("")) {
                    Log.d("Secc Dashboard", " visited List size : " + item.getHhStatus());
                    list.add(item);
                }
            }
        }
        Log.d("Secc Dashboard", " visited List size : " + list.size());
        return list;
    }

    private ArrayList<HouseHoldItem> findPendingHousehold() {
        ArrayList<HouseHoldItem> list = new ArrayList<>();
        if(houseHoldList!=null){
        for (HouseHoldItem item : houseHoldList) {
            if (item.getHhStatus() != null && item.getHhStatus().equalsIgnoreCase("")) {
                list.add(item);
            }
            if (item.getHhStatus() == null) {
                list.add(item);
            }

        }}
        return list;
    }

    private ArrayList<HouseHoldItem> syncedHousehold() {
        ArrayList<HouseHoldItem> list = new ArrayList<>();
        if(houseHoldList!=null){
        for (HouseHoldItem item : houseHoldList) {
            if (item.getHhStatus() != null && !item.getHhStatus().equalsIgnoreCase("") && item.getSyncedStatus() != null) {
                list.add(item);
            }
        }}
        return list;
    }

    private ArrayList<HouseHoldItem> underSurveyedHousehold() {
        ArrayList<HouseHoldItem> list = new ArrayList<>();
        if(houseHoldList!=null) {
            for (HouseHoldItem item : houseHoldList) {
                if (item.getHhStatus() != null && !item.getHhStatus().equalsIgnoreCase("")
                        && item.getLockSave() != null && item.getLockSave().trim().equalsIgnoreCase(AppConstant.SAVE + "")) {
                    list.add(item);
                }
            }
        }
        return list;
    }

    private ArrayList<HouseHoldItem> surveyedHousehold() {
        ArrayList<HouseHoldItem> list = new ArrayList<>();
        if(houseHoldList!=null){
        for (HouseHoldItem item : houseHoldList) {
            if (item.getHhStatus() != null && !item.getHhStatus().equalsIgnoreCase("")
                    && item.getLockSave() != null && item.getLockSave().equalsIgnoreCase(AppConstant.LOCKED + "")) {
                if (item.getSyncedStatus() != null && !item.getSyncedStatus().equalsIgnoreCase("")) {

                } else {
                    list.add(item);
                }
            }
        }}
        return list;
    }

}

