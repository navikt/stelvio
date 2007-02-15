/**
 * 
 */
package no.nav.service.pensjon.saksbehandling;

import no.nav.service.pensjon.exception.DatabaseNotFoundException;
import no.stelvio.star.example.saksbehandling.Saksbehandler;

/**
 * @author person4f9bc5bd17cc
 *
 */
public interface SaksbehandlerService {
	Saksbehandler readSaksbehandler(Long saksbehandlernr) throws PersonNotFoundException, DatabaseNotFoundException;
}