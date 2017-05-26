package no.stelvio.presentation.security.eai.support;

import no.stelvio.presentation.security.eai.headers.config.EaiHeaderConfig;
import no.stelvio.presentation.security.eai.headers.config.support.ExtendedAttributeEnum;
import no.stelvio.presentation.security.eai.headers.config.support.StanzaEntryEnum;
import no.stelvio.presentation.security.eai.headers.support.UserIdentityHeaders;

/**
 * A class representing the headers that should be sent to IBM Tivoli Access Manager Webseal when reauthenticating as another
 * user with a certain authorization type.
 * 
 * @author persondab2f89862d3, Accenture
 * @see UserIdentityHeaders
 */
public class ReAuthenticationHeaders extends UserIdentityHeaders {

	/**
	 * Constructs a ReAuthenticationHeaders instance with the EaiHeaderConfig parameter.
	 * 
	 * @param config
	 *            the header configuration.
	 */
	public ReAuthenticationHeaders(EaiHeaderConfig config) {
		super(config);
	}

	/**
	 * Sets the userId of the user to act on behalf of in the ExtendedAttributeEnum.XATTR_AUTHORIZED_AS header.
	 * 
	 * @param userId
	 *            the userId of the user to act on behalf of.
	 */
	public void setAuthorizedAs(String userId) {
		setExtendedAttribute(ExtendedAttributeEnum.XATTR_AUTHORIZED_AS, userId);
	}

	/**
	 * Sets the type of authorization that is given to a user when acting on behalf of another user in
	 * ExtendedAttributeEnum.XATTR_AUTHORIZATION_TYPE header.
	 * 
	 * @param type
	 *            the type of authorization.
	 */
	public void setAuthorizationType(String type) {
		setExtendedAttribute(ExtendedAttributeEnum.XATTR_AUTHORIZATION_TYPE, type);
	}

	/**
	 * Private helper method which constructs a header with the name of the ExtendedAttributeEnum property parameter and the
	 * value of the attribute parameter. The header name is then inserted as a value in a comma-separated list of a header with
	 * the name of the StanzaEntryEnum.EAI_XATTRS_HEADER property.
	 * 
	 * These headers are needed in order to use custom headers with the EAI mechanism of IBM Tivoli Access Manager Webseal.
	 * 
	 * @param property
	 *            the property key to use to extract the header name
	 * @param attribute
	 *            the attribute to set in the header
	 */
	private void setExtendedAttribute(ExtendedAttributeEnum property, String attribute) {
		String origHeader = super.getHeader(StanzaEntryEnum.EAI_XATTRS_HEADER);
		String headerName = super.getConfig().getHeaderName(property);
		if (origHeader != null) {
			// Add to the commaseparated list, unless it's already there
			origHeader = origHeader.contains(headerName) ? origHeader : origHeader + "," + headerName;
			super.addHeader(StanzaEntryEnum.EAI_XATTRS_HEADER, origHeader);
		} else {
			super.addHeader(StanzaEntryEnum.EAI_XATTRS_HEADER, headerName);
		}
		super.addHeader(property, attribute);
	}
}
