package no.stelvio.dto.exception;


public abstract class RecoverableDtoException extends Exception {

	private Object[] templateArguments;
	private long errorId;
	private String userId;
	private String screenId;
	private String processId;
	private String transactionId;
	private String message;
	
	public RecoverableDtoException(){
		super();
	}
	
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
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
	

	public final Object[] getTemplateArguments() {
		return templateArguments;
	}	
	

	public void setTemplateArguments(Object[] templateArguments) {
		this.templateArguments = templateArguments;
	}	

}
