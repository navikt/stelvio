/**
 * 
 */
package no.nav.presentation.pensjon.saksbehandling.stelvio.service;

import no.nav.presentation.pensjon.saksbehandling.stelvio.dao.SaksbehandlerDO;
import no.nav.service.pensjon.exception.DatabaseNotFoundException;
import no.nav.service.pensjon.person.exception.PersonNotFoundException;

/**
 * @author person4f9bc5bd17cc
 *
 */
public interface SaksbehandlerService {
	public SaksbehandlerDO readSaksbehandler(Long saksbehandlernr) throws PersonNotFoundException, DatabaseNotFoundException;
}