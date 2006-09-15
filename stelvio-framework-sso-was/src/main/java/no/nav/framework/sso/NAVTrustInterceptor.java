package no.nav.framework.sso;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import com.ibm.websphere.security.WebTrustAssociationException;
import com.ibm.websphere.security.WebTrustAssociationFailedException;
import com.ibm.websphere.security.WebTrustAssociationUserException;

/**
 * Konfigureres ved å sette denne i LTPA > Trust Association > Trust Interceptor
 * og sette ws.ext.dirs på serveren til å inkludere hvor denne filen ligger (E:\data\workspace\head\nav-framework-sso-was\bin)
 * og
 * E:\apps\IBM\WebSphere Studio\Application Developer\v5.1.1\runtimes\base_v51\lib\wssec.jar.
 * @author person1f201b37d484, Accenture
 * @version $Id: NAVTrustInterceptor.java 2654 2005-11-28 11:14:54Z shc2920 $
 * @deprecated bruk no.nav.framework.sso.NtlmTrustInterceptor istedenfor.
 */
public class NAVTrustInterceptor
	extends com.ibm.websphere.security.WebSphereBaseTrustAssociationInterceptor
	implements com.ibm.websphere.security.TrustAssociationInterceptor {

	public boolean isTargetInterceptor(HttpServletRequest req) throws WebTrustAssociationException {
		// return true if this is the target interceptor, else return false.
		String ntlmuser = (String) req.getSession().getAttribute("ntlmuser");
		if (ntlmuser != null) {
			return true;
		}
		return false;
	}

	public void validateEstablishedTrust(HttpServletRequest req) throws WebTrustAssociationFailedException {
		//validate if the request is from the trusted proxy server.
		//throw exception if the request is not from the trusted server.
	}

	public String getAuthenticatedUsername(HttpServletRequest req) throws WebTrustAssociationUserException {
		//Get the user name from the request and if the user is 
		//entitled to the requested resource 
		//return the user. Otherwise, throw the exception
		String ntlmuser = (String) req.getSession().getAttribute("ntlmuser");
		if (ntlmuser != null) {
			return ntlmuser;
		}
		throw new WebTrustAssociationUserException();
	}

	public int init(Properties props) {
		// Initialize the implementation. 
		// If successful return 0, else return -1.
		return 0;
	}

	public void cleanup() {
		//Cleanup code.
	}
}