package no.nav.esso;

public interface AuthValidator {

	public abstract ValidationResult validate(String userName, String password);

}