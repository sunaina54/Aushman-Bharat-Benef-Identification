package com.nhpm.Networking;

/**
 * Created by Priyanka PC on 31-01-2016.
 */

import android.content.Context;
import android.util.Log;

import com.android.volley.toolbox.JsonObjectRequest;
import com.nhpm.ReqRespModels.AddMemberResponse;
import com.nhpm.ReqRespModels.GetLocationRequest;
import com.nhpm.ReqRespModels.MasterLocation;
import com.nhpm.ReqRespModels.NhsDataList;
import com.nhpm.Utility.URLConstants;

import org.json.JSONException;
import org.json.JSONObject;


public class AddMemberServiceClient extends BaseServiceClient {



    private final NhsDataList _addMemberRequest;

    public AddMemberServiceClient(NhsDataList request) {
        super(NetworkRequestType.addMember);
        _addMemberRequest = request;
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
            payload = getJsonPayload(_addMemberRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = createJsonObjectRequest(NetworkMethodType.Post, URLConstants.ADDMEMBER_URL,
                payload, handler, AddMemberResponse.class,null);

        queueNetworkRequest(context, jsonObjectRequest);
    }

    private void validateRequest() throws ValidationException {


    }
}





