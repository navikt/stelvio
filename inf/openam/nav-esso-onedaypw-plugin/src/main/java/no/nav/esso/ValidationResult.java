package no.nav.esso;

public class ValidationResult {
	
	private String userId;	
	private boolean valid;
	private String serviceSecLevel;
	
	public ValidationResult(String userId, boolean valid, String serviceSecLevel) {
		this.userId = userId;
		this.valid = valid;
		this.serviceSecLevel = serviceSecLevel;
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
	
	

}
