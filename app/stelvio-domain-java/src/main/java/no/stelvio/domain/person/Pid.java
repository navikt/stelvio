package no.stelvio.domain.person;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

/**
 * Class represents a personal identification number, that can be persistet into a table. 
 * Instances of this object can not exist on it's own, they must exist inside an <code>@Entity</code>-object in order to be persisted.
 * @see Embeddable
 * 
 * There shouldn't exist an instance of this class where getNummer doesn't return a valid fnr.
 * Class is final to avoid public implementations of the no-arg constructor
 * 
 * NB! Pid is and should always be immutable
 * 
 * @author person983601e0e117, Accenture
 *
 */

@Embeddable
public final class Pid {

	@Column(name="fnr")
	private String nummer;
	
	/**
	 * Protected constructor, should only be used by persistence provider.
	 * Never to be used by a client.
	 *
	 */
	protected Pid(){
		
	}
	
	/**
	 * Creates a new Pid using the nummer
	 * @param nummer a valid fnr
	 * @throws PidValidationException if nummer isn't a valid Personal Identification Number
	 */
	public Pid(String nummer) throws PidValidationException{
		this.nummer = nummer;
		validate();
	}
	
	/**
	 * Gets the personal identification number, this number should always be a valid pid..
	 * @return nummer representing a fnr, dnr or bostnr
	 */
	public String getNummer() {
		return nummer;
	}	

	/**
	 * Method that uses the number to calculate and return the birth date for this pid
	 * @return java.util.Date representing the birth date of person with this Pid
	 */
	public Date getDate(){
		SimpleDateFormat formatter = new SimpleDateFormat("MMDDyyyy");

		//Adjust bnr or dnr (for fnr return value will be equal to nummer)
		String adjustedFnr = makeDnrOrBostnrAdjustments(nummer);
		//Construct a date string with MMDDyyyy format
		String dateString = adjustedFnr.substring(0,4) + get4DigitYearOfBirth(adjustedFnr);
		
		Date date = null;
		try {
			date = formatter.parse(dateString);
		} catch (ParseException e) {
			//This should never occur, as "nummer" has been validated by constructor
			throw new PidValidationException(e,nummer);
		}
		return date;
	}
	
	/**
	 * Determines whether the specified string is a valid personal identification number.
	 * A valid PID can be: FNR, DNR or BostNr 
	 * @param pidNum
	 * @return <code>true</code> if the specified string is valid, otherwise <code>false</code>
	 * @throws PidValidationException if nummer isn't a valid Personal Identification Number
	 */
	public static boolean isValidPidNum(String pidNum){
		String value = StringUtils.deleteWhitespace(pidNum);
		if(isValidCharacters(value) && isValidFnrLength(value)){			
			if(isMod11Compliant(value)){
				String fnr = makeDnrOrBostnrAdjustments(value);				
				if(isFnrDateValid(fnr)){
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Validates that <code>this</code> is a valid Pid
	 * @return true if object is valid, otherwise false
	 */
	private void validate() throws PidValidationException{
		boolean valid = isValidPidNum(nummer);
		if(!valid){
			throw new PidValidationException(nummer);
		}
	}	
	
	/**
	 * Validates that the length of the fnr is valid.
	 * To be valid the length must be 11.
	 * @param fnr
	 * @return <code>true</code> if valid, otherwise <code>false</code>
	 */
	private static boolean isValidFnrLength(String fnr){
		if(fnr != null){
			return (fnr.length() == 11);
		}else{
			return false;
		}
	}
	
	/**
	 * Validates that the characters that make up the fnr are valid.
	 * To be valid, all characters must be numeric.
	 * @param fnr
	 * @return <code>true</code> if valid, otherwise <code>false</code>
	 */
	private static boolean isValidCharacters(String fnr){
		return StringUtils.isNumeric(fnr);
	}	
	

	
	/**
	 * Validates that the first six digits of a fnr represents a valid birth date
	 * @param dnrOrBnrAdjustedFnr - 11 digit fødselsnummer, ajdusted if bnr or fnr
	 * @return <code>true</code> if fnr can be converted to a valid date, otherwise <code>false</code>
	 */
	private static boolean isFnrDateValid(String dnrOrBnrAdjustedFnr) {

		boolean validDate = true;

		// fnr format is <DDMMAAXXXYY>
		int day = Integer.parseInt(dnrOrBnrAdjustedFnr.substring(0, 2));
		int month = Integer.parseInt(dnrOrBnrAdjustedFnr.substring(2, 4));
		int year = Integer.parseInt(dnrOrBnrAdjustedFnr.substring(4, 6));
		int individnr = Integer.parseInt(dnrOrBnrAdjustedFnr.substring(6, 9));

		year = get4DigitYearOfBirth(dnrOrBnrAdjustedFnr);

		//invalid birth year
		if(year == -1){
			return false;
		}
		
		if (day < 1) {
			validDate = false;
		}

		switch (month) {
			case 1 : // january
			case 3 : // march
			case 5 : // may
			case 7 : // july
			case 8 : // august
			case 10 : // october
			case 12 : // december
				validDate &= (day <= 31);
				break;

			case 4 : // april
			case 6 : // june
			case 9 : // september
			case 11 : // november
				validDate &= (day <= 30);
				break;

			case 2 : // february
				/* Leap year calculation:
				 * 
				 * Rule: If year can be devided by 4, it's a leap year
				 * 
				 * Exeception 1: If also can be devided by 100, it's NOT a leap year
				 * Exception 2: If year can be devided by 100 AND 400, it IS a leap year 
				 */			
				if(year % 100 == 0 && year % 400 == 0){
					// leap year
					validDate &= (day <= 29);
				} else if (year % 100 != 0 && year % 4 == 0){
					// det er skuddår
					validDate &= (day <= 29);
				} else {
					// det er IKKE skuddår
					validDate &= (day <= 28);
				}
				break;

			default :
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

		int kontK1 = 0;
		int kontK2 = 0;

		// control 1
		int v1 = (3 * d1) + (7 * d2) + (6 * m1) + (1 * m2) + (8 * a1) + (9 * a2) + (4 * i1) + (5 * i2) + (2 * i3);

		int tmp = v1 / 11;
		int rest1 = v1 - (tmp * 11);
		kontK1 = (rest1 == 0) ? 0 : (11 - rest1);

		// control 2
		int v2 = (5 * d1) + (4 * d2) + (3 * m1) + (2 * m2) + (7 * a1) + (6 * a2) + (5 * i1) + (4 * i2) + (3 * i3) + (2 * k1);
		tmp = v2 / 11;
		int rest2 = v2 - (tmp * 11);
		kontK2 = (rest2 == 0) ? 0 : (11 - rest2);

		// checks that control number is correct
		if ((kontK1 != k1) || (kontK2 != k2)) {
			return false;
		}

		return true;
	}	
	
	/**
	 * Adjusts DNR and BostNr so that the first 6 numbers represents a valid date
	 * In the case wher DNR or BostNr is the input, the return value will fail a modulus 11 check.
	 * @param value
	 * @return the inparam if it wasn't a DNR or BostNr, otherwise the BostNr/DNR where the 6 first digits can be converted to a valid date
	 */
	private static String makeDnrOrBostnrAdjustments(String value){
		
		if(StringUtils.isBlank(value)){
			return value;
		}
		
		// fnr format will be <DDMMAAXXXYY>
		int day = Integer.parseInt(value.substring(0, 2));
		int month = Integer.parseInt(value.substring(2, 4));

		// DNR adjustment
		if (day > 40 && day <= 71) {
			day -= 40;
			StringBuffer fnr = new StringBuffer(value);
			
			if(day < 10){
				fnr.replace(0, 1, "0" + Integer.toString(day));
			}else{
				fnr.replace(0, 2, Integer.toString(day));
			}
			return fnr.toString();
			
		} else if (month > 20 && month <= 32) { 
		// BOST adjustments
			month -= 20;
			StringBuffer fnr = new StringBuffer(value);			
			
			if(month < 10){
				fnr.replace(2, 3, "0" + Integer.toString(month));
			}else{
				fnr.replace(2, 4, Integer.toString(month));
			}
			return fnr.toString();			
		}
		
		//value was neither bostnr nor dnr
		return value;
	}
	
	/**
	 * Returns a 4-digit birth date
	 * @param a fnr, adjusted if it's a bnr or dnr
	 * @return 4 digit birth date, -1 if invalid
	 */
	private static int get4DigitYearOfBirth(String dnrOrBnrAdjustedFnr){
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
