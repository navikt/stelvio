package no.stelvio.service.star.example.saksbehandling.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import no.stelvio.domain.star.example.saksbehandling.Saksbehandler;
import no.stelvio.service.star.example.saksbehandling.SaksbehandlerNotFoundException;
import no.stelvio.service.star.example.saksbehandling.SaksbehandlerServiceBi;
import no.stelvio.service.star.example.saksbehandling.to.SaksbehandlerRequest;
import no.stelvio.service.star.example.saksbehandling.to.SaksbehandlerResponse;

/**
 * Retrieves saksbehandler from the Hibernate repository.
 *
 * @author personf8e9850ed756, Accenture
 */
public class RepositorySaksbehandlerService implements SaksbehandlerServiceBi {
	private static final Log log = LogFactory.getLog(RepositorySaksbehandlerService.class);

	/** The repository to retrieve the saksbehandler with. */
	private no.stelvio.repositor.star.example.saksbehandling.SaksbehandlerRepository saksbehandlerRepository;

	public SaksbehandlerResponse hentSaksbehandler(SaksbehandlerRequest request) throws SaksbehandlerNotFoundException {
		Saksbehandler saksbehandler = saksbehandlerRepository.findSaksbehandlerById(request.getSaksbehandlerNr());

		if (log.isDebugEnabled()) {
			log.debug("saksbehandler = " + saksbehandler);
		}

		return new SaksbehandlerResponse(saksbehandler);
	}

	/**
	 * Sets the value of saksbehandlerRepository.
	 *
	 * @param saksbehandlerRepository value to set on saksbehandlerRepository.
	 */
	public void setSaksbehandlerRepository(no.stelvio.repositor.star.example.saksbehandling.SaksbehandlerRepository saksbehandlerRepository) {
		this.saksbehandlerRepository = saksbehandlerRepository;
	}
}
