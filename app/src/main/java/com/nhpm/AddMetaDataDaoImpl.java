package com.nhpm;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.nhpm.LocalDataBase.DatabaseHelpers;
import com.nhpm.ReqRespModels.NhsDataList;
import com.nhpm.Utility.AppUtility;

import java.util.ArrayList;

/**
 * Created by psqit on 9/5/2016.
 */
public class AddMetaDataDaoImpl implements AddMetaDataDao{
    @Override
    public long saveInMetaData(NhsDataList item, DatabaseHelpers helper) {
        helper.createOrOpenDatabase();
        ContentValues values = new ContentValues();

        values.put("remoteid ", item.getRemoteid());

        values.put("name_npr", item.getNameNpr());

        values.put("relname_npr",item.getRelnameNpr());

        values.put("fathernm_npr", item.getFathernmNpr());

        values.put("mothernm_npr", item.getMothernmNpr());
        values.put("aadhaar_no", item.getAadhaarNo());
        values.put("occuname_npr", item.getOccunameNpr());
        values.put("incomesource_urban", item.getIncomesourceUrban());
        values.put("phone_respondent",item.getPhoneRespondent());
        values.put("genderid_npr",item.getGenderidNpr());
        values.put("mstatusid_npr",item.getMstatusidNpr());
        values.put("dob_npr",item.getDobNpr());
        values.put("status",item.getStatus());
        values.put("tin",item.getTin());


        values.put("statecode", item.getStatecode());
        values.put("districtcode", item.getDistrictcode());
        values.put("tehsilcode", item.getTehsilcode());
        values.put("towncode", item.getTowncode());
        values.put("wardid",item.getWardid());
        values.put("blockno", item.getBlockno());
        values.put("ahlslnohhd",item.getAhlslnohhd());
        Log.d("NHMsater : ","slnohhd_npr==>>"+item.getSlnohhdNpr());
        values.put("slnohhd_npr",item.getSlnohhdNpr());

        SQLiteDatabase db = helper.getWritableDatabase();
        long insertFlag = db.insert(AppUtility.TABLE_METADATA_ADD, null,
                values);

        db.close();
        helper.close();
        return insertFlag;
    }
    @Override
    public NhsDataList getMetaDataItem(String id, DatabaseHelpers helpers) {
        SQLiteDatabase mDatabase=helpers.createOrOpenDatabase();

        NhsDataList nhsDataList = null;

        Cursor cur = mDatabase.rawQuery("Select * from "
                + AppUtility.TABLE_METADATA_ADD + " where id='"
                + id + "' ", null);
        if (cur != null) {
            if (cur.getCount() > 0) {

                cur.moveToFirst();
                while (cur.isAfterLast() == false) {
                    nhsDataList=new NhsDataList();
                    nhsDataList.setRemoteid(cur.getString(cur.getColumnIndex("remoteid")));
                    nhsDataList.setId(cur.getString(cur.getColumnIndex("id")));
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
                    nhsDataList.setStatus(cur.getString(cur.getColumnIndex("status")));
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
    public long updateMetaData(NhsDataList item, DatabaseHelpers helpers) {
        helpers.createOrOpenDatabase();
        ContentValues values = new ContentValues();
        //values.put("userbasicinfoid",item.getUserbasicinfoid());
        values.put("remoteid", item.getRemoteid());


        values.put("name_npr", item.getNameNpr());

        values.put("relname_npr",item.getRelnameNpr());

        values.put("fathernm_npr", item.getFathernmNpr());

        values.put("mothernm_npr", item.getMothernmNpr());
        values.put("aadhaar_no", item.getAadhaarNo());
        values.put("occuname_npr", item.getOccunameNpr());
        values.put("incomesource_urban", item.getIncomesourceUrban());
        values.put("phone_respondent",item.getPhoneRespondent());
        values.put("genderid_npr",item.getGenderidNpr());
        values.put("mstatusid_npr",item.getMstatusidNpr());
        values.put("dob_npr",item.getDobNpr());
        values.put("status",item.getStatus());
        values.put("tin",item.getTin());


        values.put("statecode", item.getStatecode());
        values.put("districtcode", item.getDistrictcode());
        values.put("tehsilcode", item.getTehsilcode());
        values.put("towncode", item.getTowncode());
        values.put("wardid",item.getWardid());
        values.put("blockno", item.getBlockno());
        values.put("ahlslnohhd",item.getAhlslnohhd());
        Log.d("NHMsater : ","slnohhd_npr==>>"+item.getSlnohhdNpr());
        values.put("slnohhd_npr",item.getSlnohhdNpr());
        Log.d("id",""+item.getId());

        SQLiteDatabase db = helpers.getWritableDatabase();
        int updateFlag = db.update(AppUtility.TABLE_METADATA_ADD, values,
                "id=" + item.getId(), null);

        Log.d("Update LOCAL ADD Data", " Update Flag Value : " + updateFlag);
        db.close();
        helpers.close();
        return updateFlag;
    }
    @Override
    public ArrayList<NhsDataList> getPendingAdds(DatabaseHelpers helpers) {
        SQLiteDatabase mDatabase=helpers.createOrOpenDatabase();

        NhsDataList nhsDataList = null;
        ArrayList<NhsDataList> nhsDataListArrayList = new ArrayList<>();
        Cursor cur = mDatabase.rawQuery("Select * from "
                + AppUtility.TABLE_METADATA_ADD + " where status='"
                + "false" + "'", null);
        if (cur != null) {
            if (cur.getCount() > 0) {

                cur.moveToFirst();
                while (cur.isAfterLast() == false) {

                    nhsDataList = new NhsDataList();
                  //  nhsDataList.setId();
                    nhsDataList.setRemoteid(cur.getString(cur.getColumnIndex("remoteid")));
                    nhsDataList.setId(cur.getString(cur.getColumnIndex("id")));
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
                    nhsDataList.setStatus(cur.getString(cur.getColumnIndex("status")));

                    nhsDataList.setTin(cur.getString(cur.getColumnIndex("tin")));
                    nhsDataList.setStatecode(cur.getString(cur.getColumnIndex("statecode")));
                    nhsDataList.setDistrictcode(cur.getString(cur.getColumnIndex("districtcode")));
                    nhsDataList.setTehsilcode(cur.getString(cur.getColumnIndex("tehsilcode")));
                    nhsDataList.setTowncode(cur.getString(cur.getColumnIndex("towncode")));

                    nhsDataList.setWardid(cur.getString(cur.getColumnIndex("wardid")));
                    nhsDataList.setBlockno(cur.getString(cur.getColumnIndex("blockno")));
                    nhsDataList.setAhlslnohhd(cur.getString(cur.getColumnIndex("ahlslnohhd")));
                    nhsDataList.setSlnohhdNpr(cur.getString(cur.getColumnIndex("slnohhd_npr")));
                    nhsDataListArrayList.add(nhsDataList);
                    cur.moveToNext();
                }
            }
            cur.close();

        }
        mDatabase.close();
        helpers.close();
        return nhsDataListArrayList;
    }
    @Override
    public long updateMetaDataFlag(NhsDataList item, DatabaseHelpers helpers) {
        helpers.createOrOpenDatabase();
        ContentValues values = new ContentValues();
        //values.put("userbasicinfoid",item.getUserbasicinfoid());
        //  values.put("id", item.getId());

        values.put("status",item.getStatus());
        SQLiteDatabase db = helpers.getWritableDatabase();
        int updateFlag = db.update(AppUtility.TABLE_METADATA_ADD, values,
                "id=" + item.getId(), null);

        Log.d("UADD Data FLAG", " Update: " + updateFlag);
        db.close();
        helpers.close();
        return updateFlag;
    }
    public void delete(String id,DatabaseHelpers helpers) {
        SQLiteDatabase db = helpers.getWritableDatabase();
        db.execSQL("delete from "+AppUtility.TABLE_METADATA_ADD+" where id='"+id+"'");
    }
}
