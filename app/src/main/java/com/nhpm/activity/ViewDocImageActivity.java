package com.nhpm.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.customComponent.utility.ProjectPrefrence;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;
import com.nhpm.Utility.AppUtility;

/**
 * Created by SUNAINA on 30-07-2018.
 */

public class ViewDocImageActivity extends BaseActivity {
    private Context context;
    private ViewDocImageActivity activity;
    private ImageView docIV,backIV;
    private String docImage="",screenName="";
    private TextView headerTV;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        activity = this;
        setContentView(R.layout.activity_view_doc_image);
        setupScreen();
    }

    private void setupScreen(){
        headerTV = (TextView) findViewById(R.id.centertext);
        headerTV.setText("View Document Image");
        docIV= (ImageView) findViewById(R.id.docIV);
        screenName=getIntent().getStringExtra("ScreenName");
        if(screenName!=null && !screenName.equalsIgnoreCase("") && screenName.equalsIgnoreCase("ViewFamilyDetailsFragment")){
            docImage = ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,"DocImageFamily",context);

        }

        if(screenName!=null && !screenName.equalsIgnoreCase("") && screenName.equalsIgnoreCase("ViewPersonalDetailsFragment")) {

            docImage = ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, "DocImage", context);
        }
        docIV.setImageBitmap(null);
        if(docImage!=null && !docImage.equalsIgnoreCase("")){
            docIV.setImageBitmap(AppUtility.
                    convertStringToBitmap(docImage));
            ProjectPrefrence.removeSharedPrefrenceData(AppConstant.PROJECT_PREF,"DocImage",context);
            ProjectPrefrence.removeSharedPrefrenceData(AppConstant.PROJECT_PREF,"DocImageFamily",context);

        }

        backIV = (ImageView) findViewById(R.id.back);
        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
             //   ProjectPrefrence.removeSharedPrefrenceData(AppConstant.PROJECT_PREF,"DocImage",personalDetailItem.getIdPhoto(),context);

                rightTransition();
            }
        });


    }

}
