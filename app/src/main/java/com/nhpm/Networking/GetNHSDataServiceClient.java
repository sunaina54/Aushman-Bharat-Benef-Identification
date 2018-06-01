package com.nhpm.Networking;

/**
 * Created by Priyanka PC on 31-01-2016.
 */

import android.content.Context;
import android.util.Log;

import com.android.volley.toolbox.JsonObjectRequest;
import com.nhpm.ReqRespModels.MasterNHSdata;
import com.nhpm.ReqRespModels.NHSDataRequest;
import com.nhpm.Utility.URLConstants;

import org.json.JSONException;
import org.json.JSONObject;


public class GetNHSDataServiceClient extends BaseServiceClient {



    private final NHSDataRequest _NHSdataRequest;

    public GetNHSDataServiceClient(NHSDataRequest request) {
        super(NetworkRequestType.getNHSData);
        _NHSdataRequest = request;
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
            payload = getJsonPayload(_NHSdataRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = createJsonObjectRequest(NetworkMethodType.Post, URLConstants.GetNHSData_URL,
                payload, handler, MasterNHSdata.class,null);

        queueNetworkRequest(context, jsonObjectRequest);
    }

    private void validateRequest() throws ValidationException {


    }
}





