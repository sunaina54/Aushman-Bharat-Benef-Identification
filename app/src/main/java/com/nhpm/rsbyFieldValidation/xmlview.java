package com.nhpm.rsbyFieldValidation;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.nhpm.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

public class xmlview extends Activity {
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.rsbyxmlview);
        
        
        TextView we = (TextView)findViewById(R.id.textView1);
   //     we.setText("<?xml version='1.0' encoding='UTF-8' standalone='yes' ?><RsbySmartCardDetails>  <urn id=\"32060500913000049\">    <issueTimeSpam>28032014181612</issueTimeSpam>  </urn>  <familydetails hofnamereg=\"???????\">    <doorhouse>316</doorhouse>    <villagecode>00046910</villagecode>    <panchyattowncode>0580</panchyattowncode>    <blockcode>0098</blockcode>    <districtcode>06</districtcode>    <statecode>32</statecode>  </familydetails>  <allfamilymember>    <familymember>      <memid>01</memid>      <name>Pathmini</name>      <dob>01031969</dob>      <gender>2</gender>      <relcode>01</relcode>    </familymember>    <familymember>      <memid>03</memid>      <name>Mohanan</name>      <dob>01031982</dob>      <gender>1</gender>      <relcode>15</relcode>    </familymember>    <familymember>      <memid>04</memid>      <name>Kunchu</name>      <dob>01031944</dob>      <gender>2</gender>      <relcode>04</relcode>    </familymember>    <familymember>      <memid>05</memid>      <name>rathnakumari</name>      <dob>01031991</dob>      <gender>2</gender>      <relcode>17</relcode>    </familymember>    <familymember>      <memid>06</memid>      <name>Sreenandha</name>      <dob>01032010</dob>      <gender>2</gender>      <relcode>06</relcode>    </familymember>    <familymember>      <memid>02</memid>      <name>Family of Pathmini</name>      <dob>01031814</dob>      <gender>1</gender>      <relcode>02</relcode>    </familymember>    <familymember>      <memid>07</memid>      <name>Sreeshma</name>      <dob>01032011</dob>      <gender>1</gender>      <relcode>11</relcode>    </familymember>    <familymember>      <memid>08</memid>      <name>Balakrishnan</name>      <dob>01031974</dob>      <gender>1</gender>      <relcode>15</relcode>    </familymember>  </allfamilymember>  <minutiadetials>    <memberid>01</memberid>    <fingerid>01</fingerid>    <mtemplen>01A6</mtemplen>  </minutiadetials>  <policydetails>    <insccode>13</insccode>    <policyno>00002209332838000000</policyno>    <policyamt>00030000</policyamt>    <startdate>01042013</startdate>    <enddate>31032014</enddate>  </policydetails>  <cardbal>    <balance>00030000</balance>    <balmoddate>28032014</balmoddate>    <addonbal>0000000</addonbal>    <addonbalmoddate>0000000</addonbalmoddate>  </cardbal>  <familyphotobase64>    <base64string>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</base64string>  </familyphotobase64></RsbySmartCardDetails>");
        
        try {
			we.setText (  getStringFromFile("/mnt/sdcard/rsby.xml"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
        
      //  we.setText("123");
        

        
        
        
	}
	
	public static String convertStreamToString(InputStream is) throws Exception {
	    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	    StringBuilder sb = new StringBuilder();
	    String line = null;
	    while ((line = reader.readLine()) != null) {
	      sb.append(line).append("\n");
	    }
	    reader.close();
	    return sb.toString();
	}

	public static String getStringFromFile (String filePath) throws Exception {
	    File fl = new File(filePath);
	    FileInputStream fin = new FileInputStream(fl);
	    String ret = convertStreamToString(fin);
	    //Make sure you close all streams.
	    fin.close();        
	    return ret;
	}

}
