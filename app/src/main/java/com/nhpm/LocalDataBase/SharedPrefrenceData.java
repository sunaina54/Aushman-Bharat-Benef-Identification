package com.nhpm.LocalDataBase;

import android.content.Context;
import android.util.Log;

import com.customComponent.utility.ProjectPrefrence;
import com.nhpm.Models.response.seccMembers.SeccMemberItem;
import com.nhpm.Models.response.seccMembers.SeccMemberResponse;
import com.nhpm.Utility.AppConstant;

import java.util.ArrayList;

/**
 * Created by Anand on 01-11-2016.
 */
public class SharedPrefrenceData {

    public static SeccMemberItem getSeccMemberDetail(String tinNpr,Context context){
        SeccMemberItem item=null;
        SeccMemberResponse response=SeccMemberResponse.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.SECC_MEMBER_CONTENT, context));
        for(SeccMemberItem item1 : response.getSeccMemberList()){
            if(item1.getTinNpr().equalsIgnoreCase(tinNpr)){
                item=item1;
                break;
            }
        }

        return item;
    }
    public static ArrayList<SeccMemberItem> getSeccMemberList(SeccMemberItem selectedMember,Context context){
        ArrayList<SeccMemberItem> memberList=new ArrayList<>();
        SeccMemberResponse response=SeccMemberResponse.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.SECC_MEMBER_CONTENT, context));
        String locStr=selectedMember.getStatecode()+selectedMember.getDistrictcode()+selectedMember.getTehsilcode()
                +selectedMember.getTowncode()+selectedMember.getWardid()
                +selectedMember.getAhlblockno()+selectedMember.getAhlslnohhd();
        for(SeccMemberItem item1 : response.getSeccMemberList()){
            String seccStr=item1.getStatecode()+item1.getDistrictcode()+item1.getTehsilcode()+item1.getTowncode()+item1.getWardid()
                    +item1.getAhlblockno()+item1.getAhlslnohhd();
            if(seccStr.equalsIgnoreCase(locStr)){
               memberList.add(item1);
            }
        }
        return memberList;
    }

    public static ArrayList<SeccMemberItem> getSeccMemberList(String hhdNo,Context context){
        ArrayList<SeccMemberItem> memberList=new ArrayList<>();
        SeccMemberResponse response=SeccMemberResponse.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.SECC_MEMBER_CONTENT, context));
        String locStr=hhdNo;
        Log.d("jkagkj","LocationStraaaaaaaaaa : "+locStr);
        for(SeccMemberItem item1 : response.getSeccMemberList()){

            String seccStr=item1.getHhdNo();
            Log.d("jkagkj","LocationStr : "+seccStr);
            if(seccStr.equalsIgnoreCase(locStr)){

                Log.d("jkagkj","LocationStr Match : "+locStr);
                memberList.add(item1);
            }
        }
        return memberList;
    }
    public static ArrayList<SeccMemberItem> getSeccMemberList(String stateCode,String distCode,String tehsilCode,
                                                              String villTwonCode,String wardCode,
                                                              String blockCode,String ahSlnoHhhd,Context context){
        ArrayList<SeccMemberItem> memberList=new ArrayList<>();
        SeccMemberResponse response=SeccMemberResponse.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.SECC_MEMBER_CONTENT, context));
        String locStr=stateCode+distCode+tehsilCode
                +villTwonCode+wardCode
                +blockCode+ahSlnoHhhd;
        for(SeccMemberItem item1 : response.getSeccMemberList()){
            String seccStr=item1.getStatecode()+item1.getDistrictcode()+item1.getTehsilcode()+item1.getTowncode()+item1.getWardid()
                    +item1.getAhlblockno()+item1.getAhlslnohhd();
            if(seccStr.equalsIgnoreCase(locStr)){
                memberList.add(item1);
            }
        }
        return memberList;
    }

}
