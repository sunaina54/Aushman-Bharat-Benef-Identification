package com.nhpm.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.customComponent.CustomAlert;
import com.customComponent.CustomAsyncTask;
import com.customComponent.Networking.CustomVolley;
import com.customComponent.Networking.CustomVolleyGet;
import com.customComponent.Networking.CustomVolleyHeaderGet;
import com.customComponent.Networking.VolleyTaskListener;
import com.customComponent.TaskListener;
import com.customComponent.utility.CustomHttp;
import com.customComponent.utility.ProjectPrefrence;
import com.nhpm.BaseActivity;
import com.nhpm.LocalDataBase.DatabaseHelpers;
import com.nhpm.LocalDataBase.dto.CommonDatabase;
import com.nhpm.LocalDataBase.dto.SeccDatabase;
import com.nhpm.Models.DataCountRequest;
import com.nhpm.Models.DownloadedDataCountModel;
import com.nhpm.Models.NotificationModel;
import com.nhpm.Models.response.ApplicationConfigListModel;
import com.nhpm.Models.response.ApplicationConfigurationModel;
import com.nhpm.Models.response.NotificationResponse;
import com.nhpm.Models.response.master.AadhaarStatusItem;
import com.nhpm.Models.response.master.ConfigurationItem;
import com.nhpm.Models.response.master.HealthSchemeItem;
import com.nhpm.Models.response.master.MemberRelationItem;
import com.nhpm.Models.response.master.MemberStatusItem;
import com.nhpm.Models.response.master.RelationMasterItem;
import com.nhpm.Models.response.master.StateItem;
import com.nhpm.Models.response.master.response.AadhaarStatusItemResponse;
import com.nhpm.Models.response.master.response.AppUpdatVersionResponse;
import com.nhpm.Models.response.master.response.HealthSchemeItemResponse;
import com.nhpm.Models.response.master.response.HealthSchemeRequest;
import com.nhpm.Models.response.master.response.MemberStatusItemResponse;
import com.nhpm.Models.response.rsbyMembers.RSBYPoliciesItem;
import com.nhpm.Models.response.rsbyMembers.RsbyCardCategoryItem;
import com.nhpm.Models.response.rsbyMembers.RsbyCardCategoryMasterList;
import com.nhpm.Models.response.rsbyMembers.RsbyPoliciesCompany;
import com.nhpm.Models.response.rsbyMembers.RsbyPoliciesCompanyMasterList;
import com.nhpm.Models.response.rsbyMembers.RsbyPoliciesMasterList;
import com.nhpm.Models.response.rsbyMembers.RsbyRelationItem;
import com.nhpm.Models.response.rsbyMembers.RsbyRelationMasterList;
import com.nhpm.Models.response.seccMembers.DataCountModel;
import com.nhpm.Models.response.seccMembers.HouseHoldItem;
import com.nhpm.Models.response.seccMembers.HouseHoldRequest;
import com.nhpm.Models.response.seccMembers.SeccHouseholdResponse;
import com.nhpm.Models.response.seccMembers.SeccMemberItem;
import com.nhpm.Models.response.seccMembers.SeccMemberRequest;
import com.nhpm.Models.response.seccMembers.SeccMemberResponse;
import com.nhpm.Models.response.verifier.VerifierLocationItem;
import com.nhpm.Models.response.verifier.VerifierLoginResponse;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;
import com.nhpm.Utility.AppUtility;
import com.nhpm.Utility.SyncUtility;
import com.nhpm.fragments.BeneficiaryFamilySearchFragment;
import com.nhpm.fragments.SubEBWiseDownloadFragment;
import com.nhpm.fragments.VillageWiseDownloadFragment;
import com.nhpm.fragments.WardWiseDownloadFragment;
import com.nhpm.fragments.WithoutSeccDataDownloading;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import pl.polidea.view.ZoomView;

public class BlockDetailActivity extends BaseActivity implements ComponentCallbacks2 {
    private DatabaseHelpers dbHelper;
    private final String TAG = "BlockDetailActivity";
    //  private Context context;
    private TextView headerTV;
    private boolean pinLockIsShown = false;
    private ProgressDialog progressBar;
    private RecyclerView blockList;
    private ArrayList<VerifierLocationItem> dowloadedBlockList;
    private ArrayList<String> notDownloadedBlock;
    private Spinner blockSP;
    private CustomAdapter adapter;
    private Button downloadBT;
    private ImageView settings;
    private SeccMemberResponse memberListResponse;
    private SeccHouseholdResponse houseHoldListResponse;
    private SeccMemberResponse rsbyMemberResponse;
    private SeccHouseholdResponse rsbyHouseHoldResponse;
    private TextView stateNameTV, distNameTV, tehsilNameTV, villTownNameTV, wardCodeTV, verifierNameTV, vtLableTV, ebTV;
    private ImageView backIV;
    private AlertDialog dialog, deleteDialog, askForPinDailog, internetDiaolg, proceedDailog;
    private VerifierLoginResponse verifierDetail;
    private CardView syncbuttonLayout;
    private SeccHouseholdResponse houseHoldResponse;
    private LinearLayout downloadBloackLayout;
    private Button resetHouseHoldBT;
    private CustomAsyncTask asyncTask;
    private CustomAsyncTask countAsyncTask;
    private DataCountModel dataCountModel;
    private MemberStatusItemResponse statusItemResponse;
    private AadhaarStatusItemResponse aadhaarStatusItem;
    private ScrollView scrollView;
    //private HealthSchemeRequest healthSchemeRequest;
    private HealthSchemeItemResponse healthSchemeItemResponse;
    private RelationMasterItem relationMasterItemResponse;
    private String blockCode;
    private VerifierLoginResponse storedLoginResponse;
    private HouseHoldRequest houseHoldRequest;
    private DataCountRequest seccDownloadCountRequest;
    private SeccMemberRequest seccRequest;
    private SeccHouseholdResponse seccHouseHoldResponse;
    private SeccMemberResponse seccmemberResponse;

    private RelativeLayout menuLayout;
    private Button syncAadhaarCollectedBT;
    private String CLEAN_DEVICE = "1", DELETE_DATA = "2";
    private RelativeLayout syncRelativeLayout;
    private Integer count = 1;
    private final String INTERNET_LOST = "1";
    private final String SERVER_ERROR = "2";
    private String HOUSEHOLD_DOWNLOADED;
    private String BLOCK_HOUSEHOLD_EMPTY_MSG;

    private String HOUSEHOLD_MEMBER_DOWNLOADED;
    private String DATA_NOT_ALLOTED;
    private Context context;
    private String DOWNLOAD_COMPLETED;
    private String TRY_AGAIN_MSG;
    private BlockDownloadTask downloadAsyncTask;
    private notificationDownloadTask notificationAsyncTask;
    private configurationDownloadTask configurationAsyncTask;
    private String MEMBER_STATUS = "1", AADHAAR_STATUS = "2", HEALTH_STATUS = "3";
    private String masterMsg;
    private String serverIssues;
    public static int SYNC_REQUEST = 1;
    private TextView areaTV;
    private ArrayList<VerifierLocationItem> locationList;
    private int selectedIndexForDownload;
    private CardView locationCard;
    private LinearLayout mZoomLinearLayout;
    private ZoomView zoomView;
    private VerifierLocationItem selectedLocation;
    private HealthSchemeRequest healthSchemeRequest;
    private Activity activity;
    private String progressType = "";
    private MyCountDownTimer myCountDownTimer;
    private String dataSoursce = "b";
    private StateItem selectedStateItem;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransection;
    private Fragment fragment;
    private String downloadSource;
    private String zoomMode = "N";
    private String SeccDownloaded = "Y";


    private int wrongPinCount = 0;
    private long wrongPinSavedTime;
    private long currentTime;
    private TextView wrongAttempetCountValue, wrongAttempetCountText;
    private long millisecond24 = 86400000;

    public BlockDetailActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        activity = this;
        selectedStateItem = StateItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SELECTED_STATE, context));
        ArrayList<ConfigurationItem> configurationList = SeccDatabase.findAllConfiguration(context);
        if (isNetworkAvailable()) {

            if (configurationList != null && configurationList.size() > 0) {
                checkAppConfig();
                if (zoomMode != null && zoomMode.equalsIgnoreCase("N")) {
                    setContentView(R.layout.activity_block_detail);
                    setupWothoutZoomScreen();
                } else {
                    setContentView(R.layout.dummy_layout_for_zooming);
                    setupZoomScreen();
                }
            } else {
                downloadConfigurationData();
            }
        } else {

            if (configurationList != null && configurationList.size() > 0) {
                checkAppConfig();
                if (zoomMode != null && zoomMode.equalsIgnoreCase("N")) {
                    setContentView(R.layout.activity_block_detail);
                    setupWothoutZoomScreen();
                } else {
                    setContentView(R.layout.dummy_layout_for_zooming);
                    setupZoomScreen();
                }
            } else {
                alertWithOk(context, "Application Configuration not downloaded.\nPlease retry.");
            }
        }

        // downloadNotificationData();

        DOWNLOAD_COMPLETED = context.getResources().getString(R.string.dataSetuped);
        TRY_AGAIN_MSG = context.getResources().getString(R.string.tryAgainMessage);
        masterMsg = context.getResources().getString(R.string.configSetting);
        HOUSEHOLD_MEMBER_DOWNLOADED = context.getResources().getString(R.string.applicationSetUpMsg);
        HOUSEHOLD_DOWNLOADED = context.getResources().getString(R.string.plzWaitDataDownloading);
        BLOCK_HOUSEHOLD_EMPTY_MSG = context.getResources().getString(R.string.blockEmptyMsy);
        DATA_NOT_ALLOTED = context.getResources().getString(R.string.noHouseHoldAllotted);
        serverIssues = context.getResources().getString(R.string.serverNotResponding);

    }

    private void prepareBlockForDownloaddList() {
        dowloadedBlockList = new ArrayList<>();
        VerifierLocationItem locItem = null;
        for (VerifierLocationItem item : verifierDetail.getLocationList()) {
            ArrayList<HouseHoldItem> householdList = SeccDatabase.getHouseHoldList(
                    item.getStateCode().trim(), item.getDistrictCode().trim()
                    , item.getTehsilCode().trim(), item.getVtCode().trim(), item.getWardCode().trim(), item.getBlockCode().trim(), context);
            AppUtility.showLog(AppConstant.LOG_STATUS, TAG, " Household Size : " + householdList.size());
            if (householdList.size() > 0) {
                dowloadedBlockList.add(item);
            }
        }
        if (dowloadedBlockList != null && dowloadedBlockList.size() > 0) {
            adapter = new CustomAdapter(dowloadedBlockList);
            blockList.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    private void prepareBlockForNotDownloaded() {
        locationList = new ArrayList<>();

        ArrayList<VerifierLocationItem> list = verifierDetail.getLocationList();
        if (list != null) {
            for (VerifierLocationItem item : list) {
                long houseHoldCount = SeccDatabase.
                        houseHoldCount(context, item.getStateCode().trim(), item.getDistrictCode().trim(),
                                item.getTehsilCode().trim(), item.getVtCode().trim(), item.getWardCode().trim(), item.getBlockCode().trim());
                if (houseHoldCount > 0) {

                } else {
                    locationList.add(item);
                }
            }
        }
        notDownloadedBlock = new ArrayList<>();
        notDownloadedBlock.add(context.getResources().getString(R.string.selectNewEB));
        for (VerifierLocationItem item : locationList) {
            // Log.d(TAG, "Block code : " + item.getStateCode());
            notDownloadedBlock.add(item.getBlockCode());
            /*for(SeccMemberItem item1 : memberListResponse.getSeccMemberList()) {
                if(!item.getBlockCode().equalsIgnoreCase(item1.getBlockno())) {
                    break;
                }
            }*/
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.custom_drop_down, R.id.textView, notDownloadedBlock);
        blockSP.setAdapter(adapter);
    }

    private class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
        private ArrayList<VerifierLocationItem> mDataset;

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView blockCodeTV, noOfHouseholdTV, noOfMemberTV, syncedMemberTV, totalSyncedMemberTV;
            TextView stateNameTV, distTV, tehsilTV, vtNameTV, wardTV, ebTV;
            LinearLayout subEbLinearLayout;
            Button proceedBT;

            public ViewHolder(View v) {
                super(v);
                blockCodeTV = (TextView) v.findViewById(R.id.blockCodeTV);
                noOfHouseholdTV = (TextView) v.findViewById(R.id.totalHouseholdTV);
                noOfMemberTV = (TextView) v.findViewById(R.id.totalMemberTV);
                syncedMemberTV = (TextView) v.findViewById(R.id.syncedMemberTV);
                totalSyncedMemberTV = (TextView) v.findViewById(R.id.totalSyncMemberTV);
                proceedBT = (Button) v.findViewById(R.id.proceedBT);
                stateNameTV = (TextView) v.findViewById(R.id.stateNameTV);
                distTV = (TextView) v.findViewById(R.id.distNameTV);
                tehsilTV = (TextView) v.findViewById(R.id.tehsilNameTV);
                vtNameTV = (TextView) v.findViewById(R.id.vtNameTV);
                wardTV = (TextView) v.findViewById(R.id.wardCodeTV);
                ebTV = (TextView) v.findViewById(R.id.ebTV);
                subEbLinearLayout = (LinearLayout) v.findViewById(R.id.subEbLinearLayout);
            }
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

        public CustomAdapter(ArrayList<VerifierLocationItem> myDataset) {
            mDataset = myDataset;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent,
                                             int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.block_list_item, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final VerifierLocationItem loc = mDataset.get(position);
            if (loc != null) {
                if (loc.getStateName() != null) {
                    holder.stateNameTV.setText(loc.getStateName());
                }
                if (loc.getDistrictName() != null) {
                    holder.distTV.setText(loc.getDistrictName());
                }
                if (loc.getTehsilName() != null) {
                    holder.tehsilTV.setText(loc.getTehsilName());
                }
                if (loc.getVtName() != null) {
                    holder.vtNameTV.setText(loc.getVtName());
                }
                if (loc.getWardCode() != null) {
                    holder.wardTV.setText(loc.getWardCode());
                }
                if (loc.getBlockCode() != null) {
                    holder.ebTV.setText(loc.getBlockCode());
                }
            }

            holder.subEbLinearLayout.setVisibility(View.GONE);
            holder.proceedBT.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //int position=getPosition();
                    proceedDailog(loc);
                }
            });
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return mDataset.size();
        }
    }

    private void downloadNewblockData(VerifierLocationItem item) {
        houseHoldRequest = new HouseHoldRequest();
        houseHoldRequest.setStateCode(item.getStateCode());
        houseHoldRequest.setDistCode(item.getDistrictCode());
        houseHoldRequest.setTehsilCode(item.getTehsilCode());
        houseHoldRequest.setVillTownCode(item.getVtCode());
        houseHoldRequest.setWardCode(item.getWardCode());
        houseHoldRequest.setAhlBlockNo(item.getBlockCode());
        houseHoldRequest.setAhlSubBlockNo(item.getSubBlockcode());

        seccRequest = new SeccMemberRequest();
        seccRequest.setStateCode(item.getStateCode());
        seccRequest.setDistCode(item.getDistrictCode());
        seccRequest.setTehsilCode(item.getTehsilCode());
        seccRequest.setVillTownCode(item.getVtCode());
        seccRequest.setWardCode(item.getWardCode());
        seccRequest.setBlockCode(item.getBlockCode());
        seccRequest.setAhlSubBlockNo(item.getSubBlockcode());
        healthSchemeRequest = new HealthSchemeRequest();
        healthSchemeRequest.setStatecode(item.getStateCode());
        Log.d("state", item.getStateCode());

        seccDownloadCountRequest = new DataCountRequest();
        seccDownloadCountRequest.setStateCode(item.getStateCode());
        seccDownloadCountRequest.setDistrictCode(item.getDistrictCode());
        seccDownloadCountRequest.setTehsilCode(item.getTehsilCode());
        seccDownloadCountRequest.setVillageTownCode(item.getVtCode());
        seccDownloadCountRequest.setWardCode(item.getWardCode());
        seccDownloadCountRequest.setAhlBlockCode(item.getBlockCode());
        seccDownloadCountRequest.setAhlSubBlockCode("");

        checkDataCount(seccDownloadCountRequest.serialize());
        // downloadHouseHoldVolley(houseHoldRequest);
        //   downloadEnumerationBlockData();
        //  downloadHouseholdData(houseHoldRequest);

    }


    private void setupWothoutZoomScreen() {
        context = this;
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        verifierDetail = VerifierLoginResponse.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.VERIFIER_CONTENT, context));
        storedLoginResponse = VerifierLoginResponse.create(
                ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.VERIFIER_CONTENT, context));

        openOptionFragment();
        locationCard = (CardView) findViewById(R.id.locationCard);
        headerTV = (TextView) findViewById(R.id.centertext);
        resetHouseHoldBT = (Button) findViewById(R.id.resetDataBT);
        syncRelativeLayout = (RelativeLayout) findViewById(R.id.syncRelativeLayout);
        areaTV = (TextView) findViewById(R.id.areaTV);
        backIV = (ImageView) findViewById(R.id.back);
        blockSP = (Spinner) findViewById(R.id.blockSP);
        ebTV = (TextView) findViewById(R.id.enumerationBlockTV);
        blockList = (RecyclerView) findViewById(R.id.blockList);
        downloadBT = (Button) findViewById(R.id.downloadBT);
        settings = (ImageView) findViewById(R.id.settings);
        menuLayout = (RelativeLayout) findViewById(R.id.menuLayout);
        menuLayout.setVisibility(View.VISIBLE);
        verifierNameTV = (TextView) findViewById(R.id.verifierNameTV);
        verifierNameTV.setText(verifierDetail.getName());
        stateNameTV = (TextView) findViewById(R.id.stateNameTV);
        distNameTV = (TextView) findViewById(R.id.distNameTV);
        tehsilNameTV = (TextView) findViewById(R.id.tehsilNameTV);
        villTownNameTV = (TextView) findViewById(R.id.villTownNameTV);
        wardCodeTV = (TextView) findViewById(R.id.wardNameTV);
        vtLableTV = (TextView) findViewById(R.id.vtLableTV);
        if (isNetworkAvailable()) {
            //  checkUpdatedVersion(1);
        }
        syncbuttonLayout = (CardView) findViewById(R.id.syncButtonCardView);
        downloadBloackLayout = (LinearLayout) findViewById(R.id.downloadBloackLayout);
        syncbuttonLayout.setVisibility(View.GONE);
        downloadBloackLayout.setVisibility(View.VISIBLE);
        syncAadhaarCollectedBT = (Button) findViewById(R.id.syncAadhaarCollectedBT);

        if (SeccDatabase.getMemberStatusList(context).size() > 0) {

        } else {
            downloadMemberMasterData();
        }
       // prepareBlockForDownloaddList();
        settings.setVisibility(View.VISIBLE);
        blockList.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        blockList.setLayoutManager(mLayoutManager);
        backIV.setVisibility(View.INVISIBLE);
        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                rightTransition();
            }
        });
        syncAadhaarCollectedBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent theIntent = new Intent(context, SyncHouseholdActivity.class);
                startActivityForResult(theIntent, SYNC_REQUEST);
                leftTransition();
            }
        });
        syncRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                syncAadhaarCollectedBT.performClick();
            }
        });
        downloadBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedPos = blockSP.getSelectedItemPosition();
                selectedLocation = null;
                if (selectedPos == 0) {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.pleaseSelectEb));
                } else {
                    //  downloadBlock(selectedPos);
                    selectedLocation = locationList.get(selectedPos - 1);
                    downloadNewblockData(selectedLocation);
                }
            }
        });
        blockSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                locationCard.setVisibility(View.GONE);
                if (i == 0) {
                    updateLocation(null);
                } else {
                    updateLocation(locationList.get(i - 1));
                    locationCard.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        resetHouseHoldBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        menuLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(context, settings);
                popup.getMenuInflater()
                        .inflate(R.menu.menu_home, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.clearDevice:
                                if (SeccDatabase.countSurveyedHousehold(context, null, "", "") > 0) {
                                    deleteSyncPrompt(null);
                                } else {
                                    pendingHouseholdDeletePrompt(CLEAN_DEVICE, null);
                                }
                                break;

                            case R.id.villageWise:

                                openVillageDownloadFragment();
                                break;
                            case R.id.logout:
                                // logoutVerifier();
                                // if(isNetworkAvailable()) {
                                Intent theIntent;
                              /*  if (verifierDetail != null && verifierDetail.getAadhaarNumber() != null) {*/
                                theIntent = new Intent(context, LoginActivity.class);
                              /*  } else {
                                    theIntent = new Intent(context, NonAdharLoginActivity.class);
                                }*/
                                theIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                theIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(theIntent);
                                rightTransition();
                                //verifierDetail.setLoginSession(false);
/*
                                    ProjectPrefrence.saveSharedPrefrenceData(AppConstant.
                                            PROJECT_PREF,AppConstant.VERIFIER_CONTENT,verifierDetail.serialize(),context);
*/
                                // ProjectPrefrence.removeSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.VERIFIER_CONTENT, context);
                                /*}else{
                                    CustomAlert.alertWithOk(context,"Please connect your device to internet to logout.");
                                }*/
                                break;
                            case R.id.profile:
                                // logout();
                                profileActivity();
                                break;
                            case R.id.notification:
                                //  downloadApplicationNotification();
                                downloadNotificationData();
                                break;
                            case R.id.changePin:
                                // logout();
                                changePin();
                                break;
                            case R.id.deleteHousehold:
                                // logout();
                                // deletePopupAlert();
                                // CustomAlert.alertWithOk(context,"Under development");
                                /*if(SeccDatabase.countSurveyedHousehold(context,"","")>0){
                                    deleteSyncPrompt();
                                }else{
                                    pendingHouseholdDeletePrompt(DELETE_DATA,null);
                                }*/
                                break;

                            case R.id.appConfig:

                                startAppConfigActivity();

                                break;
                            case R.id.update:
                                if (isNetworkAvailable()) {
                                    checkUpdatedVersion(0);
                                } else {
                                    AppUtility.alertWithOk(context, context.getResources().getString(R.string.internet_connection_msg));
                                }
                                break;
                        }
                        return true;
                    }
                });
                popup.show();

            }
        });
        setData();
       // openOptionFragment();

     /*   if (SeccDownloaded.equalsIgnoreCase("N")) {
            openWithoutSeccFragment();
        } else {
            if (downloadSource != null && downloadSource.equalsIgnoreCase(AppConstant.VillageWiseDownloading)) {
                openVillageDownloadFragment();
            } else if (downloadSource != null && downloadSource.equalsIgnoreCase(AppConstant.SubBlockWiseDownloading)) {
                openSubEbDownloadFragment();
            } else if (downloadSource != null && downloadSource.equalsIgnoreCase(AppConstant.WardWiseDownloading)) {
                openWardDownloadFragment();
            }
        }*/
    }

    private void setupZoomScreen() {
        context = this;
        mZoomLinearLayout = (LinearLayout) findViewById(R.id.mZoomLinearLayout);
        View v = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.activity_block_detail, null, false);
        v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
        scrollView = (ScrollView) v.findViewById(R.id.scrollView);
        openOptionFragment();
        //checkAppConfig();
        // downloadNotificationData();
        showNotification(v);
        verifierDetail = VerifierLoginResponse.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.VERIFIER_CONTENT, context));
        storedLoginResponse = VerifierLoginResponse.create(
                ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.VERIFIER_CONTENT, context));
        locationCard = (CardView) v.findViewById(R.id.locationCard);
        headerTV = (TextView) v.findViewById(R.id.centertext);
        resetHouseHoldBT = (Button) v.findViewById(R.id.resetDataBT);
        syncRelativeLayout = (RelativeLayout) v.findViewById(R.id.syncRelativeLayout);
        areaTV = (TextView) v.findViewById(R.id.areaTV);
        backIV = (ImageView) v.findViewById(R.id.back);
        blockSP = (Spinner) v.findViewById(R.id.blockSP);
        ebTV = (TextView) v.findViewById(R.id.enumerationBlockTV);
        blockList = (RecyclerView) v.findViewById(R.id.blockList);
        downloadBT = (Button) v.findViewById(R.id.downloadBT);
        settings = (ImageView) v.findViewById(R.id.settings);
        menuLayout = (RelativeLayout) v.findViewById(R.id.menuLayout);
        menuLayout.setVisibility(View.VISIBLE);
        verifierNameTV = (TextView) v.findViewById(R.id.verifierNameTV);
//        verifierNameTV.setText(verifierDetail.getName());
        stateNameTV = (TextView) v.findViewById(R.id.stateNameTV);
        distNameTV = (TextView) v.findViewById(R.id.distNameTV);
        tehsilNameTV = (TextView) v.findViewById(R.id.tehsilNameTV);
        villTownNameTV = (TextView) v.findViewById(R.id.villTownNameTV);
        wardCodeTV = (TextView) v.findViewById(R.id.wardNameTV);
        vtLableTV = (TextView) v.findViewById(R.id.vtLableTV);

//  check update version.
        if (isNetworkAvailable()) {
            //  checkUpdatedVersion(1);
        }
        syncbuttonLayout = (CardView) v.findViewById(R.id.syncButtonCardView);
        downloadBloackLayout = (LinearLayout) v.findViewById(R.id.downloadBloackLayout);
        syncbuttonLayout.setVisibility(View.GONE);
        downloadBloackLayout.setVisibility(View.VISIBLE);
        syncAadhaarCollectedBT = (Button) v.findViewById(R.id.syncAadhaarCollectedBT);
        zoomView = new ZoomView(this);
        zoomView.addView(v);
        mZoomLinearLayout.addView(zoomView);
        if (SeccDatabase.getMemberStatusList(context).size() > 0) {

        } else {
            downloadMemberMasterData();
        }
       // prepareBlockForDownloaddList();
        settings.setVisibility(View.VISIBLE);
        blockList.setHasFixedSize(true);
        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        blockList.setLayoutManager(mLayoutManager);
        backIV.setVisibility(View.INVISIBLE);

        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                rightTransition();
            }
        });
        syncAadhaarCollectedBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent theIntent = new Intent(context, SyncHouseholdActivity.class);
                startActivityForResult(theIntent, SYNC_REQUEST);
                leftTransition();
                //();
            }
        });
        syncRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                syncAadhaarCollectedBT.performClick();
            }
        });
        downloadBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedPos = blockSP.getSelectedItemPosition();
                selectedLocation = null;
                if (selectedPos == 0) {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.pleaseSelectEb));
                } else {
                    //  downloadBlock(selectedPos);
                    selectedLocation = locationList.get(selectedPos - 1);
                    downloadNewblockData(selectedLocation);
                }
            }
        });
        blockSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                locationCard.setVisibility(View.GONE);
                if (i == 0) {
                    updateLocation(null);
                } else {
                    updateLocation(locationList.get(i - 1));
                    locationCard.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        resetHouseHoldBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // dowloadPopupAlert();
                // askPinToReset();
            }
        });

        menuLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(context, settings);
                popup.getMenuInflater()
                        .inflate(R.menu.menu_home, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.clearDevice:
                               /* if (SeccDatabase.countSurveyedHousehold(context, null, "", "") > 0) {
                                    deleteSyncPrompt(null);
                                } else {
                                    pendingHouseholdDeletePrompt(CLEAN_DEVICE, null);
                                }*/
                                askPinToDelete(CLEAN_DEVICE, null);
                                break;

                            case R.id.villageWise:

                                openVillageDownloadFragment();
                                break;
                            case R.id.logout:
                                // logoutVerifier();
                                // if(isNetworkAvailable()) {
                                Intent theIntent;
                              /*  if (verifierDetail != null && verifierDetail.getAadhaarNumber() != null) {*/
                                theIntent = new Intent(context, LoginActivity.class);
                              /*  } else {
                                    theIntent = new Intent(context, NonAdharLoginActivity.class);
                                }*/
                                theIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                theIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(theIntent);
                                rightTransition();
                                //verifierDetail.setLoginSession(false);
/*
                                    ProjectPrefrence.saveSharedPrefrenceData(AppConstant.
                                            PROJECT_PREF,AppConstant.VERIFIER_CONTENT,verifierDetail.serialize(),context);
*/
                                // ProjectPrefrence.removeSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.VERIFIER_CONTENT, context);
                                /*}else{
                                    CustomAlert.alertWithOk(context,"Please connect your device to internet to logout.");
                                }*/
                                break;
                            case R.id.profile:
                                // logout();
                                profileActivity();
                                break;
                            case R.id.notification:
                                //  downloadApplicationNotification();
                                downloadNotificationData();
                                break;
                            case R.id.changePin:
                                // logout();
                                changePin();
                                break;
                            case R.id.deleteHousehold:
                                // logout();
                                // deletePopupAlert();
                                // CustomAlert.alertWithOk(context,"Under development");
                                /*if(SeccDatabase.countSurveyedHousehold(context,"","")>0){
                                    deleteSyncPrompt();
                                }else{
                                    pendingHouseholdDeletePrompt(DELETE_DATA,null);
                                }*/
                                break;

                            case R.id.appConfig:

                                startAppConfigActivity();

                                break;
                            case R.id.update:
                                if (isNetworkAvailable()) {
                                    checkUpdatedVersion(0);
                                } else {
                                    AppUtility.alertWithOk(context, context.getResources().getString(R.string.internet_connection_msg));
                                }
                                break;
                        }
                        return true;
                    }
                });
                popup.show();

            }
        });
        setData();

    /*    if (SeccDownloaded.equalsIgnoreCase("N")) {
            openWithoutSeccFragment();
        } else {
            if (downloadSource != null && downloadSource.equalsIgnoreCase(AppConstant.VillageWiseDownloading)) {
                openVillageDownloadFragment();
            } else if (downloadSource != null && downloadSource.equalsIgnoreCase(AppConstant.SubBlockWiseDownloading)) {
                openSubEbDownloadFragment();
            } else if (downloadSource != null && downloadSource.equalsIgnoreCase(AppConstant.WardWiseDownloading)) {
                openWardDownloadFragment();
            }
        }*/
    }

    private void profileActivity() {
        Intent theIntent = new Intent(context, ProfileActivity.class);
        startActivity(theIntent);
        leftTransition();
    }

    private void changePin() {
        if (isNetworkAvailable()) {
            Intent theIntent = new Intent(context, ChangePinActivity.class);
            startActivity(theIntent);
            leftTransition();
        } else {
            AppUtility.alertWithOk(context, context.getResources().getString(R.string.internet_connection_msg));
        }
    }

    private void setData() {
        headerTV.setText(context.getResources().getString(R.string.nhpsFieldValidation) +" ("+selectedStateItem.getStateName()+")");
        //prepareBlockForNotDownloaded();
    }

    private void pendingHouseholdDeletePrompt(final String status, final VerifierLocationItem item) {
        deleteDialog = new AlertDialog.Builder(context).create();
        LayoutInflater factory = LayoutInflater.from(context);
        View alertView = factory.inflate(R.layout.delete_house_hold_prompt, null);
        deleteDialog.setView(alertView);
        Button deletBT = (Button) alertView.findViewById(R.id.syncBT);
        deletBT.setText(context.getResources().getString(R.string.Delete));
        Button cancelBT = (Button) alertView.findViewById(R.id.cancelBT);
        final TextView deletePromptTV = (TextView) alertView.findViewById(R.id.deleteMsg);
        long pendingHousehold = SeccDatabase.countUnderSurveyedHousehold(context, item, "", AppConstant.SAVE + "");
        String msg = context.getResources().getString(R.string.uHave) + pendingHousehold + context.getResources().getString(R.string.deleteEbMsg);
        deletePromptTV.setText(msg);
        deletBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Log.d(TAG,"Delete Status"+status);
                askPinToDelete(status, item);
                deleteDialog.dismiss();

            }
        });
        cancelBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDialog.dismiss();
            }
        });
        deleteDialog.show();
    }

    private void deleteData() {
        SeccDatabase.deleteTable(context, AppConstant.TRANSECT_POPULATE_SECC);
        SeccDatabase.deleteTable(context, AppConstant.HOUSE_HOLD_SECC);
       /* SeccDatabase.deleteTable(context,AppConstant.MEMBER_STATUS);
        SeccDatabase.deleteTable(context,AppConstant.AADHAAR_STATUS);
        SeccDatabase.deleteTable(context,AppConstant.HEALTH_SCHEME);*/
        SeccDatabase.deleteTable(context, AppConstant.MEMBER_ERROR_TABLE);
        Intent theIntent = new Intent(context, BlockDetailActivity.class);
        startActivity(theIntent);
        finish();
    }

    private void deleteSyncPrompt(VerifierLocationItem item) {
        dialog = new AlertDialog.Builder(context).create();
        LayoutInflater factory = LayoutInflater.from(context);
        View alertView = factory.inflate(R.layout.delete_house_hold_prompt, null);
        dialog.setView(alertView);
        // dialog.setContentView(R.layout.opt_auth_layout);
        //    final TextView otpAuthMsg=(TextView)alertView.findViewById(R.id.otpAuthMsg);

        Button syncBT = (Button) alertView.findViewById(R.id.syncBT);
        syncBT.setText("Ok");
        Button cancelBT = (Button) alertView.findViewById(R.id.cancelBT);
        final TextView deletePromptTV = (TextView) alertView.findViewById(R.id.deleteMsg);
        long unsyncData = SeccDatabase.countSurveyedHousehold(context, item, "", "");
        // long underSurvey=SeccDatabase.countUnderSurveyedHousehold(context,"","");
        String msg = context.getResources().getString(R.string.uHave) + unsyncData + context.getResources().getString(R.string.deletingUnsyncHouseHold);
        //   otpAuthMsg.setText("Please enter OTP sent by the UIDAI on your Aadhaar registerd mobile number(XXXXXX0906");
        deletePromptTV.setText(msg);
        syncBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                /*Intent theIntent=new Intent(context,SyncHouseholdActivity.class);
                startActivity(theIntent);
                finish();
                leftTransition();*/
            }
        });


        cancelBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void downloadMemberMasterData() {
        VolleyTaskListener taskListener = new VolleyTaskListener() {
            @Override
            public void postExecute(String response) {
                statusItemResponse = MemberStatusItemResponse.create(response);
                // Log.d(TAG,"Member Status List : "+statusItemResponse.getMemberStatusList().size());
                AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Member status response : " + response);
                if (statusItemResponse.isStatus()) {
                    for (MemberStatusItem item : statusItemResponse.getMemberStatusList()) {
                        SeccDatabase.saveMemberStatus(item, context);
                        Log.d(TAG, "Member Status List : " + item.getStatusDesc());
                    }

                  /*  if (SeccDatabase.getAadhaarStatusList(context).size() > 0) {

                    } else {
                        downloadAadhaarMasterData();
                    }*/
                } else {
                    showDialog(BlockDetailActivity.this, context.getResources().getString(R.string.Alert), serverIssues, context.getResources().getString(R.string.tryAgain));
                }

            }

            @Override
            public void onError(VolleyError error) {

                showDialog(BlockDetailActivity.this, context.getResources().getString(R.string.Alert), TRY_AGAIN_MSG, context.getResources().getString(R.string.tryAgain));
            }
        };

        CustomVolleyHeaderGet volleyGet = new CustomVolleyHeaderGet(taskListener, masterMsg,
                AppConstant.MEMBER_STATUS_API, AppConstant.AUTHORIZATION, AppConstant.AUTHORIZATIONVALUE, context);
        volleyGet.execute();
    }

    private void downloadAadhaarMasterData() {
        VolleyTaskListener taskListener = new VolleyTaskListener() {
            @Override
            public void postExecute(String response) {
                aadhaarStatusItem = AadhaarStatusItemResponse.create(response);
                if (statusItemResponse.isStatus()) {
                    for (AadhaarStatusItem item : aadhaarStatusItem.getAadhaarStatusList()) {
                        SeccDatabase.saveAadhaarStatus(item, context);
                        Log.d(TAG, "Aadhaar Status : " + item.getaStatusDesc());

                    }
                    /*if(SeccDatabase.getHealthSchemeList(context).size()>0) {

                    }else{
                       // downloadHealthSchemeData();
                    }*/
                } else {
                    showDialog(BlockDetailActivity.this, context.getResources().getString(R.string.Alert), serverIssues, context.getResources().getString(R.string.tryAgain));
                }


            }

            @Override
            public void onError(VolleyError error) {
                showDialog(BlockDetailActivity.this, context.getResources().getString(R.string.Alert), TRY_AGAIN_MSG, context.getResources().getString(R.string.tryAgain));

            }
        };

        CustomVolleyGet volleyGet = new CustomVolleyGet(taskListener, masterMsg,
                AppConstant.AADHAAR_STATUS_API, context);
        volleyGet.execute();
    }


    private void downloadData() {
        TaskListener taskListener = new TaskListener() {
            @Override
            public void execute() {
                try {
                    HashMap<String, String> response = CustomHttp.httpGet(AppConstant.MEMBER_STATUS, null);
                    String memberStatus = response.get("response");
                    if (memberStatus != null) {
                        MemberStatusItemResponse statusItemResponse = MemberStatusItemResponse.create(memberStatus);
                        if (statusItemResponse.isStatus()) {
                            if (statusItemResponse.getMemberStatusList() != null)
                                for (MemberStatusItem item : statusItemResponse.getMemberStatusList()) {
                                    Log.d(TAG, "Member Status List : " + item.getStatusDesc());
                                    MemberStatusItem item1 = SeccDatabase.getMemberStatusDetail(context, item.getStatusCode());
                                    if (item1 != null) {
                                        SeccDatabase.updateMemberStatus(item, context);
                                    } else {
                                        SeccDatabase.saveMemberStatus(item, context);
                                    }

                                }
                        } else {

                        }
                    }
                } catch (Exception e) {

                }

            }

            @Override
            public void updateUI() {

            }
        };
        CustomAsyncTask asyncTask = new CustomAsyncTask(taskListener, context.getResources().getString(R.string.please_wait), context);
        asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void downloadHouseholdData(final HouseHoldRequest request) {
        TaskListener taskListener = new TaskListener() {
            @Override
            public void execute() {
                try {
                    AppUtility.showLog(AppConstant.LOG_STATUS, TAG, " Secc Request : " + request.serialize());
                    HashMap<String, String> response = CustomHttp.httpPost1(AppConstant.SECC_HOUSE_HOLD_API, request.serialize(), null);
                    String houseHoldResponse = response.get("response");
                    Log.d(TAG, "Household response : " + houseHoldResponse);
                    if (houseHoldResponse != null) {
                        houseHoldListResponse = SeccHouseholdResponse.create(houseHoldResponse);
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void updateUI() {
                if (houseHoldListResponse != null) {
                    Log.d(TAG, "Household list size : " + houseHoldListResponse.getSeccHouseholdList().size());
                    downloadSeccMemberData(seccRequest);
                }
            }
        };
        if (asyncTask != null) {
            asyncTask.cancel(true);
            asyncTask = null;
        }

        asyncTask = new CustomAsyncTask(taskListener, "Downloading household,Please wait...", context);
        asyncTask.execute();
    }

    private void downloadSeccMemberData(final SeccMemberRequest request) {
        TaskListener taskListener = new TaskListener() {
            @Override
            public void execute() {
                try {
                    HashMap<String, String> response = CustomHttp.httpPost1(AppConstant.SECC_MEMBER_API, request.serialize(), null);
                    String seccMemberResp = response.get("response");
                    Log.d(TAG, "Member response : " + seccMemberResp);
                    if (seccMemberResp != null) {
                        seccmemberResponse = SeccMemberResponse.create(seccMemberResp);
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void updateUI() {
               /* if(seccmemberResponse!=null){
                    Log.d(TAG, "Household list size : " + seccmemberResponse.getSeccMemberList().size());*/
                saveDataIntoDatabase();
                //  }
            }
        };
        if (asyncTask != null) {
            asyncTask.cancel(true);
            asyncTask = null;
        }
        asyncTask = new CustomAsyncTask(taskListener, "Downloading secc member, Please wait...", context);
        asyncTask.execute();


    }


    private void downloadSeccMemberVolley(SeccMemberRequest request) {
        VolleyTaskListener taskListener = new VolleyTaskListener() {
            @Override
            public void postExecute(String response) {
                Log.d(TAG, " Volley Response : " + response);
                Log.d(TAG, "Member response : " + response);
                if (response != null) {
                    seccmemberResponse = SeccMemberResponse.create(response);
                    saveDataIntoDatabase();
                }
            }

            @Override
            public void onError(VolleyError error) {

            }
        };
        CustomVolley volley = new CustomVolley(taskListener, "Downloading household, Please wait..",
                AppConstant.SECC_MEMBER_API, request.serialize(), null, null, context);
        volley.execute();
    }

    private void saveHouseholdData(VerifierLocationItem item) {
        if (houseHoldListResponse != null) {
            // Log.d(TAG,"Household list size : "+houseHoldListResponse.getSeccHouseholdList().size());
            if (SeccDatabase.houseHoldCount(context, item.getStateCode().trim(), item.getDistrictCode().trim(), item.getTehsilCode().trim(),
                    item.getVtCode().trim(), item.getWardCode().trim(), item.getBlockCode().trim()) > 0) {
                AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Member response1 : ");
            } else {

                progressType = "Saving household data to database";


                //for (HouseHoldItem item1 : houseHoldListResponse.getSeccHouseholdList()) {
                int totalSize = houseHoldListResponse.getSeccHouseholdList().size();
                for (int i = 0; i < houseHoldListResponse.getSeccHouseholdList().size(); i++) {
                    HouseHoldItem item1 = houseHoldListResponse.getSeccHouseholdList().get(i);
                    progressType = "Saving SECC household data to database : " + i + "/" + totalSize + "";
                    AppUtility.showLog(AppConstant.LOG_STATUS, TAG, " Nhps Member id : " + item1.getNhpsMemId());
                    Log.d(TAG, "Household list size : " + SeccDatabase.houseHoldCount(context));
                    // updated by saurabh
                    item1.setDataSource(AppConstant.SECC_SOURCE);
                    AppUtility.showLog(AppConstant.LOG_STATUS, TAG, " Household response byte size : " + item1.serialize().getBytes().length);
                    long houseCount = SeccDatabase.saveHouseHold(item1, context);

                }
                // saving Rsby HouseHold data
                if (rsbyHouseHoldResponse != null) {
                    if (rsbyHouseHoldResponse.getRsbyHouseholdReadList() != null && rsbyHouseHoldResponse.getRsbyHouseholdReadList().size() > 0) {
                        for (int i = 0; i < rsbyHouseHoldResponse.getRsbyHouseholdReadList().size(); i++) {
                            HouseHoldItem item1 = rsbyHouseHoldResponse.getRsbyHouseholdReadList().get(i);
                            progressType = "Saving RSBY household data to database : " + i + "/" + rsbyHouseHoldResponse.getRsbyHouseholdReadList().size() + "";
                            AppUtility.showLog(AppConstant.LOG_STATUS, TAG, " Nhps Member id : " + item1.getNhpsMemId());
                            Log.d(TAG, "Household list size : " + SeccDatabase.houseHoldCount(context));
                            // updated by saurabh
                            item1.setDataSource(AppConstant.RSBY_SOURCE);
                            AppUtility.showLog(AppConstant.LOG_STATUS, TAG, " Household response byte size : " + item1.serialize().getBytes().length);
                            SeccDatabase.saveHouseHold(item1, context);
                        }
                    }
                }
            }
        }


        if (seccmemberResponse != null) {
            if (SeccDatabase.seccMemberCount(context, item.getStateCode().trim(), item.getDistrictCode().trim(),
                    item.getTehsilCode().trim(), item.getVtCode().trim(), item.getWardCode().trim(), item.getBlockCode().trim()) > 0) {

            } else {

                for (int i = 0; i < seccmemberResponse.getSeccMemberList().size(); i++) {
                    //  for (SeccMemberItem item1 : seccmemberResponse.getSeccMemberList()) {
                    SeccMemberItem item1 = seccmemberResponse.getSeccMemberList().get(i);
                    progressType = "Saving SECC members data to database : " + i + "/" + seccmemberResponse.getSeccMemberList().size();
                    AppUtility.showLog(AppConstant.LOG_STATUS, TAG, " Secc response byte size : " + item1.serialize().getBytes().length);
                    // updated by saurabh
                    item1.setDataSource(AppConstant.SECC_SOURCE);
                    SeccDatabase.saveSeccMember(item1, context);
                }
                if (rsbyMemberResponse != null) {
                    if (rsbyMemberResponse.getRsbyMemberReadList() != null) {
                        for (int i = 0; i < rsbyMemberResponse.getRsbyMemberReadList().size(); i++) {
                            //  for (SeccMemberItem item1 : seccmemberResponse.getSeccMemberList()) {
                            SeccMemberItem item1 = rsbyMemberResponse.getRsbyMemberReadList().get(i);
                            progressType = "Saving RSBY members data to database : " + i + "/" + rsbyMemberResponse.getRsbyMemberReadList().size();
                            //   AppUtility.showLog(AppConstant.LOG_STATUS, TAG, " Secc response byte size : " + item1.serialize().getBytes().length);
                            // updated by saurabh
                            item1.setDataSource(AppConstant.RSBY_SOURCE);
                            SeccDatabase.saveSeccMember(item1, context);
                        }
                    }
                }

            }
        }


        if (healthSchemeItemResponse != null) {
            progressType = "Updating Scheme";
            if (healthSchemeItemResponse.getStateHelathSchemeList() != null && healthSchemeItemResponse.getStateHelathSchemeList().size() > 0) {
                for (HealthSchemeItem healthSchemeItem : healthSchemeItemResponse.getStateHelathSchemeList()) {
                    HealthSchemeItem item1 = SeccDatabase.getHealthSchemeDetail(context, healthSchemeItem.getStatecode(), healthSchemeItem.getSchemeId());
                    if (item1 != null) {
                        SeccDatabase.updateHealthScheme(healthSchemeItem, context);
                    } else {
                        SeccDatabase.saveHealthScheme(healthSchemeItem, context);
                    }
                }
            }
        }

        if (relationMasterItemResponse != null) {
            progressType = "Updating Relation Master";
            if (relationMasterItemResponse.getRelationMasterData() != null && relationMasterItemResponse.getRelationMasterData().size() > 0) {
                if (SeccDatabase.getRelationList(context) != null && SeccDatabase.getRelationList(context).size() > 0) {
                    String query = "delete from " + AppConstant.RELATION_TABLE;
                    SeccDatabase.deleteTable(query, context);
                }
                for (MemberRelationItem relationItem : relationMasterItemResponse.getRelationMasterData()) {

                    SeccDatabase.saveRelationMaster(relationItem, context);

                }
            }
        }

        //   fixHouseholdStatus(item);
        ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.DOWNLOADINGSOURCE, AppConstant.EbWiseDownloading, context);

    }

    private void fixHouseholdStatus(VerifierLocationItem item) {
        ArrayList<HouseHoldItem> householdList = SeccDatabase.getHouseHoldList(item.getStateCode(),
                item.getDistrictCode(), item.getTehsilCode(), item.getVtCode(), item.getWardCode(), item.getBlockCode(), context);
        if (householdList != null) {
            for (HouseHoldItem item1 : householdList) {
                if (item1.getSyncedStatus() != null && item1.getSyncedStatus().trim().equalsIgnoreCase(AppConstant.SYNCED_STATUS)) {

                } else {
                    HouseHoldItem updatedHouseholdItem = AppUtility.checkAllSyncStatus(item1, context);
                    SeccDatabase.updateHouseHold(updatedHouseholdItem, context);
                }
            }
        }
    }

    private void saveSeccMemberData() {
        if (seccmemberResponse != null) {
            if (SeccDatabase.seccMemberCount(context) > 0) {

            } else {
                for (SeccMemberItem item : seccmemberResponse.getSeccMemberList()) {
                    Log.d(TAG, "Household list size : " + SeccDatabase.seccMemberCount(context));
                    SeccDatabase.saveSeccMember(item, context);
                }
            }
        }
    }

    private void setLocationAndUpdateScreen() {
 /*       VerifierLocationItem loc=verifierDetail.getLocationList().get(0);
        loc.setBlockCode(blockCode);
        ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SELECTED_BLOCK, loc.serialize(), context);*/
        prepareBlockForNotDownloaded();
        prepareBlockForDownloaddList();

       /* if(SeccDatabase.houseHoldCount(context)>0 && SeccDatabase.seccMemberCount(context)>0){
            syncbuttonLayout.setVisibility(View.VISIBLE);
            downloadBloackLayout.setVisibility(View.GONE);
            prepareBlockForDownloaddList();
        }else{
            downloadBloackLayout.setVisibility(View.VISIBLE);
            syncbuttonLayout.setVisibility(View.GONE);

        }*/
    }

    private void saveDataIntoDatabase() {
        TaskListener taskListener = new TaskListener() {
            @Override
            public void execute() {
                if (houseHoldListResponse != null) {
                    Log.d(TAG, "Household list size : " + houseHoldListResponse.getSeccHouseholdList().size());
                    if (SeccDatabase.houseHoldCount(context) > 0) {

                    } else {
                        for (HouseHoldItem item : houseHoldListResponse.getSeccHouseholdList()) {
                            Log.d(TAG, "Household list size : " + SeccDatabase.houseHoldCount(context));
                            item.setDataSource(AppConstant.SECC_SOURCE);
                            SeccDatabase.saveHouseHold(item, context);
                        }
                    }
                }
                if (seccmemberResponse != null) {
                    if (SeccDatabase.seccMemberCount(context) > 0) {

                    } else {
                        for (SeccMemberItem item : seccmemberResponse.getSeccMemberList()) {
                            Log.d(TAG, "Household list size : " + SeccDatabase.seccMemberCount(context));
                            item.setDataSource(AppConstant.SECC_SOURCE);
                            SeccDatabase.saveSeccMember(item, context);
                        }
                    }
                }
                /*Log.d(TAG,"Household list size : "+SeccDatabase.houseHoldCount(context));
                Log.d(TAG,"Secc Members Size111 :"+SeccDatabase.seccMemberCount(context));*/
                VerifierLocationItem loc = verifierDetail.getLocationList().get(0);
                loc.setBlockCode(blockCode);
                ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SELECTED_BLOCK, loc.serialize(), context);
            }

            @Override
            public void updateUI() {
                if (SeccDatabase.houseHoldCount(context) > 0 && SeccDatabase.seccMemberCount(context) > 0) {
                    syncbuttonLayout.setVisibility(View.VISIBLE);
                    downloadBloackLayout.setVisibility(View.GONE);
                    prepareBlockForDownloaddList();
                } else {
                    downloadBloackLayout.setVisibility(View.VISIBLE);
                    syncbuttonLayout.setVisibility(View.GONE);

                }
            }
        };
        if (asyncTask != null) {
            asyncTask.cancel(true);
            asyncTask = null;
        }
        asyncTask = new CustomAsyncTask(taskListener, "Preparing database, please wait..", context);
        asyncTask.execute();

    }

    private void askPinToDelete(final String deleteStatus, final VerifierLocationItem item) {
        askForPinDailog = new AlertDialog.Builder(context).create();
        LayoutInflater factory = LayoutInflater.from(context);
        View alertView = factory.inflate(R.layout.ask_pin_layout, null);
        askForPinDailog.setView(alertView);
        askForPinDailog.show();
        // Log.d(TAG,"delete status :"+deleteStatus);
        // dialog.setContentView(R.layout.opt_auth_layout);
        //    final TextView otpAuthMsg=(TextView)alertView.findViewById(R.id.otpAuthMsg);
        final EditText pinET = (EditText) alertView.findViewById(R.id.deletPinET);
        wrongAttempetCountValue = (TextView) alertView.findViewById(R.id.wrongAttempetCountValue);
        wrongAttempetCountText = (TextView) alertView.findViewById(R.id.wrongAttempetCountText);


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


                    if (pinET.getText().toString().equalsIgnoreCase(verifierDetail.getPin())) {
                        if (deleteStatus.equalsIgnoreCase(DELETE_DATA)) {
                            //  deleteData();
                            deleteEnumerationBlock(item);
                            askForPinDailog.dismiss();
                        } else if (deleteStatus.equalsIgnoreCase(CLEAN_DEVICE)) {
                            askForPinDailog.dismiss();
                            cleanDevice();
                        }
                    } else {
                        if (wrongPinCount >= 2) {
//                        errorTV.setTextColor(context.getResources().getColor(R.color.red));
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
//                        errorTV.setVisibility(View.VISIBLE);
//                        errorTV.setText("Enter correct pin");
                            pinET.setText("");
                            CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterValidPin));
//                        pinET.setText("");
                        }
//                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterValidPin));
                    }
                } else {

                    //alert  when pin login is diabled for 24 hrs
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.pinLoginDisabled));
//                    errorTV.setText("Pin login disabled for 24 hrs.");
                    pinET.setText("");
                    return;
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

    private void cleanDevice() {
        if (AppUtility.deleteFile(new File(DatabaseHelpers.DELETE_FOLDER_PATH))) {
            ProjectPrefrence.removeSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SELECTED_STATE, context);
            ProjectPrefrence.removeSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.DOWNLOADINGSOURCE, context);
            ProjectPrefrence.removeSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.VERIFIER_CONTENT, context);
            ProjectPrefrence.removeSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.MEMBER_DOWNLOADED_COUNT, context);
            ProjectPrefrence.removeSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.HOUSEHOLD_DOWNLOADED_COUNT, context);
            ProjectPrefrence.removeSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.dataDownloaded, context);
            Intent theIntent = new Intent(context, SplashNhps.class);
            theIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            theIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(theIntent);
            finish();
        }
        /*SeccDatabase.deleteTable(context,AppConstant.TRANSECT_POPULATE_SECC);
        SeccDatabase.deleteTable(context,AppConstant.HOUSE_HOLD_SECC);
        SeccDatabase.deleteTable(context,AppConstant.MEMBER_STATUS);
        SeccDatabase.deleteTable(context,AppConstant.AADHAAR_STATUS);
        SeccDatabase.deleteTable(context,AppConstant.HEALTH_SCHEME);
        SeccDatabase.deleteTable(context,AppConstant.MEMBER_ERROR_TABLE);*/

    }

    private void downloadEnumerationBlockData() {
        if (downloadAsyncTask != null && !downloadAsyncTask.isCancelled()) {
            downloadAsyncTask.cancel(true);
            downloadAsyncTask = null;
        }

        downloadAsyncTask = new BlockDownloadTask();
        downloadAsyncTask.execute();
    }

    private class BlockDownloadTask extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... params) {

            try {
                HashMap<String, String> response = CustomHttp.httpPost(AppConstant.HEALTH_SCHEME_API, healthSchemeRequest.serialize());
                String healthSchemeResponse = AppUtility.fixEncoding(response.get(AppConstant.RESPONSE_BODY));
                AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Member response : " + healthSchemeResponse);
                if (healthSchemeResponse != null) {
                    healthSchemeItemResponse = HealthSchemeItemResponse.create(healthSchemeResponse);
                    if (healthSchemeItemResponse != null) {
                        if (healthSchemeItemResponse.isStatus()) {
                            if (healthSchemeItemResponse.getStateHelathSchemeList() != null && healthSchemeItemResponse.getStateHelathSchemeList().size() > 0) {
                                try {
                                    Thread.sleep(1000);
                                    progressType = HOUSEHOLD_DOWNLOADED;
                                    //   publishProgress(HOUSEHOLD_DOWNLOADED);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            return SERVER_ERROR;
                        }
                    } else {
                        AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Download Status : Inside seccmember response null");
                        return INTERNET_LOST;
                    }
                }
            } catch (Exception e) {
                AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Download Status : Inside seccmember response exception" + e.toString());
                return INTERNET_LOST;
            }
            /////   downloading  Relation Master


            try {
                HashMap<String, String> response = CustomHttp.getStringRequest(AppConstant.RELATION_MASTER_API, AppConstant.AUTHORIZATION, AppConstant.AUTHORIZATIONVALUE);
                String relationMasterResponse = AppUtility.fixEncoding(response.get(AppConstant.RESPONSE_BODY));
                AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Member response : " + relationMasterResponse);
                if (relationMasterResponse != null) {
                    relationMasterItemResponse = RelationMasterItem.create(relationMasterResponse);
                    if (relationMasterItemResponse != null) {
                        if (relationMasterItemResponse.isStatus()) {
                            if (relationMasterItemResponse.getRelationMasterData() != null && relationMasterItemResponse.getRelationMasterData().size() > 0) {
                                try {
                                    Thread.sleep(1000);
                                    progressType = HOUSEHOLD_DOWNLOADED;
                                    //   publishProgress(HOUSEHOLD_DOWNLOADED);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            return SERVER_ERROR;
                        }
                    } else {
                        AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Download Status : Inside seccmember response null");
                        return INTERNET_LOST;
                    }
                }
            } catch (Exception e) {
                AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Download Status : Inside seccmember response exception" + e.toString());
                return INTERNET_LOST;
            }


            /////  downloading Relation Master


            try {
                String[] header = new String[2];

                header[0] = AppConstant.AUTHORIZATION;
                header[1] = AppConstant.AUTHORIZATIONVALUE;
                AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Household Request :" +
                        " " + houseHoldRequest.serialize() + " \n URL :" + AppConstant.SECC_HOUSE_HOLD_API);

                //if()
                HashMap<String, String> response = CustomHttp.httpPostWithHeader(AppConstant.SECC_HOUSE_HOLD_API,
                        houseHoldRequest.serialize(), header);
                String householdReq = houseHoldRequest.serialize();
                System.out.print(householdReq);
                String houseHoldResponse = AppUtility.fixEncoding(response.get(AppConstant.RESPONSE_BODY));

                AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Household response : " + houseHoldResponse);
                if (houseHoldResponse != null) {
                    //    String value = new String(houseHoldResponse.getBytes("UTF-8"));

                    houseHoldListResponse = SeccHouseholdResponse.create(houseHoldResponse);
                    if (houseHoldListResponse != null) {
                        if (houseHoldListResponse.isStatus()) {
                            if (houseHoldListResponse.getSeccHouseholdList() != null) {
                                if (houseHoldListResponse.getSeccHouseholdList().size() > 0) {
                                    try {
                                        Thread.sleep(1000);
                                        progressType = HOUSEHOLD_DOWNLOADED;
                                        //  publishProgress(HOUSEHOLD_DOWNLOADED);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    return DATA_NOT_ALLOTED;
                                }
                            }
                        } else {
                            return SERVER_ERROR;
                        }
                    }
                } else {
                    return INTERNET_LOST;
                }
            } catch (Exception e) {
                return INTERNET_LOST;
            }

            try {
                String[] header = new String[2];

                header[0] = AppConstant.AUTHORIZATION;
                header[1] = AppConstant.AUTHORIZATIONVALUE;
                AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Household Request :" +
                        " " + seccRequest.serialize() + " \n URL :" + AppConstant.SECC_MEMBER_API);
                HashMap<String, String> response = CustomHttp.httpPostWithHeader(AppConstant.SECC_MEMBER_API, seccRequest.serialize(), header);
                String secReq = seccRequest.serialize();
                System.out.print(secReq);
                String seccMemberResp = AppUtility.fixEncoding(response.get(AppConstant.RESPONSE_BODY));
                AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Member response : " + seccMemberResp);
                if (seccMemberResp != null) {
                    seccmemberResponse = SeccMemberResponse.create(seccMemberResp);
                    if (seccmemberResponse != null) {
                        if (seccmemberResponse.isStatus()) {
                            if (seccmemberResponse.getSeccMemberList() != null && seccmemberResponse.getSeccMemberList().size() > 0) {
                                try {
                                    Thread.sleep(1000);
                                    progressType = HOUSEHOLD_MEMBER_DOWNLOADED;
                                    //  publishProgress(HOUSEHOLD_MEMBER_DOWNLOADED);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                //  saveHouseholdData(selectedLocation);
                                //   return DOWNLOAD_COMPLETED;
                            } else {
                                return DATA_NOT_ALLOTED;
                            }
                        } else {
                            return SERVER_ERROR;
                        }
                    } else {
                        AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Download Status : Inside seccmember response null");
                        return INTERNET_LOST;
                    }
                }
            } catch (Exception e) {
                AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Download Status : Inside seccmember response exception" + e.toString());
                return INTERNET_LOST;
            }

            // downloading RSBY houseHold data.
            try {
                AppUtility.showLog(AppConstant.LOG_STATUS, TAG, " RSBY Household Request :" +
                        " " + houseHoldRequest.serialize() + " \n URL :" + AppConstant.RSBY_HOUSEHOLD_DOWNLOAD);
                String[] header = new String[2];
                header[0] = AppConstant.AUTHORIZATION;
                header[1] = AppConstant.AUTHORIZATIONVALUE;
                seccDownloadCountRequest.setAhlSubBlockCode(selectedLocation.getSubBlockcode());
                String payload = seccDownloadCountRequest.serialize();
                HashMap<String, String> response = CustomHttp.httpPost(AppConstant.RSBY_HOUSEHOLD_DOWNLOAD,
                        payload, header);
                String RsbyhouseHoldResponse = AppUtility.fixEncoding(response.get(AppConstant.RESPONSE_BODY));
                if (RsbyhouseHoldResponse != null) {
                    rsbyHouseHoldResponse = SeccHouseholdResponse.create(RsbyhouseHoldResponse);
                    if (rsbyHouseHoldResponse != null) {
                        if (rsbyHouseHoldResponse.isStatus()) {
                            if (rsbyHouseHoldResponse.getRsbyHouseholdReadList() != null) {
                                if (rsbyHouseHoldResponse.getRsbyHouseholdReadList().size() > 0) {
                                    try {
                                        Thread.sleep(1000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                }
                            }
                        } else {
                        }
                    }
                } else {
                }
            } catch (Exception e) {
            }

            // Rsby member data
            try {

                AppUtility.showLog(AppConstant.LOG_STATUS, TAG, " RSBY Household Request :" +
                        " " + seccRequest.serialize() + " \n URL :" + AppConstant.RSBY_MEMBER_DOWNLOAD);
                String[] header = new String[2];

                header[0] = AppConstant.AUTHORIZATION;
                header[1] = AppConstant.AUTHORIZATIONVALUE;
                seccDownloadCountRequest.setAhlSubBlockCode(selectedLocation.getSubBlockcode());
                String payload = seccDownloadCountRequest.serialize();
                HashMap<String, String> response = CustomHttp.httpPost(AppConstant.RSBY_MEMBER_DOWNLOAD, payload, header);
                String RsbyMemberResp = AppUtility.fixEncoding(response.get(AppConstant.RESPONSE_BODY));
                AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "RSBY Member response : " + RsbyMemberResp);
                if (RsbyMemberResp != null) {
                    rsbyMemberResponse = SeccMemberResponse.create(RsbyMemberResp);
                    if (rsbyMemberResponse != null) {
                        if (rsbyMemberResponse.isStatus()) {
                            if (rsbyMemberResponse.getRsbyMemberReadList() != null && rsbyMemberResponse.getRsbyMemberReadList().size() > 0) {
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            } else {
                            }
                        } else {
                        }
                    } else {
                        AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Download Status : Inside RSBYmember response null");
                    }
                }
            } catch (Exception e) {
                AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Download Status : Inside RSBYmember response exception" + e.toString());
            }

            if (seccmemberResponse.getSeccMemberList() != null && seccmemberResponse.getSeccMemberList().size() > 0) {
                seccmemberResponse = SyncUtility.prepareMemberItemForReadSync(seccmemberResponse, context);
                SyncUtility.prepareHouseHoldItemForReadSync(houseHoldListResponse, context);
                saveHouseholdData(selectedLocation);
                return DOWNLOAD_COMPLETED;
            }
            return "Task Completed.";
        }

        @Override
        protected void onPostExecute(String result) {
            if (progressBar != null && progressBar.isShowing()) {
                progressBar.dismiss();
            }
            if (result.equalsIgnoreCase(DOWNLOAD_COMPLETED)) {
                CustomAlert.alertWithOk(context, DOWNLOAD_COMPLETED);
//                progressBar.dismiss();
                myCountDownTimer.cancel();
                setLocationAndUpdateScreen();
            } else if (result.equalsIgnoreCase(INTERNET_LOST)) {
                myCountDownTimer.cancel();
                internetLostMessage(TRY_AGAIN_MSG);
            } else if (result.equalsIgnoreCase(DATA_NOT_ALLOTED)) {
                CustomAlert.alertWithOk(context, result);
                myCountDownTimer.cancel();
            }
        }

        @Override
        protected void onPreExecute() {
            if (progressBar != null && progressBar.isShowing()) {
                progressBar.dismiss();
                progressBar=null;
            }
            myCountDownTimer = new MyCountDownTimer(AppConstant.TIMMERTIME * 1000 + 1000, 1000);
            myCountDownTimer.start();

            progressBar = new ProgressDialog(BlockDetailActivity.this);
            progressBar.setIndeterminateDrawable(context.getResources().getDrawable(R.mipmap.nhps_logo));
            progressBar.setMessage(context.getResources().getString(R.string.please_wait));
            progressBar.setCancelable(false);
            progressBar.show();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            //txt.setText("Running..."+ values[0]);
            // progressBar.setProgress(values[0]);
            progressBar.setMessage(values[0]);
        }
    }

    private void internetLostMessage(String msg) {
        internetDiaolg = new AlertDialog.Builder(context).create();
        LayoutInflater factory = LayoutInflater.from(context);
        View alertView = factory.inflate(R.layout.internet_try_again_popup, null);
        internetDiaolg.setView(alertView);
        internetDiaolg.show();
        TextView tryGainMsgTV = (TextView) alertView.findViewById(R.id.deleteMsg);
        tryGainMsgTV.setText(msg);
        Button tryAgainBT = (Button) alertView.findViewById(R.id.tryAgainBT);
        tryAgainBT.setText(context.getResources().getString(R.string.tryAgain));
        Button cancelBT = (Button) alertView.findViewById(R.id.cancelBT);
        tryAgainBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                internetDiaolg.dismiss();
                downloadEnumerationBlockData();
            }
        });
        cancelBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                internetDiaolg.dismiss();
            }
        });
    }

    private AlertDialog showDialogBlock(final Activity act, CharSequence title, CharSequence message, CharSequence buttonYes) {
        final AlertDialog.Builder downloadDialog = new AlertDialog.Builder(act);
        downloadDialog.setTitle(title);
        downloadDialog.setMessage(message);
        downloadDialog.setCancelable(false);
        downloadDialog.setPositiveButton(buttonYes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {


            }
        });
//        downloadDialog.setNegativeButton(context.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
////                Intent loginIntent = new Intent(context, LoginActivity.class);
////                startActivity(loginIntent);
////                finish();
//            }
//        });
        return downloadDialog.show();
    }
    private static AlertDialog showDialog(final Activity act, CharSequence title, CharSequence message, CharSequence buttonYes, CharSequence buttonNo, String faildApi) {
        AlertDialog.Builder downloadDialog = new AlertDialog.Builder(act);
        downloadDialog.setTitle(title);
        downloadDialog.setMessage(message);
        downloadDialog.setPositiveButton(buttonYes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        downloadDialog.setNegativeButton(buttonNo, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        return downloadDialog.show();
    }

    private AlertDialog showDialog(final Activity act, CharSequence title, CharSequence message, CharSequence buttonYes) {
        AlertDialog.Builder downloadDialog = new AlertDialog.Builder(act);
        downloadDialog.setTitle(title);
        downloadDialog.setMessage(message);
        downloadDialog.setCancelable(false);
        downloadDialog.setPositiveButton(buttonYes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                downloadMemberMasterData();
            }
        });
        downloadDialog.setNegativeButton(context.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent loginIntent = new Intent(context, LoginActivity.class);
                startActivity(loginIntent);
                finish();
            }
        });
        return downloadDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SYNC_REQUEST) {
            //  setupScreen();
            BlockDetailActivity.this.recreate();
        }
    }

    private void updateLocation(VerifierLocationItem item) {
        stateNameTV.setText("-");
        wardCodeTV.setText("-");
        villTownNameTV.setText("-");
        distNameTV.setText("-");
        tehsilNameTV.setText("-");
        ebTV.setText("-");
        if (item != null) {
            stateNameTV.setText(item.getStateName());
            distNameTV.setText(item.getDistrictName());
            tehsilNameTV.setText(item.getTehsilName());
            villTownNameTV.setText(item.getVtName());
            wardCodeTV.setText(item.getWardCode());
            ebTV.setText(item.getBlockCode());
        }
    }

    private void proceedDailog(final VerifierLocationItem item) {
        checkAppConfig();
        proceedDailog = new AlertDialog.Builder(context).create();
        TextView stateNameTV, distTV, tehsilTV, vtNameTV, wardTV, ebTV;
        TextView blockCodeTV, noOfHouseholdTV, noOfMemberTV, syncedMemberTV, totalSyncedMemberTV, totalRsbyHouseholdTV;
        LayoutInflater factory = LayoutInflater.from(context);
        View alertView = factory.inflate(R.layout.proceed_dialog, null);
        proceedDailog.setView(alertView);
        Button validationBT = (Button) alertView.findViewById(R.id.goValidationBT);
        Button syncBT = (Button) alertView.findViewById(R.id.goSyncBT);
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
                item.getDistrictCode(), item.getTehsilCode(), item.getVtCode(), item.getWardCode(), item.getBlockCode(), AppConstant.SYNCED_STATUS_MEMBER + "") + "");


        syncBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SELECTED_BLOCK, item.serialize(), context);
                Intent theIntent = new Intent(context, SyncHouseholdActivity.class);
                startActivityForResult(theIntent, SYNC_REQUEST);
                AppUtility.navgateFromEb = true;
                leftTransition();
                proceedDailog.dismiss();
            }
        });

        validationBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Log.d(TAG,"Delete Status"+status);
                ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SELECTED_BLOCK, item.serialize(), context);
                ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.homeNavigation, AppConstant.blockDetailActivityNavigation, context);
                ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.dataDownloaded, "Y", context);
                Intent theIntent = new Intent(context, SearchActivityWithHouseHold.class);
                startActivity(theIntent);
                leftTransition();
                proceedDailog.dismiss();
            }
        });

        deleteBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SeccDatabase.countSurveyedHousehold(context, item, "", "") > 0) {
                    deleteSyncPrompt(item);
                } else {
                    pendingHouseholdDeletePrompt(DELETE_DATA, item);
                }
                proceedDailog.dismiss();
            }
        });
        cancelBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                proceedDailog.dismiss();
            }
        });
        proceedDailog.show();
    }

    private void deleteEnumerationBlock(VerifierLocationItem item) {
        String query = "delete from "
                + AppConstant.HOUSE_HOLD_SECC
                + " where statecode='"
                + item.getStateCode().trim()
                + "' AND districtcode='" +
                item.getDistrictCode().trim()
                + "' AND tehsilcode='" + item.getTehsilCode().trim()
                + "' AND towncode='" + item.getVtCode().trim() + "' AND wardid='" + item.getWardCode().trim()
                + "' AND ahlblockno ='" + item.getBlockCode().trim() + "'";
        SeccDatabase.deleteTable(query, context);
        String seccDeleteQuery = "delete from "
                + AppConstant.TRANSECT_POPULATE_SECC
                + " where statecode='"
                + item.getStateCode()
                + "' AND districtcode='" +
                item.getDistrictCode()
                + "' AND tehsilcode='" + item.getTehsilCode()
                + "' AND towncode='" + item.getVtCode() + "' AND wardid='" + item.getWardCode()
                + "' AND blockno ='" + item.getBlockCode() + "'";
        SeccDatabase.deleteTable(seccDeleteQuery, context);
        this.recreate();
    }

    private void checkUpdatedVersion(final int value) {
        VolleyTaskListener taskListener = new VolleyTaskListener() {
            @Override
            public void postExecute(String response) {
                AppUpdatVersionResponse respItem = AppUpdatVersionResponse.create(response);

                if (respItem != null) {
                    if (respItem.isStatus()) {
                        int versionCode = Integer.parseInt(respItem.getVersionCode());
                        if (!respItem.getVersionName().equalsIgnoreCase(findApplicationVersionName())) {
                            Intent theInten = new Intent(context, AppUpdateActivity.class);
                            theInten.putExtra("VERSION", respItem);
                            startActivity(theInten);
                            // finish();

                        } else {
                            if (value == 0) {
                                AppUtility.alertWithOk(context, context.getResources().getString(R.string.updateNotAvailable));
                            }
                        }
                    }
                }
            }

            @Override
            public void onError(VolleyError error) {
                if (value == 0) {

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


    private void downloadApplicationNotification() {

        VolleyTaskListener taskListener = new VolleyTaskListener() {
            @Override
            public void postExecute(String response) {
                NotificationResponse respItem = NotificationResponse.create(response);

                if (respItem != null) {
                    if (respItem.isStatus()) {
                        if (respItem.getNotificationList() != null && respItem.getNotificationList().size() > 0) {
                            for (NotificationModel item : respItem.getNotificationList()) {
                                CommonDatabase.saveNotification(item, context);
                            }
                            AppUtility.alertWithOk(context, context.getResources().getString(R.string.notificationDownloaded));
                        }

                    }
                }
            }

            @Override
            public void onError(VolleyError error) {
                AppUtility.alertWithOk(context, context.getResources().getString(R.string.couldNotDownloadNotification));
            }
        };
        CustomVolleyGet volleyGet = new CustomVolleyGet(taskListener, context.getResources().getString(R.string.checkingNotofication), AppConstant.APPLICATION_NOTIFICATION_URL, context);
        volleyGet.execute();
    }

    public class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {

            int progress = (int) (((AppConstant.TIMMERTIME * 1000 + 1000) - millisUntilFinished) / 100);
            if (progressBar != null && progressBar.isShowing()) {
                int seconds = (int) (((AppConstant.TIMMERTIME * 1000 + 1000) - millisUntilFinished) / 1000);
                int minutes = seconds / 60;
                seconds = seconds % 60;

                progressBar.setMessage(progressType + "\n" + (String.format("%02d", minutes)
                        + ":" + String.format("%02d", seconds)));

            }
        }

        @Override
        public void onFinish() {
            if (progressBar != null && progressBar.isShowing()) {
                // progressBar.setProgress(0);
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

    private String findApplicationVersionName() {
        PackageManager packageManager = context.getPackageManager();
        String packageName = context.getPackageName();
        String versionCode = null; // initialize String

        try {
            versionCode = packageManager.getPackageInfo(packageName, 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    private void downloadNotificationData() {
        if (notificationAsyncTask != null && !notificationAsyncTask.isCancelled()) {
            notificationAsyncTask.cancel(true);
            notificationAsyncTask = null;
        }
        notificationAsyncTask = new notificationDownloadTask();
        notificationAsyncTask.execute();
    }

    private class notificationDownloadTask extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... params) {
            try {
                String url = AppConstant.APPLICATION_NOTIFICATION_URL + selectedStateItem.getStateCode();
                HashMap<String, String> response = CustomHttp.getStringRequest(AppConstant.APPLICATION_NOTIFICATION_URL + selectedStateItem.getStateCode(), AppConstant.AUTHORIZATION, AppConstant.AUTHORIZATIONVALUE);
                Log.d("response tag", String.valueOf(response));
                String healthSchemeResponse = AppUtility.fixEncoding(response.get(AppConstant.RESPONSE_BODY));
                AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Member response : " + healthSchemeResponse);
                if (healthSchemeResponse != null) {
                    NotificationResponse respItem = NotificationResponse.create(healthSchemeResponse);

                    if (respItem != null) {
                        if (respItem.isStatus()) {
                            String query = "delete from " + AppConstant.NOTIFICATION_TABLE;
                            SeccDatabase.deleteTable(query, context);
                            if (respItem.getNotificationList() != null && respItem.getNotificationList().size() > 0) {
                                for (NotificationModel item : respItem.getNotificationList()) {

                                    CommonDatabase.saveNotification(item, context);
                                    //showNotification(v);
                                }
                                return DOWNLOAD_COMPLETED;

                            }

                        }
                    }
                }
            } catch (Exception e) {
                AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Download Status : Inside seccmember response exception" + e.toString());
                return INTERNET_LOST;
            }

            return "Task Completed.";
        }

        @Override
        protected void onPostExecute(String result) {

            if (result.equalsIgnoreCase(DOWNLOAD_COMPLETED)) {
                showNotification();
            } else if (result.equalsIgnoreCase(INTERNET_LOST)) {

            }

        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onProgressUpdate(String... values) {

        }
    }

    private void startAppConfigActivity() {
        Intent theIntent = new Intent(context, AppConfigActivity.class);
        startActivity(theIntent);
    }

    private String checkAppConfig() {
        downloadSource = ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.DOWNLOADINGSOURCE, context);
        ArrayList<ConfigurationItem> configList = SeccDatabase.findConfiguration(selectedStateItem.getStateCode(), context);
        if (configList != null) {
            for (ConfigurationItem item1 : configList) {
                if (item1.getConfigId().equalsIgnoreCase(AppConstant.SECC_DOWNLOAD)) {
                    SeccDownloaded = item1.getStatus();
                }
                if (item1.getConfigId().equalsIgnoreCase(AppConstant.RSBY_DATA_SOURCE_CONFIG)) {
                    dataSoursce = item1.getStatus();
                }
                if (item1.getConfigId().equalsIgnoreCase(AppConstant.APPLICATION_ZOOM)) {
                    zoomMode = item1.getStatus();
                }
                if (downloadSource != null && !downloadSource.equalsIgnoreCase("")) {

                } else {
                    if (item1.getConfigId().equalsIgnoreCase(AppConstant.DATA_DOWNLOAD)) {
                        downloadSource = item1.getStatus();
                    }

                }
            }
        }


        fragmentManager = getSupportFragmentManager();

        if (zoomMode.equalsIgnoreCase("N")) {
            setContentView(R.layout.activity_block_detail);
            setupWothoutZoomScreen();
        } else {
            setContentView(R.layout.dummy_layout_for_zooming);
            setupZoomScreen();
        }


        return dataSoursce;
    }

    private void openOptionFragment() {
        scrollView.setVisibility(View.GONE);
        fragment = new BeneficiaryFamilySearchFragment();
        fragmentTransection = fragmentManager.beginTransaction();
        fragmentTransection.add(R.id.fragContainer, fragment);
        fragmentTransection.commitAllowingStateLoss();
      /*  scrollView.setVisibility(View.GONE);
        fragment = new OptionActivity();
        fragmentTransection = fragmentManager.beginTransaction();
        fragmentTransection.add(R.id.fragContainer, fragment);
        fragmentTransection.commitAllowingStateLoss();*/
    }

    private void openVillageDownloadFragment() {
        scrollView.setVisibility(View.GONE);
        fragment = new VillageWiseDownloadFragment();
        fragmentTransection = fragmentManager.beginTransaction();
        fragmentTransection.add(R.id.fragContainer, fragment);
        fragmentTransection.commitAllowingStateLoss();
    }

    private void openWardDownloadFragment() {
        scrollView.setVisibility(View.GONE);
        fragment = new WardWiseDownloadFragment();
        fragmentTransection = fragmentManager.beginTransaction();
        fragmentTransection.add(R.id.fragContainer, fragment);
        fragmentTransection.commitAllowingStateLoss();
    }

    private void openSubEbDownloadFragment() {
        scrollView.setVisibility(View.GONE);
        fragment = new SubEBWiseDownloadFragment();
        fragmentTransection = fragmentManager.beginTransaction();
        fragmentTransection.add(R.id.fragContainer, fragment);
        fragmentTransection.commitAllowingStateLoss();
    }

    private void openWithoutSeccFragment() {
        scrollView.setVisibility(View.GONE);
        fragment = new WithoutSeccDataDownloading();
        fragmentTransection = fragmentManager.beginTransaction();
        fragmentTransection.add(R.id.fragContainer, fragment);
        fragmentTransection.commitAllowingStateLoss();
    }

    private void checkDataCount(final String request) {

        TaskListener taskListener = new TaskListener() {
            @Override
            public void execute() {
                try {
                    HashMap<String, String> response = CustomHttp.httpPost(AppConstant.GET_DATA_DOWNLOAD_COUNT, request);
                    if (response != null) {
                        dataCountModel = new DataCountModel().create(response.get(AppConstant.RESPONSE_BODY));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void updateUI() {
                if (dataCountModel != null) {
                    if (dataCountModel.isStatus()) {
                        if (dataCountModel.getDownloadCountList() != null && dataCountModel.getDownloadCountList().size() > 0) {
                            String seccMemberCount = dataCountModel.getDownloadCountList().get(0).getMembersCount();
                            String seccHouseHoldCount = dataCountModel.getDownloadCountList().get(0).getHouseHoldCount();
                            String rsbyMemberCount = dataCountModel.getDownloadCountList().get(0).getRsbyMembersCount();
                            String rsbyHouseHoldCount = dataCountModel.getDownloadCountList().get(0).getRsbyHouseHoldCount();
                            countPopUp(seccHouseHoldCount, seccMemberCount, rsbyHouseHoldCount, rsbyMemberCount);
                        }
                    } else {
                        if (dataCountModel.getErrorMessage() != null) {
                            AppUtility.alertWithOk(context, dataCountModel.getErrorMessage());
                        }
                    }
                } else {
                    AppUtility.alertWithOk(context, context.getResources().getString(R.string.server_error));
                }
            }
        };
        if (countAsyncTask != null) {
            countAsyncTask.cancel(true);
            countAsyncTask = null;
        }


        countAsyncTask = new CustomAsyncTask(taskListener, context.getResources().getString(R.string.please_wait), context);
        countAsyncTask.execute();

    }

    private void countPopUp(final String seccHousehold, final String seccMember, final String rsbyHousehold, final String rsbyMember) {

        final AlertDialog askForPinDailog = new AlertDialog.Builder(context).create();
        LayoutInflater factory = LayoutInflater.from(context);
        View alertView = factory.inflate(R.layout.show_count, null);
        askForPinDailog.setView(alertView);
        askForPinDailog.setCancelable(false);
        askForPinDailog.show();
        Button proceedBT = (Button) alertView.findViewById(R.id.proceedBT);
        Button cancelBT = (Button) alertView.findViewById(R.id.cancelBT);
        TextView seccHouseholdCount = (TextView) alertView.findViewById(R.id.seccHouseholdCount);
        TextView seccMemberCount = (TextView) alertView.findViewById(R.id.seccMemberCount);
        TextView rsbyHouseholdCount = (TextView) alertView.findViewById(R.id.rsbyHouseholdCount);
        TextView rsbyMemberCount = (TextView) alertView.findViewById(R.id.rsbyMemberCount);

        seccHouseholdCount.setText(seccHousehold);
        seccMemberCount.setText(seccMember);
        rsbyHouseholdCount.setText(rsbyHousehold);
        rsbyMemberCount.setText(rsbyMember);
        proceedBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askForPinDailog.dismiss();
                if (SeccDatabase.getDataCount(context) != null) {
                    DownloadedDataCountModel storedDataCountModel = SeccDatabase.getDataCount(context);
                    int seccMemberN = Integer.parseInt(storedDataCountModel.getSeccMemberCount());
                    int seccHouseholdN = Integer.parseInt(storedDataCountModel.getSeccHouseholdCount());
                    int rsbyMemberN = Integer.parseInt(storedDataCountModel.getRsbyMemberCount());
                    int rsbyHouseholdN = Integer.parseInt(storedDataCountModel.getRsbyHouseholdCount());

                    DownloadedDataCountModel dataCountModel = new DownloadedDataCountModel();
                    dataCountModel.setId("1");
                    dataCountModel.setSeccMemberCount(String.valueOf(Integer.parseInt(seccMember) + seccMemberN));
                    dataCountModel.setSeccHouseholdCount(String.valueOf(Integer.parseInt(seccHousehold) + seccHouseholdN));
                    dataCountModel.setRsbyMemberCount(String.valueOf(Integer.parseInt(rsbyMember) + rsbyMemberN));
                    dataCountModel.setRsbyHouseholdCount(String.valueOf(Integer.parseInt(rsbyHousehold) + rsbyHouseholdN));
                    CommonDatabase.updateDataCount(dataCountModel, context);
                } else {
                    DownloadedDataCountModel dataCountModel = new DownloadedDataCountModel();
                    dataCountModel.setId("1");
                    dataCountModel.setSeccMemberCount(seccMember);
                    dataCountModel.setSeccHouseholdCount(seccHousehold);
                    dataCountModel.setRsbyMemberCount(rsbyMember);
                    dataCountModel.setRsbyHouseholdCount(rsbyHousehold);
                    CommonDatabase.saveDataCount(dataCountModel, context);
                }

//                    by rajesh kumar
                if (seccHousehold == null || seccHousehold.equalsIgnoreCase("0") || seccHousehold.equalsIgnoreCase("") ) {

                    showDialogBlock(BlockDetailActivity.this, context.getResources().getString(R.string.Alert), BLOCK_HOUSEHOLD_EMPTY_MSG, context.getResources().getString(R.string.tryAgain));

                } else {
                    downloadEnumerationBlockData();
                }
//                downloadEnumerationBlockData();
            }
        });
        cancelBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askForPinDailog.dismiss();
            }
        });
    }

    @Override
    public void onLowMemory() {
        try {
            Runtime.getRuntime().gc();
        } catch (Exception ex) {

        }
    }

    private void downloadConfigurationData() {
        if (configurationAsyncTask != null && !configurationAsyncTask.isCancelled()) {
            configurationAsyncTask.cancel(true);
            configurationAsyncTask = null;
        }
        configurationAsyncTask = new configurationDownloadTask();
        configurationAsyncTask.execute();
    }

    private class configurationDownloadTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                String url = AppConstant.APPLICATION_CONFIGURATION_URL + selectedStateItem.getStateCode();
                HashMap<String, String> response = CustomHttp.getStringRequest(AppConstant.APPLICATION_CONFIGURATION_URL + selectedStateItem.getStateCode(), AppConstant.AUTHORIZATION, AppConstant.AUTHORIZATIONVALUE);
                String configurationResp = AppUtility.fixEncoding(response.get(AppConstant.RESPONSE_BODY));
                System.out.print(configurationResp);
                if (configurationResp != null) {
                    ApplicationConfigListModel respItem = ApplicationConfigListModel.create(configurationResp);
                    if (respItem != null) {
                        if (respItem.isStatus()) {
                            String query = "delete from " + AppConstant.new_application_configuration;
                            SeccDatabase.deleteTable(query, context);
                            if (respItem.getAppStateConfigList() != null && respItem.getAppStateConfigList().size() > 0) {
                                for (ApplicationConfigurationModel item : respItem.getAppStateConfigList()) {
                                    CommonDatabase.saveApplicationConfigData(item, context);
                                }
                                //  return DOWNLOAD_COMPLETED;
                            }
                        }
                    }
                }
            } catch (Exception e) {
                AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Download Status : Inside seccmember response exception" + e.toString());
                return INTERNET_LOST;
            }

            try {
                HashMap<String, String> response = CustomHttp.getStringRequest(AppConstant.GET_RSBY_INSURANCE_COMPANY_MASTER, AppConstant.AUTHORIZATION, AppConstant.AUTHORIZATIONVALUE);
                String configurationResp = AppUtility.fixEncoding(response.get(AppConstant.RESPONSE_BODY));
                System.out.print(configurationResp);
                if (configurationResp != null) {
                    RsbyPoliciesCompanyMasterList respItem = RsbyPoliciesCompanyMasterList.create(configurationResp);
                    if (respItem != null) {
                        if (respItem.isStatus()) {
                            String query = "delete from " + AppConstant.RSBY_POLICY_COMPANY;
                            SeccDatabase.deleteTable(query, context);
                            if (respItem.getRsbyInsuranceCompanyList() != null && respItem.getRsbyInsuranceCompanyList().size() > 0) {
                                for (RsbyPoliciesCompany item : respItem.getRsbyInsuranceCompanyList()) {
                                    CommonDatabase.saveRsbyPoliciesCompany(item, context);
                                }
                                //           return DOWNLOAD_COMPLETED;
                            }
                        }
                    }
                }
            } catch (Exception e) {
                AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Download Status : Inside seccmember response exception" + e.toString());
                return INTERNET_LOST;
            }


            try {
                HashMap<String, String> response = CustomHttp.getStringRequest(AppConstant.GET_RSBY_CATEGORY_MASTER, AppConstant.AUTHORIZATION, AppConstant.AUTHORIZATIONVALUE);
                String configurationResp = AppUtility.fixEncoding(response.get(AppConstant.RESPONSE_BODY));
                System.out.print(configurationResp);
                if (configurationResp != null) {
                    RsbyCardCategoryMasterList respItem = RsbyCardCategoryMasterList.create(configurationResp);
                    if (respItem != null) {
                        if (respItem.isStatus()) {
                            String query = "delete from " + AppConstant.RSBY_CARD_CAT_MASTER_TABLE;
                            SeccDatabase.deleteTable(query, context);
                            if (respItem.getRsbyCategoryList() != null && respItem.getRsbyCategoryList().size() > 0) {
                                for (RsbyCardCategoryItem item : respItem.getRsbyCategoryList()) {
                                    CommonDatabase.saveRsbyCardCategory(item, context);
                                }
                                //         return DOWNLOAD_COMPLETED;
                            }
                        }
                    }
                }
            } catch (Exception e) {
                AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Download Status : Inside seccmember response exception" + e.toString());
                return INTERNET_LOST;
            }

            try {
              /* if(SeccDatabase.getRsbyPolicyItemList(context)!=null && SeccDatabase.getRsbyPolicyItemList(context).size()>0 ) {
               }else {*/
                HashMap<String, String> response = CustomHttp.getStringRequest(AppConstant.GET_RSBY_POLICIES_MASTER, AppConstant.AUTHORIZATION, AppConstant.AUTHORIZATIONVALUE);
                String configurationResp = AppUtility.fixEncoding(response.get(AppConstant.RESPONSE_BODY));
                System.out.print(configurationResp);
                if (configurationResp != null) {
                    RsbyPoliciesMasterList respItem = RsbyPoliciesMasterList.create(configurationResp);
                    if (respItem != null) {
                        if (respItem.isStatus()) {
                            String query = "delete from " + AppConstant.RSBY_POLICIES_TABLE;
                            SeccDatabase.deleteTable(query, context);
                            if (respItem.getRsbyPoliciesList() != null && respItem.getRsbyPoliciesList().size() > 0) {
                                for (RSBYPoliciesItem item : respItem.getRsbyPoliciesList()) {
                                    CommonDatabase.saveRsbyPoliceItem(item, context);
                                }
                                //        return DOWNLOAD_COMPLETED;
                            }
                        }
                    }
                }
                // }
            } catch (Exception e) {
                AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Download Status : Inside seccmember response exception" + e.toString());
                return INTERNET_LOST;
            }


            try {
            /*   if(SeccDatabase.getRsbyMemberRelationCode(context)!=null && SeccDatabase.getRsbyMemberRelationCode(context).size()>0 ) {
               }else{*/
                HashMap<String, String> response = CustomHttp.getStringRequest(AppConstant.GET_RSBY_RELATION_MASTER, AppConstant.AUTHORIZATION, AppConstant.AUTHORIZATIONVALUE);
                String configurationResp = AppUtility.fixEncoding(response.get(AppConstant.RESPONSE_BODY));
                System.out.print(configurationResp);
                if (configurationResp != null) {
                    RsbyRelationMasterList respItem = RsbyRelationMasterList.create(configurationResp);
                    if (respItem != null) {
                        if (respItem.isStatus()) {
                            String query = "delete from " + AppConstant.RSBY_RELATION_TABLE;
                            SeccDatabase.deleteTable(query, context);
                            if (respItem.getRsbyRelationList() != null && respItem.getRsbyRelationList().size() > 0) {
                                for (RsbyRelationItem item : respItem.getRsbyRelationList()) {
                                    CommonDatabase.saveRsbyRelation(item, context);
                                }
                                //  return DOWNLOAD_COMPLETED;
                            }
                        }
                    }
                }
                //}
            } catch (Exception e) {
                AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Download Status : Inside seccmember response exception" + e.toString());
                return INTERNET_LOST;
            }

            try {
                String url = AppConstant.APPLICATION_NOTIFICATION_URL + selectedStateItem.getStateCode();
                HashMap<String, String> response = CustomHttp.getStringRequest(AppConstant.APPLICATION_NOTIFICATION_URL + selectedStateItem.getStateCode(), AppConstant.AUTHORIZATION, AppConstant.AUTHORIZATIONVALUE);
                Log.d("response tag", String.valueOf(response));
                String healthSchemeResponse = AppUtility.fixEncoding(response.get(AppConstant.RESPONSE_BODY));
                AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Member response : " + healthSchemeResponse);
                if (healthSchemeResponse != null) {
                    NotificationResponse respItem = NotificationResponse.create(healthSchemeResponse);

                    if (respItem != null) {
                        if (respItem.isStatus()) {
                            String query = "delete from " + AppConstant.NOTIFICATION_TABLE;
                            SeccDatabase.deleteTable(query, context);
                            if (respItem.getNotificationList() != null && respItem.getNotificationList().size() > 0) {
                                for (NotificationModel item : respItem.getNotificationList()) {

                                    CommonDatabase.saveNotification(item, context);
                                    //showNotification(v);
                                }
                                return DOWNLOAD_COMPLETED;

                            }

                        }
                    }
                }
            } catch (Exception e) {
                AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Download Status : Inside seccmember response exception" + e.toString());
                return INTERNET_LOST;
            }
            return "Task Completed.";
        }

        @Override
        protected void onPostExecute(String result) {
            if (progressBar != null && progressBar.isShowing()) {
                progressBar.dismiss();

            }
            if (result.equalsIgnoreCase(DOWNLOAD_COMPLETED)) {
                checkAppConfig();
            } else if (result.equalsIgnoreCase(INTERNET_LOST)) {
                alertWithOk(context, "Application Configuration not downloaded.\nPlease retry.");
                // CustomAlert.alertWithOk(context,"");
                //  myCountDownTimer.cancel();
                // internetLostMessage(TRY_AGAIN_MSG);
            } else {
                alertWithOk(context, "Application Configuration not downloaded.\nPlease retry.");
            }
           /* txt.setText(result);
            btn.setText("Restart");*/
        }

        @Override
        protected void onPreExecute() {
            //txt.setText("Task Starting...");
            progressBar = new ProgressDialog(BlockDetailActivity.this);
            progressBar.setIndeterminateDrawable(context.getResources().getDrawable(R.mipmap.nhps_logo));
            progressBar.setMessage(context.getResources().getString(R.string.updatingMasters));
            progressBar.setCancelable(false);
            progressBar.show();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            //txt.setText("Running..."+ values[0]);
            // progressBar.setProgress(values[0]);
        }
    }

    public void alertWithOk(Context mContext, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Alert");
        builder.setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                        downloadConfigurationData();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

/*    private void downloadNotificationData() {
        if (notificationAsyncTask != null && !notificationAsyncTask.isCancelled()) {
            notificationAsyncTask.cancel(true);
            notificationAsyncTask = null;
        }
        notificationAsyncTask = new notificationDownloadTask();
        notificationAsyncTask.execute();
    }

    private class notificationDownloadTask extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... params) {
            try {
                String url = AppConstant.APPLICATION_NOTIFICATION_URL + selectedStateItem.getStateCode();
                HashMap<String, String> response = CustomHttp.getStringRequest(AppConstant.APPLICATION_NOTIFICATION_URL + selectedStateItem.getStateCode(), AppConstant.AUTHORIZATION, AppConstant.AUTHORIZATIONVALUE);
                Log.d("response tag", String.valueOf(response));
                String healthSchemeResponse = AppUtility.fixEncoding(response.get(AppConstant.RESPONSE_BODY));
                AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Member response : " + healthSchemeResponse);
                if (healthSchemeResponse != null) {
                    NotificationResponse respItem = NotificationResponse.create(healthSchemeResponse);

                    if (respItem != null) {
                        if (respItem.isStatus()) {
                            String query = "delete from " + AppConstant.NOTIFICATION_TABLE;
                            SeccDatabase.deleteTable(query, context);
                            if (respItem.getNotificationList() != null && respItem.getNotificationList().size() > 0) {
                                for (NotificationModel item : respItem.getNotificationList()) {

                                    CommonDatabase.saveNotification(item, context);
                                    //showNotification(v);
                                }
                                return DOWNLOAD_COMPLETED;

                            }

                        }
                    }
                }
            } catch (Exception e) {

                return INTERNET_LOST;
            }

            return "Task Completed.";
        }

        @Override
        protected void onPostExecute(String result) {

            if (result.equalsIgnoreCase(DOWNLOAD_COMPLETED)) {

            } else if (result.equalsIgnoreCase(INTERNET_LOST)) {

            }

        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onProgressUpdate(String... values) {

        }
    }*/


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
//                        errorTV.setVisibility(View.VISIBLE);
//                        errorTV.setText("Enter correct pin");
//                        pinET.setText("");
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
        cancelBT.setOnClickListener(new View.OnClickListener()

        {
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
