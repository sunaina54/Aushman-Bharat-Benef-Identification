package com.nhpm.activity;



import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.customComponent.utility.ProjectPrefrence;
import com.nhpm.Models.response.BeneficiaryListItem;
import com.nhpm.Models.response.DocsListItem;
import com.nhpm.Models.response.master.StateItem;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;
import com.nhpm.Utility.AppUtility;
import com.nhpm.fragments.BeneficiaryFamilySearchFragment;
import com.nhpm.fragments.FamilyDetailsFragment;
import com.nhpm.fragments.PersonalDetailsFragment;
import com.nhpm.fragments.PrintEcardFragment;

/**
 * Created by SUNAINA on 24-05-2018.
 */

public class CollectDataActivity extends BaseActivity {
    public LinearLayout printEcardLL,familyDetailsLL,personalDetailsLL;
    private Fragment fragment;
    private FragmentTransaction fragmentTransection;
    private FragmentManager fragmentManager;
    private TextView headerTV;
    private ImageView back;
    private RelativeLayout backLayout;
    private String name;
    public DocsListItem benefItem;
    private Context context;
    private CollectDataActivity activity;
    private StateItem selectedStateItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_data_layout);
        setupScreen();
    }

    private void setupScreen(){
        context=this;
        activity=this;
        fragmentManager = getSupportFragmentManager();
        headerTV = (TextView) findViewById(R.id.centertext);
        selectedStateItem = StateItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SELECTED_STATE, context));

        //headerTV.setText("Collect Data" +" ("+selectedStateItem.getStateName()+")");
        headerTV.setText("Collect Data" +" by "+ AppUtility.searchTitleHeader+" (" + selectedStateItem.getStateName() + ")");

        // AppUtility.navigateToHome(context,activity);
        name = getIntent().getStringExtra("Name");
        benefItem= DocsListItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_NAME,
                FamilyMembersListActivity.SELECTED_MEMBER,context));

        back = (ImageView) findViewById(R.id.back);
        backLayout = (RelativeLayout) findViewById(R.id.backLayout);
        AppUtility.navigateToHome(context,activity);
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
        printEcardLL = (LinearLayout) findViewById(R.id.printEcardLL);
        familyDetailsLL = (LinearLayout) findViewById(R.id.familyDetailsLL);
        personalDetailsLL = (LinearLayout) findViewById(R.id.personalDetailsLL);
        openPersonalDetailsFragment();

       /* printEcardLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPrintEcardFragment();
            }
        });

        familyDetailsLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFamilyDetailsFragment();
            }
        });

        personalDetailsLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPersonalDetailsFragment();
            }
        });*/
    }

    private void openPersonalDetailsFragment() {
        fragment = new PersonalDetailsFragment();
        Bundle args = new Bundle();
        args.putString("Name", name);
        fragment.setArguments(args);
        fragmentTransection = fragmentManager.beginTransaction();
        fragmentTransection.add(R.id.fragContainer, fragment);
        fragmentTransection.commitAllowingStateLoss();
    }

    private void openFamilyDetailsFragment() {
        fragment = new FamilyDetailsFragment();
        fragmentTransection = fragmentManager.beginTransaction();
        fragmentTransection.add(R.id.fragContainer, fragment);
        fragmentTransection.commitAllowingStateLoss();
    }

    private void openPrintEcardFragment() {
        fragment = new PrintEcardFragment();
        fragmentTransection = fragmentManager.beginTransaction();
        fragmentTransection.add(R.id.fragContainer, fragment);
        fragmentTransection.commitAllowingStateLoss();
    }
}
