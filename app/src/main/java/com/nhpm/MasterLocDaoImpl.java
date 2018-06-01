package com.nhpm;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.nhpm.LocalDataBase.DatabaseHelpers;
import com.nhpm.LocalDataBase.dto.MemberRequest;
import com.nhpm.ReqRespModels.MasterLocList;
import com.nhpm.Utility.AppUtility;

import java.util.ArrayList;

/**
 * Created by psqit on 8/29/2016.
 */
public class MasterLocDaoImpl implements MasterLocDao {
    /*"stateCode": "34",
    "stateName": "PUDUCHERRY",
    "districtCode": "04",
    "districtName": "Karaikal",
    "tehsilCode": "002",
    "tehsilName": "ThirunallarTaluk",
    "vtCode": "0001",
    "vtName": "Kurumbagaram",
    "wardCode": "",
    "ruralUrban": "R",
    "gpCode": "0001",
    "gpName": "THIRUNALLAR TALUK",
    "mddsStc": "34",
    "mddsDtc": "637",
    "mddsSdtc": "05915",
    "mddsPlcn": "644996",
    "mddsName": "Kurumbagaram",
    "localBodyCode": "253387",
    "localBodyName": "KURUMBAGARAM"*/
    @Override
    public long save(MasterLocList item,
                     DatabaseHelpers helper) {
        helper.createOrOpenDatabase();
        ContentValues values = new ContentValues();

        values.put("state_code", item.getStateCode());
        values.put("state_name", item.getStateName());
        values.put("district_code", item.getDistrictCode());
        values.put("district_name", item.getDistrictName());
        values.put("tehsil_code", item.getTehsilCode());
        values.put("tehsil_name", item.getTehsilName());
        values.put("vt_code", item.getVtCode());
        values.put("vt_name", item.getVtName());
        values.put("ward_code", item.getWardCode());
        values.put("rural_urban", item.getRuralUrban());
        values.put("gp_code", item.getBlockCode());
        values.put("gp_name", item.getGpName());
        values.put("mdds_stc", item.getMddsStc());
        values.put("mdds_dtc", item.getMddsDtc());
        values.put("mdds_sdtc", item.getMddsSdtc());
        values.put("mdds_plcn", item.getMddsPlcn());
        values.put("mdds_name", item.getMddsName());
        values.put("local_body_code", item.getLocalBodyCode());
        values.put("local_body_name", item.getLocalBodyName());
        SQLiteDatabase db = helper.getWritableDatabase();
        long insertFlag = db.insert(AppUtility.TABLE_MASTER_LOC, null,
                values);
        db.close();
        helper.close();
        return insertFlag;
    }

    @Override
    public ArrayList<MasterLocList> getVillages(String stateid, String distid, String subdistid, DatabaseHelpers helper) {
        SQLiteDatabase mDatabase = helper.createOrOpenDatabase();

        MasterLocList masterLocList = null;
        ArrayList<MasterLocList> loanList = new ArrayList<>();
        Cursor cur = mDatabase.rawQuery("Select * from "
                + AppUtility.TABLE_MASTER_LOC + " where state_code='"
                + stateid + "' AND district_code='" + distid + "' AND tehsil_code='" + subdistid + "' GROUP BY vt_code", null);
        Log.d("Village query", "Select * from "
                + AppUtility.TABLE_MASTER_LOC + " where state_code='"
                + stateid + "' AND district_code='" + distid + "' AND tehsil_code='" + subdistid + "' GROUP BY vt_code");
        if (cur != null) {
            if (cur.getCount() > 0) {

                cur.moveToFirst();
                while (cur.isAfterLast() == false) {
                    masterLocList = new MasterLocList();
                    masterLocList.setStateCode(cur.getString(cur.getColumnIndex("state_code")));
                    masterLocList.setStateName(cur.getString(cur.getColumnIndex("state_name")));
                    Log.d("MasterLoc", "State Name : " + masterLocList.getStateName());
                    Log.d("MasterLoc", "State Code : " + masterLocList.getStateCode());
                    masterLocList.setDistrictCode(cur.getString(cur.getColumnIndex("district_code")));
                    masterLocList.setDistrictName(cur.getString(cur.getColumnIndex("district_name")));
                    masterLocList.setTehsilCode(cur.getString(cur.getColumnIndex("tehsil_code")));
                    masterLocList.setTehsilName(cur.getString(cur.getColumnIndex("tehsil_name")));
                    masterLocList.setVtCode(cur.getString(cur.getColumnIndex("vt_code")));
                    masterLocList.setVtName(cur.getString(cur.getColumnIndex("vt_name")));
                    masterLocList.setWardCode(cur.getString(cur.getColumnIndex("ward_code")));
                    masterLocList.setRuralUrban(cur.getString(cur.getColumnIndex("rural_urban")));
                    masterLocList.setBlockCode(cur.getString(cur.getColumnIndex("gp_code")));
                    masterLocList.setGpName(cur.getString(cur.getColumnIndex("gp_name")));
                    masterLocList.setMddsStc(cur.getString(cur.getColumnIndex("mdds_stc")));
                    masterLocList.setMddsDtc(cur.getString(cur.getColumnIndex("mdds_dtc")));
                    masterLocList.setMddsSdtc(cur.getString(cur.getColumnIndex("mdds_sdtc")));
                    masterLocList.setMddsPlcn(cur.getString(cur.getColumnIndex("mdds_plcn")));
                    masterLocList.setMddsName(cur.getString(cur.getColumnIndex("mdds_name")));
                    masterLocList.setLocalBodyCode(cur.getString(cur.getColumnIndex("local_body_code")));
                    masterLocList.setLocalBodyName(cur.getString(cur.getColumnIndex("local_body_name")));
                    loanList.add(masterLocList);
                    cur.moveToNext();
                }
            }
            cur.close();

        }
        mDatabase.close();
        helper.close();
        return loanList;

    }

    @Override
    public ArrayList<MasterLocList> getWards(String stateid, String distid, String subdistid, String villcode, DatabaseHelpers helper) {
        SQLiteDatabase mDatabase = helper.createOrOpenDatabase();
        MasterLocList masterLocList = null;
        ArrayList<MasterLocList> loanList = new ArrayList<>();
        Cursor cur = mDatabase.rawQuery("Select * from "
                + AppUtility.TABLE_MASTER_LOC + " where state_code='"
                + stateid + "' AND district_code='" + distid + "' AND tehsil_code='" + subdistid + "' AND vt_code='" + villcode + "' GROUP BY ward_code", null);
        Log.d("GET WARD QUERY", "Select * from "
                + AppUtility.TABLE_MASTER_LOC + " where state_code='"
                + stateid + "' AND district_code='" + distid + "' AND tehsil_code='" + subdistid + "' AND vt_code='" + villcode + "' GROUP BY ward_code");
        if (cur != null) {
            if (cur.getCount() > 0) {

                cur.moveToFirst();
                while (cur.isAfterLast() == false) {
                    masterLocList = new MasterLocList();
                    masterLocList.setStateCode(cur.getString(cur.getColumnIndex("state_code")));
                    masterLocList.setStateName(cur.getString(cur.getColumnIndex("state_name")));
                    masterLocList.setDistrictCode(cur.getString(cur.getColumnIndex("district_code")));
                    masterLocList.setDistrictName(cur.getString(cur.getColumnIndex("district_name")));
                    masterLocList.setTehsilCode(cur.getString(cur.getColumnIndex("tehsil_code")));
                    masterLocList.setTehsilName(cur.getString(cur.getColumnIndex("tehsil_name")));
                    masterLocList.setVtCode(cur.getString(cur.getColumnIndex("vt_code")));
                    masterLocList.setVtName(cur.getString(cur.getColumnIndex("vt_name")));
                    masterLocList.setWardCode(cur.getString(cur.getColumnIndex("ward_code")));
                    masterLocList.setRuralUrban(cur.getString(cur.getColumnIndex("rural_urban")));
                    masterLocList.setBlockCode(cur.getString(cur.getColumnIndex("gp_code")));
                    masterLocList.setGpName(cur.getString(cur.getColumnIndex("gp_name")));
                    masterLocList.setMddsStc(cur.getString(cur.getColumnIndex("mdds_stc")));
                    masterLocList.setMddsDtc(cur.getString(cur.getColumnIndex("mdds_dtc")));
                    masterLocList.setMddsSdtc(cur.getString(cur.getColumnIndex("mdds_sdtc")));
                    masterLocList.setMddsPlcn(cur.getString(cur.getColumnIndex("mdds_plcn")));
                    masterLocList.setMddsName(cur.getString(cur.getColumnIndex("mdds_name")));
                    masterLocList.setLocalBodyCode(cur.getString(cur.getColumnIndex("local_body_code")));
                    masterLocList.setLocalBodyName(cur.getString(cur.getColumnIndex("local_body_name")));
                    loanList.add(masterLocList);
                    cur.moveToNext();
                }
            }
            cur.close();

        }
        mDatabase.close();
        helper.close();
        return loanList;

    }

    @Override
    public ArrayList<MasterLocList> getBlocks(String stateid, String distid, String subdistid, String villcode, String wardcode, DatabaseHelpers helper) {
        SQLiteDatabase mDatabase = helper.createOrOpenDatabase();

        MasterLocList masterLocList = null;
        ArrayList<MasterLocList> loanList = new ArrayList<>();
        Cursor cur = mDatabase.rawQuery("Select * from "
                + AppUtility.TABLE_MASTER_LOC + " where state_code='"
                + stateid + "' AND district_code='" + distid + "' AND tehsil_code='" + subdistid + "' AND vt_code='" + villcode + "' AND ward_code='" + wardcode + "' GROUP BY gp_code", null);
        Log.d("BLOCK QRY", "Select * from "
                + AppUtility.TABLE_MASTER_LOC + " where state_code='"
                + stateid + "' AND district_code='" + distid + "' AND tehsil_code='" + subdistid + "' AND vt_code='" + villcode + "' AND ward_code='" + wardcode + "' GROUP BY gp_code");
        if (cur != null) {
            if (cur.getCount() > 0) {

                cur.moveToFirst();
                while (cur.isAfterLast() == false) {
                    masterLocList = new MasterLocList();
                    masterLocList.setStateCode(cur.getString(cur.getColumnIndex("state_code")));
                    masterLocList.setStateName(cur.getString(cur.getColumnIndex("state_name")));
                    masterLocList.setDistrictCode(cur.getString(cur.getColumnIndex("district_code")));
                    masterLocList.setDistrictName(cur.getString(cur.getColumnIndex("district_name")));
                    masterLocList.setTehsilCode(cur.getString(cur.getColumnIndex("tehsil_code")));
                    masterLocList.setTehsilName(cur.getString(cur.getColumnIndex("tehsil_name")));
                    masterLocList.setVtCode(cur.getString(cur.getColumnIndex("vt_code")));
                    masterLocList.setVtName(cur.getString(cur.getColumnIndex("vt_name")));
                    masterLocList.setWardCode(cur.getString(cur.getColumnIndex("ward_code")));
                    masterLocList.setRuralUrban(cur.getString(cur.getColumnIndex("rural_urban")));
                    masterLocList.setBlockCode(cur.getString(cur.getColumnIndex("gp_code")));
                    masterLocList.setGpName(cur.getString(cur.getColumnIndex("gp_name")));
                    masterLocList.setMddsStc(cur.getString(cur.getColumnIndex("mdds_stc")));
                    masterLocList.setMddsDtc(cur.getString(cur.getColumnIndex("mdds_dtc")));
                    masterLocList.setMddsSdtc(cur.getString(cur.getColumnIndex("mdds_sdtc")));
                    masterLocList.setMddsPlcn(cur.getString(cur.getColumnIndex("mdds_plcn")));
                    masterLocList.setMddsName(cur.getString(cur.getColumnIndex("mdds_name")));
                    masterLocList.setLocalBodyCode(cur.getString(cur.getColumnIndex("local_body_code")));
                    masterLocList.setLocalBodyName(cur.getString(cur.getColumnIndex("local_body_name")));
                    loanList.add(masterLocList);
                    cur.moveToNext();
                }
            }
            cur.close();

        }
        mDatabase.close();
        helper.close();
        return loanList;

    }

    @Override
    public ArrayList<MasterLocList> getMasterLocation(MemberRequest locRequest, DatabaseHelpers dbHelper) {
        SQLiteDatabase mDatabase = dbHelper.createOrOpenDatabase();

        MasterLocList masterLocList = null;
        ArrayList<MasterLocList> loanList = new ArrayList<>();
        Cursor cur = mDatabase.rawQuery("Select * from "
                + AppUtility.TABLE_MASTER_LOC + " where state_code='"
                + locRequest.getStateCode() + "' AND district_code='" + locRequest.getDistrictCode()
                + "' AND tehsil_code='" + locRequest.getTehsilCode() + "' AND vt_code='"
                + locRequest.getVillTownCode() + "' AND ward_code='" + locRequest.getWardCode(), null);
        if (cur != null) {
            if (cur.getCount() > 0) {

                cur.moveToFirst();
                while (cur.isAfterLast() == false) {
                    masterLocList = new MasterLocList();
                    masterLocList.setStateCode(cur.getString(cur.getColumnIndex("state_code")));
                    masterLocList.setStateName(cur.getString(cur.getColumnIndex("state_name")));
                    masterLocList.setDistrictCode(cur.getString(cur.getColumnIndex("district_code")));
                    masterLocList.setDistrictName(cur.getString(cur.getColumnIndex("district_name")));
                    masterLocList.setTehsilCode(cur.getString(cur.getColumnIndex("tehsil_code")));
                    masterLocList.setTehsilName(cur.getString(cur.getColumnIndex("tehsil_name")));
                    masterLocList.setVtCode(cur.getString(cur.getColumnIndex("vt_code")));
                    masterLocList.setVtName(cur.getString(cur.getColumnIndex("vt_name")));
                    masterLocList.setWardCode(cur.getString(cur.getColumnIndex("ward_code")));
                    masterLocList.setRuralUrban(cur.getString(cur.getColumnIndex("rural_urban")));
                    masterLocList.setBlockCode(cur.getString(cur.getColumnIndex("gp_code")));
                    masterLocList.setGpName(cur.getString(cur.getColumnIndex("gp_name")));
                    masterLocList.setMddsStc(cur.getString(cur.getColumnIndex("mdds_stc")));
                    masterLocList.setMddsDtc(cur.getString(cur.getColumnIndex("mdds_dtc")));
                    masterLocList.setMddsSdtc(cur.getString(cur.getColumnIndex("mdds_sdtc")));
                    masterLocList.setMddsPlcn(cur.getString(cur.getColumnIndex("mdds_plcn")));
                    masterLocList.setMddsName(cur.getString(cur.getColumnIndex("mdds_name")));
                    masterLocList.setLocalBodyCode(cur.getString(cur.getColumnIndex("local_body_code")));
                    masterLocList.setLocalBodyName(cur.getString(cur.getColumnIndex("local_body_name")));
                    loanList.add(masterLocList);
                    cur.moveToNext();
                }
            }
            cur.close();

        }
        mDatabase.close();
        dbHelper.close();
        return loanList;
    }


}
