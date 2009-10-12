package no.nav.datapower.xmlmgmt.net;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;


import no.nav.datapower.util.DPStreamUtils;
import no.nav.datapower.xmlmgmt.XMLMgmtException;

import org.apache.commons.codec.binary.Base64;

import org.apache.log4j.Logger;



public class DPHttpClientDefault implements DPHttpClient{

	private static final Logger LOG = Logger.getLogger(DPHttpClientDefault.class);
	
	public HttpsURLConnection openPostRequestConnection(String host, String user, String password) {
		try {
			URL deviceURL = new URL(host);
//			HttpURLConnection conn = (HttpURLConnection)deviceURL.openConnection();
			HttpsURLConnection conn = (HttpsURLConnection)deviceURL.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestProperty("Authorization ", getBasicAuthString(user, password));
			
			HostnameVerifier ignore = new HostnameVerifier(){
				public boolean verify(String s, javax.net.ssl.SSLSession session){
					return true;
				}
			};
			conn.setHostnameVerifier(ignore);
			conn.connect();
			return conn;
		} catch (MalformedURLException e) {
			LOG.fatal("openHttpPostRequest(), caught MalformedURLException...");
			e.printStackTrace();
		} catch (ProtocolException e) {
			LOG.fatal("openHttpPostRequest(), caught ProtocolException...");
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			LOG.fatal("openHttpPostRequest(), caught UnsupportedEncodingException...");
			e.printStackTrace();
		} catch (IOException e) {
			LOG.fatal("openHttpPostRequest(), caught IOException...");
		
			e.printStackTrace();
		}
		return null;
	}

	public DPHttpResponse doPostRequest(String host, String user, String password, String data, int retries) throws IOException {
		LOG.debug("doPostRequest(), host=" + host + ", user=" + user);
		try {
			
			HttpsURLConnection conn = openPostRequestConnection(host, user, password);
			
			//System.out.println("Executing HTTP POST, request data.. \n");
			DPStreamUtils.writeStringToOutputStream(data, conn.getOutputStream(), true);
			String response = getResponse(conn);
			int httpResponseCode = conn.getResponseCode();
			DPHttpResponse dpResponse = new DPHttpResponse();
			dpResponse.setHttpResponseCode(httpResponseCode);
			dpResponse.setRequestMethod(conn.getRequestMethod());
			dpResponse.setResponseBody(response);
			dpResponse.setUrl(conn.getURL().toString());	
			conn.disconnect();
			
			return dpResponse;
		} catch (IOException e) {
			if(retries > 1){
				LOG.error("doPostRequest(), Retrying..., " + retries + " retries left!");
				return doPostRequest(host,user,password,data,--retries);
			}else throw e;
		}
	}
	
	public String getResponse(HttpsURLConnection conn) throws IOException {
		int httpResponseCode = conn.getResponseCode();
		LOG.info("getResponse(), HTTP Response code = " + httpResponseCode);
		if (httpResponseCode == 200)
			return DPStreamUtils.getInputStreamAsString(conn.getInputStream(), true);
		else
			return DPStreamUtils.getInputStreamAsString(conn.getErrorStream(), true);
	}
	
	public String getBasicAuthString(String user, String password) throws UnsupportedEncodingException {
		StringBuffer str = new StringBuffer();
		str.append("Basic ");
		String basicAuth = new String(Base64.encodeBase64((user + ":" + password).getBytes("UTF-8")));
		str.append(basicAuth);
		return str.toString();
	}
}

