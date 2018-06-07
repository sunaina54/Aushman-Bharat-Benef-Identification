package com.nhpm.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.customComponent.CustomAlert;
import com.nhpm.CameraUtils.CommonUtilsImageCompression;
import com.nhpm.CameraUtils.squarecamera.CameraActivity;
import com.nhpm.LocalDataBase.DatabaseHelpers;
import com.nhpm.Models.FamilyMemberModel;
import com.nhpm.Models.request.FamilyDetailsItemModel;
import com.nhpm.Models.request.PersonalDetailItem;
import com.nhpm.Models.response.DocsListItem;
import com.nhpm.Models.response.GovernmentIdItem;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;
import com.nhpm.Utility.AppUtility;
import com.nhpm.activity.CollectDataActivity;
import com.nhpm.activity.FamilyMemberEntryActivity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FamilyDetailsFragment extends Fragment {
    private View view;
    private FragmentTransaction fragmentTransection;
    private FragmentManager fragmentManager;
    private FamilyAdapter adapter;
    private Context context;
    private ArrayList<GovernmentIdItem> govtIdStatusList;
    private Spinner govtIdSP;
    private TextView beneficiaryNameTV;
    private GovernmentIdItem item;
    private Button captureImageBT;
    private Bitmap captureImageBM;
    private CollectDataActivity activity;
    private DocsListItem beneficiaryListItem;
    private String voterIdImg;
    private ImageView beneficiaryPhotoIV;
    private int CAMERA_PIC_REQUEST = 0;
    private Button previousBT;
    private ImageView addIV;
    private PersonalDetailItem personalDetailItem;
    private LinearLayout addFamilyMemberLL;
    private RecyclerView memberRecycle;
    private ArrayList<FamilyMemberModel> familyMembersList;
    private FamilyDetailsItemModel familyDetailsItemModel;
    private EditText govtIdET;


    public FamilyDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_family_details, container, false);
        context = getActivity();
        setupScreen(view);
        return view;

    }

    private void setupScreen(View view) {
        Bundle bundle = getArguments();
        //bundle.getString("personalDetail");
        personalDetailItem = PersonalDetailItem.create(bundle.getString("personalDetail"));
        fragmentManager = getActivity().getSupportFragmentManager();
        familyMembersList = new ArrayList<>();
        govtIdSP = (Spinner) view.findViewById(R.id.govtIdSP);
        govtIdET = (EditText) view.findViewById(R.id.govtIdET);
        prepareGovernmentIdSpinner();
        memberRecycle = (RecyclerView) view.findViewById(R.id.memberRecycle);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        memberRecycle.setLayoutManager(layoutManager);
        memberRecycle.setItemAnimator(new DefaultItemAnimator());
        addFamilyMemberLL = (LinearLayout) view.findViewById(R.id.addFamilyMemberLL);
        beneficiaryNameTV = (TextView) view.findViewById(R.id.beneficiaryNameTV);
        captureImageBT = (Button) view.findViewById(R.id.captureImageBT);
        beneficiaryPhotoIV = (ImageView) view.findViewById(R.id.beneficiaryPhotoIV);
        captureImageBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
            }
        });

        if (personalDetailItem != null) {

            if (personalDetailItem.getBenefName() != null) {
                beneficiaryNameTV.setText(personalDetailItem.getBenefName());
            }
            if(personalDetailItem.getFamilyDetailsItem()!=null){
                if(personalDetailItem.getFamilyDetailsItem().getIdImage()!=null &&
                        !personalDetailItem.getFamilyDetailsItem().getIdImage().equalsIgnoreCase("") ){
                    //updateScreen(personalDetailItem.getFamilyDetailsItem().getIdImage());
                    beneficiaryPhotoIV.setImageBitmap(AppUtility.convertStringToBitmap(personalDetailItem.getFamilyDetailsItem().getIdImage()));

                }

                if(personalDetailItem.getFamilyDetailsItem().getIdType()!=null &&
                        personalDetailItem.getFamilyDetailsItem().getIdType().equalsIgnoreCase("")){
                    //govtIdSP.setSelection();
                }

                if(personalDetailItem.getFamilyDetailsItem().getIdNumber()!=null &&
                        !personalDetailItem.getFamilyDetailsItem().getIdNumber().equalsIgnoreCase("")){
                    govtIdET.setText(personalDetailItem.getFamilyDetailsItem().getIdNumber());
                }

                if(personalDetailItem.getFamilyDetailsItem().getFamilyMemberModels()!=null){
                  refreshList(personalDetailItem.getFamilyDetailsItem().getFamilyMemberModels());
                }

            }
        }
        addIV = (ImageView) view.findViewById(R.id.addIV);
        addIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomAlert.alertWithOk(context, "Under Development");
            }
        });
        previousBT = (Button) view.findViewById(R.id.previousBT);
        previousBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new PersonalDetailsFragment();
                Bundle args = new Bundle();
                familyDetailsItemModel = new FamilyDetailsItemModel();

                familyDetailsItemModel.setIdNumber(govtIdET.getText().toString());
                familyDetailsItemModel.setIdType(item.status);
                familyDetailsItemModel.setIdImage(voterIdImg);
                familyDetailsItemModel.setFamilyMemberModels(familyMembersList);
                personalDetailItem.setFamilyDetailsItem(familyDetailsItemModel);
                if (personalDetailItem != null) {
                    args.putString("personalDetail", personalDetailItem.serialize());
                }
                fragment.setArguments(args);
                fragmentTransection = fragmentManager.beginTransaction();
                fragmentTransection.add(R.id.fragContainer, fragment);
                fragmentTransection.commitAllowingStateLoss();
            }
        });

        govtIdSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // AadhaarStatusItem item=aadhaarStatusList.get(position);
                item = govtIdStatusList.get(position);
                // AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Setup screen Selected id" + item.statusCode);
                switch (item.statusCode) {

                    /*case NO_GOVID:
                        voterIdLayout.setVisibility(View.GONE);
                        govtIdPhotoLayout.setVisibility(View.GONE);

                        break;

                    case 0:
                        voterIdLayout.setVisibility(View.GONE);
                        govtIdPhotoLayout.setVisibility(View.GONE);
                        *//*voterIdCardNumberET.setText("");
                        voterIdCardNameET.setText("");*//*
                        //aadhaarStatus="";
                        // voterIdImg=null;
                        //  updateScreen(voterIdImg);
                        break;
                    case ENROLLMENT_ID:
                        voterIdCardNameET.setEnabled(true);

                        voterIdImg = null;
//                        updateScreen(voterIdImg);
                        voterIdCardNumberET.requestFocus();
                        AppUtility.showSoftInput(activity);
                        voterIdLayout.setVisibility(View.VISIBLE);
                        voterIdCaptureLayout.setVisibility(View.VISIBLE);
                        govtIdPhotoLayout.setVisibility(View.VISIBLE);
                        *//*voterIdCardNumberET.setText("");
                        voterIdCardNameET.setText("");*//*
                        //updateScreen(voterIdImg);
                        voterIdCardNumberET.setHint(context.getResources().getString(R.string.enter24digitEid));
                        voterIdCardNameET.setHint(context.getResources().getString(R.string.enterNameInEid));
                        // voterIdCardNameET.setText(seccItem.getName());
                        voterIdCardNameET.setFilters(new InputFilter[]{new InputFilter.LengthFilter(24)});
                        if (seccItem!=null && seccItem.getIdType() != null && seccItem.getIdType().equalsIgnoreCase(ENROLLMENT_ID + "")) {
                            if (seccItem.getIdNo() != null && !seccItem.getIdNo().equalsIgnoreCase("")) {
                                voterIdCardNameET.setText(seccItem.getNameAsId());
                                voterIdCardNumberET.setText(seccItem.getIdNo());
                            } else {
                                voterIdCardNameET.setText(seccItem.getName());
                            }
                        }
                        updateScreen(voterIdImg);
                       *//* rationCardLayout.setVisibility(View.GONE);
                        enrollmentLayout.setVisibility(View.VISIBLE);*//*
                        //aadhaarStatus="1";
                        break;
                    case VOTER_ID:
                        AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Setup screen Selected id");
                        voterIdImg = null;
                        voterIdCardNameET.setEnabled(true);

                        voterIdLayout.setVisibility(View.VISIBLE);
                        voterIdCaptureLayout.setVisibility(View.VISIBLE);
                        govtIdPhotoLayout.setVisibility(View.VISIBLE);
                        voterIdCardNumberET.requestFocus();
                        voterIdCardNumberET.setText("");
                        voterIdCardNameET.setText("");
//                        updateScreen(voterIdImg);
                        voterIdCardNumberET.setHint(context.getResources().getString(R.string.plzEnterVoterIdNum));
                        voterIdCardNameET.setHint(context.getResources().getString(R.string.plzEnterNameAsVoterId));


                        //  voterIdCardNameET.setText(seccItem.getName());
                       *//* rationCardLayout.setVisibility(View.GONE);
                        enrollmentLayout.setVisibility(View.GONE);*//*
                        // aadhaarStatus="2";
                        if (seccItem!=null && seccItem.getIdType() != null && seccItem.getIdType().equalsIgnoreCase(VOTER_ID + "")) {
                            voterIdImg = seccItem.getGovtIdPhoto();
                            if (seccItem.getIdNo() != null && !seccItem.getIdNo().equalsIgnoreCase("")) {
                                voterIdCardNameET.setText(seccItem.getNameAsId());
                                voterIdCardNumberET.setText(seccItem.getIdNo());
                            } else {
                                voterIdCardNameET.setText(seccItem.getName());
                            }
                            voterIdCardNumberET.requestFocus();
                            AppUtility.showSoftInput(activity);
                        }
                        updateScreen(voterIdImg);


                        break;
                    case RASHAN_CARD:
                        voterIdImg = null;
                        voterIdCardNameET.setEnabled(true);

                        voterIdLayout.setVisibility(View.VISIBLE);
                        voterIdCaptureLayout.setVisibility(View.VISIBLE);
                        govtIdPhotoLayout.setVisibility(View.VISIBLE);
//                        updateScreen(voterIdImg);
                        voterIdCardNumberET.requestFocus();
                        AppUtility.showSoftInput(activity);
                        voterIdCardNumberET.setText("");
                        voterIdCardNameET.setText("");
                        voterIdCardNumberET.setHint(context.getResources().getString(R.string.plzEnterRationCardNum));
                        voterIdCardNameET.setHint(context.getResources().getString(R.string.plzEnterNameAsRationCard));
//                        voterIdCardNameET.setText(seccItem.getName());
                       *//* rationCardLayout.setVisibility(View.VISIBLE);
                        enrollmentLayout.setVisibility(View.GONE);*//*
                        //aadhaarStatus="3";
                        if (seccItem!=null && seccItem.getIdType() != null && seccItem.getIdType().equalsIgnoreCase(RASHAN_CARD + "")) {
                            voterIdImg = seccItem.getGovtIdPhoto();
                            if (seccItem.getIdNo() != null && !seccItem.getIdNo().equalsIgnoreCase("")) {
                                voterIdCardNameET.setText(seccItem.getNameAsId());
                                voterIdCardNumberET.setText(seccItem.getIdNo());
                            } else {
                                voterIdCardNameET.setText(seccItem.getName());
                            }
                        }
                        updateScreen(voterIdImg);

                        break;
                    case NREGA:
                        voterIdImg = null;
                        voterIdCardNameET.setEnabled(true);

//                        updateScreen(voterIdImg);
                        voterIdLayout.setVisibility(View.VISIBLE);
                        voterIdCaptureLayout.setVisibility(View.VISIBLE);
                        govtIdPhotoLayout.setVisibility(View.VISIBLE);
                        voterIdCardNumberET.setText("");
                        voterIdCardNameET.setText("");
                        voterIdCardNumberET.setHint(context.getResources().getString(R.string.plzEnterNaregaNum));
                        voterIdCardNameET.setHint(context.getResources().getString(R.string.plzEnterNameAsNarega));
                        // voterIdCardNameET.setText(seccItem.getName());
                        voterIdCardNumberET.requestFocus();
                        AppUtility.showSoftInput(activity);
                        if (seccItem!=null && seccItem.getIdType() != null && seccItem.getIdType().equalsIgnoreCase(NREGA + "")) {
                            voterIdImg = seccItem.getGovtIdPhoto();
                            if (seccItem.getIdNo() != null && !seccItem.getIdNo().equalsIgnoreCase("")) {
                                voterIdCardNameET.setText(seccItem.getNameAsId());
                                voterIdCardNumberET.setText(seccItem.getIdNo());
                            } else {
                                voterIdCardNameET.setText(seccItem.getName());
                            }
                        }
                        updateScreen(voterIdImg);

                        break;
                    case DRIVIG_LICENCE:
                        voterIdImg = null;
//                        updateScreen(voterIdImg);
                        voterIdCardNameET.setEnabled(true);

                        voterIdLayout.setVisibility(View.VISIBLE);
                        voterIdCaptureLayout.setVisibility(View.VISIBLE);
                        govtIdPhotoLayout.setVisibility(View.VISIBLE);
                        voterIdCardNumberET.requestFocus();
                        AppUtility.showSoftInput(activity);
                        *//*voterIdCardNumberET.setText("");

                        voterIdCardNameET.setText("");*//*
                        voterIdCardNumberET.setText("");
                        voterIdCardNameET.setText("");
                        voterIdCardNumberET.setHint(context.getResources().getString(R.string.plzEnterDrivingNum));
                        voterIdCardNameET.setHint(context.getResources().getString(R.string.plzEnterNameAsDriving));
                        //  voterIdCardNameET.setText(seccItem.getName());

                        if (seccItem!=null && seccItem.getIdType() != null && seccItem.getIdType().equalsIgnoreCase(DRIVIG_LICENCE + "")) {
                            voterIdImg = seccItem.getGovtIdPhoto();
                            if (seccItem.getIdType() != null && seccItem.getIdNo() != null && !seccItem.getIdNo().equalsIgnoreCase("")) {
                                voterIdCardNameET.setText(seccItem.getNameAsId());
                                voterIdCardNumberET.setText(seccItem.getIdNo());
                            } else {
                                voterIdCardNameET.setText(seccItem.getName());
                            }
                        }
                        updateScreen(voterIdImg);

                        break;
                    case BIRTH_CERT:
                        voterIdImg = null;
//                        updateScreen(voterIdImg);
                        voterIdCardNameET.setEnabled(true);

                        voterIdLayout.setVisibility(View.VISIBLE);
                        govtIdPhotoLayout.setVisibility(View.VISIBLE);
                        voterIdCaptureLayout.setVisibility(View.VISIBLE);
                        voterIdCardNumberET.setText("");
                        voterIdCardNameET.setText("");
                        voterIdCardNumberET.setText("");
                        voterIdCardNameET.setText("");
                        voterIdCardNumberET.requestFocus();
                        AppUtility.showSoftInput(activity);
                        voterIdCardNumberET.setHint(context.getResources().getString(R.string.plzEnterBirthCerfNum));
                        voterIdCardNameET.setHint(context.getResources().getString(R.string.plzEnterNameAsBirthCerf));
                        //voterIdCardNameET.setText(seccItem.getName());
                        if (seccItem!=null && seccItem.getIdType() != null && seccItem.getIdType().equalsIgnoreCase(BIRTH_CERT + "")) {
                            voterIdImg = seccItem.getGovtIdPhoto();
                            if (seccItem.getIdNo() != null && !seccItem.getIdNo().equalsIgnoreCase("")) {
                                voterIdCardNameET.setText(seccItem.getNameAsId());
                                voterIdCardNumberET.setText(seccItem.getIdNo());
                            } else {
                                voterIdCardNameET.setText(seccItem.getName());
                            }
                        }
                        updateScreen(voterIdImg);

                        break;
                    case OTHER_CARD:

                        voterIdImg = null;
//                        updateScreen(voterIdImg);
                        voterIdLayout.setVisibility(View.VISIBLE);
                        voterIdCaptureLayout.setVisibility(View.VISIBLE);
                        govtIdPhotoLayout.setVisibility(View.VISIBLE);
                        voterIdCardNumberET.setText("");
                        voterIdCardNameET.setText("");
//                        voterIdCardNumberET.setText("");
//                        voterIdCardNameET.setText("");
                        voterIdCardNameET.setEnabled(true);
                        voterIdCardNumberET.requestFocus();
                        AppUtility.showSoftInput(activity);
                        voterIdCardNumberET.setHint(context.getResources().getString(R.string.plzEnterId));
                        voterIdCardNameET.setHint(context.getResources().getString(R.string.plzEnterNameAsId));
                        //  voterIdCardNameET.setText(seccItem.getName());
                        if (seccItem!=null && seccItem.getIdType() != null && seccItem.getIdType().equalsIgnoreCase(OTHER_CARD + "")) {
                            voterIdImg = seccItem.getGovtIdPhoto();
                            if (seccItem.getIdNo() != null && !seccItem.getIdNo().equalsIgnoreCase("")) {
                                voterIdCardNameET.setText(seccItem.getNameAsId());
                                voterIdCardNumberET.setText(seccItem.getIdNo());
                            } else {
                                voterIdCardNameET.setText(seccItem.getName());
                            }
                        }
                        updateScreen(voterIdImg);

                        break;

                    case ID_NO_PHOTO:
                        voterIdImg = null;
//                        updateScreen(voterIdImg);
                        voterIdCardNameET.setEnabled(true);
                        voterIdLayout.setVisibility(View.VISIBLE);
                        govtIdPhotoLayout.setVisibility(View.VISIBLE);
                        //  preparedItem.setAadhaarSurveyedStat(item.getAadhaarSurveyedStat());
                        //  preparedItem.setAadhaarSurveyedStat(item.getAadhaarSurveyedStat());
//                        voterIdCaptureLayout.setVisibility(View.GONE);
                        voterIdCaptureLayout.setVisibility(View.VISIBLE);
                        voterIdCardNumberET.setText("");

                        voterIdCardNameET.setText("");
//                        voterIdCardNumberET.setText("");
//                        voterIdCardNameET.setText("");
                        voterIdCardNameET.setEnabled(true);
                        selectedIdType = ID_NO_PHOTO;
                        voterIdCardNumberET.requestFocus();
                        AppUtility.showSoftInput(activity);
                        voterIdCardNumberET.setHint(context.getResources().getString(R.string.plzEnterId));
                        voterIdCardNameET.setHint(context.getResources().getString(R.string.plzEnterNameAsId));
                        // voterIdCardNameET.setText(seccItem.getName());
                        if (seccItem !=null && seccItem.getIdType() != null && seccItem.getIdType().equalsIgnoreCase(ID_NO_PHOTO + "")) {
                            voterIdImg = seccItem.getGovtIdPhoto();
                            if (seccItem.getIdNo() != null && !seccItem.getIdNo().equalsIgnoreCase("")) {
                                voterIdCardNameET.setText(seccItem.getNameAsId());
                                voterIdCardNumberET.setText(seccItem.getIdNo());
                            } else {
                                voterIdCardNameET.setText(seccItem.getName());
                            }
                        }
                        updateScreen(voterIdImg);*/

                    // break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        addFamilyMemberLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // FamilyMemberModel item = null;

                Intent theIntent = new Intent(context, FamilyMemberEntryActivity.class);
                theIntent.putExtra(AppConstant.FAMILY_MEMBER_RESULT_CODE_NAME, item);
                startActivityForResult(theIntent, AppConstant.FAMILY_MEMBER_REQUEST_CODE_VALUE);
            }
        });

    }

    private void prepareGovernmentIdSpinner() {
        govtIdStatusList = AppUtility.prepareGovernmentIdSpinnerList();
        ArrayList<String> spinnerList = new ArrayList<>();
        for (GovernmentIdItem item : govtIdStatusList) {
            spinnerList.add(item.status);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.custom_drop_down, R.id.textView, spinnerList);
        govtIdSP.setAdapter(adapter);


        if(personalDetailItem!=null && personalDetailItem.getFamilyDetailsItem()!=null) {

               govtIdSP.setSelection(1);
        }
    }

    private void openCamera() {
        AppUtility.capturingType = AppConstant.capturingModeGovId;

        File mediaStorageDir = new File(
                DatabaseHelpers.DELETE_FOLDER_PATH,
                context.getString(R.string.squarecamera__app_name) + "/photoCapture"
        );

        if (mediaStorageDir.exists()) {
            deleteDir(mediaStorageDir);
        }
        Intent startCustomCameraIntent = new Intent(context, CameraActivity.class);
        startActivityForResult(startCustomCameraIntent, CAMERA_PIC_REQUEST);
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        //activity = (CaptureAadharDetailActivity) context;
        // if (activity instanceof CollectDataActivity) {
        activity = (CollectDataActivity) context;

        beneficiaryListItem = activity.benefItem;
        // }
       /* if (ekycActivity instanceof EkycActivity) {
            ekycActivity = (EkycActivity) context;
        }*/
    }

    private void deleteDir(File file) {

        if (file.isDirectory()) {
            String[] children = file.list();
            for (String child : children) {
                if (child.endsWith(".jpg") || child.endsWith(".jpeg"))
                    new File(file, child).delete();
            }
            file.delete();
        }
        Intent mediaScannerIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri fileContentUri = Uri.fromFile(file);
        mediaScannerIntent.setData(fileContentUri);
        context.sendBroadcast(mediaScannerIntent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_PIC_REQUEST) {
               /* Uri imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),"govtIdPhoto" +".jpg"));
                try {
                    captureImageBM = MediaStore.Images.Media.getBitmap(this.getContentResolver(),imageUri); //(Bitmap)imageUri;//data.getExtras().get("data");
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
            Uri fileUri = Uri.fromFile(new File(DatabaseHelpers.DELETE_FOLDER_PATH,
                    context.getString(R.string.squarecamera__app_name) + "/photoCapture/IMG_12345.jpg"));
            Uri compressedUri = Uri.fromFile(new File(CommonUtilsImageCompression.compressImage(fileUri.getPath(), context, "/photoCapture")));
            //  captureImageBM=(Bitmap)data.getExtras().get("data");
            try {
                captureImageBM = MediaStore.Images.Media.getBitmap(context.getContentResolver(), compressedUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Log.d(TAG," Bitmap Size : "+image.getAllocationByteCount());
            voterIdImg = AppUtility.convertBitmapToString(captureImageBM);
            updateScreen(voterIdImg);
        }

        if (requestCode == AppConstant.FAMILY_MEMBER_REQUEST_CODE_VALUE) {
            if (resultCode == Activity.RESULT_OK) {

                // Utility.scrollToEnd(scrollView);
                //  Toast.makeText(context, "Result Recieved", Toast.LENGTH_LONG).show();
                FamilyMemberModel item = (FamilyMemberModel) data.getSerializableExtra(AppConstant.FAMILY_MEMBER_RESULT_CODE_NAME);
                if (item != null) {
                    // if(familyMembersList.size()>0){
                  /*  for(int i=0;i<familyMembersList.size();i++){
                        if(item.getMemberId()!=null && item.getMemberId().equalsIgnoreCase(familyMembersList.get(i).getMemberId())){
                            familyMembersList.set(i,item);
                            refreshList();
                            return;
                        }
                    }*/
                    familyMembersList.add(item);
                    refreshList(familyMembersList);

                    // }else{
                    //item.setHouseholdId();
                    //  }
                }
                refreshList(familyMembersList);
            }
        }
    }

    private void refreshList(ArrayList<FamilyMemberModel> familyMembersList) {
        if (familyMembersList != null) {
            adapter = new FamilyAdapter(context, familyMembersList);
            memberRecycle.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    private class FamilyAdapter extends RecyclerView.Adapter<FamilyAdapter.MyViewHolder> {

        View view;
        private ArrayList<FamilyMemberModel> dataSet;


        public class MyViewHolder extends RecyclerView.ViewHolder {
            RelativeLayout itemlayout, editActionRL;
            TextView nameTV;


            public MyViewHolder(final View itemView) {
                super(itemView);
                nameTV = (TextView) itemView.findViewById(R.id.nameTV);
            }
        }


        public FamilyAdapter(Context context, ArrayList<FamilyMemberModel> data) {
            this.dataSet = data;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.member_data_row, parent, false);
            MyViewHolder myViewHolder = new MyViewHolder(view);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {


            final FamilyMemberModel item = dataSet.get(listPosition);
           /* String aadhaarNo = "";
            holder.houseHoldIdTV.setText(item.getHouseholdId());
            if(item.getAadhaarNo()!=null && !item.getAadhaarNo().equalsIgnoreCase("")) {
                aadhaarNo = "XXXXXXXX" + item.getAadhaarNo().substring(8);
            }
            holder.houseHoldAadhaarNoTV.setText(aadhaarNo);*/
            holder.nameTV.setText(item.getName());
            /*if(item.getStatus().equalsIgnoreCase(AppConstant.SYNC_STATUS)){
                holder.editActionTV.setBackgroundColor(context.getResources().getColor(R.color.sync_status_color));
                holder.editActionTV.setText("Synced");
                holder.editActionTV.setTextColor(context.getResources().getColor(R.color.white));
                holder.editActionTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ProjectPrefrence.saveSharedPrefrenceData(
                                AppConstant.PROJECT_PREF, AppConstant.ACTION_TO_PERFORM,AppConstant.VIEW_ACTION, context);
                        ProjectPrefrence.saveSharedPrefrenceData(
                                AppConstant.PROJECT_PREF,AppConstant.SELECTED_HOUSEHOLD_ITEM,item.serialize(),context);
                        Intent theIntent = new Intent(context, PreviewActivity.class);
                        context.startActivity(theIntent);
                    }
                });

            }else if(item.getStatus().equalsIgnoreCase(AppConstant.LOCK_STATUS)){
                holder.editActionTV.setBackgroundColor(context.getResources().getColor(R.color.lock_status_color));
                holder.editActionTV.setText("Locked");
                holder.editActionTV.setTextColor(context.getResources().getColor(R.color.white));
                holder.editActionRL.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openResetHousehold(item,holder.editActionRL,context,getActivity());

                    }
                });

            }*/
         /*   holder.editActionRL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openResetHousehold(item,holder.editActionRL);
                }
            });*/

        }

        @Override
        public int getItemCount() {
            return dataSet.size();
        }

    }


    private void updateScreen(String idImage) {
        try {
            if (idImage != null) {
                beneficiaryPhotoIV.setImageBitmap(AppUtility.convertStringToBitmap(idImage));
            } else {
//                if (seccItem.getGovtIdPhoto() != null && !seccItem.getGovtIdPhoto().equalsIgnoreCase("")) {
//
//                } else {
                beneficiaryPhotoIV.setImageBitmap(null);
//                }
            }
        } catch (Exception e) {

        }
    }


}
