package com.pointman.mobiledesigner;

public class CommonUtils {

	
	 public static String toHexString(byte[] buffer) {

	        String bufferString = "";

	        for (int i = 0; i < buffer.length; i++) {

	            String hexChar = Integer.toHexString(buffer[i] & 0xFF);
	            if (hexChar.length() == 1) {
	                hexChar = "0" + hexChar;
	            }

	            //bufferString += hexChar.toUpperCase() + " ";
	            bufferString += hexChar.toUpperCase() ;
	        }

	        return bufferString;
	    }
}
