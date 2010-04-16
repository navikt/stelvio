package no.stelvio.common.bus.util;

import java.util.Calendar;
import java.util.Date;

import no.stelvio.domain.person.Pid;
import no.stelvio.domain.person.PidValidationException;

/**
 * 
 * The six first digits of fødselsnummer (personal identification number) normally represents the birth date: day (dd), month
 * (mm) and year (yy). However, the two digit representation of the year has caused many problem (read the Y2k-problem) and
 * isn't sufficent to decide the century. Therefore, the three next digits that are the individsifre, are categorized to
 * represent time periods. Fødselsnummer: ddmmyyiiicc (d day, m month, y year, i individsiffer, c checksum)
 * 
 * (http://no.wikipedia.org/wiki/F%C3%B8dselsnummer) (http://no.wikipedia.org/wiki/Personnummer)
 * 
 * Class is a utility-class to check if fnr is valid <p/>
 * 
 * @author person983601e0e117 (Accenture)
 * @author person15754a4522e7
 * @author Fredrik Dahl-Jørgensen (Accenture)
 * @author Jonny Mauland (Accenture)
 * @author Ismar Slomic (Accenture)
 * @see Embeddable
 */

public final class FnrUtil {
	/**
	 * Determines whether the specified string is a valid personal identification number. A valid PID can be: FNR, DNR, HNR or
	 * BostNr. This method does not check for special circumstances i.e. where the personnummer has a specific value like 00000
	 * or 000001.
	 * 
	 * @param fnr
	 *            personal identification id to validate
	 * @return <code>true</code> if the specified string is valid, otherwise <code>false</code>
	 */
	public static boolean isValidFnr(String fnr) {
		return isValidFnr(fnr, false);
	}

	/**
	 * Determines whether the specified string is a valid personal identification number. A valid PID can be: FNR, DNR or BostNr
	 * 
	 * @param pid
	 *            personal identification id to validate
	 * @param acceptSpecialCircumstances
	 *            flag indicating if the method should accept special circumstances. Special circumstances are where the
	 *            personnummer does not follow the normal rules, but has a special value like 00000 or 00001
	 * @return <code>true</code> if the specified string is valid, otherwise <code>false</code>
	 */
	public static boolean isValidFnr(String pid, boolean acceptSpecialCircumstances) {
		return Pid.isValidPid(pid, acceptSpecialCircumstances);
	}

	/**
	 * Determines if a Norwegian social security number belongs to a male or a female
	 * 
	 * @param pid
	 *            Pid to check if belongs to male or female
	 * @return <code>true</code> if social security no belongs to male, <code>false</code> if it belongs to female and null
	 *         if input is empty
	 */
	public static Boolean pidBelongsToMale(String pid) {
		Boolean response = null;
		if (pid != null && !"".equals(pid)) {
			int ninthDigit = Integer.parseInt(pid.substring(8, 9));
			return !(ninthDigit % 2 == 0);
		}
		return response;

	}

	/**
	 * Validates that the white space usage in the pid is valid. Valid use of white space is ONE white space between index 5 and
	 * 6 (making the pid.length() == 12). No spaces is also a valid "use" of white spaces.
	 * 
	 * @param pid
	 *            Pid to check for whitespace compliance.
	 * @return <code>true</code> if usage is valid, otherwise <code>false</code>
	 */
	public static boolean isWhitespaceCompliant(String pid) {
		return Pid.isWhitespaceCompliant(pid);
	}

	/**
	 * Returns age of the person at certain date
	 * 
	 * @param fnr
	 *            personal identification of the person
	 * @param atDate
	 *            at which date to calculate the person's age
	 * @return age of person at certain date, -1 if fnr is invalid (not fnr,dnr,bostnr,hnr)
	 */
	public static int getAgeAtDateForFnr(String fnr, Calendar atDate) {
		try {
			return Pid.calculateAge(new Pid(fnr, true), atDate.getTime());
		} catch (PidValidationException e) {
			return -1;
		}
	}

	/**
	 * Returns the birthdate for a given fnr. Null if input is null or too short
	 * 
	 * @param fnr
	 *            personal identification of the person
	 * @return birthdate for given fnr
	 */
	public static Date getBirthdateForFnr(String fnr) {
		try {
			return new Pid(fnr, true).getFodselsdato();
		} catch (PidValidationException e) {
			return null;
		}
	}
}
