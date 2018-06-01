package com.nhpm.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nhpm.R;
import com.nhpm.fragments.BeneficiaryFamilySearchFragment;

/**
 * Created by SUNAINA on 22-05-2018.
 */

public class SearchDashboardActivity extends BaseActivity {

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransection;
    private Fragment fragment;
    private ImageView backIV;
    private TextView centerText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_dashboard);
        setupScreen();
    }
    private void setupScreen(){
        centerText = (TextView) findViewById(R.id.centertext);
        centerText.setText("Find Beneficiary by Name");
        backIV = (ImageView) findViewById(R.id.back);

        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        fragmentManager=getSupportFragmentManager();
        openOptionFragment();
    }


    private void openOptionFragment() {
        fragment = new OptionActivity();
        fragmentTransection = fragmentManager.beginTransaction();
        fragmentTransection.add(R.id.fragContainer, fragment);
        fragmentTransection.commitAllowingStateLoss();
    }


}
