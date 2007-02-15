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
	 * @return Response object containing HenvendelseStatistics
	 */
	HenvendelseStatisticsResponse genererHenvendelseStatistikk(HenvendelseStatisticsRequest henvendelseStatisticsRequest);
}
