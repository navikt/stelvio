package no.stelvio.presentation.security.page.definition.parse.support;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import no.stelvio.presentation.security.page.constants.Constants;

/**
 * 
 * Object representing one JSF page. Holds the parsed page security definitions obtained from <code>SecurityConfig</code>.
 * 
 * @version $Id$
 */
public class JsfPage implements Serializable {

	private static final long serialVersionUID = 1L;

	private String pageName = null;

	private boolean requiresAuthentication = false;

	private boolean requiresAuthorization = false;

	private boolean requiresSSL = false;

	private String concatinationRule = Constants.J2EE_ROLE_CONCATINATION_AND;

	private List<JeeRoles> roleSets;

	/**
	 * Sets the page to require authentication with boolean parameter.
	 * 
	 * @param requiresAuthentication
	 *            wheter or not the page requires authentication.
	 */
	public void setRequiresAuthentication(boolean requiresAuthentication) {
		this.requiresAuthentication = requiresAuthentication;
	}

	/**
	 * Sets the page to require authentication with string parameter.
	 * 
	 * @param requiresAuthentication
	 *            wheter or not the page requires authentication. Should be "true" or "false".
	 */
	public void setRequiresAuthentication(String requiresAuthentication) {
		this.requiresAuthentication = Boolean.valueOf(requiresAuthentication);
	}

	/**
	 * Checks if page requires authentication.
	 * 
	 * @return <code>true</code> if page requires authentication, <code>false</code> otherwise.
	 */
	public boolean requiresAuthentication() {
		return this.requiresAuthentication;
	}

	/**
	 * Sets the page to require authorization with boolean parameter. If set to <code>true</code>, the page will also be set to
	 * require authentication.
	 * 
	 * @param requiresAuthorization
	 *            wheter or not the page requires authorization.
	 */
	public void setRequiresAuthorization(boolean requiresAuthorization) {
		this.requiresAuthorization = requiresAuthorization;
		// authorization is based on authentication
		if (this.requiresAuthorization) {
			setRequiresAuthentication(this.requiresAuthorization);
		}
	}

	/**
	 * Sets the page to require authorization with string parameter. If set to "true", the page will also be set to require
	 * authentication.
	 * 
	 * @param requiresAuthorization
	 *            wheter or not the page requires authorization. Should be "true" or "false";
	 */
	public void setRequiresAuthorization(String requiresAuthorization) {
		this.requiresAuthorization = Boolean.valueOf(requiresAuthorization);
		// authorization is based on authentication
		if (this.requiresAuthorization) {
			setRequiresAuthentication(this.requiresAuthorization);
		}
	}

	/**
	 * Checks if page requires authorization.
	 * 
	 * @return <code>true</code> if page requires authorization, <code>false</code> otherwise.
	 */
	public boolean requiresAuthorization() {
		return this.requiresAuthorization;
	}

	/**
	 * Sets the page to require SSL with string parameter.
	 * 
	 * @param requiresSSL
	 *            wheter or not the page requires SSL. Should be true or false.
	 */
	public void setRequiresSSL(boolean requiresSSL) {
		this.requiresSSL = requiresSSL;
	}

	/**
	 * Sets the page to require SSL with string parameter.
	 * 
	 * @param requiresSSL
	 *            wheter or not the page requires SSL. Should be "true" or "false".
	 */
	public void setRequiresSSL(String requiresSSL) {
		this.requiresSSL = Boolean.valueOf(requiresSSL);
	}

	/**
	 * Checks if page requires SSL.
	 * 
	 * @return <code>true</code> if page requires SSL, <code>false</code> otherwise.
	 */
	public boolean requiresSSL() {
		return this.requiresSSL;
	}

	/**
	 * Adds a list of J2EE roles required by this page.
	 * 
	 * @param roles
	 *            - List of J2EE roles required by the page authorization
	 */
	public void addRoles(JeeRoles roles) {
		if (roleSets == null) {
			roleSets = new ArrayList<>();
		}
		roleSets.add(roles);
		// this.roles = roles;
	}

	/**
	 * Gets the list of J2EE roles associated with this page.
	 * 
	 * @return List of J2EE roles required by the page authorization
	 */
	public List<JeeRoles> getRoleSets() {
		if (roleSets == null) {
			roleSets = new ArrayList<>();
		}
		return roleSets;
	}

	/**
	 * Sets how the J2EE roles should be treated when evaluating this page, i.e. if it is required to have all roles or just one
	 * to access the page.
	 * 
	 * @param concatinationRule
	 *            Determines how J2EE roles are treated. <br>
	 *            Values are:
	 * 
	 *            <code>no.stelvio.web.security.page.Constant.J2EE_ROLE_CONCATINATION_OR</code>,
	 *            <code>no.stelvio.web.security.page.Constant.J2EE_ROLE_CONCATINATION_AND</code>
	 */
	public void setConcatinationRule(String concatinationRule) {
		this.concatinationRule = concatinationRule;
	}

	/**
	 * Gets how the J2EE roles should be treated when evaluating this page, i.e. if it is required to have all roles or just one
	 * to access the page.
	 * 
	 * @return Determines how J2EE roles are treated. Values are: <br>
	 *         Values are:
	 * 
	 *         <code>no.stelvio.web.security.page.Constant.J2EE_ROLE_CONCATINATION_OR</code>,
	 *         <code>no.stelvio.web.security.page.Constant.J2EE_ROLE_CONCATINATION_AND</code>
	 */
	public String getConcatinationRule() {
		return this.concatinationRule;
	}

	/**
	 * Sets the name of the page.
	 * 
	 * @param page
	 *            the page name.
	 */
	public void setPageName(String page) {
		this.pageName = page;
	}

	/**
	 * Gets the name of the page.
	 * 
	 * @return the page name.
	 */
	public String getPageName() {
		return this.pageName;
	}

	/**
	 * Returns all security constraints of this page as a string.
	 * 
	 * @return a string representation of this class.
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer(getClass().getName());
		sb.append(": Pagename=").append(this.pageName);
		sb.append(",Requires authentication=").append(this.requiresAuthentication);
		sb.append(",Requires authorization=").append(this.requiresAuthorization);
		sb.append(",Requires SSL=").append(this.requiresSSL);
		sb.append(",Role concatination rule=").append(this.concatinationRule);
		sb.append(",RoleSets=").append(this.roleSets);
		return sb.toString();
	}

}
