package com.nhpm.LocalDataBase.dto;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.nhpm.LocalDataBase.DatabaseHelpers;
import com.nhpm.ReqRespModels.NhsDataList;
import com.nhpm.Utility.AppUtility;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Wahid on 20-09-2016.
 * CREATE TABLE "nhsSyncStat" ("id" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE ,
 * "localId" INTEGER NOT NULL , "remoteId" INTEGER NOT NULL , "oprCode" INTEGER NOT NULL )
 */
public class NhsSyncDTO implements Serializable {
    private String TABLE="nhsSyncStat";
    private long id;
    private long localId;
    private long remoteId;
    private int oprCode;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getLocalId() {
        return localId;
    }

    public void setLocalId(long localId) {
        this.localId = localId;
    }

    public long getRemoteId() {
        return remoteId;
    }

    public void setRemoteId(long remoteId) {
        this.remoteId = remoteId;
    }

    public int getOprCode() {
        return oprCode;
    }

    public void setOprCode(int oprCode) {
        this.oprCode = oprCode;
    }

    public long updateNhsSyncStat(DatabaseHelpers helpers,NhsSyncDTO dto){
        helpers.createOrOpenDatabase();
        ContentValues values = new ContentValues();
        values.put("remoteId ", dto.getRemoteId());
        values.put("localId", dto.getLocalId());
        values.put("oprCode",dto.oprCode);
        SQLiteDatabase db = helpers.getWritableDatabase();
        int updateFlag = db.update(TABLE, values,
                "id=" + dto.getId(), null);
        Log.d("Update LOCAL  Data", " Update Flag Value : " + updateFlag);
        db.close();
        helpers.close();
        return updateFlag;
    }
    public long addNhsSyncStat(DatabaseHelpers helper,NhsSyncDTO dto){
        helper.createOrOpenDatabase();
        ContentValues values = new ContentValues();
        values.put("remoteId ", dto.getRemoteId());
        values.put("localId", dto.getLocalId());
        values.put("oprCode",dto.oprCode);
        SQLiteDatabase db = helper.getWritableDatabase();
        long insertFlag = db.insert(TABLE, null,
                values);
        db.close();
        helper.close();
        return insertFlag;
    }
/*
    public ArrayList<NhsSyncDTO> getAllPendingList(DatabaseHelpers helpers){
        SQLiteDatabase mDatabase=helpers.createOrOpenDatabase();
        NhsSyncDTO dto=null;
        ArrayList<NhsSyncDTO> dtoList=new ArrayList<>();
       */
/* Cursor cur = mDatabase.rawQuery("Select * from "
                + TABLE, null);*//*

        Cursor cur= mDatabase.query(TABLE, null, null, null, null, null, null);
        Log.d(TABLE,"Cursor size : "+cur.getCount());
        if (cur != null) {
            if (cur.getCount() > 0) {
                cur.moveToNext();
                while (cur.isAfterLast() == false) {
                    dto=new NhsSyncDTO();
                    dto.setId(cur.getLong(cur.getColumnIndex("id")));
                    dto.setLocalId(cur.getLong(cur.getColumnIndex("localId")));
                    dto.setRemoteId(cur.getLong(cur.getColumnIndex("remoteId")));
                    dto.setOprCode(cur.getInt(cur.getColumnIndex("oprCode")));
                    dtoList.add(dto);
                }
            }
        }
        mDatabase.close();
        helpers.close();
        return dtoList;
    }
*/

    public ArrayList<NhsSyncDTO> getAllPendingList(DatabaseHelpers helpers){
        SQLiteDatabase mDatabase=helpers.createOrOpenDatabase();
        NhsSyncDTO dto=null;
        ArrayList<NhsSyncDTO> dtoList=new ArrayList<>();
       /* Cursor cur = mDatabase.rawQuery("Select * from "
                + TABLE, null);*/
        Cursor cur= mDatabase.query(TABLE, null, null, null, null, null, null);
        Log.d(TABLE,"Cursor size : "+cur.getCount());


        if (cur != null) {
            if (cur.getCount() > 0) {
                cur.moveToFirst();
                do {
                    dto = new NhsSyncDTO();
                    dto.setId(cur.getLong(cur.getColumnIndex("id")));
                    dto.setLocalId(cur.getLong(cur.getColumnIndex("localId")));
                    dto.setRemoteId(cur.getLong(cur.getColumnIndex("remoteId")));
                    dto.setOprCode(cur.getInt(cur.getColumnIndex("oprCode")));
                    dtoList.add(dto);

                } while (cur.moveToNext());
                if (cur != null && !cur.isClosed())
                    cur.close();
                //cur.close();
            }
        }
        mDatabase.close();
        helpers.close();
        return dtoList;
    }



    public NhsSyncDTO getNHSSyncStat(long localId,DatabaseHelpers helpers){
        SQLiteDatabase mDatabase=helpers.createOrOpenDatabase();
        NhsSyncDTO dto=null;
        Cursor cur = mDatabase.rawQuery("Select * from "
                + TABLE + " where localId='"
                + localId + "' ", null);

      /*  if (cur != null) {
            if (cur.getCount() > 0) {
                dto=new NhsSyncDTO();
                cur.moveToFirst();
                while (cur.isAfterLast() == false) {
                    dto.setId(cur.getLong(cur.getColumnIndex("id")));
                    dto.setLocalId(cur.getLong(cur.getColumnIndex("localId")));
                    dto.setRemoteId(cur.getLong(cur.getColumnIndex("remoteId")));
                    dto.setOprCode(cur.getInt(cur.getColumnIndex("oprCode")));
                }
            }
        }*/

        if (cur != null) {
            if (cur.getCount() > 0) {
                cur.moveToFirst();
                do {
                    dto = new NhsSyncDTO();
                    dto.setId(cur.getLong(cur.getColumnIndex("id")));
                    dto.setLocalId(cur.getLong(cur.getColumnIndex("localId")));
                    dto.setRemoteId(cur.getLong(cur.getColumnIndex("remoteId")));
                    dto.setOprCode(cur.getInt(cur.getColumnIndex("oprCode")));
                   // dtoList.add(dto);

                } while (cur.moveToNext());
                if (cur != null && !cur.isClosed())
                    cur.close();
                //cur.close();
            }
        }
        mDatabase.close();
        helpers.close();
        return dto;
    }

    public boolean deleteNhsSyncDTO(DatabaseHelpers helpers,long id){
        SQLiteDatabase db=helpers.createOrOpenDatabase();
        boolean status=db.delete(TABLE, "id=" + id, null) > 0;
        db.close();
        helpers.close();
        return  status;
    }

}
