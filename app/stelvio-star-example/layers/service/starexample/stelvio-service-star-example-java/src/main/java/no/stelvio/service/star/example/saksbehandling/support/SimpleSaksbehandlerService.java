package no.stelvio.service.star.example.saksbehandling.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import no.stelvio.domain.star.example.saksbehandling.Saksbehandler;
import no.stelvio.service.star.example.saksbehandling.SaksbehandlerNotFoundException;
import no.stelvio.service.star.example.saksbehandling.SaksbehandlerServiceBi;
import no.stelvio.service.star.example.saksbehandling.to.SaksbehandlerRequest;
import no.stelvio.service.star.example.saksbehandling.to.SaksbehandlerResponse;

/**
 * Stub implementation of service for working with saksbehandler.
 * 
 * @author personf8e9850ed756, Accenture
 */
public class SimpleSaksbehandlerService implements SaksbehandlerServiceBi {
	private static final Log log = LogFactory.getLog(SimpleSaksbehandlerService.class);

	/** {@inheritDoc} */
	public SaksbehandlerResponse hentSaksbehandler(SaksbehandlerRequest request) throws SaksbehandlerNotFoundException {
		Saksbehandler saksbehandler = new Saksbehandler(12345678L, "fornavn", "etternavn", "enhet");

		if (log.isDebugEnabled()) {
			log.debug("saksbehandler = " + saksbehandler);
		}

		return new SaksbehandlerResponse(saksbehandler);
	}
}
