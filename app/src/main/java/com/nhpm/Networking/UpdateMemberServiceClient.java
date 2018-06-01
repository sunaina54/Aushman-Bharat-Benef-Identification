package com.nhpm.Networking;

/**
 * Created by Priyanka PC on 31-01-2016.
 */

import android.content.Context;
import android.util.Log;

import com.android.volley.toolbox.JsonObjectRequest;
import com.nhpm.ReqRespModels.UpdateRequest;
import com.nhpm.Utility.URLConstants;

import org.json.JSONException;
import org.json.JSONObject;


public class UpdateMemberServiceClient extends BaseServiceClient {



    private final UpdateRequest _updateRequest;

    public UpdateMemberServiceClient(UpdateRequest request) {
        super(NetworkRequestType.updateMember);
        _updateRequest = request;
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
            payload = getJsonPayload(_updateRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = createJsonObjectRequest(NetworkMethodType.Post, URLConstants.UpdateMember_URL,
                payload, handler, NetworkResponse.class,null);

        queueNetworkRequest(context, jsonObjectRequest);
    }

    private void validateRequest() throws ValidationException {


    }
}





