package no.nav.datapower.xmlmgmt.net;

import java.io.IOException;

import org.apache.log4j.Logger;

public class DPHttpClientPrintOut implements DPHttpClient {

	private static final Logger LOG = Logger.getLogger(DPHttpClientPrintOut.class);
	
	public DPHttpResponse doPostRequest(String host, String user,
			String password, String data, int retries) throws IOException {
		DPHttpResponse response = new DPHttpResponse();
		response.setHttpResponseCode(200);
		response.setRequestMethod("POST");
		response.setResponseBody(data);
		response.setUrl("System.out.println");
		System.out.println("-------------- DPHttpClientPrintOut -------------");
		System.out.println("User:" + user + " , Host:" + host);
		System.out.println("RequestBody:");
		System.out.println(data);
		System.out.println("--------------------------------------------------");
		return response;
	}

}
