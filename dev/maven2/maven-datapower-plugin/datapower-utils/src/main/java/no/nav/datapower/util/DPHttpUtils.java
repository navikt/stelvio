package no.nav.datapower.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

public class DPHttpUtils {

	private static final Logger LOG = Logger.getLogger(DPHttpUtils.class);
	
	public static HttpURLConnection openPostRequestConnection(String host, String user, String password) {
		try {
			URL deviceURL = new URL(host);
			HttpURLConnection conn = (HttpURLConnection)deviceURL.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestProperty("Authorization ", getBasicAuthString(user, password));
			conn.connect();
			return conn;
		} catch (MalformedURLException e) {
			LOG.fatal("openHttpPostRequest(), caught MalformedURLException...");
			e.printStackTrace();
		} catch (ProtocolException e) {
			LOG.fatal("HttpUtils.openHttpPostRequest(), caught ProtocolException...");
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			LOG.fatal("HttpUtils.openHttpPostRequest(), caught UnsupportedEncodingException...");
			e.printStackTrace();
		} catch (IOException e) {
			LOG.fatal("HttpUtils.openHttpPostRequest(), caught IOException...");
			e.printStackTrace();
		}
		return null;
	}

	public static String doPostRequest(String host, String user, String password, String data, int retries) throws IOException {
		LOG.debug("doPostRequest(), host=" + host + ", user=" + user);
		try {
			HttpURLConnection conn = openPostRequestConnection(host, user, password);
//			System.out.println("Executing HTTP POST, request data: \n");
//			System.out.println(data);
			DPStreamUtils.writeStringToOutputStream(data, conn.getOutputStream(), true);
			String response = getResponse(conn);
			conn.disconnect();
			return response;
		} catch (IOException e) {
			if(retries > 1){
				LOG.error("doPostRequest(), Retrying..., " + retries + " retries left!");
				return doPostRequest(host,user,password,data,--retries);
			}else throw e;
		}
	}
	
	public static String getResponse(HttpURLConnection conn) throws IOException {
		int httpResponseCode = conn.getResponseCode();
		LOG.debug("getResponse(), HTTP Response code = " + httpResponseCode);
		if (httpResponseCode == 200)
			return DPStreamUtils.getInputStreamAsString(conn.getInputStream(), true);
		else
			return DPStreamUtils.getInputStreamAsString(conn.getErrorStream(), true);
	}
	
	public static String getBasicAuthString(String user, String password) throws UnsupportedEncodingException {
		StringBuffer str = new StringBuffer();
		str.append("Basic ");
		String basicAuth = new String(Base64.encodeBase64((user + ":" + password).getBytes("UTF-8")));
		str.append(basicAuth);
		return str.toString();
	}

}
