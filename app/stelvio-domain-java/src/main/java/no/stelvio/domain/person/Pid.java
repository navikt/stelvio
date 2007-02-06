package no.stelvio.domain.person;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

/**
 * Class represents a personal identification number, that can be persistet into a table. Instances of this object can
 * not exist on it's own, they must exist inside an <code>@Entity</code>-object in order to be persisted.
 * <p/>
 * There shouldn't exist an instance of this class where <code>getPid</code> doesn't return a valid fnr. Class is final to avoid
 * public implementations of the no-arg constructor
 * <p/>
 * NB! Pid is and should always be immutable
 *
 * @author person983601e0e117 (Accenture)
 * @author person15754a4522e7
 * @see Embeddable
 */

@Embeddable
public final class Pid implements Serializable {
	/** The id used to check version of object when serializing. */
	private static final long serialVersionUID = 8098800200089499716L;

	@Column(name = "fnr_fk")
	private String pid;

	/** Protected constructor, should only be used by persistence provider. Never to be used by a client. */
	protected Pid() {
	}

	/**
	 * Creates a new Pid using the a pid String
	 *
	 * @param pid a valid fnr
	 * @throws PidValidationException if pid isn't a valid Personal Identification Number
	 */
	public Pid(String pid) throws PidValidationException {
		this.pid = StringUtils.deleteWhitespace(pid);
		validate();
	}

	/**
	 * Gets the personal identification number, this number should always be a valid pid..
	 *
	 * @return pid representing a fnr, dnr or bostnr
	 */
	public String getPid() {
		return pid;
	}

	/**
	 * Method that calculates and returns the birth date for <code>this</code> pid
	 *
	 * @return java.util.Date representing the birth date of person with this Pid
	 */
	public Date getFodselsdato() {
		SimpleDateFormat formatter = new SimpleDateFormat("MMDDyyyy");

		//Adjust bnr or dnr (for fnr return value will be equal to pid)
		String adjustedFnr = makeDnrOrBostnrAdjustments(pid);
		//Construct a date string with MMDDyyyy format
		String dateString = adjustedFnr.substring(0, 4) + get4DigitYearOfBirth(adjustedFnr);

		try {
			return formatter.parse(dateString);
		} catch (ParseException e) {
			// This should never occur, as "pid" has been validated by constructor
			throw new PidValidationException(e, pid);
		}
	}

	/**
	 * Determines whether the specified string is a valid personal identification number. A valid PID can be: FNR, DNR or
	 * BostNr
	 *
	 * @param pid personal identification id to validate
	 * @return <code>true</code> if the specified string is valid, otherwise <code>false</code>
	 */
	public static boolean isValidPid(String pid) {
		if (pid != null) {
			String value = StringUtils.deleteWhitespace(pid);
			if (isValidCharacters(value) && isValidFnrLength(value)) {
				if (isMod11Compliant(value)) {
					String fnr = makeDnrOrBostnrAdjustments(value);
					if (isFnrDateValid(fnr)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Validates that the white space usage in the pid is valid. Valid use of white space is ONE white space between index
	 * 5 and 6 (making the pid.length() == 12). No spaces is also a valid "use" of white spaces.
	 *
	 * @param pid to validate against white space rule
	 * @return <code>true</code> if usage is valid, otherwise <code>false</code>
	 */
	public static boolean isWhitespaceCompliant(String pid) {
		String pidWithWhitespace = "\\d\\d\\d\\d\\d\\d\\s\\d\\d\\d\\d\\d"; //DDMMYY XXXZZ
		String pidWithoutWhitespace = "\\d\\d\\d\\d\\d\\d\\d\\d\\d\\d\\d"; //DDMMYYXXXZZ

		return pid.matches(pidWithWhitespace) || pid.matches(pidWithoutWhitespace);
	}

	/**
	 * Method that calculates whether the this Pid is representing a Bostnummer
	 *
	 * @return <code>true</code> if <code>this</code> is representing a bostnummer, otherwise <code>false</code>
	 */
	public boolean isBostnummer() {
		// fnr format will be <DDMMAAXXXYY>
		int month = Integer.parseInt(pid.substring(2, 4));

		return month > 20 && month <= 32;
	}

	/**
	 * Idicates whether some object is equals to this object. Two Pid objects are equal if their {@link #getPid()} value
	 * are equal
	 *
	 * @param obj the reference object with which to compare
	 * @return <coode>true</code> it objects are equal, otherwise <code>false</code>
	 * @see #equals(Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Pid) {
			Pid pidObj = (Pid) obj;
			return pid.equals(pidObj.pid);
		}

		return false;
	}

	/**
	 * Returns a hash code value for the object
	 *
	 * @return a hash code value for this object
	 * @see #equals(Object)
	 */
	public int hashCode() {
		return pid.hashCode();
	}

	/**
	 * Validates that <code>this</code> is a valid Pid
	 *
	 * @throws PidValidationException if validation of <code>this</code> fails
	 */
	private void validate() throws PidValidationException {
		if (!isValidPid(pid)) {
			throw new PidValidationException(pid);
		}
	}

	/**
	 * Validates that the length of the fnr is valid. To be valid the length must be 11.
	 *
	 * @param fnr personal identification number
	 * @return <code>true</code> if valid, otherwise <code>false</code>
	 */
	private static boolean isValidFnrLength(String fnr) {
		return fnr != null && (fnr.length() == 11);
	}

	/**
	 * Validates that the characters that make up the fnr are valid. To be valid, all characters must be numeric.
	 *
	 * @param fnr personal identification number
	 * @return <code>true</code> if valid, otherwise <code>false</code>
	 */
	private static boolean isValidCharacters(String fnr) {
		return StringUtils.isNumeric(fnr);
	}

	/**
	 * Validates that the first six digits of a fnr represents a valid birth date
	 *
	 * @param dnrOrBnrAdjustedFnr - 11 digit fødselsnummer, ajdusted if bnr or fnr
	 * @return <code>true</code> if fnr can be converted to a valid date, otherwise <code>false</code>
	 */
	private static boolean isFnrDateValid(String dnrOrBnrAdjustedFnr) {
		boolean validDate = true;

		// fnr format is <DDMMAAXXXYY>
		int day = Integer.parseInt(dnrOrBnrAdjustedFnr.substring(0, 2));
		int month = Integer.parseInt(dnrOrBnrAdjustedFnr.substring(2, 4));
		int year = get4DigitYearOfBirth(dnrOrBnrAdjustedFnr);

		// invalid birth year
		if (year == -1) {
			return false;
		}

		if (day < 1) {
			validDate = false;
		}

		switch (month) {
			case 1: // january
			case 3: // march
			case 5: // may
			case 7: // july
			case 8: // august
			case 10: // october
			case 12: // december
				validDate &= (day <= 31);
				break;

			case 4: // april
			case 6: // june
			case 9: // september
			case 11: // november
				validDate &= (day <= 30);
				break;

			case 2: // february
				/* Leap year calculation:
				 * 
				 * Rule: If year can be devided by 4, it's a leap year
				 * 
				 * Exeception 1: If also can be devided by 100, it's NOT a leap year
				 * Exception 2: If year can be devided by 100 AND 400, it IS a leap year 
				 */
				if (year % 100 == 0 && year % 400 == 0) {
					// leap year
					validDate &= (day <= 29);
				} else if (year % 100 != 0 && year % 4 == 0) {
					// det er skuddår
					validDate &= (day <= 29);
				} else {
					// det er IKKE skuddår
					validDate &= (day <= 28);
				}
				break;

			default:
				validDate = false;
		}

		return validDate;
	}


	/**
	 * Checks that a fnr is valid according to the modulus 11 control.
	 *
	 * @param fnr fodselsnummer
	 * @return true if fnr is valid, otherwise false
	 */
	private static boolean isMod11Compliant(String fnr) {
		// FORMAT: DDMMYYXXXYY
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

		// control 1
		int v1 = (3 * d1) + (7 * d2) + (6 * m1) + (1 * m2) + (8 * a1) + (9 * a2) + (4 * i1) + (5 * i2) + (2 * i3);

		int tmp = v1 / 11;
		int rest1 = v1 - (tmp * 11);
		int kontK1 = (rest1 == 0) ? 0 : (11 - rest1);

		// control 2
		int v2 = (5 * d1) + (4 * d2) + (3 * m1) + (2 * m2) + (7 * a1) + (6 * a2) + (5 * i1) + (4 * i2) + (3 * i3) + (2 * k1);
		tmp = v2 / 11;
		int rest2 = v2 - (tmp * 11);
		int kontK2 = (rest2 == 0) ? 0 : (11 - rest2);

		// checks that control number is correct
		return kontK1 == k1 && kontK2 == k2;

	}


	/**
	 * Adjusts DNR and BostNr so that the first 6 numbers represents a valid date In the case wher DNR or BostNr is the
	 * input, the return value will fail a modulus 11 check.
	 *
	 * @param value a personal identification number
	 * @return the inparam if it wasn't a DNR or BostNr, otherwise the BostNr/DNR where the 6 first digits can be converted
	 *         to a valid date
	 */
	private static String makeDnrOrBostnrAdjustments(String value) {
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
				fnr.replace(0, 1, "0" + Integer.toString(day));
			} else {
				fnr.replace(0, 2, Integer.toString(day));
			}
			return fnr.toString();

		} else if (month > 20 && month <= 32) {
			// BOST adjustments
			month -= 20;
			StringBuffer fnr = new StringBuffer(value);

			if (month < 10) {
				fnr.replace(2, 3, "0" + Integer.toString(month));
			} else {
				fnr.replace(2, 4, Integer.toString(month));
			}
			return fnr.toString();
		}

		//value was neither bostnr nor dnr
		return value;
	}

	/**
	 * Returns a 4-digit birth date
	 *
	 * @param dnrOrBnrAdjustedFnr a fnr, adjusted if it's a bnr or dnr
	 * @return 4 digit birth date, -1 if invalid
	 */
	private static int get4DigitYearOfBirth(String dnrOrBnrAdjustedFnr) {
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
}
