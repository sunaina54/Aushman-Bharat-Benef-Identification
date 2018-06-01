package com.customComponent.utility;

/**
 * Created by Anand on 23-03-2016.
 */
public class PasswordValidateUtil {

    public static boolean validatePassword(String password){
        if(password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$")){
         //   System.out.println("Password true : "+password);
            return true;
        }else{
          //  System.out.println("Password false : "+password);
            return false;
        }
    }
}
