package no.stelvio.common.security.ws;

import javax.security.auth.Subject;
import javax.security.auth.login.CredentialExpiredException;

import com.ibm.websphere.security.WSSecurityException;
import com.ibm.websphere.security.auth.CredentialDestroyedException;
import com.ibm.websphere.security.auth.WSSubject;
import com.ibm.websphere.security.cred.WSCredential;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A static wrapper class used to retrieve attributes from a custom Subject created on an IBM Websphere server.
 * 
 * @version $Id: WSCustomSubject.java $
 */
public final class WSCustomSubject {

	private static final Log LOGGER = LogFactory.getLog(WSCustomSubject.class);
	/**
	 * Should match the same parameter set inside the WebsphereSubjectMapper class from the project stelvio-security-tai-java.
	 */
	private static final String AUTHORIZED_AS = "no.stelvio.presentation.security.sso.ibm.WebsphereSubjectMapper.AUTHORIZED_AS";

	/**
	 * Private constructor.
	 */
	private WSCustomSubject() {
	}

	/**
	 * Retrives the user id from a users credential.
	 * 
	 * @return user id, or null if no user id exists in the users credential.
	 * @throws WSSubjectSecurityException
	 *             thrown if it fails to get the caller identity.
	 */
	public static String getUserId() throws WSSubjectSecurityException {

		try {
			WSCredential credential = getWSCredential();
			if (credential != null) {
				LOGGER.debug("Getting the WSCredential getSecurityName(): " + credential.getSecurityName());
				return credential.getSecurityName();
			} else {
				LOGGER.debug("The WSCredential is null.");
				return null;
			}
		} catch (CredentialExpiredException e) {
			throw new WSSubjectSecurityException(
					"A CredentialExpiredException occurred when getting the securityname from the WSCredential.", e);
		} catch (CredentialDestroyedException e) {
			throw new WSSubjectSecurityException("A CredentialDestroyedException occurred when getting the WSCredential.", e);
		} catch (WSSecurityException e) {
			throw new WSSubjectSecurityException("A WSSecurityException occurred when getting the WSCredential.", e);
		}
	}

	/**
	 * Retrieves user id to the person the user is authorized as.
	 * 
	 * @return user id to the person the user is authorized as, or null if the user id doesn't exist in the users credential.
	 */
	public static String getAuthorizedAs() {
		try {
			Subject subject = WSSubject.getCallerSubject();

			if (subject != null) {
				Object[] creds = subject.getPublicCredentials().toArray();
				if (LOGGER.isDebugEnabled()) {
					for (Object object : creds) {
						LOGGER.debug("Getting the WSCredential: " + object.toString());
					}
				}
				WSCredential cred = getWSCredential();
				Object auth = cred.get(AUTHORIZED_AS);

				LOGGER.debug("Getting the authorizedAs attribute from the WSCredential:" + auth);
				String authorizedAs = auth != null ? (String) auth : getUserId();
				return authorizedAs;
			} else {
				// The subject is null
				return null;
			}
		} catch (WSSecurityException e) {
			throw new WSSubjectSecurityException("A WSSecurityException occurred when getting the Subject from WSSubject.", e);
		} catch (CredentialExpiredException e) {
			throw new WSSubjectSecurityException("A CredentialExpiredException occurred when getting the" + AUTHORIZED_AS
					+ " attribute from the WSCredential.", e);
		} catch (CredentialDestroyedException e) {
			throw new WSSubjectSecurityException("A CredentialDestroyedException occurred when getting the" + AUTHORIZED_AS
					+ " attribute from the WSCredential.", e);
		}
	}

	/**
	 * Gets the WSCredential of the current user from the WSSubject.
	 * 
	 * @return the users WSCredential.
	 * @throws WSSecurityException
	 *             thrown if it fails to get the caller identity.
	 */
	public static WSCredential getWSCredential() throws WSSecurityException {
		Subject subject = WSSubject.getCallerSubject();
		WSCredential credential;
		if (subject != null) {
			credential = subject.getPublicCredentials(WSCredential.class).iterator().next();
			return credential;
		} else {
			return null;
		}
	}
}
