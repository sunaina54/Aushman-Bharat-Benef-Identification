package com.nhpm.Utility;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;


/**
 * DialogHelper. Created on 19-09-2015.
 */
public class DialogHelper {

    private static final String SHARED_OK = "OK";

    public static AlertDialog getOkayBtnDialog(Context context, String bodyMsg) {
        return getOkayBtnDialog(context, "Alert", bodyMsg, null);
    }

    public static AlertDialog getOkayBtnDialog(Context context, String bodyMsg, Intent okRunnable) {
        return getOkayBtnDialog(context, "Alert", bodyMsg, okRunnable);
    }

    public static AlertDialog getOkayBtnDialog(Context context, String title, String message) {
        return getOkayBtnDialog(context, title, message, null);
    }

    public static AlertDialog getOkayBtnDialog(final Context context, String title, String message, final Intent
            okRunnable) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).setTitle(title).setMessage(message)
                .setPositiveButton(SHARED_OK,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                if (okRunnable != null) {

                                    context.startActivity(okRunnable);
                                    ((Activity)context).finish();

                                }
                            }
                        }).create();
        return alertDialog;
    }

}
