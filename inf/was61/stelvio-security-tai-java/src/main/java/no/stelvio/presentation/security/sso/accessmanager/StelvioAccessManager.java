package no.stelvio.presentation.security.sso.accessmanager;

/**
 * Represent an access-manager which can convert a representation of a userprincipal into
 * a StelvioPrincipal.
 * 
 * @author persondab2f89862d3
 * @see StelvioPrincipal
 */
public interface StelvioAccessManager {
	/**
	 * Sets the representation of a userprincipal and converts it to a StelvioPrincipal.
	 * 
	 * @param representation the principal representation to set
	 * @throws PrincipalNotValidException if the representation cannot be converted to a StelvioPrincipal.
	 */
	void setPrincipalRepresentation(Object representation) throws PrincipalNotValidException;
	
	/**
	 * Gets the StelvioPrincipal created from an object representing a userprincipal.
	 * 
	 * @return the StelvioPrincipal
	 */
	StelvioPrincipal getPrincipal();
}
