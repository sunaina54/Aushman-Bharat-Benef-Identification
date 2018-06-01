package com.nhpm.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.customComponent.CustomAlert;
import com.customComponent.Networking.CustomVolley;
import com.customComponent.Networking.VolleyTaskListener;
import com.customComponent.utility.ProjectPrefrence;
import com.nhpm.BaseActivity;
import com.nhpm.Models.request.PinRequestItem;
import com.nhpm.Models.response.verifier.UpdatePinResponse;
import com.nhpm.Models.response.verifier.VerifierLoginResponse;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;
import com.nhpm.Utility.AppUtility;

import pl.polidea.view.ZoomView;

public class ChangePinActivity extends BaseActivity {
    private ImageView settings;
    private TextView headerTV;
    private Button resetBT;
    private VerifierLoginResponse verifierLoginResp;
    private Context context;
    private EditText pinET,confirmPinET;
    private PinRequestItem request;
    private String TAG="Change Pin Activity";
    private ImageView backIV;
    private LinearLayout mZoomLinearLayout;
    private ZoomView zoomView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dummy_layout_for_zooming);
        setupScreen();
    }
    private void setupScreen(){
        context=this;
        mZoomLinearLayout = (LinearLayout) findViewById(R.id.mZoomLinearLayout);
        View v = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.activity_change_pin, null, false);
        v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
showNotification(v);

        verifierLoginResp= VerifierLoginResponse.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.VERIFIER_CONTENT, context));
        headerTV=(TextView)v.findViewById(R.id.centertext);
        settings=(ImageView)v.findViewById(R.id.settings);
        pinET=(EditText)v.findViewById(R.id.newPinNumberET);
        confirmPinET=(EditText)v.findViewById(R.id.confirmNumberET);
        backIV=(ImageView)v.findViewById(R.id.back);
        settings.setVisibility(View.GONE);
        headerTV.setText(context.getResources().getString(R.string.resetPin));
        resetBT=(Button)v.findViewById(R.id.resetPinBT);
        zoomView = new ZoomView(this);
        zoomView.addView(v);
        mZoomLinearLayout.addView(zoomView);
        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                rightTransition();
            }
        });
        resetBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pinEtr=pinET.getText().toString();
                String confirmPinStr=confirmPinET.getText().toString();
                if(pinEtr.equalsIgnoreCase("")){
                    CustomAlert.alertWithOk(context,context.getResources().getString(R.string.plzEnterPin));
                }else if(confirmPinStr.equalsIgnoreCase("")){
                    CustomAlert.alertWithOk(context,context.getResources().getString(R.string.plzEnterConfirmPin));
                }else if(pinEtr.length()<4) {
                    CustomAlert.alertWithOk(context,context.getResources().getString(R.string.enterPin));

                }else if(!pinEtr.equalsIgnoreCase(confirmPinStr)){
                    CustomAlert.alertWithOk(context,context.getResources().getString(R.string.pinDoesNotMAtchWithConfirm));
                }else{
                    request=new PinRequestItem();
                    request.setPin(pinEtr);
                    request.setAadharNo(verifierLoginResp.getAadhaarNumber());
                    request.setUserId(verifierLoginResp.getUserId());
                    if(isNetworkAvailable()){
                        resetPinRequest();
                    }else{
                        CustomAlert.alertWithOk(context,getResources().getString(R.string.internet_connection_msg));
                    }

                }


            }
        });
    }
    private void resetPinRequest(){
        VolleyTaskListener taskListener=new VolleyTaskListener() {
            @Override
            public void postExecute(String response) {
                UpdatePinResponse pinResp=UpdatePinResponse.create(response);
                if(pinResp.isStatus()){
                    verifierLoginResp.setPin(pinResp.getPin());
                    ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.VERIFIER_CONTENT, verifierLoginResp.serialize(), context);
                    Log.d(TAG, "Updated Pin : " + verifierLoginResp.getPin());
                    finish();
                    rightTransition();
                }else{
                    CustomAlert.alertWithOk(context,pinResp.getErrorMessage());
                }

            }

            @Override
            public void onError(VolleyError error) {
                CustomAlert.alertWithOk(context,error.getMessage());
            }
        };
        CustomVolley volley=new CustomVolley(taskListener,context.getResources().getString(R.string.please_wait),AppConstant.UPDATE_PIN,request.serialize(),AppConstant.AUTHORIZATION,AppConstant.AUTHORIZATIONVALUE,context);
        volley.execute();
    }

    public void showNotification(View v) {

        LinearLayout notificationLayout = (LinearLayout) v.findViewById(R.id.notificationLayout);
        WebView notificationWebview = (WebView) v.findViewById(R.id.notificationWebview);
        String prePairedMessage = AppUtility.getNotificationData(context);
        if (prePairedMessage != null) {
            notificationLayout.setVisibility(View.VISIBLE);
            notificationWebview.loadData(prePairedMessage, "text/html", "utf-8"); // Set focus to textview
        }
    }
}
