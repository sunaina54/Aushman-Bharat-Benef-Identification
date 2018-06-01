package com.nhpm.Networking;

/**
 * Created by Priyanka PC on 31-01-2016.
 */

import android.content.Context;
import android.util.Log;

import com.android.volley.toolbox.JsonObjectRequest;
import com.nhpm.ReqRespModels.GetLocationRequest;
import com.nhpm.ReqRespModels.MasterLocation;
import com.nhpm.Utility.URLConstants;

import org.json.JSONException;
import org.json.JSONObject;


public class GetLocationDataServiceClient extends BaseServiceClient {



    private final GetLocationRequest _getLocationRequest;

    public GetLocationDataServiceClient(GetLocationRequest request) {
        super(NetworkRequestType.getLocationData);
        _getLocationRequest = request;
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
            payload = getJsonPayload(_getLocationRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = createJsonObjectRequest(NetworkMethodType.Post, URLConstants.GetLocationData_URL,
                payload, handler, MasterLocation.class,null);

        queueNetworkRequest(context, jsonObjectRequest);
    }

    private void validateRequest() throws ValidationException {


    }
}





