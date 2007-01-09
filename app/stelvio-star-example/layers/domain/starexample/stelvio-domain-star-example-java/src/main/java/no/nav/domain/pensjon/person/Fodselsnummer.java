/**
 * PselvFodselsnummer.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf10631.06 v81706232132
 */

package no.nav.domain.pensjon.person;

import java.io.Serializable;

/**
 * Fodselsnummer (Norwegian birth number/social security number) is a unique business identifier for a Person.
 */
public class Fodselsnummer implements Serializable  {

	private String fodselsnummer;

	/**
	 * @deprecated use constructor with argument
	 */
	public Fodselsnummer() {}
	
	/**
	 * Fodselsnummer is currently not validated.  We can add a call to a validator in the constructor if we are only
	 * going to allow valid numbers being passed around.
	 *  
	 * @param fodselsnummer
	 */
	public Fodselsnummer(String fodselsnummer) {
		this.fodselsnummer = fodselsnummer;
	}

	public String asString() {
		return fodselsnummer;
	}

    public java.lang.String getFodselsnummer() {
        return fodselsnummer;
    }

    /**
     * @deprecated use constructor
     */
    public void setFodselsnummer(java.lang.String fodselsnummer) {
        this.fodselsnummer = fodselsnummer;
    }

}
