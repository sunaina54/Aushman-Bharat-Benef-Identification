package com.nhpm.backgroundService;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.customComponent.utility.CustomHttp;
import com.customComponent.utility.DateTimeUtil;
import com.nhpm.LocalDataBase.dto.SeccDatabase;
import com.nhpm.Models.response.seccMembers.SeccMemberItem;
import com.nhpm.Models.response.verifier.DemoAuthResponseItem;
import com.nhpm.Utility.AppConstant;

import java.util.HashMap;

/**
 * Created by Anand on 22-11-2016.
 */
public class VerifyAadhaarService extends IntentService {
    private SeccMemberItem seccMemberItem;
    private String TAG="VERIFY AADHAAR SERVICE";

    public VerifyAadhaarService(){
        super(VerifyAadhaarService.class.getName());
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        seccMemberItem=(SeccMemberItem)intent.getSerializableExtra("AuthAadhaar");
        validateAadhaar(seccMemberItem);
    }
    private void validateAadhaar(SeccMemberItem item){
        String aadhaarNumber=seccMemberItem.getAadhaarNo();
        String nameAsInAadhaar=seccMemberItem.getNameAadhaar();
        Log.d(TAG,"Aadhaar Number : "+aadhaarNumber+" Name As In Aadhaar : "+nameAsInAadhaar);
        String url= AppConstant.AADHAAR_DEMO_AUTH_API+aadhaarNumber+"/"+nameAsInAadhaar.replaceAll(" ", "%20")+AppConstant.USER_ID+AppConstant.PASSWORD;
        Log.d(TAG, "Complete Aadhaar Auth Api : " + url);
        try {
            HashMap<String, String> demoAuthResp = CustomHttp.httpGet(url, null);
            String demoAuth=demoAuthResp.get("response");
            if(demoAuth!=null) {
                DemoAuthResponseItem demoAuthItem = DemoAuthResponseItem.create(demoAuth);
                if(demoAuthItem!=null) {
                    Log.d(TAG, "Aadhaar Response : " + demoAuth);
                    if(demoAuthItem.getRet().equalsIgnoreCase(AppConstant.VALID_STATUS)){
                        item.setAadhaarAuthMode(AppConstant.DEMO_AUTH);
                        item.setAadhaarAuth(demoAuthItem.getRet().trim());
                        item.setAadhaarAuthDt(String.valueOf(DateTimeUtil.currentTimeMillis()));
                    }else if(demoAuthItem.getRet().equalsIgnoreCase(AppConstant.INVALID_STATUS)){
                        item.setAadhaarAuthMode(AppConstant.DEMO_AUTH);
                        item.setAadhaarAuth(demoAuthItem.getRet().trim());
                    }
                    SeccDatabase.updateSeccMember(item, getApplicationContext());
                }
            }
        }catch (Exception e){
            Log.d(TAG,"Demo auth exception : "+e.toString());
        }
    }


}
