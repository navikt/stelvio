package no.stelvio.service.star.example.henvendelse;

import java.util.List;

import no.stelvio.domain.star.example.henvendelse.Henvendelse;
import no.stelvio.domain.star.example.person.Fodselsnummer;
import no.stelvio.service.star.example.henvendelse.to.HenvendelseStatistikkRequest;
import no.stelvio.service.star.example.henvendelse.to.HenvendelseStatistikkResponse;


/**
 * Defines an interface for accessing data and functionality related to henvendelse. 
 * 
 * @author personff564022aedd
 */
public interface HenvendelseServiceBi {

	/**
	 * @return Response object containing HenvendelseStatistikk
	 */
	HenvendelseStatistikkResponse genererHenvendelseStatistikk(HenvendelseStatistikkRequest henvendelseStatistikkRequest);

	List<Henvendelse> hentHenvendelseList(Fodselsnummer fodselsnummer) throws Exception;
}
