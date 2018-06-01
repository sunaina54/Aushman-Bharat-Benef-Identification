package com.nhpm.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.customComponent.CustomAlert;
import com.customComponent.utility.ProjectPrefrence;
import com.nhpm.LocalDataBase.dto.SeccDatabase;
import com.nhpm.Models.SerachOptionItem;
import com.nhpm.Models.response.master.ConfigurationItem;
import com.nhpm.Models.response.master.StateItem;
import com.nhpm.Models.response.verifier.VerifierLocationItem;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;
import com.nhpm.Utility.AppUtility;
import com.nhpm.Utility.Verhoeff;

import java.util.ArrayList;

/**
 * Created by Dell3 on 03-05-2018.
 */

public class OptionActivity extends Fragment {
    private Context context;
    private String zoomMode = "N";
    private String benefidentificationMode="",ekycMode="",demoMode="",aadharAuthModeModel="";
    private Button nameLocationBTN, mobileNumberBTN, householdBTN, proceedBT;
    private ImageView backIV;
    private TextView headerTV, noAadhaarTV;
    private LinearLayout noAadhaarOptionLayout;
    private Dialog dialog;
    private EditText aadhaarET;
    private boolean isVeroff;
    private RelativeLayout menuLayout;
    private ImageView settings;
    private TextView wrongAttempetCountValue, wrongAttempetCountText;
    private AlertDialog dialog1, deleteDialog, askForPinDailog, internetDiaolg, proceedDailog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_option_layout, container, false);
        setupScreen(view);

        return view;
    }

    private void setupScreen(View view) {
        context=getActivity();
        checkAppConfig(view);
        aadhaarET = (EditText) view.findViewById(R.id.aadhaarET);
        noAadhaarTV = (TextView) view.findViewById(R.id.noAadhaarTV);
        noAadhaarOptionLayout = (LinearLayout) view.findViewById(R.id.noAadhaarOptionLayout);

        nameLocationBTN = (Button) view.findViewById(R.id.nameLocationBTN);
        mobileNumberBTN = (Button) view.findViewById(R.id.mobileNumberBTN);
        householdBTN = (Button) view.findViewById(R.id.householdBTN);
        proceedBT = (Button) view.findViewById(R.id.proceedBT);
        proceedBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(aadhaarET.getText().toString().equalsIgnoreCase("")){
                    CustomAlert.alertWithOk(context, "Please enter 12-digit Aadhaar Number");
                    return;
                }
                if (!isVeroff) {
                    CustomAlert.alertWithOk(context, getResources().getString(R.string.invalid_login));
                    return;
                }

                if (aadharAuthModeModel != null && !aadharAuthModeModel.equalsIgnoreCase("")) {
                    if (aadharAuthModeModel.equalsIgnoreCase("D")) {
                     /*   Intent intent = new Intent(context, DemoAuthActivity.class);
                        SerachOptionItem item=new SerachOptionItem();
                        item.setAadhaarNo(aadhaarET.getText().toString());

                        item.setSearchType(AppConstant.AADHAAR_SEARCH);
                        item.setMode(AppConstant.DEMO);
                        ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_NAME,AppConstant.SEARCH_OPTION,item.serialize(),context);

                        startActivity(intent);*/
                        Intent intent =new Intent(context,PhoneNumberActivity.class);
                        intent.putExtra("PhoneActivity","Demo");
                        SerachOptionItem item=new SerachOptionItem();
                        item.setAadhaarNo(aadhaarET.getText().toString());

                        item.setSearchType(AppConstant.AADHAAR_SEARCH);
                        item.setMode(AppConstant.DEMO);
                        ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_NAME,AppConstant.SEARCH_OPTION,item.serialize(),context);

                        startActivity(intent);

                    } else if (aadharAuthModeModel.equalsIgnoreCase("E")) {
                        Intent intent = new Intent(context, EkycActivity.class);
                        //intent.putExtra("aadhaarNo",aadhaarET.getText().toString());
                        SerachOptionItem item=new SerachOptionItem();
                        item.setAadhaarNo(aadhaarET.getText().toString());
                        item.setSearchType(AppConstant.AADHAAR_SEARCH);
                        item.setMode(AppConstant.EKYC);
                        ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_NAME,AppConstant.SEARCH_OPTION,item.serialize(),context);

                        startActivity(intent);
                    } else {
                        dialog = new Dialog(context);
                        dialog.setContentView(R.layout.aadhar_cature_popup);
                        Button ekycBTN, demoBTN, noAadhaarBTN;

                        ekycBTN = (Button) dialog.findViewById(R.id.ekycBTN);
                        demoBTN = (Button) dialog.findViewById(R.id.demoBTN);
                        noAadhaarBTN = (Button) dialog.findViewById(R.id.noAadhaarBTN);
                /*if(ekycMode.equalsIgnoreCase("")){
                    ekycBTN.setVisibility(View.GONE);
                }*/
                        ekycBTN.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(context, EkycActivity.class);
                                //intent.putExtra("aadhaarNo",aadhaarET.getText().toString());
                                SerachOptionItem item=new SerachOptionItem();
                                item.setAadhaarNo(aadhaarET.getText().toString());
                                item.setSearchType(AppConstant.AADHAAR_SEARCH);
                                item.setMode(AppConstant.EKYC);
                                ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_NAME,AppConstant.SEARCH_OPTION,item.serialize(),context);

                                startActivity(intent);
                                dialog.dismiss();
                            }
                        });

                        demoBTN.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                        Intent intent =new Intent(context,PhoneNumberActivity.class);
                        intent.putExtra("PhoneActivity","Demo");
                        SerachOptionItem item=new SerachOptionItem();
                        item.setAadhaarNo(aadhaarET.getText().toString());

                        item.setSearchType(AppConstant.AADHAAR_SEARCH);
                        item.setMode(AppConstant.DEMO);
                        ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_NAME,AppConstant.SEARCH_OPTION,item.serialize(),context);

                        startActivity(intent);
                                dialog.dismiss();
                               /* Intent intent = new Intent(context, DemoAuthActivity.class);
                                SerachOptionItem item=new SerachOptionItem();
                                item.setAadhaarNo(aadhaarET.getText().toString());

                                item.setSearchType(AppConstant.AADHAAR_SEARCH);
                                item.setMode(AppConstant.DEMO);
                                ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_NAME,AppConstant.SEARCH_OPTION,item.serialize(),context);

                                startActivity(intent);
                                dialog.dismiss();*/
                                //intent.putExtra("aadhaarNo",aadhaarET.getText().toString());
                            }
                        });
                        noAadhaarBTN.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CustomAlert.alertWithOk(context, "Under development");
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                    }
                }



            }
        });
        noAadhaarTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(context,PhoneNumberActivity.class);
                intent.putExtra("PhoneActivity","NoAadhaar");
                startActivity(intent);
             /*   Intent intent =new Intent(context,GovermentIDCaptureActivity.class);
                startActivity(intent);*/
               // CustomAlert.alertWithOk(context, "Under development");
               // noAadhaarOptionLayout.setVisibility(View.VISIBLE);
            }
        });
        nameLocationBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(context);
                dialog.setContentView(R.layout.aadhar_cature_popup);
                dialog.setTitle("Select Options");
                Button ekycBTN, demoBTN, noAadhaarBTN;
                ekycBTN = (Button) dialog.findViewById(R.id.ekycBTN);
                demoBTN = (Button) dialog.findViewById(R.id.demoBTN);
                noAadhaarBTN = (Button) dialog.findViewById(R.id.noAadhaarBTN);
                ekycBTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(context, EkycActivity.class);
                        startActivity(intent);
                        dialog.dismiss();
                    }
                });

                demoBTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       /* Intent intent =new Intent(context,PhoneNumberActivity.class);
                        intent.putExtra("PhoneActivity","Demo");
                        startActivity(intent);
                        dialog.dismiss();*/
                        Intent intent = new Intent(context, DemoAuthActivity.class);
                        startActivity(intent);
                        dialog.dismiss();
                    }
                });

                noAadhaarBTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CustomAlert.alertWithOk(context, "Under development");
                        dialog.dismiss();
                    }
                });
                dialog.show();

            }


        });
        mobileNumberBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomAlert.alertWithOk(context, "Under development");
            }
        });

        householdBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomAlert.alertWithOk(context, "Under development");
            }
        });


        aadhaarET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isVeroff = Verhoeff.validateVerhoeff(s.toString());
                aadhaarET.setTextColor(Color.BLACK);
                if (s.toString().length() > 11) {
                    if (!Verhoeff.validateVerhoeff(s.toString())) {
                        aadhaarET.setTextColor(Color.RED);
                    } else {
                        aadhaarET.setTextColor(Color.GREEN);
                        AppUtility.softKeyBoard(getActivity(), 0);
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }



    private String checkAppConfig(View rootView) {
        LinearLayout mainLayout=(LinearLayout)rootView.findViewById(R.id.parentLayout);
        LinearLayout aadhaarLayout=(LinearLayout)rootView.findViewById(R.id.adhaarLayout);
        RelativeLayout nonAadhaarLayout=(RelativeLayout)rootView.findViewById(R.id.nonAadhaarLayout);
        StateItem selectedStateItem = StateItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SELECTED_STATE, context));
        if (selectedStateItem != null && selectedStateItem.getStateCode() != null) {
            ArrayList<ConfigurationItem> configList = SeccDatabase.findConfiguration(selectedStateItem.getStateCode(), context);
            if (configList != null) {
                for (ConfigurationItem item1 : configList) {

                    if (item1.getConfigId().equalsIgnoreCase(AppConstant.APPLICATION_ZOOM)) {
                        zoomMode = item1.getStatus();
                    }
                    if(item1.getConfigId().equalsIgnoreCase(AppConstant.VALIDATION_MODE_CONFIG)){
                        benefidentificationMode=item1.getStatus();
                    }

                    if(item1.getConfigId().equalsIgnoreCase(AppConstant.AADHAR_AUTH)){
                        aadharAuthModeModel=item1.getStatus();
                    }



                    if(item1.getConfigId().equalsIgnoreCase(AppConstant.EKYC_SOURCE_CONFIG)){
                        ekycMode=item1.getStatus();
                    }

                    if(item1.getConfigId().equalsIgnoreCase(AppConstant.DEMOGRAPHIC_SOURCE_CONFIG)){
                        demoMode=item1.getStatus();
                    }

                }
            }
        }
        aadhaarLayout.setVisibility(View.GONE);
        nonAadhaarLayout.setVisibility(View.GONE);

        if (benefidentificationMode.equalsIgnoreCase("b")) {
            //appConfigWithValidationViaBoth();
            aadhaarLayout.setVisibility(View.VISIBLE);
            nonAadhaarLayout.setVisibility(View.VISIBLE);
        } else if (benefidentificationMode.equalsIgnoreCase("a")) {
            //appConfigWithValidationViaAadharOnly();
            aadhaarLayout.setVisibility(View.VISIBLE);

        } else if (benefidentificationMode.equalsIgnoreCase("g")) {
           // appConfigWithValidationViaGov();
            nonAadhaarLayout.setVisibility(View.VISIBLE);

        }
        return null;
    }

}
