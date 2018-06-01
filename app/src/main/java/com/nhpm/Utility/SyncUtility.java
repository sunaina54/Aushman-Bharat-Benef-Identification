package com.nhpm.Utility;

import android.content.Context;

import com.customComponent.utility.ProjectPrefrence;
import com.nhpm.Models.response.rsbyMembers.RSBYItem;
import com.nhpm.Models.response.rsbyMembers.RsbyHouseholdItem;
import com.nhpm.Models.response.rsbyMembers.RsbyMemberSyncRequest;
import com.nhpm.Models.response.rsbyMembers.RsbySyncHouseholdRequest;
import com.nhpm.Models.response.seccMembers.HouseHoldItem;
import com.nhpm.Models.response.seccMembers.HouseHoldWriteItem;
import com.nhpm.Models.response.seccMembers.PopSeccWriteItem;
import com.nhpm.Models.response.seccMembers.SeccHouseholdResponse;
import com.nhpm.Models.response.seccMembers.SeccMemberItem;
import com.nhpm.Models.response.seccMembers.SeccMemberResponse;
import com.nhpm.Models.response.verifier.VerifierLocationItem;
import com.nhpm.Models.response.verifier.VerifierLoginResponse;

/**
 * Created by PSQ on 12/22/2016.
 */

public class SyncUtility {
    private static VerifierLocationItem verifierLocation;
    private static VerifierLoginResponse verifierDetail;

    public static SeccMemberItem prepareMemberItemForSync(SeccMemberItem item, Context context) {
        VerifierLocationItem downloadedLocation = VerifierLocationItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF
                , AppConstant.SELECTED_BLOCK, context));
        SeccMemberItem preparedItem = new SeccMemberItem();
        if (item.getPrintStatus() != null) {
            preparedItem.setPrintStatus(item.getPrintStatus());
        } else {
            preparedItem.setPrintStatus("N");
        }
        if (item.getMember_active_status() != null) {
            preparedItem.setMember_active_status(item.getMember_active_status());
        } else {
            preparedItem.setMember_active_status("N");
        }
        preparedItem.setStatecode(downloadedLocation.getStateCode());
        preparedItem.setDistrictcode(downloadedLocation.getDistrictCode());
        preparedItem.setTehsilcode(downloadedLocation.getTehsilCode());
        preparedItem.setTowncode(downloadedLocation.getVtCode());
        preparedItem.setWardid(downloadedLocation.getWardCode());
        preparedItem.setAhlblockno(downloadedLocation.getBlockCode());
        preparedItem.setAhlsubblockno(downloadedLocation.getSubBlockcode());
        preparedItem.setNhpsMemId(item.getNhpsMemId());
        preparedItem.setAhlTin(item.getAhlTin());
        preparedItem.setNhpsId(item.getNhpsId());
        preparedItem.setMobileNo(item.getMobileNo());
        preparedItem.setMobileAuthDt(item.getMobileAuthDt());
        preparedItem.setMobileAuth(item.getMobileAuth());
        preparedItem.setMobileNoSurveyedStat(item.getMobileNoSurveyedStat());
        preparedItem.setWhoseMobile(item.getWhoseMobile());
        preparedItem.setNameNominee(item.getNameNominee());
        preparedItem.setNomineeRelationName(item.getNomineeRelationName());
        preparedItem.setRelationNomineeCode(item.getRelationNomineeCode());
        preparedItem.setNomineeDetailSurveyedStat(item.getNomineeDetailSurveyedStat());
        preparedItem.setUrnNo(item.getUrnNo());
        preparedItem.setSchemeCode(item.getSchemeCode());
        preparedItem.setSchemeId(item.getSchemeId());
        preparedItem.setSchemeNo(item.getSchemeNo());
        preparedItem.setReqDOB(item.getReqDOB());
        preparedItem.setReqFatherName(item.getReqFatherName());
        preparedItem.setReqGenderCode(item.getReqGenderCode());
        preparedItem.setReqMarritalStatCode(item.getReqMarritalStatCode());
        preparedItem.setReqMotherName(item.getReqMotherName());
        preparedItem.setReqName(item.getReqName());
        preparedItem.setReqOccupation(item.getReqOccupation());
        preparedItem.setReqRelationCode(item.getReqRelationCode());
        preparedItem.setReqRelationName(item.getReqRelationName());
        preparedItem.setNhpsRelationCode(item.getNhpsRelationCode());
        preparedItem.setNhpsRelationName(item.getNhpsRelationName());
        preparedItem.setAadhaarVerifiedBy(item.getAadhaarVerifiedBy());
        preparedItem.setMemStatus(item.getMemStatus());
        preparedItem.setHhStatus(item.getHhStatus());
        preparedItem.setLockedSave(item.getLockedSave());
        preparedItem.setSyncedStatus(item.getSyncedStatus());
        preparedItem.setDataSource(AppConstant.SECC_SOURCE);
        preparedItem.setSchemeCode(AppConstant.SCHEME_CODE);
        preparedItem.setSyncedStatus(AppConstant.SYNCED_STATUS);
        if (item.getHhStatus() != null && item.getHhStatus().equalsIgnoreCase(AppConstant.HOUSEHOLD_FOUND)) {
            if (item.getMemStatus().equalsIgnoreCase(AppConstant.MEMBERFOUND_AND_PRESENT)) {
                preparedItem.setMemStatus(item.getMemStatus());
                preparedItem.setMemberPhoto1(item.getMemberPhoto1());
                preparedItem.setLatitude(item.getLatitude());
                preparedItem.setLongitude(item.getLongitude());
                if (item.getAadhaarStatus().equalsIgnoreCase(AppConstant.AADHAAR_STAT)) {
                    preparedItem.setAadhaarAuth(item.getAadhaarAuth());
                    preparedItem.setAadhaarCapturingMode(item.getAadhaarCapturingMode());
                    preparedItem.setAadhaarStatus(item.getAadhaarStatus());
                    preparedItem.setAadhaarAuthMode(item.getAadhaarAuthMode());
                    preparedItem.setAadhaarAuthDt(item.getAadhaarAuthDt());
                    preparedItem.setAadhaarCo(item.getAadhaarCo());
                    preparedItem.setAadhaarDist(item.getAadhaarDist());
                    preparedItem.setAadhaarDob(item.getAadhaarDob());
                    preparedItem.setAadhaarGender(item.getAadhaarGender());
                    preparedItem.setAadhaarGname(item.getAadhaarGname());
                    preparedItem.setAadhaarHouse(item.getAadhaarHouse());
                    preparedItem.setAadhaarLm(item.getAadhaarLm());
                    preparedItem.setAadhaarLoc(item.getAadhaarLoc());
                    preparedItem.setAadhaarNo(item.getAadhaarNo());
                    preparedItem.setNameAadhaar(item.getNameAadhaar());
                    preparedItem.setAadhaarPc(item.getAadhaarPc());
                    preparedItem.setAadhaarPo(item.getAadhaarPo());
                    preparedItem.setAadhaarState(item.getAadhaarState());
                    preparedItem.setAadhaarStatus(item.getAadhaarStatus());
                    preparedItem.setAadhaarStreet(item.getAadhaarStreet());
                    preparedItem.setAadhaarSubdist(item.getAadhaarSubdist());
                    preparedItem.setAadhaarVtc(item.getAadhaarVtc());
                    preparedItem.setAadhaarYob(item.getAadhaarYob());
                    preparedItem.setConsent(item.getConsent());
                    preparedItem.setUserid(item.getUserid());
                }
                if (item.getAadhaarStatus().equalsIgnoreCase(AppConstant.GOVT_ID_STAT)) {
                    preparedItem.setAadhaarStatus(item.getAadhaarStatus());
                    preparedItem.setIdNo(item.getIdNo());
                    preparedItem.setNameAsId(item.getNameAsId());
                    preparedItem.setGovtIdPhoto(item.getGovtIdPhoto());
                    preparedItem.setIdType(item.getIdType());


                }
            } else if (item.getMemStatus().equalsIgnoreCase(AppConstant.MEMBERFOUND_BUT_NOT_PRESENT)) {
                preparedItem.setMemStatus(item.getMemStatus());
                preparedItem.setLatitude(item.getLatitude());
                preparedItem.setLongitude(item.getLongitude());
                if (item.getAadhaarStatus().equalsIgnoreCase(AppConstant.AADHAAR_STAT)) {
                    preparedItem.setAadhaarAuth(item.getAadhaarAuth());
                    preparedItem.setAadhaarCapturingMode(item.getAadhaarCapturingMode());
                    preparedItem.setAadhaarStatus(item.getAadhaarStatus());
                    preparedItem.setAadhaarAuthMode(item.getAadhaarAuthMode());
                    preparedItem.setAadhaarAuthDt(item.getAadhaarAuthDt());
                    preparedItem.setAadhaarCo(item.getAadhaarCo());
                    preparedItem.setAadhaarDist(item.getAadhaarDist());
                    preparedItem.setAadhaarDob(item.getAadhaarDob());
                    preparedItem.setAadhaarGender(item.getAadhaarGender());
                    preparedItem.setAadhaarGname(item.getAadhaarGname());
                    preparedItem.setAadhaarHouse(item.getAadhaarHouse());
                    preparedItem.setAadhaarLm(item.getAadhaarLm());
                    preparedItem.setAadhaarLoc(item.getAadhaarLoc());
                    preparedItem.setAadhaarNo(item.getAadhaarNo());
                    preparedItem.setNameAadhaar(item.getNameAadhaar());
                    preparedItem.setAadhaarPc(item.getAadhaarPc());
                    preparedItem.setAadhaarPo(item.getAadhaarPo());
                    preparedItem.setAadhaarState(item.getAadhaarState());
                    preparedItem.setAadhaarStatus(item.getAadhaarStatus());
                    preparedItem.setAadhaarStreet(item.getAadhaarStreet());
                    preparedItem.setAadhaarSubdist(item.getAadhaarSubdist());
                    preparedItem.setAadhaarVtc(item.getAadhaarVtc());
                    preparedItem.setAadhaarYob(item.getAadhaarYob());
                    preparedItem.setConsent(item.getConsent());
                    preparedItem.setUserid(item.getUserid());
                }
                if (item.getAadhaarStatus().equalsIgnoreCase(AppConstant.GOVT_ID_STAT)) {
                    preparedItem.setAadhaarStatus(item.getAadhaarStatus());
                    preparedItem.setIdNo(item.getIdNo());
                    preparedItem.setNameAsId(item.getNameAsId());
                    preparedItem.setIdType(item.getIdType());
                    preparedItem.setGovtIdPhoto(item.getGovtIdPhoto());
                }
            } else {
                preparedItem.setMemStatus(item.getMemStatus());
            }
        } else {
            preparedItem.setHhStatus(item.getHhStatus());
        }
        return preparedItem;
    }

    public static HouseHoldItem preparedHouseholdItem(HouseHoldItem item, Context context) {

        HouseHoldItem preparedItem = new HouseHoldItem();
        VerifierLocationItem downloadedLocation = VerifierLocationItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF
                , AppConstant.SELECTED_BLOCK, context));
        preparedItem.setStatecode(downloadedLocation.getStateCode());
        preparedItem.setDistrictcode(downloadedLocation.getDistrictCode());
        preparedItem.setTehsilcode(downloadedLocation.getTehsilCode());
        preparedItem.setTowncode(downloadedLocation.getVtCode());
        preparedItem.setWardid(downloadedLocation.getWardCode());
        preparedItem.setAhlblockno(downloadedLocation.getBlockCode());
        preparedItem.setAhlSubBlockNo(downloadedLocation.getSubBlockcode());
        preparedItem.setNhpsMemId(item.getNhpsMemId());
        preparedItem.setAppVersion(item.getAppVersion());
        preparedItem.setAhlTin("");
        preparedItem.setHhStatus(item.getHhStatus());
        preparedItem.setLockSave(item.getLockSave());
        preparedItem.setNhpsId(item.getNhpsId());
        preparedItem.setSyncedStatus(AppConstant.SYNCED_STATUS);
        return preparedItem;
    }


    public static SeccMemberItem clearCollectedData(SeccMemberItem preparedItem) {
        preparedItem.setMobileNo("");
        preparedItem.setMobileAuthDt("");
        preparedItem.setMobileAuth("");
        preparedItem.setMobileNoSurveyedStat("");
        preparedItem.setWhoseMobile("");
        preparedItem.setNameNominee("");
        preparedItem.setNomineeRelationName("");
        preparedItem.setRelationNomineeCode("");
        preparedItem.setNomineeDetailSurveyedStat("");
        preparedItem.setUrnNo("");
        preparedItem.setSchemeCode("");
        preparedItem.setSchemeId("");
        preparedItem.setSchemeNo("");
        preparedItem.setReqDOB("");
        preparedItem.setReqFatherName("");
        preparedItem.setReqGenderCode("");
        preparedItem.setReqMarritalStatCode("");
        preparedItem.setReqMotherName("");
        preparedItem.setReqName("");
        preparedItem.setReqOccupation("");
        preparedItem.setReqRelationCode("");
        preparedItem.setReqRelationName("");
        preparedItem.setNhpsRelationCode(null);
        preparedItem.setNhpsRelationName(null);
        preparedItem.setMemStatus("");
        preparedItem.setHhStatus("");
        preparedItem.setLockedSave(null);
        preparedItem.setSyncedStatus(null);
        preparedItem.setSchemeCode("");
        preparedItem.setMemStatus("");
        preparedItem.setMemberPhoto1("");
        preparedItem.setLatitude("");
        preparedItem.setLongitude("");
        preparedItem.setAadhaarStatus("");
        preparedItem.setAadhaarVerifiedBy("");
        preparedItem.setAadhaarAuth("");
        preparedItem.setAadhaarCapturingMode("");
        preparedItem.setAadhaarAuthMode("");
        preparedItem.setAadhaarAuthDt("");
        preparedItem.setAadhaarCo("");
        preparedItem.setAadhaarDist("");
        preparedItem.setAadhaarDob("");
        preparedItem.setAadhaarGender("");
        preparedItem.setAadhaarGname("");
        preparedItem.setAadhaarHouse("");
        preparedItem.setAadhaarLm("");
        preparedItem.setAadhaarLoc("");
        preparedItem.setAadhaarNo("");
        preparedItem.setNameAadhaar("");
        preparedItem.setAadhaarPc("");
        preparedItem.setAadhaarPo("");
        preparedItem.setAadhaarState("");
        preparedItem.setAadhaarStreet("");
        preparedItem.setAadhaarSubdist("");
        preparedItem.setAadhaarVtc("");
        preparedItem.setAadhaarYob("");
        preparedItem.setConsent("");
        preparedItem.setUserid("");
        preparedItem.setIdNo("");
        preparedItem.setNameAsId("");
        preparedItem.setGovtIdPhoto("");
        preparedItem.setIdType("");
        preparedItem.setError_code(null);
        preparedItem.setError_type(null);
        preparedItem.setError_msg(null);

        return preparedItem;
    }

    public static HouseHoldItem clearHousehold(HouseHoldItem preparedItem) {
        preparedItem.setHhStatus("");
        preparedItem.setLockSave(null);
        preparedItem.setSyncedStatus(null);
        preparedItem.setError_code(null);
        preparedItem.setError_type(null);
        preparedItem.setError_msg(null);
        return preparedItem;
    }

    public static RsbySyncHouseholdRequest preparedRsbyHouseholdItem(RsbyHouseholdItem item) {
        RsbySyncHouseholdRequest preparedItem = new RsbySyncHouseholdRequest();
        preparedItem.setRsbyFamilyPhoto(item.getHofImage());
        preparedItem.setEnddate(item.getEnddate());
        preparedItem.setAppVersion(item.getAppVersion());
        preparedItem.setName(item.getName());
        preparedItem.setHhStatus(item.getHhStatus());
        preparedItem.setEnddate(item.getEnddate());
        preparedItem.setStartdate(item.getStartdate());
        preparedItem.setBlockcode(item.getBlockCode());
        preparedItem.setDistrictcode(item.getDistrictCode());
        preparedItem.setDob(item.getDob());
        preparedItem.setDoorhouse(item.getDoorhouse());
        preparedItem.setHofnamereg(item.getHofnamereg());
        preparedItem.setInsccode(item.getInsccode());
        preparedItem.setIssuestimespam(item.getIssuesTimespam());
        preparedItem.setLockedSave(item.getLockedSave());
        preparedItem.setGender(item.getGender());
        preparedItem.setMemid(item.getMemid());
        preparedItem.setNhpsId(item.getNhpsId());
        preparedItem.setPanchyattowncode(item.getPanchyatTownCode());
        preparedItem.setPolicyamt(item.getPolicyamt());
        preparedItem.setPolicyno(item.getPolicyno());
        preparedItem.setRelcode(item.getRelcode());
        preparedItem.setRsbymemid(item.getRsbyMemId());
        preparedItem.setUrnid(item.getUrnId());
        preparedItem.setVillagecode(item.getVillageCode());
        preparedItem.setVlAadharNo(item.getVlAadharNo());
        preparedItem.setVlStateCode(item.getVlStateCode());
        preparedItem.setVlDistrictCode(item.getVlDistrictCode());
        preparedItem.setVlTehsilCode(item.getVlTehsilCode());
        preparedItem.setVlVtCode(item.getVlVtCode());
        preparedItem.setVlWardCode(item.getVlWardCode());
        preparedItem.setVlBlockCode(item.getVlBlockCode());
        preparedItem.setVlSubBlockcode(item.getVlSubBlockcode());
        preparedItem.setVlRuralUrban(item.getVlRuralUrban());
        preparedItem.setCsmNo(item.getCsmNo());
        preparedItem.setCardCategory(item.getCardCategory());
        preparedItem.setCardType(item.getCardType());
        preparedItem.setSyncedStatus(AppConstant.SYNCED_STATUS);
        preparedItem.setIssuestimespam(item.getIssuesTimespam());
        preparedItem.setHofnamereg(item.getHofnamereg());
        preparedItem.setDoorhouse(item.getDoorhouse());
        preparedItem.setVillagecode(item.getVillageCode());
        preparedItem.setPanchyattowncode(item.getPanchyatTownCode());
        preparedItem.setBlockcode(item.getBlockCode());
        preparedItem.setDistrictcode(item.getDistrictCode());
        preparedItem.setStatecode(item.getStateCode());
        return preparedItem;
    }

    public static RsbySyncHouseholdRequest preparedRsbyHouseholdItem(HouseHoldItem item, Context context) {
        verifierDetail = VerifierLoginResponse.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.VERIFIER_CONTENT, context));
        verifierLocation = VerifierLocationItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF
                , AppConstant.SELECTED_BLOCK, context));
        RsbySyncHouseholdRequest preparedItem = new RsbySyncHouseholdRequest();
        preparedItem.setRsbyFamilyPhoto(item.getRsbyFamilyPhoto());
        preparedItem.setDataSource(item.getDataSource());
        preparedItem.setName(item.getRsbyName());
        preparedItem.setHhStatus(item.getHhStatus());
        if (item.getRsbyStartdate() != null) {
            preparedItem.setStartdate(String.valueOf(AppUtility.convertRsbyDateToLong(item.getRsbyStartdate(), AppConstant.RSBY_DATE_FORMAT)));
        }
        if (item.getRsbyEnddate() != null) {
            preparedItem.setEnddate(String.valueOf(AppUtility.convertRsbyDateToLong(item.getRsbyEnddate(), AppConstant.RSBY_DATE_FORMAT)));
        }
        preparedItem.setBlockcode(item.getRsbyBlockCode());
        preparedItem.setDistrictcode(item.getRsbyDistrictCode());
        preparedItem.setDob(item.getRsbyDob());
        preparedItem.setDoorhouse(item.getRsbyDoorhouse());
        preparedItem.setHofnamereg(item.getRsbyHofnamereg());
        preparedItem.setInsccode(item.getRsbyInsccode());
        if (item.getRsbyIssuesTimespam() != null) {
            String timeStamp = String.valueOf(AppUtility.convertRsbyIssuesTimeStampDateToLong(AppUtility.convertRsbyIssueTimeDateFormat
                    (item.getRsbyIssuesTimespam()), AppConstant.RSBY_ISSUES_TIME_STAMP_DATE_FORMAT));
            preparedItem.setIssuestimespam(timeStamp);
        }
        preparedItem.setLockedSave(item.getLockSave());
        preparedItem.setGender(item.getRsbyGender());
        preparedItem.setMemid(item.getRsbyMemid());
        preparedItem.setNhpsId(item.getNhpsId());
        preparedItem.setPanchyattowncode(item.getRsbyPanchyatTownCode());
        preparedItem.setPolicyamt(item.getRsbyPolicyamt());
        preparedItem.setPolicyno(item.getRsbyPolicyno());
        preparedItem.setRelcode(item.getRsbyRelcode());
        preparedItem.setRsbymemid(item.getRsbyMemId());
        preparedItem.setUrnid(item.getRsbyUrnId());
        preparedItem.setVillagecode(item.getRsbyVillageCode());
        preparedItem.setVlStateCode(item.getStatecode());
        preparedItem.setVlDistrictCode(item.getDistrictcode());
        preparedItem.setVlTehsilCode(item.getTehsilcode());
        preparedItem.setVlVtCode(item.getTowncode());
        preparedItem.setVlWardCode(item.getWardid());
        preparedItem.setVlBlockCode(item.getAhlblockno());
        preparedItem.setVlSubBlockcode(item.getAhlSubBlockNo());
        preparedItem.setVlRuralUrban(item.getRuralUrban());
        if (verifierDetail != null && verifierDetail.getAadhaarNumber() != null) {
            preparedItem.setVlAadharNo(verifierDetail.getAadhaarNumber());
        }
        preparedItem.setCsmNo(item.getRsbyCsmNo());
        preparedItem.setCardCategory(item.getRsbyCardCategory());
        preparedItem.setCardType(item.getRsbyCardType());
        preparedItem.setSyncedStatus(AppConstant.SYNCED_STATUS);
        if (item.getRsbyIssuesTimespam() != null) {
            String timeStamp = String.valueOf(AppUtility.convertRsbyIssuesTimeStampDateToLong(AppUtility.convertRsbyIssueTimeDateFormat
                    (item.getRsbyIssuesTimespam()), AppConstant.RSBY_ISSUES_TIME_STAMP_DATE_FORMAT));
            preparedItem.setIssuestimespam(timeStamp);
        }
        preparedItem.setHofnamereg(item.getRsbyHofnamereg());
        preparedItem.setDoorhouse(item.getRsbyDoorhouse());
        preparedItem.setVillagecode(item.getRsbyVillageCode());
        preparedItem.setPanchyattowncode(item.getRsbyPanchyatTownCode());
        preparedItem.setBlockcode(item.getRsbyBlockCode());
        preparedItem.setDistrictcode(item.getRsbyDistrictCode());
        preparedItem.setStatecode(item.getRsbyStateCode());
        return preparedItem;
    }

    public static RsbyMemberSyncRequest prepareRsbyMemberItemForSync(RSBYItem item) {
        RsbyMemberSyncRequest preparedItem = new RsbyMemberSyncRequest();
        preparedItem.setVlStateCode(item.getVl_stateCode());
        preparedItem.setVlDistrictCode(item.getVl_districtCode());
        preparedItem.setVlTehsilCode(item.getVl_tehsilCode());
        preparedItem.setVlVtCode(item.getVl_vtCode());
        preparedItem.setVlWardCode(item.getVl_wardCode());
        preparedItem.setVlBlockCode(item.getVl_blockCode());
        preparedItem.setVlSubBlockcode(item.getVl_subBlockcode());
        preparedItem.setVlRuralUrban(item.getVl_ruralUrban());
        preparedItem.setCsmNo(item.getCsmNo());
        preparedItem.setCardCategory(item.getCardCategory());
        preparedItem.setCardType(item.getCardType());
        preparedItem.setRsbymemid(item.getRsbyMemId());
        preparedItem.setUrnid(item.getUrnId());
        preparedItem.setIssuestimespam(item.getIssuesTimespam());
        preparedItem.setHofnamereg(item.getHofnamereg());
        preparedItem.setDoorhouse(item.getDoorhouse());
        preparedItem.setVillagecode(item.getVillageCode());
        preparedItem.setPanchyattowncode(item.getPanchyatTownCode());
        preparedItem.setBlockcode(item.getBlockCode());
        preparedItem.setDistrictcode(item.getDistrictCode());
        preparedItem.setStatecode(item.getStateCode());
        preparedItem.setMemid(item.getMemid());
        preparedItem.setName(item.getName());
        preparedItem.setDob(item.getDob());
        preparedItem.setGender(item.getGender());
        preparedItem.setRelcode(item.getRelcode());
        preparedItem.setInsccode(item.getInsccode());
        preparedItem.setPolicyno(item.getPolicyno());
        preparedItem.setPolicyamt(item.getPolicyamt());
        preparedItem.setStartdate(item.getStartdate());
        preparedItem.setEnddate(item.getEnddate());
        preparedItem.setNhpsMemId(item.getNhpsMemId());
        preparedItem.setMobileNo(item.getMobileNo());
        preparedItem.setMobileAuthDt(item.getMobileAuthDt());
        preparedItem.setMobileAuth(item.getMobileAuth());
        preparedItem.setWhoseMobile(item.getWhoseMobile());
        preparedItem.setNameNominee(item.getNameNominee());
        preparedItem.setNomineeRelationName(item.getNomineeRelationName());
        preparedItem.setRelationNomineeCode(item.getRelationNomineeCode());
        preparedItem.setUrnNo(item.getUrnNo());
        preparedItem.setSchemeCode(item.getSchemeCode());
        preparedItem.setSchemeId(item.getSchemeId());
        preparedItem.setSchemeNo(item.getSchemeNo());
        preparedItem.setNhpsRelationCode(item.getNhpsRelationCode());
        preparedItem.setNhpsRelationName(item.getNhpsRelationName());
        preparedItem.setAadhaarVerifiedBy(item.getAadhaarVerifiedBy());
        preparedItem.setMemStatus(item.getMemStatus());
        preparedItem.setHhStatus(item.getHhStatus());
        preparedItem.setLockedSave(item.getLockedSave());
        preparedItem.setSyncedStatus(item.getSyncedStatus());
        preparedItem.setDataSource(AppConstant.SECC_SOURCE);
        preparedItem.setSchemeCode(AppConstant.SCHEME_CODE);
        preparedItem.setSyncedStatus(AppConstant.SYNCED_STATUS);
        if (item.getHhStatus() != null && item.getHhStatus().equalsIgnoreCase(AppConstant.HOUSEHOLD_FOUND)) {
            if (item.getMemStatus().equalsIgnoreCase(AppConstant.MEMBERFOUND_AND_PRESENT)) {
                preparedItem.setMemStatus(item.getMemStatus());
                preparedItem.setMemberPhoto1(item.getMemberPhoto1());
                preparedItem.setLatitude(item.getLatitude());
                preparedItem.setLongitude(item.getLongitude());
                if (item.getAadhaarStatus().equalsIgnoreCase(AppConstant.AADHAAR_STAT)) {
                    preparedItem.setAadhaarAuth(item.getAadhaarAuth());
                    preparedItem.setAadhaarCapturingMode(item.getAadhaarCapturingMode());
                    preparedItem.setAadhaarStatus(item.getAadhaarStatus());
                    preparedItem.setAadhaarAuthMode(item.getAadhaarAuthMode());
                    preparedItem.setAadhaarAuthDt(item.getAadhaarAuthDt());
                    preparedItem.setAadhaarCo(item.getAadhaarCo());
                    preparedItem.setAadhaarDist(item.getAadhaarDist());
                    preparedItem.setAadhaarDob(item.getAadhaarDob());
                    preparedItem.setAadhaarGender(item.getAadhaarGender());
                    preparedItem.setAadhaarGname(item.getAadhaarGname());
                    preparedItem.setAadhaarHouse(item.getAadhaarHouse());
                    preparedItem.setAadhaarLm(item.getAadhaarLm());
                    preparedItem.setAadhaarLoc(item.getAadhaarLoc());
                    preparedItem.setAadhaarNo(item.getAadhaarNo());
                    preparedItem.setNameAadhaar(item.getNameAadhaar());
                    preparedItem.setAadhaarPc(item.getAadhaarPc());
                    preparedItem.setAadhaarPo(item.getAadhaarPo());
                    preparedItem.setAadhaarState(item.getAadhaarState());
                    preparedItem.setAadhaarStatus(item.getAadhaarStatus());
                    preparedItem.setAadhaarStreet(item.getAadhaarStreet());
                    preparedItem.setAadhaarSubdist(item.getAadhaarSubdist());
                    preparedItem.setAadhaarVtc(item.getAadhaarVtc());
                    preparedItem.setAadhaarYob(item.getAadhaarYob());
                    preparedItem.setConsent(item.getConsent());
                    preparedItem.setUserid(item.getUserid());
                }
                if (item.getAadhaarStatus().equalsIgnoreCase(AppConstant.GOVT_ID_STAT)) {
                    preparedItem.setAadhaarStatus(item.getAadhaarStatus());
                    preparedItem.setIdNo(item.getIdNo());
                    preparedItem.setNameAsId(item.getNameAsId());
                    preparedItem.setGovtIdPhoto(item.getGovtIdPhoto());
                    preparedItem.setIdType(item.getIdType());
                }
            } else if (item.getMemStatus().equalsIgnoreCase(AppConstant.MEMBERFOUND_BUT_NOT_PRESENT)) {
                preparedItem.setMemStatus(item.getMemStatus());
                preparedItem.setLatitude(item.getLatitude());
                preparedItem.setLongitude(item.getLongitude());
                if (item.getAadhaarStatus().equalsIgnoreCase(AppConstant.AADHAAR_STAT)) {
                    preparedItem.setAadhaarAuth(item.getAadhaarAuth());
                    preparedItem.setAadhaarCapturingMode(item.getAadhaarCapturingMode());
                    preparedItem.setAadhaarStatus(item.getAadhaarStatus());
                    preparedItem.setAadhaarAuthMode(item.getAadhaarAuthMode());
                    preparedItem.setAadhaarAuthDt(item.getAadhaarAuthDt());
                    preparedItem.setAadhaarCo(item.getAadhaarCo());
                    preparedItem.setAadhaarDist(item.getAadhaarDist());
                    preparedItem.setAadhaarDob(item.getAadhaarDob());
                    preparedItem.setAadhaarGender(item.getAadhaarGender());
                    preparedItem.setAadhaarGname(item.getAadhaarGname());
                    preparedItem.setAadhaarHouse(item.getAadhaarHouse());
                    preparedItem.setAadhaarLm(item.getAadhaarLm());
                    preparedItem.setAadhaarLoc(item.getAadhaarLoc());
                    preparedItem.setAadhaarNo(item.getAadhaarNo());
                    preparedItem.setNameAadhaar(item.getNameAadhaar());
                    preparedItem.setAadhaarPc(item.getAadhaarPc());
                    preparedItem.setAadhaarPo(item.getAadhaarPo());
                    preparedItem.setAadhaarState(item.getAadhaarState());
                    preparedItem.setAadhaarStatus(item.getAadhaarStatus());
                    preparedItem.setAadhaarStreet(item.getAadhaarStreet());
                    preparedItem.setAadhaarSubdist(item.getAadhaarSubdist());
                    preparedItem.setAadhaarVtc(item.getAadhaarVtc());
                    preparedItem.setAadhaarYob(item.getAadhaarYob());
                    preparedItem.setConsent(item.getConsent());
                    preparedItem.setUserid(item.getUserid());
                }
                if (item.getAadhaarStatus().equalsIgnoreCase(AppConstant.GOVT_ID_STAT)) {
                    preparedItem.setAadhaarStatus(item.getAadhaarStatus());
                    preparedItem.setIdNo(item.getIdNo());
                    preparedItem.setNameAsId(item.getNameAsId());
                    preparedItem.setIdType(item.getIdType());
                    preparedItem.setGovtIdPhoto(item.getGovtIdPhoto());
                }
            } else {
                preparedItem.setMemStatus(item.getMemStatus());
            }
        } else {
            preparedItem.setHhStatus(item.getHhStatus());
        }
        return preparedItem;
    }

    public static RsbyMemberSyncRequest prepareRsbyMemberItemForSync(SeccMemberItem item, Context context) {
        System.out.print(item);
        verifierDetail = VerifierLoginResponse.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.VERIFIER_CONTENT, context));
        verifierLocation = VerifierLocationItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF
                , AppConstant.SELECTED_BLOCK, context));
        RsbyMemberSyncRequest preparedItem = new RsbyMemberSyncRequest();
        if (item.getPrintStatus() != null) {
            preparedItem.setPrintStatus(item.getPrintStatus());
        } else {
            preparedItem.setPrintStatus("N");
        }
        if (item.getMember_active_status() != null) {
            preparedItem.setMemActiveStatus(item.getMember_active_status());
        } else {
            preparedItem.setMemActiveStatus("N");
        }
        preparedItem.setVlStateCode(item.getStatecode());
        preparedItem.setVlDistrictCode(item.getDistrictcode());
        preparedItem.setVlTehsilCode(item.getTehsilcode());
        preparedItem.setVlVtCode(item.getTowncode());
        preparedItem.setVlWardCode(item.getWardid());
        preparedItem.setVlBlockCode(item.getAhlblockno());
        preparedItem.setVlSubBlockcode(item.getAhlsubblockno());
        preparedItem.setVlRuralUrban(item.getRuralUrban());
        if (verifierDetail != null && verifierDetail.getAadhaarNumber() != null) {
            preparedItem.setVlAadharNo(verifierDetail.getAadhaarNumber());
        }
        preparedItem.setCsmNo(item.getRsbyCsmNo());
        preparedItem.setCardCategory(item.getRsbyCardCategory());
        preparedItem.setCardType(item.getRsbyCardType());
        preparedItem.setRsbymemid(item.getRsbyMemId());
        preparedItem.setUrnid(item.getRsbyUrnId());
        if (item.getRsbyIssuesTimespam() != null) {
            String timeStamp = String.valueOf(AppUtility.convertRsbyIssuesTimeStampDateToLong(AppUtility.convertRsbyIssueTimeDateFormat
                    (item.getRsbyIssuesTimespam()), AppConstant.RSBY_ISSUES_TIME_STAMP_DATE_FORMAT));
            preparedItem.setIssuestimespam(timeStamp);
        }
        preparedItem.setHofnamereg(item.getRsbyHofnamereg());
        preparedItem.setDoorhouse(item.getRsbyDoorhouse());
        preparedItem.setVillagecode(item.getRsbyVillageCode());
        preparedItem.setPanchyattowncode(item.getRsbyPanchyatTownCode());
        preparedItem.setBlockcode(item.getRsbyBlockCode());
        preparedItem.setDistrictcode(item.getRsbyDistrictCode());
        String str = item.getRsbyStateCode();
        System.out.print(str);
        preparedItem.setStatecode(item.getRsbyStateCode());
        preparedItem.setMemid(item.getRsbyMemid());
        preparedItem.setName(item.getRsbyName());
        preparedItem.setDob(item.getRsbyDob());
        preparedItem.setGender(item.getRsbyGender());
        preparedItem.setRelcode(item.getRsbyRelcode());
        preparedItem.setInsccode(item.getRsbyInsccode());
        preparedItem.setPolicyno(item.getRsbyPolicyno());
        preparedItem.setPolicyamt(item.getRsbyPolicyamt());

        if (item.getRsbyStartdate() != null) {

            preparedItem.setStartdate(String.valueOf(AppUtility.convertRsbyDateToLong(item.getRsbyStartdate(), AppConstant.RSBY_DATE_FORMAT)));
        }
        if (item.getRsbyEnddate() != null) {
            preparedItem.setEnddate(String.valueOf(AppUtility.convertRsbyDateToLong(item.getRsbyEnddate(), AppConstant.RSBY_DATE_FORMAT)));
        }

        preparedItem.setNhpsMemId(item.getNhpsMemId());
        preparedItem.setMobileNo(item.getMobileNo());
        preparedItem.setMobileAuthDt(item.getMobileAuthDt());
        preparedItem.setMobileAuth(item.getMobileAuth());
        preparedItem.setWhoseMobile(item.getWhoseMobile());
        preparedItem.setNameNominee(item.getNameNominee());
        preparedItem.setNomineeRelationName(item.getNomineeRelationName());
        preparedItem.setRelationNomineeCode(item.getRelationNomineeCode());
        if (item.getUrnNo() != null && !item.getUrnNo().equalsIgnoreCase("")) {
            preparedItem.setUrnNo(item.getUrnNo());
        } else {
            preparedItem.setUrnNo(item.getRsbyUrnId());
        }
        preparedItem.setSchemeCode(item.getSchemeCode());
        preparedItem.setSchemeId(item.getSchemeId());
        preparedItem.setSchemeNo(item.getSchemeNo());
        preparedItem.setNhpsRelationCode(item.getNhpsRelationCode());
        preparedItem.setNhpsRelationName(item.getNhpsRelationName());
        preparedItem.setAadhaarVerifiedBy(item.getAadhaarVerifiedBy());
        preparedItem.setMemStatus(item.getMemStatus());
        preparedItem.setHhStatus(item.getHhStatus());
        preparedItem.setLockedSave(item.getLockedSave());
        preparedItem.setSyncedStatus(item.getSyncedStatus());
        preparedItem.setDataSource(item.getDataSource());
        preparedItem.setSchemeCode(AppConstant.SCHEME_CODE);
        preparedItem.setSyncedStatus(AppConstant.SYNCED_STATUS);
        if (item.getHhStatus() != null && item.getHhStatus().equalsIgnoreCase(AppConstant.HOUSEHOLD_FOUND)) {
            if (item.getMemStatus().equalsIgnoreCase(AppConstant.MEMBERFOUND_AND_PRESENT)) {
                preparedItem.setMemStatus(item.getMemStatus());
                preparedItem.setMemberPhoto1(item.getMemberPhoto1());
                preparedItem.setLatitude(item.getLatitude());
                preparedItem.setLongitude(item.getLongitude());
                if (item.getAadhaarStatus().equalsIgnoreCase(AppConstant.AADHAAR_STAT)) {
                    preparedItem.setAadhaarAuth(item.getAadhaarAuth());
                    preparedItem.setAadhaarCapturingMode(item.getAadhaarCapturingMode());
                    preparedItem.setAadhaarStatus(item.getAadhaarStatus());
                    preparedItem.setAadhaarAuthMode(item.getAadhaarAuthMode());
                    preparedItem.setAadhaarAuthDt(item.getAadhaarAuthDt());
                    preparedItem.setAadhaarCo(item.getAadhaarCo());
                    preparedItem.setAadhaarDist(item.getAadhaarDist());
                    preparedItem.setAadhaarDob(item.getAadhaarDob());
                    preparedItem.setAadhaarGender(item.getAadhaarGender());
                    preparedItem.setAadhaarGname(item.getAadhaarGname());
                    preparedItem.setAadhaarHouse(item.getAadhaarHouse());
                    preparedItem.setAadhaarLm(item.getAadhaarLm());
                    preparedItem.setAadhaarLoc(item.getAadhaarLoc());
                    preparedItem.setAadhaarNo(item.getAadhaarNo());
                    preparedItem.setNameAadhaar(item.getNameAadhaar());
                    preparedItem.setAadhaarPc(item.getAadhaarPc());
                    preparedItem.setAadhaarPo(item.getAadhaarPo());
                    preparedItem.setAadhaarState(item.getAadhaarState());
                    preparedItem.setAadhaarStatus(item.getAadhaarStatus());
                    preparedItem.setAadhaarStreet(item.getAadhaarStreet());
                    preparedItem.setAadhaarSubdist(item.getAadhaarSubdist());
                    preparedItem.setAadhaarVtc(item.getAadhaarVtc());
                    preparedItem.setAadhaarYob(item.getAadhaarYob());
                    preparedItem.setConsent(item.getConsent());
                    preparedItem.setUserid(item.getUserid());
                }
                if (item.getAadhaarStatus().equalsIgnoreCase(AppConstant.GOVT_ID_STAT)) {
                    preparedItem.setAadhaarStatus(item.getAadhaarStatus());
                    preparedItem.setIdNo(item.getIdNo());
                    preparedItem.setNameAsId(item.getNameAsId());
                    preparedItem.setGovtIdPhoto(item.getGovtIdPhoto());
                    preparedItem.setIdType(item.getIdType());
                }
            } else if (item.getMemStatus().equalsIgnoreCase(AppConstant.MEMBERFOUND_BUT_NOT_PRESENT)) {
                preparedItem.setMemStatus(item.getMemStatus());
                preparedItem.setLatitude(item.getLatitude());
                preparedItem.setLongitude(item.getLongitude());
                if (item.getAadhaarStatus().equalsIgnoreCase(AppConstant.AADHAAR_STAT)) {
                    preparedItem.setAadhaarAuth(item.getAadhaarAuth());
                    preparedItem.setAadhaarCapturingMode(item.getAadhaarCapturingMode());
                    preparedItem.setAadhaarStatus(item.getAadhaarStatus());
                    preparedItem.setAadhaarAuthMode(item.getAadhaarAuthMode());
                    preparedItem.setAadhaarAuthDt(item.getAadhaarAuthDt());
                    preparedItem.setAadhaarCo(item.getAadhaarCo());
                    preparedItem.setAadhaarDist(item.getAadhaarDist());
                    preparedItem.setAadhaarDob(item.getAadhaarDob());
                    preparedItem.setAadhaarGender(item.getAadhaarGender());
                    preparedItem.setAadhaarGname(item.getAadhaarGname());
                    preparedItem.setAadhaarHouse(item.getAadhaarHouse());
                    preparedItem.setAadhaarLm(item.getAadhaarLm());
                    preparedItem.setAadhaarLoc(item.getAadhaarLoc());
                    preparedItem.setAadhaarNo(item.getAadhaarNo());
                    preparedItem.setNameAadhaar(item.getNameAadhaar());
                    preparedItem.setAadhaarPc(item.getAadhaarPc());
                    preparedItem.setAadhaarPo(item.getAadhaarPo());
                    preparedItem.setAadhaarState(item.getAadhaarState());
                    preparedItem.setAadhaarStatus(item.getAadhaarStatus());
                    preparedItem.setAadhaarStreet(item.getAadhaarStreet());
                    preparedItem.setAadhaarSubdist(item.getAadhaarSubdist());
                    preparedItem.setAadhaarVtc(item.getAadhaarVtc());
                    preparedItem.setAadhaarYob(item.getAadhaarYob());
                    preparedItem.setConsent(item.getConsent());
                    preparedItem.setUserid(item.getUserid());
                }
                if (item.getAadhaarStatus().equalsIgnoreCase(AppConstant.GOVT_ID_STAT)) {
                    preparedItem.setAadhaarStatus(item.getAadhaarStatus());
                    preparedItem.setIdNo(item.getIdNo());
                    preparedItem.setNameAsId(item.getNameAsId());
                    preparedItem.setIdType(item.getIdType());
                    // item.setGovtIdPhoto("agsgagjgjag");
                    preparedItem.setGovtIdPhoto(item.getGovtIdPhoto());
                }
            } else {
                preparedItem.setMemStatus(item.getMemStatus());
            }
        } else {
            preparedItem.setHhStatus(item.getHhStatus());
        }
        return preparedItem;
    }

    public static RSBYItem clearRsbyCollectedData(RSBYItem preparedItem) {
        preparedItem.setMobileNo("");
        preparedItem.setMobileAuthDt("");
        preparedItem.setMobileAuth("");
        preparedItem.setMobileNoSurveyedStat("");
        preparedItem.setWhoseMobile("");
        preparedItem.setNameNominee("");
        preparedItem.setNomineeRelationName("");
        preparedItem.setRelationNomineeCode("");
        preparedItem.setNomineeDetailSurveyedStat("");
        preparedItem.setUrnNo("");
        preparedItem.setSchemeCode("");
        preparedItem.setSchemeId("");
        preparedItem.setSchemeNo("");
        preparedItem.setNhpsRelationCode(null);
        preparedItem.setNhpsRelationName(null);
        preparedItem.setMemStatus("");
        preparedItem.setHhStatus("");
        preparedItem.setLockedSave(null);
        preparedItem.setSyncedStatus(null);
        preparedItem.setDataSource("");
        preparedItem.setSchemeCode("");
        preparedItem.setMemStatus("");
        preparedItem.setMemberPhoto1("");
        preparedItem.setLatitude("");
        preparedItem.setLongitude("");
        preparedItem.setAadhaarStatus("");
        preparedItem.setAadhaarVerifiedBy("");
        preparedItem.setAadhaarAuth("");
        preparedItem.setAadhaarCapturingMode("");
        preparedItem.setAadhaarAuthMode("");
        preparedItem.setAadhaarAuthDt("");
        preparedItem.setAadhaarCo("");
        preparedItem.setAadhaarDist("");
        preparedItem.setAadhaarDob("");
        preparedItem.setAadhaarGender("");
        preparedItem.setAadhaarGname("");
        preparedItem.setAadhaarHouse("");
        preparedItem.setAadhaarLm("");
        preparedItem.setAadhaarLoc("");
        preparedItem.setAadhaarNo("");
        preparedItem.setNameAadhaar("");
        preparedItem.setAadhaarPc("");
        preparedItem.setAadhaarPo("");
        preparedItem.setAadhaarState("");
        preparedItem.setAadhaarStreet("");
        preparedItem.setAadhaarSubdist("");
        preparedItem.setAadhaarVtc("");
        preparedItem.setAadhaarYob("");
        preparedItem.setConsent("");
        preparedItem.setUserid("");
        preparedItem.setIdNo("");
        preparedItem.setNameAsId("");
        preparedItem.setGovtIdPhoto("");
        preparedItem.setIdType("");
        preparedItem.setError_code(null);
        preparedItem.setError_type(null);
        preparedItem.setError_msg(null);

        return preparedItem;
    }


    public static SeccMemberResponse prepareMemberItemForReadSync(SeccMemberResponse item, Context context) {

        // SeccMemberResponse seccmemberResponse=new SeccMemberResponse();

        if (item.getSeccMemberList() != null && item.getSeccMemberList().size() > 0) {
            for (PopSeccWriteItem itemWrite : item.getPopSeccWriteList()) {

                //for (SeccMemberItem itemRead : item.getSeccMemberList()) {
                for (int i = 0; i < item.getSeccMemberList().size(); i++) {//SeccMemberItem itemRead : item.getSeccMemberList()) {
                    SeccMemberItem itemRead = item.getSeccMemberList().get(i);
                    if (itemWrite.getNhpsMemId().equalsIgnoreCase(itemRead.getNhpsMemId())) {

                        item.getSeccMemberList().get(i).setAadhaarStatus(itemWrite.getAadhaarStatus());
                        //  itemRead.setNhpsMemId(itemWrite.getNhpsMemId());
                        item.getSeccMemberList().get(i).setAadhaarAuth(itemWrite.getAadhaarAuth());
                        item.getSeccMemberList().get(i).setAadhaarAuthMode(itemWrite.getAadhaarAuthMode());
                        item.getSeccMemberList().get(i).setAadhaarCapturingMode(itemWrite.getAadhaarCapturingMode());
                        item.getSeccMemberList().get(i).setAadhaarCo(itemWrite.getAadhaarCo());
                        item.getSeccMemberList().get(i).setAadhaarDist(itemWrite.getAadhaarDist());
                        item.getSeccMemberList().get(i).setLongitude(itemWrite.getLongitude());
                        item.getSeccMemberList().get(i).setAadhaarDob(itemWrite.getAadhaarDob());
                        item.getSeccMemberList().get(i).setAadhaarGender(itemWrite.getAadhaarGender());
                        item.getSeccMemberList().get(i).setAadhaarGname(itemWrite.getAadhaarGname());
                        item.getSeccMemberList().get(i).setAadhaarHouse(itemWrite.getAadhaarHouse());
                        item.getSeccMemberList().get(i).setAadhaarLm(itemWrite.getAadhaarLm());
                        item.getSeccMemberList().get(i).setAadhaarLoc(itemWrite.getAadhaarLoc());
                        item.getSeccMemberList().get(i).setAadhaarNo(itemWrite.getAadhaarNo());
                        item.getSeccMemberList().get(i).setAadhaarPc(itemWrite.getAadhaarPc());
                        item.getSeccMemberList().get(i).setAadhaarState(itemWrite.getAadhaarState());
                        item.getSeccMemberList().get(i).setAadhaarStreet(itemWrite.getAadhaarStreet());
                        item.getSeccMemberList().get(i).setAadhaarSubdist(itemWrite.getAadhaarSubdist());
                        item.getSeccMemberList().get(i).setAadhaarVerifiedBy(itemWrite.getAadhaarVerifiedBy());
                        item.getSeccMemberList().get(i).setAadhaarVtc(itemWrite.getAadhaarVtc());
                        item.getSeccMemberList().get(i).setAadhaarYob(itemWrite.getAadhaarYob());
                        item.getSeccMemberList().get(i).setAadhaarPo(itemWrite.getAadhaarPo());
                        item.getSeccMemberList().get(i).setAppVersion(itemWrite.getAppVersion());
                        item.getSeccMemberList().get(i).setConsent(itemWrite.getConsent());
                        item.getSeccMemberList().get(i).setConsentDt(itemWrite.getConsentDt());
                        item.getSeccMemberList().get(i).setDataSource(itemWrite.getDataSource());
                        item.getSeccMemberList().get(i).setDateUpdated(itemWrite.getDateUpdated());
                        item.getSeccMemberList().get(i).setGovtIdPhoto(itemWrite.getGovtIdPhoto());
                        item.getSeccMemberList().get(i).setNhpsMemId(itemWrite.getNhpsMemId());
                        item.getSeccMemberList().get(i).setNhpsId(itemWrite.getNhpsId());
                        //     item.getSeccMemberList().get(i).setHhdNo(itemWrite.getHhdNo());
                        item.getSeccMemberList().get(i).setHhStatus(itemWrite.getHhStatus());
                        item.getSeccMemberList().get(i).setIdNo(itemWrite.getIdNo());
                        item.getSeccMemberList().get(i).setLatitude(itemWrite.getLatitude());
                        item.getSeccMemberList().get(i).setLockedSave(itemWrite.getLockedSave());
                        item.getSeccMemberList().get(i).setMarkForDeletion(itemWrite.getMarkForDeletion());
                        item.getSeccMemberList().get(i).setMemberPhoto1(itemWrite.getMemberPhoto1());
                        item.getSeccMemberList().get(i).setNameAadhaar(itemWrite.getNameAadhaar());
                        item.getSeccMemberList().get(i).setNameAsId(itemWrite.getNameAsId());
                        item.getSeccMemberList().get(i).setNameNominee(itemWrite.getNameNominee());
                        item.getSeccMemberList().get(i).setNhpsRelationCode(itemWrite.getNhpsRelationCode());
                        item.getSeccMemberList().get(i).setNhpsRelationName(itemWrite.getNhpsRelationName());
                        item.getSeccMemberList().get(i).setSchemeCode(itemWrite.getSchemeCode());
                        item.getSeccMemberList().get(i).setSchemeId(itemWrite.getSchemeId());
                        item.getSeccMemberList().get(i).setSchemeNo(itemWrite.getSchemeNo());
                        item.getSeccMemberList().get(i).setStateSchemeCodeAuth(itemWrite.getStateSchemeCodeAuth());
                        item.getSeccMemberList().get(i).setStateSchemeCodeAuthDt(itemWrite.getStateSchemeCodeAuthDt());
                        item.getSeccMemberList().get(i).setSyncedStatus(itemWrite.getSyncedStatus());
                        item.getSeccMemberList().get(i).setUrnAuth(itemWrite.getUrnAuth());
                        item.getSeccMemberList().get(i).setUrnNo(itemWrite.getUrnNo());
                        item.getSeccMemberList().get(i).setWhoseMobile(itemWrite.getWhoseMobile());
                        item.getSeccMemberList().get(i).setMemStatus(itemWrite.getMemStatus());
                        item.getSeccMemberList().get(i).setMobileNo(itemWrite.getMobileNo());
                        item.getSeccMemberList().get(i).setMobileAuth(itemWrite.getMobileAuth());
                        item.getSeccMemberList().get(i).setIdType(itemWrite.getIdType());
                        item.getSeccMemberList().get(i).setSyncDt(itemWrite.getSyncDt());
                    }
                }
            }
        }
        return item;
    }


    public static SeccHouseholdResponse prepareHouseHoldItemForReadSync(SeccHouseholdResponse item, Context context) {
        SeccHouseholdResponse houseHoldListResponse = new SeccHouseholdResponse();
        if (item.getSeccHouseholdList() != null && item.getSeccHouseholdList().size() > 0) {
            for (HouseHoldWriteItem itemWrite : item.getHouseSeccWriteList()) {
                for (HouseHoldItem itemRead : item.getSeccHouseholdList()) {
                    if (itemWrite.getNhpsMemId().equalsIgnoreCase(itemRead.getNhpsMemId())) {
                        itemRead.setSyncDt(itemWrite.getSyncDt());
                        itemRead.setAppVersion(itemWrite.getAppVersion());
                        itemRead.setHhStatus(itemWrite.getHhStatus());
                        itemRead.setNhpsId(itemWrite.getNhpsId());
                        itemRead.setSyncedStatus(itemWrite.getSyncedStatus());
                    }
                }
            }
        }
        return item;
    }


}