package no.stelvio.presentation.security.page.constants;

import java.io.Serializable;

/**
 * Constants used by the phaselistener module.
 * 
 * @version $Id$
 */
public final class Constants implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * J2EE role concatination rule for security evaluation.
	 */
	public static final String J2EE_ROLE_CONCATINATION_OR = "OR";

	/**
	 * J2EE role concatination rule for security evaluation.
	 */
	public static final String J2EE_ROLE_CONCATINATION_AND = "AND";

	/**
	 * Name of the previous visited JSF page. This string is set as a session
	 * attribute by the View handler so that navigation always goes back to the
	 * origin page in case of the user having no access. Value is:
	 * <code>stelvio.web.security.page.PREVIOUS_JSF_PAGE</code>
	 */
	public static final String PREVIOUS_JSF_PAGE_URL = "no.stelvio.web.security.page.PREVIOUS_JSF_PAGE";

	/**
	 * Used for session attribute to store the URL of the page that requires
	 * authentication. This way a redirect to the origin faces request page can
	 * be performed. Value is:
	 * <code>stelvio.web.security.page.JSFPAGE_TOGO_AFTER_AUTHENTICATION</code>
	 */
	public static final String JSFPAGE_TOGO_AFTER_AUTHENTICATION = 
												"no.stelvio.web.security.page.JSFPAGE_TOGO_AFTER_AUTHENTICATION";

	/**
	 * Session attribute that optionally contains an error message about the
	 * authentication failure. This attribute can be set e.g. by custom JAAS
	 * authentication. Value is:
	 * <code>stelvio.web.security.page.AUTHENTICATION_ERROR_MESSAGE</code>
	 */
	public static final String AUTHENTICATION_ERROR_MESSAGE = "no.stelvio.web.security.page.AUTHENTICATION_ERROR_MESSAGE";

	/**
	 * Context parameter. Customize the error message shown on unauthorized page
	 * access attempt. Value is:
	 * <code>stelvio.web.security.page.unauthorized_access_error_messages</code>
	 */
	public static final String UNAUTHORIZED_ACCESS_MESSAGE = "no.stelvio.web.security.page.unauthorized_access_error_messages";

	/**
	 * Context parameter. Use page cache in session. Value is:
	 * <code>stelvio.web.security.page.use_page_cache</code>
	 */
	public static final String USE_PAGE_CACHE = "no.stelvio.web.security.page.use_page_cache";

	/**
	 * Context parameter. The configuration file containing the security
	 * definitions. Value is:
	 * <code>stelvio.web.security.page.security_config_file</code>
	 */
	public static final String SECURITY_CONFIG_FILE = "no.stelvio.web.security.page.security_config_file";
	/** Should not be able to create an instance of this class. */
	private Constants() {
	}
}
