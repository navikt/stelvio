package no.nav.service.pensjon.saksbehandling.support;

import no.nav.domain.pensjon.saksbehandling.SaksbehandlerDO;
import no.nav.service.pensjon.exception.DatabaseNotFoundException;
import no.nav.service.pensjon.person.exception.PersonNotFoundException;
import no.nav.service.pensjon.saksbehandling.SaksbehandlerService;

/**
 * @author personf8e9850ed756, Accenture
 * @todo write javadoc
 */
public class SimpleSaksbehandlerService implements SaksbehandlerService {
	public SaksbehandlerDO readSaksbehandler(Long saksbehandlernr) throws PersonNotFoundException, DatabaseNotFoundException {
		return new SaksbehandlerDO();
	}
}
