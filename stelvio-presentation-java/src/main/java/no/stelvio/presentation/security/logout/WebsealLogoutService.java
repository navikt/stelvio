package no.stelvio.presentation.security.logout;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * A logout service capable of logging the user out from both a Websphere Application Server (WAS) and a WebSEAL instance in one
 * redirect. It inherits the WasLogoutService which generates the url needed for a WAS logout, and appends the url needed to
 * terminate the user's session on WebSEAL after the WAS logoutExitPage parameter.
 * <p>
 * The url build by the WebsealLogoutService contains a WebSEAL command to terminate the current user's WebSEAL session, and a
 * parameter that specifies a custom response page at WebSEAL. The custom response page must be located in the same
 * <i>lib/html/</i> directory at WebSEAL which contains the default HTML response forms. The response page should either present
 * the user with a successful logout message or a redirect to an appropriate url.
 * <p>
 * The use of a custom response page must be configured in WebSEAL's configuration file by setting the value of the
 * <b>use-filename-for-pkmslogout</b> stanza entry in the <b>[acnt-mgt]</b> stanza to "yes".
 * <p>
 * <br>
 * The WebsealLogoutService offers default values for all its parameters except the <b>pointOfContactHostAddress</b>. This
 * parameter <b>must</b> be set and should be the absolute url of the host which is the first point of contact in what could be
 * a chain of proxy servers and load balancers. This is necessary so that WebSEAL can interpret the logout command correctly.
 * 
 * @see WasLogoutService
 * @version $Id$
 * */
public class WebsealLogoutService extends WasLogoutService implements InitializingBean {

	private static final Log LOG = LogFactory.getLog(WebsealLogoutService.class);
	private static final String WEBSEAL_LOGOUT_ACTION = "/pkmslogout";
	private String websealLogoutAction = WEBSEAL_LOGOUT_ACTION;
	private String websealLogoutFilename;
	private boolean logoutFromWebSeal = true;
	private String pointOfContactHostAddress;

	@Override
	public void afterPropertiesSet() {
		Assert.notNull(this.pointOfContactHostAddress, "A point of contact hostaddress must be specified. "
				+ " Please set the pointOfContactHostAddress property.");
		if (this.pointOfContactHostAddress != null) {
			Assert.isTrue(pointOfContactHostAddress.startsWith("http"), "The point of contact hostaddress must be absolute.");
		}

	}

	/**
	 * Builds and returns the url needed to perform a logout from both WAS and WebSEAL in one redirect. This is achieved by
	 * appending the url needed to perform a WebSEAL logout after the logoutExitPage parameter of the WasLogoutService. If the
	 * parameter logoutFromWebSeal is set to false, only the url generated by the WasLogoutService will be returned and the
	 * supplied parameter destinationUrl will be used as the logoutExitPage.
	 * 
	 * @param destinationUrl
	 *            the destinationUrl used by the WasLogoutService if logoutFromWebSeal is set to false.
	 * @return the url needed to perfom a logout from either WAS or both WAS and WebSEAL.
	 */
	@Override
	protected String buildRedirectUrl(String destinationUrl) {
		if (isLogoutFromWebSeal()) {
			String destination = generateWebsealLogoutUrl();
			return super.buildRedirectUrl(destination);
		} else {
			return super.buildRedirectUrl(destinationUrl);
		}
	}

	/**
	 * Builds a WebSEAL specific logout URL. The URL is absolute and constructed in the following way: <br>
	 * <code>[pointOfContactHostAddress]/[websealLogoutAction]?filename=[websealLogoutFilename]</code> <br>
	 * <br>
	 * <b>Example url:</b> <code>https://webseal.somedomain.no/pkmslogout?filename=logout_success.html </code>
	 * 
	 * 
	 * @return the URL used to log out from the current session at WebSEAL.
	 */
	protected String generateWebsealLogoutUrl() {

		if (LOG.isDebugEnabled()) {
			LOG.debug("Generating the URL to perform a WebSEAL logout.");
		}

		StringBuffer url = new StringBuffer();
		String tempURL = pointOfContactHostAddress;
		if (tempURL != null && tempURL.endsWith("/")) {
			int end = tempURL.lastIndexOf("/");
			pointOfContactHostAddress = tempURL.substring(0, end);
		}
		url.append(pointOfContactHostAddress);

		if (websealLogoutFilename != null) {
			url.append(websealLogoutAction).append("?filename=").append(websealLogoutFilename);
		} else {
			url.append(websealLogoutAction);
		}
		if (LOG.isDebugEnabled()) {
			LOG.debug("Point of contact host address is:" + pointOfContactHostAddress);
			LOG.debug("The logout command is:" + websealLogoutAction);
			LOG.debug("The WebSEAL logout filename is:" + websealLogoutFilename);
			LOG.debug("Final generated URL:" + url.toString());
		}
		return url.toString();
	}

	/**
	 * Checks whether or not a WebSEAL logout should be performed.
	 * 
	 * @return true or false
	 */
	public boolean isLogoutFromWebSeal() {
		return logoutFromWebSeal;
	}

	/**
	 * Sets that a logout from WebSEAL should be performed.
	 * 
	 * @param logoutFromWebSeal
	 *            true or false
	 */
	public void setLogoutFromWebSeal(boolean logoutFromWebSeal) {
		this.logoutFromWebSeal = logoutFromWebSeal;
	}

	/**
	 * Gets the filename of the html file on WebSEAL that should be used as the response page after a logout.
	 * 
	 * @return the filename
	 */
	public String getWebsealLogoutFilename() {
		return websealLogoutFilename;
	}

	/**
	 * Sets the filename of the html file on WebSEAL that should be used as the response page after a logout.
	 * 
	 * @param websealLogoutFilename feil name
	 */
	public void setWebsealLogoutFilename(String websealLogoutFilename) {
		this.websealLogoutFilename = websealLogoutFilename;
	}

	/**
	 * Gets the absolute url of the host which is the first point of contact in what could be a chain of proxy servers and load
	 * balancers.
	 * 
	 * @return the point of contact hostaddress.
	 */
	public String getPointOfContactHostAddress() {
		return pointOfContactHostAddress;
	}

	/**
	 * Sets the absolute url of the host which is the first point of contact in what could be a chain of proxy servers and load
	 * balancers.
	 * 
	 * @param pointOfContactHostAddress
	 *            the absolute url to set.
	 */
	public void setPointOfContactHostAddress(String pointOfContactHostAddress) {
		this.pointOfContactHostAddress = pointOfContactHostAddress;
	}
}
