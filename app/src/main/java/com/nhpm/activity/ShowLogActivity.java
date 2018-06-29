package com.nhpm.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nhpm.R;
import com.nhpm.Utility.AppUtility;

public class ShowLogActivity extends BaseActivity {
    private TextView actionTV,logTV;
    private Context context;
    private String action,payload;
    private ImageView back;
    private RelativeLayout backLayout;
    private ShowLogActivity activity;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_log);
        setupScreen();

    }

    private void setupScreen(){
        context=this;
        activity=this;
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
        action = getIntent().getStringExtra("action");
        payload = getIntent().getStringExtra("log");

        actionTV= (TextView) findViewById(R.id.actionTV);
        logTV= (TextView) findViewById(R.id.logTV);
        if(action!=null){
            actionTV.setText(action);
        }
        if(payload!=null){
            logTV.setText(payload);
        }
    }
}
