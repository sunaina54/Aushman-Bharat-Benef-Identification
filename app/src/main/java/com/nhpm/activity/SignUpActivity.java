package com.nhpm.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.customComponent.utility.ProjectPrefrence;
import com.nhpm.Models.request.RegisterItem;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;
import com.nhpm.fragments.SignUpWithNonAadhaar;

public class SignUpActivity extends BaseActivity {
    private FragmentManager fragMgr;
    private FragmentTransaction fragTransect;
    private Context context;
    private SignUpWithNonAadhaar nonAadhaarFrag;
    private RegisterItem regItem;
    private ImageView backIV;
    private TextView centertext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setupScreen();
    }
    private void setupScreen(){
        context=this;
        backIV = (ImageView)findViewById(R.id.back);
        centertext=(TextView)findViewById(R.id.centertext);

        regItem=RegisterItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_NAME,
                AppConstant.REGISTER_MODE,context));

        if(regItem.getRegisterMode().equalsIgnoreCase(AppConstant.NON_AADHAAR_SEARCH)) {
            centertext.setText("Register With Non-Aadhaar");
            nonAadhaarFrag = new SignUpWithNonAadhaar();
        }
        fragMgr=getSupportFragmentManager();
        fragTransect=fragMgr.beginTransaction();
        fragTransect.replace(R.id.fragment_container,nonAadhaarFrag);
        fragTransect.commitAllowingStateLoss();
        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent theIntent;
                /*if (response != null && response.getAadhaarNumber() != null) {*/
                theIntent = new Intent(context, LoginActivity.class);
              /*  } else {
                    theIntent = new Intent(context, NonAdharLoginActivity.class);
                }*/

                theIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                theIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(theIntent);
                rightTransition();
            }
        });
    }
}
