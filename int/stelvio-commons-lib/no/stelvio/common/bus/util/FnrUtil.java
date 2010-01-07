package no.stelvio.common.bus.util;

import java.util.Calendar;
import java.util.Date;

import no.stelvio.domain.person.Pid;


/**
 * 
 * The six first digits of fødselsnummer (personal identification number) 
 * normally represents the birth date: day (dd), month
 * (mm) and year (yy). However, the two digit representation of the year has
 * caused many problem (read the Y2k-problem) and isn't sufficent to decide
 * the century. Therefore, the three next digits that are the individsifre,
 * are categorized to represent time periods. Fødselsnummer: ddmmyyiiicc (d
 * day, m month, y year, i individsiffer, c checksum)
 * 
 * (http://no.wikipedia.org/wiki/F%C3%B8dselsnummer)
 * (http://no.wikipedia.org/wiki/Personnummer)
 *
 * Class is a utility-class to check if fnr is valid
 * <p/>
 * 
 * @author person983601e0e117 (Accenture)
 * @author person15754a4522e7
 * @author Fredrik Dahl-Jørgensen (Accenture)
 * @author Jonny Mauland (Accenture)
 * @author Ismar Slomic (Accenture)
 * @see Embeddable
 */

public final class FnrUtil{
	/**
	 * Determines whether the specified string is a valid personal identification number. A valid PID can be: FNR, DNR, HNR or BostNr. 
	 * This method does not check for special circumstances i.e. where the personnummer has a specific value like 00000 or 000001.
	 * 
	 * @param fnr personal identification id to validate
	 * @return <code>true</code> if the specified string is valid, otherwise <code>false</code>
	 */
	public static boolean isValidFnr(String fnr) {
		return isValidFnr(fnr, false);
	}

	/**
	 * Determines whether the specified string is a valid personal identification number. A valid PID can be: FNR, DNR or BostNr
	 * 
	 * @param pid personal identification id to validate
	 * @param acceptSpecialCircumstances flag indicating if the method should accept special circumstances. Special 
	 * circumstances are where the personnummer does not follow the normal rules, but has a special value like 00000 or 00001  
	 * @return <code>true</code> if the specified string is valid, otherwise <code>false</code>
	 */
	public static boolean isValidFnr(String pid, boolean acceptSpecialCircumstances) {
		return Pid.isValidPid(pid, acceptSpecialCircumstances);
	}
	
	/**
	 * Determines if a Norwegian social security number belongs to a male or a female 
	 * 
	 * @param pid Pid to check if belongs to male or female 
	 * @return <code>true</code> if social security no belongs to male, <code>false</code> if it
	 * belongs to female and null if input is empty
	 */
	public static Boolean pidBelongsToMale(String pid) {
		Boolean response = null;
		if(pid!=null && !"".equals(pid)){
			int ninthDigit = Integer.parseInt(pid.substring(8,9));
			return !(ninthDigit % 2 == 0);
		}
		return response;
		
	}
	
	/**
	 * Validates that the white space usage in the pid is valid. Valid use of white space is ONE white space between index 5 and
	 * 6 (making the pid.length() == 12). No spaces is also a valid "use" of white spaces.
	 * 
	 * @param pid Pid to check for whitespace compliance.
	 * @return <code>true</code> if usage is valid, otherwise <code>false</code>
	 */
	public static boolean isWhitespaceCompliant(String pid) {
		return Pid.isWhitespaceCompliant(pid);
	}

	/**
	 * Adjusts DNR, HNR and BostNr so that the first 6 numbers represents a valid date. In the case where DNR or BostNr is the input,
	 * the return value will fail a modulus 11 check.
	 * 
	 * @param value a personal identification number
	 * @return the inparam if it wasn't a DNR or BostNr, otherwise the BostNr/DNR/HNR where the 6 first digits can be converted to a
	 * valid date
	 */
	protected static String makeDnrOrBostnrAdjustments(String value) {
		if (StringUtils.isBlank(value)) {
			return value;
		}

		// fnr format will be <DDMMAAXXXYY>
		int day = Integer.parseInt(value.substring(0, 2));
		int month = Integer.parseInt(value.substring(2, 4));

		// DNR adjustment
		if (day > 40 && day <= 71) {
			day -= 40;
			StringBuffer fnr = new StringBuffer(value);

			if (day < 10) {
				fnr.replace(0, 2, "0" + Integer.toString(day));
			} else {
				fnr.replace(0, 2, Integer.toString(day));
			}

			return fnr.toString();
		} else if (month > 20 && month <= 32) {
			// BOST adjustments
			month -= 20;
			StringBuffer fnr = new StringBuffer(value);

			if (month < 10) {
				fnr.replace(2, 4, "0" + Integer.toString(month));
			} else {
				fnr.replace(2, 4, Integer.toString(month));
			}

			return fnr.toString();
		} else if(month > 40 && month <=52 ) {
			// H-Nummer adjustment
			month -= 40;
			StringBuffer fnr = new StringBuffer(value);

			if (month < 10) {
				fnr.replace(2, 4, "0" + Integer.toString(month));
			} else {
				fnr.replace(2, 4, Integer.toString(month));
			}

			return fnr.toString();
		}
		

		// value was neither bostnr nor dnr nor hnr
		return value;
	}

	/**
	 * Returns a 4-digit birth date
	 * 
	 * @param dnrOrBnrAdjustedFnr a fnr, adjusted if it's a bnr or dnr
	 * @return 4 digit birth date, -1 if invalid
	 */
	protected static int get4DigitYearOfBirth(String dnrOrBnrAdjustedFnr) {
		int year = Integer.parseInt(dnrOrBnrAdjustedFnr.substring(4, 6));
		int individnr = Integer.parseInt(dnrOrBnrAdjustedFnr.substring(6, 9));
		// stilborn baby (dødfødt barn)
		if (Integer.parseInt(dnrOrBnrAdjustedFnr.substring(6)) < 10) {
			year += 2000;
		} else {
			// recalculating year
			if (individnr < 500) {
				year += 1900;
			} else if ((individnr < 750) && (54 < year)) {
				year += 1800;
			} else if ((individnr < 1000) && (year < 40)) {
				year += 2000;
			} else if ((900 <= individnr) && (individnr < 1000) && (39 < year)) {
				year += 1900;
			} else {
				// invalid fnr
				return -1;
			}
		}
		return year;
	}
	
	/**
	 * Returns day of birth
	 * 
	 * @param dnrOrBnrAdjustedFnr a fnr, adjusted if it's a bnr, dnr or hnr
	 * @return 2 digit day of date
	 */
	protected static int getDayOfBirth(String dnrOrBnrAdjustedFnr)
	{
		// dnrOrBnrAdjustedFnr format will be ddmmyyiiicc
		int day = Integer.parseInt(dnrOrBnrAdjustedFnr.substring(0, 2));
		return day;
	}
	
	/**
	 * Returns month of birth
	 * 
	 * @param dnrOrBnrAdjustedFnr a fnr, adjusted if it's a bnr, dnr or hnr
	 * @return 2 digit month of date
	 */
	protected static int getMonthOfBirth(String dnrOrBnrAdjustedFnr)
	{
		// dnrOrBnrAdjustedFnr format will be ddmmyyiiicc
		int month = Integer.parseInt(dnrOrBnrAdjustedFnr.substring(2, 4));
		return month;
	}
	
	/**
	 * Returns age of the person at certain date
	 * 
	 * @param fnr personal identification of the person
	 * @param atDate at which date to calculate the person's age
	 * @return age of person at certain date, -1 if fnr is invalid (not fnr,dnr,bostnr,hnr)
	 */
	public static int getAgeAtDateForFnr(String fnr, Calendar atDate)
	{
		// needs to validate first because get4DigitYearOfBirth returns year of birth even if the pid is not valid
		boolean valid = isValidFnr(fnr);
		int age = -1;
		if( valid )
		{
			String adjustedFnr = makeDnrOrBostnrAdjustments(fnr);
			
			int yearOfBirth = get4DigitYearOfBirth(adjustedFnr);
			int monthOfBirth = getMonthOfBirth(adjustedFnr);
			int dayOfBirth = getDayOfBirth(adjustedFnr);

			int atYear = atDate.get(Calendar.YEAR);
			int atMonth = atDate.get(Calendar.MONTH);
			int atDay = atDate.get(Calendar.DAY_OF_MONTH);
			
			age = atYear - yearOfBirth;
			
			/* Has the persons month and day of birth passed. */
			if ((monthOfBirth > atMonth) || (monthOfBirth == atMonth && dayOfBirth > atDay))
				age--; // day of birth not yet passed
			
			/* Age can not be less than 0 years. */
			if (age < 0) 
				age = -1;
		}
		
		return age;
	}
	
	/**
	 * Returns the birthdate for a given fnr. Null if input is null or too short
	 * 
	 * @param fnr personal identification of the person
	 * @return birthdate for given fnr 
	 */
	public static Date getBirthdateForFnr(String fnr) {
		if(fnr == null || fnr.length()<6) return null;
		
		return DateUtil.parseInputString(makeDnrOrBostnrAdjustments(fnr).substring(0,6), false);
	}
}
