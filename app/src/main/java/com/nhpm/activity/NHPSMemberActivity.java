package com.nhpm.activity;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nhpm.R;
import com.nhpm.Utility.AppConstant;
import com.nhpm.Utility.AppUtility;
import com.nhpm.fragments.CompleteVerifiedFragment;
import com.nhpm.fragments.NonValidatedMamberFragment;

import pl.polidea.view.ZoomView;

public class NHPSMemberActivity extends BaseActivity {

    private Button validatedBT,nonValidatedBT;
    private int YELLOW,WHITE_SHINE;
    private NonValidatedMamberFragment nonValidatedFragment;
    private CompleteVerifiedFragment validatedMemberFragment;
    private Fragment fragment;
    private FragmentManager fragMgr;
    private int searchTab;
    private Context context;
    private TextView headerTV;
    private LinearLayout mZoomLinearLayout;
    private ZoomView zoomView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dummy_layout_for_zooming);
        setupScreen();
    }

    private void setupScreen(){
        context=this;
        mZoomLinearLayout = (LinearLayout) findViewById(R.id.mZoomLinearLayout);
        View v = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.activity_nhps_member, null, false);
        v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
showNotification(v);
        YELLOW= AppUtility.getColor(context, R.color.yellow);
        WHITE_SHINE=AppUtility.getColor(context, R.color.white_shine);
        fragMgr=getSupportFragmentManager();
        searchTab=getIntent().getIntExtra(AppConstant.MEMBER_TAB, 0);
        validatedBT=(Button)v.findViewById(R.id.validatedMemberBT);
        nonValidatedBT=(Button)v.findViewById(R.id.nonValidatedMemberBT);
        headerTV=(TextView)v.findViewById(R.id.centertext);

        zoomView = new ZoomView(this);
        zoomView.addView(v);
        mZoomLinearLayout.addView(zoomView);
        headerTV.setText(context.getResources().getString(R.string.validat_nonvalidate));
        if(searchTab==2){
           openNonValidatedSeccMember();
        }else if(searchTab==3){

        }else if(searchTab== 1){
            openValidatedMember();
        }else{

        }

        validatedBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openValidatedMember();
            }
        });
        nonValidatedBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNonValidatedSeccMember();
            }
        });
    }
    private void openNonValidatedSeccMember(){
        validatedBT.setTextColor(WHITE_SHINE);
        nonValidatedBT.setTextColor(YELLOW);
       // nonValidatedMemberBT.setTextColor(WHITE_SHINE);
        FragmentTransaction transaction=fragMgr.beginTransaction();
        if(nonValidatedFragment!=null){
            transaction.detach(nonValidatedFragment);
            nonValidatedFragment=null;
            fragment=null;
        }
        nonValidatedFragment=new NonValidatedMamberFragment();
        fragment=nonValidatedFragment;
        transaction.replace(R.id.fragContainer, fragment);
        transaction.commitAllowingStateLoss();
    }

    private void openValidatedMember(){
        nonValidatedBT.setTextColor(WHITE_SHINE);
        validatedBT.setTextColor(YELLOW);
        FragmentTransaction transaction=fragMgr.beginTransaction();
        if(validatedMemberFragment!=null){
            transaction.detach(validatedMemberFragment);
            validatedMemberFragment=null;
            fragment=null;
        }
        validatedMemberFragment=new CompleteVerifiedFragment();
        fragment=validatedMemberFragment;
        transaction.replace(R.id.fragContainer, fragment);
        transaction.commitAllowingStateLoss();
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

}
