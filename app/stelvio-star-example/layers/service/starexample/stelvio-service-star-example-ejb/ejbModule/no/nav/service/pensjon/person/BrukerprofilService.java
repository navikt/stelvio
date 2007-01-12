/**
 * 
 */
package no.nav.service.pensjon.person;

import no.nav.domain.pensjon.person.Brukerprofil;

/**
 * @author persond3cb2ee15f42
 *
 */
public interface BrukerprofilService {
	
	/**
	 * 
	 * @param fodselsnummer
	 * @return
	 * @throws Exception
	 */
	public Brukerprofil hentBrukerprofil(String fodselsnummer) throws Exception;
	
	
}