package com.nhpm.Networking;

/**
 * Response. Created on 17-09-2015.
 */
public class NetworkResponse {

   // private static final int[] SERVER_SUCCESS_RESPONSE_CODE = {1,0,-4,2,5};

  //  public int responseStatus=-1;

    public String operation,errorMessage;
    public boolean status;

    public boolean isSuccessful() {

        boolean isSuccessful = false;


            if (status) {
                isSuccessful = true;

            }


        return isSuccessful;
    }
}
