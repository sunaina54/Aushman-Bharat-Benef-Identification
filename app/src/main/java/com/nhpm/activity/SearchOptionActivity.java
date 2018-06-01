package com.nhpm.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.customComponent.CustomAlert;
import com.customComponent.CustomAsyncTask;
import com.customComponent.TaskListener;
import com.customComponent.utility.ProjectPrefrence;
import com.nhpm.LocalDataBase.DatabaseHelpers;
import com.nhpm.LocalDataBase.dto.MemberRequest;
import com.nhpm.LocalDataBase.dto.SeccDatabase;
import com.nhpm.Models.response.master.ConfigurationItem;
import com.nhpm.Models.response.master.MemberStatusItem;
import com.nhpm.Models.response.master.StateItem;
import com.nhpm.Models.response.seccMembers.HouseHoldItem;
import com.nhpm.Models.response.seccMembers.SeccMemberItem;
import com.nhpm.Models.response.seccMembers.SeccMemberResponse;
import com.nhpm.Models.response.seccMembers.SelectedMemberItem;
import com.nhpm.Models.response.verifier.FamilyStatusItem;
import com.nhpm.Models.response.verifier.VerifierLocationItem;
import com.nhpm.Models.response.verifier.VerifierLoginResponse;
import com.nhpm.R;
import com.nhpm.ReqRespModels.NhsDataList;
import com.nhpm.Utility.AppConstant;
import com.nhpm.Utility.AppUtility;



import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import pl.polidea.view.ZoomView;

public class SearchOptionActivity extends BaseActivity implements ComponentCallbacks2 {

    private static final String TAG = "Search Option Activity";
    private Context context;
    private EditText searchMemberET, searchAadhaarET;
    private DatabaseHelpers dbHelper;
    private MemberRequest memberRequest;
    private ArrayList<NhsDataList> memberList;
    private RecyclerView searchMemberList;
    private TextView headerTV;
    private String searchOption;
    private ImageView settings;
    private String searchText;
    private RelativeLayout searchCancelRelLayout, notFindMemberLayout;
    private MemberAdapter adapter;
    private TextView notFindTV;
    private VerifierLocationItem verifierLoc;
    private SeccMemberResponse seccMemberResponse;
    private ArrayList<SeccMemberItem> seccMemberList;
    private RelativeLayout menuLayout;
    private ArrayList<String> spinnerList;
    private Spinner sortedSP;
    private CustomAsyncTask asyncTask;
    private ArrayList<MemberStatusItem> memberStatusList;
    private ArrayList<SeccMemberItem> dataList;
    private ArrayList<FamilyStatusItem> householdStatusList;
    private LinearLayout mZoomLinearLayout;
    private ZoomView zoomView;
    private String downloadedDataType;
    private Activity activity;
    private RadioButton houseHoldIdRB, nameRB;
    private RadioGroup searchRadioGroup;
    private LinearLayout radioGroupLayout, searchLayout;
    private Button cancelSearch;
    private boolean pinLockIsShown = false;
    private String zoomMode = "N";
    private VerifierLocationItem locationItem;

    private int wrongPinCount = 0;
    private long wrongPinSavedTime;
    private long currentTime;
    private TextView wrongAttempetCountValue,wrongAttempetCountText;
    private long millisecond24 = 86400000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        activity = this;
        checkAppConfig();

        if (zoomMode.equalsIgnoreCase("N")) {
            setContentView(R.layout.activity_search_option);
            setupScreenWithOutZoom();

        } else {
            setContentView(R.layout.dummy_layout_for_zooming);
            setupScreenWithZoom();

        }


        loadSeccFamilyMembers();
        //  publishSearchList(searchText);

        prepareFamilyStatusSpinner();
        sortedSP.setEnabled(false);
        sortedSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (dataList != null) {
                    sortList(i);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        backScreen();
        notFindMemberLayout.setVisibility(View.GONE);
        notFindMemberLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSearchRSBY();
            }
        });


        searchMemberET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d(TAG, " Search Query : " + s.toString());

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // searchCancelRelLayout.setVisibility(View.VISIBLE);
                if (s.toString().length() > 0) {
                    publishSearchList(s.toString());
                } else {
                    dataList = new ArrayList<SeccMemberItem>();
                    adapter = new MemberAdapter(context, dataList);
                    searchMemberList.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        searchAadhaarET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // publshSearchListByAadhaar(s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(context, settings);
                popup.getMenuInflater()
                        .inflate(R.menu.menu_members, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.dashboard:
                                //openSearchRSBY();
                                Intent theIntenzt = new Intent(context, SearchActivityWithHouseHold.class);
                                theIntenzt.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                theIntenzt.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(theIntenzt);
                                leftTransition();
                                break;
                            case R.id.logout:
                                Intent theIntent = new Intent(context, LoginActivity.class);
                                theIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                theIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(theIntent);
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
        searchRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                AppUtility.showSoftInput(activity);
                searchMemberET.setEnabled(true);
                if (checkedId == houseHoldIdRB.getId()) {
                    radioGroupLayout.setVisibility(View.GONE);
                    searchLayout.setVisibility(View.VISIBLE);
                    searchMemberET.setHint("Search by HhId#");

                    sortedSP.setSelection(1);
                    searchMemberET.setInputType(InputType.TYPE_CLASS_NUMBER);
                    searchMemberET.requestFocus();

                } else if (checkedId == nameRB.getId()) {
                    radioGroupLayout.setVisibility(View.GONE);
                    searchLayout.setVisibility(View.VISIBLE);
                    searchMemberET.setHint("Search by Name");
                    sortedSP.setSelection(0);
                    searchMemberET.setInputType(InputType.TYPE_CLASS_TEXT);
                    searchMemberET.requestFocus();

                }
            }
        });
        cancelSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameRB.setChecked(false);
                houseHoldIdRB.setChecked(false);
                searchRadioGroup.clearCheck();
                radioGroupLayout.setVisibility(View.VISIBLE);
                searchLayout.setVisibility(View.GONE);
                searchMemberET.setText("");
                searchMemberET.setEnabled(false);
                AppUtility.hideSoftInput(activity);
                AppUtility.hideSoftInput(activity, cancelSearch);

            }
        });
        searchLayout.setVisibility(View.VISIBLE);
        nameRB.setChecked(true);
        sortedSP.setSelection(0);
        searchMemberET.setHint("Search by Name");
        AppUtility.showSoftInput(activity);


    }

    private void setupScreenWithOutZoom() {
        // AppUtility.hideSoftInput(activity);
        houseHoldIdRB = (RadioButton) findViewById(R.id.houseHoldIdRB);
        nameRB = (RadioButton) findViewById(R.id.nameRB);
        searchRadioGroup = (RadioGroup) findViewById(R.id.searchRadioGroup);
        radioGroupLayout = (LinearLayout) findViewById(R.id.radioGroupLayout);
        cancelSearch = (Button) findViewById(R.id.cancelSearch);
        searchLayout = (LinearLayout) findViewById(R.id.searchLayout);

        memberStatusList = SeccDatabase.getMemberStatusList(context);
        householdStatusList = SeccDatabase.getFamilyStatusList(context);
        downloadedDataType = ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.DOWNLOADINGSOURCE, context);
        searchText = "";
        searchMemberET = (EditText) findViewById(R.id.searchMemberET);
        searchMemberET.setVisibility(View.VISIBLE);
        searchMemberET.setText(searchText);
        searchMemberET.setEnabled(false);

        sortedSP = (Spinner) findViewById(R.id.sortSP);
        searchAadhaarET = (EditText) findViewById(R.id.searchAadhaarET);
        searchAadhaarET.requestFocus();
        searchMemberList = (RecyclerView) findViewById(R.id.searchMemberList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        searchMemberList.setLayoutManager(layoutManager);
        searchMemberList.setHasFixedSize(true);
        //searchCancelRelLayout=(RelativeLayout)findViewById(R.id.searchCancelRelLayout);
        notFindMemberLayout = (RelativeLayout) findViewById(R.id.notFindRelLayout);
        notFindTV = (TextView) findViewById(R.id.notFindTV);
        notFindTV.setPaintFlags(notFindTV.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        headerTV = (TextView) findViewById(R.id.centertext);
        headerTV.setVisibility(View.VISIBLE);
        if (downloadedDataType != null && !downloadedDataType.equalsIgnoreCase("")) {
            if (locationItem != null && locationItem.getVtCode() != null && locationItem.getBlockCode() != null) {
                if (downloadedDataType.equalsIgnoreCase(AppConstant.VillageWiseDownloading)) {

                    headerTV.setText("Village #" + locationItem.getVtCode());

                } else if (downloadedDataType.equalsIgnoreCase(AppConstant.EbWiseDownloading)) {

                    headerTV.setText(context.getResources().getString(R.string.enumerationBlock) + locationItem.getBlockCode());
                } else if (downloadedDataType.equalsIgnoreCase(AppConstant.WardWiseDownloading)) {

                    headerTV.setText(context.getResources().getString(R.string.Ward_) + locationItem.getWardCode());
                } else if (downloadedDataType.equalsIgnoreCase(AppConstant.SubBlockWiseDownloading)) {
                    headerTV.setText(context.getResources().getString(R.string.subEnumBlock) + locationItem.getSubBlockcode());
                }
            } else {
                headerTV.setText("Search Members");
            }


        }

        menuLayout = (RelativeLayout) findViewById(R.id.menuLayout);
        settings = (ImageView) findViewById(R.id.settings);

        settings.setVisibility(View.VISIBLE);

/*

        loadSeccFamilyMembers();
        //  publishSearchList(searchText);

        prepareFamilyStatusSpinner();
        sortedSP.setEnabled(false);
        sortedSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (dataList != null) {
                    sortList(i);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        backScreen();
        notFindMemberLayout.setVisibility(View.GONE);
        notFindMemberLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSearchRSBY();
            }
        });


        searchMemberET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d(TAG, " Search Query : " + s.toString());

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // searchCancelRelLayout.setVisibility(View.VISIBLE);
                if (s.toString().length() > 0) {
                    publishSearchList(s.toString());
                } else {
                    dataList = new ArrayList<SeccMemberItem>();
                    adapter = new MemberAdapter(context, dataList);
                    searchMemberList.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        searchAadhaarET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // publshSearchListByAadhaar(s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(context, settings);
                popup.getMenuInflater()
                        .inflate(R.menu.menu_members, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.dashboard:
                                //openSearchRSBY();
                                Intent theIntenzt = new Intent(context, SearchActivityWithHouseHold.class);
                                theIntenzt.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                theIntenzt.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(theIntenzt);
                                leftTransition();
                                break;
                            case R.id.logout:
                                Intent theIntent = new Intent(context, LoginActivity.class);
                                theIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                theIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(theIntent);
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
        searchRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                AppUtility.showSoftInput(activity);
                searchMemberET.setEnabled(true);
                if (checkedId == houseHoldIdRB.getId()) {
                    radioGroupLayout.setVisibility(View.GONE);
                    searchLayout.setVisibility(View.VISIBLE);
                    searchMemberET.setHint("Search by HhId#");

                    sortedSP.setSelection(1);
                    searchMemberET.setInputType(InputType.TYPE_CLASS_NUMBER);
                    searchMemberET.requestFocus();

                } else if (checkedId == nameRB.getId()) {
                    radioGroupLayout.setVisibility(View.GONE);
                    searchLayout.setVisibility(View.VISIBLE);
                    searchMemberET.setHint("Search by Name");
                    sortedSP.setSelection(0);
                    searchMemberET.setInputType(InputType.TYPE_CLASS_TEXT);
                    searchMemberET.requestFocus();

                }
            }
        });
        cancelSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameRB.setChecked(false);
                houseHoldIdRB.setChecked(false);
                searchRadioGroup.clearCheck();
                radioGroupLayout.setVisibility(View.VISIBLE);
                searchLayout.setVisibility(View.GONE);
                searchMemberET.setText("");
                searchMemberET.setEnabled(false);
                AppUtility.hideSoftInput(activity);
                AppUtility.hideSoftInput(activity, cancelSearch);

            }
        });
        searchLayout.setVisibility(View.VISIBLE);
        nameRB.setChecked(true);
        sortedSP.setSelection(0);
        searchMemberET.setHint("Search by Name");
        AppUtility.showSoftInput(activity);

*/

    }

    private void setupScreenWithZoom() {
        mZoomLinearLayout = (LinearLayout) findViewById(R.id.mZoomLinearLayout);
        View v = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.activity_search_option, null, false);
        v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
        //   AppUtility.hideSoftInput(activity);
        houseHoldIdRB = (RadioButton) v.findViewById(R.id.houseHoldIdRB);
        nameRB = (RadioButton) v.findViewById(R.id.nameRB);
        searchRadioGroup = (RadioGroup) v.findViewById(R.id.searchRadioGroup);
        radioGroupLayout = (LinearLayout) v.findViewById(R.id.radioGroupLayout);
        cancelSearch = (Button) v.findViewById(R.id.cancelSearch);
        searchLayout = (LinearLayout) v.findViewById(R.id.searchLayout);

        memberStatusList = SeccDatabase.getMemberStatusList(context);
        householdStatusList = SeccDatabase.getFamilyStatusList(context);
        downloadedDataType = ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.DOWNLOADINGSOURCE, context);
        searchText = "";
        searchMemberET = (EditText) v.findViewById(R.id.searchMemberET);
        searchMemberET.setVisibility(View.VISIBLE);
        searchMemberET.setText(searchText);
        searchMemberET.setEnabled(false);

        sortedSP = (Spinner) v.findViewById(R.id.sortSP);
        searchAadhaarET = (EditText) v.findViewById(R.id.searchAadhaarET);
        searchAadhaarET.requestFocus();
        searchMemberList = (RecyclerView) v.findViewById(R.id.searchMemberList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        searchMemberList.setLayoutManager(layoutManager);
        searchMemberList.setHasFixedSize(true);
        //searchCancelRelLayout=(RelativeLayout)findViewById(R.id.searchCancelRelLayout);
        notFindMemberLayout = (RelativeLayout) v.findViewById(R.id.notFindRelLayout);
        notFindTV = (TextView) v.findViewById(R.id.notFindTV);
        notFindTV.setPaintFlags(notFindTV.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        headerTV = (TextView) v.findViewById(R.id.centertext);
        headerTV.setVisibility(View.VISIBLE);
        if (downloadedDataType != null && !downloadedDataType.equalsIgnoreCase("")) {
            if (locationItem != null && locationItem.getVtCode() != null && locationItem.getBlockCode() != null) {
                if (downloadedDataType.equalsIgnoreCase(AppConstant.VillageWiseDownloading)) {

                    headerTV.setText("Village #" + locationItem.getVtCode());

                } else if (downloadedDataType.equalsIgnoreCase(AppConstant.EbWiseDownloading)) {

                    headerTV.setText(context.getResources().getString(R.string.enumerationBlock) + locationItem.getBlockCode());
                } else if (downloadedDataType.equalsIgnoreCase(AppConstant.WardWiseDownloading)) {

                    headerTV.setText(context.getResources().getString(R.string.Ward_) + locationItem.getWardCode());
                } else if (downloadedDataType.equalsIgnoreCase(AppConstant.SubBlockWiseDownloading)) {
                    headerTV.setText(context.getResources().getString(R.string.subEnumBlock) + locationItem.getSubBlockcode());
                }
            } else {
                headerTV.setText("Search Members");
            }


        }

        menuLayout = (RelativeLayout) v.findViewById(R.id.menuLayout);
        settings = (ImageView) v.findViewById(R.id.settings);


        zoomView = new ZoomView(this);
        zoomView.addView(v);
        mZoomLinearLayout.addView(zoomView);
        settings.setVisibility(View.VISIBLE);


    }

    private void sortList(int index) {
        switch (index) {
            case 0:
                Collections.sort(dataList, new Comparator<SeccMemberItem>() {
                    public int compare(SeccMemberItem v1, SeccMemberItem v2) {
                        return v1.getName().compareTo(v2.getName());
                    }
                });
                defaultList();
                break;
            case 1:
                Log.d("Pending household", " Sort by House No ");
                Collections.sort(dataList, new Comparator<SeccMemberItem>() {
                    @Override
                    public int compare(SeccMemberItem o1, SeccMemberItem o2) {
                        AppUtility.showLog(AppConstant.LOG_STATUS, "HouseholdStatusFragment", o1.getHhdNo());
                        if (o1.getHhdNo() != null && !o1.getHhdNo().equalsIgnoreCase("") && o2.getHhdNo() != null && !o2.getHhdNo().equalsIgnoreCase("")) {

                            return o1.getHhdNo().compareTo(o2.getHhdNo());

                        }

                        return 0;
                    }
                });

                defaultList();
                break;
        }


    }

    private void prepareFamilyStatusSpinner() {

        spinnerList = new ArrayList<>();
        spinnerList.add("Sort By Name");
        spinnerList.add("Sort By Household ID");
        ArrayAdapter<String> maritalAdapter = new ArrayAdapter<>(context, R.layout.custom_drop_down, R.id.textView, spinnerList);
        sortedSP.setAdapter(maritalAdapter);

    }

    private void openSearchRSBY() {
        Intent theIntent = new Intent(context, SearchRSBYMemberActivity.class);
        startActivity(theIntent);
        leftTransition();
    }

    private void publshSearchListByAadhaar(String searchTag) {
        ArrayList<SeccMemberItem> dataList = new ArrayList<>();
        for (SeccMemberItem item : seccMemberList) {
            if (item.getAadhaarNo().toLowerCase().startsWith(searchTag.toLowerCase())) {
                dataList.add(item);
            }
        }
        MemberAdapter adapter = new MemberAdapter(context, dataList);
        searchMemberList.setAdapter(adapter);
    }

    private void publishSearchList(String searchTag) {
        dataList = new ArrayList<>();
        for (SeccMemberItem item : seccMemberList) {
            if (item.getName() != null && item.getName().toLowerCase().contains(searchTag.toLowerCase())) {
                dataList.add(item);
            }
            if (item.getAadhaarNo() != null && item.getAadhaarNo().toLowerCase().contains(searchTag.toLowerCase())) {
                dataList.add(item);
            }
            if (item.getHhdNo() != null && item.getHhdNo().toLowerCase().contains(searchTag.toLowerCase())) {
                dataList.add(item);
            }
        }
        //if(dataList.size()>0){
        notFindMemberLayout.setVisibility(View.GONE);
        if (dataList != null) {
            if (nameRB.isChecked()) {
                sortList(0);
            } else if (houseHoldIdRB.isChecked()) {
                sortList(1);
            }
        }
        adapter = new MemberAdapter(context, dataList);
        searchMemberList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        //  }

      /*  if(dataList.size()>0){
            notFindMemberLayout.setVisibility(View.GONE);
            adapter=new MemberAdapter(context,dataList);
            searchMemberList.setAdapter(adapter);
        }else{
            searchMemberList.setAdapter(null);
            notFindMemberLayout.setVisibility(View.VISIBLE);
        }*/
    }

    private void backScreen() {
        final ImageView backIV = (ImageView) findViewById(R.id.back);
        RelativeLayout backLayout = (RelativeLayout) findViewById(R.id.backLayout);
        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                rightTransition();
            }
        });
        backLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backIV.performClick();
            }
        });
    }

    private void loadSeccFamilyMembers() {
        verifierLoc = VerifierLocationItem.create(ProjectPrefrence.getSharedPrefrenceData(
                AppConstant.PROJECT_PREF, AppConstant.SELECTED_BLOCK, context));
        TaskListener taskListener = new TaskListener() {
            @Override
            public void execute() {
                if (downloadedDataType != null && downloadedDataType.equalsIgnoreCase(AppConstant.EbWiseDownloading)) {
                    seccMemberList = SeccDatabase.getSearchSeccMemberList(verifierLoc.getStateCode()
                            , verifierLoc.getDistrictCode(), verifierLoc.getTehsilCode(), verifierLoc.getVtCode(), verifierLoc.getWardCode(),
                            verifierLoc.getBlockCode(), context);
                } else if (downloadedDataType != null && downloadedDataType.equalsIgnoreCase(AppConstant.EbWiseDownloading)) {
               /*     seccMemberList=SeccDatabase.getSeccMemberVillageWiseList(verifierLoc.getStateCode()
                            ,verifierLoc.getDistrictCode(),verifierLoc.getTehsilCode(),verifierLoc.getVtCode(),context);*/
                    seccMemberList = SeccDatabase.getSearchSeccMemberVillageWiseList(verifierLoc.getStateCode()
                            , verifierLoc.getDistrictCode(), verifierLoc.getTehsilCode(), verifierLoc.getVtCode(), context);

                } else if (downloadedDataType != null && downloadedDataType.equalsIgnoreCase(AppConstant.WardWiseDownloading)) {
                    seccMemberList = SeccDatabase.getSearchSeccMemberWardWiseList(verifierLoc.getStateCode()
                            , verifierLoc.getDistrictCode(), verifierLoc.getTehsilCode(), verifierLoc.getVtCode(), verifierLoc.getWardCode(), context);
                } else if (downloadedDataType != null && downloadedDataType.equalsIgnoreCase(AppConstant.SubBlockWiseDownloading)) {
                    seccMemberList = SeccDatabase.getSearchSeccMemberSubEbWiseList(verifierLoc.getStateCode()
                            , verifierLoc.getDistrictCode(), verifierLoc.getTehsilCode(), verifierLoc.getVtCode(), verifierLoc.getWardCode(), verifierLoc.getBlockCode(), verifierLoc.getSubBlockcode(), context);
                }

                // Log.d(TAG, "Secc Member list1111 : " + seccMemberList.size());
                // sortList(0);
            }

            @Override
            public void updateUI() {
                //  defaultList();
                // sortList(0);
            }
        };

        if (asyncTask != null && !asyncTask.isCancelled()) {
            asyncTask.cancel(true);
            asyncTask = null;
        }
        asyncTask = new CustomAsyncTask(taskListener, "Please wait..", context);
        asyncTask.execute();
        /*for(SeccMemberItem item : seccMemberResponse.getSeccMemberList()){
            String locStr=verifierLoc.getStateCode()+verifierLoc.getDistrictCode()+verifierLoc.getTehsilCode()
                    +verifierLoc.getVillTownCode()+verifierLoc.getWardCode()+verifierLoc.getBlockCode();
            String seccStr=item.getStatecode()+item.getDistrictcode()+item.getTehsilcode()+item.getTowncode()+item.getWardid()+
                    item.getBlockno();
            if(locStr.equalsIgnoreCase(seccStr)){
                seccMemberList.add(item);
            }
        }*/
    }

    private void defaultList() {

        adapter = new MemberAdapter(context, dataList);
        searchMemberList.setAdapter(adapter);


    }

    private class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.MyViewHolder> {
        View view;
        AlertDialog dialog;
        private ArrayList<SeccMemberItem> dataSet;
        private Context context;
        private TextView text;

        public MemberAdapter(Context context, ArrayList<SeccMemberItem> data) {
            this.dataSet = data;
            Log.d(TAG, "member List size : " + dataSet.size());
            this.context = context;
            this.text = text;
        }

        private void openFamilyMemberList() {

        }

        public void addAll(List<SeccMemberItem> list) {

            dataSet.addAll(list);
            notifyDataSetChanged();
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.search_member_list_item, parent, false);
            //view.setOnClickListener(MainActivity.myOnClickListener);

            MyViewHolder myViewHolder = new MyViewHolder(view);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

            SeccMemberItem item = dataSet.get(listPosition);
            if (item.getHhdNo() != null)
                holder.houseNoTV.setText(item.getHhdNo().replace("-", ""));
            if (item.getName() != null) {
                holder.nameTV.setText(item.getName());
            }
            holder.parentLayout.setBackgroundColor(AppUtility.getColor(context, R.color.white));
            if (item.getRelation() != null && item.getRelation().equalsIgnoreCase("HEAD")) {
                holder.parentLayout.setBackgroundColor(AppUtility.getColor(context, R.color.Bg_bal_color));
            }

            if (item.getNameSl() != null)
                // holder.locNameTV.setText(item.getNameSl());
                if (item.getFathername() != null)
                    holder.fatherNameTV.setText(item.getFathername());
            if (item.getAadhaarNo() != null) {
                holder.aadhaarTV.setText(item.getAadhaarNo());
            }
            holder.memberStatusTV.setText("-");
            if (item.getMemStatus() != null) {
                if (item.getMemStatus() != null && !item.getMemStatus().equalsIgnoreCase("")) {
                    for (MemberStatusItem item1 : memberStatusList) {
                        if (item1.getStatusCode().equalsIgnoreCase(item.getMemStatus())) {
                            holder.memberStatusTV.setText(item1.getStatusDesc());
                            break;
                        }
                    }
                /*if(flag){
                    holder.memberStatusLayout.setVisibility(View.VISIBLE);

                }*/
                }
            }
            holder.householdStatusTV.setText("-");
            if (item.getHhStatus() != null && !item.getHhStatus().equalsIgnoreCase("")) {
                for (FamilyStatusItem familyStatusItem : householdStatusList) {
                    if (item.getHhStatus().equalsIgnoreCase(familyStatusItem.getStatusCode())) {
                        holder.householdStatusTV.setText(familyStatusItem.getStatusDesc());
                        break;
                    }
                }

            }
            //   Log.d(TAG,"Address"+dataSet.get(listPosition).getAddressline1());
            String address = "";
            /*if(dataSet.get(listPosition).getAddressline1()!=null){
                address1=dataSet.get(listPosition).getAddressline1();
            }
            if(dataSet.get(listPosition).getAddressline2()!=null){
                address2=dataSet.get(listPosition).getAddressline2();
            }*/
            // address=address1+", "+address2;
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
            if (item.getGenderid() != null && item.getGenderid().equalsIgnoreCase("1")) {
                holder.genderTV.setText(AppConstant.MALE);
            } else {
                holder.genderTV.setText(AppConstant.FEMALE);
            }
            if (item.getNameSl() != null) {
                holder.reginalName.setText(Html.fromHtml(item.getNameSl()));
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

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView nameTV, genderTV, fatherNameTV, addressTV, houseNoTV, aadhaarTV, locNameTV, memberStatusTV, householdStatusTV, reginalName;
            RelativeLayout parentLayout;

            public MyViewHolder(View itemView) {
                super(itemView);
                this.houseNoTV = (TextView) itemView.findViewById(R.id.houseNoTV);
                this.genderTV = (TextView) itemView.findViewById(R.id.genderTV);
                this.nameTV = (TextView) itemView.findViewById(R.id.nameTV);
                this.fatherNameTV = (TextView) itemView.findViewById(R.id.fatherNameTV);
                this.addressTV = (TextView) itemView.findViewById(R.id.addressTV);
                this.locNameTV = (TextView) itemView.findViewById(R.id.localNameTV);
                this.aadhaarTV = (TextView) itemView.findViewById(R.id.aadhaarTV);
                this.memberStatusTV = (TextView) itemView.findViewById(R.id.memberStatusTV);
                this.householdStatusTV = (TextView) itemView.findViewById(R.id.householdStatusTV);
                this.parentLayout = (RelativeLayout) itemView.findViewById(R.id.parentLayout);
                this.reginalName = (TextView) itemView.findViewById(R.id.c);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HouseHoldItem houseHoldItem = SeccDatabase.getHouseHoldList(dataSet.get(getPosition()).getHhdNo(), context);
                        SelectedMemberItem memberItem = new SelectedMemberItem();
                        memberItem.setHouseHoldItem(houseHoldItem);
                        Intent theIntent = new Intent(context, SeccMemberListActivity.class);
                        ProjectPrefrence.saveSharedPrefrenceData
                                (AppConstant.PROJECT_PREF, AppConstant.SELECTED_ITEM_FOR_VERIFICATION, memberItem.serialize(), context);
                        startActivity(theIntent);
                        leftTransition();
                    }
                });
                //this.sendBT=(Button)itemView.findViewById(R.id.send);
            }
        }
    }

    private void dashboardDropdown() {
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
                        .inflate(R.menu.menu_nav_dashboard, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {

                            case R.id.dashboard:
                                //   String searchText = searchFamilyMemberET.getText().toString();
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

    private String checkAppConfig() {
        downloadedDataType = ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.DOWNLOADINGSOURCE, context);
        locationItem = VerifierLocationItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SELECTED_BLOCK, context));
        StateItem selectedStateItem = StateItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SELECTED_STATE, context));
        if (selectedStateItem != null && selectedStateItem.getStateCode() != null) {
            ArrayList<ConfigurationItem> configList = SeccDatabase.findConfiguration(selectedStateItem.getStateCode(), context);
            if (configList != null) {
                for (ConfigurationItem item1 : configList) {

                    if (item1.getConfigId().equalsIgnoreCase(AppConstant.APPLICATION_ZOOM)) {
                        zoomMode = item1.getStatus();
                    }
                    if (item1.getConfigId().equalsIgnoreCase(AppConstant.DATA_DOWNLOAD)) {
                        downloadedDataType = item1.getStatus();
                    }

                }
            }
        }
        return null;
    }
}
