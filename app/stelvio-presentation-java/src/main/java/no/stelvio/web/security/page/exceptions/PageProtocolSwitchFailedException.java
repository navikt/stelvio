package no.stelvio.web.security.page.exceptions;

import no.stelvio.common.security.SecurityException;

public class PageProtocolSwitchFailedException extends SecurityException {

	private boolean pageRequiresSSL;
	
	public PageProtocolSwitchFailedException(String viewId,boolean pageRequiresSSL, String port){
		super(viewId,port);
		this.pageRequiresSSL = pageRequiresSSL;
	}
	public PageProtocolSwitchFailedException(Throwable cause,String viewId,boolean pageRequiresSSL, String port){
		super(cause,viewId,port);
		this.pageRequiresSSL = pageRequiresSSL;
	}
	
	@Override
	protected String messageTemplate() {
		String toHttpsMsg = "Redirection to secure channel for page '{0}' with HTTPS port '{1}' failed.";
		String toHttpMsg = "Redirection to normal channel for page '{0}' with HTTP port '{1}' failed.";
		return this.pageRequiresSSL ? toHttpsMsg : toHttpMsg;
	}

}
