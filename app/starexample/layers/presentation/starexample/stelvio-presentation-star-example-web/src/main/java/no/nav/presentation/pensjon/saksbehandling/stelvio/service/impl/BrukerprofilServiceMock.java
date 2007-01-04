/**
 * 
 */
package no.nav.presentation.pensjon.saksbehandling.stelvio.service.impl;

import no.nav.presentation.pensjon.saksbehandling.stelvio.dao.BrukerprofilDO;
import no.nav.presentation.pensjon.saksbehandling.stelvio.exceptions.DatabaseNotFoundException;
import no.nav.presentation.pensjon.saksbehandling.stelvio.exceptions.PersonNotFoundException;
import no.nav.presentation.pensjon.saksbehandling.stelvio.service.BrukerprofilService;

/**
 * @author person4f9bc5bd17cc
 *
 */
public class BrukerprofilServiceMock implements BrukerprofilService {
	private static BrukerprofilDO ola = new BrukerprofilDO();
	private static BrukerprofilDO ole = new BrukerprofilDO();
	private static BrukerprofilDO kirsten = new BrukerprofilDO();
	private static BrukerprofilDO per = new BrukerprofilDO();
	
	static {
		ola.setFnr("12345678901");
		ole.setFnr("22222222222");
		kirsten.setFnr("33333333333");
		per.setFnr("44444444444");
	}
	
	/**
	 * TODO: Document me
	 */
	public BrukerprofilDO readBrukerprofil(String fnr) throws DatabaseNotFoundException, PersonNotFoundException {
		BrukerprofilDO brukerprofil = new BrukerprofilDO();
		
		if (fnr.equals("12345678901")) {
			brukerprofil = ola;
		}
		else if (fnr.equals("22222222222")) {
			brukerprofil = ole;
		}
		else if (fnr.equals("33333333333")) {
			brukerprofil = kirsten;
		}
		else if (fnr.equals("44444444444")) {
			brukerprofil = per;
		}
		else {
			throw new PersonNotFoundException("Fant ingen personer med fødselsnummer "+fnr+".");
		}
		
		return brukerprofil;
	}

	/**
	 * TODO: Document me
	 */
	public void updateBrukerprofil(BrukerprofilDO brukerprofil) throws DatabaseNotFoundException {
		if (brukerprofil.getFnr().equals("12345678901")) {
			ola = brukerprofil;
		}
		else if (brukerprofil.getFnr().equals("22222222222")) {
			ole = brukerprofil;
		}
		else if (brukerprofil.getFnr().equals("33333333333")) {
			kirsten = brukerprofil;
		}
		else if (brukerprofil.getFnr().equals("44444444444")) {
			per = brukerprofil;
		}
	}
}