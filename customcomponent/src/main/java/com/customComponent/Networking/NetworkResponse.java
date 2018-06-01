package com.customComponent.Networking;

/**
 * Response. Created on 17-09-2015.
 */
public class NetworkResponse {

    private static final int[] SERVER_SUCCESS_RESPONSE_CODE = {1,0,-4,2,5};

    public int responseStatus=-1;

    public String message;

    public boolean isSuccessful() {

        boolean isSuccessful = false;

        for (int successCode : SERVER_SUCCESS_RESPONSE_CODE) {
            if (responseStatus == successCode) {
                isSuccessful = true;
                break;
            }
        }

        return isSuccessful;
    }
}
