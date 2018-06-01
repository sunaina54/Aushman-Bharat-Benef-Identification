package com.nhpm.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.nhpm.BaseActivity;
import com.nhpm.R;

public class ChooseHeadActivity extends BaseActivity {

    private RecyclerView familyList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_head);
    }
    private void setupScreen(){

    }
}
