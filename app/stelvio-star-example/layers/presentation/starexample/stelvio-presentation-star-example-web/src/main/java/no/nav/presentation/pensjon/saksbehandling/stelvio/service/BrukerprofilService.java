/**
 * 
 */
package no.nav.presentation.pensjon.saksbehandling.stelvio.service;

import no.nav.presentation.pensjon.saksbehandling.stelvio.dao.BrukerprofilDO;
import no.nav.presentation.pensjon.saksbehandling.stelvio.exceptions.DatabaseNotFoundException;
import no.nav.presentation.pensjon.saksbehandling.stelvio.exceptions.PersonNotFoundException;

/**
 * @author person4f9bc5bd17cc
 *
 */
public interface BrukerprofilService {
	public BrukerprofilDO readBrukerprofil(String fnr) throws PersonNotFoundException, DatabaseNotFoundException;
	public void updateBrukerprofil(BrukerprofilDO brukerprofile) throws DatabaseNotFoundException;
}