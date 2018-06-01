package com.nhpm.backgroundService;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.customComponent.utility.CustomHttp;
import com.customComponent.utility.ProjectPrefrence;
import com.nhpm.LocalDataBase.dto.SeccDatabase;
import com.nhpm.Models.response.rsbyMembers.RSBYItem;
import com.nhpm.Models.response.rsbyMembers.RsbyHouseholdItem;
import com.nhpm.Models.response.rsbyMembers.RsbyHouseholdSyncResponse;
import com.nhpm.Models.response.rsbyMembers.RsbyMemberSyncRequest;
import com.nhpm.Models.response.rsbyMembers.RsbyMemberSyncResponse;
import com.nhpm.Models.response.rsbyMembers.RsbySyncHouseholdRequest;
import com.nhpm.Models.response.seccMembers.SeccMemberItem;
import com.nhpm.Models.response.seccMembers.SyncSeccMemberResponse;
import com.nhpm.Models.response.verifier.VerifierLoginResponse;
import com.nhpm.Utility.AppConstant;
import com.nhpm.Utility.AppUtility;
import com.nhpm.Utility.SyncUtility;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class RsbySyncService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private final String TAG="Sync Service";
    private static final String ACTION_FOO = "com.nhpm.backgroundService.action.FOO";
    private static final String ACTION_BAZ = "com.nhpm.backgroundService.action.BAZ";
    public static volatile boolean shouldContinue = true;
    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.nhpm.backgroundService.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.nhpm.backgroundService.extra.PARAM2";
    private ArrayList<RsbyHouseholdItem> pendingHouseholdList;
    public static String SYNC_ARG="syncArg";
    private Context context;
    public static final String BROADCAST_ACTION = "SyncServiceBroadcast";
    private Intent broadCastIntent;
    public static final String RESPONSE_SUCCESS="ResponseSuccess";
    public static  final String SYNC_COMPLETE="SyncComplete";
    private ArrayList<RsbyHouseholdItem> pureList;
    private VerifierLoginResponse loginResponse;
    int broadcastIndex=0;



    public RsbySyncService() {
        super("SyncService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
   /* public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, SyncService.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }*/


    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, RsbySyncService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        context=getApplicationContext();
        loginResponse=(VerifierLoginResponse.create(ProjectPrefrence.
                getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.VERIFIER_CONTENT,context)));

         broadCastIntent=new Intent(BROADCAST_ACTION);
        if (intent != null) {
            pendingHouseholdList=(ArrayList<RsbyHouseholdItem>) intent.getSerializableExtra(SYNC_ARG);
            AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"ArrayList : "+pendingHouseholdList.size());
          //  syncHousehold();
            findAllDuplicateAadhaar();

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

    private void findAllDuplicateAadhaar(){
       /* ArrayList<HouseHoldItem> lockedList=SeccDatabase.seccHouseholdLockedList(AppConstant.LOCKED+"",context);
        ArrayList<SeccMemberItem> lockedMemberList=SeccDatabase.seccMemberLockedList(AppConstant.LOCKED+"",context);
        ArrayList<HouseHoldItem> list=new ArrayList<>();*/

       pureList=new ArrayList<>();
        for(int index=0; index<pendingHouseholdList.size();index++){
            RsbyHouseholdItem item=pendingHouseholdList.get(index);
            ArrayList<RSBYItem> memberList= SeccDatabase.getRsbyMemberList(item.getUrnId(),context);
            int duplicateCount=0;
            stopService();
            for(RSBYItem item1 : memberList) {
                if(item1.getAadhaarNo()!=null && !item1.getAadhaarNo().equalsIgnoreCase("") && item1.getAadhaarNo().trim().equalsIgnoreCase(loginResponse.getAadhaarNumber().trim())) {
                    item1.setError_code(AppConstant.VERIFIER_AADHAAR_ALLOCATED);
                    item1.setError_type(AppConstant.SYNCING_ERROR);
                    item1.setError_msg(AppConstant.VERIFIER_AADHAAR_ALLOCATED_MSG);
                    SeccDatabase.updateRsbyMember(item1, context);
                    duplicateCount++;
                    stopService();
                }else if (item1.getAadhaarNo() != null && !item1.getAadhaarNo().equalsIgnoreCase("")) {
                    stopService();
                    ArrayList<RSBYItem> list1 = SeccDatabase.rsbyMemberWithAadhaarLocked(item1.getAadhaarNo(), AppConstant.LOCKED + "", context);
                    for (RSBYItem item2 : list1) {
                        if (!item2.getRsbyMemId().trim().equalsIgnoreCase(item1.getRsbyMemId().trim())) {
                            RsbyHouseholdItem houseHoldItem = SeccDatabase.getRsbyHouseHoldQ(item2.getUrnId(), context);
                            AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Sending validated data..8"+item2.getName());
                            if (houseHoldItem.getLockedSave() != null && houseHoldItem.getLockedSave()
                                    .equalsIgnoreCase(AppConstant.LOCKED + "")) {
                                AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Sending validated data..9"+item2.getName());
                                item1.setError_code(AppConstant.AADHAAR_ALREADY_ALLOCATED);
                                item1.setError_type(AppConstant.SYNCING_ERROR);
                                item1.setError_msg(AppConstant.AADHAAR_ALREADY_ALLOCATED_MSG);
                                SeccDatabase.updateRsbyMember(item1, context);
                                duplicateCount++;
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
                SeccDatabase.updateRSBYHouseHold(item, context);
                // pendingHouseholdList.remove(item);
                sendBroadcastStatus(broadcastIndex);
                broadcastIndex++;
                AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Sending validated data..1"+broadcastIndex);
            }else{
                AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Sending validated data..2"+broadcastIndex);
                pureList.add(item);
                //broadcastIndex++;
            }
        }


        for(RsbyHouseholdItem item : pureList) {
          //  pendingHouseholdList.remove(item);
            AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Sending validated data.."+broadcastIndex);
            //sendBroadcastStatus(pendingHouseholdIndex);
           // broadcastIndex=broadcastIndex+1;
            //sendBroadcastStatus(broadcastIndex);
           // syncHousehold(item);
            if(item.getHhStatus()!=null && item.getHhStatus().trim().equalsIgnoreCase(AppConstant.HOUSEHOLD_FOUND)){
                syncHouseHoldFound(item);
            }else{
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
 /*   private void syncHousehold(RsbyHouseholdItem item){
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

                }else{
                    preparedMemberItem = SyncUtility.prepareMemberItemForSync(item1);
                    AppUtility.showLog(AppConstant.LOG_STATUS, TAG, " Prepared Item : " + preparedMemberItem.serialize());
                    AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Sync Member ahl tin :" +
                        " " + item.getAhlTin() + " Aaadhar Auth Status :" + preparedMemberItem.getAadhaarAuth() + "" +
                        " Aaadhar Capturing mode : " + preparedMemberItem.getAadhaarCapturingMode() + "" +
                        " Govt Id type : " + preparedMemberItem.getIdType() + " Data Source : " + preparedMemberItem.getDataSource());

                    try {
                    HashMap<String, String> response = CustomHttp.httpPost(AppConstant.SYNC_MEMBER_API, preparedMemberItem.serialize(), null);
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

            ArrayList<SeccMemberItem> updatedSeccList= SeccDatabase.getSeccMemberList(item.getHhdNo(),context);
            boolean isAllMemberSynced=false;
            for(SeccMemberItem item1 : updatedSeccList){
                if(item1.getSyncedStatus()==null || !item1.getSyncedStatus().equalsIgnoreCase(AppConstant.SYNCED_STATUS)){
                    isAllMemberSynced=true;
                    break;
                }
            }
            if(!isAllMemberSynced) {
                HouseHoldItem preparedItem = SyncUtility.preparedHouseholdItem(item);
                AppUtility.showLog(AppConstant.LOG_STATUS,TAG," Update household Request : "+preparedItem.serialize());
                try {
                    HashMap<String, String> respStr = CustomHttp.httpPost(
                            AppConstant.HOUSE_HOLD_API, preparedItem.serialize(), null);
                    AppUtility.showLog(AppConstant.LOG_STATUS,TAG," Household in null"+respStr);
                    if(respStr!=null) {
                        String response=respStr.get(AppConstant.RESPONSE_BODY);
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
                        }else{
                            item.setError_msg(responseItem.getErrorMessage());
                            item.setError_code(responseItem.getErrorCode());
                            item.setError_type(AppConstant.SYNCING_ERROR);
                            AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Household sync error "+item.getError_code());
                            SeccDatabase.updateHouseHold(item, context);
                            sendBroadcastStatus(broadcastIndex);
                        }
                    }
                }catch (Exception e){
                    item.setError_msg(AppConstant.INTERNET_ERROR_MSG);
                    item.setError_code(AppConstant.INTERNET_ERROR_CODE);
                    item.setError_type(AppConstant.SYNCING_ERROR);
                 //   AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Household sync error "+item.getError_code());
                    SeccDatabase.updateRSBYHouseHold(item, context);
                    sendBroadcastStatus(broadcastIndex);
                }
            }else{
                item.setError_msg(AppConstant.SYNCING_MEMBER_ERROR);
                item.setError_type(AppConstant.SYNCING_ERROR);
                item.setError_code(AppConstant.SYNCING_MEMBER_ERROR);
            //    AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Household sync error "+item.getError_code());
                SeccDatabase.updateHouseHold(item, context);
                sendBroadcastStatus(broadcastIndex);
            }
        }*/
    //}
    private void sendBroadcastStatus(int index){
        if(index==pendingHouseholdList.size()-1) {
            AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Sending validated data..3 "+index);
            broadCastIntent.putExtra(RESPONSE_SUCCESS,SYNC_COMPLETE);
            sendBroadcast(broadCastIntent);
        }else if(index== AppConstant.cancelSyncService){
            broadCastIntent.putExtra(RESPONSE_SUCCESS, AppConstant.cancelSyncMsg);
            sendBroadcast(broadCastIntent);
        }else{
            AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Sending validated data..4 "+index);
            broadCastIntent.putExtra(RESPONSE_SUCCESS,"x");
            sendBroadcast(broadCastIntent);
        }
    }
    private void syncHouseHoldFound(RsbyHouseholdItem item){
        ArrayList<RSBYItem> allMemberList= SeccDatabase.getRsbyMemberList(item.getUrnId(),context);
        RSBYItem newHeadOftheFamily=null;
        RSBYItem oldHeadOftheFamily=null;
        SeccMemberItem seccHeadOftheFamily=null;
        ArrayList<RSBYItem> memberList=new ArrayList<>();
        if(allMemberList!=null){
            for(RSBYItem item1 : allMemberList){
               /* if(item1.getNhpsRelationCode()!=null && item1.getNhpsRelationCode().trim().equalsIgnoreCase(AppConstant.NEW_HEAD_RELATION_CODE)){
                    newHeadOftheFamily=item1;
                }*//*else if(item1.getNhpsMemId()!=null && item1.getNhpsMemId().trim().equalsIgnoreCase(item.getNhpsMemId().trim())){
                    oldHeadOftheFamily=item1;
                }*//*else{
                    memberList.add(item1);
                }*/
                stopService();

                if(item1.getRsbyMemId()!=null && item1.getRsbyMemId().trim().
                        equalsIgnoreCase(item.getRsbyMemId().trim()) && item1.getNhpsRelationCode()!=null &&
                        item1.getNhpsRelationCode().trim().equalsIgnoreCase(AppConstant.NEW_HEAD_RELATION_CODE)){
                        oldHeadOftheFamily=item1;
                }else if(item1.getNhpsRelationCode()!=null &&
                        item1.getNhpsRelationCode().trim().equalsIgnoreCase(AppConstant.NEW_HEAD_RELATION_CODE)
                        && !item1.getRsbyMemId().trim().equalsIgnoreCase(item.getRsbyMemId().trim()) ){
                        newHeadOftheFamily=item1;
                }else if(item1.getRsbyMemId()!=null && item1.getRsbyMemId().trim()
                        .equalsIgnoreCase(item.getRsbyMemId().trim()) ){
                       if(!item1.getMemStatus().trim().equalsIgnoreCase(AppConstant.MEMBERFOUND_AND_PRESENT)||
                               !item1.getMemStatus().equalsIgnoreCase(AppConstant.MEMBERFOUND_BUT_NOT_PRESENT)) {
                           oldHeadOftheFamily = item1;
                       }
                }else {
                    memberList.add(item1);
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

        if(oldHeadOftheFamily!=null && newHeadOftheFamily!=null){
            AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"OLD HEAD "+oldHeadOftheFamily.getSyncedStatus());
             if (oldHeadOftheFamily.getSyncedStatus() != null && oldHeadOftheFamily.getSyncedStatus().trim().
                        equalsIgnoreCase(AppConstant.SYNCED_STATUS)) {
                 AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"OLD HEAD "+oldHeadOftheFamily.getSyncedStatus());
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
                 AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"OLD HEAD "+oldHeadOftheFamily.getSyncedStatus());
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
            }else if(oldHeadOftheFamily!=null && newHeadOftheFamily==null){
                if(oldHeadOftheFamily.getSyncedStatus()!=null &&
                        oldHeadOftheFamily.getSyncedStatus().trim().equalsIgnoreCase(AppConstant.SYNCED_STATUS)){

                }else{
                    if(syncHeadOftheFamily(oldHeadOftheFamily)){
                        syncMemberExceptHeadoftheFamily(memberList,item);
                    }else{
                        sendBroadcastStatus(broadcastIndex);
                    }
                }
                /*if(syncHeadOftheFamily(oldHeadOftheFamily)){
                   // syncMemberExceptHeadoftheFamily(memberList,item);
                }*/
            }

    }
    private boolean syncHeadOftheFamily(RSBYItem item1){
        RsbyMemberSyncRequest preparedMemberItem = SyncUtility.prepareRsbyMemberItemForSync(item1);
        preparedMemberItem.setAppVersion(AppUtility.getCurrentApplicationVersion(context));
        AppUtility.showLog(AppConstant.LOG_STATUS, TAG, " Prepared Item : " + preparedMemberItem.serialize());
    /*  AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Sync Member ahl tin :" +
                " " + item1.getAhlTin() + " Aaadhar Auth Status :" + preparedMemberItem.getAadhaarAuth() + "" +
                " Aaadhar Capturing mode : " + preparedMemberItem.getAadhaarCapturingMode() + "" +
                " Govt Id type : " + preparedMemberItem.getIdType() + " Data Source : " + preparedMemberItem.getDataSource());*/
        String payLoad = AppConstant.SYNC_RSBY_MEMBER+"////"+preparedMemberItem.serialize().toString();
        System.out.print(payLoad);
        String[] header=new String[2];

        header[0]=AppConstant.AUTHORIZATION;
        header[1]=AppConstant.AUTHORIZATIONVALUE;
       try {
            HashMap<String, String> response = CustomHttp.httpPost(AppConstant.SYNC_RSBY_MEMBER, preparedMemberItem.serialize(), header);
            if (response != null) {
                AppUtility.showLog(AppConstant.LOG_STATUS, TAG,
                        "Sync Member request : " + preparedMemberItem.serialize()+" API : "+AppConstant.SYNC_RSBY_MEMBER);
              /* String payLoad = AppConstant.SYNC_RSBY_MEMBER+"////"+preparedMemberItem.serialize().toString();
                System.out.print(payLoad);*/
                String strResp = response.get(AppConstant.RESPONSE_BODY);
                AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Sync Member response : " + strResp);
                RsbyMemberSyncResponse responsItem = RsbyMemberSyncResponse.create(strResp);
                if (responsItem != null) {
                    if (responsItem != null && responsItem.isStatus()) {
                        if (responsItem.getSyncedStatus()!=null && responsItem.getSyncedStatus().trim().equalsIgnoreCase(AppConstant.SYNCED_STATUS)) {
                            item1.setSyncedStatus(responsItem.getSyncedStatus().trim());
                            item1.setNhpsId(responsItem.getNhpsId());
                            item1.setSyncDt(responsItem.getSyncdt());
                            item1.setAppVersion(responsItem.getAppVersion());
                            item1.setError_code(null);
                            item1.setError_type(null);
                            item1.setError_msg(null);
                            SeccDatabase.updateRsbyMember(item1, context);
                            return true;
                        }else{
                            System.out.print(AppConstant.SYNC_RSBY_MEMBER);
                        }
                    } else {
                        AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Inside Secc Sync status : internet issues ");
                        if (responsItem.getErrorCode().equalsIgnoreCase(AppConstant.AADHAAR_ALREADY_ALLOCATED)) {
                            item1.setError_code(responsItem.getErrorCode());
                            item1.setError_type(AppConstant.SYNCING_ERROR);
                            item1.setError_msg(responsItem.getErrorMessage());
                            SeccDatabase.updateRsbyMember(item1, context);
                        }else if(responsItem.getErrorCode().equalsIgnoreCase(AppConstant.AADHAAR_ALREADY_ALLOCATED)){

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
    private void syncMemberExceptHeadoftheFamily(ArrayList<RSBYItem> memberList, RsbyHouseholdItem houseHoldItem){
        if(memberList!=null){
            for(RSBYItem item1 : memberList){
              if(item1.getSyncedStatus()!=null && item1.getSyncedStatus().trim().equalsIgnoreCase(AppConstant.SYNCED_STATUS)){

              }else{
                  RsbyMemberSyncRequest preparedMemberItem = SyncUtility.prepareRsbyMemberItemForSync(item1);
                  preparedMemberItem.setAppVersion(AppUtility.getCurrentApplicationVersion(context));
                  AppUtility.showLog(AppConstant.LOG_STATUS, TAG, " Prepared Item : "
                          + preparedMemberItem.serialize()+" API : "+AppConstant.SYNC_RSBY_MEMBER);
                  String[] header=new String[2];

                  header[0]=AppConstant.AUTHORIZATION;
                  header[1]=AppConstant.AUTHORIZATIONVALUE;
                  try {
                      HashMap<String, String> response = CustomHttp.httpPost(AppConstant.SYNC_RSBY_MEMBER, preparedMemberItem.serialize(), header);
                      AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Sync Member response : " + response);

                      if (response != null) {
                          AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Sync Member request : " + preparedMemberItem.serialize());
                          String strResp = response.get(AppConstant.RESPONSE_BODY);
                          AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Sync Member response : " + strResp);
                          RsbyMemberSyncResponse responsItem = RsbyMemberSyncResponse.create(strResp);
                          if (responsItem != null) {
                              if (responsItem != null && responsItem.isStatus()) {
                                  if (responsItem.getSyncedStatus()!=null &&
                                          responsItem.getSyncedStatus().trim().equalsIgnoreCase(AppConstant.SYNCED_STATUS)) {
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
        }
        syncHouseholdItem(houseHoldItem);
    }
    private void syncExceptHouseholdFound(RsbyHouseholdItem houseHoldItem){
        stopService();
        ArrayList<RSBYItem> memberList= SeccDatabase.getRsbyMemberList(houseHoldItem.getUrnId(),context);
        for(RSBYItem item1 : memberList){
            if(item1.getSyncedStatus()!=null && item1.getSyncedStatus().trim().equalsIgnoreCase(AppConstant.SYNCED_STATUS)){

            }else{
                RsbyMemberSyncRequest preparedMemberItem = SyncUtility.prepareRsbyMemberItemForSync(item1);
                preparedMemberItem.setAppVersion(AppUtility.getCurrentApplicationVersion(context));
                AppUtility.showLog(AppConstant.LOG_STATUS, TAG, " Prepared Item : "
                        + preparedMemberItem.serialize()+" API : "+AppConstant.SYNC_RSBY_MEMBER);
               /* AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Sync Member ahl tin :" +
                        " " + item1.getAhlTin() + " Aaadhar Auth Status :" + preparedMemberItem.getAadhaarAuth() + "" +
                        " Aaadhar Capturing mode : " + preparedMemberItem.getAadhaarCapturingMode() + "" +
                        " Govt Id type : " + preparedMemberItem.getIdType() + " Data Source : " + preparedMemberItem.getDataSource());
         */
                String[] header=new String[2];

                header[0]=AppConstant.AUTHORIZATION;
                header[1]=AppConstant.AUTHORIZATIONVALUE;
                try {
                    HashMap<String, String> response = CustomHttp.httpPost(AppConstant.SYNC_RSBY_MEMBER, preparedMemberItem.serialize(), header);
                    if (response != null) {
                        AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Sync Member request : " + preparedMemberItem.serialize());
                        String payLoad = AppConstant.SYNC_RSBY_MEMBER+"////"+preparedMemberItem.serialize().toString();
                        System.out.print(payLoad);
                        String strResp = response.get(AppConstant.RESPONSE_BODY);
                        AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Sync Member response : " + strResp);
                        SyncSeccMemberResponse responsItem = SyncSeccMemberResponse.create(strResp);
                        System.out.print(responsItem);
                        if (responsItem != null) {
                            if (responsItem != null && responsItem.isStatus()) {
                                if (responsItem.getSyncedStatus().equalsIgnoreCase(AppConstant.SYNCED_STATUS)) {
                                    item1.setSyncedStatus(responsItem.getSyncedStatus());
                                    item1.setNhpsId(responsItem.getNhpsId());
                                    item1.setSyncDt(responsItem.getSyncDt());
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
                                }else{
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
    private void syncHouseholdItem(RsbyHouseholdItem item){
        ArrayList<RSBYItem> updatedSeccList= SeccDatabase.getRsbyMemberList(item.getUrnId(),context);
        boolean isAllMemberSynced=false;
        for(RSBYItem item1 : updatedSeccList){
            if(item1.getSyncedStatus()==null || !item1.getSyncedStatus().equalsIgnoreCase(AppConstant.SYNCED_STATUS)){
                isAllMemberSynced=true;
                break;
            }
        }
        if(!isAllMemberSynced) {
            RsbySyncHouseholdRequest preparedItem = SyncUtility.preparedRsbyHouseholdItem(item);
            preparedItem.setAppVersion(AppUtility.getCurrentApplicationVersion(context));
            AppUtility.showLog(AppConstant.LOG_STATUS,TAG," Update household Request : "+preparedItem.serialize()+" API : " +
                    ""+ AppConstant.SYNC_RSBY_HOUSEHOLD);
            String payLoad = AppConstant.SYNC_RSBY_HOUSEHOLD+"////"+preparedItem.serialize().toString();
            System.out.print(payLoad);
            String[] header=new String[2];

            header[0]=AppConstant.AUTHORIZATION;
            header[1]=AppConstant.AUTHORIZATIONVALUE;
            try {
                HashMap<String, String> respStr = CustomHttp.httpPost(
                        AppConstant.SYNC_RSBY_HOUSEHOLD, preparedItem.serialize(), header);
                AppUtility.showLog(AppConstant.LOG_STATUS,TAG," Household in null"+respStr);
                if(respStr!=null) {
                    String response=respStr.get(AppConstant.RESPONSE_BODY);
                    //  AppUtility.alertWithOk(context,"sync household response : "+response);
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
                            SeccDatabase.updateRSBYHouseHold(item, context);
                            sendBroadcastStatus(broadcastIndex);

                        }
                    }else{
                        item.setError_msg(responseItem.getErrorMessage());
                        item.setError_code(responseItem.getErrorCode());
                        item.setError_type(AppConstant.SYNCING_ERROR);
                        AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Household sync error "+item.getError_code());
                        SeccDatabase.updateRSBYHouseHold(item, context);
                        sendBroadcastStatus(broadcastIndex);
                    }
                }
            }catch (Exception e){
                item.setError_msg(AppConstant.INTERNET_ERROR_MSG);
                item.setError_code(AppConstant.INTERNET_ERROR_CODE);
                item.setError_type(AppConstant.SYNCING_ERROR);
                //   AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Household sync error "+item.getError_code());
                SeccDatabase.updateRSBYHouseHold(item, context);
                sendBroadcastStatus(broadcastIndex);
            }
        }else{
            item.setError_msg(AppConstant.SYNCING_MEMBER_ERROR);
            item.setError_type(AppConstant.SYNCING_ERROR);
            item.setError_code(AppConstant.SYNCING_MEMBER_ERROR);
            //    AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Household sync error "+item.getError_code());
            SeccDatabase.updateRSBYHouseHold(item, context);
            sendBroadcastStatus(broadcastIndex);
        }
    }

private void stopService(){
    try {
        if (shouldContinue == false) {

            sendBroadcastStatus(AppConstant.cancelSyncService);
            stopSelf();
            // unregisterReceiver(broadCastIntent);
            shouldContinue = true;
            return;
        }
    }catch (Exception ex){

    }
}
}
