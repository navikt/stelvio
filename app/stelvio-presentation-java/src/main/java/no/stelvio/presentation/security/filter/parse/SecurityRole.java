package no.stelvio.presentation.security.filter.parse;

/**
 * This class represent a security role from web.xml.
 * 
 * @author persondab2f89862d3, Accenture
 * @version $Id$
 */
public class SecurityRole {
	
	/** The name of the security role */
	private String roleName;

	/**
	 * Gets the name of the security role
	 * @return the roleName
	 */
	public String getRoleName() {
		return roleName;
	}
	/**
	 * Sets the name of the security role
	 * @param roleName the roleName to set
	 */
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
}
