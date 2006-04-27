package no.trygdeetaten.integration.framework.jms.formatter;

import java.util.Date;

/**
 * Utility for testing formatters tailored for the Oppdrag system.
 * 
 * @author persone5d69f3729a8, Accenture
 * @version $Id: OppdragTestBeanElement.java 2391 2005-06-29 07:49:29Z psa2920 $
 */
public class OppdragTestBeanElement {

	private String val;
	private Date dato;

	/**
	 * Returns the value of the element.
	 * 
	 * @return the value.
	 */
	public String getVal() {
		return val;
	}

	/**
	 * Sets the value of the element.
	 * 
	 * @param string the value.
	 */
	public void setVal(String string) {
		val = string;
	}

	/**
	 * Returns the date of the element.
	 * 
	 * @return the date.
	 */
	public Date getDato() {
		return dato;
	}

	/**
	 * Sets the date of the element.
	 * 
	 * @param date the date.
	 */
	public void setDato(Date date) {
		dato = date;
	}

}
