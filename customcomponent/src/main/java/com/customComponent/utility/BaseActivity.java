package com.customComponent.utility;

import android.app.Activity;
import android.os.Bundle;

import com.customComponent.R;


/**
 * BaseActionBarActivity. Created on 15-12-2015.
 */
public class BaseActivity extends Activity {
   // private ProgressDialog _progressDialog;
   TransDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*_progressDialog = new ProgressDialog(this);
        _progressDialog.setCancelable(false);
        _progressDialog.setCanceledOnTouchOutside(false);
        _progressDialog.setMessage(getString(R.string.sharedWaitMsg));*/
        pd = new TransDialog(this, R.drawable.loading);
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
    }





    public void showHideProgressDialog(boolean isShow) {
        showHideProgressDialog(isShow, getString(R.string.please_wait));
    }

    public void showHideProgressDialog(boolean isShow, String message) {
        if (isShow) {
          //  _progressDialog.setMessage(message);
          //  _progressDialog.show();
            pd.show();
        } else {
           // _progressDialog.dismiss();
            pd.dismiss();
        }
    }


}
