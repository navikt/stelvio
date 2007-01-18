/**
 * 
 */
package no.nav.service.pensjon.saksbehandling;

import no.nav.domain.pensjon.saksbehandling.SaksbehandlerDO;
import no.nav.service.pensjon.exception.DatabaseNotFoundException;
import no.nav.service.pensjon.person.exception.PersonNotFoundException;

/**
 * @author person4f9bc5bd17cc
 *
 */
public interface SaksbehandlerService {
	SaksbehandlerDO readSaksbehandler(Long saksbehandlernr) throws PersonNotFoundException, DatabaseNotFoundException;
}