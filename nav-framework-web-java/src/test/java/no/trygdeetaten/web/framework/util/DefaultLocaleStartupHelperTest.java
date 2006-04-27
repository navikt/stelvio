package no.trygdeetaten.web.framework.util;

import java.security.Permission;
import java.util.PropertyPermission;

import junit.framework.TestCase;

import no.trygdeetaten.common.framework.FrameworkError;
import no.trygdeetaten.common.framework.error.SystemException;

/**
 * Unit test for DefaultLocaleStartupHelper.
 * 
 * @author person7553f5959484, Accenture
 * @version $Revision: 2185 $ $Author: skb2930 $ $Date: 2005-04-05 14:04:41 +0200 (Tue, 05 Apr 2005) $
 */
public class DefaultLocaleStartupHelperTest extends TestCase {

	public void testSetDefaultLocaleOK() {
		new DefaultLocaleStartupHelper("no", "NO");
	}

	public void testSetDefaultLocaleLanguageNotValid() {
		try {
			new DefaultLocaleStartupHelper("norsk", "NO");
			fail("DefaultLocaleStartupHelper should have thrown SystemException.LOCALE_LANGUAGE_NOT_SUPPORTED");
		} catch (SystemException e) {
			assertTrue(
				"DefaultLocaleStartupHelper should have thrown SystemException.LOCALE_LANGUAGE_NOT_SUPPORTED",
				e.getErrorCode() == FrameworkError.LOCALE_LANGUAGE_NOT_SUPPORTED.getCode());
		}
	}

	public void testSetDefaultLocaleCountryNotValid() {
		try {
			new DefaultLocaleStartupHelper("no", "NORGE");
			fail("DefaultLocaleStartupHelper should have thrown SystemException.LOCALE_COUNTRY_NOT_SUPPORTED");
		} catch (SystemException e) {
			assertTrue(
				"DefaultLocaleStartupHelper should have thrown SystemException.LOCALE_COUNTRY_NOT_SUPPORTED",
				e.getErrorCode() == FrameworkError.LOCALE_COUNTRY_NOT_SUPPORTED.getCode());
		}
	}

	public void testSetDefaultLocaleLocaleNotAvailable() {
		try {
			new DefaultLocaleStartupHelper("no", "US");
			fail("DefaultLocaleStartupHelper should have thrown SystemException.LOCALE_NOT_AVAILABLE");
		} catch (SystemException e) {
			assertTrue(
				"DefaultLocaleStartupHelper should have thrown SystemException.LOCALE_NOT_AVAILABLE",
				e.getErrorCode() == FrameworkError.LOCALE_NOT_AVAILABLE.getCode());
		}
	}

	public void testSetDefaultLocaleLocaleSecurityException() {
		SecurityManager oldSecurityManager = System.getSecurityManager();

		try {
			System.setSecurityManager(new TestSecurityManager());
			new DefaultLocaleStartupHelper("no", "NO");
			fail("DefaultLocaleStartupHelper should have thrown SystemException.LOCALE_SECURITY_FAILURE");
		} catch (SystemException e) {
			assertTrue(
			        "DefaultLocaleStartupHelper should have thrown SystemException.LOCALE_SECURITY_FAILURE",
			        e.getErrorCode() == FrameworkError.LOCALE_SECURITY_FAILURE.getCode());
		} finally {
			System.setSecurityManager(oldSecurityManager);
		}
	}

	private class TestSecurityManager extends SecurityManager {
		public void checkPermission(Permission perm) {
			if (perm instanceof PropertyPermission) {
				throw new SecurityException("Not allowed to change system locale");
			}
		}
	}
}
