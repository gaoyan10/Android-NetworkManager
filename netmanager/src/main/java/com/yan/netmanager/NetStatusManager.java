package com.yan.netmanager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import android.os.AsyncTask;
import android.util.Log;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


public class NetStatusManager {
	private static NetStatusManager instance = new NetStatusManager();
	private String url;
	private String requestUrl = "";
	private int second = 20;
	private NetStatus status;
	private boolean isPing = false;

	private int readTimeOut = 2000, connectTimeOut = 2000;
	private NetStatusManager() {
		url = "www.baidu.com";
		requestUrl = "http://www.baidu.com";
		status = NetStatus.UNKNOW;
		Ping ping = new Ping();
		ping.execute();
	}
	public static NetStatusManager getInstance() {
		return instance;
	}
	@Deprecated
	public void config(String targetUrl) {
		this.url = targetUrl;
	}
	public void configTimeOut(int readTimeOut, int connectTimeOut) {
		this.readTimeOut = readTimeOut;
		this.connectTimeOut = connectTimeOut;
	}
	public void config(String targetHost, String targetUrl) {
		if (targetHost.startsWith("http://")|| targetHost.startsWith("https://")) {
			this.url = targetHost.substring(targetHost.indexOf("//") + 2, targetHost.length());
		}else {
			this.url = targetHost;
		}
		this.requestUrl = targetUrl;
	}
	public void setCheckType(boolean isPing) {
		this.isPing = isPing;
	}
	public void configTimeOut(int second) {
		this.second = second;
	}
	private class Ping extends AsyncTask<String, String, String> {
		@Override
		protected String doInBackground(String... params) {
			String s = "";
			if (isPing) {
				s = ping();
				Log.i("ping", s);
			}else {
				if(requestUrl.startsWith("https://")) {
					s = requestHttps();
				}else {
					s = request();
				}
			}
			return s;
		}
	}

	private String ping() {
		String resault = "";
		Process p;
		try {
			// ping -c 3 -w 100 中 ，-c 是指ping的次数 3是指ping 3次 ，-w 100
			// 以秒为单位指定超时间隔，是指超时时间为100秒
			this.status = NetStatus.WAIT;
			p = Runtime.getRuntime().exec("ping -c 3 -w " + second + " " + url);
			int status = p.waitFor();
			InputStream input = p.getInputStream();
			BufferedReader in = new BufferedReader(new InputStreamReader(input));
			StringBuffer buffer = new StringBuffer();
			String line = "";
			while ((line = in.readLine()) != null) {
				buffer.append(line);
			}
			System.out.println("Return ============" + buffer.toString());
			if (status == 0) {
				//resault = "success";
				this.status = NetStatus.GOOD;
			} else {
				//resault = "faild";
				this.status = NetStatus.BREAK;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return resault;
	}
	private String request() {
		HttpURLConnection urlConnection = null;
		try {
			URL url = new URL(requestUrl);
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setConnectTimeout(connectTimeOut);
			urlConnection.setReadTimeout(readTimeOut);
			int status = urlConnection.getResponseCode();
			if (status == 200) {
				this.status = NetStatus.GOOD;
			} else {
				this.status = NetStatus.BREAK;
			}
		}catch (MalformedURLException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
			this.status = NetStatus.BREAK;
		}finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}
		return "";
	}
	private String requestHttps() {
		HttpURLConnection conn = null;

		// Create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager(){
			public X509Certificate[] getAcceptedIssuers(){return null;}
			public void checkClientTrusted(X509Certificate[] certs, String authType){}
			public void checkServerTrusted(X509Certificate[] certs, String authType){}
		}};

		// Install the all-trusting trust manager
		try {// 注意这部分一定要
			HttpsURLConnection.setDefaultHostnameVerifier(new MyHostnameVerifier());
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, trustAllCerts, new SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

			URL url = new URL(requestUrl);
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(connectTimeOut);
			conn.setReadTimeout(readTimeOut);
			conn.connect();
			int status = conn.getResponseCode();
			if (status == 200) {
				this.status = NetStatus.GOOD;
			} else {
				this.status = NetStatus.BREAK;
			}
		}catch(IOException e) {
			e.printStackTrace();
			this.status = NetStatus.BREAK;
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
		return "";
	}
	public void refreshStatus() {
		Ping ping = new Ping();
		ping.execute();
	}
	protected void changeStatus(NetStatus status) {
		this.status = status;
	}
	public NetStatus getNetStatus() {
		return status;
	}
	class myX509TrustManager implements X509TrustManager {

		public void checkClientTrusted(X509Certificate[] chain, String authType) {
		}

		public void checkServerTrusted(X509Certificate[] chain, String authType) {
			System.out.println("cert: " + chain[0].toString() + ", authType: " + authType);
		}

		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}
	}

	/**
	 * 重写一个方法
	 * @author Administrator
	 *
	 */
	class MyHostnameVerifier implements HostnameVerifier {

		public boolean verify(String hostname, SSLSession session) {
			System.out.println("Warning: URL Host: " + hostname + " vs. " + session.getPeerHost());
			return true;
		}
	}
}
