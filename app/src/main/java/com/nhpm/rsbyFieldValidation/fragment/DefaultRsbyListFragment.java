package com.nhpm.rsbyFieldValidation.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.customComponent.CustomAlert;
import com.customComponent.utility.ProjectPrefrence;
import com.nhpm.LocalDataBase.dto.SeccDatabase;
import com.nhpm.Models.response.RelationItem;
import com.nhpm.Models.response.rsbyMembers.RSBYItem;
import com.nhpm.Models.response.rsbyMembers.RsbyHouseholdItem;
import com.nhpm.Models.response.seccMembers.SelectedMemberItem;
import com.nhpm.Models.response.verifier.FamilyStatusItem;
import com.nhpm.Models.response.verifier.VerifierLoginResponse;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;
import com.nhpm.Utility.AppUtility;
import com.nhpm.activity.SearchActivityWithHouseHold;
import com.nhpm.rsbyFieldValidation.RsbyMainActivity;
import com.nhpm.rsbyFieldValidation.RsbyValidationWithAadharActivity;

import java.util.ArrayList;


public class DefaultRsbyListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private AlertDialog askForPinDailog,lockDialog;
    private FamilyStatusItem householdStatus;
    private LinearLayout footerLayout;
    private Button saveBT,lockBT;
    private String mParam2;
    private RecyclerView recycleView;
    private RsbyMainActivity rsbyMainActivity;
    private Context context;
    private String urnid ;
    private ArrayList<RSBYItem> houseHoldMemberList;
    private RsbyHouseholdItem rsbyHouseHoldItem;
    private FamilyStatusItem familyStatusItem;
    private ArrayList<RelationItem> relationList;
    private SelectedMemberItem selectedMemberItem;
    private VerifierLoginResponse loginDetail;
    private RsbyHouseholdItem houseHoldItem;

    private int wrongPinCount = 0;
    private long wrongPinSavedTime;
    private long currentTime;
    private TextView wrongAttempetCountValue,wrongAttempetCountText;
    private long millisecond24 = 86400000;




    public DefaultRsbyListFragment() {
        // Required empty public constructor
    }

    public ArrayList<RSBYItem> getHouseHoldMemberList() {
        return houseHoldMemberList;
    }

    public void setHouseHoldMemberList(ArrayList<RSBYItem> houseHoldMemberList) {
        this.houseHoldMemberList = houseHoldMemberList;
    }

    public FamilyStatusItem getFamilyStatusItem() {
        return familyStatusItem;
    }

    public void setFamilyStatusItem(FamilyStatusItem familyStatusItem) {
        this.familyStatusItem = familyStatusItem;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        rsbyMainActivity=(RsbyMainActivity)context;
    }
    public static DefaultRsbyListFragment newInstance(String param1, String param2) {
        DefaultRsbyListFragment fragment = new DefaultRsbyListFragment();
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
        context=getActivity();
        View v=inflater.inflate(R.layout.fragment_default_rsby_list, container, false);
        setupScreen(v);
        return v;
    }



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */

    public class RsbyHouseholdAdapter extends RecyclerView.Adapter<RsbyHouseholdAdapter.MyViewHolder> {

        private ArrayList<RSBYItem> householdList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView nameTV, urnNoTV , dateOfBirthTV,genderTV , memberIdTV;
            public LinearLayout verifyLayout;
            private Button proceedSurveyBT;
            private RelativeLayout lay;

            public MyViewHolder(View view) {
                super(view);
                urnNoTV = (TextView) view.findViewById(R.id.urnNoTV);
                nameTV = (TextView) view.findViewById(R.id.nameTV);
                dateOfBirthTV = (TextView) view.findViewById(R.id.dateOfBirthTV);
                genderTV = (TextView) view.findViewById(R.id.genderTV);
                memberIdTV = (TextView) view.findViewById(R.id.memberIdTV);
                verifyLayout = (LinearLayout) view.findViewById(R.id.verifyLayout);
                verifyLayout.setVisibility(View.GONE);
                proceedSurveyBT=(Button)view.findViewById(R.id.proceedSurveyBT);
                proceedSurveyBT.setVisibility(View.GONE);
                lay=(RelativeLayout)view.findViewById(R.id.lay);
            }
        }


        public RsbyHouseholdAdapter(ArrayList<RSBYItem> arrayList) {
            this.householdList = arrayList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.rsby_member_item, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            final RSBYItem item = householdList.get(position);
            holder.urnNoTV.setText(item.getUrnId());
            if(item.getGender()!=null && !item.getGender().equalsIgnoreCase("")){
                if(item.getGender().equalsIgnoreCase("1")){
                    holder.genderTV.setText("Male");
                }else if(item.getGender().equalsIgnoreCase("2")){
                    holder.genderTV.setText("Female");
                }else{
                    holder.genderTV.setText("Other");
                }
            }

            holder.nameTV.setText(item.getName());
            if(item.getRsbyMemId()!=null && houseHoldItem.getRsbyMemId()!=null ) {
                if (item.getRsbyMemId().trim().equalsIgnoreCase(houseHoldItem.getRsbyMemId())) {
                    holder.lay.setBackgroundColor(AppUtility.getColor(context, R.color.Bg_bal_color));
                }
            }

            holder.dateOfBirthTV.setText(AppUtility.convertRsbyDate(item.getDob()));
            holder.memberIdTV.setText(item.getMemid());
            holder.verifyLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SelectedMemberItem selectedMemItem=new SelectedMemberItem();
                    selectedMemItem.setRsbyMemberItem(item);
                    selectedMemItem.setRsbyHouseholdItem(rsbyHouseHoldItem);
                    System.out.print(item);
                    System.out.print(houseHoldItem);
                    ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SELECTED_ITEM_FOR_VERIFICATION, selectedMemItem.serialize(), context);
                    String payLoad = selectedMemItem.serialize().toString();
                    System.out.print(payLoad);
                    Intent theIntent = new Intent(context,RsbyValidationWithAadharActivity.class);
                    startActivity(theIntent);
                    rsbyMainActivity.leftTransition();
                }
            });
        }

        @Override
        public int getItemCount() {
            return householdList.size();
        }
    }

    private void setupScreen(View v){
        footerLayout=(LinearLayout)v.findViewById(R.id.footerLayout) ;
        saveBT=(Button)v.findViewById(R.id.saveBT) ;
        lockBT=(Button)v.findViewById(R.id.lockBT) ;
        relationList= SeccDatabase.getRelationList(context);
        selectedMemberItem= SelectedMemberItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.SELECTED_ITEM_FOR_VERIFICATION, context));
        loginDetail= VerifierLoginResponse.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.VERIFIER_CONTENT,context));
        if(selectedMemberItem.getRsbyHouseholdItem()!=null){
            houseHoldItem=selectedMemberItem.getRsbyHouseholdItem();
            // Log.d(TAG,"Household Status : "+houseHoldItem.getHhStatus());
        }

         householdStatus=rsbyMainActivity.householdStatus;
        if(householdStatus!=null && householdStatus.getStatusCode().equalsIgnoreCase(AppConstant.DEFAULT_HOUSEHOLD)){
            footerLayout.setVisibility(View.GONE);
        }else if(householdStatus!=null && householdStatus.getStatusCode().equalsIgnoreCase(AppConstant.HOUSEHOLD_FOUND)){
            footerLayout.setVisibility(View.GONE);
        }else{
            footerLayout.setVisibility(View.VISIBLE);
        }

        if(houseHoldItem!=null && houseHoldItem.getLockedSave()!=null &&
                houseHoldItem.getLockedSave().equalsIgnoreCase(AppConstant.LOCKED+"")){
            rsbyMainActivity.familyStatusSP.setEnabled(false);
            footerLayout.setVisibility(View.GONE);
        }
        saveBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveFamily();
            }
        });
        lockBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  lockPrompt();
                //askPinToLock();
                alertForConsent(context);
            }
        });
        recycleView = (RecyclerView)v.findViewById(R.id.recycleViewRsbyData);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        recycleView.setLayoutManager(mLayoutManager);
        recycleView.setItemAnimator(new DefaultItemAnimator());
        if(houseHoldMemberList!=null && houseHoldMemberList.size()>0){
            RsbyHouseholdAdapter adapter = new RsbyHouseholdAdapter(houseHoldMemberList);
            recycleView.setAdapter(adapter);
        }else{
          //  AppUtility.alertWithOk(context, "No data available.  Please read RSBY card first");
        }
    }
    private void askPinToLock() {
        askForPinDailog = new AlertDialog.Builder(context).create();
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


        wrongAttempetCountText = (TextView) alertView.findViewById(R.id.wrongAttempetCountText);
        wrongAttempetCountValue = (TextView) alertView.findViewById(R.id.wrongAttempetCountValue);


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


                             currentTime = System.currentTimeMillis();
                try {

                    wrongPinSavedTime = Long.parseLong(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.WORNG_PIN_ENTERED_TIMESTAMP, context));
                } catch (Exception ex) {
                    wrongPinSavedTime = 0;
                }
                if (currentTime > (wrongPinSavedTime + millisecond24)) {




                String pin = pinET.getText().toString();
                if (pin.toString().equalsIgnoreCase(verifierDetail.getPin())) {
                    lockSubmit();
                } else if (pin.equalsIgnoreCase("")) {
                    // CustomAlert.alertWithOk(context,"Please enter valid pin");
                    errorTV.setVisibility(View.VISIBLE);
                    errorTV.setText("Enter pin");
                    pinET.setText("");
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
                askForPinDailog.dismiss();
            }
        });
    }
    private void lockSubmit(){
        houseHoldItem.setHhStatus(householdStatus.getStatusCode());
        for(RSBYItem item : houseHoldMemberList){
            item.setHhStatus(houseHoldItem.getHhStatus());
                   /* if(houseHoldItem.getHhStatus().equalsIgnoreCase(AppConstant.HOUSEHOLD_NOT_FOUND)){
                        item.setMemStatus(AppConstant.MEMBER_NOT_FOUND);
                    }
                    if(houseHoldItem.getHhStatus().equalsIgnoreCase(AppConstant.HOUSEHOLD_LOCKED)){
                        item.setMemStatus(AppConstant.NO_INFO_AVAIL);
                    }

                    if(houseHoldItem.getHhStatus().equalsIgnoreCase(AppConstant.FAMILY_MIGRATED)){
                        item.setMemStatus(AppConstant.MEMBER_MIGRATED);
                    }
                    if(houseHoldItem.getHhStatus().equalsIgnoreCase(AppConstant.HOUSEHOLD_TO_ENROLL_RSBY)){
                        item.setMemStatus(AppConstant.MEMBER_ENROL_THROUGH_RSBY);
                    }*/


            if(houseHoldItem.getHhStatus().equalsIgnoreCase(AppConstant.HOUSEHOLD_FOUND)){
            }else{
                // item=AppUtility.addAdditionalParamInSecc(item,context);
                item.setMemStatus(AppConstant.NO_INFO_AVAIL);

            }
           /* if(houseHoldItem.getHhStatus().equalsIgnoreCase(AppConstant.FAMILY_MIGRATED)){
                item.setMemStatus(AppConstant.NO_INFO_AVAIL);
            }
            if(houseHoldItem.getHhStatus().equalsIgnoreCase(AppConstant.HOUSEHOLD_TO_ENROLL_RSBY)){
                item.setMemStatus(AppConstant.MEMBER_ENROL_THROUGH_RSBY);
            }
            if(houseHoldItem.getHhStatus().equalsIgnoreCase(AppConstant.NO_FAMILY_LIVING)){
                item.setMemStatus(AppConstant.NO_INFO_AVAIL);
            }*/

                    /*if(houseHoldItem.getHhStatus().equalsIgnoreCase(AppConstant.NO_MEMBER_LIVING)){
                        item.setMemberStatus(AppConstant.NO_INFO_AVAIL);
                    }*/
            //item.setMemberStatus(AppConstant.NO_INFO_AVAIL);
            item.setAadhaarVerifiedBy(loginDetail.getAadhaarNumber());
            item.setLockedSave(AppConstant.LOCKED+"");
            SeccDatabase.updateRsbyMember(item,context);
        }
        // houseHoldItem=AppUtility.addAdditionalParamInHousehold(houseHoldItem,context);
        houseHoldItem.setLockedSave(AppConstant.LOCKED+"");
        SeccDatabase.updateRSBYHouseHold(houseHoldItem,context);
        Intent theIntent=new Intent(context,SearchActivityWithHouseHold.class);
        startActivity(theIntent);
        rsbyMainActivity.finish();
        rsbyMainActivity.leftTransition();
    }

    private void saveFamily(){
        houseHoldItem.setHhStatus(householdStatus.getStatusCode());
        for(RSBYItem item : houseHoldMemberList){
            item.setHhStatus(householdStatus.getStatusCode());
            if(householdStatus.getStatusCode()!=null && !householdStatus.getStatusCode().equalsIgnoreCase(AppConstant.HOUSEHOLD_FOUND)){
                item.setMemStatus(AppConstant.NO_INFO_AVAIL);
            }
            item.setAadhaarVerifiedBy(loginDetail.getAadhaarNumber());
            item.setLockedSave(AppConstant.SAVE+"");
            SeccDatabase.updateRsbyMember(item,context);
        }
        houseHoldItem.setLockedSave(AppConstant.SAVE+"");
        SeccDatabase.updateRSBYHouseHold(houseHoldItem,context);
        Intent theIntent=new Intent(context,SearchActivityWithHouseHold.class);
        startActivity(theIntent);
        rsbyMainActivity.finish();
        rsbyMainActivity.leftTransition();
    }


    private  void alertForConsent(Context context) {

        final AlertDialog internetDiaolg = new AlertDialog.Builder(context).create();
        LayoutInflater factory = LayoutInflater.from(context);
        View alertView = factory.inflate(R.layout.rsbyconsent_layout, null);
        internetDiaolg.setView(alertView);
        internetDiaolg.show();
        final CheckBox consent = (CheckBox) alertView.findViewById(R.id.consent);
        Button tryAgainBT = (Button) alertView.findViewById(R.id.tryAgainBT);
        Button cancelBT = (Button) alertView.findViewById(R.id.cancelBT);
        final TextView errorTV = (TextView) alertView.findViewById(R.id.errorTV);

        tryAgainBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(consent.isChecked()){
                   errorTV.setVisibility(View.GONE);
                   askPinToLock();
               }else{
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


}
