/**
 * PselvBrukerprofil.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf10631.06 v81706232132
 */

package no.nav.domain.pensjon.person;

import java.io.Serializable;

public class Brukerprofil implements Serializable {
    
	private String fodselsnummer;
	
	private String spesielleBehovKode;

    private String tilrettelagtKommunikasjon;
    

    public Boolean getHarTilrettelagtKommunikasjon()
    {
    	if( tilrettelagtKommunikasjon != null && tilrettelagtKommunikasjon.length() > 0 )
    		return true;
    	return false;
    }
    
	public Brukerprofil( String fodselsnummer ) {
		this.fodselsnummer = fodselsnummer;
    }

    public String getFodselsnummer() {
		return fodselsnummer;
	}

	public String getSpesielleBehovKode() {
        return spesielleBehovKode;
    }

    public void setSpesielleBehovKode(String spesielleBehovKode) {
        this.spesielleBehovKode = spesielleBehovKode;
    }

    public String getTilrettelagtKommunikasjon() {
		return tilrettelagtKommunikasjon;
	}

	public void setTilrettelagtKommunikasjon(String tilrettelagtKommunikasjon) {
		this.tilrettelagtKommunikasjon = tilrettelagtKommunikasjon;
	}

}
