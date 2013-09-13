package no.nav.esso;

public class ValidationResult {
	
	private String userId;	
	private boolean valid;
	private String serviceSecLevel;
	private ValidationError error;
	
	public ValidationResult(String userId, boolean valid, String serviceSecLevel, ValidationError error) {
		this.userId = userId;
		this.valid = valid;
		this.serviceSecLevel = serviceSecLevel;
		this.error = error;
	}
	
	public String getUserId() {
		return userId;
	}
	public boolean isValid() {
		return valid;
	}
	public String getServiceSecLevel() {
		return serviceSecLevel;
	}
	public ValidationError getError() {
		return error;
	}
	
	

}
