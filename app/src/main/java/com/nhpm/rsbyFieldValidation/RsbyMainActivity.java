package com.nhpm.rsbyFieldValidation;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.customComponent.CustomAlert;
import com.customComponent.utility.ProjectPrefrence;
import com.nhpm.LocalDataBase.dto.SeccDatabase;
import com.nhpm.Models.response.master.MemberStatusItem;
import com.nhpm.Models.response.rsbyMembers.RSBYItem;
import com.nhpm.Models.response.rsbyMembers.RsbyHouseholdItem;
import com.nhpm.Models.response.seccMembers.SeccMemberItem;
import com.nhpm.Models.response.seccMembers.SelectedMemberItem;
import com.nhpm.Models.response.verifier.FamilyStatusItem;
import com.nhpm.Models.response.verifier.VerifierLoginResponse;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;
import com.nhpm.Utility.AppUtility;
import com.nhpm.activity.BaseActivity;
import com.nhpm.activity.SearchActivityWithHouseHold;
import com.nhpm.rsbyFieldValidation.fragment.DefaultRsbyListFragment;
import com.nhpm.rsbyFieldValidation.fragment.RsbyFamilyListFragment;
import com.nhpm.rsbyFieldValidation.fragment.RsbyNewHeadFragment;
import com.nhpm.rsbyFieldValidation.fragment.RsbyOldheadFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import java.util.ArrayList;

import pl.polidea.view.ZoomView;

/**
 * Created by DELL on 27-02-2017.
 */

public class RsbyMainActivity extends BaseActivity {
    public static final String DUMMYSAMPLE="<?xml version='1.0' standalone='yes' ?>\n" +
            "<RsbySmartCardDetails>\n" +
            "  <urn id=\"09520100112000116\">\n" +
            "  <issueTimeSpam>19122016112331</issueTimeSpam>\n" +
            "  </urn>\n" +
            "  <familydetails hofnamereg=\"BHIKHU\">\n" +
            "  <doorhouse>-</doorhouse>\n" +
            "  <villagecode>06617830</villagecode>\n" +
            "  <panchyattowncode>0952003001</panchyattowncode>\n" +
            "  <blockcode>0952003</blockcode>\n" +
            "  <districtcode>52</districtcode>\n" +
            "  <statecode>09</statecode>\n" +
            "  </familydetails>\n" +
            "  <allfamilymember>\n" +
            "  <familymember>\n" +
            "  <memid>01</memid>\n" +
            "  <name>BHIKHU</name>\n" +
            "  <dob>01121956</dob>\n" +
            "  <gender>1</gender>\n" +
            "  <relcode>01</relcode>\n" +
            "  </familymember>\n" +
            "  <familymember>\n" +
            "  <memid>02</memid>\n" +
            "  <name>ANIVAR</name>\n" +
            "  <dob>01121951</dob>\n" +
            "  <gender>2</gender>\n" +
            "  <relcode>02</relcode>\n" +
            "  </familymember>\n" +
            "  <familymember>\n" +
            "  <memid>03</memid>\n" +
            "  <name>SVRUN NISHA</name>\n" +
            "  <dob>01121988</dob>\n" +
            "  <gender>2</gender>\n" +
            "  <relcode>17</relcode>\n" +
            "  </familymember>\n" +
            "  <familymember>\n" +
            "  <memid>04</memid>\n" +
            "  <name>BAKRIDI</name>\n" +
            "  <dob>01121986</dob>\n" +
            "  <gender>1</gender>\n" +
            "  <relcode>05</relcode>\n" +
            "  </familymember>\n" +
            "  <familymember>\n" +
            "  <memid>05</memid>\n" +
            "  <name>BAOORA</name>\n" +
            "  <dob>01121958</dob>\n" +
            "  <gender>2</gender>\n" +
            "  <relcode>17</relcode>\n" +
            "  </familymember>\n" +
            "  </allfamilymember>\n" +
            "  <policydetails>\n" +
            "  <insccode>05</insccode>\n" +
            "  <policyno>0000000505959652650956460</policyno>\n" +
            "  <policyamt>0003004320</policyamt>\n" +
            "  <startdate>01022017</startdate>\n" +
            "  <enddate>02022019</enddate>\n" +
            "  </policydetails>\n" +
            "  <familyphotobase64>\n" +
            "  <base64string> \n" +
            "\n" +
            "</base64string>\n" +
            "</familyphotobase64>\n" +
            "</RsbySmartCardDetails>\n" +
            "\n";
    public static final String TAG_RSBYSMARTCARDDETAILS="RsbySmartCardDetails";
    public static final String TAG_URN="urn";
    public static final String TAG_ISSUETIMESPAM="issueTimeSpam";
    public static final String TAG_ID="id";
    public static final String TAG_FAMILYPHOTOBASE64="familyphotobase64";
    public static final String TAG_BASE64STRING="base64string";
    public static final String TAG_FAMILYDETAILS="familydetails";
    public static final String TAG_DOORHOUSE="doorhouse";
    public static final String TAG_PANCHYATTOWNCODE="panchyattowncode";
    public static final String TAG_HOFNAMEREG="hofnamereg";
    public static final String TAG_VILLAGECODE="villagecode";
    public static final String TAG_STATECODE="statecode";
    public static final String TAG_DISTRICTCODE="districtcode";
    public static final String TAG_BLOCKCODE="blockcode";
    public static final String TAG_ALLFAMILYMEMBER="allfamilymember";
    public static final String TAG_FAMILYMEMBER="familymember";
    public static final String TAG_POLICYDETAILS="policydetails";
    public static final String TAG_ENDDATE="enddate";
    public static final String TAG_INSCCODE="insccode";
    public static final String TAG_POLICYNO="policyno";
    public static final String TAG_POLICYAMT="policyamt";
    public static final String TAG_STARTDATE="startdate";
    public static final String TAG_GENDER="gender";
    public static final String TAG_DOB="dob";
    public static final String TAG_RELCODE="relcode";
    public static final String TAG_NAME="name";
    public static final String TAG_MEMID="memid";
    private String UrnId;
    private Context context;
    private RSBYItem rsbyItem;
    private RsbyHouseholdItem rsbyHouseHoldItem;
    private SelectedMemberItem selectedMemItem;
    private RecyclerView recycleView;
    private ArrayList<RSBYItem> rsbyItemList;
    private RelativeLayout backLayout;
    private ImageView backImageView;
    private Intent intent;
    private static String urnid ="09520100112000116";
    private String xmlResponse;
    private FragmentManager fargManager;
    private FragmentTransaction fragTransect;
    private RsbyFamilyListFragment rsbyFamilyListFragment;
    private DefaultRsbyListFragment defaultFragment;
    private RsbyOldheadFragment oldHeadFragment;
    private RsbyNewHeadFragment newHeadFragment;
    private ArrayList<RSBYItem> houseHoldMemberList;
    private RSBYItem head,newHead;
    public RSBYItem oldHeadItem,newHeadItem;
    public Spinner familyStatusSP,hofStatusSP;
    private LinearLayout headMemberStatusLayout;
    public Button chooseHeadBT;
    public RelativeLayout chooseHeadLayout;
    private LinearLayout mZoomLinearLayout;
    private ZoomView zoomView;
    public  static int CHOOSE_NEW_HEAD_STATUS=1;
    public FamilyStatusItem householdStatus;
    private ArrayList<FamilyStatusItem> familyStatusList;
    private ArrayList<MemberStatusItem> memberStatusList;
    public MemberStatusItem hofStatusItem;
    private TextView centertext;

    private int wrongPinCount = 0;
    private long wrongPinSavedTime;
    private long currentTime;
    private TextView wrongAttempetCountValue,wrongAttempetCountText;
    private long millisecond24 = 86400000;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dummy_layout_for_zooming);
        // setContentView(R.layout.rsby_main_layout);

        intent = getIntent();
        context = this;
        //  fragMgr=getSupportFragmentManager();
        setupScreen();
        //  searchMenu();
        //openRSBYFamilyListFragment("");
    }

    private void setupScreen(){
        mZoomLinearLayout = (LinearLayout) findViewById(R.id.mZoomLinearLayout);
        View v = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.rsby_main_layout, null, false);
        v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));

        backLayout = (RelativeLayout) v.findViewById(R.id.backLayout);
        centertext = (TextView) v.findViewById(R.id.centertext);
        backLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               backImageView.performClick();
            }
        });
        backImageView = (ImageView) v.findViewById(R.id.back);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent theIntent = new Intent(context, SearchActivityWithHouseHold.class);
                startActivity(theIntent);
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        /*backLayout = (RelativeLayout) findViewById(R.id.backLayout);
        backLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });*/
      /*  backImageView = (ImageView) findViewById(R.id.back);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });*/



        /*String UrnId = parser(xmlToJson.toString());*/

        selectedMemItem = SelectedMemberItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.SELECTED_ITEM_FOR_VERIFICATION, context));
        if(selectedMemItem!=null && selectedMemItem.getRsbyHouseholdItem()!=null ){
            rsbyHouseHoldItem=selectedMemItem.getRsbyHouseholdItem();
            if(rsbyHouseHoldItem.getUrnId()!=null) {
                centertext.setText(rsbyHouseHoldItem.getUrnId());

            }findHouseholdMemberList();
            findHead();
            fargManager=getSupportFragmentManager();
            familyStatusSP=(Spinner)v.findViewById(R.id.familyStatusSP);
            hofStatusSP=(Spinner)v.findViewById(R.id.headMemberStatusSP);
            headMemberStatusLayout=(LinearLayout)v.findViewById(R.id.headMemberStatusLayout);
            chooseHeadBT=(Button)v.findViewById(R.id.chooseHeadBT);
            chooseHeadLayout=(RelativeLayout)v.findViewById(R.id.chooseHeadLayout);
            zoomView = new ZoomView(this);
            zoomView.addView(v);
            mZoomLinearLayout.addView(zoomView);
            chooseHeadBT.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent theIntent=new Intent(context,RsbyChooseNewActivity.class);
                    startActivityForResult(theIntent, CHOOSE_NEW_HEAD_STATUS);
                    selectedMemItem.setRsbyHouseholdItem(rsbyHouseHoldItem);
                    ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SELECTED_ITEM_FOR_VERIFICATION,selectedMemItem.serialize(),context);
                }
            });
            familyStatusSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    householdStatus=familyStatusList.get(i);
                    headMemberStatusLayout.setVisibility(View.GONE);
                    if(householdStatus.getStatusCode().equalsIgnoreCase(AppConstant.DEFAULT_HOUSEHOLD)) {
                        showDefaultFamilyList();
                    }else if(householdStatus.getStatusCode().equalsIgnoreCase(AppConstant.HOUSEHOLD_FOUND)){
                        headMemberStatusLayout.setVisibility(View.VISIBLE);
                        showDefaultFamilyList();
                    }else{
                        showDefaultFamilyList();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            hofStatusSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    hofStatusItem=memberStatusList.get(i);
                    chooseHeadLayout.setVisibility(View.GONE);
                    if(hofStatusItem.getStatusCode().equalsIgnoreCase(AppConstant.DEFAULT_MEMBER_STATUS)){
                        showDefaultFamilyList();
                    }else if(hofStatusItem.getStatusCode().equalsIgnoreCase(AppConstant.MEMBERFOUND_BUT_NOT_PRESENT)){
                        showOldHeadFamilyList();
                        chooseHeadLayout.setVisibility(View.GONE);
                    }else if(hofStatusItem.getStatusCode().equalsIgnoreCase(AppConstant.MEMBERFOUND_AND_PRESENT)) {
                        chooseHeadLayout.setVisibility(View.GONE);
                        showOldHeadFamilyList();
                    }else{
                        showNewHeadList();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            prepareFamilyStatusSpinner();
            prepareMemberStatusSpinner();
            //setHouseHoldAddress();
        }else{

        }
    }
    private void findHouseholdMemberList(){
        houseHoldMemberList= SeccDatabase.getRsbyMemberList(rsbyHouseHoldItem.getUrnId(),context);

    }
    private void findHead(){

        for(RSBYItem item : houseHoldMemberList){
            if(item.getRsbyMemId()!=null && item.getRsbyMemId().
                    equalsIgnoreCase(rsbyHouseHoldItem.getRsbyMemId())){
                head=item;
                break;
            }
        }
        for(RSBYItem item1 : houseHoldMemberList){
            if(item1.getNhpsRelationCode()!=null && item1.getNhpsRelationCode().trim().equalsIgnoreCase(AppConstant.NEW_HEAD_RELATION_CODE)){
                if(!item1.getRsbyMemId().equalsIgnoreCase(rsbyHouseHoldItem.getRsbyMemId().trim())){
                    newHead = item1;
                    break;
                }
            }
        }


    }
    private ArrayList<RSBYItem> findMemberListWithNewHead(){
        ArrayList<RSBYItem> list=new ArrayList<>();
        SeccMemberItem oldHead=null;
        for(RSBYItem item : houseHoldMemberList){
            if(item.getNhpsRelationCode()!=null && item.getNhpsRelationCode().equalsIgnoreCase(AppConstant.NEW_HEAD_RELATION_CODE)){
                if(item.getRsbyMemId()!=null && !item.getRsbyMemId().equalsIgnoreCase(rsbyHouseHoldItem.getRsbyMemId())){
                    newHeadItem=item;
                    break;
                }
            }
        }

        for(RSBYItem item : houseHoldMemberList ){
            if(item.getRsbyMemId()!=null && item.getRsbyMemId().equalsIgnoreCase(rsbyHouseHoldItem.getRsbyMemId())){
                oldHeadItem=item;
                break;
            }
        }
        if(newHeadItem!=null){
            for(RSBYItem item : houseHoldMemberList){
                if(newHeadItem.getRsbyMemId()!=null && newHeadItem.getRsbyMemId().equalsIgnoreCase(item.getRsbyMemId())){

                }else if(oldHeadItem!=null && oldHeadItem.getRsbyMemId().equalsIgnoreCase(item.getRsbyMemId())){

                }else{
                    list.add(item);
                }
            }
            list.add(0,newHeadItem);
            list.add(list.size(),oldHeadItem);
        }
        return list;
    }
    private ArrayList<RSBYItem> findMemberListWithOldHead(){
        ArrayList<RSBYItem> list=new ArrayList<>();
        RSBYItem newHead=null,oldHead=null;
        for(RSBYItem item : houseHoldMemberList ){
            if(item.getRsbyMemId()!=null && item.getRsbyMemId().equalsIgnoreCase(rsbyHouseHoldItem.getRsbyMemId())){
                oldHead=item;
                break;
            }
        }
        if(oldHead!=null){
            for(RSBYItem item : houseHoldMemberList){
                if(oldHead!=null && oldHead.getRsbyMemId().equalsIgnoreCase(item.getRsbyMemId())){

                }else{
                    list.add(item);
                }
            }
            list.add(0,oldHead);
        }
        return list;
    }
    private ArrayList<RSBYItem> findDefaultMemberList(){
        ArrayList<RSBYItem> list=new ArrayList<>();
        RSBYItem newHead=null,oldHead=null;
        for(RSBYItem item : houseHoldMemberList ){
            if(item.getRsbyMemId()!=null && item.getRsbyMemId().equalsIgnoreCase(rsbyHouseHoldItem.getRsbyMemId())){
                oldHead=item;
                break;
            }
        }
        if(oldHead!=null){
            for(RSBYItem item : houseHoldMemberList){
                if(oldHead!=null && oldHead.getRsbyMemId().equalsIgnoreCase(item.getRsbyMemId())){

                }else{
                    list.add(item);
                }
            }
            list.add(0,oldHead);
        }
        return list;
    }
    private void LoadData(){
        JSONObject soapDatainJsonObject = null;
        try {
            soapDatainJsonObject = XML.toJSONObject(DUMMYSAMPLE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.print(soapDatainJsonObject);
        if(SeccDatabase.getRsbyMemberList(urnid,context)!=null && SeccDatabase.getRsbyMemberList(urnid,context).size()>0) {

            rsbyItemList = SeccDatabase.getRsbyMemberList(urnid,context);
            rsbyHouseHoldItem = SeccDatabase.getRsbyHouseHoldQ(urnid,context);
        }else{
            UrnId = parser(soapDatainJsonObject.toString());
            rsbyItemList = SeccDatabase.getRsbyMemberList(UrnId,context);
            rsbyHouseHoldItem = SeccDatabase.getRsbyHouseHoldQ(UrnId,context);
        }

        System.out.print(rsbyItemList);
        if(rsbyItemList!=null && rsbyItemList.size()>0){
            RsbyHouseholdAdapter adapter = new RsbyHouseholdAdapter(rsbyItemList);
            recycleView.setAdapter(adapter);
        }

    }
    private void loadDataOfCard(String xml){
        JSONObject soapDatainJsonObject = null;
        try {
            soapDatainJsonObject = XML.toJSONObject(xml);
        } catch (JSONException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        System.out.print(soapDatainJsonObject);

        UrnId = parser(soapDatainJsonObject.toString());
        System.out.print(UrnId);
        //   Toast.makeText(context, UrnId, Toast.LENGTH_LONG).show();
         /*   rsbyItemList = SeccDatabase.getRsbyMemberList(UrnId,context);
            rsbyHouseHoldItem = SeccDatabase.getRsbyHouseHoldQ(UrnId,context);

*/
        //  openRSBYFamilyListFragment(UrnId);
    }
    private String parser(String data){
        String URNNO = null;
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
        String str_base64string= null;
        String str_doorhouse= null;
        String str_panchyattowncode= null;
        String str_hofnamereg= null;
        String str_villagecode= null;
        String str_statecode= null;
        String str_districtcode= null;
        String str_blockcode= null;
        String str_enddate = null;
        String str_insccode= null;
        String str_policyno= null;
        String str_policyamt= null;
        String str_startdate= null;
        JSONObject allfamilymember_obj= null;
        JSONArray familymember= null;
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

        URNNO= str_id;
        RsbyHouseholdItem householdItem = new RsbyHouseholdItem();
        householdItem.setIssuesTimespam(str_issueTimeSpam);
        householdItem.setUrnId(URNNO);
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
        try {
            familydetails_obj = RsbySmartCardDetails_obj.getJSONObject(TAG_FAMILYDETAILS);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            str_doorhouse = familydetails_obj.getString(TAG_DOORHOUSE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            str_panchyattowncode = familydetails_obj.getString(TAG_PANCHYATTOWNCODE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            str_hofnamereg = familydetails_obj.getString(TAG_HOFNAMEREG);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            str_villagecode = familydetails_obj.getString(TAG_VILLAGECODE);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            str_statecode = familydetails_obj.getString(TAG_STATECODE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            str_districtcode = familydetails_obj.getString(TAG_DISTRICTCODE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            str_blockcode = familydetails_obj.getString(TAG_BLOCKCODE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            policydetails_obj = RsbySmartCardDetails_obj.getJSONObject(TAG_POLICYDETAILS);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            str_enddate = policydetails_obj.getString(TAG_ENDDATE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            householdItem.setEnddate(str_enddate);
            str_insccode = policydetails_obj.getString(TAG_INSCCODE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            householdItem.setInsccode(str_insccode);
            str_policyno = policydetails_obj.getString(TAG_POLICYNO);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            householdItem.setPolicyno(str_policyno);
            str_policyamt = policydetails_obj.getString(TAG_POLICYAMT);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            householdItem.setPolicyamt(str_policyamt);
            str_startdate = policydetails_obj.getString(TAG_STARTDATE);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            householdItem.setStartdate(str_startdate);
            allfamilymember_obj = RsbySmartCardDetails_obj.getJSONObject(TAG_ALLFAMILYMEMBER);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            familymember = allfamilymember_obj.getJSONArray(TAG_FAMILYMEMBER);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        for(int familymember_i = 0; familymember_i < familymember.length(); familymember_i++){
            JSONObject familymember_obj= null;
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
            String str_memid = null;
            try {
                str_memid = familymember_obj.getString(TAG_MEMID);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            RSBYItem rsbyItem = new RSBYItem();
            rsbyItem.setGender(str_gender);
            rsbyItem.setDob(str_dob);
            rsbyItem.setUrnId(URNNO);
            rsbyItem.setUrnNo(URNNO);
            rsbyItem.setName(str_name);
            rsbyItem.setMemid(str_memid);
            rsbyItem.setRelcode(str_relcode);
            rsbyItem.setRsbyMemId(URNNO+str_memid);
            householdItem.setEnddate(str_enddate);
            rsbyItem.setInsccode(str_insccode);
            rsbyItem.setPolicyno(str_policyno);
            rsbyItem.setPolicyamt(str_policyamt);
            rsbyItem.setStartdate(str_startdate);
            rsbyItem.setEnddate(str_enddate);
            if(str_relcode.equalsIgnoreCase("01")){
                rsbyItem.setMemberPhoto1(str_base64string);
                householdItem.setRsbyMemId(URNNO+str_memid);
                householdItem.setGender(str_gender);
                householdItem.setDob(str_dob);
                householdItem.setUrnId(URNNO);
                householdItem.setName(str_name);
                householdItem.setMemid(str_memid);
                householdItem.setRelcode(str_relcode);
            }
            SeccDatabase.saveRSBYMember(rsbyItem,context);
        }
        SeccDatabase.rsbySaveHousehold(householdItem,context);
        if(selectedMemItem==null){
            selectedMemItem = new SelectedMemberItem();
            selectedMemItem.setRsbyHouseholdItem(householdItem);
            ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SELECTED_ITEM_FOR_VERIFICATION,selectedMemItem.serialize(),context);
        }

        return URNNO;
    }
    public class RsbyHouseholdAdapter extends RecyclerView.Adapter<RsbyHouseholdAdapter.MyViewHolder> {

        private ArrayList<RSBYItem> householdList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView nameTV, urnNoTV , dateOfBirthTV,genderTV , memberIdTV;
            public LinearLayout verifyLayout;

            public MyViewHolder(View view) {
                super(view);
                urnNoTV = (TextView) view.findViewById(R.id.urnNoTV);
                nameTV = (TextView) view.findViewById(R.id.nameTV);
                dateOfBirthTV = (TextView) view.findViewById(R.id.dateOfBirthTV);
                genderTV = (TextView) view.findViewById(R.id.genderTV);
                memberIdTV = (TextView) view.findViewById(R.id.memberIdTV);
                verifyLayout = (LinearLayout) view.findViewById(R.id.verifyLayout);
            }
        }


        public RsbyHouseholdAdapter(ArrayList<RSBYItem> arrayList) {
            this.householdList = arrayList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.rsby_member_item, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            final RSBYItem item = householdList.get(position);
            holder.urnNoTV.setText(item.getUrnId());
            if(item.getGender()!=null && !item.getGender().equalsIgnoreCase("")){
                if(item.getGender().equalsIgnoreCase("1")){
                    holder.genderTV.setText("Male");
                }else if(item.getGender().equalsIgnoreCase("2")){
                    holder.genderTV.setText("Female");
                }else{
                    holder.genderTV.setText("Other");
                }
            }

            holder.nameTV.setText(item.getName());

            holder.dateOfBirthTV.setText(AppUtility.convertRsbyDate(item.getDob()));
            holder.memberIdTV.setText(item.getMemid());
            holder.verifyLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedMemItem.setRsbyMemberItem(item);
                    selectedMemItem.setRsbyHouseholdItem(rsbyHouseHoldItem);
                    System.out.print(item);
                    System.out.print(rsbyHouseHoldItem);
                    ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                            AppConstant.SELECTED_ITEM_FOR_VERIFICATION, selectedMemItem.serialize(), context);
                    String payLoad = selectedMemItem.serialize().toString();
                    System.out.print(payLoad);
                    Intent theIntent = new Intent(context, RsbyValidationWithAadharActivity.class);
                    startActivity(theIntent);
                    leftTransition();
                }
            });
        }

        @Override
        public int getItemCount() {
            return householdList.size();
        }
    }
  /*  private void openRSBYFamilyListFragment(String urn){
        fragTransect=fra.beginTransaction();
        if(rsbyFamilyListFragment!=null){
            fragTransect.detach(rsbyFamilyListFragment);
            rsbyFamilyListFragment=null;
        }
        Bundle bundle = new Bundle();
        bundle.putString("URN", urn);
        rsbyFamilyListFragment=new RsbyFamilyListFragment();
        rsbyFamilyListFragment.setArguments(bundle);
        //  rsbyFamilyListFragment.setRequiredUrnId(urn);
        fragTransect.replace(R.id.rsbyFamilyListMainContainer,rsbyFamilyListFragment);
        fragTransect.commitAllowingStateLoss();
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AppConstant.rsbyDataCode) {
            if (resultCode == Activity.RESULT_OK) {
                //  String res=data.getExtras();
                String result = data.getStringExtra(AppConstant.RSBYCARDRESPONSE);
                // Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
                loadDataOfCard(result);
         /*       String result0 = data.getDataString();
                String result2 = String.valueOf(data.getBundleExtra(AppConstant.RSBYCARDRESPONSE));
                String result1 = data.getParcelableExtra(AppConstant.RSBYCARDRESPONSE);
                Bundle MBuddle = data.getExtras();
                String MMessage = MBuddle.getString(AppConstant.RSBYCARDRESPONSE);

                System.out.print(result);
                System.out.print(result1);
                System.out.print(MMessage);

                Toast.makeText(context, MMessage, Toast.LENGTH_LONG).show();*/
            }
        }

        if(requestCode==CHOOSE_NEW_HEAD_STATUS){
            refreshListMember();
        }


        //super.onActivityResult(requestCode, resultCode, data);
    }

    private void refreshListMember(){
        ArrayList<RSBYItem> refreshList=new ArrayList<>();
        SelectedMemberItem selectedMemberItem= SelectedMemberItem.create(ProjectPrefrence.
                getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SELECTED_ITEM_FOR_VERIFICATION,context));
        ArrayList<RSBYItem> seccFamilyList=selectedMemberItem.getRsbyRelationUpdatedList();
        if(seccFamilyList!=null) {
            for (RSBYItem item : seccFamilyList) {
                if (item.getNhpsRelationCode() != null && item.getNhpsRelationCode().equalsIgnoreCase(AppConstant.NEW_HEAD_RELATION_CODE)) {
                    // changedHead=true;
                    newHeadItem = item;
                    break;
                }
            }
            if(newHeadItem!=null) {
                if (houseHoldMemberList != null) {
                    for (RSBYItem item : houseHoldMemberList) {
                        if(item.getRsbyMemId()!=null && newHeadItem.getNhpsMemId()!=null&& item.getRsbyMemId().trim().equalsIgnoreCase(newHeadItem.getNhpsMemId().trim())){
                            // skip new head
                        }else if(item.getRsbyMemId()!=null && rsbyHouseHoldItem.getRsbyMemId()!=null && item.getRsbyMemId().equalsIgnoreCase(rsbyHouseHoldItem.getRsbyMemId().trim())){
                            // find old head
                            oldHeadItem=item;
                        }else{
                            refreshList.add(item);
                        }
                    }
                    refreshList.add(0,newHeadItem);
                    if(oldHeadItem!=null){
                        oldHeadItem.setHhStatus(householdStatus.getStatusCode());
                        oldHeadItem.setMemStatus(hofStatusItem.getStatusCode());
                        oldHeadItem.setLockedSave(AppConstant.LOCKED+"");
                        //  SeccDatabase.updateSeccMember(oldHeadItem,context);
                    }
                    refreshList.add(houseHoldMemberList.size()-1, oldHeadItem);
                    houseHoldMemberList=refreshList;
                    showNewHeadList();
                }
            }
        }


       /* if(seccFamilyList!=null) {
            for (SeccMemberItem item : seccFamilyList) {
                if (item.getNhpsRelationCode() != null && item.getNhpsRelationCode().equalsIgnoreCase(AppConstant.NEW_HEAD_RELATION_CODE)) {
                    // changedHead=true;
                   newHeadItem = item;
                    break;
                }
            }
            if (newHeadItem == null) {
                houseHoldMemberList = new ArrayList<>();
                for (SeccMemberItem item : seccFamilyList) {
                    if (item.getNhpsMemId().equalsIgnoreCase(houseHoldItem.getNhpsMemId())) {
                        houseHoldMemberList.add(0, item);
                    } else {
                        houseHoldMemberList.add(item);
                    }
                }
            } else {
                houseHoldMemberList = new ArrayList<>();
                for (SeccMemberItem item : seccFamilyList) {
                    AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Nhps relation : "+item.getNhpsRelationName());
                    if (item.getNhpsRelationCode() != null && item.getNhpsRelationCode().equalsIgnoreCase(AppConstant.NEW_HEAD_RELATION_CODE)) {
                        houseHoldMemberList.add(0, item);
                    } else if (item.getNhpsMemId().equalsIgnoreCase(houseHoldItem.getNhpsMemId())) {
                        oldHeadItem = item;
                    } else {
                        houseHoldMemberList.add(item);
                    }
                }

            if(oldHeadItem!=null){
                oldHeadItem.setHhStatus(householdStatus.getStatusCode());
                oldHeadItem.setMemStatus(hofStatusItem.getStatusCode());
                oldHeadItem.setLockedSave(AppConstant.LOCKED+"");
              //  SeccDatabase.updateSeccMember(oldHeadItem,context);
            }
                houseHoldMemberList.add(houseHoldMemberList.size(), oldHeadItem);
            }


            TreeSet<SeccMemberItem> seccTreeSet = new TreeSet<SeccMemberItem>(new Comparator<SeccMemberItem>() {

                public int compare(SeccMemberItem o1, SeccMemberItem o2) {
                    // return 0 if objects are equal in terms of your properties
                    if (o1.getNhpsMemId().equalsIgnoreCase(o2.getNhpsMemId())) {
                        return 0;
                    }
                    return 1;
                }
            });
            seccTreeSet.addAll(houseHoldMemberList);
            houseHoldMemberList = new ArrayList<>();
            houseHoldMemberList.addAll(seccTreeSet);*/
    }

    private void searchMenu(){

        RelativeLayout menuLayout=(RelativeLayout)findViewById(R.id.menuLayout);
        menuLayout.setVisibility(View.VISIBLE);

        final ImageView settings=(ImageView)findViewById(R.id.settings);
        //  settings.setVisibility(View.GONE);
        menuLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settings.performClick();
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(context, settings);
                popup.getMenuInflater()
                        .inflate(R.menu.menu_rsby_home, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {

                            case R.id.cardReader:
                               /* Intent theIntent = new Intent(context, rsbyread.class);
                                startActivityForResult(theIntent, AppConstant.rsbyDataCode);
                                leftTransition();*/
                                break;

                        }
                        return true;
                    }
                });
                popup.show();
            }
        });

        menuLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settings.performClick();
            }
        });
    }

    private void prepareFamilyStatusSpinner(){
        boolean flag=false;
        int count=0;
        familyStatusList=new ArrayList<>();
        ArrayList<String> spinnerList=new ArrayList<>();
        ArrayList<FamilyStatusItem> tempItem= SeccDatabase.getFamilyStatusList(context);
        familyStatusList.add(0,new FamilyStatusItem("H","0","Select Household Status",""));

        for(RSBYItem item : houseHoldMemberList){
            if(item.getName()==null || item.getName().equalsIgnoreCase("")){
                count=count+1;
            }
        }
        if(count==houseHoldMemberList.size()){
            for (FamilyStatusItem item : tempItem) {
                if(item.getStatusCode().equalsIgnoreCase(AppConstant.NO_FAMILY_LIVING)) {
                    // spinnerList.add(item.statusDesc);
                    familyStatusList.add(item);
                }
            }
        }else {
            for (FamilyStatusItem item : tempItem) {
                // if(item.getStatusCode().equalsIgnoreCase(AppConstant.NO_FAMILY_LIVING)) {
                // spinnerList.add(item.statusDesc);
                familyStatusList.add(item);
                //  }
            }
        }
        for (FamilyStatusItem item : familyStatusList) {
            spinnerList.add(item.statusDesc);
        }
        ArrayAdapter<String> maritalAdapter = new ArrayAdapter<>(context, R.layout.custom_drop_down, R.id.textView,spinnerList);
        familyStatusSP.setAdapter(maritalAdapter);
        int selectedHouseholdStat=0;
        //    AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Household status : "+houseHoldItem.getHhStatus());
        if(familyStatusList!=null) {
            for (int i=0;i<familyStatusList.size();i++) {
                if (rsbyHouseHoldItem != null && rsbyHouseHoldItem.getHhStatus() != null&&
                        !rsbyHouseHoldItem.getHhStatus().equalsIgnoreCase("")) {
                    if (familyStatusList.get(i).getStatusCode().equalsIgnoreCase(rsbyHouseHoldItem.getHhStatus().trim())) {
                        selectedHouseholdStat =i;
                        break;
                    }
                }
            }
        }
        //    AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Household status : "+houseHoldItem.getHhStatus()+" : "+selectedHouseholdStat);
       if(selectedHouseholdStat==0){
           familyStatusSP.setSelection(1);
       }else {
           familyStatusSP.setSelection(selectedHouseholdStat);
       }
        //   AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Household status : "+houseHoldItem.getHhStatus()+" : "+familyStatusSP.getSelectedItemPosition());

    }

    private void prepareMemberStatusSpinner(){
        memberStatusList=new ArrayList<>();
        ArrayList<MemberStatusItem>  memberStatusList1= SeccDatabase.getMemberStatusList(context);
        memberStatusList.add(0,new MemberStatusItem("M","0","Select HoF Status",null,"Y"));
        for(MemberStatusItem item : memberStatusList1 ){
            if(rsbyHouseHoldItem.getName()!=null && !rsbyHouseHoldItem.getName().equalsIgnoreCase("")){
                if(item.getStatusCode().equalsIgnoreCase(AppConstant.MEMBER_ENROL_THROUGH_RSBY)){

                }else {
                    memberStatusList.add(item);
                }
            }else{
                if(item.getStatusCode().equalsIgnoreCase(AppConstant.NO_INFO_AVAIL)){
                    memberStatusList.add(item);
                }
            }
        }
        ArrayList<String> spinnerList=new ArrayList<>();
        for(MemberStatusItem item : memberStatusList){
            spinnerList.add(item.getStatusDesc());
        }
        ArrayAdapter<String> maritalAdapter = new ArrayAdapter<>(context, R.layout.custom_drop_down, R.id.textView,spinnerList);
        hofStatusSP.setAdapter(maritalAdapter);
        hofStatusSP.setSelection(1);
        int selectedHeadStat=0;
        if(memberStatusList!=null){
            for(int i=0;i<memberStatusList.size();i++){
                if(head!=null && head.getMemStatus()!=null && !head.getMemStatus().equalsIgnoreCase("")){
                    if(head.getMemStatus()!=null && head.getMemStatus()!=null &&  !head.
                            getMemStatus().equalsIgnoreCase("") && head.getMemStatus().trim().
                            equalsIgnoreCase(memberStatusList.get(i).getStatusCode())){
                        selectedHeadStat=i;
                        break;
                    }
                }
            }
        }
       // if(selectedHeadStat==0) {
            hofStatusSP.setSelection(selectedHeadStat);
     //   }
    }


    private void showDefaultFamilyList(){
        FragmentTransaction transect=fargManager.beginTransaction();
        if(defaultFragment!=null){
            transect.detach(defaultFragment);
            defaultFragment=null;
        }
        defaultFragment=new DefaultRsbyListFragment();
        defaultFragment.setHouseHoldMemberList(findDefaultMemberList());
        transect.replace(R.id.rsbyFamilyListContainer,defaultFragment);
        transect.commitAllowingStateLoss();
    }

    private void showOldHeadFamilyList(){
        // chooseHeadLayout.setVisibility(View.GONE);
        FragmentTransaction transect=fargManager.beginTransaction();
        if(oldHeadFragment!=null){
            transect.detach(oldHeadFragment);
            oldHeadFragment=null;
        }
        oldHeadFragment=new RsbyOldheadFragment();
        oldHeadFragment.setHouseHoldMemberList(findMemberListWithOldHead());
        transect.replace(R.id.rsbyFamilyListContainer,oldHeadFragment);
        transect.commitAllowingStateLoss();
    }

    private void showNewHeadList(){
        chooseHeadLayout.setVisibility(View.VISIBLE);
        FragmentTransaction transect=fargManager.beginTransaction();
        if(newHeadFragment!=null){
            transect.detach(newHeadFragment);
            newHeadFragment=null;
        }
        findMemberListWithNewHead();
        if(newHeadItem==null){
            houseHoldMemberList=findDefaultMemberList();
        }else{
            houseHoldMemberList=findMemberListWithNewHead();
        }
        for(RSBYItem item : houseHoldMemberList){
            // AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Member Name : "+item.getName()+" Relation : "+item.getNhpsRelationCode());
        }
        newHeadFragment=new RsbyNewHeadFragment();
        newHeadFragment.setHouseHoldMemberList(houseHoldMemberList);
        transect.replace(R.id.rsbyFamilyListContainer,newHeadFragment);
        transect.commitAllowingStateLoss();
    }

  /*  public static void openReset(final RSBYItem item1, Button button, final Context context, final Activity activity){


        PopupMenu popup = new PopupMenu(context, button);
        popup.getMenuInflater()
                .inflate(R.menu.menu_reset, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.reset:
                        alertForValidateLater(context.getResources().getString(R.string.alert_for_reset),item1,context,activity,AppConstant.RESET);
                        break;
                    case R.id.unlockRecord:
                        // askPinToLock(SeccMemberListActivity.EDIT,item1);
                        alertForValidateLater(context.getResources().getString(R.string.alert_for_unlock),item1,context,activity,AppConstant.UNLOCK);
                        break;
                    case R.id.preview:
                        AppUtility.openSyncPreview(item1,context,activity);
                        break;
                }
                return true;
            }
        });
        popup.show();
    }*/

    private static void alertForValidateLater(String msg, final RSBYItem item, final Context context, final Activity activity, final String action){

        final AlertDialog internetDiaolg = new AlertDialog.Builder(context).create();
        LayoutInflater factory = LayoutInflater.from(context);
        View alertView = factory.inflate(R.layout.internet_try_again_popup, null);
        internetDiaolg.setView(alertView);
        internetDiaolg.show();
        TextView tryGainMsgTV=(TextView) alertView.findViewById(R.id.deleteMsg);
        tryGainMsgTV.setText(msg);
        Button tryAgainBT=(Button) alertView.findViewById(R.id.tryAgainBT);
        tryAgainBT.setText("Confirm");
        Button cancelBT=(Button)alertView.findViewById(R.id.cancelBT);
        tryAgainBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(action.equalsIgnoreCase(AppConstant.RESET)) {
                   // askPinToLock(AppConstant.RESET, item, context, activity);
                    internetDiaolg.dismiss();
                }else if(action.equalsIgnoreCase(AppConstant.UNLOCK)){
                    SeccDatabase.editRsbyRecord(item,context);
                   /* Intent intent=new Intent(context, SeccMemberListActivity.class);
                    activity.startActivity(intent);
                    activity.finish();*/
                    ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.DASHBOARD_TAB_STATUS,3+"",context);
                    ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                            AppConstant.HOUSEHOLD_TAB_STATUS,6+"",context);
                    Intent theIntent = new Intent(context,SearchActivityWithHouseHold.class);
                    activity.startActivity(theIntent);
                }
            }
        });
        cancelBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                internetDiaolg.dismiss();
            }
        });
    }

    public void askPinToLock(final String status, final RSBYItem item, final Context context, final Activity activity) {
        final AlertDialog askForPinDailog = new AlertDialog.Builder(context).create();
        LayoutInflater factory = LayoutInflater.from(context);
        View alertView = factory.inflate(R.layout.ask_pin_layout, null);
        askForPinDailog.setView(alertView);
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
                // errorTV.setVisibility(View.GONE);
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





                String pin = pinET.getText().toString();
                if (pin.toString().equalsIgnoreCase(verifierDetail.getPin())) {
                    if(status.equalsIgnoreCase(AppConstant.RESET)){
                        //SeccDatabase.resetData(item,context);
                        RsbyHouseholdItem houseHoldItem= SeccDatabase.getRsbyHouseHoldQ(item.getUrnId(),context);
                        SelectedMemberItem selectedMemberItem= SelectedMemberItem.create(ProjectPrefrence.
                                getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                                        AppConstant.SELECTED_ITEM_FOR_VERIFICATION,context));
                        selectedMemberItem.setRsbyHouseholdItem(houseHoldItem);
                        ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                                AppConstant.SELECTED_ITEM_FOR_VERIFICATION,selectedMemberItem.serialize(),context);
                        askForPinDailog.dismiss();
                        activity.finish();
                        Intent intent=new Intent(context, RsbyMainActivity.class);
                        // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        activity.startActivity(intent);
                    }
                } else if (pin.equalsIgnoreCase("")) {
                    // CustomAlert.alertWithOk(context,"Please enter valid pin");
                    errorTV.setVisibility(View.VISIBLE);
                    errorTV.setText("Enter pin");
                    pinET.setText("");
                } else if (!pin.toString().equalsIgnoreCase(verifierDetail.getPin())) {
                    if (wrongPinCount >= 2) {
                        errorTV.setTextColor(context.getResources().getColor(R.color.red));
                        wrongAttempetCountValue.setTextColor(context.getResources().getColor(R.color.red));
                        wrongAttempetCountText.setTextColor(context.getResources().getColor(R.color.red));
                    }
                    wrongPinCount++;
                    wrongAttempetCountValue.setText((3 - wrongPinCount)+"");
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
                askForPinDailog.dismiss();
            }
        });
    }
}