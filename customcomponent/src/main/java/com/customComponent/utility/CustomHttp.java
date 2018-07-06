package com.customComponent.utility;

import android.util.Log;

import com.customComponent.Networking.MySSLSocketFactory;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by Anand on 27-04-2016.
 */
public class CustomHttp {

    public static HashMap<String,String> httpHeaderPost(String url,String requestBody,String requestHeader,String headerValue)throws Exception{
        HttpParams httpParameters = new BasicHttpParams();
        // Set the timeout in milliseconds until a connection is established.
        // The default value is zero, that means the timeout is not used.
        int timeoutConnection = 600000;
        HttpConnectionParams.setConnectionTimeout(httpParameters,
                timeoutConnection);
        // Set the default socket timeout (SO_TIMEOUT)
        // in milliseconds which is the timeout for waiting for data.
        int timeoutSocket = 5000;
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
        DefaultHttpClient mHttpClient = new DefaultHttpClient();
        mHttpClient.setParams(httpParameters);

        String inputStream = null;
        String[] str = new String[2];
        HashMap<String,String> responseMap=new HashMap<>();
        // try {
        HttpPost mHttpPost = new HttpPost(url);
        StringEntity se = new StringEntity(requestBody, HTTP.UTF_8);
        se.setContentType("application/json");

        mHttpPost.setEntity(se);
        HttpResponse response = mHttpClient.execute(mHttpPost);
        if(requestHeader!=null) {
            Header[] headers = response.getHeaders(requestHeader);
            for (Header header : headers) {
                if (header != null) {
                    headerValue = header.getValue();
                }
            }
            responseMap.put(requestHeader,headerValue);
        }
        // HttpEntity resEntity = response.getEntity();
      /*  BufferedReader reader = new BufferedReader(
                new InputStreamReader(in, "UTF-8"), 8);
        StringBuffer sb = new StringBuffer();
        String line = "";
        while ((line = reader.readLine()) != null) {
            sb.append(line + "\n");
        }

*/


        /*} catch (Exception e) {
            e.printStackTrace();
            responseMap.put("exception",e.toString());
        }*/
        inputStream = EntityUtils.toString(response.getEntity());
        responseMap.put("response",inputStream);
        return responseMap;
    }

    public static HashMap<String,String> httpPostWithHeader(String url,String requestBody,String[] header) throws Exception{
        DefaultHttpClient mHttpClient = new DefaultHttpClient();
        String inputStream = null;
        String[] str = new String[2];
        HashMap<String,String> responseMap=new HashMap<>();
        // try {
        HttpPost mHttpPost = new HttpPost(url);
        mHttpPost.setHeader(header[0],header[1]);

    /*    if(header!=null){
            for(int i=0;i<header.length;i++){
                String strHeader=header[i];
                String arr[]=strHeader.split("/");
                mHttpPost.addHeader(arr[0], arr[1]);
            }
        }*/
        StringEntity se = new StringEntity(requestBody, HTTP.UTF_8);
        se.setContentType("application/json");
      //  mHttpPost.setRequestProperty("Content-type", "application/json;charset=UTF-8");
        mHttpPost.setEntity(se);
        HttpResponse response = mHttpClient.execute(mHttpPost);
        HttpEntity resEntity = response.getEntity();
        inputStream = EntityUtils.toString(response.getEntity());

        responseMap.put("response",inputStream);
       /* } catch (Exception e) {
            e.printStackTrace();
            responseMap.put("exception",e.toString());
           // Log.i(TAG, "ffffffffffffffffffffffffff" + e.toString());
        }*/
        return responseMap;
    }

    public static HashMap<String,String> getStringRequest(String url, String header, String tokenValue) throws Exception {
        HttpClient mHttpClient = com.customComponent.Networking.MySSLSocketFactory.getNewHttpClient();
        String inputStream = null;
        HashMap<String,String> responseMap=new HashMap<>();
        String[] str = new String[2];
        try {
       /*String query = URLEncoder.encode(url, "utf-8");
            Log.i(TAG, "[ GET REQUEST METHOD ] [Exception] : " + query);
*/
            HttpGet mHttpGet = new HttpGet(url);
            mHttpGet.addHeader(header, tokenValue);

            //DefaultHttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = null;

            response = mHttpClient.execute(mHttpGet);
            inputStream = EntityUtils.toString(response.getEntity());
            responseMap.put("response",inputStream);
            //Log.d("Exception" + inputStream);
        } catch (Exception e) {
            Log.d("Error",e.toString());
            // Log.i(TAG, "[ GET REQUEST METHOD ] [Exception] : " + e.toString());
            inputStream = null;
        }

        return responseMap;
    }
    public static HashMap<String, String> httpGet(String url, String[] header) throws Exception {
        HttpClient mHttpClient = com.customComponent.Networking.MySSLSocketFactory.getNewHttpClient();
        String inputStream = null;
        //String[] str = new String[2];
        HashMap<String, String> responseMap = new HashMap<>();
        // try {
        HttpGet mHttpPost = new HttpGet(url);
        if (header != null) {
            for (int i = 0; i < header.length; i++) {
                String strHeader = header[i];
                String arr[] = strHeader.split("/");
                mHttpPost.addHeader(arr[0], arr[1]);
            }
        }
       /* StringEntity se = new StringEntity(requestBody, HTTP.UTF_8);
        se.setContentType("application/json");
        mHttpPost.setEntity(se);*/
        HttpResponse response = mHttpClient.execute(mHttpPost);
        //HttpEntity resEntity = response.getEntity();
        inputStream = EntityUtils.toString(response.getEntity());

        responseMap.put("response", inputStream);
       /* } catch (Exception e) {
            e.printStackTrace();
            responseMap.put("exception",e.toString());
           // Log.i(TAG, "ffffffffffffffffffffffffff" + e.toString());
        }*/
        return responseMap;
    }

    public static HashMap<String, String> httpPost1(String url, String requestBody, String requestHeader) {
        HashMap<String, String> responseMap = null;
        String inputStream = null;
        try {
            HttpParams httpParameters = new BasicHttpParams();
            // Set the timeout in milliseconds until a connection is established.
            // The default value is zero, that means the timeout is not used.
            int timeoutConnection = 600000;
            HttpConnectionParams.setConnectionTimeout(httpParameters,
                    timeoutConnection);
            // Set the default socket timeout (SO_TIMEOUT)
            // in milliseconds which is the timeout for waiting for data.
            int timeoutSocket = 5000;
            HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
            DefaultHttpClient mHttpClient = new DefaultHttpClient();
            mHttpClient.setParams(httpParameters);


            String[] str = new String[2];
            responseMap = new HashMap<>();
            // try {
            HttpPost mHttpPost = new HttpPost(url);
            StringEntity se = new StringEntity(requestBody, HTTP.UTF_8);
            se.setContentType("application/json");
            mHttpPost.setEntity(se);
            HttpResponse response = mHttpClient.execute(mHttpPost);
            if (requestHeader != null) {
                Header[] headers = response.getHeaders(requestHeader);
                String headerVal = null;
                for (Header header : headers) {
                    if (header != null) {
                        headerVal = header.getValue();
                    }
                }
                responseMap.put(requestHeader, headerVal);
            }
            // HttpEntity resEntity = response.getEntity();
      /*  BufferedReader reader = new BufferedReader(
                new InputStreamReader(in, "UTF-8"), 8);
        StringBuffer sb = new StringBuffer();
        String line = "";
        while ((line = reader.readLine()) != null) {
            sb.append(line + "\n");
        }

*/


        /*} catch (Exception e) {
            e.printStackTrace();
            responseMap.put("exception",e.toString());
        }*/
            inputStream = EntityUtils.toString(response.getEntity());
            responseMap.put("response", inputStream);
            return responseMap;
        } catch (OutOfMemoryError error) {
            responseMap.put("response", error.toString());
            return responseMap;
        } catch (Exception ex) {
            responseMap.put("response", ex.toString());
            return responseMap;
        }
    }


    public static HashMap<String, String> httpPost(String url, String requestBody, String[] header) throws Exception {
        DefaultHttpClient mHttpClient = new DefaultHttpClient();
        String inputStream = null;
        String[] str = new String[2];
        HashMap<String, String> responseMap = new HashMap<>();
        // try {
        HttpPost mHttpPost = new HttpPost(url);
        if (header != null) {
            for (int i = 0; i < header.length; i++) {
                String strHeader = header[i];
              //  String arr[] = strHeader.split("/");
                mHttpPost.addHeader(header[0], header[1]);
            }
        }
        StringEntity se = new StringEntity(requestBody, HTTP.UTF_8);
        se.setContentType("application/json");
        mHttpPost.setEntity(se);
        HttpResponse response = mHttpClient.execute(mHttpPost);
        HttpEntity resEntity = response.getEntity();
        inputStream = EntityUtils.toString(response.getEntity());

        responseMap.put("response", inputStream);
       /* } catch (Exception e) {
            e.printStackTrace();
            responseMap.put("exception",e.toString());
           // Log.i(TAG, "ffffffffffffffffffffffffff" + e.toString());
        }*/
        return responseMap;
    }
    /*public static String postStringRequestWithHeader(String url, String json) {
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
            mHttpPost.setEntity(se);
            //	    form = new UrlEncodedFormEntity(nameValuePairs,"UTF-8");
            //		mHttpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));

            HttpResponse response = mHttpClient.execute(mHttpPost);
            Header[] headers = response.getHeaders("sessionId");
            String headerVal = null;
            for (Header header : headers) {
                if (header != null) {
                    ContentManager.sessionId = header.getValue();
                    *//*SalesUser user=SettingPrefrence.getUserPrefrence(AppUtility.mContext);
                    user.setSessionId(header.getValue());
					SettingPrefrence.saveUserPrefrence(user, AppUtility.mContext);*//*
                }

            }


            HttpEntity resEntity = response.getEntity();

           // Log.i("", TAG + "here...2");

			*//*String resp = EntityUtils.toString(resEntity);
        Log.i(TAG,"postSyncXML srv response:"+resp);*//*
            inputStream = EntityUtils.toString(response.getEntity());
            Log.i("", TAG + "here...3" + inputStream);

        } catch (Exception e) {
            Log.i("", TAG + "ffffffffffffffffffffffffff" + e.toString());
        }
        return inputStream;
    }*/

    /*public static String postStringRequest(String url, String json) {
        DefaultHttpClient mHttpClient = new DefaultHttpClient();
        String inputStream = null;
        String[] str = new String[2];
        try {
            HttpPost mHttpPost = new HttpPost(url);
            mHttpPost.addHeader("sessionId", AppUtility.sessionId);
//			mHttpPost.addHeader("sessionId","faghsdfha");

            StringEntity se = new StringEntity(json, HTTP.UTF_8);
            *//*List<NameValuePair> postParams = new ArrayList<NameValuePair>();
            postParams.add(new BasicNameValuePair("json", json.toString()));
			UrlEncodedFormEntity ent = new UrlEncodedFormEntity(postParams,HTTP.UTF_8);
			ent.setContentType("application/json");
			mHttpPost.setEntity(ent);*//*
            se.setContentType("application/json");
            //		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            //	    nameValuePairs.add(new BasicNameValuePair("xml",xml));
            Log.i(TAG, "here..." + AppUtility.sessionId);
            mHttpPost.setEntity(se);
            //	    form = new UrlEncodedFormEntity(nameValuePairs,"UTF-8");
            //		mHttpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));

            HttpResponse response = mHttpClient.execute(mHttpPost);
			*//*Header[] headers=response.getHeaders("sessionId");
            String headerVal = null;
			for(Header header: headers){
				if(header!=null){
					ContentManager.sessionId=header.getValue();
				}

			}*//*


            HttpEntity resEntity = response.getEntity();

            Log.i("", TAG + "here...2");

			*//*String resp = EntityUtils.toString(resEntity);
        Log.i(TAG,"postSyncXML srv response:"+resp);*//*
            inputStream = EntityUtils.toString(response.getEntity());
            Log.i(TAG, "here...3" + inputStream);

        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, "ffffffffffffffffffffffffff" + e.toString());
        }
        return inputStream;
    }*/
    public static HashMap<String, String> httpPost(String url, String requestBody) throws Exception {
        HttpClient mHttpClient = com.customComponent.Networking.MySSLSocketFactory.getNewHttpClient();
        String inputStream = null;
        HashMap<String, String> responseMap = new HashMap<>();
        HttpPost mHttpPost = new HttpPost(url);
                mHttpPost.addHeader("Authorization", "Bearer 1139a002-5856-39e5-b51e-a2ad3d59efd7");
        if(requestBody!=null) {
            StringEntity se = new StringEntity(requestBody, HTTP.UTF_8);
            se.setContentType("application/json");
            mHttpPost.setEntity(se);
        }
        HttpResponse response = mHttpClient.execute(mHttpPost);
        HttpEntity resEntity = response.getEntity();
        inputStream = EntityUtils.toString(response.getEntity());
        responseMap.put("response", inputStream);
        return responseMap;
    }

    public static HashMap<String, String> httpPostWithTokken(String url, String requestBody, String token, String tokenValue) throws Exception {
        HttpClient mHttpClient = com.customComponent.Networking.MySSLSocketFactory.getNewHttpClient();
        String inputStream = null;
        HashMap<String, String> responseMap = new HashMap<>();
        HttpPost mHttpPost = new HttpPost(url);
        mHttpPost.addHeader(token, tokenValue);
        if (requestBody != null) {
            StringEntity se = new StringEntity(requestBody, HTTP.UTF_8);
            se.setContentType("application/json");
            mHttpPost.setEntity(se);
        }
        HttpResponse response = mHttpClient.execute(mHttpPost);
        HttpEntity resEntity = response.getEntity();
        inputStream = EntityUtils.toString(response.getEntity());
        responseMap.put("response", inputStream);
        return responseMap;
    }


    public static HashMap<String, String> httpPostUpdate(String url, String requestBody) throws Exception {
        DefaultHttpClient mHttpClient = new DefaultHttpClient();
        String inputStream = null;
        HashMap<String, String> responseMap = new HashMap<>();
        HttpPost mHttpPost = new HttpPost(url);
        //mHttpPost.addHeader(token, tokenValue);
        if (requestBody != null) {
            StringEntity se = new StringEntity(requestBody, HTTP.UTF_8);
            se.setContentType("application/json");
            mHttpPost.setEntity(se);
        }
        HttpResponse response = mHttpClient.execute(mHttpPost);
        HttpEntity resEntity = response.getEntity();
        inputStream = EntityUtils.toString(response.getEntity());
        responseMap.put("response", inputStream);
        return responseMap;
    }
    public static HashMap<String, String> httpPostAadhaar(String url, String requestBody ,String token,String tokenValue) throws Exception {
        DefaultHttpClient mHttpClient = new DefaultHttpClient();
        String inputStream = null;
//        Log.e("httpPostAadhaarURL",url);
//        Log.e("httpPostAadhaarREQBODY",requestBody);
//        Log.e("httpPostAadhaarTOKEN",token);
//        Log.e("httpPostAadhaarTVALUE",tokenValue);
        HashMap<String, String> responseMap = new HashMap<>();
        HttpPost mHttpPost = new HttpPost(url);
        mHttpPost.addHeader(token, tokenValue);
        if(requestBody!=null) {
            StringEntity se = new StringEntity(requestBody, HTTP.UTF_8);
            se.setContentType("application/xml");
            mHttpPost.setEntity(se);
        }
        HttpResponse response = mHttpClient.execute(mHttpPost);
        HttpEntity resEntity = response.getEntity();
        inputStream = EntityUtils.toString(response.getEntity());
        responseMap.put("response", inputStream);
        return responseMap;
    }
    public static String HttpPostLifeCerticiate(String url, String postData, String AuthToken, String AuthTokenValue) {
        String SetServerString = "";

        try {
            HttpClient httpclient = createHttpClient();
            System.setProperty("http.keepAlive", "false");
            HttpPost post = new HttpPost(url);
            StringEntity str = new StringEntity(postData);
            post.setHeader(AuthToken, AuthTokenValue);
            str.setContentType("application/xml; charset=utf-8");
            str.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
                    "application/xml; charset=utf-8"));
            post.setEntity(str);
            HttpResponse response = httpclient.execute(post);
            HttpEntity entity = response.getEntity();
            SetServerString = EntityUtils.toString(entity);
        } catch (Exception e) {
            e.printStackTrace();
            SetServerString = "Connection time out Error "+ "\n"+e.getMessage();
        }
        return SetServerString;

    }





    public static HttpClient createHttpClient() {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);

            SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.STRICT_HOSTNAME_VERIFIER);
            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
            HttpConnectionParams.setConnectionTimeout(params, 5000);// Sets the timeout until a connection is etablished.
            HttpConnectionParams.setSoTimeout(params, 15000);    //   Sets the default socket timeout (SO_TIMEOUT) in milliseconds which is the timeout for waiting for data.

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

            return new DefaultHttpClient(ccm, params);
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            return new DefaultHttpClient();
        }
        return null;
    }


    static class MySSLSocketFactory extends SSLSocketFactory {
        SSLContext sslContext = SSLContext.getInstance("TLS");

        public MySSLSocketFactory(KeyStore truststore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
            super(truststore);

            TrustManager tm = new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };

            sslContext.init(null, new TrustManager[]{tm}, null);
        }

        @Override
        public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException {
            return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
        }

        @Override
        public Socket createSocket() throws IOException {
            return sslContext.getSocketFactory().createSocket();

        }
    }

}
