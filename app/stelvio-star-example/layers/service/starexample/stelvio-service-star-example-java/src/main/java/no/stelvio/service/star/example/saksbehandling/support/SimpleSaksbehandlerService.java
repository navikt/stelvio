package no.stelvio.service.star.example.saksbehandling.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import no.stelvio.domain.star.example.saksbehandling.Saksbehandler;
import no.stelvio.service.star.example.exception.DatabaseNotFoundException;
import no.stelvio.service.star.example.saksbehandling.PersonNotFoundException;
import no.stelvio.service.star.example.saksbehandling.SaksbehandlerServiceBi;
import no.stelvio.service.star.example.saksbehandling.to.SaksbehandlerRequest;
import no.stelvio.service.star.example.saksbehandling.to.SaksbehandlerResponse;

/**
 * @author personf8e9850ed756, Accenture
 * @todo write javadoc
 */
public class SimpleSaksbehandlerService implements SaksbehandlerServiceBi {
	private static final Log log = LogFactory.getLog(SimpleSaksbehandlerService.class);

	public SaksbehandlerResponse hentSaksbehandler(SaksbehandlerRequest request) throws PersonNotFoundException {
		Saksbehandler saksbehandler = new Saksbehandler("fornavn", "etternavn", "enhet", 12345678L);

		if (log.isDebugEnabled()) {
			log.debug("saksbehandler = " + saksbehandler);
		}

		return new SaksbehandlerResponse(saksbehandler);
	}
}
