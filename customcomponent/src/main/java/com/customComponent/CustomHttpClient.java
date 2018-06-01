package com.customComponent;

import android.net.Uri;
import android.util.Log;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

public class CustomHttpClient {

    public static final String USER_AGENT = "Mozilla/5.0";
    static String TAG = " Custom HttpClientClass ";

    static {

    }

    public static String postStringRequest1(String url, String json) throws Exception {
        DefaultHttpClient mHttpClient = new DefaultHttpClient();
        String inputStream = null;
        String[] str = new String[2];

        HttpPost mHttpPost = new HttpPost(url);
        mHttpPost.addHeader("Authorization", "Basic b3ZhbWJhcGxhdGZvcm11c2VybmFtZTp3aGF0a2luZG9mcGFzc3dvcmRpc3RoaXM=");
//			mHttpPost.addHeader("sessionId","faghsdfha");

        StringEntity se = new StringEntity(json, HTTP.UTF_8);
        se.setContentType("application/json");
        //		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        //	    nameValuePairs.add(new BasicNameValuePair("xml",xml));
        Log.i("", TAG + "here...");
        mHttpPost.setEntity(se);
        //	    form = new UrlEncodedFormEntity(nameValuePairs,"UTF-8");
        //		mHttpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));
        HttpParams httpParameters = mHttpPost.getParams();
        // Set the timeout in milliseconds until a connection is
        // established.
        int timeoutConnection = 30000;
        HttpConnectionParams.setConnectionTimeout(httpParameters,
                timeoutConnection);
        // Set the default socket timeout (SO_TIMEOUT)
        // in milliseconds which is the timeout for waiting for data.
        int timeoutSocket = 30000;
        HttpConnectionParams
                .setSoTimeout(httpParameters, timeoutSocket);

        HttpResponse response = mHttpClient.execute(mHttpPost);
            /*Header[] headers=response.getHeaders("sessionId");
            String headerVal = null;
			for(Header header: headers){
				if(header!=null){
					ContentManager.sessionId=header.getValue();
				}

			}*/


        HttpEntity resEntity = response.getEntity();

        Log.i("", TAG + "here...2");

			/*String resp = EntityUtils.toString(resEntity);
        Log.i(TAG,"postSyncXML srv response:"+resp);*/
        inputStream = EntityUtils.toString(response.getEntity());
        //Log.i("",TAG+"here...3"+inputStream);


        return inputStream;
    }
    public static InputStream getInputStreamRequest(String url) {
        InputStream inputStreamResponse = null;
        //		Uri uri=new Uri(url);
        HttpGet mHttpGet = new HttpGet(url);

        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpResponse response = null;
        try {
            response = httpclient.execute(mHttpGet);
            inputStreamResponse = response.getEntity().getContent();
            //			Log.i("",TAG+"[ GET REQUEST METHOD ] : "+EntityUtils.toString(response.getEntity()));
        } catch (Exception e) {
            Log.i("", TAG + "[ GET REQUEST METHOD ] [Exception] : " + e.toString());
            inputStreamResponse = null;
        }

        return inputStreamResponse;

    }
    public static int postMundipaggRequest(String url, String xml) {

        DefaultHttpClient mHttpClient = new DefaultHttpClient();
        InputStream inputStream = null;
        int responseCode = 0;
        try {
            HttpPost mHttpPost = new HttpPost(url);


            //			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            //	        nameValuePairs.add(new BasicNameValuePair("MerchantKey", "1ef11b35-c8d0-4ef2-bb6e-db04a8fae49d"));
            //	        mHttpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            mHttpPost.addHeader("Content-Type", "text/xml");
            mHttpPost.addHeader("MerchantKey", "1ef11b35-c8d0-4ef2-bb6e-db04a8fae49d");
            StringEntity se = new StringEntity(xml, HTTP.UTF_8);
            //			se.setContentType("text/xml");
            mHttpPost.setEntity(se);

            HttpResponse response = mHttpClient.execute(mHttpPost);
            //
            //			 inputStream = response.getEntity().getContent();

            responseCode = response.getStatusLine().getStatusCode();
            Log.i("", TAG + "Mundipagg Response : " + EntityUtils.toString(response.getEntity()));
        } catch (Exception e) {
            Log.i("", TAG + "[postInputStreamRequest]" + e.toString());
        }
        return responseCode;

    }
    public static InputStream postInputStreamRequest(String url, String xml) {

        DefaultHttpClient mHttpClient = new DefaultHttpClient();
        InputStream inputStream = null;
        try {
            HttpPost mHttpPost = new HttpPost(url);
            mHttpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
            StringEntity se = new StringEntity(xml);
            se.setContentType("text/xml");
            //		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            //	    nameValuePairs.add(new BasicNameValuePair("xml",xml));

            mHttpPost.setEntity(se);
            //	    form = new UrlEncodedFormEntity(nameValuePairs,"UTF-8");
            //		mHttpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));

            HttpResponse response = mHttpClient.execute(mHttpPost);

            HttpEntity resEntity = response.getEntity();

			/*String resp = EntityUtils.toString(resEntity);
        Log.i(TAG,"postSyncXML srv response:"+resp);*/
            inputStream = response.getEntity().getContent();
        } catch (Exception e) {
            Log.i("", TAG + "[postInputStreamRequest]" + e.toString());
        }
        return inputStream;

    }
    public static String getStringRequest(String url) {
        String stringResponse = null;
        try {
       /*String query = URLEncoder.encode(url, "utf-8");
            Log.i(TAG, "[ GET REQUEST METHOD ] [Exception] : " + query);
*/
            HttpGet mHttpGet = new HttpGet(url);

        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpResponse response = null;

            response = httpclient.execute(mHttpGet);
            stringResponse = EntityUtils.toString(response.getEntity());
            Log.i(TAG, "[ GET REQUEST METHOD ] [Exception] : " + stringResponse);
        } catch (Exception e) {
            Log.i(TAG, "[ GET REQUEST METHOD ] [Exception] : " + e.toString());
            stringResponse = null;
        }

        return stringResponse;
    }
    public static String postStringRequestWithHeader(String url, String json) {
        DefaultHttpClient mHttpClient = new DefaultHttpClient();
        String inputStream = null;
        String[] str = new String[2];
        try {
            HttpPost mHttpPost = new HttpPost(url);
//			mHttpPost.addHeader("sessionId",ContentManager.sessionId);
            StringEntity se = new StringEntity(json, HTTP.UTF_8);
            se.setContentType("application/json");
            //		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            //	    nameValuePairs.add(new BasicNameValuePair("xml",xml));
            Log.i("", TAG + "here...");
            mHttpPost.setEntity(se);
            //	    form = new UrlEncodedFormEntity(nameValuePairs,"UTF-8");
            //		mHttpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));

            HttpResponse response = mHttpClient.execute(mHttpPost);
            Header[] headers = response.getHeaders("sessionId");
            String headerVal = null;
            for (Header header : headers) {
                if (header != null) {
//					ContentManager.sessionId=header.getValue();
                    /*SalesUser user=SettingPrefrence.getUserPrefrence(AppUtility.mContext);
                    user.setSessionId(header.getValue());
					SettingPrefrence.saveUserPrefrence(user, AppUtility.mContext);*/

                }

            }


            HttpEntity resEntity = response.getEntity();

            Log.i("", TAG + "here...2");

			/*String resp = EntityUtils.toString(resEntity);
        Log.i(TAG,"postSyncXML srv response:"+resp);*/
            inputStream = EntityUtils.toString(response.getEntity());
            Log.i("", TAG + "here...3" + inputStream);

        } catch (Exception e) {
            Log.i("", TAG + "ffffffffffffffffffffffffff" + e.toString());
        }
        return inputStream;
    }
    public static String postRequest(String _url, String json, String contentType) throws Exception {

        //StringBuilder params=new StringBuilder("");
        HashMap<String, String> response = null;
        StringBuffer responseString = null;

        String url = _url;
        URL obj = new URL(_url);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "UTF-8");
        //con.setRequestProperty("Accept-Language", "UTF-8");
        if (contentType.equalsIgnoreCase("xml"))
            con.setRequestProperty("content-type", "application/x-www-form-urlencoded");
        else if (contentType.equalsIgnoreCase("json"))
            con.setRequestProperty("content-type", "application/json");
        else {

        }
        con.setDoOutput(true);
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(con.getOutputStream());
        outputStreamWriter.write(json);
        outputStreamWriter.flush();

        int responseCode = con.getResponseCode();
        if (responseCode == 100) {
            System.out.println("\nSending 'POST' request to URL : " + url);
            System.out.println("Post parameters : " + json);
            System.out.println("Response Code : " + responseCode);
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            responseString = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                responseString.append(inputLine + "\n");
            }
            in.close();
            //.put("response",responseString.toString());
            return responseString.toString();
        }
        return null;
    }
    public static String postRequest(String _url, String json, HashMap<String, String> headers, String contentType) {


        //StringBuilder params=new StringBuilder("");
        String result = "";
        try {
            /*for(String s:parameter.keySet()){
				params.append("&"+s+"=");

				params.append(URLEncoder.encode(parameter.get(s),"UTF-8"));
			}*/


            String url = _url;
            URL obj = new URL(_url);
            HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", USER_AGENT);
            con.setRequestProperty("Accept-Language", "UTF-8");

            if (contentType.equalsIgnoreCase("xml"))
                con.setRequestProperty("content-type", "application/x-www-form-urlencoded");
            else if (contentType.equalsIgnoreCase("json"))
                con.setRequestProperty("content-type", "application/json");
            else {

            }

            for (String keyIndex : headers.keySet()) {
                String key = keyIndex;
                String value = headers.get(keyIndex);
                con.addRequestProperty(key, value);
            }
            con.setDoOutput(true);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(con.getOutputStream());
            outputStreamWriter.write(json);
            outputStreamWriter.flush();

            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'POST' request to URL : " + url);
            System.out.println("Post parameters : " + json);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine + "\n");
            }
            in.close();

            result = response.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return result;
        }
    }
    public static String postStringRequest(String url, String json) {
        DefaultHttpClient mHttpClient = new DefaultHttpClient();
        String inputStream = null;
        String[] str = new String[2];
        try {
            HttpPost mHttpPost = new HttpPost(url);
//			mHttpPost.addHeader("sessionId",SettingPrefrence.getUserPrefrence(MainApplication.mContext).getSessionId());
//			mHttpPost.addHeader("sessionId","faghsdfha");

            StringEntity se = new StringEntity(json, HTTP.UTF_8);
            se.setContentType("application/json");
            //		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            //	    nameValuePairs.add(new BasicNameValuePair("xml",xml));
            Log.i("", TAG + "here...");
            mHttpPost.setEntity(se);
            //	    form = new UrlEncodedFormEntity(nameValuePairs,"UTF-8");
            //		mHttpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));

            HttpResponse response = mHttpClient.execute(mHttpPost);
			/*Header[] headers=response.getHeaders("sessionId");
			String headerVal = null;
			for(Header header: headers){
				if(header!=null){
					ContentManager.sessionId=header.getValue();
				}
				
			}*/


            HttpEntity resEntity = response.getEntity();

            Log.i("", TAG + "here...2");

			/*String resp = EntityUtils.toString(resEntity);
        Log.i(TAG,"postSyncXML srv response:"+resp);*/
            inputStream = EntityUtils.toString(response.getEntity());
            Log.i("", TAG + "here...3" + inputStream);

        } catch (Exception e) {
            Log.i("", TAG + "ffffffffffffffffffffffffff" + e.toString());
        }
        return inputStream;
    }
    public static InputStream postStringRequest(String url) {
        return null;
    }
    public static InputStream postInputStreamRequest(String url) {

        DefaultHttpClient mHttpClient = new DefaultHttpClient();
        InputStream inputStream = null;
        try {
            HttpPost mHttpPost = new HttpPost(url);
            //  mHttpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
            //  StringEntity se = new StringEntity(xml);
            //  se.setContentType("text/xml");


            //  mHttpPost.setEntity(se);

            HttpResponse response = mHttpClient.execute(mHttpPost);

            inputStream = response.getEntity().getContent();
        } catch (Exception e) {
            Log.i("", TAG + "[postInputStreamRequest]" + e.toString());
        }
        return inputStream;

    }

    public static String postStringRequestWithTimeOut(String url, String json) {
        HttpParams httpParameters = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParameters, 30000);
        HttpConnectionParams.setSoTimeout(httpParameters, 30000);
        DefaultHttpClient mHttpClient = new DefaultHttpClient(httpParameters);
        String inputStream = null;
        String[] str = new String[2];
        try {
            HttpPost mHttpPost = new HttpPost(url);

            StringEntity se = new StringEntity(json, HTTP.UTF_8);
            se.setContentType("application/json");
            Log.i("", TAG + "here...");
            mHttpPost.setEntity(se);
            HttpResponse response = mHttpClient.execute(mHttpPost);
            HttpEntity resEntity = response.getEntity();
            Log.i("", TAG + "here...2");
            inputStream = EntityUtils.toString(response.getEntity());
            Log.i("", TAG + "here...3" + inputStream);
        } catch (Exception e) {
            Log.i("", TAG + "ffffffffffffffffffffffffff" + e.toString());
        }
        return inputStream;
    }
}
