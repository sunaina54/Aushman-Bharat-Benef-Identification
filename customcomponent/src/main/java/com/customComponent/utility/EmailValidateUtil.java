package com.customComponent.utility;

/**
 * Created by Anand on 24-08-2016.
 */
public class EmailValidateUtil {
    public static boolean isValidEmail(String email){
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
           return false;
        }
        return true;
    }

    public static boolean isGmail(String email){
       int index= email.indexOf("@");
        String emailDomain=email.substring(index);
        if(emailDomain.equalsIgnoreCase("@gmail.com")){
            return true;
        }
        return false;
    }
}
