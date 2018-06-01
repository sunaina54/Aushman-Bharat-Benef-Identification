package com.nhpm.rsbyFieldValidation;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.customComponent.utility.ProjectPrefrence;
import com.nhpm.activity.BaseActivity;
import com.nhpm.LocalDataBase.dto.SeccDatabase;
import com.nhpm.Models.response.rsbyMembers.RsbyHouseholdItem;
import com.nhpm.Models.response.verifier.VerifierLocationItem;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;
import com.nhpm.Utility.AppUtility;
import com.nhpm.activity.SearchActivityWithHouseHold;
import com.nhpm.backgroundService.SyncService;
import com.nhpm.rsbyFieldValidation.fragment.SyncRsbyHouseholdDashoardfragment;

import java.util.ArrayList;

import pl.polidea.view.ZoomView;

public class RsbySyncHouseholdActivity extends BaseActivity {
    private SyncRsbyHouseholdDashoardfragment fragment;
    private Context context;
    private LinearLayout mZoomLinearLayout;
    private ZoomView zoomView;
    private ImageView backIV;
    private RelativeLayout backLayout;
    private VerifierLocationItem downloadedLocation;
    private FragmentManager fragMgr;
    private FragmentTransaction fragTransect;
    private ArrayList<RsbyHouseholdItem> totalHouseHold;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dummy_layout_for_zooming);
        setUpScreenNew();
    }


    private void setUpScreenNew(){
        context=this;
        mZoomLinearLayout = (LinearLayout) findViewById(R.id.mZoomLinearLayout);
        View v = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.activity_rsby_sync_household, null, false);
        v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        zoomView = new ZoomView(this);
        zoomView.addView(v);
        mZoomLinearLayout.addView(zoomView);
        backIV=(ImageView) v.findViewById(R.id.back);
        backLayout=(RelativeLayout)v.findViewById(R.id.backLayout);
        downloadedLocation= VerifierLocationItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF
                , AppConstant.SELECTED_BLOCK,context));
        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        backLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backIV.performClick();
            }
        });
        openSyncHouseholdFragment();
    }

    private void openSyncHouseholdFragment(){
        fragMgr=getSupportFragmentManager();
        fragTransect=fragMgr.beginTransaction();
        if(fragment!=null){
            fragTransect.detach(fragment);
            fragment=null;
        }
        fragment=new SyncRsbyHouseholdDashoardfragment();
        fragTransect.replace(R.id.fragContainer,fragment);
        fragTransect.commitAllowingStateLoss();
    }
    public BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        public static final String PROCESS_RESPONSE = "responseSucces";

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent!=null) {
                String response=intent.getStringExtra(SyncService.RESPONSE_SUCCESS);
                if(response!=null && response.equalsIgnoreCase("x")) {
                  //  AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Response : "+response);
                    openSyncHouseholdFragment();

                }else if(response!=null && response.equalsIgnoreCase(SyncService.SYNC_COMPLETE)){
               //     AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Response : "+response);
                    findAllHousehold();
                    openSyncHouseholdFragment();
                    unRegisterBroadCastReciver();
                    dismissDialog();
                    if(findErrorHousehold()!=null && findErrorHousehold().size()>0) {
                        //   AppUtility.alertWithOk(context, "Syncing done" + "\n" + "Total syncing error found : " + findErrorHousehold().size());
                        alertWithError(context,"Total sync error count : "+findErrorHousehold().size());
                    }else{
                        //  xc
                        alertWithNoError(context,"Sync Completed");
                    }
                }else if(response!=null && response.equalsIgnoreCase(AppConstant.cancelSyncMsg)){
                    AppUtility.alertWithOk(context,context.getString(R.string.sync_cancel));
                    openSyncHouseholdFragment();
                    unRegisterBroadCastReciver();
                    //    dismissDialog();


                }
            }
        }
    };

    public  void alertWithNoError(Context mContext, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Alert");
        builder.setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                        ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.DASHBOARD_TAB_STATUS,3+"",context);
                        ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                                AppConstant.HOUSEHOLD_TAB_STATUS,4+"",context);
                        Intent theIntent = new Intent(context,SearchActivityWithHouseHold.class);
                        startActivity(theIntent);
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public  void alertWithError(Context mContext, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Alert");
        builder.setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,"","4",context);
                        AppUtility.redirection = 10;
                        Intent theIntent = new Intent(context,RsbySyncHouseholdActivity.class);
                        startActivity(theIntent);
                        //do things
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void showProgressDialog(){
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setIndeterminateDrawable(context.getResources().getDrawable(R.mipmap.nhps_logo));
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel Syncing", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SyncService.shouldContinue = false;
                AppUtility.alertWithOk(context,context.getString(R.string.sync_cancel));
                openSyncHouseholdFragment();
                unRegisterBroadCastReciver();
                //  dialog.dismiss();
            }
        });
        mProgressDialog.show();

    }

    private ArrayList<RsbyHouseholdItem> findErrorHousehold(){
        ArrayList<RsbyHouseholdItem> list=new ArrayList<>();
        // ArrayList<HouseHoldItem> readyToSyncList=findReadyToSyncHousehold();

        for(RsbyHouseholdItem item : totalHouseHold) {
            if(item.getError_code()!=null){
                list.add(item);
            }
           /* ErrorItem errorItem = SeccDatabase.getMemberErrorByHhdNo(context, item.getHhdNo());
            if (errorItem != null){
                AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Error List" + errorItem.getNhpsMemId());
            }
            if(errorItem!=null && errorItem.getHhdNo().equalsIgnoreCase(item.getHhdNo())){

            }*/
        }
        return list;
    }

    public void findAllHousehold(){
        totalHouseHold = SeccDatabase.getAllRsbyHouseHoldList(context);

    }

    private void unRegisterBroadCastReciver(){
        try{
            unregisterReceiver(broadcastReceiver);
        }catch (Exception ex){

        }
    }

    public void dismissDialog(){
        if(mProgressDialog!=null){
            if(mProgressDialog.isShowing()){
                mProgressDialog.dismiss();
            }
        }
    }

}
