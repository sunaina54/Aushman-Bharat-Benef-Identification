package com.nhpm.activity;

import android.app.AlertDialog;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.customComponent.CustomAlert;
import com.customComponent.Networking.CustomVolleyGet;
import com.customComponent.Networking.VolleyTaskListener;
import com.customComponent.utility.ProjectPrefrence;
import com.nhpm.BaseActivity;
import com.nhpm.LocalDataBase.dto.SeccDatabase;
import com.nhpm.Models.response.master.ConfigurationItem;
import com.nhpm.Models.response.master.StateItem;
import com.nhpm.Models.response.master.response.AppUpdatVersionResponse;
import com.nhpm.Models.response.seccMembers.HouseHoldItem;
import com.nhpm.Models.response.verifier.VerifierLocationItem;
import com.nhpm.Models.response.verifier.VerifierLoginResponse;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;
import com.nhpm.Utility.AppUtility;
import com.nhpm.fragments.WithoutSeccDataOfflineFragment;

import java.util.ArrayList;

import pl.polidea.view.ZoomView;

public class DownloadedListActvity extends BaseActivity implements ComponentCallbacks2 {
    private Context context;
    private RecyclerView blockList;
    private ArrayList<VerifierLocationItem> dowloadedBlockList;
    private VerifierLoginResponse verifierDetail;
    private CustomAdapter adapter;
    private ImageView backIV;
    private LinearLayout mZoomLinearLayout;
    private ZoomView zoomView;
    private RelativeLayout menuLayout;
    private ImageView settings;
    private String downloadedDataType = "b";
    private String dataSoursce = "Y";
    private String SeccDownloaded = "Y";
    /*private CardView card_view1;
    private Button procedWithRsby;
    */private Fragment fragment;
    private FragmentTransaction fragmentTransection;
    private FragmentManager fragmentManager;
    private FrameLayout fragContainer;
    private String zoomApp = "N";
    private String dataDownload = "V";
    private boolean pinLockIsShown = false;

    private int wrongPinCount = 0;
    private long wrongPinSavedTime;
    private long currentTime;
    private TextView wrongAttempetCountValue, wrongAttempetCountText;
    private long millisecond24 = 86400000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        checkAppConfig();

        if (zoomApp.equalsIgnoreCase("N")) {
            setContentView(R.layout.activity_downloaded_list_actvity);
            setupScreenWithoutZoom();
        } else {
            setContentView(R.layout.dummy_layout_for_zooming);
            setupScreenWithZoom();
        }

    }

    private void setupScreenWithZoom() {

        // SeccDownloaded =  ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.dataDownloaded, context);
        downloadedDataType = ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.DOWNLOADINGSOURCE, context);
        mZoomLinearLayout = (LinearLayout) findViewById(R.id.mZoomLinearLayout);
        View v = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.activity_downloaded_list_actvity, null, false);
        v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
        fragmentManager = getSupportFragmentManager();
        showNotification(v);
        verifierDetail = VerifierLoginResponse.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.VERIFIER_CONTENT, context));
        fragContainer = (FrameLayout) v.findViewById(R.id.fragContainer);
        menuLayout = (RelativeLayout) v.findViewById(R.id.menuLayout);
        menuLayout.setVisibility(View.VISIBLE);
        settings = (ImageView) v.findViewById(R.id.settings);
        settings.setVisibility(View.VISIBLE);
        searchMenu();
        blockList = (RecyclerView) v.findViewById(R.id.locationList);
        backIV = (ImageView) v.findViewById(R.id.back);
        backIV.setVisibility(View.GONE);
        zoomView = new ZoomView(this);
        zoomView.addView(v);
        mZoomLinearLayout.addView(zoomView);
        blockList.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        blockList.setLayoutManager(mLayoutManager);
        prepareBlockForDownloaddList();
        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent theInten=new Intent(context,PinLoginActivity.class);
                startActivity(theInten);*/
                finish();
                rightTransition();
            }
        });

        if (SeccDownloaded.equalsIgnoreCase("N")) {

            openWithoutSeccFragment();
        } else {
            fragContainer.setVisibility(View.GONE);
            blockList.setVisibility(View.VISIBLE);
        }

    }

    private void setupScreenWithoutZoom() {

        // SeccDownloaded =  ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.dataDownloaded, context);
        downloadedDataType = ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.DOWNLOADINGSOURCE, context);

        fragmentManager = getSupportFragmentManager();
        showNotification();
        verifierDetail = VerifierLoginResponse.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.VERIFIER_CONTENT, context));
        fragContainer = (FrameLayout) findViewById(R.id.fragContainer);
        menuLayout = (RelativeLayout) findViewById(R.id.menuLayout);
        menuLayout.setVisibility(View.VISIBLE);
        settings = (ImageView) findViewById(R.id.settings);
        settings.setVisibility(View.VISIBLE);
        searchMenu();
        blockList = (RecyclerView) findViewById(R.id.locationList);
        backIV = (ImageView) findViewById(R.id.back);
        backIV.setVisibility(View.GONE);

        blockList.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        blockList.setLayoutManager(mLayoutManager);
        prepareBlockForDownloaddList();
        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent theInten=new Intent(context,PinLoginActivity.class);
                startActivity(theInten);*/
                finish();
                rightTransition();
            }
        });

        if (SeccDownloaded.equalsIgnoreCase("N")) {

            openWithoutSeccFragment();
        } else {
            fragContainer.setVisibility(View.GONE);
            blockList.setVisibility(View.VISIBLE);
        }

    }

    private void prepareBlockForDownloaddList() {
        dowloadedBlockList = new ArrayList<>();
        VerifierLocationItem locItem = null;
        if (downloadedDataType != null && downloadedDataType.equalsIgnoreCase(AppConstant.EbWiseDownloading)) {
            for (VerifierLocationItem item : verifierDetail.getLocationList()) {
                ArrayList<HouseHoldItem> householdList = SeccDatabase.getHouseHoldList(item.getStateCode(), item.getDistrictCode(), item.getTehsilCode(), item.getVtCode(), item.getWardCode(), item.getBlockCode(), context);
                if (householdList.size() > 0) {
                    dowloadedBlockList.add(item);
                }
            }
        } else {

            dowloadedBlockList.add(verifierDetail.getLocationList().get(0));
        }
        if (dowloadedBlockList != null && dowloadedBlockList.size() > 0) {
            adapter = new CustomAdapter(dowloadedBlockList);
            blockList.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    private void searchMenu() {
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
                        .inflate(R.menu.logout_home, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.logout:
                                Intent theIntent;
                               /* if (verifierDetail != null && verifierDetail.getAadhaarNumber() != null) {*/
                                theIntent = new Intent(context, LoginActivity.class);
                               /* } else {
                                    theIntent = new Intent(context, NonAdharLoginActivity.class);
                                }*/
                                theIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                theIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(theIntent);
                                rightTransition();
                                break;
                            case R.id.profile:
                                Intent profileIntent = new Intent(context, ProfileActivity.class);
                                startActivity(profileIntent);
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

    private void checkUpdatedVersion() {
        VolleyTaskListener taskListener = new VolleyTaskListener() {
            @Override
            public void postExecute(String response) {
                AppUpdatVersionResponse respItem = AppUpdatVersionResponse.create(response);

                if (respItem != null) {
                    if (respItem.isStatus()) {
                        int versionCode = Integer.parseInt(respItem.getVersionCode());
                        if (versionCode != findApplicationVersionCode()) {
                            Intent theInten = new Intent(context, AppUpdateActivity.class);
                            startActivity(theInten);
                            finish();
                        } else {
                            if (verifierDetail.getPin() != null && verifierDetail.getPin().equalsIgnoreCase("")) {
                                Intent theIntent = new Intent(context, SetPinActivity.class);
                                startActivity(theIntent);
                                finish();
                                leftTransition();
                            } else {
                                Intent theIntent = new Intent(context, BlockDetailActivity.class);
                                startActivity(theIntent);
                                finish();
                                leftTransition();
                            }
                        }
                    } else {
                        if (verifierDetail.getPin() != null && verifierDetail.getPin().equalsIgnoreCase("")) {
                            Intent theIntent = new Intent(context, SetPinActivity.class);
                            startActivity(theIntent);
                            finish();
                            leftTransition();
                        } else {
                            Intent theIntent = new Intent(context, BlockDetailActivity.class);
                            startActivity(theIntent);
                            finish();
                            leftTransition();
                        }
                    }
                }
            }

            @Override
            public void onError(VolleyError error) {
                if (verifierDetail.getPin() != null && verifierDetail.getPin().equalsIgnoreCase("")) {
                    Intent theIntent = new Intent(context, SetPinActivity.class);
                    startActivity(theIntent);
                    finish();
                    leftTransition();
                } else {
                    Intent theIntent = new Intent(context, BlockDetailActivity.class);
                    startActivity(theIntent);
                    finish();
                    leftTransition();
                }
            }
        };
        CustomVolleyGet volleyGet = new CustomVolleyGet(taskListener, context.getResources().getString(R.string.checkinUpdateVersion), "http://103.241.181.83:8080/nhps_service/nhps/app/updatedVersion", context);
        volleyGet.execute();
    }

    private int findApplicationVersionCode() {
        PackageManager packageManager = context.getPackageManager();
        String packageName = context.getPackageName();
        int versionCode = 0; // initialize String

        try {
            versionCode = packageManager.getPackageInfo(packageName, 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
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
                if (item1.getConfigId().equalsIgnoreCase(AppConstant.RSBY_DATA_SOURCE_CONFIG)) {
                    dataSoursce = item1.getStatus();
                }
                if (item1.getConfigId().equalsIgnoreCase(AppConstant.SECC_DOWNLOAD)) {
                    SeccDownloaded = item1.getStatus();
                }
                if (item1.getConfigId().equalsIgnoreCase(AppConstant.APPLICATION_ZOOM)) {
                    zoomApp = item1.getStatus();
                }
                if (item1.getConfigId().equalsIgnoreCase(AppConstant.DATA_DOWNLOAD)) {
                    downloadedDataType = dataDownload = item1.getStatus();
                }
            }
        }

        return dataSoursce;
    }

    private void proceedDailog(final VerifierLocationItem item) {
        //checkAppConfig();
        final AlertDialog proceedDailog = new AlertDialog.Builder(context).create();
        TextView stateNameTV, distTV, tehsilTV, vtNameTV, wardTV, ebTV;
        TextView blockCodeTV, noOfHouseholdTV, noOfMemberTV, syncedMemberTV, totalSyncedMemberTV, totalRsbyHouseholdTV;
        LayoutInflater factory = LayoutInflater.from(context);
        View alertView = factory.inflate(R.layout.proceed_dialog, null);
        proceedDailog.setView(alertView);
        Button validationBT = (Button) alertView.findViewById(R.id.goValidationBT);
        Button syncBT = (Button) alertView.findViewById(R.id.goSyncBT);
        syncBT.setVisibility(View.GONE);
        Button cancelBT = (Button) alertView.findViewById(R.id.cancelBT);
        Button deleteBT = (Button) alertView.findViewById(R.id.deleteBT);
        stateNameTV = (TextView) alertView.findViewById(R.id.stateNameTV);
        distTV = (TextView) alertView.findViewById(R.id.distNameTV);
        tehsilTV = (TextView) alertView.findViewById(R.id.tehsilNameTV);
        vtNameTV = (TextView) alertView.findViewById(R.id.vtNameTV);
        wardTV = (TextView) alertView.findViewById(R.id.wardCodeTV);
        ebTV = (TextView) alertView.findViewById(R.id.ebTV);

        blockCodeTV = (TextView) alertView.findViewById(R.id.blockCodeTV);
        // blockCodeTV.setPaintFlags(blockCodeTV.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        noOfHouseholdTV = (TextView) alertView.findViewById(R.id.totalHouseholdTV);
        noOfMemberTV = (TextView) alertView.findViewById(R.id.totalMemberTV);
        syncedMemberTV = (TextView) alertView.findViewById(R.id.syncedMemberTV);
        totalSyncedMemberTV = (TextView) alertView.findViewById(R.id.totalSyncMemberTV);
        totalRsbyHouseholdTV = (TextView) alertView.findViewById(R.id.totalRsbyHouseholdTV);
        LinearLayout rsbyLayout = (LinearLayout) alertView.findViewById(R.id.rsbyLayout);

        if (downloadedDataType != null && downloadedDataType.equalsIgnoreCase(AppConstant.VillageWiseDownloading)) {

            noOfHouseholdTV.setText(SeccDatabase.seccHouseHoldVillageCount(context, item.getStateCode(),
                    item.getDistrictCode(), item.getTehsilCode(), item.getVtCode()) + "");
            if (dataSoursce.equalsIgnoreCase("Y")) {
                totalRsbyHouseholdTV.setText(SeccDatabase.rsbyHouseHoldVillageCount(context, item.getStateCode(),
                        item.getDistrictCode(), item.getTehsilCode(), item.getVtCode()) + "");
            } else {
                totalRsbyHouseholdTV.setVisibility(View.GONE);
                rsbyLayout.setVisibility(View.GONE);
            }
            noOfMemberTV.setText(SeccDatabase.seccMemberVillageCount(context, item.getStateCode(),
                    item.getDistrictCode(), item.getTehsilCode(), item.getVtCode()) + "");
            syncedMemberTV.setText(SeccDatabase.countSyncedHouseholdVillage(context, item.getStateCode(),
                    item.getDistrictCode(), item.getTehsilCode(), item.getVtCode(), AppConstant.SYNCED_STATUS + "") + "");
            totalSyncedMemberTV.setText(SeccDatabase.countSyncedMemberVillage(context, item.getStateCode(),
                    item.getDistrictCode(), item.getTehsilCode(), item.getVtCode(), AppConstant.SYNCED_STATUS + "") + "");
        } else if (downloadedDataType != null && downloadedDataType.equalsIgnoreCase(AppConstant.EbWiseDownloading)) {


            noOfHouseholdTV.setText(SeccDatabase.seccHouseHoldCount(context, item.getStateCode(),
                    item.getDistrictCode(), item.getTehsilCode(), item.getVtCode(), item.getWardCode(), item.getBlockCode()) + "");
            if (dataSoursce.equalsIgnoreCase("Y")) {
                totalRsbyHouseholdTV.setText(SeccDatabase.rsbyHouseHoldCount(context, item.getStateCode(),
                        item.getDistrictCode(), item.getTehsilCode(), item.getVtCode(), item.getWardCode(), item.getBlockCode()) + "");
            } else {
                totalRsbyHouseholdTV.setVisibility(View.GONE);
                rsbyLayout.setVisibility(View.GONE);
            }
            noOfMemberTV.setText(SeccDatabase.seccMemberCount(context, item.getStateCode(),
                    item.getDistrictCode(), item.getTehsilCode(), item.getVtCode(), item.getWardCode(), item.getBlockCode()) + "");
            syncedMemberTV.setText(SeccDatabase.countSyncedHousehold(context, item.getStateCode(),
                    item.getDistrictCode(), item.getTehsilCode(), item.getVtCode(), item.getWardCode(), item.getBlockCode(), AppConstant.SYNCED_STATUS + "") + "");
            totalSyncedMemberTV.setText(SeccDatabase.countSyncedMember(context, item.getStateCode(),
                    item.getDistrictCode(), item.getTehsilCode(), item.getVtCode(), item.getWardCode(), item.getBlockCode(), AppConstant.SYNCED_STATUS + "") + "");


        } else if (downloadedDataType != null && downloadedDataType.equalsIgnoreCase(AppConstant.WardWiseDownloading)) {

            noOfHouseholdTV.setText(SeccDatabase.seccHouseHoldWardCount(context, item.getStateCode(),
                    item.getDistrictCode(), item.getTehsilCode(), item.getVtCode(), item.getWardCode()) + "");
            if (dataSoursce.equalsIgnoreCase("Y")) {
                totalRsbyHouseholdTV.setText(SeccDatabase.rsbyHouseHoldWardCount(context, item.getStateCode(),
                        item.getDistrictCode(), item.getTehsilCode(), item.getVtCode(), item.getWardCode()) + "");
            } else {
                totalRsbyHouseholdTV.setVisibility(View.GONE);
                rsbyLayout.setVisibility(View.GONE);
            }
            noOfMemberTV.setText(SeccDatabase.seccMemberWardCount(context, item.getStateCode(),
                    item.getDistrictCode(), item.getTehsilCode(), item.getVtCode(), item.getWardCode()) + "");
            syncedMemberTV.setText(SeccDatabase.countSyncedHouseholdWard(context, item.getStateCode(),
                    item.getDistrictCode(), item.getTehsilCode(), item.getVtCode(), item.getWardCode(), AppConstant.SYNCED_STATUS + "") + "");
            totalSyncedMemberTV.setText(SeccDatabase.countSyncedMemberWard(context, item.getStateCode(),
                    item.getDistrictCode(), item.getTehsilCode(), item.getVtCode(), item.getWardCode(), AppConstant.SYNCED_STATUS + "") + "");

        } else if (downloadedDataType != null && downloadedDataType.equalsIgnoreCase(AppConstant.SubBlockWiseDownloading)) {

            noOfHouseholdTV.setText(SeccDatabase.seccHouseHoldSubEbCount(context, item.getStateCode(),
                    item.getDistrictCode(), item.getTehsilCode(), item.getVtCode(), item.getWardCode(), item.getBlockCode(), item.getSubBlockcode()) + "");
            if (dataSoursce.equalsIgnoreCase("Y")) {
                totalRsbyHouseholdTV.setText(SeccDatabase.rsbyHouseHoldSubEbCount(context, item.getStateCode(),
                        item.getDistrictCode(), item.getTehsilCode(), item.getVtCode(), item.getWardCode(), item.getBlockCode(), item.getSubBlockcode()) + "");
            } else {
                totalRsbyHouseholdTV.setVisibility(View.GONE);
                rsbyLayout.setVisibility(View.GONE);
            }
            noOfMemberTV.setText(SeccDatabase.seccMemberSubEbCount(context, item.getStateCode(),
                    item.getDistrictCode(), item.getTehsilCode(), item.getVtCode(), item.getWardCode(), item.getBlockCode(), item.getSubBlockcode()) + "");
            syncedMemberTV.setText(SeccDatabase.countSyncedHouseholdSubEb(context, item.getStateCode(),
                    item.getDistrictCode(), item.getTehsilCode(), item.getVtCode(), item.getWardCode(), item.getBlockCode(), item.getSubBlockcode(), AppConstant.SYNCED_STATUS + "") + "");
            totalSyncedMemberTV.setText(SeccDatabase.countSyncedMemberSubEb(context, item.getStateCode(),
                    item.getDistrictCode(), item.getTehsilCode(), item.getVtCode(), item.getWardCode(), item.getBlockCode(), item.getSubBlockcode(), AppConstant.SYNCED_STATUS + "") + "");


        }






       /* syncBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SELECTED_BLOCK, item.serialize(), context);
                Intent theIntent = new Intent(context, SyncHouseholdActivity.class);
                startActivityForResult(theIntent, SYNC_REQUEST);
                AppUtility.navgateFromEb = true;
                leftTransition();
                proceedDailog.dismiss();
            }
        });*/
        validationBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Log.d(TAG,"Delete Status"+status);
                ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SELECTED_BLOCK, item.serialize(), context);
                ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.homeNavigation, AppConstant.downloadActivityNavigation, context);
                Intent theIntent = new Intent(context, SearchActivityWithHouseHold.class);
                startActivity(theIntent);
                leftTransition();
                proceedDailog.dismiss();
            }
        });
     /*   deleteBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SeccDatabase.countSurveyedHousehold(context, item, "", "") > 0) {
                    deleteSyncPrompt(item);
                } else {
                    pendingHouseholdDeletePrompt(DELETE_DATA, item);
                }

                proceedDailog.dismiss();
            }
        });*/
        cancelBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                proceedDailog.dismiss();
            }
        });
        proceedDailog.show();
    }

    private class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
        private ArrayList<VerifierLocationItem> mDataset;

        // Provide a suitable constructor (depends on the kind of dataset)
        public CustomAdapter(ArrayList<VerifierLocationItem> myDataset) {
            mDataset = myDataset;
         /*   memberListResponse=SeccMemberResponse.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                    AppConstant.SECC_MEMBER_CONTENT,context));
            Log.d(TAG, "Adapter : " + mDataset.size());*/
        }

        public void add(int position, VerifierLocationItem item) {
            mDataset.add(position, item);
            notifyItemInserted(position);
        }

        public void remove(String item) {
            int position = mDataset.indexOf(item);
            mDataset.remove(position);
            notifyItemRemoved(position);
        }

        public void updateData(ArrayList<VerifierLocationItem> itemList) {
            mDataset.clear();
            mDataset.addAll(itemList);
            notifyDataSetChanged();
        }

        // Create new1 views (invoked by the layout manager)
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent,
                                             int viewType) {
            // create a new1 view
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.location_list, parent, false);
            // set the view's size, margins, paddings and layout parameters
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final VerifierLocationItem item = mDataset.get(position);
            //  holder.blockCodeTV.setText(loc.getBlockCode());
            if (item != null) {
                if (dataDownload != null && dataDownload.equalsIgnoreCase(AppConstant.EbWiseDownloading)) {
                    holder.suvEbLinearLayout.setVisibility(View.GONE);
                } else if (dataDownload != null && dataDownload.equalsIgnoreCase(AppConstant.VillageWiseDownloading)) {
                    holder.ebLinearLayout.setVisibility(View.GONE);
                    holder.wardLinearLayout.setVisibility(View.GONE);
                    holder.suvEbLinearLayout.setVisibility(View.GONE);
                } else if (dataDownload != null && dataDownload.equalsIgnoreCase(AppConstant.SubBlockWiseDownloading)) {

                } else if (dataDownload != null && dataDownload.equalsIgnoreCase(AppConstant.WardWiseDownloading)) {
                    holder.ebLinearLayout.setVisibility(View.GONE);
                    holder.suvEbLinearLayout.setVisibility(View.GONE);
                }




/*
                if (downloadedDataType != null && downloadedDataType.equalsIgnoreCase(AppConstant.EbWiseDownloading)) {
                } else if (downloadedDataType != null && downloadedDataType.equalsIgnoreCase(AppConstant.VillageWiseDownloading)){
                    holder.ebLinearLayout.setVisibility(View.GONE);
                    holder.wardLinearLayout.setVisibility(View.GONE);
                } else if (downloadedDataType != null && downloadedDataType.equalsIgnoreCase(AppConstant.SubBlockWiseDownloading)){
                } else if (downloadedDataType != null && downloadedDataType.equalsIgnoreCase(AppConstant.WardWiseDownloading)) {
                    holder.ebLinearLayout.setVisibility(View.GONE);
                }*/
                if (item.getStateName() != null) {
                    holder.stateNameTV.setText(item.getStateName());
                }
                if (item.getDistrictName() != null) {
                    holder.distTV.setText(item.getDistrictName());
                }
                if (item.getTehsilName() != null) {
                    holder.tehsilTV.setText(item.getTehsilName());
                }
                if (item.getVtName() != null) {
                    holder.vtNameTV.setText(item.getVtName());
                }
                if (item.getWardCode() != null) {
                    holder.wardTV.setText(item.getWardCode());
                }
                if (item.getBlockCode() != null) {

                    holder.ebTV.setText(item.getBlockCode());
                }
                if (item.getSubBlockcode() != null) {
                    holder.subEbTV.setText(item.getSubBlockcode());
                }
            }
            holder.proceedBT.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                            AppConstant.SELECTED_BLOCK, item.serialize(), context);
                    ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.homeNavigation, AppConstant.downloadActivityNavigation, context);
                  /*  Intent theIntent = new Intent(context, SearchActivityWithHouseHold.class);
                    startActivity(theIntent);
                    leftTransition();*/
                    proceedDailog(item);
                    // finish();
                }
            });
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return mDataset.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            TextView stateNameTV, distTV, tehsilTV, vtNameTV, wardTV, ebTV, subEbTV;
            LinearLayout ebLinearLayout, wardLinearLayout, suvEbLinearLayout;
            Button proceedBT;

            public ViewHolder(View alertView) {
                super(alertView);
                // blockCodeTV.setPaintFlags(blockCodeTV.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                proceedBT = (Button) alertView.findViewById(R.id.proceedBT);
                stateNameTV = (TextView) alertView.findViewById(R.id.stateNameTV);
                distTV = (TextView) alertView.findViewById(R.id.distNameTV);
                tehsilTV = (TextView) alertView.findViewById(R.id.tehsilNameTV);
                vtNameTV = (TextView) alertView.findViewById(R.id.vtNameTV);
                wardTV = (TextView) alertView.findViewById(R.id.wardCodeTV);
                ebTV = (TextView) alertView.findViewById(R.id.ebTV);
                subEbTV = (TextView) alertView.findViewById(R.id.subEbTV);
                ebLinearLayout = (LinearLayout) alertView.findViewById(R.id.ebLinearLayout);
                wardLinearLayout = (LinearLayout) alertView.findViewById(R.id.wardLinearLayout);
                suvEbLinearLayout = (LinearLayout) alertView.findViewById(R.id.suvEbLinearLayout);
                // AppUtility.showLog(AppConstant.LOG_STATUS,"Block Activity"," State Name : "+item.getStateName());

                //noOfHouseholdTV.setPaintFlags(noOfHouseholdTV.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
              /*  v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position=getPosition();
                      //  CustomAlert.alertWithOk(context,"Under Development..");
                       openSearchScreen(mDataset.get(position));
                    }
                });*/

            }
        }
    }

    private void openWithoutSeccFragment() {
        blockList.setVisibility(View.GONE);
        fragment = new WithoutSeccDataOfflineFragment();
        fragmentTransection = fragmentManager.beginTransaction();
        fragmentTransection.add(R.id.fragContainer, fragment);
        fragmentTransection.commitAllowingStateLoss();
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
