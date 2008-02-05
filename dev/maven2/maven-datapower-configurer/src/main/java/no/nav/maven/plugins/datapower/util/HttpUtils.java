package no.nav.maven.plugins.datapower.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import org.apache.commons.codec.binary.Base64;

public class HttpUtils {

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
			System.out.println("HttpUtils.openHttpPostRequest(), caught MalformedURLException...");
			e.printStackTrace();
		} catch (ProtocolException e) {
			System.out.println("HttpUtils.openHttpPostRequest(), caught ProtocolException...");
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			System.out.println("HttpUtils.openHttpPostRequest(), caught UnsupportedEncodingException...");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("HttpUtils.openHttpPostRequest(), caught IOException...");
			e.printStackTrace();
		}
		return null;
	}

	public static String doPostRequest(String host, String user, String password, String data) throws IOException {
		HttpURLConnection conn = openPostRequestConnection(host, user, password);
		StreamUtils.writeStringToOutputStream(data, conn.getOutputStream(), true);
		String response = getResponse(conn);
		conn.disconnect();
		return response;
	}
	
	public static String getResponse(HttpURLConnection conn) throws IOException {
		int httpResponseCode = conn.getResponseCode();
		System.out.println("HTTP Response code = " + httpResponseCode);
		if (httpResponseCode == 200)
			return StreamUtils.getInputStreamAsString(conn.getInputStream(), true);
		else
			return StreamUtils.getInputStreamAsString(conn.getErrorStream(), true);
	}
	
	public static String getBasicAuthString(String user, String password) throws UnsupportedEncodingException {
		StringBuffer str = new StringBuffer();
		str.append("Basic ");
		String basicAuth = new String(Base64.encodeBase64((user + ":" + password).getBytes("UTF-8")));
		str.append(basicAuth);
		return str.toString();
	}

}
