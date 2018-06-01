package com.nhpm.Utility;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import com.nhpm.activity.LoginActivity;


public class SmsReceiver extends BroadcastReceiver {
    public SmsReceiver() {
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        SmsMessage[] msgs = null;
        String str = "";
        String mMessageFrom = "";
        String mMessageBody = "";
        String mRequiredMessage = "";
        if (bundle != null) {
            Object[] pdus = (Object[]) bundle.get("pdus");
            msgs = new SmsMessage[pdus.length];
            for (int i = 0; i < msgs.length; i++) {
                msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                str += "SMS from " + msgs[i].getOriginatingAddress() + " : ";
                mMessageFrom = msgs[i].getDisplayOriginatingAddress();
                str += msgs[i].getMessageBody().toString();
                mMessageBody = msgs[i].getMessageBody().toString();
                str += "\n";
            }
            String strng = mMessageBody;
            if (mMessageFrom.contains("VM-ADHAAR") || mMessageFrom.contains("VK-ADHAAR") ||mMessageFrom.contains("VI-ADHAAR")) {
                try {
                mRequiredMessage= str.substring(str.indexOf("is") + 3, str.indexOf("and") - 1);

                   // LoginActivity.SetMessageToEdittext(mRequiredMessage);
                }catch(Exception e){

                }
            }
          //  Otp_Activity.SetMessageToEdittext(mRequiredMessage);
            System.out.println(mRequiredMessage);
        }
    }
}
