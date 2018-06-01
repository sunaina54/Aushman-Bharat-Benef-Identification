package com.customComponent.Networking;

/**
 * BaseServiceClient. Created on 10-12-2015.
 */

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;


import com.customComponent.utility.URLConstants;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * BaseServiceClient. Created on 08-09-2015.
 */
public abstract class BaseServiceClient {

    public NetworkRequestType _networkRequestType;

    public static int REQUEST_TIMEOUT_MS = 60000;
    private Context _context;

    public BaseServiceClient(NetworkRequestType requestType) {
        this._networkRequestType = requestType;
    }

    public abstract void fireNetworkRequest(Context context, INetworkResponseHandler handler) throws
            ValidationException;

    public void fireNetworkRequest(Context context) throws ValidationException {
        this.fireNetworkRequest(context, null);

        this._context = context;
    }

    protected <T extends NetworkResponse> JsonObjectRequest createJsonObjectRequest(NetworkMethodType methodType,
                                                                                    String fullUrl, JSONObject
            payLoad, final
                                                                                    INetworkResponseHandler handler,
                                                                                    final Type responseType, final String header) {
        Response.Listener listener = null;
        Response.ErrorListener errorListener = null;
        if (handler != null) {
            listener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    T t = parseJsonResponse(response, responseType);
                    handler.onNetworkResponse(_networkRequestType, t);
                }
            };

            errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    NetworkResponse networkResponse = new NetworkResponse();

                    networkResponse.message = error.getMessage();
                    Log.d("ERRR", error.networkResponse + "" + error.getMessage());

                    networkResponse.message = "Please Connect to Internet";
                    /*if (!isNetworkAvailable()) {
                        networkResponse.statusMessage = "Please Connect to Internet";
                    }*/

                    handler.onNetworkResponse(_networkRequestType, networkResponse);
                }
            };
        }

        Log.d(getClass().getSimpleName(), "Request URL: " + fullUrl);

        JsonObjectRequest request = new JsonObjectRequest(methodType.getVolleyMethodType(), fullUrl, payLoad,
                listener, errorListener)
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("Accept", "application/json");
                if(header!=null)
                {
                    params.put("sessionId", header);
                }

                return params;
            }
            @Override
            protected Response<JSONObject> parseNetworkResponse(com.android.volley.NetworkResponse response) {
                try {
                    Log.d("HEADERS",response.headers+"");
                    String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers));
                    JSONObject jsonResponse = new JSONObject(jsonString);
                    jsonResponse.put("headers", new JSONObject(response.headers));
                    return Response.success(jsonResponse,
                            HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                } catch (JSONException je) {
                    return Response.error(new ParseError(je));
                }
            }

        };

        request.setRetryPolicy(new DefaultRetryPolicy(REQUEST_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy
                        .DEFAULT_BACKOFF_MULT));




        return request;
    }

    protected void queueNetworkRequest(Context context, JsonObjectRequest jsonObjectRequest) {
        NetworkOperationHelper.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    protected <T> T parseJsonResponse(JSONObject response, Type type) {
        T result;
        Gson gson = new Gson();
        Log.d(getClass().getSimpleName(), "parseJsonRespnse: " + response.toString());
        result = gson.fromJson(response.toString(), type);
        return result;
    }

    protected String getFullServiceAddress(String endServiceUrl) {
        Uri.Builder builder = Uri.parse(getBaseServiceAddress()).buildUpon();
        builder.appendEncodedPath(endServiceUrl);
        return builder.toString();
    }

    protected String getBaseServiceAddress() {
        // later fetch this from the preference manager based upon the currently selected env(qa/test/prod/dev etc..)
        return URLConstants.BASE_URL;
    }

    protected JSONObject getJsonPayload(Object obj) throws JSONException {
        Gson gson = new Gson();
        String jsonString = gson.toJson(obj);
        Log.d(getClass().getSimpleName(), "getJsonPayload: " + jsonString);
        return new JSONObject(jsonString);
    }

    public static ImageLoader getImageLoader(Context context) {
        return NetworkOperationHelper.getInstance(context).getImageLoader();
    }

    public enum NetworkMethodType {
        Get, Post, Put;

        public int getVolleyMethodType() {
            int method;
            switch (this) {
                case Get:
                    method = Request.Method.GET;
                    break;
                case Post:
                    method = Request.Method.POST;
                    break;
                case Put:
                    method = Request.Method.PUT;
                    break;
                default:
                    method = Request.Method.GET;
                    break;
            }

            return method;
        }
    }

    public class ValidationException extends Exception {
        public ValidationException(String validationMessage) {
            this.validationErrorMessage = validationMessage;
        }

        public String validationErrorMessage;
    }

  /*  public boolean isNetworkAvailable() {
        ConnectivityManager connec = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);

        // Check for network connections
        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||

                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {
            //Toast.makeText(this, " Connected ", Toast.LENGTH_LONG).show();
            return true;
        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {
            // Toast.makeText(this, " Not Connected ", Toast.LENGTH_LONG).show();
            return false;
        }
        return false;
    }
*/
}
