package no.stelvio.common.transferobject;

import java.util.Locale;

/** 
 * Defines a container for context information.
 * 
 * @author personff564022aedd 
 */
public interface ContextContainer {

	String getUserId();
	
	void setUserId(String userId);
	
	String getScreenId();
	
	void setScreenId(String screenId);
	
	String getModuleId();
	
	void setModuleId(String moduleId);
	
	String getProcessId();
	
	void setProcessId(String processId);
	
	String getTransactionId();
	
	void setTransactionId(String txId);
	
	Locale getLocale();
	
	void setLocale(Locale locale);
}
