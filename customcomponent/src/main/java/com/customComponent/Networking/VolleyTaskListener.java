package com.customComponent.Networking;

import com.android.volley.VolleyError;

import org.json.JSONObject;

/**
 * Created by Anand on 02-06-2016.
 */
public interface VolleyTaskListener {
   // void preExecute();
    void postExecute(String response);
  //  void doInBackground();
    void onError(VolleyError error);
}
