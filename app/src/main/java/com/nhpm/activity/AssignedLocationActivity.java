package com.nhpm.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.customComponent.utility.ProjectPrefrence;
import com.nhpm.BaseActivity;
import com.nhpm.Models.response.verifier.VerifierLocationItem;
import com.nhpm.Models.response.verifier.VerifierLoginResponse;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;

import pl.polidea.view.ZoomView;

public class AssignedLocationActivity extends BaseActivity {

    private TextView stateNameTV, distNameTV,tehsilNameTV,villTownNameTV,areaTV,wardCodeTV,vtLableTV,ebTV;
    private VerifierLoginResponse verifierDetail;
    private Context context;
    private ImageView backIV;
    private RelativeLayout backLayout;
    private TextView headerTV;
    private VerifierLocationItem locationItem;
    private LinearLayout mZoomLinearLayout;
    private ZoomView zoomView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dummy_layout_for_zooming);
        setupdScreen();
    }
    private void setupdScreen(){
        context=this;
        mZoomLinearLayout = (LinearLayout) findViewById(R.id.mZoomLinearLayout);
        View v = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.activity_assigned_location, null, false);
        v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
        verifierDetail=VerifierLoginResponse.create(
                ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                        AppConstant.VERIFIER_CONTENT,context));
        locationItem=VerifierLocationItem.
                create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,AppConstant.SELECTED_BLOCK,context));

        backIV=(ImageView)v.findViewById(R.id.back);
        backLayout=(RelativeLayout)v.findViewById(R.id.backLayout);
        stateNameTV=(TextView)v.findViewById(R.id.stateNameTV);
        stateNameTV.setText(locationItem.getStateName());
        distNameTV=(TextView) v.findViewById(R.id.distNameTV);
        distNameTV.setText(locationItem.getDistrictName());
        tehsilNameTV=(TextView)v.findViewById(R.id.tehsilNameTV);
        tehsilNameTV.setText(locationItem.getTehsilName());
        villTownNameTV=(TextView)v.findViewById(R.id.villTownNameTV);
        villTownNameTV.setText(locationItem.getVtName());
        wardCodeTV=(TextView)v.findViewById(R.id.wardNameTV);
        wardCodeTV.setText(locationItem.getWardCode());
        vtLableTV=(TextView)v.findViewById(R.id.vtLableTV);
        areaTV=(TextView)v.findViewById(R.id.areaTV);
        ebTV=(TextView)v.findViewById(R.id.enumerationBlockTV);
        ebTV.setText(locationItem.getBlockCode());
        zoomView = new ZoomView(this);
        zoomView.addView(v);
        mZoomLinearLayout.addView(zoomView);
       /* if(verifierDetail.getLocationList().get(0).getRuralUrban().equalsIgnoreCase(AppConstant.RURAL)){
           // vtLableTV.setText("Village");
            areaTV.setText("Rural");
        }else{
            areaTV.setText("Urban");
            //vtLableTV.setText("Town");
        }*/
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
    }
}
