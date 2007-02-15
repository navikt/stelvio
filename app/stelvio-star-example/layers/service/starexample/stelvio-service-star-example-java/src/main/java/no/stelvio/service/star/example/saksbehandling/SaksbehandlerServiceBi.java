/**
 * 
 */
package no.stelvio.service.star.example.saksbehandling;

import no.stelvio.domain.star.example.saksbehandling.Saksbehandler;
import no.stelvio.service.star.example.exception.DatabaseNotFoundException;

/**
 * @author person4f9bc5bd17cc
 *
 */
public interface SaksbehandlerServiceBi {
	Saksbehandler readSaksbehandler(Long saksbehandlernr) throws PersonNotFoundException, DatabaseNotFoundException;
}