package no.stelvio.service.star.example.henvendelse;

import no.stelvio.service.star.example.henvendelse.to.HenvendelseStatisticsRequest;
import no.stelvio.service.star.example.henvendelse.to.HenvendelseStatisticsResponse;


/**
 * Defines an interface for accessing data and functionality related to henvendelse. 
 * 
 * @author personff564022aedd
 */
public interface HenvendelseServiceBi {
	/**
	 * 
	 * @param henvendelseStatisticsRequest request object
	 * @return Response object containing <code>HenvendelseStatistics</code>
	 * @see no.stelvio.domain.star.example.henvendelse.HenvendelseStatistics
	 */
	HenvendelseStatisticsResponse genererHenvendelseStatistikk(HenvendelseStatisticsRequest henvendelseStatisticsRequest);
}
