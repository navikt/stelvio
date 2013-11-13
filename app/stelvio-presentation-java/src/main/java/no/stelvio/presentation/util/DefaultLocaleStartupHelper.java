package no.stelvio.presentation.util;

import java.util.Locale;

import no.stelvio.common.error.SystemUnrecoverableException;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.logging.LogFactory;

/**
 * Utility for setting the system's deafult locale.
 * 
 * This class is not in use and are not finished. It should have its own exception class and messageTemplate should be
 * implemented.
 * 
 * @author person7553f5959484, Accenture
 * @version $Id$
 */
public class DefaultLocaleStartupHelper {

	/**
	 * Sets the specified locale as system default. The language code must be defined in ISO-639, country code defined in
	 * ISO-3166, and the locale must be installed and available on the operating system.
	 * <p/>
	 * The security policy file used by the JVM must contain the following lines:
	 * 
	 * <pre>
	 * grant {
	 * 	permission java.util.PropertyPermission &quot;user.language&quot;, &quot;write&quot;;
	 * }
	 * </pre>
	 * 
	 * To specify another security policy file than the default, add the following system property when starting the server:
	 * <code>-Djava.security.policy=PATH_TO_YOUR_NEW_POLICY_FILE</code>.
	 * 
	 * @param language
	 *            the two-letter lowercase ISO-639 language code, e.g. "no","se" and "en"
	 * @param country
	 *            the two-letter uppercase ISO-3166 country code, e.g. "NO", "SE", "GB" and "US"
	 */
	public DefaultLocaleStartupHelper(String language, String country) {
		if (!ArrayUtils.contains(Locale.getISOLanguages(), language)) {
			throw createSystemException(language);
		}

		if (!ArrayUtils.contains(Locale.getISOCountries(), country)) {
			throw createSystemException(country);
		}

		Locale defaultLocale = new Locale(language, country);

		if (!ArrayUtils.contains(Locale.getAvailableLocales(), defaultLocale)) {
			throw createSystemException(defaultLocale.toString());
		}

		// Set the locale as the system default locale
		Locale.setDefault(defaultLocale);
		LogFactory.getLog(getClass()).info("Default System Locale set to " + defaultLocale.toString());
	}

	/**
	 * Private helper method to create a SystemException.
	 * 
	 * @param language
	 *            The language in the application
	 * @return SystemUnrecoverableException
	 */
	private SystemUnrecoverableException createSystemException(String language) {
		return new SystemUnrecoverableException(language) {
			private static final long serialVersionUID = 9169389372453059598L;

			protected String messageTemplate(final int numArgs) {
				return null;
			}
		};
	}
}
