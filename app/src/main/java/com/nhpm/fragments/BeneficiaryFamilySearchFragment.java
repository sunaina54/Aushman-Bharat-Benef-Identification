package com.nhpm.fragments;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.customComponent.CustomAlert;
import com.customComponent.CustomAsyncTask;
import com.customComponent.TaskListener;
import com.customComponent.utility.CustomHttp;
import com.customComponent.utility.ProjectPrefrence;
import com.google.zxing.client.result.VINParsedResult;
import com.nhpm.LocalDataBase.dto.SeccDatabase;
import com.nhpm.Models.SerachOptionItem;
import com.nhpm.Models.request.BeneficiarySearchModel;
import com.nhpm.Models.request.FamilyListRequestModel;
import com.nhpm.Models.request.GetSearchParaRequestModel;
import com.nhpm.Models.request.LogRequestItem;
import com.nhpm.Models.request.MobileOtpRequestLoginModel;
import com.nhpm.Models.request.MobileRationRequestModel;
import com.nhpm.Models.request.SaveLoginTransactionRequestModel;
import com.nhpm.Models.request.ValidateUrnRequestModel;
import com.nhpm.Models.response.BeneficiaryListItem;
import com.nhpm.Models.response.BeneficiaryModel;
import com.nhpm.Models.response.FamilyListResponseItem;
import com.nhpm.Models.response.GetSearchParaResponseModel;
import com.nhpm.Models.response.MobileSearchResponseModel;
import com.nhpm.Models.response.SaveLoginTransactionResponseModel;
import com.nhpm.Models.response.URNResponseModel;
import com.nhpm.Models.response.master.StateItem;
import com.nhpm.Models.response.verifier.VerifierLoginResponse;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;
import com.nhpm.Utility.AppUtility;
import com.nhpm.activity.BlockDetailActivity;
import com.nhpm.activity.FamilyListActivity;
import com.nhpm.activity.FamilyListByHHIDActivity;
import com.nhpm.activity.FamilyListByMobileActivity;
import com.nhpm.activity.FamilyListByURNActivity;
import com.nhpm.activity.FamilyMembersListActivity;
import com.nhpm.activity.LoginActivity;
import com.nhpm.activity.PhoneNumberActivity;
import com.nhpm.activity.PinLoginActivity;
import com.nhpm.activity.SearchDashboardActivity;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

/**
 * Created by SUNAINA on 22-05-2018.
 */

public class BeneficiaryFamilySearchFragment extends Fragment {
    private Spinner cardTypeSpinner;
    private Context context;
    private EditText rationCardET, rsbyET, ahlTinET, mobileET, hhIdNoET, villageCodeET, shhidET;
    private ArrayList<BeneficiarySearchModel> searchModelArrayList;
    private TextView cardTypeTV, findByNameTV, noMemberTV;
    private Button searchBTN;
    private String cardNo = "", cardType = "", villageCode = "", shhid = "";
    private ArrayList<BeneficiaryListItem> list;
    private StateItem selectedStateItem, selectedStateItem1;
    private FamilyListRequestModel request;
    private VerifierLoginResponse loginResponse;
    private CustomAsyncTask mobileOtpAsyncTask;
    private LogRequestItem logRequestItem = new LogRequestItem();
    public static int sequence = 0;
    private RelativeLayout microphoneLL;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    private FamilyListRequestModel familyListRequestModel;
    private String familyResponse;
    private FamilyListResponseItem familyListResponseModel;
    private CustomAsyncTask customAsyncTask, urnAsyncTask, mobileAsyncTask;
    private URNResponseModel urnResponseModel;
    private MobileSearchResponseModel mobileSearchResponseModel;
    private Spinner stateSP;
    private LinearLayout villageCodeLL;
    private BlockDetailActivity blockDetailActivity;
    private String searchType="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.benificiary_family_search_layout, container, false);
        setupScreen(view);
        return view;
    }

    private void setupScreen(View view) {
        context = getActivity();
        AppUtility.softKeyBoard(blockDetailActivity,0);
        selectedStateItem = StateItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SELECTED_STATE, context));
        loginResponse = VerifierLoginResponse.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.VERIFIER_CONTENT, context));
        selectedStateItem1 = StateItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SELECTED_STATE_SEARCH, context));

        stateSP = (Spinner) view.findViewById(R.id.stateSP);
        searchBTN = (Button) view.findViewById(R.id.searchBTN);
        noMemberTV = (TextView) view.findViewById(R.id.noMemberTV);
        noMemberTV.setVisibility(View.GONE);
        microphoneLL = (RelativeLayout) view.findViewById(R.id.microphoneLL);

        findByNameTV = (TextView) view.findViewById(R.id.findByNameTV);

        final ArrayList<StateItem> stateList1 = SeccDatabase.findStateList(context);

        Collections.sort(stateList1, new Comparator<StateItem>() {
            @Override
            public int compare(StateItem s1, StateItem s2) {
                return s1.getStateName().compareToIgnoreCase(s2.getStateName());
            }
        });
        ArrayList<String> spinnerStateList = new ArrayList<>();
        if (stateList1 != null) {
            for (StateItem item1 : stateList1) {
                spinnerStateList.add(item1.getStateName());
            }

        }
        stateSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();

                if (i == 0) {

                } else {
                    selectedStateItem1 = stateList1.get(i);
                    ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SELECTED_STATE_SEARCH, selectedStateItem1.serialize(), context);
                    selectedStateItem1=StateItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SELECTED_STATE_SEARCH, context));
                    blockDetailActivity.headerTV.setText(context.getResources().getString(R.string.nhpsFieldValidation) + " (" + selectedStateItem1.getStateName() + ")");

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, spinnerStateList);
        stateSP.setAdapter(adapter1);
        for (int i = 0; i < stateList1.size(); i++) {
            if(selectedStateItem1==null) {
                if (selectedStateItem.getStateCode().equalsIgnoreCase(stateList1.get(i).getStateCode())) {
                    stateSP.setSelection(i);
                    // stateSP.setTitle(item.getStateName());
                    String stateName = stateList1.get(i).getStateName();
                    Log.d("state name11 :", stateName);
                    break;
                }
            }else{
                if (selectedStateItem1.getStateCode().equalsIgnoreCase(stateList1.get(i).getStateCode())) {
                    stateSP.setSelection(i);
                    // stateSP.setTitle(item.getStateName());
                    String stateName = stateList1.get(i).getStateName();
                    Log.d("state name11 :", stateName);
                    break;
                }
            }
        }


        findByNameTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               /* Intent intent = new Intent(context, SearchDashboardActivity.class);
                startActivity(intent);*/
/*

                Intent intent =new Intent(context,PhoneNumberActivity.class);
                intent.putExtra("PhoneActivity","Demo");
                SerachOptionItem item=new SerachOptionItem();
                //item.setAadhaarNo(aadhaarET.getText().toString());

                item.setSearchType(AppConstant.AADHAAR_SEARCH);
                item.setMode(AppConstant.DEMO);
                ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_NAME,AppConstant.SEARCH_OPTION,item.serialize(),context);
*/

                Intent intent = new Intent(context, PinLoginActivity.class);
                intent.putExtra("Beneficiary", "Beneficiary");
                SerachOptionItem item = new SerachOptionItem();
                item.setSearchType(AppConstant.AADHAAR_SEARCH);
                item.setMode(AppConstant.DEMO);
                ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_NAME, AppConstant.SEARCH_OPTION, item.serialize(), context);
                startActivity(intent);
                logRequestItem.setAction("SEARCH_BY_NAME");
                searchType = AppConstant.SECC_PARAM;
                validateOTP();
            }
        });
        cardTypeTV = (TextView) view.findViewById(R.id.cardTypeTV);
        villageCodeLL = (LinearLayout) view.findViewById(R.id.villageCodeLL);

        villageCodeET = (EditText) view.findViewById(R.id.villageCodeET);
        villageCodeET.setSelection(villageCodeET.getText().toString().length());
        if (villageCodeET.getText().toString().length() == 6) {
            villageCodeET.setTextColor(AppUtility.getColor(context, R.color.green));
        }

        shhidET = (EditText) view.findViewById(R.id.shhidET);
        shhidET.setSelection(shhidET.getText().toString().length());
        if (shhidET.getText().toString().length() == 4) {
            shhidET.setTextColor(AppUtility.getColor(context, R.color.green));
        }

        villageCodeET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() > 0) {

                    villageCodeET.setTextColor(AppUtility.getColor(context, R.color.black_shine));
                    if (villageCodeET.getText().toString().length() == 6) {

                        villageCodeET.setTextColor(AppUtility.getColor(context, R.color.green));

                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        shhidET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() > 0) {

                    shhidET.setTextColor(AppUtility.getColor(context, R.color.black_shine));
                    if (shhidET.getText().toString().length() == 4) {

                        shhidET.setTextColor(AppUtility.getColor(context, R.color.green));

                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        hhIdNoET = (EditText) view.findViewById(R.id.hhIdNoET);
        hhIdNoET.setSelection(hhIdNoET.getText().toString().length());
        if (hhIdNoET.getText().toString().length() == 24) {
            hhIdNoET.setTextColor(AppUtility.getColor(context, R.color.green));

        }
        hhIdNoET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() > 0) {

                    hhIdNoET.setTextColor(AppUtility.getColor(context, R.color.black_shine));
                    if (hhIdNoET.getText().toString().length() == 24) {
                        hhIdNoET.setTextColor(AppUtility.getColor(context, R.color.green));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        rationCardET = (EditText) view.findViewById(R.id.rationCardET);
        rationCardET.setSelection(rationCardET.getText().toString().length());
        if (rationCardET.getText().toString().length() == 12) {
            rationCardET.setTextColor(AppUtility.getColor(context, R.color.green));

        }
        rationCardET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() > 0) {

                    rationCardET.setTextColor(AppUtility.getColor(context, R.color.black_shine));
                    if (rationCardET.getText().toString().length() == 12) {
                        rationCardET.setTextColor(AppUtility.getColor(context, R.color.green));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        rsbyET = (EditText) view.findViewById(R.id.rsbyET);
        rsbyET.setSelection(rsbyET.getText().toString().length());
        if (rsbyET.getText().toString().length() == 17) {
            rsbyET.setTextColor(AppUtility.getColor(context, R.color.green));

        }
        rsbyET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() > 0) {

                    rsbyET.setTextColor(AppUtility.getColor(context, R.color.black_shine));
                    if (rsbyET.getText().toString().length() == 17) {
                        rsbyET.setTextColor(AppUtility.getColor(context, R.color.green));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        ahlTinET = (EditText) view.findViewById(R.id.ahlTinET);
        ahlTinET.setSelection(ahlTinET.getText().toString().length());
        if (ahlTinET.getText().toString().length() == 29) {
            ahlTinET.setTextColor(AppUtility.getColor(context, R.color.green));

        }
        ahlTinET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() > 0) {

                    ahlTinET.setTextColor(AppUtility.getColor(context, R.color.black_shine));
                    if (ahlTinET.getText().toString().length() == 29) {

                        ahlTinET.setTextColor(AppUtility.getColor(context, R.color.green));

                    } /*else {

                        mobileET.setTextColor(AppUtility.getColor(context, R.color.red));
                    }*/
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mobileET = (EditText) view.findViewById(R.id.mobileET);
        mobileET.setSelection(mobileET.getText().toString().length());
        if (mobileET.getText().toString().length() == 10) {
            mobileET.setTextColor(AppUtility.getColor(context, R.color.green));

        }
        mobileET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() > 0) {
                    if (Integer.parseInt(charSequence.toString().substring(0, 1)) > 5) {
                        //isValidMobile = true;
                        mobileET.setTextColor(AppUtility.getColor(context, R.color.black_shine));
                        if (mobileET.getText().toString().length() == 10) {
                            // whoseMobileSP.setEnabled(true);
                            // whoseMobileSP.setAlpha(1.0f);
                            // mobileValidateLayout.setVisibility(View.VISIBLE);
                            //prepareFamilyStatusSpinner(mobileNumberET.getText().toString().trim());
                            mobileET.setTextColor(AppUtility.getColor(context, R.color.green));

                        }
                    } else {
                        //whoseMobileSP.setEnabled(false);
                        // whoseMobileSP.setAlpha(0.4f);
                        // isValidMobile = false;
                        //mobileValidateLayout.setVisibility(View.GONE);
                        mobileET.setTextColor(AppUtility.getColor(context, R.color.red));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        microphoneLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });
        ArrayList<String> spinnerList = new ArrayList<>();
        spinnerList.add("HHId Number");
        spinnerList.add("AHLTIN");
        spinnerList.add("RSBY URN");
        spinnerList.add("Mobile Number");
        spinnerList.add("Ration Card");
        spinnerList.add("Village Code");


        cardTypeSpinner = (Spinner) view.findViewById(R.id.cardTypeSpinner);
        cardType = "HHId Number";
        cardTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                rationCardET.setVisibility(View.GONE);
                rsbyET.setVisibility(View.GONE);
                ahlTinET.setVisibility(View.GONE);
                mobileET.setVisibility(View.GONE);
                hhIdNoET.setVisibility(View.GONE);
                villageCodeLL.setVisibility(View.GONE);
                if (position == 0) {
                    hhIdNoET.setVisibility(View.VISIBLE);
                    microphoneLL.setVisibility(View.VISIBLE);

                    cardTypeTV.setText("HHId Number");
                    cardType = "HHId Number";
                    searchType=AppConstant.HHID_PARAM;
                } else if (position == 1) {
                    ahlTinET.setVisibility(View.VISIBLE);
                    microphoneLL.setVisibility(View.VISIBLE);

                    cardTypeTV.setText("AHLTIN");
                    cardType = "AHLTIN";
                    String ahltin = ahlTinET.getText().toString();
                    Log.d("TAG", "AhlTine : " + ahltin);
                    if (ahltin.length() == 29) {
                        String firstTwoChar = ahltin.substring(0, 2);
                        // 2 7 8 5 4 3
                        String nextSevenChar = ahltin.substring(2, 9);
                        String nextEightChar = ahltin.substring(9, 17);
                        String nextFiveChar = ahltin.substring(17, 22);
                        String nextFourChar = ahltin.substring(22, 26);
                        String lastThreeChar = ahltin.substring(26, 29);
                        Log.d("TAG", "AhlTine : " + ahltin);
                        Log.d("TAG", "First TTwo : " + firstTwoChar);
                        Log.d("TAG", "next seven : " + nextSevenChar);
                        Log.d("TAG", "next eight : " + nextEightChar);
                        Log.d("TAG", "next five : " + nextFiveChar);
                        Log.d("TAG", "next four : " + nextFourChar);
                        Log.d("TAG", "next three : " + lastThreeChar);
                        ahltin = firstTwoChar + " " + nextSevenChar + " " + nextEightChar + " " + nextFiveChar + " " + nextFourChar + " " + lastThreeChar;
                        Log.d("TAG", "Ayushman Id  : " + ahltin);
                    }
                } else if (position == 2) {
                    cardType = "RSBY URN";
                    rsbyET.setVisibility(View.VISIBLE);
                    microphoneLL.setVisibility(View.VISIBLE);
                    searchType=AppConstant.RSBY_PARAM;

                } else if (position == 3) {
                    cardType = "Mobile Number";
                    mobileET.setVisibility(View.VISIBLE);
                    microphoneLL.setVisibility(View.VISIBLE);
                    searchType=AppConstant.MOBILE_PARAM;
                } else if (position == 4) {
                    cardType = "Ration Card";
                    rationCardET.setVisibility(View.VISIBLE);
                    microphoneLL.setVisibility(View.VISIBLE);
                    searchType=AppConstant.RATION_PARAM;
                } else if (position == 5) {
                    cardType = "Village Code";
                    villageCodeLL.setVisibility(View.VISIBLE);
                    microphoneLL.setVisibility(View.GONE);
                    searchType=AppConstant.VILLAGE_PARAM;
                }
                    /*rationCardET.setVisibility(View.GONE);
                    rsbyET.setVisibility(View.VISIBLE);
                    ahlTinET.setVisibility(View.GONE);
                    mobileET.setVisibility(View.GONE);
                    hhIdNoET.setVisibility(View.GONE);
                    cardTypeTV.setText("RSBY URN");

                    cardType = "RSBY URN";
                } else if (position == 2) {
                    rationCardET.setVisibility(View.GONE);
                    rsbyET.setVisibility(View.GONE);
                    ahlTinET.setVisibility(View.VISIBLE);
                    mobileET.setVisibility(View.GONE);
                    hhIdNoET.setVisibility(View.GONE);
                    cardTypeTV.setText("AHLTIN");

                    cardType = "AHLTIN";
                } else if (position == 3) {
                    rationCardET.setVisibility(View.GONE);
                    rsbyET.setVisibility(View.GONE);
                    ahlTinET.setVisibility(View.GONE);
                    mobileET.setVisibility(View.VISIBLE);
                    hhIdNoET.setVisibility(View.GONE);
                    cardTypeTV.setText("Mobile Number");
                    cardType = "Mobile Number";
                }
                else if (position == 4) {

                    rationCardET.setVisibility(View.VISIBLE);
                    rsbyET.setVisibility(View.GONE);
                    ahlTinET.setVisibility(View.GONE);
                    mobileET.setVisibility(View.GONE);
                    hhIdNoET.setVisibility(View.GONE);
                    cardTypeTV.setText("Ration Card");

                    cardType = "Ration Card";



                }*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.custom_drop_down, R.id.textView, spinnerList);
        cardTypeSpinner.setAdapter(adapter);
        cardTypeSpinner.setSelection(0);

        searchBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                request = new FamilyListRequestModel();
                //
                if (!cardType.equalsIgnoreCase("") && cardType.equalsIgnoreCase("Ration Card")) {
                    cardNo = rationCardET.getText().toString();
                    if (cardNo.equalsIgnoreCase("")) {
                        CustomAlert.alertWithOk(context, "Please enter ration card number");
                        return;
                    }/* else if (cardNo.length() < 15) {
                        CustomAlert.alertWithOk(context, "Please enter valid ration card number");
                        return;
                    }*/


           /*         MobileRationRequestModel requestModel = new MobileRationRequestModel();
                    requestModel.setMobileRation(cardNo);
                    requestModel.setParam(AppConstant.RATION_PARAM);

                    //requestModel.setSelectedState(selectedStateItem.getStateCode());
                    requestModel.setSelectedState("6");*/
                    AppUtility.searchTitleHeader = "Ration Card";
                    logRequestItem.setAction(AppUtility.SEARCH_BY_RATION_CARD);
                  /*  Intent theIntent = new Intent(context, FamilyListByMobileActivity.class);
                    theIntent.putExtra("SearchParam", requestModel);
                    startActivity(theIntent);*/
                    familyListDataByMobileOrRation();
                    ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.LOG_REQUEST, logRequestItem.serialize(), context);


                }

                if (!cardType.equalsIgnoreCase("") && cardType.equalsIgnoreCase("RSBY URN")) {
                    cardNo = rsbyET.getText().toString();

                    if (cardNo.equalsIgnoreCase("")) {
                        CustomAlert.alertWithOk(context, "Please enter RSBY URN number");
                        return;
                    } else if (cardNo.length() < 17) {
                        CustomAlert.alertWithOk(context, "Please enter valid RSBY URN number");
                        return;
                    }

                    AppUtility.searchTitleHeader = "URN";
                    logRequestItem.setAction(AppUtility.SEARCH_BY_RSBY_URN);
                  /*  ValidateUrnRequestModel requestModel = new ValidateUrnRequestModel();
                    requestModel.setUrn(cardNo);*/
                    familyListDataByURN();
                  /*  Intent theIntent = new Intent(context, FamilyListByURNActivity.class);
                    theIntent.putExtra("SearchParam", requestModel);
                    startActivity(theIntent);*/
                    ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.LOG_REQUEST, logRequestItem.serialize(), context);


                }

                if (!cardType.equalsIgnoreCase("") && cardType.equalsIgnoreCase("AHLTIN")) {
                    cardNo = ahlTinET.getText().toString();
                    // request.setAhlTinno(cardNo);

                    if (cardNo.equalsIgnoreCase("")) {
                        CustomAlert.alertWithOk(context, "Please enter AHLTIN number");
                        return;
                    } else if (cardNo.length() < 29) {
                        CustomAlert.alertWithOk(context, "Please enter valid AHLTIN number");
                        return;
                    }

                /*    request.setName("");
                    request.setGenderid("");
                    request.setAge("");
                    request.setPincode("");
                    request.setFathername("");*/
                    AppUtility.searchTitleHeader = "AHLTIN";
                    logRequestItem.setAction("AHLTIN");
                    familyListDatabyHHIdOrAHLTIN();
                  /*  Intent theIntent = new Intent(context, FamilyListByHHIDActivity.class);
                    theIntent.putExtra("SearchParam", request);
                    startActivity(theIntent);*/
                    ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.LOG_REQUEST, logRequestItem.serialize(), context);

                }

                if (!cardType.equalsIgnoreCase("") &&
                        cardType.equalsIgnoreCase("Mobile Number")) {
                    cardNo = mobileET.getText().toString();
                    if (cardNo.equalsIgnoreCase("")) {
                        CustomAlert.alertWithOk(context, "Please enter mobile number");
                        return;
                    } else if (cardNo.length() < 10) {
                        CustomAlert.alertWithOk(context, "Please enter valid mobile number");
                        return;
                    }
                    AppUtility.searchTitleHeader = "Mobile";
                    logRequestItem.setAction(AppUtility.SEARCH_BY_MOBILE);
                  /*  MobileRationRequestModel requestModel = new MobileRationRequestModel();
                    requestModel.setMobileRation(cardNo);
                    requestModel.setParam(AppConstant.MOBILE_PARAM);
                    requestModel.setSelectedState("6");
                    Intent theIntent = new Intent(context, FamilyListByMobileActivity.class);
                    theIntent.putExtra("SearchParam", requestModel);
                    startActivity(theIntent);*/
                    familyListDataByMobileOrRation();
                    ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.LOG_REQUEST, logRequestItem.serialize(), context);


                }

                if (!cardType.equalsIgnoreCase("") && cardType.equalsIgnoreCase("HHId Number")) {
                    cardNo = hhIdNoET.getText().toString();
                    // request.setHho_id(cardNo);
                    if (cardNo.equalsIgnoreCase("")) {
                        CustomAlert.alertWithOk(context, "Please enter HHId number");
                        return;
                    } else if (cardNo.length() < 24) {
                        CustomAlert.alertWithOk(context, "Please enter valid HHId number");
                        return;
                    }


                    AppUtility.searchTitleHeader = "HHId";
                    logRequestItem.setAction(AppUtility.SEARCH_BY_HHID);
                   /* Intent theIntent = new Intent(context, FamilyListByHHIDActivity.class);
                    theIntent.putExtra("SearchParam", request);
                    startActivity(theIntent);*/
                    familyListDatabyHHIdOrAHLTIN();
                    ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.LOG_REQUEST, logRequestItem.serialize(), context);


                }

                if (!cardType.equalsIgnoreCase("") &&
                        cardType.equalsIgnoreCase("Village Code")) {
                    villageCode = villageCodeET.getText().toString();
                    shhid = shhidET.getText().toString();
                    // request.setHho_id(cardNo);
                    if (villageCode.equalsIgnoreCase("")) {
                        CustomAlert.alertWithOk(context, "Please enter village code");
                        return;
                    }

                    if (shhid.equalsIgnoreCase("")) {
                        CustomAlert.alertWithOk(context, "Please enter SHHId number");
                        return;
                    }
                    AppUtility.searchTitleHeader = "Mobile";
                    logRequestItem.setAction(AppUtility.SEARCH_BY_VILLAGE);
                    familyListDataByMobileOrRation();
                    ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.LOG_REQUEST, logRequestItem.serialize(), context);


                }


                /*BeneficiaryModel beneficiaryModel = new BeneficiaryModel();
                beneficiaryModel.setBeneficiaryList(getList(cardNo, cardType));
                if (beneficiaryModel != null && beneficiaryModel.getBeneficiaryList() != null
                        && beneficiaryModel.getBeneficiaryList().size() > 0) {
                    Intent intent = new Intent(context, FamilyMembersListActivity.class);
                    intent.putExtra("result", beneficiaryModel);
                    intent.putExtra("cardNo",cardNo);
                    startActivity(intent);
                }*/
                //validateOTP();
            }
        });

    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = context.getAssets().open("BenificiaryList.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    private ArrayList<BeneficiaryListItem> getList(String searchParam, String serachType) {
        BeneficiaryModel beneficiaryModel = BeneficiaryModel.create(loadJSONFromAsset());
        ArrayList<BeneficiaryListItem> beneficiaryList = beneficiaryModel.getBeneficiaryList();
        ArrayList<BeneficiaryListItem> temp = new ArrayList<>();

        Log.d("TAG", "list is :" + beneficiaryList.size());

        if (!serachType.equalsIgnoreCase("")) {
            for (BeneficiaryListItem item : beneficiaryList) {

                if (serachType.equalsIgnoreCase("Ration Card")) {
                    if (searchParam.equalsIgnoreCase(item.getRashancardNo())) {
                        temp.add(item);
                    } else {
                        noMemberTV.setVisibility(View.VISIBLE);
                        noMemberTV.setText("No family found for \n Ration card no: " + searchParam + " in " + selectedStateItem.getStateName());

                    }
                }
                if (serachType.equalsIgnoreCase("RSBY URN")) {
                    if (searchParam.equalsIgnoreCase(item.getUrnNo())) {
                        temp.add(item);
                    } else {
                        noMemberTV.setVisibility(View.VISIBLE);
                        noMemberTV.setText("No family found for \n RSBY URN no: " + searchParam + " in " + selectedStateItem.getStateName());
                    }
                }
                if (serachType.equalsIgnoreCase("AHLTIN")) {
                    if (searchParam.equalsIgnoreCase(item.getAhlTin())) {
                        temp.add(item);
                    } else {
                        noMemberTV.setVisibility(View.VISIBLE);
                        noMemberTV.setText("No family found for \n AHLTIN no: " + searchParam + " in " + selectedStateItem.getStateName());
                    }
                }
                if (serachType.equalsIgnoreCase("Mobile Number")) {
                    if (searchParam.equalsIgnoreCase(item.getMobileNo())) {
                        temp.add(item);
                    } else {
                        noMemberTV.setVisibility(View.VISIBLE);
                        noMemberTV.setText("No family found for \n Mobile Number: " + searchParam + " in " + selectedStateItem.getStateName());
                    }
                }
            }
        }
        return temp;
    }

    private void validateOTP() {

        TaskListener taskListener = new TaskListener() {
            @Override
            public void execute() {

                //request.setUserName(ApplicationGlobal.MOBILE_Username);
                // request.setUserPass(ApplicationGlobal.MOBILE_Password);
              /*  String payLoad = request.serialize();
                System.out.print(payLoad);*/
                try {

                    SaveLoginTransactionRequestModel logTransReq = new SaveLoginTransactionRequestModel();
                    logTransReq.setCreated_by(loginResponse.getAadhaarNumber());
                    HashMap<String, String> responseTid = CustomHttp.httpPost("https://pmrssm.gov.in/VIEWSTAT/api/login/saveLoginTransaction", logTransReq.serialize());
                    SaveLoginTransactionResponseModel responseModel = SaveLoginTransactionResponseModel.create(responseTid.get("response"));
                    ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, "logTrans", responseModel.serialize(), context);
                    sequence = 0;
                    long time= System.currentTimeMillis();
                    GetSearchParaRequestModel getSearchParaRequestModel = new GetSearchParaRequestModel();
                    getSearchParaRequestModel.setUser_id(loginResponse.getAadhaarNumber());
                    getSearchParaRequestModel.setType_of_search(searchType);
                    getSearchParaRequestModel.setUid_search_type("");
                    getSearchParaRequestModel.setState_code(selectedStateItem1.getStateCode());
                    getSearchParaRequestModel.setDistrict_code("");
                    getSearchParaRequestModel.setAhl_tin("");
                    getSearchParaRequestModel.setType_of_doc("");
                    getSearchParaRequestModel.setTid(responseModel.getTransactionId()+"");
                    getSearchParaRequestModel.setStartTime(time);
                    //                    getSearchParaRequestModel.setSource(AppConstant.MOBILE_SOURCE);
                    String request1= getSearchParaRequestModel.serialize();
                    Log.d("Find by name",request1);
                    ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, "SEARCH_DATA", getSearchParaRequestModel.serialize(), context);



                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void updateUI() {

            }
        };

        if (mobileOtpAsyncTask != null) {
            mobileOtpAsyncTask.cancel(true);
            mobileOtpAsyncTask = null;
        }

        mobileOtpAsyncTask = new CustomAsyncTask(taskListener, context);
        mobileOtpAsyncTask.execute();

    }

    /**
     * Showing google speech input dialog
     */
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(context,
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loginResponse = VerifierLoginResponse.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.VERIFIER_CONTENT, context));
        noMemberTV.setVisibility(View.GONE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && data != null) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    if (cardType.equalsIgnoreCase("HHId Number")) {
                        hhIdNoET.setVisibility(View.VISIBLE);
                        hhIdNoET.setText(result.get(0).trim());
                        hhIdNoET.setSelection(hhIdNoET.getText().toString().length());
                    }

                    if (cardType.equalsIgnoreCase("AHLTIN")) {
                        ahlTinET.setVisibility(View.VISIBLE);
                        ahlTinET.setText(result.get(0).trim());
                        ahlTinET.setSelection(ahlTinET.getText().toString().length());
                    }

                    if (cardType.equalsIgnoreCase("RSBY URN")) {
                        rsbyET.setVisibility(View.VISIBLE);
                        rsbyET.setText(result.get(0).trim());
                        rsbyET.setSelection(rsbyET.getText().toString().length());

                    }
                    if (cardType.equalsIgnoreCase("Mobile Number")) {
                        mobileET.setVisibility(View.VISIBLE);
                        mobileET.setText(result.get(0).trim());
                        mobileET.setSelection(mobileET.getText().toString().length());

                    }
                    if (cardType.equalsIgnoreCase("Ration Card")) {
                        rationCardET.setVisibility(View.VISIBLE);
                        rationCardET.setText(result.get(0).trim());
                        rationCardET.setSelection(rationCardET.getText().toString().length());
                    }

                }
                break;
            }
        }
    }

    private void familyListDatabyHHIdOrAHLTIN() {


        familyListRequestModel = new FamilyListRequestModel();
        // familyListRequestModel.setName("sumit");
        familyListRequestModel.setUserName("nhps_fvs^1&%mobile");
        familyListRequestModel.setUserPass("ZCbEJyPUlaQXo8fJT2P+5PAKJOs6emRZgdI/w5qkIrN2NqRUQQ3Sdqp+9WbS8P4j");
//familyListRequestModel.setAge("");

        familyListRequestModel.setName("");
        familyListRequestModel.setGenderid("");
        familyListRequestModel.setAge("");
        familyListRequestModel.setPincode("");
        familyListRequestModel.setFathername("");

        familyListRequestModel.setAhlblockno("");
        familyListRequestModel.setBlock_name_english("");
        familyListRequestModel.setDistrict_code("");
        familyListRequestModel.setResultCount("100");
        if (familyListRequestModel.getFathername() == null) {
            familyListRequestModel.setFathername("");
        }
//familyListRequestModel.setGenderid("");
        if (familyListRequestModel.getMothername() == null) {
            familyListRequestModel.setMothername("");
        }

        if (cardType.equalsIgnoreCase("HHId Number")) {
            familyListRequestModel.setHho_id(cardNo);
        } else {
            familyListRequestModel.setHho_id("");
        }


        if (familyListRequestModel.getState_name() == null) {
            familyListRequestModel.setState_name("");
        }

        if (familyListRequestModel.getDistrict_name() == null) {
            familyListRequestModel.setDistrict_name("");
        }
        if (familyListRequestModel.getVt_name() == null) {
            familyListRequestModel.setVt_name("");
        }


        if (cardType.equalsIgnoreCase("AHLTIN")) {
            familyListRequestModel.setAhlTinno(cardNo);
        } else {
            familyListRequestModel.setAhlTinno("");
        }
        if (familyListRequestModel.getPincode() == null) {
            familyListRequestModel.setPincode("");
        }
        familyListRequestModel.setRural_urban("");
        if (familyListRequestModel.getSpousenm() == null) {
            familyListRequestModel.setSpousenm("");
        }
        if (familyListRequestModel.getState_name() == null) {
            familyListRequestModel.setState_name("");
        }
        familyListRequestModel.setState_name_english("");
        familyListRequestModel.setSpousenms("");

        TaskListener taskListener = new TaskListener() {
            @Override
            public void execute() {
                try {


                    String request = familyListRequestModel.serialize();
                    HashMap<String, String> response = CustomHttp.httpPostWithTokken(AppConstant.SEARCH_FAMILY_LIST, request, AppConstant.AUTHORIZATION, loginResponse.getAuthToken());
                    familyResponse = response.get("response");


                    if (familyResponse != null) {
                        if (logRequestItem == null) {
                            logRequestItem = new LogRequestItem();
                        }
                        logRequestItem.setOperatorinput(request);
                        familyListResponseModel = new FamilyListResponseItem().create(familyResponse);
                        logRequestItem.setOperatoroutput(familyListResponseModel.serialize());
                        ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.LOG_REQUEST, logRequestItem.serialize(), context);
                        try {
                            SaveLoginTransactionRequestModel logTransReq = new SaveLoginTransactionRequestModel();
                            logTransReq.setCreated_by(loginResponse.getAadhaarNumber());
                            HashMap<String, String> responseTid = CustomHttp.httpPost("https://pmrssm.gov.in/VIEWSTAT/api/login/saveLoginTransaction", logTransReq.serialize());
                            SaveLoginTransactionResponseModel responseModel = SaveLoginTransactionResponseModel.create(responseTid.get("response"));
                            ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, "logTrans", responseModel.serialize(), context);
                            BeneficiaryFamilySearchFragment.sequence = 0;

                            long time= System.currentTimeMillis();
                            GetSearchParaRequestModel getSearchParaRequestModel = new GetSearchParaRequestModel();
                            getSearchParaRequestModel.setUser_id(loginResponse.getAadhaarNumber());
                            getSearchParaRequestModel.setType_of_search(searchType);
                            getSearchParaRequestModel.setUid_search_type("");
                            getSearchParaRequestModel.setState_code(selectedStateItem1.getStateCode());
                            getSearchParaRequestModel.setDistrict_code("");
                            getSearchParaRequestModel.setAhl_tin("");
                            getSearchParaRequestModel.setType_of_doc("");
                            getSearchParaRequestModel.setTid(responseModel.getTransactionId()+"");
                            getSearchParaRequestModel.setStartTime(time);
                          //  getSearchParaRequestModel.setEndTime(Long.valueOf(""));
                            getSearchParaRequestModel.setSource(AppConstant.MOBILE_SOURCE);



                            ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, "SEARCH_DATA", getSearchParaRequestModel.serialize(), context);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }

            }

            @Override
            public void updateUI() {
                // noMemberLL.setVisibility(View.GONE);
                // searchListRV.setVisibility(View.VISIBLE);
                if (familyListResponseModel != null) {
                    int matchCount = 0;
                    noMemberTV.setVisibility(View.GONE);
                    if (familyListResponseModel.isStatus()) {
                        if (familyListResponseModel.getResult() != null && familyListResponseModel.getResult().getResponse() != null) {
                            if (familyListResponseModel.getResult().getResponse().getNumFound() != null
                                    && !familyListResponseModel.getResult().getResponse().getNumFound().equalsIgnoreCase("")) {
                                matchCount = Integer.parseInt(familyListResponseModel.getResult().getResponse().getNumFound());

                            }
                            if (matchCount == 0) {
                                if (cardType.equalsIgnoreCase("HHId Number")) {
                                    noMemberTV.setVisibility(View.VISIBLE);
                                    noMemberTV.setText("No family found for \n HHId number: " + cardNo + " in " + selectedStateItem1.getStateName());

                                }

                                if (cardType.equalsIgnoreCase("AHLTIN")) {
                                    noMemberTV.setVisibility(View.VISIBLE);
                                    noMemberTV.setText("No family found for \n AHLTIN number: " + cardNo + " in " + selectedStateItem1.getStateName());
                                }
                                //noMemberTV.setText("No Family member found");
                            }
                            if (familyListResponseModel.getResult().getResponse().getDocs()
                                    != null && familyListResponseModel.getResult().getResponse().getDocs().size() > 0) {
                                Intent theIntent = new Intent(context, FamilyListByHHIDActivity.class);
                                theIntent.putExtra("SearchByHHIdOrAHLTIN", familyListResponseModel.getResult().getResponse().getDocs());
                                startActivity(theIntent);

                               /* try {
                                    refreshMembersList(familyListResponseModel.getResult().getResponse().getDocs());
                                } catch (Exception e) {
                                    Log.d("TAG", "Exception : " + e.toString());
                                }*/

                            } else {
                                if (cardType.equalsIgnoreCase("HHId Number")) {
                                    noMemberTV.setVisibility(View.VISIBLE);
                                    noMemberTV.setText("No family found for \n HHId number: " + cardNo + " in " + selectedStateItem1.getStateName());

                                }

                                if (cardType.equalsIgnoreCase("AHLTIN")) {
                                    noMemberTV.setVisibility(View.VISIBLE);
                                    noMemberTV.setText("No family found for \n AHLTIN number: " + cardNo + " in " + selectedStateItem1.getStateName());
                                }
                                //  noMemberLL.setVisibility(View.VISIBLE);
                                //  noMemberTV.setText("No Family member found");

                            }

                        }
                    } else if (familyListResponseModel != null &&
                            familyListResponseModel.getErrorCode().equalsIgnoreCase(AppConstant.SESSION_EXPIRED)
                            || familyListResponseModel.getErrorCode().equalsIgnoreCase(AppConstant.INVALID_TOKEN)) {
                        Intent intent = new Intent(context, LoginActivity.class);
                        CustomAlert.alertWithOkLogout(context, familyListResponseModel.getErrorMessage(), intent);

                    } else {
                        CustomAlert.alertWithOk(context, familyListResponseModel.getErrorMessage());
                    }
                } else {
                    CustomAlert.alertWithOk(context, "Internal Server Error");
                    // noMemberLL.setVisibility(View.VISIBLE);
                    // noMemberTV.setText("Internal Server Error");
                    // searchListRV.setVisibility(View.GONE);
                }
            }
        };
        if (customAsyncTask != null) {
            customAsyncTask.cancel(true);
            customAsyncTask = null;
        }

        customAsyncTask = new CustomAsyncTask(taskListener, "Please wait", context);
        customAsyncTask.execute();

    }


    private void familyListDataByURN() {

        TaskListener taskListener = new TaskListener() {
            @Override
            public void execute() {
                try {
                    ValidateUrnRequestModel requestModel = new ValidateUrnRequestModel();
                    requestModel.setUrn(cardNo);
                    String request = requestModel.serialize();
                    String url = AppConstant.SEARCH_BY_MOBILE_RATION;
                    HashMap<String, String> response = CustomHttp.httpPostWithTokken(AppConstant.SEARCH_BY_MOBILE_RATION, request, AppConstant.AUTHORIZATION, loginResponse.getAuthToken());
                    familyResponse = response.get("response");
                    if (familyResponse != null) {
                        urnResponseModel = new URNResponseModel().create(familyResponse);
                        SaveLoginTransactionRequestModel logTransReq = new SaveLoginTransactionRequestModel();
                        logTransReq.setCreated_by(loginResponse.getAadhaarNumber());
                        HashMap<String, String> responseTid = CustomHttp.httpPost("https://pmrssm.gov.in/VIEWSTAT/api/login/saveLoginTransaction", logTransReq.serialize());
                        SaveLoginTransactionResponseModel responseModel = SaveLoginTransactionResponseModel.create(responseTid.get("response"));
                        ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, "logTrans", responseModel.serialize(), context);
                        BeneficiaryFamilySearchFragment.sequence = 0;

                        long time= System.currentTimeMillis();
                        GetSearchParaRequestModel getSearchParaRequestModel = new GetSearchParaRequestModel();
                        getSearchParaRequestModel.setUser_id(loginResponse.getAadhaarNumber());
                        getSearchParaRequestModel.setType_of_search(searchType);
                        getSearchParaRequestModel.setUid_search_type("");
                        getSearchParaRequestModel.setState_code(selectedStateItem1.getStateCode());
                        getSearchParaRequestModel.setDistrict_code("");
                        getSearchParaRequestModel.setAhl_tin("");
                        getSearchParaRequestModel.setType_of_doc("");
                        getSearchParaRequestModel.setTid(responseModel.getTransactionId()+"");
                        getSearchParaRequestModel.setStartTime(time);
                        //getSearchParaRequestModel.setEndTime(Long.valueOf(""));
                        getSearchParaRequestModel.setSource(AppConstant.MOBILE_SOURCE);

                     /*   if (urnResponseModel==null || urnResponseModel.getUrnResponse()==null || urnResponseModel.getUrnResponse().size() <= 0) {
                            HashMap<String, String> searchResRsby = CustomHttp.httpPostWithTokken(AppConstant.GET_SEARCH_PARA, getSearchParaRequestModel.serialize(), AppConstant.AUTHORIZATION, loginResponse.getAuthToken());
                            String searchResponse = searchResRsby.get("response");
                            GetSearchParaResponseModel getSearchParaResponseModel = GetSearchParaResponseModel.create(searchResponse);

                        } else {*/

                            ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, "SEARCH_DATA", getSearchParaRequestModel.serialize(), context);

                        //}

                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }

            }

            @Override
            public void updateUI() {
                if (urnResponseModel != null) {
                    noMemberTV.setVisibility(View.GONE);
                    if (urnResponseModel.isStatus()) {
                        if (urnResponseModel.getUrnResponse() != null) {
                            if (urnResponseModel.getUrnResponse().size() > 0) {
                                Intent theIntent = new Intent(context, FamilyListByURNActivity.class);
                                theIntent.putExtra("SearchByURN", urnResponseModel.getUrnResponse());
                                startActivity(theIntent);
                                // refreshMembersList(familyListResponseModel.getUrnResponse());
                            } else {
                                if (cardType.equalsIgnoreCase("RSBY URN")) {
                                    noMemberTV.setVisibility(View.VISIBLE);
                                    noMemberTV.setText("No family found for RSBY URN \n number: " + cardNo + " in " + selectedStateItem1.getStateName());
                                }

                            }
                        }
                    } else if (urnResponseModel != null &&
                            urnResponseModel.getErrorCode().equalsIgnoreCase(AppConstant.SESSION_EXPIRED)
                            || urnResponseModel.getErrorCode().equalsIgnoreCase(AppConstant.INVALID_TOKEN)) {
                        Intent intent = new Intent(context, LoginActivity.class);
                        CustomAlert.alertWithOkLogout(context, urnResponseModel.getErrorMessage(), intent);

                    } else {
                        // unhandled issue is coming when rsby urn is wrong and errorMessage is "" blank
                        // CustomAlert.alertWithOk(context,urnResponseModel.getErrorMessage());
                        if (cardType.equalsIgnoreCase("RSBY URN")) {
                            noMemberTV.setVisibility(View.VISIBLE);
                            noMemberTV.setText("No family found for RSBY URN \n number: " + cardNo + " in " + selectedStateItem1.getStateName());
                        }
                    }
                } else {
                    CustomAlert.alertWithOk(context, "Internal Server Error");

                   /* noMemberLL.setVisibility(View.VISIBLE);
                    noMemberTV.setText("Internal Server Error");
                    searchListRV.setVisibility(View.GONE);*/
                }
            }
        };
        if (urnAsyncTask != null) {
            urnAsyncTask.cancel(true);
            urnAsyncTask = null;
        }

        urnAsyncTask = new CustomAsyncTask(taskListener, "Please wait", context);
        urnAsyncTask.execute();

    }

    private void familyListDataByMobileOrRation() {

        TaskListener taskListener = new TaskListener() {
            @Override
            public void execute() {
                try {
                    MobileRationRequestModel requestModel = new MobileRationRequestModel();

                    if (cardType.equalsIgnoreCase("Ration Card")) {
                        requestModel.setParam(AppConstant.RATION_PARAM);
                        requestModel.setMobileRation(cardNo);
                        requestModel.setShh("");
                        requestModel.setVillageCode("");
                    }

                    if (cardType.equalsIgnoreCase("Mobile Number")) {
                        requestModel.setParam(AppConstant.MOBILE_PARAM);
                        requestModel.setMobileRation(cardNo);
                        requestModel.setShh("");
                        requestModel.setVillageCode("");
                    }

                    if (cardType.equalsIgnoreCase("Village Code")) {
                        requestModel.setParam(AppConstant.VILLAGE_PARAM);
                        requestModel.setMobileRation("");
                        requestModel.setShh(shhid);
                        requestModel.setVillageCode(villageCode);
                    }

                    requestModel.setSelectedState(selectedStateItem1.getStateCode());
                    /*if(selectedStateItem1!=null){
                        requestModel.setSelectedState(selectedStateItem1.getStateCode());
                    }else {
                        requestModel.setSelectedState(selectedStateItem.getStateCode());
                    }*/
                    String request = requestModel.serialize();
                    String url = AppConstant.SEARCH_BY_MOBILE_RATION;

                    HashMap<String, String> response = CustomHttp.httpPostWithTokken(AppConstant.SEARCH_BY_MOBILE_RATION, request, AppConstant.AUTHORIZATION, loginResponse.getAuthToken());

                    familyResponse = response.get("response");


                    if (familyResponse != null) {
                        if (logRequestItem == null) {
                            logRequestItem = new LogRequestItem();
                        }
                        logRequestItem.setOperatorinput(request);
                        mobileSearchResponseModel = new MobileSearchResponseModel().create(familyResponse);

                        logRequestItem.setOperatoroutput(mobileSearchResponseModel.serialize());
                        ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.LOG_REQUEST, logRequestItem.serialize(), context);


                        try {
                            SaveLoginTransactionRequestModel logTransReq = new SaveLoginTransactionRequestModel();
                            logTransReq.setCreated_by(loginResponse.getAadhaarNumber());
                            HashMap<String, String> responseTid = CustomHttp.httpPost("https://pmrssm.gov.in/VIEWSTAT/api/login/saveLoginTransaction", logTransReq.serialize());
                            SaveLoginTransactionResponseModel responseModel = SaveLoginTransactionResponseModel.create(responseTid.get("response"));
                            ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, "logTrans", responseModel.serialize(), context);
                            BeneficiaryFamilySearchFragment.sequence = 0;
                            long time= System.currentTimeMillis();
                            GetSearchParaRequestModel getSearchParaRequestModel = new GetSearchParaRequestModel();
                            getSearchParaRequestModel.setUser_id(loginResponse.getAadhaarNumber());
                            getSearchParaRequestModel.setType_of_search(searchType);
                            getSearchParaRequestModel.setUid_search_type("");
                            getSearchParaRequestModel.setState_code(selectedStateItem1.getStateCode());
                            getSearchParaRequestModel.setDistrict_code("");
                            getSearchParaRequestModel.setAhl_tin("");
                            getSearchParaRequestModel.setType_of_doc("");
                            getSearchParaRequestModel.setTid(responseModel.getTransactionId()+"");
                            getSearchParaRequestModel.setStartTime(time);
                           // getSearchParaRequestModel.setEndTime(Long.valueOf(""));
                            getSearchParaRequestModel.setSource(AppConstant.MOBILE_SOURCE);
                            String request1= getSearchParaRequestModel.serialize();
                            Log.d("Find by name",request1);
                            ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, "SEARCH_DATA", getSearchParaRequestModel.serialize(), context);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }

            }

            @Override
            public void updateUI() {

                if (mobileSearchResponseModel != null) {
                    noMemberTV.setVisibility(View.GONE);

                    if (mobileSearchResponseModel.isStatus()) {
                        if (mobileSearchResponseModel.getUrnResponse() != null) {
                            if (mobileSearchResponseModel.getUrnResponse().size() > 0) {
                                Intent theIntent = new Intent(context, FamilyListByMobileActivity.class);
                                theIntent.putExtra("SearchByMobileRation", mobileSearchResponseModel.getUrnResponse());
                                startActivity(theIntent);
                                //refreshMembersList(familyListResponseModel.getUrnResponse());
                            } else {
                                if (cardType.equalsIgnoreCase("Ration Card")) {
                                    noMemberTV.setVisibility(View.VISIBLE);
                                    noMemberTV.setText("No family found for \n ration card number: " + cardNo + " in " + selectedStateItem1.getStateName());

                                }

                                if (cardType.equalsIgnoreCase("Mobile Number")) {
                                    noMemberTV.setVisibility(View.VISIBLE);
                                    noMemberTV.setText("No family found for \n mobile number: " + cardNo + " in " + selectedStateItem1.getStateName());
                                }
                                if (cardType.equalsIgnoreCase("Village Code")) {
                                    noMemberTV.setVisibility(View.VISIBLE);
                                    noMemberTV.setText("No family found for \n village code: " + villageCode + " in " + selectedStateItem1.getStateName());
                                }
                             /*   noMemberLL.setVisibility(View.VISIBLE);
                                noMemberTV.setText("No member found");
                                searchListRV.setVisibility(View.GONE);*/
                            }
                        }
                    } else if (mobileSearchResponseModel != null &&
                            mobileSearchResponseModel.getErrorCode().equalsIgnoreCase(AppConstant.SESSION_EXPIRED)
                            || mobileSearchResponseModel.getErrorCode().equalsIgnoreCase(AppConstant.INVALID_TOKEN)) {
                        Intent intent = new Intent(context, LoginActivity.class);
                        CustomAlert.alertWithOkLogout(context, mobileSearchResponseModel.getErrorMessage(), intent);


                    } else {
                        if (cardType.equalsIgnoreCase("Ration Card")) {
                            noMemberTV.setVisibility(View.VISIBLE);
                            noMemberTV.setText("No family found for \n ration card number: " + cardNo + " in " + selectedStateItem1.getStateName());

                        }

                        if (cardType.equalsIgnoreCase("Mobile Number")) {
                            noMemberTV.setVisibility(View.VISIBLE);
                            noMemberTV.setText("No family found for \n mobile number: " + cardNo + " in " + selectedStateItem1.getStateName());
                        }
                        if (cardType.equalsIgnoreCase("Village Code")) {
                            noMemberTV.setVisibility(View.VISIBLE);
                            noMemberTV.setText("No family found for \n village code: " + villageCode + " in " + selectedStateItem1.getStateName());
                        }
                        //  CustomAlert.alertWithOk(context,mobileSearchResponseModel.getErrorMessage());
                       /* noMemberLL.setVisibility(View.VISIBLE);
                        noMemberTV.setText(familyListResponseModel.getErrorMessage());
                        searchListRV.setVisibility(View.GONE);*/
                    }
                } else {
                    CustomAlert.alertWithOk(context, "Internal Server Error");

                   /* noMemberLL.setVisibility(View.VISIBLE);
                    noMemberTV.setText("Internal Server Error");
                    searchListRV.setVisibility(View.GONE);*/
                }
            }
        };
        if (mobileAsyncTask != null) {
            mobileAsyncTask.cancel(true);
            mobileAsyncTask = null;
        }

        mobileAsyncTask = new CustomAsyncTask(taskListener, "Please wait", context);
        mobileAsyncTask.execute();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        blockDetailActivity = (BlockDetailActivity) context;
    }
}
