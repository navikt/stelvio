package no.nav.esso;

public enum ValidationError {
	INVALID_USERNAME_PASSWORD("bad_credentials", "Invalid username or password"),
	PASSWORD_EXPIRED("bad_credentials", "Password expired"),
	PASSWORD_BLOCKED("account_locked", "Password blocked"),
	TECHINCAL("system_error", "Technical error");
	
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
