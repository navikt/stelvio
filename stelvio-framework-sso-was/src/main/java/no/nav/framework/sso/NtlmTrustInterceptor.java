package no.nav.framework.sso;

import java.util.Enumeration;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.websphere.security.WebTrustAssociationException;
import com.ibm.websphere.security.WebTrustAssociationFailedException;
import com.ibm.websphere.security.WebTrustAssociationUserException;
import com.ibm.wsspi.security.tai.TAIResult;
import com.ibm.wsspi.security.tai.TrustAssociationInterceptor;

/**
 * TrustAssociationInterceptor som kan benyttes i WebSphere Application Server 6.0 
 * sammen med jcifs.http.NtlmServlet. Implementasjonen forventer å finne brukernavnet 
 * under HttpSession attributtet "ntlmuser".
 * <p/>
 * Konfigurasjon for WebSphere Application Server 6.0.
 * <ol>
 * 	<li>Klassen legges til under <code>/lib</code> for å bli funnet av riktig classloader.</li>
 *  <li>Konfigureres i WebSphere Administrative Console under <code>Security | Global security | Authentication</code></li>
 *  <li>Authentication mechanisms | LTPA | Trust association | Interceptors</li>
 * </ol>
 * <p/>
 * Konfigurasjon for WebSphere Studio Application developer 5.1.2.
 * <p/>
 * Konfigureres ved å sette denne i LTPA > Trust Association > Trust Interceptor
 * og sette ws.ext.dirs på serveren til å inkludere hvor denne filen ligger (E:\data\workspace\head\nav-framework-sso-was\bin)
 * og E:\apps\IBM\WebSphere Studio\Application Developer\v5.1.1\runtimes\base_v51\lib\wssec.jar.
 * 
 * @author person1f201b37d484, Accenture
 * @author personbf936f5cae20, Accenture
 * @author person7553f5959484, Accenture
 * @version $Id: NtlmTrustInterceptor.java 2830 2006-03-05 10:27:28Z skb2930 $
 */
public class NtlmTrustInterceptor implements TrustAssociationInterceptor {

	/** System Property som angir om det skal skrives til System.out. */
	private static final String DEBUG = "NtlmTrustInterceptor.Debug";

	private void debug(String message) {
		if (null != System.getProperty(DEBUG)) {
			System.out.println("[no.nav.framework.sso.NtlmTrustInterceptor] - " + message);
		}
	}

	public boolean isTargetInterceptor(HttpServletRequest req) throws WebTrustAssociationException {
		debug("isTargetInterceptor kallt");

		// return true if this is the target interceptor, else return false.
		String ntlmuser = (String) req.getSession().getAttribute("ntlmuser");
		debug("ntlmuser: " + ntlmuser);

		Enumeration enumeration = req.getSession().getAttributeNames();
		while (enumeration.hasMoreElements()) {
			debug(enumeration.nextElement().toString());
		}

		return null != ntlmuser;
	}

	public TAIResult negotiateValidateandEstablishTrust(HttpServletRequest req, HttpServletResponse resp)
		throws WebTrustAssociationFailedException {
		//validate if the request is from the trusted proxy server.
		//throw exception if the request is not from the trusted server.
		debug("negotiateValidateandEstablishTrust kallt");

		try {
			String id = getAuthenticatedUsername(req);
			debug("negotiateValidateandEstablishTrust id: " + id);

			return TAIResult.create(HttpServletResponse.SC_OK, id);
		} catch (WebTrustAssociationUserException e) {
			throw new WebTrustAssociationFailedException(e.getMessage());
		}
	}

	private String getAuthenticatedUsername(HttpServletRequest req) throws WebTrustAssociationUserException {
		//Get the user name from the request and if the user is 
		//entitled to the requested resource 
		//return the user. Otherwise, throw the exception
		String ntlmuser = (String) req.getSession().getAttribute("ntlmuser");

		if (null == ntlmuser) {
			throw new WebTrustAssociationUserException("ntlmuser should not be null");
		}

		return ntlmuser;
	}

	public void cleanup() {
		//Cleanup code.
	}

	public int initialize(Properties arg0) throws WebTrustAssociationFailedException {
		// Initialize the implementation. 
		// If successful return 0, else return -1.				
		return 0;
	}

	public String getVersion() {
		return "1.0";
	}

	public String getType() {
		return this.getClass().getName();
	}
}