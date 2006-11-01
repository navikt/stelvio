package no.stelvio.common.transferobject;

import java.util.Locale;

import no.stelvio.common.context.RequestContext;

/**
 * Contains information to be stored on the {@link RequestContext}.
 * 
 * @author personff564022aedd
 */
public class ContextContainerImpl implements ContextContainer {

	private Locale locale;
	private String moduleId;
	private String processId;
	private String screenId;
	private String transactionId;
	private String userId;
	
	public Locale getLocale() {
		return locale;
	}

	public String getModuleId() {
		return moduleId;
	}

	public String getProcessId() {
		return processId;
	}

	public String getScreenId() {
		return screenId;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public String getUserId() {
		return userId;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public void setScreenId(String screenId) {
		this.screenId = screenId;
	}

	public void setTransactionId(String txId) {
		this.transactionId = txId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}
