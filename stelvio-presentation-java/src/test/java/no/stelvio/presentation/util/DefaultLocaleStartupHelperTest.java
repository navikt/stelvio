package no.stelvio.presentation.util;

import java.security.Permission;
import java.util.PropertyPermission;

import junit.framework.TestCase;

/**
 * Unit test for DefaultLocaleStartupHelper.
 * 
 * Will not work until DefaultLocaleStartupHelper is finished.
 * 
 * @author person7553f5959484, Accenture
 * @version $Id$
 */
public class DefaultLocaleStartupHelperTest extends TestCase {

	/**
	 * Test set default localøe.
	 */
	public void testSetDefaultLocaleOK() {
		new DefaultLocaleStartupHelper("no", "NO");
	}

	
	/*
	public void testSetDefaultLocaleLanguageNotValid() {
		try {
			new DefaultLocaleStartupHelper("norsk", "NO");
			fail("DefaultLocaleStartupHelper should have thrown UnrecoverableException.LOCALE_LANGUAGE_NOT_SUPPORTED");
		} catch (SystemUnrecoverableException e) {
            // should happen
		}
	}

	public void testSetDefaultLocaleCountryNotValid() {
		try {
			new DefaultLocaleStartupHelper("no", "NORGE");
			fail("DefaultLocaleStartupHelper should have thrown UnrecoverableException.LOCALE_COUNTRY_NOT_SUPPORTED");
		} catch (SystemUnrecoverableException e) {
            // should happen
		}
	}

	public void testSetDefaultLocaleLocaleNotAvailable() {
		try {
			new DefaultLocaleStartupHelper("no", "US");
			fail("DefaultLocaleStartupHelper should have thrown UnrecoverableException.LOCALE_NOT_AVAILABLE");
		} catch (SystemUnrecoverableException e) {
            // should happen
		}
	}

	public void testSetDefaultLocaleLocaleSecurityException() {
		SecurityManager oldSecurityManager = System.getSecurityManager();

		try {
			System.setSecurityManager(new TestSecurityManager());
			new DefaultLocaleStartupHelper("no", "NO");
			fail("DefaultLocaleStartupHelper should have thrown UnrecoverableException.LOCALE_SECURITY_FAILURE");
		} catch (SystemUnrecoverableException e) {
            // should happen
		} finally {
			System.setSecurityManager(oldSecurityManager);
		}
	}
	 */

	/**
	 * TestSecurityManager.
	 */
	private class TestSecurityManager extends SecurityManager {
		/**
		 * Check permissions.
		 * 
		 * @param perm permission
		 */
		public void checkPermission(Permission perm) {
			if (perm instanceof PropertyPermission) {
				throw new SecurityException("Not allowed to change system locale");
			}
		}
	}
}
