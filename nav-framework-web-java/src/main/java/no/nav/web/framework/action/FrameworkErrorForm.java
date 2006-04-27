package no.nav.web.framework.action;

import org.apache.struts.action.ActionForm;

/**
 * The form that holds the technical error details.
 * 
 * @author person7553f5959484, Accenture
 * @version $Id: FrameworkErrorForm.java 2745 2006-01-26 20:44:41Z skb2930 $
 */
public class FrameworkErrorForm extends ActionForm {

	private String errorMessage = null;
	private String stacktrace = null;
	private String errorCode = null;
	private String errorId = null;
	private String processId = null;
	private String screenId = null;
	private String transactionId = null;
	private String userId = null;

	/**
	 * Returns the error code.
	 * 
	 * @return the error code.
	 */
	public String getErrorCode() {
		if (null == errorCode) {
			return "";
		}
		return errorCode;
	}

	/**
	 * Returns the error id.
	 * 
	 * @return the error id.
	 */
	public String getErrorId() {
		if (null == errorId) {
			return "";
		}
		return errorId;
	}

	/**
	 * Returns the error message.
	 * 
	 * @return the error message.
	 */
	public String getErrorMessage() {
		if (null == errorMessage) {
			return "";
		}
		return errorMessage;
	}

	/**
	 * Returns the process id.
	 * 
	 * @return the process id.
	 */
	public String getProcessId() {
		if (null == processId) {
			return "";
		}
		return processId;
	}

	/**
	 * Returns the screen id.
	 * 
	 * @return the screen id.
	 */
	public String getScreenId() {
		if (null == screenId) {
			return "";
		}
		return screenId;
	}

	/**
	 * Returns the stack trace.
	 * 
	 * @return the stack trace.
	 */
	public String getStacktrace() {
		if (null == stacktrace) {
			return "";
		}
		return stacktrace;
	}

	/**
	 * Returns the transaction id.
	 * 
	 * @return the transaction id.
	 */
	public String getTransactionId() {
		if (null == transactionId) {
			return "";
		}
		return transactionId;
	}

	/**
	 * Returns the user id.
	 * 
	 * @return the user id.
	 */
	public String getUserId() {
		if (null == userId) {
			return "";
		}
		return userId;
	}

	/**
	 * Sets the error code.
	 * 
	 * @param string the error code.
	 */
	public void setErrorCode(String string) {
		errorCode = string;
	}

	/**
	 * Sets the error id.
	 * 
	 * @param string the error id.
	 */
	public void setErrorId(String string) {
		errorId = string;
	}

	/**
	 * Sets the errro message.
	 * 
	 * @param string the error message.
	 */
	public void setErrorMessage(String string) {
		errorMessage = string;
	}

	/**
	 * Sets the process id.
	 * 
	 * @param string the process id.
	 */
	public void setProcessId(String string) {
		processId = string;
	}

	/**
	 * Sets the screen id.
	 * 
	 * @param string the screen id.
	 */
	public void setScreenId(String string) {
		screenId = string;
	}

	/**
	 * Sets the stack trace.
	 * 
	 * @param string the stack trace.
	 */
	public void setStacktrace(String string) {
		stacktrace = string;
	}

	/**
	 * Sets the transaction id.
	 * 
	 * @param string the transaction id.
	 */
	public void setTransactionId(String string) {
		transactionId = string;
	}

	/**
	 * Sets the user id.
	 * 
	 * @param string the user id.
	 */
	public void setUserId(String string) {
		userId = string;
	}

}
