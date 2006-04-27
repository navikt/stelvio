package no.nav.common.framework.ejb;

import no.nav.common.framework.ejb.RemoteServiceDescription;
import junit.framework.TestCase;

/**
 * RemoteServiceDescription Unit Test.
 * 
 * @author person7553f5959484, Accenture
 * @version $Revision: 1049 $ $Author: psa2920 $ $Date: 2004-08-17 13:38:20 +0200 (Tue, 17 Aug 2004) $
 */
public class RemoteServiceDescriptionTest extends TestCase {

	/**
	 * Constructor for RemoteServiceDescriptionTest.
	 * @param arg0
	 */
	public RemoteServiceDescriptionTest(String arg0) {
		super(arg0);
	}

	public void test() {

		// Test data
		String jndiName = "TheJndiName";
		String providerUrl = "TheProviderUrl";
		String initialContextFactory = "TheOneAndOnlyInitialContextFactory";
		String urlPkgPrefixes = "no.trygdeetaten.framework";
		String securityPrincipal = "username";
		String securityCredentials = "secret";
		boolean cacheable = false;

		// Cunstructor
		RemoteServiceDescription r = new RemoteServiceDescription();

		// Setters
		r.setCacheable(cacheable);
		r.setInitialContextFactory(initialContextFactory);
		r.setJndiName(jndiName);
		r.setProviderUrl(providerUrl);
		r.setSecurityCredentials(securityCredentials);
		r.setSecurityPrincipal(securityPrincipal);
		r.setUrlPkgPrefixes(urlPkgPrefixes);

		// Getters
		assertEquals("Get/Set cacheable failed", cacheable, r.isCacheable());
		assertEquals("Get/Set initialContextFactory failed", initialContextFactory, r.getInitialContextFactory());
		assertEquals("Get/Set jndiName failed", jndiName, r.getJndiName());
		assertEquals("Get/Set providerUrl failed", providerUrl, r.getProviderUrl());
		assertEquals("Get/Set securityCredentials failed", securityCredentials, r.getSecurityCredentials());
		assertEquals("Get/Set securityPrincipal failed", securityPrincipal, r.getSecurityPrincipal());
		assertEquals("Get/Set urlPkgPrefixes failed", urlPkgPrefixes, r.getUrlPkgPrefixes());

	}

}
