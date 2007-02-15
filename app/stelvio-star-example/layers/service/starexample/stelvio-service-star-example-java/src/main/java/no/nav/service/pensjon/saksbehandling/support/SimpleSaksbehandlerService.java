package no.nav.service.pensjon.saksbehandling.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import no.nav.domain.pensjon.saksbehandling.Saksbehandler;
import no.nav.service.pensjon.exception.DatabaseNotFoundException;
import no.nav.service.pensjon.saksbehandling.PersonNotFoundException;
import no.nav.service.pensjon.saksbehandling.SaksbehandlerService;

/**
 * @author personf8e9850ed756, Accenture
 * @todo write javadoc
 */
public class SimpleSaksbehandlerService implements SaksbehandlerService {
	private static final Log log = LogFactory.getLog(SimpleSaksbehandlerService.class);

	public Saksbehandler readSaksbehandler(Long saksbehandlernr) throws PersonNotFoundException, DatabaseNotFoundException {
		Saksbehandler saksbehandler = new Saksbehandler("fornavn", "etternavn", "enhet", 12345678L);

		if (log.isDebugEnabled()) {
			log.debug("saksbehandler = " + saksbehandler);
		}
		
		return saksbehandler;
	}
}
