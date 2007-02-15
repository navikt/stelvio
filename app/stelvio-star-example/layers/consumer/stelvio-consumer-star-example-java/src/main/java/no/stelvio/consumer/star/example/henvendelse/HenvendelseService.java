package no.stelvio.consumer.star.example.henvendelse;

import java.util.List;

import no.stelvio.consumer.star.example.henvendelse.to.HenvendelseStatistikkRequest;
import no.stelvio.consumer.star.example.henvendelse.to.HenvendelseStatistikkResponse;

/**
 * Defines an interface for accessing data and functionality related to henvendelse.
 *
 * @author personff564022aedd
 */
public interface HenvendelseService {

	/**
	 * @return Response object containing HenvendelseStatistikk
	 */
	HenvendelseStatistikkResponse genererHenvendelseStatistikk(HenvendelseStatistikkRequest henvendelseStatistikkRequest);

	List<Henvendelse> hentHenvendelseList(Fodselsnummer fodselsnummer) throws Exception;
}
