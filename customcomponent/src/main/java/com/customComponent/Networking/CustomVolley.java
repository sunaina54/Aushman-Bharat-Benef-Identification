package com.customComponent.Networking;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.customComponent.R;
import com.customComponent.utility.ProjectPrefrence;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Anand on 02-06-2016.
 */
public class CustomVolley {

    private JsonObjectRequest jsonObjReq;
    private String msg;
    private VolleyTaskListener listener;
    private String URL;
    private String requestBody;
    private String headers;
    private Context context;
    private String headerValue;
    private int requestTimeout = 20000;
    private int requestFailureCount = 1;
    private int backOffMulti = 1;
    private ProgressDialog mProgDialog;


    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getHeaders() {
        return headers;
    }

    public void setHeaders(String headers) {
        this.headers = headers;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getHeaderValue() {
        return headerValue;
    }

    public void setHeaderValue(String headerValue) {
        this.headerValue = headerValue;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public CustomVolley() {
    }

    public CustomVolley(VolleyTaskListener taskListener, String url, String requestBody, String header, String headerValue, Context context) {
        URL = url;
        this.headers = header;
        this.requestBody = requestBody;
        this.listener = taskListener;
        this.context = context;
        this.headerValue = headerValue;

       /* if (progressMessage != null){
            mProgDialog = new ProgressDialog(context);
            this.mProgDialog.setMessage(progressMessage);
            // this.mTaskListener = taskListener;
            this.mProgDialog.setCancelable(false);
        }*/
    }


    public CustomVolley(VolleyTaskListener taskListener, String progressMessage, String url, String requestBody, String header, String headerValue, Context context) {
        URL = url;
        this.headers = header;
        this.requestBody = requestBody;
        this.listener = taskListener;
        this.context = context;
        this.headerValue = headerValue;

        if (progressMessage != null) {
            mProgDialog = new ProgressDialog(context);
            this.mProgDialog.setMessage(progressMessage);
            this.mProgDialog.setIndeterminateDrawable(context.getResources().getDrawable(R.mipmap.nhps_logo));
            // this.mTaskListener = taskListener;
            this.mProgDialog.setCancelable(false);
        }
    }

    public void execute() {
        final String REQUESTS_TAG = "Request-Demo";
        if (mProgDialog != null) {
            mProgDialog.show();
        }
        RequestQueue rq = Volley.newRequestQueue(context);
        try {

            jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    URL, new JSONObject(requestBody), //Not null.
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(final JSONObject response) {
                            if (mProgDialog != null && mProgDialog.isShowing()) {
                                mProgDialog.dismiss();
                            }
                            Log.i("Custom Volley", "Custom Volley Response : " + response.toString());
                            // String utf8String = new String(response.toString().getBytes(), "UTF-8");
                            String utfResp = fixEncoding(response.toString());
                            listener.postExecute(utfResp);
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("Custom Volley", "Custom Volley error : " + error.getMessage());

                    listener.onError(error);
                    if (mProgDialog != null && mProgDialog.isShowing()) {
                        mProgDialog.dismiss();
                    }
                }
            }
            ) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    // params.put("Content-Type", "application/json");
                    params.put("Content-Type", "application/json; charset=utf-8");
                    params.put("Accept", "application/json");
                    if (headers != null && headerValue != null) {
                        params.put(headers, headerValue);
                    }

                    return params;
                }

                @Override
                protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                    try {
                        Log.d("HEADERS", response.headers + "");
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


            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                    requestTimeout,
                    requestFailureCount,
                    backOffMulti));

// Adding request to request queue
            rq.add(jsonObjReq);
        } catch (JSONException e) {
            Log.i("Custom Volley ", "Custom Volley Excpetion : " + e.toString());
        }
    }

    private String fixEncoding(String response) {
        try {
            byte[] u = response.toString().getBytes(
                    "ISO-8859-1");
            response = new String(u, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
        return response;
    }


}

