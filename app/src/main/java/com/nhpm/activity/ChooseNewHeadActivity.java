package com.nhpm.activity;

import android.app.AlertDialog;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.customComponent.CustomAlert;
import com.customComponent.utility.ProjectPrefrence;
import com.nhpm.BaseActivity;
import com.nhpm.LocalDataBase.dto.SeccDatabase;
import com.nhpm.Models.response.RelationItem;
import com.nhpm.Models.response.master.ConfigurationItem;
import com.nhpm.Models.response.master.StateItem;
import com.nhpm.Models.response.seccMembers.HouseHoldItem;
import com.nhpm.Models.response.seccMembers.SeccMemberItem;
import com.nhpm.Models.response.seccMembers.SelectedMemberItem;
import com.nhpm.Models.response.verifier.VerifierLoginResponse;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;
import com.nhpm.Utility.AppUtility;

import java.util.ArrayList;
import java.util.List;

import pl.polidea.view.ZoomView;

public class ChooseNewHeadActivity extends BaseActivity implements ComponentCallbacks2 {

    private RecyclerView chooseForNewHeadList;
    private ArrayList<RelationItem> relationList;
    private ArrayList<String> relationArray;
    private Context context;
    private SelectedMemberItem selectedMemberItem;
    private HouseHoldItem houseHoldItem;
    private String TAG = "ChooseNewHeadActivity";
    private ImageView backIV;
    private TextView headerTV;
    private ArrayList<SeccMemberItem> houseHoldMemberList;
    private Button updateBT;
    private LinearLayout mZoomLinearLayout;
    private ZoomView zoomView;
    private boolean pinLockIsShown = false;
    private String zoomMode = "N";

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

        if (zoomMode.equalsIgnoreCase("N")) {
            setContentView(R.layout.activity_choose_new_head);
            setupScreenWithoutZoom();
        } else {
            setContentView(R.layout.dummy_layout_for_zooming);
            setupScreenWithZoom();
        }

    }

    private void setupScreenWithZoom() {

        mZoomLinearLayout = (LinearLayout) findViewById(R.id.mZoomLinearLayout);
        View v = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.activity_choose_new_head, null, false);
        v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
        showNotification(v);
        selectedMemberItem = SelectedMemberItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.SELECTED_ITEM_FOR_VERIFICATION, context));
        if (selectedMemberItem.getHouseHoldItem() != null) {
            houseHoldItem = selectedMemberItem.getHouseHoldItem();
        }

        chooseForNewHeadList = (RecyclerView) v.findViewById(R.id.chooseForHeadList);
        chooseForNewHeadList.setHasFixedSize(true);
        chooseForNewHeadList.setLayoutManager(new LinearLayoutManager(this));
        chooseForNewHeadList.setItemAnimator(new DefaultItemAnimator());
        relationList = SeccDatabase.getRelationList(context);
        relationArray = new ArrayList<>();
        backIV = (ImageView) v.findViewById(R.id.back);
        headerTV = (TextView) v.findViewById(R.id.centertext);
        headerTV.setText(context.getResources().getString(R.string.selectNewHead));
        updateBT = (Button) v.findViewById(R.id.chooseUpdateBT);
        zoomView = new ZoomView(this);
        zoomView.addView(v);
        mZoomLinearLayout.addView(zoomView);
        updateBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (houseHoldMemberList != null && houseHoldMemberList.size() > 0) {
                    /*for(SeccMemberItem item1 : houseHoldMemberList){
                            *//*if(item.getAhlTin().equalsIgnoreCase(item1.getAhlTin())){
                                item1.setNhpsRelationCode("01");
                                item1.setNhpsRelationName("Self");*//*
                        SeccDatabase.updateSeccMember(item1,context);
                           *//* }else{
                                item1.setNhpsRelationCode(null);
                                item1.setNhpsRelationName(null);
                                SeccDatabase.updateSeccMember(item1,context);  *//*
                    }*/
                    //   houseHoldItem.setLockSave(AppConstant.SAVE + "");
                    selectedMemberItem.setHouseHoldItem(houseHoldItem);
                    selectedMemberItem.setRelationUpdatedList(houseHoldMemberList);
                    ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                            AppConstant.SELECTED_ITEM_FOR_VERIFICATION, selectedMemberItem.serialize(), context);
                    // SeccDatabase.updateHouseHold(houseHoldItem, context);
                    finish();
                } else {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.noMemberFoundForNewHead));
                }
            }
        });
        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* for (SeccMemberItem item1 : houseHoldMemberList) {
                   *//* if(item.getAhlTin().equalsIgnoreCase(item1.getAhlTin())){
                        item1.setNhpsRelationCode("01");
                        item1.setNhpsRelationName("Self");
                        SeccDatabase.updateSeccMember(item1,context);
                    }else{*//*
                    item1.setNhpsRelationCode(null);
                    item1.setNhpsRelationName(null);
                    SeccDatabase.updateSeccMember(item1, context);
                }*/
                finish();
            }


        });
        //relationArray.add(0,"Select Relation");
        for (RelationItem item : relationList) {
            relationArray.add(item.getRelationName());
        }
        findHouseholdMember(houseHoldItem);
        if (houseHoldMemberList != null && houseHoldMemberList.size() > 0) {

        } else {
            updateBT.setVisibility(View.GONE);
        }

    }

    private void setupScreenWithoutZoom() {

        showNotification();
        selectedMemberItem = SelectedMemberItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.SELECTED_ITEM_FOR_VERIFICATION, context));
        if (selectedMemberItem.getHouseHoldItem() != null) {
            houseHoldItem = selectedMemberItem.getHouseHoldItem();
        }

        chooseForNewHeadList = (RecyclerView) findViewById(R.id.chooseForHeadList);
        chooseForNewHeadList.setHasFixedSize(true);
        chooseForNewHeadList.setLayoutManager(new LinearLayoutManager(this));
        chooseForNewHeadList.setItemAnimator(new DefaultItemAnimator());
        relationList = SeccDatabase.getRelationList(context);
        relationArray = new ArrayList<>();
        backIV = (ImageView) findViewById(R.id.back);
        headerTV = (TextView) findViewById(R.id.centertext);
        headerTV.setText(context.getResources().getString(R.string.selectNewHead));
        updateBT = (Button) findViewById(R.id.chooseUpdateBT);

        updateBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (houseHoldMemberList != null && houseHoldMemberList.size() > 0) {
                    /*for(SeccMemberItem item1 : houseHoldMemberList){
                            *//*if(item.getAhlTin().equalsIgnoreCase(item1.getAhlTin())){
                                item1.setNhpsRelationCode("01");
                                item1.setNhpsRelationName("Self");*//*
                        SeccDatabase.updateSeccMember(item1,context);
                           *//* }else{
                                item1.setNhpsRelationCode(null);
                                item1.setNhpsRelationName(null);
                                SeccDatabase.updateSeccMember(item1,context);  *//*
                    }*/
                    //   houseHoldItem.setLockSave(AppConstant.SAVE + "");
                    selectedMemberItem.setHouseHoldItem(houseHoldItem);
                    selectedMemberItem.setRelationUpdatedList(houseHoldMemberList);
                    ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                            AppConstant.SELECTED_ITEM_FOR_VERIFICATION, selectedMemberItem.serialize(), context);
                    // SeccDatabase.updateHouseHold(houseHoldItem, context);
                    finish();
                } else {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.noMemberFoundForNewHead));
                }
            }
        });
        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* for (SeccMemberItem item1 : houseHoldMemberList) {
                   *//* if(item.getAhlTin().equalsIgnoreCase(item1.getAhlTin())){
                        item1.setNhpsRelationCode("01");
                        item1.setNhpsRelationName("Self");
                        SeccDatabase.updateSeccMember(item1,context);
                    }else{*//*
                    item1.setNhpsRelationCode(null);
                    item1.setNhpsRelationName(null);
                    SeccDatabase.updateSeccMember(item1, context);
                }*/
                finish();
            }


        });
        //relationArray.add(0,"Select Relation");
        for (RelationItem item : relationList) {
            relationArray.add(item.getRelationName());
        }
        findHouseholdMember(houseHoldItem);
        if (houseHoldMemberList != null && houseHoldMemberList.size() > 0) {

        } else {
            updateBT.setVisibility(View.GONE);
        }

    }

    private void resetHead() {
        ArrayList<SeccMemberItem> seccFamilyList = SeccDatabase.getSeccMemberList(houseHoldItem.getHhdNo(), context);
        houseHoldMemberList = new ArrayList<>();
        for (SeccMemberItem item : seccFamilyList) {

        }
    }

    private void findHouseholdMember(HouseHoldItem houseHoldItem) {
        /*ArrayList<SeccMemberItem> seccMemberList=SharedPrefrenceData.getSeccMemberList(houseHoldItem.getHhdNo(),context);
       Log.d(TAG, "Household list size : " + seccMemberList.size());*/
        Log.d(TAG, "Household Id : " + houseHoldItem.getHhdNo());
        if (houseHoldItem != null && houseHoldItem.getDataSource() != null
                && houseHoldItem.getDataSource().trim().equalsIgnoreCase(AppConstant.RSBY_SOURCE)) {


            ArrayList<SeccMemberItem> seccFamilyList = SeccDatabase.getRsbyMemberListWithUrn(houseHoldItem.getRsbyUrnId(), context);
            houseHoldMemberList = new ArrayList<>();
            for (SeccMemberItem item : seccFamilyList) {
                if (item.getRsbyMemId().equalsIgnoreCase(houseHoldItem.getRsbyMemId())) {
                    // houseHoldMemberList.add(0,item);
                } else if (item.getRsbyName() != null && !item.getRsbyName().equalsIgnoreCase("")) {
                    houseHoldMemberList.add(item);
                }

            }
        } else {
            ArrayList<SeccMemberItem> seccFamilyList = SeccDatabase.getSeccMemberList(houseHoldItem.getHhdNo(), context);
            houseHoldMemberList = new ArrayList<>();
            for (SeccMemberItem item : seccFamilyList) {
                if (item.getNhpsMemId().equalsIgnoreCase(houseHoldItem.getNhpsMemId())) {
                    // houseHoldMemberList.add(0,item);
                } else if (item.getName() != null && !item.getName().equalsIgnoreCase("")) {
                    houseHoldMemberList.add(item);
                }

            }
        }
       /*// Log.d(TAG, "Household list size filtered1111111111111111 : " + houseHoldMemberList.size());
        TreeSet<SeccMemberItem> seccTreeSet= new TreeSet<SeccMemberItem>(new Comparator<SeccMemberItem>(){

            public int compare(SeccMemberItem o1, SeccMemberItem o2) {
                // return 0 if objects are equal in terms of your properties
                if (o1.getNhpsMemId().equalsIgnoreCase(o2.getNhpsMemId())) {
                    return 0;
                }
                return 1;
            }
        });

        seccTreeSet.addAll(houseHoldMemberList);
        houseHoldMemberList=new ArrayList<>();

        houseHoldMemberList.addAll(seccTreeSet);*/


//by Rajesh Kumar

        /*
        * Case if familylist is null
        * and Only one menber is in household
        * Restrict the data to lock the member
        * */
        if ((houseHoldMemberList.size() == 0)) {

            SeccMemberListActivity.isFlagNoNewHead = true;
        } else {
            SeccMemberListActivity.isFlagNoNewHead = false;

        }

        SelectedMemberAdapter adapter = new SelectedMemberAdapter(context, houseHoldMemberList);
        chooseForNewHeadList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        //noOfMemberTV.setText("Total Member- " + houseHoldMemberList.size());
        //houseNoTV.setText("Household# " + houseHoldItem.getAhlslnohhd());
        String address = "";
      /*  if(houseHoldMemberList.size()>0) {
            if (houseHoldMemberList != null && houseHoldMemberList.get(0).getAddressline1() != null) {
                address = address + houseHoldMemberList.get(0).getAddressline1() + ",";
            }
            if (houseHoldMemberList != null && houseHoldMemberList.get(0).getAddressline2() != null) {
                address = address + houseHoldMemberList.get(0).getAddressline2() + ",";
            }
            if (houseHoldMemberList != null && houseHoldMemberList.get(0).getAddressline3() != null) {
                address = address + houseHoldMemberList.get(0).getAddressline3() + ",";
            }
            if (houseHoldMemberList != null && houseHoldMemberList.get(0).getAddressline4() != null) {
                address = address + houseHoldMemberList.get(0).getAddressline4() + ",";
            }
            if (houseHoldMemberList != null && houseHoldMemberList.get(0).getAddressline5() != null) {
                address = address + houseHoldMemberList.get(0).getAddressline5() + ",";
            }
        }*/
        //  householdAddressTV.setText(address);

    }


    private class SelectedMemberAdapter extends RecyclerView.Adapter<SelectedMemberAdapter.MyViewHolder> {

        View view;
        AlertDialog dialog;
        private ArrayList<SeccMemberItem> dataSet;
        // Context context;
        private TextView text;

        //private Activity context;
        public class MyViewHolder extends RecyclerView.ViewHolder {
            RelativeLayout itemlay;
            TextView nameTV, nameLocalTV, houseNoTV, verfiedTV, fatherNameTV, fatherNameLocalTV,
                    motherNameTV, genderTV, verifiedStatTV, addressTV, relationTV, newHeadStatusTV;
            LinearLayout verifylayout, underVerification,
                    printLayout, verifiedNotSync, verifiedSynced, familyHeadLayout;
            Spinner relationSP;
            Button chooseRelationBT;

            public MyViewHolder(View itemView) {
                super(itemView);
                this.nameTV = (TextView) itemView.findViewById(R.id.nameTV);
                nameLocalTV = (TextView) itemView.findViewById(R.id.localNameTV);
                this.houseNoTV = (TextView) itemView.findViewById(R.id.houseNoTV);
                fatherNameTV = (TextView) itemView.findViewById(R.id.fatherNameTV);
                fatherNameLocalTV = (TextView) itemView.findViewById(R.id.localfatherNameTV);
                addressTV = (TextView) itemView.findViewById(R.id.addressTV);
                familyHeadLayout = (LinearLayout) itemView.findViewById(R.id.familyHeadLayout);
                genderTV = (TextView) itemView.findViewById(R.id.genderTV);
                relationTV = (TextView) itemView.findViewById(R.id.relationTV);
                newHeadStatusTV = (TextView) itemView.findViewById(R.id.newHeadStatusTV);
                chooseRelationBT = (Button) itemView.findViewById(R.id.chooseRelationBT);


            }
        }

        public void addAll(List<SeccMemberItem> list) {

            dataSet.addAll(list);
            notifyDataSetChanged();
        }

        public SelectedMemberAdapter(Context context, ArrayList<SeccMemberItem> data) {
            this.dataSet = data;
            ;
            this.text = text;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_choose_for_newhead, parent, false);
            //view.setOnClickListener(MainActivity.myOnClickListener);

            MyViewHolder myViewHolder = new MyViewHolder(view);
            return myViewHolder;
        }


        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

            SeccMemberItem item = dataSet.get(listPosition);

            if (item.getDataSource() != null && item.getDataSource() != null &&
                    item.getDataSource().trim().equalsIgnoreCase(AppConstant.RSBY_SOURCE)) {
                showRsbyDetail(item, holder);
            } else {
                showSeccDetail(item, holder);
            }
            holder.chooseRelationBT.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // popupFroChooseRelation(dataSet.get(getPosition()));
                    SeccMemberItem item = dataSet.get(listPosition);
                    if (item != null && item.getDataSource() != null && item.getDataSource().trim().equalsIgnoreCase(AppConstant.RSBY_SOURCE)) {
                        updateRsby(item);
                    } else {
                        updateSecc(item);
                    }

                    notifyDataSetChanged();
                        /*for(SeccMemberItem item1 : houseHoldMemberList){
                            if(item.getAhlTin().equalsIgnoreCase(item1.getAhlTin())){
                                item.setNhpsRelationCode("01");
                                item.setNhpsRelationName("Self");
                                //.updateSeccMember(item1,context);
                            }else{
                                item.setNhpsRelationCode(null);
                                item.setNhpsRelationName(null);
                                //SeccDatabase.updateSeccMember(item1,context);
                            }
                        }*/

                       /* item1.setNhpsRelationCode("01");
                        item1.setNhpsRelationName("Self");*/

                    //findHouseholdMember(houseHoldItem);
                    //notifyDataSetChanged();
                }
            });


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

    private void popupFroChooseRelation(final SeccMemberItem item) {
        final AlertDialog dialog = new AlertDialog.Builder(context).create();
        LayoutInflater factory = LayoutInflater.from(context);
        View alertView = factory.inflate(R.layout.choose_relation_popup, null);
        dialog.setView(alertView);
        final Spinner relationSP = (Spinner) alertView.findViewById(R.id.relationSP);
        Button updateBT = (Button) alertView.findViewById(R.id.relationUpdateBT);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.custom_drop_down, R.id.textView, relationArray);
        relationSP.setAdapter(adapter);
        updateBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = relationSP.getSelectedItemPosition();
                if (index == 0) {

                } else {
                    Log.d(TAG, "index : " + index);
                    item.setNhpsRelationCode(relationList.get(index).getRelationCode());
                    item.setNhpsRelationName(relationList.get(index).getRelationName());
                    Log.d(TAG, "Relation Name : " + item.getNhpsRelationName());
                    // SeccDatabase.updateSeccMember(item, context);
                    // Log.d(TAG, "NHPS Relation : " + SeccDatabase.getSeccMemberDetail(item.getAhlTin(), context).getBankAccNo());
                    // findHouseholdMember(houseHoldItem);
                    dialog.dismiss();

                }
            }
        });

        dialog.show();

    }

    private void updateRsby(SeccMemberItem item) {
        for (int i = 0; i < houseHoldMemberList.size(); i++) {
            if (item.getRsbyMemId().equalsIgnoreCase(houseHoldMemberList.get(i).getRsbyMemId())) {
                item.setNhpsRelationCode(AppConstant.NEW_HEAD_RELATION_CODE);
                item.setNhpsRelationName("New Head");
                houseHoldMemberList.set(i, item);
            } else {
                houseHoldMemberList.get(i).setNhpsRelationCode(null);
                houseHoldMemberList.get(i).setNhpsRelationName(null);
            }
        }
    }

    private void updateSecc(SeccMemberItem item) {
        for (int i = 0; i < houseHoldMemberList.size(); i++) {
            if (item.getNhpsMemId().equalsIgnoreCase(houseHoldMemberList.get(i).getNhpsMemId())) {
                item.setNhpsRelationCode(AppConstant.NEW_HEAD_RELATION_CODE);
                item.setNhpsRelationName("New Head");
                houseHoldMemberList.set(i, item);
            } else {
                houseHoldMemberList.get(i).setNhpsRelationCode(null);
                houseHoldMemberList.get(i).setNhpsRelationName(null);
            }
        }

    }

    private void showSeccDetail(SeccMemberItem item, SelectedMemberAdapter.MyViewHolder holder) {
        if (item.getName() != null)
            holder.nameTV.setText(item.getName());
        if (item.getNameSl() != null)
            holder.nameLocalTV.setText(Html.fromHtml(item.getNameSl()));
        //  holder.idd.setText(dataSet.get(listPosition).getId());
        holder.houseNoTV.setText(item.getAhlslnohhd());
        if (item.getFathername() != null)
            holder.fatherNameTV.setText(item.getFathername());
        if (item.getFathernameSl() != null)
            holder.fatherNameLocalTV.setText(Html.fromHtml(item.getFathernameSl()));
        if (item.getRelation() != null) {
            holder.relationTV.setText(item.getRelation());
        }
        String address = "";
        if (item.getAddressline1() != null) {
            address = address + item.getAddressline1() + ",";
        }
        if (item.getAddressline2() != null) {
            address = address + item.getAddressline2() + ",";
        }
        if (item.getAddressline3() != null) {
            address = address + item.getAddressline2() + ",";
        }
        holder.addressTV.setText(address);
        if (item.getGenderid().equalsIgnoreCase("1")) {
            holder.genderTV.setText(context.getResources().getString(R.string.genderMale));
        } else {
            holder.genderTV.setText(context.getResources().getString(R.string.genderFemale));
        }
        Log.d(TAG, "NHPS Relation : " + item.getNhpsRelationName());
        if (item.getName() == null) {
            holder.chooseRelationBT.setVisibility(View.GONE);
        }
        if (item.getName() != null && item.getName().equalsIgnoreCase("")) {
            holder.chooseRelationBT.setVisibility(View.GONE);
        }
        holder.newHeadStatusTV.setVisibility(View.GONE);
        holder.chooseRelationBT.setVisibility(View.VISIBLE);
        if (item.getNhpsRelationCode() != null) {
            // holder.relationTV.setText(item.getNhpsRelationName());
            holder.newHeadStatusTV.setVisibility(View.VISIBLE);
            holder.chooseRelationBT.setVisibility(View.GONE);
        }
    }

    private void showRsbyDetail(SeccMemberItem item, SelectedMemberAdapter.MyViewHolder holder) {
        if (item.getRsbyName() != null)
            holder.nameTV.setText(item.getRsbyName());
        if (item.getNameSl() != null)
            holder.nameLocalTV.setText(Html.fromHtml(item.getNameSl()));
        //  holder.idd.setText(dataSet.get(listPosition).getId());
        // holder.houseNoTV.setText(item.getAhlslnohhd());
        /*if(item.getFathername()!=null)
            holder.fatherNameTV.setText(item.getFathername());*/
      /*  if(item.getFathernameSl()!=null)
            holder.fatherNameLocalTV.setText(item.getFathernameSl());*/
        /*if(item.getRelation()!=null){
            holder.relationTV.setText(item.getRelation());
        }*/
       /* String address="";
        if(item.getAddressline1()!=null){
            address=address+item.getAddressline1()+",";
        }
        if(item.getAddressline2()!=null){
            address=address+item.getAddressline2()+",";
        }
        if(item.getAddressline3()!=null){
            address=address+item.getAddressline2()+",";
        }
        holder.addressTV.setText(address);*/
        if (item.getRsbyGender().equalsIgnoreCase(AppConstant.MALE_GENDER)) {
            holder.genderTV.setText(AppConstant.MALE);
        } else if (item.getRsbyGender().equalsIgnoreCase(AppConstant.FEMALE_GENDER)) {
            holder.genderTV.setText(AppConstant.FEMALE);
        } else {
            holder.genderTV.setText(AppConstant.OTHER_GENDER_NAME);
        }
        Log.d(TAG, "NHPS Relation : " + item.getNhpsRelationName());
        if (item.getRsbyName() == null) {
            holder.chooseRelationBT.setVisibility(View.GONE);
        }
        if (item.getRsbyName() != null && item.getRsbyName().equalsIgnoreCase("")) {
            holder.chooseRelationBT.setVisibility(View.GONE);
        }
        holder.newHeadStatusTV.setVisibility(View.GONE);
        holder.chooseRelationBT.setVisibility(View.VISIBLE);
        if (item.getNhpsRelationCode() != null) {
            // holder.relationTV.setText(item.getNhpsRelationName());
            holder.newHeadStatusTV.setVisibility(View.VISIBLE);
            holder.chooseRelationBT.setVisibility(View.GONE);
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

    private void checkAppConfig() {
        StateItem selectedStateItem = StateItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SELECTED_STATE, context));
        ArrayList<ConfigurationItem> configList = SeccDatabase.findConfiguration(selectedStateItem.getStateCode(), context);

        if (configList != null) {
            for (ConfigurationItem item1 : configList) {

                if (item1.getConfigId().equalsIgnoreCase(AppConstant.APPLICATION_ZOOM)) {
                    zoomMode = item1.getStatus();
                }


            }
        }

    }
}
