package com.nhpm.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.nhpm.Models.response.GovernmentIdItem;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;
import com.nhpm.Utility.AppUtility;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FamilyDetailsFragment extends Fragment {
    private View view;
    private Context context;
    private ArrayList<GovernmentIdItem> govtIdStatusList;
    private Spinner govtIdSP;
    private GovernmentIdItem item;

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
        govtIdSP = (Spinner) view.findViewById(R.id.govtIdSP);
        prepareGovernmentIdSpinner();
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

    }
    private void prepareGovernmentIdSpinner() {
        govtIdStatusList = AppUtility.prepareGovernmentIdSpinner();
        ArrayList<String> spinnerList = new ArrayList<>();
        /*govtIdStatusList.add(new GovernmentIdItem(0,"Select Govt.ID"));
        govtIdStatusList.add(new GovernmentIdItem(1,"Aadhaar Enrollment ID"));
        govtIdStatusList.add(new GovernmentIdItem(2,"Voter ID Card"));
        govtIdStatusList.add(new GovernmentIdItem(3,"Ration Card"));
        govtIdStatusList.add(new GovernmentIdItem(4,"NREGA job card"));
        govtIdStatusList.add(new GovernmentIdItem(5,"Driving License"));*/
        for (GovernmentIdItem item : govtIdStatusList) {
            spinnerList.add(item.status);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.custom_drop_down, R.id.textView, spinnerList);
        govtIdSP.setAdapter(adapter);
    }

}
