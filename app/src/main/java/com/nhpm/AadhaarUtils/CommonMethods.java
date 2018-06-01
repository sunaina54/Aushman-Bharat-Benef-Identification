package com.nhpm.AadhaarUtils;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.telephony.TelephonyManager;

import com.nhpm.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
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
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/* */


public class CommonMethods {

    static Context context;

    public static void SetApplicationContext(Context context_) {
        context = context_;
    }


    public static void showInfoDialog(String message) {

        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(context);
        dlgAlert.setMessage(message);
        dlgAlert.setTitle("Information");
        dlgAlert.setIcon(R.drawable.info);
        dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // finish();
                return;
            }
        });
        dlgAlert.setCancelable(false);
        dlgAlert.create().show();

    }

    public static void showErrorDialog(String title, String message) {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(context);
        dlgAlert.setMessage(message);
        dlgAlert.setTitle(title);
        dlgAlert.setIcon(R.drawable.erroricon);
        dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // finish();
                return;
            }
        });
        dlgAlert.setCancelable(false);
        dlgAlert.create().show();
    }

    public static String GetWiFiMac() {

        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (wifiManager.isWifiEnabled()) {
            WifiInfo wInfo = wifiManager.getConnectionInfo();
            String macAddress = wInfo.getMacAddress();
            return macAddress;
        }
        return "";
    }


    public static String GetIMEI(Context ctx) {
        String imei = "";
        TelephonyManager mngr = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
        imei = mngr.getDeviceId();
        Global.imei = imei;
        return imei;
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public static String getCurrentSsid(Context context) {
        String ssid = null;
        WifiManager wifiManager_ = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (wifiManager_.isWifiEnabled()) {
            ConnectivityManager connManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connManager
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (networkInfo.isConnected()) {
                final WifiManager wifiManager = (WifiManager) context
                        .getSystemService(Context.WIFI_SERVICE);
                final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
                if (connectionInfo != null && connectionInfo.getSSID() != "") {
                    ssid = connectionInfo.getSSID();
                }
            }
        }
        return ssid;
    }

    public static int getCurrentWiFiSpeed(Context context) {
        int speed = -100;
        ConnectivityManager connManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (networkInfo.isConnected()) {
            final WifiManager wifiManager = (WifiManager) context
                    .getSystemService(Context.WIFI_SERVICE);
            final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
            if (connectionInfo != null && connectionInfo.getSSID() != "") {
                speed = connectionInfo.getRssi();
            }
        }
        return speed;
    }

    public static boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static float getBatteryLevel() {
        Intent batteryIntent = context.registerReceiver(null, new IntentFilter(
                Intent.ACTION_BATTERY_CHANGED));
        int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        // Error checking that probably isn't needed but I added just in case.
        if (level == -1 || scale == -1) {
            return 50.0f;
        }

        return ((float) level / (float) scale) * 100.0f;
    }


	/*	public static String postData(String url,String xml) {
        String result = "";

		String url2 = Global.serverurl + url;
		Log.e("url", "hit "+url2);
		URL obj = null;
		try {
			obj = new URL(url2);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HttpURLConnection con = null;
		try {
			con = (HttpURLConnection) obj.openConnection();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			con.setRequestMethod("POST");
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		con.setRequestProperty("Content-Type", "application/xml");
		con.setRequestProperty("Authorization", "Basic UHBvX1Jlc3RfVXNyXzJLOkFlQmEkX184OSElXl56cQ==");

		// For POST only - START
		con.setDoOutput(true);
		OutputStream os = null;
		int responseCode = 0;
		try {
			os = con.getOutputStream();
			os.write(xml.getBytes());
			os.flush();
			os.close();
			responseCode = con.getResponseCode();
			// For POST only - END

			System.out.println("POST Response Code :: " + responseCode);
			Log.e("Response Code", "=="+responseCode);

			if (responseCode == HttpURLConnection.HTTP_OK) { //success
				BufferedReader in = null;
				in = new BufferedReader(new InputStreamReader(
						con.getInputStream()));

				String inputLine;
				StringBuffer response = new StringBuffer();

				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
					result +=inputLine;
				}
				in.close();

				// print result
				System.out.println(response.toString());
			} else {
				System.out.println("POST request not worked");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			result = "ERROR";
			Log.e("IOException in post method", "=="+e);
			e.printStackTrace();
		}
		//		Log.e("b4 rplc", "==="+result);
		//		result = result.replace("Error", "");
		Log.e("result", "==="+result);
		return result;

	}
	 */


    @SuppressWarnings("deprecation")
    public static void checkAvailableConnection() {
        ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo wifi = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        final NetworkInfo mobile = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifi.isConnected()) {
            WifiManager myWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo myWifiInfo = myWifiManager.getConnectionInfo();
            int ipAddress = myWifiInfo.getIpAddress();
            Global.ipAddress = android.text.format.Formatter.formatIpAddress(ipAddress);
            Global.connectionType = "W";
        } else if (mobile.isConnected()) {
            Global.connectionType = "M";
            Global.ipAddress = GetLocalIpAddress();
        } else {
            Global.connectionType = "ERROR";
            Global.ipAddress = "ERROR";
        }
    }


    public static String GetLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            return "ERROR";
        }
        return "ERROR";
    }

    public static String HttpPostLifeCerticiate(String method, String postData, String AuthToken, String AuthTokenValue) {
        String SetServerString = "";
        String url = Global.serverurl + method;
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
