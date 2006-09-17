package no.stelvio.common.framework.util;

import org.apache.commons.lang.StringUtils;

import no.stelvio.common.framework.error.ApplicationException;

/**
 * Util klasse for diverse operasjoner p� FNR/DNR.
 *
 * @author person356941106810, Accenture
 * @author persone5d69f3729a8, Accenture
 * @author person7553f5959484, Accenture
 * @author Espen N�ss, Accenture
 * 
 * @version $Id: FNRUtil.java 2841 2006-04-25 10:37:39Z psa2920 $
 */
public final class FNRUtil {

	private static final int FNR_LENGTH = 11;
	private static final int DAYS_OF_MONTH_31 = 31;
	private static final int DAYS_OF_MONTH_30 = 30;
	private static final int DAYS_OF_MONTH_29 = 29;
	private static final int DAYS_OF_MONTH_28 = 28;

	/** Privat konstrukt�r for � forhindre instansiering */
	private FNRUtil() {
	}


	/**
	 * Sjekker om en fnr er et dnr. Derson fnr er null returneres false
	 *
	 * @param fnr f�dselsnummeret til personen
	 * @return om et fnr er et dnr eller ikke
	 */
	public static boolean isDnr(String fnr) {

		if (!isValidFnr(fnr)) {
			return false;
		}

		try {
			int day = Integer.parseInt(fnr.substring(0, 2));
			if (day > 40 && day <= 71) {
				return true;	
			}
			return false;
		} catch (NumberFormatException nfe) {
			return false;
		}
	}

	/**
	 * Sjekker om angitt fodselsnummer er gyldig f�dselsnummer.
	 *
	 * @param fnr fodselsnummeret
	 * @return true hvis f�dselsnummeret er gyldig, false hvis ikke.
	 */
	public static boolean isFnr(String fnr) {
		
		if (!isValidFnr(fnr)) {
			return false;
		}

		try {
			int day = Integer.parseInt(fnr.substring(0, 2));
			int month = Integer.parseInt(fnr.substring(2, 4));
			if (day >= 1 && day <= 31 && month >= 1 && month <= 12) {
				return true;
			}
			return false;
		} catch (NumberFormatException nfe) {
			return false;
		}
	}

	/**
	 * Sjekker om fnr faktisk er en av de mulige variantene FNR/DNR
	 * @param fnr FNR
	 * @return true/false
	 */
	public static boolean isPerson (String fnr) {
		
		if (isDnr(fnr) || isFnr(fnr)) {
			return true;
		}

		return false;
	}

	/**
	 * Sjekker om et barn er d�df�dt. Dersom fnr er null returneres false.
	 *
	 * @param fnr f�dselsnummeret til personen.
	 * @return om barnet er d�f�dt eller ikke
	 * @throws ApplicationException hvis en feil oppst�r
	 */
	public static boolean isDoFodt(String fnr) throws ApplicationException {

		if(!(null != fnr && fnr.length() == FNR_LENGTH && StringUtils.isNumeric(fnr))){			
			throw new IllegalArgumentException("F�dselsnummer " + fnr + " er ikke gyldig");
		}

		// sjekk om dodfodt barn (ddMMyyXXXYY)
		try {

			if (Integer.parseInt(fnr.substring(6)) < 10) {
				return true;
			}

			return false;
		} catch (NumberFormatException nfe) {
			return false;
		}
	}

	/**
	 * Sjekker om angitt fodselsnummer er gyldig ihht modulus 11 kontroll.
	 *
	 * @param fnr fodselsnummeret
	 * @return true hvis f�dselsnummeret er gyldig, false hvis ikke.
	 */
	public static boolean isMod11Compliant(String fnr) {
		
		// FORMAT: ddmmaaiiikk
		int d1 = Integer.parseInt(fnr.substring(0, 1));
		int d2 = Integer.parseInt(fnr.substring(1, 2));
		int m1 = Integer.parseInt(fnr.substring(2, 3));
		int m2 = Integer.parseInt(fnr.substring(3, 4));
		int a1 = Integer.parseInt(fnr.substring(4, 5));
		int a2 = Integer.parseInt(fnr.substring(5, 6));
		int i1 = Integer.parseInt(fnr.substring(6, 7));
		int i2 = Integer.parseInt(fnr.substring(7, 8));
		int i3 = Integer.parseInt(fnr.substring(8, 9));
		int k1 = Integer.parseInt(fnr.substring(9, 10));
		int k2 = Integer.parseInt(fnr.substring(10));

		int kontK1 = 0;
		int kontK2 = 0;

		// kontroll 1
		int v1 = (3 * d1) + (7 * d2) + (6 * m1) + (1 * m2) + (8 * a1) + (9 * a2) + (4 * i1) + (5 * i2) + (2 * i3);

		int tmp = v1 / 11;
		int rest1 = v1 - (tmp * 11);
		kontK1 = (rest1 == 0) ? 0 : (11 - rest1);

		// kontroll 2
		int v2 = (5 * d1) + (4 * d2) + (3 * m1) + (2 * m2) + (7 * a1) + (6 * a2) + (5 * i1) + (4 * i2) + (3 * i3) + (2 * k1);
		tmp = v2 / 11;
		int rest2 = v2 - (tmp * 11);
		kontK2 = (rest2 == 0) ? 0 : (11 - rest2);

		// sjekker om kontrollsiffer er riktig
		if ((kontK1 != k1) || (kontK2 != k2)) {
			return false;
		}

		return true;
	}

	/**
	 * Returnerer siste del av personnummeret.
	 *
	 * @param fnr f�dselsnummer
	 * @return string med personummerdelen av et f�dselsnummer
	 */
	public static String getPersonnummerDel(String fnr) {
		if (!isValidFnr(fnr)) {
			return null;
		}

		return fnr.substring(6);
	}

	/**
	 * Sjekker om f�rste delen av er f�dselsnummer er en gyldig dato.
	 *
	 * @param fnr - 11 siffret f�dselsnummer
	 * @return validDate true if valid, false if not valid
	 */
	public static boolean isFnrDateValid(String fnr) {

		boolean validDate = true;

		// fnr vil vare pa formatet <DDMMAAXXXYY>
		int day = Integer.parseInt(fnr.substring(0, 2));
		int month = Integer.parseInt(fnr.substring(2, 4));
		int year = Integer.parseInt(fnr.substring(4, 6));
		int individnr = Integer.parseInt(fnr.substring(6, 9));

		// sjekk om dodfodt barn
		if (Integer.parseInt(fnr.substring(6)) < 10) {
			year += 2000;
		} else {
			// omregning ar		
			if (individnr < 500) {
				year += 1900;
			} else if ((individnr < 750) && (54 < year)) {
				year += 1800;
			} else if ((individnr < 1000) && (year < 40)) {
				year += 2000;
			} else if ((900 <= individnr) && (individnr < 1000) && (39 < year)) {
				year += 1900;
			} else {
				// ugyldig fnr
				return false;
			}
		}

		if (day < 1) {
			validDate = false;
		}

		switch (month) {
			case 1 : // januar
			case 3 : // mars
			case 5 : // mai
			case 7 : // juli
			case 8 : // august
			case 10 : // oktober
			case 12 : // desember
				validDate &= (day <= DAYS_OF_MONTH_31);
				break;

			case 4 : // april
			case 6 : // juni
			case 9 : // september
			case 11 : // november
				validDate &= (day <= DAYS_OF_MONTH_30);
				break;

			case 2 : // februar
				/* Skudd�rsberegning:
				 * 
				 * Hovedregel: Hvis �rstallet er delelig med 4 er det skudd�r
				 * 
				 * Unntak 1: Hvis �rstallet ogs� er delelig med 100, er det IKKE skudd�r
				 * Unntak 2: Hvis �rstallet er delelig med 100 OG delelig med 400, er det likevel skudd�r 
				 */			
				if(year % 100 == 0 && year % 400 == 0){
					// det er skudd�r
					validDate &= (day <= DAYS_OF_MONTH_29);
				} else if (year % 100 != 0 && year % 4 == 0){
					// det er skudd�r
					validDate &= (day <= DAYS_OF_MONTH_29);
				} else {
					// det er IKKE skudd�r
					validDate &= (day <= DAYS_OF_MONTH_28);
				}
				break;

			default :
				validDate = false;
		}

		return validDate;
	}

	/**
	 * Sjekker om f�dselsnummeret er gyldig, dvs:
	 * - Skal v�re 11 numeriske tegn.
	 * - Kan v�re et DNR (dag mellom 41 og 71)
	 * - Skal v�re Modulus 11 kompatibelt
	 *
	 * @param fnr F�dselsnummer
	 * @return true hvis f�dselsnummeret er gyldig, false hvis ikke.
	 */
	public static boolean isValidFnr(String fnr) {
		return null != fnr 
			&& fnr.length() == FNR_LENGTH  
			&& StringUtils.isNumeric(fnr) 
			&& (Integer.valueOf(fnr.substring(0, 2)).intValue() <= 71)
			&& isMod11Compliant(fnr);
	}

}
