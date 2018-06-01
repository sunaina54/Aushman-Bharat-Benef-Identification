package com.nhpm.backgroundService;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.customComponent.utility.CustomHttp;
import com.customComponent.utility.ProjectPrefrence;
import com.nhpm.LocalDataBase.dto.SeccDatabase;
import com.nhpm.Models.response.rsbyMembers.RsbyHouseholdSyncResponse;
import com.nhpm.Models.response.rsbyMembers.RsbyMemberSyncRequest;
import com.nhpm.Models.response.rsbyMembers.RsbyMemberSyncResponse;
import com.nhpm.Models.response.rsbyMembers.RsbySyncHouseholdRequest;
import com.nhpm.Models.response.seccMembers.HouseHoldItem;
import com.nhpm.Models.response.seccMembers.SeccMemberItem;
import com.nhpm.Models.response.seccMembers.SyncHouseholdResponse;
import com.nhpm.Models.response.seccMembers.SyncSeccMemberResponse;
import com.nhpm.Models.response.verifier.VerifierLoginResponse;
import com.nhpm.Utility.AppConstant;
import com.nhpm.Utility.AppUtility;
import com.nhpm.Utility.SyncUtility;

import java.util.ArrayList;
import java.util.HashMap;


public class SyncService extends IntentService {
    private final String TAG = "Sync Service";
    private static final String ACTION_FOO = "com.nhpm.backgroundService.action.FOO";
    private static final String ACTION_BAZ = "com.nhpm.backgroundService.action.BAZ";
    public static volatile boolean shouldContinue = true;
    private static final String EXTRA_PARAM1 = "com.nhpm.backgroundService.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.nhpm.backgroundService.extra.PARAM2";
    private ArrayList<HouseHoldItem> pendingHouseholdList;
    public static String SYNC_ARG = "syncArg";
    private Context context;
    public static final String BROADCAST_ACTION = "SyncServiceBroadcast";
    private Intent broadCastIntent;
    public static final String RESPONSE_SUCCESS = "ResponseSuccess";
    public static final String SYNC_COMPLETE = "SyncComplete";
    private ArrayList<HouseHoldItem> pureList;
    private VerifierLoginResponse loginResponse;
    int broadcastIndex = 0;

    public SyncService() {
        super("SyncService");
    }

    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, SyncService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        context = getApplicationContext();
        loginResponse = (VerifierLoginResponse.create(ProjectPrefrence.
                getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.VERIFIER_CONTENT, context)));
        broadCastIntent = new Intent(BROADCAST_ACTION);
        if (intent != null) {
            pendingHouseholdList = (ArrayList<HouseHoldItem>) intent.getSerializableExtra(SYNC_ARG);
            AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "ArrayList : " + pendingHouseholdList.size());
            //  syncHousehold();
            try {
                findAllDuplicateAadhaar();
            } catch (Exception error) {
                error.printStackTrace();
                Toast.makeText(context, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }

           /* final String action = intent.getAction();
            if (ACTION_FOO.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionFoo(param1, param2);
            } else if (ACTION_BAZ.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionBaz(param1, param2);
            }*/
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */

    private void findAllDuplicateAadhaar() {
       /* ArrayList<HouseHoldItem> lockedList=SeccDatabase.seccHouseholdLockedList(AppConstant.LOCKED+"",context);
        ArrayList<SeccMemberItem> lockedMemberList=SeccDatabase.seccMemberLockedList(AppConstant.LOCKED+"",context);
        ArrayList<HouseHoldItem> list=new ArrayList<>();*/

        pureList = new ArrayList<>();
        for (int index = 0; index < pendingHouseholdList.size(); index++) {
            int duplicateCount = 0;
            HouseHoldItem item = pendingHouseholdList.get(index);
            if (item != null && item.getDataSource() != null && item.getDataSource().trim().equalsIgnoreCase(AppConstant.RSBY_SOURCE)) {
                findDuplicateRsbyHouseHold(item, duplicateCount);
            } else {
                findDuplicateSeccHousehold(item, duplicateCount);
            }

        }


        for (HouseHoldItem item : pureList) {
            //  pendingHouseholdList.remove(item);
            AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Sending validated data.." + broadcastIndex);
            //sendBroadcastStatus(pendingHouseholdIndex);
            // broadcastIndex=broadcastIndex+1;
            //sendBroadcastStatus(broadcastIndex);
            // syncHousehold(item);
            if (item.getHhStatus() != null && item.getHhStatus().trim().equalsIgnoreCase(AppConstant.HOUSEHOLD_FOUND)) {
                syncHouseHoldFound(item);
            } else {
                syncExceptHouseholdFound(item);
            }
            stopService();
            broadcastIndex++;
        }
    }

    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void syncHousehold(HouseHoldItem item) {
        // for(int pendingHouseholdIndex=0;pendingHouseholdIndex<pendingHouseholdList.size();pendingHouseholdIndex++) {
        // HouseHoldItem item = pendingHouseholdList.get(pendingHouseholdIndex);
        SeccMemberItem headOftheFamily;
        ArrayList<SeccMemberItem> seccList = SeccDatabase.getSeccMemberList(item.getHhdNo(), context);
        SeccMemberItem preparedMemberItem;
        int j = 0;
        for (SeccMemberItem item1 : seccList) {
            //item1.setMemberPhoto1("dAFDGHFDFHFDJHJ");
            // item1.setGovtIdPhoto("hahdhcadqcdwcq");
            AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Sync Status : " + item1.getSyncedStatus());
            if (item1.getSyncedStatus() != null && item1.getSyncedStatus().trim().equalsIgnoreCase(AppConstant.SYNCED_STATUS)) {

            } else {
                preparedMemberItem = SyncUtility.prepareMemberItemForSync(item1, context);
                AppUtility.showLog(AppConstant.LOG_STATUS, TAG, " Prepared Item : " + preparedMemberItem.serialize());
                AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Sync Member ahl tin :" +
                        " " + item.getAhlTin() + " Aaadhar Auth Status :" + preparedMemberItem.getAadhaarAuth() + "" +
                        " Aaadhar Capturing mode : " + preparedMemberItem.getAadhaarCapturingMode() + "" +
                        " Govt Id type : " + preparedMemberItem.getIdType() + " Data Source : " + preparedMemberItem.getDataSource());

                try {
                    HashMap<String, String> response = CustomHttp.httpPost(AppConstant.SYNC_MEMBER_API, preparedMemberItem.serialize(), null);
                    String payLoad = preparedMemberItem.serialize().toString();
                    AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Sync Member payLoad : " + payLoad);
                    System.out.print(payLoad);
                    if (response != null) {
                        AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Sync Member request : " + preparedMemberItem.serialize());
                        String strResp = response.get(AppConstant.RESPONSE_BODY);
                        AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Sync Member response : " + strResp);
                        SyncSeccMemberResponse responsItem = SyncSeccMemberResponse.create(strResp);
                        if (responsItem != null) {
                            if (responsItem != null && responsItem.isStatus()) {
                                if (responsItem.getSyncedStatus().equalsIgnoreCase(AppConstant.SYNCED_STATUS)) {
                                    item1.setSyncedStatus(responsItem.getSyncedStatus());
                                    item1.setNhpsId(responsItem.getNhpsId());
                                    item1.setSyncDt(responsItem.getSyncDt());
                                    item1.setError_code(null);
                                    item1.setError_type(null);
                                    item1.setError_msg(null);
                                    SeccDatabase.updateSeccMember(item1, context);
                                }
                            } else {
                                AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Inside Secc Sync status : internet issues ");
                                if (responsItem.getErrorCode().equalsIgnoreCase(AppConstant.AADHAAR_ALREADY_ALLOCATED)) {
                                    item1.setError_code(responsItem.getErrorCode());
                                    item1.setError_type(AppConstant.SYNCING_ERROR);
                                    item1.setError_msg(responsItem.getErrorMessage());
                                    SeccDatabase.updateSeccMember(item1, context);

                                }
                            }
                        } else {
                            AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Inside Secc Sync status : internet issues ");
                            item1.setError_code(AppConstant.INTERNET_ERROR_CODE);
                            item1.setError_type(AppConstant.SERVER_CONNECTION_TIMEOUT);
                            item1.setError_msg(AppConstant.INTERNET_ERROR_MSG);
                            SeccDatabase.updateSeccMember(item1, context);

                        }
                    }
                } catch (Exception e) {
                    item1.setError_code(AppConstant.INTERNET_ERROR_CODE);
                    item1.setError_type(AppConstant.SERVER_CONNECTION_TIMEOUT);
                    item1.setError_msg(AppConstant.INTERNET_ERROR_MSG);
                    SeccDatabase.updateSeccMember(item1, context);
                }
            }
        }

        ArrayList<SeccMemberItem> updatedSeccList = SeccDatabase.getSeccMemberList(item.getHhdNo(), context);
        boolean isAllMemberSynced = false;
        for (SeccMemberItem item1 : updatedSeccList) {
            if (item1.getSyncedStatus() == null || !item1.getSyncedStatus().equalsIgnoreCase(AppConstant.SYNCED_STATUS)) {
                isAllMemberSynced = true;
                break;
            }
        }
        if (!isAllMemberSynced) {
            item.setValidatedBy(loginResponse.getHnoAadhaarNo());
            HouseHoldItem preparedItem = SyncUtility.preparedHouseholdItem(item, context);
            AppUtility.showLog(AppConstant.LOG_STATUS, TAG, " Update household Request : " + preparedItem.serialize());
            try {
                HashMap<String, String> respStr = CustomHttp.httpPost(
                        AppConstant.HOUSE_HOLD_API, preparedItem.serialize(), null);
                String houseHoldPayload = preparedItem.serialize().toString();
                System.out.print(houseHoldPayload);
                AppUtility.showLog(AppConstant.LOG_STATUS, TAG, " Household payload" + houseHoldPayload);
                AppUtility.showLog(AppConstant.LOG_STATUS, TAG, " Household in null" + respStr);
                if (respStr != null) {
                    String response = respStr.get(AppConstant.RESPONSE_BODY);
                    //  AppUtility.alertWithOk(context,"sync household response : "+response);
                    SyncHouseholdResponse responseItem = SyncHouseholdResponse.create(response);
                    if (responseItem != null && responseItem.isStatus()) {
                        if (responseItem.getSyncedStatus().equalsIgnoreCase(AppConstant.SYNCED_STATUS)) {
                            item.setSyncedStatus(responseItem.getSyncedStatus());
                            item.setNhpsId(responseItem.getNhpsId());
                            item.setSyncDt(responseItem.getSyncDt());
                            item.setError_msg(null);
                            item.setError_type(null);
                            item.setError_code(null);
                            SeccDatabase.updateHouseHold(item, context);
                            sendBroadcastStatus(broadcastIndex);

                        }
                    } else {
                        item.setError_msg(responseItem.getErrorMessage());
                        item.setError_code(responseItem.getErrorCode());
                        item.setError_type(AppConstant.SYNCING_ERROR);
                        AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Household sync error " + item.getError_code());
                        SeccDatabase.updateHouseHold(item, context);
                        sendBroadcastStatus(broadcastIndex);
                    }
                }
            } catch (Exception e) {
                item.setError_msg(AppConstant.INTERNET_ERROR_MSG);
                item.setError_code(AppConstant.INTERNET_ERROR_CODE);
                item.setError_type(AppConstant.SYNCING_ERROR);
                //   AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Household sync error "+item.getError_code());
                SeccDatabase.updateHouseHold(item, context);
                sendBroadcastStatus(broadcastIndex);
            }
        } else {
            item.setError_msg(AppConstant.SYNCING_MEMBER_ERROR);
            item.setError_type(AppConstant.SYNCING_ERROR);
            item.setError_code(AppConstant.SYNCING_MEMBER_ERROR);
            //    AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Household sync error "+item.getError_code());
            SeccDatabase.updateHouseHold(item, context);
            sendBroadcastStatus(broadcastIndex);
        }
    }

    //}
    private void sendBroadcastStatus(int index) {
        if (index == pendingHouseholdList.size() - 1) {
            AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Sending validated data..3 " + index);
            broadCastIntent.putExtra(RESPONSE_SUCCESS, SYNC_COMPLETE);
            sendBroadcast(broadCastIntent);
        } else if (index == AppConstant.cancelSyncService) {
            broadCastIntent.putExtra(RESPONSE_SUCCESS, AppConstant.cancelSyncMsg);
            sendBroadcast(broadCastIntent);
        } else {
            AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Sending validated data..4 " + index);
            broadCastIntent.putExtra(RESPONSE_SUCCESS, "x");
            sendBroadcast(broadCastIntent);
        }
    }

    private void syncHouseHoldFound(HouseHoldItem item) {
        SeccMemberItem newHeadOftheFamily = null;
        SeccMemberItem oldHeadOftheFamily = null;
        SeccMemberItem seccHeadOftheFamily = null;
        ArrayList<SeccMemberItem> allMemberList = new ArrayList<>();
        ArrayList<SeccMemberItem> memberList = new ArrayList<>();
        if (item != null && item.getDataSource() != null && item.getDataSource().trim().equalsIgnoreCase(AppConstant.RSBY_SOURCE)) {
            allMemberList = SeccDatabase.getRsbyMemberListWithUrn(item.getRsbyUrnId(), context);
            if (allMemberList != null) {
                for (SeccMemberItem item1 : allMemberList) {
               /* if(item1.getNhpsRelationCode()!=null && item1.getNhpsRelationCode().trim().equalsIgnoreCase(AppConstant.NEW_HEAD_RELATION_CODE)){
                    newHeadOftheFamily=item1;
                }*//*else if(item1.getNhpsMemId()!=null && item1.getNhpsMemId().trim().equalsIgnoreCase(item.getNhpsMemId().trim())){
                    oldHeadOftheFamily=item1;
                }*//*else{
                    memberList.add(item1);
                }*/
                    stopService();

                    if (item1.getRsbyMemId() != null && item1.getRsbyMemId().trim().
                            equalsIgnoreCase(item.getRsbyMemId().trim()) && item1.getNhpsRelationCode() != null &&
                            item1.getNhpsRelationCode().trim().equalsIgnoreCase(AppConstant.NEW_HEAD_RELATION_CODE)) {
                        oldHeadOftheFamily = item1;
                    } else if (item1.getNhpsRelationCode() != null &&
                            item1.getNhpsRelationCode().trim().equalsIgnoreCase(AppConstant.NEW_HEAD_RELATION_CODE)
                            && !item1.getRsbyMemId().trim().equalsIgnoreCase(item.getRsbyMemId().trim())) {
                        newHeadOftheFamily = item1;
                    } else if (item1.getRsbyMemId() != null && item1.getRsbyMemId().trim()
                            .equalsIgnoreCase(item.getRsbyMemId().trim())) {
                        if (!item1.getMemStatus().trim().equalsIgnoreCase(AppConstant.MEMBERFOUND_AND_PRESENT) ||
                                !item1.getMemStatus().equalsIgnoreCase(AppConstant.MEMBERFOUND_BUT_NOT_PRESENT)) {
                            oldHeadOftheFamily = item1;
                        }
                    } else {
                        memberList.add(item1);
                    }

                }
            }
        } else {
            allMemberList = SeccDatabase.getSeccMemberList(item.getHhdNo(), context);
            if (allMemberList != null) {
                for (SeccMemberItem item1 : allMemberList) {
               /* if(item1.getNhpsRelationCode()!=null && item1.getNhpsRelationCode().trim().equalsIgnoreCase(AppConstant.NEW_HEAD_RELATION_CODE)){
                    newHeadOftheFamily=item1;
                }*//*else if(item1.getNhpsMemId()!=null && item1.getNhpsMemId().trim().equalsIgnoreCase(item.getNhpsMemId().trim())){
                    oldHeadOftheFamily=item1;
                }*//*else{
                    memberList.add(item1);
                }*/
                    stopService();

                    if (item1.getNhpsMemId() != null && item1.getNhpsMemId().trim().
                            equalsIgnoreCase(item.getNhpsMemId().trim()) && item1.getNhpsRelationCode() != null &&
                            item1.getNhpsRelationCode().trim().equalsIgnoreCase(AppConstant.NEW_HEAD_RELATION_CODE)) {
                        oldHeadOftheFamily = item1;
                    } else if (item1.getNhpsRelationCode() != null &&
                            item1.getNhpsRelationCode().trim().equalsIgnoreCase(AppConstant.NEW_HEAD_RELATION_CODE)
                            && !item1.getNhpsMemId().trim().equalsIgnoreCase(item.getNhpsMemId().trim())) {
                        newHeadOftheFamily = item1;
                    } else if (item1.getNhpsMemId() != null && item1.getNhpsMemId().trim()
                            .equalsIgnoreCase(item.getNhpsMemId().trim())) {
                        if (!item1.getMemStatus().trim().equalsIgnoreCase(AppConstant.MEMBERFOUND_AND_PRESENT) ||
                                !item1.getMemStatus().equalsIgnoreCase(AppConstant.MEMBERFOUND_BUT_NOT_PRESENT)) {
                            oldHeadOftheFamily = item1;
                        }
                    } else {
                        memberList.add(item1);
                    }

                }
            }
        }


        /*if(newHeadOftheFamily!=null){
            if(newHeadOftheFamily.getSyncedStatus()!=null && newHeadOftheFamily.getSyncedStatus()
                    .trim().equalsIgnoreCase(AppConstant.SYNCED_STATUS)){
                    syncMemberExceptHeadoftheFamily(memberList,item);
            }else{
                if(syncHeadOftheFamily(newHeadOftheFamily)){
                    syncMemberExceptHeadoftheFamily(memberList,item);
                }else{
                    sendBroadcastStatus(broadcastIndex);
                }
            }
        }*/

        if (oldHeadOftheFamily != null && newHeadOftheFamily != null) {
            AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "OLD HEAD " + oldHeadOftheFamily.getSyncedStatus());
            if (oldHeadOftheFamily.getSyncedStatus() != null && oldHeadOftheFamily.getSyncedStatus().trim().
                    equalsIgnoreCase(AppConstant.SYNCED_STATUS)) {
                AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "OLD HEAD " + oldHeadOftheFamily.getSyncedStatus());
                if (newHeadOftheFamily.getSyncedStatus() != null &&
                        newHeadOftheFamily.getSyncedStatus().trim().equalsIgnoreCase(AppConstant.SYNCED_STATUS)) {
                    syncMemberExceptHeadoftheFamily(memberList, item);
                } else {
                    if (syncHeadOftheFamily(newHeadOftheFamily)) {
                        syncMemberExceptHeadoftheFamily(memberList, item);
                    } else {
                        sendBroadcastStatus(broadcastIndex);
                    }
                }
            } else {
                AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "OLD HEAD " + oldHeadOftheFamily.getSyncedStatus());
                if (syncHeadOftheFamily(oldHeadOftheFamily)) {
                    if (newHeadOftheFamily.getSyncedStatus() != null &&
                            newHeadOftheFamily.getSyncedStatus().trim().equalsIgnoreCase(AppConstant.SYNCED_STATUS)) {

                    } else {
                        if (syncHeadOftheFamily(newHeadOftheFamily)) {
                            syncMemberExceptHeadoftheFamily(memberList, item);
                        } else {
                            sendBroadcastStatus(broadcastIndex);
                        }
                    }
                } else {
                    sendBroadcastStatus(broadcastIndex);
                }
            }
        } else if (oldHeadOftheFamily != null && newHeadOftheFamily == null) {
            if (oldHeadOftheFamily.getSyncedStatus() != null &&
                    oldHeadOftheFamily.getSyncedStatus().trim().equalsIgnoreCase(AppConstant.SYNCED_STATUS)) {

            } else {
                if (syncHeadOftheFamily(oldHeadOftheFamily)) {
                    syncMemberExceptHeadoftheFamily(memberList, item);
                } else {
                    sendBroadcastStatus(broadcastIndex);
                }
            }
                /*if(syncHeadOftheFamily(oldHeadOftheFamily)){
                   // syncMemberExceptHeadoftheFamily(memberList,item);
                }*/
        }

    }

    private boolean syncHeadOftheFamily(SeccMemberItem item1) {
        if (item1 != null && item1.getDataSource() != null && item1.getDataSource().trim().equalsIgnoreCase(AppConstant.RSBY_SOURCE)) {
            return sendRsbyHof(item1);
        } else {
            return sendSeccHof(item1);
        }
    }

    private void syncMemberExceptHeadoftheFamily(ArrayList<SeccMemberItem> memberList, HouseHoldItem houseHoldItem) {
        if (memberList != null) {
            for (SeccMemberItem item1 : memberList) {
                if (item1.getSyncedStatus() != null && item1.getSyncedStatus().trim().equalsIgnoreCase(AppConstant.SYNCED_STATUS)) {

                } else {
                    if (item1 != null && item1.getDataSource() != null && item1.getDataSource().trim().equalsIgnoreCase(AppConstant.RSBY_SOURCE)) {
                        sendExceptHeadRsbyMember(item1);
                    } else {
                        sendExceptHeadSeccMember(item1);
                    }
                }
            }
        }

        syncHouseholdItem(houseHoldItem);
    }

    private void syncExceptHouseholdFound(HouseHoldItem houseHoldItem) {
        stopService();
        if (houseHoldItem != null && houseHoldItem.getDataSource() != null && houseHoldItem.getDataSource().trim().equalsIgnoreCase(AppConstant.RSBY_SOURCE)) {
            sendExceptHouseholdFoundRsby(houseHoldItem);
        } else {
            sendExcptHouseholdFoundSecc(houseHoldItem);
        }

    }

    private void syncHouseholdItem(HouseHoldItem item) {
        if (item != null && item.getDataSource() != null && item.getDataSource().trim().equalsIgnoreCase(AppConstant.RSBY_SOURCE)) {
            sendRsbyHouseHold(item);
        } else {
            sendSeccHousehold(item);
        }
    }

    private void stopService() {
        try {
            if (shouldContinue == false) {

                sendBroadcastStatus(AppConstant.cancelSyncService);
                stopSelf();
                // unregisterReceiver(broadCastIntent);
                shouldContinue = true;
                return;
            }
        } catch (Exception ex) {

        }
    }

    private void findDuplicateRsbyHouseHold(HouseHoldItem item, int duplicateCount) {
        ArrayList<SeccMemberItem> memberList = SeccDatabase.getRsbyMemberListWithUrn(item.getRsbyUrnId(), context);
        stopService();
        for (SeccMemberItem item1 : memberList) {
            if (item1.getAadhaarAuth() != null && item1.getAadhaarAuth().trim().equalsIgnoreCase(AppConstant.INVALID_STATUS)) {
                duplicateCount++;
                stopService();
            } else if (item1.getAadhaarAuth() != null && item1.getAadhaarAuth().trim().equalsIgnoreCase(AppConstant.PENDING_STATUS)) {
                duplicateCount++;
                stopService();
            } else if (item1.getAadhaarNo() != null && !item1.getAadhaarNo().equalsIgnoreCase("") && item1.getAadhaarNo().trim().equalsIgnoreCase(loginResponse.getAadhaarNumber().trim())) {
                item1.setError_code(AppConstant.VERIFIER_AADHAAR_ALLOCATED);
                item1.setError_type(AppConstant.SYNCING_ERROR);
                item1.setError_msg(AppConstant.VERIFIER_AADHAAR_ALLOCATED_MSG);
                SeccDatabase.updateRsbyMember(item1, context);
                duplicateCount++;
                stopService();
            } else if (item1.getAadhaarNo() != null && !item1.getAadhaarNo().equalsIgnoreCase("")) {
                stopService();
                ArrayList<SeccMemberItem> list1 = SeccDatabase.seccMemberWithAadhaarLocked(item1.getAadhaarNo(), AppConstant.LOCKED + "", context);
                for (SeccMemberItem item2 : list1) {
                   /* if (!item2.getRsbyMemId().trim().equalsIgnoreCase(item1.getRsbyMemId().trim())) {
                        HouseHoldItem houseHoldItem = SeccDatabase.getRsbyHouseHoldByUrn(item2.getRsbyUrnId(), context);
                       // AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Sending validated data..8"+item2.getName());
                        if (houseHoldItem.getLockSave() != null && houseHoldItem.getLockSave()
                                .equalsIgnoreCase(AppConstant.LOCKED + "")) {
                           // AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Sending validated data..9"+item2.getName());
                            item1.setError_code(AppConstant.AADHAAR_ALREADY_ALLOCATED);
                            item1.setError_type(AppConstant.SYNCING_ERROR);
                            item1.setError_msg(AppConstant.AADHAAR_ALREADY_ALLOCATED_MSG);
                            SeccDatabase.updateRsbyMember(item1, context);
                            duplicateCount++;
                        }
                    }*/

//                    if (item2 != null && item2.getDataSource() != null && item2.getDataSource().trim().equalsIgnoreCase(AppConstant.RSBY_SOURCE)) {
//                        // if (!item2.getNhpsMemId().trim().equalsIgnoreCase(item1.getNhpsMemId().trim())) {
//                        if (!item2.getRsbyMemId().trim().equalsIgnoreCase(item1.getRsbyMemId().trim())) {
//                            HouseHoldItem houseHoldItem = SeccDatabase.getRsbyHouseHoldByUrn(item2.getRsbyUrnId(), context);
//                            // AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Sending validated data..8"+item2.getName());
//                            if (houseHoldItem.getLockSave() != null && houseHoldItem.getLockSave()
//                                    .equalsIgnoreCase(AppConstant.LOCKED + "")) {
//                                // AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Sending validated data..9"+item2.getName());
//                                item1.setError_code(AppConstant.AADHAAR_ALREADY_ALLOCATED);
//                                item1.setError_type(AppConstant.SYNCING_ERROR);
//                                item1.setError_msg(AppConstant.AADHAAR_ALREADY_ALLOCATED_MSG);
//                                SeccDatabase.updateRsbyMember(item1, context);
//                                duplicateCount++;
//                            }
//                        }

                    //modified by rajesh kumar verma


                    if (!item2.getNhpsMemId().trim().equalsIgnoreCase(item1.getNhpsMemId().trim())) {
                        if (item2.getDataSource().trim().equalsIgnoreCase(AppConstant.RSBY_SOURCE)) {
                            HouseHoldItem houseHoldItem = SeccDatabase.getRsbyHouseHoldByUrn(item2.getRsbyUrnId(), context);
                            // AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Sending validated data..8"+item2.getName());
                            if (houseHoldItem.getLockSave() != null && houseHoldItem.getLockSave()
                                    .equalsIgnoreCase(AppConstant.LOCKED + "")) {
                                // AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Sending validated data..9"+item2.getName());
                                item1.setError_code(AppConstant.AADHAAR_ALREADY_ALLOCATED);
                                item1.setError_type(AppConstant.SYNCING_ERROR);
                                item1.setError_msg(AppConstant.AADHAAR_ALREADY_ALLOCATED_MSG);
                                SeccDatabase.updateSeccMember(item1, context);
                                duplicateCount++;
                            }
                        }

                        if (item2.getName().equalsIgnoreCase(item1.getName()) && item2.getDob().equalsIgnoreCase(item1.getDob()) && item2.getHhdNo().equalsIgnoreCase(item1.getHhdNo())) {
                            Log.e(TAG,"++++++++>>>>>>>>>>>>>>>  Same member RSBY list match with whole list");
                        } else {

                            if (item2.getAadhaarNo().trim().equalsIgnoreCase(item1.getAadhaarNo().trim())) {
//                            HouseHoldItem houseHoldItem = SeccDatabase.getRsbyHouseHoldByUrn(item2.getRsbyUrnId(), context);
                                // AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Sending validated data..8"+item2.getName());
//                            if (houseHoldItem.getLockSave() != null && houseHoldItem.getLockSave()
//                                    .equalsIgnoreCase(AppConstant.LOCKED + "")) {
                                // AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Sending validated data..9"+item2.getName());
                                item1.setError_code(AppConstant.AADHAAR_ALREADY_ALLOCATED);
                                item1.setError_type(AppConstant.SYNCING_ERROR);
                                item1.setError_msg(AppConstant.AADHAAR_ALREADY_ALLOCATED_MSG);
                                SeccDatabase.updateSeccMember(item1, context);
                                duplicateCount++;
//                            }
                            }
                        }
                        //  }
                    } else {
                        HouseHoldItem houseHoldItem = SeccDatabase.getHouseHoldList(item2.getHhdNo(), context);
                        AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Sending validated data..8" + item2.getName());
                        if (houseHoldItem.getLockSave() != null && houseHoldItem.getLockSave()
                                .equalsIgnoreCase(AppConstant.LOCKED + "")) {
                            AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Sending validated data..9" + item2.getName());
                            item1.setError_code(AppConstant.AADHAAR_ALREADY_ALLOCATED);
                            item1.setError_type(AppConstant.SYNCING_ERROR);
                            item1.setError_msg(AppConstant.AADHAAR_ALREADY_ALLOCATED_MSG);
                            SeccDatabase.updateRsbyMember(item1, context);
                            duplicateCount++;
                        }
                    }

                }


                //HGHJJ
              /*if(list1!=null && list1.size()>1)  {
                  item1.setError_code(AppConstant.AADHAAR_ALREADY_ALLOCATED);
                  item1.setError_type(AppConstant.SYNCING_ERROR);
                  item1.setError_msg(AppConstant.AADHAAR_ALREADY_ALLOCATED_MSG);
                  SeccDatabase.updateSeccMember(item1,context);
                  duplicateCount++;
              }*/
            }


        }
        if (duplicateCount > 0) {
            item.setError_msg(AppConstant.SYNCING_MEMBER_ERROR);
            item.setError_type(AppConstant.SYNCING_ERROR);
            item.setError_code(AppConstant.SYNCING_MEMBER_ERROR);
            //    AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Household sync error "+item.getError_code());
            SeccDatabase.updateRsbyHousehold(item, context);
            // pendingHouseholdList.remove(item);
            sendBroadcastStatus(broadcastIndex);
            broadcastIndex++;
            AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Sending validated data..1" + broadcastIndex);
        } else {
            AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Sending validated data..2" + broadcastIndex);
            pureList.add(item);
            //broadcastIndex++;
        }
    }

    private void findDuplicateSeccHousehold(HouseHoldItem item, int duplicateCount) {
        ArrayList<SeccMemberItem> memberList = SeccDatabase.getSeccMemberList(item.getHhdNo(), context);
        stopService();
        for (SeccMemberItem item1 : memberList) {
            if (item1.getAadhaarAuth() != null && item1.getAadhaarAuth().trim().equalsIgnoreCase(AppConstant.INVALID_STATUS)) {
                duplicateCount++;
                stopService();
            } else if (item1.getAadhaarAuth() != null && item1.getAadhaarAuth().trim().equalsIgnoreCase(AppConstant.PENDING_STATUS)) {
                duplicateCount++;
                stopService();
            } else if (item1.getAadhaarNo() != null && !item1.getAadhaarNo().equalsIgnoreCase("") && item1.getAadhaarNo().trim().equalsIgnoreCase(loginResponse.getAadhaarNumber().trim())) {
                item1.setError_code(AppConstant.VERIFIER_AADHAAR_ALLOCATED);
                item1.setError_type(AppConstant.SYNCING_ERROR);
                item1.setError_msg(AppConstant.VERIFIER_AADHAAR_ALLOCATED_MSG);
                SeccDatabase.updateSeccMember(item1, context);
                duplicateCount++;
                stopService();
            } else if (item1.getAadhaarNo() != null && !item1.getAadhaarNo().equalsIgnoreCase("")) {
                stopService();
                ArrayList<SeccMemberItem> list1 = SeccDatabase.seccMemberWithAadhaarLocked(item1.getAadhaarNo(), AppConstant.LOCKED + "", context);
                for (SeccMemberItem item2 : list1) {
                    if (item2 != null && item2.getDataSource() != null) {


                        // if (!item2.getNhpsMemId().trim().equalsIgnoreCase(item1.getNhpsMemId().trim())) {
//                        HouseHoldItem houseHoldItem = SeccDatabase.getRsbyHouseHoldByUrn(item2.getRsbyUrnId(), context);
                        // AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Sending validated data..8"+item2.getName());
/*                        if (houseHoldItem.getLockSave() != null && houseHoldItem.getLockSave()
                                .equalsIgnoreCase(AppConstant.LOCKED + "")) {
                            // AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Sending validated data..9"+item2.getName());
                            item1.setError_code(AppConstant.AADHAAR_ALREADY_ALLOCATED);
                            item1.setError_type(AppConstant.SYNCING_ERROR);
                            item1.setError_msg(AppConstant.AADHAAR_ALREADY_ALLOCATED_MSG);
                            SeccDatabase.updateSeccMember(item1, context);
                            duplicateCount++;
                        }*/

                        //modified by rajesh kumar verma


                        // if (!item2.getNhpsMemId().trim().equalsIgnoreCase(item1.getNhpsMemId().trim())) {
                        if (item2.getDataSource().trim().equalsIgnoreCase(AppConstant.RSBY_SOURCE)) {
                            HouseHoldItem houseHoldItem = SeccDatabase.getRsbyHouseHoldByUrn(item2.getRsbyUrnId(), context);
                            // AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Sending validated data..8"+item2.getName());
                            if (houseHoldItem.getLockSave() != null && houseHoldItem.getLockSave()
                                    .equalsIgnoreCase(AppConstant.LOCKED + "")) {
                                // AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Sending validated data..9"+item2.getName());
                                item1.setError_code(AppConstant.AADHAAR_ALREADY_ALLOCATED);
                                item1.setError_type(AppConstant.SYNCING_ERROR);
                                item1.setError_msg(AppConstant.AADHAAR_ALREADY_ALLOCATED_MSG);
                                SeccDatabase.updateSeccMember(item1, context);
                                duplicateCount++;
                            }
                        }

                        if (item2.getName().equalsIgnoreCase(item1.getName()) && item2.getDob().equalsIgnoreCase(item1.getDob()) && item2.getHhdNo().equalsIgnoreCase(item1.getHhdNo())) {

                            Log.e(TAG,"++++++++>>>>>>>>>>>>>>>  Same member list match with whole list");
                        } else {

                            if (item2.getAadhaarNo().trim().equalsIgnoreCase(item1.getAadhaarNo().trim())) {
//                            HouseHoldItem houseHoldItem = SeccDatabase.getRsbyHouseHoldByUrn(item2.getRsbyUrnId(), context);
                                // AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Sending validated data..8"+item2.getName());
//                            if (houseHoldItem.getLockSave() != null && houseHoldItem.getLockSave()
//                                    .equalsIgnoreCase(AppConstant.LOCKED + "")) {
                                // AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Sending validated data..9"+item2.getName());
                                item1.setError_code(AppConstant.AADHAAR_ALREADY_ALLOCATED);
                                item1.setError_type(AppConstant.SYNCING_ERROR);
                                item1.setError_msg(AppConstant.AADHAAR_ALREADY_ALLOCATED_MSG);
                                SeccDatabase.updateSeccMember(item1, context);
                                duplicateCount++;
//                            }
                            }
                        }

                        //  }
                    } else {
                        if (!item2.getNhpsMemId().trim().equalsIgnoreCase(item1.getNhpsMemId().trim())) {
                            HouseHoldItem houseHoldItem = SeccDatabase.getHouseHoldList(item2.getHhdNo(), context);
                            AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Sending validated data..8" + item2.getName());
                            if (houseHoldItem.getLockSave() != null && houseHoldItem.getLockSave()
                                    .equalsIgnoreCase(AppConstant.LOCKED + "")) {
                                AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Sending validated data..9" + item2.getName());
                                item1.setError_code(AppConstant.AADHAAR_ALREADY_ALLOCATED);
                                item1.setError_type(AppConstant.SYNCING_ERROR);
                                item1.setError_msg(AppConstant.AADHAAR_ALREADY_ALLOCATED_MSG);
                                SeccDatabase.updateSeccMember(item1, context);
                                duplicateCount++;
                            }
                        }
                    }
                }
              /*if(list1!=null && list1.size()>1)  {
                  item1.setError_code(AppConstant.AADHAAR_ALREADY_ALLOCATED);
                  item1.setError_type(AppConstant.SYNCING_ERROR);
                  item1.setError_msg(AppConstant.AADHAAR_ALREADY_ALLOCATED_MSG);
                  SeccDatabase.updateSeccMember(item1,context);
                  duplicateCount++;
              }*/
            }

        }
        if (duplicateCount > 0) {
            item.setError_msg(AppConstant.SYNCING_MEMBER_ERROR);
            item.setError_type(AppConstant.SYNCING_ERROR);
            item.setError_code(AppConstant.SYNCING_MEMBER_ERROR);
            //    AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Household sync error "+item.getError_code());
            SeccDatabase.updateHouseHold(item, context);
            // pendingHouseholdList.remove(item);
            sendBroadcastStatus(broadcastIndex);
            broadcastIndex++;
            AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Sending validated data..1" + broadcastIndex);
        } else {
            AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Sending validated data..2" + broadcastIndex);
            pureList.add(item);
            //broadcastIndex++;
        }
    }

    private void sendExceptHouseholdFoundRsby(HouseHoldItem houseHoldItem) {
        ArrayList<SeccMemberItem> memberList = SeccDatabase.getRsbyMemberListWithUrn(houseHoldItem.getRsbyUrnId(), context);
        for (SeccMemberItem item1 : memberList) {
            if (item1.getSyncedStatus() != null && item1.getSyncedStatus().trim().equalsIgnoreCase(AppConstant.SYNCED_STATUS)) {

            } else {
                RsbyMemberSyncRequest preparedMemberItem = SyncUtility.prepareRsbyMemberItemForSync(item1, context);
                preparedMemberItem.setAppVersion(AppUtility.getCurrentApplicationVersion(context));
                try {
                    HashMap<String, String> response = CustomHttp.httpPost(AppConstant.SYNC_RSBY_MEMBER, preparedMemberItem.serialize());
                    if (response != null) {
                        String strResp = response.get(AppConstant.RESPONSE_BODY);
                        AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Sync Member response : " + strResp);
                        RsbyMemberSyncResponse responsItem = RsbyMemberSyncResponse.create(strResp);
                        if (responsItem != null) {
                            if (responsItem != null && responsItem.isStatus()) {
                                if (responsItem.getSyncedStatus().equalsIgnoreCase(AppConstant.SYNCED_STATUS)) {
                                    item1.setSyncedStatus(responsItem.getSyncedStatus());
                                    item1.setNhpsId(responsItem.getNhpsId());
                                    item1.setSyncDt(responsItem.getSyncdt());
                                    item1.setAppVersion(responsItem.getAppVersion());
                                    item1.setError_code(null);
                                    item1.setError_type(null);
                                    item1.setError_msg(null);
                                    SeccDatabase.updateRsbyMember(item1, context);
                                }
                            } else {
                                AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Inside Secc Sync status : internet issues ");
                                if (responsItem.getErrorCode().equalsIgnoreCase(AppConstant.AADHAAR_ALREADY_ALLOCATED)) {
                                    item1.setError_code(responsItem.getErrorCode());
                                    item1.setError_type(AppConstant.SYNCING_ERROR);
                                    item1.setError_msg(responsItem.getErrorMessage());
                                    SeccDatabase.updateRsbyMember(item1, context);
                                } else if (responsItem.getErrorCode().equalsIgnoreCase(AppConstant.URI_ALREADY_EXIST)) {
                                    item1.setError_code(responsItem.getErrorCode());
                                    item1.setError_type(AppConstant.SYNCING_ERROR);
                                    item1.setError_msg(responsItem.getErrorMessage());
                                    SeccDatabase.updateRsbyMember(item1, context);
                                }
                            }
                        } else {
                            AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Inside Secc Sync status : internet issues ");
                            item1.setError_code(AppConstant.INTERNET_ERROR_CODE);
                            item1.setError_type(AppConstant.SYNCING_ERROR);
                            item1.setError_msg(AppConstant.INTERNET_ERROR_MSG);
                            SeccDatabase.updateRsbyMember(item1, context);

                        }
                    }
                } catch (Exception e) {
                    item1.setError_code(AppConstant.INTERNET_ERROR_CODE);
                    item1.setError_type(AppConstant.SYNCING_ERROR);
                    item1.setError_msg(AppConstant.INTERNET_ERROR_MSG);
                    SeccDatabase.updateRsbyMember(item1, context);
                }
            }
        }
        syncHouseholdItem(houseHoldItem);
    }

    private void sendExcptHouseholdFoundSecc(HouseHoldItem houseHoldItem) {
        ArrayList<SeccMemberItem> memberList = SeccDatabase.getSeccMemberList(houseHoldItem.getHhdNo(), context);
        for (SeccMemberItem item1 : memberList) {
            if (item1.getSyncedStatus() != null && item1.getSyncedStatus().trim().equalsIgnoreCase(AppConstant.SYNCED_STATUS)) {

            } else {
                SeccMemberItem preparedMemberItem = SyncUtility.prepareMemberItemForSync(item1, context);
                preparedMemberItem.setAppVersion(AppUtility.getCurrentApplicationVersion(context));
                AppUtility.showLog(AppConstant.LOG_STATUS, TAG, " Prepared Item : " + preparedMemberItem.serialize());
                AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Sync Member ahl tin :" +
                        " " + item1.getAhlTin() + " Aaadhar Auth Status :" + preparedMemberItem.getAadhaarAuth() + "" +
                        " Aaadhar Capturing mode : " + preparedMemberItem.getAadhaarCapturingMode() + "" +
                        " Govt Id type : " + preparedMemberItem.getIdType() + " Data Source : " + preparedMemberItem.getDataSource());
                try {
                    String[] header = new String[2];

                    header[0] = AppConstant.AUTHORIZATION;
                    header[1] = AppConstant.AUTHORIZATIONVALUE;

                    HashMap<String, String> response = CustomHttp.httpPostWithHeader(AppConstant.SYNC_MEMBER_API, preparedMemberItem.serialize(), header);
                    String payLoad = preparedMemberItem.serialize().toString();
                    AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Sync Member payLoad : " + payLoad);
                    System.out.print(payLoad);
                    if (response != null) {
                        AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Sync Member request : " + preparedMemberItem.serialize());
                        String strResp = response.get(AppConstant.RESPONSE_BODY);
                        AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Sync Member response : " + strResp);
                        SyncSeccMemberResponse responsItem = SyncSeccMemberResponse.create(strResp);
                        if (responsItem != null) {
                            if (responsItem != null && responsItem.isStatus()) {
                                if (responsItem.getSyncedStatus().equalsIgnoreCase(AppConstant.SYNCED_STATUS)) {
                                    item1.setSyncedStatus(responsItem.getSyncedStatus());
                                    //  item1.setNhpsId(responsItem.getNhpsId());
                                    item1.setSyncDt(responsItem.getSyncDt());
                                    item1.setNhpsMemId(responsItem.getNhpsMemId());
                                    //   item1.setAppVersion(responsItem.getAppVersion());
                                    item1.setError_code(null);
                                    item1.setError_type(null);
                                    item1.setError_msg(null);
                                    SeccDatabase.updateSeccMember(item1, context);
                                }
                            } else {
                                AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Inside Secc Sync status : internet issues ");
                                if (responsItem.getErrorCode().equalsIgnoreCase(AppConstant.AADHAAR_ALREADY_ALLOCATED)) {
                                    item1.setError_code(responsItem.getErrorCode());
                                    item1.setError_type(AppConstant.SYNCING_ERROR);
                                    item1.setError_msg(responsItem.getErrorMessage());
                                    SeccDatabase.updateSeccMember(item1, context);
                                }
                            }
                        } else {
                            AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Inside Secc Sync status : internet issues ");
                            item1.setError_code(AppConstant.INTERNET_ERROR_CODE);
                            item1.setError_type(AppConstant.SYNCING_ERROR);
                            item1.setError_msg(AppConstant.INTERNET_ERROR_MSG);
                            SeccDatabase.updateSeccMember(item1, context);

                        }
                    }
                } catch (Exception e) {
                    item1.setError_code(AppConstant.INTERNET_ERROR_CODE);
                    item1.setError_type(AppConstant.SYNCING_ERROR);
                    item1.setError_msg(AppConstant.INTERNET_ERROR_MSG);
                    SeccDatabase.updateSeccMember(item1, context);
                }
            }
        }
        syncHouseholdItem(houseHoldItem);
    }

    private boolean sendRsbyHof(SeccMemberItem item1) {
        RsbyMemberSyncRequest preparedMemberItem = SyncUtility.prepareRsbyMemberItemForSync(item1, context);
        preparedMemberItem.setAppVersion(AppUtility.getCurrentApplicationVersion(context));
        String payLoad = preparedMemberItem.serialize();
        System.out.print(payLoad);
        String[] header = new String[2];

        header[0] = AppConstant.AUTHORIZATION;
        header[1] = AppConstant.AUTHORIZATIONVALUE;
        try {
            HashMap<String, String> response = CustomHttp.httpPost(AppConstant.SYNC_RSBY_MEMBER, preparedMemberItem.serialize(), header);
            if (response != null) {
                String strResp = response.get(AppConstant.RESPONSE_BODY);
                AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Sync Member response : " + strResp);
                RsbyMemberSyncResponse responsItem = RsbyMemberSyncResponse.create(strResp);
                if (responsItem != null) {
                    if (responsItem != null && responsItem.isStatus()) {
                        if (responsItem.getSyncedStatus().equalsIgnoreCase(AppConstant.SYNCED_STATUS)) {
                            item1.setSyncedStatus(responsItem.getSyncedStatus());
                            //      item1.setNhpsId(responsItem.getNhpsId());
                            item1.setSyncDt(responsItem.getSyncdt());
                            item1.setAppVersion(responsItem.getAppVersion());
                            item1.setError_code(null);
                            item1.setError_type(null);
                            item1.setError_msg(null);
                            SeccDatabase.updateRsbyMember(item1, context);
                            return true;
                        }
                    } else {
                        AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Inside Secc Sync status : internet issues ");
                        if (responsItem.getErrorCode().equalsIgnoreCase(AppConstant.AADHAAR_ALREADY_ALLOCATED)) {
                            item1.setError_code(responsItem.getErrorCode());
                            item1.setError_type(AppConstant.SYNCING_ERROR);
                            item1.setError_msg(responsItem.getErrorMessage());
                            SeccDatabase.updateRsbyMember(item1, context);
                        } else if (responsItem.getErrorCode().equalsIgnoreCase(AppConstant.URI_ALREADY_EXIST)) {
                            item1.setError_code(responsItem.getErrorCode());
                            item1.setError_type(AppConstant.SYNCING_ERROR);
                            item1.setError_msg(responsItem.getErrorMessage());
                            SeccDatabase.updateRsbyMember(item1, context);
                            HouseHoldItem houseHold = SeccDatabase.getRsbyHouseHoldList(item1.getRsbyUrnId(), context);
                            if (houseHold != null) {
                                houseHold.setError_code(responsItem.getErrorCode());
                                houseHold.setError_type(AppConstant.SYNCING_ERROR);
                                houseHold.setError_msg(responsItem.getErrorMessage());
                                SeccDatabase.updateRsbyHousehold(houseHold, context);
                            }
                        }
                    }
                } else {
                    AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Inside Secc Sync status : internet issues ");
                    item1.setError_code(AppConstant.INTERNET_ERROR_CODE);
                    item1.setError_type(AppConstant.SYNCING_ERROR);
                    item1.setError_msg(AppConstant.INTERNET_ERROR_MSG);
                    SeccDatabase.updateRsbyMember(item1, context);

                }
            }
        } catch (Exception e) {
            item1.setError_code(AppConstant.INTERNET_ERROR_CODE);
            item1.setError_type(AppConstant.SYNCING_ERROR);
            item1.setError_msg(AppConstant.INTERNET_ERROR_MSG);
            SeccDatabase.updateRsbyMember(item1, context);
        }
        return false;
    }

    private boolean sendSeccHof(SeccMemberItem item1) {
        SeccMemberItem preparedMemberItem = SyncUtility.prepareMemberItemForSync(item1, context);
        preparedMemberItem.setAppVersion(AppUtility.getCurrentApplicationVersion(context));
        try {
            String[] header = new String[2];

            header[0] = AppConstant.AUTHORIZATION;
            header[1] = AppConstant.AUTHORIZATIONVALUE;
            HashMap<String, String> response = CustomHttp.httpPostWithHeader(AppConstant.SYNC_MEMBER_API, preparedMemberItem.serialize(), header);
            String payLoad = preparedMemberItem.serialize().toString();
            AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Sync Member payLoad : " + payLoad);
            System.out.print(payLoad);
            if (response != null) {
                AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Sync Member request : " + preparedMemberItem.serialize());
                String strResp = response.get(AppConstant.RESPONSE_BODY);
                System.out.print(payLoad);
                AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Sync Member response : " + strResp);
                SyncSeccMemberResponse responsItem = SyncSeccMemberResponse.create(strResp);
                if (responsItem != null) {
                    if (responsItem != null && responsItem.isStatus()) {
                        if (responsItem.getSyncedStatus().equalsIgnoreCase(AppConstant.SYNCED_STATUS)) {
                            item1.setSyncedStatus(responsItem.getSyncedStatus());
                            //item1.setNhpsId(responsItem.getNhpsId());
                            item1.setSyncDt(responsItem.getSyncDt());
                            // item1.setAppVersion(responsItem.getAppVersion());
                            item1.setNhpsMemId(responsItem.getNhpsMemId());
                            item1.setError_code(null);
                            item1.setError_type(null);
                            item1.setError_msg(null);
                            SeccDatabase.updateSeccMember(item1, context);
                            return true;
                        }
                    } else {
                        AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Inside Secc Sync status : internet issues ");
                        if (responsItem.getErrorCode().equalsIgnoreCase(AppConstant.AADHAAR_ALREADY_ALLOCATED)) {
                            item1.setError_code(responsItem.getErrorCode());
                            item1.setError_type(AppConstant.SYNCING_ERROR);
                            item1.setError_msg(responsItem.getErrorMessage());
                            SeccDatabase.updateSeccMember(item1, context);
                            HouseHoldItem houseHold = SeccDatabase.getHouseHoldDetailsByHhdNo(item1.getHhdNo(), context);
                            if (houseHold != null) {
                                houseHold.setError_code(responsItem.getErrorCode());
                                houseHold.setError_type(AppConstant.SYNCING_ERROR);
                                houseHold.setError_msg(responsItem.getErrorMessage());
                                SeccDatabase.updateRsbyHousehold(houseHold, context);
                            }
                        }
                    }
                } else {
                    AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Inside Secc Sync status : internet issues ");
                    item1.setError_code(AppConstant.INTERNET_ERROR_CODE);
                    item1.setError_type(AppConstant.SYNCING_ERROR);
                    item1.setError_msg(AppConstant.INTERNET_ERROR_MSG);
                    SeccDatabase.updateSeccMember(item1, context);

                }
            }
        } catch (Exception e) {
            item1.setError_code(AppConstant.INTERNET_ERROR_CODE);
            item1.setError_type(AppConstant.SYNCING_ERROR);
            item1.setError_msg(AppConstant.INTERNET_ERROR_MSG);
            SeccDatabase.updateSeccMember(item1, context);
        }
        return false;
    }

    private void sendExceptHeadRsbyMember(SeccMemberItem item1) {
        RsbyMemberSyncRequest preparedMemberItem = SyncUtility.prepareRsbyMemberItemForSync(item1, context);
        preparedMemberItem.setAppVersion(AppUtility.getCurrentApplicationVersion(context));
        try {
            HashMap<String, String> response = CustomHttp.httpPost(AppConstant.SYNC_RSBY_MEMBER, preparedMemberItem.serialize());
            if (response != null) {
                String strResp = response.get(AppConstant.RESPONSE_BODY);
                AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Sync Member response : " + strResp);
                RsbyMemberSyncResponse responsItem = RsbyMemberSyncResponse.create(strResp);
                if (responsItem != null) {
                    if (responsItem != null && responsItem.isStatus()) {
                        if (responsItem.getSyncedStatus().equalsIgnoreCase(AppConstant.SYNCED_STATUS)) {
                            item1.setSyncedStatus(responsItem.getSyncedStatus());
                            item1.setNhpsId(responsItem.getNhpsId());
                            item1.setSyncDt(responsItem.getSyncdt());
                            item1.setAppVersion(responsItem.getAppVersion());
                            item1.setError_code(null);
                            item1.setError_type(null);
                            item1.setError_msg(null);
                            SeccDatabase.updateRsbyMember(item1, context);
                        }
                    } else {
                        AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Inside Secc Sync status : internet issues ");
                        if (responsItem.getErrorCode().equalsIgnoreCase(AppConstant.AADHAAR_ALREADY_ALLOCATED)) {
                            item1.setError_code(responsItem.getErrorCode());
                            item1.setError_type(AppConstant.SYNCING_ERROR);
                            item1.setError_msg(responsItem.getErrorMessage());
                            SeccDatabase.updateRsbyMember(item1, context);
                        } else if (responsItem.getErrorCode().equalsIgnoreCase(AppConstant.URI_ALREADY_EXIST)) {
                            item1.setError_code(responsItem.getErrorCode());
                            item1.setError_type(AppConstant.SYNCING_ERROR);
                            item1.setError_msg(responsItem.getErrorMessage());
                            SeccDatabase.updateRsbyMember(item1, context);
                        }
                    }
                } else {
                    AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Inside Secc Sync status : internet issues ");
                    item1.setError_code(AppConstant.INTERNET_ERROR_CODE);
                    item1.setError_type(AppConstant.SYNCING_ERROR);
                    item1.setError_msg(AppConstant.INTERNET_ERROR_MSG);
                    SeccDatabase.updateRsbyMember(item1, context);

                }
            }
        } catch (Exception e) {
            item1.setError_code(AppConstant.INTERNET_ERROR_CODE);
            item1.setError_type(AppConstant.SYNCING_ERROR);
            item1.setError_msg(AppConstant.INTERNET_ERROR_MSG);
            SeccDatabase.updateRsbyMember(item1, context);
        }
    }

    private void sendExceptHeadSeccMember(SeccMemberItem item1) {
        SeccMemberItem preparedMemberItem = SyncUtility.prepareMemberItemForSync(item1, context);
        preparedMemberItem.setAppVersion(AppUtility.getCurrentApplicationVersion(context));
        AppUtility.showLog(AppConstant.LOG_STATUS, TAG, " Prepared Item : " + preparedMemberItem.serialize());
        AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Sync Member ahl tin :" +
                " " + item1.getAhlTin() + " Aaadhar Auth Status :" + preparedMemberItem.getAadhaarAuth() + "" +
                " Aaadhar Capturing mode : " + preparedMemberItem.getAadhaarCapturingMode() + "" +
                " Govt Id type : " + preparedMemberItem.getIdType() + " Data Source : " + preparedMemberItem.getDataSource());
        try {
            String[] header = new String[2];

            header[0] = AppConstant.AUTHORIZATION;
            header[1] = AppConstant.AUTHORIZATIONVALUE;
            HashMap<String, String> response = CustomHttp.httpPostWithHeader(AppConstant.SYNC_MEMBER_API, preparedMemberItem.serialize(), header);
            String payLoad = preparedMemberItem.serialize().toString();
            AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Sync Member payLoad : " + payLoad);
            System.out.print(payLoad);
            if (response != null) {
                AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Sync Member request : " + preparedMemberItem.serialize());
                String strResp = response.get(AppConstant.RESPONSE_BODY);
                AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Sync Member response : " + strResp);
                SyncSeccMemberResponse responsItem = SyncSeccMemberResponse.create(strResp);
                if (responsItem != null) {
                    if (responsItem != null && responsItem.isStatus()) {
                        if (responsItem.getSyncedStatus().equalsIgnoreCase(AppConstant.SYNCED_STATUS)) {
                            item1.setSyncedStatus(responsItem.getSyncedStatus());
                            // item1.setNhpsId(responsItem.getNhpsId());
                            item1.setSyncDt(responsItem.getSyncDt());
                            //  item1.setAppVersion(responsItem.getAppVersion());
                            item1.setNhpsMemId(responsItem.getNhpsMemId());
                            item1.setError_code(null);
                            item1.setError_type(null);
                            item1.setError_msg(null);
                            SeccDatabase.updateSeccMember(item1, context);
                        }
                    } else {
                        AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Inside Secc Sync status : internet issues ");
                        if (responsItem.getErrorCode().equalsIgnoreCase(AppConstant.AADHAAR_ALREADY_ALLOCATED)) {
                            item1.setError_code(responsItem.getErrorCode());
                            item1.setError_type(AppConstant.SYNCING_ERROR);
                            item1.setError_msg(responsItem.getErrorMessage());
                            SeccDatabase.updateSeccMember(item1, context);
                        } else if (responsItem.getErrorCode().equalsIgnoreCase(AppConstant.URI_ALREADY_EXIST)) {
                            item1.setError_code(responsItem.getErrorCode());
                            item1.setError_type(AppConstant.SYNCING_ERROR);
                            item1.setError_msg(responsItem.getErrorMessage());
                            SeccDatabase.updateRsbyMember(item1, context);
                        }
                    }
                } else {
                    AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Inside Secc Sync status : internet issues ");
                    item1.setError_code(AppConstant.INTERNET_ERROR_CODE);
                    item1.setError_type(AppConstant.SYNCING_ERROR);
                    item1.setError_msg(AppConstant.INTERNET_ERROR_MSG);
                    SeccDatabase.updateSeccMember(item1, context);

                }
            }
        } catch (Exception e) {
            item1.setError_code(AppConstant.INTERNET_ERROR_CODE);
            item1.setError_type(AppConstant.SYNCING_ERROR);
            item1.setError_msg(AppConstant.INTERNET_ERROR_MSG);
            SeccDatabase.updateSeccMember(item1, context);
        }
    }

    private void sendRsbyHouseHold(HouseHoldItem item) {
        ArrayList<SeccMemberItem> updatedSeccList = SeccDatabase.getRsbyMemberListWithUrn(item.getRsbyUrnId(), context);
        boolean isAllMemberSynced = false;
        for (SeccMemberItem item1 : updatedSeccList) {
            if (item1.getSyncedStatus() == null || !item1.getSyncedStatus().equalsIgnoreCase(AppConstant.SYNCED_STATUS)) {
                isAllMemberSynced = true;
                break;
            }
        }
        if (!isAllMemberSynced) {
            RsbySyncHouseholdRequest preparedItem = SyncUtility.preparedRsbyHouseholdItem(item, context);
            preparedItem.setVlAadharNo(loginResponse.getAadhaarNumber());
            preparedItem.setAppVersion(AppUtility.getCurrentApplicationVersion(context));
            AppUtility.showLog(AppConstant.LOG_STATUS, TAG, " Update household Request : " + preparedItem.serialize());
            String[] header = new String[2];

            header[0] = AppConstant.AUTHORIZATION;
            header[1] = AppConstant.AUTHORIZATIONVALUE;
            try {
                HashMap<String, String> respStr = CustomHttp.httpPost(
                        AppConstant.SYNC_RSBY_HOUSEHOLD, preparedItem.serialize(), header);
                AppUtility.showLog(AppConstant.LOG_STATUS, TAG, " Household in null" + respStr);

                AppUtility.showLog(AppConstant.LOG_STATUS, TAG, " Update household Request : " + preparedItem.serialize() + " API : " +
                        "" + AppConstant.SYNC_RSBY_HOUSEHOLD);
                String payLoad = AppConstant.SYNC_RSBY_HOUSEHOLD + "////" + preparedItem.serialize().toString();
                System.out.print(payLoad);
                if (respStr != null) {
                    String response = respStr.get(AppConstant.RESPONSE_BODY);
                    RsbyHouseholdSyncResponse responseItem = RsbyHouseholdSyncResponse.create(response);
                    if (responseItem != null && responseItem.isStatus()) {
                        if (responseItem.getSyncedStatus().equalsIgnoreCase(AppConstant.SYNCED_STATUS)) {
                            item.setSyncedStatus(responseItem.getSyncedStatus());
                            item.setNhpsId(responseItem.getNhpsId());
                            item.setSyncDt(responseItem.getSyncDt());
                            item.setAppVersion(responseItem.getAppVersion());
                            item.setError_msg(null);
                            item.setError_type(null);
                            item.setError_code(null);
                            SeccDatabase.updateRsbyHousehold(item, context);
                            sendBroadcastStatus(broadcastIndex);

                        }
                    } else {
                        item.setError_msg(responseItem.getErrorMessage());
                        item.setError_code(responseItem.getErrorCode());
                        item.setError_type(AppConstant.SYNCING_ERROR);
                        AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Household sync error " + item.getError_code());
                        SeccDatabase.updateRsbyHousehold(item, context);
                        sendBroadcastStatus(broadcastIndex);
                    }
                }
            } catch (Exception e) {
                item.setError_msg(AppConstant.INTERNET_ERROR_MSG);
                item.setError_code(AppConstant.INTERNET_ERROR_CODE);
                item.setError_type(AppConstant.SYNCING_ERROR);
                SeccDatabase.updateRsbyHousehold(item, context);
                sendBroadcastStatus(broadcastIndex);
            }
        } else {
            item.setError_msg(AppConstant.SYNCING_MEMBER_ERROR);
            item.setError_type(AppConstant.SYNCING_ERROR);
            item.setError_code(AppConstant.SYNCING_MEMBER_ERROR);
            SeccDatabase.updateRsbyHousehold(item, context);
            sendBroadcastStatus(broadcastIndex);
        }
    }

    private void sendSeccHousehold(HouseHoldItem item) {
        ArrayList<SeccMemberItem> updatedSeccList = SeccDatabase.getSeccMemberList(item.getHhdNo(), context);
        boolean isAllMemberSynced = false;
        for (SeccMemberItem item1 : updatedSeccList) {
            if (item1.getSyncedStatus() == null || !item1.getSyncedStatus().equalsIgnoreCase(AppConstant.SYNCED_STATUS)) {
                isAllMemberSynced = true;
                break;
            }
        }
        if (!isAllMemberSynced) {
            HouseHoldItem preparedItem = SyncUtility.preparedHouseholdItem(item, context);
            preparedItem.setAppVersion(AppUtility.getCurrentApplicationVersion(context));
            AppUtility.showLog(AppConstant.LOG_STATUS, TAG, " Update household Request : " + preparedItem.serialize());
            try {
                String[] header = new String[2];

                header[0] = AppConstant.AUTHORIZATION;
                header[1] = AppConstant.AUTHORIZATIONVALUE;
                HashMap<String, String> respStr = CustomHttp.httpPostWithHeader(
                        AppConstant.HOUSE_HOLD_API, preparedItem.serialize(), header);
                String houseHoldPayload = preparedItem.serialize().toString();
                System.out.print(houseHoldPayload);
                AppUtility.showLog(AppConstant.LOG_STATUS, TAG, " Household payload" + houseHoldPayload);
                AppUtility.showLog(AppConstant.LOG_STATUS, TAG, " Household in null" + respStr);
                if (respStr != null) {
                    String response = respStr.get(AppConstant.RESPONSE_BODY);
                    SyncHouseholdResponse responseItem = SyncHouseholdResponse.create(response);
                    if (responseItem != null && responseItem.isStatus()) {
                        if (responseItem.getSyncedStatus().equalsIgnoreCase(AppConstant.SYNCED_STATUS)) {
                            item.setSyncedStatus(responseItem.getSyncedStatus());
                            //      item.setNhpsId(responseItem.getNhpsId());
                            item.setSyncDt(responseItem.getSyncDt());
                            item.setNhpsMemId(responseItem.getNhpsMemId());
                            //    item.setAppVersion(responseItem.getAppVersion());
                            item.setError_msg(null);
                            item.setError_type(null);
                            item.setError_code(null);
                            SeccDatabase.updateHouseHold(item, context);
                            sendBroadcastStatus(broadcastIndex);

                        }
                    } else {
                        item.setError_msg(responseItem.getErrorMessage());
                        item.setError_code(responseItem.getErrorCode());
                        item.setError_type(AppConstant.SYNCING_ERROR);
                        AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Household sync error " + item.getError_code());
                        SeccDatabase.updateHouseHold(item, context);
                        sendBroadcastStatus(broadcastIndex);
                    }
                }
            } catch (Exception e) {
                item.setError_msg(AppConstant.INTERNET_ERROR_MSG);
                item.setError_code(AppConstant.INTERNET_ERROR_CODE);
                item.setError_type(AppConstant.SYNCING_ERROR);
                SeccDatabase.updateHouseHold(item, context);
                sendBroadcastStatus(broadcastIndex);
            }
        } else {
            item.setError_msg(AppConstant.SYNCING_MEMBER_ERROR);
            item.setError_type(AppConstant.SYNCING_ERROR);
            item.setError_code(AppConstant.SYNCING_MEMBER_ERROR);
            SeccDatabase.updateHouseHold(item, context);
            sendBroadcastStatus(broadcastIndex);
        }
    }
}
