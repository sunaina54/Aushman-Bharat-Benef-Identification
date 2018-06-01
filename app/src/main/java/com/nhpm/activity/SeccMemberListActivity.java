package com.nhpm.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.customComponent.CustomAlert;
import com.customComponent.utility.ProjectPrefrence;
import com.nhpm.LocalDataBase.dto.SeccDatabase;
import com.nhpm.Models.response.master.ConfigurationItem;
import com.nhpm.Models.response.master.MemberStatusItem;
import com.nhpm.Models.response.master.StateItem;
import com.nhpm.Models.response.seccMembers.HouseHoldItem;
import com.nhpm.Models.response.seccMembers.SeccMemberItem;
import com.nhpm.Models.response.seccMembers.SelectedMemberItem;
import com.nhpm.Models.response.verifier.FamilyStatusItem;
import com.nhpm.Models.response.verifier.VerifierLoginResponse;
import com.nhpm.PrintCard.PrintCardMainActivity;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;
import com.nhpm.Utility.AppUtility;
import com.nhpm.fragments.ChooseNewHeadFragment;
import com.nhpm.fragments.DefaultFamilyListFragment;
import com.nhpm.fragments.OldHeadFragment;

import java.util.ArrayList;

import pl.polidea.view.ZoomView;

public class SeccMemberListActivity extends BaseActivity implements ComponentCallbacks2 {
    public Spinner familyStatusSP, hofStatusSP;
    private LinearLayout headMemberStatusLayout;
    private ArrayList<FamilyStatusItem> familyStatusList;
    private ArrayList<MemberStatusItem> memberStatusList;
    private ArrayList<SeccMemberItem> seccMemberList;
    private Context context;
    private String printStatus = "Y";
    private boolean memberFound = false;
    private boolean houseHoldEligible = false;
    private FragmentManager fargManager;
    private ChooseNewHeadFragment newHeadFragment;
    private OldHeadFragment oldHeadFragment;
    private DefaultFamilyListFragment defaultFragment;
    private ArrayList<SeccMemberItem> houseHoldMemberList;
    private SelectedMemberItem selectedMemberItem;
    private HouseHoldItem houseHoldItem;
    private ImageView back;
    public FamilyStatusItem householdStatus;
    private SeccMemberItem head = null, newHead;
    private String TAG = "Secc Member List";
    public SeccMemberItem oldHeadItem, newHeadItem;
    public MemberStatusItem hofStatusItem;
    public static int CHOOSE_NEW_HEAD_STATUS = 1;
    public Button chooseHeadBT, printPreview;
    public RelativeLayout chooseHeadLayout;
    private TextView householdAddressTV;
    private LinearLayout mZoomLinearLayout;
    private ZoomView zoomView;
    //  private TextView dashBoardNavBT;
    public static String RESET = "reset", EDIT = "edit";
    private boolean pinLockIsShown = false;
    private String zoomMode = "N";
    private boolean isHofFound;

    private int wrongPinCount = 0;
    private long wrongPinSavedTime;
    private long currentTime;
    private TextView wrongAttempetCountValue, wrongAttempetCountText;
    private long millisecond24 = 86400000;
    public static boolean isFlagNoNewHead = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        checkAppConfig();

        if (zoomMode.equalsIgnoreCase("N")) {
            setContentView(R.layout.activity_secc_membermember_list);
            setupScreenWithOutZoom();

        } else {
            setContentView(R.layout.dummy_layout_for_zooming);

            setupScreenWithZoom();
        }
    }

    private void setupScreenWithZoom() {
        mZoomLinearLayout = (LinearLayout) findViewById(R.id.mZoomLinearLayout);
        View v = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.activity_secc_membermember_list, null, false);
        v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
        showNotification(v);
        selectedMemberItem = SelectedMemberItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.SELECTED_ITEM_FOR_VERIFICATION, context));
        if (selectedMemberItem.getHouseHoldItem() != null) {
            houseHoldItem = selectedMemberItem.getHouseHoldItem();
        }
        if (houseHoldItem != null && houseHoldItem.getDataSource() != null && houseHoldItem.getDataSource().trim().equalsIgnoreCase(AppConstant.RSBY_SOURCE)) {
            findRsbyHouseholdmemberList();
            findRsbyHead();
        } else {
            findHouseholdMemberList();
            findHead();
        }


        fargManager = getSupportFragmentManager();
        familyStatusSP = (Spinner) v.findViewById(R.id.familyStatusSP);
        hofStatusSP = (Spinner) v.findViewById(R.id.headMemberStatusSP);
        //   dashBoardNavBT = (TextView) v.findViewById(R.id.dashBoardNavBT);
        householdAddressTV = (TextView) v.findViewById(R.id.householdAddressTV);
        back = (ImageView) v.findViewById(R.id.back);
        back.setVisibility(View.VISIBLE);
        headMemberStatusLayout = (LinearLayout) v.findViewById(R.id.headMemberStatusLayout);
        chooseHeadBT = (Button) v.findViewById(R.id.chooseHeadBT);
        printPreview = (Button) v.findViewById(R.id.printPreview);
        chooseHeadLayout = (RelativeLayout) v.findViewById(R.id.chooseHeadLayout);
        zoomView = new ZoomView(this);
        zoomView.addView(v);
        mZoomLinearLayout.addView(zoomView);
        if (houseHoldItem != null && houseHoldItem.getSyncedStatus() != null && houseHoldItem.getSyncDt() != null) {

            findAligableMember();
            hofStatusSP.setEnabled(false);
            familyStatusSP.setEnabled(false);
        }
        chooseHeadBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent theIntent = new Intent(context, ChooseNewHeadActivity.class);
                startActivityForResult(theIntent, CHOOSE_NEW_HEAD_STATUS);
                selectedMemberItem.setHouseHoldItem(houseHoldItem);
                ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SELECTED_ITEM_FOR_VERIFICATION, selectedMemberItem.serialize(), context);
            }
        });

        searchMenuAppcongigBoth(v);
        familyStatusSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                householdStatus = familyStatusList.get(i);
                headMemberStatusLayout.setVisibility(View.GONE);
//                isFlagNoNewHead = false;
                if (householdStatus.getStatusCode().equalsIgnoreCase(AppConstant.DEFAULT_HOUSEHOLD)) {
                    showDefaultFamilyList();
                } else if (householdStatus.getStatusCode().equalsIgnoreCase(AppConstant.HOUSEHOLD_FOUND)) {
                    headMemberStatusLayout.setVisibility(View.VISIBLE);
                    showDefaultFamilyList();
                } else {
                    showDefaultFamilyList();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        hofStatusSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                hofStatusItem = memberStatusList.get(i);
                chooseHeadLayout.setVisibility(View.GONE);
                isFlagNoNewHead = false;
                if (hofStatusItem.getStatusCode().equalsIgnoreCase(AppConstant.DEFAULT_MEMBER_STATUS)) {
                    showDefaultFamilyList();
                } else if (hofStatusItem.getStatusCode().equalsIgnoreCase(AppConstant.MEMBERFOUND_BUT_NOT_PRESENT)) {
                    showOldHeadFamilyList();
                    chooseHeadLayout.setVisibility(View.GONE);
                } else if (hofStatusItem.getStatusCode().equalsIgnoreCase(AppConstant.MEMBERFOUND_AND_PRESENT)) {
                    chooseHeadLayout.setVisibility(View.GONE);
                    showOldHeadFamilyList();
                } else {
                    showNewHeadList();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent theIntent = new Intent(context, SearchActivityWithHouseHold.class);
                startActivity(theIntent);
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        prepareFamilyStatusSpinner();
        prepareMemberStatusSpinner();
        String str = houseHoldItem.serialize();
        System.out.print(str);
        if (houseHoldItem != null && houseHoldItem.getDataSource() != null && houseHoldItem.getDataSource().trim().equalsIgnoreCase(AppConstant.RSBY_SOURCE)) {
            setRsbyHouseholdAddress();
        } else {
            setHouseHoldAddress();
        }
        //  showNewHeadList();
    }

    private void setupScreenWithOutZoom() {
        showNotification();
        selectedMemberItem = SelectedMemberItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.SELECTED_ITEM_FOR_VERIFICATION, context));
        if (selectedMemberItem.getHouseHoldItem() != null) {
            houseHoldItem = selectedMemberItem.getHouseHoldItem();
        }
        if (houseHoldItem != null && houseHoldItem.getDataSource() != null && houseHoldItem.getDataSource().trim().equalsIgnoreCase(AppConstant.RSBY_SOURCE)) {
            findRsbyHouseholdmemberList();
            findRsbyHead();
        } else {
            findHouseholdMemberList();
            findHead();
        }


        fargManager = getSupportFragmentManager();
        familyStatusSP = (Spinner) findViewById(R.id.familyStatusSP);
        hofStatusSP = (Spinner) findViewById(R.id.headMemberStatusSP);
        //   dashBoardNavBT = (TextView) findViewById(R.id.dashBoardNavBT);
        householdAddressTV = (TextView) findViewById(R.id.householdAddressTV);
        back = (ImageView) findViewById(R.id.back);
        back.setVisibility(View.VISIBLE);
        headMemberStatusLayout = (LinearLayout) findViewById(R.id.headMemberStatusLayout);
        chooseHeadBT = (Button) findViewById(R.id.chooseHeadBT);
        printPreview = (Button) findViewById(R.id.printPreview);
        chooseHeadLayout = (RelativeLayout) findViewById(R.id.chooseHeadLayout);

        if (houseHoldItem != null && houseHoldItem.getSyncedStatus() != null && houseHoldItem.getSyncDt() != null) {
            findAligableMember();
            hofStatusSP.setEnabled(false);
            familyStatusSP.setEnabled(false);

        }
        chooseHeadBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent theIntent = new Intent(context, ChooseNewHeadActivity.class);
                startActivityForResult(theIntent, CHOOSE_NEW_HEAD_STATUS);
                selectedMemberItem.setHouseHoldItem(houseHoldItem);
                ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SELECTED_ITEM_FOR_VERIFICATION, selectedMemberItem.serialize(), context);
            }
        });

        searchMenuAppcongigBoth();
        familyStatusSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                householdStatus = familyStatusList.get(i);
                headMemberStatusLayout.setVisibility(View.GONE);
//                isFlagNoNewHead = false;

                if (householdStatus.getStatusCode().equalsIgnoreCase(AppConstant.DEFAULT_HOUSEHOLD)) {
                    showDefaultFamilyList();
                } else if (householdStatus.getStatusCode().equalsIgnoreCase(AppConstant.HOUSEHOLD_FOUND)) {
                    headMemberStatusLayout.setVisibility(View.VISIBLE);
                    showDefaultFamilyList();
                } else {
                    showDefaultFamilyList();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        hofStatusSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                hofStatusItem = memberStatusList.get(i);
                chooseHeadLayout.setVisibility(View.GONE);
                isFlagNoNewHead = false;
                if (hofStatusItem.getStatusCode().equalsIgnoreCase(AppConstant.DEFAULT_MEMBER_STATUS)) {
                    showDefaultFamilyList();
                } else if (hofStatusItem.getStatusCode().equalsIgnoreCase(AppConstant.MEMBERFOUND_BUT_NOT_PRESENT)) {
                    showOldHeadFamilyList();
                    chooseHeadLayout.setVisibility(View.GONE);
                } else if (hofStatusItem.getStatusCode().equalsIgnoreCase(AppConstant.MEMBERFOUND_AND_PRESENT)) {
                    chooseHeadLayout.setVisibility(View.GONE);
                    showOldHeadFamilyList();
                } else {
                    showNewHeadList();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent theIntent = new Intent(context, SearchActivityWithHouseHold.class);
                startActivity(theIntent);
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        prepareFamilyStatusSpinner();
        prepareMemberStatusSpinner();
        String str = houseHoldItem.serialize();
        System.out.print(str);
        if (houseHoldItem != null && houseHoldItem.getDataSource() != null && houseHoldItem.getDataSource().trim().equalsIgnoreCase(AppConstant.RSBY_SOURCE)) {
            setRsbyHouseholdAddress();
        } else {
            setHouseHoldAddress();
        }
        //  showNewHeadList();
    }


    private void prepareMemberStatusSpinner() {
        memberStatusList = new ArrayList<>();
        ArrayList<MemberStatusItem> memberStatusList1 = SeccDatabase.getMemberStatusList(context);
        memberStatusList.add(0, new MemberStatusItem("M", "0", "Select HoF Status", null, "Y"));
        if (houseHoldItem != null && houseHoldItem.getDataSource() != null &&
                houseHoldItem.getDataSource().trim().equalsIgnoreCase(AppConstant.RSBY_SOURCE)) {
            for (MemberStatusItem item : memberStatusList1) {
                String str = item.getStatusCode() + " : " + item.getStatusDesc();
                System.out.print(str);
                if (houseHoldItem.getRsbyName() != null && !houseHoldItem.getRsbyName().equalsIgnoreCase("")) {
                    if (item.getStatusCode().equalsIgnoreCase(AppConstant.MEMBER_ENROL_THROUGH_RSBY) ||
                            item.getStatusCode().equalsIgnoreCase("14")) {

                    } else {
                        memberStatusList.add(item);
                    }
                } else {
                    if (item.getStatusCode().equalsIgnoreCase(AppConstant.NO_INFO_AVAIL)) {
                        memberStatusList.add(item);
                    }
                }
            }
        } else {
            for (MemberStatusItem item : memberStatusList1) {
                if (houseHoldItem.getName() != null && !houseHoldItem.getName().equalsIgnoreCase("")) {
                    if (item.getStatusCode().equalsIgnoreCase(AppConstant.MEMBER_ENROL_THROUGH_RSBY)) {

                    } else {
                        memberStatusList.add(item);
                    }
                } else {
                    if (item.getStatusCode().equalsIgnoreCase(AppConstant.NO_INFO_AVAIL)) {
                        memberStatusList.add(item);
                    }
                }
            }
        }
        ArrayList<String> spinnerList = new ArrayList<>();
        for (MemberStatusItem item : memberStatusList) {
            spinnerList.add(item.getStatusDesc());
        }
        ArrayAdapter<String> maritalAdapter = new ArrayAdapter<>(context, R.layout.custom_drop_down, R.id.textView, spinnerList);
        hofStatusSP.setAdapter(maritalAdapter);
        int selectedHeadStat = 0;
        if (memberStatusList != null) {
            for (int i = 0; i < memberStatusList.size(); i++) {
                if (head != null && head.getMemStatus() != null && !head.getMemStatus().equalsIgnoreCase("")) {
                    if (head.getMemStatus() != null && head.getMemStatus() != null && !head.
                            getMemStatus().equalsIgnoreCase("") && head.getMemStatus().trim().
                            equalsIgnoreCase(memberStatusList.get(i).getStatusCode())) {
                        selectedHeadStat = i;
                        break;
                    }
                }
            }
        }
        hofStatusSP.setSelection(selectedHeadStat);
    }

    private void prepareFamilyStatusSpinner() {
        boolean flag = false;
        int count = 0;
        familyStatusList = new ArrayList<>();
        ArrayList<String> spinnerList = new ArrayList<>();
        ArrayList<FamilyStatusItem> tempItem = SeccDatabase.getFamilyStatusList(context);
        familyStatusList.add(0, new FamilyStatusItem("H", "0", "Select Household Status", ""));
        if (houseHoldItem != null && houseHoldItem.getDataSource() != null && houseHoldItem.getDataSource().trim().equalsIgnoreCase(AppConstant.RSBY_SOURCE)) {
            for (SeccMemberItem item : houseHoldMemberList) {
                if (item.getRsbyName() == null || item.getRsbyName().equalsIgnoreCase("")) {
                    count = count + 1;
                }
            }
            if (count == houseHoldMemberList.size()) {
                for (FamilyStatusItem item : tempItem) {
                    if (item.getStatusCode().equalsIgnoreCase(AppConstant.NO_FAMILY_LIVING)) {
                        // spinnerList.add(item.statusDesc);
                        familyStatusList.add(item);
                    }
                }
            } else {
                for (FamilyStatusItem item : tempItem) {
                    // if(item.getStatusCode().equalsIgnoreCase(AppConstant.NO_FAMILY_LIVING)) {
                    // spinnerList.add(item.statusDesc);
                    String str = item.getStatusCode() + " : " + item.getStatusDesc();
                    System.out.print(str);
                    if (item.getStatusCode() != null && item.getStatusCode().trim().equalsIgnoreCase(AppConstant.HOUSEHOLD_TO_ENROLL_RSBY)) {
                    } else {
                        familyStatusList.add(item);
                    }
                    //  }
                }
            }
        } else {
            for (SeccMemberItem item : houseHoldMemberList) {
                if (item.getName() == null || item.getName().equalsIgnoreCase("")) {
                    count = count + 1;
                }
            }
            if (count == houseHoldMemberList.size()) {
                for (FamilyStatusItem item : tempItem) {
                    if (item.getStatusCode().equalsIgnoreCase(AppConstant.NO_FAMILY_LIVING)) {
                        // spinnerList.add(item.statusDesc);
                        familyStatusList.add(item);
                    }
                }
            } else {
                for (FamilyStatusItem item : tempItem) {
                    // if(item.getStatusCode().equalsIgnoreCase(AppConstant.NO_FAMILY_LIVING)) {
                    // spinnerList.add(item.statusDesc);
                    familyStatusList.add(item);
                    //  }
                }
            }
        }
        for (FamilyStatusItem item : familyStatusList) {
            spinnerList.add(item.statusDesc);
        }
        ArrayAdapter<String> maritalAdapter = new ArrayAdapter<>(context, R.layout.custom_drop_down, R.id.textView, spinnerList);
        familyStatusSP.setAdapter(maritalAdapter);
        //familyStatusSP.set
        int selectedHouseholdStat = 0;
        if (houseHoldItem != null && houseHoldItem.getDataSource() != null && houseHoldItem.getDataSource().trim().equalsIgnoreCase(AppConstant.RSBY_SOURCE)) {
            selectedHouseholdStat = 1;
            familyStatusSP.setSelection(selectedHouseholdStat);
            familyStatusSP.setEnabled(false);
        }
        if (familyStatusList != null) {
            for (int i = 0; i < familyStatusList.size(); i++) {
                if (houseHoldItem != null && houseHoldItem.getHhStatus() != null && !houseHoldItem.getHhStatus().equalsIgnoreCase("")) {
                    if (familyStatusList.get(i).getStatusCode().equalsIgnoreCase(houseHoldItem.getHhStatus().trim())) {
                        selectedHouseholdStat = i;
                        break;
                    }
                }
            }
        }
        familyStatusSP.setSelection(selectedHouseholdStat);


    }

    private void showNewHeadList() {
        chooseHeadLayout.setVisibility(View.VISIBLE);
        FragmentTransaction transect = fargManager.beginTransaction();
        if (newHeadFragment != null) {
            transect.detach(newHeadFragment);
            newHeadFragment = null;
        }
        if (houseHoldItem != null && houseHoldItem.getDataSource() != null && houseHoldItem.getDataSource().trim().equalsIgnoreCase(AppConstant.RSBY_SOURCE)) {
            findRsbyMemberListWithNewHead();
        } else {
            findMemberListWithNewHead();
        }

        if (newHeadItem == null) {
            // houseHoldMemberList=findDefaultMemberList();
            if (houseHoldItem != null && houseHoldItem.getDataSource() != null && houseHoldItem.getDataSource().trim().equalsIgnoreCase(AppConstant.RSBY_SOURCE)) {
                houseHoldMemberList = findDefaultRsbyMemberList();
            } else {
                houseHoldMemberList = findDefaultMemberList();
            }
        } else {
            if (houseHoldItem != null && houseHoldItem.getDataSource() != null && houseHoldItem.getDataSource().trim().equalsIgnoreCase(AppConstant.RSBY_SOURCE)) {
                houseHoldMemberList = findRsbyMemberListWithNewHead();
            } else {
                houseHoldMemberList = findMemberListWithNewHead();
            }
        }
        newHeadFragment = new ChooseNewHeadFragment();
        newHeadFragment.setHouseHoldMemberList(houseHoldMemberList);
        transect.replace(R.id.famlistContainer, newHeadFragment);
        transect.commitAllowingStateLoss();
    }

    private void findHouseholdMemberList() {
        if (houseHoldItem != null && houseHoldItem.getHhdNo() != null) {
            houseHoldMemberList = SeccDatabase.getSeccMemberList(houseHoldItem.getHhdNo(), context);
        }
    }

    private void findRsbyHouseholdmemberList() {
        houseHoldMemberList = SeccDatabase.getRsbyMemberListWithUrn(houseHoldItem.getRsbyUrnId(), context);
        for (SeccMemberItem item : houseHoldMemberList) {
            String str = item.serialize();
            System.out.print(str);
        }
    }

    private void findHead() {

        for (SeccMemberItem item : houseHoldMemberList) {
            if (item.getNhpsMemId() != null && item.getNhpsMemId().equalsIgnoreCase(houseHoldItem.getNhpsMemId())) {
                head = item;
                break;
            }
        }
        for (SeccMemberItem item1 : houseHoldMemberList) {
            if (item1.getNhpsMemId() != null && item1.getNhpsMemId().trim().equalsIgnoreCase(AppConstant.NEW_HEAD_RELATION_CODE)) {
                if (!item1.getNhpsMemId().equalsIgnoreCase(houseHoldItem.getNhpsMemId().trim())) {
                    newHead = item1;
                    break;
                }
            }
        }


    }

    private void findRsbyHead() {

        for (SeccMemberItem item : houseHoldMemberList) {
            if (item.getRsbyMemId() != null && item.getRsbyMemId().equalsIgnoreCase(houseHoldItem.getRsbyMemId())) {
                head = item;
                break;
            }
        }
        for (SeccMemberItem item1 : houseHoldMemberList) {
            if (item1.getRsbyMemId() != null && item1.getRsbyMemId().trim().equalsIgnoreCase(AppConstant.NEW_HEAD_RELATION_CODE)) {
                if (!item1.getRsbyMemId().equalsIgnoreCase(houseHoldItem.getRsbyMemId().trim())) {
                    newHead = item1;
                    break;
                }
            }
        }


    }

    private ArrayList<SeccMemberItem> findRsbyMemberListWithNewHead() {
        ArrayList<SeccMemberItem> list = new ArrayList<>();
        SeccMemberItem oldHead = null;
        for (SeccMemberItem item : houseHoldMemberList) {
            if (item.getNhpsRelationCode() != null && item.getNhpsRelationCode().equalsIgnoreCase(AppConstant.NEW_HEAD_RELATION_CODE)) {
                if (item.getRsbyMemId() != null && !item.getRsbyMemId().equalsIgnoreCase(houseHoldItem.getRsbyMemId())) {
                    newHeadItem = item;
                    break;
                }
            }
        }

        for (SeccMemberItem item : houseHoldMemberList) {
            if (item.getRsbyMemId() != null && item.getRsbyMemId().equalsIgnoreCase(houseHoldItem.getRsbyMemId())) {
                oldHeadItem = item;
                break;
            }
        }
        if (newHeadItem != null) {
            for (SeccMemberItem item : houseHoldMemberList) {
                if (newHeadItem.getRsbyMemId() != null && newHeadItem.getRsbyMemId().equalsIgnoreCase(item.getRsbyMemId())) {

                } else if (oldHeadItem != null && oldHeadItem.getRsbyMemId().equalsIgnoreCase(item.getRsbyMemId())) {

                } else {
                    list.add(item);
                }
            }
            list.add(0, newHeadItem);
            list.add(list.size(), oldHeadItem);
        }
        return list;
    }

    private ArrayList<SeccMemberItem> findRsbyMemberListWithOldHead() {
        ArrayList<SeccMemberItem> list = new ArrayList<>();
        SeccMemberItem newHead = null, oldHead = null;
        for (SeccMemberItem item : houseHoldMemberList) {
            if (item.getRsbyMemId() != null && item.getRsbyMemId().equalsIgnoreCase(houseHoldItem.getRsbyMemId())) {
                oldHead = item;
                break;
            }
        }
        if (oldHead != null) {
            for (SeccMemberItem item : houseHoldMemberList) {
                if (oldHead != null && oldHead.getRsbyMemId().equalsIgnoreCase(item.getRsbyMemId())) {

                } else {
                    list.add(item);
                }
            }
            list.add(0, oldHead);
        }
        return list;
    }

    private ArrayList<SeccMemberItem> findDefaultRsbyMemberList() {
        ArrayList<SeccMemberItem> list = new ArrayList<>();
        SeccMemberItem newHead = null, oldHead = null;
        for (SeccMemberItem item : houseHoldMemberList) {
            if (item.getRsbyMemId() != null && item.getRsbyMemId().equalsIgnoreCase(houseHoldItem.getRsbyMemId())) {
                oldHead = item;
                break;
            }
        }
        if (oldHead != null) {
            for (SeccMemberItem item : houseHoldMemberList) {
                if (oldHead != null && oldHead.getRsbyMemId().equalsIgnoreCase(item.getRsbyMemId())) {

                } else {
                    list.add(item);
                }
            }
            list.add(0, oldHead);
        }
        return list;
    }

    private ArrayList<SeccMemberItem> findMemberListWithNewHead() {
        ArrayList<SeccMemberItem> list = new ArrayList<>();
        SeccMemberItem oldHead = null;
        for (SeccMemberItem item : houseHoldMemberList) {
            if (item.getNhpsRelationCode() != null && item.getNhpsRelationCode().equalsIgnoreCase(AppConstant.NEW_HEAD_RELATION_CODE)) {
                if (item.getNhpsMemId() != null && !item.getNhpsMemId().equalsIgnoreCase(houseHoldItem.getNhpsMemId())) {
                    newHeadItem = item;
                    break;
                }
            }
        }

        for (SeccMemberItem item : houseHoldMemberList) {
            if (item.getNhpsMemId() != null && item.getNhpsMemId().equalsIgnoreCase(houseHoldItem.getNhpsMemId())) {
                oldHeadItem = item;
                break;
            }
        }
        if (newHeadItem != null) {
            for (SeccMemberItem item : houseHoldMemberList) {
                if (newHeadItem.getNhpsMemId() != null && newHeadItem.getNhpsMemId().equalsIgnoreCase(item.getNhpsMemId())) {

                } else if (oldHeadItem != null && oldHeadItem.getNhpsMemId().equalsIgnoreCase(item.getNhpsMemId())) {

                } else {
                    list.add(item);
                }
            }
            list.add(0, newHeadItem);
            list.add(list.size(), oldHeadItem);
        }
        return list;
    }

    private ArrayList<SeccMemberItem> findMemberListWithOldHead() {
        ArrayList<SeccMemberItem> list = new ArrayList<>();
        SeccMemberItem newHead = null, oldHead = null;
        for (SeccMemberItem item : houseHoldMemberList) {
            if (item.getNhpsMemId() != null && item.getNhpsMemId().equalsIgnoreCase(houseHoldItem.getNhpsMemId())) {
                oldHead = item;
                break;
            }
        }
        if (oldHead != null) {
            for (SeccMemberItem item : houseHoldMemberList) {
                if (oldHead != null && oldHead.getNhpsMemId().equalsIgnoreCase(item.getNhpsMemId())) {

                } else {
                    list.add(item);
                }
            }
            list.add(0, oldHead);
        }
        return list;
    }

    private ArrayList<SeccMemberItem> findDefaultMemberList() {
        ArrayList<SeccMemberItem> list = new ArrayList<>();
        SeccMemberItem newHead = null, oldHead = null;
        for (SeccMemberItem item : houseHoldMemberList) {
            if (item.getNhpsMemId() != null && item.getNhpsMemId().equalsIgnoreCase(houseHoldItem.getNhpsMemId())) {
                oldHead = item;
                break;
            }
        }
        if (oldHead != null) {
            for (SeccMemberItem item : houseHoldMemberList) {
                if (oldHead != null && oldHead.getNhpsMemId().equalsIgnoreCase(item.getNhpsMemId())) {

                } else {
                    list.add(item);
                }
            }
            list.add(0, oldHead);
        }
        return list;
    }

    private void showDefaultFamilyList() {
        FragmentTransaction transect = fargManager.beginTransaction();
        if (defaultFragment != null) {
            transect.detach(defaultFragment);
            defaultFragment = null;
        }
        defaultFragment = new DefaultFamilyListFragment();
        if (houseHoldItem != null && houseHoldItem.getDataSource() != null &&
                houseHoldItem.getDataSource().trim().equalsIgnoreCase(AppConstant.RSBY_SOURCE)) {
            defaultFragment.setHouseHoldMemberList(findDefaultRsbyMemberList());

        } else {
            defaultFragment.setHouseHoldMemberList(findDefaultMemberList());

        }
        //   defaultFragment.setHouseHoldMemberList(findDefaultMemberList());
        transect.replace(R.id.famlistContainer, defaultFragment);
        transect.commitAllowingStateLoss();
    }

    private void showOldHeadFamilyList() {
        // chooseHeadLayout.setVisibility(View.GONE);
        FragmentTransaction transect = fargManager.beginTransaction();
        if (oldHeadFragment != null) {
            transect.detach(oldHeadFragment);
            oldHeadFragment = null;
        }
        oldHeadFragment = new OldHeadFragment();
        if (houseHoldItem != null && houseHoldItem.getDataSource() != null &&
                houseHoldItem.getDataSource().trim().equalsIgnoreCase(AppConstant.RSBY_SOURCE)) {
            oldHeadFragment.setHouseHoldMemberList(findRsbyMemberListWithOldHead());

        } else {
            oldHeadFragment.setHouseHoldMemberList(findMemberListWithOldHead());

        }
        transect.replace(R.id.famlistContainer, oldHeadFragment);
        transect.commitAllowingStateLoss();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHOOSE_NEW_HEAD_STATUS) {
            if (isFlagNoNewHead) {
                hofStatusSP.setEnabled(false);
                familyStatusSP.setEnabled(true);

                try {
                    showDefaultFamilyList();
                } catch (Exception error) {

                    error.printStackTrace();
                }

            } else {
                hofStatusSP.setEnabled(false);
                familyStatusSP.setEnabled(false);
            }

//            hofStatusSP.setEnabled(false);
//            familyStatusSP.setEnabled(false);
            refreshListMember();
        }
        /*if(requestCode==SECC_FAMILY_ACTIVITY){
            setupScreen();
        }*/
    }

    private void refreshListMember() {
        ArrayList<SeccMemberItem> refreshList = new ArrayList<>();
        SelectedMemberItem selectedMemberItem = SelectedMemberItem.create(ProjectPrefrence.
                getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SELECTED_ITEM_FOR_VERIFICATION, context));
        ArrayList<SeccMemberItem> seccFamilyList = selectedMemberItem.getRelationUpdatedList();
        if (seccFamilyList != null) {
            for (SeccMemberItem item : seccFamilyList) {
                if (item.getNhpsRelationCode() != null && item.getNhpsRelationCode().equalsIgnoreCase(AppConstant.NEW_HEAD_RELATION_CODE)) {
                    // changedHead=true;
                    newHeadItem = item;
                    break;
                }
            }
            if (newHeadItem != null) {
                if (houseHoldMemberList != null) {
                    if (houseHoldItem != null && houseHoldItem.getDataSource() != null &&
                            houseHoldItem.getDataSource().trim().equalsIgnoreCase(AppConstant.RSBY_SOURCE)) {
                        for (SeccMemberItem item : houseHoldMemberList) {
                            if (item.getRsbyMemId() != null && item.getRsbyMemId().trim().equalsIgnoreCase(newHeadItem.getRsbyMemId().trim())) {
                                // skip new head
                            } else if (item.getRsbyMemId() != null && item.getRsbyMemId().equalsIgnoreCase(houseHoldItem.getRsbyMemId().trim())) {
                                // find old head
                                oldHeadItem = item;
                            } else {
                                refreshList.add(item);
                            }
                        }
                        refreshList.add(0, newHeadItem);
                        if (oldHeadItem != null) {
                            oldHeadItem.setHhStatus(householdStatus.getStatusCode());
                            oldHeadItem.setMemStatus(hofStatusItem.getStatusCode());
                            oldHeadItem.setLockedSave(AppConstant.LOCKED + "");
                            //  SeccDatabase.updateSeccMember(oldHeadItem,context);
                        }
                        refreshList.add(houseHoldMemberList.size() - 1, oldHeadItem);
                        houseHoldMemberList = refreshList;
                    } else {
                        for (SeccMemberItem item : houseHoldMemberList) {
                            if (item.getNhpsMemId() != null && item.getNhpsMemId().trim().equalsIgnoreCase(newHeadItem.getNhpsMemId().trim())) {
                                // skip new head
                            } else if (item.getNhpsMemId() != null && item.getNhpsMemId().equalsIgnoreCase(houseHoldItem.getNhpsMemId().trim())) {
                                // find old head
                                oldHeadItem = item;
                            } else {
                                refreshList.add(item);
                            }
                        }
                        refreshList.add(0, newHeadItem);
                        if (oldHeadItem != null) {
                            oldHeadItem.setHhStatus(householdStatus.getStatusCode());
                            oldHeadItem.setMemStatus(hofStatusItem.getStatusCode());
                            oldHeadItem.setLockedSave(AppConstant.LOCKED + "");
                            //  SeccDatabase.updateSeccMember(oldHeadItem,context);
                        }
                        refreshList.add(houseHoldMemberList.size() - 1, oldHeadItem);
                        houseHoldMemberList = refreshList;
                    }
                    showNewHeadList();
                }
            }
        }
       /* if(seccFamilyList!=null) {
            for (SeccMemberItem item : seccFamilyList) {
                if (item.getNhpsRelationCode() != null && item.getNhpsRelationCode().equalsIgnoreCase(AppConstant.NEW_HEAD_RELATION_CODE)) {
                    // changedHead=true;
                   newHeadItem = item;
                    break;
                }
            }
            if (newHeadItem == null) {
                houseHoldMemberList = new ArrayList<>();
                for (SeccMemberItem item : seccFamilyList) {
                    if (item.getNhpsMemId().equalsIgnoreCase(houseHoldItem.getNhpsMemId())) {
                        houseHoldMemberList.add(0, item);
                    } else {
                        houseHoldMemberList.add(item);
                    }
                }
            } else {
                houseHoldMemberList = new ArrayList<>();
                for (SeccMemberItem item : seccFamilyList) {
                    AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Nhps relation : "+item.getNhpsRelationName());
                    if (item.getNhpsRelationCode() != null && item.getNhpsRelationCode().equalsIgnoreCase(AppConstant.NEW_HEAD_RELATION_CODE)) {
                        houseHoldMemberList.add(0, item);
                    } else if (item.getNhpsMemId().equalsIgnoreCase(houseHoldItem.getNhpsMemId())) {
                        oldHeadItem = item;
                    } else {
                        houseHoldMemberList.add(item);
                    }
                }

            if(oldHeadItem!=null){
                oldHeadItem.setHhStatus(householdStatus.getStatusCode());
                oldHeadItem.setMemStatus(hofStatusItem.getStatusCode());
                oldHeadItem.setLockedSave(AppConstant.LOCKED+"");
              //  SeccDatabase.updateSeccMember(oldHeadItem,context);
            }
                houseHoldMemberList.add(houseHoldMemberList.size(), oldHeadItem);
            }


            TreeSet<SeccMemberItem> seccTreeSet = new TreeSet<SeccMemberItem>(new Comparator<SeccMemberItem>() {

                public int compare(SeccMemberItem o1, SeccMemberItem o2) {
                    // return 0 if objects are equal in terms of your properties
                    if (o1.getNhpsMemId().equalsIgnoreCase(o2.getNhpsMemId())) {
                        return 0;
                    }
                    return 1;
                }
            });
            seccTreeSet.addAll(houseHoldMemberList);
            houseHoldMemberList = new ArrayList<>();
            houseHoldMemberList.addAll(seccTreeSet);*/
    }

    private void setHouseHoldAddress() {
        String address = "";
        if (houseHoldItem != null) {
            if (houseHoldItem.getAddressline1() != null && !houseHoldItem.getAddressline1().trim().
                    equalsIgnoreCase("-") && !houseHoldItem.getAddressline1().equalsIgnoreCase("")) {
                address = address + houseHoldItem.getAddressline1() + ", ";
                // AppUtility.showLog(AppConstant.LOG_STATUS,TAG," Address3 : "+seccItem.getAddressline1());

            }
            if (houseHoldItem.getAddressline2() != null && !houseHoldItem.getAddressline2().trim()
                    .equalsIgnoreCase("-") && !houseHoldItem.getAddressline2().equalsIgnoreCase("")) {
                //AppUtility.showLog(AppConstant.LOG_STATUS,TAG," Address : "+seccItem.getAddressline2());
                address = address + houseHoldItem.getAddressline2() + ", ";
            }
            if (houseHoldItem.getAddressline3() != null && !houseHoldItem.getAddressline3().trim().
                    equalsIgnoreCase("-") && !houseHoldItem.getAddressline3().equalsIgnoreCase("")) {
                //AppUtility.showLog(AppConstant.LOG_STATUS,TAG," Address1 : "+seccItem.getAddressline3());

                address = address + houseHoldItem.getAddressline3() + ", ";
            }
            if (houseHoldItem.getAddressline4() != null && !houseHoldItem.getAddressline4().trim().
                    equalsIgnoreCase("-") && !houseHoldItem.getAddressline4().equalsIgnoreCase("")) {
                // AppUtility.showLog(AppConstant.LOG_STATUS,TAG," Address2 : "+seccItem.getAddressline4());

                address = address + houseHoldItem.getAddressline4();
            }
        }

        /*if(houseHoldMemberList!=null && houseHoldMemberList.size()>0) {
            String address = "";
            if (houseHoldMemberList.get(0).getAddressline1() != null) {
                address = address + houseHoldMemberList.get(0).getAddressline1() + ",";
            }
            if (houseHoldMemberList.get(0).getAddressline2() != null) {
                address = address + houseHoldMemberList.get(0).getAddressline2() + ",";
            }
            if (houseHoldMemberList.get(0).getAddressline3() != null) {
                address = address + houseHoldMemberList.get(0).getAddressline3() + ",";
            }
            if (houseHoldMemberList.get(0).getAddressline4() != null) {
                address = address + houseHoldMemberList.get(0).getAddressline4();
            }
           // address=;
      *//*  if(houseHoldMemberList.get(0).getAddressline5()!=null){
            address=address+houseHoldMemberList.get(0).getAddressline5()+",";
        }*//*
        }*/
        try {
            householdAddressTV.setText(houseHoldMemberList.get(0).getAhlslnohhd() + " " + address);//+"\n"+houseHoldMemberList.get(0).getHhdNo());
        } catch (Exception ex) {

        }
    }

    private void setRsbyHouseholdAddress() {
     /*   String address="";
        if(houseHoldItem!=null){
            if(houseHoldItem.getAddressline1()!=null && !houseHoldItem.getAddressline1().trim().
                    equalsIgnoreCase("-") && !houseHoldItem.getAddressline1().equalsIgnoreCase("")){
                address=address+houseHoldItem.getAddressline1()+", ";
                // AppUtility.showLog(AppConstant.LOG_STATUS,TAG," Address3 : "+seccItem.getAddressline1());

            }
            if(houseHoldItem.getAddressline2()!=null && !houseHoldItem.getAddressline2().trim()
                    .equalsIgnoreCase("-") && !houseHoldItem.getAddressline2().equalsIgnoreCase("") ){
                //AppUtility.showLog(AppConstant.LOG_STATUS,TAG," Address : "+seccItem.getAddressline2());
                address=address+houseHoldItem.getAddressline2()+", ";
            }
            if(houseHoldItem.getAddressline3()!=null && !houseHoldItem.getAddressline3().trim().
                    equalsIgnoreCase("-") && !houseHoldItem.getAddressline3().equalsIgnoreCase("") ){
                //AppUtility.showLog(AppConstant.LOG_STATUS,TAG," Address1 : "+seccItem.getAddressline3());

                address=address+houseHoldItem.getAddressline3()+", ";
            }
            if(houseHoldItem.getAddressline4()!=null && !houseHoldItem.getAddressline4().trim().
                    equalsIgnoreCase("-") && !houseHoldItem.getAddressline4().equalsIgnoreCase("") ){
                // AppUtility.showLog(AppConstant.LOG_STATUS,TAG," Address2 : "+seccItem.getAddressline4());

                address=address+houseHoldItem.getAddressline4();
            }
        }

        *//*if(houseHoldMemberList!=null && houseHoldMemberList.size()>0) {
            String address = "";
            if (houseHoldMemberList.get(0).getAddressline1() != null) {
                address = address + houseHoldMemberList.get(0).getAddressline1() + ",";
            }
            if (houseHoldMemberList.get(0).getAddressline2() != null) {
                address = address + houseHoldMemberList.get(0).getAddressline2() + ",";
            }
            if (houseHoldMemberList.get(0).getAddressline3() != null) {
                address = address + houseHoldMemberList.get(0).getAddressline3() + ",";
            }
            if (houseHoldMemberList.get(0).getAddressline4() != null) {
                address = address + houseHoldMemberList.get(0).getAddressline4();
            }
           // address=;
      *//**//*  if(houseHoldMemberList.get(0).getAddressline5()!=null){
            address=address+houseHoldMemberList.get(0).getAddressline5()+",";
        }*//**//*
        }*/
        String urn = "";
        if (houseHoldItem.getRsbyUrnId() != null) {
            urn = AppUtility.formatUrn(houseHoldItem.getRsbyUrnId());
        }
        householdAddressTV.setText(urn);

    }

    public static void openResetHousehold(final SeccMemberItem item1, Button button, final Context context, final Activity activity) {
        PopupMenu popup = new PopupMenu(context, button);
        popup.getMenuInflater()
                .inflate(R.menu.menu_reset_household, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.reset:
                        alertForValidateLater(context.getResources().getString(R.string.alert_for_reset), item1, context, activity, AppConstant.RESET);
                        break;
                    case R.id.preview:
                        // openPreview(item1);
                        AppUtility.openSyncPreview(item1, context, activity);
                        break;
                }
                return true;
            }
        });
        popup.show();
    }

    public static void openReset(final SeccMemberItem item1, Button button, final Context context, final Activity activity) {


        PopupMenu popup = new PopupMenu(context, button);
        popup.getMenuInflater()
                .inflate(R.menu.menu_reset, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.reset:
                        alertForValidateLater(context.getResources().getString(R.string.alert_for_reset), item1, context, activity, AppConstant.RESET);
                        break;
                    case R.id.unlockRecord:
                        // askPinToLock(SeccMemberListActivity.EDIT,item1);
                        alertForValidateLater(context.getResources().getString(R.string.alert_for_unlock), item1, context, activity, AppConstant.UNLOCK);
                        break;
                    case R.id.preview:
                        AppUtility.openSyncPreview(item1, context, activity);
                        break;
                }
                return true;
            }
        });
        popup.show();
    }

    public static void openUnLock(final SeccMemberItem item1, Button button, final Context context, final Activity activity) {


        PopupMenu popup = new PopupMenu(context, button);
        popup.getMenuInflater()
                .inflate(R.menu.menu_unlock, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
               /*     case R.id.reset:
                        alertForValidateLater(context.getResources().getString(R.string.alert_for_reset),item1,context,activity, AppConstant.RESET);
                        break;*/
                    case R.id.unlockRecord:
                        // askPinToLock(SeccMemberListActivity.EDIT,item1);
                        alertForValidateLater(context.getResources().getString(R.string.alert_for_unlock), item1, context, activity, AppConstant.UNLOCK);
                        break;
                    case R.id.preview:
                        AppUtility.openSyncPreview(item1, context, activity);
                        break;
                }
                return true;
            }
        });
        popup.show();
    }

    private static void alertForValidateLater(String msg, final SeccMemberItem item, final Context context, final Activity activity, final String action) {

        final android.app.AlertDialog internetDiaolg = new android.app.AlertDialog.Builder(context).create();
        LayoutInflater factory = LayoutInflater.from(context);
        View alertView = factory.inflate(R.layout.internet_try_again_popup, null);
        internetDiaolg.setView(alertView);
        internetDiaolg.show();
        TextView tryGainMsgTV = (TextView) alertView.findViewById(R.id.deleteMsg);
        tryGainMsgTV.setText(msg);
        Button tryAgainBT = (Button) alertView.findViewById(R.id.tryAgainBT);
        tryAgainBT.setText(context.getResources().getString(R.string.Confirm_Submit));
        Button cancelBT = (Button) alertView.findViewById(R.id.cancelBT);
        tryAgainBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (action.equalsIgnoreCase(AppConstant.RESET)) {
                    askPinToLock(AppConstant.RESET, item, context, activity);
                    internetDiaolg.dismiss();
                } else if (action.equalsIgnoreCase(AppConstant.UNLOCK)) {
                    SeccDatabase.editRecord(item, context);
                   /* Intent intent=new Intent(context, SeccMemberListActivity.class);
                    activity.startActivity(intent);
                    activity.finish();*/
                    if (item != null && item.getDataSource() != null && item.getDataSource().equalsIgnoreCase(AppConstant.RSBY_SOURCE)) {
                        ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.DASHBOARD_TAB_STATUS, 3 + "", context);
                        ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                                AppConstant.HOUSEHOLD_TAB_STATUS, 6 + "", context);
                    } else {
                        ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.DASHBOARD_TAB_STATUS, 1 + "", context);
                        ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                                AppConstant.HOUSEHOLD_TAB_STATUS, 6 + "", context);
                    }

                    Intent theIntent = new Intent(context, SearchActivityWithHouseHold.class);
                    activity.startActivity(theIntent);
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

    public static void askPinToLock(final String status, final SeccMemberItem item, final Context context, final Activity activity) {
        AppUtility.softKeyBoard(activity, 1);
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
                AppUtility.softKeyBoard(activity, 0);
                String pin = pinET.getText().toString();
                if (pin.toString().equalsIgnoreCase(verifierDetail.getPin())) {
                    if (status.equalsIgnoreCase(AppConstant.RESET)) {
                        SeccDatabase.resetData(item, context);
                        if (item != null && item.getDataSource() != null &&
                                item.getDataSource().trim().equalsIgnoreCase(AppConstant.RSBY_SOURCE)) {
                            ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.DASHBOARD_TAB_STATUS, 3 + "", context);
                            ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                                    AppConstant.HOUSEHOLD_TAB_STATUS, 2 + "", context);
                            HouseHoldItem houseHoldItem = SeccDatabase.getRsbyHouseHoldByUrn(item.getRsbyUrnId(), context);
                            SelectedMemberItem selectedMemberItem = SelectedMemberItem.create(ProjectPrefrence.
                                    getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                                            AppConstant.SELECTED_ITEM_FOR_VERIFICATION, context));
                            selectedMemberItem.setHouseHoldItem(houseHoldItem);
                            ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                                    AppConstant.SELECTED_ITEM_FOR_VERIFICATION, selectedMemberItem.serialize(), context);
                        } else {
                            ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.DASHBOARD_TAB_STATUS, 1 + "", context);
                            ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                                    AppConstant.HOUSEHOLD_TAB_STATUS, 2 + "", context);
                            HouseHoldItem houseHoldItem = SeccDatabase.getHouseHoldList(item.getHhdNo(), context);
                            SelectedMemberItem selectedMemberItem = SelectedMemberItem.create(ProjectPrefrence.
                                    getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                                            AppConstant.SELECTED_ITEM_FOR_VERIFICATION, context));
                            selectedMemberItem.setHouseHoldItem(houseHoldItem);
                            ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                                    AppConstant.SELECTED_ITEM_FOR_VERIFICATION, selectedMemberItem.serialize(), context);
                        }
                        askForPinDailog.dismiss();
                        activity.finish();
                        Intent intent = new Intent(context, SeccMemberListActivity.class);
                        // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        activity.startActivity(intent);
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


    @SuppressLint("NewApi")
    private void findAligableMember() {

        SeccMemberItem requiredItem = new SeccMemberItem();
        if (houseHoldItem != null && houseHoldItem.getDataSource().equalsIgnoreCase(AppConstant.RSBY_SOURCE)) {
            seccMemberList = SeccDatabase.getRsbyMemberListWithUrn(houseHoldItem.getRsbyUrnId(), context);
            printPreview.setVisibility(View.VISIBLE);
        } else {
            printPreview.setVisibility(View.VISIBLE);
            seccMemberList = SeccDatabase.getSeccMemberList(houseHoldItem.getHhdNo(), context);
        }
        if (houseHoldItem.getDataSource() != null && houseHoldItem.getDataSource().equalsIgnoreCase(AppConstant.RSBY_SOURCE)) {
            isHofFound = isHeadAvailableRsby(seccMemberList);
        } else {
            isHofFound = isHeadAvailableSecc(seccMemberList);
        }

        if (!isHofFound) {
            for (SeccMemberItem item1 : seccMemberList) {
                if (item1.getNhpsId() != null && !item1.getNhpsId().equalsIgnoreCase("")) {
                    if (houseHoldItem.getDataSource() != null && houseHoldItem.getDataSource().equalsIgnoreCase(AppConstant.RSBY_SOURCE)) {
                        if (item1.getRsbyName() != null && !item1.getRsbyName().equalsIgnoreCase("")) {
                            if (item1.getMemberPhoto1() != null && !item1.getMemberPhoto1().equalsIgnoreCase("")) {
                                if (item1.getRsbyDob() != null && !item1.getRsbyDob().equalsIgnoreCase("")) {
                                    if (item1.getRsbyGender() != null && !item1.getRsbyGender().equalsIgnoreCase("")) {
                                        if (item1.getMemStatus() != null &&
                                                item1.getMemStatus().equalsIgnoreCase(AppConstant.MEMBERFOUND_AND_PRESENT)) {

                                            requiredItem = item1;
                                            memberFound = true;
                                            houseHoldEligible = true;
                                            allowForPrint(requiredItem, houseHoldEligible);
                                            break;
                                        } else {
                                            houseHoldEligible = false;
                                        }
                                    } else {
                                        houseHoldEligible = false;
                                    }
                                } else {
                                    houseHoldEligible = false;
                                }
                            } else {
                                houseHoldEligible = false;
                            }
                        } else {
                            houseHoldEligible = false;

                        }
                    } else {

                        if (item1.getName() != null && !item1.getName().equalsIgnoreCase("")) {
                            if (item1.getMemberPhoto1() != null && !item1.getMemberPhoto1().equalsIgnoreCase("")) {
                                if (item1.getDob() != null && !item1.getDob().equalsIgnoreCase("")) {
                                    if (item1.getGenderid() != null && !item1.getGenderid().equalsIgnoreCase("")) {
                                        if (item1.getMemStatus() != null &&
                                                item1.getMemStatus().equalsIgnoreCase(AppConstant.MEMBERFOUND_AND_PRESENT)) {
                                            requiredItem = item1;
                                            memberFound = true;
                                            houseHoldEligible = true;
                                            allowForPrint(requiredItem, houseHoldEligible);

                                            break;
                                        } else {
                                            houseHoldEligible = false;
                                        }
                                    } else {
                                        houseHoldEligible = false;
                                    }
                                } else {
                                    houseHoldEligible = false;
                                }
                            } else {
                                houseHoldEligible = false;
                            }
                        } else {
                            houseHoldEligible = false;
                        }


                    }
                } else {
                    houseHoldEligible = false;
                }

                if (!houseHoldEligible) {
                    printPreview.setBackground(context.getResources().getDrawable(R.drawable.button_background_red));
                    printPreview.setTextColor(context.getResources().getColor(R.color.red));
                    printPreview.setText(context.getResources().getString(R.string.householdNotEliblePrint));
                    printPreview.setEnabled(false);
                }


            }
        }
    }


    public void showNotification(View v) {

        LinearLayout notificationLayout = (LinearLayout) v.findViewById(R.id.notificationLayout);
        WebView notificationWebview = (WebView) v.findViewById(R.id.notificationWebview);
        String prePairedMessage = AppUtility.getNotificationData(context);
        if (prePairedMessage != null) {
            notificationLayout.setVisibility(View.VISIBLE);
            notificationWebview.loadData(prePairedMessage, "text/html", "utf-8"); // Set focus to textview
        }
    }

    public void showNotification() {

        LinearLayout notificationLayout = (LinearLayout) findViewById(R.id.notificationLayout);
        WebView notificationWebview = (WebView) findViewById(R.id.notificationWebview);
        String prePairedMessage = AppUtility.getNotificationData(context);
        if (prePairedMessage != null) {
            notificationLayout.setVisibility(View.VISIBLE);
            notificationWebview.loadData(prePairedMessage, "text/html", "utf-8"); // Set focus to textview
        }
    }

    private String checkAppConfig() {
        StateItem selectedStateItem = StateItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SELECTED_STATE, context));
        ArrayList<ConfigurationItem> configList = SeccDatabase.findConfiguration(selectedStateItem.getStateCode(), context);

        if (configList != null) {
            for (ConfigurationItem item1 : configList) {
                if (item1.getConfigId().equalsIgnoreCase(AppConstant.PRINT_CARD)) {
                    printStatus = item1.getStatus();
                }

                if (item1.getConfigId().equalsIgnoreCase(AppConstant.APPLICATION_ZOOM)) {
                    zoomMode = item1.getStatus();
                }
            }
        }

        return printStatus;
    }

    private void searchMenuAppcongigBoth(View v) {
        final ImageView settings = (ImageView) v.findViewById(R.id.settings);
        settings.setVisibility(View.VISIBLE);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(context, settings);
                popup.getMenuInflater()
                        .inflate(R.menu.menu_nav_dashboard, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {

                            case R.id.dashboard:
                                //   String searchText = searchFamilyMemberET.getText().toString();
                                Intent theIntent = new Intent(context, SearchActivityWithHouseHold.class);
                                theIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                theIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(theIntent);
                                leftTransition();
                                break;

                        }
                        return true;
                    }
                });
                popup.show();
            }
        });

      /*  menuLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settings.performClick();
            }
        });*/
    }

    private void searchMenuAppcongigBoth() {
        final ImageView settings = (ImageView) findViewById(R.id.settings);
        settings.setVisibility(View.VISIBLE);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(context, settings);
                popup.getMenuInflater()
                        .inflate(R.menu.menu_nav_dashboard, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {

                            case R.id.dashboard:
                                //   String searchText = searchFamilyMemberET.getText().toString();
                                Intent theIntent = new Intent(context, SearchActivityWithHouseHold.class);
                                theIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                theIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(theIntent);
                                leftTransition();
                                break;

                        }
                        return true;
                    }
                });
                popup.show();
            }
        });

      /*  menuLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settings.performClick();
            }
        });*/
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (AppUtility.isAppIsInBackground(context)) {
            if (!pinLockIsShown) {
                askPinToLock();
            }
        }
    }

    private void askPinToLock() {
        pinLockIsShown = true;
        final AlertDialog askForPinDailog = new AlertDialog.Builder(context).create();
        LayoutInflater factory = LayoutInflater.from(context);
        View alertView = factory.inflate(R.layout.ask_pin_layout, null);
        askForPinDailog.setView(alertView);
        askForPinDailog.setCancelable(false);
        askForPinDailog.show();
        // Log.d(TAG,"delete status :"+deleteStatus);
        // dialog.setContentView(R.layout.opt_auth_layout);
        //    final TextView otpAuthMsg=(TextView)alertView.findViewById(R.id.otpAuthMsg);
        final VerifierLoginResponse verifierDetail = VerifierLoginResponse.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.VERIFIER_CONTENT, context));
        final EditText pinET = (EditText) alertView.findViewById(R.id.deletPinET);
        final TextView errorTV = (TextView) alertView.findViewById(R.id.invalidOtpTV);

        wrongAttempetCountText = (TextView) alertView.findViewById(R.id.wrongAttempetCountText);
        wrongAttempetCountValue = (TextView) alertView.findViewById(R.id.wrongAttempetCountValue);


        pinET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //   errorTV.setVisibility(View.GONE);
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


                currentTime = System.currentTimeMillis();
                try {

                    wrongPinSavedTime = Long.parseLong(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.WORNG_PIN_ENTERED_TIMESTAMP, context));
                } catch (Exception ex) {
                    wrongPinSavedTime = 0;
                }
                if (currentTime > (wrongPinSavedTime + millisecond24)) {


                    //  AppUtility.softKeyBoard(activity, 0);
                    String pin = pinET.getText().toString();
                    if (pin.toString().equalsIgnoreCase(verifierDetail.getPin())) {
                        askForPinDailog.dismiss();
                        pinLockIsShown = false;
                    } else if (pin.equalsIgnoreCase("")) {
                        // CustomAlert.alertWithOk(context,"Please enter valid pin");
                        errorTV.setVisibility(View.VISIBLE);
                        errorTV.setText("Enter pin");
                        pinET.setText("");
                        //  pinET.setHint("");
                    } else if (!pin.toString().equalsIgnoreCase(verifierDetail.getPin())) {

                        if (wrongPinCount >= 2) {
                            errorTV.setTextColor(context.getResources().getColor(R.color.red));
                            wrongAttempetCountValue.setTextColor(context.getResources().getColor(R.color.red));
                            wrongAttempetCountText.setTextColor(context.getResources().getColor(R.color.red));
                        }
                        wrongPinCount++;
                        wrongAttempetCountValue.setText((3 - wrongPinCount) + "");
                        if (wrongPinCount > 2) {
                            long time = System.currentTimeMillis();
                            ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.WORNG_PIN_ENTERED_TIMESTAMP, time + "", context);
                            CustomAlert.alertWithOk(context, context.getResources().getString(R.string.youHaveExceedPinLimit));
                        } else {
                            errorTV.setVisibility(View.VISIBLE);
                            errorTV.setText("Enter correct pin");
                            pinET.setText("");
//                        CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterValidPin));
//                        pinET.setText("");
                        }
//
//                    errorTV.setVisibility(View.VISIBLE);
//                    errorTV.setText("Enter correct pin");
//                    pinET.setText("");
                        // pinET.setHint("Enter 4-di");
                    }
                } else {

                    //alert  when pin login is diabled for 24 hrs
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.pinLoginDisabled));
                    errorTV.setText("Pin login disabled for 24 hrs.");
                    pinET.setText("");
                    return;
                }
            }
        });
        cancelBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pinLockIsShown = false;
                Intent intent_login = new Intent(context, LoginActivity.class);
                intent_login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent_login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent_login);
                finish();
            }
        });
    }

    @Override
    public void onConfigurationChanged(final Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onLowMemory() {

    }

    @Override
    public void onTrimMemory(final int level) {
        if (level == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {
            if (!pinLockIsShown) {
                askPinToLock();
            }
        }
    }

    @SuppressLint("NewApi")
    private void allowForPrint(SeccMemberItem requiredItem, boolean ishouseHoldEligible) {
        if (!ishouseHoldEligible) {
            printPreview.setBackground(context.getResources().getDrawable(R.drawable.button_background_red));
            printPreview.setTextColor(context.getResources().getColor(R.color.red));
            printPreview.setText(context.getResources().getString(R.string.householdNotEliblePrint));
            printPreview.setEnabled(false);
        } else {
            printPreview.setEnabled(true);
            final SeccMemberItem finalRequiredItem = requiredItem;
            final SeccMemberItem finalRequiredItem1 = requiredItem;
            printPreview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (printStatus.equalsIgnoreCase("Y")) {
                        if (finalRequiredItem != null) {
                            Intent theIntent = new Intent(context, PrintCardMainActivity.class);
/*if(finalRequiredItem1!=null && finalRequiredItem1.getRelation()!=null && finalRequiredItem1.getRelation().equalsIgnoreCase("SELF"))*/
                            if (finalRequiredItem1.getNhpsRelationCode() != null && finalRequiredItem1.getNhpsRelationCode().trim().equalsIgnoreCase(AppConstant.NEW_HEAD_RELATION_CODE)) {
                                theIntent.putExtra("NAMEONCARD", "HOF: ");
                            } else {
                                theIntent.putExtra("NAMEONCARD", "Family of: ");
                            }
                            theIntent.putExtra(AppConstant.sendingPrintData, finalRequiredItem1);
                            startActivity(theIntent);
                        }
                    } else {
                        AppUtility.alertWithOk(context, "Configration does not support Printing");
                    }
                }
            });
        }
    }

    private boolean isHeadAvailableRsby(ArrayList<SeccMemberItem> seccMemList) {
        SeccMemberItem requiredMember = null;
        for (SeccMemberItem item1 : seccMemList) {
            if (item1.getNhpsId() != null && !item1.getNhpsId().equalsIgnoreCase("")) {
                if (item1.getMemberPhoto1() != null && !item1.getMemberPhoto1().equalsIgnoreCase("")) {
                    if (item1.getRsbyDob() != null && !item1.getRsbyDob().equalsIgnoreCase("")) {
                        if (item1.getRsbyGender() != null && !item1.getRsbyGender().equalsIgnoreCase("")) {
                            if (item1.getMemStatus() != null &&
                                    (item1.getMemStatus().equalsIgnoreCase(AppConstant.MEMBERFOUND_AND_PRESENT) ||
                                            item1.getMemStatus().equalsIgnoreCase(AppConstant.MEMBERFOUND_AND_PRESENT))) {
                                memberFound = true;
                                houseHoldEligible = true;
                                allowForPrint(item1, houseHoldEligible);
                                break;
                            } else {
                                houseHoldEligible = false;
                            }
                        } else {
                            houseHoldEligible = false;
                        }
                    } else {
                        houseHoldEligible = false;
                    }
                } else {
                    houseHoldEligible = false;
                }
            } else {
                houseHoldEligible = false;
            }
        }
        return houseHoldEligible;
    }


    private boolean isHeadAvailableSecc(ArrayList<SeccMemberItem> seccMemberList) {

        for (SeccMemberItem item1 : seccMemberList) {
            if (item1.getNhpsId() != null && !item1.getNhpsId().equalsIgnoreCase("")) {
                if (item1.getNhpsRelationCode() != null &&
                        item1.getNhpsRelationCode().trim().equalsIgnoreCase(AppConstant.NEW_HEAD_RELATION_CODE)) {
                    if (item1.getName() != null && !item1.getName().equalsIgnoreCase("")) {
                        if (item1.getMemberPhoto1() != null && !item1.getMemberPhoto1().equalsIgnoreCase("")) {
                            if (item1.getDob() != null && !item1.getDob().equalsIgnoreCase("")) {
                                if (item1.getGenderid() != null && !item1.getGenderid().equalsIgnoreCase("")) {
                                    if (item1.getMemStatus() != null &&
                                            (item1.getMemStatus().equalsIgnoreCase(AppConstant.MEMBERFOUND_AND_PRESENT) ||
                                                    item1.getMemStatus().equalsIgnoreCase(AppConstant.MEMBERFOUND_AND_PRESENT))) {

                                        memberFound = true;
                                        houseHoldEligible = true;
                                        allowForPrint(item1, houseHoldEligible);
                                        break;
                                    } else {
                                        houseHoldEligible = false;
                                    }
                                } else {
                                    houseHoldEligible = false;
                                }
                            } else {
                                houseHoldEligible = false;
                            }
                        } else {
                            houseHoldEligible = false;
                        }
                    } else {

                        houseHoldEligible = false;
                    }
                } else {
                    houseHoldEligible = false;
                }
            }
        }
        return houseHoldEligible;
    }
}
