package com.customComponent;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.EditText;
import android.widget.TextView;


public class CustomAlert {


    public static void alertWithOk1(final Context mContext, String msg, final Intent intent) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(mContext.getResources().getString(R.string.Alert));
        builder.setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(mContext.getResources().getString(R.string.OK), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                        mContext.startActivity(intent);
                        // ((Activity)mContext).finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public static void alertOkWithFinish(final Context mContext, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(mContext.getResources().getString(R.string.Alert));
        builder.setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(mContext.getResources().getString(R.string.OK), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                       // mContext.startActivity(intent);
                         ((Activity)mContext).finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public static void alertWithOk(Context mContext, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(mContext.getResources().getString(R.string.Alert));
        builder.setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(mContext.getResources().getString(R.string.OK), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public static void alertWithValidateField(final Context mContext, String msg, final Object ediText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(mContext.getResources().getString(R.string.Alert));
        builder.setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(mContext.getResources().getString(R.string.OK), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                        if (ediText instanceof EditText) {
                            CustomComponent.setEditTextBackground((EditText) ediText, mContext);
                        } else if (ediText instanceof TextView) {
                            CustomComponent.setEditTextBackground((TextView) ediText, mContext);
                        }

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public static void alertWithOk(final Context mContext, String msg, final Intent intent) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(mContext.getResources().getString(R.string.Alert));
        builder.setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(mContext.getResources().getString(R.string.OK), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                        mContext.startActivity(intent);
                        ((Activity) mContext).finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public static void alertWithOkWithoutFinish(final Context mContext, String msg, final Intent intent) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(mContext.getResources().getString(R.string.Alert));
        builder.setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                        mContext.startActivity(intent);
//		        	  ((Activity)mContext).finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public static void alertWithOk(final Context mContext, String msg, final Intent nextScreen, Intent currentScreen) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(mContext.getResources().getString(R.string.Alert));
        builder.setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                        mContext.startActivity(nextScreen);
                        ((Activity) mContext).finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public static void alertWithYesNo(final Context mContext, String msg, final Intent nextScreen) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                mContext);

        // set title
        alertDialogBuilder.setTitle(mContext.getResources().getString(R.string.Alert));

        // set dialog message
        alertDialogBuilder
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, close
                        // current activity
                        mContext.startActivity(nextScreen);
                        ((Activity) mContext).finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }

    void setEditTextBackground(EditText text, Context mcontext) {
        text.setBackgroundResource(R.drawable.rounded_shape_edittext_for_validate);
//		text.setFocusable(true);
    }

    void setEditTextBackground(TextView text, Context mcontext) {
        text.setBackgroundResource(R.drawable.rounded_shape_edittext_for_validate);
    }

    public static void launchRingDialog(Context context,String titleMsg,String bodyMsg) {
        final ProgressDialog ringProgressDialog = ProgressDialog.show(context, bodyMsg,
                titleMsg, true);
        ringProgressDialog.setCancelable(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Here you should write your time consuming task...
                    // Let the progress ring for 10 seconds...

                    Thread.sleep(10000);

                } catch (Exception e) {

                }
                ringProgressDialog.dismiss();
            }
        }).start();
    }


}
