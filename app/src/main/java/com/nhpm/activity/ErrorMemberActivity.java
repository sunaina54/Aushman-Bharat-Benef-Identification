package com.nhpm.activity;

import android.app.AlertDialog;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.customComponent.CustomAlert;
import com.customComponent.utility.ProjectPrefrence;
import com.nhpm.BaseActivity;
import com.nhpm.LocalDataBase.dto.SeccDatabase;
import com.nhpm.Models.response.master.ConfigurationItem;
import com.nhpm.Models.response.master.StateItem;
import com.nhpm.Models.response.seccMembers.HouseHoldItem;
import com.nhpm.Models.response.seccMembers.SeccMemberItem;
import com.nhpm.Models.response.seccMembers.SelectedMemberItem;
import com.nhpm.Models.response.verifier.VerifierLoginResponse;
import com.nhpm.PrintCard.PrintCardMainActivity;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;
import com.nhpm.Utility.AppUtility;
import com.nhpm.fragments.MemberErrorFragment;

import java.util.ArrayList;

import pl.polidea.view.ZoomView;

/*import com.nhpm.PrintCard.PrintCardMainActivity;*/

public class ErrorMemberActivity extends BaseActivity implements ComponentCallbacks2 {
    private MemberErrorFragment fragment;
    private FragmentManager fragMgr;
    private FragmentTransaction fragTransect;
    private ArrayList<SeccMemberItem> memberList;
    private SelectedMemberItem selectedMemberItem;
    private Context context;
    private Button printPreview;
    private String printStatus = "Y";
    private SeccMemberItem seccMemberItem;
    private HouseHoldItem houseHoldItem;
    private ArrayList<SeccMemberItem> errorList;
    private final String TAG = "Error Member Activity";
    private TextView headerTV;
    private RelativeLayout backLayout;
    private ImageView backIV;
    private LinearLayout mZoomLinearLayout;
    private ZoomView zoomView;
    private boolean memberFound = false;
    private boolean houseHoldEligible = true;
    private boolean pinLockIsShown = false;
    private String zoomMode = "N";
    private boolean isHofFound;
    private ArrayList<SeccMemberItem> seccMemberList;

    private int wrongPinCount = 0;
    private long wrongPinSavedTime;
    private long currentTime;
    private TextView wrongAttempetCountValue, wrongAttempetCountText;
    private long millisecond24 = 86400000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        checkAppConfig();

        if (zoomMode.equalsIgnoreCase("N")) {
            setContentView(R.layout.activity_error_member);
            setupScreenWithoutZoom();
        } else {
            setContentView(R.layout.dummy_layout_for_zooming);
            setupScreenWithZoom();
        }

    }

    private void setupScreenWithZoom() {
        mZoomLinearLayout = (LinearLayout) findViewById(R.id.mZoomLinearLayout);
        View v = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.activity_error_member, null, false);
        v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
        showNotification(v);
        backLayout = (RelativeLayout) v.findViewById(R.id.backLayout);
        headerTV = (TextView) v.findViewById(R.id.centertext);
        backIV = (ImageView) v.findViewById(R.id.back);
        printPreview = (Button) v.findViewById(R.id.printPreview);
        zoomView = new ZoomView(this);
        zoomView.addView(v);
        mZoomLinearLayout.addView(zoomView);
        fragMgr = getSupportFragmentManager();
        selectedMemberItem = SelectedMemberItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.SELECTED_ITEM_FOR_VERIFICATION, context));
        if (selectedMemberItem != null && selectedMemberItem.getHouseHoldItem() != null) {
            houseHoldItem = selectedMemberItem.getHouseHoldItem();
            //  Log.d(TAG,"hhd no : "+houseHoldItem.getHhdNo());
        }
        headerTV.setText("");
        getErroredMember();
        if (errorList != null && errorList.size() > 0) {
            String address = "";
            SeccMemberItem item = errorList.get(0);
            if (item.getAddressline1() != null) {
                address = address + "" + item.getAddressline1();
            }
            if (item.getAddressline2() != null) {
                address = address + "," + item.getAddressline2();
            }
            if (item.getAddressline3() != null) {
                address = address + "," + item.getAddressline3();
            }
            if (item.getAddressline4() != null) {
                address = address + "," + item.getAddressline4();
            }
            headerTV.setText(address);
        }

        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        backLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backIV.performClick();
            }
        });
        if (houseHoldItem != null && houseHoldItem.getSyncDt() != null && !houseHoldItem.getSyncDt().equalsIgnoreCase("")) {
            findAligableMember();
        }
    }

    private void setupScreenWithoutZoom() {

        showNotification();
        backLayout = (RelativeLayout) findViewById(R.id.backLayout);
        headerTV = (TextView) findViewById(R.id.centertext);
        backIV = (ImageView) findViewById(R.id.back);
        printPreview = (Button) findViewById(R.id.printPreview);

        fragMgr = getSupportFragmentManager();
        selectedMemberItem = SelectedMemberItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.SELECTED_ITEM_FOR_VERIFICATION, context));
        if (selectedMemberItem != null && selectedMemberItem.getHouseHoldItem() != null) {
            houseHoldItem = selectedMemberItem.getHouseHoldItem();
            //  Log.d(TAG,"hhd no : "+houseHoldItem.getHhdNo());
        }
        headerTV.setText("");
        getErroredMember();
        if (errorList != null && errorList.size() > 0) {
            String address = "";
            SeccMemberItem item = errorList.get(0);
            if (item.getAddressline1() != null) {
                address = address + "" + item.getAddressline1();
            }
            if (item.getAddressline2() != null) {
                address = address + "," + item.getAddressline2();
            }
            if (item.getAddressline3() != null) {
                address = address + "," + item.getAddressline3();
            }
            if (item.getAddressline4() != null) {
                address = address + "," + item.getAddressline4();
            }
            headerTV.setText(address);
        }

        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        backLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backIV.performClick();
            }
        });
        if (houseHoldItem != null && houseHoldItem.getSyncDt() != null && !houseHoldItem.getSyncDt().equalsIgnoreCase("")) {
            findAligableMember();
        }
    }

    private void getErroredMember() {
        if (houseHoldItem != null && houseHoldItem.getDataSource() != null && houseHoldItem.getDataSource().trim().equalsIgnoreCase(AppConstant.RSBY_SOURCE)) {
            errorList = SeccDatabase.getRsbyMemberListWithUrn(houseHoldItem.getRsbyUrnId(), context);

        } else {
            errorList = SeccDatabase.getSeccMemberList(houseHoldItem.getHhdNo(), context);
        }
       /* for(int i=0;i<errorList.size();i++){
            SeccMemberItem item=errorList.get(i);
            ErrorItem errorItem=SeccDatabase.getMemberError(context,item.getNhpsMemId());
            item.setErrorItem(errorItem);
            errorList.set(i,item);
        }*/

        loadErrorFragment();
    }

    private void loadErrorFragment() {
        fragTransect = fragMgr.beginTransaction();
        if (fragment != null) {
            fragTransect.detach(fragment);
            fragment = null;
        }
        fragment = MemberErrorFragment.newInstance("", "");
        fragment.setMemberList(errorList);
        fragTransect.replace(R.id.errorFragContainer, fragment);
        fragTransect.commitAllowingStateLoss();

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

    public void showNotification() {

        LinearLayout notificationLayout = (LinearLayout) findViewById(R.id.notificationLayout);
        WebView notificationWebview = (WebView) findViewById(R.id.notificationWebview);
        String prePairedMessage = AppUtility.getNotificationData(context);
        if (prePairedMessage != null) {
            notificationLayout.setVisibility(View.VISIBLE);
            notificationWebview.loadData(prePairedMessage, "text/html", "utf-8"); // Set focus to textview
        }
    }

    /*private void findAligableMember() {
        printPreview.setVisibility(View.VISIBLE);
        SeccMemberItem requiredItem = new SeccMemberItem();
        ArrayList<SeccMemberItem> seccMemberList;
        if (houseHoldItem != null && houseHoldItem.getDataSource().equalsIgnoreCase(AppConstant.RSBY_SOURCE)) {
            seccMemberList = SeccDatabase.getRsbyMemberListWithUrn(houseHoldItem.getRsbyUrnId(), context);
        } else {
            seccMemberList = SeccDatabase.getSeccMemberList(houseHoldItem.getHhdNo(), context);
        }
        for (SeccMemberItem item1 : seccMemberList) {

            if (houseHoldItem.getDataSource() != null && houseHoldItem.getDataSource().equalsIgnoreCase(AppConstant.RSBY_SOURCE)) {
               *//* if() {

                }else*//*
                if (item1.getNhpsRelationCode() != null &&
                        item1.getNhpsRelationCode().trim().equalsIgnoreCase(AppConstant.NEW_HEAD_RELATION_CODE)) {

                    if (item1.getMemberPhoto1() != null && !item1.getMemberPhoto1().equalsIgnoreCase("")) {
                        //  if (item1.getFathername() != null && !item1.getFathername().equalsIgnoreCase("")) {
                        if (item1.getRsbyDob() != null && !item1.getRsbyDob().equalsIgnoreCase("")) {
                            if (item1.getRsbyGender() != null && !item1.getRsbyGender().equalsIgnoreCase("")) {
                                if (item1.getMemStatus() != null &&
                                        (item1.getMemStatus().equalsIgnoreCase(AppConstant.MEMBERFOUND_AND_PRESENT) ||
                                                item1.getMemStatus().equalsIgnoreCase(AppConstant.MEMBERFOUND_AND_PRESENT))) {
                                    // member contain all detail required for print
                                    requiredItem = item1;
                                    memberFound = true;
                                    houseHoldEligible = true;
                                    break;
                                } else {
                                    houseHoldEligible = false;

                                }
                            } else {
                                houseHoldEligible = false;
                                //   AppUtility.alertWithOk(context, "Member gender not available");
                            }
                        } else {
                            //   AppUtility.alertWithOk(context, "Member date of birth not available");
                            houseHoldEligible = false;
                        }
                      *//*  } else {
                            //    AppUtility.alertWithOk(context, "Member father name not available");
                            houseHoldEligible = false;
                        }*//*
                    } else {
                        //   AppUtility.alertWithOk(context, "Member photo not available");
                        houseHoldEligible = false;
                    }
                }

                if (item1.getRsbyName() != null && !item1.getRsbyName().equalsIgnoreCase("")) {
                    if (item1.getMemberPhoto1() != null && !item1.getMemberPhoto1().equalsIgnoreCase("")) {
                        //  if (item1.getFathername() != null && !item1.getFathername().equalsIgnoreCase("")) {
                        if (item1.getRsbyDob() != null && !item1.getRsbyDob().equalsIgnoreCase("")) {
                            if (item1.getRsbyGender() != null && !item1.getRsbyGender().equalsIgnoreCase("")) {
                                if (item1.getMemStatus() != null &&
                                        (item1.getMemStatus().equalsIgnoreCase(AppConstant.MEMBERFOUND_AND_PRESENT) ||
                                                item1.getMemStatus().equalsIgnoreCase(AppConstant.MEMBERFOUND_AND_PRESENT))) {
                                    // member contain all detail required for print
                                    requiredItem = item1;
                                    memberFound = true;
                                    houseHoldEligible = true;
                                    break;
                                } else {
                                    houseHoldEligible = false;

                                }
                            } else {
                                houseHoldEligible = false;
                                //   AppUtility.alertWithOk(context, "Member gender not available");
                            }
                        } else {
                            //   AppUtility.alertWithOk(context, "Member date of birth not available");
                            houseHoldEligible = false;
                        }
                      *//*  } else {
                            //    AppUtility.alertWithOk(context, "Member father name not available");
                            houseHoldEligible = false;
                        }*//*
                    } else {
                        //   AppUtility.alertWithOk(context, "Member photo not available");
                        houseHoldEligible = false;
                    }
                } else {
                    //   AppUtility.alertWithOk(context, "Member name not available");
                    houseHoldEligible = false;

                }
            } else {

                if (item1.getNhpsRelationCode() != null &&
                        item1.getNhpsRelationCode().trim().equalsIgnoreCase(AppConstant.NEW_HEAD_RELATION_CODE)) {

                    if (item1.getName() != null && !item1.getName().equalsIgnoreCase("")) {
                        if (item1.getMemberPhoto1() != null && !item1.getMemberPhoto1().equalsIgnoreCase("")) {
                            if (item1.getDob() != null && !item1.getDob().equalsIgnoreCase("")) {
                                if (item1.getGenderid() != null && !item1.getGenderid().equalsIgnoreCase("")) {
                                    if (item1.getMemStatus() != null &&
                                            (item1.getMemStatus().equalsIgnoreCase(AppConstant.MEMBERFOUND_AND_PRESENT) ||
                                                    item1.getMemStatus().equalsIgnoreCase(AppConstant.MEMBERFOUND_AND_PRESENT))) {

                                        // member contain all detail required for print
                                        requiredItem = item1;
                                        memberFound = true;
                                        houseHoldEligible = true;
                                        break;

                                    } else {
                                        houseHoldEligible = false;

                                    }

                                } else {
                                    houseHoldEligible = false;
                                    //   AppUtility.alertWithOk(context, "Member gender not available");
                                }
                            } else {
                                //   AppUtility.alertWithOk(context, "Member date of birth not available");
                                houseHoldEligible = false;
                            }
                      *//*  } else {
                            //    AppUtility.alertWithOk(context, "Member father name not available");
                            houseHoldEligible = false;
                        }*//*
                        } else {
                            //   AppUtility.alertWithOk(context, "Member photo not available");
                            houseHoldEligible = false;
                        }
                    } else {
                        //   AppUtility.alertWithOk(context, "Member name not available");
                        houseHoldEligible = false;


                    }
                    if (item1.getName() != null && !item1.getName().equalsIgnoreCase("")) {
                        if (item1.getMemberPhoto1() != null && !item1.getMemberPhoto1().equalsIgnoreCase("")) {
                            if (item1.getDob() != null && !item1.getDob().equalsIgnoreCase("")) {
                                if (item1.getGenderid() != null && !item1.getGenderid().equalsIgnoreCase("")) {
                                    if (item1.getMemStatus() != null &&
                                            (item1.getMemStatus().equalsIgnoreCase(AppConstant.MEMBERFOUND_AND_PRESENT) ||
                                                    item1.getMemStatus().equalsIgnoreCase(AppConstant.MEMBERFOUND_AND_PRESENT))) {

                                        // member contain all detail required for print
                                        requiredItem = item1;
                                        memberFound = true;
                                        houseHoldEligible = true;
                                        break;

                                    } else {
                                        houseHoldEligible = false;

                                    }

                                } else {
                                    houseHoldEligible = false;
                                    //   AppUtility.alertWithOk(context, "Member gender not available");
                                }
                            } else {
                                //   AppUtility.alertWithOk(context, "Member date of birth not available");
                                houseHoldEligible = false;
                            }
                      *//*  } else {
                            //    AppUtility.alertWithOk(context, "Member father name not available");
                            houseHoldEligible = false;
                        }*//*
                        } else {
                            //   AppUtility.alertWithOk(context, "Member photo not available");
                            houseHoldEligible = false;
                        }
                    } else {
                        //   AppUtility.alertWithOk(context, "Member name not available");
                        houseHoldEligible = false;

                    }

                }
            }
        }
        if (!houseHoldEligible) {
            printPreview.setBackground(context.getResources().getDrawable(R.drawable.button_background_red));
            printPreview.setTextColor(context.getResources().getColor(R.color.red));
            printPreview.setText(context.getResources().getString(R.string.householdNotEliblePrint));
            printPreview.setEnabled(false);
        } else {
            printPreview.setEnabled(true);
            final SeccMemberItem finalRequiredItem = requiredItem;
            final SeccMemberItem finalRequiredItem1 = requiredItem;

            printPreview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (printStatus.equalsIgnoreCase("Y")) {
                        if (finalRequiredItem != null) {
                            Intent theIntent = new Intent(context, PrintCardMainActivity.class);
*//*if(finalRequiredItem1!=null && finalRequiredItem1.getRelation()!=null && finalRequiredItem1.getRelation().equalsIgnoreCase("SELF"))*//*
                            if (finalRequiredItem1.getNhpsRelationCode() != null && finalRequiredItem1.getNhpsRelationCode().trim().equalsIgnoreCase(AppConstant.NEW_HEAD_RELATION_CODE)) {
                                theIntent.putExtra("NAMEONCARD", "HOF: ");
                            } else {
                                theIntent.putExtra("NAMEONCARD", "Family of: ");
                            }
                            theIntent.putExtra(AppConstant.sendingPrintData, finalRequiredItem1);
                            startActivity(theIntent);
                        }
                    } else {
                        AppUtility.alertWithOk(context, "Configration does not support Printing");
                    }
                }
            });
        }

    }
*/


    private void findAligableMember() {

        SeccMemberItem requiredItem = new SeccMemberItem();
        if (houseHoldItem != null && houseHoldItem.getDataSource().equalsIgnoreCase(AppConstant.RSBY_SOURCE)) {
            seccMemberList = SeccDatabase.getRsbyMemberListWithUrn(houseHoldItem.getRsbyUrnId(), context);
            printPreview.setVisibility(View.VISIBLE);
        } else {
            printPreview.setVisibility(View.VISIBLE);
            seccMemberList = SeccDatabase.getSeccMemberList(houseHoldItem.getHhdNo(), context);
        }
        if (houseHoldItem.getDataSource() != null && houseHoldItem.getDataSource().equalsIgnoreCase(AppConstant.RSBY_SOURCE)) {
            isHofFound = isHeadAvailableRsby(seccMemberList);
        } else {
            isHofFound = isHeadAvailableSecc(seccMemberList);
        }

        if (!isHofFound) {
            for (SeccMemberItem item1 : seccMemberList) {
                if (item1.getNhpsId() != null && !item1.getNhpsId().equalsIgnoreCase("")) {
                    if (houseHoldItem.getDataSource() != null && houseHoldItem.getDataSource().equalsIgnoreCase(AppConstant.RSBY_SOURCE)) {
                        if (item1.getRsbyName() != null && !item1.getRsbyName().equalsIgnoreCase("")) {
                            if (item1.getMemberPhoto1() != null && !item1.getMemberPhoto1().equalsIgnoreCase("")) {
                                if (item1.getRsbyDob() != null && !item1.getRsbyDob().equalsIgnoreCase("")) {
                                    if (item1.getRsbyGender() != null && !item1.getRsbyGender().equalsIgnoreCase("")) {
                                        if (item1.getMemStatus() != null &&
                                                (item1.getMemStatus().equalsIgnoreCase(AppConstant.MEMBERFOUND_AND_PRESENT) ||
                                                        item1.getMemStatus().equalsIgnoreCase(AppConstant.MEMBERFOUND_AND_PRESENT))) {

                                            requiredItem = item1;
                                            memberFound = true;
                                            houseHoldEligible = true;
                                            allowForPrint(requiredItem, houseHoldEligible);
                                            break;
                                        } else {
                                            houseHoldEligible = false;
                                        }
                                    } else {
                                        houseHoldEligible = false;
                                    }
                                } else {
                                    houseHoldEligible = false;
                                }
                            } else {
                                houseHoldEligible = false;
                            }
                        } else {
                            houseHoldEligible = false;

                        }
                    } else {

                        if (item1.getName() != null && !item1.getName().equalsIgnoreCase("")) {
                            if (item1.getMemberPhoto1() != null && !item1.getMemberPhoto1().equalsIgnoreCase("")) {
                                if (item1.getDob() != null && !item1.getDob().equalsIgnoreCase("")) {
                                    if (item1.getGenderid() != null && !item1.getGenderid().equalsIgnoreCase("")) {
                                        if (item1.getMemStatus() != null &&
                                                (item1.getMemStatus().equalsIgnoreCase(AppConstant.MEMBERFOUND_AND_PRESENT) ||
                                                        item1.getMemStatus().equalsIgnoreCase(AppConstant.MEMBERFOUND_AND_PRESENT))) {
                                            requiredItem = item1;
                                            memberFound = true;
                                            houseHoldEligible = true;
                                            allowForPrint(requiredItem, houseHoldEligible);
                                            break;
                                        } else {
                                            houseHoldEligible = false;
                                        }
                                    } else {
                                        houseHoldEligible = false;
                                    }
                                } else {
                                    houseHoldEligible = false;
                                }
                            } else {
                                houseHoldEligible = false;
                            }
                        } else {
                            houseHoldEligible = false;
                        }


                    }
                } else {
                    houseHoldEligible = false;
                }

                if (!houseHoldEligible) {
                    printPreview.setBackground(context.getResources().getDrawable(R.drawable.button_background_red));
                    printPreview.setTextColor(context.getResources().getColor(R.color.red));
                    printPreview.setText(context.getResources().getString(R.string.householdNotEliblePrint));
                    printPreview.setEnabled(false);
                }


            }
        }
    }

    private String checkAppConfig() {
        StateItem selectedStateItem = StateItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SELECTED_STATE, context));
        ArrayList<ConfigurationItem> configList = SeccDatabase.findConfiguration(selectedStateItem.getStateCode(), context);

        if (configList != null) {
            for (ConfigurationItem item1 : configList) {
                if (item1.getConfigId().equalsIgnoreCase(AppConstant.PRINT_CARD)) {
                    printStatus = item1.getStatus();
                }
                if (item1.getConfigId().equalsIgnoreCase(AppConstant.APPLICATION_ZOOM)) {
                    zoomMode = item1.getStatus();
                }
            }
        }

        return printStatus;
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (AppUtility.isAppIsInBackground(context)) {
            if (!pinLockIsShown) {
                askPinToLock();
            }
        }
    }

    private void askPinToLock() {
        pinLockIsShown = true;
        final AlertDialog askForPinDailog = new AlertDialog.Builder(context).create();
        LayoutInflater factory = LayoutInflater.from(context);
        View alertView = factory.inflate(R.layout.ask_pin_layout, null);
        askForPinDailog.setView(alertView);
        askForPinDailog.setCancelable(false);
        askForPinDailog.show();
        // Log.d(TAG,"delete status :"+deleteStatus);
        // dialog.setContentView(R.layout.opt_auth_layout);
        //    final TextView otpAuthMsg=(TextView)alertView.findViewById(R.id.otpAuthMsg);
        final VerifierLoginResponse verifierDetail = VerifierLoginResponse.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.VERIFIER_CONTENT, context));
        final EditText pinET = (EditText) alertView.findViewById(R.id.deletPinET);
        final TextView errorTV = (TextView) alertView.findViewById(R.id.invalidOtpTV);

        wrongAttempetCountText = (TextView) alertView.findViewById(R.id.wrongAttempetCountText);
        wrongAttempetCountValue = (TextView) alertView.findViewById(R.id.wrongAttempetCountValue);


        pinET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //   errorTV.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        Button proceedBT = (Button) alertView.findViewById(R.id.proceedBT);
        Button cancelBT = (Button) alertView.findViewById(R.id.cancelBT);
        proceedBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                currentTime = System.currentTimeMillis();
                try {

                    wrongPinSavedTime = Long.parseLong(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.WORNG_PIN_ENTERED_TIMESTAMP, context));
                } catch (Exception ex) {
                    wrongPinSavedTime = 0;
                }
                if (currentTime > (wrongPinSavedTime + millisecond24)) {


                    //  AppUtility.softKeyBoard(activity, 0);
                    String pin = pinET.getText().toString();
                    if (pin.toString().equalsIgnoreCase(verifierDetail.getPin())) {
                        askForPinDailog.dismiss();
                        pinLockIsShown = false;
                    } else if (pin.equalsIgnoreCase("")) {
                        // CustomAlert.alertWithOk(context,"Please enter valid pin");
                        errorTV.setVisibility(View.VISIBLE);
                        errorTV.setText("Enter pin");
                        pinET.setText("");
                        //  pinET.setHint("");
                    } else if (!pin.toString().equalsIgnoreCase(verifierDetail.getPin())) {

                        if (wrongPinCount >= 2) {
                            errorTV.setTextColor(context.getResources().getColor(R.color.red));
                            wrongAttempetCountValue.setTextColor(context.getResources().getColor(R.color.red));
                            wrongAttempetCountText.setTextColor(context.getResources().getColor(R.color.red));
                        }
                        wrongPinCount++;
                        wrongAttempetCountValue.setText((3 - wrongPinCount) + "");
                        if (wrongPinCount > 2) {
                            long time = System.currentTimeMillis();
                            ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.WORNG_PIN_ENTERED_TIMESTAMP, time + "", context);
                            CustomAlert.alertWithOk(context, context.getResources().getString(R.string.youHaveExceedPinLimit));
                        } else {
                            errorTV.setVisibility(View.VISIBLE);
                            errorTV.setText("Enter correct pin");
                            pinET.setText("");
//                        CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterValidPin));
//                        pinET.setText("");
                        }
//                    errorTV.setVisibility(View.VISIBLE);
//                    errorTV.setText("Enter correct pin");
//                    pinET.setText("");
                        // pinET.setHint("Enter 4-di");
                    }
                } else {

                    //alert  when pin login is diabled for 24 hrs
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.pinLoginDisabled));
                    errorTV.setText("Pin login disabled for 24 hrs.");
                    pinET.setText("");
                    return;
                }
            }
        });
        cancelBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pinLockIsShown = false;
                Intent intent_login = new Intent(context, LoginActivity.class);
                intent_login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent_login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent_login);
                finish();
            }
        });
    }

    @Override
    public void onConfigurationChanged(final Configuration newConfig) {

    }

    @Override
    public void onLowMemory() {

    }

    @Override
    public void onTrimMemory(final int level) {
        if (level == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {
            if (!pinLockIsShown) {
                askPinToLock();
            }
        }
    }

    private void allowForPrint(SeccMemberItem requiredItem, boolean ishouseHoldEligible) {
        if (!ishouseHoldEligible) {
            printPreview.setBackground(context.getResources().getDrawable(R.drawable.button_background_red));
            printPreview.setTextColor(context.getResources().getColor(R.color.red));
            printPreview.setText(context.getResources().getString(R.string.householdNotEliblePrint));
            printPreview.setEnabled(false);
        } else {
            printPreview.setEnabled(true);
            final SeccMemberItem finalRequiredItem = requiredItem;
            final SeccMemberItem finalRequiredItem1 = requiredItem;
            printPreview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (printStatus.equalsIgnoreCase("Y")) {
                        if (finalRequiredItem != null) {
                            Intent theIntent = new Intent(context, PrintCardMainActivity.class);
/*if(finalRequiredItem1!=null && finalRequiredItem1.getRelation()!=null && finalRequiredItem1.getRelation().equalsIgnoreCase("SELF"))*/
                            if (finalRequiredItem1.getNhpsRelationCode() != null && finalRequiredItem1.getNhpsRelationCode().trim().equalsIgnoreCase(AppConstant.NEW_HEAD_RELATION_CODE)) {
                                theIntent.putExtra("NAMEONCARD", "HOF: ");
                            } else {
                                theIntent.putExtra("NAMEONCARD", "Family of: ");
                            }
                            theIntent.putExtra(AppConstant.sendingPrintData, finalRequiredItem1);
                            startActivity(theIntent);
                        }
                    } else {
                        AppUtility.alertWithOk(context, "Configration does not support Printing");
                    }
                }
            });
        }
    }

    private boolean isHeadAvailableRsby(ArrayList<SeccMemberItem> seccMemList) {
        SeccMemberItem requiredMember = null;
        for (SeccMemberItem item1 : seccMemList) {
            if (item1.getNhpsId() != null && !item1.getNhpsId().equalsIgnoreCase("")) {
                if (item1.getMemberPhoto1() != null && !item1.getMemberPhoto1().equalsIgnoreCase("")) {
                    if (item1.getRsbyDob() != null && !item1.getRsbyDob().equalsIgnoreCase("")) {
                        if (item1.getRsbyGender() != null && !item1.getRsbyGender().equalsIgnoreCase("")) {
                            if (item1.getMemStatus() != null &&
                                    (item1.getMemStatus().equalsIgnoreCase(AppConstant.MEMBERFOUND_AND_PRESENT) ||
                                            item1.getMemStatus().equalsIgnoreCase(AppConstant.MEMBERFOUND_AND_PRESENT))) {
                                memberFound = true;
                                houseHoldEligible = true;
                                allowForPrint(item1, houseHoldEligible);
                                break;
                            } else {
                                houseHoldEligible = false;
                            }
                        } else {
                            houseHoldEligible = false;
                        }
                    } else {
                        houseHoldEligible = false;
                    }
                } else {
                    houseHoldEligible = false;
                }
            } else {
                houseHoldEligible = false;
            }
        }
        return houseHoldEligible;
    }


    private boolean isHeadAvailableSecc(ArrayList<SeccMemberItem> seccMemberList) {

        for (SeccMemberItem item1 : seccMemberList) {
            if (item1.getNhpsId() != null && !item1.getNhpsId().equalsIgnoreCase("")) {
                if (item1.getNhpsRelationCode() != null &&
                        item1.getNhpsRelationCode().trim().equalsIgnoreCase(AppConstant.NEW_HEAD_RELATION_CODE)) {
                    if (item1.getName() != null && !item1.getName().equalsIgnoreCase("")) {
                        if (item1.getMemberPhoto1() != null && !item1.getMemberPhoto1().equalsIgnoreCase("")) {
                            if (item1.getDob() != null && !item1.getDob().equalsIgnoreCase("")) {
                                if (item1.getGenderid() != null && !item1.getGenderid().equalsIgnoreCase("")) {
                                    if (item1.getMemStatus() != null &&
                                            (item1.getMemStatus().equalsIgnoreCase(AppConstant.MEMBERFOUND_AND_PRESENT) ||
                                                    item1.getMemStatus().equalsIgnoreCase(AppConstant.MEMBERFOUND_AND_PRESENT))) {

                                        memberFound = true;
                                        houseHoldEligible = true;
                                        allowForPrint(item1, houseHoldEligible);
                                        break;
                                    } else {
                                        houseHoldEligible = false;
                                    }
                                } else {
                                    houseHoldEligible = false;
                                }
                            } else {
                                houseHoldEligible = false;
                            }
                        } else {
                            houseHoldEligible = false;
                        }
                    } else {

                        houseHoldEligible = false;
                    }
                } else {
                    houseHoldEligible = false;
                }
            }
        }
        return houseHoldEligible;
    }
}
