package com.nhpm;

import com.nhpm.LocalDataBase.DatabaseHelpers;
import com.nhpm.ReqRespModels.NhsDataList;

import java.util.ArrayList;

/**
 * Created by psqit on 8/29/2016.
 */
public interface MetaDataDao {
    public long saveInMetaData(NhsDataList item, DatabaseHelpers helper);
    NhsDataList getMetaDataItem(String id, DatabaseHelpers helpers);
    long updateMetaData(NhsDataList item, DatabaseHelpers helper);
    ArrayList<NhsDataList> getPendingEdits(DatabaseHelpers helpers);
    long updateMetaDataFlag(NhsDataList item, DatabaseHelpers helpers);
    public void delete(String id,DatabaseHelpers helpers);
    }
