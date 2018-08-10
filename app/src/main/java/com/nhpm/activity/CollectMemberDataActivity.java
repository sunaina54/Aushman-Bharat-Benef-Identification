package com.nhpm.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.customComponent.utility.ProjectPrefrence;
import com.nhpm.Models.response.DocsListItem;
import com.nhpm.Models.response.MemberListModel;
import com.nhpm.Models.response.master.StateItem;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;
import com.nhpm.Utility.AppUtility;
import com.nhpm.fragments.FamilyRelationFragment;
import com.nhpm.fragments.PersonalDetailsFragment;

/**
 * Created by SUNAINA on 07-08-2018.
 */

public class CollectMemberDataActivity extends BaseActivity {
    private Context context;
    private CollectMemberDataActivity activity;
    private MemberListModel memberListModel;
    public DocsListItem benefItem;
    private StateItem selectedStateItem;
    private TextView headerTV;
    private ImageView back;
    public RelativeLayout backLayout;
    public LinearLayout printEcardLL,familyDetailsLL,personalDetailsLL,addFamilyRelationLL;
    private Fragment fragment;
    private FragmentTransaction fragmentTransection;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;
        activity=this;
        setContentView(R.layout.activity_collect_member_data_layout);
        setupScreen();
    }

    private void setupScreen(){
        selectedStateItem = StateItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SELECTED_STATE_SEARCH, context));
        benefItem=null;
        headerTV = (TextView) findViewById(R.id.centertext);
        headerTV.setText("Collect Data" +" by "+ AppUtility.searchTitleHeader+" (" + selectedStateItem.getStateName() + ")");
        memberListModel = MemberListModel.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, "Golden_Data", context));
        fragmentManager = getSupportFragmentManager();
        back = (ImageView) findViewById(R.id.back);
        backLayout = (RelativeLayout) findViewById(R.id.backLayout);
        AppUtility.navigateToHome(context,activity);
        addFamilyRelationLL = (LinearLayout) findViewById(R.id.addFamilyRelationLL);
        printEcardLL = (LinearLayout) findViewById(R.id.printEcardLL);
        familyDetailsLL = (LinearLayout) findViewById(R.id.familyDetailsLL);
        personalDetailsLL = (LinearLayout) findViewById(R.id.personalDetailsLL);
        backLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backLayout.performClick();
            }
        });
        openFamilyRelationDetailsFragment();

    }

    private void openFamilyRelationDetailsFragment() {
        fragment = new FamilyRelationFragment();
     /*   Bundle args = new Bundle();
        args.putString("Name", name);
        fragment.setArguments(args);*/
        fragmentTransection = fragmentManager.beginTransaction();
        fragmentTransection.add(R.id.fragContainer, fragment);
        fragmentTransection.commitAllowingStateLoss();
    }
}
