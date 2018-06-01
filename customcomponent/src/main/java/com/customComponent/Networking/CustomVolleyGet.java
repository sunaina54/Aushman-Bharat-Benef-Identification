package com.customComponent.Networking;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

/**
 * Created by Anand on 18-11-2016.
 */
public class CustomVolleyGet {
    private String JSON_URL;
    private Context context;
    private VolleyTaskListener taskListener;
    private ProgressDialog mProgDialog;
    private String progressMessage;

    public CustomVolleyGet(VolleyTaskListener taskLister,String progressMsg,String url,Context context){
        JSON_URL=url;
        this.context=context;
        this.taskListener=taskLister;
        this.progressMessage=progressMsg;
        if (progressMessage != null){
            mProgDialog = new ProgressDialog(context);
            this.mProgDialog.setMessage(progressMessage);
            // this.mTaskListener = taskListener;
            this.mProgDialog.setCancelable(false);
        }


    }


    public void execute(){
        if(mProgDialog!=null){
            mProgDialog.show();
        }
        StringRequest stringRequest = new StringRequest(JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       // showJSON(response);
                      //  Log.d("Custom Volley : ","Json Response : "+response);
                        if(mProgDialog!=null && mProgDialog.isShowing()){
                            mProgDialog.dismiss();
                        }
                        taskListener.postExecute(response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                       // Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                        if(mProgDialog !=null && mProgDialog.isShowing()) {
                            mProgDialog.dismiss();
                        }
                        taskListener.onError(error);

                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        stringRequest.setShouldCache(false);
        requestQueue.add(stringRequest);
    }
}
