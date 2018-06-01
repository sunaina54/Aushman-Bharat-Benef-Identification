package com.nhpm.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.customComponent.CustomAlert;
import com.customComponent.CustomAsyncTask;
import com.customComponent.CustomHttpClient;
import com.customComponent.TaskListener;
import com.customComponent.utility.ProjectPrefrence;
import com.nhpm.LocalDataBase.SharedPrefrenceData;
import com.nhpm.LocalDataBase.dto.SeccDatabase;
import com.nhpm.Models.request.AadhaarAuthRequestItem;
import com.nhpm.Models.response.master.ConfigurationItem;
import com.nhpm.Models.response.master.StateItem;
import com.nhpm.Models.response.rsbyMembers.RSBYItem;
import com.nhpm.Models.response.rsbyMembers.RsbyHouseholdItem;
import com.nhpm.Models.response.seccMembers.HouseHoldItem;
import com.nhpm.Models.response.seccMembers.SeccHouseholdResponse;
import com.nhpm.Models.response.seccMembers.SeccMemberItem;
import com.nhpm.Models.response.seccMembers.SelectedMemberItem;
import com.nhpm.Models.response.verifier.DemoAuthResponseItem;
import com.nhpm.Models.response.verifier.VerifierLocationItem;
import com.nhpm.Models.response.verifier.VerifierLoginResponse;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;
import com.nhpm.Utility.AppUtility;
import com.nhpm.Utility.ApplicationGlobal;
import com.nhpm.fragments.HouseholdStatusBoardRsbyFragment;
import com.nhpm.fragments.MemberStatusDadhboardFragment;
import com.nhpm.fragments.HouseholdStatusFragment;
import com.nhpm.fragments.HouseholdStatusBoardFragment;
import com.nhpm.rsbyFieldValidation.RsbyCardReadingActivity;
import com.nhpm.rsbyFieldValidation.RsbySyncHouseholdActivity;
import com.nhpm.rsbyFieldValidation.fragment.RsbyHouseholdStatusBoardFragment;


import java.util.ArrayList;

import pl.polidea.view.ZoomView;

public class SearchActivityWithHouseHold extends BaseActivity implements ComponentCallbacks2 {

    private SeccHouseholdResponse houseHoldResponse;
    private Button pendingHouseholdBT, partialVerifiedBT, seccDashboardAppConfigBT, partialVerifiedAppConfigBT;
    public FragmentManager fragMgr;
    private Context context;
    private HouseholdStatusFragment householdStatusFragment;
    /* private PartialVerifiedHouseholFragment partialVerifiedHouseholFragment;
     private VerifiedHouseHoldFragment verifiedHouseholFragment;*/
    private Fragment fragment;
    private ArrayList<HouseHoldItem> totalHouseHold;
    private VerifierLocationItem downloadedLocation;
    private final String TAG = "SearchActivityWithHouseHold";
    private VerifierLocationItem locationItem;
    private RelativeLayout searchFamilyMemberLayout, verifiedMemLayout, partialmemLayout,
            partialRsbyVerifiedMemberLayout, verifiedRsbyMemberLayout;
    // public ArrayList<SeccMemberItem> memberList;
    private CustomAsyncTask asyncTask;

    private EditText searchFamilyMemberET;
    private ImageView backIV;
    private TextView partialVerTV, verifiedMemTV, partialRsbyVerifiedMemberTV, verifiedRsbyMemberTV;
    private int YELLOW, VOILET, WHITE, BLACK;
    private LinearLayout totalhouseholdLayout, visitedHouseholdLayout, pendingHouseholdLayout;
    private LinearLayout totalMemberLayout, validatedMemberLayout, nonValidatedMemberLayout;
    private LinearLayout rsbyTotalmemberLayout, validatedRsbyMemberLayout, nonValidatedrsbymemberLayout;
    private HouseholdStatusBoardFragment seccdashboardFragment;
    private MemberStatusDadhboardFragment memberStatusFragment;
    private VerifierLoginResponse verifierDetail;
    private TextView header1TV, header2TV;
    private Button memberStatusBT;
    public static String SECC_DASHBOARD = "1", MEMBER_DASHBOARD = "2", RSBYDASHBOARD = "3";
    private String status;
    public static int SYNC_REQUEST = 2;
    private LinearLayout mZoomLinearLayout;
    private ZoomView zoomView;
    private int pendingCount = 0;
    private int validatedCount;
    private int invalidCount;
    private SelectedMemberItem selectedMemItem;
    private String rsbySoursce = "Y";
    private String seccDataSource = "Y";
    private String zoomMode = "N";
    private StateItem selectedStateItem;
    public static int filterNav;
    private String downloadedDataType;
    private boolean pinLockIsShown = false;



    private int wrongPinCount = 0;
    private long wrongPinSavedTime;
    private long currentTime;
    private TextView wrongAttempetCountValue,wrongAttempetCountText;
    private long millisecond24 = 86400000;

    private HouseholdStatusBoardRsbyFragment rsbyHouseholdStatusBoardFragment;

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        fragMgr = getSupportFragmentManager();
        checkAppConfig();

        if (zoomMode.equalsIgnoreCase("N")) {

            setContentView(R.layout.activity_search_activity_with_house_hold);
            setupWithOutZoomScreen();
        } else {
            setContentView(R.layout.dummy_layout_for_zooming);
            setupwithZoomScreen();
        }

    }

    private void setupwithZoomScreen() {
        mZoomLinearLayout = (LinearLayout) findViewById(R.id.mZoomLinearLayout);
        View v = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.activity_search_activity_with_house_hold, null, false);
        v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
        showNotification(v);
        downloadedDataType = ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.DOWNLOADINGSOURCE, context);
        YELLOW = AppUtility.getColor(context, R.color.yellow);
        VOILET = AppUtility.getColor(context, R.color.tab_color);
        WHITE = AppUtility.getColor(context, R.color.white_shine);
        BLACK = AppUtility.getColor(context, R.color.black_shine);
        status = ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.DASHBOARD_TAB_STATUS, context);

        verifierDetail = VerifierLoginResponse.create(ProjectPrefrence.
                getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.VERIFIER_CONTENT, context));
        downloadedLocation = VerifierLocationItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF
                , AppConstant.SELECTED_BLOCK, context));
        locationItem = VerifierLocationItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SELECTED_BLOCK, context));
        header1TV = (TextView) v.findViewById(R.id.header1TV);
        header2TV = (TextView) v.findViewById(R.id.header2TV);
        backIV = (ImageView) v.findViewById(R.id.back);
        pendingHouseholdBT = (Button) v.findViewById(R.id.notVerifiedFamilyBT);
        partialVerifiedBT = (Button) v.findViewById(R.id.partialVerifiedBT);
        memberStatusBT = (Button) v.findViewById(R.id.memberStatusBT);
        seccDashboardAppConfigBT = (Button) v.findViewById(R.id.seccDashboardAppConfigBT);
        partialVerifiedAppConfigBT = (Button) v.findViewById(R.id.partialVerifiedAppConfigBT);

        findAllHousehold();
        zoomView = new ZoomView(this);
        zoomView.addView(v);
        mZoomLinearLayout.addView(zoomView);
        backIV.setVisibility(View.VISIBLE);

        String locationStr = "";


        if (downloadedDataType != null && downloadedDataType.equalsIgnoreCase(AppConstant.EbWiseDownloading)) {
            if (locationItem != null) {
                if (locationItem.getRuralUrban() != null && locationItem.getRuralUrban().equalsIgnoreCase("R")) {
                    locationStr = context.getResources().getString(R.string.village_) + locationItem.getVtName() + "," + context.getResources().getString(R.string.Ward_) + locationItem.getWardCode();
                } else {
                    locationStr = context.getResources().getString(R.string.Town_) + locationItem.getVtName() + "," + context.getResources().getString(R.string.Ward_) + locationItem.getWardCode();
                }
                header1TV.setText(locationStr);
                //  if(location.getBlockCode()!=null)
                header2TV.setText(context.getResources().getString(R.string.enumerationBlock) + locationItem.getBlockCode());
            }
        } else if (downloadedDataType != null && downloadedDataType.equalsIgnoreCase(AppConstant.VillageWiseDownloading)) {

            if (locationItem != null) {
                if (locationItem.getRuralUrban() != null && locationItem.getRuralUrban().equalsIgnoreCase("R")) {
                    locationStr = context.getResources().getString(R.string.village_) + locationItem.getVtName() /*+ "," + context.getResources().getString(R.string.Ward_) + locationItem.getWardCode()*/;
                } else {
                    locationStr = context.getResources().getString(R.string.Town_) + locationItem.getVtName() /*+ "," + context.getResources().getString(R.string.Ward_) + locationItem.getWardCode()*/;
                }
                header1TV.setText(locationStr);
                //  if(location.getBlockCode()!=null)
                header2TV.setText("Village #" + locationItem.getVtCode());
            }

        } else if (downloadedDataType != null && downloadedDataType.equalsIgnoreCase(AppConstant.WardWiseDownloading)) {

            if (locationItem != null) {
                if (locationItem.getRuralUrban() != null && locationItem.getRuralUrban().equalsIgnoreCase("R")) {
                    locationStr = context.getResources().getString(R.string.ward) + locationItem.getWardCode() /*+ "," + context.getResources().getString(R.string.Ward_) + locationItem.getWardCode()*/;
                } else {
                    locationStr = context.getResources().getString(R.string.Town_) + locationItem.getVtName() /*+ "," + context.getResources().getString(R.string.Ward_) + locationItem.getWardCode()*/;
                }
                header1TV.setText(locationStr);
                //  if(location.getBlockCode()!=null)
                header2TV.setText("Ward #" + locationItem.getWardCode());
            }

        } else if (downloadedDataType != null && downloadedDataType.equalsIgnoreCase(AppConstant.SubBlockWiseDownloading)) {

            if (locationItem != null) {
                if (locationItem.getRuralUrban() != null && locationItem.getRuralUrban().equalsIgnoreCase("R")) {
                    locationStr = "Sub Block" + locationItem.getSubBlockcode() /*+ "," + context.getResources().getString(R.string.Ward_) + locationItem.getWardCode()*/;
                } else {
                    locationStr = context.getResources().getString(R.string.Town_) + locationItem.getVtName() /*+ "," + context.getResources().getString(R.string.Ward_) + locationItem.getWardCode()*/;
                }
                header1TV.setText(locationStr);
                //  if(location.getBlockCode()!=null)
                header2TV.setText("Sub Block #" + locationItem.getSubBlockcode());
            }

        }


        if (rsbySoursce.equalsIgnoreCase("N") && seccDataSource.equalsIgnoreCase("Y")) {

            appConfigWithSeccDataSource();

        } else if (rsbySoursce.equalsIgnoreCase("Y") && seccDataSource.equalsIgnoreCase("N")) {
            header2TV.setText("Village #" + locationItem.getVtCode());
            appConfigWithRsbyDataSource();

        } else if (rsbySoursce.equalsIgnoreCase("Y") && seccDataSource.equalsIgnoreCase("Y")) {

            appConfigWithBothDataSource();

        } else {

            appConfigWithBothDataSource();

        }


        pendingHouseholdBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPendingFragment();
            }
        });
        partialVerifiedBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // openPartialFragment();
                // openPendingFragment();
                openRsbyDashboard();

            }

        });
        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String navigation = ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.homeNavigation, context);
                Intent intent = null;
                if (navigation != null && !navigation.equalsIgnoreCase("")) {
                    if (navigation.equalsIgnoreCase(AppConstant.downloadActivityNavigation)) {
                        intent = new Intent(context, DownloadedListActvity.class);
                        startActivity(intent);
                        rightTransition();
                        finish();
                    } else if (navigation.equalsIgnoreCase(AppConstant.blockDetailActivityNavigation)) {
                        intent = new Intent(context, BlockDetailActivity.class);
                        startActivity(intent);
                        rightTransition();
                        finish();
                    }
                }


            }
        });
        memberStatusBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMemberStatusDashboard();
            }
        });


    }

    private void setupWithOutZoomScreen() {
        showNotification();
        downloadedDataType = ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.DOWNLOADINGSOURCE, context);
        YELLOW = AppUtility.getColor(context, R.color.yellow);
        VOILET = AppUtility.getColor(context, R.color.tab_color);
        WHITE = AppUtility.getColor(context, R.color.white_shine);
        BLACK = AppUtility.getColor(context, R.color.black_shine);
        status = ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.DASHBOARD_TAB_STATUS, context);

        verifierDetail = VerifierLoginResponse.create(ProjectPrefrence.
                getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.VERIFIER_CONTENT, context));
        downloadedLocation = VerifierLocationItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF
                , AppConstant.SELECTED_BLOCK, context));
        locationItem = VerifierLocationItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SELECTED_BLOCK, context));
        header1TV = (TextView) findViewById(R.id.header1TV);
        header2TV = (TextView) findViewById(R.id.header2TV);
        backIV = (ImageView) findViewById(R.id.back);
        pendingHouseholdBT = (Button) findViewById(R.id.notVerifiedFamilyBT);
        partialVerifiedBT = (Button) findViewById(R.id.partialVerifiedBT);
        memberStatusBT = (Button) findViewById(R.id.memberStatusBT);
        seccDashboardAppConfigBT = (Button) findViewById(R.id.seccDashboardAppConfigBT);
        partialVerifiedAppConfigBT = (Button) findViewById(R.id.partialVerifiedAppConfigBT);

        findAllHousehold();
        backIV.setVisibility(View.VISIBLE);

        String locationStr = "";

        if (downloadedDataType != null && downloadedDataType.equalsIgnoreCase(AppConstant.EbWiseDownloading)) {
            if (locationItem != null) {
                if (locationItem.getRuralUrban() != null && locationItem.getRuralUrban().equalsIgnoreCase("R")) {
                    locationStr = context.getResources().getString(R.string.village_) + locationItem.getVtName() + "," + context.getResources().getString(R.string.Ward_) + locationItem.getWardCode();
                } else {
                    locationStr = context.getResources().getString(R.string.Town_) + locationItem.getVtName() + "," + context.getResources().getString(R.string.Ward_) + locationItem.getWardCode();
                }
                header1TV.setText(locationStr);
                //  if(location.getBlockCode()!=null)
                header2TV.setText(context.getResources().getString(R.string.enumerationBlock) + locationItem.getBlockCode());
            }
        } else if (downloadedDataType != null && downloadedDataType.equalsIgnoreCase(AppConstant.VillageWiseDownloading)) {

            if (locationItem != null) {
                if (locationItem.getRuralUrban() != null && locationItem.getRuralUrban().equalsIgnoreCase("R")) {
                    locationStr = context.getResources().getString(R.string.village_) + locationItem.getVtName() /*+ "," + context.getResources().getString(R.string.Ward_) + locationItem.getWardCode()*/;
                } else {
                    locationStr = context.getResources().getString(R.string.Town_) + locationItem.getVtName() /*+ "," + context.getResources().getString(R.string.Ward_) + locationItem.getWardCode()*/;
                }
                header1TV.setText(locationStr);
                //  if(location.getBlockCode()!=null)
                header2TV.setText("Village #" + locationItem.getVtCode());
            }

        } else if (downloadedDataType != null && downloadedDataType.equalsIgnoreCase(AppConstant.WardWiseDownloading)) {

            if (locationItem != null) {
                if (locationItem.getRuralUrban() != null && locationItem.getRuralUrban().equalsIgnoreCase("R")) {
                    locationStr = context.getResources().getString(R.string.ward) + locationItem.getWardCode() /*+ "," + context.getResources().getString(R.string.Ward_) + locationItem.getWardCode()*/;
                } else {
                    locationStr = context.getResources().getString(R.string.Town_) + locationItem.getVtName() /*+ "," + context.getResources().getString(R.string.Ward_) + locationItem.getWardCode()*/;
                }
                header1TV.setText(locationStr);
                //  if(location.getBlockCode()!=null)
                header2TV.setText("Ward #" + locationItem.getWardCode());
            }

        } else if (downloadedDataType != null && downloadedDataType.equalsIgnoreCase(AppConstant.SubBlockWiseDownloading)) {

            if (locationItem != null) {
                if (locationItem.getRuralUrban() != null && locationItem.getRuralUrban().equalsIgnoreCase("R")) {
                    locationStr = "Sub Block" + locationItem.getSubBlockcode() /*+ "," + context.getResources().getString(R.string.Ward_) + locationItem.getWardCode()*/;
                } else {
                    locationStr = context.getResources().getString(R.string.Town_) + locationItem.getVtName() /*+ "," + context.getResources().getString(R.string.Ward_) + locationItem.getWardCode()*/;
                }
                header1TV.setText(locationStr);
                //  if(location.getBlockCode()!=null)
                header2TV.setText("Sub Block #" + locationItem.getSubBlockcode());
            }

        }
     /*   if (dataSoursce.equalsIgnoreCase("B")) {
            searchMenuAppcongigBoth();
            pendingHouseholdBT.setVisibility(View.VISIBLE);
            partialVerifiedBT.setVisibility(View.VISIBLE);
            memberStatusBT.setVisibility(View.GONE);
            seccDashboardAppConfigBT.setVisibility(View.GONE);
            if (status != null) {
                if (status.equalsIgnoreCase(SECC_DASHBOARD)) {
                    openPendingFragment();
                } else if (status.equalsIgnoreCase(RSBYDASHBOARD)) {
                    openRsbyDashboard();
                } else {
                    openMemberStatusDashboard();
                }
            } else {
                openPendingFragment();
            }
        } else*/
        if (rsbySoursce.equalsIgnoreCase("N") && seccDataSource.equalsIgnoreCase("Y")) {

            appConfigWithSeccDataSource();

        } else if (rsbySoursce.equalsIgnoreCase("Y") && seccDataSource.equalsIgnoreCase("N")) {
            header2TV.setText("Village #" + locationItem.getVtCode());
            appConfigWithRsbyDataSource();

        } else if (rsbySoursce.equalsIgnoreCase("Y") && seccDataSource.equalsIgnoreCase("Y")) {

            appConfigWithBothDataSource();

        } else {

            appConfigWithBothDataSource();

        }


        pendingHouseholdBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPendingFragment();
            }
        });
        partialVerifiedBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // openPartialFragment();
                // openPendingFragment();
                openRsbyDashboard();

            }

        });
        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String navigation = ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.homeNavigation, context);
                Intent intent = null;
                if (navigation != null && !navigation.equalsIgnoreCase("")) {
                    if (navigation.equalsIgnoreCase(AppConstant.downloadActivityNavigation)) {
                        intent = new Intent(context, DownloadedListActvity.class);
                        startActivity(intent);
                        rightTransition();
                        finish();
                    } else if (navigation.equalsIgnoreCase(AppConstant.blockDetailActivityNavigation)) {
                        intent = new Intent(context, BlockDetailActivity.class);
                        startActivity(intent);
                        rightTransition();
                        finish();
                    }
                }


            }
        });
        memberStatusBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMemberStatusDashboard();
            }
        });


    }

    private void openPendingFragment() {
        filterNav = 0;
        ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.DASHBOARD_TAB_STATUS, SECC_DASHBOARD, context);
        pendingHouseholdBT.setBackgroundColor(YELLOW);
        pendingHouseholdBT.setTextColor(BLACK);
        memberStatusBT.setTextColor(WHITE);
        memberStatusBT.setBackgroundColor(VOILET);
        partialVerifiedBT.setTextColor(WHITE);
        partialVerifiedBT.setBackgroundColor(VOILET);

        // verifiedBT.setTextColor(WHITE_SHINE);
        FragmentTransaction transaction = fragMgr.beginTransaction();
        if (seccdashboardFragment != null) {
            transaction.detach(seccdashboardFragment);
            seccdashboardFragment = null;
            fragment = null;
        }
        seccdashboardFragment = new HouseholdStatusBoardFragment();
        fragment = seccdashboardFragment;
        transaction.replace(R.id.fragContainer, fragment);
        transaction.commitAllowingStateLoss();
    }

    private void openMemberStatusDashboard() {
        ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.DASHBOARD_TAB_STATUS, MEMBER_DASHBOARD, context);
        memberStatusBT.setBackgroundColor(YELLOW);
        pendingHouseholdBT.setBackgroundColor(VOILET);
        pendingHouseholdBT.setTextColor(WHITE);
        memberStatusBT.setTextColor(BLACK);
        // verifiedBT.setTextColor(WHITE_SHINE);
        FragmentTransaction transaction = fragMgr.beginTransaction();
        if (memberStatusFragment != null) {
            transaction.detach(memberStatusFragment);
            memberStatusFragment = null;
            fragment = null;
        }
        memberStatusFragment = new MemberStatusDadhboardFragment();
        fragment = memberStatusFragment;
        transaction.replace(R.id.fragContainer, fragment);
        transaction.commitAllowingStateLoss();
    }

    private void searchMenuAppcongigSeccOnly() {
        RelativeLayout menuLayout = (RelativeLayout) findViewById(R.id.menuLayout);
        menuLayout.setVisibility(View.VISIBLE);

        final ImageView settings = (ImageView) findViewById(R.id.settings);
        //  settings.setVisibility(View.GONE);
        menuLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settings.performClick();
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(context, settings);
                popup.getMenuInflater()
                        .inflate(R.menu.search_menu_secconly, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {

                            case R.id.searchFamily:
                                //   String searchText = searchFamilyMemberET.getText().toString();
                                Intent theIntent = new Intent(context, SearchOptionActivity.class);
                                // theIntent.putExtra("SearchText", searchText);
                                startActivity(theIntent);
                                leftTransition();
                                break;
                            case R.id.syncRsby:
                                //   String searchText = searchFamilyMemberET.getText().toString();
                                theIntent = new Intent(context, RsbySyncHouseholdActivity.class);
                                // theIntent.putExtra("SearchText", searchText);
                                startActivity(theIntent);
                                leftTransition();
                                break;
                            case R.id.profile:
                                theIntent = new Intent(context, ProfileActivity.class);
                                // theIntent.putExtra("SearchText", searchText);
                                startActivity(theIntent);
                                leftTransition();
                                break;
                            case R.id.home:

                                String navigation = ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.homeNavigation, context);
                                Intent intent = null;
                                if (navigation != null && !navigation.equalsIgnoreCase("")) {
                                    if (navigation.equalsIgnoreCase(AppConstant.downloadActivityNavigation)) {
                                        intent = new Intent(context, DownloadedListActvity.class);
                                        startActivity(intent);
                                    } else if (navigation.equalsIgnoreCase(AppConstant.blockDetailActivityNavigation)) {
                                        intent = new Intent(context, BlockDetailActivity.class);
                                        startActivity(intent);
                                    }
                                }


                                //finish();
                                break;
                            /*case R.id.validateAaadhaar:
                                if(isNetworkAvailable()) {
                                    if (findPendngAaadhar() != null && findPendngAaadhar().size() > 0) {
                                        validateAadhaar();
                                    } else {
                                        CustomAlert.alertWithOk(context, "There are no pending aadhaar to validate");
                                    }
                                }else{
                                    CustomAlert.alertWithOk(context,getResources()
                                            .getString(R.string.internet_connection_msg));
                                }
                                break;*/
                            case R.id.sync:
                                //   String searchText = searchFamilyMemberET.getText().toString();
                                if (isNetworkAvailable()) {
                                    if (totalHouseHold != null && totalHouseHold.size() > 0) {
                                        if (findReadyToSyncHousehold() != null && findReadyToSyncHousehold().size() > 0) {
                                            theIntent = new Intent(context, SyncHouseholdActivity.class);
                                            // theIntent.putExtra("SearchText", searchText);
                                            //  startActivityForResult(theIntent, SYNC_REQUEST);
                                            startActivity(theIntent);
                                            leftTransition();
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
                                break;
                            case R.id.rsbyFamily:
                              /*  Intent theIntent = new Intent(context, rsbyread.class);
                                startActivityForResult(theIntent,AppConstant.rsbyDataCode);
                                leftTransition();*/
                                Intent rsbyIntent = new Intent(context, RsbyCardReadingActivity.class);
                                // startActivityForResult(rsbyIntent, AppConstant.rsbyDataCode);
                                startActivity(rsbyIntent);
                                leftTransition();
                                break;
                            case R.id.logout:
                                Intent loginIntent;
                              /*  if (verifierDetail != null && verifierDetail.getAadhaarNumber() != null) {*/
                                loginIntent = new Intent(context, LoginActivity.class);
                                /*} else {
                                    loginIntent = new Intent(context, NonAdharLoginActivity.class);
                                }*/
                                loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(loginIntent);
                                rightTransition();
                                break;
                            case R.id.assignedLoc:
                                theIntent = new Intent(context, AssignedLocationActivity.class);
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

        menuLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settings.performClick();
            }
        });
    }

    private void searchMenuAppcongigBoth() {
        RelativeLayout menuLayout = (RelativeLayout) findViewById(R.id.menuLayout);
        menuLayout.setVisibility(View.VISIBLE);

        final ImageView settings = (ImageView) findViewById(R.id.settings);
        //  settings.setVisibility(View.GONE);
        menuLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settings.performClick();
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(context, settings);
                popup.getMenuInflater()
                        .inflate(R.menu.search_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {

                            case R.id.searchFamily:
                                //   String searchText = searchFamilyMemberET.getText().toString();
                                Intent theIntent = new Intent(context, SearchOptionActivity.class);
                                // theIntent.putExtra("SearchText", searchText);
                                startActivity(theIntent);
                                leftTransition();
                                break;
                            case R.id.syncRsby:
                                //   String searchText = searchFamilyMemberET.getText().toString();
                                theIntent = new Intent(context, RsbySyncHouseholdActivity.class);
                                // theIntent.putExtra("SearchText", searchText);
                                startActivity(theIntent);
                                leftTransition();
                                break;
                            case R.id.profile:
                                theIntent = new Intent(context, ProfileActivity.class);
                                // theIntent.putExtra("SearchText", searchText);
                                startActivity(theIntent);
                                leftTransition();
                                break;
                            case R.id.home:

                                String navigation = ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.homeNavigation, context);
                                Intent intent = null;
                                if (navigation != null && !navigation.equalsIgnoreCase("")) {
                                    if (navigation.equalsIgnoreCase(AppConstant.downloadActivityNavigation)) {
                                        intent = new Intent(context, DownloadedListActvity.class);
                                        startActivity(intent);
                                    } else if (navigation.equalsIgnoreCase(AppConstant.blockDetailActivityNavigation)) {
                                        intent = new Intent(context, BlockDetailActivity.class);
                                        startActivity(intent);
                                    }
                                }


                                //finish();
                                break;
                            /*case R.id.validateAaadhaar:
                                if(isNetworkAvailable()) {
                                    if (findPendngAaadhar() != null && findPendngAaadhar().size() > 0) {
                                        validateAadhaar();
                                    } else {
                                        CustomAlert.alertWithOk(context, "There are no pending aadhaar to validate");
                                    }
                                }else{
                                    CustomAlert.alertWithOk(context,getResources()
                                            .getString(R.string.internet_connection_msg));
                                }
                                break;*/
                            case R.id.sync:
                                //   String searchText = searchFamilyMemberET.getText().toString();
                                if (isNetworkAvailable()) {
                                    if (totalHouseHold != null && totalHouseHold.size() > 0) {
                                        if (findReadyToSyncHousehold() != null && findReadyToSyncHousehold().size() > 0) {
                                            theIntent = new Intent(context, SyncHouseholdActivity.class);
                                            // theIntent.putExtra("SearchText", searchText);
                                            //  startActivityForResult(theIntent, SYNC_REQUEST);
                                            startActivity(theIntent);
                                            leftTransition();
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
                                break;
                            case R.id.rsbyFamily:
                              /*  Intent theIntent = new Intent(context, rsbyread.class);
                                startActivityForResult(theIntent,AppConstant.rsbyDataCode);
                                leftTransition();*/
                                Intent rsbyIntent = new Intent(context, RsbyCardReadingActivity.class);
                                // startActivityForResult(rsbyIntent, AppConstant.rsbyDataCode);
                                startActivity(rsbyIntent);
                                leftTransition();
                                break;
                            case R.id.logout:
                                Intent loginIntent;
                               /* if (verifierDetail != null && verifierDetail.getAadhaarNumber() != null) {*/
                                loginIntent = new Intent(context, LoginActivity.class);
                                /*} else {
                                    loginIntent = new Intent(context, NonAdharLoginActivity.class);
                                }*/
                                loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(loginIntent);
                                rightTransition();
                                break;
                            case R.id.assignedLoc:
                                theIntent = new Intent(context, AssignedLocationActivity.class);
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

        menuLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settings.performClick();
            }
        });
    }

    private void searchMenuAppcongigRsbyOnly() {
        RelativeLayout menuLayout = (RelativeLayout) findViewById(R.id.menuLayout);
        menuLayout.setVisibility(View.VISIBLE);

        final ImageView settings = (ImageView) findViewById(R.id.settings);
        //  settings.setVisibility(View.GONE);
        menuLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settings.performClick();
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(context, settings);
                popup.getMenuInflater()
                        .inflate(R.menu.menu_only_rsby, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {

                            case R.id.searchFamily:
                                //   String searchText = searchFamilyMemberET.getText().toString();
                                Intent theIntent = new Intent(context, SearchOptionActivity.class);
                                // theIntent.putExtra("SearchText", searchText);
                                startActivity(theIntent);
                                leftTransition();
                                break;
                            case R.id.syncRsby:
                                //   String searchText = searchFamilyMemberET.getText().toString();
                                theIntent = new Intent(context, RsbySyncHouseholdActivity.class);
                                // theIntent.putExtra("SearchText", searchText);
                                startActivity(theIntent);
                                leftTransition();
                                break;
                            case R.id.profile:
                                theIntent = new Intent(context, ProfileActivity.class);
                                // theIntent.putExtra("SearchText", searchText);
                                startActivity(theIntent);
                                leftTransition();
                                break;
                            case R.id.home:

                                String navigation = ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.homeNavigation, context);
                                Intent intent = null;
                                if (navigation != null && !navigation.equalsIgnoreCase("")) {
                                    if (navigation.equalsIgnoreCase(AppConstant.downloadActivityNavigation)) {
                                        intent = new Intent(context, DownloadedListActvity.class);
                                        startActivity(intent);
                                    } else if (navigation.equalsIgnoreCase(AppConstant.blockDetailActivityNavigation)) {
                                        intent = new Intent(context, BlockDetailActivity.class);
                                        startActivity(intent);
                                    }
                                }


                                //finish();
                                break;
                            /*case R.id.validateAaadhaar:
                                if(isNetworkAvailable()) {
                                    if (findPendngAaadhar() != null && findPendngAaadhar().size() > 0) {
                                        validateAadhaar();
                                    } else {
                                        CustomAlert.alertWithOk(context, "There are no pending aadhaar to validate");
                                    }
                                }else{
                                    CustomAlert.alertWithOk(context,getResources()
                                            .getString(R.string.internet_connection_msg));
                                }
                                break;*/
                            case R.id.sync:
                                //   String searchText = searchFamilyMemberET.getText().toString();
                                if (isNetworkAvailable()) {
                                    if (totalHouseHold != null && totalHouseHold.size() > 0) {
                                        if (findReadyToSyncHousehold() != null && findReadyToSyncHousehold().size() > 0) {
                                            theIntent = new Intent(context, SyncHouseholdActivity.class);
                                            // theIntent.putExtra("SearchText", searchText);
                                            //  startActivityForResult(theIntent, SYNC_REQUEST);
                                            startActivity(theIntent);
                                            leftTransition();
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
                                break;
                            case R.id.rsbyFamily:
                              /*  Intent theIntent = new Intent(context, rsbyread.class);
                                startActivityForResult(theIntent,AppConstant.rsbyDataCode);
                                leftTransition();*/
                                Intent rsbyIntent = new Intent(context, RsbyCardReadingActivity.class);
                                // startActivityForResult(rsbyIntent, AppConstant.rsbyDataCode);
                                startActivity(rsbyIntent);
                                leftTransition();
                                break;
                            case R.id.logout:
                                Intent loginIntent;
                               /* if (verifierDetail != null && verifierDetail.getAadhaarNumber() != null) {*/
                                loginIntent = new Intent(context, LoginActivity.class);
                                /*} else {
                                    loginIntent = new Intent(context, NonAdharLoginActivity.class);
                                }*/
                                loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(loginIntent);
                                rightTransition();
                                break;
                            case R.id.assignedLoc:
                                theIntent = new Intent(context, AssignedLocationActivity.class);
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

        menuLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settings.performClick();
            }
        });
    }


    private void openSeccMember(int tabIndex) {
        Intent theIntent = new Intent(context, NHPSMemberActivity.class);
        theIntent.putExtra(AppConstant.MEMBER_TAB, tabIndex);
        startActivity(theIntent);
        leftTransition();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SYNC_REQUEST) {
            SearchActivityWithHouseHold.this.recreate();
        }/* else if (requestCode == AppConstant.rsbyDataCode) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra(AppConstant.RSBYCARDRESPONSE);
                loadDataOfCard(result);

            }
        }*/

    }


    public void findAllHousehold() {
        if (downloadedLocation != null) {
            totalHouseHold = SeccDatabase.getHouseHoldList(downloadedLocation.getStateCode()
                    , downloadedLocation.getDistrictCode()
                    , downloadedLocation.getTehsilCode(), downloadedLocation.getVtCode(),
                    downloadedLocation.getWardCode(), downloadedLocation.getBlockCode(), context);

        }

    }

    private ArrayList<HouseHoldItem> findReadyToSyncHousehold() {
        ArrayList<HouseHoldItem> list = new ArrayList<>();
        for (HouseHoldItem item : totalHouseHold) {
            AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Ready To Sync household : " + item.getLockSave() + "  Name :" + item.getName());
         /*   if(item.getError_code()!=null){
                list.add(item);
            }else {*/
            if (item.getSyncedStatus() != null && item.getSyncedStatus().equalsIgnoreCase(AppConstant.SYNCED_STATUS)) {

            } else {
                if (item.getLockSave() != null && item.getLockSave().equalsIgnoreCase(AppConstant.LOCKED + "")) {
                    AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Ready To Sync household : " + item.getName());
                    list.add(item);
                    /*if (checkHouseholdIsReadyToSync(item) != null) {
                        AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Ready To Sync household : " + item.getName());

                    }*/
                }
                //   }
            }
        }
        return list;
    }

    private void openRsbyDashboard() {
        filterNav = 1;
        ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.DASHBOARD_TAB_STATUS, RSBYDASHBOARD, context);
        //    ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.HOUSEHOLD_TAB_STATUS, 2 + "", context);
        partialVerifiedBT.setBackgroundColor(YELLOW);
        partialVerifiedBT.setTextColor(BLACK);
        pendingHouseholdBT.setTextColor(WHITE);
        pendingHouseholdBT.setBackgroundColor(VOILET);
        // verifiedBT.setTextColor(WHITE_SHINE);
        FragmentTransaction transaction = fragMgr.beginTransaction();
        if (rsbyHouseholdStatusBoardFragment != null) {
            transaction.detach(rsbyHouseholdStatusBoardFragment);
            rsbyHouseholdStatusBoardFragment = null;
            fragment = null;
        }
        rsbyHouseholdStatusBoardFragment = new HouseholdStatusBoardRsbyFragment();
        fragment = rsbyHouseholdStatusBoardFragment;
        transaction.replace(R.id.fragContainer, fragment);
        transaction.commitAllowingStateLoss();
    }

    private ArrayList<SeccMemberItem> findPendngAaadhar() {
        ArrayList<SeccMemberItem> items = new ArrayList<>();
        ArrayList<SeccMemberItem> pendingAaadhaarList = SeccDatabase.getSeccMemberList(locationItem.getStateCode(),
                locationItem.getDistrictCode(), locationItem.getTehsilCode(), locationItem.getVtCode(), locationItem.getWardCode(), locationItem.getBlockCode(), context);
        for (SeccMemberItem item : pendingAaadhaarList) {
            if (item.getAadhaarAuth() != null && item.getAadhaarAuth().trim().equalsIgnoreCase(AppConstant.PENDING_STATUS)) {
                items.add(item);
            }
        }
        return items;
    }

    private void validateAadhaar() {

        TaskListener taskListener = new TaskListener() {
            @Override
            public void execute() {

          /*  ArrayList<SeccMemberItem> pendingAaadhaarList=SeccDatabase.getSeccMemberList(locationItem.getStateCode(),
                    locationItem.getDistrictCode(),locationItem.getTehsilCode(),locationItem.getVtCode(),locationItem.getWardCode(),locationItem.getBlockCode(),context);
*/
                ArrayList<SeccMemberItem> pendingAaadhaarList = findPendngAaadhar();
                pendingCount = 0;
                validatedCount = 0;
                invalidCount = 0;
                for (SeccMemberItem item : pendingAaadhaarList) {
                    if (item.getAadhaarAuth() != null && item.getAadhaarAuth().trim().equalsIgnoreCase(AppConstant.PENDING_STATUS)) {
                        pendingCount++;
                        HouseHoldItem houseHoldItem = null;
                        for (HouseHoldItem item1 : totalHouseHold) {
                            if (item.getHhdNo().trim().equalsIgnoreCase(item1.getHhdNo().trim())) {
                                houseHoldItem = item1;
                                break;
                            }
                        }
                        AadhaarAuthRequestItem requestItem = new AadhaarAuthRequestItem();
                        requestItem.setUid(item.getAadhaarNo().trim());
                        requestItem.setName(item.getNameAadhaar().trim());
                        String imei = AppUtility.getIMEINumber(context);
                        if (imei != null) {
                            requestItem.setImeiNo(imei);
                        }
                        requestItem.setProject(AppConstant.PROJECT_NAME);
                        requestItem.setUserName(ApplicationGlobal.AADHAAR_AUTH_USERNAME);
                        requestItem.setUserPass(ApplicationGlobal.AADHAAR_AUTH_ENCRIPTED_PASSWORD);
                        String response = CustomHttpClient.postStringRequestWithTimeOut(AppConstant.AADHAAR_DEMO_AUTH_API_NEW, requestItem.serialize());
                        DemoAuthResponseItem demoAuthItem = DemoAuthResponseItem.create(response);
                        if (demoAuthItem != null) {
                            if (demoAuthItem.getRet() != null) {
                                if (demoAuthItem.getRet().trim().equalsIgnoreCase(AppConstant.VALID_STATUS)) {
                                    validatedCount++;
                                    item.setAadhaarAuth(demoAuthItem.getRet().trim());
                                    item.setError_code(null);
                                    item.setError_msg(null);
                                    item.setError_type(null);
                                    SeccDatabase.updateSeccMember(item, context);
                                } else if (demoAuthItem.getRet().trim().equalsIgnoreCase(AppConstant.INVALID_STATUS)) {
                                    invalidCount++;
                                    item.setAadhaarAuth(demoAuthItem.getRet().trim());
                                    item.setError_code(AppConstant.AADHAAR_VALIDATION_ERROR);
                                    item.setError_msg(AppConstant.INVALID_AADHAAR_MG);
                                    item.setError_type(AppConstant.AADHAAR_VALIDATION_ERROR);
                                    houseHoldItem.setError_code(AppConstant.AADHAAR_VALIDATION_ERROR);
                                    houseHoldItem.setError_msg(AppConstant.INVALID_AADHAAR_MG);
                                    houseHoldItem.setError_type(AppConstant.AADHAAR_VALIDATION_ERROR);
                                    SeccDatabase.updateHouseHold(houseHoldItem, context);
                                    SeccDatabase.updateSeccMember(item, context);
                                }
                            } else {
                                invalidCount++;
                            }
                        } else {
                            invalidCount++;
                        }

                    }
                }

            }

            @Override
            public void updateUI() {
                //  SearchActivityWithHouseHold.this.recreate();
                CustomAlert.alertWithOk(context, context.getResources().getString(R.string.validateSucess) + validatedCount + "\n" + context.getResources().getString(R.string.validateFail) + invalidCount);
                if (status != null) {
                    if (status.equalsIgnoreCase(SECC_DASHBOARD)) {
                        openPendingFragment();
                    } else {
                        openMemberStatusDashboard();
                    }
                } else {
                    openPendingFragment();
                }
            }
        };

        if (asyncTask != null && !asyncTask.isCancelled()) {
            asyncTask.cancel(true);
            asyncTask = null;
        }
        asyncTask = new CustomAsyncTask(taskListener, context.getResources().getString(R.string.please_wait), context);
        asyncTask.execute();
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

    private void appConfigWithSeccDataSource() {
        searchMenuAppcongigSeccOnly();
        pendingHouseholdBT.setVisibility(View.GONE);
        partialVerifiedBT.setVisibility(View.GONE);
        memberStatusBT.setVisibility(View.GONE);
        partialVerifiedAppConfigBT.setVisibility(View.GONE);
        seccDashboardAppConfigBT.setVisibility(View.VISIBLE);
        openPendingFragment();
        seccDashboardAppConfigBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPendingFragment();
            }
        });
    }

    private void appConfigWithRsbyDataSource() {

        searchMenuAppcongigRsbyOnly();
        pendingHouseholdBT.setVisibility(View.GONE);
        partialVerifiedBT.setVisibility(View.GONE);
        memberStatusBT.setVisibility(View.GONE);
        partialVerifiedAppConfigBT.setVisibility(View.VISIBLE);
        seccDashboardAppConfigBT.setVisibility(View.GONE);
        openRsbyDashboard();
        partialVerifiedBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRsbyDashboard();
            }
        });
    }

    private void appConfigWithBothDataSource() {
        searchMenuAppcongigBoth();
        pendingHouseholdBT.setVisibility(View.VISIBLE);
        partialVerifiedBT.setVisibility(View.VISIBLE);
        memberStatusBT.setVisibility(View.GONE);
        partialVerifiedAppConfigBT.setVisibility(View.GONE);
        seccDashboardAppConfigBT.setVisibility(View.GONE);
        String dashBoardStatus = ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.DASHBOARD_TAB_STATUS, context);
        if (dashBoardStatus != null) {
            if (dashBoardStatus.equalsIgnoreCase(RSBYDASHBOARD)) {
                openRsbyDashboard();

            } else {

                openPendingFragment();
            }
        } else {

            openPendingFragment();
        }
        pendingHouseholdBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPendingFragment();
            }
        });
        partialVerifiedAppConfigBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRsbyDashboard();
            }
        });
    }

    private String checkAppConfig() {
        selectedStateItem = StateItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SELECTED_STATE, context));
        ArrayList<ConfigurationItem> configList = SeccDatabase.findConfiguration(selectedStateItem.getStateCode(), context);

        if (configList != null) {
            for (ConfigurationItem item1 : configList) {
                if (item1.getConfigId().equalsIgnoreCase(AppConstant.RSBY_DATA_SOURCE_CONFIG)) {
                    rsbySoursce = item1.getStatus();
                }
                if (item1.getConfigId().equalsIgnoreCase(AppConstant.SECC_DOWNLOAD)) {
                    seccDataSource = item1.getStatus();
                }
                if (item1.getConfigId().equalsIgnoreCase(AppConstant.APPLICATION_ZOOM)) {
                    zoomMode = item1.getStatus();
                }
            }
        }

        return rsbySoursce;
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
                    wrongAttempetCountValue.setText((3 - wrongPinCount)+"");
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

//                    errorTV.setVisibility(View.VISIBLE);
//                    errorTV.setText("Enter correct pin");
//                    pinET.setText("");
//                    // pinET.setHint("Enter 4-di");
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
}
