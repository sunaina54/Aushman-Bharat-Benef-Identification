package com.aadhar;

import android.content.Context;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class StatusLogs {
	Context myContext;
	ShowDialogWaitForAuth dialogWaitForAuth;
	CheckConnection checkConnection;
	String message;

	public StatusLogs(Context mycontext){
		myContext = mycontext;
		Log.e("mycontext", "=StatusLogs classs=");
	}

	public void myMessage(String t_ID , String time,String status){

		message = Global.DEVICE_IMEI_NO + ","+ t_ID +","+time + ","+status +",";
		Log.e("message", "=StatusLogs= " + message);
		appendLog(message);

	}

	public void appendLog(String text)
	{       
		File logFile = new File("sdcard/success_fail_log.txt");
		if (!logFile.exists())
		{
			try
			{
				logFile.createNewFile();
			} 
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try
		{
			//BufferedWriter for performance, true to set append to file flag
			BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
			buf.append(text);
			buf.newLine();
			buf.close();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
