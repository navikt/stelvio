package no.nav.datapower.xmlmgmt.net;

import java.io.IOException;

public interface DPHttpClient {
	
	public DPHttpResponse doPostRequest(String host, String user, String password, String data, int retries) throws IOException;
	
}
