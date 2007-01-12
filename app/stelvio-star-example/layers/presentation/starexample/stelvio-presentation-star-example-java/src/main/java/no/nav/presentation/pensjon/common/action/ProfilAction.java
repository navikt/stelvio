package no.nav.presentation.pensjon.common.action;

import no.nav.domain.pensjon.person.Brukerprofil;
import no.nav.service.pensjon.person.BrukerprofilService;

public class ProfilAction {

	private BrukerprofilService brukerprofilService;

	

	/**
	 * The method takes 'fødselsnummer' as a parameter, calls BrukerprofilService.hentBrukerprofil
	 * with 'fødselsnummer' as a parameter. It return the Brukerprofil domain object.
	 * 
	 * @param fodselsnummer
	 * @return
	 */
	public Brukerprofil hentBrukerprofil( String fodselsnummer )
		throws Exception
	{
		return brukerprofilService.hentBrukerprofil( fodselsnummer );
	}
	

	
	public void setBrukerprofilService(BrukerprofilService brukerprofilService) {
		this.brukerprofilService = brukerprofilService;
	}
	
}
