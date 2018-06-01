package com.nhpm.fragments;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.customComponent.CustomAlert;
import com.customComponent.CustomAsyncTask;
import com.customComponent.Networking.CustomVolley;
import com.customComponent.Networking.VolleyTaskListener;
import com.customComponent.TaskListener;
import com.customComponent.utility.CustomHttp;
import com.customComponent.utility.DateTimeUtil;
import com.customComponent.utility.ProjectPrefrence;
import com.nhpm.AadhaarUtils.Global;
import com.nhpm.LocalDataBase.dto.SeccDatabase;
import com.nhpm.Models.request.AadhaarAuthRequestItem;
import com.nhpm.Models.request.VerifyValidator;
import com.nhpm.Models.response.seccMembers.HouseHoldItem;
import com.nhpm.Models.response.seccMembers.SeccMemberItem;
import com.nhpm.Models.response.seccMembers.SelectedMemberItem;
import com.nhpm.Models.response.verifier.AadhaarDemoAuthResponse;
import com.nhpm.Models.response.verifier.AadhaarOtpResponse;
import com.nhpm.Models.response.verifier.AadhaarResponseItem;
import com.nhpm.Models.response.verifier.FamilyStatusItem;
import com.nhpm.Models.response.verifier.ValidatorVerificationResponse;
import com.nhpm.Models.response.verifier.VerifierLocationItem;
import com.nhpm.Models.response.verifier.VerifierLoginResponse;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;
import com.nhpm.Utility.AppUtility;
import com.nhpm.Utility.ApplicationGlobal;
import com.nhpm.Utility.Verhoeff;
import com.nhpm.activity.ErrorMemberActivity;
import com.nhpm.activity.SyncHouseholdActivity;
import com.nhpm.backgroundService.SyncService;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import fr.arnaudguyon.xmltojsonlib.XmlToJson;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * { interface
 * to handle interaction events.
 * Use the {@link SyncStatusFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SyncStatusFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ValidatorVerificationResponse verifierResponse;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ArrayList<HouseHoldItem> pendingHouseholdList;
    private String status;
    private RecyclerView syncList;
    private String TAG = "Sync Status Fragment";
    private Context context;
    private OtherMemberAdapter adapter;
    private ArrayList<FamilyStatusItem> familyStatusList;
    private Button syncAllBT, validateAllBT, ashaOtpBT;
    private SyncHouseholdActivity activity;
    private int readyToSyncCount;
    private int unsyncCount;
    private int syncCount;
    private String HOUSEHOLD_SORT_STATUS;
    private Spinner sortedSP;
    private ArrayList<String> spinnerList;
    public static int ErrorMemberRequest = 2;
    private int pendingCount = 0;
    private int validatedCount;
    private int invalidCount;
    private VerifierLocationItem locationItem;
    private VerifierLoginResponse storedLoginResponse;
    private String[] aadhaarNumber = new String[1];
    private CustomAsyncTask asyncTask;
    private AadhaarResponseItem aadhaarRespItem;
    private boolean isVeroff;
    //    private Activity activity;
    private AlertDialog popUpDialogForOtp;
    private String pidXml;
    private String aadharCertificate;
    private String aadharServiceResponse;
    private AadhaarDemoAuthResponse demoAuthResp;

    public SyncStatusFragment() {
        // Required empty public constructor
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<HouseHoldItem> getPendingHouseholdList() {
        return pendingHouseholdList;
    }

    public void setPendingHouseholdList(ArrayList<HouseHoldItem> pendingHouseholdList) {
        this.pendingHouseholdList = pendingHouseholdList;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SyncStatusFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SyncStatusFragment newInstance(String param1, String param2) {
        SyncStatusFragment fragment = new SyncStatusFragment();
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
        View view = inflater.inflate(R.layout.fragment_sync_status, container, false);
        setupScreen(view);
        return view;
    }

    private void sortList(int index) {
        switch (index) {
            case 0:
                //  Log.d("Pending household", " Sort by House No ");
               /* Collections.sort(pendingHouseholdList, new Comparator<HouseHoldItem>() {
                    @Override
                    public int compare(HouseHoldItem o1, HouseHoldItem o2) {
                        return Integer.parseInt(o1.getAhlslnohhd()) - Integer.parseInt(o2.getAhlslnohhd());
                      *//*  if(status)
                        return 1;
                        else
                            return 0;*//*

                        //return Integer.compare(o1.getAge(), o2.getId());
                    }
                });*/
                break;
            case 1:
                Collections.sort(pendingHouseholdList, new Comparator<HouseHoldItem>() {
                    @Override
                    public int compare(HouseHoldItem o1, HouseHoldItem o2) {
                        return o1.getName().compareTo(o2.getName());
                    }
                });

                break;
        }
        adapter = new OtherMemberAdapter(context, pendingHouseholdList);
        syncList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    // TODO: Rename method, update argument and hook method into UI event

    private void setupScreen(View view) {
        context = getActivity();
        locationItem = VerifierLocationItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SELECTED_BLOCK, context));
        storedLoginResponse = VerifierLoginResponse.create(
                ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.VERIFIER_CONTENT, context));
        if (storedLoginResponse != null) {
            aadhaarNumber[0] = storedLoginResponse.getAadhaarNumber();
        }
        familyStatusList = SeccDatabase.getFamilyStatusList(context);
        syncList = (RecyclerView) view.findViewById(R.id.syncMemberList);
        syncAllBT = (Button) view.findViewById(R.id.syncAllBT);
        ashaOtpBT = (Button) view.findViewById(R.id.ashaOtpBT);
        ashaOtpBT.setVisibility(View.GONE);
        syncAllBT.setVisibility(View.GONE);
        validateAllBT = (Button) view.findViewById(R.id.validateAllBT);
        validateAllBT.setVisibility(View.GONE);
        //  printPreview = (Button) view.findViewById(R.id.printPreview);
        sortedSP = (Spinner) view.findViewById(R.id.sortSP);
        syncList.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        syncList.setLayoutManager(layoutManager);
        syncList.setItemAnimator(new DefaultItemAnimator());
      /*  HOUSEHOLD_SORT_STATUS=ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,AppConstant.HOUSEHOLD_SORT_STATUS,context);
        if(HOUSEHOLD_SORT_STATUS!=null){
            sortedSP.setSelection(Integer.parseInt(HOUSEHOLD_SORT_STATUS));
        }else{

        }*/
        sortedSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,AppConstant.HOUSEHOLD_SORT_STATUS,i+"",context);
                if (pendingHouseholdList != null) {
                    sortList(i);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        prepareFamilyStatusSpinner();
        if (status != null && status.equalsIgnoreCase(SyncHouseHoldFragment.READY_TO_SYNC)) {
            // activity.updateSyncStatus(123);
            readyToSyncCount = pendingHouseholdList.size();
            if (pendingHouseholdList.size() > 0) {
                // syncAllBT.setVisibility(View.VISIBLE);

                ashaOtpBT.setVisibility(View.GONE);
                syncAllBT.setText(context.getResources().getString(R.string.syncAll));
                syncAllBT.setTextColor(context.getResources().getColor(R.color.white));
                syncAllBT.setBackground(context.getResources().getDrawable(R.drawable.button_background_orange_ehit));
                syncAllBT.setVisibility(View.VISIBLE);

                syncAllBT.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (activity.isNetworkAvailable()) {
                          /*  if (pendingHouseholdList != null && pendingHouseholdList.size() > 0) {
                                startSyncServce(context, pendingHouseholdList);
                            }*/
                            validateValidatorOtp();
                        } else {
                            CustomAlert.alertWithOk(context, getResources().getString(R.string.internet_connection_msg));
                        }
                    }
                });
                if (pendingHouseholdList != null && pendingHouseholdList.size() > 0) {
                    // startSyncServce(context, pendingHouseholdList);
                }
            } else {
                syncAllBT.setVisibility(View.GONE);
            }
        } else if (status != null && status.equalsIgnoreCase(SyncHouseHoldFragment.SYNC_ERROR)) {
            if (pendingHouseholdList.size() > 0) {
                syncAllBT.setText(context.getResources().getString(R.string.reSync));
                syncAllBT.setVisibility(View.VISIBLE);
            } else {
                syncAllBT.setVisibility(View.GONE);
            }
        } else {
            // syncAllBT.setVisibility(View.VISIBLE);

        }

        syncAllBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (activity.isNetworkAvailable()) {
                    //  syncHouseholdToServer();
                    //    ArrayList<HouseHoldItem> list =new ArrayList<HouseHoldItem>();
                  /*  for(HouseHoldItem item : pendingHouseholdList){
                        if(item.getError_type()!=null && item.getError_type().equalsIgnoreCase(AppConstant.AADHAAR_ALREADY_ALLOCATED)) {
                            // list.add(item);
                        }else  if(item.getError_type()!=null && item.getError_type().equalsIgnoreCase(AppConstant.VERIFIER_AADHAAR_ALLOCATED)){

                        }else if(item.getError_type()!=null && item.getError_type().equalsIgnoreCase(AppConstant.AADHAAR_VALIDATION_ERROR)){

                        }else{
                            list.add(item);
                        }
                    }*/
                    validateValidatorOtp();

                  /*  if (pendingHouseholdList != null && pendingHouseholdList.size() > 0) {
                        startSyncServce(context, pendingHouseholdList);
                    }*/
                } else {
                    CustomAlert.alertWithOk(context, getResources().getString(R.string.internet_connection_msg));
                }
            }
        });
        ashaOtpBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertForConsent(context);

            }
        });


        validateAllBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (activity.isNetworkAvailable()) {
                    //  syncHouseholdToServer();
                    if (pendingHouseholdList != null && pendingHouseholdList.size() > 0) {
                        validateAadhaar();
                    } else {
                        CustomAlert.alertWithOk(context, context.getResources().getString(R.string.noPendingAadharTOValidate));
                    }
                } else {
                    CustomAlert.alertWithOk(context, getResources().getString(R.string.internet_connection_msg));
                }
            }
        });

        if (status != null && status.equalsIgnoreCase(2 + "")) {
            //csd
        }

      /*  adapter=new OtherMemberAdapter(context,pendingHouseholdList);
        syncList.setAdapter(adapter);
        adapter.notifyDataSetChanged();*/
    }


    private void startSyncServce(Context context, ArrayList<HouseHoldItem> list) {
        activity.showProgressDialog();
        Intent intent = new Intent(context, SyncService.class);
        intent.putExtra(SyncService.SYNC_ARG, list);
        getActivity().startService(intent);
        getActivity().registerReceiver(activity.broadcastReceiver, new IntentFilter(SyncService.BROADCAST_ACTION));

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (SyncHouseholdActivity) context;

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private class OtherMemberAdapter extends RecyclerView.Adapter<OtherMemberAdapter.MyViewHolder> {
        private ArrayList<HouseHoldItem> dataSet;
        // Context context;
        private TextView text;

        //private Activity context;
        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView hofNameTV, noOfFamilyTV, fatherNameTV, houseHoldNo, addressTV,
                    genderTV, verifiedMemTV, localHofNameTV, householdStatusTV, syncDateTV, dataSourceTV, urnIdTV, houseHoldNoTV;
            TextView errorTV, nhpsIdTV;
            LinearLayout syncTimeLayout, errorLayout, householdIdLayout, urnIdLayout;

            public MyViewHolder(View itemView) {
                super(itemView);
                houseHoldNoTV = (TextView) itemView.findViewById(R.id.houseHoldNoTV);
                verifiedMemTV = (TextView) itemView.findViewById(R.id.verifiedMemTV);
                noOfFamilyTV = (TextView) itemView.findViewById(R.id.totalMemberTV);
                hofNameTV = (TextView) itemView.findViewById(R.id.hofNameTV);
                localHofNameTV = (TextView) itemView.findViewById(R.id.localHofNameTV);
                houseHoldNo = (TextView) itemView.findViewById(R.id.houseNoTV);
                fatherNameTV = (TextView) itemView.findViewById(R.id.hhdFatherNameTV);
                addressTV = (TextView) itemView.findViewById(R.id.hhdAddressTV);
                genderTV = (TextView) itemView.findViewById(R.id.hhdGenderTV);
                errorTV = (TextView) itemView.findViewById(R.id.errorTV);
                householdStatusTV = (TextView) itemView.findViewById(R.id.householdStatusTV);
                syncDateTV = (TextView) itemView.findViewById(R.id.syncDateTV);
                nhpsIdTV = (TextView) itemView.findViewById(R.id.nhpsIdTV);
                syncTimeLayout = (LinearLayout) itemView.findViewById(R.id.syncTimeLayout);
                errorLayout = (LinearLayout) itemView.findViewById(R.id.errorLayout);
                dataSourceTV = (TextView) itemView.findViewById(R.id.dataSourceTV);
                householdIdLayout = (LinearLayout) itemView.findViewById(R.id.householdIdLayout);
                urnIdLayout = (LinearLayout) itemView.findViewById(R.id.urnIdLayout);
                urnIdTV = (TextView) itemView.findViewById(R.id.urnIdTV);
                syncTimeLayout.setVisibility(View.GONE);
                errorLayout.setVisibility(View.GONE);
                if (status != null && status.equalsIgnoreCase(SyncHouseHoldFragment.SYNCED_HOUSEHOLD)) {
                    syncTimeLayout.setVisibility(View.VISIBLE);
                }
                if (status != null && status.equalsIgnoreCase(SyncHouseHoldFragment.SYNC_ERROR)) {
                    errorLayout.setVisibility(View.VISIBLE);
                }
                if (status != null && status.equalsIgnoreCase(SyncHouseHoldFragment.YET_TO_VALIDATE)) {
                    // syncAllBT.setVisibility(View.VISIBLE);
                    if (pendingHouseholdList.size() > 0) {
                        validateAllBT.setVisibility(View.VISIBLE);
                    }
                }
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent theIntent = new Intent(context, ErrorMemberActivity.class);
                        SelectedMemberItem selectedMemberItem = new SelectedMemberItem();
                        selectedMemberItem.setHouseHoldItem(dataSet.get(getPosition()));
                        //  Log.d(TAG," Selected Household : "+selectedMemberItem.getHouseHoldItem().getHhdNo());
                        ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                                AppConstant.SELECTED_ITEM_FOR_VERIFICATION, selectedMemberItem.serialize(), context);
                        startActivity(theIntent);
                        // startActivityForResult(theIntent,ErrorMemberRequest);
                    }
                });
            }
        }

        public void addAll(List<HouseHoldItem> list) {

            dataSet.addAll(list);
            notifyDataSetChanged();
        }

        public OtherMemberAdapter(Context context, ArrayList<HouseHoldItem> data) {

            this.dataSet = data;
            ;
            this.text = text;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_pending_family_list_item, parent, false);

            //view.setOnClickListener(MainActivity.myOnClickListener);

            MyViewHolder myViewHolder = new MyViewHolder(view);
            return myViewHolder;
        }


        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {


            HouseHoldItem item = dataSet.get(listPosition);

          /*  holder.noOfFamilyTV.setText("2");
            holder.verifiedMemTV.setText("0");

            if(item.getAhlslnohhd()!=null)
                holder.houseHoldNo.setText(item.getAhlslnohhd());
            if(item.getName()!=null)
                holder.hofNameTV.setText(item.getName());
            if(item.getFathername()!=null){
                holder.fatherNameTV.setText(item.getFathername());
            }
            if(item.getNameSl()!=null)
                holder.localHofNameTV.setText(item.getNameSl());
            if(item.getGenderid().equalsIgnoreCase("1")) {
                holder.genderTV.setText("Male");
            }else{
                holder.genderTV.setText("Female");
            }

            holder.householdStatusTV.setText("-");*/
            holder.householdIdLayout.setVisibility(View.GONE);
            holder.urnIdLayout.setVisibility(View.GONE);
            holder.houseHoldNo.setText("-");
            holder.hofNameTV.setText("-");
            holder.localHofNameTV.setText("-");
            holder.genderTV.setText("-");
            holder.addressTV.setText("-");
            holder.fatherNameTV.setVisibility(View.GONE);
            if (item.getDataSource() != null && item.getDataSource().equalsIgnoreCase(AppConstant.RSBY_SOURCE)) {
                // holder.dataSourceTV.setText("RSBY");
               /* holder.hofNameTV.setText("");
                AppUtility.showLog(AppConstant.LOG_STATUS,"gahsgjga","Hof Name : "+item.getName());
                holder.hofNameTV.setText(item.getName());
                holder.dataSourceTV.setText(AppConstant.RSBY_SOURCE_NAME);*/
                showRsbyHouseHold(item, holder);
            } else {
                /*holder.hofNameTV.setText("");
                AppUtility.showLog(AppConstant.LOG_STATUS,"gahsgjga","Hof Name : "+item.getRsbyName());
                holder.hofNameTV.setText(item.getRsbyName());
                holder.dataSourceTV.setText(AppConstant.SECC_SOURCE_NAME);*/
                showSeccHouseHold(item, holder);
            }
            if (item.getHhStatus() != null && !item.getHhStatus().equalsIgnoreCase("")) {
                for (FamilyStatusItem item1 : familyStatusList) {
                    if (item1.getStatusCode().equalsIgnoreCase(item.getHhStatus())) {
                        holder.householdStatusTV.setText(item1.getStatusDesc());
                        break;
                    }
                }
            }
  /*          String address="";

            if(item.getAddressline1()!=null && !item.getAddressline1().trim().
                    equalsIgnoreCase("-") && !item.getAddressline1().equalsIgnoreCase("")){
                address=address+item.getAddressline1()+", ";
                // AppUtility.showLog(AppConstant.LOG_STATUS,TAG," Address3 : "+seccItem.getAddressline1());

            }
            if(item.getAddressline2()!=null && !item.getAddressline2().trim()
                    .equalsIgnoreCase("-") && !item.getAddressline2().equalsIgnoreCase("") ){
                //AppUtility.showLog(AppConstant.LOG_STATUS,TAG," Address : "+seccItem.getAddressline2());
                address=address+item.getAddressline2()+", ";
            }
            if(item.getAddressline3()!=null && !item.getAddressline3().trim().
                    equalsIgnoreCase("-") && !item.getAddressline3().equalsIgnoreCase("") ){
                //AppUtility.showLog(AppConstant.LOG_STATUS,TAG," Address1 : "+seccItem.getAddressline3());

                address=address+item.getAddressline3()+", ";
            }
            if(item.getAddressline4()!=null && !item.getAddressline4().trim().
                    equalsIgnoreCase("-") && !item.getAddressline4().equalsIgnoreCase("") ){
                // AppUtility.showLog(AppConstant.LOG_STATUS,TAG," Address2 : "+seccItem.getAddressline4());

                address=address+item.getAddressline4();
            }
            holder.addressTV.setText(address);*/
            if (item.getSyncDt() != null) {
                String dateTime = DateTimeUtil.convertTimeMillisIntoStringDateNew(item.getSyncDt(), AppConstant.SYNC_DATE_TIME);
                if (dateTime != null) {
                    holder.syncDateTV.setText(dateTime);
                }
            }
            holder.nhpsIdTV.setText("-");
            if (item.getSyncedStatus() != null && item.getSyncedStatus().trim().equalsIgnoreCase(AppConstant.SYNCED_STATUS)) {
                holder.nhpsIdTV.setText(item.getNhpsId());
            }
            if (item.getError_code() != null && !item.getError_code().equalsIgnoreCase("")) {
                holder.errorTV.setText(item.getError_msg());
            }
        }

        @Override
        public int getItemCount() {
            return dataSet.size();
        }

        public void clearDataSource() {
            dataSet.clear();
            notifyDataSetChanged();
        }
    }

    private void prepareFamilyStatusSpinner() {

        spinnerList = new ArrayList<>();
        spinnerList.add("Sort by HouseholdID#");
        spinnerList.add("Sort by Name");
        ArrayAdapter<String> maritalAdapter = new ArrayAdapter<>(context, R.layout.custom_drop_down, R.id.textView, spinnerList);
        sortedSP.setAdapter(maritalAdapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ErrorMemberRequest) {
            activity.finish();
            Intent intent = new Intent(context, SyncHouseholdActivity.class);
            // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);

        }
    }

    private void validateAadhaar() {

        TaskListener taskListener = new TaskListener() {
            @Override
            public void execute() {

                ArrayList<SeccMemberItem> pendingAaadhaarList;
                if (locationItem != null) {
                    pendingAaadhaarList = SeccDatabase.getSeccMemberList(locationItem.getStateCode(),
                            locationItem.getDistrictCode(), locationItem.getTehsilCode(), locationItem.getVtCode(), locationItem.getWardCode(), locationItem.getBlockCode(), context);
                    pendingCount = 0;
                    validatedCount = 0;
                    invalidCount = 0;
                    for (SeccMemberItem item : pendingAaadhaarList) {
                        if (item != null && item.getDataSource() != null && item.getDataSource().trim().equalsIgnoreCase(AppConstant.RSBY_SOURCE)) {
                            validateRsbyAdhaar(item);
                        } else {
                            validateSeccAadhaar(item);
                        }

                    }
                }
            }

            @Override
            public void updateUI() {

                CustomAlert.alertWithOk(context, context.getResources().getString(R.string.validateSucess) + validatedCount + "\n" + context.getResources().getString(R.string.validateFail) + invalidCount);
                activity.openSyncHouseholdFragment();
            }
        };

        if (asyncTask != null && !asyncTask.isCancelled()) {
            asyncTask.cancel(true);
            asyncTask = null;
        }
        asyncTask = new CustomAsyncTask(taskListener, context.getResources().getString(R.string.please_wait), context);
        asyncTask.execute();
    }

    private void validateRsbyAdhaar(SeccMemberItem item) {
        if (item.getAadhaarAuth() != null && item.getAadhaarAuth().trim().equalsIgnoreCase(AppConstant.PENDING_STATUS)) {
            pendingCount++;
            HouseHoldItem houseHoldItem = null;
            for (HouseHoldItem item1 : pendingHouseholdList) {
                if (item.getRsbyUrnId().trim().equalsIgnoreCase(item1.getRsbyUrnId().trim())) {
                    houseHoldItem = item1;
                    break;
                }
            }
            Global.USER_NAME = ApplicationGlobal.USER_NAME;
            Global.USER_PASSWORD = ApplicationGlobal.USER_PASSWORD;
            GetPubKeycertificateData publicKey1 = new GetPubKeycertificateData();
            publicKey1.execute();

            StringBuilder total = new StringBuilder();
            try {

                InputStream is = getResources().openRawResource(
                        R.raw.uidai_auth_pre_prod);

                BufferedReader r = new BufferedReader(new InputStreamReader(is));



                System.out.println("4444444444444444444");

                String line = "";

                System.out.println("55555555555555");

                while ((line = r.readLine()) != null) {
                    // 	total.append();
                    //  	total.append();
                    total.append(line);

                }


                System.out.println("6666666666666");

                aadharCertificate = total.toString();
                if ((aadharCertificate.startsWith("-----BEGIN CERTIFICATE-----") && aadharCertificate.endsWith("-----END CERTIFICATE-----"))) {
                    aadharCertificate = aadharCertificate.replace("-----BEGIN CERTIFICATE-----", "");
                    aadharCertificate = aadharCertificate.replace("-----END CERTIFICATE-----", "");
                    aadharCertificate = aadharCertificate.replace("\r\n", "");
                }


            } catch (Exception e) {
                e.printStackTrace();
            }


            pidXml = AppUtility.generatePIDblockXml(context, aadharCertificate, item.getNameAadhaar(), item.getAadhaarDob(), item.getAadhaarGender(), item.getAadhaarNo().trim());
            if (pidXml != null && !pidXml.equalsIgnoreCase("")) {
                aadharServiceResponse = CustomHttp.HttpPostLifeCerticiate(Global.DEMO_AUTH, pidXml, AppConstant.AUTHORIZATION, AppConstant.AUTHORIZATIONVALUE);
                Log.e("Response", "==" + aadharServiceResponse);
                String JSON = null;
                try {

                    try {
                        XmlToJson xmlToJson = new XmlToJson.Builder(aadharServiceResponse).build();
                        JSON = xmlToJson.toString();
                    } catch (Exception ex) {

                    }
                    demoAuthResp = new AadhaarDemoAuthResponse().create(JSON);
                } catch (Exception ex) {

                }
            }


            if (demoAuthResp != null && demoAuthResp.getAuthRes() != null) {
                if (demoAuthResp.getAuthRes().getRet() != null) {
                    if (demoAuthResp.getAuthRes().getRet().trim().equalsIgnoreCase(AppConstant.VALID_STATUS)) {
                        validatedCount++;
                        item.setAadhaarAuth(demoAuthResp.getAuthRes().getRet().trim());
                        item.setError_code(null);
                        item.setError_msg(null);
                        item.setError_type(null);
                        SeccDatabase.updateSeccMember(item, context);
                        //   SeccDatabase.updateHouseHold(houseHoldItem,context);
                    } else if (demoAuthResp.getAuthRes().getRet().trim().equalsIgnoreCase(AppConstant.INVALID_STATUS)) {
                        invalidCount++;
                        item.setAadhaarAuth(demoAuthResp.getAuthRes().getRet().trim());
                        item.setError_code(AppConstant.AADHAAR_VALIDATION_ERROR);
                        item.setError_msg(AppConstant.INVALID_AADHAAR_MG + "  " + demoAuthResp.getAuthRes().getErr());
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

    private void validateSeccAadhaar(SeccMemberItem item) {
        if (item.getAadhaarAuth() != null && item.getAadhaarAuth().trim().equalsIgnoreCase(AppConstant.PENDING_STATUS)) {
            pendingCount++;
            HouseHoldItem houseHoldItem = null;
            for (HouseHoldItem item1 : pendingHouseholdList) {
                if (item.getHhdNo().trim().equalsIgnoreCase(item1.getHhdNo().trim())) {
                    houseHoldItem = item1;
                    break;
                }
            }
            Global.USER_NAME = ApplicationGlobal.USER_NAME;
            Global.USER_PASSWORD = ApplicationGlobal.USER_PASSWORD;
            GetPubKeycertificateData publicKey1 = new GetPubKeycertificateData();
            publicKey1.execute();

            StringBuilder total = new StringBuilder();
            try {

                InputStream is = getResources().openRawResource(
                        R.raw.uidai_auth_pre_prod);

                BufferedReader r = new BufferedReader(new InputStreamReader(is));
                System.out.println("4444444444444444444");

                String line = "";
                System.out.println("55555555555555");
                while ((line = r.readLine()) != null) {
                    // 	total.append();
                    //  	total.append();
                    total.append(line);

                }
                System.out.println("6666666666666");
                aadharCertificate = total.toString();
                if ((aadharCertificate.startsWith("-----BEGIN CERTIFICATE-----") && aadharCertificate.endsWith("-----END CERTIFICATE-----"))) {
                    aadharCertificate = aadharCertificate.replace("-----BEGIN CERTIFICATE-----", "");
                    aadharCertificate = aadharCertificate.replace("-----END CERTIFICATE-----", "");
                    aadharCertificate = aadharCertificate.replace("\r\n", "");

                }

            } catch (Exception e) {
                e.printStackTrace();
            }


            pidXml = AppUtility.generatePIDblockXml(context, aadharCertificate, item.getNameAadhaar(), item.getAadhaarDob(), item.getAadhaarGender(), item.getAadhaarNo().trim());
            if (pidXml != null && !pidXml.equalsIgnoreCase("")) {
                aadharServiceResponse = CustomHttp.HttpPostLifeCerticiate(Global.DEMO_AUTH, pidXml, AppConstant.AUTHORIZATION, AppConstant.AUTHORIZATIONVALUE);
                Log.e("Response", "==" + aadharServiceResponse);
                String JSON = null;
                try {

                    try {
                        XmlToJson xmlToJson = new XmlToJson.Builder(aadharServiceResponse).build();
                        JSON = xmlToJson.toString();
                    } catch (Exception ex) {

                    }
                    demoAuthResp = new AadhaarDemoAuthResponse().create(JSON);
                } catch (Exception ex) {

                }
            }


            if (demoAuthResp != null && demoAuthResp.getAuthRes() != null) {
                if (demoAuthResp.getAuthRes().getRet() != null) {
                    if (demoAuthResp.getAuthRes().getRet().trim().equalsIgnoreCase(AppConstant.VALID_STATUS)) {
                        validatedCount++;
                        item.setAadhaarAuth(demoAuthResp.getAuthRes().getRet().trim());
                        item.setError_code(null);
                        item.setError_msg(null);
                        item.setError_type(null);
                        SeccDatabase.updateSeccMember(item, context);
                        //   SeccDatabase.updateHouseHold(houseHoldItem,context);
                    } else if (demoAuthResp.getAuthRes().getRet().trim().equalsIgnoreCase(AppConstant.INVALID_STATUS)) {
                        invalidCount++;
                        item.setAadhaarAuth(demoAuthResp.getAuthRes().getRet().trim());
                        item.setError_code(AppConstant.AADHAAR_VALIDATION_ERROR);
                        item.setError_msg(AppConstant.INVALID_AADHAAR_MG + "  " + demoAuthResp.getAuthRes().getErr());
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

    private void showRsbyHouseHold(HouseHoldItem item, SyncStatusFragment.OtherMemberAdapter.MyViewHolder holder) {
        // holder.itemlay.setBackgroundColor(AppUtility.getColor(context,getResources().getColor(R.color.Bg_bal_color)));
        holder.urnIdLayout.setVisibility(View.VISIBLE);
        holder.urnIdTV.setText(AppUtility.formatUrn(item.getRsbyUrnId()));
        holder.hofNameTV.setText("");
        AppUtility.showLog(AppConstant.LOG_STATUS, "gahsgjga", "Hof Name : " + item.getName());
        holder.hofNameTV.setText(item.getRsbyName());
        holder.dataSourceTV.setText(AppConstant.RSBY_SOURCE_NAME);
        //  holder.dataSourceTV.setTextColor(AppUtility.getColor(context,getResources().getColor(R.color.Bg_bal_color)));
        String gender = "";
        if (item.getRsbyGender() != null && item.getRsbyGender().trim().equalsIgnoreCase(AppConstant.MALE_GENDER)) {
            gender = AppConstant.MALE;
        } else if (item.getRsbyGender() != null && item.getRsbyGender().trim().equalsIgnoreCase(AppConstant.FEMALE_GENDER)) {
            gender = AppConstant.FEMALE;
        } else {
            gender = AppConstant.OTHER_GENDER_NAME;
        }
        holder.genderTV.setText(gender);

    }

    private void showSeccHouseHold(HouseHoldItem item, SyncStatusFragment.OtherMemberAdapter.MyViewHolder holder) {
        holder.householdIdLayout.setVisibility(View.VISIBLE);
        holder.hofNameTV.setText("");
        AppUtility.showLog(AppConstant.LOG_STATUS, "gahsgjga", "Hof Name : " + item.getRsbyName());
        holder.hofNameTV.setText(item.getName());
        holder.dataSourceTV.setText(AppConstant.SECC_SOURCE_NAME);
        if (item.getHhdNo() != null) {
            holder.houseHoldNoTV.setText(item.getHhdNo());
        }
        if (item.getAhlslnohhd() != null)
            holder.houseHoldNo.setText(item.getAhlslnohhd());
        if (item.getName() != null)
            holder.hofNameTV.setText(item.getName());
        if (item.getFathername() != null) {
            holder.fatherNameTV.setVisibility(View.VISIBLE);
            holder.fatherNameTV.setText(item.getFathername());
        }
        if (item.getNameSl() != null)
            holder.localHofNameTV.setText(Html.fromHtml(item.getNameSl()));
        String gender = "";
        if (item.getGenderid() != null && item.getGenderid().trim().equalsIgnoreCase(AppConstant.MALE_GENDER)) {
            gender = AppConstant.MALE;
        } else if (item.getGenderid() != null && item.getGenderid().trim().equalsIgnoreCase(AppConstant.FEMALE_GENDER)) {
            gender = AppConstant.FEMALE;
        } else {
            gender = AppConstant.OTHER_GENDER_NAME;
        }
        holder.genderTV.setText(gender);
        String address = "";
        if (item != null) {
            if (item.getAddressline1() != null && !item.getAddressline1().trim().
                    equalsIgnoreCase("-") && !item.getAddressline1().equalsIgnoreCase("")) {
                address = address + item.getAddressline1() + ", ";
                // AppUtility.showLog(AppConstant.LOG_STATUS,TAG," Address3 : "+seccItem.getAddressline1());

            }
            if (item.getAddressline2() != null && !item.getAddressline2().trim()
                    .equalsIgnoreCase("-") && !item.getAddressline2().equalsIgnoreCase("")) {
                //AppUtility.showLog(AppConstant.LOG_STATUS,TAG," Address : "+seccItem.getAddressline2());
                address = address + item.getAddressline2() + ", ";
            }
            if (item.getAddressline3() != null && !item.getAddressline3().trim().
                    equalsIgnoreCase("-") && !item.getAddressline3().equalsIgnoreCase("")) {
                //AppUtility.showLog(AppConstant.LOG_STATUS,TAG," Address1 : "+seccItem.getAddressline3());

                address = address + item.getAddressline3() + ", ";
            }
            if (item.getAddressline4() != null && !item.getAddressline4().trim().
                    equalsIgnoreCase("-") && !item.getAddressline4().equalsIgnoreCase("")) {
                // AppUtility.showLog(AppConstant.LOG_STATUS,TAG," Address2 : "+seccItem.getAddressline4());

                address = address + item.getAddressline4();
            }
            holder.addressTV.setText(address);
        }
    }


    // asha otp

    private void validateViaAshaOtp() {
        /*syncAllBT.setText(context.getResources().getString(R.string.validateAshaOtp));
        syncAllBT.setTextColor(context.getResources().getColor(R.color.white));
        syncAllBT.setBackground(context.getResources().getDrawable(R.drawable.button_background_blue));
        syncAllBT.setVisibility(View.GONE);*/
        ashaOtp();
      /*  syncAllBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ashaOtp();
            }
        });*/
    }

    private void ashaOtp() {
        AppUtility.softKeyBoard(activity, 1);
        final AlertDialog askForPinDailog = new AlertDialog.Builder(context).create();
        LayoutInflater factory = LayoutInflater.from(context);
        View alertView = factory.inflate(R.layout.asha_aadhaar_popup, null);
        askForPinDailog.setView(alertView);
        askForPinDailog.setCancelable(false);
        askForPinDailog.setCanceledOnTouchOutside(false);
        askForPinDailog.show();
        // Log.d(TAG,"delete status :"+deleteStatus);
        // dialog.setContentView(R.layout.opt_auth_layout);
        //    final TextView otpAuthMsg=(TextView)alertView.findViewById(R.id.otpAuthMsg);
        final VerifierLoginResponse verifierDetail = VerifierLoginResponse.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.VERIFIER_CONTENT, context));
        final AutoCompleteTextView pinET = (AutoCompleteTextView) alertView.findViewById(R.id.deletPinET);
        pinET.requestFocus();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, aadhaarNumber);
        pinET.setThreshold(1);
        pinET.setAdapter(adapter);
        pinET.setInputType(InputType.TYPE_CLASS_NUMBER);
        pinET.setHint("Enter Asha Aadhar number");
        int maxLength = 12;
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(maxLength);
        pinET.setFilters(FilterArray);
        final TextView errorTV = (TextView) alertView.findViewById(R.id.invalidOtpTV);
        pinET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                isVeroff = Verhoeff.validateVerhoeff(s.toString());
                pinET.setTextColor(Color.BLACK);
                if (s.toString().length() > 11) {
                    if (!Verhoeff.validateVerhoeff(s.toString())) {
                        pinET.setTextColor(Color.RED);
                    } else {
                        pinET.setTextColor(Color.GREEN);
                        AppUtility.softKeyBoard(activity, 0);
                    }
                }
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
                if (activity.isNetworkAvailable()) {

                    AppUtility.softKeyBoard(activity, 0);
                    String pin = pinET.getText().toString();
                    if (pin.toString().equalsIgnoreCase(verifierDetail.getHnoAadhaarNo())) {
                        askForPinDailog.dismiss();
                        requestAadhaarAuth(pin.toString());
                    } else if (pin.equalsIgnoreCase("")) {
                        // CustomAlert.alertWithOk(context,"Please enter valid pin");
                        errorTV.setVisibility(View.VISIBLE);
                        errorTV.setText("Enter HNO Aadhar");
                        pinET.setText("");
                        //  pinET.setHint("");
                    } else if (!pin.toString().equalsIgnoreCase(verifierDetail.getHnoAadhaarNo())) {
                        errorTV.setVisibility(View.VISIBLE);
                        errorTV.setText("Invalid Aadhar number");
                        pinET.setText("");
                        // pinET.setHint("Enter 4-di");
                    } else if (!isVeroff) {
                        errorTV.setVisibility(View.VISIBLE);
                        errorTV.setText("Invalid Aadhar number");
                        pinET.setText("");
                    }

                } else {
                    CustomAlert.alertWithOk(context, getResources().getString(R.string.internet_connection_msg));
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

    private void submitAshaOtp(final String aadhar) {
        AppUtility.softKeyBoard(activity, 1);
        final AlertDialog askForPinDailog = new AlertDialog.Builder(context).create();
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
                AppUtility.softKeyBoard(activity, 0);
                String pin = pinET.getText().toString();
                /*if (pin.toString().equalsIgnoreCase(verifierDetail.getPin())) {*/
                validateOTP(aadhar, pinET.getText().toString());
              /*  } else if (pin.equalsIgnoreCase("")) {
                    // CustomAlert.alertWithOk(context,"Please enter valid pin");
                    errorTV.setVisibility(View.VISIBLE);
                    errorTV.setText("Enter pin");
                    pinET.setText("");
                    //  pinET.setHint("");
                } else if (!pin.toString().equalsIgnoreCase(verifierDetail.getPin())) {
                    errorTV.setVisibility(View.VISIBLE);
                    errorTV.setText("Enter correct pin");
                    pinET.setText("");
                    // pinET.setHint("Enter 4-di");
                }*/
            }
        });
        cancelBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askForPinDailog.dismiss();
            }
        });
    }

    private void allowSync() {
        AppUtility.alertWithOk(context, "Now you can Sync the household");
        AppUtility.hideSoftInput(activity);
        ashaOtpBT.setVisibility(View.GONE);
        syncAllBT.setText(context.getResources().getString(R.string.syncAll));
        syncAllBT.setTextColor(context.getResources().getColor(R.color.white));
        syncAllBT.setBackground(context.getResources().getDrawable(R.drawable.button_background_orange_ehit));
        syncAllBT.setVisibility(View.VISIBLE);

        syncAllBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activity.isNetworkAvailable()) {
                   /* if (pendingHouseholdList != null && pendingHouseholdList.size() > 0) {
                        startSyncServce(context, pendingHouseholdList);
                    }*/
                    validateValidatorOtp();
                } else {
                    CustomAlert.alertWithOk(context, getResources().getString(R.string.internet_connection_msg));
                }
            }
        });
    }

    private void requestAadhaarAuth(final String aadhaarNo) {
        VolleyTaskListener taskListener = new VolleyTaskListener() {
            @Override
            public void postExecute(String response) {
                Log.d(TAG, "Login Response : " + response.toString());
                AadhaarOtpResponse resp = AadhaarOtpResponse.create(response);
                System.out.print(resp);
                if (resp != null) {
                    if (resp.getRet() != null) {
                        if (resp.getRet().trim().equalsIgnoreCase(AppConstant.VALID_STATUS)) {
                            String stringContainingNumber = resp.getInfo();
                            System.out.print(stringContainingNumber);
                            String requiredString = resp.getInfo().substring(resp.getInfo().indexOf("*"), resp.getInfo().indexOf(",NA"));
                            popupForOTP(aadhaarNo, requiredString);
                        } else if (resp.getRet().trim().equalsIgnoreCase(AppConstant.INVALID_STATUS)) {
                            // CustomAlert.alertWithOk(context, resp.getErr());
                            CustomAlert.alertWithOk(context, context.getResources().getString(R.string.mobileno_not_reg_with_adhar));
                            //By Pass OTP

                            //   popupForOTP();
                        }
                    } else {
                        CustomAlert.alertWithOk(context, context.getResources().getString(R.string.unableToConnectUIDAI));
                    }

                } else {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.unableToConnectUIDAI));
                }

            }

            @Override
            public void onError(VolleyError error) {
                CustomAlert.alertWithOk(context, getResources().getString(R.string.aadhaar_connect_error));
                //  popupForOTP();
            }
        };
   /*     String url=AppConstant.AADHAR_OTP_AUTH_API+aadhaarNo+AppConstant.USER_ID+AppConstant.PASSWORD;
        AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"URL : "+url);
        CustomVolleyGet volley=new CustomVolleyGet(taskListener,"Please wait..",url.trim(),context);
        volley.execute();*/
        String url = AppConstant.AADHAR_OTP_AUTH_API_NEW;
        AadhaarAuthRequestItem aadhaarReq = new AadhaarAuthRequestItem();
        aadhaarReq.setUid(aadhaarNo);
        String imei = AppUtility.getIMEINumber(context);
        if (imei != null) {
            aadhaarReq.setImeiNo(imei);
        }
        aadhaarReq.setProject(AppConstant.PROJECT_NAME);
        aadhaarReq.setUserName(ApplicationGlobal.AADHAAR_AUTH_USERNAME);
        aadhaarReq.setUserPass(ApplicationGlobal.AADHAAR_AUTH_ENCRIPTED_PASSWORD);
        AppUtility.showLog(AppConstant.LOG_STATUS, TAG, " Login Response otp request : " + aadhaarReq.serialize() + " : API :" + url);
        CustomVolley volley = new CustomVolley(taskListener, context.getResources().getString(R.string.pleaseWait), AppConstant.AADHAR_OTP_AUTH_API_NEW, aadhaarReq.serialize(), null, null, context);
        volley.execute();
        Log.d(TAG, "OTP Request API : " + url);

    }

    private void validateOTP(final String aadhaarNo, final String otp) {

        TaskListener taskListener = new TaskListener() {
            @Override
            public void execute() {
                aadhaarRespItem = null;
                String url = AppConstant.AADHAAR_KYC_DATA_API_NEW;
                AadhaarAuthRequestItem aadhaarReq = new AadhaarAuthRequestItem();
                aadhaarReq.setUid(aadhaarNo.trim());
                String imei = AppUtility.getIMEINumber(context);
                if (imei != null) {
                    aadhaarReq.setImeiNo(imei);
                }
                aadhaarReq.setOtp(otp);
                aadhaarReq.setProject(AppConstant.PROJECT_NAME);
                aadhaarReq.setUserName(ApplicationGlobal.AADHAAR_AUTH_USERNAME);
                aadhaarReq.setUserPass(ApplicationGlobal.AADHAAR_AUTH_ENCRIPTED_PASSWORD);
                // AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Login Response otp request "+aadhaarReq.serialize()+" : API :"+url);
                try {
                    HashMap<String, String> response = CustomHttp.httpPost(url, aadhaarReq.serialize(), null);
                    if (response != null) {
                        aadhaarRespItem = AadhaarResponseItem.create(response.get(AppConstant.RESPONSE_BODY));
                    }

                } catch (Exception e) {

                }

            }

            @Override
            public void updateUI() {
                if (aadhaarRespItem != null) {
                    if (aadhaarRespItem.getResult() != null && aadhaarRespItem.getResult().equalsIgnoreCase("Y")) {
                        popUpDialogForOtp.dismiss();
                        allowSync();
                    } else if (aadhaarRespItem.getResult() != null && aadhaarRespItem.getResult().equalsIgnoreCase("N")) {
                        if (aadhaarRespItem.getErr() != null && !aadhaarRespItem.getErr().equalsIgnoreCase("")) {
                            CustomAlert.alertWithOk(context, getResources().getString(R.string.invalid_otp) + "\n" + aadhaarRespItem.getErr());
                        }
                    } else {
                        CustomAlert.alertWithOk(context, getResources().getString(R.string.invalid_otp));
                    }
                } else {
                    CustomAlert.alertWithOk(context, getResources().getString(R.string.aadhaar_connect_error));
                }
            }
        };

        if (asyncTask != null) {
            asyncTask.cancel(true);
            asyncTask = null;
        }

        asyncTask = new CustomAsyncTask(taskListener, context.getResources().getString(R.string.please_wait), context);
        asyncTask.execute();

    }


    private void popupForOTP(final String aadhar, String mobNo) {
        popUpDialogForOtp = new AlertDialog.Builder(context).create();
        LayoutInflater factory = LayoutInflater.from(context);
        View alertView = factory.inflate(R.layout.opt_auth_layout, null);
        popUpDialogForOtp.setView(alertView);
        popUpDialogForOtp.setCancelable(false);

        // dialog.setContentView(R.layout.opt_auth_layout);
        final TextView otpAuthMsg = (TextView) alertView.findViewById(R.id.otpAuthMsg);
        otpAuthMsg.setText(context.getResources().getString(R.string.otp_lable_title) + " " + mobNo);
        final Button okButton = (Button) alertView.findViewById(R.id.ok);
        Button cancelBT = (Button) alertView.findViewById(R.id.cancelBT);
        final Button resendBT = (Button) alertView.findViewById(R.id.resendBT);
        final TextView mTimer = (TextView) alertView.findViewById(R.id.timerTV);

        new CountDownTimer(60 * 1000 + 1000, 1000) {

            public void onTick(long millisUntilFinished) {

                int seconds = (int) (millisUntilFinished / 1000);
                int minutes = seconds / 60;
                seconds = seconds % 60;
                mTimer.setVisibility(View.VISIBLE);
                mTimer.setText(String.format("%02d", minutes)
                        + ":" + String.format("%02d", seconds));
            }

            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            public void onFinish() {
                mTimer.setVisibility(View.GONE);
                resendBT.setEnabled(true);
                resendBT.setTextColor(context.getResources().getColor(R.color.white));
                resendBT.setBackground(context.getResources().getDrawable(R.drawable.button_background_orange_ehit));
            }

        }.start();

        new CountDownTimer(10 * 1000 + 1000, 1000) {

            public void onTick(long millisUntilFinished) {


            }

            public void onFinish() {

                okButton.setEnabled(true);
                okButton.setBackground(context.getResources().getDrawable(R.drawable.button_background_orange_ehit));
                okButton.setTextColor(context.getResources().getColor(R.color.white));

            }

        }.start();


        final EditText optET = (EditText) alertView.findViewById(R.id.otpET);
        //openSoftinputKeyBoard();
        AppUtility.showSoftInput(activity);
        final TextView inalidOTP = (TextView) alertView.findViewById(R.id.invalidOtpTV);
        optET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // inalidOTP.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        // optET.setText("4040");
      /*  String mobileNo="XXXX";
        if(loginResponse!=null && loginResponse.getMobileNumber()!=null && loginResponse.getMobileNumber().length()==10) {
          mobileNo = loginResponse.getMobileNumber().substring(6);
        }*/
        //  otpAuthMsg.setText(context.getResources().getString(R.string.pleaseEnterAadharOtp) + mobileNo.replace("*", "X"));
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otp = optET.getText().toString();
                if (!otp.equalsIgnoreCase("")) {

                    validateOTP(aadhar, otp);
                } else {
                    inalidOTP.setVisibility(View.VISIBLE);
                }
               /* otpAuthMsg.setVisibility(View.GONE);
                if (otp.equalsIgnoreCase("4040")) {
                    updatedVersionApp();
                    // pinOfflineLogin();
                    dialog.dismiss();
                } else {
                    otpAuthMsg.setVisibility(View.VISIBLE);
                }*/
            }
        });

        resendBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestAadhaarAuth(aadhar);
                popUpDialogForOtp.dismiss();

            }
        });
        cancelBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUpDialogForOtp.dismiss();
            }
        });
        popUpDialogForOtp.show();
    }

    private void alertForConsent(Context context) {

        final AlertDialog internetDiaolg = new AlertDialog.Builder(context).create();
        LayoutInflater factory = LayoutInflater.from(context);
        View alertView = factory.inflate(R.layout.rsbyconsent_layout, null);
        internetDiaolg.setView(alertView);
        internetDiaolg.show();
        final CheckBox consent = (CheckBox) alertView.findViewById(R.id.consent);
        Button tryAgainBT = (Button) alertView.findViewById(R.id.tryAgainBT);
        TextView deleteMsg = (TextView) alertView.findViewById(R.id.deleteMsg);
        deleteMsg.setText("Asha has verified data captured in the survey");
        Button cancelBT = (Button) alertView.findViewById(R.id.cancelBT);
        final TextView errorTV = (TextView) alertView.findViewById(R.id.errorTV);

        tryAgainBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (consent.isChecked()) {
                    errorTV.setVisibility(View.GONE);
                    internetDiaolg.dismiss();
                    validateViaAshaOtp();
                } else {
                    errorTV.setVisibility(View.VISIBLE);
                    errorTV.setText("Please check the consent.");
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


    ///    verifing validator


    private void verifyValidator(final String pin) {

        TaskListener taskListener = new TaskListener() {
            @Override
            public void execute() {
                String url = AppConstant.VERIFY_VALIDATOR;
                VerifyValidator request = new VerifyValidator();
                request.setAadhaarNumber(aadhaarNumber[0]);
                request.setImei(AppUtility.getIMEINumber(context));
                request.setPin(pin);
                try {
                    HashMap<String, String> response = CustomHttp.httpPost(url, request.serialize());
                    if (response != null) {
                        verifierResponse = ValidatorVerificationResponse.create(response.get(AppConstant.RESPONSE_BODY));
                    }

                } catch (Exception e) {

                }

            }

            @Override
            public void updateUI() {
                if (verifierResponse != null) {
                    if (verifierResponse.isStatus()) {
                        if (pendingHouseholdList != null && pendingHouseholdList.size() > 0) {
                            startSyncServce(context, pendingHouseholdList);
                        }
                    } else {
                        AppUtility.alertWithOk(context, verifierResponse.getErrorMessage());
                    }
                } else {
                    AppUtility.alertWithOk(context, "Error Occured");
                }

            }
        };
        if (asyncTask != null) {
            asyncTask.cancel(true);
            asyncTask = null;
        }
        asyncTask = new CustomAsyncTask(taskListener, context.getResources().getString(R.string.please_wait), context);
        asyncTask.execute();

    }


    private void validateValidatorOtp() {
        AppUtility.softKeyBoard(activity, 1);
        final AlertDialog askForPinDailog = new AlertDialog.Builder(context).create();
        LayoutInflater factory = LayoutInflater.from(context);
        View alertView = factory.inflate(R.layout.ask_pin_layout, null);
        askForPinDailog.setView(alertView);
        askForPinDailog.show();
        final VerifierLoginResponse verifierDetail = VerifierLoginResponse.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.VERIFIER_CONTENT, context));
        final EditText pinET = (EditText) alertView.findViewById(R.id.deletPinET);
        final TextView errorTV = (TextView) alertView.findViewById(R.id.invalidOtpTV);

        Button proceedBT = (Button) alertView.findViewById(R.id.proceedBT);
        Button cancelBT = (Button) alertView.findViewById(R.id.cancelBT);
        proceedBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppUtility.softKeyBoard(activity, 0);
                String pin = pinET.getText().toString();
                if (!pin.equalsIgnoreCase("") && pin.length() > 3) {
                    verifyValidator(pin);
                    askForPinDailog.dismiss();
                } else {
                    errorTV.setVisibility(View.VISIBLE);
                    errorTV.setText("Enter pin");
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


    private class GetPubKeycertificateData extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            String result = null;
            StringBuilder total = new StringBuilder();

            try {

                InputStream is = getResources().openRawResource(
                        R.raw.uidai_auth_pre_prod);

                BufferedReader r = new BufferedReader(new InputStreamReader(is));
                System.out.println("4444444444444444444");

                String line = "";
                System.out.println("55555555555555");
                while ((line = r.readLine()) != null) {
                    // 	total.append();
                    //  	total.append();
                    total.append(line);

                }
                System.out.println("6666666666666");
                result = total.toString();
                Log.e("result", "==" + result);

            } catch (Exception e) {
                Log.e("GetPubKeycert", "=" + e);
                //    ShowPrompt("Connection Issue", "Please go back...");
                System.out.println("errrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr");
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            if ((result.startsWith("-----BEGIN CERTIFICATE-----") && result.endsWith("-----END CERTIFICATE-----"))) {
                result = result.replace("-----BEGIN CERTIFICATE-----", "");
                result = result.replace("-----END CERTIFICATE-----", "");
                result = result.replace("\r\n", "");
                aadharCertificate = result;
            }
            //			if (result.endsWith("=") ) {
            //				result=result.replace("\r\n", "");
            //				Global.productionPublicKey=result;
            //			}
            else {
                //				ShowPrompt("Critical Error", "Please go back...");
                Global.USER_NAME = ApplicationGlobal.USER_NAME;
                Global.USER_PASSWORD = ApplicationGlobal.USER_PASSWORD;
                GetPubKeycertificateData publicKey1 = new GetPubKeycertificateData();
                publicKey1.execute();
            }

        }
    }

    private class HitToServer extends AsyncTask<String, Void, AadhaarDemoAuthResponse> {

        @Override
        protected AadhaarDemoAuthResponse doInBackground(String... params) {
            String JSON;

            return demoAuthResp;
        }

        @Override
        protected void onPostExecute(AadhaarDemoAuthResponse result) {

        }
    }
}
