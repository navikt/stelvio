/**
 * 
 */
package no.nav.presentation.pensjon.saksbehandling.stelvio.validate;

import org.apache.commons.lang.StringUtils;

/**
 * @author person4f9bc5bd17cc
 *
 */
public class NorskKontonummerValidator {
	public static synchronized boolean isValidNorskKontonummer(String norskKontonummer) {
		if (StringUtils.isEmpty(norskKontonummer)) {
			return false;
		}
		if (norskKontonummer.length() != 11) {
			return false;
		}
		if (!StringUtils.isNumeric(norskKontonummer)) {
			return false;
		}
		return true;
	}
}