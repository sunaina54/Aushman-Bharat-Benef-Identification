package com.nhpm.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.customComponent.CustomAlert;
import com.customComponent.CustomAsyncTask;
import com.customComponent.Networking.CustomVolleyHeaderGet;
import com.customComponent.Networking.VolleyTaskListener;
import com.customComponent.utility.CustomHttp;
import com.customComponent.utility.ProjectPrefrence;
import com.nhpm.LocalDataBase.DatabaseHelpers;
import com.nhpm.LocalDataBase.dto.CommonDatabase;
import com.nhpm.LocalDataBase.dto.SeccDatabase;
import com.nhpm.Models.DownloadedDataCountModel;
import com.nhpm.Models.response.master.HealthSchemeItem;
import com.nhpm.Models.response.master.MemberStatusItem;
import com.nhpm.Models.response.master.StateItem;
import com.nhpm.Models.response.master.response.AadhaarStatusItemResponse;
import com.nhpm.Models.response.master.response.HealthSchemeItemResponse;
import com.nhpm.Models.response.master.response.HealthSchemeRequest;
import com.nhpm.Models.response.master.response.MemberStatusItemResponse;
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
import com.nhpm.activity.BlockDetailActivity;
import com.nhpm.activity.LoginActivity;
import com.nhpm.activity.SearchActivityWithHouseHold;
import com.nhpm.activity.SyncHouseholdActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Saurabh on 14-07-2017.
 */

public class SubEBWiseDownloadFragment extends Fragment {
    private CustomAsyncTask countAsyncTask;
    private DataCountModel dataCountModel;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static int SYNC_REQUEST = 1;
    private final String TAG = "BlockDetailActivity";
    private final String INTERNET_LOST = "1";
    private final String SERVER_ERROR = "2";
    private String mParam1;
    private Spinner blockSP;
    private String mParam2;
    private DatabaseHelpers dbHelper;
    private TextView headerTV;
    private ProgressDialog progressBar;
    private ArrayList<VerifierLocationItem> dowloadedBlockList;
    private ArrayList<String> notDownloadedBlock;
    private Button downloadBT;
    private SeccMemberResponse memberListResponse;
    private SeccHouseholdResponse houseHoldListResponse;
    private SeccMemberResponse rsbyMemberResponse;
    private SeccHouseholdResponse rsbyHouseHoldResponse;
    private TextView stateNameTV, distNameTV, tehsilNameTV, villTownNameTV, wardCodeTV, verifierNameTV, vtLableTV, ebTV;
    private TextView pstateNameTV, pdistNameTV, ptehsilNameTV, pvillTownNameTV, pwardCodeTV;
    private AlertDialog dialog, deleteDialog, askForPinDailog, internetDiaolg, proceedDailog;
    private VerifierLoginResponse verifierDetail;
    private CardView syncbuttonLayout;
    private SeccHouseholdResponse houseHoldResponse;
    private LinearLayout downloadBloackLayout;
    private Button resetHouseHoldBT;
    private CustomAsyncTask asyncTask;
    private MemberStatusItemResponse statusItemResponse;
    private AadhaarStatusItemResponse aadhaarStatusItem;
    private HealthSchemeItemResponse healthSchemeItemResponse;
    private String blockCode;
    private VerifierLoginResponse storedLoginResponse;
    private HouseHoldRequest houseHoldRequest;
    private SeccMemberRequest seccRequest;
    private SeccHouseholdResponse seccHouseHoldResponse;
    private SeccMemberResponse seccmemberResponse;
    private VerifierLocationItem verifierLocation;
    private Button syncAadhaarCollectedBT;
    private String CLEAN_DEVICE = "1", DELETE_DATA = "2";
    private RelativeLayout syncRelativeLayout;
    private Integer count = 1;
    private String HOUSEHOLD_DOWNLOADED;
    private String HOUSEHOLD_MEMBER_DOWNLOADED;
    private String DATA_NOT_ALLOTED;
    private Context context;
    private String DOWNLOAD_COMPLETED;
    private String TRY_AGAIN_MSG;
    private BlockDownloadTask downloadAsyncTask;
    private String MEMBER_STATUS = "1", AADHAAR_STATUS = "2", HEALTH_STATUS = "3";
    private String masterMsg;
    private String serverIssues;
    private TextView areaTV;
    private ArrayList<VerifierLocationItem> locationList;
    private int selectedIndexForDownload;
    private CardView locationCard;
    private VerifierLocationItem selectedLocation;
    private HealthSchemeRequest healthSchemeRequest;
    private BlockDetailActivity activity;
    private String progressType = "";
    private MyCountDownTimer myCountDownTimer;
    private String dataSoursce = "b";
    private StateItem selectedStateItem;
    private RelativeLayout proceedLayout;
    private ArrayList<HouseHoldItem> houseHoldList;
    private Button pProceedBT;
    private TextView subEbCodeTV, subEbNameTV;
    private TextView pebTV;
    private TextView psubEbTV;
    private RecyclerView blockList;
    private CustomAdapter adapter;

    private int wrongPinCount = 0;
    private long wrongPinSavedTime;
    private long currentTime;
    private TextView wrongAttempetCountValue, wrongAttempetCountText;
    private long millisecond24 = 86400000;




    public SubEBWiseDownloadFragment() {
    }

    // TODO: Rename and change types and number of parameters
    public static SubEBWiseDownloadFragment newInstance(String param1, String param2) {
        SubEBWiseDownloadFragment fragment = new SubEBWiseDownloadFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sub_eb_wise_download, container, false);
        setupScreen(view);
        return view;
    }

    private void setupScreen(View v) {
        context = getActivity();
        progressBar = new ProgressDialog(getActivity());
        DOWNLOAD_COMPLETED = context.getResources().getString(R.string.dataSetuped);
        TRY_AGAIN_MSG = context.getResources().getString(R.string.tryAgainMessage);
        masterMsg = context.getResources().getString(R.string.configSetting);
        HOUSEHOLD_MEMBER_DOWNLOADED = context.getResources().getString(R.string.applicationSetUpMsg);
        HOUSEHOLD_DOWNLOADED = context.getResources().getString(R.string.plzWaitDataDownloading);
        DATA_NOT_ALLOTED = context.getResources().getString(R.string.noHouseHoldAllotted);
        //  mZoomLinearLayout = (LinearLayout) findViewById(R.id.mZoomLinearLayout);
       /* View v = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.activity_block_detail, null, false);
        v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));*/
      /*  downloadNotificationData();
        showNotification(v);*/
        // sendMsg();
        // dbHelper = DatabaseHelpers.getInstance(context);
       /* verifierLocation = VerifierLocationItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF
                , AppConstant.SELECTED_BLOCK, context));*/
        verifierDetail = VerifierLoginResponse.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.VERIFIER_CONTENT, context));
        if (verifierDetail.getLocationList() != null) {
            verifierLocation = verifierDetail.getLocationList().get(0);
            selectedLocation = verifierLocation;
        }
        storedLoginResponse = VerifierLoginResponse.create(
                ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.VERIFIER_CONTENT, context));
        locationCard = (CardView) v.findViewById(R.id.locationCard);
        headerTV = (TextView) v.findViewById(R.id.centertext);
        resetHouseHoldBT = (Button) v.findViewById(R.id.resetDataBT);
        syncRelativeLayout = (RelativeLayout) v.findViewById(R.id.syncRelativeLayout);
        areaTV = (TextView) v.findViewById(R.id.areaTV);
        ebTV = (TextView) v.findViewById(R.id.enumerationBlockTV);
        subEbCodeTV = (TextView) v.findViewById(R.id.subEbCodeTV);
        subEbNameTV = (TextView) v.findViewById(R.id.subEbNameTV);
        blockList = (RecyclerView) v.findViewById(R.id.blockList);
        downloadBT = (Button) v.findViewById(R.id.downloadBT);
        blockSP = (Spinner) v.findViewById(R.id.blockSP);
    /*    menuLayout = (RelativeLayout) v.findViewById(R.id.menuLayout);
        menuLayout.setVisibility(View.VISIBLE);*/
        verifierNameTV = (TextView) v.findViewById(R.id.verifierNameTV);
        verifierNameTV.setText(verifierDetail.getName());

        syncbuttonLayout = (CardView) v.findViewById(R.id.syncButtonCardView);
        downloadBloackLayout = (LinearLayout) v.findViewById(R.id.downloadBloackLayout);
        syncAadhaarCollectedBT = (Button) v.findViewById(R.id.syncAadhaarCollectedBT);
        proceedLayout = (RelativeLayout) v.findViewById(R.id.proceedLayout);
        if (verifierLocation != null) {

            houseHoldList = SeccDatabase.getSeccHouseHoldWardWiseList(verifierLocation.getStateCode(), verifierLocation.getDistrictCode(), verifierLocation.getTehsilCode(), verifierLocation.getVtCode(), verifierLocation.getWardCode(), context);
        }
        pstateNameTV = (TextView) v.findViewById(R.id.pStateNameTV);
        pdistNameTV = (TextView) v.findViewById(R.id.pDistNameTV);
        ptehsilNameTV = (TextView) v.findViewById(R.id.pTehsilNameTV);
        pvillTownNameTV = (TextView) v.findViewById(R.id.pVtNameTV);
        pwardCodeTV = (TextView) v.findViewById(R.id.wwardCodeTV);
        pebTV = (TextView) v.findViewById(R.id.ebTV);
        psubEbTV = (TextView) v.findViewById(R.id.subEbTV);
        blockList.setHasFixedSize(true);
        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        blockList.setLayoutManager(mLayoutManager);
        //  proceedLayout
        pProceedBT = (Button) v.findViewById(R.id.pProceedBT);

        stateNameTV = (TextView) v.findViewById(R.id.stateNameTV);
        distNameTV = (TextView) v.findViewById(R.id.distNameTV);
        tehsilNameTV = (TextView) v.findViewById(R.id.tehsilNameTV);
        villTownNameTV = (TextView) v.findViewById(R.id.villTownNameTV);
        wardCodeTV = (TextView) v.findViewById(R.id.wardNameTV);
        vtLableTV = (TextView) v.findViewById(R.id.vtLableTV);


        if (verifierLocation != null) {
            stateNameTV.setText(verifierLocation.getStateName());
            distNameTV.setText(verifierLocation.getDistrictName());
            tehsilNameTV.setText(verifierLocation.getTehsilCode());
            villTownNameTV.setText(verifierLocation.getVtName());
            wardCodeTV.setText(verifierLocation.getWardCode());
            subEbCodeTV.setText(verifierLocation.getSubBlockcode());
            subEbNameTV.setText(verifierLocation.getSubBlockcode());
            ebTV.setText(verifierLocation.getBlockCode());
        }
        syncbuttonLayout.setVisibility(View.GONE);
        //downloadBloackLayout.setVisibility(View.VISIBLE);
        //  showDownloadedData();
/*        zoomView = new ZoomView(context);
        zoomView.addView(v);
        mZoomLinearLayout.addView(zoomView);*/
     /*   settings.setVisibility(View.VISIBLE);*/
        if (SeccDatabase.getMemberStatusList(context).size() > 0) {

        } else {
            downloadMemberMasterData();
        }
        prepareBlockForDownloaddList();
        syncAadhaarCollectedBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent theIntent = new Intent(context, SyncHouseholdActivity.class);
                startActivityForResult(theIntent, SYNC_REQUEST);
                activity.leftTransition();
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
                    CustomAlert.alertWithOk(context, "Please select Sub EB");
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
        setData();

      /*  downloadBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadNewblockData(verifierLocation);
            }
        });
*/
    }

    private void prepareBlockForDownloaddList() {
        dowloadedBlockList = new ArrayList<>();
        VerifierLocationItem locItem = null;
        for (VerifierLocationItem item : verifierDetail.getLocationList()) {
            ArrayList<HouseHoldItem> householdList = SeccDatabase.getHouseHoldListSubBlockWise(
                    item.getStateCode().trim(), item.getDistrictCode().trim()
                    , item.getTehsilCode().trim(), item.getVtCode().trim(), item.getWardCode().trim(), item.getBlockCode().trim(), item.getSubBlockcode().trim(), context);
            AppUtility.showLog(AppConstant.LOG_STATUS, TAG, " Household Size : " + householdList.size());
            if (householdList.size() > 0) {
                // locItem=item;
                // AppUtility.showLog(AppConstant.LOG_STATUS,TAG," ");
                dowloadedBlockList.add(item);

                // break;
            }
        }
        // AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Enumertaion Block Code : "+locItem.getBlockCode());
     /*if(locItem!=null) {
         if (locItem.getBlockCode() != null)
             ebTV.setText(locItem.getBlockCode());
     }*/
        if (dowloadedBlockList != null && dowloadedBlockList.size() > 0) {
            adapter = new CustomAdapter(dowloadedBlockList);
            blockList.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    private class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
        private ArrayList<VerifierLocationItem> mDataset;

        public class ViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case

            TextView blockCodeTV, noOfHouseholdTV, noOfMemberTV, syncedMemberTV, totalSyncedMemberTV;
            TextView stateNameTV, distTV, tehsilTV, vtNameTV, wardTV, ebTV, subEbTV;


            // AppUtility.showLog(AppConstant.LOG_STATUS,"Block Activity"," State Name : "+item.getStateName());


            Button proceedBT;

            public ViewHolder(View v) {
                super(v);
                blockCodeTV = (TextView) v.findViewById(R.id.blockCodeTV);
                // blockCodeTV.setPaintFlags(blockCodeTV.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
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
                subEbTV = (TextView) v.findViewById(R.id.subEbTV);


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

        // Provide a suitable constructor (depends on the kind of dataset)
        public CustomAdapter(ArrayList<VerifierLocationItem> myDataset) {
            mDataset = myDataset;
         /*   memberListResponse=SeccMemberResponse.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                    AppConstant.SECC_MEMBER_CONTENT,context));
            Log.d(TAG, "Adapter : " + mDataset.size());*/
        }

        // Create new1 views (invoked by the layout manager)
        @Override
        public CustomAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
            // create a new1 view
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.block_list_item, parent, false);
            // set the view's size, margins, paddings and layout parameters
            CustomAdapter.ViewHolder vh = new CustomAdapter.ViewHolder(v);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(CustomAdapter.ViewHolder holder, int position) {
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
                if (loc.getSubBlockcode() != null) {
                    holder.subEbTV.setText(loc.getSubBlockcode());
                }
            }
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

    private void setData() {
        //  headerTV.setText(context.getResources().getString(R.string.nhpsFieldValidation));
        prepareBlockForNotDownloaded();
    }

    private void prepareBlockForNotDownloaded() {
        locationList = new ArrayList<>();

        ArrayList<VerifierLocationItem> list = verifierDetail.getLocationList();
        if (list != null) {
            for (VerifierLocationItem item : list) {
                long houseHoldCount = SeccDatabase.
                        houseHoldCountSubEbWise(context, item.getStateCode().trim(), item.getDistrictCode().trim(),
                                item.getTehsilCode().trim(), item.getVtCode().trim(), item.getWardCode().trim(), item.getBlockCode().trim(), item.getSubBlockcode().trim());
                if (houseHoldCount > 0) {

                } else {
                    locationList.add(item);
                }
            }
        }
        notDownloadedBlock = new ArrayList<>();
        notDownloadedBlock.add(context.getResources().getString(R.string.selectNewSubEB));
        for (VerifierLocationItem item : locationList) {
            // Log.d(TAG, "Block code : " + item.getStateCode());
            notDownloadedBlock.add(item.getBlockCode() + "_" + item.getSubBlockcode());
            /*for(SeccMemberItem item1 : memberListResponse.getSeccMemberList()) {
                if(!item.getBlockCode().equalsIgnoreCase(item1.getBlockno())) {
                    break;
                }
            }*/
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.custom_drop_down, R.id.textView, notDownloadedBlock);
        blockSP.setAdapter(adapter);
    }


    @Override
    public void onAttach(Activity mActivity) {
        super.onAttach(mActivity);
        activity = (BlockDetailActivity) mActivity;
    }

    private void downloadNewblockData(VerifierLocationItem item) {
        //  int selectedPos=blockSP.getSelectedItemPosition();

        // blockCode=notDownloadedBlock.get(selectedPos);
        // VerifierLocationItem item=locationList.get(selectedPos);
        houseHoldRequest = new HouseHoldRequest();
        //  if(verifierDetail.getLocationList()!=null && verifierDetail.getLocationList().size()>0){
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
        // downloadHouseHoldVolley(houseHoldRequest);
        //checkDataCount(seccRequest.serialize()); commented recent
        //  downloadHouseholdData(houseHoldRequest);

    }

    private void downloadEnumerationBlockData() {
        if (downloadAsyncTask != null && !downloadAsyncTask.isCancelled()) {
            downloadAsyncTask.cancel(true);
            downloadAsyncTask = null;
        }

        downloadAsyncTask = new BlockDownloadTask();
        downloadAsyncTask.execute();
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
                    progressType = "Saving household data to database : " + i + "/" + totalSize + "";
                    AppUtility.showLog(AppConstant.LOG_STATUS, TAG, " Nhps Member id : " + item1.getNhpsMemId());
                    Log.d(TAG, "Household list size : " + SeccDatabase.houseHoldCount(context));
                    // updated by saurabh
                    item1.setDataSource(AppConstant.SECC_SOURCE);
                    AppUtility.showLog(AppConstant.LOG_STATUS, TAG, " Household response byte size : " + item1.serialize().getBytes().length);
                    long houseCount = SeccDatabase.saveHouseHold(item1, context);

                }
                // saving Rsby HouseHold data
                if (rsbyHouseHoldResponse != null) {
                    // Log.d(TAG,"Household list size : "+houseHoldListResponse.getSeccHouseholdList().size());
         /*   if (SeccDatabase.houseHoldCount(context, item.getStateCode().trim(), item.getDistrictCode().trim(), item.getTehsilCode().trim(),
                    item.getVtCode().trim(), item.getWardCode().trim(), item.getBlockCode().trim()) > 0) {
                AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Member response1 : ");
            } else {*/
                    if (rsbyHouseHoldResponse.getSeccHouseholdList() != null && rsbyHouseHoldResponse.getSeccHouseholdList().size() > 0) {

                        for (HouseHoldItem item1 : rsbyHouseHoldResponse.getSeccHouseholdList()) {
                            AppUtility.showLog(AppConstant.LOG_STATUS, TAG, " Nhps Member id : " + item1.getNhpsMemId());
                            Log.d(TAG, "Household list size : " + SeccDatabase.houseHoldCount(context));
                            // updated by saurabh
                            item1.setDataSource(AppConstant.RSBY_SOURCE);
                            AppUtility.showLog(AppConstant.LOG_STATUS, TAG, " Household response byte size : " + item1.serialize().getBytes().length);
                            SeccDatabase.saveHouseHold(item1, context);
                        }
                    }
            /*}*/
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
                    progressType = "Saving Members data to database : " + i + "/" + seccmemberResponse.getSeccMemberList().size();
                    AppUtility.showLog(AppConstant.LOG_STATUS, TAG, " Secc response byte size : " + item1.serialize().getBytes().length);
                    // updated by saurabh
                    item1.setDataSource(AppConstant.SECC_SOURCE);
                    SeccDatabase.saveSeccMember(item1, context);
                }

                if (rsbyMemberResponse != null) {
                    if (rsbyMemberResponse.getSeccMemberList() != null && rsbyMemberResponse.getSeccMemberList().size() > 0) {
                        for (SeccMemberItem item1 : rsbyMemberResponse.getSeccMemberList()) {
                            AppUtility.showLog(AppConstant.LOG_STATUS, TAG, " Secc response byte size : " + item1.serialize().getBytes().length);
                            //updated by saurabh
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
        ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.DOWNLOADINGSOURCE, AppConstant.SubBlockWiseDownloading, context);
        fixHouseholdStatus(item);
        //    showDownloadedData();
        VerifierLocationItem loc = verifierDetail.getLocationList().get(0);
        loc.setBlockCode(blockCode);
        ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SELECTED_BLOCK, loc.serialize(), context);

        // setLocationAndUpdateScreen();

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

    private void proceedDailog(final VerifierLocationItem item) {
        //checkAppConfig();
        proceedDailog = new AlertDialog.Builder(context).create();
        TextView stateNameTV, distTV, tehsilTV, vtNameTV, wardTV, ebTV, subEbTV;
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

        noOfHouseholdTV.setText(SeccDatabase.seccHouseHoldSubEbCount(context, item.getStateCode(),
                item.getDistrictCode(), item.getTehsilCode(), item.getVtCode(), item.getWardCode(), item.getBlockCode(), item.getSubBlockcode()) + "");
        if (dataSoursce.equalsIgnoreCase("B")) {
            totalRsbyHouseholdTV.setText(SeccDatabase.rsbyHouseHoldSubEbCount(context, item.getStateCode(),
                    item.getDistrictCode(), item.getTehsilCode(), item.getVtCode(), item.getWardCode(), item.getBlockCode(), item.getSubBlockcode()) + "");
        } else {
            totalRsbyHouseholdTV.setVisibility(View.GONE);
            rsbyLayout.setVisibility(View.GONE);
        }
        noOfMemberTV.setText(SeccDatabase.seccMemberSubEbCount(context, item.getStateCode(),
                item.getDistrictCode(), item.getTehsilCode(), item.getVtCode(), item.getWardCode(), item.getBlockCode(), item.getSubBlockcode()) + "");
        syncedMemberTV.setText(SeccDatabase.countSyncedHouseholdEbWise(context, item.getStateCode(),
                item.getDistrictCode(), item.getTehsilCode(), item.getVtCode(), item.getWardCode(), item.getBlockCode(), item.getSubBlockcode(), AppConstant.SYNCED_STATUS + "") + "");
        totalSyncedMemberTV.setText(SeccDatabase.countSyncedMemberSubEb(context, item.getStateCode(),
                item.getDistrictCode(), item.getTehsilCode(), item.getVtCode(), item.getWardCode(), item.getBlockCode(), item.getSubBlockcode(), AppConstant.SYNCED_STATUS + "") + "");


        syncBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.homeNavigation, AppConstant.downloadActivityNavigation, context);
                ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SELECTED_BLOCK, item.serialize(), context);
                Intent theIntent = new Intent(context, SyncHouseholdActivity.class);
                startActivityForResult(theIntent, SYNC_REQUEST);
                AppUtility.navgateFromEb = true;
                activity.leftTransition();
                proceedDailog.dismiss();
            }
        });
        validationBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Log.d(TAG,"Delete Status"+status);
                ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SELECTED_BLOCK, item.serialize(), context);
                ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.homeNavigation, AppConstant.blockDetailActivityNavigation, context);
                ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.dataDownloaded, "V", context);
               /* ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.homeNavigation, AppConstant.downloadActivityNavigation, context);*/
                Intent theIntent = new Intent(context, SearchActivityWithHouseHold.class);
                startActivity(theIntent);
                activity.leftTransition();
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
        wrongAttempetCountText = (TextView) alertView.findViewById(R.id.wrongAttempetCountText);
        wrongAttempetCountValue = (TextView) alertView.findViewById(R.id.wrongAttempetCountValue);


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
                            deleteSubEB(item);
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
        //setLocationAndUpdateScreen();
        activity.recreate();
    }

    private void deleteSubEB(VerifierLocationItem item) {
        String query = "delete from "
                + AppConstant.HOUSE_HOLD_SECC
                + " where statecode='"
                + item.getStateCode().trim()
                + "' AND districtcode='" +
                item.getDistrictCode().trim()
                + "' AND tehsilcode='" + item.getTehsilCode().trim()
                + "' AND towncode='" + item.getVtCode().trim() + "' AND wardid='" + item.getWardCode().trim()
                + "' AND ahlblockno ='" + item.getBlockCode().trim()
                + "' AND ahlsubblockno  ='" + item.getSubBlockcode().trim() + "'";
        SeccDatabase.deleteTable(query, context);
        String seccDeleteQuery = "delete from "
                + AppConstant.TRANSECT_POPULATE_SECC
                + " where statecode='"
                + item.getStateCode()
                + "' AND districtcode='" +
                item.getDistrictCode()
                + "' AND tehsilcode='" + item.getTehsilCode()
                + "' AND towncode='" + item.getVtCode() + "' AND wardid='" + item.getWardCode()
                + "' AND blockno ='" + item.getBlockCode()
                + "' AND ahlsubblockno ='" + item.getSubBlockcode() + "'";
        SeccDatabase.deleteTable(seccDeleteQuery, context);
        //setLocationAndUpdateScreen();
        activity.recreate();
    }

    private void cleanDevice() {
        if (AppUtility.deleteFile(new File(DatabaseHelpers.DELETE_FOLDER_PATH))) {
            activity.logoutVerifier();
        }


    }

    /*   private void showDownloadedData() {
           if (houseHoldList != null && houseHoldList.size() > 0) {
               proceedLayout.setVisibility(View.VISIBLE);
               downloadBloackLayout.setVisibility(View.GONE);
               if (verifierLocation != null) {
                   pstateNameTV.setText(verifierLocation.getStateName());
                   pdistNameTV.setText(verifierLocation.getDistrictName());
                   ptehsilNameTV.setText(verifierLocation.getTehsilName());
                   pvillTownNameTV.setText(verifierLocation.getVtName());
                   pwardCodeTV.setText(verifierLocation.getWardCode());
                   pebTV.setText(verifierLocation.getBlockCode());
                   psubEbTV.setText(verifierLocation.getSubBlockcode());
                   pProceedBT.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {
                           proceedDailog(verifierLocation);
                       }
                   });
               }


           } else {
               downloadBloackLayout.setVisibility(View.VISIBLE);
               proceedLayout.setVisibility(View.GONE);
           }
       }*/
    private void updateLocation(VerifierLocationItem item) {
        stateNameTV.setText("-");
        wardCodeTV.setText("-");
        villTownNameTV.setText("-");
        distNameTV.setText("-");
        tehsilNameTV.setText("-");
        ebTV.setText("-");
        subEbCodeTV.setText("-");
        subEbNameTV.setText("-");
        if (item != null) {
            stateNameTV.setText(item.getStateName());
            distNameTV.setText(item.getDistrictName());
            tehsilNameTV.setText(item.getTehsilName());
            villTownNameTV.setText(item.getVtName());
            wardCodeTV.setText(item.getWardCode());
            ebTV.setText(item.getBlockCode());
            subEbCodeTV.setText(item.getSubBlockcode());
            subEbNameTV.setText(item.getSubBlockcode());
        }
    }

    public void alertWithOk(Context mContext, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(mContext.getResources().getString(com.customComponent.R.string.Alert));
        builder.setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(mContext.getResources().getString(com.customComponent.R.string.OK), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                        Fragment fragment = new SubEBWiseDownloadFragment();
                        FragmentManager fragmentManager = activity.getSupportFragmentManager();
                        FragmentTransaction fragmentTransection = fragmentManager.beginTransaction();
                        fragmentTransection.add(R.id.fragContainer, fragment);
                        fragmentTransection.commitAllowingStateLoss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
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

    private class BlockDownloadTask extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... params) {

            try {
                HashMap<String, String> response = CustomHttp.httpHeaderPost(AppConstant.HEALTH_SCHEME_API, healthSchemeRequest.serialize(), AppConstant.AUTHORIZATION, AppConstant.AUTHORIZATIONVALUE);
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
                //if()
                HashMap<String, String> response = CustomHttp.httpPost1(AppConstant.RSBY_HOUSEHOLD_DOWNLOAD,
                        houseHoldRequest.serialize(), null);
                String RsbyhouseHoldResponse = AppUtility.fixEncoding(response.get(AppConstant.RESPONSE_BODY));

                AppUtility.showLog(AppConstant.LOG_STATUS, TAG, " RSBY Household response : " + houseHoldResponse);
                if (RsbyhouseHoldResponse != null) {
                    rsbyHouseHoldResponse = SeccHouseholdResponse.create(RsbyhouseHoldResponse);
                    if (rsbyHouseHoldResponse != null) {
                        if (rsbyHouseHoldResponse.isStatus()) {
                            if (rsbyHouseHoldResponse.getSeccHouseholdList() != null) {
                                if (rsbyHouseHoldResponse.getSeccHouseholdList().size() > 0) {
                                    try {
                                        Thread.sleep(1000);
                                        // publishProgress(HOUSEHOLD_DOWNLOADED);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    //  return DATA_NOT_ALLOTED;
                                }
                            }
                        } else {
                            //    return SERVER_ERROR;
                        }
                    }
                } else {
                    // return INTERNET_LOST;
                }
            } catch (Exception e) {
                // return INTERNET_LOST;
            }

            // Rsby member data
            try {

                AppUtility.showLog(AppConstant.LOG_STATUS, TAG, " RSBY Household Request :" +
                        " " + seccRequest.serialize() + " \n URL :" + AppConstant.RSBY_MEMBER_DOWNLOAD);
                HashMap<String, String> response = CustomHttp.httpPost1(AppConstant.RSBY_MEMBER_DOWNLOAD, seccRequest.serialize(), null);
                String RsbyMemberResp = AppUtility.fixEncoding(response.get(AppConstant.RESPONSE_BODY));
                AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "RSBY Member response : " + RsbyMemberResp);
                if (RsbyMemberResp != null) {
                    rsbyMemberResponse = SeccMemberResponse.create(RsbyMemberResp);
                    if (rsbyMemberResponse != null) {
                        if (rsbyMemberResponse.isStatus()) {
                            if (rsbyMemberResponse.getSeccMemberList() != null && rsbyMemberResponse.getSeccMemberList().size() > 0) {
                                try {
                                    Thread.sleep(1000);
                                    //  publishProgress(HOUSEHOLD_MEMBER_DOWNLOADED);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                // saveHouseholdData(selectedLocation);
                                //  return DOWNLOAD_COMPLETED;
                            } else {
                                //  return DATA_NOT_ALLOTED;
                            }
                        } else {
                            //  return SERVER_ERROR;
                        }
                    } else {
                        AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Download Status : Inside RSBYmember response null");
                        //  return INTERNET_LOST;
                    }
                }
            } catch (Exception e) {
                AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Download Status : Inside RSBYmember response exception" + e.toString());
                // return INTERNET_LOST;
            }

            if (seccmemberResponse.getSeccMemberList() != null && seccmemberResponse.getSeccMemberList().size() > 0) {
                saveHouseholdData(selectedLocation);

                return DOWNLOAD_COMPLETED;
            }
//jkklklkl

            //  for (; count <= params[0]; count++) {

            // }
            return "Task Completed.";
        }

        @Override
        protected void onPostExecute(String result) {
            if (progressBar != null && progressBar.isShowing()) {
                progressBar.dismiss();

            }
            if (result.equalsIgnoreCase(DOWNLOAD_COMPLETED)) {
                alertWithOk(context, DOWNLOAD_COMPLETED);
                myCountDownTimer.cancel();
                //setLocationAndUpdateScreen();
            } else if (result.equalsIgnoreCase(INTERNET_LOST)) {
                // CustomAlert.alertWithOk(context,"");
                myCountDownTimer.cancel();
                internetLostMessage(TRY_AGAIN_MSG);
            } else if (result.equalsIgnoreCase(DATA_NOT_ALLOTED)) {
                CustomAlert.alertWithOk(context, result);
                myCountDownTimer.cancel();
            }

           /* txt.setText(result);
            btn.setText("Restart");*/
        }

        @Override
        protected void onPreExecute() {
            //txt.setText("Task Starting...");
            myCountDownTimer = new MyCountDownTimer(AppConstant.TIMMERTIME * 1000 + 1000, 1000);

            myCountDownTimer.start();

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


    /*private void checkDataCount(final String request) {

        TaskListener taskListener = new TaskListener() {
            @Override
            public void execute() {
                try {
                    HashMap<String, String> response = CustomHttp.httpPost(AppConstant.GET_VILLAGE_COUNT, request, null);
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

                        String seccMemberCount = dataCountModel.getDownloadCount().getMembersCount();
                        String seccHouseHoldCount = dataCountModel.getDownloadCount().getHouseHoldCount();
                        String rsbyMemberCount = dataCountModel.getDownloadCount().getRsbyMembersCount();
                        String rsbyHouseHoldCount = dataCountModel.getDownloadCount().getRsbyHouseHoldCount();

                        countPopUp(seccHouseHoldCount, seccMemberCount, rsbyMemberCount, rsbyHouseHoldCount);
                    } else {
                        // AppUtility.alertWithOk(context, context.getResources().getString(R.string.server_error));
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

    }*/


    private void countPopUp(final String seccHousehold, final String seccMember, final String rsbyHousehold, final String rsbyMember) {

        final AlertDialog askForPinDailog = new AlertDialog.Builder(context).create();
        LayoutInflater factory = LayoutInflater.from(context);
        View alertView = factory.inflate(R.layout.show_count, null);
        askForPinDailog.setView(alertView);
        askForPinDailog.setCancelable(false);
        askForPinDailog.show();
        Button proceedBT = (Button) alertView.findViewById(R.id.proceedBT);
        Button cancelBT = (Button) alertView.findViewById(R.id.cancelBT);
        TextView seccHouseholdCountN = (TextView) alertView.findViewById(R.id.seccHouseholdCount);
        TextView seccMemberCountN = (TextView) alertView.findViewById(R.id.seccMemberCount);
        TextView rsbyHouseholdCountN = (TextView) alertView.findViewById(R.id.rsbyHouseholdCount);
        TextView rsbyMemberCountN = (TextView) alertView.findViewById(R.id.rsbyMemberCount);

        seccHouseholdCountN.setText(seccHousehold);
        seccMemberCountN.setText(seccMember);
        rsbyHouseholdCountN.setText(rsbyHousehold);
        rsbyMemberCountN.setText(rsbyMember);

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


    /*
                DownloadedDataCountModel dataCountModel = new DownloadedDataCountModel();
                dataCountModel.setId("1");
                dataCountModel.setSeccMemberCount(seccMember);
                dataCountModel.setSeccHouseholdCount(seccHousehold);
                dataCountModel.setRsbyMemberCount(rsbyMember);
                dataCountModel.setRsbyHouseholdCount(rsbyHousehold);
                CommonDatabase.saveDataCount(dataCountModel, context);
    */

                downloadEnumerationBlockData();

            }
        });
        cancelBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askForPinDailog.dismiss();
            }
        });
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
                    showDialog(getActivity(), context.getResources().getString(R.string.Alert), serverIssues, context.getResources().getString(R.string.tryAgain));
                }

            }

            @Override
            public void onError(VolleyError error) {

                showDialog(getActivity(), context.getResources().getString(R.string.Alert), TRY_AGAIN_MSG, context.getResources().getString(R.string.tryAgain));
            }
        };

        CustomVolleyHeaderGet volleyGet = new CustomVolleyHeaderGet(taskListener, masterMsg,
                AppConstant.MEMBER_STATUS_API, AppConstant.AUTHORIZATION, AppConstant.AUTHORIZATIONVALUE, context);
        volleyGet.execute();
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
                getActivity().finish();
            }
        });
        return downloadDialog.show();
    }
}

