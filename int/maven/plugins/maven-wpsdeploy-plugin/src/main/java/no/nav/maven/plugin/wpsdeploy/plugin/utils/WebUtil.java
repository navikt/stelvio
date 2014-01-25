package no.nav.maven.plugin.wpsdeploy.plugin.utils;

import no.nav.maven.plugin.wpsdeploy.plugin.exceptions.ConfigurationException;
import no.nav.maven.plugin.wpsdeploy.plugin.exceptions.HttpCode404Exception;
import no.nav.maven.plugin.wpsdeploy.plugin.exceptions.HttpCodeNon2XXException;
import org.codehaus.plexus.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class WebUtil {
	private final static Logger logger = LoggerFactory.getLogger(WebUtil.class);

	public static String readUrl(String urlString) {
		String outPut;

		logger.info("Accessing url: "+ urlString);

		try {
			URL url = new URL(urlString);
			HttpURLConnection huc =  (HttpURLConnection) url.openConnection();
			int responsecode = huc.getResponseCode();
			if (responsecode == 404){
				throw new HttpCode404Exception("[readUrl()]: "+ responsecode+" response code returned from URL: "+ urlString);
			} else {
				if (responsecode < 200 && responsecode >= 300){
					throw new HttpCodeNon2XXException("[readUrl()]: "+ responsecode +" response code returned from URL: "+ urlString);
				} else {
					InputStream is = huc.getInputStream();
					outPut = convertStreamToString(is);
					is.close();
				}
			}
		} catch (IOException e) {
			throw new ConfigurationException("Exception when reading url: "+ urlString +"\n"+ e);
		}

		return outPut.toString();
	}

	public static String readUrlWithAuth(String urlString, String username, String password) {

		String outPut;
		String userpass = username + ":" + password;
		String basicAuth = "Basic " + new String(new Base64().encode(userpass.getBytes()));

		logger.info("Accessing url: "+ urlString);

		try {
			URL url = new URL(urlString);
			HttpURLConnection huc =  (HttpURLConnection) url.openConnection();
			huc.setRequestProperty("Authorization", basicAuth);
			int responsecode = huc.getResponseCode();
			if (responsecode != 404){
				if (responsecode >= 200 && responsecode < 300){
					InputStream is = huc.getInputStream();
					outPut = convertStreamToString(is);
					is.close();
				} else {
					throw new HttpCodeNon2XXException("[readUrlWithAuth()]: "+ responsecode +" response code returned from URL (username was " + username + "): " + urlString);
				}
			} else {
				throw new HttpCode404Exception("[readUrlWithAuth()]: "+ responsecode +" response code returned from URL: "+ urlString);
			}
		} catch (IOException e) {
			throw new ConfigurationException("Exception when reading url with authorization: "+ urlString +"\n"+ e);
		}

		return outPut.toString();
	}

	private static String convertStreamToString(InputStream is) {
		java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
		return s.hasNext() ? s.next() : "";
	}

}
