package com.nhpm.Utility;
import android.util.Base64;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AESEncryption {
	private static SecretKeySpec secretKey;
    private static byte[] key;
    
  /*  public static void main(String[] args) {
    	//final String secretKey = "9VMUFeMKCRD+5DKSNAaLfSVeYpWXq7n6naaIt8uydpCx9yOQm97UmwkAYxiSSsEv";
    	//final String secretKey = "XuaObPFDlsOeywEMhcPIFHYbW+wJeTdaxz+lMcNbMSWHBkVrSKf2+uTpFBE9QaJr";

       // String originalString = "NHPM";
        //String encryptedString = AESEncryption.encrypt(originalString, secretKey) ;
     //   String decryptedString = AESEncryption.decrypt(secretKey, originalString) ;
       // String decryptedString = AESEncryption.decrypt(secretKey, "NHPM") ;

       // System.out.println(originalString);
        //System.out.println(encryptedString);
     //   System.out.println(decryptedString);
        
        String authToken = UUID.randomUUID().toString();
        System.out.println(authToken);
	}*/
 
    public static void setKey(String myKey)
    {
        MessageDigest sha = null;
        try {
            key = myKey.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, "AES");
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
 
    public static String encrypt(String strToEncrypt, String secret)
    {
        try
        {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")),Base64.DEFAULT);
        }
        catch (Exception e)
        {
            System.out.println("Error while encrypting: " + e.toString());
        }
        return null;
    }
 
    public static String decrypt(String strToDecrypt, String secret)
    {
        try
        {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
           // Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.decode(strToDecrypt,Base64.DEFAULT)));

        }
        catch (Exception e)
        {
            AppUtility.writeFileToStorage(e.toString(), "Exception");
            System.out.println("Error while decrypting: " + e.toString());
        }
        return null;
    }
}
