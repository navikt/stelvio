package no.nav.datapower.xmlmgmt.net;

public class DPHttpResponse {
	
	private int httpResponseCode;
	private String responseBody;
	private String url;
	private String requestMethod;
	
	public int getHttpResponseCode() {
		return httpResponseCode;
	}
	public void setHttpResponseCode(int httpResponseCode) {
		this.httpResponseCode = httpResponseCode;
	}
	public String getRequestMethod() {
		return requestMethod;
	}
	public void setRequestMethod(String requestMethod) {
		this.requestMethod = requestMethod;
	}
	public String getResponseBody() {
		return responseBody;
	}
	public void setResponseBody(String responseBody) {
		this.responseBody = responseBody;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
}
