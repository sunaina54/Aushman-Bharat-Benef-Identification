package com.aadhar;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;


public class ShowDialogWaitForAuth extends Dialog {

	public ShowDialogWaitForAuth(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d("My", "ShowDialogWaitForAuth.onCreate");
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_show_dialog_wait_for_auth);
		
		

	}

	
}
