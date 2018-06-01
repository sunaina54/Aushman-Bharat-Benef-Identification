package com.nhpm.rsbyFieldValidation.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.nhpm.LocalDataBase.dto.SeccDatabase;
import com.nhpm.Models.response.rsbyMembers.RSBYItem;
import com.nhpm.Models.response.rsbyMembers.RsbyHouseholdItem;
import com.nhpm.Models.response.verifier.FamilyStatusItem;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;
import com.nhpm.Utility.AppUtility;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RsbyFamilyListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RsbyFamilyListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private  String urnid ;
    private Context context;
    private ArrayList<RSBYItem> rsbyItemList;
    private RsbyHouseholdItem rsbyHouseHoldItem;
    private Spinner householdStatusSP;
    public FamilyStatusItem householdStatus;
    private ArrayList<FamilyStatusItem> familyStatusList;
    private FragmentManager fragMgr;
    private FragmentTransaction fragTransect;
    private DefaultRsbyListFragment defaultRsbyListFragment;
    private RsbyOldheadFragment oldHeadFragment;
    private String RequiredUrnId;

    private final String TAG="RsbyFamilyListFragment";

    public RsbyFamilyListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RsbyFamilyListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RsbyFamilyListFragment newInstance(String param1, String param2) {
        RsbyFamilyListFragment fragment = new RsbyFamilyListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
      //  args.putString(ARG_PARAM2, param2);
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
        context=getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_family_list, container, false);
        setupScreen(view);
        return view;
    }

    public String getRequiredUrnId() {
        return RequiredUrnId;
    }

    public void setRequiredUrnId(String requiredUrnId) {
        RequiredUrnId = requiredUrnId;
    }

    private void setupScreen(View v){
        urnid = getArguments().getString("URN");
    //    Toast.makeText(context,urnid,Toast.LENGTH_SHORT).show();
        if(urnid!=null) {
            if (SeccDatabase.getRsbyMemberList(urnid, context) != null && SeccDatabase.getRsbyMemberList(urnid, context).size() > 0) {
                rsbyItemList = SeccDatabase.getRsbyMemberList(urnid, context);

                rsbyHouseHoldItem = SeccDatabase.getRsbyHouseHoldQ(urnid, context);
            }
        }
        fragMgr=getActivity().getSupportFragmentManager();
        householdStatusSP=(Spinner)v.findViewById(R.id.familyStatusSP);
        //hofStatusSP=(Spinner)v.findViewById(R.id.headMemberStatusSP);

        householdStatusSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                householdStatus=familyStatusList.get(i);
                //  headMemberStatusLayout.setVisibility(View.GONE);
                if(householdStatus.getStatusCode().equalsIgnoreCase(AppConstant.DEFAULT_HOUSEHOLD)) {
                    showDefaultFamilyList(urnid);
                }else if(householdStatus.getStatusCode().equalsIgnoreCase(AppConstant.HOUSEHOLD_FOUND)){
                    // headMemberStatusLayout.setVisibility(View.VISIBLE);
                    //   showDefaultFamilyList();
                    showOldHeadFragment(urnid);
                }else{
                    showDefaultFamilyList(urnid);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        if(rsbyItemList!=null && rsbyItemList.size()>0) {
            prepareFamilyStatusSpinner();
        }else {
          //  AppUtility.alertWithOk(context,"No data available.. Please Read card first");
        }
    }

    private void showDefaultFamilyList(String urn){
        fragTransect=fragMgr.beginTransaction();
        if(defaultRsbyListFragment!=null){
            fragTransect.detach(defaultRsbyListFragment);
            defaultRsbyListFragment=null;
        }
        defaultRsbyListFragment=new DefaultRsbyListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("URN", urn);
        defaultRsbyListFragment.setArguments(bundle);
        defaultRsbyListFragment.setFamilyStatusItem(householdStatus);
        fragTransect.replace(R.id.rsbyFamilyListContainer,defaultRsbyListFragment);
        fragTransect.commitAllowingStateLoss();
    }

    private void prepareFamilyStatusSpinner(){
        boolean flag=false;
        int count=0;
        familyStatusList=new ArrayList<>();
        ArrayList<String> spinnerList=new ArrayList<>();
        ArrayList<FamilyStatusItem> tempItem= SeccDatabase.getFamilyStatusList(context);
        familyStatusList.add(0,new FamilyStatusItem("H","0","Select Household Status",""));

        for(RSBYItem item : rsbyItemList){
            if(item.getName()==null || item.getName().equalsIgnoreCase("")){
                count=count+1;
            }
        }
        if(count==rsbyItemList.size()){
            for (FamilyStatusItem item : tempItem) {
                if(item.getStatusCode().equalsIgnoreCase(AppConstant.NO_FAMILY_LIVING)) {
                    // spinnerList.add(item.statusDesc);
                    familyStatusList.add(item);
                }
            }
        }else {
            for (FamilyStatusItem item : tempItem) {
                // if(item.getStatusCode().equalsIgnoreCase(AppConstant.NO_FAMILY_LIVING)) {
                // spinnerList.add(item.statusDesc);
                familyStatusList.add(item);
                //  }
            }
        }
        for (FamilyStatusItem item : familyStatusList) {
            spinnerList.add(item.statusDesc);
        }
        ArrayAdapter<String> maritalAdapter = new ArrayAdapter<>(context, R.layout.custom_drop_down, R.id.textView,spinnerList);
        householdStatusSP.setAdapter(maritalAdapter);
        int selectedHouseholdStat=0;
        AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Household status : "+rsbyHouseHoldItem.getHhStatus());
        if(familyStatusList!=null) {
            for (int i=0;i<familyStatusList.size();i++) {
                if (rsbyHouseHoldItem != null && rsbyHouseHoldItem.getHhStatus() != null&& !rsbyHouseHoldItem.getHhStatus().equalsIgnoreCase("")) {
                    if (familyStatusList.get(i).getStatusCode().equalsIgnoreCase(rsbyHouseHoldItem.getHhStatus().trim())) {
                        selectedHouseholdStat =i;
                        break;
                    }
                }
            }
        }
        AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Household status : " +
                ""+rsbyHouseHoldItem.getHhStatus()+" : "+selectedHouseholdStat);
       // householdStatusSP.setSelection(selectedHouseholdStat);
        householdStatusSP.setSelection(1);
        AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Household status : "+rsbyHouseHoldItem.getHhStatus()+" : "+householdStatusSP.getSelectedItemPosition());

    }

    private void showOldHeadFragment(String urn){
        fragTransect=fragMgr.beginTransaction();
        if(oldHeadFragment!=null){
            fragTransect.detach(oldHeadFragment);
            oldHeadFragment=null;
        }
        oldHeadFragment=new RsbyOldheadFragment();
        Bundle bundle = new Bundle();
        bundle.putString("URN", urn);
        oldHeadFragment.setArguments(bundle);
        oldHeadFragment.setFamilyStatusItem(householdStatus);
        fragTransect.replace(R.id.rsbyFamilyListContainer,oldHeadFragment);
        fragTransect.commitAllowingStateLoss();
    }

}
