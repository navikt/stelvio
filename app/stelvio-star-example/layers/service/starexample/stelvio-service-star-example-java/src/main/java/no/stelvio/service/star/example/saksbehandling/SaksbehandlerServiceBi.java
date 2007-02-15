/**
 * 
 */
package no.stelvio.service.star.example.saksbehandling;

import no.stelvio.service.star.example.exception.DatabaseNotFoundException;
import no.stelvio.service.star.example.saksbehandling.to.SaksbehandlerRequest;
import no.stelvio.service.star.example.saksbehandling.to.SaksbehandlerResponse;

/**
 * @author person4f9bc5bd17cc
 *
 */
public interface SaksbehandlerServiceBi {
	SaksbehandlerResponse hentSaksbehandler(SaksbehandlerRequest request) throws PersonNotFoundException;
}