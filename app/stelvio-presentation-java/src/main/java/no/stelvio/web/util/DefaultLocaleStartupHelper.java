package no.stelvio.web.util;

import java.util.Locale;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.LogFactory;

import no.stelvio.common.error.SystemException;

/**
 * Utility for setting the system's deafult locale.
 * 
 * @author person7553f5959484, Accenture
 * @version $Revision: 2589 $ $Author: skb2930 $ $Date: 2005-10-27 08:41:29 +0200 (Thu, 27 Oct 2005) $
 * @todo we might need this in new framework.
 */
public class DefaultLocaleStartupHelper {

	/**
	 * Sets the specified locale as system default. The language code must be defined in ISO-639, 
	 * country code defined in ISO-3166, and the locale must be installed and available on the 
	 * operating system.
	 * <p/>
	 * The security policy file used by the JVM must contain the following lines:
	 * <pre>
	 * grant {
	 * 	permission java.util.PropertyPermission "user.language", "write";
	 * }
	 * </pre>
	 * To specify another security policy file than the default, add the following system property when starting 
	 * the server: <code>-Djava.security.policy=PATH_TO_YOUR_NEW_POLICY_FILE</code>.
	 * 
	 * @param language the two-letter lowercase ISO-639 language code, e.g. "no","se" and "en"
	 * @param country the two-letter uppercase ISO-3166 country code, e.g. "NO", "SE", "GB" and "US" 
	 */
	public DefaultLocaleStartupHelper(String language, String country) {
		if (!ArrayUtils.contains(Locale.getISOLanguages(), language)) {
			throw new SystemException(language);
		}

		if (!ArrayUtils.contains(Locale.getISOCountries(), country)) {
			throw new SystemException(country);
		}

		Locale defaultLocale = new Locale(language, country);

		if (!ArrayUtils.contains(Locale.getAvailableLocales(), defaultLocale)) {
			throw new SystemException(defaultLocale.toString());
		}

		// Set the locale as the system default locale
		try {
			Locale.setDefault(defaultLocale);
			LogFactory.getLog(getClass()).info("Default System Locale set to " + defaultLocale.toString());
		} catch (SecurityException e) {
			throw new SystemException(e);
		}
	}
}
