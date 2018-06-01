package com.nhpm.Networking;

/**
 * Created by Priyanka PC on 31-01-2016.
 */

import android.content.Context;
import android.util.Log;

import com.android.volley.toolbox.JsonObjectRequest;
import com.nhpm.ReqRespModels.LoginRequest;
import com.nhpm.ReqRespModels.LoginResponse;
import com.nhpm.Utility.URLConstants;

import org.json.JSONException;
import org.json.JSONObject;


public class LoginServiceClient extends BaseServiceClient {



    private final LoginRequest _loginRequest;

    public LoginServiceClient(LoginRequest request) {
        super(NetworkRequestType.login);
        _loginRequest = request;
    }

    @Override
    public void fireNetworkRequest(Context context, INetworkResponseHandler handler) throws ValidationException {
        if (context == null) {
            Log.e(getClass().getName(), "fireNetworkRequest: required context object");
            return;
        }

        validateRequest();

        JSONObject payload = null;
        try {
            payload = getJsonPayload(_loginRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = createJsonObjectRequest(NetworkMethodType.Post, URLConstants.Login_URL,
                payload, handler, LoginResponse.class,null);

        queueNetworkRequest(context, jsonObjectRequest);
    }

    private void validateRequest() throws ValidationException {


    }
}





