package com.nhpm.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.customComponent.CustomAlert;
import com.customComponent.utility.DateTimeUtil;
import com.customComponent.utility.ProjectPrefrence;
import com.nhpm.DocCamera.ImageUtil;
import com.nhpm.LocalDataBase.dto.SeccDatabase;
import com.nhpm.Models.request.FamilyDetailsItemModel;
import com.nhpm.Models.request.PersonalDetailItem;
import com.nhpm.Models.response.DocsListItem;
import com.nhpm.Models.response.GovernmentIdItem;
import com.nhpm.Models.response.MemberListModel;
import com.nhpm.Models.response.ProofRelationItem;
import com.nhpm.Models.response.RelationItem;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;
import com.nhpm.Utility.AppUtility;
import com.nhpm.activity.CollectDataActivity;
import com.nhpm.activity.CollectMemberDataActivity;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;
import static com.nhpm.DocCamera.ImageUtil.rotateImage;

/**
 * A simple {@link Fragment} subclass.
 */
public class FamilyRelationFragment extends Fragment {
    private View view;
    private FamilyDetailsItemModel familyDetailsItemModel;
    private DocsListItem beneficiaryListItem;
    private Context context;
    private TextView nhaIdTV, relationTV;
    private EditText proofIdET, nameET, yobET;
    private Spinner relationSP, proofRelationSP;
    private RadioGroup genderRG;
    private RadioButton maleRB, femaleRB, otherRB;
    private String manualGenderSelection = "", currentYear;
    private ImageView proofDocIV;
    private Button nextBT, captureImageBT;
    private ArrayList<String> familyRelList;
    private CollectMemberDataActivity activity;
    private ArrayList<ProofRelationItem> proofOfRelList;
    private ArrayList<RelationItem> relationList;
    private final int BIRTH_CER = 1, MARRIAGE_CER = 2, RATION_CARD = 3;
    private String proofOfRelation = "", relationName = "";
    private Bitmap bitmap;
    private MemberListModel memberListModel;
    private String purpose = "", voterIdImg = "";
    private PersonalDetailItem personalDetailItem;
    private RelationItem relationItem;

    public FamilyRelationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_family_relation, container, false);
        context = getActivity();
        setupScreen(view);
        return view;
    }


    private void setupScreen(View v) {
        memberListModel = MemberListModel.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, "Golden_Data", context));
        // personalDetailItem = activity.benefItem.getPersonalDetail();
        personalDetailItem = PersonalDetailItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, "add member per", context));

        relationTV = (TextView) v.findViewById(R.id.relationTV);
        nhaIdTV = (TextView) v.findViewById(R.id.nhaIdTV);
        if (memberListModel != null) {
            if (memberListModel.getName() != null) {
                relationTV.setText("Relation with " + memberListModel.getName());
            }
            if (memberListModel.getId() != null) {
                nhaIdTV.setText(memberListModel.getId());
            }
        }
        proofIdET = (EditText) v.findViewById(R.id.proofIdET);
        nameET = (EditText) v.findViewById(R.id.nameET);
        yobET = (EditText) v.findViewById(R.id.yobET);
        genderRG = (RadioGroup) v.findViewById(R.id.genderRG);
        maleRB = (RadioButton) v.findViewById(R.id.maleRB);
        femaleRB = (RadioButton) v.findViewById(R.id.femaleRB);
        otherRB = (RadioButton) v.findViewById(R.id.otherRB);
        proofDocIV = (ImageView) v.findViewById(R.id.proofDocIV);
        captureImageBT = (Button) v.findViewById(R.id.captureImageBT);
        captureImageBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtility.openCamera(activity, FamilyRelationFragment.this, AppConstant.BACK_CAMREA_OPEN, "ForStore", "FamilyRelationFragment");
            }
        });

        genderRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == maleRB.getId()) {
                    manualGenderSelection = "M";
                } else if (checkedId == femaleRB.getId()) {
                    manualGenderSelection = "F";
                } else if (checkedId == otherRB.getId()) {
                    manualGenderSelection = "T";
                }

            }

        });
        yobET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() > 0) {
                    if (charSequence.toString().startsWith("1") || charSequence.toString().startsWith("2")) {
                        // if (Integer.parseInt(charSequence.toString().substring(1)) < 2) {

                        yobET.setTextColor(context.getResources().getColor(R.color.black_shine));

                        if (yobET.getText().toString().length() == 4) {
                            yobET.setTextColor(context.getResources().getColor(R.color.green));
                            // isValidMobile = true;
                        } else {
                            //isValidMobile = false;
                        }
                    } else {
                        //isValidMobile = false;
                        yobET.setTextColor(context.getResources().getColor(R.color.red));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        String currentDate = DateTimeUtil.currentDate("dd MM yyyy");
        Log.d("current date", currentDate);
        currentYear = currentDate.substring(6, 10);
        Log.d("current year", currentYear);
        nextBT = (Button) v.findViewById(R.id.nextBT);
        relationSP = (Spinner) v.findViewById(R.id.relationSP);
        getRelationList();
        relationSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
             /*   if (position == 0) {

                } else {*/
                relationItem = relationList.get(position);
                relationName = relationItem.getRelationName();
                Log.d("Rel name: ", relationName);
                //}
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        proofRelationSP = (Spinner) v.findViewById(R.id.proofRelationSP);
        getProofOfRelationList();
        proofRelationSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ProofRelationItem item = proofOfRelList.get(position);
                switch (item.proofRelCode) {
                    case 0:

                        break;
                    case BIRTH_CER:
                        proofOfRelation = item.proofRelIDName;
                        Log.d("relation id:", proofOfRelation);
                        break;
                    case MARRIAGE_CER:
                        proofOfRelation = item.proofRelIDName;
                        Log.d("relation id:", proofOfRelation);
                        break;
                    case RATION_CARD:
                        proofOfRelation = item.proofRelIDName;
                        Log.d("relation id:", proofOfRelation);
                        break;

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (personalDetailItem != null) {
            if (personalDetailItem.getAmRelationHAId() != null &&
                    !personalDetailItem.getAmRelationHAId().equalsIgnoreCase("")) {
                nhaIdTV.setText(personalDetailItem.getAmRelationHAId());
                nhaIdTV.setEnabled(false);
            }
            if (personalDetailItem.getAmRelation() != null &&
                    !personalDetailItem.getAmRelation().equalsIgnoreCase("")) {

                getRelationList();
                for (int i = 0; i < relationList.size(); i++) {
                    if (relationList.get(i).getRelationName().equalsIgnoreCase(personalDetailItem.getAmRelation())) {
                        relationSP.setSelection(i);
                        relationName = relationList.get(i).getRelationName();
                    }
                }
            }

            if (personalDetailItem.getBenefName() != null &&
                    !personalDetailItem.getBenefName().equalsIgnoreCase("")) {
                nameET.setText(personalDetailItem.getBenefName());
                nameET.setEnabled(false);
            }
            if (personalDetailItem.getAmProofDocNo() != null &&
                    !personalDetailItem.getAmProofDocNo().equalsIgnoreCase("")) {
                proofIdET.setText(personalDetailItem.getAmProofDocNo());
                proofIdET.setEnabled(false);
            }

            if (personalDetailItem.getYob() != null && personalDetailItem.getYob().length() > 4) {

                String currentYear = DateTimeUtil.currentDate(AppConstant.DATE_FORMAT);

                currentYear = currentYear.substring(0, 4);
                String date = DateTimeUtil.
                        convertTimeMillisIntoStringDate(DateTimeUtil.convertDateIntoTimeMillis(personalDetailItem.getYob()), AppConstant.DATE_FORMAT);
                String arr[];
                String aadhaarYear = null;
                if (personalDetailItem.getYob().contains("-")) {
                    arr = personalDetailItem.getYob().split("-");
                    if (arr[0].length() == 4) {
                        aadhaarYear = arr[0];
                    } else if (arr[2].length() == 4) {
                        aadhaarYear = arr[2];
                    }
                } else if (personalDetailItem.getYob().contains("/")) {
                    arr = personalDetailItem.getYob().split("/");
                    if (arr[0].length() == 4) {
                        aadhaarYear = arr[0];
                    } else if (arr[2].length() == 4) {
                        aadhaarYear = arr[2];
                    }
                }
                if (aadhaarYear != null) {
                    yobET.setText(aadhaarYear);
                    yobET.setEnabled(false);
                  /*  int age = Integer.parseInt(currentYear) - Integer.parseInt(aadhaarYear);
                    kycageTV.setText(age + "");*/
                }

            } else if (personalDetailItem.getYob() != null && personalDetailItem.getYob().length() == 4) {
                /*String currentYear = DateTimeUtil.currentDate("dd-mm-yyyy");
                currentYear = currentYear.substring(6, 10);
                int age = Integer.parseInt(currentYear) - Integer.parseInt(personalDetailItem.getYob());
                kycageTV.setText(age + "");*/
                yobET.setText(personalDetailItem.getYob());
                yobET.setEnabled(false);
            }

            /*if (personalDetailItem.getYob() != null &&
                    !personalDetailItem.getYob().equalsIgnoreCase("")) {
                yobET.setText(personalDetailItem.getYob());
                yobET.setEnabled(false);
            }*/
            if (personalDetailItem.getGender() != null &&
                    !personalDetailItem.getGender().equalsIgnoreCase("")) {

                if (personalDetailItem.getGender().equalsIgnoreCase("M") || personalDetailItem.getGender().equalsIgnoreCase("1")) {
                    maleRB.setChecked(true);
                    manualGenderSelection = "M";
                } else if (personalDetailItem.getGender().equalsIgnoreCase("F") ||
                        personalDetailItem.getGender().equalsIgnoreCase("2")) {
                    femaleRB.setChecked(true);
                    manualGenderSelection = "F";
                } else {
                    otherRB.setChecked(true);
                    manualGenderSelection = "T";
                }
                maleRB.setEnabled(false);
                femaleRB.setEnabled(false);
                otherRB.setEnabled(false);
            }

            if (personalDetailItem.getAmProofDocType() != null
                    && !personalDetailItem.getAmProofDocType().equalsIgnoreCase("")) {
                //spinner

                getProofOfRelationList();
                for (int i = 0; i < proofOfRelList.size(); i++) {
                    if (proofOfRelList.get(i).getProofRelIDName().equalsIgnoreCase(personalDetailItem.getAmProofDocType())) {
                        proofRelationSP.setSelection(i);
                        proofOfRelation = proofOfRelList.get(i).getProofRelIDName();
                    }
                }


            }

            if (personalDetailItem.getAmProofDocPhoto() != null
                    && !personalDetailItem.getAmProofDocPhoto().equalsIgnoreCase("")) {
                voterIdImg = personalDetailItem.getAmProofDocPhoto();
                proofDocIV.setImageBitmap(AppUtility.convertStringToBitmap(voterIdImg));
                captureImageBT.setEnabled(false);


            }


        } else {
            personalDetailItem = new PersonalDetailItem();
        }
        nextBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String proofId = proofIdET.getText().toString();
                String name = nameET.getText().toString();
                String yob = yobET.getText().toString();
                String nhaid = nhaIdTV.getText().toString();

                if (relationName.equalsIgnoreCase("")) {
                    CustomAlert.alertWithOk(context, "Please select relation");
                    return;
                }

                if (proofOfRelation.equalsIgnoreCase("")) {
                    CustomAlert.alertWithOk(context, "Please select proof of relation");
                    return;
                }

                if (proofId.equalsIgnoreCase("")) {
                    CustomAlert.alertWithOk(context, "Please enter document number");
                    return;
                }
                if (name.equalsIgnoreCase("")) {
                    CustomAlert.alertWithOk(context, "Please enter name");
                    return;
                }


                if (yob.trim().equalsIgnoreCase("")) {
                    CustomAlert.alertWithOk(context, "Please enter year of birth");
                    return;
                }

                if (yob != null && !yob.equalsIgnoreCase("")) {
                    int yearRange = Integer.parseInt(currentYear) - 100;

                    if (yob.equalsIgnoreCase(currentYear) || Integer.parseInt(yob) < yearRange) {
                        CustomAlert.alertWithOk(context, "Please enter valid year of birth");
                        return;
                    }

                }

                if (manualGenderSelection != null && manualGenderSelection.equalsIgnoreCase("")) {
                    CustomAlert.alertWithOk(context, "Please select gender");
                    return;
                }

                if (voterIdImg == null || voterIdImg.equalsIgnoreCase("")) {
                    CustomAlert.alertWithOk(context, "Please capture proof document image");
                    return;
                }

                personalDetailItem.setAmRelationHAId(nhaid);
                personalDetailItem.setAmRelation(relationName);
                personalDetailItem.setAmProofDocNo(proofId);
                personalDetailItem.setAmProofDocType(proofOfRelation);
                personalDetailItem.setAmProofDocPhoto(voterIdImg);
                personalDetailItem.setBenefName(name);
                personalDetailItem.setYob(yob);
                personalDetailItem.setGender(manualGenderSelection);

                Log.d("member personal detail", personalDetailItem.serialize());
                if (beneficiaryListItem == null) {
                    beneficiaryListItem = new DocsListItem();
                }
                familyDetailsItemModel = FamilyDetailsItemModel.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, "member-family", context));
                if (familyDetailsItemModel != null) {
                    beneficiaryListItem.setFamilyDetailsItemModel(familyDetailsItemModel);

                }else {
                    familyDetailsItemModel = new FamilyDetailsItemModel();
                    familyDetailsItemModel.setIdNumber(memberListModel.getFamilyIdNumber());
                    beneficiaryListItem.setFamilyDetailsItemModel(familyDetailsItemModel);

                }
                beneficiaryListItem.setPersonalDetail(personalDetailItem);
                beneficiaryListItem.setHhd_no(memberListModel.getHhdNo());
                activity.benefItem = beneficiaryListItem;


                activity.addFamilyRelationLL.setBackground(context.getResources().getDrawable(R.drawable.arrow));
                activity.personalDetailsLL.setBackground(context.getResources().getDrawable(R.drawable.arrow_yellow));


                Fragment fragment = new MemberPersonalDetailsFragment();
                Bundle args = new Bundle();
                args.putString("Family_Rel", "FamilyRelationFrag");
                fragment.setArguments(args);
                CallFragment(fragment);

            }
        });


    }

    public void CallFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragContainer, fragment);
            fragmentTransaction.commit();
        }
    }

    private void getProofOfRelationList() {
        proofOfRelList = AppUtility.prepareProofOfRelationList();
        ArrayList<String> spinnerProofList = new ArrayList<>();

        for (ProofRelationItem item : proofOfRelList) {
            spinnerProofList.add(item.proofRelIDName);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.custom_drop_down, R.id.textView, spinnerProofList);
        proofRelationSP.setAdapter(adapter);
    }

    private void getRelationList() {
        relationList = SeccDatabase.getRelationList(context);
        ArrayList<String> spinnerRelationList = new ArrayList<>();
        //spinnerRelationList.add("Select Relation");

        for (RelationItem item : relationList) {
            spinnerRelationList.add(item.getRelationName());
        }
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(context, R.layout.custom_drop_down, R.id.textView, spinnerRelationList);
        relationSP.setAdapter(adapter1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppConstant.REQ_CAMERA && resultCode == RESULT_OK) {
            final Intent intent = data;//new Intent();
            String path = intent.getStringExtra("response");
            Uri uri = Uri.fromFile(new File(path));
            if (uri == null) {
                Log.d("uri", "null");
            } else {
                bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                File mediaFile = null;
                if (bitmap != null) {
                    byte[] imageBytes = ImageUtil.bitmapToByteArray(rotateImage(bitmap, 270));

                    File mediaStorageDir = new File(context.getExternalFilesDir(Environment.DIRECTORY_DCIM), "");
                    mediaFile = new File(mediaStorageDir.getPath() + File.separator + purpose + ".jpg");
                    if (mediaFile != null) {
                        try {
                            FileOutputStream fos = new FileOutputStream(mediaFile);
                            fos.write(imageBytes);
                            fos.close();
                        } catch (FileNotFoundException e) {

                        } catch (IOException e) {

                        }
                    }
                }
            }

            voterIdImg = AppUtility.converBitmapToBase64(bitmap);
            if (voterIdImg != null) {
                proofDocIV.setImageBitmap(bitmap);
            }

        }
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        activity = (CollectMemberDataActivity) context;
        //beneficiaryListItem = activity.benefItem;
    }
}
