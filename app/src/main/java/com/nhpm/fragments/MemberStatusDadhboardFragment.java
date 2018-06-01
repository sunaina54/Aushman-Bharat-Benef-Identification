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

import com.customComponent.CustomAsyncTask;
import com.customComponent.TaskListener;
import com.customComponent.utility.ProjectPrefrence;
import com.nhpm.LocalDataBase.dto.SeccDatabase;
import com.nhpm.Models.response.seccMembers.SeccMemberItem;
import com.nhpm.Models.response.verifier.VerifierLocationItem;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;
import com.nhpm.Utility.AppUtility;

import java.util.ArrayList;

import static android.graphics.Color.YELLOW;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MemberStatusDadhboardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MemberStatusDadhboardFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private LinearLayout totalMemberLayout, pendingMemberLayout, lockedLayout, underSurveyedLayout, validatedLayout, yetToSurveyedLayout, syncLayout, underValidationLayout, errorLayout;
    private TextView totalMemberTV, pendingMemberTV, lockedMemberTV, underSurveyedMemberTV, validatedMemberTV, yetToSurveyedTV,
            syncMemberTV, underValidationTV, errorTV;
    public static String status = "1";
    public ArrayList<SeccMemberItem> memberList;
    private MemberStatusFragment memberStatusFragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private VerifierLocationItem locationItem;
    private Context context;
    private ArrayList<SeccMemberItem> selectedList;
    private CustomAsyncTask asyncTask;
    public static String TOTAL_MEMBER = "1";
    public static String UNDER_SURVEYED = "2";
    public static String YET_TO_MEMBER = "3";
    public static String SYNCED = "4";
    public static String UNDER_VALIDATION = "5";
    public static String SYNC_ERROR = "6";
    public static String LOCKED = "7";
    public static String VALIDATED = "8";
    private int YELLOW, WHITE_SHINE;


    public MemberStatusDadhboardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MemberStatusDadhboardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MemberStatusDadhboardFragment newInstance(String param1, String param2) {
        MemberStatusDadhboardFragment fragment = new MemberStatusDadhboardFragment();
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
        View view = inflater.inflate(R.layout.fragment_member_status_dadhboard, container, false);
        setupScreen(view);
        return view;
    }

    private void setupScreen(View view) {
        context = getActivity();
        fragmentManager = getActivity().getSupportFragmentManager();
        locationItem = VerifierLocationItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SELECTED_BLOCK, context));
        YELLOW = AppUtility.getColor(context, R.color.yellow);
        WHITE_SHINE = AppUtility.getColor(context, R.color.dark_grey);
        totalMemberLayout = (LinearLayout) view.findViewById(R.id.totalMemberLayout);
        underSurveyedLayout = (LinearLayout) view.findViewById(R.id.underSurvayedLayout);
        yetToSurveyedLayout = (LinearLayout) view.findViewById(R.id.yetToBeSurveyedLayout);
        //validatedLayout=(LinearLayout)view.findViewById(R.id.validatedMemberLayout);
        syncLayout = (LinearLayout) view.findViewById(R.id.syncMemberLayout);
        // lockedLayout=(LinearLayout)view.findViewById(R.id.lockedMemberLayout);
        validatedLayout = (LinearLayout) view.findViewById(R.id.validatedLayout);
        lockedLayout = (LinearLayout) view.findViewById(R.id.lockedLayout);
        totalMemberTV = (TextView) view.findViewById(R.id.totalMemberTV);
        errorTV = (TextView) view.findViewById(R.id.errorTV);
        underSurveyedMemberTV = (TextView) view.findViewById(R.id.underSurvayedTV);
        yetToSurveyedTV = (TextView) view.findViewById(R.id.yetToBeSurveyedTV);
        validatedMemberTV = (TextView) view.findViewById(R.id.validatedMemberTV);
        syncMemberTV = (TextView) view.findViewById(R.id.syncedMemberTV);
        lockedMemberTV = (TextView) view.findViewById(R.id.lockedTV);
        // underValidationTV=(TextView)view.findViewById(R.id.underValidationTV);
        loadMemberDashboard();

        totalMemberLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                status = TOTAL_MEMBER;
                openMemberStatusFragment(memberList, status);
            }
        });
        underSurveyedLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedList = findUnderSurveyedList();
                status = UNDER_SURVEYED;
                openMemberStatusFragment(selectedList, status);
            }
        });
        yetToSurveyedLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedList = yetToBeSurveyedList();
                status = YET_TO_MEMBER;

                openMemberStatusFragment(selectedList, status);
            }
        });

        syncLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                status = SYNCED;

                selectedList = findSyncedMemberList();
                openMemberStatusFragment(selectedList, status);
            }
        });

        lockedLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                status = LOCKED;
                selectedList = findLockedList();
                openMemberStatusFragment(selectedList, status);
            }
        });
        validatedLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                status = VALIDATED;
                totalMemberLayout.setBackgroundColor(WHITE_SHINE);
                underSurveyedLayout.setBackgroundColor(WHITE_SHINE);
                yetToSurveyedLayout.setBackgroundColor(WHITE_SHINE);
                syncLayout.setBackgroundColor(WHITE_SHINE);
                lockedLayout.setBackgroundColor(WHITE_SHINE);
                validatedLayout.setBackgroundColor(YELLOW);
                selectedList = findValidatedMemberList();
                openMemberStatusFragment(selectedList, status);
            }
        });
    }

    private ArrayList<SeccMemberItem> getAllMemberList() {
        ArrayList<SeccMemberItem> memberItems = new ArrayList<SeccMemberItem>();
        if (locationItem != null) {
            memberItems = SeccDatabase.
                    getSeccMemberList(locationItem.getStateCode(), locationItem.getDistrictCode(),
                            locationItem.getTehsilCode(), locationItem.getVtCode(), locationItem.getWardCode(), locationItem.getBlockCode(), context);
            Log.d("Member Status Fragment", "Member List : " + memberItems.size());
        }
        return memberItems;
    }

    private void openMemberStatusFragment(ArrayList<SeccMemberItem> selectedList, String status) {
        totalMemberLayout.setBackgroundColor(WHITE_SHINE);
        underSurveyedLayout.setBackgroundColor(WHITE_SHINE);
        yetToSurveyedLayout.setBackgroundColor(WHITE_SHINE);
        syncLayout.setBackgroundColor(WHITE_SHINE);
        lockedLayout.setBackgroundColor(WHITE_SHINE);
        validatedLayout.setBackgroundColor(WHITE_SHINE);
        ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.MEMBER_TAB_STATUS, status, context);
        if (status.equalsIgnoreCase(TOTAL_MEMBER)) {
            totalMemberLayout.setBackgroundColor(YELLOW);
        } else if (status.equalsIgnoreCase(UNDER_SURVEYED)) {
            underSurveyedLayout.setBackgroundColor(YELLOW);
        } else if (status.equalsIgnoreCase(YET_TO_MEMBER)) {
            yetToSurveyedLayout.setBackgroundColor(YELLOW);
        } else if (status.equalsIgnoreCase(SYNCED)) {
            syncLayout.setBackgroundColor(YELLOW);
        } else if (status.equalsIgnoreCase(LOCKED)) {
            lockedLayout.setBackgroundColor(YELLOW);
        } else if (status.equalsIgnoreCase(VALIDATED)) {
            validatedLayout.setBackgroundColor(YELLOW);
        }

        fragmentTransaction = fragmentManager.beginTransaction();
        if (memberStatusFragment != null) {
            fragmentTransaction.detach(memberStatusFragment);
            memberStatusFragment = null;
        }
        memberStatusFragment = new MemberStatusFragment();
        memberStatusFragment.setMemberList(selectedList);
        memberStatusFragment.setMemberStatus(status);
        fragmentTransaction.replace(R.id.memberStatFragContainer, memberStatusFragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

    private ArrayList<SeccMemberItem> findErrorMember() {
        ArrayList<SeccMemberItem> errorList = new ArrayList<>();
        for (SeccMemberItem item : memberList) {
            if (item.getErrorItem() != null) {
                errorList.add(item);
            }
        }
        return errorList;
    }

    private ArrayList<SeccMemberItem> findValidatedMemberList() {
        ArrayList<SeccMemberItem> validatedList = new ArrayList<>();
        for (SeccMemberItem item : memberList) {
            if (item != null && item.getLockedSave() != null && item.getLockedSave().equalsIgnoreCase(AppConstant.SAVE + "")) {
               /* if(item.getMemStatus().equalsIgnoreCase(AppConstant.MEMBERFOUND_BUT_NOT_PRESENT )||
                        item.getMemStatus().equalsIgnoreCase(AppConstant.MEMBERFOUND_AND_PRESENT)){*/
                if (item.getAadhaarStatus() != null && item.getAadhaarStatus().equalsIgnoreCase(AppConstant.AADHAAR_STAT) && item.getAadhaarAuth() != null && !item.getAadhaarAuth().equalsIgnoreCase("") && item.getAadhaarAuth().equalsIgnoreCase(AppConstant.VALID_STATUS)) {
                    validatedList.add(item);
                }
                // }
            }
        }
        return validatedList;
    }

    private ArrayList<SeccMemberItem> findUnderValidationList() {
        ArrayList<SeccMemberItem> underValidList = new ArrayList<>();
        for (SeccMemberItem item : memberList) {
            if (item.getLockedSave() != null && item.getLockedSave().equalsIgnoreCase(AppConstant.LOCKED + "") &&
                    item.getAadhaarAuth() != null && item.getAadhaarAuth().equalsIgnoreCase(AppConstant.PENDING_STATUS)) {
                underValidList.add(item);
            }
        }
        return underValidList;
    }

    private ArrayList<SeccMemberItem> findLockedList() {
        ArrayList<SeccMemberItem> list = new ArrayList<>();
        for (SeccMemberItem item : memberList) {
            if (item.getLockedSave() != null && item.getLockedSave().equalsIgnoreCase(AppConstant.LOCKED + "")) {
                if (item.getSyncedStatus() != null && !item.getSyncedStatus().equalsIgnoreCase("")) {

                } else {
                    if (item.getAadhaarStatus() != null && !item.getAadhaarStatus().equalsIgnoreCase("")) {
                        if (item.getMemStatus().equalsIgnoreCase(AppConstant.MEMBERFOUND_AND_PRESENT)
                                || item.getMemStatus().equalsIgnoreCase(AppConstant.MEMBERFOUND_BUT_NOT_PRESENT)) {
                            if (item.getAadhaarStatus().equalsIgnoreCase(AppConstant.AADHAAR_STAT)) {
                                if (item.getAadhaarAuth() != null && item.getAadhaarAuth().equalsIgnoreCase(AppConstant.VALID_STATUS)) {
                                    list.add(item);
                                }
                            } else {
                                list.add(item);
                            }
                        }
                    } else {
                        list.add(item);
                    }

                      /*if (!item.getMemStatus().equalsIgnoreCase(AppConstant.MEMBERFOUND_AND_PRESENT)
                              && !item.getMemStatus().equalsIgnoreCase(AppConstant.MEMBERFOUND_BUT_NOT_PRESENT)) {
                        // if (item.getAadhaarAuth() != null && item.getAadhaarAuth().equalsIgnoreCase(AppConstant.PENDING_STATUS)) {
                            list.add(item);
                        // }
                      }
*/
                }
            }

        }
        return list;
    }

    private ArrayList<SeccMemberItem> findUnderSurveyedList() {
        ArrayList<SeccMemberItem> list = new ArrayList<>();
        for (SeccMemberItem item : memberList) {
            if (item.getLockedSave() != null && item.getLockedSave().equalsIgnoreCase(AppConstant.SAVE + "")) {
                if (item.getLockedSave() != null && item.getLockedSave().equalsIgnoreCase(AppConstant.SAVE + "") && item.getAadhaarAuth() != null && item.getAadhaarAuth().trim().equalsIgnoreCase(AppConstant.PENDING_STATUS)) {
                    list.add(item);
                } else {
                    list.add(item);
                }
            }

        }
        return list;
    }

    private ArrayList<SeccMemberItem> yetToBeSurveyedList() {
        ArrayList<SeccMemberItem> list = new ArrayList<>();
        for (SeccMemberItem item : memberList) {
            if (item.getLockedSave() == null || item.getLockedSave().equalsIgnoreCase("")) {
                list.add(item);
            }
        }
        return list;
    }

    private ArrayList<SeccMemberItem> findSyncedMemberList() {
        ArrayList<SeccMemberItem> list = new ArrayList<>();
        for (SeccMemberItem item : memberList) {
            if (item.getSyncedStatus() != null && item.getSyncedStatus().equalsIgnoreCase(AppConstant.SYNCED_STATUS)) {
                list.add(item);
            }
        }
        return list;
    }

    private void loadMemberDashboard() {
        TaskListener taskListener = new TaskListener() {
            @Override
            public void execute() {
                memberList = getAllMemberList();
            }

            @Override
            public void updateUI() {
                updateStatus();
            }
        };
        if (asyncTask != null && !asyncTask.isCancelled()) {
            asyncTask.cancel(true);
            asyncTask = null;
        }
        asyncTask = new CustomAsyncTask(taskListener, context.getResources().getString(R.string.please_wait), context);
        asyncTask.execute();

    }

    private void updateStatus() {
        String tabStatus = ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.MEMBER_TAB_STATUS, context);
        if (tabStatus != null) {
            if (tabStatus.equalsIgnoreCase(TOTAL_MEMBER)) {
                openMemberStatusFragment(memberList, tabStatus);
            } else if (tabStatus.equalsIgnoreCase(UNDER_SURVEYED)) {
                openMemberStatusFragment(findUnderSurveyedList(), tabStatus);
            } else if (tabStatus.equalsIgnoreCase(YET_TO_MEMBER)) {
                openMemberStatusFragment(yetToBeSurveyedList(), tabStatus);
            } else if (tabStatus.equalsIgnoreCase(SYNCED)) {
                openMemberStatusFragment(findSyncedMemberList(), tabStatus);
            } else if (tabStatus.equalsIgnoreCase(VALIDATED)) {
                openMemberStatusFragment(findValidatedMemberList(), tabStatus);

            } else if (tabStatus.equalsIgnoreCase(LOCKED)) {
                openMemberStatusFragment(findLockedList(), tabStatus);
            }
        } else {
            openMemberStatusFragment(memberList, TOTAL_MEMBER);
        }
        syncMemberTV.setText(findSyncedMemberList().size() + "");
        validatedMemberTV.setText(findValidatedMemberList().size() + "");
        lockedMemberTV.setText(findLockedList().size() + "");
        yetToSurveyedTV.setText(yetToBeSurveyedList().size() + "");
        underSurveyedMemberTV.setText(SeccDatabase.countUnderSurvayedMember(context,
                locationItem.getStateCode(), locationItem.getDistrictCode(),
                locationItem.getTehsilCode(), locationItem.getVtCode(), locationItem.getWardCode(),
                locationItem.getBlockCode(), AppConstant.SAVE + "") + "");
        totalMemberTV.setText(memberList.size() + "");

    }
}
