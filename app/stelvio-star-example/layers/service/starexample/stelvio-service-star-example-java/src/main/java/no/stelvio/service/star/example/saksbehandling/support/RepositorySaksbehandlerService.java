package no.stelvio.service.star.example.saksbehandling.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import no.stelvio.domain.star.example.henvendelse.Fagomrade;
import no.stelvio.domain.star.example.henvendelse.HenvendelseStatisticsCriteria;
import no.stelvio.domain.star.example.henvendelse.Spesifikasjon;
import no.stelvio.domain.star.example.henvendelse.Tidsperiode;
import no.stelvio.domain.star.example.saksbehandling.Saksbehandler;
import no.stelvio.repository.star.example.saksbehandling.SaksbehandlerRepository;
import no.stelvio.service.star.example.henvendelse.HenvendelseServiceBi;
import no.stelvio.service.star.example.henvendelse.to.HenvendelseStatisticsRequest;
import no.stelvio.service.star.example.henvendelse.to.HenvendelseStatisticsResponse;
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
	private SaksbehandlerRepository saksbehandlerRepository;
	/** The service used to generate statistics of henvendelser. */
	private HenvendelseServiceBi henvendelseService;

	/** {@inheritDoc} */
	public SaksbehandlerResponse hentSaksbehandler(SaksbehandlerRequest request) throws SaksbehandlerNotFoundException {
		Saksbehandler saksbehandler = saksbehandlerRepository.findSaksbehandlerById(request.getSaksbehandlerNr());

		HenvendelseStatisticsCriteria criteria = new HenvendelseStatisticsCriteria(
				"enhetId", Tidsperiode.SISTE_4_UKER, Fagomrade.PENSJON, Spesifikasjon.ANTALL);
		HenvendelseStatisticsResponse statisticsResponse =
				henvendelseService.genererHenvendelseStatistikk(new HenvendelseStatisticsRequest(criteria));

		if (log.isDebugEnabled()) {
			log.debug("saksbehandler = " + saksbehandler);
			log.debug("statistics = " + statisticsResponse.getStatistics());
		}

		return new SaksbehandlerResponse(saksbehandler);
	}

	/**
	 * Sets the value of saksbehandlerRepository.
	 *
	 * @param saksbehandlerRepository value to set on saksbehandlerRepository.
	 */
	public void setSaksbehandlerRepository(no.stelvio.repository.star.example.saksbehandling.SaksbehandlerRepository saksbehandlerRepository) {
		this.saksbehandlerRepository = saksbehandlerRepository;
	}

	/**
	 * Sets the value of henvendelseService.
	 *
	 * @param henvendelseService value to set on henvendelseService.
	 */
	public void setHenvendelseService(HenvendelseServiceBi henvendelseService) {
		this.henvendelseService = henvendelseService;
	}
}


