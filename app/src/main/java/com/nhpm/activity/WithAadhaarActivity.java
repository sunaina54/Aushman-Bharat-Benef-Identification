package com.nhpm.activity;

import android.app.AlertDialog;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.customComponent.CustomAlert;
import com.customComponent.utility.ProjectPrefrence;
import com.nhpm.LocalDataBase.dto.SeccDatabase;
import com.nhpm.Models.response.master.AadhaarStatusItem;
import com.nhpm.Models.response.master.ConfigurationItem;
import com.nhpm.Models.response.master.MemberStatusItem;
import com.nhpm.Models.response.master.StateItem;
import com.nhpm.Models.response.rsbyMembers.RSBYItem;
import com.nhpm.Models.response.seccMembers.HouseHoldItem;
import com.nhpm.Models.response.seccMembers.SeccMemberItem;
import com.nhpm.Models.response.seccMembers.SelectedMemberItem;
import com.nhpm.Models.response.verifier.VerifierLoginResponse;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;
import com.nhpm.Utility.AppUtility;

import java.util.ArrayList;

import pl.polidea.view.ZoomView;

public class WithAadhaarActivity extends BaseActivity implements ComponentCallbacks2 {
    private TextView aadhaarDetailTV, bankDetailTV, rsbyDetTV, mobileNoTV,
            healthSchemeNoTV, nomineeTV, govtIdDetTV, memberPhotoTV;
    private TextView photoStar;
    private TextView govtIdStar;
    private TextView aadhaarStar;
    private TextView mobileNoStar;
    private TextView nomineeDetStart;
    private RelativeLayout backLayout;
    private Context context;
    private ImageView settings;
    private TextView headerTV;
    private Spinner memberStatSP;
    private ArrayList<MemberStatusItem> memberStatusList;
    private RelativeLayout menuLayout;
    private int selectedAadhaarStatus;
    private RelativeLayout enrollmentLayout, bankLayout, rsbyLayout, mobileLayout, healthSchemelayout,
            nomineeLayout, editMemberDetlayout;
    private LinearLayout photoLayout, govtIdLayout, aadhaarLayout, aadhaarRadiogroupLayout, additionalSchemeLayout, nomineeCaptureStatusLinearLayout;
    private RelativeLayout aadhaarStatusLayout;
    private Object selectedMemberItem;
    private RelativeLayout aadhaarStatLayout;
    private Button updateNhpsBT;
    private ImageView backIV;
    private SeccMemberItem seccMemberItem;
    private HouseHoldItem houseHoldItem;
    private RSBYItem rsbyItem;
    private int memberType;
    private SelectedMemberItem selectedMemItem;
    private String validationMode = "g";
    private String additionalScheme = "Y";
    private String validationDataSource = "Y";
    private String photoCollection = "Y";
    private String nomineeCollection = "Y";
    private Button lockBT;
    private final String TAG = "With Aadhaar Activity";
    private ArrayList<AadhaarStatusItem> aadhaarStatusList;
    private Spinner aadhaarStatusSP;
    private RadioGroup aadhaarStatusRG;
    private RadioButton aadhaarAvailableRB, aadhaarNotPrepRB;
    private String aadhaarStatus;
    private RelativeLayout photoCaptureStatus, govtIdCaptureStatus, aadhaarCaptureStatus, mobileCaptureStatus,
            nomineeCaptureStatus, additionalCaptureStatus;
    private AlertDialog internetDiaolg;
    private LinearLayout mZoomLinearLayout;
    private ZoomView zoomView;
    private VerifierLoginResponse loginResponse;
    private boolean pinLockIsShown = false;
    private String zoomMode = "N";


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
            setContentView(R.layout.activity_nhpsmember);
            setupScreenWithOutZoom();
        } else {
            setContentView(R.layout.dummy_layout_for_zooming);
            setupScreenWithZoom();
        }

    }

    private void setupScreenWithZoom() {
        mZoomLinearLayout = (LinearLayout) findViewById(R.id.mZoomLinearLayout);
        View v = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.activity_nhpsmember, null, false);
        v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
        loginResponse = (VerifierLoginResponse.create(ProjectPrefrence.
                getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.VERIFIER_CONTENT, context)));
        showNotification(v);
        backLayout = (RelativeLayout) v.findViewById(R.id.backLayout);
        headerTV = (TextView) v.findViewById(R.id.centertext);
        selectedMemItem = SelectedMemberItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.SELECTED_ITEM_FOR_VERIFICATION, context));
        if (selectedMemItem != null && selectedMemItem.getHouseHoldItem() != null) {
            houseHoldItem = selectedMemItem.getHouseHoldItem();
        }
        if (selectedMemItem != null && selectedMemItem.getSeccMemberItem() != null) {
            seccMemberItem = selectedMemItem.getSeccMemberItem();
            if (houseHoldItem != null && houseHoldItem.getDataSource() != null
                    && houseHoldItem.getDataSource().trim().equalsIgnoreCase(AppConstant.RSBY_SOURCE)) {
                showRsbyDetail(seccMemberItem);

            } else {
                showSeccDetail(seccMemberItem);
            }
        }
        AppUtility.showLog(AppConstant.LOG_STATUS, TAG, " Secc Member Item : " + seccMemberItem);
        //   dashBoardNavBT = (TextView) v.findViewById(R.id.dashBoardNavBT);
        photoStar = (TextView) v.findViewById(R.id.capturePhotoStarTV);
        govtIdStar = (TextView) v.findViewById(R.id.govtIdStarTV);
        aadhaarStar = (TextView) v.findViewById(R.id.aadhaarStarTV);
        mobileNoStar = (TextView) v.findViewById(R.id.mobileNoStarTV);
        healthSchemeNoTV = (TextView) v.findViewById(R.id.healthSchemeNoTV);
        nomineeDetStart = (TextView) v.findViewById(R.id.nomineeDetailStarTV);
        photoCaptureStatus = (RelativeLayout) v.findViewById(R.id.photoCaptureStatus);
        govtIdCaptureStatus = (RelativeLayout) v.findViewById(R.id.govtidCaptureStatus);
        additionalSchemeLayout = (LinearLayout) v.findViewById(R.id.additionalSchemeLayout);
        aadhaarCaptureStatus = (RelativeLayout) v.findViewById(R.id.aadharCaptureStatus);
        mobileCaptureStatus = (RelativeLayout) v.findViewById(R.id.mobileCaptureStatus);
        nomineeCaptureStatus = (RelativeLayout) v.findViewById(R.id.nomineeCaptureStatus);
        additionalCaptureStatus = (RelativeLayout) v.findViewById(R.id.additionalCaptureStatus);
        aadhaarRadiogroupLayout = (LinearLayout) v.findViewById(R.id.aadhaarRadiogroupLayout);
        menuLayout = (RelativeLayout) v.findViewById(R.id.menuLayout);

        aadhaarStatusRG = (RadioGroup) v.findViewById(R.id.aadhaarStatusRG);
        aadhaarAvailableRB = (RadioButton) v.findViewById(R.id.aadhaarAvailRB);
        aadhaarNotPrepRB = (RadioButton) v.findViewById(R.id.aadhaarNotPrepRB);
        aadhaarStatusSP = (Spinner) v.findViewById(R.id.aadhaarStatusSP);
        aadhaarLayout = (LinearLayout) v.findViewById(R.id.aadhaarRelLayout);
        enrollmentLayout = (RelativeLayout) v.findViewById(R.id.enrollRelLayout);
        bankLayout = (RelativeLayout) v.findViewById(R.id.bankDetRelLayout);
        rsbyLayout = (RelativeLayout) v.findViewById(R.id.rsbyDetRelLayout);
        mobileLayout = (RelativeLayout) v.findViewById(R.id.mobileRelLayout);
        healthSchemelayout = (RelativeLayout) v.findViewById(R.id.healthSchemeRelLayout);
        nomineeLayout = (RelativeLayout) v.findViewById(R.id.nomineeRelLayout);
        nomineeCaptureStatusLinearLayout = (LinearLayout) v.findViewById(R.id.nomineeCaptureStatusLinearLayout);
        photoLayout = (LinearLayout) v.findViewById(R.id.memberPhotoLayout);
        govtIdLayout = (LinearLayout) v.findViewById(R.id.govtIdLayout);
        editMemberDetlayout = (RelativeLayout) v.findViewById(R.id.memberDetRelLayout);
        editMemberDetlayout.setVisibility(View.GONE);
        backIV = (ImageView) v.findViewById(R.id.back);
        updateNhpsBT = (Button) v.findViewById(R.id.updateNhpsBT);
        lockBT = (Button) v.findViewById(R.id.lockBT);
        settings = (ImageView) v.findViewById(R.id.settings);
        aadhaarStatLayout = (RelativeLayout) v.findViewById(R.id.aadhaarStatLayout);
        aadhaarStatLayout.findViewById(R.id.aadhVerifiedIV).setVisibility(View.VISIBLE);
        ImageView verified = (ImageView) aadhaarStatLayout.findViewById(R.id.aadhVerifiedIV);
        verified.setVisibility(View.GONE);
        ImageView rejected = (ImageView) aadhaarStatLayout.findViewById(R.id.aadhRejectedIV);
        rejected.setVisibility(View.GONE);
        ImageView pending = (ImageView) aadhaarStatLayout.findViewById(R.id.aadhPendingIV);
        pending.setVisibility(View.GONE);

        zoomView = new ZoomView(this);
        zoomView.addView(v);
        mZoomLinearLayout.addView(zoomView);


        if (seccMemberItem.getMemStatus() != null && seccMemberItem.getAadhaarAuth() != null && seccMemberItem.getAadhaarAuth().equalsIgnoreCase("Y")) {
            verified.setVisibility(View.VISIBLE);
        } else if (seccMemberItem.getMemStatus() != null && seccMemberItem.getAadhaarAuth() != null && seccMemberItem.getAadhaarAuth().equalsIgnoreCase("N")) {
            rejected.setVisibility(View.VISIBLE);
        } else if (seccMemberItem.getMemStatus() != null && seccMemberItem.getAadhaarAuth() != null && seccMemberItem.getAadhaarAuth().equalsIgnoreCase("P")) {
            pending.setVisibility(View.VISIBLE);
        }

        photoCaptureStatus.setVisibility(View.INVISIBLE);
        govtIdCaptureStatus.setVisibility(View.INVISIBLE);
        aadhaarCaptureStatus.setVisibility(View.INVISIBLE);
        mobileCaptureStatus.setVisibility(View.INVISIBLE);
        nomineeCaptureStatus.setVisibility(View.INVISIBLE);
        additionalCaptureStatus.setVisibility(View.INVISIBLE);
        setMobileStatus();
        setURNStatus();
        setPhotoCaptureStatus();
        // prepareAadhaarStatusSpinner();
        setAadhaarStatus();
        setNomineeStatus();


        showMendatoryField();
       /* aadhaarAvailableRB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){

                }

            }
        });*/
        dashboardDropdown(v);

        aadhaarLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent theIntent = new Intent(context, CaptureAadhaarActivity.class);*/
                Intent theIntent = new Intent(context, CaptureAadharDetailActivity.class);
                // theIntent.putExtra(AppConstant.MEMBER_TYPE,memberType);
                selectedMemItem.setSeccMemberItem(seccMemberItem);
                ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                        AppConstant.SELECTED_ITEM_FOR_VERIFICATION, selectedMemItem.serialize(), context);
                startActivity(theIntent);
                //  finish();
                leftTransition();
            }
        });
        govtIdLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent theIntent;
                //if(seccMemberItem.getIdType()==null){

                theIntent = new Intent(context, GovermentIdCapturingNavigation.class);
                //                   theIntent = new Intent(context, GovermentIDCaptureActivity.class);


                //theIntent.putExtra(AppConstant.NEVIGATE, AppConstant.WITH_OUT_AADHAAR);
                selectedMemItem.setSeccMemberItem(seccMemberItem);

                ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                        AppConstant.SELECTED_ITEM_FOR_VERIFICATION, selectedMemItem.serialize(), context);
                startActivity(theIntent);
                finish();
                leftTransition();
            }
        });
        enrollmentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  Intent theIntent = new Intent(context, EnrollmentActivity.class);
                startActivity(theIntent);
                finish();
                leftTransition();*/

            }
        });
        mobileLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent theIntent = new Intent(context, PhoneNumberActivity.class);
                selectedMemItem.setSeccMemberItem(seccMemberItem);
                ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                        AppConstant.SELECTED_ITEM_FOR_VERIFICATION, selectedMemItem.serialize(), context);
                startActivity(theIntent);
                finish();
                leftTransition();
            }
        });

       /* bankLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent theIntent=new Intent(context,BankDetailsActivity.class);
                *//*theIntent.putExtra(AppConstant.NEVIGATE,AppConstant.WITH_AADHAAR);
                theIntent.putExtra(AppConstant.MEMBER_TYPE,memberType);
*//*                startActivity(theIntent);
              //  finish();
                leftTransition();
            }
        });
        rsbyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent theIntent=new Intent(context,RSBYDetails.class);
                ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                        AppConstant.SELECTED_ITEM_FOR_VERIFICATION,selectedMemItem.serialize(), context);
                startActivity(theIntent);
                finish();
                leftTransition();
            }
        });*/

        backLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backIV.performClick();
            }
        });
        healthSchemelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent theIntent = new Intent(context, HealthSchemeActivity.class);
                selectedMemItem.setSeccMemberItem(seccMemberItem);

                ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                        AppConstant.SELECTED_ITEM_FOR_VERIFICATION, selectedMemItem.serialize(), context);
                startActivity(theIntent);
                finish();
                leftTransition();
            }
        });
        nomineeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent theIntent = new Intent(context, NomineeCaptureActivity.class);
                ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                        AppConstant.SELECTED_ITEM_FOR_VERIFICATION, selectedMemItem.serialize(), context);
                startActivity(theIntent);
                finish();
                leftTransition();
            }
        });
        photoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent theIntent = new Intent(context, PhotoCaptureActivity.class);
                selectedMemItem.setSeccMemberItem(seccMemberItem);
                ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                        AppConstant.SELECTED_ITEM_FOR_VERIFICATION, selectedMemItem.serialize(), context);
                startActivity(theIntent);
                finish();
                leftTransition();

            }
        });

        editMemberDetlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent theIntent = new Intent(context, OtherDetailActivity.class);
                selectedMemItem.setSeccMemberItem(seccMemberItem);
                ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                        AppConstant.SELECTED_ITEM_FOR_VERIFICATION,selectedMemItem.serialize(), context);
                startActivity(theIntent);
                finish();
                leftTransition();*/
                //  finish();

            }
        });
        aadhaarStatusSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                AadhaarStatusItem item = aadhaarStatusList.get(i);
                aadhaarLayout.setVisibility(View.GONE);
                govtIdLayout.setVisibility(View.GONE);
                seccMemberItem.setAadhaarStatus(item.getaStatusCode());
                //Log.d(TAG,"Secc Member Status "+seccMemberItem.getAadhaarStatus());
                if (item.getaStatusCode().equalsIgnoreCase("1")) {
                    aadhaarLayout.setVisibility(View.VISIBLE);
                } else {
                    govtIdLayout.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent theIntent = null;
                /*if(seccMemberItem!=null) {
                    theIntent = new Intent(context, SeccMemberDetailActivity.class);

                }else{
                    theIntent = new Intent(context, RSBYMembersDetailsActivity.class);
                }*/
              /*  startActivity(theIntent);
                rightTransition();*/
                rightTransition();
                finish();
            }
        });
        menuLayout.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        settings.performClick();

                    }
                });
        updateNhpsBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent theIntent = null;
                if (houseHoldItem != null && houseHoldItem.getDataSource() != null && houseHoldItem.getDataSource().equalsIgnoreCase(AppConstant.RSBY_SOURCE)) {
                    updateRsbyDetail();
                } else {
                    updateSeccDetail();
                }

                ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                        AppConstant.SELECTED_ITEM_FOR_VERIFICATION, selectedMemItem.serialize(), context);
                //  theIntent = new Intent(context, SECCFamilyListActivity.class);
                theIntent = new Intent(context, SeccMemberListActivity.class);
                theIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(theIntent);
                finish();
                leftTransition();
            }
        });
        lockBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent theIntent = null;

                if (seccMemberItem != null) {
                    if (seccMemberItem.getMemStatus() != null &&
                            !seccMemberItem.getMemStatus().trim().equalsIgnoreCase(AppConstant.MEMBERFOUND_BUT_NOT_PRESENT)) {
                        //  AppUtility.showLog(AppConstant.LOG_STATUS,TAG," Aadhaar Status "+seccMemberItem.getAadhaarStatus());
                        if (!photoCollection.equalsIgnoreCase("N")) {
                            if (seccMemberItem.getMemberPhoto1() == null || seccMemberItem.getMemberPhoto1().equalsIgnoreCase("")) {
                                CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzCaptureMemberPhoto));
                                return;
                            }
                        }
                        // AppUtility.showLog(AppConstant.LOG_STATUS,TAG," Aadhaar Status 6"+seccMemberItem.getAadhaarStatus());

                        if (seccMemberItem.getAadhaarStatus() != null && seccMemberItem.getAadhaarStatus().trim().equalsIgnoreCase(AppConstant.AADHAAR_STAT)) {
                            if (seccMemberItem.getAadhaarNo() == null || seccMemberItem.getAadhaarNo().equalsIgnoreCase("")) {
                                CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzCaptureAadhaarDetail));
                                return;
                            }

                            //    AppUtility.showLog(AppConstant.LOG_STATUS,TAG," Aadhaar Status 7 :"+seccMemberItem.getAadhaarNo());

                            //if (seccMemberItem.getAadhaarNo()!=null || !seccMemberItem.getAadhaarNo().trim().equalsIgnoreCase("")) {


                            // comment by sauarbh  for bypassing aadhar validation on lock time
                             /*  if(seccMemberItem.getAadhaarAuth()!=null &&
                                    seccMemberItem.getAadhaarAuth().trim().equalsIgnoreCase(AppConstant.PENDING_STATUS) ) {
                                CustomAlert.alertWithOk(context, "Please validate Aadhaar");
                                return;
                            }*/
                            // }
                            //    AppUtility.showLog(AppConstant.LOG_STATUS,TAG," Aadhaar Status 8 :"+seccMemberItem.getAadhaarAuth());
                        }
                        //     AppUtility.showLog(AppConstant.LOG_STATUS,TAG," Aadhaar Status 5"+seccMemberItem.getAadhaarStatus());

                        if (seccMemberItem.getAadhaarStatus() != null && seccMemberItem.getAadhaarStatus().equalsIgnoreCase(AppConstant.GOVT_ID_STAT)) {
                            if (seccMemberItem.getIdType() != null && !seccMemberItem.getIdType().equalsIgnoreCase("8")) {
                                if (seccMemberItem.getIdNo() == null || ((seccMemberItem.getIdNo() != null && seccMemberItem.getIdNo().equalsIgnoreCase("")))) {
                                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzCaptureGovernId));
                                    return;
                                }
                            }
                        }
                        if (seccMemberItem.getMobileNo() == null || seccMemberItem.getMobileNo().equalsIgnoreCase("")) {
                            CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzCaptureMobileNum));
                            return;
                        }
                        if (seccMemberItem.getMobileNo() != null && seccMemberItem.getMobileNo().equalsIgnoreCase(loginResponse.getMobileNumber())) {
                            CustomAlert.alertWithOk(context, context.getResources().getString(R.string.youHaveEnteredValidatorMobileNum));
                            return;
                        }
                        //   AppUtility.showLog(AppConstant.LOG_STATUS,TAG," Aadhaar Status 3"+seccMemberItem.getAadhaarStatus());
                        if (nomineeCollection.equalsIgnoreCase("Y")) {
                            if (seccMemberItem.getNameNominee() == null || seccMemberItem.getNameNominee().equalsIgnoreCase("")) {
                                CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzCaptureNomineeDetail));
                                return;
                            }
                        }

                        //  AppUtility.showLog(AppConstant.LOG_STATUS,TAG," Aadhaar Status 2"+seccMemberItem.getAadhaarStatus());

                        if (seccMemberItem != null && seccMemberItem.getAadhaarStatus() != null
                                && seccMemberItem.getAadhaarStatus().equalsIgnoreCase(AppConstant.AADHAAR_STAT)) {
                            //ArrayList<SeccMemberItem> checkAadhaarList = SeccDatabase.seccMemberListByAadhaar(seccMemberItem.getAadhaarNo().trim(), context);

                            if (seccMemberItem.getMobileNo().equalsIgnoreCase(loginResponse.getMobileNumber())) {
                                CustomAlert.alertWithOk(context, context.getResources().getString(R.string.youHaveEnteredValidatorMobileNum));
                                return;
                            }

                            if (AppUtility.isAadhaarDuplicate(seccMemberItem, context)) {
                                alertForValidateLater(getResources().getString(R.string.aadhaar_already_captured), null);
                            } else {
                                seccMemberItem.setError_code(null);
                                seccMemberItem.setError_type(null);
                                seccMemberItem.setError_msg(null);
                                houseHoldItem.setError_code(null);
                                houseHoldItem.setError_msg(null);
                                houseHoldItem.setError_type(null);
                                lockedDetail();
                            }
                        } else if (seccMemberItem != null && seccMemberItem.getAadhaarStatus() != null
                                && seccMemberItem.getAadhaarStatus().equalsIgnoreCase(AppConstant.GOVT_ID_STAT)) {
                            seccMemberItem.setError_code(null);
                            seccMemberItem.setError_type(null);
                            seccMemberItem.setError_msg(null);
                            houseHoldItem.setError_code(null);
                            houseHoldItem.setError_msg(null);
                            houseHoldItem.setError_type(null);
                            lockedDetail();
                        }

                        //  finish();
                    } else {
                        seccMemberItem.setMemberPhoto1(null);
                        if (seccMemberItem.getAadhaarStatus() != null && seccMemberItem.getAadhaarStatus().equalsIgnoreCase("1")) {
                            if (seccMemberItem.getAadhaarNo() == null || seccMemberItem.getAadhaarNo().equalsIgnoreCase("")) {
                                CustomAlert.alertWithOk(context, "Please capture aadhaar detail");
                                return;
                            }
                        }
                      /*  if(seccMemberItem.getAadhaarStatus()!=null && seccMemberItem.getAadhaarStatus().equalsIgnoreCase("2")){
                            if(seccMemberItem.getIdNo()==null || (seccMemberItem.getIdNo()!=null && seccMemberItem.getIdNo().equalsIgnoreCase(""))){
                                CustomAlert.alertWithOk(context,"Please capture government id detail");
                                return;
                            }
                        }*/
                        if (seccMemberItem.getAadhaarStatus() != null && seccMemberItem.getAadhaarStatus().equalsIgnoreCase(AppConstant.GOVT_ID_STAT)) {
                            if (seccMemberItem.getIdType() != null && !seccMemberItem.getIdType().equalsIgnoreCase("8")) {
                                if (seccMemberItem.getIdNo() == null || ((seccMemberItem.getIdNo() != null && seccMemberItem.getIdNo().equalsIgnoreCase("")))) {
                                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzCaptureGovernId));
                                    return;
                                }
                            }
                        }
                        if (seccMemberItem.getMobileNo() == null || seccMemberItem.getMobileNo().equalsIgnoreCase("")) {
                            CustomAlert.alertWithOk(context, "Please capture mobile number");
                            return;
                        }
                        if (seccMemberItem.getNameNominee() == null || seccMemberItem.getNameNominee().equalsIgnoreCase("")) {
                            CustomAlert.alertWithOk(context, "Please capture nominee detail");
                            return;
                        }


                        if (seccMemberItem.getMobileNo() != null && seccMemberItem.getMobileNo().equalsIgnoreCase(loginResponse.getMobileNumber())) {
                            CustomAlert.alertWithOk(context, context.getResources().getString(R.string.youHaveEnteredValidatorMobileNum));
                            return;
                        }


                        if (seccMemberItem.getAadhaarStatus() != null && seccMemberItem.getAadhaarStatus().trim().equalsIgnoreCase(AppConstant.AADHAAR_STAT)) {
                            if (seccMemberItem.getAadhaarNo() == null || seccMemberItem.getAadhaarNo().equalsIgnoreCase("")) {
                                CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzCaptureAadhaarDetail));
                                return;
                            }
                            AppUtility.showLog(AppConstant.LOG_STATUS, TAG, " Aadhaar Status 7 :" + seccMemberItem.getAadhaarNo());

                            //if (seccMemberItem.getAadhaarNo()!=null || !seccMemberItem.getAadhaarNo().trim().equalsIgnoreCase("")) {
                         /*   if(seccMemberItem.getAadhaarAuth()!=null &&
                                    seccMemberItem.getAadhaarAuth().trim().equalsIgnoreCase(AppConstant.PENDING_STATUS) ) {
                                CustomAlert.alertWithOk(context, "Please validate Aadhaar");
                                return;
                            }*/
                        }

                        /*if(seccMemberItem!=null && seccMemberItem.getAadhaarStatus()!=null && seccMemberItem.getAadhaarStatus().equalsIgnoreCase(AppConstant.AADHAAR_STAT)) {
                            SeccMemberItem checkAadhaar = SeccDatabase.seccMemberDetailByAadhaar(seccMemberItem.getAadhaarNo().trim(), context);
                            if (checkAadhaar != null) {
                              *//*if (checkAadhaar.getAadhaarNo() != null && checkAadhaar.getAadhaarNo().trim().equalsIgnoreCase(seccMemberItem.getAadhaarNo().trim())) {
                              } else {
                                  CustomAlert.alertWithOk(context, "Aadhaar Number has been already captured");
                                  return;
                              }*//*
                                alertForValidateLater(getResources().getString(R.string.aadhaar_already_captured),null);
                            }else{
                                lockedDetail();

                            }
                        }else if(seccMemberItem!=null && seccMemberItem.getAadhaarStatus()!=null
                                && seccMemberItem.getAadhaarStatus().equalsIgnoreCase(AppConstant.GOVT_ID_STAT)) {
                            lockedDetail();
                        }*/

                        if (seccMemberItem != null && seccMemberItem.getAadhaarStatus() != null
                                && seccMemberItem.getAadhaarStatus().equalsIgnoreCase(AppConstant.AADHAAR_STAT)) {
                            //ArrayList<SeccMemberItem> checkAadhaarList = SeccDatabase.seccMemberListByAadhaar(seccMemberItem.getAadhaarNo().trim(), context);

                            if (loginResponse.getAadhaarNumber().equalsIgnoreCase(seccMemberItem.getAadhaarNo().toString())) {
                                alertForValidateLater(getResources().getString(R.string.validator_adhaar_captured), null);
                            } else if (AppUtility.isAadhaarDuplicate(seccMemberItem, context)) {
                                alertForValidateLater(getResources().getString(R.string.aadhaar_already_captured), null);
                            } else if (seccMemberItem.getAadhaarAuth() != null && !seccMemberItem.getAadhaarAuth().equalsIgnoreCase(AppConstant.AADHAAR_AUTH_YES)) {
                                alertForValidateLater(getResources().getString(R.string.aadhaar_not_validate), null);
                            } else {
                                seccMemberItem.setError_code(null);
                                seccMemberItem.setError_type(null);
                                seccMemberItem.setError_msg(null);
                                houseHoldItem.setError_code(null);
                                houseHoldItem.setError_msg(null);
                                houseHoldItem.setError_type(null);
                                lockedDetail();
                            }
                        } else if (seccMemberItem != null && seccMemberItem.getAadhaarStatus() != null
                                && seccMemberItem.getAadhaarStatus().equalsIgnoreCase(AppConstant.GOVT_ID_STAT)) {
                            lockedDetail();
                        }
                    }
                } else {
                    // theIntent = new Intent(context, RSBYFamilyMemberListActivity.class);
                }
            }
        });

        if (validationMode.equalsIgnoreCase("b")) {
            appConfigWithValidationViaBoth();
        } else if (validationMode.equalsIgnoreCase("a")) {
            appConfigWithValidationViaAadharOnly();
        } else if (validationMode.equalsIgnoreCase("g")) {
            appConfigWithValidationViaGov();
        }

        if (additionalScheme.equalsIgnoreCase("S")) {
            additionalSchemeLayout.setVisibility(View.VISIBLE);

        } else if (additionalScheme.equalsIgnoreCase("R")) {
            additionalSchemeLayout.setVisibility(View.VISIBLE);
            healthSchemeNoTV.setText(context.getResources().getString(R.string.captureRsby));

        } else if (additionalScheme.equalsIgnoreCase("B")) {
            additionalSchemeLayout.setVisibility(View.VISIBLE);

        } else if (additionalScheme.equalsIgnoreCase("N")) {
            additionalSchemeLayout.setVisibility(View.GONE);

        }

        if (photoCollection.equalsIgnoreCase("Y")) {
            if (seccMemberItem.getMemStatus() != null && seccMemberItem.getMemStatus().equalsIgnoreCase(AppConstant.MEMBERFOUND_BUT_NOT_PRESENT)) {
                photoLayout.setVisibility(View.GONE);
            } else {
                photoLayout.setVisibility(View.VISIBLE);
            }
        } else if (photoCollection.equalsIgnoreCase("N")) {
            photoLayout.setVisibility(View.GONE);
        }

        if (nomineeCollection.equalsIgnoreCase("Y")) {
            nomineeCaptureStatusLinearLayout.setVisibility(View.VISIBLE);
        } else if (nomineeCollection.equalsIgnoreCase("N")) {
            nomineeCaptureStatusLinearLayout.setVisibility(View.GONE);
        }
    }

    private void setupScreenWithOutZoom() {
        loginResponse = (VerifierLoginResponse.create(ProjectPrefrence.
                getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.VERIFIER_CONTENT, context)));
        showNotification();
        headerTV = (TextView) findViewById(R.id.centertext);
        selectedMemItem = SelectedMemberItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.SELECTED_ITEM_FOR_VERIFICATION, context));
        if (selectedMemItem != null && selectedMemItem.getHouseHoldItem() != null) {
            houseHoldItem = selectedMemItem.getHouseHoldItem();
        }
        if (selectedMemItem != null && selectedMemItem.getSeccMemberItem() != null) {
            seccMemberItem = selectedMemItem.getSeccMemberItem();
            if (houseHoldItem != null && houseHoldItem.getDataSource() != null
                    && houseHoldItem.getDataSource().trim().equalsIgnoreCase(AppConstant.RSBY_SOURCE)) {
                showRsbyDetail(seccMemberItem);

            } else {
                showSeccDetail(seccMemberItem);
            }
        }
        AppUtility.showLog(AppConstant.LOG_STATUS, TAG, " Secc Member Item : " + seccMemberItem);
        //   dashBoardNavBT = (TextView) v.findViewById(R.id.dashBoardNavBT);
        photoStar = (TextView) findViewById(R.id.capturePhotoStarTV);
        govtIdStar = (TextView) findViewById(R.id.govtIdStarTV);
        backLayout = (RelativeLayout) findViewById(R.id.backLayout);
        aadhaarStar = (TextView) findViewById(R.id.aadhaarStarTV);
        mobileNoStar = (TextView) findViewById(R.id.mobileNoStarTV);
        healthSchemeNoTV = (TextView) findViewById(R.id.healthSchemeNoTV);
        nomineeDetStart = (TextView) findViewById(R.id.nomineeDetailStarTV);
        photoCaptureStatus = (RelativeLayout) findViewById(R.id.photoCaptureStatus);
        govtIdCaptureStatus = (RelativeLayout) findViewById(R.id.govtidCaptureStatus);
        additionalSchemeLayout = (LinearLayout) findViewById(R.id.additionalSchemeLayout);
        aadhaarCaptureStatus = (RelativeLayout) findViewById(R.id.aadharCaptureStatus);
        mobileCaptureStatus = (RelativeLayout) findViewById(R.id.mobileCaptureStatus);
        nomineeCaptureStatus = (RelativeLayout) findViewById(R.id.nomineeCaptureStatus);
        additionalCaptureStatus = (RelativeLayout) findViewById(R.id.additionalCaptureStatus);
        aadhaarRadiogroupLayout = (LinearLayout) findViewById(R.id.aadhaarRadiogroupLayout);
        menuLayout = (RelativeLayout) findViewById(R.id.menuLayout);

        aadhaarStatusRG = (RadioGroup) findViewById(R.id.aadhaarStatusRG);
        aadhaarAvailableRB = (RadioButton) findViewById(R.id.aadhaarAvailRB);
        aadhaarNotPrepRB = (RadioButton) findViewById(R.id.aadhaarNotPrepRB);
        aadhaarStatusSP = (Spinner) findViewById(R.id.aadhaarStatusSP);
        aadhaarLayout = (LinearLayout) findViewById(R.id.aadhaarRelLayout);
        enrollmentLayout = (RelativeLayout) findViewById(R.id.enrollRelLayout);
        bankLayout = (RelativeLayout) findViewById(R.id.bankDetRelLayout);
        rsbyLayout = (RelativeLayout) findViewById(R.id.rsbyDetRelLayout);
        mobileLayout = (RelativeLayout) findViewById(R.id.mobileRelLayout);
        healthSchemelayout = (RelativeLayout) findViewById(R.id.healthSchemeRelLayout);
        nomineeLayout = (RelativeLayout) findViewById(R.id.nomineeRelLayout);
        nomineeCaptureStatusLinearLayout = (LinearLayout) findViewById(R.id.nomineeCaptureStatusLinearLayout);
        photoLayout = (LinearLayout) findViewById(R.id.memberPhotoLayout);
        govtIdLayout = (LinearLayout) findViewById(R.id.govtIdLayout);
        settings = (ImageView) findViewById(R.id.settings);
        editMemberDetlayout = (RelativeLayout) findViewById(R.id.memberDetRelLayout);
        editMemberDetlayout.setVisibility(View.GONE);
        backIV = (ImageView) findViewById(R.id.back);
        updateNhpsBT = (Button) findViewById(R.id.updateNhpsBT);
        lockBT = (Button) findViewById(R.id.lockBT);
        aadhaarStatLayout = (RelativeLayout) findViewById(R.id.aadhaarStatLayout);
        aadhaarStatLayout.findViewById(R.id.aadhVerifiedIV).setVisibility(View.VISIBLE);
        ImageView verified = (ImageView) aadhaarStatLayout.findViewById(R.id.aadhVerifiedIV);
        verified.setVisibility(View.GONE);
        ImageView rejected = (ImageView) aadhaarStatLayout.findViewById(R.id.aadhRejectedIV);
        rejected.setVisibility(View.GONE);
        ImageView pending = (ImageView) aadhaarStatLayout.findViewById(R.id.aadhPendingIV);
        pending.setVisibility(View.GONE);

        if (seccMemberItem.getMemStatus() != null && seccMemberItem.getAadhaarAuth() != null && seccMemberItem.getAadhaarAuth().equalsIgnoreCase("Y")) {
            verified.setVisibility(View.VISIBLE);
        } else if (seccMemberItem.getMemStatus() != null && seccMemberItem.getAadhaarAuth() != null && seccMemberItem.getAadhaarAuth().equalsIgnoreCase("N")) {
            rejected.setVisibility(View.VISIBLE);
        } else if (seccMemberItem.getMemStatus() != null && seccMemberItem.getAadhaarAuth() != null && seccMemberItem.getAadhaarAuth().equalsIgnoreCase("P")) {
            pending.setVisibility(View.VISIBLE);
        }

        photoCaptureStatus.setVisibility(View.INVISIBLE);
        govtIdCaptureStatus.setVisibility(View.INVISIBLE);
        aadhaarCaptureStatus.setVisibility(View.INVISIBLE);
        mobileCaptureStatus.setVisibility(View.INVISIBLE);
        nomineeCaptureStatus.setVisibility(View.INVISIBLE);
        additionalCaptureStatus.setVisibility(View.INVISIBLE);
        setMobileStatus();
        setURNStatus();
        setPhotoCaptureStatus();
        // prepareAadhaarStatusSpinner();
        setAadhaarStatus();
        setNomineeStatus();


        showMendatoryField();
       /* aadhaarAvailableRB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){

                }

            }
        });*/
        dashboardDropdown();
        backLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backIV.performClick();
            }
        });
        aadhaarLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent theIntent = new Intent(context, CaptureAadhaarActivity.class);*/
                Intent theIntent = new Intent(context, CaptureAadharDetailActivity.class);
                // theIntent.putExtra(AppConstant.MEMBER_TYPE,memberType);
                selectedMemItem.setSeccMemberItem(seccMemberItem);
                ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                        AppConstant.SELECTED_ITEM_FOR_VERIFICATION, selectedMemItem.serialize(), context);
                startActivity(theIntent);
                //  finish();
                leftTransition();
            }
        });
        govtIdLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent theIntent;
                //if(seccMemberItem.getIdType()==null){

                theIntent = new Intent(context, GovermentIdCapturingNavigation.class);
                //                   theIntent = new Intent(context, GovermentIDCaptureActivity.class);


                //theIntent.putExtra(AppConstant.NEVIGATE, AppConstant.WITH_OUT_AADHAAR);
                selectedMemItem.setSeccMemberItem(seccMemberItem);

                ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                        AppConstant.SELECTED_ITEM_FOR_VERIFICATION, selectedMemItem.serialize(), context);
                startActivity(theIntent);
                finish();
                leftTransition();
            }
        });
        enrollmentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  Intent theIntent = new Intent(context, EnrollmentActivity.class);
                startActivity(theIntent);
                finish();
                leftTransition();*/

            }
        });
        mobileLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent theIntent = new Intent(context, PhoneNumberActivity.class);
                selectedMemItem.setSeccMemberItem(seccMemberItem);
                ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                        AppConstant.SELECTED_ITEM_FOR_VERIFICATION, selectedMemItem.serialize(), context);
                startActivity(theIntent);
                finish();
                leftTransition();
            }
        });

       /* bankLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent theIntent=new Intent(context,BankDetailsActivity.class);
                *//*theIntent.putExtra(AppConstant.NEVIGATE,AppConstant.WITH_AADHAAR);
                theIntent.putExtra(AppConstant.MEMBER_TYPE,memberType);
*//*                startActivity(theIntent);
              //  finish();
                leftTransition();
            }
        });
        rsbyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent theIntent=new Intent(context,RSBYDetails.class);
                ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                        AppConstant.SELECTED_ITEM_FOR_VERIFICATION,selectedMemItem.serialize(), context);
                startActivity(theIntent);
                finish();
                leftTransition();
            }
        });*/
        healthSchemelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent theIntent = new Intent(context, HealthSchemeActivity.class);
                selectedMemItem.setSeccMemberItem(seccMemberItem);

                ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                        AppConstant.SELECTED_ITEM_FOR_VERIFICATION, selectedMemItem.serialize(), context);
                startActivity(theIntent);
                finish();
                leftTransition();
            }
        });
        nomineeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent theIntent = new Intent(context, NomineeCaptureActivity.class);
                ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                        AppConstant.SELECTED_ITEM_FOR_VERIFICATION, selectedMemItem.serialize(), context);
                startActivity(theIntent);
                finish();
                leftTransition();
            }
        });
        photoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent theIntent = new Intent(context, PhotoCaptureActivity.class);
                selectedMemItem.setSeccMemberItem(seccMemberItem);
                ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                        AppConstant.SELECTED_ITEM_FOR_VERIFICATION, selectedMemItem.serialize(), context);
                startActivity(theIntent);
                finish();
                leftTransition();

            }
        });

        editMemberDetlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent theIntent = new Intent(context, OtherDetailActivity.class);
                selectedMemItem.setSeccMemberItem(seccMemberItem);
                ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                        AppConstant.SELECTED_ITEM_FOR_VERIFICATION,selectedMemItem.serialize(), context);
                startActivity(theIntent);
                finish();
                leftTransition();*/
                //  finish();

            }
        });
        aadhaarStatusSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                AadhaarStatusItem item = aadhaarStatusList.get(i);
                aadhaarLayout.setVisibility(View.GONE);
                govtIdLayout.setVisibility(View.GONE);
                seccMemberItem.setAadhaarStatus(item.getaStatusCode());
                //Log.d(TAG,"Secc Member Status "+seccMemberItem.getAadhaarStatus());
                if (item.getaStatusCode().equalsIgnoreCase("1")) {
                    aadhaarLayout.setVisibility(View.VISIBLE);
                } else {
                    govtIdLayout.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent theIntent = null;
                /*if(seccMemberItem!=null) {
                    theIntent = new Intent(context, SeccMemberDetailActivity.class);

                }else{
                    theIntent = new Intent(context, RSBYMembersDetailsActivity.class);
                }*/
              /*  startActivity(theIntent);
                rightTransition();*/
                rightTransition();
                finish();
            }
        });
        menuLayout.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        settings.performClick();
                    }
                });
        updateNhpsBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent theIntent = null;
                if (houseHoldItem != null && houseHoldItem.getDataSource() != null && houseHoldItem.getDataSource().equalsIgnoreCase(AppConstant.RSBY_SOURCE)) {
                    updateRsbyDetail();
                } else {
                    updateSeccDetail();
                }

                ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                        AppConstant.SELECTED_ITEM_FOR_VERIFICATION, selectedMemItem.serialize(), context);
                //  theIntent = new Intent(context, SECCFamilyListActivity.class);
                theIntent = new Intent(context, SeccMemberListActivity.class);
                theIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(theIntent);
                finish();
                leftTransition();
            }
        });
        lockBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent theIntent = null;

                if (seccMemberItem != null) {
                    if (seccMemberItem.getMemStatus() != null &&
                            !seccMemberItem.getMemStatus().trim().equalsIgnoreCase(AppConstant.MEMBERFOUND_BUT_NOT_PRESENT)) {
                        //  AppUtility.showLog(AppConstant.LOG_STATUS,TAG," Aadhaar Status "+seccMemberItem.getAadhaarStatus());
                        if (!photoCollection.equalsIgnoreCase("N")) {
                            if (seccMemberItem.getMemberPhoto1() == null || seccMemberItem.getMemberPhoto1().equalsIgnoreCase("")) {
                                CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzCaptureMemberPhoto));
                                return;
                            }
                        }
                        // AppUtility.showLog(AppConstant.LOG_STATUS,TAG," Aadhaar Status 6"+seccMemberItem.getAadhaarStatus());

                        if (seccMemberItem.getAadhaarStatus() != null && seccMemberItem.getAadhaarStatus().trim().equalsIgnoreCase(AppConstant.AADHAAR_STAT)) {
                            if (seccMemberItem.getAadhaarNo() == null || seccMemberItem.getAadhaarNo().equalsIgnoreCase("")) {
                                CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzCaptureAadhaarDetail));
                                return;
                            }

                            //    AppUtility.showLog(AppConstant.LOG_STATUS,TAG," Aadhaar Status 7 :"+seccMemberItem.getAadhaarNo());

                            //if (seccMemberItem.getAadhaarNo()!=null || !seccMemberItem.getAadhaarNo().trim().equalsIgnoreCase("")) {


                            // comment by sauarbh  for bypassing aadhar validation on lock time
                             /*  if(seccMemberItem.getAadhaarAuth()!=null &&
                                    seccMemberItem.getAadhaarAuth().trim().equalsIgnoreCase(AppConstant.PENDING_STATUS) ) {
                                CustomAlert.alertWithOk(context, "Please validate Aadhaar");
                                return;
                            }*/
                            // }
                            //    AppUtility.showLog(AppConstant.LOG_STATUS,TAG," Aadhaar Status 8 :"+seccMemberItem.getAadhaarAuth());
                        }
                        //     AppUtility.showLog(AppConstant.LOG_STATUS,TAG," Aadhaar Status 5"+seccMemberItem.getAadhaarStatus());

                        if (seccMemberItem.getAadhaarStatus() != null && seccMemberItem.getAadhaarStatus().equalsIgnoreCase(AppConstant.GOVT_ID_STAT)) {
                            if (seccMemberItem.getIdType() != null && !seccMemberItem.getIdType().equalsIgnoreCase("8")) {
                                if (seccMemberItem.getIdNo() == null || (seccMemberItem.getIdNo() != null && seccMemberItem.getIdNo().equalsIgnoreCase(""))) {
                                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzCaptureGovernId));
                                    return;
                                }
                            }
                        }
                        if (seccMemberItem.getMobileNo() == null || seccMemberItem.getMobileNo().equalsIgnoreCase("")) {
                            CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzCaptureMobileNum));
                            return;
                        }
                        if (seccMemberItem.getMobileNo() != null && seccMemberItem.getMobileNo().equalsIgnoreCase(loginResponse.getMobileNumber())) {
                            CustomAlert.alertWithOk(context, context.getResources().getString(R.string.youHaveEnteredValidatorMobileNum));
                            return;
                        }
                        //   AppUtility.showLog(AppConstant.LOG_STATUS,TAG," Aadhaar Status 3"+seccMemberItem.getAadhaarStatus());
                        if (nomineeCollection.equalsIgnoreCase("Y")) {
                            if (seccMemberItem.getNameNominee() == null || seccMemberItem.getNameNominee().equalsIgnoreCase("")) {
                                CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzCaptureNomineeDetail));
                                return;
                            }
                        }

                        //  AppUtility.showLog(AppConstant.LOG_STATUS,TAG," Aadhaar Status 2"+seccMemberItem.getAadhaarStatus());

                        if (seccMemberItem != null && seccMemberItem.getAadhaarStatus() != null
                                && seccMemberItem.getAadhaarStatus().equalsIgnoreCase(AppConstant.AADHAAR_STAT)) {
                            //ArrayList<SeccMemberItem> checkAadhaarList = SeccDatabase.seccMemberListByAadhaar(seccMemberItem.getAadhaarNo().trim(), context);

                            if (seccMemberItem.getMobileNo().equalsIgnoreCase(loginResponse.getMobileNumber())) {
                                CustomAlert.alertWithOk(context, context.getResources().getString(R.string.youHaveEnteredValidatorMobileNum));
                                return;
                            }

                            if (AppUtility.isAadhaarDuplicate(seccMemberItem, context)) {
                                alertForValidateLater(getResources().getString(R.string.aadhaar_already_captured), null);
                            } else {
                                seccMemberItem.setError_code(null);
                                seccMemberItem.setError_type(null);
                                seccMemberItem.setError_msg(null);
                                houseHoldItem.setError_code(null);
                                houseHoldItem.setError_msg(null);
                                houseHoldItem.setError_type(null);
                                lockedDetail();
                            }
                        } else if (seccMemberItem != null && seccMemberItem.getAadhaarStatus() != null
                                && seccMemberItem.getAadhaarStatus().equalsIgnoreCase(AppConstant.GOVT_ID_STAT)) {
                            seccMemberItem.setError_code(null);
                            seccMemberItem.setError_type(null);
                            seccMemberItem.setError_msg(null);
                            houseHoldItem.setError_code(null);
                            houseHoldItem.setError_msg(null);
                            houseHoldItem.setError_type(null);
                            lockedDetail();
                        }

                        //  finish();
                    } else {
                        seccMemberItem.setMemberPhoto1(null);
                        /*if(seccMemberItem.getAadhaarStatus()!=null && seccMemberItem.getAadhaarStatus().equalsIgnoreCase("1")){
                            if (seccMemberItem.getAadhaarNo()==null || seccMemberItem.getAadhaarNo().equalsIgnoreCase("")) {
                                CustomAlert.alertWithOk(context, "Please capture aadhaar detail");
                                return;
                            }
                        }
                        if(seccMemberItem.getAadhaarStatus()!=null && seccMemberItem.getAadhaarStatus().equalsIgnoreCase("2")){
                            if(seccMemberItem.getIdNo()==null || (seccMemberItem.getIdNo()!=null && seccMemberItem.getIdNo().equalsIgnoreCase(""))){
                                CustomAlert.alertWithOk(context,"Please capture government id detail");
                                return;
                            }
                        }
                        if (seccMemberItem.getMobileNo()==null || seccMemberItem.getMobileNo().equalsIgnoreCase("")) {
                            CustomAlert.alertWithOk(context, "Please capture mobile number");
                            return;
                        }*/


                        if (seccMemberItem.getMobileNo() != null && seccMemberItem.getMobileNo().equalsIgnoreCase(loginResponse.getMobileNumber())) {
                            CustomAlert.alertWithOk(context, context.getResources().getString(R.string.youHaveEnteredValidatorMobileNum));
                            return;
                        }


                        if (seccMemberItem.getAadhaarStatus() != null && seccMemberItem.getAadhaarStatus().trim().equalsIgnoreCase(AppConstant.AADHAAR_STAT)) {
                            if (seccMemberItem.getAadhaarNo() == null || seccMemberItem.getAadhaarNo().equalsIgnoreCase("")) {
                                CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzCaptureAadhaarDetail));
                                return;
                            }
                            AppUtility.showLog(AppConstant.LOG_STATUS, TAG, " Aadhaar Status 7 :" + seccMemberItem.getAadhaarNo());

                            //if (seccMemberItem.getAadhaarNo()!=null || !seccMemberItem.getAadhaarNo().trim().equalsIgnoreCase("")) {
                         /*   if(seccMemberItem.getAadhaarAuth()!=null &&
                                    seccMemberItem.getAadhaarAuth().trim().equalsIgnoreCase(AppConstant.PENDING_STATUS) ) {
                                CustomAlert.alertWithOk(context, "Please validate Aadhaar");
                                return;
                            }*/
                        }

                        if (seccMemberItem.getAadhaarStatus() != null && seccMemberItem.getAadhaarStatus().equalsIgnoreCase(AppConstant.GOVT_ID_STAT)) {
                            if (seccMemberItem.getIdType() != null && !seccMemberItem.getIdType().equalsIgnoreCase("8")) {
                                if (seccMemberItem.getIdNo() == null || (seccMemberItem.getIdNo() != null && seccMemberItem.getIdNo().equalsIgnoreCase(""))) {
                                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzCaptureGovernId));
                                    return;
                                }
                            }
                        }
                        /*if(seccMemberItem!=null && seccMemberItem.getAadhaarStatus()!=null && seccMemberItem.getAadhaarStatus().equalsIgnoreCase(AppConstant.AADHAAR_STAT)) {
                            SeccMemberItem checkAadhaar = SeccDatabase.seccMemberDetailByAadhaar(seccMemberItem.getAadhaarNo().trim(), context);
                            if (checkAadhaar != null) {
                              *//*if (checkAadhaar.getAadhaarNo() != null && checkAadhaar.getAadhaarNo().trim().equalsIgnoreCase(seccMemberItem.getAadhaarNo().trim())) {
                              } else {
                                  CustomAlert.alertWithOk(context, "Aadhaar Number has been already captured");
                                  return;
                              }*//*
                                alertForValidateLater(getResources().getString(R.string.aadhaar_already_captured),null);
                            }else{
                                lockedDetail();

                            }
                        }else if(seccMemberItem!=null && seccMemberItem.getAadhaarStatus()!=null
                                && seccMemberItem.getAadhaarStatus().equalsIgnoreCase(AppConstant.GOVT_ID_STAT)) {
                            lockedDetail();
                        }*/
                        if (seccMemberItem.getNameNominee() == null || seccMemberItem.getNameNominee().equalsIgnoreCase("")) {
                            CustomAlert.alertWithOk(context, "Please capture nominee detail");
                            return;
                        }
                        if (seccMemberItem != null && seccMemberItem.getAadhaarStatus() != null
                                && seccMemberItem.getAadhaarStatus().equalsIgnoreCase(AppConstant.AADHAAR_STAT)) {
                            //ArrayList<SeccMemberItem> checkAadhaarList = SeccDatabase.seccMemberListByAadhaar(seccMemberItem.getAadhaarNo().trim(), context);

                            if (loginResponse.getAadhaarNumber().equalsIgnoreCase(seccMemberItem.getAadhaarNo().toString())) {
                                alertForValidateLater(getResources().getString(R.string.validator_adhaar_captured), null);
                            } else if (AppUtility.isAadhaarDuplicate(seccMemberItem, context)) {
                                alertForValidateLater(getResources().getString(R.string.aadhaar_already_captured), null);
                            } else if (seccMemberItem.getAadhaarAuth() != null && !seccMemberItem.getAadhaarAuth().equalsIgnoreCase(AppConstant.AADHAAR_AUTH_YES)) {
                                alertForValidateLater(getResources().getString(R.string.aadhaar_not_validate), null);
                            } else {
                                seccMemberItem.setError_code(null);
                                seccMemberItem.setError_type(null);
                                seccMemberItem.setError_msg(null);
                                houseHoldItem.setError_code(null);
                                houseHoldItem.setError_msg(null);
                                houseHoldItem.setError_type(null);
                                lockedDetail();
                            }
                        } else if (seccMemberItem != null && seccMemberItem.getAadhaarStatus() != null
                                && seccMemberItem.getAadhaarStatus().equalsIgnoreCase(AppConstant.GOVT_ID_STAT)) {
                            lockedDetail();
                        }
                    }
                } else {
                    // theIntent = new Intent(context, RSBYFamilyMemberListActivity.class);
                }
            }
        });

        if (validationMode.equalsIgnoreCase("b")) {
            appConfigWithValidationViaBoth();
        } else if (validationMode.equalsIgnoreCase("a")) {
            appConfigWithValidationViaAadharOnly();
        } else if (validationMode.equalsIgnoreCase("g")) {
            appConfigWithValidationViaGov();
        }

        if (additionalScheme.equalsIgnoreCase("S")) {
            additionalSchemeLayout.setVisibility(View.VISIBLE);

        } else if (additionalScheme.equalsIgnoreCase("R")) {
            additionalSchemeLayout.setVisibility(View.VISIBLE);
            healthSchemeNoTV.setText(context.getResources().getString(R.string.captureRsby));

        } else if (additionalScheme.equalsIgnoreCase("B")) {
            additionalSchemeLayout.setVisibility(View.VISIBLE);

        } else if (additionalScheme.equalsIgnoreCase("N")) {
            additionalSchemeLayout.setVisibility(View.GONE);

        }

        if (photoCollection.equalsIgnoreCase("Y")) {
            if (seccMemberItem.getMemStatus() != null && seccMemberItem.getMemStatus().equalsIgnoreCase(AppConstant.MEMBERFOUND_BUT_NOT_PRESENT)) {
                photoLayout.setVisibility(View.GONE);
            } else {
                photoLayout.setVisibility(View.VISIBLE);
            }
        } else if (photoCollection.equalsIgnoreCase("N")) {
            photoLayout.setVisibility(View.GONE);
        }

        if (nomineeCollection.equalsIgnoreCase("Y")) {
            nomineeCaptureStatusLinearLayout.setVisibility(View.VISIBLE);
        } else if (nomineeCollection.equalsIgnoreCase("N")) {
            nomineeCaptureStatusLinearLayout.setVisibility(View.GONE);
        }
    }

    private void alertForValidateLater(String msg, SeccMemberItem item) {
        internetDiaolg = new AlertDialog.Builder(context).create();
        LayoutInflater factory = LayoutInflater.from(context);
        View alertView = factory.inflate(R.layout.internet_try_again_popup, null);
        internetDiaolg.setView(alertView);
        internetDiaolg.show();
        TextView tryGainMsgTV = (TextView) alertView.findViewById(R.id.deleteMsg);
        tryGainMsgTV.setText(msg);
        Button tryAgainBT = (Button) alertView.findViewById(R.id.tryAgainBT);
        tryAgainBT.setText("Confirm");
        Button cancelBT = (Button) alertView.findViewById(R.id.cancelBT);
        tryAgainBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                internetDiaolg.dismiss();
                lockedDetail();
            }
        });
        cancelBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                internetDiaolg.dismiss();
            }
        });
    }

    private void lockedDetail() {
        Intent theIntent;
        seccMemberItem = AppUtility.clearAadhaarOrGovtDetail(seccMemberItem);
        selectedMemItem.setSeccMemberItem(seccMemberItem);
        AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Total Byte size : " + seccMemberItem.serialize().getBytes().length);
        ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.SELECTED_ITEM_FOR_VERIFICATION, selectedMemItem.serialize(), context);
        theIntent = new Intent(context, PreviewActivity.class);
        startActivity(theIntent);
        leftTransition();
    }

    private void prepareAadhaarStatusSpinner() {
        aadhaarStatusList = new ArrayList<>();
        ArrayList<String> spinnerList = new ArrayList<>();
        aadhaarStatusList.add(new AadhaarStatusItem("1", "Aadhaar Available"));
        aadhaarStatusList.add(new AadhaarStatusItem("2", "Aadhaar not prepared"));
        for (AadhaarStatusItem item : aadhaarStatusList) {
            spinnerList.add(item.getaStatusDesc());
        }
        ArrayAdapter<String> maritalAdapter = new ArrayAdapter<>(context, R.layout.custom_drop_down, R.id.textView, spinnerList);
        aadhaarStatusSP.setAdapter(maritalAdapter);
        if (seccMemberItem != null) {
            for (int i = 0; i < aadhaarStatusList.size(); i++) {
                if (seccMemberItem.getAadhaarStatus() != null && seccMemberItem.getAadhaarStatus()
                        .equalsIgnoreCase(aadhaarStatusList.get(i).getaStatusCode())) {
                    selectedAadhaarStatus = i;
                    break;
                }
            }
        }
        aadhaarStatusSP.setSelection(selectedAadhaarStatus);

    }

    private void setScreenData() {
        // prepareMemberStatusSpinner();
    }

    private void setMobileStatus() {
        RelativeLayout mobileStatusLayout = (RelativeLayout) findViewById(R.id.mobileStatusLayout);
        ImageView mobVerified = (ImageView) mobileStatusLayout.findViewById(R.id.mobileVerifiedIV);
        ImageView mobRejected = (ImageView) mobileStatusLayout.findViewById(R.id.mobileRejectedIV);
        ImageView mobPending = (ImageView) mobileStatusLayout.findViewById(R.id.mobilePendingIV);
        mobRejected.setVisibility(View.GONE);
        mobPending.setVisibility(View.GONE);
        mobVerified.setVisibility(View.GONE);
        if (seccMemberItem.getMobileNo() != null && !seccMemberItem.getMobileNo().equalsIgnoreCase("")) {
            if (seccMemberItem.getMobileAuth().equalsIgnoreCase("Y")) {
                mobVerified.setVisibility(View.VISIBLE);
            } else if (seccMemberItem.getMobileAuth().equalsIgnoreCase("N")) {
                mobRejected.setVisibility(View.VISIBLE);
            } else if (seccMemberItem.getMobileAuth().equalsIgnoreCase("P")) {
                mobPending.setVisibility(View.VISIBLE);
            }
            mobileCaptureStatus.setVisibility(View.VISIBLE);
        }
    }

    private void setURNStatus() {
        ImageView rsbyVerified = (ImageView) findViewById(R.id.rsbyVerifiedIV);
        ImageView rsbyPending = (ImageView) findViewById(R.id.rsbyPendingIV);
        ImageView rsbyRejected = (ImageView) findViewById(R.id.rsbyRejectedIV);
        rsbyVerified.setVisibility(View.GONE);
        rsbyPending.setVisibility(View.GONE);
        rsbyRejected.setVisibility(View.GONE);
        if (seccMemberItem.getUrnNo() != null && !seccMemberItem.getUrnNo().equalsIgnoreCase("")) {
            additionalCaptureStatus.setVisibility(View.VISIBLE);
        }
        if (seccMemberItem.getSchemeId() != null && !seccMemberItem.getSchemeId().equalsIgnoreCase("")) {
            additionalCaptureStatus.setVisibility(View.VISIBLE);
        }
    }

    private void setPhotoCaptureStatus() {

        if (seccMemberItem.getMemberPhoto1() != null && !seccMemberItem.getMemberPhoto1().equalsIgnoreCase("")) {
            photoCaptureStatus.setVisibility(View.VISIBLE);
        }
    }

    private void setAadhaarStatus() {
        aadhaarStatus = seccMemberItem.getAadhaarStatus();
        aadhaarLayout.setVisibility(View.GONE);
        govtIdLayout.setVisibility(View.GONE);
        if (aadhaarStatus != null && !aadhaarStatus.equalsIgnoreCase("")) {
            if (aadhaarStatus.equalsIgnoreCase("1")) {
                aadhaarLayout.setVisibility(View.VISIBLE);
                aadhaarAvailableRB.setChecked(true);
            } else if (aadhaarStatus.equalsIgnoreCase("2")) {
                govtIdLayout.setVisibility(View.VISIBLE);
                aadhaarNotPrepRB.setChecked(true);
            } else {
                aadhaarLayout.setVisibility(View.VISIBLE);
                aadhaarStatus = "1";
                seccMemberItem.setAadhaarStatus(aadhaarStatus);
                aadhaarAvailableRB.setChecked(true);
            }
        } else {
            aadhaarLayout.setVisibility(View.VISIBLE);
            aadhaarStatus = "1";
            seccMemberItem.setAadhaarStatus(aadhaarStatus);
            aadhaarAvailableRB.setChecked(true);
        }
        if (seccMemberItem.getAadhaarStatus().equalsIgnoreCase(AppConstant.AADHAAR_STAT)) {
            if (seccMemberItem.getNameAadhaar() != null && !seccMemberItem.getNameAadhaar().equalsIgnoreCase(""))
                aadhaarCaptureStatus.setVisibility(View.VISIBLE);
        } else if (seccMemberItem.getAadhaarStatus().equalsIgnoreCase(AppConstant.GOVT_ID_STAT)) {
            if (seccMemberItem.getIdType() != null && !seccMemberItem.getIdType().equalsIgnoreCase("")) {
                govtIdCaptureStatus.setVisibility(View.VISIBLE);
            }
        }
    }

    private void setNomineeStatus() {
        if (seccMemberItem.getNameNominee() != null && !seccMemberItem.getNameNominee().equalsIgnoreCase("")) {
            nomineeCaptureStatus.setVisibility(View.VISIBLE);
        }
    }

    private void showMendatoryField() {

        photoStar.setVisibility(View.INVISIBLE);
        govtIdStar.setVisibility(View.INVISIBLE);
        aadhaarStar.setVisibility(View.INVISIBLE);
        mobileNoStar.setVisibility(View.INVISIBLE);
        nomineeDetStart.setVisibility(View.INVISIBLE);
        if (seccMemberItem != null && seccMemberItem.getMemStatus() != null &&
                seccMemberItem.getMemStatus().equalsIgnoreCase(AppConstant.MEMBERFOUND_AND_PRESENT)) {
            if (seccMemberItem.getAadhaarStatus().equalsIgnoreCase(AppConstant.AADHAAR_STAT)) {
                aadhaarStar.setVisibility(View.VISIBLE);
            } else if (seccMemberItem.getAadhaarStatus().equalsIgnoreCase(AppConstant.GOVT_ID_STAT)) {
                govtIdStar.setVisibility(View.VISIBLE);
            }
            photoStar.setVisibility(View.VISIBLE);
            mobileNoStar.setVisibility(View.VISIBLE);
            nomineeDetStart.setVisibility(View.VISIBLE);
        } else if (seccMemberItem != null && seccMemberItem.getMemStatus() != null &&
                seccMemberItem.getMemStatus().equalsIgnoreCase(AppConstant.MEMBERFOUND_BUT_NOT_PRESENT)) {
            photoStar.setVisibility(View.INVISIBLE);
            mobileNoStar.setVisibility(View.VISIBLE);
            nomineeDetStart.setVisibility(View.VISIBLE);
            if (seccMemberItem.getAadhaarStatus().equalsIgnoreCase(AppConstant.AADHAAR_STAT)) {
                aadhaarStar.setVisibility(View.VISIBLE);

            } else if (seccMemberItem.getAadhaarStatus().equalsIgnoreCase(AppConstant.GOVT_ID_STAT)) {
                // if (seccMemberItem.getIdNo() != null && !seccMemberItem.getIdNo().equalsIgnoreCase("")) {
                govtIdStar.setVisibility(View.VISIBLE);
                //}
            }
           /* photoStar.setVisibility(View.VISIBLE);
            mobileNoStar.setVisibility(View.VISIBLE);
            nomineeDetStart.setVisibility(View.VISIBLE);*/
        }
    }

    private void showRsbyDetail(SeccMemberItem item) {
        headerTV.setText(seccMemberItem.getRsbyName());
    }

    private void showSeccDetail(SeccMemberItem item) {
        headerTV.setText(seccMemberItem.getName());
    }

    private void updateRsbyDetail() {
        if (seccMemberItem != null) {
            seccMemberItem = AppUtility.clearAadhaarOrGovtDetail(seccMemberItem);
            seccMemberItem.setLockedSave(AppConstant.SAVE + "");
            AppUtility.showLog(AppConstant.LOG_STATUS, TAG, " OLD Household name " +
                    ": " + selectedMemItem.getOldHeadMember().getName() + "" +
                    " Member Status " + selectedMemItem.getOldHeadMember().getMemStatus() + " House hold Status :" +
                    " " + selectedMemItem.getOldHeadMember().getHhStatus() + " Locked Save :" + selectedMemItem.getOldHeadMember().getLockedSave());
            Log.d(TAG, " NHPS Relation : " + seccMemberItem.getNhpsRelationCode());
            if (selectedMemItem.getOldHeadMember() != null && selectedMemItem.getNewHeadMember() != null) {
                SeccMemberItem oldHead = selectedMemItem.getOldHeadMember();
                oldHead.setLockedSave(AppConstant.LOCKED + "");
                AppUtility.showLog(AppConstant.LOG_STATUS, TAG, " OLD Household name " +
                        ": " + oldHead.getName() + "" +
                        " Member Status " + oldHead.getMemStatus() + " House hold Status :" +
                        " " + oldHead.getHhStatus() + " Locked Save :" + oldHead.getLockedSave());
                SeccDatabase.updateRsbyMember(selectedMemItem.getOldHeadMember(), context);
            }

            SeccDatabase.updateRsbyMember(seccMemberItem, context);
            HouseHoldItem houseHoldItem = selectedMemItem.getHouseHoldItem();
            AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "  Household name " +
                    ": " + houseHoldItem.getName() + "House hold Status :" +
                    " " + houseHoldItem.getHhStatus() + " Locked Save :" + houseHoldItem.getLockSave());
            if (SeccDatabase.checkUnderSurveyMember(context, houseHoldItem)) {
                houseHoldItem.setLockSave(AppConstant.SAVE + "");
            } else {
                houseHoldItem.setLockSave(AppConstant.LOCKED + "");
            }
            selectedMemItem.setHouseHoldItem(houseHoldItem);
            Log.d("Preview Activity", " household json : " + houseHoldItem.serialize());
            SeccDatabase.updateRsbyHousehold(selectedMemItem.getHouseHoldItem(), context);
            selectedMemItem.setSeccMemberItem(seccMemberItem);
        }
    }

    private void updateSeccDetail() {
        if (seccMemberItem != null) {
            seccMemberItem = AppUtility.clearAadhaarOrGovtDetail(seccMemberItem);
            seccMemberItem.setLockedSave(AppConstant.SAVE + "");
            AppUtility.showLog(AppConstant.LOG_STATUS, TAG, " OLD Household name " +
                    ": " + selectedMemItem.getOldHeadMember().getName() + "" +
                    " Member Status " + selectedMemItem.getOldHeadMember().getMemStatus() + " House hold Status :" +
                    " " + selectedMemItem.getOldHeadMember().getHhStatus() + " Locked Save :" + selectedMemItem.getOldHeadMember().getLockedSave());
            Log.d(TAG, " NHPS Relation : " + seccMemberItem.getNhpsRelationCode());
            if (selectedMemItem.getOldHeadMember() != null && selectedMemItem.getNewHeadMember() != null) {
                SeccMemberItem oldHead = selectedMemItem.getOldHeadMember();
                oldHead.setLockedSave(AppConstant.LOCKED + "");
                AppUtility.showLog(AppConstant.LOG_STATUS, TAG, " OLD Household name " +
                        ": " + oldHead.getName() + "" +
                        " Member Status " + oldHead.getMemStatus() + " House hold Status :" +
                        " " + oldHead.getHhStatus() + " Locked Save :" + oldHead.getLockedSave());
                SeccDatabase.updateSeccMember(selectedMemItem.getOldHeadMember(), context);
            }

            SeccDatabase.updateSeccMember(seccMemberItem, context);
            HouseHoldItem houseHoldItem = selectedMemItem.getHouseHoldItem();
            AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "  Household name " +
                    ": " + houseHoldItem.getName() + "House hold Status :" +
                    " " + houseHoldItem.getHhStatus() + " Locked Save :" + houseHoldItem.getLockSave());
            if (SeccDatabase.checkUnderSurveyMember(context, houseHoldItem)) {
                houseHoldItem.setLockSave(AppConstant.SAVE + "");
            } else {
                houseHoldItem.setLockSave(AppConstant.LOCKED + "");
            }
            selectedMemItem.setHouseHoldItem(houseHoldItem);
            SeccDatabase.updateHouseHold(selectedMemItem.getHouseHoldItem(), context);
            selectedMemItem.setSeccMemberItem(seccMemberItem);
        }
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

    private void appConfigWithValidationViaGov() {
        aadhaarStatus = "2";
        seccMemberItem.setAadhaarStatus(aadhaarStatus);
        govtIdStar.setVisibility(View.VISIBLE);
        aadhaarLayout.setVisibility(View.GONE);
        aadhaarRadiogroupLayout.setVisibility(View.GONE);
        govtIdLayout.setVisibility(View.VISIBLE);
        if (seccMemberItem.getAadhaarStatus().equalsIgnoreCase(AppConstant.GOVT_ID_STAT)) {
            if (seccMemberItem.getIdType() != null && !seccMemberItem.getIdType().equalsIgnoreCase("")) {
                govtIdCaptureStatus.setVisibility(View.VISIBLE);
            }
        }

    }

    private void appConfigWithValidationViaAadharOnly() {
        aadhaarStatus = "1";
        seccMemberItem.setAadhaarStatus(aadhaarStatus);
        aadhaarLayout.setVisibility(View.VISIBLE);
        aadhaarRadiogroupLayout.setVisibility(View.GONE);

        aadhaarStatusRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                View radioButton = aadhaarStatusRG.findViewById(checkedId);
                int index = aadhaarStatusRG.indexOfChild(radioButton);

                // Add logic here
                aadhaarLayout.setVisibility(View.GONE);
                govtIdLayout.setVisibility(View.GONE);
             /*   seccMemberItem.setAadhaarStatus(item.getaStatusCode());
                Log.d(TAG,"Secc Member Status "+seccMemberItem.getAadhaarStatus());
                if(item.getaStatusCode().equalsIgnoreCase("1")){
                    aadhaarLayout.setVisibility(View.VISIBLE);
                }else{
                    govtIdLayout.setVisibility(View.VISIBLE);

                }*/
                switch (index) {
                    case 0:
                        Log.d(TAG, "Aadhaar Status Selected");
                        aadhaarStatus = "1";
                        seccMemberItem.setAadhaarStatus(aadhaarStatus);
                        aadhaarLayout.setVisibility(View.VISIBLE);
                        showMendatoryField();
                        break;
                    case 1:
                        Log.d(TAG, "Aadhaar Status not selected");
                        aadhaarStatus = "2";
                        seccMemberItem.setAadhaarStatus(aadhaarStatus);
                        govtIdLayout.setVisibility(View.VISIBLE);
                        showMendatoryField();
                        break;
                }

            }
        });

        govtIdLayout.setVisibility(View.GONE);
    }

    private void appConfigWithValidationViaBoth() {

       /* aadhaarLayout.setVisibility(View.VISIBLE);*/
        aadhaarRadiogroupLayout.setVisibility(View.VISIBLE);
        //  govtIdLayout.setVisibility(View.VISIBLE);
        aadhaarStatusRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                View radioButton = aadhaarStatusRG.findViewById(checkedId);
                int index = aadhaarStatusRG.indexOfChild(radioButton);

                // Add logic here
                aadhaarLayout.setVisibility(View.GONE);
                govtIdLayout.setVisibility(View.GONE);

                switch (index) {
                    case 0:
                        Log.d(TAG, "Aadhaar Status Selected");
                        aadhaarStatus = "1";
                        seccMemberItem.setAadhaarStatus(aadhaarStatus);
                        aadhaarLayout.setVisibility(View.VISIBLE);
                        showMendatoryField();
                        break;
                    case 1:
                        Log.d(TAG, "Aadhaar Status not selected");
                        aadhaarStatus = "2";
                        seccMemberItem.setAadhaarStatus(aadhaarStatus);
                        govtIdLayout.setVisibility(View.VISIBLE);
                        showMendatoryField();
                        break;
                }

            }
        });


    }

    private void checkAppConfig() {
        StateItem selectedStateItem = StateItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SELECTED_STATE, context));
        ArrayList<ConfigurationItem> configList = SeccDatabase.findConfiguration(selectedStateItem.getStateCode(), context);

        if (configList != null) {
            for (ConfigurationItem item1 : configList) {
                if (item1.getConfigId().equalsIgnoreCase(AppConstant.VALIDATION_MODE_CONFIG)) {
                    validationMode = item1.getStatus();
                }
                if (item1.getConfigId().equalsIgnoreCase(AppConstant.ADDITIONAL_SCHEME)) {
                    additionalScheme = item1.getStatus();
                }
                if (item1.getConfigId().equalsIgnoreCase(AppConstant.RSBY_DATA_SOURCE_CONFIG)) {
                    validationDataSource = item1.getStatus();
                }
                if (item1.getConfigId().equalsIgnoreCase(AppConstant.PHOTO_COLLECT)) {
                    photoCollection = item1.getStatus();
                }
                if (item1.getConfigId().equalsIgnoreCase(AppConstant.NOMINEE_COLLECT)) {
                    nomineeCollection = item1.getStatus();
                }
                if (item1.getConfigId().equalsIgnoreCase(AppConstant.APPLICATION_ZOOM)) {
                    zoomMode = item1.getStatus();
                }
            }
        }

    }

    private void dashboardDropdown(View v) {

        settings.setVisibility(View.VISIBLE);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(context, settings);
                popup.getMenuInflater()
                        .inflate(R.menu.menu_nav_dashboard, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {

                            case R.id.dashboard:
                                //   String searchText = searchFamilyMemberET.getText().toString();
                                Intent theIntent = new Intent(context, SearchActivityWithHouseHold.class);
                                theIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                theIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(theIntent);
                                leftTransition();
                                break;

                        }
                        return true;
                    }
                });
                popup.show();
            }
        });

    }

    private void dashboardDropdown() {

        settings.setVisibility(View.VISIBLE);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(context, settings);
                popup.getMenuInflater()
                        .inflate(R.menu.menu_nav_dashboard, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {

                            case R.id.dashboard:
                                //   String searchText = searchFamilyMemberET.getText().toString();
                                Intent theIntent = new Intent(context, SearchActivityWithHouseHold.class);
                                theIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                theIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(theIntent);
                                leftTransition();
                                break;

                        }
                        return true;
                    }
                });
                popup.show();
            }
        });

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

}

