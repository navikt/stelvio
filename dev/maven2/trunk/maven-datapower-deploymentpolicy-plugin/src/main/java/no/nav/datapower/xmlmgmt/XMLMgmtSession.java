package no.nav.datapower.xmlmgmt;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import no.nav.datapower.xmlmgmt.net.DPHttpClient;
import no.nav.datapower.xmlmgmt.net.DPHttpClientDefault;
import no.nav.datapower.xmlmgmt.net.DPHttpResponse;



import org.apache.log4j.Logger;

public class XMLMgmtSession {
	
	private static final Logger LOG = Logger.getLogger(XMLMgmtSession.class);
	private DPHttpClient httpClient;
	private String host;
	private String user;
	private String password;
	private File logDirectory; 
	
	public XMLMgmtSession(String host, String user, String password) {
		this.host = host;
		this.user = user;
		this.password = password;
		this.httpClient = new DPHttpClientDefault();
	}
	
	public XMLMgmtSession(String host, String user, String password, DPHttpClient httpClient) {
		this.host = host;
		this.user = user;
		this.password = password;
		this.httpClient = httpClient != null ? httpClient : new DPHttpClientDefault();
	}
	
	
	
	public String doSoapRequest(XMLMgmtRequest requestBody) throws XMLMgmtException {
		String request = XMLMgmtUtil.wrapInSoapEnvelope(requestBody);
		return doRequest(request);
	}
	
	protected String doRequest(String request) throws XMLMgmtException {
		LOG.info("Posting soap request to XML Management interface.");
		try {
			
			DPHttpResponse resp = httpClient.doPostRequest(host, user, password, request, 10);
		 	
			String response = resp.getResponseBody();
		 	if(logDirectory != null){
				logRequestResponse(request, response);
			} 
		 	XMLMgmtResponse xmlResponse = XMLMgmtResponseFactory.createXMLMgmtResponse(resp);
		 	if(!xmlResponse.isSuccessful()){
		 		throw new XMLMgmtException("XML Mgmt request failed. Response body: " 
		 				+ xmlResponse.toString() );
		 	} else{
		 		return xmlResponse.toString();
		 	}
		 	/*if(resp.getHttpResponseCode() == 500){
		 		throw new XMLMgmtException("Received response code 500. Response body: " 
		 				+ response );
		 	}*/
		 	
		 	
			
		} catch (IOException e) {
			throw new XMLMgmtException("Post request failed, content:\n" + request, e);
		}
	}
	
	private void logRequestResponse(String request, String response) throws XMLMgmtException {
		try{
			if(logDirectory != null){
				Date now = new Date();
				XMLMgmtUtil.logToFileWithDate(this.logDirectory,"xmlmgmt-request",now,".xml", request);
				XMLMgmtUtil.logToFileWithDate(this.logDirectory,"xmlmgmt-response",now,".xml", response);
			}			
		} catch (IOException io){
			throw new XMLMgmtException("Failed to log request/response correspondance.", io);
		}
	}

	public File getLogDirectory() {
		return logDirectory;
	}

	public void setLogDirectory(File logDirectory) {
		this.logDirectory = logDirectory;
	}

}
