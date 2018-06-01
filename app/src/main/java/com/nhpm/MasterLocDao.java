package com.nhpm;

import com.nhpm.LocalDataBase.DatabaseHelpers;
import com.nhpm.LocalDataBase.dto.MemberRequest;
import com.nhpm.Models.MemberModel;
import com.nhpm.ReqRespModels.MasterLocList;

import java.util.ArrayList;

/**
 * Created by psqit on 8/29/2016.
 */
public interface MasterLocDao {
    long save(MasterLocList item,
              DatabaseHelpers helper);
    ArrayList<MasterLocList> getVillages(String stateid,String distid,String subdistid,DatabaseHelpers helper);
   ArrayList<MasterLocList> getWards(String stateid,String distid,String subdistid,String wardcode,DatabaseHelpers helper);
 ArrayList<MasterLocList> getBlocks(String stateid,String distid,String subdistid,String villcode,String wardcode,DatabaseHelpers helper);

    ArrayList<MasterLocList> getMasterLocation(MemberRequest locRequest,DatabaseHelpers dbHelper);
}
