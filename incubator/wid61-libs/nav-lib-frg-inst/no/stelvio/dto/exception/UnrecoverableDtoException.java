package no.stelvio.dto.exception;

import java.text.MessageFormat;


/**
 * Exception that is Java SE 1.4 and WS-I compliant.
 * @author person983601e0e117 (Accenture)
 *
 */
public abstract class UnrecoverableDtoException extends RuntimeException {

	private static final long serialVersionUID = -2075059349877576324L;
	private Object[] templateArguments;

	private long errorId;
	private String userId;
	private String screenId;
	private String processId;
	private String transactionId;
	
	public long getErrorId() {
		return errorId;
	}

	public void setErrorId(long errorId) {
		this.errorId = errorId;
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public String getScreenId() {
		return screenId;
	}

	public void setScreenId(String screenId) {
		this.screenId = screenId;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public UnrecoverableDtoException(){}

	/**
	 * Creates a new UnrecoverableDtoException
	 * @param arguments
	 */
	public UnrecoverableDtoException(Object[] arguments){
		this(null,arguments);
	}
	
	/**
	 * Creates a new UnrecoverableDtoException
	 * @param cause
	 * @param arguments
	 */
	public UnrecoverableDtoException(Throwable cause, Object[] arguments){
		super(cause);
		templateArguments = arguments;
	}
	
	public final String getMessage(){
		MessageFormat messageFormat = new MessageFormat(messageTemplate());
		StringBuffer message =  new StringBuffer(messageFormat.format(getTemplateArguments()));
        Throwable cause = getCause();
        
        if(cause != null){
        	message.append(". Cause:");
        }
        
		for (; cause != null; cause = cause.getCause()) {
			message.append(" ").append(cause).append(".");
        }		
		return message.toString();
	}
	
	/**
	 * Returns a message template used by the {@link #getMessage()} method
	 * @return
	 */
	protected abstract String messageTemplate();


	public final Object[] getTemplateArguments() {
		return templateArguments;
	}	

}
