package com.aadhar;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class Logs {

	Context myContext;
	ShowDialogWaitForAuth dialogWaitForAuth;
	CheckConnection checkConnection;
	String message;

	public Logs(Context mycontext){
		myContext = mycontext;
		Log.e("mycontext", "=Logs classs=");
	}

	public void myMessage(String msg , String uid){
		Calendar c = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		String formattedDate = df.format(c.getTime());
		//message = Global.DEVICE_IMEI_NO + ","+ formattedDate +","+Global.latitude + ","+Global.longitude +","+(Global.OPERATOR_NAME + "#" + Global.OPERATOR_NAME_SET) +","+(Global.LOCATION_ADDRESS.replaceAll(",", "_") + "#" + Global.CURRENT_LOCATION_SET.replace(",", "_"))+ "," +Global.SIGNAL_STRENGTH+  ","+msg + ","+uid+",";
		if(Global.connectionType.equalsIgnoreCase("Wifi"))
			message = Global.DEVICE_IMEI_NO + ","+ formattedDate +","+ Global.NETWORK_TYPE + ","+ Global.NETWORK_NAME +","+(Global.LOCATION_ADDRESS.replaceAll(",", "_") + "#" + Global.CURRENT_LOCATION_SET.replace(",", "_"))+ "," + Global.SIGNAL_STRENGTH+  ","+msg + ","+uid+",";
		else
			message = Global.DEVICE_IMEI_NO + ","+ formattedDate +","+ Global.NETWORK_TYPE + ","+(Global.OPERATOR_NAME+"#"+ Global.OPERATOR_NAME_SET) +","+(Global.LOCATION_ADDRESS.replaceAll(",", "_") + "#" + Global.CURRENT_LOCATION_SET.replace(",", "_"))+ "," + Global.SIGNAL_STRENGTH+  ","+msg + ","+uid+",";
//		if (!Global.LOCATION_ADDRESS.equalsIgnoreCase("")) {
//		}else if (Global.LOCATION_ADDRESS.equalsIgnoreCase("")) {
//			message = Global.DEVICE_IMEI_NO + ","+ formattedDate +","+Global.latitude + ","+Global.longitude +  ","+msg + ","+uid + ","+Global.CURRENT_LOCATION_SET;
//		}
		
		
		Log.e("message", "==" + message);
		appendLog(message);
//		message = readandUploadLog();
		
	}

	public void appendLog(String text)
	{       
		File logFile = new File("sdcard/log.txt");
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
	
	public String readandUploadLog()
	{
		String output="";
		try {
			File file = new File("/sdcard/log.txt");
			if (file.exists())
			{

				DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuilder;
				docBuilder = docFactory.newDocumentBuilder();
				Document doc = docBuilder.newDocument();


				BufferedReader br = new BufferedReader(new FileReader(file));

				String line;
				try
				{   
					boolean rootadded=false;
					Element rootElement=null;
					while ((line = br.readLine()) != null){
						String[] values = line.split(",");
						if(!rootadded){
							rootElement = doc.createElement("imei");
							rootElement.setAttribute("number", values[0]);
							doc.appendChild(rootElement);
							rootadded=true;
						}
						Element tsele = doc.createElement("ts");
						tsele.setAttribute("timestamp", values[1]);
						rootElement.appendChild(tsele);

						Element latNode = doc.createElement("lat");
						latNode.appendChild(doc.createTextNode(values[2]));
						tsele.appendChild(latNode);

						Element langNode = doc.createElement("lang");
						langNode.appendChild(doc.createTextNode(values[3]));
						tsele.appendChild(langNode);

						Element errNode = doc.createElement("err");
						errNode.appendChild(doc.createTextNode(values[4]));
						tsele.appendChild(errNode);
						if(values.length>5){
							Element uidNode = doc.createElement("uid");
							uidNode.appendChild(doc.createTextNode(values[5]));
							tsele.appendChild(uidNode);
						}
					}
					if(rootadded)  { 
						TransformerFactory transformerFactory = TransformerFactory
								.newInstance();
						Transformer transformer = transformerFactory.newTransformer();
						DOMSource source = new DOMSource(doc);

						StringWriter writer = new StringWriter();
						StreamResult result = new StreamResult(writer);
						transformer.transform(source, result);
						output = writer.getBuffer().toString()
								.replaceAll("\n|\r", "");

						return output;

					}
				}catch (Exception e)
				{
					e.printStackTrace();
				}
				finally {
					br.close();
				}
			}else{
//				showMyMessage("No Log to Upload.");
				Toast.makeText(myContext, "No Log to Upload.", Toast.LENGTH_SHORT).show();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return output;
	}
	
	private class UploadLogsToServer extends AsyncTask<String, Void, String>
	{

		@Override
		protected String doInBackground(String... params) {
			String res = "";
			res = CommonMethods.HttpPostLifeCerticiate(Global.LOG_URL,message,"","");
			return res;
		}
		@Override
		protected void onPostExecute(String result) {
			dialogWaitForAuth.dismiss();
			if (result.equalsIgnoreCase("ERROR") || result.equalsIgnoreCase("False from server") || result.equalsIgnoreCase("Connection time out Error")) {
				//				ShowErrorMessage(result);
				myMessage(result, "");
			}else {
				File myFile = new File("/sdcard/log.txt");
				boolean deleted = myFile.delete();
				if (deleted) {
					Toast.makeText(myContext,
							"File Uploaded successfully.",
							Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(myContext,
							"File uploading failed.",
							Toast.LENGTH_SHORT).show();
				}
			}
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialogWaitForAuth.show();
		}
	}

}
