package com.nhpm.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.Spinner;
import android.widget.TextView;

import com.customComponent.CustomAlert;
import com.customComponent.utility.ProjectPrefrence;
import com.google.zxing.client.result.VINParsedResult;
import com.nhpm.Models.SerachOptionItem;
import com.nhpm.Models.request.BeneficiarySearchModel;
import com.nhpm.Models.request.FamilyListRequestModel;
import com.nhpm.Models.response.BeneficiaryListItem;
import com.nhpm.Models.response.BeneficiaryModel;
import com.nhpm.Models.response.master.StateItem;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;
import com.nhpm.Utility.AppUtility;
import com.nhpm.activity.FamilyListActivity;
import com.nhpm.activity.FamilyListByHHIDActivity;
import com.nhpm.activity.FamilyMembersListActivity;
import com.nhpm.activity.PhoneNumberActivity;
import com.nhpm.activity.PinLoginActivity;
import com.nhpm.activity.SearchDashboardActivity;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by SUNAINA on 22-05-2018.
 */

public class BeneficiaryFamilySearchFragment extends Fragment {
    private Spinner cardTypeSpinner;
    private Context context;
    private EditText rationCardET, rsbyET, ahlTinET, mobileET,hhIdNoET;
    private ArrayList<BeneficiarySearchModel> searchModelArrayList;
    private TextView cardTypeTV, findByNameTV, noMemberTV;
    private Button searchBTN;
    private String cardNo = "", cardType = "";
    private ArrayList<BeneficiaryListItem> list;
    private StateItem selectedStateItem;
    private   FamilyListRequestModel request;


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
        selectedStateItem = StateItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SELECTED_STATE, context));

        searchBTN = (Button) view.findViewById(R.id.searchBTN);
        noMemberTV = (TextView) view.findViewById(R.id.noMemberTV);
        noMemberTV.setVisibility(View.GONE);
        findByNameTV = (TextView) view.findViewById(R.id.findByNameTV);
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

                Intent intent =new Intent(context,PinLoginActivity.class);
                intent.putExtra("Beneficiary","Beneficiary");
                SerachOptionItem item=new SerachOptionItem();
                item.setSearchType(AppConstant.AADHAAR_SEARCH);
                item.setMode(AppConstant.DEMO);
                ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_NAME,AppConstant.SEARCH_OPTION,item.serialize(),context);
                startActivity(intent);
            }
        });
        cardTypeTV = (TextView) view.findViewById(R.id.cardTypeTV);
        hhIdNoET = (EditText) view.findViewById(R.id.hhIdNoET);
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
        rationCardET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() > 0) {

                    rationCardET.setTextColor(AppUtility.getColor(context, R.color.black_shine));
                    if (rationCardET.getText().toString().length() == 15) {
                        rationCardET.setTextColor(AppUtility.getColor(context, R.color.green));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        rsbyET = (EditText) view.findViewById(R.id.rsbyET);
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
        ArrayList<String> spinnerList = new ArrayList<>();
        spinnerList.add("HHId Number");
        spinnerList.add("AHLTIN");


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
                if (position == 0) {
                    hhIdNoET.setVisibility(View.VISIBLE);
                    cardTypeTV.setText("HHId Number");
                    cardType = "HHId Number";
                } else if (position == 1) {
                    ahlTinET.setVisibility(View.VISIBLE);
                    cardTypeTV.setText("AHLTIN");
                    cardType = "AHLTIN";
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
                request=new FamilyListRequestModel();
                //
                if (!cardType.equalsIgnoreCase("") && cardType.equalsIgnoreCase("Ration Card")) {
                    cardNo = rationCardET.getText().toString();
                    if (cardNo.equalsIgnoreCase("")) {
                        CustomAlert.alertWithOk(context, "Please enter ration card number");
                        return;
                    } else if (cardNo.length() < 15) {
                        CustomAlert.alertWithOk(context, "Please enter valid ration card number");
                        return;
                    }

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

                }

                if (!cardType.equalsIgnoreCase("") && cardType.equalsIgnoreCase("AHLTIN")) {
                    cardNo = ahlTinET.getText().toString();
                    request.setAhlTinno(cardNo);

                    if (cardNo.equalsIgnoreCase("")) {
                        CustomAlert.alertWithOk(context, "Please enter AHLTIN number");
                        return;
                    } else if (cardNo.length() < 29) {
                        CustomAlert.alertWithOk(context, "Please enter valid AHLTIN number");
                        return;
                    }

                }

                if (!cardType.equalsIgnoreCase("") && cardType.equalsIgnoreCase("Mobile Number")) {
                    cardNo = mobileET.getText().toString();
                    if (cardNo.equalsIgnoreCase("")) {
                        CustomAlert.alertWithOk(context, "Please enter mobile number");
                        return;
                    } else if (cardNo.length() < 10) {
                        CustomAlert.alertWithOk(context, "Please enter valid mobile number");
                        return;
                    }

                }

                if (!cardType.equalsIgnoreCase("") && cardType.equalsIgnoreCase("HHId Number")) {
                    cardNo = hhIdNoET.getText().toString();
                    request.setHho_id(cardNo);
                    if (cardNo.equalsIgnoreCase("")) {
                        CustomAlert.alertWithOk(context, "Please enter HHId number");
                        return;
                    } /*else if (cardNo.length() < 24) {
                        CustomAlert.alertWithOk(context, "Please enter valid HHId number");
                        return;
                    }*/

                }
                request.setName("");
                request.setGenderid("");
                request.setAge("");
                request.setPincode("");
                request.setFathername("");
                Intent theIntent=new Intent(context,FamilyListByHHIDActivity.class);
                theIntent.putExtra("SearchParam",request);
                startActivity(theIntent);

                /*BeneficiaryModel beneficiaryModel = new BeneficiaryModel();
                beneficiaryModel.setBeneficiaryList(getList(cardNo, cardType));
                if (beneficiaryModel != null && beneficiaryModel.getBeneficiaryList() != null
                        && beneficiaryModel.getBeneficiaryList().size() > 0) {
                    Intent intent = new Intent(context, FamilyMembersListActivity.class);
                    intent.putExtra("result", beneficiaryModel);
                    intent.putExtra("cardNo",cardNo);
                    startActivity(intent);
                }*/
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

}
