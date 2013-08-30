package no.nav.esso;

public class MockValidator extends OneDayPwValidator {

	protected static final String VALID_USER = "VALID";
	private boolean returnValid = false;

	public MockValidator(String untUserName, String untPassword, String endpoint) {
		super(untUserName, untPassword, endpoint);
		if(untUserName.equals(VALID_USER)) {
			returnValid = true;
		} else {
			returnValid = false;			
		}
	}

	@Override
	public ValidationResult validate(String userName, String password) {
		if(returnValid) {
			return new ValidationResult("userid", true, "M");			
		} else {
			return new ValidationResult("userid", false, null);
		}
		
	}

}
