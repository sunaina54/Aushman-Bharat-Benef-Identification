package com.nhpm;

import com.nhpm.LocalDataBase.DatabaseHelpers;
import com.nhpm.LocalDataBase.dto.MemberRequest;
import com.nhpm.Models.MemberModel;
import com.nhpm.ReqRespModels.NhsDataList;

import java.util.ArrayList;

/**
 * Created by psqit on 8/29/2016.
 */
public interface NHSMasterDao {
    ArrayList<String> getHouseholds(String stateid,String distid,String subdistid,String towncode,String wardid,String blockno,DatabaseHelpers helper);
    long save(NhsDataList item,
              DatabaseHelpers helper);
    public ArrayList<MemberModel> getMembers(String householdId,String towncode,String wardid,String blockno,DatabaseHelpers helpers);
    NhsDataList getMemberDetail(String tin,DatabaseHelpers helpers);
    long update(NhsDataList item,DatabaseHelpers helpers);
    String getTin(String stateid, String distid, String subdistid, String towncode, String wardid, String blockno,String householdID, DatabaseHelpers helper);
    NhsDataList getMemberDetailById(String id,DatabaseHelpers helpers);
     ArrayList<NhsDataList> getMembers1(MemberRequest memberRequest,DatabaseHelpers helpers);
     ArrayList<NhsDataList> getMemberList(MemberRequest memberRequest,DatabaseHelpers helpers);



    }
