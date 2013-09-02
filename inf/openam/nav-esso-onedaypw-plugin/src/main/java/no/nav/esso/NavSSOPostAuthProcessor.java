package no.nav.esso;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.iplanet.sso.SSOException;
import com.iplanet.sso.SSOToken;
import com.sun.identity.authentication.spi.AMPostAuthProcessInterface;
import com.sun.identity.authentication.spi.AuthenticationException;

public class NavSSOPostAuthProcessor implements AMPostAuthProcessInterface {

	@Override
	public void onLoginFailure(Map map, HttpServletRequest httpservletrequest,
			HttpServletResponse httpservletresponse)
			throws AuthenticationException {
		
	}

	@Override
	public void onLoginSuccess(Map map, HttpServletRequest httpservletrequest,
			HttpServletResponse httpservletresponse, SSOToken ssotoken)
			throws AuthenticationException {
		try {
			ssotoken.setProperty("SecurityLevel", new Integer(ssotoken.getAuthLevel()).toString());
		} catch (SSOException e) {
			throw new AuthenticationException("NavSSOPostAuthProcessor onLoginSuccess failed: " + e.getMessage());
		}
		
	}

	@Override
	public void onLogout(HttpServletRequest httpservletrequest,
			HttpServletResponse httpservletresponse, SSOToken ssotoken)
			throws AuthenticationException {
		
	}

}
