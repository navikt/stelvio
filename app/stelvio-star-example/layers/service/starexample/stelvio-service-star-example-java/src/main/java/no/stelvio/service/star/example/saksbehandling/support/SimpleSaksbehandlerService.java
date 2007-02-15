package no.stelvio.service.star.example.saksbehandling.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import no.stelvio.service.star.example.exception.DatabaseNotFoundException;
import no.stelvio.service.star.example.saksbehandling.PersonNotFoundException;
import no.stelvio.service.star.example.saksbehandling.SaksbehandlerService;
import no.stelvio.star.example.saksbehandling.Saksbehandler;

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
