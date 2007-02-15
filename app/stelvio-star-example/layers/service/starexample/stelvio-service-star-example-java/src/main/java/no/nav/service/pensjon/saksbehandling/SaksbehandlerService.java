/**
 * 
 */
package no.nav.service.pensjon.saksbehandling;

import no.nav.domain.pensjon.saksbehandling.Saksbehandler;
import no.nav.service.pensjon.exception.DatabaseNotFoundException;

/**
 * @author person4f9bc5bd17cc
 *
 */
public interface SaksbehandlerService {
	Saksbehandler readSaksbehandler(Long saksbehandlernr) throws PersonNotFoundException, DatabaseNotFoundException;
}