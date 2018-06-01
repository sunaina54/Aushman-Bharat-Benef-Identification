package com.nhpm.backgroundService;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.customComponent.utility.CustomHttp;
import com.nhpm.LocalDataBase.dto.SeccDatabase;
import com.nhpm.Models.response.rsbyMembers.RSBYMemberItemResponse;
import com.nhpm.Models.response.seccMembers.SeccMemberItem;
import com.nhpm.Utility.AppConstant;

import java.util.HashMap;

/**
 * Created by Anand on 22-11-2016.
 */
public class VerifyStateHealthService extends IntentService {
    private SeccMemberItem seccMemberItem;
    private String TAG="VERIFY AADHAAR SERVICE";

    public VerifyStateHealthService(){
        super(VerifyStateHealthService.class.getName());
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        seccMemberItem=(SeccMemberItem)intent.getSerializableExtra("stateHealth");
        validateAadhaar(seccMemberItem);
    }
    private void validateAadhaar(SeccMemberItem item){
        String urnNumber=item.getUrnNo();
        String url=AppConstant.RSBY_URN_SEARCH+urnNumber.trim();
        Log.d(TAG,"RSBY URN URL : "+url);
        try {
            HashMap<String, String> demoAuthResp = CustomHttp.httpGet(url, null);
            String demoAuth=demoAuthResp.get("response");
            Log.d(TAG,"Aadhaar Response : "+demoAuth);
            RSBYMemberItemResponse resp=RSBYMemberItemResponse.create(demoAuth);
            Log.d(TAG, "RSBY List Size : " + resp.getUrnList().size());
            if(resp.getStatusCode().equalsIgnoreCase("1")) {
                item.setUrnAuth("Y");
            }else if(resp.getStatusCode().equalsIgnoreCase("0")){
                item.setUrnAuth("N");
            }else{
                item.setUrnAuth("P");
            }
            SeccDatabase.updateSeccMember(item,getApplicationContext());
            SeccMemberItem item1=SeccDatabase.getSeccMemberDetail(item, getApplicationContext());
            Log.d(TAG,"URN Status : "+item1.getUrnAuth());


        }catch (Exception e){
            Log.d(TAG,"Demo auth exception : "+e.toString());
        }
    }


}
