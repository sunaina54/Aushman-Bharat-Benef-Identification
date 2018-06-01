package com.nhpm;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.nhpm.LocalDataBase.DatabaseHelpers;
import com.nhpm.LocalDataBase.dto.MemberRequest;
import com.nhpm.Models.MemberModel;
import com.nhpm.ReqRespModels.NhsDataList;
import com.nhpm.Utility.AppUtility;

import java.util.ArrayList;

/**
 * Created by psqit on 8/31/2016.
 */
public class NHSMasterDaoImpl implements NHSMasterDao {
    @Override
    public ArrayList<String> getHouseholds(String stateid, String distid, String subdistid, String towncode, String wardid, String blockno, DatabaseHelpers helper) {
        SQLiteDatabase mDatabase=helper.createOrOpenDatabase();


        ArrayList<String> houselist = new ArrayList<>();
        Cursor cur = mDatabase.rawQuery("Select ahlslnohhd from "
                + AppUtility.TABLE_MASTER_NHSDATA + " where statecode='"
                + stateid + "' AND districtcode='"+distid+"' AND tehsilcode='"+subdistid+"'AND towncode='"+towncode+"'AND wardid='"+wardid+"' AND blockno='"+blockno+"'", null);
        if (cur != null) {
            if (cur.getCount() > 0) {

                cur.moveToFirst();
                while (cur.isAfterLast() == false) {

                    houselist.add(cur.getString(cur.getColumnIndex("ahlslnohhd")));


                    cur.moveToNext();
                }
            }
            cur.close();

        }
        mDatabase.close();
        helper.close();
        return houselist;
    }

    @Override
    public long save(NhsDataList item, DatabaseHelpers helper) {
        helper.createOrOpenDatabase();
        ContentValues values = new ContentValues();
        values.put("remoteid", item.getRemoteid());
        values.put("tin ", item.getTin());
        values.put("statecode", item.getStatecode());
        values.put("districtcode", item.getDistrictcode());
        values.put("tehsilcode", item.getTehsilcode());
        values.put("towncode", item.getTowncode());
        values.put("wardid",item.getWardid());
        values.put("blockno", item.getBlockno());
        values.put("thid", item.getThid());
        Log.d("NHMsater : ", "slnohhd_npr==>>" + item.getSlnohhdNpr());
        values.put("slnohhd_npr", item.getSlnohhdNpr());
        values.put("slnomember_npr", item.getSlnomemberNpr());
        values.put("name_npr", item.getNameNpr());
        values.put("namesl_npr", item.getNameslNpr());
        values.put("fathernm_npr", item.getFathernmNpr());
        values.put("fathernmsl_npr", item.getFathernmslNpr());
        values.put("mothernm_npr", item.getMothernmNpr());
        values.put("mothernmsl_npr", item.getMothernmslNpr());
        values.put("occuname_npr", item.getOccunameNpr());
        values.put("occunamesl_npr",item.getOccunameslNpr());
        values.put("relname_npr",item.getRelnameNpr());

        values.put("relnamesl_npr",item.getRelnameslNpr());
        values.put("genderid_npr",item.getGenderidNpr());
        values.put("dob_npr",item.getDobNpr());
        values.put("mstatusid_npr",item.getMstatusidNpr());
        values.put("eduname_npr",item.getEdunameNpr());
        values.put("spousenm",item.getSpousenm());
        values.put("aadhaar_no",item.getAadhaarNo());
        values.put("eid",item.getEid());
        values.put("statename",item.getStatename());
        values.put("districtname",item.getDistrictname());
        values.put("villagename",item.getVillagename());
        values.put("countryname",item.getCountryname());
        values.put("addressline1",item.getAddressline1());
        values.put("addressline2",item.getAddressline2());
        values.put("addressline3",item.getAddressline3());
        values.put("addressline4",item.getAddressline4());

        values.put("addressline5",item.getAddressline5());
        values.put("pincode",item.getPincode());
        values.put("paddressline1",item.getPaddressline1());
        values.put("paddressline2",item.getPaddressline2());
        values.put("paddressline3",item.getPaddressline3());
        values.put("paddressline4",item.getPaddressline4());
        values.put("paddressline5",item.getPaddressline5());
        values.put("ppincode",item.getPpincode());

        values.put("phone_respondent",item.getPhoneRespondent());
        values.put("slnohhd_secc",item.getSlnohhdNpr());
        values.put("slnomember_secc",item.getSlnomemberSecc());
        values.put("name_secc",item.getNameSecc());
        values.put("name_sl_secc",item.getNameSlSecc());
        values.put("fathername_secc",item.getFathernameSecc());
        values.put("fathername_sl_secc",item.getFathernameSlSecc());
        values.put("mothername_secc",item.getMothernameSecc());
        values.put("mothername_sl_secc",item.getMothernameSlSecc());
        values.put("occupation_secc",item.getOccupationSecc());
        values.put("occupation_sl_secc",item.getOccupationSlSecc());
        values.put("relation_secc",item.getRelationSecc());
        values.put("relation_sl_secc",item.getRelationSlSecc());
        values.put("genderid_secc",item.getGenderidSecc());
        values.put("dob_secc",item.getDobSecc());
        values.put("mstatusid_secc",item.getMstatusidSecc());
        values.put("educode_secc",item.getEducodeSecc());
        values.put("education_other",item.getEducationOther());
        values.put("ahlblockno",item.getAhlblockno());
        values.put("ahlslnohhd",item.getAhlslnohhd());
        values.put("disabilitycode",item.getDisabilitycode());
        values.put("typeofhhd",item.getTypeofhhd());

        values.put("ahl_tin",item.getAhlTin());
        values.put("hhd_landownedcodes",item.getHhdLandownedcodes());
        values.put("totalirrigated",item.getTotalirrigated());
        values.put("totalunirrigated",item.getTotalunirrigated());
        values.put("otherirrigated",item.getOtherirrigated());
        values.put("caste_group",item.getCasteGroup());
        values.put("ahltypeofeb",item.getAhltypeofeb());
        values.put("livinginshelter",item.getLivinginshelter());
        values.put("incomesource_urban",item.getIncomesourceUrban());
        values.put("wages_urban",item.getWagesUrban());
        values.put("religion",item.getReligion());
        values.put("chronicillness",item.getChronicillness());
        values.put("housing_walltype",item.getHousingWalltype());
        values.put("housing_rooftype",item.getHousingRooftype());
        values.put("housing_ownership",item.getHousingOwnership());
        values.put("housing_no_of_rooms",item.getHousingNoOfRooms());
        values.put("hhd_asset_ref",item.getHhdAssetRef());
        values.put("hhd_asset_telmob",item.getHhdAssetTelmob());
        values.put("hhd_asset_com_lap",item.getHhdAssetComLap());
        values.put("hhd_asset_mveh",item.getHhdAssetMveh());
        values.put("hhd_asset_ac",item.getHhdAssetAc());

        values.put("hhd_asset_wmachine",item.getHhdAssetWmachine());
        values.put("hhd_ptg",item.getHhdPtg());
        values.put("hhd_lrbl",item.getHhdLrbl());
        values.put("hhd_ms",item.getHhdMs());
        values.put("hhd_magri_equip",item.getHhdMagriEquip());
        values.put("hhd_irri_equip",item.getHhdIrriEquip());
        values.put("hhd_kcc",item.getHhdKcc());
        values.put("hhd_emp_sal",item.getHhdEmpSal());
        values.put("hhd_emp_sector",item.getHhdEmpSector());
        values.put("hhd_emp_pit",item.getHhdEmpPit());
        values.put("hhd_emp_erg",item.getHhdEmpErg());
        values.put("hhd_emp_hem",item.getHhdEmpHem());
        values.put("hhd_emp_other",item.getHhdEmpOther());
        values.put("hhd_amenitiescodes_dw",item.getHhdAmenitiescodesDw());
        values.put("hhd_amenitiescodes_l",item.getHhdAmenitiescodesL());
        values.put("hhd_amenitiescodes_wsl",item.getHhdAmenitiescodesWsl());
        values.put("hhd_amenitiescodes_wwo",item.getHhdAmenitiescodesWwo());
        values.put("hhd_amenitiescodes_srk",item.getHhdAmenitiescodesSrk());

        values.put("member_status",item.getMemberStatus());
        values.put("source",item.getSource());
        values.put("imagefilename",item.getImagefilename());
        values.put("pds",item.getPds());
        values.put("pension",item.getPension());
        values.put("edunamesl",item.getEdunamesl());
        values.put("spousenmsl",item.getSpousenmsl());
        values.put("villagenamesl",item.getVillagenamesl());
        values.put("districtnamesl",item.getDistrictnamesl());
        values.put("statenamesl",item.getStatenamesl());

        values.put("countrynamesl",item.getCountrynamesl());
        values.put("natnamesl",item.getNatnamesl());
        values.put("addressline1sl",item.getAddressline1Sl());
        values.put("addressline2sl",item.getAddressline2Sl());
        values.put("addressline3sl",item.getAddressline3Sl());
        values.put("addressline4sl",item.getAddressline4Sl());
        values.put("addressline5sl",item.getAddressline5Sl());

        values.put("paddressline1sl",item.getPaddressline1Sl());
        values.put("paddressline2sl",item.getPaddressline2Sl());
        values.put("paddressline3sl",item.getPaddressline3Sl());
        values.put("paddressline4sl",item.getPaddressline4Sl());
        values.put("paddressline5sl",item.getPaddressline5Sl());
        values.put("name_respondentsl",item.getNameRespondentsl());
        values.put("census2011_stcode",item.getCensus2011Stcode());

        values.put("census2011_dtcode",item.getCensus2011Dtcode());
        values.put("census2011_tehsilcode",item.getCensus2011Tehsilcode());
        values.put("cencus2011_towncode",item.getCencus2011Towncode());

        SQLiteDatabase db = helper.getWritableDatabase();
        long insertFlag = db.insert(AppUtility.TABLE_MASTER_NHSDATA, null,
                values);
        Log.d("MEMBER ","ADDED"+insertFlag);
        db.close();
        helper.close();
        return insertFlag;
    }

    @Override
    public ArrayList<MemberModel> getMembers(String householdId,String towncode,String wardid,String blockno,DatabaseHelpers helpers) {
        SQLiteDatabase mDatabase=helpers.createOrOpenDatabase();

        MemberModel memberModel = null;
        ArrayList<MemberModel> memberlist = new ArrayList<>();
        Cursor cur = mDatabase.rawQuery("Select id,tin,name_npr,ahlslnohhd from "
                + AppUtility.TABLE_MASTER_NHSDATA + " where ahlslnohhd='"
                + householdId + "'AND towncode='"+towncode+"'AND wardid='"+wardid+"' AND blockno='"+blockno+"'", null);
        if (cur != null) {
            if (cur.getCount() > 0) {

                cur.moveToFirst();
                while (cur.isAfterLast() == false) {
                    memberModel=new MemberModel();
                    memberModel.setId(cur.getString(cur.getColumnIndex("id")));
                    memberModel.setTin(cur.getString(cur.getColumnIndex("tin")));
                    memberModel.setName(cur.getString(cur.getColumnIndex("name_npr")));
                    memberModel.setSlnohhd_npr(cur.getString(cur.getColumnIndex("ahlslnohhd")));
                    memberlist.add(memberModel);
                    cur.moveToNext();
                }
            }
            cur.close();

        }
        mDatabase.close();
        helpers.close();
        return memberlist;
    }

    @Override
    public NhsDataList getMemberDetail(String tin, DatabaseHelpers helpers) {
        SQLiteDatabase mDatabase=helpers.createOrOpenDatabase();

        NhsDataList nhsDataList = null;

        Cursor cur = mDatabase.rawQuery("Select * from "
                + AppUtility.TABLE_MASTER_NHSDATA + " where id='"
                + tin + "' ", null);
        if (cur != null) {
            if (cur.getCount() > 0) {

                cur.moveToFirst();
                while (cur.isAfterLast() == false)
                {
                    nhsDataList=new NhsDataList();
                    nhsDataList.setId(cur.getString(cur.getColumnIndex("id")));
                    nhsDataList.setRemoteid(cur.getString(cur.getColumnIndex("remoteid")));
                    nhsDataList.setTin(cur.getString(cur.getColumnIndex("tin")));
                    nhsDataList.setNameNpr(cur.getString(cur.getColumnIndex("name_npr")));
                    nhsDataList.setRelnameNpr(cur.getString(cur.getColumnIndex("relname_npr")));
                    nhsDataList.setFathernmNpr(cur.getString(cur.getColumnIndex("fathernm_npr")));
                    nhsDataList.setMothernmNpr(cur.getString(cur.getColumnIndex("mothernm_npr")));
                    nhsDataList.setDobNpr(cur.getString(cur.getColumnIndex("dob_npr")));
                    nhsDataList.setGenderidNpr(cur.getString(cur.getColumnIndex("genderid_npr")));
                    nhsDataList.setPhoneRespondent(cur.getString(cur.getColumnIndex("phone_respondent")));
                    nhsDataList.setMstatusidNpr(cur.getString(cur.getColumnIndex("mstatusid_npr")));
                    nhsDataList.setAadhaarNo(cur.getString(cur.getColumnIndex("aadhaar_no")));
                    //nhsDataList.setMobile(cur.getString(cur.getColumnIndex("genderid_npr")));
                    nhsDataList.setOccunameNpr(cur.getString(cur.getColumnIndex("occuname_npr")));
                    //confirm state inssurance field
                    nhsDataList.setIncomesourceUrban(cur.getString(cur.getColumnIndex("incomesource_urban")));
                    //nhsDataList.setGenderidNpr(cur.getString(cur.getColumnIndex("genderid_npr")));
                    cur.moveToNext();
                }
            }
            cur.close();

        }
        mDatabase.close();
        helpers.close();
        return nhsDataList;
    }
    @Override
    public long update(NhsDataList item,DatabaseHelpers helpers) {
        helpers.createOrOpenDatabase();
        ContentValues values = new ContentValues();
        //values.put("userbasicinfoid",item.getUserbasicinfoid());
       // values.put("id", item.getId());
        values.put("remoteid",item.getRemoteid());
        values.put("name_npr", item.getNameNpr());
        values.put("relname_npr", item.getRelnameNpr());
        values.put("fathernm_npr", item.getFathernmNpr());
        values.put("mothernm_npr", item.getMothernmNpr());
        values.put("aadhaar_no", item.getAadhaarNo());
        values.put("occuname_npr",item.getOccunameNpr());
        values.put("incomesource_urban", item.getIncomesourceUrban());
        values.put("phone_respondent",item.getPhoneRespondent());
        values.put("genderid_npr",item.getGenderidNpr());
        values.put("mstatusid_npr",item.getMstatusidNpr());
        values.put("dob_npr",item.getDobNpr());
        values.put("tin",item.getTin());
        SQLiteDatabase db = helpers.getWritableDatabase();
        int updateFlag = db.update(AppUtility.TABLE_MASTER_NHSDATA, values,
                "id=" + item.getId(), null);
        Log.d("ID",item.getId()+"FATHER"+item.getFathernmNpr());
        Log.d("Update Member Data", " Update Flag Value : " + updateFlag);
        db.close();
        helpers.close();
        return updateFlag;
    }
    public String getTin(String stateid, String distid, String subdistid, String towncode, String wardid, String blockno,String householdID, DatabaseHelpers helper) {
        SQLiteDatabase mDatabase=helper.createOrOpenDatabase();


   String tinno = null;
        Cursor cur = mDatabase.rawQuery("Select tin from "
                + AppUtility.TABLE_MASTER_NHSDATA + " where statecode='"
                + stateid + "' AND districtcode='"+distid+"' AND tehsilcode='"+subdistid+"'AND towncode='"+towncode+"'AND wardid='"+wardid+"' AND blockno='"+blockno+"' AND ahlslnohhd='"+householdID+"' ORDER BY tin DESC LIMIT 1", null);
        Log.d("TIN QUERY","Select tin from "
                + AppUtility.TABLE_MASTER_NHSDATA + " where statecode='"
                + stateid + "' AND districtcode='"+distid+"' AND tehsilcode='"+subdistid+"'AND towncode='"+towncode+"'AND wardid='"+wardid+"' AND blockno='"+blockno+"' AND ahlslnohhd='"+householdID+"' ORDER BY tin DESC LIMIT 1");
        if (cur != null) {
            if (cur.getCount() > 0) {

                cur.moveToFirst();
                while (cur.isAfterLast() == false) {

                    tinno=cur.getString(cur.getColumnIndex("tin"));


                    cur.moveToNext();
                }
            }
            cur.close();

        }
        mDatabase.close();
        helper.close();
        return tinno;
    }

    @Override
    public NhsDataList getMemberDetailById(String id, DatabaseHelpers helpers) {
        SQLiteDatabase mDatabase=helpers.createOrOpenDatabase();
        NhsDataList nhsDataList = null;

        Cursor cur = mDatabase.rawQuery("Select * from "
                + AppUtility.TABLE_MASTER_NHSDATA + " where id='"
                + id + "' ", null);
        if (cur != null) {
            if (cur.getCount() > 0) {

                cur.moveToFirst();
                while (cur.isAfterLast() == false)
                {
                    nhsDataList=new NhsDataList();
                    nhsDataList.setId(cur.getString(cur.getColumnIndex("id")));
                    nhsDataList.setRemoteid(cur.getString(cur.getColumnIndex("remoteid")));
                    nhsDataList.setStatecode(cur.getString(cur.getColumnIndex("statecode")));
                    nhsDataList.setDistrictcode(cur.getString(cur.getColumnIndex("districtcode")));
                    nhsDataList.setTehsilcode(cur.getString(cur.getColumnIndex("tehsilcode")));
                    nhsDataList.setTowncode(cur.getString(cur.getColumnIndex("towncode")));
                    nhsDataList.setWardid(cur.getString(cur.getColumnIndex("wardid")));
                    nhsDataList.setBlockno(cur.getString(cur.getColumnIndex("blockno")));
                    nhsDataList.setAhlslnohhd(cur.getString(cur.getColumnIndex("ahlslnohhd")));
                    nhsDataList.setSlnohhdNpr(cur.getString(cur.getColumnIndex("slnohhd_npr")));
                   /* *//*values.put("statecode", item.getStatecode());
                    values.put("districtcode", item.getDistrictcode());
                    values.put("tehsilcode", item.getTehsilcode());
                    values.put("towncode", item.getTowncode());
                    values.put("wardid",item.getWardid());
                    values.put("blockno", item.getBlockno());*//*
                    "ahlslnohhd": "0001",
                        "slnohhdNpr":"076",*/
                    nhsDataList.setTin(cur.getString(cur.getColumnIndex("tin")));
                    nhsDataList.setNameNpr(cur.getString(cur.getColumnIndex("name_npr")));
                    nhsDataList.setRelnameNpr(cur.getString(cur.getColumnIndex("relname_npr")));
                    nhsDataList.setFathernmNpr(cur.getString(cur.getColumnIndex("fathernm_npr")));
                    nhsDataList.setMothernmNpr(cur.getString(cur.getColumnIndex("mothernm_npr")));
                    nhsDataList.setDobNpr(cur.getString(cur.getColumnIndex("dob_npr")));
                    nhsDataList.setGenderidNpr(cur.getString(cur.getColumnIndex("genderid_npr")));
                    nhsDataList.setPhoneRespondent(cur.getString(cur.getColumnIndex("phone_respondent")));
                    nhsDataList.setMstatusidNpr(cur.getString(cur.getColumnIndex("mstatusid_npr")));
                    nhsDataList.setAadhaarNo(cur.getString(cur.getColumnIndex("aadhaar_no")));
                    //nhsDataList.setMobile(cur.getString(cur.getColumnIndex("genderid_npr")));
                    nhsDataList.setOccunameNpr(cur.getString(cur.getColumnIndex("occuname_npr")));
                    //confirm state inssurance field
                    nhsDataList.setIncomesourceUrban(cur.getString(cur.getColumnIndex("incomesource_urban")));
                    //nhsDataList.setGenderidNpr(cur.getString(cur.getColumnIndex("genderid_npr")));
                    cur.moveToNext();
                }
            }
            cur.close();

        }
        mDatabase.close();
        helpers.close();
        return nhsDataList;
    }



    @Override
    public ArrayList<NhsDataList> getMembers1(MemberRequest memberRequest,DatabaseHelpers helpers) {
        SQLiteDatabase mDatabase=helpers.createOrOpenDatabase();
        NhsDataList nhsDataList = null;
        ArrayList<NhsDataList> memberlist = new ArrayList<>();
     /*   Cursor cur = mDatabase.rawQuery("Select * from "
                + AppUtility.TABLE_MASTER_NHSDATA + " where id='"
                + id + "' ", null);*/
       /* String query="Select * from "
                + AppUtility.TABLE_MASTER_NHSDATA
                + " where statecode='"
                + memberRequest.getStateCode()
                +"' AND districtcode='"+
                memberRequest.getDistrictCode()
                +"' AND tehsilcode='"+memberRequest.getTehsilCode()
                +"' AND towncode='"+memberRequest.getVillTownCode()+"' AND wardid='"+memberRequest.getWardCode()
                +"' AND blockno='"+memberRequest.getBlockCode()+"' AND fathernm_npr='VENGADESAN'";*/
        String query="Select * from "
                + AppUtility.TABLE_MASTER_NHSDATA
                + " where statecode='"
                + memberRequest.getStateCode()
                +"' AND districtcode='"+
                memberRequest.getDistrictCode()
                +"' AND tehsilcode='"+memberRequest.getTehsilCode()
                +"' AND towncode='"+memberRequest.getVillTownCode()+"' AND wardid='"+memberRequest.getWardCode()
                +"' AND blockno='"+memberRequest.getBlockCode()+"'";
        Log.d("NHSMasterDaoImpl"," Query : "+query);
        Cursor cur = mDatabase.rawQuery(query, null);
        Log.d("NHSMasterDaoImpl"," Query : "+cur.getCount());
        if (cur != null) {
            if (cur.getCount() > 0) {
                cur.moveToFirst();
                while (cur.isAfterLast() == false) {
                    nhsDataList=new NhsDataList();
                    nhsDataList.setId(cur.getString(cur.getColumnIndex("id")));
                    nhsDataList.setRemoteid(cur.getString(cur.getColumnIndex("remoteid")));
                    nhsDataList.setTin(cur.getString(cur.getColumnIndex("tin")));
                    nhsDataList.setAhlTin(cur.getString(cur.getColumnIndex("ahl_tin")));
                    nhsDataList.setAddressline1(cur.getString(cur.getColumnIndex("addressline1")));
                    nhsDataList.setAddressline2(cur.getString(cur.getColumnIndex("addressline2")));
                    nhsDataList.setAhlslnohhd(cur.getString(cur.getColumnIndex("ahlslnohhd")));
                    nhsDataList.setNameNpr(cur.getString(cur.getColumnIndex("name_npr")));
                    nhsDataList.setRelnameNpr(cur.getString(cur.getColumnIndex("relname_npr")));
                    nhsDataList.setFathernmNpr(cur.getString(cur.getColumnIndex("fathernm_npr")));
                    nhsDataList.setMothernmNpr(cur.getString(cur.getColumnIndex("mothernm_npr")));
                    nhsDataList.setDobNpr(cur.getString(cur.getColumnIndex("dob_npr")));
                    nhsDataList.setGenderidNpr(cur.getString(cur.getColumnIndex("genderid_npr")));
                    nhsDataList.setPhoneRespondent(cur.getString(cur.getColumnIndex("phone_respondent")));
                    nhsDataList.setMstatusidNpr(cur.getString(cur.getColumnIndex("mstatusid_npr")));
                    nhsDataList.setAadhaarNo(cur.getString(cur.getColumnIndex("aadhaar_no")));
                    //nhsDataList.setMobile(cur.getString(cur.getColumnIndex("genderid_npr")));
                    nhsDataList.setOccunameNpr(cur.getString(cur.getColumnIndex("occuname_npr")));
                    //confirm state inssurance field
                    nhsDataList.setIncomesourceUrban(cur.getString(cur.getColumnIndex("incomesource_urban")));
                    //nhsDataList.setGenderidNpr(cur.getString(cur.getColumnIndex("genderid_npr")));
                    memberlist.add(nhsDataList);
                    cur.moveToNext();
                }
            }
            cur.close();

        }
        mDatabase.close();
        helpers.close();
        return memberlist;

    }

    @Override
    public ArrayList<NhsDataList> getMemberList(MemberRequest memberRequest,DatabaseHelpers helpers) {
        SQLiteDatabase mDatabase=helpers.createOrOpenDatabase();
        NhsDataList nhsDataList = null;
        ArrayList<NhsDataList> memberlist = new ArrayList<>();
     /*   Cursor cur = mDatabase.rawQuery("Select * from "
                + AppUtility.TABLE_MASTER_NHSDATA + " where id='"
                + id + "' ", null);*/
       /* String query="Select * from "
                + AppUtility.TABLE_MASTER_NHSDATA
                + " where statecode='"
                + memberRequest.getStateCode()
                +"' AND districtcode='"+
                memberRequest.getDistrictCode()
                +"' AND tehsilcode='"+memberRequest.getTehsilCode()
                +"' AND towncode='"+memberRequest.getVillTownCode()+"' AND wardid='"+memberRequest.getWardCode()
                +"' AND blockno='"+memberRequest.getBlockCode()+"' AND fathernm_npr='VENGADESAN'";*/
        String query="Select * from "
                + AppUtility.TABLE_MASTER_NHSDATA
                + " where statecode='"
                + memberRequest.getStateCode()
                +"' AND districtcode='"+
                memberRequest.getDistrictCode()
                +"' AND tehsilcode='"+memberRequest.getTehsilCode()
                +"' AND towncode='"+memberRequest.getVillTownCode()+"' AND wardid='"+memberRequest.getWardCode()
                +"' AND blockno='"+memberRequest.getBlockCode()+"'";
        Log.d("NHSMasterDaoImpl"," Query : "+query);
        Cursor cur = mDatabase.rawQuery(query, null);
        Log.d("NHSMasterDaoImpl"," Query : "+cur.getCount());
        if (cur != null) {
            if (cur.getCount() > 0) {
                cur.moveToFirst();
                while (cur.isAfterLast() == false) {
                    nhsDataList=new NhsDataList();
                    nhsDataList.setId(cur.getString(cur.getColumnIndex("id")));
                    nhsDataList.setRemoteid(cur.getString(cur.getColumnIndex("remoteid")));
                    nhsDataList.setTin(cur.getString(cur.getColumnIndex("tin")));
                    nhsDataList.setAhlTin(cur.getString(cur.getColumnIndex("ahl_tin")));
                    nhsDataList.setAddressline1(cur.getString(cur.getColumnIndex("addressline1")));
                    nhsDataList.setAddressline2(cur.getString(cur.getColumnIndex("addressline2")));
                    nhsDataList.setAhlslnohhd(cur.getString(cur.getColumnIndex("ahlslnohhd")));
                    nhsDataList.setNameNpr(cur.getString(cur.getColumnIndex("name_npr")));
                    nhsDataList.setRelnameNpr(cur.getString(cur.getColumnIndex("relname_npr")));
                    nhsDataList.setFathernmNpr(cur.getString(cur.getColumnIndex("fathernm_npr")));
                    nhsDataList.setMothernmNpr(cur.getString(cur.getColumnIndex("mothernm_npr")));
                    nhsDataList.setDobNpr(cur.getString(cur.getColumnIndex("dob_npr")));
                    nhsDataList.setGenderidNpr(cur.getString(cur.getColumnIndex("genderid_npr")));
                    nhsDataList.setPhoneRespondent(cur.getString(cur.getColumnIndex("phone_respondent")));
                    nhsDataList.setMstatusidNpr(cur.getString(cur.getColumnIndex("mstatusid_npr")));
                    nhsDataList.setAadhaarNo(cur.getString(cur.getColumnIndex("aadhaar_no")));
                    //nhsDataList.setMobile(cur.getString(cur.getColumnIndex("genderid_npr")));
                    nhsDataList.setOccunameNpr(cur.getString(cur.getColumnIndex("occuname_npr")));
                    //confirm state inssurance field
                    nhsDataList.setIncomesourceUrban(cur.getString(cur.getColumnIndex("incomesource_urban")));
                    //nhsDataList.setGenderidNpr(cur.getString(cur.getColumnIndex("genderid_npr")));
                    memberlist.add(nhsDataList);
                    cur.moveToNext();
                }
            }
            cur.close();

        }
        mDatabase.close();
        helpers.close();
        return memberlist;

    }
}
