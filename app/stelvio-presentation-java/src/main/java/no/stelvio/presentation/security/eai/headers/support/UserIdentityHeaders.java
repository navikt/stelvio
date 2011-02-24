package no.stelvio.presentation.security.eai.headers.support;

import no.stelvio.presentation.security.eai.headers.config.EaiHeaderConfig;
import no.stelvio.presentation.security.eai.headers.config.support.ExtendedAttributeEnum;
import no.stelvio.presentation.security.eai.headers.config.support.StanzaEntryEnum;

/**
 * UserIdentityHeaders.
 * 
 * @author persondab2f89862d3, Accenture
 * @version $Id$
 * @see AbstractEaiHeaders
 * @see EaiHeaderConfig
 * @see StanzaEntryEnum
 * @see ExtendedAttributeEnum
 * 
 */
public class UserIdentityHeaders extends AbstractEaiHeaders {

	/**
	 * Creates a new instance of UserIdentityHeaders.
	 * 
	 * @param config
	 *            configuration
	 */
	public UserIdentityHeaders(EaiHeaderConfig config) {
		super(config);
	}

	/**
	 * Set user identity.
	 * 
	 * @param userId
	 *            used id
	 */
	public void setUserIdentity(String userId) {
		super.addHeader(StanzaEntryEnum.EAI_USER_ID_HEADER, userId);
	}

	/**
	 * Set authentication level.
	 * 
	 * @param authLevel
	 *            authentication level.
	 */
	public void setAuthenticationLevel(String authLevel) {
		super.addHeader(StanzaEntryEnum.EAI_AUTH_LEVEL_HEADER, authLevel);
		setExtendedAttribute(ExtendedAttributeEnum.XATTR_AUTHENTICATION_LEVEL, authLevel);
	}

	/**
	 * Requires a header with value = commaseparated list of headernames.
	 * 
	 * @param property
	 *            property
	 * @param attribute
	 *            attribute
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

	/**
	 * Set original user id.
	 * 
	 * @param origUserId
	 *            original user id
	 */
	public void setOriginalUserId(String origUserId) {
		setExtendedAttribute(ExtendedAttributeEnum.XATTR_ORIGINAL_USER, origUserId);
	}
}
