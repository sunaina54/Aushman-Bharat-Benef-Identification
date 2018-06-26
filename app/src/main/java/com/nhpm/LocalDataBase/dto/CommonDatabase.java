package com.nhpm.LocalDataBase.dto;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.nhpm.LocalDataBase.DatabaseHelpers;
import com.nhpm.Models.ApplicationDataModel;
import com.nhpm.Models.DownloadedDataCountModel;
import com.nhpm.Models.NotificationModel;
import com.nhpm.Models.response.ApplicationConfigurationModel;
import com.nhpm.Models.response.master.ConfigurationItem;
import com.nhpm.Models.response.master.MemberRelationItem;
import com.nhpm.Models.response.master.StateItem;
import com.nhpm.Models.response.rsbyMembers.RSBYItem;
import com.nhpm.Models.response.rsbyMembers.RSBYPoliciesItem;
import com.nhpm.Models.response.rsbyMembers.RsbyCardCategoryItem;
import com.nhpm.Models.response.rsbyMembers.RsbyHouseholdItem;
import com.nhpm.Models.response.rsbyMembers.RsbyPoliciesCompany;
import com.nhpm.Models.response.rsbyMembers.RsbyRelationItem;
import com.nhpm.Models.response.seccMembers.HouseHoldItem;
import com.nhpm.Models.response.seccMembers.SeccMemberItem;
import com.nhpm.Utility.AppConstant;
import com.nhpm.Utility.AppUtility;

import java.util.ArrayList;

/**
 * Created by PSQ on 12/23/2016.
 */

public class CommonDatabase {
    private static String TAG = "CommonDatabase";


    public static long save(SeccMemberItem item, Context context) {
        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        ContentValues values = new ContentValues();
        values.put("member_active_status", item.getMember_active_status());
        values.put("printStatus", item.getPrintStatus());
        values.put("nhps_mem_id", item.getNhpsMemId());
        values.put("tin_npr", item.getTinNpr());
        values.put("statecode", item.getStatecode());
        values.put("districtcode", item.getDistrictcode());
        values.put("tehsilcode", item.getTehsilcode());
        values.put("towncode", item.getTowncode());
        values.put("wardid", item.getWardid());
        values.put("blockno", item.getAhlblockno());
        values.put("grampanchayatcode", item.getGrampanchayatcode());
        values.put("grampanchayatname", item.getGrampanchayatname());
        if (item.getAhlblockno() != null)
            values.put("ahlblockno", item.getAhlblockno());
        if (item.getAhlsubblockno() != null)
            values.put("ahlsubblockno", item.getAhlsubblockno());
        values.put("slnohhd", item.getSlnohhd());
        values.put("slnomember", item.getSlnomember());
        values.put("ahlslnohhd", item.getAhlslnohhd());
        values.put("ahltypeofeb", item.getAhltypeofeb());
        values.put("typeofhhd", item.getTypeofhhd());
        values.put("livinginshelter", item.getLivinginshelter());
        values.put("name", item.getName());
        //String nameRel = item.getNameSl();
        // System.out.print(nameRel);
        values.put("name_sl", item.getNameSl());

        values.put("relation", item.getRelation());
        values.put("relation_sl", item.getRelationSl());
        values.put("genderid", item.getGenderid());
        values.put("dob", item.getDob());
        values.put("mstatusid", item.getMstatusid());
        values.put("fathername", item.getFathername());
        values.put("fathername_sl", item.getFathernameSl());
        values.put("mothername", item.getMothername());
        values.put("mothername_sl", item.getMothernameSl());
        values.put("occupation", item.getOccupation());
        values.put("occupation_sl", item.getOccupationSl());
        values.put("educode", item.getEducode());
        values.put("education_other", item.getEducationOther());
        values.put("disabilitycode", item.getDisabilitycode());
        values.put("caste_group", item.getCasteGroup());
        values.put("ahl_tin", item.getAhlTin());
        //  values.put("member_status", item.getMemberStatus());
        values.put("addressline1", item.getAddressline1());
        values.put("addressline2", item.getAddressline2());
        values.put("addressline3", item.getAddressline3());
        values.put("addressline4", item.getAddressline4());
        values.put("addressline5", item.getAddressline5());
        values.put("pincode", item.getPincode());
        values.put("addressline1sl", item.getAddressline1Sl());
        values.put("addressline2sl", item.getAddressline2Sl());
        values.put("addressline3sl", item.getAddressline3Sl());
        values.put("addressline4sl", item.getAddressline4Sl());
        values.put("addressline5sl", item.getAddressline5Sl());
        values.put("dob_frm_npr", item.getDobFrmNpr());
        values.put("spousenm", item.getSpousenm());
        values.put("hhd_no", item.getHhdNo());
        if (item.getAadhaarNo() != null) {
            values.put("aadhaar_no", item.getAadhaarNo());
        }
        values.put("name_aadhaar", item.getNameAadhaar());
        values.put("urn_no", item.getUrnNo());
        values.put("mobile_no", item.getMobileNo());
        values.put("rural_urban", item.getRuralUrban());
        values.put("bank_ifsc", item.getNhpsRelationCode());
        values.put("mark_for_deletion", item.getMarkForDeletion());
        values.put("aadhaar_status", item.getAadhaarStatus());
        //AppUtility.showLog(AppConstant.LOG_STATUS, TAG, " Household Status : " + item.getHhStatus());
        values.put("hh_status", item.getHhStatus());
        values.put("mem_status", item.getMemStatus());
        //AppUtility.showLog(AppConstant.LOG_STATUS, TAG, " Member Status : " + item.getMemStatus());
        values.put("eid", item.getEid());
        values.put("bank_acc_no", item.getNhpsRelationName());
        values.put("scheme_id", item.getSchemeId());
        values.put("scheme_no", item.getSchemeNo());
        values.put("name_nominee", item.getNameNominee());
        values.put("relation_nominee_code", item.getRelationNomineeCode());
        values.put("nominee_relation_name", item.getNomineeRelationName());
        //AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Nominee Relation Name : " + item.getNomineeRelationName());
        values.put("id_type", item.getIdType());
        values.put("id_no", item.getIdNo());
        values.put("name_as_id", item.getNameAsId());
        values.put("member_photo1", item.getMemberPhoto1());
        //Log.d("Secc Data base", "ID Image : " + item.getGovtIdPhoto());
        values.put("govt_id_photo1", item.getGovtIdPhoto());

        // values.put("id_photo1",item.getIdPhoto1()!=null ? item.getIdPhoto1().getBytes() : null);
                /*"values.put("id_photo2 bytea,
                        values.put("member_photo bytea,*/
        //AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Capture Mode : " + item.getAadhaarCapturingMode());
        values.put("aadhaar_capturing_mode", item.getAadhaarCapturingMode());
        values.put("consent", item.getConsent());
        values.put("aadhaar_auth_mode", item.getAadhaarAuthMode());
        values.put("aadhaar_auth_dt", item.getAadhaarAuthDt());
        values.put("aadhaar_verified_by", item.getAadhaarVerifiedBy());
        values.put("userid", item.getUserid());
        values.put("date_updated", item.getDateUpdated());
        values.put("aadhaar_auth", item.getAadhaarAuth());
        values.put("mobile_auth", item.getMobileAuth());
        values.put("urn_auth", item.getUrnAuth());
        values.put("ifsc_auth", item.getIfscAuth());
        values.put("bank_acc_auth", item.getBankAccAuth());
        values.put("state_scheme_code_auth", item.getStateSchemeCodeAuth());
        values.put("scheme_code", item.getSchemeCode());
        values.put("data_source", item.getDataSource());
        values.put("nhps_id", item.getNhpsId());
        values.put("mobile_auth_dt", item.getMobileAuthDt());
        values.put("urn_auth_dt", item.getUrnAuthDt());
        values.put("ifsc_auth_dt", item.getIfscAuthDt());
        values.put("bank_acc_auth_dt", item.getBankAccAuthDt());
        values.put("state_scheme_code_auth_dt", item.getStateSchemeCodeAuthDt());
        values.put("consent_dt", item.getConsentDt());
        values.put("aadhaar_gender", item.getAadhaarGender());
        values.put("aadhaar_yob", item.getAadhaarYob());
        values.put("aadhaar_dob", item.getAadhaarDob());
        values.put("aadhaar_co", item.getAadhaarCo());
        values.put("aadhaar_gname", item.getAadhaarGname());
        values.put("aadhaar_house", item.getAadhaarHouse());
        values.put("aadhaar_street", item.getAadhaarStreet());
        values.put("aadhaar_loc", item.getAadhaarLoc());
        values.put("aadhaar_vtc", item.getAadhaarVtc());
        values.put("aadhaar_po", item.getAadhaarPo());
        values.put("aadhaar_dist", item.getAadhaarDist());
        values.put("aadhaar_subdist", item.getAadhaarSubdist());
        values.put("aadhaar_state", item.getAadhaarState());
        values.put("aadhaar_pc", item.getAadhaarPc());
        values.put("aadhaar_lm", item.getNhpsRelationName());
        values.put("latitude", item.getLatitude());
        values.put("longitude", item.getLongitude());
        values.put("whose_mobile", item.getWhoseMobile());
        values.put("scheme_id3", item.getSchemeId3());
        values.put("scheme_id2", item.getSchemeId2());
        values.put("scheme_id1", item.getSchemeId1());
        values.put("scheme_no3", item.getSchemeNo3());
        values.put("scheme_no2", item.getSchemeNo2());
        values.put("scheme_no1", item.getSchemeNo1());
        //  Log.d("Secc Databs"," Relation Code :"+item.getNhpsRelationCode());
        values.put("nhps_relation_code", item.getNhpsRelationCode());
        values.put("nhps_relation_name", item.getNhpsRelationName());
       /* values.put("req_name", item.getReqName());
        values.put("req_relation_name",item.getReqRelationName());
        values.put("req_relation_code", item.getReqRelationCode());
        values.put("req_father_name", item.getReqFatherName());
        values.put("req_mother_name", item.getReqMotherName());
        values.put("req_dob", item.getReqDOB());
        values.put("req_gender_id", item.getReqGenderCode());
        values.put("req_marital_status_id", item.getReqMarritalStatCode());
        values.put("req_occupation", item.getReqOccupation());*/
        values.put("locked_save", item.getLockedSave());
       /* values.put("govt_id_survey_status", item.getGovtIdSurveyedStat());
        values.put("aadhar_survey_status", item.getAadhaarSurveyedStat());
        values.put("photo_capture_status", item.getPhotoSurveyedStatus());
        values.put("mobile_surveyed_status", item.getMobileNoSurveyedStat());
        values.put("nominee_surveyed_status", item.getNomineeDetailSurveyedStat());*/
        values.put("app_version", item.getAppVersion());
        values.put("synced_status", item.getSyncedStatus());
        // updated by saurabh
        values.put("data_source", item.getDataSource());
        //RSBY ITEM DATA
        if (item.getSyncDt() != null) {
            values.put("sync_dt", item.getSyncDt());
        }
        values.put("rsby_rsbyMemId", item.getRsbyMemId());
        if (item.getRsbyUrnId() != null) {
            values.put("rsby_urnId", item.getRsbyUrnId());
        } else {
            values.put("rsby_urnId", item.getUrnId());

        }
        if (item.getRsbyInsccode() != null) {
            values.put("rsby_insccode", item.getRsbyInsccode());
        } else {
            values.put("rsby_insccode", item.getInscCode());
        }
        if (item.getRsbyPolicyno() != null) {
            values.put("rsby_policyno", item.getRsbyPolicyno());
        } else {
            values.put("rsby_policyno", item.getPolicyNo());

        }
        if (item.getRsbyPolicyamt() != null) {
            values.put("rsby_policyamt", item.getRsbyPolicyamt());
        } else {
            values.put("rsby_policyamt", item.getPolicyAmount());
        }
        if (item.getRsbyStartdate() != null) {
            values.put("rsby_startdate", item.getRsbyStartdate());
        } else {

            values.put("rsby_startdate", item.getStartDate());
        }
        if (item.getRsbyEnddate() != null) {
            values.put("rsby_enddate", item.getRsbyEnddate());
        } else {
            values.put("rsby_enddate", item.getEndDate());

        }

        if (item.getRsbyIssuesTimespam() != null) {
            values.put("rsby_issuesTimespam", item.getRsbyIssuesTimespam());
        } else {

            values.put("rsby_issuesTimespam", item.getIssueTimeStamp());
        }
        if (item.getRsbyHofnamereg() != null) {
            values.put("rsby_hofnamereg", item.getRsbyHofnamereg());
        } else {
            values.put("rsby_hofnamereg", item.getHofNameReg());
        }
        if (item.getRsbyDoorhouse() != null) {
            values.put("rsby_doorhouse", item.getRsbyDoorhouse());
        } else {
            values.put("rsby_doorhouse", item.getRsbyDoorhouse());
        }
        if (item.getRsbyVillageCode() != null) {
            values.put("rsby_villageCode", item.getRsbyVillageCode());
        } else {
            values.put("rsby_villageCode", item.getVillageCode());

        }
        if (item.getRsbyPanchyatTownCode() != null) {
            values.put("rsby_panchyatTownCode", item.getRsbyPanchyatTownCode());
        } else {

            values.put("rsby_panchyatTownCode", item.getPanchayatTownCode());
        }
        // values.put("rsby_blockCode", item.getRsbyBlockCode());
        values.put("rsby_blockCode", item.getRsbyBlockCode());
        //values.put("rsby_districtCode", item.getRsbyDistrictCode());
        values.put("rsby_districtCode", item.getRsbyDistrictCode());
        //values.put("rsby_stateCode", item.getRsbyStateCode());
        //values.put("rsby_stateCode", item.getRsbyStateCode());
        if (item.getRsbyMemid() != null) {
            values.put("rsby_memid", item.getRsbyMemid());
        } else {
            values.put("rsby_memid", item.getRsbyMemId());
        }

        values.put("rsby_relcode", item.getRsbyRelcode());
        // values.put("rsby_relcode", item.getRsbyRelcode());
        if (item.getRsbyCsmNo() != null) {
            values.put("rsby_csmNo", item.getRsbyCsmNo());
        } else {
            values.put("rsby_csmNo", item.getCsmNo());
        }
        if (item.getRsbyCardType() != null) {
            values.put("rsby_cardType", item.getRsbyCardType());
        } else {
            values.put("rsby_cardType", item.getCardType());
        }
        if (item.getRsbyCardCategory() != null) {
            values.put("rsby_cardCategory", item.getRsbyCardCategory());
        } else {
            values.put("rsby_cardCategory", item.getCardCategory());

        }
        if (item.getRsbyCardMenutiaDetail() != null) {
            values.put("rsby_cardMenutiaDetail", item.getRsbyCardMenutiaDetail());
        } else {
            values.put("rsby_cardMenutiaDetail", item.getRsbyCardMenutiaDetail());
        }
        if (item.getRsbyName() != null) {
            values.put("rsby_name", item.getRsbyName());
        } else {
            values.put("rsby_name", item.getName());
        }
        values.put("rsby_dob", item.getRsbyDob());
        if (item.getRsbyGender() != null) {
            values.put("rsby_gender", item.getRsbyGender());
        } else {
            values.put("rsby_gender", item.getGenderid());
        }
        values.put("nominee_guardian_name", item.getNomineeGaurdianName());

        SQLiteDatabase db = helpers.getWritableDatabase();
        long insertFlag = db.insert(AppConstant.TRANSECT_POPULATE_SECC, null,
                values);
        Log.d("MEMBER ", "ADDED" + insertFlag);
        db.close();
        helpers.close();
        return insertFlag;
    }

    public static long saveRelationMaster(MemberRelationItem item, Context context) {
        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        ContentValues values = new ContentValues();
        values.put("relation_code", item.getRelationCode());
        values.put("relation_name", item.getRelationName());
        values.put("active_status", item.getActiveStatus());
        values.put("relation_gender", item.getRelationGender());
        values.put("display_order", item.getDisplayOrder());
        values.put("mdds_rcode", item.getMddsRCode());
        SQLiteDatabase db = helpers.getWritableDatabase();
        long insertFlag = db.insert(AppConstant.RELATION_TABLE, null,
                values);
        Log.d("RELATION ", "ADDED" + insertFlag);
        db.close();
        helpers.close();
        return insertFlag;
    }

    public static SeccMemberItem getSeccMember(Context context, String query) {
        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        SQLiteDatabase mDatabase = helpers.createOrOpenDatabase();
        SeccMemberItem item = null;
        //  lllll
//        ArrayList<SeccMemberItem> seccMemberItems=new ArrayList<>();
        Cursor cur = mDatabase.rawQuery(query, null);
        // Log.d("SECC Data ", " Query : " + cur.getCount());
        if (cur != null) {
            if (cur.getCount() > 0) {
                cur.moveToFirst();
                while (cur.isAfterLast() == false) {
                    item = new SeccMemberItem();
                    item.setMember_active_status(cur.getString(cur.getColumnIndex("member_active_status")));
                    item.setNhpsMemId(cur.getString(cur.getColumnIndex("nhps_mem_id")));
                    item.setTinNpr(cur.getString(cur.getColumnIndex("tin_npr")));
                    item.setStatecode(cur.getString(cur.getColumnIndex("statecode")));
                    item.setDistrictcode(cur.getString(cur.getColumnIndex("districtcode")));
                    item.setTehsilcode(cur.getString(cur.getColumnIndex("tehsilcode")));
                    item.setTowncode(cur.getString(cur.getColumnIndex("towncode")));
                    item.setWardid(cur.getString(cur.getColumnIndex("wardid")));
                    item.setPrintStatus(cur.getString(cur.getColumnIndex("printStatus")));
                    item.setAhlblockno(cur.getString(cur.getColumnIndex("blockno")));
                    item.setGrampanchayatcode(cur.getString(cur.getColumnIndex("grampanchayatcode")));
                    item.setGrampanchayatname(cur.getString(cur.getColumnIndex("grampanchayatname")));
                    item.setAhlblockno(cur.getString(cur.getColumnIndex("ahlblockno")));
                    item.setAhlsubblockno(cur.getString(cur.getColumnIndex("ahlsubblockno")));
                    item.setSlnohhd(cur.getString(cur.getColumnIndex("slnohhd")));
                    item.setSlnomember(cur.getString(cur.getColumnIndex("slnomember")));
                    item.setAhlslnohhd(cur.getString(cur.getColumnIndex("ahlslnohhd")));
                    item.setAhltypeofeb(cur.getString(cur.getColumnIndex("ahltypeofeb")));
                    item.setTypeofhhd(cur.getString(cur.getColumnIndex("typeofhhd")));
                    item.setLivinginshelter(cur.getString(cur.getColumnIndex("livinginshelter")));
                    item.setName(cur.getString(cur.getColumnIndex("name")));
                    item.setNameSl(cur.getString(cur.getColumnIndex("name_sl")));
                    item.setRelation(cur.getString(cur.getColumnIndex("relation")));
                    item.setRelationSl(cur.getString(cur.getColumnIndex("relation_sl")));
                    item.setGenderid(cur.getString(cur.getColumnIndex("genderid")));
                    item.setDob(cur.getString(cur.getColumnIndex("dob")));
                    item.setMstatusid(cur.getString(cur.getColumnIndex("mstatusid")));
                    item.setFathername(cur.getString(cur.getColumnIndex("fathername")));
                    item.setFathernameSl(cur.getString(cur.getColumnIndex("fathername_sl")));
                    item.setMothername(cur.getString(cur.getColumnIndex("mothername")));
                    item.setMothernameSl(cur.getString(cur.getColumnIndex("mothername_sl")));
                    item.setOccupation(cur.getString(cur.getColumnIndex("occupation")));
                    item.setOccupationSl(cur.getString(cur.getColumnIndex("occupation_sl")));
                    item.setEducode(cur.getString(cur.getColumnIndex("educode")));
                    item.setEducationOther(cur.getString(cur.getColumnIndex("education_other")));
                    item.setDisabilitycode(cur.getString(cur.getColumnIndex("disabilitycode")));
                    item.setCasteGroup(cur.getString(cur.getColumnIndex("caste_group")));
                    item.setAhlTin(cur.getString(cur.getColumnIndex("ahl_tin")));
                    // item.setMemberStatus(cur.getString(cur.getColumnIndex("member_status")));
                    item.setAddressline1(cur.getString(cur.getColumnIndex("addressline1")));
                    item.setAddressline2(cur.getString(cur.getColumnIndex("addressline2")));
                    item.setAddressline3(cur.getString(cur.getColumnIndex("addressline3")));
                    item.setAddressline4(cur.getString(cur.getColumnIndex("addressline4")));
                    item.setAddressline5(cur.getString(cur.getColumnIndex("addressline5")));
                    item.setPincode(cur.getString(cur.getColumnIndex("pincode")));

                    item.setSchemeId1(cur.getString(cur.getColumnIndex("scheme_id1")));
                    item.setSchemeId2(cur.getString(cur.getColumnIndex("scheme_id2")));
                    item.setSchemeId3(cur.getString(cur.getColumnIndex("scheme_id3")));
                    item.setSchemeNo1(cur.getString(cur.getColumnIndex("scheme_no1")));
                    item.setSchemeNo2(cur.getString(cur.getColumnIndex("scheme_no2")));
                    item.setSchemeNo3(cur.getString(cur.getColumnIndex("scheme_no3")));

                    item.setAddressline1Sl(cur.getString(cur.getColumnIndex("addressline1sl")));
                    item.setAddressline2Sl(cur.getString(cur.getColumnIndex("addressline2sl")));
                    item.setAddressline3Sl(cur.getString(cur.getColumnIndex("addressline3sl")));
                    item.setAddressline4Sl(cur.getString(cur.getColumnIndex("addressline4sl")));
                    item.setAddressline5Sl(cur.getString(cur.getColumnIndex("addressline5sl")));
                    item.setDobFrmNpr(cur.getString(cur.getColumnIndex("dob_frm_npr")));
                    item.setSpousenm(cur.getString(cur.getColumnIndex("spousenm")));
                    item.setHhdNo(cur.getString(cur.getColumnIndex("hhd_no")));
                    item.setAadhaarNo(cur.getString(cur.getColumnIndex("aadhaar_no")));
                    item.setNameAadhaar(cur.getString(cur.getColumnIndex("name_aadhaar")));
                    item.setUrnNo(cur.getString(cur.getColumnIndex("urn_no")));
                    item.setMobileNo(cur.getString(cur.getColumnIndex("mobile_no")));
                    item.setRuralUrban(cur.getString(cur.getColumnIndex("rural_urban")));
                    item.setBankIfsc(cur.getString(cur.getColumnIndex("bank_ifsc")));
                    item.setMarkForDeletion(cur.getString(cur.getColumnIndex("mark_for_deletion")));
                    item.setAadhaarStatus(cur.getString(cur.getColumnIndex("aadhaar_status")));
                    item.setHhStatus(cur.getString(cur.getColumnIndex("hh_status")));
                    item.setMemStatus(cur.getString(cur.getColumnIndex("mem_status")));
                    item.setEid(cur.getString(cur.getColumnIndex("eid")));
                    item.setBankAccNo(cur.getString(cur.getColumnIndex("bank_acc_no")));
                    item.setSchemeId(cur.getString(cur.getColumnIndex("scheme_id")));
                    item.setSchemeNo(cur.getString(cur.getColumnIndex("scheme_no")));
                    item.setNameNominee(cur.getString(cur.getColumnIndex("name_nominee")));
                    item.setIdType(cur.getString(cur.getColumnIndex("id_type")));
                    item.setIdNo(cur.getString(cur.getColumnIndex("id_no")));
                    item.setNameAsId(cur.getString(cur.getColumnIndex("name_as_id")));
                    /*if(cur.getBlob(cur.getColumnIndex("id_photo1"))!=null) {
                        item.setIdPhoto1(AppUtility.convertBitmapToString(BitmapFactory.decodeByteArray(cur.getBlob(cur.getColumnIndex("id_photo1")),
                                0, cur.getBlob(cur.getColumnIndex("id_photo1")).length)));
                    }*/
                    String img = cur.getString(cur.getColumnIndex("govt_id_photo1"));
                    Log.d("Secc Database", "Govt ID Img111111111111 : " + img);
                    item.setGovtIdPhoto(cur.getString(cur.getColumnIndex("govt_id_photo1")));
                    // item.set(cur.getString(cur.getColumnIndex("id_photo2")));
                    // item.set(cur.getString(cur.getColumnIndex("member_photo")));
                    item.setAadhaarCapturingMode(cur.getString(cur.getColumnIndex("aadhaar_capturing_mode")));
                    item.setConsent(cur.getString(cur.getColumnIndex("consent")));
                    item.setAadhaarAuthMode(cur.getString(cur.getColumnIndex("aadhaar_auth_mode")));
                    item.setAadhaarAuthDt(cur.getString(cur.getColumnIndex("aadhaar_auth_dt")));
                    item.setAadhaarVerifiedBy(cur.getString(cur.getColumnIndex("aadhaar_verified_by")));
                    item.setUserid(cur.getString(cur.getColumnIndex("userid")));
                    item.setDateUpdated(cur.getString(cur.getColumnIndex("date_updated")));
                    item.setAadhaarAuth(cur.getString(cur.getColumnIndex("aadhaar_auth")));
                    item.setMobileAuth(cur.getString(cur.getColumnIndex("mobile_auth")));
                    item.setUrnAuth(cur.getString(cur.getColumnIndex("urn_auth")));
                    item.setIfscAuth(cur.getString(cur.getColumnIndex("ifsc_auth")));
                    item.setBankAccAuth(cur.getString(cur.getColumnIndex("bank_acc_auth")));
                    item.setStateSchemeCodeAuth(cur.getString(cur.getColumnIndex("state_scheme_code_auth")));
                    item.setSchemeCode(cur.getString(cur.getColumnIndex("scheme_code")));
                    item.setDataSource(cur.getString(cur.getColumnIndex("data_source")));
                    item.setNhpsId(cur.getString(cur.getColumnIndex("nhps_id")));
                    item.setMobileAuthDt(cur.getString(cur.getColumnIndex("mobile_auth_dt")));
                    item.setUrnAuthDt(cur.getString(cur.getColumnIndex("urn_auth_dt")));
                    item.setIfscAuthDt(cur.getString(cur.getColumnIndex("ifsc_auth_dt")));
                    item.setBankAccAuthDt(cur.getString(cur.getColumnIndex("bank_acc_auth_dt")));
                    item.setStateSchemeCodeAuthDt(cur.getString(cur.getColumnIndex("state_scheme_code_auth_dt")));
                    item.setConsentDt(cur.getString(cur.getColumnIndex("consent_dt")));
                    item.setAadhaarGender(cur.getString(cur.getColumnIndex("aadhaar_gender")));
                    item.setAadhaarYob(cur.getString(cur.getColumnIndex("aadhaar_yob")));
                    item.setAadhaarDob(cur.getString(cur.getColumnIndex("aadhaar_dob")));
                    item.setAadhaarCo(cur.getString(cur.getColumnIndex("aadhaar_co")));
                    item.setAadhaarGname(cur.getString(cur.getColumnIndex("aadhaar_gname")));
                    item.setAadhaarHouse(cur.getString(cur.getColumnIndex("aadhaar_house")));
                    item.setAadhaarStreet(cur.getString(cur.getColumnIndex("aadhaar_street")));
                    item.setAadhaarLoc(cur.getString(cur.getColumnIndex("aadhaar_loc")));
                    item.setAadhaarVtc(cur.getString(cur.getColumnIndex("aadhaar_vtc")));
                    item.setAadhaarPo(cur.getString(cur.getColumnIndex("aadhaar_po")));
                    item.setAadhaarDist(cur.getString(cur.getColumnIndex("aadhaar_dist")));
                    item.setAadhaarSubdist(cur.getString(cur.getColumnIndex("aadhaar_subdist")));
                    item.setAadhaarState(cur.getString(cur.getColumnIndex("aadhaar_state")));
                    item.setAadhaarPc(cur.getString(cur.getColumnIndex("aadhaar_pc")));
                    item.setAadhaarLm(cur.getString(cur.getColumnIndex("aadhaar_lm")));
                    item.setLatitude(cur.getString(cur.getColumnIndex("latitude")));
                    item.setLongitude(cur.getString(cur.getColumnIndex("longitude")));
                    item.setWhoseMobile(cur.getString(cur.getColumnIndex("whose_mobile")));
                    item.setMemberPhoto1(cur.getString(cur.getColumnIndex("member_photo1")));

                    item.setNhpsRelationCode(cur.getString(cur.getColumnIndex("nhps_relation_code")));
                    item.setNhpsRelationName(cur.getString(cur.getColumnIndex("nhps_relation_name")));
                    item.setNomineeRelationName(cur.getString(cur.getColumnIndex("nominee_relation_name")));
                 /*   item.setReqName(cur.getString(cur.getColumnIndex("req_name")));
                    item.setReqRelationCode(cur.getString(cur.getColumnIndex("req_relation_code")));
                    item.setReqRelationName(cur.getString(cur.getColumnIndex("req_relation_name")));
                    item.setReqFatherName(cur.getString(cur.getColumnIndex("req_father_name")));
                    item.setReqMotherName(cur.getString(cur.getColumnIndex("req_mother_name")));
                    item.setReqDOB(cur.getString(cur.getColumnIndex("req_dob")));
                    item.setReqGenderCode(cur.getString(cur.getColumnIndex("req_gender_id")));
                    item.setReqMarritalStatCode(cur.getString(cur.getColumnIndex("req_marital_status_id")));
                    item.setReqOccupation(cur.getString(cur.getColumnIndex("req_occupation")));*/
                    item.setLockedSave(cur.getString(cur.getColumnIndex("locked_save")));

                    /*item.setGovtIdSurveyedStat(cur.getString(cur.getColumnIndex("govt_id_survey_status")));
                    item.setAadhaarSurveyedStat(cur.getString(cur.getColumnIndex("aadhar_survey_status")));
                    item.setPhotoSurveyedStatus(cur.getString(cur.getColumnIndex("photo_capture_status")));
                    item.setMobileNoSurveyedStat(cur.getString(cur.getColumnIndex("mobile_surveyed_status")));
                    item.setNomineeDetailSurveyedStat(cur.getString(cur.getColumnIndex("nominee_surveyed_status")));
                */
                    item.setSyncedStatus(cur.getString(cur.getColumnIndex("synced_status")));


                    String hhStatus = cur.getString(cur.getColumnIndex("hh_status"));
                    String memStatus = cur.getString(cur.getColumnIndex("mem_status"));
                    item.setLockedSave(cur.getString(cur.getColumnIndex("locked_save")));
                    item.setAadhaarStatus(cur.getString(cur.getColumnIndex("aadhaar_status")));
                    String syncStatus = cur.getString(cur.getColumnIndex("synced_status"));
                    item.setRelationNomineeCode(cur.getString(cur.getColumnIndex("relation_nominee_code")));
                    item.setSyncDt(cur.getString(cur.getColumnIndex("sync_dt")));
                    item.setError_type(cur.getString(cur.getColumnIndex("error_type")));
                    item.setError_code(cur.getString(cur.getColumnIndex("error_msg")));
                    item.setError_msg(cur.getString(cur.getColumnIndex("error_msg")));
                    item.setAppVersion(cur.getString(cur.getColumnIndex("app_version")));
                    //  String
                    if (memStatus != null) {
                        item.setMemStatus(memStatus);
                    }
                    if (hhStatus != null && !hhStatus.equalsIgnoreCase("")) {
                        item.setHhStatus(hhStatus);
                    }
                    if (syncStatus != null && !syncStatus.equalsIgnoreCase("")) {
                        item.setSyncedStatus(syncStatus);
                    }
                    item.setRsbyMemId(cur.getString(cur.getColumnIndex("rsby_rsbyMemId")));
                    item.setRsbyUrnId(cur.getString(cur.getColumnIndex("rsby_urnId")));
                    item.setRsbyInsccode(cur.getString(cur.getColumnIndex("rsby_insccode")));
                    item.setRsbyPolicyno(cur.getString(cur.getColumnIndex("rsby_policyno")));
                    item.setRsbyPolicyamt(cur.getString(cur.getColumnIndex("rsby_policyamt")));
                    item.setRsbyStartdate(cur.getString(cur.getColumnIndex("rsby_startdate")));
                    item.setRsbyEnddate(cur.getString(cur.getColumnIndex("rsby_enddate")));
                    item.setRsbyIssuesTimespam(cur.getString(cur.getColumnIndex("rsby_issuesTimespam")));
                    item.setRsbyHofnamereg(cur.getString(cur.getColumnIndex("rsby_hofnamereg")));
                    item.setRsbyDoorhouse(cur.getString(cur.getColumnIndex("rsby_doorhouse")));
                    item.setRsbyVillageCode(cur.getString(cur.getColumnIndex("rsby_villageCode")));
                    item.setRsbyPanchyatTownCode(cur.getString(cur.getColumnIndex("rsby_panchyatTownCode")));
                    item.setRsbyBlockCode(cur.getString(cur.getColumnIndex("rsby_blockCode")));
                    item.setRsbyDistrictCode(cur.getString(cur.getColumnIndex("rsby_districtCode")));
                    item.setRsbyMemid(cur.getString(cur.getColumnIndex("rsby_memid")));
                    item.setRsbyName(cur.getString(cur.getColumnIndex("rsby_name")));
                    item.setRsbyDob(cur.getString(cur.getColumnIndex("rsby_dob")));
                    item.setRsbyGender(cur.getString(cur.getColumnIndex("rsby_gender")));
                    item.setRsbyRelcode(cur.getString(cur.getColumnIndex("rsby_relcode")));
                    item.setRsbyCsmNo(cur.getString(cur.getColumnIndex("rsby_csmNo")));
                    item.setRsbyCardType(cur.getString(cur.getColumnIndex("rsby_cardType")));
                    item.setRsbyCardCategory(cur.getString(cur.getColumnIndex("rsby_cardCategory")));
                    item.setRsbyStateCode(cur.getString(cur.getColumnIndex("rsby_stateCode")));
                    item.setRsbyCardMenutiaDetail(cur.getString(cur.getColumnIndex("rsby_cardMenutiaDetail")));
                    item.setNomineeGaurdianName(cur.getString(cur.getColumnIndex("nominee_guardian_name")));

                    // values.put("nominee_guardian_name ",item.getNomineeGaurdianName());

                    cur.moveToNext();
                }
            }
            cur.close();

        }
        mDatabase.close();
        helpers.close();
        return item;
    }

    public static ArrayList<SeccMemberItem> getSeccMemberList(Context context, String query) {

        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        SQLiteDatabase mDatabase = helpers.createOrOpenDatabase();
        ArrayList<SeccMemberItem> seccMemberItems = new ArrayList<>();
        Cursor cur = mDatabase.rawQuery(query, null);
        Log.d("SECC Data ", " Query : " + query + " count :" + cur.getCount());
        if (cur != null) {
            if (cur.getCount() > 0) {
                cur.moveToFirst();
                while (cur.isAfterLast() == false) {
                    SeccMemberItem item = new SeccMemberItem();
                    item.setMember_active_status(cur.getString(cur.getColumnIndex("member_active_status")));
                    item.setPrintStatus(cur.getString(cur.getColumnIndex("printStatus")));
                    item.setNhpsMemId(cur.getString(cur.getColumnIndex("nhps_mem_id")));
                    item.setTinNpr(cur.getString(cur.getColumnIndex("tin_npr")));
                    item.setStatecode(cur.getString(cur.getColumnIndex("statecode")));
                    item.setDistrictcode(cur.getString(cur.getColumnIndex("districtcode")));
                    item.setTehsilcode(cur.getString(cur.getColumnIndex("tehsilcode")));
                    item.setTowncode(cur.getString(cur.getColumnIndex("towncode")));
                    item.setWardid(cur.getString(cur.getColumnIndex("wardid")));
                    item.setAhlblockno(cur.getString(cur.getColumnIndex("blockno")));
                    item.setGrampanchayatcode(cur.getString(cur.getColumnIndex("grampanchayatcode")));
                    item.setGrampanchayatname(cur.getString(cur.getColumnIndex("grampanchayatname")));
                    item.setAhlblockno(cur.getString(cur.getColumnIndex("ahlblockno")));
                    item.setAhlsubblockno(cur.getString(cur.getColumnIndex("ahlsubblockno")));
                    item.setSlnohhd(cur.getString(cur.getColumnIndex("slnohhd")));
                    item.setSlnomember(cur.getString(cur.getColumnIndex("slnomember")));
                    item.setAhlslnohhd(cur.getString(cur.getColumnIndex("ahlslnohhd")));
                    item.setName(cur.getString(cur.getColumnIndex("name")));
                    item.setNameSl(cur.getString(cur.getColumnIndex("name_sl")));
                    item.setRelation(cur.getString(cur.getColumnIndex("relation")));
                    item.setRelationSl(cur.getString(cur.getColumnIndex("relation_sl")));
                    item.setGenderid(cur.getString(cur.getColumnIndex("genderid")));
                    item.setDob(cur.getString(cur.getColumnIndex("dob")));
                    item.setMstatusid(cur.getString(cur.getColumnIndex("mstatusid")));
                    item.setFathername(cur.getString(cur.getColumnIndex("fathername")));
                    item.setFathernameSl(cur.getString(cur.getColumnIndex("fathername_sl")));
                    item.setMothername(cur.getString(cur.getColumnIndex("mothername")));
                    item.setMothernameSl(cur.getString(cur.getColumnIndex("mothername_sl")));
                    item.setOccupation(cur.getString(cur.getColumnIndex("occupation")));
                    item.setOccupationSl(cur.getString(cur.getColumnIndex("occupation_sl")));
                    //   item.setMemberStatus(cur.getString(cur.getColumnIndex("member_status")));
                    item.setAddressline1(cur.getString(cur.getColumnIndex("addressline1")));
                    item.setAddressline2(cur.getString(cur.getColumnIndex("addressline2")));
                    item.setAddressline3(cur.getString(cur.getColumnIndex("addressline3")));
                    item.setAddressline4(cur.getString(cur.getColumnIndex("addressline4")));
                    item.setAddressline5(cur.getString(cur.getColumnIndex("addressline5")));
                    item.setSchemeId1(cur.getString(cur.getColumnIndex("scheme_id1")));
                    item.setSchemeId2(cur.getString(cur.getColumnIndex("scheme_id2")));
                    item.setSchemeId3(cur.getString(cur.getColumnIndex("scheme_id3")));
                    item.setSchemeNo1(cur.getString(cur.getColumnIndex("scheme_no1")));
                    item.setSchemeNo2(cur.getString(cur.getColumnIndex("scheme_no2")));
                    item.setSchemeNo3(cur.getString(cur.getColumnIndex("scheme_no3")));
                    item.setPincode(cur.getString(cur.getColumnIndex("pincode")));
                    item.setAhlTin(cur.getString(cur.getColumnIndex("ahl_tin")));
                    item.setDobFrmNpr(cur.getString(cur.getColumnIndex("dob_frm_npr")));

                    item.setAhltypeofeb(cur.getString(cur.getColumnIndex("ahltypeofeb")));
                    item.setTypeofhhd(cur.getString(cur.getColumnIndex("typeofhhd")));
                    item.setLivinginshelter(cur.getString(cur.getColumnIndex("livinginshelter")));

                    item.setEducode(cur.getString(cur.getColumnIndex("educode")));
                    item.setEducationOther(cur.getString(cur.getColumnIndex("education_other")));
                    item.setDisabilitycode(cur.getString(cur.getColumnIndex("disabilitycode")));
                    item.setCasteGroup(cur.getString(cur.getColumnIndex("caste_group")));


                    item.setAddressline1Sl(cur.getString(cur.getColumnIndex("addressline1sl")));
                    item.setAddressline2Sl(cur.getString(cur.getColumnIndex("addressline2sl")));
                    item.setAddressline3Sl(cur.getString(cur.getColumnIndex("addressline3sl")));
                    item.setAddressline4Sl(cur.getString(cur.getColumnIndex("addressline4sl")));
                    item.setAddressline5Sl(cur.getString(cur.getColumnIndex("addressline5sl")));
                    item.setSpousenm(cur.getString(cur.getColumnIndex("spousenm")));
                    item.setHhdNo(cur.getString(cur.getColumnIndex("hhd_no")));
                    item.setAadhaarNo(cur.getString(cur.getColumnIndex("aadhaar_no")));
                    item.setNameAadhaar(cur.getString(cur.getColumnIndex("name_aadhaar")));
                    item.setUrnNo(cur.getString(cur.getColumnIndex("urn_no")));
                    item.setMobileNo(cur.getString(cur.getColumnIndex("mobile_no")));
                    item.setRuralUrban(cur.getString(cur.getColumnIndex("rural_urban")));
                    item.setBankIfsc(cur.getString(cur.getColumnIndex("bank_ifsc")));
                    item.setMarkForDeletion(cur.getString(cur.getColumnIndex("mark_for_deletion")));

                    item.setEid(cur.getString(cur.getColumnIndex("eid")));
                    item.setBankAccNo(cur.getString(cur.getColumnIndex("bank_acc_no")));
                    item.setSchemeId(cur.getString(cur.getColumnIndex("scheme_id")));
                    item.setSchemeNo(cur.getString(cur.getColumnIndex("scheme_no")));
                    item.setNameNominee(cur.getString(cur.getColumnIndex("name_nominee")));
                    item.setRelationNomineeCode(cur.getString(cur.getColumnIndex("relation_nominee_code")));
                    item.setIdType(cur.getString(cur.getColumnIndex("id_type")));
                    item.setIdNo(cur.getString(cur.getColumnIndex("id_no")));
                    item.setNameAsId(cur.getString(cur.getColumnIndex("name_as_id")));
                    /*if(cur.getBlob(cur.getColumnIndex("id_photo1"))!=null) {
                        item.setIdPhoto1(AppUtility.convertBitmapToString(BitmapFactory.decodeByteArray(cur.getBlob(cur.getColumnIndex("id_photo1")),
                                0, cur.getBlob(cur.getColumnIndex("id_photo1")).length)));
                    }*/
                    // String img=cur.getString(cur.getColumnIndex("govt_id_photo1"));
                    //  Log.d("Secc Database","Govt ID Img111111111111 : "+img);
                    item.setGovtIdPhoto(cur.getString(cur.getColumnIndex("govt_id_photo1")));
                    // item.set(cur.getString(cur.getColumnIndex("id_photo2")));
                    // item.set(cur.getString(cur.getColumnIndex("member_photo")));
                    item.setAadhaarCapturingMode(cur.getString(cur.getColumnIndex("aadhaar_capturing_mode")));
                    item.setConsent(cur.getString(cur.getColumnIndex("consent")));
                    item.setAadhaarAuthMode(cur.getString(cur.getColumnIndex("aadhaar_auth_mode")));
                    item.setAadhaarAuthDt(cur.getString(cur.getColumnIndex("aadhaar_auth_dt")));
                    item.setAadhaarVerifiedBy(cur.getString(cur.getColumnIndex("aadhaar_verified_by")));
                    item.setUserid(cur.getString(cur.getColumnIndex("userid")));
                    item.setDateUpdated(cur.getString(cur.getColumnIndex("date_updated")));
                    item.setAadhaarAuth(cur.getString(cur.getColumnIndex("aadhaar_auth")));
                    item.setMobileAuth(cur.getString(cur.getColumnIndex("mobile_auth")));
                    item.setUrnAuth(cur.getString(cur.getColumnIndex("urn_auth")));
                    item.setIfscAuth(cur.getString(cur.getColumnIndex("ifsc_auth")));
                    item.setBankAccAuth(cur.getString(cur.getColumnIndex("bank_acc_auth")));
                    item.setStateSchemeCodeAuth(cur.getString(cur.getColumnIndex("state_scheme_code_auth")));
                    item.setSchemeCode(cur.getString(cur.getColumnIndex("scheme_code")));
                    item.setDataSource(cur.getString(cur.getColumnIndex("data_source")));
                    item.setNhpsId(cur.getString(cur.getColumnIndex("nhps_id")));
                    item.setMobileAuthDt(cur.getString(cur.getColumnIndex("mobile_auth_dt")));
                    item.setUrnAuthDt(cur.getString(cur.getColumnIndex("urn_auth_dt")));
                    item.setIfscAuthDt(cur.getString(cur.getColumnIndex("ifsc_auth_dt")));
                    item.setBankAccAuthDt(cur.getString(cur.getColumnIndex("bank_acc_auth_dt")));
                    item.setStateSchemeCodeAuthDt(cur.getString(cur.getColumnIndex("state_scheme_code_auth_dt")));
                    item.setConsentDt(cur.getString(cur.getColumnIndex("consent_dt")));
                    item.setAadhaarGender(cur.getString(cur.getColumnIndex("aadhaar_gender")));
                    item.setAadhaarYob(cur.getString(cur.getColumnIndex("aadhaar_yob")));
                    item.setAadhaarDob(cur.getString(cur.getColumnIndex("aadhaar_dob")));
                    item.setAadhaarCo(cur.getString(cur.getColumnIndex("aadhaar_co")));
                    item.setAadhaarGname(cur.getString(cur.getColumnIndex("aadhaar_gname")));
                    item.setAadhaarHouse(cur.getString(cur.getColumnIndex("aadhaar_house")));
                    item.setAadhaarStreet(cur.getString(cur.getColumnIndex("aadhaar_street")));
                    item.setAadhaarLoc(cur.getString(cur.getColumnIndex("aadhaar_loc")));
                    item.setAadhaarVtc(cur.getString(cur.getColumnIndex("aadhaar_vtc")));
                    item.setAadhaarPo(cur.getString(cur.getColumnIndex("aadhaar_po")));
                    item.setAadhaarDist(cur.getString(cur.getColumnIndex("aadhaar_dist")));
                    item.setAadhaarSubdist(cur.getString(cur.getColumnIndex("aadhaar_subdist")));
                    item.setAadhaarState(cur.getString(cur.getColumnIndex("aadhaar_state")));
                    item.setAadhaarPc(cur.getString(cur.getColumnIndex("aadhaar_pc")));
                    item.setAadhaarLm(cur.getString(cur.getColumnIndex("aadhaar_lm")));
                    item.setLatitude(cur.getString(cur.getColumnIndex("latitude")));
                    item.setLongitude(cur.getString(cur.getColumnIndex("longitude")));
                    item.setWhoseMobile(cur.getString(cur.getColumnIndex("whose_mobile")));
                    item.setMemberPhoto1(cur.getString(cur.getColumnIndex("member_photo1")));

                    item.setNhpsRelationCode(cur.getString(cur.getColumnIndex("nhps_relation_code")));
                    item.setNhpsRelationName(cur.getString(cur.getColumnIndex("nhps_relation_name")));
                    item.setNomineeRelationName(cur.getString(cur.getColumnIndex("nominee_relation_name")));
                /*    item.setReqName(cur.getString(cur.getColumnIndex("req_name")));
                    item.setReqRelationCode(cur.getString(cur.getColumnIndex("req_relation_code")));
                    item.setReqRelationName(cur.getString(cur.getColumnIndex("req_relation_name")));
                    item.setReqFatherName(cur.getString(cur.getColumnIndex("req_father_name")));
                    item.setReqMotherName(cur.getString(cur.getColumnIndex("req_mother_name")));
                    item.setReqDOB(cur.getString(cur.getColumnIndex("req_dob")));
                    item.setReqGenderCode(cur.getString(cur.getColumnIndex("req_gender_id")));
                    item.setReqMarritalStatCode(cur.getString(cur.getColumnIndex("req_marital_status_id")));
                    item.setReqOccupation(cur.getString(cur.getColumnIndex("req_occupation")));*/


                  /*  item.setGovtIdSurveyedStat(cur.getString(cur.getColumnIndex("govt_id_survey_status")));
                    item.setAadhaarSurveyedStat(cur.getString(cur.getColumnIndex("aadhar_survey_status")));
                    item.setPhotoSurveyedStatus(cur.getString(cur.getColumnIndex("photo_capture_status")));
                    item.setMobileNoSurveyedStat(cur.getString(cur.getColumnIndex("mobile_surveyed_status")));
                    item.setNomineeDetailSurveyedStat(cur.getString(cur.getColumnIndex("nominee_surveyed_status")));
*/

                    String hhStatus = cur.getString(cur.getColumnIndex("hh_status"));
                    String memStatus = cur.getString(cur.getColumnIndex("mem_status"));
                    item.setLockedSave(cur.getString(cur.getColumnIndex("locked_save")));
                    item.setAadhaarStatus(cur.getString(cur.getColumnIndex("aadhaar_status")));
                    String syncStatus = cur.getString(cur.getColumnIndex("synced_status"));
                    if (memStatus != null) {
                        item.setMemStatus(memStatus.trim());
                    }
                    if (hhStatus != null && !hhStatus.equalsIgnoreCase("")) {
                        item.setHhStatus(hhStatus.trim());
                    }
                    if (syncStatus != null && !syncStatus.equalsIgnoreCase("")) {
                        item.setSyncedStatus(syncStatus.trim());
                    }
                    item.setSyncDt(cur.getString(cur.getColumnIndex("sync_dt")));
                    item.setError_type(cur.getString(cur.getColumnIndex("error_type")));
                    item.setError_code(cur.getString(cur.getColumnIndex("error_msg")));
                    item.setError_msg(cur.getString(cur.getColumnIndex("error_msg")));
                    item.setAppVersion(cur.getString(cur.getColumnIndex("app_version")));

                    item.setRsbyMemId(cur.getString(cur.getColumnIndex("rsby_rsbyMemId")));
                    item.setRsbyUrnId(cur.getString(cur.getColumnIndex("rsby_urnId")));


                    item.setUrnId(cur.getString(cur.getColumnIndex("rsby_urnId")));
                    item.setRsbyInsccode(cur.getString(cur.getColumnIndex("rsby_insccode")));
                    item.setRsbyPolicyno(cur.getString(cur.getColumnIndex("rsby_policyno")));
                    item.setRsbyPolicyamt(cur.getString(cur.getColumnIndex("rsby_policyamt")));
                    item.setRsbyStartdate(cur.getString(cur.getColumnIndex("rsby_startdate")));
                    item.setRsbyEnddate(cur.getString(cur.getColumnIndex("rsby_enddate")));
                    item.setRsbyIssuesTimespam(cur.getString(cur.getColumnIndex("rsby_issuesTimespam")));
                    item.setRsbyHofnamereg(cur.getString(cur.getColumnIndex("rsby_hofnamereg")));
                    item.setRsbyDoorhouse(cur.getString(cur.getColumnIndex("rsby_doorhouse")));
                    item.setRsbyVillageCode(cur.getString(cur.getColumnIndex("rsby_villageCode")));
                    item.setRsbyPanchyatTownCode(cur.getString(cur.getColumnIndex("rsby_panchyatTownCode")));
                    item.setRsbyBlockCode(cur.getString(cur.getColumnIndex("rsby_blockCode")));
                    item.setRsbyDistrictCode(cur.getString(cur.getColumnIndex("rsby_districtCode")));
                    String str = cur.getString(cur.getColumnIndex("rsby_stateCode"));
                    System.out.print(str);
                    item.setRsbyStateCode(cur.getString(cur.getColumnIndex("rsby_stateCode")));
                    item.setRsbyMemid(cur.getString(cur.getColumnIndex("rsby_memid")));
                    item.setRsbyName(cur.getString(cur.getColumnIndex("rsby_name")));
                    item.setRsbyDob(cur.getString(cur.getColumnIndex("rsby_dob")));
                    item.setRsbyGender(cur.getString(cur.getColumnIndex("rsby_gender")));
                    item.setRsbyRelcode(cur.getString(cur.getColumnIndex("rsby_relcode")));
                    item.setRsbyCsmNo(cur.getString(cur.getColumnIndex("rsby_csmNo")));
                    item.setRsbyCardType(cur.getString(cur.getColumnIndex("rsby_cardType")));
                    item.setRsbyCardCategory(cur.getString(cur.getColumnIndex("rsby_cardCategory")));
                    item.setRsbyCardMenutiaDetail(cur.getString(cur.getColumnIndex("rsby_cardMenutiaDetail")));
                    item.setNomineeGaurdianName(cur.getString(cur.getColumnIndex("nominee_guardian_name")));
                    // values.put("rsby_stateCode",item.getRsbyStateCode());
                    //  values.put("nominee_guardian_name ",item.getNomineeGaurdianName());


                    //     AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Sync Date and time"+item.getSyncDt());
                    seccMemberItems.add(item);

                    //  AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Fetching data");

                    cur.moveToNext();
                }
            }
            cur.close();
        }
        mDatabase.close();
        helpers.close();
        return seccMemberItems;
    }

    public static long update(SeccMemberItem item, Context context) {
        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        ContentValues values = new ContentValues();
        values.put("member_active_status", item.getMember_active_status());
        values.put("printStatus", item.getPrintStatus());
        values.put("nhps_mem_id", item.getNhpsMemId());
        values.put("tin_npr", item.getTinNpr());
        values.put("statecode", item.getStatecode());
        values.put("districtcode", item.getDistrictcode());
        values.put("tehsilcode", item.getTehsilcode());
        values.put("towncode", item.getTowncode());
        values.put("wardid", item.getWardid());
        values.put("blockno", item.getAhlblockno());
        values.put("grampanchayatcode", item.getGrampanchayatcode());
        values.put("grampanchayatname", item.getGrampanchayatname());
        values.put("ahlblockno", item.getAhlblockno());
        values.put("ahlsubblockno", item.getAhlsubblockno());
        values.put("slnohhd", item.getSlnohhd());
        values.put("slnomember", item.getSlnomember());
        values.put("ahlslnohhd", item.getAhlslnohhd());
        values.put("ahltypeofeb", item.getAhltypeofeb());
        values.put("typeofhhd", item.getTypeofhhd());
        values.put("livinginshelter", item.getLivinginshelter());
        values.put("name", item.getName());
        values.put("name_sl", item.getNameSl());
        values.put("relation", item.getRelation());
        values.put("relation_sl", item.getRelationSl());
        values.put("genderid", item.getGenderid());
        values.put("dob", item.getDob());
        values.put("mstatusid", item.getMstatusid());
        values.put("fathername", item.getFathername());
        values.put("fathername_sl", item.getFathernameSl());
        values.put("mothername", item.getMothername());
        values.put("mothername_sl", item.getMothernameSl());
        values.put("occupation", item.getOccupation());
        values.put("occupation_sl", item.getOccupationSl());
        values.put("educode", item.getEducode());
        values.put("education_other", item.getEducationOther());
        values.put("disabilitycode", item.getDisabilitycode());
        values.put("caste_group", item.getCasteGroup());
        values.put("ahl_tin", item.getAhlTin());
        // values.put("member_status", item.getMemberStatus());
        values.put("addressline1", item.getAddressline1());
        values.put("addressline2", item.getAddressline2());
        values.put("addressline3", item.getAddressline3());
        values.put("addressline4", item.getAddressline4());
        values.put("addressline5", item.getAddressline5());
        values.put("pincode", item.getPincode());
        values.put("addressline1sl", item.getAddressline1Sl());
        values.put("addressline2sl", item.getAddressline2Sl());
        values.put("addressline3sl", item.getAddressline3Sl());
        values.put("addressline4sl", item.getAddressline4Sl());
        values.put("addressline5sl", item.getAddressline5Sl());
        values.put("dob_frm_npr", item.getDobFrmNpr());
        values.put("spousenm", item.getSpousenm());
        values.put("hhd_no", item.getHhdNo());
        if (item.getAadhaarNo() != null) {
            values.put("aadhaar_no", item.getAadhaarNo().trim());
        }
        values.put("name_aadhaar", item.getNameAadhaar());
        values.put("urn_no", item.getUrnNo());
        values.put("mobile_no", item.getMobileNo());
        values.put("rural_urban", item.getRuralUrban());
        values.put("bank_ifsc", item.getNhpsRelationCode());
        values.put("mark_for_deletion", item.getMarkForDeletion());
        values.put("aadhaar_status", item.getAadhaarStatus());
        values.put("hh_status", item.getHhStatus());
        values.put("mem_status", item.getMemStatus());
        values.put("eid", item.getEid());
        values.put("bank_acc_no", item.getNhpsRelationName());
        values.put("scheme_id", item.getSchemeId());
        values.put("scheme_no", item.getSchemeNo());
        values.put("name_nominee", item.getNameNominee());
        values.put("relation_nominee_code", item.getRelationNomineeCode());
        values.put("nominee_relation_name", item.getNomineeRelationName());
        values.put("id_type", item.getIdType());
        values.put("id_no", item.getIdNo());
        values.put("name_as_id", item.getNameAsId());
        values.put("member_photo1", item.getMemberPhoto1());
        Log.d("Secc Data base", "ID Image : " + item.getGovtIdPhoto());
        values.put("govt_id_photo1", item.getGovtIdPhoto());

        // values.put("id_photo1",item.getIdPhoto1()!=null ? item.getIdPhoto1().getBytes() : null);
                /*"values.put("id_photo2 bytea,
                        values.put("member_photo bytea,*/
        values.put("aadhaar_capturing_mode", item.getAadhaarCapturingMode());
        values.put("consent", item.getConsent());
        values.put("aadhaar_auth_mode", item.getAadhaarAuthMode());
        values.put("aadhaar_auth_dt", item.getAadhaarAuthDt());
        values.put("aadhaar_verified_by", item.getAadhaarVerifiedBy());
        values.put("userid", item.getUserid());
        values.put("date_updated", item.getDateUpdated());
        values.put("aadhaar_auth", item.getAadhaarAuth());
        values.put("mobile_auth", item.getMobileAuth());
        values.put("urn_auth", item.getUrnAuth());
        values.put("ifsc_auth", item.getIfscAuth());
        //
        values.put("scheme_id3", item.getSchemeId3());
        values.put("scheme_id2", item.getSchemeId2());
        values.put("scheme_id1", item.getSchemeId1());
        values.put("scheme_no3", item.getSchemeNo3());
        values.put("scheme_no2", item.getSchemeNo2());
        values.put("scheme_no1", item.getSchemeNo1());

        //
        values.put("bank_acc_auth", item.getBankAccAuth());
        values.put("state_scheme_code_auth", item.getStateSchemeCodeAuth());
        values.put("scheme_code", item.getSchemeCode());
        values.put("data_source", item.getDataSource());
        values.put("nhps_id", item.getNhpsId());
        values.put("mobile_auth_dt", item.getMobileAuthDt());
        values.put("urn_auth_dt", item.getUrnAuthDt());
        values.put("ifsc_auth_dt", item.getIfscAuthDt());
        values.put("bank_acc_auth_dt", item.getBankAccAuthDt());
        values.put("state_scheme_code_auth_dt", item.getStateSchemeCodeAuthDt());
        values.put("consent_dt", item.getConsentDt());
        values.put("aadhaar_gender", item.getAadhaarGender());
        values.put("aadhaar_yob", item.getAadhaarYob());
        values.put("aadhaar_dob", item.getAadhaarDob());
        values.put("aadhaar_co", item.getAadhaarCo());
        values.put("aadhaar_gname", item.getAadhaarGname());
        values.put("aadhaar_house", item.getAadhaarHouse());
        values.put("aadhaar_street", item.getAadhaarStreet());
        values.put("aadhaar_loc", item.getAadhaarLoc());
        values.put("aadhaar_vtc", item.getAadhaarVtc());
        values.put("aadhaar_po", item.getAadhaarPo());
        values.put("aadhaar_dist", item.getAadhaarDist());
        values.put("aadhaar_subdist", item.getAadhaarSubdist());
        values.put("aadhaar_state", item.getAadhaarState());
        values.put("aadhaar_pc", item.getAadhaarPc());
        values.put("aadhaar_lm", item.getNhpsRelationName());
        values.put("latitude", item.getLatitude());
        values.put("longitude", item.getLongitude());
        values.put("whose_mobile", item.getWhoseMobile());
        //  Log.d("Secc Databs"," Relation Code :"+item.getNhpsRelationCode());
        values.put("nhps_relation_code", item.getNhpsRelationCode());
        values.put("nhps_relation_name", item.getNhpsRelationName());
        /*values.put("req_name", item.getReqName());
        values.put("req_relation_name",item.getReqRelationName());
        values.put("req_relation_code", item.getReqRelationCode());
        values.put("req_father_name", item.getReqFatherName());
        values.put("req_mother_name", item.getReqMotherName());
        values.put("req_dob", item.getReqDOB());
        values.put("req_gender_id", item.getReqGenderCode());
        values.put("req_marital_status_id", item.getReqMarritalStatCode());
        values.put("req_occupation", item.getReqOccupation());*/
        values.put("locked_save", item.getLockedSave());
      /*  values.put("govt_id_survey_status", item.getGovtIdSurveyedStat());
        values.put("aadhar_survey_status", item.getAadhaarSurveyedStat());
        values.put("photo_capture_status", item.getPhotoSurveyedStatus());
        values.put("mobile_surveyed_status", item.getMobileNoSurveyedStat());
        values.put("nominee_surveyed_status", item.getNomineeDetailSurveyedStat());*/
        values.put("synced_status", item.getSyncedStatus());//
        values.put("error_code", item.getError_code());
        values.put("error_msg", item.getError_msg());
        values.put("error_type", item.getError_type());
        values.put("app_version", item.getAppVersion());
        values.put("nominee_guardian_name ", item.getNomineeGaurdianName());

        if (item.getSyncDt() != null) {
            values.put("sync_dt", item.getSyncDt().trim());
        }

        SQLiteDatabase db = helpers.getWritableDatabase();
        int updateFlag = db.update(AppConstant.TRANSECT_POPULATE_SECC, values,
                "nhps_mem_id='" + item.getNhpsMemId() + "'", null);
        db.close();
        helpers.close();
        return updateFlag;
    }

    public static ArrayList<HouseHoldItem> getHouseholdList(Context context, String query) {
        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        ArrayList<HouseHoldItem> houseHoldItems = new ArrayList<>();
        SQLiteDatabase mDatabase = helpers.createOrOpenDatabase();
        Cursor cur = mDatabase.rawQuery(query, null);
        Log.d("NHSMasterDaoImpl", " Query : " + query + " count :" + cur.getCount());
        if (cur != null) {
            if (cur.getCount() > 0) {
                cur.moveToFirst();
                while (cur.isAfterLast() == false) {
                    HouseHoldItem item = new HouseHoldItem();
                    item.setNhpsMemId(cur.getString(cur.getColumnIndex("nhps_mem_id")));
                    item.setAhlTin(cur.getString(cur.getColumnIndex("ahl_tin")));
                    item.setHhdNo(cur.getString(cur.getColumnIndex("hhd_no")));
                    item.setSlnomember(cur.getString(cur.getColumnIndex("slnomember")));
                    item.setSlnohhd(cur.getString(cur.getColumnIndex("slnohhd")));
                    item.setAhlslnohhd(cur.getString(cur.getColumnIndex("ahlslnohhd")));
                    item.setName(cur.getString(cur.getColumnIndex("name")));
                    item.setNameSl(cur.getString(cur.getColumnIndex("name_sl")));
                    item.setFathername(cur.getString(cur.getColumnIndex("fathername")));
                    item.setFathernameSl(cur.getString(cur.getColumnIndex("fathername_sl")));
                    item.setRelation(cur.getString(cur.getColumnIndex("relation")));
                    item.setRelationSl(cur.getString(cur.getColumnIndex("relation_sl")));
                    item.setGenderid(cur.getString(cur.getColumnIndex("genderid")));
                    item.setDob(cur.getString(cur.getColumnIndex("dob")));
                    item.setMstatusid(cur.getString(cur.getColumnIndex("mstatusid")));
                    item.setMothername(cur.getString(cur.getColumnIndex("mothername")));
                    item.setMothernameSl(cur.getString(cur.getColumnIndex("mothername_sl")));
                    item.setOccupation(cur.getString(cur.getColumnIndex("occupation")));
                    item.setOccupationSl(cur.getString(cur.getColumnIndex("occupation_sl")));
                    item.setCasteGroup(cur.getString(cur.getColumnIndex("caste_group")));
                    item.setStatecode(cur.getString(cur.getColumnIndex("statecode")));
                    item.setDistrictcode(cur.getString(cur.getColumnIndex("districtcode")));
                    item.setTehsilcode(cur.getString(cur.getColumnIndex("tehsilcode")));
                    item.setTowncode(cur.getString(cur.getColumnIndex("towncode")));
                    item.setWardid(cur.getString(cur.getColumnIndex("wardid")));
                    item.setAhlblockno(cur.getString(cur.getColumnIndex("ahlblockno")));
                    item.setUrn_no(cur.getString(cur.getColumnIndex("urn_no")));
                    item.setTypeofhhd(cur.getString(cur.getColumnIndex("typeofhhd")));
                    item.setHhdLandownedcodes(cur.getString(cur.getColumnIndex("hhd_landownedcodes")));
                    item.setTotalirrigated(cur.getString(cur.getColumnIndex("totalirrigated")));
                    item.setHhdIrriEquip(cur.getString(cur.getColumnIndex("hhd_irri_equip")));
                    item.setTotalirrigated(cur.getString(cur.getColumnIndex("totalunirrigated")));
                    item.setOtherirrigated(cur.getString(cur.getColumnIndex("otherirrigated")));
                    item.setHhdAssetMveh(cur.getString(cur.getColumnIndex("hhd_asset_mveh")));
                    item.setHhdMagriEquip(cur.getString(cur.getColumnIndex("hhd_magri_equip")));
                    item.setHhdKcc(cur.getString(cur.getColumnIndex("hhd_kcc")));
                    item.setHhdEmpSector(cur.getString(cur.getColumnIndex("hhd_emp_sector")));
                    item.setHhdEmpErg(cur.getString(cur.getColumnIndex("hhd_emp_erg")));
                    item.setHhdEmpHem(cur.getString(cur.getColumnIndex("hhd_emp_hem")));
                    item.setHhdEmpPit(cur.getString(cur.getColumnIndex("hhd_emp_pit")));
                    item.setHousingWalltype(cur.getString(cur.getColumnIndex("housing_walltype")));
                    item.setHousingRooftype(cur.getString(cur.getColumnIndex("housing_rooftype")));
                    item.setHousingNoOfRooms(cur.getString(cur.getColumnIndex("housing_no_of_rooms")));
                    item.setHhdAssetRef(cur.getString(cur.getColumnIndex("hhd_asset_ref")));
                    item.setHhdAssetTelmob(cur.getString(cur.getColumnIndex("hhd_asset_telmob")));
                    item.setLivinginshelter(cur.getString(cur.getColumnIndex("livinginshelter")));
                    item.setHhdEmpOther(cur.getString(cur.getColumnIndex("hhd_emp_other")));
                    item.setHhdMs(cur.getString(cur.getColumnIndex("hhd_ms")));
                    item.setHhdPtg(cur.getString(cur.getColumnIndex("hhd_ptg")));
                    item.setHhdLrbl(cur.getString(cur.getColumnIndex("hhd_lrbl")));
                    item.setRuralUrban(cur.getString(cur.getColumnIndex("rural_urban")));
                    /*item.setStateCode(cur.getString(cur.getColumnIndex("state_code")));
                    item.setDistrictCode(cur.getString(cur.getColumnIndex("district_code")));
                    item.setTehsilCode(cur.getString(cur.getColumnIndex("tehsil_code")));
                    item.setVtCode(cur.getString(cur.getColumnIndex("vt_code")));
                    item.setWardCode(cur.getString(cur.getColumnIndex("ward_code")));*/
                    item.setMddsStc(cur.getString(cur.getColumnIndex("mdds_stc")));
                    item.setMddsDtc(cur.getString(cur.getColumnIndex("mdds_dtc")));
                    item.setMddsDtc(cur.getString(cur.getColumnIndex("mdds_sdtc")));
                    item.setMddsPlcn(cur.getString(cur.getColumnIndex("mdds_plcn")));

                    item.setAhlSubBlockNo(cur.getString(cur.getColumnIndex("ahlsubblockno")));
                    item.setNhpsId(cur.getString(cur.getColumnIndex("nhps_id")));
                    String hhStatus = cur.getString(cur.getColumnIndex("hh_status"));

                    if (hhStatus != null && !hhStatus.equalsIgnoreCase("")) {
                        item.setHhStatus(hhStatus.trim());
                    }

                    String syncStatus = cur.getString(cur.getColumnIndex("synced_status"));
                    if (syncStatus != null && !syncStatus.equalsIgnoreCase("")) {
                        item.setSyncedStatus(syncStatus);
                    }
                    String saveLocked = cur.getString(cur.getColumnIndex("locked_save"));
                    if (saveLocked != null && !saveLocked.equalsIgnoreCase("")) {
                        item.setLockSave(saveLocked);
                    }
                    //  Timestamp syncDt=new Timestamp(cur.getColumnIndex("sync_dt"));
                    long syncDt = cur.getLong(cur.getColumnIndex("sync_dt"));
                    item.setAddressline1(cur.getString(cur.getColumnIndex("addressline1")));
                    item.setAddressline2(cur.getString(cur.getColumnIndex("addressline2")));
                    item.setAddressline3(cur.getString(cur.getColumnIndex("addressline3")));
                    item.setAddressline4(cur.getString(cur.getColumnIndex("addressline4")));
                    item.setAddressline5(cur.getString(cur.getColumnIndex("addressline5")));
                    item.setPincode(cur.getString(cur.getColumnIndex("pincode")));
                    item.setError_type(cur.getString(cur.getColumnIndex("error_type")));
                    item.setError_code(cur.getString(cur.getColumnIndex("error_msg")));
                    item.setError_msg(cur.getString(cur.getColumnIndex("error_msg")));
                    item.setAppVersion(cur.getString(cur.getColumnIndex("app_version")));
                    //  if(syncDt!=null) {
                    item.setSyncDt(cur.getString(cur.getColumnIndex("sync_dt")));
                    // }
                    /*values.put("rsby_rsbyMemId",item.getRsbyMemId());
                    values.put("rsby_urnId",item.getRsbyUrnId());
                    values.put("rsby_insccode",item.getRsbyInsccode());
                    values.put("rsby_policyno",item.getRsbyPolicyno());

                    values.put("rsby_policyamt",item.getRsbyPolicyamt());

                    values.put("rsby_startdate",item.getRsbyStartdate());
                    values.put("rsby_enddate",item.getRsbyEnddate());
                    values.put("rsby_issuesTimespam",item.getRsbyIssuesTimespam());

                    values.put("rsby_hofnamereg",item.getRsbyHofnamereg());
                    values.put("rsby_doorhouse",item.getRsbyDoorhouse());
                    values.put("rsby_villageCode",item.getRsbyVillageCode());
                    values.put("rsby_panchyatTownCode",item.getRsbyPanchyatTownCode());

                    values.put("rsby_blockCode",item.getRsbyBlockCode());
                    values.put("rsby_districtCode",item.getRsbyDistrictCode());
                    values.put("rsby_stateCode",item.getRsbyStateCode());
                    values.put("rsby_memid",item.getRsbyMemId());
                    values.put("rsby_name",item.getRsbyName());
                    values.put("rsby_dob",item.getRsbyDob());
                    values.put("rsby_gender",item.getRsbyGender());
                    values.put("rsby_relcode",item.getRsbyRelcode());
                    values.put("rsby_csmNo",item.getRsbyCsmNo());
                    values.put("rsby_cardType",item.getRsbyCardType());
                    values.put("rsby_cardCategory",item.getRsbyCardCategory());*/
                    AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Sync Date and time" + item.getSyncDt());
                    item.setRsbyMemId(cur.getString(cur.getColumnIndex("rsby_rsbyMemId")));
                    item.setRsbyUrnId(cur.getString(cur.getColumnIndex("rsby_urnId")));
                    item.setRsbyInsccode(cur.getString(cur.getColumnIndex("rsby_insccode")));
                    item.setRsbyPolicyno(cur.getString(cur.getColumnIndex("rsby_policyno")));
                    item.setRsbyPolicyamt(cur.getString(cur.getColumnIndex("rsby_policyamt")));
                    item.setRsbyStartdate(cur.getString(cur.getColumnIndex("rsby_startdate")));
                    item.setRsbyEnddate(cur.getString(cur.getColumnIndex("rsby_enddate")));
                    item.setRsbyIssuesTimespam(cur.getString(cur.getColumnIndex("rsby_issuesTimespam")));
                    item.setRsbyHofnamereg(cur.getString(cur.getColumnIndex("rsby_hofnamereg")));
                    item.setRsbyDoorhouse(cur.getString(cur.getColumnIndex("rsby_doorhouse")));
                    item.setRsbyVillageCode(cur.getString(cur.getColumnIndex("rsby_villageCode")));
                    item.setRsbyPanchyatTownCode(cur.getString(cur.getColumnIndex("rsby_panchyatTownCode")));
                    item.setRsbyBlockCode(cur.getString(cur.getColumnIndex("rsby_blockCode")));
                    item.setRsbyDistrictCode(cur.getString(cur.getColumnIndex("rsby_districtCode")));
                    item.setRsbyMemid(cur.getString(cur.getColumnIndex("rsby_memid")));
                    item.setRsbyName(cur.getString(cur.getColumnIndex("rsby_name")));
                    item.setRsbyDob(cur.getString(cur.getColumnIndex("rsby_dob")));
                    item.setRsbyGender(cur.getString(cur.getColumnIndex("rsby_gender")));
                    item.setRsbyRelcode(cur.getString(cur.getColumnIndex("rsby_relcode")));
                    item.setRsbyCsmNo(cur.getString(cur.getColumnIndex("rsby_csmNo")));
                    item.setRsbyCardType(cur.getString(cur.getColumnIndex("rsby_cardType")));
                    item.setRsbyCardCategory(cur.getString(cur.getColumnIndex("rsby_cardCategory")));
                    // AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Data Source : "+cur.getString(cur.getColumnIndex("data_source"))+" Rsby Name : "+item.getRsbyName());
                    item.setDataSource(cur.getString(cur.getColumnIndex("data_source")));
                    item.setRsbyStateCode(cur.getString(cur.getColumnIndex("rsby_stateCode")));
                    item.setRsbyFamilyPhoto(cur.getString(cur.getColumnIndex("rsby_familyphoto")));
                    // values.put("rsby_stateCode",item.getRsbyStateCode());
                    houseHoldItems.add(item);
                    cur.moveToNext();
                }
            }
            cur.close();

        }
        mDatabase.close();
        helpers.close();
        return houseHoldItems;
    }

    public static HouseHoldItem getHouseholdDetail(Context context, String query) {

        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        ArrayList<HouseHoldItem> houseHoldItems = new ArrayList<>();
        SQLiteDatabase mDatabase = helpers.createOrOpenDatabase();
        HouseHoldItem item = null;
        Cursor cur = mDatabase.rawQuery(query, null);
        Log.d("NHSMasterDaoImpl", " Query : " + cur.getCount());
        if (cur != null) {
            if (cur.getCount() > 0) {
                cur.moveToFirst();
                while (cur.isAfterLast() == false) {
                    item = new HouseHoldItem();
                    item.setNhpsMemId(cur.getString(cur.getColumnIndex("nhps_mem_id")));
                    item.setAhlTin(cur.getString(cur.getColumnIndex("ahl_tin")));
                    item.setHhdNo(cur.getString(cur.getColumnIndex("hhd_no")));
                    item.setSlnomember(cur.getString(cur.getColumnIndex("slnomember")));
                    item.setSlnohhd(cur.getString(cur.getColumnIndex("slnohhd")));
                    item.setAhlslnohhd(cur.getString(cur.getColumnIndex("ahlslnohhd")));
                    item.setName(cur.getString(cur.getColumnIndex("name")));
                    item.setNameSl(cur.getString(cur.getColumnIndex("name_sl")));
                    item.setFathername(cur.getString(cur.getColumnIndex("fathername")));
                    item.setFathernameSl(cur.getString(cur.getColumnIndex("fathername_sl")));
                    item.setRelation(cur.getString(cur.getColumnIndex("relation")));
                    item.setRelationSl(cur.getString(cur.getColumnIndex("relation_sl")));
                    item.setGenderid(cur.getString(cur.getColumnIndex("genderid")));
                    item.setDob(cur.getString(cur.getColumnIndex("dob")));
                    item.setMstatusid(cur.getString(cur.getColumnIndex("mstatusid")));
                    item.setMothername(cur.getString(cur.getColumnIndex("mothername")));
                    item.setMothernameSl(cur.getString(cur.getColumnIndex("mothername_sl")));
                    item.setOccupation(cur.getString(cur.getColumnIndex("occupation")));
                    item.setOccupationSl(cur.getString(cur.getColumnIndex("occupation_sl")));
                    item.setCasteGroup(cur.getString(cur.getColumnIndex("caste_group")));
                    item.setStatecode(cur.getString(cur.getColumnIndex("statecode")));
                    item.setDistrictcode(cur.getString(cur.getColumnIndex("districtcode")));
                    item.setTehsilcode(cur.getString(cur.getColumnIndex("tehsilcode")));
                    item.setTowncode(cur.getString(cur.getColumnIndex("towncode")));
                    item.setWardid(cur.getString(cur.getColumnIndex("wardid")));
                    item.setAhlblockno(cur.getString(cur.getColumnIndex("ahlblockno")));
                    item.setTypeofhhd(cur.getString(cur.getColumnIndex("typeofhhd")));
                    item.setHhdLandownedcodes(cur.getString(cur.getColumnIndex("hhd_landownedcodes")));
                    item.setTotalirrigated(cur.getString(cur.getColumnIndex("totalirrigated")));
                    item.setHhdIrriEquip(cur.getString(cur.getColumnIndex("hhd_irri_equip")));
                    item.setTotalirrigated(cur.getString(cur.getColumnIndex("totalunirrigated")));
                    item.setOtherirrigated(cur.getString(cur.getColumnIndex("otherirrigated")));
                    item.setHhdAssetMveh(cur.getString(cur.getColumnIndex("hhd_asset_mveh")));
                    item.setHhdMagriEquip(cur.getString(cur.getColumnIndex("hhd_magri_equip")));
                    item.setHhdKcc(cur.getString(cur.getColumnIndex("hhd_kcc")));
                    item.setHhdEmpSector(cur.getString(cur.getColumnIndex("hhd_emp_sector")));
                    item.setHhdEmpErg(cur.getString(cur.getColumnIndex("hhd_emp_erg")));
                    item.setHhdEmpHem(cur.getString(cur.getColumnIndex("hhd_emp_hem")));
                    item.setHhdEmpPit(cur.getString(cur.getColumnIndex("hhd_emp_pit")));
                    item.setHousingWalltype(cur.getString(cur.getColumnIndex("housing_walltype")));
                    item.setHousingRooftype(cur.getString(cur.getColumnIndex("housing_rooftype")));
                    item.setHousingNoOfRooms(cur.getString(cur.getColumnIndex("housing_no_of_rooms")));
                    item.setHhdAssetRef(cur.getString(cur.getColumnIndex("hhd_asset_ref")));
                    item.setHhdAssetTelmob(cur.getString(cur.getColumnIndex("hhd_asset_telmob")));
                    item.setLivinginshelter(cur.getString(cur.getColumnIndex("livinginshelter")));
                    item.setHhdEmpOther(cur.getString(cur.getColumnIndex("hhd_emp_other")));
                    item.setHhdMs(cur.getString(cur.getColumnIndex("hhd_ms")));
                    item.setHhdPtg(cur.getString(cur.getColumnIndex("hhd_ptg")));
                    item.setHhdLrbl(cur.getString(cur.getColumnIndex("hhd_lrbl")));
                    item.setRuralUrban(cur.getString(cur.getColumnIndex("rural_urban")));
                   /* item.setStateCode(cur.getString(cur.getColumnIndex("state_code")));
                    item.setDistrictCode(cur.getString(cur.getColumnIndex("district_code")));
                    item.setTehsilCode(cur.getString(cur.getColumnIndex("tehsil_code")));
                    item.setVtCode(cur.getString(cur.getColumnIndex("vt_code")));
                    item.setWardCode(cur.getString(cur.getColumnIndex("ward_code")));*/
                    item.setMddsStc(cur.getString(cur.getColumnIndex("mdds_stc")));
                    item.setMddsDtc(cur.getString(cur.getColumnIndex("mdds_dtc")));
                    item.setMddsDtc(cur.getString(cur.getColumnIndex("mdds_sdtc")));
                    item.setMddsPlcn(cur.getString(cur.getColumnIndex("mdds_plcn")));
                    item.setHhStatus(cur.getString(cur.getColumnIndex("hh_status")));
                    item.setAhlSubBlockNo(cur.getString(cur.getColumnIndex("ahlsubblockno")));
                    item.setSyncedStatus(cur.getString(cur.getColumnIndex("synced_status")));
                    item.setNhpsId(cur.getString(cur.getColumnIndex("nhps_id")));
                    item.setLockSave(cur.getString(cur.getColumnIndex("locked_save")));
                    item.setUrn_no(cur.getString(cur.getColumnIndex("urn_no")));
                    String hhStatus = cur.getString(cur.getColumnIndex("hh_status"));


                    if (hhStatus != null && !hhStatus.equalsIgnoreCase("")) {
                        item.setHhStatus(hhStatus.trim());
                    }
                    String syncStatus = cur.getString(cur.getColumnIndex("synced_status"));
                    if (syncStatus != null && !syncStatus.equalsIgnoreCase("")) {
                        item.setSyncedStatus(syncStatus);
                    }
                    String saveLocked = cur.getString(cur.getColumnIndex("locked_save"));
                    if (saveLocked != null && !saveLocked.equalsIgnoreCase("")) {
                        item.setLockSave(saveLocked);
                    }

                    // Timestamp syncDt=new Timestamp(cur.getColumnIndex("sync_dt"));
                    item.setAddressline1(cur.getString(cur.getColumnIndex("addressline1")));
                    item.setAddressline2(cur.getString(cur.getColumnIndex("addressline2")));
                    item.setAddressline3(cur.getString(cur.getColumnIndex("addressline3")));
                    item.setAddressline4(cur.getString(cur.getColumnIndex("addressline4")));
                    item.setAddressline5(cur.getString(cur.getColumnIndex("addressline5")));
                    item.setPincode(cur.getString(cur.getColumnIndex("pincode")));
                    item.setSyncDt(cur.getString(cur.getColumnIndex("sync_dt")));
                    item.setError_type(cur.getString(cur.getColumnIndex("error_type")));
                    item.setError_code(cur.getString(cur.getColumnIndex("error_msg")));
                    item.setError_msg(cur.getString(cur.getColumnIndex("error_msg")));
                    item.setAppVersion(cur.getString(cur.getColumnIndex("app_version")));
                    item.setRsbyMemId(cur.getString(cur.getColumnIndex("rsby_rsbyMemId")));
                    item.setRsbyUrnId(cur.getString(cur.getColumnIndex("rsby_urnId")));
                    item.setRsbyInsccode(cur.getString(cur.getColumnIndex("rsby_insccode")));
                    item.setRsbyPolicyno(cur.getString(cur.getColumnIndex("rsby_policyno")));
                    item.setRsbyPolicyamt(cur.getString(cur.getColumnIndex("rsby_policyamt")));
                    item.setRsbyStartdate(cur.getString(cur.getColumnIndex("rsby_startdate")));
                    item.setRsbyEnddate(cur.getString(cur.getColumnIndex("rsby_enddate")));
                    item.setRsbyIssuesTimespam(cur.getString(cur.getColumnIndex("rsby_issuesTimespam")));
                    item.setRsbyHofnamereg(cur.getString(cur.getColumnIndex("rsby_hofnamereg")));
                    item.setRsbyDoorhouse(cur.getString(cur.getColumnIndex("rsby_doorhouse")));
                    item.setRsbyVillageCode(cur.getString(cur.getColumnIndex("rsby_villageCode")));

                    item.setRsbyPanchyatTownCode(cur.getString(cur.getColumnIndex("rsby_panchyatTownCode")));
                    item.setRsbyBlockCode(cur.getString(cur.getColumnIndex("rsby_blockCode")));
                    item.setRsbyDistrictCode(cur.getString(cur.getColumnIndex("rsby_districtCode")));
                    item.setRsbyMemid(cur.getString(cur.getColumnIndex("rsby_memid")));

                    item.setRsbyName(cur.getString(cur.getColumnIndex("rsby_name")));
                    item.setRsbyDob(cur.getString(cur.getColumnIndex("rsby_dob")));
                    item.setRsbyGender(cur.getString(cur.getColumnIndex("rsby_gender")));
                    item.setRsbyRelcode(cur.getString(cur.getColumnIndex("rsby_relcode")));

                    item.setRsbyCsmNo(cur.getString(cur.getColumnIndex("rsby_csmNo")));
                    item.setRsbyCardType(cur.getString(cur.getColumnIndex("rsby_cardType")));
                    item.setRsbyCardCategory(cur.getString(cur.getColumnIndex("rsby_cardCategory")));
                    item.setDataSource(cur.getString(cur.getColumnIndex("data_source")));
                    item.setRsbyStateCode(cur.getString(cur.getColumnIndex("rsby_stateCode")));
                    item.setRsbyFamilyPhoto(cur.getString(cur.getColumnIndex("rsby_familyphoto")));
                    cur.moveToNext();
                }
            }
            cur.close();
        }
        mDatabase.close();
        helpers.close();
        return item;
    }

    public static long saveHousehold(Context context, HouseHoldItem item) {
        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        ContentValues values = new ContentValues();
        //   AppUtility.showLog(AppConstant.LOG_STATUS, TAG, " NHPS Member Id : " + item.getNhpsMemId());
        values.put("nhps_mem_id", item.getNhpsMemId());
        values.put("ahl_tin", item.getAhlTin());
        values.put("hhd_no", item.getHhdNo());
        values.put("slnomember", item.getSlnomember());
        values.put("slnohhd", item.getSlnohhd());
        values.put("ahlslnohhd", item.getAhlslnohhd());
        values.put("name", item.getName());
        //  String regionalName = item.getNameSl();
        // System.out.print(regionalName);
        values.put("name_sl", item.getNameSl());
        if (item.getUrn_no() != null) {
            values.put("urn_no", item.getUrn_no());
        } else {
            values.put("urn_no", item.getUrnId());
        }

        values.put("fathername", item.getFathername());
        values.put("fathername_sl", item.getFathernameSl());
        values.put("relation", item.getRelation());
        values.put("relation_sl", item.getRelationSl());
        values.put("genderid", item.getGenderid());
        values.put("dob", item.getDob());
        values.put("mstatusid", item.getMstatusid());
        values.put("mothername", item.getMothername());
        values.put("mothername_sl", item.getMothernameSl());
        values.put("occupation", item.getOccupation());
        values.put("occupation_sl", item.getOccupationSl());
        values.put("caste_group", item.getCasteGroup());

        values.put("statecode", item.getStatecode());
        values.put("districtcode", item.getDistrictcode());
        values.put("tehsilcode", item.getTehsilcode());
        values.put("towncode", item.getTowncode());
        values.put("wardid", item.getWardid());
        values.put("ahlblockno", item.getAhlblockno());
        values.put("ahlsubblockno ", item.getAhlSubBlockNo());

        values.put("typeofhhd", item.getTypeofhhd());
        values.put("hhd_landownedcodes", item.getHhdLandownedcodes());
        values.put("totalirrigated", item.getTotalirrigated());
        values.put("hhd_irri_equip", item.getHhdIrriEquip());
        values.put("totalunirrigated", item.getTotalunirrigated());
        values.put("otherirrigated", item.getOtherirrigated());
        values.put("hhd_asset_mveh", item.getHhdAssetMveh());
        values.put("hhd_magri_equip", item.getHhdMagriEquip());
        values.put("hhd_kcc", item.getHhdKcc());
        values.put("hhd_emp_sector", item.getHhdEmpSector());
        values.put("hhd_emp_erg", item.getHhdEmpErg());
        values.put("hhd_emp_hem", item.getHhdEmpHem());
        values.put("hhd_emp_pit", item.getHhdEmpPit());
        values.put("housing_walltype", item.getHousingWalltype());
        values.put("housing_rooftype", item.getHousingRooftype());
        values.put("housing_no_of_rooms", item.getHousingNoOfRooms());
        values.put("hhd_asset_ref", item.getHhdAssetRef());
        values.put("hhd_asset_telmob", item.getHhdAssetTelmob());
        values.put("livinginshelter", item.getLivinginshelter());
        values.put("hhd_emp_other", item.getHhdEmpOther());
        values.put("hhd_ms", item.getHhdMs());
        values.put("hhd_ptg", item.getHhdPtg());
        values.put("hhd_lrbl", item.getHhdLrbl());
        values.put("rural_urban", item.getRuralUrban());
        values.put("mdds_stc", item.getMddsStc());
        values.put("mdds_dtc", item.getMddsDtc());
        values.put("mdds_sdtc", item.getMddsDtc());
        values.put("mdds_plcn", item.getMddsPlcn());
        values.put("addressline1", item.getAddressline1());
        values.put("addressline2", item.getAddressline2());
        values.put("addressline3", item.getAddressline3());
        values.put("addressline4", item.getAddressline4());
        values.put("addressline5", item.getAddressline5());
        values.put("pincode", item.getPincode());
        values.put("app_version", item.getAppVersion());
        if (item.getSyncDt() != null) {
            values.put("sync_dt", item.getSyncDt());
        }
        // values.put("ahlsubblockno",item.getAh)
        if (item.getHhStatus() != null && !item.getHhStatus().equalsIgnoreCase("")) {
            values.put("hh_status", item.getHhStatus());
        }
        if (item.getSyncedStatus() != null && !item.getSyncedStatus().equalsIgnoreCase("")) {
            values.put("synced_status", item.getSyncedStatus());
        }
        values.put("nhps_id", item.getNhpsId());
        if (item.getLockSave() != null && !item.getLockSave().equalsIgnoreCase("")) {
            values.put("locked_save", item.getLockSave());
        }
        /* character varying(99),
                 character varying(99),
                 character varying(99),
                 character varying(99),
                 character varying(99),
                 timestamp with time zone,
         timestamp with time zone,
                 timestamp with time zone,

         character varying(99),
                 character varying(99),
                 character varying(99),
                 character varying(99),
                 character varying(99),
                 character varying(99),
                 character varying(99),
                 character varying(99),
                 character varying(99),
                 character varying(99),
                 character varying(99),
                 character varying(99),
                 character varying(99),
                 character varying(99),
                 character varying(99),*/
        // updated by saurabh
        values.put("data_source", item.getDataSource());
        // RSBY ITEM data
        values.put("rsby_rsbyMemId", item.getRsbyMemId());
        if (item.getRsbyUrnId() != null) {
            values.put("rsby_urnId", item.getRsbyUrnId());

        } else {
            values.put("rsby_urnId", item.getUrnId());
        }
        values.put("rsby_insccode", item.getRsbyInsccode());
        values.put("rsby_policyno", item.getRsbyPolicyno());

        values.put("rsby_policyamt", item.getRsbyPolicyamt());

        values.put("rsby_startdate", item.getRsbyStartdate());
        values.put("rsby_enddate", item.getRsbyEnddate());
        values.put("rsby_issuesTimespam", item.getRsbyIssuesTimespam());

        values.put("rsby_hofnamereg", item.getRsbyHofnamereg());
        values.put("rsby_doorhouse", item.getRsbyDoorhouse());
        values.put("rsby_villageCode", item.getRsbyVillageCode());
        values.put("rsby_panchyatTownCode", item.getRsbyPanchyatTownCode());

        values.put("rsby_blockCode", item.getRsbyBlockCode());
        values.put("rsby_districtCode", item.getRsbyDistrictCode());
        values.put("rsby_stateCode", item.getRsbyStateCode());
        values.put("rsby_memid", item.getRsbyMemid());
        if (item.getRsbyName() != null) {
            values.put("rsby_name", item.getRsbyName());
        } else {
            values.put("rsby_name", item.getName());

        }
        values.put("rsby_dob", item.getRsbyDob());
        if (item.getRsbyGender() != null) {

            values.put("rsby_gender", item.getRsbyGender());
        } else {

            values.put("rsby_gender", item.getGenderid());
        }

        values.put("rsby_relcode", item.getRsbyRelcode());

        values.put("rsby_csmNo", item.getRsbyCsmNo());
        values.put("rsby_cardType", item.getRsbyCardType());
        values.put("rsby_cardCategory", item.getRsbyCardCategory());
        //    AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Data Sourace : " + item.getDataSource());
        values.put("data_source", item.getDataSource());
        //      AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Data Sourace : " + item.getRsbyFamilyPhoto());

        values.put("rsby_familyphoto", item.getRsbyFamilyPhoto());

        SQLiteDatabase db = helpers.getWritableDatabase();
        long insertFlag = db.insert(AppConstant.HOUSE_HOLD_SECC, null,
                values);
        Log.d("MEMBER ", "House hold ADDED" + insertFlag);
        db.close();
        helpers.close();
        return insertFlag;
    }

    public static long updateHousehold(Context context, HouseHoldItem item) {
        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        ContentValues values = new ContentValues();
        values.put("nhps_mem_id", item.getNhpsMemId());
        values.put("ahl_tin", item.getAhlTin());
        values.put("hhd_no", item.getHhdNo());
        values.put("slnomember", item.getSlnomember());
        values.put("slnohhd", item.getSlnohhd());
        values.put("ahlslnohhd", item.getAhlslnohhd());
        values.put("name", item.getName());
        values.put("name_sl", item.getNameSl());
        values.put("fathername", item.getFathername());
        values.put("fathername_sl", item.getFathernameSl());
        values.put("relation", item.getRelation());
        values.put("relation_sl", item.getRelationSl());
        values.put("genderid", item.getGenderid());
        values.put("dob", item.getDob());
        if (item.getUrn_no() != null) {
            values.put("urn_no", item.getUrn_no());
        } else {
            values.put("urn_no", item.getUrnId());
        }
        values.put("mstatusid", item.getMstatusid());
        values.put("mothername", item.getMothername());
        values.put("mothername_sl", item.getMothernameSl());
        values.put("occupation", item.getOccupation());
        values.put("occupation_sl", item.getOccupationSl());
        values.put("caste_group", item.getCasteGroup());
        values.put("statecode", item.getStatecode());
        values.put("districtcode", item.getDistrictcode());
        values.put("tehsilcode", item.getTehsilcode());
        values.put("towncode", item.getTowncode());
        values.put("wardid", item.getWardid());
        values.put("ahlblockno", item.getAhlblockno());
        values.put("typeofhhd", item.getTypeofhhd());
        values.put("hhd_landownedcodes", item.getHhdLandownedcodes());
        values.put("totalirrigated", item.getTotalirrigated());
        values.put("hhd_irri_equip", item.getHhdIrriEquip());
        values.put("totalunirrigated", item.getTotalunirrigated());
        values.put("otherirrigated", item.getOtherirrigated());
        values.put("hhd_asset_mveh", item.getHhdAssetMveh());
        values.put("hhd_magri_equip", item.getHhdMagriEquip());
        values.put("hhd_kcc", item.getHhdKcc());
        values.put("hhd_emp_sector", item.getHhdEmpSector());
        values.put("hhd_emp_erg", item.getHhdEmpErg());
        values.put("hhd_emp_hem", item.getHhdEmpHem());
        values.put("hhd_emp_pit", item.getHhdEmpPit());
        values.put("housing_walltype", item.getHousingWalltype());
        values.put("housing_rooftype", item.getHousingRooftype());
        values.put("housing_no_of_rooms", item.getHousingNoOfRooms());
        values.put("hhd_asset_ref", item.getHhdAssetRef());
        values.put("hhd_asset_telmob", item.getHhdAssetTelmob());
        values.put("livinginshelter", item.getLivinginshelter());
        values.put("hhd_emp_other", item.getHhdEmpOther());
        values.put("hhd_ms", item.getHhdMs());
        values.put("hhd_ptg", item.getHhdPtg());
        values.put("hhd_lrbl", item.getHhdLrbl());
        //  values.put("inclusion integer",item.getI);
        values.put("rural_urban", item.getRuralUrban());
      /*  values.put("state_code", item.getStateCode());
        values.put("district_code", item.getDistrictCode());
        values.put("tehsil_code", item.getTehsilCode());
        values.put("vt_code", item.getVtCode());
        values.put("ward_code", item.getWardCode());*/
        values.put("mdds_stc", item.getMddsStc());
        values.put("mdds_dtc", item.getMddsDtc());
        values.put("mdds_sdtc", item.getMddsDtc());
        values.put("mdds_plcn", item.getMddsPlcn());
        // values.put("ahlsubblockno",item.getAh)
        //  Log.d("Secc Database","Household Status :"+item.getHhStatus());
        values.put("addressline1", item.getAddressline1());
        values.put("addressline2", item.getAddressline2());
        values.put("addressline3", item.getAddressline3());
        values.put("addressline4", item.getAddressline4());
        values.put("addressline5", item.getAddressline5());
        values.put("pincode", item.getPincode());
        AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Household sync error1 " + item.getError_code());

        values.put("error_code", item.getError_code());
        values.put("error_msg", item.getError_msg());
        values.put("error_type", item.getError_type());
        values.put("app_version", item.getAppVersion());

        if (item.getSyncDt() != null) {
            values.put("sync_dt", item.getSyncDt().trim());
        }

        values.put("nhps_id", item.getNhpsId());
        /*values.put("hh_status",item.getHhStatus());
        values.put("locked_save",item.getLockSave());
        values.put("synced_status",item.getSyncedStatus());*/

        if (item.getHhStatus() != null && !item.getHhStatus().equalsIgnoreCase("")) {
            values.put("hh_status", item.getHhStatus().trim());
        } else {
            values.put("hh_status", item.getHhStatus());
        }
        if (item.getSyncedStatus() != null && !item.getSyncedStatus().equalsIgnoreCase("")) {
            values.put("synced_status", item.getSyncedStatus().trim());
        } else {
            values.put("synced_status", item.getSyncedStatus());
        }
        values.put("nhps_id", item.getNhpsId());
        if (item.getLockSave() != null && !item.getLockSave().equalsIgnoreCase("")) {
            values.put("locked_save", item.getLockSave().trim());
        } else {
            values.put("locked_save", item.getLockSave());
        }
        // Log.d("Secc Database","Ahl Tin : "+item.getAhlTin()+" "+item.getNhpsRelationName());
        SQLiteDatabase db = helpers.getWritableDatabase();
        int updateFlag = db.update(AppConstant.HOUSE_HOLD_SECC, values,
                "nhps_mem_id='" + item.getNhpsMemId() + "'", null);
        // Log.d("ID",item.getId()+"FATHER"+item.getFathernmNpr());
        // Log.d("Secc Database", " Update household Value : " + updateFlag);
        db.close();
        helpers.close();
        return updateFlag;
    }

    ///  RSBY common database
    public static long rsbySavePopulation(RSBYItem item, Context context) {
        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        ContentValues values = new ContentValues();
        values.put("dob", item.getDob());
        //     values.put("member_status", item.getMemberStatus());
        if (item.getAadhaarNo() != null) {
            values.put("aadhaar_no", item.getAadhaarNo().trim());
        }
        values.put("rsbyMemId", item.getRsbyMemId());
        values.put("urnId", item.getUrnId());
        //  values.put("issuesTimespam", item.getIssuesTimespam());
        values.put("insccode", item.getInsccode());
        values.put("policyno", item.getPolicyno());
        values.put("policyamt", item.getPolicyamt());
        values.put("startdate", item.getStartdate());
        values.put("enddate", item.getEnddate());
        values.put("hofnamereg", item.getHofnamereg());
        values.put("doorhouse", item.getDoorhouse());
        values.put("villageCode", item.getVillageCode());
        values.put("panchyatTownCode", item.getPanchyatTownCode());
        values.put("blockCode", item.getBlockCode());
        values.put("districtCode", item.getDistrictCode());
        values.put("stateCode", item.getStateCode());
        values.put("memid", item.getMemid());
        values.put("name", item.getName());
        values.put("dob", item.getDob());
        values.put("gender", item.getGender());
        values.put("relcode", item.getRelcode());
        values.put("name_aadhaar", item.getNameAadhaar());
        if (item.getUrnNo() != null) {
            values.put("urn_no", item.getUrnNo());
        } else {
            values.put("urn_no", item.getUrnId());
        }
        values.put("mobile_no", item.getMobileNo());
        values.put("rural_urban", item.getRuralUrban());
        values.put("bank_ifsc", item.getNhpsRelationCode());
        values.put("mark_for_deletion", item.getMarkForDeletion());
        values.put("aadhaar_status", item.getAadhaarStatus());
        AppUtility.showLog(AppConstant.LOG_STATUS, TAG, " Household Status : " + item.getHhStatus());
        values.put("hh_status", item.getHhStatus());
        values.put("mem_status", item.getMemStatus());
        AppUtility.showLog(AppConstant.LOG_STATUS, TAG, " Member Status : " + item.getMemStatus());
        values.put("eid", item.getEid());
        values.put("bank_acc_no", item.getNhpsRelationName());
        values.put("scheme_id", item.getSchemeId());
        values.put("scheme_no", item.getSchemeNo());
        values.put("name_nominee", item.getNameNominee());
        values.put("relation_nominee_code", item.getRelationNomineeCode());
        values.put("nominee_relation_name", item.getNomineeRelationName());
        AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Nominee Relation Name : " + item.getNomineeRelationName());
        values.put("id_type", item.getIdType());
        values.put("id_no", item.getIdNo());
        values.put("name_as_id", item.getNameAsId());
        values.put("member_photo1", item.getMemberPhoto1());
        Log.d("Secc Data base", "ID Image : " + item.getGovtIdPhoto());
        //   values.put("govt_id_photo1",item.getGovtIdPhoto());
        AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Capture Mode : " + item.getAadhaarCapturingMode());
        values.put("aadhaar_capturing_mode", item.getAadhaarCapturingMode());
        values.put("consent", item.getConsent());
        values.put("aadhaar_auth_mode", item.getAadhaarAuthMode());
        values.put("aadhaar_auth_dt", item.getAadhaarAuthDt());
        values.put("aadhaar_verified_by", item.getAadhaarVerifiedBy());
        values.put("userid", item.getUserid());
        values.put("date_updated", item.getDateUpdated());
        values.put("aadhaar_auth", item.getAadhaarAuth());
        values.put("mobile_auth", item.getMobileAuth());
        values.put("urn_auth", item.getUrnAuth());
        values.put("ifsc_auth", item.getIfscAuth());
        values.put("bank_acc_auth", item.getBankAccAuth());
        values.put("state_scheme_code_auth", item.getStateSchemeCodeAuth());
        values.put("scheme_code", item.getSchemeCode());
       /* values.put("data_source", item.getDataSource());*/
        values.put("nhps_id", item.getNhpsId());
        values.put("mobile_auth_dt", item.getMobileAuthDt());
        values.put("urn_auth_dt", item.getUrnAuthDt());
        values.put("ifsc_auth_dt", item.getIfscAuthDt());
        values.put("bank_acc_auth_dt", item.getBankAccAuthDt());
        values.put("state_scheme_code_auth_dt", item.getStateSchemeCodeAuthDt());
        values.put("consent_dt", item.getConsentDt());
        values.put("aadhaar_gender", item.getAadhaarGender());
        values.put("aadhaar_yob", item.getAadhaarYob());
        values.put("aadhaar_dob", item.getAadhaarDob());
        values.put("aadhaar_co", item.getAadhaarCo());
        values.put("aadhaar_gname", item.getAadhaarGname());
        values.put("aadhaar_house", item.getAadhaarHouse());
        values.put("aadhaar_street", item.getAadhaarStreet());
        values.put("aadhaar_loc", item.getAadhaarLoc());
        values.put("aadhaar_vtc", item.getAadhaarVtc());
        values.put("aadhaar_po", item.getAadhaarPo());
        values.put("aadhaar_dist", item.getAadhaarDist());
        values.put("aadhaar_subdist", item.getAadhaarSubdist());
        values.put("aadhaar_state", item.getAadhaarState());
        values.put("aadhaar_pc", item.getAadhaarPc());
        values.put("aadhaar_lm", item.getNhpsRelationName());
        values.put("latitude", item.getLatitude());
        values.put("longitude", item.getLongitude());
        values.put("whose_mobile", item.getWhoseMobile());
        //  Log.d("Secc Databs"," Relation Code :"+item.getNhpsRelationCode());
        values.put("nhps_relation_code", item.getNhpsRelationCode());
        values.put("nhps_relation_name", item.getNhpsRelationName());
        values.put("locked_save", item.getLockedSave());
        /*values.put("govt_id_survey_status", item.getGovtIdSurveyedStat());
        values.put("aadhar_survey_status", item.getAadhaarSurveyedStat());
        values.put("photo_capture_status", item.getPhotoSurveyedStatus());
        values.put("mobile_surveyed_status", item.getMobileNoSurveyedStat());
        values.put("nominee_surveyed_status", item.getNomineeDetailSurveyedStat());*/
        values.put("app_version", item.getAppVersion());
        values.put("synced_status", item.getSyncedStatus());

        values.put("vl_aadharNo", item.getVl_aadharNo());
        values.put("vl_blockCode", item.getVl_blockCode());
        values.put("vl_districtCode", item.getVl_districtCode());
        values.put("vl_ruralUrban", item.getVl_ruralUrban());
        values.put("vl_stateCode", item.getVl_stateCode());
        values.put("vl_subBlockcode", item.getVl_subBlockcode());

        AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Sub block code : " + item.getVl_subBlockcode());
        values.put("vl_tehsilCode", item.getVl_tehsilCode());
        values.put("vl_vtCode", item.getVl_vtCode());
        values.put("vl_wardCode", item.getVl_wardCode());
        values.put("cardType", item.getCardType());
        values.put("cardCategory", item.getCardCategory());
        values.put("csmNo", item.getCsmNo());


     /*   if(item.getSyncDt()!=null) {
            values.put("sync_dt",item.getSyncDt().trim());
        }*/

        //values.put("consent_photo", item.get);

        SQLiteDatabase db = helpers.getWritableDatabase();
        long insertFlag = db.insert(AppConstant.RSBY_POPULATION_TABLE, null,
                values);
        Log.d("MEMBER ", "ADDED" + insertFlag);
        db.close();
        helpers.close();
        return insertFlag;
    }

    public static long rsbySaveHousehold(RsbyHouseholdItem item, Context context) {
        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        ContentValues values = new ContentValues();
        values.put("rsbyMemId", item.getRsbyMemId());
        values.put("urnId", item.getUrnId());
        values.put("issuesTimespam", item.getIssuesTimespam());
        values.put("hofnamereg", item.getHofnamereg());
        values.put("rsby_familyphoto", item.getHofImage());
        values.put("insccode", item.getInsccode());
        values.put("policyno", item.getPolicyno());
        values.put("policyamt", item.getPolicyamt());
        values.put("startdate", item.getStartdate());
        values.put("enddate", item.getEnddate());
        values.put("cardType", item.getCardType());
        values.put("cardCategory", item.getCardCategory());
        values.put("csmNo", item.getCsmNo());
        values.put("doorhouse", item.getDoorhouse());
        values.put("villageCode", item.getVillageCode());
        values.put("panchyatTownCode", item.getPanchyatTownCode());
        values.put("blockCode", item.getBlockCode());
        values.put("districtCode", item.getDistrictCode());
        values.put("stateCode", item.getStateCode());
        values.put("memid", item.getMemid());
        values.put("name", item.getName());
        values.put("dob", item.getDob());
        values.put("gender", item.getGender());
        values.put("relcode", item.getRelcode());

        values.put("app_version", item.getAppVersion());
        if (item.getSyncDt() != null) {
            values.put("sync_dt", item.getSyncDt().trim());
        }
        if (item.getHhStatus() != null && !item.getHhStatus().equalsIgnoreCase("")) {
            values.put("hh_status", item.getHhStatus().trim());
        }
        if (item.getSyncedStatus() != null && !item.getSyncedStatus().equalsIgnoreCase("")) {
            values.put("synced_status", item.getSyncedStatus().trim());
        }
        values.put("nhps_id", item.getNhpsId());
        if (item.getLockedSave() != null && !item.getLockedSave().equalsIgnoreCase("")) {
            values.put("locked_save", item.getLockedSave().trim());
        }
        values.put("vl_aadharNo", item.getVlAadharNo());
        values.put("vl_blockCode", item.getVlBlockCode());
        values.put("vl_districtCode", item.getVlDistrictCode());
        values.put("vl_ruralUrban", item.getVlRuralUrban());
        values.put("vl_stateCode", item.getVlStateCode());
        values.put("vl_subBlockcode", item.getVlSubBlockcode());
        values.put("vl_tehsilCode", item.getVlTehsilCode());
        AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Tehsil Code : " + item.getVlTehsilCode());
        values.put("vl_vtCode", item.getVlVtCode());
        values.put("vl_wardCode", item.getVlWardCode());
        AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "ward Code : " + item.getVlWardCode());


        SQLiteDatabase db = helpers.getWritableDatabase();
        long insertFlag = db.insert(AppConstant.RSBY_HOUSEHOD_TABLE, null, values);
        db.close();
        helpers.close();
        return insertFlag;
    }

    public static long rsbyUpdatePopulation(RSBYItem item, Context context) {
        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        ContentValues values = new ContentValues();
        values.put("dob", item.getDob());
        if (item.getAadhaarNo() != null) {
            values.put("aadhaar_no", item.getAadhaarNo().trim());
        }
        values.put("insccode", item.getInsccode());
        values.put("policyno", item.getPolicyno());
        values.put("policyamt", item.getPolicyamt());
        values.put("startdate", item.getStartdate());
        values.put("enddate", item.getEnddate());
        values.put("rsbyMemId", item.getRsbyMemId());
        values.put("urnId", item.getUrnId());
        values.put("issuesTimespam", item.getIssuesTimespam());
        values.put("hofnamereg", item.getHofnamereg());
        values.put("doorhouse", item.getDoorhouse());
        values.put("villageCode", item.getVillageCode());
        values.put("panchyatTownCode", item.getPanchyatTownCode());
        values.put("blockCode", item.getBlockCode());
        values.put("districtCode", item.getDistrictCode());
        values.put("stateCode", item.getStateCode());
        values.put("memid", item.getMemid());
        values.put("name", item.getName());
        values.put("dob", item.getDob());
        values.put("gender", item.getGender());
        values.put("relcode", item.getRelcode());
        values.put("name_aadhaar", item.getNameAadhaar());
        values.put("urn_no", item.getUrnNo());
        values.put("mobile_no", item.getMobileNo());
        values.put("rural_urban", item.getRuralUrban());
        values.put("bank_ifsc", item.getNhpsRelationCode());
        values.put("mark_for_deletion", item.getMarkForDeletion());
        values.put("aadhaar_status", item.getAadhaarStatus());
        values.put("hh_status", item.getHhStatus());
        values.put("mem_status", item.getMemStatus());
        values.put("eid", item.getEid());
        values.put("bank_acc_no", item.getNhpsRelationName());
        values.put("scheme_id", item.getSchemeId());
        values.put("scheme_no", item.getSchemeNo());
        values.put("name_nominee", item.getNameNominee());
        values.put("relation_nominee_code", item.getRelationNomineeCode());
        values.put("nominee_relation_name", item.getNomineeRelationName());
        values.put("id_type", item.getIdType());
        values.put("id_no", item.getIdNo());
        values.put("name_as_id", item.getNameAsId());
        values.put("member_photo1", item.getMemberPhoto1());
        values.put("govt_id_photo", item.getGovtIdPhoto());
        values.put("aadhaar_capturing_mode", item.getAadhaarCapturingMode());
        values.put("consent", item.getConsent());
        values.put("aadhaar_auth_mode", item.getAadhaarAuthMode());
        values.put("aadhaar_auth_dt", item.getAadhaarAuthDt());
        values.put("aadhaar_verified_by", item.getAadhaarVerifiedBy());
        values.put("userid", item.getUserid());
        values.put("date_updated", item.getDateUpdated());
        values.put("aadhaar_auth", item.getAadhaarAuth());
        values.put("mobile_auth", item.getMobileAuth());
        values.put("urn_auth", item.getUrnAuth());
        values.put("ifsc_auth", item.getIfscAuth());
        values.put("bank_acc_auth", item.getBankAccAuth());
        values.put("state_scheme_code_auth", item.getStateSchemeCodeAuth());
        values.put("scheme_code", item.getSchemeCode());
        values.put("data_source", item.getDataSource());
        values.put("nhps_id", item.getNhpsId());
        values.put("mobile_auth_dt", item.getMobileAuthDt());
        values.put("urn_auth_dt", item.getUrnAuthDt());
        values.put("ifsc_auth_dt", item.getIfscAuthDt());
        values.put("bank_acc_auth_dt", item.getBankAccAuthDt());
        values.put("state_scheme_code_auth_dt", item.getStateSchemeCodeAuthDt());
        values.put("consent_dt", item.getConsentDt());
        values.put("aadhaar_gender", item.getAadhaarGender());
        values.put("aadhaar_yob", item.getAadhaarYob());
        values.put("aadhaar_dob", item.getAadhaarDob());
        values.put("aadhaar_co", item.getAadhaarCo());
        values.put("aadhaar_gname", item.getAadhaarGname());
        values.put("aadhaar_house", item.getAadhaarHouse());
        values.put("aadhaar_street", item.getAadhaarStreet());
        values.put("aadhaar_loc", item.getAadhaarLoc());
        values.put("aadhaar_vtc", item.getAadhaarVtc());
        values.put("aadhaar_po", item.getAadhaarPo());
        values.put("aadhaar_dist", item.getAadhaarDist());
        values.put("aadhaar_subdist", item.getAadhaarSubdist());
        values.put("aadhaar_state", item.getAadhaarState());
        values.put("aadhaar_pc", item.getAadhaarPc());
        values.put("aadhaar_lm", item.getNhpsRelationName());
        values.put("latitude", item.getLatitude());
        values.put("longitude", item.getLongitude());
        values.put("whose_mobile", item.getWhoseMobile());
        values.put("nhps_relation_code", item.getNhpsRelationCode());
        values.put("nhps_relation_name", item.getNhpsRelationName());
        values.put("locked_save", item.getLockedSave());
/*        values.put("govt_id_survey_status", item.getGovtIdSurveyedStat());
        values.put("aadhar_survey_status", item.getAadhaarSurveyedStat());
        values.put("photo_capture_status", item.getPhotoSurveyedStatus());
        values.put("mobile_surveyed_status", item.getMobileNoSurveyedStat());
        values.put("nominee_surveyed_status", item.getNomineeDetailSurveyedStat());*/
        values.put("app_version", item.getAppVersion());
        values.put("synced_status", item.getSyncedStatus());
        if (item.getSyncDt() != null) {
            values.put("sync_dt", item.getSyncDt().trim());
        }
        values.put("vl_aadharNo", item.getVl_aadharNo());
        values.put("vl_blockCode", item.getVl_blockCode());
        values.put("vl_districtCode", item.getVl_districtCode());
        values.put("vl_ruralUrban", item.getVl_ruralUrban());
        values.put("vl_stateCode", item.getVl_stateCode());
        values.put("vl_subBlockcode", item.getVl_subBlockcode());
        values.put("vl_tehsilCode", item.getVl_tehsilCode());
        values.put("vl_vtCode", item.getVl_vtCode());
        values.put("vl_wardCode", item.getVl_wardCode());
        values.put("cardType", item.getCardType());
        values.put("cardCategory", item.getCardCategory());
        values.put("csmNo", item.getCsmNo());
        SQLiteDatabase db = helpers.getWritableDatabase();
        int updateFlag = db.update(AppConstant.RSBY_POPULATION_TABLE, values,
                "rsbyMemId='" + item.getRsbyMemId() + "'", null);
        db.close();
        helpers.close();
        return updateFlag;
    }

    public static long rsbyUpdateHousehold(Context context, RsbyHouseholdItem item) {
        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        ContentValues values = new ContentValues();
        values.put("rsbyMemId", item.getRsbyMemId());
        values.put("urnId", item.getUrnId());
        values.put("issuesTimespam", item.getIssuesTimespam());
        values.put("hofnamereg", item.getHofnamereg());
        values.put("insccode", item.getInsccode());
        values.put("policyno", item.getPolicyno());
        values.put("policyamt", item.getPolicyamt());
        values.put("startdate", item.getStartdate());
        values.put("enddate", item.getEnddate());
        values.put("doorhouse", item.getDoorhouse());
        values.put("villageCode", item.getVillageCode());
        values.put("panchyatTownCode", item.getPanchyatTownCode());
        values.put("blockCode", item.getBlockCode());
        values.put("rsby_familyphoto", item.getHofImage());
        values.put("districtCode", item.getDistrictCode());
        values.put("stateCode", item.getStateCode());
        values.put("memid", item.getMemid());
        values.put("name", item.getName());

        values.put("dob", item.getDob());
        values.put("gender", item.getGender());
        values.put("relcode", item.getRelcode());
        values.put("app_version", item.getAppVersion());
        if (item.getSyncDt() != null) {
            values.put("sync_dt", item.getSyncDt().trim());
        }
        // values.put("ahlsubblockno",item.getAh)
        if (item.getHhStatus() != null && !item.getHhStatus().equalsIgnoreCase("")) {
            values.put("hh_status", item.getHhStatus().trim());
        }
        if (item.getSyncedStatus() != null && !item.getSyncedStatus().equalsIgnoreCase("")) {
            values.put("synced_status", item.getSyncedStatus().trim());
        }
        values.put("nhps_id", item.getNhpsId());
        if (item.getLockedSave() != null && !item.getLockedSave().equalsIgnoreCase("")) {
            values.put("locked_save", item.getLockedSave().trim());
        }
        values.put("vl_aadharNo", item.getVlAadharNo());
        values.put("vl_blockCode", item.getVlBlockCode());
        values.put("vl_districtCode", item.getVlDistrictCode());
        values.put("vl_ruralUrban", item.getVlRuralUrban());
        values.put("vl_stateCode", item.getVlStateCode());
        values.put("vl_subBlockcode", item.getVlSubBlockcode());
        values.put("vl_tehsilCode", item.getVlTehsilCode());
        values.put("vl_vtCode", item.getVlVtCode());
        values.put("vl_wardCode", item.getVlWardCode());
        values.put("cardType", item.getCardType());
        values.put("cardCategory", item.getCardCategory());
        values.put("csmNo", item.getCsmNo());
        SQLiteDatabase db = helpers.getWritableDatabase();
        int updateFlag = db.update(AppConstant.RSBY_HOUSEHOD_TABLE, values,
                "rsbyMemId='" + item.getRsbyMemId() + "'", null);
        // Log.d("ID",item.getId()+"FATHER"+item.getFathernmNpr());
        // Log.d("Secc Database", " Update household Value : " + updateFlag);
        db.close();
        helpers.close();
        return updateFlag;
    }

    public static ArrayList<RSBYItem> getRsbyMemberListNew(Context context, String query) {
        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        SQLiteDatabase mDatabase = helpers.createOrOpenDatabase();
        ArrayList<RSBYItem> seccMemberItems = new ArrayList<>();
        RSBYItem item;
        Cursor cur = mDatabase.rawQuery(query, null);
        Log.d("SECC Data ", " Query : " + query + " count :" + cur.getCount());
        if (cur != null) {
            if (cur.getCount() > 0) {
                cur.moveToFirst();
                while (cur.isAfterLast() == false) {
                    item = new RSBYItem();

                    item.setIssuesTimespam(cur.getString(cur.getColumnIndex("issuesTimespam")));
                    item.setHofnamereg(cur.getString(cur.getColumnIndex("hofnamereg")));
                    item.setDoorhouse(cur.getString(cur.getColumnIndex("doorhouse")));
                    item.setVillageCode(cur.getString(cur.getColumnIndex("villageCode")));
                    item.setPanchyatTownCode(cur.getString(cur.getColumnIndex("panchyatTownCode")));
                    item.setBlockCode(cur.getString(cur.getColumnIndex("blockCode")));
                    item.setDistrictCode(cur.getString(cur.getColumnIndex("districtCode")));
                    item.setStateCode(cur.getString(cur.getColumnIndex("stateCode")));

                    item.setAadhaarNo(cur.getString(cur.getColumnIndex("aadhaar_no")));
                    item.setNameAadhaar(cur.getString(cur.getColumnIndex("name_aadhaar")));
                    item.setUrnNo(cur.getString(cur.getColumnIndex("urn_no")));
                    item.setMobileNo(cur.getString(cur.getColumnIndex("mobile_no")));
                    item.setRuralUrban(cur.getString(cur.getColumnIndex("rural_urban")));
                    item.setBankIfsc(cur.getString(cur.getColumnIndex("bank_ifsc")));
                    item.setMarkForDeletion(cur.getString(cur.getColumnIndex("mark_for_deletion")));
                    item.setName(cur.getString(cur.getColumnIndex("name")));
                    item.setUrnId(cur.getString(cur.getColumnIndex("urnId")));
                    item.setDob(cur.getString(cur.getColumnIndex("dob")));
                    item.setGender(cur.getString(cur.getColumnIndex("gender")));
                    item.setRelcode(cur.getString(cur.getColumnIndex("relcode")));
                    item.setMemid(cur.getString(cur.getColumnIndex("memid")));
                    item.setRsbyMemId(cur.getString(cur.getColumnIndex("rsbyMemId")));

                    item.setInsccode(cur.getString(cur.getColumnIndex("insccode")));
                    item.setPolicyno(cur.getString(cur.getColumnIndex("policyno")));
                    item.setPolicyamt(cur.getString(cur.getColumnIndex("policyamt")));
                    item.setStartdate(cur.getString(cur.getColumnIndex("startdate")));
                    item.setEnddate(cur.getString(cur.getColumnIndex("enddate")));

                    item.setEid(cur.getString(cur.getColumnIndex("eid")));
                    item.setBankAccNo(cur.getString(cur.getColumnIndex("bank_acc_no")));
                    item.setSchemeId(cur.getString(cur.getColumnIndex("scheme_id")));
                    item.setSchemeNo(cur.getString(cur.getColumnIndex("scheme_no")));
                    item.setNameNominee(cur.getString(cur.getColumnIndex("name_nominee")));
                    item.setRelationNomineeCode(cur.getString(cur.getColumnIndex("relation_nominee_code")));
                    item.setIdType(cur.getString(cur.getColumnIndex("id_type")));
                    item.setIdNo(cur.getString(cur.getColumnIndex("id_no")));
                    item.setNameAsId(cur.getString(cur.getColumnIndex("name_as_id")));
                    /*if(cur.getBlob(cur.getColumnIndex("id_photo1"))!=null) {
                        item.setIdPhoto1(AppUtility.convertBitmapToString(BitmapFactory.decodeByteArray(cur.getBlob(cur.getColumnIndex("id_photo1")),
                                0, cur.getBlob(cur.getColumnIndex("id_photo1")).length)));
                    }*/
                    // String img=cur.getString(cur.getColumnIndex("govt_id_photo1"));
                    //  Log.d("Secc Database","Govt ID Img111111111111 : "+img);
                    item.setGovtIdPhoto(cur.getString(cur.getColumnIndex("govt_id_photo")));
                    // item.set(cur.getString(cur.getColumnIndex("id_photo2")));
                    // item.set(cur.getString(cur.getColumnIndex("member_photo")));
                    item.setAadhaarCapturingMode(cur.getString(cur.getColumnIndex("aadhaar_capturing_mode")));
                    item.setConsent(cur.getString(cur.getColumnIndex("consent")));
                    item.setAadhaarAuthMode(cur.getString(cur.getColumnIndex("aadhaar_auth_mode")));
                    item.setAadhaarAuthDt(cur.getString(cur.getColumnIndex("aadhaar_auth_dt")));
                    item.setAadhaarVerifiedBy(cur.getString(cur.getColumnIndex("aadhaar_verified_by")));
                    item.setUserid(cur.getString(cur.getColumnIndex("userid")));
                    item.setDateUpdated(cur.getString(cur.getColumnIndex("date_updated")));
                    item.setAadhaarAuth(cur.getString(cur.getColumnIndex("aadhaar_auth")));
                    item.setMobileAuth(cur.getString(cur.getColumnIndex("mobile_auth")));
                    item.setUrnAuth(cur.getString(cur.getColumnIndex("urn_auth")));
                    item.setIfscAuth(cur.getString(cur.getColumnIndex("ifsc_auth")));
                    item.setBankAccAuth(cur.getString(cur.getColumnIndex("bank_acc_auth")));
                    item.setStateSchemeCodeAuth(cur.getString(cur.getColumnIndex("state_scheme_code_auth")));
                    item.setSchemeCode(cur.getString(cur.getColumnIndex("scheme_code")));
                    item.setDataSource(cur.getString(cur.getColumnIndex("data_source")));
                    item.setNhpsId(cur.getString(cur.getColumnIndex("nhps_id")));
                    item.setMobileAuthDt(cur.getString(cur.getColumnIndex("mobile_auth_dt")));
                    item.setUrnAuthDt(cur.getString(cur.getColumnIndex("urn_auth_dt")));
                    item.setIfscAuthDt(cur.getString(cur.getColumnIndex("ifsc_auth_dt")));
                    item.setBankAccAuthDt(cur.getString(cur.getColumnIndex("bank_acc_auth_dt")));
                    item.setStateSchemeCodeAuthDt(cur.getString(cur.getColumnIndex("state_scheme_code_auth_dt")));
                    item.setConsentDt(cur.getString(cur.getColumnIndex("consent_dt")));
                    item.setAadhaarGender(cur.getString(cur.getColumnIndex("aadhaar_gender")));
                    item.setAadhaarYob(cur.getString(cur.getColumnIndex("aadhaar_yob")));
                    item.setAadhaarDob(cur.getString(cur.getColumnIndex("aadhaar_dob")));
                    item.setAadhaarCo(cur.getString(cur.getColumnIndex("aadhaar_co")));
                    item.setAadhaarGname(cur.getString(cur.getColumnIndex("aadhaar_gname")));
                    item.setAadhaarHouse(cur.getString(cur.getColumnIndex("aadhaar_house")));
                    item.setAadhaarStreet(cur.getString(cur.getColumnIndex("aadhaar_street")));
                    item.setAadhaarLoc(cur.getString(cur.getColumnIndex("aadhaar_loc")));
                    item.setAadhaarVtc(cur.getString(cur.getColumnIndex("aadhaar_vtc")));
                    item.setAadhaarPo(cur.getString(cur.getColumnIndex("aadhaar_po")));
                    item.setAadhaarDist(cur.getString(cur.getColumnIndex("aadhaar_dist")));
                    item.setAadhaarSubdist(cur.getString(cur.getColumnIndex("aadhaar_subdist")));
                    item.setAadhaarState(cur.getString(cur.getColumnIndex("aadhaar_state")));
                    item.setAadhaarPc(cur.getString(cur.getColumnIndex("aadhaar_pc")));
                    item.setAadhaarLm(cur.getString(cur.getColumnIndex("aadhaar_lm")));
                    item.setLatitude(cur.getString(cur.getColumnIndex("latitude")));
                    item.setLongitude(cur.getString(cur.getColumnIndex("longitude")));
                    item.setWhoseMobile(cur.getString(cur.getColumnIndex("whose_mobile")));
                    item.setMemberPhoto1(cur.getString(cur.getColumnIndex("member_photo1")));

                    item.setNhpsRelationCode(cur.getString(cur.getColumnIndex("nhps_relation_code")));
                    item.setNhpsRelationName(cur.getString(cur.getColumnIndex("nhps_relation_name")));
                    item.setNomineeRelationName(cur.getString(cur.getColumnIndex("nominee_relation_name")));

                   /* item.setGovtIdSurveyedStat(cur.getString(cur.getColumnIndex("govt_id_survey_status")));
                    item.setAadhaarSurveyedStat(cur.getString(cur.getColumnIndex("aadhar_survey_status")));
                    item.setPhotoSurveyedStatus(cur.getString(cur.getColumnIndex("photo_capture_status")));
                    item.setMobileNoSurveyedStat(cur.getString(cur.getColumnIndex("mobile_surveyed_status")));
                    item.setNomineeDetailSurveyedStat(cur.getString(cur.getColumnIndex("nominee_surveyed_status")));*/


                    String hhStatus = cur.getString(cur.getColumnIndex("hh_status"));
                    String memStatus = cur.getString(cur.getColumnIndex("mem_status"));
                    item.setLockedSave(cur.getString(cur.getColumnIndex("locked_save")));
                    item.setAadhaarStatus(cur.getString(cur.getColumnIndex("aadhaar_status")));
                    String syncStatus = cur.getString(cur.getColumnIndex("synced_status"));
                    if (memStatus != null) {
                        item.setMemStatus(memStatus.trim());
                    }
                    if (hhStatus != null && !hhStatus.equalsIgnoreCase("")) {
                        item.setHhStatus(hhStatus.trim());
                    }
                    if (syncStatus != null && !syncStatus.equalsIgnoreCase("")) {
                        item.setSyncedStatus(syncStatus.trim());
                    }
                    // item.setSyncDt(cur.getString(cur.getColumnIndex("sync_dt")));
                    item.setError_type(cur.getString(cur.getColumnIndex("error_type")));
                    item.setError_code(cur.getString(cur.getColumnIndex("error_msg")));
                    item.setError_msg(cur.getString(cur.getColumnIndex("error_msg")));
                    item.setAppVersion(cur.getString(cur.getColumnIndex("app_version")));
                    item.setVl_aadharNo(cur.getString(cur.getColumnIndex("vl_aadharNo")));
                    item.setVl_blockCode(cur.getString(cur.getColumnIndex("vl_blockCode")));
                    item.setVl_districtCode(cur.getString(cur.getColumnIndex("vl_districtCode")));
                    item.setVl_ruralUrban(cur.getString(cur.getColumnIndex("vl_ruralUrban")));
                    item.setVl_stateCode(cur.getString(cur.getColumnIndex("vl_stateCode")));
                    AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Sub block : " + cur.getString(cur.getColumnIndex("vl_subBlockcode")));

                    item.setVl_subBlockcode(cur.getString(cur.getColumnIndex("vl_subBlockcode")));
                    item.setVl_tehsilCode(cur.getString(cur.getColumnIndex("vl_tehsilCode")));
                    item.setVl_vtCode(cur.getString(cur.getColumnIndex("vl_vtCode")));
                    item.setVl_wardCode(cur.getString(cur.getColumnIndex("vl_wardCode")));
                    item.setCsmNo(cur.getString(cur.getColumnIndex("csmNo")));
                    item.setCardCategory(cur.getString(cur.getColumnIndex("cardCategory")));
                    item.setCardType(cur.getString(cur.getColumnIndex("cardType")));
                    //     AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Sync Date and time"+item.getSyncDt());
                    seccMemberItems.add(item);

                    //  AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Fetching data");

                    cur.moveToNext();
                }
            }
            cur.close();
        }
        mDatabase.close();
        helpers.close();
        return seccMemberItems;
    }

    public static RSBYItem getRsbyMember(Context context, String query) {
        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        SQLiteDatabase mDatabase = helpers.createOrOpenDatabase();
        RSBYItem item = null;
//        ArrayList<SeccMemberItem> seccMemberItems=new ArrayList<>();
        Cursor cur = mDatabase.rawQuery(query, null);
        // Log.d("SECC Data ", " Query : " + cur.getCount());
        if (cur != null) {
            if (cur.getCount() > 0) {
                cur.moveToFirst();
                while (cur.isAfterLast() == false) {
                    item = new RSBYItem();
                    item.setIssuesTimespam(cur.getString(cur.getColumnIndex("issuesTimespam")));
                    item.setHofnamereg(cur.getString(cur.getColumnIndex("hofnamereg")));
                    item.setDoorhouse(cur.getString(cur.getColumnIndex("doorhouse")));
                    item.setVillageCode(cur.getString(cur.getColumnIndex("villageCode")));
                    item.setPanchyatTownCode(cur.getString(cur.getColumnIndex("panchyatTownCode")));
                    item.setBlockCode(cur.getString(cur.getColumnIndex("blockCode")));
                    item.setDistrictCode(cur.getString(cur.getColumnIndex("districtCode")));
                    item.setStateCode(cur.getString(cur.getColumnIndex("stateCode")));
                    item.setAadhaarNo(cur.getString(cur.getColumnIndex("aadhaar_no")));
                    item.setNameAadhaar(cur.getString(cur.getColumnIndex("name_aadhaar")));
                    item.setUrnNo(cur.getString(cur.getColumnIndex("urn_no")));
                    item.setMobileNo(cur.getString(cur.getColumnIndex("mobile_no")));
                    item.setRuralUrban(cur.getString(cur.getColumnIndex("rural_urban")));
                    item.setBankIfsc(cur.getString(cur.getColumnIndex("bank_ifsc")));
                    item.setMarkForDeletion(cur.getString(cur.getColumnIndex("mark_for_deletion")));
                    item.setName(cur.getString(cur.getColumnIndex("name")));
                    item.setUrnId(cur.getString(cur.getColumnIndex("urnId")));
                    item.setDob(cur.getString(cur.getColumnIndex("dob")));
                    item.setGender(cur.getString(cur.getColumnIndex("gender")));
                    item.setRelcode(cur.getString(cur.getColumnIndex("relcode")));
                    item.setMemid(cur.getString(cur.getColumnIndex("memid")));
                    item.setRsbyMemId(cur.getString(cur.getColumnIndex("rsbyMemId")));

                    item.setInsccode(cur.getString(cur.getColumnIndex("insccode")));
                    item.setPolicyno(cur.getString(cur.getColumnIndex("policyno")));
                    item.setPolicyamt(cur.getString(cur.getColumnIndex("policyamt")));
                    item.setStartdate(cur.getString(cur.getColumnIndex("startdate")));
                    item.setEnddate(cur.getString(cur.getColumnIndex("enddate")));

                    item.setEid(cur.getString(cur.getColumnIndex("eid")));
                    item.setBankAccNo(cur.getString(cur.getColumnIndex("bank_acc_no")));
                    item.setSchemeId(cur.getString(cur.getColumnIndex("scheme_id")));
                    item.setSchemeNo(cur.getString(cur.getColumnIndex("scheme_no")));
                    item.setNameNominee(cur.getString(cur.getColumnIndex("name_nominee")));
                    item.setRelationNomineeCode(cur.getString(cur.getColumnIndex("relation_nominee_code")));
                    item.setIdType(cur.getString(cur.getColumnIndex("id_type")));
                    item.setIdNo(cur.getString(cur.getColumnIndex("id_no")));
                    item.setNameAsId(cur.getString(cur.getColumnIndex("name_as_id")));
                    /*if(cur.getBlob(cur.getColumnIndex("id_photo1"))!=null) {
                        item.setIdPhoto1(AppUtility.convertBitmapToString(BitmapFactory.decodeByteArray(cur.getBlob(cur.getColumnIndex("id_photo1")),
                                0, cur.getBlob(cur.getColumnIndex("id_photo1")).length)));
                    }*/
                    // String img=cur.getString(cur.getColumnIndex("govt_id_photo1"));
                    //  Log.d("Secc Database","Govt ID Img111111111111 : "+img);
                    item.setGovtIdPhoto(cur.getString(cur.getColumnIndex("govt_id_photo")));
                    // item.set(cur.getString(cur.getColumnIndex("id_photo2")));
                    // item.set(cur.getString(cur.getColumnIndex("member_photo")));
                    item.setAadhaarCapturingMode(cur.getString(cur.getColumnIndex("aadhaar_capturing_mode")));
                    item.setConsent(cur.getString(cur.getColumnIndex("consent")));
                    item.setAadhaarAuthMode(cur.getString(cur.getColumnIndex("aadhaar_auth_mode")));
                    item.setAadhaarAuthDt(cur.getString(cur.getColumnIndex("aadhaar_auth_dt")));
                    item.setAadhaarVerifiedBy(cur.getString(cur.getColumnIndex("aadhaar_verified_by")));
                    item.setUserid(cur.getString(cur.getColumnIndex("userid")));
                    item.setDateUpdated(cur.getString(cur.getColumnIndex("date_updated")));
                    item.setAadhaarAuth(cur.getString(cur.getColumnIndex("aadhaar_auth")));
                    item.setMobileAuth(cur.getString(cur.getColumnIndex("mobile_auth")));
                    item.setUrnAuth(cur.getString(cur.getColumnIndex("urn_auth")));
                    item.setIfscAuth(cur.getString(cur.getColumnIndex("ifsc_auth")));
                    item.setBankAccAuth(cur.getString(cur.getColumnIndex("bank_acc_auth")));
                    item.setStateSchemeCodeAuth(cur.getString(cur.getColumnIndex("state_scheme_code_auth")));
                    item.setSchemeCode(cur.getString(cur.getColumnIndex("scheme_code")));
                    item.setDataSource(cur.getString(cur.getColumnIndex("data_source")));
                    item.setNhpsId(cur.getString(cur.getColumnIndex("nhps_id")));
                    item.setMobileAuthDt(cur.getString(cur.getColumnIndex("mobile_auth_dt")));
                    item.setUrnAuthDt(cur.getString(cur.getColumnIndex("urn_auth_dt")));
                    item.setIfscAuthDt(cur.getString(cur.getColumnIndex("ifsc_auth_dt")));
                    item.setBankAccAuthDt(cur.getString(cur.getColumnIndex("bank_acc_auth_dt")));
                    item.setStateSchemeCodeAuthDt(cur.getString(cur.getColumnIndex("state_scheme_code_auth_dt")));
                    item.setConsentDt(cur.getString(cur.getColumnIndex("consent_dt")));
                    item.setAadhaarGender(cur.getString(cur.getColumnIndex("aadhaar_gender")));
                    item.setAadhaarYob(cur.getString(cur.getColumnIndex("aadhaar_yob")));
                    item.setAadhaarDob(cur.getString(cur.getColumnIndex("aadhaar_dob")));
                    item.setAadhaarCo(cur.getString(cur.getColumnIndex("aadhaar_co")));
                    item.setAadhaarGname(cur.getString(cur.getColumnIndex("aadhaar_gname")));
                    item.setAadhaarHouse(cur.getString(cur.getColumnIndex("aadhaar_house")));
                    item.setAadhaarStreet(cur.getString(cur.getColumnIndex("aadhaar_street")));
                    item.setAadhaarLoc(cur.getString(cur.getColumnIndex("aadhaar_loc")));
                    item.setAadhaarVtc(cur.getString(cur.getColumnIndex("aadhaar_vtc")));
                    item.setAadhaarPo(cur.getString(cur.getColumnIndex("aadhaar_po")));
                    item.setAadhaarDist(cur.getString(cur.getColumnIndex("aadhaar_dist")));
                    item.setAadhaarSubdist(cur.getString(cur.getColumnIndex("aadhaar_subdist")));
                    item.setAadhaarState(cur.getString(cur.getColumnIndex("aadhaar_state")));
                    item.setAadhaarPc(cur.getString(cur.getColumnIndex("aadhaar_pc")));
                    item.setAadhaarLm(cur.getString(cur.getColumnIndex("aadhaar_lm")));
                    item.setLatitude(cur.getString(cur.getColumnIndex("latitude")));
                    item.setLongitude(cur.getString(cur.getColumnIndex("longitude")));
                    item.setWhoseMobile(cur.getString(cur.getColumnIndex("whose_mobile")));
                    item.setMemberPhoto1(cur.getString(cur.getColumnIndex("member_photo1")));

                    item.setNhpsRelationCode(cur.getString(cur.getColumnIndex("nhps_relation_code")));
                    item.setNhpsRelationName(cur.getString(cur.getColumnIndex("nhps_relation_name")));
                    item.setNomineeRelationName(cur.getString(cur.getColumnIndex("nominee_relation_name")));

                   /* item.setGovtIdSurveyedStat(cur.getString(cur.getColumnIndex("govt_id_survey_status")));
                    item.setAadhaarSurveyedStat(cur.getString(cur.getColumnIndex("aadhar_survey_status")));
                    item.setPhotoSurveyedStatus(cur.getString(cur.getColumnIndex("photo_capture_status")));
                    item.setMobileNoSurveyedStat(cur.getString(cur.getColumnIndex("mobile_surveyed_status")));
                    item.setNomineeDetailSurveyedStat(cur.getString(cur.getColumnIndex("nominee_surveyed_status")));*/


                    String hhStatus = cur.getString(cur.getColumnIndex("hh_status"));
                    String memStatus = cur.getString(cur.getColumnIndex("mem_status"));
                    item.setLockedSave(cur.getString(cur.getColumnIndex("locked_save")));
                    item.setAadhaarStatus(cur.getString(cur.getColumnIndex("aadhaar_status")));
                    String syncStatus = cur.getString(cur.getColumnIndex("synced_status"));
                    if (memStatus != null) {
                        item.setMemStatus(memStatus.trim());
                    }
                    if (hhStatus != null && !hhStatus.equalsIgnoreCase("")) {
                        item.setHhStatus(hhStatus.trim());
                    }
                    if (syncStatus != null && !syncStatus.equalsIgnoreCase("")) {
                        item.setSyncedStatus(syncStatus.trim());
                    }
                    // item.setSyncDt(cur.getString(cur.getColumnIndex("sync_dt")));
                    item.setError_type(cur.getString(cur.getColumnIndex("error_type")));
                    item.setError_code(cur.getString(cur.getColumnIndex("error_msg")));
                    item.setError_msg(cur.getString(cur.getColumnIndex("error_msg")));
                    item.setAppVersion(cur.getString(cur.getColumnIndex("app_version")));
                    item.setVl_aadharNo(cur.getString(cur.getColumnIndex("vl_aadharNo")));
                    item.setVl_blockCode(cur.getString(cur.getColumnIndex("vl_blockCode")));
                    item.setVl_districtCode(cur.getString(cur.getColumnIndex("vl_districtCode")));
                    item.setVl_ruralUrban(cur.getString(cur.getColumnIndex("vl_ruralUrban")));
                    item.setVl_stateCode(cur.getString(cur.getColumnIndex("vl_stateCode")));
                    item.setVl_subBlockcode(cur.getString(cur.getColumnIndex("vl_subBlockcode")));
                    item.setVl_tehsilCode(cur.getString(cur.getColumnIndex("vl_tehsilCode")));
                    item.setVl_vtCode(cur.getString(cur.getColumnIndex("vl_vtCode")));
                    item.setVl_wardCode(cur.getString(cur.getColumnIndex("vl_wardCode")));
                    item.setCsmNo(cur.getString(cur.getColumnIndex("csmNo")));
                    item.setCardCategory(cur.getString(cur.getColumnIndex("cardCategory")));
                    item.setCardType(cur.getString(cur.getColumnIndex("cardType")));

                    cur.moveToNext();
                }
            }
            cur.close();

        }
        mDatabase.close();
        helpers.close();
        return item;
    }

    public static RsbyHouseholdItem getRsbyHouseHold(Context context, String query) {
        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        SQLiteDatabase mDatabase = helpers.createOrOpenDatabase();
        RsbyHouseholdItem item = null;

        Cursor cur = mDatabase.rawQuery(query, null);
        // Log.d("SECC Data ", " Query : " + cur.getCount());
        if (cur != null) {
            if (cur.getCount() > 0) {
                cur.moveToFirst();
                while (cur.isAfterLast() == false) {
                    item = new RsbyHouseholdItem();

                    item.setInsccode(cur.getString(cur.getColumnIndex("insccode")));
                    item.setPolicyno(cur.getString(cur.getColumnIndex("policyno")));
                    item.setPolicyamt(cur.getString(cur.getColumnIndex("policyamt")));
                    item.setStartdate(cur.getString(cur.getColumnIndex("startdate")));
                    item.setEnddate(cur.getString(cur.getColumnIndex("enddate")));
                    item.setNhpsId(cur.getString(cur.getColumnIndex("nhps_id")));
                    item.setLockedSave(cur.getString(cur.getColumnIndex("locked_save")));
                    item.setHofImage(cur.getString(cur.getColumnIndex("rsby_familyphoto")));
                    item.setRsbyMemId(cur.getString(cur.getColumnIndex("rsbyMemId")));
                    item.setUrnId(cur.getString(cur.getColumnIndex("urnId")));
                    item.setIssuesTimespam(cur.getString(cur.getColumnIndex("issuesTimespam")));
                    item.setHofnamereg(cur.getString(cur.getColumnIndex("hofnamereg")));
                    item.setDob(cur.getString(cur.getColumnIndex("dob")));
                    item.setVillageCode(cur.getString(cur.getColumnIndex("villageCode")));
                    item.setDoorhouse(cur.getString(cur.getColumnIndex("doorhouse")));
                    item.setPanchyatTownCode(cur.getString(cur.getColumnIndex("panchyatTownCode")));
                    item.setBlockCode(cur.getString(cur.getColumnIndex("blockCode")));
                    item.setDistrictCode(cur.getString(cur.getColumnIndex("districtCode")));
                    item.setStateCode(cur.getString(cur.getColumnIndex("stateCode")));
                    item.setMemid(cur.getString(cur.getColumnIndex("memid")));
                    item.setName(cur.getString(cur.getColumnIndex("name")));
                    item.setGender(cur.getString(cur.getColumnIndex("gender")));
                    item.setRelcode(cur.getString(cur.getColumnIndex("relcode")));
                    String hhStatus = cur.getString(cur.getColumnIndex("hh_status"));
                    String syncStatus = cur.getString(cur.getColumnIndex("synced_status"));
                    if (hhStatus != null && !hhStatus.equalsIgnoreCase("")) {
                        item.setHhStatus(hhStatus.trim());
                    }
                    if (syncStatus != null && !syncStatus.equalsIgnoreCase("")) {
                        item.setSyncedStatus(syncStatus.trim());
                    }
                    item.setSyncDt(cur.getString(cur.getColumnIndex("sync_dt")));
                    item.setError_type(cur.getString(cur.getColumnIndex("error_type")));
                    item.setError_code(cur.getString(cur.getColumnIndex("error_msg")));
                    item.setError_msg(cur.getString(cur.getColumnIndex("error_msg")));
                    item.setAppVersion(cur.getString(cur.getColumnIndex("app_version")));

                    item.setVlAadharNo(cur.getString(cur.getColumnIndex("vl_aadharNo")));
                    item.setVlBlockCode(cur.getString(cur.getColumnIndex("vl_blockCode")));
                    item.setVlDistrictCode(cur.getString(cur.getColumnIndex("vl_districtCode")));
                    item.setVlRuralUrban(cur.getString(cur.getColumnIndex("vl_ruralUrban")));
                    item.setVlStateCode(cur.getString(cur.getColumnIndex("vl_stateCode")));
                    item.setVlSubBlockcode(cur.getString(cur.getColumnIndex("vl_subBlockcode")));
                    item.setVlTehsilCode(cur.getString(cur.getColumnIndex("vl_tehsilCode")));
                    item.setVlVtCode(cur.getString(cur.getColumnIndex("vl_vtCode")));
                    item.setVlWardCode(cur.getString(cur.getColumnIndex("vl_wardCode")));
                    item.setCsmNo(cur.getString(cur.getColumnIndex("csmNo")));
                    item.setCardCategory(cur.getString(cur.getColumnIndex("cardCategory")));
                    item.setCardType(cur.getString(cur.getColumnIndex("cardType")));

                    cur.moveToNext();
                }
            }
            cur.close();

        }
        mDatabase.close();
        helpers.close();
        return item;
    }

    public static ArrayList<RsbyHouseholdItem> getAllRsbyHouseHold(Context context, String query) {
        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        SQLiteDatabase mDatabase = helpers.createOrOpenDatabase();
        ArrayList<RsbyHouseholdItem> houseHoldItems = new ArrayList<>();
        RsbyHouseholdItem item = null;

        Cursor cur = mDatabase.rawQuery(query, null);
        // Log.d("SECC Data ", " Query : " + cur.getCount());
        if (cur != null) {
            if (cur.getCount() > 0) {
                cur.moveToFirst();
                while (cur.isAfterLast() == false) {
                    item = new RsbyHouseholdItem();
                    item.setInsccode(cur.getString(cur.getColumnIndex("insccode")));
                    item.setPolicyno(cur.getString(cur.getColumnIndex("policyno")));
                    item.setPolicyamt(cur.getString(cur.getColumnIndex("policyamt")));
                    item.setStartdate(cur.getString(cur.getColumnIndex("startdate")));
                    item.setEnddate(cur.getString(cur.getColumnIndex("enddate")));
                    item.setNhpsId(cur.getString(cur.getColumnIndex("nhps_id")));
                    item.setLockedSave(cur.getString(cur.getColumnIndex("locked_save")));
                    item.setHofImage(cur.getString(cur.getColumnIndex("rsby_familyphoto")));
                    item.setRsbyMemId(cur.getString(cur.getColumnIndex("rsbyMemId")));
                    item.setUrnId(cur.getString(cur.getColumnIndex("urnId")));
                    item.setIssuesTimespam(cur.getString(cur.getColumnIndex("issuesTimespam")));
                    item.setHofnamereg(cur.getString(cur.getColumnIndex("hofnamereg")));
                    item.setDob(cur.getString(cur.getColumnIndex("dob")));
                    item.setVillageCode(cur.getString(cur.getColumnIndex("villageCode")));
                    item.setDoorhouse(cur.getString(cur.getColumnIndex("doorhouse")));
                    item.setPanchyatTownCode(cur.getString(cur.getColumnIndex("panchyatTownCode")));
                    item.setBlockCode(cur.getString(cur.getColumnIndex("blockCode")));
                    item.setDistrictCode(cur.getString(cur.getColumnIndex("districtCode")));
                    item.setStateCode(cur.getString(cur.getColumnIndex("stateCode")));
                    item.setMemid(cur.getString(cur.getColumnIndex("memid")));
                    item.setName(cur.getString(cur.getColumnIndex("name")));
                    item.setGender(cur.getString(cur.getColumnIndex("gender")));
                    item.setRelcode(cur.getString(cur.getColumnIndex("relcode")));
                    String hhStatus = cur.getString(cur.getColumnIndex("hh_status"));
                    String syncStatus = cur.getString(cur.getColumnIndex("synced_status"));
                    if (hhStatus != null && !hhStatus.equalsIgnoreCase("")) {
                        item.setHhStatus(hhStatus.trim());
                    }
                    if (syncStatus != null && !syncStatus.equalsIgnoreCase("")) {
                        item.setSyncedStatus(syncStatus.trim());
                    }
                    item.setSyncDt(cur.getString(cur.getColumnIndex("sync_dt")));
                    item.setError_type(cur.getString(cur.getColumnIndex("error_type")));
                    item.setError_code(cur.getString(cur.getColumnIndex("error_msg")));
                    item.setError_msg(cur.getString(cur.getColumnIndex("error_msg")));
                    item.setAppVersion(cur.getString(cur.getColumnIndex("app_version")));
                    item.setVlAadharNo(cur.getString(cur.getColumnIndex("vl_aadharNo")));
                    item.setVlBlockCode(cur.getString(cur.getColumnIndex("vl_blockCode")));
                    item.setVlDistrictCode(cur.getString(cur.getColumnIndex("vl_districtCode")));
                    item.setVlRuralUrban(cur.getString(cur.getColumnIndex("vl_ruralUrban")));
                    item.setVlStateCode(cur.getString(cur.getColumnIndex("vl_stateCode")));
                    AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Sub block : " + cur.getString(cur.getColumnIndex("vl_subBlockcode")));
                    item.setVlSubBlockcode(cur.getString(cur.getColumnIndex("vl_subBlockcode")));
                    AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Sub block tehsil code: " + cur.getString(cur.getColumnIndex("vl_tehsilCode")));
                    item.setVlTehsilCode(cur.getString(cur.getColumnIndex("vl_tehsilCode")));
                    item.setVlVtCode(cur.getString(cur.getColumnIndex("vl_vtCode")));
                    AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Sub block ward code code: " + cur.getString(cur.getColumnIndex("vl_wardCode")));

                    item.setVlWardCode(cur.getString(cur.getColumnIndex("vl_wardCode")));
                    item.setCsmNo(cur.getString(cur.getColumnIndex("csmNo")));
                    item.setCardCategory(cur.getString(cur.getColumnIndex("cardCategory")));
                    item.setCardType(cur.getString(cur.getColumnIndex("cardType")));

                    houseHoldItems.add(item);
                    cur.moveToNext();
                }
            }
            cur.close();

        }
        mDatabase.close();
        helpers.close();
        return houseHoldItems;
    }


    public static ArrayList<RsbyCardCategoryItem> getAllCardCategory(Context context, String query) {
        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        SQLiteDatabase mDatabase = helpers.createOrOpenDatabase();
        ArrayList<RsbyCardCategoryItem> categoryList = new ArrayList<>();
        RsbyCardCategoryItem item = null;

        Cursor cur = mDatabase.rawQuery(query, null);
        // Log.d("SECC Data ", " Query : " + cur.getCount());
        if (cur != null) {
            if (cur.getCount() > 0) {
                cur.moveToFirst();
                while (cur.isAfterLast() == false) {
                    item = new RsbyCardCategoryItem();
                    item.setSno(cur.getString(cur.getColumnIndex("sno")));
                    item.setCatCode(cur.getString(cur.getColumnIndex("cat_code")));
                    item.setCategoryCode(cur.getString(cur.getColumnIndex("category_code")));
                    item.setCatName(cur.getString(cur.getColumnIndex("cat_name")));
                    categoryList.add(item);
                    cur.moveToNext();
                }
            }
            cur.close();

        }
        mDatabase.close();
        helpers.close();
        return categoryList;
    }


    public static long saveRsbyCardCategory(RsbyCardCategoryItem item, Context context) {
        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        ContentValues values = new ContentValues();
        values.put("cat_code", item.getCatCode());
        values.put("category_code", item.getCategoryCode());
        values.put("cat_name", item.getCatName());
        SQLiteDatabase db = helpers.getWritableDatabase();
        long insertFlag = db.insert(AppConstant.RSBY_CARD_CAT_MASTER_TABLE, null, values);
        db.close();
        helpers.close();
        return insertFlag;
    }

    public static ArrayList<RsbyPoliciesCompany> getInsuranceCompaniesName(Context context, String query) {
        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        SQLiteDatabase mDatabase = helpers.createOrOpenDatabase();
        ArrayList<RsbyPoliciesCompany> categoryList = new ArrayList<>();
        RsbyPoliciesCompany item = null;

        Cursor cur = mDatabase.rawQuery(query, null);
        // Log.d("SECC Data ", " Query : " + cur.getCount());
        if (cur != null) {
            if (cur.getCount() > 0) {
                cur.moveToFirst();
                while (cur.isAfterLast() == false) {
                    item = new RsbyPoliciesCompany();
                    item.setCompanyCode(cur.getString(cur.getColumnIndex("InsCode")));
                    item.setCompanyName(cur.getString(cur.getColumnIndex("Ins_Company")));
                    categoryList.add(item);
                    cur.moveToNext();
                }
            }
            cur.close();

        }
        mDatabase.close();
        helpers.close();
        return categoryList;
    }


    public static long saveRsbyPoliciesCompany(RsbyPoliciesCompany item, Context context) {
        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        ContentValues values = new ContentValues();
        values.put("InsCode", item.getCompanyCode());
        values.put("Ins_Company", item.getCompanyName());
        SQLiteDatabase db = helpers.getWritableDatabase();
        long insertFlag = db.insert(AppConstant.RSBY_POLICY_COMPANY, null, values);
        db.close();
        helpers.close();
        return insertFlag;
    }


    public static ArrayList<RsbyRelationItem> getRsbyMemberRelationCode(Context context, String query) {
        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        SQLiteDatabase mDatabase = helpers.createOrOpenDatabase();
        ArrayList<RsbyRelationItem> categoryList = new ArrayList<>();
        RsbyRelationItem item = null;

        Cursor cur = mDatabase.rawQuery(query, null);
        // Log.d("SECC Data ", " Query : " + cur.getCount());
        if (cur != null) {
            if (cur.getCount() > 0) {
                cur.moveToFirst();
                while (cur.isAfterLast() == false) {
                    item = new RsbyRelationItem();
                    item.setRelCode(cur.getString(cur.getColumnIndex("rel_code")));
                    item.setRelName(cur.getString(cur.getColumnIndex("rel_name")));
                    categoryList.add(item);
                    cur.moveToNext();
                }
            }
            cur.close();

        }
        mDatabase.close();
        helpers.close();
        return categoryList;
    }

    public static long saveRsbyRelation(RsbyRelationItem item, Context context) {
        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        ContentValues values = new ContentValues();
        values.put("rel_code", item.getRelCode());
        values.put("rel_name", item.getRelName());
        SQLiteDatabase db = helpers.getWritableDatabase();
        long insertFlag = db.insert(AppConstant.RSBY_RELATION_TABLE, null, values);
        db.close();
        helpers.close();
        return insertFlag;
    }

    public static RSBYPoliciesItem getRSBYPolicies(Context context, String query) {
        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        SQLiteDatabase mDatabase = helpers.createOrOpenDatabase();
        RSBYPoliciesItem item = null;

        Cursor cur = mDatabase.rawQuery(query, null);
        // Log.d("SECC Data ", " Query : " + cur.getCount());
        if (cur != null) {
            if (cur.getCount() > 0) {
                cur.moveToFirst();
                while (cur.isAfterLast() == false) {
                    item = new RSBYPoliciesItem();
                    item.setPolicyNo(cur.getString(cur.getColumnIndex("policyNo")));
                    item.setStateCode(cur.getString(cur.getColumnIndex("StateCode")));
                    item.setDistrictCode(cur.getString(cur.getColumnIndex("DistrictCode")));
                    item.setExtensionEndDate(cur.getString(cur.getColumnIndex("ExtensionEndDate")));
                    item.setExtensionType(cur.getString(cur.getColumnIndex("ExtensionType")));
                    item.setInsuranceCompanyCode(cur.getString(cur.getColumnIndex("InsuranceCompanyCode")));

                    cur.moveToNext();
                }
            }
            cur.close();

        }
        mDatabase.close();
        helpers.close();
        return item;
    }

    public static ArrayList<RSBYPoliciesItem> getRSBYPoliciesList(Context context, String query) {
        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        SQLiteDatabase mDatabase = helpers.createOrOpenDatabase();
        RSBYPoliciesItem item = null;
        ArrayList<RSBYPoliciesItem> rsbyPoliciesList = new ArrayList<>();

        Cursor cur = mDatabase.rawQuery(query, null);
        // Log.d("SECC Data ", " Query : " + cur.getCount());
        if (cur != null) {
            if (cur.getCount() > 0) {
                cur.moveToFirst();
                while (cur.isAfterLast() == false) {
                    item = new RSBYPoliciesItem();
                    item.setPolicyNo(cur.getString(cur.getColumnIndex("policyNo")));
                    item.setStateCode(cur.getString(cur.getColumnIndex("StateCode")));
                    item.setDistrictCode(cur.getString(cur.getColumnIndex("DistrictCode")));
                    item.setExtensionEndDate(cur.getString(cur.getColumnIndex("ExtensionEndDate")));
                    item.setExtensionType(cur.getString(cur.getColumnIndex("ExtensionType")));
                    item.setInsuranceCompanyCode(cur.getString(cur.getColumnIndex("InsuranceCompanyCode")));
                    rsbyPoliciesList.add(item);
                    cur.moveToNext();
                }
            }
            cur.close();

        }
        mDatabase.close();
        helpers.close();
        return rsbyPoliciesList;
    }

    public static long saveRsbyPoliceItem(RSBYPoliciesItem item, Context context) {
        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        ContentValues values = new ContentValues();
        values.put("InsuranceCompanyCode", item.getInsuranceCompanyCode());
        values.put("ExtensionType", item.getExtensionType());
        values.put("ExtensionEndDate", item.getExtensionEndDate());
        values.put("DistrictCode", item.getDistrictCode());
        values.put("StateCode", item.getStateCode());
        values.put("policyNo", item.getPolicyNo());


        SQLiteDatabase db = helpers.getWritableDatabase();
        long insertFlag = db.insert(AppConstant.RSBY_POLICIES_TABLE, null, values);
        db.close();
        helpers.close();
        return insertFlag;
    }


    public static long updateRsbyMember(SeccMemberItem item, Context context) {
        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        ContentValues values = new ContentValues();
        values.put("member_active_status", item.getMember_active_status());
        values.put("printStatus", item.getPrintStatus());
        values.put("nhps_mem_id", item.getNhpsMemId());
        values.put("tin_npr", item.getTinNpr());
        values.put("statecode", item.getStatecode());
        values.put("districtcode", item.getDistrictcode());
        values.put("tehsilcode", item.getTehsilcode());
        values.put("towncode", item.getTowncode());
        values.put("wardid", item.getWardid());
        values.put("blockno", item.getAhlblockno());
        values.put("grampanchayatcode", item.getGrampanchayatcode());
        values.put("grampanchayatname", item.getGrampanchayatname());
        values.put("ahlblockno", item.getAhlblockno());
        values.put("ahlsubblockno", item.getAhlsubblockno());
        values.put("slnohhd", item.getSlnohhd());
        values.put("slnomember", item.getSlnomember());
        values.put("ahlslnohhd", item.getAhlslnohhd());
        values.put("ahltypeofeb", item.getAhltypeofeb());
        values.put("typeofhhd", item.getTypeofhhd());
        values.put("livinginshelter", item.getLivinginshelter());
        values.put("name", item.getName());
        values.put("name_sl", item.getNameSl());
        values.put("relation", item.getRelation());
        values.put("relation_sl", item.getRelationSl());
        values.put("genderid", item.getGenderid());
        values.put("dob", item.getDob());
        values.put("mstatusid", item.getMstatusid());
        values.put("fathername", item.getFathername());
        values.put("fathername_sl", item.getFathernameSl());
        values.put("mothername", item.getMothername());
        values.put("mothername_sl", item.getMothernameSl());
        values.put("occupation", item.getOccupation());
        values.put("occupation_sl", item.getOccupationSl());
        values.put("educode", item.getEducode());
        values.put("education_other", item.getEducationOther());
        values.put("disabilitycode", item.getDisabilitycode());
        values.put("caste_group", item.getCasteGroup());
        values.put("ahl_tin", item.getAhlTin());
        // values.put("member_status", item.getMemberStatus());
        values.put("addressline1", item.getAddressline1());
        values.put("addressline2", item.getAddressline2());
        values.put("addressline3", item.getAddressline3());
        values.put("addressline4", item.getAddressline4());
        values.put("addressline5", item.getAddressline5());
        values.put("pincode", item.getPincode());
        values.put("addressline1sl", item.getAddressline1Sl());
        values.put("addressline2sl", item.getAddressline2Sl());
        values.put("addressline3sl", item.getAddressline3Sl());
        values.put("addressline4sl", item.getAddressline4Sl());
        values.put("addressline5sl", item.getAddressline5Sl());
        values.put("dob_frm_npr", item.getDobFrmNpr());
        values.put("spousenm", item.getSpousenm());
        values.put("hhd_no", item.getHhdNo());
        if (item.getAadhaarNo() != null) {
            values.put("aadhaar_no", item.getAadhaarNo().trim());
        }
        values.put("name_aadhaar", item.getNameAadhaar());
        values.put("urn_no", item.getUrnNo());
        values.put("mobile_no", item.getMobileNo());
        values.put("rural_urban", item.getRuralUrban());
        values.put("bank_ifsc", item.getNhpsRelationCode());
        values.put("mark_for_deletion", item.getMarkForDeletion());
        values.put("aadhaar_status", item.getAadhaarStatus());
        values.put("hh_status", item.getHhStatus());
        values.put("mem_status", item.getMemStatus());
        values.put("eid", item.getEid());
        values.put("bank_acc_no", item.getNhpsRelationName());
        values.put("scheme_id", item.getSchemeId());
        values.put("scheme_no", item.getSchemeNo());
        values.put("name_nominee", item.getNameNominee());
        values.put("relation_nominee_code", item.getRelationNomineeCode());
        values.put("nominee_relation_name", item.getNomineeRelationName());
        values.put("id_type", item.getIdType());
        values.put("id_no", item.getIdNo());
        values.put("name_as_id", item.getNameAsId());
        values.put("member_photo1", item.getMemberPhoto1());
        Log.d("Secc Data base", "ID Image : " + item.getGovtIdPhoto());
        values.put("govt_id_photo1", item.getGovtIdPhoto());

        // values.put("id_photo1",item.getIdPhoto1()!=null ? item.getIdPhoto1().getBytes() : null);
                /*"values.put("id_photo2 bytea,
                        values.put("member_photo bytea,*/
        values.put("aadhaar_capturing_mode", item.getAadhaarCapturingMode());
        values.put("consent", item.getConsent());
        values.put("aadhaar_auth_mode", item.getAadhaarAuthMode());
        values.put("aadhaar_auth_dt", item.getAadhaarAuthDt());
        values.put("aadhaar_verified_by", item.getAadhaarVerifiedBy());
        values.put("userid", item.getUserid());
        values.put("date_updated", item.getDateUpdated());
        values.put("aadhaar_auth", item.getAadhaarAuth());
        values.put("mobile_auth", item.getMobileAuth());
        values.put("urn_auth", item.getUrnAuth());
        values.put("ifsc_auth", item.getIfscAuth());
        values.put("bank_acc_auth", item.getBankAccAuth());
        values.put("state_scheme_code_auth", item.getStateSchemeCodeAuth());
        values.put("scheme_code", item.getSchemeCode());
        values.put("data_source", item.getDataSource());
        values.put("nhps_id", item.getNhpsId());
        values.put("mobile_auth_dt", item.getMobileAuthDt());
        values.put("urn_auth_dt", item.getUrnAuthDt());
        values.put("ifsc_auth_dt", item.getIfscAuthDt());
        values.put("bank_acc_auth_dt", item.getBankAccAuthDt());
        values.put("state_scheme_code_auth_dt", item.getStateSchemeCodeAuthDt());
        values.put("consent_dt", item.getConsentDt());
        values.put("aadhaar_gender", item.getAadhaarGender());
        values.put("aadhaar_yob", item.getAadhaarYob());
        values.put("aadhaar_dob", item.getAadhaarDob());
        values.put("aadhaar_co", item.getAadhaarCo());
        values.put("aadhaar_gname", item.getAadhaarGname());
        values.put("aadhaar_house", item.getAadhaarHouse());
        values.put("aadhaar_street", item.getAadhaarStreet());
        values.put("aadhaar_loc", item.getAadhaarLoc());
        values.put("aadhaar_vtc", item.getAadhaarVtc());
        values.put("aadhaar_po", item.getAadhaarPo());
        values.put("aadhaar_dist", item.getAadhaarDist());
        values.put("aadhaar_subdist", item.getAadhaarSubdist());
        values.put("aadhaar_state", item.getAadhaarState());
        values.put("aadhaar_pc", item.getAadhaarPc());
        values.put("aadhaar_lm", item.getNhpsRelationName());
        values.put("latitude", item.getLatitude());
        values.put("longitude", item.getLongitude());
        values.put("whose_mobile", item.getWhoseMobile());
        values.put("nhps_relation_code", item.getNhpsRelationCode());
        values.put("nhps_relation_name", item.getNhpsRelationName());
        values.put("synced_status", item.getSyncedStatus());//
        values.put("error_code", item.getError_code());
        values.put("error_msg", item.getError_msg());
        values.put("error_type", item.getError_type());
        values.put("app_version", item.getAppVersion());
        values.put("locked_save", item.getLockedSave());
        if (item.getSyncDt() != null) {
            values.put("sync_dt", item.getSyncDt().trim());
        }
        AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Sync Date and time" + item.getSyncDt());
        values.put("rsby_rsbyMemId", item.getRsbyMemId());

        if (item.getRsbyUrnId() != null) {

            values.put("rsby_urnId", item.getRsbyUrnId());
        } else {
            values.put("rsby_urnId", item.getUrnId());
        }
        values.put("rsby_insccode", item.getRsbyInsccode());
        values.put("rsby_policyno", item.getRsbyPolicyno());
        values.put("rsby_policyamt", item.getRsbyPolicyamt());
        values.put("rsby_startdate", item.getRsbyStartdate());
        values.put("rsby_enddate", item.getRsbyEnddate());
        values.put("rsby_issuesTimespam", item.getRsbyIssuesTimespam());
        values.put("rsby_hofnamereg", item.getRsbyHofnamereg());
        values.put("rsby_doorhouse", item.getRsbyDoorhouse());
        values.put("rsby_villageCode", item.getRsbyVillageCode());
        values.put("rsby_panchyatTownCode", item.getRsbyPanchyatTownCode());
        values.put("rsby_blockCode", item.getRsbyBlockCode());
        values.put("rsby_districtCode", item.getRsbyDistrictCode());
        String str = item.getRsbyStateCode();
        System.out.print(str);
        values.put("rsby_stateCode", item.getRsbyStateCode());
        values.put("rsby_name", item.getRsbyName());
        values.put("rsby_dob", item.getRsbyDob());
        if (item.getRsbyGender() != null) {

            values.put("rsby_gender", item.getRsbyGender());
        } else {

            values.put("rsby_gender", item.getGenderid());
        }
        values.put("rsby_relcode", item.getRsbyRelcode());
        values.put("rsby_csmNo", item.getRsbyCsmNo());
        values.put("rsby_cardType", item.getRsbyCardType());
        values.put("rsby_cardCategory", item.getRsbyCardCategory());
        values.put("nominee_guardian_name ", item.getNomineeGaurdianName());

        SQLiteDatabase db = helpers.getWritableDatabase();
        int updateFlag = db.update(AppConstant.TRANSECT_POPULATE_SECC, values,
                "rsby_rsbyMemId='" + item.getRsbyMemId() + "'", null);
        db.close();
        helpers.close();
        return updateFlag;
    }

    public static long updateRsbyHousehold(HouseHoldItem item, Context context) {
        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        ContentValues values = new ContentValues();
        values.put("nhps_mem_id", item.getNhpsMemId());
        values.put("ahl_tin", item.getAhlTin());
        values.put("hhd_no", item.getHhdNo());
        values.put("slnomember", item.getSlnomember());
        values.put("slnohhd", item.getSlnohhd());
        values.put("ahlslnohhd", item.getAhlslnohhd());
        values.put("name", item.getName());
        values.put("name_sl", item.getNameSl());
        values.put("fathername", item.getFathername());
        values.put("fathername_sl", item.getFathernameSl());
        values.put("relation", item.getRelation());
        values.put("relation_sl", item.getRelationSl());
        values.put("genderid", item.getGenderid());
        values.put("dob", item.getDob());
        values.put("mstatusid", item.getMstatusid());
        values.put("mothername", item.getMothername());
        values.put("mothername_sl", item.getMothernameSl());
        values.put("occupation", item.getOccupation());
        values.put("occupation_sl", item.getOccupationSl());
        values.put("caste_group", item.getCasteGroup());
        values.put("statecode", item.getStatecode());
        values.put("districtcode", item.getDistrictcode());
        values.put("tehsilcode", item.getTehsilcode());
        values.put("towncode", item.getTowncode());
        values.put("wardid", item.getWardid());
        values.put("ahlblockno", item.getAhlblockno());
        values.put("typeofhhd", item.getTypeofhhd());
        values.put("hhd_landownedcodes", item.getHhdLandownedcodes());
        values.put("totalirrigated", item.getTotalirrigated());
        values.put("hhd_irri_equip", item.getHhdIrriEquip());
        values.put("totalunirrigated", item.getTotalunirrigated());
        values.put("otherirrigated", item.getOtherirrigated());
        values.put("hhd_asset_mveh", item.getHhdAssetMveh());
        values.put("hhd_magri_equip", item.getHhdMagriEquip());
        values.put("hhd_kcc", item.getHhdKcc());
        values.put("hhd_emp_sector", item.getHhdEmpSector());
        values.put("hhd_emp_erg", item.getHhdEmpErg());
        values.put("hhd_emp_hem", item.getHhdEmpHem());
        values.put("hhd_emp_pit", item.getHhdEmpPit());
        values.put("housing_walltype", item.getHousingWalltype());
        values.put("housing_rooftype", item.getHousingRooftype());
        values.put("housing_no_of_rooms", item.getHousingNoOfRooms());
        values.put("hhd_asset_ref", item.getHhdAssetRef());
        values.put("hhd_asset_telmob", item.getHhdAssetTelmob());
        values.put("livinginshelter", item.getLivinginshelter());
        values.put("hhd_emp_other", item.getHhdEmpOther());
        values.put("hhd_ms", item.getHhdMs());
        values.put("hhd_ptg", item.getHhdPtg());
        values.put("hhd_lrbl", item.getHhdLrbl());
        //  values.put("inclusion integer",item.getI);
        values.put("rural_urban", item.getRuralUrban());
     /*   values.put("state_code", item.getStateCode());
        values.put("district_code", item.getDistrictCode());
        values.put("tehsil_code", item.getTehsilCode());
        values.put("vt_code", item.getVtCode());
        values.put("ward_code", item.getWardCode());*/
        values.put("mdds_stc", item.getMddsStc());
        values.put("mdds_dtc", item.getMddsDtc());
        values.put("mdds_sdtc", item.getMddsDtc());
        values.put("mdds_plcn", item.getMddsPlcn());
        // values.put("ahlsubblockno",item.getAh)
        //  Log.d("Secc Database","Household Status :"+item.getHhStatus());
        values.put("addressline1", item.getAddressline1());
        values.put("addressline2", item.getAddressline2());
        values.put("addressline3", item.getAddressline3());
        values.put("addressline4", item.getAddressline4());
        values.put("addressline5", item.getAddressline5());
        values.put("pincode", item.getPincode());
        AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Household sync error1 " + item.getError_code());

        values.put("error_code", item.getError_code());
        values.put("error_msg", item.getError_msg());
        values.put("error_type", item.getError_type());
        values.put("app_version", item.getAppVersion());

        if (item.getSyncDt() != null) {
            values.put("sync_dt", item.getSyncDt().trim());
        }

        values.put("nhps_id", item.getNhpsId());
        /*values.put("hh_status",item.getHhStatus());
        values.put("locked_save",item.getLockSave());
        values.put("synced_status",item.getSyncedStatus());*/

        if (item.getHhStatus() != null && !item.getHhStatus().equalsIgnoreCase("")) {
            values.put("hh_status", item.getHhStatus().trim());
        } else {
            values.put("hh_status", item.getHhStatus());
        }
        if (item.getSyncedStatus() != null && !item.getSyncedStatus().equalsIgnoreCase("")) {
            values.put("synced_status", item.getSyncedStatus().trim());
        } else {
            values.put("synced_status", item.getSyncedStatus());
        }
        values.put("nhps_id", item.getNhpsId());
        if (item.getLockSave() != null && !item.getLockSave().equalsIgnoreCase("")) {
            values.put("locked_save", item.getLockSave().trim());
        } else {
            values.put("locked_save", item.getLockSave());
        }

        values.put("rsby_rsbyMemId", item.getRsbyMemId());
        if (item.getRsbyUrnId() != null) {
            values.put("rsby_urnId", item.getRsbyUrnId());


        } else {

            values.put("rsby_urnId", item.getUrnId());

        }
        values.put("rsby_insccode", item.getRsbyInsccode());
        values.put("rsby_policyno", item.getRsbyPolicyno());

        values.put("rsby_policyamt", item.getRsbyPolicyamt());

        values.put("rsby_startdate", item.getRsbyStartdate());
        values.put("rsby_enddate", item.getRsbyEnddate());
        values.put("rsby_issuesTimespam", item.getRsbyIssuesTimespam());

        values.put("rsby_hofnamereg", item.getRsbyHofnamereg());
        values.put("rsby_doorhouse", item.getRsbyDoorhouse());
        values.put("rsby_villageCode", item.getRsbyVillageCode());
        values.put("rsby_panchyatTownCode", item.getRsbyPanchyatTownCode());

        values.put("rsby_blockCode", item.getRsbyBlockCode());
        values.put("rsby_districtCode", item.getRsbyDistrictCode());
        values.put("rsby_stateCode", item.getRsbyStateCode());

        values.put("rsby_name", item.getRsbyName());
        values.put("rsby_dob", item.getRsbyDob());
        if (item.getRsbyGender() != null) {

            values.put("rsby_gender", item.getRsbyGender());
        } else {

            values.put("rsby_gender", item.getGenderid());
        }
        values.put("rsby_relcode", item.getRsbyRelcode());

        values.put("rsby_csmNo", item.getRsbyCsmNo());
        values.put("rsby_cardType", item.getRsbyCardType());
        values.put("rsby_cardCategory", item.getRsbyCardCategory());
        values.put("data_source", item.getDataSource());
        // Log.d("Secc Database","Ahl Tin : "+item.getAhlTin()+" "+item.getNhpsRelationName());
        SQLiteDatabase db = helpers.getWritableDatabase();
        int updateFlag = db.update(AppConstant.HOUSE_HOLD_SECC, values,
                "rsby_rsbyMemId='" + item.getRsbyMemId() + "'", null);
        // Log.d("ID",item.getId()+"FATHER"+item.getFathernmNpr());
        // Log.d("Secc Database", " Update household Value : " + updateFlag);
        db.close();
        helpers.close();
        return updateFlag;
    }


    public static ArrayList<NotificationModel> getAllNotification(Context context, String query) {
        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        SQLiteDatabase mDatabase = helpers.createOrOpenDatabase();
        ArrayList<NotificationModel> notificationItem = new ArrayList<>();
        NotificationModel item = null;
        Cursor cur = mDatabase.rawQuery(query, null);
        if (cur != null) {
            if (cur.getCount() > 0) {
                cur.moveToFirst();
                while (cur.isAfterLast() == false) {
                    item = new NotificationModel();
                    item.setActiveStatus(cur.getString(cur.getColumnIndex("active_status")));
                    // item.setId(cur.getString(cur.getColumnIndex("id")));
                    // item.setAadhaarNo(cur.getString(cur.getColumnIndex("aadhaar_no")));
                    item.setCreatedDate(cur.getString(cur.getColumnIndex("created_date")));
                    item.setDateExpire(cur.getString(cur.getColumnIndex("date_expire")));
                    item.setStartDate(cur.getString(cur.getColumnIndex("start_date")));
                    item.setDescription(cur.getString(cur.getColumnIndex("description")));
                    // item.setIpAddress(cur.getString(cur.getColumnIndex("ip_address")));
                    item.setSource(cur.getString(cur.getColumnIndex("source")));
                    item.setTargetState(cur.getString(cur.getColumnIndex("target_state")));
                    notificationItem.add(item);
                    cur.moveToNext();
                }
            }
            cur.close();
        }
        mDatabase.close();
        helpers.close();
        return notificationItem;
    }


    public static long saveNotification(NotificationModel item, Context context) {
        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        ContentValues values = new ContentValues();
        //    values.put("id", item.getId());
        values.put("active_status", item.getActiveStatus());
        //  values.put("aadhaar_no", item.getAadhaarNo());
     /*   long expireDate = AppUtility.convertDateIntoTimeMillis(item.getDateExpire().replace(".0", ""));
        long createdDate = AppUtility.convertDateIntoTimeMillis(item.getCreatedDate().replace(".0", ""));
        long startDate = AppUtility.convertDateIntoTimeMillis(item.getStartDate().replace(".0", ""));*/

        values.put("start_date", item.getStartDate());
        values.put("created_date", item.getStartDate());
        values.put("date_expire ", item.getDateExpire());
        values.put("description", item.getDescription());
        //    values.put("ip_address", item.getIpAddress());
        values.put("source", item.getSource());
        values.put("target_state ", item.getTargetState());
        SQLiteDatabase db = helpers.getWritableDatabase();
        long insertFlag = db.insert(AppConstant.NOTIFICATION_TABLE, null, values);
        Log.d("NOTIFICATION ", "ADDED" + insertFlag);
        db.close();
        helpers.close();
        return insertFlag;
    }

    public static long saveStateMasterData(StateItem item, Context context) {
        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        ContentValues values = new ContentValues();
        values.put("state_code", item.getStateCode());
        values.put("state_name", item.getStateName());
        values.put("status", item.getStatus());
        values.put("login_type", item.getLogin_type());


        SQLiteDatabase db = helpers.getWritableDatabase();
        long insertFlag = db.insert(AppConstant.m_state, null, values);
        db.close();
        helpers.close();
        return insertFlag;
    }


    public static ArrayList<StateItem> stateList(String query, Context context) {
        ArrayList<StateItem> stateItemList = new ArrayList<>();
        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        SQLiteDatabase mDatabase = helpers.createOrOpenDatabase();
        Cursor cur = mDatabase.rawQuery(query, null);
        Log.d("NHSMasterDaoImpl", " Query : " + query + " count :" + cur.getCount());
        if (cur != null) {
            if (cur.getCount() > 0) {
                cur.moveToFirst();
                while (cur.isAfterLast() == false) {
                    StateItem item = new StateItem();
                    item.setStateCode(cur.getString(cur.getColumnIndex("state_code")));
                    item.setStateName(cur.getString(cur.getColumnIndex("state_name")));
                    item.setStatus(cur.getString(cur.getColumnIndex("status")));
                    item.setLogin_type(cur.getString(cur.getColumnIndex("login_type")));
                    stateItemList.add(item);
                    cur.moveToNext();
                }
                cur.close();
            }
        }
        return stateItemList;
    }

    public static ArrayList<ConfigurationItem> configurationList(String query, Context context) {
        ArrayList<ConfigurationItem> list = new ArrayList<>();
        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        SQLiteDatabase mDatabase = helpers.createOrOpenDatabase();
        Cursor cur = mDatabase.rawQuery(query, null);
        Log.d("NHSMasterDaoImpl", " Query : " + query + " count :" + cur.getCount());
        if (cur != null) {
            if (cur.getCount() > 0) {
                cur.moveToFirst();
               /* config_id INTEGER PRIMARY KEY AUTOINCREMENT,
                        state_code INTEGER,
                        description character varying(50),
                        status character varying(5)*/
                while (cur.isAfterLast() == false) {
                    ConfigurationItem item = new ConfigurationItem();
                    item.setStateCode(cur.getString(cur.getColumnIndex("state_code")));
                    item.setConfigId(cur.getString(cur.getColumnIndex("config_id")));
                    item.setDescription(cur.getString(cur.getColumnIndex("description")));
                    item.setStatus(cur.getString(cur.getColumnIndex("status")));
                    item.setAcceptedvalueName(cur.getString(cur.getColumnIndex("acceptedvalueName")));
                    item.setStateName(cur.getString(cur.getColumnIndex("state_name")));
                    list.add(item);
                    cur.moveToNext();
                }
                cur.close();

            }
        }
        return list;
    }

    public static long updateAppConfig(ConfigurationItem item, Context context) {
        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        ContentValues values = new ContentValues();
        values.put("state_code", item.getStateCode());
        values.put("config_id", item.getConfigId());
        values.put("description", item.getDescription());
        values.put("status", item.getStatus());
        values.put("acceptedvalueName", item.getAcceptedvalueName());
        values.put("state_name", item.getStateName());

        SQLiteDatabase db = helpers.getWritableDatabase();
        long insertFlag = db.update(AppConstant.new_application_configuration, values,
                "config_id='" + item.getConfigId() + "' AND state_code+'" + item.getStateCode() + "'", null);
        Log.d("APP_CONFIG ", "UPDATED" + insertFlag);
        db.close();
        helpers.close();
        return insertFlag;
    }

    public static long saveApplicationConfigData(ConfigurationItem item, Context context) {
        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        ContentValues values = new ContentValues();
        values.put("state_code", item.getStateCode());
        values.put("config_id", item.getConfigId());
        values.put("state_name", item.getStateName());
        values.put("description", item.getDescription());
        values.put("status", item.getStatus());
        values.put("acceptedvalueName ", item.getAcceptedvalueName());

        SQLiteDatabase db = helpers.getWritableDatabase();
        long insertFlag = db.insert(AppConstant.new_application_configuration, null, values);
        db.close();
        helpers.close();
        return insertFlag;
    }
    public static long saveFlowLogData(ConfigurationItem item, Context context) {
        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        ContentValues values = new ContentValues();
        values.put("state_code", item.getStateCode());
        values.put("config_id", item.getConfigId());
        values.put("state_name", item.getStateName());
        values.put("description", item.getDescription());
        values.put("status", item.getStatus());
        values.put("acceptedvalueName ", item.getAcceptedvalueName());

        SQLiteDatabase db = helpers.getWritableDatabase();
        long insertFlag = db.insert(AppConstant.new_application_configuration, null, values);
        db.close();
        helpers.close();
        return insertFlag;
    }
    public static long saveApplicationConfigData(ApplicationConfigurationModel item, Context context) {
        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        ContentValues values = new ContentValues();
        values.put("state_code", item.getStateCode());
        values.put("config_id", item.getConfigId());
        values.put("state_name", item.getStateName());
        values.put("description", item.getConfigName());
        values.put("status", item.getAcceptedvalue());
        values.put("acceptedvalueName ", item.getAcceptedvalueName());

        SQLiteDatabase db = helpers.getWritableDatabase();
        long insertFlag = db.insert(AppConstant.new_application_configuration, null, values);
        db.close();
        helpers.close();
        return insertFlag;
    }

    public static ArrayList<SeccMemberItem> getSearchSeccMemberList(Context context, String query) {

        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        SQLiteDatabase mDatabase = helpers.createOrOpenDatabase();
        ArrayList<SeccMemberItem> seccMemberItems = new ArrayList<>();
        Cursor cur = mDatabase.rawQuery(query, null);
        Log.d("SECC Data ", " Query : " + query + " count :" + cur.getCount());
        if (cur != null) {
            if (cur.getCount() > 0) {
                cur.moveToFirst();
                while (cur.isAfterLast() == false) {
                    SeccMemberItem item = new SeccMemberItem();

                    item.setAhlslnohhd(cur.getString(cur.getColumnIndex("ahlslnohhd")));
                    item.setName(cur.getString(cur.getColumnIndex("name")));
                    item.setFathername(cur.getString(cur.getColumnIndex("fathername")));
                    item.setAddressline1(cur.getString(cur.getColumnIndex("addressline1")));
                    item.setAddressline2(cur.getString(cur.getColumnIndex("addressline2")));
                    item.setAddressline3(cur.getString(cur.getColumnIndex("addressline3")));
                    item.setAddressline4(cur.getString(cur.getColumnIndex("addressline4")));
                    item.setAddressline5(cur.getString(cur.getColumnIndex("addressline5")));
                    item.setNameSl(cur.getString(cur.getColumnIndex("name_sl")));
                    item.setRsbyName(cur.getString(cur.getColumnIndex("rsby_name")));
                    item.setRsbyGender(cur.getString(cur.getColumnIndex("rsby_gender")));
                    item.setRsbyMemid(cur.getString(cur.getColumnIndex("rsby_rsbyMemId")));
                    item.setDataSource(cur.getString(cur.getColumnIndex("data_source")));
                    item.setRsbyUrnId(cur.getString(cur.getColumnIndex("rsby_urnId")));

                    item.setHhdNo(cur.getString(cur.getColumnIndex("hhd_no")));
                    item.setHhStatus(cur.getString(cur.getColumnIndex("hh_status")));
                    item.setMemStatus(cur.getString(cur.getColumnIndex("mem_status")));
                    seccMemberItems.add(item);


                    cur.moveToNext();
                }
            }
            cur.close();
        }
        mDatabase.close();
        helpers.close();
        return seccMemberItems;
    }


    public static DownloadedDataCountModel getDataCount(Context context, String query) {

        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        DownloadedDataCountModel item = null;
        SQLiteDatabase mDatabase = helpers.createOrOpenDatabase();
        Cursor cur = mDatabase.rawQuery(query, null);
        Log.d("SECC Data ", " Query : " + query + " count :" + cur.getCount());
        if (cur != null) {
            if (cur.getCount() > 0) {
                cur.moveToFirst();
                while (cur.isAfterLast() == false) {
                    item = new DownloadedDataCountModel();

                    item.setId(cur.getString(cur.getColumnIndex("id")));
                    item.setSeccHouseholdCount(cur.getString(cur.getColumnIndex("secc_household_count")));
                    item.setSeccMemberCount(cur.getString(cur.getColumnIndex("secc_member_count")));
                    item.setRsbyHouseholdCount(cur.getString(cur.getColumnIndex("rsby_household_count")));
                    item.setRsbyMemberCount(cur.getString(cur.getColumnIndex("rsby_member_count")));
                    cur.moveToNext();
                }
            }
            cur.close();
        }
        mDatabase.close();
        helpers.close();
        return item;
    }


    public static long saveDataCount(DownloadedDataCountModel item, Context context) {
        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        ContentValues values = new ContentValues();
        values.put("id", item.getId());
        values.put("secc_household_count", item.getSeccHouseholdCount());
        values.put("secc_member_count", item.getSeccMemberCount());
        values.put("rsby_household_count", item.getRsbyHouseholdCount());
        values.put("rsby_member_count", item.getRsbyMemberCount());

        SQLiteDatabase db = helpers.getWritableDatabase();
        long insertFlag = db.insert(AppConstant.DATA_COUNT_TABLE, null, values);
        db.close();
        helpers.close();
        return insertFlag;
    }

    public static long updateDataCount(DownloadedDataCountModel item, Context context) {
        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        ContentValues values = new ContentValues();
        values.put("id", item.getId());
        values.put("secc_household_count", item.getSeccHouseholdCount());
        values.put("secc_member_count", item.getSeccMemberCount());
        values.put("rsby_household_count", item.getRsbyHouseholdCount());
        values.put("rsby_member_count", item.getRsbyMemberCount());
        SQLiteDatabase db = helpers.getWritableDatabase();
        long insertFlag = db.update(AppConstant.DATA_COUNT_TABLE, values,
                "id='" + item.getId() + "'", null);
        Log.d("APP_CONFIG ", "UPDATED" + insertFlag);
        db.close();
        helpers.close();
        return insertFlag;
    }


    public static ArrayList<ApplicationDataModel> getApplicationData(String query, Context context) {
        ArrayList<ApplicationDataModel> dataList = new ArrayList<ApplicationDataModel>();
        DatabaseHelpers helpers = DatabaseHelpers.getInstance(context);
        SQLiteDatabase mDatabase = helpers.createOrOpenDatabase();
        Cursor cur = mDatabase.rawQuery(query, null);
        if (cur != null) {
            if (cur.getCount() > 0) {
                cur.moveToFirst();
                while (cur.isAfterLast() == false) {
                    ApplicationDataModel  data = new ApplicationDataModel();
                    data.setConfigId(cur.getString(cur.getColumnIndex("config_id")));
                    data.setUserName(cur.getString(cur.getColumnIndex("user_name")));
                    data.setUserPwd(cur.getString(cur.getColumnIndex("user_pwd")));
                    dataList.add(data);
                    cur.moveToNext();
                }

                cur.close();
            }
        }
        return dataList;
    }

}
