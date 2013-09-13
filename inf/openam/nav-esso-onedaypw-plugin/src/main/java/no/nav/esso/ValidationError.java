package no.nav.esso;

public enum ValidationError {
	INVALID_USERNAME_PASSWORD("1", "Invalid username or password"),
	PASSWORD_EXPIRED("2", "Password expired"),
	PASSWORD_BLOCKED("3", "Password blocked"),
	TECHINCAL("4", "Technical error");
	
	private final String code;
	private final String description;
	
	private ValidationError(String code, String description) {
		this.code = code;
		this.description = description;		
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getCode() {
		return code;
	}
	
	@Override	
	public String toString() {
		return code + ": " + description;
	}

}
