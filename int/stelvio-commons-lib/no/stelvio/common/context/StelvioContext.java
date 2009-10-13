package no.stelvio.common.context;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.security.auth.Subject;

import no.stelvio.common.util.ExceptionUtils;

import com.ibm.websphere.security.auth.WSSubject;
import com.ibm.websphere.security.cred.WSCredential;
import com.ibm.websphere.workarea.UserWorkArea;
import com.ibm.ws.session.WBISessionManager;

/**
 * @author test@example.com
 */
public class StelvioContext {
	private final static String className = StelvioContext.class.getName();
	private final Logger log = Logger.getLogger(className);

	public static final String DEFAULT_USER_NAME = "UNKNOWN";
	public static final String DEFAULT_LANGUAGE = "no";
	public static final String DEFAULT_APPLICATION_NAME = "UNKN";

	private String applicationId = null;
	private String correlationId = null;
	private String languageId = null;
	private String navUserId = null;
	private String userId = null;

	public StelvioContext(UserWorkArea workArea) {
		setStelvioBusContext(new UserWorkAreaContextAdapter(workArea));
	}

	/**
	 * internal method to set the context
	 */
	private void setStelvioBusContext(UserWorkAreaContextAdapter workArea) {
		String wName = workArea.getWorkAreaName();
		if (UserWorkAreaContextAdapter.WORK_AREA_NAME.equalsIgnoreCase(wName)) {
			log.logp(Level.FINE, className, "setStelvioBusContext()", "StelvioContext exists in WorkArea");

			userId = workArea.getUserId();
			if (userId == null || userId.length() <= 0) {
				userId = DEFAULT_USER_NAME;
			}

			languageId = workArea.getLanguageId();
			if (languageId == null || languageId.length() <= 0) {
				languageId = DEFAULT_LANGUAGE;
			}

			applicationId = workArea.getApplicationId();
			if (applicationId == null || applicationId.length() <= 0) {
				applicationId = DEFAULT_APPLICATION_NAME;
			}

			correlationId = workArea.getCorrelationId();
			if (correlationId == null || correlationId.length() <= 0) {
				correlationId = getWBISessionId();
			}
		} else {
			// another WorkArea
			log.logp(Level.FINE, className, "setStelvioBusContext()",
					"StelvioContext doesn't exists within WorkArea - use default values");
			userId = DEFAULT_USER_NAME;
			languageId = DEFAULT_LANGUAGE;
			applicationId = DEFAULT_APPLICATION_NAME;
			correlationId = getWBISessionId();
		}

		// Set NAV-user from security-credential
		navUserId = getSecurityIdentity();
		if (navUserId == null || navUserId.length() <= 0) {
			navUserId = DEFAULT_USER_NAME;
		}
	}

	/**
	 * getWBISessionId
	 */
	private String getWBISessionId() {
		String wbiSessionId;

		WBISessionManager sessionManager = WBISessionManager.getInstance();
		if (sessionManager.isSessionExisted()) {
			wbiSessionId = sessionManager.getSessionContext().getSessionId();
		} else {
			// TODO
			wbiSessionId = null;
		}

		log.logp(Level.FINE, className, "getWBISessioId()", "WBISessionId=" + wbiSessionId);

		return wbiSessionId;
	}

	/**
	 * @return internal systemUserId.
	 */
	private String getSecurityIdentity() {
		String tName = Thread.currentThread().getName();
		String sysUser = null;

		log.logp(Level.FINE, className, "getInternIdentity()", "Credential executing on the thread " + tName);
		try {
			Subject runAsSecurity = WSSubject.getRunAsSubject();
			if (runAsSecurity != null) {
				Set security_credentials = runAsSecurity.getPublicCredentials(WSCredential.class);
				WSCredential security_credential = (WSCredential) security_credentials.iterator().next();
				sysUser = (String) security_credential.getSecurityName();

				log.logp(Level.FINE, className, "getInternIdentity()", "return " + sysUser);
			}
		} catch (Exception e) {
			log.logp(Level.SEVERE, className, "getInternIdentity()", "CatchedError: " + ExceptionUtils.getStackTrace(e));
		}

		return sysUser;
	}

	public String getApplicationId() {
		return applicationId;
	}

	public String getCorrelationId() {
		return correlationId;
	}

	public String getLanguageId() {
		return languageId;
	}

	public String getNavUserId() {
		return navUserId;
	}

	public String getUserId() {
		return userId;
	}
}
