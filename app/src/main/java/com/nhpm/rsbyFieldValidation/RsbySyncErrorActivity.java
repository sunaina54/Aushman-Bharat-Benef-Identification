package com.nhpm.rsbyFieldValidation;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.customComponent.utility.ProjectPrefrence;
import com.nhpm.activity.BaseActivity;
import com.nhpm.LocalDataBase.dto.SeccDatabase;
import com.nhpm.Models.response.rsbyMembers.RSBYItem;
import com.nhpm.Models.response.rsbyMembers.RsbyHouseholdItem;
import com.nhpm.Models.response.seccMembers.SelectedMemberItem;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;
import com.nhpm.rsbyFieldValidation.fragment.RsbyMemberErrorFragment;

import java.util.ArrayList;

import pl.polidea.view.ZoomView;

public class RsbySyncErrorActivity extends BaseActivity {
    private RsbyMemberErrorFragment fragment;
    private FragmentManager fragMgr;
    private FragmentTransaction fragTransect;
    private ArrayList<RSBYItem> memberList;
    private SelectedMemberItem selectedMemberItem;
    private Context context;
    private RSBYItem seccMemberItem;
    private RsbyHouseholdItem houseHoldItem;
    private  ArrayList<RSBYItem> errorList;
    private final String TAG="Error Member Activity";
    private TextView headerTV;
    private RelativeLayout backLayout;
    private ImageView backIV;
    private LinearLayout mZoomLinearLayout;
    private ZoomView zoomView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dummy_layout_for_zooming);
        setupScreen();
    }

    private  void setupScreen(){
        context=this;
        mZoomLinearLayout = (LinearLayout) findViewById(R.id.mZoomLinearLayout);
        View v = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.activity_rsby_sync_error, null, false);
        v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));

        backLayout=(RelativeLayout)v.findViewById(R.id.backLayout);
        headerTV=(TextView)v.findViewById(R.id.centertext);
        backIV=(ImageView)v.findViewById(R.id.back);

        zoomView = new ZoomView(this);
        zoomView.addView(v);
        mZoomLinearLayout.addView(zoomView);
        fragMgr=getSupportFragmentManager();
        selectedMemberItem= SelectedMemberItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.SELECTED_ITEM_FOR_VERIFICATION,context));
        if(selectedMemberItem !=null && selectedMemberItem.getRsbyHouseholdItem()!=null){
            houseHoldItem=selectedMemberItem.getRsbyHouseholdItem();
            //  Log.d(TAG,"hhd no : "+houseHoldItem.getHhdNo());
        }
        headerTV.setText("");
        getErroredMember();
        /*if(errorList!=null && errorList.size()>0){
            String address="";
            SeccMemberItem item=errorList.get(0);
            if(item.getAddressline1()!=null){
                address=address+""+item.getAddressline1();
            }
            if(item.getAddressline2()!=null){
                address=address+","+item.getAddressline2();
            }
            if(item.getAddressline3()!=null){
                address=address+","+item.getAddressline3();
            }
            if(item.getAddressline4()!=null){
                address=address+","+item.getAddressline4();
            }
            headerTV.setText(address);
        }
*/
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

    private void getErroredMember(){
        errorList= SeccDatabase.getRsbyMemberList(houseHoldItem.getUrnId(),context);
       /* for(int i=0;i<errorList.size();i++){
            SeccMemberItem item=errorList.get(i);
            ErrorItem errorItem=SeccDatabase.getMemberError(context,item.getNhpsMemId());
            item.setErrorItem(errorItem);
            errorList.set(i,item);
        }*/

        loadErrorFragment();
    }

    private void loadErrorFragment(){
        fragTransect=fragMgr.beginTransaction();
        if(fragment!=null){
            fragTransect.detach(fragment);
            fragment=null;
        }
        fragment= RsbyMemberErrorFragment.newInstance("","");
        fragment.setMemberList(errorList);
        fragTransect.replace(R.id.errorFragContainer,fragment);
        fragTransect.commitAllowingStateLoss();

    }
}
