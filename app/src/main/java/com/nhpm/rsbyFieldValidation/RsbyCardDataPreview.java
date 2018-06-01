package com.nhpm.rsbyFieldValidation;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.customComponent.utility.ProjectPrefrence;
import com.nhpm.LocalDataBase.dto.SeccDatabase;
import com.nhpm.Models.response.rsbyMembers.RSBYPoliciesItem;
import com.nhpm.Models.response.rsbyMembers.RsbyPoliciesCompany;
import com.nhpm.Models.response.seccMembers.HouseHoldItem;
import com.nhpm.Models.response.seccMembers.SeccMemberItem;
import com.nhpm.Models.response.seccMembers.SelectedMemberItem;
import com.nhpm.Models.response.verifier.VerifierLocationItem;
import com.nhpm.activity.BaseActivity;
import com.nhpm.Models.response.rsbyMembers.RSBYItem;
import com.nhpm.Models.response.rsbyMembers.RsbyHouseholdItem;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;
import com.nhpm.Utility.AppUtility;
import com.nhpm.activity.SearchActivityWithHouseHold;
import com.nhpm.fragments.HouseholdStatusBoardFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by Saurabh on 09-03-2017.
 */

public class RsbyCardDataPreview extends BaseActivity {
    public static final String RSBYDASHBOARD = "3";
    public static final String TAG_RSBYSMARTCARDDETAILS = "RsbySmartCardDetails";
    public static final String TAG_URN = "urn";
    public static final String TAG_ISSUETIMESPAM = "issueTimeSpam";
    public static final String TAG_ID = "id";
    public static final String TAG_FAMILYPHOTOBASE64 = "familyphotobase64";
    public static final String TAG_BASE64STRING = "base64string";
    public static final String TAG_FAMILYDETAILS = "familydetails";
    public static final String TAG_DOORHOUSE = "doorhouse";
    public static final String TAG_PANCHYATTOWNCODE = "panchyattowncode";
    public static final String TAG_HOFNAMEREG = "hofnamereg";
    public static final String TAG_VILLAGECODE = "villagecode";
    public static final String TAG_STATECODE = "statecode";
    public static final String TAG_DISTRICTCODE = "districtcode";
    public static final String TAG_BLOCKCODE = "blockcode";
    public static final String TAG_ALLFAMILYMEMBER = "allfamilymember";
    public static final String TAG_FAMILYMEMBER = "familymember";
    public static final String TAG_POLICYDETAILS = "policydetails";
    public static final String TAG_ENDDATE = "enddate";
    public static final String TAG_INSCCODE = "insccode";
    public static final String TAG_POLICYNO = "policyno";
    public static final String TAG_POLICYAMT = "policyamt";
    public static final String TAG_STARTDATE = "startdate";
    public static final String TAG_GENDER = "gender";
    public static final String TAG_DOB = "dob";
    public static final String TAG_RELCODE = "relcode";
    public static final String TAG_NAME = "name";
    public static final String TAG_FINGERPRINT = "fingerprint";
    public static final String TAG_MEMID = "memid";
    public static final String TAG_CARDDETAILS = "Carddetails";
    public static final String TAG_CARDTYPE = "Cardtype";
    public static final String TAG_CARDCATEGORY = "CardCategory";
    public static final String TAG_CSN = "CSN";
    public static final int DUPLICATE_CARD = 2;
    public static final int INVALID_CARD_CATEGORY = 3;
    public static final int CARD_EXPIRE = 4;
    public static final int INVALID_STATECODE = 5;
    public static final int INVALID_DISTRICTCODE = 6;
    public static final int CARD_SAVED_SUCESSFULLY = 0;

    private VerifierLocationItem downloadedLocation;
    private RecyclerView memberDetailRecycleView;
    private TextView urnNumber, issuetimeSpam, hofName, doorhouse, villagecode,
            panchyattowncode, blockcode, districtcode, statecode, insccode, policyno,
            policyamt, startdate, enddate, cardScn, cardCategory, cardType, errorTV, cardActivatedTV;
    private ImageView householdImage, errorIndicatorIV;
    private Button importCard, cancel;
    // private ArrayList<RSBYItem> rsbyItemList;
    private ArrayList<SeccMemberItem> rsbyItemList;
    private HouseHoldItem rsbyHouseholdItem;
    private RsbyCardPreviewAdapter adapter;
    private Context context;
    private String XML = null;
    private SelectedMemberItem selectedMemItem;
    private String policyNo, stateCode, districtCode;
    private StringBuilder errorMessageBuilder;
    private boolean checksEnable = true;
    private boolean isRenewed = false;
    private String renewedDate = "";
    private int menitiuiaCount = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rsby_carddetail_preview);
        context = this;
        downloadedLocation = VerifierLocationItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF
                , AppConstant.SELECTED_BLOCK, context));
        cancel = (Button) findViewById(R.id.cancel);
        cancel.bringToFront();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertWithOkReturn(context, context.getResources().getString(R.string.disconnectCard));

            }
        });
        XML = getIntent().getStringExtra("XML");
        if (XML != null && !XML.equalsIgnoreCase("")) {
            JSONObject soapDatainJsonObject = null;
            try {
                soapDatainJsonObject = org.json.XML.toJSONObject(XML);
            } catch (JSONException e) {
                Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
            if (soapDatainJsonObject != null) {
                if (parserCardData(soapDatainJsonObject.toString())) {
                    setUpScreen();
                } else {
                    alertWithOk(context, context.getResources().getString(R.string.invaliCardData), 0);
                }
            } else {
                alertWithOk(context, context.getResources().getString(R.string.invaliCardData), 0);
            }
        } else {
            alertWithOk(context, context.getResources().getString(R.string.invaliCardData), 0);
        }

    }

    public void alertWithOk(Context mContext, String msg, final int nav) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(context.getResources().getString(R.string.Alert));
        builder.setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(context.getResources().getString(R.string.OK), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                        /*if (nav == 0) {
                          *//*  Intent returnIntent = new Intent(context, SearchActivityWithHouseHold.class);
                            startActivity(returnIntent);*//*
                            finish();


                        } else {
                            ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.DASHBOARD_TAB_STATUS, SearchActivityWithHouseHold.RSBYDASHBOARD, context);
                            ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.HOUSEHOLD_TAB_STATUS, 2 + "", context);
                            Intent returnIntent = new Intent(context, SearchActivityWithHouseHold.class);
                            startActivity(returnIntent);
                            finish();
                        }*/
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void setUpScreen() {

        AppUtility.alertWithOk(context, context.getResources().getString(R.string.cardReadedPreviewScreen));
        errorTV = (TextView) findViewById(R.id.errorTV);
        errorIndicatorIV = (ImageView) findViewById(R.id.errorIndicatorIV);
        householdImage = (ImageView) findViewById(R.id.householdImage);
        importCard = (Button) findViewById(R.id.importCard);
        cardActivatedTV = (TextView) findViewById(R.id.cardActivatedTV);
        memberDetailRecycleView = (RecyclerView) findViewById(R.id.memberDetailRecycleView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        memberDetailRecycleView.setLayoutManager(mLayoutManager);
        memberDetailRecycleView.setItemAnimator(new DefaultItemAnimator());
        adapter = new RsbyCardPreviewAdapter(rsbyItemList);
        memberDetailRecycleView.setAdapter(adapter);
        urnNumber = (TextView) findViewById(R.id.urnNumber);
        issuetimeSpam = (TextView) findViewById(R.id.issuetimeSpam);
        hofName = (TextView) findViewById(R.id.hofName);
        doorhouse = (TextView) findViewById(R.id.doorhouse);
        villagecode = (TextView) findViewById(R.id.villagecode);
        panchyattowncode = (TextView) findViewById(R.id.panchyattowncode);
        blockcode = (TextView) findViewById(R.id.blockcode);
        districtcode = (TextView) findViewById(R.id.districtcode);
        statecode = (TextView) findViewById(R.id.statecode);
        insccode = (TextView) findViewById(R.id.insccode);
        policyno = (TextView) findViewById(R.id.policyno);
        policyamt = (TextView) findViewById(R.id.policyamt);
        startdate = (TextView) findViewById(R.id.startdate);
        enddate = (TextView) findViewById(R.id.enddate);
        cardScn = (TextView) findViewById(R.id.cardScn);
        cardCategory = (TextView) findViewById(R.id.cardCategory);
        cardCategory.setTextColor(context.getResources().getColor(R.color.green));
        cardType = (TextView) findViewById(R.id.cardType);
        importCard.bringToFront();
        importCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int status;
                if (XML != null && !XML.equalsIgnoreCase("")) {
                    JSONObject soapDatainJsonObject = null;
                    try {
                        soapDatainJsonObject = org.json.XML.toJSONObject(XML);
                    } catch (JSONException e) {
                        Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                    if (soapDatainJsonObject != null) {
                        status = parseDataForDatabase(soapDatainJsonObject.toString());
                        if (status == DUPLICATE_CARD) {
                            alertWithOk(context, context.getResources().getString(R.string.dublicateCard), 0);
                        }

                        if (menitiuiaCount > 0) {
                            AppUtility.alertWithOk(context, context.getResources().getString(R.string.noMenitiaDetail1) + " " + menitiuiaCount + " " + context.getResources().getString(R.string.noMenitiaDetail));
                        }
                        if (errorMessageBuilder != null && !errorMessageBuilder.toString().equalsIgnoreCase("")) {
                            alertForErrors(errorMessageBuilder.toString());
                        }
                        /*else if (status == INVALID_CARD_CATEGORY) {
                            alertWithOk(context, context.getResources().getString(R.string.cardCategoryNotMatch), 0);
                        } else if (status == CARD_EXPIRE) {
                            alertWithOk(context, context.getResources().getString(R.string.cardExpire), 0);
                        } else if (status == INVALID_STATECODE) {
                            alertWithOk(context, context.getResources().getString(R.string.cardInvalidStateCode), 0);
                        } else if (status == CARD_SAVED_SUCESSFULLY) {
                            alertWithOk(context, context.getResources().getString(R.string.cardSavedSucess), 1);
                        } else if (status == INVALID_DISTRICTCODE) {
                            alertWithOk(context, context.getResources().getString(R.string.cardInvalidDistrictCode), 0);
                        }*/
                    }
                }
            }
        });

        if (rsbyHouseholdItem != null) {
            errorMessageBuilder = new StringBuilder();
            if (rsbyHouseholdItem.getRsbyFamilyPhoto() != null) {
                householdImage.setImageBitmap(convertStringToBitmap(rsbyHouseholdItem.getRsbyFamilyPhoto()));
            }
            if (rsbyHouseholdItem.getRsbyUrnId() != null) {
                urnNumber.setText(context.getResources().getString(R.string.urnNo) + AppUtility.formatUrn(rsbyHouseholdItem.getRsbyUrnId()));
            }
            if (rsbyHouseholdItem.getRsbyIssuesTimespam() != null) {
                issuetimeSpam.setText(context.getResources().getString(R.string.issueTimeStamp) + AppUtility.convertRsbyIssueTimeDateFormat(rsbyHouseholdItem.getRsbyIssuesTimespam()));
            }

            if (rsbyHouseholdItem.getRsbyHofnamereg() != null) {
                String name = "";
                try {
                    name = new String(rsbyHouseholdItem.getRsbyHofnamereg().getBytes(), "UTF-8");
                } catch (UnsupportedEncodingException e) {

                    e.printStackTrace();
                }

                String decodedName = Html.fromHtml(name).toString();
                hofName.setText(context.getResources().getString(R.string.hofName) + decodedName);
            }
            if (rsbyHouseholdItem.getRsbyDoorhouse() != null) {
                doorhouse.setText(context.getResources().getString(R.string.doorHouse) + rsbyHouseholdItem.getRsbyDoorhouse());
            }
            if (rsbyHouseholdItem.getRsbyVillageCode() != null) {
                villagecode.setText(context.getResources().getString(R.string.villageCode) + rsbyHouseholdItem.getRsbyVillageCode());
            }
            if (rsbyHouseholdItem.getRsbyPanchyatTownCode() != null) {
                panchyattowncode.setText(context.getResources().getString(R.string.panchayatCode) + rsbyHouseholdItem.getRsbyPanchyatTownCode());
            }
            if (rsbyHouseholdItem.getRsbyBlockCode() != null) {
                blockcode.setText(context.getResources().getString(R.string.blockCode) + rsbyHouseholdItem.getRsbyBlockCode());
            }
            if (rsbyHouseholdItem.getRsbyDistrictCode() != null) {
                districtcode.setText(context.getResources().getString(R.string.districtCode) + rsbyHouseholdItem.getRsbyDistrictCode());
            }
            if (rsbyHouseholdItem.getRsbyStateCode() != null) {
                statecode.setText(context.getResources().getString(R.string.stateCode) + rsbyHouseholdItem.getRsbyStateCode());
            }
            if (rsbyHouseholdItem.getRsbyEnddate() != null) {
                enddate.setText(context.getResources().getString(R.string.endDate) + AppUtility.convertRsbyDateFormat(rsbyHouseholdItem.getRsbyEnddate()));
            }
            if (rsbyHouseholdItem.getRsbyInsccode() != null) {


                insccode.setText(context.getResources().getString(R.string.insuranceCompanyCode) + getCompanyName(rsbyHouseholdItem.getRsbyInsccode()) + "(" + rsbyHouseholdItem.getRsbyInsccode() + ")");
            }
            if (rsbyHouseholdItem.getRsbyPolicyamt() != null) {
                policyamt.setText(context.getResources().getString(R.string.policyAmount) + rsbyHouseholdItem.getRsbyPolicyamt());
            }
            if (rsbyHouseholdItem.getRsbyPolicyno() != null) {
                policyno.setText(context.getResources().getString(R.string.policyNo) + rsbyHouseholdItem.getRsbyPolicyno());
            }
            if (rsbyHouseholdItem.getRsbyStartdate() != null) {
                startdate.setText(context.getResources().getString(R.string.startDate) + AppUtility.convertRsbyDateFormat(rsbyHouseholdItem.getRsbyStartdate()));
            }
            if (rsbyHouseholdItem.getRsbyCsmNo() != null) {
                cardScn.setText(context.getResources().getString(R.string.cardCsn) + rsbyHouseholdItem.getRsbyCsmNo());
            }
            if (rsbyHouseholdItem.getRsbyCardType() != null) {
                cardType.setText(context.getResources().getString(R.string.cardType) + rsbyHouseholdItem.getRsbyCardType());
            }
            if (rsbyHouseholdItem.getRsbyCardCategory() != null) {
                if (AppUtility.getCardCategoryName(context, rsbyHouseholdItem.getRsbyCardCategory()) != null && !AppUtility.getCardCategoryName(context, rsbyHouseholdItem.getRsbyCardCategory()).equalsIgnoreCase("")) {
                    cardCategory.setText(context.getResources().getString(R.string.cardCategory) + rsbyHouseholdItem.getRsbyCardCategory() + "(" + AppUtility.getCardCategoryName(context, rsbyHouseholdItem.getRsbyCardCategory()) + ")");
                } else {
                    cardCategory.setText(context.getResources().getString(R.string.cardCategory) + rsbyHouseholdItem.getRsbyCardCategory());
                }

            }


            if (!downloadedLocation.getStateCode().equalsIgnoreCase(rsbyHouseholdItem.getRsbyStateCode())) {
                errorIndicatorIV.setImageDrawable(context.getResources().getDrawable(R.drawable.cancel));
                //    errorTV.setText(context.getResources().getString(R.string.cardInvalidStateCode));
                errorMessageBuilder.append(context.getResources().getString(R.string.cardInvalidStateCode) + "\n");
                importCard.setEnabled(true);
            }

            if (!downloadedLocation.getDistrictCode().equalsIgnoreCase(rsbyHouseholdItem.getRsbyDistrictCode())) {
                errorIndicatorIV.setImageDrawable(context.getResources().getDrawable(R.drawable.cancel));
                //errorTV.setText(context.getResources().getString(R.string.cardInvalidDistrictCode));
                errorMessageBuilder.append(context.getResources().getString(R.string.cardInvalidDistrictCode) + "\n");
                importCard.setEnabled(true);
            }

            if (!AppUtility.isValidCard(context, rsbyHouseholdItem.getRsbyCardCategory())) {
                errorIndicatorIV.setImageDrawable(context.getResources().getDrawable(R.drawable.cancel));
                //  errorTV.setText(context.getResources().getString(R.string.cardCategoryNotMatch));
                errorMessageBuilder.append(context.getResources().getString(R.string.cardCategoryNotMatch) + "\n");
                importCard.setEnabled(true);

            }
            if (!AppUtility.compareCardExpDate(AppUtility.currentDateLong(), AppUtility.convertRsbyDateToLong(AppUtility.convertRsbyDateFormat(rsbyHouseholdItem.getRsbyEnddate()), AppConstant.RSBY_DATE_FORMAT))) {
                if (!checkPolicy(rsbyHouseholdItem.getRsbyPolicyno(),
                        rsbyHouseholdItem.getRsbyStateCode(), rsbyHouseholdItem.getRsbyDistrictCode())) {
                    errorIndicatorIV.setImageDrawable(context.getResources().getDrawable(R.drawable.cancel));
                    //   errorTV.setText(context.getResources().getString(R.string.cardExpire));
                    errorMessageBuilder.append(context.getResources().getString(R.string.cardExpire));
                    importCard.setEnabled(true);
                }
            }
            if (!errorMessageBuilder.toString().trim().equalsIgnoreCase("")) {
                errorTV.setText(errorMessageBuilder.toString());
                errorTV.setTextColor(context.getResources().getColor(R.color.red));
                //   alertForErrors(errorMessageBuilder.toString());
            } else {
                errorTV.setTextColor(context.getResources().getColor(R.color.green));
            }
            if (isRenewed) {
                cardActivatedTV.setVisibility(View.VISIBLE);
                cardActivatedTV.setText("(" + context.getResources().getString(R.string.cardRenew) + renewedDate + ")");
            }
        }

    }

    public class RsbyCardPreviewAdapter extends RecyclerView.Adapter<RsbyCardPreviewAdapter.MyViewHolder> {

        private ArrayList<SeccMemberItem> householdList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView nameTV, dateOfBirthTV, genderTV, memberIdTV,
                    memberRelationCode, menutiaDetails;
            public ImageView menutiaIV;

            public MyViewHolder(View view) {
                super(view);
                nameTV = (TextView) view.findViewById(R.id.memberName);
                dateOfBirthTV = (TextView) view.findViewById(R.id.memberDob);
                genderTV = (TextView) view.findViewById(R.id.memberGender);
                memberIdTV = (TextView) view.findViewById(R.id.memberId);
                memberRelationCode = (TextView) view.findViewById(R.id.memberRelationCode);
                menutiaDetails = (TextView) view.findViewById(R.id.menutiaDetails);
                menutiaIV = (ImageView) view.findViewById(R.id.menutiaIV);
            }
        }

        public RsbyCardPreviewAdapter(ArrayList<SeccMemberItem> arrayList) {
            this.householdList = arrayList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.rsby_carddetails_item, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            final SeccMemberItem item = householdList.get(position);
            if (item.getName() != null) {
                holder.nameTV.setText(context.getResources().getString(R.string.memberName) + " " + item.getName());
            } else {
                holder.nameTV.setText(context.getResources().getString(R.string.memberName) + " " + item.getRsbyName());
            }
            if (item.getRsbyGender() != null) {
                if (item.getRsbyGender().equalsIgnoreCase(AppConstant.MALE_GENDER)) {
                    holder.genderTV.setText(context.getResources().getString(R.string.genderMale));
                } else if (item.getRsbyGender().equalsIgnoreCase(AppConstant.FEMALE_GENDER)) {
                    holder.genderTV.setText(context.getResources().getString(R.string.genderFemale));
                } else {
                    holder.genderTV.setText(context.getResources().getString(R.string.genderOther));

                }
            }

            if (item.getRsbyDob() != null) {
                holder.dateOfBirthTV.setText(context.getResources().getString(R.string.dateOfBirth) + " " +
                        AppUtility.convertRsbyDateFormat(item.getRsbyDob()));
            }
            if (item.getRsbyMemid() != null) {
                holder.memberIdTV.setText(context.getResources().getString(R.string.memberId) + " " + item.getRsbyMemid());
            }
            if (item.getRsbyRelcode() != null) {
                holder.memberRelationCode.setText(context.getResources().getString(R.string.relationName) + " " + AppUtility.getRsbyMemberRelationName(context, Integer.parseInt(item.getRsbyRelcode())));
            }

            if (item.getRsbyCardMenutiaDetail() != null && !item.getRsbyCardMenutiaDetail().equalsIgnoreCase("")) {
                if (item.getRsbyCardMenutiaDetail().equalsIgnoreCase(AppConstant.MENUTIA_DETAIL_AVAILABLE)) {
                    holder.menutiaDetails.setText(context.getResources().getString(R.string.minutiaeAval));
                    holder.menutiaIV.setImageDrawable(context.getResources().getDrawable(R.drawable.right_tick));
                } else {
                    holder.menutiaDetails.setText(context.getResources().getString(R.string.minutiaeUnAval));
                    holder.menutiaIV.setImageDrawable(context.getResources().getDrawable(R.drawable.cancel));
                }

            }
        }

        @Override
        public int getItemCount() {
            return householdList.size();
        }
    }

/*
    private boolean parserCardData(String data) {
        String URNNO = null;
        rsbyItemList = new ArrayList<RSBYItem>();
        boolean tags = false;
        JSONObject json = null;
        try {
            try {
                json = new JSONObject(data);
                String reqJson = data.toString();
                System.out.print(reqJson);
            } catch (JSONException e) {
                Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
            JSONObject RsbySmartCardDetails_obj = null;
            JSONObject urn_obj = null;
            String str_issueTimeSpam = null;
            String str_id = null;
            JSONObject familyphotobase64_obj = null;
            JSONObject familydetails_obj = null;
            JSONObject policydetails_obj = null;
            String str_base64string = null;
            String str_doorhouse = null;
            String str_panchyattowncode = null;
            String str_hofnamereg = null;
            String str_villagecode = null;
            String str_statecode = null;
            String str_districtcode = null;
            String str_menutiadetail = null;
            String str_blockcode = null;
            String str_enddate = null;
            String str_insccode = null;
            String str_policyno = null;
            String str_policyamt = null;
            String str_startdate = null;
            JSONObject allfamilymember_obj = null;
            JSONArray familymember = null;
            JSONObject Carddetails_obj = null;
            String str_CSN = null;
            String str_CardCategory = null;
            String str_Cardtype = null;
            rsbyHouseholdItem = new RsbyHouseholdItem();
            try {


                RsbySmartCardDetails_obj = json.getJSONObject(TAG_RSBYSMARTCARDDETAILS);
            } catch (JSONException e) {
                Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
            try {
                urn_obj = RsbySmartCardDetails_obj.getJSONObject(TAG_URN);
            } catch (JSONException e) {

                Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
            try {
                str_issueTimeSpam = urn_obj.getString(TAG_ISSUETIMESPAM);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                str_id = urn_obj.getString(TAG_ID);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            URNNO = str_id;

            rsbyHouseholdItem.setIssuesTimespam(str_issueTimeSpam);
            rsbyHouseholdItem.setUrnId(URNNO);
            try {
                familyphotobase64_obj = RsbySmartCardDetails_obj.getJSONObject(TAG_FAMILYPHOTOBASE64);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                str_base64string = familyphotobase64_obj.getString(TAG_BASE64STRING);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            rsbyHouseholdItem.setHofImage(str_base64string);
            try {
                familydetails_obj = RsbySmartCardDetails_obj.getJSONObject(TAG_FAMILYDETAILS);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                str_doorhouse = familydetails_obj.getString(TAG_DOORHOUSE);
                rsbyHouseholdItem.setDoorhouse(str_doorhouse
                );
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                str_panchyattowncode = familydetails_obj.getString(TAG_PANCHYATTOWNCODE);
                rsbyHouseholdItem.setPanchyatTownCode(str_panchyattowncode);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                str_hofnamereg = familydetails_obj.getString(TAG_HOFNAMEREG);
                rsbyHouseholdItem.setHofnamereg(str_hofnamereg);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                str_villagecode = familydetails_obj.getString(TAG_VILLAGECODE);
                rsbyHouseholdItem.setVillageCode(str_villagecode);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                str_statecode = familydetails_obj.getString(TAG_STATECODE);
                stateCode = str_statecode;
                rsbyHouseholdItem.setStateCode(str_statecode);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                str_districtcode = familydetails_obj.getString(TAG_DISTRICTCODE);
                rsbyHouseholdItem.setDistrictCode(str_districtcode);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                str_blockcode = familydetails_obj.getString(TAG_BLOCKCODE);
                rsbyHouseholdItem.setBlockCode(str_blockcode);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                policydetails_obj = RsbySmartCardDetails_obj.getJSONObject(TAG_POLICYDETAILS);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (policydetails_obj != null) {
                try {

                    str_enddate = policydetails_obj.getString(TAG_ENDDATE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    rsbyHouseholdItem.setEnddate(str_enddate);
                    str_insccode = policydetails_obj.getString(TAG_INSCCODE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    rsbyHouseholdItem.setInsccode(str_insccode);
                    str_policyno = policydetails_obj.getString(TAG_POLICYNO);
                    policyNo = str_policyno;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    rsbyHouseholdItem.setPolicyno(str_policyno);
                    str_policyamt = policydetails_obj.getString(TAG_POLICYAMT);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    rsbyHouseholdItem.setPolicyamt(str_policyamt);
                    str_startdate = policydetails_obj.getString(TAG_STARTDATE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    rsbyHouseholdItem.setStartdate(str_startdate);
                    allfamilymember_obj = RsbySmartCardDetails_obj.getJSONObject(TAG_ALLFAMILYMEMBER);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            try {
                familymember = allfamilymember_obj.getJSONArray(TAG_FAMILYMEMBER);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            for (int familymember_i = 0; familymember_i < familymember.length(); familymember_i++) {
                JSONObject familymember_obj = null;
                try {
                    familymember_obj = familymember.getJSONObject(familymember_i);
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }

                String str_gender = null;
                try {
                    str_gender = familymember_obj.getString(TAG_GENDER);
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                String str_dob = null;
                try {
                    str_dob = familymember_obj.getString(TAG_DOB);
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                String str_relcode = null;
                try {
                    str_relcode = familymember_obj.getString(TAG_RELCODE);
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                String str_name = null;
                try {
                    str_name = familymember_obj.getString(TAG_NAME);
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                try {
                    str_menutiadetail = familymember_obj.getString(TAG_FINGERPRINT);
                } catch (Exception ex) {

                }
                String str_memid = null;
                try {
                    str_memid = familymember_obj.getString(TAG_MEMID);
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }

                try {
                    Carddetails_obj = RsbySmartCardDetails_obj.getJSONObject(TAG_CARDDETAILS);

                } catch (Exception ex) {

                }
                try {
                    str_Cardtype = Carddetails_obj.getString(TAG_CARDTYPE);
                } catch (Exception ex) {

                }
                try {
                    str_CardCategory = Carddetails_obj.getString(TAG_CARDCATEGORY);
                } catch (Exception ex) {

                }
                try {
                    str_CSN = Carddetails_obj.getString(TAG_CSN);
                } catch (Exception ex) {

                }


                RSBYItem rsbyItem = new RSBYItem();
                rsbyItem.setGender(str_gender);
                rsbyItem.setDob(str_dob);
                rsbyItem.setUrnId(URNNO);
                rsbyItem.setCsmNo(str_CSN);
           //     rsbyItem.setMenutiaDetail(str_menutiadetail);
                rsbyItem.setCardType(str_Cardtype);
                rsbyItem.setCardCategory(str_CardCategory);
                rsbyItem.setUrnNo(URNNO);
                rsbyItem.setName(str_name);
                rsbyItem.setMemid(str_memid);
                rsbyItem.setRelcode(str_relcode);
                rsbyItem.setRsbyMemId(URNNO + str_memid);
                rsbyHouseholdItem.setEnddate(str_enddate);
                rsbyItem.setInsccode(str_insccode);
                rsbyItem.setPolicyno(str_policyno);
                rsbyItem.setPolicyamt(str_policyamt);
                rsbyItem.setStartdate(str_startdate);
                rsbyItem.setEnddate(str_enddate);




                if (str_relcode.equalsIgnoreCase("01")) {
                    //  rsbyItem.setMemberPhoto1(str_base64string);
                    rsbyHouseholdItem.setRsbyMemId(URNNO + str_memid);
                    rsbyHouseholdItem.setGender(str_gender);
                    rsbyHouseholdItem.setDob(str_dob);
                    rsbyHouseholdItem.setUrnId(URNNO);
                    rsbyHouseholdItem.setName(str_name);
                    rsbyHouseholdItem.setCsmNo(str_CSN);
                    rsbyHouseholdItem.setCardType(str_Cardtype);
                    rsbyHouseholdItem.setCardCategory(str_CardCategory);
                    rsbyHouseholdItem.setMemid(str_memid);
                    rsbyHouseholdItem.setRelcode(str_relcode);
                }
                rsbyItemList.add(rsbyItem);
            }
        } catch (Exception e) {
            return false;
        }

        return true;

    }
*/


    private boolean parserCardData(String data) {
        String URNNO = null;
        rsbyItemList = new ArrayList<SeccMemberItem>();
        boolean tags = false;
        JSONObject json = null;
        try {
            try {
                json = new JSONObject(data);
                String reqJson = data.toString();
                System.out.print(reqJson);
            } catch (JSONException e) {
                Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
            JSONObject RsbySmartCardDetails_obj = null;
            JSONObject urn_obj = null;
            String str_issueTimeSpam = null;
            String str_id = null;
            JSONObject familyphotobase64_obj = null;
            JSONObject familydetails_obj = null;
            JSONObject policydetails_obj = null;
            String str_base64string = null;
            String str_doorhouse = null;
            String str_panchyattowncode = null;
            String str_hofnamereg = null;
            String str_villagecode = null;
            String str_statecode = null;
            String str_districtcode = null;
            String str_menutiadetail = null;
            String str_blockcode = null;
            String str_enddate = null;
            String str_insccode = null;
            String str_policyno = null;
            String str_policyamt = null;
            String str_startdate = null;
            JSONObject allfamilymember_obj = null;
            JSONArray familymember = null;
            JSONObject Carddetails_obj = null;
            String str_CSN = null;
            String str_CardCategory = null;
            String str_Cardtype = null;
            rsbyHouseholdItem = new HouseHoldItem();
            try {


                RsbySmartCardDetails_obj = json.getJSONObject(TAG_RSBYSMARTCARDDETAILS);
            } catch (JSONException e) {
                //   Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
            try {
                urn_obj = RsbySmartCardDetails_obj.getJSONObject(TAG_URN);
            } catch (JSONException e) {

                Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
            try {
                str_issueTimeSpam = urn_obj.getString(TAG_ISSUETIMESPAM);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                str_id = urn_obj.getString(TAG_ID);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            URNNO = str_id;

            rsbyHouseholdItem.setRsbyIssuesTimespam(str_issueTimeSpam);
            rsbyHouseholdItem.setRsbyUrnId(URNNO);
            try {
                familyphotobase64_obj = RsbySmartCardDetails_obj.getJSONObject(TAG_FAMILYPHOTOBASE64);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                str_base64string = familyphotobase64_obj.getString(TAG_BASE64STRING);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            rsbyHouseholdItem.setRsbyFamilyPhoto(str_base64string);
            try {
                familydetails_obj = RsbySmartCardDetails_obj.getJSONObject(TAG_FAMILYDETAILS);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                str_doorhouse = familydetails_obj.getString(TAG_DOORHOUSE);
                rsbyHouseholdItem.setRsbyDoorhouse(str_doorhouse
                );
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                str_panchyattowncode = familydetails_obj.getString(TAG_PANCHYATTOWNCODE);
                rsbyHouseholdItem.setRsbyPanchyatTownCode(str_panchyattowncode);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                str_hofnamereg = familydetails_obj.getString(TAG_HOFNAMEREG);
                rsbyHouseholdItem.setRsbyHofnamereg(str_hofnamereg);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                str_villagecode = familydetails_obj.getString(TAG_VILLAGECODE);
                rsbyHouseholdItem.setRsbyVillageCode(str_villagecode);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                str_statecode = familydetails_obj.getString(TAG_STATECODE);
                stateCode = str_statecode;
                rsbyHouseholdItem.setRsbyStateCode(str_statecode);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                str_districtcode = familydetails_obj.getString(TAG_DISTRICTCODE);
                rsbyHouseholdItem.setRsbyDistrictCode(str_districtcode);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                str_blockcode = familydetails_obj.getString(TAG_BLOCKCODE);
                rsbyHouseholdItem.setRsbyBlockCode(str_blockcode);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                policydetails_obj = RsbySmartCardDetails_obj.getJSONObject(TAG_POLICYDETAILS);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (policydetails_obj != null) {
                try {

                    str_enddate = policydetails_obj.getString(TAG_ENDDATE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    rsbyHouseholdItem.setRsbyEnddate(str_enddate);
                    str_insccode = policydetails_obj.getString(TAG_INSCCODE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    rsbyHouseholdItem.setRsbyInsccode(str_insccode);
                    str_policyno = policydetails_obj.getString(TAG_POLICYNO);
                    policyNo = str_policyno;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    rsbyHouseholdItem.setRsbyPolicyno(str_policyno);
                    str_policyamt = policydetails_obj.getString(TAG_POLICYAMT);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    rsbyHouseholdItem.setRsbyPolicyamt(str_policyamt);
                    str_startdate = policydetails_obj.getString(TAG_STARTDATE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    rsbyHouseholdItem.setRsbyStartdate(str_startdate);
                    allfamilymember_obj = RsbySmartCardDetails_obj.getJSONObject(TAG_ALLFAMILYMEMBER);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            try {
                familymember = allfamilymember_obj.getJSONArray(TAG_FAMILYMEMBER);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            for (int familymember_i = 0; familymember_i < familymember.length(); familymember_i++) {
                JSONObject familymember_obj = null;
                try {
                    familymember_obj = familymember.getJSONObject(familymember_i);
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }

                String str_gender = null;
                try {
                    str_gender = familymember_obj.getString(TAG_GENDER);
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                String str_dob = null;
                try {
                    str_dob = familymember_obj.getString(TAG_DOB);
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                String str_relcode = null;
                try {
                    str_relcode = familymember_obj.getString(TAG_RELCODE);
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                String str_name = null;
                try {
                    str_name = familymember_obj.getString(TAG_NAME);
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                try {
                    str_menutiadetail = familymember_obj.getString(TAG_FINGERPRINT);
                } catch (Exception ex) {

                }
                String str_memid = null;
                try {
                    str_memid = familymember_obj.getString(TAG_MEMID);
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }

                try {
                    Carddetails_obj = RsbySmartCardDetails_obj.getJSONObject(TAG_CARDDETAILS);

                } catch (Exception ex) {

                }
                try {
                    str_Cardtype = Carddetails_obj.getString(TAG_CARDTYPE);
                } catch (Exception ex) {

                }
                try {
                    str_CardCategory = Carddetails_obj.getString(TAG_CARDCATEGORY);
                } catch (Exception ex) {

                }
                try {
                    str_CSN = Carddetails_obj.getString(TAG_CSN);
                } catch (Exception ex) {

                }

/*
                RSBYItem rsbyItem = new RSBYItem();

                rsbyItem.setGender(str_gender);
                rsbyItem.setDob(str_dob);
                rsbyItem.setUrnId(URNNO);
                rsbyItem.setCsmNo(str_CSN);
                //     rsbyItem.setMenutiaDetail(str_menutiadetail);
                rsbyItem.setCardType(str_Cardtype);
                rsbyItem.setCardCategory(str_CardCategory);
                rsbyItem.setUrnNo(URNNO);
                rsbyItem.setName(str_name);
                rsbyItem.setMemid(str_memid);
                rsbyItem.setRelcode(str_relcode);
                rsbyItem.setRsbyMemId(URNNO + str_memid);
                rsbyHouseholdItem.setEnddate(str_enddate);
                rsbyItem.setInsccode(str_insccode);
                rsbyItem.setPolicyno(str_policyno);
                rsbyItem.setPolicyamt(str_policyamt);
                rsbyItem.setStartdate(str_startdate);
                rsbyItem.setEnddate(str_enddate);*/

                SeccMemberItem rsbyItem = new SeccMemberItem();
                rsbyItem.setRsbyInsccode(str_insccode);
                rsbyItem.setRsbyPolicyno(str_policyno);
                rsbyItem.setRsbyPolicyamt(str_policyamt);
                rsbyItem.setRsbyStartdate(str_startdate);
                rsbyItem.setRsbyEnddate(str_enddate);
                rsbyItem.setDataSource(AppConstant.RSBY_SOURCE);
                rsbyItem.setRsbyStateCode(str_statecode);
                rsbyItem.setRsbyDistrictCode(str_districtcode);
                rsbyItem.setRsbyRelcode(str_relcode);
                rsbyItem.setRsbyBlockCode(str_blockcode);
                rsbyItem.setRsbyDoorhouse(str_doorhouse);
                rsbyItem.setRsbyHofnamereg(str_hofnamereg);
                rsbyItem.setRsbyPanchyatTownCode(str_panchyattowncode);
                rsbyItem.setRsbyVillageCode(str_villagecode);
                rsbyItem.setRsbyIssuesTimespam(str_issueTimeSpam);
                rsbyItem.setRsbyMemId(URNNO + str_memid);
                rsbyItem.setRsbyGender(str_gender);
                rsbyItem.setRsbyDob(str_dob);
                rsbyItem.setRsbyUrnId(URNNO);
                rsbyItem.setRsbyName(str_name);
                rsbyItem.setRsbyCsmNo(str_CSN);
                rsbyItem.setRsbyCardType(str_Cardtype);
                rsbyItem.setRsbyCardCategory(str_CardCategory);
                rsbyItem.setRsbyMemid(str_memid);
                rsbyItem.setRsbyCardMenutiaDetail(str_menutiadetail);
                rsbyItem.setDataSource(AppConstant.RSBY_SOURCE);


                if (str_relcode.equalsIgnoreCase("01")) {
                    //  rsbyItem.setMemberPhoto1(str_base64string);
                   /* rsbyHouseholdItem.setRsbyMemId(URNNO + str_memid);
                    rsbyHouseholdItem.setGender(str_gender);
                    rsbyHouseholdItem.setDob(str_dob);
                    rsbyHouseholdItem.setUrnId(URNNO);
                    rsbyHouseholdItem.setName(str_name);
                    rsbyHouseholdItem.setCsmNo(str_CSN);
                    rsbyHouseholdItem.setCardType(str_Cardtype);
                    rsbyHouseholdItem.setCardCategory(str_CardCategory);
                    rsbyHouseholdItem.setMemid(str_memid);
                    rsbyHouseholdItem.setRelcode(str_relcode);*/

                    rsbyHouseholdItem.setRsbyStateCode(str_statecode);
                    rsbyHouseholdItem.setRsbyDistrictCode(str_districtcode);
                    rsbyHouseholdItem.setRsbyBlockCode(str_blockcode);
                    rsbyHouseholdItem.setRsbyDoorhouse(str_doorhouse);
                    rsbyHouseholdItem.setRsbyHofnamereg(str_hofnamereg);
                    rsbyHouseholdItem.setRsbyPanchyatTownCode(str_panchyattowncode);
                    rsbyHouseholdItem.setRsbyVillageCode(str_villagecode);
                    rsbyHouseholdItem.setRsbyIssuesTimespam(str_issueTimeSpam);
                    rsbyHouseholdItem.setRsbyMemId(URNNO + str_memid);
                    rsbyHouseholdItem.setRsbyGender(str_gender);
                    rsbyHouseholdItem.setRsbyDob(str_dob);
                    rsbyHouseholdItem.setRsbyUrnId(URNNO);
                    rsbyHouseholdItem.setRsbyName(str_name);
                    rsbyHouseholdItem.setRsbyCsmNo(str_CSN);
                    rsbyHouseholdItem.setRsbyCardType(str_Cardtype);
                    rsbyHouseholdItem.setRsbyCardCategory(str_CardCategory);
                    rsbyHouseholdItem.setRsbyMemid(str_memid);
                    rsbyHouseholdItem.setRsbyRelcode(str_relcode);
                    rsbyHouseholdItem.setDataSource(AppConstant.RSBY_SOURCE);
                    if (downloadedLocation.getBlockCode() != null) {
                        rsbyHouseholdItem.setAhlblockno(downloadedLocation.getBlockCode());
                    }
                   /* if (downloadedLocation.getAadharNo() != null) {
                        householdItem.set(downloadedLocation.getAadharNo());
                    }*/
                    if (downloadedLocation.getWardCode() != null) {
                        rsbyHouseholdItem.setWardid(downloadedLocation.getWardCode());
                    }
                    if (downloadedLocation.getRuralUrban() != null) {
                        rsbyHouseholdItem.setRuralUrban(downloadedLocation.getRuralUrban());
                    }
                    if (downloadedLocation.getTehsilCode() != null) {
                        rsbyHouseholdItem.setTehsilcode(downloadedLocation.getTehsilCode());
                    }
                    if (downloadedLocation.getSubBlockcode() != null) {
                        rsbyHouseholdItem.setAhlSubBlockNo(downloadedLocation.getSubBlockcode());
                    }
                    if (downloadedLocation.getStateCode() != null) {
                        rsbyHouseholdItem.setStatecode(downloadedLocation.getStateCode());
                    }
                    if (downloadedLocation.getVtCode() != null) {
                        rsbyHouseholdItem.setTowncode(downloadedLocation.getVtCode());
                    }
                    if (downloadedLocation.getDistrictCode() != null) {
                        rsbyHouseholdItem.setDistrictcode(downloadedLocation.getDistrictCode());
                    }

                }
                rsbyItemList.add(rsbyItem);
            }
        } catch (Exception e) {
            return false;
        }

        return true;

    }

    private Bitmap convertStringToBitmap(String strBase64) {
        byte[] decodedString = Base64.decode(strBase64, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        //image.setImageBitmap(decodedByte);
        return decodedByte;
    }

    private void sendResult(String XML) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(AppConstant.RSBYCARDPREVIEWRESPONSE, XML);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }


    private int parseDataForDatabase(String data) {
        String URNNO = null;
        ArrayList<SeccMemberItem> rsbyArrayList = new ArrayList<SeccMemberItem>();
        boolean tags = false;
        JSONObject json = null;
        try {
            json = new JSONObject(data);
            String reqJson = data.toString();
            System.out.print(reqJson);
        } catch (JSONException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        JSONObject RsbySmartCardDetails_obj = null;
        JSONObject urn_obj = null;
        String str_issueTimeSpam = null;
        String str_id = null;
        JSONObject familyphotobase64_obj = null;
        JSONObject familydetails_obj = null;
        JSONObject policydetails_obj = null;
        String str_base64string = null;
        String str_doorhouse = null;
        String str_panchyattowncode = null;
        String str_hofnamereg = null;
        String str_villagecode = null;
        String str_statecode = null;
        String str_districtcode = null;
        String str_blockcode = null;
        String str_enddate = null;
        String str_insccode = null;
        String str_policyno = null;
        String str_policyamt = null;
        String str_startdate = null;
        JSONObject allfamilymember_obj = null;
        JSONArray familymember = null;
        JSONObject Carddetails_obj = null;
        String str_CSN = null;
        String str_CardCategory = null;
        String str_Cardtype = null;
        try {


            RsbySmartCardDetails_obj = json.getJSONObject(TAG_RSBYSMARTCARDDETAILS);
        } catch (JSONException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        try {
            urn_obj = RsbySmartCardDetails_obj.getJSONObject(TAG_URN);
        } catch (JSONException e) {

            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        try {
           str_issueTimeSpam = urn_obj.getString(TAG_ISSUETIMESPAM);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            str_id = urn_obj.getString(TAG_ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        URNNO = str_id;
        HouseHoldItem householdItem = new HouseHoldItem();
        householdItem.setRsbyIssuesTimespam(str_issueTimeSpam);
        householdItem.setRsbyUrnId(URNNO);
        householdItem.setDataSource(AppConstant.RSBY_SOURCE);
        try {
            familyphotobase64_obj = RsbySmartCardDetails_obj.getJSONObject(TAG_FAMILYPHOTOBASE64);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            str_base64string = familyphotobase64_obj.getString(TAG_BASE64STRING);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        householdItem.setRsbyFamilyPhoto(str_base64string);
        try {
            familydetails_obj = RsbySmartCardDetails_obj.getJSONObject(TAG_FAMILYDETAILS);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            str_doorhouse = familydetails_obj.getString(TAG_DOORHOUSE);
            householdItem.setRsbyDoorhouse(str_doorhouse);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            str_panchyattowncode = familydetails_obj.getString(TAG_PANCHYATTOWNCODE);
            householdItem.setRsbyPanchyatTownCode(str_panchyattowncode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            str_hofnamereg = familydetails_obj.getString(TAG_HOFNAMEREG);
            householdItem.setRsbyHofnamereg(str_hofnamereg);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            str_villagecode = familydetails_obj.getString(TAG_VILLAGECODE);
            householdItem.setRsbyVillageCode(str_villagecode);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            str_statecode = familydetails_obj.getString(TAG_STATECODE);

            householdItem.setRsbyStateCode(str_statecode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            str_districtcode = familydetails_obj.getString(TAG_DISTRICTCODE);
            householdItem.setRsbyDistrictCode(str_districtcode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            str_blockcode = familydetails_obj.getString(TAG_BLOCKCODE);
            householdItem.setRsbyBlockCode(str_blockcode);
        } catch (JSONException e) {
            e.printStackTrace();

        }
        try {
            policydetails_obj = RsbySmartCardDetails_obj.getJSONObject(TAG_POLICYDETAILS);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        try {
            householdItem.setRsbyEnddate(str_enddate);
            str_insccode = policydetails_obj.getString(TAG_INSCCODE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            householdItem.setRsbyInsccode(str_insccode);
            str_policyno = policydetails_obj.getString(TAG_POLICYNO);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            householdItem.setRsbyPolicyno(str_policyno);
            str_policyamt = policydetails_obj.getString(TAG_POLICYAMT);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            householdItem.setRsbyPolicyamt(str_policyamt);
            str_startdate = AppUtility.convertRsbyDateFormat(policydetails_obj.getString(TAG_STARTDATE));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            householdItem.setRsbyStartdate(str_startdate);
            allfamilymember_obj = RsbySmartCardDetails_obj.getJSONObject(TAG_ALLFAMILYMEMBER);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            familymember = allfamilymember_obj.getJSONArray(TAG_FAMILYMEMBER);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        try {
            str_enddate = AppUtility.convertRsbyDateFormat(policydetails_obj.getString(TAG_ENDDATE));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        for (int familymember_i = 0; familymember_i < familymember.length(); familymember_i++) {
            JSONObject familymember_obj = null;
            try {
                familymember_obj = familymember.getJSONObject(familymember_i);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            String str_gender = null;

            try {
                str_gender = familymember_obj.getString(TAG_GENDER);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }

            String str_menutiadetail = null;
            try {
                str_menutiadetail = familymember_obj.getString(TAG_FINGERPRINT);
            } catch (Exception ex) {

            }

            String str_dob = null;
            try {
                str_dob = AppUtility.convertRsbyDateFormat(familymember_obj.getString(TAG_DOB));
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            String str_relcode = null;
            try {
                str_relcode = familymember_obj.getString(TAG_RELCODE);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            String str_name = null;
            try {
                str_name = familymember_obj.getString(TAG_NAME);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            String str_memid = null;
            try {
                str_memid = familymember_obj.getString(TAG_MEMID);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }

            try {
                Carddetails_obj = RsbySmartCardDetails_obj.getJSONObject(TAG_CARDDETAILS);

            } catch (Exception ex) {

            }
            try {
                str_Cardtype = Carddetails_obj.getString(TAG_CARDTYPE);
            } catch (Exception ex) {

            }
            try {
                str_CardCategory = Carddetails_obj.getString(TAG_CARDCATEGORY);

            } catch (Exception ex) {

            }
            try {
                str_CSN = Carddetails_obj.getString(TAG_CSN);
            } catch (Exception ex) {

            }


            SeccMemberItem rsbyItem = new SeccMemberItem();
            rsbyItem.setRsbyStateCode(str_statecode);
            rsbyItem.setRsbyDistrictCode(str_districtcode);
            rsbyItem.setRsbyBlockCode(str_blockcode);
            rsbyItem.setRsbyCardMenutiaDetail(str_menutiadetail);
            rsbyItem.setRsbyDoorhouse(str_doorhouse);
            rsbyItem.setRsbyHofnamereg(str_hofnamereg);
            rsbyItem.setRsbyPanchyatTownCode(str_panchyattowncode);
            rsbyItem.setRsbyVillageCode(str_villagecode);
            rsbyItem.setRsbyIssuesTimespam(str_issueTimeSpam);
            rsbyItem.setRsbyGender(str_gender);
            rsbyItem.setRsbyDob(str_dob);
            rsbyItem.setRsbyUrnId(URNNO);
            rsbyItem.setUrnId(null);
            rsbyItem.setRsbyCsmNo(str_CSN);
            rsbyItem.setRsbyCardType(str_Cardtype);
            rsbyItem.setRsbyCardCategory(str_CardCategory);
            //rsbyItem.setRsbyUrnId(URNNO);
            rsbyItem.setRsbyName(str_name);
            rsbyItem.setName(str_name);
            rsbyItem.setRsbyMemid(str_memid);
            rsbyItem.setRsbyRelcode(str_relcode);
            rsbyItem.setRsbyMemId(URNNO + str_memid);
            householdItem.setRsbyEnddate(str_enddate);
            rsbyItem.setRsbyInsccode(str_insccode);
            rsbyItem.setRsbyPolicyno(str_policyno);
            rsbyItem.setRsbyPolicyamt(str_policyamt);
            rsbyItem.setRsbyStartdate(str_startdate);
            rsbyItem.setRsbyEnddate(str_enddate);
            if (str_relcode.equalsIgnoreCase("01")) {
                //  rsbyItem.setMemberPhoto1(str_base64string);
                householdItem.setRsbyName(str_name);
                householdItem.setRsbyStateCode(str_statecode);
                householdItem.setRsbyDistrictCode(str_districtcode);
                householdItem.setRsbyBlockCode(str_blockcode);
                householdItem.setRsbyDoorhouse(str_doorhouse);
                householdItem.setRsbyHofnamereg(str_hofnamereg);
                householdItem.setRsbyPanchyatTownCode(str_panchyattowncode);
                householdItem.setRsbyVillageCode(str_villagecode);
                householdItem.setRsbyIssuesTimespam(str_issueTimeSpam);
                householdItem.setRsbyMemId(URNNO + str_memid);
                householdItem.setRsbyGender(str_gender);
                householdItem.setRsbyDob(str_dob);
                householdItem.setRsbyUrnId(URNNO);
                householdItem.setUrnId(null);
                householdItem.setName(str_name);
                householdItem.setRsbyCsmNo(str_CSN);
                householdItem.setRsbyCardType(str_Cardtype);
                householdItem.setRsbyCardCategory(str_CardCategory);
                householdItem.setRsbyMemid(str_memid);
                householdItem.setRsbyRelcode(str_relcode);
                householdItem.setDataSource(AppConstant.RSBY_SOURCE);

                /*if (downloadedLocation.getBlockCode() != null) {
                    householdItem.setVlBlockCode(downloadedLocation.getBlockCode());
                }
                if (downloadedLocation.getAadharNo() != null) {
                    householdItem.setVlAadharNo(downloadedLocation.getAadharNo());
                }
                if (downloadedLocation.getWardCode() != null) {
                    householdItem.setVlWardCode(downloadedLocation.getWardCode());
                }
                if (downloadedLocation.getRuralUrban() != null) {
                    householdItem.setVlRuralUrban(downloadedLocation.getRuralUrban());
                }
                if (downloadedLocation.getTehsilCode() != null) {
                    householdItem.setVlTehsilCode(downloadedLocation.getTehsilCode());
                }
                if (downloadedLocation.getSubBlockcode() != null) {
                    householdItem.setVlSubBlockcode(downloadedLocation.getSubBlockcode());
                }
                if (downloadedLocation.getStateCode() != null) {
                    householdItem.setVlStateCode(downloadedLocation.getStateCode());
                }
                if (downloadedLocation.getVtCode() != null) {
                    householdItem.setVlVtCode(downloadedLocation.getVtCode());
                }
                if (downloadedLocation.getDistrictCode() != null) {
                    householdItem.setVlDistrictCode(downloadedLocation.getDistrictCode());
                }*/

                if (downloadedLocation.getBlockCode() != null) {
                    householdItem.setAhlblockno(downloadedLocation.getBlockCode());
                }
                   /* if (downloadedLocation.getAadharNo() != null) {
                        householdItem.set(downloadedLocation.getAadharNo());
                    }*/
                if (downloadedLocation.getWardCode() != null) {
                    householdItem.setWardid(downloadedLocation.getWardCode());
                }
                if (downloadedLocation.getRuralUrban() != null) {
                    householdItem.setRuralUrban(downloadedLocation.getRuralUrban());
                }
                if (downloadedLocation.getTehsilCode() != null) {
                    householdItem.setTehsilcode(downloadedLocation.getTehsilCode());
                }
                if (downloadedLocation.getSubBlockcode() != null) {
                    householdItem.setAhlSubBlockNo(downloadedLocation.getSubBlockcode());
                }
                if (downloadedLocation.getStateCode() != null) {
                    householdItem.setStatecode(downloadedLocation.getStateCode());
                }
                if (downloadedLocation.getVtCode() != null) {
                    householdItem.setTowncode(downloadedLocation.getVtCode());
                }
                if (downloadedLocation.getDistrictCode() != null) {
                    householdItem.setDistrictcode(downloadedLocation.getDistrictCode());
                }
            }

            if (downloadedLocation.getBlockCode() != null) {
                rsbyItem.setAhlblockno(downloadedLocation.getBlockCode());
            }
              /*  if (downloadedLocation.getAadharNo() != null) {
                    item.setVl_aadharNo(downloadedLocation.getAadharNo());
                }*/
            if (downloadedLocation.getWardCode() != null) {
                rsbyItem.setWardid(downloadedLocation.getWardCode());
            }
            if (downloadedLocation.getRuralUrban() != null) {
                rsbyItem.setRuralUrban(downloadedLocation.getRuralUrban());
            }
            if (downloadedLocation.getTehsilCode() != null) {
                rsbyItem.setTehsilcode(downloadedLocation.getTehsilCode());
            }
            if (downloadedLocation.getSubBlockcode() != null) {
                rsbyItem.setAhlsubblockno(downloadedLocation.getSubBlockcode());
            }
            if (downloadedLocation.getStateCode() != null) {
                rsbyItem.setStatecode(downloadedLocation.getStateCode());
            }
            if (downloadedLocation.getVtCode() != null) {
                rsbyItem.setTowncode(downloadedLocation.getVtCode());
            }
            if (downloadedLocation.getDistrictCode() != null) {
                rsbyItem.setDistrictcode(downloadedLocation.getDistrictCode());
            }
            rsbyItem.setDataSource(AppConstant.RSBY_SOURCE);
            rsbyArrayList.add(rsbyItem);
        }
        if (checksEnable) {
           /* if (!downloadedLocation.getStateCode().equalsIgnoreCase(str_statecode)) {
                return INVALID_STATECODE;
            }
            if (!downloadedLocation.getDistrictCode().equalsIgnoreCase(str_districtcode)) {
                return INVALID_DISTRICTCODE;
            }*/
            if (!AppUtility.isValidCard(context, str_CardCategory)) {
                return INVALID_CARD_CATEGORY;
            }
            if (!AppUtility.compareCardExpDate(AppUtility.currentDateLong(), AppUtility.convertRsbyDateToLong(str_enddate, AppConstant.RSBY_DATE_FORMAT))) {
                if (!checkPolicy(str_policyno, str_statecode, str_districtcode)) {
                    //    return CARD_EXPIRE;
                }
            }
        }
        String str1 = householdItem.serialize();
        System.out.print(str1);
        HouseHoldItem holdItem = SeccDatabase.getRsbyHouseHoldByUrn(URNNO, context);
        for (SeccMemberItem item : rsbyArrayList) {
            String str = item.serialize();
            System.out.print(str);
            if (item.getRsbyCardMenutiaDetail() != null && !item.getRsbyCardMenutiaDetail().equalsIgnoreCase(AppConstant.MENUTIA_DETAIL_AVAILABLE)) {
                menitiuiaCount++;
            }

        }
        if (holdItem != null) {
            String str = holdItem.serialize();
            System.out.print(str);
            //  AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Household Name : "+holdItem.getRsbyName()+"  "+holdItem.getDataSource()+"nnn : ");
        }
        if (holdItem == null) {
            String houseHoldJson = householdItem.serialize();
            System.out.print(houseHoldJson);
            SeccDatabase.saveHousehold(context, householdItem);
            if (SeccDatabase.getRsbyMemberListWithUrn(URNNO, context).size() == 0) {
                for (SeccMemberItem item : rsbyArrayList) {
                    String str = item.serialize();
                    System.out.print(str);
                  /*  if (item.getRsbyCardMenutiaDetail() != null && !item.getRsbyCardMenutiaDetail().equalsIgnoreCase(AppConstant.MENUTIA_DETAIL_AVAILABLE)) {
                        menitiuiaCount++;
                    }*/
                    //SeccDatabase.save(item, context);
                    if (item.getRsbyCardMenutiaDetail() != null && item.getRsbyCardMenutiaDetail().equalsIgnoreCase(AppConstant.MENUTIA_DETAIL_AVAILABLE)) {
                      String memberJson = item.serialize();
                        System.out.print(memberJson);
                        SeccDatabase.save(item, context);
                    }
                }
            }
        } else {
            return DUPLICATE_CARD;
        }
       /* if (SeccDatabase.getRsbyHouseHoldQ(URNNO, context) == null) {
            SeccDatabase.rsbySaveHousehold(householdItem, context);
        } else {
            return DUPLICATE_CARD;
        }
        if (SeccDatabase.getRsbyMemberList(URNNO, context).size() == 0) {
            for (RSBYItem item : rsbyArrayList) {

                if (item.getMenutiaDetail() != null && item.getMenutiaDetail().equalsIgnoreCase(AppConstant.MENUTIA_DETAIL_AVAILABLE)) {
                    SeccDatabase.saveRSBYMember(item, context);
                }
            }

        } else {
            return DUPLICATE_CARD;
        }*/
        if (selectedMemItem == null) {
            selectedMemItem = new SelectedMemberItem();
            selectedMemItem.setHouseHoldItem(householdItem);
            ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SELECTED_ITEM_FOR_VERIFICATION, selectedMemItem.serialize(), context);
        }
        return CARD_SAVED_SUCESSFULLY;
    }

    private boolean checkPolicy(String policy, String stateCode, String districtCode) {
        RSBYPoliciesItem item = SeccDatabase.getRsbyPolicyItem(context, policy, stateCode, districtCode);
        if (item != null) {
            if (AppUtility.compareCardExpDate(AppUtility.currentDateLong(), AppUtility.convertRsbyDateToLong(item.getExtensionEndDate(), AppConstant.RSBY_POLICIES_DATA_DATE_FORMAT))) {
                isRenewed = true;
                renewedDate = item.getExtensionEndDate();
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public void alertWithOkReturn(Context mContext, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Alert");
        builder.setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                        finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void alertForErrors(String msg) {
        final AlertDialog internetDiaolg = new AlertDialog.Builder(context).create();
        LayoutInflater factory = LayoutInflater.from(context);
        View alertView = factory.inflate(R.layout.rsby_cardpreview_error, null);
        internetDiaolg.setView(alertView);
        internetDiaolg.show();
        TextView tryGainMsgTV = (TextView) alertView.findViewById(R.id.errorMsg);
        tryGainMsgTV.setText(msg);

        Button cancelBT = (Button) alertView.findViewById(R.id.cancelBT);
        cancelBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                internetDiaolg.dismiss();
                ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.DASHBOARD_TAB_STATUS, SearchActivityWithHouseHold.RSBYDASHBOARD, context);
                ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.HOUSEHOLD_TAB_STATUS, 2 + "", context);
                Intent returnIntent = new Intent(context, SearchActivityWithHouseHold.class);
                startActivity(returnIntent);
                finish();
            }
        });
    }


    private String getCompanyName(String companyCode) {
        String companyName = companyCode;
        ArrayList<RsbyPoliciesCompany> itemList = SeccDatabase.getAllPoliciesCompany(context);
        if (itemList != null && itemList.size() > 0) {
            for (RsbyPoliciesCompany item : itemList) {
                if (item.getCompanyCode().equalsIgnoreCase(companyCode)) {
                    companyName = item.getCompanyName();
                    return companyName;
                }

            }
        } else {
            return companyName;
        }

        return companyName;
    }
}
