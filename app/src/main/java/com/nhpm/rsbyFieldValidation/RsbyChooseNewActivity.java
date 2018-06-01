package com.nhpm.rsbyFieldValidation;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.customComponent.CustomAlert;
import com.customComponent.utility.ProjectPrefrence;
import com.nhpm.activity.BaseActivity;
import com.nhpm.LocalDataBase.dto.SeccDatabase;
import com.nhpm.Models.response.RelationItem;
import com.nhpm.Models.response.rsbyMembers.RSBYItem;
import com.nhpm.Models.response.rsbyMembers.RsbyHouseholdItem;
import com.nhpm.Models.response.seccMembers.SelectedMemberItem;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;
import com.nhpm.Utility.AppUtility;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

import pl.polidea.view.ZoomView;

public class RsbyChooseNewActivity extends BaseActivity {

    private RecyclerView chooseForNewHeadList;
    private ArrayList<RelationItem> relationList;
    private ArrayList<String> relationArray;
    private Context context;
    private SelectedMemberItem selectedMemberItem;
    private RsbyHouseholdItem houseHoldItem;
    private String TAG = "ChooseNewHeadActivity";
    private ImageView backIV;
    private TextView headerTV, centertext;
    private ArrayList<RSBYItem> houseHoldMemberList;
    private Button updateBT;
    private LinearLayout mZoomLinearLayout;
    private ZoomView zoomView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dummy_layout_for_zooming);
        setupScreen();
    }

    private void setupScreen() {
        context = this;
        mZoomLinearLayout = (LinearLayout) findViewById(R.id.mZoomLinearLayout);
        View v = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.activity_rsby_choose_new, null, false);
        v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
        centertext = (TextView) v.findViewById(R.id.centertext);

        selectedMemberItem = SelectedMemberItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.SELECTED_ITEM_FOR_VERIFICATION, context));
        if (selectedMemberItem.getRsbyHouseholdItem() != null) {
            houseHoldItem = selectedMemberItem.getRsbyHouseholdItem();
        }
        if (houseHoldItem.getUrnId() != null) {
            centertext.setText(houseHoldItem.getUrnId());
        }
        chooseForNewHeadList = (RecyclerView) v.findViewById(R.id.chooseForHeadList);
        chooseForNewHeadList.setHasFixedSize(true);
        chooseForNewHeadList.setLayoutManager(new LinearLayoutManager(this));
        chooseForNewHeadList.setItemAnimator(new DefaultItemAnimator());
        relationList = SeccDatabase.getRelationList(context);
        relationArray = new ArrayList<>();
        backIV = (ImageView) v.findViewById(R.id.back);
        headerTV = (TextView) v.findViewById(R.id.centertext);
        headerTV.setText("Select New Head of family");
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
                    selectedMemberItem.setRsbyHouseholdItem(houseHoldItem);
                    selectedMemberItem.setRsbyRelationUpdatedList(houseHoldMemberList);
                    ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                            AppConstant.SELECTED_ITEM_FOR_VERIFICATION, selectedMemberItem.serialize(), context);
                    // SeccDatabase.updateHouseHold(houseHoldItem, context);
                /*    Intent theINtent = new Intent(context, RsbyMainActivity.class);
                    startActivity(theINtent);*/
                    finish();
                } else {
                    CustomAlert.alertWithOk(context, "No member found to choose new head.");
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

    private void findHouseholdMember(RsbyHouseholdItem houseHoldItem) {
        /*ArrayList<SeccMemberItem> seccMemberList=SharedPrefrenceData.getSeccMemberList(houseHoldItem.getHhdNo(),context);
       Log.d(TAG, "Household list size : " + seccMemberList.size());*/
        ArrayList<RSBYItem> seccFamilyList = SeccDatabase.getRsbyMemberList(houseHoldItem.getUrnId(), context);
        houseHoldMemberList = new ArrayList<>();
        for (RSBYItem item : seccFamilyList) {
            if (item.getRsbyMemId().equalsIgnoreCase(houseHoldItem.getRsbyMemId())) {
                // houseHoldMemberList.add(0,item);
            } else if (item.getName() != null && !item.getName().equalsIgnoreCase("")) {
                houseHoldMemberList.add(item);
            }

        }
        // Log.d(TAG, "Household list size filtered1111111111111111 : " + houseHoldMemberList.size());
        TreeSet<RSBYItem> seccTreeSet = new TreeSet<RSBYItem>(new Comparator<RSBYItem>() {

            public int compare(RSBYItem o1, RSBYItem o2) {
                // return 0 if objects are equal in terms of your properties
                if (o1.getRsbyMemId().equalsIgnoreCase(o2.getRsbyMemId())) {
                    return 0;
                }
                return 1;
            }
        });

        seccTreeSet.addAll(houseHoldMemberList);
        houseHoldMemberList = new ArrayList<>();
        houseHoldMemberList.addAll(seccTreeSet);
        SelectedMemberAdapter adapter = new SelectedMemberAdapter(context, houseHoldMemberList);
        chooseForNewHeadList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        //noOfMemberTV.setText("Total Member- " + houseHoldMemberList.size());
        //houseNoTV.setText("Household# " + houseHoldItem.getAhlslnohhd());
        String address = "";
        /*if(houseHoldMemberList.size()>0) {
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
        private ArrayList<RSBYItem> dataSet;
        // Context context;
        private TextView text;

        //private Activity context;
        public class MyViewHolder extends RecyclerView.ViewHolder {
            RelativeLayout itemlay;
            TextView nameTV, nameLocalTV, houseNoTV, fatherNameTV, fatherNameLocalTV,
                    genderTV, addressTV, relationTV, newHeadStatusTV, fatherName, dobTV;
            LinearLayout familyHeadLayout;
            Button chooseRelationBT;

            public MyViewHolder(View itemView) {
                super(itemView);
                this.nameTV = (TextView) itemView.findViewById(R.id.nameTV);
                dobTV = (TextView) itemView.findViewById(R.id.dobTV);
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
                fatherName = (TextView) itemView.findViewById(R.id.fatherName);


            }
        }

        public void addAll(List<RSBYItem> list) {

            dataSet.addAll(list);
            notifyDataSetChanged();
        }

        public SelectedMemberAdapter(Context context, ArrayList<RSBYItem> data) {
            this.dataSet = data;
            ;
            this.text = text;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.rsby_choosenewhead_item, parent, false);
            //view.setOnClickListener(MainActivity.myOnClickListener);

            MyViewHolder myViewHolder = new MyViewHolder(view);
            return myViewHolder;
        }


        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

            RSBYItem item = dataSet.get(listPosition);
            if (item.getName() != null)
                holder.nameTV.setText(item.getName());
         /*   if(item.getGender()!=null) {
                if (item.getGender().equalsIgnoreCase("1"))
                    holder.genderTV.setText(item.get);

            }*/
            if (item.getGender() != null && !item.getGender().equalsIgnoreCase("")) {
                if (item.getGender().equalsIgnoreCase("1")) {
                    holder.genderTV.setText("Male");
                } else if (item.getGender().equalsIgnoreCase("2")) {
                    holder.genderTV.setText("Female");
                } else {
                    holder.genderTV.setText("Other");
                }
            }

            holder.dobTV.setText(AppUtility.convertRsbyDate(item.getDob()));
            //  holder.idd.setText(dataSet.get(listPosition).getId());
            if (item.getMemid() != null) {
                holder.fatherNameTV.setText(item.getMemid());
            }
            /*
            String address="";
            if(item.getAddressline1()!=null){
                address=address+item.getAddressline1()+",";
            }
            if(item.getAddressline2()!=null){
                address=address+item.getAddressline2()+",";
            }
            if(item.getAddressline3()!=null){
                address=address+item.getAddressline2()+",";
            }

            holder.addressTV.setText(address);
            Log.d(TAG,"NHPS Relation : "+item.getNhpsRelationName());*/
            if (item.getGender() != null) {
                if (item.getGender().equalsIgnoreCase("1")) {
                    holder.genderTV.setText("Male");
                } else {
                    holder.genderTV.setText("Female");
                }
            }
            if (item.getName() == null) {
                holder.chooseRelationBT.setVisibility(View.GONE);
            }
            if (item.getName() != null && item.getName().equalsIgnoreCase("")) {
                holder.chooseRelationBT.setVisibility(View.GONE);
            }
            holder.newHeadStatusTV.setVisibility(View.GONE);
            holder.chooseRelationBT.setVisibility(View.VISIBLE);
            holder.relationTV.setText(item.getRelcode());
            if (item.getNhpsRelationCode() != null) {
                // holder.relationTV.setText(item.getNhpsRelationName());
                holder.newHeadStatusTV.setVisibility(View.VISIBLE);
                holder.chooseRelationBT.setVisibility(View.GONE);
            }

            holder.chooseRelationBT.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // popupFroChooseRelation(dataSet.get(getPosition()));
                    RSBYItem item = dataSet.get(listPosition);
                    // houseHoldMemberList=SeccDatabase.getSeccMemberList(houseHoldItem.getHhdNo(),context);
                      /*  for(SeccMemberItem item1 : houseHoldMemberList){
                            if(item.getAhlTin().equalsIgnoreCase(item1.getAhlTin())){
                                item1.setNhpsRelationCode("01");
                                item1.setNhpsRelationName("Self");
                                SeccDatabase.updateSeccMember(item1,context);
                            }else{
                                item1.setNhpsRelationCode(null);
                                item1.setNhpsRelationName(null);
                                SeccDatabase.updateSeccMember(item1,context);                            }
                        }*/

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



}
