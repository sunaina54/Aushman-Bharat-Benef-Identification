package com.nhpm.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.customComponent.CustomAlert;
import com.nhpm.Models.FamilyMemberModel;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;
import com.nhpm.Utility.AppUtility;
import com.nhpm.fragments.FamilyDetailsFragment;

/**
 * Created by SUNAINA on 07-06-2018.
 */

public class FamilyMemberEntryActivity extends BaseActivity {
    private Context context;
    private FamilyMemberEntryActivity activity;
    private TextView headerTV;
    private ImageView backIV;
    private Button saveBT,cancelBT;
    private EditText familyMemberNameET;
    private FamilyMemberModel familymemberItem;
    private Intent theIntent;
    private String index;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        activity=this;
        setContentView(R.layout.activity_family_member_entry);
        setupScreen();
    }

    private void setupScreen() {
        //mProgressBar = (ProgressBar) findViewById(R.id.mProgressBar);
        headerTV = (TextView) findViewById(R.id.centertext);
        headerTV.setText("Add Member");
        backIV = (ImageView) findViewById(R.id.back);
        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                rightTransition();
            }
        });



        AppUtility.navigateToHome(context, activity);
        theIntent = getIntent();
        familymemberItem = (FamilyMemberModel) theIntent.getSerializableExtra(AppConstant.FAMILY_MEMBER_RESULT_CODE_NAME);
        index= theIntent.getStringExtra(FamilyDetailsFragment.INDEX);
        familyMemberNameET = (EditText) findViewById(R.id.familyMemberNameET);
        familyMemberNameET.requestFocus();
        AppUtility.softKeyBoard(activity, 1);
        saveBT = (Button) findViewById(R.id.saveBT);
        cancelBT = (Button) findViewById(R.id.cancelBT);
        if(index!=null){
            saveBT.setText("Update");
            familyMemberNameET.setText(familymemberItem.getName());


        }

        saveBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(familyMemberNameET.getText().toString().equalsIgnoreCase("")){
                    CustomAlert.alertWithOk(context,getResources().getString(R.string.please_enter_name));
                    return;
                }

                if(familymemberItem!=null){
                    familymemberItem.setName(familyMemberNameET.getText().toString());

                }else{
                    familymemberItem = new FamilyMemberModel();
                    familymemberItem.setName(familyMemberNameET.getText().toString());

                }

                Intent theIntent = new Intent();
                theIntent.putExtra(AppConstant.FAMILY_MEMBER_RESULT_CODE_NAME,familymemberItem);
                theIntent.putExtra(FamilyDetailsFragment.INDEX,index);
                setResult(Activity.RESULT_OK,theIntent);

                AppUtility.softKeyBoard(activity, 0);
                finish();

            }
        });
        cancelBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
