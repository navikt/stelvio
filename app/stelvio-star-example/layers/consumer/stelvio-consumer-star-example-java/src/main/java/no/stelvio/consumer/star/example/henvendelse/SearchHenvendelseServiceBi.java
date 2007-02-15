package no.stelvio.consumer.star.example.henvendelse;

import no.stelvio.consumer.star.example.henvendelse.to.SearchHenvendelseRequest;
import no.stelvio.consumer.star.example.henvendelse.to.SearchHenvendelseResponse;

/**
 * Defines an interface for accessing data and functionality related to henvendelse.
 *
 * @author personff564022aedd
 */
public interface SearchHenvendelseServiceBi {

	/**
	 * @return Response object containing HenvendelseStatistics
	 */
	SearchHenvendelseResponse genererHenvendelseStatistikk(SearchHenvendelseRequest searchHenvendelseRequest);
}
