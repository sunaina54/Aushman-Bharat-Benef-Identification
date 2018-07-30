package com.nhpm.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.nhpm.R;
import com.nhpm.Utility.AppUtility;

/**
 * Created by SUNAINA on 30-07-2018.
 */

public class ViewDocImageActivity extends BaseActivity {
    private Context context;
    private ViewDocImageActivity activity;
    private ImageView docIV,backIV;
    private String docImage="";
    private TextView headerTV;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        activity = this;
        setContentView(R.layout.activity_search_result);
        setupScreen();
    }

    private void setupScreen(){
        headerTV = (TextView) findViewById(R.id.centertext);
        headerTV.setText("View Document Image");
        docIV= (ImageView) findViewById(R.id.docIV);
        docImage = getIntent().getStringExtra("DocImage");
        docIV.setImageBitmap(null);
        if(docImage!=null && !docImage.equalsIgnoreCase("")){
            docIV.setImageBitmap(AppUtility.
                    convertStringToBitmap(docImage));
        }

        backIV = (ImageView) findViewById(R.id.back);
        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                rightTransition();
            }
        });


    }

}
