package com.nhpm.LocalDataBase.dto;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.customComponent.utility.ProjectPrefrence;
import com.nhpm.LocalDataBase.DatabaseHelpers;
import com.nhpm.Models.ApplicationDataModel;
import com.nhpm.Models.DownloadedDataCountModel;
import com.nhpm.Models.NotificationModel;
import com.nhpm.Models.response.RelationItem;
import com.nhpm.Models.response.master.AadhaarStatusItem;
import com.nhpm.Models.response.master.ConfigurationItem;
import com.nhpm.Models.response.master.HealthSchemeItem;
import com.nhpm.Models.response.master.MemberRelationItem;
import com.nhpm.Models.response.master.MemberStatusItem;
import com.nhpm.Models.response.master.StateItem;
import com.nhpm.Models.response.rsbyMembers.RSBYItem;
import com.nhpm.Models.response.rsbyMembers.RSBYPoliciesItem;
import com.nhpm.Models.response.rsbyMembers.RsbyCardCategoryItem;
import com.nhpm.Models.response.rsbyMembers.RsbyHouseholdItem;
import com.nhpm.Models.response.rsbyMembers.RsbyPoliciesCompany;
import com.nhpm.Models.response.rsbyMembers.RsbyRelationItem;
import com.nhpm.Models.response.seccMembers.ErrorItem;
import com.nhpm.Models.response.seccMembers.HouseHoldItem;
import com.nhpm.Models.response.seccMembers.SeccMemberItem;
import com.nhpm.Models.response.verifier.FamilyStatusItem;
import com.nhpm.Models.response.verifier.VerifierLocationItem;
import com.nhpm.Utility.AppConstant;
import com.nhpm.Utility.AppUtility;
import com.nhpm.Utility.SyncUtility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Anand on 29-10-2016.
 */
public class SeccDatabase extends CommonDatabase {
    private final static String TAG = "SeccDatabase";


    //////////   secc database
    public static RSBYPoliciesItem getRsbyPolicyItem(Context context, String policy, String stateCode, String distrctCode) {
        String query = "Select * from "
                + AppConstant.RSBY_POLICIES_TABLE + " where policyNo='" + policy + "' AND stateCode='" + stateCode + "'  AND districtCode='" + distrctCode + "'";
        return CommonDatabase.getRSBYPolicies(context, query);
    }


    public static ArrayList<ApplicationDataModel> getApplicationDataItem(Context context) {
        String query = "Select * from "
                + AppConstant.USER_DATA_TABLE;// + " where config_id='" + congigId + "'";
        return CommonDatabase.getApplicationData(query, context);
    }

    public static ArrayList<RSBYPoliciesItem> getRsbyPolicyItemList(Context context) {
        String query = "Select * from "
                + AppConstant.RSBY_POLICIES_TABLE;// + " where policyNo='" + policy + "' AND stateCode='" + stateCode + "'  AND districtCode='" + distrctCode + "'";
        return CommonDatabase.getRSBYPoliciesList(context, query);
    }

    public static void resetData(SeccMemberItem item, Context context) {
        if (item != null && item.getDataSource() != null && item.getDataSource().trim()
                .equalsIgnoreCase(AppConstant.RSBY_SOURCE)) {
            ArrayList<SeccMemberItem> list = getRsbyMemberListWithUrn(item.getRsbyUrnId(), context);
            if (list != null) {
                for (SeccMemberItem item1 : list) {
                    SeccMemberItem item2 = SyncUtility.clearCollectedData(item1);
                    updateRsbyMember(item2, context);
                }
                //  HouseHoldItem houseHoldItem=getHouseHoldDetailsByHhdNo(item.getHhdNo(),context);
                HouseHoldItem houseHoldItem = SeccDatabase.getRsbyHouseHoldByUrn(item.getRsbyUrnId(), context);
                AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "House hold item : " + houseHoldItem.serialize());
                HouseHoldItem houseHoldItem1 = SyncUtility.clearHousehold(houseHoldItem);
                AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "House hold item after clear: " + houseHoldItem1.serialize());
                updateRsbyHousehold(houseHoldItem1, context);
            }
        } else {
            ArrayList<SeccMemberItem> list = getSeccMemberList(item.getHhdNo(), context);
            if (list != null) {
                for (SeccMemberItem item1 : list) {
                    SeccMemberItem item2 = SyncUtility.clearCollectedData(item1);
                    updateSeccMember(item2, context);
                }
                //  HouseHoldItem houseHoldItem=getHouseHoldDetailsByHhdNo(item.getHhdNo(),context);
                HouseHoldItem houseHoldItem = SeccDatabase.getHouseHoldList(item.getHhdNo(), context);
                AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "House hold item : " + houseHoldItem.serialize());
                HouseHoldItem houseHoldItem1 = SyncUtility.clearHousehold(houseHoldItem);
                AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "House hold item after clear: " + houseHoldItem1.serialize());
                updateHouseHold(houseHoldItem1, context);
            }
        }
    }

    /* public static void resetRsbyData(RSBYItem item, Context context){
         ArrayList<RSBYItem> list=getRsbyMemberList(item.getUrnId(),context);
         if(list!=null){
             for(RSBYItem item1 : list){
                 RSBYItem item2= SyncUtility.clearRsbyCollectedData(item1);
                // updateSeccMember(item2,context);
             }
           *//*  //  HouseHoldItem houseHoldItem=getHouseHoldDetailsByHhdNo(item.getHhdNo(),context);
            HouseHoldItem houseHoldItem= SeccDatabase.getHouseHoldList(item.getHhdNo(), context);
            AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"House hold item : "+houseHoldItem.serialize());
            HouseHoldItem houseHoldItem1=SyncUtility.clearHousehold(houseHoldItem);
            AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"House hold item after clear: "+houseHoldItem1.serialize());
            updateHouseHold(houseHoldItem1,context);*//*
        }
    }*/
    public static void editRecord(SeccMemberItem item, Context context) {

        item.setLockedSave(AppConstant.SAVE + "");
        item.setError_code(null);
        item.setError_msg(null);
        item.setError_type(null);
        if (item.getDataSource() != null && item.getDataSource().trim().equalsIgnoreCase(AppConstant.RSBY_SOURCE)) {
            SeccDatabase.updateRsbyMember(item, context);

        } else {
            SeccDatabase.updateSeccMember(item, context);
        }
        if (item.getDataSource() != null && item.getDataSource().trim().equalsIgnoreCase(AppConstant.RSBY_SOURCE)) {
            HouseHoldItem houseHoldItem = SeccDatabase.getRsbyHouseHoldByUrn(item.getRsbyUrnId(), context);
            houseHoldItem.setLockSave(AppConstant.SAVE + "");
            houseHoldItem.setError_code(null);
            houseHoldItem.setError_msg(null);
            houseHoldItem.setError_type(null);
            AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "House hold item : " + houseHoldItem.serialize());
            updateRsbyHousehold(houseHoldItem, context);
        } else {
            HouseHoldItem houseHoldItem = SeccDatabase.getHouseHoldList(item.getHhdNo(), context);
            houseHoldItem.setLockSave(AppConstant.SAVE + "");
            houseHoldItem.setError_code(null);
            houseHoldItem.setError_msg(null);
            houseHoldItem.setError_type(null);
            AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "House hold item : " + houseHoldItem.serialize());
            updateHouseHold(houseHoldItem, context);
        }
    }

    public static void editRsbyRecord(RSBYItem item, Context context) {
        item.setLockedSave(AppConstant.SAVE + "");
        item.setError_code(null);
        item.setError_msg(null);
        item.setError_type(null);
        SeccDatabase.updateRsbyMember(item, context);
        RsbyHouseholdItem houseHoldItem = SeccDatabase.getRsbyHouseHoldQ(item.getUrnId(), context);
        houseHoldItem.setLockedSave(AppConstant.SAVE + "");
        houseHoldItem.setError_code(null);
        houseHoldItem.setError_msg(null);
        houseHoldItem.setError_type(null);
        AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "House hold item : " + houseHoldItem.serialize());
        updateRSBYHouseHold(houseHoldItem, context);
    }

    public static void alterTable(String query, Context context) {
        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        SQLiteDatabase mDatabase = helpers.createOrOpenDatabase();
        mDatabase.execSQL(query);
        mDatabase.close();
        helpers.close();

    }

    public static void deleteTable(String query, Context context) {
        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        SQLiteDatabase mDatabase = helpers.createOrOpenDatabase();
        mDatabase.execSQL(query);
        mDatabase.close();
        helpers.close();

    }

    public static void createTable(String query, Context context) {
        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        SQLiteDatabase mDatabase = helpers.createOrOpenDatabase();
        mDatabase.execSQL(query);
        Log.d(TAG, "Table created" + AppConstant.MEMBER_ERROR_TABLE);
        mDatabase.close();
        helpers.close();
        //  db.execSQL(
    }

    public static long saveMemberError(Context context, ErrorItem item) {
        DatabaseHelpers helper = DatabaseHelpers.getInstance(context);
        helper.createOrOpenDatabase();
        ContentValues values = new ContentValues();
        values.put("ahl_tin", item.getAhlTin());
        values.put("nhps_mem_id", item.getNhpsMemId());
        values.put("error_code", item.getErrorCode());
        values.put("error_msg", item.getErrorMsg());
        values.put("error_type", item.getErrorType());
        values.put("hhd_no", item.getHhdNo());


        SQLiteDatabase db = helper.getWritableDatabase();
        long insertFlag = db.insert(AppConstant.MEMBER_ERROR_TABLE, null,
                values);
        db.close();
        helper.close();
        return insertFlag;
    }

    public static void deleteErrorFromMember(Context context, String nhpsMemId) {
        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);

        SQLiteDatabase db = helpers.getWritableDatabase();
        String query = "delete from " + AppConstant.MEMBER_ERROR_TABLE + " where nhps_mem_id='" + nhpsMemId + "'";
        Log.d(TAG, " delete query : " + query);
        db.execSQL(query);
        Log.d(TAG, " Row deleted");
        db.close();
        helpers.close();
    }

    public static long updateMemberError(Context context, ErrorItem item) {
        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);

        SQLiteDatabase db = helpers.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("ahl_tin", item.getAhlTin());
        values.put("nhps_mem_id", item.getNhpsMemId());
        values.put("error_code", item.getErrorCode());
        values.put("error_msg", item.getErrorMsg());
        values.put("error_type", item.getErrorType());
        values.put("error_type", item.getErrorType());
        values.put("hhd_no", item.getHhdNo());


        int updateFlag = db.update(AppConstant.MEMBER_ERROR_TABLE, values,
                "nhps_mem_id='" + item.getNhpsMemId() + "'", null);
        // Log.d("ID",item.getId()+"FATHER"+item.getFathernmNpr());
        Log.d("Update Member Data", " Update Flag Value : " + updateFlag);
        db.close();
        helpers.close();
        return updateFlag;
    }

    public static ErrorItem getMemberError(Context context, String nhpsMemId) {
        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        SQLiteDatabase mDatabase = helpers.createOrOpenDatabase();
        // Cursor findNormalItems = mDatabase.query(AppConstant.HOUSE_HOLD_SECC,new String[] { "ahl_tin" }, "type != ?",new String[] { "onSale" });
        String query = "Select * from "
                + AppConstant.MEMBER_ERROR_TABLE + " WHERE nhps_mem_id='" + nhpsMemId + "'";
        Log.d("Secc Database", " Query count surveyed : " + query);
        ErrorItem item = null;
        Cursor cur = mDatabase.rawQuery(query, null);
        if (cur != null) {
            if (cur.getCount() > 0) {
                cur.moveToFirst();
                while (cur.isAfterLast() == false) {
                    item = new ErrorItem();
                    item.setNhpsMemId(cur.getString(cur.getColumnIndex("nhps_mem_id")));
                    item.setAhlTin(cur.getString(cur.getColumnIndex("ahl_tin")));
                    item.setErrorCode(cur.getString(cur.getColumnIndex("error_code")));
                    item.setErrorMsg(cur.getString(cur.getColumnIndex("error_msg")));
                    item.setErrorType(cur.getString(cur.getColumnIndex("error_type")));
                    item.setHhdNo(cur.getString(cur.getColumnIndex("hhd_no")));
                    cur.moveToNext();
                }
            }
            cur.close();
        }
        mDatabase.close();
        helpers.close();
        return item;
    }

    public static ErrorItem getMemberErrorByHhdNo(Context context, String hhdNo) {
        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        SQLiteDatabase mDatabase = helpers.createOrOpenDatabase();
        // Cursor findNormalItems = mDatabase.query(AppConstant.HOUSE_HOLD_SECC,new String[] { "ahl_tin" }, "type != ?",new String[] { "onSale" });
        String query = "Select * from "
                + AppConstant.MEMBER_ERROR_TABLE + " WHERE hhd_no='" + hhdNo + "'";
        //  Log.d("Secc Database"," Query count surveyed : "+query);
        ErrorItem item = null;
        Cursor cur = mDatabase.rawQuery(query, null);
        if (cur != null) {
            if (cur.getCount() > 0) {
                cur.moveToFirst();
                while (cur.isAfterLast() == false) {
                    item = new ErrorItem();
                    item.setNhpsMemId(cur.getString(cur.getColumnIndex("nhps_mem_id")));
                    //item.setAhlTin(cur.getString(cur.getColumnIndex("ahl_tin")));
                    item.setErrorCode(cur.getString(cur.getColumnIndex("error_code")));
                    item.setErrorMsg(cur.getString(cur.getColumnIndex("error_msg")));
                    item.setErrorType(cur.getString(cur.getColumnIndex("error_type")));
                    item.setHhdNo(cur.getString(cur.getColumnIndex("hhd_no")));

                    cur.moveToNext();
                }
            }
            cur.close();
        }
        mDatabase.close();
        helpers.close();
        return item;
    }

    public static ArrayList<HouseHoldItem> getAllHouseHold(Context context) {
        String query = "Select * from "
                + AppConstant.HOUSE_HOLD_SECC;

        return CommonDatabase.getHouseholdList(context, query);
    }

    public static HouseHoldItem getHouseHoldList(String hhdNo, Context context) {
        String query = "Select * from "
                + AppConstant.HOUSE_HOLD_SECC
                + " where hhd_no='"
                + hhdNo
                + "'";

        return CommonDatabase.getHouseholdDetail(context, query);
    }

    public static HouseHoldItem getRsbyHouseHoldList(String urnId, Context context) {
        String query = "Select * from "
                + AppConstant.HOUSE_HOLD_SECC
                + " where rsby_urnId='"
                + urnId
                + "'";
        return CommonDatabase.getHouseholdDetail(context, query);
    }

    public static HouseHoldItem getHouseHoldDetailsByHhdNo(String hhdNo, Context context) {
        String query = "Select * from "
                + AppConstant.HOUSE_HOLD_SECC
                + " where hhd_no='"
                + hhdNo
                + "'";
        return CommonDatabase.getHouseholdDetail(context, query);
    }

    public static long updateHouseHold(HouseHoldItem item, Context context) {
        return CommonDatabase.updateHousehold(context, item);
    }

    public static ArrayList<HouseHoldItem> getHouseHoldList(String stateCode, String distCode, String tehsilCode,
                                                            String villTownCode, String wardCode, String blockCode, Context context) {
        String query = "Select * from "
                + AppConstant.HOUSE_HOLD_SECC
                + " where statecode='"
                + stateCode.trim()
                + "' AND districtcode='" +
                distCode.trim()
                + "' AND tehsilcode='" + tehsilCode.trim()
                + "' AND towncode='" + villTownCode.trim() + "' AND wardid='" + wardCode.trim()
                + "' AND ahlblockno ='" + blockCode.trim() + "'";
        AppUtility.showLog(AppConstant.LOG_STATUS, TAG, " Household Query : " + query);


        return CommonDatabase.getHouseholdList(context, query);
    }

    public static ArrayList<HouseHoldItem> getHouseHoldListSubBlockWise(String stateCode, String distCode, String tehsilCode,
                                                                        String villTownCode, String wardCode, String blockCode, String subBlockCode, Context context) {
        String query = "Select * from "
                + AppConstant.HOUSE_HOLD_SECC
                + " where statecode='"
                + stateCode.trim()
                + "' AND districtcode='" +
                distCode.trim()
                + "' AND tehsilcode='" + tehsilCode.trim()
                + "' AND towncode='" + villTownCode.trim() + "' AND wardid='" + wardCode.trim()
                + "' AND ahlblockno ='" + blockCode.trim() + "' AND ahlsubblockno ='" + subBlockCode.trim() + "'";
        AppUtility.showLog(AppConstant.LOG_STATUS, TAG, " Household Query : " + query);


        return CommonDatabase.getHouseholdList(context, query);
    }

    public static ArrayList<HouseHoldItem> getSeccHouseHoldList(String stateCode, String distCode, String tehsilCode,
                                                                String villTownCode, String wardCode, String blockCode, Context context) {
        String query = "Select * from "
                + AppConstant.HOUSE_HOLD_SECC
                + " where statecode='"
                + stateCode.trim()
                + "' AND districtcode='" +
                distCode.trim()
                + "' AND data_source='" +
                AppConstant.SECC_SOURCE
                + "' AND tehsilcode='" + tehsilCode.trim()
                + "' AND towncode='" + villTownCode.trim() + "' AND wardid='" + wardCode.trim()
                + "' AND ahlblockno ='" + blockCode.trim() + "'";
        AppUtility.showLog(AppConstant.LOG_STATUS, TAG, " Household Query : " + query);


        return CommonDatabase.getHouseholdList(context, query);
    }

    /* public static ArrayList<HouseHoldItem> getHouseHoldList(String stateCode, String distCode, String tehsilCode,
                                                                 String villTownCode, String wardCode, String blockCode, Context context) {
         String query = "Select * from "
                 + AppConstant.HOUSE_HOLD_SECC
                 + " where statecode='"
                 + stateCode.trim()
                 + "' AND districtcode='" +
                 distCode.trim()
                *//* + "' AND data_source='" +
                AppConstant.SECC_SOURCE*//*
                + "' AND tehsilcode='" + tehsilCode.trim()
                + "' AND towncode='" + villTownCode.trim() + "' AND wardid='" + wardCode.trim()
                + "' AND ahlblockno ='" + blockCode.trim() + "'";
        AppUtility.showLog(AppConstant.LOG_STATUS, TAG, " Household Query : " + query);


        return CommonDatabase.getHouseholdList(context, query);
    }*/
    public static ArrayList<HouseHoldItem> getSeccHouseHoldVillageWiseList(String stateCode, String distCode, String tehsilCode,
                                                                           String villTownCode, Context context) {
        String query = "Select * from "
                + AppConstant.HOUSE_HOLD_SECC
                + " where statecode='"
                + stateCode.trim()
                + "' AND districtcode='" +
                distCode.trim()
                + "' AND data_source='" +
                AppConstant.SECC_SOURCE
                + "' AND tehsilcode='" + tehsilCode.trim()
                + "' AND towncode='" + villTownCode.trim() + "'";
        AppUtility.showLog(AppConstant.LOG_STATUS, TAG, " Household Query : " + query);


        return CommonDatabase.getHouseholdList(context, query);
    }

    public static ArrayList<HouseHoldItem> getHouseHoldVillageWiseList(String stateCode, String distCode, String tehsilCode,
                                                                       String villTownCode, Context context) {
        String query = "Select * from "
                + AppConstant.HOUSE_HOLD_SECC
                + " where statecode='"
                + stateCode.trim()
                + "' AND districtcode='" +
                distCode.trim()
              /*  + "' AND data_source='" +
                AppConstant.SECC_SOURCE*/
                + "' AND tehsilcode='" + tehsilCode.trim()
                + "' AND towncode='" + villTownCode.trim() + "'";
        AppUtility.showLog(AppConstant.LOG_STATUS, TAG, " Household Query : " + query);


        return CommonDatabase.getHouseholdList(context, query);
    }

    public static ArrayList<HouseHoldItem> getRSBYHouseHoldList(String stateCode, String distCode, String tehsilCode,
                                                                String villTownCode, String wardCode, String blockCode, Context context) {
        String query = "Select * from "
                + AppConstant.HOUSE_HOLD_SECC
                + " where statecode='"
                + stateCode.trim()
                + "' AND districtcode='" +
                distCode.trim()
                + "' AND data_source='" +
                AppConstant.RSBY_SOURCE
                + "' AND tehsilcode='" + tehsilCode.trim()
                + "' AND towncode='" + villTownCode.trim() + "' AND wardid='" + wardCode.trim()
                + "' AND ahlblockno ='" + blockCode.trim() + "'";
        AppUtility.showLog(AppConstant.LOG_STATUS, TAG, " Household Query : " + query);


        return CommonDatabase.getHouseholdList(context, query);
    }

    public static ArrayList<HouseHoldItem> getRSBYHouseHoldListWardWise(String stateCode, String distCode, String tehsilCode,
                                                                        String villTownCode, String wardCode, Context context) {
        String query = "Select * from "
                + AppConstant.HOUSE_HOLD_SECC
                + " where statecode='"
                + stateCode.trim()
                + "' AND districtcode='" +
                distCode.trim()
                + "' AND data_source='" +
                AppConstant.RSBY_SOURCE
                + "' AND tehsilcode='" + tehsilCode.trim()
                + "' AND towncode='" + villTownCode.trim() + "' AND wardid='" + wardCode.trim() + "'";
        AppUtility.showLog(AppConstant.LOG_STATUS, TAG, " Household Query : " + query);


        return CommonDatabase.getHouseholdList(context, query);
    }

    public static ArrayList<HouseHoldItem> getRSBYHouseHoldListVillageWise(String stateCode, String distCode, String tehsilCode,
                                                                           String villTownCode, Context context) {
        String query = "Select * from "
                + AppConstant.HOUSE_HOLD_SECC
                + " where statecode='"
                + stateCode.trim()
                + "' AND districtcode='" +
                distCode.trim()
                + "' AND data_source='" +
                AppConstant.RSBY_SOURCE
                + "' AND tehsilcode='" + tehsilCode.trim()
                + "' AND towncode='" + villTownCode.trim() + "' ";
        AppUtility.showLog(AppConstant.LOG_STATUS, TAG, " Household Query : " + query);


        return CommonDatabase.getHouseholdList(context, query);
    }

    public static long saveHouseHold(HouseHoldItem item, Context context) {
        return CommonDatabase.saveHousehold(context, item);
    }

    public static ArrayList<SeccMemberItem> getSeccMemberList(String hhdNo, Context context) {
        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        SQLiteDatabase mDatabase = helpers.createOrOpenDatabase();
        ArrayList<SeccMemberItem> seccMemberItems = new ArrayList<>();
        String query = "Select * from "
                + AppConstant.TRANSECT_POPULATE_SECC
                + " where hhd_no='" + hhdNo + "'";
        Cursor cur = mDatabase.rawQuery(query, null);
        Log.d("SECC Data ", " Query : " + cur.getCount());

        return CommonDatabase.getSeccMemberList(context, query);
    }

    public static ArrayList<SeccMemberItem> getSeccMemberListWithMobileNoSelf(String hhdNo,String whoseMobile,String mobileNo, Context context) {
        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        SQLiteDatabase mDatabase = helpers.createOrOpenDatabase();
        ArrayList<SeccMemberItem> seccMemberItems = new ArrayList<>();
        String query = "Select * from "
                + AppConstant.TRANSECT_POPULATE_SECC
                + " where hhd_no='" + hhdNo + "'"
                + "and whose_mobile ='" + whoseMobile + "'"
                + "and mobile_no ='" + mobileNo + "'";
        Cursor cur = mDatabase.rawQuery(query, null);
        Log.d("SECC Data ", " Query : " + cur.getCount());

        return CommonDatabase.getSeccMemberList(context, query);
    }

    public static ArrayList<SeccMemberItem> getRsbyFamilyMemberList(String UrnId, Context context) {
        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        SQLiteDatabase mDatabase = helpers.createOrOpenDatabase();
        ArrayList<SeccMemberItem> seccMemberItems = new ArrayList<>();
        String query = "Select * from "
                + AppConstant.TRANSECT_POPULATE_SECC
                + " where urnId='" + UrnId + "'";
        Cursor cur = mDatabase.rawQuery(query, null);
        Log.d("SECC Data ", " Query : " + cur.getCount());

        return CommonDatabase.getSeccMemberList(context, query);
    }

    public static long updateSeccMember(SeccMemberItem item, Context context) {

        return CommonDatabase.update(item, context);
    }

    public static long updateRsbyMember(SeccMemberItem item, Context context) {

        return CommonDatabase.updateRsbyMember(item, context);
    }


    public static long saveSeccMember(SeccMemberItem item, Context context) {
        long insertFlag = CommonDatabase.save(item, context);
        return insertFlag;

    }

    public static long saveSeccMemberRelationMaster(MemberRelationItem item, Context context) {
        long insertFlag = CommonDatabase.saveRelationMaster(item, context);
        return insertFlag;

    }

    public static ArrayList<SeccMemberItem> getSeccMemberList(String stateCode, String distCode, String tehsilCode,
                                                              String villTownCode, String wardCode, String blockCode,
                                                              Context context) {
        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        SQLiteDatabase mDatabase = helpers.createOrOpenDatabase();
        ArrayList<SeccMemberItem> seccMemberItems = new ArrayList<>();
        String query = "Select * from "
                + AppConstant.TRANSECT_POPULATE_SECC
                + " where statecode='"
                + stateCode
                + "' AND districtcode='" +
                distCode
                + "' AND tehsilcode='" + tehsilCode
                + "' AND towncode='" + villTownCode + "' AND wardid='" + wardCode
                + "' AND blockno ='" + blockCode + "'";

        return CommonDatabase.getSeccMemberList(context, query);
    }

    public static ArrayList<SeccMemberItem> getSearchSeccMemberVillageWiseList(String stateCode, String distCode, String tehsilCode,
                                                                               String villTownCode/*, String wardCode, String blockCode*/,
                                                                               Context context) {


        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        SQLiteDatabase mDatabase = helpers.createOrOpenDatabase();
        ArrayList<SeccMemberItem> seccMemberItems = new ArrayList<>();
        String query = "Select name,fathername,ahlslnohhd,name_sl,hh_status,mem_status,addressline1,addressline2,addressline3,addressline4,addressline5,hhd_no,data_source,rsby_rsbyMemId,rsby_urnId,rsby_name,rsby_gender from "
                + AppConstant.TRANSECT_POPULATE_SECC
                + " where statecode='"
                + stateCode
                + "' AND districtcode='" +
                distCode
                + "' AND tehsilcode='" + tehsilCode
                + "' AND towncode='" + villTownCode +/* "' AND wardid='" + wardCode
                + "' AND blockno ='" + blockCode +*/ "'";

        return CommonDatabase.getSearchSeccMemberList(context, query);
    }

    public static ArrayList<SeccMemberItem> getSearchSeccMemberList(String stateCode, String distCode, String tehsilCode,
                                                                    String villTownCode, String wardCode, String blockCode,
                                                                    Context context) {


        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        SQLiteDatabase mDatabase = helpers.createOrOpenDatabase();
        ArrayList<SeccMemberItem> seccMemberItems = new ArrayList<>();
        String query = "Select name,fathername,ahlslnohhd,name_sl,hh_status,mem_status,addressline1,addressline2,addressline3,addressline4,addressline5,hhd_no,data_source,rsby_rsbyMemId,rsby_urnId,rsby_name,rsby_gender from "
                + AppConstant.TRANSECT_POPULATE_SECC
                + " where statecode='"
                + stateCode
                + "' AND districtcode='" +
                distCode
                + "' AND tehsilcode='" + tehsilCode
                + "' AND towncode='" + villTownCode + "' AND wardid='" + wardCode
                + "' AND blockno ='" + blockCode + "'";

        return CommonDatabase.getSearchSeccMemberList(context, query);
    }

    public static ArrayList<SeccMemberItem> getSeccMemberVillageWiseList(String stateCode, String distCode, String tehsilCode,
                                                                         String villTownCode,
                                                                         Context context) {
        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        SQLiteDatabase mDatabase = helpers.createOrOpenDatabase();
        ArrayList<SeccMemberItem> seccMemberItems = new ArrayList<>();
        String query = "Select * from "
                + AppConstant.TRANSECT_POPULATE_SECC
                + " where statecode='"
                + stateCode
                + "' AND districtcode='" +
                distCode
                + "' AND tehsilcode='" + tehsilCode
                + "' AND towncode='" + villTownCode + "'";

        return CommonDatabase.getSeccMemberList(context, query);
    }

    public static SeccMemberItem getSeccMemberDetail(SeccMemberItem item, Context context) {
        String query = null;
        if (item != null && item.getDataSource() != null && item.getDataSource().trim().equalsIgnoreCase(AppConstant.RSBY_SOURCE)) {
            query = "Select * from "
                    + AppConstant.TRANSECT_POPULATE_SECC
                    + " where rsby_rsbyMemId='" + item.getRsbyMemId() + "'";
        } else {


            query = "Select * from "
                    + AppConstant.TRANSECT_POPULATE_SECC
                    + " where nhps_mem_id='" + item.getNhpsMemId() + "'";
        }

        return CommonDatabase.getSeccMember(context, query);
    }

    public static SeccMemberItem seccMemberDetailByAadhaar(String aadhaarNumber, Context context) {
        String query = "Select * from "
                + AppConstant.TRANSECT_POPULATE_SECC
                + " where aadhaar_no='" + aadhaarNumber + "'";
        return CommonDatabase.getSeccMember(context, query);
    }

    public static ArrayList<SeccMemberItem> seccMemberListByAadhaar(String aadhaarNumber, Context context) {
        String query = "Select * from "
                + AppConstant.TRANSECT_POPULATE_SECC
                + " where aadhaar_no='" + aadhaarNumber + "'";
        return CommonDatabase.getSeccMemberList(context, query);
    }

    public static ArrayList<HouseHoldItem> seccHouseholdLockedList(String lockedStatus, Context context) {
        String query = "Select * from "
                + AppConstant.HOUSE_HOLD_SECC + " WHERE locked_save='" + lockedStatus + "'";
        return CommonDatabase.getHouseholdList(context, query);
    }

    public static ArrayList<SeccMemberItem> seccMemberWithAadhaarLocked(String adhaarNo, String lockedStatus, Context context) {
        String query = "Select * from "
                + AppConstant.TRANSECT_POPULATE_SECC
                + " where aadhaar_no='" + adhaarNo + "' AND locked_save='" + lockedStatus + "'";
        return CommonDatabase.getSeccMemberList(context, query);
    }

    public static ArrayList<SeccMemberItem> seccMemberLockedList(String lockedStatus, Context context) {
        String query = "Select * from "
                + AppConstant.TRANSECT_POPULATE_SECC + " WHERE locked_save='" + lockedStatus + "'";
        return CommonDatabase.getSeccMemberList(context, query);
    }

    public static long saveMemberStatus(MemberStatusItem item, Context context) {
        DatabaseHelpers helper = DatabaseHelpers.getInstance(context);
        helper.createOrOpenDatabase();
        ContentValues values = new ContentValues();
        values.put("sno", item.getSno());
        values.put("status_type", item.getStatusType());
        values.put("status_code", item.getStatusCode());
        values.put("status_desc", item.getStatusDesc());
        values.put("status_active", item.getIsActive());
        SQLiteDatabase db = helper.getWritableDatabase();
        long insertFlag = db.insert(AppConstant.MEMBER_STATUS, null,
                values);
        db.close();
        helper.close();
        return insertFlag;
    }

    public static long updateMemberStatus(MemberStatusItem item, Context context) {
        DatabaseHelpers helper = DatabaseHelpers.getInstance(context);
        helper.createOrOpenDatabase();
        ContentValues values = new ContentValues();
        if (item.getSno() != null)
            values.put("sno", item.getSno().trim());
        if (item.getStatusType() != null)
            values.put("status_type", item.getStatusType().trim());
        if (item.getStatusCode() != null)
            values.put("status_code", item.getStatusCode().trim());
        if (item.getIsActive() != null)
            values.put("status_active", item.getIsActive().trim());

        values.put("status_desc", item.getStatusDesc());
        SQLiteDatabase db = helper.getWritableDatabase();
        int updateFlag = db.update(AppConstant.MEMBER_STATUS, values,
                "status_code='" + item.getStatusCode() + "'", null);
        db.close();
        helper.close();
        return updateFlag;
    }

    public static MemberStatusItem getMemberStatusDetail(Context context, String statusCode) {
        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        SQLiteDatabase mDatabase = helpers.createOrOpenDatabase();
        ArrayList<MemberStatusItem> items = new ArrayList<>();
        MemberStatusItem item = null;
        String query = "Select * from "
                + AppConstant.MEMBER_STATUS + " WHERE status_code='" + statusCode.trim() + "'";

        Cursor cur = mDatabase.rawQuery(query, null);
        Log.d("SECC Data ", " Query : " + cur.getCount());
        if (cur != null) {
            if (cur.getCount() > 0) {
                cur.moveToFirst();
                while (cur.isAfterLast() == false) {
                    item = new MemberStatusItem();
                    item.setSno(cur.getString(cur.getColumnIndex("sno")));
                    item.setStatusCode(cur.getString(cur.getColumnIndex("status_code")));
                    item.setStatusType(cur.getString(cur.getColumnIndex("status_type")));
                    item.setStatusDesc(cur.getString(cur.getColumnIndex("status_desc")));
                    item.setIsActive(cur.getString(cur.getColumnIndex("status_active")));

                    cur.moveToNext();
                }
            }
            cur.close();

        }
        mDatabase.close();
        helpers.close();
        return item;
    }

    public static long saveAadhaarStatus(AadhaarStatusItem item, Context context) {
        DatabaseHelpers helper = DatabaseHelpers.getInstance(context);
        helper.createOrOpenDatabase();
        ContentValues values = new ContentValues();
        values.put("a_status_code", item.getaStatusCode());
        values.put("a_status_desc", item.getaStatusDesc());
        SQLiteDatabase db = helper.getWritableDatabase();
        long insertFlag = db.insert(AppConstant.AADHAAR_STATUS, null,
                values);
        db.close();
        helper.close();
        return insertFlag;
    }

    public static long saveHealthScheme(HealthSchemeItem item, Context context) {
        DatabaseHelpers helper = DatabaseHelpers.getInstance(context);
        helper.createOrOpenDatabase();
        ContentValues values = new ContentValues();
        if (item.getStatecode() != null) {
            values.put("statecode", item.getStatecode().trim());
        } else {
            values.put("statecode", item.getStatecode());
        }
        if (item.getSchemeId() != null) {
            values.put("scheme_id", item.getSchemeId().trim());
        } else {
            values.put("scheme_id", item.getSchemeId());
        }
        values.put("scheme_name", item.getSchemeName());
        SQLiteDatabase db = helper.getWritableDatabase();
        long insertFlag = db.insert(AppConstant.HEALTH_SCHEME, null,
                values);
        db.close();
        helper.close();
        return insertFlag;
    }

    public static long updateHealthScheme(HealthSchemeItem item, Context context) {
        DatabaseHelpers helper = DatabaseHelpers.getInstance(context);
        helper.createOrOpenDatabase();
        ContentValues values = new ContentValues();
        if (item.getStatecode() != null) {
            values.put("statecode", item.getStatecode().trim());
        }
        if (item.getSchemeId() != null) {
            values.put("scheme_id", item.getSchemeId().trim());
        }
        values.put("scheme_name", item.getSchemeName());
        SQLiteDatabase db = helper.getWritableDatabase();
        int updateFlag = db.update(AppConstant.HEALTH_SCHEME, values,
                "statecode='" + item.getStatecode() + "' AND scheme_id='" + item.getSchemeId() + "'", null);
        db.close();
        helper.close();
        return updateFlag;
    }

    public static ArrayList<MemberStatusItem> getMemberStatusList(Context context) {
        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        SQLiteDatabase mDatabase = helpers.createOrOpenDatabase();
        ArrayList<MemberStatusItem> items = new ArrayList<>();
        String query = "Select * from "
                + AppConstant.MEMBER_STATUS + " WHERE status_type='M'";

        Cursor cur = mDatabase.rawQuery(query, null);
        Log.d("SECC Data ", " Query : " + cur.getCount());
        if (cur != null) {
            if (cur.getCount() > 0) {
                cur.moveToFirst();
                while (cur.isAfterLast() == false) {
                    MemberStatusItem item = new MemberStatusItem();
                    item.setSno(cur.getString(cur.getColumnIndex("sno")));
                    item.setStatusCode(cur.getString(cur.getColumnIndex("status_code")));
                    item.setStatusType(cur.getString(cur.getColumnIndex("status_type")));
                    item.setStatusDesc(cur.getString(cur.getColumnIndex("status_desc")));
                    item.setIsActive(cur.getString(cur.getColumnIndex("status_active")));
                    items.add(item);
                    cur.moveToNext();
                }
            }
            cur.close();

        }
        mDatabase.close();
        helpers.close();

        Collections.sort(items, new Comparator<MemberStatusItem>() {
            @Override
            public int compare(MemberStatusItem o1, MemberStatusItem o2) {
                return Integer.parseInt(o1.getSno()) - Integer.parseInt(o2.getSno());
                      /*  if(status)
                        return 1;
                        else
                            return 0;*/

                //return Integer.compare(o1.getAge(), o2.getId());
            }
        });
        return items;
    }

    public static ArrayList<FamilyStatusItem> getFamilyStatusList(Context context) {
        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        SQLiteDatabase mDatabase = helpers.createOrOpenDatabase();
        ArrayList<FamilyStatusItem> items = new ArrayList<>();
        String query = "Select * from "
                + AppConstant.MEMBER_STATUS + " WHERE status_type='H'";

        Cursor cur = mDatabase.rawQuery(query, null);
        Log.d("SECC Data ", " Query : " + cur.getCount());
        if (cur != null) {
            if (cur.getCount() > 0) {
                cur.moveToFirst();
                while (cur.isAfterLast() == false) {
                    FamilyStatusItem item = new FamilyStatusItem();
                    item.setSno(cur.getString(cur.getColumnIndex("sno")));
                    item.setStatusCode(cur.getString(cur.getColumnIndex("status_code")));
                    item.setStatusType(cur.getString(cur.getColumnIndex("status_type")));
                    item.setStatusDesc(cur.getString(cur.getColumnIndex("status_desc")));
                    items.add(item);
                    cur.moveToNext();
                }
            }
            cur.close();

        }
        mDatabase.close();
        helpers.close();
        Collections.sort(items, new Comparator<FamilyStatusItem>() {
            @Override
            public int compare(FamilyStatusItem o1, FamilyStatusItem o2) {
                return Integer.parseInt(o1.getSno()) - Integer.parseInt(o2.getSno());
                      /*  if(status)
                        return 1;
                        else
                            return 0;*/

                //return Integer.compare(o1.getAge(), o2.getId());
            }
        });

        return items;
    }

    public static ArrayList<AadhaarStatusItem> getAadhaarStatusList(Context context) {
        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        SQLiteDatabase mDatabase = helpers.createOrOpenDatabase();
        ArrayList<AadhaarStatusItem> items = new ArrayList<>();
        String query = "Select * from "
                + AppConstant.AADHAAR_STATUS;

        Cursor cur = mDatabase.rawQuery(query, null);
        Log.d("SECC Data ", " Query : " + cur.getCount());
        if (cur != null) {
            if (cur.getCount() > 0) {
                cur.moveToFirst();
                while (cur.isAfterLast() == false) {
                    AadhaarStatusItem item = new AadhaarStatusItem();
                    item.setaStatusCode(cur.getString(cur.getColumnIndex("a_status_code")));
                    item.setaStatusDesc(cur.getString(cur.getColumnIndex("a_status_desc")));
                    items.add(item);
                    cur.moveToNext();
                }
            }
            cur.close();

        }
        mDatabase.close();
        helpers.close();
        return items;
    }

    public static HealthSchemeItem getHealthSchemeDetail(Context context, String stateCode, String schemeId) {
        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        SQLiteDatabase mDatabase = helpers.createOrOpenDatabase();
        HealthSchemeItem item = null;
        ArrayList<HealthSchemeItem> items = new ArrayList<>();
        String query = "Select * from "
                + AppConstant.HEALTH_SCHEME + " where statecode='" + stateCode + "' AND scheme_id='" + schemeId + "'";

        Cursor cur = mDatabase.rawQuery(query, null);
        Log.d("SECC Data ", " Query : " + cur.getCount());
        if (cur != null) {
            if (cur.getCount() > 0) {
                cur.moveToFirst();
                while (cur.isAfterLast() == false) {
                    item = new HealthSchemeItem();
                    item.setStatecode(cur.getString(cur.getColumnIndex("statecode")));
                    item.setSchemeId(cur.getString(cur.getColumnIndex("scheme_id")));
                    item.setSchemeName(cur.getString(cur.getColumnIndex("scheme_name")));
                    cur.moveToNext();
                }
            }
            cur.close();

        }
        mDatabase.close();
        helpers.close();
        return item;
    }

    public static ArrayList<HealthSchemeItem> getHealthSchemeList(Context context, String stateCode) {
        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        SQLiteDatabase mDatabase = helpers.createOrOpenDatabase();
        ArrayList<HealthSchemeItem> items = new ArrayList<>();
        String query = "Select * from "
                + AppConstant.HEALTH_SCHEME + " where statecode='" + stateCode + "'";

        Cursor cur = mDatabase.rawQuery(query, null);
        Log.d("SECC Data ", " Query : " + cur.getCount());
        if (cur != null) {
            if (cur.getCount() > 0) {
                cur.moveToFirst();
                while (cur.isAfterLast() == false) {
                    HealthSchemeItem item = new HealthSchemeItem();
                    item.setStatecode(cur.getString(cur.getColumnIndex("statecode")));
                    item.setSchemeId(cur.getString(cur.getColumnIndex("scheme_id")));
                    item.setSchemeName(cur.getString(cur.getColumnIndex("scheme_name")));
                    items.add(item);
                    cur.moveToNext();
                }
            }
            cur.close();

        }
        mDatabase.close();
        helpers.close();
        return items;
    }

    public static long houseHoldCount(Context context) {
        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        SQLiteDatabase mDatabase = helpers.createOrOpenDatabase();
        long cnt = DatabaseUtils.queryNumEntries(mDatabase, AppConstant.HOUSE_HOLD_SECC);
        mDatabase.close();
        return cnt;
    }

    public static long seccMemberCount(Context context) {
        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        SQLiteDatabase mDatabase = helpers.createOrOpenDatabase();
        long cnt = DatabaseUtils.queryNumEntries(mDatabase, AppConstant.TRANSECT_POPULATE_SECC);
        mDatabase.close();
        return cnt;
    }

    public static ArrayList<RelationItem> getRelationListByGender(Context context, String gender) {

        String HOD_GENDER_ID = ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SELECTED_HEAD_GENDER_ID, context);
        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        SQLiteDatabase mDatabase = helpers.createOrOpenDatabase();
        ArrayList<RelationItem> relationList = new ArrayList<>();
        String query = null;
        if (gender != null) {
            query = "Select * from "
                    + AppConstant.RELATION_TABLE + " where relation_gender='" + gender + "' OR relation_gender='O'  OR relation_gender='O'";
        } else {
            query = "Select * from "
                    + AppConstant.RELATION_TABLE;
        }
        Cursor cur = mDatabase.rawQuery(query, null);
        Log.d("SECC Data ", " Query : " + query + " count :" + cur.getCount());
        if (cur != null) {
            if (cur.getCount() > 0) {
                cur.moveToFirst();
                while (cur.isAfterLast() == false) {
                    RelationItem item = new RelationItem();
                    String Rel_code = cur.getString(cur.getColumnIndex("relation_code"));
                    System.out.println("RELCODE" + Rel_code);
                    try {

//                        if (gender != null && gender.equalsIgnoreCase("M") && Rel_code.equalsIgnoreCase("98")) {
////                            if(HOD_GENDER_ID.equalsIgnoreCase(""))
//                            cur.moveToNext();
//                        } else if (gender != null && gender.equalsIgnoreCase("F") && Rel_code.equalsIgnoreCase("99")) {
//                            cur.moveToNext();
//
//                        }else
                            if (HOD_GENDER_ID != null && HOD_GENDER_ID.equalsIgnoreCase("1") && Rel_code.equalsIgnoreCase("98")&&gender.equalsIgnoreCase("M")) {
//                            if(HOD_GENDER_ID.equalsIgnoreCase(""))
                            cur.moveToNext();
                        } else if (HOD_GENDER_ID != null && HOD_GENDER_ID.equalsIgnoreCase("2") && Rel_code.equalsIgnoreCase("99") && gender.equalsIgnoreCase("F")) {
                            cur.moveToNext();

                        } else {
                            item.setRelationCode(cur.getString(cur.getColumnIndex("relation_code")));
                            item.setRelationName(cur.getString(cur.getColumnIndex("relation_name")));
                            item.setActiveStatus(cur.getString(cur.getColumnIndex("active_status")));
                            item.setRelationGender(cur.getString(cur.getColumnIndex("relation_gender")));
                            item.setDisplayOrder(cur.getString(cur.getColumnIndex("display_order")));
                            relationList.add(item);
                            cur.moveToNext();

                        }

                    } catch (Exception error) {
                        error.printStackTrace();
                        item.setRelationCode(cur.getString(cur.getColumnIndex("relation_code")));
                        item.setRelationName(cur.getString(cur.getColumnIndex("relation_name")));
                        item.setActiveStatus(cur.getString(cur.getColumnIndex("active_status")));
                        item.setRelationGender(cur.getString(cur.getColumnIndex("relation_gender")));
                        item.setDisplayOrder(cur.getString(cur.getColumnIndex("display_order")));
                        relationList.add(item);
                        cur.moveToNext();
                    }
                }
            }
            cur.close();

        }
        mDatabase.close();
        helpers.close();
        return relationList;
    }


    public static ArrayList<RelationItem> getRelationListByGenderForNominee(Context context, String gender) {

        String SELECTED_RESP_GENDER_ID = ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SELECTED_MEMBER_GENDER_ID, context);
        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        SQLiteDatabase mDatabase = helpers.createOrOpenDatabase();
        ArrayList<RelationItem> relationList = new ArrayList<>();
        String query = null;
        if (gender != null) {
            query = "Select * from "
                    + AppConstant.RELATION_TABLE + " where relation_gender='" + gender + "' OR relation_gender='O'  OR relation_gender='O'";
        } else {
            query = "Select * from "
                    + AppConstant.RELATION_TABLE;
        }
        Cursor cur = mDatabase.rawQuery(query, null);
        Log.d("SECC Data ", " Query : " + query + " count :" + cur.getCount());
        if (cur != null) {
            if (cur.getCount() > 0) {
                cur.moveToFirst();
                while (cur.isAfterLast() == false) {
                    RelationItem item = new RelationItem();
                    String Rel_code = cur.getString(cur.getColumnIndex("relation_code"));
                    System.out.println("RELCODE" + Rel_code);
                    try {

                        if (gender != null && gender.equalsIgnoreCase("M") &&SELECTED_RESP_GENDER_ID !=null && SELECTED_RESP_GENDER_ID.equalsIgnoreCase("1")&& Rel_code.equalsIgnoreCase("98")) {
//                            if(HOD_GENDER_ID.equalsIgnoreCase(""))
                            cur.moveToNext();
                        } else if (gender != null && gender.equalsIgnoreCase("F")&&SELECTED_RESP_GENDER_ID !=null && SELECTED_RESP_GENDER_ID.equalsIgnoreCase("2") && Rel_code.equalsIgnoreCase("99")) {
                            cur.moveToNext();

                        }

//                        else
//                            if (HOD_GENDER_ID != null && HOD_GENDER_ID.equalsIgnoreCase("1") && Rel_code.equalsIgnoreCase("98")&&gender.equalsIgnoreCase("M")) {
////                            if(HOD_GENDER_ID.equalsIgnoreCase(""))
//                            cur.moveToNext();
//                        } else if (HOD_GENDER_ID != null && HOD_GENDER_ID.equalsIgnoreCase("2") && Rel_code.equalsIgnoreCase("99") && gender.equalsIgnoreCase("F")) {
//                            cur.moveToNext();
//
//                        }

                        else {
                            item.setRelationCode(cur.getString(cur.getColumnIndex("relation_code")));
                            item.setRelationName(cur.getString(cur.getColumnIndex("relation_name")));
                            item.setActiveStatus(cur.getString(cur.getColumnIndex("active_status")));
                            item.setRelationGender(cur.getString(cur.getColumnIndex("relation_gender")));
                            item.setDisplayOrder(cur.getString(cur.getColumnIndex("display_order")));
                            relationList.add(item);
                            cur.moveToNext();

                        }

                    } catch (Exception error) {
                        error.printStackTrace();
                        item.setRelationCode(cur.getString(cur.getColumnIndex("relation_code")));
                        item.setRelationName(cur.getString(cur.getColumnIndex("relation_name")));
                        item.setActiveStatus(cur.getString(cur.getColumnIndex("active_status")));
                        item.setRelationGender(cur.getString(cur.getColumnIndex("relation_gender")));
                        item.setDisplayOrder(cur.getString(cur.getColumnIndex("display_order")));
                        relationList.add(item);
                        cur.moveToNext();
                    }
                }
            }
            cur.close();

        }
        mDatabase.close();
        helpers.close();
        return relationList;
    }

    public static ArrayList<RelationItem> getRelationList(Context context) {
        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        SQLiteDatabase mDatabase = helpers.createOrOpenDatabase();
        ArrayList<RelationItem> relationList = new ArrayList<>();
        String query = "Select * from "
                + AppConstant.RELATION_TABLE;

        Cursor cur = mDatabase.rawQuery(query, null);
        Log.d("SECC Data ", " Query : " + query + " count :" + cur.getCount());
        if (cur != null) {
            if (cur.getCount() > 0) {
                cur.moveToFirst();
                while (cur.isAfterLast() == false) {
                    RelationItem item = new RelationItem();
                    item.setRelationCode(cur.getString(cur.getColumnIndex("relation_code")));
                    item.setRelationName(cur.getString(cur.getColumnIndex("relation_name")));
                    item.setActiveStatus(cur.getString(cur.getColumnIndex("active_status")));
                    item.setRelationGender(cur.getString(cur.getColumnIndex("relation_gender")));
                    item.setDisplayOrder(cur.getString(cur.getColumnIndex("display_order")));
                    relationList.add(item);
                    cur.moveToNext();
                }
            }
            cur.close();

        }
        mDatabase.close();
        helpers.close();
        return relationList;
    }

    public static long countSyncedHousehold(Context context, String stateCode, String distCode, String tehsilCode, String vtCode, String wardCode, String blockCode, String syncedStatus) {
        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        SQLiteDatabase mDatabase = helpers.createOrOpenDatabase();
        /*String query="Select * from " ahlsubblockno
                + AppConstant.HOUSE_HOLD_SECC+" WHERE synced_status='"+syncedStatus+"'";*/
        String query = "Select * from "
                + AppConstant.HOUSE_HOLD_SECC
                + " where statecode='"
                + stateCode
                + "' AND districtcode='" +
                distCode
                + "' AND tehsilcode='" + tehsilCode
                + "' AND towncode='" + vtCode + "' AND wardid='" + wardCode
                + "' AND ahlblockno ='" + blockCode + "' AND synced_status='" + syncedStatus + "'";
        Cursor cur = mDatabase.rawQuery(query, null);
        //  long cnt  = DatabaseUtils.queryNumEntries(mDatabase,AppConstant.HOUSE_HOLD_SECC,"");
        long cnt = cur.getCount();
        mDatabase.close();
        return cnt;
    }

    public static long countSyncedHouseholdSubEb(Context context, String stateCode, String distCode, String tehsilCode,
                                                 String vtCode, String wardCode, String blockCode, String subBlockCode, String syncedStatus) {
        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        SQLiteDatabase mDatabase = helpers.createOrOpenDatabase();
        /*String query="Select * from " ahlsubblockno
                + AppConstant.HOUSE_HOLD_SECC+" WHERE synced_status='"+syncedStatus+"'";*/
        String query = "Select * from "
                + AppConstant.HOUSE_HOLD_SECC
                + " where statecode='"
                + stateCode
                + "' AND districtcode='" +
                distCode
                + "' AND tehsilcode='" + tehsilCode
                + "' AND towncode='" + vtCode + "' AND wardid='" + wardCode
                + "' AND ahlblockno ='" + blockCode + "' AND ahlsubblockno ='" + subBlockCode + "' AND synced_status='" + syncedStatus + "'";
        Cursor cur = mDatabase.rawQuery(query, null);
        //  long cnt  = DatabaseUtils.queryNumEntries(mDatabase,AppConstant.HOUSE_HOLD_SECC,"");
        long cnt = cur.getCount();
        mDatabase.close();
        return cnt;
    }

    public static long countSyncedHouseholdVillage(Context context, String stateCode, String distCode, String tehsilCode, String vtCode, String syncedStatus) {
        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        SQLiteDatabase mDatabase = helpers.createOrOpenDatabase();
        /*String query="Select * from " ahlsubblockno
                + AppConstant.HOUSE_HOLD_SECC+" WHERE synced_status='"+syncedStatus+"'";*/
        String query = "Select * from "
                + AppConstant.HOUSE_HOLD_SECC
                + " where statecode='"
                + stateCode
                + "' AND districtcode='" +
                distCode
                + "' AND tehsilcode='" + tehsilCode
                + "' AND towncode='" + vtCode + "' AND synced_status='" + syncedStatus + "'";
        Cursor cur = mDatabase.rawQuery(query, null);
        //  long cnt  = DatabaseUtils.queryNumEntries(mDatabase,AppConstant.HOUSE_HOLD_SECC,"");
        long cnt = cur.getCount();
        mDatabase.close();
        return cnt;
    }

    public static long countSyncedMember(Context context, String stateCode, String distCode, String tehsilCode, String vtCode, String wardCode, String blockCode, String syncedStatus) {
        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        SQLiteDatabase mDatabase = helpers.createOrOpenDatabase();
       /* String query="Select * from "
                + AppConstant.TRANSECT_POPULATE_SECC+" WHERE synced_status='"+syncedStatus+"'";*/

        String query = "Select * from "
                + AppConstant.TRANSECT_POPULATE_SECC
                + " where statecode='"
                + stateCode
                + "' AND districtcode='" +
                distCode
                + "' AND tehsilcode='" + tehsilCode
                + "' AND towncode='" + vtCode + "' AND wardid='" + wardCode
                + "' AND blockno ='" + blockCode + "' AND synced_status='" + syncedStatus + "'";

        Cursor cur = mDatabase.rawQuery(query, null);
        //  long cnt  = DatabaseUtils.queryNumEntries(mDatabase,AppConstant.HOUSE_HOLD_SECC,"");
        long cnt = cur.getCount();
        mDatabase.close();
        return cnt;
    }

    public static long countSyncedMemberVillage(Context context, String stateCode, String distCode, String tehsilCode, String vtCode, String syncedStatus) {
        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        SQLiteDatabase mDatabase = helpers.createOrOpenDatabase();
       /* String query="Select * from "
                + AppConstant.TRANSECT_POPULATE_SECC+" WHERE synced_status='"+syncedStatus+"'";*/

        String query = "Select * from "
                + AppConstant.TRANSECT_POPULATE_SECC
                + " where statecode='"
                + stateCode
                + "' AND districtcode='" +
                distCode
                + "' AND tehsilcode='" + tehsilCode
                + "' AND towncode='" + vtCode + "' AND synced_status='" + syncedStatus + "'";

        Cursor cur = mDatabase.rawQuery(query, null);
        //  long cnt  = DatabaseUtils.queryNumEntries(mDatabase,AppConstant.HOUSE_HOLD_SECC,"");
        long cnt = cur.getCount();
        mDatabase.close();
        return cnt;
    }

    public static long countSurveyedHousehold(Context context, VerifierLocationItem item, String syncedStatus, String lockedStatus) {
        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        SQLiteDatabase mDatabase = helpers.createOrOpenDatabase();
        int surveyedCount = 0;
        String query;
        if (item != null) {
            query = "Select synced_status,locked_save from "
                    + AppConstant.HOUSE_HOLD_SECC + " where statecode='"
                    + item.getStateCode()
                    + "' AND districtcode='" +
                    item.getDistrictCode()
                    + "' AND tehsilcode='" + item.getTehsilCode()
                    + "' AND towncode='" + item.getVtCode() + "' AND wardid='" + item.getWardCode()
                    + "' AND ahlblockno ='" + item.getBlockCode() + "'";
        } else {
            query = "Select synced_status,locked_save from "
                    + AppConstant.HOUSE_HOLD_SECC;
        }
        Log.d("Secc Database", " Query count surveyed : " + query);
        Cursor cur = mDatabase.rawQuery(query, null);
        if (cur != null) {
            if (cur.getCount() > 0) {
                cur.moveToFirst();
                while (cur.isAfterLast() == false) {

                    String saveLocked = cur.getString(cur.getColumnIndex("locked_save"));
                    String syncStatus = cur.getString(cur.getColumnIndex("synced_status"));
                    if (saveLocked != null && saveLocked.equalsIgnoreCase(AppConstant.LOCKED + "") && syncStatus != null && syncStatus.equalsIgnoreCase(AppConstant.SYNCED_STATUS)) {

                    } else if (saveLocked != null && saveLocked.equalsIgnoreCase(AppConstant.LOCKED + "")) {
                        surveyedCount++;
                    }

                    cur.moveToNext();
                }
            }
            cur.close();
        }
        mDatabase.close();
        helpers.close();
        return surveyedCount;
    }

    public static long countUnderSurveyedHousehold(Context context, VerifierLocationItem item, String syncedStatus, String lockStatus) {
        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        SQLiteDatabase mDatabase = helpers.createOrOpenDatabase();
        // Cursor findNormalItems = mDatabase.query(AppConstant.HOUSE_HOLD_SECC,new String[] { "ahl_tin" }, "type != ?",new String[] { "onSale" });
        int underSurveyedCount = 0;
        String query;
        if (item == null) {
            query = "Select locked_save from "
                    + AppConstant.HOUSE_HOLD_SECC;

        } else {
            query = "Select locked_save from  "
                    + AppConstant.HOUSE_HOLD_SECC
                    + " where statecode='"
                    + item.getStateCode()
                    + "' AND districtcode='" +
                    item.getDistrictCode()
                    + "' AND tehsilcode='" + item.getTehsilCode()
                    + "' AND towncode='" + item.getVtCode() + "' AND wardid='" + item.getWardCode()
                    + "' AND ahlblockno ='" + item.getBlockCode() + "'";
        }
        Log.d("Secc Database", " Query count surveyed : " + query);
        Cursor cur = mDatabase.rawQuery(query, null);
        if (cur != null) {
            if (cur.getCount() > 0) {
                cur.moveToFirst();
                while (cur.isAfterLast() == false) {

                    String saveLocked = cur.getString(cur.getColumnIndex("locked_save"));
                    if (saveLocked != null && saveLocked.equalsIgnoreCase(AppConstant.LOCKED + "")) {

                    } else if (saveLocked != null && saveLocked.equalsIgnoreCase(AppConstant.SAVE + "")) {
                        underSurveyedCount++;
                    } else if (saveLocked != null && saveLocked.equalsIgnoreCase("")) {
                        underSurveyedCount++;
                    } else if (saveLocked == null) {
                        underSurveyedCount++;
                    }

                    cur.moveToNext();
                }
            }
            cur.close();
        }
        mDatabase.close();
        helpers.close();
        return underSurveyedCount;
    }

    public static void deleteTable(Context context, String tableName) {

        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        SQLiteDatabase mDatabase = helpers.createOrOpenDatabase();
        mDatabase.execSQL("delete from " + tableName);
        mDatabase.close();
    }

    public static boolean checkUnderSurveyMember(Context context, HouseHoldItem item) {
        boolean flag = false;
        ArrayList<SeccMemberItem> memberList = new ArrayList<>();
        if (item != null && item.getDataSource() != null && item.getDataSource().trim().equalsIgnoreCase(AppConstant.RSBY_SOURCE)) {
            memberList = getRsbyMemberListWithUrn(item.getRsbyUrnId(), context);
            for (SeccMemberItem item1 : memberList) {
                if (item1.getLockedSave() == null) {
                    flag = true;
                    break;
                }
                if (item1.getLockedSave().equalsIgnoreCase("")) {
                    flag = true;
                    break;
                }
                if (item1.getLockedSave() != null && item1.getLockedSave().equalsIgnoreCase(AppConstant.SAVE + "")) {
                    flag = true;
                    break;
                }
            }
        } else {
            memberList = getSeccMemberList(item.getHhdNo(), context);
            for (SeccMemberItem item1 : memberList) {
                if (item1.getLockedSave() == null) {
                    flag = true;
                    break;
                }
                if (item1.getLockedSave().equalsIgnoreCase("")) {
                    flag = true;
                    break;
                }
                if (item1.getLockedSave() != null && item1.getLockedSave().equalsIgnoreCase(AppConstant.SAVE + "")) {
                    flag = true;
                    break;
                }
            }
        }

        return flag;
    }

    public static boolean checkRsbyUnderSurveyMember(String Urn_no, Context context) {
        ArrayList<RSBYItem> memberList = getRsbyMemberList(Urn_no, context);
        boolean flag = false;
        for (RSBYItem item : memberList) {
            if (item.getLockedSave() == null) {
                flag = true;
                break;
            }
            if (item.getLockedSave().equalsIgnoreCase("")) {
                flag = true;
                break;
            }
            if (item.getLockedSave() != null && item.getLockedSave().equalsIgnoreCase(AppConstant.SAVE + "")) {
                flag = true;
                break;
            }
        }

        return flag;
    }

    public static long countUnderSurvayedMember(String saveLocked, Context context) {
        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        SQLiteDatabase mDatabase = helpers.createOrOpenDatabase();
        String query = "Select * from "
                + AppConstant.TRANSECT_POPULATE_SECC + " WHERE locked_save='" + saveLocked + "'";
        Cursor cur = mDatabase.rawQuery(query, null);
        //  long cnt  = DatabaseUtils.queryNumEntries(mDatabase,AppConstant.HOUSE_HOLD_SECC,"");
        long cnt = cur.getCount();
        mDatabase.close();
        return cnt;
    }

    public static long countUnderSurvayedMember(Context context, String stateCode, String distCode, String tehsilCode,
                                                String vtCode, String wardCode, String blockCode, String saveLocked) {
        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        SQLiteDatabase mDatabase = helpers.createOrOpenDatabase();
        String query = "Select * from "
                + AppConstant.TRANSECT_POPULATE_SECC
                + " where statecode='"
                + stateCode
                + "' AND districtcode='" +
                distCode
                + "' AND tehsilcode='" + tehsilCode
                + "' AND towncode='" + vtCode + "' AND wardid='" + wardCode
                + "' AND blockno ='" + blockCode + "' AND locked_save='" + saveLocked + "'";
       /* String query="Select * from "
                + AppConstant.TRANSECT_POPULATE_SECC+" WHERE locked_save='"+saveLocked+"'";*/
        Cursor cur = mDatabase.rawQuery(query, null);
        //  long cnt  = DatabaseUtils.queryNumEntries(mDatabase,AppConstant.HOUSE_HOLD_SECC,"");
        long cnt = cur.getCount();
        mDatabase.close();
        return cnt;
    }

    public static long houseHoldCount(Context context,
                                      String stateCode, String distCode, String tehsilCode, String vtCode, String wardCode, String blockCode) {
        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        SQLiteDatabase mDatabase = helpers.createOrOpenDatabase();
        // long cnt  = DatabaseUtils.queryNumEntries(mDatabase,AppConstant.HOUSE_HOLD_SECC);
        long count = 0;
        String query = "Select * from "
                + AppConstant.HOUSE_HOLD_SECC
                + " where statecode='"
                + stateCode
                + "' AND districtcode='" +
                distCode
                + "' AND tehsilcode='" + tehsilCode
                + "' AND towncode='" + vtCode + "' AND wardid='" + wardCode
                + "' AND ahlblockno ='" + blockCode + "'";


        Cursor cur = mDatabase.rawQuery(query, null);
        count = cur.getCount();
        mDatabase.close();
        return count;
    }

    public static long houseHoldCountSubEbWise(Context context,
                                               String stateCode, String distCode, String tehsilCode, String vtCode, String wardCode, String blockCode, String subBlockCode) {
        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        SQLiteDatabase mDatabase = helpers.createOrOpenDatabase();
        // long cnt  = DatabaseUtils.queryNumEntries(mDatabase,AppConstant.HOUSE_HOLD_SECC);
        long count = 0;
        String query = "Select * from "
                + AppConstant.HOUSE_HOLD_SECC
                + " where statecode='"
                + stateCode
                + "' AND districtcode='" +
                distCode
                + "' AND tehsilcode='" + tehsilCode
                + "' AND towncode='" + vtCode + "' AND wardid='" + wardCode
                + "' AND ahlblockno ='" + blockCode + "' AND ahlsubblockno ='" + subBlockCode + "'";


        Cursor cur = mDatabase.rawQuery(query, null);
        count = cur.getCount();
        mDatabase.close();
        return count;
    }

    public static long houseHoldVillageCount(Context context,
                                             String stateCode, String distCode, String tehsilCode, String vtCode) {
        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        SQLiteDatabase mDatabase = helpers.createOrOpenDatabase();
        // long cnt  = DatabaseUtils.queryNumEntries(mDatabase,AppConstant.HOUSE_HOLD_SECC);
        long count = 0;
        String query = "Select * from "
                + AppConstant.HOUSE_HOLD_SECC
                + " where statecode='"
                + stateCode
                + "' AND districtcode='" +
                distCode
                + "' AND tehsilcode='" + tehsilCode
                + "' AND towncode='" + vtCode + "'";


        Cursor cur = mDatabase.rawQuery(query, null);
        count = cur.getCount();
        mDatabase.close();
        return count;
    }

    public static long VillageHouseholdCountCustom(Context context,
                                                   String stateCode, String distCode, String tehsilCode, String vtCode, String dataDource) {
        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        SQLiteDatabase mDatabase = helpers.createOrOpenDatabase();
        // long cnt  = DatabaseUtils.queryNumEntries(mDatabase,AppConstant.HOUSE_HOLD_SECC);
        long count = 0;
        String query = "Select * from "
                + AppConstant.HOUSE_HOLD_SECC
                + " where statecode='"
                + stateCode
            /*    + "' AND districtcode='" +
                distCode*/
                + "' AND data_source='" +
                dataDource
               /* + "' AND tehsilcode='" + tehsilCode
                + "' AND towncode='" + vtCode*/ + "'";


        Cursor cur = mDatabase.rawQuery(query, null);
        count = cur.getCount();
        mDatabase.close();
        return count;
    }

    public static long seccHouseHoldCount(Context context,
                                          String stateCode, String distCode, String tehsilCode, String vtCode, String wardCode, String blockCode) {
        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        SQLiteDatabase mDatabase = helpers.createOrOpenDatabase();
        // long cnt  = DatabaseUtils.queryNumEntries(mDatabase,AppConstant.HOUSE_HOLD_SECC);
        long count = 0;
        String query = "Select * from "
                + AppConstant.HOUSE_HOLD_SECC
                + " where statecode='"
                + stateCode
                + "' AND districtcode='" +
                distCode
                + "' AND data_source='" +
                AppConstant.SECC_SOURCE
                + "' AND tehsilcode='" + tehsilCode
                + "' AND towncode='" + vtCode + "' AND wardid='" + wardCode
                + "' AND ahlblockno ='" + blockCode + "'";


        Cursor cur = mDatabase.rawQuery(query, null);
        count = cur.getCount();
        mDatabase.close();
        return count;
    }

    public static long seccHouseHoldVillageCount(Context context,
                                                 String stateCode, String distCode, String tehsilCode, String vtCode) {
        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        SQLiteDatabase mDatabase = helpers.createOrOpenDatabase();
        // long cnt  = DatabaseUtils.queryNumEntries(mDatabase,AppConstant.HOUSE_HOLD_SECC);
        long count = 0;
        String query = "Select * from "
                + AppConstant.HOUSE_HOLD_SECC
                + " where statecode='"
                + stateCode
                + "' AND districtcode='" +
                distCode
                + "' AND data_source='" +
                AppConstant.SECC_SOURCE
                + "' AND tehsilcode='" + tehsilCode
                + "' AND towncode='" + vtCode + "'";


        Cursor cur = mDatabase.rawQuery(query, null);
        count = cur.getCount();
        mDatabase.close();
        return count;
    }

    public static long rsbyHouseHoldCount(Context context,
                                          String stateCode, String distCode, String tehsilCode, String vtCode, String wardCode, String blockCode) {
        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        SQLiteDatabase mDatabase = helpers.createOrOpenDatabase();
        // long cnt  = DatabaseUtils.queryNumEntries(mDatabase,AppConstant.HOUSE_HOLD_SECC);
        long count = 0;
        String query = "Select * from "
                + AppConstant.HOUSE_HOLD_SECC
                + " where statecode='"
                + stateCode
                + "' AND districtcode='" +
                distCode
                + "' AND data_source='" +
                AppConstant.RSBY_SOURCE
                + "' AND tehsilcode='" + tehsilCode
                + "' AND towncode='" + vtCode + "' AND wardid='" + wardCode
                + "' AND ahlblockno ='" + blockCode + "'";


        Cursor cur = mDatabase.rawQuery(query, null);
        count = cur.getCount();
        mDatabase.close();
        return count;
    }

    public static long rsbyHouseHoldVillageCount(Context context,
                                                 String stateCode, String distCode, String tehsilCode, String vtCode) {
        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        SQLiteDatabase mDatabase = helpers.createOrOpenDatabase();
        // long cnt  = DatabaseUtils.queryNumEntries(mDatabase,AppConstant.HOUSE_HOLD_SECC);
        long count = 0;
        String query = "Select * from "
                + AppConstant.HOUSE_HOLD_SECC
                + " where statecode='"
                + stateCode
                + "' AND districtcode='" +
                distCode
                + "' AND data_source='" +
                AppConstant.RSBY_SOURCE
                + "' AND tehsilcode='" + tehsilCode
                + "' AND towncode='" + vtCode + "'";


        Cursor cur = mDatabase.rawQuery(query, null);
        count = cur.getCount();
        mDatabase.close();
        return count;
    }

    public static long seccMemberCount(Context context,
                                       String stateCode, String distCode, String tehsilCode, String vtCode, String wardCode, String blockCode) {
        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        SQLiteDatabase mDatabase = helpers.createOrOpenDatabase();
        // long cnt  = DatabaseUtils.queryNumEntries(mDatabase,AppConstant.HOUSE_HOLD_SECC);
        long count = 0;
        String query = "Select * from "
                + AppConstant.TRANSECT_POPULATE_SECC
                + " where statecode='"
                + stateCode
                + "' AND districtcode='" +
                distCode
                + "' AND tehsilcode='" + tehsilCode
                + "' AND towncode='" + vtCode + "' AND wardid='" + wardCode
                + "' AND blockno ='" + blockCode + "'";

        Cursor cur = mDatabase.rawQuery(query, null);
        count = cur.getCount();
        mDatabase.close();
        return count;
    }

    public static long seccMemberVillageCount(Context context,
                                              String stateCode, String distCode, String tehsilCode, String vtCode) {
        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        SQLiteDatabase mDatabase = helpers.createOrOpenDatabase();
        // long cnt  = DatabaseUtils.queryNumEntries(mDatabase,AppConstant.HOUSE_HOLD_SECC);
        long count = 0;
        String query = "Select * from "
                + AppConstant.TRANSECT_POPULATE_SECC
                + " where statecode='"
                + stateCode
                + "' AND districtcode='" +
                distCode
                + "' AND tehsilcode='" + tehsilCode
                + "' AND towncode='" + vtCode + "'";

        Cursor cur = mDatabase.rawQuery(query, null);
        count = cur.getCount();
        mDatabase.close();
        return count;
    }

    public static long VillageMemberCountCustom(Context context,
                                                String stateCode, String distCode, String tehsilCode, String vtCode, String dataSource) {
        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        SQLiteDatabase mDatabase = helpers.createOrOpenDatabase();
        // long cnt  = DatabaseUtils.queryNumEntries(mDatabase,AppConstant.HOUSE_HOLD_SECC);
        long count = 0;
        String query = "Select * from "
                + AppConstant.TRANSECT_POPULATE_SECC
                + " where statecode='"
                + stateCode
               /* + "' AND districtcode='" +
                distCode*/
                + "' AND data_source='" +
                dataSource
                /*+ "' AND tehsilcode='" + tehsilCode
                + "' AND towncode='" + vtCode*/ + "'";

        Cursor cur = mDatabase.rawQuery(query, null);
        count = cur.getCount();
        mDatabase.close();
        return count;
    }

    public static RSBYItem getRsbyMemberDetail(String rsbyMemId, Context context) {
        String query = "Select * from "
                + AppConstant.RSBY_POPULATION_TABLE
                + " where rsbyMemId='" + rsbyMemId + "'";

        return CommonDatabase.getRsbyMember(context, query);
    }

    public static ArrayList<RSBYItem> getRsbyMemberList(String urn_no, Context context) {
        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        SQLiteDatabase mDatabase = helpers.createOrOpenDatabase();
        ArrayList<RSBYItem> seccMemberItems = new ArrayList<>();
     /*   String query="Select * from "
                + AppConstant.RSBY_POPULATION_TABLE
                + " where urn_no='"+urn_no+"'";*/
        String query = "Select * from "
                + AppConstant.RSBY_POPULATION_TABLE
                + " where urnId='" + urn_no + "'";

        Cursor cur = mDatabase.rawQuery(query, null);
        Log.d("SECC Data ", " Query : " + cur.getCount());

        return CommonDatabase.getRsbyMemberListNew(context, query);
        // return CommonDatabase.getSeccMemberList(context,query);
    }

    public static ArrayList<SeccMemberItem> getRsbyMemberListWithUrn(String urn_no, Context context) {
        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        SQLiteDatabase mDatabase = helpers.createOrOpenDatabase();
        ArrayList<RSBYItem> seccMemberItems = new ArrayList<>();
     /*   String query="Select * from "
                + AppConstant.RSBY_POPULATION_TABLE
                + " where urn_no='"+urn_no+"'";*/
        String query = "Select * from "
                + AppConstant.TRANSECT_POPULATE_SECC
                + " where rsby_urnId='" + urn_no + "'";

        Cursor cur = mDatabase.rawQuery(query, null);
        Log.d("SECC Data ", " Query : " + cur.getCount());

        //return CommonDatabase.getRsbyMemberListNew(context,query);

        return CommonDatabase.getSeccMemberList(context, query);
    }

    public static ArrayList<RSBYItem> rsbyMemberWithAadhaarLocked(String adhaarNo, String lockedStatus, Context context) {
        String query = "Select * from "
                + AppConstant.RSBY_POPULATION_TABLE
                + " where aadhaar_no='" + adhaarNo + "' AND locked_save='" + lockedStatus + "'";
        return CommonDatabase.getRsbyMemberListNew(context, query);
    }

    public static long updateRSBYHouseHold(RsbyHouseholdItem item, Context context) {
        return CommonDatabase.rsbyUpdateHousehold(context, item);
    }

    public static long updateRsbyMember(RSBYItem item, Context context) {


        return CommonDatabase.rsbyUpdatePopulation(item, context);
    }

    public static long saveRSBYMember(RSBYItem item, Context context) {
        long insertFlag = CommonDatabase.rsbySavePopulation(item, context);
        //Toast.makeText(context, "Insert Flag " +insertFlag, Toast.LENGTH_SHORT).show();
        return insertFlag;
    }

    public static long saveRSBYMember(SeccMemberItem item, Context context) {
        long insertFlag = CommonDatabase.save(item, context);
        //Toast.makeText(context, "Insert Flag " +insertFlag, Toast.LENGTH_SHORT).show();
        return insertFlag;

    }

    public static RsbyHouseholdItem getRsbyHouseHoldQ(String UrnID, Context context) {
        String query = "Select * from "
                + AppConstant.RSBY_HOUSEHOD_TABLE
                + " where urnId='" + UrnID + "'";

        return CommonDatabase.getRsbyHouseHold(context, query);
    }

    public static HouseHoldItem getRsbyHouseHoldByUrn(String UrnID, Context context) {
        String query = "Select * from "
                + AppConstant.HOUSE_HOLD_SECC
                + " where rsby_urnId='" + UrnID + "'";

        return CommonDatabase.getHouseholdDetail(context, query);
    }

    public static ArrayList<RsbyCardCategoryItem> getAllCategoryMaster(Context context) {
        String query = "Select * from "
                + AppConstant.RSBY_CARD_CAT_MASTER_TABLE;

        return CommonDatabase.getAllCardCategory(context, query);
    }

    public static ArrayList<RsbyPoliciesCompany> getAllPoliciesCompany(Context context) {
        String query = "Select * from "
                + AppConstant.RSBY_POLICY_COMPANY;

        return CommonDatabase.getInsuranceCompaniesName(context, query);
    }

    public static ArrayList<RsbyRelationItem> getRsbyMemberRelationCode(Context context) {
        String query = "Select * from "
                + AppConstant.RSBY_RELATION_TABLE;

        return CommonDatabase.getRsbyMemberRelationCode(context, query);
    }

    public static ArrayList<RsbyHouseholdItem> getAllRsbyHouseHoldList(Context context) {
        String query = "Select * from "
                + AppConstant.RSBY_HOUSEHOD_TABLE;

        AppUtility.showLog(AppConstant.LOG_STATUS, TAG, " Household Query : " + query);


        return CommonDatabase.getAllRsbyHouseHold(context, query);
    }

    public static ArrayList<NotificationModel> getAllAppNotification(Context context) {
        String query = "Select * from "
                + AppConstant.NOTIFICATION_TABLE;
        return CommonDatabase.getAllNotification(context, query);
    }

    public static ArrayList<ConfigurationItem> findConfiguration(String stateCode, Context context) {
        //  DatabaseHelpers helpers= DatabaseHelpers.getInstance(context);
        //   SQLiteDatabase db=helpers.getWritableDatabase();
        String query = "select * from " + AppConstant.new_application_configuration + " where state_code= '" + stateCode + "'";
        return CommonDatabase.configurationList(query, context);
    }

    public static ArrayList<ConfigurationItem> findAllConfiguration(Context context) {
    /*    DatabaseHelpers helpers= DatabaseHelpers.getInstance(context);
        SQLiteDatabase db=helpers.getWritableDatabase();*/
        String query = "select * from " + AppConstant.new_application_configuration;
        return CommonDatabase.configurationList(query, context);
    }

    public static long saveStateMaster(StateItem item, Context context) {
        return CommonDatabase.saveStateMasterData(item, context);
    }

    public static ArrayList<StateItem> findStateList(Context context) {
    /*    DatabaseHelpers helpers= DatabaseHelpers.getInstance(context);
        SQLiteDatabase db=helpers.getWritableDatabase();*/
        String query = "select * from " + AppConstant.m_state;
        return CommonDatabase.stateList(query, context);
    }

    public static long updateAppConf(ConfigurationItem item, Context context) {
        return CommonDatabase.updateAppConfig(item, context);
    }

    private ArrayList<HealthSchemeItem> getStateScheme(String stateCode, Context context) {
        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        SQLiteDatabase mDatabase = helpers.createOrOpenDatabase();
        ArrayList<HealthSchemeItem> schemeItems = new ArrayList<>();
        String query = "Select * from "
                + AppConstant.HEALTH_SCHEME
                + " where statecode='" + stateCode + "'";

        Cursor cur = mDatabase.rawQuery(query, null);
        Log.d("SECC Data ", " Query : " + cur.getCount());
        if (cur != null) {
            if (cur.getCount() > 0) {
                cur.moveToFirst();
                while (cur.isAfterLast() == false) {
                    HealthSchemeItem item = new HealthSchemeItem();
                    item.setSchemeName(cur.getString(cur.getColumnIndex("scheme_name")));
                    item.setSchemeId(cur.getString(cur.getColumnIndex("scheme_id")));
                    item.setStatecode(cur.getString(cur.getColumnIndex("statecode")));
                    schemeItems.add(item);
                    cur.moveToNext();
                }
            }
            cur.close();
        }
        mDatabase.close();
        helpers.close();
        return schemeItems;
    }


    public static DownloadedDataCountModel getDataCount(Context context) {
        String query = "Select * from "
                + AppConstant.DATA_COUNT_TABLE;

        return CommonDatabase.getDataCount(context, query);
    }


    public static long countSyncedHouseholdWard(Context context, String stateCode, String distCode,
                                                String tehsilCode, String vtCode, String wardCode, String syncedStatus) {
        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        SQLiteDatabase mDatabase = helpers.createOrOpenDatabase();
        /*String query="Select * from " ahlsubblockno
                + AppConstant.HOUSE_HOLD_SECC+" WHERE synced_status='"+syncedStatus+"'";*/
        String query = "Select * from "
                + AppConstant.HOUSE_HOLD_SECC
                + " where statecode='"
                + stateCode
                + "' AND districtcode='" +
                distCode
                + "' AND tehsilcode='" + tehsilCode
                + "' AND towncode='" + vtCode + "' AND wardid='" + wardCode + "' AND synced_status='" + syncedStatus + "'";
        Cursor cur = mDatabase.rawQuery(query, null);
        //  long cnt  = DatabaseUtils.queryNumEntries(mDatabase,AppConstant.HOUSE_HOLD_SECC,"");
        long cnt = cur.getCount();
        mDatabase.close();
        return cnt;
    }

    public static long countSyncedHouseholdEbWise(Context context, String stateCode, String distCode,
                                                  String tehsilCode, String vtCode, String wardCode,
                                                  String ebCode, String subEbCode, String syncedStatus) {
        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        SQLiteDatabase mDatabase = helpers.createOrOpenDatabase();
        /*String query="Select * from " ahlsubblockno
                + AppConstant.HOUSE_HOLD_SECC+" WHERE synced_status='"+syncedStatus+"'";*/
        String query = "Select * from "
                + AppConstant.HOUSE_HOLD_SECC
                + " where statecode='"
                + stateCode
                + "' AND districtcode='" +
                distCode
                + "' AND tehsilcode='" + tehsilCode
                + "' AND towncode='" + vtCode + "' AND wardid='" + wardCode + "' AND ahlblockno ='" + ebCode +
                "' AND ahlsubblockno ='" + subEbCode + "' AND synced_status='" + syncedStatus + "'";
        Cursor cur = mDatabase.rawQuery(query, null);
        //  long cnt  = DatabaseUtils.queryNumEntries(mDatabase,AppConstant.HOUSE_HOLD_SECC,"");
        long cnt = cur.getCount();
        mDatabase.close();
        return cnt;
    }

    public static long countSyncedMemberWard(Context context, String stateCode, String distCode, String tehsilCode, String vtCode,
                                             String wardCode, String syncedStatus) {
        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        SQLiteDatabase mDatabase = helpers.createOrOpenDatabase();
       /* String query="Select * from "
                + AppConstant.TRANSECT_POPULATE_SECC+" WHERE synced_status='"+syncedStatus+"'";*/

        String query = "Select * from "
                + AppConstant.TRANSECT_POPULATE_SECC
                + " where statecode='"
                + stateCode
                + "' AND districtcode='" +
                distCode
                + "' AND tehsilcode='" + tehsilCode
                + "' AND towncode='" + vtCode + "' AND wardid='" + wardCode + "' AND synced_status='" + syncedStatus + "'";
        //   + "' AND towncode='" + vtCode + "' AND synced_status='" + syncedStatus + "'";

        Cursor cur = mDatabase.rawQuery(query, null);
        //  long cnt  = DatabaseUtils.queryNumEntries(mDatabase,AppConstant.HOUSE_HOLD_SECC,"");
        long cnt = cur.getCount();
        mDatabase.close();
        return cnt;
    }

    public static long countSyncedMemberSubEb(Context context, String stateCode, String distCode, String tehsilCode, String vtCode,
                                              String wardCode, String ebCode, String subEbCode, String syncedStatus) {
        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        SQLiteDatabase mDatabase = helpers.createOrOpenDatabase();
       /* String query="Select * from "
                + AppConstant.TRANSECT_POPULATE_SECC+" WHERE synced_status='"+syncedStatus+"'";*/

        String query = "Select * from "
                + AppConstant.TRANSECT_POPULATE_SECC
                + " where statecode='"
                + stateCode
                + "' AND districtcode='" +
                distCode
                + "' AND tehsilcode='" + tehsilCode
                + "' AND towncode='" + vtCode + "' AND wardid='" + wardCode + "' AND ahlblockno ='" + ebCode +
                "' AND ahlsubblockno ='" + subEbCode + "' AND synced_status='" + syncedStatus + "'";
        //   + "' AND towncode='" + vtCode + "' AND synced_status='" + syncedStatus + "'";

        Cursor cur = mDatabase.rawQuery(query, null);
        //  long cnt  = DatabaseUtils.queryNumEntries(mDatabase,AppConstant.HOUSE_HOLD_SECC,"");
        long cnt = cur.getCount();
        mDatabase.close();
        return cnt;
    }

    public static ArrayList<SeccMemberItem> getSearchSeccMemberWardWiseList(String stateCode, String distCode, String tehsilCode,
                                                                            String villTownCode, String wardCode/*, String blockCode*/,
                                                                            Context context) {


        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        SQLiteDatabase mDatabase = helpers.createOrOpenDatabase();
        ArrayList<SeccMemberItem> seccMemberItems = new ArrayList<>();
        String query = "Select name,fathername,ahlslnohhd,name_sl,hh_status,mem_status,addressline1,addressline2,addressline3,addressline4,addressline5,hhd_no,data_source,rsby_rsbyMemId,rsby_urnId,rsby_name,rsby_gender from "
                + AppConstant.TRANSECT_POPULATE_SECC
                + " where statecode='"
                + stateCode
                + "' AND districtcode='" +
                distCode
                + "' AND tehsilcode='" + tehsilCode
                + "' AND towncode='" + villTownCode + "' AND wardid='" + wardCode
                + /*"' AND blockno ='" + blockCode +*/ "'";

        return CommonDatabase.getSearchSeccMemberList(context, query);
    }

    public static ArrayList<SeccMemberItem> getSearchSeccMemberSubEbWiseList(String stateCode, String distCode, String tehsilCode,
                                                                             String villTownCode, String wardCode, String ebCode, String subEbCode,
                                                                             Context context) {


        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        SQLiteDatabase mDatabase = helpers.createOrOpenDatabase();
        ArrayList<SeccMemberItem> seccMemberItems = new ArrayList<>();
        String query = "Select name,fathername,ahlslnohhd,name_sl,hh_status,mem_status,addressline1,addressline2,addressline3,addressline4,addressline5,hhd_no,data_source,rsby_rsbyMemId,rsby_urnId,rsby_name,rsby_gender from "
                + AppConstant.TRANSECT_POPULATE_SECC
                + " where statecode='"
                + stateCode
                + "' AND districtcode='" +
                distCode
                + "' AND tehsilcode='" + tehsilCode
                + "' AND towncode='" + villTownCode + "' AND wardid='" + wardCode

                + "' AND ahlblockno ='" + ebCode +
                "' AND ahlsubblockno ='" + subEbCode + "'";

        return CommonDatabase.getSearchSeccMemberList(context, query);
    }

    public static ArrayList<SeccMemberItem> getSeccMemberWardWiseList(String stateCode, String distCode, String tehsilCode,
                                                                      String villTownCode, String wardCode,
                                                                      Context context) {
        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        SQLiteDatabase mDatabase = helpers.createOrOpenDatabase();
        ArrayList<SeccMemberItem> seccMemberItems = new ArrayList<>();
        String query = "Select * from "
                + AppConstant.TRANSECT_POPULATE_SECC
                + " where statecode='"
                + stateCode
                + "' AND districtcode='" +
                distCode
                + "' AND tehsilcode='" + tehsilCode
                + "' AND towncode='" + villTownCode
                + "' AND wardid='" + wardCode + "'";

        return CommonDatabase.getSeccMemberList(context, query);
    }


    public static ArrayList<HouseHoldItem> getSeccHouseHoldWardWiseList(String stateCode, String distCode, String tehsilCode,
                                                                        String villTownCode, String wardCode, Context context) {
        String query = "Select * from "
                + AppConstant.HOUSE_HOLD_SECC
                + " where statecode='"
                + stateCode.trim()
                + "' AND districtcode='" +
                distCode.trim()
                + "' AND data_source='" +
                AppConstant.SECC_SOURCE
                + "' AND tehsilcode='" + tehsilCode.trim()
                + "' AND towncode='" + villTownCode.trim() + "' AND wardid='" + wardCode + "'";
        AppUtility.showLog(AppConstant.LOG_STATUS, TAG, " Household Query : " + query);


        return CommonDatabase.getHouseholdList(context, query);
    }

    public static ArrayList<HouseHoldItem> getAllHouseHoldWardWiseList(String stateCode, String distCode, String tehsilCode,
                                                                       String villTownCode, String wardCode, Context context) {
        String query = "Select * from "
                + AppConstant.HOUSE_HOLD_SECC
                + " where statecode='"
                + stateCode.trim()
                + "' AND districtcode='" +
                distCode.trim()
                + "'  AND tehsilcode='" + tehsilCode.trim()
                + "' AND towncode='" + villTownCode.trim() + "' AND wardid='" + wardCode + "'";
        AppUtility.showLog(AppConstant.LOG_STATUS, TAG, " Household Query : " + query);


        return CommonDatabase.getHouseholdList(context, query);
    }

    public static ArrayList<HouseHoldItem> getSeccHouseHoldSubEBWiseList(String stateCode, String distCode, String tehsilCode,
                                                                         String villTownCode, String wardCode, String
                                                                                 ebCode, String subEbCode, Context context) {
        String query = "Select * from "
                + AppConstant.HOUSE_HOLD_SECC
                + " where statecode='"
                + stateCode.trim()
                + "' AND districtcode='" +
                distCode.trim()
                + "' AND data_source='" +
                AppConstant.SECC_SOURCE
                + "' AND tehsilcode='" + tehsilCode.trim()
                + "' AND towncode='" + villTownCode.trim() + "' AND wardid='" + wardCode
                + "' AND ahlblockno ='" + ebCode +
                "' AND ahlsubblockno ='" + subEbCode + "'";
        AppUtility.showLog(AppConstant.LOG_STATUS, TAG, " Household Query : " + query);


        return CommonDatabase.getHouseholdList(context, query);
    }


    public static ArrayList<SeccMemberItem> getSeccMemberSubEbWiseList(String stateCode, String distCode, String tehsilCode,
                                                                       String villTownCode, String wardCode, String ebCode,
                                                                       String subEbCode, Context context) {
        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        SQLiteDatabase mDatabase = helpers.createOrOpenDatabase();
        ArrayList<SeccMemberItem> seccMemberItems = new ArrayList<>();
        String query = "Select * from "
                + AppConstant.TRANSECT_POPULATE_SECC
                + " where statecode='"
                + stateCode
                + "' AND districtcode='" +
                distCode
                + "' AND tehsilcode='" + tehsilCode
                + "' AND towncode='" + villTownCode
                + "' AND wardid='" + wardCode
                + "' AND ahlblockno ='" + ebCode +
                "' AND ahlsubblockno ='" + subEbCode + "'";

        return CommonDatabase.getSeccMemberList(context, query);
    }

    public static long WardHouseholdCountCustom(Context context, String stateCode, String distCode,
                                                String tehsilCode, String vtCode, String wardCode, String dataDource) {
        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        SQLiteDatabase mDatabase = helpers.createOrOpenDatabase();
        // long cnt  = DatabaseUtils.queryNumEntries(mDatabase,AppConstant.HOUSE_HOLD_SECC);
        long count = 0;
        String query = "Select * from "
                + AppConstant.HOUSE_HOLD_SECC
                + " where statecode='"
                + stateCode
            /*    + "' AND districtcode='" +
                distCode*/
                + "' AND data_source='" +
                dataDource
               /* + "' AND tehsilcode='" + tehsilCode
                + "' AND towncode='" + vtCode*/ + "'";


        Cursor cur = mDatabase.rawQuery(query, null);
        count = cur.getCount();
        mDatabase.close();
        return count;
    }

    public static long seccHouseHoldWardCount(Context context,
                                              String stateCode, String distCode, String tehsilCode, String vtCode, String wardCode) {
        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        SQLiteDatabase mDatabase = helpers.createOrOpenDatabase();
        // long cnt  = DatabaseUtils.queryNumEntries(mDatabase,AppConstant.HOUSE_HOLD_SECC);
        long count = 0;
        String query = "Select * from "
                + AppConstant.HOUSE_HOLD_SECC
                + " where statecode='"
                + stateCode
                + "' AND districtcode='" +
                distCode
                + "' AND data_source='" +
                AppConstant.SECC_SOURCE
                + "' AND tehsilcode='" + tehsilCode
                + "' AND towncode='" + vtCode
                + "' AND wardid='" + wardCode + "'";


        Cursor cur = mDatabase.rawQuery(query, null);
        count = cur.getCount();
        mDatabase.close();
        return count;
    }

    public static long seccHouseHoldSubEbCount(Context context,
                                               String stateCode, String distCode, String tehsilCode, String vtCode,
                                               String wardCode, String ebCode, String subEbCode) {
        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        SQLiteDatabase mDatabase = helpers.createOrOpenDatabase();
        // long cnt  = DatabaseUtils.queryNumEntries(mDatabase,AppConstant.HOUSE_HOLD_SECC);
        long count = 0;
        String query = "Select * from "
                + AppConstant.HOUSE_HOLD_SECC
                + " where statecode='"
                + stateCode
                + "' AND districtcode='" +
                distCode
                + "' AND data_source='" +
                AppConstant.SECC_SOURCE
                + "' AND tehsilcode='" + tehsilCode
                + "' AND towncode='" + vtCode
                + "' AND wardid='" + wardCode
                + "' AND ahlblockno  ='" + ebCode +
                "' AND ahlsubblockno ='" + subEbCode + "'";
        ;


        Cursor cur = mDatabase.rawQuery(query, null);
        count = cur.getCount();
        mDatabase.close();
        return count;
    }

    public static long rsbyHouseHoldWardCount(Context context,
                                              String stateCode, String distCode, String tehsilCode, String vtCode, String wardCode) {
        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        SQLiteDatabase mDatabase = helpers.createOrOpenDatabase();
        // long cnt  = DatabaseUtils.queryNumEntries(mDatabase,AppConstant.HOUSE_HOLD_SECC);
        long count = 0;
        String query = "Select * from "
                + AppConstant.HOUSE_HOLD_SECC
                + " where statecode='"
                + stateCode
                + "' AND districtcode='" +
                distCode
                + "' AND data_source='" +
                AppConstant.RSBY_SOURCE
                + "' AND tehsilcode='" + tehsilCode
                + "' AND towncode='" + vtCode
                + "' AND wardid='" + wardCode + "'";


        Cursor cur = mDatabase.rawQuery(query, null);
        count = cur.getCount();
        mDatabase.close();
        return count;
    }

    public static long rsbyHouseHoldSubEbCount(Context context,
                                               String stateCode, String distCode, String tehsilCode, String vtCode,
                                               String wardCode, String ebCode, String subEbCode) {
        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        SQLiteDatabase mDatabase = helpers.createOrOpenDatabase();
        // long cnt  = DatabaseUtils.queryNumEntries(mDatabase,AppConstant.HOUSE_HOLD_SECC);
        long count = 0;
        String query = "Select * from "
                + AppConstant.HOUSE_HOLD_SECC
                + " where statecode='"
                + stateCode
                + "' AND districtcode='" +
                distCode
                + "' AND data_source='" +
                AppConstant.RSBY_SOURCE
                + "' AND tehsilcode='" + tehsilCode
                + "' AND towncode='" + vtCode
                + "' AND wardid='" + wardCode
                + "' AND ahlblockno  ='" + ebCode +
                "' AND ahlsubblockno ='" + subEbCode + "'";

        Cursor cur = mDatabase.rawQuery(query, null);
        count = cur.getCount();
        mDatabase.close();
        return count;
    }

    public static long seccMemberWardCount(Context context,
                                           String stateCode, String distCode, String tehsilCode, String vtCode, String wardCode) {
        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        SQLiteDatabase mDatabase = helpers.createOrOpenDatabase();
        // long cnt  = DatabaseUtils.queryNumEntries(mDatabase,AppConstant.HOUSE_HOLD_SECC);
        long count = 0;
        String query = "Select * from "
                + AppConstant.TRANSECT_POPULATE_SECC
                + " where statecode='"
                + stateCode
                + "' AND districtcode='" +
                distCode
                + "' AND tehsilcode='" + tehsilCode
                + "' AND towncode='" + vtCode
                + "' AND wardid='" + wardCode + "'";

        Cursor cur = mDatabase.rawQuery(query, null);
        count = cur.getCount();
        mDatabase.close();
        return count;
    }

    public static long seccMemberSubEbCount(Context context,
                                            String stateCode, String distCode, String tehsilCode, String vtCode,
                                            String wardCode, String ebCode, String subEbCode) {
        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        SQLiteDatabase mDatabase = helpers.createOrOpenDatabase();
        // long cnt  = DatabaseUtils.queryNumEntries(mDatabase,AppConstant.HOUSE_HOLD_SECC);
        long count = 0;
        String query = "Select * from "
                + AppConstant.TRANSECT_POPULATE_SECC
                + " where statecode='"
                + stateCode
                + "' AND districtcode='" +
                distCode
                + "' AND tehsilcode='" + tehsilCode
                + "' AND towncode='" + vtCode
                + "' AND wardid='" + wardCode
                + "' AND ahlblockno  ='" + ebCode +
                "' AND ahlsubblockno ='" + subEbCode + "'";

        Cursor cur = mDatabase.rawQuery(query, null);
        count = cur.getCount();
        mDatabase.close();
        return count;
    }

    public static long WardMemberCountCustom(Context context,
                                             String stateCode, String distCode, String tehsilCode, String vtCode,
                                             String wardCode, String dataSource) {
        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        SQLiteDatabase mDatabase = helpers.createOrOpenDatabase();
        // long cnt  = DatabaseUtils.queryNumEntries(mDatabase,AppConstant.HOUSE_HOLD_SECC);
        long count = 0;
        String query = "Select * from "
                + AppConstant.TRANSECT_POPULATE_SECC
                + " where statecode='"
                + stateCode
               /* + "' AND districtcode='" +
                distCode*/
                + "' AND data_source='" +
                dataSource
                /*+ "' AND tehsilcode='" + tehsilCode
                + "' AND towncode='" + vtCode*/ + "'";

        Cursor cur = mDatabase.rawQuery(query, null);
        count = cur.getCount();
        mDatabase.close();
        return count;
    }


}
