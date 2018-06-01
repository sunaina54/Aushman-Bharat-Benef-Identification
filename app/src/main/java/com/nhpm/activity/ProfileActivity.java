package com.nhpm.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.customComponent.utility.DateTimeUtil;
import com.customComponent.utility.ProjectPrefrence;
import com.nhpm.LocalDataBase.dto.SeccDatabase;
import com.nhpm.Models.NotificationModel;
import com.nhpm.Models.response.master.ConfigurationItem;
import com.nhpm.Models.response.master.StateItem;
import com.nhpm.Models.response.seccMembers.HouseHoldItem;
import com.nhpm.Models.response.verifier.AadhaarResponseItem;
import com.nhpm.Models.response.verifier.VerifierLocationItem;
import com.nhpm.Models.response.verifier.VerifierLoginResponse;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;
import com.nhpm.Utility.AppUtility;

import java.util.ArrayList;
import java.util.List;

import pl.polidea.view.ZoomView;

public class ProfileActivity extends BaseActivity {
    private TextView nameTV, designTV, genderTV, mobileNoTV, emailTV, ebTV, wardTV, villagetTV, subDistrictTV, districtTV, stateTV;
    private TextView ekycModeTV,demoGraphicModeTV,dataCatogeryTV, cardPrintModeTV, nomineeserveyModeTV, seccDataTV, capturePhotoModeTV, aadharValidationModeTV, rsbyDataTV, zoomModeTV, surveyorDataTV, identificationModeTV;
    //  private LinearLayout ebLayout, wardLayout;
    private VerifierLoginResponse loginResponse;
    private Context context;
    private TextView headerTV, aadhaarNoTV, addressTV;
    private ImageView backIV, profilePicIV;
    private AadhaarResponseItem aadhaarItem;
    private LinearLayout mZoomLinearLayout;
    private ZoomView zoomView;
    private VerifierLocationItem location;
    private String downloadedDataType;
    private String rsbyDataModel;
    private String nomineeCollectModeModel;
    private String validationModeModel;
    private StateItem selectedStateItem;
    private String photoCollectModeModel;
    private String dataDownloadingModeModel;
    private String ekycModel;
    private String demographicModel;
    private String aadharAuthModeModel;
    private String dataCategory;
    private String additionalSchemeModel;
    private String printCardModel;
    private String seccDataDownloadModeModel;
    private String zoomMode = "N";
    private RecyclerView profileDownloadedLocation, profileNotificationRecyclerView,nationalNotificationRecyclerView;
    private VerifierLoginResponse verifierDetail;
    private LinearLayout downloadLocationLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        checkAppConfig();

        if (zoomMode.equalsIgnoreCase("N")) {
            setContentView(R.layout.activity_profile);
            setupScreenWithoutZoom();
        } else {
            setContentView(R.layout.dummy_layout_for_zooming);
            setupScreenWithZoom();
        }
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        profileDownloadedLocation.setLayoutManager(mLayoutManager);
        profileDownloadedLocation.setItemAnimator(new DefaultItemAnimator());
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        profileNotificationRecyclerView.setLayoutManager(layoutManager);
        profileNotificationRecyclerView.setItemAnimator(new DefaultItemAnimator());
        nationalNotificationRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        nationalNotificationRecyclerView.setItemAnimator(new DefaultItemAnimator());


        ArrayList<NotificationModel> arrayList = SeccDatabase.getAllAppNotification(context);

        if (arrayList != null && arrayList.size() > 0) {
            ArrayList<NotificationModel> nationalNotificationArray = new ArrayList<NotificationModel>();

            ArrayList<NotificationModel> stateNotificationArray = new ArrayList<NotificationModel>();

            if (selectedStateItem != null && selectedStateItem.getStateCode() != null && !selectedStateItem.getStateCode().equalsIgnoreCase("")) {
                for (int i = 0; i < arrayList.size(); i++) {

                    if (arrayList.get(i).getTargetState().equalsIgnoreCase("00")) {
                        nationalNotificationArray.add(arrayList.get(i));
                    }
                    if (arrayList.get(i).getTargetState().equalsIgnoreCase(selectedStateItem.getStateCode())) {
                        stateNotificationArray.add(arrayList.get(i));
                    }
                }
            } else {
                stateNotificationArray = arrayList;
            }
            NotificationAdapter nationalAdapter = new NotificationAdapter(nationalNotificationArray);
            nationalNotificationRecyclerView.setAdapter(nationalAdapter);
            nationalAdapter.notifyDataSetChanged();


            NotificationAdapter adapter = new NotificationAdapter(stateNotificationArray);
            profileNotificationRecyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
        prepareBlockForDownloaddList();
    }

    private void setupScreenWithZoom() {

        mZoomLinearLayout = (LinearLayout) findViewById(R.id.mZoomLinearLayout);
        View v = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.activity_profile, null, false);
        v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
        showNotification(v);
        checkAppConfigToSetData(v);
        profileDownloadedLocation = (RecyclerView) v.findViewById(R.id.profileDownloadedLocation);
        profileNotificationRecyclerView = (RecyclerView) v.findViewById(R.id.profileNotification);
        nationalNotificationRecyclerView=(RecyclerView) v.findViewById(R.id.nationalNotification);
        loginResponse = VerifierLoginResponse.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.VERIFIER_CONTENT, context));
        downloadedDataType = ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.DOWNLOADINGSOURCE, context);
        location = VerifierLocationItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF
                , AppConstant.SELECTED_BLOCK, context));
        aadhaarItem = loginResponse.getAadhaarItem();
        headerTV = (TextView) v.findViewById(R.id.centertext);
        headerTV.setText("Profile");
        downloadLocationLayout = (LinearLayout) v.findViewById(R.id.downloadLocationLayout);
        backIV = (ImageView) v.findViewById(R.id.back);
        nameTV = (TextView) v.findViewById(R.id.verifierNameTV);
        //  designTV=(TextView)findViewById(R.id.designTV);
        genderTV = (TextView) v.findViewById(R.id.genderTV);
        mobileNoTV = (TextView) v.findViewById(R.id.mobileTV);
        dataCatogeryTV = (TextView) v.findViewById(R.id.dataCatogeryTV);
        aadhaarNoTV = (TextView) v.findViewById(R.id.verifierAadharNoTV);
        emailTV = (TextView) v.findViewById(R.id.emailTV);
        addressTV = (TextView) v.findViewById(R.id.addressTV);
        profilePicIV = (ImageView) v.findViewById(R.id.verifierProfileIV);



        zoomView = new ZoomView(this);
        zoomView.addView(v);
        mZoomLinearLayout.addView(zoomView);

        if (loginResponse.getAadhaarItem() != null) {
            aadhaarItem = loginResponse.getAadhaarItem();
            if (loginResponse.getAadhaarItem() != null && loginResponse.getAadhaarItem().getBase64() != null) {

                profilePicIV.setImageBitmap(AppUtility.convertStringToBitmap(loginResponse.getAadhaarItem().getBase64()));
            }
        }


        String aadhaarAddress = "";

        if (aadhaarItem != null) {
            if (aadhaarItem.getName() != null)
                nameTV.setText(aadhaarItem.getName());

            if (aadhaarItem.getUid() != null)
                aadhaarNoTV.setText("XXXXXXXX" + aadhaarItem.getUid().substring(10));
            // designTV.setText(loginResponse.getDesignation());
            // if(loginResponse.getAadhaarItem().getGender()!=null)
            if (aadhaarItem.getPht() != null)
                mobileNoTV.setText(aadhaarItem.getPht());
           /* if(aadhaarItem.ge)
            emailTV.setText(loginResponse.getEmail());
*/
            if (aadhaarItem.getGender() != null)
                genderTV.setText(aadhaarItem.getGender().equalsIgnoreCase("M") ? "Male" : "Female");

            if (aadhaarItem.getPhone() != null)
                mobileNoTV.setText(aadhaarItem.getPhone());

            if (aadhaarItem.getEmail() != null) {
                emailTV.setText(aadhaarItem.getEmail());
            }

            if (aadhaarItem.getDob() != null)


                if (aadhaarItem.getCo() != null) {
                    aadhaarAddress = aadhaarItem.getCo();
                }
            AppUtility.showLog(AppConstant.LOG_STATUS, "Profile Activity", " House No." + aadhaarItem.getHouse());
            if (aadhaarItem.getHouse() != null) {
                aadhaarAddress = aadhaarAddress + ", " + aadhaarItem.getHouse();
            }
            if (aadhaarItem.getStreet() != null) {
                aadhaarAddress = aadhaarAddress + ", " + aadhaarItem.getStreet();
            }
            if (aadhaarItem.getLoc() != null) {
                aadhaarAddress = aadhaarAddress + ", " + aadhaarItem.getLoc();
            }
            if (aadhaarItem.getVtc() != null) {
                aadhaarAddress = aadhaarAddress + ", " + aadhaarItem.getVtc();
            }

            /*if(aadhaarItem.getPo()!=null){
                aadhaarAddress=aadhaarAddress+", "+aadhaarItem.getPo();
            }*/
            /*if(aadhaarItem.getSubdist()!=null){
                aadhaarAddress=aadhaarAddress+", "+aadhaarItem.getSubdist();
            }*/

            if (aadhaarItem.getDist() != null) {
                aadhaarAddress = aadhaarAddress + ", " + aadhaarItem.getDist();
            }

            if (aadhaarItem.getState() != null) {
                aadhaarAddress = aadhaarAddress + ", " + aadhaarItem.getState();
            }
            if (aadhaarItem.getPc() != null) {
                aadhaarAddress = aadhaarAddress + ", " + aadhaarItem.getPc();
            }


        } else if (loginResponse != null) {
            if (loginResponse.getName() != null) {
                nameTV.setText(loginResponse.getName());
            }
            if (loginResponse.getEmailLogin() != null) {
                emailTV.setText(loginResponse.getEmailLogin());
            }
            if (loginResponse.getMobileNumber() != null) {
                mobileNoTV.setText(loginResponse.getMobileNumber());
            }
            if (loginResponse.getGender() != null) {
                genderTV.setText(loginResponse.getGender());
            }

        }

        if (downloadedDataType != null) {
            if (downloadedDataType.equalsIgnoreCase("V")) {
                //           ebLayout.setVisibility(View.GONE);
                //         wardLayout.setVisibility(View.GONE);
            }
        }

        addressTV.setText(aadhaarAddress);

        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                rightTransition();
            }
        });
    }

    private void setupScreenWithoutZoom() {
        showNotification();
        checkAppConfigToSetData();
        loginResponse = VerifierLoginResponse.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.VERIFIER_CONTENT, context));
        downloadedDataType = ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.DOWNLOADINGSOURCE, context);
        location = loginResponse.getLocationList().get(0);
        aadhaarItem = loginResponse.getAadhaarItem();
        profileDownloadedLocation = (RecyclerView) findViewById(R.id.profileDownloadedLocation);
        profileNotificationRecyclerView = (RecyclerView) findViewById(R.id.profileNotification);
        nationalNotificationRecyclerView=(RecyclerView)findViewById(R.id.nationalNotification);

        headerTV = (TextView) findViewById(R.id.centertext);
        downloadLocationLayout = (LinearLayout) findViewById(R.id.downloadLocationLayout);
        headerTV.setText("Profile");
        backIV = (ImageView) findViewById(R.id.back);
        nameTV = (TextView) findViewById(R.id.verifierNameTV);
        //  designTV=(TextView)findViewById(R.id.designTV);
        genderTV = (TextView) findViewById(R.id.genderTV);
        mobileNoTV = (TextView) findViewById(R.id.mobileTV);
        aadhaarNoTV = (TextView) findViewById(R.id.verifierAadharNoTV);
        emailTV = (TextView) findViewById(R.id.emailTV);
        addressTV = (TextView) findViewById(R.id.addressTV);
        profilePicIV = (ImageView) findViewById(R.id.verifierProfileIV);
        dataCatogeryTV = (TextView) findViewById(R.id.dataCatogeryTV);

     /*   ebTV = (TextView) findViewById(R.id.ebTV);
        wardTV = (TextView) findViewById(R.id.wardTV);
        villagetTV = (TextView) findViewById(R.id.villagetTV);
        subDistrictTV = (TextView) findViewById(R.id.subDistrictTV);
        districtTV = (TextView) findViewById(R.id.districtTV);
        stateTV = (TextView) findViewById(R.id.stateTV);
        ebLayout = (LinearLayout) findViewById(R.id.ebLayout);
        wardLayout = (LinearLayout) findViewById(R.id.wardLayout);


        if (location != null) {
            if (location.getBlockCode() != null) {
                ebTV.setText(location.getBlockCode());
            }

            if (location.getTehsilName() != null) {
                subDistrictTV.setText(location.getTehsilName());
            }
            if (location.getWardCode() != null) {
                wardTV.setText(location.getWardCode());
            }

            if (location.getVtName() != null) {
                villagetTV.setText(location.getVtName());
            }
            if (location.getDistrictName() != null) {
                districtTV.setText(location.getDistrictName());
            }
            if (location.getStateName() != null) {
                stateTV.setText(location.getStateName());
            }

        }*/


        if (loginResponse.getAadhaarItem() != null) {
            aadhaarItem = loginResponse.getAadhaarItem();
            if (loginResponse.getAadhaarItem() != null && loginResponse.getAadhaarItem().getBase64() != null) {

                profilePicIV.setImageBitmap(AppUtility.convertStringToBitmap(loginResponse.getAadhaarItem().getBase64()));
            }
        }


        String aadhaarAddress = "";

        if (aadhaarItem != null) {
            if (aadhaarItem.getName() != null)
                nameTV.setText(aadhaarItem.getName());

            if (aadhaarItem.getUid() != null)
                aadhaarNoTV.setText("XXXXXXXX" + aadhaarItem.getUid().substring(10));
            // designTV.setText(loginResponse.getDesignation());
            // if(loginResponse.getAadhaarItem().getGender()!=null)
            if (aadhaarItem.getPht() != null)
                mobileNoTV.setText(aadhaarItem.getPht());
           /* if(aadhaarItem.ge)
            emailTV.setText(loginResponse.getEmail());
*/
            if (aadhaarItem.getGender() != null)
                genderTV.setText(aadhaarItem.getGender().equalsIgnoreCase("M") ? "Male" : "Female");

            if (aadhaarItem.getPhone() != null)
                mobileNoTV.setText(aadhaarItem.getPhone());

            if (aadhaarItem.getEmail() != null) {
                emailTV.setText(aadhaarItem.getEmail());
            }

            if (aadhaarItem.getDob() != null)


                if (aadhaarItem.getCo() != null) {
                    aadhaarAddress = aadhaarItem.getCo();
                }
            AppUtility.showLog(AppConstant.LOG_STATUS, "Profile Activity", " House No." + aadhaarItem.getHouse());
            if (aadhaarItem.getHouse() != null) {
                aadhaarAddress = aadhaarAddress + ", " + aadhaarItem.getHouse();
            }
            if (aadhaarItem.getStreet() != null) {
                aadhaarAddress = aadhaarAddress + ", " + aadhaarItem.getStreet();
            }
            if (aadhaarItem.getLoc() != null) {
                aadhaarAddress = aadhaarAddress + ", " + aadhaarItem.getLoc();
            }
            if (aadhaarItem.getVtc() != null) {
                aadhaarAddress = aadhaarAddress + ", " + aadhaarItem.getVtc();
            }

            /*if(aadhaarItem.getPo()!=null){
                aadhaarAddress=aadhaarAddress+", "+aadhaarItem.getPo();
            }*/
            /*if(aadhaarItem.getSubdist()!=null){
                aadhaarAddress=aadhaarAddress+", "+aadhaarItem.getSubdist();
            }*/

            if (aadhaarItem.getDist() != null) {
                aadhaarAddress = aadhaarAddress + ", " + aadhaarItem.getDist();
            }

            if (aadhaarItem.getState() != null) {
                aadhaarAddress = aadhaarAddress + ", " + aadhaarItem.getState();
            }
            if (aadhaarItem.getPc() != null) {
                aadhaarAddress = aadhaarAddress + ", " + aadhaarItem.getPc();
            }


        } else if (loginResponse != null) {
            if (loginResponse.getName() != null) {
                nameTV.setText(loginResponse.getName());
            }
            if (loginResponse.getEmailLogin() != null) {
                emailTV.setText(loginResponse.getEmailLogin());
            }
            if (loginResponse.getMobileNumber() != null) {
                mobileNoTV.setText(loginResponse.getMobileNumber());
            }
            if (loginResponse.getGender() != null) {
                genderTV.setText(loginResponse.getGender());
            }

        }

        if (downloadedDataType != null) {
            if (downloadedDataType.equalsIgnoreCase("V")) {
                //         ebLayout.setVisibility(View.GONE);
                //           wardLayout.setVisibility(View.GONE);
            }
        }

        addressTV.setText(aadhaarAddress);

        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                rightTransition();
            }
        });
    }

    private void checkAppConfigToSetData(View v) {
        StateItem selectedStateItem = StateItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SELECTED_STATE, context));
        ArrayList<ConfigurationItem> configList;
        if (selectedStateItem != null && selectedStateItem.getStateCode() != null) {
            configList = SeccDatabase.findConfiguration(selectedStateItem.getStateCode(), context);
        } else {
            configList = SeccDatabase.findConfiguration("24", context);
        }
        if (configList != null) {
            for (ConfigurationItem item1 : configList) {
                if (item1.getConfigId().equalsIgnoreCase(AppConstant.RSBY_DATA_SOURCE_CONFIG)) {
                    rsbyDataModel = item1.getStatus();
                }
                if (item1.getConfigId().equalsIgnoreCase(AppConstant.VALIDATION_MODE_CONFIG)) {
                    validationModeModel = item1.getStatus();
                }
                if (item1.getConfigId().equalsIgnoreCase(AppConstant.PRINT_CARD)) {
                    printCardModel = item1.getStatus();
                }
                if (item1.getConfigId().equalsIgnoreCase(AppConstant.ADDITIONAL_SCHEME)) {
                    additionalSchemeModel = item1.getStatus();
                }
                if (item1.getConfigId().equalsIgnoreCase(AppConstant.AADHAR_AUTH)) {
                    aadharAuthModeModel = item1.getStatus();
                }
                if (item1.getConfigId().equalsIgnoreCase(AppConstant.DATA_DOWNLOAD)) {
                    dataDownloadingModeModel = item1.getStatus();
                }
                if (item1.getConfigId().equalsIgnoreCase(AppConstant.PHOTO_COLLECT)) {
                    photoCollectModeModel = item1.getStatus();
                }
                if (item1.getConfigId().equalsIgnoreCase(AppConstant.NOMINEE_COLLECT)) {
                    nomineeCollectModeModel = item1.getStatus();
                }
                if (item1.getConfigId().equalsIgnoreCase(AppConstant.SECC_DOWNLOAD)) {
                    seccDataDownloadModeModel = item1.getStatus();
                }
                if (item1.getConfigId().equalsIgnoreCase(AppConstant.APPLICATION_ZOOM)) {
                    zoomMode = item1.getStatus();
                }
                if (item1.getConfigId().equalsIgnoreCase(AppConstant.DATA_CATEGORY)) {
                    dataCategory = item1.getAcceptedvalueName();
                }
                if (item1.getConfigId().equalsIgnoreCase(AppConstant.EKYC_SOURCE_CONFIG)) {
                    ekycModel = item1.getStatus();
                }
                if (item1.getConfigId().equalsIgnoreCase(AppConstant.DEMOGRAPHIC_SOURCE_CONFIG)) {
                    demographicModel = item1.getStatus();
                }
            }

            dataCatogeryTV = (TextView) v.findViewById(R.id.dataCatogeryTV);
            dataCatogeryTV.setText(dataCategory);

            nomineeserveyModeTV = (TextView) v.findViewById(R.id.nomineeserveyModeTV);
            if (nomineeCollectModeModel != null && !nomineeCollectModeModel.equalsIgnoreCase("")) {
                // nomineeserveyModeTV.setText(nomineeCollectModeModel);
                if (nomineeCollectModeModel.equalsIgnoreCase("Y")) {
                    nomineeserveyModeTV.setText("Yes");
                } else {
                    nomineeserveyModeTV.setText("No");
                }
            }
            cardPrintModeTV = (TextView) v.findViewById(R.id.cardPrintModeTV);
            if (printCardModel != null && !printCardModel.equalsIgnoreCase("")) {
                // cardPrintModeTV.setText(printCardModel);
                if (printCardModel.equalsIgnoreCase("Y")) {
                    cardPrintModeTV.setText("Yes");
                } else {
                    cardPrintModeTV.setText("No");
                }
            }
            capturePhotoModeTV = (TextView) v.findViewById(R.id.capturePhotoModeTV);
            if (photoCollectModeModel != null && !photoCollectModeModel.equalsIgnoreCase("")) {
                // capturePhotoModeTV.setText(photoCollectModeModel);
                if (photoCollectModeModel.equalsIgnoreCase("Y")) {
                    capturePhotoModeTV.setText("Yes");
                } else {
                    capturePhotoModeTV.setText("No");
                }
            }

            identificationModeTV = (TextView) v.findViewById(R.id.identificationModeTV);
            if (validationModeModel != null && !validationModeModel.equalsIgnoreCase("")) {
                String mode;
                if (validationModeModel.equalsIgnoreCase("A")) {
                    mode = "Aadhar";
                } else if (validationModeModel.equalsIgnoreCase("G")) {
                    mode = "Goverment ID";
                } else {
                    mode = "Both(Aadhar/GovID)";
                }
                identificationModeTV.setText(mode);
            }

            aadharValidationModeTV = (TextView) v.findViewById(R.id.aadharValidationModeTV);
            if (aadharAuthModeModel != null && !aadharAuthModeModel.equalsIgnoreCase("")) {
                if (aadharAuthModeModel.equalsIgnoreCase("D")) {
                    aadharValidationModeTV.setText("Demo Auth");
                } else if (aadharAuthModeModel.equalsIgnoreCase("E")) {
                    aadharValidationModeTV.setText("Bio Auth");
                } else {
                    aadharValidationModeTV.setText("Both (Demo/Bio Auth)");
                }
            }
            ekycModeTV = (TextView) v.findViewById(R.id.ekycModeTV);
            if(ekycModel!=null && !ekycModel.equalsIgnoreCase("")){
                String mode="";
                if(ekycModel.contains("I")){
                    mode="Iris";
                }
                if(ekycModel.contains("F")){
                    mode="FingerPrint";
                }
                if(ekycModel.contains("O")){
                    mode="OTP";
                }
                if(ekycModel.contains("IF")){
                    mode="Iris/FingerPrint";
                }
                if(ekycModel.contains("IO")){
                    mode="Iris/OTP";
                }
                if(ekycModel.contains("FO")){
                    mode="FingerPrint/OTP";
                }
                if(ekycModel.contains("IFO")){
                    mode="Iris/FingerPrint/OTP";
                }
                ekycModeTV.setText(mode);
            }

            demoGraphicModeTV = (TextView) v.findViewById(R.id.demoGraphicModeTV);
            if(demographicModel!=null && !demographicModel.equalsIgnoreCase("")){
                String mode="";
                if(demographicModel.contains("Q")){
                    mode="QR Code";
                }
                if(demographicModel.contains("M")){
                    mode="Manual";
                }
                if(demographicModel.contains("QM")){
                    mode="QR Code/Manual";
                }

                demoGraphicModeTV.setText(mode);
            }
            seccDataTV = (TextView) v.findViewById(R.id.seccDataTV);
            if (seccDataDownloadModeModel != null && !seccDataDownloadModeModel.equalsIgnoreCase("")) {
                if (seccDataDownloadModeModel.equalsIgnoreCase("Y")) {
                    seccDataTV.setText("Yes");
                } else {
                    seccDataTV.setText("No");
                }
            }
            rsbyDataTV = (TextView) v.findViewById(R.id.rsbyDataTV);
            if (rsbyDataModel != null && !rsbyDataModel.equalsIgnoreCase("")) {
                if (rsbyDataModel.equalsIgnoreCase("Y")) {
                    rsbyDataTV.setText("Yes");
                } else {
                    rsbyDataTV.setText("No");
                }
            }
            surveyorDataTV = (TextView) v.findViewById(R.id.surveyorDataTV);
            if (dataDownloadingModeModel != null && !dataDownloadingModeModel.equalsIgnoreCase("")) {
                if (dataDownloadingModeModel.equalsIgnoreCase(AppConstant.VillageWiseDownloading)) {
                    surveyorDataTV.setText("Village wise");
                } else if (dataDownloadingModeModel.equalsIgnoreCase(AppConstant.EbWiseDownloading)) {
                    surveyorDataTV.setText("EB wise");
                } else if (dataDownloadingModeModel.equalsIgnoreCase(AppConstant.WardWiseDownloading)) {
                    surveyorDataTV.setText("Ward wise");
                } else if (dataDownloadingModeModel.equalsIgnoreCase(AppConstant.SubBlockWiseDownloading)) {
                    surveyorDataTV.setText("Sub EB wise");
                }
            }
          /*  identificationModeTV = (TextView) v.findViewById(R.id.identificationModeTV);
            if (aadharAuthModeModel != null && !aadharAuthModeModel.equalsIgnoreCase("")) {
                if (aadharAuthModeModel.equalsIgnoreCase("D")) {
                    identificationModeTV.setText("Demo Auth");
                } else if (aadharAuthModeModel.equalsIgnoreCase("E")) {
                    identificationModeTV.setText("Bio Auth");
                } else {
                    identificationModeTV.setText("Both (Demo/Bio Auth)");
                }
            }*/

            zoomModeTV = (TextView) v.findViewById(R.id.zoomModeTV);
            if (zoomMode != null && !zoomMode.equalsIgnoreCase("")) {
                if (zoomMode.equalsIgnoreCase("Y")) {
                    zoomModeTV.setText("Yes");
                } else if (zoomMode.equalsIgnoreCase("N")) {
                    zoomModeTV.setText("No");
                }
            }
        }


    }

    private void checkAppConfigToSetData() {
        StateItem selectedStateItem = StateItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SELECTED_STATE, context));
        ArrayList<ConfigurationItem> configList;
        if (selectedStateItem != null && selectedStateItem.getStateCode() != null) {
            configList = SeccDatabase.findConfiguration(selectedStateItem.getStateCode(), context);
        } else {
            configList = SeccDatabase.findConfiguration("24", context);
        }
        if (configList != null) {
            for (ConfigurationItem item1 : configList) {
                if (item1.getConfigId().equalsIgnoreCase(AppConstant.RSBY_DATA_SOURCE_CONFIG)) {
                    rsbyDataModel = item1.getStatus();
                }
                if (item1.getConfigId().equalsIgnoreCase(AppConstant.VALIDATION_MODE_CONFIG)) {
                    validationModeModel = item1.getStatus();
                }
                if (item1.getConfigId().equalsIgnoreCase(AppConstant.PRINT_CARD)) {
                    printCardModel = item1.getStatus();
                }
                if (item1.getConfigId().equalsIgnoreCase(AppConstant.ADDITIONAL_SCHEME)) {
                    additionalSchemeModel = item1.getStatus();
                }
                if (item1.getConfigId().equalsIgnoreCase(AppConstant.AADHAR_AUTH)) {
                    aadharAuthModeModel = item1.getStatus();
                }
                if (item1.getConfigId().equalsIgnoreCase(AppConstant.DATA_DOWNLOAD)) {
                    dataDownloadingModeModel = item1.getStatus();
                }
                if (item1.getConfigId().equalsIgnoreCase(AppConstant.PHOTO_COLLECT)) {
                    photoCollectModeModel = item1.getStatus();
                }
                if (item1.getConfigId().equalsIgnoreCase(AppConstant.NOMINEE_COLLECT)) {
                    nomineeCollectModeModel = item1.getStatus();
                }
                if (item1.getConfigId().equalsIgnoreCase(AppConstant.SECC_DOWNLOAD)) {
                    seccDataDownloadModeModel = item1.getStatus();
                }
                if (item1.getConfigId().equalsIgnoreCase(AppConstant.APPLICATION_ZOOM)) {
                    zoomMode = item1.getStatus();
                }
                if (item1.getConfigId().equalsIgnoreCase(AppConstant.DATA_CATEGORY)) {
                    dataCategory = item1.getAcceptedvalueName();
                }

            }
            dataCatogeryTV = (TextView) findViewById(R.id.dataCatogeryTV);
            dataCatogeryTV.setText(dataCategory);

            nomineeserveyModeTV = (TextView) findViewById(R.id.nomineeserveyModeTV);
            if (nomineeCollectModeModel != null && !nomineeCollectModeModel.equalsIgnoreCase("")) {
                // nomineeserveyModeTV.setText(nomineeCollectModeModel);
                if (nomineeCollectModeModel.equalsIgnoreCase("Y")) {
                    nomineeserveyModeTV.setText("Yes");
                } else {
                    nomineeserveyModeTV.setText("No");
                }
            }
            cardPrintModeTV = (TextView) findViewById(R.id.cardPrintModeTV);
            if (printCardModel != null && !printCardModel.equalsIgnoreCase("")) {
                // cardPrintModeTV.setText(printCardModel);
                if (printCardModel.equalsIgnoreCase("Y")) {
                    cardPrintModeTV.setText("Yes");
                } else {
                    cardPrintModeTV.setText("No");
                }
            }
            capturePhotoModeTV = (TextView) findViewById(R.id.capturePhotoModeTV);
            if (photoCollectModeModel != null && !photoCollectModeModel.equalsIgnoreCase("")) {
                // capturePhotoModeTV.setText(photoCollectModeModel);
                if (photoCollectModeModel.equalsIgnoreCase("Y")) {
                    capturePhotoModeTV.setText("Yes");
                } else {
                    capturePhotoModeTV.setText("No");
                }
            }
            aadharValidationModeTV = (TextView) findViewById(R.id.aadharValidationModeTV);
            if (validationModeModel != null && !validationModeModel.equalsIgnoreCase("")) {
                String mode;
                if (validationModeModel.equalsIgnoreCase("A")) {
                    mode = "Aadhar";
                } else if (validationModeModel.equalsIgnoreCase("G")) {
                    mode = "Goverment ID";
                } else {
                    mode = "Both(Aadhar/GovID)";
                }
                aadharValidationModeTV.setText(mode);
            }
            seccDataTV = (TextView) findViewById(R.id.seccDataTV);
            if (seccDataDownloadModeModel != null && !seccDataDownloadModeModel.equalsIgnoreCase("")) {
                if (seccDataDownloadModeModel.equalsIgnoreCase("Y")) {
                    seccDataTV.setText("Yes");
                } else {
                    seccDataTV.setText("No");
                }
            }
            rsbyDataTV = (TextView) findViewById(R.id.rsbyDataTV);
            if (rsbyDataModel != null && !rsbyDataModel.equalsIgnoreCase("")) {
                if (rsbyDataModel.equalsIgnoreCase("Y")) {
                    rsbyDataTV.setText("Yes");
                } else {
                    rsbyDataTV.setText("No");
                }
            }
            surveyorDataTV = (TextView) findViewById(R.id.surveyorDataTV);
            if (dataDownloadingModeModel != null && !dataDownloadingModeModel.equalsIgnoreCase("")) {
                if (dataDownloadingModeModel.equalsIgnoreCase(AppConstant.VillageWiseDownloading)) {
                    surveyorDataTV.setText("Village wise");
                } else if (dataDownloadingModeModel.equalsIgnoreCase(AppConstant.EbWiseDownloading)) {
                    surveyorDataTV.setText("EB wise");
                } else if (dataDownloadingModeModel.equalsIgnoreCase(AppConstant.WardWiseDownloading)) {
                    surveyorDataTV.setText("Ward wise");
                } else if (dataDownloadingModeModel.equalsIgnoreCase(AppConstant.SubBlockWiseDownloading)) {
                    surveyorDataTV.setText("Sub EB wise");
                }
            }
            identificationModeTV = (TextView) findViewById(R.id.identificationModeTV);
            if (aadharAuthModeModel != null && !aadharAuthModeModel.equalsIgnoreCase("")) {
                if (aadharAuthModeModel.equalsIgnoreCase("D")) {
                    identificationModeTV.setText("Demographic Auth");
                } else if (aadharAuthModeModel.equalsIgnoreCase("E")) {
                    identificationModeTV.setText("Biographic Auth");
                } else {
                    identificationModeTV.setText("Both (Demographic/Biographic Auth)");
                }
            }
            zoomModeTV = (TextView) findViewById(R.id.zoomModeTV);
            if (zoomMode != null && !zoomMode.equalsIgnoreCase("")) {
                if (zoomMode.equalsIgnoreCase("Y")) {
                    zoomModeTV.setText("Yes");
                } else if (zoomMode.equalsIgnoreCase("N")) {
                    zoomModeTV.setText("No");
                }
            }
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

    private String checkAppConfig() {
        verifierDetail = VerifierLoginResponse.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.VERIFIER_CONTENT, context));
        selectedStateItem = StateItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SELECTED_STATE, context));
        if (selectedStateItem != null && selectedStateItem.getStateCode() != null) {
            ArrayList<ConfigurationItem> configList = SeccDatabase.findConfiguration(selectedStateItem.getStateCode(), context);
            if (configList != null) {
                for (ConfigurationItem item1 : configList) {

                    if (item1.getConfigId().equalsIgnoreCase(AppConstant.APPLICATION_ZOOM)) {
                        zoomMode = item1.getStatus();
                    }

                }
            }
        }
        return null;
    }


    private void prepareBlockForDownloaddList() {
        if (verifierDetail.getLocationList() != null && verifierDetail.getLocationList().size() > 0) {
            ArrayList<VerifierLocationItem> dowloadedBlockList = new ArrayList<>();
            VerifierLocationItem locItem = null;
            if (dataDownloadingModeModel.equalsIgnoreCase(AppConstant.EbWiseDownloading)) {
                for (VerifierLocationItem item : verifierDetail.getLocationList()) {
                    ArrayList<HouseHoldItem> householdList = SeccDatabase.getHouseHoldList(
                            item.getStateCode().trim(), item.getDistrictCode().trim()
                            , item.getTehsilCode().trim(), item.getVtCode().trim(), item.getWardCode().trim(), item.getBlockCode().trim(), context);
                    AppUtility.showLog(AppConstant.LOG_STATUS, "", " Household Size : " + householdList.size());


                    if (householdList.size() > 0) {
                        dowloadedBlockList.add(item);
                    }


                }
            }
            if (dataDownloadingModeModel.equalsIgnoreCase(AppConstant.VillageWiseDownloading)) {
                VerifierLocationItem item0 = verifierDetail.getLocationList().get(0);
                ArrayList<HouseHoldItem> householdList = SeccDatabase.getHouseHoldVillageWiseList(
                        item0.getStateCode().trim(), item0.getDistrictCode().trim()
                        , item0.getTehsilCode().trim(), item0.getVtCode().trim().trim(), context);
                if (householdList != null && householdList.size() > 0) {
                    dowloadedBlockList.add(item0);
                }
            } else if (dataDownloadingModeModel.equalsIgnoreCase(AppConstant.WardWiseDownloading)) {
                VerifierLocationItem item0 = verifierDetail.getLocationList().get(0);
                ArrayList<HouseHoldItem> householdList = SeccDatabase.getAllHouseHoldWardWiseList(
                        item0.getStateCode().trim(), item0.getDistrictCode().trim()
                        , item0.getTehsilCode().trim(), item0.getVtCode().trim(), item0.getWardCode().trim(), context);
                if (householdList != null && householdList.size() > 0) {
                    dowloadedBlockList.add(item0);
                }
            }

            if (dowloadedBlockList != null && dowloadedBlockList.size() > 0) {
                downloadLocationLayout.setVisibility(View.VISIBLE);
                CustomAdapter adapter = new CustomAdapter(dowloadedBlockList);
                profileDownloadedLocation.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }else{
                downloadLocationLayout.setVisibility(View.GONE);
                profileDownloadedLocation.setVisibility(View.GONE);
            }
        }
    }


    private class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
        private ArrayList<VerifierLocationItem> mDataset;

        public class ViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case

            TextView blockCodeTV, noOfHouseholdTV, noOfMemberTV, syncedMemberTV, totalSyncedMemberTV;
            TextView stateNameTV, distTV, tehsilTV, vtNameTV, wardTV, ebTV, subEbTV;
            LinearLayout subEbLinearLayout, ebLinearLayout, wardLinearLayout;


            // AppUtility.showLog(AppConstant.LOG_STATUS,"Block Activity"," State Name : "+item.getStateName());


            Button proceedBT;

            public ViewHolder(View v) {
                super(v);


                wardLinearLayout = (LinearLayout) v.findViewById(R.id.wardLinearLayout);
                ebLinearLayout = (LinearLayout) v.findViewById(R.id.ebLinearLayout);
                subEbLinearLayout = (LinearLayout) v.findViewById(R.id.subEbLinearLayout);
                blockCodeTV = (TextView) v.findViewById(R.id.blockCodeTV);
                // blockCodeTV.setPaintFlags(blockCodeTV.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                noOfHouseholdTV = (TextView) v.findViewById(R.id.totalHouseholdTV);
                noOfMemberTV = (TextView) v.findViewById(R.id.totalMemberTV);
                syncedMemberTV = (TextView) v.findViewById(R.id.syncedMemberTV);
                totalSyncedMemberTV = (TextView) v.findViewById(R.id.totalSyncMemberTV);
                proceedBT = (Button) v.findViewById(R.id.proceedBT);

                stateNameTV = (TextView) v.findViewById(R.id.stateNameTV);
                distTV = (TextView) v.findViewById(R.id.distNameTV);
                tehsilTV = (TextView) v.findViewById(R.id.tehsilNameTV);
                vtNameTV = (TextView) v.findViewById(R.id.vtNameTV);
                wardTV = (TextView) v.findViewById(R.id.wardCodeTV);
                ebTV = (TextView) v.findViewById(R.id.ebTV);
                subEbTV = (TextView) v.findViewById(R.id.subEbTV);
                //noOfHouseholdTV.setPaintFlags(noOfHouseholdTV.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
              /*  v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position=getPosition();
                      //  CustomAlert.alertWithOk(context,"Under Development..");
                       openSearchScreen(mDataset.get(position));
                    }
                });*/

            }
        }


        public void add(int position, VerifierLocationItem item) {
            mDataset.add(position, item);
            notifyItemInserted(position);
        }

        public void remove(String item) {
            int position = mDataset.indexOf(item);
            mDataset.remove(position);
            notifyItemRemoved(position);
        }

        public void updateData(ArrayList<VerifierLocationItem> itemList) {
            mDataset.clear();
            mDataset.addAll(itemList);
            notifyDataSetChanged();
        }

        // Provide a suitable constructor (depends on the kind of dataset)
        public CustomAdapter(ArrayList<VerifierLocationItem> myDataset) {
            mDataset = myDataset;
         /*   memberListResponse=SeccMemberResponse.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                    AppConstant.SECC_MEMBER_CONTENT,context));
            Log.d(TAG, "Adapter : " + mDataset.size());*/
        }

        // Create new1 views (invoked by the layout manager)
        @Override
        public CustomAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
            // create a new1 view
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.block_list_item, parent, false);
            // set the view's size, margins, paddings and layout parameters
            CustomAdapter.ViewHolder vh = new CustomAdapter.ViewHolder(v);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(CustomAdapter.ViewHolder holder, int position) {
            final VerifierLocationItem loc = mDataset.get(position);

            //  holder.blockCodeTV.setText(loc.getBlockCode());
         /*   holder.noOfHouseholdTV.setText(SeccDatabase.houseHoldCount(context,loc.getStateCode(),
                    loc.getDistrictCode(),loc.getTehsilCode(),loc.getVtCode(),loc.getWardCode(),loc.getBlockCode())+"");
            holder.noOfMemberTV.setText(SeccDatabase.seccMemberCount(context,loc.getStateCode(),
                    loc.getDistrictCode(),loc.getTehsilCode(),loc.getVtCode(),loc.getWardCode(),loc.getBlockCode())+"");
            holder.syncedMemberTV.setText(SeccDatabase.countSyncedHousehold(context,loc.getStateCode(),
                    loc.getDistrictCode(),loc.getTehsilCode(),loc.getVtCode(),loc.getWardCode(),loc.getBlockCode(),AppConstant.SYNCED_STATUS+"")+"");
            holder.totalSyncedMemberTV.setText(SeccDatabase.countSyncedMember(context,AppConstant.SYNCED_STATUS+"")+"");

*/
            if (dataDownloadingModeModel != null && !dataDownloadingModeModel.equalsIgnoreCase("")) {
                if (dataDownloadingModeModel.equalsIgnoreCase(AppConstant.VillageWiseDownloading)) {
                    holder.wardLinearLayout.setVisibility(View.GONE);
                    holder.ebLinearLayout.setVisibility(View.GONE);
                    holder.subEbLinearLayout.setVisibility(View.GONE);
                } else if (dataDownloadingModeModel.equalsIgnoreCase(AppConstant.EbWiseDownloading)) {

                    holder.subEbLinearLayout.setVisibility(View.GONE);

                } else if (dataDownloadingModeModel.equalsIgnoreCase(AppConstant.WardWiseDownloading)) {


                    holder.ebLinearLayout.setVisibility(View.GONE);
                    holder.subEbLinearLayout.setVisibility(View.GONE);
                } else if (dataDownloadingModeModel.equalsIgnoreCase(AppConstant.SubBlockWiseDownloading)) {

                }
            }

            if (loc != null) {
                if (loc.getStateName() != null) {
                    holder.stateNameTV.setText(loc.getStateName());
                }
                if (loc.getDistrictName() != null) {
                    holder.distTV.setText(loc.getDistrictName());
                }
                if (loc.getTehsilName() != null) {
                    holder.tehsilTV.setText(loc.getTehsilName());
                }
                if (loc.getVtName() != null) {
                    holder.vtNameTV.setText(loc.getVtName());
                }
                if (loc.getWardCode() != null) {
                    holder.wardTV.setText(loc.getWardCode());
                }
                if (loc.getBlockCode() != null) {
                    holder.ebTV.setText(loc.getBlockCode());
                }

                if (loc.getSubBlockcode() != null) {
                    holder.subEbTV.setText(loc.getSubBlockcode());
                }


            }
            holder.proceedBT.setVisibility(View.GONE);/*setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //int position=getPosition();
                    proceedDailog(loc);
                }
            });*/
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return mDataset.size();
        }
    }

    private class NotificationAdapter extends
            RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {
        private ArrayList<NotificationModel> dataSet;

        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView descriptionTV, stateCodeTV, createdDateTV, expiryDateTV;

            public MyViewHolder(View v) {
                super(v);

                descriptionTV = (TextView) v.findViewById(R.id.descriptionTV);
                stateCodeTV = (TextView) v.findViewById(R.id.stateCodeTV);
                createdDateTV = (TextView) v.findViewById(R.id.createdDateTV);
                expiryDateTV = (TextView) v.findViewById(R.id.expiryDateTV);

            }

        }

        public void addAll(List<NotificationModel> list) {

            dataSet.addAll(list);
            notifyDataSetChanged();
        }

        public NotificationAdapter(ArrayList<NotificationModel> data) {
            this.dataSet = data;

        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.profile_notification_item, parent, false);
            //view.setOnClickListener(MainActivity.myOnClickListener);
            MyViewHolder myViewHolder = new MyViewHolder(view);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {
            final NotificationModel item = dataSet.get(listPosition);
            holder.descriptionTV.setText(item.getDescription());
            holder.stateCodeTV.setText(item.getTargetState());
            String createddate = DateTimeUtil.convertTimeMillisIntoStringDateNew(Long.valueOf(item.getCreatedDate()));
            holder.createdDateTV.setText(createddate.replace("00:00:00", ""));
            String expiryDate = DateTimeUtil.convertTimeMillisIntoStringDateNew(Long.valueOf(item.getDateExpire()));
            holder.expiryDateTV.setText(expiryDate.replace("00:00:00", ""));
        }

        @Override
        public int getItemCount() {
            return dataSet.size();
        }

        public void clearDataSource() {
            dataSet.clear();
            notifyDataSetChanged();
        }
    }
}
