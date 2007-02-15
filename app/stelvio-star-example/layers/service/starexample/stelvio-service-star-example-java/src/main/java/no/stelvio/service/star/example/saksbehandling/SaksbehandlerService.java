/**
 * 
 */
package no.stelvio.service.star.example.saksbehandling;

import no.stelvio.service.star.example.exception.DatabaseNotFoundException;
import no.stelvio.star.example.saksbehandling.Saksbehandler;

/**
 * @author person4f9bc5bd17cc
 *
 */
public interface SaksbehandlerService {
	Saksbehandler readSaksbehandler(Long saksbehandlernr) throws PersonNotFoundException, DatabaseNotFoundException;
}